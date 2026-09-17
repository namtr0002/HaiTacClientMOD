package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienHalloween — Gắn option "Sự kiện Halloween" vào NPC Rubin (-100) và NPC Bí Ngô Ma (-1021).
 * Chỉ hiển thị khi Sự kiện Halloween đang active (ZEVENT_ID == ID_SUKIEN_HALLOWEEN_2025 = 7).
 */
public class DymanicSuKienHalloween implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1021};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Halloween"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_HALLOWEEN_2025;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienHalloween.gI().sendMenu(p, npcId);
    }
}
