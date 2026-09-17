package model;

import zabstracts.AbsVongQuay;
import java.io.IOException;
import network.Message;
import network.Service;
import template.ItemBag47;
import template.ItemTemplate4;
import core.ZUtil;

public class VongQuayNami extends AbsVongQuay {

    private static VongQuayNami instance;

    public VongQuayNami() {
        this.id = 99;
        this.name = "Vòng Quay Nami Special";
    }

    public static VongQuayNami gI() {
        if (instance == null) {
            instance = new VongQuayNami();
        }
        return instance;
    }

    public static short[] ID_ITEM = new short[]{
        29, 158, 221, 222, 223, 224, 225, 159, 133, 174, 173, 7, 48, 112
    };

    private static ItemBag47 getRandomReward(Player p) {
        ItemBag47 result = new ItemBag47();
        result.category = 4;
        result.quant = 1;

        int rand = ZUtil.random(10_000);
        if (rand < 200) { // 2% Rương Ác Quỷ
            result.id = 29;
        } else if (rand < 600) { // 4% Đá Thần Thoại
            result.id = 221;
        } else if (rand < 1200) { // 6% Bùa Ma Thuật
            result.id = 222;
        } else if (rand < 2000) { // 8% Vé Cường Hóa
            result.id = 224;
        } else if (rand < 3000) { // 10% Vé X2 EXP
            result.id = 159;
        } else if (rand < 4200) { // 12% Thẻ Bài
            result.id = 158;
        } else if (rand < 6500) { // 23% HP Cao Cấp
            result.id = 173;
            result.quant = (short) ZUtil.random(5, 15);
        } else { // 35% MP Cao Cấp
            result.id = 174;
            result.quant = (short) ZUtil.random(5, 15);
        }
        return result;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.currentVongQuay = this;
        p.typeVongQuay = 99;
        achievement.NamiPassManager.NamiPassData data = achievement.NamiPassManager.getPassData(p);
        
        Message m = new Message(54);
        m.writer().writeByte(0);
        p.addmsg(m);
        m.cleanup();

        p.getService().send_box_ThongBao_OK("Vòng Quay Nami Special!\nBạn đang có: " + data.spinCount + " lượt quay.");
    }

    @Override
    public void process(Player p, byte action, Message m2) throws IOException {
        achievement.NamiPassManager.NamiPassData passData = achievement.NamiPassManager.getPassData(p);

        switch (action) {
            case 3: { // Send item icons list
                Message m = new Message(54);
                m.writer().writeByte(3);
                m.writer().writeByte(ID_ITEM.length);
                for (short itemId : ID_ITEM) {
                    m.writer().writeByte(4);
                    ItemTemplate4 temp = ItemTemplate4.get_it_by_id(itemId);
                    m.writer().writeShort(temp != null ? temp.icon : 0);
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 1: // Quay 1 lần
            case 2: { // Quay nhiều lần
                int ticketsNeeded = (action == 2) ? 3 : 1;
                int clientDisplayCount = (action == 2) ? 9 : 3;
                if (passData.spinCount < ticketsNeeded) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ lượt quay Nami Special! Cần " + ticketsNeeded + " lượt, hiện có " + passData.spinCount + " lượt.");
                    return;
                }

                passData.spinCount -= ticketsNeeded;
                achievement.NamiPassManager.savePassData(p);

                Message m = new Message(54);
                m.writer().writeByte(action);
                m.writer().writeByte(clientDisplayCount);
                
                StringBuilder rewardTxt = new StringBuilder("Phần thưởng Nami Special:\n");
                for (int i = 0; i < clientDisplayCount; i++) {
                    ItemBag47 reward = getRandomReward(p);
                    ItemTemplate4 temp = ItemTemplate4.get_it_by_id(reward.id);
                    String name = temp != null ? temp.name : "Vật phẩm";
                    short icon = temp != null ? temp.icon : 0;

                    m.writer().writeByte(reward.category);
                    m.writer().writeUTF(name);
                    m.writer().writeShort(icon);
                    m.writer().writeInt(reward.quant);
                    m.writer().writeByte(0); // color

                    rewardTxt.append("- ").append(name).append(" x").append(reward.quant).append("\n");

                    // Add reward to player inventory
                    p.item.add_item_bag47(reward.category, reward.id, reward.quant);
                }
                p.addmsg(m);
                m.cleanup();

                p.getService().send_box_ThongBao_OK(rewardTxt.toString() + "\nLượt quay còn lại: " + passData.spinCount);
                break;
            }
            case 4: { // Thẻ quay info
                p.getService().send_box_ThongBao_OK("Lượt quay Nami Special kiếm được bằng cách tăng Cấp Nami Pass!\nLượt quay hiện có: " + passData.spinCount);
                break;
            }
        }
    }
}
