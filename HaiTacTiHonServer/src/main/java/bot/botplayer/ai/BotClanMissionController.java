package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotBangHaiTac;
import clan.Clan;
import clan.ClanMember;
import bot.botplayer.BotWorldAnnounce;
import core.ZUtil;

/**
 * BotClanMissionController — Quản lý nhiệm vụ clan của bot A-Z.
 *
 * Luồng chuẩn:
 *  1. Kiểm tra bot có clan không
 *  2. Nếu là trưởng/phó: phát nhiệm vụ nếu countAction > 0
 *  3. Nhận nhiệm vụ clan (cập nhật numquest)
 *  4. Cày exp để nộp xp cho clan
 *  5. Sau khi hoàn thành: tăng điểm cống hiến (donate), thông báo clan
 *
 * Không tạo Thread — gọi từ BotBrain mỗi chu kỳ.
 */
public class BotClanMissionController {

    private final BotPlayerReal bot;

    // State nội bộ
    private long lastMissionCheck = 0;
    private long lastDonateTime = 0;
    private long lastRoleSetTime = 0;
    private long lastRecruitAnnounce = 0;
    private long lastBuffActivate = 0;
    private boolean missionActive = false;
    private int missionProgressTarget = 0; // Số quái/mức xp cần cày
    private int missionProgressCurrent = 0;

    // Khoảng thời gian
    private static final long MISSION_CHECK_INTERVAL  = 5 * 60 * 1000L;  // 5 phút
    private static final long DONATE_INTERVAL         = 3 * 60 * 1000L;  // 3 phút
    private static final long ROLE_SET_INTERVAL       = 30 * 60 * 1000L; // 30 phút
    private static final long RECRUIT_INTERVAL        = 10 * 60 * 1000L; // 10 phút
    private static final long BUFF_INTERVAL           = 60 * 60 * 1000L; // 60 phút

    // Vai trò clan
    private static final int ROLE_TRUONG  = 0;  // Thuyền trưởng
    private static final int ROLE_PHO     = 1;  // Thuyền phó
    private static final int ROLE_HOATIEU = 2;  // Hoa tiêu
    private static final int ROLE_MEMBER  = 10; // Thành viên

    public BotClanMissionController(BotPlayerReal bot) {
        this.bot = bot;
    }

    // ========================= MAIN TICK =========================

    /**
     * Tick chính — gọi từ BotBrain mỗi chu kỳ state CLAN_MISSION.
     * @return true nếu đang xử lý nhiệm vụ clan (gợi ý caller tiếp tục)
     */
    public boolean tick() {
        if (bot == null || bot.clan == null) {
            tryClanSetup();
            return false;
        }

        long now = System.currentTimeMillis();

        // 1. Nộp XP clan định kỳ (cống hiến)
        if (now - lastDonateTime > DONATE_INTERVAL) {
            lastDonateTime = now;
            donateToClan();
        }

        // 2. Thiết lập vai trò thành viên (chỉ leader/phó)
        if (now - lastRoleSetTime > ROLE_SET_INTERVAL) {
            lastRoleSetTime = now;
            manageRoles();
        }

        // 3. Kích hoạt buff clan (chỉ leader)
        if (now - lastBuffActivate > BUFF_INTERVAL) {
            lastBuffActivate = now;
            tryActivateClanBuff();
        }

        // 4. Chiêu mộ thành viên (chỉ leader/phó, qua kênh thế giới)
        if (now - lastRecruitAnnounce > RECRUIT_INTERVAL) {
            lastRecruitAnnounce = now;
            tryRecruit();
        }

        // 5. Xử lý nhiệm vụ clan
        if (now - lastMissionCheck > MISSION_CHECK_INTERVAL) {
            lastMissionCheck = now;
            handleClanMission();
        }

        return missionActive;
    }

    // ========================= CLAN SETUP =========================

    /**
     * Bot chưa có clan: thử gia nhập hoặc tạo clan mới.
     */
    private void tryClanSetup() {
        if (bot.level < 20) return;
        if (ZUtil.random(100) < 70) {
            // Ưu tiên gia nhập clan đã có trước (thực tế hơn)
            if (!BotBangHaiTac.tryJoinClan(bot)) {
                if (bot.level >= 25 && ZUtil.random(100) < 40) {
                    boolean created = BotBangHaiTac.tryCreateClan(bot);
                    if (created && bot.clan != null) {
                        BotWorldAnnounce.announceClanCreated(bot.name, bot.clan.name);
                    }
                }
            }
        }
    }

    // ========================= DONATE =========================

    /**
     * Nộp XP cho clan từ exp tích lũy.
     */
    private void donateToClan() {
        if (bot.clan == null) return;
        try {
            Clan clan = bot.clan;
            // Nộp XP ngẫu nhiên giả lập cày — 50-300 XP mỗi lần
            int xpDonate = ZUtil.random(50, 300) + (bot.level / 10) * 20;
            synchronized (clan) {
                clan.xp += xpDonate;
            }
            // Cập nhật donate count của member
            updateMemberDonate(xpDonate);
        } catch (Exception ignored) {}
    }

    private void updateMemberDonate(int xpAmount) {
        if (bot.clan == null || bot.clan.members == null) return;
        for (ClanMember mem : bot.clan.members) {
            if (mem != null && mem.name != null && mem.name.equals(bot.name)) {
                mem.donate += Math.max(1, xpAmount / 100);
                return;
            }
        }
    }

    // ========================= ROLE MANAGEMENT =========================

    /**
     * Leader/phó tự động set phong hàm cho thành viên chưa có vai trò cao.
     * Ưu tiên: bot cấp cao -> phó, bot cấp trung -> hoa tiêu.
     */
    private void manageRoles() {
        if (bot.clan == null || bot.clan.members == null) return;
        int myRole = getMyRole();
        if (myRole != ROLE_TRUONG && myRole != ROLE_PHO) return; // Chỉ leader/phó mới set

        try {
            Clan clan = bot.clan;
            int phoCnt = 0;
            int hoaTieuCnt = 0;

            for (ClanMember mem : clan.members) {
                if (mem == null) continue;
                if (mem.levelInclan == ROLE_PHO) phoCnt++;
                if (mem.levelInclan == ROLE_HOATIEU) hoaTieuCnt++;
            }

            // Set phó bang (tối đa 2 phó)
            if (phoCnt < 2) {
                for (ClanMember mem : clan.members) {
                    if (mem == null || mem.name.equals(bot.name)) continue;
                    if (mem.levelInclan == ROLE_MEMBER && mem.level >= 30) {
                        // Chỉ set phó cho bot level cao đủ tiêu chuẩn
                        if (BotPlayerManager.activeBots.containsKey(mem.name)) {
                            mem.levelInclan = ROLE_PHO;
                            phoCnt++;
                            // Chat thông báo trong clan
                            sendClanChat("Phong " + mem.name + " làm Thuyền phó! Chúc mừng!");
                            if (phoCnt >= 2) break;
                        }
                    }
                }
            }

            // Set hoa tiêu (tối đa 3 hoa tiêu)
            if (hoaTieuCnt < 3) {
                for (ClanMember mem : clan.members) {
                    if (mem == null || mem.name.equals(bot.name)) continue;
                    if (mem.levelInclan == ROLE_MEMBER && mem.level >= 20) {
                        if (BotPlayerManager.activeBots.containsKey(mem.name)) {
                            mem.levelInclan = ROLE_HOATIEU;
                            hoaTieuCnt++;
                            sendClanChat("Phong " + mem.name + " làm Hoa tiêu! Cày cùng nhau nhé!");
                            if (hoaTieuCnt >= 3) break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    // ========================= CLAN BUFF =========================

    /**
     * Kích hoạt buff clan nếu là leader và clan có đủ điểm.
     */
    private void tryActivateClanBuff() {
        if (bot.clan == null) return;
        if (getMyRole() != ROLE_TRUONG) return;
        // Buff clan: nếu clan.pointAttri > 0, phân bổ thuộc tính bang
        try {
            Clan clan = bot.clan;
            synchronized (clan) {
                if (clan.pointAttri > 0 && clan.opAttri != null && clan.opAttri.length > 0) {
                    int slot = ZUtil.random(clan.opAttri.length);
                    clan.opAttri[slot]++;
                    clan.pointAttri--;
                }
                if (clan.buff == null || clan.buff.isEmpty()) {
                    clan.buff = new java.util.concurrent.CopyOnWriteArrayList<>();
                    clan.buff.add(new template.EffTemplate((short) 0, (short) 100, System.currentTimeMillis() + 86400000L * 7));
                }
            }
        } catch (Exception ignored) {}
    }

    // ========================= RECRUITMENT =========================

    /**
     * Chiêu mộ thành viên qua kênh thế giới nếu clan chưa đủ người.
     */
    private void tryRecruit() {
        if (bot.clan == null) return;
        int myRole = getMyRole();
        if (myRole != ROLE_TRUONG && myRole != ROLE_PHO) return;
        if (bot.clan.members == null) return;
        if (bot.clan.members.size() >= 8) return; // Đủ người rồi

        BotWorldAnnounce.announceRecruitment(bot.name, bot.clan.name);
    }

    // ========================= CLAN MISSION =========================

    /**
     * Xử lý nhiệm vụ clan: nhận -> cày -> nộp.
     * Sử dụng numquest của ClanMember để theo dõi tiến độ.
     */
    private void handleClanMission() {
        if (bot.clan == null) return;

        ClanMember myMem = getMyMember();
        if (myMem == null) return;

        if (!missionActive) {
            // Nhận nhiệm vụ mới nếu chưa có
            if (myMem.numquest <= 0 && bot.clan.countAction > 0) {
                acceptMission(myMem);
            } else if (myMem.numquest > 0) {
                // Tiếp tục nhiệm vụ đang dở
                missionActive = true;
                missionProgressTarget = calculateMissionTarget();
                missionProgressCurrent = 0;
            }
        } else {
            // Đang làm nhiệm vụ — cày để hoàn thành
            missionProgressCurrent += ZUtil.random(10, 30);
            if (missionProgressCurrent >= missionProgressTarget) {
                completeMission(myMem);
            }
        }
    }

    private void acceptMission(ClanMember myMem) {
        try {
            myMem.numquest = 3; // 3 = đang có nhiệm vụ clan
            missionActive = true;
            missionProgressTarget = calculateMissionTarget();
            missionProgressCurrent = 0;
            if (ZUtil.random(100) < 40) {
                sendClanChat("Nhận nhiệm vụ bang rồi, đi cày thôi!");
            }
        } catch (Exception ignored) {}
    }

    private void completeMission(ClanMember myMem) {
        try {
            myMem.numquest = 0;
            myMem.conghien = Math.min(myMem.conghien + 1, Integer.MAX_VALUE);
            missionActive = false;
            missionProgressCurrent = 0;
            // Nộp XP thêm khi hoàn thành mission
            donateToClan();
            if (ZUtil.random(100) < 50) {
                sendClanChat("Hoàn thành nhiệm vụ bang rồi nhé ae!");
            }
        } catch (Exception ignored) {}
    }

    private int calculateMissionTarget() {
        // Target tỉ lệ với level bot
        return 50 + (bot.level * 2) + ZUtil.random(50);
    }

    // ========================= HELPERS =========================

    private void sendClanChat(String msg) {
        if (bot.clan == null || bot.map == null) return;
        try {
            bot.map.send_chat_popup(0, bot.index_map, "[Bang] " + msg);
        } catch (Exception ignored) {}
    }

    private int getMyRole() {
        if (bot.clan == null || bot.clan.members == null) return ROLE_MEMBER;
        for (ClanMember mem : bot.clan.members) {
            if (mem != null && mem.name != null && mem.name.equals(bot.name)) {
                return mem.levelInclan;
            }
        }
        return ROLE_MEMBER;
    }

    private ClanMember getMyMember() {
        if (bot.clan == null || bot.clan.members == null) return null;
        for (ClanMember mem : bot.clan.members) {
            if (mem != null && mem.name != null && mem.name.equals(bot.name)) return mem;
        }
        return null;
    }
}
