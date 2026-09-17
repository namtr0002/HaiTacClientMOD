package model;

import network.Service;
import core.ZUtil;
import network.Message;
import template.GiftBox;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;
import zabstracts.AbsListTichNap;
import template.TichLuyEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTichNapTuan extends AbsListTichNap {

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

        // Mốc 1: 50.000 VNĐ Tuần
        TichLuyEntry t = new TichLuyEntry();
        t.num = 50_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4};
        t.id = new short[]{0, 1, 3, 10, 802, 29};
        t.quant = new short[]{5, 25, 15, 5, 20, 2}; // 5M Beri, 25K Ruby, 15 Bột tím, 5 Khiên, 20 Vé VIP, 2 Rương Ác Quỷ
        ENTRY.add(t);

        // Mốc 2: 100.000 VNĐ Tuần
        t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4};
        t.id = new short[]{0, 1, 4, 10, 802, 158};
        t.quant = new short[]{10, 50, 20, 8, 35, 2}; // 10M Beri, 50 Ruby, 20 Bột vàng, 8 Khiên, 35 Vé VIP, 2 Rương Đại Ác Quỷ
        ENTRY.add(t);

        // Mốc 3: 200.000 VNĐ Tuần
        t = new TichLuyEntry();
        t.num = 200_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 5, 10, 802, 158, -13};
        t.quant = new short[]{20, 100, 20, 12, 50, 3, 1}; // 20M Beri, 100 Ruby, 20 Ngôi sao, 12 Khiên, 50 Vé VIP, 3 Rương Đại Ác Quỷ, 1 Đá Thần Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 4: 500.000 VNĐ Tuần
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 6, 10, 802, 911, 732};
        t.quant = new short[]{40, 250, 25, 18, 80, 2, 1}; // 40M Beri, 250 Ruby, 25 Mai rùa, 18 Khiên, 80 Vé VIP, 2 Rương Siêu Đại Ác Quỷ, 1 Rương Đá Thần Tự Chọn
        ENTRY.add(t);

        // Mốc 5: 1.000.000 VNĐ Tuần
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 105};
        t.id = new short[]{0, 1, 6, 10, 802, 911, 829, 127};
        t.quant = new short[]{70, 500, 30, 25, 120, 3, 1, 1}; // 70M Beri, 500 Ruby, 30 Mai rùa, 25 Khiên, 120 Vé VIP, 3 Rương Siêu Đại Ác Quỷ, 1 Rương Cam +15, TT Doflamingo
        ENTRY.add(t);

        // Mốc 6: 2.000.000 VNĐ Tuần
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 40};
        t.quant = new short[]{100, 1000, 35, 35, 180, 5, 2, 1, 1}; // 100M Beri, 1000 Ruby, 35 Tinh tú, 35 Khiên, 180 Vé VIP, 5 Rương Siêu Đại Ác Quỷ, 2 Rương Đá Thần, 1 Đá Khảm Vô Cực, Pet Zeus
        ENTRY.add(t);

        // Mốc 7: 5.000.000 VNĐ Tuần (Mốc Tuần Tối Thượng)
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 754, 49};
        t.quant = new short[]{200, 2500, 50, 50, 300, 8, 3, 2, 2, 1}; // 200M Beri, 2500 Ruby, 50 Tinh tú, 50 Khiên, 300 Vé VIP, 8 Rương Siêu Đại Ác Quỷ, 3 Rương Đá Thần, 2 Đá Vô Cực, 2 Sách Haki, Pet Necrozma
        ENTRY.add(t);
    }

    public ListTichNapTuan() {
        this.type = 5;
    }

    private static int getNapTuan(Player p) {
        return p != null ? p.getTongnap2() : 0;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapTuanCheck();
        int tongnap = getNapTuan(p);
        p.getService().sendTichNapTable(tongnap, ENTRY, p.tichNapTuanCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapTuanCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) {
            claimMissedRewards(p);
            return;
        }

        if (type == 1 && id < p.tichNapTuanCheck.length && id < ENTRY.size() && p.tichNapTuanCheck[id] != 1
                && getNapTuan(p) >= ENTRY.get(id).num) {
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
            p.tichNapTuanCheck[id] = 1;
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (list.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", list, true);
            }
            p.getService().sendTichNapClaimSuccess(id);
        }
    }

    public static void claimMissedRewards(Player p) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapTuanCheck();
        int tongnap = getNapTuan(p);
        List<GiftBox> allGifts = new ArrayList<>();
        int countClaimed = 0;

        for (int idx = 0; idx < ENTRY.size() && idx < p.tichNapTuanCheck.length; idx++) {
            if (p.tichNapTuanCheck[idx] != 1 && tongnap >= ENTRY.get(idx).num) {
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
                p.tichNapTuanCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (allGifts.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", allGifts, true);
            }
            p.getService().sendTichNapClaimSuccess((byte) -1);
            p.getService().send_box_ThongBao_OK("Bạn đã nhận thành công " + countClaimed + " mốc tích nạp tuần!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có phần thưởng tích nạp tuần nào chưa nhận!");
        }
    }
}
