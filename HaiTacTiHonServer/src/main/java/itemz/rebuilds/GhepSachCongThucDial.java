package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepSachCongThucDial extends AbsCheTao {

    public GhepSachCongThucDial() {
        // Result ID: 452, Category: 4 (Item), Beri cost: 10,000, Ruby cost: 0
        super(452, (byte) 4, 10_000, 0);
        this.successRate = 10;
        // Ingredients: 5x of item 451
        addIngredient(451, (byte) 4, 5);
    }

    public static void showTable(model.Player p) throws java.io.IOException {
        zabstracts.AbsCheTao.showTable(p, GhepSachCongThucDial.class);
    }

    @Override
    protected boolean isEventActive() {
        return true; // Always active
    }
}
