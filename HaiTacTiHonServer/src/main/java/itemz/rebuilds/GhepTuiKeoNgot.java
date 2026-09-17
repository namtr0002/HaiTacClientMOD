package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepTuiKeoNgot extends AbsCheTao {

    public GhepTuiKeoNgot() {
        super(404, (byte) 4, 10_000, 0);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(401, (byte) 4, 5); // Đường phèn
        addIngredient(402, (byte) 4, 5); // Bột socola
        addIngredient(403, (byte) 4, 5); // Bơ sữa
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{401, 402, 403};
        short[] ingQtys = new short[]{5, 5, 5};
        byte[] ingCats = new byte[]{4, 4, 4};
        short[] ingIcons = new short[3];
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
            "Làm Túi Kẹo Ngọt",
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
            p.update_pointEvent1(quantity * 5);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienTetThieuNhi.ID);
    }
}
