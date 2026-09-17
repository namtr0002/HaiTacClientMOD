package model;

import zabstracts.AbsVongQuay;
import itemz.MainItem;
import itemz.RandomMainItem;
import core.Manager;
import core.ZUtil;
import network.Message;
import template.ItemTemplate4;
import template.ItemTemplate7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EventLuckyWheel — Triển khai chuẩn Vòng Quay May Mắn Message 54 cho từng sự kiện.
 * Kế thừa AbsVongQuay, tự động gắn season key và danh sách phần thưởng theo từng sự kiện.
 */
public class EventLuckyWheel extends AbsVongQuay {

    private final int eventId;
    private final String eventName;
    private int costRuby = 100;
    private int ticketItemId = 866; // Mặc định: Vé vòng quay sự kiện (#866)

    private final List<RandomMainItem> previewItems = new ArrayList<>();
    private final List<RandomMainItem> rewardItems = new ArrayList<>();

    public EventLuckyWheel() {
        this(0, "Sự Kiện", "DEFAULT");
    }

    public EventLuckyWheel(int eventId, String eventName, String seasonKey) {
        this.id = 1000 + eventId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.name = "Vòng Quay " + eventName;
        this.key = "VONG_QUAY_EVENT_" + eventId;
        if (seasonKey != null && !seasonKey.trim().isEmpty()) {
            this.key = historys.HistoryManager.formatKey(this.key, seasonKey.trim());
        }
        initDefaultItems();
    }

    public void setCostRuby(int costRuby) {
        this.costRuby = costRuby;
    }

    public void setTicketItemId(int ticketItemId) {
        this.ticketItemId = ticketItemId;
    }

    public void addRewardItem(MainItem item, int weight) {
        rewardItems.add(new RandomMainItem(item, weight));
    }

    private void initDefaultItems() {
        previewItems.clear();
        rewardItems.clear();

        // 14 Icon preview hiển thị trên vòng quay
        previewItems.add(new RandomMainItem(new MainItem(158, 4, 1), 2));   // Rương Đại Ác Quỷ
        previewItems.add(new RandomMainItem(new MainItem(323, 4, 1), 3));   // Rương Hệ
        previewItems.add(new RandomMainItem(new MainItem(29, 4, 1), 3));    // Rương Ác Quỷ
        previewItems.add(new RandomMainItem(new MainItem(221, 4, 1), 3));   // Đá Thần Thoại 1
        previewItems.add(new RandomMainItem(new MainItem(226, 4, 1), 2));   // Đá Thần Thoại 6
        previewItems.add(new RandomMainItem(new MainItem(80, 4, 1), 3));    // x2 Exp SP
        previewItems.add(new RandomMainItem(new MainItem(133, 4, 1), 3));   // x3 Exp SP
        previewItems.add(new RandomMainItem(new MainItem(159, 4, 1), 3));   // x2 Skill SP
        previewItems.add(new RandomMainItem(new MainItem(349, 4, 1), 3));   // Đá Mài / Túi Beri
        previewItems.add(new RandomMainItem(new MainItem(339, 4, 1), 2));   // Bùa Sơ Cấp
        previewItems.add(new RandomMainItem(new MainItem(2, 7, 2), 4));     // Bột Vàng
        previewItems.add(new RandomMainItem(new MainItem(1, 7, 3), 5));     // Bột Cường Hóa
        previewItems.add(new RandomMainItem(new MainItem(4, 7, 2), 4));     // Đá Thô
        previewItems.add(new RandomMainItem(new MainItem(9, 7, 1), 3));     // Đá Ác Quỷ

        // Bảng phần thưởng có trọng số (weighted rewards)
        rewardItems.addAll(previewItems);
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (p == null) return;
        p.currentVongQuay = this;
        p.typeVongQuay = this.id;

        Message m = new Message(54);
        m.writer().writeByte(0); // Mở giao diện vòng quay
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void process(Player p, byte action, Message m) throws IOException {
        if (p == null) return;

        switch (action) {
            case 3: { // Gửi danh sách 14 icon preview hiển thị trên vòng quay
                Message m3 = new Message(54);
                m3.writer().writeByte(3);
                int count = Math.min(14, previewItems.size());
                m3.writer().writeByte(count);
                for (int i = 0; i < count; i++) {
                    RandomMainItem rmi = previewItems.get(i);
                    m3.writer().writeByte(rmi.item.cat);
                    m3.writer().writeShort(rmi.item.idIcon);
                }
                p.addmsg(m3);
                m3.cleanup();
                break;
            }
            case 1:
            case 2: { // Quay 1 lần (action 1) hoặc Quay 3 lần (action 2)
                int rolls = (action == 1) ? 1 : 3;
                int totalSlots = rolls * 3;

                if (p.item.able_bag() < Math.max(1, rolls)) {
                    p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                    return;
                }

                int ticketId = (ticketItemId > 0) ? ticketItemId : 866;
                int currentTickets = p.item.total_item_bag_by_id(4, ticketId);
                if (currentTickets < rolls) {
                    String ticketName = ItemTemplate4.get_item_name(ticketId);
                    if (ticketName == null || ticketName.trim().isEmpty()) {
                        ticketName = "Vé vòng quay sự kiện";
                    }
                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + rolls + " " + ticketName + "!");
                    return;
                }

                p.item.remove_item47(4, ticketId, rolls);

                event.Event ev = event.EventManager.get(this.eventId);
                if (ev != null) {
                    event.EventData ed = ev.getOrCreateEventData(p);
                    if (ed != null) {
                        ed.data[action == 1 ? event.Event.INDEX_VQT : event.Event.INDEX_VQV] += rolls;
                    }
                }

                List<MainItem> listSlots = new ArrayList<>();
                List<MainItem> listAdd = new ArrayList<>();

                for (int i = 0; i < totalSlots; i++) {
                    // Tỷ lệ trúng ô: 60% trúng quà, 40% ô trống
                    if (ZUtil.random(100) < 40) {
                        listSlots.add(null);
                        continue;
                    }

                    MainItem rmi = RandomMainItem.random(rewardItems);
                    if (rmi == null) {
                        listSlots.add(null);
                        continue;
                    }

                    MainItem itemAdd = new MainItem(rmi.id, rmi.cat, rmi.num > 0 ? rmi.num : 1);

                    // Random cấp đá thần thoại 1 - 5
                    if (itemAdd.cat == 4 && itemAdd.id == 221) {
                        itemAdd = new MainItem(itemAdd.id + ZUtil.random(5), 4, 1);
                    }

                    // Thông báo thế giới nếu trúng quà cực hiếm
                    if (itemAdd.cat == 4 && (itemAdd.id == 158 || itemAdd.id == 323 || itemAdd.id == 29)) {
                        Manager.gI().chatKTG(0, p.name + " nhận được [" + itemAdd.name + "] khi quay " + this.name + ", thật đáng ngưỡng mộ!", 5);
                    }

                    listSlots.add(itemAdd);
                    listAdd.add(itemAdd);

                    // Thêm vật phẩm vào túi người chơi
                    if (itemAdd.cat == 3) {
                        template.ItemTemplate3 temp3 = template.ItemTemplate3.get_it_by_id(itemAdd.id);
                        if (temp3 != null) {
                            template.Item_wear itAdd = new template.Item_wear(temp3);
                            p.item.add_item_bag3(itAdd);
                        }
                    } else {
                        p.item.add_item_bag47(itemAdd.cat, itemAdd.id, itemAdd.num);
                    }
                }

                // Gửi kết quả vòng quay Message 54
                Message mRoll = new Message(54);
                mRoll.writer().writeByte(action);
                mRoll.writer().writeByte(listSlots.size());
                for (MainItem item : listSlots) {
                    if (item == null) {
                        mRoll.writer().writeByte(-1);
                        mRoll.writer().writeUTF("");
                        mRoll.writer().writeShort(-1);
                        mRoll.writer().writeInt(-1);
                        mRoll.writer().writeByte(-1);
                    } else {
                        mRoll.writer().writeByte(item.cat);
                        mRoll.writer().writeUTF(item.name != null ? item.name : "");
                        mRoll.writer().writeShort(item.idIcon);
                        mRoll.writer().writeInt(item.num);
                        mRoll.writer().writeByte(0);
                    }
                }
                p.addmsg(mRoll);
                mRoll.cleanup();

                p.item.updateInventory(false);

                // Hiển thị GiftBox thông báo quà nhận được (isAdd = false vì đã add trước đó)
                if (!listAdd.isEmpty()) {
                    MainItem.showGiftBox(p, this.name, "Chúc mừng bạn nhận được quà từ " + this.name + "!", listAdd, false, false);
                }
                break;
            }
            case 4: { // Dialog mua vé quay
                p.setyesNoDialog(new YesNoDialog(p, 36, "Vòng Quay " + eventName,
                        "Bạn cần mua bao nhiêu vé quay sự kiện? Giá mỗi vé là " + costRuby + " Ruby.",
                        new String[]{"1 Vé (" + costRuby + " Ruby)", "3 Vé (" + (costRuby * 3) + " Ruby)", "Nhập số lượng", "Hủy"},
                        new byte[]{-1, -1, -1, 1},
                        (val) -> {
                            if (val == 0) {
                                buyTicketDirect(p, 1);
                            } else if (val == 1) {
                                buyTicketDirect(p, 3);
                            } else if (val == 2) {
                                new InputDialog(p, 36, "Nhập số lượng", new String[]{"Số vé muốn mua:"}, inputs -> {
                                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                    try {
                                        int buyNum = Integer.parseInt(inputs[0].trim());
                                        if (buyNum <= 0 || buyNum > 1000) {
                                            p.getService().send_box_ThongBao_OK("Số lượng mua mỗi lần từ 1 đến 1.000 vé!");
                                            return;
                                        }
                                        buyTicketDirect(p, buyNum);
                                    } catch (NumberFormatException e) {
                                        p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).startInput();
                            }
                        }));
                p.getService().startYesNo();
                break;
            }
        }
    }

    private void buyTicketDirect(Player p, int buyNum) {
        if (p == null || buyNum <= 0 || buyNum > 1000) return;
        synchronized (p) {
            try {
                long totalCost = (long) buyNum * costRuby;
                if (totalCost > Integer.MAX_VALUE || totalCost <= 0) {
                    p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
                    return;
                }
                int rubyCost = (int) totalCost;
                if (p.get_ngoc() < rubyCost) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + rubyCost + " Ruby để mua vé!");
                    return;
                }
                int tId = (ticketItemId > 0) ? ticketItemId : 866;
                if (!p.item.can_add_item_bag47(4, tId, buyNum)) {
                    p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                    return;
                }
                p.update_ngoc(-rubyCost);
                try {
                    p.updateMoney();
                    p.item.add_item_bag47(4, tId, buyNum);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Mua thành công " + buyNum + " " + ItemTemplate4.get_item_name(tId) + "!");
                } catch (Exception ignored) {}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
