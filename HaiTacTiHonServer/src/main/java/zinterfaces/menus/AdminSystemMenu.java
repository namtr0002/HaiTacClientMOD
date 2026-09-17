package zinterfaces.menus;

import activities.TimedDungeonManager;
import model.DeTu;
import model.Player;
import model.Menu;
import core.Manager;
import core.MenuController;
import core.ZUtil;
import core.Log;
import event.EventManager;
import event.SuKienHalloween;
import map.Zone;
import map.zones.BaoVePhaoDai;
import map.zones.DauTruongTuDo;
import map.zones.WorldWar;
import boss.BossTheGioi;
import boss.BossAndWildManager;
import boss.BossAndWildManager.TrackedEntityDTO;
import boss.SuperBossManager;
import boss.BossPica;
import zabstracts.AbsBoss;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import network.Message;
import network.SessionManager;
import skill.Skill_info;
import skill.Skill_Template;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.Item_wear;
import itemz.Rebuild_Item;
import itemz.Item;
import template.Level;
import template.Option;
import zinterfaces.iMenu;
import zinterfaces.iMenuDymanic;
import model.Quest;
import template.QuestP;

import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.util.*;

/**
 * AdminSystemMenu  Hệ Thống Quản Lý Thẻ Hệ Thống (Admin Control Center).
 * Tự động đăng ký qua interface iMenu trong zinterfaces.menus.
 */
public class AdminSystemMenu implements iMenu {

    public static final short MENU_MAIN                 = 8720;
    public static final short MENU_TIMED_DUNGEON_LIST    = 8721;
    public static final short MENU_TIMED_DUNGEON_DETAIL  = 8722;
    public static final short MENU_BOT_MANAGEMENT        = 8723;
    public static final short MENU_BOT_LIST_DETAIL       = 8724;
    public static final short MENU_SWITCH_CLAZZ          = 8725;
    public static final short MENU_DISCIPLE_MANAGEMENT   = 8726;
    public static final short MENU_TELEPORT              = 8727;
    public static final short MENU_BOSS_EVENT            = 8728;
    public static final short MENU_PLAYER_MANAGEMENT     = 8729;
    public static final short MENU_ADD_ITEM_CATEGORY     = 8730;
    public static final short MENU_ITEM_872_ROOT        = 8731;
    public static final short MENU_DISCIPLE_STATUS      = 8732;
    public static final short MENU_SWITCH_CLAZZ_TYPE     = 8733;
    public static final short MENU_BUFF_DEVIL_FRUIT      = 8734;
    public static final short MENU_PLAYER_SKILL_INSPECT  = 8735;
    public static final short MENU_CLEAN_THAN_TRANG      = 8736;
    public static final short MENU_GIVE_THAN_TRANG_SET   = 8737;
    public static final short MENU_BOSS_LOCATION_LIST    = 8738;
    public static final short MENU_SPAWN_WILD_DETU       = 8739;
    public static final short MENU_SPAWN_SUPER_BOSS         = 8788;
    public static final short MENU_SPAWN_NORMAL_BOSS        = 8789;
    public static final short MENU_ENTITY_ACTION_DETAIL     = 8790;
    public static final short MENU_OTHER_EVENTS             = 8791;
    public static final short MENU_SELECT_SUPER_BOSS_SPAWN  = 8792;
    public static final short MENU_SUPER_BUFF_ROOT          = 8770;
    public static final short MENU_ALL_BUFF_TARGET_PICKER    = 8771;
    public static final short MENU_DEVIL_SKILL_TARGET_PICKER = 8772;
    public static final short MENU_EAT_DEVIL_SELECT        = 8773;
    public static final short MENU_DEVIL_FRUIT_PAGE2       = 8774;
    public static final short MENU_EAT_DEVIL_LEVEL         = 8775;
    public static final short MENU_KHAM_DA_BODY            = 8776;
    public static final short MENU_KHAM_DA_CAT             = 8777;
    public static final short MENU_KHAM_DA_DETAIL          = 8778;
    public static final short MENU_SMART_KHAM_DA_SELECT    = 8779;
    public static final short MENU_SMART_KHAM_DA_SLOT      = 8780;
    public static final short MENU_KICH_AN_SELECT          = 8781;
    public static final short MENU_KICH_AN_TARGET_SLOT     = 8782;
    public static final short MENU_QUEST_ACTION             = 8783;
    public static final short MENU_QUEST_TARGET_PICKER      = 8784;
    public static final short MENU_TARGET_SELECT           = 8785;
    public static final short MENU_ONLINE_PLAYER_LIST      = 8786;
    public static final short MENU_ACTION_ONLINE_PLAYER_LIST = 8787;
    public static final short MENU_FULL_SET_ROOT          = 8801;
    public static final short MENU_FULL_SET_BUILD         = 8802;
    public static final short MENU_FULL_SET_CLAZZ         = 8803;
    public static final short MENU_SPAWN_HERE_ROOT        = 8804;
    public static final short MENU_SPAWN_HERE_DETU        = 8805;
    public static final short MENU_SPAWN_HERE_SUPER_BOSS  = 8806;
    public static final short MENU_FULL_SET_LEVEL         = 8807;
    public static final short MENU_FULL_SET_GEM_TYPE      = 8808;
    public static final short MENU_THAN_TRANG_PAGE1       = 8809;
    public static final short MENU_THAN_TRANG_PAGE2       = 8810;
    public static final short MENU_THAN_TRANG_ACTION      = 8811;
    public static final short MENU_KICH_AN_TIM_DIAL       = 8812;
    public static final short MENU_BUFF_SKILLS_ADVANCED   = 8813;
    public static final short MENU_ACTIVE_BUFF_SELECT     = 8814;
    public static final short MENU_SMART_KHAM_ALL_GEMS    = 8815;
    public static final short MENU_DISCIPLE_AND_MERCENARY = 8816;

    private static final Map<Integer, Integer> playerSetScope = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Integer, Integer> playerSetBuild = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Integer, Integer> playerSetLevel = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Integer, Integer> playerChosenGem = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Integer, Integer> playerSelectedThanTrangSet = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Integer, String> playerItBodyBackup = new java.util.concurrent.ConcurrentHashMap<>();

    public static final int[] EQUIP_LEVELS = new int[]{1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110};

    public static final int[] GEM_PRESET_IDS = new int[]{
        647, // 0: Cẩm thạch Thần Thoại (Công cực đại)
        652, // 1: Topaz Thần Thoại (Thủ cực đại)
        657, // 2: Ruby Thần Thoại (Máu cực đại)
        73,  // 3: Saphia Cấp 6 (Né tránh & Tốc độ)
        651, // 4: Thạch Anh Thần Thoại (Chí mạng & Xuyên giáp)
        67,  // 5: Lục Bảo Cấp 6 (Sát thương chuẩn)
        367, // 6: Hổ Phách Cấp 6 (Kháng hiệu ứng)
        226, // 7: Hải Thạch Cấp 6 (Bền bỉ)
        241, // 8: Đá Siêu Cấp (Tổng hợp)
        677, // 9: Đá Thần Thoại (Toàn năng)
        326, // 10: Đá Khảm Vô Cực S
        910  // 11: Đá Khảm Siêu Cấp 910
    };
    public static final String[] GEM_PRESET_NAMES = new String[]{
        "Cẩm Thạch Thần Thoại (Max Công)",
        "Topaz Thần Thoại (Max Thủ)",
        "Ruby Thần Thoại (Max HP Máu)",
        "Saphia Cấp 6 (Max Né Tránh & Tốc)",
        "Thạch Anh Thần Thoại (Max Bạo & Xuyên Giáp)",
        "Lục Bảo Cấp 6 (Max Sát Thương Chuẩn)",
        "Hổ Phách Cấp 6 (Max Kháng Hiệu Ứng)",
        "Hải Thạch Cấp 6 (Max Kháng Vật & Phép)",
        "Đá Siêu Cấp (Tổng Hợp)",
        "Đá Thần Thoại Cực Hạn (Siêu Chỉ Số)",
        "Đá Khảm Vô Cực S (ID 326)",
        "Đá Khảm Siêu Cấp (ID 910)"
    };

    private static final int[][] CLASS_EQUIP_TEMPLATES = new int[][]{
        {2119, 2149, 2165, 2129, 2161, 2139},
        {2121, 2151, 2165, 2131, 2161, 2141},
        {2123, 2153, 2165, 2133, 2161, 2143},
        {2125, 2155, 2165, 2135, 2161, 2145},
        {2127, 2157, 2165, 2137, 2161, 2147}
    };
    private static final int ITEM_ID_HEART = 11000;
    private static final int ITEM_ID_DIAL = 12017;
    private static final int[] ITEM_IDS_THAN_TRANG = new int[]{2688, 2689, 2690, 2691, 2692, 2693};

    @Override
    public short[] getId() {
        return new short[]{
            MENU_MAIN,
            MENU_TIMED_DUNGEON_LIST,
            MENU_TIMED_DUNGEON_DETAIL,
            MENU_BOT_MANAGEMENT,
            MENU_BOT_LIST_DETAIL,
            MENU_SWITCH_CLAZZ,
            MENU_DISCIPLE_MANAGEMENT,
            MENU_TELEPORT,
            MENU_BOSS_EVENT,
            MENU_PLAYER_MANAGEMENT,
            MENU_ADD_ITEM_CATEGORY,
            MENU_ITEM_872_ROOT,
            MENU_DISCIPLE_STATUS,
            MENU_SWITCH_CLAZZ_TYPE,
            MENU_BUFF_DEVIL_FRUIT,
            MENU_PLAYER_SKILL_INSPECT,
            MENU_CLEAN_THAN_TRANG,
            MENU_GIVE_THAN_TRANG_SET,
            MENU_BOSS_LOCATION_LIST,
            MENU_SPAWN_WILD_DETU,
            MENU_SPAWN_SUPER_BOSS,
            MENU_SPAWN_NORMAL_BOSS,
            MENU_ENTITY_ACTION_DETAIL,
            MENU_OTHER_EVENTS,
            MENU_SELECT_SUPER_BOSS_SPAWN,
            MENU_SUPER_BUFF_ROOT,
            MENU_ALL_BUFF_TARGET_PICKER,
            MENU_DEVIL_SKILL_TARGET_PICKER,
            MENU_EAT_DEVIL_SELECT,
            MENU_DEVIL_FRUIT_PAGE2,
            MENU_EAT_DEVIL_LEVEL,
            MENU_KHAM_DA_BODY,
            MENU_KHAM_DA_CAT,
            MENU_KHAM_DA_DETAIL,
            MENU_SMART_KHAM_DA_SELECT,
            MENU_SMART_KHAM_DA_SLOT,
            MENU_KICH_AN_SELECT,
            MENU_KICH_AN_TARGET_SLOT,
            MENU_TARGET_SELECT,
            MENU_ONLINE_PLAYER_LIST,
            MENU_QUEST_ACTION,
            MENU_QUEST_TARGET_PICKER,
            MENU_ACTION_ONLINE_PLAYER_LIST,
            MENU_DISCIPLE_AND_MERCENARY,
            MENU_FULL_SET_ROOT,
            MENU_FULL_SET_BUILD,
            MENU_FULL_SET_CLAZZ,
            MENU_SPAWN_HERE_ROOT,
            MENU_SPAWN_HERE_DETU,
            MENU_SPAWN_HERE_SUPER_BOSS,
            MENU_FULL_SET_LEVEL,
            MENU_FULL_SET_GEM_TYPE,
            MENU_THAN_TRANG_PAGE1,
            MENU_THAN_TRANG_PAGE2,
            MENU_THAN_TRANG_ACTION,
            MENU_KICH_AN_TIM_DIAL,
            MENU_BUFF_SKILLS_ADVANCED,
            MENU_ACTIVE_BUFF_SELECT,
            MENU_SMART_KHAM_ALL_GEMS
        };
    }

    public static boolean isAdmin(Player p) {
        if (p == null || p.isClosed) return false;
        if (p.admin == 1) return true;
        if (p.conn != null) {
            if (p.conn.role == 1 || p.conn.is_admin == 1) {
                p.admin = 1;
                return true;
            }
            if ("admin".equalsIgnoreCase(p.conn.user) || "ad".equalsIgnoreCase(p.conn.user)) {
                p.admin = 1;
                return true;
            }
        }
        if ("admin".equalsIgnoreCase(p.name) || "ad".equalsIgnoreCase(p.name)) {
            p.admin = 1;
            return true;
        }
        // Fallback DB check
        try {
            int accId = (p.conn != null) ? p.conn.idUser : 0;
            if (accId <= 0) {
                try (Connection c = DbManager.gI().getConnect();
                     PreparedStatement ps = c.prepareStatement("SELECT `account_id` FROM `players` WHERE `name` = ? LIMIT 1")) {
                    ps.setString(1, p.name);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) accId = rs.getInt("account_id");
                    }
                }
            }
            if (accId > 0) {
                try (Connection c = DbManager.gI().getConnect();
                     PreparedStatement ps = c.prepareStatement("SELECT `role`, `is_admin` FROM `account` WHERE `id` = ? LIMIT 1")) {
                    ps.setInt(1, accId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int r = rs.getInt("role");
                            int a = rs.getInt("is_admin");
                            if (r == 1 || a == 1) {
                                p.admin = 1;
                                if (p.conn != null) {
                                    p.conn.role = r;
                                    p.conn.is_admin = a;
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static void openItem872Menu(Player p) throws IOException {
        if (p == null) return;
        if (isAdmin(p)) {
            openMain(p, MENU_ITEM_872_ROOT);
        } else {
            iMenuDymanic.buildAndSend(p, MENU_ITEM_872_ROOT,
                "THẺ HỆ THỐNG - TIỆN ÍCH",
                new String[]{
                    "Cài Đặt Hệ Thống Auto",
                    "Dọn Dẹp Hành Trang Ngay (1 Chạm)",
                    "Quản Lý Đệ Tử",
                    "Quản Lý Lính Đánh Thuê",
                    "Menu Thần Trang",
                    "Sổ Tay & Hướng Dẫn Tính Năng",
                    "Đóng"
                },
                new short[]{110, 110, 155, 133, 154, 118, 134}
            );
        }
    }

    private void handleItem872Root(Player p, int index) throws IOException {
        if (isAdmin(p)) {
            handleMainMenu(p, index);
            return;
        }
        switch (index) {
            case 0:
                AutoSystemMenu.openAutoMainMenu(p);
                break;
            case 1:
                AutoSystemMenu.openCleanPromptMenu(p);
                break;
            case 2:
                if (p.detu == null && (p.nameDe == null || p.nameDe.isBlank())) {
                    if (bot.mercenary.MercenaryManager.gI().hasAnyContracts(p)) {
                        openDiscipleAndMercenaryMenu(p);
                        return;
                    }
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                    return;
                }
                openDiscipleManagement(p);
                break;
            case 3:
                zinterfaces.menus.dymanics.DymanicMercenary.sendMercenaryMenu(p);
                break;
            case 4:
                zinterfaces.menus.npcs.NpcJohny.openMenuThanTrang(p);
                break;
            case 5:
                p.getService().send_box_ThongBao_OK(
                    "THẺ HỆ THỐNG - HƯỚNG DẪN TIỆN ÍCH:\n" +
                    "- Cài Đặt Auto: Bán/xóa trang bị rác theo màu/cấp, tự động ghép đá, ghép nguyên liệu, mở rương.\n" +
                    "- Dọn Dẹp Hành Trang: Quét và giải phóng ô trống nhanh chóng theo cài đặt.\n" +
                    "- Quản Lý Đệ Tử & Lính Đánh Thuê: Chăm sóc, nâng cấp và điều khiển trợ thủ.\n" +
                    "- Thần Trang: Xem và nâng cấp trang bị thần trang tại Johny.\n" +
                    "* Lưu ý: Cường hóa trang bị vui lòng tới gặp Thợ Rèn Johny theo đúng lối chơi game!"
                );
                break;
            case 6:
                break;
        }
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == MENU_ITEM_872_ROOT) {
            handleItem872Root(p, index);
            return;
        }

        if (idNPC == MENU_DISCIPLE_AND_MERCENARY) {
            handleDiscipleAndMercenaryMenu(p, index);
            return;
        }

        if (idNPC == MENU_DISCIPLE_MANAGEMENT || idNPC == MENU_DISCIPLE_STATUS) {
            handleDiscipleManagement(p, index);
            return;
        }

        if (!isAdmin(p)) {
            p.getService().send_box_ThongBao_OK("Chỉ Admin mới có quyền hạn sử dụng chức năng này!");
            return;
        }

        switch (idNPC) {
            case MENU_MAIN:                      handleMainMenu(p, index); break;
            case MENU_SUPER_BUFF_ROOT:           handleSuperBuffRootMenu(p, index); break;
            case MENU_BUFF_DEVIL_FRUIT:          handleBuffDevilFruitMenu(p, index); break;
            case MENU_TIMED_DUNGEON_LIST:        handleTimedDungeonList(p, index); break;
            case MENU_TIMED_DUNGEON_DETAIL:      handleTimedDungeonDetail(p, index); break;
            case MENU_BOT_MANAGEMENT:            handleBotManagement(p, index); break;
            case MENU_BOT_LIST_DETAIL:           handleBotSelectDetail(p, index); break;
            case MENU_SWITCH_CLAZZ:              handleSwitchClazz(p, index); break;
            case MENU_SWITCH_CLAZZ_TYPE:         handleSwitchClazzType(p, index); break;
            case MENU_DISCIPLE_MANAGEMENT:       handleDiscipleManagement(p, index); break;
            case MENU_TELEPORT:                  handleTeleport(p, index); break;
            case MENU_BOSS_EVENT:                handleBossEvent(p, index); break;
            case MENU_PLAYER_MANAGEMENT:         handlePlayerManagement(p, index); break;
            case MENU_ADD_ITEM_CATEGORY:         handleAddItemCategory(p, index); break;
            case MENU_PLAYER_SKILL_INSPECT:      handlePlayerSkillInspectSelect(p, index); break;
            case MENU_CLEAN_THAN_TRANG:          handleCleanThanTrangMenu(p, index); break;
            case MENU_GIVE_THAN_TRANG_SET:       handleGiveThanTrangSetMenu(p, index); break;
            case MENU_OTHER_EVENTS:              handleOtherEventsMenu(p, index); break;
            case MENU_EAT_DEVIL_SELECT:          handleEatDevilSelect(p, index); break;
            case MENU_DEVIL_FRUIT_PAGE2:         handleEatDevilSelectPage2(p, index); break;
            case MENU_EAT_DEVIL_LEVEL:           handleEatDevilLevel(p, index); break;
            case MENU_KHAM_DA_BODY:              handleKhamDaBody(p, index); break;
            case MENU_KHAM_DA_CAT:               handleKhamDaCat(p, index); break;
            case MENU_KHAM_DA_DETAIL:            handleKhamDaDetail(p, index); break;
            case MENU_SMART_KHAM_DA_SELECT:      handleSmartKhamDaSelect(p, index); break;
            case MENU_SMART_KHAM_DA_SLOT:        handleSmartKhamDaSlot(p, index); break;
            case MENU_KICH_AN_SELECT:            handleKichAnSelect(p, index); break;
            case MENU_KICH_AN_TARGET_SLOT:       handleKichAnTargetSlot(p, index); break;
            case MENU_TARGET_SELECT:             handleTargetSelect(p, index); break;
            case MENU_ONLINE_PLAYER_LIST:        handleOnlinePlayerList(p, index); break;
            case MENU_ALL_BUFF_TARGET_PICKER:    handleAllBuffTargetPicker(p, index); break;
            case MENU_DEVIL_SKILL_TARGET_PICKER: handleDevilSkillsTargetPicker(p, index); break;
            case MENU_QUEST_ACTION:              handleQuestAction(p, index); break;
            case MENU_QUEST_TARGET_PICKER:       handleQuestTargetPicker(p, index); break;
            case MENU_ACTION_ONLINE_PLAYER_LIST: handleActionOnlinePlayerList(p, index); break;
            case MENU_FULL_SET_ROOT:             handleFullSetRoot(p, index); break;
            case MENU_FULL_SET_BUILD:            handleFullSetBuild(p, index); break;
            case MENU_FULL_SET_CLAZZ:            handleFullSetClazz(p, index); break;
            case MENU_SPAWN_HERE_ROOT:           handleSpawnHereRoot(p, index); break;
            case MENU_SPAWN_HERE_DETU:           handleSpawnHereDetu(p, index); break;
            case MENU_SPAWN_HERE_SUPER_BOSS:     handleSpawnHereSuperBoss(p, index); break;
            case MENU_FULL_SET_LEVEL:            handleFullSetLevel(p, index); break;
            case MENU_FULL_SET_GEM_TYPE:         handleFullSetGemType(p, index); break;
            case MENU_THAN_TRANG_PAGE1:          handleGiveThanTrangSetMenu(p, index, 0); break;
            case MENU_THAN_TRANG_PAGE2:          handleGiveThanTrangSetMenu(p, index, 1); break;
            case MENU_THAN_TRANG_ACTION:         handleThanTrangAction(p, index); break;
            case MENU_KICH_AN_TIM_DIAL:          handleKichAnTimDial(p, index); break;
            case MENU_BUFF_SKILLS_ADVANCED:      handleBuffSkillsAdvanced(p, index); break;
            case MENU_ACTIVE_BUFF_SELECT:        handleActiveBuffSelect(p, index); break;
            case MENU_SMART_KHAM_ALL_GEMS:       handleSmartKhamGemType(p, index); break;
        }
    }

    // ==============================================================
    //  1. MENU CHÍNH (MAIN ADMIN MENU)
    // ==============================================================

    public static void openMain(Player p) throws IOException {
        openMain(p, MENU_MAIN);
    }

    public static void openMain(Player p, short menuId) throws IOException {
        if (!isAdmin(p)) {
            p.getService().send_box_ThongBao_OK("Chỉ Admin mới có quyền hạn sử dụng Thẻ Hệ Thống!");
            return;
        }

        iMenuDymanic.buildAndSend(p, menuId,
            "THẺ HỆ THỐNG - ADMIN CONTROL",
            new String[]{
                "Mặc Đè Full Set Trang Bị: Sát Thương - Chống Chịu - Hỗ Trợ",
                "Kéo Toàn Bộ Người Chơi Online Về Map Hiện Tại",
                "Triệu Hồi Boss Và Đệ Hoang Tại Vị Trí Đang Đứng",
                "Siêu Buff Toàn Diện: Chỉ Số, Ác Quỷ, Khảm Đá, Kích Ẩn",
                "Quản Lý Và Buff Trái Ác Quỷ",
                "Đổi Môn Phái Và Chuyển Đồ",
                "Thêm Hoặc Trừ Cấp Độ",
                "Thêm Vật Phẩm, Vàng, Ruby, Extol",
                "Quản Lý Thần Trang: Nhận Set Thần, Dọn Dẹp",
                "Dịch Chuyển Tức Thời: Map Và Người Chơi",
                "Quản Lý Hệ Thống Bot",
                "Quản Lý Đệ Tử Và Lính Đánh Thuê",
                "Quản Lý Phó Bản Thời Gian",
                "Quản Lý Boss Và Sự Kiện Server",
                "Quản Lý Người Chơi Online",
                "Cài Đặt Auto",
                "Đóng"
            },
            new short[]{110, 116, 155, 133, 155, 110, 110, 133, 154, 116, 155, 155, 110, 136, 134, 110, 118}
        );
    }

    private void handleMainMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: openFullSetRootMenu(p); break;
            case 1: TimedDungeonManager.pullAllOnlinePlayersToAdmin(p); break;
            case 2: new AdminSystemMenu().openSpawnHereMenu(p); break;
            case 3: openSuperBuffRootMenu(p); break;
            case 4: openBuffDevilFruitMenuStatic(p); break;
            case 5: openSwitchClazz(p); break;
            case 6: openAdjustLevelDialog(p); break;
            case 7: openAddItemCategory(p); break;
            case 8: openCleanThanTrangMenu(p); break;
            case 9: new AdminSystemMenu().openTeleport(p); break;
            case 10: new AdminSystemMenu().openBotManagement(p); break;
            case 11: openDiscipleManagement(p); break;
            case 12: openTimedDungeonList(p); break;
            case 13: openBossEvent(p); break;
            case 14: openPlayerManagement(p); break;
            case 15: AutoSystemMenu.openAutoMainMenu(p); break;
            case 16: break;
        }
    }

    public static void openSuperBuffRootMenu(Player p) throws IOException {
        AdminTarget cur = getCurrentTarget(p);
        String targetDesc = getTargetDisplayName(cur);

        iMenuDymanic.buildAndSend(p, MENU_SUPER_BUFF_ROOT,
            "SIÊU BUFF ADMIN VÀ TRANG BỊ: " + targetDesc,
            new String[]{
                "Buff Admin Full Chỉ Số Và Tài Nguyên",
                "Siêu Buff Toàn Diện Chỉ Số, Đá Khảm, Kích Ẩn, Nhiệm Vụ",
                "Siêu Buff Kỹ Năng & Trạng Thái Thần Thoại (Max Skill, Ác Quỷ +5, Bất Tử, Haki...)",
                "Cấu Hình Quả Tim & Dial (Tắt Lỗ Khảm, Kích Ẩn Tim/Dial)",
                "Khảm Đá Từng Trang Bị Đang Mặc",
                "Buff Full Đá Khảm 6 Lỗ: Chọn Loại Đá Hoặc Phong Cách",
                "Buff Kích Ẩn Trang Bị 12 Loại",
                "Hoàn Thành Toàn Bộ Nhiệm Vụ Chính Tuyến",
                "Chọn Mục Tiêu Mặc Định Cần Buff",
                "Quay Lại"
            },
            new short[]{133, 155, 155, 118, 127, 133, 110, 116, 110, 134}
        );
    }

    private void handleSuperBuffRootMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: buffAdminFullStats(p); break;
            case 1: openAllBuffTargetPicker(p); break;
            case 2: openBuffSkillsAdvancedMenu(p); break;
            case 3: openKichAnTimDialMenu(p); break;
            case 4: openKhamDaBodySelect(p); break;
            case 5: openSmartKhamDaSelect(p); break;
            case 6: openKichAnSelect(p); break;
            case 7: openQuestActionMenu(p); break;
            case 8: openTargetSelectMenu(p); break;
            case 9: openMain(p); break;
        }
    }

    // ==============================================================
    //  HỆ THỐNG MỤC TIÊU BUFF (TARGET SELECTOR)
    // ==============================================================

    public static class AdminTarget {
        public static final int TYPE_SELF = 0;
        public static final int TYPE_SELF_DETU = 1;
        public static final int TYPE_ONLINE_PLAYER = 2;
        public static final int TYPE_ONLINE_PLAYER_DETU = 3;
        public static final int TYPE_OFFLINE_PLAYER = 4;
        public static final int TYPE_OFFLINE_PLAYER_DETU = 5;

        public int type = TYPE_SELF;
        public Player player;
        public String name = "";

        public AdminTarget(int type, Player player, String name) {
            this.type = type;
            this.player = player;
            this.name = name != null ? name : "";
        }
    }

    public static final Map<Integer, AdminTarget> adminTargets = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Integer> tempEatFruitId = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, String> tempEatFruitName = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Integer> tempKhamSlot = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Integer> tempKhamCategory = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Integer> tempSmartKhamBuild = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Integer> tempKichAnId = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Boolean> tempOnlineSelectIsDetu = new java.util.concurrent.ConcurrentHashMap<>();

    public static final int ACTION_ALL_BUFF            = 1;
    public static final int ACTION_ALL_BUFF_DETU       = 2;
    public static final int ACTION_DEVIL_SKILLS        = 3;
    public static final int ACTION_DEVIL_SKILLS_DETU   = 4;
    public static final int ACTION_QUEST_NEXT          = 5;
    public static final int ACTION_QUEST_FULL          = 6;

    public static final Map<Integer, Integer> tempActionType = new java.util.concurrent.ConcurrentHashMap<>();
    public static final Map<Integer, Boolean> tempQuestIsFull = new java.util.concurrent.ConcurrentHashMap<>();

    public static AdminTarget getCurrentTarget(Player admin) {
        if (admin == null) return new AdminTarget(AdminTarget.TYPE_SELF, null, "");
        AdminTarget t = adminTargets.get(admin.IDPlayer);
        if (t == null) {
            t = new AdminTarget(AdminTarget.TYPE_SELF, admin, admin.name);
            adminTargets.put(admin.IDPlayer, t);
        }
        return t;
    }

    public static String getTargetDisplayName(AdminTarget t) {
        if (t == null) return "Bản Thân Admin";
        switch (t.type) {
            case AdminTarget.TYPE_SELF: return "Bản Thân: " + t.name + ")";
            case AdminTarget.TYPE_SELF_DETU: return "Đệ Tử Của Admin: " + t.name + ")";
            case AdminTarget.TYPE_ONLINE_PLAYER: return "Player Online [" + t.name + "]";
            case AdminTarget.TYPE_ONLINE_PLAYER_DETU: return "Đệ Tử Của [" + t.name + "] (Online)";
            case AdminTarget.TYPE_OFFLINE_PLAYER: return "Player Offline [" + t.name + "]";
            case AdminTarget.TYPE_OFFLINE_PLAYER_DETU: return "Đệ Tử Của [" + t.name + "] (Offline)";
            default: return "Bản Thân";
        }
    }

    public static Player getResolvedTargetPlayer(Player admin) {
        AdminTarget t = getCurrentTarget(admin);
        if (t.type == AdminTarget.TYPE_SELF) return admin;
        if (t.type == AdminTarget.TYPE_SELF_DETU) return admin.detu;
        if (t.type == AdminTarget.TYPE_ONLINE_PLAYER) {
            Player pOn = Zone.get_player_by_name_allmap(t.name);
            if (pOn != null) return pOn;
            return t.player;
        }
        if (t.type == AdminTarget.TYPE_ONLINE_PLAYER_DETU) {
            Player pOn = Zone.get_player_by_name_allmap(t.name);
            if (pOn != null && pOn.detu != null) return pOn.detu;
            if (t.player instanceof DeTu) return t.player;
            return null;
        }
        return null;
    }

    public void openTargetSelectMenu(Player p) throws IOException {
        AdminTarget cur = getCurrentTarget(p);
        iMenuDymanic.buildAndSend(p, MENU_TARGET_SELECT,
            "CHỌN MỤC TIÊU BUFF [" + getTargetDisplayName(cur) + "]",
            new String[]{
                "1. Bản Thân Admin [" + p.name + "]",
                "2. Đệ Tử Của Admin",
                "3. Người Chơi Online Chọn Từ Danh Sách",
                "4. Đệ Tử Của Người Chơi Online Chọn Từ Danh Sách",
                "5. Nhập Tên Nhân Vật Online Hoặc Offline",
                "6. Nhập Tên Sư Phụ Để Buff Đệ Tử Online Hoặc Offline",
                "Quay lại"
            },
            new short[]{110, 155, 133, 155, 116, 155, 134}
        );
    }

    private void handleTargetSelect(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_SELF, p, p.name));
                p.getService().send_box_ThongBao_OK("Đã chuyển mục tiêu buff sang: BẢN THÂN ADMIN [" + p.name + "]!");
                openSuperBuffRootMenu(p);
                break;
            }
            case 1: {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử! Hãy nhận đệ tử trước.");
                    openSuperBuffRootMenu(p);
                    return;
                }
                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_SELF_DETU, p.detu, p.detu.name));
                p.getService().send_box_ThongBao_OK("Đã chuyển mục tiêu buff sang: ĐỆ TỬ CỦA BẠN [" + p.detu.name + "]!");
                openSuperBuffRootMenu(p);
                break;
            }
            case 2: {
                openOnlinePlayerListMenu(p, false);
                break;
            }
            case 3: {
                openOnlinePlayerListMenu(p, true);
                break;
            }
            case 4: {
                p.sendInput("Nhập tên người chơi cần buff Online Hoặc Offline:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String name = inputs[0].trim();
                        Player targetOn = Zone.get_player_by_name_allmap(name);
                        if (targetOn != null) {
                            adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_ONLINE_PLAYER, targetOn, targetOn.name));
                            try { p.getService().send_box_ThongBao_OK("Đã chọn Player ONLINE: [" + targetOn.name + "]!"); openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                        } else {
                            boolean exists = checkPlayerExistsInDb(name);
                            if (exists) {
                                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_OFFLINE_PLAYER, null, name));
                                try { p.getService().send_box_ThongBao_OK("Đã chọn Player OFFLINE: [" + name + "] Đã Ghi Trực Tiếp Cơ Sở Dữ Liệu!"); openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + name + "] trên toàn hệ thống!"); openTargetSelectMenu(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 5: {
                p.sendInput("Nhập tên Sư Phụ để buff Đệ Tử Online Hoặc Offline:", new String[]{"Tên Sư Phụ"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String masterName = inputs[0].trim();
                        Player masterOn = Zone.get_player_by_name_allmap(masterName);
                        if (masterOn != null) {
                            if (masterOn.detu != null) {
                                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_ONLINE_PLAYER_DETU, masterOn.detu, masterOn.name));
                                try { p.getService().send_box_ThongBao_OK("Đã chọn ĐỆ TỬ CỦA [" + masterOn.name + "] (ONLINE)!"); openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Người chơi [" + masterOn.name + "] hiện chưa có đệ tử!"); openTargetSelectMenu(p); } catch (Exception ignored) {}
                            }
                        } else {
                            boolean detuExists = checkDetuExistsInDb(masterName);
                            if (detuExists) {
                                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_OFFLINE_PLAYER_DETU, null, masterName));
                                try { p.getService().send_box_ThongBao_OK("Đã chọn ĐỆ TỬ OFFLINE của [" + masterName + "] Đã Ghi Trực Tiếp Cơ Sở Dữ Liệu!"); openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy đệ tử nào thuộc về sư phụ [" + masterName + "]!"); openTargetSelectMenu(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 6: {
                openSuperBuffRootMenu(p);
                break;
            }
        }
    }

    public void openOnlinePlayerListMenu(Player p, boolean isDetu) throws IOException {
        tempOnlineSelectIsDetu.put(p.IDPlayer, isDetu);
        List<Player> onlineList = new ArrayList<>();
        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 != null && p0.name != null && !p0.name.isBlank()) {
                if (isDetu) {
                    if (p0.detu != null) onlineList.add(p0);
                } else {
                    onlineList.add(p0);
                }
            }
        }

        if (onlineList.isEmpty()) {
            p.getService().send_box_ThongBao_OK(isDetu ? "Hiện không có người chơi online nào sở hữu đệ tử!" : "Không có người chơi nào khác đang online!");
            openTargetSelectMenu(p);
            return;
        }

        int limit = Math.min(onlineList.size(), 15);
        String[] options = new String[limit + 1];
        short[] icons = new short[limit + 1];
        for (int i = 0; i < limit; i++) {
            Player target = onlineList.get(i);
            options[i] = (i + 1) + ". " + target.name + " (Lv." + target.level + ")" + (isDetu ? " [Đệ: " + target.detu.name + "]" : "");
            icons[i] = (short) (isDetu ? 155 : 133);
        }
        options[limit] = "Quay lại";
        icons[limit] = 134;

        iMenuDymanic.buildAndSend(p, MENU_ONLINE_PLAYER_LIST,
            isDetu ? "CHỌN ĐỆ TỬ CỦA PLAYER ONLINE" : "CHỌN NGƯỜI CHƠI ONLINE",
            options, icons
        );
    }

    private void handleOnlinePlayerList(Player p, int index) throws IOException {
        boolean isDetu = Boolean.TRUE.equals(tempOnlineSelectIsDetu.get(p.IDPlayer));
        List<Player> onlineList = new ArrayList<>();
        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 != null && p0.name != null && !p0.name.isBlank()) {
                if (isDetu) {
                    if (p0.detu != null) onlineList.add(p0);
                } else {
                    onlineList.add(p0);
                }
            }
        }

        int limit = Math.min(onlineList.size(), 15);
        if (index == limit) {
            openTargetSelectMenu(p);
            return;
        }

        if (index >= 0 && index < limit) {
            Player chosen = onlineList.get(index);
            if (isDetu) {
                if (chosen.detu != null) {
                    adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_ONLINE_PLAYER_DETU, chosen.detu, chosen.name));
                    p.getService().send_box_ThongBao_OK("Đã chọn mục tiêu: ĐỆ TỬ CỦA [" + chosen.name + "]!");
                }
            } else {
                adminTargets.put(p.IDPlayer, new AdminTarget(AdminTarget.TYPE_ONLINE_PLAYER, chosen, chosen.name));
                p.getService().send_box_ThongBao_OK("Đã chọn mục tiêu: PLAYER ONLINE [" + chosen.name + "]!");
            }
            openSuperBuffRootMenu(p);
        }
    }

    // ==============================================================
    //  SIÊU BUFF TOÀN DIỆN (ALL BUFF)
    // ==============================================================

    public static void executeAllBuff(Player admin) {
        try {
            AdminTarget cur = getCurrentTarget(admin);
            Player target = getResolvedTargetPlayer(admin);

            if (target != null) {
                executeAllBuffForTarget(admin, target, getTargetDisplayName(cur));
            } else {
                // Offline target
                if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER) {
                    executeAllBuffOfflinePlayer(admin, cur.name);
                } else if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER_DETU) {
                    executeAllBuffOfflineDetu(admin, cur.name);
                } else {
                    if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Mục tiêu hiện không hợp lệ hoặc không tìm thấy!");
                }
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeAllBuff error: " + e.getMessage());
        }
    }

    public static void executeAllBuffForTarget(Player admin, Player target, String targetDesc) {
        try {
            if (target == null) return;
            // Online target
            if (!(target instanceof DeTu)) {
                target.updateVnd(2_000_000_000);
                target.update_ruby(2_000_000_000);
                target.update_vang(2_000_000_000);
                target.updateMoney();
                if (target.item != null) {
                    target.item.add_item_bag47(7, (short) 9, 999);
                    target.item.add_item_bag47(4, (short) 158, 99);
                    target.item.updateInventory(false);
                }
                executeFullMainQuest(admin, target);
            }
            target.level = 150;
            target.pointAttribute = (short) Level.get_total_point_by_level(target.level);

            // Buff full cấp ác quỷ
            buffFullDevilSkills(target, false);

            // Khảm full 6 lỗ Cân Bằng (Topaz, Ruby, Saphia, Thạch Anh, Vô Cực, 910) cho ALL 8 món
            smartKhamAllEquipped(target, 2);

            // Buff full kích ẩn Bộc Phá (ID 4 - x2 sát thương) cho ALL 8 món
            buffKichAnAllEquipped(target, 4);

            // Restore full HP & MP
            if (target.ability != null) {
                target.ability.recalculatePlayerStats(target);
                target.hp = target.ability.get_hp_max(true);
                target.mp = target.ability.get_mp_max(true);
            }

            target.update_info_to_all();
            if (target.getService() != null) {
                target.getService().Main_char_Info(true);
                try {
                    target.getService().charWearing(target, false);
                } catch (Exception ignored) {}
                if (target != admin) {
                    target.getService().send_box_ThongBao_OK("Bạn vừa được Admin buff SIÊU CẤP TOÀN DIỆN Toàn Diện!");
                }
            }
            target.flush(target, false);
            if (target instanceof DeTu && ((DeTu) target).master != null) {
                ((DeTu) target).master.flush(((DeTu) target).master, false);
            }

            if (admin != null && admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ THỰC HIỆN SIÊU BUFF TOÀN DIỆN CHO [" + targetDesc.toUpperCase() + "]!\n" +
                    "- Max Level: 150: Full điểm tiềm năng\n" +
                    "- Full 2 Tỷ Vàng, 2 Tỷ Ruby, 2 Tỷ Extol: Nếu là người chơi\n" +
                    "- Full Hoàn Tất Toàn Bộ Nhiệm Vụ Chính & Mở Khóa Full Bản Đồ!\n" +
                    "- Full Cấp Ác Quỷ Cấp 5 100% Cho TOÀN BỘ Kỹ Năng!\n" +
                    "- Mở Full 6 Lỗ & Khảm Full 6 Đá Thần Thoại / Vô Cực Cho ALL Trang Bị Đang Mặc!\n" +
                    "- Full Kích Hoạt Kích Ẩn Bộc Phá: X2 Sát ThươngCho ALL Trang Bị Đang Mặc!\n" +
                    "- Phục hồi 100% HP/MP Max!"
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeAllBuffForTarget error: " + e.getMessage());
        }
    }

    public void openAllBuffTargetPicker(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_ALL_BUFF_TARGET_PICKER,
            "SIÊU BUFF TOÀN DIỆN - ĐỐI TƯỢNG",
            new String[]{
                "1. Bản Thân Admin [" + p.name + "]",
                "2. Đệ Tử Của Admin",
                "3. Người Chơi Online Chọn Từ Danh Sách",
                "4. Đệ Tử Của Người Chơi Online Chọn Từ Danh Sách",
                "5. Nhập Tên Người Chơi Online Hoặc Offline",
                "6. Nhập Tên Sư Phụ Để Buff Đệ Tử Online Hoặc Offline",
                "Quay lại"
            },
            new short[]{110, 155, 133, 155, 116, 155, 134}
        );
    }

    private void handleAllBuffTargetPicker(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                executeAllBuffForTarget(p, p, "BẢN THÂN ADMIN [" + p.name + "]");
                openSuperBuffRootMenu(p);
                break;
            }
            case 1: {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử! Hãy nhận đệ tử trước.");
                    openAllBuffTargetPicker(p);
                    return;
                }
                executeAllBuffForTarget(p, p.detu, "ĐỆ TỬ CỦA ADMIN [" + p.detu.name + "]");
                openSuperBuffRootMenu(p);
                break;
            }
            case 2: {
                openActionOnlinePlayerList(p, ACTION_ALL_BUFF);
                break;
            }
            case 3: {
                openActionOnlinePlayerList(p, ACTION_ALL_BUFF_DETU);
                break;
            }
            case 4: {
                p.sendInput("Nhập tên người chơi cần Siêu Buff Online Hoặc Offline:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String name = inputs[0].trim();
                        Player targetOn = Zone.get_player_by_name_allmap(name);
                        if (targetOn != null) {
                            executeAllBuffForTarget(p, targetOn, "PLAYER ONLINE [" + targetOn.name + "]");
                            try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                        } else {
                            boolean exists = checkPlayerExistsInDb(name);
                            if (exists) {
                                executeAllBuffOfflinePlayer(p, name);
                                try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + name + "]!"); openAllBuffTargetPicker(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 5: {
                p.sendInput("Nhập tên Sư Phụ để Siêu Buff Đệ Tử Online Hoặc Offline:", new String[]{"Tên Sư Phụ"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String masterName = inputs[0].trim();
                        Player masterOn = Zone.get_player_by_name_allmap(masterName);
                        if (masterOn != null) {
                            if (masterOn.detu != null) {
                                executeAllBuffForTarget(p, masterOn.detu, "ĐỆ TỬ CỦA [" + masterOn.name + "]");
                                try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Người chơi [" + masterOn.name + "] chưa có đệ tử!"); openAllBuffTargetPicker(p); } catch (Exception ignored) {}
                            }
                        } else {
                            boolean detuExists = checkDetuExistsInDb(masterName);
                            if (detuExists) {
                                executeAllBuffOfflineDetu(p, masterName);
                                try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy đệ tử nào của sư phụ [" + masterName + "]!"); openAllBuffTargetPicker(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 6: {
                openSuperBuffRootMenu(p);
                break;
            }
        }
    }

    public void openDevilSkillsTargetPicker(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_DEVIL_SKILL_TARGET_PICKER,
            "BUFF CẤP ÁC QUỶ - ĐỐI TƯỢNG",
            new String[]{
                "1. Bản Thân Admin [" + p.name + "]",
                "2. Đệ Tử Của Admin",
                "3. Người Chơi Online Chọn Từ Danh Sách",
                "4. Đệ Tử Của Người Chơi Online Chọn Từ Danh Sách",
                "5. Nhập Tên Người Chơi Online Hoặc Offline",
                "6. Nhập Tên Sư Phụ Để Buff Đệ Tử Online Hoặc Offline",
                "Quay lại"
            },
            new short[]{110, 155, 133, 155, 116, 155, 134}
        );
    }

    private void handleDevilSkillsTargetPicker(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                buffFullDevilSkills(p, true);
                p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ Cấp 5 100% cho Bản Thân Admin!");
                openBuffDevilFruitMenuStatic(p);
                break;
            }
            case 1: {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                    openDevilSkillsTargetPicker(p);
                    return;
                }
                buffFullDevilSkills(p.detu, true);
                p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ Cấp 5 100% cho Đệ Tử của bạn!");
                openBuffDevilFruitMenuStatic(p);
                break;
            }
            case 2: {
                openActionOnlinePlayerList(p, ACTION_DEVIL_SKILLS);
                break;
            }
            case 3: {
                openActionOnlinePlayerList(p, ACTION_DEVIL_SKILLS_DETU);
                break;
            }
            case 4: {
                p.sendInput("Nhập tên người chơi cần buff Ác Quỷ Online Hoặc Offline:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String name = inputs[0].trim();
                        Player targetOn = Zone.get_player_by_name_allmap(name);
                        if (targetOn != null) {
                            buffFullDevilSkills(targetOn, true);
                            try { p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ cho Player Online [" + targetOn.name + "]!"); openBuffDevilFruitMenuStatic(p); } catch (Exception ignored) {}
                        } else {
                            boolean exists = checkPlayerExistsInDb(name);
                            if (exists) {
                                executeBuffDevilSkillsOffline(p, new AdminTarget(AdminTarget.TYPE_OFFLINE_PLAYER, null, name));
                                try { openBuffDevilFruitMenuStatic(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy người chơi [" + name + "]!"); openDevilSkillsTargetPicker(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 5: {
                p.sendInput("Nhập tên Sư Phụ để buff Đệ Tử Online Hoặc Offline:", new String[]{"Tên Sư Phụ"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String masterName = inputs[0].trim();
                        Player masterOn = Zone.get_player_by_name_allmap(masterName);
                        if (masterOn != null) {
                            if (masterOn.detu != null) {
                                buffFullDevilSkills(masterOn.detu, true);
                                try { p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ cho Đệ Tử của [" + masterOn.name + "]!"); openBuffDevilFruitMenuStatic(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Người chơi [" + masterOn.name + "] chưa có đệ tử!"); openDevilSkillsTargetPicker(p); } catch (Exception ignored) {}
                            }
                        } else {
                            boolean detuExists = checkDetuExistsInDb(masterName);
                            if (detuExists) {
                                executeBuffDevilSkillsOffline(p, new AdminTarget(AdminTarget.TYPE_OFFLINE_PLAYER_DETU, null, masterName));
                                try { openBuffDevilFruitMenuStatic(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy đệ tử nào của sư phụ [" + masterName + "]!"); openDevilSkillsTargetPicker(p); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 6: {
                openBuffDevilFruitMenuStatic(p);
                break;
            }
        }
    }

    public void openQuestActionMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_QUEST_ACTION,
            "BUFF NHIỆM VỤ CHÍNH TUYẾN",
            new String[]{
                "1. Next Nhiệm Vụ Chính: Chuyển sang NV tiếp theo",
                "2. Full Nhiệm Vụ Chính: Hoàn thành TẤT CẢ & Mở Full Map",
                "Quay lại"
            },
            new short[]{110, 133, 134}
        );
    }

    private void handleQuestAction(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                tempQuestIsFull.put(p.IDPlayer, false);
                openQuestTargetPicker(p, false);
                break;
            }
            case 1: {
                tempQuestIsFull.put(p.IDPlayer, true);
                openQuestTargetPicker(p, true);
                break;
            }
            case 2: {
                openSuperBuffRootMenu(p);
                break;
            }
        }
    }

    public void openQuestTargetPicker(Player p, boolean isFull) throws IOException {
        String title = isFull ? "FULL NHIỆM VỤ CHÍNH: MỞ FULL MAP" : "NEXT NHIỆM VỤ CHÍNH TUYẾN";
        iMenuDymanic.buildAndSend(p, MENU_QUEST_TARGET_PICKER,
            title + " - ĐỐI TƯỢNG",
            new String[]{
                "1. Bản Thân Admin [" + p.name + "]",
                "2. Người Chơi Online Chọn Từ Danh Sách",
                "3. Nhập Tên Người Chơi Online Hoặc Offline",
                "Quay lại"
            },
            new short[]{110, 133, 116, 134}
        );
    }

    private void handleQuestTargetPicker(Player p, int index) throws IOException {
        boolean isFull = Boolean.TRUE.equals(tempQuestIsFull.get(p.IDPlayer));
        switch (index) {
            case 0: {
                if (isFull) {
                    executeFullMainQuest(p, p);
                } else {
                    executeNextMainQuest(p, p);
                }
                openSuperBuffRootMenu(p);
                break;
            }
            case 1: {
                openActionOnlinePlayerList(p, isFull ? ACTION_QUEST_FULL : ACTION_QUEST_NEXT);
                break;
            }
            case 2: {
                p.sendInput("Nhập tên người chơi cần buff nhiệm vụ Online Hoặc Offline:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String name = inputs[0].trim();
                        Player targetOn = Zone.get_player_by_name_allmap(name);
                        if (targetOn != null) {
                            if (isFull) {
                                executeFullMainQuest(p, targetOn);
                            } else {
                                executeNextMainQuest(p, targetOn);
                            }
                            try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                        } else {
                            boolean exists = checkPlayerExistsInDb(name);
                            if (exists) {
                                if (isFull) {
                                    executeFullMainQuestOffline(p, name);
                                } else {
                                    executeNextMainQuestOffline(p, name);
                                }
                                try { openSuperBuffRootMenu(p); } catch (Exception ignored) {}
                            } else {
                                try { p.getService().send_box_ThongBao_OK("Không tìm thấy người chơi [" + name + "]!"); openQuestTargetPicker(p, isFull); } catch (Exception ignored) {}
                            }
                        }
                    }
                });
                break;
            }
            case 3: {
                openQuestActionMenu(p);
                break;
            }
        }
    }

    public void openActionOnlinePlayerList(Player p, int actionType) throws IOException {
        tempActionType.put(p.IDPlayer, actionType);
        boolean isDetuAction = (actionType == ACTION_ALL_BUFF_DETU || actionType == ACTION_DEVIL_SKILLS_DETU);

        List<Player> onlineList = new ArrayList<>();
        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 != null && p0.name != null && !p0.name.isBlank()) {
                if (isDetuAction) {
                    if (p0.detu != null) onlineList.add(p0);
                } else {
                    onlineList.add(p0);
                }
            }
        }

        if (onlineList.isEmpty()) {
            p.getService().send_box_ThongBao_OK(isDetuAction ? "Hiện không có người chơi online nào sở hữu đệ tử!" : "Không có người chơi nào khác đang online!");
            openSuperBuffRootMenu(p);
            return;
        }

        int limit = Math.min(onlineList.size(), 15);
        String[] options = new String[limit + 1];
        short[] icons = new short[limit + 1];
        for (int i = 0; i < limit; i++) {
            Player target = onlineList.get(i);
            options[i] = (i + 1) + ". " + target.name + " (Lv." + target.level + ")" + (isDetuAction ? " [Đệ: " + target.detu.name + "]" : "");
            icons[i] = (short) (isDetuAction ? 155 : 133);
        }
        options[limit] = "Quay lại";
        icons[limit] = 134;

        String title = "CHỌN NGƯỜI CHƠI ONLINE";
        if (actionType == ACTION_ALL_BUFF) title = "SIÊU BUFF: ALL BUFF- CHỌN PLAYER ONLINE";
        else if (actionType == ACTION_ALL_BUFF_DETU) title = "SIÊU BUFF - CHỌN ĐỆ TỬ CỦA PLAYER ONLINE";
        else if (actionType == ACTION_DEVIL_SKILLS) title = "BUFF CẤP ÁC QUỶ - CHỌN PLAYER ONLINE";
        else if (actionType == ACTION_DEVIL_SKILLS_DETU) title = "BUFF CẤP ÁC QUỶ - CHỌN ĐỆ TỬ CỦA PLAYER ONLINE";
        else if (actionType == ACTION_QUEST_NEXT) title = "NEXT NHIỆM VỤ - CHỌN PLAYER ONLINE";
        else if (actionType == ACTION_QUEST_FULL) title = "FULL NHIỆM VỤ - CHỌN PLAYER ONLINE";

        iMenuDymanic.buildAndSend(p, MENU_ACTION_ONLINE_PLAYER_LIST, title, options, icons);
    }

    private void handleActionOnlinePlayerList(Player p, int index) throws IOException {
        int actionType = tempActionType.getOrDefault(p.IDPlayer, ACTION_ALL_BUFF);
        boolean isDetuAction = (actionType == ACTION_ALL_BUFF_DETU || actionType == ACTION_DEVIL_SKILLS_DETU);

        List<Player> onlineList = new ArrayList<>();
        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 != null && p0.name != null && !p0.name.isBlank()) {
                if (isDetuAction) {
                    if (p0.detu != null) onlineList.add(p0);
                } else {
                    onlineList.add(p0);
                }
            }
        }

        int limit = Math.min(onlineList.size(), 15);
        if (index == limit) {
            if (actionType == ACTION_ALL_BUFF || actionType == ACTION_ALL_BUFF_DETU) {
                openAllBuffTargetPicker(p);
            } else if (actionType == ACTION_DEVIL_SKILLS || actionType == ACTION_DEVIL_SKILLS_DETU) {
                openDevilSkillsTargetPicker(p);
            } else if (actionType == ACTION_QUEST_NEXT || actionType == ACTION_QUEST_FULL) {
                openQuestTargetPicker(p, actionType == ACTION_QUEST_FULL);
            } else {
                openSuperBuffRootMenu(p);
            }
            return;
        }

        if (index >= 0 && index < limit) {
            Player chosen = onlineList.get(index);
            switch (actionType) {
                case ACTION_ALL_BUFF: {
                    executeAllBuffForTarget(p, chosen, "PLAYER ONLINE [" + chosen.name + "]");
                    break;
                }
                case ACTION_ALL_BUFF_DETU: {
                    if (chosen.detu != null) {
                        executeAllBuffForTarget(p, chosen.detu, "ĐỆ TỬ CỦA [" + chosen.name + "]");
                    }
                    break;
                }
                case ACTION_DEVIL_SKILLS: {
                    buffFullDevilSkills(chosen, true);
                    p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ Cấp 5 100% cho Player Online [" + chosen.name + "]!");
                    break;
                }
                case ACTION_DEVIL_SKILLS_DETU: {
                    if (chosen.detu != null) {
                        buffFullDevilSkills(chosen.detu, true);
                        p.getService().send_box_ThongBao_OK("Đã buff Cấp Ác Quỷ Cấp 5 100% cho Đệ tử của [" + chosen.name + "]!");
                    }
                    break;
                }
                case ACTION_QUEST_NEXT: {
                    executeNextMainQuest(p, chosen);
                    break;
                }
                case ACTION_QUEST_FULL: {
                    executeFullMainQuest(p, chosen);
                    break;
                }
            }
            if (actionType == ACTION_DEVIL_SKILLS || actionType == ACTION_DEVIL_SKILLS_DETU) {
                openBuffDevilFruitMenuStatic(p);
            } else {
                openSuperBuffRootMenu(p);
            }
        }
    }

    public static void executeNextMainQuest(Player admin, Player target) {
        try {
            if (target == null) return;
            if (target.list_quest == null) target.list_quest = new ArrayList<>();
            QuestP mq = target.getMainQuest();
            if (mq == null) {
                mq = new QuestP();
                mq.template = Quest.get_quest(0);
                if (mq.template != null) {
                    mq.data = new short[mq.template.data_quest != null ? mq.template.data_quest.length : 0][];
                    for (int i = 0; i < mq.data.length; i++) {
                        mq.data[i] = new short[mq.template.data_quest[i].length];
                        for (int j = 0; j < mq.data[i].length; j++) {
                            mq.data[i][j] = mq.template.data_quest[i][j];
                        }
                    }
                    target.list_quest.add(mq);
                }
            } else {
                if (mq.template.equals(Quest.QUEST_FINISH) || mq.template.id >= Quest.QUEST_FINISH.id) {
                    if (admin != null && admin.getService() != null) {
                        admin.getService().send_box_ThongBao_OK("Nhân vật [" + target.name + "] đã hoàn thành toàn bộ nhiệm vụ chính tuyến!");
                    }
                    return;
                }
                short nextId = (short) (mq.template.id + 1);
                Quest nextTemplate = Quest.get_quest(nextId);
                mq.template = nextTemplate;
                mq.data = new short[nextTemplate.data_quest != null ? nextTemplate.data_quest.length : 0][];
                for (int i = 0; i < mq.data.length; i++) {
                    mq.data[i] = new short[nextTemplate.data_quest[i].length];
                    for (int j = 0; j < mq.data[i].length; j++) {
                        mq.data[i][j] = nextTemplate.data_quest[i][j];
                    }
                }
            }
            Quest.send_List_Quest(target, false);
            target.flush(target, false);
            if (target.getService() != null && target != admin) {
                target.getService().send_box_ThongBao_OK("Admin đã chuyển tiếp nhiệm vụ chính tuyến cho bạn sang: " + (mq.template != null ? mq.template.name : "") + "!");
            }
            if (admin != null && admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ NEXT NHIỆM VỤ CHÍNH CHO [" + target.name.toUpperCase() + "]!\n" +
                    "- Nhiệm vụ hiện tại: [" + (mq.template != null ? mq.template.id : 0) + "] " + (mq.template != null ? mq.template.name : "") + "\n" +
                    "- Hướng dẫn: " + (mq.template != null && mq.template.showDialog != null ? mq.template.showDialog : "")
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeNextMainQuest error: " + e.getMessage());
        }
    }

    public static void executeFullMainQuest(Player admin, Player target) {
        try {
            if (target == null) return;
            if (target.list_quest == null) target.list_quest = new ArrayList<>();
            QuestP mq = target.getMainQuest();
            if (mq == null) {
                mq = new QuestP();
                target.list_quest.add(mq);
            }
            mq.template = Quest.QUEST_FINISH;
            mq.data = new short[0][];
            Quest.send_List_Quest(target, false);
            target.flush(target, false);
            if (target.getService() != null && target != admin) {
                target.getService().send_box_ThongBao_OK("Admin đã hoàn thành TOÀN BỘ nhiệm vụ chính tuyến & mở full bản đồ cho bạn!");
            }
            if (admin != null && admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ HOÀN THÀNH TẤT CẢ NHIỆM VỤ CHÍNH CHO [" + target.name.toUpperCase() + "]!\n" +
                    "- Đã đưa về trạng thái hoàn tất cốt truyện: " + (Quest.QUEST_FINISH != null ? Quest.QUEST_FINISH.name : "Hết nhiệm vụ") + "\n" +
                    "- Đã mở khóa toàn bộ tất cả bản đồ: Max Unlocked Map: 198!"
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeFullMainQuest error: " + e.getMessage());
        }
    }

    public static void executeNextMainQuestOffline(Player admin, String playerName) {
        try (Connection conn = DbManager.gI().getConnect()) {
            int pId = -1;
            String questStr = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT `id`, `quest` FROM `players` WHERE `name` = ? LIMIT 1")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        pId = rs.getInt("id");
                        questStr = rs.getString("quest");
                    }
                }
            }
            if (pId == -1) {
                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + playerName + "] trong DB!");
                return;
            }

            String newQuest = nextQuestJsonString(questStr);
            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `quest` = ? WHERE `id` = ?")) {
                psUp.setString(1, newQuest);
                psUp.setInt(2, pId);
                psUp.executeUpdate();
            }
            database.CacheManager.gI().remove("players_" + playerName);
            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK("Đã Next nhiệm vụ chính cho Player Offline [" + playerName + "] thành công: Đã lưu DB và xóa Cache!");
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeNextMainQuestOffline error: " + e.getMessage());
        }
    }

    public static void executeFullMainQuestOffline(Player admin, String playerName) {
        try (Connection conn = DbManager.gI().getConnect()) {
            int pId = -1;
            String questStr = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT `id`, `quest` FROM `players` WHERE `name` = ? LIMIT 1")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        pId = rs.getInt("id");
                        questStr = rs.getString("quest");
                    }
                }
            }
            if (pId == -1) {
                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + playerName + "] trong DB!");
                return;
            }

            String newQuest = fullQuestJsonString(questStr);
            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `quest` = ? WHERE `id` = ?")) {
                psUp.setString(1, newQuest);
                psUp.setInt(2, pId);
                psUp.executeUpdate();
            }
            database.CacheManager.gI().remove("players_" + playerName);
            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ FULL NHIỆM VỤ CHÍNH CHO PLAYER OFFLINE [" + playerName + "] THÀNH CÔNG!\n" +
                    "- Trạng thái: Hoàn tất toàn bộ cốt truyện & Mở Full Bản Đồ\n" +
                    "- Đã lưu vào MySQL và xóa Cache."
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeFullMainQuestOffline error: " + e.getMessage());
        }
    }

    public static String nextQuestJsonString(String questJson) {
        try {
            JSONArray arr = null;
            if (questJson != null && !questJson.isEmpty()) {
                Object parsed = JSONValue.parse(questJson);
                if (parsed instanceof JSONArray) {
                    arr = (JSONArray) parsed;
                }
            }
            if (arr == null) arr = new JSONArray();

            boolean found = false;
            for (int i = 0; i < arr.size(); i++) {
                Object o = arr.get(i);
                if (o instanceof JSONArray) {
                    JSONArray qEntry = (JSONArray) o;
                    if (!qEntry.isEmpty()) {
                        int qId = ((Number) qEntry.get(0)).intValue();
                        if (qId >= 0) { // Main quest
                            found = true;
                            int nextId = qId + 1;
                            Quest nextT = Quest.get_quest(nextId);
                            JSONArray newEntry = new JSONArray();
                            newEntry.add((int) nextT.id);
                            JSONArray rows = new JSONArray();
                            if (nextT.data_quest != null) {
                                for (short[] row : nextT.data_quest) {
                                    JSONArray cells = new JSONArray();
                                    for (short v : row) cells.add(v);
                                    rows.add(cells);
                                }
                            }
                            newEntry.add(rows);
                            arr.set(i, newEntry);
                            break;
                        }
                    }
                }
            }
            if (!found) {
                Quest nextT = Quest.get_quest(1);
                JSONArray newEntry = new JSONArray();
                newEntry.add((int) nextT.id);
                JSONArray rows = new JSONArray();
                if (nextT.data_quest != null) {
                    for (short[] row : nextT.data_quest) {
                        JSONArray cells = new JSONArray();
                        for (short v : row) cells.add(v);
                        rows.add(cells);
                    }
                }
                newEntry.add(rows);
                arr.add(newEntry);
            }
            return arr.toJSONString();
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "nextQuestJsonString error: " + e.getMessage());
            return questJson;
        }
    }

    public static String fullQuestJsonString(String questJson) {
        try {
            JSONArray arr = null;
            if (questJson != null && !questJson.isEmpty()) {
                Object parsed = JSONValue.parse(questJson);
                if (parsed instanceof JSONArray) {
                    arr = (JSONArray) parsed;
                }
            }
            if (arr == null) arr = new JSONArray();

            JSONArray finishEntry = new JSONArray();
            finishEntry.add((int) (Quest.QUEST_FINISH != null ? Quest.QUEST_FINISH.id : 9999));
            finishEntry.add(new JSONArray()); // empty rows

            boolean found = false;
            for (int i = 0; i < arr.size(); i++) {
                Object o = arr.get(i);
                if (o instanceof JSONArray) {
                    JSONArray qEntry = (JSONArray) o;
                    if (!qEntry.isEmpty()) {
                        int qId = ((Number) qEntry.get(0)).intValue();
                        if (qId >= 0) {
                            arr.set(i, finishEntry);
                            found = true;
                            break;
                        }
                    }
                }
            }
            if (!found) {
                arr.add(finishEntry);
            }
            return arr.toJSONString();
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "fullQuestJsonString error: " + e.getMessage());
            return questJson;
        }
    }

    public static void executeBuffDevilSkills(Player admin) {
        AdminTarget cur = getCurrentTarget(admin);
        Player target = getResolvedTargetPlayer(admin);
        if (target != null) {
            buffFullDevilSkills(target, true);
            if (target != admin && admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK("Đã buff Full Cấp Ác Quỷ cho [" + getTargetDisplayName(cur) + "]!");
            }
        } else {
            executeBuffDevilSkillsOffline(admin, cur);
        }
    }

    // ==============================================================
    //  ĂN TRÁI ÁC QUỶ (20 LOẠI TRÁI - MIN / MAX)
    // ==============================================================

    public static final int[][] DEVIL_FRUITS_ALL = new int[][]{
        {4869, 133}, // 0: Pika
        {4160, 133}, // 1: Goro
        {4161, 133}, // 2: Magu
        {4090, 133}, // 3: Hie
        {4033, 133}, // 4: Mera
        {4427, 133}, // 5: Yami
        {4870, 133}, // 6: Mero
        {4873, 133}, // 7: Nika
        {4240, 133}, // 8: Phoenix
        {4032, 133}, // 9: Gomu
        {4034, 133}, // 10: Suna
        {4088, 133}, // 11: Moku
        {4091, 133}, // 12: Chopper
        {4092, 133}, // 13: Fujitora
        {4093, 133}, // 14: Attack
        {4219, 133}, // 15: BoCongAnh
        {4220, 133}, // 16: Dao
        {4316, 133}, // 17: Bomb
        {4317, 133}, // 18: TachRoi
        {4318, 133}  // 19: Kilo
    };

    public static final String[] DEVIL_FRUIT_NAMES_ALL = new String[]{
        "1. Trái Ánh Sáng: Pika Pika - Kizaru",
        "2. Trái Sấm Sét: Goro Goro - Enel",
        "3. Trái Nham Thạch: Magu Magu - Akainu",
        "4. Trái Băng: Hie Hie - Aokiji",
        "5. Trái Lửa: Mera Mera - Ace",
        "6. Trái Bóng Tối: Yami Yami - Teach",
        "7. Trái Tình Yêu: Mero Mero - Hancock",
        "8. Trái Nika: Hito Hito Nika - Gear 5",
        "9. Trái Phượng Hoàng: Tori Tori - Marco",
        "10. Trái Cao Su: Gomu Gomu - Luffy",
        "11. Trái Cát: Suna Suna - Crocodile",
        "12. Trái Khói: Moku Moku - Smoker",
        "13. Trái Người Hươu: Hito Hito - Chopper",
        "14. Trái Trọng Lực: Zushi Zushi - Fujitora",
        "15. Trái Tấn Công: Attack - Chiến Đấu",
        "16. Trái Bồ Công Anh: Chiến Binh",
        "17. Trái Dao: Dicer - Lưỡi Dao",
        "18. Trái Bom: Bomu Bomu - Mr.5",
        "19. Trái Tách Rời: Bara Bara - Buggy",
        "20. Trái Kilo: Kilo Kilo - Miss Valentine"
    };

    public void openEatDevilSelectMenu(Player p, int page) throws IOException {
        int start = (page == 0) ? 0 : 10;
        int end = (page == 0) ? 10 : 20;

        String[] options = new String[12];
        short[] icons = new short[12];
        for (int i = start; i < end; i++) {
            int idx = i - start;
            options[idx] = DEVIL_FRUIT_NAMES_ALL[i];
            icons[idx] = (short) DEVIL_FRUITS_ALL[i][1];
        }
        if (page == 0) {
            options[10] = "Trang Tiếp Theo: Trái 11-20->";
            icons[10] = 110;
        } else {
            options[10] = "<- Trang Trước: Trái 1-10";
            icons[10] = 110;
        }
        options[11] = "Quay lại";
        icons[11] = 134;

        short menuId = (page == 0) ? MENU_EAT_DEVIL_SELECT : MENU_DEVIL_FRUIT_PAGE2;
        iMenuDymanic.buildAndSend(p, menuId,
            "ĂN TRÁI ÁC QUỶ: TRANG " + (page + 1) + "/2)",
            options, icons
        );
    }

    private void handleEatDevilSelect(Player p, int index) throws IOException {
        if (index >= 0 && index < 10) {
            int fruitId = DEVIL_FRUITS_ALL[index][0];
            String fruitName = DEVIL_FRUIT_NAMES_ALL[index];
            tempEatFruitId.put(p.IDPlayer, fruitId);
            tempEatFruitName.put(p.IDPlayer, fruitName);
            openEatDevilLevelMenu(p);
        } else if (index == 10) {
            openEatDevilSelectMenu(p, 1);
        } else {
            openBuffDevilFruitMenuStatic(p);
        }
    }

    private void handleEatDevilSelectPage2(Player p, int index) throws IOException {
        if (index >= 0 && index < 10) {
            int realIdx = 10 + index;
            int fruitId = DEVIL_FRUITS_ALL[realIdx][0];
            String fruitName = DEVIL_FRUIT_NAMES_ALL[realIdx];
            tempEatFruitId.put(p.IDPlayer, fruitId);
            tempEatFruitName.put(p.IDPlayer, fruitName);
            openEatDevilLevelMenu(p);
        } else if (index == 10) {
            openEatDevilSelectMenu(p, 0);
        } else {
            openBuffDevilFruitMenuStatic(p);
        }
    }

    public void openEatDevilLevelMenu(Player p) throws IOException {
        String fruitName = tempEatFruitName.getOrDefault(p.IDPlayer, "Trái Ác Quỷ");
        iMenuDymanic.buildAndSend(p, MENU_EAT_DEVIL_LEVEL,
            "ĂN " + fruitName.toUpperCase() + " - CHỌN CẤP ĐỘ",
            new String[]{
                "[MIN] Cấp Ác Quỷ 0, Exp 0%: Kỹ Năng Cấp Cơ Bản",
                "[MAX] Cấp Ác Quỷ 5, Exp 100%: Max Cấp Kỹ Năng",
                "Quay lại"
            },
            new short[]{110, 155, 134}
        );
    }

    private void handleEatDevilLevel(Player p, int index) throws IOException {
        if (index == 2) {
            openEatDevilSelectMenu(p, 0);
            return;
        }

        boolean isMax = (index == 1);
        int fruitId = tempEatFruitId.getOrDefault(p.IDPlayer, 4869);
        String fruitName = tempEatFruitName.getOrDefault(p.IDPlayer, "Trái Ác Quỷ");
        AdminTarget cur = getCurrentTarget(p);
        Player target = getResolvedTargetPlayer(p);

        if (target != null) {
            executeEatDevilFruit(p, target, fruitId, fruitName, isMax);
        } else {
            if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER || cur.type == AdminTarget.TYPE_OFFLINE_PLAYER_DETU) {
                p.getService().send_box_ThongBao_OK("Để thiết lập trọn bộ animation kỹ năng chuẩn xác nhất, tính năng Ăn Trái Ác Quỷ yêu cầu nhân vật đang Online!");
            }
        }
        openBuffDevilFruitMenuStatic(p);
    }

    public static void executeEatDevilFruit(Player admin, Player target, int fruitId, String fruitName, boolean isMax) {
        try {
            if (target == null) return;
            // 1. Gán kỹ năng trái ác quỷ
            target.get_skill_taq_new(fruitId);

            // 2. Thiết lập cấp độ kỹ năng và cấp cường hóa ác quỷ
            if (target.skill_point != null) {
                for (Skill_info sk : target.skill_point) {
                    if (sk != null && sk.temp != null) {
                        boolean isDevilFruit = (sk.temp.ID >= 2000 || sk.temp.typeDevil > 0);
                        if (isDevilFruit) {
                            if (sk.temp.Lv_RQ == -1) {
                                Skill_Template.learn_skill(sk);
                            }
                            if (isMax) {
                                if (sk.temp.Lv_RQ > 0) {
                                    for (int lvl = sk.temp.Lv_RQ; lvl < 30; lvl++) {
                                        Skill_Template.upgrade_skill(sk, target.clazz);
                                    }
                                }
                                sk.lvdevil = 5;
                                sk.devilpercent = 100;
                            } else {
                                sk.lvdevil = 0;
                                sk.devilpercent = 0;
                            }
                        } else if (sk.temp.ID == 0 || sk.temp.ID == 1 || sk.temp.ID == 2 || isClassSpecificSkill(sk)) {
                            if (isMax) {
                                sk.lvdevil = 5;
                                sk.devilpercent = 100;
                            }
                        }
                    }
                }
            }

            // 3. Đăng ký thời gian hồi chiêu
            if (target.time_use_skill != null && target.skill_point != null) {
                for (Skill_info sk : target.skill_point) {
                    if (sk != null && sk.temp != null) {
                        target.time_use_skill.put(sk.temp.ID, 0L);
                    }
                }
            }

            // 4. Đồng bộ
            target.send_skill();
            target.update_info_to_all();
            if (target.getService() != null) {
                target.getService().Main_char_Info(false);
                if (target != admin) {
                    target.getService().send_box_ThongBao_OK(
                        "Bạn đã được Admin cho ăn " + fruitName.toUpperCase() + "\n" +
                        "Cấp độ: " + (isMax ? "MAX: Cấp Ác Quỷ 5, Exp 100%" : "MIN: Cấp Ác Quỷ 0, Exp 0%")
                    );
                }
            }
            target.flush(target, false);
            if (target instanceof DeTu && ((DeTu) target).master != null) {
                ((DeTu) target).master.flush(((DeTu) target).master, false);
            }

            if (admin != null && admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ CHO [" + (target == admin ? "BẢN THÂN" : target.name.toUpperCase()) + "] ĂN " + fruitName.toUpperCase() + "!\n" +
                    "- Cấp độ: " + (isMax ? "[MAX] Cấp Ác Quỷ 5, Exp 100%, Max Cấp Chiêu" : "[MIN] Cấp Ác Quỷ 0, Exp 0%") + "\n" +
                    "- Đồng bộ dữ liệu và lưu DB thành công!"
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeEatDevilFruit error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  KHẢM ĐÁ TRANG BỊ ĐANG MẶC (THỦ CÔNG - CHUẨN ICON)
    // ==============================================================

    public static final String[] BODY_SLOT_NAMES = new String[]{
        "Vũ khí", "Mũ Nón", "Áo", "Quần", "Giày", "Găng tay", "Dây chuyền / Tim", "Nhẫn"
    };

    public void openKhamDaBodySelect(Player p) throws IOException {
        Player target = getResolvedTargetPlayer(p);
        if (target == null || target.item == null || target.item.it_body == null) {
            p.getService().send_box_ThongBao_OK("Mục tiêu hiện tại không online hoặc không có dữ liệu trang bị!");
            openSuperBuffRootMenu(p);
            return;
        }

        String[] options = new String[9];
        short[] icons = new short[9];
        for (int i = 0; i < 8; i++) {
            Item_wear it = (i < target.item.it_body.length) ? target.item.it_body[i] : null;
            if (it != null && it.template != null) {
                if (it.template.typeEquip == 6 || it.template.id == 11000 || it.isThanTrang() || it.template.isThanTrang()) {
                    options[i] = "[" + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": " + it.template.name + " [Không Có Lỗ / Không Khảm]";
                } else {
                    int daCount = (it.mdakham != null) ? it.mdakham.length : 0;
                    options[i] = "[" + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": " + it.template.name + " (+" + it.levelUp + "[Lỗ: " + it.numLoKham + "/6, Đã khảm: " + daCount + "]";
                }
                icons[i] = it.template.icon;
            } else {
                options[i] = "[" + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": [Trống] Chưa mặc đồ";
                icons[i] = 118;
            }
        }
        options[8] = "Quay lại";
        icons[8] = 134;

        iMenuDymanic.buildAndSend(p, MENU_KHAM_DA_BODY,
            "KHẢM ĐÁ - CHỌN TRANG BỊ [" + getTargetDisplayName(getCurrentTarget(p)) + "]",
            options, icons
        );
    }

    private void handleKhamDaBody(Player p, int index) throws IOException {
        if (index == 8) {
            openSuperBuffRootMenu(p);
            return;
        }

        Player target = getResolvedTargetPlayer(p);
        if (target == null || target.item == null || target.item.it_body == null || index >= target.item.it_body.length) {
            openSuperBuffRootMenu(p);
            return;
        }

        Item_wear it = target.item.it_body[index];
        if (it == null || it.template == null) {
            p.getService().send_box_ThongBao_OK("Ô trang bị này đang trống! Hãy mặc đồ trước khi khảm đá.");
            openKhamDaBodySelect(p);
            return;
        }

        if (it.template.typeEquip == 6 || it.template.id == 11000 || it.isThanTrang() || it.template.isThanTrang()) {
            p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim và Thần Trang không có lỗ và không thể khảm ngọc!");
            openKhamDaBodySelect(p);
            return;
        }

        tempKhamSlot.put(p.IDPlayer, index);
        openKhamDaCategorySelect(p);
    }

    public static final String[] GEM_CAT_NAMES = new String[]{
        "1. Cẩm Thạch: Cấp 1 - 6[Hút HP]",
        "2. Topaz: Cấp 1 - 6[Tấn Công]",
        "3. Ruby: Cấp 1 - 6[Bạo Kích]",
        "4. Lục Bảo: Cấp 1 - 6[Xuyên Giáp]",
        "5. Saphia: Cấp 1 - 6[Giảm ST/Thủ]",
        "6. Thạch Anh Tím: Cấp 1 - 6[Phản ST]",
        "7. Hổ Phách: Cấp 1 - 6[Né Tránh]",
        "8. Đá Hải Thạch: Cấp 1 - 6",
        "9. Đá Siêu Cấp: Song Thuộc Tính",
        "10. Đá Thần Thoại: Song Thuộc Tính Max",
        "11. Đá Kháng / Vô Cực & Cổ Đại 910",
        "Quay lại"
    };

    public static final short[] GEM_CAT_ICONS = new short[]{
        24, 30, 36, 42,
        48, 54, 313, 177,
        205, 604, 278, 134
    };


    public void openKhamDaCategorySelect(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_KHAM_DA_CAT,
            "CHỌN LOẠI ĐÁ KHẢM",
            GEM_CAT_NAMES, GEM_CAT_ICONS
        );
    }

    private void handleKhamDaCat(Player p, int index) throws IOException {
        if (index == 11) {
            openKhamDaBodySelect(p);
            return;
        }

        tempKhamCategory.put(p.IDPlayer, index);
        openKhamDaDetailSelect(p, index);
    }

    public static List<ItemTemplate4> getGemsByCategory(int catIndex) {
        List<ItemTemplate4> list = new ArrayList<>();
        switch (catIndex) {
            case 0: // Cẩm thạch 1-6
                for (int id = 44; id <= 49; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 1: // Topaz 1-6
                for (int id = 50; id <= 55; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 2: // Ruby 1-6
                for (int id = 56; id <= 61; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 3: // Lục bảo 1-6
                for (int id = 62; id <= 67; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 4: // Saphia 1-6
                for (int id = 68; id <= 73; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 5: // Thạch anh 1-6
                for (int id = 74; id <= 79; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 6: // Hổ phách 1-6
                for (int id = 362; id <= 367; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 7: // Hải thạch 1-6
                for (int id = 221; id <= 226; id++) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 8: // Siêu cấp
                int[] scIds = new int[]{241, 242, 243, 244, 245, 246, 251, 256, 261, 266, 368};
                for (int id : scIds) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 9: // Thần thoại
                int[] ttIds = new int[]{647, 648, 649, 650, 651, 652, 657, 662, 667, 672, 677};
                for (int id : ttIds) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
            case 10: // Vô cực & Đá 910
                int[] vcIds = new int[]{324, 325, 326, 910};
                for (int id : vcIds) { ItemTemplate4 it = ItemTemplate4.get_it_by_id(id); if (it != null) list.add(it); }
                break;
        }
        return list;
    }

    public void openKhamDaDetailSelect(Player p, int catIndex) throws IOException {
        List<ItemTemplate4> gems = getGemsByCategory(catIndex);
        if (gems.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Danh mục này chưa có dữ liệu!");
            openKhamDaCategorySelect(p);
            return;
        }

        String[] options = new String[gems.size() + 1];
        short[] icons = new short[gems.size() + 1];
        for (int i = 0; i < gems.size(); i++) {
            ItemTemplate4 it4 = gems.get(i);
            options[i] = it4.name;
            icons[i] = it4.icon;
        }
        options[gems.size()] = "Quay lại";
        icons[gems.size()] = 134;

        iMenuDymanic.buildAndSend(p, MENU_KHAM_DA_DETAIL,
            "CHỌN VIÊN ĐÁ ĐỂ KHẢM",
            options, icons
        );
    }

    private void handleKhamDaDetail(Player p, int index) throws IOException {
        int catIndex = tempKhamCategory.getOrDefault(p.IDPlayer, 0);
        List<ItemTemplate4> gems = getGemsByCategory(catIndex);
        if (index == gems.size()) {
            openKhamDaCategorySelect(p);
            return;
        }

        if (index >= 0 && index < gems.size()) {
            ItemTemplate4 chosenGem = gems.get(index);
            int slot = tempKhamSlot.getOrDefault(p.IDPlayer, 0);
            Player target = getResolvedTargetPlayer(p);

            if (target != null && target.item != null && target.item.it_body != null && slot < target.item.it_body.length) {
                Item_wear it = target.item.it_body[slot];
                if (it != null && it.template != null) {
                    int curGems = (it.mdakham != null) ? it.mdakham.length : 0;
                    if (it.numLoKham < 6) {
                        it.numLoKham = 6;
                    } else if (curGems >= it.numLoKham) {
                        it.numLoKham = (byte) (curGems + 1);
                    }

                    Rebuild_Item.add_op_ngoc_kham_new(it, (short) chosenGem.id);

                    if (target.ability != null) target.ability.recalculatePlayerStats(target);
                    try { target.update_info_to_all(); } catch (Exception ignored) {}
                    if (target.getService() != null) {
                        target.getService().charWearing(target, false);
                        target.item.updateInventory(false);
                    }
                    target.flush(target, false);
                    if (target instanceof DeTu && ((DeTu) target).master != null) {
                        ((DeTu) target).master.flush(((DeTu) target).master, false);
                    }

                    p.getService().send_box_ThongBao_OK(
                        "ĐÃ KHẢM THÀNH CÔNG!\n" +
                        "- Viên đá: " + chosenGem.name + "\n" +
                        "- Trang bị: " + it.template.name + " (" + BODY_SLOT_NAMES[slot] + ")\n" +
                        "- Tổng số đá đã khảm: " + it.mdakham.length + "/" + it.numLoKham
                    );
                    openKhamDaBodySelect(p);
                    return;
                }
            }
            openKhamDaBodySelect(p);
        }
    }

    // ==============================================================
    //  BUFF FULL ĐÁ KHẢM 6 Ô THÔNG MINH (DAME / TANK / CÂN BẰNG)
    // ==============================================================

    public void openSmartKhamDaSelect(Player p) throws IOException {
        String[] items = new String[GEM_PRESET_NAMES.length + 3];
        short[] icons = new short[items.length];
        for (int i = 0; i < GEM_PRESET_NAMES.length; i++) {
            items[i] = "Khảm Full 6 Ô: " + GEM_PRESET_NAMES[i];
            icons[i] = (short) (i >= 8 ? 133 : 110);
        }
        items[GEM_PRESET_NAMES.length] = "Khảm Theo Phong Cách Cũ (Dame, Tank, Cân Bằng)";
        icons[GEM_PRESET_NAMES.length] = 155;
        items[GEM_PRESET_NAMES.length + 1] = "Tẩy Toàn Bộ Lỗ Khảm Về Trống (Xóa Đá)";
        icons[GEM_PRESET_NAMES.length + 1] = 118;
        items[GEM_PRESET_NAMES.length + 2] = "Quay Lại";
        icons[GEM_PRESET_NAMES.length + 2] = 134;

        iMenuDymanic.buildAndSend(p, MENU_SMART_KHAM_DA_SELECT,
            "BUFF FULL ĐÁ KHẢM 6 Ô - CHỌN LOẠI ĐÁ",
            items, icons
        );
    }

    private void handleSmartKhamDaSelect(Player p, int index) throws IOException {
        if (index == GEM_PRESET_NAMES.length + 2) {
            openSuperBuffRootMenu(p);
            return;
        }

        if (index >= 0 && index < GEM_PRESET_NAMES.length) {
            tempSmartKhamBuild.put(p.IDPlayer, 100 + GEM_PRESET_IDS[index]);
            openSmartKhamDaSlotSelect(p);
            return;
        }

        if (index == GEM_PRESET_NAMES.length) {
            tempSmartKhamBuild.put(p.IDPlayer, 2); // Cân bằng
            openSmartKhamDaSlotSelect(p);
            return;
        }

        if (index == GEM_PRESET_NAMES.length + 1) {
            tempSmartKhamBuild.put(p.IDPlayer, -1); // Tẩy sạch
            openSmartKhamDaSlotSelect(p);
            return;
        }
    }

    public void handleSmartKhamGemType(Player p, int index) throws IOException {
        handleSmartKhamDaSelect(p, index);
    }

    public void openSmartKhamDaSlotSelect(Player p) throws IOException {
        Player target = getResolvedTargetPlayer(p);
        String[] options = new String[10];
        short[] icons = new short[10];

        options[0] = "[KHẢM TOÀN BỘ] Khảm Cho TẤT CẢ 8 Món Trang Bị Đang Mặc";
        icons[0] = 133;

        for (int i = 0; i < 8; i++) {
            Item_wear it = (target != null && target.item != null && target.item.it_body != null && i < target.item.it_body.length) ? target.item.it_body[i] : null;
            if (it != null && it.template != null) {
                options[i + 1] = "[Slot " + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": " + it.template.name;
                icons[i + 1] = it.template.icon;
            } else {
                options[i + 1] = "[Slot " + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": [Trống]";
                icons[i + 1] = 118;
            }
        }
        options[9] = "Quay Lại";
        icons[9] = 134;

        iMenuDymanic.buildAndSend(p, MENU_SMART_KHAM_DA_SLOT,
            "CHỌN TRANG BỊ ĐỂ ÁP DỤNG ĐÁ KHẢM",
            options, icons
        );
    }

    private void handleSmartKhamDaSlot(Player p, int index) throws IOException {
        if (index == 9) {
            openSmartKhamDaSelect(p);
            return;
        }

        int buildType = tempSmartKhamBuild.getOrDefault(p.IDPlayer, 2);
        String bName;
        if (buildType == -1) {
            bName = "TẨY TRẮNG (XÓA ĐÁ)";
        } else if (buildType >= 100) {
            bName = getGemNameById(buildType - 100);
        } else {
            String[] buildNames = {"SÁT THƯƠNG / DAME", "CHỐNG CHỊU / TANK", "CÂN BẰNG TOÀN DIỆN"};
            bName = buildNames[Math.max(0, Math.min(buildType, 2))];
        }

        AdminTarget cur = getCurrentTarget(p);
        Player target = getResolvedTargetPlayer(p);

        if (index == 0) {
            // Khảm toàn bộ
            if (target != null) {
                smartKhamAllEquipped(target, buildType);
                target.flush(target, false);
                if (target instanceof DeTu && ((DeTu) target).master != null) {
                    ((DeTu) target).master.flush(((DeTu) target).master, false);
                }
                p.getService().send_box_ThongBao_OK("Đã khảm: " + bName + " cho TOÀN BỘ trang bị đang mặc của [" + getTargetDisplayName(cur) + "]!");
            } else {
                smartKhamOfflineBody(p, cur.name, buildType);
            }
            openSuperBuffRootMenu(p);
        } else {
            // Khảm 1 món cụ thể
            int slot = index - 1;
            if (target != null && target.item != null && target.item.it_body != null && slot < target.item.it_body.length) {
                Item_wear it = target.item.it_body[slot];
                if (it != null && it.template != null) {
                    smartKhamSlot(target, slot, buildType);
                    target.flush(target, false);
                    if (target instanceof DeTu && ((DeTu) target).master != null) {
                        ((DeTu) target).master.flush(((DeTu) target).master, false);
                    }
                    p.getService().send_box_ThongBao_OK("Đã khảm: " + bName + " vào " + it.template.name + " (" + BODY_SLOT_NAMES[slot] + ")!");
                } else {
                    p.getService().send_box_ThongBao_OK("Ô trang bị này đang trống!");
                }
            } else {
                p.getService().send_box_ThongBao_OK("Mục tiêu hiện không khả dụng!");
            }
            openSmartKhamDaSlotSelect(p);
        }
    }

    public static void smartKhamAllEquipped(Player target, int buildType) {
        if (target == null || target.item == null || target.item.it_body == null) return;
        int[] gemIds = getGemsForBuild(buildType);
        for (int i = 0; i < target.item.it_body.length; i++) {
            Item_wear it = target.item.it_body[i];
            if (it != null && it.template != null) {
                if (it.template.typeEquip == 6 || it.template.id == 11000 || it.isThanTrang() || it.template.isThanTrang()) {
                    it.numLoKham = 0;
                    it.numHoleDaDuc = 0;
                    it.mdakham = new short[0];
                    if (it.option_item_2 != null) it.option_item_2.clear();
                    continue;
                }
                applySmartGemsToItem(it, gemIds);
            }
        }
        if (target.ability != null) target.ability.recalculatePlayerStats(target);
        try { target.update_info_to_all(); } catch (Exception ignored) {}
        if (target.getService() != null) {
            try {
                target.getService().charWearing(target, false);
                target.item.updateInventory(false);
            } catch (Exception ignored) {}
        }
    }

    public static void smartKhamSlot(Player target, int slot, int buildType) {
        if (target == null || target.item == null || target.item.it_body == null) return;
        if (slot < 0 || slot >= target.item.it_body.length) return;
        Item_wear it = target.item.it_body[slot];
        if (it == null) return;

        if (it.template != null && (it.template.typeEquip == 6 || it.template.id == 11000 || it.isThanTrang() || it.template.isThanTrang())) {
            it.numLoKham = 0;
            it.numHoleDaDuc = 0;
            it.mdakham = new short[0];
            if (it.option_item_2 != null) it.option_item_2.clear();
            try { target.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim và Thần Trang không có lỗ và không thể khảm ngọc!"); } catch (Exception ignored) {}
            return;
        }

        int[] gemIds = getGemsForBuild(buildType);
        applySmartGemsToItem(it, gemIds);
        if (target.ability != null) target.ability.recalculatePlayerStats(target);
        try { target.update_info_to_all(); } catch (Exception ignored) {}
        if (target.getService() != null) {
            try {
                target.getService().charWearing(target, false);
                target.item.updateInventory(false);
            } catch (Exception ignored) {}
        }
    }

    public static void applySmartGemsToItem(Item_wear it, int[] gemIds) {
        if (it == null) return;
        if (it.template != null && (it.template.typeEquip == 6 || it.template.id == 11000 || it.isThanTrang() || it.template.isThanTrang())) {
            it.numLoKham = 0;
            it.numHoleDaDuc = 0;
            it.mdakham = new short[0];
            if (it.option_item_2 != null) it.option_item_2.clear();
            return;
        }
        if (gemIds == null || gemIds.length == 0) {
            it.numLoKham = 0;
            it.numHoleDaDuc = 0;
            it.mdakham = new short[0];
            it.option_item_2 = new ArrayList<>();
            return;
        }
        it.numLoKham = (byte) gemIds.length;
        it.numHoleDaDuc = (byte) gemIds.length;
        it.mdakham = new short[gemIds.length];
        for (int i = 0; i < gemIds.length; i++) {
            it.mdakham[i] = (short) gemIds[i];
        }
        it.option_item_2 = new ArrayList<>();
        for (int gid : gemIds) {
            Rebuild_Item.add_op_ngoc_kham_new(it, (short) gid);
        }
    }

    public static int[] getGemsForBuild(int buildType) {
        if (buildType >= 100) {
            int gemId = buildType - 100;
            return new int[]{gemId, gemId, gemId, gemId, gemId, gemId};
        }
        if (buildType == -1) {
            return new int[0];
        }
        switch (buildType) {
            case 0: // Dame
                return new int[]{652, 657, 662, 647, 326, 910};
            case 1: // Tank
                return new int[]{667, 672, 677, 650, 326, 910};
            case 2: // Balance
            default:
                return new int[]{652, 657, 667, 672, 326, 910};
        }
    }

    // ==============================================================
    //  BUFF KÍCH ẨN TRANG BỊ (12 LOẠI KÍCH ẨN)
    // ==============================================================

    public static final String[] KICH_AN_LIST_NAMES = new String[]{
        "1. Kích Ẩn Bất Tử: Miễn tử khi nguy cấp",
        "2. Kích Ẩn Lời Cảm Ơn: Hồi HP khi nhận ST",
        "3. Kích Ẩn Lá Chắn: Khiên hấp thụ ST",
        "4. Kích Ẩn Khóa Năng Lượng: Khóa MP đối phương",
        "5. Kích Ẩn Bộc Phá: X2 Sát thương bộc phát",
        "6. Kích Ẩn Tập Trung Cao Độ: Bạo kích & chính xác",
        "7. Kích Ẩn Ma Cà Rồng: Hút HP liên tục",
        "8. Kích Ẩn Đánh Là Choáng: Gây choáng đối thủ",
        "9. Kích Ẩn Thanh Lọc: Xóa bỏ khống chế",
        "10. Kích Ẩn Nén Đau: Nén sát thương gánh chịu",
        "11. Kích Ẩn Giải Phóng Năng Lượng: ST diện rộng",
        "12. Kích Ẩn Cuồng Bạo: Tăng tốc & sát thương",
        "13. Kích Ẩn Thần Thánh: Toàn diện",
        "Quay lại"
    };

    public static final short[] KICH_AN_ICONS = new short[]{
        110, 110, 110, 110, 155, 110, 110, 155, 110, 110, 110, 110, 133, 134
    };

    public void openKichAnSelect(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_KICH_AN_SELECT,
            "BUFF KÍCH ẨN TRANG BỊ - CHỌN LOẠI",
            KICH_AN_LIST_NAMES, KICH_AN_ICONS
        );
    }

    private void handleKichAnSelect(Player p, int index) throws IOException {
        if (index == 13) {
            openSuperBuffRootMenu(p);
            return;
        }

        tempKichAnId.put(p.IDPlayer, index);
        openKichAnTargetSlotSelect(p);
    }

    public void openKichAnTargetSlotSelect(Player p) throws IOException {
        Player target = getResolvedTargetPlayer(p);
        String[] options = new String[10];
        short[] icons = new short[10];

        int kaId = tempKichAnId.getOrDefault(p.IDPlayer, 0);
        String kaName = (kaId >= 0 && kaId < KICH_AN_LIST_NAMES.length) ? KICH_AN_LIST_NAMES[kaId] : "Kích Ẩn";

        options[0] = "[KÍCH HOẠT TOÀN BỘ] Gán Cho TẤT CẢ 8 Món Đang Mặc: Kích Hoạt Full Set";
        icons[0] = 133;

        for (int i = 0; i < 8; i++) {
            Item_wear it = (target != null && target.item != null && target.item.it_body != null && i < target.item.it_body.length) ? target.item.it_body[i] : null;
            if (it != null && it.template != null) {
                options[i + 1] = "[Slot " + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": " + it.template.name + ": Ẩn hiện tại: " + it.valueKichAn + ")";
                icons[i + 1] = it.template.icon;
            } else {
                options[i + 1] = "[Slot " + (i + 1) + "] " + BODY_SLOT_NAMES[i] + ": [Trống]";
                icons[i + 1] = 118;
            }
        }
        options[9] = "Quay lại";
        icons[9] = 134;

        iMenuDymanic.buildAndSend(p, MENU_KICH_AN_TARGET_SLOT,
            "GÁN " + kaName.toUpperCase() + " [" + getTargetDisplayName(getCurrentTarget(p)) + "]",
            options, icons
        );
    }

    private void handleKichAnTargetSlot(Player p, int index) throws IOException {
        if (index == 9) {
            openKichAnSelect(p);
            return;
        }

        int kaId = tempKichAnId.getOrDefault(p.IDPlayer, 0);
        String kaName = (kaId >= 0 && kaId < KICH_AN_LIST_NAMES.length) ? KICH_AN_LIST_NAMES[kaId] : "Kích Ẩn";
        AdminTarget cur = getCurrentTarget(p);
        Player target = getResolvedTargetPlayer(p);

        if (index == 0) {
            // Gán toàn bộ
            if (target != null) {
                buffKichAnAllEquipped(target, kaId);
                target.flush(target, false);
                if (target instanceof DeTu && ((DeTu) target).master != null) {
                    ((DeTu) target).master.flush(((DeTu) target).master, false);
                }
                p.getService().send_box_ThongBao_OK("Đã gán " + kaName + " cho TOÀN BỘ trang bị đang mặc của [" + getTargetDisplayName(cur) + "]!");
            } else {
                kichAnOfflineBody(p, cur.name, kaId);
            }
            openSuperBuffRootMenu(p);
        } else {
            // Gán 1 món cụ thể
            int slot = index - 1;
            if (target != null && target.item != null && target.item.it_body != null && slot < target.item.it_body.length) {
                Item_wear it = target.item.it_body[slot];
                if (it != null && it.template != null) {
                    buffKichAnSlot(target, slot, kaId);
                    target.flush(target, false);
                    if (target instanceof DeTu && ((DeTu) target).master != null) {
                        ((DeTu) target).master.flush(((DeTu) target).master, false);
                    }
                    p.getService().send_box_ThongBao_OK("Đã gán " + kaName + " vào " + it.template.name + " (" + BODY_SLOT_NAMES[slot] + ")!");
                } else {
                    p.getService().send_box_ThongBao_OK("Ô trang bị này đang trống!");
                }
            } else {
                p.getService().send_box_ThongBao_OK("Mục tiêu hiện không khả dụng!");
            }
            openKichAnTargetSlotSelect(p);
        }
    }

    public static void buffKichAnAllEquipped(Player target, int kichAnId) {
        if (target == null || target.item == null || target.item.it_body == null) return;
        for (int i = 0; i < target.item.it_body.length; i++) {
            Item_wear it = target.item.it_body[i];
            if (it != null && it.template != null) {
                if (it.getColor() < 2) it.color = 2; // Tím để hợp lệ kích ẩn
                it.valueKichAn = (byte) kichAnId;
            }
        }
        if (target.ability != null) target.ability.recalculatePlayerStats(target);
        try { target.update_info_to_all(); } catch (Exception ignored) {}
        if (target.getService() != null) {
            try {
                target.getService().charWearing(target, false);
                target.item.updateInventory(false);
            } catch (Exception ignored) {}
        }
    }

    public static void buffKichAnSlot(Player target, int slot, int kichAnId) {
        if (target == null || target.item == null || target.item.it_body == null) return;
        if (slot < 0 || slot >= target.item.it_body.length) return;
        Item_wear it = target.item.it_body[slot];
        if (it != null && it.template != null) {
            if (it.getColor() < 2) it.color = 2;
            it.valueKichAn = (byte) kichAnId;
            if (target.ability != null) target.ability.recalculatePlayerStats(target);
            try { target.update_info_to_all(); } catch (Exception ignored) {}
            if (target.getService() != null) {
                try {
                    target.getService().charWearing(target, false);
                    target.item.updateInventory(false);
                } catch (Exception ignored) {}
            }
        }
    }

    // ==============================================================
    //  CẤU HÌNH QUẢ TIM & DIAL (TẮT LỖ KHẢM / KÍCH ẨN TIM & DIAL)
    // ==============================================================

    public void openKichAnTimDialMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_KICH_AN_TIM_DIAL,
            "CẤU HÌNH QUẢ TIM & DIAL",
            new String[]{
                "1. Tim Thuần Túy: Tắt Toàn Bộ Lỗ Khảm, Không Đá (Bản Thân)",
                "2. Kích Ẩn Bộc Phá (Ẩn 4) Cho Cả Tim & Dial (Bản Thân)",
                "3. Kích Ẩn Bất Tử (Ẩn 1) Cho Cả Tim & Dial (Bản Thân)",
                "4. Kích Ẩn Hút Máu Ma Cà Rồng (Ẩn 7) Cho Cả Tim & Dial (Bản Thân)",
                "5. Kích Ẩn Thần Thánh (Ẩn 12) Cho Cả Tim & Dial (Bản Thân)",
                "6. Tim Thuần Túy Cho Đệ Tử (Tắt Toàn Bộ Lỗ Khảm)",
                "7. Kích Ẩn Bộc Phá Cho Cả Tim & Dial Của Đệ Tử",
                "8. Mở 6 Lỗ Khảm Đá Thần Thoại Cho Dial (Slot 8)",
                "Quay Lại"
            },
            new short[]{118, 155, 110, 110, 133, 118, 155, 133, 134}
        );
    }

    private void handleKichAnTimDial(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                boolean ok = disableHeartSockets(p);
                p.getService().send_box_ThongBao_OK(ok ? "Đã tắt toàn bộ lỗ khảm của Quả Tim (Trạng Thái Thuần Túy) thành công!" : "Bạn chưa trang bị Quả Tim!");
                break;
            }
            case 1: {
                applyKichAnToTimDial(p, 4);
                p.getService().send_box_ThongBao_OK("Đã gán Kích Ẩn Bộc Phá (Ẩn 4) cho Quả Tim và Dial!");
                break;
            }
            case 2: {
                applyKichAnToTimDial(p, 1);
                p.getService().send_box_ThongBao_OK("Đã gán Kích Ẩn Bất Tử (Ẩn 1) cho Quả Tim và Dial!");
                break;
            }
            case 3: {
                applyKichAnToTimDial(p, 7);
                p.getService().send_box_ThongBao_OK("Đã gán Kích Ẩn Ma Cà Rồng (Ẩn 7) cho Quả Tim và Dial!");
                break;
            }
            case 4: {
                applyKichAnToTimDial(p, 12);
                p.getService().send_box_ThongBao_OK("Đã gán Kích Ẩn Thần Thánh (Ẩn 12) cho Quả Tim và Dial!");
                break;
            }
            case 5: {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                } else {
                    boolean ok = disableHeartSockets(p.detu);
                    p.getService().send_box_ThongBao_OK(ok ? "Đã tắt lỗ khảm Quả Tim của Đệ Tử thành công!" : "Đệ Tử chưa trang bị Quả Tim!");
                }
                break;
            }
            case 6: {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                } else {
                    applyKichAnToTimDial(p.detu, 4);
                    p.getService().send_box_ThongBao_OK("Đã gán Kích Ẩn Bộc Phá cho Quả Tim và Dial của Đệ Tử!");
                }
                break;
            }
            case 7: {
                socketHeartDialGems(p, 677);
                p.getService().send_box_ThongBao_OK("Đã khảm 6 lỗ Đá Thần Thoại cho Dial thành công! (Quả Tim được bảo toàn không có lỗ khảm).");
                break;
            }
            case 8:
                openSuperBuffRootMenu(p);
                break;
        }
    }

    public static boolean disableHeartSockets(Player target) {
        if (target == null || target.item == null) return false;
        boolean found = false;
        if (target.item.it_heart != null) {
            target.item.it_heart.numLoKham = 0;
            target.item.it_heart.numHoleDaDuc = 0;
            target.item.it_heart.mdakham = new short[0];
            if (target.item.it_heart.option_item_2 != null) target.item.it_heart.option_item_2.clear();
            found = true;
        }
        if (target.item.it_body != null && target.item.it_body.length > 6 && target.item.it_body[6] != null) {
            target.item.it_body[6].numLoKham = 0;
            target.item.it_body[6].numHoleDaDuc = 0;
            target.item.it_body[6].mdakham = new short[0];
            if (target.item.it_body[6].option_item_2 != null) target.item.it_body[6].option_item_2.clear();
            found = true;
        }
        if (found) {
            try {
                if (target.ability != null) target.ability.recalculatePlayerStats(target);
                target.item.updateInventory(false);
                if (target.getService() != null) target.getService().charWearing(target, false);
                target.flush(target, false);
            } catch (Exception ignored) {}
        }
        return found;
    }

    public static void applyKichAnToTimDial(Player target, int kaId) {
        if (target == null || target.item == null) return;
        if (target.item.it_heart != null) {
            target.item.it_heart.valueKichAn = (byte) kaId;
            if (target.item.it_heart.getColor() < 2) target.item.it_heart.color = 2;
        }
        if (target.item.it_body != null && target.item.it_body.length > 6 && target.item.it_body[6] != null) {
            target.item.it_body[6].valueKichAn = (byte) kaId;
            if (target.item.it_body[6].getColor() < 2) target.item.it_body[6].color = 2;
        }
        if (target.item.it_body != null && target.item.it_body.length > 7 && target.item.it_body[7] != null) {
            target.item.it_body[7].valueKichAn = (byte) kaId;
            if (target.item.it_body[7].getColor() < 2) target.item.it_body[7].color = 2;
        }
        try {
            if (target.ability != null) target.ability.recalculatePlayerStats(target);
            target.item.updateInventory(false);
            if (target.getService() != null) target.getService().charWearing(target, false);
            target.flush(target, false);
        } catch (Exception ignored) {}
    }

    public static void socketHeartDialGems(Player target, int gemId) {
        if (target == null || target.item == null) return;
        // Quả Tim tuyệt đối không có lỗ, không khảm đá
        disableHeartSockets(target);

        // Chỉ khảm đá cho Dial (slot 7)
        int[] gems = new int[]{gemId, gemId, gemId, gemId, gemId, gemId};
        if (target.item.it_body != null && target.item.it_body.length > 7 && target.item.it_body[7] != null) {
            applySmartGemsToItem(target.item.it_body[7], gems);
        }
        try {
            if (target.ability != null) target.ability.recalculatePlayerStats(target);
            target.item.updateInventory(false);
            if (target.getService() != null) target.getService().charWearing(target, false);
            target.flush(target, false);
        } catch (Exception ignored) {}
    }

    // ==============================================================
    //  HỖ TRỢ XỬ LÝ OFFLINE (MYSQL DATABASE)
    // ==============================================================

    public static boolean checkPlayerExistsInDb(String name) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `id` FROM `players` WHERE `name` = ? LIMIT 1")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkDetuExistsInDb(String masterName) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT d.`id` FROM `players_detu` d JOIN `players` p ON d.`owner_id` = p.`id` WHERE p.`name` = ? LIMIT 1")) {
            ps.setString(1, masterName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void executeAllBuffOfflinePlayer(Player admin, String playerName) {
        try (Connection conn = DbManager.gI().getConnect()) {
            int pId = -1;
            String itBodyStr = null;
            String skillStr = null;
            String questStr = null;
            String invStr = null;
            String lvlStr = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT `id`, `it_body`, `skill`, `quest`, `inventory`, `level` FROM `players` WHERE `name` = ? LIMIT 1")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        pId = rs.getInt("id");
                        itBodyStr = rs.getString("it_body");
                        skillStr = rs.getString("skill");
                        questStr = rs.getString("quest");
                        invStr = rs.getString("inventory");
                        lvlStr = rs.getString("level");
                    }
                }
            }
            if (pId == -1) {
                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + playerName + "] trong DB!");
                return;
            }

            JSONObject invObj = null;
            try {
                Object p = JSONValue.parse(invStr != null ? invStr : "{}");
                if (p instanceof JSONObject) invObj = (JSONObject) p;
            } catch (Exception ignored) {}
            if (invObj == null) invObj = new JSONObject();

            JSONObject pointInven = invObj.get("point_inven") instanceof JSONObject ? (JSONObject) invObj.get("point_inven") : new JSONObject();
            pointInven.put("gold", 2000000000L);
            pointInven.put("vnd", 2000000000L);
            invObj.put("point_inven", pointInven);
            invObj.put("ruby", 2000000000);

            JSONObject lvlObj = null;
            try {
                Object p = JSONValue.parse(lvlStr != null ? lvlStr : "{}");
                if (p instanceof JSONObject) lvlObj = (JSONObject) p;
            } catch (Exception ignored) {}
            if (lvlObj == null) lvlObj = new JSONObject();
            lvlObj.put("lv", 150);
            lvlObj.put("exp", 2000000000L);

            try (PreparedStatement psUp = conn.prepareStatement(
                    "UPDATE `players` SET `level` = ?, `exp` = 2000000000, `inventory` = ?, `coin` = 2000000000 WHERE `id` = ?")) {
                psUp.setString(1, lvlObj.toJSONString());
                psUp.setString(2, invObj.toJSONString());
                psUp.setInt(3, pId);
                psUp.executeUpdate();
            }

            if (skillStr != null && !skillStr.isEmpty()) {
                try {
                    Object parsed = JSONValue.parse(skillStr);
                    if (parsed instanceof JSONArray) {
                        JSONArray arr = (JSONArray) parsed;
                        for (Object o : arr) {
                            if (o instanceof JSONObject) {
                                JSONObject skObj = (JSONObject) o;
                                skObj.put("lvdevil", 5);
                                skObj.put("devilpercent", 100);
                            }
                        }
                        try (PreparedStatement psSk = conn.prepareStatement("UPDATE `players` SET `skill` = ? WHERE `id` = ?")) {
                            psSk.setString(1, arr.toJSONString());
                            psSk.setInt(2, pId);
                            psSk.executeUpdate();
                        }
                    }
                } catch (Exception ignored) {}
            }

            if (itBodyStr != null && !itBodyStr.isEmpty()) {
                String newBody = buffItBodyJsonString(itBodyStr, 2, 4);
                try (PreparedStatement psBd = conn.prepareStatement("UPDATE `players` SET `it_body` = ? WHERE `id` = ?")) {
                    psBd.setString(1, newBody);
                    psBd.setInt(2, pId);
                    psBd.executeUpdate();
                }
            }

            if (questStr != null) {
                String newQuest = fullQuestJsonString(questStr);
                try (PreparedStatement psQ = conn.prepareStatement("UPDATE `players` SET `quest` = ? WHERE `id` = ?")) {
                    psQ.setString(1, newQuest);
                    psQ.setInt(2, pId);
                    psQ.executeUpdate();
                }
            }

            database.CacheManager.gI().remove("players_" + playerName);
            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ BUFF TOÀN DIỆN CHO PLAYER OFFLINE [" + playerName + "] THÀNH CÔNG!\n" +
                    "- Max Level 150, 2 Tỷ Beri/Ruby/Extol\n" +
                    "- Full Hoàn Tất Nhiệm Vụ Chính & Mở Khóa Full Bản Đồ\n" +
                    "- Full Cấp Ác Quỷ Cấp 5 100% cho All Kỹ Năng\n" +
                    "- Full 6 Đá Thần Thoại & Kích Ẩn Bộc Phá cho Toàn Bộ Trang Bị Đang Mặc!\n" +
                    "- Đã lưu vào MySQL và xóa cache."
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeAllBuffOfflinePlayer error: " + e.getMessage());
        }
    }

    public static void executeAllBuffOfflineDetu(Player admin, String masterName) {
        try (Connection conn = DbManager.gI().getConnect()) {
            int dtId = -1;
            int ownerId = -1;
            String itBodyStr = null;
            String skillStr = null;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT d.`id`, d.`owner_id`, d.`it_body`, d.`skill` FROM `players_detu` d JOIN `players` p ON d.`owner_id` = p.`id` WHERE p.`name` = ? LIMIT 1")) {
                ps.setString(1, masterName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        dtId = rs.getInt("id");
                        ownerId = rs.getInt("owner_id");
                        itBodyStr = rs.getString("it_body");
                        skillStr = rs.getString("skill");
                    }
                }
            }
            if (dtId == -1) {
                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Không tìm thấy đệ tử của [" + masterName + "] trong DB!");
                return;
            }

            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players_detu` SET `level` = 150 WHERE `id` = ?")) {
                psUp.setInt(1, dtId);
                psUp.executeUpdate();
            }

            if (skillStr != null && !skillStr.isEmpty()) {
                try {
                    Object parsed = JSONValue.parse(skillStr);
                    if (parsed instanceof JSONArray) {
                        JSONArray arr = (JSONArray) parsed;
                        for (Object o : arr) {
                            if (o instanceof JSONObject) {
                                JSONObject skObj = (JSONObject) o;
                                skObj.put("lvdevil", 5);
                                skObj.put("devilpercent", 100);
                            }
                        }
                        try (PreparedStatement psSk = conn.prepareStatement("UPDATE `players_detu` SET `skill` = ? WHERE `id` = ?")) {
                            psSk.setString(1, arr.toJSONString());
                            psSk.setInt(2, dtId);
                            psSk.executeUpdate();
                        }
                    }
                } catch (Exception ignored) {}
            }

            if (itBodyStr != null && !itBodyStr.isEmpty()) {
                String newBody = buffItBodyJsonString(itBodyStr, 2, 4);
                try (PreparedStatement psBd = conn.prepareStatement("UPDATE `players_detu` SET `it_body` = ? WHERE `id` = ?")) {
                    psBd.setString(1, newBody);
                    psBd.setInt(2, dtId);
                    psBd.executeUpdate();
                }
            }

            database.CacheManager.gI().remove("players_detu_" + ownerId);
            database.CacheManager.gI().remove("players_" + masterName + "_detu");
            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ BUFF TOÀN DIỆN CHO ĐỆ TỬ OFFLINE CỦA [" + masterName + "] THÀNH CÔNG!\n" +
                    "- Max Level 150\n" +
                    "- Full Cấp Ác Quỷ Cấp 5 100% cho All Kỹ Năng\n" +
                    "- Full 6 Đá Thần Thoại & Kích Ẩn Bộc Phá cho Toàn Bộ Trang Bị Đang Mặc!\n" +
                    "- Đã lưu vào MySQL và xóa cache."
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeAllBuffOfflineDetu error: " + e.getMessage());
        }
    }

    public static String buffItBodyJsonString(String jsonStr, int buildType, int kichAnId) {
        try {
            Object parsed = JSONValue.parse(jsonStr);
            if (parsed instanceof JSONArray) {
                JSONArray arr = (JSONArray) parsed;
                int[] gemIds = getGemsForBuild(buildType);
                JSONArray newArr = new JSONArray();
                for (Object o : arr) {
                    if (o instanceof JSONObject) {
                        JSONObject itObj = (JSONObject) o;
                        long itId = itObj.containsKey("id") ? ((Long) itObj.get("id")) : -1;
                        ItemTemplate3 tpl = (itId >= 0) ? ItemTemplate3.get_it_by_id((short) itId) : null;
                        boolean isHeartOrThanTrang = (itId == 11000 || (tpl != null && (tpl.typeEquip == 6 || tpl.isThanTrang())));

                        if (isHeartOrThanTrang) {
                            itObj.put("gems", 0);
                            itObj.put("gem_ids", new JSONArray());
                        } else {
                            itObj.put("gems", 6);
                            JSONArray gemArr = new JSONArray();
                            for (int gid : gemIds) {
                                gemArr.add(gid);
                            }
                            itObj.put("gem_ids", gemArr);
                        }
                        itObj.put("hidden", kichAnId);
                        int curColor = itObj.containsKey("color") ? ((Long) itObj.get("color")).intValue() : 0;
                        itObj.put("color", Math.max(2, curColor));
                        newArr.add(itObj);
                    }
                }
                return newArr.toJSONString();
            }
        } catch (Exception ignored) {}
        return jsonStr;
    }

    public static void executeBuffDevilSkillsOffline(Player admin, AdminTarget cur) {
        if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `skill` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, cur.name);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int pId = rs.getInt("id");
                        String skillStr = rs.getString("skill");
                        if (skillStr != null && !skillStr.isEmpty()) {
                            Object parsed = JSONValue.parse(skillStr);
                            if (parsed instanceof JSONArray) {
                                JSONArray arr = (JSONArray) parsed;
                                for (Object o : arr) {
                                    if (o instanceof JSONObject) {
                                        JSONObject skObj = (JSONObject) o;
                                        skObj.put("lvdevil", 5);
                                        skObj.put("devilpercent", 100);
                                    }
                                }
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `skill` = ? WHERE `id` = ?")) {
                                    psUp.setString(1, arr.toJSONString());
                                    psUp.setInt(2, pId);
                                    psUp.executeUpdate();
                                }
                                database.CacheManager.gI().remove("players_" + cur.name);
                                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã buff Max Cấp Ác Quỷ cho Player Offline [" + cur.name + "]!");
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "executeBuffDevilSkillsOffline error: " + e.getMessage());
            }
        } else if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER_DETU) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement(
                     "SELECT d.`id`, d.`owner_id`, d.`skill` FROM `players_detu` d JOIN `players` p ON d.`owner_id` = p.`id` WHERE p.`name` = ? LIMIT 1")) {
                psSel.setString(1, cur.name);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int dtId = rs.getInt("id");
                        int ownerId = rs.getInt("owner_id");
                        String skillStr = rs.getString("skill");
                        if (skillStr != null && !skillStr.isEmpty()) {
                            Object parsed = JSONValue.parse(skillStr);
                            if (parsed instanceof JSONArray) {
                                JSONArray arr = (JSONArray) parsed;
                                for (Object o : arr) {
                                    if (o instanceof JSONObject) {
                                        JSONObject skObj = (JSONObject) o;
                                        skObj.put("lvdevil", 5);
                                        skObj.put("devilpercent", 100);
                                    }
                                }
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players_detu` SET `skill` = ? WHERE `id` = ?")) {
                                    psUp.setString(1, arr.toJSONString());
                                    psUp.setInt(2, dtId);
                                    psUp.executeUpdate();
                                }
                                database.CacheManager.gI().remove("players_detu_" + ownerId);
                                database.CacheManager.gI().remove("players_" + cur.name + "_detu");
                                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã buff Max Cấp Ác Quỷ cho Đệ tử Offline của [" + cur.name + "]!");
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "executeBuffDevilSkillsOffline detu error: " + e.getMessage());
            }
        }
    }

    public static void smartKhamOfflineBody(Player admin, String targetName, int buildType) {
        AdminTarget cur = getCurrentTarget(admin);
        if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `it_body` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, targetName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int pId = rs.getInt("id");
                        String itBodyStr = rs.getString("it_body");
                        if (itBodyStr != null && !itBodyStr.isEmpty()) {
                            String newBody = buffItBodyJsonString(itBodyStr, buildType, 4);
                            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `it_body` = ? WHERE `id` = ?")) {
                                psUp.setString(1, newBody);
                                psUp.setInt(2, pId);
                                psUp.executeUpdate();
                            }
                            database.CacheManager.gI().remove("players_" + targetName);
                            if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã khảm full 6 lỗ cho toàn bộ trang bị của Player Offline [" + targetName + "]!");
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "smartKhamOfflineBody error: " + e.getMessage());
            }
        } else if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER_DETU) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement(
                     "SELECT d.`id`, d.`owner_id`, d.`it_body` FROM `players_detu` d JOIN `players` p ON d.`owner_id` = p.`id` WHERE p.`name` = ? LIMIT 1")) {
                psSel.setString(1, targetName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int dtId = rs.getInt("id");
                        int ownerId = rs.getInt("owner_id");
                        String itBodyStr = rs.getString("it_body");
                        if (itBodyStr != null && !itBodyStr.isEmpty()) {
                            String newBody = buffItBodyJsonString(itBodyStr, buildType, 4);
                            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players_detu` SET `it_body` = ? WHERE `id` = ?")) {
                                psUp.setString(1, newBody);
                                psUp.setInt(2, dtId);
                                psUp.executeUpdate();
                            }
                            database.CacheManager.gI().remove("players_detu_" + ownerId);
                            database.CacheManager.gI().remove("players_" + targetName + "_detu");
                            if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã khảm full 6 lỗ cho toàn bộ trang bị của Đệ tử Offline [" + targetName + "]!");
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "smartKhamOfflineBody detu error: " + e.getMessage());
            }
        }
    }

    public static void kichAnOfflineBody(Player admin, String targetName, int kichAnId) {
        AdminTarget cur = getCurrentTarget(admin);
        if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `it_body` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, targetName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int pId = rs.getInt("id");
                        String itBodyStr = rs.getString("it_body");
                        if (itBodyStr != null && !itBodyStr.isEmpty()) {
                            Object parsed = JSONValue.parse(itBodyStr);
                            if (parsed instanceof JSONArray) {
                                JSONArray arr = (JSONArray) parsed;
                                for (Object o : arr) {
                                    if (o instanceof JSONObject) {
                                        JSONObject itObj = (JSONObject) o;
                                        itObj.put("hidden", kichAnId);
                                        int curColor = itObj.containsKey("color") ? ((Long) itObj.get("color")).intValue() : 0;
                                        itObj.put("color", Math.max(2, curColor));
                                    }
                                }
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `it_body` = ? WHERE `id` = ?")) {
                                    psUp.setString(1, arr.toJSONString());
                                    psUp.setInt(2, pId);
                                    psUp.executeUpdate();
                                }
                                database.CacheManager.gI().remove("players_" + targetName);
                                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã gán kích ẩn cho toàn bộ trang bị của Player Offline [" + targetName + "]!");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "kichAnOfflineBody error: " + e.getMessage());
            }
        } else if (cur.type == AdminTarget.TYPE_OFFLINE_PLAYER_DETU) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psSel = conn.prepareStatement(
                     "SELECT d.`id`, d.`owner_id`, d.`it_body` FROM `players_detu` d JOIN `players` p ON d.`owner_id` = p.`id` WHERE p.`name` = ? LIMIT 1")) {
                psSel.setString(1, targetName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        int dtId = rs.getInt("id");
                        int ownerId = rs.getInt("owner_id");
                        String itBodyStr = rs.getString("it_body");
                        if (itBodyStr != null && !itBodyStr.isEmpty()) {
                            Object parsed = JSONValue.parse(itBodyStr);
                            if (parsed instanceof JSONArray) {
                                JSONArray arr = (JSONArray) parsed;
                                for (Object o : arr) {
                                    if (o instanceof JSONObject) {
                                        JSONObject itObj = (JSONObject) o;
                                        itObj.put("hidden", kichAnId);
                                        int curColor = itObj.containsKey("color") ? ((Long) itObj.get("color")).intValue() : 0;
                                        itObj.put("color", Math.max(2, curColor));
                                    }
                                }
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players_detu` SET `it_body` = ? WHERE `id` = ?")) {
                                    psUp.setString(1, arr.toJSONString());
                                    psUp.setInt(2, dtId);
                                    psUp.executeUpdate();
                                }
                                database.CacheManager.gI().remove("players_detu_" + ownerId);
                                database.CacheManager.gI().remove("players_" + targetName + "_detu");
                                if (admin.getService() != null) admin.getService().send_box_ThongBao_OK("Đã gán kích ẩn cho toàn bộ trang bị của Đệ tử Offline [" + targetName + "]!");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "kichAnOfflineBody detu error: " + e.getMessage());
            }
        }
    }

    // ==============================================================
    //  2. BUFF ADMIN FULL STATS
    // ==============================================================

    private void buffAdminFullStats(Player p) {
        try {
            p.updateVnd(2_000_000_000);
            p.update_ruby(2_000_000_000);
            p.update_vang(2_000_000_000);
            p.level = 150;
            p.pointAttribute = (short) Level.get_total_point_by_level(p.level);
            p.hp = p.ability.get_hp_max(true);
            p.mp = p.ability.get_mp_max(true);
            p.updateMoney();
            
            // Buff full cấp ác quỷ cho all kỹ năng
            buffFullDevilSkills(p, false);

            p.update_info_to_all();
            p.getService().Main_char_Info(true);

            p.getService().send_box_ThongBao_OK(
                "ĐÃ BUFF ADMIN FULL CHỈ SỐ, TÀI NGUYÊN & ÁC QUỶ\n" +
                "- 2 Tỷ Extol: Trái Bá Vương\n" +
                "- 2 Tỷ Ruby: Trái Thượng Cấp\n" +
                "- 2 Tỷ Beri: Vàng\n" +
                "- Level Max: Lv.150\n" +
                "- Full Cấp Ác Quỷ Cấp 5 100% Cho Toàn Bộ Kỹ Năng!\n" +
                "- Phục Hồi 100% HP / MP Max!"
            );
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "buffAdminFullStats error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  2.1. BUFF FULL CẤP ÁC QUỶ & KỸ NĂNG (DEVIL FRUIT BUFF)
    // ==============================================================

    public static void openBuffDevilFruitMenuStatic(Player p) throws IOException {
        new AdminSystemMenu().openBuffDevilFruitMenu(p);
    }

    public void openBuffDevilFruitMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_BUFF_DEVIL_FRUIT,
            "BUFF FULL CẤP ÁC QUỶ & KỸ NĂNG",
            new String[]{
                "1. Buff Full Cấp Ác Quỷ Cấp 5 Cho All Kỹ Năng Chọn Đối Tượng",
                "2. Ăn Trái Ác Quỷ: Chọn 20 Loại - Min/Max Cấp Độ",
                "3. Nhận & Buff Max Trái Ánh Sáng: Pika Pika - Kizaru",
                "4. Nhận & Buff Max Trái Sấm Sét: Goro Goro - Enel",
                "5. Nhận & Buff Max Trái Nham Thạch: Magu Magu - Akainu",
                "6. Nhận & Buff Max Trái Băng: Hie Hie - Aokiji",
                "7. Nhận & Buff Max Trái Lửa: Mera Mera - Ace",
                "8. Nhận & Buff Max Trái Bóng Tối: Yami Yami - Teach",
                "9. Nhận & Buff Max Trái Tình Yêu: Mero Mero - Hancock",
                "10. Nhận & Buff Max Trái Nika: Gear 5 - Thần Mặt Trời",
                "11. Nhận & Buff Max Trái Cao Su: Gomu Gomu - Luffy",
                "12. Nhận & Buff Max Trái Cát: Suna Suna - Crocodile",
                "13. Nhận & Buff Max Trái Khói: Moku Moku - Smoker",
                "14. Nhận 999 Đá Ác Quỷ & 99 Rương Đại Ác Quỷ",
                "Quay lại"
            },
            new short[]{155, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 110, 134}
        );
    }

    private void handleBuffDevilFruitMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                openDevilSkillsTargetPicker(p);
                break;
            case 1:
                openEatDevilSelectMenu(p, 0);
                break;
            case 2:
                giveAndBuffDevilFruit(p, 4869, "Trái Ánh Sáng: Pika Pika");
                break;
            case 3:
                giveAndBuffDevilFruit(p, 4160, "Trái Sấm Sét: Goro Goro");
                break;
            case 4:
                giveAndBuffDevilFruit(p, 4161, "Trái Nham Thạch: Magu Magu");
                break;
            case 5:
                giveAndBuffDevilFruit(p, 4090, "Trái Băng: Hie Hie");
                break;
            case 6:
                giveAndBuffDevilFruit(p, 4033, "Trái Lửa: Mera Mera");
                break;
            case 7:
                giveAndBuffDevilFruit(p, 4427, "Trái Bóng Tối: Yami Yami");
                break;
            case 8:
                giveAndBuffDevilFruit(p, 4870, "Trái Tình Yêu: Mero Mero");
                break;
            case 9:
                giveAndBuffDevilFruit(p, 4873, "Trái Nika: Gear 5");
                break;
            case 10:
                giveAndBuffDevilFruit(p, 4032, "Trái Cao Su: Gomu Gomu");
                break;
            case 11:
                giveAndBuffDevilFruit(p, 4034, "Trái Cát: Suna Suna");
                break;
            case 12:
                giveAndBuffDevilFruit(p, 4088, "Trái Khói: Moku Moku");
                break;
            case 13:
                if (p.item != null) {
                    p.item.add_item_bag47(7, (short) 9, 999);
                    p.item.add_item_bag47(4, (short) 158, 99);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đã thêm 999 Đá Ác Quỷ: ID 9và 99 Rương Đại Ác Quỷ: ID 158vào hành trang!");
                }
                break;
            case 14:
                openMain(p);
                break;
        }
    }

    /**
     * Buff Full Cấp Ác Quỷ (+5) và 100% tiến độ cường hóa cho toàn bộ kỹ năng có thể nâng cấp ác quỷ.
     * Bao gồm: 3 kỹ năng chủ động môn phái, toàn bộ kỹ năng Trái Ác Quỷ và các kỹ năng môn phái.
     */
    public static void buffFullDevilSkills(Player p, boolean notify) {
        try {
            if (p == null || p.skill_point == null) return;
            int count = 0;
            for (Skill_info sk : p.skill_point) {
                if (sk == null || sk.temp == null) continue;

                boolean isClassActive = (sk.temp.ID == 0 || sk.temp.ID == 1 || sk.temp.ID == 2);
                boolean isDevilFruit = (sk.temp.ID >= 2000 || sk.temp.typeDevil > 0);
                int idx = sk.temp.indexSkillInServer;
                boolean isDevilIndex = ((idx >= 475 && idx <= 486) || (idx >= 512 && idx <= 551)) || (idx >= 656 && idx <= 659) || (idx >= 791 && idx <= 802);

                if (isClassActive || isDevilFruit || isDevilIndex || isClassSpecificSkill(sk)) {
                    // Nếu kỹ năng chưa học (Lv_RQ = -1), tự động học cấp 1
                    if (sk.temp.Lv_RQ == -1) {
                        Skill_Template.learn_skill(sk);
                    }
                    // Nâng cấp level kỹ năng lên tối đa nếu là skill có nhiều cấp
                    if (isDevilFruit && sk.temp.Lv_RQ > 0) {
                        for (int lvl = sk.temp.Lv_RQ; lvl < 30; lvl++) {
                            Skill_Template.upgrade_skill(sk, p.clazz);
                        }
                    }
                    // Set Max Cấp Cường Hóa Ác Quỷ (+5) và 100%
                    sk.lvdevil = 5;
                    sk.devilpercent = 100;
                    count++;
                }
            }

            // Đăng ký cooldown 0 vào time_use_skill
            if (p.time_use_skill != null && p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        p.time_use_skill.put(sk.temp.ID, 0L);
                    }
                }
            }

            // Đồng bộ toàn bộ dữ liệu tới Client và Map
            p.send_skill();
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(false);
                if (notify) {
                    p.getService().send_box_ThongBao_OK(
                        "ĐÃ BUFF FULL CẤP ÁC QUỶ THÀNH CÔNG!\n" +
                        "- Đã nâng cấp Max Cấp 5 Cấp 5 Ác Quỷ cho " + count + " Kỹ Năng!\n" +
                        "- Tiến độ Cường Hóa Ác Quỷ: 100%: Max\n" +
                        "- Kích hoạt 100% Hiệu Ứng Thức Tỉnh & X2 Sát Thương Kỹ Năng!"
                    );
                }
            }
            p.flush(p, false);
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "buffFullDevilSkills error: " + e.getMessage());
        }
    }

    /**
     * Gán Trái Ác Quỷ mới, tự động học max level kỹ năng và buff Full Cấp Ác Quỷ (+5) 100%.
     */
    public static void giveAndBuffDevilFruit(Player p, int fruitId, String fruitName) {
        try {
            if (p == null) return;

            // 1. Gán bộ kỹ năng của Trái Ác Quỷ
            p.get_skill_taq_new(fruitId);

            // 2. Học và nâng max cấp kỹ năng Ác Quỷ + Set Cấp Ác Quỷ = 5, Phần Trăm = 100%
            if (p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        if (sk.temp.ID >= 2000 || sk.temp.typeDevil > 0) {
                            if (sk.temp.Lv_RQ == -1) {
                                Skill_Template.learn_skill(sk);
                            }
                            if (sk.temp.Lv_RQ > 0) {
                                for (int lvl = sk.temp.Lv_RQ; lvl < 30; lvl++) {
                                    Skill_Template.upgrade_skill(sk, p.clazz);
                                }
                            }
                            sk.lvdevil = 5;
                            sk.devilpercent = 100;
                        } else if (sk.temp.ID == 0 || sk.temp.ID == 1 || sk.temp.ID == 2 || isClassSpecificSkill(sk)) {
                            if (sk.temp.Lv_RQ == -1) {
                                Skill_Template.learn_skill(sk);
                            }
                            sk.lvdevil = 5;
                            sk.devilpercent = 100;
                        }
                    }
                }
            }

            // 3. Đăng ký thời gian hồi chiêu
            if (p.time_use_skill != null && p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        p.time_use_skill.put(sk.temp.ID, 0L);
                    }
                }
            }

            // 4. Đồng bộ toàn bộ client & map
            p.send_skill();
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(false);
                p.getService().send_box_ThongBao_OK(
                    "ĐÃ NHẬN & BUFF FULL " + fruitName.toUpperCase() + "!\n" +
                    "- Đã hấp thụ thành công " + fruitName + ".\n" +
                    "- Toàn bộ Kỹ Năng Ác Quỷ đã đạt Cấp Tối Đa & Cường Hóa Cấp 5 Cấp 5 100%.\n" +
                    "- Toàn bộ Chiêu Môn Phái đã đạt Cường Hóa Ác Quỷ Cấp 5 Cấp 5 100%.\n" +
                    "- Kích hoạt 100% Hiệu Ứng Thức Tỉnh & X2 Sát Thương Kỹ Năng!"
                );
            }
            p.flush(p, false);
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "giveAndBuffDevilFruit error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  2.2. SIÊU BUFF KỸ NĂNG & TRẠNG THÁI THẦN THOẠI
    // ==============================================================

    public void openBuffSkillsAdvancedMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_BUFF_SKILLS_ADVANCED,
            "SIÊU BUFF KỸ NĂNG & BUFF THẦN",
            new String[]{
                "1. Max Cấp Độ Toàn Bộ Kỹ Năng Đang Có (Level 1 -> Max)",
                "2. Mở Khóa Đủ Bộ Kỹ Năng 5 Hệ Phái (Full 5 Phái)",
                "3. Max Cấp Độ Kỹ Năng Trái Ác Quỷ (+5 100% Cho All Skill)",
                "4. Kích Hoạt Buff Trạng Thái Thần Thoại (Bất Tử, Haki, Nika...)",
                "5. Xóa Toàn Bộ Buff & Trạng Thái Tạm Thời",
                "Quay Lại"
            },
            new short[]{133, 155, 133, 110, 118, 134}
        );
    }

    private void handleBuffSkillsAdvanced(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                buffMaxAllSkills(p);
                break;
            case 1:
                unlockAllClassSkills(p);
                break;
            case 2:
                buffFullDevilSkills(p, true);
                break;
            case 3:
                openActiveBuffSelect(p);
                break;
            case 4:
                clearAllPlayerBuffs(p);
                break;
            case 5:
                openSuperBuffRootMenu(p);
                break;
        }
    }

    public void openActiveBuffSelect(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_ACTIVE_BUFF_SELECT,
            "CHỌN BUFF TRẠNG THÁI THẦN THOẠI",
            new String[]{
                "1. Bất Tử Toàn Diện: Miễn Nhiễm Toàn Bộ Sát Thương & Khống Chế",
                "2. Haki Bá Vương: Thức Tỉnh Sức Mạnh, +200% Sát Thương & Bạo Kích",
                "3. Gear 5 Thần Mặt Trời Nika: Siêu Trạng Thái +300% Toàn Diện",
                "4. Lôi Thần Enel: Thiên Lôi Phản Kích +80% Phản Đòn & Giật Điện",
                "5. Hỏa Nham Akainu: Dung Nham Thiêu Đốt +200% ST Chuẩn & Bộc Phá",
                "6. Phượng Hoàng Lam Hỏa Marco: Tái Sinh Bất Diệt Siêu Hồi Máu",
                "Quay Lại"
            },
            new short[]{133, 155, 155, 133, 133, 133, 134}
        );
    }

    private void handleActiveBuffSelect(Player p, int index) throws IOException {
        if (index >= 0 && index <= 5) {
            applyActiveGodBuff(p, index);
        } else {
            openBuffSkillsAdvancedMenu(p);
        }
    }

    public static void buffMaxAllSkills(Player p) {
        if (p == null || p.skill_point == null) return;
        try {
            int count = 0;
            for (Skill_info sk : p.skill_point) {
                if (sk != null && sk.temp != null) {
                    if (sk.temp.Lv_RQ == -1) {
                        Skill_Template.learn_skill(sk);
                        count++;
                    }
                    if (sk.temp.Lv_RQ > 0) {
                        for (int lvl = sk.temp.Lv_RQ; lvl < 30; lvl++) {
                            Skill_Template.upgrade_skill(sk, p.clazz);
                        }
                        count++;
                    }
                    sk.lvdevil = 5;
                    sk.devilpercent = 100;
                }
            }
            p.send_skill();
            p.setAbility();
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(false);
                p.getService().send_box_ThongBao_OK("Đã nâng tối đa cấp độ và Max Cấp Ác Quỷ (+5 100%) cho " + count + " kỹ năng đang sở hữu!");
            }
            p.flush(p, false);
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "buffMaxAllSkills error: " + e.getMessage());
        }
    }

    public static void unlockAllClassSkills(Player p) {
        if (p == null) return;
        try {
            if (p.skill_point == null) p.skill_point = new ArrayList<>();
            Set<Integer> existing = new HashSet<>();
            for (Skill_info sk : p.skill_point) {
                if (sk != null && sk.temp != null) existing.add(sk.temp.ID);
            }
            int added = 0;
            for (Skill_Template st : Skill_Template.ENTRYS) {
                if (st != null && !existing.contains(st.ID) && (st.typeDevil == 0 || st.typeSkill == 0)) {
                    Skill_info sk = new Skill_info();
                    sk.exp = 0;
                    sk.temp = st;
                    Skill_Template.learn_skill(sk);
                    if (sk.temp.Lv_RQ > 0) {
                        for (int lvl = sk.temp.Lv_RQ; lvl < 30; lvl++) {
                            Skill_Template.upgrade_skill(sk, p.clazz);
                        }
                    }
                    sk.lvdevil = 5;
                    sk.devilpercent = 100;
                    p.skill_point.add(sk);
                    existing.add(st.ID);
                    added++;
                }
            }
            p.send_skill();
            p.setAbility();
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(false);
                p.getService().send_box_ThongBao_OK("Đã mở khóa và học thành công " + added + " kỹ năng của 5 hệ phái!");
            }
            p.flush(p, false);
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "unlockAllClassSkills error: " + e.getMessage());
        }
    }

    public static void applyActiveGodBuff(Player p, int buffType) {
        if (p == null) return;
        try {
            long dur = 3600_000L; // 1 giờ
            String buffName = "";
            switch (buffType) {
                case 0:
                    p.add_new_eff(7, 1, dur);
                    p.add_new_eff(9, 1, dur);
                    p.add_new_eff(104, 500, dur);
                    p.add_new_eff(117, 500, dur);
                    buffName = "BẤT TỬ TOÀN DIỆN (Miễn Toàn Bộ Sát Thương & Khống Chế)";
                    break;
                case 1:
                    p.add_new_eff(101, 200, dur);
                    p.add_new_eff(110, 100, dur);
                    p.add_new_eff(111, 200, dur);
                    p.add_new_eff(113, 100, dur);
                    buffName = "HAKI BÁ VƯƠNG (Hào Khí Thức Tỉnh & Sát Thương Cực Đại)";
                    break;
                case 2:
                    p.add_new_eff(11, 1, dur);
                    p.add_new_eff(101, 300, dur);
                    p.add_new_eff(104, 300, dur);
                    p.add_new_eff(117, 300, dur);
                    p.add_new_eff(112, 60, dur);
                    buffName = "GEAR 5 NIKA (Thần Mặt Trời Toàn Năng)";
                    break;
                case 3:
                    p.add_new_eff(114, 80, dur);
                    p.add_new_eff(101, 150, dur);
                    p.add_new_eff(157, 100, dur);
                    buffName = "LÔI THẦN ENEL (Thiên Lôi Phản Kích & Sấm Sét Giật)";
                    break;
                case 4:
                    p.add_new_eff(18, 1, dur);
                    p.add_new_eff(157, 200, dur);
                    p.add_new_eff(101, 250, dur);
                    buffName = "HỎA NHAM AKAINU (Thiêu Đốt Dung Nham & Bộc Phá)";
                    break;
                case 5:
                    p.add_new_eff(0, 50000, dur);
                    p.add_new_eff(117, 200, dur);
                    p.add_new_eff(104, 200, dur);
                    buffName = "PHƯỢNG HOÀNG LAM HỎA MARCO (Tái Sinh Bất Diệt)";
                    break;
            }
            if (p.ability != null) p.ability.recalculatePlayerStats(p);
            p.hp = p.ability.get_hp_max(true);
            p.mp = p.ability.get_mp_max(true);
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(true);
                p.getService().send_box_ThongBao_OK("ĐÃ KÍCH HOẠT THÀNH CÔNG BUFF:\n" + buffName + "\n- Thời gian: 60 Phút\n- HP/MP đã hồi phục 100%!");
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "applyActiveGodBuff error: " + e.getMessage());
        }
    }

    public static void clearAllPlayerBuffs(Player p) {
        if (p == null) return;
        try {
            if (p.list_eff != null) p.list_eff.clear();
            if (p.ability != null) p.ability.recalculatePlayerStats(p);
            p.update_info_to_all();
            if (p.getService() != null) {
                p.getService().Main_char_Info(true);
                p.getService().send_box_ThongBao_OK("Đã xóa sạch toàn bộ các hiệu ứng Buff tạm thời!");
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "clearAllPlayerBuffs error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  3. ĐỔI MÔN PHÁI (SWITCH CLAZZ)
    // ==============================================================

    public static String getClassName(int clazz) {
        switch (clazz) {
            case 1: return "Class 1: Võ Sĩ: Luffy";
            case 2: return "Class 2: Kiếm Sĩ: Zoro";
            case 3: return "Class 3: Đầu Bếp: Sanji";
            case 4: return "Class 4: Hoa Tiêu: Nami";
            case 5: return "Class 5: Xạ Thủ: Usopp";
            default: return "Class " + clazz;
        }
    }

    public void openSwitchClazz(Player p) throws IOException {
        p.getService().openDynamicMenu(MENU_SWITCH_CLAZZ,
            "Đổi Môn Phái: Hiện tại: " + getClassName(p.clazz) + ")",
            new String[]{
                "Class 1: Võ Sĩ: Luffy",
                "Class 2: Kiếm Sĩ: Zoro",
                "Class 3: Đầu Bếp: Sanji",
                "Class 4: Hoa Tiêu: Nami",
                "Class 5: Xạ Thủ: Usopp",
                "Quay lại"
            },
            new short[]{110, 110, 110, 110, 110, 134}
        );
    }

    private void handleSwitchClazz(Player p, int choice) throws IOException {
        if (choice == 5) {
            openMain(p);
            return;
        }
        if (choice < 0 || choice >= 5) return;
        byte newClazz = (byte) (choice + 1);
        if (p.clazz == newClazz) {
            p.getService().send_box_ThongBao_OK("Bạn đang ở " + getClassName(newClazz) + " rồi!");
            return;
        }

        p.tempTargetClazz = newClazz;
        openSwitchClazzTypeMenu(p, newClazz);
    }

    public void openSwitchClazzTypeMenu(Player p, byte newClazz) throws IOException {
        p.getService().openDynamicMenu(MENU_SWITCH_CLAZZ_TYPE,
            "Đổi Sang " + getClassName(newClazz) + " - Chọn Loại Đổi:",
            new String[]{
                "1. Đổi Phái [Tân Thủ]: Reset Đồ & 4 Kỹ Năng Khởi Đầu",
                "2. Đổi Phái [Chuyển Hóa]: Giữ Cấp Đồ & Chuyển Kỹ Năng",
                "Quay lại"
            },
            new short[]{110, 133, 134}
        );
    }

    private void handleSwitchClazzType(Player p, int choice) throws IOException {
        if (choice == 2) {
            openSwitchClazz(p);
            return;
        }
        byte newClazz = p.tempTargetClazz;
        if (newClazz < 1 || newClazz > 5) {
            openSwitchClazz(p);
            return;
        }

        if (choice == 0) {
            changeClassReset(p, newClazz);
        } else if (choice == 1) {
            changeClassConvert(p, newClazz);
        }
    }

    /**
     * Loại 1: Clean đồ và set đồ tân thủ từ đầu, clean skill của phái cũ và set lại 4 kỹ năng khởi đầu.
     * 4 kỹ năng khởi đầu gồm: 3 kỹ năng chủ động thường + 1 kỹ năng trên biển.
     * Cấp độ nhân vật, EXP nhân vật, và toàn bộ Trái Ác Quỷ (kèm exp/level ác quỷ) giữ nguyên 100%.
     */
    public static void changeClassReset(Player p, byte newClazz) {
        try {
            if (p == null) return;
            p.clazz = newClazz;

            // 1. Cập nhật an toàn ngoại hình theo class mới (KHÔNG xóa fashion, itfashionP, itemboat)
            int fashionHairId = 0, defaultHair = 1;
            switch (newClazz) {
                case 1: fashionHairId = 5; defaultHair = 1; break;
                case 2: fashionHairId = 1; defaultHair = 24; break;
                case 3: fashionHairId = 2; defaultHair = 28; break;
                case 4: fashionHairId = 3; defaultHair = 32; break;
                case 5: fashionHairId = 4; defaultHair = 36; break;
            }
            p.head = 0;
            p.hair = (short) defaultHair;

            if (p.itfashionP == null) {
                p.itfashionP = new ArrayList<>();
            }
            boolean foundHairFashion = false;
            for (template.ItemFashionP ifp : p.itfashionP) {
                if (ifp != null && ifp.category == 103) {
                    ifp.id = (short) fashionHairId;
                    ifp.icon = (short) defaultHair;
                    foundHairFashion = true;
                    break;
                }
            }
            if (!foundHairFashion) {
                p.itfashionP.add(new template.ItemFashionP((short) fashionHairId, (short) defaultHair, (byte) 103, true));
            }

            // 2. Clean trang bị đang mặc (it_body) và set đồ tân thủ từ đầu
            if (p.item != null) {
                p.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
                List<Item_wear> equips = model.DeTu.getDefaultEquip(newClazz);
                for (Item_wear w : equips) {
                    if (w != null && w.index >= 0 && w.index < p.item.it_body.length) {
                        p.item.it_body[w.index] = w;
                    }
                }
            }
            p.updateParts();

            // 3. Hoàn trả toàn bộ điểm tiềm năng
            p.pointAttribute = (short) template.Level.get_total_point_by_level(p.level);
            p.point1 = 1;
            p.point2 = 1;
            p.point3 = 1;
            p.point4 = 1;
            p.point5 = 0;

            // 4. Clean skill của phái cũ và set 4 kỹ năng khởi đầu theo class mới
            List<Skill_info> keptSkills = new ArrayList<>();
            if (p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        if (getClassSkillSlot(sk) == -1) {
                            keptSkills.add(sk);
                        }
                    }
                }
            }
            p.skill_point = new ArrayList<>();

            // 4 kỹ năng khởi đầu của phái mới:
            // Chiêu 1 (Lv 1, exp 0)
            Skill_Template st1 = getMatchingClassSkillTemplate(newClazz, 1, (byte) 1);
            if (st1 != null) {
                Skill_info sk1 = new Skill_info();
                sk1.temp = st1;
                sk1.exp = 0;
                p.skill_point.add(sk1);
            }

            // Chiêu 2 (Lv_RQ -1, exp -1)
            Skill_Template st2 = getMatchingClassSkillTemplate(newClazz, 2, (byte) -1);
            if (st2 != null) {
                Skill_info sk2 = new Skill_info();
                sk2.temp = st2;
                sk2.exp = -1;
                p.skill_point.add(sk2);
            }

            // Chiêu 3 (Lv_RQ -1, exp -1)
            Skill_Template st3 = getMatchingClassSkillTemplate(newClazz, 3, (byte) -1);
            if (st3 != null) {
                Skill_info sk3 = new Skill_info();
                sk3.temp = st3;
                sk3.exp = -1;
                p.skill_point.add(sk3);
            }

            // Chiêu 4: Kỹ năng trên biển (Thủy Chiến) (Lv_RQ -1, exp -1)
            Skill_Template st4 = getMatchingClassSkillTemplate(newClazz, 4, (byte) -1);
            if (st4 != null) {
                Skill_info sk4 = new Skill_info();
                sk4.temp = st4;
                sk4.exp = -1;
                p.skill_point.add(sk4);
            }

            // Chiêu 6: Kỹ năng Buff Môn Phái (Lv_RQ -1, exp -1)
            Skill_Template st6 = getMatchingClassSkillTemplate(newClazz, 6, (byte) -1);
            if (st6 != null) {
                Skill_info sk6 = new Skill_info();
                sk6.temp = st6;
                sk6.exp = -1;
                p.skill_point.add(sk6);
            }

            // Giữ lại các skill không theo class (Trái Ác Quỷ, Haki, Dial, Phe Phái, Passive chung)
            p.skill_point.addAll(keptSkills);
            ensureDefaultHaki(p);

            // Đăng ký toàn bộ kỹ năng vào time_use_skill để dùng ngay
            if (p.time_use_skill != null && p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        p.time_use_skill.put(sk.temp.ID, 0L);
                    }
                }
            }

            // 5. Reset RMS Hotkey 4
            if (p.rms == null || p.rms.length < 11) {
                p.rms = new byte[11][];
                for (int i = 0; i < 11; i++) p.rms[i] = new byte[0];
            }
            p.rms[4] = new byte[]{0, 18};

            // 6. Đồng bộ toàn bộ dữ liệu chuẩn hóa
            syncClassChange(p);

            p.getService().send_box_ThongBao_OK(
                "ĐÃ ĐỔI SANG " + getClassName(newClazz).toUpperCase() + " [TÂN THỦ] THÀNH CÔNG!\n" +
                "- Đã làm sạch trang bị và nhận bộ trang bị Tân Thủ Class " + newClazz + ".\n" +
                "- Đã thiết lập 4 kỹ năng khởi đầu: 3 chiêu chủ động + 1 chiêu thủy chiến.\n" +
                "- Điểm tiềm năng đã được hoàn trả toàn bộ để bạn nâng lại theo hệ môn phái mới.\n" +
                "- Toàn bộ cấp độ nhân vật, EXP, Thời Trang, Thuyền, và Trái Ác Quỷ giữ nguyên 100%.\n" +
                "- Toàn bộ dữ liệu đã được đồng bộ chuẩn toàn map và lưu vào hệ thống."
            );
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "changeClassReset error: " + e.getMessage());
        }
    }

    /**
     * Loại 2: Giữ đồ và chuyển đổi trang bị sang phái mới (bảo toàn cấp cường hóa, hoàn mỹ, đá khảm, kích ẩn),
     * đồng thời chuyển đổi các kỹ năng môn phái sang kỹ năng tương ứng của phái mới.
     * Bảo toàn 100% cấp độ skill, exp skill, exp devil fruit (lvdevil, devilpercent), level & exp nhân vật.
     */
    public static void changeClassConvert(Player p, byte newClazz) {
        try {
            if (p == null) return;
            byte oldClazz = p.clazz;
            p.clazz = newClazz;

            // 1. Cập nhật an toàn ngoại hình theo class mới (KHÔNG xóa fashion, itfashionP, itemboat)
            int fashionHairId = 0, defaultHair = 1;
            switch (newClazz) {
                case 1: fashionHairId = 5; defaultHair = 1; break;
                case 2: fashionHairId = 1; defaultHair = 24; break;
                case 3: fashionHairId = 2; defaultHair = 28; break;
                case 4: fashionHairId = 3; defaultHair = 32; break;
                case 5: fashionHairId = 4; defaultHair = 36; break;
            }
            p.head = 0;
            p.hair = (short) defaultHair;

            if (p.itfashionP == null) {
                p.itfashionP = new ArrayList<>();
            }
            boolean foundHairFashion = false;
            for (template.ItemFashionP ifp : p.itfashionP) {
                if (ifp != null && ifp.category == 103) {
                    ifp.id = (short) fashionHairId;
                    ifp.icon = (short) defaultHair;
                    foundHairFashion = true;
                    break;
                }
            }
            if (!foundHairFashion) {
                p.itfashionP.add(new template.ItemFashionP((short) fashionHairId, (short) defaultHair, (byte) 103, true));
            }

            // 2. Chuyển hóa toàn bộ trang bị môn phái trong: it_body, bag3, box3, save_item_wear
            if (p.item != null) {
                convertItemList(p.item.it_body, newClazz);
                convertItemList(p.item.bag3, newClazz);
                convertItemList(p.item.box3, newClazz);
                convertItemList(p.item.save_item_wear, newClazz);
            }
            p.updateParts();

            // 3. Hoàn trả toàn bộ điểm tiềm năng để người chơi nâng điểm phù hợp với phái mới
            p.pointAttribute = (short) template.Level.get_total_point_by_level(p.level);
            p.point1 = 1;
            p.point2 = 1;
            p.point3 = 1;
            p.point4 = 1;
            p.point5 = 0;

            // 4. Chuyển hóa kỹ năng môn phái và bảo toàn 100% exp, level, devil stats
            convertSkills(p, oldClazz, newClazz);
            ensureDefaultHaki(p);

            // Đăng ký toàn bộ kỹ năng vào time_use_skill để dùng ngay
            if (p.time_use_skill != null && p.skill_point != null) {
                for (Skill_info sk : p.skill_point) {
                    if (sk != null && sk.temp != null) {
                        p.time_use_skill.put(sk.temp.ID, 0L);
                    }
                }
            }

            // 5. Đồng bộ toàn bộ dữ liệu chuẩn hóa
            syncClassChange(p);

            p.getService().send_box_ThongBao_OK(
                "ĐÃ CHUYỂN HÓA SANG " + getClassName(newClazz).toUpperCase() + " THÀNH CÔNG!\n" +
                "- Vũ khí, Nón, Áo, Quần đã chuyển sang Class " + newClazz + ": Đúng cấp, phẩm chất màu & Set.\n" +
                "- Cấp cường hóa Cấp 17, Hoàn Mỹ, Kích Ẩn, Đá Khảm & chỉ số ngọc khảm giữ nguyên 100%.\n" +
                "- Toàn bộ kỹ năng môn phái đã tự động chuyển đổi tương ứng: Bảo toàn cấp độ & EXP.\n" +
                "- Điểm tiềm năng đã được hoàn trả toàn bộ để bạn nâng lại theo hệ môn phái mới.\n" +
                "- Trái Ác Quỷ: EXP/Level, Haki, Dial, Thời Trang, Thuyền, Level & EXP nhân vật giữ nguyên 100%.\n" +
                "- Toàn bộ dữ liệu đã được đồng bộ chuẩn toàn map và lưu vào hệ thống."
            );
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "changeClassConvert error: " + e.getMessage());
        }
    }

    public static void convertItemWear(Item_wear wear, byte newClazz) {
        if (wear == null || wear.template == null) return;
        int typeEq = wear.template.typeEquip;

        // Chỉ chuyển đổi các trang bị môn phái (Vũ khí 0, Nón 1, Áo 3, Quần 5) thuộc phái khác
        // Các trang bị dùng chung (clazz == 0: Nhẫn, Dây chuyền, Tim, Dial, v.v.) hoặc đã thuộc newClazz giữ nguyên 100%
        if (wear.template.clazz != 0 && wear.template.clazz != newClazz && (typeEq == 0 || typeEq == 1 || typeEq == 3 || typeEq == 5)) {
            ItemTemplate3 oldTemp = wear.template;
            ItemTemplate3 newTemp = findMatchingTemplate(oldTemp, newClazz);
            if (newTemp != null) {
                wear.template = newTemp;

                // Giữ nguyên kích ẩn của người chơi nếu có, chỉ lấy từ template mới nếu chưa kích ẩn
                if (wear.valueKichAn == -1 && newTemp.valueKichAn != -1) {
                    wear.valueKichAn = newTemp.valueKichAn;
                }

                // Cập nhật lại option_item:
                // Nếu newTemp có option_item -> sao chép chuẩn từ template mới
                // Nếu newTemp không có option_item -> khởi tạo bằng initOptions() chuẩn theo level, color, phái mới
                if (newTemp.option_item != null && !newTemp.option_item.isEmpty()) {
                    wear.option_item = new ArrayList<>();
                    for (Option baseOp : newTemp.option_item) {
                        if (baseOp != null) {
                            wear.option_item.add(new Option(baseOp.id, baseOp.getParam()));
                        }
                    }
                } else {
                    wear.option_item = new ArrayList<>();
                    wear.initOptions();
                }

                // Bảo toàn và đồng bộ lại option_item_2 (Đá khảm / Inlay gems):
                if (wear.option_item_2 == null) {
                    wear.option_item_2 = new ArrayList<>();
                }
                if (wear.mdakham != null && wear.mdakham.length > 0) {
                    // Nếu option_item_2 bị rỗng do bất kỳ lý do gì, tái tạo lại toàn bộ từ mdakham
                    if (wear.option_item_2.isEmpty()) {
                        for (short gemId : wear.mdakham) {
                            if (gemId > 0) {
                                itemz.Rebuild_Item.add_op_ngoc_kham_new(wear, gemId);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void convertItemList(Item_wear[] items, byte newClazz) {
        if (items == null) return;
        for (int i = 0; i < items.length; i++) {
            convertItemWear(items[i], newClazz);
        }
    }

    public static void convertItemList(List<Item_wear> items, byte newClazz) {
        if (items == null) return;
        for (Item_wear wear : items) {
            convertItemWear(wear, newClazz);
        }
    }

    public static ItemTemplate3 findMatchingTemplate(ItemTemplate3 source, byte targetClazz) {
        if (source == null || ItemTemplate3.ENTRYS == null) return null;
        int typeEquip = source.typeEquip;
        int level = source.level;
        byte color = source.color;

        // 1. Tìm danh sách tất cả template cùng typeEquip, cùng targetClazz, cùng level, cùng color
        List<ItemTemplate3> candidates = new ArrayList<>();
        for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
            if (t != null && t.typeEquip == typeEquip && t.clazz == targetClazz && t.level == level && t.color == color) {
                candidates.add(t);
            }
        }

        if (candidates.size() == 1) {
            return candidates.get(0);
        } else if (candidates.size() > 1) {
            // Xác định xem source là Set 1 hay Set 2 (dựa vào đuôi tên " 2" hoặc "2")
            boolean sourceIsSet2 = (source.name != null && (source.name.endsWith(" 2") || source.name.endsWith("2")));
            for (ItemTemplate3 c : candidates) {
                boolean cIsSet2 = (c.name != null && (c.name.endsWith(" 2") || c.name.endsWith("2")));
                if (sourceIsSet2 == cIsSet2) {
                    return c;
                }
            }
            return candidates.get(0);
        }

        // 2. Fallback: tìm theo cùng typeEquip, targetClazz, ưu tiên level gần nhất, color gần nhất
        ItemTemplate3 best = null;
        int bestScore = Integer.MAX_VALUE;
        for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
            if (t != null && t.typeEquip == typeEquip && t.clazz == targetClazz) {
                int levelDiff = Math.abs(t.level - level);
                int colorDiff = Math.abs(t.color - color);
                int score = levelDiff * 1000 + colorDiff;
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                }
            }
        }
        if (best != null) return best;

        // 3. Fallback cuối cùng
        return findMatchingTemplate(typeEquip, targetClazz, level, color);
    }

    public static ItemTemplate3 findMatchingTemplate(int typeEquip, byte clazz, int level, byte color) {
        if (ItemTemplate3.ENTRYS == null) return null;
        ItemTemplate3 best = null;
        int bestScore = Integer.MAX_VALUE;

        // 1. Ưu tiên tìm chính xác cùng typeEquip, cùng clazz, cùng level, cùng color
        for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
            if (t != null && t.typeEquip == typeEquip && t.clazz == clazz) {
                int levelDiff = Math.abs(t.level - level);
                int colorDiff = Math.abs(t.color - color);
                int score = levelDiff * 1000 + colorDiff;
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                    if (score == 0) return t;
                }
            }
        }
        if (best != null) return best;

        // 2. Fallback tìm theo typeEquip
        for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
            if (t != null && t.typeEquip == typeEquip) {
                int levelDiff = Math.abs(t.level - level);
                int colorDiff = Math.abs(t.color - color);
                int score = levelDiff * 1000 + colorDiff;
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                }
            }
        }
        return best;
    }

    public static ItemTemplate3 findMatchingTemplate(int typeEquip, byte clazz, int level) {
        return findMatchingTemplate(typeEquip, clazz, level, (byte) 0);
    }

    public static boolean isClassSpecificSkill(Skill_info sk) {
        return getClassSkillSlot(sk) != -1;
    }

    /**
     * Xác định Slot của kỹ năng môn phái (1: Chiêu 1, 2: Chiêu 2, 3: Chiêu 3, 4: Thủy Chiến, 5: Chiêu Nộ, 6: Bị Động Phái).
     * Trả về -1 nếu là kỹ năng không theo phái (Trái Ác Quỷ, Haki, Dial, Phe Phái, Bị Động Chung).
     */
    public static int getClassSkillSlot(Skill_info sk) {
        if (sk == null || sk.temp == null) return -1;
        int idx = sk.temp.indexSkillInServer;
        int id = sk.temp.ID;

        // Slot 6: Chiêu Buff Môn Phái (487..511 hoặc ID 1010..1014: Luffy 487, Zoro 492, Sanji 497, Nami 502, Usopp 507)
        if ((idx >= 487 && idx <= 511) || (id >= 1010 && id <= 1014)) {
            return 6;
        }

        // Loại trừ các skill dùng chung / Haki / Dial / Trái Ác Quỷ
        if (id >= 2000 || sk.temp.typeDevil > 0) return -1;
        if (id == 1016 || id == 1017) return -1;
        if (idx == 779 || idx == 785) return -1;
        if (idx >= 661 && idx <= 666) return -1; // Dial
        if (idx >= 672 && idx <= 690) return -1; // Haki
        if (idx >= 691 && idx <= 702) return -1; // Hải quân / Hải tặc
        if ((idx >= 475 && idx <= 486) || (idx >= 512 && idx <= 551)) return -1; // Trái ác quỷ
        if (idx >= 656 && idx <= 659) return -1; // Trái ác quỷ
        if (idx >= 791 && idx <= 802) return -1; // Trái ác quỷ

        // Passive chung
        int[] commonPassives = new int[]{300, 305, 310, 315, 320, 325, 552, 557, 667};
        for (int cp : commonPassives) {
            if (idx >= cp && idx < cp + 5) return -1;
        }

        // Slot 5: Chiêu Nộ (566..655)
        if (idx >= 566 && idx <= 655) {
            return 5;
        }

        // Slot 1: Active 1 (ID == 0 hoặc trong các dải index)
        if (id == 0 || (idx >= 0 && idx <= 19) || (idx >= 60 && idx <= 79) || (idx >= 120 && idx <= 139) || (idx >= 180 && idx <= 199) || (idx >= 240 && idx <= 259) ||
            (idx >= 703 && idx <= 707) || (idx >= 718 && idx <= 722) || (idx >= 733 && idx <= 737) || (idx >= 748 && idx <= 752) || (idx >= 763 && idx <= 767)) {
            return 1;
        }

        // Slot 2: Active 2 (ID == 1 hoặc trong các dải index)
        if (id == 1 || (idx >= 20 && idx <= 39) || (idx >= 80 && idx <= 99) || (idx >= 140 && idx <= 159) || (idx >= 200 && idx <= 219) || (idx >= 260 && idx <= 279) ||
            (idx >= 708 && idx <= 712) || (idx >= 723 && idx <= 727) || (idx >= 738 && idx <= 742) || (idx >= 753 && idx <= 757) || (idx >= 768 && idx <= 772)) {
            return 2;
        }

        // Slot 3: Active 3 (ID == 2 hoặc trong các dải index)
        if (id == 2 || (idx >= 40 && idx <= 59) || (idx >= 100 && idx <= 119) || (idx >= 160 && idx <= 179) || (idx >= 220 && idx <= 239) || (idx >= 280 && idx <= 299) ||
            (idx >= 713 && idx <= 717) || (idx >= 728 && idx <= 732) || (idx >= 743 && idx <= 747) || (idx >= 758 && idx <= 762) || (idx >= 773 && idx <= 777)) {
            return 3;
        }

        // Slot 4: Thủy Chiến (ID == 3 hoặc 375..474)
        if (id == 3 || (idx >= 375 && idx <= 474)) {
            return 4;
        }

        return -1;
    }

    /**
     * Lấy Skill_Template tương ứng cho class mới tại slot và cấp độ (Lv_RQ) mong muốn.
     */
    public static Skill_Template getMatchingClassSkillTemplate(byte clazz, int slot, byte lvRq) {
        if (Skill_Template.ENTRYS == null || clazz < 1 || clazz > 5) return null;

        int minIdx = 0, maxIdx = 0, advMin = -1, advMax = -1;
        if (slot == 1) {
            minIdx = (clazz - 1) * 60;
            maxIdx = minIdx + 19;
            advMin = 703 + (clazz - 1) * 15;
            advMax = advMin + 4;
        } else if (slot == 2) {
            minIdx = (clazz - 1) * 60 + 20;
            maxIdx = minIdx + 19;
            advMin = 703 + (clazz - 1) * 15 + 5;
            advMax = advMin + 4;
        } else if (slot == 3) {
            minIdx = (clazz - 1) * 60 + 40;
            maxIdx = minIdx + 19;
            advMin = 703 + (clazz - 1) * 15 + 10;
            advMax = advMin + 4;
        } else if (slot == 4) {
            minIdx = 375 + (clazz - 1) * 20;
            maxIdx = minIdx + 19;
        } else if (slot == 5) {
            minIdx = switch (clazz) {
                case 1 -> 566;
                case 2 -> 584;
                case 3 -> 602;
                case 4 -> 620;
                case 5 -> 638;
                default -> 566;
            };
            maxIdx = minIdx + 17;
        } else if (slot == 6) {
            minIdx = switch (clazz) {
                case 1 -> 487;
                case 2 -> 492;
                case 3 -> 497;
                case 4 -> 502;
                case 5 -> 507;
                default -> 487;
            };
            maxIdx = minIdx + 4;
        } else {
            return null;
        }

        // 1. Ưu tiên tìm trong dải kỹ năng nâng cao nếu lvRq >= 27
        if (advMin != -1 && lvRq >= 27) {
            for (Skill_Template st : Skill_Template.ENTRYS) {
                if (st != null && st.indexSkillInServer >= advMin && st.indexSkillInServer <= advMax && st.Lv_RQ == lvRq) {
                    return st;
                }
            }
        }

        // 2. Tìm chính xác theo Lv_RQ trong dải chính
        for (Skill_Template st : Skill_Template.ENTRYS) {
            if (st != null && st.indexSkillInServer >= minIdx && st.indexSkillInServer <= maxIdx && st.Lv_RQ == lvRq) {
                return st;
            }
        }

        // 3. Fallback: tìm template gần nhất
        Skill_Template bestFallback = null;
        int minDiff = Integer.MAX_VALUE;
        for (Skill_Template st : Skill_Template.ENTRYS) {
            if (st != null) {
                boolean inMain = (st.indexSkillInServer >= minIdx && st.indexSkillInServer <= maxIdx);
                boolean inAdv = (advMin != -1 && st.indexSkillInServer >= advMin && st.indexSkillInServer <= advMax);
                if (inMain || inAdv) {
                    if (lvRq == -1 && st.Lv_RQ == -1) {
                        return st;
                    }
                    int target = (lvRq == -1) ? 1 : lvRq;
                    int diff = Math.abs(st.Lv_RQ - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        bestFallback = st;
                    }
                }
            }
        }

        return bestFallback;
    }

    public static void convertSkills(Player p, byte oldClazz, byte newClazz) {
        if (p == null || p.skill_point == null) return;

        // Lưu thông tin từng slot kỹ năng môn phái cũ (1..6)
        class SkillSlotData {
            byte lvRq = 1;
            long exp = 0;
            byte lvdevil = 0;
            byte devilpercent = 0;
            boolean present = false;
        }
        SkillSlotData[] slotData = new SkillSlotData[7];
        for (int s = 1; s <= 6; s++) slotData[s] = new SkillSlotData();

        List<Skill_info> keptPassives = new ArrayList<>();
        List<Skill_info> keptHaki = new ArrayList<>();
        List<Skill_info> keptDevils = new ArrayList<>();
        List<Skill_info> keptOthers = new ArrayList<>();

        for (Skill_info sk : p.skill_point) {
            if (sk == null || sk.temp == null) continue;
            int slot = getClassSkillSlot(sk);
            if (slot >= 1 && slot <= 6) {
                SkillSlotData sd = slotData[slot];
                if (!sd.present || sk.temp.Lv_RQ > sd.lvRq) {
                    sd.lvRq = sk.temp.Lv_RQ;
                    sd.exp = sk.exp;
                    sd.lvdevil = sk.lvdevil;
                    sd.devilpercent = sk.devilpercent;
                    sd.present = true;
                }
            } else {
                // Phân loại kỹ năng ngoài môn phái để giữ nguyên 100%
                if (sk.temp.typeDevil > 0 || sk.temp.ID >= 2000) {
                    keptDevils.add(sk);
                } else if (sk.temp.ID == 1016 || sk.temp.ID == 1017 || sk.temp.indexSkillInServer == 779 || sk.temp.indexSkillInServer == 785 || (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690)) {
                    keptHaki.add(sk);
                } else {
                    int idx = sk.temp.indexSkillInServer;
                    if ((idx >= 300 && idx <= 329) || (idx >= 552 && idx <= 561) || idx == 667) {
                        keptPassives.add(sk);
                    } else {
                        keptOthers.add(sk);
                    }
                }
            }
        }

        // Tạo danh sách kỹ năng mới hoàn toàn cho class mới theo cấu trúc chuẩn
        List<Skill_info> newSkills = new ArrayList<>();

        // 1. Chiêu 1 (Active 1) - Bắt buộc có
        {
            SkillSlotData sd = slotData[1];
            byte targetLv = sd.present ? sd.lvRq : (byte) 1;
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 1, targetLv);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.present ? sd.exp : 0;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        }

        // 2. Chiêu 2 (Active 2) - Bắt buộc có
        {
            SkillSlotData sd = slotData[2];
            byte targetLv = sd.present ? sd.lvRq : (byte) -1;
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 2, targetLv);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.present ? sd.exp : -1;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        }

        // 3. Chiêu 3 (Active 3) - Bắt buộc có
        {
            SkillSlotData sd = slotData[3];
            byte targetLv = sd.present ? sd.lvRq : (byte) -1;
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 3, targetLv);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.present ? sd.exp : -1;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        }

        // 4. Bị động dùng chung (300..325, 552..557, 667)
        newSkills.addAll(keptPassives);

        // 5. Thủy Chiến (Chiêu 4) - Bắt buộc có
        {
            SkillSlotData sd = slotData[4];
            byte targetLv = sd.present ? sd.lvRq : (byte) -1;
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 4, targetLv);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.present ? sd.exp : -1;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        }

        // 6. Chiêu Buff Riêng Phái (Slot 6)
        if (slotData[6].present) {
            SkillSlotData sd = slotData[6];
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 6, sd.lvRq);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.exp;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        } else {
            // Nếu người chơi chưa có chiêu buff môn phái, khởi tạo sẵn cho class mới ở trạng thái chưa học (Lv_RQ = -1)
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 6, (byte) -1);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = -1;
                newSkills.add(sk);
            }
        }

        // 7. Chiêu Nộ (Chỉ add nếu người chơi đã sở hữu)
        if (slotData[5].present) {
            SkillSlotData sd = slotData[5];
            Skill_Template st = getMatchingClassSkillTemplate(newClazz, 5, sd.lvRq);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = sd.exp;
                sk.lvdevil = sd.lvdevil;
                sk.devilpercent = sd.devilpercent;
                newSkills.add(sk);
            }
        }

        // 8. Haki
        newSkills.addAll(keptHaki);

        // 9. Dial, Phe Phái, Kỹ năng khác
        newSkills.addAll(keptOthers);

        // 10. Trái Ác Quỷ (Devil Fruit)
        newSkills.addAll(keptDevils);

        p.skill_point = newSkills;
    }

    private static void ensureDefaultHaki(Player p) {
        if (p == null || p.skill_point == null) return;
        boolean has779 = false;
        boolean has785 = false;
        for (Skill_info sk : p.skill_point) {
            if (sk != null && sk.temp != null) {
                if (sk.temp.indexSkillInServer == 779 || sk.temp.ID == 1016) has779 = true;
                if (sk.temp.indexSkillInServer == 785 || sk.temp.ID == 1017) has785 = true;
            }
        }
        if (!has779) {
            Skill_info sk = new Skill_info();
            sk.exp = -1;
            sk.temp = Skill_Template.get_temp(779, sk.exp);
            if (sk.temp != null) p.skill_point.add(sk);
        }
        if (!has785) {
            Skill_info sk = new Skill_info();
            sk.exp = -1;
            sk.temp = Skill_Template.get_temp(785, sk.exp);
            if (sk.temp != null) p.skill_point.add(sk);
        }
    }

    /**
     * Đồng bộ toàn bộ dữ liệu chuẩn hóa của nhân vật sau khi đổi class sang client và toàn bộ người chơi trong map.
     */
    public static void syncClassChange(Player p) {
        try {
            if (p == null) return;

            byte currentTypePk = p.type_pk;
            int currentTypePirate = p.typePirate;
            boolean currentBackTypePk = p.isBackTypePk;

            // 1. Tính toán lại toàn bộ chỉ số Ability & Stats
            p.setin4();
            p.setAbility();
            int maxHp = p.ability.get_hp_max(true);
            int maxMp = p.ability.get_mp_max(true);
            if (p.hp > maxHp) p.hp = maxHp;
            if (p.mp > maxMp) p.mp = maxMp;

            // Phục hồi và bảo toàn nguyên vẹn trạng thái PK & Phe phái trước khi đổi class
            p.type_pk = currentTypePk;
            p.typePirate = currentTypePirate;
            p.isBackTypePk = currentBackTypePk;

            // 2. Gửi thông tin nhân vật chính (Msg -10) với isSwitch = true
            if (p.getService() != null) {
                p.getService().Main_char_Info(true);
                p.getService().Weapon_fashion(p, true);
            }

            // 3. Gửi lại RMS / Phím tắt Hotkey (Msg -33)
            if (p.rms != null && p.conn != null) {
                for (int i = 0; i < p.rms.length; i++) {
                    Message mRms = new Message(-33);
                    mRms.writer().writeByte(i);
                    if (p.rms[i] != null && p.rms[i].length > 0) {
                        mRms.writer().writeShort(p.rms[i].length);
                        mRms.writer().write(p.rms[i]);
                    } else {
                        mRms.writer().writeShort(0);
                    }
                    p.conn.addmsg(mRms);
                    mRms.cleanup();
                }
            }

            // 4. Gửi danh sách kỹ năng mới (Msg -7 byte 3)
            p.send_skill();

            // 5. Gửi hành trang và trang bị đang mặc (Msg 17, 18, 19)
            if (p.item != null) {
                p.item.updateInventory(false);
            }
            if (p.getService() != null) {
                p.getService().charWearing(p, false);
                p.getService().Weapon_fashion(p, false);
                p.updateMoney();
            }

            // 6. Broadcast cập nhật ngoại hình & class tới TOÀN BỘ người chơi trong map
            p.update_info_to_all();
            if (p.map != null && p.map.players != null) {
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player p0 = p.map.players.get(i);
                    if (p0 != null && p0.index_map != p.index_map && p0.getService() != null) {
                        p.map.send_char_in4_inmap(p0, p.index_map);
                        p0.getService().charWearing(p, false);
                        p0.getService().Weapon_fashion(p, false);
                    }
                }
            }

            // 7. Tái kiểm tra và đồng bộ chuẩn xác toàn bộ trạng thái PK theo Map & Cờ
            checkAndSyncMapPk(p);

            // 8. Lưu ngay lập tức vào Database (đã có clazz)
            p.flush(p, false);

        } catch (Exception e) {
            Log.error("AdminSystemMenu", "syncClassChange error: " + e.getMessage());
        }
    }

    /**
     * Tái kiểm tra và đồng bộ chuẩn xác trạng thái PK, cờ môn phái, đấu trường, phó bản khi chuyển class
     */
    public static void checkAndSyncMapPk(Player p) {
        if (p == null || p.map == null) return;
        try {
            // 1. Kiểm tra Đấu Trường Tự Do (Map 70, 71, 72, 74)
            if (map.zones.DauTruongTuDo.IsCantChangeFlag(p)) {
                template.DauTruongTuDoInfo info = map.zones.DauTruongTuDo.GetInfo(p);
                if (info != null) {
                    byte assignedFlag = (info.typePk == 4 || info.typePk == 5) ? info.typePk : map.zones.DauTruongTuDo.getBalancedFlag(p.map, p);
                    info.typePk = assignedFlag;
                    p.type_pk = info.typePk;
                    p.isBackTypePk = true;
                    p.map.change_flag(p, info.typePk);
                    if (p.getService() != null) {
                        p.getService().update_PK(p, false);
                    }
                    // Gửi UI Đấu Trường Tự Do
                    Message m = new Message(-7);
                    m.writer().writeByte(22);
                    m.writer().writeByte(1);
                    p.addmsg(m);
                    m.cleanup();

                    m = new Message(39);
                    m.writer().writeShort(p.index_map);
                    m.writer().writeInt(info.point);
                    m.writer().writeByte(info.color == 0 ? 11 : (info.color == 1 ? 4 : (info.color == 2 ? 3 : 1)));
                    p.addmsg(m);
                    m.cleanup();

                    for (int i = 0; i < p.map.players.size(); i++) {
                        Player p0 = p.map.players.get(i);
                        if (p0 != null && p0.index_map != p.index_map) {
                            map.zones.DauTruongTuDo.SendInfoOther(p0, p);
                            map.zones.DauTruongTuDo.SendInfoOther(p, p0);
                        }
                    }
                    return;
                }
            }

            // 2. Tự động kiểm tra và áp dụng quy tắc PK theo từng loại Map thông qua change_flag
            p.map.change_flag(p, p.type_pk);

            // 3. Cập nhật gói tin PK (Msg 14) cho chính bản thân người chơi
            if (p.getService() != null) {
                p.getService().update_PK(p, false);
            }

            // 4. Đồng bộ trạng thái PK cho Đệ tử và Bot Lính đánh thuê
            if (p.detu != null) {
                p.detu.type_pk = p.type_pk;
                p.detu.typePirate = p.typePirate;
                p.detu.clan = p.clan;
                if (p.getService() != null) {
                    p.getService().update_PK(p.detu, false);
                }
            }
            java.util.List<bot.mercenary.MercenaryBot> activeBots = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
            if (activeBots != null) {
                for (bot.mercenary.MercenaryBot merc : activeBots) {
                    if (merc != null && merc.map != null && merc.map.equals(p.map)) {
                        merc.type_pk = p.type_pk;
                        merc.typePirate = p.typePirate;
                        merc.clan = p.clan;
                        if (p.getService() != null) {
                            p.getService().update_PK(merc, false);
                        }
                    }
                }
            }

            // 5. Gửi cập nhật PK của nhân vật và đệ tử tới tất cả người chơi khác trong map
            for (int i = 0; i < p.map.players.size(); i++) {
                Player p0 = p.map.players.get(i);
                if (p0 != null && p0.index_map != p.index_map && p0.getService() != null) {
                    p0.getService().update_PK(p, false);
                    if (p.detu != null) {
                        p0.getService().update_PK(p.detu, false);
                    }
                }
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "checkAndSyncMapPk error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  4. THÊM / TRỪ LEVEL
    // ==============================================================

    public void openAdjustLevelDialog(Player p) throws IOException {
        p.sendInput("Nhập số Level muốn thay đổi: Ví dụ: 10 để tăng 10 lv, -5 để trừ 5 lv, hoặc 150 để đặt max:", new String[]{"Số Level Tăng Giảm"}, (inputs) -> {
            try {
                if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                    int val = Integer.parseInt(inputs[0].trim());
                    int oldLevel = p.level;
                    if (val > 0 && val <= 150 && inputs[0].trim().length() <= 3 && !inputs[0].trim().startsWith("+")) {
                        p.level = (short) Math.min(150, val);
                    } else {
                        p.level = (short) Math.max(1, Math.min(150, p.level + val));
                    }
                    if (p.level > oldLevel) {
                        for (int lv = oldLevel; lv < p.level; lv++) {
                            p.pointAttribute += (lv < 100) ? Level.ENTRYS[lv - 1].tiemnang : Level.ENTRYS[lv - 2].tiemnang;
                        }
                    }
                    p.hp = p.ability.get_hp_max(true);
                    p.mp = p.ability.get_mp_max(true);
                    p.update_info_to_all();
                    p.getService().send_box_ThongBao_OK("Cấp độ hiện tại của bạn: Level " + p.level);
                }
            } catch (Exception e) {
                try { p.getService().send_box_ThongBao_OK("Số level không hợp lệ!"); } catch (Exception ignored) {}
            }
        });
    }

    // ==============================================================
    //  5. QUẢN LÝ TÀI NGUYÊN & ITEM
    // ==============================================================

    public void openAddItemCategory(Player p) throws IOException {
        p.getService().openDynamicMenu(MENU_ADD_ITEM_CATEGORY,
            "Quản Lý Tài Nguyên & Item",
            new String[]{
                "Thêm Item Tự Chọn",
                "Thêm Vàng Beri",
                "Thêm Ruby",
                "Thêm Extol Trái Bá Vương",
                "Nhận Full Trái Ác Quỷ Thượng Cấp vào Hành Trang",
                "Quay lại"
            },
            new short[]{133, 110, 110, 133, 155, 134}
        );
    }

    private void handleAddItemCategory(Player p, int index) throws IOException {
        switch (index) {
            case 0: openAddItemDialog(p); break;
            case 1: {
                p.sendInput("Nhập số lượng Vàng: Berimuốn cộng:", new String[]{"Số Vàng"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.update_vang(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Beri!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 2: {
                p.sendInput("Nhập số lượng Ruby muốn cộng:", new String[]{"Số Ruby"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.update_ruby(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Ruby!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 3: {
                p.sendInput("Nhập số lượng Extol: Trái Bá Vươngmuốn cộng:", new String[]{"Số Extol"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.updateVnd(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Extol!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 4: {
                int[] dfIds = new int[]{207, 208, 209, 210, 211, 212, 213, 214};
                for (int dfId : dfIds) {
                    p.item.add_item_bag47(4, (short) dfId, 10);
                }
                p.item.updateInventory(false);
                p.getService().send_box_ThongBao_OK("Đã thêm Bộ Trái Ác Quỷ Thượng Cấp vào Hành Trang!");
                break;
            }
            case 5: openMain(p); break;
        }
    }

    public void openAddItemDialog(Player p) throws IOException {
        p.sendInput("Nhập: Loại_Item ID_Item Số_Lượng: Ví dụ: 4 207 100 cho Item Bag47, hoặc 3 100 1 cho Item Trang bị:", new String[]{"Category ID Quantity"}, (inputs) -> {
            try {
                if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                    String[] parts = inputs[0].trim().split("\\s+");
                    if (parts.length >= 3) {
                        byte cat = Byte.parseByte(parts[0]);
                        short itemId = Short.parseShort(parts[1]);
                        int quant = Integer.parseInt(parts[2]);
                        if (cat == 4 || cat == 7) {
                            p.item.add_item_bag47(cat, itemId, quant);
                        } else if (cat == 3) {
                            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(itemId);
                            if (t3 != null) {
                                Item_wear newWear = new Item_wear();
                                newWear.setup_template_by_id(t3);
                                p.item.add_item_bag3(newWear);
                            }
                        }
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Đã thêm " + quant + " vật phẩm: Category " + cat + ", ID " + itemId + "vào hành trang!");
                    } else {
                        p.getService().send_box_ThongBao_OK("Nhập đủ 3 tham số: Category ID Quantity!");
                    }
                }
            } catch (Exception e) {
                try { p.getService().send_box_ThongBao_OK("Thông số item không hợp lệ!"); } catch (Exception ignored) {}
            }
        });
    }

    // ==============================================================
    //  6. FAST TELEPORT & NGƯỜI CHƠI
    // ==============================================================

    public void openTeleport(Player p) throws IOException {
        p.getService().openDynamicMenu(MENU_TELEPORT,
            "Dịch Chuyển Tức Thời",
            new String[]{
                "Kéo Toàn Bộ Người Chơi Online Về Map Hiện Tại",
                "Làng Cối Xay Gió",
                "Thị Trấn Vỏ Sò",
                "Làng Khởi Đầu",
                "Mỏm Sinh Đôi",
                "Đấu Trường Tự Do",
                "Map Boss Thế Giới",
                "Dịch chuyển Đến vị trí Người Chơi",
                "Kéo Người Chơi đến vị trí Admin",
                "Quay lại"
            },
            new short[]{116, 116, 116, 116, 116, 136, 136, 110, 110, 134}
        );
    }

    private void handleTeleport(Player p, int index) throws IOException {
        switch (index) {
            case 0: TimedDungeonManager.pullAllOnlinePlayersToAdmin(p); break;
            case 1: TimedDungeonManager.teleportPlayerToMap(p, 1); break;
            case 2: TimedDungeonManager.teleportPlayerToMap(p, 9); break;
            case 3: TimedDungeonManager.teleportPlayerToMap(p, 49); break;
            case 4: TimedDungeonManager.teleportPlayerToMap(p, 66); break;
            case 5: TimedDungeonManager.teleportPlayerToMap(p, 69); break;
            case 6: TimedDungeonManager.teleportPlayerToMap(p, 107); break;
            case 7: {
                p.sendInput("Nhập tên người chơi muốn đến:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        TimedDungeonManager.teleportPlayerToPlayer(p, inputs[0].trim());
                    }
                });
                break;
            }
            case 8: {
                p.sendInput("Nhập tên người chơi muốn kéo đến Admin:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        TimedDungeonManager.pullPlayerToAdmin(p, inputs[0].trim());
                    }
                });
                break;
            }
            case 9: openMain(p); break;
        }
    }

    // ==============================================================
    //  7. QUẢN LÝ BOT SYSTEM
    // ==============================================================

    public void openBotManagement(Player p) throws IOException {
        TimedDungeonManager.gI().openBotManagementMenu(p);
    }

    private void handleBotManagement(Player p, int index) throws IOException {
        if (index == 9) {
            openMain(p);
            return;
        }
        TimedDungeonManager.gI().handleBotManagementMenu(p, index);
    }

    private void handleBotSelectDetail(Player p, int botIndex) throws IOException {
        TimedDungeonManager.gI().handleBotSelectDetail(p, botIndex);
    }

    // ==============================================================
    //  8. QUẢN LÝ ĐỆ TỬ & LÍNH ĐÁNH THUÊ
    // ==============================================================

    public static void openDiscipleAndMercenaryMenu(Player p) throws IOException {
        if (p == null) return;
        boolean hasDeTu = (p.detu != null || (p.nameDe != null && !p.nameDe.isBlank()));
        boolean hasMerc = bot.mercenary.MercenaryManager.gI().hasAnyContracts(p);
        int mercCount = bot.mercenary.MercenaryManager.gI().getPlayerContracts(p).size();
        String deTuStatus = hasDeTu ? (" [Có Đệ: " + (p.detu != null ? p.detu.name : p.nameDe) + "]") : " [Chưa có]";
        String mercStatus = hasMerc ? (" [Có Lính: " + mercCount + " hợp đồng]") : " [Chưa có]";

        iMenuDymanic.buildAndSend(p, MENU_DISCIPLE_AND_MERCENARY,
            "QUẢN LÝ ĐỆ TỬ & LÍNH ĐÁNH THUÊ",
            new String[]{
                "Quản Lý Đệ Tử" + deTuStatus,
                "Quản Lý Lính Đánh Thuê" + mercStatus,
                "Rút Toàn Bộ Lính Đánh Thuê Về (Ẩn/Nghỉ Ngơi)",
                "Xuất Chiến Toàn Bộ Lính Đánh Thuê",
                "Đổi Trạng Thái Lính (Đi theo / Bảo vệ / Tấn công / Về)",
                "Quay Lại"
            },
            new short[]{155, 133, 118, 110, 116, 134}
        );
    }

    private void handleDiscipleAndMercenaryMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                boolean hasDeTu = (p.detu != null || (p.nameDe != null && !p.nameDe.isBlank()));
                if (!hasDeTu) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử! Hãy bái sư nhận đệ tử trước.");
                    return;
                }
                TimedDungeonManager.gI().openDiscipleManagementMenu(p);
                break;
            }
            case 1: {
                zinterfaces.menus.dymanics.DymanicMercenary.sendMercenaryMenu(p);
                break;
            }
            case 2: {
                bot.mercenary.MercenaryManager.gI().recallAllBots(p);
                break;
            }
            case 3: {
                bot.mercenary.MercenaryManager.gI().summonAllActiveBots(p);
                break;
            }
            case 4: {
                zinterfaces.menus.dymanics.DymanicMercenary.showStatusChangeMenu(p);
                break;
            }
            case 5: {
                if (isAdmin(p)) {
                    openMain(p, MENU_ITEM_872_ROOT);
                } else {
                    openItem872Menu(p);
                }
                break;
            }
        }
    }

    public void openDiscipleManagement(Player p) throws IOException {
        openDiscipleAndMercenaryMenu(p);
    }

    private void handleDiscipleManagement(Player p, int choice) throws IOException {
        if (choice == 8 || choice == 9) {
            openDiscipleAndMercenaryMenu(p);
            return;
        }
        TimedDungeonManager.gI().handleDiscipleManagementMenu(p, choice);
    }

    // ==============================================================
    //  9. QUẢN LÝ PHÓ BẢN THỜI GIAN
    // ==============================================================

    public void openTimedDungeonList(Player p) throws IOException {
        TimedDungeonManager.gI().openTimedDungeonManagementMenu(p);
    }

    private void handleTimedDungeonList(Player p, int index) throws IOException {
        TimedDungeonManager.gI().handleTimedDungeonManagementMenu(p, index);
    }

    private void handleTimedDungeonDetail(Player p, int action) throws IOException {
        if (action == 4) {
            openTimedDungeonList(p);
            return;
        }
        TimedDungeonManager.gI().handleTimedDungeonDetailAction(p, action);
    }

    // ==============================================================
    //  10. QUẢN LÝ BOSS, ĐỆ HOANG & SIÊU TRÙM (THẺ HỆ THỐNG)
    // ==============================================================

    public void openBossEvent(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // 1. Kiểm tra vị trí Siêu Trùm
        menus.add(new model.Menu("Vị Trí Siêu Trùm", (short) 136, () -> {
            try {
                openTrackedEntitiesList(p, "SIÊU TRÙM", "Vị Trí Siêu Trùm");
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openTrackedEntitiesList error: " + e.getMessage());
            }
        }));

        // 2. Kiểm tra vị trí Đệ Tử Hoang
        int wildCount = DeTu.WILD_DETU_MAP != null ? DeTu.WILD_DETU_MAP.size() : 0;
        menus.add(new model.Menu("Vị Trí Đệ Hoang: " + wildCount + " Đệ", (short) 155, () -> {
            try {
                openTrackedEntitiesList(p, "ĐỆ HOANG", "Vị Trí Đệ Hoang");
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openTrackedEntitiesList error: " + e.getMessage());
            }
        }));

        // 3. Kiểm tra vị trí Boss Thường & Thế Giới
        menus.add(new model.Menu("Vị Trí Boss Thường & TG", (short) 136, () -> {
            try {
                openTrackedEntitiesList(p, "BOSS_OTHER", "Vị Trí Boss Thường & TG");
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openTrackedEntitiesList error: " + e.getMessage());
            }
        }));

        // 4. Báo cáo nhanh toàn bộ vị trí
        menus.add(new model.Menu("Báo Cáo Nhanh Toàn Bộ Vị Trí", (short) 133, () -> {
            try {
                showQuickEntityReport(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "showQuickEntityReport error: " + e.getMessage());
            }
        }));

        // 5. Spawn Đệ Hoang
        menus.add(new model.Menu("Spawn Đệ Hoang: Random K.Trùng Map / Tùy Chọn", (short) 110, () -> {
            try {
                openSpawnWildDeTuMenu(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openSpawnWildDeTuMenu error: " + e.getMessage());
            }
        }));

        // 6. Spawn Siêu Trùm
        menus.add(new model.Menu("Spawn Siêu Trùm: Random K.Trùng Map / Chọn Boss", (short) 110, () -> {
            try {
                openSpawnSuperBossMenu(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openSpawnSuperBossMenu error: " + e.getMessage());
            }
        }));

        // 7. Spawn Boss Thường / Thế Giới
        menus.add(new model.Menu("Spawn Boss Thường / Boss Thế Giới", (short) 110, () -> {
            try {
                openSpawnNormalBossMenu(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openSpawnNormalBossMenu error: " + e.getMessage());
            }
        }));

        // 8. Tiêu Diệt Toàn Bộ Boss
        menus.add(new model.Menu("Tiêu Diệt / Despawn Toàn Bộ Boss Đang Sống", (short) 118, () -> {
            p.sendYesNo("Xác nhận", "Bạn có chắc muốn tiêu diệt TOÀN BỘ Boss đang sống trên server?", () -> {
                int killed = BossAndWildManager.gI().killAllBosses();
                p.getService().send_box_ThongBao_OK("Đã tiêu diệt " + killed + " Boss đang sống trên server!");
                try { openBossEvent(p); } catch (Exception ignored) {}
            });
        }));

        // 9. Dọn Dẹp Toàn Bộ Đệ Hoang
        menus.add(new model.Menu("Dọn Dẹp / Thu Hồi Toàn Bộ Đệ Hoang", (short) 118, () -> {
            p.sendYesNo("Xác nhận", "Bạn có chắc muốn dọn dẹp TOÀN BỘ Đệ Hoang trên server?", () -> {
                int cleared = BossAndWildManager.gI().clearAllWildDeTu();
                p.getService().send_box_ThongBao_OK("Đã dọn dẹp " + cleared + " Đệ Tử Hoang trên server!");
                try { openBossEvent(p); } catch (Exception ignored) {}
            });
        }));

        // 10. Quản Lý Sự Kiện Khác
        menus.add(new model.Menu("Quản Lý Sự Kiện Khác: Đấu Trường, Noel...", (short) 146, () -> {
            try {
                openOtherEventsMenu(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openOtherEventsMenu error: " + e.getMessage());
            }
        }));

        // 11. Quay Lại
        menus.add(new model.Menu("Quay Lại Menu Admin", (short) 134, () -> {
            try {
                openMain(p);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_BOSS_EVENT, "QUẢN LÝ BOSS & ĐỆ HOANG & EVENT", menus);
    }

    private void handleBossEvent(Player p, int index) throws IOException {
        switch (index) {
            case 0: openTrackedEntitiesList(p, "SIÊU TRÙM", "Vị Trí Siêu Trùm"); break;
            case 1: openTrackedEntitiesList(p, "ĐỆ HOANG", "Vị Trí Đệ Hoang"); break;
            case 2: openTrackedEntitiesList(p, "BOSS_OTHER", "Vị Trí Boss Thường & TG"); break;
            case 3: showQuickEntityReport(p); break;
            case 4: openSpawnWildDeTuMenu(p); break;
            case 5: openSpawnSuperBossMenu(p); break;
            case 6: openSpawnNormalBossMenu(p); break;
            case 7: {
                p.sendYesNo("Xác nhận", "Bạn có chắc muốn tiêu diệt TOÀN BỘ Boss đang sống trên server?", () -> {
                    int killed = BossAndWildManager.gI().killAllBosses();
                    p.getService().send_box_ThongBao_OK("Đã tiêu diệt " + killed + " Boss đang sống trên server!");
                    try { openBossEvent(p); } catch (Exception ignored) {}
                });
                break;
            }
            case 8: {
                p.sendYesNo("Xác nhận", "Bạn có chắc muốn dọn dẹp TOÀN BỘ Đệ Hoang trên server?", () -> {
                    int cleared = BossAndWildManager.gI().clearAllWildDeTu();
                    p.getService().send_box_ThongBao_OK("Đã dọn dẹp " + cleared + " Đệ Tử Hoang trên server!");
                    try { openBossEvent(p); } catch (Exception ignored) {}
                });
                break;
            }
            case 9: openOtherEventsMenu(p); break;
            case 10: openMain(p); break;
        }
    }

    public void openTrackedEntitiesList(Player p, String category, String title) throws IOException {
        List<TrackedEntityDTO> list;
        if ("SIÊU TRÙM".equalsIgnoreCase(category)) {
            list = BossAndWildManager.gI().getSuperBossEntities();
        } else if ("ĐỆ HOANG".equalsIgnoreCase(category)) {
            list = BossAndWildManager.gI().getWildDeTuEntities();
        } else if ("BOSS_OTHER".equalsIgnoreCase(category)) {
            list = BossAndWildManager.gI().getNormalAndWorldBossEntities();
        } else {
            list = BossAndWildManager.gI().getAllTrackedEntities();
        }

        List<model.Menu> menus = new ArrayList<>();

        if (list.isEmpty()) {
            menus.add(new model.Menu("Không có thực thể nào phù hợp hiện tại", (short) 118, () -> {
                try { openBossEvent(p); } catch (Exception ignored) {}
            }));
        } else {
            for (TrackedEntityDTO e : list) {
                if (e == null) continue;
                String itemTitle;
                short icon;
                if (e.isAlive) {
                    itemTitle = "[" + e.name + "] Map " + e.mapId + " (" + e.mapName + ") - K" + (e.zoneId + 1) + " [" + e.x + "," + e.y + "]";
                    icon = "ĐỆ HOANG".equalsIgnoreCase(e.category) ? (short) 155 : (short) 136;
                } else {
                    itemTitle = "[" + e.name + "] (" + e.statusText + ")";
                    icon = (short) 118;
                }

                menus.add(new model.Menu(itemTitle, icon, () -> {
                    try {
                        openEntityDetailActionMenu(p, e, category, title);
                    } catch (Exception ex) {
                        Log.error("AdminSystemMenu", "openEntityDetailActionMenu error: " + ex.getMessage());
                    }
                }));
            }
        }

        // Action: Làm mới
        menus.add(new model.Menu("Làm Mới Danh Sách", (short) 116, () -> {
            try {
                openTrackedEntitiesList(p, category, title);
            } catch (Exception ignored) {}
        }));

        // Action: Quay lại
        menus.add(new model.Menu("Quay Lại Menu Boss", (short) 134, () -> {
            try {
                openBossEvent(p);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_BOSS_LOCATION_LIST, title + " (" + list.size() + ")", menus);
    }

    public void openEntityDetailActionMenu(Player p, TrackedEntityDTO e, String returnCategory, String returnTitle) throws IOException {
        if (e == null) {
            openTrackedEntitiesList(p, returnCategory, returnTitle);
            return;
        }

        List<model.Menu> menus = new ArrayList<>();

        // 1. Bay đến vị trí
        String flyTitle = e.isAlive
                ? ("Bay Đến: Map " + e.mapId + " (" + e.mapName + ") - K" + (e.zoneId + 1))
                : "Bay Đến Vị Trí: Thực thể hiện chưa sống";
        menus.add(new model.Menu(flyTitle, (short) 116, () -> {
            try {
                if (!e.isAlive || e.rawEntity == null) {
                    p.getService().send_box_ThongBao_OK("Thực thể hiện không sống hoặc chưa xuất hiện trên bản đồ!");
                    return;
                }
                boolean ok = BossAndWildManager.gI().teleportAdminToEntity(p, e.rawEntity);
                if (!ok) {
                    p.getService().send_box_ThongBao_OK("Không thể bay đến thực thể này: Có thể đã chết hoặc rời map!");
                }
            } catch (Exception ex) {
                Log.error("AdminSystemMenu", "teleport error: " + ex.getMessage());
            }
        }));

        // 2. Tiêu diệt / Thu hồi thực thể
        menus.add(new model.Menu("Tiêu Diệt / Thu Hồi Thực Thể Này", (short) 118, () -> {
            p.sendYesNo("Xác nhận", "Tiêu diệt / thu hồi: " + e.name + "?", () -> {
                boolean killed = BossAndWildManager.gI().killEntity(e);
                p.getService().send_box_ThongBao_OK(killed ? ("Đã tiêu diệt/thu hồi: " + e.name) : "Không tìm thấy thực thể để tiêu diệt!");
                try { openTrackedEntitiesList(p, returnCategory, returnTitle); } catch (Exception ignored) {}
            });
        }));

        // 3. Xem thông số chi tiết
        menus.add(new model.Menu("Xem Thông Số Toàn Diện", (short) 133, () -> {
            StringBuilder info = new StringBuilder();
            info.append("=== THÔNG SỐ: ").append(e.name).append(" ===\n");
            info.append("ID: ").append(e.id).append("| Loại: ").append(e.category).append("\n");
            info.append("Trạng thái: ").append(e.statusText).append("\n");
            if (e.isAlive) {
                info.append("Vị trí: ").append(e.mapName).append("(Map ").append(e.mapId).append(")\n");
                info.append("Khu: ").append(e.zoneId + 1).append("| Tọa độ: [").append(e.x).append(", ").append(e.y).append("]\n");
                info.append("HP: ").append(e.getHpDisplay()).append("\n");
                info.append("Cấp độ: ").append(e.level).append("\n");
            }
            if (e.extraInfo != null && !e.extraInfo.isBlank()) {
                info.append("Thông tin: ").append(e.extraInfo).append("\n");
            }
            p.getService().send_box_ThongBao_OK(info.toString());
        }));

        // 4. Quay lại danh sách
        menus.add(new model.Menu("Quay Lại Danh Sách", (short) 134, () -> {
            try {
                openTrackedEntitiesList(p, returnCategory, returnTitle);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_ENTITY_ACTION_DETAIL, e.name + " (" + e.category + ")", menus);
    }

    public void showQuickEntityReport(Player p) {
        String report = BossAndWildManager.gI().getLiveStatusSummary("ALL");
        p.getService().send_box_ThongBao_OK(report);
    }

    public void openSpawnWildDeTuMenu(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // Option 1: Random 5 con KHÔNG TRÙNG Map/Khu
        menus.add(new model.Menu("Spawn 5 Đệ Hoang: KHÔNG TRÙNG Map/Khu", (short) 155, () -> {
            List<DeTu> list = BossAndWildManager.gI().spawnWildDeTu(null, null, null, 5, true, true, true);
            p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Đệ Hoang phân bổ đều không trùng map/khu!");
        }));

        // Option 2: Random Số Lượng Tự Chọn (KHÔNG TRÙNG Map/Khu)
        menus.add(new model.Menu("Spawn Random Số Lượng: K.Trùng Map/Khu", (short) 110, () -> {
            p.sendInput("Nhập số lượng Đệ Hoang cần spawn: 1 - 20:", new String[]{"Số lượng"}, (inputs) -> {
                try {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        int count = Math.max(1, Math.min(30, Integer.parseInt(inputs[0].trim())));
                        List<DeTu> list = BossAndWildManager.gI().spawnWildDeTu(null, null, null, count, true, true, true);
                        p.getService().send_box_ThongBao_OK("Đã spawn " + list.size() + " Đệ Hoang không trùng map/khu!");
                    }
                } catch (Exception ex) {
                    p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
                }
            });
        }));

        // Option 3: Tùy Chọn Đầy Đủ (Map, Zone, Clazz, Count)
        menus.add(new model.Menu("Spawn Tùy Chọn: Nhập Map, Khu, Phái, Số Lượng", (short) 133, () -> {
            p.sendInput("Nhập: MapID Khu Phái: 1-5SốLượng\n: Bỏ trống/0 để Random hoàn toàn, vd: 1 1 1 1 hoặc 0 0 0 3:", new String[]{"Map Khu Phái Count"}, (inputs) -> {
                try {
                    Integer mapId = null;
                    Integer zoneId = null;
                    Integer clazz = null;
                    int count = 1;

                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String[] parts = inputs[0].trim().split("\\s+");
                        if (parts.length >= 1 && !parts[0].equals("0") && !parts[0].equalsIgnoreCase("null")) {
                            mapId = Integer.parseInt(parts[0]);
                        }
                        if (parts.length >= 2 && !parts[1].equals("0") && !parts[1].equalsIgnoreCase("null")) {
                            if (parts[1].equalsIgnoreCase("all") || parts[1].equals("-1")) {
                                zoneId = -1;
                            } else {
                                int rawZone = Integer.parseInt(parts[1]);
                                zoneId = rawZone > 0 ? (rawZone - 1) : 0;
                            }
                        }
                        if (parts.length >= 3 && !parts[2].equals("0") && !parts[2].equalsIgnoreCase("null")) {
                            clazz = Integer.parseInt(parts[2]);
                        }
                        if (parts.length >= 4) {
                            count = Math.max(1, Integer.parseInt(parts[3]));
                        }
                    }

                    boolean noDupMap = (mapId == null);
                    boolean noDupZone = (zoneId == null);
                    List<DeTu> list = BossAndWildManager.gI().spawnWildDeTu(mapId, zoneId, clazz, count, noDupMap, noDupZone, true);
                    p.getService().send_box_ThongBao_OK("Đã spawn thành công " + list.size() + " Đệ Tử Hoang!");
                } catch (Exception ex) {
                    p.getService().send_box_ThongBao_OK("Thông số không hợp lệ: " + ex.getMessage());
                }
            });
        }));

        // Nút quay lại
        menus.add(new model.Menu("Quay Lai Menu Boss", (short) 134, () -> {
            try { openBossEvent(p); } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_SPAWN_WILD_DETU, "SPAWN ĐỆ TỬ HOANG", menus);
    }

    public void openSpawnSuperBossMenu(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // Option 1: Random 3 Siêu Trùm KHÔNG TRÙNG Map/Khu
        menus.add(new model.Menu("Spawn 3 Siêu Trùm: KHÔNG TRÙNG Map/Khu", (short) 136, () -> {
            List<AbsBoss> list = BossAndWildManager.gI().spawnSuperBoss(null, null, null, 3, true, true, true);
            p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Siêu Trùm không trùng map/khu!");
        }));

        // Option 2: Random Số Lượng Tự Chọn (KHÔNG TRÙNG Map/Khu)
        menus.add(new model.Menu("Spawn Random Số Lượng: K.Trùng Map/Khu", (short) 110, () -> {
            p.sendInput("Nhập số lượng Siêu Trùm cần spawn: 1 - 10:", new String[]{"Số lượng"}, (inputs) -> {
                try {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        int count = Math.max(1, Math.min(10, Integer.parseInt(inputs[0].trim())));
                        List<AbsBoss> list = BossAndWildManager.gI().spawnSuperBoss(null, null, null, count, true, true, true);
                        p.getService().send_box_ThongBao_OK("Đã triệu hồi " + list.size() + " Siêu Trùm không trùng map/khu!");
                    }
                } catch (Exception ex) {
                    p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
                }
            });
        }));

        // Option 3: Chọn Siêu Trùm Cụ Thể (trong 10 Siêu Trùm)
        menus.add(new model.Menu("Chọn Siêu Trùm Cụ Thể Trong Danh Sách", (short) 136, () -> {
            try {
                openSelectSuperBossToSpawn(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openSelectSuperBossToSpawn error: " + e.getMessage());
            }
        }));

        // Option 4: Tùy Chọn Đầy Đủ (MobID, Map, Zone, Count)
        menus.add(new model.Menu("Spawn Tùy Chỉnh Đầy Đủ: MobID, Map, Khu, SL", (short) 133, () -> {
            p.sendInput("Nhập: MobID MapID Khu SốLượng\n: Bỏ trống/0 để Random, vd: 16 1 1 1 hoặc 0 0 0 3:", new String[]{"MobID Map Khu Count"}, (inputs) -> {
                try {
                    Integer mobId = null;
                    Integer mapId = null;
                    Integer zoneId = null;
                    int count = 1;

                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String[] parts = inputs[0].trim().split("\\s+");
                        if (parts.length >= 1 && !parts[0].equals("0") && !parts[0].equalsIgnoreCase("null")) {
                            mobId = Integer.parseInt(parts[0]);
                        }
                        if (parts.length >= 2 && !parts[1].equals("0") && !parts[1].equalsIgnoreCase("null")) {
                            mapId = Integer.parseInt(parts[1]);
                        }
                        if (parts.length >= 3 && !parts[2].equals("0") && !parts[2].equalsIgnoreCase("null")) {
                            int rawZone = Integer.parseInt(parts[2]);
                            zoneId = rawZone > 0 ? (rawZone - 1) : 0;
                        }
                        if (parts.length >= 4) {
                            count = Math.max(1, Integer.parseInt(parts[3]));
                        }
                    }

                    boolean noDupMap = (mapId == null);
                    boolean noDupZone = (zoneId == null);
                    List<AbsBoss> list = BossAndWildManager.gI().spawnSuperBoss(mobId, mapId, zoneId, count, noDupMap, noDupZone, true);
                    p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Siêu Trùm!");
                } catch (Exception ex) {
                    p.getService().send_box_ThongBao_OK("Thông số không hợp lệ: " + ex.getMessage());
                }
            });
        }));

        // Nút quay lại
        menus.add(new model.Menu("Quay Lai Menu Boss", (short) 134, () -> {
            try { openBossEvent(p); } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_SPAWN_SUPER_BOSS, "SPAWN SIÊU TRÙM", menus);
    }

    public void openSelectSuperBossToSpawn(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();
        SuperBossManager.init();
        short[] bossIds = SuperBossManager.ID_BOSS;

        for (int i = 0; i < bossIds.length; i++) {
            final short bMobId = bossIds[i];
            final String bName = SuperBossManager.getBossName(i);

            menus.add(new model.Menu("[Mob " + bMobId + "] " + bName, (short) 136, () -> {
                p.sendInput("Spawn " + bName + " (Mob " + bMobId + ":\nNhập: MapID Khu SốLượng\n: Bỏ trống/0 để Random Map/Khu, vd: 0 0 1 hoặc 1 1 1:", new String[]{"Map Khu Count"}, (inputs) -> {
                    try {
                        Integer mapId = null;
                        Integer zoneId = null;
                        int count = 1;

                        if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                            String[] parts = inputs[0].trim().split("\\s+");
                            if (parts.length >= 1 && !parts[0].equals("0") && !parts[0].equalsIgnoreCase("null")) {
                                mapId = Integer.parseInt(parts[0]);
                            }
                            if (parts.length >= 2 && !parts[1].equals("0") && !parts[1].equalsIgnoreCase("null")) {
                                int rawZone = Integer.parseInt(parts[1]);
                                zoneId = rawZone > 0 ? (rawZone - 1) : 0;
                            }
                            if (parts.length >= 3) {
                                count = Math.max(1, Integer.parseInt(parts[2]));
                            }
                        }

                        boolean noDupMap = (mapId == null);
                        boolean noDupZone = (zoneId == null);
                        List<AbsBoss> list = BossAndWildManager.gI().spawnSuperBoss((int) bMobId, mapId, zoneId, count, noDupMap, noDupZone, true);
                        p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Siêu Trùm " + bName + "!");
                    } catch (Exception ex) {
                        p.getService().send_box_ThongBao_OK("Thông số không hợp lệ: " + ex.getMessage());
                    }
                });
            }));
        }

        menus.add(new model.Menu("Quay Lai Menu Spawn", (short) 134, () -> {
            try { openSpawnSuperBossMenu(p); } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_SELECT_SUPER_BOSS_SPAWN, "CHỌN SIÊU TRÙM CẦN TRIỆU HỒI", menus);
    }

    public void openSpawnNormalBossMenu(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // Option 1: Boss Thế Giới
        menus.add(new model.Menu("Spawn Boss Thế Giới: ID 172", (short) 136, () -> {
            try {
                BossTheGioi.spawn_boss();
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Thế Giới thành công!");
            } catch (Exception e) {
                p.getService().send_box_ThongBao_OK("Lỗi triệu hồi Boss Thế Giới: " + e.getMessage());
            }
        }));

        // Option 2: Boss Pica
        menus.add(new model.Menu("Spawn Boss Pica: ID 173", (short) 136, () -> {
            try {
                BossPica.gI().spawn();
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Pica thành công!");
            } catch (Exception e) {
                p.getService().send_box_ThongBao_OK("Lỗi triệu hồi Boss Pica: " + e.getMessage());
            }
        }));

        // Option 3: Boss Tùy Chỉnh
        menus.add(new model.Menu("Spawn Boss Tùy Chỉnh: Nhập MobID, Map, Khu, SL", (short) 110, () -> {
            p.sendInput("Nhập: MobID MapID Khu SốLượng\n: Ví dụ: 16 1 1 1 hoặc 16 0 0 2:", new String[]{"MobID Map Khu Count"}, (inputs) -> {
                try {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        String[] parts = inputs[0].trim().split("\\s+");
                        int mobId = Integer.parseInt(parts[0]);
                        Integer mapId = (parts.length >= 2 && !parts[1].equals("0")) ? Integer.parseInt(parts[1]) : null;
                        Integer zoneId = null;
                        if (parts.length >= 3 && !parts[2].equals("0")) {
                            int rawZone = Integer.parseInt(parts[2]);
                            zoneId = rawZone > 0 ? (rawZone - 1) : 0;
                        }
                        int count = (parts.length >= 4) ? Math.max(1, Integer.parseInt(parts[3])) : 1;

                        boolean noDupMap = (mapId == null);
                        boolean noDupZone = (zoneId == null);
                        List<AbsBoss> list = BossAndWildManager.gI().spawnNormalBoss(mobId, mapId, zoneId, count, noDupMap, noDupZone, true);
                        p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Boss!");
                    }
                } catch (Exception ex) {
                    p.getService().send_box_ThongBao_OK("Thông số không hợp lệ: " + ex.getMessage());
                }
            });
        }));

        // Nút quay lại
        menus.add(new model.Menu("Quay Lại Menu Boss", (short) 134, () -> {
            try { openBossEvent(p); } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_SPAWN_NORMAL_BOSS, "SPAWN BOSS THƯỜNG & THẾ GIỚI", menus);
    }

    public void openOtherEventsMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(MENU_OTHER_EVENTS,
            "Quản Lý Sự Kiện Khác Của Server",
            new String[]{
                "Mở Ngay Đấu Trường Tự Do",
                "Đóng & Phát Quà Đấu Trường Tự Do",
                "Mở Cổng Đăng Ký Đại Chiến Thế Giới",
                "Mở Sự Kiện Bảo Vệ Pháo Đài",
                "Bật/Tắt Event Halloween",
                "Bật/Tắt Event Noel",
                "Bật/Tắt Event Trồng Cây",
                "Bật/Tắt Mở Tự Do Phó Bản Khổng Lồ",
                "Quay Lai Menu Boss"
            },
            new short[]{136, 118, 146, 110, 155, 155, 155, 142, 134}
        );
    }

    public void handleOtherEventsMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                DauTruongTuDo.Open();
                p.getService().send_box_ThongBao_OK("Đã mở thủ công Đấu Trường Tự Do!");
                break;
            }
            case 1: {
                DauTruongTuDo.Close();
                p.getService().send_box_ThongBao_OK("Đã đóng Đấu Trường Tự Do & Phát Quà Top!");
                break;
            }
            case 2: {
                WorldWar.status = WorldWar.STATUS_REGISTER;
                try { Manager.gI().chatKTG(0, "Đại Chiến Thế Giới đã chính thức mở cổng đăng ký từ Admin!", 5); } catch (Exception ignored) {}
                p.getService().send_box_ThongBao_OK("Đã mở đăng ký Đại Chiến Thế Giới!");
                break;
            }
            case 3: {
                BaoVePhaoDai.is_open = true;
                try { Manager.gI().chatKTG(0, "Sự kiện Bảo Vệ Pháo Đài đã kích hoạt từ Admin!", 5); } catch (Exception ignored) {}
                p.getService().send_box_ThongBao_OK("Đã mở Bảo Vệ Pháo Đài!");
                break;
            }
            case 4: {
                boolean current = EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
                EventManager.setEventActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025, !current);
                p.getService().send_box_ThongBao_OK("Event Halloween -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 5: {
                boolean current = EventManager.isActive(1);
                EventManager.setEventActive(1, !current);
                p.getService().send_box_ThongBao_OK("Event Noel -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 6: {
                boolean current = EventManager.isActive(3);
                EventManager.setEventActive(3, !current);
                p.getService().send_box_ThongBao_OK("Event Trồng Cây -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 7: {
                map.zones.TranChienKhongLo.isForceOpen = !map.zones.TranChienKhongLo.isForceOpen;
                p.getService().send_box_ThongBao_OK("Phó Bản Khổng Lồ -> " + (map.zones.TranChienKhongLo.isForceOpen ? "Đang Mở Tự Do" : "Mở Theo Giờ: 21h T3, 5, 7"));
                break;
            }
            case 8: openBossEvent(p); break;
        }
    }

    // ==============================================================
    //  11. QUẢN LÝ NGƯỜI CHƠI THẬT & KTG
    // ==============================================================

    public static List<Player> getRealOnlinePlayers() {
        Map<Integer, Player> map = new LinkedHashMap<>();
        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 != null && p0.conn != null && p0.conn.connected
                    && !p0.isBot && !p0.isDe
                    && !(p0 instanceof model.DeTu)
                    && !(p0 instanceof bot.mercenary.MercenaryBot)
                    && !(p0 instanceof bot.botplayer.BotPlayerReal)
                    && !(p0 instanceof bot.Bot)) {
                map.put(p0.IDPlayer, p0);
            }
        }
        return new ArrayList<>(map.values());
    }

    public static String getMapName(Player p) {
        if (p == null || p.map == null || p.map.template == null) return "Không xác định";
        return p.map.template.name;
    }

    public void openPlayerManagement(Player p) throws IOException {
        List<Player> realPlayers = getRealOnlinePlayers();
        List<model.Menu> menus = new ArrayList<>();

        // 1. Xem danh sách toàn bộ người chơi thật online
        menus.add(new model.Menu("Danh Sách Người Chơi Thật Online: " + realPlayers.size() + " Người", (short) 133, () -> {
            try {
                openRealPlayersList(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openRealPlayersList error: " + e.getMessage());
            }
        }));

        // 2. Thông báo KTG toàn server
        menus.add(new model.Menu("Gửi Thông Báo KTG Toàn Server", (short) 134, () -> {
            try {
                p.sendInput("Nội dung thông báo toàn server:", new String[]{"Nhập thông báo"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        try {
                            Manager.gI().chatKTG(0, "[ADMIN " + p.name + "]: " + inputs[0], 5);
                        } catch (Exception ignored) {}
                    }
                });
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "chatKTG error: " + e.getMessage());
            }
        }));

        // 3. Phục hồi 100% HP/MP toàn server
        menus.add(new model.Menu("Phục Hồi 100% HP/MP Cho TOÀN BỘ Server", (short) 133, () -> {
            try {
                int count = 0;
                for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
                    if (p0 != null && p0.ability != null) {
                        p0.hp = p0.ability.get_hp_max(true);
                        p0.mp = p0.ability.get_mp_max(true);
                        p0.update_info_to_all();
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã phục hồi 100% HP/MP cho " + count + " người chơi online!");
                openPlayerManagement(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "healAll error: " + e.getMessage());
            }
        }));

        // 4. Tặng quà toàn server
        menus.add(new model.Menu("Tặng 500k Beri + 1k Ruby Cho TOÀN BỘ Server", (short) 110, () -> {
            try {
                int count = 0;
                for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
                    if (p0 != null) {
                        p0.update_vang(500_000);
                        p0.update_ruby(1_000);
                        p0.updateMoney();
                        try { p0.getService().send_box_ThongBao_OK("Bạn nhận được 500,000 Beri + 1,000 Ruby từ Admin!"); } catch (Exception ignored) {}
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã phát phần thưởng cho " + count + " người chơi online!");
                openPlayerManagement(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "giftAll error: " + e.getMessage());
            }
        }));

        // 5. Quay lại menu chính
        menus.add(new model.Menu("Quay Lại Menu Chính", (short) 134, () -> {
            try {
                openMain(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openMain error: " + e.getMessage());
            }
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "Quản Lý Người Chơi Thật & Server", menus);
    }

    public void openRealPlayersList(Player p) throws IOException {
        List<Player> realPlayers = getRealOnlinePlayers();
        List<model.Menu> menus = new ArrayList<>();

        if (realPlayers.isEmpty()) {
            menus.add(new model.Menu("Hiện không có người chơi thật nào khác online", (short) 118, () -> {
                try {
                    openRealPlayersList(p);
                } catch (Exception ignored) {}
            }));
        } else {
            for (Player target : realPlayers) {
                if (target == null) continue;
                String mapInfo = getMapName(target) + " [Khu " + (target.map != null ? (target.map.zone_id + 1) : 1) + "]";
                String title = "[Lv." + target.level + " - " + getClassName(target.clazz) + "] " + target.name + " (" + mapInfo + ")";
                short icon = (short) 110;

                menus.add(new model.Menu(title, icon, () -> {
                    try {
                        openPlayerDetailActionMenu(p, target);
                    } catch (Exception e) {
                        Log.error("AdminSystemMenu", "openPlayerDetailActionMenu error: " + e.getMessage());
                    }
                }));
            }
        }

        // Nút Làm Mới & Quay Lại
        menus.add(new model.Menu("Làm Mới Danh Sách", (short) 116, () -> {
            try {
                openRealPlayersList(p);
            } catch (Exception ignored) {}
        }));

        menus.add(new model.Menu("Quay Lại", (short) 134, () -> {
            try {
                openPlayerManagement(p);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "Danh Sách Người Chơi Thật: " + realPlayers.size() + " Online)", menus);
    }

    public void openPlayerDetailActionMenu(Player p, Player target) throws IOException {
        if (target == null || target.conn == null || !target.conn.connected) {
            p.getService().send_box_ThongBao_OK("Người chơi này đã rời mạng: Offline!");
            openRealPlayersList(p);
            return;
        }

        List<model.Menu> menus = new ArrayList<>();

        // 1. Xem thông tin & chỉ số chi tiết
        menus.add(new model.Menu("Xem Thông Tin & Chỉ Số Chi Tiết", (short) 110, () -> {
            try {
                int maxHp = target.ability != null ? target.ability.get_hp_max(true) : target.hp;
                int maxMp = target.ability != null ? target.ability.get_mp_max(true) : target.mp;
                int dame = target.ability != null ? target.ability.get_dame(false) : 0;
                int def = target.ability != null ? target.ability.get_def(false) : 0;
                String clanName = (target.clan != null && target.clan.name != null) ? target.clan.name : "Chưa gia nhập";
                String ip = (target.conn != null && target.conn.ip != null) ? target.conn.ip : "N/A";
                String mapStr = (target.map != null && target.map.template != null) ? (target.map.template.name + " (Map " + target.map.template.id + ") - Khu " + (target.map.zone_id + 1)) : "N/A";

                String info = "=== THÔNG TIN: " + target.name.toUpperCase() + " ===\n" +
                    "Tên: "+ target.name + "(ID: "+ target.IDPlayer + ")\n"+
                    "Class: "+ getClassName(target.clazz) + "| Level: "+ target.level + "\n"+
                    "Băng Hải Tặc: "+ clanName + "\n"+
                    "HP: "+ ZUtil.number_format(target.hp) + "/ "+ ZUtil.number_format(maxHp) + "\n"+
                    "MP: "+ ZUtil.number_format(target.mp) + "/ "+ ZUtil.number_format(maxMp) + "\n"+
                    "Sát Thương: "+ ZUtil.number_format(dame) + "| Phòng Thủ: "+ ZUtil.number_format(def) + "\n"+
                    "Vàng Beri: "+ ZUtil.number_format(target.get_vang()) + "\n"+
                    "Ruby: "+ ZUtil.number_format(target.get_ngoc()) + "| Extol: "+ ZUtil.number_format(target.get_vnd()) + "\n"+
                    "Điểm Tiềm Năng: "+ target.pointAttribute + "| Điểm Kỹ Năng: "+ target.pointSkill + "\n"+
                    "Vị Trí: "+ mapStr + "(X: "+ target.x + ", Y: "+ target.y + ")\n"+
                    "IP Kết Nối: "+ ip;
                p.getService().send_box_ThongBao_OK(info);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "viewPlayerInfo error: " + e.getMessage());
            }
        }));

        // 2. Xem kỹ năng & Trái ác quỷ (Full kỹ năng)
        menus.add(new model.Menu("Xem Kỹ Năng & Trái Ác Quỷ Đầy Đủ Kỹ Năng", (short) 155, () -> {
            try {
                showPlayerSkillsInspect(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "showPlayerSkillsInspect error: " + e.getMessage());
            }
        }));

        // 3. Dịch chuyển ĐẾN người chơi
        menus.add(new model.Menu("Dịch Chuyển ĐẾN Vị Trí Người Chơi", (short) 116, () -> {
            try {
                TimedDungeonManager.teleportPlayerToPlayer(p, target.name);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "teleportToPlayer error: " + e.getMessage());
            }
        }));

        // 4. KÉO người chơi đến Admin
        menus.add(new model.Menu("KÉO Người Chơi Đến Vị Trí Admin", (short) 116, () -> {
            try {
                TimedDungeonManager.pullPlayerToAdmin(p, target.name);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "pullPlayerToAdmin error: " + e.getMessage());
            }
        }));

        // 5. Ép người chơi đi phó bản / đổi map
        menus.add(new model.Menu("Ép Người Chơi Đi Phó Bản / Đổi Map", (short) 133, () -> {
            try {
                openForcePlayerDungeonMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openForcePlayerDungeonMenu error: " + e.getMessage());
            }
        }));

        // 6. Buff cho người chơi
        menus.add(new model.Menu("Buff Cho Người Chơi Này", (short) 110, () -> {
            try {
                openBuffPlayerSubMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openBuffPlayerSubMenu error: " + e.getMessage());
            }
        }));

        // 7. Trừng phạt / Khóa / Ban / Kick
        menus.add(new model.Menu("Trừng Phạt / Khóa / Ban / Kick", (short) 118, () -> {
            try {
                openPunishPlayerSubMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "openPunishPlayerSubMenu error: " + e.getMessage());
            }
        }));

        // 8. Quay lại danh sách
        menus.add(new model.Menu("Quay Lại Danh Sách Người Chơi", (short) 134, () -> {
            try {
                openRealPlayersList(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "return to real players list error: " + e.getMessage());
            }
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "QUẢN LÝ: " + target.name + " [Lv." + target.level + "]", menus);
    }

    public static void showPlayerSkillsInspect(Player p, Player target) throws IOException {
        if (target == null || target.skill_point == null || target.skill_point.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Người chơi này chưa có kỹ năng nào.");
            return;
        }

        List<String> names = new ArrayList<>();
        List<Integer> icons = new ArrayList<>();
        p.tempSkillList = new ArrayList<>();

        for (Skill_info sk : target.skill_point) {
            if (sk == null || sk.temp == null) continue;

            String prefix = "";
            int lv = (sk.temp.Lv_RQ > 0) ? sk.temp.Lv_RQ : 1;

            boolean isDevil = (sk.temp.ID >= 2000 
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
            } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690 || sk.temp.ID == 1016 || sk.temp.ID == 1017) {
                prefix = "[Haki] ";
            } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
                prefix = "[Buff Phái] ";
            } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
                prefix = "[Nội tại] ";
            } else {
                prefix = "[Chủ động] ";
            }

            String devilTag = sk.lvdevil > 0 ? " (+" + sk.lvdevil + " Ác Quỷ " + sk.devilpercent + "%)" : "";
            names.add(prefix + sk.temp.name + " (Lv." + lv + ")" + devilTag);
            icons.add((int) sk.temp.idIcon);
            p.tempSkillList.add(sk);
        }

        if (names.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Người chơi này chưa có kỹ năng nào.");
            return;
        }

        p.getService().send_dynamic_menu_type4(MENU_PLAYER_SKILL_INSPECT, 0, "Kỹ Năng: " + target.name, names, icons);
    }

    public static void handlePlayerSkillInspectSelect(Player p, int index) throws IOException {
        if (p.tempSkillList == null || index < 0 || index >= p.tempSkillList.size()) return;
        Skill_info sk = p.tempSkillList.get(index);
        if (sk == null || sk.temp == null) return;

        boolean isDevil = (sk.temp.ID >= 2000 
                || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659) 
                || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802) 
                || sk.temp.typeDevil > 0)
                && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

        String typeStr = "Chủ động tấn công";
        if (isDevil) {
            typeStr = "Năng lực Trái Ác Quỷ Năng Lực Ác Quỷ";
        } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690 || sk.temp.ID == 1016 || sk.temp.ID == 1017) {
            typeStr = "Bí kỹ Haki Tối Thượng";
        } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
            typeStr = "Kỹ năng Buff Môn Phái";
        } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
            typeStr = "Kỹ năng Bị Động / Nội Tại";
        } else if (sk.temp.typeSkill == 1) {
            typeStr = "Kỹ năng Buff / Hỗ trợ";
        }

        int lv = sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : (sk.lvdevil > 0 ? sk.lvdevil : 1);

        String detail = "" + sk.temp.name + "\n"
            + "- Phân loại: " + typeStr + "\n"
            + "- Cấp độ chiêu: Cấp " + lv + (sk.lvdevil > 0 ? (": + Cấp " + sk.lvdevil + " Ác Quỷ " + sk.devilpercent + "%)") : "") + "\n"
            + (sk.temp.damage > 0 ? ("- Sát thương cơ bản: " + sk.temp.damage + "\n") : "")
            + (sk.temp.manaLost > 0 ? ("- Tiêu hao: " + sk.temp.manaLost + " MP\n") : "")
            + "- Phạm vi tác dụng: " + (sk.temp.rangeLan > 0 ? sk.temp.rangeLan + "m" : "Đơn mục tiêu / Bản thân") + "\n"
            + "- Mô tả: " + (sk.temp.info != null && !sk.temp.info.isBlank() ? sk.temp.info : "Kỹ năng chiến đấu.");

        p.getService().send_box_ThongBao_OK(detail);
    }

    public void openForcePlayerDungeonMenu(Player p, Player target) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        int[][] dungeons = new int[][]{
            {69, 133},  // Đấu Trường Tự Do
            {107, 136}, // Trận Chiến Khổng Lồ
            {113, 146}, // Đại Chiến Thế Giới
            {88, 110},  // Bảo Vệ Pháo Đài
            {72, 133},  // Thủ Lĩnh Biển Khơi
            {1, 116},   // Làng Cối Xay Gió
            {49, 116},  // Làng Khởi Đầu
            {9, 116},   // Thị Trấn Vỏ Sò
            {66, 116}   // Mỏm Sinh Đôi
        };
        String[] dungeonNames = new String[]{
            "Đấu Trường Tự Do: Map 69",
            "Trận Chiến Khổng Lồ: Map 107",
            "Đại Chiến Thế Giới: Map 113",
            "Bảo Vệ Pháo Đài: Map 88",
            "Thủ Lĩnh Biển Khơi: Map 72",
            "Làng Cối Xay Gió: Map 1",
            "Làng Khởi Đầu: Map 49",
            "Thị Trấn Vỏ Sò: Map 9",
            "Mỏm Sinh Đôi: Map 66"
        };

        for (int i = 0; i < dungeons.length; i++) {
            int mapId = dungeons[i][0];
            short icon = (short) dungeons[i][1];
            String name = dungeonNames[i];
            menus.add(new model.Menu(name, icon, () -> {
                try {
                    TimedDungeonManager.teleportPlayerToMap(target, mapId);
                    p.getService().send_box_ThongBao_OK("Đã ép " + target.name + " dịch chuyển đến " + name + "!");
                    try { target.getService().send_box_ThongBao_OK("Bạn đã được Admin dịch chuyển đến " + name + "!"); } catch (Exception ignored) {}
                    openPlayerDetailActionMenu(p, target);
                } catch (Exception e) {
                    Log.error("AdminSystemMenu", "forceTeleport error: " + e.getMessage());
                }
            }));
        }

        // Tự nhập map
        menus.add(new model.Menu("Tự Nhập ID Map Muốn Ép Đến", (short) 110, () -> {
            try {
                p.sendInput("Nhập ID Map muốn ép người chơi đến: Ví dụ: 69:", new String[]{"ID Map"}, (inputs) -> {
                    try {
                        if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                            int mapId = Integer.parseInt(inputs[0].trim());
                            TimedDungeonManager.teleportPlayerToMap(target, mapId);
                            p.getService().send_box_ThongBao_OK("Đã ép " + target.name + " đến Map " + mapId + "!");
                            try { target.getService().send_box_ThongBao_OK("Bạn đã được Admin dịch chuyển đến Map " + mapId + "!"); } catch (Exception ignored) {}
                            openPlayerDetailActionMenu(p, target);
                        }
                    } catch (Exception ignored) {}
                });
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "customMap error: " + e.getMessage());
            }
        }));

        menus.add(new model.Menu("Quay Lai", (short) 134, () -> {
            try {
                openPlayerDetailActionMenu(p, target);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "Ép " + target.name + " Đi Map / Phó Bản", menus);
    }

    public void openBuffPlayerSubMenu(Player p, Player target) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // 1. Phục hồi 100% HP/MP
        menus.add(new model.Menu(" Phục Hồi 100% HP & MP Max", (short) 133, () -> {
            try {
                if (target.ability != null) {
                    target.hp = target.ability.get_hp_max(true);
                    target.mp = target.ability.get_mp_max(true);
                }
                target.update_info_to_all();
                target.getService().Main_char_Info(false);
                p.getService().send_box_ThongBao_OK("Đã hồi phục 100% HP/MP cho " + target.name + "!");
                try { target.getService().send_box_ThongBao_OK("Admin đã hồi phục 100% HP/MP cho bạn!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "buffHeal error: " + e.getMessage());
            }
        }));

        // 2. Buff Full Cấp Ác Quỷ (+5) 100%
        menus.add(new model.Menu("Buff Full Cấp Ác Quỷ Cấp 5 100% All Kỹ Năng", (short) 155, () -> {
            try {
                buffFullDevilSkills(target, true);
                p.getService().send_box_ThongBao_OK("Đã Buff Full Cấp Ác Quỷ Cấp 5 100% cho " + target.name + "!");
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "buffDevil error: " + e.getMessage());
            }
        }));

        // 3. Tặng 50M Beri + 5k Ruby + 50k Extol
        menus.add(new model.Menu(" Tặng 50M Beri + 5k Ruby + 50k Extol", (short) 110, () -> {
            try {
                target.update_vang(50_000_000);
                target.update_ruby(5_000);
                target.updateVnd(50_000);
                target.updateMoney();
                p.getService().send_box_ThongBao_OK("Đã tặng 50M Beri + 5k Ruby + 50k Extol cho " + target.name + "!");
                try { target.getService().send_box_ThongBao_OK("Bạn nhận được 50,000,000 Beri + 5,000 Ruby + 50,000 Extol từ Admin!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "buffMoney error: " + e.getMessage());
            }
        }));

        // 4. Tăng +10 Level
        menus.add(new model.Menu(" Tăng +10 Level Cho Nhân Vật", (short) 133, () -> {
            try {
                int oldLevel = target.level;
                target.level = (short) Math.min(150, target.level + 10);
                for (int lv = oldLevel; lv < target.level; lv++) {
                    target.pointAttribute += (lv < 100) ? Level.ENTRYS[lv - 1].tiemnang : Level.ENTRYS[lv - 2].tiemnang;
                }
                if (target.ability != null) {
                    target.hp = target.ability.get_hp_max(true);
                    target.mp = target.ability.get_mp_max(true);
                }
                target.update_info_to_all();
                target.getService().Main_char_Info(false);
                p.getService().send_box_ThongBao_OK("Đã tăng Level cho " + target.name + " lên Lv." + target.level + "!");
                try { target.getService().send_box_ThongBao_OK("Admin đã nâng cấp bạn lên Level " + target.level + "!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "buffLevel error: " + e.getMessage());
            }
        }));

        // 5. Tặng Bộ Trái Ác Quỷ Thượng Cấp vào Túi
        menus.add(new model.Menu(" Tặng Bộ Trái Ác Quỷ Thượng Cấp vào Túi", (short) 155, () -> {
            try {
                if (target.item != null) {
                    int[] dfIds = new int[]{207, 208, 209, 210, 211, 212, 213, 214};
                    for (int dfId : dfIds) {
                        target.item.add_item_bag47(4, (short) dfId, 5);
                    }
                    target.item.updateInventory(false);
                }
                p.getService().send_box_ThongBao_OK("Đã thêm Bộ Trái Ác Quỷ Thượng Cấp vào túi của " + target.name + "!");
                try { target.getService().send_box_ThongBao_OK("Bạn nhận được Bộ Trái Ác Quỷ Thượng Cấp từ Admin!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "buffDF error: " + e.getMessage());
            }
        }));

        menus.add(new model.Menu("Quay Lai", (short) 134, () -> {
            try {
                openPlayerDetailActionMenu(p, target);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "Buff Cho: " + target.name, menus);
    }

    public void openPunishPlayerSubMenu(Player p, Player target) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // 1. Kick khỏi server
        menus.add(new model.Menu("Kick Khoi Server Ngay Lap Tuc", (short) 118, () -> {
            try {
                if (target.conn != null) {
                    target.conn.close();
                }
                p.getService().send_box_ThongBao_OK("Đã ngắt kết nối " + target.name + " khỏi server!");
                openRealPlayersList(p);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "kickPlayer error: " + e.getMessage());
            }
        }));

        // 2. Hạ gục tại chỗ (HP về 0)
        menus.add(new model.Menu("Ha Guc / Danh Bai Tai Cho (HP ve 0)", (short) 118, () -> {
            try {
                target.hp = 0;
                target.isdie = true;
                target.update_info_to_all();
                p.getService().send_box_ThongBao_OK("Đã hạ gục " + target.name + " tại chỗ!");
                try { target.getService().send_box_ThongBao_OK("Bạn đã bị Admin trừng phạt hạ gục!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "killPlayer error: " + e.getMessage());
            }
        }));

        // 3. Khóa mõm / Cấm chat
        menus.add(new model.Menu("Khoa Mom (Cam Chat Server)", (short) 118, () -> {
            try {
                target.isMute = !target.isMute;
                p.getService().send_box_ThongBao_OK(target.isMute ? ("Đã cấm chat người chơi " + target.name + "!") : ("Đã mở chat cho " + target.name + "!"));
                try { target.getService().send_box_ThongBao_OK(target.isMute ? "Bạn đã bị Admin cấm chat !" : "Bạn đã được Admin mở khóa chat!"); } catch (Exception ignored) {}
                openPlayerDetailActionMenu(p, target);
            } catch (Exception e) {
                Log.error("AdminSystemMenu", "mutePlayer error: " + e.getMessage());
            }
        }));

        menus.add(new model.Menu("Quay Lai", (short) 134, () -> {
            try {
                openPlayerDetailActionMenu(p, target);
            } catch (Exception ignored) {}
        }));

        p.getService().openDynamicMenu(MENU_PLAYER_MANAGEMENT, "Trừng Phạt: " + target.name, menus);
    }

    private void handlePlayerManagement(Player p, int index) throws IOException {
        openPlayerManagement(p);
    }

    // ==============================================================
    //  13. QUẢN LÝ / XÓA THẦN TRANG (CLEAN THAN TRANG SYSTEM)
    // ==============================================================

    public static void openCleanThanTrangMenu(Player p) throws IOException {
        if (!isAdmin(p)) return;
        iMenuDymanic.buildAndSend(p, MENU_CLEAN_THAN_TRANG,
            "QUẢN LÝ / XÓA THẦN TRANG HỆ THỐNG",
            new String[]{
                "Xóa Thần Trang của TÔI Bản Thân",
                "Xóa Thần Trang ALL Người Chơi ONLINE",
                "Xóa Thần Trang TOÀN BỘ SERVER: Online & Offline DB",
                "Xóa Thần Trang Người Chơi Theo Tên Online Hoặc Offline",
                "Nhan Set Than Trang Test (AUTO MAX +20, 6 Lo, Hoan My, Kich An)",
                "Quay Lai Menu Admin"
            },
            new short[]{118, 133, 110, 155, 133, 134}
        );
    }

    private void handleCleanThanTrangMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;
        switch (index) {
            case 0: { // 1. Xóa Thần Trang của TÔI
                int deleted = removeThanTrangPlayer(p);
                p.getService().send_box_ThongBao_OK("Đã xóa sạch " + deleted + " món Thần Trang: Hành trang, Rương, Đang mặccủa bạn!");
                break;
            }
            case 1: { // 2. Xóa Thần Trang ALL Online
                int totalDeleted = 0;
                int playerCount = 0;
                for (Player pl : SessionManager.PLAYERS_MAP.values()) {
                    if (pl != null && !pl.isClosed) {
                        int del = removeThanTrangPlayer(pl);
                        if (del > 0) {
                            totalDeleted += del;
                            playerCount++;
                            if (pl != p) {
                                try {
                                    pl.getService().send_box_ThongBao_OK(" Admin đã thu hồi/xóa toàn bộ Thần Trang để kiểm tra hệ thống!");
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã xóa tổng cộng " + totalDeleted + " món Thần Trang từ " + playerCount + " người chơi Online!");
                break;
            }
            case 2: { // 3. Xóa Thần Trang TOÀN BỘ SERVER (Online & Offline DB)
                p.getService().send_box_ThongBao_OK("Đang tiến hành quét và xóa toàn bộ Thần Trang Server: Online + Offline DB...");
                new Thread(() -> {
                    try {
                        int totalDeleted = cleanAllThanTrangDatabase();
                        p.getService().send_box_ThongBao_OK(" HOÀN TẤT: Đã xóa thành công toàn bộ " + totalDeleted + " món Thần Trang trên toàn Server: Online & Offline Database!");
                    } catch (Exception e) {
                        Log.error("AdminSystemMenu", "cleanAllThanTrangDatabase error: " + e.getMessage());
                        try {
                            p.getService().send_box_ThongBao_OK("Lỗi khi xóa Thần Trang toàn server: " + e.getMessage());
                        } catch (Exception ignored) {}
                    }
                }).start();
                break;
            }
            case 3: { // 4. Xóa Thần Trang Theo Tên
                p.sendInput("Nhập tên nhân vật cần xóa Thần Trang Online Hoặc Offline:", new String[]{"Tên Nhân Vật"}, (inputs) -> {
                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                    String targetName = inputs[0].trim();
                    new Thread(() -> {
                        cleanThanTrangByPlayerName(p, targetName);
                    }).start();
                });
                break;
            }
            case 4: { // 5. Nhận Set Thần Trang Test AUTO MAX
                openGiveThanTrangSetMenu(p);
                break;
            }
            case 5: { // 6. Quay lại
                openMain(p);
                break;
            }
        }
    }

    public static final String[] THAN_TRANG_SET_NAMES = template.ThanTrangConfig.SET_NAMES;

    public static int getThanTrangStartId(int setIndex) {
        return 2604 + setIndex * 6;
    }

    public static void openGiveThanTrangSetMenu(Player p) throws IOException {
        openGiveThanTrangSetMenu(p, 0);
    }

    public static void openGiveThanTrangSetMenu(Player p, int page) throws IOException {
        if (!isAdmin(p)) return;
        if (page == 0) {
            String[] menuItems = new String[10];
            short[] icons = new short[10];
            for (int i = 0; i < 8; i++) {
                menuItems[i] = THAN_TRANG_SET_NAMES[i];
                icons[i] = 133;
            }
            menuItems[8] = ">> Trang Sau (Set 9 Đến Set 15) >>";
            icons[8] = 155;
            menuItems[9] = "Quay Lại";
            icons[9] = 134;

            iMenuDymanic.buildAndSend(p, MENU_THAN_TRANG_PAGE1,
                "CHỌN SET THẦN TRANG (TRANG 1/2: SET 1-8)",
                menuItems, icons
            );
        } else {
            String[] menuItems = new String[9];
            short[] icons = new short[9];
            for (int i = 0; i < 7; i++) {
                menuItems[i] = THAN_TRANG_SET_NAMES[8 + i];
                icons[i] = 133;
            }
            menuItems[7] = "<< Trang Trước (Set 1 Đến Set 8) <<";
            icons[7] = 155;
            menuItems[8] = "Quay Lại";
            icons[8] = 134;

            iMenuDymanic.buildAndSend(p, MENU_THAN_TRANG_PAGE2,
                "CHỌN SET THẦN TRANG (TRANG 2/2: SET 9-15)",
                menuItems, icons
            );
        }
    }

    public void handleGiveThanTrangSetMenu(Player p, int index) throws IOException {
        handleGiveThanTrangSetMenu(p, index, 0);
    }

    public void handleGiveThanTrangSetMenu(Player p, int index, int page) throws IOException {
        if (!isAdmin(p)) return;
        if (page == 0) {
            if (index >= 0 && index < 8) {
                playerSelectedThanTrangSet.put(p.IDPlayer, index);
                openThanTrangActionMenu(p, index);
            } else if (index == 8) {
                openGiveThanTrangSetMenu(p, 1);
            } else {
                openCleanThanTrangMenu(p);
            }
        } else {
            if (index >= 0 && index < 7) {
                int setIdx = 8 + index;
                playerSelectedThanTrangSet.put(p.IDPlayer, setIdx);
                openThanTrangActionMenu(p, setIdx);
            } else if (index == 7) {
                openGiveThanTrangSetMenu(p, 0);
            } else {
                openCleanThanTrangMenu(p);
            }
        }
    }

    public static void openThanTrangActionMenu(Player p, int setIndex) throws IOException {
        if (setIndex < 0 || setIndex >= THAN_TRANG_SET_NAMES.length) return;
        String sName = THAN_TRANG_SET_NAMES[setIndex];
        iMenuDymanic.buildAndSend(p, MENU_THAN_TRANG_ACTION,
            "THAO TÁC CHO " + sName.toUpperCase(),
            new String[]{
                "1. Thần Trang Thuần Túy (Không Khảm, Tắt Lỗ, Không Ẩn, Max Gốc +20) -> Nhận Túi",
                "2. Thần Trang Thuần Túy (Không Khảm, Tắt Lỗ, Không Ẩn, Max Gốc +20) -> Mặc Đè Bản Thân",
                "3. Thần Trang Thuần Túy (Không Khảm, Tắt Lỗ, Không Ẩn, Max Gốc +20) -> Mặc Đè Đệ Tử",
                "4. Thần Trang Cực Phẩm (Full 6 Lỗ Đá Thần Thoại + Kích Ẩn Bộc Phá) -> Nhận Túi",
                "5. Thần Trang Cực Phẩm (Full 6 Lỗ Đá Thần Thoại + Kích Ẩn Bộc Phá) -> Mặc Đè Bản Thân",
                "6. Thần Trang Cực Phẩm (Full 6 Lỗ Đá Thần Thoại + Kích Ẩn Bộc Phá) -> Mặc Đè Đệ Tử",
                "Quay Lại Danh Sách Set"
            },
            new short[]{118, 118, 118, 133, 133, 133, 134}
        );
    }

    public void handleThanTrangAction(Player p, int index) throws IOException {
        int setIdx = playerSelectedThanTrangSet.getOrDefault(p.IDPlayer, 0);
        switch (index) {
            case 0:
                giveThanTrangSetPure(p, setIdx);
                break;
            case 1:
                equipThanTrangSetPure(p, p, setIdx);
                break;
            case 2:
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                } else {
                    equipThanTrangSetPure(p, p.detu, setIdx);
                }
                break;
            case 3:
                giveThanTrangSetSuper(p, setIdx);
                break;
            case 4:
                equipThanTrangSetSuper(p, p, setIdx);
                break;
            case 5:
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                } else {
                    equipThanTrangSetSuper(p, p.detu, setIdx);
                }
                break;
            case 6:
                openGiveThanTrangSetMenu(p, setIdx < 8 ? 0 : 1);
                break;
        }
    }

    public static Item_wear createThanTrangPureItem(int templateId, int slot) {
        ItemTemplate3 tpl = ItemTemplate3.get_it_by_id(templateId);
        if (tpl == null) return null;
        Item_wear it = new Item_wear();
        it.setup_template_by_id(tpl);
        it.index = (short) slot;
        it.levelUp = 20;
        it.numLoKham = 0;
        it.numHoleDaDuc = 0;
        it.mdakham = new short[0];
        it.valueChetac = 0;
        it.isHoanMy = 0;
        it.valueKichAn = -1;
        it.typelock = 1;
        return it;
    }

    public static Item_wear createThanTrangSuperItem(int templateId, int slot) {
        ItemTemplate3 tpl = ItemTemplate3.get_it_by_id(templateId);
        if (tpl == null) return null;
        Item_wear it = new Item_wear();
        it.setup_template_by_id(tpl);
        it.index = (short) slot;
        it.levelUp = 20;
        it.numLoKham = 6;
        it.numHoleDaDuc = 6;
        it.mdakham = new short[]{677, 677, 326, 910, 647, 652};
        it.valueChetac = 100;
        it.isHoanMy = 100;
        it.valueKichAn = 4; // Bộc phá
        it.typelock = 1;
        it.option_item_2 = new ArrayList<>();
        for (short gid : it.mdakham) {
            Rebuild_Item.add_op_ngoc_kham_new(it, gid);
        }
        return it;
    }

    public static void giveThanTrangSetPure(Player p, int setIndex) {
        if (p == null || p.item == null || setIndex < 0 || setIndex >= THAN_TRANG_SET_NAMES.length) return;
        int startId = getThanTrangStartId(setIndex);
        for (int i = 0; i < 6; i++) {
            Item_wear it = createThanTrangPureItem(startId + i, 8 + i);
            if (it != null) p.item.add_item_bag3(it);
        }
        try {
            p.item.updateInventory(false);
            p.setAbility();
            p.update_info_to_all();
            p.getService().send_box_ThongBao_OK("Đã nhận 6 món " + THAN_TRANG_SET_NAMES[setIndex] + "\n[Thuần Túy: +20 MAX, Tắt Lỗ, Không Đá, Không Kích Ẩn] vào hành trang!");
        } catch (Exception ignored) {}
    }

    public static void giveThanTrangSetSuper(Player p, int setIndex) {
        if (p == null || p.item == null || setIndex < 0 || setIndex >= THAN_TRANG_SET_NAMES.length) return;
        int startId = getThanTrangStartId(setIndex);
        for (int i = 0; i < 6; i++) {
            Item_wear it = createThanTrangSuperItem(startId + i, 8 + i);
            if (it != null) p.item.add_item_bag3(it);
        }
        try {
            p.item.updateInventory(false);
            p.setAbility();
            p.update_info_to_all();
            p.getService().send_box_ThongBao_OK("Đã nhận 6 món " + THAN_TRANG_SET_NAMES[setIndex] + "\n[Cực Phẩm: +20 MAX, Full 6 Lỗ Đá Thần Thoại + Kích Ẩn Bộc Phá] vào hành trang!");
        } catch (Exception ignored) {}
    }

    public static void equipThanTrangSetPure(Player admin, Player target, int setIndex) {
        if (admin == null || target == null || target.item == null || setIndex < 0 || setIndex >= THAN_TRANG_SET_NAMES.length) return;
        if (target.item.it_body == null || target.item.it_body.length < itemz.Item.MAX_BODY) {
            Item_wear[] newBody = new Item_wear[itemz.Item.MAX_BODY];
            if (target.item.it_body != null) System.arraycopy(target.item.it_body, 0, newBody, 0, Math.min(target.item.it_body.length, newBody.length));
            target.item.it_body = newBody;
        }
        int startId = getThanTrangStartId(setIndex);
        for (int i = 0; i < 6; i++) {
            Item_wear it = createThanTrangPureItem(startId + i, 8 + i);
            target.item.it_body[8 + i] = it;
        }
        try {
            target.updateParts();
            if (target.ability != null) target.ability.recalculatePlayerStats(target);
            target.item.updateInventory(false);
            target.update_info_to_all();
            if (target.getService() != null) {
                target.getService().Main_char_Info(true);
                target.getService().charWearing(target, false);
            }
            target.flush(target, false);
            admin.getService().send_box_ThongBao_OK("Đã mặc đè 6 món " + THAN_TRANG_SET_NAMES[setIndex] + "\n[Thuần Túy: +20 MAX, Tắt Lỗ, Không Đá, Không Kích Ẩn] cho " + (target == admin ? "Bản Thân" : "Đệ Tử") + "!");
        } catch (Exception ignored) {}
    }

    public static void equipThanTrangSetSuper(Player admin, Player target, int setIndex) {
        if (admin == null || target == null || target.item == null || setIndex < 0 || setIndex >= THAN_TRANG_SET_NAMES.length) return;
        if (target.item.it_body == null || target.item.it_body.length < itemz.Item.MAX_BODY) {
            Item_wear[] newBody = new Item_wear[itemz.Item.MAX_BODY];
            if (target.item.it_body != null) System.arraycopy(target.item.it_body, 0, newBody, 0, Math.min(target.item.it_body.length, newBody.length));
            target.item.it_body = newBody;
        }
        int startId = getThanTrangStartId(setIndex);
        for (int i = 0; i < 6; i++) {
            Item_wear it = createThanTrangSuperItem(startId + i, 8 + i);
            target.item.it_body[8 + i] = it;
        }
        try {
            target.updateParts();
            if (target.ability != null) target.ability.recalculatePlayerStats(target);
            target.item.updateInventory(false);
            target.update_info_to_all();
            if (target.getService() != null) {
                target.getService().Main_char_Info(true);
                target.getService().charWearing(target, false);
            }
            target.flush(target, false);
            admin.getService().send_box_ThongBao_OK("Đã mặc đè 6 món " + THAN_TRANG_SET_NAMES[setIndex] + "\n[Cực Phẩm: +20 MAX, Full 6 Lỗ Đá Thần Thoại + Kích Ẩn Bộc Phá] cho " + (target == admin ? "Bản Thân" : "Đệ Tử") + "!");
        } catch (Exception ignored) {}
    }

    public static boolean isThanTrang(Item_wear it) {
        if (it == null || it.template == null) return false;
        return it.isThanTrang();
    }

    public static boolean isThanTrangTemplate(int templateId, int typeEquip) {
        return ItemTemplate3.isThanTrang(templateId, typeEquip);
    }

    /**
     * Xóa toàn bộ Thần Trang của 1 người chơi Online (Hành trang, Rương, Đang mặc, Lưu trữ, Đệ tử).
     */
    public static int removeThanTrangPlayer(Player p) {
        if (p == null || p.item == null) return 0;
        int count = 0;

        // 1. Hành trang (bag3)
        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (isThanTrang(it)) {
                    p.item.bag3[i] = null;
                    count++;
                }
            }
        }

        // 2. Rương đồ (box3)
        if (p.item.box3 != null) {
            for (int i = 0; i < p.item.box3.length; i++) {
                Item_wear it = p.item.box3[i];
                if (isThanTrang(it)) {
                    p.item.box3[i] = null;
                    count++;
                }
            }
        }

        // 3. Đang mặc (it_body)
        if (p.item.it_body != null) {
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = p.item.it_body[i];
                if (isThanTrang(it)) {
                    p.item.it_body[i] = null;
                    count++;
                }
            }
        }

        // 4. Lưu trữ (save_item_wear)
        if (p.item.save_item_wear != null) {
            int before = p.item.save_item_wear.size();
            p.item.save_item_wear.removeIf(AdminSystemMenu::isThanTrang);
            count += (before - p.item.save_item_wear.size());
        }

        // 5. it_heart
        if (p.item.it_heart != null && isThanTrang(p.item.it_heart)) {
            p.item.it_heart = null;
            count++;
        }

        // 6. Đệ tử nếu có đang trang bị Thần Trang
        if (p.detu != null && p.detu.item != null && p.detu.item.it_body != null) {
            for (int i = 0; i < p.detu.item.it_body.length; i++) {
                Item_wear it = p.detu.item.it_body[i];
                if (isThanTrang(it)) {
                    p.detu.item.it_body[i] = null;
                    count++;
                }
            }
            try { p.detu.setAbility(); } catch (Exception ignored) {}
        }

        // Đồng bộ chỉ số và cập nhật giao diện client
        if (count > 0) {
            try {
                p.setAbility();
                if (p.hp > p.hpMax) p.hp = p.hpMax;
                if (p.mp > p.mpMax) p.mp = p.mpMax;
                p.item.updateInventory(false);
                p.item.update_Inventory_box(3, false);
                p.update_info_to_all();
            } catch (Exception ignored) {}
            try {
                p.flush(p, false);
            } catch (Exception ignored) {}
        }

        return count;
    }

    public static Player getOnlinePlayerByName(String name) {
        if (name == null || name.isBlank()) return null;
        for (Player pl : SessionManager.PLAYERS_MAP.values()) {
            if (pl != null && !pl.isClosed && name.equalsIgnoreCase(pl.name)) {
                return pl;
            }
        }
        return null;
    }

    /**
     * Làm sạch chuỗi JSON mảng Item_wear trong DB (loại bỏ tất cả Thần Trang).
     */
    @SuppressWarnings("unchecked")
    public static String cleanThanTrangJsonArray(String jsonStr, int[] deletedCount) {
        if (jsonStr == null || jsonStr.trim().isEmpty() || jsonStr.equals("[]")) {
            return "[]";
        }
        try {
            Object parsed = JSONValue.parse(jsonStr);
            if (parsed instanceof JSONArray) {
                JSONArray arr = (JSONArray) parsed;
                JSONArray newArr = new JSONArray();
                for (Object obj : arr) {
                    if (obj instanceof JSONObject) {
                        JSONObject itemObj = (JSONObject) obj;
                        long id = -1;
                        if (itemObj.containsKey("id")) {
                            Object idVal = itemObj.get("id");
                            if (idVal instanceof Number) {
                                id = ((Number) idVal).longValue();
                            }
                        }
                        if (id > 0 && isThanTrangTemplate((int) id, -1)) {
                            if (deletedCount != null && deletedCount.length > 0) {
                                deletedCount[0]++;
                            }
                            continue; // Bỏ qua món Thần Trang
                        }
                        newArr.add(itemObj);
                    } else if (obj instanceof JSONArray) {
                        JSONArray itemArr = (JSONArray) obj;
                        if (!itemArr.isEmpty()) {
                            try {
                                int id = Integer.parseInt(itemArr.get(0).toString());
                                if (id > 0 && isThanTrangTemplate(id, -1)) {
                                    if (deletedCount != null && deletedCount.length > 0) {
                                        deletedCount[0]++;
                                    }
                                    continue; // Bỏ qua món Thần Trang dạng mảng legacy
                                }
                            } catch (Exception ignored) {}
                        }
                        newArr.add(itemArr);
                    }
                }
                return newArr.toJSONString();
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "cleanThanTrangJsonArray error: " + e.getMessage());
        }
        return jsonStr;
    }

    private static class PlayerDbCleanData {
        int id;
        String bag3, it_body, box3, save_it3;
        PlayerDbCleanData(int id, String bag3, String it_body, String box3, String save_it3) {
            this.id = id;
            this.bag3 = bag3;
            this.it_body = it_body;
            this.box3 = box3;
            this.save_it3 = save_it3;
        }
    }

    /**
     * Quét và xóa toàn bộ Thần Trang trong hệ thống Server (Online + Offline DB).
     */
    public static int cleanAllThanTrangDatabase() {
        int totalDeleted = 0;

        // 1. Dọn dẹp tất cả người chơi Online trước
        for (Player onlineP : SessionManager.PLAYERS_MAP.values()) {
            if (onlineP != null && !onlineP.isClosed) {
                totalDeleted += removeThanTrangPlayer(onlineP);
            }
        }

        // 2. Quét và dọn dẹp trực tiếp trong Database `players` cho toàn bộ tài khoản
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement psSelect = conn.prepareStatement("SELECT `id`, `name`, `bag3`, `it_body`, `box3`, `save_it3` FROM `players`");
             ResultSet rs = psSelect.executeQuery()) {

            List<PlayerDbCleanData> updateList = new ArrayList<>();
            int[] counter = new int[1];

            while (rs.next()) {
                int pId = rs.getInt("id");
                String pName = rs.getString("name");
                String bag3 = rs.getString("bag3");
                String it_body = rs.getString("it_body");
                String box3 = rs.getString("box3");
                String save_it3 = rs.getString("save_it3");

                counter[0] = 0;
                String newBag3 = cleanThanTrangJsonArray(bag3, counter);
                String newItBody = cleanThanTrangJsonArray(it_body, counter);
                String newBox3 = cleanThanTrangJsonArray(box3, counter);
                String newSaveIt3 = cleanThanTrangJsonArray(save_it3, counter);

                if (counter[0] > 0) {
                    Player onP = getOnlinePlayerByName(pName);
                    if (onP == null) {
                        totalDeleted += counter[0];
                        updateList.add(new PlayerDbCleanData(pId, newBag3, newItBody, newBox3, newSaveIt3));
                    }
                }
            }

            if (!updateList.isEmpty()) {
                try (PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE `players` SET `bag3` = ?, `it_body` = ?, `box3` = ?, `save_it3` = ? WHERE `id` = ?")) {
                    for (PlayerDbCleanData d : updateList) {
                        psUpdate.setString(1, d.bag3);
                        psUpdate.setString(2, d.it_body);
                        psUpdate.setString(3, d.box3);
                        psUpdate.setString(4, d.save_it3);
                        psUpdate.setInt(5, d.id);
                        psUpdate.addBatch();
                    }
                    psUpdate.executeBatch();
                }
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "cleanAllThanTrangDatabase players error: " + e.getMessage());
        }

        // 3. Quét dọn dẹp thêm trong players_detu
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement psDeTu = conn.prepareStatement("SELECT `id`, `it_body` FROM `players_detu`");
             ResultSet rsDeTu = psDeTu.executeQuery()) {
            int[] counter = new int[1];
            List<Map.Entry<Integer, String>> updateDeTu = new ArrayList<>();
            while (rsDeTu.next()) {
                int dtId = rsDeTu.getInt("id");
                String it_body = rsDeTu.getString("it_body");
                counter[0] = 0;
                String newItBody = cleanThanTrangJsonArray(it_body, counter);
                if (counter[0] > 0) {
                    totalDeleted += counter[0];
                    updateDeTu.add(new AbstractMap.SimpleEntry<>(dtId, newItBody));
                }
            }
            if (!updateDeTu.isEmpty()) {
                try (PreparedStatement psUpDt = conn.prepareStatement("UPDATE `players_detu` SET `it_body` = ? WHERE `id` = ?")) {
                    for (Map.Entry<Integer, String> entry : updateDeTu) {
                        psUpDt.setString(1, entry.getValue());
                        psUpDt.setInt(2, entry.getKey());
                        psUpDt.addBatch();
                    }
                    psUpDt.executeBatch();
                }
            }
        } catch (Exception ignored) {}

        // 4. Quét dọn dẹp thêm trong players_bot
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement psBot = conn.prepareStatement("SELECT `id`, `it_body` FROM `players_bot`");
             ResultSet rsBot = psBot.executeQuery()) {
            int[] counter = new int[1];
            List<Map.Entry<Integer, String>> updateBot = new ArrayList<>();
            while (rsBot.next()) {
                int botId = rsBot.getInt("id");
                String it_body = rsBot.getString("it_body");
                counter[0] = 0;
                String newItBody = cleanThanTrangJsonArray(it_body, counter);
                if (counter[0] > 0) {
                    totalDeleted += counter[0];
                    updateBot.add(new AbstractMap.SimpleEntry<>(botId, newItBody));
                }
            }
            if (!updateBot.isEmpty()) {
                try (PreparedStatement psUpBot = conn.prepareStatement("UPDATE `players_bot` SET `it_body` = ? WHERE `id` = ?")) {
                    for (Map.Entry<Integer, String> entry : updateBot) {
                        psUpBot.setString(1, entry.getValue());
                        psUpBot.setInt(2, entry.getKey());
                        psUpBot.addBatch();
                    }
                    psUpBot.executeBatch();
                }
            }
        } catch (Exception ignored) {}

        return totalDeleted;
    }

    /**
     * Xóa Thần Trang của 1 người chơi theo tên (xử lý cả Online và Offline DB).
     */
    public static void cleanThanTrangByPlayerName(Player admin, String targetName) {
        if (targetName == null || targetName.isBlank()) return;

        // 1. Nếu người chơi Online
        Player targetOnline = getOnlinePlayerByName(targetName);
        if (targetOnline != null) {
            int count = removeThanTrangPlayer(targetOnline);
            try {
                targetOnline.getService().send_box_ThongBao_OK(" Admin đã xóa toàn bộ Thần Trang của bạn!");
            } catch (Exception ignored) {}
            if (admin != null) {
                try {
                    admin.getService().send_box_ThongBao_OK("Đã xóa " + count + " món Thần Trang của người chơi Online [" + targetOnline.name + "]!");
                } catch (Exception ignored) {}
            }
            return;
        }

        // 2. Nếu người chơi Offline: Quét và cập nhật trực tiếp Database
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement psSelect = conn.prepareStatement("SELECT `id`, `name`, `bag3`, `it_body`, `box3`, `save_it3` FROM `players` WHERE `name` = ?")) {
            psSelect.setString(1, targetName);
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    int pId = rs.getInt("id");
                    int[] count = new int[1];
                    String newBag3 = cleanThanTrangJsonArray(rs.getString("bag3"), count);
                    String newItBody = cleanThanTrangJsonArray(rs.getString("it_body"), count);
                    String newBox3 = cleanThanTrangJsonArray(rs.getString("box3"), count);
                    String newSaveIt3 = cleanThanTrangJsonArray(rs.getString("save_it3"), count);

                    if (count[0] > 0) {
                        try (PreparedStatement psUpdate = conn.prepareStatement(
                                "UPDATE `players` SET `bag3` = ?, `it_body` = ?, `box3` = ?, `save_it3` = ? WHERE `id` = ?")) {
                            psUpdate.setString(1, newBag3);
                            psUpdate.setString(2, newItBody);
                            psUpdate.setString(3, newBox3);
                            psUpdate.setString(4, newSaveIt3);
                            psUpdate.setInt(5, pId);
                            psUpdate.executeUpdate();
                        }
                    }
                    if (admin != null) {
                        admin.getService().send_box_ThongBao_OK("Đã xóa " + count[0] + " món Thần Trang của nhân vật Offline [" + targetName + "] trong Database!");
                    }
                } else {
                    if (admin != null) {
                        admin.getService().send_box_ThongBao_OK("Không tìm thấy nhân vật [" + targetName + "] trong Cơ Sở Dữ Liệu!");
                    }
                }
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "cleanThanTrangByPlayerName error: " + e.getMessage());
            if (admin != null) {
                try {
                    admin.getService().send_box_ThongBao_OK("Lỗi khi xử lý xóa Thần Trang cho [" + targetName + "]: " + e.getMessage());
                } catch (Exception ignored) {}
            }
        }
    }

    // ==============================================================
    //  HỆ THỐNG MẶC ĐÈ FULL SET TRANG BỊ
    // ==============================================================

    public static void openFullSetRootMenu(Player p) throws IOException {
        int curLevel = playerSetLevel.getOrDefault(p.IDPlayer, 100);
        int curBuild = playerSetBuild.getOrDefault(p.IDPlayer, 0);
        int curGem = playerChosenGem.getOrDefault(p.IDPlayer, 647);
        int curScope = playerSetScope.getOrDefault(p.IDPlayer, 1);

        String scopeName = (curScope == 0 ? "Ô 0-5 (Vũ Khí -> Giày)" : (curScope == 1 ? "Ô 0-7 (Kèm Tim & Dial)" : "Ô 0-13 (Kèm 6 Thần Trang)"));

        iMenuDymanic.buildAndSend(p, MENU_FULL_SET_ROOT,
            "CẤU HÌNH FULL SET THEO CẤP ĐỘ",
            new String[]{
                "1. Cấp Độ Đồ: Lv." + curLevel + " (Bấm Để Chọn 1, 10..110)",
                "2. Phong Cách Build: " + getBuildName(curBuild, curGem) + " (Bấm Để Đổi)",
                "3. Chọn Loại Đá Khảm Cụ Thể (12 Loại Đá Cực Phẩm)",
                "4. Tắt Khảm Đá (Không Khảm / Trống Lỗ)",
                "5. Phạm Vi Mặc: " + scopeName + " (Bấm Để Đổi)",
                "6. Mặc Đè Trực Tiếp Cho Bản Thân",
                "7. Mặc Đè Trực Tiếp Cho Đệ Tử",
                "8. Nhận Toàn Bộ Set Này Vào Hành Trang",
                "9. Khôi Phục Trang Bị Cũ Đã Sao Lưu Gần Nhất",
                "Quay Lại"
            },
            new short[]{110, 133, 133, 118, 155, 110, 155, 110, 116, 134}
        );
    }

    private void handleFullSetRoot(Player p, int index) throws IOException {
        int curScope = playerSetScope.getOrDefault(p.IDPlayer, 1);
        switch (index) {
            case 0:
                openFullSetLevelMenu(p);
                break;
            case 1:
                openFullSetBuildMenu(p);
                break;
            case 2:
                openFullSetGemTypeMenu(p);
                break;
            case 3:
                playerSetBuild.put(p.IDPlayer, -1);
                p.getService().send_box_ThongBao_OK("Đã chọn cấu hình: Tắt Khảm Đá (Không Khảm / Trống Lỗ)!");
                openFullSetRootMenu(p);
                break;
            case 4:
                playerSetScope.put(p.IDPlayer, (curScope + 1) % 3);
                openFullSetRootMenu(p);
                break;
            case 5:
                openFullSetClazzMenu(p, false);
                break;
            case 6:
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
                } else {
                    openFullSetClazzMenu(p, true);
                }
                break;
            case 7:
                giveFullSetToBag(p);
                break;
            case 8:
                restorePlayerItBody(p);
                break;
            case 9:
                openItem872Menu(p);
                break;
        }
    }

    public static void openFullSetLevelMenu(Player p) throws IOException {
        String[] items = new String[EQUIP_LEVELS.length + 1];
        short[] icons = new short[items.length];
        for (int i = 0; i < EQUIP_LEVELS.length; i++) {
            items[i] = "Trang Bị Cấp Độ Lv." + EQUIP_LEVELS[i];
            icons[i] = (short) (EQUIP_LEVELS[i] >= 100 ? 155 : 110);
        }
        items[EQUIP_LEVELS.length] = "Quay Lại";
        icons[EQUIP_LEVELS.length] = 134;

        iMenuDymanic.buildAndSend(p, MENU_FULL_SET_LEVEL,
            "CHỌN CẤP ĐỘ TRANG BỊ (1 -> 110)",
            items, icons
        );
    }

    private void handleFullSetLevel(Player p, int index) throws IOException {
        if (index >= 0 && index < EQUIP_LEVELS.length) {
            playerSetLevel.put(p.IDPlayer, EQUIP_LEVELS[index]);
            p.getService().send_box_ThongBao_OK("Đã chọn Set Trang Bị Cấp Độ: Level " + EQUIP_LEVELS[index]);
        }
        openFullSetRootMenu(p);
    }

    public static void openFullSetBuildMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_FULL_SET_BUILD,
            "CHỌN PHONG CÁCH BUILD TRANG BỊ",
            new String[]{
                "1. Sát Thương Bộc Phá: Tối Đa Công, Bạo Kích, Xuyên Giáp",
                "2. Chống Chịu Bất Tử: Siêu Trâu, Tối Đa Máu, Kháng Sát Thương",
                "3. Hỗ Trợ Toàn Năng: Tối Đa Hồi Phục, Tốc Độ, Giảm Hồi Chiêu",
                "4. Cân Bằng Hoàn Hảo: Toàn Diện Công Thủ Và Bạo Kích",
                "Quay Lại"
            },
            new short[]{133, 155, 127, 110, 134}
        );
    }

    private void handleFullSetBuild(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                playerSetBuild.put(p.IDPlayer, 0);
                openFullSetRootMenu(p);
                break;
            case 1:
                playerSetBuild.put(p.IDPlayer, 1);
                openFullSetRootMenu(p);
                break;
            case 2:
                playerSetBuild.put(p.IDPlayer, 2);
                openFullSetRootMenu(p);
                break;
            case 3:
                playerSetBuild.put(p.IDPlayer, 4);
                openFullSetRootMenu(p);
                break;
            case 4:
                openFullSetRootMenu(p);
                break;
        }
    }

    public static void openFullSetGemTypeMenu(Player p) throws IOException {
        String[] items = new String[GEM_PRESET_NAMES.length + 1];
        short[] icons = new short[items.length];
        for (int i = 0; i < GEM_PRESET_NAMES.length; i++) {
            items[i] = GEM_PRESET_NAMES[i];
            icons[i] = (short) (i >= 8 ? 133 : 110);
        }
        items[GEM_PRESET_NAMES.length] = "Quay Lại";
        icons[GEM_PRESET_NAMES.length] = 134;

        iMenuDymanic.buildAndSend(p, MENU_FULL_SET_GEM_TYPE,
            "CHỌN LOẠI ĐÁ KHẢM CHO FULL SET",
            items, icons
        );
    }

    private void handleFullSetGemType(Player p, int index) throws IOException {
        if (index >= 0 && index < GEM_PRESET_IDS.length) {
            playerSetBuild.put(p.IDPlayer, 3); // Custom gem
            playerChosenGem.put(p.IDPlayer, GEM_PRESET_IDS[index]);
            p.getService().send_box_ThongBao_OK("Đã chọn khảm 6 lỗ bằng loại:\n" + GEM_PRESET_NAMES[index]);
        }
        openFullSetRootMenu(p);
    }

    public static void openFullSetClazzMenu(Player p, boolean isDetu) throws IOException {
        tempOnlineSelectIsDetu.put(p.IDPlayer, isDetu);
        iMenuDymanic.buildAndSend(p, MENU_FULL_SET_CLAZZ,
            "CHỌN HỆ PHÁI MẶC CHO " + (isDetu ? "ĐỆ TỬ" : "BẢN THÂN"),
            new String[]{
                "Theo Môn Phái Hiện Tại Của " + (isDetu ? "Đệ Tử" : "Bản Thân"),
                "Kiếm Sĩ: Hệ Phái Kiếm Chém Cận Chiến",
                "Xạ Thủ: Hệ Phái Bắn Tỉa Tầm Xa",
                "Chiến Binh: Hệ Phái Quyền Thuật Dũng Mãnh",
                "Hoa Tiêu: Hệ Phái Pháp Thuật Gậy Phép",
                "Đầu Bếp: Hệ Phái Cước Thuật Song Súng Hỗ Trợ",
                "Quay Lại"
            },
            new short[]{155, 110, 110, 110, 110, 110, 134}
        );
    }

    private void handleFullSetClazz(Player p, int index) throws IOException {
        if (index == 6) {
            openFullSetRootMenu(p);
            return;
        }

        boolean isDetu = tempOnlineSelectIsDetu.getOrDefault(p.IDPlayer, false);
        Player target = isDetu ? p.detu : p;
        if (target == null) {
            p.getService().send_box_ThongBao_OK("Mục tiêu không tồn tại!");
            openFullSetRootMenu(p);
            return;
        }

        int targetClazz = (index == 0) ? ((target.clazz >= 1 && target.clazz <= 5) ? target.clazz : 1) : index;
        int scope = playerSetScope.getOrDefault(p.IDPlayer, 1);
        int build = playerSetBuild.getOrDefault(p.IDPlayer, 0);
        int gemId = playerChosenGem.getOrDefault(p.IDPlayer, 647);
        int level = playerSetLevel.getOrDefault(p.IDPlayer, 100);

        executeEquipFullSet(p, target, scope, build, gemId, level, targetClazz);
    }

    public static void executeEquipFullSet(Player p, int scope, int buildType, int clazz) {
        int level = playerSetLevel.getOrDefault(p.IDPlayer, 100);
        int gemId = playerChosenGem.getOrDefault(p.IDPlayer, 647);
        executeEquipFullSet(p, p, scope, buildType, gemId, level, clazz);
    }

    public static void executeEquipFullSet(Player admin, Player target, int scope, int buildType, int chosenGem, int level, int clazz) {
        if (admin == null || target == null || target.item == null) return;
        try {
            backupPlayerItBody(target);

            if (target.item.it_body == null || target.item.it_body.length < itemz.Item.MAX_BODY) {
                Item_wear[] newBody = new Item_wear[itemz.Item.MAX_BODY];
                if (target.item.it_body != null) {
                    System.arraycopy(target.item.it_body, 0, newBody, 0, Math.min(target.item.it_body.length, newBody.length));
                }
                target.item.it_body = newBody;
            }

            // Slots 0 đến 5 theo level và class
            for (int slot = 0; slot <= 5; slot++) {
                int tId = findBestEquipByLevel(level, clazz, slot);
                Item_wear it = createFullSetItem(tId, slot, buildType, 20, 8, chosenGem);
                target.item.it_body[slot] = it;
            }

            // Scope >= 1: Kèm Quả Tim (ô 6) và Dial (ô 7)
            if (scope >= 1) {
                Item_wear heart = createFullSetItem(ITEM_ID_HEART, 6, buildType, 20, 8, chosenGem);
                if (heart != null) {
                    heart.isHoanMy = 0;
                    heart.valueKichAn = -1;
                    heart.typelock = 1;
                    target.item.it_body[6] = heart;
                    target.item.it_heart = heart;
                }

                Item_wear dial = createFullSetItem(ITEM_ID_DIAL, 7, buildType, 20, 8, chosenGem);
                if (dial != null) {
                    dial.isHoanMy = 0;
                    dial.valueKichAn = -1;
                    dial.typelock = 1;
                    target.item.it_body[7] = dial;
                }
            }

            // Scope >= 2: Kèm đủ 6 món Thần Trang (ô 8 đến 13)
            if (scope >= 2) {
                for (int slot = 8; slot <= 13; slot++) {
                    int ttId = ITEM_IDS_THAN_TRANG[slot - 8];
                    Item_wear tt = createFullSetItem(ttId, slot, buildType, 20, 8, chosenGem);
                    target.item.it_body[slot] = tt;
                }
            }

            target.updateParts();
            if (target.ability != null) {
                target.ability.recalculatePlayerStats(target);
                target.hp = target.ability.get_hp_max(true);
                target.mp = target.ability.get_mp_max(true);
            }
            target.item.updateInventory(false);
            target.update_info_to_all();
            if (target.getService() != null) {
                target.getService().Main_char_Info(true);
                target.getService().charWearing(target, false);
                target.getService().UpdatePvpPoint();
            }
            target.flush(target, false);

            String buildName = getBuildName(buildType, chosenGem);
            String scopeName = scope == 0 ? "Ô 0 Đến 5" : scope == 1 ? "Ô 0 Đến 7 (Kèm Tim/Dial)" : "Ô 0 Đến 13 (Kèm Thần Trang)";

            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK(
                    "ĐÃ MẶC ĐÈ FULL SET TRANG BỊ CHO " + (target == admin ? "BẢN THÂN" : "ĐỆ TỬ") + "\n" +
                    "- Cấp Độ Đồ: Level " + level + " (" + getClassName(clazz) + ")\n" +
                    "- Phong Cách Build: " + buildName + "\n" +
                    "- Phạm Vi Mặc: " + scopeName + "\n" +
                    "- Cấp Cường Hóa: +20, Phẩm Chất Hoàn Mỹ\n" +
                    "- Đã Tự Động Sao Lưu Toàn Bộ Trang Bị Cũ Trước Khi Mặc!"
                );
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "executeEquipFullSet error: " + e.getMessage());
            try {
                if (admin.getService() != null) {
                    admin.getService().send_box_ThongBao_OK("Lỗi mặc đè trang bị: " + e.getMessage());
                }
            } catch (Exception ignored) {}
        }
    }

    public static int findBestEquipByLevel(int targetLevel, int clazz, int slot) {
        int cIdx = Math.max(0, Math.min(4, clazz - 1));
        int defaultId = CLASS_EQUIP_TEMPLATES[cIdx][slot];
        if (ItemTemplate3.ENTRYS == null || ItemTemplate3.ENTRYS.isEmpty()) {
            return defaultId;
        }
        ItemTemplate3 best = null;
        int bestScore = Integer.MAX_VALUE;
        for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
            if (t == null) continue;
            if (t.typeEquip != slot) continue;
            if (slot == 2 || slot == 4) {
                if (t.clazz != 0 && t.clazz != clazz) continue;
            } else {
                if (t.clazz != clazz && t.clazz != 0) continue;
            }
            if (t.isThanTrang()) continue;

            int levelDiff = Math.abs(t.level - targetLevel);
            int colorScore = 10 - t.color;
            int score = levelDiff * 100 + colorScore;
            if (score < bestScore) {
                bestScore = score;
                best = t;
            }
        }
        return (best != null) ? best.id : defaultId;
    }

    public static void giveFullSetToBag(Player p) {
        if (p == null || p.item == null) return;
        int level = playerSetLevel.getOrDefault(p.IDPlayer, 100);
        int build = playerSetBuild.getOrDefault(p.IDPlayer, 0);
        int gemId = playerChosenGem.getOrDefault(p.IDPlayer, 647);
        int clazz = (p.clazz >= 1 && p.clazz <= 5) ? p.clazz : 1;
        int scope = playerSetScope.getOrDefault(p.IDPlayer, 1);

        for (int slot = 0; slot <= 5; slot++) {
            int tId = findBestEquipByLevel(level, clazz, slot);
            Item_wear it = createFullSetItem(tId, slot, build, 20, 8, gemId);
            if (it != null) p.item.add_item_bag3(it);
        }

        if (scope >= 1) {
            Item_wear heart = createFullSetItem(ITEM_ID_HEART, 6, build, 20, 8, gemId);
            if (heart != null) p.item.add_item_bag3(heart);
            Item_wear dial = createFullSetItem(ITEM_ID_DIAL, 7, build, 20, 8, gemId);
            if (dial != null) p.item.add_item_bag3(dial);
        }

        if (scope >= 2) {
            for (int slot = 8; slot <= 13; slot++) {
                int ttId = ITEM_IDS_THAN_TRANG[slot - 8];
                Item_wear tt = createFullSetItem(ttId, slot, build, 20, 8, gemId);
                if (tt != null) p.item.add_item_bag3(tt);
            }
        }

        try {
            p.item.updateInventory(false);
            p.getService().send_box_ThongBao_OK("Đã nhận bộ trang bị Level " + level + " (" + getBuildName(build, gemId) + ") vào hành trang thành công!");
        } catch (Exception ignored) {}
    }

    public static String getBuildName(int buildType, int customGemId) {
        switch (buildType) {
            case -1: return "Tắt Khảm Đá (Không Khảm / Trống Lỗ)";
            case 0: return "Sát Thương Bộc Phá (Công & Bạo)";
            case 1: return "Chống Chịu Bất Tử (Thủ & Máu)";
            case 2: return "Hỗ Trợ Toàn Năng (Hồi Phục & Tốc)";
            case 3: return "Khảm: " + getGemNameById(customGemId);
            case 4: return "Cân Bằng Hoàn Hảo (Công Thủ Toàn Diện)";
            default: return "Sát Thương Bộc Phá";
        }
    }

    public static String getGemNameById(int gemId) {
        for (int i = 0; i < GEM_PRESET_IDS.length; i++) {
            if (GEM_PRESET_IDS[i] == gemId) {
                return GEM_PRESET_NAMES[i];
            }
        }
        return "Đá ID " + gemId;
    }

    private static Item_wear createFullSetItem(int templateId, int slot, int buildType, int enhanceLv, int color) {
        return createFullSetItem(templateId, slot, buildType, enhanceLv, color, 647);
    }

    private static Item_wear createFullSetItem(int templateId, int slot, int buildType, int enhanceLv, int color, int chosenGem) {
        ItemTemplate3 temp = ItemTemplate3.get_it_by_id(templateId);
        if (temp == null) return null;
        Item_wear it = new Item_wear();
        it.template = temp;
        it.index = (short) slot;
        it.levelUp = (byte) Math.min(20, Math.max(0, enhanceLv));
        it.setColor((byte) color);
        it.color = (byte) color;
        it.typelock = 1;
        it.timeUse = 0;
        it.valueChetac = 100;
        it.isHoanMy = (byte) (slot >= 6 ? 0 : 100);
        it.valueKichAn = (byte) (slot >= 6 ? -1 : (buildType == 1 ? 1 : (buildType == 2 ? 7 : 4)));

        it.option_item = new ArrayList<>();
        it.option_item_2 = new ArrayList<>();

        if (buildType == -1) {
            // Tắt khảm đá: không lỗ, không đá
            it.numLoKham = 0;
            it.numHoleDaDuc = 0;
            it.mdakham = new short[0];
        } else {
            it.numLoKham = 6;
            it.numHoleDaDuc = 6;
            int[] gems;
            if (buildType == 3) {
                gems = new int[]{chosenGem, chosenGem, chosenGem, chosenGem, chosenGem, chosenGem};
            } else if (buildType == 1) {
                gems = new int[]{652, 662, 326, 652, 662, 326};
            } else if (buildType == 2) {
                gems = new int[]{647, 326, 652, 647, 326, 652};
            } else if (buildType == 4) {
                gems = new int[]{657, 652, 326, 910, 662, 647};
            } else {
                gems = new int[]{657, 910, 326, 657, 910, 326};
            }
            it.mdakham = new short[gems.length];
            for (int i = 0; i < gems.length; i++) {
                it.mdakham[i] = (short) gems[i];
            }
            for (int gid : gems) {
                Rebuild_Item.add_op_ngoc_kham_new(it, (short) gid);
            }
        }

        if (buildType == 1) { // Tank
            it.option_item.add(new Option(3, 180));
            it.option_item.add(new Option(4, 280));
            it.option_item.add(new Option(15, 60000));
            it.option_item.add(new Option(17, 55));
            it.option_item.add(new Option(26, 50));
            it.option_item.add(new Option(27, 50));

            it.option_item_2.add(new Option(53, 35));
            it.option_item_2.add(new Option(56, 40));
            it.option_item_2.add(new Option(49, 35));
            it.option_item_2.add(new Option(50, 30));
            it.option_item_2.add(new Option(52, 30));
            it.option_item_2.add(new Option(71, 35));
            it.option_item_2.add(new Option(47, 80));
            it.option_item_2.add(new Option(79, 30));
        } else if (buildType == 2) { // SP
            it.option_item.add(new Option(12, 40));
            it.option_item.add(new Option(19, 8000));
            it.option_item.add(new Option(25, 40));
            it.option_item.add(new Option(15, 45000));
            it.option_item.add(new Option(17, 40));
            it.option_item.add(new Option(1, 300));

            it.option_item_2.add(new Option(47, 95));
            it.option_item_2.add(new Option(58, 35));
            it.option_item_2.add(new Option(71, 40));
            it.option_item_2.add(new Option(73, 35));
            it.option_item_2.add(new Option(79, 40));
            it.option_item_2.add(new Option(26, 35));
            it.option_item_2.add(new Option(27, 35));
        } else if (buildType == 4) { // Balance
            it.option_item.add(new Option(1, 500));
            it.option_item.add(new Option(4, 200));
            it.option_item.add(new Option(10, 25));
            it.option_item.add(new Option(15, 40000));
            it.option_item.add(new Option(13, 80));
            it.option_item.add(new Option(17, 35));

            it.option_item_2.add(new Option(46, 30));
            it.option_item_2.add(new Option(53, 25));
            it.option_item_2.add(new Option(47, 85));
            it.option_item_2.add(new Option(71, 30));
            it.option_item_2.add(new Option(26, 30));
            it.option_item_2.add(new Option(27, 30));
            it.option_item_2.add(new Option(57, 20));
        } else { // Dame & Custom
            it.option_item.add(new Option(1, 650));
            it.option_item.add(new Option(10, 35));
            it.option_item.add(new Option(11, 60));
            it.option_item.add(new Option(13, 120));
            it.option_item.add(new Option(46, 45));
            it.option_item.add(new Option(57, 30));

            it.option_item_2.add(new Option(1, 250));
            it.option_item_2.add(new Option(47, 90));
            it.option_item_2.add(new Option(10, 25));
            it.option_item_2.add(new Option(13, 110));
            it.option_item_2.add(new Option(48, 25));
            it.option_item_2.add(new Option(70, 35));
            it.option_item_2.add(new Option(51, 20));
            it.option_item_2.add(new Option(75, 25));
            it.option_item_2.add(new Option(76, 30));
        }
        return it;
    }

    public static void backupPlayerItBody(Player p) {
        if (p == null || p.item == null || p.item.it_body == null) return;
        try {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = (i == 6 && p.item.it_heart != null) ? p.item.it_heart : p.item.it_body[i];
                if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                    JSONObject obj = Item.it_data_to_json(it);
                    obj.put("idx", i);
                    arr.add(obj);
                }
            }
            String backupJson = arr.toJSONString();
            playerItBodyBackup.put(p.IDPlayer, backupJson);

            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO `historys` (`type`, `player_id`, `content`, `time`) VALUES (?, ?, ?, NOW())")) {
                ps.setString(1, "BACKUP_IT_BODY");
                ps.setInt(2, p.IDPlayer);
                ps.setString(3, backupJson);
                ps.executeUpdate();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "backupPlayerItBody error: " + e.getMessage());
        }
    }

    public static void restorePlayerItBody(Player p) {
        if (p == null || p.item == null) return;
        try {
            String backupJson = playerItBodyBackup.get(p.IDPlayer);
            if (backupJson == null || backupJson.isEmpty()) {
                try (Connection conn = DbManager.gI().getConnect();
                     PreparedStatement ps = conn.prepareStatement(
                         "SELECT `content` FROM `historys` WHERE `type` = 'BACKUP_IT_BODY' AND `player_id` = ? ORDER BY `id` DESC LIMIT 1")) {
                    ps.setInt(1, p.IDPlayer);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            backupJson = rs.getString("content");
                        }
                    }
                } catch (Exception ignored) {}
            }

            if (backupJson == null || backupJson.isEmpty()) {
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy bản sao lưu trang bị nào của bạn!");
                }
                return;
            }

            Object parsed = JSONValue.parse(backupJson);
            if (parsed instanceof JSONArray) {
                JSONArray arr = (JSONArray) parsed;
                if (p.item.it_body == null || p.item.it_body.length < itemz.Item.MAX_BODY) {
                    p.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
                }
                Arrays.fill(p.item.it_body, null);
                p.item.it_heart = null;

                for (Object o : arr) {
                    Item_wear it = new Item_wear();
                    Item.readUpdateItem(o.toString(), it);
                    if (it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                        if (it.index < p.item.it_body.length) {
                            if (it.index == 6) {
                                p.item.it_heart = it;
                                p.item.it_heart.typelock = 1;
                            }
                            p.item.it_body[it.index] = it;
                        }
                    }
                }

                p.updateParts();
                if (p.ability != null) {
                    p.ability.recalculatePlayerStats(p);
                    p.hp = p.ability.get_hp_max(true);
                    p.mp = p.ability.get_mp_max(true);
                }
                p.item.updateInventory(false);
                p.update_info_to_all();
                if (p.getService() != null) {
                    p.getService().Main_char_Info(true);
                    p.getService().charWearing(p, false);
                    p.getService().UpdatePvpPoint();
                    p.getService().send_box_ThongBao_OK("Đã khôi phục thành công toàn bộ trang bị cũ từ bản sao lưu!");
                }
                p.flush(p, false);
            }
        } catch (Exception e) {
            Log.error("AdminSystemMenu", "restorePlayerItBody error: " + e.getMessage());
            try {
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Lỗi khi khôi phục trang bị: " + e.getMessage());
                }
            } catch (Exception ignored) {}
        }
    }

    // ==============================================================
    //  HỆ THỐNG TRIỆU HỒI BOSS VÀ ĐỆ HOANG TẠI TỌA ĐỘ HIỆN TẠI
    // ==============================================================

    public void openSpawnHereMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_SPAWN_HERE_ROOT,
            "TRIỆU HỒI TẠI VỊ TRÍ ĐANG ĐỨNG",
            new String[]{
                "Triệu Hồi Toàn Bộ 10 Siêu Trùm Cùng Lúc Tại Đây",
                "Chọn Từng Siêu Trùm Để Triệu Hồi Tại Đây",
                "Triệu Hồi Đệ Tử Hoang Tại Vị Trí Đang Đứng",
                "Triệu Hồi Boss Thế Giới Hải Tặc Tại Đây",
                "Triệu Hồi Boss Pica Khổng Lồ Tại Đây",
                "Nhập Mã Mob ID Để Triệu Hồi Boss Bất Kỳ Tại Đây",
                "Quay Lại"
            },
            new short[]{133, 155, 110, 136, 136, 116, 134}
        );
    }

    private void handleSpawnHereRoot(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                List<AbsBoss> list = BossAndWildManager.gI().spawnAll10SuperBossesAtZone(p.map, p.x, p.y, true);
                p.getService().send_box_ThongBao_OK("Đã triệu hồi thành công " + list.size() + " Siêu Trùm tại tọa độ hiện tại!");
                break;
            }
            case 1: {
                openSpawnHereSuperBossMenu(p);
                break;
            }
            case 2: {
                openSpawnHereDetuMenu(p);
                break;
            }
            case 3: {
                AbsBoss boss = BossAndWildManager.gI().spawnNormalBossAtCoords(172, p.map, p.x, p.y, true);
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Thế Giới Hải Tặc tại vị trí đang đứng!");
                break;
            }
            case 4: {
                AbsBoss boss = BossAndWildManager.gI().spawnNormalBossAtCoords(173, p.map, p.x, p.y, true);
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Pica Khổng Lồ tại vị trí đang đứng!");
                break;
            }
            case 5: {
                p.sendInput("Nhập Mob ID Boss cần triệu hồi:", new String[]{"Mob ID"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        try {
                            int mobId = Integer.parseInt(inputs[0].trim());
                            AbsBoss b = BossAndWildManager.gI().spawnNormalBossAtCoords(mobId, p.map, p.x, p.y, true);
                            if (b != null) {
                                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Mob ID " + mobId + " tại vị trí đang đứng!");
                            } else {
                                p.getService().send_box_ThongBao_OK("Không thể khởi tạo Boss với Mob ID " + mobId);
                            }
                        } catch (Exception ex) {
                            try { p.getService().send_box_ThongBao_OK("Mã Mob ID không hợp lệ: " + ex.getMessage()); } catch (Exception ignored) {}
                        }
                    }
                });
                break;
            }
            case 6: {
                openItem872Menu(p);
                break;
            }
        }
    }

    public void openSpawnHereSuperBossMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_SPAWN_HERE_SUPER_BOSS,
            "CHỌN SIÊU TRÙM TRIỆU HỒI TẠI ĐÂY",
            new String[]{
                "Siêu Trùm Buggy Hề: Cấp 30",
                "Siêu Trùm Kuro Thuyền Trưởng: Cấp 40",
                "Siêu Trùm Arlong Người Cá: Cấp 50",
                "Siêu Trùm Smoker Thợ Săn Trắng: Cấp 60",
                "Siêu Trùm Wapol Bạo Chúa: Cấp 70",
                "Siêu Trùm Mr3 Sáp Nến: Cấp 80",
                "Siêu Trùm Crocodile Sa Mạc: Cấp 90",
                "Siêu Trùm Enel Lôi Thần: Cấp 100",
                "Siêu Trùm Ryuma Kiếm Hào Zombie: Cấp 110",
                "Siêu Trùm Rob Lucci Báo Đốm: Cấp 120",
                "Quay Lại"
            },
            new short[]{133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 134}
        );
    }

    private void handleSpawnHereSuperBoss(Player p, int index) throws IOException {
        if (index >= 0 && index < SuperBossManager.ID_BOSS.length) {
            short bMobId = SuperBossManager.ID_BOSS[index];
            AbsBoss boss = BossAndWildManager.gI().spawnSuperBossAtCoords(bMobId, p.map, p.x, p.y, true);
            if (boss != null) {
                String bName = (boss.mtemplate != null && boss.mtemplate.name != null) ? boss.mtemplate.name : ("Siêu Trùm " + bMobId);
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Siêu Trùm " + bName + " tại vị trí đang đứng!");
            } else {
                p.getService().send_box_ThongBao_OK("Không thể triệu hồi Siêu Trùm Mob " + bMobId);
            }
        } else {
            openSpawnHereMenu(p);
        }
    }

    public void openSpawnHereDetuMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_SPAWN_HERE_DETU,
            "TRIỆU HỒI ĐỆ TỬ HOANG TẠI ĐÂY",
            new String[]{
                "Đệ Tử Hoang Ngẫu Nhiên 5 Hệ Phái",
                "Đệ Tử Hoang Kiếm Sĩ Cận Chiến",
                "Đệ Tử Hoang Xạ Thủ Tầm Xa",
                "Đệ Tử Hoang Chiến Binh Quyền Thuật",
                "Đệ Tử Hoang Hoa Tiêu Pháp Thuật",
                "Đệ Tử Hoang Đầu Bếp Hỗ Trợ",
                "Quay Lại"
            },
            new short[]{155, 110, 110, 110, 110, 110, 134}
        );
    }

    private void handleSpawnHereDetu(Player p, int index) throws IOException {
        if (index == 0) {
            List<DeTu> list = BossAndWildManager.gI().spawnWildDeTuAtCoords(p.map, p.x, p.y, null, 1, true);
            p.getService().send_box_ThongBao_OK("Đã triệu hồi Đệ Tử Hoang ngẫu nhiên tại vị trí đang đứng!");
        } else if (index >= 1 && index <= 5) {
            List<DeTu> list = BossAndWildManager.gI().spawnWildDeTuAtCoords(p.map, p.x, p.y, index, 1, true);
            p.getService().send_box_ThongBao_OK("Đã triệu hồi Đệ Tử Hoang môn phái thành công tại vị trí đang đứng!");
        } else {
            openSpawnHereMenu(p);
        }
    }
}
