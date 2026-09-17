package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import map.zones.TranChienKhongLo;
import java.io.IOException;
import java.util.List;

public class PhoBanKhongLoTick extends AbsTableTickOption {

    public PhoBanKhongLoTick(Player player) {
        super(player);
    }

    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        if (leader == null || leader.clan == null) {
            if (leader != null) {
                leader.getService().send_box_ThongBao_OK("Bạn chưa có băng hải tặc!");
            }
            return;
        }

        if (readyPlayers == null || readyPlayers.isEmpty()) {
            leader.getService().send_box_ThongBao_OK("Không có thành viên nào sẵn sàng tham gia phó bản Khổng Lồ!");
            return;
        }

        // Đăng ký trực tiếp vào hệ thống ghép trận Phó Bản Khổng Lồ
        TranChienKhongLo.registerClanQueue(leader.clan, readyPlayers);
    }

    @Override
    public void onDecline(Player p) throws IOException {
        if (p != null) {
            p.getService().send_box_ThongBao_OK("Đã huỷ thao tác ghép trận Phó Bản Khổng Lồ.");
        }
    }
}
