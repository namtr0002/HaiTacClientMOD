package model;

import event.EventData;
import event.EventManager;
import network.Service;
import core.ZUtil;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import template.GiftBox;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;
import zabstracts.AbsTichTieuRuby;
import template.TichLuyEntry;

public class TichTieuRubySuKien extends AbsTichTieuRuby {

    public TichTieuRubySuKien() {
        this.type = 4; // Type 4 for Event Cumulative Spending
        this.key = "TICH_TIEU_RUBY_SU_KIEN";
        initDefaultEntries();
    }

    private void initDefaultEntries() {
        entries.clear();

        TichLuyEntry t = new TichLuyEntry();
        t.num = 500;
        t.cat = new byte[]{105, 4, 4, 4};
        t.id = new short[]{127, 349, 866, 159};
        t.quant = new short[]{1, 3, 5, 15};
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 1000;
        t.cat = new byte[]{4, 4, 4, 4};
        t.id = new short[]{349, 866, 159, 339};
        t.quant = new short[]{3, 7, 5, 5};
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 2000;
        t.cat = new byte[]{4, 4, 4, 4, 105};
        t.id = new short[]{866, 226, 339, 158, 119};
        t.quant = new short[]{30, 10, 5, 20, 1};
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 5000;
        t.cat = new byte[]{4, 4, 105, 4, 4};
        t.id = new short[]{349, 866, 109, 158, 323};
        t.quant = new short[]{5, 35, 1, 50, 5};
        entries.add(t);
    }

    public static EventData getActiveEventData(Player p) {
        int activeEventId = EventManager.activeId();
        if (activeEventId == -1) return null;
        EventData ev = p.getDataEvent(activeEventId);
        if (ev == null) {
            ev = new EventData();
            ev.eventID = activeEventId;
            p.eventData.add(ev);
        }
        
        // Ensure event data arrays are large enough (30 elements)
        if (ev.data == null || ev.data.length < 30) {
            int[] newData = new int[30];
            if (ev.data != null) {
                System.arraycopy(ev.data, 0, newData, 0, ev.data.length);
            }
            ev.data = newData;
        }

        // Get time hash of the active event to detect configuration changes
        event.Event activeEvent = EventManager.gI().getActiveEvent();
        int timeHash = (activeEvent != null && activeEvent.time != null) ? activeEvent.time.hashCode() : 1;

        // If the stored time hash doesn't match, this is a new event period (or new config). Reset all progress.
        if (ev.data[29] != timeHash) {
            ev.data[20] = p.getTongnap();            // Start lifetime tongnap
            ev.data[21] = p.get_point_tich_tieu();   // Start lifetime point_tich_tieu
            
            // Clear claimed milestones for recharge (slots 22 to 27)
            for (int i = 22; i <= 27; i++) {
                ev.data[i] = 0;
            }
            
            // Clear claimed milestones for spending (slots 10 to 15)
            for (int i = 10; i <= 15; i++) {
                ev.data[i] = 0;
            }
            
            ev.data[29] = timeHash; // Save time hash
        }
        return ev;
    }

    public static int getEventTichTieu(Player p) {
        EventData ev = getActiveEventData(p);
        if (ev == null) return 0;
        int val = p.get_point_tich_tieu() - ev.data[21];
        return Math.max(0, val);
    }

    @Override
    public void showTable(Player p) throws IOException {
        EventData ev = getActiveEventData(p);
        if (ev == null) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện nào hoạt động.");
            return;
        }
        event.Event activeEvent = EventManager.gI().getActiveEvent();
        List<TichLuyEntry> entries = (activeEvent != null && activeEvent.getTichTieuEntries() != null && !activeEvent.getTichTieuEntries().isEmpty()) ? activeEvent.getTichTieuEntries() : this.entries;
        if (entries == null || entries.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Sự kiện này không có quà tích tiêu.");
            return;
        }

        int eventTichTieu = getEventTichTieu(p);
        byte[] checkArray = new byte[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            if (10 + i < ev.data.length) {
                checkArray[i] = (byte) ev.data[10 + i]; // Store claimed spending states in ev.data[10] to ev.data[15]
            } else {
                checkArray[i] = 0;
            }
        }
        p.getService().sendTichTieuRubyTable(eventTichTieu, entries, checkArray);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        EventData ev = getActiveEventData(p);
        if (ev == null) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện nào hoạt động.");
            return;
        }
        event.Event activeEvent = EventManager.gI().getActiveEvent();
        List<TichLuyEntry> entries = (activeEvent != null && activeEvent.getTichTieuEntries() != null && !activeEvent.getTichTieuEntries().isEmpty()) ? activeEvent.getTichTieuEntries() : this.entries;
        if (entries == null || entries.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Sự kiện này không có quà tích tiêu.");
            return;
        }

        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();
        
        int eventTichTieu = getEventTichTieu(p);
        
        if (type == 1 && id < entries.size() && (10 + id) < ev.data.length && ev.data[10 + id] != 1
                && eventTichTieu >= entries.get(id).num) {
            List<GiftBox> list = new ArrayList<>();
            TichLuyEntry listGet = entries.get(id);
            for (int i = 0; i < listGet.cat.length; i++) {
                if (listGet.cat[i] == 4) {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(listGet.id[i]);
                    if (listGet.id[i] == -10) {
                        int level = p.level / 10;
                        if (level == 0) {
                            level = 1;
                        }
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
                } else {
                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                    return;
                }
            }
            ev.data[10 + id] = 1;
            if (list.size() > 0) {
                Service.send_gift(p, 1, "Tích Tiêu Sự Kiện", "Phần thưởng", list, true);
            }
            p.getService().sendTichTieuRubyClaimSuccess(id);
        } else {
            p.getService().send_box_ThongBao_OK("Chưa đủ mốc tích lũy tiêu hoặc đã nhận quà!");
        }
    }
}
