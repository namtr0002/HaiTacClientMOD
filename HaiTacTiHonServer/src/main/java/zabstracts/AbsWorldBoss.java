package zabstracts;

import zabstracts.AbsBoss;
import model.Player;
import core.Manager;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Zone;
import mob.Mob;
import map.MapManager;
import template.DataTemplate;
import template.MobTemplate;

public abstract class AbsWorldBoss extends AbsBoss {
    public static Mob mob;

    public AbsWorldBoss(int mob_id) {
        super(mob_id);
        mob = this;
        this.isSieuTrum = false;
        this.boss_inf = null;
    }

    @Override
    public boolean isSieuTrum() {
        return false;
    }

    @Override
    public boolean isLevelUpOnDeath() {
        return false;
    }

    @Override
    public byte[] getSpawnHours() {
        return null;
    }

    @Override
    public int getRespawnTimeSeconds() {
        return -1;
    }

    @Override
    public void checkRespawn() {
        // Controlled manually by event handlers, no auto-respawn checks
    }

    protected abstract int getMobTemplateId();
    protected abstract int getMobIndex();       // -2 hoặc -10
    protected abstract String getDisplayName();
    protected abstract boolean isValidMapId(int templateId); // filter map

    protected Zone selectZoneFrom(Zone[] zones) {
        return zones[ZUtil.random(zones.length)];
    }

    public void spawn() throws IOException {
        Zone[] mapsSelect = MapManager.getInstance().getMap(ZUtil.random(MapManager.getInstance().getMaps().size())).zones;
        boolean isSea = false;
        for (int i = 0; i < DataTemplate.mSea.length; i++) {
            if (DataTemplate.mSea[i][1] == mapsSelect[0].template.id) {
                isSea = true;
                break;
            }
        }
        int maxAttempts = 100;
        int attempts = 0;
        while ((mapsSelect[0].list_mob.length < 5 || mapsSelect.length < 5 || isSea || !isValidMapId(mapsSelect[0].template.id)) && attempts < maxAttempts) {
            mapsSelect = MapManager.getInstance().getMap(ZUtil.random(MapManager.getInstance().getMaps().size())).zones;
            isSea = false;
            for (int i = 0; i < DataTemplate.mSea.length; i++) {
                if (DataTemplate.mSea[i][1] == mapsSelect[0].template.id) {
                    isSea = true;
                    break;
                }
            }
            attempts++;
        }

        // Chọn Zone và đảm bảo khu này chưa có Boss nào đang sống
        Zone map0 = selectZoneFrom(mapsSelect);
        if (isZoneOccupied(map0)) {
            Zone freeZone = findAvailableZone(map0.template.id, map0);
            if (freeZone != null) {
                map0 = freeZone;
            } else if (!isAllowMultipleInSameZone()) {
                // Thử tìm map hợp lệ khác còn khu trống
                for (map.Map mCandidate : MapManager.getInstance().getMaps()) {
                    if (mCandidate.zones != null && mCandidate.zones.length > 0 && isValidMapId(mCandidate.zones[0].template.id)) {
                        Zone candidateZone = findAvailableZone(mCandidate.zones[0].template.id, null);
                        if (candidateZone != null) {
                            map0 = candidateZone;
                            break;
                        }
                    }
                }
            }
        }

        if (map0 == null || (isZoneOccupied(map0) && !isAllowMultipleInSameZone())) {
            // Không tìm được khu trống hợp lệ
            return;
        }

        this.isdie = false;
        this.map = map0;
        this.hp_max = getHpMax();
        this.hp = this.hp_max;
        
        short[] coords = calculateCoordinatesInZone(map0, (short) -1, (short) -1);
        this.x = coords[0];
        this.y = coords[1];
        this.index = getMobIndex();
        this.index_mob_save = getMobIndex();
        map0.list_mob_custom_add(this);
        this.map.getService().move(this.index, this.x, this.y);
        Manager.gI().chatKTG(0, getDisplayName() + " đã xuất hiện tại " + map0.template.name + " khu " + (map0.zone_id + 1) + "! Các hải tặc hãy mau đến tiêu diệt!", 5);
    }

    // Abstract methods implemented with defaults for dynamic world bosses
    @Override
    public short[] getSpawnLocation() {
        return new short[]{-1, -1, -1}; // Dynamic random map & coordinates
    }

    @Override
    public int getLevel() {
        return 50;
    }

    @Override
    public short[] getSkills() {
        return new short[0];
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
    }

    @Override
    public void onDeath(Player pKill) {
    }

    @Override
    public void dropReward(Player pKill, List<template.GiftBox> list_gift) {
    }
}
