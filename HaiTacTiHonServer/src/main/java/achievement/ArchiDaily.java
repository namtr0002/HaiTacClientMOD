package achievement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import network.Service;
import core.ZUtil;
import network.Message;
import template.GiftBox;
import template.ItemTemplate4;

public class ArchiDaily extends zabstracts.AbsArchive {
  public static List<ArchiDaily> ENTRY = new ArrayList<>();
  
  public int currentNum;
  public byte state;

  public ArchiDaily(int id, String title, String info, int num, int icon, int type) {
    this.id = id;
    this.title = title;
    this.info = info;
    this.num = num;
    this.icon = (short) icon;
    this.type = (byte) type;
    this.key = "ARCHI_DAILY_" + id;
  }

  public ArchiDaily(ArchiDaily template) {
    this.id = template.id;
    this.title = template.title;
    this.info = template.info;
    this.num = template.num;
    this.icon = template.icon;
    this.type = template.type;
    this.state = (byte) (template.type == -1 ? -1 : 0);
    this.currentNum = 0;
    this.key = "ARCHI_DAILY_" + template.id;
  }

  @Override
  public List<GiftBox> getRewards() {
    if (this.type == -1) {
      return getDefaultSpecialRewards();
    } else {
      return getDefaultNormalRewards();
    }
  }

  protected List<GiftBox> getDefaultNormalRewards() {
    List<GiftBox> listGift = new ArrayList<>();
    ItemTemplate4 coin = ItemTemplate4.get_it_by_id(0);
    if (coin != null) {
      GiftBox gb = new GiftBox(coin, 1_000_000);
      listGift.add(gb);
    }
    ItemTemplate4 ruby = ItemTemplate4.get_it_by_id(1);
    if (ruby != null) {
      GiftBox gb = new GiftBox(ruby, 100);
      listGift.add(gb);
    }
    return listGift;
  }

  protected List<GiftBox> getDefaultSpecialRewards() {
    List<GiftBox> listGift = new ArrayList<>();
    ItemTemplate4 coin = ItemTemplate4.get_it_by_id(0);
    if (coin != null) {
      GiftBox gb = new GiftBox(coin, 2_500_000);
      listGift.add(gb);
    }
    ItemTemplate4 ruby = ItemTemplate4.get_it_by_id(1);
    if (ruby != null) {
      GiftBox gb = new GiftBox(ruby, 300);
      listGift.add(gb);
    }
    return listGift;
  }

  public static void init() {
    ENTRY.clear();

    register(new ArchiDaily(1, "Tập thể lực", "Giết 100 con quái bất kỳ có cấp độ thấp hơn (Tối đa 10 Lv).", 100, 17, 0));
    register(new ArchiDaily(2, "Bậc thầy dịch chuyển", "Sử dụng bản dịch chuyển.", 1, 18, 0));
    register(new ArchiDaily(3, "Săn Boss", "Hạ gục 1 con Boss bất kỳ.", 1, 10, 0));
    register(new ArchiDaily(4, "Hái cam", "Vượt qua ít nhất cửa 1 của phó bảng vườn cam.", 1, 15, -1));
    register(new ArchiDaily(5, "Kiên trì", "Hoàn thành 5 nhiệm vụ lặp.", 5, 13, 0));
    register(new ArchiDaily(6, "Đánh bóng trang bị", "Cường hóa thành công trang bị.", 1, 14, 0));
    register(new ArchiDaily(7, "Bài tập nâng cao", "Giết 500 con quái bất kỳ có cấp độ thấp hơn (Tối đa 10 Lv).", 500, 10, -1));
    register(new ArchiDaily(8, "Luyện tập", "Hoàn thành nhiệm vụ lặp.", 1, 13, 0));
    register(new ArchiDaily(9, "Thắng và thắng", "Thắng 2 ván liên tiếp trong PvP.", 2, 16, -1));
    register(new ArchiDaily(10, "Giao lưu", "Chiến đấu 1 trận đấu PvP.", 1, 16, 0));
    register(new ArchiDaily(11, "Đông vui", "Tham gia 1 nhóm đủ 5 người.", 1, 9, 0));
    register(new ArchiDaily(12, "Vượt ải", "Hoàn thành 1 phó bảng.", 1, 15, 0));
    register(new ArchiDaily(13, "Người chiến thắng", "Thắng 1 trận đấu PvP.", 1, 16, 0));
    register(new ArchiDaily(14, "Không thể ngăn cản", "Qua tầng 7 phó bảng liên tầng.", 1, 15, -1));
    register(new ArchiDaily(52, "Đập hộp", "Mở 1 rương huyền bí.", 1, 11, 0));
  }

  private static void register(ArchiDaily item) {
    ENTRY.add(item);
    zabstracts.AbsArchive.register(item);
  }

  public static void show_table(Player p) throws IOException {
    if (p.archiDaily == null) return;
    p.currentArchiveType = 0; // 0 = ArchiDaily
    Message m = new Message(37);
    m.writer().writeByte(0);
    m.writer().writeUTF("Nhiệm vụ hàng ngày");
    m.writer().writeByte(p.archiDaily.length);
    boolean b = true;
    for (int i = 0; i < p.archiDaily.length; i++) {
      ArchiDaily item = p.archiDaily[i];
      if (item.state == 0 && item.currentNum >= item.num) {
        item.state = 1;
      }
      if (item.state == 0) {
        b = false;
      }
      if (i == p.archiDaily.length - 1 && b && item.state == -1) {
        item.state = 0;
      }
      m.writer().writeUTF(item.title);
      m.writer().writeUTF(item.info);
      m.writer().writeInt(item.currentNum);
      m.writer().writeInt(item.num);
      m.writer().writeShort(item.icon);
      m.writer().writeByte(item.state);
    }
    p.addmsg(m);
    m.cleanup();
  }

  public static ArchiDaily getTemplate(int id) {
    zabstracts.AbsArchive abs = zabstracts.AbsArchive.get(id);
    return (ArchiDaily) abs;
  }

  public static void ramdomArchiDaily(Player p) {
    p.archiDaily = new ArchiDaily[7];
    List<ArchiDaily> listNormal = new ArrayList<>();
    List<ArchiDaily> listSpec = new ArrayList<>();
    for (int i = 0; i < ENTRY.size(); i++) {
      if (ENTRY.get(i).type == -1) {
        listSpec.add(ENTRY.get(i));
      } else {
        listNormal.add(ENTRY.get(i));
      }
    }
    for (int i = 0; i < p.archiDaily.length; i++) {
      if (i == p.archiDaily.length - 1) {
        ArchiDaily templateSelect = listSpec.get(ZUtil.random(listSpec.size()));
        p.archiDaily[i] = new ArchiDaily(templateSelect);
        listSpec.remove(templateSelect);
      } else {
        ArchiDaily templateSelect = listNormal.get(ZUtil.random(listNormal.size()));
        p.archiDaily[i] = new ArchiDaily(templateSelect);
        listNormal.remove(templateSelect);
      }
    }
  }

  public static void handle(Player p, Message m2) throws IOException {
    byte type = m2.reader().readByte();
    byte index = m2.reader().readByte();
    handleDirect(p, type, index);
  }

  public static void handleDirect(Player p, byte type, byte index) throws IOException {
    if (type == 1 && p.archiDaily != null && index >= 0 && index < p.archiDaily.length && p.archiDaily[index] != null) {
      ArchiDaily item = p.archiDaily[index];
      if (item.state == 1 && item.currentNum >= item.num) {
        List<GiftBox> listGift = item.getRewards();
        item.state = 2;
        ArchiDaily.show_table(p);
        p.get_coin();
       
        Service.send_gift(p, 1, "Thành tích hằng ngày", "Nhận được", listGift, true);
        if (p.active_so_tay() == 1) {
          p.update_exp_so_tay(200);
        }
      }
    }
  }
}
