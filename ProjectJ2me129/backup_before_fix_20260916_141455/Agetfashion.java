import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;

public class Agetfashion {
    private static PrintStream ps;

    private static synchronized PrintStream getPrintStream() {
        if (ps == null) {
            try {
                int ts = (int)(System.currentTimeMillis()/1000);
                String file = "C:\\ThMadara\\HTTH\\res\\icon\\fashion_" + ts + ".sql";
                File f = new File(file);
                if (f.getParentFile() != null) {
                    f.getParentFile().mkdirs();
                }
                ps = new PrintStream(new FileOutputStream(f, true));
                System.out.println("Exporting fashion templates to: " + file);
            } catch(Throwable ignored) {}
        }
        return ps;
    }

    public static void run() {
        try {
            Message msg = new Message((byte) -19);
            msg.writer().writeShort((short) -9999);
            Session_ME.getInstance().sendMessage(msg);
            System.out.println("[Agetfashion] Direct request sent to server for fashion table.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void onFashionReceived(mVector list) {
        if (list == null) return;
        System.out.println("[Agetfashion] onFashionReceived called with list size: " + list.size());
        try {
            StringBuffer sqlBatch = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.elementAt(i);
                if (obj instanceof Class_DW) {
                    Class_DW fashion = (Class_DW) obj;
                    
                    StringBuffer mwearJson = new StringBuffer("[");
                    if (fashion.BI != null) {
                        for (int j = 0; j < fashion.BI.length; j++) {
                            mwearJson.append(fashion.BI[j]);
                            if (j < fashion.BI.length - 1) {
                                mwearJson.append(",");
                            }
                        }
                    }
                    mwearJson.append("]");

                    // Parse hsd from info safely in J2ME (no regex/split)
                    int hsd = -1;
                    String infoLower = fashion.info.toLowerCase();
                    if (infoLower.indexOf("hạn sử dụng") != -1) {
                        try {
                            StringBuffer numBuf = new StringBuffer();
                            // Find first numeric character after index of "hạn sử dụng"
                            int startIdx = infoLower.indexOf("hạn sử dụng") + "hạn sử dụng".length();
                            for (int k = startIdx; k < fashion.info.length(); k++) {
                                char ch = fashion.info.charAt(k);
                                if (ch >= '0' && ch <= '9') {
                                    numBuf.append(ch);
                                } else if (numBuf.length() > 0) {
                                    break; // finished reading the number digits
                                }
                            }
                            if (numBuf.length() > 0) {
                                hsd = Integer.parseInt(numBuf.toString());
                            }
                        } catch (Exception ignored) {}
                    }

                    // SQL statement compatible with database fashiontemplate table
                    String sql = "INSERT INTO `fashiontemplate` (`id`, `icon`, `name`, `info`, `mwear`, `op`, `price`, `hsd`) VALUES ("
                               + fashion.ID + ", " + fashion.idIcon + ", '" + replaceString(fashion.name, "'", "''") + "', '" + replaceString(fashion.info, "'", "''") + "', '" 
                               + mwearJson.toString() + "', '[]', " + fashion.AJ + ", " + hsd + ") "
                               + "ON DUPLICATE KEY UPDATE `icon`=VALUES(`icon`), `name`=VALUES(`name`), `info`=VALUES(`info`), `mwear`=VALUES(`mwear`), `price`=VALUES(`price`), `hsd`=VALUES(`hsd`);\n";
                    sqlBatch.append(sql);
                }
            }
            PrintStream pStream = getPrintStream();
            if (pStream != null) {
                pStream.print(sqlBatch.toString());
                pStream.flush();
                System.out.println("[Agetfashion] SQL saved to file successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // J2ME-compatible replace method
    private static String replaceString(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }
}
