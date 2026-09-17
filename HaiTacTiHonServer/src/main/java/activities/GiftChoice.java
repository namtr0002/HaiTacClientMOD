package activities;

import model.Player;
import network.Service;
import template.GiftBox;
import template.ItemFashion;
import template.ItemFashionP2;
import template.ItemTemplate4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiftChoice {

    public static void handle(Player p, short idItem, byte cat, byte select) throws IOException {
        if (p == null || cat != 4) return;

        if (idItem == -999) {
            zabstracts.AbsDiemDanh handler = zabstracts.AbsDiemDanh.get(idItem);
            if (handler != null) {
                handler.process(p, select);
            }
            return;
        }

        if (p.item.total_item_bag_by_id(4, idItem) <= 0) {
            p.getService().send_box_ThongBao_OK("Bạn không có vật phẩm này trong hành trang!");
            return;
        }

        // 1. Rương trái ác quỷ tự chọn (Item 4)
        if (idItem == 690) {
            if (p.check_already_have_devil_fruit()) {
                p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                return;
            }
            short[] listId = new short[]{32, 92, 93, 160, 161, 240};
            if (select >= 0 && select < listId.length) {
                giveItem4Choice(p, idItem, listId[select]);
            }
            return;
        }
        if (idItem == 894) {
            if (p.check_already_have_devil_fruit()) {
                p.getService().send_box_ThongBao_OK("Bạn đã có 1 trái ác quỷ trong hành trang!");
                return;
            }
            short[] listId = new short[]{160, 161, 240, 427, 869, 870, 873};
            if (select >= 0 && select < listId.length) {
                giveItem4Choice(p, idItem, listId[select]);
            }
            return;
        }

        if (idItem == 188) {
            short[] listId = new short[]{183, 184, 185, 186, 187};
            if (select >= 0 && select < listId.length) {
                giveItem4Choice(p, idItem, listId[select]);
            }
            return;
        }

        // 2. Rương thời trang có thời hạn 30 ngày (Item 358)
        if (idItem == 358) {
            short[] listId = (p.clazz == 4) ? new short[]{50, 52, 79} : new short[]{49, 51, 78};
            if (select >= 0 && select < listId.length) {
                ItemFashion itf = ItemFashion.get_item(listId[select]);
                giveFashionChoice30Days(p, idItem, itf);
            }
            return;
        }

        // 3. Các rương thời trang vĩnh viễn
        short[] listId = null;
        switch (idItem) {
            case 739:
                listId = new short[]{65, 66, 70, 72};
                break;
            case 726:
                listId = (p.clazz == 4) ? new short[]{63, 58, 60} : new short[]{62, 58, 60};
                break;
            case 748:
                listId = new short[]{122};
                break;
            case 749:
                listId = new short[]{123};
                break;
            case 750:
                listId = new short[]{2};
                break;
            case 736:
                listId = new short[]{120};
                break;
            case 526:
                listId = (p.clazz == 4) ? new short[]{92, 81} : new short[]{92, 80};
                break;
            case 589:
                listId = new short[]{112, 113, 114, 115, 116, 117};
                break;
            case 456:
                listId = new short[]{110, 120, 123};
                break;
            case 356:
                listId = (p.clazz == 4) ? new short[]{23, 55, 54} : new short[]{23, 55, 53};
                break;
            case 469:
                listId = new short[]{122, 74, 126};
                break;
            case 482:
                listId = new short[]{92, 58, 36};
                break;
            case 518:
                listId = (p.clazz == 4) ? new short[]{77, 79, 50} : new short[]{77, 78, 49};
                break;
            case 520:
                listId = new short[]{93, 94};
                break;
            case 568:
                listId = new short[]{98};
                break;
            case 581:
                listId = new short[]{102};
                break;
            case 600:
                listId = new short[]{121};
                break;
            case 621:
                listId = new short[]{98};
                break;
            case 622:
                listId = new short[]{75, 76};
                break;
            case 637:
                listId = (p.clazz == 4) ? new short[]{50, 52} : new short[]{49, 51};
                break;
            case 638:
                listId = (p.clazz == 4) ? new short[]{77, 79} : new short[]{77, 78};
                break;
            case 778:
                listId = new short[]{130};
                break;
            case 790:
                listId = new short[]{99, 100, 101};
                break;
            case 791:
                listId = new short[]{75, 76};
                break;
            case 792:
                listId = new short[]{129};
                break;
            case 793:
                listId = new short[]{130};
                break;
            case 821:
                listId = new short[]{84};
                break;
            case 822:
                listId = new short[]{125};
                break;
            case 840:
                listId = new short[]{124};
                break;
            case 843:
                listId = new short[]{56};
                break;
            case 844:
                listId = new short[]{57};
                break;
            case 887:
                listId = new short[]{98};
                break;
            case 906:
                listId = new short[]{131, 0};
                break;
            case 907:
                listId = new short[]{1, 125};
                break;
        }

        if (listId != null && select >= 0 && select < listId.length) {
            ItemFashion itf = ItemFashion.get_item(listId[select]);
            giveFashionChoice(p, idItem, itf);
        }
    }

    private static void giveFashionChoice(Player p, short boxId, ItemFashion itf) throws IOException {
        if (itf == null) return;
        if (p.check_fashion(itf.ID) != null) {
            p.getService().send_box_ThongBao_OK("Bạn đã sở hữu thời trang này rồi!");
            return;
        }
        p.item.remove_item47(4, boxId, 1);
        List<GiftBox> listGift = new ArrayList<>();
        GiftBox gb_ = new GiftBox();
        gb_.id = itf.ID;
        gb_.type = 105;
        gb_.name = itf.name;
        gb_.icon = itf.idIcon;
        gb_.num = 1;
        gb_.color = 0;
        listGift.add(gb_);
        Service.send_gift(p, 1, ItemTemplate4.get_item_name(boxId), "Phần thưởng", listGift, true);
    }

    private static void giveFashionChoice30Days(Player p, short boxId, ItemFashion itf) throws IOException {
        if (itf == null) return;
        if (p.check_fashion(itf.ID) != null) {
            p.getService().send_box_ThongBao_OK("Bạn đã sở hữu thời trang này rồi!");
            return;
        }
        p.item.remove_item47(4, boxId, 1);
        ItemFashionP2 temp2 = new ItemFashionP2();
        temp2.id = itf.ID;
        temp2.expires = System.currentTimeMillis() + 60_000L * 60 * 24 * 30;
        p.fashion.add(temp2);
        p.update_fashionP2(temp2);
        if (p.map != null && p.map.players != null) {
            for (int i = 0; i < p.map.players.size(); i++) {
                Player p0 = p.map.players.get(i);
                if (p0 != null && p0.getService() != null) {
                    p0.getService().charWearing(p, false);
                }
            }
        }
        p.getService().UpdateInfoMaincharInfo();
        p.getService().send_box_ThongBao_OK("Bạn nhận được thời trang: " + itf.name + " (HSD: 30 ngày)");
    }

    private static void giveItem4Choice(Player p, short boxId, short targetItemId) throws IOException {
        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(targetItemId);
        if (it4 == null) return;
        List<GiftBox> listGift = new ArrayList<>();
        GiftBox gb_ = new GiftBox();
        gb_.id = it4.id;
        gb_.type = 4;
        gb_.name = it4.name;
        gb_.icon = it4.icon;
        gb_.num = 1;
        gb_.color = 0;
        listGift.add(gb_);
        if (!core.RewardService.hasEnoughBagSpace(p, listGift)) {
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống để mở rương!");
            return;
        }
        p.item.remove_item47(4, boxId, 1);
        Service.send_gift(p, 1, ItemTemplate4.get_item_name(boxId), "Phần thưởng", listGift, true);
    }
}
