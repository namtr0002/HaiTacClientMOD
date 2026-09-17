package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienTrungThu — Gắn option "Sự kiện Trung Thu" vào NPC Rubin (-100) và Chị Hằng (-877, -1018).
 * Chỉ hiển thị khi event Trung Thu đang hoạt động (ZEVENT_ID == ID_SUKIEN_TRUNGTHU_2025 = 8).
 */
public class DymanicSuKienTrungThu implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -877, -1018};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Trung Thu"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_TRUNGTHU_2025;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienTrungThu.gI().sendMenu(p, npcId);
    }
}
