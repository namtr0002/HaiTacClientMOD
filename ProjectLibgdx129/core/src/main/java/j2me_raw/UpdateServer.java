import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UpdateServer - robust parsing for remote ip list
 *
 * Expected lines (comma-separated entries per line OK, one entry per line OK):
 *   name:host:port[:lang[:frame]]
 *   host:port[:lang[:frame]]
 *   host
 *
 * Lines beginning with # are ignored.
 */
public class UpdateServer {
    private static final String SERVER_LIST_URL =
        "https://raw.githubusercontent.com/thachdeptrai/listIP/main/iphtth.txt";

    public static List<String> serverHosts = new ArrayList<>();      // host:port
    public static List<String> serverNames = new ArrayList<>();      // display names
    public static List<Integer> serverLang = new ArrayList<>();      // language index
    public static List<Integer> serverFrameId = new ArrayList<>();   // frame id 0..3

    public static void loadServers() {
        serverHosts.clear();
        serverNames.clear();
        serverLang.clear();
        serverFrameId.clear();

        BufferedReader br = null;
        try {
            URL url = new URL(SERVER_LIST_URL + "?t=" + System.currentTimeMillis());
            br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // support comma-separated entries on a single line and also single entry per line
                String[] entries = line.split(",");
                for (String entryRaw : entries) {
                    String entry = entryRaw.trim();
                    if (entry.isEmpty()) continue;

                    // split by colon
                    String[] parts = entry.split(":");
                    // trim parts
                    for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                    String name;
                    String host;
                    int port = 2229;
                    int lang = 0;
                    int frameId = 0;

                    if (parts.length >= 3) {
                        // name:host:port[:lang[:frame]]
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
                        // ambiguous: could be host:port or name:host
                        // Heuristic: if second part is numeric => host:port
                        if (parts[1].matches("\\d+")) {
                            host = parts[0];
                            try { port = Integer.parseInt(parts[1]); } catch (Exception e) { port = 2229; }
                            name = host;
                        } else {
                            // treat as name:host
                            name = parts[0];
                            host = parts[1];
                        }
                    } else {
                        // only one token -> treat as host (no port)
                        host = parts[0];
                        name = host;
                    }

                    // normalize host string: if someone wrote "localhost" keep track but we'll normalize later
                    host = host.trim();
                    if (host.isEmpty()) continue;

                    // clamp frameId
                    frameId = Math.max(0, Math.min(3, frameId));
                    // ensure port reasonable
                    if (port <= 0 || port > 65535) port = 2229;

                    String hostPort = host + ":" + port;
                    serverHosts.add(hostPort);
                    serverNames.add(name.length() > 0 ? name : hostPort);
                    serverLang.add(lang);
                    serverFrameId.add(frameId);
                }
            }
        } catch (Exception e) {
            System.err.println("[UpdateServer] ERROR loading list: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (br != null) br.close(); } catch (Exception ex) {}
        }

        // fallback default if list empty
        if (serverHosts.isEmpty()) {
            serverHosts.add("127.0.0.1:2229");
            serverNames.add("LocalP");
            serverLang.add(0);
            serverFrameId.add(0);
        }

        // Build GameCanvas.strListServer per language (safe)
        buildServerListForLanguage();

        // restore previously saved IndexServer and set host/port
        restoreIndexServer();
    }

    private static void buildServerListForLanguage() {
        // find max language index found (at least ensure current language index is covered)
        int maxLang = GameCanvas.language;
        for (Integer lg : serverLang) if (lg != null) maxLang = Math.max(maxLang, lg);

        // create array of size maxLang+1 with empty lists
        String[][] arr = new String[maxLang + 1][];
        for (int i = 0; i <= maxLang; i++) {
            arr[i] = new String[0];
        }

        // For each language index, collect server names that match
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
            // choose first server matching current language, otherwise 0
            int lang = GameCanvas.language;
            newIndex = -1;
            for (int i = 0; i < serverLang.size(); i++) {
                if (serverLang.get(i) == lang) { newIndex = i; break; }
            }
            if (newIndex == -1) newIndex = 0;
            GameCanvas.IndexServer = newIndex;
        }

        // assign host/port from IndexServer (guarded)
        if (GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < serverHosts.size()) {
            GameCanvas.hostServer = getHost(GameCanvas.IndexServer);
            GameCanvas.portServer = getPort(GameCanvas.IndexServer);
        } else {
            GameCanvas.IndexServer = 0;
            GameCanvas.hostServer = getHost(0);
            GameCanvas.portServer = getPort(0);
        }

        // notify UI if loaded
        if (GameCanvas.loginScr != null) {
            GameCanvas.loginScr.onServersUpdated();
        }
    }

    public static String getServer(int index) {
        if (serverHosts.isEmpty()) loadServers();
        if (index < 0 || index >= serverHosts.size()) return serverHosts.get(0);
        return serverHosts.get(index);
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
        return "127.0.0.1";
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
}
