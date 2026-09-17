package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienTetThieuNhi — Gắn option "Sự kiện Tết Thiếu Nhi" vào NPC Rubin (-100) và NPC Bé Pudding (-1031).
 * Chỉ hiển thị khi Sự kiện Tết Thiếu Nhi đang active (ZEVENT_ID == ID_SUKIEN_TETTHIEUNHI_2026 = 5).
 */
public class DymanicSuKienTetThieuNhi implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1031};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Tết Thiếu Nhi"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_TETTHIEUNHI_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienTetThieuNhi.gI().sendMenu(p, npcId);
    }
}
