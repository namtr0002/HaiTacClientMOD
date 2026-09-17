package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class CheTaoDial extends AbsCheTao {

    public CheTaoDial() {
        // Result ID: 455, Category: 4 (Item), Beri cost: 15,000, Ruby cost: 0
        super(455, (byte) 4, 15_000, 0);
        this.costExtol = 2_000;
        this.successRate = 20;
        // Ingredients: 1x of 452, 1x of 453
        addIngredient(452, (byte) 4, 1);
        addIngredient(453, (byte) 4, 1);
    }

    public static void showTable(Player p) throws IOException {
        AbsCheTao.showTable(p, CheTaoDial.class);
    }

    @Override
    public void show_table(Player p) throws IOException {
        this.successRate = 20 + p.get_tyle_ghep_dial();
        super.show_table(p);
    }

    @Override
    public void process(Player p, byte action, short id, byte cat, short num) throws IOException {
        // Dynamically adjust success rate based on player stats
        this.successRate = 20 + p.get_tyle_ghep_dial();
        super.process(p, action, id, cat, num);
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        p.updateExpSkill(5000, 20);
    }

    @Override
    protected boolean isEventActive() {
        return true; // Always active
    }
}
