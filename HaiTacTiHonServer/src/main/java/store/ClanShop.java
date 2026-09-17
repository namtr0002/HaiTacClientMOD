package store;

import clan.Clan;
import clan.ClanMember;
import map.Zone;
import model.Player;
import network.Message;
import template.DataTemplate;
import template.ItemBag47;
import template.ItemTemplate8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cửa hàng vật phẩm băng (Clan Shop) — opcode type = 110, item category = 8.
 * Dữ liệu vật phẩm và giá cả chuẩn hóa từ store_data (store_id = 8 / 110 hoặc item_category = 8).
 */
public class ClanShop extends zabstracts.AbsShop {

    public ClanShop() {
        super(110, "Cửa hàng vật phẩm băng");
    }

    /**
     * Lấy danh sách vật phẩm clan từ store_data.
     * Nếu store_data chưa cấu hình thì fallback sang ItemTemplate8.ENTRYS.
     */
    public List<StoreItem> getClanStoreItems() {
        List<StoreItem> items = new ArrayList<>();
        List<StoreItem> allStoreItems = zabstracts.AbsShop.getItemsForStore(8);
        if (allStoreItems != null && !allStoreItems.isEmpty()) {
            for (StoreItem si : allStoreItems) {
                if (si.itemCategory == 8 || si.storeId == 8) {
                    items.add(si);
                }
            }
        }
        // Kiểm tra thêm storeId 110 nếu có cấu hình
        if (items.isEmpty()) {
            List<StoreItem> store110 = zabstracts.AbsShop.getItemsForStore(110);
            if (store110 != null && !store110.isEmpty()) {
                items.addAll(store110);
            }
        }
        // Kiểm tra tất cả các item có itemCategory == 8 trong store_data
        if (items.isEmpty()) {
            for (StoreItem si : zabstracts.AbsShop.storeItems) {
                if (si.itemCategory == 8) {
                    items.add(si);
                }
            }
        }
        // Fallback sang ItemTemplate8.ENTRYS nếu store_data chưa có dữ liệu
        if (items.isEmpty()) {
            if (ItemTemplate8.ENTRYS == null || ItemTemplate8.ENTRYS.isEmpty()) {
                ItemTemplate8.initDefaultEntries();
            }
            for (ItemTemplate8 it8 : ItemTemplate8.ENTRYS) {
                items.add(new StoreItem(it8.id, 8, it8.id, 8, it8.beri, it8.ruby, -1, 0, -1));
            }
        }
        return items;
    }

    public StoreItem findStoreItem(short itemId) {
        for (StoreItem item : getClanStoreItems()) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            if (displayId == itemId || item.itemId == itemId) {
                return item;
            }
        }
        ItemTemplate8 it8 = ItemTemplate8.get_it_by_id(itemId);
        if (it8 != null) {
            return new StoreItem(it8.id, 8, it8.id, 8, it8.beri, it8.ruby, -1, 0, -1);
        }
        return null;
    }

    @Override
    public void openUI(Player p) throws IOException {
        if (p == null) return;
        p.typeShop = (byte) 110;

        // Đảm bảo template vật phẩm clan được khởi tạo
        if (ItemTemplate8.ENTRYS == null || ItemTemplate8.ENTRYS.isEmpty()) {
            ItemTemplate8.initDefaultEntries();
        }

        // Gửi gói Message -7 subtype 18 để đồng bộ template vào bộ nhớ client
        // Điều này đảm bảo MainItem.hashPotionClan trên client 100% có dữ liệu đầy đủ
        sendSyncClanPotionTemplate(p, false);

        // Nạp dữ liệu kho băng vào Client để Tab 0 (Kho Băng / Hành trang) luôn hiển thị đầy đủ
        if (p.clan != null) {
            p.clan.send_inventory(p, false);
        }

        List<StoreItem> items = getClanStoreItems();
        Message m = new Message(-19);
        m.writer().writeByte(110); // Shop type 110 (Clan Shop)
        m.writer().writeUTF(this.name != null ? this.name : "Cửa hàng vật phẩm băng");
        m.writer().writeByte(8);   // Category 8 (Potion Clan)
        m.writer().writeShort(items.size());
        for (StoreItem item : items) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            m.writer().writeShort((short) displayId);
            m.writer().writeShort(1);
        }
        p.addmsg(m);
        m.cleanup();
    }

    /**
     * Gửi gói Message -7 subtype 18 chứa toàn bộ ItemTemplate8 trực tiếp cho client
     * để đảm bảo client chắc chắn có hashPotionClan trong bộ nhớ RAM, tránh lỗi trắng shop hoặc kho băng.
     */
    public static void sendSyncClanPotionTemplate(Player p, boolean b) {
        try {
            if (p == null) return;
            if (ItemTemplate8.ENTRYS == null || ItemTemplate8.ENTRYS.isEmpty()) {
                ItemTemplate8.initDefaultEntries();
            }
            Message m2 = new Message(-7);
            m2.writer().writeByte(18);
            m2.writer().writeShort(ItemTemplate8.ENTRYS.size());
            for (int i = 0; i < ItemTemplate8.ENTRYS.size(); i++) {
                ItemTemplate8 temp = ItemTemplate8.ENTRYS.get(i);
                m2.writer().writeShort(temp.id);
                m2.writer().writeShort(temp.icon);
                m2.writer().writeUTF(temp.name != null ? temp.name : "");
                m2.writer().writeUTF(temp.info != null ? temp.info : "");
                m2.writer().writeInt(temp.beri);
                m2.writer().writeShort(temp.ruby);
                m2.writer().writeByte(temp.istrade);
                m2.writer().writeByte(temp.type);
                m2.writer().writeShort(temp.timedelay);
                m2.writer().writeShort(temp.value);
                m2.writer().writeShort(temp.timeactive);
                m2.writer().writeUTF(temp.nameuse != null ? temp.nameuse : "");
            }
            m2.writer().writeShort(DataTemplate.VerdataPotionClan);
            if (b) {
                p.msgs.add(m2);
            } else {
                p.addmsg(m2);
            }
            m2.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendSyncClanPotionTemplate(Player p) {
        sendSyncClanPotionTemplate(p, false);
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        if (p == null) return;

        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa tham gia Băng hải tặc!");
            return;
        }

        boolean isLeader = p.clan.members != null && !p.clan.members.isEmpty()
                && p.clan.members.get(0).name.equals(p.name);
        if (!isLeader) {
            p.getService().send_box_ThongBao_OK("Chỉ Thuyền trưởng mới có quyền mua vật phẩm băng!");
            return;
        }

        if (num <= 0 || num > 9999) {
            p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
            return;
        }

        StoreItem item = findStoreItem(id);
        if (item == null) {
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        int realItemId = item.itemId;
        ItemTemplate8 it8 = ItemTemplate8.get_it_by_id(realItemId);
        String itemName = it8 != null ? it8.name : ("Vật phẩm #" + realItemId);

        int priceCoin = item.priceCoin;
        int priceRuby = item.priceRuby;
        if (priceCoin <= 0 && priceRuby <= 0 && it8 != null) {
            priceCoin = it8.beri;
            priceRuby = it8.ruby;
        }

        if (priceCoin <= 0 && priceRuby <= 0) {
            p.getService().send_box_ThongBao_OK("Vật phẩm chưa mở bán!");
            return;
        }

        // Kiểm tra tiền clan
        if (priceRuby > 0) {
            long totalCost = (long) priceRuby * num;
            if (totalCost < 0 || totalCost > Integer.MAX_VALUE) {
                p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                return;
            }
            int rubyReq = (int) totalCost;
            if (p.clan.get_ngoc() < rubyReq) {
                p.getService().send_box_ThongBao_OK("Không đủ " + rubyReq + " ruby băng!");
                return;
            }

            p.clan.update_ruby(-rubyReq);
        } else if (priceCoin > 0) {
            long totalCost = (long) priceCoin * num;
            if (totalCost < 0 || totalCost > Integer.MAX_VALUE) {
                p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                return;
            }
            int coinReq = (int) totalCost;
            if (p.clan.get_vang() < coinReq) {
                p.getService().send_box_ThongBao_OK("Không đủ " + coinReq + " beri băng!");
                return;
            }
            p.clan.update_beri(-coinReq);
        }

        // Thêm vào kho băng (p.clan.list_it)
        ItemBag47 it_add = null;
        for (int j = 0; j < p.clan.list_it.size(); j++) {
            if (p.clan.list_it.get(j).id == realItemId) {
                it_add = p.clan.list_it.get(j);
                break;
            }
        }
        if (it_add == null) {
            it_add = new ItemBag47();
            it_add.category = 4;
            it_add.id = (short) realItemId;
            it_add.quant = 0;
            p.clan.list_it.add(it_add);
        }
        it_add.quant += num;

        // Đồng bộ tiền và kho băng cho toàn bộ thành viên đang online
        for (int i = 0; i < p.clan.members.size(); i++) {
            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (p0 != null) {
                Clan.send_money(p0, false);
                p0.clan.send_inventory(p0, false);
            }
        }

        // Gửi thông báo mua thành công
        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua " + num);
        p.addmsg(m22);
        m22.cleanup();

        p.getService().send_box_ThongBao_OK("Mua thành công " + num + " " + itemName + " vào kho băng!");
    }
}
