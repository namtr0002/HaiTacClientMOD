package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepHopQuaThuong2 extends AbsCheTao {

    public GhepHopQuaThuong2() {
        super(593, (byte) 4, 20_000, 0);
        this.successRate = 100;
        addIngredient(591, (byte) 4, 10); // Cành hoa 20/10
        addIngredient(575, (byte) 4, 1);  // Giấy gói quà
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "top_goi_hop_qua", 20, 2 * quantity);
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "point_event1", 20, 2 * quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_20THANG10_2025);
    }
}
