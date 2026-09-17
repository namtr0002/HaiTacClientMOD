package model;

import event.Event;
import event.EventData;
import map.zones.DauTruongTuDo;
import skill.Skill_info;
import skill.Skill_Template;
import event.SuKienTrongCay;
import network.MessageHandler;
import event.SuKienHalloween;
import event.SuKienNoel;
import event.SuKienGioTo;
import event.SuKien20Thang10;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import core.Manager;
import network.Service;
import core.ZUtil;
import event.EventManager;
import network.Message;
import java.util.HashSet;
import java.util.Set;
import database.RandomCollection;
import itemz.MainItem;
import template.*;


public class UseItem {

    public static void process(Player p, Message m2) throws IOException {
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        // System.out.println(id);
        // System.out.println(cat);
        switch (cat) {
            case 3: {
                use_item_3(p, id);
                break;
            }
            case 4: {
                use_item_potion(p, id);
                break;
            }
            case 7: {
                use_item_7(p, id);
                break;
            }
            case 105: {
                ItemFashionP2 temp = p.check_fashion(id);
                if (temp != null) {
                    if (temp.is_use) {
                        temp.is_use = false;
                        if (temp.id == 122) {
                            p.tocSuper = 0;
                        }
                        p.update_info_to_all();
                        ItemFashionP.show_table(p, 105);
                        p.getService().send_box_ThongBao_OK("Tháo thành công " + ItemFashion.get_item(temp.id).name);
                    } else {
                        p.update_fashionP2(temp);
                        p.update_info_to_all();
                        ItemFashionP.show_table(p, 105);
                        p.getService().send_box_ThongBao_OK("Mặc thành công " + ItemFashion.get_item(temp.id).name);
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Chưa mua vật phẩm này!");
                }
                break;
            }
            case 102: {
                ItemBoatP temp = p.check_itboat(id);
                if (temp != null) {
                    temp.is_use = true;
                    p.update_new_part_boat(temp);
                    ItemBoat.update_part_boat_when_shopping(p);
                    ItemFashionP.show_table(p, 102);
                    p.getService().send_box_ThongBao_OK("Sử dụng " + ItemBoat.get_item(temp.id).name);
                } else {
                    p.getService().send_box_ThongBao_OK("Chưa mua vật phẩm này!");
                }
                break;
            }
            case 108:
            case 103: {
                ItemFashionP temp = p.check_itfashionP(id, cat);
                if (temp != null) {
                    p.update_itfashionP(temp, cat);
                    p.update_wearing_to_all();
                    p.getService().UpdateInfoMaincharInfo();
                    ItemFashionP.show_table(p, cat);
                    ItemHair it = ItemHair.get_item(temp.id, cat);
                    String itName = (it != null && it.name != null) ? it.name : ("vật phẩm #" + temp.id);
                    p.getService().send_box_ThongBao_OK("Sử dụng " + itName);
                } else {
                    p.getService().send_box_ThongBao_OK("Chưa mua vật phẩm này!");
                }
                break;
            }
        }
    }

    public static void use_item_7(Player p, int id) {
    }

    public static boolean use_item_4(Player p, int id) throws IOException {
        if (p == null || p.isdie) return false;
        ItemTemplate4 it_temp = ItemTemplate4.get_it_by_id(id);
        if (it_temp != null) {
            int cdKey = Player.getItemCooldownKey(it_temp);
            if (it_temp.type == 1 || it_temp.type == 2) { // item hp mp
                if (p.type_pk == 0) {
                    p.getService().send_box_ThongBao_OK("Không thể sử dụng bình hồi phục ở trạng thái Đồ sát!");
                    return false;
                }
                if (it_temp.type == 1) {
                    if (p.hp >= p.ability.get_hp_max(true)) {
                        p.getService().sendThongBao("HP đã đầy!");
                        return false;
                    }
                    if (!p.isItemReady(cdKey)) {
                        return false;
                    }
                    EffTemplate eff = p.get_eff(0);
                    if (it_temp.timedelay > 0 && eff != null && (eff.time - System.currentTimeMillis()) > 500) {
                        return false;
                    }
                    long par = it_temp.value;
                    if (it_temp.id == 173) { // com hop hai tac
                        par = p.ability.get_hp_max(true) / 20;
                    }
                    par = (par * (100 + p.ability.get_hp_potion_use_percent(true) / 10)) / 100;
                    if (par < 0) {
                        par = 0;
                    }
                    if (it_temp.id == 173 && par > 10_000) { // com hop hai tac
                        par = 10_000;
                    }
                    int delay = it_temp.timedelay >= 0 ? it_temp.timedelay : 5000;
                    if (delay > 0) {
                        p.add_new_eff(0, (int) par, delay);
                    }
                    p.applyItemCooldown(cdKey, delay);
                    p.getService().use_potion((int) par, 0);
                    p.time_potion_tick = System.currentTimeMillis() + 1000L;
                    return true;
                } else if (it_temp.type == 2) {
                    int maxMp = p.ability != null ? p.ability.get_mp_max(true) : p.mpMax;
                    if (maxMp <= 0) maxMp = 500;
                    if (p.mp >= maxMp) {
                        p.getService().sendThongBao("MP đã đầy!");
                        return false;
                    }
                    if (!p.isItemReady(cdKey)) {
                        return false;
                    }
                    EffTemplate eff = p.get_eff(1);
                    if (it_temp.timedelay > 0 && eff != null && (eff.time - System.currentTimeMillis()) > 500) {
                        return false;
                    }
                    int par = it_temp.value;
                    par = (par * (100 + p.ability.get_mp_potion_use_percent(true) / 10)) / 100;
                    int delay = it_temp.timedelay >= 0 ? it_temp.timedelay : 5000;
                    if (delay > 0) {
                        p.add_new_eff(1, par, delay);
                    }
                    p.applyItemCooldown(cdKey, delay);
                    p.getService().use_potion(0, par);
                    p.time_potion_tick = System.currentTimeMillis() + 1000L;
                    return true;
                }
            } else {
                if (!p.isItemReady(cdKey)) {
                    return false;
                }
                long itemDelay = it_temp.timedelay >= 0 ? it_temp.timedelay : 300L;
                if (EventManager.dispatchUseItem(p, id)) {
                    p.applyItemCooldown(cdKey, itemDelay);
                    return true;
                }
                boolean used = true;
                switch (id) {
                    case 909: {
                        List<Skill_info> name_skill = new ArrayList<>();
                        for (int i = 0; i < p.skill_point.size(); i++) {
                            Skill_info sk = p.skill_point.get(i);
                            if (sk.temp != null && (sk.temp.ID >= 2000 || sk.temp.typeDevil > 0 || (sk.temp.ID >= 0 && sk.temp.ID <= 2))) {
                                name_skill.add(sk);
                            }
                        }
                        if (name_skill.isEmpty()) {
                            p.getService().send_box_ThongBao_OK("Chưa có kỹ năng Ác Quỷ nào có thể dùng với vật phẩm này!");
                            return false;
                        }
                        String[] str = new String[name_skill.size() + 1];
                        byte[] select = new byte[name_skill.size() + 1];
                        for (int i = 0; i < name_skill.size(); i++) {
                            Skill_info sk = name_skill.get(i);
                            str[i] = sk.temp.name + " (Cấp " + sk.lvdevil + " - " + sk.devilpercent + "%)";
                            select[i] = (byte) -1;
                        }
                        str[str.length - 1] = "Hủy";
                        select[str.length - 1] = (byte) -1;
                        model.YesNoDialog dlg909 = new model.YesNoDialog(p, 9090, "Năng Lượng Ác Quỷ",
                                "Chọn kỹ năng bạn muốn tăng kinh nghiệm Ác Quỷ (+100 EXP):", str, select);
                        dlg909.setHandler(val -> {
                            try {
                                if (val >= 0 && val < name_skill.size()) {
                                    Skill_info sk_select = name_skill.get(val);
                                    if (sk_select.lvdevil >= 5) {
                                        p.getService().send_box_ThongBao_OK("Kỹ năng " + sk_select.temp.name + " đã đạt cấp ác quỷ tối đa (+5)!");
                                        return;
                                    }
                                    if (p.item.total_item_bag_by_id(4, 909) <= 0) {
                                        p.getService().send_box_ThongBao_OK("Không đủ Năng lượng ác quỷ trong hành trang!");
                                        return;
                                    }
                                    ItemTemplate4 itTemp = ItemTemplate4.get_it_by_id(909);
                                    int exp_devil = (itTemp != null && itTemp.value > 0) ? itTemp.value : 100;
                                    p.item.remove_item47(4, 909, 1);
                                    Message m2 = new Message(-13);
                                    m2.writer().writeShort(909);
                                    m2.writer().writeShort(p.item.total_item_bag_by_id(4, 909));
                                    p.conn.addmsg(m2);
                                    m2.cleanup();
                                    p.item.updateInventory(false);
                                    sk_select.update_exp_devil(exp_devil);
                                    p.send_skill();
                                    p.update_info_to_all();
                                    p.getService().send_box_ThongBao_OK("Sử dụng Năng Lượng Ác Quỷ cho "
                                            + sk_select.temp.name + " thành công!\n(Cấp Ác Quỷ: Cấp " + sk_select.lvdevil + " - " + sk_select.devilpercent + "%)");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        p.setyesNoDialog(dlg909);
                        p.getService().startYesNo();
                        return false;
                    }
                    case 871: {
                        // Mở menu quản lý đệ tử từ xa
                        if (p.detu == null && !p.isFusion && (p.nameDe == null || p.nameDe.isBlank())) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        String curStatus = (p.detu != null)
                                ? model.DeTu.STATUS_NAMES[Math.min(p.detu.detuStatus, model.DeTu.STATUS_NAMES.length - 1)]
                                : (p.isFusion ? "Hợp thể" : "Chưa triệu hồi");
                        model.YesNoDialog dlg871 = new model.YesNoDialog(p, 87100,
                                "Quản Lý Đệ Tử [Đang: " + curStatus + "]",
                                "Chọn trạng thái cho đệ tử:",
                                new String[]{"Triệu hồi/Đi theo", "Bảo vệ", "Tấn công", "Hợp thể", "Về nhà", "Xóa đệ tử", "Hủy"},
                                new byte[]{-1, -1, -1, -1, -1, -1, -1});
                        dlg871.setHandler(val -> {
                            try {
                                model.DeTu dt = p.detu;
                                if (val == 6) return; // Hủy
                                switch (val) {
                                    case 0: // Triệu hồi / Đi theo
                                        if (dt == null) {
                                            p.getService().send_box_ThongBao_OK("Đệ tử không khả dụng!");
                                            return;
                                        }
                                        dt.setStatus(model.DeTu.STATUS_FOLLOW);
                                        break;
                                    case 1:
                                        if (dt != null) dt.setStatus(model.DeTu.STATUS_PROTECT);
                                        break;
                                    case 2:
                                        if (dt != null) dt.setStatus(model.DeTu.STATUS_ATTACK);
                                        break;
                                    case 3: // Hợp thể
                                        if (dt == null) {
                                            p.getService().send_box_ThongBao_OK("Đệ tử đang về nhà!");
                                            return;
                                        }
                                        dt.setStatus(model.DeTu.STATUS_FUSION);
                                        break;
                                    case 4: // Về nhà
                                        if (dt != null) dt.setStatus(model.DeTu.STATUS_HOME);
                                        break;
                                    case 5: // Xóa đệ tử
                                        model.DeTu.confirmDeleteDeTu(p);
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        p.setyesNoDialog(dlg871);
                        p.getService().startYesNo();
                        return false;
                    }
                    case 872: {
                        zinterfaces.menus.AdminSystemMenu.openItem872Menu(p);
                        return false;
                    }
                    case 101: {
                        return DauTruongTuDo.UseItem(p);
                    }
                    case 105: {
                        return open_orange_by_player_level(p, 105, 0, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Báu Vật");
                    }
                    case 106: {
                        return open_orange_by_player_level(p, 106, 0, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Huyền Thoại");
                    }
                    case 213: {
                        if (EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025)) {
                            SuKienHalloween.gI().OpenHQMQ(p);
                            return true;
                        } else {
                            return open_event_gift_box(p, 213, "Hộp Quà Ma Quái", 5000, 15_000_000, 40_000_000, 30, 100, 3, 5);
                        }
                    }
                    case 330:
                    case 328:
                    case 411:
                    case 412:
                    case 329:
                    case 410: {
                        if (EventManager.isActive(3)) {
                            p.update_pointEvent2(1);
                        }
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 3000, 5_000_000, 15_000_000, 10, 50, 1, 4);
                    }
                    case 207: {
                        if (EventManager.isActive(3)) {
                            EventData temp_select = null;
                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                                if (p.eventData.get(i2).eventID == 3) {
                                    temp_select = p.eventData.get(i2);
                                    break;
                                }
                            }
                            if (temp_select != null) {
                                temp_select.data[4] += 1;
                            }
                        }
                        return open_event_gift_box(p, 207, "Bánh Trung Thu", 3000, 5_000_000, 15_000_000, 10, 50, 1, 3);
                    }
                    case 208: { // Bánh Đậu Xanh: Nhận 1 Đá khảm cấp 1
                        List<GiftBox> list = new ArrayList<>();
                        short[] id_da1 = new short[]{44, 50, 56, 62, 68, 74};
                        short daId = id_da1[ZUtil.random(id_da1.length)];
                        template.GiftBox.addGift(list, 4, daId, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(3)) {
                            EventData temp_select = null;
                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                                if (p.eventData.get(i2).eventID == 3) {
                                    temp_select = p.eventData.get(i2);
                                    break;
                                }
                            }
                            if (temp_select != null) {
                                temp_select.data[4] += 2;
                            }
                        }
                        return true;
                    }
                    case 209: { // Bánh Trứng Muối: Nhận 1 Đá khảm cấp 2
                        List<GiftBox> list = new ArrayList<>();
                        short[] id_da2 = new short[]{45, 51, 57, 63, 69, 75};
                        short daId = id_da2[ZUtil.random(id_da2.length)];
                        template.GiftBox.addGift(list, 4, daId, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(3)) {
                            EventData temp_select = null;
                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                                if (p.eventData.get(i2).eventID == 3) {
                                    temp_select = p.eventData.get(i2);
                                    break;
                                }
                            }
                            if (temp_select != null) {
                                temp_select.data[4] += 3;
                            }
                        }
                        return true;
                    }
                    case 210: { // Bánh Hạt Sen: Nhận 1 Đá khảm cấp 3
                        List<GiftBox> list = new ArrayList<>();
                        short[] id_da3 = new short[]{46, 52, 58, 64, 70, 76};
                        short daId = id_da3[ZUtil.random(id_da3.length)];
                        template.GiftBox.addGift(list, 4, daId, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(3)) {
                            EventData temp_select = null;
                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                                if (p.eventData.get(i2).eventID == 3) {
                                    temp_select = p.eventData.get(i2);
                                    break;
                                }
                            }
                            if (temp_select != null) {
                                temp_select.data[4] += 4;
                            }
                        }
                        return true;
                    }
                    case 408: { // Bánh Đặc Biệt: Nhận 1 Rương Ác Quỷ
                        List<GiftBox> list = new ArrayList<>();
                        template.GiftBox.addGift(list, 4, 29, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(3)) {
                            EventData temp_select = null;
                            for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                                if (p.eventData.get(i2).eventID == 3) {
                                    temp_select = p.eventData.get(i2);
                                    break;
                                }
                            }
                            if (temp_select != null) {
                                temp_select.data[4] += 20;
                            }
                        }
                        return true;
                    }
                    case 211: {
                        short[] ids = new short[]{207, 208, 209, 210};
                        List<GiftBox> list = new ArrayList<>();
                        int rd = ZUtil.random(ids.length);
                        template.GiftBox.addGift(list, 4, ids[rd], 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        return true;
                    }
                    case 735: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                            return false;
                        }
                        EffTemplate eff = p.detu.get_eff_param(8, 1);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian khóa EXP đệ tử đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.detu.add_new_eff(8, 1, (60_000L * 60 * 2));
                        }
                        break;
                    }
                    case 734: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                            return false;
                        }
                        EffTemplate eff = p.detu.get_eff_param(3, 2000);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian X3 EXP đệ tử đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.detu.add_new_eff(3, 2000, (60_000L * 60 * 2));
                        }
                        break;
                    }
                    case 733: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                            return false;
                        }
                        EffTemplate eff = p.detu.get_eff_param(3, 1000);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian X2 EXP đệ tử đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.detu.add_new_eff(3, 1000, (60_000L * 60 * 2));
                        }
                        break;
                    }
                    case 763:
                    case 764:
                    case 765:
                    case 766:
                    case 767:
                    case 768:
                    case 769:
                    case 770:
                    case 779:
                    case 780:
                    case 781:
                    case 782:
                    case 787:
                    case 788:
                    case 795:
                    case 796:
                    case 805:
                    case 824:
                    case 832:
                    case 840:
                    case 842:
                    case 865: {
                        return use_title_badge(p, id, ItemTemplate4.get_item_name(id));
                    }
                    case 349: {
                        long soberi = ZUtil.random(30_000_000, 50_000_000);
                        p.update_vang(soberi);
                        p.updateMoney();
                        p.getService().send_box_ThongBao_OK("Bạn nhận được " + String.format("%,d", soberi) + " Beri!");
                        break;
                    }
                    case 772: {
                        return open_weapon_box(p, 772, 14, false, 3);
                    }
                    case 773: {
                        return open_weapon_box(p, 773, 14, false, 8);
                    }
                    case 774: {
                        return open_weapon_box(p, 774, 14, true, 8);
                    }
                    case 775: {
                        return open_weapon_box(p, 775, 15, true, 8);
                    }
                    case 776:
                    case 777: {
                        return open_weapon_box(p, id, 15, true, 8);
                    }
                    case 827: {
                        return open_orange_by_player_level(p, 827, 15, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Cam +15 Cùng Hệ");
                    }
                    case 828: {
                        return open_orange_by_player_level(p, 828, 15, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Cam +15 Cùng Hệ");
                    }
                    case 829: {
                        return open_orange_by_player_level(p, 829, 15, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Cam +15 Cùng Hệ");
                    }
                    case 830: {
                        return open_orange_by_player_level(p, 830, 14, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Cam +14 Cùng Hệ");
                    }
                    case 831: {
                        return open_orange_by_player_level(p, 831, 13, (50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2), "Rương Cam +13 Cùng Hệ");
                    }
                    case 778: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{130};

                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(778);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 790: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{99, 100, 101};

                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(790);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 791: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{75, 76};

                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(791);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 168:
                    case 227: {
                        if (SuKienNoel.gI().onUseItem(p, id)) {
                            return true;
                        }
                        return false;
                    }
                    case 737: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                            return false;
                        }
                        EffTemplate eff = p.detu.get_eff_param(2, 1000);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian hiệu lực đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.detu.add_new_eff(2, 1000, (60_000L * 60 * 2));
                        }
                        break;
                    }
                    case 738: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                            return false;
                        }
                        EffTemplate eff = p.detu.get_eff_param(2, 2000);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian hiệu lực đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.detu.add_new_eff(2, 2000, (60_000L * 60 * 2));
                        }
                        break;
                    }
                    case 175: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        p.detu.add_new_eff(25, 1000, 60_000L * 30);
                        p.detu.add_new_eff(26, 1000, 60_000L * 30);
                        p.getService().send_box_ThongBao_OK("Đệ tử được tăng tốc hồi 1000 HP/MP mỗi giây trong 30 phút!");
                        break;
                    }
                    case 176: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        p.detu.add_new_eff(25, 500, 60_000L * 30);
                        p.detu.add_new_eff(26, 500, 60_000L * 30);
                        p.getService().send_box_ThongBao_OK("Đệ tử được tăng tốc hồi 500 HP/MP mỗi giây trong 30 phút!");
                        break;
                    }
                    case 178: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        p.detu.add_new_eff(25, 100, 60_000L * 30);
                        p.detu.add_new_eff(26, 100, 60_000L * 30);
                        p.getService().send_box_ThongBao_OK("Đệ tử được tăng tốc hồi 100 HP/MP mỗi giây trong 30 phút!");
                        break;
                    }
                    case 179: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        p.detu.add_new_eff(25, 200, 60_000L * 30);
                        p.detu.add_new_eff(26, 200, 60_000L * 30);
                        p.getService().send_box_ThongBao_OK("Đệ tử được tăng tốc hồi 200 HP/MP mỗi giây trong 30 phút!");
                        break;
                    }
                    case 727: {
                        int clazz = ZUtil.random(5) + 1;
                        short head = 0, hair = -1;
                        DeTu.deleteDeTu(p);
                        if (p.detu != null) {
                            p.map.remove_obj(p.detu.index_map, 0);
                            if (!p.detu.my_pet.isEmpty()) {
                                Set<String> hs = new HashSet<>();
                                for (MyPet mp : p.my_pet) {
                                    hs.add(mp.id + "");
                                }
                                for (MyPet mp : p.detu.my_pet) {
                                    while (hs.contains(mp.id + "")) {
                                        mp.id++;
                                    }
                                    hs.add(mp.id + "");
                                }
                                p.my_pet.addAll(p.detu.my_pet);
                            }
                            p.detu = null;
                        }
                        switch (clazz) {
                            case 1: {
                                hair = 1;
                                break;
                            }
                            case 2: {
                                hair = 24;
                                break;
                            }
                            case 3: {
                                hair = 28;
                                break;
                            }
                            case 4: {
                                hair = 32;
                                break;
                            }
                            case 5: {
                                hair = 36;
                                break;
                            }
                        }
                        try {
                            DeTu pCreate = DeTu.createNewDeTu(p, clazz, head, hair);
                            if (pCreate != null) {
                                if (MessageHandler.isMapDetu(p.map.template.id)) {
                                    Message m_local = new Message(1);
                                    m_local.writer().writeByte(0);
                                    m_local.writer().writeShort(pCreate.index_map);
                                    m_local.writer().writeShort(p.x);
                                    m_local.writer().writeShort(p.y);
                                    p.map.send_msg_all_p(m_local, null, true);
                                    m_local.cleanup();
                                    pCreate.map = p.map;
                                }
                                p.detu = pCreate;
                            } else {
                                p.getService().send_box_ThongBao_OK("Có lỗi xảy ra khi tạo đệ tử");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
                            return false;
                        }
                    }
                    break;
                    case 388:
                    case 387: {
                        if (SuKienTrongCay.gI().onUseItem(p, id)) {
                            return true;
                        }
                        return false;
                    }

                    case 338:
                    case 337:
                    case 336:
                    case 335:
                    case 334: {
                        if (p.level < 5) {
                            p.getService().send_box_ThongBao_OK("Yêu cầu nhân vật đạt cấp độ 5 trở lên!");
                            return false;
                        }
                        List<Skill_info> listSelect = new ArrayList<>();
                        for (int i = 0; i < p.skill_point.size(); i++) {
                            Skill_info sk = p.skill_point.get(i);
                            if (sk != null && sk.temp != null && sk.exp > -1
                                    && (sk.temp.ID >= 0 && sk.temp.ID <= 3)
                                    && ((sk.temp.Lv_RQ < 20 && sk.temp.ID == 3)
                                    || (sk.temp.Lv_RQ < 30 && sk.temp.ID != 3))) {
                                listSelect.add(sk);
                            }
                        }
                        if (listSelect.size() > 0) {
                            Skill_info skillSelect = listSelect.get(ZUtil.random(listSelect.size()));
                            if (skillSelect != null) {
                                long xpReq = ZUtil.random(500, 1_000);
                                if (id == 335) {
                                    xpReq = ZUtil.random(1_000, 1_500);
                                } else if (id == 336) {
                                    xpReq = ZUtil.random(1_500, 2_000);
                                } else if (id == 337) {
                                    xpReq = ZUtil.random(2_000, 3_000);
                                } else if (id == 338) {
                                    xpReq = ZUtil.random(3_000, 5_000);
                                }
                                p.updateExpSkill(skillSelect.temp.ID, xpReq);
                                p.getService().send_box_ThongBao_OK("Dùng " + ItemTemplate4.get_item_name(id) + " thành công!");
                                return true;
                            }
                        }
                        p.getService().send_box_ThongBao_OK("Hiện tại không có kỹ năng nào phù hợp để tăng EXP!");
                        return false;
                    }
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15: {
                        List<GiftBox> list = new ArrayList<>();
                        //
                        if (85 > ZUtil.random(120)) {
                            byte it_color = (byte) ((70 > ZUtil.random(120)) ? 0
                                    : ((20 > ZUtil.random(120)) ? 2 : 1));
                            int bound1;
                            int bound2;
                            ItemTemplate3 template3;
                            switch (id) {
                                case 8: {
                                    bound1 = ((10 / 10) * 192);
                                    bound2 = (((10 / 10) + 1) * 192);
                                    break;
                                }
                                case 9: {
                                    bound1 = ((20 / 10) * 192);
                                    bound2 = (((20 / 10) + 1) * 192);
                                    break;
                                }
                                case 10: {
                                    bound1 = ((30 / 10) * 192);
                                    bound2 = (((30 / 10) + 1) * 192);
                                    break;
                                }
                                case 11: {
                                    bound1 = ((40 / 10) * 192);
                                    bound2 = (((40 / 10) + 1) * 192);
                                    break;
                                }
                                case 12: {
                                    bound1 = ((50 / 10) * 192);
                                    bound2 = (((50 / 10) + 1) * 192);
                                    break;
                                }
                                case 13: {
                                    bound1 = ((60 / 10) * 192);
                                    bound2 = (((60 / 10) + 1) * 192);
                                    break;
                                }
                                case 14: {
                                    bound1 = ((70 / 10) * 192);
                                    bound2 = (((70 / 10) + 1) * 192);
                                    break;
                                }
                                case 15: {
                                    bound1 = ((80 / 10) * 192);
                                    bound2 = (((80 / 10) + 1) * 192);
                                    break;
                                }
                                default: {
                                    bound1 = 0;
                                    bound2 = 192;
                                    break;
                                }
                            }
                            template3 = ItemTemplate3.get_it_by_id(ZUtil.random(bound1, bound2));
                            int id_exact = template3.id;
                            if ((template3.typeEquip == 0 || template3.typeEquip == 1
                                    || template3.typeEquip == 3 || template3.typeEquip == 5)
                                    && template3.clazz != p.clazz && 90 > ZUtil.random(120)) {
                                template3 = ItemTemplate3.get_item_random(template3.typeEquip,
                                        p.clazz, bound1, bound2);
                            }
                            id_exact = template3.id;
                            id_exact -= (template3.color - it_color);
                            template3 = ItemTemplate3.get_it_by_id(id_exact);
                            GiftBox gb1 = new GiftBox();
                            if (template3 != null) {
                                gb1.id = template3.id;
                                gb1.type = 3;
                                gb1.name = template3.name;
                                gb1.icon = template3.icon;
                                gb1.num = 1;
                                gb1.color = template3.color;
                                list.add(gb1);
                            }
                        }
                        //
                        GiftBox gb2 = new GiftBox();
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb2.id = it_temp4.id;
                            gb2.type = 4;
                            gb2.name = it_temp4.name;
                            gb2.icon = it_temp4.icon;
                            gb2.num = ZUtil.random(50, 180);
                            gb2.color = 0;
                            list.add(gb2);
                        }
                        //
                        if (80 > ZUtil.random(120)) {
                            GiftBox gb3 = new GiftBox();
                            it_temp4 = ItemTemplate4.get_it_by_id(ZUtil.random(2, 6));
                            if (it_temp4 != null) {
                                gb3.id = it_temp4.id;
                                gb3.type = 4;
                                gb3.name = it_temp4.name;
                                gb3.icon = it_temp4.icon;
                                gb3.num = ZUtil.random(2, 5);
                                gb3.color = 0;
                                list.add(gb3);
                            }
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Mở khóa rương",
                                    ItemTemplate4.get_item_name(id), list, true);
                        }
                        break;
                    }
                    case 16:
                    case 17: {
                        List<GiftBox> list = new ArrayList<>();
                        //
                        if (85 > ZUtil.random(120)) {
                            byte it_color = (byte) ((70 > ZUtil.random(120)) ? 0
                                    : ((20 > ZUtil.random(120)) ? 2 : 1));
                            ItemTemplate4 temp4;
                            if (id == 16) {
                                temp4 = ItemTemplate4
                                        .get_it_by_id(it_color == 0 ? ZUtil.random(304, 307)
                                                : (it_color == 1 ? ZUtil.random(307, 310)
                                                        : ZUtil.random(310, 313)));
                            } else {
                                temp4 = ItemTemplate4
                                        .get_it_by_id(it_color == 0 ? ZUtil.random(536, 539)
                                                : (it_color == 1 ? ZUtil.random(539, 542)
                                                        : ZUtil.random(542, 545)));
                            }
                            GiftBox gb1 = new GiftBox();
                            gb1.id = temp4.id;
                            gb1.type = 4;
                            gb1.name = temp4.name;
                            gb1.icon = temp4.icon;
                            gb1.num = 1;
                            gb1.color = 0;
                            list.add(gb1);
                        }
                        //
                        GiftBox gb2 = new GiftBox();
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb2.id = it_temp4.id;
                            gb2.type = 4;
                            gb2.name = it_temp4.name;
                            gb2.icon = it_temp4.icon;
                            gb2.num = ZUtil.random(50, 180);
                            gb2.color = 0;
                            list.add(gb2);
                        }
                        //
                        if (80 > ZUtil.random(120)) {
                            GiftBox gb3 = new GiftBox();
                            it_temp4 = ItemTemplate4.get_it_by_id(ZUtil.random(2, 6));
                            if (it_temp4 != null) {
                                gb3.id = it_temp4.id;
                                gb3.type = 4;
                                gb3.name = it_temp4.name;
                                gb3.icon = it_temp4.icon;
                                gb3.num = ZUtil.random(2, 5);
                                gb3.color = 0;
                                list.add(gb3);
                            }
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Mở khóa rương",
                                    ItemTemplate4.get_item_name(id), list, true);
                        }
                        break;
                    }
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26: {
                        List<GiftBox> list = new ArrayList<>();
                        //
                        if (80 > ZUtil.random(120)) {
                            byte it_color = (byte) ((60 > ZUtil.random(120)) ? 1
                                    : ((20 > ZUtil.random(120)) ? 3
                                    : (20 > ZUtil.random(120) ? 0 : 2)));
                            int bound1;
                            int bound2;
                            ItemTemplate3 template3;
                            switch (id) {
                                case 19: {
                                    bound1 = ((10 / 10) * 192);
                                    bound2 = (((10 / 10) + 1) * 192);
                                    break;
                                }
                                case 20: {
                                    bound1 = ((20 / 10) * 192);
                                    bound2 = (((20 / 10) + 1) * 192);
                                    break;
                                }
                                case 21: {
                                    bound1 = ((30 / 10) * 192);
                                    bound2 = (((30 / 10) + 1) * 192);
                                    break;
                                }
                                case 22: {
                                    bound1 = ((40 / 10) * 192);
                                    bound2 = (((40 / 10) + 1) * 192);
                                    break;
                                }
                                case 23: {
                                    bound1 = ((50 / 10) * 192);
                                    bound2 = (((50 / 10) + 1) * 192);
                                    break;
                                }
                                case 24: {
                                    bound1 = ((60 / 10) * 192);
                                    bound2 = (((60 / 10) + 1) * 192);
                                    break;
                                }
                                case 25: {
                                    bound1 = ((70 / 10) * 192);
                                    bound2 = (((70 / 10) + 1) * 192);
                                    break;
                                }
                                case 26: {
                                    bound1 = ((80 / 10) * 192);
                                    bound2 = (((80 / 10) + 1) * 192);
                                    break;
                                }
                                default: {
                                    bound1 = 0;
                                    bound2 = 192;
                                    break;
                                }
                            }
                            template3 = ItemTemplate3.get_it_by_id(ZUtil.random(bound1, bound2));
                            int id_exact = template3.id;
                            if ((template3.typeEquip == 0 || template3.typeEquip == 1
                                    || template3.typeEquip == 3 || template3.typeEquip == 5)
                                    && template3.clazz != p.clazz && 90 > ZUtil.random(120)) {
                                template3 = ItemTemplate3.get_item_random(template3.typeEquip,
                                        p.clazz, bound1, bound2);
                            }
                            id_exact = template3.id;
                            id_exact -= (template3.color - it_color);
                            template3 = ItemTemplate3.get_it_by_id(id_exact);
                            // while (template3.color > it_color) {
                            // id_exact--;
                            // template3 = ItemTemplate3.get_it_by_id(id_exact);
                            // }
                            // while (template3.color < it_color) {
                            // id_exact++;
                            // template3 = ItemTemplate3.get_it_by_id(id_exact);
                            // }
                            GiftBox gb1 = new GiftBox();
                            if (template3 != null) {
                                gb1.id = template3.id;
                                gb1.type = 3;
                                gb1.name = template3.name;
                                gb1.icon = template3.icon;
                                gb1.num = 1;
                                gb1.color = template3.color;
                                list.add(gb1);
                            }
                        }
                        //
                        GiftBox gb2 = new GiftBox();
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb2.id = it_temp4.id;
                            gb2.type = 4;
                            gb2.name = it_temp4.name;
                            gb2.icon = it_temp4.icon;
                            gb2.num = ZUtil.random(100, 500);
                            gb2.color = 0;
                            list.add(gb2);
                        }
                        //
                        if (80 > ZUtil.random(120)) {
                            GiftBox gb3 = new GiftBox();
                            it_temp4 = ItemTemplate4.get_it_by_id(ZUtil.random(2, 6));
                            if (it_temp4 != null) {
                                gb3.id = it_temp4.id;
                                gb3.type = 4;
                                gb3.name = it_temp4.name;
                                gb3.icon = it_temp4.icon;
                                gb3.num = ZUtil.random(3, 6);
                                gb3.color = 0;
                                list.add(gb3);
                            }
                        }
                        if (25 > ZUtil.random(120)) {
                            GiftBox gb4 = new GiftBox();
                            short[] id_random = new short[]{44, 50, 56, 62, 68, 74};
                            it_temp4 = ItemTemplate4
                                    .get_it_by_id(id_random[ZUtil.random(id_random.length)]);
                            if (it_temp4 != null) {
                                gb4.id = it_temp4.id;
                                gb4.type = 4;
                                gb4.name = it_temp4.name;
                                gb4.icon = it_temp4.icon;
                                gb4.num = 1;
                                gb4.color = 0;
                                list.add(gb4);
                            }
                        }
                        if (list.size() > 0) {
                            if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                                return false;
                            }
                            Service.send_gift(p, 1, "Mở khóa rương",
                                    ItemTemplate4.get_item_name(id), list, true);
                            p.updateArchiDaily(52);
                        }
                        break;
                    }
                    case 27:
                    case 28: {
                        List<GiftBox> list = new ArrayList<>();
                        //
                        if (80 > ZUtil.random(120)) {
                            byte it_color;
                            int r = ZUtil.random(120);
                            if (60 > r) {
                                it_color = 1;
                            } else if (20 > ZUtil.random(120)) {
                                it_color = 3;
                            } else if (20 > ZUtil.random(120)) {
                                it_color = 0;
                            } else {
                                it_color = 2;
                            }
                            ItemTemplate4 temp4;
                            if (id == 27) {
                                temp4 = ItemTemplate4
                                        .get_it_by_id(it_color == 0 ? ZUtil.random(304, 307)
                                                        : (it_color == 1 ? ZUtil.random(307, 310)
                                                                : (it_color == 2
                                                                        ? ZUtil.random(310, 313)
                                                                        : ZUtil.random(313, 316))));
                            } else {
                                temp4 = ItemTemplate4
                                        .get_it_by_id(it_color == 0 ? ZUtil.random(536, 539)
                                                        : (it_color == 1 ? ZUtil.random(539, 542)
                                                                : (it_color == 2
                                                                        ? ZUtil.random(542, 545)
                                                                        : ZUtil.random(545, 548))));
                            }
                            GiftBox gb1 = new GiftBox();
                            gb1.id = temp4.id;
                            gb1.type = 4;
                            gb1.name = temp4.name;
                            gb1.icon = temp4.icon;
                            gb1.num = 1;
                            gb1.color = 0;
                            list.add(gb1);
                        }
                        //
                        GiftBox gb2 = new GiftBox();
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb2.id = it_temp4.id;
                            gb2.type = 4;
                            gb2.name = it_temp4.name;
                            gb2.icon = it_temp4.icon;
                            gb2.num = ZUtil.random(100, 500);
                            gb2.color = 0;
                            list.add(gb2);
                        }
                        //
                        if (80 > ZUtil.random(120)) {
                            GiftBox gb3 = new GiftBox();
                            it_temp4 = ItemTemplate4.get_it_by_id(ZUtil.random(2, 6));
                            if (it_temp4 != null) {
                                gb3.id = it_temp4.id;
                                gb3.type = 4;
                                gb3.name = it_temp4.name;
                                gb3.icon = it_temp4.icon;
                                gb3.num = ZUtil.random(3, 6);
                                gb3.color = 0;
                                list.add(gb3);
                            }
                        }
                        if (25 > ZUtil.random(120)) {
                            GiftBox gb4 = new GiftBox();
                            short[] id_random = new short[]{44, 50, 56, 62, 68, 74};
                            it_temp4 = ItemTemplate4
                                    .get_it_by_id(id_random[ZUtil.random(id_random.length)]);
                            if (it_temp4 != null) {
                                gb4.id = it_temp4.id;
                                gb4.type = 4;
                                gb4.name = it_temp4.name;
                                gb4.icon = it_temp4.icon;
                                gb4.num = 1;
                                gb4.color = 0;
                                list.add(gb4);
                            }
                        }
                        if (list.size() > 0) {
                            if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                                return false;
                            }
                            Service.send_gift(p, 1, "Mở khóa rương",
                                    ItemTemplate4.get_item_name(id), list, true);
                            p.updateArchiDaily(52);
                        }
                        break;
                    }
                    case 29: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                            return false;
                        }
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        } else {
                            // 80% Trái Sơ Cấp, 20% Trái Trung Cấp
                            short id_add = (short) ((20 > ZUtil.random(100)) 
                                    ? RandomCollection.getTraiAcQuyTrungCapId() 
                                    : RandomCollection.getTraiAcQuySoCapId());
                            if (!p.item.add_item_bag47(4, id_add, 1)) {
                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                                return false;
                            }
                            open_taq_random(p, 29, id_add, "Rương ác quỷ", "Nhận ngẫu nhiên");
                        }
                        break;
                    }
                    case 189: {
                        if (EventManager.isActive(1)) {
                            p.getService().openDynamicMenu(974, ItemTemplate4.get_item_name(id), new String[]{"Pokemon lửa", "Pokemon cỏ", "Pokemon đá",
                                "Pokemon nước",
                                "Pokemon điện"}, null);
                            return false;
                        }

                    }
                    case 192: {

                        if (EventManager.isActive(1)) {
                            List<GiftBox> list = new ArrayList<>();
                            if (5 > ZUtil.random(120)) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(158);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (20 > ZUtil.random(120)) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id((p.level < 10) ? 112 : (p.level / 10 + 111));
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (20 > ZUtil.random(120)) {
                                short[] ids = new short[]{48, 54, 60, 66, 72, 78};
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (10 > ZUtil.random(120)) {
                                short[] ids = new short[]{48, 54, 60, 66, 72, 78};
//                                ItemFashion itF = ItemFashion.get_item(ids[Util.random(ids.length)]);
//                                if (itF != null) {
//                                    GiftBox gb4 = new GiftBox();
//                                    gb4.id = itF.ID;
//                                    gb4.type = 105;
//                                    gb4.name = itF.name;
//                                    gb4.icon = itF.idIcon;
//                                    gb4.num = 1;
//                                    gb4.color = 0;
//                                    list.add(gb4);
//                                }
                            } else {
                                short[] ids = new short[]{0, 30, 159, 133, 44, 45, 46, 50, 51, 52, 56, 57, 58, 62, 63, 64, 68, 69, 70, 74, 75, 76};

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    if (gb4.id == 0) {
                                        gb4.num = ZUtil.random(10_000, 50_000);
                                    }
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            }

                            if (list.size() > 0) {
                                Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Nhận được",
                                        list, true);
                            }
                        } else {
                            return false;
                        }

                    }
                    break;
                    
                     case 837: {

                        if (EventManager.isActive(3)) {
                            List<GiftBox> list = new ArrayList<>();
                            if (5 > ZUtil.random(120)) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(158);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (20 > ZUtil.random(120)) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id((p.level < 10) ? 112 : (p.level / 10 + 111));
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (20 > ZUtil.random(120)) {
                                short[] ids = new short[]{48, 54, 60, 66, 72, 78};
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            } else if (10 > ZUtil.random(120)) {
                            } else {
                                short[] ids = new short[]{0, 30, 159, 133, 44, 45, 46, 50, 51, 52, 56, 57, 58, 62, 63, 64, 68, 69, 70, 74, 75, 76};

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                                if (itemTemplate4 != null) {
                                    GiftBox gb4 = new GiftBox();
                                    gb4.id = itemTemplate4.id;
                                    gb4.type = 4;
                                    gb4.name = itemTemplate4.name;
                                    gb4.icon = itemTemplate4.icon;
                                    gb4.num = 1;
                                    if (gb4.id == 0) {
                                        gb4.num = ZUtil.random(10_000, 50_000);
                                    }
                                    gb4.color = 0;
                                    list.add(gb4);
                                }
                            }

                            if (list.size() > 0) {
                                Service.send_gift(p, 1, ItemTemplate4.get_item_name(id), "Mâm Bạc",
                                        list, true);
                            }
                             p.update_pointEvent1(1); // Cập nhật điểm sự kiện
                        } else {
                            return false;
                        }

                    }
                    break;
                    

                    
                   
                    
                     case 838: {
                         if (EventManager.isActive(3)) {
                            if (p.conn.status != 1) {
                                p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt. Không thể nhận quà"); 
                                return false; 
                            }

                            // Duyệt qua danh sách người chơi trong bản đồ
                            for (int i = 0; i < p.map.players.size(); i++) {

                                Player targetPlayer = p.map.players.get(i);

                                // Kiểm tra xem người chơi có đủ điều kiện nhận quà hay không
                                 if (targetPlayer.conn.status != 1) {
                                    // Nếu người chơi chưa kích hoạt thì không gửi quà
                                    targetPlayer.getService().send_box_ThongBao_OK("Chưa kích hoạt. không thể nhận quà");
                                    continue; // Không gửi quà cho người chơi này
                                }

                                List<GiftBox> list = new ArrayList<>();
                                {
                                    short[] ids = new short[]{29, 80, 46, 52, 58, 64, 70, 76, 221, 222, 362, 29, 9, 6, 5, 4, 1};
                                    byte[] type = new byte[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 7, 7, 7, 7, 7};
                                    int[] num = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 20};
                                    int rd = ZUtil.random(ids.length);
                                    template.GiftBox.addGift(list, type[rd], ids[rd], num[rd]);

                                    // Thêm các vật phẩm ngẫu nhiên
                                    if (5 > ZUtil.random(500)) {
                                        template.GiftBox.addGift(list, 4, 158, 1);
                                    }
                                }
                                if (list.size() > 0) {
                                    core.RewardService.sendGiftOrMail(targetPlayer, 1, ItemTemplate4.get_item_name(id), "Mâm Vàng", list, true);
                                }
                            }
                            p.update_pointEvent1(1); // Cập nhật điểm sự kiện
                        } else {
                            return false;
                        }
                    }
                    break;

                    case 815:
                    case 816:
                    case 817: { // Bó hoa sự kiện
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 300_000));
                        list.add(new GiftBox(4, 0, 100_000));
                        MainItem giftStone = RandomCollection.getStone(ZUtil.random(1, 4));
                        if (giftStone != null) {
                            template.GiftBox.addGift(list, 4, giftStone.id, 1);
                        }
                        if (ZUtil.random(100) < 20) {
                            template.GiftBox.addGift(list, 4, 29, 1); // Rương Ác Quỷ
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(10) || EventManager.isActive(Event.ID_SUKIEN_8THANG3_2026)) {
                            p.update_pointEvent2(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }

                    case 820: { // Giỏ hoa sự kiện
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 1_000_000));
                        list.add(new GiftBox(4, 0, 500_000));
                        list.add(new GiftBox(4, 1, 10));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(2, 5));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        if (ZUtil.random(100) < 30) {
                            template.GiftBox.addGift(list, 4, 158, 1); // Rương Đại Ác Quỷ
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (p.map != null && p.map.players != null) {
                            for (Player other : p.map.players) {
                                if (other != null && !other.equals(p) && other.conn != null && other.conn.status == 1) {
                                    other.update_exp((long) other.level * 3000L, false);
                                    other.update_vang(50000);
                                    other.updateMoney();
                                    other.getService().send_box_ThongBao_OK("[" + p.name + "] vừa tặng Giỏ Hoa! Bạn nhận được lộc chia vui: " + ZUtil.number_format((long) other.level * 3000L) + " Exp và 50.000 Beri!");
                                }
                            }
                        }
                        if (EventManager.isActive(10) || EventManager.isActive(Event.ID_SUKIEN_8THANG3_2026)) {
                            p.update_pointEvent1(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }
                    
                    case 839: {
                        if (p.conn.status != 1) {
                            p.getService().send_box_ThongBao_OK("Chưa kích hoạt không thể gọi Lân Sư Tử!");
                            return false;
                        }
                        if (!SuKienGioTo.gI().callBoss(p)) {
                            return false;
                        }
                        break;
                    }
					
                    case 803:
                    case 804: { // Hộp kim cương đỏ / tím
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 500_000));
                        list.add(new GiftBox(4, 0, 300_000));
                        list.add(new GiftBox(4, 1, 5));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(3, 6));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        if (ZUtil.random(100) < 25) {
                            template.GiftBox.addGift(list, 4, 158, 1); // Rương Đại Ác Quỷ
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(6) || EventManager.isActive(Event.ID_SUKIEN_30THANG41THANG5_2026)) {
                            p.update_pointEvent1(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }

                    case 785:
                    case 786: { // Bó sen trắng / Bó sen hồng
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 400_000));
                        list.add(new GiftBox(4, 0, 200_000));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(2, 5));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        if (ZUtil.random(100) < 20) {
                            template.GiftBox.addGift(list, 4, 29, 1);
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(Event.ID_SUKIEN_VULAN_2026)) {
                            p.update_pointEvent1(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }

                    case 462: { // Rương kì bí
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 600_000));
                        list.add(new GiftBox(4, 0, 300_000));
                        MainItem stone = RandomCollection.getStone(id == 470 ? ZUtil.random(4, 6) : ZUtil.random(2, 5));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        if (ZUtil.random(100) < 25) {
                            template.GiftBox.addGift(list, 4, 158, 1);
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        p.item.updateInventory(false);
                        break;
                    }

                    case 332: { // Đèn trời
                        sendDenTroi(p);
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 200_000));
                        list.add(new GiftBox(4, 0, 100_000));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(2, 4));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(Event.ID_SUKIEN_TRUNGTHU_2025)) {
                            p.update_pointEvent1(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }

                    case 359: { // Pháo hoa
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 300_000));
                        list.add(new GiftBox(4, 0, 150_000));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(2, 5));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        if (EventManager.isActive(Event.ID_SUKIEN_TETAMLICH_2026)) {
                            p.update_pointEvent1(1);
                        }
                        p.item.updateInventory(false);
                        break;
                    }

                    case 191: { // Vé triệu hồi Pokemon
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 300_000));
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Sử dụng Vé Triệu Hồi Pokemon thành công", list, true);
                        p.item.updateInventory(false);
                        break;
                    }
                    case 190: { // Bộ vật phẩm pokemon
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(99, 0, 250_000));
                        list.add(new GiftBox(4, 0, 100_000));
                        list.add(new GiftBox(4, 1, 5));
                        MainItem stone = RandomCollection.getStone(ZUtil.random(1, 4));
                        if (stone != null) {
                            template.GiftBox.addGift(list, 4, stone.id, 1);
                        }
                        core.RewardService.sendGiftOrMail(p, 1, ItemTemplate4.get_item_name(id), "Nhận được", list, true);
                        p.item.updateInventory(false);
                        break;
                    }
                    case 639:
                    case 640:
                    case 641:
                    case 642: {
                        for (int i = 639; i <= 642; i++) {
                            if (p.item.total_item_bag_by_id(4, i) < 1) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 " + ItemTemplate4.get_item_name(i));
                                return false;
                            }
                        }
                        for (int i = 639; i <= 642; i++) {
                            p.item.remove_item47(4, i, 1);
                        }
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(643);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Sách Haki Quan Sát", "Chúc mừng bạn đã ghép thành công",
                                    list, true);
                            //
                            boolean check = false;
                            for (int i = 0; i < p.skill_point.size(); i++) {
                                if (p.skill_point.get(i).temp.ID == 1015) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                Skill_info newSkill = new Skill_info();
                                newSkill.exp = -1;
                                newSkill.temp = Skill_Template.get_temp(667, newSkill.exp);
                                newSkill.lvdevil = 0;
                                newSkill.devilpercent = 0;
                                p.skill_point.add(newSkill);
                            }
                        }
                        return false;
                    }
                    case 755:
                    case 756:
                    case 757:
                    case 758: {
                        for (int i = 755; i <= 758; i++) {
                            if (p.item.total_item_bag_by_id(4, i) < 1) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 " + ItemTemplate4.get_item_name(i));
                                return false;
                            }
                        }
                        for (int i = 755; i <= 758; i++) {
                            p.item.remove_item47(4, i, 1);
                        }
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(753);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Sách Haki Vũ Trang", "Chúc mừng bạn đã ghép thành công",
                                    list, true);
                            //
                            boolean check = false;
                            for (int i = 0; i < p.skill_point.size(); i++) {
                                if (p.skill_point.get(i).temp.ID == 1016) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                Skill_info newSkill = new Skill_info();
                                newSkill.exp = -1;
                                newSkill.temp = Skill_Template.get_temp(778, newSkill.exp);
                                newSkill.lvdevil = 0;
                                newSkill.devilpercent = 0;
                                p.skill_point.add(newSkill);
                            }
                        }
                        return false;
                    }
                    case 759:
                    case 760:
                    case 761:
                    case 762: {
                        for (int i = 759; i <= 762; i++) {
                            if (p.item.total_item_bag_by_id(4, i) < 1) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 " + ItemTemplate4.get_item_name(i));
                                return false;
                            }
                        }
                        for (int i = 759; i <= 762; i++) {
                            p.item.remove_item47(4, i, 1);
                        }
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(754);
                        if (itemTemplate4 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = itemTemplate4.id;
                            gb4.type = 4;
                            gb4.name = itemTemplate4.name;
                            gb4.icon = itemTemplate4.icon;
                            gb4.num = 1;
                            gb4.color = 0;
                            list.add(gb4);
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Sách Haki Bá Vương", "Chúc mừng bạn đã ghép thành công",
                                    list, true);
                            //
                            boolean check = false;
                            for (int i = 0; i < p.skill_point.size(); i++) {
                                if (p.skill_point.get(i).temp.ID == 1017) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                Skill_info newSkill = new Skill_info();
                                newSkill.exp = -1;
                                newSkill.temp = Skill_Template.get_temp(784, newSkill.exp);
                                newSkill.lvdevil = 0;
                                newSkill.devilpercent = 0;
                                p.skill_point.add(newSkill);
                            }
                        }
                        return false;
                    }
                    case 31: {
                        if (p.pointPk <= 0) {
                            p.getService().send_box_ThongBao_OK("Điểm hiếu chiến của bạn đã bằng 0!");
                            return false;
                        }
                        int diemGiam = 100;
                        if (p.pointPk >= 100) {
                            diemGiam = 1000;
                        }
                        p.update_point_pk(-diemGiam);
                        p.getService().send_box_ThongBao_OK("Dùng vật phẩm giảm " + diemGiam
                                + ". Điểm hiếu chiến hiện tại của bạn là " + p.pointPk);
                        break;
                    }
                    case 40: {
                        p.update_key_boss(1);
                        p.getService().CountDown_Ticket();
                        p.item.updateMoney(false);
                        p.getService().send_box_ThongBao_OK("Số lượng chìa hiện tại: "
                                + p.get_key_boss() + " / " + p.get_key_boss_max());
                        break;
                    }
                    case 158: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                            return false;
                        }
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        } else {
                            // Rương đại ác quỷ: Sơ cấp, Trung cấp (có Tình Yêu), Cao cấp (Sét, Nham Thạch, Chấn Thiên, Bóng Tối). KHÔNG CÓ Ánh Sáng, Nika!
                            short id_add = (short) RandomCollection.getTraiAcQuyDaiAcQuyId();
                            if (!p.item.add_item_bag47(4, id_add, 1)) {
                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                                return false;
                            }
                            if (id_add == 160 || id_add == 161 || id_add == 240 || id_add == 427) {
                                Manager.gI().chatKTG(0,
                                        (p.name + " mở Rương đại ác quỷ nhận được ["
                                        + ItemTemplate4.get_item_name(id_add)
                                        + "] Cao Cấp, thật là may mắn!"),
                                        5);
                            }
                            open_taq_random(p, 158, id_add, "Rương đại ác quỷ", "Nhận ngẫu nhiên");
                        }
                        break;
                    }
                    case 911: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                            return false;
                        }
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        } else {
                            // Rương Siêu Đại Ác Quỷ: Mở từ Trung cấp trở lên (bao gồm Tình Yêu, Sét, Nham Thạch, Chấn Thiên, Bóng Tối). Tỉ lệ thấp ra Ánh Sáng, Nika!
                            short id_add = (short) RandomCollection.getTraiAcQuySieuDaiAcQuyId();
                            if (!p.item.add_item_bag47(4, id_add, 1)) {
                                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                                return false;
                            }
                            if (id_add == 160 || id_add == 161 || id_add == 240 || id_add == 427 || id_add == 869 || id_add == 873) {
                                String rarity = (id_add == 873) ? "Thần Thoại Nika Vô Song" : (id_add == 869 ? "Siêu Cấp Ánh Sáng" : "Cao Cấp");
                                Manager.gI().chatKTG(0,
                                        (p.name + " mở Rương Siêu Đại Ác Quỷ nhận được ["
                                        + ItemTemplate4.get_item_name(id_add)
                                        + "] " + rarity + ", cả server hãy chúc mừng!"),
                                        5);
                            }
                            open_taq_random(p, 911, id_add, "Rương Siêu Đại Ác Quỷ", "Nhận ngẫu nhiên");
                        }
                        break;
                    }
                    case 810: {
                        MainItem pet = RandomCollection.getPet();
                        if (pet != null) {
                            List<GiftBox> list = new ArrayList<>();
                            template.GiftBox.addGift(list, pet.cat, pet.id, 1);
                            core.RewardService.sendGiftOrMail(p, 1, "Rương Pet", "Nhận được thú cưng", list, true);
                        }
                        break;
                    }

                    case 32:
                    case 33:
                    case 34:
                    case 88:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 160:
                    case 161:
                    case 219:
                    case 220:
                    case 240:
                    case 316:
                    case 317:
                    case 318:
                    case 427:
                    case 869:
                    case 870:
                    case 873: {

                        if (p.detu != null) {
                            p.data_yesno = new int[]{91, id};
                            p.setyesNoDialog(new model.YesNoDialog(p, 91, "Thông báo",
                                    "Bạn có muốn sử dụng " + ItemTemplate4.get_it_by_id(id).name + " cho?",
                                    new String[]{"Bản thân", "Đệ tử", "Hủy"}, new byte[]{-1, -1, -1}));
                            p.getService().startYesNo();
                        } else {
                            p.useTAQ = 1;
                            p.setyesNoDialog(new model.YesNoDialog(p, (id + 4000), "Thông báo",
                                    "Bạn có muốn sử dụng " + ItemTemplate4.get_it_by_id(id).name,
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                        }

                        return false;
                    }
                    case 643:
                    case 684: {
                        p.setyesNoDialog(new model.YesNoDialog(p, (4428), "Thông báo",
                                "Bạn có muốn sử dụng " + ItemTemplate4.get_it_by_id(id).name,
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                        return false;
                    }
                    case 80: {
                        return openMenuUseBuffExpItem(p, 80, 2, 2, "Kinh nghiệm X2");
                    }
                    case 86: {
                        p.setyesNoDialog(new model.YesNoDialog(p, 35, "Thông báo",
                                "Bạn có muốn sử dụng Trái Ác Quỷ?", new String[]{"Đồng ý", "Hủy"},
                                new byte[]{-1, -1}));
                        p.getService().startYesNo();
                        return false;
                    }
                    case 87: {
                        p.setyesNoDialog(new model.YesNoDialog(p, 37, "Thông báo",
                                "Bạn có muốn sử dụng Trái Ác Quỷ trung cấp?",
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                        return false;
                    }
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                    case 116:
                    case 117:
                    case 118:
                    case 119:
                    case 120:
                    case 121: {
                        open_box(p, id, ItemTemplate4.get_it_by_id(id).type, (id - 111) * 10);
                        break;
                    }
                    case 122:
                    case 123:
                    case 124:
                    case 125:
                    case 126:
                    case 127:
                    case 128:
                    case 129:
                    case 130:
                    case 131: {
                        open_box(p, id, ItemTemplate4.get_it_by_id(id).type, (id - 121) * 10);
                        break;
                    }
                    case 133: {
                        return openMenuUseBuffExpItem(p, 133, 17, 3, "Vé kinh nghiệm đặc biệt");
                    }
                    case 159: {
                        return openMenuUseBuffExpItem(p, 159, 3, 3, "Xp Chiêu thức");
                    }
//                    case 179: {
//                        EffTemplate eff = p.get_eff(4);
//                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
//                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24
//                                    * 7)) {
//                                eff.time += (1000L * 60 * 5);
//                            }
//                        } else {
//                            p.add_new_eff(4, 50, (60_000L * 5));
//                            //
//                            p.update_info_to_all();
//                        }
//                        break;
//                    }
                    case 726: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp quà đáp lễ");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{62, 58, 60};
                        if (p.clazz == 4) {
                            ids = new short[]{63, 58, 60};
                        }
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(726);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 526: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp quà đáp lễ");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{92, 80};
                        if (p.clazz == 4) {
                            ids = new short[]{92, 81};
                        }
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(526);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 748: {
                        return send_fashion_box_dialog(p, 748, "Hộp trang phục", new short[]{122});
                    }
                    case 749: {
                        return send_fashion_box_dialog(p, 749, "Hộp trang phục", new short[]{123});
                    }
                    case 821: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục Boa Hancock");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{84};

                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(821);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    
                    case 822: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục Nami Wano");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{125};
                        
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(822);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 843: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục Natra");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{56};
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(843);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 844: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục Ngao Bính");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{57};
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(844);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 356: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục sơ");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{23, 55, 53};
                        if (p.clazz == 4) {
                            ids = new short[]{23, 55, 54};
                        }
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(356);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 358: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Thời trang tết 30 ngày");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{49, 51, 78};
                        if (p.clazz == 4) {
                            ids = new short[]{50, 52, 79};
                        }
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            if (itF != null) {
                                m.writer().writeByte(105);
                                m.writer().writeUTF(itF.name);
                                m.writer().writeShort(itF.idIcon);
                                m.writer().writeByte(0);
                                m.writer().writeShort(1);
                                m.writer().writeByte(0);
                            }
                        }
                        m.writer().writeShort(358);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 456: {// vuong item Hộp trang phục trung
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục trung ");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{ 110, 120, 123};
                       
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(456);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 469: {// // vuong item Hộp trang phục cao
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục cao ");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{122, 74};
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(469);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    case 589: {
                        Message m = new Message(69);
                        m.writer().writeUTF("Hộp trang phục Raid Suit");
                        m.writer().writeUTF("Đổi");
                        short[] ids = new short[]{112, 113, 114, 115, 116, 117};
                        m.writer().writeByte(ids.length);
                        for (int i = 0; i < ids.length; i++) {
                            ItemFashion itF = ItemFashion.get_item(ids[i]);
                            m.writer().writeByte(105);
                            m.writer().writeUTF(itF.name);
                            m.writer().writeShort(itF.idIcon);
                            m.writer().writeByte(0);
                            m.writer().writeShort(1);
                            m.writer().writeByte(0);
                        }
                        m.writer().writeShort(589);
                        m.writer().writeByte(4);
                        p.conn.addmsg(m);
                        m.cleanup();
                        return false;
                    }
                    // ruong do cung he
                    case 845: {
                        return open_red_box(p, 2502, 2550, 15, 751, "Rương Đồ Đỏ +15 Lv 10");
                    }
                    case 846: {
                        return open_red_box(p, 2550, 2598, 15, 752, "Rương Đồ Đỏ +15 Lv 20");
                    }
                    case 847: {
                        return open_red_box(p, 2166, 2214, 15, 740, "Rương Đồ Đỏ +15 Lv 30");
                    }
                    case 848: {
                        return open_red_box(p, 2214, 2264, 15, 741, "Rương Đồ Đỏ +15 Lv 40");
                    }
                    case 849: {
                        return open_red_box(p, 2264, 2312, 15, 742, "Rương Đồ Đỏ +15 Lv 50");
                    }
                    case 850: {
                        return open_red_box(p, 2312, 2360, 15, 743, "Rương Đồ Đỏ +15 Lv 60");
                    }
                    case 851: {
                        return open_red_box(p, 2360, 2408, 15, 744, "Rương Đồ Đỏ +15 Lv 70");
                    }
                    case 852: {
                        return open_red_box(p, 2408, 2456, 15, 745, "Rương Đồ Đỏ +15 Lv 80");
                    }
                    case 853: {
                        return open_red_box(p, 2454, 2502, 15, 746, "Rương Đồ Đỏ +15 Lv 90");
                    }
                    case 854: {
                        return open_red_box(p, 2118, 2166, 15, 747, "Rương Đồ Đỏ +15 Lv 100");
                    }
                    case 855: {
                        return open_red_box(p, 2502, 2550, 15, 751, "Rương Đồ Đỏ +15 Lv 10");
                    }
                    case 856: {
                        return open_red_box(p, 2550, 2598, 15, 752, "Rương Đồ Đỏ +15 Lv 20");
                    }
                    case 857: {
                        return open_red_box(p, 2166, 2214, 15, 740, "Rương Đồ Đỏ +15 Lv 30");
                    }
                    case 858: {
                        return open_red_box(p, 2214, 2264, 15, 741, "Rương Đồ Đỏ +15 Lv 40");
                    }
                    case 859: {
                        return open_red_box(p, 2264, 2312, 15, 742, "Rương Đồ Đỏ +15 Lv 50");
                    }
                    case 860: {
                        return open_red_box(p, 2312, 2360, 15, 743, "Rương Đồ Đỏ +15 Lv 60");
                    }
                    case 861: {
                        return open_red_box(p, 2360, 2408, 15, 744, "Rương Đồ Đỏ +15 Lv 70");
                    }
                    case 862: {
                        return open_red_box(p, 2408, 2456, 15, 745, "Rương Đồ Đỏ +15 Lv 80");
                    }
                    case 863: {
                        return open_red_box(p, 2454, 2502, 15, 746, "Rương Đồ Đỏ +15 Lv 90");
                    }
                    case 864: {
                        return open_red_box(p, 2118, 2166, 15, 747, "Rương Đồ Đỏ +15 Lv 100");
                    }

                    case 455: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        int random_color
                                = (10 > ZUtil.random(120)) ? 3 : ((50 > ZUtil.random(120)) ? 2 : 1);
                        for (int i = 0; i < p.skill_point.size(); i++) {
                            if (p.skill_point.get(i).temp.indexSkillInServer == 666) {
                                random_color = (70 > ZUtil.random(100)) ? 3 : 2;
                                break;
                            }
                        }
                        int id_random = 0;
                        switch (random_color) {
                            case 1: {
                                id_random = (5 > ZUtil.random(120)) ? 12012
                                        : ((20 > ZUtil.random(120)) ? 12011
                                        : ((40 > ZUtil.random(120)) ? 12010 : 12009));
                                break;
                            }
                            case 2: {
                                id_random = (5 > ZUtil.random(120)) ? 12008
                                        : ((20 > ZUtil.random(120)) ? 12007
                                        : ((40 > ZUtil.random(120)) ? 12006 : 12005));
                                break;
                            }
                            case 3: {
                                id_random = (5 > ZUtil.random(120)) ? 12004
                                        : ((20 > ZUtil.random(120)) ? 12003
                                        : ((40 > ZUtil.random(120)) ? 12002 : 12001));
                                break;
                            }
                        }
                        //
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate3 itemTemplate3 = ItemTemplate3.get_it_by_id(id_random);
                        if (itemTemplate3 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = (short) id_random;
                            gb4.type = 3;
                            gb4.name = itemTemplate3.name;
                            gb4.icon = itemTemplate3.icon;
                            gb4.num = 1;
                            gb4.color = itemTemplate3.color;
                            list.add(gb4);
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Mở rương Dial", "Nhận được", list, true);
                        }
                        break;
                    }
                    case 823: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        int random_color
                                = (10 > ZUtil.random(120)) ? 3 : ((50 > ZUtil.random(120)) ? 2 : 1);
                        for (int i = 0; i < p.skill_point.size(); i++) {
                            if (p.skill_point.get(i).temp.indexSkillInServer == 666) {
                                random_color = (70 > ZUtil.random(100)) ? 3 : 2;
                                break;
                            }
                        }
                        int id_random = 0;
                        switch (random_color) {
                            case 1: {
                                id_random = (5 > ZUtil.random(120)) ? 12012
                                        : ((20 > ZUtil.random(120)) ? 12012
                                        : ((40 > ZUtil.random(120)) ? 12012 : 12012));
                                break;
                            }
                            case 2: {
                                id_random = (5 > ZUtil.random(120)) ? 12008
                                        : ((20 > ZUtil.random(120)) ? 12008
                                        : ((40 > ZUtil.random(120)) ? 12008 : 12008));
                                break;
                            }
                            case 3: {
                                id_random = (5 > ZUtil.random(120)) ? 12004
                                        : ((20 > ZUtil.random(120)) ? 12004
                                        : ((40 > ZUtil.random(120)) ? 12004 : 12004));
                                break;
                            }
                        }
                        //
                        List<GiftBox> list = new ArrayList<>();
                        ItemTemplate3 itemTemplate3 = ItemTemplate3.get_it_by_id(id_random);
                        if (itemTemplate3 != null) {
                            GiftBox gb4 = new GiftBox();
                            gb4.id = (short) id_random;
                            gb4.type = 3;
                            gb4.name = itemTemplate3.name;
                            gb4.icon = itemTemplate3.icon;
                            gb4.num = 1;
                            gb4.color = itemTemplate3.color;
                            list.add(gb4);
                        }
                        if (list.size() > 0) {
                            Service.send_gift(p, 1, "Mở rương Dial", "Nhận được", list, true);
                        }
                        break;
                    }
                    case 188: {
                        if (EventManager.isActive(1)) {
                            p.getService().openDynamicMenu(973, ItemTemplate4.get_item_name(id), new String[]{"Huy Hiệu lửa", "Huy Hiệu cỏ", "Huy Hiệu đá",
                                "Huy Hiệu nước",
                                "Huy Hiệu điện"}, null);
                            return false;
                        }
                    }
                    break;
                    case 690: {
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        } else {
                            Message m = new Message(69);
                            m.writer().writeUTF("Rương Trái ác quỷ tự chọn");
                            m.writer().writeUTF("Đổi");
                            short[] ids = new short[]{32, 92, 93, 160, 161, 240};
                            m.writer().writeByte(ids.length);
                            for (int i = 0; i < ids.length; i++) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[i]);
                                m.writer().writeByte(4);
                                m.writer().writeUTF(itemTemplate4.name);
                                m.writer().writeShort(itemTemplate4.icon);
                                m.writer().writeByte(0);
                                m.writer().writeShort(1);
                                m.writer().writeByte(0);
                            }
                            m.writer().writeShort(690);
                            m.writer().writeByte(4);
                            p.conn.addmsg(m);
                            m.cleanup();
                            return false;
                        }
                    }
                    case 413: {
                        return openMenuUseItem413(p);
                    }
                    case 415: {
                        return openMenuUseItem415(p);
                    }
                    case 333: {
                        return openMenuUseItem333(p);
                    }
                    case 691: {
                        p.getService().openDynamicMenu(885, ItemTemplate4.get_item_name(id), new String[]{"Cẩm Thạch", "Topaz", "Ruby",
                            "Ngọc lục bảo", "Saphia",
                            "Thạch anh tím", "Hổ phách"}, null);

                        return false;
                    }
                    case 732: {
                        p.getService().openDynamicMenu(886, ItemTemplate4.get_item_name(id), new String[]{"Cẩm Thạch", "Topaz", "Ruby",
                            "Ngọc lục bảo", "Saphia",
                            "Thạch anh tím", "Hổ phách"}, null);

                        return false;
                    }
                    case 271: {
                        if (p.nameNew == null) {
                            p.data_yesno = new int[]{20};
                            new model.InputDialog(p, 20, "Nhập tên mới", new String[]{"Nhập tên mới"}).startInput();
                        }
                        return false;
                    }
                    case 519: {
                        int cat = activities.HanhTrinh.getIslandCategory(p.map.template.id);
                        if (cat >= 0) {
                            List<GiftBox> listGift = new ArrayList<>();
                            int id_random = ZUtil.random(493, 518);
                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id_random);
                            GiftBox giftBox = new GiftBox();
                            giftBox.id = (short) id_random;
                            giftBox.type = 4;
                            giftBox.name = (itemTemplate4 != null && itemTemplate4.name != null) ? itemTemplate4.name : "Đá hành trình";
                            giftBox.icon = (itemTemplate4 != null) ? itemTemplate4.icon : (short) id_random;
                            giftBox.num = 1;
                            giftBox.color = 0;
                            listGift.add(giftBox);
                            core.RewardService.sendGiftOrMail(p, 0, "Rương hành trình", "Phần thưởng", listGift, true);
                            p.flush(p, false);
                        } else {
                            p.getService().send_box_ThongBao_OK("Hãy đứng ở các đảo/làng để mở rương");
                            used = false;
                        }
                        break;
                    }
                    case 548: {
                        EffTemplate eff = p.get_eff(8);
                        if (eff != null && (eff.time > (System.currentTimeMillis() + 3000L))) {
                            if ((eff.time - System.currentTimeMillis()) < (1000L * 60 * 60 * 24 * 30)) {
                                eff.time += (1000L * 60 * 60 * 2);
                            } else {
                                p.getService().send_box_ThongBao_OK("Thời gian khóa EXP đã đạt tối đa (30 ngày)!");
                                return false;
                            }
                        } else {
                            p.add_new_eff(8, 2, (60_000L * 60 * 2));
                        }
                        eff = p.get_eff(8);
                        p.getService().send_box_ThongBao_OK("Thời gian khóa EXP còn lại "
                                + ZUtil.get_time_str_by_sec2(eff.time - System.currentTimeMillis())
                                + "\nCó thể xem lại ở npc Robin, Lưu ý thời gian cộng dồn tối đa 30 ngày");
                        break;
                    }
                    case 414: {
                        return openMenuUseItem414(p);
                    }
                    case 35: {
                        return DauTruongTuDo.UseItem(p);
                    }
                    case 36:
                    case 37:
                    case 38:
                    case 39: {
                        long beri = ZUtil.random(500_000, 2_000_000);
                        p.update_vang(beri);
                        p.updateMoney();
                        p.update_exp(10_000, true);
                        p.getService().send_box_ThongBao_OK("Đã gửi vật phẩm về và nhận " + String.format("%,d", beri) + " Beri + EXP!");
                        break;
                    }
                    case 42: {
                        p.getService().send_box_ThongBao_OK("Bạn đã mở quyền vào khu luyện tập 7 ngày!");
                        break;
                    }
                    case 81:
                    case 82:
                    case 83: {
                        if (p.type_pk == 0) {
                            p.getService().send_box_ThongBao_OK("Không thể sử dụng bình hồi phục ở trạng thái Đồ sát!");
                            return false;
                        }
                        int hpMax = p.ability.get_hp_max(true);
                        int mpMax = p.ability.get_mp_max(true);
                        if (p.hp >= hpMax && p.mp >= mpMax) {
                            p.getService().sendThongBao("HP và MP đã đầy!");
                            return false;
                        }
                        int pct = (id == 81 ? 25 : (id == 82 ? 50 : 100));
                        int healHp = (int) Math.min((long) hpMax - p.hp, (long) hpMax * pct / 100L);
                        int healMp = (int) Math.min((long) mpMax - p.mp, (long) mpMax * pct / 100L);
                        if (healHp < 0) healHp = 0;
                        if (healMp < 0) healMp = 0;
                        p.getService().use_potion(healHp, healMp);
                        p.update_info_to_all();
                        return true;
                    }
                    case 84: {
                        p.add_new_eff(4, 50, 60_000L * 60);
                        p.getService().send_box_ThongBao_OK("Kích hoạt tăng 50% tỉ lệ rơi đồ trong 60 phút!");
                        break;
                    }
                    case 89: {
                        p.getService().send_box_ThongBao_OK("Vé hồi sinh sẵn sàng bảo hộ bạn khi tử trận!");
                        break;
                    }
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100: {
                        return use_food_or_drink(p, id, 3000, 5000, 1000, "Sử dụng " + ItemTemplate4.get_item_name(id) + " thành công!");
                    }
                    case 107:
                    case 109:
                    case 110:
                    case 111: {
                        p.getService().send_box_ThongBao_OK("Nguyên liệu dùng để ghép Lồng Đèn tại NPC Sự Kiện!");
                        return false;
                    }
                    case 132: {
                        return send_fashion_box_dialog(p, 132, "Vé đổi đồ thời trang", (p.clazz == 4) ? new short[]{23, 55, 54} : new short[]{23, 55, 53});
                    }
                    case 136: {
                        int lvl = (p.level < 10) ? 1 : Math.min(100, (p.level / 10) * 10);
                        return open_purple_box(p, 136, lvl, false, "Rương Tím Lv" + lvl);
                    }
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                    case 145:
                    case 146: {
                        int lvl = (id - 136) * 10;
                        return open_purple_box(p, id, lvl, false, "Rương Tím Lv" + lvl);
                    }
                    case 147: {
                        int lvl = (p.level < 10) ? 1 : Math.min(100, (p.level / 10) * 10);
                        return open_purple_box(p, 147, lvl, true, "Rương Anh Hùng Lv" + lvl);
                    }
                    case 148:
                    case 149:
                    case 150:
                    case 151:
                    case 152:
                    case 153:
                    case 154:
                    case 155:
                    case 156:
                    case 157: {
                        int lvl = (id - 147) * 10;
                        return open_purple_box(p, id, lvl, true, "Rương Anh Hùng Lv" + lvl);
                    }
                    case 169: {
                        return open_event_gift_box(p, id, "Bao Lì Xì", 3000, 5_000_000, 20_000_000, 20, 60, 1, 4);
                    }
                    case 170: {
                        return open_event_gift_box(p, id, "Rương Tết", 6000, 15_000_000, 50_000_000, 50, 150, 3, 6);
                    }
                    case 171: {
                        return open_event_gift_box(p, id, "Hộp Quà 8.3", 5000, 15_000_000, 40_000_000, 30, 100, 3, 5);
                    }
                    case 172: {
                        return open_event_gift_box(p, id, "Rương Vua Hùng", 8000, 20_000_000, 60_000_000, 50, 200, 4, 6);
                    }
                    case 177: {
                        if (p.detu == null) {
                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                            return false;
                        }
                        p.detu.add_new_eff(25, 300, 60_000 * 30);
                        p.detu.add_new_eff(26, 300, 60_000 * 30);
                        p.getService().send_box_ThongBao_OK("Sử dụng Dược phẩm cứng cáp thành công!");
                        break;
                    }
                    case 180:
                    case 181:
                    case 182:
                    case 193: {
                        p.update_exp(30_000, true);
                        p.getService().send_box_ThongBao_OK("Ném " + ItemTemplate4.get_item_name(id) + " nhận 30.000 EXP!");
                        break;
                    }
                    case 195: {
                        p.item.remove_item47(4, 195, 1);
                        p.item.add_item_bag47(4, 6, 50); // 50 ổ bánh mì (item4: 6)
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Mở Giỏ Bánh Mì nhận được 50 Bánh Mì!");
                        return true;
                    }
                    case 197: {
                        p.getService().send_box_ThongBao_OK("Phiếu giảm giá 500 Ruby khi mua đồ thời trang tại Cửa Hàng Thời Trang!");
                        return false;
                    }
                    case 212: {
                        p.update_exp(50_000, true);
                        p.getService().send_box_ThongBao_OK("Sử dụng Vé Ma Quỷ nhận 50.000 EXP!");
                        break;
                    }
                    case 214: {
                        p.reset_point(0);
                        p.getService().send_box_ThongBao_OK("Tẩy điểm tiềm năng thành công!");
                        break;
                    }
                    case 215:
                    case 216:
                    case 217:
                    case 218: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 6000, 20_000_000, 60_000_000, 50, 200, 4, 6);
                    }
                    case 228: {
                        return send_fashion_box_dialog(p, 228, "Rương trang phục", new short[]{75, 76});
                    }
                    case 229:
                    case 230: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 3000, 10_000_000, 25_000_000, 20, 80, 2, 4);
                    }
                    case 231: {
                        return open_event_gift_box(p, 231, "Hộp quà sinh nhật", 10000, 50_000_000, 100_000_000, 100, 500, 4, 6);
                    }
                    case 233:
                    case 234:
                    case 235: {
                        return use_food_or_drink(p, id, 2000, 3000, 500, "Sử dụng " + ItemTemplate4.get_item_name(id) + " nhận EXP!");
                    }
                    case 239: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 5000, 20_000_000, 50_000_000, 50, 200, 3, 6);
                    }
                    case 284: {
                        p.getService().send_box_ThongBao_OK("Chìa khóa sắc màu dùng để mở Rương Sắc Màu!");
                        return false;
                    }
                    case 285: {
                        int keyCount = p.item.total_item_bag_by_id(4, 284);
                        if (keyCount < 1) {
                            p.getService().send_box_ThongBao_OK("Bạn cần có Chìa Khóa Sắc Màu để mở Rương Sắc Màu!");
                            return false;
                        }
                        p.item.remove_item47(4, 284, 1);
                        return open_event_gift_box(p, 285, "Rương Sắc Màu", 5000, 10_000_000, 30_000_000, 30, 100, 3, 5);
                    }
                    case 286: {
                        p.item.remove_item47(4, 286, 1);
                        p.item.add_item_bag47(4, 284, 1); // Chìa khóa sắc màu
                        p.item.add_item_bag47(4, 285, 1); // Rương sắc màu
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Mở gói nhận được 1 Rương Sắc Màu và 1 Chìa Khóa Sắc Màu!");
                        return true;
                    }
                    case 287: {
                        ItemFashion itf = ItemFashion.get_item(38);
                        if (itf == null) {
                            itf = ItemFashion.get_item(84);
                        }
                        if (itf != null) {
                            if (p.check_fashion(itf.ID) != null) {
                                p.getService().send_box_ThongBao_OK("Bạn đã sở hữu Thời Trang Râu Trắng rồi!");
                                return false;
                            }
                            p.item.remove_item47(4, 287, 1);
                            ItemFashionP2 temp2 = new ItemFashionP2();
                            temp2.id = itf.ID;
                            temp2.expires = -1;
                            p.fashion.add(temp2);
                            p.update_fashionP2(temp2);
                            p.getService().send_box_ThongBao_OK("Nhận thành công Thời Trang Râu Trắng Vĩnh Viễn!");
                            return true;
                        }
                        p.getService().send_box_ThongBao_OK("Kích hoạt thời trang Râu Trắng!");
                        return false;
                    }
                    case 288:
                    case 289:
                    case 290:
                    case 291:
                    case 292:
                    case 293:
                    case 294:
                    case 295:
                    case 296:
                    case 297:
                    case 298:
                    case 299:
                    case 300:
                    case 301:
                    case 302:
                    case 303:
                    case 552:
                    case 553:
                    case 554:
                    case 555:
                    case 556:
                    case 557:
                    case 558:
                    case 559:
                    case 560:
                    case 561:
                    case 562:
                    case 563:
                    case 564:
                    case 565:
                    case 566:
                    case 567:
                    case 601:
                    case 602:
                    case 603:
                    case 604:
                    case 605:
                    case 606:
                    case 607:
                    case 608: {
                        p.getService().send_box_ThongBao_OK("Đã mở phiếu dự đoán: " + ItemTemplate4.get_item_name(id) + "!");
                        break;
                    }
                    case 304:
                    case 305:
                    case 306:
                    case 307:
                    case 308:
                    case 309:
                    case 310:
                    case 311:
                    case 312:
                    case 313:
                    case 314:
                    case 315: {
                        p.update_exp(50_000, true);
                        p.getService().send_box_ThongBao_OK("Sử dụng " + ItemTemplate4.get_item_name(id) + " nhận 50.000 EXP!");
                        break;
                    }
                    case 320: {
                        return open_event_gift_box(p, 320, "Quà khai giảng", 5000, 15_000_000, 30_000_000, 30, 100, 3, 5);
                    }
                    case 321: { // 6 Đá Khảm: Nhận đủ 6 viên đá khảm cấp 1
                        List<GiftBox> list = new ArrayList<>();
                        short[] id_da1 = new short[]{44, 50, 56, 62, 68, 74};
                        for (short daId : id_da1) {
                            template.GiftBox.addGift(list, 4, daId, 1);
                        }
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "6 Đá Khảm", "Nhận được 6 viên đá khảm cấp 1", list, true);
                        return true;
                    }
                    case 322: {
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
                            return false;
                        }
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        }
                        short id_add = (short) ((30 > ZUtil.random(100)) 
                                ? RandomCollection.getTraiAcQuyTrungCapId() 
                                : RandomCollection.getTraiAcQuySoCapId());
                        if (!p.item.add_item_bag47(4, id_add, 1)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        open_taq_random(p, 326, id_add, "Mở Rương", "Trái Ác Quỷ");
                        break;
                    }
                    case 327: { // Đá Khảm Ngẫu Nhiên: Nhận 1 viên đá khảm cấp 1
                        List<GiftBox> list = new ArrayList<>();
                        short[] id_da1 = new short[]{44, 50, 56, 62, 68, 74};
                        short daId = id_da1[ZUtil.random(id_da1.length)];
                        template.GiftBox.addGift(list, 4, daId, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Đá Khảm Ngẫu Nhiên", "Nhận được 1 viên đá khảm cấp 1", list, true);
                        return true;
                    }
                    case 331: { // Hộp Nguyên Liệu: Nhận 1 giấy dán + 1 nguyên liệu ngẫu nhiên
                        List<GiftBox> list = new ArrayList<>();
                        template.GiftBox.addGift(list, 4, 110, 1); // 1 Giấy dán
                        short[] mats = new short[]{107, 109, 111, 199}; // Đèn cầy, Nang tre, Lồng đen, Bột nếp
                        template.GiftBox.addGift(list, 4, mats[ZUtil.random(mats.length)], 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Hộp Nguyên Liệu", "Nhận được nguyên liệu sự kiện", list, true);
                        return true;
                    }
                    case 339: {
                        p.bua += 1;
                        p.getService().send_box_ThongBao_OK("Nhận thêm 1 Búa sơ cấp!");
                        break;
                    }
                    case 348: {
                        p.update_exp(30_000, true);
                        p.getService().send_box_ThongBao_OK("Triệu hồi Tuyết Nhỏ thành công!");
                        break;
                    }
                    case 350: {
                        return use_food_or_drink(p, 350, 5000, 8000, 2000, "Thưởng thức Bánh Chưng truyền thống nhận EXP!");
                    }
                    case 355: {
                        return open_event_gift_box(p, 355, "Rương Nguyên Liệu Tết", 3000, 10_000_000, 30_000_000, 20, 100, 2, 4);
                    }
                    case 357: {
                        return open_event_gift_box(p, 357, "Bao Lì Xì Canh Tý", 5000, 20_000_000, 50_000_000, 50, 200, 3, 5);
                    }
                    case 360: {
                        p.update_ngoc(10);
                        p.updateMoney();
                        p.getService().send_box_ThongBao_OK("Bạn nhận được 10 Ruby!");
                        break;
                    }
                    case 378:
                    case 379:
                    case 380:
                    case 391:
                    case 392:
                    case 393:
                    case 394:
                    case 395: {
                        return use_food_or_drink(p, id, 2500, 2000, 500, "Ăn " + ItemTemplate4.get_item_name(id) + " hồi phục thể lực và nhận EXP!");
                    }
                    case 381:
                    case 382:
                    case 383:
                    case 384:
                    case 385:
                    case 386:
                    case 389:
                    case 390: {
                        p.update_exp(30_000, true);
                        p.getService().send_box_ThongBao_OK("Sử dụng " + ItemTemplate4.get_item_name(id) + " nhận EXP!");
                        break;
                    }
                    case 396: {
                        return use_food_or_drink(p, 396, 6000, 10000, 2000, "Mở Giỏ Trái Cây nhận lượng lớn EXP và hồi phục thể lực!");
                    }
                    case 397: {
                        p.tichLuy += 50;
                        p.getService().send_box_ThongBao_OK("Nhận 50 Điểm hoạt động!");
                        break;
                    }
                    case 398: {
                        p.getService().send_box_ThongBao_OK("Radar đang quét tín hiệu kho báu trong khu vực!");
                        break;
                    }
                    case 399:
                    case 400:
                    case 401:
                    case 402:
                    case 403:
                    case 404:
                    case 405:
                    case 406: {
                        return use_food_or_drink(p, id, 2000, 1000, 200, "Sử dụng " + ItemTemplate4.get_item_name(id) + " nhận EXP!");
                    }
                    case 407: {
                        return use_food_or_drink(p, 407, 3000, 5000, 1000, "Thưởng thức Bánh Thập Cẩm nhận EXP!");
                    }
                    case 416: {
                        return useItemBuaDucTui(p);
                    }
                    case 417:
                    case 418:
                    case 419:
                    case 420:
                    case 421:
                    case 422:
                    case 423:
                    case 424:
                    case 425:
                    case 426: {
                        return use_food_or_drink(p, id, 3000, 2000, 500, "Sử dụng " + ItemTemplate4.get_item_name(id) + " nhận EXP!");
                    }
                    case 428: {
                        p.update_exp(50_000, true);
                        p.getService().send_box_ThongBao_OK("Sử dụng thành công nhận 50.000 EXP!");
                        break;
                    }
                    case 431:
                    case 432:
                    case 433: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), (id == 431 ? 8000 : (id == 432 ? 5000 : 3000)), 10_000_000, 50_000_000, 20, 100, 2, 5);
                    }
                    case 434:
                    case 435:
                    case 436:
                    case 437:
                    case 438:
                    case 806:
                    case 807:
                    case 808: {
                        return use_food_or_drink(p, id, 3000, 4000, 1000, "Thưởng thức Socola tình yêu ngọt ngào nhận EXP!");
                    }
                    case 439:
                    case 809: {
                        return open_event_gift_box(p, id, "Hộp Socola", 5000, 10_000_000, 30_000_000, 50, 150, 3, 5);
                    }
                    case 440: {
                        return send_fashion_box_dialog(p, 440, "Rương tình yêu", (p.clazz == 4) ? new short[]{63} : new short[]{62});
                    }
                    case 442:
                    case 443:
                    case 444:
                    case 445:
                    case 446:
                    case 447:
                    case 448: {
                        return craft_treasure_map(p);
                    }
                    case 458: {
                        return use_food_or_drink(p, 458, 5000, 6000, 1500, "Thưởng thức Bánh Giò nóng hổi nhận EXP!");
                    }
                    case 459: {
                        return open_event_gift_box(p, 459, "Rương Bí Ẩn", 5000, 20_000_000, 50_000_000, 50, 200, 4, 6);
                    }
                    case 465: {
                        return use_food_or_drink(p, 465, 10000, 20000, 5000, "Ăn Đào Tiên tăng cường tu vi và nhận siêu lượng EXP!");
                    }
                    case 467:
                    case 468: {
                        ItemFashion itf = ItemFashion.get_item(85);
                        if (itf == null) {
                            return false;
                        }
                        ItemFashionP2 temp2 = new ItemFashionP2();
                        temp2.id = itf.ID;
                        temp2.expires = (id == 467) ? (System.currentTimeMillis() + 60_000L * 60 * 24 * 60) : -1;
                        p.fashion.add(temp2);
                        p.update_fashionP2(temp2);
                        p.getService().send_box_ThongBao_OK("Nhận thành công Thời Trang Tôn Ngộ Không!");
                        break;
                    }

                    case 471:
                    case 472: {
                        return use_food_or_drink(p, id, 3000, 5000, 1000, "Thưởng thức " + ItemTemplate4.get_item_name(id) + " nhận EXP!");
                    }
                    case 475: {
                        return send_fashion_box_dialog(p, 475, "Thẻ TT Trung Thu", (p.clazz == 4) ? new short[]{66} : new short[]{65});
                    }
                    case 476: {
                        p.add_new_eff(25, 500, 60_000L * 30);
                        p.getService().send_box_ThongBao_OK("Uống thuốc biến dị tăng cường sức mạnh trong 30 phút!");
                        break;
                    }
                    case 480: {
                        return use_food_or_drink(p, 480, 4000, 3000, 800, "Sử dụng Huy hiệu bí ẩn nhận EXP!");
                    }
                    case 482: {
                        return send_fashion_box_dialog(p, 482, "Hộp trang phục Halloween", new short[]{92, 58, 36});
                    }
                    case 483: { // Rương Ma Quái: 1 trong các Trái Ác Quỷ Cao Cấp (Sét, Nham Thạch, Chấn Thiên, Bóng Tối)
                        List<GiftBox> list = new ArrayList<>();
                        short[] caoCapTAQ = new short[]{160, 161, 240, 427};
                        short taqId = caoCapTAQ[ZUtil.random(caoCapTAQ.length)];
                        template.GiftBox.addGift(list, 4, taqId, 1);
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Rương Ma Quái", "Nhận được Trái Ác Quỷ Cao Cấp", list, true);
                        Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở Rương Ma Quái nhận được [" + ItemTemplate4.get_item_name(taqId) + "]!", 0);
                        return true;
                    }
                    case 484: {
                        return open_event_gift_box(p, id, "Rương Halloween", 5000, 20_000_000, 50_000_000, 50, 200, 3, 6);
                    }
                    case 485: {
                        return open_event_gift_box(p, 485, "Túi Giáng Sinh", 3000, 10_000_000, 30_000_000, 20, 100, 2, 4);
                    }
                    case 489: {
                        return use_food_or_drink(p, 489, 3000, 3000, 1000, "Ăn Kẹo Giáng Sinh nhận EXP!");
                    }
                    case 492: {
                        return open_event_gift_box(p, 492, "Hộp Quà Giáng Sinh", 5000, 20_000_000, 50_000_000, 50, 200, 3, 6);
                    }
                    case 518: {
                        return send_fashion_box_dialog(p, 518, "Hộp trang phục Tết", (p.clazz == 4) ? new short[]{77, 79, 50} : new short[]{77, 78, 49});
                    }
                    case 520: {
                        return send_fashion_box_dialog(p, 520, "Rương TT Franky", new short[]{93, 94});
                    }
                    case 521: {
                        return craft_fragment_item(p, 521, 100, 522, (byte) 4, "Thẻ TT Franky lịch lãm");
                    }
                    case 522: {
                        return send_fashion_box_dialog(p, 522, "Thẻ TT Franky Lịch Lãm", (p.clazz == 4) ? new short[]{94} : new short[]{93});
                    }
                    case 525: {
                        return use_food_or_drink(p, 525, 5000, 8000, 2000, "Thưởng thức Bánh Giầy truyền thống nhận EXP!");
                    }
                    case 530:
                    case 531:
                    case 532:
                    case 533:
                    case 534: {
                        return use_food_or_drink(p, id, 4000, 5000, 1500, "Thưởng thức " + ItemTemplate4.get_item_name(id) + " nhận EXP và năng lượng tươi mát!");
                    }
                    case 535: {
                        return open_event_gift_box(p, 535, "Rương Nguyên Liệu Hè", 3000, 10_000_000, 30_000_000, 20, 100, 2, 4);
                    }
                    case 549:
                    case 550:
                    case 551:
                    case 794: {
                        p.getService().send_box_ThongBao_OK("Vật phẩm dùng để bảo hiểm khi chuyển hóa trang bị tại NPC!");
                        return false;
                    }
                    case 568: {
                        return send_fashion_box_dialog(p, 568, "Thẻ thời trang Euro", new short[]{98});
                    }
                    case 569:
                    case 570: {
                        return use_food_or_drink(p, id, (id == 570 ? 8000 : 4000), 5000, 1000, "Thưởng thức Chè Đậu Đỏ may mắn nhận EXP!");
                    }
                    case 574: {
                        p.getService().send_box_ThongBao_OK("Phiếu giao hàng dùng trong hoạt động vận chuyển!");
                        break;
                    }
                    case 576: { // Hộp Bánh Thượng Hạng: 4 bánh trung thu thượng hạng (207, 208, 209, 210, có tỉ lệ 408)
                        List<GiftBox> list = new ArrayList<>();
                        template.GiftBox.addGift(list, 4, 207, 1);
                        template.GiftBox.addGift(list, 4, 208, 1);
                        template.GiftBox.addGift(list, 4, 209, 1);
                        template.GiftBox.addGift(list, 4, 210, 1);
                        if (15 > ZUtil.random(100)) {
                            template.GiftBox.addGift(list, 4, 408, 1);
                        }
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Hộp Bánh Thượng Hạng", "Nhận được bộ 4 bánh trung thu thượng hạng", list, true);
                        return true;
                    }
                    case 577: {
                        p.update_exp(30_000, true);
                        p.getService().send_box_ThongBao_OK("Ném Quả Cầu Bí Ngô ma quái nhận 30.000 EXP!");
                        break;
                    }
                    case 578: { // Rương Đầu Lâu: 5tr Beri, 50 Ruby, 1 Hải thạch c5, 1 đá khảm lv6 tự chọn, 10 vé x3 skill
                        List<GiftBox> list = new ArrayList<>();
                        list.add(new GiftBox(4, 0, 5_000_000));
                        list.add(new GiftBox(4, 1, 50));
                        list.add(new GiftBox(4, 225, 1));
                        list.add(new GiftBox(4, 588, 1));
                        list.add(new GiftBox(4, 159, 10));
                        if (!core.RewardService.hasEnoughBagSpace(p, list)) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return false;
                        }
                        core.RewardService.sendGiftOrMail(p, 1, "Rương Đầu Lâu", "Nhận được gói quà Rương Đầu Lâu", list, true);
                        return true;
                    }
                    case 581: {
                        return send_fashion_box_dialog(p, 581, "Thẻ TT Chopper khổng lồ", new short[]{102});
                    }
                    case 582:
                    case 583:
                    case 584:
                    case 585:
                    case 586:
                    case 587: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 4000, 10_000_000, 30_000_000, 30, 100, 3, 5);
                    }
                    case 588: {
                        p.getService().openDynamicMenu(885, ItemTemplate4.get_item_name(id), new String[]{"Cẩm Thạch", "Topaz", "Ruby",
                            "Ngọc lục bảo", "Saphia",
                            "Thạch anh tím", "Hổ phách"}, null);
                        return false;
                    }
                    case 590:
                    case 591: {
                        if (EventManager.isActive(Event.ID_SUKIEN_20THANG10_2025)) {
                            SuKien20Thang10.gI().sendMenu(p, -881);
                        } else {
                            p.getService().send_box_ThongBao_OK("Vật phẩm sự kiện 20 Tháng 10!");
                        }
                        return true;
                    }
                    case 592:
                    case 593:
                    case 594:
                    case 595:
                    case 596: {
                        return open_hop_qua_20_10(p, id);
                    }
                    case 600: {
                        return send_fashion_box_dialog(p, 600, "Thẻ TT Trọng Tài WC", new short[]{121});
                    }
                    case 609: {
                        return open_event_gift_box(p, 609, "Rương Worldcup", 5000, 20_000_000, 50_000_000, 50, 200, 3, 6);
                    }
                    case 610: {
                        return open_event_gift_box(p, 610, "Cúp Worldcup", 10000, 50_000_000, 100_000_000, 200, 500, 5, 6);
                    }
                    case 611: {
                        p.update_exp(20_000, true);
                        p.getService().send_box_ThongBao_OK("Ném bóng tuyết mát lạnh nhận 20.000 EXP!");
                        break;
                    }
                    case 613: {
                        return open_event_gift_box(p, 613, "Tất Noel", 3000, 10_000_000, 30_000_000, 20, 100, 2, 4);
                    }
                    case 621: {
                        return send_fashion_box_dialog(p, 621, "Thẻ TT Argentina", new short[]{98});
                    }
                    case 622: {
                        return send_fashion_box_dialog(p, 622, "Rương trang phục Noel", new short[]{75, 76});
                    }
                    case 628: {
                        p.getService().send_box_ThongBao_OK("Đã xóa bỏ trạng thái phản sư phụ!");
                        break;
                    }
                    case 629:
                    case 635: {
                        return use_food_or_drink(p, id, 3000, 3000, 1000, "Sử dụng " + ItemTemplate4.get_item_name(id) + " đón xuân nhận EXP!");
                    }
                    case 637: {
                        return send_fashion_box_dialog(p, 637, "Hộp trang phục Tết 1", (p.clazz == 4) ? new short[]{50, 52} : new short[]{49, 51});
                    }
                    case 638: {
                        return send_fashion_box_dialog(p, 638, "Hộp trang phục Tết 2", (p.clazz == 4) ? new short[]{77, 79} : new short[]{77, 78});
                    }
                    case 644:
                    case 645:
                    case 646:
                    case 683: {
                        p.getService().send_box_ThongBao_OK("Đá đặc biệt dùng để nâng cấp trang bị tại NPC thợ rèn!");
                        return false;
                    }
                    case 685: {
                        return use_pet_direct(p, 685, 685, "Khuyển Kinh Nghiệm");
                    }
                    case 686: {
                        return use_pet_direct(p, 686, 686, "Khuyển Kỹ Năng");
                    }
                    case 687: {
                        return use_pet_direct(p, 687, 687, "Khuyển Cao Cấp");
                    }
                    case 688: {
                        return use_pet_direct(p, 688, 688, "Khuyển Siêu Cấp");
                    }
                    case 689: {
                        return use_pet_direct(p, 689, 689, "Pet Thần Hỏa Prometheus");
                    }
                    case 695:
                    case 696:
                    case 697:
                    case 698:
                    case 699:
                    case 700:
                    case 701:
                    case 702:
                    case 703:
                    case 704: {
                        return open_red_box(p, id, (id - 694) * 10, false);
                    }
                    case 751:
                    case 752: {
                        return open_red_box(p, id, (id == 751 ? 10 : 20), true);
                    }
                    case 740:
                    case 741:
                    case 742:
                    case 743:
                    case 744:
                    case 745:
                    case 746:
                    case 747: {
                        return open_red_box(p, id, (id - 737) * 10, true);
                    }
                    case 728: {
                        return use_pet_direct(p, 728, 690, "Black Zeus");
                    }
                    case 729: {
                        return use_pet_direct(p, 729, 695, "Tuần Lộc KuTo");
                    }
                    case 730: {
                        return use_pet_direct(p, 730, 698, "Lửa Phượng Hoàng");
                    }
                    case 731: {
                        return use_pet_direct(p, 731, 697, "Thần Chết");
                    }
                    case 736: {
                        return send_fashion_box_dialog(p, 736, "Hộp trang phục Zombie", new short[]{120});
                    }
                    case 739: {
                        return send_fashion_box_dialog(p, 739, "Hộp trang phục sự kiện", new short[]{65, 66, 70, 72});
                    }
                    case 750: {
                        return send_fashion_box_dialog(p, 750, "Hộp trang phục ACE", new short[]{2});
                    }
                    case 753: {
                        p.getService().send_box_ThongBao_OK("Sách Haki Vũ Trang đã sẵn sàng để nâng cấp!");
                        break;
                    }
                    case 754: {
                        p.getService().send_box_ThongBao_OK("Sách Haki Bá Vương đã sẵn sàng để nâng cấp!");
                        break;
                    }
                    case 771: {
                        p.getService().send_box_ThongBao_OK("Vé dùng để đổi kiểu tóc đặc biệt tại NPC Salon!");
                        return false;
                    }
                    case 783:
                    case 784: {
                        return use_food_or_drink(p, id, 4000, 5000, 1000, "Dâng hoa sen Vu Lan nhận EXP và phúc lành!");
                    }
                    case 789: {
                        return open_event_gift_box(p, 789, "Cây Thông Noel", 5000, 20_000_000, 50_000_000, 50, 150, 3, 5);
                    }
                    case 792: {
                        return send_fashion_box_dialog(p, 792, "Hộp trang phục Jinbei", new short[]{129});
                    }
                    case 793: {
                        return send_fashion_box_dialog(p, 793, "Hộp trang phục Tiểu Thư Vivi", new short[]{130});
                    }
                    case 797:
                    case 798:
                    case 799:
                    case 800: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 5000, 15_000_000, 40_000_000, 30, 100, 3, 5);
                    }
                    case 801:
                    case 802: {
                        p.getService().send_box_ThongBao_OK("Vé vòng quay may mắn đã sẵn sàng!");
                        break;
                    }
                    case 825: {
                        return use_pet_direct(p, 825, 720, "Rồng Lửa Baby");
                    }
                    case 826: {
                        return use_pet_direct(p, 826, 726, "Kibi Xanh");
                    }
                    case 841: {
                        p.getService().send_box_ThongBao_OK("Thẻ bài hang động dùng để tham gia thử thách vượt Ải Hang Động!");
                        return false;
                    }
                    case 876:
                    case 877: {
                        if (event.EventManager.isActive(event.Event.ID_SUKIEN_BIGMOM)) {
                            return false;
                        }
                        return use_food_or_drink(p, id, (id == 877 ? 12000 : 6000), 15000, 5000, "Thưởng thức Bánh Kem Big Mom nhận siêu lượng EXP!");
                    }
                    case 879: {
                        return craft_fragment_item(p, 879, 10, 691, (byte) 110, "Tiểu Merry");
                    }
                    case 885: {
                        return use_food_or_drink(p, 885, 10000, 50000, 50000, "Sử dụng Viên Kẹo Thần Kỳ nhận siêu lượng EXP và phục hồi thể lực!");
                    }
                    case 887: {
                        return send_fashion_box_dialog(p, 887, "Hộp Quà Euro 2024", new short[]{98});
                    }
                    case 890: {
                        return open_event_gift_box(p, 890, "Bó Hoa Rực Rỡ", 5000, 20_000_000, 50_000_000, 50, 200, 3, 5);
                    }
                    case 891:
                    case 892:
                    case 893: {
                        return open_event_gift_box(p, id, ItemTemplate4.get_item_name(id), 3000, 5_000_000, 15_000_000, 10, 50, 2, 4);
                    }
                    case 894: {
                        if (p.check_already_have_devil_fruit()) {
                            p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                            return false;
                        } else {
                            Message m = new Message(69);
                            m.writer().writeUTF("Rương Trái ác quỷ cao cấp tự chọn");
                            m.writer().writeUTF("Đổi");
                            short[] ids = new short[]{160, 161, 240, 427, 869, 870, 873};
                            m.writer().writeByte(ids.length);
                            for (int i = 0; i < ids.length; i++) {
                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(ids[i]);
                                m.writer().writeByte(4);
                                m.writer().writeUTF(itemTemplate4 != null ? itemTemplate4.name : "Trái Ác Quỷ");
                                m.writer().writeShort(itemTemplate4 != null ? itemTemplate4.icon : (short) 0);
                                m.writer().writeByte(0);
                                m.writer().writeShort(1);
                                m.writer().writeByte(0);
                            }
                            m.writer().writeShort(894);
                            m.writer().writeByte(4);
                            p.conn.addmsg(m);
                            m.cleanup();
                            return false;
                        }
                    }
                    case 895: {
                        return craft_fragment_item(p, 895, 10, 695, (byte) 110, "Pet Chopper Giáng Sinh");
                    }
                    case 903: {
                        return craft_fragment_item(p, 903, 10, 721, (byte) 110, "Pet Capybara");
                    }
                    case 904: {
                        return craft_fragment_item(p, 904, 10, 722, (byte) 110, "Pet Lobby");
                    }
                    case 905: {
                        return craft_fragment_item(p, 905, 10, 723, (byte) 110, "Pet Lucci");
                    }
                    case 906: {
                        return send_fashion_box_dialog(p, 906, "Hộp Thời Trang Kyros", new short[]{131, 0});
                    }
                    case 907: {
                        return send_fashion_box_dialog(p, 907, "Hộp Thời Trang Rebecca", new short[]{1, 125});
                    }
                    case 908: {
                        p.getService().send_box_ThongBao_OK("Extol - Đơn vị tiền tệ cổ xưa tại vùng đất Skypiea!");
                        return false;
                    }
                    case 910: {
                        p.getService().openDynamicMenu(885, ItemTemplate4.get_item_name(id), new String[]{"Cẩm Thạch", "Topaz", "Ruby",
                            "Ngọc lục bảo", "Saphia",
                            "Thạch anh tím", "Hổ phách"}, null);
                        return false;
                    }
                    default: {
                        p.getService().send_box_ThongBao_OK("Hiện tại "
                                + ItemTemplate4.get_item_name(id) + " chưa sử dụng được");
                        return false;
                    }
                }
                return used;
            }
        } else {
            p.getService().send_box_ThongBao_OK("Vật phẩm lỗi, hãy báo cho admin");
            return false;
        }
        return false;
    }

    private static void open_taq_random(Player p, int boxId, int id, String name1, String name2)
            throws IOException {
        Message m = new Message(-34);
        m.writer().writeByte(21);
        m.writer().writeShort(boxId);
        m.writer().writeUTF(name1);
        m.writer().writeUTF(name2);
        m.writer().writeByte(1);
        ItemTemplate4 it_temp = ItemTemplate4.get_it_by_id(id);
        m.writer().writeByte(4);
        m.writer().writeUTF(it_temp != null ? it_temp.name : "");
        m.writer().writeShort(it_temp != null ? it_temp.icon : 0);
        m.writer().writeInt(1);
        m.writer().writeByte(0);
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static boolean open_purple_box(Player p, int boxId, int targetLevel, boolean sameClass, String customTitle) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
            return false;
        }
        int lvl = targetLevel;
        if (lvl <= 0) {
            lvl = (p.level < 10) ? 1 : Math.min(100, (p.level / 10) * 10);
        }
        List<ItemTemplate3> listMatch = new ArrayList<>();
        for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
            if (it3.color == 2 && it3.typeEquip < 6 && !it3.isThanTrang() && it3.level == lvl) {
                if (sameClass && it3.clazz != 0 && it3.clazz != p.clazz) continue;
                listMatch.add(it3);
            }
        }
        if (listMatch.isEmpty()) {
            for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
                if (it3.color == 2 && it3.typeEquip < 6 && !it3.isThanTrang() && it3.level == lvl) {
                    listMatch.add(it3);
                }
            }
        }
        if (!listMatch.isEmpty()) {
            ItemTemplate3 chosen = listMatch.get(ZUtil.random(listMatch.size()));
            Item_wear temp = new Item_wear();
            temp.setup_template_by_id(chosen.id);
            temp.levelUp = 0;
            temp.numLoKham = (byte) ((50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2));
            if (temp.template != null) {
                p.item.add_item_bag3(temp);
            }
            List<Item_wear> listReceiv = new ArrayList<>();
            listReceiv.add(temp);
            String title = (customTitle != null && !customTitle.isEmpty()) ? customTitle : ItemTemplate4.get_item_name(boxId);
            if (title == null || title.isEmpty()) {
                title = (sameClass ? "Rương Anh Hùng Lv" : "Rương Tím Lv") + lvl;
            }
            Service.open_box_item3_orange(p, listReceiv, boxId, "Mở Khóa Rương", title);
            return true;
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị tím phù hợp!");
            return false;
        }
    }

    public static boolean open_red_box(Player p, int boxId, int targetLevel, boolean sameClass, int levelUp, String customTitle) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
            return false;
        }
        List<ItemTemplate3> listMatch = new ArrayList<>();
        for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
            if (it3.color == 8 && it3.typeEquip < 6 && !it3.isThanTrang() && it3.id < 2598 && it3.level == targetLevel) {
                if (sameClass && it3.clazz != 0 && it3.clazz != p.clazz) continue;
                listMatch.add(it3);
            }
        }
        if (listMatch.isEmpty()) {
            for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
                if (it3.color == 8 && it3.typeEquip < 6 && !it3.isThanTrang() && it3.id < 2598 && it3.level == targetLevel) {
                    listMatch.add(it3);
                }
            }
        }
        if (!listMatch.isEmpty()) {
            ItemTemplate3 chosen = listMatch.get(ZUtil.random(listMatch.size()));
            Item_wear temp = new Item_wear();
            temp.setup_template_by_id(chosen.id);
            temp.levelUp = (byte) Math.max(0, Math.min(15, levelUp));
            temp.numLoKham = (byte) ((50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2));
            if (temp.template != null) {
                p.item.add_item_bag3(temp);
            }
            List<Item_wear> listReceiv = new ArrayList<>();
            listReceiv.add(temp);
            String title = (customTitle != null && !customTitle.isEmpty()) ? customTitle : ItemTemplate4.get_item_name(boxId);
            if (title == null || title.isEmpty()) {
                title = "Rương Đồ Đỏ Lv" + targetLevel;
            }
            Service.open_box_item3_orange(p, listReceiv, boxId, "Mở Khóa Rương", title);
            return true;
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị phù hợp!");
            return false;
        }
    }

    public static boolean open_red_box(Player p, int bound1, int bound2, int levelUp, int iconEff, String title) throws IOException {
        int targetLevel = 10;
        if (bound1 >= 2502 && bound1 < 2550) targetLevel = 10;
        else if (bound1 >= 2550 && bound1 < 2598) targetLevel = 20;
        else if (bound1 >= 2166 && bound1 < 2214) targetLevel = 30;
        else if (bound1 >= 2214 && bound1 < 2264) targetLevel = 40;
        else if (bound1 >= 2264 && bound1 < 2312) targetLevel = 50;
        else if (bound1 >= 2312 && bound1 < 2360) targetLevel = 60;
        else if (bound1 >= 2360 && bound1 < 2408) targetLevel = 70;
        else if (bound1 >= 2408 && bound1 < 2456) targetLevel = 80;
        else if (bound1 >= 2454 && bound1 < 2502) targetLevel = 90;
        else if (bound1 >= 2118 && bound1 < 2166) targetLevel = 100;
        return open_red_box(p, iconEff, targetLevel, true, levelUp, title);
    }

    public static boolean open_orange_by_player_level(Player p, int boxId, int levelUp, int numLoKham, String title) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
            return false;
        }
        int targetLevel = (p.level < 10) ? 1 : Math.min(100, (p.level / 10) * 10);
        List<ItemTemplate3> listRandom = new ArrayList<>();
        for (ItemTemplate3 itemTemplate3 : ItemTemplate3.ENTRYS) {
            if (itemTemplate3.color == 3 && itemTemplate3.typeEquip < 6 && (itemTemplate3.clazz == 0 || itemTemplate3.clazz == p.clazz)) {
                if (itemTemplate3.level == targetLevel) {
                    listRandom.add(itemTemplate3);
                }
            }
        }
        if (listRandom.isEmpty()) {
            for (ItemTemplate3 itemTemplate3 : ItemTemplate3.ENTRYS) {
                if (itemTemplate3.color == 3 && itemTemplate3.typeEquip < 6) {
                    if (itemTemplate3.level == targetLevel) {
                        listRandom.add(itemTemplate3);
                    }
                }
            }
        }
        if (listRandom.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị phù hợp với cấp độ và hệ của bạn.");
            return false;
        }
        int id_add = listRandom.get(ZUtil.random(listRandom.size())).id;
        if (id_add >= 0) {
            Item_wear temp = new Item_wear();
            temp.setup_template_by_id(id_add);
            temp.levelUp = (byte) Math.max(0, Math.min(15, levelUp));
            temp.numLoKham = (byte) numLoKham;
            if (temp.template != null) {
                p.item.add_item_bag3(temp);
            }
            List<Item_wear> list_receiv = new ArrayList<>();
            list_receiv.add(temp);
            Service.open_box_item3_orange(p, list_receiv, boxId, "Mở Khóa Rương", title != null ? title : "Rương Đồ Cam");
            return true;
        } else {
            p.getService().send_box_ThongBao_OK("Lỗi, hãy thử lại");
            return false;
        }
    }

    public static boolean open_weapon_red(Player p, int levelUp, boolean checkLevel, String title) throws IOException {
        return open_weapon_box(p, 774, levelUp, true, 8);
    }

    private static boolean open_box(Player p, int boxId, byte type, int level) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
            return false;
        }
        boolean sameClass = (type == 23) || (90 > ZUtil.random(120));
        List<ItemTemplate3> listRandom = new ArrayList<>();
        for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
            if (it3.color == 3 && it3.typeEquip < 6 && it3.level == level) {
                if (sameClass && it3.clazz != 0 && it3.clazz != p.clazz) continue;
                listRandom.add(it3);
            }
        }
        if (listRandom.isEmpty()) {
            for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
                if (it3.color == 3 && it3.typeEquip < 6 && it3.level == level) {
                    listRandom.add(it3);
                }
            }
        }
        if (!listRandom.isEmpty()) {
            ItemTemplate3 chosen = listRandom.get(ZUtil.random(listRandom.size()));
            Item_wear temp = new Item_wear();
            temp.setup_template_by_id(chosen.id);
            temp.numLoKham = (byte) ((50 > ZUtil.random(120)) ? 0 : (70 > ZUtil.random(120) ? 1 : 2));
            if (temp.template != null) {
                p.item.add_item_bag3(temp);
            }
            List<Item_wear> list_receiv = new ArrayList<>();
            list_receiv.add(temp);
            String title = (type == 23 ? "Rương Đồ Cam Cùng Hệ Lv" : "Rương Đồ Cam Lv") + level;
            Service.open_box_item3_orange(p, list_receiv, boxId, "Mở Khóa Rương", title);
            return true;
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị phù hợp!");
            return false;
        }
    }
    
   

    private static void use_item_3(Player p, int id) throws IOException {
        if (p == null || p.item == null || p.item.bag3 == null) {
            return;
        }
        if (p.trade_target != null) {
            if (p.getService() != null) {
                p.getService().send_box_ThongBao_OK("Không thể mặc trang bị khi đang giao dịch!");
            }
            return;
        }
        Item_wear it = null;
        if (id >= 0 && id < p.item.bag3.length) {
            it = p.item.bag3[id];
        }
        if (it == null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                if (p.item.bag3[i] != null && p.item.bag3[i].index == id) {
                    it = p.item.bag3[i];
                    id = i;
                    break;
                }
            }
        }
        if (it == null || it.template == null) {
            return;
        }
        final int targetBagSlot = id;
        it.index = (short) targetBagSlot;

        if (check_it_can_wear(it.template.typeEquip)) {
            if (p.detu != null) {
                p.data_yesno = new int[]{87, targetBagSlot};
                p.use_item_3 = targetBagSlot;
                YesNoDialog diag87 = new YesNoDialog(p, 87, "Thông báo",
                        "Sử dụng vật phẩm này cho?",
                        new String[]{"Bản thân", "Đệ tử", "Hủy"}, new byte[]{-1, -1, -1}, (byte val) -> {
                    if (val == 0) { // Bản thân
                        Item_wear itCur = (targetBagSlot >= 0 && targetBagSlot < p.item.bag3.length) ? p.item.bag3[targetBagSlot] : null;
                        if (itCur != null) {
                            p.use_item_3 = targetBagSlot;
                            if (itCur.typelock == 1 || itCur.valueKichAn == 12) {
                                p.wear_item(itCur);
                            } else {
                                String itemName = (itCur.template != null) ? itCur.template.name : "Trang bị";
                                YesNoDialog diag0 = new YesNoDialog(p, 0, "Thông báo",
                                        "Khi trang bị lên người vật phẩm "
                                        + itemName
                                        + " sẽ chuyển sang trạng thái khóa không thể giao dịch. "
                                        + "Bạn có muốn trang bị?",
                                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, (byte val0) -> {
                                    if (val0 == 0) {
                                        Item_wear itWear = (targetBagSlot >= 0 && targetBagSlot < p.item.bag3.length) ? p.item.bag3[targetBagSlot] : null;
                                        if (itWear != null && check_it_can_wear(itWear.template.typeEquip)) {
                                            p.wear_item(itWear);
                                        }
                                    }
                                    p.use_item_3 = -1;
                                });
                                diag0.startYesNo();
                                return;
                            }
                        }
                    } else if (val == 1) { // Đệ tử
                        if (p.detu != null) {
                            Item_wear itCur = (targetBagSlot >= 0 && targetBagSlot < p.item.bag3.length) ? p.item.bag3[targetBagSlot] : null;
                            if (itCur != null && itCur.template != null) {
                                if (p.detu.level < itCur.template.level) {
                                    if (p.getService() != null) {
                                        p.getService().send_box_ThongBao_OK("Đệ tử chưa đủ level để mặc trang bị này!");
                                    }
                                    return;
                                }
                                if (itCur.template.clazz != 0 && p.detu.clazz != itCur.template.clazz) {
                                    if (p.getService() != null) {
                                        p.getService().send_box_ThongBao_OK("Đệ tử không phù hợp hệ phái để mặc trang bị này!");
                                    }
                                    return;
                                }
                                p.detu.wear_item(itCur);
                                p.detu.updateParts();
                                p.detu.setAbility();
                                if (p.isFusion) {
                                    p.detu.applyFusion();
                                    p.update_info_to_all();
                                }
                                p.item.updateInventory(false);
                                if (p.getService() != null) {
                                    p.getService().send_box_ThongBao_OK("Đã mặc " + itCur.template.name + " cho Đệ tử thành công!");
                                }
                            }
                        }
                    }
                    p.use_item_3 = -1;
                    p.data_yesno = null;
                });
                diag87.startYesNo();
            } else {
                p.use_item_3 = targetBagSlot;
                if (it.typelock == 1 || it.valueKichAn == 12) {
                    p.wear_item(it);
                } else {
                    String itemName = (it.template != null) ? it.template.name : "Trang bị";
                    YesNoDialog diag0 = new YesNoDialog(p, 0, "Thông báo",
                            "Khi trang bị lên người vật phẩm "
                            + itemName
                            + " sẽ chuyển sang trạng thái khóa không thể giao dịch. "
                            + "Bạn có muốn trang bị?",
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, (byte val0) -> {
                        if (val0 == 0) {
                            Item_wear itWear = (targetBagSlot >= 0 && targetBagSlot < p.item.bag3.length) ? p.item.bag3[targetBagSlot] : null;
                            if (itWear != null && check_it_can_wear(itWear.template.typeEquip)) {
                                p.wear_item(itWear);
                            }
                        }
                        p.use_item_3 = -1;
                    });
                    diag0.startYesNo();
                }
            }
        } else {
            p.getService().send_box_ThongBao_OK("Chưa có chức năng");
        }
    }

    public static boolean check_it_can_wear(byte type) {
        return type >= 0 && type < itemz.Item.MAX_BODY;
    }

    public static boolean use_item_potion(Player p, int id) throws IOException {
        if (p == null || p.isdie) return false;
        int numInBag = p.item.total_item_bag_by_id(4, id);
        if (numInBag > 0) {
            if (use_item_4(p, id)) {
                if (id != 271) {
                    if (numInBag == 1 && (id == 180 || id == 181 || id == 182)) {
                        Message mIcon = new Message(-15);
                        mIcon.writer().writeByte(15);
                        mIcon.writer().writeByte(4);
                        mIcon.writer().writeShort(id);
                        mIcon.writer().writeByte(0);
                        p.addmsg(mIcon);
                        mIcon.cleanup();
                    }
                    p.item.remove_item47(4, id, 1);
                }
                Message m2 = new Message(-13);
                m2.writer().writeShort(id);
                m2.writer().writeShort(p.item.total_item_bag_by_id(4, id));
                p.conn.addmsg(m2);
                m2.cleanup();
                //
                p.item.updateInventory(false);
                return true;
            }
        }
        return false;
    }

    private static void sendDenTroi(Player p) throws IOException {
        Message m = new Message(-15);
        m.writer().writeByte(20);
        m.writer().writeShort(p.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(0);
        p.map.send_msg_all_p(m, p, true);
    }

    public static boolean send_fashion_box_dialog(Player p, int boxId, String title, short[] fashionIds) throws IOException {
        if (p == null || fashionIds == null || fashionIds.length == 0) return false;
        Message m = new Message(69);
        m.writer().writeUTF(title != null ? title : "Hộp trang phục");
        m.writer().writeUTF("Đổi");
        m.writer().writeByte(fashionIds.length);
        for (int i = 0; i < fashionIds.length; i++) {
            ItemFashion itF = ItemFashion.get_item(fashionIds[i]);
            if (itF != null) {
                m.writer().writeByte(105);
                m.writer().writeUTF(itF.name);
                m.writer().writeShort(itF.idIcon);
                m.writer().writeByte(0);
                m.writer().writeShort(1);
                m.writer().writeByte(0);
            } else {
                m.writer().writeByte(105);
                m.writer().writeUTF("Thời trang " + fashionIds[i]);
                m.writer().writeShort(0);
                m.writer().writeByte(0);
                m.writer().writeShort(1);
                m.writer().writeByte(0);
            }
        }
        m.writer().writeShort(boxId);
        m.writer().writeByte(4);
        p.conn.addmsg(m);
        m.cleanup();
        return false;
    }

    public static boolean open_red_box(Player p, int boxId, int level, boolean sameClass) throws IOException {
        return open_red_box(p, boxId, level, sameClass, 0, ItemTemplate4.get_item_name(boxId));
    }

    public static boolean open_weapon_box(Player p, int boxId, int levelUp, boolean isMcs, int color) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return false;
        }
        int targetLevel = (p.level < 10 ? (color == 8 ? 10 : 1) : Math.min(100, (p.level / 10) * 10));
        List<ItemTemplate3> listMatch = new ArrayList<>();
        for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
            if (it3.color == color && it3.typeEquip == 0 && (it3.clazz == 0 || it3.clazz == p.clazz) && it3.level == targetLevel) {
                listMatch.add(it3);
            }
        }
        if (listMatch.isEmpty()) {
            for (ItemTemplate3 it3 : ItemTemplate3.ENTRYS) {
                if (it3.color == color && it3.typeEquip == 0 && (it3.clazz == 0 || it3.clazz == p.clazz)) {
                    listMatch.add(it3);
                }
            }
        }
        if (!listMatch.isEmpty()) {
            ItemTemplate3 chosen = listMatch.get(ZUtil.random(listMatch.size()));
            Item_wear temp = new Item_wear();
            if (isMcs) {
                temp.setupItemMCS(chosen.id);
            } else {
                temp.setup_template_by_id(chosen.id);
            }
            temp.levelUp = (byte) Math.max(0, Math.min(15, levelUp));
            temp.numLoKham = 2;
            if (temp.template != null) {
                p.item.add_item_bag3(temp);
            }
            List<Item_wear> listReceiv = new ArrayList<>();
            listReceiv.add(temp);
            Service.open_box_item3_orange(p, listReceiv, boxId, "Mở Khóa Rương", ItemTemplate4.get_item_name(boxId));
            return true;
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy vũ khí phù hợp!");
            return false;
        }
    }

    public static boolean craft_treasure_map(Player p) throws IOException {
        if (p == null) return false;
        boolean hasAll = true;
        List<Integer> missing = new ArrayList<>();
        for (int i = 442; i <= 448; i++) {
            if (p.item.total_item_bag_by_id(4, i) <= 0) {
                hasAll = false;
                missing.add(i - 441);
            }
        }
        if (!hasAll) {
            p.getService().send_box_ThongBao_OK("Bạn cần đủ 7 mảnh bản đồ (1-7) để ghép Bản Đồ Kho Báu!\nCòn thiếu mảnh: " + missing.toString());
            return false;
        }
        for (int i = 442; i <= 448; i++) {
            p.item.remove_item47(4, i, 1);
        }
        p.item.updateInventory(false);
        List<GiftBox> list = new ArrayList<>();
        list.add(new GiftBox(99, 0, 50_000));
        list.add(new GiftBox(4, 449, 1));
        core.RewardService.sendGiftOrMail(p, 1, "Ghép Bản Đồ Kho Báu", "Ghép thành công Bản Đồ Kho Báu!", list, true);
        return false;
    }

    public static boolean craft_haki_book(Player p, int baseId, int bookId, String bookName) throws IOException {
        if (p == null) return false;
        boolean hasAll = true;
        List<Integer> missing = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int pieceId = baseId + i;
            if (p.item.total_item_bag_by_id(4, pieceId) <= 0) {
                hasAll = false;
                missing.add(i + 1);
            }
        }
        if (!hasAll) {
            p.getService().send_box_ThongBao_OK("Bạn cần đủ 4 mảnh (1-4) để ghép " + bookName + "!\nCòn thiếu mảnh: " + missing.toString());
            return false;
        }
        for (int i = 0; i < 4; i++) {
            p.item.remove_item47(4, baseId + i, 1);
        }
        p.item.updateInventory(false);
        List<GiftBox> list = new ArrayList<>();
        list.add(new GiftBox(99, 0, 100_000));
        list.add(new GiftBox(4, bookId, 1));
        core.RewardService.sendGiftOrMail(p, 1, "Ghép Sách Haki", "Ghép thành công " + bookName + "!", list, true);
        return false;
    }

    public static boolean craft_fragment_item(Player p, int fragmentId, int needCount, int targetId, byte targetType, String targetName) throws IOException {
        if (p == null) return false;
        int count = p.item.total_item_bag_by_id(4, fragmentId);
        if (count < needCount) {
            p.getService().send_box_ThongBao_OK("Bạn cần " + needCount + " mảnh để ghép " + targetName + "!\nHiện tại có: " + count + "/" + needCount);
            return false;
        }
        p.item.remove_item47(4, fragmentId, needCount);
        p.item.updateInventory(false);
        List<GiftBox> list = new ArrayList<>();
        list.add(new GiftBox(targetType, targetId, 1));
        core.RewardService.sendGiftOrMail(p, 1, "Ghép Mảnh Vật Phẩm", "Ghép thành công " + targetName + "!", list, true);
        return false;
    }

    public static boolean use_pet_direct(Player p, int itemId, int petId, String petName) throws IOException {
        if (p == null) return false;
        for (MyPet mp : p.my_pet) {
            if (mp.id == petId) {
                p.getService().send_box_ThongBao_OK("Bạn đã sở hữu Pet: " + petName + " rồi!");
                return false;
            }
        }
        List<GiftBox> list = new ArrayList<>();
        list.add(new GiftBox(110, petId, 1));
        core.RewardService.sendGiftOrMail(p, 1, "Kích Hoạt Pet", "Kích hoạt Pet: " + petName + " vĩnh viễn!", list, true);
        return true;
    }

    public static boolean use_title_badge(Player p, int itemId, String titleName) throws IOException {
        if (p == null) return false;
        p.getService().send_box_ThongBao_OK("Kích hoạt thành công Danh Hiệu: " + titleName + "!");
        p.update_info_to_all();
        return true;
    }

    public static boolean use_food_or_drink(Player p, int itemId, int expMultiplier, int healHp, int healMp, String msg) throws IOException {
        if (p == null) return false;
        if (expMultiplier > 0) {
            long expGain = (long) p.level * expMultiplier;
            p.update_exp(expGain, true);
        }
        if (healHp > 0 || healMp > 0) {
            p.getService().use_potion(healHp, healMp);
        }
        if (msg != null && !msg.isEmpty()) {
            p.getService().send_box_ThongBao_OK(msg);
        }
        p.update_info_to_all();
        return true;
    }

    public static boolean open_event_gift_box(Player p, int boxId, String boxName, int expMultiplier, long minBeri, long maxBeri, int minRuby, int maxRuby, int stoneMinTier, int stoneMaxTier) throws IOException {
        if (p == null) return false;
        if (p.item.able_bag() < 2) {
            p.getService().send_box_ThongBao_OK("Hành trang cần ít nhất 2 ô trống để mở quà!");
            return false;
        }
        List<GiftBox> listGift = new ArrayList<>();
        String title = (boxName != null && !boxName.isEmpty()) ? boxName : ItemTemplate4.get_item_name(boxId);

        // 1. Phần thưởng đảm bảo (Kinh nghiệm & Beri)
        if (expMultiplier > 0) {
            long expGain = (long) p.level * expMultiplier;
            listGift.add(new GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expGain)));
        }
        if (maxBeri > 0) {
            long beri = (minBeri >= maxBeri) ? minBeri : ZUtil.random((int) Math.min(Integer.MAX_VALUE, minBeri), (int) Math.min(Integer.MAX_VALUE, maxBeri));
            listGift.add(new GiftBox(4, 0, (int) Math.min(Integer.MAX_VALUE, beri)));
        }

        // 2. Random Drop 1 phần quà chính theo tỉ lệ phân tầng
        boolean isHighTierBox = (stoneMaxTier >= 5 || boxId == 170 || boxId == 171 || boxId == 172 
                || boxId == 215 || boxId == 216 || boxId == 217 || boxId == 218 
                || boxId == 231 || boxId == 239 || boxId == 285 || boxId == 462 
                || boxId == 470 || boxId == 492 || boxId == 610 || boxId == 887);

        int roll = ZUtil.random(100);

        if (isHighTierBox) {
            // === RƯƠNG SỰ KIỆN CAO CẤP (Tết, Vua Hùng, Sinh Nhật, Sắc Màu, Kì Bí S, Cúp WC, Euro...) ===
            if (roll < 30) {
                // Tier 1 (30%): Phổ thông (Ruby vừa, Bình hồi phục xịn, Bùa)
                int sub = ZUtil.random(100);
                if (sub < 40) {
                    listGift.add(new GiftBox(4, 1, ZUtil.random(minRuby > 0 ? minRuby : 30, maxRuby > 0 ? maxRuby : 60))); // Ruby
                } else if (sub < 70) {
                    listGift.add(new GiftBox(4, 173, ZUtil.random(3, 8))); // Cơm hộp hải tặc
                } else if (sub < 90) {
                    listGift.add(new GiftBox(4, 82, ZUtil.random(2, 5))); // Lọ hồi sức 50%
                } else {
                    listGift.add(new GiftBox(4, 31, ZUtil.random(1, 2))); // Bùa xóa tội
                }
            } else if (roll < 65) {
                // Tier 2 (35%): Hữu ích & Đá 2-3 & Buff (X2 EXP, Xp Chiêu thức x3, Vé KN đặc biệt, Đá 2-3, Hải Thạch 2-3, 60-100 Ruby)
                int sub = ZUtil.random(100);
                if (sub < 25) {
                    listGift.add(new GiftBox(4, 80, ZUtil.random(1, 2))); // Kinh nghiệm X2
                } else if (sub < 50) {
                    listGift.add(new GiftBox(4, 159, ZUtil.random(3, 6))); // Xp Chiêu thức (Vé x3 skill)
                } else if (sub < 70) {
                    listGift.add(new GiftBox(4, 133, 1)); // Vé kinh nghiệm đặc biệt
                } else if (sub < 85) {
                    short[][] daGroups = {
                        {45, 51, 57, 63, 69, 75}, // Đá cấp 2
                        {46, 52, 58, 64, 70, 76}  // Đá cấp 3
                    };
                    short[] group = daGroups[ZUtil.random(daGroups.length)];
                    listGift.add(new GiftBox(4, group[ZUtil.random(group.length)], ZUtil.random(1, 2)));
                } else if (sub < 95) {
                    short[] htGroup = {222, 223}; // Hải thạch c2, c3
                    listGift.add(new GiftBox(4, htGroup[ZUtil.random(htGroup.length)], 1));
                } else {
                    listGift.add(new GiftBox(4, 1, ZUtil.random(60, 100))); // 60-100 Ruby
                }
            } else if (roll < 90) {
                // Tier 3 (25%): Hiếm & Đá 4-5 (Đá khảm 4-5, Hải Thạch 4-5, Vé khóa exp, Rương Ác Quỷ, Rương Cam Cùng Hệ)
                int sub = ZUtil.random(100);
                if (sub < 30) {
                    short[][] daGroups = {
                        {47, 53, 59, 65, 71, 77}, // Đá cấp 4
                        {48, 54, 60, 66, 72, 78}  // Đá cấp 5
                    };
                    short[] group = daGroups[ZUtil.random(daGroups.length)];
                    listGift.add(new GiftBox(4, group[ZUtil.random(group.length)], 1));
                } else if (sub < 50) {
                    short[] htGroup = {224, 225}; // Hải thạch c4, c5
                    listGift.add(new GiftBox(4, htGroup[ZUtil.random(htGroup.length)], 1));
                } else if (sub < 70) {
                    listGift.add(new GiftBox(4, 548, ZUtil.random(2, 5))); // Vé khóa exp
                } else if (sub < 85) {
                    listGift.add(new GiftBox(4, 29, 1)); // Rương Ác Quỷ
                } else {
                    int pLv = (p.level < 10) ? 10 : Math.min(100, (p.level / 10) * 10);
                    int ruongCamId = 121 + (pLv / 10); // 122..131: Rương Cam Cùng Hệ Lv10..100
                    listGift.add(new GiftBox(4, ruongCamId, 1));
                }
            } else {
                // Tier 4 (10%): Cực Phẩm (Rương Đại Ác Quỷ, Hải Thạch c6, Đá khảm c6, Vé thời trang, Rương Siêu Đại Ác Quỷ)
                int sub = ZUtil.random(100);
                String rareName = "";
                if (sub < 40) {
                    listGift.add(new GiftBox(4, 158, 1)); // Rương Đại Ác Quỷ
                    rareName = "Rương Đại Ác Quỷ";
                } else if (sub < 65) {
                    listGift.add(new GiftBox(4, 226, 1)); // Đá Hải Thạch cấp 6
                    rareName = "Đá Hải Thạch Cấp 6";
                } else if (sub < 85) {
                    short[] da6 = {49, 55, 61, 67, 73, 79}; // Đá khảm cấp 6
                    short chosenDa = da6[ZUtil.random(da6.length)];
                    listGift.add(new GiftBox(4, chosenDa, 1));
                    rareName = ItemTemplate4.get_item_name(chosenDa);
                } else if (sub < 95) {
                    listGift.add(new GiftBox(4, 132, 1)); // Vé đổi đồ thời trang
                    rareName = "Vé Đổi Đồ Thời Trang";
                } else {
                    listGift.add(new GiftBox(4, 911, 1)); // Rương Siêu Đại Ác Quỷ
                    rareName = "Rương Siêu Đại Ác Quỷ";
                }
                if (!rareName.isEmpty()) {
                    Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở " + title + " may mắn nhận được [" + rareName + "]!", 0);
                }
            }
        } else {
            // === RƯƠNG SỰ KIỆN TIÊU CHUẨN (Bao Lì Xì, Túi Giáng Sinh, Tất Noel, Bó Hoa, Lồng Đèn...) ===
            if (roll < 45) {
                // Tier 1 (45%): Phổ thông (Ruby, Cơm hộp, Quả Conache, Bùa)
                int sub = ZUtil.random(100);
                if (sub < 50) {
                    listGift.add(new GiftBox(4, 1, ZUtil.random(minRuby > 0 ? minRuby : 10, maxRuby > 0 ? maxRuby : 30))); // Ruby
                } else if (sub < 75) {
                    listGift.add(new GiftBox(4, 173, ZUtil.random(2, 4))); // Cơm hộp hải tặc
                } else if (sub < 90) {
                    listGift.add(new GiftBox(4, 174, ZUtil.random(2, 4))); // Quả Conache
                } else {
                    listGift.add(new GiftBox(4, 31, 1)); // Bùa xóa tội
                }
            } else if (roll < 80) {
                // Tier 2 (35%): Hữu ích & Đá 1-3 & Buff (X2 EXP, Xp Chiêu thức x3, Vé KN đặc biệt, Đá 1-3, Hải thạch 1-3, 30-50 Ruby)
                int sub = ZUtil.random(100);
                if (sub < 25) {
                    listGift.add(new GiftBox(4, 80, 1)); // Kinh nghiệm X2
                } else if (sub < 50) {
                    listGift.add(new GiftBox(4, 159, ZUtil.random(2, 4))); // Xp Chiêu thức (Vé x3 skill)
                } else if (sub < 65) {
                    listGift.add(new GiftBox(4, 133, 1)); // Vé kinh nghiệm đặc biệt
                } else if (sub < 85) {
                    short[][] daGroups = {
                        {44, 50, 56, 62, 68, 74}, // Đá cấp 1
                        {45, 51, 57, 63, 69, 75}, // Đá cấp 2
                        {46, 52, 58, 64, 70, 76}  // Đá cấp 3
                    };
                    short[] group = daGroups[ZUtil.random(daGroups.length)];
                    listGift.add(new GiftBox(4, group[ZUtil.random(group.length)], ZUtil.random(1, 2)));
                } else if (sub < 95) {
                    short[] htGroup = {221, 222, 223}; // Hải thạch c1, c2, c3
                    listGift.add(new GiftBox(4, htGroup[ZUtil.random(htGroup.length)], 1));
                } else {
                    listGift.add(new GiftBox(4, 1, ZUtil.random(30, 50))); // Ruby
                }
            } else if (roll < 96) {
                // Tier 3 (16%): Hiếm & Đá 4-5 (Đá 4-5, Hải Thạch 4-5, Vé khóa exp, Rương Ác Quỷ, Rương Anh Hùng)
                int sub = ZUtil.random(100);
                if (sub < 30) {
                    short[][] daGroups = {
                        {47, 53, 59, 65, 71, 77}, // Đá cấp 4
                        {48, 54, 60, 66, 72, 78}  // Đá cấp 5
                    };
                    short[] group = daGroups[ZUtil.random(daGroups.length)];
                    listGift.add(new GiftBox(4, group[ZUtil.random(group.length)], 1));
                } else if (sub < 55) {
                    short[] htGroup = {224, 225}; // Hải thạch c4, c5
                    listGift.add(new GiftBox(4, htGroup[ZUtil.random(htGroup.length)], 1));
                } else if (sub < 75) {
                    listGift.add(new GiftBox(4, 548, ZUtil.random(1, 3))); // Vé khóa exp
                } else if (sub < 90) {
                    listGift.add(new GiftBox(4, 29, 1)); // Rương Ác Quỷ
                } else {
                    int pLv = (p.level < 10) ? 10 : Math.min(100, (p.level / 10) * 10);
                    int ruongAnhHungId = 147 + (pLv / 10); // 148..157: Rương Anh Hùng Lv10..100
                    listGift.add(new GiftBox(4, ruongAnhHungId, 1));
                }
            } else {
                // Tier 4 (4%): Cực Phẩm (Rương Đại Ác Quỷ, Hải Thạch c6, Đá khảm c6, Vé thời trang)
                int sub = ZUtil.random(100);
                String rareName = "";
                if (sub < 45) {
                    listGift.add(new GiftBox(4, 158, 1)); // Rương Đại Ác Quỷ
                    rareName = "Rương Đại Ác Quỷ";
                } else if (sub < 70) {
                    listGift.add(new GiftBox(4, 226, 1)); // Đá Hải Thạch cấp 6
                    rareName = "Đá Hải Thạch Cấp 6";
                } else if (sub < 90) {
                    short[] da6 = {49, 55, 61, 67, 73, 79}; // Đá khảm cấp 6
                    short chosenDa = da6[ZUtil.random(da6.length)];
                    listGift.add(new GiftBox(4, chosenDa, 1));
                    rareName = ItemTemplate4.get_item_name(chosenDa);
                } else {
                    listGift.add(new GiftBox(4, 132, 1)); // Vé đổi đồ thời trang
                    rareName = "Vé Đổi Đồ Thời Trang";
                }
                if (!rareName.isEmpty()) {
                    Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở " + title + " may mắn nhận được [" + rareName + "]!", 0);
                }
            }
        }

        core.RewardService.sendGiftOrMail(p, 1, title, "Phần thưởng", listGift, true);
        return true;
    }

    public static boolean open_hop_qua_20_10(Player p, int id) throws IOException {
        if (p == null || p.isdie) return false;
        if (p.item.total_item_bag_by_id(4, id) < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có vật phẩm này trong hành trang!");
            return false;
        }

        List<GiftBox> listGift = new ArrayList<>();
        String boxName = ItemTemplate4.get_item_name(id);

        switch (id) {
            case 592: { // Hộp quà số 1: 3tr Bery, 30 Ruby, 1 Đá Hải Thạch c4, 1 Khảm lv6 tự chọn, 5 Vé x3 skill
                listGift.add(new GiftBox(4, 0, 3_000_000));
                listGift.add(new GiftBox(4, 1, 30));
                listGift.add(new GiftBox(4, 224, 1)); // Đá Hải Thạch cấp 4
                listGift.add(new GiftBox(4, 588, 1)); // Khảm lv6 tự chọn
                listGift.add(new GiftBox(4, 159, 5)); // Xp Chiêu thức (Vé x3 skill)
                break;
            }
            case 593: { // Hộp quà số 2: 5tr Bery, 50 Ruby, 1 Đá Hải Thạch c5, 1 Khảm lv6 tự chọn, 10 Vé x3 skill
                listGift.add(new GiftBox(4, 0, 5_000_000));
                listGift.add(new GiftBox(4, 1, 50));
                listGift.add(new GiftBox(4, 225, 1)); // Đá Hải Thạch cấp 5
                listGift.add(new GiftBox(4, 588, 1)); // Khảm lv6 tự chọn
                listGift.add(new GiftBox(4, 159, 10)); // Xp Chiêu thức (Vé x3 skill)
                break;
            }
            case 594: { // Hộp quà số 3: 10tr Bery, 100 Ruby, 1 Đá Hải Thạch c5, 1 Khảm lv6 tự chọn, 15 Vé x3 skill, 1 Rương đại ác quỷ
                listGift.add(new GiftBox(4, 0, 10_000_000));
                listGift.add(new GiftBox(4, 1, 100));
                listGift.add(new GiftBox(4, 225, 1)); // Đá Hải Thạch cấp 5
                listGift.add(new GiftBox(4, 588, 1)); // Khảm lv6 tự chọn
                listGift.add(new GiftBox(4, 159, 15)); // Xp Chiêu thức (Vé x3 skill)
                listGift.add(new GiftBox(4, 158, 1)); // Rương đại ác quỷ
                break;
            }
            case 595: { // Hộp quà số 4: 10tr Bery, 100 Ruby, 2 Đá Hải Thạch c6, 20 Vé x3 skill, 1 Rương đại ác quỷ, 5 Vé khóa exp, 2 Xu hành trình
                listGift.add(new GiftBox(4, 0, 10_000_000));
                listGift.add(new GiftBox(4, 1, 100));
                listGift.add(new GiftBox(4, 226, 2)); // 2v Đá Hải Thạch cấp 6
                listGift.add(new GiftBox(4, 159, 20)); // 20 Vé x3 skill
                listGift.add(new GiftBox(4, 158, 1)); // 1 Rương đại ác quỷ
                listGift.add(new GiftBox(4, 548, 5)); // 5 Vé khóa exp
                listGift.add(new GiftBox(4, 580, 2)); // 2 Xu hành trình
                break;
            }
            case 596: { // Hộp quà đặc biệt: 20tr Bery, 200 Ruby, 3 Đá Hải Thạch c6, 1 Rương đại ác quỷ, 20 Vé x3 skill, 20 Vé khóa exp, 2 Xu hành trình, 1 Sao 8 cánh
                listGift.add(new GiftBox(4, 0, 20_000_000));
                listGift.add(new GiftBox(4, 1, 200));
                listGift.add(new GiftBox(4, 226, 3)); // 3v Đá Hải Thạch cấp 6
                listGift.add(new GiftBox(4, 158, 1)); // 1 Rương đại ác quỷ
                listGift.add(new GiftBox(4, 159, 20)); // 20 Vé x3 skill
                listGift.add(new GiftBox(4, 548, 20)); // 20 Vé khóa exp
                listGift.add(new GiftBox(4, 580, 2)); // 2 Xu hành trình
                listGift.add(new GiftBox(7, 16, 1)); // 1 Sao 8 cánh (item7)
                break;
            }
            default:
                return false;
        }

        p.item.remove_item47(4, id, 1);
        Message m2 = new Message(-13);
        m2.writer().writeShort(id);
        m2.writer().writeShort(p.item.total_item_bag_by_id(4, id));
        p.conn.addmsg(m2);
        m2.cleanup();
        p.item.updateInventory(false);

        core.RewardService.sendGiftOrMail(p, 1, boxName, "Phần thưởng mở " + boxName, listGift, true);

        if (id == 596) {
            Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở Hộp Quà Đặc Biệt 20/10 nhận được [Rương Đại Ác Quỷ, 3 Đá Hải Thạch c6, Sao 8 cánh]!", 0);
        }
        return true;
    }

    // ==========================================
    // AUTO USE LEVEL & SKILL EXP ITEMS WITH MENU
    // ==========================================

    public static boolean openMenuUseItem415(Player p) {
        if (p == null) return false;
        int totalInBag = p.item.total_item_bag_by_id(4, 415);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không có Đại bổ đơn trong hành trang!");
            return false;
        }
        if (p.level >= 100) {
            p.getService().send_box_ThongBao_OK("Cấp độ đã đạt tối đa (Lv.100), không thể sử dụng!");
            return false;
        }

        YesNoDialog diag = new YesNoDialog(p, 4150, "Đại Bổ Đơn",
                "Bạn muốn sử dụng Đại Bổ Đơn như thế nào?\n(Hiện có: " + totalInBag + " viên, Cấp hiện tại: Lv." + p.level + ")",
                new String[]{"Dùng", "Dùng liên tục", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1}, (byte val) -> {
            try {
                if (val == 0) {
                    executeUseItem415(p, 1);
                } else if (val == 1) {
                    int need = 100 - p.level;
                    int count = p.item.total_item_bag_by_id(4, 415);
                    int use = Math.min(need, count);
                    if (use <= 0) {
                        p.getService().send_box_ThongBao_OK("Cấp độ đã đạt tối đa (Lv.100) hoặc không đủ vật phẩm!");
                        return;
                    }
                    executeUseItem415(p, use);
                } else if (val == 2) {
                    InputDialog inputDlg = new InputDialog(p, 4151, "Nhập số lượng Đại Bổ Đơn", new String[]{"Số lượng:"}, (String[] inputs) -> {
                        try {
                            if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0])) {
                                int qty = Integer.parseInt(inputs[0]);
                                if (qty > 0) {
                                    int need = 100 - p.level;
                                    int count = p.item.total_item_bag_by_id(4, 415);
                                    int use = Math.min(qty, Math.min(need, count));
                                    if (use <= 0) {
                                        p.getService().send_box_ThongBao_OK("Cấp độ đã đạt tối đa (Lv.100) hoặc không đủ vật phẩm!");
                                        return;
                                    }
                                    executeUseItem415(p, use);
                                } else {
                                    p.getService().send_box_ThongBao_OK("Số lượng nhập vào không hợp lệ!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    inputDlg.startInput();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diag.startYesNo();
        return false;
    }

    private static void executeUseItem415(Player p, int count) throws IOException {
        if (p == null) return;
        if (p.level >= 100) {
            p.getService().send_box_ThongBao_OK("Cấp độ đã đạt tối đa (Lv.100)!");
            return;
        }
        int totalInBag = p.item.total_item_bag_by_id(4, 415);
        int realUse = Math.min(count, Math.min(100 - p.level, totalInBag));
        if (realUse <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ Đại bổ đơn để sử dụng!");
            return;
        }

        int pointsGained = 0;
        for (int i = 0; i < realUse; i++) {
            p.level++;
            int pt = (p.level < 100) ? Level.ENTRYS[p.level - 1].tiemnang : Level.ENTRYS[p.level - 2].tiemnang;
            pointsGained += pt;
        }
        p.pointAttribute += pointsGained;
        p.item.remove_item47(4, 415, realUse);
        p.item.updateInventory(false);
        p.getService().send_eff(0, 0);
        p.update_info_to_all();
        p.updateMoney();
        p.getService().CountDown_Ticket();
        p.getService().send_box_ThongBao_OK("Đã sử dụng " + realUse + " Đại bổ đơn!\nCấp độ hiện tại: Lv." + p.level + "\nNhận được: " + pointsGained + " điểm tiềm năng.");
    }

    public static boolean openMenuUseItem413(Player p) {
        if (p == null) return false;
        int totalInBag = p.item.total_item_bag_by_id(4, 413);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không có Tiến cấp đơn trong hành trang!");
            return false;
        }
        if (p.level >= 30) {
            p.getService().send_box_ThongBao_OK("Cấp độ dưới 30 mới có thể sử dụng!");
            return false;
        }

        YesNoDialog diag = new YesNoDialog(p, 4130, "Tiến Cấp Đơn",
                "Bạn muốn sử dụng Tiến Cấp Đơn như thế nào?\n(Hiện có: " + totalInBag + " viên, Cấp hiện tại: Lv." + p.level + ")",
                new String[]{"Dùng", "Dùng liên tục", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1}, (byte val) -> {
            try {
                if (val == 0 || val == 1) {
                    executeUseItem413(p);
                } else if (val == 2) {
                    InputDialog inputDlg = new InputDialog(p, 4131, "Nhập số lượng Tiến Cấp Đơn", new String[]{"Số lượng:"}, (String[] inputs) -> {
                        try {
                            if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0])) {
                                int qty = Integer.parseInt(inputs[0]);
                                if (qty > 0) {
                                    executeUseItem413(p);
                                } else {
                                    p.getService().send_box_ThongBao_OK("Số lượng nhập vào không hợp lệ!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    inputDlg.startInput();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diag.startYesNo();
        return false;
    }

    private static void executeUseItem413(Player p) throws IOException {
        if (p == null) return;
        if (p.level >= 30) {
            p.getService().send_box_ThongBao_OK("Cấp độ dưới 30 mới có thể sử dụng!");
            return;
        }
        int totalInBag = p.item.total_item_bag_by_id(4, 413);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ Tiến cấp đơn để sử dụng!");
            return;
        }

        int oldLevel = p.level;
        p.level = 30;
        int pointsGained = 0;
        for (int lv = oldLevel; lv < 30; lv++) {
            if (lv < 100) {
                pointsGained += Level.ENTRYS[lv - 1].tiemnang;
            } else {
                pointsGained += Level.ENTRYS[lv - 2].tiemnang;
            }
        }
        p.pointAttribute += pointsGained;
        p.item.remove_item47(4, 413, 1);
        p.item.updateInventory(false);
        p.getService().send_eff(0, 0);
        p.update_info_to_all();
        p.updateMoney();
        p.getService().CountDown_Ticket();
        p.getService().send_box_ThongBao_OK("Đã sử dụng 1 Tiến cấp đơn!\nTăng cấp lên Lv.30 thành công! Nhận " + pointsGained + " điểm tiềm năng.");
    }

    public static boolean openMenuUseItem414(Player p) {
        if (p == null) return false;
        int totalInBag = p.item.total_item_bag_by_id(4, 414);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không có Kỹ năng đơn trong hành trang!");
            return false;
        }
        if (p.skill_point == null || p.skill_point.size() < 3
                || p.skill_point.get(0).exp < 0
                || p.skill_point.get(1).exp < 0
                || p.skill_point.get(2).exp < 0) {
            p.getService().send_box_ThongBao_OK("Hãy đứng ở làng để sử dụng");
            return false;
        }
        if (p.skill_point.get(0).temp.Lv_RQ >= 30
                && p.skill_point.get(1).temp.Lv_RQ >= 30
                && p.skill_point.get(2).temp.Lv_RQ >= 30) {
            p.getService().send_box_ThongBao_OK("Tất cả kỹ năng đã đạt cấp tối đa (Lv.30)!");
            return false;
        }

        YesNoDialog diag = new YesNoDialog(p, 4140, "Kỹ Năng Đơn",
                "Bạn muốn sử dụng Kỹ Năng Đơn như thế nào?\n(Hiện có: " + totalInBag + " viên)",
                new String[]{"Dùng", "Dùng liên tục", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1}, (byte val) -> {
            try {
                if (val == 0) {
                    executeUseItem414(p, 1);
                } else if (val == 1) {
                    int count = p.item.total_item_bag_by_id(4, 414);
                    executeUseItem414(p, count);
                } else if (val == 2) {
                    InputDialog inputDlg = new InputDialog(p, 4141, "Nhập số lượng Kỹ Năng Đơn", new String[]{"Số lượng:"}, (String[] inputs) -> {
                        try {
                            if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0])) {
                                int qty = Integer.parseInt(inputs[0]);
                                if (qty > 0) {
                                    int count = p.item.total_item_bag_by_id(4, 414);
                                    int use = Math.min(qty, count);
                                    executeUseItem414(p, use);
                                } else {
                                    p.getService().send_box_ThongBao_OK("Số lượng nhập vào không hợp lệ!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    inputDlg.startInput();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diag.startYesNo();
        return false;
    }

    private static void executeUseItem414(Player p, int maxCount) throws IOException {
        if (p == null) return;
        if (p.skill_point == null || p.skill_point.size() < 3
                || p.skill_point.get(0).exp < 0
                || p.skill_point.get(1).exp < 0
                || p.skill_point.get(2).exp < 0) {
            p.getService().send_box_ThongBao_OK("Hãy đứng ở làng để sử dụng");
            return;
        }

        int totalInBag = p.item.total_item_bag_by_id(4, 414);
        int canUse = Math.min(maxCount, totalInBag);
        if (canUse <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ Kỹ năng đơn để sử dụng!");
            return;
        }

        int used = 0;
        for (int step = 0; step < canUse; step++) {
            List<Skill_info> availSkills = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                if (p.skill_point.get(i).temp.Lv_RQ < 30) {
                    availSkills.add(p.skill_point.get(i));
                }
            }
            if (availSkills.isEmpty()) {
                break;
            }

            Skill_info skill_info = availSkills.get(ZUtil.random(availSkills.size()));
            long exp_total = Skill_info.EXP[skill_info.temp.Lv_RQ - 1];
            skill_info.exp = exp_total;
            if (Skill_Template.upgrade_skill(skill_info, p.clazz)) {
                skill_info.exp -= exp_total;
                if (skill_info.exp >= exp_total) {
                    skill_info.exp = 1;
                }
                used++;
            }
        }

        if (used > 0) {
            p.item.remove_item47(4, 414, used);
            p.item.updateInventory(false);
            for (int i = 0; i < 3; i++) {
                p.sendSkillLevelUP(p.skill_point.get(i));
            }
            p.send_skill();
            p.update_info_to_all();
            p.getService().send_box_ThongBao_OK("Đã sử dụng " + used + " Kỹ năng đơn!\nNâng cấp kỹ năng thành công.");
        } else {
            p.getService().send_box_ThongBao_OK("Tất cả kỹ năng đã đạt cấp tối đa (Lv.30)!");
        }
    }

    public static boolean openMenuUseItem333(Player p) {
        if (p == null) return false;
        int totalInBag = p.item.total_item_bag_by_id(4, 333);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không có XP Skill trong hành trang!");
            return false;
        }

        YesNoDialog diag = new YesNoDialog(p, 3330, "XP Skill",
                "Bạn muốn sử dụng XP Skill như thế nào?\n(Hiện có: " + totalInBag + " vật phẩm)",
                new String[]{"Dùng", "Dùng liên tục", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1}, (byte val) -> {
            try {
                if (val == 0) {
                    executeUseItem333(p, 1);
                } else if (val == 1) {
                    int count = p.item.total_item_bag_by_id(4, 333);
                    executeUseItem333(p, count);
                } else if (val == 2) {
                    InputDialog inputDlg = new InputDialog(p, 3331, "Nhập số lượng XP Skill", new String[]{"Số lượng:"}, (String[] inputs) -> {
                        try {
                            if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0])) {
                                int qty = Integer.parseInt(inputs[0]);
                                if (qty > 0) {
                                    int count = p.item.total_item_bag_by_id(4, 333);
                                    int use = Math.min(qty, count);
                                    executeUseItem333(p, use);
                                } else {
                                    p.getService().send_box_ThongBao_OK("Số lượng nhập vào không hợp lệ!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    inputDlg.startInput();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diag.startYesNo();
        return false;
    }

    private static void executeUseItem333(Player p, int count) throws IOException {
        if (p == null) return;
        int totalInBag = p.item.total_item_bag_by_id(4, 333);
        int realUse = Math.min(count, totalInBag);
        if (realUse <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ XP Skill để sử dụng!");
            return;
        }

        p.updateHk(realUse * 100L);
        p.item.remove_item47(4, 333, realUse);
        p.item.updateInventory(false);
        p.getService().send_box_ThongBao_OK("Đã sử dụng " + realUse + " XP Skill thành công!");
    }

    public static boolean openMenuUseBuffExpItem(Player p, int itemId, int effId, int effVal, String itemName) {
        if (p == null) return false;
        int totalInBag = p.item.total_item_bag_by_id(4, itemId);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không có " + itemName + " trong hành trang!");
            return false;
        }

        long now = System.currentTimeMillis();
        long maxDuration = 1000L * 60 * 60 * 24 * 7; // 7 ngày
        EffTemplate eff = p.get_eff(effId);
        long curRemaining = (eff != null && eff.time > now) ? (eff.time - now) : 0;
        if (curRemaining >= maxDuration) {
            p.getService().send_box_ThongBao_OK("Thời gian hiệu lực của " + itemName + " đã đạt tối đa (7 ngày)!");
            return false;
        }

        String remainingStr = (curRemaining > 0) ? ("\nThời gian còn lại: " + ZUtil.get_time_str_by_sec2(curRemaining)) : "";
        YesNoDialog diag = new YesNoDialog(p, itemId * 10, itemName,
                "Bạn muốn sử dụng " + itemName + " như thế nào?\n(Hiện có: " + totalInBag + " vật phẩm" + remainingStr + ")",
                new String[]{"Dùng", "Dùng liên tục", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1}, (byte val) -> {
            try {
                if (val == 0) {
                    executeUseBuffExpItem(p, itemId, effId, effVal, itemName, 1);
                } else if (val == 1) {
                    int count = p.item.total_item_bag_by_id(4, itemId);
                    executeUseBuffExpItem(p, itemId, effId, effVal, itemName, count);
                } else if (val == 2) {
                    InputDialog inputDlg = new InputDialog(p, itemId * 10 + 1, "Nhập số lượng " + itemName, new String[]{"Số lượng:"}, (String[] inputs) -> {
                        try {
                            if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0])) {
                                int qty = Integer.parseInt(inputs[0]);
                                if (qty > 0) {
                                    int count = p.item.total_item_bag_by_id(4, itemId);
                                    int use = Math.min(qty, count);
                                    executeUseBuffExpItem(p, itemId, effId, effVal, itemName, use);
                                } else {
                                    p.getService().send_box_ThongBao_OK("Số lượng nhập vào không hợp lệ!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    inputDlg.startInput();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diag.startYesNo();
        return false;
    }

    private static void executeUseBuffExpItem(Player p, int itemId, int effId, int effVal, String itemName, int count) throws IOException {
        if (p == null) return;
        int totalInBag = p.item.total_item_bag_by_id(4, itemId);
        if (totalInBag <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ " + itemName + " để sử dụng!");
            return;
        }

        long now = System.currentTimeMillis();
        long maxDuration = 1000L * 60 * 60 * 24 * 7; // 7 ngày
        long unitTime = 1000L * 60 * 60 * 2; // 2 giờ / viên

        EffTemplate eff = p.get_eff(effId);
        long curRemaining = (eff != null && eff.time > now) ? (eff.time - now) : 0;
        if (curRemaining >= maxDuration) {
            p.getService().send_box_ThongBao_OK("Thời gian hiệu lực của " + itemName + " đã đạt tối đa (7 ngày)!");
            return;
        }

        long timeGap = maxDuration - curRemaining;
        int maxCanUse = (int) Math.ceil((double) timeGap / unitTime);
        int realUse = Math.min(count, Math.min(maxCanUse, totalInBag));
        if (realUse <= 0) {
            p.getService().send_box_ThongBao_OK("Thời gian đã đạt tối đa hoặc không đủ vật phẩm!");
            return;
        }

        long addDuration = (long) realUse * unitTime;
        if (eff != null && eff.time > (now + 3000L)) {
            eff.time = Math.min(now + maxDuration, eff.time + addDuration);
        } else {
            p.add_new_eff(effId, effVal, Math.min(maxDuration, addDuration));
        }

        p.item.remove_item47(4, itemId, realUse);
        p.item.updateInventory(false);
        p.getService().CountDown_Ticket();

        eff = p.get_eff(effId);
        long newRemaining = (eff != null && eff.time > now) ? (eff.time - now) : 0;
        p.getService().send_box_ThongBao_OK("Đã sử dụng " + realUse + " " + itemName + " thành công!\nThời gian hiệu lực còn lại: "
                + ZUtil.get_time_str_by_sec2(newRemaining)
                + "\n(Lưu ý thời gian cộng dồn tối đa 7 ngày)");
    }

    /**
     * Tính chi phí Ruby để mở rộng thêm 1 ô hành trang từ mốc ô hiện tại.
     * - Dưới 126 ô: 0 Ruby (chỉ dùng Búa đục túi item 4 id 416, tối đa 126 ô)
     * - Từ 126 đến 1260 ô: Lũy tiến theo từng mốc 200 ô (+25 Ruby / mốc):
     *     + 126 -> 300 ô (174 ô): 30 Ruby / ô (5,220 Ruby)
     *     + 301 -> 500 ô (200 ô): 55 Ruby / ô (11,000 Ruby)
     *     + 501 -> 700 ô (200 ô): 80 Ruby / ô (16,000 Ruby)
     *     + 701 -> 900 ô (200 ô): 105 Ruby / ô (21,000 Ruby)
     *     + 901 -> 1100 ô (200 ô): 130 Ruby / ô (26,000 Ruby)
     *     + 1101 -> 1260 ô (160 ô): 130 Ruby / ô (20,800 Ruby)
     *   Tổng toàn bộ 1,134 ô từ 126 -> 1260 là đúng 100,020 Ruby (~100k Ruby).
     */
    public static int getRubyCostExpandBag(int currentBag) {
        if (currentBag < 126) return 0;
        if (currentBag < 300) return 30;
        if (currentBag < 500) return 55;
        if (currentBag < 700) return 80;
        if (currentBag < 900) return 105;
        return 130;
    }

    /**
     * Mở rộng hành trang bằng Búa đục túi (Item 4 ID 416).
     * Mở 1 ô mỗi lần sử dụng.
     * - Dưới 126 ô: Miễn phí Ruby, tối đa 126 ô.
     * - Từ 126 đến 1260 ô: Yêu cầu 1 Búa đục túi + Ruby tương ứng.
     */
    public static boolean useItemBuaDucTui(Player p) throws IOException {
        if (p == null || p.item == null) return false;
        int curBag = p.item.max_bag & 0xFFFF;
        if (curBag >= itemz.Item.MAX_BAG_LIMIT) {
            p.getService().send_box_ThongBao_OK("Hành trang đã đạt giới hạn tối đa (" + itemz.Item.MAX_BAG_LIMIT + " ô)!");
            return false;
        }

        if (curBag < 126) {
            int added = p.item.expand_bag(1);
            if (added > 0) {
                p.item.sendNumCellBag();
                p.item.updateInventory(false);
                p.getService().send_box_ThongBao_OK("Đã dùng Búa đục túi mở rộng thêm 1 ô hành trang!\n(Hiện tại: " + (p.item.max_bag & 0xFFFF) + "/" + itemz.Item.MAX_BAG_LIMIT + " ô)\n* Dùng Búa đục túi miễn phí Ruby tối đa đến 126 ô.");
                return true;
            } else {
                p.getService().send_box_ThongBao_OK("Không thể mở rộng thêm hành trang!");
                return false;
            }
        } else {
            int rubyCost = getRubyCostExpandBag(curBag);
            if (p.get_ngoc() < rubyCost) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ " + rubyCost + " Ruby để mở ô thứ " + (curBag + 1) + "!\n(Yêu cầu: 1 Búa đục túi + " + rubyCost + " Ruby)");
                return false;
            }

            p.update_ngoc(-rubyCost);
            p.updateMoney();
            int added = p.item.expand_bag(1);
            if (added > 0) {
                p.item.sendNumCellBag();
                p.item.updateInventory(false);
                p.getService().send_box_ThongBao_OK("Đã dùng 1 Búa đục túi và " + rubyCost + " Ruby mở rộng thêm 1 ô hành trang!\n(Hiện tại: " + (p.item.max_bag & 0xFFFF) + "/" + itemz.Item.MAX_BAG_LIMIT + " ô)");
                return true;
            } else {
                p.update_ngoc(rubyCost);
                p.updateMoney();
                p.getService().send_box_ThongBao_OK("Không thể mở rộng thêm hành trang!");
                return false;
            }
        }
    }
}





