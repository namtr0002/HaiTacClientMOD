package boss;

import zabstracts.AbsBoss;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import core.ZUtil;
import model.Player;
import core.Manager;
import database.DbManager;
import mob.Mob;
import map.Vgo;
import map.Zone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.*;

public class SuperBossManager {
    
    // [FIX EVENT-C1] ENTRYS phải được khởi tạo ngay để tránh NPE khi createSuperBoss/resultBoss/get_mob được gọi
    public static List<AbsBoss> ENTRYS = new java.util.concurrent.CopyOnWriteArrayList<>();
    public static byte[] BOSS_LIVE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static byte[] BOSS_AREA = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public static byte[] TIME_NOW = new byte[]{18, 0, 0};
    public static short[] ID_BOSS = new short[]{16, 23, 168, 135, 136, 137, 138, 139, 140, 163};

    public static List<String> top_dame_gift = new ArrayList<>();
    public static List<BossReward> top_boss_reward = new ArrayList<>();

    public static void init() {
        if (!ENTRYS.isEmpty()) return;
        for (short mobId : ID_BOSS) {
            AbsBoss b = BossManager.gI().createBoss(mobId);
            if (b != null) {
                ENTRYS.add(b);
            }
        }
    }

    public static String getBossName(int bossIndex) {
        if (bossIndex < 0 || bossIndex >= ID_BOSS.length) return "Siêu Trùm";
        short mobId = ID_BOSS[bossIndex];
        AbsBoss boss = getBossByMobId(mobId);
        if (boss != null && boss.mtemplate != null && boss.mtemplate.name != null && !boss.mtemplate.name.isBlank()) {
            return boss.mtemplate.name;
        }
        MobTemplate mt = MobTemplate.get_mob_template(mobId);
        if (mt != null && mt.name != null && !mt.name.isBlank()) {
            return mt.name;
        }
        return "Siêu Trùm " + (bossIndex + 1);
    }

    public static AbsBoss getBossByMobId(short mobId) {
        for (AbsBoss b : ENTRYS) {
            if (b != null && b.mtemplate != null && b.mtemplate.mob_id == mobId) {
                return b;
            }
        }
        return null;
    }

    public static AbsBoss getActiveSuperBossInZone(Zone zone) {
        if (zone == null) return null;
        for (AbsBoss b : ENTRYS) {
            if (b != null && !b.isdie && b.hp > 0 && zone.equals(b.map)) {
                return b;
            }
        }
        return null;
    }

    public static boolean checkLevelRequirement(Player p, AbsBoss boss) {
        if (p == null || boss == null) return true;
        if (p.admin == 1 || p.isBot || zinterfaces.menus.AdminSystemMenu.isAdmin(p)) return true;
        if (core.Manager.gI() != null && core.Manager.gI().isTestMode()) return true;
        int bossLv = boss.getLevel();
        return Math.abs(p.level - bossLv) <= 10;
    }

    public static void kickPlayerOutOfSuperBossZone(Player p, AbsBoss boss) {
        if (p == null || p.map == null || p.isdie) return;
        if (p.admin == 1 || p.isBot || zinterfaces.menus.AdminSystemMenu.isAdmin(p)) return;
        if (core.Manager.gI() != null && core.Manager.gI().isTestMode()) return;

        String bossName = (boss != null && boss.mtemplate != null) ? boss.mtemplate.name : "Siêu Trùm";
        int bossLv = (boss != null) ? boss.getLevel() : 0;

        // 1. Tìm khu vực an toàn khác trong cùng map
        Zone currentZone = p.map;
        Zone safeZone = null;
        Zone[] zones = Zone.getMapByID(currentZone.template.id);
        if (zones != null) {
            for (Zone z : zones) {
                if (z != null && !z.equals(currentZone) && z.getNumPlayerSlot() < z.template.max_player) {
                    if (getActiveSuperBossInZone(z) == null) {
                        safeZone = z;
                        break;
                    }
                }
            }
        }

        try {
            if (safeZone != null) {
                currentZone.leave_map(p, 1);
                p.map = safeZone;
                p.map.enter_map(p);
                p.map.enter_zone(p);
                p.getService().send_box_ThongBao_OK("Bạn đã bị kích ra khu " + (safeZone.zone_id + 1)
                        + " do không đạt yêu cầu cấp độ so với Siêu Trùm " + bossName + " Cấp " + bossLv + "!");
            } else {
                int returnVillage = Zone.getVillageMapId(currentZone.template.id);
                if (returnVillage <= 0) returnVillage = (p.id_map_save > 0) ? p.id_map_save : 1;
                Zone[] villageZones = Zone.getMapByID(returnVillage);
                if (villageZones != null && villageZones.length > 0 && villageZones[0] != null) {
                    currentZone.leave_map(p, 1);
                    p.map = villageZones[0];
                    p.x = 500;
                    p.y = 300;
                    p.map.enter_map(p);
                    p.map.enter_zone(p);
                    p.getService().send_box_ThongBao_OK("Bạn đã bị kích về làng do không đạt yêu cầu cấp độ so với Siêu Trùm " + bossName + " Cấp " + bossLv + "!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void kickInvalidPlayersInZone(Zone zone, AbsBoss boss) {
        if (zone == null || boss == null || zone.players == null) return;
        if (core.Manager.gI() != null && core.Manager.gI().isTestMode()) return;
        List<Player> toKick = new ArrayList<>();
        synchronized (zone.players) {
            for (Player pl : zone.players) {
                if (pl != null && !pl.isBot && pl.admin != 1 && !zinterfaces.menus.AdminSystemMenu.isAdmin(pl)) {
                    if (!checkLevelRequirement(pl, boss)) {
                        toKick.add(pl);
                    }
                }
            }
        }
        for (Player pl : toKick) {
            kickPlayerOutOfSuperBossZone(pl, boss);
        }
    }

    public static void sendSuperBossMenu(Player p) throws IOException {
        init();
        int count = ID_BOSS.length;
        String[] menu = new String[count + 2];
        short[] icons = new short[count + 2];

        for (int i = 0; i < count; i++) {
            menu[i] = getBossName(i);
            icons[i] = 136;
        }
        menu[count] = "Vị trí xuất hiện";
        icons[count] = 148;
        menu[count + 1] = "Nhận phần thưởng";
        icons[count + 1] = 110;

        p.getService().openDynamicMenu(951, "Siêu Trùm", menu, icons);
    }

    public static void handleMenu(Player p, int menuId, int index) throws IOException {
        init();

        // 1. Xem danh sách Top Sát Thương (Menu 120)
        if (menuId == 120) {
            return;
        }

        // 2. Dynamic Sub-Menu cho từng Boss riêng biệt (IDs 9510..9519)
        if (menuId >= 9510 && menuId <= 9519) {
            int bossIndex = menuId - 9510;
            if (bossIndex < 0 || bossIndex >= ID_BOSS.length) return;
            short mobId = ID_BOSS[bossIndex];
            String bossName = getBossName(bossIndex);
            AbsBoss tempBoss = getBossByMobId(mobId);

            switch (index) {
                case 0: { // Top sát thương của Boss này
                    List<Top_Dame> list_select = (tempBoss != null && tempBoss.TopDame != null) ? tempBoss.TopDame : null;
                    if (list_select != null && !list_select.isEmpty()) {
                        try {
                            list_select.sort((o1, o2) -> Long.compare(o2.dame, o1.dame));
                        } catch (Exception e) {
                            p.getService().send_box_ThongBao_OK("Chưa có thông tin sát thương");
                            return;
                        }
                        int topCount = Math.min(list_select.size(), 10);
                        String[] list_str = new String[topCount];
                        for (int i = 0; i < topCount; i++) {
                            list_str[i] = "Top " + (i + 1) + ": " + list_select.get(i).name + " : " + ZUtil.number_format(list_select.get(i).dame) + " sát thương";
                        }
                        p.getService().openDynamicMenu(120, "Top Sát Thương " + bossName, list_str, null);
                    } else {
                        String locInfo = (tempBoss != null && !tempBoss.isdie && tempBoss.map != null && tempBoss.map.template != null)
                                ? ("Đang xuất hiện tại " + tempBoss.map.template.name + " khu " + (tempBoss.map.zone_id + 1))
                                : "Chưa xuất hiện";
                        p.getService().send_box_ThongBao_OK("Trùm " + bossName + "\nTrạng thái: " + locInfo + "\nChưa có dữ liệu sát thương.");
                    }
                    break;
                }
                case 1: { // Vị trí xuất hiện của Boss này
                    if (tempBoss != null && !tempBoss.isdie && tempBoss.map != null && tempBoss.map.template != null) {
                        p.getService().send_box_ThongBao_OK("VỊ TRÍ SIÊU TRÙM " + bossName.toUpperCase() + ":\n" 
                                + tempBoss.map.template.name + " (Khu " + (tempBoss.map.zone_id + 1) + ")");
                    } else {
                        p.getService().send_box_ThongBao_OK("Siêu Trùm " + bossName + " hiện CHƯA XUẤT HIỆN.\n\nThời gian hồi sinh:\nVào phút thứ :00 và :30 hằng giờ (khi đã bị tiêu diệt).");
                    }
                    break;
                }
                case 2: { // Nhận phần thưởng
                    claimRewardBoss(p);
                    break;
                }
            }
            return;
        }

        // 3. Main Dynamic Menu (ID 951)
        if (menuId == 951) {
            int count = ID_BOSS.length; // 10
            if (index == count) { // Vị trí xuất hiện (Tất cả)
                showAllBossLocations(p);
            } else if (index == count + 1) { // Nhận phần thưởng (Tất cả)
                claimRewardBoss(p);
            } else if (index >= 0 && index < count) { // Click vào tên Boss (0..9) -> Hiện Sub-Menu
                int subMenuId = 9510 + index;
                String bossName = getBossName(index);
                String[] subOptions = new String[]{"Top sát thương", "Vị trí xuất hiện", "Nhận phần thưởng"};
                short[] subIcons = new short[]{136, 148, 110};
                p.getService().openDynamicMenu(subMenuId, "Siêu Trùm: " + bossName, subOptions, subIcons);
            }
            return;
        }

        handleMenu(p, index);
    }

    private static void showAllBossLocations(Player p) throws IOException {
        HashMap<String, String> list_boss = new HashMap<>();
        List<String> bossList = new ArrayList<>();
        for (AbsBoss temp : ENTRYS) {
            if (!temp.isdie && temp.map != null && temp.mtemplate != null) {
                String key = temp.mtemplate.name + ": " + temp.map.template.name;
                if (!list_boss.containsKey(key)) {
                    list_boss.put(key, "" + (temp.map.zone_id + 1));
                } else {
                    String old_value = list_boss.get(key);
                    list_boss.replace(key, old_value, old_value + ", " + (temp.map.zone_id + 1));
                }
                bossList.add(key + ": k" + (temp.map.zone_id + 1));
            }
        }
        StringBuilder notice = new StringBuilder();
        for (String s : bossList) {
            notice.append(s).append("\n");
        }
        if (list_boss.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Siêu Trùm tự động hồi sinh mỗi 30 phút (vào phút thứ :00 và :30 khi đã bị tiêu diệt)."
                    + "\nTop 1: 4M Beri, 5 Khiên, 1500 Ruby, 2 Búa Sơ."
                    + "\nTop 2: 3 Khiên, 2M Beri, 1000 Ruby, 1 Búa Sơ."
                    + "\nTop 3: 3 Khiên, 1M Beri, 500 Ruby, 1 Búa Sơ."
                    + "\nTop 4-10: 400 Ruby, 1M Beri, 1 Khiên.");
        } else {
            p.getService().send_box_ThongBao_OK("VỊ TRÍ SIÊU TRÙM:\n" + notice.toString());
        }
    }

    private static void claimRewardBoss(Player p) throws IOException {
        List<historys.DungeonRewardHistory.PendingReward> pending = historys.DungeonRewardHistory.getPendingRewardsByType(p.IDPlayer, "SUPER_BOSS");
        if (pending != null && !pending.isEmpty()) {
            activities.DungeonGiftMenu.openMenuByType(p, "SUPER_BOSS");
            return;
        }

        // Fallback kiểm tra quà trong bộ nhớ tạm thời
        List<GiftBox> list_gift = get_reward(p.IDPlayer, p.name);
        if (!list_gift.isEmpty()) {
            core.RewardService.sendGiftOrMail(p, 1, "Săn siêu trùm", "Phần thưởng siêu trùm", list_gift, true);
            if (p.active_so_tay() == 1) {
                p.update_exp_so_tay(ZUtil.random(100, 250));
            }
            p.getService().send_box_ThongBao_OK("Nhận phần thưởng Siêu Trùm thành công!");
        } else {
            p.getService().send_box_ThongBao_OK("Chưa có kết quả tổng kết hoặc bạn không có tên trong danh sách nhận quà Siêu Trùm.");
        }
    }

    public static void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, 951, index);
    }

    public static void createSuperBoss() {
        top_dame_gift.clear();
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        for(short id : ID_BOSS) {
            List<AbsBoss> listBossDead = new ArrayList<>();
            for(AbsBoss boss : ENTRYS) {
                boss.TopDame.clear();
                if(!boss.isdie || boss.mtemplate.mob_id != id) {
                    continue;
                }
                listBossDead.add(boss);
            }
            if(listBossDead.isEmpty()) {
                continue;
            }
            AbsBoss boss = listBossDead.get(ZUtil.random(listBossDead.size()));
            
            // Check if this boss is configured to spawn at this hour (null = 24/24)
            byte[] spawnHours = boss.getSpawnHours();
            if (spawnHours != null) {
                boolean hourMatches = false;
                for (byte h : spawnHours) {
                    if (h == hour) {
                        hourMatches = true;
                        break;
                    }
                }
                if (!hourMatches) {
                    continue;
                }
            }

            int idx = -1;
            for (int i = 0; i < ID_BOSS.length; i++) {
                if (ID_BOSS[i] == boss.mtemplate.mob_id) {
                    idx = i;
                    break;
                }
            }
            if (idx == -1 || BOSS_LIVE[idx] == 1) {
                continue;
            }

            int targetMapId = (boss.map != null && boss.map.template != null) ? boss.map.template.id : boss.getMapId();
            if (targetMapId <= 0) continue;

            // Mỗi map id chỉ được 1 siêu trùm ở khu bất kì. Nếu 1 trong all khu có siêu trùm thì không init/spawn nữa!
            if (Zone.hasActiveSuperBossInMap(targetMapId)) {
                continue;
            }

            Zone[] zones = Zone.getMapByID(targetMapId);
            if (zones == null || zones.length == 0) continue;

            // Chọn 1 khu ngẫu nhiên bất kỳ
            Zone targetZone = (zones.length > 1) ? zones[ZUtil.random(1, zones.length - 1)] : zones[0];
            if (targetZone == null || targetZone.template == null) continue;

            // Dọn dẹp ở zone cũ nếu khác targetZone
            if (boss.map != null && !boss.map.equals(targetZone)) {
                try {
                    boss.map.remove_obj(boss.index_mob_save, 1);
                    boss.map.mobs.remove(boss.index);
                    for (int j = 0; j < 10; j++) {
                        boss.map.mobs.remove(boss.index + j);
                    }
                } catch (Exception ignored) {}
            }

            boss.map = targetZone;
            short[] coords = boss.calculateCoordinatesInZone(targetZone, (short) -1, (short) -1);
            boss.x = coords[0];
            boss.y = coords[1];
            targetZone.mobs.put(boss.index, boss);
            for (int j = 0; j < 10; j++) {
                targetZone.mobs.put(boss.index + j, boss);
            }

            boss.isdie = false;
            boss.hp = boss.hp_max;
            boss.id_target = -1;
            boss.levelBoss = 1;
            boss.index = boss.index_mob_save;
            BOSS_AREA[idx] = (byte) targetZone.zone_id;
            targetZone.can_PK = false;
            targetZone.list_mob_custom_add(boss);
            boss.sendMove();
            kickInvalidPlayersInZone(targetZone, boss);
            Manager.gI().chatKTG(0, "Siêu trùm " + boss.mtemplate.name + " đã xuất hiện tại " 
                + targetZone.template.name + " khu " + (targetZone.zone_id + 1) + "! Các hải tặc hãy mau đến săn lùng!", 5);
            BOSS_LIVE[idx] = 1;
        }
    }

    public static Mob get_mob(Player p, int id) {
        for(AbsBoss boss : SuperBossManager.ENTRYS) {
            if(!boss.isdie && boss.map.template.id == p.map.template.id && boss.index == id) {
                return boss;
            }
        }
        return null;
    }

    public static void resultBoss() {
        top_dame_gift.clear();
        for(short id : ID_BOSS) {
            List<Top_Dame> list_select = null;
            for(AbsBoss boss : SuperBossManager.ENTRYS) {
                if(boss.mtemplate.mob_id == id && !boss.TopDame.isEmpty()) {
                    list_select = boss.TopDame;
                    break;
                }
            }
            if(list_select == null) {
                continue;
            }
            List<Top_Dame> result = ZUtil.sort(list_select);
            for (int i = 0; i < result.size(); i++) {
                String name_add = result.get(i).name + "_" + i;
                if (!top_dame_gift.contains(name_add)) {
                    top_dame_gift.add(name_add);
                }
            }
        }
        for(AbsBoss boss : ENTRYS) {
            boss.isdie = true;
            boss.map.remove_obj(boss.index_mob_save, 1);
        }
        Manager.gI().chatKTG(0, "Hoạt động săn siêu trùm hôm nay đã kết thúc", 5);
        BOSS_LIVE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        BOSS_AREA = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        for (int i = 0; i < SuperBossManager.top_dame_gift.size(); i++) {
            try {
                // [FIX BUG-006] Dùng lastIndexOf('_') để xử lý đúng khi player name có chứa '_'
                String entry = SuperBossManager.top_dame_gift.get(i);
                int lastUnderscore = entry.lastIndexOf('_');
                if (lastUnderscore < 0 || lastUnderscore >= entry.length() - 1) {
                    System.err.println("[SuperBossManager] Invalid entry format: " + entry);
                    continue;
                }
                BossReward bossReward = new BossReward();
                bossReward.name = entry.substring(0, lastUnderscore);
                bossReward.listGiftBox = new ArrayList<>();
                int index = Integer.parseInt(entry.substring(lastUnderscore + 1));
                switch (index) {
                    case 0: {
                        ItemTemplate7 it_temp7_in = ItemTemplate7.get_it_by_id(10);
                        if (it_temp7_in != null) {
                           bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 3));
                        }
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 3_000_000));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(1);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 300));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(29);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 1));
                        }
                        it_temp7_in = ItemTemplate7.get_it_by_id(3);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 3));
                        }
                        break;
                    }
                    case 1: {
                        ItemTemplate7 it_temp7_in = ItemTemplate7.get_it_by_id(10);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 2));
                        }
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 1_500_000));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(1);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 150));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(106);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 1));
                        }
                        it_temp7_in = ItemTemplate7.get_it_by_id(2);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 2));
                        }
                        break;
                    }
                    case 2: {
                        ItemTemplate7 it_temp7_in = ItemTemplate7.get_it_by_id(10);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 1));
                        }
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 800_000));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(1);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 80));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(106);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 1));
                        }
                        it_temp7_in = ItemTemplate7.get_it_by_id(2);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 1));
                        }
                        break;
                    }
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9: {
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 40));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 400_000));
                        }
                        it_temp4 = ItemTemplate4.get_it_by_id(19);
                        if (it_temp4 != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp4, 1));
                        }
                        ItemTemplate7 it_temp7_in = ItemTemplate7.get_it_by_id(1);
                        if (it_temp7_in != null) {
                            bossReward.listGiftBox.add(new GiftBox(it_temp7_in, 1));
                        }
                        break;
                    }
                }
                boolean isAdd = true;
                for(BossReward bossRQ : top_boss_reward) {
                    if(bossRQ.name.equals(bossReward.name)) {
                        for(GiftBox giftBox : bossReward.listGiftBox) {
                            GiftBox existingGiftBox = findExistingGiftBox(bossRQ.listGiftBox, giftBox);
                            if (existingGiftBox != null) {
                                existingGiftBox.num += giftBox.num;
                            } else {
                                bossRQ.listGiftBox.add(giftBox);
                            }
                        }
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                   top_boss_reward.add(bossReward);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static GiftBox findExistingGiftBox(List<GiftBox> listGiftBox, GiftBox giftBox) {
        for (GiftBox existingGiftBox : listGiftBox) {
            if (giftBox.id == existingGiftBox.id && giftBox.type == existingGiftBox.type) {
                return existingGiftBox;
            }
        }
        return null;
    }

    public static void update_reward() {
        historys.BossRewardHistory.updateReward(top_boss_reward);
    }

    public static void processBossCycleComplete(AbsBoss boss) {
        if (boss == null || boss.TopDame == null || boss.TopDame.isEmpty()) return;
        try {
            List<Top_Dame> sorted = ZUtil.sort(new ArrayList<>(boss.TopDame));
            int topCount = Math.min(sorted.size(), 10);
            long now = System.currentTimeMillis();
            String roundToken = ZUtil.generateMD5Token("SUPER_BOSS_" + boss.id + "_" + now);
            String bossName = (boss.mtemplate != null && boss.mtemplate.name != null) ? boss.mtemplate.name : "Siêu Trùm";

            StringBuilder topChat = new StringBuilder("Tổng kết sát thương " + bossName + ": ");
            for (int i = 0; i < topCount; i++) {
                Top_Dame td = sorted.get(i);
                if (td == null || td.name == null) continue;
                List<GiftBox> gifts = buildSuperBossTopGifts(i);
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(td.name);
                if (ids[0] > 0) {
                    historys.DungeonRewardHistory.saveReward(
                        ids[0], ids[1],
                        "SUPER_BOSS", roundToken,
                        i, td.dame, gifts
                    );
                }
                topChat.append("Top ").append(i + 1).append(": ").append(td.name).append(" (").append(ZUtil.number_format(td.dame)).append("), ");
            }
            if (topChat.length() > 2) topChat.setLength(topChat.length() - 2);
            Manager.gI().chatKTG(0, topChat.toString(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.TopDame.clear();
        }
    }

    public static List<GiftBox> buildSuperBossTopGifts(int rank) {
        List<GiftBox> gifts = new ArrayList<>();
        // Thêm Thần Trang cho Top Sát Thương chu kỳ Siêu Trùm (Top 1 tỉ lệ 50%)
        int ttChance = switch (rank) {
            case 0 -> 50; // Top 1: 50% nhận 1 món Thần Trang
            case 1 -> 30; // Top 2: 30%
            case 2 -> 20; // Top 3: 20%
            default -> (rank < 6 ? 10 : 5); // Top 4-10: 5-10%
        };
        if (ZUtil.random(100) < ttChance) {
            Item_wear tt = template.ThanTrangConfig.createRandomSuperBossDrop();
            if (tt != null && tt.template != null) {
                GiftBox gb = new GiftBox();
                gb.id = (short) tt.template.id;
                gb.type = 3;
                gb.name = tt.template.name;
                gb.icon = tt.template.icon;
                gb.num = 1;
                gb.color = tt.color;
                gb.options = new ArrayList<>(tt.option_item);
                gifts.add(gb);
            }
        }

        switch (rank) {
            case 0: { // Top 1
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(10); // Đá thạch anh
                if (it7 != null) gifts.add(new GiftBox(it7, 5));
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(0); // Beri
                if (it4 != null) gifts.add(new GiftBox(it4, 4_000_000));
                it4 = ItemTemplate4.get_it_by_id(1); // Ruby
                if (it4 != null) gifts.add(new GiftBox(it4, 1500));
                it4 = ItemTemplate4.get_it_by_id(339); // Búa sơ
                if (it4 != null) gifts.add(new GiftBox(it4, 2));
                it4 = ItemTemplate4.get_it_by_id(192); // Rương
                if (it4 != null) gifts.add(new GiftBox(it4, ZUtil.random(7, 20)));
                break;
            }
            case 1: { // Top 2
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(10);
                if (it7 != null) gifts.add(new GiftBox(it7, 3));
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(0);
                if (it4 != null) gifts.add(new GiftBox(it4, 2_000_000));
                it4 = ItemTemplate4.get_it_by_id(1);
                if (it4 != null) gifts.add(new GiftBox(it4, 1000));
                it4 = ItemTemplate4.get_it_by_id(339);
                if (it4 != null) gifts.add(new GiftBox(it4, 1));
                it4 = ItemTemplate4.get_it_by_id(192);
                if (it4 != null) gifts.add(new GiftBox(it4, ZUtil.random(2, 10)));
                break;
            }
            case 2: { // Top 3
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(10);
                if (it7 != null) gifts.add(new GiftBox(it7, 1));
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(0);
                if (it4 != null) gifts.add(new GiftBox(it4, 1_000_000));
                it4 = ItemTemplate4.get_it_by_id(1);
                if (it4 != null) gifts.add(new GiftBox(it4, 500));
                it4 = ItemTemplate4.get_it_by_id(339);
                if (it4 != null) gifts.add(new GiftBox(it4, 1));
                it4 = ItemTemplate4.get_it_by_id(192);
                if (it4 != null) gifts.add(new GiftBox(it4, ZUtil.random(2, 7)));
                break;
            }
            default: { // Top 4-10
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(1);
                if (it4 != null) gifts.add(new GiftBox(it4, 400));
                ItemTemplate4 it4_0 = ItemTemplate4.get_it_by_id(0);
                if (it4_0 != null) gifts.add(new GiftBox(it4_0, 100_000));
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(10);
                if (it7 != null) gifts.add(new GiftBox(it7, 1));
                it4 = ItemTemplate4.get_it_by_id(192);
                if (it4 != null) gifts.add(new GiftBox(it4, ZUtil.random(2, 5)));
                break;
            }
        }
        return gifts;
    }

    public static List<GiftBox> get_reward(int playerId, String name) {
        List<GiftBox> result = new ArrayList<>();
        for (int i = 0; i < top_boss_reward.size(); i++) {
             BossReward br = top_boss_reward.get(i);
             if (br != null) {
                 if ((playerId > 0 && br.playerId == playerId) || (name != null && br.name.equalsIgnoreCase(name))) {
                     result.addAll(br.listGiftBox);
                     top_boss_reward.remove(i);
                     historys.BossRewardHistory.updateReward(top_boss_reward);
                     break;
                 }
             }
        }
        return result;
    }

    public static List<GiftBox> get_reward(String name) {
        return get_reward(-1, name);
    }
}
