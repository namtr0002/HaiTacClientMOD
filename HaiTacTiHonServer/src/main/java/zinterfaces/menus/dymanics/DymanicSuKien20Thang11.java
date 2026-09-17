package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKien20Thang11 — Gắn option "Sự kiện 20.11" vào NPC Rubin (-100) và NPC Boa Hancock (-1051), Rayleigh (-1052).
 * Chỉ hiển thị khi event 20.11 đang hoạt động (ZEVENT_ID == ID_SUKIEN_20THANG11_REAL = 14).
 */
public class DymanicSuKien20Thang11 implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1051, -1052};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện 20.11"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_20THANG11_REAL;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKien20Thang11.gI().sendMenu(p, npcId);
    }
}
