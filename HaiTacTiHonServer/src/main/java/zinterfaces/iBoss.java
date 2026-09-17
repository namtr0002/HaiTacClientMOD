package zinterfaces;

import model.Player;

public interface iBoss {
    void onDeath(Player pKill);
    void update();
}
