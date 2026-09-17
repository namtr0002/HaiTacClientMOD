package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepGioTraiCay extends AbsCheTao {

    public GhepGioTraiCay() {
        // Result ID: 396, Category: 4 (Item), Beri cost: 1,000, Ruby cost: 0
        super(396, (byte) 4, 1_000, 0);
        this.costExtol = 0;
        this.successRate = 100;
        // Ingredients: 1x of items 391 through 395
        addIngredient(391, (byte) 4, 1);
        addIngredient(392, (byte) 4, 1);
        addIngredient(393, (byte) 4, 1);
        addIngredient(394, (byte) 4, 1);
        addIngredient(395, (byte) 4, 1);
    }

    public static void showTable(model.Player p) throws java.io.IOException {
        zabstracts.AbsCheTao.showTable(p, GhepGioTraiCay.class);
    }

    @Override
    protected void afterCraft(model.Player p, int quantity, boolean success) {
        if (success) {
            p.update_pointEvent2(quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(9);
    }
}
