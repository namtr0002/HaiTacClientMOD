package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepHuyHieuDracula extends AbsCheTao {

    public GhepHuyHieuDracula() {
        super(415, (byte) 4, 0, 50);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(411, (byte) 4, 20); // Răng sói ma
        addIngredient(412, (byte) 4, 20); // Móng vuốt quỷ
        addIngredient(413, (byte) 4, 20); // Mảnh bí ngô
        addIngredient(414, (byte) 4, 1);  // Huy hiệu sói ma
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{411, 412, 413, 414};
        short[] ingQtys = new short[]{20, 20, 20, 1};
        byte[] ingCats = new byte[]{4, 4, 4, 4};
        short[] ingIcons = new short[4];
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
            "Ghép Huy Hiệu Dracula",
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
            p.update_pointEvent1(quantity * 30);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
    }
}
