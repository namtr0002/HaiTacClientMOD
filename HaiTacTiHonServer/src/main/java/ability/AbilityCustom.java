package ability;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Player;
import template.Option;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class AbilityCustom implements AbilityStrategy {
    private int hp;
    private int mp;
    private int damage, damage2;
    private int exactly;
    private int miss;
    private int fatal;
    private byte speed;
    private int resFire, resIce, resWind, reactDame;
    private ArrayList<Option> options;
    @Override
    public void setAbility(Player owner) {
        if (owner == null) return;
        if (this.hp > 0) owner.hpMax = this.hp;
        if (this.mp > 0) owner.mpMax = this.mp;
        if (this.damage > 0) owner.dame = this.damage;
        if (this.miss > 0) owner.miss = this.miss;
        if (this.fatal > 0) owner.crit = this.fatal;
        if (this.speed > 0) owner.agility = (int) this.speed;
        if (this.reactDame > 0) owner.reactDame = this.reactDame;
        if (this.resFire > 0) owner.resPhys = this.resFire;
        if (this.resIce > 0) owner.resMag = this.resIce;

        if (this.options != null && !this.options.isEmpty()) {
            int optSize = template.ItemOptionTemplate.ENTRYS != null ? template.ItemOptionTemplate.ENTRYS.size() : 200;
            if (owner.optionParams == null || owner.optionParams.length < optSize) {
                owner.optionParams = new int[optSize];
            }
            for (Option op : this.options) {
                if (op != null && op.id >= 0 && op.id < owner.optionParams.length) {
                    owner.optionParams[op.id] += op.getParam();
                }
            }
        }
    }
    
}
