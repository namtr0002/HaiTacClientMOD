
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;

public class GameMidlet extends MIDlet {

    public static boolean AA = false;
    public static int AB = 0;
    public static GameCanvas AC;
    public static GameMidlet AD;
    private static boolean AM;
    public static boolean AE = false;
    public static byte AF = 0;
    public static String AG = "";
    public static String AH = "";
    public static byte ZOOM_IOS = 4;
    private static String strLinkServerCheckENG = "http://54.255.184.239/service/thongbao.txt";
    private static String AO = "http://teamobi.com/services/ht/t.txt";
    private static String AP = "http://teamobi.com/services/ht/t2.txt";
    public static final String[] AJ = new String[]{"htthgem25", "htthgem150", "htthgem350", "htthgem800", "htthgem2500"};
    public static final String[] AK = new String[]{"pwgem25", "pwgem150", "pwgem350", "pwgem800", "pwgem2500"};
    public static String[] AL = new String[]{"0,99$ - 20k Extol + 4 Ruby", "2,99$ - 60k Extol + 20 Ruby", "100$ - 1M Extol + 5k Ruby", "650 Gems ($9.99)", "1400 Gems ($24.99)", "3750 Gems ($49.99)", "8000 Gems ($49.99)"};

    public GameMidlet() {
        AD = this;
    }

    protected void destroyApp(boolean var1) {
        AD.notifyDestroyed();
    }

    protected void pauseApp() {
    }

    public static void AA() {
        try {
            AD.destroyApp(true);
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public void startApp() {
        if (!AM) {
            (AC = new GameCanvas()).update();
            Session_ME.getInstance();
            Session_ME.setHandler((IMessageHandler) GlobalMessageHandler.getInstance());
            GameCanvas var10000 = AC;
            MotherCanvas.setDisplay(AD);
            AM = true;
        }

    }

    private static byte[] AA(byte[] var0) {
        if (var0 != null) {
            for (int var1 = 0; var1 < var0.length; ++var1) {
                var0[var1] = (byte) (~var0[var1]);
            }
        }

        return var0;
    }

    public static void AA(String var0, byte[] var1) {
        try {
            var1 = AA(var1);
            RecordStore var2;
            if ((var2 = RecordStore.openRecordStore(var0, true)).getNumRecords() > 0) {
                var2.setRecord(1, var1, 0, var1.length);
            } else {
                var2.addRecord(var1, 0, var1.length);
            }

            var2.closeRecordStore();
        } catch (Exception var2) {
        }
    }

    public static byte[] AA(String var0) {
        byte[] var1;
        try {
            RecordStore var3;
            var1 = (var3 = RecordStore.openRecordStore(var0, false)).getRecord(1);
            var3.closeRecordStore();
        } catch (Exception var2) {
            return null;
        }

        return AA(var1);
    }

    public static void AB(String var0) {
        try {
            AD.platformRequest(var0);
            AD.notifyDestroyed();
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public static void AB() {
    }

    public static void AC() {
        try {
            String[] var0;
            if ((var0 = RecordStore.listRecordStores()) != null) {
                for (int var1 = 0; var1 < var0.length; ++var1) {
                    if (CRes.AB(var0[var1])) {
                        RecordStore.deleteRecordStore(var0[var1]);
                    }
                }

                return;
            }
        } catch (Exception var2) {
        }

    }

    public static void AC(String var0) {
        try {
            RecordStore.deleteRecordStore(var0);
        } catch (Exception var1) {
        }
    }

    public static String loginPlus() {
        return "Java";
    }

    public static DataInputStream AD(String var0) {
        return new DataInputStream("".getClass().getResourceAsStream(var0));
    }

    public static boolean AE() {
        return false;
    }

    public static String AA(String var0, int var1, int var2) {
        return var0.substring(1, var2);
    }

    public static String AA(String var0, String var1) {
        return Start_Normal_DiaLog(var0, new String[]{var1});
    }

    public static String Start_Normal_DiaLog(String var0, String[] var1) {
        int var2 = 0;
        int var3 = 0;
        int var4;
        if ((var4 = var0.indexOf("%s")) == -1) {
            return var0;
        } else {
            int var5 = var0.length();
            if (var1 != null) {
                for (int var6 = 0; var6 < var1.length; ++var6) {
                    var5 += var1[var6].length();
                }
            }

            StringBuffer var7;
            for (var7 = new StringBuffer(var5); var4 != -1; var4 = var0.indexOf("%s", var3)) {
                var7.append(var0.substring(var3, var4));
                if (var1 != null && var2 < var1.length) {
                    var7.append(var1[var2]);
                }

                ++var2;
                var3 = var4 + 2;
            }

            if (var3 < var0.length()) {
                var7.append(var0.substring(var3));
            }

            return var7.toString();
        }
    }

    public static String AE(String var0) {
        return var0;
    }

    public static String AF(String var0) {
        try {
            HttpConnection var4;
            (var4 = (HttpConnection) Connector.open(var0)).setRequestMethod("GET");
            var4.setRequestProperty("Content-Type", "//text plain");
            var4.setRequestProperty("Connection", "close");
            if (var4.getResponseCode() == 200) {
                String var1 = "";
                InputStream var2 = var4.openInputStream();
                int var5;
                if ((var5 = (int) var4.getLength()) != -1) {
                    byte[] var6 = new byte[var5];
                    var2.read(var6);
                    var1 = new String(var6, "UTF-8");
                }

                return var1;
            } else {
                return null;
            }
        } catch (IOException var3) {
            return null;
        }
    }

    public static void AF() {
    }

    public static String AG() {
        if (GameCanvas.language != 0) {
            return strLinkServerCheckENG;
        } else {
            return GameCanvas.IndexServer == 0 ? AO : AP;
        }
    }

    public static void AH() {
        GlobalService.getInstance().AM();
    }

    public static void AI() {
    }

    public static void loginOk() {
    }

    public static void AK() {
    }
}
