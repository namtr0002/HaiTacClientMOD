import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Agetpet {

    // Nếu muốn chỉ gửi 1 lần và nhận list từ server
    private static boolean requested = false;
    private static final String OUT_PATH = "C:\\ThMadara\\HTTH\\res\\pet\\sql\\pet_template.sql";

    // Gửi request lấy pet list (action = 3 theo server)
    public static void requestNext() {
    try {
        Message m = new Message((byte) -80); 
        m.writer().writeByte(3); // yêu cầu danh sách pet của player
        m.writer().flush();
        Session_ME.getInstance().sendMessage(m);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // Server trả về pet -> gọi hàm này
    public static void run(Message m) {
        try {
            byte action = m.reader().readByte();

            if (action == 3) { // server gửi list pet
                short total = m.reader().readShort();
                StringBuilder sqlBuilder = new StringBuilder();

                // optional header
                sqlBuilder.append("-- pet_template dump (generated)\n");

                for (int i = 0; i < total; i++) {
                    short id = m.reader().readShort();            // ID pet
                    String name = m.reader().readUTF();           // tên pet
                    String info = m.reader().readUTF();           // server gửi name 2 lần -> info == name
                    short icon = m.reader().readShort();          // icon id
                    byte type = m.reader().readByte();            // server ghi cứng 110
                    byte isUse = m.reader().readByte();           // flag isUse
                    byte optCount = m.reader().readByte();        // số options

                    StringBuilder options = new StringBuilder("[");
                    for (int k = 0; k < optCount; k++) {
                        byte optId = m.reader().readByte();
                        short param = m.reader().readShort();
                        options.append("{\"id\":").append(optId)
                               .append(",\"param\":").append(param).append("}");
                        if (k < optCount - 1) options.append(",");
                    }
                    options.append("]");

                    // Vì server không gửi frame, dùng icon làm frame (như bạn đang làm trước đó)
                    short frame = icon;

                    sqlBuilder.append("INSERT INTO `pet_template` ")
                              .append("(`id`, `name`, `info`, `icon`, `type`, `frame`, `default_options`) VALUES (")
                              .append(id).append(", '")
                              .append(name.replace("'", "`")).append("', '")
                              .append(info.replace("'", "`")).append("', ")
                              .append(icon).append(", ")
                              .append(type).append(", ")
                              .append(frame).append(", '")
                              .append(options.toString().replace("'", "`"))
                              .append("');\n");
                }

                // Ghi ra file (append). Nếu bạn muốn overwrite lần chạy, đổi 'true' -> 'false'
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUT_PATH, true))) {
                    writer.write(sqlBuilder.toString());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                System.out.println("Đã nhận và lưu " + total + " pet vào: " + OUT_PATH);

                // KHÔNG gọi requestNext() lặp lại nữa — server trả toàn bộ list một lần.
            } else {
                // Nếu server trả packet khác, in debug
                System.out.println("Packet không phải action=3, action=" + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
