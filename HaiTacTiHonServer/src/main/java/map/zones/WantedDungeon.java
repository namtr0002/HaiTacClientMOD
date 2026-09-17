package map.zones;

import model.Player;
import map.Zone;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WantedDungeon extends zabstracts.AbsTreasureDungeon {

    @Override
    public void create() {}

    @Override
    public void update(Zone zone) throws IOException {
        updateLobby(zone);
    }

    public static void updateLobby(Zone zone) throws IOException {
        if (zone == null || zone.template == null) return;

        if (zone.template.id == 119) { // Phòng chờ Đấu Trường Truy Nã
            // 1. Duy trì số lượng Bot đứng chờ tại Map 119 tạo không khí đông đúc
            boolean hasRealPlayer = false;
            int botCount = 0;
            Player sampleRealPlayer = null;

            for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                Player p0 = zone.onlyPlayers.get(i);
                if (p0 != null && !p0.isBot && p0.conn != null) {
                    hasRealPlayer = true;
                    if (sampleRealPlayer == null) sampleRealPlayer = p0;
                } else if (p0 != null && p0.isBot) {
                    botCount++;
                }
            }

            // Nếu có người chơi thật mà số bot < 3 -> Spawn bot đứng chờ phù hợp cấp độ
            if (hasRealPlayer && botCount < 3 && sampleRealPlayer != null) {
                try {
                    int fakeBotId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                    bot.BotPVP waitBot = bot.BotPVP.createAnalyzedBot(fakeBotId, sampleRealPlayer);
                    if (waitBot != null) {
                        waitBot.type_pk = -1;
                        waitBot.join(zone, (short) ZUtil.random(300, 650), (short) ZUtil.random(240, 280));
                    }
                } catch (Exception ignored) {}
            }

            // Nếu không còn người chơi thật nào trong phòng chờ -> Dọn dẹp bot
            if (!hasRealPlayer && botCount > 0) {
                List<Player> botsToRemove = new ArrayList<>();
                for (Player p0 : zone.onlyPlayers) {
                    if (p0 != null && p0.isBot && p0 instanceof bot.BotPVP) {
                        botsToRemove.add(p0);
                    }
                }
                for (Player b : botsToRemove) {
                    try {
                        ((bot.BotPVP) b).leave();
                    } catch (Exception ignored) {}
                }
            }

            // Đã tắt chat bot ở phòng chờ pvp truy nã (map 119)

            // 2. Xử lý ghép trận tìm kiếm đối thủ trong hàng đợi
            Player[] p0 = activities.Wanted.get_p_random_waiting();
            if (p0 != null && p0[0] != null && p0[1] != null) {
                p0[0].type_pk = -1;
                p0[1].type_pk = -1;
                
                MapPvp pvpInstance = MapPvp.createDungeon(p0[0], p0[1], (byte) 2);
                pvpInstance.join();
            }
        }
    }
}

