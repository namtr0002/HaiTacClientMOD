package activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import itemz.Item;
import model.Player;
import historys.zLog;
import network.Service;
import core.ZUtil;
import network.Message;
import template.*;

public class Trade {

    public static synchronized void process(Player p, Message m2) throws IOException {
        if (p == null || p.isClosed || p.map == null) {
            return;
        }
        if (core.Manager.gI().isDuaTopCaoThu()) {
            p.getService().send_box_ThongBao_OK("Máy chủ đang trong thời gian Đua Top Cao Thủ!\nTính năng giao dịch trực tiếp tạm thời khóa để đảm bảo tính công bằng.");
            return;
        }
        int minHours = core.Manager.gI().trade_min_online_hours;
        if (minHours > 0 && !p.isOnlineEnough(minHours)) {
            p.getService().send_box_ThongBao_OK("Tài khoản của bạn cần online đủ " + minHours + " giờ mới có thể giao dịch!\n(Thời gian đã online: " + p.getOnlineDurationString() + " / " + minHours + " giờ)");
            return;
        }
        if (p instanceof model.DeTu || p.isDe) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện giao dịch!");
            return;
        }
        if (p.getConnStatus() != 1) {
            p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt thành viên");
            return;
        }
        if (!p.checkPassRuong()) {
            return;
        }
        byte action = m2.reader().readByte();
        int id = -1;
        byte cat = -1;
        int num = -1;
        String str = "";
        if (action == 1 || action == 6) {
            id = m2.reader().readShort();
            cat = m2.reader().readByte();
            num = m2.reader().readInt();
        }
        if (action == 2) {
            str = m2.reader().readUTF();
        }

        switch (action) {
            case 6: { // Request (0) or Accept Invitation (1)
                if (num == 1) { // accept invite
                    Player p0 = p.map != null ? p.map.get_player_by_id_inmap(id) : null;
                    if (p0 != null && !p0.isClosed && p0.map == p.map) {
                        if (minHours > 0 && !p0.isOnlineEnough(minHours)) {
                            p.getService().send_box_ThongBao_OK("Đối phương chưa online đủ " + minHours + " giờ, không thể giao dịch!");
                            p0.getService().send_box_ThongBao_OK("Tài khoản của bạn cần online đủ " + minHours + " giờ mới có thể tham gia giao dịch!\n(Thời gian đã online: " + p0.getOnlineDurationString() + " / " + minHours + " giờ)");
                            resetTradeState(p);
                            return;
                        }
                        if (p0.trade_target == null || !p0.trade_target.equals(p)) {
                            p.getService().send_box_ThongBao_OK("Đối phương đang giao dịch với người khác");
                            resetTradeState(p);
                            return;
                        }
                        Trade.show_table(p, p0.name);
                        Trade.show_table(p0, p.name);
                    } else {
                        p.getService().send_box_ThongBao_OK("Đối phương không online hoặc khác khu vực");
                        resetTradeState(p);
                    }
                } else if (num == 0) { // request invite
                    Player p0 = p.map != null ? p.map.get_player_by_id_inmap(id) : null;
                    if (p0 != null && !p0.isClosed && p0.map == p.map) {
                        if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                            p.getService().send_box_ThongBao_OK("Không thể giao dịch với lính đánh thuê hoặc đệ tử!");
                            return;
                        }
                        if (minHours > 0 && !p0.isOnlineEnough(minHours)) {
                            p.getService().send_box_ThongBao_OK("Đối phương chưa online đủ " + minHours + " giờ, không thể giao dịch!");
                            p0.getService().send_box_ThongBao_OK("Tài khoản của bạn cần online đủ " + minHours + " giờ mới có thể tham gia giao dịch!\n(Thời gian đã online: " + p0.getOnlineDurationString() + " / " + minHours + " giờ)");
                            return;
                        }
                        if (p0.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Đối phương đang có giao dịch");
                            return;
                        }
                        if (p.trade_target != null) {
                            p.getService().send_box_ThongBao_OK("Bạn đang đợi đối phương chấp nhận lời mời giao dịch");
                            return;
                        }
                        p0.trade_target = p;
                        p.trade_target = p0;

                        Message m = new Message(-49);
                        m.writer().writeByte(6);
                        m.writer().writeByte(1);
                        m.writer().writeShort(p.index_map);
                        m.writer().writeUTF(p.name);
                        p0.addmsg(m);
                        m.cleanup();
                    } else {
                        p.getService().send_box_ThongBao_OK("Đối phương không online");
                    }
                }
                break;
            }
            case 1: { // Add / Remove Item or Beri
                if (p.trade_target == null || p.trade_target.isClosed || p.trade_target.map != p.map || p.trade_target.trade_target != p) {
                    cancelTrade(p);
                    return;
                }
                if (p.is_lock_trade || p.trade_target.is_lock_trade) {
                    p.getService().send_box_ThongBao_OK("Không thể thay đổi khi đã khóa giao dịch");
                    return;
                }
                if (p.list_item_trade3 == null || p.list_item_trade47 == null) {
                    return;
                }

                // Add/Remove Item Wear (cat == 3, num == 1)
                if (num == 1 && cat == 3) {
                    if (id < 0 || id >= p.item.bag3.length) {
                        return;
                    }
                    Item_wear it_select = p.item.bag3[id];
                    if (it_select == null) {
                        return;
                    }

                    // Check if item is locked or non-tradeable
                    if (it_select.typelock == 1 || (it_select.template != null && it_select.template.typelock == 1)) {
                        p.getService().send_box_ThongBao_OK("Trang bị đã khóa không thể giao dịch!");
                        return;
                    }

                    boolean isAlreadyInTrade = false;
                    for (int i = 0; i < p.list_item_trade3.size(); i++) {
                        if (it_select.equals(p.list_item_trade3.get(i))) {
                            isAlreadyInTrade = true;
                            break;
                        }
                    }

                    if (isAlreadyInTrade) {
                        // Remove from trade
                        p.list_item_trade3.remove(it_select);
                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(3);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(3);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.addmsg(m);
                        m.cleanup();
                    } else {
                        // Add to trade
                        if (!Trade.can_add_item_trade(p)) {
                            p.getService().send_box_ThongBao_OK("Tối đa 4 vật phẩm giao dịch");
                            return;
                        }
                        if ((p.trade_target.item.able_bag() - p.list_item_trade3.size() - p.list_item_trade47.size()) < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang đối phương không đủ chỗ trống");
                            return;
                        }

                        p.list_item_trade3.add(it_select);

                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(3);
                        m.writer().writeByte(1);
                        Item.readUpdateItem(m.writer(), it_select, p);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(3);
                        m.writer().writeByte(1);
                        Item.readUpdateItem(m.writer(), it_select, p);
                        p.addmsg(m);
                        m.cleanup();
                    }
                } else if (num >= 0 && cat == 6 && id == 0) { // Set Beri
                    if (num < 0 || num > 2_000_000_000) {
                        p.getService().send_box_ThongBao_OK("Số tiền không hợp lệ (tối đa 2 tỷ)");
                        return;
                    }
                    if (p.get_vang() < num) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(num) + " beri");
                        return;
                    }
                    p.money_trade = num;

                    Message m = new Message(-49);
                    m.writer().writeByte(1);
                    m.writer().writeByte(1);
                    m.writer().writeByte(6);
                    m.writer().writeInt(num);
                    p.trade_target.addmsg(m);
                    m.cleanup();

                    m = new Message(-49);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(6);
                    m.writer().writeInt(num);
                    p.addmsg(m);
                    m.cleanup();
                } else if (num > 0 && cat == 7) { // Add/Remove Item 7
                    if (num > DataTemplate.MAX_ITEM_IN_BAG) {
                        return;
                    }
                    ItemTemplate7 it_temp = ItemTemplate7.get_it_by_id(id);
                    if (it_temp == null) {
                        return;
                    }
                    if (it_temp.istrade == 1) {
                        p.getService().send_box_ThongBao_OK("Vật phẩm này không thể giao dịch!");
                        return;
                    }

                    ItemBag47 existing = null;
                    for (int i = 0; i < p.list_item_trade47.size(); i++) {
                        if (p.list_item_trade47.get(i).category == 7 && p.list_item_trade47.get(i).id == id) {
                            existing = p.list_item_trade47.get(i);
                            break;
                        }
                    }

                    if (existing != null) {
                        // Remove from trade
                        p.list_item_trade47.remove(existing);

                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(7);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(7);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.addmsg(m);
                        m.cleanup();
                    } else {
                        // Add to trade
                        if (p.item.total_item_bag_by_id(7, id) < num) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + num + " " + it_temp.name);
                            return;
                        }
                        if (!Trade.can_add_item_trade(p)) {
                            p.getService().send_box_ThongBao_OK("Tối đa 4 vật phẩm giao dịch");
                            return;
                        }
                        int availableTargetSlots = p.trade_target.item.able_bag() - p.list_item_trade3.size() - p.list_item_trade47.size();
                        if (availableTargetSlots < 0) availableTargetSlots = 0;
                        int freeInTarget = 0;
                        if (p.trade_target.item.bag47 != null) {
                            for (ItemBag47 bit : p.trade_target.item.bag47) {
                                if (bit != null && bit.category == 7 && bit.id == id && bit.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                                    freeInTarget += (DataTemplate.MAX_ITEM_IN_BAG - bit.quant);
                                }
                            }
                        }
                        int slotsNeeded = (num <= freeInTarget) ? 0 : ((num - freeInTarget + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG);
                        if (slotsNeeded > availableTargetSlots) {
                            p.getService().send_box_ThongBao_OK("Hành trang đối phương không đủ chỗ trống");
                            return;
                        }

                        ItemBag47 it_add = new ItemBag47();
                        it_add.category = 7;
                        it_add.id = (short) id;
                        it_add.quant = (short) num;
                        p.list_item_trade47.add(it_add);

                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(7);
                        m.writer().writeByte(1);
                        m.writer().writeByte(id);
                        m.writer().writeShort(num);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(7);
                        m.writer().writeByte(1);
                        m.writer().writeByte(id);
                        m.writer().writeShort(num);
                        p.addmsg(m);
                        m.cleanup();
                    }
                } else if (num > 0 && cat == 4) { // Add/Remove Item 4
                    if (num > DataTemplate.MAX_ITEM_IN_BAG) {
                        return;
                    }
                    ItemTemplate4 it_temp = ItemTemplate4.get_it_by_id(id);
                    if (it_temp == null) {
                        return;
                    }
                    if (it_temp.istrade == 1) {
                        p.getService().send_box_ThongBao_OK("Vật phẩm này không thể giao dịch!");
                        return;
                    }

                    ItemBag47 existing = null;
                    for (int i = 0; i < p.list_item_trade47.size(); i++) {
                        if (p.list_item_trade47.get(i).category == 4 && p.list_item_trade47.get(i).id == id) {
                            existing = p.list_item_trade47.get(i);
                            break;
                        }
                    }

                    if (existing != null) {
                        // Remove from trade
                        p.list_item_trade47.remove(existing);

                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(4);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(4);
                        m.writer().writeByte(0);
                        m.writer().writeShort(id);
                        p.addmsg(m);
                        m.cleanup();
                    } else {
                        // Add to trade
                        if (p.item.total_item_bag_by_id(4, id) < num) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + num + " " + it_temp.name);
                            return;
                        }
                        if (!Trade.can_add_item_trade(p)) {
                            p.getService().send_box_ThongBao_OK("Tối đa 4 vật phẩm giao dịch");
                            return;
                        }
                        int availableTargetSlots = p.trade_target.item.able_bag() - p.list_item_trade3.size() - p.list_item_trade47.size();
                        if (availableTargetSlots < 0) availableTargetSlots = 0;
                        int freeInTarget = 0;
                        if (p.trade_target.item.bag47 != null) {
                            for (ItemBag47 bit : p.trade_target.item.bag47) {
                                if (bit != null && bit.category == 4 && bit.id == id && bit.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                                    freeInTarget += (DataTemplate.MAX_ITEM_IN_BAG - bit.quant);
                                }
                            }
                        }
                        int slotsNeeded = (num <= freeInTarget) ? 0 : ((num - freeInTarget + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG);
                        if (slotsNeeded > availableTargetSlots) {
                            p.getService().send_box_ThongBao_OK("Hành trang đối phương không đủ chỗ trống");
                            return;
                        }

                        ItemBag47 it_add = new ItemBag47();
                        it_add.category = 4;
                        it_add.id = (short) id;
                        it_add.quant = (short) num;
                        p.list_item_trade47.add(it_add);

                        Message m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(4);
                        m.writer().writeByte(1);
                        m.writer().writeShort(id);
                        m.writer().writeShort(num);
                        p.trade_target.addmsg(m);
                        m.cleanup();

                        m = new Message(-49);
                        m.writer().writeByte(1);
                        m.writer().writeByte(0);
                        m.writer().writeByte(4);
                        m.writer().writeByte(1);
                        m.writer().writeShort(id);
                        m.writer().writeShort(num);
                        p.addmsg(m);
                        m.cleanup();
                    }
                }
                break;
            }
            case 5: { // Cancel / Exit Trade
                if (p.trade_target != null) {
                    Player target = p.trade_target;
                    end_trade_by_disconnect(target, p, 0, "");
                    end_trade_by_disconnect(p, target, 0, "");
                } else {
                    resetTradeState(p);
                }
                break;
            }
            case 2: { // Chat Popup
                if (p.trade_target != null && !str.isEmpty()) {
                    if (str.length() > 100) {
                        str = str.substring(0, 100);
                    }
                    Message m = new Message(-49);
                    m.writer().writeByte(2);
                    m.writer().writeByte(1);
                    m.writer().writeUTF(str);
                    p.trade_target.addmsg(m);
                    m.cleanup();
                }
                break;
            }
            case 3: { // Lock Trade
                if (p.trade_target == null || p.trade_target.isClosed || p.trade_target.map != p.map || p.trade_target.trade_target != p) {
                    cancelTrade(p);
                    return;
                }
                if (p.is_lock_trade) {
                    return;
                }
                p.is_lock_trade = true;
                p.fee_trade = 0; // Free trade without ruby cost

                Message m = new Message(-49);
                m.writer().writeByte(3);
                m.writer().writeByte(1);
                p.trade_target.addmsg(m);
                m.cleanup();

                m = new Message(-49);
                m.writer().writeByte(3);
                m.writer().writeByte(0);
                p.addmsg(m);
                m.cleanup();

                p.trade_target.getService().send_box_ThongBao_OK(p.name + " đã khóa giao dịch");
                p.getService().send_box_ThongBao_OK("Bạn đã khóa giao dịch");
                break;
            }
            case 4: { // Accept / Confirm Trade
                if (p.trade_target == null || p.trade_target.isClosed || p.trade_target.map != p.map || p.trade_target.trade_target != p) {
                    cancelTrade(p);
                    return;
                }
                if (!p.is_lock_trade || !p.trade_target.is_lock_trade) {
                    p.getService().send_box_ThongBao_OK("Cả 2 bên cần phải khóa giao dịch trước");
                    return;
                }
                if (p.is_accept_trade) {
                    return;
                }
                p.is_accept_trade = true;

                if (p.trade_target.is_accept_trade) {
                    // Both confirmed -> execute transfer atomically
                    executeTrade(p, p.trade_target);
                } else {
                    Message m = new Message(-49);
                    m.writer().writeByte(4);
                    m.writer().writeByte(1);
                    p.trade_target.addmsg(m);
                    m.cleanup();

                    m = new Message(-49);
                    m.writer().writeByte(4);
                    m.writer().writeByte(0);
                    p.addmsg(m);
                    m.cleanup();
                }
                break;
            }
        }
    }

    private static void executeTrade(Player p1, Player p2) throws IOException {
        Player first = p1.IDPlayer < p2.IDPlayer ? p1 : p2;
        Player second = p1.IDPlayer < p2.IDPlayer ? p2 : p1;

        synchronized (first) {
            synchronized (second) {
                // 1. Basic validation
                if (p1.isClosed || p2.isClosed || p1.map == null || p2.map == null || p1.map != p2.map
                        || p1.trade_target != p2 || p2.trade_target != p1
                        || !p1.is_lock_trade || !p2.is_lock_trade
                        || !p1.is_accept_trade || !p2.is_accept_trade
                        || p1.list_item_trade3 == null || p2.list_item_trade3 == null
                        || p1.list_item_trade47 == null || p2.list_item_trade47 == null) {
                    cancelTrade(p1);
                    return;
                }

                // 2. Beri validation
                if (p1.money_trade < 0 || p2.money_trade < 0
                        || p1.money_trade > 2_000_000_000L || p2.money_trade > 2_000_000_000L) {
                    p1.getService().send_box_ThongBao_OK("Số tiền giao dịch không hợp lệ!");
                    p2.getService().send_box_ThongBao_OK("Số tiền giao dịch không hợp lệ!");
                    cancelTrade(p1);
                    return;
                }
                if (p1.get_vang() < p1.money_trade) {
                    p1.getService().send_box_ThongBao_OK("Bạn không đủ beri để hoàn tất giao dịch!");
                    p2.getService().send_box_ThongBao_OK(p1.name + " không đủ beri để hoàn tất giao dịch!");
                    cancelTrade(p1);
                    return;
                }
                if (p2.get_vang() < p2.money_trade) {
                    p2.getService().send_box_ThongBao_OK("Bạn không đủ beri để hoàn tất giao dịch!");
                    p1.getService().send_box_ThongBao_OK(p2.name + " không đủ beri để hoàn tất giao dịch!");
                    cancelTrade(p1);
                    return;
                }
                if (p1.get_vang() - p1.money_trade + p2.money_trade > 2_000_000_000L) {
                    p1.getService().send_box_ThongBao_OK("Beri của bạn sau giao dịch vượt quá giới hạn 2 tỷ!");
                    p2.getService().send_box_ThongBao_OK("Beri của đối phương sau giao dịch vượt quá giới hạn 2 tỷ!");
                    cancelTrade(p1);
                    return;
                }
                if (p2.get_vang() - p2.money_trade + p1.money_trade > 2_000_000_000L) {
                    p2.getService().send_box_ThongBao_OK("Beri của bạn sau giao dịch vượt quá giới hạn 2 tỷ!");
                    p1.getService().send_box_ThongBao_OK("Beri của đối phương sau giao dịch vượt quá giới hạn 2 tỷ!");
                    cancelTrade(p1);
                    return;
                }

                // 3. Validate Items Wear in P1 inventory
                for (Item_wear it : p1.list_item_trade3) {
                    if (it == null || it.typelock == 1) {
                        p1.getService().send_box_ThongBao_OK("Trang bị giao dịch không hợp lệ!");
                        p2.getService().send_box_ThongBao_OK("Trang bị giao dịch của đối phương không hợp lệ!");
                        cancelTrade(p1);
                        return;
                    }
                    boolean exists = false;
                    for (int i = 0; i < p1.item.bag3.length; i++) {
                        if (p1.item.bag3[i] != null && p1.item.bag3[i].equals(it)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        p1.getService().send_box_ThongBao_OK("Trang bị không còn trong hành trang!");
                        p2.getService().send_box_ThongBao_OK("Trang bị của " + p1.name + " không còn trong hành trang!");
                        cancelTrade(p1);
                        return;
                    }
                }

                // 4. Validate Items Wear in P2 inventory
                for (Item_wear it : p2.list_item_trade3) {
                    if (it == null || it.typelock == 1) {
                        p2.getService().send_box_ThongBao_OK("Trang bị giao dịch không hợp lệ!");
                        p1.getService().send_box_ThongBao_OK("Trang bị giao dịch của đối phương không hợp lệ!");
                        cancelTrade(p1);
                        return;
                    }
                    boolean exists = false;
                    for (int i = 0; i < p2.item.bag3.length; i++) {
                        if (p2.item.bag3[i] != null && p2.item.bag3[i].equals(it)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        p2.getService().send_box_ThongBao_OK("Trang bị không còn trong hành trang!");
                        p1.getService().send_box_ThongBao_OK("Trang bị của " + p2.name + " không còn trong hành trang!");
                        cancelTrade(p1);
                        return;
                    }
                }

                // 5. Validate Item 4/7 for P1
                Map<String, Integer> p1Req47 = new HashMap<>();
                for (ItemBag47 it : p1.list_item_trade47) {
                    if (it == null || it.quant <= 0) {
                        cancelTrade(p1);
                        return;
                    }
                    String key = it.category + "_" + it.id;
                    p1Req47.put(key, p1Req47.getOrDefault(key, 0) + (int) it.quant);
                }
                for (Map.Entry<String, Integer> entry : p1Req47.entrySet()) {
                    String[] parts = entry.getKey().split("_");
                    int c = Integer.parseInt(parts[0]);
                    int itemId = Integer.parseInt(parts[1]);
                    int req = entry.getValue();
                    if (p1.item.total_item_bag_by_id(c, itemId) < req) {
                        p1.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                        p2.getService().send_box_ThongBao_OK(p1.name + " không đủ vật phẩm trong hành trang!");
                        cancelTrade(p1);
                        return;
                    }
                }

                // 6. Validate Item 4/7 for P2
                Map<String, Integer> p2Req47 = new HashMap<>();
                for (ItemBag47 it : p2.list_item_trade47) {
                    if (it == null || it.quant <= 0) {
                        cancelTrade(p1);
                        return;
                    }
                    String key = it.category + "_" + it.id;
                    p2Req47.put(key, p2Req47.getOrDefault(key, 0) + (int) it.quant);
                }
                for (Map.Entry<String, Integer> entry : p2Req47.entrySet()) {
                    String[] parts = entry.getKey().split("_");
                    int c = Integer.parseInt(parts[0]);
                    int itemId = Integer.parseInt(parts[1]);
                    int req = entry.getValue();
                    if (p2.item.total_item_bag_by_id(c, itemId) < req) {
                        p2.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                        p1.getService().send_box_ThongBao_OK(p2.name + " không đủ vật phẩm trong hành trang!");
                        cancelTrade(p1);
                        return;
                    }
                }

                // 7. Validate inventory space for P1 receiving from P2
                int p1EmptySlots = p1.item.able_bag() + p1.list_item_trade3.size();
                for (Map.Entry<String, Integer> entry : p1Req47.entrySet()) {
                    String[] parts = entry.getKey().split("_");
                    int c = Integer.parseInt(parts[0]);
                    int itemId = Integer.parseInt(parts[1]);
                    if (p1.item.total_item_bag_by_id(c, itemId) == entry.getValue()) {
                        p1EmptySlots++;
                    }
                }
                int p1NeededSlots = p2.list_item_trade3.size();
                Map<String, Integer> p1FreeSpace = new HashMap<>();
                for (ItemBag47 it : p2.list_item_trade47) {
                    String key = it.category + "_" + it.id;
                    int free = p1FreeSpace.computeIfAbsent(key, k -> {
                        int space = 0;
                        if (p1.item.bag47 != null) {
                            for (ItemBag47 bit : p1.item.bag47) {
                                if (bit != null && bit.category == it.category && bit.id == it.id && bit.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                                    space += (DataTemplate.MAX_ITEM_IN_BAG - bit.quant);
                                }
                            }
                        }
                        int givenAway = p1Req47.getOrDefault(key, 0);
                        space += givenAway;
                        return space;
                    });
                    if (it.quant <= free) {
                        p1FreeSpace.put(key, free - it.quant);
                    } else {
                        int overflow = it.quant - free;
                        int slots = (overflow + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG;
                        p1NeededSlots += slots;
                        p1FreeSpace.put(key, (slots * DataTemplate.MAX_ITEM_IN_BAG) - overflow);
                    }
                }
                if (p1NeededSlots > p1EmptySlots) {
                    p1.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống để nhận vật phẩm!");
                    p2.getService().send_box_ThongBao_OK("Hành trang của " + p1.name + " không đủ chỗ trống!");
                    cancelTrade(p1);
                    return;
                }

                // 8. Validate inventory space for P2 receiving from P1
                int p2EmptySlots = p2.item.able_bag() + p2.list_item_trade3.size();
                for (Map.Entry<String, Integer> entry : p2Req47.entrySet()) {
                    String[] parts = entry.getKey().split("_");
                    int c = Integer.parseInt(parts[0]);
                    int itemId = Integer.parseInt(parts[1]);
                    if (p2.item.total_item_bag_by_id(c, itemId) == entry.getValue()) {
                        p2EmptySlots++;
                    }
                }
                int p2NeededSlots = p1.list_item_trade3.size();
                Map<String, Integer> p2FreeSpace = new HashMap<>();
                for (ItemBag47 it : p1.list_item_trade47) {
                    String key = it.category + "_" + it.id;
                    int free = p2FreeSpace.computeIfAbsent(key, k -> {
                        int space = 0;
                        if (p2.item.bag47 != null) {
                            for (ItemBag47 bit : p2.item.bag47) {
                                if (bit != null && bit.category == it.category && bit.id == it.id && bit.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                                    space += (DataTemplate.MAX_ITEM_IN_BAG - bit.quant);
                                }
                            }
                        }
                        int givenAway = p2Req47.getOrDefault(key, 0);
                        space += givenAway;
                        return space;
                    });
                    if (it.quant <= free) {
                        p2FreeSpace.put(key, free - it.quant);
                    } else {
                        int overflow = it.quant - free;
                        int slots = (overflow + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG;
                        p2NeededSlots += slots;
                        p2FreeSpace.put(key, (slots * DataTemplate.MAX_ITEM_IN_BAG) - overflow);
                    }
                }
                if (p2NeededSlots > p2EmptySlots) {
                    p2.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống để nhận vật phẩm!");
                    p1.getService().send_box_ThongBao_OK("Hành trang của " + p2.name + " không đủ chỗ trống!");
                    cancelTrade(p1);
                    return;
                }

                // 9. ATOMIC EXECUTION:
                // A. Beri transfer
                if (p1.money_trade > 0) {
                    p1.update_vang(-p1.money_trade);
                    p2.update_vang(p1.money_trade);
                }
                if (p2.money_trade > 0) {
                    p2.update_vang(-p2.money_trade);
                    p1.update_vang(p2.money_trade);
                }
                p1.updateMoney();
                p2.updateMoney();

                String log_p1 = "Giao dịch với " + p2.name + " nhận " + p2.money_trade + " beri, chuyển " + p1.money_trade + " beri. Nhận: ";
                String log_p2 = "Giao dịch với " + p1.name + " nhận " + p1.money_trade + " beri, chuyển " + p2.money_trade + " beri. Nhận: ";

                // B. Remove Item Wear from bags first
                List<Item_wear> p1ItemsToGive = new ArrayList<>();
                for (Item_wear it : p1.list_item_trade3) {
                    for (int i = 0; i < p1.item.bag3.length; i++) {
                        if (p1.item.bag3[i] != null && p1.item.bag3[i].equals(it)) {
                            p1ItemsToGive.add(p1.item.bag3[i]);
                            p1.item.bag3[i] = null;
                            break;
                        }
                    }
                }

                List<Item_wear> p2ItemsToGive = new ArrayList<>();
                for (Item_wear it : p2.list_item_trade3) {
                    for (int i = 0; i < p2.item.bag3.length; i++) {
                        if (p2.item.bag3[i] != null && p2.item.bag3[i].equals(it)) {
                            p2ItemsToGive.add(p2.item.bag3[i]);
                            p2.item.bag3[i] = null;
                            break;
                        }
                    }
                }

                // C. Remove Item 4/7 from bags
                for (ItemBag47 it : p1.list_item_trade47) {
                    p1.item.remove_item47(it.category, it.id, it.quant);
                }
                for (ItemBag47 it : p2.list_item_trade47) {
                    p2.item.remove_item47(it.category, it.id, it.quant);
                }

                // D. Deliver items to recipients
                // P1 receives from P2
                for (Item_wear it : p2ItemsToGive) {
                    p1.item.add_item_bag3(it);
                    log_p1 += "x1 " + (it.template != null ? it.template.name : "Trang bị") + " +" + it.levelUp + " (mdakham " + it.mdakham.length + "), ";
                }
                for (ItemBag47 it : p2.list_item_trade47) {
                    p1.item.add_item_bag47(it.category, it.id, it.quant);
                    if (it.category == 4) {
                        log_p1 += String.format("x%s %s, ", it.quant, ItemTemplate4.get_item_name(it.id));
                    } else {
                        log_p1 += String.format("x%s %s, ", it.quant, ItemTemplate7.get_item_name(it.id));
                    }
                }

                // P2 receives from P1
                for (Item_wear it : p1ItemsToGive) {
                    p2.item.add_item_bag3(it);
                    log_p2 += "x1 " + (it.template != null ? it.template.name : "Trang bị") + " +" + it.levelUp + " (mdakham " + it.mdakham.length + "), ";
                }
                for (ItemBag47 it : p1.list_item_trade47) {
                    p2.item.add_item_bag47(it.category, it.id, it.quant);
                    if (it.category == 4) {
                        log_p2 += String.format("x%s %s, ", it.quant, ItemTemplate4.get_item_name(it.id));
                    } else {
                        log_p2 += String.format("x%s %s, ", it.quant, ItemTemplate7.get_item_name(it.id));
                    }
                }

                // E. Save and sync inventories
                p1.item.updateInventory(false);
                p2.item.updateInventory(false);

                // F. Log
                zLog.gI().add_log(p1, log_p1);
                zLog.gI().add_log(p2, log_p2);

                // G. Clean finish
                end_trade_by_disconnect(p1, p2, 1, "");
                end_trade_by_disconnect(p2, p1, 1, "");
            }
        }
    }

    public static void cancelTrade(Player p) {
        if (p == null) return;
        Player target = p.trade_target;
        if (target == null) {
            synchronized (p) {
                resetTradeState(p);
                try {
                    end_trade_by_disconnect(p, p, 0, "");
                } catch (Exception ignored) {}
            }
            return;
        }
        Player first = p.IDPlayer < target.IDPlayer ? p : target;
        Player second = p.IDPlayer < target.IDPlayer ? target : p;
        synchronized (first) {
            synchronized (second) {
                try {
                    end_trade_by_disconnect(target, p, 0, "");
                    end_trade_by_disconnect(p, target, 0, "");
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static void resetTradeState(Player p) {
        if (p == null) return;
        p.fee_trade = 0;
        p.money_trade = 0;
        p.is_lock_trade = false;
        p.is_accept_trade = false;
        p.list_item_trade3 = null;
        p.list_item_trade47 = null;
        p.trade_target = null;
    }

    private static boolean can_add_item_trade(Player p) {
        if (p.list_item_trade3 == null || p.list_item_trade47 == null) return false;
        return (p.list_item_trade3.size() + p.list_item_trade47.size() < 4);
    }

    public static void end_trade_by_disconnect(Player p_mine, Player p_target, int type,
            String name_exit) throws IOException {
        if (p_mine == null) return;
        try {
            Message m = new Message(-49);
            m.writer().writeByte(5);
            m.writer().writeByte(0);
            if (type == 1) {
                m.writer().writeUTF("Giao dịch với " + (p_target != null ? p_target.name : "") + " hoàn tất");
            } else if (type == 0) {
                m.writer().writeUTF((p_target != null ? p_target.name : "Đối phương") + " hủy giao dịch");
            } else if (type == 2) {
                m.writer().writeUTF("Giao dịch bị hủy bỏ vì " + name_exit
                        + " không đủ khả năng để trả phí cho giao dịch này");
            }
            p_mine.addmsg(m);
            m.cleanup();
        } catch (Exception ignored) {
        } finally {
            resetTradeState(p_mine);
        }
    }

    public static void show_table(Player p, String name) throws IOException {
        if (p == null) return;
        p.list_item_trade3 = new ArrayList<>();
        p.list_item_trade47 = new ArrayList<>();
        p.fee_trade = 0;
        p.money_trade = 0;
        p.is_lock_trade = false;
        p.is_accept_trade = false;

        Message m = new Message(-49);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeUTF(name != null ? name : "");
        p.addmsg(m);
        m.cleanup();
    }
}
