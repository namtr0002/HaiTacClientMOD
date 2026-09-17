package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepKeoBongGonVip extends AbsCheTao {

    public GhepKeoBongGonVip() {
        super(406, (byte) 4, 0, 20);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(401, (byte) 4, 10); // Đường phèn
        addIngredient(402, (byte) 4, 10); // Bột socola
        addIngredient(403, (byte) 4, 10); // Bơ sữa
        addIngredient(404, (byte) 4, 2);  // Túi kẹo ngọt
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{401, 402, 403, 404};
        short[] ingQtys = new short[]{10, 10, 10, 2};
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
            "Làm Kẹo Bông Gòn VIP",
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
            p.update_pointEvent1(quantity * 20);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienTetThieuNhi.ID);
    }
}
