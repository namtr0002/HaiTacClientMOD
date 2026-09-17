package template;

/**
 * Data structure representing a Clan's "Thủ Lĩnh Biển Khơi" status (DTO).
 * Stores flag, buffs, times, and points for clan activities.
 */
public class ThuLinhBienKhoiClan {
    public int flag;
    public int buff;
    public long time;
    public int point;

    public ThuLinhBienKhoiClan() {
        this.flag = 0;
        this.buff = -1;
        this.time = 0;
        this.point = 0;
    }
}
