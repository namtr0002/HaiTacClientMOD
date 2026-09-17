package map;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapTemplate {
    public static List<MapTemplate> ENTRYS = new CopyOnWriteArrayList<>();
    public int id;
    public String name;
    public List<Vgo> vgos = new CopyOnWriteArrayList<>();
    public List<Npc> npcs = new CopyOnWriteArrayList<>();
    public byte max_zone;
    public byte max_player;
    public byte[][] data;
    public byte IDBack;
    public int HBack;
    public List<BoatInMap> list_boat = new CopyOnWriteArrayList<>();
    public short maxW;
    public short maxH;
    public byte type_view_p;
    public byte b;
    public byte specMap;
    public byte id_eff_map;
    public byte level;
    public byte typeChangeMap;
    public byte[][] mPosMapTrain;
    public String strTimeChange;
    public short w;
    public short h;
    public byte tile_id;

    @Override
    public String toString() {
        return "MapTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", npcs=" + (npcs == null ? "null" : npcs.size()) +
                ", vgos=" + (vgos == null ? "null" : vgos.size()) +
                ", max_zone=" + max_zone +
                ", max_player=" + max_player +
                ", maxW=" + maxW +
                ", maxH=" + maxH +
                ", type_view_p=" + type_view_p +
                ", b=" + b +
                ", specMap=" + specMap +
                ", id_eff_map=" + id_eff_map +
                ", level=" + level +
                ", typeChangeMap=" + typeChangeMap +
                ", strTimeChange='" + strTimeChange + '\'' +
                '}';
    }
}