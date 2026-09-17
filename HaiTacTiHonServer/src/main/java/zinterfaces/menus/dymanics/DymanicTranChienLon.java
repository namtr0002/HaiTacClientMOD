package zinterfaces.menus.dymanics;

import static core.MenuController.sendDymanicMenu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.zones.WorldWar;
import model.Player;
import network.Service;
import template.GiftBox;
import template.ItemTemplate4;
import zinterfaces.iMenu;

public class DymanicTranChienLon implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{-911};
    }

    private static final String GUIDE_WAR_TXT = "LỄ HỘI HẢI TẶC\n\n"
            + "Chào mừng bạn đến với chiến trường Lễ Hội Hải Tặc rực lửa!\b"
            + "THỜI GIAN HOẠT ĐỘNG HÀNG NGÀY:\n"
            + "- 19h00 - 19h15: Thời gian đăng ký và chọn phe (Hải Quân hoặc Hải Tặc).\n"
            + "- 19h15 - 21h00: Lễ hội chính thức bắt đầu, PK tự do tranh đoạt điểm giữa 2 phe.\n"
            + "- Sau 21h00: Kết thúc lễ hội và tự động phát thưởng.\b"
            + "PHẦN THƯỞNG & QUY ĐỊNH:\n"
            + "- Tự động trao thưởng Top 1 - 3 điểm cao nhất và thưởng phe chiến thắng qua Hòm Thư.\n"
            + "- Mỗi khi hoạt động mở, bạn bắt buộc phải đăng ký chọn lại phe từ đầu.\n"
            + "- Chúc các bạn có những trận chiến nảy lửa và giành chiến thắng vẻ vang!";

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (WorldWar.status == WorldWar.STATUS_CLOSE) {
            if (index == 0) {
                p.getService().send_box_ThongBao_OK("Phần thưởng Lễ Hội (Top 1-3, Thưởng Phe, Thưởng Tham Gia) đã được hệ thống tự động gửi vào Hộp Thư của bạn!\nVui lòng mở Hộp Thư để nhận.");
            } else if (index == 1) {
                p.getService().send_box_ThongBao_OK("Không thể thực hiện thao tác này khi chưa diễn ra lễ hội");
            } else if (index == 2) {
                p.getService().Help_From_Server(-78, GUIDE_WAR_TXT);
            }
        } else if (WorldWar.status == WorldWar.STATUS_REGISTER) {
            if (index == 0) {
                if (WorldWar.isNavy(p.name) || WorldWar.isPirate(p.name)) {
                    p.getService().send_box_ThongBao_OK("Bạn đã đăng ký chọn phe rồi! Hãy chờ đến 19h15 để tham chiến.");
                } else {
                    sendDymanicMenu(p, -990, "Chọn Phe Tham Chiến", new String[]{"Gia Nhập Phe Hải Quân", "Gia Nhập Phe Hải Tặc"}, null);
                }
            } else if (index == 1) {
                p.getService().send_box_ThongBao_OK("Lễ hội chưa bắt đầu, bảng xếp hạng trống.");
            } else if (index == 2) {
                p.getService().Help_From_Server(-78, GUIDE_WAR_TXT);
            }
        } else if (WorldWar.status == WorldWar.STATUS_WAR) {
            if (index == 0) {
                p.getService().send_box_ThongBao_OK(WorldWar.findTop());
            } else if (index == 1) {
                p.getService().Help_From_Server(-78, GUIDE_WAR_TXT);
            }
        }
    }
    
}
