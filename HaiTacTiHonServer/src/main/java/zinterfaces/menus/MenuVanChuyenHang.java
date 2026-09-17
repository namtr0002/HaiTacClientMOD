package zinterfaces.menus;

import model.Player;
import zinterfaces.iNpc;
import map.Npc;
import map.Zone;
import activities.Ship;
import template.Ship_pet;
import template.ItemTemplate4;
import network.Message;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuVanChuyenHang — Xử lý menu Vận Chuyển Hàng / Lái Buôn (Dynamic Menu ID 986)
 * Khôi phục 100% chuẩn logic gốc từ .bak/0, tích hợp với hệ thống OOP mới.
 */
public class MenuVanChuyenHang implements iNpc {

    private static final MenuVanChuyenHang instance = new MenuVanChuyenHang();

    public static MenuVanChuyenHang gI() {
        return instance;
    }

    @Override
    public short[] getId() {
        return new short[]{986, 982, 985};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        String[] name = new String[]{
            "Lấy Hàng", "Trả hàng", "Đăng ký bảo vệ hàng",
            "Thuê bảo vệ hàng", "Đăng ký chức năng", "Hủy vận buôn",
            "Gọi Lạc đà trở về", "Xem vị trí lạc đà", "Xem số lần vận buôn", "Hướng dẫn"
        };
        short[] icon = new short[]{107, 109, 110, 111, 110, 111, 151, -1, 114, 114};
        byte[] b7 = new byte[]{3, 3, 3, 3, 7, 3, 3, 7, 7, 7};
        p.getService().send_dynamic_menu_type6(986, 0, "Vận chuyển hàng", name, icon, b7);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        int menuId = p.currentNpcId != 0 ? p.currentNpcId : 986;
        handleMenu(p, menuId, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (menuId == 982) {
            if (p.name_ThoSanHaiTac != null && index >= 0 && index < p.name_ThoSanHaiTac.length) {
                String selectedHunter = p.name_ThoSanHaiTac[index];
                Player pHunter = Zone.get_player_by_name_allmap(selectedHunter);
                if (pHunter != null) {
                    if (pHunter.typePirate == 1) {
                        if (p.ship_pet != null) {
                            p.ship_pet.mainBaoVe = pHunter.name;
                            pHunter.name_ThoSanHaiTac = new String[]{p.name};
                            p.getService().send_box_ThongBao_OK("Đã thuê Thợ Săn " + pHunter.name + " bảo vệ chuyến hàng!");
                            pHunter.getService().send_box_ThongBao_OK("Lái buôn " + p.name + " đã thuê bạn bảo vệ chuyến hàng!");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Người chơi này không còn ở trạng thái Thợ Săn!");
                    }
                }
            }
            return;
        }

        if (menuId == 985) {
            switch (index) {
                case 0: // Lái buôn
                    p.typePirate = 0;
                    p.getService().send_box_ThongBao_OK("Đăng ký làm Lái Buôn thành công!");
                    break;
                case 1: // Thợ săn Hải Tặc
                    p.typePirate = 1;
                    p.getService().send_box_ThongBao_OK("Đăng ký làm Thợ Săn Hải Tặc thành công!");
                    break;
                case 2: // Hải Tặc
                    p.typePirate = 2;
                    p.getService().send_box_ThongBao_OK("Đăng ký làm Hải Tặc thành công!");
                    break;
                case 3: // Không đăng ký
                    p.typePirate = -1;
                    p.getService().send_box_ThongBao_OK("Đã hủy chức năng vận chuyển.");
                    break;
            }
            p.getService().update_PK(p, false);
            p.update_info_to_all();
            return;
        }
        switch (index) {
            case 0: { // 0. Lấy Hàng
                if (p.map == null || p.map.template == null || p.map.template.id != 1) {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể bắt đầu vận chuyển hàng từ Làng Cối Xay Gió (Map 1)!");
                    return;
                }
                if (p.typePirate != 0) {
                    p.getService().send_box_ThongBao_OK("Bạn phải đăng ký là Lái Buôn trước khi lấy hàng!\n(Chọn 'Đăng ký chức năng' -> 'Lái buôn')");
                    return;
                }
                if (p.time_ship >= 5) {
                    p.getService().send_box_ThongBao_OK("Hôm nay bạn đã vận chuyển tối đa (5/5 chuyến)!");
                    return;
                }
                if (p.ship_pet != null) {
                    p.getService().send_box_ThongBao_OK("Bạn đang có gói hàng đang vận chuyển!");
                    return;
                }
                Ship.show_table(p);
                break;
            }
            case 1: { // 1. Trả hàng
                if (p.id_ship_packet == -1 || p.ship_pet == null) {
                    p.getService().send_box_ThongBao_OK("Bạn không có gói hàng nào để trả!");
                    return;
                }
                int mapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
                if (mapId != 9 && mapId != 17 && mapId != 25) {
                    p.getService().send_box_ThongBao_OK("Không thể trả hàng tại đây!\nHãy giao hàng tại NPC Quán ăn ở:\n- Thị trấn Vỏ Sò (Map 9)\n- Thị trấn Orange (Map 17)\n- Thị trấn Syrup (Map 25)");
                    return;
                }
                if (p.ship_pet.map == null || !p.ship_pet.map.equals(p.map)
                        || Math.abs(p.ship_pet.x - p.x) >= 200 || Math.abs(p.ship_pet.y - p.y) >= 200) {
                    p.getService().send_box_ThongBao_OK("Ta không thấy vật phẩm buôn / lạc đà của ngươi gần đây!");
                    return;
                }
                if (p.typePirate == 0 && p.time_ship >= 5) {
                    p.getService().send_box_ThongBao_OK("Hôm nay đã vận chuyển tối đa (5/5 chuyến)!");
                    return;
                }

                // Tính Beri thưởng theo gói hàng chuẩn gốc
                int beri_total = 0;
                switch (p.id_ship_packet) {
                    case 36: beri_total = 2_000_000; break;
                    case 37: beri_total = 2_500_000; break;
                    case 38: beri_total = 3_000_000; break;
                    case 39: beri_total = 3_500_000; break;
                    default: beri_total = 2_000_000; break;
                }

                // Hệ số khoảng cách theo map trả hàng
                if (mapId == 17) {
                    beri_total = (beri_total * 15) / 10; // x1.5 (Orange)
                } else if (mapId == 25) {
                    beri_total *= 2; // x2.0 (Syrup)
                }

                // Nếu là Hải Tặc cướp hàng thành công
                if (p.typePirate == 2) {
                    beri_total = (beri_total * 3) / 10; // Nhận 30%
                }

                p.update_vang(beri_total);

                // Gửi packet 3 xóa lạc đà cho toàn map
                Message m = new Message(3);
                m.writer().writeShort(p.ship_pet.index_map);
                m.writer().writeByte(2);
                if (p.map != null) {
                    p.map.send_msg_all_p(m, null, true);
                }
                m.cleanup();

                Ship_pet.remove(p.ship_pet);
                String name_BaoVe = p.ship_pet.mainBaoVe;
                p.ship_pet = null;
                p.id_ship_packet = -1;

                // Xử lý chia tiền cho Thợ Săn Hải Tặc bảo vệ (nếu có)
                if (name_BaoVe != null && !name_BaoVe.isEmpty()) {
                    Player pThoSan = Zone.get_player_by_name_allmap(name_BaoVe);
                    if (pThoSan != null && pThoSan.name_ThoSanHaiTac != null
                            && pThoSan.name_ThoSanHaiTac.length == 1
                            && pThoSan.name_ThoSanHaiTac[0].equals(p.name)) {
                        int share = beri_total / 3;
                        pThoSan.update_vang(share);
                        p.update_vang(-share);
                        pThoSan.updateMoney();
                        pThoSan.getService().send_box_ThongBao_OK("Bảo vệ hàng thành công! Nhận được " + ZUtil.number_format(share) + " Beri từ Lái buôn.");
                        pThoSan.name_ThoSanHaiTac = null;
                        p.getService().send_box_ThongBao_OK("Trả hàng thành công nhận " + ZUtil.number_format(beri_total - share) + " Beri!\n(Đã chia " + ZUtil.number_format(share) + " Beri cho Thợ Săn " + pThoSan.name + ")");
                    } else {
                        p.getService().send_box_ThongBao_OK("Trả hàng thành công nhận " + ZUtil.number_format(beri_total) + " Beri!");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Trả hàng thành công nhận " + ZUtil.number_format(beri_total) + " Beri!");
                }

                if (p.typePirate == 0) {
                    p.time_ship++;
                    p.item.add_item_bag47(4, (short) 211, 5);
                    p.item.updateInventory(false);
                }
                event.EventManager.dispatchOnVanChuyen(p);
                p.name_ThoSanHaiTac = null;
                p.updateMoney();
                break;
            }
            case 2: { // 2. Đăng ký bảo vệ hàng
                p.getService().send_box_ThongBao_OK("Hãy chọn 'Đăng ký chức năng' -> 'Thợ săn Hải Tặc' và đứng tại khu vực để các Lái Buôn có thể thuê bạn bảo vệ chuyến hàng.");
                break;
            }
            case 3: { // 3. Thuê bảo vệ hàng
                if (p.ship_pet != null) {
                    List<String> hunterNames = new ArrayList<>();
                    if (p.map != null && p.map.players != null) {
                        for (int i = 0; i < p.map.players.size(); i++) {
                            Player p0 = p.map.players.get(i);
                            if (p0 != null && p0.typePirate == 1 && !p0.name.equals(p.name)) {
                                hunterNames.add(p0.name);
                            }
                        }
                    }
                    if (!hunterNames.isEmpty()) {
                        p.name_ThoSanHaiTac = hunterNames.toArray(new String[0]);
                        p.getService().openDynamicMenu(982, "Chọn Thợ Săn Hải Tặc", p.name_ThoSanHaiTac, null);
                    } else {
                        p.getService().openDynamicMenu(982, "Chọn Thợ Săn Hải Tặc", new String[]{"Trống"}, null);
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Hãy nhận chuyến hàng trước khi thuê bảo vệ!");
                }
                break;
            }
            case 4: { // 4. Đăng ký chức năng
                if (p.ship_pet == null) {
                    String[] roles = new String[]{"Lái buôn", "Thợ săn Hải Tặc", "Hải Tặc", "Không đăng ký"};
                    short[] icons = new short[]{107, 110, 111, 114};
                    byte[] selectStates = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        selectStates[i] = (p.typePirate == (i == 3 ? -1 : i)) ? (byte) 3 : (byte) 7;
                    }
                    p.getService().send_dynamic_menu_type6(985, 0, "Đăng ký chức năng", roles, icons, selectStates);
                } else {
                    p.getService().send_box_ThongBao_OK("Hãy hoàn thành hoặc hủy chuyến hàng hiện tại trước khi đổi chức năng!");
                }
                break;
            }
            case 5: { // 5. Hủy vận buôn
                if (p.ship_pet != null) {
                    if (p.ship_pet.map != null) {
                        Message m = new Message(3);
                        m.writer().writeShort(p.ship_pet.index_map);
                        m.writer().writeByte(2);
                        p.ship_pet.map.send_msg_all_p(m, null, true);
                        m.cleanup();
                    }
                    Ship_pet.remove(p.ship_pet);
                    p.ship_pet = null;
                    p.id_ship_packet = -1;
                    p.getService().send_box_ThongBao_OK("Đã hủy chuyến vận buôn thành công!");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn hiện tại không có chuyến vận buôn nào để hủy.");
                }
                break;
            }
            case 6: { // 6. Gọi Lạc đà trở về
                if (p.ship_pet != null) {
                    if (p.map == null || p.map.template == null) return;
                    if (p.map.template.id > p.ship_pet.id_map_save) {
                        Zone[] saveZones = Zone.getMapByID(p.ship_pet.id_map_save);
                        String saveName = (saveZones != null && saveZones.length > 0 && saveZones[0] != null) ? saveZones[0].template.name : "map xuất phát";
                        p.getService().send_box_ThongBao_OK("Hiện tại chỉ có thể gọi lạc đà từ " + saveName + " trở lại!");
                        return;
                    }
                    if (p.ship_pet.map != null && !p.ship_pet.map.equals(p.map)) {
                        Message mOld = new Message(3);
                        mOld.writer().writeShort(p.ship_pet.index_map);
                        mOld.writer().writeByte(2);
                        p.ship_pet.map.send_msg_all_p(mOld, null, true);
                        mOld.cleanup();
                    }
                    p.ship_pet.map = p.map;
                    p.ship_pet.x = p.x;
                    p.ship_pet.y = p.y;

                    Message m_local = new Message(1);
                    m_local.writer().writeByte(0);
                    m_local.writer().writeShort(p.ship_pet.index_map);
                    m_local.writer().writeShort(p.ship_pet.x);
                    m_local.writer().writeShort(p.ship_pet.y);
                    p.map.send_msg_all_p(m_local, null, true);
                    m_local.cleanup();

                    p.getService().send_box_ThongBao_OK("Lạc đà vận chuyển đã được triệu hồi đến vị trí của bạn!");
                } else {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy chuyến hàng của bạn!");
                }
                break;
            }
            case 7: { // 7. Xem vị trí lạc đà
                if (p.ship_pet != null && p.ship_pet.map != null && p.ship_pet.map.template != null) {
                    p.getService().send_box_ThongBao_OK("Vị trí lạc đà: " + p.ship_pet.map.template.name + " (Khu " + (p.ship_pet.map.zone_id + 1) + ") tọa độ (" + p.ship_pet.x + ", " + p.ship_pet.y + ").");
                } else {
                    p.ship_pet = null;
                    p.getService().send_box_ThongBao_OK("Không tìm thấy lạc đà chở hàng!");
                }
                break;
            }
            case 8: { // 8. Xem số lần vận buôn
                p.getService().send_box_ThongBao_OK("Hôm nay bạn đã hoàn thành " + p.time_ship + " / 5 chuyến vận buôn.");
                break;
            }
            case 9: { // 9. Hướng dẫn
                p.getService().Help_From_Server(-2,
                    "HƯỚNG DẪN VẬN CHUYỂN HÀNG\n\n"
                    + "1. LÁI BUÔN (TYPE 0):\n"
                    + "- Chọn 'Đăng ký chức năng' -> 'Lái buôn'.\n"
                    + "- Đến Làng Cối Xay Gió (Map 1), chọn 'Lấy Hàng', trả 10.000 Beri để nhận Lạc đà.\n"
                    + "- Vận chuyển an toàn đến NPC Quán ăn ở:\n"
                    + "  + Thị trấn Vỏ Sò (Map 9): Thưởng x1.0 (2M - 3.5M Beri)\n"
                    + "  + Thị trấn Orange (Map 17): Thưởng x1.5 (3M - 5.25M Beri)\n"
                    + "  + Thị trấn Syrup (Map 25): Thưởng x2.0 (4M - 7M Beri)\n"
                    + "- Tối đa 5 chuyến/ngày.\b"
                    + "2. THỢ SĂN HẢI TẶC (TYPE 1):\n"
                    + "- Đăng ký làm Thợ săn và đi cùng Lái buôn để bảo vệ.\n"
                    + "- Nhận được 1/3 tổng tiền Beri khi Lái buôn trả hàng thành công!\b"
                    + "3. HẢI TẶC (TYPE 2):\n"
                    + "- Đăng ký làm Hải Tặc, tấn công cướp Lạc đà của Lái buôn ở map ngoài làng.\n"
                    + "- Đưa Lạc đà cướp được về các thị trấn để nhận 30% Beri!"
                );
                break;
            }
        }
    }
}

