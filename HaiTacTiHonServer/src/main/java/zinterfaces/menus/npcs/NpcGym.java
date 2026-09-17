package zinterfaces.menus.npcs;

import model.Player;
import zinterfaces.iMenuDymanic;
import zinterfaces.iNpc;
import java.io.IOException;
import map.Npc;
import map.Zone;
import map.zones.DauTruongTuDo;
import activities.DungeonGiftMenu;

public class NpcGym implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-77};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -77;
        p.menus.clear();
        p.menus.add(new model.Menu("Tham gia Đấu Trường", (short) 146, () -> { try { handleMenu(p, 0); } catch (IOException e) {} }));
        p.menus.add(new model.Menu("Nhận thưởng Đấu Trường", (short) 138, () -> { try { DungeonGiftMenu.openMenuByType(p, "DAU_TRUONG_TU_DO"); } catch (IOException e) {} }));
        p.menus.add(new model.Menu("BXH Đấu trường", (short) 148, () -> { try { handleMenu(p, 1); } catch (IOException e) {} }));
        p.menus.add(new model.Menu("Hướng dẫn", (short) 110, () -> { try { handleMenu(p, 2); } catch (IOException e) {} }));
        p.getService().openDynamicMenu(type, "Ms. Gym", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                if (!DauTruongTuDo.IsOpen()) {
                    p.getService().send_box_ThongBao_OK("Đấu Trường Tự Do hiện chưa mở cửa!\n" + activities.TimedDungeonManager.gI().getDungeonStatusMessage("DAU_TRUONG_TU_DO"));
                    return;
                }
                if (p.level < 50) {
                    p.getService().send_box_ThongBao_OK("Đấu Trường Tự Do chỉ dành cho hải tặc từ Cấp 50 (5x) trở lên!");
                    return;
                }
                int targetMapId = DauTruongTuDo.getMapIdByLevel(p.level);
                Zone[] zones = Zone.getMapByID(targetMapId);
                if (zones != null && zones.length > 0) {
                    // Chọn khu 0 hoặc khu 1 có ít người hơn để cân bằng
                    Zone targetZone = zones[0];
                    if (zones.length > 1 && zones[1] != null && zones[1].players.size() < zones[0].players.size()) {
                        targetZone = zones[1];
                    }
                    map.Vgo vgo = new map.Vgo();
                    vgo.map_go = new Zone[]{targetZone};
                    vgo.xnew = (short) core.ZUtil.random(150, Math.max(200, targetZone.template.maxW - 150));
                    vgo.ynew = 260;
                    p.goto_map(vgo);
                    p.getService().send_box_ThongBao_OK("Bạn đã tiến vào " + targetZone.template.name + " (Khu " + (targetZone.zone_id + 1) + ") phù hợp cấp độ " + p.level + "!");
                } else {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy khu vực đấu trường phù hợp!");
                }
                break;
            }
            case 1: {
                DauTruongTuDo.SendBxh(p);
                break;
            }
            case 2: {
                String txt = "Đấu trường tự do\n"
                        + "Nơi các hải tặc so tài cao thấp theo phân cấp cấp độ.\b"
                        + "Cùng Băng/Nhóm sẽ mang cùng màu cờ, hãy phối hợp để chiến thắng đối thủ!\b"
                        + "Hạ gục đối thủ để tích lũy điểm và thăng hạng trên BXH.";
                p.getService().Help_From_Server(this.getId()[0], txt);
                break;
            }
        }
    }
}
