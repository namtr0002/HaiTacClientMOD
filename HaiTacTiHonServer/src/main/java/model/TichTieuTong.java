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

public class TichTieuTong extends AbsTichTieuRuby {

    public TichTieuTong() {
        this.type = 6;
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
        // Mốc 1: 100.000 Ruby Tổng
        TichLuyEntry t = new TichLuyEntry();
        t.num = 100_000;
        t.cat = new byte[]{4, 7, 7, 4, 4};
        t.id = new short[]{0, 3, 10, 802, 158};
        t.quant = new short[]{15, 15, 8, 30, 2}; // 15M Beri, 15 Bột tím, 8 Khiên, 30 Vé VIP, 2 Rương Đại Ác Quỷ
        ENTRY.add(t);

        // Mốc 2: 500.000 Ruby Tổng
        t = new TichLuyEntry();
        t.num = 500_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 4, 10, 802, 158, -13};
        t.quant = new short[]{35, 20, 12, 60, 3, 1}; // 35M Beri, 20 Bột vàng, 12 Khiên, 60 Vé VIP, 3 Rương Đại Ác Quỷ, 1 Đá Thần Ngẫu Nhiên
        ENTRY.add(t);

        // Mốc 3: 1.000.000 Ruby Tổng
        t = new TichLuyEntry();
        t.num = 1_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4};
        t.id = new short[]{0, 5, 10, 802, 911, 732};
        t.quant = new short[]{70, 25, 18, 100, 3, 1}; // 70M Beri, 25 Ngôi sao, 18 Khiên, 100 Vé VIP, 3 Rương Siêu Đại Ác Quỷ, 1 Rương Đá Thần Tự Chọn
        ENTRY.add(t);

        // Mốc 4: 2.000.000 Ruby Tổng
        t = new TichLuyEntry();
        t.num = 2_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 324};
        t.quant = new short[]{120, 30, 25, 160, 4, 2, 1}; // 120M Beri, 30 Mai rùa, 25 Khiên, 160 Vé VIP, 4 Rương Siêu Đại Ác Quỷ, 2 Rương Đá Thần, 1 Đá Khảm Vô Cực
        ENTRY.add(t);

        // Mốc 5: 5.000.000 Ruby Tổng
        t = new TichLuyEntry();
        t.num = 5_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 105};
        t.id = new short[]{0, 6, 10, 802, 911, 732, 324, 142};
        t.quant = new short[]{200, 35, 30, 250, 6, 3, 1, 1}; // 200M Beri, 35 Mai rùa, 30 Khiên, 250 Vé VIP, 6 Rương Siêu Đại Ác Quỷ, 3 Rương Đá Thần, 1 Đá Vô Cực, TT Shanks Tóc Đỏ
        ENTRY.add(t);

        // Mốc 6: 10.000.000 Ruby Tổng
        t = new TichLuyEntry();
        t.num = 10_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 754, 49, 126};
        t.quant = new short[]{400, 45, 45, 400, 10, 5, 2, 2, 1, 1}; // 400M Beri, 45 Tinh tú, 45 Khiên, 400 Vé VIP, 10 Rương Siêu Đại Ác Quỷ, 5 Rương Đá Thần, 2 Đá Vô Cực, 2 Sách Haki, Pet Necrozma, TT Roger
        ENTRY.add(t);

        // Mốc 7: 20.000.000 Ruby Tổng (Mốc Trọn Đời Tối Thượng)
        t = new TichLuyEntry();
        t.num = 20_000_000;
        t.cat = new byte[]{4, 7, 7, 4, 4, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 7, 10, 802, 911, 732, 324, 754, 48, 153};
        t.quant = new short[]{800, 60, 60, 800, 15, 8, 3, 5, 1, 1}; // 800M Beri, 60 Tinh tú, 60 Khiên, 800 Vé VIP, 15 Rương Siêu Đại Ác Quỷ, 8 Rương Đá Thần, 3 Đá Vô Cực, 5 Sách Haki, Pet Solgaleo, TT Kyros
        ENTRY.add(t);
    }

    private static int diem_tich_tieu(Player p) {
        return p != null ? p.tieuTong : 0;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.ensureTieuTongCheck();
        int point = diem_tich_tieu(p);
        p.getService().sendTichTieuRubyTable(point, ENTRY, p.tieuTongCheck);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        p.ensureTieuTongCheck();
        byte type = m2.reader().readByte();
        byte id = m2.reader().readByte();

        if (type == 2) {
            claimMissedSpendRewards(p);
            return;
        }

        if (type == 1 && id < p.tieuTongCheck.length && id < ENTRY.size() && p.tieuTongCheck[id] != 1
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
            p.tieuTongCheck[id] = 1;
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

        for (int idx = 0; idx < ENTRY.size() && idx < p.tieuTongCheck.length; idx++) {
            if (p.tieuTongCheck[idx] != 1 && point_tich_tieu >= ENTRY.get(idx).num) {
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
                p.tieuTongCheck[idx] = 1;
                countClaimed++;
            }
        }

        if (countClaimed > 0) {
            try { p.flush(p, false); } catch (Exception ignored) {}
            if (allGifts.size() > 0) {
                Service.send_gift(p, 1, "Phần thưởng", "Phần thưởng", allGifts, true);
            }
            p.getService().sendTichTieuClaimSuccess((byte) -1);
            p.getService().send_box_ThongBao_OK("Bạn đã nhận thành công " + countClaimed + " mốc tích tiêu tổng!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có phần thưởng tích tiêu tổng nào chưa nhận!");
        }
    }
}
