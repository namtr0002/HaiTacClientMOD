package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import network.Message;
import java.io.IOException;

public class GhepRuongWorldcup extends AbsCheTao {

    public GhepRuongWorldcup() {
        // Result ID: 609, Category: 4 (Item), Beri cost: 50,000, Ruby cost: 10
        super(609, (byte) 4, 50_000, 10);
        this.successRate = 100;
        // Ingredients: 10x 599, 5x 597, 2x 598
        addIngredient(599, (byte) 4, 10);
        addIngredient(597, (byte) 4, 5);
        addIngredient(598, (byte) 4, 2);
    }

    @Override
    public void show_table(Player p) throws IOException {
        p.getService().sendUpgradeDevilCraftPanel(
            "Ghép Rương Worldcup",
            (byte) 3,
            new short[]{599, 597, 598},
            new short[]{10, 5, 2},
            new byte[]{4, 4, 4},
            new short[]{570, 567, 568},
            50_000,
            (short) 10,
            0,
            (short) 609,
            (short) 1,
            (byte) 4,
            (short) 573,
            (byte) 100
        );
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.update_pointEvent1(quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(13);
    }
}

