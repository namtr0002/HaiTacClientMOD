package zinterfaces.menus;

import model.Player;
import zinterfaces.iMenu;
import template.EffTemplate;
import java.io.IOException;

public class SubMenuExpLock implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{932, 933};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == 932) {
            if (index == 0) {
                // Do nothing/placeholder
            } else if (index == 1) {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                }
            }
        } else if (idNPC == 933) {
            if (index == 0) {
                EffTemplate effTemplate = p.get_eff(8);
                if (effTemplate != null) {
                    effTemplate.time = 0;
                    p.getService().send_box_ThongBao_OK("Hủy thành công");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn đang không sử dụng khóa EXP");
                }
            } else if (index == 1) {
                if (p.detu == null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");
                    return;
                }
                EffTemplate effTemplate = p.detu.get_eff(8);
                if (effTemplate != null) {
                    p.detu.removeEff(effTemplate);
                    p.getService().send_box_ThongBao_OK("Hủy thành công");
                } else {
                    p.getService().send_box_ThongBao_OK("Đệ tử đang không sử dụng khóa EXP");
                }
            }
        }
    }
}
