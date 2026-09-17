package bot.botplayer;

import java.util.*;

/**
 * BotNameGenerator — Phát sinh tên ngẫu nhiên chuẩn hóa cho Bot và Clan (Băng Hải Tặc).
 * Hỗ trợ tiếng Việt có dấu, có dấu cách, tiếng Anh, đảm bảo không trùng lặp và độ dài hợp lệ (4 - 15 ký tự).
 */
public class BotNameGenerator {

    // English first words / adjectives
    private static final String[] EN_FIRST = {
        "Dark", "Fire", "Storm", "Ghost", "Shadow", "Black", "Red", "White", "Gold", "Ice",
        "Iron", "Thunder", "Wild", "Frost", "Wind", "Night", "Death", "Blood", "Soul", "Tiger",
        "Wolf", "Hawk", "Eagle", "Knight", "Silver", "Blue", "Green", "Doom", "Grim", "Mad",
        "Crazy", "Cool", "Super", "Star", "Sky", "Sun", "Moon", "Cloud", "Smoke", "Blade",
        "Sword", "Bow", "Gun", "Bullet", "Venom", "Fang", "Claw", "Fury", "Rage", "Light",
        "Deep", "Dead", "Neon", "Swift", "Sharp", "Mega", "Alpha", "Beta", "Omega", "Zero",
        "One", "Titan", "Giant", "Dragon", "Phoenix", "Devil", "Angel", "Demon", "Beast", "Hunter"
    };

    // English second words / nouns
    private static final String[] EN_SECOND = {
        "Hunter", "Rider", "Runner", "Seeker", "Slayer", "Master", "Knight", "King", "Lord", "Fighter",
        "Warrior", "Reaper", "Ghost", "Shadow", "Storm", "Wolf", "Tiger", "Hawk", "Eagle", "Dragon",
        "Blade", "Sword", "Fist", "Heart", "Soul", "Spirit", "Gamer", "Player", "Boy", "Girl",
        "Boss", "Chief", "Hero", "Legend", "Rogue", "Mage", "Priest", "Thief", "Joker", "Beast",
        "Demon", "Angel", "Devil", "Titan", "Giant", "Ninja", "Samurai", "Monk", "Pirate", "Sailor"
    };

    // English standalone/short names (to combine with suffixes)
    private static final String[] EN_SHORT = {
        "Jack", "Will", "Sam", "Ben", "Leo", "Neo", "Max", "Rex", "Tom", "Bob",
        "Ray", "Zack", "Alex", "Dan", "Roy", "Ken", "Jay", "Lee", "Jon", "Ron",
        "Sky", "Ash", "Cole", "Finn", "Kai", "Luke", "Mark", "Paul", "Ryan", "Sean"
    };

    // Vietnamese prefixes/titles/surnames
    private static final String[] VN_PREFIX = {
        "Lâm", "Trần", "Nguyễn", "Lê", "Phạm", "Huỳnh", "Hoàng", "Phan", "Vũ", "Đặng",
        "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Độc Cô", "Vô Danh", "Lão", "Đại",
        "Tiểu", "Bá", "Hùng", "Thanh", "Tử", "Bạch", "Hắc", "Thiên", "Thần", "Quỷ",
        "Ma", "Lôi", "Hỏa", "Băng", "Phong", "Vân", "Tây", "Nam", "Bắc", "Đông"
    };

    // Vietnamese main names / nouns
    private static final String[] VN_MAIN = {
        "Hải", "Long", "Phong", "Vân", "Sơn", "Thủy", "Lôi", "Hỏa", "Băng", "Vương",
        "Hoàng", "Đế", "Tặc", "Sát", "Kiếm", "Đao", "Cung", "Pháp", "Tôn", "Bại",
        "Song", "Phi", "Dực", "Hổ", "Báo", "Ưng", "Lân", "Phụng", "Dũng", "Khanh",
        "Đạt", "Huy", "Đức", "Thành", "Bảo", "Việt", "Hưng", "Linh", "Tùng", "Giang",
        "Khoa", "Trường", "Bình", "An", "Phát", "Lộc", "Tài", "Quốc", "Thịnh", "Kiệt",
        "Mạnh", "Trí", "Minh", "Thế", "Tuấn", "Kiên", "Quang", "Văn", "Anh", "Tú"
    };

    // Suffixes
    private static final String[] SUFFIXES = {
        "Pro", "Vip", "9x", "2k", "TV", "VN", "Cool", "OP", "Top", "Win", "GG",
        "S1", "S2", "Ca", "Tỷ", "Muội", "Đệ"
    };

    // Pirate-themed name components (specifically for clan names!)
    private static final String[] PIRATE_PREFIX_VN = {
        "Băng", "Hội", "Liên Minh", "Đội", "Hạm Đội", "Đoàn"
    };

    private static final String[] PIRATE_ADJ_VN = {
        "Mũ Rơm", "Tóc Đỏ", "Râu Trắng", "Râu Đen", "Bách Thú", "Mắt Diều Hâu", "Bão Biển", "Sóng Thần",
        "Hắc Long", "Bạch Hổ", "Kim Cang", "Tử Thần", "Vô Song", "Bất Bại", "Hùng Bá", "Quỷ Vương",
        "Bóng Đêm", "Ánh Sáng", "Lửa Đỏ", "Băng Giá", "Sấm Sét", "Cuồng Phong", "Đại Dương", "Viễn Đông"
    };

    private static final String[] PIRATE_ADJ_EN = {
        "Straw Hat", "Red Hair", "Whitebeard", "Blackbeard", "Beast", "Hawkeye", "Sea Storm", "Tsunami",
        "Black Dragon", "White Tiger", "Iron Fist", "Grim Reaper", "Unrivaled", "Invincible", "Overlord", "Demon King",
        "Darkness", "Light", "Wildfire", "Frostbite", "Thunder", "Cyclone", "Ocean", "Far East"
    };

    private static final String[] PIRATE_SUFFIX_EN = {
        "Crew", "Pirates", "Fleet", "Gang", "Alliance", "Bandits"
    };

    /**
     * Remove accents from a string to support unaccented names.
     */
    public static String removeAccents(String str) {
        if (str == null) return null;
        String nfdNormalizedString = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(nfdNormalizedString).replaceAll("");
        // Handle special characters like 'đ' and 'Đ'
        noAccents = noAccents.replace("đ", "d").replace("Đ", "D");
        return noAccents;
    }

    /**
     * Tạo danh sách tên độc nhất cho bot.
     * Sử dụng Random seed cố định để đảm bảo tính ổn định của tập tên qua các lần restart.
     */
    public static List<String> generateUniqueNames(int count) {
        Set<String> unique = new LinkedHashSet<>();
        Random rand = new Random(42); // Seed cố định
        int attempts = 0;

        while (unique.size() < count && attempts < 10000000) {
            attempts++;
            String firstWord = "";
            String secondWord = "";
            
            // Choose styles:
            // 0: EN + EN
            // 1: EN + VN (accent)
            // 2: EN + VN (no accent)
            // 3: VN (accent) + EN
            // 4: VN (no accent) + EN
            // 5: VN (accent) + VN (accent)
            // 6: VN (no accent) + VN (no accent)
            // 7: VN (accent) + VN (no accent)
            // 8: Short Name / Suffix Only
            int style = rand.nextInt(9);
            
            switch (style) {
                case 0:
                    firstWord = EN_FIRST[rand.nextInt(EN_FIRST.length)];
                    secondWord = EN_SECOND[rand.nextInt(EN_SECOND.length)];
                    break;
                case 1:
                    firstWord = EN_FIRST[rand.nextInt(EN_FIRST.length)];
                    secondWord = VN_MAIN[rand.nextInt(VN_MAIN.length)];
                    break;
                case 2:
                    firstWord = EN_FIRST[rand.nextInt(EN_FIRST.length)];
                    secondWord = removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                    break;
                case 3:
                    firstWord = VN_PREFIX[rand.nextInt(VN_PREFIX.length)];
                    secondWord = EN_SECOND[rand.nextInt(EN_SECOND.length)];
                    break;
                case 4:
                    firstWord = removeAccents(VN_PREFIX[rand.nextInt(VN_PREFIX.length)]);
                    secondWord = EN_SECOND[rand.nextInt(EN_SECOND.length)];
                    break;
                case 5:
                    firstWord = VN_PREFIX[rand.nextInt(VN_PREFIX.length)];
                    secondWord = VN_MAIN[rand.nextInt(VN_MAIN.length)];
                    break;
                case 6:
                    firstWord = removeAccents(VN_PREFIX[rand.nextInt(VN_PREFIX.length)]);
                    secondWord = removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                    break;
                case 7:
                    firstWord = VN_PREFIX[rand.nextInt(VN_PREFIX.length)];
                    secondWord = removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                    break;
                default: // case 8
                    if (rand.nextBoolean()) {
                        firstWord = EN_SHORT[rand.nextInt(EN_SHORT.length)];
                    } else {
                        firstWord = rand.nextBoolean() ? VN_MAIN[rand.nextInt(VN_MAIN.length)] : removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                    }
                    break;
            }

            String name = "";
            if (secondWord.isEmpty()) {
                // Single word name -> add suffix or number
                if (rand.nextBoolean()) {
                    boolean useSpace = rand.nextBoolean();
                    name = firstWord + (useSpace ? " " : "") + SUFFIXES[rand.nextInt(SUFFIXES.length)];
                } else {
                    boolean useSpace = rand.nextBoolean();
                    name = firstWord + (useSpace ? " " : "") + rand.nextInt(100);
                }
            } else {
                // Two word name -> decide space or no space
                boolean useSpaceBetweenWords = rand.nextBoolean();
                name = firstWord + (useSpaceBetweenWords ? " " : "") + secondWord;

                // 30% chance to append a suffix or number
                if (rand.nextInt(10) < 3) {
                    boolean useSpaceSuffix = rand.nextBoolean();
                    if (rand.nextBoolean()) {
                        name += (useSpaceSuffix ? " " : "") + SUFFIXES[rand.nextInt(SUFFIXES.length)];
                    } else {
                        name += (useSpaceSuffix ? " " : "") + rand.nextInt(100);
                    }
                }
            }

            if (isValidName(name)) {
                unique.add(name);
            }
        }
        return new ArrayList<>(unique);
    }

    /**
     * Tạo tên băng hải tặc (clan name) ngẫu nhiên.
     */
    public static String generateRandomClanName() {
        Random rand = new Random();
        String name = "";
        int type = rand.nextInt(3);

        if (type == 0) {
            // Vietnamese style: "Băng Mũ Rơm", "Hội Hắc Long"
            String prefix = PIRATE_PREFIX_VN[rand.nextInt(PIRATE_PREFIX_VN.length)];
            String adj = PIRATE_ADJ_VN[rand.nextInt(PIRATE_ADJ_VN.length)];
            name = prefix + " " + adj;
        } else if (type == 1) {
            // English style: "Sea Storm Pirates", "Blackbeard Crew"
            String adj = PIRATE_ADJ_EN[rand.nextInt(PIRATE_ADJ_EN.length)];
            String suffix = PIRATE_SUFFIX_EN[rand.nextInt(PIRATE_SUFFIX_EN.length)];
            name = adj + " " + suffix;
        } else {
            // Custom short Vietnamese: "Băng Lôi Thần", "Băng Hải Vương"
            name = "Băng " + VN_MAIN[rand.nextInt(VN_MAIN.length)] + " " + VN_MAIN[rand.nextInt(VN_MAIN.length)];
        }

        if (name.length() > 15) {
            name = name.substring(0, 15).trim();
        }
        return name;
    }

    public static String getRandomBotName() {
        Random rand = new Random();
        int style = rand.nextInt(5);
        String name;
        switch (style) {
            case 0: { // English: First + Second
                name = EN_FIRST[rand.nextInt(EN_FIRST.length)] + EN_SECOND[rand.nextInt(EN_SECOND.length)];
                break;
            }
            case 1: { // Vietnamese có dấu: Tiền tố + Tên chính
                name = VN_PREFIX[rand.nextInt(VN_PREFIX.length)] + " " + VN_MAIN[rand.nextInt(VN_MAIN.length)];
                break;
            }
            case 2: { // Vietnamese không dấu: Tiền tố + Tên chính
                name = removeAccents(VN_PREFIX[rand.nextInt(VN_PREFIX.length)]) + " " + removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                break;
            }
            case 3: { // Tên ngắn + hậu tố
                String first = rand.nextBoolean() ? EN_SHORT[rand.nextInt(EN_SHORT.length)] : removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                String suffix = SUFFIXES[rand.nextInt(SUFFIXES.length)];
                name = first + (rand.nextBoolean() ? " " : "") + suffix;
                break;
            }
            default: { // Tiếng Việt liền không dấu hoặc có dấu
                if (rand.nextBoolean()) {
                    name = removeAccents(VN_PREFIX[rand.nextInt(VN_PREFIX.length)]) + removeAccents(VN_MAIN[rand.nextInt(VN_MAIN.length)]);
                } else {
                    name = VN_PREFIX[rand.nextInt(VN_PREFIX.length)] + VN_MAIN[rand.nextInt(VN_MAIN.length)];
                }
                break;
            }
        }
        if (name.length() > 15) {
            name = name.substring(0, 15).trim();
        }
        return name;
    }

    public static final String[] CLAN_NOTICES = {
        "Băng Hải Tặc số 1 Đại Hải Trình! Tuyển mem chăm chỉ cày cấp, săn boss!",
        "Vươn buồm ra biển lớn! Cùng nhau chinh phục kho báu One Piece!",
        "Chiến đấu vì danh dự Băng Hải Tặc! PK lôi đài, phó bản mỗi ngày!",
        "Ai có ước mơ làm Vua Hải Tặc thì mau mau gia nhập nhé!",
        "Bang đoàn kết, giúp đỡ tân thủ nhiệt tình, ngày 3 nhiệm vụ bang!",
        "Gia nhập băng để nhận bùa EXP và cùng nhau săn Boss Thế Giới!",
        "Băng chill, anh em vào giao lưu chém gió kết bạn bốn phương!",
        "Kỷ luật là sức mạnh! Băng quyết tâm đứng Top 1 Bảng Xếp Hạng!",
        "Chiếm Đảo - Phó Bản Khổng Lồ - Đấu Trường Băng: Chiến hết mình!",
        "Thuyền trưởng bảo kê từ A-Z, mem chỉ việc cày cấp và nhận quà!"
    };

    public static String getRandomClanNotice() {
        return CLAN_NOTICES[new Random().nextInt(CLAN_NOTICES.length)];
    }

    /**
     * Kiểm tra độ dài và ký tự hợp lệ.
     * Chấp nhận chữ cái tiếng Anh, tiếng Việt có dấu, số và dấu cách.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.length() < 4 || name.length() > 15) return false;
        if (name.contains("  ")) return false;
        if (name.startsWith(" ") || name.endsWith(" ")) return false;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c) || c == ' ') {
                continue;
            }
            return false;
        }
        return true;
    }
}
