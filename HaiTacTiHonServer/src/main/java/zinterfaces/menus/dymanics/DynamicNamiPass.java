package zinterfaces.menus.dymanics;

import achievement.ArchiPrivatePass;
import achievement.NamiPassManager;
import model.Player;
import java.io.IOException;
import model.VongQuayNami;
import model.YesNoDialog;
import zinterfaces.iMenu;
import zinterfaces.iMenuDymanic;

/**
 * DynamicNamiPass — Dynamic sub-menu cho Thẻ Vô Cực Nami Pass.
 * Tự động chèn option "Nami Pass" vào NPC Nami (-72).
 */
public class DynamicNamiPass implements iMenuDymanic, iMenu {

    public static final short SUB_MENU_ID = 9956;

    @Override
    public short[] getNpcId() {
        return new short[]{SUB_MENU_ID};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Nami Pass"};
    }

    @Override
    public short[] getIcons() {
        return new short[]{134};
    }

    @Override
    public Position getPosition() {
        return Position.INDEX;
    }

    @Override
    public int getPositionIndex() {
        return 2; // Hiển thị sau "Thành tích hằng ngày"
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        showNamiPassMenu(p);
    }

    public static void showNamiPassMenu(Player p) throws IOException {
        String[] options = new String[]{
            "Nhiệm Vụ Nami Pass",
            "Thông Tin Nami Pass",
            "Bảng Mốc Thưởng Pass",
            "Kích Hoạt VIP Pass",
            "Mua Cấp Nami Pass",
            "Nhận Tất Cả Quà Pass",
            "Vòng Quay Nami Special",
            "BXH Nami Pass",
            "Xem Pass Mùa Cũ"
        };
        short[] icons = new short[]{134, 134, 134, 110, 133, 134, 169, 110, 134};
        iMenuDymanic.buildAndSend(p, SUB_MENU_ID, "Nami Pass", options, icons);
    }

    @Override
    public short[] getId() {
        return new short[]{SUB_MENU_ID, 9955};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == SUB_MENU_ID) {
            switch (index) {
                case 0: { // Nhiệm Vụ Nami Pass
                    ArchiPrivatePass.show_table(p, p.currentPassSeason != null ? p.currentPassSeason : ArchiPrivatePass.CURRENT_SEASON);
                    break;
                }
                case 1: { // Thông Tin Nami Pass
                    NamiPassManager.showPassOverview(p);
                    break;
                }
                case 2: { // Bảng Mốc Thưởng Pass
                    NamiPassManager.showRewardsPreview(p);
                    break;
                }
                case 3: { // Kích Hoạt VIP Pass
                    NamiPassManager.NamiPassData data = NamiPassManager.getPassData(p);
                    if (data.isVip) {
                        p.getService().send_box_ThongBao_OK("Bạn đã kích hoạt Thẻ Vô Cực VIP Nami Pass mùa này rồi!");
                        return;
                    }
                    new YesNoDialog(p, 9992, "Kích Hoạt VIP Pass",
                        "Bạn có muốn dùng 5.000 Ruby để kích hoạt Thẻ Vô Cực VIP Nami Pass mùa này không?\n(Mở khóa toàn bộ Quà VIP từ Cấp 1 -> 100 & Rương Hoàng Kim Vượt Cấp)",
                        new String[]{"Kích hoạt", "Hủy"}, new byte[]{2, 1}, val -> {
                            if (val == 0) {
                                if (p.get_ngoc() < 5000) {
                                    p.getService().send_box_ThongBao_OK("Bạn không đủ 5.000 Ruby!");
                                    return;
                                }
                                p.update_ngoc_ex(-5000);
                                p.updateMoney();
                                NamiPassManager.unlockVipPass(p);
                                p.getService().send_box_ThongBao_OK("Chúc mừng! Bạn đã kích hoạt thành công Thẻ Vô Cực VIP Nami Pass!");
                            }
                        }).startYesNo();
                    break;
                }
                case 4: { // Mua Cấp Nami Pass
                    p.sendInput("Mua Cấp Nami Pass (20 Ruby / Cấp)", new String[]{"Nhập số cấp muốn mua (1 - 1000):"}, textInputs -> {
                        try {
                            int count = Integer.parseInt(textInputs[0].trim());
                            if (count < 1 || count > 1000) {
                                p.getService().send_box_ThongBao_OK("Số cấp nhập phải từ 1 đến 1.000!");
                                return;
                            }
                            int finalCount = count;
                            long costRuby = (long) finalCount * 20L; // 20 Ruby / 1 Cấp
                            new YesNoDialog(p, 9991, "Xác nhận mua cấp Nami Pass",
                                "Bạn có chắc muốn dùng " + costRuby + " Ruby để tăng " + finalCount + " cấp Nami Pass không?",
                                new String[]{"Mua ngay", "Hủy"}, new byte[]{2, 1}, val -> {
                                    if (val == 0) {
                                        if (p.get_ngoc() < costRuby) {
                                            p.getService().send_box_ThongBao_OK("Bạn không đủ " + costRuby + " Ruby!");
                                            return;
                                        }
                                        p.update_ngoc_ex((int) -costRuby);
                                        p.updateMoney();
                                        NamiPassManager.addPassLevel(p, finalCount);
                                        p.getService().send_box_ThongBao_OK("Đã mua thành công +" + finalCount + " cấp Nami Pass!");
                                    }
                                }).startYesNo();
                        } catch (Exception e) {
                            p.getService().send_box_ThongBao_OK("Số cấp nhập vào không hợp lệ!");
                        }
                    });
                    break;
                }
                case 5: { // Nhận Tất Cả Quà Pass
                    NamiPassManager.claimAllRewards(p);
                    break;
                }
                case 6: { // Vòng Quay Nami Special
                    VongQuayNami.gI().showTable(p);
                    break;
                }
                case 7: { // BXH Nami Pass
                    NamiPassManager.showLeaderboard(p);
                    break;
                }
                case 8: { // Xem Pass Mùa Cũ
                    String[] seasonMenu = new String[ArchiPrivatePass.SEASONS.length];
                    for (int i = 0; i < seasonMenu.length; i++) {
                        seasonMenu[i] = "Pass " + ArchiPrivatePass.SEASONS[i];
                    }
                    iMenuDymanic.buildAndSend(p, (short) 9955, "Xem Pass Mùa Cũ", seasonMenu, null);
                    break;
                }
            }
        } else if (idNPC == 9955) {
            if (index >= 0 && index < ArchiPrivatePass.SEASONS.length) {
                String selectedSeason = ArchiPrivatePass.SEASONS[index];
                NamiPassManager.NamiPassData oldData = NamiPassManager.getPassData(p, selectedSeason);
                
                StringBuilder sb = new StringBuilder();
                sb.append("=== NAMI PASS (").append(selectedSeason).append(") ===\n");
                if (oldData.level <= NamiPassManager.MAX_MAIN_LEVEL) {
                    sb.append("- Cấp Độ: ").append(oldData.level).append(" / 100\n");
                } else {
                    sb.append("- Cấp Độ: 100 (Vượt Mốc: +").append(oldData.level - NamiPassManager.MAX_MAIN_LEVEL).append(" Cấp)\n");
                }
                sb.append("- EXP: ").append(oldData.exp).append("\n");
                sb.append("- Trạng Thái: ").append(oldData.isVip ? " VIP Thẻ Vô Cực" : " Thường (Free Pass)").append("\n");
                sb.append("- Quà Đã Nhận: ").append(oldData.claimedFree.size()).append("/").append(NamiPassManager.TOTAL_FREE_MILESTONES).append(" mốc Thường");
                if (oldData.isVip) {
                    sb.append(" | ").append(oldData.claimedVip.size()).append("/").append(NamiPassManager.TOTAL_VIP_MILESTONES).append(" mốc VIP");
                }
                if (oldData.claimedOverLevels > 0) {
                    sb.append(" | Đã mở ").append(oldData.claimedOverLevels).append(" Rương Vô Cực");
                }
                sb.append("\n(Chế độ xem tiến trình mùa cũ)");
                p.getService().send_box_ThongBao_OK(sb.toString());
                
                ArchiPrivatePass.show_table(p, selectedSeason);
            }
        }
    }
}
