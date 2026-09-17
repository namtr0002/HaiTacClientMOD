package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import java.io.IOException;
import java.util.List;
import map.Zone;
import map.Vgo;
import map.zones.ThuLinhBienKhoi;

public class ThuLinhBienKhoiTick extends AbsTableTickOption {

    public ThuLinhBienKhoiTick(Player player) {
        super(player);
    }

    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        if (leader == null) return;
        if (!ThuLinhBienKhoi.isOpen()) {
            leader.getService().send_box_ThongBao_OK("Hoạt động Thủ Lĩnh Biển Khơi chưa đến giờ mở cửa (19h T2, T4, T6)!");
            return;
        }
        if (leader.clan == null || leader.clan.map_create == null || leader.clan.map_create.map_thuLinhBienKhoi == null) {
            leader.getService().send_box_ThongBao_OK("Chưa khởi tạo khu vực phó bản cho Băng!");
            return;
        }

        // Kiểm tra chìa khóa phó bản của các thành viên
        StringBuilder name_ok = new StringBuilder();
        for (Player p0 : readyPlayers) {
            if (p0 != null && !p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
                if (p0.get_key_boss() < 2) {
                    leader.getService().send_box_ThongBao_OK("Thành viên " + p0.name + " không đủ 2 Chìa khóa phó bản!");
                    return;
                }
                name_ok.append(p0.name).append(", ");
            }
        }

        int seaFlag = leader.clan.map_create.map_thuLinhBienKhoi.flag;

        leader.data_yesno = new int[]{81};
        leader.setyesNoDialog(new model.YesNoDialog(leader, 81, "Thông báo",
                "Để tham gia phó bản Thủ Lĩnh Biển Khơi mỗi thành viên sẽ phải mất 2 Chìa khoá phó bản, bạn có muốn tham gia?\nThành viên: " + name_ok,
                new String[]{"Đồng ý", "Huỷ"}, new byte[]{0, 1},
                (val) -> {
                    if (val == 0 || val == 2) {
                        if (!ThuLinhBienKhoi.isOpen()) {
                            try { leader.getService().send_box_ThongBao_OK("Hoạt động Thủ Lĩnh Biển Khơi chưa đến giờ mở cửa!"); } catch (Exception ignored) {}
                            return;
                        }
                        if (leader.clan == null || leader.clan.map_create == null) {
                            try { leader.getService().send_box_ThongBao_OK("Băng hội chưa tạo phòng phó bản!"); } catch (Exception ignored) {}
                            return;
                        }
                        // Kiểm tra lại chìa khóa
                        for (Player p0 : readyPlayers) {
                            if (p0 != null && !p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
                                if (p0.get_key_boss() < 2) {
                                    try { leader.getService().send_box_ThongBao_OK("Thành viên " + p0.name + " không đủ 2 Chìa khóa phó bản!"); } catch (Exception ignored) {}
                                    return;
                                }
                            }
                        }
                        // Trừ 2 chìa khóa phó bản
                        for (Player l : readyPlayers) {
                            if (l != null && !l.isBot && !l.isDe && !(l instanceof model.DeTu) && !(l instanceof bot.mercenary.MercenaryBot)) {
                                l.update_key_boss(-2);
                                l.updateMoney();
                            }
                        }
                        // Dịch chuyển toàn đội vào phó bản
                        Vgo vgo = new Vgo();
                        vgo.map_go = new Zone[]{leader.clan.map_create};
                        vgo.xnew = 100;
                        vgo.ynew = 230;
                        for (Player l : readyPlayers) {
                            if (l != null) {
                                l.save_previous_map();
                                l.tableTickOption = null;
                                l.type_pk = -1; // Tắt PK khi ở vùng biển riêng của clan
                                try {
                                    l.goto_map(vgo);
                                    if (l.getService() != null) {
                                        l.getService().update_PK(l, false);
                                    }
                                } catch (IOException ignored) {}
                            }
                        }
                    }
                }));
        leader.getService().startYesNo();
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
        if (p != null) {
            p.getService().send_box_ThongBao_OK("Đã huỷ thao tác tham gia Thủ Lĩnh Biển Khơi.");
        }
    }
}
