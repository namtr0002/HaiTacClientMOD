package store;

import model.Player;
import network.Service;
import itemz.Item;
import network.Message;
import template.Item_wear;
import java.io.IOException;

/**
 * Thùng Rác — type = 119.
 * Cho phép player lấy lại item đã bỏ vào thùng rác với chi phí 5 ruby.
 * Không đọc dữ liệu từ store_data (không có item list cố định).
 */
public class TrashShop extends zabstracts.AbsShop {

    public TrashShop() {
        super(119, "Thùng Rác");
    }

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = type;
        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(name);
        m.writer().writeByte(3); // category item3
        m.writer().writeShort(p.item.save_item_wear.size());
        // Gửi theo thứ tự ngược (mới nhất lên đầu)
        for (int i = p.item.save_item_wear.size() - 1; i >= 0; i--) {
            Item_wear it = p.item.save_item_wear.get(i);
            if (it == null) continue;
            it.index = (short) i;
            Item.readUpdateItem(m.writer(), it, p);
            m.writer().writeByte(1); // 1 = ruby
            m.writer().writeInt(5);  // 5 ruby
        }
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        if (p == null || p.item == null) return;
        synchronized (p.item) {
            if (p.trade_target != null) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                return;
            }
            // id = vị trí trong save_item_wear, num phải là 1, cat = -1
            if (num != 1 || cat != -1) {
                p.getService().end_Dialog();
                return;
            }
            if (id < 0 || id >= p.item.save_item_wear.size()) {
                p.getService().end_Dialog();
                return;
            }

            Item_wear it = p.item.save_item_wear.get(id);
            if (it == null) {
                p.getService().end_Dialog();
                return;
            }

            if (p.get_ngoc() < 5) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                return;
            }

            if (p.item.able_bag() <= 0) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Hành trang đầy");
                return;
            }

            // Remove from save_item_wear first before adding to bag to prevent dupe
            p.item.save_item_wear.remove((int) id);
            if (p.item.add_item_bag3(it)) {
                p.update_ngoc(-5);
                p.updateMoney();
                p.item.updateInventory(false);
                p.getService().Send_UI_Shop(119);
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK(
                    "Lấy " + it.template.name + " về thành công, phí 5 ruby"
                );
            } else {
                // Rollback if unable to add to bag
                p.item.save_item_wear.add((int) id, it);
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không thể lấy vật phẩm vào hành trang!");
            }
        }
    }
}
