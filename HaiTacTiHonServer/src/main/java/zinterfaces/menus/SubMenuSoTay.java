package zinterfaces.menus;

import model.Player;
import zinterfaces.iMenu;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.ItemFashion;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubMenuSoTay implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{-989, -988};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == -988) {
            if (index == 0) { // Thường
                if (p.active_so_tay() == 0) {
                    if (p.get_ngoc() < 500) {
                        p.getService().send_box_ThongBao_OK("Không đủ 500 Ruby");
                        return;
                    }
                    p.update_ngoc(-500);
                    p.update_active_so_tay(1);
                    p.getService().send_box_ThongBao_OK("Kích hoạt Sổ Tay Hải Tặc Thường thành công");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn đã kích hoạt Sổ Tay Hải Tặc rồi");
                }
            } else if (index == 1) { // Cao Cấp
                if (p.so_tay_vip() == 0) {
                    if (p.get_ngoc() < 1000) {
                        p.getService().send_box_ThongBao_OK("Không đủ 1000 Ruby");
                        return;
                    }
                    p.update_ngoc(-1000);
                    p.update_so_tay_vip(1);
                    p.getService().send_box_ThongBao_OK("Kích hoạt Sổ Tay Hải Tặc Cao Cấp thành công");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn đã kích hoạt Sổ Tay Hải Tặc Cao Cấp rồi");
                }
            }
            return;
        }

        if (index >= 0 && index < 20) { 
            int requiredExp = 1000;
            if (p.exp_so_tay() < requiredExp) {
                p.getService().send_box_ThongBao_OK("Bạn Không đủ 1000 EXP Sổ Tay. Bạn có " + p.exp_so_tay() + " EXP!");
                return;
            }
            if (p.so_tay() < index) {
                p.getService().send_box_ThongBao_OK("Bạn chưa nhận quà ở các cấp thấp hơn. Hãy hoàn thành các cấp trước đó.");
                return;
            }
            if (p.so_tay() >= index + 1) {
                p.getService().send_box_ThongBao_OK("Bạn đã nhận quà ở cấp " + (index + 1) + " hoặc cấp cao hơn.");
                return;
            }
            List<GiftBox> list = new ArrayList<>();
            int giftItemId = 0;
            int giftType = 0;
            int giftQuantity = 1;

            if (index >= 0 && index <= 9) { 
                switch (index) {
                    case 0:
                        giftItemId = 10; 
                        giftType = 7;    
                        giftQuantity = 15;
                        break;
                    case 1:
                        giftItemId = 339; 
                        giftType = 4;     
                        giftQuantity = 5; 
                        break;
                    case 2:
                        giftItemId = 1;
                        giftType = 4;
                        giftQuantity = 5000;
                        break;
                    case 3:
                        giftItemId = 549;
                        giftType = 4;
                        giftQuantity = 2;
                        break;
                    case 4:
                        giftItemId = 691;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 5:
                        giftItemId = 691;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 6:
                        giftItemId = 823;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 7:
                        giftItemId = 551;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 8:
                        giftItemId = 323;
                        giftType = 4;
                        giftQuantity = 2;
                        break;
                    case 9:
                        giftItemId = 62;
                        giftType = 105;
                        giftQuantity = 1;
                        break;
                }
            } else if (index >= 10 && index < 20) { 
                if (p.so_tay_vip() <= 0) { 
                    p.getService().send_box_ThongBao_OK("Để nhận quà từ cấp 10 trở lên, bạn cần nạp tiền!");
                    return;
                }

                switch (index) {
                    case 10:
                        giftItemId = 10;
                        giftType = 7;
                        giftQuantity = 30;
                        break;
                    case 11:
                        giftItemId = 1;
                        giftType = 4;
                        giftQuantity = 5000;
                        break;
                    case 12:
                        giftItemId = 732;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 13:
                        giftItemId = 732;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 14:
                        giftItemId = 325;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 15:
                        giftItemId = 325;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 16:
                        giftItemId = 326;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 17:
                        giftItemId = 323;
                        giftType = 4;
                        giftQuantity = 5;
                        break;
                    case 18:
                        giftItemId = 832;
                        giftType = 4;
                        giftQuantity = 1;
                        break;
                    case 19:
                        giftItemId = 36;
                        giftType = 105;
                        giftQuantity = 1;
                        break;
                }
            }

            if (giftType == 4) {
                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(giftItemId);
                if (itemTemplate4 != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = itemTemplate4.id;
                    gb4.type = 4;
                    gb4.name = itemTemplate4.name;
                    gb4.icon = itemTemplate4.icon;
                    gb4.num = giftQuantity;
                    gb4.color = 0;
                    list.add(gb4);
                }
            } else if (giftType == 7) {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(giftItemId);
                if (itemTemplate7 != null) {
                    GiftBox gb7 = new GiftBox();
                    gb7.id = itemTemplate7.id;
                    gb7.type = 7;
                    gb7.name = itemTemplate7.name;
                    gb7.icon = itemTemplate7.icon;
                    gb7.num = giftQuantity;
                    gb7.color = 0;
                    list.add(gb7);
                }
            } else if (giftType == 105) {
                ItemFashion itemFashion = ItemFashion.get_item(giftItemId);
                if (itemFashion != null) {
                    GiftBox gb4 = new GiftBox();
                    gb4.id = itemFashion.ID;
                    gb4.type = 105;
                    gb4.name = itemFashion.name;
                    gb4.icon = itemFashion.idIcon;
                    gb4.num = giftQuantity;
                    gb4.color = 0;
                    list.add(gb4);
                }
            }

            if (list.size() > 0) {
                Service.send_gift(p, 1, "Nhận Quà Sổ Tay", "Cấp " + (index + 1), list, true);
                p.update_exp_so_tay(-requiredExp);
                p.update_level_so_tay(1);
            }
        }
    }
}
