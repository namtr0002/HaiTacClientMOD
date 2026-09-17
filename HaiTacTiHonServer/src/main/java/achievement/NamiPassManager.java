package achievement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import model.Player;
import model.YesNoDialog;
import database.DbManager;
import network.Message;
import network.Service;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemBag47;
import historys.HistoryManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import core.ZUtil;

public class NamiPassManager {

    public static final String CURRENT_SEASON = "MUA_1";
    public static final String[] SEASONS = new String[]{"MUA_1"};

    public static final int MAX_MAIN_LEVEL = 100; // 100 Mốc chính chuẩn Free Fire
    public static final long EXP_PER_LEVEL = 1500L; // EXP cố định 1.500 / 1 Cấp


    public static class NamiPassData {
        public String seasonKey = CURRENT_SEASON;
        public int level = 1; // Cấp độ hiện tại (1 -> 100 và tiếp tục vượt cấp không giới hạn)
        public long exp = 0;
        public boolean isVip = false; // Thẻ Vô Cực VIP
        public boolean isElitePlus = false; // Gói VIP Thượng Hạng Elite Plus (+25 Cấp)
        public int spinCount = 0; // Lượt quay Vòng Quay Nami Special
        public int spinsEarned = 0; // Tổng số lượt quay đã kiếm được
        public Set<Integer> claimedFree = new HashSet<>(); // Mốc thường 1-100 đã nhận
        public Set<Integer> claimedVip = new HashSet<>(); // Mốc VIP 1-100 đã nhận
        public int claimedOverLevels = 0; // Tổng số mốc vượt cấp (>100) đã mở Rương Vô Cực
        public ArchiPrivatePass[] tasks = ArchiPrivatePass.createDefaultPass();
        public String lastResetDay = ""; // Ngày reset nhiệm vụ gần nhất (YYYY-MM-DD)
    }

    public static String getHistoryKey(String seasonKey) {
        String s = (seasonKey != null && !seasonKey.trim().isEmpty()) ? seasonKey.trim() : CURRENT_SEASON;
        return HistoryManager.formatKey("NAMI_PASS", s);
    }

    /**
     * EXP cần cho 1 cấp độ
     */
    public static long getExpRequiredForLevel(int level) {
        return EXP_PER_LEVEL;
    }

    public static NamiPassData getPassData(Player p) {
        return getPassData(p, p.currentPassSeason != null ? p.currentPassSeason : CURRENT_SEASON);
    }

    public static NamiPassData getPassData(Player p, String seasonKey) {
        String key = getHistoryKey(seasonKey);
        String jsonStr = HistoryManager.loadData(p, key);
        NamiPassData data = new NamiPassData();
        data.seasonKey = seasonKey;

        if (jsonStr != null && !jsonStr.trim().isEmpty() && !jsonStr.trim().equals("{}")) {
            try {
                JSONObject obj = (JSONObject) JSONValue.parse(jsonStr);
                if (obj != null) {
                    data.level = Integer.parseInt(obj.getOrDefault("lv", 1).toString());
                    data.exp = Long.parseLong(obj.getOrDefault("exp", 0L).toString());
                    data.isVip = Integer.parseInt(obj.getOrDefault("vip", 0).toString()) >= 1;
                    data.isElitePlus = Integer.parseInt(obj.getOrDefault("elite", 0).toString()) == 1;
                    data.spinCount = Integer.parseInt(obj.getOrDefault("spin", 0).toString());
                    data.spinsEarned = Integer.parseInt(obj.getOrDefault("earned", 0).toString());
                    data.claimedOverLevels = Integer.parseInt(obj.getOrDefault("c_over", 0).toString());
                    data.lastResetDay = obj.getOrDefault("last_reset", "").toString();

                    JSONArray cf = (JSONArray) obj.get("c_free");
                    if (cf != null) {
                        deserializeRanges(cf, data.claimedFree);
                    }

                    JSONArray cv = (JSONArray) obj.get("c_vip");
                    if (cv != null) {
                        deserializeRanges(cv, data.claimedVip);
                    }

                    String tasksJson = obj.containsKey("tasks") ? obj.get("tasks").toString() : "[]";
                    ArchiPrivatePass[] parsedTasks = ArchiPrivatePass.parseFromJson(tasksJson);
                    if (parsedTasks != null && parsedTasks.length > 0) {
                        data.tasks = parsedTasks;
                    }
                }
            } catch (Exception e) {
                System.err.println("[NamiPassManager] Error parsing NamiPassData: " + e.getMessage());
            }
        }
        return data;
    }

    public static JSONArray serializeRanges(Set<Integer> set) {
        JSONArray arr = new JSONArray();
        if (set == null || set.isEmpty()) return arr;
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list);
        int start = list.get(0);
        int prev = start;
        for (int i = 1; i < list.size(); i++) {
            int cur = list.get(i);
            if (cur == prev + 1) {
                prev = cur;
            } else {
                if (start == prev) {
                    arr.add(start);
                } else {
                    arr.add(start + "-" + prev);
                }
                start = cur;
                prev = cur;
            }
        }
        if (start == prev) {
            arr.add(start);
        } else {
            arr.add(start + "-" + prev);
        }
        return arr;
    }

    public static void deserializeRanges(JSONArray arr, Set<Integer> targetSet) {
        if (arr == null) return;
        for (Object v : arr) {
            if (v == null) continue;
            String s = v.toString().trim();
            if (s.contains("-")) {
                String[] parts = s.split("-");
                if (parts.length == 2) {
                    try {
                        int start = Integer.parseInt(parts[0].trim());
                        int end = Integer.parseInt(parts[1].trim());
                        for (int i = start; i <= end; i++) {
                            targetSet.add(i);
                        }
                    } catch (Exception ignored) {}
                }
            } else {
                try {
                    targetSet.add(Integer.parseInt(s));
                } catch (Exception ignored) {}
            }
        }
    }

    public static void savePassData(Player p) {
        savePassData(p, p.currentPassSeason != null ? p.currentPassSeason : CURRENT_SEASON, getPassData(p));
    }

    public static void savePassData(Player p, String seasonKey, NamiPassData data) {
        savePassData(null, p, seasonKey, data);
    }

    public static void savePassData(Connection conn, Player p, String seasonKey, NamiPassData data) {
        if (data == null) return;
        try {
            JSONObject obj = new JSONObject();
            obj.put("lv", data.level);
            obj.put("exp", data.exp);
            obj.put("vip", data.isVip ? 1 : 0);
            obj.put("elite", data.isElitePlus ? 1 : 0);
            obj.put("spin", data.spinCount);
            obj.put("earned", data.spinsEarned);
            obj.put("c_over", data.claimedOverLevels);
            obj.put("last_reset", data.lastResetDay != null ? data.lastResetDay : "");

            obj.put("c_free", serializeRanges(data.claimedFree));
            obj.put("c_vip", serializeRanges(data.claimedVip));

            if (data.tasks != null) {
                obj.put("tasks", ArchiPrivatePass.serializeToJson(data.tasks));
            }

            String key = getHistoryKey(seasonKey);
            if (conn != null) {
                HistoryManager.saveData(conn, p, key, obj.toJSONString());
            } else {
                HistoryManager.saveData(p, key, obj.toJSONString());
            }
        } catch (Exception e) {
            System.err.println("[NamiPassManager] Error saving NamiPassData: " + e.getMessage());
        }
    }

    public static void addPassExp(Player p, long expAmount) {
        if (expAmount <= 0) return;
        NamiPassData data = getPassData(p);
        data.exp += expAmount;

        boolean leveledUp = false;
        long reqExp = getExpRequiredForLevel(data.level);
        while (data.exp >= reqExp) {
            data.exp -= reqExp;
            data.level++;
            leveledUp = true;

            // Nhận lượt quay Nami Special mỗi 5 cấp
            if (data.level % 5 == 0) {
                data.spinCount++;
                data.spinsEarned++;
                p.getService().send_box_ThongBao_OK("Chúc mừng! Đạt Cấp " + data.level + " Nami Pass, nhận thưởng +1 Lượt Quay Nami Special!");
            }

            reqExp = getExpRequiredForLevel(data.level);
        }

        savePassData(p, p.currentPassSeason, data);
        if (leveledUp) {
            if (data.level <= MAX_MAIN_LEVEL) {
                p.getService().send_box_ThongBao_OK("Chúc mừng! Nami Pass của bạn đã lên Cấp " + data.level + " / 100!");
            } else {
                int over = data.level - MAX_MAIN_LEVEL;
                p.getService().send_box_ThongBao_OK("Chúc mừng! Nami Pass đã Vượt Cấp +" + over + "! (Cấp hiện tại: " + data.level + ")\nBạn nhận thêm 1 Rương Vô Cực ngẫu nhiên để mở!");
            }
        }
    }

    public static void addPassLevel(Player p, int count) {
        if (count <= 0) return;
        NamiPassData data = getPassData(p);
        
        int oldLevel = data.level;
        data.level += count;

        for (int lv = oldLevel + 1; lv <= data.level; lv++) {
            if (lv % 5 == 0) {
                data.spinCount++;
                data.spinsEarned++;
            }
        }

        savePassData(p, p.currentPassSeason, data);
    }

    public static void unlockVipPass(Player p) {
        NamiPassData data = getPassData(p);
        data.isVip = true;
        savePassData(p, p.currentPassSeason, data);
    }

    /**
     * Thêm quà mốc cố định từ Cấp 1 -> 100
     */
    public static final int TOTAL_FREE_MILESTONES = 21; // Mốc 1 + các mốc 5, 10, 15, ... 100 (21 mốc)
    public static final int TOTAL_VIP_MILESTONES = 55;  // Các mốc lẻ (50 mốc) + các mốc 10, 20, ... 100 (55 mốc)

    public static boolean hasFreeReward(int level) {
        if (level < 1 || level > MAX_MAIN_LEVEL) return false;
        return level == 1 || (level % 2 == 0) || (level % 5 == 0);
    }

    public static boolean hasVipReward(int level) {
        if (level < 1 || level > MAX_MAIN_LEVEL) return false;
        return true; // 100% các mốc 1 -> 100 đều có quà VIP chuẩn Booyah Pass
    }

    /**
     * Thêm quà mốc cố định từ Cấp 1 -> 100
     */
    public static void addMilestoneRewards(List<GiftBox> rewards, int level, boolean isVip, NamiPassData data) {
        if (!isVip) {
            // Nhánh Free Pass
            if (level == 1) {
                GiftBox.addGift(rewards, 4, 0, 100_000);   // 100k Beri
                GiftBox.addGift(rewards, 4, 1, 10);        // 10 Ruby
                GiftBox.addGift(rewards, 7, 0, 5);         // 5 Đá cường hóa 1
                GiftBox.addGift(rewards, 4, 35, 3);        // 3 Vé Đấu Trường
            } else if (level == 50) {
                // Siêu Mốc Nửa Mùa Free
                GiftBox.addGift(rewards, 4, 0, 2_000_000); // 2M Beri
                GiftBox.addGift(rewards, 4, 1, 100);       // 100 Ruby
                GiftBox.addGift(rewards, 4, 106, 1);       // 1 Rương vàng
                GiftBox.addGift(rewards, 4, 29, 1);        // 1 Rương ác quỷ
                data.spinCount += 3;
                data.spinsEarned += 3;
            } else if (level == 100) {
                // Đại Mốc Hoàn Thành Thẻ Vô Cực Free
                GiftBox.addGift(rewards, 4, 0, 5_000_000); // 5M Beri
                GiftBox.addGift(rewards, 4, 1, 300);       // 300 Ruby
                GiftBox.addGift(rewards, 4, 106, 2);       // 2 Rương vàng
                GiftBox.addGift(rewards, 4, 29, 2);        // 2 Rương ác quỷ
                data.spinCount += 10;
                data.spinsEarned += 10;
            } else if (level % 10 == 0) {
                // Mốc chẵn x10 (10, 20, 30, 40, 60, 70, 80, 90)
                long beri = 500_000L + (long) level * 20_000L;
                int ruby = 20 + level;
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);
                if (level >= 60) {
                    GiftBox.addGift(rewards, 4, 29, 1); // 1 Rương ác quỷ
                } else {
                    GiftBox.addGift(rewards, 4, 18, 1); // 1 Rương huyền bí
                }
                int spins = (level >= 60) ? 2 : 1;
                data.spinCount += spins;
                data.spinsEarned += spins;
            } else if (level % 5 == 0) {
                // Mốc x5 (5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
                long beri = 200_000L + (long) level * 10_000L;
                int ruby = 10 + (level / 2);
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);

                if (level <= 35) {
                    GiftBox.addGift(rewards, 4, 221, 3); // 3 Đá Hải Thạch cấp 1
                    GiftBox.addGift(rewards, 7, 1, 5);   // 5 Đá cường hóa 2
                    data.spinCount += 1;
                    data.spinsEarned += 1;
                } else if (level <= 65) {
                    GiftBox.addGift(rewards, 4, 222, 3); // 3 Đá Hải Thạch cấp 2
                    GiftBox.addGift(rewards, 7, 3, 5);   // 5 Đá cường hóa 3
                    data.spinCount += 1;
                    data.spinsEarned += 1;
                } else {
                    GiftBox.addGift(rewards, 4, 223, 3); // 3 Đá Hải Thạch cấp 3
                    GiftBox.addGift(rewards, 7, 5, 5);   // 5 Đá cường hóa 5
                    data.spinCount += 2;
                    data.spinsEarned += 2;
                }
            } else {
                // Mốc chẵn thường (2, 4, 6, 8, 12, 14, 16...)
                long beri = 50_000L + (long) level * 5_000L;
                int ruby = 5;
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);
                if (level <= 40) {
                    GiftBox.addGift(rewards, 7, 1, 2); // Đá cường hóa 2
                } else if (level <= 80) {
                    GiftBox.addGift(rewards, 7, 3, 2); // Đá cường hóa 3
                } else {
                    GiftBox.addGift(rewards, 7, 4, 2); // Đá cường hóa 4
                }
            }
        } else {
            // Nhánh VIP Pass (100% các mốc từ 1 -> 100)
            if (level == 1) {
                // VIP Chào Mừng
                GiftBox.addGift(rewards, 4, 0, 500_000);  // 500k Beri
                GiftBox.addGift(rewards, 4, 1, 50);       // 50 Ruby
                GiftBox.addGift(rewards, 7, 4, 5);        // 5 Đá cường hóa 4
                data.spinCount += 3;
                data.spinsEarned += 3;
            } else if (level == 50) {
                // Siêu Mốc VIP Giữa Mùa
                GiftBox.addGift(rewards, 4, 0, 5_000_000); // 5M Beri
                GiftBox.addGift(rewards, 4, 1, 300);       // 300 Ruby
                GiftBox.addGift(rewards, 4, 106, 2);       // 2 Rương vàng
                GiftBox.addGift(rewards, 4, 29, 2);        // 2 Rương ác quỷ
                data.spinCount += 6;
                data.spinsEarned += 6;
            } else if (level == 100) {
                // Đại Mốc Thần Thoại VIP
                GiftBox.addGift(rewards, 4, 0, 10_000_000); // 10M Beri
                GiftBox.addGift(rewards, 4, 1, 1000);       // 1000 Ruby
                GiftBox.addGift(rewards, 4, 106, 3);        // 3 Rương vàng
                GiftBox.addGift(rewards, 4, 29, 5);         // 5 Rương ác quỷ
                data.spinCount += 15;
                data.spinsEarned += 15;
            } else if (level % 10 == 0) {
                // Mốc chẵn x10 VIP (10, 20, 30, 40, 60, 70, 80, 90)
                long beri = 1_500_000L + (long) level * 30_000L;
                int ruby = 50 + (level * 2);
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);
                GiftBox.addGift(rewards, 4, 106, 1); // 1 Rương vàng
                GiftBox.addGift(rewards, 4, 29, 1);  // 1 Rương ác quỷ
                int spins = (level >= 60) ? 4 : 2;
                data.spinCount += spins;
                data.spinsEarned += spins;
            } else if (level % 5 == 0) {
                // Mốc lẻ x5 VIP (5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
                long beri = 800_000L + (long) level * 20_000L;
                int ruby = 30 + level;
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);
                GiftBox.addGift(rewards, 4, 29, 1);  // 1 Rương ác quỷ
                GiftBox.addGift(rewards, 4, 223, 3); // 3 Đá Hải Thạch cấp 3
                int spins = (level >= 60) ? 3 : 2;
                data.spinCount += spins;
                data.spinsEarned += spins;
            } else if (level % 2 == 0) {
                // Mốc chẵn VIP
                long beri = 300_000L + (long) level * 10_000L;
                int ruby = 15 + (level / 2);
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);
                if (level <= 40) {
                    GiftBox.addGift(rewards, 7, 3, 5);   // 5 Đá cường hóa 3
                    GiftBox.addGift(rewards, 4, 222, 3); // 3 Đá Hải Thạch 2
                } else if (level <= 80) {
                    GiftBox.addGift(rewards, 7, 4, 5);   // 5 Đá cường hóa 4
                    GiftBox.addGift(rewards, 4, 223, 3); // 3 Đá Hải Thạch 3
                } else {
                    GiftBox.addGift(rewards, 7, 5, 5);   // 5 Đá cường hóa 5
                    GiftBox.addGift(rewards, 4, 29, 1);  // 1 Rương ác quỷ
                }
                data.spinCount += 1;
                data.spinsEarned += 1;
            } else {
                // Các mốc lẻ thường VIP (3, 7, 9, 11, 13, 17, 19...)
                long beri = 200_000L + (long) level * 8_000L;
                int ruby = 10 + (level / 3);
                GiftBox.addGift(rewards, 4, 0, (int) Math.min(Integer.MAX_VALUE, beri));
                GiftBox.addGift(rewards, 4, 1, ruby);

                if (level <= 30) {
                    GiftBox.addGift(rewards, 7, 3, 5);   // 5 Đá cường hóa 3
                    GiftBox.addGift(rewards, 4, 222, 3); // 3 Đá Hải Thạch 2
                } else if (level <= 60) {
                    GiftBox.addGift(rewards, 7, 4, 5);   // 5 Đá cường hóa 4
                    GiftBox.addGift(rewards, 4, 223, 3); // 3 Đá Hải Thạch 3
                } else {
                    GiftBox.addGift(rewards, 7, 5, 5);   // 5 Đá cường hóa 5
                    GiftBox.addGift(rewards, 4, 224, 3); // 3 Đá Hải Thạch 4
                }
                data.spinCount += 1;
                data.spinsEarned += 1;
            }
        }
    }

    /**
     * Mở Rương Vô Cực ngẫu nhiên cho các cấp vượt mốc (> 100) chuẩn phong cách Free Fire
     */
    public static void rollOverLevelReward(List<GiftBox> rewards, boolean isVip, int overLevel, NamiPassData data) {
        int r = ZUtil.random(100);
        if (!isVip) {
            // Rương Vô Cực Thường
            if (r < 40) {
                int beri = ZUtil.random(500_000, 2_000_000);
                GiftBox.addGift(rewards, 4, 0, beri);
            } else if (r < 70) {
                int ruby = ZUtil.random(15, 35);
                GiftBox.addGift(rewards, 4, 1, ruby);
            } else if (r < 85) {
                int haiThachId = ZUtil.random(221, 222);
                int count = ZUtil.random(2, 4);
                GiftBox.addGift(rewards, 4, haiThachId, count);
            } else if (r < 95) {
                int count = ZUtil.random(1, 2);
                GiftBox.addGift(rewards, 4, 18, count); // Rương huyền bí
            } else {
                GiftBox.addGift(rewards, 4, 29, 1); // Rương ác quỷ
            }
        } else {
            // Rương Vô Cực Hoàng Kim VIP
            if (r < 35) {
                int ruby = ZUtil.random(50, 150);
                GiftBox.addGift(rewards, 4, 1, ruby);
            } else if (r < 60) {
                int beri = ZUtil.random(2_000_000, 8_000_000);
                GiftBox.addGift(rewards, 4, 0, beri);
            } else if (r < 75) {
                int spins = ZUtil.random(1, 2);
                data.spinCount += spins;
                data.spinsEarned += spins;
            } else if (r < 90) {
                int stoneId = ZUtil.random(4, 6); // Đá cường hóa cấp 4-6
                int count = ZUtil.random(2, 5);
                GiftBox.addGift(rewards, 7, stoneId, count);
            } else if (r < 97) {
                int count = ZUtil.random(1, 2);
                GiftBox.addGift(rewards, 4, 29, count); // Rương ác quỷ
            } else {
                GiftBox.addGift(rewards, 4, 106, 1); // Rương huyền thoại
            }
        }
    }

    /**
     * Nhận toàn bộ phần thưởng Nami Pass:
     * - Quà các mốc hợp lệ 1 -> 100 chưa nhận (Free 1, 5, 10... / VIP 1, 3, 5, 7...)
     * - Mở tất cả Rương Vô Cực ngẫu nhiên cho các cấp vượt mốc (> 100) chưa nhận
     */
    public static void claimAllRewards(Player p) throws IOException {
        NamiPassData data = getPassData(p);
        List<GiftBox> rewards = new ArrayList<>();

        int claimedFreeCount = 0;
        int claimedVipCount = 0;
        int overLevelsToClaim = 0;
        int spinsGained = 0;

        int maxMain = Math.min(MAX_MAIN_LEVEL, data.level);

        // 1. Nhận quà mốc cố định 1 -> 100 (chỉ các mốc hợp lệ)
        for (int lv = 1; lv <= maxMain; lv++) {
            if (hasFreeReward(lv) && !data.claimedFree.contains(lv)) {
                data.claimedFree.add(lv);
                claimedFreeCount++;
                addMilestoneRewards(rewards, lv, false, data);
            }

            if (data.isVip && hasVipReward(lv) && !data.claimedVip.contains(lv)) {
                data.claimedVip.add(lv);
                claimedVipCount++;
                addMilestoneRewards(rewards, lv, true, data);
            }
        }

        // 2. Mở Rương Vô Cực cho các cấp vượt mốc (> 100)
        if (data.level > MAX_MAIN_LEVEL) {
            int totalOver = data.level - MAX_MAIN_LEVEL;
            int unclaimedOver = totalOver - data.claimedOverLevels;
            if (unclaimedOver > 0) {
                overLevelsToClaim = unclaimedOver;
                int spinStart = data.spinCount;
                for (int i = 0; i < unclaimedOver; i++) {
                    int currentOver = data.claimedOverLevels + i + 1;
                    int actualLevel = MAX_MAIN_LEVEL + currentOver;
                    rollOverLevelReward(rewards, false, currentOver, data);
                    if (data.isVip) {
                        rollOverLevelReward(rewards, true, currentOver, data);
                    }
                    if (actualLevel % 5 == 0) {
                        data.spinCount++;
                        data.spinsEarned++;
                    }
                }
                spinsGained = data.spinCount - spinStart;
                data.claimedOverLevels = totalOver;
            }
        }

        if (claimedFreeCount == 0 && claimedVipCount == 0 && overLevelsToClaim == 0) {
            p.getService().send_box_ThongBao_OK("Bạn chưa có phần thưởng Pass nào mới để nhận!");
            return;
        }

        savePassData(p, p.currentPassSeason, data);
        p.get_coin();

        StringBuilder msg = new StringBuilder("Nhận thành công quà Thẻ Vô Cực Nami Pass:");
        if (claimedFreeCount > 0) msg.append("\n• ").append(claimedFreeCount).append(" mốc Thường (1, 5, 10, 15...)");
        if (claimedVipCount > 0) msg.append("\n• ").append(claimedVipCount).append(" mốc VIP (1, 3, 5, 7...)");
        if (overLevelsToClaim > 0) msg.append("\n• ").append(overLevelsToClaim).append(" Rương Vô Cực ngẫu nhiên (Cấp > 100)");
        if (spinsGained > 0) msg.append("\n• +").append(spinsGained).append(" Lượt Quay Nami Special");

        Service.send_gift(p, 1, "Thẻ Vô Cực Nami Pass", msg.toString(), rewards, true);
    }

    public static void showPassOverview(Player p) throws IOException {
        NamiPassData data = getPassData(p);
        
        StringBuilder info = new StringBuilder();
        info.append("=== THẺ VÔ CỰC NAMI PASS (").append(data.seasonKey != null ? data.seasonKey : CURRENT_SEASON).append(") ===\n");
        if (data.level <= MAX_MAIN_LEVEL) {
            info.append("• Cấp Độ: ").append(data.level).append(" / 100\n");
        } else {
            info.append("• Cấp Độ: 100 (Vượt Mốc: +").append(data.level - MAX_MAIN_LEVEL).append(" Cấp)\n");
        }
        info.append("• EXP Hiện Tại: ").append(data.exp).append(" / ").append(getExpRequiredForLevel(data.level)).append("\n");
        info.append("• Trạng Thái: ").append(data.isVip ? " VIP Thẻ Vô Cực (Mở toàn bộ quà VIP)" : " Thường (Free Pass)").append("\n");
        info.append("• Lượt Quay Nami Special: ").append(data.spinCount).append(" lượt\n");
        info.append("-----------------------------\n");
        info.append("• Tiến Trình Mốc Quà Chính:\n");
        info.append("  - Thường: ").append(data.claimedFree.size()).append("/").append(TOTAL_FREE_MILESTONES).append(" mốc (Cấp 1, 5, 10, 15... 100)\n");
        if (data.isVip) {
            info.append("  - VIP: ").append(data.claimedVip.size()).append("/").append(TOTAL_VIP_MILESTONES).append(" mốc (Cấp 1, 3, 5, 7... 100)\n");
        } else {
            info.append("  - VIP: Chưa kích hoạt (55 mốc VIP đang chờ mở khóa!)\n");
        }
        if (data.level > MAX_MAIN_LEVEL) {
            int totalOver = data.level - MAX_MAIN_LEVEL;
            int unclaimedOver = totalOver - data.claimedOverLevels;
            info.append("• Rương Vô Cực Vượt Cấp: Đã mở ").append(data.claimedOverLevels).append("/").append(totalOver).append(" Rương");
            if (unclaimedOver > 0) {
                info.append(" (Đang có ").append(unclaimedOver).append(" Rương chưa nhận!)");
            }
            info.append("\n");
        }
        info.append("-----------------------------\n");
        info.append("Lưu ý: Sau Cấp 100, mỗi cấp tăng thêm sẽ nhận ngay 1 Rương Vô Cực ngẫu nhiên (VIP nhận thêm Rương Hoàng Kim)! Cứ 5 cấp tặng thêm 1 Lượt quay!");

        p.getService().send_box_ThongBao_OK(info.toString());
    }

    public static void showRewardsPreview(Player p) {
        StringBuilder sb = new StringBuilder("=== BẢNG MỐC THƯỞNG THẺ VÔ CỰC NAMI PASS ===\n");
        sb.append("[MỐC THƯỜNG - 21 MỐC (1, 5, 10, 15... 100)]:\n");
        sb.append("• Mốc 1: 1M Beri, 50 Ruby, 10 Đá Cường Hóa 1, 5 Vé Đấu Trường\n");
        sb.append("• Mốc 5, 15, 25, 35, 45, 55, 65, 75, 85, 95: Đá Hải Thạch, Đá Cường Hóa, Vé Quay Nami\n");
        sb.append("• Mốc 10, 20, 30, 40, 60, 70, 80, 90: Rương Huyền Bí, Rương Ác Quỷ, Ruby lớn\n");
        sb.append("• Mốc 50 (Nửa Mùa): 25M Beri, 500 Ruby, 1 Rương Huyền Thoại, 3 Rương Ác Quỷ, 5 Vé Quay Special!\n");
        sb.append("• Mốc 100 (Đại Mốc): 50M Beri, 1.500 Ruby, 2 Rương Huyền Thoại, 5 Rương Ác Quỷ, 15 Vé Quay Special!\n");
        sb.append("-----------------------------\n");
        sb.append("[MỐC VIP - 55 MỐC (CẤP LẺ 1, 3, 5, 7... & MỐC 10, 20, 30...)]:\n");
        sb.append("• Mốc 1 (VIP Chào Mừng): 5M Beri, 300 Ruby, 10 Đá Cường Hóa 4, 5 Vé Quay Nami\n");
        sb.append("• Mốc Lẻ Thường (3, 7, 9...): 2M-20M Beri, 50-150 Ruby, Đá Cường Hóa 4-7, Vé Quay\n");
        sb.append("• Mốc Lẻ x5 (5, 15, 25...): 10M-30M Beri, 300-800 Ruby, Rương Ác Quỷ, 3-5 Vé Quay\n");
        sb.append("• Mốc Chẵn x10 (10, 20, 30...): 20M-50M Beri, 500-1500 Ruby, Rương Huyền Thoại, 5-10 Vé Quay\n");
        sb.append("• Mốc 50 VIP: 50M Beri, 1.500 Ruby, 2 Rương Huyền Thoại, 5 Rương Ác Quỷ, 10 Vé Quay\n");
        sb.append("• Mốc 100 VIP: 100M Beri, 3.000 Ruby, 3 Rương Huyền Thoại, 10 Rương Ác Quỷ, 25 Vé Quay Special!\n");
        sb.append("-----------------------------\n");
        sb.append("[CẤP > 100 - RƯƠNG VÔ CỰC RANDOM]:\n");
        sb.append("• Mỗi 1 Cấp sau 100: Nhận 1 Rương Vô Cực Thường (Random Beri, Ruby, Đá Hải Thạch, Rương Ác Quỷ)\n");
        sb.append("• VIP nhận thêm: 1 Rương Vô Cực Hoàng Kim (Random 50-150 Ruby, 2M-8M Beri, Đá Cường Hóa S/Vô Cực, Rương Huyền Thoại)\n");
        sb.append("• Cứ mỗi 5 cấp vượt mốc: Thưởng thêm 1 Lượt Quay Nami Special!");

        p.getService().send_box_ThongBao_OK(sb.toString());
    }

    public static void showLeaderboard(Player p) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT h.player_id, pl.name, h.data FROM historys h JOIN players pl ON h.player_id = pl.id WHERE h.type LIKE 'NAMI_PASS_%' LIMIT 200"
             );
             ResultSet rs = ps.executeQuery()
        ) {
            List<Map<String, Object>> players = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String jsonData = rs.getString("data");
                if (jsonData != null && !jsonData.isEmpty()) {
                    JSONObject obj = (JSONObject) JSONValue.parse(jsonData);
                    if (obj != null) {
                        int lv = Integer.parseInt(obj.getOrDefault("lv", 1).toString());
                        long exp = Long.parseLong(obj.getOrDefault("exp", 0L).toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name);
                        map.put("lv", lv);
                        map.put("exp", exp);
                        players.add(map);
                    }
                }
            }

            players.sort((a, b) -> {
                int cmpLv = Integer.compare((int) b.get("lv"), (int) a.get("lv"));
                if (cmpLv != 0) return cmpLv;
                return Long.compare((long) b.get("exp"), (long) a.get("exp"));
            });

            StringBuilder sb = new StringBuilder("=== TOP BẢNG XẾP HẠNG NAMI PASS ===\n");
            for (int i = 0; i < Math.min(100, players.size()); i++) {
                Map<String, Object> entry = players.get(i);
                int lv = (int) entry.get("lv");
                sb.append("#").append(i + 1).append(". ").append(entry.get("name"));
                if (lv <= MAX_MAIN_LEVEL) {
                    sb.append(" - Cấp ").append(lv).append("/100");
                } else {
                    sb.append(" - Cấp 100 (+").append(lv - MAX_MAIN_LEVEL).append(" Vượt Cấp)");
                }
                sb.append(" (EXP: ").append(entry.get("exp")).append(")\n");
            }
            if (players.isEmpty()) {
                sb.append("Chưa có dữ liệu bảng xếp hạng Nami Pass.");
            }
            p.getService().send_box_ThongBao_OK(sb.toString());
        } catch (Exception e) {
            p.getService().send_box_ThongBao_OK("Lỗi tải bảng xếp hạng Nami Pass: " + e.getMessage());
        }
    }
}
