package model;

import map.MapBossInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.Service;
import core.ZUtil;
import core.Manager;
import network.Message;
import mob.Mob;
import template.EffTemplate;
import template.Option;
import skill.Skill_info;

public class Buff {

    public static short getCanonicalBuffId(Skill_info sk_info, short requestedId) {
        if (requestedId >= 4001 && requestedId <= 4080) {
            return requestedId;
        }
        if (sk_info != null && sk_info.temp != null) {
            int id2 = sk_info.temp.ID;
            int idx = sk_info.temp.indexSkillInServer;
            short icon = sk_info.temp.idIcon;

            if (id2 >= 4001 && id2 <= 4080) return (short) id2;
            if (idx >= 4001 && idx <= 4080) return (short) idx;

            // Devil Fruit active buffs (ưu tiên nhận diện chính xác trước)
            if (id2 == 657 || idx == 657 || id2 == 2056 || idx == 2056 || id2 == 2057 || idx == 2057 || icon == 2056 || icon == 2057 || icon == 88) return 2056;
            if (id2 == 793 || idx == 793 || id2 == 5009 || idx == 5009 || icon == 96) return 5009;
            if (id2 == 801 || idx == 801 || id2 == 5017 || idx == 5017 || icon == 99) return 5017;
            if (id2 == 797 || idx == 797 || id2 == 5013 || idx == 5013 || icon == 440) return 5013;
            if (id2 == 3122 || idx == 3122 || id2 == 5022 || idx == 5022 || id2 == 807 || idx == 807 || icon == 3122 || icon == 5022 || icon == 117) return 3122;

            // Sect class buffs (1010..1014)
            if ((id2 >= 487 && id2 <= 491) || (idx >= 487 && idx <= 491) || (id2 >= 946 && id2 <= 954) || (idx >= 946 && idx <= 954) || id2 == 1010 || idx == 1010 || icon == 1010) return 1010;
            if ((id2 >= 492 && id2 <= 496) || (idx >= 492 && idx <= 496) || (id2 >= 966 && id2 <= 974) || (idx >= 966 && idx <= 974) || id2 == 1011 || idx == 1011 || icon == 1011) return 1011;
            if ((id2 >= 497 && id2 <= 501) || (idx >= 497 && idx <= 501) || (id2 >= 956 && id2 <= 964) || (idx >= 956 && idx <= 964) || id2 == 1012 || idx == 1012 || icon == 1012) return 1012;
            if ((id2 >= 502 && id2 <= 506) || (idx >= 502 && idx <= 506) || (id2 >= 976 && id2 <= 984) || (idx >= 976 && idx <= 984) || id2 == 1013 || idx == 1013 || icon == 1013) return 1013;
            if ((id2 >= 507 && id2 <= 511) || (idx >= 507 && idx <= 511) || (id2 >= 986 && id2 <= 994) || (idx >= 986 && idx <= 994) || id2 == 1014 || idx == 1014 || icon == 1014) return 1014;

            // Common buffs (1002, 1003) - không gộp dải 798..806 và 808..816 tránh ăn nhầm buff trái ác quỷ
            if ((id2 >= 310 && id2 <= 314) || (idx >= 310 && idx <= 314) || id2 == 326 || idx == 326 || id2 == 1002 || idx == 1002 || icon == 1002) return 1002;
            if ((id2 >= 315 && id2 <= 319) || (idx >= 315 && idx <= 319) || id2 == 330 || idx == 330 || id2 == 1003 || idx == 1003 || icon == 1003) return 1003;

            // Other Devil Fruit buffs
            if (id2 == 480 || idx == 480 || id2 == 452 || idx == 452 || id2 == 2008 || idx == 2008 || icon == 2008) return 2008;
            if (id2 == 481 || idx == 481 || id2 == 453 || idx == 453 || id2 == 2009 || idx == 2009 || icon == 2009) return 2009;
            if (id2 == 482 || idx == 482 || id2 == 454 || idx == 454 || id2 == 2010 || idx == 2010 || icon == 2010) return 2010;
            if (id2 == 513 || idx == 513 || id2 == 460 || idx == 460 || id2 == 2016 || idx == 2016 || icon == 2016) return 2016;
            if (id2 == 514 || idx == 514 || id2 == 461 || idx == 461 || id2 == 2017 || idx == 2017 || icon == 2017) return 2017;
            if (id2 == 515 || idx == 515 || id2 == 462 || idx == 462 || id2 == 2018 || idx == 2018 || icon == 2018) return 2018;
            if (id2 == 516 || idx == 516 || id2 == 463 || idx == 463 || id2 == 2019 || idx == 2019 || icon == 2019) return 2019;
            if (id2 == 517 || idx == 517 || id2 == 464 || idx == 464 || id2 == 2020 || idx == 2020 || icon == 2020) return 2020;
            if (id2 == 525 || idx == 525 || id2 == 472 || idx == 472 || id2 == 2028 || idx == 2028 || icon == 2028) return 2028;
            if (id2 == 529 || idx == 529 || id2 == 476 || idx == 476 || id2 == 2032 || idx == 2032 || icon == 2032) return 2032;
            if (id2 == 534 || idx == 534 || id2 == 480 || idx == 480 || id2 == 2037 || idx == 2037 || icon == 2037) return 2037;
            if (id2 == 537 || idx == 537 || id2 == 483 || idx == 483 || id2 == 2040 || idx == 2040 || icon == 2040) return 2040;
            if (id2 == 539 || idx == 539 || id2 == 485 || idx == 485 || id2 == 2042 || idx == 2042 || icon == 2042) return 2042;
            if (id2 == 550 || idx == 550 || id2 == 496 || idx == 496 || id2 == 2053 || idx == 2053 || icon == 2053) return 2053;
            if (id2 == 660 || idx == 660 || id2 == 2059 || idx == 2059 || icon == 2059) return 2059;
            if (id2 == 5008 || idx == 5008 || id2 == 2072 || idx == 2072 || icon == 5008 || icon == 2072 || icon == 95) return 5008;
            if (id2 == 2073 || idx == 2073 || icon == 2073) return 2073;

            if (id2 > 0) return (short) id2;
        }
        return requestedId;
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.isCantSkill()) {
            if (p != null && Manager.gI() != null && Manager.gI().debug) {
                System.out.println("[BUFF-DEBUG] " + (p.name != null ? p.name : "?") + " isCantSkill=" + p.isCantSkill() + " isdie=" + p.isdie);
            }
            return;
        }
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        byte size = m2.reader().readByte();
        if (Manager.gI() != null && Manager.gI().debug) {
            System.out.println("[BUFF-DEBUG] " + p.name + " sent buff id=" + id + " cat=" + cat + " size=" + size + " skill_point_size=" + (p.skill_point != null ? p.skill_point.size() : -1));
        }
        short[] id2 = null;
        if (size > 0) {
            id2 = new short[size];
            for (int i = 0; i < size; i++) {
                id2[i] = m2.reader().readShort();
            }
        }
        int time_buff = 0;
        List<Short> list_id = new ArrayList<>();
        List<Integer> list_par = new ArrayList<>();
        Skill_info sk_info = p.get_skill_temp(id);
        if (sk_info == null && p.skill_point != null) {
            short canonical = getCanonicalBuffId(null, id);
            for (int i = 0; i < p.skill_point.size(); i++) {
                Skill_info sk = p.skill_point.get(i);
                if (sk != null && sk.temp != null) {
                    if (sk.temp.ID == id || sk.temp.indexSkillInServer == id || sk.temp.idIcon == id ||
                        getCanonicalBuffId(sk, (short) sk.temp.ID) == canonical ||
                        getCanonicalBuffId(sk, (short) sk.temp.indexSkillInServer) == canonical ||
                        getCanonicalBuffId(sk, sk.temp.idIcon) == canonical) {
                        sk_info = sk;
                        break;
                    }
                }
            }
        }
        
        short canonicalBuffId = getCanonicalBuffId(sk_info, id);
        if (Manager.gI() != null && Manager.gI().debug) {
            System.out.println("[BUFF-DEBUG] sk_info=" + (sk_info != null ? "FOUND(ID=" + sk_info.temp.ID + " idx=" + sk_info.temp.indexSkillInServer + " Lv=" + sk_info.temp.Lv_RQ + ")" : "NULL") + " canonical=" + canonicalBuffId);
        }

        if (sk_info != null && sk_info.temp != null && sk_info.temp.Lv_RQ >= 0) {
            if (sk_info.temp.op != null) {
                for (int j = 0; j < sk_info.temp.op.size(); j++) {
                    template.Option op = sk_info.temp.op.get(j);
                    if (op == null) continue;
                    switch (op.id) {
                        case 32: {
                            int param = op.getParam();
                            if (param <= 60) {
                                time_buff = param * 1000;
                            } else {
                                time_buff = param * 100;
                            }
                            break;
                        }
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                            break;
                        default: {
                            if (op.id >= 0 && op.id < 100) {
                                int opVal = op.getParam();
                                if (sk_info.lvdevil > 0) {
                                    switch (sk_info.lvdevil) {
                                        case 1: opVal = (opVal * 11) / 10; break;
                                        case 2: opVal = (opVal * 125) / 100; break;
                                        case 3: opVal = (opVal * 145) / 100; break;
                                        case 4: opVal = (opVal * 17) / 10; break;
                                        case 5: opVal *= 2; break;
                                    }
                                }
                                list_id.add((short) op.id);
                                list_par.add(opVal);
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (time_buff <= 0 && sk_info != null) {
            if (canonicalBuffId == 2056 || canonicalBuffId == 2057 || canonicalBuffId == 2072 || canonicalBuffId == 2073 || canonicalBuffId == 5008 || canonicalBuffId == 5009 || canonicalBuffId == 5017 || canonicalBuffId == 5013 || canonicalBuffId == 5022 || canonicalBuffId == 3122) {
                time_buff = 30_000;
            } else if (sk_info.temp.timeDelay > 0) {
                time_buff = sk_info.temp.timeDelay;
            } else {
                time_buff = 15_000;
            }
        }
        if (time_buff < 15_000 && canonicalBuffId >= 1010 && canonicalBuffId <= 1014) {
            time_buff = 15_000;
        }
        if (Manager.gI() != null && Manager.gI().debug) {
            System.out.println("[BUFF-DEBUG] time_buff=" + time_buff + " sk_info=" + (sk_info != null ? "OK" : "NULL") + " mp=" + p.mp + " need=" + (sk_info != null ? sk_info.temp.manaLost : 0));
            if (sk_info == null) {
                System.out.println("[BUFF-DEBUG] FAIL: sk_info is null for id=" + id + ". Listing skill_point IDs:");
                if (p.skill_point != null) {
                    for (int dbg = 0; dbg < p.skill_point.size(); dbg++) {
                        Skill_info dbgSk = p.skill_point.get(dbg);
                        if (dbgSk != null && dbgSk.temp != null) {
                            System.out.println("  [" + dbg + "] ID=" + dbgSk.temp.ID + " idx=" + dbgSk.temp.indexSkillInServer + " icon=" + dbgSk.temp.idIcon + " Lv=" + dbgSk.temp.Lv_RQ + " typeSkill=" + dbgSk.temp.typeSkill);
                        }
                    }
                }
            }
        }
        if (sk_info != null && time_buff > 0) {
            if (!p.isSkillReady(sk_info.temp.ID)) {
                if (Manager.gI() != null && Manager.gI().debug) {
                    System.out.println("[BUFF-DEBUG] FAIL: skill " + sk_info.temp.ID + " not ready (cooldown)");
                }
                return;
            }
            if (p.mp < sk_info.temp.manaLost) {
                p.getService().send_box_ThongBao_OK("MP không đủ!");
                return;
            }
            p.applySkillCooldown(sk_info.temp.ID, sk_info.temp);
            p.getService().use_potion(1, -sk_info.temp.manaLost);
            p.getService().pet(p, false);
            p.getService().UpdateInfoMaincharInfo();
            
            for (int i = 0; i < list_id.size(); i++) {
                int effId = list_id.get(i) + 100;
                EffTemplate eff = p.get_eff(effId);
                if (eff == null) {
                    p.add_new_eff(effId, list_par.get(i), time_buff);
                } else {
                    eff.param = list_par.get(i);
                    eff.time = Math.max(eff.time, System.currentTimeMillis() + time_buff);
                }
            }
            p.ability.recalculatePlayerStats(p);

            short iconToSend = (sk_info.temp.idIcon >= 4001 && sk_info.temp.idIcon <= 4080)
                    ? (short) (sk_info.temp.idIcon + 500)
                    : sk_info.temp.idIcon;

            short[] extraEffs = null;
            switch (canonicalBuffId) {
                case 2009: {
                    extraEffs = new short[]{308, 309, 310};
                    break;
                }
                case 2016: {
                    extraEffs = new short[]{341, 342, 343};
                    break;
                }
                case 2037: { // chim ung
                    extraEffs = new short[]{490, 491, 492};
                    p.add_new_eff(6, 1, time_buff);
                    break;
                }
                case 2040: { // bao dom
                    extraEffs = new short[]{659, 660, 661};
                    p.add_new_eff(6, 1, time_buff);
                    break;
                }
            }

            if (p.map != null) {
                p.map.getService().send_buff(p, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, extraEffs);
            } else {
                p.getService().send_buff((byte)1, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, extraEffs);
            }
            if (Manager.gI() != null && Manager.gI().debug) {
                System.out.println("[BUFF-DEBUG] SUCCESS! Sending buff=" + canonicalBuffId + " icon=" + iconToSend + " eff=" + sk_info.get_eff_skills()[0] + " time=" + time_buff + " list_id=" + list_id.size() + " to player " + p.name);
            }

            short idDataEff = 0;
            switch (canonicalBuffId) {
                case 2056:
                    idDataEff = 3;
                    break;
                case 5009:
                    idDataEff = 23;
                    break;
                case 5017:
                    idDataEff = 24;
                    break;
                case 5013:
                    idDataEff = 69;
                    break;
                case 3122:
                case 5022:
                    idDataEff = 3124;
                    break;
            }
            if (idDataEff > 0) {
                if (p.map != null) {
                    p.map.getService().addEffect(p.index_map, idDataEff, time_buff, (byte) 0, (byte) 0);
                    effect.DataEffect.sendDataToZone(p.map, idDataEff);
                } else {
                    p.getService().addEffect(p.index_map, idDataEff, time_buff, (byte) 0, (byte) 0);
                    effect.DataEffect.sendData(p.conn, idDataEff);
                }
            }
            if (p.active_buffs != null) {
                p.active_buffs.removeIf(b -> b != null && b.canonicalBuffId == canonicalBuffId);
                p.active_buffs.add(new ActiveBuff(canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], System.currentTimeMillis() + time_buff, list_id, list_par, extraEffs, idDataEff));
            }
            
//             Handle Than Trang Transform Skill (Skill 3: 4003, 4008, ...)
//            if (canonicalBuffId >= 4001 && canonicalBuffId <= 4080) {
//                if ((canonicalBuffId - 4001) % 5 == 2) { // Skill 3: Transform Buff
//                    ThanTrangConfig.activateTransformation(p);
//                } else if ((canonicalBuffId - 4001) % 5 == 3) { // Skill 4: Stat Buff
//                    int setId = ThanTrangConfig.getSetIdBySkill(canonicalBuffId);
//                    if (setId > 0) {
//                        p.add_new_eff(ThanTrangConfig.getStatBuffId(setId), 1, time_buff);
//                    }
//                }
//            }

            //
            p.send_skill();
            p.update_info_to_all();
            if (canonicalBuffId != 2009 && canonicalBuffId != 2016 && canonicalBuffId != 2037 && canonicalBuffId != 2040 && sk_info.temp.typeSkill == 2
                    && sk_info.temp.nTarget > 1 && p.party != null) {
                int num_party_eff = 1;
                for (int j = 0; j < p.party.list.size(); j++) {
                    Player p0 = p.party.list.get(j);
                    if (p0.conn != null && !p0.name.equals(p.name) && p0.map != null && p0.map.equals(p.map)) {
                        for (int i = 0; i < list_id.size(); i++) {
                            int effId = list_id.get(i) + 100;
                            EffTemplate eff = p0.get_eff(effId);
                            if (eff == null) {
                                p0.add_new_eff(effId, list_par.get(i), time_buff);
                            } else {
                                eff.param = list_par.get(i);
                                eff.time = Math.max(eff.time, System.currentTimeMillis() + time_buff);
                            }
                        }
                        p0.ability.recalculatePlayerStats(p0);
                        p0.getService().send_buff((byte)0, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, null);
                        if (idDataEff > 0) {
                            if (p0.map != null) {
                                p0.map.getService().addEffect(p0.index_map, idDataEff, time_buff, (byte) 0, (byte) 0);
                                effect.DataEffect.sendDataToZone(p0.map, idDataEff);
                            } else {
                                p0.getService().addEffect(p0.index_map, idDataEff, time_buff, (byte) 0, (byte) 0);
                                effect.DataEffect.sendData(p0.conn, idDataEff);
                            }
                        }
                        if (p0.active_buffs != null) {
                            p0.active_buffs.removeIf(b -> b != null && b.canonicalBuffId == canonicalBuffId);
                            p0.active_buffs.add(new ActiveBuff(canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], System.currentTimeMillis() + time_buff, list_id, list_par, null, idDataEff));
                        }
                        p0.update_info_to_all();
                        num_party_eff++;
                    }
                    if (num_party_eff >= sk_info.temp.nTarget) {
                        break;
                    }
                }
            }
            //
            switch (canonicalBuffId) {
                case 1010: { // luffy
                    p.add_new_eff(11, 1, time_buff);
                    if (p.party != null) {
                        for (int j = 0; j < p.party.list.size(); j++) {
                            Player p0 = p.party.list.get(j);
                            if (p0.conn != null && !p0.name.equals(p.name) && p0.map != null && p0.map.equals(p.map)) {
                                p0.add_new_eff(11, 1, time_buff);
                                for (int i = 0; i < list_id.size(); i++) {
                                    int effId = list_id.get(i) + 100;
                                    EffTemplate eff = p0.get_eff(effId);
                                    if (eff == null) {
                                        p0.add_new_eff(effId, list_par.get(i), time_buff);
                                    } else {
                                        eff.param = list_par.get(i);
                                        eff.time = Math.max(eff.time, System.currentTimeMillis() + time_buff);
                                    }
                                }
                                p0.getService().send_buff((byte)0, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, null);
                                if (p0.active_buffs != null) {
                                    p0.active_buffs.removeIf(b -> b != null && b.canonicalBuffId == canonicalBuffId);
                                    p0.active_buffs.add(new ActiveBuff(canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], System.currentTimeMillis() + time_buff, list_id, list_par, null, idDataEff));
                                }
                                p0.update_info_to_all();
                            }
                        }
                    }
                    break;
                }
                case 1011: { // zoro
                    p.add_new_eff(12, 1, time_buff);
                    if (p.party != null) {
                        for (int j = 0; j < p.party.list.size(); j++) {
                            Player p0 = p.party.list.get(j);
                            if (p0.conn != null && !p0.name.equals(p.name) && p0.map != null && p0.map.equals(p.map)) {
                                p0.add_new_eff(12, 1, time_buff);
                                for (int i = 0; i < list_id.size(); i++) {
                                    int effId = list_id.get(i) + 100;
                                    EffTemplate eff = p0.get_eff(effId);
                                    if (eff == null) {
                                        p0.add_new_eff(effId, list_par.get(i), time_buff);
                                    } else {
                                        eff.param = list_par.get(i);
                                        eff.time = Math.max(eff.time, System.currentTimeMillis() + time_buff);
                                    }
                                }
                                p0.getService().send_buff((byte)0, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, null);
                                if (p0.active_buffs != null) {
                                    p0.active_buffs.removeIf(b -> b != null && b.canonicalBuffId == canonicalBuffId);
                                    p0.active_buffs.add(new ActiveBuff(canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], System.currentTimeMillis() + time_buff, list_id, list_par, null, idDataEff));
                                }
                                p0.update_info_to_all();
                            }
                        }
                    }
                    break;
                }
                case 1012: { // sanji
                    p.add_new_eff(13, 1, time_buff);
                    p.getService().Main_char_Info();
                    if (p.party != null) {
                        for (int j = 0; j < p.party.list.size(); j++) {
                            Player p0 = p.party.list.get(j);
                            if (p0.conn != null && !p0.name.equals(p.name) && p0.map != null && p0.map.equals(p.map)) {
                                p0.add_new_eff(13, 1, time_buff);
                                for (int i = 0; i < list_id.size(); i++) {
                                    int effId = list_id.get(i) + 100;
                                    EffTemplate eff = p0.get_eff(effId);
                                    if (eff == null) {
                                        p0.add_new_eff(effId, list_par.get(i), time_buff);
                                    } else {
                                        eff.param = list_par.get(i);
                                        eff.time = Math.max(eff.time, System.currentTimeMillis() + time_buff);
                                    }
                                }
                                p0.getService().send_buff((byte)0, canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], time_buff, list_id, list_par, null);
                                if (p0.active_buffs != null) {
                                    p0.active_buffs.removeIf(b -> b != null && b.canonicalBuffId == canonicalBuffId);
                                    p0.active_buffs.add(new ActiveBuff(canonicalBuffId, iconToSend, sk_info.get_eff_skills()[0], System.currentTimeMillis() + time_buff, list_id, list_par, null, idDataEff));
                                }
                                p0.update_info_to_all();
                            }
                        }
                    }
                    break;
                }
                case 1013: { // nami
                    p.add_new_eff(14, 1, time_buff);
                    break;
                }
                case 1014: { // usop - Bomb hẹn giờ
                    int maxTargets = (sk_info != null && sk_info.temp != null && sk_info.temp.nTarget > 0) ? sk_info.temp.nTarget : 5;
                    int baseStunTime = time_buff > 0 ? time_buff : 4000;

                    // 1. PvP: Choáng người chơi đối địch trong phạm vi 200
                    if (p.map != null && p.map.can_PK && p.map.players != null) {
                        List<Player> targetPlayers = new ArrayList<>();
                        for (int i = 0; i < p.map.players.size(); i++) {
                            Player pNear = p.map.players.get(i);
                            if (pNear != null && !pNear.equals(p) && !pNear.isdie && p.canAttackTargetPlayer(pNear)
                                    && Math.abs(pNear.x - p.x) < 200 && Math.abs(pNear.y - p.y) < 200) {
                                targetPlayers.add(pNear);
                                if (targetPlayers.size() >= maxTargets) {
                                    break;
                                }
                            }
                        }
                        for (Player pTarget : targetPlayers) {
                            int reduce_Eff = Math.min(1000, Math.max(0, pTarget.ability.get_reduce_Eff()));
                            int actualStunTime = (baseStunTime * (1000 - reduce_Eff)) / 1000;
                            if (actualStunTime > 0) {
                                pTarget.add_new_eff(201, 1, actualStunTime);
                                if (p.map != null) {
                                    p.map.getService().send_eff_15_4(p, pTarget.index_map, (byte)0, (short)500);
                                    p.map.getService().send_choang(p, pTarget, actualStunTime);
                                }
                            }
                        }
                    }

                    // 2. PvE: Choáng quái vật trong phạm vi 200
                    if (p.map != null) {
                        List<Mob> listMob = new ArrayList<>();
                        if (p.map.list_mob != null) {
                            for (int i = 0; i < p.map.list_mob.length; i++) {
                                Mob mob = p.map.getMob(p.map.list_mob[i]);
                                if (mob != null && !mob.isdie && Math.abs(mob.x - p.x) < 200 && Math.abs(mob.y - p.y) < 200) {
                                    listMob.add(mob);
                                }
                            }
                        }
                        for (int i = 0; i < MapBossInfo.ENTRY.size(); i++) {
                            for (int k = 0; k < MapBossInfo.ENTRY.get(i).mob.size(); k++) {
                                Mob bMob = MapBossInfo.ENTRY.get(i).mob.get(k);
                                if (bMob != null && bMob.map != null && bMob.map.equals(p.map) && !bMob.isdie
                                        && Math.abs(bMob.x - p.x) < 200 && Math.abs(bMob.y - p.y) < 200) {
                                    listMob.add(bMob);
                                }
                            }
                        }
                        int count = 0;
                        for (Mob targetMob : listMob) {
                            if (count >= maxTargets) {
                                break;
                            }
                            targetMob.isChoang = true;
                            targetMob.timeChoang = System.currentTimeMillis() + baseStunTime;
                            p.map.getService().send_eff_15_4(p, (short) targetMob.index, (byte) 1, (short) 500);
                            p.map.getService().send_choang_mob(p, targetMob, baseStunTime);

                            long dame = p.ability.get_dame(true);
                            targetMob.hp -= dame;
                            if (targetMob.hp <= 0) {
                                targetMob.hp = 0;
                                p.map.die_mob(targetMob);
                            }
                            p.map.getService().send_attack_mob(p, targetMob, (int) dame);
                            count++;
                        }
                    }
                    break;
                }
                case 2059: { // vuong boc pha
                    EffTemplate eff = p.get_eff(18);
                    if (eff == null) {
                        if (3 > ZUtil.random(120)) {
                            p.add_new_eff(18, 280, time_buff);
                        } else {
                            p.add_new_eff(18, 180, time_buff);
                        }
                    } else {
                        eff.time += time_buff;
                        if (3 > ZUtil.random(120)) {
                            eff.param = 280;
                        } else {
                            eff.param = 180;
                        }
                    }
                    break;
                }
            }
        }
    }
}
