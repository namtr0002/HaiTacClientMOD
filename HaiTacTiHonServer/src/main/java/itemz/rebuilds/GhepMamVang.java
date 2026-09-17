package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepMamVang extends AbsCheTao {

    public GhepMamVang() {
        super(838, (byte) 4, 0, 25); // Kết quả: 838 = Mâm Vàng; giá: 25 Ruby
        this.costExtol = 25_000;     // + 25.000 Extol
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(834, (byte) 4, 5); // 834 = Cựa Gà
        addIngredient(835, (byte) 4, 5); // 835 = Hồng Mao
        addIngredient(836, (byte) 4, 5); // 836 = Ngà Voi
        addIngredient(837, (byte) 4, 1); // 837 = Mâm Bạc
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{834, 835, 836, 837};
        short[] ingQtys = new short[]{5, 5, 5, 1};
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
            "Dâng Mâm Vàng Cúng Tổ",
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
        return event.EventManager.isActive(event.SuKienGioTo.ID);
    }
}
