import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//ok
public class Ageticon {
    public static int fakezoomlv = 1;//fakezoom here
    private static final String BASE_PATH = "C:\\ThMadara\\HTTH\\res\\icon\\";
    private static final int BATCH_PER_TICK = 5; // số icon gửi mỗi lần run()
    private static boolean[] requested = new boolean[32768];
    private static boolean[] received = new boolean[32768];
    private static short currentId = 0;

    private static void requestIcon(short id) {
        try {
            if (id < 0 || id > 32767) return;
            if (requested[id]) return;
            requested[id] = true;
            Message msg = new Message((byte) -51);
            msg.writer().writeShort(id);
            Session_ME.getInstance().sendMessage(msg);
            System.out.println("Request icon id " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lưu icon ra ổ đĩa
    private static void saveIcon(short id, byte[] data) {
        try {
            if (id < 0 || id > 32767 || data == null || data.length == 0) return;

            String folderPath = BASE_PATH + fakezoomlv + "\\";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folderPath + id + ".png");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
        } catch (IOException e) {
        }
    }

    // --- Mỗi lần gọi chỉ request một batch id ---
    public static void run() {
        int count = 0;
        while (count < BATCH_PER_TICK && currentId <= 32767) {
            requestIcon(currentId);
            currentId++;
            count++;
        }
    }

    // Nhận icon từ server
    public static void onReceive(short id, byte[] array) {
        if (array == null || array.length == 0) return;
        saveIcon(id, array);
        received[id] = true;
    }

    // Còn icon nào chưa nhận không?
    public static boolean isPending() {
        return currentId <= 32767;
    }
}
