package network;

import effect.DataEffect;
import model.MyPet;
import model.Player;
import ability.Ability;
import activities.HanhTrinh;
import activities.Upgrade_Skin;
import model.Pet;
import event.EventData;
import skill.Skill_info;
import skill.Skill_Template;
import map.MapBossInfo;
import zabstracts.AbsService;
import clan.ClanHanhTrinhIcon;
import clan.Clan;
import rank.Ranked;
import itemz.Item;
import event.SuKienHalloween;
import event.eboss.BiNgoMa;
import event.SuKienNoel;
import boss.BossPica;
import boss.BossTheGioi;
import java.io.IOException;
import java.util.List;
import itemz.Rebuild_Item;
import event.EventManager;
import map.zones.WorldWar;
import event.SuKienTrongCay;
import java.util.ArrayList;
import boss.SuperBossManager;
import map.Zone;
import mob.Mob;
import template.*;
import core.*;

public class Service extends AbsService {



    protected Player player;



    public Service(Player player) {

        this.player = player;

    }



    public void setPlayer(Player player) {

        this.player = player;

    }



    @Override

    public void sendMessage(Message m) {

        if (player != null && player.conn != null) {

            try { player.addmsg(m); } catch (Exception e) {}

        }

    }





    public void send_msg_data(int cmd, String path, boolean save_cache)

            throws IOException {

        Session conn = this.player.conn;

        Message m = new Message(cmd);

        m.writer().write(ZUtil.loadfile(path));

        if (save_cache) {

            conn.p.msgs.add(m);

        } else {

            sendMessage(m);

        }

        m.cleanup();

    }



    public static void send_msg_data(Session conn, int cmd, String path, boolean save_cache)

            throws IOException {

        Message m = new Message(cmd);

        m.writer().write(ZUtil.loadfile(path));

        if (save_cache) {

            conn.p.msgs.add(m);

        } else {

            conn.addmsg(m);

        }

        m.cleanup();

    }



    public static void send_msg_data(Session conn, int cmd, byte[] data, boolean save_cache)

            throws IOException {

        Message m = new Message(cmd);

        m.writer().write(data);

        if (save_cache) {

            conn.p.msgs.add(m);

        } else {

            conn.addmsg(m);

        }

        m.cleanup();

    }



    public void UpdateInfoMaincharInfo() throws IOException {

        Player p = this.player;

        Message m = new Message(-75);

        int id_f = -1;

        for (int i = 0; i < p.fashion.size(); i++) {

            if (p.fashion.get(i).is_use) {

                id_f = p.fashion.get(i).id;

                break;

            }

        }

        m.writer().writeShort(id_f); // id fashion

        int par_agility = (p.ability != null) ? p.ability.get_agility(true) : 0;
        m.writer().writeShort(par_agility); // giam cooldown skill : % giam hoi chieu

        m.writer().writeShort(p.get_percent_mana_use_skill());

        sendMessage(m);

        m.cleanup();

    }



    public void Main_char_Info() throws IOException {

        Main_char_Info(false);

    }



    public void Main_char_Info(boolean isSwitch) throws IOException {

        Player p = this.player;

        int hp_max = p.ability.get_hp_max(true);

        int mp_max = p.ability.get_mp_max(true);

        if (p.hp > hp_max) {

            p.hp = hp_max;

        }

        if (p.mp > mp_max) {

            p.mp = mp_max;

        }

        Message m = new Message(-10);

        m.writer().writeShort(p.index_map);

        m.writer().writeUTF(p.name);

        m.writer().writeInt(hp_max);

        m.writer().writeInt(mp_max);

        m.writer().writeInt(p.hp);

        m.writer().writeInt(p.mp);

        m.writer().writeShort(p.level);

        m.writer().writeShort(p.get_level_percent());

        m.writer().writeShort(p.thongthao);

        if (p.level >= 100) {

            m.writer().writeShort(p.get_level_percent());

        } else {

            m.writer().writeShort(0);

        }

        m.writer().writeInt(Ranked.get_rank_wanted(p.name));

        m.writer().writeByte(p.clazz);

        m.writer().writeInt(p.pointPk);

        m.writer().writeShort((int) p.pointAttribute);

        m.writer().writeByte(p.typePirate);

        m.writer().writeByte(p.indexGhostServer);

        m.writer().writeByte(p.getNumPassive());

        m.writer().writeByte(p.ability.get_level_perfect());

        //

        m.writer().writeByte(Ability.NameAttribute.length); // size

        int p_, p_pluss;

        for (int i = 0; i < Ability.NameAttribute.length; i++) {

            m.writer().writeUTF(Ability.NameAttribute[i]);

            p_ = (int) (i == 0 ? p.point1

                    : (i == 1 ? p.point2 : (i == 2 ? p.point3 : (i == 3 ? p.point4 : p.point5))));

            p_pluss = p.ability.get_point_plus(i + 1);

            m.writer().writeShort(p_);

            m.writer().writeShort(p_pluss);

            int dem = 0;

            int[] par_show = new int[Ability.Id[i].length];

            for (int j = 0; j < Ability.Id[i].length; j++) {

                par_show[j] = (int) p.ability.getTemplateValue(Ability.Id[i][j], p_ + p_pluss);

                if (par_show[j] > 0) {

                    dem++;

                }

            }

            m.writer().writeByte(dem);

            for (int j = 0; j < Ability.Id[i].length; j++) {

                if (par_show[j] > 0) {

                    m.writer().writeByte(Ability.Id[i][j]);

                    m.writer().writeInt(par_show[j]);

                }

            }

        }

        //

        m.writer().writeShort(p.pointSkill);

        m.writer().writeByte(10);

        for (int i = 0; i < 10; i++) {

            m.writer().writeByte(1);

            m.writer().writeByte(1);

        }

        byte[] a = new byte[] { 0, 1, 2, 3, 4, 25, 15, 16, 26, 27, 17, 18, 10, 11, 12, 13, 14, 19,

                20, 21, 22, 23, 24, 46, 48, 47, 49, 50, 51, 52, 53, 63, 55, 56, 57, 58, 59, 62, 67,

                68, 69, 70, 71, 72, 73, 74, 75, 76 };

        int[] b = new int[a.length];

        for (int i = 0; i < b.length; i++) {

            b[i] = p.ability.view_in4(a[i]);

        }

        int dem = 0;

        for (int i = 0; i < b.length; i++) {

            if (b[i] > 0 || a[i] == 15 || a[i] == 16 || a[i] == 17 || a[i] == 18 || a[i] == 26

                    || a[i] == 27 || a[i] == 53 || a[i] == 10 || a[i] == 11 || a[i] == 12

                    || a[i] == 2) {

                dem++;

            }

        }

        m.writer().writeByte(dem);

        for (int i = 0; i < a.length; i++) {

            if (b[i] > 0 || a[i] == 15 || a[i] == 16 || a[i] == 17 || a[i] == 18 || a[i] == 26

                    || a[i] == 27 || a[i] == 53 || a[i] == 10 || a[i] == 11 || a[i] == 12

                    || a[i] == 2) {

                m.writer().writeByte(a[i]);

                m.writer().writeInt(b[i]);

            }

        }

        m.writer().writeShort(-1);
        m.writer().writeShort(-1);
        m.writer().writeShort(-1);
        m.writer().writeShort(-1);

        sendMessage(m);
        m.cleanup();

        if (p.id_danh_hieu_su_dung > 0) {
            activities.DanhHieu dhSelf = activities.DanhHieu.get_Id(p.id_danh_hieu_su_dung);
            if (dhSelf != null && dhSelf.idEff > 0) {
                try {
                    Message mDh = new Message(74);
                    mDh.writer().writeByte(1);
                    mDh.writer().writeShort(p.index_map);
                    mDh.writer().writeShort(dhSelf.idEff);
                    mDh.writer().writeInt(-1);
                    mDh.writer().writeByte(0);
                    mDh.writer().writeByte(0);
                    sendMessage(mDh);
                    mDh.cleanup();
                } catch (Exception ignored) {}
            }
        }
    }



    public void UpdatePvpPoint() throws IOException {

        Player p = this.player;

        Message m = new Message(-66);

        m.writer().writeInt(p.get_pvpPoint());

        m.writer().writeInt(p.pvp_win); // win

        m.writer().writeInt(p.pvp_lose); // lose

        sendMessage(m);

        m.cleanup();

    }



    public void update_PK(Player target, boolean save_cache) throws IOException {

        Player recipient = this.player;

        if (target instanceof bot.mercenary.MercenaryBot) {
            ((bot.mercenary.MercenaryBot) target).updateBotTypePk();
        } else if (target instanceof model.DeTu) {
            model.DeTu dt = (model.DeTu) target;
            if (dt.master != null) {
                dt.type_pk = dt.master.type_pk;
                dt.typePirate = dt.master.typePirate;
                dt.clan = dt.master.clan;
            }
        } else if (target instanceof bot.Bot) {
            ((bot.Bot) target).updateBotTypePk();
        }

        Message m = new Message(14);

        m.writer().writeShort(target.index_map);

        m.writer().writeByte(target.type_pk); // type pk

        m.writer().writeByte(target.typePirate); // type pirate

        m.writer().writeByte(target.is_show_hat ? 0 : 1); // dont show hat

        m.writer().writeShort(-1);

        m.writer().writeByte(target.is_show_weapon ? 0 : 1); // dont show weaponF

        m.writer().writeByte(0); // type color

        if (save_cache) {

            recipient.msgs.add(m);

        } else {

            sendMessage(m);

        }

        m.cleanup();

    }



    public void getThanhTich(Player p) throws IOException {

        Player p0 = p;

        Message m = new Message(65);

        m.writer().writeShort(p0.index_map);

        m.writer().writeByte(0);

        //

        m.writer().writeByte(Ranked.get_Thanh_tich_pvp(p0)); // pvp

        m.writer().writeByte(Ranked.get_Thanh_tich_level(p0)); // level

        m.writer().writeByte(p0.get_index_full_set());

        sendMessage(m);

        m.cleanup();

    }



    public void Weapon_fashion(Player p, boolean save_cache) throws IOException {

        Player p0 = p;

        Message m = new Message(-104);

        m.writer().writeShort(p0.index_map);

        m.writer().writeByte(0);

        m.writer().writeByte(6);

        m.writer().writeShort(p0.get_head());

        if (save_cache) {

            this.player.msgs.add(m);

        } else {

            sendMessage(m);

        }

        m.cleanup();

    }

    

    public void Send_UI_Shop(int type) throws IOException {

        Player p = this.player;

        if (p != null) {

            p.typeShop = (byte) type;

        }

        if (type == 110 || type == 118 || type == 116 || type == 12 || type == 8) {
            zabstracts.AbsShop shop = zabstracts.AbsShop.get(type);
            if (shop != null) {
                shop.openUI(p);
                return;
            }
        }

        Message m = new Message(-19);

        m.writer().writeByte(type);

        switch (type) {

            case 0:

            case 1:

            case 2:

            case 3:

            case 4: {

                m.writer().writeUTF(Manager.NAME_ITEM_SELL_TEMP[type]);

                m.writer().writeByte(3);

                List<ItemSell> list_sell = ItemSell.get_it_sell(p.level, type);

                List<ItemSell> validItems = new ArrayList<>();

                if (list_sell != null) {

                    for (ItemSell is : list_sell) {

                        if (ItemTemplate3.get_it_by_id(is.id) != null) {

                            validItems.add(is);

                        }

                    }

                }

                m.writer().writeShort(validItems.size());

                for (int i = 0; i < validItems.size(); i++) {

                    ItemSell it_sell_temp = validItems.get(i);

                    ItemTemplate3 it_temp = ItemTemplate3.get_it_by_id(it_sell_temp.id);

                    ItemTemplate3.readUpdateItem(m.writer(), it_temp);

                    if (it_temp.ruby > 0) {

                        m.writer().writeByte(1);

                        m.writer().writeInt(it_temp.ruby);

                    } else {

                        m.writer().writeByte(0);

                        m.writer().writeInt(it_sell_temp.price > 0 ? it_sell_temp.price : it_temp.beri);

                    }

                }

                break;

            }

            case 6: {

                m.writer().writeUTF("Shop Nguyên liệu");

                m.writer().writeByte(7);

                byte[] id_sell = ItemSell.get_it_sell_material();

                m.writer().writeShort(id_sell.length);

                for (int i = 0; i < id_sell.length; i++) {

                    m.writer().writeByte(id_sell[i]);

                    m.writer().writeShort(1);

                }

                break;

            }

            case 20: {

                m.writer().writeUTF("Quán ăn");

                m.writer().writeByte(4);

                short[] id_sell = ItemSell.get_it_sell_potion(p);

                m.writer().writeShort(id_sell.length);

                for (int i = 0; i < id_sell.length; i++) {

                    m.writer().writeShort(id_sell[i]);

                    m.writer().writeShort(1);

                }

                break;

            }

            case 99: {

                m.writer().writeUTF("Rương đồ");

                m.writer().writeByte(99);

                m.writer().writeShort(0);

                break;

            }

            case 111: {

                m.writer().writeUTF("Shop Đá");

                m.writer().writeByte(4);

                m.writer().writeShort(Rebuild_Item.ID_SELL.length);

                for (int i = 0; i < Rebuild_Item.ID_SELL.length; i++) {

                    m.writer().writeShort(Rebuild_Item.ID_SELL[i]);

                    m.writer().writeShort(1);

                }

                break;

            }

            case 119: {

                m.writer().writeUTF("Thùng Rác");

                m.writer().writeByte(3);

                m.writer().writeShort(p.item.save_item_wear.size());

                for (int i = p.item.save_item_wear.size() - 1; i >= 0; i--) {

                    Item_wear it_select = p.item.save_item_wear.get(i);

                    if (it_select != null) {

                        it_select.index = (short) i;

                        Item.readUpdateItem(m.writer(), it_select, p);

                        m.writer().writeByte(1); // ruby

                        m.writer().writeInt(5);  // 5 ruby

                    }

                }

                break;

            }

        }

        sendMessage(m);

        m.cleanup();

    }



    public void openUIBox() throws IOException {

        Player p = this.player;

        zabstracts.AbsShop shop = zabstracts.AbsShop.get(99);

        if (shop != null) {

            shop.openUI(p);

            return;

        }

        Message m = new Message(-19);

        m.writer().writeByte(99);

        m.writer().writeUTF("Rương đồ");

        m.writer().writeByte(99);

        m.writer().writeShort(player.item.max_box);

        sendMessage(m);

        m.cleanup();

    }



    public void charWearing(Player p, boolean save_cache) throws IOException {
        Player p0 = p;
        Message m = new Message(19);
        m.writer().writeShort(p0.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(p0.get_head());
        m.writer().writeShort(p0.get_hair());
        m.writer().writeByte(16);
        for (int i = 0; i < 16; i++) {
            Item_wear it_w = (p0.item != null && p0.item.it_body != null && i < p0.item.it_body.length) ? p0.item.it_body[i] : null;
            if (i == 6 && p0.item != null && p0.item.it_heart != null) {
                it_w = p0.item.it_heart;
            }
            if (it_w != null) {
                m.writer().writeByte(1);
                if (p0.index_map == this.player.index_map) {
                    Item.readUpdateItem(m.writer(), it_w, p0);
                }
                m.writer().writeShort(p0.get_wearing_part(i));
            } else {
                m.writer().writeByte(0);
                m.writer().writeShort(p0.get_wearing_part(i));
            }
        }
        m.writer().writeShort(-1);
        m.writer().writeShort(-1);
        m.writer().writeShort(-1);
        m.writer().writeShort(-1);
        if (save_cache) {
            this.player.msgs.add(m);
        } else {
            sendMessage(m);
        }
        m.cleanup();
    }



    public static void send_icon(Message m, Session conn) {
        if (conn != null) {
            byte zoomLv = (conn.zoomlv <= 0) ? 1 : conn.zoomlv;
            String path = "";
            try {
                int available = m.reader().available();
                byte iconType;
                int iconId;
                boolean isModern = false;

                int legacyId = 0;
                if (available >= 5) {
                    // Giao thức mới: byte type (1 byte) + int id (4 bytes)
                    iconType = m.reader().readByte();
                    iconId = m.reader().readInt();
                    isModern = true;
                } else {
                    // Giao thức cũ: short id (2 bytes)
                    short id = m.reader().readShort();
                    if (id == 23088 || id == 23089) {
                        return;
                    }
                    if (id >= 4114 && id <= 4187) { // id icon combo skill
                        id += 406;
                    }
                    if (id > 4912 && id < 4935) { // icon kich an
                        id = 4912;
                    }
                    legacyId = id;
                    iconType = template.IconType.detectLegacyType(id);
                    iconId = template.IconType.detectLegacyId(id);
                }

                String folder = template.IconType.getFolder(iconType);
                List<String> paths = new ArrayList<>();

                // Chỉ lấy CỨNG đúng thư mục của loại icon: data/icon/{folder}/{zoomLv}/{id}.png
                paths.add("data/icon/" + folder + "/" + zoomLv + "/" + iconId + ".png");

                // --- Tạm comment toàn bộ fallback thư mục cũ để test ---
                // paths.add("data/icon/" + zoomLv + "/" + folder + "/" + iconId + ".png");
                // paths.add("release/data/icon/" + zoomLv + "/" + folder + "/" + iconId + ".png");
                // if (iconType == template.IconType.EFF_CLIENT) {
                //     paths.add("data/datafromserver/x" + zoomLv + "/eff/g" + iconId + ".png");
                //     paths.add("data/datafromserver/x" + zoomLv + "/eff/" + iconId + ".png");
                //     paths.add("data/datafromserver/x4/eff/g" + iconId + ".png");
                //     paths.add("data/datafromserver/x1/eff/g" + iconId + ".png");
                // } else if (iconType == template.IconType.EFF_CLIENT_LOW) {
                //     paths.add("data/datafromserver/x" + zoomLv + "/efflow/g" + iconId + ".png");
                //     paths.add("data/datafromserver/x" + zoomLv + "/efflow/" + iconId + ".png");
                //     paths.add("data/datafromserver/x4/efflow/g" + iconId + ".png");
                //     paths.add("data/datafromserver/x1/efflow/g" + iconId + ".png");
                // }
                // int legacyOffset = template.IconType.getLegacyOffset(iconType);
                // int legacyId = iconId + legacyOffset;
                // if (iconType == template.IconType.CHAR_PART && iconId >= 10000) {
                //     legacyId = iconId - 10000 + 26000;
                // }
                // paths.add("data/icon/" + zoomLv + "/" + legacyId + ".png");
                // paths.add("release/data/icon/" + zoomLv + "/" + legacyId + ".png");
                // if (legacyId >= 4201 && legacyId <= 4216) {
                //     paths.add("data/icon/" + zoomLv + "/" + (legacyId - 200) + ".png");
                //     paths.add("release/data/icon/" + zoomLv + "/" + (legacyId - 200) + ".png");
                // } else if (legacyId >= 4701 && legacyId <= 4716) {
                //     paths.add("data/icon/" + zoomLv + "/" + (legacyId - 200) + ".png");
                //     paths.add("release/data/icon/" + zoomLv + "/" + (legacyId - 200) + ".png");
                // }
                // paths.add("data/icon/4/" + legacyId + ".png");
                // paths.add("release/data/icon/4/" + legacyId + ".png");
                // paths.add("data/icon/1/" + legacyId + ".png");
                // paths.add("release/data/icon/1/" + legacyId + ".png");
                // paths.add("data/icon/" + legacyId + ".png");
                // paths.add("release/data/icon/" + legacyId + ".png");

                byte[] dataImg = null;
                for (String p : paths) {
                    try {
                        dataImg = ZUtil.loadfile(p);
                        if (dataImg != null && dataImg.length > 0) {
                            path = p;
                            break;
                        }
                    } catch (Exception ignored) {}
                }

                if (dataImg == null || dataImg.length == 0) {
                    return;
                }

                Message m2 = new Message(-101);
                if (isModern) {
                    m2.writer().writeByte(iconType);
                    m2.writer().writeInt(iconId);
                } else {
                    m2.writer().writeShort((short) legacyId);
                }
                m2.writer().write(dataImg);
                conn.addmsg(m2);
                m2.cleanup();

            } catch (IOException e) {
                core.Log.debug("Resource", "Icon file not found: " + path);
            }
        }
    }

    public static void send_icon_new(Message m, Session conn) {
        if (conn != null) {
            byte zoomLv = (conn.zoomlv <= 0) ? 1 : conn.zoomlv;
            String path = "";
            try {
                byte iconType = m.reader().readByte();
                int iconId = m.reader().readInt();

                String folder = template.IconType.getFolder(iconType);
                List<String> paths = new ArrayList<>();

                // Chỉ lấy CỨNG đúng thư mục của loại icon: data/icon/{folder}/{zoomLv}/{id}.png
                paths.add("data/icon/" + folder + "/" + zoomLv + "/" + iconId + ".png");
                // paths.add("release/data/icon/" + folder + "/" + zoomLv + "/" + iconId + ".png");
                // paths.add("data/icon/" + folder + "/4/" + iconId + ".png");
                // paths.add("data/icon/" + folder + "/1/" + iconId + ".png");
                // paths.add("release/data/icon/" + folder + "/4/" + iconId + ".png");
                // paths.add("release/data/icon/" + folder + "/1/" + iconId + ".png");

                byte[] dataImg = null;
                for (String p : paths) {
                    try {
                        dataImg = ZUtil.loadfile(p);
                        if (dataImg != null && dataImg.length > 0) {
                            path = p;
                            break;
                        }
                    } catch (Exception ignored) {}
                }

                if (dataImg == null || dataImg.length == 0) {
                    return;
                }

                Message m2 = new Message(-93);
                m2.writer().writeByte(iconType);
                m2.writer().writeInt(iconId);
                m2.writer().write(dataImg);
                conn.addmsg(m2);
                m2.cleanup();

            } catch (IOException e) {
                core.Log.debug("Resource", "Icon file not found (new): " + path);
            }
        }
    }



    public void send_obj_template(Message m) throws IOException {

        Player p = this.player;

        byte type = m.reader().readByte();

        short id   = m.reader().readShort();

        switch (type) {

            case 1: case 97: case 98:

                sendFileTemplate(p, type, id);

                break;

            case 96:

                if (id < DataTemplate.AttriKichAn.length) {

                    Message m2 = new Message(48);

                    m2.writer().writeByte(96);

                    m2.writer().writeByte(id);

                    m2.writer().writeUTF(DataTemplate.AttriKichAn[id]);

                    p.addmsg(m2);

                    m2.cleanup();

                }

                break;

            case 4:

                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);

                if (it4 != null) {

                    Message m2 = new Message(48);

                    m2.writer().writeByte(4);

                    m2.writer().writeShort(it4.id);

                    m2.writer().writeShort(it4.icon);

                    m2.writer().writeUTF(it4.name);

                    m2.writer().writeShort(it4.indexInfoPotion);

                    m2.writer().writeInt(it4.beri);

                    m2.writer().writeShort(it4.ruby);

                    m2.writer().writeByte(it4.istrade);

                    m2.writer().writeByte(it4.type);

                    m2.writer().writeShort(it4.timedelay);

                    m2.writer().writeShort(it4.value);

                    m2.writer().writeShort(it4.timeactive);

                    m2.writer().writeUTF(it4.nameuse);

                    p.addmsg(m2);

                    m2.cleanup();

                }

                break;

        }

    }



    /** Gửi file template type 1/97/98 — dùng chung pattern byte+short+file */

    private void sendFileTemplate(Player p, byte type, short id) throws IOException {

        try {

            Message m2 = new Message(48);

            m2.writer().writeByte(type);

            m2.writer().writeShort(id);

            m2.writer().write(ZUtil.loadfile("data/template/" + type + "/" + id));

            p.addmsg(m2);

            m2.cleanup();

        } catch (IOException ignored) {

            // file không tồn tại — client sẽ retry

        }

    }



    public void request_mob_in4(Message m2) throws IOException {

        Player p = this.player;
        if (p == null || p.map == null) return;
        int id = m2.reader().readShort();
        Mob temp = findMobById(p, id);
        if (temp != null && !temp.isdie) {
            if (temp.map == null) {
                temp.map = p.map;
            }
            if (temp.map.equals(p.map)) {
                send_mob_info(temp);
            }
        }
    }

    /**
     * Tìm mob theo index trong tất cả các nguồn: zone thường, boss, event boss,
     * dungeon, clan, săn trùm, map đặc biệt...
     */
    private Mob findMobById(Player p, int id) {
        if (p == null || p.map == null) return null;
        Mob temp;

        // 1. Zone thông thường (ưu tiên mob trong map trước)
        temp = p.map.mobs.get(id);
        if (temp != null && !temp.isdie) return temp;

        // 2. Map boss
        if (Zone.is_map_boss(p.map.template.id)) {
            temp = MapBossInfo.get_mob(p, id);
            if (temp != null && !temp.isdie) return temp;
        }

        // 3. Dungeon gắn với Map/Zone (p.map.map_dungeon)
        if (p.map.map_dungeon != null) {
            temp = p.map.map_dungeon.get_mob(p, id);
            if (temp != null && !temp.isdie) return temp;
            if (p.map.map_dungeon.mobs != null) {
                temp = searchMobInList(p.map.map_dungeon.mobs, id);
                if (temp != null) return temp;
            }
        }

        // 4. Dungeon của người chơi (p.dungeon hoặc p.dungeons)
        zabstracts.AbsDungeon curDungeon = p.getCurrentDungeon();
        if (curDungeon != null && curDungeon != p.map.map_dungeon) {
            temp = curDungeon.get_mob(p, id);
            if (temp != null && !temp.isdie) return temp;
            if (curDungeon.mobs != null) {
                temp = searchMobInList(curDungeon.mobs, id);
                if (temp != null) return temp;
            }
        }
        if (p.dungeons != null) {
            for (zabstracts.AbsDungeon d : p.dungeons.values()) {
                if (d != null && d != p.map.map_dungeon && d != curDungeon) {
                    temp = d.get_mob(p, id);
                    if (temp != null && !temp.isdie) return temp;
                    if (d.mobs != null) {
                        temp = searchMobInList(d.mobs, id);
                        if (temp != null) return temp;
                    }
                }
            }
        }

        // 5. Map đặc biệt: liên tầng, siêu liên tầng, mr3, hang động, thử thách vệ thần, bảo vệ pháo đài, đảo kho báu...
        temp = findMobInSpecialMaps(p, id);
        if (temp != null && !temp.isdie) return temp;

        // 6. Săn trùm
        if (p.map.mapSanTrum != null) {
            Mob sm = p.map.mapSanTrum.mob;
            if (sm != null && !sm.isdie && sm.index == id) return sm;
        }

        // 7. Vườn cam
        if (p.map.vuonCam != null && p.map.vuonCam.mobs != null) {
            for (Mob mob : p.map.vuonCam.mobs) {
                if (mob != null && !mob.isdie && mob.index == id) return mob;
            }
        }

        // 8. Clan mob
        if (p.clan != null && p.clan.mob1 != null) {
            for (Mob mob : p.clan.mob1) {
                if (mob != null && mob.index == id) return mob;
            }
        }

        // 9. Siêu trùm
        temp = SuperBossManager.get_mob(p, id);
        if (temp != null && !temp.isdie) return temp;

        // 10. Little garden
        if (p.map.map_little_garden != null) {
            temp = p.map.get_mobs(id, 0);
            if (temp != null && !temp.isdie) return temp;
        }

        // 11. WorldBoss — BossTG
        if (BossTheGioi.mob != null && BossTheGioi.mob.map != null && BossTheGioi.mob.map.equals(p.map) && !BossTheGioi.mob.isdie && BossTheGioi.mob.index == id)
            return BossTheGioi.mob;

        // 12. Giờ tổ (event ID 3)
        if (event.EventManager.isActive(3) && event.eboss.LucciGioTo.mob != null
                && event.eboss.LucciGioTo.mob.map != null && event.eboss.LucciGioTo.mob.map.equals(p.map) && !event.eboss.LucciGioTo.mob.isdie && event.eboss.LucciGioTo.mob.index == id)
            return event.eboss.LucciGioTo.mob;

        // 13. Event boss từ EventManager (sự kiện 1)
        if (event.EventManager.isActive(1)) {
            temp = EventManager.dispatchGetMobInMap(p.map);
            if (temp != null && !temp.isdie && temp.index == id) return temp;
        }

        // 14. BossPica
        if (BossPica.mob != null && !BossPica.mob.isdie && BossPica.mob.index == id) return BossPica.mob;

        // 15. Fallback Mob.ENTRYS (chỉ lấy nếu mob thuộc đúng map hiện tại)
        temp = p.map.getMob(id);
        if (temp != null && !temp.isdie && (temp.map == null || temp.map.equals(p.map))) return temp;

        return null;
    }

    /** Tìm mob trong các map chiến đấu đặc biệt (liên tầng, hang, mr3, ttvt, bvpd, đảo kho báu, vv.) */
    private Mob findMobInSpecialMaps(Player p, int id) {
        if (p == null || p.map == null) return null;
        Mob found;

        found = searchMobInList(p.map.map_LienTang   != null ? p.map.map_LienTang.mobs   : null, id);
        if (found != null) return found;

        found = searchMobInList(p.map.map_SieuLienTang != null ? p.map.map_SieuLienTang.mobs : null, id);
        if (found != null) return found;

        found = searchMobInList(p.map.map_Mr3         != null ? p.map.map_Mr3.mobs         : null, id);
        if (found != null) return found;

        found = searchMobInList(p.map.map_Hang        != null ? p.map.map_Hang.mobs        : null, id);
        if (found != null) return found;

        if (p.map.map_ThuThachVeThan != null) {
            found = searchMobInList(p.map.map_ThuThachVeThan.mobs, id);
            if (found != null) return found;
        }

        if (p.map.map_DaoKhoBau != null && p.map.map_DaoKhoBau.mobs != null) {
            found = searchMobInList(p.map.map_DaoKhoBau.mobs, id);
            if (found != null) return found;
        }

        if (p.map.baoVePhaoDai != null) {
            found = p.map.baoVePhaoDai.GetMob(id);
            if (found != null && !found.isdie) return found;
        }

        return null;
    }

    private Mob searchMobInList(java.util.List<Mob> list, int id) {
        if (list == null) return null;
        for (Mob mob : list) {
            if (mob != null && !mob.isdie && mob.index == id) return mob;
        }
        return null;

    }



    public void send_mob_info(Mob temp) throws IOException {

        if (temp == null || temp.mtemplate == null) return;

        Player p = this.player;

        Message m = new Message(4);

        m.writer().writeShort(temp.index);

        m.writer().writeShort(temp.mtemplate.mob_id); // id mob

        m.writer().writeShort(temp.x);

        m.writer().writeShort(temp.y);

        m.writer().writeShort(temp.level); // lv

        m.writer().writeInt(temp.hp);

        m.writer().writeInt(temp.hp_max);

        short skill0 = (temp.mtemplate.skill != null && temp.mtemplate.skill.length > 0)
                ? temp.mtemplate.skill[0] : 0;
        m.writer().writeShort(skill0);

        short timeRespawn = (short) Mob.TIME_RESPAWN;
        if (temp.mtemplate != null && (temp.mtemplate.mob_id == 132 || temp.mtemplate.mob_id == 133)) {
            timeRespawn = 0; // Trụ Chiếm Đảo không tự động hồi sinh theo client timer
        }
        m.writer().writeShort(timeRespawn); // tgian hs

        m.writer().writeByte(temp.mtemplate.typemonster); // type mons

        //

        if (temp.boss_inf != null) {

            m.writer().writeByte(temp.boss_inf.levelBoss); // lvthongthao

        } else {

            m.writer().writeByte(0); // lvthongthao

        }

        sendMessage(m);

        m.cleanup();

    }



    public void sendRms(byte id) {
        Player p = this.player;
        if (p == null || p.rms == null || id < 0 || id >= p.rms.length) return;
        try {
            Message m = new Message(-33);
            m.writer().writeByte(id);
            byte[] data = p.rms[id];
            if (data != null && data.length > 0) {
                m.writer().writeShort(data.length);
                m.writer().write(data);
            } else {
                m.writer().writeShort(0);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception ignored) {}
    }

    public void rms_process(Message m2) {

        Player p = this.player;

        if (p == null) return;

        try {

            byte type = m2.reader().readByte();

            byte id = m2.reader().readByte();

            int size = 0;

            try {

                size = m2.reader().readShort();

            } catch (IOException e) {

            }

            // System.out.println("type " + type + " id " + id + " size " + size);

            if (id < 0) {
                return;
            }
            if (p.rms == null) {
                p.rms = new byte[Math.max(11, id + 1)][];
                for (int i = 0; i < p.rms.length; i++) p.rms[i] = new byte[0];
            } else if (id >= p.rms.length) {
                byte[][] newRms = new byte[Math.max(id + 1, p.rms.length * 2)][];
                System.arraycopy(p.rms, 0, newRms, 0, p.rms.length);
                for (int i = p.rms.length; i < newRms.length; i++) newRms[i] = new byte[0];
                p.rms = newRms;
            }

            if (type == 0) {

                if (id == 0) {

                    p.updateThanTrangRmsHotKey();

                    boolean check = false;

                    if (p.map != null && p.map.template != null) {

                        for (int i = 0; i < DataTemplate.mSea.length; i++) {

                            if (DataTemplate.mSea[i][1] == p.map.template.id) {

                                check = true;

                                break;

                            }

                        }

                    }

                    if (check) {

                        return;

                    }

                }

                Message m = new Message(-33);

                m.writer().writeByte(id);

                byte[] data = p.rms[id];

                if (data != null && data.length > 0) {

                    m.writer().writeShort(data.length);

                    m.writer().write(data);

                } else {

                    m.writer().writeShort(0);

                }

                sendMessage(m);

                m.cleanup();

            } else if (type == 1) {

                if (size > 0) {

                    p.rms[id] = new byte[size];

                    for (int i = 0; i < p.rms[id].length; i++) {

                        p.rms[id][i] = m2.reader().readByte();

                    }

                } else {

                    p.rms[id] = new byte[0];

                }

            }

        } catch (Exception e) {

            // e.printStackTrace();

        }

    }



    public void area_select(Message m2) throws IOException {

        Player p = this.player;

        if (p == null || p.map == null || p.map.template == null) return;

        if (WorldWar.runnning && !WorldWar.mapTranChienLon(p.map.template.id)) {

            send_box_ThongBao_OK( "Không thể thực hiện thao tác này khi đang diễn ra lễ hội");

            return;

        }



        m2.reader().readByte();

        byte select = m2.reader().readByte();

        Zone[] map_enter = Zone.getMapByID(p.map.template.id);

        if (map_enter == null || select < 0 || select >= map_enter.length || map_enter[select] == null) {
            send_box_ThongBao_OK("Khu vực không hợp lệ!");
            return;
        }

        if (map_enter[select].equals(p.map)) {

            send_box_ThongBao_OK( "Hiện tại đang ở khu này");

            return;

        }

        // Kiểm tra giới hạn cấp độ +-10 so với Siêu Trùm khi vào khu ở Open Mode
        zabstracts.AbsBoss sBoss = boss.SuperBossManager.getActiveSuperBossInZone(map_enter[select]);
        if (sBoss != null && !boss.SuperBossManager.checkLevelRequirement(p, sBoss)) {
            String bossName = (sBoss.mtemplate != null) ? sBoss.mtemplate.name : "Siêu Trùm";
            int bossLv = sBoss.getLevel();
            send_box_ThongBao_OK("Khu vực này đang có Siêu Trùm " + bossName + " (Cấp " + bossLv + ")!\n"
                    + "Giới hạn cấp độ vào khu: Cấp " + Math.max(1, bossLv - 10) + " - " + (bossLv + 10) + ".\n"
                    + "Cấp hiện tại của bạn: " + p.level + " (không đạt yêu cầu)!");
            return;
        }

        boolean isUnlimit = false;

        if (event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025) && SuKienHalloween.gI().isUnlimitMapBoss && select == 4

                && map_enter[select].template.id == 2) {

            isUnlimit = true;

        }

        if (!isUnlimit && map_enter[select].getNumPlayerSlot() >= map_enter[select].template.max_player) {

            send_box_ThongBao_OK( "Hiện tại khu vực đã đầy, hãy thử lại sau!");

            return;

        }

        p.map.leave_map(p, 1);

        p.map = map_enter[select];

        p.map.enter_map(p);

        p.map.enter_zone(p);

    }



    public void pet(Player p, boolean save_cache) throws IOException {

        Player p0 = p;

        MyPet pet_select = p0.get_pet();

        if (pet_select != null && !p0.isSpectator) {

            Message m = new Message(-80);

            m.writer().writeByte(0);

            m.writer().writeShort(0);

            m.writer().writeShort(p0.index_map);

            m.writer().writeShort(pet_select.template.frame); // 977

            m.writer().writeByte(pet_select.template.type); // 5

            if (save_cache) {

                this.player.msgs.add(m);

            } else {

                sendMessage(m);

            }

            m.cleanup();

        } else {

            Message m = new Message(-80);

            m.writer().writeByte(1);

            m.writer().writeShort(-1);

            m.writer().writeShort(p0.index_map);

            if (save_cache) {

                this.player.msgs.add(m);

            } else {

                sendMessage(m);

            }

            m.cleanup();

        }

    }



    public void login_ok(boolean save_cache) throws IOException {

        Player p = this.player;

        if (p != null) {

            bot.mercenary.MercenaryManager.gI().loadPlayerContracts(p);

        }

        Message m = new Message(-2);

        if (save_cache) {

            p.msgs.add(m);

        } else {

            sendMessage(m);

        }

        m.cleanup();

    }



    public void checkPlayInMap(Message m2) {

        Player p = this.player;

        try {

            // short id_p =

            m2.reader().readShort();

            // System.out.println("player id " + id_p);

        } catch (IOException e) {

            System.err.println("checkPlayInMap faill " + p.map.template.id);

            // e.printStackTrace();

        }

    }



    public void buy_item(Message m2) throws IOException {
        Player p = this.player;
        if (p == null || p.item == null) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể mua đồ khi đang giao dịch!");
            end_Dialog();
            return;
        }

        byte TypeShop = m2.reader().readByte();

        short id = m2.reader().readShort();

        short value = m2.reader().readShort();

        byte cat = -1;

        if (TypeShop == 116 || TypeShop == 118) {

            cat = m2.reader().readByte();

        }

        if (value <= 0 || value > DataTemplate.MAX_ITEM_IN_BAG) {

            send_box_ThongBao_OK( "Số lượng không hợp lệ!");

            return;

        }



        // 1. Kiểm tra Event Shop trước (nếu mở từ sự kiện)
        if (TypeShop == 118 && p.isShopSk && p.typeShop > 0) {
            event.Event ev = event.EventManager.gI().getEvent(p.typeShop);
            if (ev != null) {
                ev.buyShop(p, id, cat);
                return;
            }
        }

        // 2. Shop đặc thù (AbsShop: ClanShop 110 / LimitShop 118, 8, 12 / DaThanThoaiShop 116)
        if ((TypeShop == 110 || TypeShop == 118 || TypeShop == 8 || TypeShop == 12) && !p.isShopSk) {
            zabstracts.AbsShop shop = zabstracts.AbsShop.get(p.typeShop > 0 ? p.typeShop : TypeShop);
            if (shop != null) {
                shop.buy(p, cat, id, value);
                return;
            }
        } else if (TypeShop == 116) {
            zabstracts.AbsShop shop = zabstracts.AbsShop.get(TypeShop);
            if (shop != null) {
                shop.buy(p, cat, id, value);
                return;
            }
        }

        if (TypeShop == 118 && p.typeShop > 0) {
            EventManager.dispatchBuyShop(p, id, cat);
            return;
        }



        

        boolean check = false;

        if (cat == -1 && TypeShop >= 0 && TypeShop < 5) {

            List<ItemSell> list_sell = ItemSell.get_it_sell(p.level, TypeShop);

            for (int i = 0; i < list_sell.size(); i++) {

                ItemSell temp_sell = list_sell.get(i);

                if (temp_sell != null && temp_sell.id == id) {

                    if (p.item.able_bag() > 0) {

                        if (p.get_vang() < temp_sell.price) {

                            send_box_ThongBao_OK(

                                    "Bạn không đủ " + temp_sell.price + " beri!");

                            return;

                        }

                        p.update_vang(-temp_sell.price);

                        p.updateMoney();

                        Item_wear it_add = new Item_wear();

                        it_add.setup_template_by_id(temp_sell.id);

                        if (it_add.template != null) {

                            p.item.add_item_bag3(it_add);

                        }

                        p.item.updateInventory(false);

                        check = true;

                    } else {

                        send_box_ThongBao_OK( "Hành trang không đủ chỗ trống!");

                        return;

                    }

                    break;

                }

            }

            //

            if (check) {

                end_Dialog();

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua 1");

                p.addmsg(m22);

                m22.cleanup();

            } else {

                send_box_ThongBao_OK( "Mua thất bại, hãy thử lại!");

            }



        } else if (cat == -1 && TypeShop == 20) {

            if (ItemSell.check_item_sell_potion(p, id)) {

                if (id == 190 || id == 388) {
                    execute_buy_potion(p, id, 1);
                    return;
                }

                execute_buy_potion(p, id, value);

            } else if (id >= 685 && id <= 688) {

                if (value != 1) {

                    value = 1;

                }

                ItemTemplate4 it_template = ItemTemplate4.get_it_by_id(id);

                if (it_template != null) {

                    int vang_req = it_template.ruby * value;

                    MyPet pet = null;

                    for (int i = 0; i < p.my_pet.size(); i++) {

                        if (p.my_pet.get(i).id == it_template.id) {

                            pet = p.my_pet.get(i);

                            break;

                        }

                    }

                    if (pet != null) {

                        send_box_ThongBao_OK( "Đã sở hữu thú cưng này");

                        return;

                    }

                    if (vang_req > 0) {

                        if (p.get_ngoc() < vang_req) {

                            send_box_ThongBao_OK( "Không đủ " + vang_req + " ruby");

                            return;

                        }

                        p.update_ngoc(-vang_req);

                    } else {

                        vang_req = it_template.beri * value;

                        if (vang_req <= 0) {

                            return;

                        }

                        if (p.get_vang() < vang_req) {

                            send_box_ThongBao_OK( "Không đủ " + vang_req + " beri");

                            return;

                        }

                        p.update_vang(-vang_req);

                    }

                    p.updateMoney();

                    //

                    if (pet == null) {

                        pet = new MyPet();

                        pet.id = it_template.id;

                        pet.isUse = false;

                        pet.template = Pet.getTemplate(pet.id);

                        pet.time = System.currentTimeMillis() + 60_000L * 60 * 24 * 3;

                        p.my_pet.add(pet);

                        Message m22 = new Message(-64);

                        m22.writer().writeUTF("Mua 1");

                        p.addmsg(m22);

                        m22.cleanup();

                    }

                } else {

                    send_box_ThongBao_OK( "Có lỗi xảy ra, hãy báo cho admin!");

                }

            }

        } else if (cat == -1 && TypeShop == 6) {

            if (ItemSell.check_item_sell_material(p, id)) {

                execute_buy_material(p, id, value);

            }

        } else if (cat == -1 && TypeShop == 103) {

            ItemHair ith = ItemHair.get_item(id, 103);

            if (ith != null) {

                if (p.check_itfashionP(ith.ID, 103) != null) {

                    send_box_ThongBao_OK( "Đã mua rồi!");

                    return;

                }



                if (id >= 62 && id <= 67 && p.item.total_item_bag_by_id(4, 771) < 1) {

                    send_box_ThongBao_OK( "Bạn không có vé mua tóc đặc biệt");

                    return;

                }



                ItemHair itTemp = ItemHair.get_item(ith.ID, 103);

                if (itTemp.beri > 0) {

                    if (p.get_vang() < itTemp.beri) {

                        send_box_ThongBao_OK( "Không đủ " + itTemp.beri + " beri!");

                        return;

                    }

                    p.update_vang(-itTemp.beri);

                } else if (itTemp.ruby > 0) {

                    if (p.get_ngoc() < itTemp.ruby) {

                        send_box_ThongBao_OK( "Không đủ " + itTemp.ruby + " ruby!");

                        return;

                    }

                    p.update_ngoc(-itTemp.ruby);

                }



                p.updateMoney();

                ItemFashionP temp_new = new ItemFashionP(ith.ID, ith.idIcon, 103, false);

                p.itfashionP.add(temp_new);



                if (p.detu != null) {

                    ItemFashionP temp_new2 = new ItemFashionP(ith.ID, ith.idIcon, 103, false);

                    p.detu.itfashionP.add(temp_new2);

                }



                p.update_itfashionP(temp_new, 103);

                if (p.map != null && p.map.players != null) {

                    for (int i = 0; i < p.map.players.size(); i++) {

                        Player p0 = p.map.players.get(i);

                        if (p0 != null && p0.getService() != null) {

                            p0.getService().charWearing(p, false);

                        }

                    }

                }



                ItemFashionP.show_table(p, 103);

                end_Dialog();

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua 1");

                p.addmsg(m22);

                m22.cleanup();

            } else {

                send_box_ThongBao_OK( "Mua thất bại, hãy thử lại!");

            }

        } else if (cat == -1 && (TypeShop == 112 || TypeShop == 108)) {

            ItemHair ith = ItemHair.get_item(id, 108);

            if (ith != null) {

                if (p.check_itfashionP(ith.ID, 108) != null) {

                    send_box_ThongBao_OK( "Đã mua rồi!");

                    return;

                }

                ItemHair itTemp = ItemHair.get_item(ith.ID, 108);

                if (itTemp.beri > 0) {

                    if (p.get_vang() < itTemp.beri) {

                        send_box_ThongBao_OK("Không đủ " + itTemp.beri + " beri!");

                        return;

                    }

                    p.update_vang(-itTemp.beri);

                } else if (itTemp.ruby > 0) {

                    if (p.get_ngoc() < itTemp.ruby) {

                        send_box_ThongBao_OK("Không đủ " + itTemp.ruby + " ruby!");

                        return;

                    }

                    p.update_ngoc(-itTemp.ruby);

                }

                p.updateMoney();

                ItemFashionP temp_new = new ItemFashionP(ith.ID, ith.idIcon, 108, false);

                p.itfashionP.add(temp_new);



                if (p.detu != null) {

                    ItemFashionP temp_new2 = new ItemFashionP(ith.ID, ith.idIcon, 108, false);

                    p.detu.itfashionP.add(temp_new2);

                }



                p.update_itfashionP(temp_new, 108);

                if (p.map != null && p.map.players != null) {

                    for (int i = 0; i < p.map.players.size(); i++) {

                        Player p0 = p.map.players.get(i);

                        if (p0 != null && p0.getService() != null) {

                            p0.getService().charWearing(p, false);

                        }

                    }

                }

                ItemFashionP.show_table(p, 108);

                end_Dialog();

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua 1");

                p.addmsg(m22);

                m22.cleanup();

            } else {

                send_box_ThongBao_OK( "Mua thất bại, hãy thử lại!");

            }

        } else if (cat == -1 && TypeShop == 105) {

            ItemFashion itf = ItemFashion.get_item(id);

            if (itf != null) {

                if (!Manager.gI().isTestMode() && itf.price <= 0) {
                    send_box_ThongBao_OK("Vật phẩm thời trang này hiện chưa được mở bán!");
                    return;
                }

                int fashionPrice = itf.price > 0 ? itf.price : 500;

                if (p.get_ngoc() < fashionPrice) {

                    send_box_ThongBao_OK( "Không đủ " + fashionPrice + " ruby!");

                    return;

                }

                if (p.check_fashion(itf.ID) != null) {

                    send_box_ThongBao_OK( "Đã mua rồi!");

                    return;

                }

                p.update_ngoc(-fashionPrice);

                p.updateMoney();

                ItemFashionP2 temp2 = new ItemFashionP2();

                temp2.id = itf.ID;

                if (itf.hsd > -1) {

                    temp2.expires = System.currentTimeMillis() + 60_000L * 60 * 24 * itf.hsd;

                } else {

                    temp2.expires = -1;

                }

                p.fashion.add(temp2);

                p.update_fashionP2(temp2);

                //

                if (p.detu != null) {

                    ItemFashionP2 temp3 = new ItemFashionP2();

                    temp3.id = temp2.id;

                    temp3.is_use = false;

                    temp3.level = temp2.level;

                    temp3.expires = temp2.expires;

                    p.detu.fashion.add(temp3);

                }



                //

                if (p.map != null && p.map.players != null) {

                    for (int i = 0; i < p.map.players.size(); i++) {

                        Player p0 = p.map.players.get(i);

                        if (p0 != null && p0.getService() != null) {

                            p0.getService().charWearing(p, false);

                        }

                    }

                }

                p.getService().UpdateInfoMaincharInfo();

                ItemFashionP.show_table(p, 105);

                // inline expires was set on temp2



                end_Dialog();

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua 1");

                p.addmsg(m22);

                m22.cleanup();

            } else {

                send_box_ThongBao_OK( "Mua thất bại, hãy thử lại!");

            }

        } else if (cat == -1 && TypeShop == 111) {

            execute_buy_stone(p, id, value);

        } else if (cat == -1 && TypeShop == 102) {

            ItemBoat itb = ItemBoat.get_item(id);

            if (itb != null) {

                if (p.check_itboat(itb.id) != null) {

                    send_box_ThongBao_OK( "Đã mua rồi!");

                    return;

                }

                if (p.get_ngoc() < 5) {

                    send_box_ThongBao_OK( "Không đủ 5 ruby!");

                    return;

                }

                p.update_ngoc(-5);

                p.updateMoney();

                ItemBoatP temp_new = new ItemBoatP();

                temp_new.id = itb.id;

                temp_new.is_use = true;

                p.itemboat.add(temp_new);

                p.update_new_part_boat(temp_new);

                ItemBoat.update_part_boat_when_shopping(p);

                ItemFashionP.show_table(p, 102);

                end_Dialog();

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua 1");

                p.addmsg(m22);

                m22.cleanup();

            } else {

                send_box_ThongBao_OK( "Mua thất bại, hãy thử lại!");

            }

        } else if (p.clan != null && TypeShop == 98 && value == 1 && cat == -1 && id >= 0

                && id < 10) {

            p.clan.icon = id;

            p.clan.hanhtrinhIcon = null;

            Clan.send_info(p, false);

            p.syncFullPlayerStats();

            for (int i = 0; i < p.map.players.size(); i++) {

                if (!p.map.players.get(i).equals(p)) {

                    Clan.send_me_to_other(p, p.map.players.get(i), false);

                }

            }

            Message m = new Message(-52);

            m.writer().writeByte(21);

            m.writer().writeUTF("Đăng ký băng hải tặc " + p.clan.name + " thành công");

            sendMessage(m);

            m.cleanup();

        } else if (p.clan != null && TypeShop == 97 && value == 1 && cat == -1) {
            if (p.clan.members == null || p.clan.members.isEmpty() || !p.clan.members.get(0).name.equals(p.name)) {
                send_box_ThongBao_OK("Chỉ thuyền trưởng mới có quyền thay đổi biểu tượng băng!");
                return;
            }
            ClanIcon clanIcon = ClanIcon.GetById(id);
            if (clanIcon != null) {
                int ngoc_quant = clanIcon.price;
                if (p.clan.get_ngoc() < ngoc_quant) {
                    send_box_ThongBao_OK("Không đủ " + ngoc_quant + " ruby băng");
                } else {
                    if (ngoc_quant > 0) {
                        p.clan.update_ruby(-ngoc_quant);
                    }
                    p.clan.icon = id;
                    p.clan.hanhtrinhIcon = null;
                    if (clan.ClanHanhTrinhIcon.ENTRY != null) {
                        for (clan.ClanHanhTrinhIcon hti : clan.ClanHanhTrinhIcon.ENTRY) {
                            if (hti != null && (hti.icon == id || hti.id == id)) {
                                p.clan.hanhtrinhIcon = hti;
                                break;
                            }
                        }
                    }
                    clan.ClanService.broadcastInfo(p.clan);
                    for (int i2 = 0; i2 < p.clan.members.size(); i2++) {
                        Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i2).name);
                        if (p0 != null) {
                            p0.syncFullPlayerStats();
                            if (p0.map != null) {
                                for (int i = 0; i < p0.map.players.size(); i++) {
                                    if (!p0.map.players.get(i).equals(p0)) {
                                        Clan.send_me_to_other(p0, p0.map.players.get(i), false);
                                    }
                                }
                            }
                        }
                    }
                    Message m = new Message(-52);
                    m.writer().writeByte(21);
                    m.writer().writeUTF("Đổi biểu tượng băng thành công");
                    sendMessage(m);
                    m.cleanup();
                    send_box_ThongBao_OK("Đã đổi biểu tượng băng thành: " + clanIcon.name);
                }
            } else if (id >= 0 && id < 10) {
                p.clan.icon = id;
                p.clan.hanhtrinhIcon = null;
                clan.ClanService.broadcastInfo(p.clan);
                for (int i2 = 0; i2 < p.clan.members.size(); i2++) {
                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i2).name);
                    if (p0 != null) {
                        p0.syncFullPlayerStats();
                        if (p0.map != null) {
                            for (int i = 0; i < p0.map.players.size(); i++) {
                                if (!p0.map.players.get(i).equals(p0)) {
                                    Clan.send_me_to_other(p0, p0.map.players.get(i), false);
                                }
                            }
                        }
                    }
                }
                Message m = new Message(-52);
                m.writer().writeByte(21);
                m.writer().writeUTF("Đổi biểu tượng băng thành công");
                sendMessage(m);
                m.cleanup();
                send_box_ThongBao_OK("Đã đổi sang cờ mặc định số " + (id + 1));
            } else {
                send_box_ThongBao_OK("Biểu tượng không tồn tại hoặc chưa mở bán!");
            }

        } else if (p.clan != null && p.clan.members.get(0).name.equals(p.name) && TypeShop == 110

                && value <= 20 && value > 0 && cat == -1) {

            if (id == 21) {

                value = 1;

            }

            check = false;

            for (int i = 0; i < ItemTemplate8.ENTRYS.size(); i++) {

                if (ItemTemplate8.ENTRYS.get(i).id == id) {

                    if (ItemTemplate8.ENTRYS.get(i).ruby > 0) {

                        long totalCostLong = (long) ItemTemplate8.ENTRYS.get(i).ruby * value;
                        if (totalCostLong < 0 || totalCostLong > Integer.MAX_VALUE) {
                            end_Dialog();
                            send_box_ThongBao_OK("Số lượng quá lớn!");
                            return;
                        }
                        int vang_req = (int) totalCostLong;

                        if (p.clan.get_ngoc() < vang_req) {
                            end_Dialog();
                            send_box_ThongBao_OK( "Không đủ " + vang_req + " ruby băng");

                            return;

                        }

                        if (id == 21) {

                            EventData temp_select = null;

                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {

                                if (p.eventData.get(i2).eventID == 9) {

                                    temp_select = p.eventData.get(i2);

                                    break;

                                }

                            }

                            if (temp_select != null && temp_select.data[SuKienTrongCay.IDX_NUOC_TUOI_BANG] < 5) {

                                temp_select.data[SuKienTrongCay.IDX_NUOC_TUOI_BANG]++;

                            } else {
                                end_Dialog();
                                send_box_ThongBao_OK( "Hôm nay đã mua tối đa 5/5");

                                return;

                            }

                        }

                        p.clan.update_ruby(-vang_req);

                        ItemBag47 it_add = null;

                        for (int j = 0; j < p.clan.list_it.size(); j++) {

                            if (p.clan.list_it.get(j).id == id) {

                                it_add = p.clan.list_it.get(j);

                                break;

                            }

                        }

                        if (it_add == null) {

                            it_add = new ItemBag47();

                            it_add.category = 8;

                            it_add.id = id;

                            it_add.quant = 0;

                            p.clan.list_it.add(it_add);

                        }

                        it_add.quant += value;

                        check = true;

                    } else if (ItemTemplate8.ENTRYS.get(i).beri > 0) {

                        long totalCostLong = (long) ItemTemplate8.ENTRYS.get(i).beri * value;
                        if (totalCostLong < 0 || totalCostLong > Integer.MAX_VALUE) {
                            end_Dialog();
                            send_box_ThongBao_OK("Số lượng quá lớn!");
                            return;
                        }
                        int vang_req = (int) totalCostLong;

                        if (p.clan.get_vang() < vang_req) {
                            end_Dialog();
                            send_box_ThongBao_OK( "Không đủ " + vang_req + " beri băng");

                            return;

                        }

                        p.clan.update_beri(-vang_req);

                        ItemBag47 it_add = null;

                        for (int j = 0; j < p.clan.list_it.size(); j++) {

                            if (p.clan.list_it.get(j).id == id) {

                                it_add = p.clan.list_it.get(j);

                                break;

                            }

                        }

                        if (it_add == null) {

                            it_add = new ItemBag47();

                            it_add.category = 8;

                            it_add.id = id;

                            it_add.quant = 0;

                            p.clan.list_it.add(it_add);

                        }

                        it_add.quant += value;

                        check = true;

                    } else {
                        end_Dialog();
                        send_box_ThongBao_OK( "Vật phẩm chưa bán");

                    }

                    break;

                }

            }

            if (check) {

                for (int i = 0; i < p.clan.members.size(); i++) {

                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);

                    if (p0 != null) {

                        Clan.send_money(p0, false);

                        p0.clan.send_inventory(p0, false);

                    }

                }

                Message m22 = new Message(-64);

                m22.writer().writeUTF("Mua " + value);

                p.addmsg(m22);

                m22.cleanup();
                end_Dialog();

            } else {
                end_Dialog();
            }

        } else if (TypeShop == 118 && value == 1 && p.typeShop > 0) {

            zabstracts.AbsShop accShop = zabstracts.AbsShop.get(p.typeShop);

            if (accShop != null) {

                accShop.buy(p, cat, id, 1);

            } else {

                EventManager.dispatchBuyShop(p, id, cat);

            }

        } else if (TypeShop == 119 && value == 1 && cat == -1 && id >= 0

                && id < p.item.save_item_wear.size()) {

            Item_wear it_select = p.item.save_item_wear.get(id);

            if (it_select != null) {

                if (p.get_ngoc() < 5) {

                    send_box_ThongBao_OK( "Không đủ 5 ruby");

                    return;

                }

                if (p.item.able_bag() > 0) {
                    p.item.save_item_wear.remove((int) id);
                    if (p.item.add_item_bag3(it_select)) {
                        p.update_ngoc(-5);
                        p.updateMoney();
                        p.item.updateInventory(false);
                        Send_UI_Shop(119);
                        send_box_ThongBao_OK(
                                "Lấy " + it_select.template.name + " về thành công, phí 5 ruby");
                    } else {
                        p.item.save_item_wear.add((int) id, it_select);
                        send_box_ThongBao_OK("Không thể lấy vật phẩm vào hành trang!");
                    }
                } else {
                    send_box_ThongBao_OK("Hành trang đầy");
                }
            }
        } else if (TypeShop == 107 && value == 1 && cat == -1) { // huy hieu hanh trinh
            ClanHanhTrinhIcon iconSelect = ClanHanhTrinhIcon.getById(id);
            if (iconSelect != null) {
                boolean have = false;
                for (int i = 1; i < p.clan.hanhtrinh.size(); i++) {
                    if (p.clan.hanhtrinh.get(i) == iconSelect.id) {
                        have = true;
                        break;
                    }
                }
                if (have) {
                    p.clan.icon = iconSelect.icon;
                    p.clan.hanhtrinhIcon = iconSelect;
                    for (int i2 = 0; i2 < p.clan.members.size(); i2++) {
                        Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i2).name);
                        if (p0 != null) {
                            Clan.send_info(p0, false);
                            for (int i = 0; i < p0.map.players.size(); i++) {
                                if (!p0.map.players.get(i).equals(p0)) {
                                    Clan.send_me_to_other(p0, p0.map.players.get(i), false);
                                }
                            }
                        }
                    }
                    Message m = new Message(-52);
                    m.writer().writeByte(21);
                    m.writer().writeUTF("Đổi icon băng thành công");
                    sendMessage(m);
                    m.cleanup();
                    send_box_ThongBao_OK( "Sử dụng huy hiệu hành trình " + iconSelect.name);
                } else {
                    send_box_ThongBao_OK( "Chưa sở hữu huy hiệu này!");
                }
            }
        }
    }

    public void execute_buy_potion(Player p, short id, int value) throws IOException {
        if (p == null || p.item == null || value < 1 || value > 9999) {
            if (p != null) {
                end_Dialog();
                send_box_ThongBao_OK("Số lượng không hợp lệ!");
            }
            return;
        }

        if (id == 190 || id == 388) {
            value = 1;
        }

        if (!p.item.can_add_item_bag47(4, id, value)) {
            end_Dialog();
            send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return;
        }

        ItemTemplate4 it_template = ItemTemplate4.get_it_by_id(id);
        if (it_template == null) {
            end_Dialog();
            send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        if (SuKienNoel.gI().isNoel()) {
            int numVe = 0;
            if (id == 488) {
                numVe = 15;
            } else if (id == 486 || id == 487 || id == 492) {
                numVe = 5;
            }
            if (numVe != 0) {
                long totalVeReq = (long) numVe * value;
                if (p.item.total_item_bag_by_id(4, SuKienNoel.ITEM_VE_NOEL) < totalVeReq) {
                    end_Dialog();
                    send_box_ThongBao_OK("Bạn không đủ " + totalVeReq + " vé Noel");
                    return;
                }
                p.item.remove_item47(4, SuKienNoel.ITEM_VE_NOEL, (int) totalVeReq);
                p.item.add_item_bag47(4, id, value);
                Message m22 = new Message(-64);
                m22.writer().writeUTF("Mua " + value);
                p.addmsg(m22);
                m22.cleanup();
                p.item.updateInventory(false);
                p.updateMoney();
                end_Dialog();
                return;
            }
        }

        long totalCostLong;
        boolean useRuby = it_template.ruby > 0;
        if (useRuby) {
            totalCostLong = (long) it_template.ruby * value;
        } else {
            totalCostLong = (long) it_template.beri * value;
        }

        if (totalCostLong < 0 || totalCostLong > Integer.MAX_VALUE) {
            end_Dialog();
            send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
            return;
        }

        int vang_req = (int) totalCostLong;

        if (useRuby) {
            if (p.get_ngoc() < vang_req) {
                end_Dialog();
                send_box_ThongBao_OK("Không đủ " + vang_req + " ruby");
                return;
            }
            if (id == 388) {
                EventData temp_select = null;
                for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                    if (p.eventData.get(i2).eventID == 9) {
                        temp_select = p.eventData.get(i2);
                        break;
                    }
                }
                if (temp_select != null && temp_select.data[SuKienTrongCay.IDX_CAY_GIONG_MUA] < 1000) {
                    temp_select.data[SuKienTrongCay.IDX_CAY_GIONG_MUA]++;
                } else {
                    end_Dialog();
                    send_box_ThongBao_OK("Hôm nay đã mua tối đa 1000/1000");
                    return;
                }
            }
            p.update_ngoc(-vang_req);
        } else {
            if (vang_req <= 0) {
                end_Dialog();
                return;
            }
            if (p.get_vang() < vang_req) {
                end_Dialog();
                send_box_ThongBao_OK("Không đủ " + vang_req + " beri");
                return;
            }
            p.update_vang(-vang_req);
        }

        if (id == 43) {
            p.update_pvp_ticket(value);
            CountDown_Ticket();
        } else if (id == 40) {
            p.update_key_boss(value);
            CountDown_Ticket();
        } else if (id == 6) {
            p.update_ticket(value);
            CountDown_Ticket();
        } else {
            p.item.add_item_bag47(4, id, value);
            Message m22 = new Message(-64);
            m22.writer().writeUTF("Mua " + value);
            p.addmsg(m22);
            m22.cleanup();
            p.item.updateInventory(false);
        }

        p.updateMoney();
    }

    public void execute_buy_material(Player p, short id, int value) throws IOException {
        if (p == null || p.item == null || value < 1 || value > 9999) {
            if (p != null) {
                end_Dialog();
                send_box_ThongBao_OK("Số lượng không hợp lệ!");
            }
            return;
        }

        if (!p.item.can_add_item_bag47(7, id, value)) {
            end_Dialog();
            send_box_ThongBao_OK("Hành trang đầy!");
            return;
        }

        ItemTemplate7 it_template = ItemTemplate7.get_it_by_id(id);
        if (it_template == null) {
            end_Dialog();
            send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        long totalCostLong;
        boolean useRuby = it_template.priceruby > 0;
        if (useRuby) {
            totalCostLong = (long) it_template.priceruby * value;
        } else {
            totalCostLong = (long) it_template.price * value;
        }

        if (totalCostLong < 0 || totalCostLong > Integer.MAX_VALUE) {
            end_Dialog();
            send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
            return;
        }

        int vang_req = (int) totalCostLong;

        if (useRuby) {
            if (p.get_ngoc() < vang_req) {
                end_Dialog();
                send_box_ThongBao_OK("Không đủ " + vang_req + " ruby");
                return;
            }
            p.update_ngoc(-vang_req);
        } else {
            if (vang_req <= 0) {
                end_Dialog();
                return;
            }
            if (p.get_vang() < vang_req) {
                end_Dialog();
                send_box_ThongBao_OK("Không đủ " + vang_req + " beri");
                return;
            }
            p.update_vang(-vang_req);
        }

        p.item.add_item_bag47(7, id, value);
        p.item.updateInventory(false);
        p.updateMoney();

        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua " + value);
        p.addmsg(m22);
        m22.cleanup();
    }

    public void execute_buy_stone(Player p, short id, int value) throws IOException {
        if (p == null || p.item == null || value < 1 || value > 9999) {
            if (p != null) {
                end_Dialog();
                send_box_ThongBao_OK("Số lượng không hợp lệ!");
            }
            return;
        }

        int targetItemId = id;
        int targetQuant = value;
        if (id >= 272 && id <= 277) {
            targetItemId = 46 + (id - 272) * 6;
            long totalPackQuant = 100L * value;
            if (totalPackQuant > Integer.MAX_VALUE) {
                end_Dialog();
                send_box_ThongBao_OK("Số lượng quá lớn!");
                return;
            }
            targetQuant = (int) totalPackQuant;
        }

        if (!p.item.can_add_item_bag47(4, targetItemId, targetQuant)) {
            end_Dialog();
            send_box_ThongBao_OK("Hành trang đầy!");
            return;
        }

        boolean found = false;
        for (int i = 0; i < Rebuild_Item.ID_SELL.length; i++) {
            if (Rebuild_Item.ID_SELL[i] == id) {
                found = true;
                ItemTemplate4 it_temp = ItemTemplate4.get_it_by_id(id);
                if (it_temp != null) {
                    long totalCostLong;
                    boolean useRuby = it_temp.ruby > 0;
                    if (useRuby) {
                        totalCostLong = (long) it_temp.ruby * value;
                    } else {
                        totalCostLong = (long) it_temp.beri * value;
                    }

                    if (totalCostLong < 0 || totalCostLong > Integer.MAX_VALUE) {
                        end_Dialog();
                        send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
                        return;
                    }

                    int vang_req = (int) totalCostLong;

                    if (useRuby) {
                        if (p.get_ngoc() < vang_req) {
                            end_Dialog();
                            send_box_ThongBao_OK("Không đủ " + vang_req + " ruby");
                            return;
                        }
                        p.update_ngoc(-vang_req);
                    } else {
                        if (vang_req <= 0) {
                            end_Dialog();
                            return;
                        }
                        if (p.get_vang() < vang_req) {
                            end_Dialog();
                            send_box_ThongBao_OK("Không đủ " + vang_req + " beri");
                            return;
                        }
                        p.update_vang(-vang_req);
                    }

                    if (p.item.add_item_bag47(4, targetItemId, targetQuant)) {
                        Message m22 = new Message(-64);
                        m22.writer().writeUTF("Mua " + value);
                        p.addmsg(m22);
                        m22.cleanup();
                    } else {
                        if (useRuby) {
                            p.update_ngoc(vang_req);
                        } else {
                            p.update_vang(vang_req);
                        }
                        p.updateMoney();
                        end_Dialog();
                        send_box_ThongBao_OK("Không thể mua với số lượng này");
                        return;
                    }

                    p.item.updateInventory(false);
                    p.updateMoney();
                } else {
                    end_Dialog();
                    send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
                }
                break;
            }
        }
        if (!found) {
            end_Dialog();
            send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
        }
    }

    @Override
    public void send_box_ThongBao_OK(String notice) {
        Player p = this.player;
        try {
            Message m = new Message(-11);
            m.writer().writeShort(0);
            m.writer().writeByte(0);
            m.writer().writeUTF("Thông Báo");
            m.writer().writeUTF(notice);
            m.writer().writeByte(0);
            sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    @Override
    public void sendThongBao(String notice) {
        sendThongBao(notice, 0);
    }

    @Override
    public void sendThongBao(String notice, int color) {
        Player p = this.player;
        if (p == null || notice == null) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0);
            m.writer().writeUTF(notice);
            m.writer().writeByte(color >= 0 ? color : 0);
            m.writer().writeShort(-1);
            sendMessage(m);
            m.cleanup();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void end_Dialog() {
        try {
            Message m = new Message(47);
            m.writer().writeByte(1);
            sendMessage(m);
            m.cleanup();
        } catch (Exception ignored) {
        }
    }

    public void ChestWanted(boolean save_cache) throws IOException {
        Player p = this.player;
        Message m = new Message(-86);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        if (save_cache) {
            p.msgs.add(m);
        } else {
            sendMessage(m);
        }
        m.cleanup();
    }

    @Override
    public void use_potion(int healHp, int healMp) throws IOException {
        Player p = this.player;
        if (p == null) return;
        int hp_max = p.ability.get_hp_max(true);
        int mp_max = p.ability.get_mp_max(true);
        if (healHp > 0) {
            p.hp = Math.min(hp_max, p.hp + healHp);
        } else if (healHp < 0) {
            p.hp = Math.max(1, p.hp + healHp);
        }
        if (healMp > 0) {
            p.mp = Math.min(mp_max, p.mp + healMp);
        } else if (healMp < 0) {
            p.mp = Math.max(0, p.mp + healMp);
        }
        Message m = new Message(-83);
        m.writer().writeShort(p.index_map);
        m.writer().writeByte(0);
        m.writer().writeInt(hp_max); // maxhp
        m.writer().writeInt(p.hp); // hp remain
        m.writer().writeInt(healHp); // heal hp
        m.writer().writeInt(mp_max); // maxmp
        m.writer().writeInt(p.mp); // mp remain
        m.writer().writeInt(healMp); // heal mp
        if (p.map != null) {
            p.map.send_msg_all_p(m, p, true);
        } else if (p.conn != null) {
            p.conn.addmsg(m);
        }
        m.cleanup();
    }

    public void send_view_other_player(Player p) throws IOException {
        if (p == null) return;
        Player p0 = p;

        // Tự động chuẩn hóa và bảo đảm mọi dữ liệu của Bot đều hợp lệ
        if (p0.isBot || p0 instanceof bot.Bot) {
            if (p0.level <= 0) p0.level = 50;
            if (p0.clazz < 1 || p0.clazz > 5) p0.clazz = (byte) (core.ZUtil.random(1, 5));
            if (p0.item == null || p0.item.it_body == null || p0.item.it_body[0] == null) {
                int botTier = p0.level >= 90 ? 4 : (p0.level >= 75 ? 3 : (p0.level >= 50 ? 2 : 1));
                p0.setupBotEquip(p0.clazz, p0.level, botTier, p0.level >= 75, true, p0.level >= 60);
            }
            if (p0.ability == null) {
                p0.ability = new ability.Ability(p0);
            }
            try {
                p0.setAbility();
            } catch (Exception ignored) {}
            if (p0.hp <= 0 && p0.ability != null) {
                p0.hp = Math.max(1000, p0.ability.get_hp_max(true));
            }
            if (p0.mp <= 0 && p0.ability != null) {
                p0.mp = Math.max(500, p0.ability.get_mp_max(true));
            }
        }

        Message m = new Message(-42);
        m.writer().writeUTF(p0.name != null ? p0.name : "");
        int maxHp = 1000;
        int maxMp = 500;
        try {
            maxHp = (p0.ability != null) ? p0.ability.get_hp_max(true) : Math.max(1000, p0.hp);
            maxMp = (p0.ability != null) ? p0.ability.get_mp_max(true) : Math.max(500, p0.mp);
        } catch (Exception ignored) {
            maxHp = Math.max(1000, p0.hp);
            maxMp = Math.max(500, p0.mp);
        }
        m.writer().writeInt(Math.max(1, maxHp));
        m.writer().writeInt(Math.max(1, maxMp));
        m.writer().writeInt(Math.max(1, p0.hp > 0 ? p0.hp : maxHp));
        m.writer().writeInt(Math.max(1, p0.mp > 0 ? p0.mp : maxMp));
        m.writer().writeShort(Math.max((short) 1, p0.level));
        short percent = 0;
        try {
            percent = (short) p0.get_level_percent();
        } catch (Exception ignored) {}
        m.writer().writeShort(percent);
        m.writer().writeShort((short) p0.get_head());
        m.writer().writeShort((short) p0.get_hair());
        clan.Clan pClan = p0.clan;
        if (pClan == null && p0.name != null) {
            pClan = clan.Clan.get_my_clan(p0.name);
            if (pClan != null) p0.clan = pClan;
        }
        if (pClan != null && pClan.name != null && !pClan.name.isEmpty()) {
            m.writer().writeShort(pClan.id); // clan
            m.writer().writeShort(pClan.icon >= 0 ? pClan.icon : 0); // clan
            m.writer().writeUTF(pClan.name);
        } else {
            m.writer().writeShort(-1); // clan
        }
        m.writer().writeByte(16);
        for (int i = 0; i < 16; i++) {
            try {
                Item_wear it_w = (p0.item != null && p0.item.it_body != null && i < p0.item.it_body.length) ? p0.item.it_body[i] : null;
                if (i == 6 && p0.item != null && p0.item.it_heart != null) {
                    it_w = p0.item.it_heart;
                }
                if (it_w != null && it_w.template != null) {
                    if (it_w.option_item == null) it_w.option_item = new ArrayList<>();
                    if (it_w.option_item_2 == null) it_w.option_item_2 = new ArrayList<>();
                    if (it_w.mdakham == null) it_w.mdakham = new short[0];
                    m.writer().writeByte(1);
                    Item.readUpdateItem(m.writer(), it_w, p0);
                    m.writer().writeShort(p0.get_wearing_part(i));
                } else {
                    m.writer().writeByte(0);
                }
            } catch (Exception eSlot) {
                m.writer().writeByte(0);
            }
        }
        m.writer().writeByte(0);
        m.writer().writeShort(-1);
        byte fullSet = -1;
        try {
            fullSet = p0.get_index_full_set();
        } catch (Exception ignored) {}
        m.writer().writeByte(fullSet);
        sendMessage(m);
        m.cleanup();
    }

    public static boolean isEquippedOnBody(Player p, Item_wear it) {
        if (p == null || it == null) return false;
        if (p.item != null) {
            if (p.item.it_body != null) {
                for (Item_wear bodyIt : p.item.it_body) {
                    if (bodyIt != null && bodyIt == it) {
                        return true;
                    }
                }
            }
            if (p.item.it_heart != null && p.item.it_heart == it) {
                return true;
            }
        }
        if (p.detu != null && p.detu.item != null) {
            if (p.detu.item.it_body != null) {
                for (Item_wear bodyIt : p.detu.item.it_body) {
                    if (bodyIt != null && bodyIt == it) {
                        return true;
                    }
                }
            }
            if (p.detu.item.it_heart != null && p.detu.item.it_heart == it) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValuableItemToConfirm(byte cat, short id, Item_wear it_select, String name) {
        if (name == null) name = "";
        String lowerName = name.toLowerCase();

        // 1. Item +1 tro len (Trang bi)
        if (cat == 3) {
            if (it_select != null) {
                if (it_select.levelUp > 0) return true;
                if (it_select.isThanTrang()) return true;
                if (it_select.getColor() >= 3) return true;
                if (it_select.mdakham != null && it_select.mdakham.length > 0) return true;
                if (it_select.typelock == 1) return true;
            }
            return false;
        }

        // 2. Ruong Dai Ac Quy, Sieu Dai Ac Quy, Ruong Ac Quy
        if (cat == 4) {
            if (id == 158 || id == 911 || id == 29) return true;
            if (lowerName.contains("rương") && lowerName.contains("ác quỷ")) return true;
        }

        // 3. Trai Ac Quy trung tro len (Trung cap, Cao cap, Cuc pham)
        if (cat == 4) {
            int[] dfTrungTroLen = {32, 92, 93, 219, 870, 160, 161, 240, 427, 869, 873};
            for (int dfId : dfTrungTroLen) {
                if (id == dfId) return true;
            }
            if (lowerName.contains("trái") || lowerName.contains("ác quỷ")) {
                int[] dfSoCap = {33, 34, 88, 90, 91, 220, 316, 317, 318};
                boolean isSoCap = false;
                for (int scId : dfSoCap) {
                    if (id == scId) {
                        isSoCap = true;
                        break;
                    }
                }
                if (!isSoCap && (lowerName.contains("lửa") || lowerName.contains("băng") || lowerName.contains("cát") 
                        || lowerName.contains("báo") || lowerName.contains("tình yêu") || lowerName.contains("sét") 
                        || lowerName.contains("nham thạch") || lowerName.contains("chấn thiên") || lowerName.contains("bóng tối") 
                        || lowerName.contains("ánh sáng") || lowerName.contains("nika") || lowerName.contains("quỷ"))) {
                    return true;
                }
            }
        }

        // 4. Da cap 3 tro len (neu vut duoc)
        if (cat == 4 || cat == 7) {
            if (id == 9 || id == 134) return true; // Da Ac Quy
            boolean isDa = lowerName.contains("đá") || lowerName.contains("khảm") || lowerName.contains("thạch") || cat == 7;
            if (isDa) {
                if (lowerName.contains("cấp 3") || lowerName.contains("cấp 4") || lowerName.contains("cấp 5") 
                        || lowerName.contains("cấp 6") || lowerName.contains("cấp 7") || lowerName.contains("cấp 8") 
                        || lowerName.contains("cấp 9") || lowerName.contains("cấp 10") || lowerName.contains("siêu cấp") 
                        || lowerName.contains("vô cực") || lowerName.contains("truyền thuyết") || lowerName.contains("thời không") 
                        || lowerName.contains("thần")) {
                    return true;
                }
                if ((id >= 52 && id <= 55) || (id >= 70 && id <= 73) || (id >= 58 && id <= 61) 
                        || (id >= 64 && id <= 67) || (id >= 223 && id <= 226) || id == 212 || id == 216 
                        || (id >= 324 && id <= 326) || (id >= 644 && id <= 646) || id == 683 || id == 691 || id == 732) {
                    return true;
                }
            }
        }

        return false;
    }

    public void sell_item(Message m2) throws IOException {
        Player p = this.player;
        if (p == null || p.item == null) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }

        byte type = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short num = m2.reader().readShort();
        short stackIndex = -1;
        if (m2.reader().available() > 0) {
            stackIndex = m2.reader().readShort();
        }

        if (num <= 0 || num > DataTemplate.MAX_ITEM_IN_BAG) {
            return;
        }

        if (type == 1) { // VUT BO ITEM (DROP)
            Item_wear it_wear = null;
            String itemName = "";
            if (cat == 3) {
                if (id >= 0 && id < p.item.bag3.length && p.item.bag3[id] != null) {
                    it_wear = p.item.bag3[id];
                } else {
                    for (int i = 0; i < p.item.bag3.length; i++) {
                        Item_wear c = p.item.bag3[i];
                        if (c != null && c.template != null && (c.index == id || c.template.id == id)) {
                            it_wear = c;
                            id = (short) i;
                            break;
                        }
                    }
                }
                if (it_wear == null) {
                    send_box_ThongBao_OK("Trang bị không tồn tại trong hành trang!");
                    return;
                }
                if (isEquippedOnBody(p, it_wear) || it_wear == p.item.it_heart) {
                    send_box_ThongBao_OK("Không thể vứt bỏ trang bị đang mặc trên người hoặc Trái Tim!");
                    return;
                }
                itemName = it_wear.template.name + (it_wear.levelUp > 0 ? (" +" + it_wear.levelUp) : "");
            } else {
                itemName = (cat == 4 ? (ItemTemplate4.get_it_by_id(id) != null ? ItemTemplate4.get_it_by_id(id).name : "vật phẩm") 
                                     : (ItemTemplate7.get_it_by_id(id) != null ? ItemTemplate7.get_it_by_id(id).name : "vật phẩm"));
            }

            boolean needConfirm = isValuableItemToConfirm(cat, id, it_wear, itemName);
            if (needConfirm) {
                final short fId = id;
                final byte fCat = cat;
                final short fNum = num;
                final short fStackIndex = stackIndex;
                final String fItemName = itemName;
                StringBuilder sbDropMsg = new StringBuilder();
                sbDropMsg.append("Bạn có chắc chắn muốn vứt bỏ ").append(cat == 3 ? "" : (num + " ")).append(fItemName).append(" không?\n");
                if (cat == 3 && it_wear != null) {
                    if (it_wear.isThanTrang()) sbDropMsg.append("• CẢNH BÁO: Đây là Thần Trang cực kỳ quý hiếm!\n");
                    if (it_wear.levelUp > 0) sbDropMsg.append("• CẢNH BÁO: Trang bị đã được cường hóa +").append(it_wear.levelUp).append("!\n");
                    if (it_wear.mdakham != null && it_wear.mdakham.length > 0) sbDropMsg.append("• CẢNH BÁO: Trang bị đang được khảm ngọc!\n");
                    if (it_wear.typelock == 1) sbDropMsg.append("• Trang bị đang ở trạng thái Khóa.\n");
                }
                sbDropMsg.append("Vật phẩm quý giá sau khi vứt sẽ biến mất vĩnh viễn và không thể khôi phục!");

                model.YesNoDialog dialog = new model.YesNoDialog(p, 9999, "Xác Nhận Vứt Bỏ",
                    sbDropMsg.toString(), new String[]{"Chắc chắn", "Hủy"});
                dialog.setHandler(val -> {
                    if (val == 0) {
                        executeDropItem(p, fCat, fId, fNum, fStackIndex);
                    }
                });
                dialog.startYesNo();
                return;
            } else {
                executeDropItem(p, cat, id, num, stackIndex);
                return;
            }
        } else if (type == 0) { // BAN ITEM (SELL)
            switch (cat) {
                case 3: {
                    Item_wear it = null;
                    int targetSlot = -1;
                    if (id >= 0 && id < p.item.bag3.length && p.item.bag3[id] != null) {
                        it = p.item.bag3[id];
                        targetSlot = id;
                    } else {
                        for (int i = 0; i < p.item.bag3.length; i++) {
                            Item_wear c = p.item.bag3[i];
                            if (c != null && c.template != null && (c.index == id || c.template.id == id)) {
                                it = c;
                                targetSlot = i;
                                break;
                            }
                        }
                    }
                    if (it == null || targetSlot < 0) {
                        send_box_ThongBao_OK("Trang bị không tồn tại trong hành trang!");
                        return;
                    }

                    if (isEquippedOnBody(p, it) || it == p.item.it_heart) {
                        send_box_ThongBao_OK("Không thể bán trang bị đang mặc trên người hoặc Trái Tim!");
                        return;
                    }

                    String itemName = it.template.name + (it.levelUp > 0 ? (" +" + it.levelUp) : "");
                    int vang_recive = 30 + (2 * it.getColor() + (it.template.level / 10) + 1) * DataTemplate.TabInventory_ItemSell[0];
                    if (vang_recive > DataTemplate.TabInventory_ItemSell[1]) {
                        vang_recive = DataTemplate.TabInventory_ItemSell[1];
                    }

                    boolean needConfirm = isValuableItemToConfirm(cat, (short) targetSlot, it, itemName);
                    if (needConfirm) {
                        final short fId = (short) targetSlot;
                        final byte fCat = cat;
                        final short fNum = num;
                        final short fStackIndex = stackIndex;
                        final String fItemName = itemName;
                        final int fVangRecive = vang_recive;

                        StringBuilder sbMsg = new StringBuilder();
                        sbMsg.append("Bạn có chắc chắn muốn bán ").append(fItemName)
                             .append(" với giá ").append(ZUtil.number_format(fVangRecive)).append(" Beri không?\n");
                        if (it.isThanTrang()) {
                            sbMsg.append("• CẢNH BÁO: Đây là Thần Trang cực kỳ quý hiếm!\n");
                        }
                        if (it.levelUp > 0) {
                            sbMsg.append("• CẢNH BÁO: Trang bị đã được cường hóa +").append(it.levelUp).append("!\n");
                        }
                        if (it.mdakham != null && it.mdakham.length > 0) {
                            sbMsg.append("• CẢNH BÁO: Trang bị đang được khảm ngọc!\n");
                        }
                        if (it.typelock == 1) {
                            sbMsg.append("• Trang bị đang ở trạng thái Khóa.\n");
                        }
                        sbMsg.append("Sau khi bán, bạn có thể chuộc lại tại Thùng Rác với phí 5 Ruby.");

                        model.YesNoDialog dialog = new model.YesNoDialog(p, 9998, "Xác Nhận Bán",
                            sbMsg.toString(), new String[]{"Bán", "Hủy"});
                        dialog.setHandler(val -> {
                            if (val == 0) {
                                executeSellItem(p, fCat, fId, fNum, fStackIndex);
                            }
                        });
                        dialog.startYesNo();
                        return;
                    } else {
                        executeSellItem(p, cat, (short) targetSlot, num, stackIndex);
                        return;
                    }
                }

                case 4:
                case 7: {
                    executeSellItem(p, cat, id, num, stackIndex);
                    break;
                }
            }
        }
    }

    public void executeSellItem(Player p, byte cat, short id, short num, short stackIndex) {
        try {
            if (p == null || p.item == null) return;
            if (p.trade_target != null) {
                send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                return;
            }
            if (num <= 0) return;

            switch (cat) {
                case 3: {
                    Item_wear it = null;
                    int targetSlot = -1;
                    if (id >= 0 && id < p.item.bag3.length && p.item.bag3[id] != null) {
                        it = p.item.bag3[id];
                        targetSlot = id;
                    } else {
                        for (int i = 0; i < p.item.bag3.length; i++) {
                            Item_wear c = p.item.bag3[i];
                            if (c != null && c.template != null && (c.index == id || c.template.id == id)) {
                                it = c;
                                targetSlot = i;
                                break;
                            }
                        }
                    }
                    if (it == null || targetSlot < 0) {
                        send_box_ThongBao_OK("Trang bị không tồn tại trong hành trang!");
                        return;
                    }
                    if (isEquippedOnBody(p, it) || it == p.item.it_heart) {
                        send_box_ThongBao_OK("Không thể bán trang bị đang mặc trên người hoặc Trái Tim!");
                        return;
                    }

                    // [AUTO-SELL HOOK] Ghi nho item vao blacklist neu dang bat che do hoc
                    if (p.autoSettings != null && p.autoSettings.isAutoSellMode) {
                        int templateId = it.template.id;
                        String itemName = it.template.name;
                        boolean added = activities.AutoManager.addBlacklistItem(p, 3, templateId);
                        if (added) {
                            send_box_ThongBao_OK(" [Auto Bán] Đã nhớ: " + itemName + "\nSẽ tự bán mỗi khi có trong túi!");
                        }
                    }

                    int vang_recive = 30 + (2 * it.getColor() + (it.template.level / 10) + 1) * DataTemplate.TabInventory_ItemSell[0];
                    if (vang_recive > DataTemplate.TabInventory_ItemSell[1]) {
                        vang_recive = DataTemplate.TabInventory_ItemSell[1];
                    }

                    p.update_vang(vang_recive * 1);
                    p.updateMoney();

                    String soldName = it.template.name + (it.levelUp > 0 ? (" +" + it.levelUp) : "");
                    p.item.add_item_save(it);
                    p.item.bag3[targetSlot] = null;
                    p.item.updateInventory(false);
                    send_box_ThongBao_OK("Đã bán " + soldName + " nhận được " + ZUtil.number_format(vang_recive) + " Beri");
                    break;
                }

                case 4:
                case 7: {
                    List<Integer> matchingIndices = new ArrayList<>();
                    for (int i = 0; i < p.item.bag47.size(); i++) {
                        ItemBag47 it = p.item.bag47.get(i);
                        if (it != null && it.category == cat && it.id == id && it.quant > 0) {
                            matchingIndices.add(i);
                        }
                    }
                    if (matchingIndices.isEmpty()) {
                        return;
                    }

                    int targetBagIndex = -1;
                    if (stackIndex >= 0 && stackIndex < matchingIndices.size()) {
                        targetBagIndex = matchingIndices.get(stackIndex);
                    } else {
                        for (int idx : matchingIndices) {
                            if (p.item.bag47.get(idx).quant == num) {
                                targetBagIndex = idx;
                                break;
                            }
                        }
                        if (targetBagIndex < 0) {
                            targetBagIndex = matchingIndices.get(0);
                        }
                    }

                    ItemBag47 targetItem = p.item.bag47.get(targetBagIndex);
                    int sellNum = Math.min((int) num, (int) targetItem.quant);
                    if (sellNum <= 0) return;

                    // [AUTO-SELL HOOK] Ghi nho item vao blacklist neu dang bat che do hoc
                    if (p.autoSettings != null && p.autoSettings.isAutoSellMode) {
                        String name = activities.AutoManager.getItemNameFromKey(cat + ":" + id);
                        boolean added = activities.AutoManager.addBlacklistItem(p, cat, id);
                        if (added) {
                            send_box_ThongBao_OK(" [Auto Bán] Đã nhớ: " + name + "\nSẽ tự bán mỗi khi có trong túi!");
                        }
                    }

                    targetItem.quant -= sellNum;
                    if (targetItem.quant <= 0) {
                        p.item.bag47.remove(targetBagIndex);
                    }
                    p.item.updateInventory(false);

                    int vang_receiv = DataTemplate.TabInventory_ItemSell[2] * sellNum;
                    p.update_vang(vang_receiv);
                    p.updateMoney();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeDropItem(Player p, byte cat, short id, short num, short stackIndex) {
        try {
            if (p == null || p.item == null) return;
            if (p.trade_target != null) {
                send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                return;
            }
            if (num <= 0) return;

            switch (cat) {
                case 3: {
                    Item_wear it = null;
                    int targetSlot = -1;
                    if (id >= 0 && id < p.item.bag3.length && p.item.bag3[id] != null) {
                        it = p.item.bag3[id];
                        targetSlot = id;
                    } else {
                        for (int i = 0; i < p.item.bag3.length; i++) {
                            Item_wear c = p.item.bag3[i];
                            if (c != null && c.template != null && (c.index == id || c.template.id == id)) {
                                it = c;
                                targetSlot = i;
                                break;
                            }
                        }
                    }
                    if (it == null || targetSlot < 0) {
                        send_box_ThongBao_OK("Trang bị không tồn tại trong hành trang!");
                        return;
                    }
                    if (isEquippedOnBody(p, it) || it == p.item.it_heart) {
                        send_box_ThongBao_OK("Không thể vứt bỏ trang bị đang mặc trên người hoặc Trái Tim!");
                        return;
                    }

                    String itemName = it.template.name + (it.levelUp > 0 ? (" +" + it.levelUp) : "");
                    p.item.add_item_save(it);
                    p.item.bag3[targetSlot] = null;
                    p.item.updateInventory(false);
                    send_box_ThongBao_OK("Đã vứt bỏ " + itemName);
                    break;
                }

                case 4:
                case 7: {
                    List<Integer> matchingIndices = new ArrayList<>();
                    for (int i = 0; i < p.item.bag47.size(); i++) {
                        ItemBag47 it = p.item.bag47.get(i);
                        if (it != null && it.category == cat && it.id == id && it.quant > 0) {
                            matchingIndices.add(i);
                        }
                    }
                    if (matchingIndices.isEmpty()) {
                        send_box_ThongBao_OK("Vật phẩm không tồn tại trong hành trang!");
                        return;
                    }

                    int targetBagIndex = -1;
                    if (stackIndex >= 0 && stackIndex < matchingIndices.size()) {
                        targetBagIndex = matchingIndices.get(stackIndex);
                    } else {
                        for (int idx : matchingIndices) {
                            if (p.item.bag47.get(idx).quant == num) {
                                targetBagIndex = idx;
                                break;
                            }
                        }
                        if (targetBagIndex < 0) {
                            targetBagIndex = matchingIndices.get(0);
                        }
                    }

                    ItemBag47 targetItem = p.item.bag47.get(targetBagIndex);
                    int dropNum = Math.min((int) num, (int) targetItem.quant);
                    if (dropNum <= 0) return;

                    String name = (cat == 4 ? (ItemTemplate4.get_it_by_id(id) != null ? ItemTemplate4.get_it_by_id(id).name : "vật phẩm") 
                                           : (ItemTemplate7.get_it_by_id(id) != null ? ItemTemplate7.get_it_by_id(id).name : "vật phẩm"));

                    targetItem.quant -= dropNum;
                    if (targetItem.quant <= 0) {
                        p.item.bag47.remove(targetBagIndex);
                    }
                    p.item.updateInventory(false);
                    send_box_ThongBao_OK("Đã vứt bỏ " + dropNum + " " + name);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void request_item4_info(Message m2) throws IOException {
        Player p = this.player;
        short id = m2.reader().readShort();
        ItemTemplate4_Info temp = ItemTemplate4_Info.get_by_id(id);
        Message m = new Message(-105);
        m.writer().writeShort(id);
        if (temp != null && temp.info != null && !temp.info.isEmpty()) {
            m.writer().writeUTF(temp.info);
        } else {
            ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
            if (it4 != null && it4.name != null && !it4.name.isEmpty()) {
                m.writer().writeUTF(it4.name);
            } else if (id == 345) {
                m.writer().writeUTF("Đá đặc biệt dùng để nâng cấp trang bị lên Thời Không tại NPC Thợ Rèn.");
            } else if (id == 346) {
                m.writer().writeUTF("Đá đặc biệt dùng để nâng cấp trang bị lên Truyền Thuyết tại NPC Thợ Rèn.");
            } else if (id == 347) {
                m.writer().writeUTF("Đá đặc biệt dùng để nâng cấp trang bị lên Thần Thoại tại NPC Thợ Rèn.");
            } else if (id == 384) {
                m.writer().writeUTF("Đá đặc biệt dùng để nâng cấp trang bị lên Vô Cực tại NPC Thợ Rèn.");
            } else {
                m.writer().writeUTF("");
            }
        }
        sendMessage(m);
        m.cleanup();
    }







    public static void open_box_item3_orange(Player p, List<Item_wear> list, int id_chest,

            String s1, String s2) throws IOException {

        Message m = new Message(-34);

        m.writer().writeByte(21);

        m.writer().writeShort(id_chest);

        m.writer().writeUTF(s1);

        m.writer().writeUTF(s2);

        m.writer().writeByte(list.size());

        for (int i = 0; i < list.size(); i++) {

            Item_wear temp = list.get(i);

            m.writer().writeByte(3);

            m.writer().writeUTF(temp.template.name);

            m.writer().writeShort(temp.template.icon);

            m.writer().writeInt(1);

            m.writer().writeByte(temp.template != null ? temp.template.color : temp.getColor());

        }

        p.getService().sendMessage(m);

        m.cleanup();

    }



    public static void open_box_item3_orange7(Player p, List<Item_wear> list, int id_chest,

            String s1, String s2) throws IOException {

        Message m = new Message(-34);

        m.writer().writeByte(21);

        m.writer().writeShort(id_chest);

        m.writer().writeUTF(s1);

        m.writer().writeUTF(s2);

        m.writer().writeByte(list.size());

        for (int i = 0; i < list.size(); i++) {

            Item_wear temp = list.get(i);

            m.writer().writeByte(3);

            m.writer().writeUTF(temp.template.name);

            m.writer().writeShort(temp.template.icon);

            m.writer().writeInt(1);

            m.writer().writeByte(8);

        }

        p.getService().sendMessage(m);

        m.cleanup();

    }



    public void CountDown_Ticket() throws IOException {

        Player p = this.player;

        //

        if (p.get_ticket() >= p.get_ticket_max()) {

            p.cd_ticket_next = System.currentTimeMillis();

        }

        if (p.get_pvp_ticket() >= p.get_pvp_ticket_max()) {

            p.cd_pvp_next = System.currentTimeMillis();

        }

        if (p.get_key_boss() >= p.get_key_boss_max()) {

            p.cd_keyboss_next = System.currentTimeMillis();

        }

        // ticket cd

        Message m = new Message(-61);

        m.writer().writeByte(0);

        long cd = (p.cd_ticket_next - System.currentTimeMillis()) / 1000;

        if (cd < 0 || p.get_ticket() == p.get_ticket_max()) {

            cd = 0;

        }

        m.writer().writeInt((int) cd);

        sendMessage(m);

        m.cleanup();

        // keyboss cd

        m = new Message(-61);

        m.writer().writeByte(1);

        cd = (p.cd_keyboss_next - System.currentTimeMillis()) / 1000;

        if (cd < 0 || p.get_key_boss() == p.get_key_boss_max()) {

            cd = 0;

        }

        m.writer().writeInt((int) cd);

        sendMessage(m);

        m.cleanup();

        // pvp cd

        m = new Message(-61);

        m.writer().writeByte(2);

        cd = (p.cd_pvp_next - System.currentTimeMillis()) / 1000;

        if (cd < 0 || p.get_pvp_ticket() == p.get_pvp_ticket_max()) {

            cd = 0;

        }

        m.writer().writeInt((int) cd);

        sendMessage(m);

        m.cleanup();

        // x2 cd

        m = new Message(-61);

        m.writer().writeByte(3);

        EffTemplate eff = p.get_eff(2);

        if (eff != null) {

            cd = (eff.time - System.currentTimeMillis()) / 1000;

            if (cd < 0) {

                cd = 0;

            }

        } else {

            cd = 0;

        }

        m.writer().writeInt((int) cd);

        sendMessage(m);

        m.cleanup();

    }



    public void NewDialog_eat_taq(String[] name_, int[] icon_, int id)

            throws IOException {

        Player p = this.player;

        Message m = new Message(40);

        m.writer().writeByte(1);

        m.writer().writeUTF("");

        m.writer().writeByte(name_.length + 1);

        ItemTemplate4 it_temp = ItemTemplate4.get_it_by_id(id);

        m.writer().writeByte(4);

        m.writer().writeUTF(it_temp.name);

        m.writer().writeShort(it_temp.icon);

        for (int i = 0; i < name_.length; i++) {

            m.writer().writeByte(104);

            m.writer().writeUTF(name_[i]);

            m.writer().writeShort(icon_[i]);

        }

        sendMessage(m);

        m.cleanup();

    }



    public void Help_From_Server(int num, String text) throws IOException {

        Player p = this.player;

        Message m = new Message(-54);

        m.writer().writeShort(num);

        m.writer().writeUTF(text);

        sendMessage(m);

        m.cleanup();

    }



    public void Wanted(boolean save_cache) throws IOException {

        Player p = this.player;

        Message m = new Message(-85);

        m.writer().writeByte(4);

        m.writer().writeInt(p.get_wanted_point());

        if (save_cache) {

            p.msgs.add(m);

        } else {

            sendMessage(m);

        }

        m.cleanup();

    }



    public void start_combo(int type) throws IOException {

        Player p = this.player;

        Message m = new Message(29);

        int size0 = p.list_can_combo.size();

        if (type == 1 && size0 > 0) {

            p.time_combo = System.currentTimeMillis() + 30_000L;

            int size = ZUtil.random(4, 10);

            p.is_combo = new byte[size];

            m.writer().writeByte(p.is_combo.length);

            for (int i = 0; i < p.is_combo.length; i++) {

                Skill_info get_skill = p.list_can_combo.get(ZUtil.random(size0));

                p.is_combo[i] = (byte) get_skill.temp.ID;

                m.writer().writeShort(get_skill.temp.idIcon + 94);

            }

        } else {

            m.writer().writeByte(0);

        }

        sendMessage(m);

        m.cleanup();

    }



    /** Gửi packet cập nhật HP cây trồng tới toàn bộ map */

    public static void sendTreeHp(map.Zone map, short index, int hp) {

        try {

            Message m = new Message(-83);

            m.writer().writeShort(index);

            m.writer().writeByte(2);

            m.writer().writeInt(100);

            m.writer().writeInt(hp);

            m.writer().writeInt(0);

            m.writer().writeInt(100);

            m.writer().writeInt(hp);

            m.writer().writeInt(0);

            map.send_msg_all_p(m, null, true);

            m.cleanup();

        } catch (Exception ignored) {}

    }



    /** Gửi packet cập nhật trạng thái cây trồng (state, timeRemain) tới toàn bộ map */

    public static void sendTreeState(map.Zone map, short index, byte state, long timeThuHoach) {

        try {

            Message m = new Message(71);

            m.writer().writeShort(index);

            m.writer().writeByte(2);

            int timeRemain = (int) ((timeThuHoach - System.currentTimeMillis()) / 1_000L);

            m.writer().writeShort(timeRemain);

            m.writer().writeByte(state);

            map.send_msg_all_p(m, null, true);

            m.cleanup();

        } catch (Exception ignored) {}

    }



    public static void send_gift(Player p, int type, String title, String notice,
            List<GiftBox> gift, boolean show_table) throws IOException {
        if (p == null || p.conn == null || gift == null || gift.isEmpty()) {
            return;
        }

        gift = template.GiftBox.consolidateGifts(gift);

        String str_show = "Nhận ";

        Message m = new Message(-34);

        m.writer().writeByte(type);

        m.writer().writeUTF(title != null ? title : "");

        m.writer().writeUTF(notice != null ? notice : "");

        m.writer().writeByte(gift.size());

        for (int i = 0; i < gift.size(); i++) {

            GiftBox temp = gift.get(i);
            if (temp == null) continue;

            if (temp.name == null || temp.name.isEmpty()) {
                if (temp.type == 99) {
                    temp.name = "Kinh nghiệm";
                } else if (temp.type == 4) {
                    if (temp.id == 0) temp.name = "Beri";
                    else if (temp.id == 1) temp.name = "Ruby";
                    else if (temp.id == 2 || temp.id == 908) temp.name = "Extol";
                    else if (temp.id == 6) temp.name = "Vé";
                    else if (temp.id == 333) temp.name = "Kinh nghiệm kỹ năng";
                    else if (temp.id == -10) temp.name = "Kinh nghiệm Bang";
                    else if (temp.id == -11) temp.name = "Beri Bang";
                    else if (temp.id == -12) temp.name = "Ruby Bang";
                    else {
                        ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(temp.id);
                        if (t4 != null) temp.name = t4.name;
                        else temp.name = "Vật phẩm";
                    }
                } else if (temp.type == 7) {
                    ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(temp.id);
                    if (t7 != null) temp.name = t7.name;
                    else temp.name = "Nguyên liệu";
                } else if (temp.type == 3) {
                    ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(temp.id);
                    if (t3 != null) temp.name = t3.name;
                    else temp.name = "Trang bị";
                } else if (temp.type == 105) {
                    ItemFashion itf = ItemFashion.get_item(temp.id);
                    if (itf != null) temp.name = itf.name;
                    else temp.name = "Thời trang";
                } else if (temp.type == 110) {
                    Pet pet = Pet.getTemplate(temp.id);
                    if (pet != null) temp.name = pet.name;
                    else temp.name = "Thú cưng";
                } else {
                    temp.name = "Vật phẩm";
                }
            }
            if (temp.icon == 0) {
                if (temp.type == 99) {
                    temp.icon = 333;
                } else if (temp.type == 4) {
                    if (temp.id == 0) temp.icon = 0;
                    else if (temp.id == 1) temp.icon = 1;
                    else if (temp.id == 2) temp.icon = 2;
                    else if (temp.id == 908) temp.icon = 908;
                    else if (temp.id == 6) temp.icon = 6;
                    else if (temp.id == 333) temp.icon = 333;
                    else {
                        ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(temp.id);
                        if (t4 != null) temp.icon = t4.icon;
                    }
                } else if (temp.type == 7) {
                    ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(temp.id);
                    if (t7 != null) temp.icon = (short) t7.icon;
                } else if (temp.type == 3) {
                    ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(temp.id);
                    if (t3 != null) temp.icon = t3.icon;
                } else if (temp.type == 105) {
                    ItemFashion itf = ItemFashion.get_item(temp.id);
                    if (itf != null) temp.icon = itf.idIcon;
                } else if (temp.type == 110) {
                    Pet pet = Pet.getTemplate(temp.id);
                    if (pet != null) temp.icon = pet.icon;
                }
            }

            m.writer().writeByte(temp.type);

            m.writer().writeUTF(temp.name != null ? temp.name : "");

            m.writer().writeShort(temp.icon);

            int num_item = temp.num;

            m.writer().writeInt(num_item);

            m.writer().writeByte(temp.color);

            //
            switch (temp.type) {
                case 110: { // nhận pet (hỗ trợ cả Pet template ID và Item4 ID, vĩnh viễn)
                    Pet petTemp = Pet.getTemplate(temp.id);
                    short petId = (petTemp != null) ? petTemp.id : temp.id;
                    MyPet pet = null;
                    if (p.my_pet == null) {
                        p.my_pet = new ArrayList<>();
                    }
                    for (int i1 = 0; i1 < p.my_pet.size(); i1++) {
                        if (p.my_pet.get(i1).id == petId) {
                            pet = p.my_pet.get(i1);
                            break;
                        }
                    }
                    if (pet != null) {
                        pet.time = -1;
                    } else {
                        pet = new MyPet();
                        pet.id = petId;
                        pet.isUse = false;
                        pet.template = (petTemp != null) ? petTemp : Pet.getTemplate(petId);
                        pet.time = -1;
                        p.my_pet.add(pet);
                    }
                    break;
                }

                case 105: {// vuong code nhận cải trang có thời hạn

                    ItemFashion itf = ItemFashion.get_item(temp.id);

                    if (itf != null) {

                        ItemFashionP2 temp2 = new ItemFashionP2();

                        temp2.id = itf.ID;

                        if (itf.hsd > -1) {

                            temp2.expires = System.currentTimeMillis() + 60_000L * 60 * 24 * itf.hsd;

                        } else {

                            temp2.expires = -1;

                        }

                        p.fashion.add(temp2);

                        p.update_fashionP2(temp2);

                        if (p.map != null && p.map.players != null) {

                            for (int i2 = 0; i2 < p.map.players.size(); i2++) {

                                Player p0 = p.map.players.get(i2);

                                if (p0 != null && p0.getService() != null) {

                                    p0.getService().charWearing(p, false);

                                }

                            }

                        }

                        p.getService().UpdateInfoMaincharInfo();

                    }

                    break;

                }

                case 99: { // xp

                    long exp_receiv = num_item;

                    int buff_percent = 100;

                    if (p.clan != null && p.clan.check_buff(0)) {

                        buff_percent += 50;

                    }

                    if (p.clan != null && p.clan.check_buff(1)) {

                        buff_percent += 50;

                    }

                    if (p.get_eff(2) != null) {

                        buff_percent += 100;

                    }

                    if (p.get_eff(17) != null) {

                        buff_percent += 100;

                    }

                    exp_receiv = (exp_receiv * buff_percent) / 100;

                    p.update_exp(exp_receiv, false);

                    break;

                }

                // case 3: {

                // ItemTemplate3 template3 = ItemTemplate3.get_it_by_id(temp.id);

                // if (template3 != null) {

                // Item_wear it_add = new Item_wear();

                // it_add.setup_template_by_id(template3);

                // if (it_add.template != null) {

                // int numLoKham

                // = (60 > Util.random(120)) ? 0 : ((70 > Util.random(120)) ? 1 : 2);

                // it_add.numLoKham = (byte) numLoKham;

                // p.item.add_item_bag3(it_add);

                // //

                // if (it_add.template.name.equals("Dial Cực Phẩm")) {

                // for (int j = 0; j < 7; j++) {

                // int random_add = Util.random(11);

                // switch (random_add) {

                // case 0: {

                // it_add.option_item.add(new Option(Util.random(5, 10), Util.random(5, 10)));

                // break;

                // }

                // case 1: {

                // it_add.option_item.add(new Option(Util.random(19, 21), Util.random(25, 50)));

                // break;

                // }

                // case 2: {

                // int value_random = Util.random(10, 15);

                // while (value_random == 11) {

                // value_random = Util.random(10, 15);

                // }

                // it_add.option_item.add(new Option(value_random, Util.random(120, 220)));

                // break;

                // }

                // case 3: {

                // it_add.option_item.add(new Option(56, Util.random(120, 220)));

                // break;

                // }

                // case 4: {

                // it_add.option_item.add(new Option(4, Util.random(120, 220)));

                // break;

                // }

                // case 5: {

                // it_add.option_item.add(new Option(53, Util.random(55, 160)));

                // break;

                // }

                // case 6: {

                // it_add.option_item.add(new Option(51, Util.random(40, 90)));

                // break;

                // }

                // case 7: {

                // it_add.option_item.add(new Option(50, Util.random(40, 90)));

                // break;

                // }

                // case 8: {

                // it_add.option_item.add(new Option(52, Util.random(40, 90)));

                // break;

                // }

                // case 9: {

                // it_add.option_item.add(new Option(63, Util.random(40, 90)));

                // break;

                // }

                // case 10: {

                // it_add.option_item.add(new Option(49, Util.random(40, 90)));

                // break;

                // }

                // }

                // }

                // } else if (it_add.template.name.equals("Dial Thần Thoại")) {

                // for (int j = 0; j < 2; j++) {

                // int random_add = Util.random(7);

                // switch (random_add) {

                // case 0: {

                // it_add.option_item.add(new Option(Util.random(5, 10), Util.random(5, 10)));

                // break;

                // }

                // case 1: {

                // it_add.option_item.add(new Option(Util.random(19, 21), Util.random(25, 50)));

                // break;

                // }

                // case 2: {

                // int value_random = Util.random(10, 15);

                // while (value_random == 11) {

                // value_random = Util.random(10, 15);

                // }

                // it_add.option_item.add(new Option(value_random, Util.random(100, 200)));

                // break;

                // }

                // case 3: {

                // it_add.option_item.add(new Option(56, Util.random(100, 200)));

                // break;

                // }

                // case 4: {

                // it_add.option_item.add(new Option(4, Util.random(100, 200)));

                // break;

                // }

                // case 5: {

                // it_add.option_item.add(new Option(53, Util.random(50, 150)));

                // break;

                // }

                // case 6: {

                // it_add.option_item.add(new Option(51, Util.random(30, 80)));

                // break;

                // }

                // }

                // }

                // } else if (it_add.template.name.equals("Dial Truyền thuyết")) {

                // for (int j = 0; j < 3; j++) {

                // int random_add = Util.random(11);

                // switch (random_add) {

                // case 0: {

                // it_add.option_item.add(new Option(Util.random(5, 10), Util.random(5, 10)));

                // break;

                // }

                // case 1: {

                // it_add.option_item.add(new Option(Util.random(19, 21), Util.random(25, 50)));

                // break;

                // }

                // case 2: {

                // int value_random = Util.random(10, 15);

                // while (value_random == 11) {

                // value_random = Util.random(10, 15);

                // }

                // it_add.option_item.add(new Option(value_random, Util.random(100, 200)));

                // break;

                // }

                // case 3: {

                // it_add.option_item.add(new Option(56, Util.random(100, 200)));

                // break;

                // }

                // case 4: {

                // it_add.option_item.add(new Option(4, Util.random(100, 200)));

                // break;

                // }

                // case 5: {

                // it_add.option_item.add(new Option(53, Util.random(50, 150)));

                // break;

                // }

                // case 6: {

                // it_add.option_item.add(new Option(51, Util.random(30, 80)));

                // break;

                // }

                // case 7: {

                // it_add.option_item.add(new Option(50, Util.random(30, 80)));

                // break;

                // }

                // case 8: {

                // it_add.option_item.add(new Option(52, Util.random(30, 80)));

                // break;

                // }

                // case 9: {

                // it_add.option_item.add(new Option(63, Util.random(30, 80)));

                // break;

                // }

                // case 10: {

                // it_add.option_item.add(new Option(49, Util.random(30, 80)));

                // break;

                // }

                // }

                // }

                // } else if (it_add.template.name.equals("Dial Sử Thi")) {

                // int random_add = Util.random(5);

                // switch (random_add) {

                // case 0: {

                // it_add.option_item.add(new Option(Util.random(5, 10), Util.random(5, 10)));

                // break;

                // }

                // case 1: {

                // it_add.option_item.add(new Option(Util.random(19, 21), Util.random(25, 50)));

                // break;

                // }

                // case 2: {

                // int value_random = Util.random(10, 15);

                // while (value_random == 11) {

                // value_random = Util.random(10, 15);

                // }

                // it_add.option_item.add(new Option(value_random, Util.random(100, 200)));

                // break;

                // }

                // case 3: {

                // it_add.option_item.add(new Option(56, Util.random(100, 200)));

                // break;

                // }

                // case 4: {

                // it_add.option_item.add(new Option(4, Util.random(100, 200)));

                // break;

                // }

                // }

                // } else if (it_add.template.name.equals("Dial Siêu Năng")) {

                // int random_add = Util.random(4);

                // switch (random_add) {

                // case 0: {

                // it_add.option_item.add(new Option(Util.random(5, 10), Util.random(5, 10)));

                // break;

                // }

                // case 1: {

                // it_add.option_item.add(new Option(Util.random(19, 21), Util.random(25, 50)));

                // break;

                // }

                // case 2: {

                // int value_random = Util.random(10, 15);

                // while (value_random == 11) {

                // value_random = Util.random(10, 15);

                // }

                // it_add.option_item.add(new Option(value_random, Util.random(100, 200)));

                // break;

                // }

                // case 3: {

                // it_add.option_item.add(new Option(56, Util.random(100, 200)));

                // break;

                // }

                // }

                // }

                //

                //// short[] ids = new short[]{5, 6, 7, 8, 9, 10, 12, 13, 14, 11, 56};

                //// int[] par1 = new int[]{5, 5, 5, 5, 5, 50, 50, 50, 50, 100, 100};

                //// int[] par2 = new int[]{8, 8, 8, 8, 8, 80, 80, 80, 80, 200, 150};

                //// //

                //// if (it_add.template.typeEquip == 7) {

                //// int random_add;

                //// if (it_add.template.name.equals("Dial Thần Thoại")) {

                //// random_add = 3;

                //// } else if (it_add.template.name.equals("Dial Truyền thuyết")) {

                //// random_add = 4;

                //// } else {

                //// random_add = 2;

                //// }

                //// for (int j = 0; j < random_add; j++) {

                //// int idx = Util.random(ids.length);

                //// boolean ch = true;

                //// for (int k = 0; k < it_add.option_item.size(); k++) {

                //// if (it_add.option_item.get(k).id == ids[idx]) {

                //// ch = false;

                //// break;

                //// }

                //// }

                //// while (!ch) {

                //// idx = Util.random(ids.length);

                //// ch = true;

                //// for (int k = 0; k < it_add.option_item.size(); k++) {

                //// if (it_add.option_item.get(k).id == ids[idx]) {

                //// ch = false;

                //// break;

                //// }

                //// }

                //// }

                //// it_add.option_item

                //// .add(new Option(ids[idx], Util.random(par1[idx], par2[idx] + 1)));

                //// }

                //// }

                // }

                // }

                // break;

                // }

                case 3: {

                    ItemTemplate3 template3 = ItemTemplate3.get_it_by_id(temp.id);

                    if (template3 != null) {

                        Item_wear it_add = new Item_wear();

                        it_add.setup_template_by_id(template3);

                        if (it_add.template != null) {

                            int numLoKham = (60 > ZUtil.random(120)) ? 0 : ((70 > ZUtil.random(120)) ? 1 : 2);
                            it_add.numLoKham = (byte) numLoKham;

                            if (template3.isThanTrang()) {
                                it_add.levelUp = 0;
                                it_add.color = (byte) (temp.color > 0 ? temp.color : 1);
                                it_add.typelock = 0;
                                it_add.numLoKham = 0;
                                it_add.numHoleDaDuc = 0;
                                it_add.mdakham = new short[0];
                                it_add.valueChetac = 0;
                                it_add.isHoanMy = 0;
                                it_add.valueKichAn = -1;
                            }

                            if (temp.options != null && !temp.options.isEmpty()) {
                                it_add.option_item.clear();
                                for (Option opt : temp.options) {
                                    it_add.option_item.add(new Option(opt.id, opt.getParam()));
                                }
                            }

                            if (template3.typeEquip == 7) {
                                itemz.UpgradeDial.initDialOptions(it_add);
                            }

                            p.item.add_item_bag3(it_add);
                        }

                    }

                    break;

                }

                case 4: {
                    if (temp.id == 0 || (temp.name != null && temp.name.equalsIgnoreCase("beri"))) {
                        p.update_vang(num_item);
                        p.updateMoney();
                        str_show += (num_item + " Beri, ");
                    } else if (temp.id == 1 || (temp.name != null && temp.name.equalsIgnoreCase("ruby"))) {
                        p.update_ngoc(num_item);
                        p.updateMoney();
                        str_show += (num_item + " Ruby, ");
                    } else if (temp.id == 908 || temp.id == 2 || (temp.name != null && temp.name.toLowerCase().contains("extol"))) {
                        p.updateVnd(num_item);
                        p.updateMoney();
                        str_show += (num_item + " Extol, ");
                    } else if (temp.id == 6) {
                        p.update_ticket(num_item);
                        p.updateMoney();
                        str_show += (num_item + " Vé, ");
                    } else if (temp.id == 333) {
                        int lvOld = p.level;
                        p.update_exp(temp.num, false);
                        str_show += (num_item + " XP Skill, ");
                        if (lvOld != p.level && p.isDe) {
                            if (lvOld == 29 && p.level == 30) {
                                for (int i22 = 0; i22 < p.skill_point.size(); i22++) {
                                    if (p.skill_point.get(i22).temp.ID == 1
                                            && p.skill_point.get(i22).temp.Lv_RQ == -1) {
                                        Skill_Template.learn_skill(p.skill_point.get(i22));
                                        break;
                                    }
                                }
                            } else if (lvOld == 49 && p.level == 50) {
                                for (int i22 = 0; i22 < p.skill_point.size(); i22++) {
                                    if (p.skill_point.get(i22).temp.ID == 2
                                            && p.skill_point.get(i22).temp.Lv_RQ == -1) {
                                        Skill_Template.learn_skill(p.skill_point.get(i22));
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        ItemTemplate4 template4 = ItemTemplate4.get_it_by_id(temp.id);
                        if (template4 != null) {
                            str_show += (num_item + " " + template4.name + ", ");
                            if (template4.type == -16) {
                                int cat = activities.HanhTrinh.getIslandCategory(p.map.template.id);
                                if (cat >= 0) {
                                    ItemBag47 it_select = new ItemBag47();
                                    it_select.category = (byte) cat;
                                    it_select.id = template4.id;
                                    it_select.quant = 0;
                                    boolean add = true;
                                    for (int j = 0; j < p.daHanhTrinh.size(); j++) {
                                        ItemBag47 existing = p.daHanhTrinh.get(j);
                                        if (existing != null && existing.category == cat
                                                && existing.id == it_select.id) {
                                            add = false;
                                            break;
                                        }
                                    }
                                    if (add) {
                                        p.daHanhTrinh.add(it_select);
                                    }
                                }
                            } else {
                                p.item.add_item_bag47(4, template4.id, num_item);
                            }
                        } else if (temp.id == -10) {
                            if (p.clan != null) {
                                p.clan.update_xp(num_item);
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.set_data(p0, false);
                                    }
                                }
                            }
                        } else if (temp.id == -11) {
                            if (p.clan != null) {
                                p.clan.update_beri(num_item);
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_money(p0, false);
                                    }
                                }
                            }
                        } else if (temp.id == -12) {
                            if (p.clan != null) {
                                p.clan.update_ruby(num_item);
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_money(p0, false);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }

                case 7: {
                    ItemTemplate7 template7 = ItemTemplate7.get_it_by_id(temp.id);
                    if (template7 != null) {
                        p.item.add_item_bag47(7, template7.id, num_item);
                        str_show += (num_item + " " + template7.name + ", ");
                    }
                    break;
                }
            }
        }

        if (show_table) {
            p.addmsg(m);
        } else {
            if (str_show.endsWith(", ")) {
                str_show = str_show.substring(0, str_show.length() - 2);
            }
            Message m2 = new Message(-31);
            m2.writer().writeByte(0);
            m2.writer().writeUTF(str_show);
            m2.writer().writeByte(0);
            m2.writer().writeShort(-1);
            p.addmsg(m2);
            m2.cleanup();
        }

        m.cleanup();
        p.item.updateInventory(false);
    }



    public void send_eff(int b, int num) throws IOException {

        Player p = this.player;

        // 0 lv up

        // 1 bien hinh

        // 3 thunder to obj

        // 11: open bi ngo

        // 14, 15 green ball

        // 19: exit obj

        // 20 khinh khi cau

        // 21 deny death

        // 22 red thunder

        // 23 firework

        // 24 room heart

        //

        // 100 = 10s

        if (b == 14 || b == 15) {

            num /= 10;

        }

        Message m = new Message(-15);

        m.writer().writeByte(b);

        m.writer().writeShort(p.index_map);

        m.writer().writeByte(0);

        m.writer().writeShort(num);

        p.map.send_msg_all_p(m, p, true);

        m.cleanup();

    }



    public void send_eff_sword_splash(int id) throws IOException {

        Player p = this.player;

        Player p0 = p.map.get_player_by_id_inmap(id);

        Mob mob = null;

        if (p0 == null) {

            mob = p.map.getMob(id);

        }

        if (p0 != null || mob != null) {

            Message m = new Message(-15);

            m.writer().writeByte(5);

            m.writer().writeShort(p.index_map);

            m.writer().writeByte(0);

            m.writer().writeShort(2000);

            //

            m.writer().writeShort(id);

            if (mob != null) {

                m.writer().writeByte(1);

            } else {

                m.writer().writeByte(0);

            }

            //

            p.map.send_msg_all_p(m, p, true);

            m.cleanup();

        }

    }



    @Override

    public void addEffect(short indexMap, short idEff, int time, byte typeMove, byte loop) {

        try {

            Message m = new Message(74);

            m.writer().writeByte(1);

            m.writer().writeShort(indexMap);

            m.writer().writeShort(idEff);

            m.writer().writeInt(time);

            m.writer().writeByte(typeMove);

            m.writer().writeByte(loop);

            if (player != null && player.map != null) {

                player.map.send_msg_all_p(m, null, true);

            } else {

                sendMessage(m);

            }

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    public void send_count_kick_ava(byte id, byte count) {
        if (this.player == null || this.player.isBot) return;
        try {
            if (count < 0) count = 0;
            Message m = new Message(58);
            m.writer().writeByte(id);
            m.writer().writeByte(count);
            this.player.addmsg(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void send_vong_sinh_tu(short x, short y, short w, short h, int color) {

        if (this.player == null || this.player.isBot) return;

        try {

            Message m = new Message(73);

            m.writer().writeByte(0);

            m.writer().writeShort(x);

            m.writer().writeShort(y);

            m.writer().writeShort(w);

            m.writer().writeShort(h);

            m.writer().writeInt(color);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    // ==========================================

    // MESSAGE 53: ĐẠI CHIẾN THẾ GIỚI (WorldWar)

    // ==========================================

    public void send_update_point_ww(Player target) {

        if (this.player == null || this.player.isBot || target == null) return;

        try {

            Message m = new Message(53);

            m.writer().writeShort(target.index_map);

            m.writer().writeByte(target.point_ww);

            m.writer().writeShort(target.killWW);

            m.writer().writeShort(target.deadWW);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    // ==========================================

    // MESSAGE 82: VÒNG QUAY WORLD CUP (Quay_WC)

    // ==========================================

    public void send_quay_wc_show() {

        if (this.player == null || this.player.isBot) return;

        try {

            Message m = new Message(82);

            m.writer().writeByte(0);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_quay_wc_list_items(byte[] cats, short[] icons) {

        if (this.player == null || this.player.isBot || cats == null || icons == null) return;

        try {

            Message m = new Message(82);

            m.writer().writeByte(3);

            m.writer().writeByte(cats.length);

            for (int i = 0; i < cats.length; i++) {

                m.writer().writeByte(cats[i]);

                m.writer().writeShort(icons[i]);

            }

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_quay_wc_info(short idIconVongQuay, short numVe, short numLanDaQuay) {

        if (this.player == null || this.player.isBot) return;

        try {

            Message m = new Message(82);

            m.writer().writeByte(4);

            m.writer().writeShort(idIconVongQuay);

            m.writer().writeShort(numVe);

            m.writer().writeShort(numLanDaQuay);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_quay_wc_result(byte typeQuay, byte[] cats, String[] names, short[] icons, int[] quantities, byte[] colors) {

        if (this.player == null || this.player.isBot || cats == null) return;

        try {

            Message m = new Message(82);

            m.writer().writeByte(typeQuay);

            m.writer().writeByte(cats.length);

            for (int i = 0; i < cats.length; i++) {

                m.writer().writeByte(cats[i]);

                m.writer().writeUTF(names[i]);

                m.writer().writeShort(icons[i]);

                m.writer().writeInt(quantities[i]);

                m.writer().writeByte(colors[i]);

            }

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    // ==========================================

    // MESSAGE 101: THÁCH ĐẤU BANG HỘI (Clan_Fight)

    // ==========================================

    public void send_clan_fight_list(java.util.List<clan.Clan> listClan) {

        if (this.player == null || this.player.isBot || listClan == null) return;

        try {

            Message m = new Message(101);

            m.writer().writeByte(0);

            m.writer().writeByte(listClan.size());

            for (int i = 0; i < listClan.size(); i++) {

                clan.Clan c = listClan.get(i);

                m.writer().writeShort(c.id);

                m.writer().writeUTF(c.name);

                m.writer().writeShort(c.icon);

                m.writer().writeShort(c.level);

            }

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_clan_fight_notice(String text) {

        if (this.player == null || this.player.isBot) return;

        try {

            Message m = new Message(101);

            m.writer().writeByte(4);

            m.writer().writeUTF(text);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_clan_fight_invite(short clanId, String clanName, short clanLv, byte typeFight) {

        if (this.player == null || this.player.isBot) return;

        try {

            Message m = new Message(101);

            m.writer().writeByte(5);

            m.writer().writeShort(clanId);

            m.writer().writeUTF(clanName);

            m.writer().writeShort(clanLv);

            m.writer().writeByte(typeFight);

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void send_clan_fight_members(java.util.List<clan.ClanMember> members) {

        if (this.player == null || this.player.isBot || members == null) return;

        try {

            Message m = new Message(101);

            m.writer().writeByte(6);

            m.writer().writeByte(members.size());

            for (int i = 0; i < members.size(); i++) {

                clan.ClanMember mem = members.get(i);

                m.writer().writeShort(mem.id);

                m.writer().writeUTF(mem.name);

                m.writer().writeShort(mem.level);

            }

            this.player.addmsg(m);

            m.cleanup();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public void showVongQuayWC() {

        send_quay_wc_show();

    }



    public static void send_kich_an(Player p0, Player p, int time_buff, int type, int type_eff,

            int par) throws IOException {

        if (p != null && p.map != null && p.map.getService() != null) {

            p.map.getService().send_kich_an(p0, p, time_buff, type, type_eff, par);

        }

    }



    public void DonotAutoReconnect() throws IOException {

        Session ss = this.player.conn;

        Message m = new Message(-88);

        ss.addmsg(m);

        m.cleanup();

    }



    public void send_time_cool_down(long t, String title, int type)throws IOException {

        Player p = this.player;

        if (type == 0 || type == 2 || type == 3) {

            Message m = new Message(-73);

            m.writer().writeByte(type); // time type

            long time_remain = t - System.currentTimeMillis();

            m.writer().writeShort((short) (time_remain / 1000));

            m.writer().writeUTF(title);

            sendMessage(m);

            m.cleanup();

        }

    }



    public void send_hp_map(int hp, int hpMax) throws IOException {

        Player p = this.player;

        Message m = new Message(-73);

        m.writer().writeByte(1);

        m.writer().writeInt(hpMax);

        m.writer().writeInt(hp);

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void sendConfirmYesNo(short yesNoType) {}

    @Override

    public void sendUpgradeGear(short itemIndex) {}

    @Override

    public void sendQuestProcess(short questId, byte status) {}

    @Override

    public void sendPartyAccept(short inviterId) {}

    @Override

    public void sendPartyInvite(short indexMap) {}

    @Override

    public void sendTableTickOption(short idDialog) {}

    @Override

    public void sendMarketEarningsClaim(int index) {}

    @Override

    public void sendMarketListPurpleGear(int index, int price) {}

    @Override

    public void sendMarketBuyItem(int type, int index) {}



    @Override

    public void notice_dice_TaiXiu() throws IOException {

        Player p = this.player;

        Message m = new Message(80);

        m.writer().writeByte(0);

        m.writer().writeByte(2);

        byte[] result = core.Manager.gI().TaiXiu().get_dice_now();

        if ((result[0] + result[1] + result[2]) >= 11

                && (result[0] + result[1] + result[2]) <= 17) {

            m.writer().writeByte(1); // kq

        } else {

            m.writer().writeByte(0); // kq

        }

        m.writer().write(result);

        p.addmsg(m);

        m.cleanup();

    }



    @Override

    public void updateInfoTaiXiu() throws IOException {

        Player p = this.player;

        Message m = new Message(80);

        m.writer().writeByte(0);

        m.writer().writeByte(1);

        m.writer().writeInt(core.Manager.gI().TaiXiu().MoneyTotal(0)); // xiu

        m.writer().writeInt(core.Manager.gI().TaiXiu().MoneyTotal(1)); // tai

        TaiXiuInfo myInfo = core.Manager.gI().TaiXiu().get_my_info(p);

        if (myInfo != null) {

            m.writer().writeInt(myInfo.money); // cuoc

            m.writer().writeByte(myInfo.TaiorXiu); // tai or xiu

        } else {

            m.writer().writeInt(0); // cuoc

            m.writer().writeByte(-1); // tai or xiu

        }

        p.addmsg(m);

        m.cleanup();

    }



    @Override

    public void showTableTaiXiu(int type) throws IOException {

        Player p = this.player;

        if (type == 0) {

            Message m = new Message(80);

            m.writer().writeByte(0);

            m.writer().writeByte(0);

            m.writer().writeUTF("Tài xỉu");

            long time_ = core.Manager.gI().TaiXiu().get_time();

            if (time_ < 0) {

                time_ = 0;

            }

            m.writer().writeShort((short) (time_ / 1000)); // time = second

            m.writer().writeInt(core.Manager.gI().TaiXiu().MoneyTotal(0)); // xiu

            m.writer().writeInt(core.Manager.gI().TaiXiu().MoneyTotal(1)); // tai

            TaiXiuInfo myInfo = core.Manager.gI().TaiXiu().get_my_info(p);

            if (myInfo != null) {

                m.writer().writeInt(myInfo.money); // cuoc

                m.writer().writeByte(myInfo.TaiorXiu); // tai or xiu

            } else {

                m.writer().writeInt(0); // cuoc

                m.writer().writeByte(-1); // tai or xiu

            }

            m.writer().writeByte(-1); // kq

            m.writer().write(core.Manager.gI().TaiXiu().get_dice_now());

            p.addmsg(m);

            m.cleanup();

        }

    }



    @Override

    public void sendRebuildPutItem(short idItem, byte cat, short num) throws IOException {

        Message m = new Message(-67);

        m.writer().writeByte(1);

        m.writer().writeShort(idItem);

        m.writer().writeByte(cat);

        m.writer().writeShort(num);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendRebuildActionMsg(byte actionType, String msg) throws IOException {

        Message m = new Message(-67);

        m.writer().writeByte(actionType);

        m.writer().writeUTF(msg);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendRebuildCombineHopDaKham(String msg, short id1, short id2, short num, byte cat) throws IOException {

        Message m = new Message(-67);

        m.writer().writeByte(5);

        m.writer().writeUTF(msg);

        m.writer().writeShort(id1);

        m.writer().writeShort(id2);

        m.writer().writeShort(num);

        m.writer().writeByte(cat);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendRebuildCombineDaSieuCapPutMaterial(byte action, short idItem, byte cat, short num, byte percent) throws IOException {

        Message m = new Message(-67);

        m.writer().writeByte(action);

        m.writer().writeShort(idItem);

        m.writer().writeByte(cat);

        m.writer().writeShort(num);

        m.writer().writeByte(percent);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendRebuildCombineGhepManh(String msg, short index) throws IOException {

        Message m = new Message(-67);

        m.writer().writeByte(31);

        m.writer().writeUTF(msg);

        m.writer().writeShort(index);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendUpgradeDevilShowTable(byte clientType) throws IOException {

        Message m = new Message(45);

        m.writer().writeByte(clientType);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendUpgradeDevilPutMaterial(byte action, byte slot, short id, byte cat, short quantity) throws IOException {

        Message m = new Message(45);

        m.writer().writeByte(action);

        m.writer().writeByte(slot);

        m.writer().writeShort(id);

        m.writer().writeByte(cat);

        m.writer().writeShort(quantity);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendUpgradeDevilPercent(byte action, byte percent) throws IOException {

        Message m = new Message(45);

        m.writer().writeByte(action);

        m.writer().writeByte(percent);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendUpgradeDevilActionMsg(byte action, byte resultType, String msg) throws IOException {

        Message m = new Message(45);

        m.writer().writeByte(action);

        m.writer().writeByte(resultType);

        m.writer().writeUTF(msg);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendUpgradeDevilCraftPanel(String title, byte numIngredients, short[] ingIds, short[] ingQtys, byte[] ingCats, short[] ingIcons, int costBeri, short costRuby, int costExtol, short resultId, short resultQty, byte resultCat, short resultIcon, byte successRate) throws IOException {

        Message m = new Message(45);

        m.writer().writeByte(20);

        m.writer().writeUTF(title);

        m.writer().writeByte(numIngredients);

        for (int i = 0; i < numIngredients; i++) {

            m.writer().writeShort(ingIds[i]);

            m.writer().writeShort(ingQtys[i]);

            m.writer().writeByte(ingCats[i]);

            m.writer().writeShort(ingIcons[i]);

        }

        m.writer().writeInt(costBeri);

        m.writer().writeShort(costRuby);

        m.writer().writeInt(costExtol);

        m.writer().writeShort(resultId);

        m.writer().writeShort(resultQty);

        m.writer().writeByte(resultCat);

        m.writer().writeShort(resultIcon);

        m.writer().writeByte(successRate);

        this.player.addmsg(m);

        m.cleanup();

    }



    @Override

    public void sendTichTieuRubyTable(int point, List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException {

        Message m = new Message(-96);

        m.writer().writeByte(0);

        m.writer().writeInt(point);

        m.writer().writeByte(entries.size());

        for (int i = 0; i < entries.size(); i++) {

            template.TichLuyEntry t = entries.get(i);

            m.writer().writeByte(i);

            m.writer().writeInt(t.num);

            m.writer().writeByte(checkArray[i] == 1 ? 2 : (point >= t.num ? 1 : 0));

            m.writer().writeShort(t.cat.length);

            for (int j = 0; j < t.cat.length; j++) {

                if (t.cat[j] == 110) {
                    Pet petTemp = Pet.getTemplate(t.id[j]);
                    if (petTemp != null) {
                        ItemTemplate4 itTemp4Select = new ItemTemplate4();
                        itTemp4Select.id = t.id[j];
                        itTemp4Select.name = "Pet " + petTemp.name;
                        itTemp4Select.icon = petTemp.icon;
                        m.writer().writeUTF(itTemp4Select.name);
                        m.writer().writeByte(t.cat[j]);
                        m.writer().writeShort(itTemp4Select.icon);
                        m.writer().writeShort(t.quant[j]);
                        m.writer().writeByte(0);
                        continue;
                    }
                }
                if (t.cat[j] == 4 || t.cat[j] == 110) {
                    ItemTemplate4 itTemp4Select = ItemTemplate4.get_it_by_id(t.id[j]);



                    if (t.cat[j] == 110 && t.id[j] == 698) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(730);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 720) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(825);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 771) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(826);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 777) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(868);

                    }



                    if (t.id[j] == -10) {

                        int level = player.level / 10;

                        if (level == 0) {

                            level = 1;

                        }

                        itTemp4Select = ItemTemplate4.get_it_by_id(level + 694);

                    } else if (t.id[j] == -12) {

                        int level = player.level / 10;

                        if (level == 0) {

                            level = 1;

                        }

                        itTemp4Select = ItemTemplate4.get_it_by_id(level + 121);

                    } else if (t.id[j] == -11) {

                        short[] ids = new short[]{49, 55, 61, 67, 73, 79};

                        itTemp4Select = ItemTemplate4.get_it_by_id(ids[0]);

                    } else if (t.id[j] == -13) {

                        itTemp4Select = new ItemTemplate4();
                        itTemp4Select.id = -13;
                        itTemp4Select.name = "Đá thần ngẫu nhiên";
                        itTemp4Select.icon = (ItemTemplate4.get_it_by_id(647) != null) ? ItemTemplate4.get_it_by_id(647).icon : (short) 0;

                    }



                    if (itTemp4Select == null) {

                        itTemp4Select = new ItemTemplate4();

                        itTemp4Select.id = t.id[j];

                        itTemp4Select.name = "Vật phẩm lỗi (" + t.id[j] + ")";

                        itTemp4Select.icon = 0;

                    }



                    switch (itTemp4Select.id) {

                        case 0:

                            if (t.quant[j] == 1000) {

                                m.writer().writeUTF("b " + itTemp4Select.name);

                            } else {

                                m.writer().writeUTF("m " + itTemp4Select.name);

                            }

                            break;

                        case 1:

                            if (t.quant[j] == 1000) {

                                m.writer().writeUTF("m " + itTemp4Select.name);

                            } else {

                                m.writer().writeUTF("k " + itTemp4Select.name);

                            }

                            break;

                        default:

                            m.writer().writeUTF(itTemp4Select.name);

                            break;

                    }



                    m.writer().writeByte(t.cat[j]);

                    m.writer().writeShort(itTemp4Select.icon);

                    m.writer().writeShort(t.quant[j]);

                    m.writer().writeByte(0);

                } else if (t.cat[j] == 105) {

                    ItemFashion itemFashion = ItemFashion.get_item(t.id[j]);

                    if (itemFashion == null) {

                        m.writer().writeUTF("Thời trang lỗi (" + t.id[j] + ")");

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort((short) 0);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    } else {

                        m.writer().writeUTF(itemFashion.name + " Hạn sử dụng " + (itemFashion.hsd > -1 ? itemFashion.hsd + " day" : " vĩnh viễn"));

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort(itemFashion.idIcon);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    }

                } else if (t.cat[j] == 7) {

                    ItemTemplate7 itTemp7Select = ItemTemplate7.get_it_by_id(t.id[j]);

                    if (itTemp7Select == null) {

                        m.writer().writeUTF("Vật phẩm lỗi (" + t.id[j] + ")");

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort((short) 0);

                        m.writer().writeShort(t.quant[j]);

                    } else {

                        m.writer().writeUTF(itTemp7Select.name);

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort(itTemp7Select.icon);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    }

                }

            }

        }

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void sendTichLuyCongDonTable(int pointK, List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException {

        Message m = new Message(-97);

        m.writer().writeByte(0);

        m.writer().writeByte(entries.size());

        for (int i = 0; i < entries.size(); i++) {

            template.TichLuyEntry t = entries.get(i);

            short displayCostK = (short) (t.num >= 10000 ? t.num / 1000 : t.num);

            m.writer().writeShort(displayCostK);

            m.writer().writeInt(pointK);

            byte status = (checkArray != null && i < checkArray.length && checkArray[i] == 1) ? (byte) 2 : (pointK >= displayCostK ? (byte) 1 : (byte) 0);

            m.writer().writeByte(status);

            m.writer().writeByte(t.cat.length);

            for (int j = 0; j < t.cat.length; j++) {

                String name = "";

                short icon = 0;



                byte color = 0;

                if (t.cat[j] == 4 || t.cat[j] == 110) {

                    ItemTemplate4 itTemp4Select = ItemTemplate4.get_it_by_id(t.id[j]);



                    if (t.cat[j] == 110 && t.id[j] == 688) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(688);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 695) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(729);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 697) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(731);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 685) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(685);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 686) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(686);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 698) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(730);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 720) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(825);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 771) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(826);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 774) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(867);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 777) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(868);

                    }



                    if (t.id[j] == -10) {

                        int level = player.level / 10;

                        if (level == 0) {

                            level = 1;

                        }

                        itTemp4Select = ItemTemplate4.get_it_by_id(level + 694);

                    } else if (t.id[j] == -12) {

                        int level = player.level / 10;

                        if (level == 0) {

                            level = 1;

                        }

                        itTemp4Select = ItemTemplate4.get_it_by_id(level + 121);

                    } else if (t.id[j] == -11) {

                        short[] ids = new short[]{49, 55, 61, 67, 73, 79};

                        itTemp4Select = ItemTemplate4.get_it_by_id(ids[0]);

                    }



                    if (itTemp4Select == null) {

                        itTemp4Select = new ItemTemplate4();

                        itTemp4Select.id = t.id[j];

                        itTemp4Select.name = "Vật phẩm (" + t.id[j] + ")";

                        itTemp4Select.icon = 0;

                    }



                    switch (itTemp4Select.id) {

                        case 0:

                            if (t.quant[j] == 1000) {

                                name = "b " + itTemp4Select.name;

                            } else {

                                name = "m " + itTemp4Select.name;

                            }

                            break;

                        case 1:

                            if (t.quant[j] == 1000) {

                                name = "m " + itTemp4Select.name;

                            } else {

                                name = "k " + itTemp4Select.name;

                            }

                            break;

                        default:

                            name = itTemp4Select.name;

                            break;

                    }

                    icon = itTemp4Select.icon;

                    color = (byte) (t.cat[j] == 110 ? 4 : 0);

                } else if (t.cat[j] == 105) {

                    ItemFashion itemFashion = ItemFashion.get_item(t.id[j]);

                    if (itemFashion == null) {

                        name = "Thời trang (" + t.id[j] + ")";

                        icon = 0;

                    } else {

                        name = itemFashion.name + " HSD " + (itemFashion.hsd > -1 ? itemFashion.hsd + " ngày" : "vĩnh viễn");

                        icon = itemFashion.idIcon;

                    }

                    color = 5;

                } else if (t.cat[j] == 7) {

                    ItemTemplate7 itTemp7Select = ItemTemplate7.get_it_by_id(t.id[j]);

                    if (itTemp7Select == null) {

                        name = "Vật phẩm (" + t.id[j] + ")";

                        icon = 0;

                    } else {

                        name = itTemp7Select.name;

                        icon = itTemp7Select.icon;

                        color = 0;

                    }

                } else if (t.cat[j] == 3) {

                    template.ItemTemplate3 itTemp3Select = template.ItemTemplate3.get_it_by_id(t.id[j]);

                    if (itTemp3Select == null) {

                        name = "Trang bị (" + t.id[j] + ")";

                        icon = 0;

                    } else {

                        name = itTemp3Select.name;

                        icon = itTemp3Select.icon;

                        color = (byte) itTemp3Select.color;

                    }

                }

                if (name == null) {

                    name = "";

                }

                m.writer().writeUTF(name);

                m.writer().writeByte(t.cat[j]);

                m.writer().writeShort(icon);

                m.writer().writeShort(t.quant[j]);

                m.writer().writeByte(color);

            }

        }

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void sendTichLuyCongDonClaimSuccess(short cost, int pointK) throws IOException {

        Message m = new Message(-97);

        m.writer().writeByte(2);

        m.writer().writeShort(cost);

        m.writer().writeInt(pointK);

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void sendTichNapTable(int point, List<template.TichLuyEntry> entries, byte[] checkArray) throws IOException {

        Message m = new Message(-90);

        m.writer().writeByte(0);

        m.writer().writeInt(point);

        m.writer().writeByte(entries.size());

        for (int i = 0; i < entries.size(); i++) {

            template.TichLuyEntry t = entries.get(i);

            m.writer().writeByte(i);

            m.writer().writeInt(t.num);

            m.writer().writeByte(checkArray[i] == 1 ? 2 : (point >= t.num ? 1 : 0));

            m.writer().writeShort(t.cat.length);

            for (int j = 0; j < t.cat.length; j++) {
                if (t.cat[j] == 110) {
                    Pet petTemp = Pet.getTemplate(t.id[j]);
                    if (petTemp != null) {
                        ItemTemplate4 itTemp4Select = new ItemTemplate4();
                        itTemp4Select.id = t.id[j];
                        itTemp4Select.name = "Pet " + petTemp.name;
                        itTemp4Select.icon = petTemp.icon;
                        m.writer().writeUTF(itTemp4Select.name);
                        m.writer().writeByte(t.cat[j]);
                        m.writer().writeShort(itTemp4Select.icon);
                        m.writer().writeShort(t.quant[j]);
                        m.writer().writeByte(0);
                        continue;
                    }
                }
                if (t.cat[j] == 4 || t.cat[j] == 110) {
                    ItemTemplate4 itTemp4Select = ItemTemplate4.get_it_by_id(t.id[j]);



                    if (t.cat[j] == 110 && t.id[j] == 688) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(688);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 695) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(729);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 697) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(731);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 685) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(685);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 686) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(686);

                    }

                    if (t.cat[j] == 110 && t.id[j] == 774) {

                        itTemp4Select = ItemTemplate4.get_it_by_id(867);

                    }



                    if (t.id[j] == -10) {

                        int level = player.level / 10;

                        if (level == 0) {

                            level = 1;

                        }

                        itTemp4Select = ItemTemplate4.get_it_by_id(level + 694);

                    } else if (t.id[j] == -11) {

                        short[] ids = new short[]{49, 55, 61, 67, 73, 79};

                        itTemp4Select = ItemTemplate4.get_it_by_id(ids[0]);

                    } else if (t.id[j] == -13) {

                        itTemp4Select = new ItemTemplate4();
                        itTemp4Select.id = -13;
                        itTemp4Select.name = "Đá thần ngẫu nhiên";
                        itTemp4Select.icon = (ItemTemplate4.get_it_by_id(647) != null) ? ItemTemplate4.get_it_by_id(647).icon : (short) 0;

                    }



                    if (itTemp4Select == null) {

                        itTemp4Select = new ItemTemplate4();

                        itTemp4Select.id = t.id[j];

                        itTemp4Select.name = "Vật phẩm lỗi (" + t.id[j] + ")";

                        itTemp4Select.icon = 0;

                    }



                    switch (itTemp4Select.id) {

                        case 0:

                            if (t.quant[j] == 1000) {

                                m.writer().writeUTF("b " + itTemp4Select.name);

                            } else {

                                m.writer().writeUTF("m " + itTemp4Select.name);

                            }

                            break;

                        case 1:

                            if (t.quant[j] == 1000) {

                                m.writer().writeUTF("m " + itTemp4Select.name);

                            } else {

                                m.writer().writeUTF("k " + itTemp4Select.name);

                            }

                            break;

                        default:

                            m.writer().writeUTF(itTemp4Select.name);

                            break;

                    }



                    m.writer().writeByte(t.cat[j]);

                    m.writer().writeShort(itTemp4Select.icon);

                    m.writer().writeShort(t.quant[j]);

                    m.writer().writeByte(0);

                } else if (t.cat[j] == 105) {

                    ItemFashion itemFashion = ItemFashion.get_item(t.id[j]);

                    if (itemFashion == null) {

                        m.writer().writeUTF("Thời trang lỗi (" + t.id[j] + ")");

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort((short) 0);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    } else {

                        m.writer().writeUTF(itemFashion.name + " Hạn sử dụng " + (itemFashion.hsd > -1 ? itemFashion.hsd + " day" : " vĩnh viễn"));

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort(itemFashion.idIcon);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    }

                } else if (t.cat[j] == 7) {

                    ItemTemplate7 itTemp7Select = ItemTemplate7.get_it_by_id(t.id[j]);

                    if (itTemp7Select == null) {

                        m.writer().writeUTF("Vật phẩm lỗi (" + t.id[j] + ")");

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort((short) 0);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    } else {

                        m.writer().writeUTF(itTemp7Select.name);

                        m.writer().writeByte(t.cat[j]);

                        m.writer().writeShort(itTemp7Select.icon);

                        m.writer().writeShort(t.quant[j]);

                        m.writer().writeByte(0);

                    }

                }

            }

        }

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void sendTichNapClaimSuccess(byte id) throws IOException {

        Message m = new Message(-90);

        m.writer().writeByte(2);

        m.writer().writeByte(id);

        sendMessage(m);

        m.cleanup();

    }



    @Override

    public void startInput() {

        if (this.player != null && this.player.inputDialog != null) {

            model.InputDialog dialog = this.player.inputDialog;

            try {

                Message m = new Message(-81);

                m.writer().writeShort(dialog.id);

                m.writer().writeUTF(dialog.title);

                m.writer().writeByte(dialog.options.length);

                for (int i = 0; i < dialog.options.length; i++) {

                    m.writer().writeUTF(dialog.options[i]);

                }

                sendMessage(m);

                m.cleanup();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



    @Override

    public void startYesNo() {

        if (this.player != null && this.player.yesNoDialog != null) {

            model.YesNoDialog dialog = this.player.yesNoDialog;

            try {

                Message m = new Message(-11);

                m.writer().writeShort(dialog.id);

                m.writer().writeByte(2);

                m.writer().writeUTF(dialog.title);

                m.writer().writeUTF(dialog.text);

                m.writer().writeByte(dialog.commands.length);

                for (int i = 0; i < dialog.commands.length; i++) {

                    m.writer().writeUTF(dialog.commands[i]);

                    m.writer().writeByte(i);

                    m.writer().writeByte(dialog.icons[i]);

                }

                sendMessage(m);

                m.cleanup();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



    @Override

    public void send_buff(byte type, short idSkill, short idIcon, short idEffSkill, int time_buff, java.util.List<Short> list_id, java.util.List<Integer> list_par, short[] extraEffs) {

        try {

            Message m = new Message(20);

            m.writer().writeByte(type);

            m.writer().writeShort(idSkill);

            m.writer().writeShort(player.index_map);

            m.writer().writeByte(0);

            m.writer().writeShort(idIcon);

            m.writer().writeShort(idEffSkill);

            m.writer().writeInt(time_buff);

            m.writer().writeByte(0);

            m.writer().writeByte(1);

            m.writer().writeShort(player.index_map);

            m.writer().writeByte(list_id.size());

            for (int i = 0; i < list_id.size(); i++) {

                m.writer().writeByte(list_id.get(i));

                m.writer().writeShort(list_par.get(i));

            }

            if (extraEffs != null) {

                m.writer().writeByte(extraEffs.length);

                for (int i = 0; i < extraEffs.length; i++) {

                    m.writer().writeShort(extraEffs[i]);

                }

            } else if (type == 1) {

                m.writer().writeByte(0);

            } else if (type == 0) {

                m.writer().writeByte(0);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_menu(String title, String[] menuNames, int npcId) {

        try {

            Message m = new Message(-20);

            m.writer().writeByte(0); // no icon

            m.writer().writeShort(npcId);

            m.writer().writeByte(0);

            m.writer().writeUTF(title);

            m.writer().writeByte(menuNames.length);

            for (int i = 0; i < menuNames.length; i++) {

                m.writer().writeUTF(menuNames[i]);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu(int npcId, int menuId, String title, String[] options, short[] icons) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(icons == null ? 0 : 5);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                if (icons != null) {

                    m.writer().writeShort(icons[i]);

                }

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu(int npcId, String title, String[] options) {

        send_dynamic_menu(npcId, 0, title, options, null);

    }



    public void send_dynamic_menu(int npcId, int menuId, String title, String[] options) {

        send_dynamic_menu(npcId, menuId, title, options, null);

    }



    @Override

    public void send_dynamic_menu_type1(int npcId, int menuId, String title, String[] options, byte[] checkStates, byte[] colorStates) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(1);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                m.writer().writeByte(checkStates != null && i < checkStates.length ? checkStates[i] : 0);

                m.writer().writeByte(colorStates != null && i < colorStates.length ? colorStates[i] : 0);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_maps(int npcId, int menuId, String title, int[] mapIds) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            int mapIDCanGo = player.checkQuest();

            Message m = new Message(-20);

            m.writer().writeByte(1);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(mapIds.length);

            boolean isVillageTele = (npcId == 996);
            for (int i = 0; i < mapIds.length; i++) {
                Zone[] mapArr = Zone.getMapByID(mapIds[i]);
                String mapName = (mapArr != null && mapArr.length > 0 && mapArr[0] != null && mapArr[0].template != null)
                        ? mapArr[0].template.name : ("Map " + mapIds[i]);
                m.writer().writeUTF(mapName);
                boolean isCurrent = (player.map != null && player.map.template != null && mapIds[i] == player.map.template.id);
                boolean isUnlocked = isVillageTele || (mapIds[i] <= mapIDCanGo) || Zone.isMapNoQuestLimit(mapIds[i]);
                byte icon = isCurrent ? (byte) 4 : (isUnlocked ? (byte) 2 : (byte) 0);
                byte color = isUnlocked ? (byte) 7 : (byte) 3;
                m.writer().writeByte(icon);
                m.writer().writeByte(color);
            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type1(int npcId, int menuId, String title, String[] options) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(1);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type2(int npcId, int menuId, String title, String[] options) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(2);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void sendDymanicMenuMap(int npcId, String title, String[] options) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(2);

            m.writer().writeShort(npcId);

            m.writer().writeByte(0);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type3(int npcId, int menuId, String title, String[] options, byte[] itemTemplateIds, byte itemType) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(3);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                m.writer().writeShort(itemTemplateIds != null && i < itemTemplateIds.length ? itemTemplateIds[i] : -1);

                m.writer().writeByte(itemType);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type4(int npcId, int menuId, String title, List<String> options, List<Integer> icons) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(4);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.size());

            for (int i = 0; i < options.size(); i++) {

                m.writer().writeUTF(options.get(i));

                m.writer().writeShort(icons != null && i < icons.size() ? icons.get(i).shortValue() : -1);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type5(int npcId, int menuId, String title, String[] options, short[] icons) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(5);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                m.writer().writeShort(icons != null && i < icons.length ? icons[i] : -1);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type6(int npcId, int menuId, String title, String[] options, short[] icons, byte[] colorStates) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(6);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                m.writer().writeShort(icons != null && i < icons.length ? icons[i] : -1);

                m.writer().writeByte(colorStates != null && i < colorStates.length ? colorStates[i] : 0);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void send_dynamic_menu_type7(int npcId, int menuId, String title, String[] options, short[] icons) {

        try {

            if (player == null || player.isdie) return;

            player.currentNpcId = (short) npcId;

            player.currentMenuNpcId = (short) npcId;

            Message m = new Message(-20);

            m.writer().writeByte(7);

            m.writer().writeShort(npcId);

            m.writer().writeByte(menuId);

            m.writer().writeUTF(title);

            m.writer().writeByte(options.length);

            for (int i = 0; i < options.length; i++) {

                m.writer().writeUTF(options[i]);

                m.writer().writeShort(icons != null && i < icons.length ? icons[i] : -1);

            }

            sendMessage(m);

            m.cleanup();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    // ======================================================

    //  LAMBDA MENU (Type 2 Menu Support)

    // ======================================================



    @Override

    public void openMenu() {

        if (player == null) return;

        int npcId = player.currentNpcId != 0 ? player.currentNpcId : -1;

        String title = core.MenuController.get_name_npc(npcId);

        openMenu(npcId, title, player.menus);

    }



    @Override

    public void openMenu(String title) {

        if (player == null) return;

        int npcId = player.currentNpcId != 0 ? player.currentNpcId : -1;

        openMenu(npcId, title, player.menus);

    }



    @Override

    public void openMenu(int npcId, String title) {

        if (player == null) return;

        player.currentNpcId = (short) npcId;

        player.currentMenuNpcId = (short) npcId;

        openMenu(npcId, title, player.menus);

    }



    @Override

    public void openMenu(List<model.Menu> menus) {

        if (player == null) return;

        player.menus = menus != null ? menus : new ArrayList<>();

        openMenu();

    }



    @Override

    public void openMenu(String title, List<model.Menu> menus) {

        if (player == null) return;

        player.menus = menus != null ? menus : new ArrayList<>();

        openMenu(title);

    }



    @Override

    public void openMenu(int npcId, String title, List<model.Menu> menus) {

        openDynamicMenu(npcId, title, menus);

    }



    @Override

    public void openDynamicMenu(int npcId, String title, String[] base, short[] baseIcons) {

        if (player == null || player.isdie) return;

        player.currentNpcId = (short) npcId;

        player.currentMenuNpcId = (short) npcId;

        if (player.menus != null) {

            player.menus.clear();

        }

        short sid = (short) npcId;

        List<zinterfaces.iMenuDymanic.DynamicEntry> dynamics = zinterfaces.iMenuDymanic.getActiveFor(sid);



        List<zinterfaces.iMenuDymanic.MenuSnapshot> snapshots = new ArrayList<>();

        if (base != null) {

            for (int i = 0; i < base.length; i++) {

                snapshots.add(new zinterfaces.iMenuDymanic.MenuSnapshot(i));

            }

        }



        for (zinterfaces.iMenuDymanic.DynamicEntry de : dynamics) {

            if (!de.handler.shouldShow(player)) continue;

            zinterfaces.iMenuDymanic.MenuSnapshot snap = new zinterfaces.iMenuDymanic.MenuSnapshot(de);

            zinterfaces.iMenuDymanic.Position pos = de.handler.getPosition();

            if (pos == zinterfaces.iMenuDymanic.Position.TOP) {

                snapshots.add(0, snap);

            } else if (pos == zinterfaces.iMenuDymanic.Position.INDEX) {

                int idx = de.handler.getPositionIndex();

                idx = Math.max(0, Math.min(idx, snapshots.size()));

                snapshots.add(idx, snap);

            } else {

                snapshots.add(snap);

            }

        }



        player.currentMenuOptions = snapshots;



        boolean hasIcon = (baseIcons != null);

        if (!hasIcon) {

            for (zinterfaces.iMenuDymanic.DynamicEntry de : dynamics) {

                if (de.icon != -1) { hasIcon = true; break; }

            }

        }



        String[] rawNames = new String[snapshots.size()];

        short[] rawIcons = new short[snapshots.size()];



        for (int i = 0; i < snapshots.size(); i++) {

            zinterfaces.iMenuDymanic.MenuSnapshot s = snapshots.get(i);

            if (s.isDynamic) {

                rawNames[i] = s.entry.name;

                rawIcons[i] = s.entry.icon;

            } else {

                rawNames[i] = base[s.baseIndex];

                rawIcons[i] = (baseIcons != null && s.baseIndex < baseIcons.length) ? baseIcons[s.baseIndex] : -1;

            }

        }



        send_dynamic_menu(npcId, 0, title, rawNames, hasIcon ? rawIcons : null);

    }



    @Override

    public void openDynamicMenu(int npcId, String title, List<model.Menu> menus) {

        if (player == null || player.isdie) return;

        player.currentNpcId = (short) npcId;

        player.currentMenuNpcId = (short) npcId;

        player.currentMenuOptions = null;

        short sid = (short) npcId;

        List<zinterfaces.iMenuDymanic.DynamicEntry> dynamics = zinterfaces.iMenuDymanic.getActiveFor(sid);



        List<model.Menu> finalMenus = menus != null ? new ArrayList<>(menus) : new ArrayList<>();



        for (zinterfaces.iMenuDymanic.DynamicEntry de : dynamics) {

            if (!de.handler.shouldShow(player)) continue;

            model.Menu dynMenu = new model.Menu(de.name, de.icon, () -> {

                try {

                    de.handler.handleSelect(player, sid, de.menuId);

                } catch (IOException e) {

                    e.printStackTrace();

                }

            });



            zinterfaces.iMenuDymanic.Position pos = de.handler.getPosition();

            if (pos == zinterfaces.iMenuDymanic.Position.TOP) {

                finalMenus.add(0, dynMenu);

            } else if (pos == zinterfaces.iMenuDymanic.Position.INDEX) {

                int idx = de.handler.getPositionIndex();

                idx = Math.max(0, Math.min(idx, finalMenus.size()));

                finalMenus.add(idx, dynMenu);

            } else {

                finalMenus.add(dynMenu);

            }

        }



        player.menus = finalMenus;

        if (player.menus.isEmpty()) return;



        String[] names = new String[player.menus.size()];

        short[] icons = new short[player.menus.size()];

        boolean hasIcon = false;



        for (int i = 0; i < player.menus.size(); i++) {

            model.Menu menu = player.menus.get(i);

            names[i] = menu.getName();

            icons[i] = menu.getIcon();

            if (menu.getIcon() != -1) {

                hasIcon = true;

            }

        }



        send_dynamic_menu(npcId, 0, title, names, hasIcon ? icons : null);

    }

    

    @Override

    public void openDynamicMenu(int npcId, String title) {

        if (player == null || player.isdie) return;

        player.currentNpcId = (short) npcId;

        player.currentMenuNpcId = (short) npcId;

        player.currentMenuOptions = null;

        short sid = (short) npcId;

        List<zinterfaces.iMenuDymanic.DynamicEntry> dynamics = zinterfaces.iMenuDymanic.getActiveFor(sid);



        List<model.Menu> finalMenus = new ArrayList<>(player.menus);



        for (zinterfaces.iMenuDymanic.DynamicEntry de : dynamics) {

            if (!de.handler.shouldShow(player)) continue;

            model.Menu dynMenu = new model.Menu(de.name, de.icon, () -> {

                try {

                    de.handler.handleSelect(player, sid, de.menuId);

                } catch (IOException e) {

                    e.printStackTrace();

                }

            });



            zinterfaces.iMenuDymanic.Position pos = de.handler.getPosition();

            if (pos == zinterfaces.iMenuDymanic.Position.TOP) {

                finalMenus.add(0, dynMenu);

            } else if (pos == zinterfaces.iMenuDymanic.Position.INDEX) {

                int idx = de.handler.getPositionIndex();

                idx = Math.max(0, Math.min(idx, finalMenus.size()));

                finalMenus.add(idx, dynMenu);

            } else {

                finalMenus.add(dynMenu);

            }

        }



        player.menus = finalMenus;

        if (player.menus.isEmpty()) return;



        String[] names = new String[player.menus.size()];

        short[] icons = new short[player.menus.size()];

        boolean hasIcon = false;



        for (int i = 0; i < player.menus.size(); i++) {

            model.Menu menu = player.menus.get(i);

            names[i] = menu.getName();

            icons[i] = menu.getIcon();

            if (menu.getIcon() != -1) {

                hasIcon = true;

            }

        }



        send_dynamic_menu(npcId, 0, title, names, hasIcon ? icons : null);

    }



    @Override

    public void sendTichTieuRubyClaimSuccess(byte id) throws IOException {

    }



    public void sendTichTieuClaimSuccess(byte id) throws IOException {

        sendTichTieuRubyClaimSuccess(id);

    }



    public void Chest(Message m2) throws IOException {

        Player p = this.player;
        if (p == null || p.item == null || p.isClosed) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }

        synchronized (p.item) {
            byte act = m2.reader().readByte();

            short id = m2.reader().readShort();

            byte cat = m2.reader().readByte();

        int num = m2.reader().readInt();
        if (num <= 0) return;

        if ((act == 1 || act == 2) && cat == 4 && (id == 180 || id == 181 || id == 182)) {

            return;

        }

        if (act == 1) { // cat vao

            if (p.item.able_box() < 1) {

                send_box_ThongBao_OK("Rương đã đầy!");

                return;

            }

            switch (cat) {

                case 3: {
                    if (id < 0 || id >= p.item.bag3.length) return;
                    Item_wear it_select = p.item.bag3[id];

                    if (it_select != null) {

                        Item_wear it_add = new Item_wear();

                        it_add.clone_obj(it_select);

                        p.item.bag3[id] = null;

                        if (!p.item.add_item_box3(it_add)) {

                            p.item.bag3[id] = it_select;

                            send_box_ThongBao_OK("Rương đã đầy!");

                        }

                    }

                    break;

                }

                case 4:
                case 7: {
                    if (p.item.total_item_bag_by_id(cat, id) >= num) {
                        int maxAddable = p.item.get_max_addable_box47(cat, id);
                        int moveNum = Math.min(num, maxAddable);
                        if (moveNum > 0) {
                            p.item.remove_item47(cat, id, moveNum);
                            p.item.add_item_box47(cat, id, moveNum);
                            if (moveNum < num) {
                                send_box_ThongBao_OK("Rương đã đầy, chỉ cất được " + moveNum + " vật phẩm!");
                            }
                        } else {
                            send_box_ThongBao_OK("Rương đã đầy!");
                        }
                    }
                    break;
                }

                default: {
                    return;
                }
            }

            p.item.updateInventory(false);
            p.item.update_Inventory_box(-1, false);

        } else if (act == 2) { // lay ra
            switch (cat) {
                case 3: {
                    if (p.item.able_bag() < 1) {
                        send_box_ThongBao_OK("Hành trang đã đầy!");
                        return;
                    }
                    if (id < 0 || id >= p.item.box3.length) return;
                    Item_wear it_select = p.item.box3[id];
                    if (it_select != null) {
                        Item_wear it_add = new Item_wear();
                        it_add.clone_obj(it_select);
                        p.item.box3[id] = null;
                        if (!p.item.add_item_bag3(it_add)) {
                            p.item.box3[id] = it_select;
                            send_box_ThongBao_OK("Hành trang đã đầy!");
                        }
                    }
                    break;
                }

                case 4:
                case 7: {
                    if (p.item.total_item_box_by_id(cat, id) >= num) {
                        int maxAddable = p.item.get_max_addable_bag47(cat, id);
                        int moveNum = Math.min(num, maxAddable);
                        if (moveNum > 0) {
                            p.item.remove_item47_box(cat, id, moveNum);
                            p.item.add_item_bag47(cat, id, moveNum);
                            if (moveNum < num) {
                                send_box_ThongBao_OK("Hành trang đã đầy, chỉ lấy được " + moveNum + " vật phẩm!");
                            }
                        } else {
                            send_box_ThongBao_OK("Hành trang đã đầy!");
                        }
                    }
                    break;
                }

                default: {
                    return;
                }
            }

            p.item.updateInventory(false);

            p.item.update_Inventory_box(-1, false);

        } else if (act == 4) { // vut bo trong ruong
            if (num <= 0) return;
            switch (cat) {
                case 3: {
                    Item_wear it_select = (id >= 0 && id < p.item.box3.length) ? p.item.box3[id] : null;
                    if (it_select == null) {
                        for (int i = 0; i < p.item.box3.length; i++) {
                            Item_wear c = p.item.box3[i];
                            if (c != null && c.template != null && (c.index == id || c.template.id == id)) {
                                it_select = c;
                                id = (short) i;
                                break;
                            }
                        }
                    }
                    if (it_select != null) {
                        String itemName = it_select.template.name + (it_select.levelUp > 0 ? (" +" + it_select.levelUp) : "");
                        final short fId = id;
                        final Item_wear fIt = it_select;
                        final String fItemName = itemName;
                        boolean needConfirm = isValuableItemToConfirm(cat, id, it_select, itemName);
                        if (needConfirm) {
                            StringBuilder sbDropMsg = new StringBuilder();
                            sbDropMsg.append("Bạn có chắc chắn muốn vứt bỏ ").append(fItemName).append(" trong rương đồ không?\n");
                            if (it_select.isThanTrang()) sbDropMsg.append("• CẢNH BÁO: Đây là Thần Trang cực kỳ quý hiếm!\n");
                            if (it_select.levelUp > 0) sbDropMsg.append("• CẢNH BÁO: Trang bị đã được cường hóa +").append(it_select.levelUp).append("!\n");
                            if (it_select.mdakham != null && it_select.mdakham.length > 0) sbDropMsg.append("• CẢNH BÁO: Trang bị đang được khảm ngọc!\n");
                            if (it_select.typelock == 1) sbDropMsg.append("• Trang bị đang ở trạng thái Khóa.\n");
                            sbDropMsg.append("Vật phẩm quý giá sau khi vứt sẽ biến mất vĩnh viễn và không thể khôi phục!");

                            model.YesNoDialog dialog = new model.YesNoDialog(p, 9999, "Xác Nhận Vứt Bỏ",
                                sbDropMsg.toString(),
                                new String[]{"Chắc chắn", "Hủy"});
                            dialog.setHandler(val -> {
                                if (val == 0) {
                                    p.item.add_item_save(fIt);
                                    p.item.box3[fId] = null;
                                    p.item.update_Inventory_box(-1, false);
                                    send_box_ThongBao_OK("Đã vứt bỏ " + fItemName);
                                }
                            });
                            dialog.startYesNo();
                            return;
                        }
                        p.item.add_item_save(it_select);
                        p.item.box3[id] = null;
                        p.item.update_Inventory_box(-1, false);
                        send_box_ThongBao_OK("Đã vứt bỏ " + itemName);
                    }
                    break;
                }
                case 4:
                case 7: {
                    int totalBox = p.item.total_item_box_by_id(cat, id);
                    if (totalBox >= num) {
                        String name = (cat == 4 ? (ItemTemplate4.get_it_by_id(id) != null ? ItemTemplate4.get_it_by_id(id).name : "vật phẩm") : (ItemTemplate7.get_it_by_id(id) != null ? ItemTemplate7.get_it_by_id(id).name : "vật phẩm"));
                        final short fId = id;
                        final byte fCat = cat;
                        final int fNum = num;
                        final String fName = name;
                        boolean needConfirm = isValuableItemToConfirm(cat, id, null, name);
                        if (needConfirm) {
                            model.YesNoDialog dialog = new model.YesNoDialog(p, 9999, "Xác Nhận Vứt Bỏ",
                                "Bạn có chắc chắn muốn vứt bỏ " + (num > 1 ? (num + " ") : "") + fName + " trong rương đồ không?\n"
                                + "Vật phẩm quý giá sau khi vứt sẽ biến mất vĩnh viễn và không thể khôi phục!",
                                new String[]{"Chắc chắn", "Hủy"});
                            dialog.setHandler(val -> {
                                if (val == 0) {
                                    p.item.remove_item47_box(fCat, fId, fNum);
                                    p.item.update_Inventory_box(-1, false);
                                    send_box_ThongBao_OK("Đã vứt bỏ " + fNum + " " + fName);
                                }
                            });
                            dialog.startYesNo();
                            return;
                        }
                        p.item.remove_item47_box(cat, id, num);
                        p.item.update_Inventory_box(-1, false);
                        send_box_ThongBao_OK("Đã vứt bỏ " + num + " " + name);
                    }
                    break;
                }
            }
        } else if (act == 5) {
            handleExpandStorageMenu(p);
        }
        }
    }

    /**
     * Menu mở rộng ô cấp 1: Lựa chọn mở rộng Hành Trang hoặc Rương Đồ.
     */
    public void handleExpandStorageMenu(Player p) {
        if (p == null || p.item == null || p.isdie) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }

        int curBag = p.item.max_bag & 0xFFFF;
        int maxBag = itemz.Item.MAX_BAG_LIMIT;
        int curBox = p.item.max_box & 0xFFFF;
        int maxBox = itemz.Item.MAX_BOX_LIMIT;

        if (curBag >= maxBag && curBox >= maxBox) {
            send_box_ThongBao_OK("Cả hành trang (" + maxBag + " ô) và rương đồ (" + maxBox + " ô) đều đã mở rộng tối đa!");
            return;
        }

        List<model.Menu> menus = new ArrayList<>();
        menus.add(new model.Menu("Hành trang (" + curBag + "/" + maxBag + ")", () -> {
            try {
                handleSelectStorageTarget(p, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        menus.add(new model.Menu("Rương đồ (" + curBox + "/" + maxBox + ")", () -> {
            try {
                handleSelectStorageTarget(p, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        openDynamicMenu(-9988, "Mở Rộng Ô", menus);
    }

    /**
     * Menu mở rộng ô cấp 2: Chọn số lượng ô mở rộng (6, 12, 18, 24, 30, 126 hoặc ô thiếu tiệm cận).
     */
    public void handleSelectStorageTarget(Player p, boolean isBag) {
        if (p == null || p.item == null || p.isdie) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }

        int cur = (isBag ? p.item.max_bag : p.item.max_box) & 0xFFFF;
        int max = isBag ? itemz.Item.MAX_BAG_LIMIT : itemz.Item.MAX_BOX_LIMIT;
        String name = isBag ? "Hành trang" : "Rương đồ";

        if (cur >= max) {
            send_box_ThongBao_OK(name + " đã mở rộng tối đa (" + max + " ô)!");
            return;
        }

        int remain = max - cur;
        List<Integer> slotOptions = new ArrayList<>();
        int[] BASE_STEPS = {6, 12, 18, 24, 30};
        for (int s : BASE_STEPS) {
            if (s < remain) {
                slotOptions.add(s);
            }
        }
        if (remain >= 126) {
            slotOptions.add(126);
        } else {
            // Gần đến mốc tối đa: hiện nút bằng số ô còn thiếu
            if (!slotOptions.contains(remain)) {
                slotOptions.add(remain);
            }
        }

        if (slotOptions.isEmpty()) {
            slotOptions.add(remain);
        }

        List<model.Menu> subMenus = new ArrayList<>();
        for (int slots : slotOptions) {
            int rubyCost = slots * 2;
            String label = slots + " ô (" + rubyCost + " Ruby)";
            final int fSlots = slots;
            subMenus.add(new model.Menu(label, () -> {
                try {
                    confirmExpandStorage(p, isBag, fSlots);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        openDynamicMenu(-9988, "Mở Rộng " + name + " (" + cur + "/" + max + ")", subMenus);
    }

    /**
     * Xác nhận mở rộng ô qua hộp thoại YesNo.
     */
    public void confirmExpandStorage(Player p, boolean isBag, int slots) {
        if (p == null || p.item == null || p.isdie) return;
        if (p.trade_target != null) {
            send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }

        int cur = (isBag ? p.item.max_bag : p.item.max_box) & 0xFFFF;
        int max = isBag ? itemz.Item.MAX_BAG_LIMIT : itemz.Item.MAX_BOX_LIMIT;
        String name = isBag ? "hành trang" : "rương đồ";
        String nameUpper = isBag ? "Hành Trang" : "Rương Đồ";

        if (cur >= max) {
            send_box_ThongBao_OK(nameUpper + " đã mở rộng tối đa (" + max + " ô)!");
            return;
        }

        int realSlots = Math.min(slots, max - cur);
        if (realSlots <= 0) return;

        int rubyCost = realSlots * 2;
        int nextSlots = cur + realSlots;

        model.YesNoDialog dialog = new model.YesNoDialog(p, 5555, "Mở Rộng " + nameUpper,
                "Bạn có muốn dùng " + rubyCost + " Ruby để mở rộng thêm " + realSlots + " ô " + name + " không?\n"
                + "(Hiện tại: " + cur + " ô -> " + nextSlots + "/" + max + " ô)",
                new String[]{"Đồng ý", "Hủy"},
                new byte[]{-1, -1});

        dialog.setHandler(val -> {
            try {
                if (val == 0) {
                    if (p.item == null || p.isdie) return;
                    if (p.trade_target != null) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                        return;
                    }
                    int current = (isBag ? p.item.max_bag : p.item.max_box) & 0xFFFF;
                    if (current >= max) {
                        p.getService().send_box_ThongBao_OK(nameUpper + " đã mở rộng tối đa (" + max + " ô)!");
                        return;
                    }
                    int actualAdd = Math.min(realSlots, max - current);
                    if (actualAdd <= 0) return;
                    int cost = actualAdd * 2;

                    if (p.get_ngoc() < cost) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + cost + " Ruby để mở rộng " + name + "!");
                        return;
                    }

                    p.update_ngoc(-cost);
                    if (isBag) {
                        p.item.expand_bag(actualAdd);
                        p.item.sendNumCellBag();
                        p.item.updateInventory(false);
                    } else {
                        p.item.expand_box(actualAdd);
                        p.item.sendNumCellBox();
                        p.item.update_Inventory_box(-1, false);
                    }
                    p.item.updateMoney(false);
                    p.getService().send_box_ThongBao_OK("Mở rộng " + name + " thành công!\n(Hiện tại: "
                            + ((isBag ? p.item.max_bag : p.item.max_box) & 0xFFFF) + "/" + max + " ô)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        p.setyesNoDialog(dialog);
        startYesNo();
    }



    public void processUseItem(Message m) throws IOException {

        if (this.player != null) {

            model.UseItem.process(this.player, m);

        }

    }



    public void processClientInput(Message m) throws IOException {

        if (this.player != null) {

            model.ClientInput.process(this.player, m);

        }

    }



    public void processClientYesNo(Message m) throws IOException {

        if (this.player != null) {

            model.ClientYesNo.process(this.player, m);

        }

    }



    public void processQuest(Message m) throws IOException {

        if (this.player != null) {

            model.Quest.process(this.player, m);

        }

    }



    public void processPet(Message m) throws IOException {

        if (this.player != null) {

            model.Pet.process(this.player, m);

        }

    }

    public void processDanhHieu(Message m) throws IOException {

        if (this.player != null) {

            activities.DanhHieu.process(this.player, m);

        }

    }

    public void sendDanhHieuList() throws IOException {

        if (this.player != null) {

            activities.DanhHieu.send_list(this.player);

        }

    }





    public void processDauGia(Message m) throws IOException {

        if (this.player != null) {

            model.DauGia.process(this.player, m);

        }

    }



    public void processPokemon(Message m) throws IOException {

        if (this.player != null) {

            model.Pokemon.process(this.player, m);

        }

    }



    public void processTrade(Message m) throws IOException {

        if (this.player != null) {

            activities.Trade.process(this.player, m);

        }

    }



    public void processFriend(Message m) throws IOException {

        if (this.player != null) {

            activities.Friend.process(this.player, m);

        }

    }



    public void processChat(Message m, int type) throws IOException {

        if (this.player != null) {

            activities.Chat.process(this.player, m, type);

        }

    }



    public void processLearnSkill(Message m) throws IOException {

        if (this.player != null) {

            activities.Learn_Skill.process(this.player, m);

        }

    }



    public void processMarket(Message m) throws IOException {

        if (this.player != null) {

            activities.Market.process(this.player, m);

        }

    }



    public void processPvp(Message m) throws IOException {

        if (this.player != null) {

            activities.Pvp.process(this.player, m);

        }

    }



    public void processRanked(Message m) throws IOException {

        if (this.player != null) {

            rank.Ranked.process(this.player, m);

        }

    }



    public void processParty(Message m) throws IOException {

        if (this.player != null) {

            model.Party.process(this.player, m);

        }

    }



    public void processBuff(Message m) throws IOException {

        if (this.player != null) {

            model.Buff.process(this.player, m);

        }

    }



    public void processChuyenHoa(Message m) throws IOException {

        if (this.player != null) {

            activities.ChuyenHoa.process(this.player, m);

        }

    }



    public void processRebuildItem(Message m) throws IOException {

        if (this.player != null) {

            itemz.Rebuild_Item.process(this.player, m);

        }

    }



    public void processSplitItem(Message m) throws IOException {

        if (this.player != null) {

            itemz.Split_Item.process(this.player, m);

        }

    }



    public void processJoinItem(Message m) throws IOException {

        if (this.player != null) {

            itemz.Join_Item.process(this.player, m);

        }

    }



    public void processUpgradeItem(Message m) throws IOException {

        if (this.player != null) {

            itemz.UpgradeItem.process(this.player, m);

        }

    }



    public void processUpgradeSuperItem(Message m) throws IOException {

        if (this.player != null) {

            itemz.UpgradeSuperItem.process(this.player, m);

        }

    }



    public void processUpgradeDial(Message m) throws IOException {

        if (this.player != null) {

            itemz.UpgradeDial.process(this.player, m);

        }

    }



    public void processUpgradeSkin(Message m) throws IOException {

        if (this.player != null) {

            Upgrade_Skin.process(this.player, m);

        }

    }



    public void processShip(Message m) throws IOException {

        if (this.player != null) {

            activities.Ship.process(this.player, m);

        }

    }



    public void processTableTickOption(Message m) throws IOException {

        if (this.player != null) {

            if (player.tableTickOption != null && !player.tableTickOption.is_finish) {

                byte type = m.reader().readByte();

                short idDialog = m.reader().readShort();

                if (player.tableTickOption.idDialog == idDialog) {

                    player.tableTickOption.processResponse(player, type);

                }

            }

        }

    }



    public void processMaxLevel(Message m) throws IOException {

        if (this.player != null) {

            byte act = m.reader().readByte();

            short id = m.reader().readShort();

            if (act == 0 && player.level >= 100) {

                if (player.pointAttributeThongThao < 1) {

                    player.getService().send_box_ThongBao_OK("Không đủ 1 điểm thông thạo");

                    return;

                }

                String[] name = new String[] {"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ",

                        "HP+", "Tăng tấn công"};

                short[] id_op = new short[]{27, 16, 26, 4, 15, 1};

                for (int i = 0; i < id_op.length; i++) {

                    if (id_op[i] == id) {

                        player.data_yesno = new int[] {i};

                        player.setyesNoDialog(new model.YesNoDialog(player, 41, "Thông báo",

                                ("Bạn có muốn cộng 1 điểm vào " + name[i] + "?"),

                                new String[] {"Đồng ý", "Hủy"}, new byte[] {2, 1}));

                        player.getService().startYesNo();

                        break;

                    }

                }

            }

        }

    }



    public void processHanhTrinh(Message m) throws IOException {
        activities.HanhTrinh.process(this.player, m);
    }



    public void processWanted(Message m2) throws IOException {

        Player p = this.player;

        if (p == null) return;

        byte act = m2.reader().readByte();

        switch (act) {

            case -1: { // exit map
                activities.Wanted.remove_player_wait(p);
                p.return_to_previous_map();
                break;
            }

            case 1: { // find

                activities.Wanted.add_player_wait(p);

                Message m = new Message(-85);

                m.writer().writeByte(1);

                p.addmsg(m);

                m.cleanup();

                break;

            }

            case 3: { // stop find

                activities.Wanted.remove_player_wait(p);

                Message m = new Message(-85);

                m.writer().writeByte(3);

                p.addmsg(m);

                m.cleanup();

                break;

            }

        }

    }



    public void processWantedChest(Message m2) throws IOException {

        Player p = this.player;

        if (p == null) return;

        byte act = m2.reader().readByte();

        short id = m2.reader().readShort();

        if (act == 0) {

            for (int i = 0; i < p.wanted_chest.length; i++) {

                if (p.wanted_chest[i] != null && p.wanted_chest[i].id == id) {

                    long time = (p.wanted_chest[i].timeUse - System.currentTimeMillis());

                    if (time < 0) {

                        List<GiftBox> list_gift = new ArrayList<>();

                        int beri_receiv = core.ZUtil.random(10_000, 50_000);

                        GiftBox gb_beri = new GiftBox();

                        template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(0);

                        if (it_temp4 != null) {

                            gb_beri.id = it_temp4.id;

                            gb_beri.type = 4;

                            gb_beri.name = it_temp4.name;

                            gb_beri.icon = it_temp4.icon;

                            gb_beri.num = beri_receiv;

                            gb_beri.color = 0;

                            list_gift.add(gb_beri);

                        }

                        if (60 > core.ZUtil.random(120)) {

                            GiftBox gb_ = new GiftBox();

                            it_temp4 = template.ItemTemplate4.get_it_by_id(18 + (p.level / 10));

                            if (it_temp4 != null) {

                                gb_.id = it_temp4.id;

                                gb_.type = 4;

                                gb_.name = it_temp4.name;

                                gb_.icon = it_temp4.icon;

                                gb_.num = core.ZUtil.random(1, 3);

                                gb_.color = 0;

                                list_gift.add(gb_);

                            }

                        }

                        if (p.level >= 10 && 40 > core.ZUtil.random(120)) {

                            GiftBox gb_ = new GiftBox();

                            it_temp4 = template.ItemTemplate4.get_it_by_id(111 + (p.level / 10));

                            if (it_temp4 != null) {

                                gb_.id = it_temp4.id;

                                gb_.type = 4;

                                gb_.name = it_temp4.name;

                                gb_.icon = it_temp4.icon;

                                gb_.num = core.ZUtil.random(1, 3);

                                gb_.color = 0;

                                list_gift.add(gb_);

                            }

                        }

                        if (p.level >= 10 && 30 > core.ZUtil.random(120)) {

                            GiftBox gb_ = new GiftBox();

                            it_temp4 = template.ItemTemplate4.get_it_by_id(121 + (p.level / 10));

                            if (it_temp4 != null) {

                                gb_.id = it_temp4.id;

                                gb_.type = 4;

                                gb_.name = it_temp4.name;

                                gb_.icon = it_temp4.icon;

                                gb_.num = core.ZUtil.random(1, 3);

                                gb_.color = 0;

                                list_gift.add(gb_);

                            }

                        }

                        if (p.level >= 10 && 20 > core.ZUtil.random(120)) {

                            GiftBox gb_ = new GiftBox();

                            it_temp4 = template.ItemTemplate4.get_it_by_id(29);

                            if (it_temp4 != null) {

                                gb_.id = it_temp4.id;

                                gb_.type = 4;

                                gb_.name = it_temp4.name;

                                gb_.icon = it_temp4.icon;

                                gb_.num = core.ZUtil.random(1, 3);

                                gb_.color = 0;

                                list_gift.add(gb_);

                            }

                        }

                        if (p.level >= 10 && 5 > core.ZUtil.random(120)) {

                            GiftBox gb_ = new GiftBox();

                            it_temp4 = template.ItemTemplate4.get_it_by_id(158);

                            if (it_temp4 != null) {

                                gb_.id = it_temp4.id;

                                gb_.type = 4;

                                gb_.name = it_temp4.name;

                                gb_.icon = it_temp4.icon;

                                gb_.num = core.ZUtil.random(1, 3);

                                gb_.color = 0;

                                list_gift.add(gb_);

                            }

                        }

                        send_gift(p, 1, "Rương truy nã", "Phần thưởng", list_gift, true);

                        p.wanted_chest[i] = null;

                        model.Wanted_Chest.send_box(p);

                    } else {

                        send_box_ThongBao_OK("Mở sau " + core.ZUtil.get_time_str_by_sec2(time) + " nữa");

                    }

                    break;

                }

            }

        }

    }



    public void processGiftChoice(Message m2) throws IOException {

        Player p = this.player;

        if (p == null) return;

        short idItem = m2.reader().readShort();

        byte cat = m2.reader().readByte();

        byte select = m2.reader().readByte();

        activities.GiftChoice.handle(p, idItem, cat, select);

    }



    public void sendSudoInfo(boolean save_cache) throws IOException {
        Player p = this.player;
        if (p == null) return;
        activities.Sudo mySudo = activities.Sudo.getSuDo(p);
        if (mySudo != null) {
            Message m = new Message(-108);
            m.writer().writeByte(13); // case 13
            m.writer().writeShort(p.index_map); // player ID in map
            m.writer().writeShort(mySudo.id); // Sudo id
            m.writer().writeByte(mySudo.chucInSudo); // 1 = Sư phụ, 2 = Đệ tử
            m.writer().writeShort(mySudo.lvExp); // level
            m.writer().writeUTF(String.valueOf(mySudo.point)); // diemSudo
            if (save_cache) {
                p.msgs.add(m);
            } else {
                sendMessage(m);
            }
            m.cleanup();
        } else {
            sendSudoClear(p);
        }
    }

    public void sendSudoClear(Player target) throws IOException {
        if (target == null) return;
        Message m = new Message(-108);
        m.writer().writeByte(10); // case 10
        m.writer().writeShort(target.index_map);
        target.addmsg(m);
        m.cleanup();
    }

    public void sendSudoMemberList() throws IOException {
        Player p = this.player;
        if (p == null) return;
        activities.Sudo mySudo = activities.Sudo.getSuDo(p);
        if (mySudo == null) return;

        Message m = new Message(-108);
        m.writer().writeByte(2); // case 2

        List<activities.Sudo> validList = new ArrayList<>();
        if (mySudo.nameRelative != null) {
            for (int i = 0; i < mySudo.nameRelative.size(); i++) {
                activities.Sudo temp = activities.Sudo.getSuDoByName(mySudo.nameRelative.get(i));
                if (temp != null) {
                    validList.add(temp);
                }
            }
        }
        m.writer().writeByte(validList.size());

        for (activities.Sudo temp : validList) {
            Player p0 = map.Zone.get_player_by_name_allmap(temp.name);
            if (p0 != null) {
                temp.name = p0.name;
                temp.head = p0.head;
                temp.hair = p0.hair;
                temp.hat = p0.get_hat();
            }
            m.writer().writeUTF((temp.chucInSudo == 1 ? "Sư phụ" : "Đệ tử") + (temp.id == p.IDPlayer ? " (bản thân)" : ""));
            m.writer().writeUTF(temp.name);
            m.writer().writeShort(temp.head);
            m.writer().writeShort(temp.hair);
            m.writer().writeShort(temp.hat);
            m.writer().writeByte(temp.chucInSudo);
            m.writer().writeShort(temp.lvExp);
            m.writer().writeUTF(temp.point + "");
            m.writer().writeByte(p0 != null ? 1 : 0);
        }

        p.addmsg(m);
        m.cleanup();
    }

    public void processSudo(Message m2) throws IOException {
        Player p = this.player;
        if (p == null) return;

        byte type = m2.reader().readByte();
        activities.Sudo mySudo = activities.Sudo.getSuDo(p);

        if (mySudo != null || type == 19) {
            if (type == 3 && m2.reader().available() == 0) {
                Message m = new Message(-108);
                m.writer().writeByte(3);
                m.writer().writeByte(mySudo.chucInSudo);
                m.writer().writeByte(mySudo.point); // lv
                m.writer().writeShort(mySudo.lvExp); // lv exp
                m.writer().writeShort(mySudo.getExp()); // exp

                activities.Sudo spSudo = (mySudo.nameRelative != null && !mySudo.nameRelative.isEmpty()) ? activities.Sudo.getSuDoByName(mySudo.nameRelative.get(0)) : null;

                if (spSudo != null && spSudo.nameRelative != null) {
                    m.writer().writeByte(Math.max(0, spSudo.nameRelative.size() - 1));
                    for (int i = 0; i < spSudo.nameRelative.size() - 1; i++) {
                        m.writer().writeUTF(activities.Sudo.getOpBySize(i, mySudo.lvExp));
                    }
                } else {
                    m.writer().writeByte(0);
                }

                p.addmsg(m);
                m.cleanup();
            } else if (type == 2 && m2.reader().available() == 0) {
                sendSudoMemberList();
            } else if (type == 19 && m2.reader().available() == 4) {
                int id = m2.reader().readInt();
                Player p0 = map.Zone.get_player_by_Index_allmap(id);
                if (p0 != null) {
                    if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                        send_box_ThongBao_OK("Không thể bái sư hoặc nhận đệ tử là lính đánh thuê / đệ tử!");
                        return;
                    }

                    if (p0.IDPlayer == p.IDPlayer) {
                        send_box_ThongBao_OK("Không thể tự bái sư chính mình!");
                        return;
                    }

                    activities.Sudo mySudo0 = activities.Sudo.getSuDoByName(p0.name);
                    activities.Sudo mySudoP = activities.Sudo.getSuDoByName(p.name);

                    if (mySudoP != null) {
                        send_box_ThongBao_OK("Bạn đã có sư môn, không thể bái sư thêm!");
                        return;
                    }

                    if (mySudo0 == null || mySudo0.chucInSudo != 1) {
                        send_box_ThongBao_OK(p0.name + " chưa mở chức năng Sư Phụ tại NPC Garp!");
                        return;
                    }

                    if (mySudo0.nameRelative != null && mySudo0.nameRelative.size() >= 4) {
                        send_box_ThongBao_OK(p0.name + " đã nhận đủ 3 đệ tử!");
                        return;
                    }

                    p0.data_yesno = new int[]{80, p.index_map};
                    p0.setyesNoDialog(new model.YesNoDialog(p0, 80, "Thông báo", (p.name + " muốn làm đệ tử của bạn?"), new String[]{"Đồng ý", "Từ chối"}, new byte[]{2, 1}));
                    p0.getService().startYesNo();
                    send_box_ThongBao_OK("Đã gửi yêu cầu bái sư tới " + p0.name + ", xin hãy đợi...");
                }
            } else if (type == 17 && mySudo != null && mySudo.chucInSudo == 2 && mySudo.nameRelative != null && !mySudo.nameRelative.isEmpty()) {
                if (m2.reader().available() >= 4) {
                    m2.reader().readInt();
                }
                String spName = mySudo.nameRelative.get(0);
                activities.Sudo spSudo = activities.Sudo.getSuDoByName(spName);
                if (spSudo != null && spSudo.nameRelative != null) {
                    spSudo.nameRelative.remove(mySudo.name);
                }
                activities.Sudo.removeSudo(mySudo.name);
                activities.Sudo.updateDb();

                sendSudoClear(p);
                send_box_ThongBao_OK("Bạn đã rời khỏi sư môn!");

                Player pMaster = map.Zone.get_player_by_name_allmap(spName);
                if (pMaster != null && pMaster.getService() != null) {
                    pMaster.getService().send_box_ThongBao_OK(p.name + " đã rời khỏi sư môn!");
                    pMaster.getService().sendSudoMemberList();
                }
            } else if (type == 16 && mySudo != null && mySudo.chucInSudo == 1) {
                String name = m2.reader().readUTF();
                byte b = m2.reader().readByte();

                if (mySudo.nameRelative != null) {
                    mySudo.nameRelative.remove(name);
                }
                activities.Sudo.removeSudo(name);
                activities.Sudo.updateDb();

                Player p0 = map.Zone.get_player_by_name_allmap(name);
                if (p0 != null && p0.getService() != null) {
                    p0.getService().sendSudoClear(p0);
                    p0.getService().send_box_ThongBao_OK("Bạn đã bị trục xuất khỏi sư môn!");
                }

                send_box_ThongBao_OK("Đã trục xuất " + name + " khỏi sư môn!");
                sendSudoMemberList();
            } else if (type == 15 && mySudo != null) {
                String chatContent = m2.reader().readUTF();
                byte chucvu = m2.reader().readByte();
                if (chatContent != null && !chatContent.trim().isEmpty()) {
                    activities.Sudo spSudo = (mySudo.chucInSudo == 1) ? mySudo : (mySudo.nameRelative != null && !mySudo.nameRelative.isEmpty() ? activities.Sudo.getSuDoByName(mySudo.nameRelative.get(0)) : null);
                    if (spSudo != null && spSudo.nameRelative != null) {
                        for (String memberName : spSudo.nameRelative) {
                            Player memberPlayer = map.Zone.get_player_by_name_allmap(memberName);
                            if (memberPlayer != null) {
                                Message mChat = new Message(-108);
                                mChat.writer().writeByte(8);
                                mChat.writer().writeByte(0);
                                mChat.writer().writeShort(0);
                                mChat.writer().writeShort(p.index_map);
                                mChat.writer().writeUTF(p.name);
                                mChat.writer().writeUTF(chatContent);
                                mChat.writer().writeLong(System.currentTimeMillis());
                                memberPlayer.addmsg(mChat);
                                mChat.cleanup();
                            }
                        }
                    }
                }
            }
        } else {
            sendSudoClear(p);
            send_box_ThongBao_OK("Bạn hiện tại chưa có sư đồ");
        }
    }

    public void processFight(Message m) throws IOException {
        Player p = this.player;
        if (p == null || p.map == null) return;
        p.map.getService().processFight(p, m);
    }

    public void processArchiPrivatePass(Message m) throws IOException {
        Player p = this.player;
        if (p == null) return;
        achievement.ArchiPrivatePass.handle(p, m);
    }

    public void processArchiDaily(Message m) throws IOException {
        Player p = this.player;
        if (p == null) return;
        achievement.ArchiDaily.handle(p, m);
    }
}