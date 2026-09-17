package zinterfaces.menus.dymanics;

import bot.mercenary.MercenaryBot;
import bot.mercenary.MercenaryManager;
import bot.mercenary.MercenaryManager.HiredRecord;
import bot.mercenary.MercenaryTemplate;
import bot.mercenary.MercenaryTemplate.MercEntry;
import model.Player;
import core.MenuController;
import core.Log;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.YesNoDialog;
import skill.Skill_Template;
import skill.Skill_info;
import zinterfaces.iMenu;
import zinterfaces.iMenuDymanic;

/**
 * DymanicMercenary — Dynamic Menu hệ thống Lính Đánh Thuê chuẩn xịn.
 * Handles menu IDs: 900, 901, 905, 906, 907.
 */
public class DymanicMercenary implements iMenu {

    public static final short MENU_MAIN = 900;
    public static final short MENU_FACTION_LIST = 901;
    public static final short MENU_BOT_DETAIL = 905;
    public static final short MENU_CONTRACT_LIST = 906;
    public static final short MENU_CONTRACT_ACTION = 907;
    public static final short MENU_SKILL_DETAIL = 941;
    public static final short MENU_STATUS_CHANGE = 908;
    public static final short MENU_UPGRADE_MERCENARY = 910;

    @Override
    public short[] getId() {
        return new short[]{MENU_MAIN, MENU_FACTION_LIST, MENU_BOT_DETAIL, MENU_CONTRACT_LIST, MENU_CONTRACT_ACTION, MENU_SKILL_DETAIL, MENU_STATUS_CHANGE, MENU_UPGRADE_MERCENARY};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        switch (idNPC) {
            case MENU_MAIN:
                handleMercenaryMenu(p, index);
                break;
            case MENU_FACTION_LIST:
                handleFactionListSelect(p, index);
                break;
            case MENU_BOT_DETAIL:
                handleBotDetailMenu(p, index);
                break;
            case MENU_CONTRACT_LIST:
                handleContractSelect(p, index);
                break;
            case MENU_CONTRACT_ACTION:
                handleContractActionMenu(p, index);
                break;
            case MENU_SKILL_DETAIL:
                handleSkillItemSelect(p, index);
                break;
            case MENU_STATUS_CHANGE:
                handleStatusChangeMenu(p, index);
                break;
            case MENU_UPGRADE_MERCENARY:
                handleUpgradeMercenaryMenu(p, index);
                break;
        }
    }

    // ==================================================
    //  MENU CHÍNH (MENU 900)
    // ==================================================

    public static void sendMercenaryMenu(Player p) throws IOException {
        if (p.level < 40) {
            p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 40 trở lên để có thể mở khóa tính năng Thuê Lính Đánh Thuê!");
            return;
        }

        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);
        int activeCount = MercenaryManager.gI().countActive(contracts);
        boolean hasSpawned = MercenaryManager.gI().hasSpawnedBots(p);
        int curStatus = MercenaryManager.gI().getPlayerMercStatus(p);

        String toggleBtnText = hasSpawned 
            ? "Rút Lính Đánh Thuê Về" 
            : (!contracts.isEmpty() ? "Tập Hợp Xuất Chiến Lính" : "Thuê Triệu Hồi Lính Xuất Chiến");

        iMenuDymanic.buildAndSend(p, MENU_MAIN,
            "Lính Đánh Thuê [" + MercenaryBot.STATUS_NAMES[curStatus] + "]",
            new String[]{
                "Hải Quân",
                "Hải Tặc",
                "Quân Cách Mạng",
                "Đổi Trạng Thái Lính [" + MercenaryBot.STATUS_NAMES[curStatus] + "]",
                toggleBtnText,
                "Quản lý hợp đồng",
                "Sa thải toàn bộ",
                "Hướng dẫn"
            },
            new short[]{133, 133, 133, 110, 110, 110, 118, 134}
        );
    }

    public static void handleMercenaryMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: showFactionList(p, MercenaryTemplate.TYPE_HAI_QUAN);  break;
            case 1: showFactionList(p, MercenaryTemplate.TYPE_HAI_TAC);   break;
            case 2: showFactionList(p, MercenaryTemplate.TYPE_CACH_MANG); break;
            case 3: showStatusChangeMenu(p);                              break;
            case 4: MercenaryManager.gI().toggleOrSummonBotsInstant(p);           break;
            case 5: showContractList(p);                                  break;
            case 6: confirmDismissAll(p);                                 break;
            case 7: showGuide(p);                                          break;
        }
    }

    public static void showStatusChangeMenu(Player p) throws IOException {
        int curStatus = MercenaryManager.gI().getPlayerMercStatus(p);
        iMenuDymanic.buildAndSend(p, MENU_STATUS_CHANGE,
            "Trạng Thái Lính [Hiện tại: " + MercenaryBot.STATUS_NAMES[curStatus] + "]",
            new String[]{
                "Đi theo (Chỉ theo sau, không đánh)",
                "Bảo vệ (Bảo vệ chủ trong phạm vi 150px)",
                "Tấn công (Tàn sát quái toàn map)",
                "Về nhà / Rút về (Ẩn lính khỏi map)",
                "Quay lại"
            },
            new short[]{133, 110, 110, 118, 134}
        );
    }

    public static void handleStatusChangeMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                MercenaryManager.gI().setPlayerMercStatus(p, MercenaryBot.STATUS_FOLLOW);
                break;
            case 1:
                MercenaryManager.gI().setPlayerMercStatus(p, MercenaryBot.STATUS_PROTECT);
                break;
            case 2:
                MercenaryManager.gI().setPlayerMercStatus(p, MercenaryBot.STATUS_ATTACK);
                break;
            case 3:
                MercenaryManager.gI().setPlayerMercStatus(p, MercenaryBot.STATUS_HOME);
                break;
            case 4:
                sendMercenaryMenu(p);
                break;
        }
    }

    // ==================================================
    //  DANH SÁCH BOT THEO PHE (MENU 901)
    // ==================================================

    public static void showFactionList(Player p, int faction) throws IOException {
        List<MercEntry> entries = MercenaryManager.gI().getRefreshedList(p, faction);
        if (entries.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Hiện không có lính " + MercenaryTemplate.getFactionName(faction));
            return;
        }

        String[] names = new String[entries.size() + 1];
        for (int i = 0; i < entries.size(); i++) {
            MercEntry e = entries.get(i);
            names[i] = e.getTierStars() + " - " + e.name + " (Lv." + e.minLv + "-" + e.maxLv + ")";
        }
        names[entries.size()] = "Làm mới danh sách Lính Đánh Thuê";

        if (p.data_yesno == null || p.data_yesno.length < 4) {
            p.data_yesno = new int[4];
        }
        p.data_yesno[0] = faction;

        p.getService().openDynamicMenu(MENU_FACTION_LIST,
            MercenaryTemplate.getFactionName(faction) + " (Danh Sách Mới)", names, null);
    }

    // ==================================================
    //  SUB-MENU CHO BOT ĐƯỢC CHỌN (MENU 905)
    // ==================================================

    public static void handleFactionListSelect(Player p, int botIndex) throws IOException {
        if (p.data_yesno == null || p.data_yesno.length < 1) return;
        int faction = p.data_yesno[0];

        List<MercEntry> entries = MercenaryManager.gI().getRefreshedList(p, faction);
        if (botIndex == entries.size()) {
            MercenaryManager.gI().refreshFactionList(p, faction);
            showFactionList(p, faction);
            p.getService().send_box_ThongBao_OK("Đã làm mới danh sách Lính Đánh Thuê xịn thành công!");
            return;
        }
        if (botIndex < 0 || botIndex >= entries.size()) return;

        MercEntry entry = entries.get(botIndex);
        if (p.data_yesno.length < 4) {
            p.data_yesno = new int[4];
        }
        p.data_yesno[1] = entry.id;

        p.getService().openDynamicMenu(MENU_BOT_DETAIL,
            entry.name,
            new String[]{
                "Xem thông tin & Chỉ số",
                "Xem Kỹ năng & Trái Ác Quỷ",
                "Thuê Lính Đánh Thuê",
                "Làm mới danh sách",
                "Quay lại"
            },
            new short[]{110, 155, 133, 116, 118}
        );
    }

    public static void handleBotDetailMenu(Player p, int choice) throws IOException {
        if (p.data_yesno == null || p.data_yesno.length < 2) return;
        int faction = p.data_yesno[0];
        int botId = p.data_yesno[1];
        MercEntry entry = MercenaryManager.gI().getRefreshedEntry(p, faction, botId);
        if (entry == null) return;

        switch (choice) {
            case 0: {
                MercenaryManager.gI().sendMercenaryProfile(p, entry, null);
                break;
            }
            case 1: {
                showBotSkillMenu(p, entry, null);
                break;
            }
            case 2: {
                openHireCurrencyDialog(p, entry);
                break;
            }
            case 3: {
                MercenaryManager.gI().refreshFactionList(p, entry.faction);
                showFactionList(p, entry.faction);
                p.getService().send_box_ThongBao_OK("Đã làm mới danh sách Lính Đánh Thuê xịn thành công!");
                break;
            }
            case 4: {
                showFactionList(p, entry.faction);
                break;
            }
        }
    }

    // ==================================================
    //  DỰNG SKILL LIST VÀ DYNAMIC MENU DỌC
    // ==================================================

    public static List<Skill_Template> getTAQSkillsForFruit(int fruitItemId) {
        List<Skill_Template> list = new ArrayList<>();
        if (fruitItemId <= 0) fruitItemId = 32;
        int id = fruitItemId + 4000;
        int[] skillIndexes;
        switch (id) {
            case 4032: skillIndexes = new int[]{478, 476, 475}; break; // Magma Nham Thạch
            case 4033: skillIndexes = new int[]{480, 479, 477}; break; // Ice Băng Giá
            case 4034: skillIndexes = new int[]{483, 482, 481}; break; // Light Ánh Sáng / Khói
            case 4088: skillIndexes = new int[]{484, 485, 486}; break; // Flame Lửa
            case 4090: skillIndexes = new int[]{514, 513, 512}; break; // Nika Thức Tỉnh
            case 4091: skillIndexes = new int[]{517, 516, 515}; break; // Rumble Sét
            case 4092: skillIndexes = new int[]{523, 522, 519}; break; // Sand Cát
            case 4093: skillIndexes = new int[]{521, 520, 518}; break; // Gura Chấn Động
            case 4160: skillIndexes = new int[]{527, 526, 525, 524}; break; // Phoenix Phượng Hoàng
            case 4161: skillIndexes = new int[]{531, 530, 529, 528}; break; // Dragon Rồng
            case 4219: skillIndexes = new int[]{538, 537, 536}; break; // Bomb Bộc Phá
            case 4220: skillIndexes = new int[]{535, 534, 533}; break; // Spring Lò Xo
            case 4240: skillIndexes = new int[]{542, 541, 539, 540}; break; // Barrier Rào Chắn
            case 4316: skillIndexes = new int[]{548, 547, 546}; break; // Mochi
            case 4317: skillIndexes = new int[]{545, 544, 543}; break; // Yamato
            case 4318: skillIndexes = new int[]{551, 550, 549}; break; // Trọng Lượng
            case 4427: skillIndexes = new int[]{656, 657, 658, 659}; break; // Bóng Tối
            default:   skillIndexes = new int[]{478, 476, 475}; break;
        }

        for (int idx : skillIndexes) {
            Skill_Template temp = Skill_Template.get_temp((short) idx, 0);
            if (temp != null) list.add(temp);
        }
        return list;
    }

    public static List<Skill_info> getMercenarySkills(byte clazz, int skillLv) {
        return getMercenarySkills(clazz, skillLv, 101);
    }

    public static List<Skill_info> getMercenarySkills(byte clazz, int skillLv, int entryId) {
        byte botClazz = (clazz > 0 && clazz <= 5) ? clazz : 1;
        return Player.getFullBotSkills(botClazz, (short) skillLv);
    }

    private static void showBotSkillMenu(Player p, MercEntry entry) throws IOException {
        showBotSkillMenu(p, entry, null);
    }

    private static void showBotSkillMenu(Player p, MercEntry entry, HiredRecord rec) throws IOException {
        if (entry == null && rec != null) {
            entry = rec.entry;
        }
        if (entry == null) return;

        List<Skill_info> skillList = null;
        if (rec != null && rec.bot != null && rec.bot.skill_point != null && !rec.bot.skill_point.isEmpty()) {
            skillList = new ArrayList<>(rec.bot.skill_point);
        } else {
            bot.mercenary.MercenaryBot preview = bot.mercenary.MercenaryBot.createPreview(p, entry);
            if (preview != null && preview.skill_point != null && !preview.skill_point.isEmpty()) {
                skillList = new ArrayList<>(preview.skill_point);
            }
        }

        if (skillList == null || skillList.isEmpty()) {
            int fruitId = MercenaryTemplate.getFruitIdForEntry(entry);
            skillList = Player.getFullBotSkills(entry.clazz, (short) entry.skillLv);
            List<Skill_Template> taq = getTAQSkillsForFruit(fruitId);
            for (Skill_Template st : taq) {
                Skill_info info = new Skill_info();
                info.temp = st;
                info.lvdevil = (byte) Math.max(1, Math.min(5, entry.skillLv / 6));
                skillList.add(info);
            }
        }

        List<String> names = new ArrayList<>();
        List<Integer> icons = new ArrayList<>();
        p.tempSkillList = new ArrayList<>();

        for (Skill_info sk : skillList) {
            if (sk == null || sk.temp == null) continue;

            String prefix = "";
            int lv = (sk.temp.Lv_RQ > 0) ? sk.temp.Lv_RQ : entry.skillLv;

            boolean isDevil = (sk.temp.ID > 2000 
                    || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                    || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                    || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659)
                    || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802)
                    || sk.temp.typeDevil > 0)
                    && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                    && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

            if (isDevil) {
                prefix = "[Ác Quỷ] ";
                lv = sk.lvdevil > 0 ? sk.lvdevil : (sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : 5);
            } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690) {
                prefix = "[Haki] ";
            } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
                prefix = "[Buff Phái] ";
            } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
                prefix = "[Nội tại] ";
            }

            names.add(prefix + sk.temp.name + " (Lv." + lv + ")");
            icons.add((int) sk.temp.idIcon);
            p.tempSkillList.add(sk);
        }

        if (names.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Lính đánh thuê này chưa học kỹ năng nào.");
            return;
        }

        p.getService().send_dynamic_menu_type4(941, 0, "Kỹ năng: " + entry.name, names, icons);
    }

    private static void showContractSkillMenu(Player p, HiredRecord rec) throws IOException {
        showBotSkillMenu(p, rec.entry, rec);
    }

    public static void handleSkillItemSelect(Player p, int index) throws IOException {
        if (p.tempSkillList == null || index < 0 || index >= p.tempSkillList.size()) return;
        Skill_info sk = p.tempSkillList.get(index);
        if (sk == null || sk.temp == null) return;

        boolean isDevil = (sk.temp.ID > 2000 
                || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659)
                || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802)
                || sk.temp.typeDevil > 0)
                && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

        String typeStr = "Chủ động tấn công";
        if (isDevil) {
            typeStr = "Năng lực Trái Ác Quỷ (Devil Fruit)";
        } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690) {
            typeStr = "Bí kỹ Haki Tối Thượng";
        } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
            typeStr = "Kỹ năng Buff Môn Phái";
        } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
            typeStr = "Kỹ năng Bị Động / Nội Tại";
        } else if (sk.temp.typeSkill == 1) {
            typeStr = "Kỹ năng Buff / Hỗ trợ";
        }

        int lv = sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : (sk.lvdevil > 0 ? sk.lvdevil : 20);

        String detail = "" + sk.temp.name + "\n"
            + "- Phan loai: " + typeStr + "\n"
            + "- Cap do chieu: Cap " + lv + "\n"
            + (sk.temp.manaLost > 0 ? ("- Tieu hao: " + sk.temp.manaLost + " MP\n") : "")
            + "- Pham vi tac dung: " + (sk.temp.rangeLan > 0 ? sk.temp.rangeLan + "m" : "Don muc tieu / Ban than") + "\n"
            + "- Mo ta: " 
            + (sk.temp.info != null && !sk.temp.info.isBlank() ? sk.temp.info : "Ky nang chien dau cua Linh Danh Thue.");

        p.getService().send_box_ThongBao_OK(detail);
    }

    // ==================================================
    //  THUÊ LÍNH ĐÁNH THUÊ DIALOGS
    // ==================================================

    private static void openHireCurrencyDialog(Player p, MercEntry entry) throws IOException {
        String[] options = new String[]{
            "Extol",
            "Ruby",
            "Beri",
            "Huy"
        };
        byte[] icons = new byte[]{-1, -1, -1, -1};

        p.setyesNoDialog(new YesNoDialog(p, 902, "Thue: " + entry.name,
            "Chon loai tien ban muon dung de thanh toan:\n" +
            "- Extol: " + ZUtil.number_format(entry.priceExtol) + " (" + entry.durationExtol + "p) [Trai Ba Vuong]\n" +
            "- Ruby:  " + ZUtil.number_format(entry.priceRuby)  + " (" + entry.durationRuby  + "p) [Trai Thuong Cap]\n" +
            "- Beri:  " + ZUtil.number_format(entry.priceBeri) + " (" + entry.durationBeri + "p) [Trai Pho Thong]",
            options, icons, (byte choice) -> {
                try {
                    if (choice < 0 || choice == 3) return;
                    int currency;
                    switch (choice) {
                        case 0: currency = MercenaryTemplate.CURRENCY_EXTOL; break;
                        case 1: currency = MercenaryTemplate.CURRENCY_RUBY;  break;
                        case 2: currency = MercenaryTemplate.CURRENCY_BERI;  break;
                        default: return;
                    }
                    confirmHire(p, entry, currency);
                } catch (Exception e) {
                    Log.error("DymanicMercenary", "Currency selection error: " + e.getMessage());
                }
            }
        ));
        p.getService().startYesNo();
    }

    private static void confirmHire(Player p, MercEntry entry, int currency) throws IOException {
        String price;
        int durationMin;
        switch (currency) {
            case MercenaryTemplate.CURRENCY_EXTOL:
                price = ZUtil.number_format(entry.priceExtol) + " Extol";
                durationMin = entry.durationExtol;
                break;
            case MercenaryTemplate.CURRENCY_RUBY:
                price = ZUtil.number_format(entry.priceRuby) + " Ruby";
                durationMin = entry.durationRuby;
                break;
            default:
                price = ZUtil.number_format(entry.priceBeri) + " Beri";
                durationMin = entry.durationBeri;
                break;
        }

        p.setyesNoDialog(new YesNoDialog(p, 903, "Xac nhan hop dong",
            "Thue " + entry.name + "?\n" +
            "Gia: " + price + "\n" +
            "Thoi gian: " + durationMin + " phut\n\n" +
            "Linh se duoc luu vao danh sach cua ban (toi da 10 linh).\n" +
            "Toi da " + MercenaryManager.MAX_ACTIVE_BOTS + " linh xuat chien ho tong ban than!",
            new String[]{"Dong y", "Huy"}, new byte[]{-1, -1}, (byte choice) -> {
                if (choice == 0) {
                    MercenaryManager.gI().hireMercenary(p, entry, currency);
                }
            }
        ));
        p.getService().startYesNo();
    }

    // ==================================================
    //  QUẢN LÝ HỢP ĐỒNG (MENU 906 & 907)
    // ==================================================

    public static void showContractList(Player p) throws IOException {
        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);

        if (contracts.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Ban chua thue linh danh thue nao!");
            return;
        }

        int activeCount = MercenaryManager.gI().countActive(contracts);
        String[] options = new String[contracts.size()];

        for (int i = 0; i < contracts.size(); i++) {
            HiredRecord r = contracts.get(i);
            String status = r.isActive ? "[XUAT CHIEN]" : "[NGHI NGOI]";
            options[i] = status + " " + r.entry.name + " (" + r.getRemainingMinutes() + "p)";
        }

        p.getService().openDynamicMenu(MENU_CONTRACT_LIST,
            "Hop Dong (" + contracts.size() + "/10 | " + activeCount + "/" + MercenaryManager.MAX_ACTIVE_BOTS + " Xuat Chien)",
            options, null);
    }

    public static void handleContractSelect(Player p, int index) throws IOException {
        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);
        if (index < 0 || index >= contracts.size()) return;

        HiredRecord rec = contracts.get(index);
        if (p.data_yesno == null || p.data_yesno.length < 4) {
            p.data_yesno = new int[4];
        }
        p.data_yesno[2] = index;

        String toggleText = rec.isActive ? "Cho nghỉ ngơi (Rút về đội)" : "Cho xuất chiến (Theo trận)";

        p.getService().openDynamicMenu(MENU_CONTRACT_ACTION,
            rec.entry.name + " (" + (rec.isActive ? "Đang xuất chiến" : "Đang nghỉ ngơi") + ")",
            new String[]{
                toggleText,
                "Xem chỉ số & thông tin",
                "Xem Kỹ năng & Trái Ác Quỷ",
                "Nâng cấp Lính (Level / Tier / TAQ)",
                "Sa thải (Hủy hợp đồng)",
                "Quay lại"
            },
            new short[]{133, 110, 155, 116, 118, 134}
        );
    }

    public static void handleContractActionMenu(Player p, int action) throws IOException {
        if (p.data_yesno == null || p.data_yesno.length < 3) return;
        int contractIndex = p.data_yesno[2];
        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);
        if (contractIndex < 0 || contractIndex >= contracts.size()) return;

        HiredRecord rec = contracts.get(contractIndex);

        switch (action) {
            case 0: {
                MercenaryManager.gI().toggleActiveStatus(p, rec);
                showContractList(p);
                break;
            }
            case 1: {
                MercenaryManager.gI().sendMercenaryProfile(p, rec.entry, rec);
                break;
            }
            case 2: {
                showContractSkillMenu(p, rec);
                break;
            }
            case 3: {
                showUpgradeMercenaryMenu(p, rec);
                break;
            }
            case 4: {
                p.setyesNoDialog(new YesNoDialog(p, 908, "Xác nhận sa thải",
                    "Sa thải " + rec.entry.name + "?\n" +
                    "Thời gian còn lại: " + rec.getRemainingMinutes() + " phút\n" +
                    "Tiền thuê sẽ KHÔNG được hoàn lại!",
                    new String[]{"Sa thải", "Hủy"}, new byte[]{-1, -1}, (byte choice) -> {
                        if (choice == 0) {
                            MercenaryManager.gI().dismissRecord(p, rec);
                        }
                    }
                ));
                p.getService().startYesNo();
                break;
            }
            case 5: {
                showContractList(p);
                break;
            }
        }
    }

    public static void showUpgradeMercenaryMenu(Player p, HiredRecord rec) throws IOException {
        if (rec == null) return;
        int curLv = (rec.bot != null) ? rec.bot.level : rec.entry.maxLv;
        long lvCost = MercenaryTemplate.getUpgradeLevelCostBeri(curLv);
        double curTier = (rec.bot != null && rec.bot.entry != null) ? rec.bot.entry.tier : rec.entry.tier;
        double targetTier = Math.min(4.0, curTier + 1.0);
        long tierCost = MercenaryTemplate.getUpgradeTierCostBeri(targetTier);

        p.getService().openDynamicMenu(MENU_UPGRADE_MERCENARY,
            "Nâng Cấp: " + rec.entry.name + " [Lv." + curLv + " - Tier " + String.format("%.1f", curTier) + "]",
            new String[]{
                "Nâng Cấp +5 Level (" + core.ZUtil.number_format(lvCost) + " Beri)",
                "Nâng Bậc Tier -> " + (targetTier >= 3.5 ? "Thần Thoại (Thần Trang)" : "Hoàn Mỹ") + " (" + core.ZUtil.number_format(tierCost) + " Beri)",
                "Thức Tỉnh Trái Ác Quỷ Cấp 5 (250 Ruby)",
                "Quay lại"
            },
            new short[]{116, 116, 155, 134}
        );
    }

    public static void handleUpgradeMercenaryMenu(Player p, int choice) throws IOException {
        if (p.data_yesno == null || p.data_yesno.length < 3) return;
        int contractIndex = p.data_yesno[2];
        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);
        if (contractIndex < 0 || contractIndex >= contracts.size()) return;

        HiredRecord rec = contracts.get(contractIndex);

        switch (choice) {
            case 0:
                MercenaryManager.gI().upgradeMercenaryLevel(p, rec);
                showUpgradeMercenaryMenu(p, rec);
                break;
            case 1:
                MercenaryManager.gI().upgradeMercenaryTier(p, rec);
                showUpgradeMercenaryMenu(p, rec);
                break;
            case 2:
                MercenaryManager.gI().awakenMercenaryFruit(p, rec);
                showUpgradeMercenaryMenu(p, rec);
                break;
            case 3:
                handleContractSelect(p, contractIndex);
                break;
        }
    }

    private static void confirmDismissAll(Player p) throws IOException {
        List<HiredRecord> contracts = MercenaryManager.gI().getPlayerContracts(p);
        if (contracts.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Ban chua thue linh danh thue nao!");
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 909, "Xac nhan sa thai toan bo",
            "Ban co chac muon sa thai toan bo " + contracts.size() + " linh danh thue?\n" +
            "Toan bo hop dong se bi huy va KHONG hoan tien!",
            new String[]{"Sa thai tat ca", "Huy"}, new byte[]{-1, -1}, (byte choice) -> {
                if (choice == 0) {
                    MercenaryManager.gI().dismissAll(p);
                }
            }
        ));
        p.getService().startYesNo();
    }

    // ==================================================
    //  HƯỚNG DẪN
    // ==================================================

    private static void showGuide(Player p) throws IOException {
        String guide =
            "+== HUONG DAN LINH DANH THUE ==+\n\n" +
            "1. Tong quan he thong:\n" +
            "- Linh danh thue gom 3 Phe: Hai Quan, Hai Tac, Quan Cach Mang.\n" +
            "- Yeu cau nhan vat dat cap do 40 tro len de mo khoa.\n" +
            "- Pham cap trang bi va cap do cua linh bi gioi han boi cap do cua ban.\n\n" +
            "2. Quy dinh hop dong & Xuat chien:\n" +
            "- Moi nguoi choi thue toi da 10 Hop dong linh cung luc.\n" +
            "- Toi da " + MercenaryManager.MAX_ACTIVE_BOTS + " linh duoc phep Xuat chien ho tong ban than.\n" +
            "- Du lieu hop dong luu vao DB, khong bi mat khi offline.\n\n" +
            "3. 4 Che do hanh vi cua Linh:\n" +
            "- Di theo: Chi di theo sau chu nhan, khong tan cong.\n" +
            "- Bao ve: Bao ve chu nhan trong pham vi 150px, danh quai tiep can.\n" +
            "- Tan cong: Ho tro chien dau xung quanh chu nhan (ban kinh 350px).\n" +
            "- Ve nha / Rut ve: An toan bo linh khoi ban do.\n\n" +
            "4. Quyen loi & Loi ich:\n" +
            "- Nguoi choi huong 25% EXP tu quai do linh tieu diet (co phat chenh lech cap > 10 cap).\n" +
            "- Toan bo vat pham, vang roi ra thuoc ve nguoi thue.\n" +
            "- Linh ho tro gay sat thuong (~55% suc tan cong cua ban).\n\n" +
            "5. Cach thanh toan:\n" +
            "- Extol: Thoi gian thue 90-240 phut.\n" +
            "- Ruby: Thoi gian thue 60-180 phut.\n" +
            "- Beri: Thoi gian thue 30-90 phut.\n" +
            "+===============================+";

        p.getService().Help_From_Server((short) 900, guide);
    }
}
