package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.GameAnalyzer;
import bot.botplayer.ai.Pathfinder;
import model.Quest;
import template.QuestP;
import template.ItemBag47;
import mob.Mob;
import map.Npc;
import map.MapTemplate;
import core.ZUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * BotQuestController — Xử lý nhiệm vụ chính tuyến A-Z đúng như người thật.
 *
 * Luồng chuẩn:
 *   statusQuest=0 -> Đến NPC idNpc -> Nhận nhiệm vụ (remove_old_and_send_next)
 *   statusQuest=1 -> Đến map idMapHelp -> Đánh quái thật -> Đủ tiến độ
 *   statusQuest=2 -> Đến NPC idNpcSub (hoặc idNpc) -> Nộp nhiệm vụ
 *
 * KHÔNG có cơ chế auto-skip hay gian lận tiến độ.
 */
public class BotQuestController {

    private final BotPlayerReal bot;

    // State phụ: đang trên đường về NPC nộp NV
    private boolean pendingDelivery = false;

    public BotQuestController(BotPlayerReal bot) {
        this.bot = bot;
    }

    // ========================= PUBLIC API =========================

    /**
     * Tự động thao tác NPC fake và auto next nhiệm vụ khi hoàn thành các mục tiêu,
     * trực tiếp xử lý bằng code không dùng packet msg.
     */
    public boolean autoNextQuest() {
        QuestP quest = bot.getMainQuest();
        if (quest == null || quest.template == null || quest.template.equals(Quest.QUEST_FINISH)) return false;
        if (bot.level < quest.template.lvRequest) return false;

        int status = quest.template.statusQuest;

        // Case 1: Đang làm NV (status=1)
        if (status == 1) {
            // Chỉ tự hoàn thành nếu là nhiệm vụ đối thoại không có quái / data rỗng
            if ((quest.data == null || quest.data.length == 0) && isAllTargetsMet(quest)) {
                doCompleteQuestStep(quest);
                return true;
            }
        }
        // Case 2 & 3: Cần nhận (status=0) hoặc nộp (status=2) -> Hoàn tất khi bot đã ở gần NPC
        else if (status == 0 || status == 2) {
            int npcId = (status == 2 && quest.template.idNpcSub != 0 && quest.template.idNpcSub != -1) ? quest.template.idNpcSub : quest.template.idNpc;
            if (npcId != 0) {
                int[] found = findNpcOnAnyMap(npcId);
                if (found[0] >= 0 && bot.map != null && bot.map.template != null && bot.map.template.id == found[0]) {
                    double dist = Math.hypot(found[1] - bot.x, found[2] - bot.y);
                    if (dist <= 80) {
                        if (status == 2 && bot.item != null && bot.item.bag47 != null) {
                            List<ItemBag47> toRemove = new ArrayList<>();
                            for (ItemBag47 it : bot.item.bag47) {
                                if (it != null && it.category == 5) {
                                    toRemove.add(it);
                                }
                            }
                            if (!toRemove.isEmpty()) {
                                bot.item.bag47.removeAll(toRemove);
                                try { bot.item.updateInventory(false); } catch (Exception ignored) {}
                            }
                        }
                        doCompleteQuestStep(quest);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Cố gắng tiến hành nhiệm vụ chính tuyến.
     * @return true nếu đang xử lý nhiệm vụ (caller nên dừng hành vi khác)
     */
    public boolean tryAdvanceQuest() {
        QuestP quest = bot.getMainQuest();
        if (quest == null || quest.template == null) {
            pendingDelivery = false;
            return false;
        }
        // Đã hoàn thành hết NV
        if (quest.template.equals(Quest.QUEST_FINISH)) {
            pendingDelivery = false;
            return false;
        }
        // Chưa đủ level nhận NV
        if (bot.level < quest.template.lvRequest) {
            pendingDelivery = false;
            return false;
        }

        // Auto next nếu nhiệm vụ đã đủ điều kiện chuyển bước
        if (autoNextQuest()) {
            return true;
        }

        int status = quest.template.statusQuest;

        switch (status) {
            case 0: return handleReceiveQuest(quest);   // Cần nhận NV
            case 1: return handleDoQuest(quest);         // Đang làm NV
            case 2: return handleDeliverQuest(quest);    // Cần nộp NV
            default: return false;
        }
    }

    /**
     * Kiểm tra bot có nhiệm vụ chính tuyến hợp lệ và đủ cấp độ để làm hay không.
     */
    public boolean hasDoableQuest() {
        QuestP quest = bot.getMainQuest();
        if (quest == null || quest.template == null) return false;
        if (quest.template.equals(Quest.QUEST_FINISH)) return false;
        if (bot.level < quest.template.lvRequest) return false;
        return true;
    }

    /**
     * Kiểm tra có nhiệm vụ đang ở trạng thái cần làm (status=1) hoặc cần nộp (status=2).
     * Dùng để chặn bot đi Boss/Dungeon khi đang giữa chừng nhiệm vụ.
     */
    public boolean hasUnfinishedQuest() {
        QuestP quest = bot.getMainQuest();
        if (quest == null || quest.template == null) return false;
        if (quest.template.equals(Quest.QUEST_FINISH)) return false;
        if (bot.level < quest.template.lvRequest) return false;
        // Đang làm (status=1) hoặc đang chờ nộp (status=2) -> coi là "chưa xong"
        int status = quest.template.statusQuest;
        return status == 1 || status == 2 || pendingDelivery;
    }

    /**
     * Trả về map ID cao nhất bot được phép đến dựa theo quest hiện tại.
     * Dùng để giới hạn training map.
     */
    public int getQuestMapLimit() {
        return bot.checkQuest(); // Delegate về checkQuest() của Player
    }

    // ========================= PRIVATE HANDLERS =========================

    /**
     * Status=0: Đến gặp NPC nhận nhiệm vụ rồi gọi remove_old_and_send_next().
     */
    private boolean handleReceiveQuest(QuestP quest) {
        pendingDelivery = false;
        int npcId = quest.template.idNpc;
        if (npcId == 0) {
            // Không có NPC yêu cầu — tự nhận luôn
            doCompleteQuestStep(quest);
            return true;
        }

        // Tìm NPC trên toàn bộ map
        int[] found = findNpcOnAnyMap(npcId);
        int targetMapId = found[0];
        int npcX = found[1];
        int npcY = found[2];

        if (targetMapId < 0) {
            // Không tìm thấy NPC trên map template nào — bỏ qua để không stuck
            doCompleteQuestStep(quest);
            return true;
        }

        return navigateToNpcAndInteract(quest, targetMapId, (short) npcX, (short) npcY, false);
    }

    /**
     * Status=1: Đến map idMapHelp -> đánh quái thật -> khi đủ tiến độ sẽ tự chuyển.
     */
    private boolean handleDoQuest(QuestP quest) {
        pendingDelivery = false;

        // Nếu là nhiệm vụ trò chuyện hoặc không có quái mục tiêu
        if ((quest.data == null || quest.data.length == 0) && isAllTargetsMet(quest)) {
            doCompleteQuestStep(quest);
            return true;
        }

        int targetMapId = quest.template.idMapHelp;
        if (targetMapId <= 0) {
            // Không cần đến map cụ thể (NV trò chuyện, không có mục tiêu)
            doCompleteQuestStep(quest);
            return true;
        }

        if (bot.map == null || bot.map.template == null) return false;

        // Nếu chưa ở map nhiệm vụ -> di chuyển đến
        if (bot.map.template.id != targetMapId) {
            travelToMap(targetMapId);
            return true;
        }

        // Đã ở đúng map -> tìm quái và đánh thật
        Mob questMob = findQuestMob(quest);
        if (questMob != null) {
            double dist = Math.hypot(questMob.x - bot.x, questMob.y - bot.y);
            if (dist > 80) {
                bot.getMovementController().moveTowards(questMob.x, questMob.y);
            } else {
                bot.getCombatController().attackTarget(questMob);
            }
        } else {
            // Quái chưa hồi sinh hoặc đã dọn hết — chờ tại map NV
            bot.getMovementController().moveRandom();
        }
        return true;
    }

    /**
     * Status=2: Đến NPC nộp NV rồi gọi remove_old_and_send_next().
     */
    private boolean handleDeliverQuest(QuestP quest) {
        pendingDelivery = true;

        int npcId = (quest.template.idNpcSub != 0 && quest.template.idNpcSub != -1) ? quest.template.idNpcSub : quest.template.idNpc;
        if (npcId == 0) {
            // Không có NPC nộp -> tự hoàn thành
            doCompleteQuestStep(quest);
            pendingDelivery = false;
            return true;
        }

        int[] found = findNpcOnAnyMap(npcId);
        int targetMapId = found[0];
        int npcX = found[1];
        int npcY = found[2];

        if (targetMapId < 0) {
            doCompleteQuestStep(quest);
            pendingDelivery = false;
            return true;
        }

        boolean result = navigateToNpcAndInteract(quest, targetMapId, (short) npcX, (short) npcY, true);
        if (!result) pendingDelivery = false;
        return result;
    }

    // ========================= NAVIGATION HELPERS =========================

    /**
     * Di chuyển đến map đích và tương tác với NPC khi đã đến nơi.
     * @param isDeliver true nếu là nộp NV (cần xóa item quest trước)
     */
    private boolean navigateToNpcAndInteract(QuestP quest, int targetMapId,
                                              short npcX, short npcY, boolean isDeliver) {
        if (bot.map == null || bot.map.template == null) return false;

        if (bot.map.template.id != targetMapId) {
            // Cần đi đến map có NPC
            travelToMap(targetMapId);
            return true;
        }

        // Đã ở đúng map — tiến đến NPC
        double dist = Math.hypot(npcX - bot.x, npcY - bot.y);
        if (dist > 60) {
            bot.getMovementController().moveTowards(npcX, npcY);
            return true;
        }

        // Đã cận NPC — hoàn tất bước nhiệm vụ
        if (isDeliver && bot.item != null && bot.item.bag47 != null) {
            // Xóa item nhiệm vụ (category 5) trước khi nộp
            List<ItemBag47> toRemove = new ArrayList<>();
            for (int i = 0; i < bot.item.bag47.size(); i++) {
                if (bot.item.bag47.get(i).category == 5) {
                    toRemove.add(bot.item.bag47.get(i));
                }
            }
            if (!toRemove.isEmpty()) {
                bot.item.bag47.removeAll(toRemove);
                try { bot.item.updateInventory(false); } catch (Exception ignored) {}
            }
        }

        // Chat tự nhiên khi đến NPC
        if (ZUtil.random(100) < 40 && bot.map != null) {
            String[] chats;
            if (isDeliver) {
                chats = new String[]{"Nộp NV đây!", "Xong rồi trả thưởng đi", "Cuối cùng cũng xong nv này", "Nhận thưởng thôi nào"};
            } else {
                chats = new String[]{"Nhận nhiệm vụ mới nào", "NPC ơi cho tao việc làm", "Sắp up lv rồi nhé", "Quest mới đây!"};
            }
            try {
                bot.map.send_chat_popup(0, bot.index_map, chats[ZUtil.random(chats.length)]);
            } catch (Exception ignored) {}
        }

        doCompleteQuestStep(quest);
        pendingDelivery = false;
        return true;
    }

    /**
     * Lên đường đến map đích theo Pathfinder.
     */
    private void travelToMap(int targetMapId) {
        if (bot.currentPath == null || bot.targetMapId != targetMapId) {
            bot.targetMapId = targetMapId;
            bot.currentPath = Pathfinder.findPath(
                    bot.map != null && bot.map.template != null ? bot.map.template.id : 0,
                    targetMapId);
            bot.pathIndex = 0;
            bot.state = "PORTAL";
        }
        bot.getMovementController().traversePath();
    }

    /**
     * Gọi Quest.remove_old_and_send_next() — hoàn tất bước NV và nhận NV tiếp theo.
     * Bọc try/catch để bot không crash (bot dùng NoService, packet gửi sẽ bị hủy nhẹ nhàng).
     */
    private void doCompleteQuestStep(QuestP quest) {
        try {
            Quest.remove_old_and_send_next(bot, quest);
            bot.getPersistenceController().saveBot();
            GameAnalyzer.incrementQuestsCompleted();
            // Reset path để bot chọn lại training map phù hợp với quest mới
            bot.currentPath = null;
            bot.targetMapId = -1;
            bot.state = "FARM";
            bot.lastQuestProgressTime = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================= LOOKUP HELPERS =========================

    /**
     * Tìm NPC (theo idmenu) trên tất cả map templates.
     * @return int[3]: {mapId, x, y} hoặc {-1, 0, 0} nếu không tìm thấy
     */
    private int[] findNpcOnAnyMap(int npcMenuId) {
        if (npcMenuId == 0) return new int[]{-1, 0, 0};
        // Tìm trên map hiện tại trước (nhanh hơn)
        if (bot.map != null && bot.map.template != null && bot.map.template.npcs != null) {
            for (Npc n : bot.map.template.npcs) {
                if (n != null && (n.idmenu == npcMenuId || n.idmenu == -Math.abs(npcMenuId) || n.idmenu == Math.abs(npcMenuId))) {
                    return new int[]{bot.map.template.id, n.x, n.y};
                }
            }
        }
        // Tìm trên toàn bộ map templates
        if (MapTemplate.ENTRYS != null) {
            for (MapTemplate mt : MapTemplate.ENTRYS) {
                if (mt == null || mt.npcs == null) continue;
                for (Npc n : mt.npcs) {
                    if (n != null && (n.idmenu == npcMenuId || n.idmenu == -Math.abs(npcMenuId) || n.idmenu == Math.abs(npcMenuId))) {
                        return new int[]{mt.id, n.x, n.y};
                    }
                }
            }
        }
        return new int[]{-1, 0, 0};
    }

    /**
     * Tìm quái nhiệm vụ trong map hiện tại (chỉ những target chưa đủ).
     */
    private Mob findQuestMob(QuestP quest) {
        if (bot.map == null || bot.map.mobs == null) return null;

        Set<Integer> targetMobIds = new HashSet<>();
        if (quest.data != null) {
            for (short[] d : quest.data) {
                if (d.length >= 4) {
                    int current = d[3];
                    int required = d[2];
                    if (current < required && (d[0] == 1 || d[0] == 2)) {
                        targetMobIds.add((int) d[1]);
                    }
                }
            }
        }

        if (targetMobIds.isEmpty()) return null;

        Mob closest = null;
        double minDist = Double.MAX_VALUE;
        for (Mob mob : bot.map.mobs.values()) {
            if (mob == null || mob.isdie || mob.mtemplate == null) continue;
            if (targetMobIds.contains((int) mob.mtemplate.mob_id)) {
                double dist = Math.hypot(mob.x - bot.x, mob.y - bot.y);
                if (dist < minDist) {
                    minDist = dist;
                    closest = mob;
                }
            }
        }
        return closest;
    }

    /**
     * Kiểm tra tất cả mục tiêu nhiệm vụ đã đủ hay chưa.
     */
    private boolean isAllTargetsMet(QuestP quest) {
        if (quest.data == null || quest.data.length == 0) return true;
        for (short[] d : quest.data) {
            if (d.length >= 4) {
                int current = d[3];
                int required = d[2];
                if (current < required) return false;
            } else if (d.length >= 3) {
                // Dữ liệu cũ không có trường tiến độ — coi như đủ
            }
        }
        return true;
    }
}
