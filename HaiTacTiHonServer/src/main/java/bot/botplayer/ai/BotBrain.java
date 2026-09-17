package bot.botplayer.ai;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import bot.botplayer.BotPlayerReal.BotMode;
import bot.botplayer.BotPlayerReal.BotType;
import bot.botplayer.BotMarket;
import bot.botplayer.GameAnalyzer;
import bot.botplayer.TrainBoss;
import bot.botplayer.TrainSkill;
import bot.botplayer.TrainEvent;
import bot.botplayer.ai.nextgen.*;
import mob.Mob;
import model.Player;
import map.Zone;
import map.MapCanGoTo;
import core.ZUtil;

/**
 * BotBrain — Bộ não Next-Gen MMO AI điều khiển BotPlayer.
 * Phối hợp Goal Manager, Decision Engine, Planner, Action Queue, Anti-Stuck Engine và LOD Scheduler.
 */
public class BotBrain {

    private final BotPlayerReal bot;
    private final BotPersonality personality;
    private final GoalManager goalManager;
    private final ActionQueue actionQueue;
    private final AntiStuckEngine antiStuckEngine;

    // Timers
    private long lastSaveCheck = 0;
    private long lastWearCheck = 0;
    private long lastChatTime = 0;
    private long lastLevelCheck = 0;
    private long lastBossCheck = 0;
    private long lastMarketCheck = 0;
    private long lastRestEnd = 0;
    private long lastUpgradeCheck = 0;
    private long lastRegenTime = 0;
    private long lastDungeonCheck = 0;
    private long lastDailyCheck = 0;
    private long lastSkillCheck = 0;
    private long lastEventCheck = 0;
    private long deathTime = 0;
    private long lastSocialCheck = 0;
    private long lastPvpCheck = 0;
    private long lastZoneJumpCheck = 0;
    private long lastGoalEvalTime = 0;
    private long lastMailCheck = 0;
    private long lastChestCheck = 0;
    private long lastQuestCheck = 0;

    public BotBrain(BotPlayerReal bot) {
        this.bot = bot;
        this.personality = new BotPersonality(bot.name);
        this.goalManager = new GoalManager();
        this.actionQueue = new ActionQueue();
        this.antiStuckEngine = new AntiStuckEngine();
    }

    public BotPersonality getPersonality() { return personality; }
    public GoalManager getGoalManager() { return goalManager; }
    public ActionQueue getActionQueue() { return actionQueue; }

    /**
     * Hàm tick chính được gọi từ update loop của BotPlayerReal.
     */
    public void tick() {
        // 1. Run delayed tasks
        runDelayedTasks();

        long now = System.currentTimeMillis();

        // 2. Anti-Stuck Check & Recovery
        antiStuckEngine.checkAndRecover(bot);

        // 3. Check PVP map & WorldWar map
        if (bot.map != null && bot.map.map_vp != null) {
            if (!"PVP".equals(bot.state)) {
                bot.state = "PVP";
            }
        }
        if (bot.map != null && map.zones.WorldWar.mapTranChienLon(bot.map.template.id)) {
            bot.state = "WORLD_WAR";
            if (map.zones.WorldWar.status == map.zones.WorldWar.STATUS_WAR) {
                Player enemy = bot.getCombatController().findNearestEnemyToPVP();
                if (enemy != null) {
                    double dist = Math.hypot(enemy.x - bot.x, enemy.y - bot.y);
                    if (dist > 70) {
                        bot.getMovementController().moveTowards(enemy.x, enemy.y);
                    } else {
                        bot.getCombatController().attackPlayer(enemy);
                    }
                } else if (ZUtil.random(100) < 25) {
                    bot.getMovementController().moveRandom();
                }
                return;
            } else {
                if (ZUtil.random(100) < 20) {
                    bot.getMovementController().moveRandom();
                }
                return;
            }
        }

        // 4. Death & Revive Handling
        if (bot.isdie) {
            if (deathTime == 0) {
                deathTime = now + 5000 + ZUtil.random(3000); // 5 - 8s revive delay
                return;
            }
            if (now < deathTime) {
                return;
            }
            deathTime = 0;
            handleDeath();
            return;
        } else {
            deathTime = 0;
        }

        // 5. Staggered Ticks & LOD AI Sleep Scheduler
        if (now < bot.nextActionTime) {
            return;
        }
        bot.nextActionTime = now + TickScheduler.calculateNextTickDelay(bot);

        // Lobby / PVP queue special modes
        if (bot.botType == BotType.LOBBY_IDLE) {
            tickLobbyIdle(now);
            return;
        }
        if (bot.botType == BotType.PVP_QUEUE) {
            tickPvpQueue();
            return;
        }

        // Loot ground items
        bot.getInventoryController().pickGroundItems();

        // HP/MP Regen
        regenStats();

        // Counter-attack only if actively attacked recently by a valid hostile player in the same map
        Player attacker = bot.lastAttacker;
        if (attacker != null && !attacker.isdie && attacker.map != null && attacker.map.equals(bot.map) && (now - bot.lastAttackedTime) < 7000) {
            if (bot.getCombatController().canPVP(attacker)) {
                double dist = Math.hypot(attacker.x - bot.x, attacker.y - bot.y);
                if (dist > 80) {
                    bot.getMovementController().moveTowards(attacker.x, attacker.y);
                } else {
                    bot.getCombatController().attackPlayer(attacker);
                }
                return;
            }
        } else if (now - bot.lastAttackedTime >= 7000) {
            bot.lastAttacker = null;
        }

        // Auto allocate potential points
        autoAllocatePotentialPoints();

        // Dynamic Goal Selection (Next-Gen AI Decision Loop)
        if (actionQueue.isFinished() || (now - lastGoalEvalTime > 15000L)) {
            lastGoalEvalTime = now;
            IGoal selectedGoal = goalManager.evaluateAndSelectGoal(bot, personality);
            if (selectedGoal != null) {
                ActionPlan plan = Planner.createPlan(bot, selectedGoal);
                if (plan != null && !plan.isEmpty()) {
                    actionQueue.setPlan(plan);
                    bot.state = selectedGoal.getName();
                    AIDebugLogger.logDecision(bot, selectedGoal.getName(), "Utility evaluation", 
                            selectedGoal.evaluateUtility(new GoalContext(bot), personality),
                            selectedGoal.getRisk(), selectedGoal.getCost(), System.currentTimeMillis() - now);
                }
            }
        }

        // Execute step in ActionQueue
        actionQueue.tick(bot);

        // Maintenance timers (save, wear, daily, skills, analyzer)
        runPeriodicMaintenance(now);
    }

    private void runPeriodicMaintenance(long now) {
        if (now - lastWearCheck > 30000) {
            lastWearCheck = now;
            bot.getEquipmentController().autoWearBetterGear();
        }
        if (now - lastUpgradeCheck > 120000) {
            lastUpgradeCheck = now;
            UpgradeAI.processUpgrades(bot);
        }
        if (now - lastMailCheck > 120_000L) {
            lastMailCheck = now;
            bot.getInventoryController().checkAndClaimMails();
        }
        if (now - lastChestCheck > 60_000L) {
            lastChestCheck = now;
            bot.getInventoryController().autoOpenBoxesAndChests();
        }
        if (now - lastQuestCheck > 30_000L) {
            lastQuestCheck = now;
            bot.getQuestController().autoNextQuest();
        }
        if (now - lastSaveCheck > 300000) {
            lastSaveCheck = now;
            bot.getPersistenceController().saveBot();
        }
        if (now - lastDailyCheck > 600_000L) {
            lastDailyCheck = now;
            bot.getDailyController().tick();
        }
        if (now - lastSkillCheck > 60_000L) {
            lastSkillCheck = now;
            TrainSkill.tick(bot);
        }
        GameAnalyzer.trackBotProgress(bot);
    }

    private void tickLobbyIdle(long now) {
        if (now - lastChatTime > (15000 + ZUtil.random(20000))) {
            lastChatTime = now;
            bot.getSocialController().tryChat();
        }
        if (ZUtil.random(100) < 30) {
            bot.getMovementController().moveRandom();
        }

        // Tự động chuyển sang cày cấp hoặc đi phó bản khi có hoạt động
        activities.TimedDungeonManager.ScheduleConfig pvpBangCfg = activities.TimedDungeonManager.gI().getConfigs().get("PVP_BANG");
        activities.TimedDungeonManager.ScheduleConfig tcklCfg = activities.TimedDungeonManager.gI().getConfigs().get("TRAN_CHIEN_KHONG_LO");
        boolean isDungeonRunning = (pvpBangCfg != null && pvpBangCfg.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING)
                || (tcklCfg != null && tcklCfg.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING)
                || (bot.clan != null && bot.clan.map_create != null);

        // Chuyển sang FULL_AI để cày cấp (TRAIN_LEVEL) hoặc đi phó bản
        if (isDungeonRunning || ZUtil.random(100) < 35) {
            bot.botType = BotType.FULL_AI;
            bot.state = isDungeonRunning ? "CLAN" : "TRAIN_LEVEL";
            bot.targetMapId = bot.getTrainingMapId();
        }
    }

    private void tickPvpQueue() {
        bot.getPvpQueueController().tick();
    }

    private void runDelayedTasks() {
        synchronized (bot.delayedTasks) {
            long now = System.currentTimeMillis();
            for (int i = bot.delayedTasks.size() - 1; i >= 0; i--) {
                BotPlayerReal.DelayedTask task = bot.delayedTasks.get(i);
                if (now >= task.executeTime) {
                    try {
                        task.action.run();
                    } catch (Exception ignored) {}
                    bot.delayedTasks.remove(i);
                }
            }
        }
    }

    private void handleDeath() {
        try {
            bot.lastAttacker = null;
            bot.targetFight = null;
            bot.pvp_target = null;
            GameAnalyzer.incrementDeaths();

            if (actionQueue != null) {
                actionQueue.clear();
            }

            int currentMapId = (bot.map != null && bot.map.template != null) ? bot.map.template.id : (bot.id_map_save > 0 ? bot.id_map_save : 1);
            boolean isBossMap = Zone.is_map_boss(currentMapId);
            boolean isDungeonMap = Zone.is_map_dungeon(currentMapId) || (bot.map != null && (bot.map.map_little_garden != null || bot.map.pvpBang != null));

            // TH1: Hồi sinh tại chỗ (Revive on spot) nếu đủ điều kiện (có Beri và 35% chance, hoặc khi đánh Boss/Phó bản)
            long currentBeri = bot.get_vang();
            boolean canReviveOnSpot = (currentBeri >= 500 && (ZUtil.random(100) < 35 || isBossMap)) || isDungeonMap;
            if (canReviveOnSpot && bot.map != null) {
                if (currentBeri >= 500 && !isDungeonMap) {
                    bot.update_vang(-500);
                }
                bot.isdie = false;
                int hpMax = bot.ability.get_hp_max(true);
                int mpMax = bot.ability.get_mp_max(true);
                bot.hp = Math.max(500, hpMax);
                bot.mp = Math.max(500, mpMax);
                bot.time_can_mob_atk = System.currentTimeMillis() + 1500L;

                try {
                    bot.map.update_hp_mp_eff(bot, null, 1, bot.hp);
                    bot.getService().use_potion(0, bot.hp);
                    if (ZUtil.random(100) < 30) {
                        String[] reviveChats = {"May quá hồi sinh kịp!", "Chiến tiếp nào ae!", "Quay lại phục thù!", "Suýt thì toang!"};
                        bot.map.send_chat_popup(0, bot.index_map, reviveChats[ZUtil.random(reviveChats.length)]);
                    }
                } catch (Exception ignored) {}

                bot.state = isBossMap ? "BOSS" : (isDungeonMap ? "DUNGEON" : "TRAIN_LEVEL");
                return;
            }

            // TH2: Quay về làng (Return to village)
            bot.leave();
            bot.isdie = false;
            int hpMax = bot.ability.get_hp_max(true);
            int mpMax = bot.ability.get_mp_max(true);
            bot.hp = Math.max(500, hpMax);
            bot.mp = Math.max(500, mpMax);
            bot.time_can_mob_atk = System.currentTimeMillis() + 1500L;

            int villageMapId = Zone.getVillageMapId(currentMapId);
            if (villageMapId <= 0) villageMapId = (bot.id_map_save > 0) ? bot.id_map_save : 1;
            bot.id_map_save = villageMapId;

            Zone[] zones = Zone.getMapByID(villageMapId);
            if (zones != null && zones.length > 0) {
                short vx = (short) (240 + ZUtil.random(80) - 40);
                short vy = (short) (200 + ZUtil.random(30) - 15);
                Zone selectedZone = BotPlayerManager.findBestZoneForBot(bot, villageMapId);
                bot.join(selectedZone != null ? selectedZone : zones[0], vx, vy);
            }

            // Sau khi về làng: Kiểm tra nếu cần mua potion/bán đồ rác
            if (bot.getInventoryController().shouldSellTrash() || bot.getInventoryController().needsPotions()) {
                bot.state = "SHOP";
                BotMarket.sellTrashAtNpc(bot);
                bot.getInventoryController().buyPotions();
                bot.getInventoryController().autoExpandInventorySlots();
                bot.getInventoryController().buyUpgradeMaterials();
            }

            // Tự động lên kế hoạch quay lại map cày cấp hoặc làm nhiệm vụ
            int targetTrainMap = bot.getTrainingMapId();
            if (targetTrainMap <= 1 || MapCanGoTo.isVillageMap(targetTrainMap)) {
                targetTrainMap = 2;
            }
            bot.targetMapId = targetTrainMap;
            bot.currentPath = Pathfinder.findPath(villageMapId, targetTrainMap);
            bot.pathIndex = 0;
            bot.state = "TRAIN_LEVEL";
            bot.ischangemap = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void regenStats() {
        try {
            long now = System.currentTimeMillis();
            if (now - lastRegenTime < 5000) return;

            int hpMax = bot.ability.get_hp_max(true);
            int mpMax = bot.ability.get_mp_max(true);

            if (bot.hp < hpMax * 3 / 10) {
                if (bot.getInventoryController().usePotion(true)) {
                    lastRegenTime = now;
                }
            }
            if (bot.mp < mpMax * 3 / 10) {
                if (bot.getInventoryController().usePotion(false)) {
                    lastRegenTime = now;
                }
            }
        } catch (Exception ignored) {}
    }

    private void autoAllocatePotentialPoints() {
        try {
            if (bot.pointAttribute > 0) {
                bot.autoAllocatePotentialPoints(bot.clazz);
                if (bot.ability != null) {
                    int hpMax = bot.ability.get_hp_max(true);
                    int mpMax = bot.ability.get_mp_max(true);
                    if (bot.hp > hpMax) bot.hp = hpMax;
                    if (bot.mp > mpMax) bot.mp = mpMax;
                }
            }
        } catch (Exception ignored) {}
    }
}
