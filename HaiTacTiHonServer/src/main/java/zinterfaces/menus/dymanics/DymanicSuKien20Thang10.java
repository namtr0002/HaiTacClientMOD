package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKien20Thang10 — Gắn option "Sự kiện 20 Tháng 10" vào NPC Rubin (-100) và NPC Robin (-1041).
 * Chỉ hiển thị khi Sự kiện 20 Tháng 10 đang active (ZEVENT_ID == ID_SUKIEN_20THANG10_2025 = 11).
 */
public class DymanicSuKien20Thang10 implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1041};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện 20 Tháng 10"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_20THANG10_2025;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKien20Thang10.gI().sendMenu(p, npcId);
    }
}
