package com.haitac.manager.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Quản lý cấu hình cục bộ của tool ClientManager (lưu trong config.properties)
 */
public class ManagerConfig {

    private String githubOwner;
    private String githubRepo;
    private String githubBranch;
    private String githubFilePath;
    private String githubToken;
    private String encryptionKey;
    private boolean autoPullOnStartup;

    private static final String CONFIG_FILE = "config.properties";

    public ManagerConfig() {
        this.githubOwner = "namtr0002";
        this.githubRepo = "HaiTacClientMOD";
        this.githubBranch = "main";
        this.githubFilePath = "data/license.enc";
        this.githubToken = "";
        this.encryptionKey = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
        this.autoPullOnStartup = true;
    }

    public static ManagerConfig load() {
        ManagerConfig cfg = new ManagerConfig();
        File f = new File(CONFIG_FILE);
        if (f.exists()) {
            try (InputStream in = new FileInputStream(f);
                 Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                Properties prop = new Properties();
                prop.load(reader);
                cfg.githubOwner = prop.getProperty("github.owner", cfg.githubOwner);
                cfg.githubRepo = prop.getProperty("github.repo", cfg.githubRepo);
                cfg.githubBranch = prop.getProperty("github.branch", cfg.githubBranch);
                cfg.githubFilePath = prop.getProperty("github.filepath", cfg.githubFilePath);
                cfg.githubToken = prop.getProperty("github.token", cfg.githubToken);
                cfg.encryptionKey = prop.getProperty("encryption.key", cfg.encryptionKey);
                cfg.autoPullOnStartup = Boolean.parseBoolean(prop.getProperty("app.autopull", "true"));
            } catch (Exception e) {
                System.err.println("Không thể đọc config.properties: " + e.getMessage());
            }
        }
        return cfg;
    }

    public void save() {
        try (OutputStream out = new FileOutputStream(CONFIG_FILE);
             Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            Properties prop = new Properties();
            prop.setProperty("github.owner", githubOwner != null ? githubOwner : "");
            prop.setProperty("github.repo", githubRepo != null ? githubRepo : "");
            prop.setProperty("github.branch", githubBranch != null ? githubBranch : "main");
            prop.setProperty("github.filepath", githubFilePath != null ? githubFilePath : "data/license.enc");
            prop.setProperty("github.token", githubToken != null ? githubToken : "");
            prop.setProperty("encryption.key", encryptionKey != null ? encryptionKey : "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026");
            prop.setProperty("app.autopull", String.valueOf(autoPullOnStartup));
            prop.store(writer, "Cấu hình Hải Tặc Client Manager Tool");
        } catch (Exception e) {
            System.err.println("Không thể ghi config.properties: " + e.getMessage());
        }
    }

    public String getGithubOwner() { return githubOwner; }
    public void setGithubOwner(String githubOwner) { this.githubOwner = githubOwner; }

    public String getGithubRepo() { return githubRepo; }
    public void setGithubRepo(String githubRepo) { this.githubRepo = githubRepo; }

    public String getGithubBranch() { return githubBranch; }
    public void setGithubBranch(String githubBranch) { this.githubBranch = githubBranch; }

    public String getGithubFilePath() { return githubFilePath; }
    public void setGithubFilePath(String githubFilePath) { this.githubFilePath = githubFilePath; }

    public String getGithubToken() { return githubToken; }
    public void setGithubToken(String githubToken) { this.githubToken = githubToken; }

    public String getEncryptionKey() { return encryptionKey; }
    public void setEncryptionKey(String encryptionKey) { this.encryptionKey = encryptionKey; }

    public boolean isAutoPullOnStartup() { return autoPullOnStartup; }
    public void setAutoPullOnStartup(boolean autoPullOnStartup) { this.autoPullOnStartup = autoPullOnStartup; }
}
