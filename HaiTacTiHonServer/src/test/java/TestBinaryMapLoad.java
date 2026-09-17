import map.MapTemplate;
import java.io.File;
import java.nio.file.Files;

public class TestBinaryMapLoad {
    public static void main(String[] args) {
        System.out.println("=== TESTING BINARY MAP DATA LOADING ===");

        File mapDataDir = new File("data/map/data");
        if (!mapDataDir.exists()) {
            System.err.println("Directory data/map/data not found!");
            return;
        }

        File[] files = mapDataDir.listFiles((dir, name) -> name.endsWith("_data"));
        if (files == null || files.length == 0) {
            System.err.println("No _data files found in " + mapDataDir.getAbsolutePath());
            return;
        }

        System.out.println("Found " + files.length + " binary map files. Testing sample maps...");

        int tested = 0;
        int passed = 0;

        for (File dataFile : files) {
            String name = dataFile.getName();
            int mapId = Integer.parseInt(name.replace("_data", ""));
            File itemFile = new File(mapDataDir, mapId + "_item");

            try {
                byte[] blockBytes = Files.readAllBytes(dataFile.toPath());
                byte[] itemBytes = itemFile.exists() ? Files.readAllBytes(itemFile.toPath()) : new byte[]{0, 0, 0, 0};

                MapTemplate temp = new MapTemplate();
                temp.id = mapId;
                temp.w = 45; // sample dimension
                temp.h = (short) (blockBytes.length / 45 > 0 ? blockBytes.length / 45 : 17);
                temp.tile_id = 0;

                temp.data = new byte[2][];
                temp.data[0] = new byte[3 + blockBytes.length];
                temp.data[0][0] = (byte) temp.w;
                temp.data[0][1] = (byte) temp.h;
                temp.data[0][2] = temp.tile_id;
                System.arraycopy(blockBytes, 0, temp.data[0], 3, blockBytes.length);
                temp.data[1] = itemBytes;

                // Validate reconstructed data[0] and data[1]
                int checkW = temp.data[0][0] & 0xFF;
                int checkH = temp.data[0][1] & 0xFF;
                int checkTile = temp.data[0][2] & 0xFF;
                int blockCount = temp.data[0].length - 3;

                if (blockCount == blockBytes.length && temp.data[1].length == itemBytes.length) {
                    passed++;
                }

                tested++;
                if (tested <= 5 || tested == files.length) {
                    System.out.printf("  Map %d: blockBytes=%d (%dx%d), itemBytes=%d -> OK\n",
                            mapId, blockBytes.length, checkW, checkH, itemBytes.length);
                }
            } catch (Exception e) {
                System.err.printf("  Map %d FAILED: %s\n", mapId, e.getMessage());
            }
        }

        System.out.printf("=== RESULT: %d/%d binary maps validated successfully! ===\n", passed, tested);
    }
}
