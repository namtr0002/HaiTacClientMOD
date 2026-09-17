package map;

public class MapCanGoTo {
	public final static int[] idQuest =
                        //new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			                  new int[] {0, 24, 49, 71, 99, 119, 141, 160, 190, 210, 242, 253};
	public final static int[] idMap = new int[] {9, 17, 25, 33, 41,  49,  69,  79,  93, 107, 113, 198};
        
    public final static int[] idMapPb = new int[] {119, 120, 121, 122, 123};

    public static boolean isVillageMap(int mapId) {
        return mapId == 1 || mapId == 9 || mapId == 17 || mapId == 25 || mapId == 33 
            || mapId == 41 || mapId == 49 || mapId == 69 || mapId == 83 || mapId == 93 
            || mapId == 113 || mapId == 191;
    }

    public static int getRequiredQuestForBossMap(int mapId) {
        switch (mapId) {
            case 5: return 20;
            case 13: return 45;
            case 21: return 67;
            case 29: return 93;
            case 37: return 117;
            case 45: return 139;
            case 53: return 156;
            case 73: return 186;
            case 87: return 208;
            case 101: return 235;
            case 102: return 238;
            case 126: return 277;
            case 127: return 280;
            case 198: return 283;
            default: return 0;
        }
    }

    public static boolean isDoingBossMainQuest(int mapId, int questId) {
        switch (mapId) {
            case 5: return questId == 20 || questId == 21;
            case 13: return questId == 45 || questId == 46;
            case 21: return questId == 67 || questId == 68;
            case 29: return questId == 93 || questId == 94;
            case 37: return questId == 117 || questId == 118;
            case 45: return questId == 139 || questId == 140;
            case 53: return questId == 156 || questId == 157;
            case 73: return questId == 186 || questId == 187;
            case 87: return questId == 208 || questId == 209;
            case 101: return questId == 235 || questId == 236;
            case 102: return questId == 238 || questId == 239;
            case 126: return questId == 277 || questId == 278;
            case 127: return questId == 280 || questId == 281;
            default: return false;
        }
    }

    public static boolean isMapHaveRepeatQuest(int mapId) {
        // Danh sách các map quái có nhiệm vụ lặp theo từng làng
        switch (mapId) {
            case 2: case 3: case 4:
            case 10: case 11: case 12:
            case 18: case 19: case 20:
            case 26: case 27: case 28:
            case 34: case 35: case 36:
            case 42: case 43: case 44:
            case 50: case 51: case 52:
            case 70: case 71: case 72:
            case 84: case 85: case 86:
            case 94: case 95: case 96: case 97: case 98: case 99: case 100:
            case 112: case 115: case 116: case 117: case 118: case 124: case 125:
            case 192: case 193: case 194: case 195: case 196: case 197:
                return true;
            default:
                return false;
        }
    }
}
