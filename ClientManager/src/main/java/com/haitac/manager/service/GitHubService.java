package com.haitac.manager.service;

import com.haitac.manager.model.ManagerConfig;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Tương tác với GitHub REST API v3
 * Hỗ trợ kiểm tra kết nối, tải (pull) và đẩy (push) tệp license.
 */
public class GitHubService {

    private final ManagerConfig config;
    private String lastFileSha = null;

    public GitHubService(ManagerConfig config) {
        this.config = config;
    }

    public String getLastFileSha() {
        return lastFileSha;
    }

    public void setLastFileSha(String lastFileSha) {
        this.lastFileSha = lastFileSha;
    }

    private HttpURLConnection createConnection(String endpoint, String method) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(20000);
        conn.setRequestProperty("User-Agent", "HaiTac-ClientManager-Tool");
        conn.setRequestProperty("Accept", "application/vnd.github+json");

        String token = config.getGithubToken();
        if (token != null && !token.trim().isEmpty()) {
            token = token.trim();
            if (token.startsWith("ghp_") || token.startsWith("github_pat_") || token.startsWith("gho_")) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                conn.setRequestProperty("Authorization", "token " + token);
            }
        }
        return conn;
    }

    public String testConnection() throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s",
                config.getGithubOwner().trim(), config.getGithubRepo().trim());
        HttpURLConnection conn = createConnection(url, "GET");
        int code = conn.getResponseCode();
        if (code == 200) {
            return "Kết nối GitHub thành công!";
        } else if (code == 404) {
            return "Không tìm thấy Repository (404). Kiểm tra lại Owner và Repo.";
        } else if (code == 401) {
            return "Token không hợp lệ hoặc đã hết hạn (401 Unauthorized).";
        } else {
            return "Lỗi phản hồi HTTP: " + code;
        }
    }

    @SuppressWarnings("unchecked")
    public String pullLicenseEncrypted() throws Exception {
        String endpoint = String.format("https://api.github.com/repos/%s/%s/contents/%s?ref=%s",
                config.getGithubOwner().trim(),
                config.getGithubRepo().trim(),
                config.getGithubFilePath().trim(),
                config.getGithubBranch().trim());

        HttpURLConnection conn = createConnection(endpoint, "GET");
        int code = conn.getResponseCode();

        if (code == 200) {
            String jsonResp = readStream(conn.getInputStream());
            Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(jsonResp);
            this.lastFileSha = (String) map.get("sha");
            String contentBase64 = (String) map.get("content");
            if (contentBase64 != null) {
                return contentBase64.replaceAll("\\s+", "");
            }
            return "";
        } else if (code == 404) {
            // Fallback tải raw URL
            return pullFromRawUrl();
        } else {
            throw new Exception("GitHub API trả về mã lỗi: " + code);
        }
    }

    public String pullFromRawUrl() throws Exception {
        String rawUrl = String.format("https://raw.githubusercontent.com/%s/%s/%s/%s?t=%d",
                config.getGithubOwner().trim(),
                config.getGithubRepo().trim(),
                config.getGithubBranch().trim(),
                config.getGithubFilePath().trim(),
                System.currentTimeMillis());

        URL url = new URL(rawUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        conn.setRequestProperty("User-Agent", "HaiTac-ClientManager-Tool");

        int code = conn.getResponseCode();
        if (code == 200) {
            return readStream(conn.getInputStream()).trim();
        } else {
            throw new Exception("Không thể tải raw file: HTTP " + code);
        }
    }

    public String pushLicenseEncrypted(String base64Content, String commitMessage) throws Exception {
        String token = config.getGithubToken();
        if (token == null || token.trim().isEmpty()) {
            throw new Exception("Bạn chưa nhập GitHub Token trong Cài đặt! Cần Token để đẩy file lên GitHub.");
        }

        String endpoint = String.format("https://api.github.com/repos/%s/%s/contents/%s",
                config.getGithubOwner().trim(),
                config.getGithubRepo().trim(),
                config.getGithubFilePath().trim());

        if (lastFileSha == null || lastFileSha.isEmpty()) {
            try {
                HttpURLConnection checkConn = createConnection(endpoint + "?ref=" + config.getGithubBranch().trim(), "GET");
                if (checkConn.getResponseCode() == 200) {
                    String jsonResp = readStream(checkConn.getInputStream());
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(jsonResp);
                    this.lastFileSha = (String) map.get("sha");
                }
            } catch (Exception ignored) {}
        }

        Map<String, Object> payload = new LinkedHashMap<String, Object>();
        payload.put("message", commitMessage != null && !commitMessage.trim().isEmpty()
                ? commitMessage.trim() : "Update license.enc via ClientManager");
        payload.put("content", base64Content);
        payload.put("branch", config.getGithubBranch().trim());
        if (lastFileSha != null && !lastFileSha.isEmpty()) {
            payload.put("sha", lastFileSha);
        }

        String payloadJson = JsonHelper.toJson(payload);

        HttpURLConnection conn = createConnection(endpoint, "PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payloadJson.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int code = conn.getResponseCode();
        if (code == 200 || code == 201) {
            String jsonResp = readStream(conn.getInputStream());
            @SuppressWarnings("unchecked")
            Map<String, Object> respMap = (Map<String, Object>) JsonHelper.parse(jsonResp);
            if (respMap.containsKey("content")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> contentMap = (Map<String, Object>) respMap.get("content");
                this.lastFileSha = (String) contentMap.get("sha");
            }
            return "Đã đẩy lên GitHub thành công (HTTP " + code + ")!";
        } else {
            String err = readStream(conn.getErrorStream());
            throw new Exception("Lỗi khi đẩy lên GitHub (HTTP " + code + "): " + err);
        }
    }

    private String readStream(InputStream in) throws Exception {
        if (in == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
