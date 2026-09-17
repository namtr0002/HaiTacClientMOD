package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienNoel — Gắn option "Sự kiện Noel" vào NPC Rubin (-100) và Cây Thông Noel (-1019).
 * Chỉ hiển thị khi Sự kiện Noel đang active (ZEVENT_ID == ID_SUKIEN_NOEL_2025 = 12 / 12346).
 */
public class DymanicSuKienNoel implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1019};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Noel"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_NOEL_2025;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienNoel.gI().sendMenu(p, npcId);
    }
}
