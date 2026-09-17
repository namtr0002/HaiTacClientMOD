package bot;

import model.Player;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import template.EffTemplate;
import skill.Skill_info;
import java.util.ArrayList;
import java.util.List;

public class AttackMonster implements IAttack {

    private Mob target;

    private Mob detect(Bot owner) {
        Zone map = owner.map;
        if (map == null || map.list_mob == null) return null;
        for (int i = 0; i < map.list_mob.length; i++) {
            Mob mob = map.getMob(map.list_mob[i]);
            if (mob != null && !mob.isdie) {
                return mob;
            }
        }
        return null;
    }

    @Override
    public void attack(Bot owner) {
        Zone map = owner.map;
        if (map == null || owner.isdie) {
            return;
        }

        if (target == null || target.isdie || target.map != map) {
            target = detect(owner);
        }

        if (target == null) {
            return;
        }

        if (owner.timeAtkBot < System.currentTimeMillis()) {
            List<Skill_info> listSkillAtk = new ArrayList<>();
            for (int j = 0; j < owner.skill_point.size(); j++) {
                if (owner.skill_point.get(j).temp.typeSkill == 1) {
                    listSkillAtk.add(owner.skill_point.get(j));
                }
            }
            long nowNano = System.nanoTime();
            for (int j = 0; j < listSkillAtk.size(); j++) {
                Skill_info sk = listSkillAtk.get(j);
                if (sk == null || sk.temp == null) continue;
                int idSkill = sk.temp.ID;
                if (owner.time_use_skill.containsKey(idSkill)) {
                    long time = owner.time_use_skill.get(idSkill);
                    if (time > nowNano) {
                        continue;
                    }
                }
                long cdMs = owner.calculateSkillCooldown(sk.temp);
                owner.time_use_skill.put(idSkill, nowNano + cdMs * 1_000_000L);
                owner.applySkillCooldown(idSkill, sk.temp);
                owner.timeAtkBot = System.currentTimeMillis() + cdMs;
                
                long dame = owner.ability.get_dame(true);
                dame = (dame * owner.ability.get_dame_devil_percent()) / 100;
                EffTemplate eff = owner.get_eff(5); // combo
                if (eff != null) {
                    dame *= 2;
                }
                eff = owner.get_eff(18); // skill boc pha
                if (eff != null) {
                    dame = (dame * eff.param) / 100;
                }
                if (dame > 2 && owner.get_eff(21) != null) { // zoombie
                    dame /= 2;
                }
                
                if (!owner.isdie) {
                    try {
                        owner.attackMob(new Mob[]{target}, idSkill, dame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
