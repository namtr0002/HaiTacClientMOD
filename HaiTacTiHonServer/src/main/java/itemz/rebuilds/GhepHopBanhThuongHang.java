package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepHopBanhThuongHang extends AbsCheTao {

    public GhepHopBanhThuongHang() {
        super(576, (byte) 4, 20_000, 0);
        this.successRate = 100;
        this.payChoice = false;
        addIngredient(575, (byte) 4, 5); // Giấy gói quà
        addIngredient(569, (byte) 4, 2); // Chè đậu đỏ
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{575, 569};
        short[] ingQtys = new short[]{5, 2};
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
            "Gói Hộp Bánh Thượng Hạng",
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
        return event.EventManager.isActive(event.Event.ID_SUKIEN_THATTICH);
    }
}
