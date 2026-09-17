package bot;

import model.Player;
import core.ZUtil;
import network.Message;
import map.Zone;
import template.EffTemplate;
import skill.Skill_info;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttackTarget implements IAttack {

    private Player target;

    public AttackTarget(Player target) {
        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    @Override
    public void attack(Bot owner) {
        Zone map = owner.map;
        if (map == null || owner.isdie || target == null || target.isdie || target.map != map || Zone.isWaitingOrUnstartedMatch(map)) {
            return;
        }

        // Trạng thái PK hợp lệ theo quy tắc game
        if (!owner.canAttackTargetPlayer(target)) {
            return;
        }

        if (owner.timeAtkBot < System.currentTimeMillis()) {
            if (owner.skill_point == null || owner.skill_point.isEmpty()) {
                owner.setupBotSkills(owner.clazz, (short) Math.max(5, (int) owner.level / 3), 1, 0);
            }

            // === THUẬT TOÁN CHỌN SKILL THÔNG MINH (ALL SKILL ROTATION) ===
            // Lọc chính xác Chiêu chủ động (loại bỏ hoàn toàn chiêu bị động typeSkill == 2 hoặc 3).
            // Ưu tiên xoay vòng toàn bộ Chiêu chủ động, Nộ, TAQ, Haki trước. Khi tất cả đang CD mới về Chiêu thường.
            Skill_info selectedSkill = null;
            long nowNano = System.nanoTime();
            List<Skill_info> readySpecialSkills = new ArrayList<>();
            Skill_info basicSkill = null;

            if (owner.skill_point != null) {
                for (int j = 0; j < owner.skill_point.size(); j++) {
                    Skill_info sk = owner.skill_point.get(j);
                    if (sk != null && sk.temp != null) {
                        // Chỉ chấp nhận CHIÊU CHỦ ĐỘNG (loại bỏ bị động typeSkill 2 hoặc 3)
                        boolean isActive = sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3;
                        if (!isActive) continue;

                        int skId = sk.temp.ID;
                        int indexInServer = sk.temp.indexSkillInServer;
                        int baseClassIdx = (owner.clazz > 0 ? owner.clazz - 1 : 0) * 60;
                        boolean isBasic = (skId == 0 || indexInServer == baseClassIdx);

                        if (isBasic) {
                            if (basicSkill == null) basicSkill = sk;
                        } else {
                            if (!owner.time_use_skill.containsKey(skId) || owner.time_use_skill.get(skId) <= nowNano) {
                                readySpecialSkills.add(sk);
                            }
                        }
                    }
                }
            }

            if (!readySpecialSkills.isEmpty()) {
                selectedSkill = readySpecialSkills.get(ZUtil.random(readySpecialSkills.size()));
            } else if (basicSkill != null) {
                selectedSkill = basicSkill;
            } else if (owner.skill_point != null && !owner.skill_point.isEmpty()) {
                for (Skill_info sk : owner.skill_point) {
                    if (sk != null && sk.temp != null && sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3) {
                        selectedSkill = sk;
                        break;
                    }
                }
            }

            if (selectedSkill != null && selectedSkill.temp != null) {
                int idSkill = selectedSkill.temp.ID;
                long skillCdMs = owner.calculateSkillCooldown(selectedSkill.temp);
                owner.time_use_skill.put(idSkill, (nowNano + skillCdMs * 1_000_000L));
                owner.applySkillCooldown(idSkill, selectedSkill.temp);

                long basicDelay = (basicSkill != null && basicSkill.temp != null) ? owner.calculateSkillCooldown(basicSkill.temp) : skillCdMs;
                long attackDelay = (selectedSkill == basicSkill) ? skillCdMs : basicDelay;
                owner.timeAtkBot = System.currentTimeMillis() + attackDelay;
                
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
                // skill kaido thoi trang
                for (int i12 = 0; i12 < owner.fashion.size(); i12++) {
                    if (owner.fashion.get(i12).id == 122 && owner.fashion.get(i12).is_use) {
                        eff = owner.get_eff(23); // skill kaido thoi trang
                        if (eff == null && 8 > ZUtil.random(120)) {
                            owner.add_new_eff(23, 0, 8000);
                            owner.getService().addEffect(owner.index_map, (short) 103, 8000, (byte) 0, (byte) 20);
                        }
                        break;
                    }
                }
                if (dame > 2 && owner.get_eff(21) != null) { // zoombie
                    dame /= 2;
                }
                // Kích ẩn tích lũy khi tấn công (Đánh là choáng 16 hits / Thanh lọc 8 hits)
                owner.accumulateOffensiveKichAn(owner.map != null ? owner.map.getService() : null);
                
                if (!owner.isdie && !target.isdie) {
                    try {
                        owner.attackPlayer(new Player[]{target}, idSkill, dame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
