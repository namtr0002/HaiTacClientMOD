package bot;

import model.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import map.MapManager;
import map.Zone;
import database.IDManager;

/**
 * BotTruyNa — Hệ thống Thợ Săn Hải Tặc Truy Nã đa thể hiện (Multi-Instance Bot Pool).
 * Hỗ trợ tạo động nhiều Thợ săn đồng thời khi có nhiều người chơi cùng thuê săn tiền thưởng.
 */
public class BotTruyNa extends Bot {

    public static BotTruyNa botThoSan; // Tương thích ngược
    public static final List<BotTruyNa> botPool = Collections.synchronizedList(new ArrayList<>());
    public static final List<Integer> idmap = new ArrayList<>();

    public static BotTruyNa getBot(Player p) {
        if (p == null) return null;
        synchronized (botPool) {
            for (BotTruyNa b : botPool) {
                if (b != null && b.pTarget != null && b.pTarget.name.equals(p.name)) {
                    b.pTarget = p;
                    return b;
                }
            }
        }
        if (botThoSan != null && botThoSan.pTarget != null && botThoSan.pTarget.name.equals(p.name)) {
            botThoSan.pTarget = p;
            return botThoSan;
        }
        return null;
    }

    public short idxThue;
    public BotTruyNa p; // compatibility field
    public long time;
    public long timeAtk;
    public Player pTarget;

    public BotTruyNa(int id, String name) throws Exception {
        super(id, name);
        this.type_pk = -1;
        this.p = this;
        this.botTruyNa = this;
    }

    @Override
    public void join(Zone map, short x, short y) {
        this.type_pk = -1;
        this.map = map;
        this.x = x;
        this.y = y;
        if (this.ability != null) {
            this.hp = this.ability.get_hp_max(true);
            this.mp = this.ability.get_mp_max(true);
        }
        if (map != null && isMap(map.template.id)) {
            try {
                map.getService().move((byte) 0, this.index_map, this.x, this.y);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.enter_map(this);
        }
    }

    @Override
    public void leave() {
        if (this.pTarget != null) {
            if (this.pTarget.map != null) {
                this.pTarget.map.remove_obj(this.index_map, 0);
                this.pTarget.map.leave_map(this, 0);
            }
            this.pTarget.botTruyNa = null;
            this.pTarget = null;
        }
        this.setAttack(null);
        this.setMove(null);
        this.map = null;
    }

    @Override
    public void update() {
        if (this.isdie || this.time < System.currentTimeMillis()) {
            this.leave();
            return;
        }
        super.update();
    }

    public static void initClass() {
        int idxVal = -31_000;
        try {
            for (int i = 1; i <= 5; i++) {
                BotTruyNa pCreate = BotFactory.getInstance().newBotTruyNa(idxVal + i, "botbtds" + i, bot.botplayer.BotNameGenerator.getRandomBotName(), null);
                if (pCreate != null) {
                    pCreate.x = -1;
                    pCreate.y = -1;
                    pCreate.type_pk = -1;
                    botPool.add(pCreate);
                    if (i == 1) {
                        botThoSan = pCreate;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] mapcanjoin = new int[]{1, 220, 2, 3, 4, 6, 9, 221, 10, 11, 12, 14, 191, 192, 193, 194, 195, 196, 197, 113, 112, 115, 116, 117, 118, 124, 125, 126,
            93, 229, 94, 95, 96, 97, 98, 99, 100, 101, 103, 83, 228, 84, 85, 86, 88, 69, 227, 70, 71, 72, 74, 49, 226, 50, 51, 52, 54, 41, 225, 42, 43, 44, 46,
            33, 224, 34, 35, 36, 38, 25, 223, 26, 27, 28, 30, 17, 222, 18, 19, 20, 22};
        for (int i = 0; i < MapManager.getInstance().getMaps().size(); i++) {
            if (MapManager.getInstance().getMap(i).zones[0].list_mob.length > 2) {
                idmap.add(MapManager.getInstance().getMap(i).zones[0].template.id);
            }
        }
        for (int i = 0; i < mapcanjoin.length; i++) {
            idmap.add(mapcanjoin[i]);
        }
    }

    public static boolean ok(Player p, int idx) throws IOException {
        Player p0 = Zone.get_player_by_Index_allmap(idx);
        if (p0 != null) {
            BotTruyNa b = getAvailableBot();
            if (b != null) {
                p0.botTruyNa = b;
                b.pTarget = p0;
                b.idxThue = p.index_map;
                b.time = System.currentTimeMillis() + 60_000 * 10;
                
                // Khởi tạo toàn bộ Chỉ số, Trang bị (Full Hoàn Mỹ, Đá Khảm, Kích Ẩn, TAQ), Kỹ năng chuẩn cân bằng theo p0
                b.clazz = (byte) (b.clazz > 0 ? b.clazz : core.ZUtil.random(1, 5));
                b.setupBotBalancedAgainst(p0);
                
                p.getService().send_box_ThongBao_OK("Kích hoạt Thợ Săn Hải Tặc săn mục tiêu " + p0.name + " thành công!");

                b.setAttack(new bot.AttackTarget(p0));
                b.setMove(new bot.MoveToTarget(p0, 1800));
                
                b.join(p0.map, p0.x, p0.y);

                return true;
            } else {
                p.getService().send_box_ThongBao_OK("Hiện tại toàn bộ Thợ Săn đều đang bận. Vui lòng thử lại sau!");
            }
        }
        return false;
    }

    public static boolean isMap(int id) {
        for (int i = 0; i < idmap.size(); i++) {
            if (idmap.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    public static BotTruyNa getAvailableBot() {
        synchronized (botPool) {
            for (BotTruyNa b : botPool) {
                if (b != null && b.pTarget == null) {
                    if (b.isdie) {
                        b.isdie = false;
                        if (b.ability != null) {
                            b.hp = b.ability.get_hp_max(true);
                            b.mp = b.ability.get_mp_max(true);
                        }
                    }
                    return b;
                }
            }
        }
        // Nếu tất cả bot trong pool đều bận, tạo động 1 bot mới từ BotFactory!
        try {
            int botId = IDManager.takeID(IDManager.FAKE_BOT);
            BotTruyNa newBot = BotFactory.getInstance().newBotTruyNa(botId, "botbtds_" + botId, bot.botplayer.BotNameGenerator.getRandomBotName(), null);
            if (newBot != null) {
                newBot.x = -1;
                newBot.y = -1;
                newBot.type_pk = -1;
                botPool.add(newBot);
                return newBot;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
