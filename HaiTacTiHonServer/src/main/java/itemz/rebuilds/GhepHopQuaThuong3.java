package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepHopQuaThuong3 extends AbsCheTao {

    public GhepHopQuaThuong3() {
        super(594, (byte) 4, 30_000, 0);
        this.successRate = 100;
        addIngredient(591, (byte) 4, 15); // Cành hoa 20/10
        addIngredient(575, (byte) 4, 1);  // Giấy gói quà
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "top_goi_hop_qua", 20, 3 * quantity);
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "point_event1", 20, 3 * quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_20THANG10_2025);
    }
}
