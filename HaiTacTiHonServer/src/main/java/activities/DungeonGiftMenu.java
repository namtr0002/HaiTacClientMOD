package activities;

import model.Menu;
import model.Player;
import network.Service;
import template.GiftBox;
import historys.DungeonRewardHistory;
import historys.DungeonRewardHistory.PendingReward;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DungeonGiftMenu — Menu nhận quà Top Phó Bản & Săn Boss cho player.
 *
 * Lưu vào bảng historys (TYPE=DUNGEON_TOP_REWARD), có mã MD5 định danh và đánh dấu claimed.
 * Hỗ trợ mở menu tổng quát (tất cả phó bản) hoặc menu chuyên biệt theo từng phó bản/NPC.
 */
public class DungeonGiftMenu {

    /**
     * Mở menu danh sách toàn bộ quà top phó bản / săn Boss chưa nhận của player.
     */
    public static void openMenu(Player p) throws IOException {
        openMenuByType(p, null);
    }

    /**
     * Mở menu danh sách quà chưa nhận theo đúng loại phó bản / Boss cụ thể (hoặc null để lấy tất cả).
     */
    public static void openMenuByType(Player p, String dungeonType) throws IOException {
        if (p == null || p.isdie) return;
        List<PendingReward> pending = (dungeonType != null && !dungeonType.trim().isEmpty())
            ? DungeonRewardHistory.getPendingRewardsByType(p.IDPlayer, dungeonType)
            : DungeonRewardHistory.getPendingRewards(p.IDPlayer);

        String typeDisplayName = (dungeonType != null && !dungeonType.trim().isEmpty())
            ? PendingReward.dungeonDisplayName(dungeonType)
            : "Phó Bản & Boss";

        if (pending == null || pending.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bạn hiện không có quà [" + typeDisplayName + "] nào chưa nhận!");
            return;
        }

        List<Menu> menus = new ArrayList<>();

        // Nút nhận tất cả nếu có từ 2 phần thưởng trở lên
        if (pending.size() > 1) {
            menus.add(new Menu(">>> NHẬN TẤT CẢ (" + pending.size() + " phần thưởng)", (short) 136, () -> {
                try {
                    List<GiftBox> allGifts = new ArrayList<>();
                    List<Integer> rowIds = new ArrayList<>();
                    for (PendingReward pr : pending) {
                        if (pr.gifts != null && !pr.gifts.isEmpty()) {
                            allGifts.addAll(pr.gifts);
                            rowIds.add(pr.rowId);
                        }
                    }
                    if (!allGifts.isEmpty()) {
                        allGifts = GiftBox.consolidateGifts(allGifts);
                        core.RewardService.sendGiftOrMail(p, 1,
                            "Quà " + typeDisplayName,
                            "Tất cả " + rowIds.size() + " phần thưởng [" + typeDisplayName + "]",
                            allGifts, true);
                        for (int rId : rowIds) {
                            DungeonRewardHistory.markClaimed(rId);
                        }
                        p.getService().send_box_ThongBao_OK("Đã nhận thành công tất cả " + rowIds.size() + " phần thưởng [" + typeDisplayName + "]!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        // Từng phần thưởng riêng lẻ
        for (PendingReward pr : pending) {
            String label = "> " + pr.getDisplayName() + " [" + pr.getShortMd5() + "] | " + pr.getGiftSummary();
            menus.add(new Menu(label, (short) 133, () -> {
                try {
                    if (pr.gifts == null || pr.gifts.isEmpty()) {
                        p.getService().send_box_ThongBao_OK("Phần quà này không còn hợp lệ!");
                        return;
                    }

                    // Trao quà
                    core.RewardService.sendGiftOrMail(p, 1,
                        "Quà " + pr.getDisplayName(),
                        pr.getDisplayName() + " (MD5: " + pr.getShortMd5() + ")",
                        pr.gifts, true);

                    // Đánh dấu đã nhận trong DB
                    DungeonRewardHistory.markClaimed(pr.rowId);

                    p.getService().send_box_ThongBao_OK("Nhận quà [" + pr.getDisplayName() + "] thành công!\nMã MD5: " + pr.roundToken);

                    // Mở lại menu nếu còn quà khác
                    List<PendingReward> remaining = (dungeonType != null && !dungeonType.trim().isEmpty())
                        ? DungeonRewardHistory.getPendingRewardsByType(p.IDPlayer, dungeonType)
                        : DungeonRewardHistory.getPendingRewards(p.IDPlayer);

                    if (remaining != null && !remaining.isEmpty()) {
                        openMenuByType(p, dungeonType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        menus.add(new Menu("Đóng", (short) 134, () -> {}));

        String title = "Hòm Thư Quà [" + typeDisplayName + "] (" + pending.size() + " phần thưởng)";
        p.getService().openDynamicMenu(8810, title, menus);
    }

    /**
     * Fallback cho ClientYesNo (nếu có).
     */
    public static void handleMenu(Player p, int index) throws IOException {
        openMenu(p);
    }
}
