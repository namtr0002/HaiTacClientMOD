package model;

import zabstracts.AbsVongQuayWC;
import network.Message;
import core.ZUtil;
import template.ItemTemplate4;
import template.ItemTemplate7;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VongQuayWC extends AbsVongQuayWC {

    private static VongQuayWC instance;

    public static VongQuayWC gI() {
        if (instance == null) {
            instance = new VongQuayWC();
        }
        return instance;
    }

    public static class RewardWC {
        public byte cat;
        public short id;
        public String name;
        public short idIcon;
        public int quantity;
        public byte color;
        public int rate;

        public RewardWC(byte cat, short id, String name, short idIcon, int quantity, byte color, int rate) {
            this.cat = cat;
            this.id = id;
            this.name = name;
            this.idIcon = idIcon;
            this.quantity = quantity;
            this.color = color;
            this.rate = rate;
        }
    }

    public static final short VE_QUAY_WC_ID = 866;
    public static final short ICON_TRAM_VONG_QUAY = -1;

    public static final RewardWC[] WHEEL_ITEMS = new RewardWC[] {
        new RewardWC((byte) 4, (short) 10, "Bình HP cực lớn", (short) 48, 10, (byte) 1, 200),
        new RewardWC((byte) 4, (short) 11, "Bình MP cực lớn", (short) 49, 10, (byte) 1, 200),
        new RewardWC((byte) 7, (short) 0, "Đá Cường Hóa Cấp 1", (short) 347, 5, (byte) 2, 150),
        new RewardWC((byte) 7, (short) 1, "Đá Cường Hóa Cấp 2", (short) 348, 3, (byte) 2, 120),
        new RewardWC((byte) 4, (short) 221, "Rương Ác Quỷ", (short) 700, 1, (byte) 4, 80),
        new RewardWC((byte) 4, (short) 158, "Rương Đại Ác Quỷ", (short) 701, 1, (byte) 5, 50),
        new RewardWC((byte) 7, (short) 5, "Đá Siêu Cấp", (short) 352, 2, (byte) 4, 70),
        new RewardWC((byte) 4, (short) 80, "Thẻ Tăng EXP 200%", (short) 500, 2, (byte) 3, 100),
        new RewardWC((byte) 4, (short) 362, "Rương Vàng World Cup", (short) 800, 1, (byte) 5, 40),
        new RewardWC((byte) 4, (short) 225, "Đá Khảm Hoàn Mỹ", (short) 805, 2, (byte) 4, 60),
        new RewardWC((byte) 4, (short) 338, "Túi Beri Khổng Lồ", (short) 400, 1, (byte) 3, 90),
        new RewardWC((byte) 4, (short) 339, "Túi Ruby Khổng Lồ", (short) 401, 1, (byte) 4, 70),
        new RewardWC((byte) 4, (short) 131, "Búa Rèn Thần Bí", (short) 600, 1, (byte) 3, 80),
        new RewardWC((byte) 4, (short) 322, "Danh Hiệu World Cup", (short) 850, 1, (byte) 6, 30)
    };

    public VongQuayWC() {
        this.id = 82;
        this.name = "Vòng Quay World Cup";
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (p == null || p.isBot) return;
        if (p.getService() != null) {
            p.getService().send_quay_wc_show();
            sendListItems(p);
            sendInfo(p);
        }
    }

    @Override
    public void sendListItems(Player p) throws IOException {
        if (p == null || p.isBot) return;
        if (p.getService() != null) {
            byte[] cats = new byte[WHEEL_ITEMS.length];
            short[] icons = new short[WHEEL_ITEMS.length];
            for (int i = 0; i < WHEEL_ITEMS.length; i++) {
                cats[i] = WHEEL_ITEMS[i].cat;
                short icon = WHEEL_ITEMS[i].idIcon;
                if (WHEEL_ITEMS[i].cat == 7) {
                    var it7 = ItemTemplate7.get_it_by_id(WHEEL_ITEMS[i].id);
                    if (it7 != null) icon = it7.icon;
                } else if (WHEEL_ITEMS[i].cat == 4) {
                    var it4 = ItemTemplate4.get_it_by_id(WHEEL_ITEMS[i].id);
                    if (it4 != null) icon = it4.icon;
                }
                icons[i] = icon;
            }
            // Chỉ gửi list items, không gọi sendInfo ở đây để tránh packet subCmd=4 dư thừa
            p.getService().send_quay_wc_list_items(cats, icons);
        }
    }

    @Override
    public void sendInfo(Player p) throws IOException {
        if (p == null || p.isBot) return;
        if (p.getService() != null) {
            short numVe = 0;
            if (p.item != null) {
                numVe = (short) p.item.total_item_bag_by_id(4, VE_QUAY_WC_ID);
            }
            p.getService().send_quay_wc_info(ICON_TRAM_VONG_QUAY, numVe, (short) p.numQuayWC);
        }
    }

    private RewardWC rollItem() {
        int totalWeight = 0;
        for (RewardWC r : WHEEL_ITEMS) {
            totalWeight += r.rate;
        }
        int rand = ZUtil.random(totalWeight);
        int current = 0;
        for (RewardWC r : WHEEL_ITEMS) {
            current += r.rate;
            if (rand < current) {
                return r;
            }
        }
        return WHEEL_ITEMS[0];
    }

    private void giveReward(Player p, RewardWC reward) throws IOException {
        if (reward.cat == 4 || reward.cat == 7) {
            p.item.add_item_bag47(reward.cat, reward.id, reward.quantity);
        }
        p.item.updateInventory(false);
    }

    @Override
    public void processQuay(Player p, byte typeQuay) throws IOException {
        if (p == null || p.isBot) return;
        int spinCount = (typeQuay == 2) ? 3 : 1;
        int totalTickets = p.item.total_item_bag_by_id(4, VE_QUAY_WC_ID);
        int rubyCostPerSpin = 10;

        if (totalTickets >= spinCount) {
            p.item.remove_item47(4, VE_QUAY_WC_ID, spinCount);
        } else {
            int rubyNeeded = spinCount * rubyCostPerSpin;
            if (p.ruby < rubyNeeded) {
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ Vé Quay WC hoặc Ruby! Cần " + spinCount + " vé hoặc " + rubyNeeded + " Ruby.");
                }
                return;
            }
            p.update_ruby(-rubyNeeded);
        }

        List<RewardWC> wonRewards = new ArrayList<>();
        for (int i = 0; i < spinCount; i++) {
            RewardWC reward = rollItem();
            wonRewards.add(reward);
            giveReward(p, reward);
        }

        p.numQuayWC += spinCount;

        if (p.getService() != null) {
            int size = wonRewards.size();
            byte[] cats = new byte[size];
            String[] names = new String[size];
            short[] icons = new short[size];
            int[] quantities = new int[size];
            byte[] colors = new byte[size];
            for (int i = 0; i < size; i++) {
                RewardWC r = wonRewards.get(i);
                cats[i] = r.cat;
                names[i] = r.name;
                short icon = r.idIcon;
                if (r.cat == 7) {
                    var it7 = ItemTemplate7.get_it_by_id(r.id);
                    if (it7 != null) icon = it7.icon;
                } else if (r.cat == 4) {
                    var it4 = ItemTemplate4.get_it_by_id(r.id);
                    if (it4 != null) icon = it4.icon;
                }
                icons[i] = icon;
                quantities[i] = r.quantity;
                colors[i] = r.color;
            }
            p.getService().send_quay_wc_result(typeQuay, cats, names, icons, quantities, colors);
            sendInfo(p);
        }
    }

    @Override
    public void process(Player p, byte action, Message m) throws IOException {
        if (p == null || p.isBot) return;
        switch (action) {
            case 1:
                processQuay(p, (byte) 1);
                break;
            case 2:
                processQuay(p, (byte) 2);
                break;
            case 3:
                sendListItems(p);
                break;
            case 4:
                sendInfo(p);
                break;
            default:
                sendInfo(p);
                break;
        }
    }
}