import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;

public class Agetpart {

    private static final int MAX_PART_ID = 32767;
    private static final int START_PART_ID = 0;
    private static Hashtable requested = new Hashtable();
    private static PrintStream ps;

    private static synchronized PrintStream getPrintStream() {
        if (ps == null) {
            try {
                int ts = (int)(System.currentTimeMillis()/1000);
                String file = "C:\\ThMadara\\HTTH\\res\\icon\\part_" + ts + ".sql";
                File f = new File(file);
                if (f.getParentFile() != null) {
                    f.getParentFile().mkdirs();
                }
                ps = new PrintStream(new FileOutputStream(f, true));
                System.out.println("Exporting parts to: " + file);
            } catch(Throwable ignored) {}
        }
        return ps;
    }

    // Chỉ cần gọi run() là gửi tất cả request
    public static void run() {
        for(int id = START_PART_ID; id <= MAX_PART_ID; id++) {
            String key = ""+id;
            if(CharPartInfo.hashMyPart.get(key)==null && requested.get(new Integer(id))==null) {
                requested.put(new Integer(id), new Boolean(true));
                sendRequest(id);
            }
        }
        System.out.println("All request sent");
    }

    // Gửi request tới server
    private static void sendRequest(int id) {
        try {
            System.out.println("requestPart id " + id);
            GlobalService.getInstance().getDataPart((short)id);
        } catch(Exception e){}
    }

    // Gọi ngay khi nhận message -82
    public static void onPartReceived(int id, mPart part) {
        if(part==null || part.pi==null) return;
        CharPartInfo.hashMyPart.put(""+id, part);

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for(int i=0;i<part.pi.length;i++){
                PartImage img = part.pi[i];
                if(img!=null){
                    sb.append("[").append(img.id).append(",").append(img.dx).append(",").append(img.dy).append("]");
                    if(i<part.pi.length-1) sb.append(",");
                }
            }
            sb.append("]");
            String sql = "INSERT INTO `parts` (`id`,`type`,`data`) VALUES (" + id + "," + part.type + ",'" + sb.toString() + "');\n";
            PrintStream pStream = getPrintStream();
            if(pStream!=null) { pStream.print(sql); pStream.flush(); }
        } catch(Exception e){ e.printStackTrace(); }
    }
}
