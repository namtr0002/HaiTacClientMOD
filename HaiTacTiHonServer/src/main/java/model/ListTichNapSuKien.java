package model;

import event.Event;
import event.EventData;
import event.EventManager;
import network.Message;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import template.GiftBox;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.TichLuyEntry;
import zabstracts.AbsListTichNap;
import core.ZUtil;

/**
 * Class trung gian cho Tích Nạp Sự Kiện, tự động lấy dữ liệu mốc nạp trực tiếp từ class Event.
 * Hỗ trợ nhận quà an toàn và tự động chuyển Hộp Thư khi hành trang đầy.
 */
public class ListTichNapSuKien extends AbsListTichNap {

    public ListTichNapSuKien() {
        this.type = 3;
        this.key = "LIST_TICH_NAP_SU_KIEN";
    }

    @Override
    public void showTable(Player p) throws IOException {
        Event activeEvent = EventManager.gI().getActiveEvent();
        if (activeEvent == null) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện nào hoạt động.");
            return;
        }
        List<TichLuyEntry> entries = activeEvent.getTichNapEntries();
        if (entries == null || entries.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Sự kiện này không có quà tích nạp.");
            return;
        }
        EventData ev = TichTieuRubySuKien.getActiveEventData(p);
        int eventTongNap = (ev != null && ev.data != null && ev.data.length > 20) ? Math.max(0, p.getTongnap() - ev.data[20]) : p.getTongnap();
        byte[] checkArray = new byte[entries.size()];
        if (ev != null && ev.data != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (22 + i < ev.data.length) {
                    checkArray[i] = (byte) ev.data[22 + i];
                }
            }
        }
        p.getService().sendTichNapTable(eventTongNap, entries, checkArray);
    }

    @Override
    public void process(Player p, Message m) throws IOException {
        Event activeEvent = EventManager.gI().getActiveEvent();
        if (activeEvent == null) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện nào hoạt động.");
            return;
        }
        List<TichLuyEntry> entries = activeEvent.getTichNapEntries();
        if (entries == null || entries.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Sự kiện này không có quà tích nạp.");
            return;
        }
        byte actionType = m.reader().readByte();
        byte index = m.reader().readByte();
        EventData ev = TichTieuRubySuKien.getActiveEventData(p);
        if (ev == null) return;
        int eventTongNap = (ev.data != null && ev.data.length > 20) ? Math.max(0, p.getTongnap() - ev.data[20]) : p.getTongnap();
        
        if (actionType == 1 && index < entries.size() && (22 + index) < ev.data.length && ev.data[22 + index] != 1
                && eventTongNap >= entries.get(index).num) {
            List<GiftBox> list = new ArrayList<>();
            TichLuyEntry listGet = entries.get(index);
            for (int i = 0; i < listGet.cat.length; i++) {
                if (listGet.cat[i] == 4) {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(listGet.id[i]);
                    if (listGet.id[i] == -10) {
                        int level = p.level / 10;
                        if (level == 0) level = 1;
                        itemTemplate4 = ItemTemplate4.get_it_by_id(level + 694);
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
                    Pet petTemplate = Pet.getTemplate(listGet.id[i]);
                    ItemTemplate4 it_template = ItemTemplate4.get_it_by_id(listGet.id[i]);
                    short icon = petTemplate != null ? (short) petTemplate.icon : (it_template != null ? it_template.icon : 0);
                    String name = petTemplate != null ? petTemplate.name : (it_template != null ? it_template.name : "Thú cưng");
                    if (petTemplate != null || it_template != null) {
                        MyPet pet = null;
                        for (int i1 = 0; i1 < p.my_pet.size(); i1++) {
                            if (p.my_pet.get(i1).id == listGet.id[i]) {
                                pet = p.my_pet.get(i1);
                                break;
                            }
                        }
                        if (pet != null) {
                            if (pet.time != -1) {
                                pet.time = -1;
                            }
                        } else {
                            pet = new MyPet();
                            pet.id = (short) listGet.id[i];
                            pet.isUse = false;
                            pet.template = Pet.getTemplate(pet.id);
                            pet.time = -1;
                            p.my_pet.add(pet);
                        }
                        GiftBox gb4 = new GiftBox();
                        gb4.id = (short) listGet.id[i];
                        gb4.type = 110;
                        gb4.name = name;
                        gb4.icon = icon;
                        gb4.num = listGet.quant[i];
                        gb4.color = 0;
                        list.add(gb4);
                    }
                }
            }
            ev.data[22 + index] = 1;
            if (!list.isEmpty()) {
                Service.send_gift(p, 1, "Tích Nạp Sự Kiện", "Phần thưởng", list, true);
            }
            p.getService().sendTichNapClaimSuccess(index);
        } else {
            p.getService().send_box_ThongBao_OK("Chưa đủ mốc tích lũy hoặc đã nhận quà!");
        }
    }
}
