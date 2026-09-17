package zinterfaces.menus.npcs;

import model.Player;
import model.Menu;
import model.InputDialog;
import event.Event;
import event.EventData;
import event.EventManager;
import event.SuKienBigMom;
import map.Npc;
import map.Zone;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcPudding — Handler NPC Charlotte Pudding (idmenu -154).
 *
 * Chỉ xuất hiện tại Thị Trấn Khởi Đầu (Map 49) khi Sự Kiện Big Mom đang chạy.
 * SQL data chuẩn: [-154,"C.Pudding","Thông Tin","Cậu cần đổi quà? Hãy đến gặp tớ nhé.",804,312,0,-1,0,0,0,[82,2],0,0,[]]
 */
public class NpcPudding implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-154, -878};
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null || zone.template.npcs == null) return;

        boolean isEventActive = EventManager.isActive(Event.ID_SUKIEN_BIGMOM);

        // Chỉ xuất hiện tại Thị Trấn Khởi Đầu (Map 49) hoặc Làng Cối Xay Gió (Map 1) khi event Big Mom chạy
        if (zone.template.id == 49 || zone.template.id == 1) {
            if (isEventActive) {
                if (!hasNpc(zone, (short) -154)) {
                    Npc pudding = new Npc();
                    pudding.idmenu    = (short) -154;
                    pudding.name      = "C.Pudding";
                    pudding.namegt    = "Thông Tin";
                    pudding.chat      = "Cậu cần đổi quà? Hãy đến gặp tớ nhé.";
                    pudding.x         = (zone.template.id == 1) ? (short) 380 : (short) 355;
                    pudding.y         = (zone.template.id == 1) ? (short) 170 : (short) 174;
                    pudding.isPerson  = 0;
                    pudding.typeIcon  = -1;
                    pudding.wBlock    = 0;
                    pudding.hBlock    = 0;
                    pudding.b3        = 0;
                    pudding.dataFrame = new byte[]{82, 2};
                    pudding.head      = 0;
                    pudding.hair      = 0;
                    pudding.wearing   = new short[]{};
                    zone.template.npcs.add(pudding);
                }
            } else {
                zone.template.npcs.removeIf(n -> n != null && n.idmenu == -154);
            }
        }
    }

    private boolean hasNpc(Zone zone, short idmenu) {
        if (zone.template == null || zone.template.npcs == null) return false;
        for (Npc n : zone.template.npcs) {
            if (n != null && n.idmenu == idmenu) return true;
        }
        return false;
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -154;
        p.menus.clear();

        if (type == -878) {
            p.menus.add(new Menu("Điểm Danh", (short) 134, () -> { try { handleMenu(p, -878, 0); } catch (IOException e) {} }));
            p.menus.add(new Menu("Sổ Tay Hải Tặc", (short) 135, () -> { try { handleMenu(p, -878, 1); } catch (IOException e) {} }));
            p.menus.add(new Menu("Mở Sổ Tay ", (short) 166, () -> { try { handleMenu(p, -878, 2); } catch (IOException e) {} }));
            p.menus.add(new Menu("Xem Điểm EXP", (short) 141, () -> { try { handleMenu(p, -878, 3); } catch (IOException e) {} }));
            p.menus.add(new Menu("Hướng Dẫn", (short) 123, () -> { try { handleMenu(p, -878, 4); } catch (IOException e) {} }));
            p.getService().openDynamicMenu(type, "C.Pudding", p.menus);
            return;
        }

        // Kiểm tra cấp độ tham gia
        if (p.level < 40) {
            p.getService().send_box_ThongBao_OK("Cậu cần đạt cấp độ 40 trở lên mới có thể tham gia Sự Kiện Big Mom nhé!");
            return;
        }

        p.menus.add(new Menu("Làm Bánh Mê Hoặc", (short) 165, () -> { try { new itemz.rebuilds.GhepBanhMeHoac().show_table(p); } catch (IOException e) {} }));
        p.menus.add(new Menu("Đổi Bánh Siêu Cấp", (short) 165, () -> { try { new itemz.rebuilds.GhepBanhSieuCap().show_table(p); } catch (IOException e) {} }));
        p.menus.add(new Menu("Cửa Hàng Big Mom", (short) 104, () -> { try { handleMenu(p, -154, 2); } catch (IOException e) {} }));
        p.menus.add(new Menu("Điểm Hài Lòng", (short) 141, () -> { try { handleMenu(p, -154, 3); } catch (IOException e) {} }));
        p.menus.add(new Menu("Hướng Dẫn", (short) 123, () -> { try { handleMenu(p, -154, 4); } catch (IOException e) {} }));

        p.getService().openDynamicMenu(type, "C.Pudding", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        handleMenu(p, currentId, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (menuId == -878) {
            switch (index) {
                case 0: { // Điểm Danh
                    p.getService().openDynamicMenu(-985, "Điểm Danh",
                            new String[]{"Điểm danh", "Xem điểm EXP Sổ Tay"}, null);
                    break;
                }
                case 1: { // Sổ Tay Hải Tặc
                    if (p.getConnStatus() != 1) {
                        p.getService().send_box_ThongBao_OK("Chưa kích hoạt thành viên không thể nhận quà lúc này !");
                        return;
                    }
                    if (p.active_so_tay() < 1) {
                        p.getService().send_box_ThongBao_OK("Chưa kích hoạt sổ tay không thể nhận quà lúc này !");
                        return;
                    }
                    p.getService().openDynamicMenu(-989, "Sổ Tay Hải Tặc",
                            new String[]{
                                "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4", "Cấp 5", "Cấp 6", "Cấp 7", "Cấp 8", "Cấp 9", "Cấp 10",
                                "Cấp 11", "Cấp 12", "Cấp 13", "Cấp 14", "Cấp 15", "Cấp 16", "Cấp 17", "Cấp 18", "Cấp 19", "Cấp 20"
                            },
                            new short[]{
                                165, 165, 165, 165, 165, 165, 165, 165, 165, 165,
                                165, 165, 165, 165, 165, 165, 165, 165, 165, 165
                            });
                    break;
                }
                case 2: { // Mở Sổ Tay
                    if (p.getConnStatus() != 1) {
                        p.getService().send_box_ThongBao_OK("Chưa kích hoạt thành viên không thể nhận quà lúc này !");
                        return;
                    }
                    p.getService().openDynamicMenu(-988, "Mở Sổ Tay",
                            new String[]{"Thường", "Cao Cấp"}, new short[]{165, 165});
                    break;
                }
                case 3: { // Xem Điểm EXP
                    p.getService().send_box_ThongBao_OK("Điểm EXP Sổ Tay hiện tại: " + p.exp_so_tay);
                    break;
                }
                case 4: { // Hướng Dẫn Sổ Tay Hải Tặc
                    String txt = "HƯỚNG DẪN SỔ TAY HẢI TẶC\n\n"
                            + "• Sổ Tay Hải Tặc là chuỗi phần thưởng cấp độ đặc biệt diễn ra theo mùa.\n"
                            + "• Tích lũy điểm EXP Sổ Tay mỗi ngày qua Điểm Danh và hoàn thành nhiệm vụ để nâng cấp Sổ Tay.\b"
                            + "CÁC LOẠI SỔ TAY:\n"
                            + "- Sổ Tay Thường: Mở khóa miễn phí cho tất cả thuyền trưởng đã kích hoạt thành viên.\n"
                            + "- Sổ Tay Cao Cấp: Nhận thêm gấp đôi phần thưởng độc quyền giá trị cao tại mỗi mốc cấp độ!\b"
                            + "PHẦN THƯỞNG 20 CẤP ĐỘ:\n"
                            + "- Mỗi khi tăng cấp Sổ Tay (từ Cấp 1 đến Cấp 20), bạn sẽ nhận được vô số Beri, Ruby, Rương Nguyên Liệu, Đá Thần Thoại và Thời Trang đặc biệt!";
                    p.getService().Help_From_Server(-878, txt);
                    break;
                }
            }
            return;
        }

        // Xử lý menu Charlotte Pudding (ID -154)
        switch (index) {
            case 0: { // Làm Bánh Mê Hoặc qua bàn ghép AbsCheTao (show_table)
                new itemz.rebuilds.GhepBanhMeHoac().show_table(p);
                break;
            }
            case 1: { // Đổi Bánh Siêu Cấp qua bàn ghép AbsCheTao (show_table)
                new itemz.rebuilds.GhepBanhSieuCap().show_table(p);
                break;
            }
            case 2: { // Cửa Hàng Big Mom
                Event ev = EventManager.gI().getEvent(Event.ID_SUKIEN_BIGMOM);
                if (ev != null) {
                    ev.openShop(p);
                }
                break;
            }
            case 3: { // Xem Điểm Hài Lòng
                EventData evData = p.getDataEvent(Event.ID_SUKIEN_BIGMOM);
                int score = 0;
                int dq = 0, vb = 0, tn = 0, vc = 0, lt = 0;
                if (evData != null && evData.data != null) {
                    if (evData.data.length > SuKienBigMom.IDX_DIEM_HAI_LONG) {
                        score = evData.data[SuKienBigMom.IDX_DIEM_HAI_LONG];
                    }
                    dq = evData.data[SuKienBigMom.IDX_DANH_QUAI];
                    vb = evData.data[SuKienBigMom.IDX_VAN_BUON];
                    tn = evData.data[SuKienBigMom.IDX_TRUY_NA];
                    vc = evData.data[SuKienBigMom.IDX_VUON_CAM];
                    lt = evData.data[SuKienBigMom.IDX_LIEN_TANG];
                }
                String msg = "THÔNG TIN SỰ KIỆN BIG MOM:\n"
                        + "Tổng Điểm Hài Lòng: " + score + "\n"
                        + "--- TIẾN ĐỘ NGUYÊN LIỆU HÔM NAY ---\n"
                        + "Đánh quái: " + dq + "/" + SuKienBigMom.LIMIT_DANH_QUAI + " nl\n"
                        + "Vận buôn: " + vb + "/" + SuKienBigMom.LIMIT_VAN_BUON + " nl\n"
                        + "Thắng truy nã: " + tn + "/" + SuKienBigMom.LIMIT_TRUY_NA + " nl\n"
                        + "PB Vườn cam vòng 10: " + vc + "/" + SuKienBigMom.LIMIT_VUON_CAM + " nl\n"
                        + "PB Liên tầng tầng 5: " + lt + "/" + SuKienBigMom.LIMIT_LIEN_TANG + " nl";
                p.getService().send_box_ThongBao_OK(msg);
                break;
            }
            case 4: { // Hướng Dẫn Sự Kiện
                String txt = "SỰ KIỆN TIỆC BÁNH NGỌT BIG MOM\b"
                        + "Yêu cầu cấp độ: Cấp 40 trở lên.\b"
                        + "1. HOẠT ĐỘNG KIẾM NGUYÊN LIỆU (Bột Mì & Đường Trắng):\b"
                        + "- Đánh quái dã ngoại: tối đa 80 nl/ngày.\b"
                        + "- Vận buôn: 2 nl/lần, tối đa 10 nl/ngày.\b"
                        + "- Thắng truy nã: 10 nl/lần, tối đa 30 nl/ngày.\b"
                        + "- PB Vườn Cam Nami vòng 10: 5 nl/lần, tối đa 50 nl/ngày.\b"
                        + "- PB Liên Tầng tầng 5: 5 nl/lần, tối đa 50 nl/ngày.\b"
                        + "2. CÔNG THỨC LÀM BÁNH:\b"
                        + "- 1 Bánh Mê Hoặc = 2 Đường Trắng + 1 Bột Mì.\b"
                        + "- 1 Bánh Siêu Cấp = 5 Bánh Mê Hoặc.\b"
                        + "3. CỐNG NẠP BÁNH CHO BIG MOM:\b"
                        + "- Boss Big Mom bậc 99 (3000 HP) xuất hiện tại các map ngoài làng (1-1, 2-1...).\b"
                        + "- Big Mom miễn nhiễm mọi sát thương đánh thường/skill.\b"
                        + "- Gắn Bánh vào ô phím 4/5 hoặc ấn Dùng trong túi khi gặp Big Mom để dâng bánh.\b"
                        + "- Bánh Mê Hoặc: +1 Điểm Hài Lòng, -1 HP Big Mom.\b"
                        + "- Bánh Siêu Cấp: +5 Điểm Hài Lòng, -5 HP Big Mom.\b"
                        + "- Cống nạp thành công nhận nhiều phần quà quý giá (Huy Hiệu Big Mom, Mảnh Ghép Tiểu Merry, Đá Mài, Ruby, Rương...).\b"
                        + "Chúc các bạn có những trải nghiệm vui vẻ!";
                p.getService().Help_From_Server(-154, txt);
                break;
            }
        }
    }
}

