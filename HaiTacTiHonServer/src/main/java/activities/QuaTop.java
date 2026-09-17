package activities;

import model.MyPet;
import model.Pet;
import model.Player;
import network.Service;
import core.ZUtil;
import template.GiftBox;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuaTop {

    public final static List<QuaTop> ENTRY;

    static {
        ENTRY = new ArrayList<>();
        
        // QUÀ TOP 1 (Hạng 1)
        QuaTop t = new QuaTop();
        t.cat = new byte[]{105, 4, 4, 7};
        t.id = new short[]{126, 106, 323, 10};
        t.quant = new short[]{1, 3, 5, 20}; // Thời trang Roger (126), 3 Rương vàng (106), 5 Bùa bảo vệ (323), 20 Khiên (10)
        ENTRY.add(t);

        // QUÀ TOP 2 (Hạng 2)
        t = new QuaTop();
        t.cat = new byte[]{105, 4, 4, 7};
        t.id = new short[]{119, 106, 323, 10};
        t.quant = new short[]{1, 2, 3, 15}; // Thời trang Mihawk Gold (119), 2 Rương vàng (106), 3 Bùa bảo vệ (323), 15 Khiên (10)
        ENTRY.add(t);

        // QUÀ TOP 3 (Hạng 3)
        t = new QuaTop();
        t.cat = new byte[]{105, 4, 4, 7};
        t.id = new short[]{127, 19, 323, 10};
        t.quant = new short[]{1, 3, 2, 10}; // Thời trang Doflamingo (127), 3 Rương bạc (19), 2 Bùa bảo vệ (323), 10 Khiên (10)
        ENTRY.add(t);

        // Other entries ...

    }

    public byte[] cat;
    public short[] id;
    public short[] quant;

    // Hàm đọc tên nhân vật từ file .txt
    public static List<String> loadCharacterNames(String filename) throws IOException {
        List<String> characterNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                characterNames.add(line.trim());
            }
        }
        return characterNames;
    }

    // Hàm cập nhật lại file .txt sau khi xóa tên nhân vật
    private static void updateCharacterNamesFile(String filename, List<String> characterNames) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String name : characterNames) {
                writer.write(name);
                writer.newLine();
            }
        }
    }

    public static List<GiftBox> buildGiftsForEntry(QuaTop listGet, int playerLevel) {
        List<GiftBox> list = new ArrayList<>();
        if (listGet == null) return list;
        for (int i = 0; i < listGet.cat.length; i++) {
            if (listGet.cat[i] == 4) {
                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(listGet.id[i]);
                if (listGet.id[i] == -10) {
                    int level = playerLevel / 10;
                    if (level <= 0) level = 1;
                    if (level > 10) level = 10;
                    itemTemplate4 = ItemTemplate4.get_it_by_id(level + 111);
                }
                if (listGet.id[i] == -12) {
                    int level = playerLevel / 10;
                    if (level <= 0) level = 1;
                    if (level > 10) level = 10;
                    itemTemplate4 = ItemTemplate4.get_it_by_id(level + 121);
                }
                if (listGet.id[i] == -11) {
                    short[] ids = new short[]{49, 55, 61, 67, 73, 79};
                    itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                }
                if (itemTemplate4 != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = itemTemplate4.id;
                    gb4.type = 4;
                    gb4.name = itemTemplate4.name;
                    gb4.icon = itemTemplate4.icon;
                    gb4.num = listGet.quant[i];
                    if (gb4.id == 0) {
                        gb4.num *= 1_000_000;
                    }
                    gb4.color = 0;
                    list.add(gb4);
                }
            } else if (listGet.cat[i] == 7) {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(listGet.id[i]);
                if (itemTemplate7 != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = itemTemplate7.id;
                    gb4.type = 7;
                    gb4.name = itemTemplate7.name;
                    gb4.icon = itemTemplate7.icon;
                    gb4.num = listGet.quant[i];
                    gb4.color = 0;
                    list.add(gb4);
                }
            } else if (listGet.cat[i] == 105) {
                ItemFashion itemFashion = ItemFashion.get_item(listGet.id[i]);
                if (itemFashion != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = itemFashion.ID;
                    gb4.type = 105;
                    gb4.name = itemFashion.name;
                    gb4.icon = itemFashion.idIcon;
                    gb4.num = listGet.quant[i];
                    gb4.color = 0;
                    list.add(gb4);
                }
            } else if (listGet.cat[i] == 110) {
                ItemTemplate4 it_template = ItemTemplate4.get_it_by_id(listGet.id[i]);
                if (it_template != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = it_template.id;
                    gb4.type = 110;
                    gb4.name = it_template.name;
                    gb4.icon = it_template.icon;
                    gb4.num = listGet.quant[i];
                    gb4.color = 0;
                    list.add(gb4);
                }
            }
        }
        return list;
    }

    // Tự động chuyển toàn bộ quà Top trong file text vào Hộp Thư nếu người chơi chưa kịp nhận
    public static void autoSendUnclaimedGiftsToMail() {
        try {
            File f = new File("gift_top/name_char.txt");
            if (!f.exists()) return;
            List<String> characterNames = loadCharacterNames("gift_top/name_char.txt");
            if (characterNames.isEmpty()) return;

            for (int rankIndex = 0; rankIndex < characterNames.size(); rankIndex++) {
                String charName = characterNames.get(rankIndex);
                if (charName == null || charName.trim().isEmpty()) continue;
                charName = charName.trim();
                int[] pInfo = historys.DungeonRewardHistory.getPlayerAndAccountId(charName);
                int playerId = pInfo[0];
                int accountId = pInfo[1];
                if (playerId <= 0) continue;

                QuaTop listGet = (rankIndex < ENTRY.size()) ? ENTRY.get(rankIndex) : null;
                if (listGet != null) {
                    List<GiftBox> list = buildGiftsForEntry(listGet, 50);
                    if (!list.isEmpty()) {
                        core.MailService.sendMailOffline(playerId, accountId, charName, "Hệ Thống",
                                "Phần Thưởng Top (Hạng " + (rankIndex + 1) + ")",
                                "Chúc mừng bạn đã đạt Hạng " + (rankIndex + 1) + " trong sự kiện! Hãy nhận quà đính kèm bên dưới.",
                                core.MailService.MAIL_TYPE_GIFT, false, list, 0L);
                    }
                }
            }
            // Sau khi chuyển toàn bộ vào thư, làm rỗng file để không gửi trùng
            updateCharacterNamesFile("gift_top/name_char.txt", new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm xử lý phần thưởng khi người chơi nhận trực tiếp
    public static void handler(Player p) throws IOException {
        try {
            // Đọc tên nhân vật từ file .txt
            List<String> characterNames = loadCharacterNames("gift_top/name_char.txt");

            // Kiểm tra xem tên nhân vật có trong danh sách hay không
            if (characterNames.contains(p.name)) {
                int index = characterNames.indexOf(p.name);
                QuaTop listGet = (index < ENTRY.size()) ? ENTRY.get(index) : null;

                if (listGet != null) {
                    List<GiftBox> list = buildGiftsForEntry(listGet, p.level);

                    if (list.size() > 0) {
                        if (p.conn != null) {
                            p.conn.update_quatop();
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Phần Thưởng Top", "Phần thưởng Hạng " + (index + 1), list, true);

                        // Sau khi nhận quà thành công, xóa tên nhân vật khỏi file .txt
                        characterNames.remove(index);
                        updateCharacterNamesFile("gift_top/name_char.txt", characterNames);
                    }
                }
            } else {
                p.getService().send_box_ThongBao_OK("Tên nhân vật không có trong danh sách phần thưởng.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
