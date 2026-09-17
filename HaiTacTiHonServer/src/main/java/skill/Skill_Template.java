package skill;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import template.Option;

public class Skill_Template {

    // skill id buff 10 trung doc, 11 bat tu, 12 crit lien tuc
    // 1 choang, 2 chay mau
    public static List<Skill_Template> ENTRYS;

    public int ID;
    public int indexSkillInServer;
    public short idIcon;
    public byte typeSkill;
    public byte typeBuff;
    public String name;
    public short range;
    public short typeEffSkill;
    public List<Option> op;
    public byte idEffSpec;
    public short perEffSpec;
    public short timeEffSpec;
    public byte Lv_RQ;
    public byte nTarget;
    public short rangeLan;
    public int damage;
    public short manaLost;
    public int timeDelay;
    public byte nKick;
    public String info;
    public byte typeDevil;
    public int percentDame = 100;

    public String getInfo(byte level, int clazz) {
        if (info == null) return "";
        String result = info;
        switch (clazz) {
            case 2: {
                result = result.replace("Quả đấm tốc độ", "Nhất kiếm");
                break;
            }
            case 3: {
                result = result.replace("Quả đấm tốc độ", "Hắc cước");
                break;
            }
            case 4: {
                result = result.replace("Quả đấm tốc độ", "Gậy chong chóng");
                break;
            }
            case 5: {
                result = result.replace("Quả đấm tốc độ", "Double Shot");
                break;
            }
        }
        if (level > 0) {
            Matcher matcher = Pattern.compile("(\\d+)\\s*%\\s*sát\\s*thương\\s*của\\s*chiêu", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(result);
            if (!matcher.find()) {
                matcher = Pattern.compile("(\\d+)\\s*%\\s*sát\\s*thương", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(result);
                if (!matcher.find()) {
                    Matcher allM = Pattern.compile("(\\d+)\\s*%").matcher(result);
                    int lastStart = -1, lastEnd = -1;
                    String lastGroup = null;
                    while (allM.find()) {
                        lastStart = allM.start(1);
                        lastEnd = allM.end(1);
                        lastGroup = allM.group(1);
                    }
                    if (lastGroup != null) {
                        try {
                            int value = Integer.parseInt(lastGroup);
                            switch (level) {
                                case 1: value = (value * 11) / 10; break;
                                case 2: value = (value * 125) / 100; break;
                                case 3: value = (value * 145) / 100; break;
                                case 4: value = (value * 17) / 10; break;
                                case 5: value *= 2; break;
                            }
                            result = result.substring(0, lastStart) + value + result.substring(lastEnd);
                        } catch (Exception ignored) {}
                    }
                    matcher = null;
                }
            }
            if (matcher != null) {
                String percentStr = matcher.group(1);
                try {
                    int value = Integer.parseInt(percentStr);
                    switch (level) {
                        case 1: value = (value * 11) / 10; break;
                        case 2: value = (value * 125) / 100; break;
                        case 3: value = (value * 145) / 100; break;
                        case 4: value = (value * 17) / 10; break;
                        case 5: value *= 2; break;
                    }
                    result = result.substring(0, matcher.start(1)) + value + result.substring(matcher.end(1));
                } catch (Exception ignored) {}
            }
        }
        return result;
    }

    public Skill_Template(int Index, int Id, short IdImage, byte type, byte typeBuff, String name, short typeEff,
            short range) {
        this.indexSkillInServer = Index;
        this.ID = (int) Id;
        this.idIcon = IdImage;
        this.typeSkill = type;
        this.typeBuff = typeBuff;
        this.name = name;
        this.range = range;
        this.typeEffSkill = typeEff;
    }

    public void getData(byte nTarget, short rangeLan, int Damage, short Manacost, int CoolDown, byte nkick,
            String Description, byte LvCur, byte typeDevil) {
        this.nTarget = nTarget;
        this.rangeLan = rangeLan;
        this.damage = Damage;
        this.manaLost = Manacost;
        this.timeDelay = CoolDown;
        this.nKick = nkick;
        this.info = Description;
        this.Lv_RQ = LvCur;
        this.typeDevil = typeDevil;
    }

    public static Skill_Template get_temp(int index, long exp) {
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp = Skill_Template.ENTRYS.get(i);
            if (temp.indexSkillInServer == index) {
                if (exp == -1 && temp.Lv_RQ == -1) {
                    return temp;
                } else if (exp > -1 && temp.Lv_RQ > -1) {
                    return temp;
                }
            }
        }
        return null;
    }

    public static Skill_Template get_temp_by_id(int id) {
        if (Skill_Template.ENTRYS == null) return null;
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp = Skill_Template.ENTRYS.get(i);
            if (temp.ID == id || temp.indexSkillInServer == id) {
                return temp;
            }
        }
        return null;
    }

    public static boolean upgrade_skill(Skill_info sk_info, byte clazz) {
        Skill_Template result = null;
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
            if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                switch (clazz) {
                    case 1: {
                        if (temp_ss.indexSkillInServer >= 0 && temp_ss.indexSkillInServer < 60
                                || temp_ss.indexSkillInServer >= 375 && temp_ss.indexSkillInServer < 395
                                || temp_ss.indexSkillInServer >= 566 && temp_ss.indexSkillInServer <= 583
                                || temp_ss.indexSkillInServer >= 703 && temp_ss.indexSkillInServer <= 707
                                || temp_ss.indexSkillInServer >= 708 && temp_ss.indexSkillInServer <= 712
                                || temp_ss.indexSkillInServer >= 713 && temp_ss.indexSkillInServer <= 717) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 2: {
                        if (temp_ss.indexSkillInServer >= 60 && temp_ss.indexSkillInServer < 120
                                || temp_ss.indexSkillInServer >= 395 && temp_ss.indexSkillInServer < 415
                                || temp_ss.indexSkillInServer >= 584 && temp_ss.indexSkillInServer <= 601
                                || temp_ss.indexSkillInServer >= 718 && temp_ss.indexSkillInServer <= 722
                                || temp_ss.indexSkillInServer >= 723 && temp_ss.indexSkillInServer <= 727
                                || temp_ss.indexSkillInServer >= 728 && temp_ss.indexSkillInServer <= 732) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 3: {
                        if (temp_ss.indexSkillInServer >= 120 && temp_ss.indexSkillInServer < 180
                                || temp_ss.indexSkillInServer >= 415 && temp_ss.indexSkillInServer < 435
                                || temp_ss.indexSkillInServer >= 602 && temp_ss.indexSkillInServer <= 619
                                || temp_ss.indexSkillInServer >= 733 && temp_ss.indexSkillInServer <= 737
                                || temp_ss.indexSkillInServer >= 738 && temp_ss.indexSkillInServer <= 742
                                || temp_ss.indexSkillInServer >= 743 && temp_ss.indexSkillInServer <= 747) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 4: {
                        if (temp_ss.indexSkillInServer >= 180 && temp_ss.indexSkillInServer < 240
                                || temp_ss.indexSkillInServer >= 435 && temp_ss.indexSkillInServer < 455
                                || temp_ss.indexSkillInServer >= 620 && temp_ss.indexSkillInServer <= 637
                                || temp_ss.indexSkillInServer >= 748 && temp_ss.indexSkillInServer <= 752
                                || temp_ss.indexSkillInServer >= 753 && temp_ss.indexSkillInServer <= 757
                                || temp_ss.indexSkillInServer >= 758 && temp_ss.indexSkillInServer <= 762) {
                            result = temp_ss;
                        }
                        break;
                    }
                    case 5: {
                        if (temp_ss.indexSkillInServer >= 240 && temp_ss.indexSkillInServer < 300
                                || temp_ss.indexSkillInServer >= 455 && temp_ss.indexSkillInServer < 475
                                || temp_ss.indexSkillInServer >= 638 && temp_ss.indexSkillInServer <= 655
                                || temp_ss.indexSkillInServer >= 763 && temp_ss.indexSkillInServer <= 767
                                || temp_ss.indexSkillInServer >= 768 && temp_ss.indexSkillInServer <= 772
                                || temp_ss.indexSkillInServer >= 773 && temp_ss.indexSkillInServer <= 777) {
                            result = temp_ss;
                        }
                        break;
                    }
                }
                if (result != null) {
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 661 && sk_info.temp.indexSkillInServer <= 665) { // skill
                                                                                                                  // dial
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 672 && sk_info.temp.indexSkillInServer <= 678) { // skill
                                                                                                                  // ba
                                                                                                                  // vuong
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 679 && sk_info.temp.indexSkillInServer <= 684) { // skill
                                                                                                                  // vu
                                                                                                                  // trang
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 685 && sk_info.temp.indexSkillInServer <= 690) { // skill
                                                                                                                  // quan
                                                                                                                  // sat
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 697 && sk_info.temp.indexSkillInServer <= 702) { // skill
                                                                                                                  // hai
                                                                                                                  // quan
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 691 && sk_info.temp.indexSkillInServer <= 696) { // skill hai tac
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result == null && sk_info.temp.indexSkillInServer >= 803 && sk_info.temp.indexSkillInServer <= 808) { // skill cach mang
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                Skill_Template temp_ss = Skill_Template.ENTRYS.get(i);
                if (sk_info.temp.ID == temp_ss.ID && temp_ss.Lv_RQ == (sk_info.temp.Lv_RQ + 1)) {
                    result = temp_ss;
                    break;
                }
            }
        }
        if (result != null && result.Lv_RQ > 0) {
            if (result.Lv_RQ > 30) {
                return false;
            } else {
                sk_info.temp = result;
                return true;
            }
        }
        return false;
    }

    public static boolean learn_skill(Skill_info sk_info) {
        if (sk_info.temp.Lv_RQ == -1) {
            Skill_Template result = null;
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                if (sk_info.temp.indexSkillInServer == Skill_Template.ENTRYS.get(i).indexSkillInServer
                        && sk_info.temp.ID == Skill_Template.ENTRYS.get(i).ID
                        && Skill_Template.ENTRYS.get(i).Lv_RQ == 1) {
                    result = Skill_Template.ENTRYS.get(i);
                    break;
                }
            }
            if (result != null) {
                sk_info.temp = result;
                sk_info.exp = 0;
                return true;
            }
        } else {
            Skill_Template result = null;
            for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                if (sk_info.temp.indexSkillInServer == (Skill_Template.ENTRYS.get(i).indexSkillInServer - 1)) {
                    result = Skill_Template.ENTRYS.get(i);
                    break;
                }
            }
            if (result != null) {
                sk_info.temp = result;
                sk_info.exp = 0;
                return true;
            }
        }
        return false;
    }

    public static void reset_skill(Skill_info sk_info) {
        if (sk_info.temp.Lv_RQ == -1) {
            return;
        }
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            if (sk_info.temp.ID == Skill_Template.ENTRYS.get(i).ID && Skill_Template.ENTRYS.get(i).Lv_RQ == -1) {
                sk_info.temp = Skill_Template.ENTRYS.get(i);
                sk_info.exp = -1;
                break;
            }
        }
    }

    public short getTypeEffSkill() {
        return typeEffSkill;
    }

    public static Skill_Template get_nextHk(String name, int old) {
        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
            Skill_Template temp = Skill_Template.ENTRYS.get(i);
            if (temp.name.equals(name) && temp.Lv_RQ == (old + 1)) {
                return temp;
            }
        }
        return null;
    }
}
