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

public class ListNapHangNgay extends AbsListTichNap {

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
            // Random Đá Thần Thoại (ID 647 đến 682)
            int randId = ZUtil.random(647, 682);
            return ItemTemplate4.get_it_by_id(randId);
        }
        return ItemTemplate4.get_it_by_id(rawId);
    }

    static {
        ENTRY = new ArrayList<>();
        
        // Mốc 1: 20.000 VNĐ
        TichLuyEntry t = new TichLuyEntry();
        t.num = 20_000;
        t.cat = new byte[]{4, 4, 7, 4, 4};
        t.id = new short[]{0, 1, 2, 802, 415};
        t.quant = new short[]{2, 10, 15, 10, 5}; // 2M Beri, 10 Ruby (was 100), 15 Bột than, 10 Vé VIP, 5 Đại Bổ Đơn
        ENTRY.add(t);

        // Mốc 2: 50.000 VNĐ
        t = new TichLuyEntry();
        t.num = 50_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4};
        t.id = new short[]{0, 1, 3, 10, 802, 29};
        t.quant = new short[]{5, 25, 15, 5, 15, 2}; // 5M Beri, 25 Ruby (was 250), 15 Bột tím, 5 Khiên, 15 Vé VIP, 2 Rương Ác Quỷ
        ENTRY.add(t);

        // Mốc 3: 100.000 VNĐ
        t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4};
        t.id = new short[]{0, 1, 4, 10, 802, 29};
        t.quant = new short[]{10, 50, 25, 10, 25, 3}; // 10M Beri, 50 Ruby (was 500), 25 Bột vàng, 10 Khiên, 25 Vé VIP, 3 Rương Ác Quỷ
        ENTRY.add(t);

        // Mốc 4: 200.000 VNĐ
        t = new TichLuyEntry();
        t.num = 200_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 5, 10, 802, 158, -13};
        t.quant = new short[]{20, 120, 20, 10, 40, 3, 2}; // 20M Beri, 120 Ruby (was 1200), 20 Ngôi sao, 10 Khiên, 40 Vé VIP, 3 Rương Đại Ác Quỷ, 2 Đá Thần Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 5: 500.000 VNĐ (Duy nhất mốc 500k mới có 1 viên Đá Siêu Cấp)
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 5, 10, 802, 158, 691};
        t.quant = new short[]{35, 300, 25, 15, 60, 4, 1}; // 35M Beri, 300 Ruby (was 3000), 25 Ngôi sao, 15 Khiên, 60 Vé VIP, 4 Rương Đại Ác Quỷ, 1 Rương Đá Siêu Cấp Tự Chọn
        ENTRY.add(t);

        // Mốc 6: 1.000.000 VNĐ (1M)
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 6, 10, 802, 911, 732};
        t.quant = new short[]{50, 700, 25, 20, 100, 2, 2}; // 50M Beri, 700 Ruby (was 7000), 25 Mai rùa, 20 Khiên, 100 Vé VIP, 2 Rương Siêu Đại Ác Quỷ, 2 Rương Đá Thần Tự Chọn
        ENTRY.add(t);

        // Mốc 7: 2.000.000 VNĐ (2M)
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 1, 6, 10, 802, 911, 829};
        t.quant = new short[]{80, 1500, 35, 25, 150, 3, 1}; // 80M Beri, 1500 Ruby (was 15000), 35 Mai rùa, 25 Khiên, 150 Vé VIP, 3 Rương Siêu Đại Ác Quỷ, 1 Rương Cam +15 Cùng Hệ
        ENTRY.add(t);

        // Mốc 8: 3.000.000 VNĐ (3M)
        t = new TichLuyEntry();
        t.num = 3_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 110};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 829, 40};
        t.quant = new short[]{100, 2500, 40, 35, 200, 4, 2, 1}; // 100M Beri, 2500 Ruby (was 25000), 40 Tinh Tú, 35 Khiên, 200 Vé VIP, 4 Rương Siêu Đại Ác Quỷ, 2 Rương Cam +15 Cùng Hệ, Pet Homie Zeus
        ENTRY.add(t);

        // Mốc 9: 5.000.000 VNĐ (5M)
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 4, 4, 105};
        t.id = new short[]{0, 1, 7, 10, 802, 911, 324, 754, 152};
        t.quant = new short[]{150, 5000, 50, 50, 300, 5, 1, 1, 1}; // 150M Beri, 5000 Ruby (was 30000), 50 Tinh Tú, 50 Khiên, 300 Vé VIP, 5 Rương Siêu Đại Ác Quỷ, 1 Đá Khảm Vô Cực, 1 Sách Haki Bá Vương, Thời Trang Nezuko
        ENTRY.add(t);
    }

    public ListNapHangNgay() {
        this.type = 2;
    }

    private static int getNapHangNgay(Player p) {
        return p.getNaphangngay();
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (event.EventManager.hasActiveEvent()) {
            p.getService().send_box_ThongBao_OK("Sự kiện đang diễn ra, Tích nạp ngày tạm đóng!");
            return;
        }
        p.refreshRecharge();
        p.ensureTichHangNgayCheck();
        int tongnap = getNapHangNgay(p);
        p.getService().sendTichNapTable(tongnap, ENTRY, p.tichHangNgayCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        if (event.EventManager.hasActiveEvent()) {
            p.getService().send_box_ThongBao_OK("Sự kiện đang diễn ra, Tích nạp ngày tạm đóng!");
            return;
        }
        p.refreshRecharge();
        p.ensureTichHangNgayCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) { // Nhận quà các ngày/mốc bỏ lỡ
            claimMissedRewards(p);
            return;
        }

        if (type == 1 && id < p.tichHangNgayCheck.length && id < ENTRY.size() && p.tichHangNgayCheck[id] != 1
                && getNapHangNgay(p) >= ENTRY.get(id).num) {
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
                }
 else {
                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                    return;
                }
            }
            p.tichHangNgayCheck[id] = 1;
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (list.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", list, true);
            }
            p.getService().sendTichNapClaimSuccess(id);
        }
    }

    public static void claimMissedRewards(Player p) throws IOException {
        p.refreshRecharge();
        int tongnap = getNapHangNgay(p);
        List<GiftBox> allGifts = new ArrayList<>();
        int countClaimed = 0;

        for (int idx = 0; idx < ENTRY.size() && idx < p.tichHangNgayCheck.length; idx++) {
            if (p.tichHangNgayCheck[idx] != 1 && tongnap >= ENTRY.get(idx).num) {
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
                        GiftBox gb = new GiftBox();
                        gb.id = listGet.id[i];
                        gb.type = 110;
                        gb.name = "Pet " + (petTemplate != null ? petTemplate.name : ("#" + listGet.id[i]));
                        gb.icon = (petTemplate != null ? petTemplate.icon : 0);
                        gb.num = listGet.quant[i];
                        gb.color = 0;
                        allGifts.add(gb);
                    }
                }
                p.tichHangNgayCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (!allGifts.isEmpty()) {
                Service.send_gift(p, 1, "Quà nạp ngày bỏ lỡ", "Thưởng mốc bỏ lỡ (" + countClaimed + " mốc)", allGifts, true);
            }
            p.getService().send_box_ThongBao_OK("Đã nhận lại thành công quà của " + countClaimed + " mốc nạp ngày bỏ lỡ!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có mốc quà bỏ lỡ nào chưa nhận.");
        }
    }
}
