package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienWano — Gắn option "Sự kiện Wano Quốc" vào NPC Rubin (-100) và O-Tsuru (-888, -889).
 * Chỉ hiển thị khi Sự kiện Wano đang active (ZEVENT_ID == ID_SUKIEN_WANO_2026 = 17).
 */
public class DymanicSuKienWano implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -888, -889};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Wano Quốc"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_WANO_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienWano.gI().sendMenu(p, npcId);
    }
}
