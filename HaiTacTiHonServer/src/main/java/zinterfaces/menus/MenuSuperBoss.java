package zinterfaces.menus;

import model.Player;
import zinterfaces.iNpc;
import map.Npc;
import boss.SuperBossManager;
import java.io.IOException;

/**
 * MenuSuperBoss — Dynamic menu handler cho Siêu Trùm (ID 951), Sub-menu từng boss (ID 9510..9519), và Top Dame (ID 120)
 */
public class MenuSuperBoss implements iNpc {

    private static final MenuSuperBoss instance = new MenuSuperBoss();

    public static MenuSuperBoss gI() {
        return instance;
    }

    @Override
    public short[] getId() {
        return new short[]{
            951, 120,
            9510, 9511, 9512, 9513, 9514, 9515, 9516, 9517, 9518, 9519
        };
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        SuperBossManager.sendSuperBossMenu(p);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        int menuId = p.currentNpcId != 0 ? p.currentNpcId : 951;
        handleMenu(p, menuId, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        SuperBossManager.handleMenu(p, menuId, index);
    }
}
