package zinterfaces.menus;

import model.Player;
import core.MenuController;
import network.Service;
import core.ZUtil;
import event.SuKienHalloween;
import zinterfaces.iMenu;
import rank.TopHang;
import template.GiftBox;
import template.ItemTemplate4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.InputDialog;

public class MenuSuKienHalloween implements iMenu {
    @Override
    public short[] getId() {
        return new short[]{7}; // Event.ID_SUKIEN_HALLOWEEN_2025
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == 7 || idNPC == -1021) {
            SuKienHalloween.gI().processMenu(p, index);
        }
    }
}
