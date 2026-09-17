package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienVuLan — Gắn option "Sự kiện Vu Lan" vào NPC Rubin (-100) và Boa Hancock (-1055).
 * Chỉ hiển thị khi Sự kiện Vu Lan đang active (ZEVENT_ID == ID_SUKIEN_VULAN_2026 = 18).
 */
public class DymanicSuKienVuLan implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1055};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Vu Lan Báo Hiếu"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_VULAN_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienVuLan.gI().sendMenu(p, npcId);
    }
}
