package event.eboss;

import model.Player;
import core.ZUtil;
import core.ZCollection;
import database.RandomCollection;
import itemz.MainItem;
import zabstracts.AbsEventBoss;
import mob.Mob;
import java.util.ArrayList;

public class LanSuTu extends AbsEventBoss {

    private static final RandomCollection<MainItem> giftKillBossALL = new RandomCollection<>();
    static {
        giftKillBossALL.add(5, new MainItem(158, 4, 1));
        giftKillBossALL.add(5, new MainItem(45, 4, 1));
        giftKillBossALL.add(10, new MainItem(46, 4, 1));
    }

    public LanSuTu(int slotId) {
        super(slotId,
              153,           // mobTemplateId: Lân Sư Tử
              500,           // hpMax
              99,            // level
              2, 4,          // defaultMapId=2, defaultZoneId=4
              "Boss Lân Sư Tử");
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
                    id = ZCollection.listDa2.randomId();
                } else if (id == 46) {
                    id = ZCollection.listDa3.randomId();
                } else if (cat == 7) {
                    num = ZUtil.random(1, 5);
                }
                listGift.add(new MainItem(id, cat, num));
            }
            MainItem.showGiftBox(p, "Quà Giỗ Tổ", "Hạ gục Boss chung", listGift, true, true);
        }
    }

    @Override
    public void onKillReward(Player killer) {
        ArrayList<MainItem> listGift = new ArrayList<>();
        listGift.add(ZCollection.listDa4.randomItem());
        listGift.add(new MainItem(11, 7, 1));
        listGift.add(new MainItem(172, 4, 1));
        listGift.add(new MainItem(0, 4, ZUtil.random(10_000, 200_000)));
        listGift.add(ZCollection.listDa3.randomItem());
        MainItem.showGiftBox(killer, "Quà Giỗ Tổ", "Hạ gục Boss", listGift, true, true);
        killer.update_pointEvent2(1);
    }

    @Override public short getDefaultX() { return 500; }
    @Override public short getDefaultY() { return 250; }
}
