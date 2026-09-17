package zinterfaces.menus;

import event.EventManager;

import model.Player;
import zinterfaces.iMenu;
import core.Manager;
import event.Event;
import network.Service;
import template.ItemTemplate4;
import template.GiftBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubMenuEvent implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{934, -934};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == 934) {
            if (event.EventManager.isActive(Event.ID_SUKIEN_TRUNGTHU_2025)) { // sk trang ram
                if (index == 0) {
                    p.getService().send_box_ThongBao_OK("Bạn có " + p.pointEvent2 + " Điểm!");
                } else {
                    int req = 0;
                    int idGift = 0;
                    String label = "";
                    if (index == 1) { req = 300; idGift = 739; label = "Mốc_300"; }
                    else if (index == 2) { req = 1000; idGift = 414; label = "Mốc_1k"; }
                    else if (index == 3) { req = 2000; idGift = 727; label = "Mốc_2k"; }
                    else if (index == 4) { req = 5000; idGift = 457; label = "Mốc_5k"; }
                    
                    if (req > 0) {
                        if (p.pointEvent2 < req) {
                            p.getService().send_box_ThongBao_OK("Bạn Không đủ " + req + " Điểm nhận mốc .Bạn có " + p.pointEvent2 + " Điểm!");
                            return;
                        }
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(idGift);
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
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, label, "Nhận được", list, true);
                            p.update_pointEvent2(-req);
                        }
                    }
                }
            }
        } else if (idNPC == -934) {
            if (event.EventManager.isActive(10)) { // sk 8 thang 3
                if (index == 0) {
                    p.getService().send_box_ThongBao_OK("Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa và " + p.pointEvent2 + " Điểm Bó Hoa");
                } else if (index == 1) {
                    if (p.pointEvent2 < 2000) {
                        p.getService().send_box_ThongBao_OK("Bạn Không đủ 2000 Điểm Bó Hoa .Bạn có " + p.pointEvent2 + " Điểm Bó Hoa!");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(821);
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
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Điểm", "Nhận được", list, true);
                        p.update_pointEvent2(-2000);
                    }
                } else if (index == 2) {
                    if (p.pointEvent2 < 5000) {
                        p.getService().send_box_ThongBao_OK("Bạn Không đủ 5000 Điểm Bó Hoa .Bạn có " + p.pointEvent2 + " Điểm Bó Hoa!");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(822);
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
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi Điểm", "Nhận được", list, true);
                        p.update_pointEvent2(-5000);
                    }
                } else if (index == 3) {
                    if (p.pointEvent1 < 7000) {
                        p.getService().send_box_ThongBao_OK("Bạn Không đủ 7.000 Điểm Giỏ Hoa .Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa!");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(749);
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
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi điểm", "Nhận được", list, true);
                        p.update_pointEvent1(-7000);
                    }
                } else if (index == 4) {
                    if (p.pointEvent1 < 10_000) {
                        p.getService().send_box_ThongBao_OK("Bạn Không đủ 10000 Điểm Giỏ Hoa .Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa!");
                        return;
                    }
                    List<GiftBox> list = new ArrayList<>();
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(325);
                    if (itemTemplate4 != null) {
                        GiftBox gb4 = new GiftBox();
                        gb4.id = itemTemplate4.id;
                        gb4.type = 4;
                        gb4.name = itemTemplate4.name;
                        gb4.icon = itemTemplate4.icon;
                        gb4.num = 2;
                        gb4.color = 0;
                        list.add(gb4);
                    }
                    if (list.size() > 0) {
                        Service.send_gift(p, 1, "Đổi điểm", "Nhận được", list, true);
                        p.update_pointEvent1(-10000);
                    }
                }
            }
        }
    }
}
