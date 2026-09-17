package clan;

import java.util.List;

public class ClanMember {
    public int playerId = -1;
    public String name = "";
    public short id = 0;
    public short level = 1;
    public byte levelInclan = 10;
    public short donate = 0;
    public short gopRuby = 0;
    public short numquest = 0;
    public int conghien = 0;
    public short head = 0;
    public short hair = 1;
    public short hat = -1;
    public byte clazz = 1;
    public long timeJoinClan = System.currentTimeMillis();

    public static int get_id(List<ClanMember> members) {
        int result = 0;
        for (int i = 0; i < members.size(); i++) {
            result = Math.max(result, members.get(i).id);
        }
        result++;
        return result;
    }
    
    public boolean isJoin24H() {
        return  System.currentTimeMillis() - timeJoinClan > 1000 * 60 * 60 * 24;
    }
}
