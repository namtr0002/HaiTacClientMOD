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
import zabstracts.AbsListTichNap;
import template.TichLuyEntry;

public class ListTichNap extends AbsListTichNap {

    public ListTichNap() {
        this.type = 0;
        timeStart("");
        timeEnd("");
    }

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
            // Random 1 viên Đá Thần Thoại (ID 647 đến 682)
            int randId = ZUtil.random(647, 682);
            return ItemTemplate4.get_it_by_id(randId);
        }
        return ItemTemplate4.get_it_by_id(rawId);
    }

    public final static List<TichLuyEntry> ENTRY;

    static {
        ENTRY = new ArrayList<>();
        
        // Mốc 1: 50.000 VNĐ
        TichLuyEntry t = new TichLuyEntry();
        t.num = 50_000;
        t.cat = new byte[]{4, 4, 7, 4, 4, 105};
        t.id = new short[]{0, 1, 2, 802, 29, 0};
        t.quant = new short[]{10, 15, 30, 20, 3, 1}; // 10M Beri, 15 Ruby (was 150), 30 Bột than, 20 Vé VIP, 3 Rương Ác Quỷ, TT Sabo
        ENTRY.add(t);

        // Mốc 2: 100.000 VNĐ
        t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 110};
        t.id = new short[]{0, 1, 3, 10, 802, 158, 1};
        t.quant = new short[]{20, 30, 30, 15, 35, 3, 1}; // 20M Beri, 30 Ruby (was 300), 30 Bột tím, 15 Khiên, 35 Vé VIP, 3 Rương Đại Ác Quỷ, Pet White Zeus
        ENTRY.add(t);

        // Mốc 3: 200.000 VNĐ
        t = new TichLuyEntry();
        t.num = 200_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 105};
        t.id = new short[]{0, 1, 4, 10, 802, 732, -13, 84};
        t.quant = new short[]{35, 60, 30, 20, 50, 2, 2, 1}; // 35M Beri, 60 Ruby (was 600), 30 Bột vàng, 20 Khiên, 50 Vé VIP, 2 Rương Đá Thần, 2 Đá Thần Ngẫu Nhiên, TT Boa Hancock
        ENTRY.add(t);

        // Mốc 4: 500.000 VNĐ
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 105};
        t.id = new short[]{0, 1, 5, 10, 802, 690, 732, 122};
        t.quant = new short[]{60, 150, 35, 25, 80, 2, 2, 1}; // 60M Beri, 150 Ruby (was 1500), 35 Ngôi sao, 25 Khiên, 80 Vé VIP, 2 Rương Trái Ác Quỷ Tự Chọn, 2 Rương Đá Thần Tự Chọn, TT Kaido Bách Thú
        ENTRY.add(t);

        // Mốc 5: 1.000.000 VNĐ
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 6, 10, 802, 894, 732, 829, 12, 140};
        t.quant = new short[]{100, 350, 35, 30, 120, 3, 2, 1, 1, 1}; // 100M Beri, 350 Ruby (was 3500), 35 Mai rùa, 30 Khiên, 120 Vé VIP, 3 Rương Trái Ác Quỷ Cao Cấp, 2 Rương Đá Thần, 1 Rương Cam +15, Pet Prometheus, TT Shanks
        ENTRY.add(t);

        // Mốc 6: 2.000.000 VNĐ
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 6, 10, 802, 911, 732, 829, 18, 133};
        t.quant = new short[]{150, 800, 40, 40, 180, 4, 3, 2, 1, 1}; // 150M Beri, 800 Ruby (was 8000), 40 Mai rùa, 40 Khiên, 180 Vé VIP, 4 Rương Siêu Đại Ác Quỷ, 3 Rương Đá Thần, 2 Rương Cam +15, Pet Meow Two, TT Sung Jin-Woo
        ENTRY.add(t);

        // Mốc 7: 5.000.000 VNĐ
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 829, 49, 126};
        t.quant = new short[]{200, 1500, 45, 50, 250, 5, 3, 2, 2, 1, 1}; // 200M Beri, 1500 Ruby (was 15000), 45 Tinh tú, 50 Khiên, 250 Vé VIP, 5 Rương Siêu Đại Ác Quỷ, 3 Rương Đá Thần, 2 Đá Khảm Vô Cực, 2 Rương Cam +15, Pet Necrozma, TT Roger
        ENTRY.add(t);

        // Mốc 8: 10.000.000 VNĐ
        t = new TichLuyEntry();
        t.num = 10_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 754, 40, 152};
        t.quant = new short[]{300, 2000, 50, 60, 350, 8, 4, 2, 2, 1, 1}; // 300M Beri, 2000 Ruby (was 20000), 50 Tinh tú, 60 Khiên, 350 Vé VIP, 8 Rương Siêu Đại Ác Quỷ, 4 Rương Đá Thần, 2 Đá Khảm Vô Cực, 2 Sách Haki Bá Vương, Pet Homie Zeus, TT Nezuko
        ENTRY.add(t);

        // Mốc 9: 15.000.000 VNĐ
        t = new TichLuyEntry();
        t.num = 15_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 754, 48, 153};
        t.quant = new short[]{400, 2500, 60, 70, 450, 10, 5, 3, 3, 1, 1}; // 400M Beri, 2500 Ruby (was 25000), 60 Tinh tú, 70 Khiên, 450 Vé VIP, 10 Rương Siêu Đại Ác Quỷ, 5 Rương Đá Thần, 3 Đá Khảm Vô Cực, 3 Sách Haki Bá Vương, Pet Solgaleo, TT Kyros
        ENTRY.add(t);

        // Mốc 10: 20.000.000 VNĐ (Mốc Tối Thượng)
        t = new TichLuyEntry();
        t.num = 20_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 732, 324, 754, 5, 154};
        t.quant = new short[]{500, 3000, 70, 100, 600, 15, 6, 4, 5, 1, 1}; // 500M Beri, 3000 Ruby (was 30000), 70 Tinh tú, 100 Khiên, 600 Vé VIP, 15 Rương Siêu Đại Ác Quỷ, 6 Rương Đá Thần, 4 Đá Khảm Vô Cực, 5 Sách Haki Bá Vương, Pet Capybara, TT Rebecca
        ENTRY.add(t);
    }

    private static int getTongNap(Player p) {
        return p.getTongnap();
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapCheck();
        int tongnap = getTongNap(p);
        p.getService().sendTichNapTable(tongnap, ENTRY, p.tichTieuCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) { // Nhận quà các mốc bỏ lỡ / nhận hết
            claimMissedRewards(p);
            return;
        }

        if (type == 1 && id < p.tichTieuCheck.length && id < ENTRY.size() && p.tichTieuCheck[id] != 1
                && getTongNap(p) >= ENTRY.get(id).num) {
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
            p.tichTieuCheck[id] = 1;
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (list.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", list, true);
            }
            p.getService().sendTichNapClaimSuccess(id);
        }
    }

    public static void claimMissedRewards(Player p) throws IOException {
        p.refreshRecharge();
        p.ensureTichNapCheck();
        int tongnap = getTongNap(p);
        List<GiftBox> allGifts = new ArrayList<>();
        int countClaimed = 0;

        for (int idx = 0; idx < ENTRY.size() && idx < p.tichTieuCheck.length; idx++) {
            if (p.tichTieuCheck[idx] != 1 && tongnap >= ENTRY.get(idx).num) {
                TichLuyEntry listGet = ENTRY.get(idx);
                for (int i = 0; i < listGet.cat.length; i++) {
                    if (listGet.cat[i] == 4) {
                        ItemTemplate4 itemTemplate4 = resolveItemTemplate4(p, listGet.id[i]);
                        if (itemTemplate4 != null) {
                            GiftBox gb = new GiftBox();
                            gb.id = itemTemplate4.id;
                            gb.type = 4;
                            gb.name = itemTemplate4.name;
                            gb.icon = itemTemplate4.icon;
                            gb.num = listGet.quant[i];
                            if (gb.id == 0) gb.num *= 1_000_000;
                            gb.color = 0;
                            allGifts.add(gb);
                        }
                    } else if (listGet.cat[i] == 7) {
                        ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(listGet.id[i]);
                        if (itemTemplate7 != null) {
                            GiftBox gb = new GiftBox();
                            gb.id = itemTemplate7.id;
                            gb.type = 7;
                            gb.name = itemTemplate7.name;
                            gb.icon = itemTemplate7.icon;
                            gb.num = listGet.quant[i];
                            gb.color = 0;
                            allGifts.add(gb);
                        }
                    } else if (listGet.cat[i] == 105) {
                        ItemFashion itemFashion = ItemFashion.get_item(listGet.id[i]);
                        if (itemFashion != null) {
                            GiftBox gb = new GiftBox();
                            gb.id = itemFashion.ID;
                            gb.type = 105;
                            gb.name = itemFashion.name;
                            gb.icon = itemFashion.idIcon;
                            gb.num = listGet.quant[i];
                            gb.color = 0;
                            allGifts.add(gb);
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
                        GiftBox gb = new GiftBox();
                        gb.id = petId;
                        gb.type = 110;
                        gb.name = "Pet " + (petTemplate != null ? petTemplate.name : ("#" + petId));
                        gb.icon = (petTemplate != null ? petTemplate.icon : 0);
                        gb.num = listGet.quant[i];
                        gb.color = 0;
                        allGifts.add(gb);
                    }
                }
                p.tichTieuCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (!allGifts.isEmpty()) {
                Service.send_gift(p, 1, "Quà tích nạp tổng", "Thưởng mốc tích nạp (" + countClaimed + " mốc)", allGifts, true);
            }
            p.getService().send_box_ThongBao_OK("Đã nhận lại thành công quà của " + countClaimed + " mốc tích nạp tổng!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có mốc quà tích nạp tổng nào chưa nhận.");
        }
    }
}
