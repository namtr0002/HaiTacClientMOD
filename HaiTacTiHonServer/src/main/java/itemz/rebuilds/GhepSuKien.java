package itemz.rebuilds;

import zabstracts.AbsCombie;
import model.Player;
import model.YesNoDialog;
import java.io.IOException;

public class GhepSuKien extends AbsCombie {

    @Override
    public byte getType() {
        return 15;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (type != 15) return;

        // Action 1: Bỏ vật phẩm vào
        if (action == 1 && cat == 4 && idItem >= 0 && num > 0) {
            if (p.item.total_item_bag_by_id(cat, idItem) < num) {
                p.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                return;
            }
            if (isValidEventMaterial(idItem)) {
                p.getService().sendRebuildPutItem(idItem, cat, num);
            } else {
                p.getService().send_box_ThongBao_OK("Vật phẩm này không dùng để ghép sự kiện!");
            }
            return;
        }

        // Action 5: Bắt đầu ghép
        if (action == 5 && cat == 4 && idItem >= 0 && num > 0) {
            delegateCombine(p, idItem, num);
        }
    }

    private boolean isValidEventMaterial(short idItem) {
        return idItem == 353 || // Gạo nếp (sự kiện tết 353)
               idItem == 583 || // Kim cương thô (sk cũ)
               idItem == 597 || // Bóng Bạc (Đấu Trường Rực Lửa)
               idItem == 598 || // Bóng Vàng (Đấu Trường Rực Lửa)
               idItem == 599 || // Vé World Cup (Đấu Trường Rực Lửa)
               idItem == 609 || // Rương World Cup
               idItem == 797 || // Quả bóng bạc (sk 30/4)
               idItem == 799 || // Quả bóng vàng (sk 30/4)
               idItem == 591 || // Cành hoa 20/10
               idItem == 109 || // Nang Tre (sk Trung Thu)
               idItem == 434 || // Socola Đỏ (sk Valentine)
               idItem == 812 || // Hoa hồng đỏ (sk 8/3)
               idItem == 813 || // Hoa hồng vàng (sk 8/3)
               idItem == 814 || // Hoa hồng xanh (sk 8/3)
               idItem == 815 || // Bó hoa hồng đỏ (sk 8/3)
               idItem == 888 || // Hoa hồng trắng (sk Vu Lan 2026)
               idItem == 889 || // Hoa hồng đỏ (sk Vu Lan 2026)
               idItem == 571 || // Gỗ (sk Thất Tịch)
               idItem == 572 || // Đá (sk Thất Tịch)
               idItem == 573 || // Lông chim thước (sk Thất Tịch)
               idItem == 575;   // Giấy gói quà (sk Thất Tịch)
    }

    private void delegateCombine(Player p, short idItem, short num) throws IOException {
        switch (idItem) {
            case 571:
            case 572:
            case 573:
                p.setyesNoDialog(new YesNoDialog(p, 9950, "Sự Kiện Thất Tịch", "Chọn hoạt động muốn thực hiện:",
                    new String[]{"Nấu Chè Thường", "Nấu Chè VIP", "Xây Cầu Ô Thước", "Hủy"}, new byte[]{-1, -1, -1, 1},
                    value -> {
                        try {
                            if (value == 0) {
                                new GhepCheDauDo().show_table(p);
                            } else if (value == 1) {
                                new GhepCheDauDoDacBiet().show_table(p);
                            } else if (value == 2) {
                                new XayCauOThuoc().show_table(p);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                p.getService().startYesNo();
                break;
            case 575:
                new GhepHopBanhThuongHang().show_table(p);
                break;
            case 597:
            case 598:
            case 599:
                new GhepRuongWorldcup().show_table(p);
                break;
            case 609:
                new GhepCupWorldcup().show_table(p);
                break;
            case 353:
                new GhepBanhChung350().show_table(p);
                break;
            case 583:
                p.setyesNoDialog(new YesNoDialog(p, 9921, "Gói hộp quà", "Bạn muốn gói loại hộp nào?",
                    new String[]{"Hộp Tím", "Hộp Đỏ", "Hủy"}, new byte[]{-1, -1, 1},
                    value -> {
                        try {
                            if (value == 0) {
                                new GhepHopKimCuongTim().show_table(p);
                            } else if (value == 1) {
                                new GhepHopKimCuongDo().show_table(p);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                p.getService().startYesNo();
                break;
            case 797:
                new GhepKimCuongDo803().show_table(p);
                break;
            case 799:
                new GhepKimCuongTim804().show_table(p);
                break;
            case 591:
                p.setyesNoDialog(new YesNoDialog(p, 9923, "Gói hộp quà", "Bạn muốn gói hộp quà nào?",
                    new String[]{"Quà Đặc Biệt", "Quà Thường", "Hủy"}, new byte[]{-1, -1, 1},
                    value -> {
                        try {
                            if (value == 0) {
                                new GhepHopQuaDacBiet().show_table(p);
                            } else if (value == 1) {
                                p.setyesNoDialog(new YesNoDialog(p, 9924, "Quà Thường", "Chọn mốc quà thường muốn gói:",
                                    new String[]{"Mốc 1: 5 hoa", "Mốc 2: 10 hoa", "Mốc 3: 15 hoa", "Mốc 4: 20 hoa", "Hủy"}, new byte[]{-1, -1, -1, -1, 1},
                                    val -> {
                                        try {
                                            if (val == 0) new GhepHopQuaThuong1().show_table(p);
                                            else if (val == 1) new GhepHopQuaThuong2().show_table(p);
                                            else if (val == 2) new GhepHopQuaThuong3().show_table(p);
                                            else if (val == 3) new GhepHopQuaThuong4().show_table(p);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }));
                                p.getService().startYesNo();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                p.getService().startYesNo();
                break;
            case 109:
                p.setyesNoDialog(new YesNoDialog(p, 9925, "Làm lồng đèn", "Bạn muốn làm loại đèn nào?",
                    new String[]{"Đèn Kéo Quân", "Lồng Đèn Thường", "Hủy"}, new byte[]{-1, -1, 1},
                    value -> {
                        try {
                            if (value == 0) {
                                new GhepDenKeoQuan().show_table(p);
                            } else if (value == 1) {
                                new GhepLongDenThuong().show_table(p);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                p.getService().startYesNo();
                break;

            case 434:
                new GhepSocola().show_table(p);
                break;
            case 812:
                new GhepBoHoaDo().show_table(p);
                break;
            case 813:
                new GhepBoHoaVang().show_table(p);
                break;
            case 814:
                new GhepBoHoaXanh().show_table(p);
                break;
            case 815:
                new GhepGioHoa().show_table(p);
                break;
            case 888:
            case 889:
                new GhepBoHoaRucRo().show_table(p);
                break;
            default:
                p.getService().send_box_ThongBao_OK("Công thức ghép sự kiện không hợp lệ!");
                break;
        }
    }
}
