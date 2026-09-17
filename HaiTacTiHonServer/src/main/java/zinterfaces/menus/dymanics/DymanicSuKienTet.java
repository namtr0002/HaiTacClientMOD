package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienTet — Gắn option "Sự kiện Tết" vào NPC Rubin (-100) và các NPC Tết (-1011, -1012, -1013).
 * Chỉ hiển thị khi Sự kiện Tết đang active (ZEVENT_ID == ID_SUKIEN_TETAMLICH_2026 = 2).
 */
public class DymanicSuKienTet implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1011, -1012, -1013};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Tết"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_TETAMLICH_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienTet.gI().sendMenu(p, npcId);
    }
}
