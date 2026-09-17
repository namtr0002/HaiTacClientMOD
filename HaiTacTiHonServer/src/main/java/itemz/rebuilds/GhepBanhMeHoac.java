package itemz.rebuilds;

import zabstracts.AbsCheTao;
import model.Player;
import template.ItemTemplate4;
import event.Event;
import event.EventManager;
import event.SuKienBigMom;

import java.io.IOException;

/**
 * GhepBanhMeHoac — Bàn làm Bánh Kem Mê Hoặc (876).
 * Công thức: 2 Đường Trắng (875) + 1 Bột Mì (874) -> 1 Bánh Kem Mê Hoặc (876).
 */
public class GhepBanhMeHoac extends AbsCheTao {

    public GhepBanhMeHoac() {
        super(SuKienBigMom.ITEM_BANH_ME_HOAC, (byte) 4, 0, 0);
        this.successRate = 100;
        this.payChoice = false;
        // 2 Đường trắng (875) + 1 Bột mì (874)
        addIngredient(SuKienBigMom.ITEM_DUONG_TRANG, (byte) 4, 2);
        addIngredient(SuKienBigMom.ITEM_BOT_MI, (byte) 4, 1);
    }

    @Override
    public void show_table(Player p) throws IOException {
        if (p == null) return;
        if (p.level < 40) {
            p.getService().send_box_ThongBao_OK("Cần đạt cấp độ 40 trở lên mới có thể làm bánh!");
            return;
        }

        short[] ingIds = new short[]{SuKienBigMom.ITEM_DUONG_TRANG, SuKienBigMom.ITEM_BOT_MI};
        short[] ingQtys = new short[]{2, 1};
        byte[] ingCats = new byte[]{4, 4};
        short[] ingIcons = new short[2];

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
            "Làm Bánh Kem Mê Hoặc",
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
        // Sau khi ghép thành công
    }

    @Override
    protected boolean isEventActive() {
        return EventManager.isActive(Event.ID_SUKIEN_BIGMOM);
    }
}
