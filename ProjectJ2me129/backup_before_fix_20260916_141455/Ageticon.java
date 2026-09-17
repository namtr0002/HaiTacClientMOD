import java.io.*;
import java.util.Hashtable;

public class Ageticon {

    public static int fakezoomlv = 1; // zoom mặc định, có thể set thủ công
    private static String BASE_PATH = "C:\\ThMadara\\HTTH\\res\\icon\\";
    private static final int BATCH_PER_TICK = 5;

    private static boolean[] requested = new boolean[32768];
    private static boolean[] received = new boolean[32768];
    private static short currentId = 0;

    // --- Không còn loadConfig, path cố định ---

    private static void requestIcon(short id) {
        try {
            if (id < 0 || id > 32767) return;
            if (requested[id]) return;
            requested[id] = true;

            Message msg = new Message((byte) -51);
            msg.writer().writeShort(id);
            Session_ME.getInstance().sendMessage(msg);

            System.out.println("[Ageticon] Request icon id " + id);
        } catch (Exception e) {
            System.err.println("[Ageticon] Lỗi request icon id " + id + ": " + e.getMessage());
        }
    }

    private static void saveIcon(short id, byte[] data) {
        try {
            if (id < 0 || id > 32767 || data == null || data.length == 0) return;

            String folderPath = BASE_PATH + fakezoomlv + File.separator;
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folderPath + id + ".png");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                //fos.write(data);
            }

            //System.out.println("[Ageticon] ✅ Saved icon id " + id + " to " + file.getAbsolutePath());
        } catch (Exception e) {
            //System.err.println("[Ageticon] ⚠ Lỗi lưu icon id " + id + ": " + e.getMessage());
        }
    }

    public static void run() {
        int count = 0;
        while (count < BATCH_PER_TICK && currentId <= 32767) {
            requestIcon(currentId);
            currentId++;
            count++;
        }
    }

    public static void onReceive(short id, byte[] array) {
        if (array == null || array.length == 0) return;
        saveIcon(id, array);
        received[id] = true;
    }

    public static boolean isPending() {
        return currentId <= 32767;
    }
    
    public static void loadConfig() {
        String content = readConfigFile("config.txt");
        if (content == null || content.trim().length() == 0) {
            return;
        }

        Hashtable config = new Hashtable();
        try {
            BufferedReader br = new BufferedReader(new StringReader(content));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) continue;
                int idx = line.indexOf('=');
                if (idx > 0) {
                    String key = line.substring(0, idx).trim();
                    String val = line.substring(idx + 1).trim();
                    config.put(key, val);
                }
            }
            br.close();

            if (config.containsKey("zoomLevel")) {
                try {
                    fakezoomlv = Integer.parseInt((String) config.get("zoomLevel"));
                } catch (Exception e) {
                    fakezoomlv = 1;
                }
            }
        } catch (Exception e) {
            System.out.println("[Ageticon] ⚠ Lỗi đọc config: " + e.getMessage());
        }
    }

    // --- đọc file config ngoài hoặc trong resource ---
    private static String readConfigFile(String fileName) {
        // 1️⃣ Ưu tiên file ngoài
        try {
            File f = new File(fileName);
            if (f.exists()) {
                InputStream fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8");
            }
        } catch (Exception ignored) {}
        return loadFile(fileName);
    }

    private static String loadFile(String fileName) {
        InputStream input = getResourceAsStream("/" + fileName);
        try {
            if (input == null) return "";
            byte[] data = new byte[input.available()];
            input.read(data);
            input.close();
            fileName = new String(data, "UTF-8");
        } catch (Exception ex) {
            fileName = "";
        }
        return fileName;
    }

    public static InputStream getResourceAsStream(String var0) {
        return GameMidlet.getResourceAsStream(var0);
    }

}
