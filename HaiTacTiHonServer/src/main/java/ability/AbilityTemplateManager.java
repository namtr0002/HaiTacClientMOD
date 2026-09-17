package ability;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import database.CacheFileCodec;

public class AbilityTemplateManager {
    private static final AbilityTemplateManager instance = new AbilityTemplateManager();
    public static AbilityTemplateManager gI() {
        return instance;
    }

    private static final String CACHE_DIR = "./.cache";
    private static final int PAGE_SIZE = 10000;
    private static final byte XOR_KEY = 0x5C;

    // LRU Cache for memory pages
    private final int MAX_MEM_PAGES = 20;
    private final Map<String, long[]> memoryCache = new LinkedHashMap<String, long[]>(MAX_MEM_PAGES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, long[]> eldest) {
            return size() > MAX_MEM_PAGES;
        }
    };

    private boolean initialized = false;

    public synchronized void init() {
        if (initialized) return;
        initialized = true; // Set to true immediately to prevent recursion during warm-up test calculations
        try {
            File dir = new File(CACHE_DIR);
            if (dir.exists()) {
                // Clear formula cache on startup so values are recomputed fresh each run
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isFile() && (f.getName().startsWith("atk_")
                                || f.getName().startsWith("def_")
                                || f.getName().startsWith("mp_"))) {
                            f.delete();
                        }
                    }
                }
            } else {
                dir.mkdirs();
            }
            core.Log.info("AbilityTemplate", "Initialized cache directory: " + CACHE_DIR);
            
            // Warm-up test calculations
            long testAtk1 = getPoint1Atk(1);
            long testAtk20 = getPoint1Atk(20);
            long testAtk600 = getPoint1Atk(600);
            //core.Log.success("AbilityTemplate", String.format("Baseline calculations verified (pt 1=%d, pt 20=%d, pt 600=%d)", testAtk1, testAtk20, testAtk600));
        } catch (Exception e) {
            initialized = false; // reset if initialization failed
            e.printStackTrace();
        }
    }

    // Direct O(1) formulas
    public long getPoint1Crit(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : i - 9;
    }

    public long getPoint1Pierce(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 2L * i - 28;
    }

    public long getPoint2ResistMagic(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 5L * i - 85;
    }

    public long getPoint2ResistPhysical(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 5L * i - 85;
    }

    public long getPoint3Hp(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return 1001L + 5L * i * (i + 2);
    }

    public long getPoint3HpPotion(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 12L * i - 216;
    }

    public long getPoint4DameCrit(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 50L * i - 900;
    }

    public long getPoint4ReactDame(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 2L * i - 28;
    }

    public long getPoint5Miss(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return i < 19 ? 0 : 2L * i - 28;
    }

    public long getPoint5Cooldown(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        return 15L + (i * 285L) / 79L;
    }

    public long getTemplateValue(int optionId, long point) {
        if (point <= 0) return 0;
        switch (optionId) {
            case 1:
                return getPoint1Atk(point);
            case 10:
                return getPoint1Crit(point);
            case 13:
                return getPoint1Pierce(point);
            case 4:
                return getPoint2Def(point);
            case 26:
                return getPoint2ResistPhysical(point);
            case 27:
                return getPoint2ResistMagic(point);
            case 15:
                return getPoint3Hp(point);
            case 23:
                return getPoint3HpPotion(point);
            case 11:
                return getPoint4DameCrit(point);
            case 14:
                return getPoint4ReactDame(point);
            case 16:
                return getPoint4Mp(point);
            case 12:
                return getPoint5Miss(point);
            case 25:
                return getPoint5Cooldown(point);
            default:
                return 0;
        }
    }

    // Cumulative templates with Page Cache (SSD + RAM)
    public long getPoint1Atk(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        long pageNum = i / PAGE_SIZE;
        int offset = (int) (i % PAGE_SIZE);
        long[] page = getPage("atk", pageNum);
        return page[offset];
    }

    public long getPoint2Def(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        long pageNum = i / PAGE_SIZE;
        int offset = (int) (i % PAGE_SIZE);
        long[] page = getPage("def", pageNum);
        return page[offset];
    }

    public long getPoint4Mp(long point) {
        if (point <= 0) return 0;
        long i = point - 1;
        long pageNum = i / PAGE_SIZE;
        int offset = (int) (i % PAGE_SIZE);
        long[] page = getPage("mp", pageNum);
        return page[offset];
    }

    private synchronized long[] getPage(String type, long pageNum) {
        // Ensure initialized
        init();
        
        String key = type + "_" + pageNum;
        long[] page = memoryCache.get(key);
        if (page != null) {
            return page;
        }

        // Try load from SSD
        File file = new File(CACHE_DIR, key + ".dat");
        if (file.exists()) {
            try {
                page = CacheFileCodec.loadLongArray(file, PAGE_SIZE, XOR_KEY);
                memoryCache.put(key, page);
                return page;
            } catch (Exception e) {
                System.err.println("[AbilityTemplateManager] Error reading cache file for " + key + ", recalculating...");
                e.printStackTrace();
            }
        }

        // Generate page
        page = generatePage(type, pageNum);
        try {
            CacheFileCodec.saveLongArray(file, page, XOR_KEY);
        } catch (Exception e) {
            System.err.println("[AbilityTemplateManager] Error writing cache file for " + key);
            e.printStackTrace();
        }
        memoryCache.put(key, page);
        return page;
    }

    private long[] generatePage(String type, long pageNum) {
        long startIdx = pageNum * PAGE_SIZE;
        long endIdx = startIdx + PAGE_SIZE - 1;
        long[] pageData = new long[PAGE_SIZE];

        if ("atk".equals(type)) {
            long val = 44;
            long par_add = 27;
            int[] add_per_level = {2, 4, 2, 2, 4};
            int addIdx = 0;
            if (startIdx == 0) {
                pageData[0] = val;
            }
            for (long i = 1; i <= endIdx; i++) {
                if (i == 1) {
                    val += par_add;
                } else {
                    par_add += add_per_level[addIdx++];
                    if (addIdx >= add_per_level.length) {
                        addIdx = 0;
                    }
                    val += par_add;
                }
                if (i >= startIdx) {
                    pageData[(int) (i - startIdx)] = val;
                }
            }
        } else if ("def".equals(type)) {
            long val = 140;
            long par_add = 89;
            int change = 3;
            if (startIdx == 0) {
                pageData[0] = val;
            }
            for (long i = 1; i <= endIdx; i++) {
                if (i == 1) {
                    val = (val * 10 + par_add) / 10;
                } else {
                    long offset = i - 2;
                    if ((offset + 1) % 10 == 0) {
                        change = 2;
                    }
                    if (offset % change == 0) {
                        par_add += 89;
                        change = 3;
                    }
                    val = (val * 10 + par_add) / 10;
                }
                if (i >= startIdx) {
                    pageData[(int) (i - startIdx)] = val;
                }
            }
        } else if ("mp".equals(type)) {
            long val = 20;
            long par_add = 1;
            if (startIdx == 0) {
                pageData[0] = val;
            }
            for (long i = 1; i <= endIdx; i++) {
                if (i == 1) {
                    val += par_add;
                } else {
                    long offset = i - 2;
                    if (offset % 2 == 0) {
                        par_add += 2;
                    }
                    val += par_add;
                }
                if (i >= startIdx) {
                    pageData[(int) (i - startIdx)] = val;
                }
            }
        }
        return pageData;
    }

}
