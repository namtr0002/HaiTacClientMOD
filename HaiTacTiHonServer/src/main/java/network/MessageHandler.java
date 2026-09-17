package network;

import network.Service;
import model.ListNapHangNgay;
import model.TichTieuRuby;
import effect.DataEffect;
import map.zones.Red_Line;
import map.zones.TranChienKhongLo;
import itemz.UpgradeItem;
import map.zones.WorldWar;
import bot.BotTruyNa;
import map.zones.ChiemDao;
import map.zones.ThuLinhBienKhoi;
import event.SuKienHalloween;
import java.io.IOException;
import activities.*;
import clan.Clan;
import clan.ClanMember;
import event.EventData;
import model.Player;
import model.Quest;
import model.UseItem;
import model.Wanted_Chest;
import static bot.BotTruyNa.idmap;
import static bot.BotTruyNa.isMap;
import core.*;
import event.SuKienKinhKibi;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import map.Zone;
import mob.Mob;
import template.DataTemplate;
import template.EffTemplate;
import template.ItemBoat;
import template.ItemFashionP;
import template.ItemFashionP2;
import template.ItemTemplate4;
import event.SuKienTrongCay;
import model.VongQuay;
import model.VongQuayOcSen;
import model.VongQuayWC;
import zabstracts.AbsVongQuay;

public class MessageHandler {

    private Session conn;

    public MessageHandler(Session session) {
        this.conn = session;
    }

    public void logData(Message m) {
        try {
            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream("BUG/_" + m.cmd + "_" + System.nanoTime()))) {
                byte[] data = new byte[m.reader().available()];
                m.reader().read(data, 0, data.length);

                dos.write(data);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                DataInputStream newDis = new DataInputStream(byteArrayInputStream);

                m.setReader(newDis);
            }
        } catch (IOException e) {

        }
    }

    public void process_msg(Message m) throws IOException {
        // System.out.println(m.cmd);
        // if(conn.user != null && conn.user.equals("lovesukem")) {
        // logData(m);
        // Log.gI().add_log("BUGVCL", m.cmd + "");
        // }
        // if(conn.p != null && conn.p.isClosed) {
        // Log.gI().add_log("BUGVCL", m.cmd + " " + conn.user);
        // conn.disconnectSC();
        // return;
        // }
        switch (m.cmd) {
            case -17: {
                if (conn.p != null) {
                    short idSkill = m.reader().readShort();
                    short points = m.reader().readShort();
                    conn.p.addPointSkill(idSkill, points);
                }
                break;
            }
            case -107: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    Message mResponse = new Message(-107);
                    mResponse.writer().writeByte(type);
                    if (type == 1) {
                        short[] potions = new short[] { 0, 1, 2, 3, 4, 5, 23, 24, 25 };
                        mResponse.writer().writeShort(potions.length);
                        for (short potion : potions) {
                            mResponse.writer().writeShort(potion);
                        }
                    } else if (type == 2) {
                        short[] pokemons = new short[] { 115, 116, 117, 118, 119 };
                        mResponse.writer().writeShort(pokemons.length);
                        for (short pk : pokemons) {
                            mResponse.writer().writeShort(pk);
                        }
                    }
                    conn.addmsg(mResponse);
                    mResponse.cleanup();
                }
                break;
            }
            case 30: {
                // client loaded map change link successfully
                break;
            }
            case 34: {
                // client requested next map data
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.send_data(conn.p);
                }
                break;
            }
            case -55: {
                // anti-cheat response placeholder
                break;
            }
            case -65: {
                // help request placeholder
                break;
            }
            case -110: {
                if (conn.p != null) {
                    byte action = m.reader().readByte();
                    int mailId = (m.reader().available() >= 4) ? m.reader().readInt() : 0;
                    if (action == 1) {
                        core.MailService.claimMailGift(conn.p, mailId);
                    } else if (action == 2) {
                        core.MailService.deleteMail(conn.p, mailId);
                    } else if (action == 3) {
                        core.MailService.markMailRead(conn.p, mailId);
                    } else if (action == 4) {
                        core.MailService.sendAllMailsOnLogin(conn.p);
                    }
                }
                break;
            }
            case -108: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Sư đồ!");
                        break;
                    }
                    conn.p.getService().processSudo(m);
                }
                break;
            }
            case -95: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Bang hội!");
                        break;
                    }
                    clan.ClanService.processHuyHieuHanhTrinh(conn.p, m);
                }
                break;
            }
            case -87: {
                if (event.EventManager.isActive(1) && conn.p != null && conn.p.map != null && !conn.p.isdie) { // sk bat
                    // pokemon
                    conn.p.getService().processPokemon(m);
                }
                break;
            }
            case -91: {
                if (conn.p != null) { // dau gia
                    conn.p.getService().processDauGia(m);
                }
                break;
            }
            case 77: {
                if (conn.p != null) {
                    byte act77 = m.reader().readByte();
                    VongQuayOcSen.gI().process(conn.p, act77, m);
                }
                break;
            }
            case 82: {
                if (conn.p != null) {
                    byte act82 = m.reader().readByte();
                    VongQuayWC.gI().process(conn.p, act82, m);
                }
                break;
            }
            case 101: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Bang hội!");
                        break;
                    }
                    clan.ClanService.processClanFight(conn.p, m);
                }
                break;
            }
            case -97: {
                if (conn.p != null) {
                    zabstracts.AbsTichLuyCongDon tichCongdon = zabstracts.AbsTichLuyCongDon.get(conn.p.typeTichLuyCongDon);
                    if (tichCongdon != null) {
                        tichCongdon.process(conn.p, m);
                    }
                }
                break;
            }
            case -96: {
                if (conn.p != null) {
                    int tichTieuType = conn.p.typeShopTichTieuSuKien > 0 ? conn.p.typeShopTichTieuSuKien : conn.p.typeShopTichTieu;
                    zabstracts.AbsTichTieuRuby tichTieu = zabstracts.AbsTichTieuRuby.get(tichTieuType);
                    if (tichTieu != null) {
                        tichTieu.process(conn.p, m);
                    }
                }
                break;
            }
            case -90: {
                if (conn.p != null) {
                    int tichNapType = conn.p.typeShopTichNapSuKien > 0 ? conn.p.typeShopTichNapSuKien : conn.p.typeShopTichNap;
                    zabstracts.AbsListTichNap tichNap = zabstracts.AbsListTichNap.get(tichNapType);
                    if (tichNap != null) {
                        tichNap.process(conn.p, m);
                    }
                }
                break;
            }
            case 69: {
                if (conn.p != null) {
                    conn.p.getService().processGiftChoice(m);
                }
                break;
            }
            case -86: {
                if (conn.p != null) {
                    conn.p.getService().processWantedChest(m);
                }
                break;
            }
            case -85: {
                if (conn.p != null) {
                    conn.p.getService().processWanted(m);
                }
                break;
            }
            case -35: {
                if (conn.p != null) {
                    conn.p.getService().processFight(m);
                }
                break;
            }
            case -80: {
                if (conn.p != null) {
                    conn.p.getService().processPet(m);
                }
                break;
            }
            case -109: {
                if (conn.p != null) {
                    conn.p.getService().processDanhHieu(m);
                }
                break;
            }

            case 79: {
                if (conn.p != null) {
                    conn.p.getService().processHanhTrinh(m);
                }
                break;
            }
            case -94: {
                if (conn.p != null) {
                    conn.p.getService().processUpgradeDial(m);
                }
                break;
            }
            case 81: {
                if (conn.p != null) {
                    conn.p.getService().processUpgradeSkin(m);
                }
                break;
            }
            case -53: {
                if (conn.p != null) {
                    conn.p.getService().processShip(m);
                }
                break;
            }
            case 74: {
                try {
                    int avail = m.reader().available();
                    if (avail >= 3) {
                        byte type = m.reader().readByte();
                        short id = m.reader().readShort();
                        if (type == 0) {
                            DataEffect.sendData(conn, id);
                        }
                    } else if (avail >= 2) {
                        short id = m.reader().readShort();
                        DataEffect.sendData(conn, id);
                    }
                } catch (Exception ignored) {
                }
                break;
            }

            case -43: {
                if (conn.p != null) {
                    conn.p.exitSpectatorMode();
                }
                break;
            }
            case -44: {
                try {
                    if (m.reader().available() >= 2) {
                        short id = m.reader().readShort();
                        effect.DataEffectAuto.sendData(conn, id);
                    }
                } catch (Exception ignored) {
                }
                break;
            }
            case 43: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    byte value = m.reader().readByte();
                    if (type == 0) {
                        if (value == 1 || value == 0) {
                            conn.p.is_show_hat = (value == 0);
                            conn.p.update_wearing_to_all();
                            conn.p.getService().send_box_ThongBao_OK(conn.p.is_show_hat ? "Đã bật hiển thị nón"
                                            : "Đã tắt hiển thị nón");
                        }
                    } else if (type == 1) {
                        if (value == 1 || value == 0) {
                            conn.p.is_show_weapon = (value == 0);
                            conn.p.update_wearing_to_all();
                            conn.p.getService().send_box_ThongBao_OK(conn.p.is_show_weapon ? "Đã bật hiển thị vũ khí thời trang"
                                            : "Đã tắt hiển thị vũ khí thời trang");
                        }
                    }
                }
                break;
            }
            case 47: {
                if (conn.p != null && m.reader().available() > 0) {
                    byte action = m.reader().readByte();
                    event.EventManager.dispatchCmdEvent(conn.p, action);
                }
                break;
            }
            case -63: {
                if (conn.p != null) {
                    conn.p.getService().processPvp(m);
                }
                break;
            }
            case 68: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.tocSuper++;
                    if (conn.p.tocSuper > 2) {
                        conn.p.tocSuper = 0;
                    }
                    for (int i = 0; i < conn.p.map.players.size(); i++) {
                        Player p0 = conn.p.map.players.get(i);
                        p0.getService().charWearing(conn.p, false);
                    }
                }
                break;
            }

            case -36: {
                int id = m.reader().readInt();
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Dịch chuyển!");
                        break;
                    }
                    Player p0 = null;
                    for (int i = 0; i < conn.p.friend_list.size(); i++) {
                        if (conn.p.friend_list.get(i).id == id) {
                            p0 = Zone.get_player_by_id_allmap(conn.p.friend_list.get(i).playerId);
                            break;
                        }
                    }
                    if (p0 == null) {
                        for (int i = 0; i < conn.p.enemy_list.size(); i++) {
                            if (conn.p.enemy_list.get(i).id == id) {
                                p0 = Zone.get_player_by_id_allmap(conn.p.enemy_list.get(i).playerId);
                                break;
                            }
                        }
                    }
                    boolean check = false;
                    if (p0 != null) {
                        if (Zone.map_cant_save_site(p0.map.template.id)) {
                            conn.p.getService().send_box_ThongBao_OK("Không thể dịch chuyển đến lúc này");
                            return;
                        }
                        for (int i = 0; i < p0.friend_list.size(); i++) {
                            if (p0.friend_list.get(i).playerId == conn.p.IDPlayer) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            for (int i = 0; i < conn.p.enemy_list.size(); i++) {
                                if (conn.p.enemy_list.get(i).playerId == p0.IDPlayer) {
                                    check = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (check) {
                        if (conn.p.ship_pet != null) {
                            conn.p.getService().send_box_ThongBao_OK("Đang buôn chuyển con đĩ mẹ mày à?");
                        } else {
                            conn.p.data_yesno = new int[] { id };
                            conn.p.setyesNoDialog(new model.YesNoDialog(conn.p, 43, "Thông báo",
                                    ("Dịch chuyển đến người này mất 5 ruby, xác nhận dịch chuyển?"),
                                    new String[] { "5", "Không" }, new byte[] { 7, -1 }));
                            conn.p.getService().startYesNo();
                        }
                    } else {
                        conn.p.getService().send_box_ThongBao_OK("Đối phương không online hoặc không có trong danh sách");
                    }
                }
                break;
            }

            case -52: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Bang hội!");
                        break;
                    }
                    byte type = m.reader().readByte();
                    Clan.process(conn.p, m, type);
                }
                break;
            }
            case 49: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    conn.p.getService().processMaxLevel(m);
                }
                break;
            }
            case 80: { // event
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia Tài xỉu!");
                        break;
                    }
                    Player p = conn.p;
                    if (!p.checkPassRuong()) {
                        break;
                    }
                    byte type = m.reader().readByte();
                    byte act = m.reader().readByte();
                    int money = -1;
                    byte TaiorXiu = -1;
                    byte isAll = -1;
                    try {
                        money = m.reader().readInt();
                        TaiorXiu = m.reader().readByte();
                        isAll = m.reader().readByte();
                    } catch (IOException e) {
                    }

                    if (type == 0 && act == 3 && money == -1 && TaiorXiu == -1 && isAll == -1) {
                        p.getService().updateInfoTaiXiu();
                        long time = core.Manager.gI().TaiXiu().get_time();
                        if (time > 5_000 && time < (core.TaiXiu.TIME_ROUND - 25_000)) {
                            p.getService().showTableTaiXiu(0);
                        }
                    } else if (type == 0 && act == 2 && money == -1 && TaiorXiu == -1 && isAll == -1) {
                        p.getService().notice_dice_TaiXiu();
                    } else if (type == 0 && act == 0 && money == -1 && TaiorXiu == -1 && isAll == -1) {
                        p.getService().showTableTaiXiu(0);
                        template.TaiXiuInfo t = core.Manager.gI().TaiXiu().get_my_result(p);
                        if (t != null) {
                            if (t.isReceive == 0) {
                                t.isReceive = 1;
                                p.update_vang(t.money);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận " + t.money + " beri");
                                core.Manager.gI().TaiXiu().remove_result(p);
                            }
                        }
                    } 
                    else if (type == 0 && act == 1 && money > 0 && (TaiorXiu == 1 || TaiorXiu == 0)
                            && isAll == 0) {
                        core.Manager.gI().TaiXiu().register(p, money, TaiorXiu);
                    } 
                    else if (type == 0 && act == 1 && money > 0 && (TaiorXiu == 1 || TaiorXiu == 0)
                            && isAll == 1) {
                        if (p.get_vang() < money) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + money + " beri");
                            break;
                        }
                        core.Manager.gI().TaiXiu().register(p, money, TaiorXiu);
                    }
                }
                break;
            }
            case -71: { // auto revive
                if (conn.p != null && conn.p.map != null && conn.p.map.map_vp == null
                        && conn.p.map.map_little_garden == null
                        && conn.p.time_hs_little_garden < System.currentTimeMillis()) {
                    if (conn.p.type_pk == -1 && conn.p.typePirate == -1 && conn.p.pointPk == 0) {
                        if (m.reader().readByte() == 1) {
                            if (conn.p.item.total_item_bag_by_id(4, 89) > 0) {
                                conn.p.item.remove_item47(4, 89, 1);
                                conn.p.item.updateInventory(false);
                                conn.p.isdie = false;
                                conn.p.getService().use_potion(0, conn.p.ability.get_hp_max(true));
                                conn.p.getService().use_potion(1, conn.p.ability.get_mp_max(true));
                                //
                                Message m2 = new Message(-71);
                                m2.writer().writeByte(1);
                                m2.writer().writeShort(conn.p.index_map);
                                m2.writer().writeByte(0);
                                m2.writer().writeInt(60 * 30);
                                conn.p.map.send_msg_all_p(m2, conn.p, true);
                                m2.cleanup();
                                EffTemplate eff = conn.p.get_eff(7);
                                if (eff != null) {
                                    eff.time = System.currentTimeMillis() + 60_000L * 15;
                                } else {
                                    conn.p.add_new_eff(7, 1, 60_000L * 15);
                                }
                            }
                        }
                    }
                }
                break;
            }
            case -74: {
                if (conn.p != null) {
                    conn.p.getService().processTableTickOption(m);
                }
                break;
            }
            case 44: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng Chợ/Ký gửi!");
                        break;
                    }
                    conn.p.getService().processMarket(m);
                }
                break;
            }
            case 37: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    byte subType = m.reader().readByte();
                    if (subType == 1) {
                        byte index = m.reader().readByte();
                        if (conn.p.currentArchiveType == 1) {
                            achievement.ArchiPrivatePass.handleDirect(conn.p, subType, index);
                        } else {
                            achievement.ArchiDaily.handleDirect(conn.p, subType, index);
                        }
                    }
                }
                break;
            }
            case -23: {
                if (conn.p != null) {
                    conn.p.getService().processQuest(m);
                }
                break;
            }
            case -72: {
                if (conn.p != null && conn.p.map != null) {
                    if (conn.p.map.template.id == 64) {
                        Red_Line.process(conn.p, m);
                    } else if (conn.p.map.template.id == 109) {
                        Red_Line.process_sky(conn.p, m);
                    } else if (conn.p.map.map_ThuThachVeThan != null
                            && !conn.p.map.map_ThuThachVeThan.isFinish) {
                        Red_Line.process_TTVT(conn.p, m);
                    }
                }
                break;
            }
            case 66: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    conn.p.getService().processUpgradeSuperItem(m);
                }
                break;
            }
            case -30: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia Đấu trường!");
                        break;
                    }
                    conn.p.getService().processRanked(m);
                }
                break;
            }
            case -50: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    byte type = m.reader().readByte();
                    if (type == 0) {
                        conn.p.getService().processSplitItem(m);
                    } else if (type == 1) {
                        conn.p.getService().processJoinItem(m);
                    }
                }
                break;
            }
            case -62: {
                if (conn.p != null) {
                    ItemBoat.update_part_boat_when_shopping(conn.p);
                }
                break;
            }
            case -28: {
                if (conn.p != null) {
                    conn.p.getService().processLearnSkill(m);
                }
                break;
            }
            case 45: {
                if (conn.p != null) {
                    zabstracts.AbsUpgradeDevil upgradeDevil = new UpgradeDevil();
                    upgradeDevil.process(conn.p, m);
                }
                break;
            }
            case -25: {
                if (conn.p != null) {
                    conn.p.getService().processParty(m);
                }
                break;
            }
            case 20: {
                if (conn.p != null && conn.p.map != null && !conn.p.isCantSkill()) {
                    conn.p.getService().processBuff(m);
                }
                break;
            }
            case -32: {
                if (conn.p != null) {
                    conn.p.getService().Chest(m);
                }
                break;
            }
            case 54: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia Vòng quay!");
                        break;
                    }
                    byte act54 = m.reader().readByte();
                    AbsVongQuay vq = resolveVongQuay(conn.p);
                    if (vq != null) {
                        vq.process(conn.p, act54, m);
                    } else {
                        VongQuay.gI().process(conn.p, act54, m);
                    }
                }
                break;
            }
            case 18: {
                if (conn.p != null) {
                    conn.p.getService().processChat(m, 0);
                }
                break;
            }
            case -29: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Bạn bè!");
                        break;
                    }
                    conn.p.getService().processFriend(m);
                }
                break;
            }
            case -49: {
                // [FIX BUG-003-NPE] conn.p có thể null nếu chưa chọn nhân vật
                if (conn.p == null) break;
                if (conn.p.isDe || conn.p instanceof model.DeTu) {
                    conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện giao dịch!");
                    return;
                }
                if (conn.p.level < 1) {
                    conn.p.getService().send_box_ThongBao_OK("Chưa đủ level 30  không thể giao dịch");
                    return;
                }
                conn.p.getService().processTrade(m);
                break;
            }
            case -67: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    conn.p.getService().processRebuildItem(m);
                }
                break;
            }
            case -77: {
                if (conn.p != null) {
                    if (conn.p.isDe || conn.p instanceof model.DeTu) {
                        conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng này!");
                        break;
                    }
                    conn.p.getService().processChuyenHoa(m);
                }
                break;
            }
            case -16: {
                if (conn.p != null) {
                    conn.p.plus_point(m);
                }
                break;
            }
            case -13: { // use potion
                if (conn.p != null) {
                    short id = m.reader().readShort();
                    UseItem.use_item_potion(conn.p, id);
                }
                break;
            }
            case -11: {
                if (conn.p != null) {
                    conn.p.getService().processClientYesNo(m);
                }
                break;
            }
            case -58: {
                if (conn.p != null) {
                    conn.p.getService().processClientInput(m);
                }
                break;
            }
            case -31: {
                if (conn.p == null) break;
                try {
                    String text = null;
                    if (m.reader().available() > 0) {
                        m.reader().mark(1000);
                        try {
                            byte type = m.reader().readByte();
                            text = m.reader().readUTF();
                        } catch (Exception e) {
                            m.reader().reset();
                            text = m.reader().readUTF();
                        }
                    }
                    if (text != null && !text.trim().isEmpty()) {
                        activities.Chat.requestWorldChat(conn.p, text);
                    }
                } catch (Exception e) {
                }
                break;
            }
            case -46: {
                // [FIX BUG-003-NPE] Thỉ tự check null trước, sau đó mới check level
                if (conn.p == null) break;
                if (conn.p.level < 20) {
                    conn.p.getService().send_box_ThongBao_OK("Chưa đủ level 20 không thể chat KTG");
                    return;
                }
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    String text = m.reader().readUTF();
                    if (type == 0) {
                        activities.Chat.requestWorldChat(conn.p, text);
                    } else if (type == 1 && conn.p.clan != null) {
                        if (conn.p.isDe || conn.p instanceof model.DeTu) {
                            conn.p.getService().send_box_ThongBao_OK("Đệ tử không thể chat KTG Bang!");
                            return;
                        }
                        boolean check = false;
                        for (int i = 0; i < conn.p.clan.members.size(); i++) {
                            if (conn.p.clan.members.get(i).name.equals(conn.p.name)
                                    && (conn.p.clan.members.get(i).levelInclan == 1
                                            || conn.p.clan.members.get(i).levelInclan == 0)) {
                                check = true;
                                break;
                            }
                        }
                        if (check) {
                            if (conn.p.clan.get_ngoc() < 15) {
                                conn.p.getService().send_box_ThongBao_OK("Không đủ 15 ruby băng để chat KTG");
                                return;
                            }
                            conn.p.clan.update_ruby(-15);
                            for (int i = 0; i < conn.p.clan.members.size(); i++) {
                                Player p0 = Zone
                                        .get_player_by_name_allmap(conn.p.clan.members.get(i).name);
                                if (p0 != null) {
                                    Clan.send_money(p0, false);
                                }
                            }
                            Message m23 = new Message(-31);
                            m23.writer().writeByte(type);
                            m23.writer().writeUTF(conn.p.clan.name + ": " + text);
                            m23.writer().writeByte(0);
                            m23.writer().writeShort(conn.p.clan.icon);
                            for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
                                for (Zone map : mapall.zones) {
                                    for (int i = 0; i < map.players.size(); i++) {
                                        Player p0 = map.players.get(i);
                                        p0.addmsg(m23);
                                    }
                                }
                            }
                            m23.cleanup();
                        }
                    }
                }
                break;
            }
            case -48: {
                if (conn.p != null) {
                    conn.p.getService().processUpgradeItem(m);
                }
                break;
            }
            case -22: {
                if (conn.p != null) {
                    conn.p.getService().processUseItem(m);
                }
                break;
            }
            case -105: {
                if (conn.p != null) {
                    conn.p.getService().request_item4_info(m);
                }
                break;
            }
            case -21: {
                if (conn.p != null) {
                    if (!conn.p.checkPassRuong()) {
                        return;
                    }
                    conn.p.getService().sell_item(m);
                }
                break;
            }
            case 12: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.pick_item(conn.p, m);
                }
                break;
            }
            case -42: {
                if (conn.p != null) {
                    try {
                        String name = m.reader().readUTF();
                        if (name == null || name.trim().isEmpty()) break;
                        String cleanTarget = name.trim();

                        // 1. Kiểm tra nếu cleanTarget là tên Băng Hải Tặc (Clan)
                        // Khi người chơi ấn "Xem thông tin" trong BXH Top Clan hoặc danh sách Băng, client gửi tên Băng.
                        Clan targetClan = Clan.get_clan_by_name(cleanTarget);
                        if (targetClan != null) {
                            ClanMember leaderMem = targetClan.getLeader();
                            String leaderName = (leaderMem != null && leaderMem.name != null && !leaderMem.name.isEmpty())
                                    ? leaderMem.name : null;

                            Player leaderPlayer = null;
                            if (leaderName != null) {
                                leaderPlayer = findPlayerOrBot(leaderName, conn.p.map);
                            }

                            if (leaderPlayer != null) {
                                if (leaderPlayer.clan == null) {
                                    leaderPlayer.clan = targetClan;
                                }
                                conn.p.getService().send_view_other_player(leaderPlayer);
                                break;
                            }
                        }

                        // 2. Tìm kiếm nhân vật hoặc Bot thông thường
                        Player p0 = findPlayerOrBot(cleanTarget, conn.p.map);
                        if (p0 != null) {
                            if (p0.clan == null) {
                                Clan myClan = Clan.get_my_clan(p0.name);
                                if (myClan != null) p0.clan = myClan;
                            }
                            if (p0 instanceof model.DeTu && ((model.DeTu) p0).master == null) {
                                model.DeTu.promptCaptureWildDeTu(conn.p, (model.DeTu) p0);
                            } else {
                                conn.p.getService().send_view_other_player(p0);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
            case 14: {
                if (conn.p != null) {
                    byte type = m.reader().readByte();
                    byte act = m.reader().readByte();
                    if (act == 0) {
                        if (conn.p.map != null && conn.p.map.map_vp != null) {
                            conn.p.getService().send_box_ThongBao_OK("Không thể đổi cờ khi đang trong trận đấu!");
                            return;
                        }
                        if (map.zones.DauTruongTuDo.IsCantChangeFlag(conn.p)) {
                            conn.p.getService().send_box_ThongBao_OK("Không thể tự đổi cờ trong Đấu Trường Tự Do");
                            return;
                        }
                        if (WorldWar.runnning && conn.p.map != null && !WorldWar.mapTranChienLon(conn.p.map.template.id)) {
                            conn.p.getService().send_box_ThongBao_OK("Không thể thực hiện thao tác này khi đang diễn ra lễ hội");
                            return;
                        }
                        conn.p.map.change_flag(conn.p, type);
                    }
                }
                break;
            }
            case 6: {
                if (conn.p != null) {
                    conn.p.request_live_from_die(m);
                }
                break;
            }
            case -18: {
                if (conn.p != null) {
                    conn.p.getService().buy_item(m);
                }
                break;
            }
            case -5: {
                if (conn.p != null && conn.p.map != null) {
                    short id = m.reader().readShort();
                    conn.p.map.send_char_in4_inmap(conn.p, id);
                }
                break;
            }
            case 46: {
                if (conn.p != null) {
                    conn.p.getService().checkPlayInMap(m);
                }
                break;
            }
            case 0: {
                if (conn.p != null) {
                    try {
                        m.reader().readShort();
                        m.reader().readByte();
                    } catch (Exception ignored) {}
                    
                    try {
                        while (!conn.p.msgs.isEmpty()) {
                            Message m_send = conn.p.msgs.poll();
                            if (m_send != null) {
                                try {
                                    conn.addmsg(m_send);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        conn.p.getService().getThanhTich(conn.p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        conn.p.map.send_in4_obj_inmap(conn.p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    conn.p.wait_change_map = false;
                    conn.p.ischangemap = false;
                    conn.p.spawnX = conn.p.x;
                    conn.p.spawnY = conn.p.y;
                    conn.p.hasMovedFromSpawn = false;
                    conn.p.time_change_map = System.currentTimeMillis() + 1500L;
                    // List<Player> pBot = BotKhoBau.getBot(conn.p.map);
                    // if (!pBot.isEmpty()) {
                    //// pBot.forEach(l -> {
                    //// try {
                    // Player l = pBot.get(0);
                    // Message m_local = new Message(1);
                    // m_local.writer().writeByte(0);
                    // m_local.writer().writeShort(l.index_map);
                    // m_local.writer().writeShort(l.x);
                    // m_local.writer().writeShort(l.y);
                    //// conn.addmsg(m_local);
                    // m_local.cleanup();
                    //// } catch (Exception e) {
                    //// }
                    //// });
                    // }
                    //
                    if (conn.p.botTruyNa != null && !conn.p.botTruyNa.p.isdie) {
                        conn.p.botTruyNa.p.map = conn.p.map;
                        conn.p.botTruyNa.p.x = conn.p.x;
                        conn.p.botTruyNa.p.y = conn.p.y;
                        if (BotTruyNa.isMap(conn.p.botTruyNa.p.map.template.id)) {
                            Message m_local = new Message(1);
                            m_local.writer().writeByte(0);
                            m_local.writer().writeShort(conn.p.botTruyNa.p.index_map);
                            m_local.writer().writeShort(conn.p.botTruyNa.p.x);
                            m_local.writer().writeShort(conn.p.botTruyNa.p.y);
                            conn.addmsg(m_local);
                            m_local.cleanup();
                            conn.p.botTruyNa.p.map.enter_map(conn.p.botTruyNa.p);
                        }
                    }
                    // Chỉ cho đệ tử theo sư phụ khi sư phụ đang active (isDeOnl=true)
                    // KHÔNG cho sư phụ đi theo đệ tử (khi đang điều khiển đệ tử, người chơi tự điều khiển)
                    if ((conn.p.map.map_vp != null || MessageHandler.isMapDetu(conn.p.map.template.id))
                            && !(conn.p instanceof model.DeTu)) {
                        if (conn.p.isDeOnl && conn.p.detu != null && conn.p.detu.detuStatus != model.DeTu.STATUS_HOME && conn.p.detu.detuStatus != model.DeTu.STATUS_FUSION) {
                            Player follower = conn.p.detu;
                            follower.clan = conn.p.clan;
                            follower.typePirate = conn.p.typePirate;
                            follower.type_pk = conn.p.type_pk;
                            Message m_local = new Message(1);
                            m_local.writer().writeByte(0);
                            m_local.writer().writeShort(follower.index_map);
                            m_local.writer().writeShort(conn.p.x);
                            m_local.writer().writeShort(conn.p.y);
                            conn.addmsg(m_local);
                            m_local.cleanup();
                            follower.map = conn.p.map;
                            follower.x = conn.p.x;
                            follower.y = conn.p.y;
                            if (conn.p.map != null && !conn.p.map.players.contains(follower)) {
                                conn.p.map.enter_map(follower);
                            }
                            if (conn.p.map != null) {
                                for (int j = 0; j < conn.p.map.players.size(); j++) {
                                    Player p0 = conn.p.map.players.get(j);
                                    if (p0 != null && p0.getService() != null) {
                                        p0.getService().update_PK(follower, false);
                                    }
                                }
                            }
                        }
                    }
                    if (conn.p.map.map_DaoKhoBau != null && conn.p.map.map_DaoKhoBau.mobs != null && !conn.p.map.map_DaoKhoBau.mobs.isEmpty()) {
                        Message m_local = new Message(1);
                        m_local.writer().writeByte(1);
                        m_local.writer().writeShort(conn.p.map.map_DaoKhoBau.mobs.get(0).index);
                        m_local.writer().writeShort(conn.p.map.map_DaoKhoBau.mobs.get(0).x);
                        m_local.writer().writeShort(conn.p.map.map_DaoKhoBau.mobs.get(0).y);
                        conn.addmsg(m_local);
                        m_local.cleanup();
                    }
                    if (conn.p.map.template.id == 1000) {// map wait pvp
                        Pvp.show_table(conn.p);
                    }
                    if (conn.p.map.template.id == 119) { // map wait truy na
                        Wanted.show_table(conn.p);
                        conn.p.getService().Wanted(false);
                        Wanted_Chest.send_box(conn.p);
                    }
                    if (conn.p.map.map_vp != null) {// map pvp
                        map.zones.MapPvp pvp = conn.p.map.map_vp;
                        int leftScore = conn.p.equals(pvp.player1) ? pvp.num_win_p1 : pvp.num_win_p2;
                        int rightScore = conn.p.equals(pvp.player1) ? pvp.num_win_p2 : pvp.num_win_p1;
                        int timePvp = (pvp.status_pvp == 3) ? pvp.time_pvp : ((pvp.time_pvp > 0) ? pvp.time_pvp : 2);
                        Pvp.show_info(conn.p, timePvp, leftScore, rightScore, 3);
                        if (pvp.status_pvp == 0 || pvp.status_pvp == 90) {
                            Pvp.pvp_notice(conn.p, 0);
                        } else if (pvp.status_pvp == 1) {
                            Pvp.pvp_notice(conn.p, 1);
                        } else if (pvp.status_pvp == 2 || pvp.status_pvp == 3) {
                            Pvp.pvp_notice(conn.p, 2);
                        }
                    }
                    conn.p.map.change_flag(conn.p, conn.p.type_pk);
                    conn.p.getService().update_PK(conn.p, false);
                    // weather
                    conn.p.map.send_weather(conn.p);
                    // ship pet
                    if (conn.p.ship_pet != null && conn.p.ship_pet.map == null) {
                        conn.p.ship_pet.map = conn.p.map;
                        conn.p.ship_pet.id_map_save = conn.p.map.template.id;
                        conn.p.ship_pet.x = conn.p.x;
                        conn.p.ship_pet.y = conn.p.y;
                        Message m_local = new Message(1);
                        m_local.writer().writeByte(0);
                        m_local.writer().writeShort(conn.p.ship_pet.index_map);
                        m_local.writer().writeShort(conn.p.ship_pet.x);
                        m_local.writer().writeShort(conn.p.ship_pet.y);
                        for (int j = 0; j < conn.p.map.players.size(); j++) {
                            Player p0 = conn.p.map.players.get(j);
                            p0.addmsg(m_local);
                        }
                        m_local.cleanup();
                    }
                    zabstracts.AbsDungeon curDungeon = conn.p.getCurrentDungeon();
                    if (curDungeon == null && conn.p.map != null) {
                        curDungeon = conn.p.map.map_dungeon;
                    }
                    if (curDungeon != null && !(curDungeon instanceof map.zones.MapPvp) && !(curDungeon instanceof map.zones.WantedDungeon)
                            && conn.p.map.map_vp == null && conn.p.map.template.id != 119 && conn.p.map.template.id != 1000
                            && curDungeon.mobs != null && !curDungeon.mobs.isEmpty()) {
                        for (int i = 0; i < curDungeon.mobs.size(); i++) {
                            Mob mob = curDungeon.mobs.get(i);
                            if (mob != null && (mob.map == null || mob.map.equals(conn.p.map)) && !mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(curDungeon.time, "Thời gian", 2);
                    } else if (conn.p.map.mapSanTrum != null) {
                        conn.p.getService().send_time_cool_down(conn.p.map.mapSanTrum.time, "Thời gian", 2);
                    } else if (conn.p.map.map_Mr3 != null) { // mr3
                        for (int i = 0; i < conn.p.map.map_Mr3.mobs.size(); i++) {
                            Mob mob = conn.p.map.map_Mr3.mobs.get(i);
                            if (!mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.p.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_Mr3.time, "Mr3 ", 0);
                        if (conn.p.isdie) {
                            conn.p.getService().use_potion(0, 0);
                        }
                    } else if (conn.p.map.map_LienTang != null) { // lien tang
                        for (int i = 0; i < conn.p.map.map_LienTang.mobs.size(); i++) {
                            Mob mob = conn.p.map.map_LienTang.mobs.get(i);
                            if (!mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.p.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_LienTang.time, "Tầng " + (conn.p.map.template.id - 9989), 0);
                        if (conn.p.isdie) {
                            conn.p.getService().use_potion(0, 0);
                        }
                    } else if (conn.p.map.map_SieuLienTang != null) { // sieu lien tang
                        for (int i = 0; i < conn.p.map.map_SieuLienTang.mobs.size(); i++) {
                            Mob mob = conn.p.map.map_SieuLienTang.mobs.get(i);
                            if (!mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.p.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_SieuLienTang.time, "Tầng " + (conn.p.map.template.id - 198), 0);
                        if (conn.p.isdie) {
                            conn.p.getService().use_potion(0, 0);
                        }
                    } else if (conn.p.map.map_Hang != null) { // hang dong
                        for (int i = 0; i < conn.p.map.map_Hang.mobs.size(); i++) {
                            Mob mob = conn.p.map.map_Hang.mobs.get(i);
                            if (!mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.p.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_Hang.time, "Hang động tầng" + (conn.p.map.map_Hang.level), 0);
                        if (conn.p.isdie) {
                            conn.p.getService().use_potion(0, 0);
                        }
                    } else if (conn.p.map.map_DaoKhoBau != null) { // dao kho bau
                        if (conn.p.map.map_DaoKhoBau.mobs != null) {
                            for (int i = 0; i < conn.p.map.map_DaoKhoBau.mobs.size(); i++) {
                                Mob mob = conn.p.map.map_DaoKhoBau.mobs.get(i);
                                if (!mob.isdie) {
                                    Message m_local = new Message(1);
                                    m_local.writer().writeByte(1);
                                    m_local.writer().writeShort(mob.index);
                                    m_local.writer().writeShort(mob.x);
                                    m_local.writer().writeShort(mob.y);
                                    conn.p.addmsg(m_local);
                                    m_local.cleanup();
                                    conn.p.getService().send_mob_info(mob);
                                }
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_DaoKhoBau.time, "Thời gian", 0);
                    } else if (conn.p.map.vuonCam != null) { // vuoncam
                        conn.p.getService().send_hp_map(conn.p.map.vuonCam.hp, conn.p.map.vuonCam.hpMax);
                    } else if (conn.p.map.template.id == 9999 && conn.p.map.clan_resource != null) {
                        conn.p.getService().send_time_cool_down(conn.p.map.clan_resource.time, "Thời gian", 2);
                    } else if (conn.p.map.template.id == 81
                            && conn.p.map.map_little_garden != null) { // pho ban khong
                        // lo
                        TranChienKhongLo.send_info(conn.p);
                        conn.p.getService().send_time_cool_down(conn.p.map.map_little_garden.time, "Thời gian", 2);
                    } else if (conn.p.map.template.id == 199) {
                        conn.p.map.update_boat(conn.p, conn.p, false);
                        //
                        Red_Line.init_key_TTVT(conn.p);
                    } else if (conn.p.map.template.id == 109) {
                        conn.p.map.update_boat(conn.p, conn.p, false);
                        if (conn.p.party != null) {
                            if (conn.p.party.activeKeyIndex < conn.p.party.list.size()
                                    && conn.p.party.list.get(conn.p.party.activeKeyIndex).name.equals(conn.p.name)) {
                                if (conn.p.time_key_red_line == -1) {
                                    conn.p.time_key_red_line = 3;
                                    Red_Line.init_key_sky(conn.p);
                                }
                            }
                        } else {
                            if (conn.p.time_key_red_line == -1) {
                                conn.p.time_key_red_line = ZUtil.random(3, 5);
                                Red_Line.init_key_sky(conn.p);
                            }
                        }
                    } else if (conn.p.map.template.id == 984) { // pho ban thu thach ve than
                        conn.p.map.update_boat(conn.p, conn.p, false);
                        //
                        Red_Line.init_key_TTVT(conn.p);
                    } else if (conn.p.map.map_vp != null && conn.p.map.map_vp.type_map == 1) { // map
                        // sieu
                        // hang
                        conn.p.update_info_to_all();
                    } else if (conn.p.map.map_ThuThachVeThan != null
                            && conn.p.map.map_ThuThachVeThan.type == 2) { // ttvt
                        for (int i = 0; i < conn.p.map.map_ThuThachVeThan.mobs.size(); i++) {
                            Mob mob = conn.p.map.map_ThuThachVeThan.mobs.get(i);
                            if (!mob.isdie) {
                                Message m_local = new Message(1);
                                m_local.writer().writeByte(1);
                                m_local.writer().writeShort(mob.index);
                                m_local.writer().writeShort(mob.x);
                                m_local.writer().writeShort(mob.y);
                                conn.p.addmsg(m_local);
                                m_local.cleanup();
                                conn.p.getService().send_mob_info(mob);
                            }
                        }
                        conn.p.getService().send_time_cool_down(conn.p.map.map_ThuThachVeThan.time_state, "Tầng " + (conn.p.map.template.id - 912), 0);
                        if (conn.p.isdie) {
                            conn.p.getService().use_potion(0, 0);
                        }
                    }
                    if (ChiemDao.isMapHaveGate(conn.p.map.template.id)) {
                        if (ChiemDao.isOpen()) {
                            Message m223 = new Message(-7);
                            m223.writer().writeByte(27);
                            m223.writer().writeByte(1); // isopenDao
                            conn.addmsg(m223);
                            m223.cleanup();
                        } else {
                            Message m223 = new Message(-7);
                            m223.writer().writeByte(27);
                            m223.writer().writeByte(0); // isopenDao
                            conn.addmsg(m223);
                            m223.cleanup();
                        }
                    }
                    Clan occupyingClan = ChiemDao.getClanTop(conn.p.map.template.id);
                    if (occupyingClan != null) {
                        Message mm = new Message(63);
                        mm.writer().writeShort(occupyingClan.id);
                        mm.writer().writeShort(occupyingClan.icon >= 0 ? occupyingClan.icon : 1);
                        mm.writer().writeUTF(occupyingClan.name);
                        String capName = occupyingClan.getLeaderName();
                        if (capName == null || capName.isEmpty()) capName = occupyingClan.name;
                        mm.writer().writeUTF(capName); // Captain / Leader name
                        mm.writer().writeShort(occupyingClan.level);
                        int numMem = (occupyingClan.members != null) ? occupyingClan.members.size() : 1;
                        int maxMem = Clan.get_mem_max(occupyingClan.level, occupyingClan.trungsinh);
                        mm.writer().writeByte(numMem);
                        mm.writer().writeByte(maxMem);
                        mm.writer().writeInt(Clan.get_rank(occupyingClan));
                        conn.addmsg(mm);
                        mm.cleanup();
                    }
                    if ((conn.p.map.template.id >= 254 && conn.p.map.template.id <= 258)
                            || (conn.p.map.template.id >= 261 && conn.p.map.template.id <= 265)) {
                        conn.p.map.change_flag(conn.p, -1);
                    }
                    if (conn.p.lg != null && !conn.p.lg.isEmpty()) {
                        core.RewardService.sendGiftOrMail(conn.p, 1, "Thử Thách Vệ Thần",
                                "Hoàn thành", conn.p.lg, true);
                        conn.p.lg.clear();
                    }
                    /*
                     * if (Zone.is_map_luyentap(conn.p.map.template.id) && conn.p.time_luyen_tap >
                     * System.currentTimeMillis()) {
                     * // conn.p.time_luyen_tap = System.currentTimeMillis()+ 60_000*60*61;
                     * conn.p.getService().send_time_cool_down(conn.p.time_luyen_tap, "Thời gian", 0);
                     * }
                     */
                    for (int i12 = 0; i12 < conn.p.fashion.size(); i12++) {
                        if ((conn.p.fashion.get(i12).id == 55) && conn.p.fashion.get(i12).is_use) {
                            Message m3 = new Message(-47);
                            m3.writer().writeByte(8);
                            m3.writer().writeByte(4);
                            conn.addmsg(m3);
                            m3.cleanup();
                            break;
                        }
                    }
                    conn.p.idWeather = -1;
                    ThuLinhBienKhoi.sendInfo(conn.p);
                    if (conn.p.clan != null && conn.p.clan.baoVePhaoDai != null) {
                        conn.p.clan.baoVePhaoDai.SendInfoMap(conn.p);
                    }
                    map.zones.DauTruongTuDo.SendInfoGotoMap(conn.p);
                    model.Tree t = SuKienTrongCay.getByName(conn.p.name);
                    if (t != null && t.map.equals(conn.p.map)) {
                        Message m_local = new Message(1);
                        m_local.writer().writeByte(2);
                        m_local.writer().writeShort(t.index);
                        m_local.writer().writeShort(t.x);
                        m_local.writer().writeShort(t.y);
                        conn.addmsg(m_local);
                        m_local.cleanup();
                    }
                }
                break;
            }
            case 23: {
                if (conn.p != null && !conn.p.isdie) {
                    if (conn.p.ship_pet != null) {
                        conn.p.getService().send_box_ThongBao_OK("Không thể chuyến khu khi đang chuyển hàng");
                    } else {
                        conn.p.getService().area_select(m);
                    }
                }
                break;
            }
            case 17: {
                if (conn.p != null) {
                    conn.p.map.send_chat(conn.p, m);
                }
                break;
            }
            case -20: {
                if (conn.p != null) {
                    MenuController.process_menu(conn.p, m);
                }
                break;
            }
            case -19: {
                if (conn.p != null) {
                    MenuController.send_menu(conn.p, m);
                }
                break;
            }
            case 2: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.use_skill(conn.p, m);
                }
                break;
            }
            case -70: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.update_num_player_in_map(conn.p);
                }
                break;
            }
            case -45: {// update pk point
                if (conn.p != null) {
                    conn.p.update_point_pk(0);
                    conn.p.getService().CountDown_Ticket();
                    //
                    conn.p.getService().charWearing(conn.p, false);
                }
                break;
            }
            case -33: {
                if (conn.p != null) {
                    conn.p.getService().rms_process(m);
                }
                break;
            }
            case 1: {
                if (conn.p != null && conn.p.map != null) {
                    conn.p.map.send_move(conn.p, m);
                }
                break;
            }
            case 4: {
                if (conn.p != null) {
                    conn.p.getService().request_mob_in4(m);
                }
                break;
            }
            case 48: {
                if (conn.p != null) {
                    conn.p.getService().send_obj_template(m);
                }
                break;
            }

            case -9: {
                if (conn.p == null) {
                    login(m);
                }
                break;
            }
            case -8: {
                conn.create_char(m);
                break;
            }
            case -51: {
                Service.send_icon(m, conn);
                break;
            }
            case -92: {
                Service.send_icon_new(m, conn);
                break;
            }
            case -82: {
                conn.ReadPartNew(m);
                break;
            }
            case -38: {
                conn.sendDataClient(m);
                break;
            }
            case -2: {
                conn.login(m);
                conn.kh = conn.tongnap >= 20000;
                break;
            }
            case -6: {
                conn.Check_Data_Ver();
                break;
            }
            case -7: {
                conn.request_data_update(m);
                break;
            }
        }
    }

    public void login(Message m2) throws IOException {
        short id = m2.reader().readShort();
        m2.reader().readByte();
        m2.reader().readShort();
        login_into_char_select(id);
    }

    public void login_into_char_select(short id) throws IOException {
        try {
            if (conn.list_char != null && id >= 0 && id < conn.list_char.size()) {
                if (conn.p != null) {
                    try {
                        conn.p.flush(conn.p, false);
                    } catch (Exception ignore) {}
                    try {
                        if (conn.p.map != null) conn.p.map.leave_map(conn.p, 0);
                    } catch (Exception ignore) {}
                    SessionManager.PLAYERS_MAP.remove(conn.p.IDPlayer);
                    SessionManager.PLAYERS_BY_NAME.remove(conn.p.name);
                    SessionManager.PLAYERS_BY_INDEX.remove((int) conn.p.index_map);
                    conn.p.isClosed = true;
                    conn.p = null;
                }

                String targetName = conn.list_char.get(id);
                Player existingP = Zone.get_player_by_name_allmap(targetName);
                if (existingP != null) {
                    existingP.isClosed = true;
                    if (existingP.conn != null && existingP.conn != conn) {
                        try { existingP.conn.disconnect(); } catch (Exception ignore) {}
                    }
                    if (existingP.map != null) {
                        try { existingP.map.leave_map(existingP, 0); } catch (Exception ignore) {}
                    }
                    SessionManager.PLAYERS_MAP.remove(existingP.IDPlayer);
                    SessionManager.PLAYERS_BY_NAME.remove(existingP.name);
                    SessionManager.PLAYERS_BY_INDEX.remove((int) existingP.index_map);
                }

                Player p0 = new Player(conn, targetName);
                if (!p0.setup()) {
                    conn.disconnect();
                    return;
                }
                p0.setin4();

                conn.p = p0;
                SessionManager.PLAYERS_MAP.put(p0.IDPlayer, p0);
                SessionManager.PLAYERS_BY_NAME.put(p0.name, p0);
                Message m = new Message(-7); // update clock
                m.writer().writeByte(17);
                m.writer().writeLong(System.currentTimeMillis());
                conn.addmsg(m);
                m.cleanup();
                //
                conn.p.ensureRms0();
                conn.p.getService().UpdateInfoMaincharInfo();
                conn.p.getService().Main_char_Info();
                conn.p.send_skill();

                conn.p.getService().UpdatePvpPoint();
                conn.p.getService().update_PK(conn.p, false);
                conn.p.getService().getThanhTich(conn.p);
                conn.p.item.sendNumCellBag();
                // send data map
                if (conn.p.map == null) {
                    conn.p.map = Player.getSafeMap(conn.p.id_map_save > 0 ? conn.p.id_map_save : 1, 0);
                }
                if (conn.p.map != null) {
                    conn.p.map.goto_map(conn.p);
                }
                //
                conn.p.getService().update_PK(conn.p, true);
                conn.p.getService().pet(conn.p, true);
                conn.p.item.updateInventory(true);
                conn.p.item.updateMoney(true);
                conn.p.getService().ChestWanted(true);
                conn.p.item.sendNumCellBox();
                conn.p.item.update_assets_Box(true);
                conn.p.item.update_Inventory_box(-1, true);
                Quest.send_List_Quest(conn.p, true);
                Quest.update_map_have_side_quest(conn.p, true);
                conn.p.getService().Weapon_fashion(conn.p, true);
                UpgradeItem.send_heart_info(conn.p, true);
                conn.p.getService().charWearing(conn.p, true);
                if (conn.p.map != null) {
                    conn.p.map.send_boat(conn.p, true);
                    conn.p.map.update_boat(conn.p, conn.p, true);
                }
                conn.p.getService().login_ok(true);
            conn.p.getService().Wanted(true);
            Clan.send_info(conn.p, true);
            conn.p.getService().sendSudoInfo(true);
            LocalDate currentDate = LocalDate.now();
            if (conn.p.timeresetHangNgay == null || conn.p.timeresetHangNgay.toLocalDate().isBefore(currentDate)) {
                conn.p.resetDailyData(true);
            }
            // Reset nạp tuần (thứ 2 hàng tuần)
            LocalDate currentMonday = currentDate.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            if (conn.p.timeresetHangNgay != null && conn.p.timeresetHangNgay.toLocalDate().isBefore(currentMonday)) {
                conn.update_tongnap2_zero();
                conn.p.tongnap2 = 0;
            }
            if (conn.p.clan != null) {
                Message mcdGift = new Message(-52);
                mcdGift.writer().writeByte(13);
                if (Clan.HM_TIME_GIFT.containsKey(conn.p.name)) {
                    long time = Clan.HM_TIME_GIFT.get(conn.p.name);
                    time -= System.currentTimeMillis();
                    time /= 1_000;
                    mcdGift.writer().writeInt((int) time); // tinh = sec
                } else {
                    mcdGift.writer().writeInt(0); // tinh = sec
                }
                for (int i = 0; i < conn.p.clan.members.size(); i++) {
                    if (conn.p.clan.members.get(i).name.equals(conn.p.name)) {
                        mcdGift.writer().writeInt(conn.p.clan.members.get(i).donate);
                        conn.p.msgs.add(mcdGift);
                        mcdGift.cleanup();
                        break;
                    }
                }
            }
            core.MailService.sendAllMailsOnLogin(conn.p);

            Manager.gI().Notify(p0, 0, "Tìm gặp NPC Nami điểm danh và tham gia Nami pass nào!", 0);

            if (event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025)) {
                Manager.gI().Notify(p0, 1, "Đang diễn ra sự kiện Halloween, chúc các hải tặc một mùa sự kiện vui vẻ",
                        5);
            }
            if (event.EventManager.isActive(10)) {
                Manager.gI().Notify(p0, 1, "Đang diễn ra sự kiện 8 tháng 3, chúc các hải tặc một mùa sự kiện vui vẻ",
                        5);
            }
            if (event.EventManager.isActive(3)) {
                Manager.gI().Notify(p0, 1, "Đang diễn ra sự kiện Giỗ Tỗ 2025, chúc các hải tặc một mùa sự kiện vui vẻ",
                        5);
            }
            if (event.EventManager.isActive(1)) {
                Manager.gI().Notify(p0, 1, "Đang diễn ra sự kiện Hè 2025, chúc các hải tặc một mùa sự kiện vui vẻ", 5);
            }
            //
            Message m4 = new Message(-7);
            m4.writer().writeByte(28);
            m4.writer().writeShort(ItemTemplate4.ENTRYS.size());
            for (int i = 0; i < ItemTemplate4.ENTRYS.size(); i++) {
                ItemTemplate4 temp = ItemTemplate4.ENTRYS.get(i);
                m4.writer().writeShort(temp.id);
                m4.writer().writeShort(temp.icon);
                m4.writer().writeUTF(temp.name != null ? temp.name : "");
                m4.writer().writeShort(temp.indexInfoPotion);
                m4.writer().writeInt(temp.beri);
                m4.writer().writeShort(temp.ruby);
                m4.writer().writeByte(temp.istrade);
                m4.writer().writeByte(temp.type);
                m4.writer().writeShort(temp.timedelay);
                m4.writer().writeShort(temp.value);
                m4.writer().writeShort(temp.timeactive);
                m4.writer().writeUTF(temp.nameuse != null ? temp.nameuse : "");
            }
            m4.writer().writeShort(DataTemplate.VerdataPotion);
            conn.p.msgs.add(m4);

            // Gửi dữ liệu tên vật phẩm nhiệm vụ (DataTemplate.NamePotionquest)
            Message mq = new Message(-7);
            mq.writer().writeByte(7);
            mq.writer().writeShort(DataTemplate.NamePotionquest.length);
            for (int i = 0; i < DataTemplate.NamePotionquest.length; i++) {
                mq.writer().writeUTF(DataTemplate.NamePotionquest[i] != null ? DataTemplate.NamePotionquest[i] : "");
            }
            mq.writer().writeShort(DataTemplate.VerdataNamePotionquest);
            conn.p.msgs.add(mq);

            if (WorldWar.runnning && conn.p.map != null && conn.p.map.map_little_garden == null) {
                WorldWar.setType(conn.p);
            }
            // TopGift.getGift(conn.p);
            //
            if (conn.p.clan != null) {
                conn.p.clan.sendGift(conn.p);
            }

            conn.p.botTruyNa = BotTruyNa.getBot(conn.p);
            //

            if (!conn.p.mbv.isBlank() && conn.p.timeHuyMbv != -1) {
                long t = conn.p.timeHuyMbv - System.currentTimeMillis();
                t /= 1000;
                if (t > 0) {
                    Message m5 = new Message(18);
                    m5.writer().writeUTF("Mã Khóa");
                    m5.writer().writeUTF("Hiện tại đã đăng ký hủy mã bảo vệ. Thời gian còn lại: " + t + "s");
                    conn.p.msgs.add(m5);

                }
            }
            if (conn.p.detu != null) {
                if (conn.p.detu.fashion.size() != conn.p.fashion.size()) {
                    conn.p.detu.fashion.clear();
                    for (int i = 0; i < conn.p.fashion.size(); i++) {
                        ItemFashionP2 get = conn.p.fashion.get(i);
                        ItemFashionP2 add = new ItemFashionP2();
                        add.id = get.id;
                        add.is_use = false;
                        add.level = 1;
                        conn.p.detu.fashion.add(add);
                    }
                }

                if (conn.p.detu.itfashionP.size() != conn.p.itfashionP.size()) {
                    conn.p.detu.itfashionP.clear();
                    for (int i = 0; i < conn.p.itfashionP.size(); i++) {
                        ItemFashionP get = conn.p.itfashionP.get(i);

                        ItemFashionP add = new ItemFashionP(get.id, get.icon, get.category, false);

                        conn.p.detu.itfashionP.add(add);
                    }
                }

            }
        }
    } catch (Throwable t) {
        t.printStackTrace();
        try {
            conn.disconnect();
        } catch (Exception ignore) {}
    }
}

    public static boolean isMapDetu(int id) {
        for (int i = 0; i < idmap.size(); i++) {
            if (idmap.get(i) == id) {
                return true;
            }
        }
        if ((id >= 9990 && id <= 9996) || (id >= 199 && id <= 211)
                || (id >= 254 && id <= 258) || (id >= 261 && id <= 265) || (id >= 178 && id <= 184) || id == 266
                || (id >= 267 && id <= 275) || (id >= 62 && id <= 65) || id == 68 || (id >= 108 && id <= 112)
                || (id >= 146 && id <= 150) || (id >= 220 && id <= 229) || id >= 900
                || id == 81 || id == 120 || id == 260 || id == 58 || id == 119 || id == 122 || id == 123 || id == 1000
                || Zone.is_map_boss(id) || Zone.is_map_dungeon(id) || Zone.is_map_luyentap(id)) {
            return true;
        }
        return false;
    }

    private static zabstracts.AbsVongQuay resolveVongQuay(model.Player p) {
        if (p.currentVongQuay != null && !(p.currentVongQuay instanceof model.VongQuayOcSen)) {
            return p.currentVongQuay;
        }
        if (event.EventManager.isActive(event.SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025) && (p.typeVongQuay == 1 || p.typeVongQuay == 2)) {
            return model.VongQuayHLW.gI();
        }
        if (event.EventManager.isActive(1) && p.typeVongQuay == 5) {
            return model.VongQuayHLW.gI();
        }
        if (event.EventManager.isActive(2) && (p.typeVongQuay == 3 || p.typeVongQuay == 4)) {
            return model.VongQuayHLW.gI();
        }
        return model.VongQuay.gI();
    }

    /**
     * Tìm kiếm toàn diện Người chơi / Bot / Đệ tử từ mọi Map, Pool, Dynamic Zone và Cache/DB.
     */
    public static Player findPlayerOrBot(String targetName, Zone currentMap) {
        if (targetName == null || targetName.trim().isEmpty()) return null;
        String clean = targetName.trim();

        // 1. Quét map hiện tại của người chơi (Player, Bot, Đệ tử)
        if (currentMap != null && currentMap.players != null) {
            for (int i = 0; i < currentMap.players.size(); i++) {
                Player pl = currentMap.players.get(i);
                if (pl != null) {
                    if (pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                        return pl;
                    }
                    if (pl.detu != null && pl.detu.name != null && (pl.detu.name.equalsIgnoreCase(clean) || pl.detu.name.trim().equalsIgnoreCase(clean))) {
                        return pl.detu;
                    }
                }
            }
        }

        // 2. Tìm trong SessionManager.PLAYERS_BY_NAME
        Player p0 = network.SessionManager.PLAYERS_BY_NAME.get(clean);
        if (p0 != null) return p0;
        for (Player pl : network.SessionManager.PLAYERS_BY_NAME.values()) {
            if (pl != null && pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                return pl;
            }
        }

        // 3. Quét tất cả các Bot Pools hoạt động
        // 3.1 BotPlayerReal
        for (bot.botplayer.BotPlayerReal b : bot.botplayer.BotPlayerManager.getActiveBots()) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.2 Bot Chiếm Đảo
        for (bot.BotChiemDao b : bot.BotChiemDao.POOL) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.3 Bot Thủ Lĩnh Biển Khơi
        for (bot.BotThuLinhBienKhoi b : bot.BotThuLinhBienKhoi.POOL) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.4 Bot Trận Chiến Khổng Lồ (Little Garden)
        for (bot.BotTranChienKhongLo b : bot.BotTranChienKhongLo.POOL) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.5 Bot Khổng Lồ Map Ngoài
        for (bot.BotKhongLoMapNgoai b : bot.BotKhongLoMapNgoai.POOL) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.6 Bot Đấu Trường Tự Do
        for (bot.BotDauTruongTuDo b : bot.BotDauTruongTuDo.POOL) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }
        // 3.7 Bot Lính Đánh Thuê
        Player mercBot = bot.mercenary.MercenaryManager.gI().findBotByName(clean);
        if (mercBot != null) return mercBot;

        // 3.8 Bot Đấu Trường Arena
        if (event.eboss.DauTruongBotArena.gI() != null) {
            for (bot.BotPVP b : event.eboss.DauTruongBotArena.gI().team1Bots) {
                if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                    return b;
                }
            }
            for (bot.BotPVP b : event.eboss.DauTruongBotArena.gI().team2Bots) {
                if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                    return b;
                }
            }
        }
        // 3.9 Bot Lobby
        for (bot.BotLobbyManager.BotLobbyBot b : bot.BotLobbyManager.LOBBY_BOTS) {
            if (b != null && b.name != null && (b.name.equalsIgnoreCase(clean) || b.name.trim().equalsIgnoreCase(clean))) {
                return b;
            }
        }

        // 4. Quét toàn bộ các Dynamic Maps & Dungeons (Zone.get_map_plus())
        try {
            List<Zone> mapPlus = Zone.get_map_plus();
            if (mapPlus != null) {
                for (int i = 0; i < mapPlus.size(); i++) {
                    Zone z = mapPlus.get(i);
                    if (z != null && z.players != null) {
                        for (int j = 0; j < z.players.size(); j++) {
                            Player pl = z.players.get(j);
                            if (pl != null) {
                                if (pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                                    return pl;
                                }
                                if (pl.detu != null && pl.detu.name != null && (pl.detu.name.equalsIgnoreCase(clean) || pl.detu.name.trim().equalsIgnoreCase(clean))) {
                                    return pl.detu;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // 5. Quét toàn bộ Active Zones từ MapManager (Bao gồm tất cả map phó bản, pvp, thế giới, làng...)
        try {
            java.util.Set<Zone> activeZones = map.MapManager.getInstance().getActiveZones();
            if (activeZones != null) {
                for (Zone z : activeZones) {
                    if (z != null && z.players != null) {
                        for (int j = 0; j < z.players.size(); j++) {
                            Player pl = z.players.get(j);
                            if (pl != null) {
                                if (pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                                    return pl;
                                }
                                if (pl.detu != null && pl.detu.name != null && (pl.detu.name.equalsIgnoreCase(clean) || pl.detu.name.trim().equalsIgnoreCase(clean))) {
                                    return pl.detu;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // 6. Quét SessionManager PLAYERS_MAP và PLAYERS_BY_INDEX
        for (Player pl : network.SessionManager.PLAYERS_MAP.values()) {
            if (pl != null && pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                return pl;
            }
        }
        for (Player pl : network.SessionManager.PLAYERS_BY_INDEX.values()) {
            if (pl != null && pl.name != null && (pl.name.equalsIgnoreCase(clean) || pl.name.trim().equalsIgnoreCase(clean))) {
                return pl;
            }
        }

        // 7. Fallback Offline: Thử load từ DB player thật (players)
        try {
            Player offlineP = new Player();
            offlineP.name = clean;
            if (offlineP.setup()) {
                offlineP.setin4();
                offlineP.updateParts();
                return offlineP;
            }
        } catch (Exception ignored) {}

        // 8. Fallback Offline: Thử load từ DB bot (players_bot)
        try {
            int[] botIds = bot.botplayer.BotDb.getIds(clean);
            if (botIds != null) {
                bot.botplayer.BotPlayerReal offlineBot = bot.botplayer.BotPlayerReal.loadBot(clean, "bot123456");
                if (offlineBot != null) {
                    return offlineBot;
                }
            }
            bot.Bot offlineGenericBot = new bot.Bot(0, clean);
            if (offlineGenericBot.setup("players_bot")) {
                offlineGenericBot.setin4();
                offlineGenericBot.init();
                return offlineGenericBot;
            }
        } catch (Exception ignored) {}

        // 9. Fallback Cached Profile trong BotPvpCacheManager
        try {
            bot.botplayer.ai.BotPvpCacheManager.BotProfile profile = bot.botplayer.ai.BotPvpCacheManager.findCachedProfileByName(clean);
            if (profile != null) {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                bot.BotPVP pvpBot = new bot.BotPVP(botId, profile.name);
                pvpBot.isBot = true;
                if (pvpBot.setup(profile.tableName)) {
                    pvpBot.setin4();
                    pvpBot.init();
                    return pvpBot;
                }
            }
        } catch (Exception ignored) {}

        // 10. Dynamic Fallback: Tạo bot mẫu hoàn chỉnh với trang bị đầy đủ nếu tên hợp lệ
        try {
            Clan memberClan = Clan.get_my_clan(clean);
            clan.ClanMember memberInfo = (memberClan != null) ? memberClan.getMember(clean) : null;

            int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
            bot.Bot dynamicBot = new bot.Bot(botId, clean);
            dynamicBot.isBot = true;
            byte bClazz = (memberInfo != null && memberInfo.clazz > 0) ? memberInfo.clazz : (byte) core.ZUtil.random(1, 5);
            dynamicBot.clazz = bClazz;
            short refLv = (memberInfo != null && memberInfo.level > 0) ? memberInfo.level
                    : ((currentMap != null && !currentMap.players.isEmpty() && currentMap.players.get(0) != null) ? currentMap.players.get(0).level : 50);
            dynamicBot.level = refLv;
            dynamicBot.clan = memberClan;
            int botTier = dynamicBot.level >= 90 ? 4 : (dynamicBot.level >= 75 ? 3 : (dynamicBot.level >= 50 ? 2 : 1));
            dynamicBot.autoAllocatePotentialPoints(dynamicBot.clazz);
            dynamicBot.setupBotEquip(dynamicBot.clazz, dynamicBot.level, botTier, dynamicBot.level >= 75, true, dynamicBot.level >= 60);
            dynamicBot.setupBotSkills(dynamicBot.clazz, (short) Math.max(15, dynamicBot.level / 3), botTier, 0);
            if (memberInfo != null && memberInfo.head > 0) {
                dynamicBot.head = memberInfo.head;
                dynamicBot.hair = memberInfo.hair;
            } else {
                dynamicBot.setupBotAppearance(dynamicBot.clazz, botTier);
            }
            dynamicBot.setin4();
            dynamicBot.init();
            dynamicBot.ability = new ability.Ability(dynamicBot);
            dynamicBot.updateParts();
            return dynamicBot;
        } catch (Exception ignored) {}

        return null;
    }
}
