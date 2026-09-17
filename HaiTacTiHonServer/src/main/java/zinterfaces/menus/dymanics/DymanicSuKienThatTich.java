package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienThatTich — Gắn option "Sự kiện Thất Tịch" vào NPC Rubin (-100) và Đầu Bếp Sanji (-1048).
 * Chỉ hiển thị khi Sự kiện Thất Tịch đang active (ID_SUKIEN_THATTICH = 19).
 */
public class DymanicSuKienThatTich implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1048};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Thất Tịch"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_THATTICH;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienThatTich.gI().sendMenu(p, npcId);
    }
}
