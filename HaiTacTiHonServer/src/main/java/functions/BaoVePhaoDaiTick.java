package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import clan.Clan;
import map.zones.BaoVePhaoDai;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaoVePhaoDaiTick extends AbsTableTickOption {

    public BaoVePhaoDaiTick(Player player) {
        super(player);
    }

    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        if (leader == null || leader.clan == null) {
            if (leader != null && leader.getService() != null) {
                leader.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            }
            return;
        }

        boolean isOpenBv = activities.TimedDungeonManager.gI().isDungeonOpen("BAO_VE_PHAO_DAI")
                || (leader.clan.baoVePhaoDai != null && !leader.clan.baoVePhaoDai.isClose);
        if (!isOpenBv && leader.admin == 0) {
            if (leader.getService() != null) {
                leader.getService().send_box_ThongBao_OK("Bảo Vệ Pháo Đài hiện chưa mở cửa!\n"
                        + activities.TimedDungeonManager.gI().getDungeonStatusMessage("BAO_VE_PHAO_DAI"));
            }
            return;
        }

        if (readyPlayers == null || readyPlayers.isEmpty()) {
            if (leader.getService() != null) {
                leader.getService().send_box_ThongBao_OK("Không có thành viên nào sẵn sàng!");
            }
            return;
        }

        // Yêu cầu ít nhất 3 người hoặc 1 người 2 lính đánh thuê (tổng sẵn sàng >= 3)
        if (readyPlayers.size() < 3) {
            if (leader.getService() != null) {
                leader.getService().send_box_ThongBao_OK("Bảo Vệ Pháo Đài yêu cầu ít nhất 3 thành viên (hoặc 1 người cùng 2 lính đánh thuê)!");
            }
            return;
        }

        // Nếu clan đang trong trận -> vào thẳng trận
        if (leader.clan.baoVePhaoDai != null && !leader.clan.baoVePhaoDai.isClose) {
            boolean isTeamA = leader.clan.equals(leader.clan.baoVePhaoDai.clanA);
            map.Zone targetZone = isTeamA ? leader.clan.baoVePhaoDai.mapClanA : leader.clan.baoVePhaoDai.mapClanB;
            if (targetZone != null) {
                map.Vgo vgo = new map.Vgo();
                vgo.map_go = new map.Zone[]{targetZone};
                vgo.xnew = (short) (isTeamA ? 200 : 1400);
                vgo.ynew = 260;
                for (Player p : readyPlayers) {
                    if (p != null) {
                        p.type_pk = (byte) (isTeamA ? 4 : 5);
                        try {
                            p.goto_map(vgo);
                        } catch (Exception ignored) {}
                    }
                }
            }
            return;
        }

        // Đưa clan vào hàng đợi ghép trận Bảo Vệ Pháo Đài
        leader.clan.pWait = new ArrayList<>(readyPlayers);
        leader.clan.isGhepBaoVePhaoDai = true;
        leader.clan.timeGhepBaoVePhaoDai = System.currentTimeMillis() + core.ZUtil.random(18_000, 35_000);
        long expireTime = leader.clan.timeGhepBaoVePhaoDai;
        for (Player p : readyPlayers) {
            if (p != null && p.getService() != null) {
                p.getService().send_time_cool_down(expireTime, "Ghép Bảo Vệ Pháo Đài", 0);
            }
        }
        Clan.findBaoVePhaoDai();

        if (leader.clan.baoVePhaoDai != null) {
            leader.getService().send_box_ThongBao_OK("Ghép trận Bảo Vệ Pháo Đài thành công!\nTiến vào chiến trường!");
        } else {
            leader.getService().send_box_ThongBao_OK("Đã đăng ký ghép trận Bảo Vệ Pháo Đài!\nĐang tìm đối thủ...");
        }
    }

    @Override
    public void onDecline(Player p) throws IOException {
        if (p != null && p.getService() != null) {
            p.getService().send_box_ThongBao_OK("Đã huỷ thao tác ghép trận Bảo Vệ Pháo Đài.");
        }
    }
}
