package event;

import model.Player;
import model.Menu;
import model.YesNoDialog;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import event.eboss.DauTruongBotArena;
import itemz.MainItemShop;
import template.ItemTemplate4;

/**
 * SuKienDauTruongRucLua2026 — Sự Kiện Đấu Trường Rực Lửa 2026.
 * Event ID: 13.
 */
public class SuKienDauTruongRucLua2026 extends Event {

    public static final int ID = 13;
    private static SuKienDauTruongRucLua2026 instance;

    public static SuKienDauTruongRucLua2026 gI() {
        if (instance == null) {
            instance = new SuKienDauTruongRucLua2026();
        }
        return instance;
    }

    public SuKienDauTruongRucLua2026() {
        super(ID, "Đấu Trường Rực Lửa 2026");
        shopName = "Cửa Hàng Bóng Đá";
        costItemId = 599; // Vé World Cup
        costItemType = 4;
        this.bxhSubTypes = new int[]{1301, 1302};
        this.bxhNames = new String[]{"Top Làm Vật Phẩm", "Top Dự Đoán Trận Đấu"};
        sellableItems = new short[0];
        time = "0:00:00 15/6/2026 > 23:59:59 01/7/2026";
        timex2pay = "0:00:00 15/6/2026 > 23:59:59 01/7/2026";
        timechangeitem = "0:00:00 15/6/2026 > 23:59:59 01/7/2026";
        timedropitem = "0:00:00 15/6/2026 > 23:59:59 01/7/2026";
        timeremoveitem = "0:00:00 15/6/2026 > 23:59:59 01/7/2026";

        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{597, 598, 599, 609, 610, 886, 887};
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienDauTruongRucLua2026(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
        DauTruongBotArena.gI().initNewMatchCycle();
        System.out.println("[SuKienDauTruongRucLua2026] Init completed. DauTruongBotArena registered.");
    }

    @Override
    public void update(int hour, int min, int sec) throws Exception {
        if (!event.EventManager.isActive(ID)) return;
        // Cập nhật vòng lặp Đấu Trường Bot PvP & Dự Đoán
        DauTruongBotArena.gI().update();
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        openEventMenu(p);
        return true;
    }

    public void openEventMenu(Player p) throws IOException {
        if (p == null) return;
        p.menus.clear();
        DauTruongBotArena arena = DauTruongBotArena.gI();

        // 1. Đấu Trường Dự Đoán
        p.menus.add(new Menu("Đấu Trường Dự Đoán", (short) 141, () -> {
            try {
                openPredictionSubMenu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 2. Cửa Hàng Sự Kiện
        p.menus.add(new Menu("Cửa Hàng Sự Kiện", (short) 104, () -> {
            try {
                openShop(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 3. Ghép Rương World Cup
        p.menus.add(new Menu("Ghép Rương World Cup", (short) 165, () -> {
            try {
                new itemz.rebuilds.GhepRuongWorldcup().show_table(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 4. Ghép Cúp World Cup
        p.menus.add(new Menu("Ghép Cúp World Cup", (short) 165, () -> {
            try {
                new itemz.rebuilds.GhepCupWorldcup().show_table(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 5. Đổi Thời Trang Vĩnh Viễn
        p.menus.add(new Menu("Đổi Thời Trang Vĩnh Viễn", (short) 130, () -> {
            try {
                openExchangeFashionMenu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 6. Vòng Quay May Mắn
        p.menus.add(new Menu("Vòng Quay May Mắn", (short) 141, () -> {
            try {
                openLuckyWheel(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 7. Bảng Xếp Hạng Sự Kiện
        p.menus.add(new Menu("Bảng Xếp Hạng", (short) 124, () -> {
            try {
                openRankSubMenu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 8. Tích Nạp Sự Kiện
        p.menus.add(new Menu("Tích Nạp Sự Kiện", (short) 134, () -> {
            try {
                showTichNap(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 9. Tích Tiêu Ruby
        p.menus.add(new Menu("Tích Tiêu Ruby", (short) 135, () -> {
            try {
                showTichTieu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // Nếu đang ở chế độ xem Spectator -> Cho phép thoát
        if (p.isSpectator) {
            p.menus.add(new Menu("Thoát Xem Trận Đấu", (short) 113, () -> {
                p.exitSpectatorMode();
                p.getService().send_box_ThongBao_OK("Đã thoát chế độ xem và trở về vị trí cũ!");
            }));
        }

        // 10. Hướng Dẫn Chi Tiết
        p.menus.add(new Menu("Hướng Dẫn", (short) 123, () -> {
            try {
                sendHelp(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(-883, "NPC Bóng Đá", p.menus);
    }

    /**
     * Mở SubMenu Đấu Trường Dự Đoán
     */
    public void openPredictionSubMenu(Player p) throws IOException {
        if (p == null) return;
        DauTruongBotArena arena = DauTruongBotArena.gI();

        String info = "[Trận #" + arena.matchId + "] " + arena.team1Name + " VS " + arena.team2Name 
                + "\n- Trạng thái: " + arena.getMatchStatusText()
                + "\n- Thể thức: " + arena.formatType + "vs" + arena.formatType + " (Map PvP Băng)"
                + "\n- Tỉ số hiện tại: " + arena.scoreTeam1 + " - " + arena.scoreTeam2 + " (Chạm 5 điểm)";

        List<Menu> subMenus = new ArrayList<>();

        // 1. Cược Đội Đỏ
        subMenus.add(new Menu("Dự đoán " + arena.team1Name + " [1 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 1, 1); } catch (IOException e) { e.printStackTrace(); }
        }));
        subMenus.add(new Menu("Dự đoán " + arena.team1Name + " [5 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 1, 5); } catch (IOException e) { e.printStackTrace(); }
        }));
        subMenus.add(new Menu("Dự đoán " + arena.team1Name + " [10 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 1, 10); } catch (IOException e) { e.printStackTrace(); }
        }));

        // 2. Cược Đội Xanh
        subMenus.add(new Menu("Dự đoán " + arena.team2Name + " [1 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 2, 1); } catch (IOException e) { e.printStackTrace(); }
        }));
        subMenus.add(new Menu("Dự đoán " + arena.team2Name + " [5 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 2, 5); } catch (IOException e) { e.printStackTrace(); }
        }));
        subMenus.add(new Menu("Dự đoán " + arena.team2Name + " [10 Vé]", (short) 141, () -> {
            try { arena.placeBet(p, 2, 10); } catch (IOException e) { e.printStackTrace(); }
        }));

        // 3. Vào Xem Trực Tiếp
        subMenus.add(new Menu("Vào Xem Trực Tiếp", (short) 117, () -> {
            try { arena.enterSpectator(p); } catch (IOException e) { e.printStackTrace(); }
        }));

        // 4. Kết quả trận trước
        subMenus.add(new Menu("Kết Quả Trận Trước", (short) 123, () -> {
            p.getService().send_box_ThongBao_OK(arena.lastMatchResultInfo);
        }));

        p.getService().openDynamicMenu(-883, info, subMenus);
    }

    /**
     * Mở SubMenu Đổi Thời Trang Vĩnh Viễn
     */
    public void openExchangeFashionMenu(Player p) throws IOException {
        if (p == null) return;
        List<Menu> subMenus = new ArrayList<>();

        subMenus.add(new Menu("Đổi TT Đội Tuyển Đức", (short) 130, () -> exchangeFashion(p, 137, "Đức")));
        subMenus.add(new Menu("Đổi TT Đội Tuyển Brazil", (short) 130, () -> exchangeFashion(p, 138, "Brazil")));
        subMenus.add(new Menu("Đổi TT Đội Tuyển Pháp", (short) 130, () -> exchangeFashion(p, 139, "Pháp")));
        subMenus.add(new Menu("Đổi TT Trọng Tài Worldcup", (short) 130, () -> exchangeFashion(p, 121, "Trọng Tài")));
        subMenus.add(new Menu("Đổi TT Euro 2024", (short) 130, () -> exchangeFashion(p, 98, "Euro 2024")));

        p.getService().openDynamicMenu(-883, "Đổi Thời Trang Bóng Đá Vĩnh Viễn\n- Chi phí: 1 Quả Bóng Vàng (#799) cho mỗi bộ.", subMenus);
    }

    private void exchangeFashion(Player p, int fashionId, String name) {
        if (p.item.total_item_bag_by_id(4, 799) < 1) {
            p.getService().send_box_ThongBao_OK("Bạn cần có ít nhất 1 Quả Bóng Vàng (#799) để đổi thời trang " + name + " vĩnh viễn!");
            return;
        }
        if (p.item.able_bag() < 1) {
            p.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy!");
            return;
        }

        p.item.remove_item47(4, 799, 1);
        p.item.updateInventory(false);

        List<MainItem> listGift = new ArrayList<>();
        MainItem fashion = new MainItem(fashionId, 105, 1);
        fashion.time = -1; // Vĩnh viễn!
        listGift.add(fashion);
        MainItem.showGiftBox(p, "Đấu Trường Rực Lửa", "Nhận thời trang " + name + " Vĩnh Viễn!", listGift, true, true);
    }

    /**
     * Mở SubMenu Bảng Xếp Hạng
     */
    public void openRankSubMenu(Player p) throws IOException {
        if (p == null) return;
        List<Menu> subMenus = new ArrayList<>();

        subMenus.add(new Menu("Top Dự Đoán Trận Đấu", (short) 124, () -> {
            try {
                p.typeBXH = 1302;
                showEventRank(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        subMenus.add(new Menu("Top Làm Vật Phẩm", (short) 124, () -> {
            try {
                p.typeBXH = 1301;
                showEventRank(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(-883, "Bảng Xếp Hạng Sự Kiện Đấu Trường Rực Lửa", subMenus);
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        openEventMenu(p);
        return true;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(ID) || mob.is_boss) return;
        if (!isDropItemActive()) return;

        // Rơi nguyên liệu sự kiện: Bóng Bạc (597), Bóng Vàng (598), Vé World Cup (599)
        int[] items = new int[]{597, 598, 599};
        dropEventItemRandom(p, mob, items, 15, 150, 0); // 15% cơ bản, giới hạn 150/ngày
    }

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == 600 || id == 599 || id == 799 || id == 609 || id == 610) {
                p.getService().send_box_ThongBao_OK("Sự kiện Đấu Trường Rực Lửa đã kết thúc!");
                return false;
            }
            return false;
        }

        // 1. Vé World Cup (#599) -> Mở Menu Đấu Trường Dự Đoán
        if (id == 599) {
            openPredictionSubMenu(p);
            return true;
        }

        // 2. Quả Bóng Vàng (#799) -> Mở Menu Đổi Thời Trang Vĩnh Viễn
        if (id == 799) {
            openExchangeFashionMenu(p);
            return true;
        }

        // 3. Thẻ TT Trọng Tài WC (#600) -> Mở Menu Sự Kiện
        if (id == 600) {
            openEventMenu(p);
            return true;
        }

        // 4. Rương World Cup (#609)
        if (id == 609) {
            return openEventGiftBox(p, 609, "Rương World Cup");
        }

        // 5. Cúp World Cup (#610)
        if (id == 610) {
            return openEventGiftBox(p, 610, "Cúp World Cup");
        }

        return false;
    }

    private boolean openEventGiftBox(Player p, int boxId, String boxName) {
        if (p.item.total_item_bag_by_id(4, boxId) < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có " + boxName + "!");
            return true;
        }
        if (p.item.able_bag() < 2) {
            p.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống!");
            return true;
        }

        p.item.remove_item47(4, boxId, 1);
        p.item.updateInventory(false);

        List<MainItem> gifts = new ArrayList<>();
        long exp = (boxId == 610) ? 50_000_000L : 15_000_000L;
        p.update_exp(exp, false);

        int beri = (boxId == 610) ? ZUtil.random(500_000, 2_000_000) : ZUtil.random(100_000, 500_000);
        p.update_vang(beri);
        try { p.updateMoney(); } catch (Exception ignored) {}

        // Thưởng vật phẩm
        if (boxId == 610) {
            gifts.add(new MainItem(799, 4, ZUtil.random(1, 2))); // Quả Bóng Vàng
            gifts.add(new MainItem(866, 4, ZUtil.random(5, 10))); // Vé Vòng Quay
            gifts.add(new MainItem(18, 7, ZUtil.random(10, 20))); // Bột siêu cấp
            if (ZUtil.random(100) < 30) {
                gifts.add(new MainItem(158, 4, 1)); // Rương đại ác quỷ
            }
        } else {
            gifts.add(new MainItem(599, 4, ZUtil.random(2, 5))); // Vé World Cup
            gifts.add(new MainItem(1, 7, ZUtil.random(5, 15)));  // Bột cường hóa
            gifts.add(new MainItem(135, 4, ZUtil.random(2, 5))); // Tinh thể đá
        }

        MainItem.showGiftBox(p, boxName, "Mở " + boxName + " nhận được " + ZUtil.number_format(exp) + " Exp, " + ZUtil.number_format(beri) + " Beri và các vật phẩm sau:", gifts, true, true);
        return true;
    }

    @Override
    public void sendHelp(Player p) throws IOException {
        String txt = "Sự Kiện Đấu Trường Rực Lửa 2026\n"
                + "1. Đấu Trường Dự Đoán Bot PvP:\n"
                + "- Các trận đấu Bot PvP đỉnh cao (Đơn đấu 1v1, Đồng đội 3v3, Đại chiến 5v5) diễn ra liên tục trên các Map Băng.\n"
                + "- Thể thức 5 Điểm Chạm: Đội nào thắng 5 hiệp trước sẽ giành chiến thắng chung cuộc. Mỗi hiệp kết thúc, các chiến binh sẽ hồi đầy máu, xóa sạch hồi chiêu và tiếp tục giao tranh.\n"
                + "- Dự đoán: Bạn cần tham gia dự đoán trước 1 phút trước khi trận đấu bắt đầu. Dự đoán chính xác nhận x2 Vé World Cup, Quả Bóng Vàng, Rương World Cup và Điểm BXH Dự Đoán!\n"
                + "- Chế độ Xem: Người chơi có thể vào xem trực tiếp các hiệp đấu nảy lửa. Kết thúc trận sẽ tự động đưa về vị trí cũ.\n"
                + "2. Up Quái & Chế Tạo:\n"
                + "- Đánh quái rơi: Bóng Bạc, Bóng Vàng, Vé World Cup.\n"
                + "- Ghép Rương World Cup và Cúp World Cup để nhận lượng lớn điểm BXH Làm Vật Phẩm và phần quà siêu cấp!";
        p.getService().Help_From_Server(-883, txt);
    }
}
