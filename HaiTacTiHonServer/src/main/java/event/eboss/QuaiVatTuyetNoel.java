package event.eboss;

import model.Player;
import core.ZUtil;
import core.ZCollection;
import database.RandomCollection;
import itemz.MainItem;
import zabstracts.AbsEventBoss;
import mob.Mob;
import java.util.ArrayList;

public class QuaiVatTuyetNoel extends AbsEventBoss {

    private static final RandomCollection<MainItem> giftKillBossALL = new RandomCollection<>();
    static {
        double a = 9995.0 / 4.0;
        giftKillBossALL.add(a, new MainItem(158, 4, 1));
        giftKillBossALL.add(a, new MainItem(122, 4, 1));
        giftKillBossALL.add(a, new MainItem(45, 4, 1));
        giftKillBossALL.add(10, new MainItem(46, 4, 1));
        giftKillBossALL.add(5, new MainItem(692, 4, 1));
        giftKillBossALL.add(a, new MainItem(3, 7, 1));
    }

    public QuaiVatTuyetNoel(int slotId) {
        super(slotId,
              99,            // mobTemplateId: Quái Vật Tuyết
              3000,          // hpMax
              1,             // level
              2, 4,          // defaultMapId=2, defaultZoneId=4
              "Quái Vật Tuyết");
    }

    @Override
    public void onKillRewardAll(Player killer) {
        for (Player p : killer.map.players) {
            ArrayList<MainItem> listGift = new ArrayList<>();
            byte random = (byte) ZUtil.random(1, 3);
            for (int i = 0; i < random; i++) {
                MainItem mainItem = giftKillBossALL.next();
                short id = mainItem.id;
                byte cat = mainItem.cat;
                int num = mainItem.num;

                if (id == 45) {
                    id = ZCollection.listDa2.randomItem().id;
                } else if (id == 122) {
                    id = ZCollection.listRuongCamCungHe.randomItem().id;
                } else if (id == 46) {
                    id = ZCollection.listDa3.randomItem().id;
                } else if (cat == 7) {
                    num = ZUtil.random(1, 5);
                }
                listGift.add(new MainItem(id, cat, num));
            }

            MainItem.showGiftBox(p, "Quà Noel", "Hạ gục Boss chung", listGift, true, true);
        }
    }

    @Override
    public void onKillReward(Player killer) {
        ArrayList<MainItem> listGift = new ArrayList<>();
        listGift.add(ZCollection.listDa4.randomItem());
        listGift.add(ZCollection.listRuongCamCungHe.randomItem());
        listGift.add(new MainItem(158, 4, 2));
        listGift.add(ZCollection.listManhDo.randomItem());
        MainItem.showGiftBox(killer, "Quà Noel", "Hạ gục Boss", listGift, true, true);
    }
}
