package network;

import model.Player;
import mob.Mob;
import java.io.IOException;

public class NoService extends Service {

    // ——— Singleton chuẩn NsoZen: bot KHÔNG có session, dùng chung 1 instance ———
    private static final NoService INSTANCE = new NoService(null);

    /**
     * Trả về singleton NoService — dùng cho bot, không cần tạo mới mỗi instance.
     * Giống NsoZen: Bot.getService() -> NoService.getInstance()
     */
    public static NoService getInstance() {
        return INSTANCE;
    }

    public NoService(Player player) {
        super(player);
    }

    @Override
    public void sendMessage(Message m) {
        if (m != null) {
            try { m.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void send_msg_data(int cmd, String path, boolean save_cache) throws IOException {
    }

    @Override
    public void UpdateInfoMaincharInfo() throws IOException {
    }

    @Override
    public void Main_char_Info() throws IOException {
    }

    @Override
    public void UpdatePvpPoint() throws IOException {
    }

    @Override
    public void update_PK(Player target, boolean save_cache) throws IOException {
    }

    @Override
    public void getThanhTich(Player p) throws IOException {
    }

    @Override
    public void Weapon_fashion(Player p, boolean save_cache) throws IOException {
    }

    @Override
    public void Send_UI_Shop(int type) throws IOException {
    }

    @Override
    public void charWearing(Player p, boolean save_cache) throws IOException {
    }

    @Override
    public void send_obj_template(Message m) throws IOException {
        if (m != null) {
            try { m.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void request_mob_in4(Message m2) throws IOException {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void send_mob_info(Mob temp) throws IOException {
    }

    @Override
    public void rms_process(Message m2) {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void area_select(Message m2) throws IOException {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void pet(Player p, boolean save_cache) throws IOException {
    }

    @Override
    public void login_ok(boolean save_cache) throws IOException {
    }

    @Override
    public void checkPlayInMap(Message m2) {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void buy_item(Message m2) throws IOException {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void send_box_ThongBao_OK(String notice) {
    }

    @Override
    public void ChestWanted(boolean save_cache) throws IOException {
    }

    @Override
    public void use_potion(int healHp, int healMp) throws IOException {
    }

    @Override
    public void send_view_other_player(Player p) throws IOException {
    }

    @Override
    public void sell_item(Message m2) throws IOException {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }

    @Override
    public void request_item4_info(Message m2) throws IOException {
        if (m2 != null) {
            try { m2.cleanup(); } catch (IOException e) {}
        }
    }


    @Override
    public void CountDown_Ticket() throws IOException {
    }

    @Override
    public void NewDialog_eat_taq(String[] name_, int[] icon_, int id) throws IOException {
    }

    @Override
    public void Help_From_Server(int num, String text) throws IOException {
    }

    @Override
    public void Wanted(boolean save_cache) throws IOException {
    }

    @Override
    public void start_combo(int type) throws IOException {
    }

    @Override
    public void send_eff(int b, int num) throws IOException {
    }

    @Override
    public void send_eff_sword_splash(int id) throws IOException {
    }

    @Override
    public void DonotAutoReconnect() throws IOException {
    }

    @Override
    public void send_time_cool_down(long t, String title, int type) throws IOException {
    }

    @Override
    public void send_hp_map(int hp, int hpMax) throws IOException {
    }

    @Override
    public void sendConfirmYesNo(short yesNoType) {
        try {
            Message mAns = new Message(-11);
            mAns.writer().writeShort(yesNoType);
            mAns.writer().writeByte((byte) 0); // Confirm "Đồng ý"
            model.ClientYesNo.process(this.player, mAns);
            mAns.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUpgradeGear(short itemIndex) {
        try {
            this.player.tool_upgrade = new int[] {-1, -1};
            Message mUp = new Message(-48);
            mUp.writer().writeByte(2); // start upgrade
            mUp.writer().writeShort(itemIndex);
            mUp.writer().writeByte(1); // use Beri
            itemz.UpgradeItem.process(this.player, mUp);
            mUp.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendQuestProcess(short questId, byte status) {
        try {
            Message mOpt = new Message(-23);
            mOpt.writer().writeByte(status); // 1: do, 4: finish
            mOpt.writer().writeShort(questId);
            model.Quest.process(this.player, mOpt);
            mOpt.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPartyAccept(short inviterId) {
        try {
            Message mAccept = new Message(-25);
            mAccept.writer().writeByte(4); // accept type
            mAccept.writer().writeShort(inviterId);
            model.Party.process(this.player, mAccept);
            mAccept.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPartyInvite(short indexMap) {
        try {
            Message mInvite = new Message(-25);
            mInvite.writer().writeByte(0); // invite request
            mInvite.writer().writeShort(indexMap);
            model.Party.process(this.player, mInvite);
            mInvite.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTableTickOption(short idDialog) {
        try {
            Message mReady = new Message(-74);
            mReady.writer().writeByte(1); // tick/ready type
            mReady.writer().writeShort(idDialog);
            if (player.tableTickOption.idDialog == idDialog) {
                player.tableTickOption.processResponse(player, (byte)1);
            }
            mReady.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMarketEarningsClaim(int index) {
        try {
            this.player.data_yesno = new int[]{3, index};
            Message mAns = new Message(-11);
            mAns.writer().writeShort((short) 19);
            mAns.writer().writeByte((byte) 0);
            model.ClientYesNo.process(this.player, mAns);
            mAns.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMarketListPurpleGear(int index, int price) {
        try {
            this.player.data_yesno = new int[]{index, price};
            Message mAns = new Message(-11);
            mAns.writer().writeShort((short) 17);
            mAns.writer().writeByte((byte) 0);
            model.ClientYesNo.process(this.player, mAns);
            mAns.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMarketBuyItem(int type, int index) {
        try {
            this.player.data_yesno = new int[]{type, index};
            Message mAns = new Message(-11);
            mAns.writer().writeShort((short) 25);
            mAns.writer().writeByte((byte) 0);
            model.ClientYesNo.process(this.player, mAns);
            mAns.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    //  DYNAMIC MENUS — bot không cần gửi menu UI
    // ======================================================

    @Override
    public void send_menu(String title, String[] menuNames, int npcId) {}

    @Override
    public void send_dynamic_menu(int npcId, int menuId, String title, String[] options, short[] icons) {}

    @Override
    public void send_dynamic_menu(int npcId, String title, String[] options) {}

    @Override
    public void send_dynamic_menu_type1(int npcId, int menuId, String title, String[] options, byte[] checkStates, byte[] colorStates) {}

    @Override
    public void send_dynamic_menu_maps(int npcId, int menuId, String title, int[] mapIds) {}

    @Override
    public void send_dynamic_menu_type2(int npcId, int menuId, String title, String[] options) {}

    @Override
    public void send_dynamic_menu_type3(int npcId, int menuId, String title, String[] options, byte[] itemTemplateIds, byte itemType) {}

    @Override
    public void send_dynamic_menu_type4(int npcId, int menuId, String title, java.util.List<String> options, java.util.List<Integer> icons) {}

    @Override
    public void send_dynamic_menu_type6(int npcId, int menuId, String title, String[] options, short[] icons, byte[] colorStates) {}

    // ======================================================
    //  REBUILD / UPGRADE — bot không cần UI
    // ======================================================

    @Override
    public void sendRebuildPutItem(short idItem, byte cat, short num) throws IOException {}

    @Override
    public void sendRebuildActionMsg(byte actionType, String msg) throws IOException {}

    @Override
    public void sendRebuildCombineHopDaKham(String msg, short id1, short id2, short num, byte cat) throws IOException {}

    @Override
    public void sendRebuildCombineDaSieuCapPutMaterial(byte action, short idItem, byte cat, short num, byte percent) throws IOException {}

    @Override
    public void sendRebuildCombineGhepManh(String msg, short index) throws IOException {}

    // ======================================================
    //  BUFF / EFFECT — bot không gửi buff UI
    // ======================================================

    @Override
    public void send_buff(byte type, short idSkill, short idIcon, short idEffSkill, int time_buff,
                          java.util.List<Short> list_id, java.util.List<Integer> list_par, short[] extraEffs) {}

    @Override
    public void addEffect(short indexMap, short idEff, int time, byte typeMove, byte loop) {}

    // ======================================================
    //  TÀI XỈU — bot không tham gia
    // ======================================================

    @Override
    public void notice_dice_TaiXiu() throws IOException {}

    @Override
    public void updateInfoTaiXiu() throws IOException {}

    @Override
    public void showTableTaiXiu(int type) throws IOException {}

    @Override
    public void sendUpgradeDevilShowTable(byte clientType) throws IOException {}

    @Override
    public void sendUpgradeDevilPutMaterial(byte action, byte slot, short id, byte cat, short quantity) throws IOException {}

    @Override
    public void sendUpgradeDevilPercent(byte action, byte percent) throws IOException {}

    @Override
    public void sendUpgradeDevilActionMsg(byte action, byte resultType, String msg) throws IOException {}

    @Override
    public void sendUpgradeDevilCraftPanel(String title, byte numIngredients, short[] ingIds, short[] ingQtys, byte[] ingCats, short[] ingIcons, int costBeri, short costRuby, int costExtol, short resultId, short resultQty, byte resultCat, short resultIcon, byte successRate) throws IOException {}

    @Override
    public void send_count_kick_ava(byte id, byte count) {}

    @Override
    public void send_vong_sinh_tu(short x, short y, short w, short h, int color) {}

    @Override
    public void send_update_point_ww(Player target) {}

    @Override
    public void send_quay_wc_show() {}

    @Override
    public void send_quay_wc_list_items(byte[] cats, short[] icons) {}

    @Override
    public void send_quay_wc_info(short idIconVongQuay, short numVe, short numLanDaQuay) {}

    @Override
    public void send_quay_wc_result(byte typeQuay, byte[] cats, String[] names, short[] icons, int[] quantities, byte[] colors) {}

    @Override
    public void send_clan_fight_list(java.util.List<clan.Clan> listClan) {}

    @Override
    public void send_clan_fight_notice(String text) {}

    @Override
    public void send_clan_fight_invite(short clanId, String clanName, short clanLv, byte typeFight) {}

    @Override
    public void send_clan_fight_members(java.util.List<clan.ClanMember> members) {}

    @Override
    public void showVongQuayWC() {}
}
