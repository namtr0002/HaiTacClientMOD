package ability;

import skill.Skill_info;
import clan.Clan;
import template.*;
import activities.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.MyPet;
import model.Player;

public class Ability {

    public static String[] NameAttribute
            = new String[]{"Sức mạnh", "Phòng thủ", "Thể lực", "Tinh thần", "Nhanh nhẹn"};
    public static int[][] Id = new int[][]{ //
        new int[]{1, 13, 10}, // 1
        new int[]{4, 26, 27}, // 2
        new int[]{15, 23}, // 3
        new int[]{16, 11, 14}, // 4
        new int[]{25, 12}}; // 5

    public long getPoint1Atk(long point) {
        return AbilityTemplateManager.gI().getPoint1Atk(point);
    }
    public long getPoint1Crit(long point) {
        return AbilityTemplateManager.gI().getPoint1Crit(point);
    }
    public long getPoint1Pierce(long point) {
        return AbilityTemplateManager.gI().getPoint1Pierce(point);
    }
    public long getPoint2Def(long point) {
        return AbilityTemplateManager.gI().getPoint2Def(point);
    }
    public long getPoint2ResistPhysical(long point) {
        return AbilityTemplateManager.gI().getPoint2ResistPhysical(point);
    }
    public long getPoint2ResistMagic(long point) {
        return AbilityTemplateManager.gI().getPoint2ResistMagic(point);
    }
    public long getPoint3Hp(long point) {
        return AbilityTemplateManager.gI().getPoint3Hp(point);
    }
    public long getPoint3HpPotion(long point) {
        return AbilityTemplateManager.gI().getPoint3HpPotion(point);
    }
    public long getPoint4Mp(long point) {
        return AbilityTemplateManager.gI().getPoint4Mp(point);
    }
    public long getPoint4DameCrit(long point) {
        return AbilityTemplateManager.gI().getPoint4DameCrit(point);
    }
    public long getPoint4ReactDame(long point) {
        return AbilityTemplateManager.gI().getPoint4ReactDame(point);
    }
    public long getPoint5Cooldown(long point) {
        return AbilityTemplateManager.gI().getPoint5Cooldown(point);
    }
    public long getPoint5Miss(long point) {
        return AbilityTemplateManager.gI().getPoint5Miss(point);
    }
    public long getTemplateValue(int optionId, long point) {
        return AbilityTemplateManager.gI().getTemplateValue(optionId, point);
    }
    private final Player p;
    private AbilityStrategy strategy = new AbilityFromEquip();

    public Ability(Player p) {
        this.p = p;
    }

    public AbilityStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AbilityStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
    }

    public void setAbility() {
        if (this.strategy != null && this.p != null) {
            this.strategy.setAbility(this.p);
        }
    }

    public void setAbility(Player owner) {
        if (this.strategy != null && owner != null) {
            this.strategy.setAbility(owner);
        }
    }

    public int total_param_item(int id, boolean have_eff) {
        if (p == null) return 0;
        int[] params = (have_eff && p.optionParams != null && p.optionParams.length >= 200) ? p.optionParams : calculateAllOptionParams(have_eff);
        if (id >= 0 && id < params.length) {
            return params[id];
        }
        return 0;
    }

    public int[] calculateAllOptionParams(boolean have_eff) {
        int[] params = new int[ItemOptionTemplate.ENTRYS != null ? ItemOptionTemplate.ENTRYS.size() : 200];
        if (p == null) return params;

        // 1. Sudo
        Sudo mySudo = Sudo.getSuDoByName(p.name);
        if (mySudo != null && mySudo.nameRelative != null && !mySudo.nameRelative.isEmpty()) {
            Sudo spSudo = Sudo.getSuDoByName(mySudo.nameRelative.get(0));
            if (spSudo != null && spSudo.nameRelative != null) {
                if (spSudo.nameRelative.size() >= 2) {
                    params[1] += ((5 + (2 * (mySudo.lvExp - 1))) * 10);
                }
                if (spSudo.nameRelative.size() >= 3) {
                    params[25] += (10 * mySudo.lvExp);
                }
                if (spSudo.nameRelative.size() >= 4) {
                    params[51] += (10 * mySudo.lvExp);
                }
            }
        }

        // 2. Equipment levelUp bonuses
        if (p.item != null && p.item.it_body != null) {
            if (p.item.it_body[0] != null && !p.item.it_body[0].isThanTrang() && p.item.it_body[0].levelUp > 10) {
                params[46] += (15 * (Math.min(15, p.item.it_body[0].levelUp) - 10)); // Sát thương cuối (+1.5%/cấp, tối đa +15)
            }
            if (p.item.it_body[2] != null && !p.item.it_body[2].isThanTrang() && p.item.it_body[2].levelUp > 10) {
                params[53] += (10 * (Math.min(15, p.item.it_body[2].levelUp) - 10)); // Bỏ qua sát thương (+1.0%/cấp, tối đa +15)
            }
            if (p.item.it_body[1] != null && !p.item.it_body[1].isThanTrang() && p.item.it_body[1].levelUp > 10) {
                params[56] += (30 * (Math.min(15, p.item.it_body[1].levelUp) - 10));  // % HP max (+3.0%/cấp, tối đa +15)
            }
            if (p.item.it_body[3] != null && !p.item.it_body[3].isThanTrang() && p.item.it_body[3].levelUp > 10) {
                params[56] += (30 * (Math.min(15, p.item.it_body[3].levelUp) - 10));  // % HP max (+3.0%/cấp, tối đa +15)
            }
            if (p.item.it_body[5] != null && !p.item.it_body[5].isThanTrang() && p.item.it_body[5].levelUp > 10) {
                params[56] += (30 * (Math.min(15, p.item.it_body[5].levelUp) - 10));  // % HP max (+3.0%/cấp, tối đa +15)
            }
            if (p.item.it_body[4] != null && !p.item.it_body[4].isThanTrang() && p.item.it_body[4].levelUp > 10) {
                params[47] += (10 * (Math.min(15, p.item.it_body[4].levelUp) - 10)); // Giảm hiệu ứng (+1.0%/cấp, tối đa +15)
            }
            // 3. Equipment options
            for (int j = 0; j < p.item.it_body.length; j++) {
                Item_wear bodyItem = p.item.it_body[j];
                if (bodyItem != null) {
                    if (bodyItem.option_item != null) {
                        for (int i = 0; i < bodyItem.option_item.size(); i++) {
                            Option op = bodyItem.option_item.get(i);
                            if (op != null && op.id >= 0 && op.id < params.length) {
                                params[op.id] += op.getParam(
                                        bodyItem.template != null ? bodyItem.template.typeEquip : 0,
                                        bodyItem.levelUp,
                                        bodyItem.isHoanMy);
                            }
                        }
                    }
                    if (bodyItem.option_item_2 != null) {
                        for (int i = 0; i < bodyItem.option_item_2.size(); i++) {
                            Option op = bodyItem.option_item_2.get(i);
                            if (op != null && op.id >= 0 && op.id < params.length) {
                                params[op.id] += op.getParam();
                            }
                        }
                    }
                }
            }
            // 3b. Thần Trang Full Set Passive Bonus
            model.ThanTrangConfig.applyPassiveBonus(p, params, have_eff);
        }

        // 4. Clan HanhtrinhIcon & Clan Buffs
        if (p.clan != null) {
            // Auto-resolve hanhtrinhIcon if not set but icon matches
            if (p.clan.hanhtrinhIcon == null && p.clan.icon > 0 && clan.ClanHanhTrinhIcon.ENTRY != null) {
                for (clan.ClanHanhTrinhIcon hti : clan.ClanHanhTrinhIcon.ENTRY) {
                    if (hti != null && (hti.icon == p.clan.icon || hti.id == p.clan.icon)) {
                        p.clan.hanhtrinhIcon = hti;
                        break;
                    }
                }
            }
            if (p.clan.hanhtrinhIcon != null && p.clan.hanhtrinhIcon.op != null) {
                for (int i = 0; i < p.clan.hanhtrinhIcon.op.size(); i++) {
                    Option op = p.clan.hanhtrinhIcon.op.get(i);
                    if (op != null && op.id >= 0 && op.id < params.length) {
                        params[op.id] += op.getParam();
                    }
                }
            }
            // Clan Buffs
            if (have_eff && p.clan.buff != null) {
                long nowMs = System.currentTimeMillis();
                for (EffTemplate b : p.clan.buff) {
                    if (b != null && b.time > nowMs) {
                        switch (b.id) {
                            case 2: // Bùa HP (+% HP max)
                                params[17] += b.param;
                                break;
                            case 3: // Bùa MP (+% MP max)
                                params[18] += b.param;
                                break;
                            case 4: // Bùa Tổng Hợp (+% HP, MP, Dame, Def)
                                params[17] += b.param;
                                params[18] += b.param;
                                params[1]  += b.param;
                                params[4]  += b.param;
                                break;
                        }
                    }
                }
            }
        }

        // 5. Fashion items
        if (p.fashion != null) {
            for (int i = 0; i < p.fashion.size(); i++) {
                ItemFashionP2 temp = p.fashion.get(i);
                if (temp != null && temp.is_use) {
                    ItemFashion tempF = ItemFashion.get_item(temp.id);
                    if (tempF != null) {
                        if (tempF.op != null) {
                            for (int j = 0; j < tempF.op.size(); j++) {
                                Option op = tempF.op.get(j);
                                if (op != null && op.id >= 0 && op.id < params.length) {
                                    int op_value = op.getParam();
                                    int percent = 0;
                                    for (int k = 1; k <= temp.level; k++) {
                                        if (k % 3 == 0) percent += 10;
                                        else percent += 3;
                                    }
                                    ItemOptionTemplate iot = ItemOptionTemplate.get(op.id);
                                    if (iot != null && iot.percent == 1) {
                                        op_value += percent;
                                    } else {
                                        op_value = (int) (((long) op_value * (1000L + percent)) / 1000L);
                                    }
                                    params[op.id] += op_value;
                                }
                            }
                        }
                        // Devil Fruit activation effects (Chỉ số Ẩn):
                        if (tempF.ID == 53 || tempF.ID == 54) {
                            if (checkSkillRange(541, 542)) addFashionExtra(params, 52, 150, temp.level); // Giảm phản đòn
                        } else if (tempF.ID == 59 || tempF.ID == 64) {
                            if (checkSkillRange(526, 527)) addFashionExtra(params, 49, 150, temp.level); // Giảm chí mạng
                        } else if (tempF.ID == 19) {
                            if (checkSkillRange(524, 525)) addFashionExtra(params, 52, 150, temp.level); // Giảm phản đòn
                        } else if (tempF.ID == 20) {
                            if (checkSkillRange(522, 523)) addFashionExtra(params, 50, 150, temp.level); // Giảm xuyên giáp
                        } else if (tempF.ID == 22) {
                            if (checkSkillRange(520, 521)) addFashionExtra(params, 56, 150, temp.level); // Máu cuối
                        }
                    }
                    break; // Chỉ cho phép mặc tối đa 1 bộ thời trang tại một thời điểm
                }
            }
        }

        // 6. Hair Fashion
        if (p.itfashionP != null) {
            for (ItemFashionP hair : p.itfashionP) {
                if (hair != null && hair.is_use && hair.category == 103 && hair.template != null && hair.template.options != null) {
                    for (Option option : hair.template.options) {
                        if (option != null && option.id >= 0 && option.id < params.length) {
                            params[option.id] += option.getParam();
                        }
                    }
                    break;
                }
            }
        }

        // 7. Pets
        if (p.my_pet != null) {
            for (int i = 0; i < p.my_pet.size(); i++) {
                MyPet pet = p.my_pet.get(i);
                if (pet != null && pet.isUse) {
                    if (pet.template == null) {
                        pet.template = model.Pet.getTemplate(pet.id);
                    }
                    if (pet.template != null && pet.template.op != null) {
                        for (int j = 0; j < pet.template.op.size(); j++) {
                            Option op = pet.template.op.get(j);
                            if (op != null && op.id >= 0 && op.id < params.length) {
                                params[op.id] += op.getParam();
                            }
                        }
                    }
                    break;
                }
            }
        }

        // 8. Danh Hieu (Titles)
        if (p.getIdEff() != -1) {
            activities.DanhHieu dh = activities.DanhHieu.get_Id(p.getIdEff());
            if (dh != null && dh.op != null) {
                for (int i = 0; i < dh.op.size(); i++) {
                    Option op = dh.op.get(i);
                    if (op != null && op.id >= 0 && op.id < params.length) {
                        params[op.id] += op.getParam();
                    }
                }
            }
        }

        // 9. Skill passive options
        if (p.skill_point != null) {
            for (int i = 0; i < p.skill_point.size(); i++) {
                Skill_info temp = p.skill_point.get(i);
                if (temp != null && temp.temp != null && temp.temp.Lv_RQ >= 0 && (temp.temp.typeSkill == 3 || temp.temp.typeSkill == 6)) {
                    if (temp.temp.op != null) {
                        for (int j = 0; j < temp.temp.op.size(); j++) {
                            Option op = temp.temp.op.get(j);
                            if (op != null && op.id >= 0 && op.id < params.length) {
                                int opVal = op.getParam();
                                if (temp.lvdevil > 0) {
                                    switch (temp.lvdevil) {
                                        case 1: opVal = (opVal * 11) / 10; break;
                                        case 2: opVal = (opVal * 125) / 100; break;
                                        case 3: opVal = (opVal * 145) / 100; break;
                                        case 4: opVal = (opVal * 17) / 10; break;
                                        case 5: opVal *= 2; break;
                                    }
                                }
                                params[op.id] += opVal;
                            }
                        }
                    }
                }
            }
        }

        // 9. Active effects & Party
        if (have_eff) {
            if (p.list_eff != null) {
                long nowMs = System.currentTimeMillis();
                for (int eIdx = 0; eIdx < p.list_eff.size(); eIdx++) {
                    EffTemplate temp = p.list_eff.get(eIdx);
                    if (temp != null && temp.id >= 100 && (temp.id - 100) < params.length && temp.time > nowMs) {
                        params[temp.id - 100] += temp.param;
                    }
                }
            }
            if (p.party != null) {
                List<Option> op_select = p.party.get_list_buff_now(p);
                if (op_select != null) {
                    // [FIX BUG 10] Snapshot list trước khi duyệt — tránh ConcurrentModificationException khi member rời đội
                    List<Option> op_snapshot = new ArrayList<>(op_select);
                    for (int i = 0; i < op_snapshot.size(); i++) {
                        Option op = op_snapshot.get(i);
                        if (op != null && op.id >= 0 && op.id < params.length) {
                            params[op.id] += op.getParam();
                        }
                    }
                }
            }
        }

        // 10. Heart item — all options (FIX BUG 4)
        if (p.item != null && p.item.it_heart != null) {
            boolean alreadyCounted = false;
            if (p.item.it_body != null) {
                for (int j = 0; j < p.item.it_body.length; j++) {
                    if (p.item.it_body[j] == p.item.it_heart) {
                        alreadyCounted = true;
                        break;
                    }
                }
            }
            if (!alreadyCounted) {
                int heartType = (p.item.it_heart.template != null) ? p.item.it_heart.template.typeEquip : 6;
                int heartTier = p.item.it_heart.levelUp;
                boolean hasOption = false;
                if (p.item.it_heart.option_item != null && !p.item.it_heart.option_item.isEmpty()) {
                    for (int i = 0; i < p.item.it_heart.option_item.size(); i++) {
                        Option op = p.item.it_heart.option_item.get(i);
                        if (op != null && op.id >= 0 && op.id < params.length) {
                            params[op.id] += op.getParam(heartType, heartTier, 0);
                            if (op.id == 56) hasOption = true;
                        }
                    }
                }
                if (!hasOption) {
                    params[56] += (100 + 50 * heartTier);
                }
            }
        }

        // 11. Special Hair
        boolean hasSuperHair = false;
        if (p.itfashionP != null) {
            for (ItemFashionP hairP : p.itfashionP) {
                if (hairP != null && hairP.is_use && hairP.category == 103 && (hairP.icon == 772 || hairP.id == 772)) {
                    hasSuperHair = true;
                    break;
                }
            }
        }
        if (!hasSuperHair) {
            int hair = p.get_hair();
            if (hair >= 772 && hair <= 774) {
                hasSuperHair = true;
            }
        }
        if (hasSuperHair) {
            params[17] += 50;
            params[12] += 30;
            params[19] += 20;
        }

        // 12. Journey Stones (daHanhTrinh)
        // Chỉ tính chỉ số của đá hành trình tại làng/đảo người chơi đang đứng, trừ các map biển
        if (p.daHanhTrinh != null && p.map != null && p.map.template != null) {
            int currentCat = HanhTrinh.getIslandCategory(p.map.template.id);
            if (currentCat >= 0 && currentCat < HanhTrinh.LANG.length) {
                for (int i = 0; i < p.daHanhTrinh.size(); i++) {
                    ItemBag47 dht = p.daHanhTrinh.get(i);
                    if (dht != null && dht.category == currentCat && dht.quant == 1) {
                        switch (dht.id) {
                            case 493: params[67] += 20; break; // +2% Exp đánh quái
                            case 494: params[68] += 20; break; // +2% Exp skill đánh quái
                            case 495: params[56] += 20; break; // +2% HP cuối
                            case 496: params[18] += 20; break; // +2% MP
                            case 497: params[23] += 20; break; // Ngăn dùng thức ăn / +2% Thức ăn
                            case 498: params[24] += 20; break; // Ngăn dùng nước uống / +2% Nước uống
                            case 499: params[69] += 20; break; // -2% Sát thương chí mạng đ/t
                            case 500: params[49] += 20; break; // -2% Tỉ lệ chí mạng đ/t
                            case 501: params[51] += 20; break; // -2% Né tránh đ/t
                            case 502: params[50] += 20; break; // -2% Xuyên giáp đ/t
                            case 503: params[52] += 20; break; // -2% Phản đòn đ/t
                            case 504: params[63] += 20; break; // -2% Miễn thương đ/t
                            case 505: params[80] += 20; break; // +2% Thời gian hồi chiêu đ/t
                            case 506: params[71] += 50; params[47] += 50; break; // +5% Kháng hiệu ứng
                            case 507: params[70] += 50; break; // -5% Tổng phòng thủ đ/t
                            case 508: params[5]  += 2; break; // +2 Tiềm năng sức mạnh
                            case 509: params[6]  += 2; break; // +2 Tiềm năng phòng thủ
                            case 510: params[7]  += 2; break; // +2 Tiềm năng thể lực
                            case 511: params[8]  += 2; break; // +2 Tiềm năng tinh thần
                            case 512: params[9]  += 2; break; // +2 Tiềm năng nhanh nhẹn
                            case 513: params[72] += 50; break; // +5% Beri nhận được khi train quái
                            case 514: params[73] += 20; break; // +2% Chuyển hóa sát thương thành HP
                            case 515: params[74] += 20; break; // +2% Chuyển hóa sát thương thành MP
                            case 516: params[75] += 10; break; // +1% Tỉ lệ choáng
                            case 517: params[76] += 10; break; // +1% Tỉ lệ chính xác
                        }
                        break; // Mỗi làng chỉ khảm tối đa 1 đá
                    }
                }
            }
        }

        // 13. Zombie Effect
        EffTemplate effZombie = p.get_eff(21);
        if (effZombie != null) {
            int targetId = effZombie.param;
            if (targetId >= 0 && targetId < params.length && params[targetId] > 1) {
                params[targetId] /= 2;
            }
        }

        return params;
    }

    private boolean checkSkillRange(int minIdx, int maxIdx) {
        if (p == null || p.skill_point == null) return false;
        for (int i = 0; i < p.skill_point.size(); i++) {
            Skill_info sk = p.skill_point.get(i);
            if (sk != null && sk.temp != null && sk.temp.indexSkillInServer >= minIdx && sk.temp.indexSkillInServer <= maxIdx) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSkillExact(int... indices) {
        if (p == null || p.skill_point == null) return false;
        for (int i = 0; i < p.skill_point.size(); i++) {
            Skill_info sk = p.skill_point.get(i);
            if (sk != null && sk.temp != null) {
                for (int idx : indices) {
                    if (sk.temp.indexSkillInServer == idx) return true;
                }
            }
        }
        return false;
    }

    private void addFashionExtra(int[] params, int optionId, int baseVal, int level) {
        if (optionId < 0 || optionId >= params.length) return;
        int op_value = baseVal;
        int percent = 0;
        for (int k = 1; k <= level; k++) {
            if (k % 3 == 0) percent += 10;
            else percent += 3;
        }
        ItemOptionTemplate iot = ItemOptionTemplate.get(optionId);
        if (iot != null && iot.percent == 1) {
            op_value += percent;
        } else {
            op_value = (int) (((long) op_value * (1000L + percent)) / 1000L);
        }
        params[optionId] += op_value;
    }

    public void recalculatePlayerStats(Player owner) {
        if (owner == null) return;
        if (owner instanceof bot.Bot && ((bot.Bot) owner).isBalancedStats && owner.hpMax > 0) {
            if (owner.optionParams == null || owner.optionParams.length < 200) {
                owner.optionParams = calculateAllOptionParams(true);
            }
            return;
        }
        owner.optionParams = null;
        owner.dame = 0;
        owner.damePercent = 0;
        owner.def = 0;
        owner.defPercent = 0;
        owner.hpMax = 0;
        owner.mpMax = 0;
        owner.agility = 0;
        owner.crit = 0;
        owner.pierce = 0;
        owner.miss = 0;
        owner.reactDame = 0;
        owner.resPhys = 0;
        owner.resMag = 0;

        int[] optionsWithEff = calculateAllOptionParams(true);
        owner.optionParams = optionsWithEff;

        owner.damePercent = computeDamePercent(true, optionsWithEff[1]);
        owner.dame = computeDame(true, owner.damePercent, optionsWithEff[0]);
        owner.defPercent = computeDefPercent(true, owner.ability.get_total_point(2), optionsWithEff[4]);
        owner.def = computeDef(true, owner.ability.get_total_point(2), optionsWithEff[3]);
        owner.hpMax = computeHpMax(true, owner.ability.get_total_point(3), optionsWithEff[15], optionsWithEff[17], optionsWithEff[56]);
        owner.mpMax = computeMpMax(true, owner.ability.get_total_point(4), optionsWithEff[16], optionsWithEff[18]);
        owner.agility = computeAgility(true, owner.ability.get_total_point(5), optionsWithEff[25]);
        owner.crit = computeCrit(true, owner.ability.get_total_point(1), optionsWithEff[10]);
        owner.pierce = computePierce(true, owner.ability.get_total_point(1), optionsWithEff[13]);
        owner.miss = computeMiss(true, owner.ability.get_total_point(5), optionsWithEff[12]);
        owner.reactDame = computeReactDame(true, owner.ability.get_total_point(4), optionsWithEff[14]);
        owner.resPhys = computeResistPhys(true, owner.ability.get_total_point(2), optionsWithEff[26]);
        owner.resMag = computeResistMag(true, owner.ability.get_total_point(2), optionsWithEff[27]);
    }

    public int computeDamePercent(boolean have_eff, int op1) {
        return op1 + (int) getPoint1Atk(get_total_point(1));
    }

    public int computeDame(boolean have_eff, int damePercent, int op0) {
        Skill_info sk_temp = p.get_skill_temp(0);
        if (sk_temp == null) return 0;
        long dame = sk_temp.temp.damage;
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 1) {
                    dame += (p.list_op_thongthao.get(i).getParam() * 5);
                }
            }
        }
        // [FIX BUG 5] Dùng long tránh int overflow khi dame * damePercent lớn
        dame = (dame * damePercent) / 1000L;
        dame += sk_temp.get_dame(p);
        dame += op0;
        dame += p.fusionBonusDame;
        return (int) Math.min(dame, Integer.MAX_VALUE);
    }

    public int computeDef(boolean have_eff, int totalP2, int op3) {
        int def = totalP2 + op3;
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 4) {
                    def += (p.list_op_thongthao.get(i).getParam() * 100);
                }
            }
        }
        def += p.fusionBonusDef;
        return def;
    }

    public int computeDefPercent(boolean have_eff, int totalP2, int op4) {
        // [FIX BUG 2] Đã bỏ op80 (Điện giật) khỏi đây — op80 không liên quan đến phòng thủ
        return op4 + (int) getPoint2Def(totalP2);
    }

    public int computeHpMax(boolean have_eff, int totalP3, int op15, int op17, int op56) {
        long hp = getPoint3Hp(totalP3) + op15;
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 15) {
                    hp += (p.list_op_thongthao.get(i).getParam() * 200);
                }
            }
        }
        int percent = op17;
        if (have_eff) {
            EffTemplate eff = p.get_eff(4);
            if (eff != null) percent += eff.param;
        }
        hp = (hp * (1000L + percent)) / 1000L;
        hp = (hp * (1000L + op56)) / 1000L;
        hp += p.fusionBonusHp;
        if (hp > 2_000_000_000L) hp = 2_000_000_000L;
        return (int) hp;
    }

    public int computeMpMax(boolean have_eff, int totalP4, int op16, int op18) {
        int mp = (int) getPoint4Mp(totalP4);
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 16) {
                    mp += (p.list_op_thongthao.get(i).getParam() * 10);
                }
            }
        }
        mp += op16;
        long totalMp = ((long) mp * (1000L + (long) op18)) / 1000L;
        mp = (int) Math.min(totalMp, 2_000_000_000L);
        mp += p.fusionBonusMp;
        if (mp < 500) {
            mp = 500;
        }
        return mp;
    }

    public int computeAgility(boolean have_eff, int totalP5, int op25) {
        int agi = op25;
        if (totalP5 > 0) agi += (int) getPoint5Cooldown(totalP5);
        if (p != null && p.get_eff(13) != null) agi += 500;
        if (have_eff && p != null && p.party != null) {
            List<Option> op_select = p.party.get_list_buff_now(p);
            if (op_select != null) {
                for (int i = 0; i < op_select.size(); i++) {
                    if (op_select.get(i).id == 25) {
                        agi += op_select.get(i).getParam();
                        break;
                    }
                }
            }
        }
        // Giới hạn giảm hồi chiêu mượt mà: 1:1 đến 75% (750), soft-cap từ 75% đến 85% (850)
        if (agi > 750) {
            agi = 750 + ((agi - 750) * 3) / 10;
        }
        if (agi > 850) {
            agi = 850;
        }
        if (agi < 0) {
            agi = 0;
        }
        return agi;
    }

    public int computeCrit(boolean have_eff, int totalP1, int op10) {
        int par = op10 + (int) getPoint1Crit(totalP1);
        return Math.max(0, par);
    }

    public int computePierce(boolean have_eff, int totalP1, int op13) {
        int par = op13 + (int) getPoint1Pierce(totalP1);
        return Math.max(0, par);
    }

    public int computeMiss(boolean have_eff, int totalP5, int op12) {
        int par = op12;
        if (totalP5 > 0) par += (int) getPoint5Miss(totalP5);
        return Math.max(0, par);
    }

    public int computeReactDame(boolean have_eff, int totalP4, int op14) {
        // % op14 trang bị + % Phản đòn từ Tiềm Năng Tinh Thần
        int par = Math.max(0, op14);
        if (totalP4 > 0) {
            par += (int) getPoint4ReactDame(totalP4);
        }
        if (p != null && p.get_eff(12) != null) {
            par += 1000;
        }
        return Math.max(0, par);
    }

    public int computeResistPhys(boolean have_eff, int totalP2, int op26) {
        int par = op26 + (int) getPoint2ResistPhysical(totalP2);
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 26) {
                    par += (p.list_op_thongthao.get(i).getParam() * 50);
                }
            }
        }
        if (p.get_eff(11) != null) par *= 2;
        return par;
    }

    public int computeResistMag(boolean have_eff, int totalP2, int op27) {
        int par = op27;
        if (p.list_op_thongthao != null) {
            for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                if (p.list_op_thongthao.get(i).id == 27) {
                    par += (p.list_op_thongthao.get(i).getParam() * 50);
                }
            }
        }
        par += (int) getPoint2ResistMagic(totalP2);
        if (p.get_eff(11) != null) par *= 2;
        return par;
    }

    public int get_total_point(int type) {
        int param = 0;
        switch (type) {
            case 1: {
                param += p.point1 + get_point_plus(1);
                break;
            }
            case 2: {
                param += p.point2 + get_point_plus(2);
                break;
            }
            case 3: {
                
                param += p.point3 + get_point_plus(3);
                  
                break;
            }
            case 4: {
                param += p.point4 + get_point_plus(4);
                break;
            }
            case 5: {
                param += p.point5 + get_point_plus(5);
                break;
            }
        }
        // [FIX BUG 8] Đã bỏ giảm 20% điểm tiềm năng khi Zombie eff — đã phạt đủ bằng chia đôi option ngẫu nhiên trong calculateAllOptionParams
        return Math.max(1, param);
    }

    public int get_dame(boolean have_eff) {
        if (have_eff && p != null && p.dame > 0) return p.dame;
        int damePercent = get_dame_percent(have_eff);
        return computeDame(have_eff, damePercent, total_param_item(0, have_eff));
    }

    public int get_dame_percent(boolean have_eff) {
        if (have_eff && p != null && p.damePercent > 0) return p.damePercent;
        return computeDamePercent(have_eff, total_param_item(1, have_eff));
    }

    public int get_def(boolean have_eff) {
        if (have_eff && p != null && p.def > 0) return p.def;
        return computeDef(have_eff, get_total_point(2), total_param_item(3, have_eff));
    }

    public int get_def_percent(boolean have_eff) {
        if (have_eff && p != null && p.defPercent > 0) return p.defPercent;
        // [FIX BUG 2] Đã bỏ tham số op80 — op80 (Điện giật) không liên quan đến phòng thủ
        return computeDefPercent(have_eff, get_total_point(2), total_param_item(4, have_eff));
    }

    public int get_hp_max(boolean have_eff) {
        if (have_eff && p != null && p.hpMax > 0) return p.hpMax;
        return computeHpMax(have_eff, get_total_point(3), total_param_item(15, have_eff), total_param_item(17, have_eff), total_param_item(56, true));
    }

    public int get_mp_max(boolean have_eff) {
        if (have_eff && p != null && p.mpMax > 0) return p.mpMax;
        return computeMpMax(have_eff, get_total_point(4), total_param_item(16, have_eff), total_param_item(18, have_eff));
    }

    public int get_agility(boolean have_eff) {
        if (have_eff && p != null && p.agility > 0) return p.agility;
        return computeAgility(have_eff, get_total_point(5), total_param_item(25, have_eff));
    }

    public int view_in4(int b) {
        switch (b) {
            case 0:
                return get_dame(true);
            case 1:
                return get_dame_percent(false);
            case 2:
                return get_dame_ap();
            case 3:
                return get_def(true);
            case 4:
                return get_def_percent(false);
            case 15:
                return (int) (total_param_item(15, false)
                        + getPoint3Hp(this.get_total_point(3)));
            case 16:
                return (int) (total_param_item(16, false)
                        + getPoint4Mp(this.get_total_point(4)));
            case 25:
                return get_agility(true);
            case 26:
                return get_dame_resist(false);
            case 27:
                return get_dame_resist_ap(false);
            case 17:
                return total_param_item(17, false);
            case 18:
                return total_param_item(18, false);
            case 13:
                return get_pierce(false);
            case 10:
                return get_crit(false);
            case 12:
                return get_miss(false);
            case 14:
                return get_dame_react(false);
            case 23:
                return get_hp_potion_use_percent(false);
            case 24:
                return get_mp_potion_use_percent(false);
            case 19:
                return get_hp_auto_buff(false);
            case 20:
                return get_mp_auto_buff(false);
            case 21:
                return get_hp_atk_absorb(false);
            case 22:
                return get_mp_atk_absorb(false);
            case 11:
                return get_multi_dame_when_crit(false);
            case 53:
                return get_dame_skip(false);
            case 63:
                return get_dame_skip_reduce();
            case 49:
                return get_crit_reduce();
            case 50:
                return get_pierce_reduce();
            case 51:
                return get_miss_reduce();
            case 52:
                return get_dame_react_reduce();
            case 55:
                return get_TuChoiTuThan();
            case 57:
                return get_true_dame();
            case 58:
            case 59:
                return get_HapThu_Hp();
            case 46:
                return get_percent_final_dame();
            case 67:
                return get_xp_more();
            case 68:
                return get_xp_skill_more();
            case 69:
                return get_multi_dame_decrease();
            case 72:
                return get_percent_beri_train();
            case 48:
                return get_dame_percent_hp_target();
        }
        return total_param_item(b, false);
    }

    public int get_param_by_id(int id) {
        return total_param_item(id, true);
    }

    public int get_multi_dame_decrease() {
        return total_param_item(69, false);
    }

    public int get_dame_skip_reduce() {
        int par = total_param_item(63, false);
        if (par > 750) {
            int save = par - 750;
            par = 750 + (save * 2) / 10;
        }
        return Math.min(5000, par);
    }

    public int get_TuChoiTuThan() {
        int result = total_param_item(55, true);
        int index_full_set = p.get_index_full_set();
        if (index_full_set > 3) {
            result += 100;
        }
        return result;
    }

    public int get_mp_atk_absorb(boolean have_eff) {
        return total_param_item(22, have_eff);
    }

    public int get_hp_atk_absorb(boolean have_eff) {
        return total_param_item(21, have_eff);
    }

    public int get_dame_react(boolean have_eff) {
        if (have_eff && p != null && p.reactDame > 0) return p.reactDame;
        return computeReactDame(have_eff, this.get_total_point(4), total_param_item(14, have_eff));
    }

    public int get_pierce(boolean have_eff) {
        if (have_eff && p != null && p.pierce > 0) return p.pierce;
        return computePierce(have_eff, this.get_total_point(1), total_param_item(13, have_eff));
    }

    public int get_miss(boolean have_eff) {
        if (have_eff && p != null && p.miss > 0) return p.miss;
        return computeMiss(have_eff, this.get_total_point(5), total_param_item(12, have_eff));
    }

    public int get_crit(boolean have_eff) {
        if (have_eff && p != null && p.crit > 0) return p.crit;
        return computeCrit(have_eff, this.get_total_point(1), total_param_item(10, have_eff));
    }

    public int get_multi_dame_when_crit(boolean have_eff) {
        int par = total_param_item(11, have_eff);
        par += (int) getPoint4DameCrit(this.get_total_point(4));
        return par;
    }

    public int get_point_plus(int type) {
        int par = 0;
        switch (type) {
            case 1: {
                par += total_param_item(5, true);
//                for (int i = 0; i < p.list_op_thongthao.size(); i++) {
//                    if (p.list_op_thongthao.get(i).id == 1) {
//                        par += p.list_op_thongthao.get(i).getParam();
//                    }
//                }
                break;
            }
            case 2: {
                par += total_param_item(6, true);
//                for (int i = 0; i < p.list_op_thongthao.size(); i++) {
//                    if (p.list_op_thongthao.get(i).id == 4 || p.list_op_thongthao.get(i).id == 26
//                            || p.list_op_thongthao.get(i).id == 27) {
//                        par += p.list_op_thongthao.get(i).getParam();
//                    }
//                }
                break;
            }
            case 3: {
                par += total_param_item(7, true);
//                for (int i = 0; i < p.list_op_thongthao.size(); i++) {
//                    if (p.list_op_thongthao.get(i).id == 15) {
//                        par += p.list_op_thongthao.get(i).getParam();
//                    }
//                }
                break;
            }
            case 4: {
                par += total_param_item(8, true);
//                for (int i = 0; i < p.list_op_thongthao.size(); i++) {
//                    if (p.list_op_thongthao.get(i).id == 16) {
//                        par += p.list_op_thongthao.get(i).getParam();
//                    }
//                }
                break;
            }
            case 5: {
                par += total_param_item(9, true);
                break;
            }
        }
        if (p != null && p.clan != null && p.clan.opAttri != null && (type - 1) >= 0 && (type - 1) < p.clan.opAttri.length) {
            par += p.clan.opAttri[type - 1] + Clan.get_point_trungsinh_plus(p.clan);
        }
        // Điểm tiềm năng cộng thêm từ trang bị và bang hội: giới hạn an toàn tối đa 80 điểm (+-50 đến 80)
        return Math.max(0, Math.min(80, par));
    }

    public int get_dame_skip(boolean have_eff) {
        int par = total_param_item(53, have_eff);
        if (par > 750) {
            int save = par - 750;
            par = 750 + (save * 2) / 10;
        }
        return Math.min(5000, par);
    }

    public int get_dame_resist(boolean have_eff) {
        if (have_eff && p != null && p.resPhys > 0) return p.resPhys;
        return computeResistPhys(have_eff, this.get_total_point(2), total_param_item(26, have_eff));
    }

    public int get_dame_resist_ap(boolean have_eff) {
        if (have_eff && p != null && p.resMag > 0) return p.resMag;
        return computeResistMag(have_eff, this.get_total_point(2), total_param_item(27, have_eff));
    }

    public int get_hp_potion_use_percent(boolean have_eff) {
        int par = total_param_item(23, have_eff);
        par += (int) getPoint3HpPotion(this.get_total_point(3));
        return par;
    }

    public int get_mp_potion_use_percent(boolean have_eff) {
        int par = total_param_item(24, have_eff);
        return par;
    }

    public int get_hp_auto_buff(boolean have_eff) {
        return total_param_item(19, have_eff);
    }

    public int get_mp_auto_buff(boolean have_eff) {
        return total_param_item(20, have_eff);
    }

    public int get_dame_ap() {
        return total_param_item(2, true);
    }

    public int get_dame_percent_hp_target() {
        int result = total_param_item(48, true);
        if (result >= 250) {
            result = 250;
        }
        return result;
    }

    public int get_kich_an(int id) {
        if (id < 0 || id > 12) return 0;
        if (p == null || p.item == null || p.item.it_body == null) return 0;
        int count = 0;
        for (int i = 0; i < Math.min(8, p.item.it_body.length); i++) {
            Item_wear it = p.item.it_body[i];
            if (it != null && it.canHaveKichAn() && it.getSanitizedValueKichAn() == id) {
                count++;
            }
        }
        return count; // Đếm toàn bộ số món trang bị (hỗ trợ full set kể cả Tim & Dial)
    }

    public int get_percent_beri_train() {
        int par = total_param_item(72, true);
        for (int i = 0; i < p.fashion.size(); i++) {
            if (p.fashion.get(i).id == 77 && p.fashion.get(i).is_use) {
                par += 300;
                break;
            }
        }
        return par;
    }

    public int get_crit_reduce() {// vuong code fix giảm chí mạng
        int par = total_param_item(49, true);
        int index_full_set = p.get_index_full_set();
//        if (index_full_set == 5) {
//            par += (80 * 1);
//        } else if (index_full_set > 2) {
//            par += (50 * 1);
//        }
        if (index_full_set == 5) {
            par += 4;
        } else if (index_full_set > 0) {
            par += 2;
        }
        return par;
    }

    public int get_dame_react_reduce() {// vuong code fix giảm phản đòn
        int par = total_param_item(52, true);
        int index_full_set = p.get_index_full_set();
//        if (index_full_set == 5) {
//            par += (80 * 1);
//        } else if (index_full_set > 1) {
//            par += (50 * 1);
//        }
        if (index_full_set == 5) {
            par += 4;
        } else if (index_full_set > 0) {
            par += 2;
        }
        return par;
    }

    public int get_pierce_reduce() {// vuong code fix giảm xuyên giáp
        int par = total_param_item(50, true);
        int index_full_set = p.get_index_full_set();
//        if (index_full_set == 5) {
//            par += (80 * 1);
//        } else if (index_full_set > 2) {
//            par += (50 * 1);
//        }
        if (index_full_set == 5) {
            par += 4;
        } else if (index_full_set > 0) {
            par += 2;
        }
        return par;
    }

    public int get_miss_reduce() {// vuong code fix giảm né
        int par = total_param_item(51, true) + total_param_item(76, true);
        int index_full_set = p.get_index_full_set();
//        if (index_full_set == 5) {
//            par += (80 * 1);
//        } else if (index_full_set > 0) {
//            par += (50 * 1);
        if (index_full_set == 5) {
            par += 4;
        } else if (index_full_set > 0) {
            par += 2;
        }
        return par;
    }

    public int get_true_dame() {
        int par = total_param_item(57, true);
        int index_full_set = p.get_index_full_set();
        if (index_full_set == 5) {
            par += 100;
        }
        return par;
    }

    public byte get_level_perfect() {
        if (p == null || p.item == null || p.item.it_body == null) return 0;
        byte result = 0;
        for (int i = 0; i < Math.min(6, p.item.it_body.length); i++) {
            Item_wear it = p.item.it_body[i];
            if (it != null && it.canHaveKichAn() && it.isHoanMy == 1
                    && it.getSanitizedValueKichAn() > -1) {
                result++;
            }
        }
        return result;
    }

    public int get_percent_final_dame() {
        return total_param_item(46, true);
    }

    public int get_xp_more() {
        return total_param_item(67, true);
    }

    public int get_xp_skill_more() {
        return total_param_item(68, true);
    }

    public int get_dame_devil_percent() {
        int maxLv = 0;
        if (p != null && p.skill_point != null) {
            for (int i = 0; i < p.skill_point.size(); i++) {
                Skill_info sk = p.skill_point.get(i);
                if (sk != null && sk.lvdevil > maxLv) {
                    maxLv = sk.lvdevil;
                }
            }
        }
        return get_dame_devil_percent_by_level(maxLv);
    }

    public int get_dame_devil_percent(Skill_info sk) {
        if (sk != null && sk.lvdevil > 0) {
            return get_dame_devil_percent_by_level(sk.lvdevil);
        }
        return get_dame_devil_percent();
    }

    public static int get_dame_devil_percent_by_level(int lvdevil) {
        int par = 100;
        switch (lvdevil) {
            case 1: {
                par += 10;
                break;
            }
            case 2: {
                par += 25;
                break;
            }
            case 3: {
                par += 45;
                break;
            }
            case 4: {
                par += 70;
                break;
            }
            case 5: {
                par += 100;
                break;
            }
        }
        return par;
    }

    public int get_reduce_Eff() {
        return total_param_item(47, true);
    }

    public int get_HapThu_Hp() {
        int par = total_param_item(59, true);
        ItemFashionP2 itF = p.check_fashion(74);
        if (itF != null && itF.is_use) {
            par *= 2;
        }
        return par;
    }

    public long get_def_target_reduce() {
        int par = total_param_item(70, true);
        return par;
    }



    public int getSkillHkChoang() {
       return total_param_item(78, true);
    }

    public int getGiamDame() {
    return total_param_item(54, true);
    }
}
