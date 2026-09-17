package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienHe — Gắn option "Sự kiện Hè" vào NPC Rubin (-100) và NPC Sự Kiện Hè (-1020).
 * Chỉ hiển thị khi Sự kiện Hè đang active (ZEVENT_ID == ID_SUKIEN_HE_2026 = 1).
 */
public class DymanicSuKienHe implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1020};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Hè"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_HE_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienHe.gI().sendMenu(p, npcId);
    }
}
