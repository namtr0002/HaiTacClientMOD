package bot;

import ability.Ability;
import clan.Clan;
import core.ZUtil;
import map.Npc;
import map.Zone;
import map.zones.TranChienKhongLo;
import map.zones.ThuLinhBienKhoi;
import bot.botplayer.ai.Pathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotLobbyManager — Quản lý các nhóm Bot Bang Hội tự động:
 * <ul>
 *   <li>Khi phó bản <b>CHƯA MỞ</b>: Tự động di chuyển ra các map cày cấp ngang level để train quái, kiếm EXP đóng góp cho Bang Hội.</li>
 *   <li>Khi phó bản <b>MỞ RA</b>: Tự động quay trở về làng quanh NPC phó bản, tập hợp đội hình, chat rủ rê/hiệu triệu và tham gia phó bản bang.</li>
 *   <li>Khi phó bản <b>KẾT THÚC</b>: Rời phó bản và tiếp tục ra map luyện cấp cày tiếp để chờ đợt phó bản kế tiếp.</li>
 * </ul>
 */
public class BotLobbyManager {

    public static final List<BotLobbyBot> LOBBY_BOTS = new CopyOnWriteArrayList<>();

    public static class BotLobbyBot extends Bot {
        public Clan groupClan;
        public int villageMapId = 1;
        public int trainingMapId = 58;
        public short targetNpcX = 0;
        public short targetNpcY = 0;
        public String roleType = "PVP_BANG"; // PVP_BANG, THU_LINH, PHO_BAN, SAN_TRUM

        public enum ClanBotState {
            FARMING_TRAIN,
            RETURNING_TO_LOBBY,
            LOBBY_WAITING,
            IN_DUNGEON
        }

        public ClanBotState state = ClanBotState.FARMING_TRAIN;

        private long lastChatTime = 0;
        private long lastRoamTime = 0;
        private long lastDungeonCheckTime = 0;
        private long lastAttackTime = 0;

        public BotLobbyBot(int id, String name, Clan groupClan, String roleType, int villageMapId, int trainingMapId) throws Exception {
            super(id, name);
            this.groupClan = groupClan;
            this.clan = groupClan;
            this.roleType = roleType;
            this.villageMapId = villageMapId;
            this.trainingMapId = trainingMapId;
            this.type_pk = -1;
            this.typePirate = -1;
            this.disableBaseChat = true;
        }

        @Override
        public void init() {
            this.type_pk = -1;
            this.typePirate = -1;
            this.setAttack(new AttackAround());
            this.setMove(new MoveAround(2500L));
            this.lastChatTime = System.currentTimeMillis() + ZUtil.random(5000, 20000);
            this.lastRoamTime = 0;
            this.lastDungeonCheckTime = 0;

            if (this.ability != null) {
                this.hp = (int) Math.max(5000, this.ability.get_hp_max(true));
                this.mp = (int) Math.max(2000, this.ability.get_mp_max(true));
            }
            this.isdie = false;
        }

        public boolean isDungeonOpen() {
            try {
                if (this.groupClan != null && this.groupClan.map_create != null) {
                    return true;
                }
                activities.TimedDungeonManager.ScheduleConfig pvpBangCfg = activities.TimedDungeonManager.gI().getConfigs().get("PVP_BANG");
                activities.TimedDungeonManager.ScheduleConfig tcklCfg = activities.TimedDungeonManager.gI().getConfigs().get("TRAN_CHIEN_KHONG_LO");
                activities.TimedDungeonManager.ScheduleConfig tlbkCfg = activities.TimedDungeonManager.gI().getConfigs().get("THU_LINH_BIEN_KHOI");
                boolean pvpBangOpen = (pvpBangCfg != null && pvpBangCfg.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING);
                boolean tcklOpen = (tcklCfg != null && tcklCfg.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING);
                boolean thuLinhOpen = (tlbkCfg != null && tlbkCfg.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING);
                return pvpBangOpen || tcklOpen || thuLinhOpen;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void update() {
            long now = System.currentTimeMillis();

            if (this.isdie) {
                if (respawnTime == 0) {
                    respawnTime = now + 5000L + ZUtil.random(3000);
                }
                if (now >= respawnTime) {
                    respawnTime = 0;
                    this.isdie = false;
                    if (this.ability != null) {
                        this.hp = (int) Math.max(5000, this.ability.get_hp_max(true));
                        this.mp = (int) Math.max(2000, this.ability.get_mp_max(true));
                    }
                    if (isDungeonOpen()) {
                        returnToVillage();
                    } else {
                        goToTrainingMap();
                    }
                }
                return;
            } else {
                respawnTime = 0;
            }

            // Kiểm tra trạng thái phó bản mỗi 3 giây
            if (now - lastDungeonCheckTime > 3000L) {
                lastDungeonCheckTime = now;
                boolean dungeonOpen = isDungeonOpen();

                if (dungeonOpen) {
                    // PHÓ BẢN MỞ: Nếu đang ở map cày cấp -> lập tức quay về làng / vào phó bản
                    if (this.groupClan != null && this.groupClan.map_create != null) {
                        if (this.map != this.groupClan.map_create) {
                            joinDungeonMap(this.groupClan.map_create);
                        }
                    } else {
                        // Chưa có map instance -> quay về làng quanh NPC để tập hợp & ghép đội
                        if (this.map != null && this.map.template != null && this.map.template.id != this.villageMapId) {
                            returnToVillage();
                        }
                    }
                } else {
                    // PHÓ BẢN ĐÓNG: Nếu đang ở làng hoặc phó bản cũ -> tự động xuất phát ra map cày cấp ngang level
                    if (this.map != null && this.map.template != null) {
                        int curMapId = this.map.template.id;
                        boolean inDungeon = (this.map.map_little_garden != null || this.map.pvpBang != null || this.map.pvpBangMapFight != null);
                        if (curMapId == this.villageMapId || inDungeon || curMapId != this.trainingMapId) {
                            goToTrainingMap();
                        }
                    }
                }
            }

            if (this.map == null) return;

            // Xử lý hành vi theo từng khu vực hiện tại
            int currentMapId = this.map.template != null ? this.map.template.id : -1;

            if (currentMapId == this.villageMapId) {
                // --- ĐANG Ở LÀNG (Tập hợp quanh NPC phó bản) ---
                this.state = ClanBotState.LOBBY_WAITING;
                if (now - lastRoamTime > 4500) {
                    lastRoamTime = now;
                    if (this.targetNpcX > 0 && this.targetNpcY > 0) {
                        short newX = (short) Math.max(100, Math.min(this.map.template.maxW - 100, this.targetNpcX + ZUtil.random(-70, 70)));
                        short newY = (short) Math.max(150, Math.min(this.map.template.maxH - 80, this.targetNpcY + ZUtil.random(-35, 35)));
                        try {
                            this.x = newX;
                            this.y = newY;
                            this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        } catch (Exception ignored) {}
                    }
                }

                if (now >= this.lastChatTime) {
                    this.lastChatTime = now + ZUtil.random(15000, 32000);
                    speakLobbyChat();
                }
            } else if (this.map.map_little_garden != null || this.map.pvpBang != null || this.map.pvpBangMapFight != null) {
                // --- ĐANG TRONG PHÓ BẢN BANG ---
                this.state = ClanBotState.IN_DUNGEON;
                if (this.getAttack() == null) {
                    this.setAttack(new AttackAround());
                }
            } else {
                // --- ĐANG Ở MAP LUYỆN CẤP (Farming quái ngang level) ---
                this.state = ClanBotState.FARMING_TRAIN;
                if (this.getAttack() == null) {
                    this.setAttack(new AttackAround());
                }

                // Định kỳ đóng góp EXP cho Clan khi cày quái
                if (now - lastAttackTime > 10000L) {
                    lastAttackTime = now;
                    if (this.groupClan != null) {
                        synchronized (this.groupClan) {
                            this.groupClan.xp += ZUtil.random(10, 50);
                        }
                    }
                }

                // Chat cày cấp thể hiện tinh thần bang hội
                if (now >= this.lastChatTime) {
                    this.lastChatTime = now + ZUtil.random(25000, 45000);
                    speakTrainingChat();
                }
            }

            super.update();
        }

        private void goToTrainingMap() {
            try {
                Zone[] zones = Zone.getMapByID(this.trainingMapId);
                if (zones != null && zones.length > 0 && zones[0] != null) {
                    Zone targetZone = zones[0];
                    short spawnX = (short) Math.max(100, Math.min(targetZone.template.maxW - 100, 250 + ZUtil.random(150)));
                    short spawnY = (short) Math.max(150, Math.min(targetZone.template.maxH - 100, 200 + ZUtil.random(100)));
                    this.leave();
                    this.type_pk = -1;
                    this.typePirate = -1;
                    this.setAttack(new AttackAround());
                    this.join(targetZone, spawnX, spawnY);
                    this.state = ClanBotState.FARMING_TRAIN;
                }
            } catch (Exception ignored) {}
        }

        private void returnToVillage() {
            try {
                Zone[] zones = Zone.getMapByID(this.villageMapId);
                if (zones != null && zones.length > 0 && zones[0] != null) {
                    Zone villageZone = zones[0];
                    short targetX = this.targetNpcX > 0 ? (short) (this.targetNpcX + ZUtil.random(-60, 60)) : (short) (villageZone.template.maxW / 2);
                    short targetY = this.targetNpcY > 0 ? (short) (this.targetNpcY + ZUtil.random(-30, 30)) : (short) (villageZone.template.maxH / 2);
                    targetX = (short) Math.max(80, Math.min(villageZone.template.maxW - 80, targetX));
                    targetY = (short) Math.max(120, Math.min(villageZone.template.maxH - 60, targetY));

                    this.leave();
                    this.type_pk = -1;
                    this.typePirate = -1;
                    this.join(villageZone, targetX, targetY);
                    this.state = ClanBotState.LOBBY_WAITING;
                }
            } catch (Exception ignored) {}
        }

        private void joinDungeonMap(Zone dungeonZone) {
            try {
                if (dungeonZone == null) return;
                this.leave();
                if (dungeonZone.map_little_garden != null) {
                    byte pkFlag = (byte) (this.groupClan != null && this.groupClan.equals(dungeonZone.map_little_garden.clan1) ? 4 : 5);
                    this.type_pk = pkFlag;
                    dungeonZone.map_little_garden.join(this);
                } else if (dungeonZone.pvpBang != null) {
                    dungeonZone.pvpBang.join(this);
                } else {
                    this.join(dungeonZone, (short) (dungeonZone.template.maxW / 2), (short) (dungeonZone.template.maxH / 2));
                }
                this.state = ClanBotState.IN_DUNGEON;
            } catch (Exception ignored) {}
        }

        private void speakTrainingChat() {
            if (this.map == null) return;
            String clanName = this.groupClan != null ? this.groupClan.name : "Bang Hội";
            String[] chats = new String[]{
                "[" + clanName + "] Đang cày cấp đợi phó bản mở anh em ơi!",
                "[" + clanName + "] Cày nhanh lên level tí phó bản mở còn gánh team!",
                "[" + clanName + "] Bang ta quyết tâm săn boss phó bản đợt này!",
                "Anh em bang tranh thủ cày quái tăng EXP bang nào!",
                "Đang luyện skill chuẩn bị đánh phó bản lớn!"
            };
            String msg = chats[ZUtil.random(chats.length)];
            try {
                this.map.send_chat_popup(0, this.index_map, msg);
            } catch (Exception ignored) {}
        }

        private void speakLobbyChat() {
            if (this.map == null) return;
            String[] chats;

            switch (this.roleType) {
                case "PVP_BANG":
                    chats = new String[]{
                        "Ghép phó bản PVP Băng đi anh em ơi!",
                        "Bang mình đang tìm đối thủ ghép PVP Băng!",
                        "Ai vào ghép phó bản PVP Băng không nè?",
                        "Chuẩn bị vào trận PVP Băng nào anh em!"
                    };
                    break;
                case "THU_LINH":
                    chats = new String[]{
                        "Thủ Lĩnh Biển Khơi đang mở, ai ghép đội không?",
                        "Thủ Lĩnh Biển Khơi còn slot đi phó bản nè!",
                        "Cùng phe biển vào ghép phó bản nhận quà lớn nào!",
                        "Anh em tập hợp đánh Thủ Lĩnh Biển Khơi thôi!"
                    };
                    break;
                case "PHO_BAN":
                    chats = new String[]{
                        "Phó bản Khổng Lồ / Thử Thách Vệ Thần ghép đội nhanh nào!",
                        "Ai đi phó bản cùng bang không, vào ghép ngay!",
                        "Thiếu 1 slot đi phó bản nhận ngọc và đồ ngon!",
                        "Ghép phó bản đi các bạn ơi!"
                    };
                    break;
                default:
                    chats = new String[]{
                        "Ai ghép phó bản bang hội cùng không nè?",
                        "Bang mình đang tuyển slot đi phó bản chung!",
                        "Ghép đội đi anh em, nhận quà bao la!"
                    };
                    break;
            }

            String msg = chats[ZUtil.random(chats.length)];
            try {
                this.map.send_chat_popup(0, this.index_map, msg);
            } catch (Exception ignored) {}
        }
    }

    public static int getTrainingMapIdForLevel(short level, int botId) {
        if (level < 20) return 10 + (Math.abs(botId) % 4); // Map 10, 11, 12, 14
        if (level < 30) return 26 + (Math.abs(botId) % 4); // Map 26, 27, 28, 30
        if (level < 40) return 34 + (Math.abs(botId) % 4); // Map 34, 35, 36, 38
        if (level < 50) return 42 + (Math.abs(botId) % 4); // Map 42, 43, 44, 46
        if (level < 60) return 50 + (Math.abs(botId) % 4); // Map 50, 51, 52, 54
        if (level < 70) return 58 + (Math.abs(botId) % 4); // Map 58, 59, 60, 62
        if (level < 80) return 66 + (Math.abs(botId) % 3); // Map 66, 67, 68
        if (level < 90) return 74 + (Math.abs(botId) % 5); // Map 74, 75, 76, 77, 78
        if (level < 100) return 84 + (Math.abs(botId) % 4); // Map 84, 85, 86, 88
        return 96 + (Math.abs(botId) % 5); // Map 96, 97, 98, 99, 100
    }

    /**
     * Vô hiệu hóa việc spawn bot tĩnh đứng ở làng theo yêu cầu chuẩn hóa hệ thống AI:
     * Bot phải tự cày, tự học hành vi người chơi, không đứng tụ tập nhân tạo ở làng.
     */
    public static void initLobbyBots() {
        clearLobbyBots();
    }

    public static void clearLobbyBots() {
        for (BotLobbyBot b : LOBBY_BOTS) {
            try {
                b.leave();
            } catch (Exception ignored) {}
        }
        LOBBY_BOTS.clear();
        if (clan.Clan.ENTRY != null) {
            clan.Clan.ENTRY.removeIf(c -> c != null && c.id <= -700);
        }
    }
}
