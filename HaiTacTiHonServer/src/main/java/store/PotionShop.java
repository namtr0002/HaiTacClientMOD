package store;

import event.EventData;
import model.MyPet;
import model.Pet;
import model.Player;
import event.SuKienNoel;
import event.SuKienTrongCay;
import network.Message;
import template.DataTemplate;
import template.ItemTemplate4;
import template.ItemSell;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Quán ăn / Shop tiêu hao — store_id = 6, item category = 4 (ItemTemplate4).
 * - Luôn tự động tính toán bình HP, MP và thức ăn phù hợp theo cấp độ người chơi.
 * - Hỗ trợ nạp vật phẩm đặc biệt và thú cưng (id 685-688) từ store_data.
 */
public class PotionShop extends zabstracts.AbsShop {

    public PotionShop() {
        super(20, "Quán ăn");
    }

    private static StoreItem createDefaultStoreItem(short id) {
        ItemTemplate4 temp4 = ItemTemplate4.get_it_by_id(id);
        int coin = temp4 != null ? temp4.beri : 0;
        int ruby = temp4 != null ? temp4.ruby : 0;
        return new StoreItem(id, 6, id, 4, coin, ruby, -1, 0, -1);
    }

    private StoreItem findStoreItem(short id) {
        List<StoreItem> storeItems = zabstracts.AbsShop.getItemsForStore(6);
        if (storeItems != null) {
            for (StoreItem item : storeItems) {
                int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
                if (displayId == id || item.itemId == id) {
                    return item;
                }
            }
        }
        return null;
    }

    /** Tất cả item của shop này từ store_data */
    private List<StoreItem> getAllItems() {
        List<StoreItem> items = new ArrayList<>(zabstracts.AbsShop.getItemsForStore(6));
        if (items.isEmpty()) {
            for (short id : ItemSell.ITEM_POTION_SELL) {
                items.add(createDefaultStoreItem(id));
            }
        }
        return items;
    }

    /**
     * Lấy danh sách item hiển thị cho người chơi:
     * Kết hợp giữa bình HP/MP & thức ăn chuẩn theo cấp + vật phẩm mở rộng từ store_data / sự kiện.
     */
    private List<StoreItem> getFilteredItems(Player p) {
        boolean isNoel = SuKienNoel.gI().isNoel();
        int pLvGrp = (p != null) ? Math.min(10, Math.max(0, p.level / 10)) : 0;

        List<StoreItem> result = new ArrayList<>();
        Set<Integer> addedIds = new HashSet<>();

        // 1. Luôn nạp danh sách thức ăn / bình HP MP chuẩn theo cấp độ người chơi
        short[] basePotions = ItemSell.get_it_sell_potion(p);
        if (basePotions != null) {
            for (short id : basePotions) {
                StoreItem si = findStoreItem(id);
                if (si == null) {
                    si = createDefaultStoreItem(id);
                }
                result.add(si);
                addedIds.add((int) id);
            }
        }

        // 2. Thêm các item mở rộng từ store_data (nếu có) mà chưa có trong danh sách
        for (StoreItem item : getAllItems()) {
            if (addedIds.contains((int) item.itemId)) continue;
            if (item.itemId == 29) continue;
            if (item.reqLevel != -1 && item.reqLevel != 0 && item.reqLevel != pLvGrp) continue;
            if (item.eventId == 0
                    || (item.eventId > 0 && event.EventManager.isActive(item.eventId))) {
                result.add(item);
                addedIds.add((int) item.itemId);
            }
        }

        if (result.isEmpty()) {
            for (short id : ItemSell.ITEM_POTION_SELL) {
                result.add(createDefaultStoreItem(id));
            }
        }
        return result;
    }

    // ------------------------------------------------------------------ Shop interface

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = type;
        List<StoreItem> items = getFilteredItems(p);
        List<StoreItem> validItems = new ArrayList<>();
        for (StoreItem item : items) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            if (ItemTemplate4.get_it_by_id(displayId) != null) {
                validItems.add(item);
            }
        }
        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(name != null ? name : "Quán ăn");
        m.writer().writeByte(4); // category item4
        m.writer().writeShort(validItems.size());
        for (StoreItem item : validItems) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            m.writer().writeShort((short) displayId);
            m.writer().writeShort(1);
        }
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        StoreItem found = findStoreItem(id);

        if (found == null) {
            try {
                if (ItemSell.check_item_sell_potion(p, id) || ItemTemplate4.get_it_by_id(id) != null) {
                    found = createDefaultStoreItem(id);
                }
            } catch (Exception ignored) {}
        }

        if (found == null) {
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        int realItemId = found.itemId;

        // --- Mua thú cưng (pet) ---
        if (realItemId >= 685 && realItemId <= 688) {
            buyPet(p, (short) realItemId);
            return;
        }

        // Một số item chỉ mua được 1 cái / lần
        if (realItemId == 190) {
            buyWithQuantity(p, (short) realItemId, 1);
            return;
        }

        buyWithQuantity(p, (short) realItemId, num > 0 ? num : 1);
    }

    public void buyWithQuantity(Player p, short id, int num) throws IOException {
        if (p == null || num < 1 || num > 9999) {
            if (p != null) p.getService().end_Dialog();
            return;
        }
        StoreItem found = findStoreItem(id);
        if (found == null) {
            try {
                if (ItemSell.check_item_sell_potion(p, id) || ItemTemplate4.get_it_by_id(id) != null) {
                    found = createDefaultStoreItem(id);
                }
            } catch (Exception ignored) {}
        }
        if (found == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        int realItemId = found.itemId;

        // Thú cưng
        if (realItemId >= 685 && realItemId <= 688) {
            p.getService().end_Dialog();
            buyPet(p, (short) realItemId);
            return;
        }

        if (realItemId == 190) {
            num = 1;
        }

        // Kiểm tra túi
        if (!p.item.can_add_item_bag47(4, realItemId, num)) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return;
        }

        ItemTemplate4 itTemplate = ItemTemplate4.get_it_by_id(realItemId);
        if (itTemplate == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        // Kiểm tra giới hạn mua đặc biệt (Trồng cây giống 388)
        if (realItemId == 388) {
            EventData trongCayData = null;
            for (int i = 0; i < p.eventData.size(); i++) {
                if (p.eventData.get(i).eventID == 9) {
                    trongCayData = p.eventData.get(i);
                    break;
                }
            }
            if (trongCayData != null) {
                int bought = trongCayData.data[SuKienTrongCay.IDX_CAY_GIONG_MUA];
                if (bought >= 1000) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Hôm nay đã mua tối đa 1000/1000 cây giống!");
                    return;
                }
                if (bought + num > 1000) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Bạn chỉ có thể mua thêm " + (1000 - bought) + " cây giống trong ngày hôm nay!");
                    return;
                }
            }
        }

        // --- Kiểm tra và tính tiền tránh tràn số ---
        long totalCost;
        boolean useRuby = found.priceRuby > 0 || (found.priceCoin <= 0 && itTemplate.ruby > 0);
        int unitPrice = useRuby ? (found.priceRuby > 0 ? found.priceRuby : itTemplate.ruby)
                                : (found.priceCoin > 0 ? found.priceCoin : itTemplate.beri);
        totalCost = (long) unitPrice * num;
        if (totalCost < 0 || totalCost > Integer.MAX_VALUE) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
            return;
        }
        int cost = (int) totalCost;

        if (useRuby) {
            if (cost > 0 && p.get_ngoc() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " ruby");
                return;
            }
        } else {
            if (cost > 0 && p.get_vang() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " beri");
                return;
            }
        }

        // Cập nhật count trồng cây nếu mua cây giống 388
        if (realItemId == 388) {
            EventData trongCayData = null;
            for (int i = 0; i < p.eventData.size(); i++) {
                if (p.eventData.get(i).eventID == 9) {
                    trongCayData = p.eventData.get(i);
                    break;
                }
            }
            if (trongCayData != null) {
                trongCayData.data[SuKienTrongCay.IDX_CAY_GIONG_MUA] += num;
            }
        }

        // --- Mua item sự kiện Noel (dùng vé Noel thay tiền) ---
        if (SuKienNoel.gI().isNoel()) {
            int numVe = 0;
            if (realItemId == 488) {
                numVe = 15;
            } else if (realItemId == 486 || realItemId == 487 || realItemId == 492) {
                numVe = 5;
            }
            if (numVe > 0) {
                int totalVe = numVe * num;
                if (p.item.total_item_bag_by_id(4, SuKienNoel.ITEM_VE_NOEL) < totalVe) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + totalVe + " vé Noel");
                    return;
                }
                p.item.remove_item47(4, SuKienNoel.ITEM_VE_NOEL, totalVe);
                p.item.add_item_bag47(4, realItemId, num);
                Message m22 = new Message(-64);
                m22.writer().writeUTF("Mua " + num);
                p.addmsg(m22);
                m22.cleanup();
                p.item.updateInventory(false);
                p.updateMoney();
                return;
            }
        }

        // --- Trừ tiền ---
        if (useRuby) {
            if (cost > 0) p.update_ngoc(-cost);
        } else {
            if (cost > 0) p.update_vang(-cost);
        }

        // --- Thêm item vào túi hoặc cập nhật vé đặc biệt ---
        if (realItemId == 43) {
            p.update_pvp_ticket(num);
            p.getService().CountDown_Ticket();
        } else if (realItemId == 40) {
            p.update_key_boss(num);
            p.getService().CountDown_Ticket();
        } else if (realItemId == 6) {
            p.update_ticket(num);
            p.getService().CountDown_Ticket();
        } else {
            p.item.add_item_bag47(4, realItemId, num);
            p.item.updateInventory(false);
        }
        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua " + num);
        p.addmsg(m22);
        m22.cleanup();
        p.updateMoney();
    }

    /** Xử lý mua thú cưng */
    private void buyPet(Player p, short id) throws IOException {
        ItemTemplate4 itTemplate = ItemTemplate4.get_it_by_id(id);
        if (itTemplate == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        // Kiểm tra đã sở hữu chưa
        for (int i = 0; i < p.my_pet.size(); i++) {
            if (p.my_pet.get(i).id == itTemplate.id) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Đã sở hữu thú cưng này");
                return;
            }
        }

        // Trừ tiền
        int cost;
        boolean useRuby = itTemplate.ruby > 0;
        if (useRuby) {
            cost = itTemplate.ruby;
            if (p.get_ngoc() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " ruby");
                return;
            }
            p.update_ngoc(-cost);
        } else {
            cost = itTemplate.beri;
            if (cost <= 0) {
                p.getService().end_Dialog();
                return;
            }
            if (p.get_vang() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " beri");
                return;
            }
            p.update_vang(-cost);
        }

        // Thêm pet
        MyPet pet = new MyPet();
        pet.id = itTemplate.id;
        pet.isUse = false;
        pet.template = Pet.getTemplate(pet.id);
        pet.time = System.currentTimeMillis() + 60_000L * 60 * 24 * 3; // 3 ngày
        p.my_pet.add(pet);

        p.updateMoney();
        p.getService().end_Dialog();
        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua 1");
        p.addmsg(m22);
        m22.cleanup();
    }
}
