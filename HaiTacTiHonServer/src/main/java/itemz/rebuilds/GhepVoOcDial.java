package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepVoOcDial extends AbsCheTao {

    public GhepVoOcDial() {
        // Result ID: 453, Category: 4 (Item), Beri cost: 10,000, Ruby cost: 0
        super(453, (byte) 4, 10_000, 0);
        this.successRate = 10;
        // Ingredients: 5x of item 454
        addIngredient(454, (byte) 4, 5);
    }

    public static void showTable(model.Player p) throws java.io.IOException {
        zabstracts.AbsCheTao.showTable(p, GhepVoOcDial.class);
    }

    @Override
    protected boolean isEventActive() {
        return true; // Always active
    }
}
