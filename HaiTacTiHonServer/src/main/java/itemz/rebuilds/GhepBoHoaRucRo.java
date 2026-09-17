package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;
import event.Event;

/**
 * GhepBoHoaRucRo — Ghép Bó Hoa Rực Rỡ mùa Vu Lan 2026
 * Công thức: 2 Hoa Hồng Trắng (888) + 1 Hoa Hồng Đỏ (889) + 5 Ruby -> 1 Bó Hoa Rực Rỡ (890)
 */
public class GhepBoHoaRucRo extends AbsCheTao {

    public GhepBoHoaRucRo() {
        super(890, (byte) 4, 0, 5); // Result: 890 (Bó hoa rực rỡ), cost: 5 Ruby, 0 Beri
        this.successRate = 100;
        addIngredient(888, (byte) 4, 2); // 2 Hoa hồng trắng
        addIngredient(889, (byte) 4, 1); // 1 Hoa hồng đỏ
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{888, 889};
        short[] ingQtys = new short[]{2, 1};
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
            "Tạo Bó Hoa Rực Rỡ",
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
            if (event.EventManager.isActive(event.SuKien20Thang11.ID)) {
                p.update_pointEvent1(quantity);
            } else {
                p.update_pointEvent2(quantity);
            }
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKien20Thang11.ID) || event.EventManager.isActive(Event.ID_SUKIEN_VULAN_2026);
    }
}
