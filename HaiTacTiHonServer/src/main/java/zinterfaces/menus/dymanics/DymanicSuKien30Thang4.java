package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKien30Thang4 — Gắn option "Sự kiện 30 Tháng 4" vào NPC Rubin (-100) và NPC Garp (-1007).
 * Chỉ hiển thị khi Sự kiện 30 Tháng 4 đang active (ZEVENT_ID == ID_SUKIEN_30THANG41THANG5_2026 = 6).
 */
public class DymanicSuKien30Thang4 implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1007};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện 30 Tháng 4"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_30THANG41THANG5_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKien30Thang4.gI().sendMenu(p, npcId);
    }
}
