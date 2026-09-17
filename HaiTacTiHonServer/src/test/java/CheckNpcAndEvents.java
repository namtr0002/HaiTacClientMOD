import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import database.DbManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import event.*;

public class CheckNpcAndEvents {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("=== FULL AUDIT OF ALL EVENT NPCS & MAP POSITIONS ===");
        System.out.println("==================================================");

        try {
            core.Manager.gI().load_config();
        } catch (Exception e) {
            System.err.println("Config load error: " + e.getMessage());
            return;
        }

        Map<Integer, String> mapNames = new HashMap<>();
        Map<Integer, List<NpcInfo>> mapNpcs = new HashMap<>();

        try (Connection conn = DbManager.gI().getConnect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, npcs FROM maps;")) {

            while (rs.next()) {
                int idMap = rs.getInt("id");
                String name = rs.getString("name");
                mapNames.put(idMap, name);

                List<NpcInfo> npcList = new ArrayList<>();
                String npcsJson = rs.getString("npcs");
                if (npcsJson != null && !npcsJson.isEmpty()) {
                    try {
                        JSONArray js_npc = (JSONArray) JSONValue.parse(npcsJson);
                        if (js_npc != null) {
                            for (int i = 0; i < js_npc.size(); i++) {
                                JSONArray js_temp = (JSONArray) JSONValue.parse(js_npc.get(i).toString());
                                short idmenu = Short.parseShort(js_temp.get(0).toString());
                                String npcName = js_temp.get(1).toString();
                                short x = Short.parseShort(js_temp.get(4).toString());
                                short y = Short.parseShort(js_temp.get(5).toString());
                                byte wBlock = Byte.parseByte(js_temp.get(8).toString());
                                byte hBlock = Byte.parseByte(js_temp.get(9).toString());
                                npcList.add(new NpcInfo(idmenu, npcName, idMap, name, x, y, wBlock, hBlock, "DB Map"));
                            }
                        }
                    } catch (Exception e) {}
                }
                mapNpcs.put(idMap, npcList);
                for (NpcInfo n : npcList) {
                    System.out.println(String.format("Map %d (%s) -> NPC idmenu=%d, name='%s', pos=(%d,%d)", idMap, name, n.idmenu, n.name, n.x, n.y));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = DbManager.gI().getConnect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM store_data;")) {
            System.out.println("\n--- STORE DATA TABLE ---");
            while (rs.next()) {
                int storeId = rs.getInt("store_id");
                if (storeId == 6 || storeId == 20) {
                    System.out.println(String.format("store_id=%d, item_id=%d, cat=%d, price_coin=%d, price_ruby=%d, req_lv=%d, ev_id=%d, type2=%s",
                            storeId, rs.getInt("item_id"), rs.getInt("item_category"), rs.getInt("price_coin"), rs.getInt("price_ruby"),
                            rs.getInt("req_level"), rs.getInt("event_id"), rs.getString("type2")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    static class NpcInfo {
        short idmenu;
        String name;
        int mapId;
        String mapName;
        short x;
        short y;
        byte wBlock;
        byte hBlock;
        String source;

        public NpcInfo(short idmenu, String name, int mapId, String mapName, short x, short y, byte wBlock, byte hBlock, String source) {
            this.idmenu = idmenu;
            this.name = name;
            this.mapId = mapId;
            this.mapName = mapName;
            this.x = x;
            this.y = y;
            this.wBlock = wBlock;
            this.hBlock = hBlock;
            this.source = source;
        }
    }
}
