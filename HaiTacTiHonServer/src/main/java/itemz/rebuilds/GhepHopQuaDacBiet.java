package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepHopQuaDacBiet extends AbsCheTao {

    public GhepHopQuaDacBiet() {
        super(596, (byte) 4, 0, 5);
        this.successRate = 100;
        addIngredient(591, (byte) 4, 30); // Cành hoa 20/10
        addIngredient(575, (byte) 4, 1);  // Giấy gói quà
        addIngredient(576, (byte) 4, 1);  // Ruy băng tím
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "top_hop_qua_db", 21, 1 * quantity);
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "point_event2", 21, 1 * quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_20THANG10_2025);
    }
}
