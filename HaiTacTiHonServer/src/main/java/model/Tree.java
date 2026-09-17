package model;

import map.Zone;

public class Tree {
    public short index, x, y;
    public String name, nameKill;
    public int hp;
    public long timeTuoiNuoc, timeBonPhan, timeThuHoach, timeHp;
    public Zone map;
    public byte type;
    public boolean canThuHoach;
    public byte stateNew, stateOld;

    public void setup(Player p) {
        this.x = p.x;
        this.y = p.y;
        this.name = p.name;
        this.nameKill = "";
        this.hp = 100;
        this.timeThuHoach = System.currentTimeMillis() + 60_000L * 10; // [FIX BUG-8] 10 phút thu hoạch
        this.timeHp = System.currentTimeMillis() + 1_000L;
        // [FIX BUG-8] Khởi tạo timer nước/phân để cây không bị "khô" ngay sau khi trồng
        this.timeTuoiNuoc = System.currentTimeMillis() + 30_000L;
        this.timeBonPhan  = System.currentTimeMillis() + 30_000L;
        this.map = p.map;
        this.canThuHoach = false;
    }
}
