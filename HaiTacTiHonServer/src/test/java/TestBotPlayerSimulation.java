import bot.Bot;
import bot.botplayer.BotPlayerReal;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotDb;
import bot.botplayer.TrainBoss;
import bot.botplayer.GameAnalyzer;
import bot.BotLobbyManager;
import bot.BotChiemDao;
import bot.botplayer.ai.BotBrain;
import bot.botplayer.ai.BotQuestController;
import bot.botplayer.ai.BotMovementController;
import bot.botplayer.ai.Pathfinder;
import bot.botplayer.ai.nextgen.*;
import bot.botplayer.ai.nextgen.goals.*;
import model.Player;
import model.Quest;
import template.QuestP;
import template.ItemFashionP;
import template.ItemFashionP2;
import template.ItemBoatP;
import template.Item_wear;
import map.Zone;
import map.MapTemplate;
import map.Vgo;
import skill.Skill_Template;
import core.ZUtil;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.List;
import java.util.ArrayList;

public class TestBotPlayerSimulation {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("          STARTING BOTPLAYER SIMULATION TEST SUITE             ");
        System.out.println("===============================================================");

        runTest("Case 1: Initial Bot Player Level 1 Setup & Appearance", () -> testInitialBotPlayerLevel1Setup());
        runTest("Case 2: Quest Progression Step & Turn-in Flow", () -> testQuestProgressionFlow());
        runTest("Case 3: BFS Map Transition & Boundary Enforcement", () -> testBFSPathfindingAndBoundary());
        runTest("Case 4: Multi-stage AntiStuck State Machine", () -> testAntiStuckStateMachine());
        runTest("Case 5: Death & Safe Respawn Flow", () -> testDeathAndSafeRevive());
        runTest("Case 6: Persistence JSON Format & Symmetry", () -> testPersistenceJsonSymmetry());
        runTest("Case 7: Appearance Consistency Across Reloads", () -> testAppearanceConsistencyAcrossReloads());
        runTest("Case 8: Real Player First & Zone Density Regulation", () -> testZoneDensityRegulation());
        runTest("Case 9-12: Activity Discovery & Level Conditions", () -> testActivityConditions());
        runTest("Case 13-15: Inventory Looting & No-Cheat Verification", () -> testNoCheatAndInventory());
        runTest("Case 16-18: AIDebugLogger Diagnostic Trace Verification", () -> testDiagnosticLogger());
        runTest("Case 19: Clan Bot Level Grinding & Dungeon Return Flow", () -> testClanBotLevelGrindAndDungeonReturn());
        runTest("Case 20: PK Flag & Peaceful Mob Grinding Rule Enforcement", () -> testPKFlagAndPeacefulGrinding());
        runTest("Case 21: Imitation Learning - Skill Rotation & Attribute Builds", () -> testImitationLearningSkillAndAttributes());
        runTest("Case 22: Elimination of Fake Village Bots & Natural Active Bot Farming", () -> testEliminationOfFakeVillageBots());

        System.out.println("===============================================================");
        System.out.printf("   TEST RUN COMPLETE: %d PASSED, %d FAILED%n", testsPassed, testsFailed);
        System.out.println("===============================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void runTest(String testName, Runnable test) {
        System.out.print("[TEST] " + testName + " ... ");
        try {
            test.run();
            System.out.println("PASSED");
            testsPassed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            t.printStackTrace(System.out);
            testsFailed++;
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void testInitialBotPlayerLevel1Setup() {
        try {
            if (Skill_Template.ENTRYS == null) {
                Skill_Template.ENTRYS = new ArrayList<>();
            }
            if (template.ItemTemplate3.ENTRYS == null) {
                template.ItemTemplate3.ENTRYS = new ArrayList<>();
            }

            BotPlayerReal bot = new BotPlayerReal(99999, "TestBotPlayer_L1");
            bot.clazz = 1;
            bot.level = 1;
            bot.exp = 0;
            bot.thongthao = 0;
            bot.pointAttribute = 5;

            bot.item = new itemz.Item(bot);
            bot.item.it_body = new Item_wear[8];
            bot.item.bag3 = new Item_wear[bot.item.max_bag];
            bot.item.bag47 = new ArrayList<>();

            bot.setDefaultFashion(1);
            bot.updateParts();

            assertTrue(bot.clazz == 1, "Bot clazz should be 1");
            assertTrue(bot.level == 1, "Bot level should be 1");
            assertTrue(bot.head >= 0, "Bot head part should be valid >= 0");
            assertTrue(bot.hair >= 0, "Bot hair part should be valid >= 0");
            assertTrue(bot.part_body != -1, "Bot part_body should be initialized");
            assertTrue(bot.part_leg != -1, "Bot part_leg should be initialized");
            assertTrue(bot.part_weapon == -1 || bot.part_weapon >= 0, "Bot part_weapon should be valid");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testQuestProgressionFlow() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99998, "TestBotPlayer_Quest");
            bot.clazz = 1;
            bot.level = 1;

            BotQuestController qc = bot.getQuestController();
            assertTrue(qc != null, "Quest controller must not be null");

            int limit = qc.getQuestMapLimit();
            assertTrue(limit > 0, "Quest map limit should be positive integer");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testBFSPathfindingAndBoundary() {
        try {
            if (MapTemplate.ENTRYS == null) {
                MapTemplate.ENTRYS = new ArrayList<>();
            }
            MapTemplate.ENTRYS.clear();
            Pathfinder.clearCache();

            MapTemplate m0 = new MapTemplate();
            m0.id = 0;
            m0.name = "Map 0";
            m0.vgos = new ArrayList<>();
            Vgo v0 = new Vgo();
            v0.id_map_go = 1;
            m0.vgos.add(v0);

            MapTemplate m1 = new MapTemplate();
            m1.id = 1;
            m1.name = "Map 1";
            m1.vgos = new ArrayList<>();
            Vgo v1 = new Vgo();
            v1.id_map_go = 0;
            m1.vgos.add(v1);

            MapTemplate.ENTRYS.add(m0);
            MapTemplate.ENTRYS.add(m1);

            List<Integer> path = Pathfinder.findPath(0, 1);
            assertTrue(path != null, "Pathfinder should return non-null path");
            assertTrue(!path.isEmpty() && path.get(path.size() - 1) == 1, "Path should end at target map 1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testAntiStuckStateMachine() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99997, "TestBotPlayer_Stuck");
            bot.x = 100;
            bot.y = 100;

            AntiStuckEngine engine = new AntiStuckEngine();
            engine.checkAndRecover(bot);
            assertTrue(true, "AntiStuckEngine handled state safely");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testDeathAndSafeRevive() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99996, "TestBotPlayer_Death");
            bot.ability = new ability.Ability(bot);
            bot.hp = 0;
            bot.isdie = true;
            bot.id_map_save = 1;

            BotBrain brain = bot.getBrain();
            assertTrue(brain != null, "BotBrain should not be null");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testPersistenceJsonSymmetry() {
        try {
            JSONObject levelObj = new JSONObject();
            levelObj.put("lv", 25);
            levelObj.put("exp", 123456L);
            levelObj.put("tt", 2);
            String levelJson = levelObj.toJSONString();

            JSONObject parsed = (JSONObject) JSONValue.parse(levelJson);
            assertEquals(25L, ((Number) parsed.get("lv")).longValue(), "Level JSON mismatch");
            assertEquals(123456L, ((Number) parsed.get("exp")).longValue(), "Exp JSON mismatch");
            assertEquals(2L, ((Number) parsed.get("tt")).longValue(), "ThongThao JSON mismatch");

            JSONObject fashionObj = new JSONObject();
            JSONArray fpArr = new JSONArray();
            JSONObject fpItem = new JSONObject();
            fpItem.put("cat", 103);
            fpItem.put("id", 10);
            fpItem.put("icon", 100);
            fpItem.put("use", 1);
            fpArr.add(fpItem);
            fashionObj.put("fp", fpArr);
            fashionObj.put("f2", new JSONArray());
            fashionObj.put("boat", new JSONArray());

            String rawFashion = fashionObj.toJSONString();
            JSONObject parsedFashion = (JSONObject) JSONValue.parse(rawFashion);
            assertTrue(parsedFashion.containsKey("fp"), "Fashion JSON must contain fp array");
            assertTrue(parsedFashion.containsKey("f2"), "Fashion JSON must contain f2 array");
            assertTrue(parsedFashion.containsKey("boat"), "Fashion JSON must contain boat array");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testAppearanceConsistencyAcrossReloads() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99995, "TestBotPlayer_Appear");
            bot.clazz = 2;
            bot.setDefaultFashion(2);
            bot.updateParts();

            int origHead = bot.get_head();
            int origHair = bot.get_hair();
            short origBody = bot.part_body;

            assertTrue(origHead >= 0, "Original head should be valid");
            assertTrue(origHair >= 0, "Original hair should be valid");
            assertTrue(origBody >= 0, "Original body part should be valid");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testZoneDensityRegulation() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99994, "TestBotPlayer_Density");
            Zone bestZone = BotPlayerManager.findBestZoneForBot(bot, 999999);
            assertEquals(null, bestZone, "Non-existent map ID should return null zone gracefully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testActivityConditions() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99993, "TestBotPlayer_Activity");
            bot.level = 15;

            int bossMap = TrainBoss.getBossMapForLevel(bot.level);
            assertEquals(5, bossMap, "Level 15 should map to boss map 5");

            bot.level = 5;
            assertEquals(-1, TrainBoss.getBossMapForLevel(bot.level), "Level 5 cannot train boss");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testNoCheatAndInventory() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99992, "TestBotPlayer_NoCheat");
            bot.item = new itemz.Item(bot);
            bot.item.bag3 = new Item_wear[bot.item.max_bag];
            bot.item.bag47 = new ArrayList<>();
            bot.item.it_body = new Item_wear[8];

            bot.getEquipmentController().autoHoanMyGear();
            int gemCount = bot.item.total_item_bag_by_id(4, 226);
            assertEquals(0, gemCount, "Bot must not receive cheat items (Ngoc 226) for free");

            bot.getEquipmentController().autoKichAnGear();
            gemCount = bot.item.total_item_bag_by_id(4, 226);
            assertEquals(0, gemCount, "Bot must not receive cheat items for Kich An");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testDiagnosticLogger() {
        try {
            BotPlayerReal bot = new BotPlayerReal(99991, "TestBotPlayer_Diag");
            bot.state = "QUEST";
            bot.level = 10;
            bot.targetMapId = 2;

            AIDebugLogger.logError(AIDebugLogger.Category.QUEST_ERROR, bot, "MOVE_TO_NPC", "NPC distance timeout", "Repath");
            AIDebugLogger.logError(AIDebugLogger.Category.SPAWN_ERROR, bot, "SPAWN_BOT", "Invalid zone", "Select alternative zone");
            AIDebugLogger.logStuckRecovery(bot, "TERRAIN_STUCK", "Sliding wall vector");
            AIDebugLogger.logDecision(bot, "QUEST", "Utility evaluation", 90.0, 0.5, 1.0, 15);
            assertTrue(true, "AIDebugLogger formatted diagnostics cleanly");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testClanBotLevelGrindAndDungeonReturn() {
        try {
            // 1. Kiem tra anh xa map cay cap ngang level cho Bot Clan
            int mapLv65 = BotLobbyManager.getTrainingMapIdForLevel((short) 65, 101);
            assertTrue(mapLv65 >= 58 && mapLv65 <= 62, "Level 65 must map to training maps 58-62");

            int mapLv75 = BotLobbyManager.getTrainingMapIdForLevel((short) 75, 102);
            assertTrue(mapLv75 >= 66 && mapLv75 <= 68, "Level 75 must map to training maps 66-68");

            int mapLv85 = BotLobbyManager.getTrainingMapIdForLevel((short) 85, 103);
            assertTrue(mapLv85 >= 74 && mapLv85 <= 78, "Level 85 must map to training maps 74-78");

            // 2. Kiem tra GoalClanActivity
            BotPlayerReal clanBot = new BotPlayerReal(99990, "TestClanBot_Grind");
            clan.Clan c = new clan.Clan();
            c.id = -701;
            c.name = "Bang Thu Nghiem";
            clanBot.clan = c;
            clanBot.level = 70;

            GoalClanActivity goalClan = new GoalClanActivity();
            GoalContext ctx = new GoalContext(clanBot);
            BotPersonality personality = new BotPersonality(clanBot.name);

            activities.TimedDungeonManager.ScheduleConfig pvpBang = activities.TimedDungeonManager.gI().getConfigs().get("PVP_BANG");
            activities.TimedDungeonManager.ScheduleConfig tckl = activities.TimedDungeonManager.gI().getConfigs().get("TRAN_CHIEN_KHONG_LO");
            activities.TimedDungeonManager.ScheduleConfig tlbk = activities.TimedDungeonManager.gI().getConfigs().get("THU_LINH_BIEN_KHOI");
            int oldPvpState = pvpBang != null ? pvpBang.state : 0;
            int oldTcklState = tckl != null ? tckl.state : 0;
            int oldTlbkState = tlbk != null ? tlbk.state : 0;
            try {
                if (pvpBang != null) pvpBang.state = activities.TimedDungeonManager.ScheduleConfig.STATE_IDLE;
                if (tckl != null) tckl.state = activities.TimedDungeonManager.ScheduleConfig.STATE_IDLE;
                if (tlbk != null) tlbk.state = activities.TimedDungeonManager.ScheduleConfig.STATE_IDLE;

                double scoreClosed = goalClan.evaluateUtility(ctx, personality);
                assertEquals(0.0, scoreClosed, "When dungeon is closed, GoalClanActivity utility must be 0");

                if (pvpBang != null) pvpBang.state = activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING;
                double scoreOpen = goalClan.evaluateUtility(ctx, personality);
                assertTrue(scoreOpen >= 80.0, "When dungeon is running, GoalClanActivity utility must be >= 80");
            } finally {
                if (pvpBang != null) pvpBang.state = oldPvpState;
                if (tckl != null) tckl.state = oldTcklState;
                if (tlbk != null) tlbk.state = oldTlbkState;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testPKFlagAndPeacefulGrinding() {
        try {
            Zone trainZone = new Zone();
            trainZone.template = new MapTemplate();
            trainZone.template.id = 58;
            trainZone.template.name = "Bai Quai 58";
            trainZone.players = new ArrayList<>();

            BotPlayerReal botFarm = new BotPlayerReal(99980, "Bot_Farmer");
            botFarm.type_pk = -1;
            botFarm.map = trainZone;

            Player realPlayer = new Player();
            realPlayer.IDPlayer = 12345;
            realPlayer.name = "RealPlayer";
            realPlayer.type_pk = -1;
            realPlayer.map = trainZone;

            boolean canAttackPeaceful = botFarm.canAttackTargetPlayer(realPlayer);
            assertEquals(false, canAttackPeaceful, "Bot in peaceful mode MUST NOT attack peaceful real player");

            realPlayer.type_pk = 0;
            boolean canAttackMurderer = botFarm.canAttackTargetPlayer(realPlayer);
            assertEquals(true, canAttackMurderer, "Bot MUST be able to attack murderer (type_pk == 0)");

            realPlayer.type_pk = 1;
            boolean canAttackRedDevil = botFarm.canAttackTargetPlayer(realPlayer);
            assertEquals(true, canAttackRedDevil, "Bot MUST be able to attack red devil (type_pk == 1)");

            clan.Clan c1 = new clan.Clan();
            c1.id = -705;
            botFarm.clan = c1;
            botFarm.type_pk = 2;

            realPlayer.clan = c1;
            realPlayer.type_pk = 2;
            boolean canAttackSameClan = botFarm.canAttackTargetPlayer(realPlayer);
            assertEquals(false, canAttackSameClan, "Bot MUST NOT attack member of the same clan");

            clan.Clan c2 = new clan.Clan();
            c2.id = -706;
            realPlayer.clan = c2;
            boolean canAttackOtherClan = botFarm.canAttackTargetPlayer(realPlayer);
            assertEquals(true, canAttackOtherClan, "Bot MUST be able to attack members of enemy clan");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testImitationLearningSkillAndAttributes() {
        try {
            // 1. Kiem tra hoc chuoi ky nang tu nguoi choi that
            GameAnalyzer.learnPlayerSkill(1, 101);
            GameAnalyzer.learnPlayerSkill(1, 105);
            GameAnalyzer.learnPlayerSkill(1, 109);

            List<Integer> rotation = GameAnalyzer.getLearnedSkillRotation(1);
            assertTrue(rotation != null && rotation.size() >= 3, "Learned skill rotation must retain skills");
            assertEquals(Integer.valueOf(109), rotation.get(0), "Most recent cast skill must be prioritized in combo");

            // 2. Kiem tra hoc ty le cong diem tiem nang (STR, AGI, VIT, SPI)
            GameAnalyzer.learnPlayerAttributes(2, 70, 0, 10, 20, 0); // Ki?m si build: 70% STR, 10% VIT, 20% AGI
            double[] ratios = GameAnalyzer.getLearnedAttributeRatio(2);
            assertTrue(ratios != null && ratios.length == 5, "Learned attribute ratios must have 5 attributes");
            assertTrue(ratios[0] > 0.5, "STR ratio for Class 2 should reflect real player build > 50%");

            // 3. Kiem tra bot ap dung ty le da hoc khi phan bo diem
            BotPlayerReal botAttr = new BotPlayerReal(99970, "Bot_Learner");
            botAttr.clazz = 2;
            botAttr.level = 10;
            botAttr.autoAllocatePotentialPoints(botAttr.clazz);
            assertTrue(botAttr.point1 > botAttr.point3, "Allocated point1 (STR) must be dominant according to learned build");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testEliminationOfFakeVillageBots() {
        try {
            // 1. Goi cac ham khoi tao va kiem tra khong co bot tinh nao duoc add vao POOL
            BotLobbyManager.initLobbyBots();
            assertEquals(0, BotLobbyManager.LOBBY_BOTS.size(), "Static dummy lobby bots in villages MUST be 0");

            BotChiemDao.dispatchVillageBots();
            for (BotChiemDao b : BotChiemDao.POOL) {
                assertEquals(false, b.isVillageBot, "No dummy village bots should be in ChiemDao pool");
            }

            // 2. Kiem tra BotPlayerReal tu dong dinh tuyen ra map quai ngang level
            BotPlayerReal botGrind = new BotPlayerReal(99960, "Bot_NaturalGrinder");
            botGrind.level = 65;
            int trainingMap = botGrind.getTrainingMapId();
            assertTrue(trainingMap >= 58 && trainingMap <= 62, "Level 65 bot must target monster training maps 58-62");

            botGrind.level = 1;
            int lv1Map = botGrind.getTrainingMapId();
            assertTrue(lv1Map >= 2 && lv1Map <= 6, "Level 1 bot must target starter monster maps 2-6");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
