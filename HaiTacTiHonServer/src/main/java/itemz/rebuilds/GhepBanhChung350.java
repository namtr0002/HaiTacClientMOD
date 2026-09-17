package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepBanhChung350 extends AbsCheTao {

    public GhepBanhChung350() {
        super(350, (byte) 4, 10_000, 50);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(353, (byte) 4, 5); // Gạo nếp
        addIngredient(352, (byte) 4, 5); // Đậu xanh
        addIngredient(351, (byte) 4, 5); // Lá dong
        addIngredient(354, (byte) 4, 5); // Thịt heo
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{353, 352, 351, 354};
        short[] ingQtys = new short[]{5, 5, 5, 5};
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
            "Làm Bánh Chưng",
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
            p.update_pointEvent2(quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienTet.ID_EVENT_TET);
    }
}
