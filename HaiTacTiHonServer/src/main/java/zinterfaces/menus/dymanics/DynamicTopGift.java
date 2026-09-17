package zinterfaces.menus.dymanics;

import model.Player;
import model.Menu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import zinterfaces.iMenuDymanic;

/**
 * DynamicTopGift — injects the "Hướng Dẫn Đua Top" option into Trưởng Làng NPCs.
 */
public class DynamicTopGift implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        return new short[]{-145, -122, -118, -103, -87, -74, -67, -45, -31, -21, -13, -1}; // Trưởng Làng IDs
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Hướng Dẫn Đua Top"};
    }

    @Override
    public Position getPosition() {
        return Position.BOTTOM; // Add to the bottom of the list
    }

    @Override
    public boolean shouldShow(Player p) {
        return true;
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        if (p == null) return;
        short targetNpc = (short) (p.currentNpcId != 0 ? p.currentNpcId : npcId);
        List<Menu> subMenus = new ArrayList<>();
        subMenus.add(new Menu("Top Cao Thủ", (short) 101, () -> {
            try { p.getService().Help_From_Server(targetNpc, rank.TopCaoThu.getGuideInfo()); } catch (Exception ignored) {}
        }));
        subMenus.add(new Menu("Top Thách Đấu PvP", (short) 101, () -> {
            try { p.getService().Help_From_Server(targetNpc, rank.TopPVP.getGuideInfo()); } catch (Exception ignored) {}
        }));
        subMenus.add(new Menu("Top Lệnh Truy Nã", (short) 101, () -> {
            try { p.getService().Help_From_Server(targetNpc, rank.TopWanted.getGuideInfo()); } catch (Exception ignored) {}
        }));
        p.getService().openDynamicMenu((short) 989, "Đua Top Máy Chủ", subMenus);
    }
}

