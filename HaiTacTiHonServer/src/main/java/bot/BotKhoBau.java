package bot;

import model.Player;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Zone;

/**
 * BotKhoBau — Vệ Binh Đảo Kho Báu với cơ chế nộ nổ kỹ năng khi sinh lực xuống thấp.
 */
public class BotKhoBau extends Bot {

    public static final short[] idMap = new short[]{999};
    public static final List<String> id = new ArrayList<>();
    public static final List<String> idx = new ArrayList<>();
    public static final List<String> nameQueue = new ArrayList<>();

    private boolean isEnraged = false;
    private long lastEnrageSkillTime = 0;

    public BotKhoBau(int id, String name) throws Exception {
        super(id, name);
        this.type_pk = -1;
    }

    @Override
    public void init() {
        this.type_pk = -1;
        this.setAttack(new bot.AttackAround());
        this.setMove(new bot.MoveAround(15000));
        if (this.ability != null) {
            this.hp = this.ability.get_hp_max(true);
            this.mp = this.ability.get_mp_max(true);
        }
    }

    @Override
    public void update() {
        if (this.isdie) {
            return;
        }

        // Cơ chế Nổ Nộ (Enrage Mode) khi sinh lực < 30%
        if (this.ability != null) {
            long hpMax = this.ability.get_hp_max(true);
            if (hpMax > 0 && ((double) this.hp / hpMax) < 0.30 && !isEnraged) {
                isEnraged = true;
                if (this.map != null) {
                    try {
                        this.map.send_chat_popup(0, this.index_map, "SỨC MẠNH KHO BÁU BỘC PHÁ!!!");
                    } catch (Exception ignored) {}
                }
            }

            if (isEnraged) {
                long now = System.currentTimeMillis();
                if (now - lastEnrageSkillTime > 4000) {
                    lastEnrageSkillTime = now;
                    // Hồi phục 10% máu và bộc phát nổ chiêu diện rộng
                    this.hp = (int) Math.min(hpMax, this.hp + (hpMax * 0.10));
                    if (this.map != null) {
                        try {
                            this.map.send_chat_popup(0, this.index_map, "CUỒNG PHONG KHO BÁU BỘC PHÁ!");
                        } catch (Exception ignored) {}
                    }
                }
            }
        }

        super.update();
    }

    public static BotKhoBau create(int id, String username, String name, Zone targetZone) {
        return BotFactory.getInstance().newBotKhoBau(id, username, name, targetZone);
    }

    public static void initClass() {
        int idxVal = -32_000;
        try {
            for (int i = 1; i <= 10; i++) {
                Zone[] map = Zone.getMapByID(idMap[ZUtil.random(idMap.length)]);
                if (map == null || map.length == 0) {
                    continue;
                }
                Zone targetZone = map[ZUtil.random(map.length)];
                BotKhoBau.create(idxVal + i, "botbt" + i, bot.botplayer.BotNameGenerator.getRandomBotName(), targetZone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Player> getBot(Zone map) {
        List<Player> result = new ArrayList<>();
        if (map == null) return result;
        for (int i = 0; i < map.players.size(); i++) {
            Player pl = map.players.get(i);
            if (pl instanceof BotKhoBau) {
                result.add(pl);
            }
        }
        return result;
    }

    public static void updateSpawning() throws IOException {
        if (!id.isEmpty()) {
            String idd = id.remove(0);
            String idxx = idx.remove(0);
            String namee = nameQueue.remove(0);
            
            Zone[] map = Zone.getMapByID(idMap[ZUtil.random(idMap.length)]);
            if (map != null && map.length > 0) {
                Zone targetZone = map[ZUtil.random(map.length)];
                BotKhoBau.create(Integer.parseInt(idxx), "botbt" + idd, namee, targetZone);
            }
        }
    }
}
