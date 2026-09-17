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

public class TichTieuRuby extends AbsTichTieuRuby {

    public TichTieuRuby() {
        this.type = 1;
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
            // Random Đá Thần Thoại (ID 647 đến 682)
            int randId = ZUtil.random(647, 682);
            return ItemTemplate4.get_it_by_id(randId);
        }
        return ItemTemplate4.get_it_by_id(rawId);
    }

    static {
        ENTRY = new ArrayList<>();
        // Mốc 1: 20.000 Ruby
        TichLuyEntry t = new TichLuyEntry();
        t.num = 20_000;
        t.cat = new byte[]{4, 7, 7, 4, 4};
        t.id = new short[]{0, 2, 10, 802, 29};
        t.quant = new short[]{2, 5, 2, 10, 1}; // 2M Beri, 5 Bột than, 2 Khiên, 10 Vé VIP, 1 Rương Ác Quỷ
        ENTRY.add(t);

        // Mốc 2: 50.000 Ruby
        t = new TichLuyEntry();
        t.num = 50_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 2, 10, 802, 158, -11};
        t.quant = new short[]{5, 10, 5, 20, 1, 1}; // 5M Beri, 10 Bột than, 5 Khiên, 20 Vé VIP, 1 Rương Đại Ác Quỷ, 1 Đá Cấp 6 Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 3: 100.000 Ruby
        t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 3, 10, 802, 158, -13};
        t.quant = new short[]{10, 10, 8, 30, 2, 1}; // 10M Beri, 10 Bột tím, 8 Khiên, 30 Vé VIP, 2 Rương Đại Ác Quỷ, 1 Đá Thần Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 4: 200.000 Ruby
        t = new TichLuyEntry();
        t.num = 200_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 3, 10, 802, 911, -13};
        t.quant = new short[]{20, 15, 12, 50, 1, 1}; // 20M Beri, 15 Bột tím, 12 Khiên, 50 Vé VIP, 1 Rương Siêu Đại Ác Quỷ, 1 Đá Thần Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 5: 350.000 Ruby
        t = new TichLuyEntry();
        t.num = 350_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 105};
        t.id = new short[]{0, 4, 10, 802, 911, 829, 127};
        t.quant = new short[]{35, 15, 15, 70, 1, 1, 1}; // 35M Beri, 15 Bột vàng, 15 Khiên, 70 Vé VIP, 1 Rương Siêu Đại Ác Quỷ, 1 Rương Cam +15 Cùng Hệ, Thời Trang Doflamingo
        ENTRY.add(t);

        // Mốc 6: 500.000 Ruby (Tiêu 500k Ruby nhận 1 viên Đá Khảm Vô Cực mỗi ngày)
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 110};
        t.id = new short[]{0, 4, 10, 802, 911, 324, 5};
        t.quant = new short[]{50, 20, 20, 100, 2, 1, 1}; // 50M Beri, 20 Bột vàng, 20 Khiên, 100 Vé VIP, 2 Rương Siêu Đại Ác Quỷ, 1 Đá Khảm Vô Cực, Pet Capybara
        ENTRY.add(t);

        // Mốc 7: 750.000 Ruby
        t = new TichLuyEntry();
        t.num = 750_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 6, 10, 802, 911, 829, 40, 152};
        t.quant = new short[]{70, 20, 25, 150, 3, 1, 1, 1}; // 70M Beri, 20 Mai rùa, 25 Khiên, 150 Vé VIP, 3 Rương Siêu Đại Ác Quỷ, 1 Rương Cam +15 Cùng Hệ, Pet Homie Zeus, Thời Trang Nezuko
        ENTRY.add(t);

        // Mốc 8: 1.000.000 Ruby
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 754, 6};
        t.quant = new short[]{100, 25, 35, 200, 5, 1, 1, 1}; // 100M Beri, 25 Mai rùa, 35 Khiên, 200 Vé VIP, 5 Rương Siêu Đại Ác Quỷ, 1 Rương Đá Thần Tự Chọn, 1 Sách Haki Bá Vương, Pet Lucci
        ENTRY.add(t);

        // Mốc 9: 1.500.000 Ruby
        t = new TichLuyEntry();
        t.num = 1_500_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 829, 754};
        t.quant = new short[]{120, 30, 40, 250, 6, 2, 1, 1}; // 120M Beri, 30 Mai rùa, 40 Khiên, 250 Vé VIP, 6 Rương Siêu Đại Ác Quỷ, 2 Rương Đá Thần, 1 Rương Cam +15, 1 Sách Haki Bá Vương
        ENTRY.add(t);

        // Mốc 10: 2.000.000 Ruby
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 49};
        t.quant = new short[]{150, 35, 45, 300, 7, 2, 1, 1}; // 150M Beri, 35 Tinh tú, 45 Khiên, 300 Vé VIP, 7 Rương Siêu Đại Ác Quỷ, 2 Rương Đá Thần, 1 Đá Khảm Vô Cực, Pet Dusk Mane Necrozma
        ENTRY.add(t);

        // Mốc 11: 3.500.000 Ruby
        t = new TichLuyEntry();
        t.num = 3_500_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4, 105};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 754, 153};
        t.quant = new short[]{200, 40, 50, 400, 8, 3, 1, 2, 1}; // 200M Beri, 40 Tinh tú, 50 Khiên, 400 Vé VIP, 8 Rương Siêu Đại Ác Quỷ, 3 Rương Đá Thần, 1 Đá Khảm Vô Cực, 2 Sách Haki Bá Vương, TT Kyros
        ENTRY.add(t);

        // Mốc 12: 5.000.000 Ruby (Mốc Tối Thượng 5M Ruby)
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4, 110};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 754, 48};
        t.quant = new short[]{300, 50, 60, 500, 10, 5, 2, 3, 1}; // 300M Beri, 50 Tinh tú, 60 Khiên, 500 Vé VIP, 10 Rương Siêu Đại Ác Quỷ, 5 Rương Đá Thần, 2 Đá Khảm Vô Cực, 3 Sách Haki Bá Vương, Pet Solgaleo
        ENTRY.add(t);
    }

    private static int diem_tich_tieu(Player p) {
        return p != null ? p.tieuRuby : 0;
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (event.EventManager.hasActiveEvent()) {
            p.getService().send_box_ThongBao_OK("Sự kiện đang diễn ra, Tích tiêu ngày tạm đóng!");
            return;
        }
        p.ensureTieuRubyCheck();
        int point_tich_tieu = diem_tich_tieu(p);
        p.getService().sendTichTieuRubyTable(point_tich_tieu, ENTRY, p.tieuRubyCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        if (event.EventManager.hasActiveEvent()) {
            p.getService().send_box_ThongBao_OK("Sự kiện đang diễn ra, Tích tiêu ngày tạm đóng!");
            return;
        }
        p.ensureTieuRubyCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) { // Nhận quà các ngày/mốc tiêu bỏ lỡ
            claimMissedSpendRewards(p);
            return;
        }

        if (type == 1 && id < p.tieuRubyCheck.length && id < ENTRY.size() && p.tieuRubyCheck[id] != 1
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
            p.tieuRubyCheck[id] = 1;
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

        for (int idx = 0; idx < ENTRY.size() && idx < p.tieuRubyCheck.length; idx++) {
            if (p.tieuRubyCheck[idx] != 1 && point_tich_tieu >= ENTRY.get(idx).num) {
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
                p.tieuRubyCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (!allGifts.isEmpty()) {
                Service.send_gift(p, 1, "Quà tích tiêu bỏ lỡ", "Thưởng mốc tiêu bỏ lỡ (" + countClaimed + " mốc)", allGifts, true);
            }
            p.getService().send_box_ThongBao_OK("Đã nhận lại thành công quà của " + countClaimed + " mốc tích tiêu bỏ lỡ!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có mốc quà tiêu bỏ lỡ nào chưa nhận.");
        }
    }
}
