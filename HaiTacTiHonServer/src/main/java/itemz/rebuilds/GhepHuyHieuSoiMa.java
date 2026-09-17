package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;
import template.ItemTemplate4;

public class GhepHuyHieuSoiMa extends AbsCheTao {

    public GhepHuyHieuSoiMa() {
        super(414, (byte) 4, 20_000, 0);
        this.successRate = 100;
        this.payChoice = true;
        addIngredient(411, (byte) 4, 10); // Răng sói ma
        addIngredient(412, (byte) 4, 10); // Móng vuốt quỷ
        addIngredient(413, (byte) 4, 10); // Mảnh bí ngô
    }

    @Override
    public void show_table(Player p) throws IOException {
        short[] ingIds = new short[]{411, 412, 413};
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
            "Ghép Huy Hiệu Sói Ma",
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
            p.update_pointEvent1(quantity * 10);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
    }
}
