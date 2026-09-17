package ability;

import model.Player;

public class AbilityFromEquip implements AbilityStrategy {

    @Override
    public void setAbility(Player owner) {
        if (owner == null) return;
        owner.ability.recalculatePlayerStats(owner);
    }
    
}

