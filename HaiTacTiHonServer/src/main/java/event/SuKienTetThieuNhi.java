package event;

import event.eboss.DuaBeThieuNhi;
import model.Player;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import template.GiftBox;
import template.ItemTemplate4;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKienTetThieuNhi extends Event {

    public static final int ID = Event.ID_SUKIEN_TETTHIEUNHI_2026;

    // Nguyên liệu
    public static final int ITEM_BANH_QUI = 399;
    public static final int ITEM_KEO_7_MAU = 400;
    public static final int ITEM_KEO_DEO_GAU = 401;
    public static final int ITEM_BANH_QUI_BO = 402;

    // Vật phẩm chế tạo & đổi
    public static final int ITEM_TUI_BANH = 403;
    public static final int ITEM_HOP_BANH_KEO = 404;
    public static final int ITEM_KEO_BONG_GON = 405;
    public static final int ITEM_BONG_BONG = 406;

    public static final int[] MAP_OUTER_VILLAGES = new int[]{
        2, 10, 18, 26, 34, 42, 50, 67, 70, 80, 84, 94, 108, 115, 192
    };

    public List<DuaBeThieuNhi> listBe = new ArrayList<>();
    private static SuKienTetThieuNhi instance;

    public SuKienTetThieuNhi() {
        super(ID, "Sự Kiện Tết Thiếu Nhi");
        shopName = "Cửa Hàng Kẹo Ngọt";
        costItemId = ITEM_BANH_QUI;
        costItemType = 4;

        this.bxhSubTypes = new int[]{501, 502};
        this.bxhNames = new String[]{"Top Bánh Kẹo Thiếu Nhi", "Top Bong Bóng Thiếu Nhi"};

        initShop();
        instance = this;
    }

    public static SuKienTetThieuNhi gI() {
        if (instance == null) {
            instance = new SuKienTetThieuNhi();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            399, 400, 401, 402, 403, 404, 405, 406, 885
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienTetThieuNhi(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Khởi tạo Bé Thiếu Nhi tại các map ngoài làng
        listBe = new ArrayList<>();
        int slot = 0;
        for (int mapId : MAP_OUTER_VILLAGES) {
            DuaBeThieuNhi be = new DuaBeThieuNhi(slot++, mapId, 0);
            listBe.add(be);
        }

        // Activity rewards
        lienTangReward = new ActivityReward(2, 5);
        lienTangReward.addItemGift(ITEM_HOP_BANH_KEO, (byte) 4, 1);

        wantedReward = new ActivityReward(3, 10);
        wantedReward.addItemGift(ITEM_KEO_7_MAU, (byte) 4, 2);

        trainReward = new ActivityReward(4, 5);
        trainReward.addItemGift(ITEM_BANH_QUI_BO, (byte) 4, 2);

        System.out.println("[SuKienTetThieuNhi] Init completed with full features. Spawned " + listBe.size() + " DuaBeThieuNhi entities.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -875 || npcId == -1031 || npcId == -100 || npcId == ID) { // NPC Bé Pudding / Menu Sự Kiện
            p.getService().openDynamicMenu(npcId, "Bé Pudding", new String[]{
                "Làm Túi Kẹo Ngọt",
                "Làm Kẹo Bông Gòn (VIP)",
                "BXH Bánh Kẹo",
                "BXH Bong Bóng",
                "Cửa Hàng Kẹo Ngọt",
                "Hướng dẫn",
                "Tích nạp sự kiện",
                "Tích tiêu sự kiện"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -875 || npcId == -1031 || npcId == -100 || npcId == ID) {
            switch (index) {
                case 0:
                    new itemz.rebuilds.GhepTuiKeoNgot().show_table(p);
                    break;
                case 1:
                    new itemz.rebuilds.GhepKeoBongGonVip().show_table(p);
                    break;
                case 2:
                    p.typeBXH = 501;
                    showEventRank(p);
                    break;
                case 3:
                    p.typeBXH = 502;
                    showEventRank(p);
                    break;
                case 4:
                    openShop(p);
                    break;
                case 5: {
                    String txt = "Sự Kiện Tết Thiếu Nhi (1/6)\n"
                            + "- Đánh quái +-10 level rơi Bánh qui (#399), Kẹo 7 màu (#400), Bánh qui bơ, Kẹo dẻo gấu (tối đa 150/ngày).\n"
                            + "- Gói Túi kẹo ngọt và Kẹo bông gòn VIP để nhận nhiều phần quà quý giá.\n"
                            + "- Gán kẹo vào ô phím 4, 5 và chỉ vào Bé Thiếu Nhi chạy quanh map ngoài làng để cho kẹo nhận EXP, Beri, Rương và Trái ác quỷ!";
                    p.getService().Help_From_Server(npcId, txt);
                    break;
                }
                case 6:
                    showTichNap(p);
                    break;
                case 7:
                    showTichTieu(p);
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_BANH_QUI || id == ITEM_KEO_7_MAU || id == ITEM_TUI_BANH || id == ITEM_KEO_BONG_GON || id == ITEM_HOP_BANH_KEO || id == ITEM_BONG_BONG) {
                p.getService().send_box_ThongBao_OK("Sự kiện Tết Thiếu Nhi đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            // Xử lý khi cho kẹo / bánh (399: Bánh qui, 400: Kẹo 7 màu, 403: Túi bánh, 405: Kẹo bông VIP)
            if (id == ITEM_BANH_QUI || id == ITEM_KEO_7_MAU || id == ITEM_TUI_BANH || id == ITEM_KEO_BONG_GON) {
                if (p.map == null) return false;

                // Tìm Bé Thiếu Nhi trong bản đồ hiện tại của người chơi
                DuaBeThieuNhi targetBe = null;
                for (DuaBeThieuNhi be : listBe) {
                    if (be != null && be.mob != null && be.mob.map != null
                            && be.mob.map.template.id == p.map.template.id
                            && be.mob.map.zone_id == p.map.zone_id
                            && !be.mob.isdie && be.mob.hp > 0) {
                        targetBe = be;
                        break;
                    }
                }

                // Fallback tìm trong map.mobs
                if (targetBe == null) {
                    for (Mob m : p.map.mobs.values()) {
                        if (m != null && m.iMob instanceof DuaBeThieuNhi && !m.isdie && m.hp > 0) {
                            targetBe = (DuaBeThieuNhi) m.iMob;
                            break;
                        }
                    }
                }

                // Nếu có Bé Thiếu Nhi trong map: tiến hành cho kẹo
                if (targetBe != null) {
                    return targetBe.giveCandy(p, id);
                }

                // Nếu không có Bé trong map:
                // Đối với Túi bánh hoặc Kẹo bông VIP -> fallback mở quà trực tiếp
                if (id == ITEM_TUI_BANH || id == ITEM_KEO_BONG_GON) {
                    if (p.item.total_item_bag_by_id(4, id) < 1) {
                        p.getService().send_box_ThongBao_OK("Bạn không có vật phẩm này!");
                        return false;
                    }

                    p.item.remove_item47(4, id, 1);
                    List<template.GiftBox> giftList = new ArrayList<>();
                    int rand = ZUtil.random(100);
                    long expEarn;

                    if (id == ITEM_KEO_BONG_GON) { // VIP
                        expEarn = (long) p.level * 6000L;
                        giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));

                        if (rand < 40) {
                            int rubyAdd = ZUtil.random(5, 10);
                            giftList.add(new template.GiftBox(4, 1, rubyAdd));
                        } else if (rand < 75) {
                            giftList.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                        } else {
                            giftList.add(new template.GiftBox(4, 404, 1)); // Hộp bánh kẹo
                        }
                    } else { // Thường
                        expEarn = (long) p.level * 2000L;
                        giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));

                        if (rand < 60) {
                            int beriAdd = ZUtil.random(100000, 300000);
                            giftList.add(new template.GiftBox(4, 0, beriAdd));
                        } else if (rand < 85) {
                            giftList.add(new template.GiftBox(7, 1, 1)); // Bột cường hóa
                        } else {
                            giftList.add(new template.GiftBox(4, 135, 1)); // Tinh thể đá
                        }
                    }

                    p.item.updateInventory(false);

                    String itemName = (id == ITEM_KEO_BONG_GON) ? "Kẹo Bông Gòn VIP" : "Túi Bánh Ngọt";
                    core.RewardService.sendGiftOrMail(p, 1, "Phần Thưởng " + itemName, itemName, giftList, true);
                    return true;
                }

                p.getService().send_box_ThongBao_OK("Không tìm thấy Bé Thiếu Nhi ở khu vực này!\nHãy tìm Bé Thiếu Nhi tại các bản đồ ngoài làng (như 1-1, 2-1...) để cho bé kẹo nhận Trái Ác Quỷ và quà ngẫu nhiên!");
                return false;
            } else if (id == ITEM_HOP_BANH_KEO) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Hộp Bánh Kẹo!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 15000;
                int rubyEarn = ZUtil.random(10, 30);

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));
                listGift.add(new template.GiftBox(4, 122, 1));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Hộp Bánh Kẹo Thiếu Nhi", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_BONG_BONG) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Bong Bóng!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 5000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 0, 200_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Bong Bóng Tuổi Thơ", "Phần thưởng", listGift, true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(id) || mob == null || mob.is_boss) return;
        if (!isDropItemActive()) return;

        if (!Zone.is_map_sea(mob.map.template.id)) {
            int[] items = new int[]{ITEM_BANH_QUI, ITEM_KEO_7_MAU, ITEM_KEO_DEO_GAU, ITEM_BANH_QUI_BO};
            dropEventItemRandom(p, mob, items, 18, 150, 0); // 18% cơ bản, tối đa 150/ngày
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[5] < 5) {
            evData.data[5]++;
            p.item.add_item_bag47(4, ITEM_BONG_BONG, 2);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[6] < 3) {
            evData.data[6]++;
            p.item.add_item_bag47(4, ITEM_HOP_BANH_KEO, 1);
            p.item.updateInventory(false);
        }
    }
}
