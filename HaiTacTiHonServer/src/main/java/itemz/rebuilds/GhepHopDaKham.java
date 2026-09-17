package itemz.rebuilds;

import zabstracts.AbsCombie;
import model.Player;
import itemz.Rebuild_Item;
import network.Message;
import template.ItemTemplate4;
import core.ZUtil;
import java.io.IOException;

public class GhepHopDaKham extends AbsCombie {
    @Override
    public byte getType() {
        return 4;
    }

    public static boolean isGhepDaHopLe(short idItem) {
        if (idItem >= 44 && idItem <= 78) {
            int tier = (idItem - 44) % 6;
            return tier < 5; // Cấp 1..5
        }
        if (idItem >= 221 && idItem <= 225) { // Hải Thạch Cấp 1..5
            return true;
        }
        if (idItem >= 362 && idItem <= 366) { // Hổ Phách Cấp 1..5
            return true;
        }
        return false;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (cat == 4 && num > 0 && type == 4 && action == 1) { // bo da kham vao de hop
            if (num < 3) {
                p.getService().send_box_ThongBao_OK("Số lượng nhập vào của bạn không được nhỏ hơn 3 viên!");
                return;
            }
            if (p.item.total_item_bag_by_id(4, idItem) < num) {
                p.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                return;
            }
            if (isGhepDaHopLe(idItem)) {
                p.getService().sendRebuildPutItem(idItem, (byte) 4, num);
            } else {
                p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ hoặc đã đạt cấp tối đa!");
            }
        } else if (cat == 4 && num > 0 && type == 4 && action == 5) { // hop da kham
            if (num < 3 || num > 30000) {
                p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
                return;
            }
            if (isGhepDaHopLe(idItem)) {
                int time_success = 0;
                int time_lose = 0;
                int tier = Rebuild_Item.get_percent_hop_ngoc(idItem);
                int percent = (tier >= 0 && tier < Rebuild_Item.PERCENT_HOP_NGOC.length) ? Rebuild_Item.PERCENT_HOP_NGOC[tier] : 0;

                synchronized (p.item) {
                    if (p.item.total_item_bag_by_id(4, idItem) < num) {
                        p.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                        return;
                    }
                    int remaining = num;
                    while (remaining >= 3) {
                        if (percent > ZUtil.random(120)) {
                            time_success++;
                        } else {
                            time_lose++;
                        }
                        remaining -= 3;
                    }
                    p.item.remove_item47(4, idItem, ((2 * time_lose) + (3 * time_success)));
                    if (time_success > 0) {
                        p.item.add_item_bag47(4, (short)(idItem + 1), time_success);
                    }
                    p.item.updateInventory(false);
                }
                
                p.getService().sendRebuildCombineHopDaKham("Sử dụng " + (3 * (time_lose + time_success)) + " "
                        + ItemTemplate4.get_item_name(idItem) + " để nâng cấp. Thành công "
                        + time_success + " lần, thất bại " + time_lose + " lần.",
                        idItem, (short)(idItem + 1), (short)time_success, (byte)4);
            } else {
                Rebuild_Item.show_table(p, 1);
                p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ hoặc đã đạt cấp tối đa!");
            }
        }
    }
}
