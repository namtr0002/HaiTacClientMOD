package zinterfaces.menus;

import model.Player;
import core.MenuController;
import network.Service;
import core.ZUtil;
import zinterfaces.iMenu;
import rank.TopHang;
import template.GiftBox;
import template.ItemTemplate4;
import map.zones.Map_Hang_Dong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuHoatDong implements iMenu {
    @Override
    public short[] getId() {
        return new short[]{-990, -992};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        switch (idNPC) {
            case -990: {
                if (index == 0) {
                    if (p.party != null && p.party.list.size() > 1 && !p.party.list.get(0).equals(p)) {
                        p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                        return;
                    }
                    new model.YesNoDialog(p, -64, "Thông báo",
                            "Để tham gia phó bản hang động mỗi thành viên sẽ phải mất 10 chìa khoá phó bản, Bạn có muốn tham gia?",
                            new String[]{"Đồng ý", "Huỷ"}, new byte[]{2, 1}, value -> {
                                if (value == 0) { // Đồng ý
                                    List<Player> listP = new ArrayList<>();
                                    if (p.party != null && p.party.list.size() > 1) {
                                        if (!p.party.list.get(0).equals(p)) {
                                            p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                            return;
                                        }
                                        List<String> missingKeys = new ArrayList<>();
                                        for (int i = 0; i < p.party.list.size(); i++) {
                                            Player p0 = p.party.list.get(i);
                                            if (p0 == null) continue;
                                            if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                                continue;
                                            }
                                            if (p0.map == null || !p0.map.equals(p.map)) {
                                                continue;
                                            }
                                            if (p0.get_key_boss() < 10) {
                                                missingKeys.add(p0.name);
                                            }
                                            listP.add(p0);
                                        }
                                        if (!missingKeys.isEmpty()) {
                                            p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 10 chìa khóa phó bản");
                                            return;
                                        }
                                    } else {
                                        if (p.get_key_boss() < 10) {
                                            p.getService().send_box_ThongBao_OK("Bạn không đủ 10 chìa khóa phó bản");
                                            return;
                                        }
                                        listP.add(p);
                                    }

                                    List<Player> allParticipants = new ArrayList<>(listP);
                                    for (Player realP : listP) {
                                        if (realP != null && !realP.isBot && !realP.isDe && !(realP instanceof model.DeTu) && !(realP instanceof bot.mercenary.MercenaryBot)) {
                                            List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(realP);
                                            if (activeMercs != null && !activeMercs.isEmpty()) {
                                                for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                                    if (merc != null && !merc.isdie && !allParticipants.contains(merc)) {
                                                        allParticipants.add(merc);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Map_Hang_Dong dungeon = Map_Hang_Dong.createDungeon(allParticipants);
                                    dungeon.join(allParticipants);
                                }
                            }).startYesNo();
                } else if (index == 1) {
                    p.getService().openDynamicMenu(-992, "Đổi Thẻ Hang Động", new String[]{"Búa Đục Dial", "Rương Dial Truyền Thuyết", "Danh Hiệu King Of Hell","Hộp Trang Phục Sơ","Trái Ác Quỷ Tự Chọn","Búa Sơ Cấp"},
                            null);
                } else if (index == 2) {
                    TopHang.gI().show(p, 0);
                }
                break;
            }

            case -992: {
                if (index == 0) {
                    if (p.item.total_item_bag_by_id(4, 841) < 300) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 300 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(457);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 300);
                        p.item.updateInventory(false);
                    }
                } else if (index == 1) {
                    if (p.item.total_item_bag_by_id(4, 841) < 1000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 1000 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(823);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 1000);
                        p.item.updateInventory(false);
                    }
                } else if (index == 2) {
                    if (p.item.total_item_bag_by_id(4, 841) < 5000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 5000 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(842);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 5000);
                        p.item.updateInventory(false);
                    }
                } else if (index == 3) {
                    if (p.item.total_item_bag_by_id(4, 841) < 5000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 5000 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(356);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 5000);
                        p.item.updateInventory(false);
                    }
                } else if (index == 4) {
                    if (p.item.total_item_bag_by_id(4, 841) < 10000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 10000 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(690);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 10000);
                        p.item.updateInventory(false);
                    }
                } else if (index == 5) {
                    if (p.item.total_item_bag_by_id(4, 841) < 200) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 200 Thẻ Bài Hang Động");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(339);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Thẻ Bài Hang Động", "Nhận được",
                                list, true);
                        p.item.remove_item47(4, 841, 200);
                        p.item.updateInventory(false);
                    }
                }
                break;
            }
        }
    }
}
