package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepRuongChienThang807 extends AbsCheTao {

    public GhepRuongChienThang807() {
        super(887, (byte) 4, 50_000, 10);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(803, (byte) 4, 3); // Hộp kim cương đỏ
        addIngredient(804, (byte) 4, 3); // Hộp kim cương tím
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{803, 804};
        short[] ingQtys = new short[]{3, 3};
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
            "Chế Tạo Rương Chiến Thắng 30/4",
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
        return event.EventManager.isActive(event.Event.ID_SUKIEN_30THANG41THANG5_2026);
    }
}
