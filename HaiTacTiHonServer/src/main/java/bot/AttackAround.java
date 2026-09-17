package bot;

import model.Player;
import core.ZUtil;
import network.Message;
import map.Zone;
import mob.Mob;
import template.EffTemplate;
import skill.Skill_info;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.zones.WorldWar;

import map.zones.ChiemDao;
import clan.Clan;

public class AttackAround implements IAttack {

    private Player targetPlayer;
    private Mob targetMob;
    private Player priorityTarget;
    private long priorityExpireTime;

    public void setPriorityTarget(Player target) {
        this.priorityTarget = target;
        this.priorityExpireTime = System.currentTimeMillis() + 15_000L;
    }

    private Player detectPlayer(Bot owner) {
        Zone map = owner.map;
        if (map == null || Zone.isMapLang(map.template.id)) return null;

        // Ưu tiên số 1: Trả thù người chơi vừa tấn công Bot
        if (priorityTarget != null && priorityExpireTime > System.currentTimeMillis()
                && !priorityTarget.isdie && priorityTarget.map != null && priorityTarget.map.equals(map)) {
            if (owner.canAttackTargetPlayer(priorityTarget)) {
                return priorityTarget;
            }
        }

        List<Player> targets = new ArrayList<>();
        for (int i = 0; i < map.players.size(); i++) {
            Player p = map.players.get(i);
            if (p != null && p.IDPlayer != owner.IDPlayer && !p.isdie && !p.isSpectator) {
                if (owner.canAttackTargetPlayer(p)) {
                    targets.add(p);
                }
            }
        }
        if (targets.isEmpty()) return null;

        // 70% tìm đối thủ gần nhất để tấn công, 30% chọn ngẫu nhiên
        if (core.ZUtil.random(100) < 70) {
            Player closest = null;
            double minDist = Double.MAX_VALUE;
            for (Player p : targets) {
                double dist = Math.hypot(p.x - owner.x, p.y - owner.y);
                if (dist < minDist) {
                    minDist = dist;
                    closest = p;
                }
            }
            return closest;
        } else {
            return targets.get(core.ZUtil.random(targets.size()));
        }
    }

    private Mob detectMob(Bot owner) {
        Zone map = owner.map;
        if (map == null) return null;
        
        // Trong map Bảo Vệ Pháo Đài: Tự động target trụ đối thủ hoặc boss đường tương ứng
        if (map.IsMapBaoVePhaoDai() && map.baoVePhaoDai != null) {
            int botFlag = owner.type_pk; // 4: Đội Đỏ, 5: Đội Xanh
            if (map.template.id == 268) { // Đường Trên
                Mob enemyTurret = (botFlag == 5) ? map.baoVePhaoDai.truThuongATren : map.baoVePhaoDai.truThuongBTren;
                if (enemyTurret != null && !enemyTurret.isdie && enemyTurret.map != null && enemyTurret.map.equals(map)) return enemyTurret;
                if (map.baoVePhaoDai.bossDuongTren != null && !map.baoVePhaoDai.bossDuongTren.isdie && map.baoVePhaoDai.bossDuongTren.map != null && map.baoVePhaoDai.bossDuongTren.map.equals(map)) {
                    return map.baoVePhaoDai.bossDuongTren;
                }
            } else if (map.template.id == 269) { // Đường Dưới
                Mob enemyTurret = (botFlag == 5) ? map.baoVePhaoDai.truThuongADuoi : map.baoVePhaoDai.truThuongBDuoi;
                if (enemyTurret != null && !enemyTurret.isdie && enemyTurret.map != null && enemyTurret.map.equals(map)) return enemyTurret;
                if (map.baoVePhaoDai.bossDuongDuoi != null && !map.baoVePhaoDai.bossDuongDuoi.isdie && map.baoVePhaoDai.bossDuongDuoi.map != null && map.baoVePhaoDai.bossDuongDuoi.map.equals(map)) {
                    return map.baoVePhaoDai.bossDuongDuoi;
                }
            } else if (map.template.id == 270) { // Đường Giữa
                Mob enemyTurret = (botFlag == 5) ? map.baoVePhaoDai.truChinhA : map.baoVePhaoDai.truChinhB;
                if (enemyTurret != null && !enemyTurret.isdie && enemyTurret.map != null && enemyTurret.map.equals(map)) return enemyTurret;
                if (map.baoVePhaoDai.bossDuongGiua != null && !map.baoVePhaoDai.bossDuongGiua.isdie && map.baoVePhaoDai.bossDuongGiua.map != null && map.baoVePhaoDai.bossDuongGiua.map.equals(map)) {
                    return map.baoVePhaoDai.bossDuongGiua;
                }
            }
            return null;
        }

        // Trong map Chiếm Đảo Bang Hội (Zone 254..258, 261..265, map_dungeon instanceof ChiemDao)
        boolean isChiemDao = (map.map_dungeon instanceof ChiemDao)
                || (map.template != null && ((map.template.id >= 254 && map.template.id <= 258) || (map.template.id >= 261 && map.template.id <= 265)));
        if (isChiemDao) {
            Clan topClan = ChiemDao.getClanTop(map.template != null ? map.template.id : 0);
            if (map.map_dungeon instanceof ChiemDao && ((ChiemDao) map.map_dungeon).occupyingClan != null) {
                topClan = ((ChiemDao) map.map_dungeon).occupyingClan;
            }
            boolean isDefender = (owner.type_pk == 5)
                    || (owner.clan != null && topClan != null && (owner.clan.id == topClan.id || (owner.clan.name != null && owner.clan.name.equalsIgnoreCase(topClan.name))));
            if (isDefender || owner.clan == null) {
                // Phe Thủ hoặc không có bang -> Tuyệt đối không đánh trụ phe mình
                return null;
            }

            // Phe Công: Ưu tiên Trụ Phụ (132) trước, khi sạch Trụ Phụ mới chuyển sang Trụ Chính (133)
            int aliveSubs = ChiemDao.getAliveSubTurretCount(map);
            if (aliveSubs > 0) {
                if (map.mobs != null) {
                    for (Mob m : map.mobs.values()) {
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) return m;
                    }
                }
                if (map.list_mob != null) {
                    for (int idx : map.list_mob) {
                        Mob m = map.getMob(idx);
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) return m;
                    }
                }
                return null;
            } else {
                if (map.map_dungeon instanceof ChiemDao && ((ChiemDao) map.map_dungeon).mainTurret != null && !((ChiemDao) map.map_dungeon).mainTurret.isdie && ((ChiemDao) map.map_dungeon).mainTurret.hp > 0) {
                    return ((ChiemDao) map.map_dungeon).mainTurret;
                }
                if (map.mobs != null) {
                    for (Mob m : map.mobs.values()) {
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 133 && !m.isdie && m.hp > 0) return m;
                    }
                }
                if (map.list_mob != null) {
                    for (int idx : map.list_mob) {
                        Mob m = map.getMob(idx);
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 133 && !m.isdie && m.hp > 0) return m;
                    }
                }
                return null;
            }
        }

        if (map.list_mob == null) return null;
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

        boolean isPvpContext = map.map_vp != null || map.pvpBang != null || map.map_little_garden != null 
                || WorldWar.mapTranChienLon(map.template.id) || (owner.type_pk >= 0) 
                || owner instanceof BotPVP || owner instanceof BotTruyNa || owner instanceof BotKhoBau 
                || (priorityTarget != null && priorityExpireTime > System.currentTimeMillis());

        if (isPvpContext) {
            // Trong bối cảnh PvP / Bị tấn công / Có cờ PK -> Ưu tiên tìm Player địch
            if (targetPlayer == null || targetPlayer.isdie || targetPlayer.map != map || !owner.canAttackTargetPlayer(targetPlayer)) {
                targetPlayer = detectPlayer(owner);
            }
            if (targetPlayer == null) {
                if (targetMob == null || targetMob.isdie || targetMob.map != map) {
                    targetMob = detectMob(owner);
                }
            } else {
                targetMob = null;
            }
        } else {
            // Trong bối cảnh cày cấp / train quái bình thường -> Ưu tiên 100% Mob quái
            if (targetMob == null || targetMob.isdie || targetMob.map != map) {
                targetMob = detectMob(owner);
            }
            if (targetMob == null) {
                // Tuyệt đối không tự ý tìm đánh người chơi vô cớ khi đang train quái.
                // Chỉ tự vệ phản đòn nếu có người chơi vừa chủ động tấn công bot.
                if (priorityTarget != null && priorityExpireTime > System.currentTimeMillis()
                        && !priorityTarget.isdie && priorityTarget.map != null && priorityTarget.map.equals(map)
                        && owner.canAttackTargetPlayer(priorityTarget)) {
                    targetPlayer = priorityTarget;
                } else {
                    targetPlayer = null;
                }
            } else {
                targetPlayer = null;
            }
        }

        if (targetPlayer == null && targetMob == null) {
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
                
                if (!owner.isdie) {
                    try {
                        if (targetPlayer != null) {
                            owner.attackPlayer(new Player[]{targetPlayer}, idSkill, dame);
                        } else if (targetMob != null) {
                            owner.attackMob(new Mob[]{targetMob}, idSkill, dame);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
