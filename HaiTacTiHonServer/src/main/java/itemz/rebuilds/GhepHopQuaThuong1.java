package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import java.io.IOException;

public class GhepHopQuaThuong1 extends AbsCheTao {

    public GhepHopQuaThuong1() {
        super(592, (byte) 4, 10_000, 0);
        this.successRate = 100;
        addIngredient(591, (byte) 4, 5); // Cành hoa 20/10
        addIngredient(575, (byte) 4, 1); // Giấy gói quà
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        if (success && p != null) {
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "top_goi_hop_qua", 20, 1 * quantity);
            p.addEventPoint(event.Event.ID_SUKIEN_20THANG10_2025, "point_event1", 20, 1 * quantity);
        }
    }

    @Override
    protected boolean isEventActive() {
        return event.EventManager.isActive(event.Event.ID_SUKIEN_20THANG10_2025);
    }
}
