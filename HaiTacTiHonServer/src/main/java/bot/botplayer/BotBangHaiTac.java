package bot.botplayer;

import clan.Clan;
import clan.ClanMember;
import core.ZUtil;
import core.Log;
import database.DbManager;
import template.ItemBag47;
import template.EffTemplate;
import rank.TopClan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotBangHaiTac — Hệ thống Băng Hải Tặc Bot chuẩn xịn toàn diện từ A-Z.
 *
 * Tính năng chính:
 *  1. Khởi tạo & duy trì hệ sinh thái 16-24 Băng Hải Tặc Bot đa tầng (Tứ Hoàng, Siêu Tân Tinh, Chiến Binh, Tân Binh).
 *  2. Khắc phục triệt để lỗi Clan level 0 / Member level 0 ở mọi khâu (khởi tạo, nạp DB, đồng bộ, hiển thị).
 *  3. Cơ cấu nhân sự chuẩn xịn: Thuyền trưởng, Thuyền phó, Hoa tiêu, Thuyền viên đầy đủ phái, trang phục, cống hiến.
 *  4. Phân bổ điểm tiềm năng bang (opAttri: Công, Thủ, HP, Crit, EXP), kho item (list_it), và bùa buff (buff).
 *  5. Tự động cày cấp, đóng góp EXP/Beri/Ruby, tự thăng cấp bang, kích hoạt bùa buff, và tự xét duyệt người chơi thật xin vào bang.
 *  6. Đồng bộ liền mạch với Bảng Xếp Hạng Top Clan (TopClan) và các minigame bang hội.
 */
public class BotBangHaiTac {

    public static final int MAX_BOTS_PER_CLAN = 10;
    public static final int MIN_LEADER_LEVEL = 20;
    public static final int TARGET_BOT_CLANS = 20;

    /** Map clanName -> tên leader */
    public static final ConcurrentHashMap<String, String> BOT_CLAN_LEADERS = new ConcurrentHashMap<>();

    /** Set danh sách tên các clan bot đang hoạt động */
    public static final Set<String> BOT_CLAN_NAMES_SET = ConcurrentHashMap.newKeySet();

    // ========================= INIT & SEEDING =========================

    /**
     * Khởi tạo hệ sinh thái Băng Hải Tặc Bot.
     * Gọi từ BotPlayerManager.init() khi server khởi động.
     */
    public static void initBotClans() {
        BOT_CLAN_NAMES_SET.clear();
        BOT_CLAN_LEADERS.clear();

        // 1. Quét và chuẩn hóa toàn bộ Clan hiện có trong Clan.ENTRY
        if (Clan.ENTRY != null) {
            for (Clan clan : Clan.ENTRY) {
                if (clan == null) continue;
                sanitizeClan(clan);
                if (clan.members != null && !clan.members.isEmpty()) {
                    String leaderName = clan.members.get(0).name;
                    if (isBotName(leaderName) || isKnownBotClan(clan.name)) {
                        BOT_CLAN_NAMES_SET.add(clan.name);
                        BOT_CLAN_LEADERS.put(clan.name, leaderName);
                    }
                }
            }
        }

        // 2. Nếu số lượng clan bot chưa đủ mục tiêu (TARGET_BOT_CLANS), sinh bổ sung các clan phong phú
        if (BOT_CLAN_NAMES_SET.size() < TARGET_BOT_CLANS) {
            seedBotClansWorld();
        }

        // 3. Cập nhật lại Bảng xếp hạng Top Clan
        TopClan.gI().update();

        Log.success("BotClan", "Bot Clan Ecosystem Initialized: " + BOT_CLAN_NAMES_SET.size() + " active pirate clans.");
    }

    /**
     * Chuẩn hóa Clan để không bao giờ bị level 0 hoặc dữ liệu rỗng.
     */
    public static void sanitizeClan(Clan clan) {
        if (clan == null) return;
        if (clan.level <= 0) clan.level = 1;
        if (clan.icon <= 0) clan.icon = 1;
        if (clan.maxAttri < 20) {
            clan.maxAttri = (short) (20 + clan.trungsinh * 5);
        }
        if (clan.opAttri == null || clan.opAttri.length < 5) {
            clan.opAttri = new short[]{0, 0, 0, 0, 0};
        }
        if (clan.thongbao == null || clan.thongbao.trim().isEmpty()) {
            clan.thongbao = BotNameGenerator.getRandomClanNotice();
        }
        if (clan.members == null) {
            clan.members = new ArrayList<>();
        } else {
            for (ClanMember mem : clan.members) {
                if (mem != null) {
                    if (mem.level <= 0) {
                        mem.level = (short) Math.max(1, clan.level * 6 + ZUtil.random(5, 15));
                    }
                    if (mem.clazz <= 0 || mem.clazz > 5) {
                        mem.clazz = (byte) ZUtil.random(1, 5);
                    }
                }
            }
        }
    }

    /**
     * Tự động sinh thế giới các Băng Hải Tặc Bot đa tầng với các chỉ số, cấp độ và thành viên chuẩn xịn.
     */
    private static synchronized void seedBotClansWorld() {
        // Cấu hình 4 phân tầng Băng Hải Tặc
        BotClanTierConfig[] tierConfigs = {
            // Tier 1: Tứ Hoàng / Băng Huyền Thoại (Yonko - 4 clans)
            new BotClanTierConfig(
                new String[]{"Băng Râu Trắng", "Băng Tóc Đỏ", "Băng Bách Thú", "Băng Big Mom"},
                new short[]{500, 513, 516, 520},
                (short) 14, (short) 15, // level
                (byte) 4, (byte) 6,     // trung sinh
                50,                      // maxAttri
                new short[]{18, 16, 12, 2, 2}, // opAttri
                100_000_000, 200_000_000,      // beri
                20_000, 50_000,                // ruby
                8, 12,                         // members count
                90, 105                        // member levels
            ),
            // Tier 2: Siêu Tân Tinh (Supernovas - 6 clans)
            new BotClanTierConfig(
                new String[]{"Băng Trái Tim", "Băng Kid", "Băng Hỏa Quyền", "Băng Mặt Trời", "Băng Quỷ Cốc", "Băng Hắc Long"},
                new short[]{320, 322, 324, 326, 330, 342},
                (short) 9, (short) 12,
                (byte) 2, (byte) 3,
                35,
                new short[]{10, 10, 8, 4, 3},
                25_000_000, 60_000_000,
                5_000, 15_000,
                6, 9,
                68, 88
            ),
            // Tier 3: Chiến Binh Biển Cả (Veterans - 6 clans)
            new BotClanTierConfig(
                new String[]{"Băng Bão Biển", "Băng Sóng Thần", "Băng Thần Kiếm", "Băng Viễn Đông", "Băng Hải Long", "Băng Sấm Sét"},
                new short[]{206, 224, 275, 294, 318, 319},
                (short) 5, (short) 8,
                (byte) 0, (byte) 1,
                25,
                new short[]{6, 6, 6, 4, 3},
                5_000_000, 15_000_000,
                1_000, 4_000,
                5, 7,
                45, 65
            ),
            // Tier 4: Tân Binh Đại Dương (Rookies - 4 clans)
            new BotClanTierConfig(
                new String[]{"Băng Tân Binh", "Băng Khởi Đầu", "Băng Thủy Thủ", "Băng Biển Đông"},
                new short[]{7, 9, 20, 24},
                (short) 2, (short) 4,
                (byte) 0, (byte) 0,
                20,
                new short[]{2, 2, 2, 1, 1},
                1_000_000, 4_000_000,
                200, 800,
                4, 5,
                25, 42
            )
        };

        int maxId = 0;
        if (Clan.ENTRY != null) {
            for (Clan c : Clan.ENTRY) {
                if (c != null && c.id > maxId) maxId = c.id;
            }
        }

        for (BotClanTierConfig cfg : tierConfigs) {
            for (int i = 0; i < cfg.clanNames.length; i++) {
                String clanName = cfg.clanNames[i];
                if (BOT_CLAN_NAMES_SET.contains(clanName) || Clan.get_clan_by_name(clanName) != null) {
                    continue; // Đã có clan này
                }

                maxId++;
                short icon = (i < cfg.icons.length) ? cfg.icons[i] : (short) ZUtil.random(1, 20);
                short level = (short) ZUtil.random(cfg.minLevel, cfg.maxLevel);
                byte trungsinh = (byte) ZUtil.random(cfg.minTrungSinh, cfg.maxTrungSinh);
                int beri = ZUtil.random(cfg.minBeri, cfg.maxBeri);
                int ruby = ZUtil.random(cfg.minRuby, cfg.maxRuby);
                int memCount = ZUtil.random(cfg.minMembers, cfg.maxMembers);

                Clan clan = createRichBotClan(maxId, clanName, icon, level, trungsinh, cfg.maxAttri, cfg.opAttri, beri, ruby, memCount, cfg.minMemLevel, cfg.maxMemLevel);
                if (clan != null) {
                    Clan.create_new_clan(clan);
                    BOT_CLAN_NAMES_SET.add(clan.name);
                    if (clan.members != null && !clan.members.isEmpty()) {
                        BOT_CLAN_LEADERS.put(clan.name, clan.members.get(0).name);
                    }
                }
            }
        }
    }

    /**
     * Xây dựng đối tượng Clan hoàn chỉnh với thuộc tính, kho item, bùa buff và thành viên.
     */
    public static Clan createRichBotClan(int id, String name, short icon, short level, byte trungsinh, short maxAttri, short[] opAttri, int beri, int ruby, int memCount, int minMemLv, int maxMemLv) {
        Clan clan = new Clan();
        clan.id = (short) id;
        clan.name = name;
        clan.icon = icon;
        clan.level = (short) Math.max(1, level);
        clan.trungsinh = trungsinh;
        clan.maxAttri = maxAttri;
        clan.pointAttri = (short) ZUtil.random(0, 5);
        clan.opAttri = new short[5];
        for (int i = 0; i < 5; i++) {
            clan.opAttri[i] = (opAttri != null && i < opAttri.length) ? opAttri[i] : 0;
        }
        clan.xp = ZUtil.random(1000, Clan.get_xp_max(clan.level, clan.trungsinh) - 500);
        clan.beri = beri;
        clan.ruby = ruby;
        clan.countAction = ZUtil.random(5, 50);
        clan.allowRequest = 1;
        clan.thongbao = BotNameGenerator.getRandomClanNotice();

        // Kho item Bang
        clan.list_it = new ArrayList<>();
        clan.list_it.add(new ItemBag47((short) 0, (short) ZUtil.random(5, 50)));  // Bùa exp 1
        clan.list_it.add(new ItemBag47((short) 1, (short) ZUtil.random(5, 30)));  // Bùa exp 2
        clan.list_it.add(new ItemBag47((short) 2, (short) ZUtil.random(10, 50))); // Bùa HP
        clan.list_it.add(new ItemBag47((short) 3, (short) ZUtil.random(10, 50))); // Bùa MP
        clan.list_it.add(new ItemBag47((short) 6, (short) ZUtil.random(50, 300))); // Túi Beri
        clan.list_it.add(new ItemBag47((short) 7, (short) ZUtil.random(5, 20)));  // Tẩy tiềm năng
        clan.list_it.add(new ItemBag47((short) 8, (short) ZUtil.random(2, 10)));  // Vé PvP Băng
        clan.list_it.add(new ItemBag47((short) 17, (short) ZUtil.random(10, 50))); // Đá hành trình 1
        clan.list_it.add(new ItemBag47((short) 18, (short) ZUtil.random(5, 30)));  // Đá hành trình 2

        // Bùa buff Bang (Bùa EXP & Bùa HP đang hoạt động)
        clan.buff = new CopyOnWriteArrayList<>();
        clan.buff.add(new EffTemplate((short) 0, (short) 100, System.currentTimeMillis() + 86400000L * 7)); // Bùa EXP 100%
        if (level >= 5) {
            clan.buff.add(new EffTemplate((short) 1, (short) 100, System.currentTimeMillis() + 86400000L * 7)); // Bùa HP 100%
        }
        if (level >= 10) {
            clan.buff.add(new EffTemplate((short) 4, (short) 25, System.currentTimeMillis() + 86400000L * 7));  // Bùa Tổng hợp
        }

        clan.hanhtrinh = new ArrayList<>();
        clan.hanhtrinh.add(0);

        // Sinh danh sách thành viên với cơ cấu chức vụ chuẩn
        clan.members = new ArrayList<>();
        generateClanMembers(clan, memCount, minMemLv, maxMemLv);

        return clan;
    }

    /**
     * Sinh danh sách thành viên cho Clan:
     * - 1 Thuyền trưởng (Role 0, cấp cao nhất)
     * - 1-2 Thuyền phó (Role 1)
     * - 1 Hoa tiêu (Role 2)
     * - Các thuyền viên còn lại (Role 10)
     * - Đầy đủ 5 class, không class nào vượt quá 4
     */
    private static void generateClanMembers(Clan clan, int totalMembers, int minLv, int maxLv) {
        int[] classCounts = new int[6];
        int captainLv = maxLv;

        // 1. Thuyền trưởng
        byte captainClass = (byte) ZUtil.random(1, 5);
        classCounts[captainClass]++;
        ClanMember captain = new ClanMember();
        captain.id = 0;
        captain.name = generateCaptainName(clan.name);
        captain.level = (short) captainLv;
        captain.levelInclan = 0; // Thuyền trưởng
        captain.donate = (short) ZUtil.random(50, 200);
        captain.gopRuby = (short) 32000;
        captain.numquest = 3;
        captain.conghien = ZUtil.random(100_000, 2_000_000);
        captain.clazz = captainClass;
        setMemberAppearance(captain, captainClass, captain.level);
        captain.timeJoinClan = System.currentTimeMillis() - 86400000L * 30;
        clan.members.add(captain);

        int remaining = totalMembers - 1;

        // 2. Thuyền phó (1 - 2 người)
        int numPho = (remaining >= 4) ? 2 : 1;
        for (int p = 0; p < numPho && remaining > 0; p++) {
            byte phoClass = getBalancedClass(classCounts);
            classCounts[phoClass]++;
            ClanMember pho = new ClanMember();
            pho.id = (short) clan.members.size();
            pho.name = BotNameGenerator.getRandomBotName();
            pho.level = (short) Math.max(minLv, captainLv - ZUtil.random(2, 5));
            pho.levelInclan = 1; // Thuyền phó
            pho.donate = (short) ZUtil.random(30, 100);
            pho.gopRuby = (short) ZUtil.random(10000, 32000);
            pho.numquest = (short) ZUtil.random(1, 3);
            pho.conghien = ZUtil.random(50_000, 800_000);
            pho.clazz = phoClass;
            setMemberAppearance(pho, phoClass, pho.level);
            pho.timeJoinClan = System.currentTimeMillis() - 86400000L * 20;
            clan.members.add(pho);
            remaining--;
        }

        // 3. Hoa tiêu (1 người)
        if (remaining > 0) {
            byte hoaTieuClass = getBalancedClass(classCounts);
            classCounts[hoaTieuClass]++;
            ClanMember hoaTieu = new ClanMember();
            hoaTieu.id = (short) clan.members.size();
            hoaTieu.name = BotNameGenerator.getRandomBotName();
            hoaTieu.level = (short) Math.max(minLv, captainLv - ZUtil.random(4, 8));
            hoaTieu.levelInclan = 2; // Hoa tiêu
            hoaTieu.donate = (short) ZUtil.random(20, 80);
            hoaTieu.gopRuby = (short) ZUtil.random(5000, 20000);
            hoaTieu.numquest = (short) ZUtil.random(1, 3);
            hoaTieu.conghien = ZUtil.random(30_000, 500_000);
            hoaTieu.clazz = hoaTieuClass;
            setMemberAppearance(hoaTieu, hoaTieuClass, hoaTieu.level);
            hoaTieu.timeJoinClan = System.currentTimeMillis() - 86400000L * 15;
            clan.members.add(hoaTieu);
            remaining--;
        }

        // 4. Thuyền viên
        while (remaining > 0) {
            byte memClass = getBalancedClass(classCounts);
            classCounts[memClass]++;
            ClanMember mem = new ClanMember();
            mem.id = (short) clan.members.size();
            mem.name = BotNameGenerator.getRandomBotName();
            mem.level = (short) ZUtil.random(minLv, Math.max(minLv + 5, captainLv - 6));
            mem.levelInclan = 10; // Thuyền viên
            mem.donate = (short) ZUtil.random(5, 50);
            mem.gopRuby = (short) ZUtil.random(1000, 15000);
            mem.numquest = (short) ZUtil.random(0, 3);
            mem.conghien = ZUtil.random(5_000, 200_000);
            mem.clazz = memClass;
            setMemberAppearance(mem, memClass, mem.level);
            mem.timeJoinClan = System.currentTimeMillis() - 86400000L * ZUtil.random(1, 10);
            clan.members.add(mem);
            remaining--;
        }
    }

    private static byte getBalancedClass(int[] classCounts) {
        List<Byte> validClasses = new ArrayList<>();
        for (byte c = 1; c <= 5; c++) {
            if (classCounts[c] < 4) {
                validClasses.add(c);
            }
        }
        if (validClasses.isEmpty()) {
            return (byte) ZUtil.random(1, 5);
        }
        return validClasses.get(ZUtil.random(validClasses.size()));
    }

    private static void setMemberAppearance(ClanMember mem, byte clazz, short level) {
        short hair = 1;
        short head = 0;
        short hat = -1;

        switch (clazz) {
            case 1: hair = 1; head = (short) (level >= 80 ? 747 : 0); break;
            case 2: hair = 24; head = (short) (level >= 80 ? 729 : 0); break;
            case 3: hair = 28; head = (short) (level >= 80 ? 732 : 0); break;
            case 4: hair = 32; head = (short) (level >= 80 ? 731 : 0); break;
            case 5: hair = 36; head = (short) (level >= 80 ? 574 : 0); break;
            default: hair = 1; break;
        }

        if (level >= 90) {
            head = (short) (level % 2 == 0 ? 1061 : 1062);
            hat = (short) (level >= 95 ? 758 : -1);
        }

        mem.head = head;
        mem.hair = hair;
        mem.hat = hat;
    }

    private static String generateCaptainName(String clanName) {
        if (clanName.contains("Râu Trắng")) return "Râu Trắng";
        if (clanName.contains("Tóc Đỏ")) return "Shanks Tóc Đỏ";
        if (clanName.contains("Bách Thú")) return "Kaido Bách Thú";
        if (clanName.contains("Big Mom")) return "Big Mom";
        if (clanName.contains("Mũ Rơm")) return "Luffy Mũ Rơm";
        if (clanName.contains("Trái Tim")) return "Trafalgar Law";
        if (clanName.contains("Kid")) return "Eustass Kid";
        if (clanName.contains("Hỏa Quyền")) return "Ace Hỏa Quyền";
        if (clanName.contains("Quỷ Cốc")) return "Quỷ Cốc Tử";
        if (clanName.contains("Hắc Long")) return "Hắc Long Vương";
        return BotNameGenerator.getRandomBotName();
    }

    // ========================= REALTIME BOT CLAN LIFECYCLE =========================

    /**
     * Bot tạo clan mới (khi bot chưa có clan và muốn tạo).
     */
    public static boolean tryCreateClan(BotPlayerReal leader) {
        if (leader == null || leader.level < MIN_LEADER_LEVEL || leader.clan != null) return false;

        String clanName = null;
        for (int attempts = 0; attempts < 100; attempts++) {
            String candidate = BotNameGenerator.generateRandomClanName();
            if (!BOT_CLAN_NAMES_SET.contains(candidate) && Clan.get_clan_by_name(candidate) == null) {
                clanName = candidate;
                break;
            }
        }
        if (clanName == null) return false;

        int maxId = 0;
        if (Clan.ENTRY != null) {
            for (Clan c : Clan.ENTRY) {
                if (c != null && c.id > maxId) maxId = c.id;
            }
        }

        short icon = (short) ZUtil.random(1, 20);
        short level = (short) Math.max(1, leader.level / 10);
        byte trungsinh = (byte) (leader.level >= 90 ? 2 : (leader.level >= 70 ? 1 : 0));
        int beri = leader.level * 50_000;
        int ruby = leader.level * 50;

        Clan newClan = createRichBotClan(maxId + 1, clanName, icon, level, trungsinh, (short) (20 + trungsinh * 5),
                new short[]{2, 2, 2, 1, 1}, beri, ruby, ZUtil.random(4, 7), Math.max(10, leader.level - 20), leader.level);

        if (newClan != null && Clan.create_new_clan(newClan)) {
            leader.clan = newClan;
            BOT_CLAN_NAMES_SET.add(clanName);
            BOT_CLAN_LEADERS.put(clanName, leader.name);
            BotWorldAnnounce.announceClanCreated(leader.name, clanName);
            return true;
        }
        return false;
    }

    /**
     * Bot tìm và gia nhập clan bot phù hợp.
     */
    public static boolean tryJoinClan(BotPlayerReal bot) {
        if (bot == null || bot.clan != null) return false;

        for (Clan clan : Clan.ENTRY) {
            if (clan == null || clan.name == null) continue;
            if (!BOT_CLAN_NAMES_SET.contains(clan.name) && !isKnownBotClan(clan.name)) continue;
            if (clan.members == null) continue;
            if (clan.members.size() >= Clan.get_mem_max(clan.level, clan.trungsinh)) continue;

            // Kiểm tra số lượng cùng class trong clan < 4
            int sameClassCount = 0;
            for (ClanMember cm : clan.members) {
                if (cm != null && cm.clazz == bot.clazz) sameClassCount++;
            }
            if (sameClassCount >= 4) continue;

            ClanMember newMember = buildMember(bot, 10);
            synchronized (clan.members) {
                clan.members.add(newMember);
            }
            bot.clan = clan;
            return true;
        }
        return false;
    }

    /**
     * Tick chu kỳ hoạt động của Bot trong Clan:
     * - Nộp EXP / Beri / Ruby cho bang
     * - Tự nâng cấp bang khi đủ điều kiện
     * - Tự kích hoạt bùa buff
     * - Tự duyệt đơn gia nhập của người chơi thật
     */
    public static void tickClanActivity(BotPlayerReal bot) {
        if (bot == null) return;
        if (bot.clan == null) {
            if (bot.level >= MIN_LEADER_LEVEL && ZUtil.random(100) < 30) {
                if (!tryJoinClan(bot)) {
                    tryCreateClan(bot);
                }
            } else {
                tryJoinClan(bot);
            }
            return;
        }

        Clan clan = bot.clan;

        // 1. Thử tham gia phó bản
        if (bot.level >= 30) {
            tryJoinClanDungeon(bot);
        }

        // 2. Nộp EXP & Beri cho Clan
        int xpDonate = ZUtil.random(100, 400) + (bot.level * 5);
        synchronized (clan) {
            clan.xp += xpDonate;
            clan.beri += (bot.level * 100);

            // Nâng cấp Bang khi đủ EXP
            int maxExp = Clan.get_xp_max(clan.level, clan.trungsinh);
            if (clan.xp >= maxExp && clan.level < 15) {
                clan.level++;
                clan.xp = 0;
                clan.maxAttri = (short) Math.min(50, clan.maxAttri + 2);
                clan.pointAttri += 2;
                clan.thongbao = BotNameGenerator.getRandomClanNotice();
            }

            // Tự động phân bổ điểm tiềm năng bang (opAttri)
            if (clan.pointAttri > 0 && clan.opAttri != null) {
                int slot = ZUtil.random(5);
                clan.opAttri[slot]++;
                clan.pointAttri--;
            }

            // Tự động gia hạn bùa buff nếu hết hạn
            if (clan.buff == null || clan.buff.isEmpty()) {
                clan.buff = new CopyOnWriteArrayList<>();
                clan.buff.add(new EffTemplate((short) 0, (short) 100, System.currentTimeMillis() + 86400000L * 7));
            }

            // Tự động xét duyệt đơn xin vào bang của người chơi thật (Auto-accept)
            if (clan.mem_request != null && !clan.mem_request.isEmpty()) {
                for (Iterator<ClanMember> it = clan.mem_request.iterator(); it.hasNext(); ) {
                    ClanMember req = it.next();
                    if (req != null && clan.members.size() < Clan.get_mem_max(clan.level, clan.trungsinh)) {
                        req.levelInclan = 10;
                        clan.members.add(req);
                        it.remove();
                    }
                }
            }
        }

        // Cập nhật thông tin level của bot trong clan
        updateMemberInfo(bot, clan);
    }

    /**
     * Tự động đăng ký hoặc tham gia phó bản bang (Trận Chiến Khổng Lồ hoặc PvP Băng).
     */
    public static boolean tryJoinClanDungeon(BotPlayerReal bot) {
        if (bot == null || bot.clan == null) return false;

        if (bot.clan.map_create != null) {
            map.Zone dungeonZone = bot.clan.map_create;
            if (dungeonZone.map_little_garden != null && !dungeonZone.map_little_garden.is_finish) {
                if (bot.map != dungeonZone) {
                    try {
                        bot.type_pk = (byte) (bot.clan.equals(dungeonZone.map_little_garden.clan1) ? 4 : 5);
                        dungeonZone.map_little_garden.join(bot);
                        bot.state = "DUNGEON";
                        return true;
                    } catch (Exception ignored) {}
                }
            } else if (dungeonZone.pvpBang != null) {
                if (bot.map != dungeonZone) {
                    try {
                        dungeonZone.pvpBang.join(bot);
                        bot.state = "DUNGEON";
                        return true;
                    } catch (Exception ignored) {}
                }
            }
        }
        return false;
    }

    private static void updateMemberInfo(BotPlayerReal bot, Clan clan) {
        if (clan == null || clan.members == null) return;
        for (ClanMember mem : clan.members) {
            if (mem != null && mem.name != null && mem.name.equals(bot.name)) {
                mem.level = (short) Math.max(1, bot.level);
                mem.head = (short) bot.get_head();
                mem.hair = (short) bot.get_hair();
                mem.hat = bot.get_hat();
                return;
            }
        }
    }

    public static ClanMember buildMember(BotPlayerReal bot, int role) {
        ClanMember mem = new ClanMember();
        mem.name = bot.name;
        mem.level = (short) Math.max(1, bot.level);
        mem.levelInclan = (byte) role;
        mem.donate = (short) ZUtil.random(10, 50);
        mem.gopRuby = (short) ZUtil.random(1000, 5000);
        mem.numquest = (short) ZUtil.random(1, 3);
        mem.conghien = ZUtil.random(5_000, 50_000);
        mem.head = (short) bot.get_head();
        mem.hair = (short) bot.get_hair();
        mem.hat = bot.get_hat();
        mem.clazz = (byte) bot.clazz;
        mem.id = (short) (bot.IDPlayer & 0xFFFF);
        mem.timeJoinClan = System.currentTimeMillis();
        return mem;
    }

    public static boolean isBotName(String name) {
        if (name == null) return false;
        return BotDb.getIds(name) != null;
    }

    public static boolean isKnownBotClan(String name) {
        if (name == null) return false;
        String[] KNOWN = {
            "Băng Râu Trắng", "Băng Tóc Đỏ", "Băng Bách Thú", "Băng Big Mom", "Băng Mũ Rơm",
            "Băng Trái Tim", "Băng Kid", "Băng Hỏa Quyền", "Băng Mặt Trời", "Băng Quỷ Cốc", "Băng Hắc Long",
            "Băng Bão Biển", "Băng Sóng Thần", "Băng Thần Kiếm", "Băng Viễn Đông", "Băng Hải Long", "Băng Sấm Sét",
            "Băng Tân Binh", "Băng Khởi Đầu", "Băng Thủy Thủ", "Băng Biển Đông"
        };
        for (String k : KNOWN) {
            if (name.contains(k) || k.contains(name)) return true;
        }
        return false;
    }

    /**
     * DTO cấu hình cho từng phân tầng Băng Hải Tặc.
     */
    private static class BotClanTierConfig {
        String[] clanNames;
        short[] icons;
        short minLevel, maxLevel;
        byte minTrungSinh, maxTrungSinh;
        short maxAttri;
        short[] opAttri;
        int minBeri, maxBeri;
        int minRuby, maxRuby;
        int minMembers, maxMembers;
        int minMemLevel, maxMemLevel;

        BotClanTierConfig(String[] clanNames, short[] icons, short minLevel, short maxLevel, byte minTrungSinh, byte maxTrungSinh,
                          int maxAttri, short[] opAttri, int minBeri, int maxBeri, int minRuby, int maxRuby,
                          int minMembers, int maxMembers, int minMemLevel, int maxMemLevel) {
            this.clanNames = clanNames;
            this.icons = icons;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.minTrungSinh = minTrungSinh;
            this.maxTrungSinh = maxTrungSinh;
            this.maxAttri = (short) maxAttri;
            this.opAttri = opAttri;
            this.minBeri = minBeri;
            this.maxBeri = maxBeri;
            this.minRuby = minRuby;
            this.maxRuby = maxRuby;
            this.minMembers = minMembers;
            this.maxMembers = maxMembers;
            this.minMemLevel = minMemLevel;
            this.maxMemLevel = maxMemLevel;
        }
    }
}
