package model;

import java.util.List;

public class ActiveBuff {
    public short canonicalBuffId;
    public short icon;
    public short effSkill;
    public long expireTime;
    public List<Short> list_id;
    public List<Integer> list_par;
    public short[] extraEffs;
    public short idDataEff; // e.g. 3, 23, 24, 3124 if any

    public ActiveBuff(short canonicalBuffId, short icon, short effSkill, long expireTime, List<Short> list_id, List<Integer> list_par, short[] extraEffs, short idDataEff) {
        this.canonicalBuffId = canonicalBuffId;
        this.icon = icon;
        this.effSkill = effSkill;
        this.expireTime = expireTime;
        this.list_id = list_id;
        this.list_par = list_par;
        this.extraEffs = extraEffs;
        this.idDataEff = idDataEff;
    }
}
