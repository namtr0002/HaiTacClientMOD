package zabstracts;

import model.Player;
import mob.Mob;
import java.io.IOException;
import java.util.List;
import map.*;
import network.Message;

public abstract class AbsService {
    protected String key;

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : "Service";
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return baseKey;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    // ======================================================
    //  CORE MESSAGE
    // ======================================================

    public abstract void sendMessage(Message m);

    public abstract void send_msg_data(int cmd, String path, boolean save_cache) throws IOException;

    // ======================================================
    //  NHÂN VẬT / CHARACTER
    // ======================================================

    public abstract void UpdateInfoMaincharInfo() throws IOException;
    public abstract void Main_char_Info() throws IOException;
    public abstract void UpdatePvpPoint() throws IOException;
    public abstract void update_PK(Player p, boolean save_cache) throws IOException;
    public abstract void getThanhTich(Player p) throws IOException;
    public abstract void Weapon_fashion(Player p, boolean save_cache) throws IOException;
    public abstract void charWearing(Player p, boolean save_cache) throws IOException;
    public abstract void login_ok(boolean save_cache) throws IOException;
    public void sendSudoInfo(boolean save_cache) throws IOException {}
    public void sendSudoClear(Player target) throws IOException {}
    public void sendSudoMemberList() throws IOException {}

    // ======================================================
    //  SHOP / ITEM
    // ======================================================

    public abstract void Send_UI_Shop(int type) throws IOException;
    public abstract void send_obj_template(Message m) throws IOException;
    public abstract void request_mob_in4(Message m2) throws IOException;
    public abstract void send_mob_info(Mob temp) throws IOException;
    public abstract void rms_process(Message m2);
    public abstract void buy_item(Message m2) throws IOException;
    public abstract void sell_item(Message m2) throws IOException;
    public abstract void request_item4_info(Message m2) throws IOException;

    // ======================================================
    //  MAP / BATTLE
    // ======================================================

    public abstract void area_select(Message m2) throws IOException;
    public abstract void checkPlayInMap(Message m2);
    public abstract void use_potion(int healHp, int healMp) throws IOException;
    public abstract void send_view_other_player(Player p) throws IOException;

    // ======================================================
    //  UI HELPERS
    // ======================================================

    public abstract void send_box_ThongBao_OK(String notice);
    public abstract void sendThongBao(String notice);
    public abstract void sendThongBao(String notice, int color);
    public abstract void send_hp_map(int hp, int hpMax) throws IOException;
    public abstract void CountDown_Ticket() throws IOException;
    public abstract void NewDialog_eat_taq(String[] name_, int[] icon_, int id) throws IOException;
    public abstract void Help_From_Server(int num, String text) throws IOException;
    public abstract void send_eff(int b, int num) throws IOException;
    public abstract void send_eff_sword_splash(int id) throws IOException;
    public abstract void send_time_cool_down(long t, String title, int type) throws IOException;
    public abstract void DonotAutoReconnect() throws IOException;

    // ======================================================
    //  GAME FEATURES
    // ======================================================

    public abstract void pet(Player p, boolean save_cache) throws IOException;
    public abstract void ChestWanted(boolean save_cache) throws IOException;
    public abstract void Wanted(boolean save_cache) throws IOException;
    public abstract void start_combo(int type) throws IOException;

    // ======================================================
    //  DIALOG FLOWS
    // ======================================================

    public abstract void sendConfirmYesNo(short yesNoType);
    public abstract void sendUpgradeGear(short itemIndex);
    public abstract void sendQuestProcess(short questId, byte status);
    public abstract void sendPartyAccept(short inviterId);
    public abstract void sendPartyInvite(short indexMap);
    public abstract void sendTableTickOption(short idDialog);
    public abstract void sendMarketEarningsClaim(int index);
    public abstract void sendMarketListPurpleGear(int index, int price);
    public abstract void sendMarketBuyItem(int type, int index);
    public abstract void end_Dialog();

    // ======================================================
    //  TÍCH TIÊU RUBY / TÍCH NẠP
    // ======================================================

    public abstract void sendTichTieuRubyTable(int point, java.util.List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException;
    public abstract void sendTichTieuRubyClaimSuccess(byte id) throws IOException;
    public abstract void sendTichNapTable(int point, java.util.List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException;
    public abstract void sendTichNapClaimSuccess(byte id) throws IOException;
    public abstract void sendTichLuyCongDonTable(int point, java.util.List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException;
    public abstract void sendTichLuyCongDonClaimSuccess(short cost, int ruby) throws IOException;

    // ======================================================
    //  INPUT / DIALOG FLOW
    // ======================================================

    public abstract void startInput();
    public abstract void startYesNo();

    // ======================================================
    //  DYNAMIC MENUS (iMenuDymanic & NPC handlers call these)
    // ======================================================

    /** Gửi menu NPC chuẩn (no icon). */
    public abstract void send_menu(String title, String[] menuNames, int npcId);

    /** Gửi menu động với icon tuỳ chọn. icons == null -> no icon. */
    public abstract void send_dynamic_menu(int npcId, int menuId, String title, String[] options, short[] icons);

    /** Shortcut: send_dynamic_menu không icon, menuId=0. */
    public abstract void send_dynamic_menu(int npcId, String title, String[] options);
    
    public abstract void sendDymanicMenuMap(int npcId, String title, String[] options);

    /** Type 1: menu với checkState + colorState cho mỗi option. */
    public abstract void send_dynamic_menu_type1(int npcId, int menuId, String title, String[] options, byte[] checkStates, byte[] colorStates);

    /** Gửi menu danh sách bản đồ (type 1 với unlock logic). */
    public abstract void send_dynamic_menu_maps(int npcId, int menuId, String title, int[] mapIds);

    /** Type 2: menu text đơn giản. */
    /** Type 1: menu dọc chuẩn. */
    public abstract void send_dynamic_menu_type1(int npcId, int menuId, String title, String[] options);

    /** Type 2: menu dọc có header. */
    public abstract void send_dynamic_menu_type2(int npcId, int menuId, String title, String[] options);

    /** Type 3: menu kèm itemTemplateId + itemType cho mỗi option. */
    public abstract void send_dynamic_menu_type3(int npcId, int menuId, String title, String[] options, byte[] itemTemplateIds, byte itemType);

    /** Type 4: menu dùng List<String> và List<Integer> icon. */
    public abstract void send_dynamic_menu_type4(int npcId, int menuId, String title, List<String> options, List<Integer> icons);

    /** Type 5: menu ngang có icon. */
    public abstract void send_dynamic_menu_type5(int npcId, int menuId, String title, String[] options, short[] icons);

    /** Type 6: menu với icon + colorState. */
    public abstract void send_dynamic_menu_type6(int npcId, int menuId, String title, String[] options, short[] icons, byte[] colorStates);

    /** Type 7: menu ngang lưới grid. */
    public abstract void send_dynamic_menu_type7(int npcId, int menuId, String title, String[] options, short[] icons);

    // ======================================================
    //  LAMBDA MENU (Type 2 Menu Support)
    // ======================================================

    public abstract void openMenu();
    public abstract void openMenu(String title);
    public abstract void openMenu(int npcId, String title);
    public abstract void openMenu(List<model.Menu> menus);
    public abstract void openMenu(String title, List<model.Menu> menus);
    public abstract void openMenu(int npcId, String title, List<model.Menu> menus);
    public abstract void openDynamicMenu(int npcId, String title, String[] base, short[] baseIcons);
    public abstract void openDynamicMenu(int npcId, String title, List<model.Menu> menus);
    public abstract void openDynamicMenu(int npcId, String title);

    // ======================================================
    //  REBUILD / UPGRADE UI
    // ======================================================

    public abstract void sendRebuildPutItem(short idItem, byte cat, short num) throws IOException;
    public abstract void sendRebuildActionMsg(byte actionType, String msg) throws IOException;
    public abstract void sendRebuildCombineHopDaKham(String msg, short id1, short id2, short num, byte cat) throws IOException;
    public abstract void sendRebuildCombineDaSieuCapPutMaterial(byte action, short idItem, byte cat, short num, byte percent) throws IOException;
    public abstract void sendRebuildCombineGhepManh(String msg, short index) throws IOException;

    // ======================================================
    //  BUFF / EFFECT
    // ======================================================

    public abstract void send_buff(byte type, short idSkill, short idIcon, short idEffSkill, int time_buff,
                                   List<Short> list_id, List<Integer> list_par, short[] extraEffs);

    public abstract void addEffect(short indexMap, short idEff, int time, byte typeMove, byte loop);

    // ======================================================
    //  TÀI XỈU
    // ======================================================

    public abstract void notice_dice_TaiXiu() throws IOException;
    public abstract void updateInfoTaiXiu() throws IOException;
    public abstract void showTableTaiXiu(int type) throws IOException;

    // ======================================================
    //  MESSAGE 45 (DEVIL UPGRADE & CRAFT PANEL)
    // ======================================================
    public abstract void sendUpgradeDevilShowTable(byte clientType) throws IOException;
    public abstract void sendUpgradeDevilPutMaterial(byte action, byte slot, short id, byte cat, short quantity) throws IOException;
    public abstract void sendUpgradeDevilPercent(byte action, byte percent) throws IOException;
    public abstract void sendUpgradeDevilActionMsg(byte action, byte resultType, String msg) throws IOException;
    public abstract void sendUpgradeDevilCraftPanel(String title, byte numIngredients, short[] ingIds, short[] ingQtys, byte[] ingCats, short[] ingIcons, int costBeri, short costRuby, int costExtol, short resultId, short resultQty, byte resultCat, short resultIcon, byte successRate) throws IOException;
}
