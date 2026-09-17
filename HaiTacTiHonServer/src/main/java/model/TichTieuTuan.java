package model;

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

public class TichTieuTuan extends AbsTichTieuRuby {

    public TichTieuTuan() {
        this.type = 5;
        timeStart("");
        timeEnd("");
    }

    public final static List<TichLuyEntry> ENTRY;

    public static ItemTemplate4 resolveItemTemplate4(Player p, short rawId) {
        if (rawId == -10) {
            int level = p.level / 10;
            if (level <= 0) level = 1;
            return ItemTemplate4.get_it_by_id(level + 694);
        }
        if (rawId == -12) {
            int level = p.level / 10;
            if (level <= 0) level = 1;
            return ItemTemplate4.get_it_by_id(level + 121);
        }
        if (rawId == -11) {
            short[] ids = new short[]{49, 55, 61, 67, 73, 79};
            return ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
        }
        if (rawId == -13) {
            int randId = ZUtil.random(647, 682);
            return ItemTemplate4.get_it_by_id(randId);
        }
        return ItemTemplate4.get_it_by_id(rawId);
    }

    static {
        ENTRY = new ArrayList<>();
        // Mốc 1: 50.000 Ruby Tuần
        TichLuyEntry t = new TichLuyEntry();
        t.num = 50_000;
        t.cat = new byte[]{4, 7, 7, 4, 4};
        t.id = new short[]{0, 3, 10, 802, 158};
        t.quant = new short[]{10, 10, 5, 20, 1};
        ENTRY.add(t);

        // Mốc 2: 100.000 Ruby Tuần
        t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 4, 10, 802, 158, -13};
        t.quant = new short[]{20, 15, 10, 40, 2, 1};
        ENTRY.add(t);

        // Mốc 3: 250.000 Ruby Tuần
        t = new TichLuyEntry();
        t.num = 250_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 5, 10, 802, 911, 732};
        t.quant = new short[]{35, 20, 15, 60, 2, 1};
        ENTRY.add(t);

        // Mốc 4: 500.000 Ruby Tuần
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 324};
        t.quant = new short[]{60, 25, 20, 100, 3, 1, 1};
        ENTRY.add(t);

        // Mốc 5: 1.000.000 Ruby Tuần
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 105};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 324, 137};
        t.quant = new short[]{100, 30, 25, 150, 4, 2, 1, 1};
        ENTRY.add(t);

        // Mốc 6: 2.000.000 Ruby Tuần
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 754, 40};
        t.quant = new short[]{150, 35, 35, 250, 6, 3, 2, 1};
        ENTRY.add(t);

        // Mốc 7: 5.000.000 Ruby Tuần (Mốc Tuần Tối Thượng)
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 754, 48};
        t.quant = new short[]{300, 50, 50, 500, 10, 5, 2, 3, 1};
        ENTRY.add(t);
    }

    private static int diem_tich_tieu(Player p) {
        return p != null ? p.tieuTuan : 0;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.ensureTieuTuanCheck();
        int point = diem_tich_tieu(p);
        p.getService().sendTichTieuRubyTable(point, ENTRY, p.tieuTuanCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        p.ensureTieuTuanCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) {
            claimMissedSpendRewards(p);
            return;
        }

        if (type == 1 && id < p.tieuTuanCheck.length && id < ENTRY.size() && p.tieuTuanCheck[id] != 1
                && diem_tich_tieu(p) >= ENTRY.get(id).num) {
            List<GiftBox> list = new ArrayList<>();
            TichLuyEntry listGet = ENTRY.get(id);
            for (int i = 0; i < listGet.cat.length; i++) {
                if (listGet.cat[i] == 4) {
                    ItemTemplate4 itemTemplate4 = resolveItemTemplate4(p, listGet.id[i]);
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
                    short petId = (petTemplate != null) ? petTemplate.id : listGet.id[i];
                    if (p.my_pet == null) {
                        p.my_pet = new ArrayList<>();
                    }
                    MyPet pet = null;
                    for (int i1 = 0; i1 < p.my_pet.size(); i1++) {
                        if (p.my_pet.get(i1).id == petId) {
                            pet = p.my_pet.get(i1);
                            break;
                        }
                    }
                    if (pet != null) {
                        pet.time = -1;
                    } else {
                        pet = new MyPet();
                        pet.id = petId;
                        pet.isUse = false;
                        pet.template = (petTemplate != null) ? petTemplate : Pet.getTemplate(petId);
                        pet.time = -1;
                        p.my_pet.add(pet);
                    }
                    GiftBox gb4 = new GiftBox();
                    gb4.id = petId;
                    gb4.type = 110;
                    gb4.name = "Pet " + (petTemplate != null ? petTemplate.name : ("#" + petId));
                    gb4.icon = (petTemplate != null ? petTemplate.icon : 0);
                    gb4.num = listGet.quant[i];
                    gb4.color = 0;
                    list.add(gb4);
                } else {
                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                    return;
                }
            }
            p.tieuTuanCheck[id] = 1;
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (list.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", list, true);
            }
            p.getService().sendTichTieuClaimSuccess(id);
        }
    }

    public static void claimMissedSpendRewards(Player p) throws IOException {
        int point_tich_tieu = diem_tich_tieu(p);
        List<GiftBox> allGifts = new ArrayList<>();
        int countClaimed = 0;

        for (int idx = 0; idx < ENTRY.size() && idx < p.tieuTuanCheck.length; idx++) {
            if (p.tieuTuanCheck[idx] != 1 && point_tich_tieu >= ENTRY.get(idx).num) {
                TichLuyEntry listGet = ENTRY.get(idx);
                for (int i = 0; i < listGet.cat.length; i++) {
                    if (listGet.cat[i] == 4) {
                        ItemTemplate4 itemTemplate4 = resolveItemTemplate4(p, listGet.id[i]);
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
                            allGifts.add(gb4);
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
                            allGifts.add(gb4);
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
                            allGifts.add(gb4);
                        }
                    } else if (listGet.cat[i] == 110) {
                        Pet petTemplate = Pet.getTemplate(listGet.id[i]);
                        short petId = (petTemplate != null) ? petTemplate.id : listGet.id[i];
                        if (p.my_pet == null) {
                            p.my_pet = new ArrayList<>();
                        }
                        MyPet pet = null;
                        for (int i1 = 0; i1 < p.my_pet.size(); i1++) {
                            if (p.my_pet.get(i1).id == petId) {
                                pet = p.my_pet.get(i1);
                                break;
                            }
                        }
                        if (pet != null) {
                            pet.time = -1;
                        } else {
                            pet = new MyPet();
                            pet.id = petId;
                            pet.isUse = false;
                            pet.template = (petTemplate != null) ? petTemplate : Pet.getTemplate(petId);
                            pet.time = -1;
                            p.my_pet.add(pet);
                        }
                        GiftBox gb4 = new GiftBox();
                        gb4.id = petId;
                        gb4.type = 110;
                        gb4.name = "Pet " + (petTemplate != null ? petTemplate.name : ("#" + petId));
                        gb4.icon = (petTemplate != null ? petTemplate.icon : 0);
                        gb4.num = listGet.quant[i];
                        gb4.color = 0;
                        allGifts.add(gb4);
                    }
                }
                p.tieuTuanCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (allGifts.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", allGifts, true);
            }
            p.getService().sendTichTieuClaimSuccess((byte) -1);
            p.getService().send_box_ThongBao_OK("Bạn đã nhận thành công " + countClaimed + " mốc tích tiêu tuần!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có phần thưởng tích tiêu tuần nào chưa nhận!");
        }
    }
}
