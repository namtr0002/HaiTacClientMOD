package core;

import model.Player;
import template.GiftBox;
import template.ItemBag47;
import template.ItemTemplate4;
import template.ItemTemplate7;
import java.util.ArrayList;
import java.util.List;

public class RewardService {
    public static final short[] DA_HANH_TRINH_V0 = new short[] { 493, 494 };
    public static final short[] DA_HANH_TRINH_V1 = new short[] { 495, 496, 513 };
    public static final short[] DA_HANH_TRINH_V2 = new short[] { 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507 };
    public static final short[] DA_HANH_TRINH_V3 = new short[] { 508, 509, 510, 511, 512, 514, 515, 516, 517 };

    public static List<GiftBox> getGiftMapBossByLevel(Player p) {
        List<GiftBox> listGift = new ArrayList<>();
        short[] id = new short[] { 0, (short) ((p.level / 10) + 7) };
        short[] quant = new short[] { (short) ZUtil.random(10, 100), 1 };
        for (int i = 0; i < id.length; i++) {
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id[i]);
            GiftBox giftBox = new GiftBox();
            giftBox.id = id[i];
            giftBox.type = 4;
            giftBox.name = itemTemplate4.name;
            giftBox.icon = itemTemplate4.icon;
            giftBox.num = quant[i];
            giftBox.color = 0;
            listGift.add(giftBox);
        }
        
        if (35 > ZUtil.random(120)) { // da hanh trinh
            int id_random;
            if (5 > ZUtil.random(120)) {
                id_random = DA_HANH_TRINH_V3[ZUtil.random(DA_HANH_TRINH_V3.length)];
            } else if (20 > ZUtil.random(120)) {
                id_random = DA_HANH_TRINH_V2[ZUtil.random(DA_HANH_TRINH_V2.length)];
            } else if (70 > ZUtil.random(120)) {
                id_random = DA_HANH_TRINH_V1[ZUtil.random(DA_HANH_TRINH_V1.length)];
            } else {
                id_random = DA_HANH_TRINH_V0[ZUtil.random(DA_HANH_TRINH_V0.length)];
            }
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id_random);
            GiftBox giftBox = new GiftBox();
            giftBox.id = (short) id_random;
            giftBox.type = 4;
            giftBox.name = itemTemplate4.name;
            giftBox.icon = itemTemplate4.icon;
            giftBox.num = 1;
            giftBox.color = 0;
            listGift.add(giftBox);
        }
        return listGift;
    }

    public static List<GiftBox> getGiftBossPica(Player p) {
        List<GiftBox> listGift = new ArrayList<>();
        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(0);
        GiftBox giftBox = new GiftBox();
        giftBox.id = itemTemplate4.id;
        giftBox.type = 4;
        giftBox.name = itemTemplate4.name;
        giftBox.icon = itemTemplate4.icon;
        giftBox.num = ZUtil.random(10_000, 200_000);
        giftBox.color = 0;
        listGift.add(giftBox);
        if (60 > ZUtil.random(120)) { // bot vang
            GiftBox gb_ = new GiftBox();
            ItemTemplate7 it_temp7 = ItemTemplate7.get_it_by_id(4);
            if (it_temp7 != null) {
                gb_.id = it_temp7.id;
                gb_.type = 7;
                gb_.name = it_temp7.name;
                gb_.icon = it_temp7.icon;
                gb_.num = ZUtil.random(2, 10);
                gb_.color = 0;
                listGift.add(gb_);
            }
        }
        if (40 > ZUtil.random(120)) { // ruong dai ac quy
            GiftBox gb_ = new GiftBox();
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(158);
            if (it_temp4 != null) {
                gb_.id = it_temp4.id;
                gb_.type = 4;
                gb_.name = it_temp4.name;
                gb_.icon = it_temp4.icon;
                gb_.num = 1;
                gb_.color = 0;
                listGift.add(gb_);
            }
        }
        if (p.level > 9 && 60 > ZUtil.random(120)) { // ruong cam
            GiftBox gb_ = new GiftBox();
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id((p.level / 10) + 121);
            if (it_temp4 != null) {
                gb_.id = it_temp4.id;
                gb_.type = 4;
                gb_.name = it_temp4.name;
                gb_.icon = it_temp4.icon;
                gb_.num = 1;
                gb_.color = 0;
                listGift.add(gb_);
            }
        }
        if (40 > ZUtil.random(120)) { // da 3-4
            short[] id_random = new short[] { 46, 52, 58, 64, 70, 76, 47, 53, 59, 65, 71, 77 };
            GiftBox gb_ = new GiftBox();
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(id_random[ZUtil.random(id_random.length)]);
            if (it_temp4 != null) {
                gb_.id = it_temp4.id;
                gb_.type = 4;
                gb_.name = it_temp4.name;
                gb_.icon = it_temp4.icon;
                gb_.num = 1;
                gb_.color = 0;
                listGift.add(gb_);
            }
        }
        return listGift;
    }

    /**
     * Tính toán số lượng ô trống hành trang cần thiết cho danh sách quà.
     * Tiền tệ (Beri, Ruby, Extol), EXP nhân vật, XP skill, Cải trang, Pet... hoàn toàn KHÔNG tốn ô hành trang.
     * Vật phẩm cộng dồn (type 4, 7) nếu nhét vừa vào stack hiện có thì KHÔNG tốn thêm ô trống mới.
     */
    public static int getRequiredBagSlots(Player p, List<GiftBox> gifts) {
        if (gifts == null || gifts.isEmpty() || p == null || p.item == null) return 0;
        int required = 0;
        final int finalMaxStack = (template.DataTemplate.MAX_ITEM_IN_BAG > 0) ? template.DataTemplate.MAX_ITEM_IN_BAG : 9999;
        java.util.Map<String, Integer> simFreeSpace = new java.util.HashMap<>();

        for (GiftBox gb : gifts) {
            if (gb == null || gb.num <= 0) continue;
            if (gb.type == 99) continue; // XP nhân vật (không tốn ô hành trang)
            if (gb.type == 105) continue; // Cải trang thời trang (lưu vào p.fashion)
            if (gb.type == 110) continue; // Pet có thời hạn (lưu vào p.my_pet)

            if (gb.type == 4 || gb.type == 7) {
                if (gb.type == 4) {
                    // Các loại tiền tệ và tài nguyên hệ thống
                    if (gb.id == 0 || gb.id == 1 || gb.id == 2 || gb.id == 908 || gb.id == 6 || gb.id == 333) continue; // Beri, Ruby, Extol, Vé, XP skill
                    if (gb.id == -10 || gb.id == -11 || gb.id == -12) continue; // Clan XP / Beri / Ruby
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(gb.id);
                    if (it4 != null && it4.type == -16) continue; // Đá hành trình có kho riêng
                }

                String key = gb.type + "_" + gb.id;
                int freeSpace = simFreeSpace.computeIfAbsent(key, k -> {
                    int space = 0;
                    if (p.item.bag47 != null) {
                        for (ItemBag47 it : p.item.bag47) {
                            if (it != null && it.category == gb.type && it.id == gb.id) {
                                if (it.quant < finalMaxStack) {
                                    space += (finalMaxStack - it.quant);
                                }
                            }
                        }
                    }
                    return space;
                });

                if (gb.num <= freeSpace) {
                    simFreeSpace.put(key, freeSpace - gb.num);
                } else {
                    int overflow = gb.num - freeSpace;
                    int slotsNeeded = (overflow + finalMaxStack - 1) / finalMaxStack;
                    required += slotsNeeded;
                    simFreeSpace.put(key, (slotsNeeded * finalMaxStack) - overflow);
                }
            } else {
                // Trang bị, vật phẩm category 3 hoặc không xếp chồng
                required += Math.max(1, gb.num);
            }
        }
        return required;
    }

    /**
     * Kiểm tra người chơi có đủ ô trống hành trang để nhận trọn bộ danh sách quà không.
     */
    public static boolean hasEnoughBagSpace(Player p, List<GiftBox> gifts) {
        if (p == null || p.item == null) return false;
        int needed = getRequiredBagSlots(p, gifts);
        return p.item.able_bag() >= needed;
    }

    /**
     * Gửi quà cho người chơi:
     * - Nếu đủ ô trống hành trang -> hiện bảng nhận quà và add trực tiếp (Service.send_gift như cũ).
     * - Nếu không đủ ô trống -> chuyển quà an toàn vào Hộp Thư (MailService.sendMail) vĩnh viễn cho đến khi nhận.
     */
    public static void sendGiftOrMail(Player p, int type, String title, String notice, List<GiftBox> gifts, boolean showTable) {
        if (p == null || p.isBot || p.conn == null) return;
        if (gifts == null || gifts.isEmpty()) return;
        try {
            gifts = template.GiftBox.consolidateGifts(gifts);
            for (GiftBox gb : gifts) {
                MailService.populateGiftMetadata(gb);
            }
            if (hasEnoughBagSpace(p, gifts)) {
                network.Service.send_gift(p, type, title, notice, gifts, showTable);
            } else {
                core.MailService.sendMail(p, "Hệ Thống", title,
                        "Hành trang của bạn không đủ chỗ trống để nhận quà (" + (notice != null ? notice : "") + "), phần thưởng đã được chuyển an toàn vào Hộp Thư!",
                        core.MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống, phần thưởng [" + title + "] đã được gửi vào Hộp Thư!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
