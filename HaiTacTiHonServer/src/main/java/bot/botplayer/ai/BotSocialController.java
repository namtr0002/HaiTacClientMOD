package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotBangHaiTac;
import bot.botplayer.BotWorldAnnounce;
import bot.botplayer.GameAnalyzer;
import model.Party;
import model.Player;
import core.ZUtil;
import network.Message;
import map.Zone;

/**
 * BotSocialController — Engine Tương Tác Xã Hội Cao Cấp.
 * Quản lý bang hội, phản hồi từ khóa chat chân thực, tự lập đội đi phó bản,
 * tự accept party invite từ người thật, follow vào dungeon, và biểu cảm bong bóng thoại.
 */
public class BotSocialController {

    private final BotPlayerReal bot;

    /** Map pool cho LOBBY_IDLE — phân tán khắp các map để tạo cảm giác đông người online */
    public static final int[] LOBBY_IDLE_MAPS = { 0, 1, 2, 3, 25, 49, 50, 69 };

    private static final String[] GREETINGS = {
        "Chào mng nha!", "Hải tặc xịn chào ae!", "Hôm nay đi săn boss rực rỡ quá",
        "Chúc ae cày game vui vẻ nha", "Lên cấp nhanh lẹ mng ơi",
        "Hôm nay drop đồ xịn không mng ơi?", "Server hôm nay ổn định ghê",
        "Cày thêm tí rồi offline", "Event gì thú vị không mọi người?"
    };

    private static final String[] COMBAT_CHAT = {
        "Dồn sát thương ae ơi!", "Đập nó nhanh lẹ!", "Boss này trâu vãi",
        "Giữ khoảng cách ae ơi", "Bơm máu lẹ lên!", "Cẩn thận skill boss!",
        "Ăn boss này là lên cấp rồi nha!"
    };

    private static final String[] DUNGEON_CHAT = {
        "Vào thôi ae!", "Phó bản này mình quen tay rồi!",
        "Dồn boss nhanh lên nào!", "Tank đứng trước đi!",
        "Ok tôi sẵn sàng rồi!", "Rush qua nha mọi người!",
        "Cảnh giác boss pha 2 mạnh lắm!", "Hặng vào thôi!",
        "Drop đồ đẹp nhé ae!", "Lần này mình thắng đẹp nha!"
    };

    private long lastChatTime = 0;

    public BotSocialController(BotPlayerReal bot) {
        this.bot = bot;
    }

    // ========================= CHAT =========================

    /**
     * Tự động nói chuyện bằng bong bóng chat dựa trên ngữ cảnh hiện tại.
     * Chỉ chat khi trong map có người chơi thật.
     */
    public void tryChat() {
        if (bot == null || bot.map == null) return;
        if (bot.map.template != null && bot.map.template.id == 119) return;
        long now = System.currentTimeMillis();
        if (now - lastChatTime < 15000) return;
        lastChatTime = now;

        if (!BotPlayerManager.hasRealPlayer(bot.map)) return;

        // Thỉnh thoảng rủ party
        if (ZUtil.random(100) < 15) {
            for (Player p : bot.map.players) {
                if (p != null && !p.isdie && p.IDPlayer != bot.IDPlayer && !p.isBot) {
                    double dist = Math.hypot(p.x - bot.x, p.y - bot.y);
                    if (dist < 100) {
                        try { bot.map.send_chat_popup(0, bot.index_map, "Pt đi chung cho lẹ bạn ơi!"); }
                        catch (Exception e) {}
                        break;
                    }
                }
            }
        }

        try {
            GameAnalyzer.ChatContext ctx = GameAnalyzer.ChatContext.IDLE;
            if ("BOSS".equals(bot.state) || "PVP".equals(bot.state)) {
                ctx = GameAnalyzer.ChatContext.COMBAT;
            } else if ("MARKET".equals(bot.state)) {
                ctx = GameAnalyzer.ChatContext.TRADING;
            } else if ("DUNGEON".equals(bot.state) || "FOLLOW_PARTY".equals(bot.state)) {
                ctx = GameAnalyzer.ChatContext.PARTY;
            }

            String chatText = GameAnalyzer.getLearnedChat(ctx);
            if (chatText == null || chatText.isEmpty()) {
                if (ctx == GameAnalyzer.ChatContext.COMBAT) {
                    chatText = COMBAT_CHAT[ZUtil.random(COMBAT_CHAT.length)];
                } else if (ctx == GameAnalyzer.ChatContext.PARTY) {
                    chatText = DUNGEON_CHAT[ZUtil.random(DUNGEON_CHAT.length)];
                } else {
                    chatText = GREETINGS[ZUtil.random(GREETINGS.length)];
                }
            }

            if (chatText != null && !chatText.isEmpty()) {
                bot.map.send_chat_popup(0, bot.index_map, chatText);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Gửi tin nhắn chat từ bot (bóng thoại).
     */
    public void chat(String msg) {
        if (bot.map == null || (bot.map.template != null && bot.map.template.id == 119)) return;
        try { bot.map.send_chat_popup(0, bot.index_map, msg); }
        catch (Exception ignored) {}
    }

    /**
     * Phản hồi thông minh khi có người chơi thật chat ở gần bot.
     */
    public void onPlayerChatNearby(Player sender, String text) {
        if (sender == null || sender.isBot || text == null || bot.map == null) return;
        if (bot.map.template != null && bot.map.template.id == 119) return;
        long now = System.currentTimeMillis();
        if (now - lastChatTime < 8000) return; // Cooldown phản hồi 8s
        
        String lower = text.toLowerCase().trim();
        String response = null;

        if (lower.contains("chào") || lower.contains("hello") || lower.contains("hi")) {
            String[] greets = {"Chào " + sender.name + " nha!", "Hế lô bro " + sender.name, "Chào bạn hiền!"};
            response = greets[ZUtil.random(greets.length)];
        } else if (lower.contains("bot")) {
            String[] botReplies = {"AI bảo tôi bot?", "Tôi người thật 100% nha bro", "Bot cái gì, tui đang cày nè!", "Nói bot là solo không?"};
            response = botReplies[ZUtil.random(botReplies.length)];
        } else if (lower.contains("pt") || lower.contains("nhóm") || lower.contains("phó bản")) {
            String[] partyReplies = {"Mời tui vào nhóm đi!", "Cho xin 1 slot pt với!", "Pt chung cày lẹ hơn nè"};
            response = partyReplies[ZUtil.random(partyReplies.length)];
        } else if (lower.contains("boss")) {
            String[] bossReplies = {"Boss xuất hiện map nào vậy?", "AE tập trung quật boss lẹ!", "Boss trâu phết"};
            response = bossReplies[ZUtil.random(bossReplies.length)];
        } else if (lower.contains("pro") || lower.contains("vip") || lower.contains("mạnh")) {
            String[] praiseReplies = {"Quá khen rồi haha", "Cố gắng cày thôi bro!", "Cảm ơn nha!"};
            response = praiseReplies[ZUtil.random(praiseReplies.length)];
        }

        if (response != null && ZUtil.random(100) < 80) { // 80% xác suất trả lời
            lastChatTime = now;
            chat(response);
        }
    }

    // ========================= PARTY & DUNGEON =========================

    /**
     * Đánh giá có nên đồng ý lời mời (Party/Trade) hay không.
     */
    public boolean shouldAcceptInvite(short inviteType) {
        int chance = (bot.botMode == BotPlayerReal.BotMode.BALANCED) ? 75 : 20;
        if (bot.state.equals("PVP") || bot.state.equals("BOSS_ASSIST") || bot.state.equals("BOSS")) {
            return false;
        }
        if (ZUtil.random(100) < chance) {
            bot.state = "REST";
            bot.nextActionTime = System.currentTimeMillis() + 5000;
            return true;
        }
        return false;
    }

    /**
     * Bot tự accept khi người thật mời vào party.
     * Được gọi từ Party.process() hoặc BotBrain khi nhận packet mời nhóm.
     *
     * @param leader Người chơi thật mời bot vào party của họ
     */
    public void tryAcceptPartyFromReal(Player leader) {
        if (leader == null || leader.isBot || leader.conn == null) return;
        if (bot.party != null) return;      // Đã trong nhóm khác
        if (bot.isdie) return;

        // Bot LOBBY_IDLE không đi phó bản — giả vờ không thấy lời mời
        if (bot.botType == BotPlayerReal.BotType.LOBBY_IDLE) return;

        // Xác suất accept 85% — giống người thật
        if (ZUtil.random(100) >= 85) {
            // Từ chối với lý do thực tế
            String[] declines = {
                "Tao bận cày, lần sau nha!", "Sorry mình đang có việc!",
                "Lát nữa nha bro!", "Không kịp rồi bạn ơi!"
            };
            chat(declines[ZUtil.random(declines.length)]);
            return;
        }

        try {
            // Tham gia party của leader
            if (leader.party == null) {
                leader.party = new Party(leader);
                leader.party.send_info();
            }
            leader.party.add_new_mem(bot);

            String[] acceptChats = {
                "Ok ok, đi thôi!", "Vào đi mọi người!", "Tôi sẵn sàng rồi!",
                "Rush thôi!", "Gồ đứng lại nào!", "Oke let's go!"
            };
            if (ZUtil.random(100) < 70) {
                chat(acceptChats[ZUtil.random(acceptChats.length)]);
            }

            // Bot sẽ follow leader vào dungeon
            bot.state = "FOLLOW_PARTY";

        } catch (Exception ignored) {}
    }

    /**
     * Bot tìm và mời người thật/bot khác vào party để đi phó bản.
     * Gọi định kỳ từ BotBrain khi bot đang ở map có NPC dungeon.
     */
    public void tryFormPartyForDungeon() {
        if (bot.map == null || bot.map.template == null) return;

        int mapId = bot.map.template.id;
        // Chỉ mời khi đang ở gần cửa phó bản
        boolean hasDungeonNpc = (mapId == 49 || mapId == 93 || mapId == 69
                || mapId == 113 || mapId == 191 || mapId == 83
                || mapId == 25 || mapId == 50);
        if (!hasDungeonNpc) return;
        if (ZUtil.random(100) < 80) return;  // Tỉ lệ thấp, không spam
        if (bot.party != null && bot.party.list.size() >= 3) return;

        // Ưu tiên mời người thật
        Player candidate = null;
        for (Player p : bot.map.players) {
            if (p != null && p.IDPlayer != bot.IDPlayer && !p.isBot && p.conn != null) {
                if (p.party == null) { candidate = p; break; }
            }
        }
        // Fallback: mời bot khác
        if (candidate == null) {
            for (Player p : bot.map.players) {
                if (p != null && p.IDPlayer != bot.IDPlayer && (p instanceof bot.botplayer.BotPlayerReal) && p.party == null) {
                    candidate = p; break;
                }
            }
        }

        if (candidate == null) return;

        try {
            if (bot.party == null) {
                bot.party = new Party(bot);
                bot.party.send_info();
            }

            if (candidate.isBot) {
                // Add bot trực tiếp
                bot.party.add_new_mem(candidate);
                if (ZUtil.random(100) < 40) chat("Ghép nhóm phó bản nhé @" + candidate.name);
            } else {
                // Gửi packet mời người thật
                if (candidate.party == null) {
                    Message m = new Message(-25);
                    m.writer().writeByte(0);
                    m.writer().writeShort(bot.index_map);
                    m.writer().writeUTF(bot.name);
                    candidate.addmsg(m);
                    m.cleanup();
                    if (ZUtil.random(100) < 60) chat("Mời @" + candidate.name + " vào nhóm đi phó bản!");
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Bot follow party leader vào dungeon.
     * Gọi từ BotBrain khi state = FOLLOW_PARTY.
     *
     * @return true nếu đã vào dungeon thành công
     */
    public boolean tryFollowPartyIntoDungeon() {
        if (bot.party == null || bot.party.list.isEmpty()) {
            bot.state = "FARM";
            return false;
        }

        // Tìm leader (ai không phải bot hiện tại)
        Player leader = null;
        for (Player p : bot.party.list) {
            if (p != null && !p.equals(bot)) {
                leader = p;
                break;
            }
        }

        if (leader == null) { bot.state = "FARM"; return false; }

        // Nếu leader đã vào dungeon, theo vào luôn
        if (leader.dungeon != null && leader.dungeon.maps != null && !leader.dungeon.maps.isEmpty()) {
            try {
                Zone firstMap = leader.dungeon.maps.get(0);
                if (firstMap != null) {
                    bot.leave();
                    bot.dungeon = leader.dungeon;
                    bot.x = (short)(350 + ZUtil.random(80) - 40);
                    bot.y = 260;
                    bot.join(firstMap, bot.x, bot.y);
                    bot.state = "DUNGEON";

                    if (ZUtil.random(100) < 60) {
                        String[] joinChat = { "Tôi vào rồi!", "Ok bắt đầu!", "Thôi đi thôi!", "Gồ sẵn sàng!" };
                        chat(joinChat[ZUtil.random(joinChat.length)]);
                    }
                    return true;
                }
            } catch (Exception ignored) {}
        }

        // Leader chưa vào dungeon — follow leader sang map
        if (leader.map != null && bot.map != null && !leader.map.equals(bot.map)) {
            int fromMap = bot.map.template != null ? bot.map.template.id : 0;
            int toMap   = leader.map.template != null ? leader.map.template.id : 0;
            if (toMap > 0 && (bot.currentPath == null || bot.targetMapId != toMap)) {
                bot.targetMapId = toMap;
                bot.currentPath = Pathfinder.findPath(fromMap, toMap);
                bot.pathIndex = 0;
            }
            bot.getMovementController().traversePath();
        } else {
            // Cùng map — đứng chờ leader vào dungeon
            if (ZUtil.random(100) < 5) chat("Mình chờ vào pb nha ae!");
            bot.getMovementController().moveRandom();
        }
        return false;
    }

    // ========================= CLAN =========================

    /**
     * Thực hiện các hành động bang hội định kỳ.
     */
    public void tickClanActivity() {
        BotBangHaiTac.tickClanActivity(bot);
    }

    /**
     * Khi bật chính sách tuyển thành viên: đăng thông báo kênh thế giới.
     * Gọi từ BotClanMissionController.
     */
    public void recruitForClan() {
        if (bot.clan == null) return;
        BotWorldAnnounce.announceRecruitment(bot.name, bot.clan.name);
    }

    /**
     * Gửi tin nhắn kênh thế giới tùy chỉnh từ bot.
     */
    public void sendWorldMessage(String message) {
        BotWorldAnnounce.announceCustom(bot.name, message);
    }
}
