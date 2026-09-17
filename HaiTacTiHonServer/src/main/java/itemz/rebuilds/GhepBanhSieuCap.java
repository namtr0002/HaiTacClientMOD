package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import template.ItemTemplate4;
import event.Event;
import event.EventManager;
import event.SuKienBigMom;

import java.io.IOException;

/**
 * GhepBanhSieuCap — Bàn đổi Bánh Kem Siêu Cấp (877).
 * Công thức: 5 Bánh Kem Mê Hoặc (876) -> 1 Bánh Kem Siêu Cấp (877).
 */
public class GhepBanhSieuCap extends AbsCheTao {

    public GhepBanhSieuCap() {
        super(SuKienBigMom.ITEM_BANH_SIEU_CAP, (byte) 4, 0, 0);
        this.successRate = 100;
        this.payChoice = false;
        // 5 Bánh Kem Mê Hoặc (876)
        addIngredient(SuKienBigMom.ITEM_BANH_ME_HOAC, (byte) 4, 5);
    }

    @Override
    public void show_table(Player p) throws IOException {
        if (p == null) return;
        if (p.level < 40) {
            p.getService().send_box_ThongBao_OK("Cần đạt cấp độ 40 trở lên mới có thể đổi bánh!");
            return;
        }

        short[] ingIds = new short[]{SuKienBigMom.ITEM_BANH_ME_HOAC};
        short[] ingQtys = new short[]{5};
        byte[] ingCats = new byte[]{4};
        short[] ingIcons = new short[1];

        for (int i = 0; i < ingIds.length; i++) {
            ItemTemplate4 it = ItemTemplate4.get_it_by_id(ingIds[i]);
            ingIcons[i] = it != null ? it.icon : 0;
        }

        short resultIcon = 0;
        ItemTemplate4 resIt = ItemTemplate4.get_it_by_id(resultId);
        if (resIt != null) {
            resultIcon = resIt.icon;
        }

        p.setCheTao(this);
        p.getService().sendUpgradeDevilCraftPanel(
            "Đổi Bánh Kem Siêu Cấp",
            (byte) ingIds.length,
            ingIds,
            ingQtys,
            ingCats,
            ingIcons,
            costBeri,
            (short) costRuby,
            costExtol,
            (short) resultId,
            (short) 1,
            resultCat,
            resultIcon,
            (byte) successRate
        );
    }

    @Override
    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        // Sau khi đổi thành công
    }

    @Override
    protected boolean isEventActive() {
        return EventManager.isActive(Event.ID_SUKIEN_BIGMOM);
    }
}
