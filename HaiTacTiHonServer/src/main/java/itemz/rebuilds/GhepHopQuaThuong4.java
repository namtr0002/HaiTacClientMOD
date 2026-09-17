package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepHopQuaThuong4 extends AbsCheTao {

    public GhepHopQuaThuong4() {
        super(595, (byte) 4, 40_000, 0);
        this.successRate = 100;
        addIngredient(591, (byte) 4, 20); // Cành hoa 20/10
        addIngredient(575, (byte) 4, 1);  // Giấy gói quà
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "top_goi_hop_qua", 20, 4 * quantity);
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "point_event1", 20, 4 * quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_20THANG10_2025);
    }
}
