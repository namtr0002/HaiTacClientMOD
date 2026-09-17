package template;

import model.Player;
import map.Zone;

import java.io.DataOutputStream;
import java.io.IOException;

public class InfoMemList {
    public int id;
    public String name;
    public short level;
    public short head;
    public short hair;
    public short hat;
    public short body = -1;
    public short leg = -1;
    public short weapon = -1;
    public String info;
    public long thongthao;
    public short rank;

    public static void WriteInfoMemList(DataOutputStream dos, InfoMemList temp) throws IOException {
        Player p0 = Zone.get_player_by_name_allmap(temp.name);
        dos.writeInt(temp.id);
        dos.writeUTF(temp.name);
        dos.writeShort(p0 != null ? p0.level : temp.level);
        dos.writeShort(p0 != null ? p0.get_head() : temp.head);
        dos.writeShort(p0 != null ? p0.get_hair() : temp.hair);
        dos.writeShort(p0 != null ? p0.get_hat() : temp.hat);
        dos.writeByte(p0 != null ? 1 : 0);
        dos.writeUTF(temp.info != null ? temp.info : "");
        dos.writeShort(temp.rank);
    }
}
