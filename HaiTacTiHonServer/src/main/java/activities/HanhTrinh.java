package activities;

import model.Player;
import network.Service;
import network.Message;
import template.ItemBag47;
import template.ItemTemplate4;
import template.ItemTemplate4_Info;
import java.io.IOException;
import java.util.List;

public class HanhTrinh {
    public static final String[] NAME = new String[] {"Làng Foosha", "TT. Vỏ Sò", "TT. Orange",
            "Làng Sirup", "Baratie", "Làng hạt dẻ", "TT. Khởi Đầu", "TT. Whiskey", "TT. HORN",
            "TT. Nanohana", "Đảo trời", "Water 7"};
    public static final short[] ICON =
            new short[] {803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 814};
    public static final short[] LANG = new short[] {1, 9, 17, 25, 33, 41, 49, 69, 83, 93, 113, 191};

    /**
     * Xác định làng/đảo (category 0..11) của bản đồ hiện tại.
     * Trả về -1 nếu là bản đồ biển hoặc không thuộc làng nào.
     */
    public static int getIslandCategory(int mapId) {
        // Category 0: Làng Foosha (maps 0 - 6)
        if (mapId >= 0 && mapId <= 6) return 0;
        // Category 1: TT. Vỏ Sò (maps 8 - 14)
        if (mapId >= 8 && mapId <= 14) return 1;
        // Category 2: TT. Orange (maps 16 - 22)
        if (mapId >= 16 && mapId <= 22) return 2;
        // Category 3: Làng Sirup (maps 24 - 30)
        if (mapId >= 24 && mapId <= 30) return 3;
        // Category 4: Baratie (maps 32 - 38)
        if (mapId >= 32 && mapId <= 38) return 4;
        // Category 5: Làng hạt dẻ (maps 40 - 46)
        if (mapId >= 40 && mapId <= 46) return 5;
        // Category 6: TT. Khởi Đầu (maps 48 - 54, 62)
        if ((mapId >= 48 && mapId <= 54) || mapId == 62) return 6;
        // Category 7: TT. Whiskey (maps 68 - 74, 79 - 81)
        if ((mapId >= 68 && mapId <= 74) || (mapId >= 79 && mapId <= 81)) return 7;
        // Category 8: TT. HORN (maps 82 - 88)
        if (mapId >= 82 && mapId <= 88) return 8;
        // Category 9: TT. Nanohana (maps 92 - 103, 107, 108)
        if ((mapId >= 92 && mapId <= 103) || mapId == 107 || mapId == 108) return 9;
        // Category 10: Đảo trời (maps 112 - 118, 124 - 127, 913 - 917, 984)
        if ((mapId >= 112 && mapId <= 118) || (mapId >= 124 && mapId <= 127) || (mapId >= 913 && mapId <= 917) || mapId == 984) return 10;
        // Category 11: Water 7 (maps 189, 191 - 211)
        if (mapId == 189 || (mapId >= 191 && mapId <= 211)) return 11;

        // Bản đồ biển hoặc các bản đồ khác không thuộc làng nào -> trả về -1
        return -1;
    }

    public static boolean isSeaMap(int mapId) {
        return mapId == 7 || mapId == 15 || mapId == 23 || mapId == 31 || mapId == 39 
                || mapId == 47 || mapId == 63 || mapId == 64 || mapId == 65 || mapId == 66 
                || mapId == 67 || mapId == 78 || mapId == 91 || mapId == 106 || mapId == 110 
                || mapId == 111 || mapId == 178 || mapId == 181 || mapId == 182 || mapId == 183 
                || mapId == 190;
    }

    public static void show_table(Player p, int type) throws IOException {
        if (p == null) return;
        if (type == 0) {
            Message m = new Message(79);
            m.writer().writeByte(4);
            m.writer().writeUTF("Bản đồ hành trình");
            m.writer().writeByte(NAME.length);
            for (int i = 0; i < NAME.length; i++) {
                m.writer().writeUTF(NAME[i]);
                m.writer().writeShort(p.get_icon_daHanhTrinh(i));
            }
            p.addmsg(m);
            m.cleanup();
        } else if (type == 1) {
            if (p.map == null || p.map.template == null) return;
            Message m = new Message(79);
            m.writer().writeByte(0);
            m.writer().writeShort(get_map(p));
            m.writer().writeUTF(p.map.template.name);
            List<ItemBag47> list_DaHanhTrinh = p.get_list_daHanhTrinh_total(p.map.template.id);
            List<ItemBag47> validList = new java.util.ArrayList<>();
            for (ItemBag47 it : list_DaHanhTrinh) {
                if (it != null && ItemTemplate4.get_it_by_id(it.id) != null) {
                    validList.add(it);
                }
            }
            m.writer().writeByte(validList.size());
            for (int i = 0; i < validList.size(); i++) {
                ItemTemplate4 itemTemplate4 =
                        ItemTemplate4.get_it_by_id(validList.get(i).id);
                m.writer().writeUTF(itemTemplate4.name != null ? itemTemplate4.name : "");
                m.writer().writeByte(4);
                m.writer().writeShort(itemTemplate4.id);
                m.writer().writeByte(1);
                m.writer().writeShort(itemTemplate4.icon);
                ItemTemplate4_Info temp_info =
                        ItemTemplate4_Info.get_by_id(itemTemplate4.indexInfoPotion);
                if (temp_info != null && temp_info.info != null) {
                    m.writer().writeUTF(temp_info.info);
                } else {
                    m.writer().writeUTF("Chưa có thông tin");
                }
            }
            p.addmsg(m);
            m.cleanup();
            //
            update_da_kham(p);
        }
    }

    public static int get_map(Player p) {
        if (p == null || p.map == null || p.map.template == null) return ICON[0];
        int cat = getIslandCategory(p.map.template.id);
        if (cat >= 0 && cat < ICON.length) {
            return ICON[cat];
        }
        return ICON[0];
    }

    public static void update_da_kham(Player p) throws IOException {
        if (p == null || p.map == null || p.map.template == null) return;
        Message m = new Message(79);
        m.writer().writeByte(1);
        ItemBag47 it_select = p.get_daHanhTrinh(p.map.template.id);
        ItemTemplate4 itemTemplate4 = it_select != null ? ItemTemplate4.get_it_by_id(it_select.id) : null;
        if (it_select != null && itemTemplate4 != null) {
            m.writer().writeByte(1);
            m.writer().writeUTF(itemTemplate4.name != null ? itemTemplate4.name : "");
            m.writer().writeByte(4);
            m.writer().writeShort(itemTemplate4.id);
            m.writer().writeShort(itemTemplate4.icon);
            ItemTemplate4_Info temp_info =
                    ItemTemplate4_Info.get_by_id(itemTemplate4.indexInfoPotion);
            if (temp_info != null && temp_info.info != null) {
                m.writer().writeUTF(temp_info.info);
            } else {
                m.writer().writeUTF("Chưa có thông tin");
            }
        } else {
            m.writer().writeByte(0);
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void process(Player player, Message m) throws IOException {
        if (player == null || player.map == null || player.map.template == null) return;
        byte act = m.reader().readByte();
        short id = m.reader().readShort();
        int cat = getIslandCategory(player.map.template.id);
        if (cat == -1) {
            player.getService().send_box_ThongBao_OK("Khu vực biển hoặc phó bản không hỗ trợ thao tác đá hành trình!");
            return;
        }
        if (act == 2) {
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
            if (itemTemplate4 != null) {
                ItemBag47 it_select = null;
                for (int i = 0; i < player.daHanhTrinh.size(); i++) {
                    ItemBag47 it = player.daHanhTrinh.get(i);
                    if (it != null && it.category == cat && it.id == id && it.quant == 0) {
                        it_select = it;
                        break;
                    }
                }
                if (it_select != null) {
                    player.data_yesno = new int[] {id};
                    player.setyesNoDialog(new model.YesNoDialog(player, 58, "Thông báo",
                            "Bạn có muốn khảm " + itemTemplate4.name + "?",
                            new String[] {"Đồng ý", "Hủy"}, new byte[] {2, -1}));
                    player.getService().startYesNo();
                } else {
                    player.getService().send_box_ThongBao_OK("Không đủ 1 " + itemTemplate4.name);
                }
            }
        } else if (act == 3) {
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
            if (itemTemplate4 != null) {
                ItemBag47 it_select = null;
                for (int i = 0; i < player.daHanhTrinh.size(); i++) {
                    ItemBag47 it = player.daHanhTrinh.get(i);
                    if (it != null && it.category == cat && it.id == id && it.quant == 1) {
                        it_select = it;
                        break;
                    }
                }
                if (it_select != null) {
                    player.data_yesno = new int[] {id};
                    player.setyesNoDialog(new model.YesNoDialog(player, 59, "Thông báo",
                            "Bạn muốn tách " + itemTemplate4.name + " với phí 100 ruby?",
                            new String[] {"Đồng ý", "Hủy"}, new byte[] {2, -1}));
                    player.getService().startYesNo();
                } else {
                    player.getService().send_box_ThongBao_OK("Không đủ 1 " + itemTemplate4.name);
                }
            }
        }
    }
}
