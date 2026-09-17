package zinterfaces.menus.npcs;

import zinterfaces.menus.dymanics.DymanicMercenary;
import activities.ChuyenHoa;
import activities.Upgrade_Skin;
import activities.UpgradeDevil;
import model.Player;
import model.Menu;
import core.MenuController;
import itemz.Rebuild_Item;
import itemz.Split_Item;
import itemz.UpgradeItem;
import itemz.UpgradeSuperItem;
import network.Message;
import template.ItemTemplate7;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Npc;

/**
 * NpcJohny — NPC Johny (-105, -90, -70, -47) + sub-menu động:
 *   32000 = Cường Hóa (ex MenuRebuiltItem)
 *   32001 = Khảm Đá  (ex MenuKhamNgoc)
 *   32002 = Cường hóa ác quỷ
 *   32004 = Thần Trang
 *   992   = Ghép nguyên liệu (sub của 32000)
 *   900, 901, 905, 906, 907 = Thuê lính đánh thuê (dispatch sang NpcMercenary)
 */
public class NpcJohny implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-105, -90, -70, -47, 32000, 32001, 32002, 32004, 992};
    }

    @Override
    public String getChatText() {
        return "Khoảng thời gian rong rủi săn hải tặc đã giúp ta biết được một cách mạnh lên rất nhanh chóng. Lại đây ta sẻ chỉ ngươi nếu muốn.";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Khoảng thời gian rong rủi săn hải tặc đã giúp ta biết được một cách mạnh lên rất nhanh chóng. Lại đây ta sẻ chỉ ngươi nếu muốn.",
            "Cường hóa trang bị giúp ngươi mạnh mẽ hơn!",
            "Hãy khảm những viên đá quý vào trang bị để tăng thêm sức mạnh!"
        };
    }

    // -- Xây dựng danh sách menu chính ---------------------------------------
    private void buildMenuOptions(Player p) {
        p.menus.clear();

        p.menus.add(new Menu("Cường Hóa", (short) 126, () -> {
            try {
                p.getService().openDynamicMenu(32000, "Cường Hóa",
                        new String[]{"Cửa hàng Nguyên liệu", "Ghép nguyên liệu",
                            "Tách đồ", "Cường hóa đồ", "Cường hóa cao cấp", "Cường Hóa Thần Trang", "Tinh Luyện Thần Trang", "Tách Cường Hóa Thần Trang"},
                        new short[]{129, 127, 130, 131, 163, 154, 128, 130});
            } catch (Exception ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        p.menus.add(new Menu("Thần Trang", (short) 154, () -> {
            try {
                UpgradeItem.show_table_upgrade(p);
            } catch (IOException ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        p.menus.add(new Menu("Khảm Đá", (short) 127, () -> {
            try {
                p.getService().openDynamicMenu(32001, "Khảm Đá",
                        new String[]{"Cửa Hàng Đá Khảm", "Ghép Đá", "Đục lỗ khảm", "Khảm Vật Phẩm",
                            "Tách Đá", "Đá siêu cấp", "Auto Ghép Đá Siêu Cấp", "Đá thần thoại", "Hướng dẫn"},
                        new short[]{129, 127, 133, 126, 130, 141, 141, 132, 148});
            } catch (Exception ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        p.menus.add(new Menu("Chuyển Hóa", (short) 128, () -> {
            try {
                ChuyenHoa.show_table(p);
            } catch (IOException ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        p.menus.add(new Menu("Ghép mảnh trang bị", (short) 126, () -> {
            try {
                Rebuild_Item.show_table(p, 9);
            } catch (IOException ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // icon 126 theo bak (không phải 154)
        p.menus.add(new Menu("Cường hóa thời trang", (short) 126, () -> {
            try {
                int ver_ = p.getConnVersionInt();
                if (ver_ >= 115) {
                    Upgrade_Skin.show_table(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Hãy sử dụng phiên bản từ 1.1.5 trở lên");
                }
            } catch (IOException ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // Cường hóa ác quỷ (icon 154) - Chỉ hiển thị tại Làng Sirup (map id = 25)
        if (p.map != null && p.map.template != null && p.map.template.id == 25) {
            p.menus.add(new Menu("Cường hóa ác quỷ", (short) 154, () -> {
                try {
                    p.getService().openDynamicMenu(32002, "Cường hóa ác quỷ",
                        new String[]{"Cửa hàng đá khảm", "Cường hóa Rương ác quỷ", "Cường hóa Kỹ năng"},
                        new short[]{129, 155, 156});
                } catch (Exception ex) {
                    Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
        }

        // Lính đánh thuê — delegate sang DymanicMercenary (icon 111 theo Zosaku bak)
        p.menus.add(new Menu("Lính đánh thuê", (short) 111, () -> {
            try {
                if (p.level < 40) {
                    p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 40 trở lên để có thể mở khóa tính năng Thuê Lính Đánh Thuê!");
                    return;
                }
                DymanicMercenary.sendMercenaryMenu(p);
            } catch (IOException ex) {
                Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
    }

    // -- Handler 32000: Cường Hóa (logic từ MenuRebuiltItem) -----------------
    private void handleCuongHoa(Player p, int index) throws IOException {
        switch (index) {
            case 0: { // Cửa hàng Nguyên liệu
                p.getService().Send_UI_Shop(6);
                break;
            }
            case 1: { // Ghép nguyên liệu -> gửi sub-menu type7 (992)
                int[] id = new int[]{1, 3, 9, 8, 11};
                String[] name = new String[id.length];
                byte[] icon = new byte[id.length];
                for (int i = 0; i < id.length; i++) {
                    ItemTemplate7 temp = ItemTemplate7.get_it_by_id(id[i]);
                    if (temp != null) {
                        name[i] = temp.name;
                        icon[i] = temp.icon;
                    }
                }
                p.getService().send_dynamic_menu_type3(992, 1, "Ghép nguyên liệu", name, icon, (byte) 7);
                break;
            }
            case 2: { // Tách nguyên liệu
                Split_Item.show_table(p);
                break;
            }
            case 3: { // Cường hóa đồ
                UpgradeItem.show_table_upgrade(p);
                break;
            }
            case 4: { // Cường hóa cao cấp
                UpgradeSuperItem.show_table(p);
                break;
            }
            case 5: { // Cường Hóa Thần Trang -> Mở thẳng UI Bàn Cường Hóa chuẩn trực quan
                UpgradeItem.show_table_upgrade(p);
                break;
            }
            case 6: { // Tinh Luyện Thần Trang -> Mở thẳng UI Bàn Tinh Luyện Rebuild
                itemz.rebuilds.NangCapThanTrang.getInstance().showTable(p);
                break;
            }
            case 7: { // Tách Cường Hóa Thần Trang -> Mở thẳng UI Bàn Tách Rebuild
                itemz.rebuilds.NangCapTachThanTrang.getInstance().showTable(p);
                break;
            }
        }
    }

    public static void openMenuThanTrang(Player p) {
        try {
            p.getService().openDynamicMenu(32004, "Thần Trang",
                    new String[]{"Cường Hóa Thần Trang", "Tinh Luyện Thần Trang (+0)", "Tách Cường Hóa Thần Trang", "Auto Cường Hóa Thần Trang", "Hướng Dẫn Thần Trang"},
                    new short[]{131, 128, 130, 131, 148});
        } catch (Exception ex) {
            Logger.getLogger(NpcJohny.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleThanTrang(Player p, int index) throws IOException {
        switch (index) {
            case 0: { // Cường Hóa Thần Trang (+0 -> +20) -> Mở Bàn Cường Hóa UI chuẩn trực quan
                UpgradeItem.show_table_upgrade(p);
                break;
            }
            case 1: { // Tinh Luyện Thần Trang (Chỉ +0) -> Mở Bàn Tinh Luyện Rebuild chuẩn trực quan
                itemz.rebuilds.NangCapThanTrang.getInstance().showTable(p);
                break;
            }
            case 2: { // Tách Cường Hóa Thần Trang -> Mở Bàn Tách Cường Hóa Rebuild chuẩn trực quan
                itemz.rebuilds.NangCapTachThanTrang.getInstance().showTable(p);
                break;
            }
            case 3: { // Auto Cường Hóa Thần Trang (Chọn đồ nhanh)
                itemz.rebuilds.NangCapThanTrang.openSelectThanTrangDialog(p);
                break;
            }
            case 4: { // Hướng Dẫn Thần Trang
                String txt = "TÍNH NĂNG ĐỘC QUYỀN THẦN TRANG (TYPE 8-15)\n\n"
                        + "1. CƯỜNG HÓA (+0 -> +20):\n"
                        + "- Đặt Thần Trang vào Bàn Cường Hóa để nâng cấp lên tối đa +20.\n"
                        + "- Tự động kiểm tra nguyên liệu chuẩn theo cấp (Bột Cường Hóa, Bột Tím, Bột Vàng, Bột Siêu Cấp).\n"
                        + "- Mốc an toàn (+4, +8, +10, +14, +16, +20): Thất bại KHÔNG BỊ RỚT CẤP.\n"
                        + "- Các mốc khác: Có thể rớt 1 cấp khi thất bại.\b"
                        + "2. TINH LUYỆN THẦN TRANG (+0):\n"
                        + "- Chỉ áp dụng cho trang bị Thần Trang cấp +0.\n"
                        + "- Tinh luyện giúp ngẫu nhiên lại toàn bộ các dòng thuộc tính ẩn (tỉ lệ tối đa đạt mốc 5%).\n"
                        + "- Tính năng Tự Động Tinh Luyện sẽ tự dừng ngay khi đạt mức tối đa tất cả các dòng.\b"
                        + "3. TÁCH CƯỜNG HÓA & PHÁ HỦY:\n"
                        + "- Tách Cường Hóa (+1 -> +20 về +0): Đặt Thần Trang đã cường hóa vào Bàn Tách Cường Hóa để đưa về +0 và nhận lại 50% bột nguyên liệu (không mất trang bị gốc).\n"
                        + "- Tách Đồ (Phá hủy trang bị): Tách trang bị Thần Trang tại Bàn Tách Đồ nhận lại Bột Vàng hoặc Bột Siêu Cấp.";
                p.getService().Help_From_Server(-16, txt);
                break;
            }
        }
    }

    // -- Handler 992: Ghép nguyên liệu (chọn loại nguyên liệu) ---------------
    private void handleGhepNguyenLieu(Player p, int index) throws IOException {
        itemz.Join_Item.show_table(p, index);
    }

    // -- Handler 32001: Khảm Đá (logic từ MenuKhamNgoc) ----------------------
    private void handleKhamNgoc(Player p, int index) throws IOException {
        switch (index) {
            case 0: { // Cửa Hàng Đá Khảm
                p.getService().Send_UI_Shop(111);
                break;
            }
            case 1: { // Ghép Đá
                Rebuild_Item.show_table(p, 1);
                break;
            }
            case 2: { // Đục lỗ khảm
                itemz.rebuilds.NangCapDucLo.getInstance().showTable(p);
                break;
            }
            case 3: { // Khảm Vật Phẩm
                itemz.rebuilds.NangCapKhamNgoc.getInstance().showTable(p);
                break;
            }
            case 4: { // Tách Đá
                itemz.rebuilds.NangCapThaoNgoc.getInstance().showTable(p);
                break;
            }
            case 5: { // Đá siêu cấp
                Rebuild_Item.show_table(p, 5);
                break;
            }
            case 6: { // Auto Ghép Đá Siêu Cấp
                itemz.rebuilds.GhepDaSieuCap.openAutoGhepDaSieuCapMenu(p);
                break;
            }
            case 7: { // Đá thần thoại
                p.getService().Send_UI_Shop(116);
                break;
            }
            case 8: { // Hướng dẫn
                String txt = "Khảm vật phẩm gồm 2 chức năng chính:\n"
                        + "Khảm đá vào vật phẩm\nGhép đá\b"
                        + "Khảm đá vào vật phẩm:\n"
                        + "Mỗi vật phẩm mới khi mở rương sẽ có ngẫu nhiên các Lỗ Khảm để bạn có thể gắn "
                        + "những viên đá đặc biệt vào giúp tăng sức mạnh cho bản thân.\b"
                        + "Bạn có thể đục lỗ để có thể gắn được nhiều đá hơn (tối đa 2 lần).\n"
                        + "Ngoài ra còn có chức năng lấy đá từ vật phẩm đã khảm để gắn vào vật phẩm mới.\b"
                        + "Ghép đá:\n"
                        + "Đá khảm rất đa dạng và mỗi loại có 6 cấp độ khác nhau.\b"
                        + "Bạn có thể dùng 3 viên đá cấp thấp để ghép thành viên đá cấp cao hơn cùng loại.";
                p.getService().Help_From_Server(-47, txt);
                break;
            }
        }
    }

    // -- Handler 32002: Cường hóa ác quỷ ------------------------------------
    private void handleCuongHoaAcQuy(Player p, int index) throws IOException {
        switch (index) {
            case 0: // Cửa hàng đá khảm
                p.getService().Send_UI_Shop(6);
                break;
            case 1: // Cường hóa Rương ác quỷ
                activities.UpgradeDevil.show_table(p, 2);
                break;
            case 2: // Cường hóa Kỹ năng
                activities.UpgradeDevil.show_table(p, 1);
                break;
        }
    }

    // -- sendMenu -------------------------------------------------------------
    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -105;
        buildMenuOptions(p);
        p.getService().openDynamicMenu(type, "Johny", p.menus);
    }

    // -- handleMenu — dispatch dựa trên menuId hoặc p.currentNpcId -----------
    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        switch (menuId) {
            case 32000:
                handleCuongHoa(p, index);
                break;
            case 32004:
                handleThanTrang(p, index);
                break;
            case 992:
                handleGhepNguyenLieu(p, index);
                break;
            case 32001:
                handleKhamNgoc(p, index);
                break;
            case 32002:
                handleCuongHoaAcQuy(p, index);
                break;
            default: // NPC chính (-105, -90, -70, -47)
                if (p.menus == null || p.menus.isEmpty()) {
                    buildMenuOptions(p);
                }
                if (p.menus != null && index >= 0 && index < p.menus.size()) {
                    p.menus.get(index).execute(p, index);
                }
                break;
        }
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, (int) p.currentNpcId, index);
    }
}
