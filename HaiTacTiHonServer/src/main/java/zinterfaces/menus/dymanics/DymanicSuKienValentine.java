package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienValentine — Gắn option "Sự kiện Valentine" vào NPC Rubin (-100) và Sanji Tình Yêu (-1008).
 * Chỉ hiển thị khi Sự kiện Valentine đang active (ZEVENT_ID == ID_SUKIEN_VALENTINE = 15).
 */
public class DymanicSuKienValentine implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1008};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Valentine"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_VALENTINE;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienValentine.gI().sendMenu(p, npcId);
    }
}
