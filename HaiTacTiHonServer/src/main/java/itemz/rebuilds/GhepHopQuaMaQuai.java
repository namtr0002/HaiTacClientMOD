package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepHopQuaMaQuai extends AbsCheTao {

    public GhepHopQuaMaQuai() {
        super(416, (byte) 4, 0, 30);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(414, (byte) 4, 1); // Huy hiệu sói ma
        addIngredient(415, (byte) 4, 1); // Huy hiệu Dracula
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{414, 415};
        short[] ingQtys = new short[]{1, 1};
        byte[] ingCats = new byte[]{4, 4};
        short[] ingIcons = new short[2];
        for (int i = 0; i < ingIds.length; i++) {
            ItemTemplate4 it = ItemTemplate4.get_it_by_id(ingIds[i]);
            ingIcons[i] = it != null ? it.icon : 0;
        }
        short resultIcon = 0;
        ItemTemplate4 resIt = ItemTemplate4.get_it_by_id(resultId);
        if (resIt != null) {
            resultIcon = resIt.icon;
        }
        p.getService().sendUpgradeDevilCraftPanel(
            "Ghép Hộp Quà Ma Quái",
            (byte) ingIds.length,
            ingIds,
            ingQtys,
            ingCats,
            ingIcons,
            costBeri,
            (short) costRuby,
            costExtol,
            (short) resultId,
            (short) 1,
            resultCat,
            resultIcon,
            (byte) successRate
        );
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success) {
            p.update_pointEvent1(quantity * 50);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
    }
}
