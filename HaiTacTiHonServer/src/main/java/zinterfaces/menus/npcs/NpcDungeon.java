package zinterfaces.menus.npcs;

import model.Player;
import zinterfaces.iNpc;
import functions.MrCandleTick;
import functions.PhoBanKhongLoTick;
import map.Zone;
import map.Vgo;
import map.zones.TranChienKhongLo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Npc;

public class NpcDungeon implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-86};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -86;
        p.menus.clear();
        p.menus.add(new model.Menu("Đá đít Mr3", (short) 150, () -> { try { handleMenu(p, 0); } catch (IOException ignored) {} }));
        p.menus.add(new model.Menu("Phó bản khổng lồ", (short) 142, () -> { try { handleMenu(p, 1); } catch (IOException ignored) {} }));
        p.menus.add(new model.Menu("Thử thách vệ thần", (short) 138, () -> { try { handleMenu(p, 2); } catch (IOException ignored) {} }));
        p.menus.add(new model.Menu("Hòm Thư Quà Phó Bản", (short) 110, () -> { try { activities.DungeonGiftMenu.openMenu(p); } catch (IOException ignored) {} }));
        p.getService().openDynamicMenu(type, "Phó bản", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (index == 0) {
            if (p.party != null && p.party.list != null && p.party.list.size() > 1) {
                if (!p.party.list.get(0).equals(p)) {
                    p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                    return;
                }
            }
            List<Player> validParty = new ArrayList<>();
            validParty.add(p);
            if (p.party != null && p.party.list != null && p.party.list.size() > 1) {
                for (int i = 0; i < p.party.list.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                        validParty.add(p0);
                    }
                }
            }
            if (validParty.size() <= 1) {
                p.data_yesno = new int[]{67};
                p.setyesNoDialog(new model.YesNoDialog(p, 67, "Thông báo",
                        "Phó bản bộ đôi Mr Candle mỗi lần đi tốn 2 chìa khóa, xác nhận vào?",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            } else {
                p.data_yesno = new int[]{67};
                p.tableTickOption = new MrCandleTick(p);
                p.tableTickOption.listP = validParty;
                p.tableTickOption.list_check = new byte[validParty.size()];
                p.tableTickOption.list_check[0] = 1;
                for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
                    p.tableTickOption.list_check[i] = 0;
                }
                p.tableTickOption.show("Bộ đôi Mr Candle");
            }
        } else if (index == 2) {
            if (p.party != null && p.party.list != null && p.party.list.size() > 1) {
                if (!p.party.list.get(0).name.equals(p.name)) {
                    p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                    return;
                }
            }
            List<Player> validParty = new ArrayList<>();
            validParty.add(p);
            if (p.party != null && p.party.list != null && p.party.list.size() > 1) {
                for (int i = 0; i < p.party.list.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                        validParty.add(p0);
                    }
                }
            }
            if (validParty.size() <= 1) {
                p.setyesNoDialog(new model.YesNoDialog(p, 56, "Thông báo",
                        "Phó bản thử thách vệ thần mỗi lần đi tốn 1 chìa khóa, xác nhận vào?",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            } else {
                p.tableTickOption = new functions.TTVTTick(p);
                p.tableTickOption.listP = validParty;
                p.tableTickOption.list_check = new byte[validParty.size()];
                p.tableTickOption.list_check[0] = 1;
                for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
                    p.tableTickOption.list_check[i] = 0;
                }
                p.tableTickOption.show("Thử Thách Vệ Thần");
            }
        } else if (index == 1) {
            if (p.clan == null) {
                p.getService().send_box_ThongBao_OK("Bạn cần gia nhập băng để tham gia phó bản băng!");
                return;
            }

            // 1. Kiểm tra nếu trận chiến của clan đang diễn ra -> Cho người chơi vào thẳng trận
            if (p.clan.map_create != null && p.clan.map_create.map_little_garden != null && !p.clan.map_create.map_little_garden.is_finish) {
                int team = (p.clan.map_create.map_little_garden.clan1 != null
                        && p.clan.map_create.map_little_garden.clan1.equals(p.clan)) ? 4 : 5;
                p.type_pk = (byte) team;
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{p.clan.map_create};
                vgo.xnew = (short) (team == 4 ? 350 : 1400);
                vgo.ynew = 260;
                p.goto_map(vgo);
                p.getService().send_box_ThongBao_OK("Bạn đã tiến vào trận chiến khổng lồ cùng Băng của mình!");
                return;
            }

            // 2. Kiểm tra nếu clan đang nằm trong hàng chờ ghép trận
            if (TranChienKhongLo.isClanInQueue(p.clan)) {
                p.getService().send_box_ThongBao_OK("Băng của bạn đang trong hàng chờ ghép trận, vui lòng đợi trong giây lát...");
                return;
            }

            // 3. Kiểm tra phó bản có đang mở không
            if (!TranChienKhongLo.isOpen()) {
                p.getService().send_box_ThongBao_OK("Trận Chiến Khổng Lồ hiện chưa mở cửa!\n" + activities.TimedDungeonManager.gI().getDungeonStatusMessage("TRAN_CHIEN_KHONG_LO"));
                return;
            }

            // 4. Mở đăng ký ghép trận (Hỗ trợ từ 1 người solo đến toàn bộ thành viên cùng map)
            p.tableTickOption = new PhoBanKhongLoTick(p);
            p.tableTickOption.listP = new ArrayList<>();
            p.tableTickOption.listP.add(p);

            // Tìm tất cả thành viên cùng clan đang có mặt ở cùng map
            if (p.clan.members != null) {
                for (int i = 0; i < p.clan.members.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                        p.tableTickOption.listP.add(p0);
                    }
                }
            }

            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];
            p.tableTickOption.list_check[0] = 1; // Người mở luôn sẵn sàng
            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
                p.tableTickOption.list_check[i] = 0;
            }

            p.tableTickOption.show("Phó bản khổng lồ");
        }
    }
}
