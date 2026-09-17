package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepKimCuongDo803 extends AbsCheTao {

    public GhepKimCuongDo803() {
        super(803, (byte) 4, 10_000, 0);
        this.successRate = 100;
        addIngredient(797, (byte) 4, 5); // Quả bóng bạc
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{797};
        short[] ingQtys = new short[]{5};
        byte[] ingCats = new byte[]{4};
        short[] ingIcons = new short[1];
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
            "Gói Hộp Kim Cương Đỏ (Thường)",
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
            p.update_pointEvent1(quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_30THANG41THANG5_2026);
    }
}
