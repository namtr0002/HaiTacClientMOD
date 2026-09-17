package event.eboss;

import model.Player;
import core.Manager;
import core.ZUtil;
import event.Event;
import event.EventData;
import event.SuKienBigMom;
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
 * BigMom — Boss Sự Kiện Big Mom (Mob Template ID: 172, Level: 0, Thông Thạo / Bậc: 99, HP: 3000).
 *
 * - Set level = 0, boss_inf.levelBoss = 99 để client hiển thị chuẩn "Level: 0 - Thông Thạo: 99" (Bậc 99).
 * - Miễn nhiễm hoàn toàn sát thương từ đánh thường và kỹ năng.
 * - Chỉ nhận cống nạp Bánh Kem Mê Hoặc (876) và Bánh Kem Siêu Cấp (877) qua phím 4/5 hoặc túi đồ.
 * - Chỉ xuất hiện tại các bản đồ ngoài làng (như 1-1, 2-1...).
 */
public class BigMom extends AbsEventBoss {

    public static final int MOB_TEMPLATE_ID = 172;
    public static final int MAX_HP = 3000;
    public static final int LEVEL = 0;
    public static final byte THONG_THAO_BAC = 99;

    public long timeRespawn = 0;
    public int targetMapId;
    public int targetZoneId;

    public BigMom(int slotId, int mapId, int zoneId) {
        super(slotId,
              MOB_TEMPLATE_ID,
              MAX_HP,
              LEVEL,
              mapId, zoneId,
              "Big Mom (Bậc 99)");
        this.targetMapId = mapId;
        this.targetZoneId = zoneId;
        this.mob.hp_max = MAX_HP;
        this.mob.hp = MAX_HP;
        this.mob.level = LEVEL; // Level 0
        this.mob.isdie = false;
        this.mob.keyflag = "BigMom";

        // Gắn boss_inf để client nhận lvthongthao (levelBoss) = 99 -> hiển thị Bậc / Thông Thạo 99
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
            public boolean isSieuTrum() { return true; }
            @Override
            public void dropReward(Player p, java.util.List<template.GiftBox> list) {}
            @Override
            public void update() {}
        };
        bossInfo.levelBoss = THONG_THAO_BAC;
        bossInfo.level = LEVEL;
        this.mob.boss_inf = bossInfo;
    }

    @Override
    public short getDefaultX() {
        return 500;
    }

    @Override
    public short getDefaultY() {
        return 250;
    }

    /**
     * Chặn toàn bộ sát thương tấn công/skill — Big Mom miễn nhiễm sát thương vật lý/phép!
     */
    public long modifyIncomingDamage(Player p, long rawDamage) {
        return 0;
    }

    /**
     * Xử lý khi người chơi cống nạp bánh ngọt cho Big Mom.
     * @param p Người chơi dâng bánh
     * @param cakeId ID item bánh (876: Mê Hoặc, 877: Siêu Cấp)
     * @return true nếu đã xử lý cống nạp
     */
    public synchronized boolean offerCake(Player p, int cakeId) {
        if (p == null || this.mob == null) return false;

        if (p.level < 40) {
            try {
                p.getService().send_box_ThongBao_OK("Cần đạt cấp độ 40 trở lên mới có thể cống nạp bánh cho Big Mom!");
            } catch (Exception ignored) {}
            return false;
        }

        if (this.mob.isdie || this.mob.hp <= 0) {
            try {
                p.getService().send_box_ThongBao_OK("Big Mom đã no nê bánh ngọt và tạm thời rời đi!");
            } catch (Exception ignored) {}
            return false;
        }

        int points;
        int hpDeduct;
        int successRate;
        String cakeName;

        if (cakeId == SuKienBigMom.ITEM_BANH_SIEU_CAP) { // 877
            points = 5;
            hpDeduct = 5;
            successRate = 85; // 85% thành công
            cakeName = "Bánh Kem Siêu Cấp";
        } else if (cakeId == SuKienBigMom.ITEM_BANH_ME_HOAC) { // 876
            points = 1;
            hpDeduct = 1;
            successRate = 70; // 70% thành công
            cakeName = "Bánh Kem Mê Hoặc";
        } else {
            return false;
        }

        if (p.item.total_item_bag_by_id(4, cakeId) < 1) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn không có " + cakeName + " trong hành trang!");
            } catch (Exception ignored) {}
            return false;
        }

        // Trừ 1 bánh khi cống nạp
        p.item.remove_item47(4, cakeId, 1);
        p.item.updateInventory(false);

        boolean success = ZUtil.random(100) < successRate;

        if (success) {
            // Trừ máu Big Mom
            this.mob.hp = Math.max(0, this.mob.hp - hpDeduct);

            // Cộng điểm hài lòng cho người chơi
            EventData evData = p.getDataEvent(Event.ID_SUKIEN_BIGMOM);
            if (evData != null) {
                if (evData.data != null && evData.data.length > SuKienBigMom.IDX_DIEM_HAI_LONG) {
                    evData.data[SuKienBigMom.IDX_DIEM_HAI_LONG] += points;
                }
                evData.addPoint("diem_hai_long", points);
            }
            p.update_pointEvent1(points);

            // Trao thưởng ngẫu nhiên
            int beri = points * (20_000 + ZUtil.random(15_000));
            long exp = points * (50_000L + ZUtil.random(30_000));

            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, exp)));
            listGift.add(new template.GiftBox(4, 0, beri));

            // Tỉ lệ nhận Huy Hiệu Big Mom (878)
            int hhCount = ZUtil.random(1, points);
            listGift.add(new template.GiftBox(4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, hhCount));

            // Tỉ lệ nhận Mảnh Ghép Tiểu Merry (879)
            if (ZUtil.random(100) < (cakeId == SuKienBigMom.ITEM_BANH_SIEU_CAP ? 25 : 8)) {
                listGift.add(new template.GiftBox(4, SuKienBigMom.ITEM_MANH_TIEU_MERRY, 1));
            }

            // Tỉ lệ nhận Tinh thể đá (135)
            int randDaMai = ZUtil.random(100);
            int daMai = 0;
            if (randDaMai < 10) daMai = 5;
            else if (randDaMai < 30) daMai = 3;
            else if (randDaMai < 60) daMai = 2;
            else if (randDaMai < 100) daMai = 1;
            if (daMai > 0) {
                listGift.add(new template.GiftBox(4, 135, daMai));
            }

            // Tỉ lệ nhận Ruby
            if (ZUtil.random(100) < (cakeId == SuKienBigMom.ITEM_BANH_SIEU_CAP ? 20 : 5)) {
                int ruby = ZUtil.random(1, 3);
                listGift.add(new template.GiftBox(4, 1, ruby));
            }

            // Tỉ lệ nhận Rương Cam theo cấp độ
            if (ZUtil.random(100) < 15) {
                int ruongId = Math.min(120, Math.max(111, 111 + (p.level / 10)));
                listGift.add(new template.GiftBox(4, ruongId, 1));
            }

            // Tỉ lệ nhận Rương Ác Quỷ / Đại Ác Quỷ
            if (ZUtil.random(1000) < (cakeId == SuKienBigMom.ITEM_BANH_SIEU_CAP ? 15 : 3)) {
                int ruongAq = ZUtil.random(2) == 0 ? 29 : 158;
                listGift.add(new template.GiftBox(4, ruongAq, 1));
            }

            p.item.updateInventory(false);

            // Thông báo và lời thoại Big Mom
            String[] happyChats = {
                "Mama mama! " + cakeName + " ngon tuyệt cú mèo! Thêm nữa đi nào!",
                "Hô hô hô! Vị bánh ngọt ngào làm sao, ta rất hài lòng!",
                "Măm măm! Tuyệt phẩm bánh ngọt! Ngươi làm tốt lắm!",
                "Mama mama! Bánh này thật mê hoặc lòng người! Hãy nhận lấy quà của ta!"
            };
            String chat = happyChats[ZUtil.random(happyChats.length)];
            sendMobChat(chat);

            core.RewardService.sendGiftOrMail(p, 1, "Cống Nạp Big Mom", "Cống nạp thành công " + cakeName + " (+" + points + " Điểm Hài Lòng)", listGift, true);

            // Kiểm tra nếu Big Mom đã hết máu (no bánh)
            if (this.mob.hp <= 0) {
                this.mob.hp = 0;
                this.mob.isdie = true;
                onDeath(p, this.mob);
            }
        } else {
            // Xịt (thất bại)
            String[] failChats = {
                "Mama mama! Bánh này dở tệ, không đúng khẩu vị của ta!",
                "Phì! Bánh gì mà ngọt gắt quá, ta không thích!",
                "Ngươi dám đem bánh chưa đạt chuẩn dâng cho ta sao?! Mama mama!"
            };
            String chat = failChats[ZUtil.random(failChats.length)];
            sendMobChat(chat);

            int consolBeri = 5_000;
            List<template.GiftBox> consolGifts = new ArrayList<>();
            consolGifts.add(new template.GiftBox(4, 0, consolBeri));
            core.RewardService.sendGiftOrMail(p, 1, "Cống Nạp Big Mom (Thất Bại)", "Big Mom không thích hương vị này. Nhận quà an ủi", consolGifts, true);
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
    public void onKillRewardAll(Player killer) {
        if (killer == null || killer.map == null) return;

        List<MainItem> list = new ArrayList<>();
        list.add(new MainItem(SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 4, ZUtil.random(3, 6)));
        list.add(new MainItem(135, 4, ZUtil.random(2, 5))); // Tinh thể đá
        list.add(new MainItem(SuKienBigMom.ITEM_MANH_TIEU_MERRY, 4, 1)); // Mảnh ghép Merry
        list.add(new MainItem(ZUtil.random(115, 120), 4, 1)); // Rương cam

        if (ZUtil.random(100) < 20) {
            list.add(new MainItem(158, 4, 1)); // Rương đại ác quỷ
        }

        for (Player p : killer.map.players) {
            if (p != null && p.IDPlayer != killer.IDPlayer && !p.isBot) {
                p.update_vang(500_000);
                p.update_ngoc(10);
                try { p.updateMoney(); } catch (Exception ignored) {}
                MainItem.showGiftBox(p, "Phần thưởng chung Big Mom", "Big Mom No Nê", list, true, true);
            }
        }
    }

    @Override
    public void onKillReward(Player killer) {
        if (killer == null) return;

        killer.update_vang(2_000_000);
        killer.update_ngoc(50);
        try { killer.updateMoney(); } catch (Exception ignored) {}

        List<MainItem> list = new ArrayList<>();
        list.add(new MainItem(SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 4, 10));
        list.add(new MainItem(SuKienBigMom.ITEM_MANH_TIEU_MERRY, 4, 3));
        list.add(new MainItem(135, 4, 10)); // Tinh thể đá
        list.add(new MainItem(158, 4, 2));  // Rương đại ác quỷ
        list.add(new MainItem(120, 4, 2));  // Rương cam cấp cao

        MainItem.showGiftBox(killer, "Thưởng Cống Nạp Cuối Cùng", "Big Mom Thỏa Mãn", list, true, true);
    }

    @Override
    public void afterDeath(Player killer, Mob mob) {
        Manager.gI().chatKTG(0, "Big Mom tại " + (mob.map != null ? mob.map.template.name : "bản đồ")
                + " đã được " + (killer != null ? killer.name : "các hải tặc")
                + " cống nạp đủ 3000 bánh ngọt và no nê rời đi!", 0);

        // Hồi sinh sau 15 phút (900.000 ms)
        this.timeRespawn = System.currentTimeMillis() + 900_000L;
    }

    @Override
    public void update(Mob mob) {
        // Tự động hồi sinh khi hết thời gian chờ
        if (this.mob.isdie && this.timeRespawn > 0 && System.currentTimeMillis() >= this.timeRespawn) {
            this.timeRespawn = 0;
            respawn();
        }
    }

    public void respawn() {
        Zone[] zones = Zone.getMapByID(this.targetMapId);
        if (zones != null && this.targetZoneId < zones.length) {
            Zone z = zones[this.targetZoneId];
            this.mob.map = z;
            this.mob.hp = MAX_HP;
            this.mob.hp_max = MAX_HP;
            this.mob.level = LEVEL;
            if (this.mob.boss_inf != null) {
                this.mob.boss_inf.levelBoss = THONG_THAO_BAC;
                this.mob.boss_inf.level = LEVEL;
            }
            this.mob.isdie = false;
            this.mob.x = getDefaultX();
            this.mob.y = getDefaultY();
            z.mobs.putIfAbsent(this.mob.index, this.mob);
            this.mob.sendMove();
            Manager.gI().chatKTG(0, "Big Mom đã xuất hiện lại tại " + z.template.name + "! Hãy mau đến cống nạp bánh ngọt!", 5);
        }
    }
}
