import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigBuild {
    public static int zoomLevel = 4;
    public static String screenWStr = "40%";
    public static String screenHStr = "40%";
    public static int targetW = 800;
    public static int targetH = 600;
    public static boolean isResizable = false;
    public static byte deviceType = 2; // 0=JAVA, 1=ANDROID, 2=PC, 3=IOS
    public static byte typeClient = 4; // 0=JAVA, 1=ANDROID, 4=PC, 5=IOS_STORE

    public static void loadConfig(int monitorW, int monitorH) {
        File configFile = new File("configbuild.txt");
        if (!configFile.exists()) {
            configFile = new File("../configbuild.txt");
        }

        if (configFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    int eqIdx = line.indexOf('=');
                    if (eqIdx > 0) {
                        String key = line.substring(0, eqIdx).trim().toLowerCase();
                        String val = line.substring(eqIdx + 1).trim();

                        if (key.equals("zoomlevel")) {
                            try {
                                zoomLevel = Integer.parseInt(val);
                                if (zoomLevel < 1) zoomLevel = 1;
                                if (zoomLevel > 4) zoomLevel = 4;
                            } catch (Exception e) {}
                        } else if (key.equals("screen_w") || key.equals("pc_screen_w")) {
                            screenWStr = val;
                        } else if (key.equals("screen_h") || key.equals("pc_screen_h")) {
                            screenHStr = val;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("[ConfigBuild] Error reading configbuild.txt: " + e.getMessage());
            }
        }

        // Apply device & client type based on zoomlevel (x1 Java, x2 Android, x3 iPhone, x4 PC)
        switch (zoomLevel) {
            case 1:
                deviceType = 0; // JAVA
                typeClient = 0;
                break;
            case 2:
                deviceType = 1; // ANDROID
                typeClient = 1;
                break;
            case 3:
                deviceType = 3; // IOS
                typeClient = 5;
                break;
            case 4:
            default:
                deviceType = 2; // PC
                typeClient = 4;
                break;
        }

        mGraphics.zoomLevel = zoomLevel;
        Ageticon.fakezoomlv = zoomLevel;
        GameMidlet.DEVICE = deviceType;

        targetW = parseDimension(screenWStr, monitorW, 800);
        targetH = parseDimension(screenHStr, monitorH, 600);

        if (targetW < 320) targetW = 320;
        if (targetH < 240) targetH = 240;

        System.out.println("[ConfigBuild] Parsed configbuild.txt -> ZoomLevel: " + zoomLevel +
                " (Device: " + deviceType + ", TypeClient: " + typeClient + ")" +
                ", ScreenWidth: " + targetW + " (" + screenWStr + ")" +
                ", ScreenHeight: " + targetH + " (" + screenHStr + ")");
    }

    private static int parseDimension(String val, int maxMonitorDim, int defaultVal) {
        if (val == null || val.trim().isEmpty()) {
            return defaultVal;
        }
        val = val.trim();
        try {
            if (val.endsWith("%")) {
                String numPart = val.substring(0, val.length() - 1).trim();
                float pct = Float.parseFloat(numPart);
                if (pct <= 0) pct = 40.0f;
                return (int) (maxMonitorDim * (pct / 100.0f));
            } else {
                return Integer.parseInt(val);
            }
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
