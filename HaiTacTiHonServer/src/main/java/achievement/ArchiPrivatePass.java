package achievement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import network.Service;
import network.Message;
import template.GiftBox;
import template.ItemTemplate4;
import historys.HistoryManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ArchiPrivatePass extends zabstracts.AbsArchive {
  public static List<ArchiPrivatePass> ENTRY = new ArrayList<>();
  
  public static final String CURRENT_SEASON = "MUA_1";
  public static final String[] SEASONS = new String[]{"MUA_1"};

  public int currentNum;
  public byte state; // -1: Khóa (yêu cầu đặc biệt), 0: Đang làm, 1: Hoàn thành (chờ nhận), 2: Đã nhận

  public ArchiPrivatePass(int id, String title, String info, int num, int icon, int type) {
    this.id = id;
    this.title = title;
    this.info = info;
    this.num = num;
    this.icon = (short) icon;
    this.type = (byte) type;
    this.key = "PRIVATE_PASS_" + CURRENT_SEASON + "_" + id;
  }

  public ArchiPrivatePass(ArchiPrivatePass template) {
    this.id = template.id;
    this.title = template.title;
    this.info = template.info;
    this.num = template.num;
    this.icon = template.icon;
    this.type = template.type;
    this.state = (byte) (template.type == -1 ? -1 : 0);
    this.currentNum = 0;
    this.key = "PRIVATE_PASS_" + CURRENT_SEASON + "_" + template.id;
  }

  @Override
  public List<GiftBox> getRewards() {
    if (this.type == -1) {
      return getSpecialRewards();
    } else {
      return getNormalRewards();
    }
  }

  protected List<GiftBox> getNormalRewards() {
    List<GiftBox> listGift = new ArrayList<>();
    ItemTemplate4 coin = ItemTemplate4.get_it_by_id(0);
    if (coin != null) {
      GiftBox gb = new GiftBox(coin, 3_000_000);
      listGift.add(gb);
    }
    ItemTemplate4 ruby = ItemTemplate4.get_it_by_id(1);
    if (ruby != null) {
      GiftBox gb = new GiftBox(ruby, 200);
      listGift.add(gb);
    }
    return listGift;
  }

  protected List<GiftBox> getSpecialRewards() {
    List<GiftBox> listGift = new ArrayList<>();
    ItemTemplate4 coin = ItemTemplate4.get_it_by_id(0);
    if (coin != null) {
      GiftBox gb = new GiftBox(coin, 10_000_000);
      listGift.add(gb);
    }
    ItemTemplate4 ruby = ItemTemplate4.get_it_by_id(1);
    if (ruby != null) {
      GiftBox gb = new GiftBox(ruby, 800);
      listGift.add(gb);
    }
    return listGift;
  }

  public static void init() {
    ENTRY.clear();

    register(new ArchiPrivatePass(1, "Nami Pass: Sát Thủ Bến Cảng", "Giết 100 con quái bất kỳ có cấp độ thấp hơn (Tối đa 10 Lv).", 100, 17, 0));
    register(new ArchiPrivatePass(2, "Nami Pass: Thám Hiểm Hải Trình", "Sử dụng 3 bản dịch chuyển.", 3, 18, 0));
    register(new ArchiPrivatePass(3, "Nami Pass: Trảm Tướng Cát Cứ", "Hạ gục 2 con Boss bất kỳ.", 2, 10, 0));
    register(new ArchiPrivatePass(4, "Nami Pass: Thu Hoạch Vườn Cam", "Vượt qua cửa 1 phó bản Vườn Cam Nami.", 1, 15, 0));
    register(new ArchiPrivatePass(5, "Nami Pass: Thử Thách Vệ Thần", "Tham gia 1 lần Thử Thách Vệ Thần.", 1, 15, 0));
    register(new ArchiPrivatePass(6, "Nami Pass: Rèn Giũa Binh Khí", "Cường hóa thành công trang bị 2 lần.", 2, 14, 0));
    register(new ArchiPrivatePass(7, "Nami Pass: Thắng Lợi PvP", "Thắng 2 ván đấu PvP.", 2, 16, 0));
    register(new ArchiPrivatePass(8, "Nami Pass: Điểm Danh Ngày", "Đăng nhập điểm danh hằng ngày.", 1, 13, 0));
    register(new ArchiPrivatePass(9, "Nami Pass: Đập Hộp Kho Báu", "Mở 2 rương huyền bí hoặc rương báu.", 2, 11, 0));
    register(new ArchiPrivatePass(10, "Nami Pass: Giao Lưu Võ Học", "Chiến đấu 3 trận đấu PvP bất kỳ.", 3, 16, 0));
    register(new ArchiPrivatePass(11, "Nami Pass: Tri Kỷ Đồng Hành", "Tham gia 1 nhóm đủ 5 người.", 1, 9, 0));
    register(new ArchiPrivatePass(12, "Nami Pass: Chinh Phục Phó Bản", "Hoàn thành 2 phó bản bất kỳ.", 2, 15, 0));
    register(new ArchiPrivatePass(13, "Nami Pass: Bậc Thầy Vượt Cửa", "Vượt tầng 7 phó bản liên tầng.", 1, 15, 0));
    register(new ArchiPrivatePass(14, "Nami Pass: Thợ Săn Quái Tinh Anh", "Giết 500 quái bất kỳ.", 500, 17, 0));
    register(new ArchiPrivatePass(15, "Nami Pass: Kiên Trì Luyện Tập", "Hoàn thành 5 nhiệm vụ lặp.", 5, 13, 0));
    register(new ArchiPrivatePass(16, "Nami Pass: Thương Gia Hải Tặc", "Thực hiện 1 giao dịch trên Chợ Mua Bán.", 1, 12, 0));
    register(new ArchiPrivatePass(17, "Nami Pass: Săn Boss Chiếm Đảo", "Tham gia phó bản Chiếm Đảo 1 lần.", 1, 15, 0));
    register(new ArchiPrivatePass(18, "Nami Pass: Đổi Tiền Nami", "Đổi Extol hoặc Ruby 1 lần tại NPC Nami.", 1, 132, 0));
    register(new ArchiPrivatePass(19, "Nami Pass: Nâng Cấp Ác Quỷ", "Cường hóa kỹ năng hoặc Ác quỷ 1 lần.", 1, 14, 0));
    register(new ArchiPrivatePass(20, "Nami Pass: Tham Gia Săn Trùm", "Tham gia sự kiện Săn Trùm 1 lần.", 1, 10, 0));
    register(new ArchiPrivatePass(21, "Nami Pass: Thợ Săn Tiền Thưởng", "Hạ gục 1 Boss Truy Nà.", 1, 10, 0));
    register(new ArchiPrivatePass(22, "Nami Pass: Bảo Vệ Pháo Đài", "Tham gia 1 ván Bảo Vệ Pháo Đài.", 1, 15, 0));
  }

  private static void register(ArchiPrivatePass item) {
    ENTRY.add(item);
    zabstracts.AbsArchive.register(item);
  }

  public static ArchiPrivatePass[] createDefaultPass() {
    ArchiPrivatePass[] pass = new ArchiPrivatePass[ENTRY.size()];
    for (int i = 0; i < ENTRY.size(); i++) {
      pass[i] = new ArchiPrivatePass(ENTRY.get(i));
    }
    return pass;
  }

  public static void initPrivatePass(Player p) {
    p.archiPrivatePass = createDefaultPass();
  }

  public static String buildTypeKey(String seasonKey) {
    String s = (seasonKey != null && !seasonKey.trim().isEmpty()) ? seasonKey.trim() : CURRENT_SEASON;
    return HistoryManager.formatKey("ARCHI_PRIVATE_PASS", s);
  }

  public static ArchiPrivatePass[] loadPassData(Player p, String seasonKey) {
    NamiPassManager.NamiPassData passData = NamiPassManager.getPassData(p, seasonKey);
    String today = java.time.LocalDate.now().toString();

    // Tự động reset nhiệm vụ mỗi ngày nếu sang ngày mới
    if (seasonKey.equalsIgnoreCase(CURRENT_SEASON) && (passData.lastResetDay == null || !today.equals(passData.lastResetDay))) {
      passData.lastResetDay = today;
      passData.tasks = createDefaultPass();
      NamiPassManager.savePassData(p, seasonKey, passData);
      return passData.tasks;
    }

    if (passData.tasks != null && passData.tasks.length > 0) {
      if (passData.tasks.length < ENTRY.size()) {
        ArchiPrivatePass[] expanded = new ArchiPrivatePass[ENTRY.size()];
        System.arraycopy(passData.tasks, 0, expanded, 0, passData.tasks.length);
        for (int i = passData.tasks.length; i < expanded.length; i++) {
          expanded[i] = new ArchiPrivatePass(ENTRY.get(i));
        }
        passData.tasks = expanded;
        NamiPassManager.savePassData(p, seasonKey, passData);
      }
      return passData.tasks;
    }

    String typeKey = buildTypeKey(seasonKey);
    String jsonStr = HistoryManager.loadData(p, typeKey);
    if (jsonStr != null && !jsonStr.trim().isEmpty() && !jsonStr.trim().equals("[]")) {
      ArchiPrivatePass[] loaded = parseFromJson(jsonStr);
      if (loaded != null && loaded.length > 0) {
        if (loaded.length < ENTRY.size()) {
          ArchiPrivatePass[] expanded = new ArchiPrivatePass[ENTRY.size()];
          System.arraycopy(loaded, 0, expanded, 0, loaded.length);
          for (int i = loaded.length; i < expanded.length; i++) {
            expanded[i] = new ArchiPrivatePass(ENTRY.get(i));
          }
          loaded = expanded;
        }
        return loaded;
      }
    }
    return createDefaultPass();
  }

  public static void savePassData(Player p, String seasonKey, ArchiPrivatePass[] passData) {
    savePassData(null, p, seasonKey, passData);
  }

  public static void savePassData(java.sql.Connection conn, Player p, String seasonKey, ArchiPrivatePass[] passData) {
    if (passData == null) return;
    NamiPassManager.NamiPassData data = NamiPassManager.getPassData(p, seasonKey);
    data.tasks = passData;
    NamiPassManager.savePassData(conn, p, seasonKey, data);
  }

  public static String serializeToJson(ArchiPrivatePass[] pass) {
    if (pass == null) return "[]";
    JSONArray a = new JSONArray();
    for (ArchiPrivatePass app : pass) {
      if (app != null) {
        JSONObject o = new JSONObject();
        o.put("id", app.id);
        o.put("n", app.currentNum);
        o.put("s", app.state);
        a.add(o);
      }
    }
    return a.toJSONString();
  }

  public static ArchiPrivatePass[] parseFromJson(String jsonStr) {
    if (jsonStr == null || jsonStr.trim().isEmpty() || jsonStr.trim().equals("[]")) {
      return null;
    }
    try {
      JSONArray arr = (JSONArray) JSONValue.parse(jsonStr);
      if (arr == null) return null;
      ArchiPrivatePass[] passArr = new ArchiPrivatePass[arr.size()];
      for (int i = 0; i < arr.size(); i++) {
        int id = 0; int currentNum = 0; byte state = 0;
        Object elem = arr.get(i);
        if (elem instanceof JSONObject) {
          JSONObject eo = (JSONObject) elem;
          id = Integer.parseInt(eo.get("id").toString());
          currentNum = Integer.parseInt(eo.get("n").toString());
          state = Byte.parseByte(eo.get("s").toString());
        }
        ArchiPrivatePass template = getTemplate(id);
        if (template != null) {
          ArchiPrivatePass item = new ArchiPrivatePass(template);
          item.currentNum = currentNum;
          item.state = state;
          passArr[i] = item;
        }
      }
      return passArr;
    } catch (Exception e) {
      System.err.println("[ArchiPrivatePass] Error parsing pass JSON: " + e.getMessage());
    }
    return null;
  }

  public static void updateProgress(Player p, int id, int num) {
    if (p == null || num <= 0) return;
    String season = p.currentPassSeason != null ? p.currentPassSeason : CURRENT_SEASON;
    if (p.archiPrivatePass == null || p.archiPrivatePass.length == 0) {
      p.archiPrivatePass = loadPassData(p, season);
    }
    if (p.archiPrivatePass == null) return;

    boolean updated = false;
    for (ArchiPrivatePass item : p.archiPrivatePass) {
      if (item != null && item.id == id && item.state == 0) {
        item.currentNum += num;
        if (item.currentNum >= item.num) {
          item.currentNum = item.num;
          item.state = 1;
        }
        updated = true;
      }
    }
    if (updated) {
      savePassData(p, season, p.archiPrivatePass);
    }
  }

  public static void show_table(Player p) throws IOException {
    show_table(p, p.viewingPassSeason != null ? p.viewingPassSeason : CURRENT_SEASON);
  }

  public static void show_table(Player p, String seasonKey) throws IOException {
    p.viewingPassSeason = seasonKey != null ? seasonKey : CURRENT_SEASON;
    p.currentArchiveType = 1; // 1 = ArchiPrivatePass

    ArchiPrivatePass[] displayPass;
    if (p.viewingPassSeason.equalsIgnoreCase(p.currentPassSeason)) {
      displayPass = loadPassData(p, p.currentPassSeason);
      p.archiPrivatePass = displayPass;
    } else {
      displayPass = loadPassData(p, p.viewingPassSeason);
    }

    Message m = new Message(37);
    m.writer().writeByte(0);
    m.writer().writeUTF("Pass Nami (" + p.viewingPassSeason + ")");
    m.writer().writeByte(displayPass.length);

    for (int i = 0; i < displayPass.length; i++) {
      ArchiPrivatePass item = displayPass[i];
      if (item == null) continue;

      if (item.state == 0 && item.currentNum >= item.num) {
        item.state = 1;
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

  public static ArchiPrivatePass getTemplate(int id) {
    zabstracts.AbsArchive abs = zabstracts.AbsArchive.get(id);
    if (abs instanceof ArchiPrivatePass) {
      return (ArchiPrivatePass) abs;
    }
    return null;
  }

  public static void handle(Player p, Message m2) throws IOException {
    byte type = m2.reader().readByte();
    byte index = m2.reader().readByte();
    handleDirect(p, type, index);
  }

  public static void handleDirect(Player p, byte type, byte index) throws IOException {
    String activeViewSeason = p.viewingPassSeason != null ? p.viewingPassSeason : CURRENT_SEASON;

    if (!activeViewSeason.equalsIgnoreCase(p.currentPassSeason)) {
      p.getService().send_box_ThongBao_OK("Đây là Pass mùa cũ (" + activeViewSeason + "), bạn chỉ có thể xem tiến trình chứ không thể nhận thưởng!");
      return;
    }

    ArchiPrivatePass[] targetPass = p.archiPrivatePass != null ? p.archiPrivatePass : loadPassData(p, activeViewSeason);
    p.archiPrivatePass = targetPass;

    if (type == 1 && targetPass != null && index >= 0 && index < targetPass.length && targetPass[index] != null) {
      ArchiPrivatePass item = targetPass[index];
      if (item.state == 1 && item.currentNum >= item.num) {
        item.state = 2; // Đã nhận
        
        long passExpReward = (item.type == -1) ? 1500L : 500L;
        NamiPassManager.addPassExp(p, passExpReward);
        
        savePassData(p, activeViewSeason, targetPass);
        show_table(p, activeViewSeason);
        
        p.getService().send_box_ThongBao_OK("Nhận thành công +" + passExpReward + " EXP Nami Pass từ nhiệm vụ: " + item.title);
        if (p.active_so_tay() == 1) {
          p.update_exp_so_tay(500);
        }
      }
    }
  }
}
