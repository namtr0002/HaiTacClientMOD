package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class XayCauOThuoc extends AbsCheTao {

    public XayCauOThuoc() {
        super(570, (byte) 4, 20_000, 0);
        this.successRate = 100;
        this.payChoice = false;
        addIngredient(571, (byte) 4, 10); // Gỗ
        addIngredient(572, (byte) 4, 10); // Đá
        addIngredient(573, (byte) 4, 10); // Lông chim thước
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{571, 572, 573};
        short[] ingQtys = new short[]{10, 10, 10};
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
            "Xây Cầu Ô Thước",
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
        return event.EventManager.isActive(event.Event.ID_SUKIEN_THATTICH);
    }
}
