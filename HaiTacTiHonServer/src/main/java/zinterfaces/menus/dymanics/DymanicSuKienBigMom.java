package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienBigMom — Gắn option "Sự kiện BigMom" vào NPC Rubin (-100), Đầu Bếp Sanji (-1048), và Pudding (-154).
 * Chỉ hiển thị khi Sự kiện Big Mom đang active (ZEVENT_ID == ID_SUKIEN_BIGMOM = 16).
 */
public class DymanicSuKienBigMom implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -1048, -154};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Sự kiện Tiệc Bánh Ngọt BigMom"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_BIGMOM;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienBigMom.gI().sendMenu(p, npcId);
    }
}
