package event.eboss;

import model.Player;
import core.Manager;
import core.ZUtil;
import event.Event;
import event.EventData;
import event.SuKienTetThieuNhi;
import itemz.MainItem;
import map.Zone;
import mob.Mob;
import network.Message;
import zabstracts.AbsBoss;
import zabstracts.AbsEventBoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DuaBeThieuNhi — Bé Thiếu Nhi Sự Kiện Tết Thiếu Nhi 1/6
 *
 * - HP cố định 3000 (không giảm khi cho kẹo).
 * - Miễn nhiễm hoàn toàn sát thương từ đánh thường và kỹ năng.
 * - Di chuyển chạy nhảy quanh map (tự động sendMove và chat vui vẻ).
 * - Nhận Bánh qui (399), Kẹo 7 màu (400), Túi bánh ngọt (403), Kẹo bông gòn VIP (405) qua phím 4/5 hoặc túi đồ.
 * - Tỉ lệ xịt: KHÔNG BÁO GÌ (im lặng).
 * - Tỉ lệ thành công: Trao thưởng ngẫu nhiên Beri, EXP, Trái ác quỷ sơ-trung cấp, và tỉ lệ rất hiếm trái cao cấp (Nham thạch, Sét...).
 */
public class DuaBeThieuNhi extends AbsEventBoss {

    public static final int MOB_TEMPLATE_ID = 151; // Template Heo mini / Bé con
    public static final int MAX_HP = 3000;
    public static final int LEVEL = 1;

    public int targetMapId;
    public int targetZoneId;
    private long lastMoveTime = 0;
    private long lastChatTime = 0;

    public DuaBeThieuNhi(int slotId, int mapId, int zoneId) {
        super(slotId,
              MOB_TEMPLATE_ID,
              MAX_HP,
              LEVEL,
              mapId, zoneId,
              "Bé Thiếu Nhi");
        this.targetMapId = mapId;
        this.targetZoneId = zoneId;
        this.mob.hp_max = MAX_HP;
        this.mob.hp = MAX_HP;
        this.mob.level = LEVEL;
        this.mob.isdie = false;
        this.mob.keyflag = "DuaBeThieuNhi";

        AbsBoss bossInfo = new AbsBoss(MOB_TEMPLATE_ID) {
            @Override
            public void init() {}
            @Override
            public int getHpMax() { return MAX_HP; }
            @Override
            public int getLevel() { return LEVEL; }
            @Override
            public short[] getSkills() { return new short[]{0}; }
            @Override
            public short[] getSpawnLocation() { return new short[]{(short) targetMapId, getDefaultX(), getDefaultY()}; }
            @Override
            public boolean isSieuTrum() { return false; }
            @Override
            public void dropReward(Player p, java.util.List<template.GiftBox> list) {}
            @Override
            public void update() {}
        };
        bossInfo.levelBoss = 1;
        bossInfo.level = LEVEL;
        this.mob.boss_inf = bossInfo;
    }

    @Override
    public short getDefaultX() {
        return 450;
    }

    @Override
    public short getDefaultY() {
        return 280;
    }

    /**
     * Miễn nhiễm hoàn toàn sát thương tấn công/kỹ năng.
     */
    public long modifyIncomingDamage(Player p, long rawDamage) {
        return 0;
    }

    /**
     * Xử lý khi người chơi cho Bé Thiếu Nhi kẹo qua phím 4, 5 hoặc túi đồ.
     * @param p Người chơi cho kẹo
     * @param candyId ID item kẹo (399: Bánh qui, 400: Kẹo 7 màu, 403: Túi bánh, 405: Kẹo bông VIP)
     * @return true nếu đã xử lý
     */
    public synchronized boolean giveCandy(Player p, int candyId) throws IOException {
        if (p == null || this.mob == null || this.mob.isdie) return false;

        // Kiểm tra số lượng kẹo trong hành trang
        if (p.item.total_item_bag_by_id(4, candyId) < 1) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn không có vật phẩm này!");
            } catch (Exception ignored) {}
            return false;
        }

        if (p.item.able_bag() < 2) {
            try {
                p.getService().send_box_ThongBao_OK("Hành trang cần tối thiểu 2 ô trống để nhận quà!");
            } catch (Exception ignored) {}
            return false;
        }

        // Trừ 1 kẹo khi sử dụng
        p.item.remove_item47(4, candyId, 1);
        p.item.updateInventory(false);

        // Lưu ý: HP của đứa bé KHÔNG giảm, giữ nguyên 3000!
        this.mob.hp = MAX_HP;

        // Thiết lập tỉ lệ thành công và điểm số theo từng loại kẹo
        int successRate;
        int eventPoints;
        long expBase;
        String candyName;

        switch (candyId) {
            case SuKienTetThieuNhi.ITEM_KEO_BONG_GON: // 405 (VIP)
                successRate = 95; // 95% thành công
                eventPoints = 5;
                expBase = (long) p.level * 12_000L;
                candyName = "Kẹo Bông Gòn (VIP)";
                break;
            case SuKienTetThieuNhi.ITEM_TUI_BANH: // 403 (Thường)
                successRate = 85; // 85% thành công
                eventPoints = 2;
                expBase = (long) p.level * 6_000L;
                candyName = "Túi Bánh Ngọt";
                break;
            case SuKienTetThieuNhi.ITEM_KEO_7_MAU: // 400
                successRate = 75; // 75% thành công
                eventPoints = 1;
                expBase = (long) p.level * 3_000L;
                candyName = "Kẹo 7 Màu";
                break;
            case SuKienTetThieuNhi.ITEM_BANH_QUI: // 399
            default:
                successRate = 70; // 70% thành công
                eventPoints = 1;
                expBase = (long) p.level * 3_000L;
                candyName = "Bánh Qui";
                break;
        }

        boolean success = ZUtil.random(100) < successRate;

        if (success) {
            // Cộng điểm sự kiện
            p.update_pointEvent1(eventPoints);

            // Trao EXP & Tiền
            long expEarn = expBase + (long) ZUtil.random(5000, 20000);
            int beriEarn = ZUtil.random(50_000, 200_000);

            // Danh sách quà tặng ngẫu nhiên nhận được
            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
            giftList.add(new template.GiftBox(4, 0, beriEarn));

            if (candyId == SuKienTetThieuNhi.ITEM_KEO_BONG_GON) {
                int rubyEarn = ZUtil.random(1, 3);
                giftList.add(new template.GiftBox(4, 1, rubyEarn));
            }

            int roll = ZUtil.random(10000); // Roll tỉ lệ từ 0 đến 9999 (100.00%)

            // 1. Tỉ lệ RẤT THẤP: Trái Ác Quỷ Cao Cấp (Nham thạch, Sét, Chấn thiên, Bóng tối, Ánh sáng, Rương Đại Ác Quỷ) ~0.8% - 1.5%
            int highFruitChance = (candyId == SuKienTetThieuNhi.ITEM_KEO_BONG_GON) ? 150 : 60; // 1.5% VIP, 0.6% Thường
            if (roll < highFruitChance) {
                int[] highFruits = new int[]{
                    160, // Trái Sét
                    161, // Trái Nham Thạch
                    240, // Trái Chấn Thiên
                    427, // Trái Bóng Tối
                    869, // Trái Ánh Sáng
                    158  // Rương Đại Ác Quỷ
                };
                int fruitId = highFruits[ZUtil.random(highFruits.length)];
                giftList.add(new template.GiftBox(4, fruitId, 1));
            }
            // 2. Tỉ lệ VỪA PHẢI: Trái Ác Quỷ Sơ - Trung Cấp ~10% - 18%
            else if (roll < (candyId == SuKienTetThieuNhi.ITEM_KEO_BONG_GON ? 2000 : 1200)) {
                int[] midFruits = new int[]{
                    29,  // Rương Ác Quỷ
                    86,  // Trái Ác Quỷ
                    87,  // Trái Ác Quỷ Trung Cấp
                    32,  // Trái Lửa
                    33,  // Trái Cao Su
                    34,  // Trái Tuần Lộc
                    90,  // Trái Bò Tót
                    91,  // Trái Vẽ Vẽ
                    92,  // Trái Băng
                    93,  // Trái Cát
                    219, // Trái Báo Đốm
                    220, // Trái Chim Ưng
                    316, // Trái Sáp
                    317, // Trái Dao
                    318  // Trái Kilo
                };
                int fruitId = midFruits[ZUtil.random(midFruits.length)];
                giftList.add(new template.GiftBox(4, fruitId, 1));
            }
            // 3. Tỉ lệ Rương trang bị, thời trang, danh hiệu ~25%
            else if (roll < 4500) {
                int subRoll = ZUtil.random(100);
                if (subRoll < 25) {
                    giftList.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                } else if (subRoll < 50) {
                    giftList.add(new template.GiftBox(4, 739, 1)); // Hộp trang phục sự kiện
                } else if (subRoll < 60) {
                    giftList.add(new template.GiftBox(4, 787, 1)); // Danh Hiệu Thiếu Nhi
                } else if (subRoll < 80) {
                    int ruongCam = Math.min(121, Math.max(112, 112 + (p.level / 10)));
                    giftList.add(new template.GiftBox(4, ruongCam, 1)); // Rương đồ cam theo cấp
                } else {
                    int ruongTim = Math.min(146, Math.max(137, 137 + (p.level / 10)));
                    giftList.add(new template.GiftBox(4, ruongTim, 1)); // Rương đồ tím theo cấp
                }
            }
            // 4. Vật phẩm tiêu hao, cường hóa, khảm đá phổ biến (~55%)
            else {
                int subRoll = ZUtil.random(100);
                if (subRoll < 30) {
                    int botCount = ZUtil.random(1, 3);
                    giftList.add(new template.GiftBox(7, 1, botCount)); // Bột cường hóa
                } else if (subRoll < 55) {
                    int daMaiCount = ZUtil.random(1, 3);
                    giftList.add(new template.GiftBox(4, 135, daMaiCount)); // Tinh thể đá
                } else if (subRoll < 70) {
                    giftList.add(new template.GiftBox(4, 80, 1)); // Thẻ X2 kinh nghiệm
                } else if (subRoll < 85) {
                    int[] daKham = new int[]{44, 45, 50, 51, 56, 57, 62, 63, 68, 69, 74, 75};
                    int daId = daKham[ZUtil.random(daKham.length)];
                    giftList.add(new template.GiftBox(4, daId, 1));
                } else {
                    int buaId = ZUtil.random(2) == 0 ? 339 : 416; // Búa sơ cấp / Búa đục túi
                    giftList.add(new template.GiftBox(4, buaId, 1));
                }
            }

            p.item.updateInventory(false);

            // Lời thoại cảm ơn của Bé Thiếu Nhi
            String[] happyChats = {
                "Yay! " + candyName + " ngon quá! Cảm ơn anh/chị nhiều nha!",
                "Măm măm... ngọt lịm luôn! Bé tặng anh/chị món quà này nè!",
                "Thích quá đi thôi! Chúc anh/chị Tết Thiếu Nhi vui vẻ!",
                "Kẹo ngon tuyệt cú mèo! Em yêu anh/chị nhất trần đời!"
            };
            sendMobChat(happyChats[ZUtil.random(happyChats.length)]);

            core.RewardService.sendGiftOrMail(p, 1, "Cho Kẹo Bé Thiếu Nhi", candyName, giftList, true);
        } else {
            // XỊT (Thất bại): KHÔNG BÁO GÌ CẢ theo đúng yêu cầu!
            // Chỉ thỉnh thoảng có chat nhẹ của mob, không bật popup thông báo
            if (ZUtil.random(100) < 40) {
                String[] failChats = {
                    "Ưm... kẹo này hơi chua một xíu!",
                    "Hihi, kẹo dính răng bé rồi!",
                    "Bé làm rơi mất một miếng kẹo rồi, tiếc quá!"
                };
                sendMobChat(failChats[ZUtil.random(failChats.length)]);
            }
        }

        return true;
    }

    private void sendMobChat(String text) {
        if (this.mob == null || this.mob.map == null || text == null) return;
        try {
            Message m = new Message(-15);
            m.writer().writeByte(10); // chat
            m.writer().writeShort(this.mob.index);
            m.writer().writeByte(1); // mob
            m.writer().writeUTF(text);
            this.mob.map.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (Exception ignored) {}
    }

    @Override
    public void onKillRewardAll(Player killer) {}

    @Override
    public void onKillReward(Player killer) {}

    @Override
    public void update(Mob mob) {
        if (this.mob == null || this.mob.isdie || this.mob.map == null) return;

        long now = System.currentTimeMillis();

        // Di chuyển ngẫu nhiên quanh map mỗi 3 - 6 giây
        if (now - lastMoveTime >= (3000L + ZUtil.random(3000))) {
            lastMoveTime = now;
            short limitW = (this.mob.map.template != null && this.mob.map.template.maxW > 200) ? this.mob.map.template.maxW : (short) 1000;
            short limitH = (this.mob.map.template != null && this.mob.map.template.maxH > 200) ? this.mob.map.template.maxH : (short) 600;
            short newX = (short) Math.max(100, Math.min(limitW - 100, this.mob.x + ZUtil.random(-60, 60)));
            short newY = (short) Math.max(100, Math.min(limitH - 100, this.mob.y + ZUtil.random(-20, 20)));
            this.mob.x = newX;
            this.mob.y = newY;
            this.mob.sendMove();
        }

        // Thỉnh thoảng gọi xin kẹo mỗi 15 - 25 giây
        if (now - lastChatTime >= (15000L + ZUtil.random(10000))) {
            lastChatTime = now;
            String[] idleChats = {
                "Anh chị ơi, cho em xin kẹo với!",
                "Tết Thiếu Nhi có kẹo ăn là vui nhất trên đời!",
                "Em thích Bánh qui và Kẹo 7 màu lắm!",
                "Ai có Kẹo bông gòn VIP cho em không ạ?",
                "La la la... 1/6 Tết Thiếu Nhi thật là vui!",
                "Ai cho em kẹo em tặng quà bí mật nha!"
            };
            sendMobChat(idleChats[ZUtil.random(idleChats.length)]);
        }
    }
}
