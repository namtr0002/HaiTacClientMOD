package zinterfaces.menus.dymanics;

import model.Player;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienKinhKibi — Gắn option "Sự kiện Kinh Kì Bí" vào NPC Rubin (-100) và NPC Cổ Thư (-1022).
 * Chỉ hiển thị khi Sự kiện Kinh Kì Bí đang active (ZEVENT_ID == 4).
 */
public class DymanicSuKienKinhKibi implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1022};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Kinh Kì Bí"};
    }

    @Override
    public int getEventId() {
        return event.Event.ID_SUKIEN_TETDUONGLICH_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienKinhKibi.gI().sendMenu(p, npcId);
    }
}
