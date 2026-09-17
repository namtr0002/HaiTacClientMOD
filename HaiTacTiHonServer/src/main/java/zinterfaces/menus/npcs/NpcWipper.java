package zinterfaces.menus.npcs;

import model.Player;
import zinterfaces.iNpc;
import map.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Npc;
import itemz.UpgradeDial;
import itemz.rebuilds.GhepSachCongThucDial;
import itemz.rebuilds.GhepVoOcDial;
import itemz.rebuilds.CheTaoDial;
import itemz.rebuilds.NangCapDucLoDial;

public class NpcWipper implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-140, 980};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -140;
        p.menus.clear();
        p.menus.add(new model.Menu("Chế tạo DIAL", (short) -1, () -> {
            try {
                sendSubMenuCheTao(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Thử thách vệ thần", (short) -1, () -> {
            try {
                handleThuThachVeThan(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.getService().openDynamicMenu(type, "WIPPER", p.menus);
    }

    private void sendSubMenuCheTao(Player p) throws IOException {
        p.menus.clear();
        p.menus.add(new model.Menu("Ghép sách công thức", (short) -1, () -> {
            try {
                GhepSachCongThucDial.showTable(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Ghép vỏ ốc", (short) -1, () -> {
            try {
                GhepVoOcDial.showTable(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Chế tạo dial", (short) -1, () -> {
            try {
                CheTaoDial.showTable(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Cường hóa dial", (short) -1, () -> {
            try {
                UpgradeDial.show_table(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Đục lỗ dial", (short) -1, () -> {
            try {
                NangCapDucLoDial.getInstance().showTable(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.getService().openDynamicMenu(980, "WIPPER", p.menus);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (menuId == 980) {
            handleSubMenu(p, index);
            return;
        }
        handleMenu(p, index);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        if (currentId == 980) {
            handleSubMenu(p, index);
            return;
        }

        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
            return;
        }

        if (index == 0) {
            sendSubMenuCheTao(p);
        } else if (index == 1) {
            handleThuThachVeThan(p);
        }
    }

    private void handleSubMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                GhepSachCongThucDial.showTable(p);
                break;
            case 1:
                GhepVoOcDial.showTable(p);
                break;
            case 2:
                CheTaoDial.showTable(p);
                break;
            case 3:
                UpgradeDial.show_table(p);
                break;
            case 4:
                NangCapDucLoDial.getInstance().showTable(p);
                break;
        }
    }

    private void handleThuThachVeThan(Player p) throws IOException {
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
    }
}
