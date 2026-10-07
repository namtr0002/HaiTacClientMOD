package model;

import event.EventData;
import skill.Skill_info;
import skill.Skill_Template;
import event.EventManager;

import map.MapBossInfo;
import itemz.Rebuild_Item;
import clan.ClanMember;
import clan.ClanChat;
import clan.Clan;
import map.zones.TranChienKhongLo;
import map.zones.*;
import itemz.UpgradeItem;
import bot.BotTruyNa;
import map.zones.ThuLinhBienKhoi;
import map.zones.MapAiDon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import event.SuKienNoel;
import event.SuKienGioTo;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import activities.*;
import historys.zLog;
import core.Manager;

import network.Service;
import core.ZUtil;
import database.IDManager;
import database.DbManager;
import event.SuKienTrongCay;
import network.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import boss.SuperBossManager;
import map.Map;
import map.Zone;
import map.MapCanGoTo;
import mob.Mob;
import map.Npc;
import map.Vgo;
import template.*;
import model.VongQuay;
import model.VongQuayOcSen;

public class ClientYesNo {

    public static void process(Player p, Message m2) throws IOException {
        short id = m2.reader().readShort();
        byte value = m2.reader().readByte();
        // System.out.println("id " + id);
        // System.out.println("value " + value);
        if (p.yesNoDialog != null) {
            if (p.yesNoDialog.hasHandler()) {
                model.YesNoDialog dialog = p.yesNoDialog;
                if (!dialog.isHold) {
                    p.yesNoDialog = null;
                }
                dialog.handle(value);
                return;
            } else {
                p.yesNoDialog = null;
            }
        }
        zinterfaces.iNpc npcHandler = zinterfaces.iNpc.get(id);
        if (npcHandler != null) {
            npcHandler.handleMenu(p, id, value);
            return;
        }
        if (id == 87200) {
            activities.TimedDungeonManager.gI().handleAdminSystemMenu(p, value);
            return;
        } else if (id == 87201) {
            activities.TimedDungeonManager.gI().handleTimedDungeonManagementMenu(p, value);
            return;
        } else if (id == 87202) {
            activities.TimedDungeonManager.gI().handleTimedDungeonDetailAction(p, value);
            return;
        } else if (id == 87210) {
            activities.TimedDungeonManager.gI().handleBotManagementMenu(p, value);
            return;
        } else if (id == 87211) {
            activities.TimedDungeonManager.gI().handleBotSelectDetail(p, value);
            return;
        } else if (id == 87220) {
            activities.TimedDungeonManager.gI().handleSwitchClazz(p, value);
            return;
        } else if (id == 87230) {
            activities.TimedDungeonManager.gI().handleDiscipleManagementMenu(p, value);
            return;
        } else if (id == 87240) {
            activities.TimedDungeonManager.gI().handleTeleportMenu(p, value);
            return;
        } else if (id == 87250) {
            activities.TimedDungeonManager.gI().handleBossEventMenu(p, value);
            return;
        } else if (id == 87260) {
            activities.TimedDungeonManager.gI().handlePlayerManagementMenu(p, value);
            return;
        } else if (id == 87270) {
            activities.TimedDungeonManager.gI().handleAddItemCategoryMenu(p, value);
            return;
        } else if (id == 88100) {
            // Menu nhận quà Top Phó Bản (DungeonGiftMenu)
            activities.DungeonGiftMenu.handleMenu(p, value);
            return;
        } else if (id == 88200) {
            // Menu Chiếm Đảo Bang Hội
            activities.ChiemDaoMenu.handleMenu(p, value);
            return;
        }
        if (id == 871 && p.detu != null) {
            switch (value) {
                case 0: { // Thông tin đệ tử
                    p.detu.sendInfoToMaster();
                    break;
                }
                case 1: { // Kỹ năng đệ tử
                    p.detu.sendSkillInfoToMaster();
                    break;
                }
                case 2: { // Đi theo
                    p.detu.setStatus(DeTu.STATUS_FOLLOW);
                    break;
                }
                case 3: { // Bảo vệ (bán kính 150px)
                    p.detu.setStatus(DeTu.STATUS_PROTECT);
                    break;
                }
                case 4: { // Tấn công (ưu tiên kẻ đánh sư phụ hoặc đệ)
                    p.detu.setStatus(DeTu.STATUS_ATTACK);
                    break;
                }
                case 5: { // Hợp thể (gộp 70% chỉ số)
                    p.detu.setStatus(DeTu.STATUS_FUSION);
                    break;
                }
                case 6: { // Về nhà (cất đệ off, triệu hồi tức thời không cần load map)
                    p.detu.setStatus(DeTu.STATUS_HOME);
                    break;
                }
                case 7: { // Xóa đệ tử
                    DeTu.confirmDeleteDeTu(p);
                    break;
                }
            }
            p.data_yesno = null;
            return;
        }
        if (id == 39 && value < 4) {
            if (p.trade_target != null) {
                p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                p.data_yesno = null;
                p.map_tele = null;
                return;
            }
            int vang_total = 0;
            synchronized (p.item) {
                for (int i = 0; i < p.item.bag3.length; i++) {
                    Item_wear it = p.item.bag3[i];
                    if (it != null && it.template != null && it.template.typeEquip < 6
                            && it.levelUp == 0
                            && !it.isThanTrang()
                            && (it.mdakham == null || it.mdakham.length == 0)
                            && !Service.isEquippedOnBody(p, it)
                            && it != p.item.it_heart
                            && it.getColor() <= value) {
                        int vang_recive = 30 + (2 * it.getColor()
                                + (it.template.level / 10) + 1)
                                * DataTemplate.TabInventory_ItemSell[0];
                        if (vang_recive > DataTemplate.TabInventory_ItemSell[1]) {
                            vang_recive = DataTemplate.TabInventory_ItemSell[1];
                        }
                        vang_total += vang_recive;
                        //
                        p.item.add_item_save(it);
                        p.item.bag3[i] = null;
                    }
                }
                if (vang_total > 0) {
                    p.update_vang(vang_total);
                    p.updateMoney();
                }
                p.item.updateInventory(false);
            }
            p.setAbility();
            p.update_info_to_all();
            p.data_yesno = null;
            p.map_tele = null;
            return;
        } else if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 96 && id == 96 && value < 6) {
            short[] id_op = new short[]{27, 16, 26, 4, 15, 1};
            if (p.detu.pointAttributeThongThao < 1) {
                p.getService().send_box_ThongBao_OK("Không đủ 1 điểm thông thạo");
                p.data_yesno = null;
                p.map_tele = null;
                return;
            }
            Option op_add = null;
            for (int i = 0; i < p.detu.list_op_thongthao.size(); i++) {
                if (p.detu.list_op_thongthao.get(i).id == id_op[value]) {
                    op_add = p.detu.list_op_thongthao.get(i);
                    break;
                }
            }
            if (op_add != null) {
                int old_value = op_add.getParam();
                if (old_value >= 40) {
                    p.getService().send_box_ThongBao_OK("Không thể cộng quá 40 điểm");
                    p.data_yesno = null;
                    p.map_tele = null;
                    return;
                }
                op_add.setParam(old_value + 1);

            } else {
                p.detu.list_op_thongthao.add(new Option(id_op[value], 1));
            }
            p.detu.pointAttributeThongThao--;
            p.detu.update_info_to_all();

            String[] name = new String[]{"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ", "HP+",
                "Tăng tấn công"};
            String notice = "Điểm tiềm năng: " + p.detu.pointAttributeThongThao;

            for (int i = 0; i < name.length; i++) {

                int value2 = 0;
                for (int j = 0; j < p.detu.list_op_thongthao.size(); j++) {
                    if (p.detu.list_op_thongthao.get(j).id == id_op[i]) {
                        value2 += p.detu.list_op_thongthao.get(j).getParam();
                    }
                }
                notice += ("\n" + name[i] + ": " + value2 + "điểm");

            }
            p.data_yesno = new int[]{96};
            p.setyesNoDialog(new model.YesNoDialog(p, 96, "Thông báo", notice, new String[]{
                "+Kháng phép", "+MP", "+Kháng vật lý", "+Tăng phòng thủ", "+HP",
                "+Tăng tấn công", "Hủy"}, new byte[]{-1, -1, -1, -1, -1, -1, -1}));
            p.getService().startYesNo();

            return;
        }
        if (value == 0) { // ok
            switch (id) {
                case -9966: {// vuong event noel
                    if(!SuKienNoel.gI().isNoel()) {
                        return;
                    }
                    SantaNoel santa = SuKienNoel.gI().getSantaInMap(p.map);
                    if(santa == null || !santa.saveID.contains(p.IDPlayer)) {
                        return;
                    }
                    EventData eventData = p.getDataEvent(SuKienNoel.ID_EVENT);
                    if(eventData == null) {
                        return;
                    }
                    if(eventData.data[SuKienNoel.INDEX_GIFT_SANTA] >= 10) {
                        p.getService().send_box_ThongBao_OK("Mỗi ngày có thể nhận quà tối đa 10 lần");
                        return;
                    }
                    if(p.item.total_item_bag_by_id(4, SuKienNoel.ITEM_KEO_GIANG_SINH) < 1) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ Kẹo giáng sinh");
                        return;
                    }
                    SuKienNoel.gI().accectGiftSanta(p, eventData);
                    break;
                }
                case 95: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 95) {
                        if (p.detu.item.it_heart != null && p.detu.item.it_heart.levelUp < 99) {

                            long vang_req = 1_200_000 + p.detu.item.it_heart.levelUp * 200_000L;
                            if (p.get_vang() < vang_req) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ZUtil.number_format(vang_req) + " beri");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            int ruby_req = 0;
                            if ((p.detu.item.it_heart.levelUp % 5) == 4) {
                                ruby_req = ((p.detu.item.it_heart.levelUp / 5) + 1) * 10;
                            }
                            if (p.get_ngoc() < ruby_req) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ruby_req + " ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_vang(-vang_req);
                            p.update_ngoc(-ruby_req);
                            p.updateMoney();
                            boolean suc = (530 - (p.detu.item.it_heart.levelUp * 5)) > ZUtil.random(1200);
                            if (suc || p.detu.item.it_heart.valueChetac == 100) {
                                suc = true;
                                p.detu.item.it_heart.levelUp++;
                                p.detu.item.it_heart.valueChetac = 0;
                            } else {
                                p.detu.item.it_heart.valueChetac += 10;
                            }
                            UpgradeItem.send_heart_info(p.detu, false);
                            p.detu.update_info_to_all();
                            //
                            ruby_req = 0;
                            if ((p.detu.item.it_heart.levelUp % 5) == 4) {
                                ruby_req = ((p.detu.item.it_heart.levelUp / 5) + 1) * 10;
                            }
                            p.data_yesno = new int[]{95};
                            p.setyesNoDialog(new model.YesNoDialog(p, 95, "Thông báo",
                                    ("Nâng cấp " + (suc ? "thành công" : "thất bại")
                                    + ". Cấp hiện tại: " + p.detu.item.it_heart.levelUp
                                    + ", chế tác: " + p.detu.item.it_heart.valueChetac
                                    + ".\nĐể tiến hành phẫu thuật tim cho đệ tử, bạn cần có "
                                    + ZUtil.number_format(
                                            1_200_000L + (p.detu.item.it_heart.levelUp * 200_000L))
                                    + " beri" + ((ruby_req > 0) ? (" và " + ruby_req + " ruby ") : "")
                                    + ". " + "Bạn thật sự muốn phẫu thuật?"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                            p.getService().startYesNo();
                            return;
                        }
                    }
                }
                break;
                case 94: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 94) {
                        if (p.detu.item.it_heart == null) {
                            if (p.item.total_item_bag_by_id(4, 221) < ((p.detu.level < 40) ? 120 : 100)) {
                                p.getService().send_box_ThongBao_OK("Không đủ "
                                        + ((p.detu.level < 40) ? 120 : 100) + " Đá Hải Thạch cấp 1");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            short[] id_check = new short[]{4, 9};
                            for (int i = 0; i < id_check.length; i++) {
                                if (p.item.total_item_bag_by_id(7,
                                        id_check[i]) < ((p.detu.level < 40) ? 120 : 100)) {
                                    p.getService().send_box_ThongBao_OK("Không đủ " + ((p.detu.level < 40) ? 120 : 100) + " "
                                            + ItemTemplate7.get_item_name(id_check[i]));
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                            }
                            if (p.get_vang() < ((p.detu.level < 40) ? 12_000_000 : 10_000_000)) {
                                p.getService().send_box_ThongBao_OK("Không đủ 10.000.000 beri");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.item.remove_item47(4, 221, ((p.detu.level < 40) ? 120 : 100));
                            for (int i = 0; i < id_check.length; i++) {
                                p.item.remove_item47(7, id_check[i], ((p.detu.level < 40) ? 120 : 100));
                            }
                            p.update_vang(-((p.detu.level < 40) ? 12_000_000 : 10_000_000));
                            p.updateMoney();
                            p.item.updateInventory(false);
                            //
                            p.detu.item.it_heart = new Item_wear();
                            p.detu.item.it_heart.setup_template_by_id(11_000);
                            p.detu.item.it_heart.valueChetac = 0;
                            p.detu.item.it_heart.typelock = 1;
                            boolean has56 = false;
                            for (Option op : p.detu.item.it_heart.option_item) {
                                if (op != null && op.id == 56) {
                                    has56 = true;
                                    break;
                                }
                            }
                            if (!has56) {
                                p.detu.item.it_heart.option_item.add(new Option(56, 100));
                            }
                            //
                            p.detu.item.it_heart.index = 6;
                            p.detu.item.it_heart.typelock = 1;
                            p.detu.item.it_body[6] = p.detu.item.it_heart;
                            p.detu.update_info_to_all();
                            UpgradeItem.send_heart_info(p.detu, false);
                            p.getService().send_box_ThongBao_OK("Thành công");
                        }

                    }
                }
                break;
                case 93: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 93
                            && p.data_yesno[1] >= 0 && p.data_yesno[1] < p.detu.skill_point.size()) {
                        Skill_info sk = p.detu.skill_point.get(p.data_yesno[1]);
                        if (sk.temp.typeSkill == 1 && sk.temp.Lv_RQ > 0) {
                            if (sk.lvdevil > 4) {
                                p.getService().send_box_ThongBao_OK(sk.temp.name + " đã được cường hóa tối đa!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(7, 9) < 10) {
                                p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            int percent = (sk.lvdevil == 0) ? 10 //
                                    : ((sk.lvdevil == 1) ? 8 //
                                            : ((sk.lvdevil == 2) ? 6 //
                                                    : ((sk.lvdevil == 3) ? 5 : 4)));
                            if (p.get_vang() < 100_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ 100.000 beri");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_vang(-100_000);
                            p.updateMoney();
                            p.item.remove_item47(7, 9, 10);
                            p.item.updateInventory(false);
                            //
                            boolean suc = 50 > ZUtil.random(120);
                            if (suc) {
                                sk.devilpercent += percent;
                                if (sk.devilpercent >= 100) {
                                    sk.devilpercent = 0;
                                    sk.lvdevil++;
                                }
                                p.send_skill();
                                p.update_info_to_all();
                            }
                            percent = (sk.lvdevil == 0) ? 10 //
                                    : ((sk.lvdevil == 1) ? 8 //
                                            : ((sk.lvdevil == 2) ? 6 //
                                                    : ((sk.lvdevil == 3) ? 5 : 4)));
                            String notice = ("Nâng cấp " + (suc ? "thành công" : "thất bại")
                                    + ".\nXác nhận nâng cấp ác quỷ kỹ năng " + sk.temp.name + "? Hiện tại cấp "
                                    + sk.lvdevil + " - " + sk.devilpercent + "%. Thành công sẽ tăng thêm " + percent
                                    + "% vào cấp ác quỷ. Nguyên liệu 10 đá ác quỷ");
                            p.data_yesno = new int[]{93, p.detu.skill_point.indexOf(sk)};
                            p.setyesNoDialog(new model.YesNoDialog(p, 93, "Thông báo", notice, new String[]{"100.000", "100", "Đóng"}, new byte[]{6, 7, 1}));
                            p.getService().startYesNo();
                            return;
                        }

                    }

                }
                break;
                case 92: {

                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 3 && p.data_yesno[0] == 92) {
                        ItemFashionP temp = p.check_itfashionP(p.data_yesno[1], p.data_yesno[2]);
                        if (temp != null) {

                            p.update_itfashionP(temp, p.data_yesno[2]);
                            for (int i = 0; i < p.map.players.size(); i++) {
                                Player p0 = p.map.players.get(i);
                                p.getService().charWearing(p0, false);
                            }
                            ItemFashionP.show_table(p, p.data_yesno[2]);
                            p.getService().send_box_ThongBao_OK("Sử dụng " + ItemHair.get_item(temp.id, p.data_yesno[2]).name + " cho bản thân");

                        } else {
                            p.getService().send_box_ThongBao_OK("Chưa mua vật phẩm này!");
                        }
                    }

                }
                break;
                case 91: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 91) {
                        p.useTAQ = 1;
                        p.setyesNoDialog(new model.YesNoDialog(p, (p.data_yesno[1] + 4000), "Thông báo",
                                "Bạn có muốn sử dụng " + ItemTemplate4.get_it_by_id(p.data_yesno[1]).name + " cho bản thân",
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();

                    }
                }
                break;
                case 90: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 90 && !p.mbv.isBlank() && p.timeHuyMbv == -1) {
                        p.timeHuyMbv = System.currentTimeMillis() + 60_000L * 60 * 24;
                        p.getService().send_box_ThongBao_OK("Đăng ký thành công");
                    }
                }
                break;
                case 89: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 89) {
                        ItemFashionP2 temp = p.check_fashion(p.data_yesno[1]);
                        if (temp != null) {
                            p.update_fashionP2(temp);
                            for (int i = 0; i < p.map.players.size(); i++) {
                                Player p0 = p.map.players.get(i);
                                p.getService().charWearing(p0, false);
                            }
                            p.getService().UpdateInfoMaincharInfo();
                            ItemFashionP.show_table(p, 105);
                            p.getService().send_box_ThongBao_OK("Mặc thành công " + ItemFashion.get_item(temp.id).name);
                        }
                    }
                }
                break;
                case 88: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 88) {
                        ItemFashionP2 temp = p.check_fashion(p.data_yesno[1]);
                        if (temp != null) {
                            if (temp.is_use) {
                                temp.is_use = false;
                                //
                                if (temp.id == 122) {
                                    p.tocSuper = 0;
                                }
                                //
                                p.update_info_to_all();
                                for (int i = 0; i < p.map.players.size(); i++) {
                                    Player p0 = p.map.players.get(i);
                                    p.getService().charWearing(p0, false);
                                }
                                p.getService().UpdateInfoMaincharInfo();
                                ItemFashionP.show_table(p, 105);
                                p.getService().send_box_ThongBao_OK("Tháo thành công " + ItemFashion.get_item(temp.id).name);
                            }
                        }
                    }
                }
                break;
                case 87: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 87) {
                        int bagIndex = p.data_yesno[1];
                        p.data_yesno = null;
                        if (bagIndex >= 0 && bagIndex < p.item.bag3.length) {
                            Item_wear it = p.item.bag3[bagIndex];
                            if (it != null) {
                                p.use_item_3 = bagIndex;
                                if (it.typelock == 1 || it.valueKichAn == 12) {
                                    p.wear_item(it);
                                } else {
                                    p.setyesNoDialog(new model.YesNoDialog(p, 0, "Thông báo",
                                            "Khi trang bị lên người vật phẩm "
                                            + ItemTemplate3.get_it_by_id(it.template.id).name
                                            + " sẽ chuyển sang trạng thái khóa không thể giao dịch. "
                                            + "Bạn có muốn trang bị?",
                                            new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                                    p.getService().startYesNo();
                                }
                            }
                        }
                    }
                    break;
                }
                case 86: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 86) {
                        if (p.detu.pointAttribute >= 1) {
                            switch (p.data_yesno[1]) {
                                case 1:
                                    if ((p.detu.point1 + 1) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point1 += 1;
                                    break;
                                case 2:
                                    if ((p.detu.point2 + 1) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point2 += 1;
                                    break;
                                case 3:
                                    if ((p.detu.point3 + 1) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point3 += 1;
                                    break;
                                case 4:
                                    if ((p.detu.point4 + 1) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point4 += 1;
                                    break;
                                case 5:
                                    if ((p.detu.point5 + 1) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point5 += 1;
                                    break;
                            }
                            p.detu.pointAttribute -= 1;
                            p.detu.update_info_to_all();
                            p.getService().send_box_ThongBao_OK("Nâng thành công");
                        } else {
                            p.getService().send_box_ThongBao_OK("Không đủ điểm tiềm năng");
                        }
                    }
                }
                break;
                case 85: {
                    if (p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 85) {
                        if (p.get_vang() < 500) {
                            p.getService().send_box_ThongBao_OK("Không đủ 500 beri");
                            p.data_yesno = null;
                            return;
                        }
                        if (BotTruyNa.ok(p, p.data_yesno[1])) {
                            p.update_vang(-500);
                            p.updateMoney();
                        }

                    }
                }
                break;
                case 84: {
                    if (p.data_yesno != null && p.data_yesno.length >= 2 && p.data_yesno[0] == 84) {
                        int ticket = p.data_yesno[1];
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).equals(p)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            List<Player> teamMembers = new ArrayList<>();
                            for (Player mem : p.party.list) {
                                Player p0 = Zone.get_player_by_name_allmap(mem.name);
                                if (p0 == null) p0 = mem;
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                boolean hasEntry = false;
                                if (ticket > 0 && p0.item.total_item_bag_by_id(4, ticket) >= 1) {
                                    hasEntry = true;
                                } else if (p0.get_key_boss() >= 1) {
                                    hasEntry = true;
                                }
                                if (!hasEntry) {
                                    missingKeys.add(p0.name);
                                }
                                teamMembers.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            MapDaoKhoBau dungeon = MapDaoKhoBau.createDungeon(p, ticket);
                            for (Player p0 : teamMembers) {
                                if (ticket > 0 && p0.item.total_item_bag_by_id(4, ticket) >= 1) {
                                    p0.item.remove_item47(4, ticket, 1);
                                    p0.item.updateInventory(false);
                                } else {
                                    p0.update_key_boss(-1);
                                    p0.updateMoney();
                                }
                                p0.getService().CountDown_Ticket();
                                p0.save_previous_map();
                                if (p0.map != null) {
                                    p0.map.leave_map(p0, 2);
                                }
                                dungeon.join(p0);
                            }
                        } else {
                            boolean hasEntry = false;
                            if (ticket > 0 && p.item.total_item_bag_by_id(4, ticket) >= 1) {
                                hasEntry = true;
                                p.item.remove_item47(4, ticket, 1);
                                p.item.updateInventory(false);
                            } else if (p.get_key_boss() >= 1) {
                                hasEntry = true;
                                p.update_key_boss(-1);
                                p.updateMoney();
                            }
                            if (!hasEntry) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.getService().CountDown_Ticket();
                            p.save_previous_map();
                            p.map.leave_map(p, 2);
                            MapDaoKhoBau dungeon = MapDaoKhoBau.createDungeon(p, ticket);
                            dungeon.join(p);
                        }
                        p.data_yesno = null;
                    }
                }
                break;
                case 83: {
                    if (event.EventManager.isActive(9) && p.data_yesno != null && p.data_yesno.length > 0 && p.data_yesno[0] == 83) {
                        EventData temp_select = null;
                        for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                            if (p.eventData.get(i2).eventID == 9) {
                                temp_select = p.eventData.get(i2);
                                break;
                            }
                        }
                        if (temp_select != null) {
                            if (temp_select.data[SuKienTrongCay.SHOP_SIZE] >= 100) {
                                if (temp_select.data[SuKienTrongCay.SHOP_SIZE + 15] == 0) {
                                    temp_select.data[SuKienTrongCay.SHOP_SIZE + 15] = 1;
                                    List<GiftBox> listGift = new ArrayList<>();
                                    GiftBox.addGift(listGift, 4, 339, 5);
                                    if (!listGift.isEmpty()) {
                                        Service.send_gift(p, 1, "Thưởng trồng cây", "Nhận được", listGift, true);
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Bạn đã nhận quà rồi");
                                }
                            } else {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ số lần trồng cây may mắn để nhận.Bạn mới trồng được " + temp_select.data[SuKienTrongCay.SHOP_SIZE] + "/100 cây may mắn");
                            }
                        }
                    }
                }
                break;

                case 82: {
                    if (p.clan == null || (p.clan.getTypeMem(p) != 0 && p.clan.getTypeMem(p) != 1)) {
                        p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");
                        p.data_yesno = null;
                        return;
                    }
                    if (!ThuLinhBienKhoi.isOpen()) {
                        p.getService().send_box_ThongBao_OK("Hoạt động Thủ Lĩnh Biển Khơi chưa đến giờ mở cửa (19:00 Thứ 2, Thứ 4, Thứ 6)!");
                        p.data_yesno = null;
                        return;
                    }
                    if (p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 82) {
                        p.tableTickOption = null;
                        if (!ThuLinhBienKhoi.updateFlag(p.data_yesno[1])) {
                            p.getService().send_box_ThongBao_OK("Hiện tại số lượng phe này đã đầy, xin hãy chọn phe khác hoặc thử lại sau");
                            p.data_yesno = null;
                            return;
                        }
                        ClanMember me = null;
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            if (p.clan.members.get(i).name.equals(p.name)) {
                                me = p.clan.members.get(i);
                                break;
                            }
                        }
                        ClanChat chat = new ClanChat();
                        chat.idMem = me.id;
                        chat.name = me.name;
                        String[] name = new String[]{"Đông", "Tây", "Nam", "Bắc"};
                        chat.str = "Thủ lĩnh biển khơi: chọn phe Biển " + name[p.data_yesno[1] - 4];
                        chat.time = System.currentTimeMillis();
                        chat.typeChat = -4;
                        p.clan.add_chat(chat);
                        //
                        p.clan.send_chat(chat, null);
                        //
                        int index_mob = -2;
                        Zone mapTemplate;
                        if (p.data_yesno[1] == 4) {
                            mapTemplate = Zone.getMapByID(182)[0];
                        } else if (p.data_yesno[1] == 5) {
                            mapTemplate = Zone.getMapByID(183)[0];
                        } else if (p.data_yesno[1] == 6) {
                            mapTemplate = Zone.getMapByID(181)[0];
                        } else {
                            mapTemplate = Zone.getMapByID(178)[0];
                        }
                        Zone map_dungeon = new Zone();
                        map_dungeon.template = mapTemplate.template;
                        map_dungeon.zone_id = (byte) 0;
                        map_dungeon.list_mob = new int[0];
                        map_dungeon.map_thuLinhBienKhoi = new ThuLinhBienKhoi(p.data_yesno[1]);
//                     map_dungeon.map_little_garden.mobs = new ArrayList<>();
//                     map_dungeon.map_little_garden.time = System.currentTimeMillis() + 60_000L * 15;
//                     map_dungeon.map_little_garden.clan1 = clan1;
//                     map_dungeon.map_little_garden.clan2 = clan2;
                        map_dungeon.map_thuLinhBienKhoi.flag = p.data_yesno[1];
                        //
                        p.clan.map_create = map_dungeon;
                        //
//                     for (int i = 0; i < mapTemplate.list_mob.length; i++) {
//                        Mob temp = Mob.ENTRYS.get(mapTemplate.list_mob[i]);
//                        Mob mob_add = new Mob();
//                        mob_add.mob_template = temp.mob_template;
//                        mob_add.x = temp.x;
//                        mob_add.y = temp.y;
//                        mob_add.hp_max = temp.mob_template.hp_max;
//                        mob_add.hp = mob_add.hp_max;
//                        mob_add.level = 75;
//                        mob_add.isdie = false;
//                        mob_add.id_target = -1;
//                        mob_add.index = index_mob--;
//                        mob_add.map = map_dungeon;
//                        mob_add.boss_info = null;
//                        map_dungeon.map_little_garden.mobs.add(mob_add);
//                     }
                        map_dungeon.start_map();
                        //
                        Zone.add_map_plus(map_dungeon);
                        //
                        p.tableTickOption = new functions.ThuLinhBienKhoiTick(p);
                        p.tableTickOption.listP = new ArrayList<>();
                        p.tableTickOption.listP.add(p);
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                            if (p0 != null && p0.index_map != p.index_map && p0.map.equals(p.map)) {
                                p.tableTickOption.listP.add(p0);
                            }
                        }
                        p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];
                        p.tableTickOption.list_check[0] = 1;
                        for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
                            p.tableTickOption.list_check[i] = 0;
                        }
                        p.tableTickOption.show("Thủ lĩnh biển khơi");
                    }
                }
                break;
                case 81: {
                    if (ThuLinhBienKhoi.isOpen() && p.clan != null && (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1) && p.clan.map_create != null) {
                        List<Player> listP = new ArrayList<>();
                        if (p.tableTickOption != null && p.tableTickOption.listP != null) {
                            for (int i = 0; i < p.tableTickOption.listP.size(); i++) {
                                if (p.tableTickOption.list_check != null && i < p.tableTickOption.list_check.length && p.tableTickOption.list_check[i] == 1) {
                                    Player p0 = p.tableTickOption.listP.get(i);
                                    if (p0 != null) {
                                        listP.add(p0);
                                    }
                                }
                            }
                        }
                        if (listP.isEmpty()) {
                            listP.add(p);
                        }
                        int seaFlag = (p.clan.map_create.map_thuLinhBienKhoi != null) ? p.clan.map_create.map_thuLinhBienKhoi.flag : 4;
                        List<String> missingKeys = new ArrayList<>();
                        for (Player l : listP) {
                            if (l != null && !l.isBot && !l.isDe && !(l instanceof model.DeTu) && !(l instanceof bot.mercenary.MercenaryBot)) {
                                if (l.get_key_boss() < 2) {
                                    missingKeys.add(l.name);
                                }
                            }
                        }
                        if (!missingKeys.isEmpty()) {
                            p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 2 chìa khoá phó bản");
                            p.data_yesno = null;
                            return;
                        }
                        for (Player l : listP) {
                            if (l != null && !l.isBot && !l.isDe && !(l instanceof model.DeTu) && !(l instanceof bot.mercenary.MercenaryBot)) {
                                l.update_key_boss(-2);
                                l.updateMoney();
                            }
                        }
                        Vgo vgo = new Vgo();
                        vgo.map_go = new Zone[1];
                        vgo.map_go[0] = p.clan.map_create;
                        vgo.xnew = 100;
                        vgo.ynew = 230;
                        listP.forEach(l -> {
                            if (l != null) {
                                l.save_previous_map();
                                l.tableTickOption = null;
                                l.type_pk = -1; // Tắt PK khi ở map biển riêng của clan
                                try {
                                    l.goto_map(vgo);
                                    if (l.getService() != null) {
                                        l.getService().update_PK(l, false);
                                    }
                                } catch (IOException ignored) {
                                }
                            }
                        });
                    }
                    break;
                }

                case 80: {
                    Sudo mySudo = Sudo.getSuDoByName(p.name);
                    if (p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 80 && mySudo != null && mySudo.chucInSudo == 1) {
                        Player p0 = Zone.get_player_by_Index_allmap(p.data_yesno[1]);
                        if (p0 != null) {
                            if (mySudo.nameRelative.size() >= 4) {
                                p0.getService().send_box_ThongBao_OK(p.name + " đã nhận đủ 3 đệ tử");
                                p.getService().send_box_ThongBao_OK("Bạn đã có 3 đệ tử, không thể nhận thêm");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.data_yesno_gem = null;
                                return;
                            }
                            if (Sudo.getSuDoByName(p0.name) != null) {
                                p.getService().send_box_ThongBao_OK(p0.name + " đã gia nhập sư môn khác!");
                                p0.getService().send_box_ThongBao_OK("Bạn đã có sư môn rồi!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.data_yesno_gem = null;
                                return;
                            }
                            Sudo sudoAdd = new Sudo();
                            sudoAdd.id = p0.IDPlayer;
                            sudoAdd.accountId = p0.conn != null ? p0.conn.idUser : 0;
                            sudoAdd.name = p0.name;
                            sudoAdd.head = p0.head;
                            sudoAdd.hair = p0.hair;
                            sudoAdd.hat = p0.get_hat();
                            sudoAdd.chucInSudo = 2;
                            sudoAdd.lvExp = 1;
                            sudoAdd.exp = 0;
                            sudoAdd.point = 1;
                            sudoAdd.nameRelative = new ArrayList<>();
                            sudoAdd.nameRelative.add(p.name);
                            sudoAdd.nameRelative.add(p0.name);
                            mySudo.nameRelative.add(p0.name);
                            Sudo.addSuDoByName(p0.name, sudoAdd);
                            Sudo.updateDb();
                            p.getService().sendSudoInfo(false);
                            p0.getService().sendSudoInfo(false);
                            p.getService().sendSudoMemberList();
                            p.getService().send_box_ThongBao_OK(p0.name + " trở thành đồ đệ của bạn");
                            p0.getService().send_box_ThongBao_OK(p.name + " trở thành sư phụ của bạn");
                        } else {
                            p.getService().send_box_ThongBao_OK("Nhân vật đã offline");
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                        p.data_yesno_gem = null;
                    }
                }
                break;
                case 75: {// vuong code đổi tên
                    try {
                        if (p.nameNew != null && p.data_yesno != null && p.data_yesno.length == 1
                                && p.data_yesno[0] == 75) {
                            p.nameNew = p.nameNew.trim();
                            if (p.nameNew.isEmpty() || p.nameNew.equalsIgnoreCase(p.name)) {
                                p.getService().send_box_ThongBao_OK("Tên mới không hợp lệ hoặc trùng tên cũ!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.nameNew = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, 271) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 thẻ đổi tên!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.nameNew = null;
                                return;
                            }
                            // Check if new name is already taken in DB
                            if (DbManager.getPlayerIdByName(p.nameNew) > 0) {
                                p.getService().send_box_ThongBao_OK("Tên đã được sử dụng! Vui lòng chọn tên khác");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.nameNew = null;
                                return;
                            }
                            p.getService().send_box_ThongBao_OK("Chờ trong giây lát...");

                            // 1. Update players table
                            try (Connection conn = DbManager.gI().getConnect(); PreparedStatement ps = conn.prepareStatement(
                                    "UPDATE `players` SET `name` = ? WHERE `id` = ?")) {
                                ps.setString(1, p.nameNew);
                                ps.setInt(2, p.IDPlayer);
                                ps.executeUpdate();
                                zLog.gI().add_log(p, "NAME_CHANGE", "Đổi tên từ: " + p.name + " thành " + p.nameNew);
                            } catch (SQLException e) {
                                p.getService().send_box_ThongBao_OK("Tên đã được sử dụng! Vui lòng chọn tên khác");
                                p.data_yesno = null;
                                p.map_tele = null;
                                p.nameNew = null;
                                return;
                            }

                            // 2. Consume item
                            p.item.remove_item47(4, 271, 1);
                            p.item.updateInventory(false);

                            // 3. Update session list_char in memory
                            if (p.conn != null && p.conn.list_char != null) {
                                for (int i = 0; i < p.conn.list_char.size(); i++) {
                                    if (p.name.equals(p.conn.list_char.get(i))) {
                                        p.conn.list_char.set(i, p.nameNew);
                                    }
                                }
                            }

                            // 4. Update in-memory Clan data & flush
                            for (int i = 0; i < Clan.ENTRY.size(); i++) {
                                Clan clan = Clan.ENTRY.get(i);
                                if (clan == null) continue;
                                for (int j = 0; j < clan.mem_request.size(); j++) {
                                    ClanMember memReq = clan.mem_request.get(j);
                                    if (memReq != null && (memReq.playerId == p.IDPlayer || (memReq.name != null && memReq.name.equals(p.name)))) {
                                        memReq.playerId = p.IDPlayer;
                                        memReq.name = p.nameNew;
                                    }
                                }
                                for (int j = 0; j < clan.members.size(); j++) {
                                    ClanMember mem = clan.members.get(j);
                                    if (mem != null && (mem.playerId == p.IDPlayer || (mem.name != null && mem.name.equals(p.name)))) {
                                        mem.playerId = p.IDPlayer;
                                        mem.name = p.nameNew;
                                    }
                                }
                                for (int j2 = 0; j2 < clan.chat.size(); j2++) {
                                    if (clan.chat.get(j2) != null && clan.chat.get(j2).str != null) {
                                        clan.chat.get(j2).str = clan.chat.get(j2).str.replace(p.name, p.nameNew);
                                    }
                                }
                            }
                            Clan.update();

                            // 5. Update Sudo & flush
                            Sudo mySudo = Sudo.getSuDoById(p.IDPlayer);
                            if (mySudo == null) {
                                mySudo = Sudo.getSuDoByName(p.name);
                            }
                            if (mySudo != null) {
                                Sudo.removeSudo(p.name);
                                mySudo.name = p.nameNew;
                                mySudo.id = p.IDPlayer;
                                if (mySudo.nameRelative != null) {
                                    for (int i = 0; i < mySudo.nameRelative.size(); i++) {
                                        if (mySudo.nameRelative.get(i).equals(p.name)) {
                                            mySudo.nameRelative.set(i, p.nameNew);
                                        } else {
                                            Sudo oSudo = Sudo.getSuDoByName(mySudo.nameRelative.get(i));
                                            if (oSudo != null && oSudo.nameRelative != null) {
                                                for (int j = 0; j < oSudo.nameRelative.size(); j++) {
                                                    if (oSudo.nameRelative.get(j).equals(p.name)) {
                                                        oSudo.nameRelative.set(j, p.nameNew);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Sudo.addSuDoByName(p.nameNew, mySudo);
                                Sudo.updateDb();
                            }

                            // 6. Update DeTu (chỉ đổi tên đệ tử nếu đệ tử mang tên theo sư phụ)
                            if (p.detu != null && (p.detu.name.equals(p.name) || p.detu.name.equals(p.name + "_detu"))) {
                                DeTu.renameDeTu(p, p.nameNew);
                            }

                            // 7. Update online players' friend and enemy lists
                            for (Player onlineP : network.SessionManager.PLAYERS_MAP.values()) {
                                if (onlineP == null || onlineP.isClosed) continue;
                                if (onlineP.friend_list != null) {
                                    for (template.FriendTemp ft : onlineP.friend_list) {
                                        if (ft != null && ft.playerId == p.IDPlayer) {
                                            ft.name = p.nameNew;
                                        }
                                    }
                                }
                                if (onlineP.enemy_list != null) {
                                    for (template.FriendTemp et : onlineP.enemy_list) {
                                        if (et != null && et.playerId == p.IDPlayer) {
                                            et.name = p.nameNew;
                                        }
                                    }
                                }
                            }

                            // 8. Clear CacheManager cache entries
                            database.CacheManager.gI().remove("players_" + p.name);
                            database.CacheManager.gI().remove("players_" + p.nameNew);
                            database.CacheManager.gI().remove("players_detu_" + p.IDPlayer);
                            database.CacheManager.gI().remove("players_" + p.name + "_detu");
                            database.CacheManager.gI().remove("players_" + p.nameNew + "_detu");

                            // 9. Update player instance name, flush, and disconnect
                            String oldName = p.name;
                            p.name = p.nameNew;
                            p.nameNew = null;
                            p.flush(p, false);
                            database.CacheManager.gI().remove("players_" + oldName);
                            database.CacheManager.gI().remove("players_" + p.name);

                            p.getService().send_box_ThongBao_OK("Đổi tên thành công! Thoát game để có hiệu lực! Tên mới: " + p.name);
                            p.disconnectSC();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                case 74: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 74) {
                        int rubyReq = VongQuayOcSen.getRubyQuay(p);
                        if (p.get_ngoc() >= rubyReq) {
                            p.update_ngoc(-rubyReq);
                            p.updateMoney();
                            int index = ZUtil.random(p.itemVongQuayOcSen.length);
                            int time = 0;
                            while (p.itemVongQuayOcSen[index] == 1) {
                                index = ZUtil.random(p.itemVongQuayOcSen.length);
                                if (time > 1_000) {
                                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                                    return;
                                }
                                time++;
                            }
                            if (95 > ZUtil.random(100)) {
                                if (VongQuayOcSen.CAT[index] == 105) {
                                    while (p.itemVongQuayOcSen[index] == 1) {
                                        index = ZUtil.random(p.itemVongQuayOcSen.length);
                                        if (time > 1_000) {
                                            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                                            return;
                                        }
                                        time++;
                                    }
                                }
                            }
                            p.itemVongQuayOcSen[index] = 1;
                            Message m = new Message(77);
                            m.writer().writeByte(2);
                            m.writer().writeByte(index);
                            p.addmsg(m);
                            m.cleanup();
                            //
                            String rewardName = "";
                            if (VongQuayOcSen.CAT[index] == 7) {
                                ItemTemplate7 it_temp7 = ItemTemplate7.get_it_by_id(VongQuayOcSen.ID[index]);
                                if (it_temp7 != null) {
                                    p.item.add_item_bag47(7, it_temp7.id, 1);
                                    rewardName = it_temp7.name;
                                }
                            } else if (VongQuayOcSen.CAT[index] == 105) {
                                ItemFashion itf = ItemFashion.get_item(VongQuayOcSen.ID[index]);
                                if (itf != null) {
                                    rewardName = itf.name;
                                    if (p.check_fashion(itf.ID) == null) {
                                        ItemFashionP2 temp2 = new ItemFashionP2();
                                        temp2.id = itf.ID;
                                        p.fashion.add(temp2);
                                        p.update_fashionP2(temp2);
                                        for (int i = 0; i < p.map.players.size(); i++) {
                                            Player p0 = p.map.players.get(i);
                                            p.getService().charWearing(p0, false);
                                        }
                                        p.getService().UpdateInfoMaincharInfo();
                                    }
                                }
                            } else {
                                ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(VongQuayOcSen.ID[index]);
                                if (it_temp4 != null) {
                                    rewardName = it_temp4.name;
                                    if (it_temp4.id == 0) {
                                        p.update_vang(20_000);
                                        p.updateMoney();
                                        rewardName = "20,000 Beri";
                                    } else if (it_temp4.id == 6) {
                                        p.update_ticket(1);
                                        p.updateMoney();
                                        rewardName = "1 Vé";
                                    } else {
                                        p.item.add_item_bag47(4, it_temp4.id, 1);
                                    }
                                }
                            }
                            p.item.updateInventory(false);
                            // Log
                            String logData = String.format("{\"action\":\"VONG_QUAY_OC_SEN_RUBY\", \"ruby_spent\":%d, \"index\":%d, \"cat\":%d, \"item_id\":%d, \"reward\":\"%s\"}",
                                    rubyReq, index, VongQuayOcSen.CAT[index], VongQuayOcSen.ID[index], rewardName);
                            zLog.gI().add_log(p, "VONG_QUAY_OC_SEN", logData);
                        } else {
                            p.getService().send_box_ThongBao_OK("Không đủ " + ZUtil.number_format(rubyReq) + " Ruby để tiếp tục!");
                        }
                    }
                    break;
                }
                case 68: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 68) {
                        List<Player> listP = new ArrayList<>();
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).equals(p)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                break;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            for (int i = 0; i < p.party.list.size(); i++) {
                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                                if (p0 == null) p0 = p.party.list.get(i);
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || p.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                if (p0.get_key_boss() < 2) {
                                    missingKeys.add(p0.name);
                                }
                                listP.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        } else {
                            if (p.get_key_boss() < 2) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            listP.add(p);
                        }
                        List<Player> allParticipants = new ArrayList<>(listP);
                        for (Player realP : listP) {
                            if (realP != null && !realP.isBot && !realP.isDe && !(realP instanceof model.DeTu) && !(realP instanceof bot.mercenary.MercenaryBot)) {
                                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(realP);
                                if (activeMercs != null) {
                                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                        if (merc != null && !merc.isdie && !allParticipants.contains(merc)) {
                                            allParticipants.add(merc);
                                        }
                                    }
                                }
                            }
                        }
                        Map_Sieu_Lien_Tang dungeon = Map_Sieu_Lien_Tang.createDungeon(allParticipants, 199);
                        dungeon.join(allParticipants);
                    }
                }
                break;
                case 67: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 67) {
                        List<Player> listP = new ArrayList<>();
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).equals(p)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                break;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            for (int i = 0; i < p.party.list.size(); i++) {
                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                                if (p0 == null) p0 = p.party.list.get(i);
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || p.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                if (p0.get_key_boss() < 2) {
                                    missingKeys.add(p0.name);
                                }
                                listP.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        } else {
                            if (p.get_key_boss() < 2) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            listP.add(p);
                        }

                        // Tự động mang lính đánh thuê đang xuất chiến vào phó bản Mr3
                        List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
                        if (activeMercs != null && !activeMercs.isEmpty()) {
                            for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                if (merc != null && !merc.isdie && !listP.contains(merc)) {
                                    listP.add(merc);
                                }
                            }
                        }

                        Map_Mr3 dungeon = Map_Mr3.createDungeon(listP);
                        dungeon.join(listP);
                    }
                }
                break;

                case 66: {
                    p.huongnghiep = 2;
                    p.ensureFactionSkill();
                    p.send_skill();
                    p.update_info_to_all();
                    zLog.gI().add_log(p, "HUONG_NGHIEP", "Gia nhập phe Hải Tặc");
                    p.getService().send_box_ThongBao_OK("Chúc mừng bạn đã gia nhập phe Hải Tặc!");
                    break;
                }
                case 65: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 65) {
                        ClanMember mem = null;
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            if (p.clan.members.get(i).levelInclan == 1) {
                                mem = p.clan.members.get(i);
                                break;
                            }
                        }
                        if (mem != null) {
                            ClanMember myMem = p.clan.members.get(0);
                            p.clan.members.remove(0);
                            p.clan.members.remove(mem);
                            p.clan.members.add(0, mem);
                            mem.levelInclan = 0;
                            p.clan.members.add(myMem);
                            myMem.levelInclan = 10;
                            //
                            Player p0 = Zone.get_player_by_name_allmap(mem.name);
                            if (p0 != null) {
                                Clan.send_info(p0, false);
                                for (int i = 0; i < p.map.players.size(); i++) {
                                    if (!p.map.players.get(i).equals(p0)) {
                                        Clan.send_me_to_other(p0, p.map.players.get(i), false);
                                    }
                                }
                            }
                            Clan.send_info(p, false);
                            for (int i = 0; i < p.map.players.size(); i++) {
                                if (!p.map.players.get(i).equals(p)) {
                                    Clan.send_me_to_other(p, p.map.players.get(i), false);
                                }
                            }
                            for (int i = 0; i < p.clan.members.size(); i++) {
                                Player p0ther = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                if (p0ther != null) {
                                    Clan.update_list_member(p0ther, false);
                                    Clan.send_info(p0ther, false);
                                }
                                Clan.update_list_member(p, false);
                                Clan.send_info(p, false);
                            }
                            p.getService().send_box_ThongBao_OK("Nhường thuyền trưởng cho " + mem.name + " thành công");
                        }
                    }
                }
                break;
                case 64: {
                    p.huongnghiep = 1;
                    p.ensureFactionSkill();
                    p.send_skill();
                    p.update_info_to_all();
                    zLog.gI().add_log(p, "HUONG_NGHIEP", "Gia nhập phe Hải Quân");
                    p.getService().send_box_ThongBao_OK("Chúc mừng con đã lựa chọn sáng suốt, hãy luyện tập để bảo vệ công lý!");
                    break;
                }
                case 69: {
                    p.huongnghiep = 3;
                    p.ensureFactionSkill();
                    p.send_skill();
                    p.update_info_to_all();
                    zLog.gI().add_log(p, "HUONG_NGHIEP", "Gia nhập phe Quân Cách Mạng");
                    p.getService().send_box_ThongBao_OK("Chúc mừng bạn đã gia nhập phe Quân Cách Mạng!");
                    break;
                }
                 case -63: {
                    if (p.clan != null) {
                        boolean isLeader = p.clan.members.get(0).name.equals(p.name);

                        if (isLeader) {
                            if (Clan.delete_clan_from_db(p.clan)) {
                                for (ClanMember member : p.clan.members) {
                                    Player memberPlayer = Zone.get_player_by_name_allmap(member.name);
                                    if (memberPlayer != null) {
                                        Clan.send_info(memberPlayer, false);
                                        for (int i = 0; i < p.map.players.size(); i++) {
                                            if (!p.map.players.get(i).equals(memberPlayer)) {
                                                Clan.send_me_to_other(memberPlayer, p.map.players.get(i), false);
                                            }
                                        }
                                        memberPlayer.getService().send_box_ThongBao_OK("Băng của bạn đã bị xóa");

                                        Message m = new Message(-52);
                                        m.writer().writeByte(10);
                                        m.writer().writeShort(memberPlayer.index_map);
                                        p.map.send_msg_all_p(m, memberPlayer, true);
                                        m.cleanup();

                                        memberPlayer.clan = null;
                                    }
                                }
                                p.clan = null;
                                p.getService().send_box_ThongBao_OK("Xóa băng thành công");
                            } else {
                                p.getService().send_box_ThongBao_OK("Xóa băng thất bại");
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Chỉ truyền trưởng mới có thể xóa");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn không có băng để xóa");
                    }
                    break;
                }
                case 63: {
                    if (p.map.vuonCam == null) {
                        List<Player> listP = new ArrayList<>();
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).name.equals(p.name)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            for (int i = 0; i < p.party.list.size(); i++) {
                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                                if (p0 == null) {
                                    p0 = p.party.list.get(i);
                                }
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                if (p0.get_key_boss() < 2) {
                                    missingKeys.add(p0.name);
                                }
                                listP.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        } else {
                            if (p.get_key_boss() < 2) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            listP.add(p);
                        }

                        List<Player> allParticipants = new ArrayList<>(listP);
                        for (Player realP : listP) {
                            if (realP != null && !realP.isBot && !realP.isDe && !(realP instanceof model.DeTu) && !(realP instanceof bot.mercenary.MercenaryBot)) {
                                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(realP);
                                if (activeMercs != null) {
                                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                        if (merc != null && !merc.isdie && !allParticipants.contains(merc)) {
                                            allParticipants.add(merc);
                                        }
                                    }
                                }
                            }
                        }

                        VuonCam dungeon = VuonCam.createDungeon(allParticipants);
                        dungeon.join(allParticipants, p.party);
                    }
                    break;
                }
                case 62: {
                    if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 62) {
                        List<Player> listP = new ArrayList<>();
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).equals(p)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                break;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            for (int i = 0; i < p.party.list.size(); i++) {
                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                                if (p0 == null) p0 = p.party.list.get(i);
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || p.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                if (p0.get_key_boss() < 2) {
                                    missingKeys.add(p0.name);
                                }
                                listP.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        } else {
                            if (p.get_key_boss() < 2) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 2 chìa khoá phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            listP.add(p);
                        }
                        List<Player> allParticipants = new ArrayList<>(listP);
                        for (Player realP : listP) {
                            if (realP != null && !realP.isBot && !realP.isDe && !(realP instanceof model.DeTu) && !(realP instanceof bot.mercenary.MercenaryBot)) {
                                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(realP);
                                if (activeMercs != null) {
                                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                        if (merc != null && !merc.isdie && !allParticipants.contains(merc)) {
                                            allParticipants.add(merc);
                                        }
                                    }
                                }
                            }
                        }
                        Map_Lien_Tang dungeon = Map_Lien_Tang.createDungeon(allParticipants, 9990);
                        dungeon.join(allParticipants);
                    }
                    break;
                }
                case 60: {// vuong code đổi coin sang rubi
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        int ruby = p.data_yesno[0];
                        int coin = ruby * 5;

                        if (p.get_coin() < coin) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(coin) + " coin");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        } else {// vuong code
                            // Nếu số coin là từ 200.000 trở lên, cộng thêm 25% vào số ruby
                            if (coin >= 200000) {
                                ruby += ruby * 25 / 100;  // Thêm 25% ruby
                            }

                            // Trừ số coin gốc và cập nhật ruby đã tăng (nếu có)
                            p.update_coin(-coin);
                            p.update_ngoc(ruby);  // Cập nhật ruby đã tăng 25% nếu đủ điều kiện
                            p.updateMoney();

                            p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công " + ZUtil.number_format(coin) + " coin ra "
                                            + ZUtil.number_format(ruby) + " Ruby.");
                        }
                    }
                    break;
                }
                case 59: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(p.data_yesno[0]);
                        if (itemTemplate4 != null) {
                            int currentCat = HanhTrinh.getIslandCategory(p.map.template.id);
                            if (currentCat == -1) {
                                p.getService().send_box_ThongBao_OK("Không thể tách đá ở khu vực này!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            ItemBag47 it_select = null;
                            for (int i = 0; i < p.daHanhTrinh.size(); i++) {
                                ItemBag47 it = p.daHanhTrinh.get(i);
                                if (it != null && it.category == currentCat
                                        && it.id == p.data_yesno[0]
                                        && it.quant == 1) {
                                    it_select = it;
                                    break;
                                }
                            }
                            if (it_select != null) {
                                if (p.get_ngoc() < 100) {
                                    p.getService().send_box_ThongBao_OK("Không đủ 100 ruby");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                p.update_ngoc(-100);
                                p.updateMoney();
                                //
                                it_select.quant = 0;
                                HanhTrinh.update_da_kham(p);
                                //
                                Message m = new Message(79);
                                m.writer().writeByte(0);
                                m.writer().writeShort(HanhTrinh.get_map(p));
                                m.writer().writeUTF(p.map.template.name);
                                List<ItemBag47> list_DaHanhTrinh
                                        = p.get_list_daHanhTrinh_total(p.map.template.id);
                                m.writer().writeByte(list_DaHanhTrinh.size());
                                for (int i = 0; i < list_DaHanhTrinh.size(); i++) {
                                    ItemTemplate4 itemTemplate4_
                                            = ItemTemplate4.get_it_by_id(list_DaHanhTrinh.get(i).id);
                                    if (itemTemplate4_ != null) {
                                        m.writer().writeUTF(itemTemplate4_.name);
                                        m.writer().writeByte(4);
                                        m.writer().writeShort(itemTemplate4_.id);
                                        m.writer().writeByte(1);
                                        m.writer().writeShort(itemTemplate4_.icon);
                                        ItemTemplate4_Info temp_info = ItemTemplate4_Info
                                                .get_by_id(itemTemplate4_.indexInfoPotion);
                                        if (temp_info != null && temp_info.info != null) {
                                            m.writer().writeUTF(temp_info.info);
                                        } else {
                                            m.writer().writeUTF("Chưa có thông tin");
                                        }
                                    }
                                }
                                p.addmsg(m);
                                m.cleanup();
                                //
                                if (p.ability != null) {
                                    p.ability.recalculatePlayerStats(p);
                                }
                                p.getService().UpdateInfoMaincharInfo();
                                p.update_info_to_all();
                                p.flush(p, false);
                                p.getService().send_box_ThongBao_OK("Tách thành công " + itemTemplate4.name);
                            } else {
                                p.getService().send_box_ThongBao_OK("Không tìm thấy đá đã khảm " + itemTemplate4.name);
                            }
                        }
                    }
                    break;
                }
                case 58: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(p.data_yesno[0]);
                        if (itemTemplate4 != null) {
                            int currentCat = HanhTrinh.getIslandCategory(p.map.template.id);
                            if (currentCat == -1) {
                                p.getService().send_box_ThongBao_OK("Không thể khảm đá ở khu vực này!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            ItemBag47 it_select = null;
                            for (int i = 0; i < p.daHanhTrinh.size(); i++) {
                                ItemBag47 it = p.daHanhTrinh.get(i);
                                if (it != null && it.category == currentCat
                                        && it.id == p.data_yesno[0]
                                        && it.quant == 0) {
                                    it_select = it;
                                    break;
                                }
                            }
                            if (it_select != null) {
                                // Tháo đá cũ đã khảm ở đảo này nếu có (không làm mất đá cũ)
                                for (int i = 0; i < p.daHanhTrinh.size(); i++) {
                                    ItemBag47 it = p.daHanhTrinh.get(i);
                                    if (it != null && it.category == currentCat && it.quant == 1) {
                                        it.quant = 0;
                                    }
                                }
                                it_select.quant = 1;
                                //
                                Message m = new Message(79);
                                m.writer().writeByte(2);
                                m.writer().writeShort(p.data_yesno[0]);
                                p.addmsg(m);
                                m.cleanup();
                                HanhTrinh.update_da_kham(p);
                                //
                                if (p.ability != null) {
                                    p.ability.recalculatePlayerStats(p);
                                }
                                p.getService().UpdateInfoMaincharInfo();
                                p.update_info_to_all();
                                p.flush(p, false);
                                p.getService().send_box_ThongBao_OK("Khảm thành công " + itemTemplate4.name);
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 " + itemTemplate4.name);
                            }
                        }
                    }
                    break;
                }
                case 57: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                        if (it_select != null && it_select.template.typeEquip == 7
                                && it_select.numLoKham < 5) {
                            if (it_select.valueChetac < 50) {
                                p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, 457) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 búa đục dial");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            int ruby_req = 50 * (it_select.numLoKham + 1);
                            if (p.get_ngoc() < ruby_req) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ruby_req + " ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-ruby_req);
                            p.updateMoney();
                            p.item.remove_item47(4, 457, 1);
                            if (it_select.numHoleDaDuc < 0) {
                                it_select.numHoleDaDuc = 0;
                            }
                            it_select.numLoKham++;
                            it_select.numHoleDaDuc++;
                            Message m = new Message(-67);
                            m.writer().writeByte(7);
                            m.writer().writeUTF("Đục lỗ thành công " + it_select.template.name);
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        }
                    }
                    break;
                }
                case 56: {
                    if (p.map.map_ThuThachVeThan == null) {
                        List<Player> listP = new ArrayList<>();
                        if (p.party != null && p.party.list.size() > 1) {
                            if (!p.party.list.get(0).name.equals(p.name)) {
                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            List<String> missingKeys = new ArrayList<>();
                            for (int i = 0; i < p.party.list.size(); i++) {
                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);
                                if (p0 == null) {
                                    p0 = p.party.list.get(i);
                                }
                                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                                    continue;
                                }
                                if (p0.map == null || !p0.map.equals(p.map)) {
                                    continue;
                                }
                                if (p0.get_key_boss() < 1) {
                                    missingKeys.add(p0.name);
                                }
                                listP.add(p0);
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        } else {
                            if (p.get_key_boss() < 1) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            listP.add(p);
                        }

                        // Tự động mang lính đánh thuê đang xuất chiến vào Thử Thách Vệ Thần
                        List<Player> allParticipants = new ArrayList<>(listP);
                        for (Player realP : listP) {
                            if (realP != null && !realP.isBot && !realP.isDe && !(realP instanceof model.DeTu) && !(realP instanceof bot.mercenary.MercenaryBot)) {
                                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(realP);
                                if (activeMercs != null && !activeMercs.isEmpty()) {
                                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                                        if (merc != null && !merc.isdie && !allParticipants.contains(merc)) {
                                            allParticipants.add(merc);
                                        }
                                    }
                                }
                            }
                        }

                        PhoBanThuThachVeThan dungeon = PhoBanThuThachVeThan.createDungeon(allParticipants);
                        dungeon.join(allParticipants);
                    }
                    break;
                }
                case 55: {
                    Skill_info sk_select = null;
                    for (int i = 0; i < p.skill_point.size(); i++) {
                        if (p.skill_point.get(i).temp.indexSkillInServer >= 661
                                && p.skill_point.get(i).temp.indexSkillInServer <= 666) {
                            sk_select = p.skill_point.get(i);
                            break;
                        }
                    }
                    if (sk_select == null) {
                        if (p.time_ttvt < 50) {
                            if (p.get_ngoc() < 500) {
                                p.getService().send_box_ThongBao_OK("không đủ 500 ruby để có thể học");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-500);
                            p.updateMoney();
                        }
                        //
                        sk_select = new Skill_info();
                        sk_select.exp = 0;
                        sk_select.temp = Skill_Template.get_temp(661, 0);
                        sk_select.lvdevil = 0;
                        sk_select.devilpercent = 0;
                        p.skill_point.add(sk_select);
                        p.send_skill();
                        p.update_info_to_all();
                        p.getService().send_box_ThongBao_OK("Học thành công kỹ năng chế tạo dial cấp 1");
                    } else {
                        p.getService().send_box_ThongBao_OK("Đã học kỹ năng này rồi");
                    }
                    break;
                }
                case 53: {
                    if (p.name_ThoSanHaiTac != null && p.name_ThoSanHaiTac.length == 1
                            && p.typePirate == 1) {
                        Player p0 = Zone.get_player_by_name_allmap(p.name_ThoSanHaiTac[0]);
                        if (p0 != null && p0.map.equals(p.map) && p0.ship_pet != null) {
                            p0.getService().send_box_ThongBao_OK(p.name + " đồng ý bảo vệ vận hàng, hãy bắt đầu chuyến đi");
                            p.getService().send_box_ThongBao_OK("đồng ý bảo vệ vận hàng thành công, hãy bắt đầu chuyến đi");
                            p0.ship_pet.mainBaoVe = p.name;
                        } else {
                            p.getService().send_box_ThongBao_OK("Đối phương đã rời đi");
                        }
                    }
                    break;
                }
                case 52: {

                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        if (p.party != null && p.party.list != null && p.party.list.size() > 1) {
                            p.getService().send_box_ThongBao_OK("Hãy hủy nhóm trước khi vào phó bản");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        byte mode = (byte) p.data_yesno[0];
                        if (mode < 7) {
                            if (p.get_key_boss() < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_key_boss(-1);
                        } else {
                            if (p.get_key_boss() < 2) {
                                p.getService().send_box_ThongBao_OK("Không đủ 2 chìa khóa phó bản");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_key_boss(-2);
                        }
                        //
                        p.updateMoney();
                        p.getService().CountDown_Ticket();
                        //
                        MapAiDon dungeon = MapAiDon.createDungeon(p, mode);
                        dungeon.join(p);
                    }
                    break;
                }
                case 51: {
                    if (p.clan != null) {
                        List<Player> listP = new ArrayList<>();
                        listP.add(p);
                        TranChienKhongLo.registerClanQueue(p.clan, listP);
                    }
                    break;
                }
                case 50: {
                    if (p.map.template.id == 1 && p.typePirate == 0 && p.id_ship_packet != -1) {
                        if (p.get_vang() < 10_000) {
                            p.getService().send_box_ThongBao_OK("Không đủ 10.000 beri");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.time_ship >= 5) {
                            p.getService().send_box_ThongBao_OK("Hôm nay đã vận chuyến tối đa!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.getConnStatus() != 1) {
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.update_vang(-10_000);
                        p.updateMoney();
                        //
                        Ship.notice_start_shipping(p);
                        //
                        p.ship_pet = new Ship_pet();
                        p.ship_pet.index_map = IDManager.takeID(IDManager.SHIP_PET);
                        if (p.ship_pet.index_map != -1) {
                            p.ship_pet.main_ship = p;
                            p.ship_pet.map = p.map;
                            p.ship_pet.name = "Hàng " + p.name;
                            p.ship_pet.x = 315;
                            p.ship_pet.y = 210;
                            p.ship_pet.hp_max = 2000;
                            p.ship_pet.hp = p.ship_pet.hp_max;
                            p.ship_pet.time_start = System.currentTimeMillis();
                            p.ship_pet.id_map_save = p.map.template.id;
                            Ship_pet.add(p.ship_pet);
                            Message m_local = new Message(1);
                            m_local.writer().writeByte(0);
                            m_local.writer().writeShort(p.ship_pet.index_map);
                            m_local.writer().writeShort(p.ship_pet.x);
                            m_local.writer().writeShort(p.ship_pet.y);
                            for (int j = 0; j < p.map.players.size(); j++) {
                                Player p0 = p.map.players.get(j);
                                p0.addmsg(m_local);
                            }
                            m_local.cleanup();
                        } else {
                            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra hãy thủ lại");
                        }
                    }
                    break;
                }
                case 46: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                        if (it_select != null && it_select.numLoKham < 6
                                && it_select.template != null && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                            if (it_select.valueChetac < 50) {
                                Rebuild_Item.show_table(p, 2);
                                p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, 323) < 1) {
                                Rebuild_Item.show_table(p, 2);
                                p.getService().send_box_ThongBao_OK("Không đủ 1 búa siêu cấp");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.item.remove_item47(4, 323, 1);
                            if (it_select.numHoleDaDuc < 0) {
                                it_select.numHoleDaDuc = 0;
                            }
                            it_select.numLoKham++;
                            it_select.numHoleDaDuc++;
                            Message m = new Message(-67);
                            m.writer().writeByte(7);
                            m.writer().writeUTF("Đục lỗ thành công " + it_select.template.name);
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        } else {
                            Rebuild_Item.show_table(p, 2);
                            p.getService().send_box_ThongBao_OK("Không thể dùng búa siêu cấp với vật phẩm này");
                        }
                    }
                    break;
                }
                case 45: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                        if (it_select != null && it_select.numLoKham < 5
                                && it_select.template != null && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                            if (it_select.valueChetac < 50) {
                                Rebuild_Item.show_table(p, 2);
                                p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, 339) < 1) {
                                Rebuild_Item.show_table(p, 2);
                                p.getService().send_box_ThongBao_OK("Không đủ 1 búa sơ cấp");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.item.remove_item47(4, 339, 1);
                            boolean suc = 10 > ZUtil.random(150);
                            if (suc) {
                                if (it_select.numHoleDaDuc < 0) {
                                    it_select.numHoleDaDuc = 0;
                                }
                                it_select.numLoKham++;
                                it_select.numHoleDaDuc++;
                            } else {
                                it_select.valueChetac -= ZUtil.random(10, 20);
                                if (it_select.valueChetac < 0) {
                                    it_select.valueChetac = 0;
                                }
                            }
                            Message m = new Message(-67);
                            m.writer().writeByte(7);
                            m.writer()
                                    .writeUTF(suc ? ("Đục lỗ thành công " + it_select.template.name)
                                            : "Rất tiếc đục lỗ thất bại");
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        } else {
                            Rebuild_Item.show_table(p, 2);
                            p.getService().send_box_ThongBao_OK("Búa sơ cấp chỉ có thể đục lỗ vật phẩm tối đa 5 lỗ");
                        }
                    }
                    break;
                }
                case 44: {
                    break;
                }
                case 43: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        Player p0 = null;
                        for (int i = 0; i < p.friend_list.size(); i++) {
                            if (p.friend_list.get(i).id == p.data_yesno[0]) {
                                p0 = Zone.get_player_by_id_allmap(p.friend_list.get(i).playerId);
                                break;
                            }
                        }
                        if (p0 == null) {
                            for (int i = 0; i < p.enemy_list.size(); i++) {
                                if (p.enemy_list.get(i).id == p.data_yesno[0]) {
                                    p0 = Zone.get_player_by_id_allmap(p.enemy_list.get(i).playerId);
                                    break;
                                }
                            }
                        }
                        boolean check = false;
                        if (p0 != null) {
                            for (int i = 0; i < p0.friend_list.size(); i++) {
                                if (p0.friend_list.get(i).playerId == p.IDPlayer) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                for (int i = 0; i < p.enemy_list.size(); i++) {
                                    if (p.enemy_list.get(i).playerId == p0.IDPlayer) {
                                        check = true;
                                        break;
                                    }
                                }
                            }
                            if (check) {
                                if (p.get_ngoc() < 5) {
                                    p.getService().send_box_ThongBao_OK("Không đủ 5 ngọc để thực hiện");
                                    return;
                                } 
                                int mapIDcanGo = p.checkQuest();
                                if (p0.map != null && p0.map.template != null) {
                                    if (Zone.isMapNoQuestLimit(p0.map.template.id)
                                            || (p.map != null && p.map.template != null && HanhTrinh.getIslandCategory(p.map.template.id) == HanhTrinh.getIslandCategory(p0.map.template.id))) {
                                        mapIDcanGo = 9999;
                                    }
                                    if (p0.map.template.id > mapIDcanGo) {
                                        p.getService().send_box_ThongBao_OK("Hãy hoàn thành hết nhiệm vụ trước khi đến làng tiếp theo!");
                                        return;
                                    }
                                }
                                 boolean flag = false;
                                 for(zabstracts.AbsBoss boss : SuperBossManager.ENTRYS) {
                                     if(!boss.isdie && boss.map.template.id == p0.map.template.id && boss.map.zone_id == p0.map.zone_id) {
                                         flag = true;
                                         break;
                                     }
                                 }
                                 if(flag) {
                                    p.getService().send_box_ThongBao_OK("Không thể dịch chuyển vào khu đang có siêu trùm!");
                                     return;
                                 }
                                p.update_ngoc(-5);
                                p.updateMoney();
                                Vgo vgo = new Vgo();
                                vgo.map_go = new Zone[1];
                                vgo.map_go[0] = p0.map;
                                vgo.xnew = p0.x;
                                vgo.ynew = p0.y;
                                p.goto_map(vgo);
                            }
                        }
                        if (!check) {
                            p.getService().send_box_ThongBao_OK("Đối phương không online hoặc không có trong danh sách");
                        }
                    }
                    break;
                }
                case 42: {
                    if (p.clan != null) {
                        ClanMember clan_mem = null;
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            if (p.clan.members.get(i).name.equals(p.name)) {
                                clan_mem = p.clan.members.get(i);
                                break;
                            }
                        }
                        if (clan_mem != null) {
                            if(!clan_mem.isJoin24H()) {
                                p.getService().send_box_ThongBao_OK("Tham gia băng trên 24h mới có thể làm nhiệm vụ băng");
                                return;
                            }
                            if (clan_mem.numquest >= 3) {
                                p.getService().send_box_ThongBao_OK("Hôm nay đã hết nhiệm vụ, hãy quay lại vào ngày mai");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            Clan.send_info(p, false);
                            QuestP questP = null;
                            for (int i = 0; i < p.list_quest.size(); i++) {
                                if (p.list_quest.get(i).template.id < -2000) {
                                    questP = p.list_quest.get(i);
                                    break;
                                }
                            }
                            if (questP == null) {
                                clan_mem.numquest++;
                                questP = new QuestP();
                                questP.template
                                        = Quest.get_quest(-3000 + ((clan_mem.numquest - 1) * 2));
                                questP.data = new short[questP.template.data_quest.length][];
                                for (int i = 0; i < questP.data.length; i++) {
                                    questP.data[i]
                                            = new short[questP.template.data_quest[i].length];
                                    for (int j = 0; j < questP.data[i].length; j++) {
                                        questP.data[i][j] = questP.template.data_quest[i][j];
                                    }
                                }
                                p.list_quest.add(questP);
                                //
                                Message m = new Message(-23);
                                m.writer().writeByte(1);
                                m.writer().writeByte(questP.template.statusQuest);
                                Quest.write_Quest(m.writer(), questP);
                                p.addmsg(m);
                                m.cleanup();
                                m = new Message(-23);
                                m.writer().writeByte(5);
                                m.writer().writeShort(questP.template.index);
                                p.addmsg(m);
                                m.cleanup();
                            } else {
                                p.getService().send_box_ThongBao_OK("Nhiệm vụ hiện tại chưa hoàn thành");
                            }
                        }
                    }
                    break;
                }
                case 41: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        short[] id_op = new short[]{27, 16, 26, 4, 15, 1};
                        if (p.pointAttributeThongThao < 1) {
                            p.getService().send_box_ThongBao_OK("Không đủ 1 điểm thông thạo");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.pointAttributeThongThao < 1) {
                            p.getService().send_box_ThongBao_OK("Không đủ 1 điểm thông thạo");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }

                        Option op_add = null;
                        for (int i = 0; i < p.list_op_thongthao.size(); i++) {
                            if (p.list_op_thongthao.get(i).id == id_op[p.data_yesno[0]]) {
                                op_add = p.list_op_thongthao.get(i);
                                break;
                            }
                        }
                        if (op_add != null) {
                            int old_value = op_add.getParam();
                            if (old_value >= 40) {
                                p.getService().send_box_ThongBao_OK("Không thể cộng quá 40 điểm");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            op_add.setParam(old_value + 1);

                        } else {
                            p.list_op_thongthao.add(new Option(id_op[p.data_yesno[0]], 1));
                        }
                        p.pointAttributeThongThao--;
                        p.update_info_to_all();
                        Max_Level.show_table(p);
                        p.getService().send_box_ThongBao_OK("Tăng thành công");
                    }
                    break;
                }
                case 40: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(p.data_yesno[0]);
                        if (itemTemplate4 != null) {
                            if (itemTemplate4.type == 74) {
                                if (p.item.total_item_bag_by_id(4, itemTemplate4.id) < 10) {
                                    p.getService().send_box_ThongBao_OK("Không đủ 10 " + itemTemplate4.name);
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                if (p.item.able_bag() < 1) {
                                    p.getService().send_box_ThongBao_OK("Hành trang phải chừa ít nhất 1 ô trống");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }// vuong code số lượng mảnh đỏ
                                if ((itemTemplate4.id >= 692 && itemTemplate4.id <= 694) || (itemTemplate4.id >= 705 && itemTemplate4.id <= 725)) {
                                    if (true) {
                                        p.getService().send_box_ThongBao_OK("Tính năng ghép trang bị đỏ tạm thời bị vô hiệu hóa!");
                                        p.data_yesno = null;
                                        p.map_tele = null;
                                        return;
                                    }
                                }
                                p.item.remove_item47(4, itemTemplate4.id, 10);
                                Item_wear it_add = null;
                                int id_b1 = 0;
                                int id_b2 = 0;
                                int color = 0;
                                int clazz = p.clazz;
                                short[] type_equip = new short[]{0};
                                switch (itemTemplate4.id) {
                                    case 304: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 0;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 305: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 0;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 306: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 0;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 307: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 1;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 308: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 1;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 309: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 1;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 310: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 2;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 311: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 2;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 312: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 2;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 313: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 3;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 314: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 3;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 315: {
                                        id_b1 = 1728;
                                        id_b2 = 1919;
                                        color = 3;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 536: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 0;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 537: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 0;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 538: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 0;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 539: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 1;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 540: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 1;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 541: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 1;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 542: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 2;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 543: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 2;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 544: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 2;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 545: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 3;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 546: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 3;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 547: {
                                        id_b1 = 1920;
                                        id_b2 = 2111;
                                        color = 3;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    case 692: {
                                        id_b1 = 1920;
                                        id_b2 = 2165;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 693: {
                                        id_b1 = 1920;
                                        id_b2 = 2165;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 694: {
                                        id_b1 = 1920;
                                        id_b2 = 2165;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 725: {
                                        id_b1 = 2166;
                                        id_b2 = 2213;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 724: {
                                        id_b1 = 2166;
                                        id_b2 = 2213;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 723: {
                                        id_b1 = 2166;
                                        id_b2 = 2213;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 722: {
                                        id_b1 = 2214;
                                        id_b2 = 2261;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 721: {
                                        id_b1 = 2214;
                                        id_b2 = 2261;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 720: {
                                        id_b1 = 2214;
                                        id_b2 = 2261;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 719: {
                                        id_b1 = 2262;
                                        id_b2 = 2309;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 718: {
                                        id_b1 = 2262;
                                        id_b2 = 2309;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 717: {
                                        id_b1 = 2262;
                                        id_b2 = 2309;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 716: {
                                        id_b1 = 2310;
                                        id_b2 = 2357;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 715: {
                                        id_b1 = 2310;
                                        id_b2 = 2357;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 714: {
                                        id_b1 = 2310;
                                        id_b2 = 2357;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 713: {
                                        id_b1 = 2358;
                                        id_b2 = 2405;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 712: {
                                        id_b1 = 2358;
                                        id_b2 = 2405;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 711: {
                                        id_b1 = 2358;
                                        id_b2 = 2405;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 710: {
                                        id_b1 = 2406;
                                        id_b2 = 2453;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 709: {
                                        id_b1 = 2406;
                                        id_b2 = 2453;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 708: {
                                        id_b1 = 2406;
                                        id_b2 = 2453;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                    case 707: {
                                        id_b1 = 2454;
                                        id_b2 = 2501;
                                        color = 8;
                                        type_equip = new short[]{1, 3, 5};
                                        break;
                                    }
                                    case 706: {
                                        id_b1 = 2454;
                                        id_b2 = 2501;
                                        color = 8;
                                        type_equip = new short[]{2, 4};
                                        clazz = 0;
                                        break;
                                    }
                                    case 705: {
                                        id_b1 = 2454;
                                        id_b2 = 2501;
                                        color = 8;
                                        type_equip = new short[]{0};
                                        break;
                                    }
                                    //
                                }
                                List<ItemTemplate3> list_random = new ArrayList<>();
                                for (int i = 0; i < ItemTemplate3.ENTRYS.size(); i++) {
                                    ItemTemplate3 it_temp = ItemTemplate3.ENTRYS.get(i);
                                    if (it_temp.id >= id_b1 && it_temp.id <= id_b2
                                            && it_temp.color == color && it_temp.clazz == clazz) {
                                        for (int j = 0; j < type_equip.length; j++) {
                                            if (type_equip[j] == it_temp.typeEquip) {
                                                list_random.add(it_temp);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (list_random.size() > 0) {
                                    it_add = new Item_wear();
                                    it_add.setup_template_by_id(list_random.get(ZUtil.random(list_random.size())));
                                }
//                                if (it_add.template != null) {// vuong code tỉ lệ ghép mảnh đỏ
////                                    int numLoKham = (50 > Util.random(120)) ? 0
////                                            : ((70 > Util.random(120)) ? 1 : 2);
//                                    int numLoKham = (85 > Util.random(100)) ? 0
//                                            : ((95 > Util.random(100)) ? 1 : 2);
//                                    it_add.numLoKham = (byte) numLoKham;
//                                    p.item.add_item_bag3(it_add);
//                                }
                                if (it_add.template != null) {
                                    int numLoKham = (50 > ZUtil.random(120)) ? 0
                                            : ((70 > ZUtil.random(120)) ? 1 : 2);
                                    it_add.numLoKham = (byte) numLoKham;
                                    p.item.add_item_bag3(it_add);
                                }
                                p.item.updateInventory(false);
                                if (it_add != null) {
                                    Message m = new Message(-67);
                                    m.writer().writeByte(31);
                                    m.writer().writeUTF(
                                            "Bạn ghép thành công được " + it_add.template.name);
                                    m.writer().writeShort(it_add.index);
                                    p.addmsg(m);
                                    m.cleanup();
                                }
                            }
                        }
                    }
                    break;
                }
                case 39:
                case 38: { // no use
                    break;
                }
                case 37: {
                    // so
                    // 88, 318, 90, 34, 91
                    //
                    // trung
                    // 32,33,92,93,219,220,316,317
                    //
                    // cao
                    // 240, 160, 161, 427
                    if (p.item.total_item_bag_by_id(4, 87) > 0) {
                        int index = ZUtil.random(1000);
                        if (index < 50) { // 316
                            String[] name_ = new String[]{"Giáp sáp", "Đao không kích", "Lao sáp"};
                            int[] icon_ = new int[]{79, 77, 78};
                            p.getService().NewDialog_eat_taq(name_, icon_, 316);
                            p.get_skill_taq_new(316);
                        } else if (index < 130) { // 32
                            String[] name_
                                    = new String[]{"Sức mạnh của lửa", "Hỏa quyền", "Nắm đấm lửa"};
                            int[] icon_ = new int[]{32, 30, 29};
                            p.getService().NewDialog_eat_taq(name_, icon_, 32);
                            p.get_skill_taq_new(32);
                        } else if (index < 210) { // 93
                            String[] name_ = new String[]{"Cát lưu động", "Bão cát sa mạc",
                                "Cát linh động"};
                            int[] icon_ = new int[]{55, 54, 52};
                            p.getService().NewDialog_eat_taq(name_, icon_, 93);
                            p.get_skill_taq_new(93);
                        } else if (index < 330) { // 317
                            String[] name_
                                    = new String[]{"Thân thể thép", "Ảo ảnh trảm", "Loạn trảm"};
                            int[] icon_ = new int[]{82, 81, 80};
                            p.getService().NewDialog_eat_taq(name_, icon_, 317);
                            p.get_skill_taq_new(317);
                        } else if (index < 450) { // 92
                            String[] name_
                                    = new String[]{"Sức mạnh của lửa", "Hỏa quyền", "Nắm đấm lửa"};
                            int[] icon_ = new int[]{57, 56, 53};
                            p.getService().NewDialog_eat_taq(name_, icon_, 92);
                            p.get_skill_taq_new(92);
                        } else if (index < 580) { // 219
                            String[] name_
                                    = new String[]{"Sóng âm - Xung kích", "Hóa báo đốm", "Tia chớp"};
                            int[] icon_ = new int[]{72, 71, 70};
                            p.getService().NewDialog_eat_taq(name_, icon_, 219);
                            p.get_skill_taq_new(219);
                        } else if (index < 710) { // 220
                            String[] name_ = new String[]{"Cơn lốc - Ưng kích", "Hóa chim ưng",
                                "Chim săn mồi"};
                            int[] icon_ = new int[]{69, 68, 67};
                            p.getService().NewDialog_eat_taq(name_, icon_, 220);
                            p.get_skill_taq_new(220);
                        } else { // 33
                            String[] name_ = new String[]{"Sức sống bất diệt", "Chất bất ổn",
                                "Súng máy caosu"};
                            int[] icon_ = new int[]{34, 33, 31};
                            p.getService().NewDialog_eat_taq(name_, icon_, 33);
                            p.get_skill_taq_new(33);
                        }
                        p.item.remove_item47(4, 87, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 36: {
                    if (p.get_ngoc() < 15) {
                        p.getService().send_box_ThongBao_OK("Không đủ 15 ruby");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (!p.item.can_add_item_bag47(4, 232, 1)) {
                        p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_ngoc(-15);
                    p.updateMoney();
                    p.item.add_item_bag47(4, 232, 1);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Mua 1 Vé vòng xoay may mắn thành công");
                    break;
                }
                case 35: {
                    if (p.item.total_item_bag_by_id(4, 86) > 0) {
                        int index = ZUtil.random(1000);
                        if (index < 50) { // 88
                            String[] name_
                                    = new String[]{"Khói bất tử", "Khói tốc độ", "Mưa khói"};
                            int[] icon_ = new int[]{38, 39, 40};
                            p.getService().NewDialog_eat_taq(name_, icon_, 88);
                            p.get_skill_taq_new(88);
                        } else if (index < 150) { // 318
                            String[] name_
                                    = new String[]{"Thần hộ thể", "Tăng trọng", "Sức nặng ngàn cân"};
                            int[] icon_ = new int[]{85, 84, 83};
                            p.getService().NewDialog_eat_taq(name_, icon_, 318);
                            p.get_skill_taq_new(318);
                        } else if (index < 350) { // 90
                            String[] name_
                                    = new String[]{"Sức mạnh của lửa", "Hỏa quyền", "Nắm đấm lửa"};
                            int[] icon_ = new int[]{48, 47, 46};
                            p.getService().NewDialog_eat_taq(name_, icon_, 90);
                            p.get_skill_taq_new(90);
                        } else if (index < 650) { // 34
                            String[] name_
                                    = new String[]{"Tiến hóa", "Thuốc tăng trưởng", "Hóa tuần lộc"};
                            int[] icon_ = new int[]{37, 36, 35};
                            p.getService().NewDialog_eat_taq(name_, icon_, 34);
                            p.get_skill_taq_new(34);
                        } else { // 91
                            String[] name_ = new String[]{"Cát lưu động", "Bão cát sa mạc",
                                "Nét vẽ sức mạnh"};
                            int[] icon_ = new int[]{51, 50, 49};
                            p.getService().NewDialog_eat_taq(name_, icon_, 91);
                            p.get_skill_taq_new(91);
                        }
                        p.item.remove_item47(4, 86, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 34: {
                    if (p.item.total_item_bag_by_id(7, 9) < 10) {
                        p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 29) < 1) {
                        p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate4.get_it_by_id(29).name);
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.get_vang() < 50_000) {
                        p.getService().send_box_ThongBao_OK("Không đủ 50.000 beri");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (!p.item.can_add_item_bag47(4, 158, 1)) {
                        p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_vang(-50_000);
                    p.updateMoney();
                    p.item.remove_item47(7, 9, 10);
                    //
                    boolean suc = 5 > ZUtil.random(150);
                    if (suc) {
                        p.item.remove_item47(4, 29, 1);
                        p.item.add_item_bag47(4, 158, 1);
                    }
                    p.item.updateInventory(false);
                    //
                    p.getService().sendUpgradeDevilActionMsg((byte) 17, (byte) (suc ? 1 : 3), "Cường hóa kỹ năng " + (suc ? "thành công" : "thất bại"));
                    break;
                }
                case 33: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        if (model.ThanTrangConfig.isThanTrangSkill(p.data_yesno[0])) {
                            p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        Skill_info sk_temp = p.get_skill_temp(p.data_yesno[0]);
                        if (sk_temp != null) {
                            if (sk_temp.temp != null && model.ThanTrangConfig.isThanTrangSkill(sk_temp.temp.ID)) {
                                p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (sk_temp.lvdevil > 4) {
                                p.getService().send_box_ThongBao_OK(sk_temp.temp.name + " đã được cường hóa tối đa!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            int percent = (sk_temp.lvdevil == 0) ? 10 //
                                    : ((sk_temp.lvdevil == 1) ? 8 //
                                            : ((sk_temp.lvdevil == 2) ? 6 //
                                                    : ((sk_temp.lvdevil == 3) ? 5 : 4)));
                            if (p.get_vang() < 50_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ 50.000 beri");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_vang(-50_000);
                            p.updateMoney();
                            p.item.remove_item47(7, 9, 10);
                            p.item.updateInventory(false);
                            //
                            boolean suc = 50 > ZUtil.random(120);
                            if (suc) {
                                sk_temp.devilpercent += percent;
                                if (sk_temp.devilpercent >= 100) {
                                    sk_temp.devilpercent = 0;
                                    sk_temp.lvdevil++;
                                }
                                p.send_skill();
                                p.setAbility();
                                p.update_info_to_all();
                                if (p instanceof model.DeTu) {
                                    ((model.DeTu) p).saveDeTu(false);
                                }
                            }
                            //
                            p.getService().sendUpgradeDevilActionMsg((byte) 12, (byte) (suc ? 1 : 3), "Cường hóa kỹ năng " + (suc ? "thành công" : "thất bại"));
                        }
                    }
                    break;
                }
                case 32: {
                    if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc_id_ngoc >= 221
                            && p.item_to_kham_ngoc_id_ngoc <= 226) {
                        Item_wear it_select = p.item_to_kham_ngoc;
                        if ((it_select.getColor() >= 2) && it_select.valueChetac >= 50) {
                            if (p.item.total_item_bag_by_id(4, p.item_to_kham_ngoc_id_ngoc) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ "
                                        + ItemTemplate4.get_item_name(p.item_to_kham_ngoc_id_ngoc));
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.get_ngoc() < 5) {
                                p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.item.remove_item47(4, p.item_to_kham_ngoc_id_ngoc, 1);
                            p.update_ngoc(-5);
                            p.updateMoney();
                            int kichAnChance = 0;
                            if (p.item_to_kham_ngoc_id_ngoc == 221) kichAnChance = 4;
                            else if (p.item_to_kham_ngoc_id_ngoc == 222) kichAnChance = 8;
                            else if (p.item_to_kham_ngoc_id_ngoc == 223) kichAnChance = 13;
                            else if (p.item_to_kham_ngoc_id_ngoc == 224) kichAnChance = 18;
                            else if (p.item_to_kham_ngoc_id_ngoc == 225) kichAnChance = 22;
                            else if (p.item_to_kham_ngoc_id_ngoc == 226) kichAnChance = 30;

                            boolean suc = kichAnChance > ZUtil.random(120);
                            if (suc) {
                                it_select.valueKichAn = ZUtil.rollKichAn();
                                if (it_select.valueKichAn == 12) {
                                    it_select.typelock = -1;
                                }
                            } else {
                                int reduce_chetac = switch (p.item_to_kham_ngoc_id_ngoc) {
                                    case 226 -> ZUtil.random(1, 6);
                                    case 225 -> ZUtil.random(14, 23);
                                    case 224 -> ZUtil.random(16, 25);
                                    case 223 -> ZUtil.random(18, 27);
                                    case 222 -> ZUtil.random(20, 29);
                                    default  -> ZUtil.random(22, 31);
                                };
                                int newChetac = it_select.valueChetac - reduce_chetac;
                                if (newChetac < 0) newChetac = 0;
                                it_select.valueChetac = (short) newChetac;
                            }
                            //
                            Message m = new Message(-67);
                            m.writer().writeByte(20);
                            m.writer().writeUTF(suc
                                    ? ("Chúc mừng bạn đã kích ẩn thành công " + it_select.template.name)
                                    : "Rất tiếc quá trình kích ẩn thất bại");
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        } else {
                            p.getService().send_box_ThongBao_OK("Trang bị đem đi kích ẩn phải là trang bị tím hoặc cam và có điểm chế tác > 50");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
                    }
                    break;
                }
                case 31: {
                    if (p.item_to_kham_ngoc != null && (p.item_to_kham_ngoc.template.typeEquip < 6
                            || p.item_to_kham_ngoc.template.typeEquip == 7)) {
                        if (p.get_ngoc() < 5) {
                            p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.update_ngoc(-5);
                        p.updateMoney();
                        p.item_to_kham_ngoc.valueChetac++;
                        if (p.item_to_kham_ngoc.valueChetac >= 100) {
                            p.item_to_kham_ngoc.valueChetac = 100;
                        }
                        //
                        Message m = new Message(-67);
                        m.writer().writeByte(25);
                        m.writer().writeUTF("Bạn phục hồi 1 điểm chế tác thành công");
                        p.addmsg(m);
                        m.cleanup();
                        //
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 30: {
                    if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc_id_ngoc >= 221
                            && p.item_to_kham_ngoc_id_ngoc <= 226) {
                        Item_wear it_select = p.item_to_kham_ngoc;
                        if (it_select.isHoanMy == 1) {
                            p.getService().send_box_ThongBao_OK("Trang bị này đã được hoàn mỹ rồi!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if ((it_select.getColor() >= 2) && it_select.valueChetac >= 50) {
                            if (p.item.total_item_bag_by_id(4, p.item_to_kham_ngoc_id_ngoc) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ "
                                        + ItemTemplate4.get_item_name(p.item_to_kham_ngoc_id_ngoc));
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.get_ngoc() < 5) {
                                p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.item.remove_item47(4, p.item_to_kham_ngoc_id_ngoc, 1);
                            p.update_ngoc(-5);
                            p.updateMoney();
                            int hoanMyChance = switch (p.item_to_kham_ngoc_id_ngoc) {
                                case 221 -> 3;
                                case 222 -> 6;
                                case 223 -> 10;
                                case 224 -> 15;
                                case 225 -> 20;
                                case 226 -> 25;
                                default  -> 0;
                            };
                            boolean suc = hoanMyChance > ZUtil.random(150);
                            if (suc) {
                                it_select.isHoanMy = 1;
                            } else {
                                int reduce_chetac;
                                switch (p.item_to_kham_ngoc_id_ngoc) {
                                    case 226: {
                                        reduce_chetac = ZUtil.random(1, 6);
                                        break;
                                    }
                                    case 225: {
                                        reduce_chetac = ZUtil.random(14, 23);
                                        break;
                                    }
                                    case 224: {
                                        reduce_chetac = ZUtil.random(16, 25);
                                        break;
                                    }
                                    case 223: {
                                        reduce_chetac = ZUtil.random(18, 27);
                                        break;
                                    }
                                    case 222: {
                                        reduce_chetac = ZUtil.random(20, 29);
                                        break;
                                    }
                                    default: {
                                        reduce_chetac = ZUtil.random(22, 31);
                                        break;
                                    }
                                }
                                it_select.valueChetac -= reduce_chetac;
                                if (it_select.valueChetac <= 0) {
                                    it_select.valueChetac = 0;
                                }
                            }
                            //
                            Message m = new Message(-67);
                            m.writer().writeByte(20);
                            m.writer()
                                    .writeUTF(suc
                                            ? ("Chúc mừng bạn đã kích ẩn thành công "
                                            + it_select.template.name)
                                            : "Rất tiếc quá trình hoàn mỹ thất bại");
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        } else {
                            p.getService().send_box_ThongBao_OK("Trang bị đem đi hoàn mỹ phải là trang bị tím hoặc cam và có điểm chế tác > 50");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
                    }
                    break;
                }
                case 28: {
                    if (p.item.it_heart != null && p.item.it_heart.levelUp < 110) {
                        long vang_req = 1_200_000 + p.item.it_heart.levelUp * 200_000L;
                        if (p.get_vang() < vang_req) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + ZUtil.number_format(vang_req) + " beri");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        int ruby_req = 0;
                        if ((p.item.it_heart.levelUp % 5) == 4) {
                            ruby_req = ((p.item.it_heart.levelUp / 5) + 1) * 10;
                            if (p.item.it_heart.levelUp == 104) {
                                ruby_req = 400;
                            } else if (p.item.it_heart.levelUp == 109) {
                                ruby_req = 600;
                            }
                        }
                        if (p.get_ngoc() < ruby_req) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + ruby_req + " ruby");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.update_vang(-vang_req);
                        p.update_ngoc(-ruby_req);
                        p.updateMoney();
                        boolean suc = (530 - (p.item.it_heart.levelUp * 5)) > ZUtil.random(1200);
                        if (suc || p.item.it_heart.valueChetac == 100) {
                            p.item.it_heart.levelUp++;
                            p.item.it_heart.valueChetac = 0;
                            if (p.item.it_heart.levelUp == 100) {
                                boolean has79 = false;
                                for (Option op : p.item.it_heart.option_item) {
                                    if (op != null && op.id == 79) {
                                        has79 = true;
                                        break;
                                    }
                                }
                                if (!has79) {
                                    p.item.it_heart.option_item.add(0, new Option(79, 2));
                                }
                            }
                        } else {
                            if (p.item.it_heart.levelUp >= 100) {
                                p.item.it_heart.valueChetac += 5;
                            } else {
                                p.item.it_heart.valueChetac += 10;
                            }
                        }
                        if (p.item != null && p.item.it_heart != null) {
                            if (p.item.it_body != null && p.item.it_body.length > 6) {
                                p.item.it_body[6] = p.item.it_heart;
                            }
                        }
                        p.setAbility();
                        UpgradeItem.send_heart_info(p, false);
                        p.update_info_to_all();
                        p.flush(p, false);
                        //
                        Message m5 = new Message(-48);
                        m5.writer().writeByte(suc ? 16 : 17); // 16 ok, 17 fail
                        m5.writer().writeUTF(
                                "Cường hóa kỹ năng " + (suc ? "thành công" : "thất bại"));
                        p.addmsg(m5);
                        m5.cleanup();
                    }
                    break;
                }
                case 27: {
                    if (p.item.it_heart == null) {
                        if (p.item.total_item_bag_by_id(4, 221) < ((p.level < 40) ? 120 : 100)) {
                            p.getService().send_box_ThongBao_OK("Không đủ "
                                    + ((p.level < 40) ? 120 : 100) + " Đá Hải Thạch cấp 1");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        short[] id_check = new short[]{4, 9};
                        for (int i = 0; i < id_check.length; i++) {
                            if (p.item.total_item_bag_by_id(7,
                                    id_check[i]) < ((p.level < 40) ? 120 : 100)) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ((p.level < 40) ? 120 : 100) + " "
                                        + ItemTemplate7.get_item_name(id_check[i]));
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                        }
                        if (p.get_vang() < ((p.level < 40) ? 12_000_000 : 10_000_000)) {
                            p.getService().send_box_ThongBao_OK("Không đủ 10.000.000 beri");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.item.remove_item47(4, 221, ((p.level < 40) ? 120 : 100));
                        for (int i = 0; i < id_check.length; i++) {
                            p.item.remove_item47(7, id_check[i], ((p.level < 40) ? 120 : 100));
                        }
                        p.update_vang(-((p.level < 40) ? 12_000_000 : 10_000_000));
                        p.updateMoney();
                        p.item.updateInventory(false);
                        //
                        p.item.it_heart = new Item_wear();
                        p.item.it_heart.setup_template_by_id(11_000);
                        p.item.it_heart.valueChetac = 0;
                        p.item.it_heart.typelock = 1;
                        boolean has56 = false;
                        for (Option op : p.item.it_heart.option_item) {
                            if (op != null && op.id == 56) {
                                has56 = true;
                                break;
                            }
                        }
                        if (!has56) {
                            p.item.it_heart.option_item.add(new Option(56, 100));
                        }
                        //
                        p.item.it_heart.index = 6;
                        p.item.it_heart.typelock = 1;
                        p.item.it_body[6] = p.item.it_heart;
                        p.setAbility();
                        p.update_info_to_all();
                        UpgradeItem.send_heart_info(p, false);
                        UpgradeItem.show_eff_get_heart(p);
                        p.flush(p, false);
                    }
                    break;
                }
                case 26: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        PotionMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item47) {
                                for (int j = 0; j < market.item47.size(); j++) {
                                    if (market.item47.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item47.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id != p.IDPlayer) {
                            if (p.get_vnd() < it_receive.price_market) {
                                if (it_receive.category == 4) {
                                    if (it_receive.id == 0) {
                                        p.getService().send_box_ThongBao_OK("Bạn không đủ "
                                                + ZUtil.number_format(it_receive.price_market)
                                                + " extol để mua " + it_receive.quant + " triệu beri");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Bạn không đủ "
                                                + ZUtil.number_format(it_receive.price_market)
                                                + " extol để mua " + it_receive.quant + " "
                                                + ItemTemplate4.get_item_name(it_receive.id));
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Bạn không đủ "
                                            + ZUtil.number_format(it_receive.price_market)
                                            + " extol để mua " + it_receive.quant + " "
                                            + ItemTemplate7.get_item_name(it_receive.id));
                                }
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            synchronized (it_receive) {
                                if (it_receive.type_market == 1 && it_receive.time_market > System.currentTimeMillis()) {
                                    synchronized (p.item) {
                                        if (p.item.can_add_item_bag47(it_receive.category, it_receive.id, it_receive.quant)) {
                                            if (it_receive.price_market < 0) {
                                                zLog.gI().add_log("BUG", "Bug ne 666: " + it_receive.price_market);
                                                p.data_yesno = null;
                                                p.map_tele = null;
                                                return;
                                            }
                                            it_receive.time_market = 0;
                                            it_receive.type_market = 2;
                                            it_receive.buyer_id = p.IDPlayer;
                                            Market.updatePotionStatus(it_receive);
                                            p.updateVnd(-it_receive.price_market);
                                            if (!(it_receive.category == 4 && it_receive.id == 0)) {
                                                p.updateMoney();
                                            }
                                            if (it_receive.category == 4) {
                                                if (it_receive.id == 0) { // beri
                                                    long beri_add = 1_000_000L * it_receive.quant;
                                                    p.update_vang(beri_add);
                                                    p.updateMoney();
                                                    p.getService().send_box_ThongBao_OK("Mua thành công " + it_receive.quant + " "
                                                            + " triệu beri với giá "
                                                            + ZUtil.number_format(
                                                                    it_receive.price_market)
                                                            + " extol");
                                                    zLog.gI().add_log(p, "Mua thành công " + beri_add + " beri trên market mat " + it_receive.price_market + " extol, seller_id=" + it_receive.seller_id);
                                                } 
                                                else {
                                                    p.item.add_item_bag47(it_receive.category, it_receive.id,
                                                            it_receive.quant);
                                                    p.getService().send_box_ThongBao_OK("Mua thành công " + it_receive.quant + " "
                                                            + ItemTemplate4.get_item_name(it_receive.id)
                                                            + " với giá "
                                                            + ZUtil.number_format(
                                                                    it_receive.price_market)
                                                            + " extol");
                                                    zLog.gI().add_log(p, "mua " + it_receive.quant + " " + ItemTemplate4.get_item_name(it_receive.id) + " trên market mat " + it_receive.price_market + ", seller_id=" + it_receive.seller_id);
                                                }
                                            } else {
                                                p.item.add_item_bag47(it_receive.category, it_receive.id,
                                                        it_receive.quant);
                                                p.getService().send_box_ThongBao_OK("Mua thành công " + it_receive.quant + " "
                                                        + ItemTemplate7.get_item_name(it_receive.id)
                                                        + " với giá "
                                                        + ZUtil.number_format(it_receive.price_market)
                                                        + " extol");
                                                zLog.gI().add_log(p, "mua " + it_receive.quant + " " + ItemTemplate7.get_item_name(it_receive.id) + " trên market mat " + it_receive.price_market + ", seller_id=" + it_receive.seller_id);
                                            }
                                            p.item.updateInventory(false);
                                            Market.update_at_market_index(p, market.type);
                                            Market.update_at_market_index(p, 3);
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ");
                                        }
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Vật phẩm đã được bán hoặc hết hạn");
                                }
                            }
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                    }
                    break;
                }
                case 25: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        ItemMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item3) {
                                for (int j = 0; j < market.item3.size(); j++) {
                                    if (market.item3.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item3.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id != p.IDPlayer) {
                            if (p.get_vnd() < it_receive.price_market) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ "
                                        + ZUtil.number_format(it_receive.price_market)
                                        + " extol để mua " + it_receive.template.name);
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            synchronized (it_receive) {
                                if (it_receive.type_market == 1 && it_receive.time_market > System.currentTimeMillis()) {
                                    synchronized (p.item) {
                                        if (p.item.able_bag() > 0) {
                                            if (it_receive.price_market < 0) {
                                                zLog.gI().add_log("BUG", "Bug ne 667: " + it_receive.price_market);
                                                p.data_yesno = null;
                                                p.map_tele = null;
                                                return;
                                            }
                                            Item_wear it_add = new Item_wear();
                                            it_add.clone_obj(it_receive);
                                            if (it_add.template != null) {
                                                it_receive.time_market = 0;
                                                it_receive.type_market = 2;
                                                it_receive.buyer_id = p.IDPlayer;
                                                Market.updateItemStatus(it_receive);
                                                p.updateVnd(-it_receive.price_market);
                                                p.updateMoney();
                                                //
                                                p.item.add_item_bag3(it_add);
                                                p.item.updateInventory(false);
                                                p.getService().send_box_ThongBao_OK("Mua thành công " + it_receive.template.name + " với giá "
                                                        + ZUtil.number_format(it_receive.price_market)
                                                        + " extol");
                                                zLog.gI().add_log(p, "Mua thành công " + it_receive.template.name + " với giá " + it_receive.price_market + " extol, seller_id=" + it_receive.seller_id);
                                                Market.update_at_market_index(p, market.type);
                                                Market.update_at_market_index(p, 3);
                                            }
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ");
                                        }
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Vật phẩm đã được bán hoặc hết hạn");
                                }
                            }
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                    }
                    break;
                }
                case 24: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        PotionMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item47) {
                                for (int j = 0; j < market.item47.size(); j++) {
                                    if (market.item47.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item47.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id == p.IDPlayer) {
                            synchronized (it_receive) {
                                if (it_receive.type_market == 1 && it_receive.time_market > System.currentTimeMillis()) {
                                    it_receive.time_market = 0;
                                    it_receive.type_market = 3;
                                    Market.updatePotionStatus(it_receive);
                                    Market.update_at_market_index(p, market.type);
                                    Market.update_at_market_index(p, 3);
                                    if (it_receive.category == 4) {
                                        if (it_receive.id == 0) {
                                            p.getService().send_box_ThongBao_OK("Hủy bán " + it_receive.quant
                                                    + " triệu beri thành công");
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Hủy bán " + it_receive.quant + " "
                                                    + ItemTemplate4.get_item_name(it_receive.id)
                                                    + " thành công");
                                        }
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Hủy bán " + it_receive.quant + " "
                                                + ItemTemplate7.get_item_name(it_receive.id)
                                                + " thành công");
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 23: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        ItemMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item3) {
                                for (int j = 0; j < market.item3.size(); j++) {
                                    if (market.item3.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item3.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id == p.IDPlayer) {
                            if (p.get_vnd() < 1_500) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1.500 extol để đăng bán vật phẩm");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            synchronized (it_receive) {
                                if (it_receive.type_market == 3 && it_receive.time_market < System.currentTimeMillis()) {
                                    p.updateVnd(-1_500);
                                    p.updateMoney();
                                    it_receive.time_market = System.currentTimeMillis() + 60_000L * 60 * 24;
                                    it_receive.type_market = 1;
                                    Market.updateItemStatus(it_receive);
                                    p.getService().send_box_ThongBao_OK("Gia hạn thêm 24h cho "
                                             + it_receive.template.name + " thành công");
                                    Market.update_at_market_index(p, market.type);
                                    Market.update_at_market_index(p, 3);
                                }
                            }
                        }
                    }
                    break;
                }
                case 22: {
                    if (p.data_yesno != null && p.data_yesno.length == 4) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.get_vnd() < 200) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ 200 extol để đăng bán vật phẩm");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        Market getMarket = null;
                        if (p.data_yesno[0] == 4) {
                            if (p.item.total_item_bag_by_id(p.data_yesno[0],
                                    p.data_yesno[1]) < p.data_yesno[2]) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ " + p.data_yesno[2] + " "
                                        + ItemTemplate4.get_item_name(p.data_yesno[1])
                                        + " để đăng bán");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (Market.check_it_47_cant_sell(4, p.data_yesno[1])) {
                                p.getService().send_box_ThongBao_OK("Không thể đăng bán vật phẩm này");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            getMarket = Market.get_list_by_type(6);
                        } else if (p.data_yesno[0] == 7) {
                            if (p.item.total_item_bag_by_id(p.data_yesno[0],
                                    p.data_yesno[1]) < p.data_yesno[2]) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ " + p.data_yesno[2] + " "
                                        + ItemTemplate7.get_item_name(p.data_yesno[1])
                                        + " để đăng bán");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (Market.check_it_47_cant_sell(7, p.data_yesno[1])) {
                                p.getService().send_box_ThongBao_OK("Không thể đăng bán vật phẩm này");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            getMarket = Market.get_list_by_type(5);
                        }
                        if (getMarket != null) {
                            PotionMarket it_add = new PotionMarket();
                            it_add.index = Market.get_index();
                            it_add.id = (short) p.data_yesno[1];
                            it_add.category = (byte) p.data_yesno[0];
                            it_add.quant = (short) p.data_yesno[2];
                            it_add.time_market = System.currentTimeMillis() + 60_000L * 60 * 24;
                            it_add.price_market = p.data_yesno[3];
                            it_add.seller_id = p.IDPlayer;
                            it_add.buyer_id = -1;
                            it_add.market_type = (byte) getMarket.type;
                            it_add.type_market = 1;
                            if (it_add.index != -1) {
                                synchronized (p.item) {
                                    if (p.item.total_item_bag_by_id(p.data_yesno[0], p.data_yesno[1]) < p.data_yesno[2]) {
                                        p.getService().send_box_ThongBao_OK("Số lượng vật phẩm không đủ!");
                                        p.data_yesno = null;
                                        p.map_tele = null;
                                        return;
                                    }
                                    p.updateVnd(-200);
                                    p.updateMoney();
                                    p.item.remove_item47(p.data_yesno[0], p.data_yesno[1],
                                            p.data_yesno[2]);
                                    p.item.updateInventory(false);
                                    //
                                    Market.insertPotion(it_add);
                                    synchronized (getMarket.item47) {
                                        getMarket.item47.add(it_add);
                                    }
                                    Market.update_at_market_index(p, getMarket.type);
                                    Market.update_at_market_index(p, 3);
                                    if (p.data_yesno[0] == 4) {
                                        p.getService().send_box_ThongBao_OK(p.data_yesno[2] + " "
                                                + ItemTemplate4.get_item_name(p.data_yesno[1])
                                                + " đã được đăng bán với giá "
                                                + ZUtil.number_format(p.data_yesno[3])
                                                + " extol");
                                    } else if (p.data_yesno[0] == 7) {
                                        p.getService().send_box_ThongBao_OK(p.data_yesno[2] + " "
                                                + ItemTemplate7.get_item_name(p.data_yesno[1])
                                                + " đã được đăng bán với giá "
                                                + ZUtil.number_format(p.data_yesno[3])
                                                + " extol");
                                    }
                                }
                            } else {
                                p.getService().send_box_ThongBao_OK("Chợ mua bán có quá nhiều vật phẩm, không thể đăng thêm");
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra hãy thử lại");
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                    }
                    break;
                }
                case 21: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        PotionMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item47) {
                                for (int j = 0; j < market.item47.size(); j++) {
                                    if (market.item47.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item47.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id == p.IDPlayer
                                && it_receive.time_market < System.currentTimeMillis()) {
                            synchronized (it_receive) {
                                if (it_receive.type_market == 2) {
                                    long price_receive =  (long) it_receive.price_market * 90L / 100L;
                                    p.updateVnd(price_receive);
                                    p.updateMoney();
                                    if (it_receive.category == 4) {
                                        if (it_receive.id == 0) {
                                            p.getService().send_box_ThongBao_OK("Nhận " + price_receive + " extol (phí 10%) tiền bán "
                                                    + it_receive.quant + " triệu beri");
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Nhận " + price_receive + " extol (phí 10%) tiền bán "
                                                    + it_receive.quant + " "
                                                    + ItemTemplate4.get_item_name(it_receive.id));
                                        }
                                    } else if (it_receive.category == 7) {
                                        p.getService().send_box_ThongBao_OK("Nhận " + price_receive + " extol (phí 10%) tiền bán "
                                                + it_receive.quant + " "
                                                + ItemTemplate7.get_item_name(it_receive.id));
                                    }
                                    Market.deleteItem(it_receive.id_db);
                                    synchronized (market.item47) {
                                        market.item47.remove(it_receive);
                                    }
                                } else {
                                    if (it_receive.category == 4) {
                                        if (it_receive.id == 0) {
                                            long beri_add = 1_000_000L * it_receive.quant;
                                            p.update_vang(beri_add);
                                            p.updateMoney();
                                            p.getService().send_box_ThongBao_OK("Nhận " + it_receive.quant
                                                    + " " + " triệu beri về hành trang");
                                            Market.deleteItem(it_receive.id_db);
                                            synchronized (market.item47) {
                                                market.item47.remove(it_receive);
                                            }
                                        } else {
                                            if (p.item.add_item_bag47(4, it_receive.id,
                                                    it_receive.quant)) {
                                                p.item.updateInventory(false);
                                                p.getService().send_box_ThongBao_OK("Nhận " + it_receive.quant + " "
                                                        + ItemTemplate4
                                                                .get_item_name(it_receive.id)
                                                        + " về hành trang");
                                                Market.deleteItem(it_receive.id_db);
                                                synchronized (market.item47) {
                                                    market.item47.remove(it_receive);
                                                }
                                            } else {
                                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                                                p.data_yesno = null;
                                                p.map_tele = null;
                                                return;
                                            }
                                        }
                                    } else if (it_receive.category == 7) {
                                        if (p.item.add_item_bag47(7, it_receive.id, it_receive.quant)) {
                                            p.item.updateInventory(false);
                                            p.getService().send_box_ThongBao_OK("Nhận " + it_receive.quant + " "
                                                    + ItemTemplate7.get_item_name(it_receive.id)
                                                    + " về hành trang");
                                            Market.deleteItem(it_receive.id_db);
                                            synchronized (market.item47) {
                                                market.item47.remove(it_receive);
                                            }
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                                            p.data_yesno = null;
                                            p.map_tele = null;
                                            return;
                                        }
                                    }
                                }
                                Market.update_at_market_index(p, market.type);
                                Market.update_at_market_index(p, 3);
                            }
                        }
                    }
                    break;
                }
                case 20: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        ItemMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item3) {
                                for (int j = 0; j < market.item3.size(); j++) {
                                    if (market.item3.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item3.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id == p.IDPlayer) {
                            synchronized (it_receive) {
                                if (it_receive.type_market == 1 && it_receive.time_market > System.currentTimeMillis()) {
                                    it_receive.time_market = 0;
                                    it_receive.type_market = 3;
                                    Market.updateItemStatus(it_receive);
                                    Market.update_at_market_index(p, market.type);
                                    Market.update_at_market_index(p, 3);
                                    p.getService().send_box_ThongBao_OK("Hủy bán " + it_receive.template.name + " thành công");
                                }
                            }
                        }
                    }
                    break;
                }
                case 19: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        ItemMarket it_receive = null;
                        Market market = Market.get_list_by_type(p.data_yesno[0]);
                        if (market != null) {
                            synchronized (market.item3) {
                                for (int j = 0; j < market.item3.size(); j++) {
                                    if (market.item3.get(j).index == p.data_yesno[1]) {
                                        it_receive = market.item3.get(j);
                                        break;
                                    }
                                }
                            }
                        }
                        if (it_receive != null && it_receive.seller_id == p.IDPlayer
                                && it_receive.time_market < System.currentTimeMillis()) {
                            synchronized (it_receive) {
                                if (it_receive.type_market == 2) {
                                    long price_receive = (long) it_receive.price_market * 90L / 100L;
                                    p.updateVnd(price_receive);
                                    p.updateMoney();
                                    p.getService().send_box_ThongBao_OK("Nhận " + price_receive
                                            + " extol (phí 10%) tiền bán " + it_receive.template.name);
                                    Market.deleteItem(it_receive.id_db);
                                    synchronized (market.item3) {
                                        market.item3.remove(it_receive);
                                    }
                                } else {
                                    if (p.item.able_bag() <= 0) {
                                        p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                                        p.data_yesno = null;
                                        p.map_tele = null;
                                        return;
                                    }
                                    Item_wear it_add = new Item_wear();
                                    it_add.clone_obj(it_receive);
                                    if (it_add.template != null) {
                                        p.item.add_item_bag3(it_add);
                                        p.item.updateInventory(false);
                                        Market.deleteItem(it_receive.id_db);
                                        synchronized (market.item3) {
                                            market.item3.remove(it_receive);
                                        }
                                    }
                                    p.getService().send_box_ThongBao_OK("Nhận " + it_receive.template.name + " về hành trang");
                                }
                                Market.update_at_market_index(p, 3);
                            }
                        }
                    }
                    break;
                }
                case 18: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.get_vnd() < 2_000) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ 2000 extol để đăng bán vật phẩm");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        long beri_add = 1_000_000L * p.data_yesno[0];
                        if (beri_add > 2_000_000_000 || beri_add < 1) {
                            p.getService().send_box_ThongBao_OK("Số beri phải lớn hơn 0 và nhỏ hơn 2tỷ!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.get_vang() < beri_add) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ "
                                    + ZUtil.number_format(beri_add) + " beri để đăng bán");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        Market getMarket = Market.get_list_by_type(6);
                        if (getMarket != null) {
                            PotionMarket it_add = new PotionMarket();
                            it_add.index = Market.get_index();
                            it_add.id = 0;
                            it_add.category = 4;
                            it_add.quant = (short) p.data_yesno[0];
                            it_add.time_market = System.currentTimeMillis() + 60_000L * 60 * 24;
                            it_add.price_market = p.data_yesno[1];
                            it_add.seller_id = p.IDPlayer;
                            it_add.buyer_id = -1;
                            it_add.market_type = 6;
                            it_add.type_market = 1;
                            if (it_add.index != -1) {
                                p.updateVnd(-2000);
                                p.update_vang(-beri_add);
                                p.updateMoney();
                                Market.insertPotion(it_add);
                                synchronized (getMarket.item47) {
                                    getMarket.item47.add(it_add);
                                }
                                Market.update_at_market_index(p, 3);
                                Market.update_at_market_index(p, 6);
                                p.getService().send_box_ThongBao_OK(p.data_yesno[0] + " triệu beri đã được đăng bán với giá "
                                        + ZUtil.number_format(p.data_yesno[1])
                                        + " extol");
                            } else {
                                p.getService().send_box_ThongBao_OK("Chợ mua bán có quá nhiều vật phẩm, không thể đăng thêm");
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra hãy thử lại");
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                    }
                    break;
                }
                case 17: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item == null || p.item.bag3 == null || p.data_yesno[0] < 0 || p.data_yesno[0] >= p.item.bag3.length) {
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        synchronized (p.item) {
                            Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                            if (it_select != null && !p.isBot && p.conn != null) {
                                bot.botplayer.BotPlayerManager.learnMarketPrice(it_select.template.id, p.data_yesno[1]);
                            }
                            if (it_select != null) {
                                if (p.get_vnd() < 2_000) {
                                    p.getService().send_box_ThongBao_OK("Bạn không đủ 2000 extol để đăng bán vật phẩm");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                if (it_select.typelock == 1) {
                                    p.getService().send_box_ThongBao_OK("Các trang bị sau khi mặc lên người sẽ bị Khoá, và bạn không thể đăng bán trang bị khoá");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                if (it_select.mdakham != null && it_select.mdakham.length > 0) {
                                    p.getService().send_box_ThongBao_OK("Các trang bị đã được Khảm đá thì không thể đăng bán");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                int type_market
                                        = (it_select.template.typeEquip == 0
                                        || it_select.template.typeEquip == 1
                                        || it_select.template.typeEquip == 7)
                                                ? 0
                                                : ((it_select.template.typeEquip == 3
                                                || it_select.template.typeEquip == 5)
                                                        ? 1
                                                        : 2);
                                Market getMarket = Market.get_list_by_type(type_market);
                                if (getMarket != null) {
                                    ItemMarket it_add = new ItemMarket();
                                    it_add.clone_from_item_wear(it_select);
                                    it_add.time_market = System.currentTimeMillis() + 60_000L * 60 * 24;
                                    it_add.price_market = p.data_yesno[1];
                                    it_add.seller_id = p.IDPlayer;
                                    it_add.buyer_id = -1;
                                    it_add.market_type = (byte) type_market;
                                    it_add.type_market = 1;
                                    if (it_add.index != -1) {
                                        p.updateVnd(-2000);
                                        p.updateMoney();
                                        //
                                        Market.insertItem(it_add);
                                        synchronized (getMarket.item3) {
                                            getMarket.item3.add(it_add);
                                        }
                                        p.item.remove_item_wear(it_select);
                                        p.item.updateInventory(false);
                                        Market.update_at_market_index(p, 3);
                                        Market.update_at_market_index(p, type_market);
                                        p.getService().send_box_ThongBao_OK(it_select.template.name + " đã được đăng bán với giá "
                                                + ZUtil.number_format(p.data_yesno[1])
                                                + " extol");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Chợ mua bán có quá nhiều vật phẩm, không thể đăng thêm");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra hãy thử lại");
                                }
                            }
                            p.data_yesno = null;
                            p.map_tele = null;
                        }
                    }
                    break;
                }
                case 16: {
                    if (p.map_boss_info != null) {
                        int bossMapId = p.map_boss_info.map.template.id;
                        int reqQuest = map.MapCanGoTo.getRequiredQuestForBossMap(bossMapId);
                        template.QuestP mq = p.getMainQuest();
                        int curQId = (mq != null && mq.template != null) ? mq.template.id : 0;
                        if (!core.Manager.gI().isTestMode() && curQId < reqQuest) {
                            p.map_boss_info = null;
                            p.getService().send_box_ThongBao_OK("Bạn chưa đạt cấp độ nhiệm vụ chính tuyến (" + reqQuest + ") để vào map Săn Boss!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        // Auto advance statusQuest = 0 to 1 for boss main quest
                        if (mq != null && mq.template != null && mq.template.statusQuest == 0
                                && map.MapCanGoTo.isDoingBossMainQuest(bossMapId, mq.template.id)) {
                            try {
                                model.Quest.remove_old_and_send_next(p, mq);
                                mq = p.getMainQuest();
                                curQId = (mq != null && mq.template != null) ? mq.template.id : 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        boolean isDoingQuest = map.MapCanGoTo.isDoingBossMainQuest(bossMapId, curQId);
                        if (!isDoingQuest) {
                            if (p.get_ticket() < 5) {
                                p.map_boss_info = null;
                                p.getService().send_box_ThongBao_OK("Không đủ 5 bánh mì");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ticket(-5);
                            p.updateMoney();
                        }
                        p.map.leave_map(p, 2);
                        // create map boss
                        Zone map_boss = new Zone();
                        map_boss.template = p.map_boss_info.map.template;
                        map_boss.zone_id = (byte) 0;
                        map_boss.list_mob = new int[0];
                        p.map_boss_info.mob = new ArrayList<>();
                        
                        int index_mob = -2;
                        if (p.map_boss_info.map.list_mob != null && p.map_boss_info.map.list_mob.length > 0) {
                            for (int i = 0; i < p.map_boss_info.map.list_mob.length; i++) {
                                Mob temp = p.map_boss_info.map.getMob(p.map_boss_info.map.list_mob[i]);
                                if (temp == null || temp.mtemplate == null) continue;
                                Mob mob_add = mob.MobFactory.createMob(temp.mtemplate, map_boss, temp.x, temp.y, index_mob--);
                                mob_add.hp_max
                                        = temp.mtemplate.hp_max + (int) p.ability.getPoint3Hp(p.level + 1);
                                mob_add.hp = mob_add.hp_max;
                                mob_add.level = p.level;
                                mob_add.isdie = false;
                                mob_add.id_target = -1;
                                mob_add.boss_inf = null;
                                map_boss.mobs.put(mob_add.index, mob_add);
                                p.map_boss_info.mob.add(mob_add);
                            }
                        }
                        if (p.map_boss_info.mob.isEmpty() && p.map_boss_info.map.mobs != null && !p.map_boss_info.map.mobs.isEmpty()) {
                            for (Mob temp : p.map_boss_info.map.mobs.values()) {
                                if (temp == null || temp.mtemplate == null) continue;
                                Mob mob_add = mob.MobFactory.createMob(temp.mtemplate, map_boss, temp.x, temp.y, index_mob--);
                                mob_add.hp_max = temp.mtemplate.hp_max + (int) p.ability.getPoint3Hp(p.level + 1);
                                mob_add.hp = mob_add.hp_max;
                                mob_add.level = p.level;
                                mob_add.isdie = false;
                                mob_add.id_target = -1;
                                mob_add.boss_inf = null;
                                map_boss.mobs.put(mob_add.index, mob_add);
                                p.map_boss_info.mob.add(mob_add);
                            }
                        }
                        if (p.map_boss_info.mob.isEmpty() && bossMapId == 5) {
                            template.MobTemplate mt4 = template.MobTemplate.get_mob_template(4);
                            if (mt4 != null) {
                                Mob mob_add = mob.MobFactory.createMob(mt4, map_boss, (short) 402, (short) 311, index_mob--);
                                mob_add.hp_max = mt4.hp_max + (int) p.ability.getPoint3Hp(p.level + 1);
                                mob_add.hp = mob_add.hp_max;
                                mob_add.level = p.level;
                                mob_add.isdie = false;
                                mob_add.id_target = -1;
                                mob_add.boss_inf = null;
                                map_boss.mobs.put(mob_add.index, mob_add);
                                p.map_boss_info.mob.add(mob_add);
                            }
                        }
                        MapBossInfo.add(p.map_boss_info);
                        //
                        p.map_boss_info.map = map_boss;
                        p.map = p.map_boss_info.map;
                        p.x = p.map_boss_info.x_new;
                        p.y = p.map_boss_info.y_new;
                        p.xold = p.x;
                        p.yold = p.y;
                        p.map.goto_map(p);
                        p.getService().update_PK(p, true);
                        p.getService().pet(p, true);
                        Quest.update_map_have_side_quest(p, true);
                        //
                        map_boss.setRunning(true);
                        map_boss.start_map();
                        Zone.add_map_plus(map_boss);
                    }
                    break;
                }
                case 15: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        QuestP temp = p.get_quest(p.data_yesno[0]);
                        if (temp == null || temp.template.equals(Quest.QUEST_FINISH)) {
                            p.getService().send_box_ThongBao_OK("Bạn đã hoàn thành hết nhiệm vụ hiện tại");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (temp != null) {
                            if (p.get_ticket() < 3) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 3 bánh mì để nhận");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ticket(-3);
                            p.updateMoney();
                            p.getService().CountDown_Ticket();
                            //
                            // remove quest now
                            Quest.remove_old_and_send_next(p, temp);
                            // send dialog quest new
                            Message m = new Message(-23);
                            m.writer().writeByte(5);
                            m.writer().writeShort(temp.template.index);
                            p.addmsg(m);
                            m.cleanup();
                        }
                    }
                    break;
                }
                case 14: {
                    if (p.isdie) {
                        if (p.time_can_hs < 1) {
                            p.time_can_hs = 7;
                        }
                        if (p.pointPk < 20) {
                            if (p.get_vang() < 500) {
                                p.getService().send_box_ThongBao_OK("Không đủ 500 beri!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_vang(-500);
                        } else {
                            int fee = p.pointPk / 4;
                            if (fee < 1) fee = 1;
                            if (p.get_ngoc() < fee) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + fee + " ruby!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-fee);
                        }
                        p.time_can_hs--;
                        p.updateMoney();
                        p.time_can_mob_atk = System.currentTimeMillis() + 1200L;
                        p.time_hs_little_garden = 0;
                        p.isdie = false;
                        p.hp = p.ability.get_hp_max(true);
                        p.mp = p.ability.get_mp_max(true);
                        if (p.getService() != null) {
                            p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                            p.getService().use_potion(0, p.hp);
                            p.getService().use_potion(1, p.mp);
                            p.getService().update_PK(p, true);
                        }
                        if (p.map != null) {
                            p.map.change_flag(p, p.type_pk);
                        }
                        p.sendRevive();
                    }
                    break;
                }
                case 13: {
                    if (p.data_yesno != null && p.data_yesno.length == 2) {
                        ItemTemplate4 it_temp1 = ItemTemplate4.get_it_by_id(p.data_yesno[0]);
                        ItemTemplate4 it_temp2 = ItemTemplate4.get_it_by_id(p.data_yesno[1]);
                        if (it_temp1 != null && it_temp2 != null) {
                            if (it_temp1.id == it_temp2.id) {
                                p.getService().send_box_ThongBao_OK("Đá siêu cấp và đá nguyên liệu phải khác nhau!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.able_bag() < 1) {
                                p.getService().send_box_ThongBao_OK("Hành trang phải chừa ít nhất 1 ô trống");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, it_temp1.id) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 " + ItemTemplate4.get_item_name(it_temp1.id));
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, it_temp2.id) < 2) {
                                p.getService().send_box_ThongBao_OK("Không đủ 2 " + ItemTemplate4.get_item_name(it_temp2.id));
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            boolean suc = (p.percent_da_sieu_cap == 100 ? 120
                                    : p.percent_da_sieu_cap) > ZUtil.random(120);
                            p.item.remove_item47(4, p.data_yesno[1], suc ? 2 : 1);
                            if (suc) {
                                p.item.remove_item47(4, p.data_yesno[0], 1);
                                if (p.data_yesno[0] == 367) { // ho phach
                                    switch (p.data_yesno[1]) {
                                        case 55: {
                                            p.item.add_item_bag47(4, 369, 1);
                                            break;
                                        }
                                        case 61: {
                                            p.item.add_item_bag47(4, 370, 1);
                                            break;
                                        }
                                        case 67: {
                                            p.item.add_item_bag47(4, 371, 1);
                                            break;
                                        }
                                        case 73: {
                                            p.item.add_item_bag47(4, 372, 1);
                                            break;
                                        }
                                        case 79: {
                                            p.item.add_item_bag47(4, 373, 1);
                                            break;
                                        }
                                        default: { // 49
                                            p.item.add_item_bag47(4, 368, 1);
                                            break;
                                        }
                                    }
                                } else {
                                    p.item.add_item_bag47(4, Rebuild_Item.get_id_ngoc_sieu_cap(
                                            p.data_yesno[0], p.data_yesno[1]), 1);
                                }
                                p.percent_da_sieu_cap = 35;
                                //
                                Message m = new Message(-67);
                                m.writer().writeByte(27);
                                m.writer().writeUTF("Chúc mừng bạn nâng cấp thành công ");
                                p.addmsg(m);
                                m.cleanup();
                            } else {
                                p.percent_da_sieu_cap += 5;
                                Message m = new Message(-67);
                                m.writer().writeByte(30);
                                m.writer().writeUTF("Quá trình nâng cấp thất bại!");
                                p.addmsg(m);
                                m.cleanup();
                            }
                            p.item.updateInventory(false);
                        }
                    }
                    break;
                }
                case 12: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                        if (it_select != null && it_select.numLoKham < 4
                                && it_select.template != null && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                            int ruby_req = (it_select.numHoleDaDuc >= 1 ? 200 : 50);
                            if (p.get_ngoc() < ruby_req) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ " + ruby_req + " Ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-ruby_req);
                            if (it_select.numHoleDaDuc < 0) {
                                it_select.numHoleDaDuc = 0;
                            }
                            it_select.numHoleDaDuc++;
                            it_select.numLoKham++;
                            p.updateMoney();
                            Message m = new Message(-67);
                            m.writer().writeByte(7);
                            m.writer().writeUTF("Đục lỗ thành công " + it_select.template.name);
                            p.addmsg(m);
                            m.cleanup();
                            p.item.updateInventory(false);
                        } else {
                            Rebuild_Item.show_table(p, 2);
                            p.getService().send_box_ThongBao_OK("Không thể đục thêm lỗ với vật phẩm này, hãy sử dụng búa đục lỗ để có thể tiếp tục");
                        }
                    }
                    break;
                }
//                case 11: {
//                    if (p.isTachTB && p.data_yesno != null && p.data_yesno.length == 1) {
//                        Item_wear it_select = p.item.bag3[p.data_yesno[0]];
//                        if (it_select != null) {
//                            byte id_7 = 2;// vuong code tách đồ thành bột
//                            if (it_select.template.color == 8) {
//                                id_7 = 3;
//                            }else if (it_select.template.color == 2) {
//                                id_7 = 2;
//                            }
//                            else if (it_select.template.color == 3) {
//                                id_7 = 4;
//                            }
//                            //
//                            p.item.bag3[p.data_yesno[0]] = null;
//                            p.item.add_item_bag47(7, id_7, 1);
//                            p.item.update_Inventory(-1, false);
//                            //
//                            Message m = new Message(-50);
//                            m.writer().writeByte(0);
//                            m.writer().writeByte(1);
//                            m.writer().writeUTF("Thành công");
//                            m.writer().writeShort(id_7);
//                            m.writer().writeByte(7);
//                            m.writer().writeShort(1);
//                            p.addmsg(m);
//                            m.cleanup();
//                        }
//                        p.isTachTB = false;
//                    }
//                    break;
//                }
                case 11: {
                    if (p.isTachTB && p.data_yesno != null && p.data_yesno.length == 1) {
                        if (p.trade_target != null) {
                            p.isTachTB = false;
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            break;
                        }
                        if (p.data_yesno[0] < 0 || p.data_yesno[0] >= p.item.bag3.length) {
                            p.isTachTB = false;
                            break;
                        }
                        synchronized (p.item) {
                            Item_wear it_select = p.item.bag3[p.data_yesno[0]];
                            if (it_select != null) {
                                byte id_7 = itemz.Split_Item.getDustId(it_select);
                                
                                // Nếu đồ đã cường hóa, thu về thêm 50% nguyên liệu bột cường hóa (chỉ bột, không beri/ruby)
                                if (it_select.levelUp > 0) {
                                    if (it_select.isThanTrang()) {
                                        int totalBotCH = 0, totalBotTim = 0, totalBotVang = 0, totalBotSieuCap = 0;
                                        for (int lvl = 0; lvl < it_select.levelUp; lvl++) {
                                            int sId = itemz.rebuilds.NangCapThanTrang.getRequiredStoneId(lvl);
                                            int sNum = itemz.rebuilds.NangCapThanTrang.getRequiredStoneNum(lvl);
                                            if (sId == 1) totalBotCH += sNum;
                                            else if (sId == 3) totalBotTim += sNum;
                                            else if (sId == 4) totalBotVang += sNum;
                                            else if (sId == 18) totalBotSieuCap += sNum;
                                        }
                                        if (totalBotCH > 0) p.item.add_item_bag47(7, (short) 1, Math.max(1, (totalBotCH * 50) / 100));
                                        if (totalBotTim > 0) p.item.add_item_bag47(7, (short) 3, Math.max(1, (totalBotTim * 50) / 100));
                                        if (totalBotVang > 0) p.item.add_item_bag47(7, (short) 4, Math.max(1, (totalBotVang * 50) / 100));
                                        if (totalBotSieuCap > 0) p.item.add_item_bag47(7, (short) 18, Math.max(1, (totalBotSieuCap * 50) / 100));
                                    } else {
                                        int totalBotCH = 0, totalBotMau = 0;
                                        int matColorId = it_select.getColor() >= 3 ? 4 : (it_select.getColor() == 2 ? 3 : 2);
                                        for (int lvl = 0; lvl < it_select.levelUp; lvl++) {
                                            int[] mReq = itemz.UpgradeItem.get_material(lvl, it_select.getColor());
                                            if (mReq[0] > 0 && mReq[1] > 0) totalBotCH += mReq[1];
                                            if (mReq[2] > 0 && mReq[3] > 0) totalBotMau += mReq[3];
                                        }
                                        if (totalBotCH > 0) p.item.add_item_bag47(7, (short) 1, Math.max(1, (totalBotCH * 50) / 100));
                                        if (totalBotMau > 0) p.item.add_item_bag47(7, (short) matColorId, Math.max(1, (totalBotMau * 50) / 100));
                                    }
                                }

                                p.item.bag3[p.data_yesno[0]] = null;
                                p.item.add_item_bag47(7, id_7, 1);
                                p.item.updateInventory(false);
                                //
                                Message m = new Message(-50);
                                m.writer().writeByte(0);
                                m.writer().writeByte(1);
                                m.writer().writeUTF("Thành công");
                                m.writer().writeShort(id_7);
                                m.writer().writeByte(7);
                                m.writer().writeShort(1);
                                p.addmsg(m);
                                p.setAbility();
                                p.update_info_to_all();
                            }
                            p.isTachTB = false;
                        }
                    }
                    p.data_yesno = null;
                    break;
                }
                case 10: {
                    
                    if (p.get_ngoc() < 3_000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 3.000 Ruby");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_ngoc_ex(-3_000);
                    p.updateVnd(2_000_000);
                    p.updateMoney();
                    p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công 2.000.000 Extol.");
                    zLog.gI().add_log(p, "Đổi extol");
                    break;
                }
                
                 
                
                
                
                                
                case 9: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        long extol = (long)p.data_yesno[0] * 1000L;
                        if (p.get_vnd() < extol) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(extol) + " extol");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if(extol < 0) {
                            zLog.gI().add_log("BUG", "Bug ne 4: " + extol);
                            // System.err.println("Bug ne 4: " + extol);
                            return;
                        }
                        p.updateVnd(-extol);
                        p.update_ngoc(p.data_yesno[0]);
                        p.updateMoney();
                        p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công " + ZUtil.number_format(extol) + " extol ra "
                                + ZUtil.number_format(p.data_yesno[0]) + " Ruby.");
                    }
                    break;
                }
                


                case 8: {
                    if (p.item_chuyenhoa_save_0 != null && p.item_chuyenhoa_save_1 != null
                            && p.data_yesno != null && p.data_yesno.length == 1) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_0)
                                || ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_1)) {
                            p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        boolean has0 = false, has1 = false;
                        if (p.item != null && p.item.bag3 != null) {
                            for (Item_wear it : p.item.bag3) {
                                if (it != null) {
                                    if (it == p.item_chuyenhoa_save_0) has0 = true;
                                    if (it == p.item_chuyenhoa_save_1) has1 = true;
                                }
                            }
                        }
                        if (!has0 || !has1) {
                            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị trong hành trang!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.get_ngoc() < 500) {
                            p.getService().send_box_ThongBao_OK("Không đủ 500 ruby!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item.total_item_bag_by_id(4, p.data_yesno[0]) < 1) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + ItemTemplate4.get_item_name(p.data_yesno[0]));
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_1.template.level > 50
                                && p.item_chuyenhoa_save_1.template.level > (p.item_chuyenhoa_save_0.template.level
                                + 10)) {
                            p.getService().send_box_ThongBao_OK("Trang bị 5x trở lên, khi chuyển hóa chỉ được chuyển hóa cho trang bị cao hơn 1 cấp trang bị!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.template.typeEquip != p.item_chuyenhoa_save_1.template.typeEquip) {
                            p.getService().send_box_ThongBao_OK("Chỉ có thể chuyển hóa giữa 2 trang bị cùng loại!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.template.typeEquip == 7
                                || p.item_chuyenhoa_save_1.template.typeEquip == 7) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện chuyển hóa đối với dial!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.levelUp > 15 || p.item_chuyenhoa_save_1.levelUp > 15) {
                            p.getService().send_box_ThongBao_OK("Không thể chuyển hóa trang bị trên cấp +15!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.levelUp <= p.item_chuyenhoa_save_1.levelUp) {
                            p.getService().send_box_ThongBao_OK("Cấp cường hóa trang bị gốc phải cao hơn trang bị nhận!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        synchronized (p.item) {
                            p.item_chuyenhoa_save_1.levelUp = p.item_chuyenhoa_save_0.levelUp;
                            p.item_chuyenhoa_save_0.levelUp = 0;
                            p.update_ngoc(-500);
                            p.updateMoney();
                            p.item.remove_item47(4, p.data_yesno[0], 1);
                            p.item.updateInventory(false);
                            p.setAbility();
                            p.update_info_to_all();
                            int newLv = p.item_chuyenhoa_save_1.levelUp;
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            ChuyenHoa.show_result(p,
                                    "Quá trình chuyển số cường hóa hoàn tất. Số cường hóa mới là "
                                    + newLv,
                                    newLv);
                        }
                    }
                    break;
                }
                case 7: {
                    if (p.item_chuyenhoa_save_0 != null && p.item_chuyenhoa_save_1 != null
                            && p.data_yesno != null && p.data_yesno.length == 1) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_0)
                                || ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_1)) {
                            p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        boolean has0 = false, has1 = false;
                        if (p.item != null && p.item.bag3 != null) {
                            for (Item_wear it : p.item.bag3) {
                                if (it != null) {
                                    if (it == p.item_chuyenhoa_save_0) has0 = true;
                                    if (it == p.item_chuyenhoa_save_1) has1 = true;
                                }
                            }
                        }
                        if (!has0 || !has1) {
                            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị trong hành trang!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.setyesNoDialog(new model.YesNoDialog(p, 8, "Thông báo", ("Khi sử dụng "
                                + ItemTemplate4.get_item_name(p.data_yesno[0]) + " Bạn sẽ"
                                + "mất phí bảo hiểm chuyển hóa 500 Ruby để chuyển hóa 100% cấp cường hóa.\n"
                                + "Lưu ý: Bạn có thể lựa chọn không mất phí nhưng sẽ có tỷ lệ rớt 1 cấp cường hóa"),
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                        p.getService().startYesNo();
                        return;
                    }
                    break;
                }
                case 6: {
                    if (p.item_chuyenhoa_save_0 != null && p.item_chuyenhoa_save_1 != null
                            && p.item_chuyenhoa_save_0.levelUp > p.item_chuyenhoa_save_1.levelUp) {
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_0)
                                || ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_1)) {
                            p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        boolean has0 = false, has1 = false;
                        if (p.item != null && p.item.bag3 != null) {
                            for (Item_wear it : p.item.bag3) {
                                if (it != null) {
                                    if (it == p.item_chuyenhoa_save_0) has0 = true;
                                    if (it == p.item_chuyenhoa_save_1) has1 = true;
                                }
                            }
                        }
                        if (!has0 || !has1) {
                            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị trong hành trang!");
                            p.item_chuyenhoa_save_0 = null;
                            p.item_chuyenhoa_save_1 = null;
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_1.template.level > 50
                                && p.item_chuyenhoa_save_1.template.level > (p.item_chuyenhoa_save_0.template.level
                                + 10)) {
                            p.getService().send_box_ThongBao_OK("Trang bị 5x trở lên, khi chuyển hóa chỉ được chuyển hóa cho trang bị cao hơn 1 cấp trang bị!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.template.typeEquip != p.item_chuyenhoa_save_1.template.typeEquip) {
                            p.getService().send_box_ThongBao_OK("Chỉ có thể chuyển hóa giữa 2 trang bị cùng loại!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.template.typeEquip == 7
                                || p.item_chuyenhoa_save_1.template.typeEquip == 7) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện chuyển hóa đối với dial!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.levelUp > 15 || p.item_chuyenhoa_save_1.levelUp > 15) {
                            p.getService().send_box_ThongBao_OK("Không thể chuyển hóa trang bị trên cấp +15!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        if (p.item_chuyenhoa_save_0.levelUp >= 10) {
                            ItemTemplate4 it_bh = null;
                            switch (p.item_chuyenhoa_save_0.levelUp) {
                                case 15: {
                                    it_bh = ItemTemplate4.get_it_by_id(551);
                                    p.data_yesno = new int[]{551};
                                    break;
                                }
                                case 13:
                                case 14: {
                                    it_bh = ItemTemplate4.get_it_by_id(550);
                                    p.data_yesno = new int[]{550};
                                    break;
                                }
                                default: { // 10, 11, 12
                                    it_bh = ItemTemplate4.get_it_by_id(549);
                                    p.data_yesno = new int[]{549};
                                    break;
                                }
                            }
                            p.setyesNoDialog(new model.YesNoDialog(p, 7, "Thông báo",
                                    ("Bạn có muốn sử dụng " + it_bh.name + "?"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                            p.getService().startYesNo();
                            return;
                        } 
                        else {
                            if (p.get_ngoc() < 250) {
                                p.getService().send_box_ThongBao_OK("Không đủ 250 ruby!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            synchronized (p.item) {
                                int random = ZUtil.random(100);
                                p.item_chuyenhoa_save_1.levelUp = p.item_chuyenhoa_save_0.levelUp;
                                p.item_chuyenhoa_save_0.levelUp = 0;
                                if (random < 30) {
                                    p.item_chuyenhoa_save_1.levelUp -= 1;
                                } else if (random < 55) {
                                    p.item_chuyenhoa_save_1.levelUp -= 2;
                                } else if (random < 90) {
                                    p.item_chuyenhoa_save_1.levelUp -= 3;
                                }
                                if (p.item_chuyenhoa_save_1.levelUp < 0) {
                                    p.item_chuyenhoa_save_1.levelUp = 0;
                                }
                                p.update_ngoc(-250);
                                p.updateMoney();
                                p.item.updateInventory(false);
                                p.setAbility();
                                p.update_info_to_all();
                                int newLv = p.item_chuyenhoa_save_1.levelUp;
                                p.item_chuyenhoa_save_0 = null;
                                p.item_chuyenhoa_save_1 = null;
                                p.data_yesno = null;
                                p.map_tele = null;
                                ChuyenHoa.show_result(p,
                                        "Quá trình chuyển số cường hóa hoàn tất. Số cường hóa mới là "
                                        + newLv,
                                        newLv);
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    if (value != 0) {
                        p.map_tele = null;
                        p.data_yesno = null;
                        break;
                    }
                    if (p.ship_pet != null) {
                        p.getService().send_box_ThongBao_OK("Không thể chuyển map khi đang chuyển hàng");
                    } else {
                        if (p.map_tele != null && p.data_yesno != null && p.data_yesno.length == 1
                                && p.data_yesno[0] >= 0 && p.data_yesno[0] < p.map_tele.length) {
                            int targetMapId = p.map_tele[p.data_yesno[0]];
                            int mapIDCanGo = p.checkQuest();
                            if (p.map != null && p.map.template != null && core.MenuController.isSameVillage(p.map.template.id, targetMapId)) {
                                mapIDCanGo = 9999;
                            }
                            if (!Zone.isMapNoQuestLimit(targetMapId) && targetMapId > mapIDCanGo) {
                                p.getService().send_box_ThongBao_OK("Bản đồ này chưa mở! Hãy hoàn thành nhiệm vụ để mở khóa.");
                                p.map_tele = null;
                                p.data_yesno = null;
                                break;
                            }
                            Zone[] map_go = Zone.getMapByID(targetMapId);
                            if (map_go == null || map_go.length == 0 || map_go[0] == null || map_go[0].template == null) {
                                p.getService().send_box_ThongBao_OK("Không tìm thấy bản đồ!");
                            } else if (p.map != null && p.map.template != null && p.map.template.id == map_go[0].template.id) {
                                p.getService().send_box_ThongBao_OK("Đang ở map này rồi!");
                            } else if (p.get_vang() >= 20) {
                                p.update_vang(-20);
                                p.updateMoney();
                                Vgo vgo = new Vgo();
                                vgo.map_go = map_go;
                                if (vgo.map_go[0].template.npcs != null) {
                                    for (int i = 0; i < vgo.map_go[0].template.npcs.size(); i++) {
                                        Npc npc_temp = vgo.map_go[0].template.npcs.get(i);
                                        if (npc_temp != null) {
                                            boolean isTeleNpc = false;
                                            zinterfaces.iNpc teleHandler = zinterfaces.iNpc.get((short) -5);
                                            if (teleHandler != null && teleHandler.getId() != null) {
                                                for (short teleId : teleHandler.getId()) {
                                                    if (npc_temp.idmenu == teleId) {
                                                        isTeleNpc = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (isTeleNpc || (npc_temp.namegt != null && npc_temp.namegt.equals("Bản đồ")) 
                                                    || (npc_temp.name != null && npc_temp.name.contains("Bản đồ"))) {
                                                vgo.xnew = npc_temp.x;
                                                if (npc_temp.y < 250) {
                                                    vgo.ynew = (short) (npc_temp.y + 20);
                                                } else {
                                                    vgo.ynew = (short) (npc_temp.y - 20);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (vgo.xnew == 0 || vgo.ynew == 0) {
                                    vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                                    vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
                                }
                                p.goto_map(vgo);
                                p.updateArchiDaily(2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ 20 beri");
                            }
                        }
                    }
                    p.map_tele = null;
                    p.data_yesno = null;
                    break;
                }
                case 4: {
                    if (value != 0) {
                        p.data_yesno = null;
                        p.map_tele = null;
                        break;
                    }
                    if (p.map_tele == null && p.data_yesno != null && p.data_yesno.length == 1
                            && p.data_yesno[0] >= 0 && p.data_yesno[0] < p.skill_point.size()) {
                        Skill_info temp = p.skill_point.get(p.data_yesno[0]);
                        if (temp != null && temp.temp != null && temp.temp.ID >= 1000 && temp.temp.ID < 2000 && temp.temp.Lv_RQ > 0) {
                            if (p.get_ngoc() >= 2) {
                                p.update_ngoc(-2);
                                p.updateMoney();
                                Skill_Template.reset_skill(temp);
                                p.send_skill();
                                p.update_info_to_all();
                                p.getService().send_box_ThongBao_OK("Xóa kỹ năng " + temp.temp.name + " thành công");
                            } else {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 2 Ruby. Phí xóa kỹ năng này là 2 ruby!");
                            }
                        }
                    }
                    p.data_yesno = null;
                    break;
                }
                case 3: {
                    if (value != 0) {
                        p.data_yesno = null;
                        p.map_tele = null;
                        break;
                    }
                    if (p.map_tele == null && p.data_yesno != null && p.data_yesno.length == 1
                            && p.data_yesno[0] >= 0 && p.data_yesno[0] < p.skill_point.size()) {
                        Skill_info curSkill = p.skill_point.get(p.data_yesno[0]);
                        if (curSkill != null && curSkill.temp != null && curSkill.temp.ID < 2000) {
                            if (curSkill.temp.ID >= 1000) {
                                int dem = 0;
                                for (int i2 = 0; i2 < p.skill_point.size(); i2++) {
                                    Skill_info temp2 = p.skill_point.get(i2);
                                    if (temp2 != null && temp2.temp != null && temp2.temp.ID >= 1000 && temp2.temp.ID < 2000
                                            && temp2.temp.typeSkill == 3 && temp2.temp.Lv_RQ > 0) {
                                        dem++;
                                    }
                                }
                                if (curSkill.temp.Lv_RQ == -1 && curSkill.temp.typeSkill == 3
                                        && dem >= p.getNumPassive()) {
                                    p.getService().send_box_ThongBao_OK("Bạn đã học tối đa " + dem + " / " + p.getNumPassive()
                                            + " chiêu nội tại, hãy up level để mở thêm!");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                            }
                            Skill_info skillToSend;
                            if (curSkill.temp.Lv_RQ == -1) {
                                skillToSend = curSkill;
                            } else {
                                if (curSkill.temp.Lv_RQ >= 5) {
                                    p.getService().send_box_ThongBao_OK("Kỹ năng đã đạt cấp tối đa!");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                Skill_Template nextTemplate = Skill_Template.get_next_level_template(curSkill.temp);
                                if (nextTemplate == null) {
                                    p.getService().send_box_ThongBao_OK("Không tìm thấy cấp tiếp theo của kỹ năng này!");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                skillToSend = new Skill_info();
                                skillToSend.temp = nextTemplate;
                                skillToSend.exp = 0;
                                skillToSend.lvdevil = 0;
                                skillToSend.devilpercent = 0;
                            }
                            Learn_Skill.request_learn_new_skill(p, skillToSend);
                        }
                    }
                    p.data_yesno = null;
                    break;
                }
                case 2: {
                    if (p.get_ngoc() < 5) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_ngoc(-5);
                    p.updateMoney();
                    p.reset_point(0);
                    p.getService().send_box_ThongBao_OK("Bạn đã hồi điểm tiềm năng thành công");
                    break;
                }
                case 4032: {
                    if (p.item.total_item_bag_by_id(4, 32) > 0) {
                        String[] name_
                                = new String[]{"Sức mạnh của lửa", "Hỏa quyền", "Nắm đấm lửa"};
                        int[] icon_ = new int[]{32, 30, 29};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));

                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;

                        p.item.remove_item47(4, 32, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4033: {
                    if (p.item.total_item_bag_by_id(4, 33) > 0) {
                        String[] name_
                                = new String[]{"Sức sống bất diệt", "Chất bất ổn", "Súng máy caosu"};
                        int[] icon_ = new int[]{34, 33, 31};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 33, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4034: {
                    if (p.item.total_item_bag_by_id(4, 34) > 0) {
                        String[] name_
                                = new String[]{"Tiến hóa", "Thuốc tăng trưởng", "Hóa tuần lộc"};
                        int[] icon_ = new int[]{37, 36, 35};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 34, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4088: {
                    if (p.item.total_item_bag_by_id(4, 88) > 0) {
                        String[] name_ = new String[]{"Khói bất tử", "Khói tốc độ", "Mưa khói"};
                        int[] icon_ = new int[]{38, 39, 40};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 88, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4090: {
                    if (p.item.total_item_bag_by_id(4, 90) > 0) {
                        String[] name_
                                = new String[]{"Sức mạnh của lửa", "Hỏa quyền", "Nắm đấm lửa"};
                        int[] icon_ = new int[]{48, 47, 46};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 90, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4091: {
                    if (p.item.total_item_bag_by_id(4, 91) > 0) {
                        String[] name_ = new String[]{"Nét vẽ cường hóa", "Nét vẽ phòng thủ",
                            "Nét vẽ sức mạnh"};
                        int[] icon_ = new int[]{51, 50, 49};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 91, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4092: {
                    if (p.item.total_item_bag_by_id(4, 92) > 0) {
                        String[] name_ = new String[]{"Khói bất tử", "Khói tốc độ", "Mưa khói"};
                        int[] icon_ = new int[]{57, 56, 53};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 92, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4093: {
                    if (p.item.total_item_bag_by_id(4, 93) > 0) {
                        String[] name_
                                = new String[]{"Cát lưu động", "Bão cát sa mạc", "Cát linh động"};
                        int[] icon_ = new int[]{55, 54, 52};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 93, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4160: {
                    if (p.item.total_item_bag_by_id(4, 160) > 0) {
                        String[] name_ = new String[]{"Sấm chớp rền vang", "Lôi phạt",
                            "Bùng nổ sức mạnh", "Ý chí thần sấm"};
                        int[] icon_ = new int[]{61, 60, 59, 58};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 160, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4161: {
                    if (p.item.total_item_bag_by_id(4, 161) > 0) {
                        String[] name_ = new String[]{"Bão nham thạch", "Cột lửa", "Bùng cháy",
                            "Nỗi đau bỏng cháy"};
                        int[] icon_ = new int[]{65, 64, 63, 62};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 161, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4219: {
                    if (p.item.total_item_bag_by_id(4, 219) > 0) {
                        String[] name_
                                = new String[]{"Sóng âm - Xung kích", "Hóa báo đốm", "Tia chớp"};
                        int[] icon_ = new int[]{72, 71, 70};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 219, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4220: {
                    if (p.item.total_item_bag_by_id(4, 220) > 0) {
                        String[] name_
                                = new String[]{"Cơn lốc - Ưng kích", "Hóa chim ưng", "Chim săn mồi"};
                        int[] icon_ = new int[]{69, 68, 67};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 220, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4240: {
                    if (p.item.total_item_bag_by_id(4, 240) > 0) {
                        String[] name_
                                = new String[]{"Bộc phá", "Vết nứt", "Kình lực", "Địa chấn"};
                        int[] icon_ = new int[]{76, 75, 73, 74};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 240, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4316: {
                    if (p.item.total_item_bag_by_id(4, 316) > 0) {
                        String[] name_ = new String[]{"Giáp sáp", "Đao không kích", "Lao sáp"};
                        int[] icon_ = new int[]{79, 77, 78};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 316, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4317: {
                    if (p.item.total_item_bag_by_id(4, 317) > 0) {
                        String[] name_ = new String[]{"Thân thể thép", "Ảo ảnh trảm", "Loạn trảm"};
                        int[] icon_ = new int[]{82, 81, 80};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 317, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4318: {
                    if (p.item.total_item_bag_by_id(4, 318) > 0) {
                        String[] name_
                                = new String[]{"Thần hộ thể", "Tăng trọng", "Sức nặng ngàn cân"};
                        int[] icon_ = new int[]{85, 84, 83};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 318, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4427: { // trai bong toi
                    if (p.item.total_item_bag_by_id(4, 427) > 0) {
                        String[] name_ = new String[]{"Dòng chảy ma pháp", "Vòng xoáy ma pháp",
                            "Giải phóng", "Xoáy đen"};
                        int[] icon_ = new int[]{91, 88, 90, 89};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 427, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4428: { // trai 684
                    if (p.item.total_item_bag_by_id(4, 684) > 0) {
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(428);
                        } else {
                            p.get_skill_taq_new(428);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 684, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4869: { // trai anh sang (869)
                    if (p.item.total_item_bag_by_id(4, 869) > 0) {
                        String[] name_ = new String[]{"Cực quang phong ấn", "Thiên quang bất diệt",
                            "Quang vũ thực tâm", "Quang minh hóa thân"};
                        int[] icon_ = new int[]{97, 95, 96, 94};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 869, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4870: { // trai tinh yeu (870)
                    if (p.item.total_item_bag_by_id(4, 870) > 0) {
                        String[] name_ = new String[]{"Trái tim tử thần", "Tâm tiễn ái tình",
                            "Khí chất nữ thần", "Sắc đẹp chí mạng"};
                        int[] icon_ = new int[]{101, 100, 99, 98};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 870, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4873: { // trai nika (873)
                    if (p.item.total_item_bag_by_id(4, 873) > 0) {
                        String[] name_ = new String[]{"Nắm đấm của Thần", "Số 4: Snakeman",
                            "Hơi thở của Thần", "Tiếng trống tự do"};
                        int[] icon_ = new int[]{438, 439, 440, 441};
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 873, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4912: { // trai venom (912)
                    if (p.item.total_item_bag_by_id(4, 912) > 0) {
                        String[] name_ = new String[]{"Cổ Độc Phán Quyết", "Bách Độc Vũ Mưa Độc",
                            "Độc Long Thức Tỉnh", "Độc Ma Thần Thể"};
                        int[] icon_ = new int[]{442, 443, 444, 445};
                        int[] venomIdx = new int[]{809, 810, 811, 812};
                        for (int i = 0; i < venomIdx.length; i++) {
                            Skill_Template st = Skill_Template.get_temp(venomIdx[i], 0);
                            if (st != null) {
                                if (st.name != null && !st.name.isBlank() && !st.name.equalsIgnoreCase("Venom")) {
                                    name_[i] = st.name;
                                }
                                if (st.idIcon > 0 && st.idIcon != 4010) {
                                    icon_[i] = st.idIcon;
                                }
                            }
                        }
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 912, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4913: { // trai thanh long (913)
                    if (p.item.total_item_bag_by_id(4, 913) > 0) {
                        String[] name_ = new String[]{"Cổ Long Hoàng Kim", "Thần Long Giáng Lôi", "Long Thần Hộ Thể", "Vảy Rồng Thần"};
                        int[] icon_ = new int[]{446, 447, 448, 449};
                        int[] kaidoIdx = new int[]{813, 814, 815, 816};
                        for (int i = 0; i < kaidoIdx.length; i++) {
                            Skill_Template st = Skill_Template.get_temp(kaidoIdx[i], 0);
                            if (st != null) {
                                if (st.name != null && !st.name.isBlank()) {
                                    name_[i] = st.name;
                                }
                                if (st.idIcon > 0) {
                                    icon_[i] = st.idIcon;
                                }
                            }
                        }
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 913, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4914: { // trai phuong hoang (914)
                    if (p.item.total_item_bag_by_id(4, 914) > 0) {
                        String[] name_ = new String[]{"Phượng Hoàng Điểu Trảo", "Đại Phượng Ấn Hypernova", "Lam Hỏa Niết Bàn", "Thể Chất Bất Tử Điểu"};
                        int[] icon_ = new int[]{450, 451, 452, 453};
                        int[] phoIdx = new int[]{817, 818, 819, 820};
                        for (int i = 0; i < phoIdx.length; i++) {
                            Skill_Template st = Skill_Template.get_temp(phoIdx[i], 0);
                            if (st != null) {
                                if (st.name != null && !st.name.isBlank()) {
                                    name_[i] = st.name;
                                }
                                if (st.idIcon > 0) {
                                    icon_[i] = st.idIcon;
                                }
                            }
                        }
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 914, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 4915: { // trai mochi mochi (915)
                    if (p.item.total_item_bag_by_id(4, 915) > 0) {
                        String[] name_ = new String[]{"Zan Giri Mochi", "Mochi Tendrils", "Thức Tỉnh Mochi", "Thấu Thị Tương Lai"};
                        int[] icon_ = new int[]{454, 455, 456, 457};
                        int[] mochiIdx = new int[]{821, 822, 823, 824};
                        for (int i = 0; i < mochiIdx.length; i++) {
                            Skill_Template st = Skill_Template.get_temp(mochiIdx[i], 0);
                            if (st != null) {
                                if (st.name != null && !st.name.isBlank()) {
                                    name_[i] = st.name;
                                }
                                if (st.idIcon > 0) {
                                    icon_[i] = st.idIcon;
                                }
                            }
                        }
                        p.getService().NewDialog_eat_taq(name_, icon_, (id - 4000));
                        if (p.useTAQ == 2 && p.detu != null) {
                            p.detu.get_skill_taq_new(id - 4000);
                        } else {
                            p.get_skill_taq_new(id - 4000);
                        }
                        p.useTAQ = 0;
                        p.item.remove_item47(4, 915, 1);
                        p.item.updateInventory(false);
                    }
                    break;
                }

                case 1: {
                    if (p.item_to_kham_ngoc != null) {
                        int vang_req = 0;
                        for (int i = 0; i < p.item_to_kham_ngoc.mdakham.length; i++) {
                            if (p.item_to_kham_ngoc.mdakham[i] >= 44
                                    && p.item_to_kham_ngoc.mdakham[i] <= 79) {
                                vang_req += Rebuild_Item.PRICE_THAO_NGOC[Rebuild_Item
                                        .get_percent_hop_ngoc(p.item_to_kham_ngoc.mdakham[i])];
                            } else if (p.item_to_kham_ngoc.mdakham[i] >= 241
                                    && p.item_to_kham_ngoc.mdakham[i] <= 270) {
                                vang_req += 300;
                            } else {
                                vang_req += 350;
                            }
                        }
                        if (vang_req > 0) {
                            if (p.get_ngoc() < vang_req) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + vang_req + " ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (p.item_to_kham_ngoc.mdakham.length > p.item.able_bag()) {
                                p.getService().send_box_ThongBao_OK("Hãy chừa ít nhất " + p.item_to_kham_ngoc.mdakham.length
                                        + " ô trống trong hành trang");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-vang_req);
                            p.updateMoney();
                            for (int i = 0; i < p.item_to_kham_ngoc.mdakham.length; i++) {
                                p.item.add_item_bag47(4, p.item_to_kham_ngoc.mdakham[i], 1);
                            }
                            p.item_to_kham_ngoc.mdakham = new short[0];
                            p.item_to_kham_ngoc.option_item_2.clear();
                            p.item.updateInventory(false);
                            Message m = new Message(-67);
                            m.writer().writeByte(6);
                            m.writer().writeUTF("Tháo ngọc khảm trang bị "
                                    + p.item_to_kham_ngoc.template.name + " thành công");
                            p.addmsg(m);
                            m.cleanup();
                        }
                    } else {
                        Rebuild_Item.show_table(p, 4);
                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại");
                    }
                    break;
                }
                case 0: {
                    // use_item_3
                    if (p.use_item_3 != -1) {
                        Item_wear it = p.item.bag3[p.use_item_3];
                        if (it != null && UseItem.check_it_can_wear(it.template.typeEquip)) {
                            p.wear_item(it);
                        }
                        p.use_item_3 = -1;
                    }
                    break;
                }
            }
        } else if (value == 1) { // hoi ren
            switch (id) {
                case 80: {
                    if (p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 80) {
                        Player p0 = Zone.get_player_by_Index_allmap(p.data_yesno[1]);
                        if (p0 != null && p0.getService() != null) {
                            p0.getService().send_box_ThongBao_OK(p.name + " đã từ chối nhận bạn làm đệ tử.");
                        }
                        p.data_yesno = null;
                        p.map_tele = null;
                        p.data_yesno_gem = null;
                    }
                    break;
                }
                case 93: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 93) {
                        for (int i = 0; i < p.detu.skill_point.size(); i++) {
                            Skill_info sk = p.detu.skill_point.get(i);
                            if (p.data_yesno[1] == i && sk.temp.typeSkill == 1 && sk.temp.Lv_RQ > 0) {
                                if (sk.lvdevil > 4) {
                                    p.getService().send_box_ThongBao_OK(sk.temp.name + " đã được cường hóa tối đa!");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                if (p.item.total_item_bag_by_id(7, 9) < 10) {
                                    p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                int percent = (sk.lvdevil == 0) ? 10 //
                                        : ((sk.lvdevil == 1) ? 8 //
                                                : ((sk.lvdevil == 2) ? 6 //
                                                        : ((sk.lvdevil == 3) ? 5 : 4)));
                                if (p.get_ngoc() < 10) {
                                    p.getService().send_box_ThongBao_OK("Không đủ 100 ruby");
                                    p.data_yesno = null;
                                    p.map_tele = null;
                                    return;
                                }
                                p.update_ngoc(-100);
                                p.updateMoney();
                                p.item.remove_item47(7, 9, 10);
                                p.item.updateInventory(false);
                                //
                                boolean suc = 50 > ZUtil.random(120);
                                if (suc) {
                                    sk.devilpercent += percent;
                                    if (sk.devilpercent >= 100) {
                                        sk.devilpercent = 0;
                                        sk.lvdevil++;
                                    }
                                    p.send_skill();
                                    p.update_info_to_all();
                                }
                                percent = (sk.lvdevil == 0) ? 10 //
                                        : ((sk.lvdevil == 1) ? 8 //
                                                : ((sk.lvdevil == 2) ? 6 //
                                                        : ((sk.lvdevil == 3) ? 5 : 4)));
                                String notice = ("Nâng cấp " + (suc ? "thành công" : "thất bại")
                                        + ".\nXác nhận nâng cấp ác quỷ kỹ năng " + sk.temp.name + "? Hiện tại cấp "
                                        + sk.lvdevil + " - " + sk.devilpercent + "%. Thành công sẽ tăng thêm " + percent
                                        + " chiêu nội tại, hãy up level để mở thêm!");
                                p.data_yesno = new int[]{93, p.data_yesno[1]};
                                p.setyesNoDialog(new model.YesNoDialog(p, 93, "Thông báo", notice, new String[]{"100.000", "100", "Đóng"}, new byte[]{6, 7, 1}));
                                p.getService().startYesNo();
                                return;
                            }
                        }
                    }

                }
                break;
                case 92: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 3 && p.data_yesno[0] == 92) {
                        ItemFashionP temp = p.detu.check_itfashionP(p.data_yesno[1], p.data_yesno[2]);
                        if (temp != null) {
                            p.detu.map = p.map;
                            p.detu.update_itfashionP(temp, p.data_yesno[2]);
                            p.detu.update_info_to_all();
                            ItemFashionP.show_table(p, p.data_yesno[2]);
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Sử dụng " + ItemHair.get_item(temp.id, p.data_yesno[2]).name + " cho đệ tử");
                            }
                        } else {
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Chưa mua vật phẩm này!");
                            }
                        }
                    }
                    p.data_yesno = null;
                }
                break;
                case 91: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 91) {
                        p.useTAQ = 2;
                        p.setyesNoDialog(new model.YesNoDialog(p, (p.data_yesno[1] + 4000), "Thông báo",
                                "Bạn có muốn sử dụng " + ItemTemplate4.get_it_by_id(p.data_yesno[1]).name + " cho đệ tử",
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();

                    }
                }
                break;
                case 89: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 89) {
                        ItemFashionP2 temp = p.detu.check_fashion(p.data_yesno[1]);
                        if (temp != null) {
                            p.detu.update_fashionP2(temp);
                            p.detu.update_info_to_all();
                            ItemFashionP.show_table(p, 105);
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Mặc thành công " + ItemFashion.get_item(temp.id).name);
                            }
                        }
                    }
                    p.data_yesno = null;
                }
                break;
                case 2: {
                    if (p.detu != null) {
                        if (p.get_ngoc() < 5) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.update_ngoc(-5);
                        p.updateMoney();
                        p.detu.reset_point(0);
                        p.getService().send_box_ThongBao_OK("Bạn đã hồi điểm tiềm năng cho đệ tử thành công");
                    }
                    break;
                }
                case 88: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 88) {
                        ItemFashionP2 temp = p.detu.check_fashion(p.data_yesno[1]);
                        if (temp != null) {
                            if (temp.is_use) {
                                temp.is_use = false;
                                if (temp.id == 122) {
                                    p.detu.tocSuper = 0;
                                }
                                p.detu.update_info_to_all();
                                ItemFashionP.show_table(p, 105);
                                if (p.getService() != null) {
                                    p.getService().send_box_ThongBao_OK("Tháo thành công " + ItemFashion.get_item(temp.id).name);
                                }
                            }
                        }
                    }
                    p.data_yesno = null;
                }
                break;
                case 87: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 87) {
                        int bagIndex = p.data_yesno[1];
                        p.data_yesno = null;
                        p.use_item_3 = -1;
                        if (bagIndex >= 0 && bagIndex < p.item.bag3.length) {
                            Item_wear it = p.item.bag3[bagIndex];
                            if (it != null && it.template != null) {
                                if (p.detu.level < it.template.level) {
                                    if (p.getService() != null) {
                                        p.getService().send_box_ThongBao_OK("Đệ tử chưa đủ level để mặc trang bị này!");
                                    }
                                    return;
                                }
                                if (it.template.clazz != 0 && p.detu.clazz != it.template.clazz) {
                                    if (p.getService() != null) {
                                        p.getService().send_box_ThongBao_OK("Đệ tử không phù hợp hệ phái để mặc trang bị này!");
                                    }
                                    return;
                                }
                                p.detu.wear_item(it);
                                p.detu.updateParts();
                                p.detu.setAbility();
                                if (p.isFusion) {
                                    p.detu.applyFusion();
                                    p.update_info_to_all();
                                }
                                p.item.updateInventory(false);
                                if (p.getService() != null) {
                                    p.getService().send_box_ThongBao_OK("Đã mặc " + it.template.name + " cho Đệ tử thành công!");
                                }
                            }
                        }
                    }
                    break;
                }
                case 86: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 86) {
                        if (p.detu.pointAttribute >= 2) {
                            switch (p.data_yesno[1]) {
                                case 1:
                                    if ((p.detu.point1 + 2) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point1 += 2;
                                    break;
                                case 2:
                                    if ((p.detu.point2 + 2) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point2 += 2;
                                    break;
                                case 3:
                                    if ((p.detu.point3 + 2) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point3 += 2;
                                    break;
                                case 4:
                                    if ((p.detu.point4 + 2) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point4 += 2;
                                    break;
                                case 5:
                                    if ((p.detu.point5 + 2) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point5 += 2;
                                    break;
                            }
                            p.detu.pointAttribute -= 2;
                            p.detu.update_info_to_all();
                            p.getService().send_box_ThongBao_OK("Nâng thành công");
                        } else {
                            p.getService().send_box_ThongBao_OK("Không đủ điểm tiềm năng");
                        }
                    }
                }
                break;
                case 85: {
                    if (p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 85) {
                        if (p.get_ngoc() < 500) {
                            p.getService().send_box_ThongBao_OK("Không đủ 500 ruby");
                            p.data_yesno = null;
                            return;
                        }
                        if (BotTruyNa.ok(p, p.data_yesno[1])) {
                            p.update_ngoc(-500);
                            p.updateMoney();
                        }

                    }
                }
                break;
                case 83: {
                    if (event.EventManager.isActive(9) && p.data_yesno != null && p.data_yesno.length > 0 && p.data_yesno[0] == 83) {
                        EventData temp_select = null;
                        for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                            if (p.eventData.get(i2).eventID == 9) {
                                temp_select = p.eventData.get(i2);
                                break;
                            }
                        }
                        if (temp_select != null) {
                            if (temp_select.data[SuKienTrongCay.SHOP_SIZE] >= 150) {
                                if (temp_select.data[SuKienTrongCay.SHOP_SIZE + 17] == 0) {
                                    temp_select.data[SuKienTrongCay.SHOP_SIZE + 17] = 1;
                                    List<GiftBox> listGift = new ArrayList<>();
                                    GiftBox.addGift(listGift, 7, 10, 5);
                                    if (!listGift.isEmpty()) {
                                        Service.send_gift(p, 1, "Thưởng trồng cây", "Nhận được", listGift, true);
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Bạn đã nhận quà rồi");
                                }
                            } else {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ số lần trồng cây may mắn để nhận.Bạn mới trồng được " + temp_select.data[SuKienTrongCay.SHOP_SIZE] + "/150 cây may mắn");
                            }
                        }
                    }
                }
                break;
                case 53: {
                    if (p.name_ThoSanHaiTac != null && p.name_ThoSanHaiTac.length == 1
                            && p.typePirate == 1) {
                        Player p0 = Zone.get_player_by_name_allmap(p.name_ThoSanHaiTac[0]);
                        if (p0 != null && p0.map.equals(p.map)) {
                            p0.getService().send_box_ThongBao_OK(p.name + " từ chối bảo vệ vận hàng");
                        }
                        p.getService().send_box_ThongBao_OK("từ chối thành công");
                        p.name_ThoSanHaiTac = null;
                    }
                    break;
                }
                case 36: {
                    if (p.get_ngoc() < 45) {
                        p.getService().send_box_ThongBao_OK("Không đủ 45 ruby");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (!p.item.can_add_item_bag47(4, 232, 3)) {
                        p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_ngoc(-45);
                    p.updateMoney();
                    p.item.add_item_bag47(4, 232, 3);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Mua 3 Vé vòng xoay may mắn thành công");
                    break;
                }
                case 34: {
                    if (p.item.total_item_bag_by_id(7, 9) < 10) {
                        p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 29) < 1) {
                        p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate4.get_it_by_id(29).name);
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.get_ngoc() < 5) {
                        p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (!p.item.can_add_item_bag47(4, 158, 1)) {
                        p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    p.update_ngoc(-5);
                    p.updateMoney();
                    p.item.remove_item47(7, 9, 10);
                    //
                    boolean suc = 5 > ZUtil.random(150);
                    if (suc) {
                        p.item.remove_item47(4, 29, 1);
                        p.item.add_item_bag47(4, 158, 1);
                    }
                    p.item.updateInventory(false);
                    //
                    p.getService().sendUpgradeDevilActionMsg((byte) 17, (byte) (suc ? 1 : 3), "Cường hóa kỹ năng " + (suc ? "thành công" : "thất bại"));
                    break;
                }
                case 33: {
                    if (p.data_yesno != null && p.data_yesno.length == 1) {
                        if (model.ThanTrangConfig.isThanTrangSkill(p.data_yesno[0])) {
                            p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        Skill_info sk_temp = p.get_skill_temp(p.data_yesno[0]);
                        if (sk_temp != null) {
                            if (sk_temp.temp != null && model.ThanTrangConfig.isThanTrangSkill(sk_temp.temp.ID)) {
                                p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            if (sk_temp.lvdevil > 4) {
                                p.getService().send_box_ThongBao_OK(sk_temp.temp.name + " đã được cường hóa tối đa!");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            int percent = (sk_temp.lvdevil == 0) ? 10 //
                                    : ((sk_temp.lvdevil == 1) ? 8 //
                                            : ((sk_temp.lvdevil == 2) ? 6 //
                                                    : ((sk_temp.lvdevil == 3) ? 5 : 4)));
                            if (p.get_ngoc() < 5) {
                                p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                                p.data_yesno = null;
                                p.map_tele = null;
                                return;
                            }
                            p.update_ngoc(-5);
                            p.updateMoney();
                            p.item.remove_item47(7, 9, 10);
                            p.item.updateInventory(false);
                            //
                            boolean suc = 50 > ZUtil.random(120 + sk_temp.lvdevil * 10);
                            if (suc) {
                                sk_temp.devilpercent += percent;
                                if (sk_temp.devilpercent >= 100) {
                                    sk_temp.devilpercent = 0;
                                    sk_temp.lvdevil++;
                                }
                                p.send_skill();
                                p.update_info_to_all();
                            }
                            //
                            p.getService().sendUpgradeDevilActionMsg((byte) 12, (byte) (suc ? 1 : 3), "Cường hóa kỹ năng " + (suc ? "thành công" : "thất bại"));
                        }
                    }
                    break;
                }
                case 31: {
                    if (p.item_to_kham_ngoc != null && (p.item_to_kham_ngoc.template.typeEquip < 6
                            || p.item_to_kham_ngoc.template.typeEquip == 7)) {
                        if (p.get_ngoc() < 100) {
                            p.getService().send_box_ThongBao_OK("Không đủ 100 ruby");
                            p.data_yesno = null;
                            p.map_tele = null;
                            return;
                        }
                        p.update_ngoc(-100);
                        p.updateMoney();
                        p.item_to_kham_ngoc.valueChetac += 10;
                        if (p.item_to_kham_ngoc.valueChetac >= 100) {
                            p.item_to_kham_ngoc.valueChetac = 100;
                        }
                        //
                        Message m = new Message(-67);
                        m.writer().writeByte(25);
                        m.writer().writeUTF("Bạn phục hồi 10 điểm chế tác thành công");
                        p.addmsg(m);
                        m.cleanup();
                        //
                        p.item.updateInventory(false);
                    }
                    break;
                }
                case 16: {
                    p.map_boss_info = null;
                    p.ischangemap = false;
                    p.wait_change_map = false;
                    p.time_change_map = System.currentTimeMillis() + 1000L;
                    break;
                }
                case 11: {
                    p.isTachTB = false;
                    break;
                }
                case 7: {
                    if (p.item_chuyenhoa_save_0 == null || p.item_chuyenhoa_save_1 == null
                            || ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_0)
                            || ChuyenHoa.isItemBlockedChuyenHoa(p.item_chuyenhoa_save_1)) {
                        p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.trade_target != null) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    boolean has0 = false, has1 = false;
                    if (p.item != null && p.item.bag3 != null) {
                        for (Item_wear it : p.item.bag3) {
                            if (it != null) {
                                if (it == p.item_chuyenhoa_save_0) has0 = true;
                                if (it == p.item_chuyenhoa_save_1) has1 = true;
                            }
                        }
                    }
                    if (!has0 || !has1) {
                        p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị trong hành trang!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.levelUp <= p.item_chuyenhoa_save_1.levelUp) {
                        p.getService().send_box_ThongBao_OK("Cấp cường hóa trang bị gốc phải cao hơn trang bị nhận!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.template.typeEquip != p.item_chuyenhoa_save_1.template.typeEquip) {
                        p.getService().send_box_ThongBao_OK("Chỉ có thể chuyển hóa giữa 2 trang bị cùng loại!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.template.typeEquip == 7
                            || p.item_chuyenhoa_save_1.template.typeEquip == 7) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện chuyển hóa đối với dial!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.levelUp > 15 || p.item_chuyenhoa_save_1.levelUp > 15) {
                        p.getService().send_box_ThongBao_OK("Không thể chuyển hóa trang bị trên cấp +15!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_1.template.level > 50
                            && p.item_chuyenhoa_save_1.template.level > (p.item_chuyenhoa_save_0.template.level
                            + 10)) {
                        p.getService().send_box_ThongBao_OK("Trang bị 5x trở lên, khi chuyển hóa chỉ được chuyển hóa cho trang bị cao hơn 1 cấp trang bị!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    if (p.get_ngoc() < 250) {
                        p.getService().send_box_ThongBao_OK("Không đủ 250 ruby!");
                        p.data_yesno = null;
                        p.map_tele = null;
                        return;
                    }
                    synchronized (p.item) {
                        p.item_chuyenhoa_save_1.levelUp = p.item_chuyenhoa_save_0.levelUp;
                        p.item_chuyenhoa_save_0.levelUp = 0;
                        if (80 > ZUtil.random(120)) {
                            p.item_chuyenhoa_save_1.levelUp -= ZUtil.random(0, 3);
                            if (p.item_chuyenhoa_save_1.levelUp < 0) {
                                p.item_chuyenhoa_save_1.levelUp = 0;
                            }
                        }
                        p.update_ngoc(-250);
                        p.updateMoney();
                        p.item.updateInventory(false);
                        p.setAbility();
                        p.update_info_to_all();
                        int newLv = p.item_chuyenhoa_save_1.levelUp;
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        p.data_yesno = null;
                        p.map_tele = null;
                        ChuyenHoa.show_result(p,
                                "Quá trình chuyển số cường hóa hoàn tất. Số cường hóa mới là "
                                + newLv,
                                newLv);
                    }
                    break;
                }
                case 1: {
                    Rebuild_Item.show_table(p, 4);
                    break;
                }
                case 0: {
                    p.use_item_3 = -1;
                    break;
                }
            }
        } else if (value == 2) { // other
            switch (id) {
                case 0: {
                    p.use_item_3 = -1;
                    break;
                }
                case 87: {
                    p.data_yesno = null;
                    p.use_item_3 = -1;
                    break;
                }
                case 86: {
                    if (p.detu != null && p.data_yesno != null && p.data_yesno.length == 2 && p.data_yesno[0] == 86) {
                        if (p.detu.pointAttribute >= 10) {
                            switch (p.data_yesno[1]) {
                                case 1:
                                    if ((p.detu.point1 + 10) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point1 += 10;
                                    break;
                                case 2:
                                    if ((p.detu.point2 + 10) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point2 += 10;
                                    break;
                                case 3:
                                    if ((p.detu.point3 + 10) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point3 += 10;
                                    break;
                                case 4:
                                    if ((p.detu.point4 + 10) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point4 += 10;
                                    break;
                                case 5:
                                    if ((p.detu.point5 + 10) > 80) {
                                        p.getService().send_box_ThongBao_OK("Không thể tăng quá 80 điểm");
                                        p.data_yesno = null;
                                        return;
                                    }
                                    p.detu.point5 += 10;
                                    break;
                            }
                            p.detu.pointAttribute -= 10;
                            p.detu.update_info_to_all();
                            p.getService().send_box_ThongBao_OK("Nâng thành công");
                        } else {
                            p.getService().send_box_ThongBao_OK("Không đủ điểm tiềm năng");
                        }
                    }
                }
                break;
            }
        }
        if (id != 13) {
            p.data_yesno = null;
            p.map_tele = null;
            p.data_yesno_gem = null;
        }
    }
}
