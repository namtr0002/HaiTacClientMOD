import com.deplor.haitactihontool.map.GameMap;
import com.deplor.haitactihontool.map.GameMapCanvas;
import com.deplor.haitactihontool.map.MapDataLoader;
import com.deplor.haitactihontool.map.SQLMapLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class TestSaveExportDialog {
    public static void main(String[] args) {
        System.out.println("Testing SQLMapLoader and GameMapCanvas...");
        SQLMapLoader sqlLoader = new SQLMapLoader();
        List<GameMap> maps = sqlLoader.loadAllMaps("");
        System.out.println("Total maps loaded: " + maps.size());
        if (maps.isEmpty()) {
            throw new RuntimeException("No maps loaded!");
        }

        GameMap map0 = maps.get(0);
        System.out.println("Map 0: id=" + map0.id + ", name=" + map0.name + ", w=" + map0.width + ", h=" + map0.height);
        System.out.println("Map 0 mapPaint length: " + (map0.mapPaint != null ? map0.mapPaint.length : "null"));
        System.out.println("Map 0 items count: " + map0.items.size());
        System.out.println("Tile (0,0): " + map0.mapPaint[0] + ", Tile(1,0): " + map0.mapPaint[1] + ", TileSet: " + map0.getTileSetId() + ", IDBack: " + map0.IDBack);

        if (map0.mapPaint == null || map0.mapPaint.length == 0) {
            throw new RuntimeException("Map 0 mapPaint is null or empty!");
        }

        System.out.println("Testing GameMapCanvas setMap and painting...");
        GameMapCanvas canvas = new GameMapCanvas();
        canvas.setMap(map0);
        canvas.setSize(800, 600);

        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        canvas.paint(g2);
        g2.dispose();

        System.out.println("Testing MapDataLoader cache clear and reload...");
        MapDataLoader.clearCache();
        List<GameMap> mapsViaLoader = MapDataLoader.loadAllMaps("");
        System.out.println("Maps via MapDataLoader: " + mapsViaLoader.size());
        GameMap first = mapsViaLoader.get(0);
        if (first.mapPaint == null || first.mapPaint.length == 0) {
            throw new RuntimeException("First map mapPaint is null or empty via MapDataLoader!");
        }

        System.out.println("TEST COMPLETED WITH SUCCESS!");
    }
}

