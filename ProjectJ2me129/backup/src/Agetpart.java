import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;

public class Agetpart {

    private static final int MAX_PART_ID = 32767;
    private static final int START_PART_ID = 0;
    private static Hashtable requested = new Hashtable();
    private static PrintStream ps;

    // Mở file SQL khi class load
    static {
        try {
            int ts = (int)(System.currentTimeMillis()/1000);
            String file = "C:\\ThMadara\\HTTH\\SQL\\part_" + ts + ".sql";
            ps = new PrintStream(new FileOutputStream(file, true));
            System.out.println("Exporting parts to: " + file);
        } catch(Exception e){ e.printStackTrace(); }
    }

    // Chỉ cần gọi run() là gửi tất cả request
    public static void run() {
        for(int id = START_PART_ID; id <= MAX_PART_ID; id++) {
            String key = ""+id;
            if(CharPartInfo.hashMyPart.get(key)==null && requested.get(new Integer(id))==null) {
                requested.put(new Integer(id), Boolean.TRUE);
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
            String sql = "INSERT INTO `parts` (`id`,`type`,`data`) VALUES (" + id + "," + part.pi.length + ",'" + sb.toString() + "');\n";
            if(ps!=null) { ps.print(sql); ps.flush(); }
        } catch(Exception e){ e.printStackTrace(); }
    }
}
