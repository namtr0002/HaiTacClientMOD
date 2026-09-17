package template;

import java.util.ArrayList;
import java.util.List;

public class ItemTemplate8 {
    public static List<ItemTemplate8> ENTRYS = new ArrayList<>();
    public short icon;
    public short id;
    public String name = "";
    public byte type;
    public short ruby;
    public int beri;
    public byte istrade;
    public short timedelay;
    public short value;
    public short timeactive;
    public String nameuse = "";
    public String info = "";

    public ItemTemplate8() {
    }

    public ItemTemplate8(short id, String name, short icon, String info, int beri, short ruby, byte istrade, byte type, short timedelay, short value, short timeactive, String nameuse) {
        this.id = id;
        this.name = name != null ? name : "";
        this.icon = icon;
        this.info = info != null ? info : "";
        this.beri = beri;
        this.ruby = ruby;
        this.istrade = istrade;
        this.type = type;
        this.timedelay = timedelay;
        this.value = value;
        this.timeactive = timeactive;
        this.nameuse = nameuse != null ? nameuse : "";
    }

    public static ItemTemplate8 get_it_by_id(int id) {
        if (ENTRYS == null || ENTRYS.isEmpty()) {
            initDefaultEntries();
        }
        for (int i = 0; i < ENTRYS.size(); i++) {
            ItemTemplate8 it = ENTRYS.get(i);
            if (it != null && it.id == id) {
                return it;
            }
        }
        return null;
    }

    public static String getItemName(int id) {
        ItemTemplate8 it8 = get_it_by_id(id);
        if (it8 != null && it8.name != null && !it8.name.isEmpty()) {
            return it8.name;
        }
        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
        if (it4 != null && it4.name != null && !it4.name.isEmpty()) {
            return it4.name;
        }
        ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(id);
        if (it7 != null && it7.name != null && !it7.name.isEmpty()) {
            return it7.name;
        }
        return "vật phẩm #" + id;
    }

    public static void initDefaultEntries() {
        if (ENTRYS == null) {
            ENTRYS = new ArrayList<>();
        }
        if (!ENTRYS.isEmpty()) {
            return;
        }
        ENTRYS.add(new ItemTemplate8((short) 0, "Bùa kinh nghiệm cấp 1", (short) 70, "Tăng 50% kinh nghiệm khi trả nhiệm vụ lặp cho tất cả thành viên trong Bang Hội Tặc, thời gian hiệu lực 1h", 10000, (short) 0, (byte) 1, (byte) 0, (short) 3600, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 1, "Bùa kinh nghiệm cấp 2", (short) 71, "Tăng 50% kinh nghiệm khi trả nhiệm vụ lặp và đánh quái cho tất cả thành viên trong Bang Hội Tặc, thời gian hiệu lực 1h", 0, (short) 10, (byte) 1, (byte) 0, (short) 3600, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 2, "Bùa cường hóa Thức Ăn", (short) 72, "Tăng 25% lượng máu hồi khi sử dụng Thức Ăn cho tất cả các thành viên trong Bang Hội Tặc, tác dụng trong 1h.", 5000, (short) 0, (byte) 1, (byte) 1, (short) 3600, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 3, "Bùa cường hóa Nước Uống", (short) 73, "Tăng 25% lượng năng lượng hồi khi sử dụng Nước Uống cho tất cả các thành viên trong Bang Hội Tặc, tác dụng trong 1h.", 5000, (short) 0, (byte) 1, (byte) 1, (short) 3600, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 4, "Bùa Cường hóa Tổng Hợp", (short) 74, "Tăng 25% lượng máu và năng lượng hồi khi sử dụng thức ăn hoặc nước uống. Hiệu lực trong 1h", 0, (short) 5, (byte) 1, (byte) 1, (short) 3600, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 5, "Bùa cường hóa Vật Phẩm", (short) 75, "Tăng 25% tỷ lệ thành công khi cường hóa vật phẩm cho tất cả thành viên trong bang Hội Tặc. Hiệu lực trong vòng 1h", 0, (short) 0, (byte) 1, (byte) 2, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 6, "Gói 10.000 Bery", (short) 76, "Tăng 10.000 Bery cho Bang Hội Tặc.", 0, (short) 10, (byte) 1, (byte) 3, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 7, "Thuốc tẩy tiềm năng", (short) 77, "Tẩy toàn bộ điểm tiềm năng hiện tại của Bang Hội Tặc.", 0, (short) 20, (byte) 1, (byte) 4, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 8, "Vé PvP Bang", (short) 94, "Vé tham gia đấu PvP Bang Hội Tặc", 0, (short) 10, (byte) 1, (byte) 5, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 9, "Cây thông Noel", (short) 123, "Sự kiện Noel", 0, (short) 0, (byte) 1, (byte) 6, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 10, "Pokemon Ball Bang", (short) 148, "Tất cả thành viên trong bang mỗi người nhận 10 quả cầu bang hội tặc với 25% bắt được pokemon.", 0, (short) 0, (byte) 1, (byte) 7, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 11, "Triệu hồi Bí Ngô", (short) 168, "Gọi ra quái vật Halloween", 0, (short) 0, (byte) 1, (byte) 8, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 12, "Bánh sinh nhật 1 tầng", (short) 188, "Gọi ra Bánh sinh nhật ", 0, (short) 0, (byte) 1, (byte) 9, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 13, "Bánh sinh nhật 2 tầng", (short) 189, "Gọi ra Bánh sinh nhật ", 0, (short) 0, (byte) 1, (byte) 9, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 14, "Gói Xương Ống", (short) 202, "Mỗi thành viên trong Bang sẽ nhận được 5 Xương Ống", 0, (short) 0, (byte) 1, (byte) 10, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 15, "Vé gọi Siêu Khuyển", (short) 197, "Gọi ra siêu khuyển", 0, (short) 0, (byte) 1, (byte) 11, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 16, "Vé gọi Pokemon", (short) 249, "Gọi ra để bắt 5 Pokemon", 0, (short) 0, (byte) 1, (byte) 12, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 17, "Choáng váng", (short) 269, "Làm choáng tất cả các thành viên bang đối phương trong 10s", 0, (short) 25, (byte) 1, (byte) 13, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 18, "Kiệt sức", (short) 270, "Tất cả những thành viên bang đối phương bị giảm 50% sát thương trong vòng 15s", 0, (short) 25, (byte) 1, (byte) 13, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 19, "Bất tử", (short) 271, "Tất cả những thành viên trong bang được bất tử trong 15s", 0, (short) 25, (byte) 1, (byte) 13, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 20, "Kháng tất cả", (short) 272, "Tất cả đòn đánh vào trụ chính trong map đều hụt trong 15s", 0, (short) 25, (byte) 1, (byte) 13, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 21, "Hạt giống bang", (short) 273, "Hạt giống bang", 0, (short) 0, (byte) 1, (byte) 14, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 22, "Vé gọi Lân", (short) 364, "Gọi ra Lân Sư Tử", 0, (short) 0, (byte) 1, (byte) 15, (short) 60, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 23, "Nồi nấu bánh Tét", (short) 390, "Sự kiện Tết! Sử dụng để nấu bánh Tét", 0, (short) 0, (byte) 1, (byte) 16, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 24, "Chim Thước", (short) 549, "Sử dụng để xây cầu Ô thước", 0, (short) 0, (byte) 1, (byte) 16, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 25, "Thẻ gọi Chopper", (short) 548, "Sử dụng để gọi Chopper", 0, (short) 0, (byte) 1, (byte) 17, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 26, "Xu Hành Trình", (short) 552, "Sử dụng để mở rương huy hiệu bang hành trình", 0, (short) 0, (byte) 1, (byte) 18, (short) 0, (short) 0, (short) 0, "null"));
        ENTRYS.add(new ItemTemplate8((short) 27, "Lò sưởi", (short) 585, "Sự kiện giáng sinh! Lò sưởi trang trí", 0, (short) 0, (byte) 1, (byte) 6, (short) 0, (short) 0, (short) 0, "Sử dụng"));
        ENTRYS.add(new ItemTemplate8((short) 28, "Lò sưởi ấm", (short) 586, "Sự kiện giáng sinh! Sử dụng để cùng các thành viên sưởi ấm qua mùa đông lạnh giá", 0, (short) 0, (byte) 1, (byte) 6, (short) 0, (short) 0, (short) 0, "Sử dụng"));
    }
}
