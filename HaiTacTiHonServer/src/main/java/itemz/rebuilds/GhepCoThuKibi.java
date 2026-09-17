package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepCoThuKibi extends AbsCheTao {

    public GhepCoThuKibi() {
        super(373, (byte) 4, 5_000, 0);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(371, (byte) 4, 10); // Giấy da cổ
        addIngredient(372, (byte) 4, 10); // Mực ma thuật
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{371, 372};
        short[] ingQtys = new short[]{10, 10};
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
            "Ghép Cổ Thư Kibi",
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
        return event.EventManager.isActive(event.SuKienKinhKibi.ID);
    }
}
