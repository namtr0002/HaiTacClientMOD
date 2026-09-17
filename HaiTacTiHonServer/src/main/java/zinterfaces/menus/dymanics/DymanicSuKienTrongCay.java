package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienTrongCay — Gắn option "Sự kiện Trồng Cây" vào NPC Rubin (-100) và NPH Trồng Cây (-1023).
 * Chỉ hiển thị khi Sự kiện Trồng Cây đang active (ZEVENT_ID == ID_SUKIEN_TRONGCAY = 9).
 */
public class DymanicSuKienTrongCay implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1023};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Trồng Cây"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_TRONGCAY;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienTrongCay.gI().sendMenu(p, npcId);
    }
}
