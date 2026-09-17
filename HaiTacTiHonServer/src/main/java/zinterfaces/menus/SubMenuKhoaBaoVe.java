package zinterfaces.menus;

import model.Player;
import model.InputDialog;
import model.YesNoDialog;
import zinterfaces.iMenu;
import java.io.IOException;

/**
 * SubMenuKhoaBaoVe — Xử lý menu Khóa Bảo Vệ (Dynamic Menu ID 994)
 * Hỗ trợ tự động hiển thị hướng dẫn theo từng bản đồ (Map 1, 9, 17, 25, 33, 49, 69, 83).
 */
public class SubMenuKhoaBaoVe implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{994};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC != 994) return;

        switch (index) {
            case 0: { // Đăng ký / Đổi mã khóa
                if (p.mbv == null || p.mbv.isBlank()) {
                    new InputDialog(p, 22, "Nhập mã bảo vệ:", new String[]{""}).startInput();
                } else {
                    p.getService().send_box_ThongBao_OK("Hiện tại đang dùng mã khoá bảo vệ khác!");
                }
                break;
            }
            case 1: { // Hướng dẫn
                String txt = "HƯỚNG DẪN KHÓA BẢO VỆ\n\n"
                        + "• Ai cũng có những món đồ mình rất quý trọng và không muốn mất nó.\n"
                        + "• Chức năng Khóa Bảo Vệ sẽ giúp bạn bảo vệ tài sản tài khoản an toàn tuyệt đối.\b"
                        + "CÁCH THỨC HOẠT ĐỘNG:\n"
                        + "- Sau khi đăng ký mã khóa bảo vệ, các thao tác có ảnh hưởng đến tài sản của bạn sẽ yêu cầu nhập đúng mã để xác nhận.\n"
                        + "- Khi đang khóa bảo vệ, các thao tác: Giao dịch, Bán trang bị, Vứt bỏ item, Tháo ngọc... sẽ bị chặn để chống trộm cắp.\b"
                        + "QUY ĐỊNH HỦY MÃ KHÓA:\n"
                        + "- Nếu muốn hủy mã, bạn có thể chọn 'Đăng ký hủy mã khóa'.\n"
                        + "- Thời gian chờ hủy an toàn là 24 giờ (1 ngày) để đảm bảo an toàn tuyệt đối cho tài khoản.";
                short helpNpcId = -3;
                if (p.map != null && p.map.template != null) {
                    switch (p.map.template.id) {
                        case 1:  helpNpcId = -3; break;
                        case 9:  helpNpcId = -15; break;
                        case 17: helpNpcId = -23; break;
                        case 25: helpNpcId = -30; break;
                        case 33: helpNpcId = -38; break;
                        case 49: helpNpcId = -69; break;
                        case 69: helpNpcId = -75; break;
                        case 83: helpNpcId = -88; break;
                        default: helpNpcId = (short) p.currentNpcId; break;
                    }
                }
                p.getService().Help_From_Server(helpNpcId, txt);
                break;
            }
            case 2: { // Đăng ký hủy mã khóa
                if (p.mbv == null || p.mbv.isBlank()) {
                    p.getService().send_box_ThongBao_OK("Hiện tại chưa đăng ký mã khoá bảo vệ!");
                } else {
                    if (p.timeHuyMbv != -1) {
                        long t = p.timeHuyMbv - System.currentTimeMillis();
                        t /= 1000;
                        if (t < 0) t = 0;
                        p.getService().send_box_ThongBao_OK("Hiện tại đã đăng ký hủy mã. Thời gian còn lại: " + t + "s");
                    } else {
                        p.data_yesno = new int[]{90};
                        p.setyesNoDialog(new YesNoDialog(p, 90, "Thông báo",
                                "Bạn muốn đăng ký hủy mã khóa? Thời gian hủy 1 ngày.",
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                }
                break;
            }
            case 3: { // Hủy đăng ký xóa mã rương
                if (p.mbv != null && !p.mbv.isBlank() && p.timeHuyMbv != -1) {
                    p.timeHuyMbv = -1;
                    p.mbv = "";
                    p.passBagOK = true;
                    p.getService().send_box_ThongBao_OK("Hủy đăng ký thành công");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đăng ký hủy mã bảo vệ!");
                }
                break;
            }
        }
    }
}
