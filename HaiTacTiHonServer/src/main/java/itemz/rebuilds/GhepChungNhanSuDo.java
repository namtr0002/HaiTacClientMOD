package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepChungNhanSuDo extends AbsCheTao {

    public GhepChungNhanSuDo() {
        // Result ID: 627, Category: 4 (Item), Beri cost: 0, Ruby cost: 50
        super(627, (byte) 4, 0, 50);
        this.costExtol = 50_000;
        this.successRate = 100;
        // Ingredients: 1x of items 623 through 626
        addIngredient(623, (byte) 4, 1);
        addIngredient(624, (byte) 4, 1);
        addIngredient(625, (byte) 4, 1);
        addIngredient(626, (byte) 4, 1);
    }

    public static void showTable(model.Player p) throws java.io.IOException {
        zabstracts.AbsCheTao.showTable(p, GhepChungNhanSuDo.class);
    }

    @Override
    protected boolean isEventActive() {
        return true; // Always active
    }
}
