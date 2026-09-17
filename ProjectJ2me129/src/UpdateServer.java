import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * UpdateServer - Quản lý danh sách Server game Hải Tặc
 * Mặc định list EMPTY, chỉ nạp server khi KeyAuthManager xác thực thành công.
 * Không cho phép tự ý nhập IP tùy tiện.
 */
public class UpdateServer {
    public static final String DEFAULT_SERVER_LIST_URL = ClientConfig.SERVER_LIST_URL;
    public static final String FALLBACK_SERVER_LIST_URL =
        "https://raw.githubusercontent.com/thachdeptrai/listIP/main/iphtth.txt";

    public static List<String> serverHosts = new ArrayList<>();      // host:port
    public static List<String> serverNames = new ArrayList<>();      // display names
    public static List<Integer> serverLang = new ArrayList<>();      // language index
    public static List<Integer> serverFrameId = new ArrayList<>();   // frame id 0..3

    /**
     * Xóa sạch danh sách server (trạng thái mặc định khi chưa kích hoạt bản quyền)
     */
    public static void clearServers() {
        serverHosts.clear();
        serverNames.clear();
        serverLang.clear();
        serverFrameId.clear();
        GameCanvas.strListServer = new String[0][];
        GameCanvas.hostServer = "";
        GameCanvas.portServer = 0;
        GameCanvas.IndexServer = 0;

        if (GameCanvas.loginScr != null) {
            GameCanvas.loginScr.onServersUpdated();
        }
        if (GameCanvas.fristLoginScr != null) {
            GameCanvas.fristLoginScr.onServersUpdated();
        }
    }

    /**
     * Chỉ nạp server khi đã được xác thực mã định danh chuẩn
     */
    public static void loadServers() {
        if (!KeyAuthManager.isAuthorized) {
            clearServers();
            return;
        }
        loadServersFromRemote();
    }

    /**
     * Nạp server riêng được cấu hình cho Key của Client này
     */
    public static void applyCustomServers(List<String> entries) {
        if (!KeyAuthManager.isAuthorized) {
            clearServers();
            return;
        }
        if (entries == null || entries.isEmpty()) {
            loadServersFromRemote();
            return;
        }

        serverHosts.clear();
        serverNames.clear();
        serverLang.clear();
        serverFrameId.clear();

        for (String raw : entries) {
            parseAndAddServerEntry(raw);
        }

        if (!serverHosts.isEmpty()) {
            buildServerListForLanguage();
            restoreIndexServer();
        } else {
            loadServersFromRemote();
        }
    }

    /**
     * Nạp server danh sách chung từ remote GitHub
     */
    public static void loadServersFromRemote() {
        serverHosts.clear();
        serverNames.clear();
        serverLang.clear();
        serverFrameId.clear();

        BufferedReader br = null;
        boolean loaded = false;

        // Thử tải từ URL chính thức của repo
        try {
            String query = "?client_id=" + ClientConfig.CLIENT_ID + "&v=" + ClientConfig.CLIENT_VERSION + "&t=" + System.currentTimeMillis();
            URL url = new URL(DEFAULT_SERVER_LIST_URL + query);
            br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] entries = line.split(",");
                for (String entryRaw : entries) {
                    if (parseAndAddServerEntry(entryRaw)) {
                        loaded = true;
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            try { if (br != null) br.close(); } catch (Exception ignored) {}
        }

        // Nếu URL chính không tải được thì fallback sang URL dự phòng
        if (!loaded) {
            try {
                URL url = new URL(FALLBACK_SERVER_LIST_URL + "?t=" + System.currentTimeMillis());
                br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] entries = line.split(",");
                    for (String entryRaw : entries) {
                        if (parseAndAddServerEntry(entryRaw)) {
                            loaded = true;
                        }
                    }
                }
            } catch (Exception ignored) {
            } finally {
                try { if (br != null) br.close(); } catch (Exception ignored) {}
            }
        }

        loadCustomServersFromRms();

        if (!serverHosts.isEmpty()) {
            buildServerListForLanguage();
            restoreIndexServer();
        }
    }

    public static void loadCustomServersFromRms() {
        if (!KeyAuthManager.isAuthorized) return;
        try {
            byte[] savedData = CRes.loadRMS("RMS_CUSTOM_SERVERS");
            if (savedData != null && savedData.length > 0) {
                String saved = new String(savedData, "UTF-8");
                String[] entries = saved.split(";");
                for (String entry : entries) {
                    parseAndAddServerEntry(entry);
                }
            }
        } catch (Exception ignored) {}
    }

    public static boolean parseAndAddCustomServer(String input) {
        if (!KeyAuthManager.checkAuthorizedOrNotice()) return false;
        if (input == null || input.trim().isEmpty()) return false;
        String[] parts = input.trim().split(":");
        if (parts.length < 2) return false;
        String name = parts[0].trim();
        String host = parts[1].trim();
        int port = 2229;
        if (parts.length >= 3) {
            try { port = Integer.parseInt(parts[2].trim()); } catch (Exception e) { port = 2229; }
        }
        if (host.equalsIgnoreCase("localhost")) host = "127.0.0.1";
        String hostPort = host + ":" + port;
        int existingIdx = serverHosts.indexOf(hostPort);
        if (existingIdx >= 0) {
            serverNames.set(existingIdx, name);
            setServerIndex(existingIdx);
        } else {
            serverHosts.add(hostPort);
            serverNames.add(name);
            serverLang.add(Integer.valueOf(GameCanvas.language));
            serverFrameId.add(Integer.valueOf(0));
            setServerIndex(serverHosts.size() - 1);
        }
        try {
            byte[] savedData = CRes.loadRMS("RMS_CUSTOM_SERVERS");
            String saved = (savedData != null) ? new String(savedData, "UTF-8") : "";
            String newEntry = name + ":" + hostPort + ":" + GameCanvas.language + ":0";
            if (!saved.contains(hostPort)) {
                saved = (saved.length() > 0 ? saved + ";" : "") + newEntry;
                CRes.saveRMS("RMS_CUSTOM_SERVERS", saved.getBytes("UTF-8"));
            }
        } catch (Exception ignored) {}
        buildServerListForLanguage();
        return true;
    }

    public static boolean parseAndAddServerEntry(String entryRaw) {
        if (entryRaw == null) return false;
        String entry = entryRaw.trim();
        if (entry.isEmpty() || entry.startsWith("#")) return false;

        String[] parts = entry.split(":");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

        String name;
        String host;
        int port = 2229;
        int lang = 0;
        int frameId = 0;

        if (parts.length >= 3) {
            name = parts[0];
            host = parts[1];
            try { port = Integer.parseInt(parts[2]); } catch (Exception e) { port = 2229; }
            if (parts.length >= 4) {
                try { lang = Integer.parseInt(parts[3]); } catch (Exception e) { lang = 0; }
            }
            if (parts.length >= 5) {
                try { frameId = Integer.parseInt(parts[4]); } catch (Exception e) { frameId = 0; }
            }
        } else if (parts.length == 2) {
            if (parts[1].matches("\\d+")) {
                host = parts[0];
                try { port = Integer.parseInt(parts[1]); } catch (Exception e) { port = 2229; }
                name = host;
            } else {
                name = parts[0];
                host = parts[1];
            }
        } else {
            host = parts[0];
            name = host;
        }

        host = host.trim();
        if (host.isEmpty()) return false;
        if (host.equalsIgnoreCase("localhost")) host = "127.0.0.1";

        frameId = Math.max(0, Math.min(3, frameId));
        if (port <= 0 || port > 65535) port = 2229;

        String hostPort = host + ":" + port;
        serverHosts.add(hostPort);
        serverNames.add(name.length() > 0 ? name : hostPort);
        serverLang.add(lang);
        serverFrameId.add(frameId);
        return true;
    }

    public static boolean isServerNew(int globalIndex) {
        if (globalIndex >= 0 && globalIndex < serverFrameId.size()) {
            Integer fid = serverFrameId.get(globalIndex);
            if (fid != null && fid == 1) return true;
        }
        if (globalIndex >= 0 && globalIndex < serverNames.size()) {
            String name = serverNames.get(globalIndex);
            if (name != null) {
                String upper = name.toUpperCase();
                if (upper.contains("[NEW]") || upper.contains("(NEW)") || upper.contains(" NEW") || upper.contains("MỚI")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int relativeToGlobal(int relativeIndex, int lang) {
        int count = 0;
        for (int i = 0; i < serverHosts.size(); i++) {
            int lg = (i < serverLang.size() && serverLang.get(i) != null) ? serverLang.get(i) : 0;
            if (lg == lang) {
                if (count == relativeIndex) {
                    return i;
                }
                count++;
            }
        }
        for (int i = 0; i < serverHosts.size(); i++) {
            return i;
        }
        return 0;
    }

    public static int globalToRelative(int globalIndex) {
        if (globalIndex < 0 || globalIndex >= serverHosts.size()) {
            return 0;
        }
        int lang = (globalIndex < serverLang.size() && serverLang.get(globalIndex) != null) ? serverLang.get(globalIndex) : 0;
        int relativeIndex = 0;
        for (int i = 0; i < globalIndex; i++) {
            int lg = (i < serverLang.size() && serverLang.get(i) != null) ? serverLang.get(i) : 0;
            if (lg == lang) {
                relativeIndex++;
            }
        }
        return relativeIndex;
    }

    public static String getCurrentServerName() {
        int relIdx = globalToRelative(GameCanvas.IndexServer);
        if (GameCanvas.strListServer != null
                && GameCanvas.language >= 0
                && GameCanvas.language < GameCanvas.strListServer.length
                && GameCanvas.strListServer[GameCanvas.language] != null
                && relIdx >= 0
                && relIdx < GameCanvas.strListServer[GameCanvas.language].length) {
            return GameCanvas.strListServer[GameCanvas.language][relIdx];
        }
        return getServer(GameCanvas.IndexServer);
    }

    public static void setServerIndex(int index) {
        if (index >= 0 && index < serverHosts.size()) {
            GameCanvas.IndexServer = index;
            GameCanvas.hostServer = getHost(index);
            GameCanvas.portServer = getPort(index);
            if (GameCanvas.loginScr != null) {
                GameCanvas.loginScr.onServersUpdated();
            }
            if (GameCanvas.fristLoginScr != null) {
                GameCanvas.fristLoginScr.onServersUpdated();
            }
        }
    }

    private static void buildServerListForLanguage() {
        int maxLang = GameCanvas.language;
        for (Integer lg : serverLang) if (lg != null) maxLang = Math.max(maxLang, lg);

        String[][] arr = new String[maxLang + 1][];
        for (int i = 0; i <= maxLang; i++) {
            arr[i] = new String[0];
        }

        for (int langIdx = 0; langIdx <= maxLang; langIdx++) {
            List<String> namesForLang = new ArrayList<>();
            for (int i = 0; i < serverNames.size(); i++) {
                int lg = serverLang.get(i) != null ? serverLang.get(i) : 0;
                if (lg == langIdx) namesForLang.add(serverNames.get(i));
            }
            arr[langIdx] = namesForLang.toArray(new String[0]);
        }

        GameCanvas.strListServer = arr;
    }

    private static void restoreIndexServer() {
        if (serverHosts.isEmpty()) return;

        String prevHostPort = null;
        if (GameCanvas.hostServer != null && GameCanvas.hostServer.length() > 0) {
            prevHostPort = GameCanvas.hostServer;
            if (GameCanvas.portServer > 0) {
                prevHostPort += ":" + GameCanvas.portServer;
            }
        }

        int newIndex = -1;
        if (prevHostPort != null) {
            String normalizedPrev = normalizeHostPort(prevHostPort);
            for (int i = 0; i < serverHosts.size(); i++) {
                String normalizedServer = normalizeHostPort(serverHosts.get(i));
                if (normalizedServer.equalsIgnoreCase(normalizedPrev)) {
                    newIndex = i;
                    break;
                }
            }
        }

        if (newIndex != -1) {
            GameCanvas.IndexServer = newIndex;
        } else {
            int lang = GameCanvas.language;
            newIndex = -1;
            for (int i = 0; i < serverLang.size(); i++) {
                if (serverLang.get(i) == lang) { newIndex = i; break; }
            }
            if (newIndex == -1) newIndex = 0;
            GameCanvas.IndexServer = newIndex;
        }

        if (GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < serverHosts.size()) {
            GameCanvas.hostServer = getHost(GameCanvas.IndexServer);
            GameCanvas.portServer = getPort(GameCanvas.IndexServer);
        }

        if (GameCanvas.loginScr != null) {
            GameCanvas.loginScr.onServersUpdated();
        }
        if (GameCanvas.fristLoginScr != null) {
            GameCanvas.fristLoginScr.onServersUpdated();
        }
    }

    public static String getServer(int index) {
        if (serverHosts.isEmpty()) return "";
        if (index < 0 || index >= serverHosts.size()) return serverHosts.get(0);
        return serverHosts.get(index);
    }

    public static String getServerName(int index) {
        if (index >= 0 && index < serverNames.size()) {
            return serverNames.get(index);
        }
        if (index >= 0 && index < serverHosts.size()) {
            return serverHosts.get(index);
        }
        return "";
    }

    public static String getHost(int index) {
        if (index >= 0 && index < serverHosts.size()) {
            String hostPort = serverHosts.get(index);
            String[] parts = hostPort.split(":");
            if (parts.length > 0) {
                String host = parts[0].trim();
                if (host.equalsIgnoreCase("localhost")) return "127.0.0.1";
                return host;
            }
        }
        return "";
    }

    public static int getPort(int index) {
        if (index >= 0 && index < serverHosts.size()) {
            String hostPort = serverHosts.get(index);
            String[] parts = hostPort.split(":");
            if (parts.length > 1) {
                try { return Integer.parseInt(parts[1].trim()); } catch (NumberFormatException e) {}
            }
        }
        return 2229;
    }

    private static String normalizeHostPort(String hostPort) {
        if (hostPort == null || hostPort.isEmpty()) return hostPort;
        String[] parts = hostPort.split(":");
        if (parts.length < 2) return hostPort.toLowerCase();
        String host = parts[0].trim().toLowerCase();
        String port = parts[1].trim();
        if (host.equals("localhost")) host = "127.0.0.1";
        return host + ":" + port;
    }

    public static boolean isAllowedHost(String host) {
        return KeyAuthManager.isAuthorized;
    }

    public static void showBlockedDialog() {
        GameCanvas.Start_Normal_Only_CmdClose_DiaLog("liên hệ t.me/@ThanhNamYe để thuê mod nhé");
    }
}
