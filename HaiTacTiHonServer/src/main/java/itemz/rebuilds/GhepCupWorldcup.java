package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepCupWorldcup extends AbsCheTao {

    public GhepCupWorldcup() {
        // Result ID: 610, Category: 4 (Item), Beri cost: 100,000, Ruby cost: 20
        super(610, (byte) 4, 100_000, 20);
        this.successRate = 100;
        // Ingredients: 1x 609 (Rương Worldcup), 1x 799 (Quả Bóng Vàng)
        addIngredient(609, (byte) 4, 1);
        addIngredient(799, (byte) 4, 1);
    }

    @Override
    public void show_table(Player p) throws IOException {
        p.getService().sendUpgradeDevilCraftPanel(
            "Ghép Cúp Worldcup",
            (byte) 2,
            new short[]{609, 799},
            new short[]{1, 1},
            new byte[]{4, 4},
            new short[]{573, 575},
            100_000,
            (short) 20,
            0,
            (short) 610,
            (short) 1,
            (byte) 4,
            (short) 574,
            (byte) 100
        );
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.update_pointEvent1(quantity * 5); // 5 điểm cho mỗi Cúp World Cup
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(13);
    }
}
