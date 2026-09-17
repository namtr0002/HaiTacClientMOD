package event.eboss;

import model.Player;
import core.ZUtil;
import event.SuKienHalloween;
import zabstracts.AbsEventBoss;
import mob.Mob;

public class BiNgoMa extends AbsEventBoss {

    public BiNgoMa(int slotId) {
        super(slotId,
              93,            // mobTemplateId: Bí Ngô Ma Quái
              100,           // hpMax
              1,             // level
              2, 4,          // defaultMapId=2, defaultZoneId=4
              "Bí Ngô Ma Quái");
    }

    @Override
    public void onKillRewardAll(Player killer) {
        java.util.List<itemz.MainItem> list = new java.util.ArrayList<>();
        list.add(new itemz.MainItem(ZUtil.random(417, 419), 4, ZUtil.random(1, 3)));
        
        if (ZUtil.random(1000) < 150) {
            list.add(new itemz.MainItem(ZUtil.random(112, 122), 4, ZUtil.random(1, 3)));
        }
        if (ZUtil.random(1000) < 150) {
            list.add(new itemz.MainItem(158, 4, ZUtil.random(1, 3)));
        }
        if (ZUtil.random(1000) < 20) {
            list.add(new itemz.MainItem(ZUtil.random(2) == 0 ? 80 : 737, 4, ZUtil.random(1, 3)));
        }
        if (ZUtil.random(1000) < 200) {
            list.add(new itemz.MainItem(11, 7, ZUtil.random(1, 3)));
        }
        if (ZUtil.random(1000) < 150) {
            list.add(new itemz.MainItem(6, 7, ZUtil.random(1, 3)));
        }
        
        while (list.size() > 2) {            
            list.remove(ZUtil.random(list.size() - 1));
        }
        
        if (list.isEmpty()) {
            list.add(new itemz.MainItem(ZUtil.random(112, 122), 4, ZUtil.random(1, 3)));
        }
        
        for (Player p : killer.map.players) {
            if (killer.IDPlayer == p.IDPlayer) continue;
            itemz.MainItem.showGiftBox(p, "Phần thưởng chung Boss", "Bí Ngô Ma Quái", list, true, true);
        }
    }

    @Override
    public void onKillReward(Player killer) {
        java.util.List<itemz.MainItem> list = new java.util.ArrayList<>();
        list.add(new itemz.MainItem(426, 4, 1));
        list.add(new itemz.MainItem(SuKienHalloween.gI().listDa23.get(ZUtil.random(SuKienHalloween.gI().listDa23.size() - 1)).id, 4, 1));
        list.add(new itemz.MainItem(SuKienHalloween.gI().listManhDo.get(ZUtil.random(SuKienHalloween.gI().listManhDo.size() - 1)).id, 4, 1));
        list.add(new itemz.MainItem(121 + Math.min(Math.max(killer.level / 10, 1), 10), 4, ZUtil.random(1, 6)));
        if (ZUtil.random(1000) < 5) {
            list.add(new itemz.MainItem(727, 4, 1));
        }
        itemz.MainItem.showGiftBox(killer, "Phần thưởng hạ Boss", "Bí Ngô Ma Quái", list, true, true);
    }

    @Override
    public void afterDeath(Player killer, Mob mob) {
        int nextId = slotId + 1;
        if (nextId >= SuKienHalloween.gI().listBiNgoMa.size()) {
            SuKienHalloween.gI().closeBoss();
        } else if (nextId >= 0) {
            BiNgoMa nextBoss = SuKienHalloween.gI().listBiNgoMa.get(nextId);
            nextBoss.mob.hp = nextBoss.mob.hp_max;
            nextBoss.mob.isdie = false;
            nextBoss.mob.sendMove();
        }
    }

    @Override public short getDefaultX() { return 1080; }
    @Override public short getDefaultY() { return 249; }
}
