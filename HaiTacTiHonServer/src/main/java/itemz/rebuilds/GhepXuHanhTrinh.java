package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepXuHanhTrinh extends AbsCheTao {

    public GhepXuHanhTrinh() {
        // Result ID: 580, Category: 4 (Item), Beri cost: 50,000, Ruby cost: 10
        super(580, (byte) 4, 50_000, 10);
        this.costExtol = 10_000;
        this.successRate = 10;
        // Ingredients: 100x of item 579
        addIngredient(579, (byte) 4, 100);
    }

    public static void showTable(model.Player p) throws java.io.IOException {
        zabstracts.AbsCheTao.showTable(p, GhepXuHanhTrinh.class);
    }

    @Override
    protected boolean isEventActive() {
        return true; // Always active
    }
}
