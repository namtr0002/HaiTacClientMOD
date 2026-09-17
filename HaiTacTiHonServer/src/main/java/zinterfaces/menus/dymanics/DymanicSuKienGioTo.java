package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienGioTo — Gắn option "Sự kiện Giỗ Tổ" vào NPC Rubin (-100) và NPC Giỗ Tổ (-975).
 * Chỉ hiển thị khi Sự kiện Giỗ Tổ đang active (ZEVENT_ID == ID_SUKIEN_GIOTOHUNGVUONG_2026 = 3).
 */
public class DymanicSuKienGioTo implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -975};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Giỗ Tổ"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_GIOTOHUNGVUONG_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienGioTo.gI().sendMenu(p, npcId);
    }
}
