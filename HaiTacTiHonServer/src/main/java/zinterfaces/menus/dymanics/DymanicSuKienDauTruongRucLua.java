package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DymanicSuKienDauTruongRucLua — Gắn option "Đấu Trường Rực Lửa" vào NPC Rubin (-100) và các NPC Bóng Đá (-876, -878, -879).
 * Chỉ hiển thị khi Sự kiện Đấu Trường Rực Lửa đang active (ZEVENT_ID == ID_SUKIEN_DAU_TRUONG_RUC_LUA_2026 = 13).
 */
public class DymanicSuKienDauTruongRucLua implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-100, -876, -878, -879};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Đấu Trường Rực Lửa"};
    }

    @Override
    public int getEventId() {
        return Event.ID_SUKIEN_DAU_TRUONG_RUC_LUA_2026;
    }

    @Override
    public Position getPosition() {
        return Position.TOP;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        event.SuKienDauTruongRucLua2026.gI().sendMenu(p, npcId);
    }
}
