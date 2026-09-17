package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepGioHoa extends AbsCheTao {

    public GhepGioHoa() {
        super(820, (byte) 4, 50_000, 10);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(815, (byte) 4, 1); // Bó hoa đỏ
        addIngredient(816, (byte) 4, 1); // Bó hoa xanh
        addIngredient(817, (byte) 4, 1); // Bó hoa vàng
        addIngredient(819, (byte) 4, 1); // Giấy màu
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{815, 816, 817, 819};
        short[] ingQtys = new short[]{1, 1, 1, 1};
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
            "Làm Giỏ Hoa",
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
        return event.EventManager.isActive(event.Event.ID_SUKIEN_8THANG3_2026);
    }
}
