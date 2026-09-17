package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKien8Thang3 — Gắn option "Sự kiện 8 Tháng 3" vào NPC Rubin (-100) và NPC Boa Hancock (-935).
 * Chỉ hiển thị khi Sự kiện 8 Tháng 3 đang active (ZEVENT_ID == ID_SUKIEN_8THANG3_2026 = 10).
 */
public class DymanicSuKien8Thang3 implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -935};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện 8 Tháng 3"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_8THANG3_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKien8Thang3.gI().sendMenu(p, npcId);
    }
}
