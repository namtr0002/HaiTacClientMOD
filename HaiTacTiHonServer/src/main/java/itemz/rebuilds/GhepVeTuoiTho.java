package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import network.Message;
import java.io.IOException;

public class GhepVeTuoiTho extends AbsCheTao {

    public GhepVeTuoiTho() {
        // Result ID: 192, Category: 4 (Item), Beri cost: 35,000, Ruby cost: 25
        super(192, (byte) 4, 35_000, 25);
        this.costExtol = 20_000;
        this.successRate = 100;
        // Ingredients: 5x of each from 183 to 187
        addIngredient(183, (byte) 4, 5);
        addIngredient(184, (byte) 4, 5);
        addIngredient(185, (byte) 4, 5);
        addIngredient(186, (byte) 4, 5);
        addIngredient(187, (byte) 4, 5);
    }

    @Override
    public void show_table(Player p) throws IOException {
        p.getService().sendUpgradeDevilCraftPanel(
            "Ghép vé về tuổi thơ",
            (byte) 5,
            new short[]{183, 184, 185, 186, 187},
            new short[]{5, 5, 5, 5, 5},
            new byte[]{4, 4, 4, 4, 4},
            new short[]{138, 139, 140, 141, 142},
            35_000,
            (short) 25,
            20_000,
            (short) 192,
            (short) 1,
            (byte) 4,
            (short) 147,
            (byte) 100
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
        return event.EventManager.isActive(event.SuKienHe.ID);
    }
}
