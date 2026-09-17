package itemz.rebuilds;

import zabstracts.AbsCheTao;

public class GhepRuongTinhYeu extends AbsCheTao {

    public GhepRuongTinhYeu() {
        // Result ID: 440, Category: 4 (Item), Beri cost: 0, Ruby cost: 10
        super(440, (byte) 4, 0, 10);
        this.successRate = 100;
        // Ingredients: 5x Item 437, 5x Item 438
        addIngredient(437, (byte) 4, 5);
        addIngredient(438, (byte) 4, 5);
    }

    @Override
    protected void afterCraft(model.Player p, int quantity, boolean success) {
        if (success) {
            p.update_pointEvent1(quantity * 5);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_VALENTINE);
    }
}
