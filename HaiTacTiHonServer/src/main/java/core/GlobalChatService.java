package core;

import clan.Clan;
import clan.ClanMember;
import map.Map;
import map.MapManager;
import map.Zone;
import model.DeTu;
import model.Party;
import model.Player;
import network.Message;
import network.Session;
import network.SessionManager;
import template.GiftBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * GlobalChatService — Dịch vụ quản lý gửi Chat & Thông Báo đa kênh toàn diện của Server.
 * 
 * Hỗ trợ chuẩn xác các kênh giao thức Client:
 *  1. CAT_WORLD  (Kênh Thế Giới / Loa Thế Giới - Tab "Thế Giới" & Banner chạy chữ Opcode -31 type 1)
 *  2. CAT_SYSTEM (Kênh Hệ Thống / Thông Báo Server / Boss / Sự Kiện / Phó Bản - Tab "Hệ Thống" & Opcode -11)
 *  3. CAT_PUBLIC (Kênh Công Cộng / Chat Map / Khu vực xung quanh - Tab "Công Cộng" & Bubble Opcode 17)
 *  4. CAT_MAIL   (Hộp Thư / 'Cập Nhật Hệ Thống Thư' - Tab "Hộp Thư" Opcode 18 mở rộng & Opcode -110)
 *  5. CAT_CLAN   (Kênh Bang Hội / Nhóm - Tab "Bang Hội" / "Nhóm")
 *  6. CAT_PRIVATE(Trò Chuyện riêng 1-1 theo tên người chơi)
 * 
 * Linh hoạt nhận diện đối tượng gửi/nhận:
 *  - Player ID (int)
 *  - Player Name (String)
 *  - Player Object
 *  - Map / Zone
 *  - All Online Players (có tùy chọn lọc / loại trừ bot ảo, lính đánh thuê)
 */
public class GlobalChatService {

    private static GlobalChatService instance;

    public static GlobalChatService getInstance() {
        if (instance == null) {
            instance = new GlobalChatService();
        }
        return instance;
    }

    public static GlobalChatService gI() {
        return getInstance();
    }

    // =========================================================================
    // TÊN CÁC TAB CHUẨN TƯƠNG THÍCH HOÀN TOÀN CLIENT CHATTABSCREEN
    // =========================================================================
    public static final String TAB_WORLD   = "Thế Giới";
    public static final String TAB_SYSTEM  = "Hệ Thống";
    public static final String TAB_PUBLIC  = "Công Cộng";
    public static final String TAB_CLAN    = "Bang Hội";
    public static final String TAB_PARTY   = "Nhóm";
    public static final String TAB_MAIL    = "Hộp Thư";
    public static final String TAB_NOTIFY  = "Thông Báo";

    // =========================================================================
    // 1. TRUY VẤN VÀ LỌC NGƯỜI CHƠI (REAL PLAYER VS BOT ẢO)
    // =========================================================================

    /**
     * Kiểm tra xem Player có phải là người chơi thật đang online hay không (loại bỏ bot ảo).
     */
    public boolean isRealPlayer(Player p) {
        if (p == null || p.isClosed) return false;
        if (p.isBot) return false;
        if (p instanceof bot.Bot) return false;
        if (p instanceof bot.mercenary.MercenaryBot) return false;
        if (p instanceof bot.BotTruyNa) return false;

        // Nếu là Đệ tử đang được người chơi thật điều khiển qua lệnh !doithan:
        if (p instanceof DeTu) {
            DeTu dt = (DeTu) p;
            return dt.master != null && dt.master.conn != null && dt.master.conn.connected && !dt.master.isBot;
        }

        return p.conn != null && p.conn.connected;
    }

    /**
     * Lấy danh sách tất cả người chơi thật đang Online (loại trừ toàn bộ bot ảo).
     */
    public List<Player> getRealOnlinePlayers() {
        List<Player> list = new ArrayList<>();
        Collection<Player> players = SessionManager.PLAYERS_MAP.values();
        for (Player p : players) {
            if (isRealPlayer(p)) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Lấy danh sách người chơi online với tùy chọn lọc bot ảo.
     */
    public List<Player> getAllOnlinePlayers(boolean excludeBots) {
        if (excludeBots) {
            return getRealOnlinePlayers();
        }
        List<Player> list = new ArrayList<>();
        Collection<Player> players = SessionManager.PLAYERS_MAP.values();
        for (Player p : players) {
            if (p != null && !p.isClosed) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Tìm Player theo ID (tìm trong SessionManager và Zone map).
     */
    public Player getPlayer(int playerId) {
        if (playerId <= 0) return null;
        Player p = SessionManager.PLAYERS_MAP.get(playerId);
        if (p == null) {
            p = Zone.get_player_by_id_allmap(playerId);
        }
        return p;
    }

    /**
     * Tìm Player theo tên.
     */
    public Player getPlayer(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) return null;
        Player p = SessionManager.PLAYERS_BY_NAME.get(playerName.trim());
        if (p == null) {
            p = Zone.get_player_by_name_allmap(playerName.trim());
        }
        return p;
    }

    /**
     * Chuyển đổi linh hoạt Object (Player, Integer ID, String Name) thành Player.
     */
    public Player resolvePlayer(Object target) {
        if (target == null) return null;
        if (target instanceof Player) return (Player) target;
        if (target instanceof Number) return getPlayer(((Number) target).intValue());
        if (target instanceof String) return getPlayer((String) target);
        return null;
    }

    /**
     * Lấy tên hiển thị của người gửi (hỗ trợ cả Player, String, hoặc đệ tử).
     */
    public String getSenderName(Object sender) {
        if (sender == null) return "Hệ Thống";
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p instanceof DeTu && ((DeTu) p).master != null) {
                return ((DeTu) p).master.name;
            }
            return p.name != null ? p.name : "Hệ Thống";
        }
        return sender.toString();
    }

    /**
     * Làm sạch chuỗi ký tự unicode để hiển thị chuẩn xác trên font Client J2ME/LibGDX/Unity.
     */
    public String cleanText(String text) {
        return MailService.cleanClientText(text);
    }

    // =========================================================================
    // 2. PHƯƠNG THỨC GỬI PACKET AN TOÀN TỚI CLIENT
    // =========================================================================

    /**
     * Gửi Message trực tiếp hoặc vào hàng đợi msg của Player (hỗ trợ cả khi đang đổi thân đệ tử).
     */
    public void sendMessage(Player p, Message m, boolean cache) {
        if (p == null || m == null) return;
        try {
            if (p.conn != null && p.conn.connected) {
                if (cache) {
                    p.msgs.add(m);
                } else {
                    p.addmsg(m);
                }
            } else if (p.detu != null && p.detu.conn != null && p.detu.conn.connected) {
                if (cache) {
                    p.detu.msgs.add(m);
                } else {
                    p.detu.addmsg(m);
                }
            } else if (p instanceof DeTu && ((DeTu) p).master != null && ((DeTu) p).master.conn != null) {
                if (cache) {
                    ((DeTu) p).master.msgs.add(m);
                } else {
                    ((DeTu) p).master.addmsg(m);
                }
            }
        } catch (Exception ignored) {}
    }

    // =========================================================================
    // 3. GỬI TAB CHAT CHUẨN (OPCODE 18 - CHATTAB)
    // =========================================================================

    /**
     * Gửi tin nhắn vào Tab Chat của người chơi (Opcode 18).
     */
    public void sendChatTab(Player recipient, String tabName, String text) {
        if (recipient == null || text == null) return;
        try {
            Message m = new Message(18);
            m.writer().writeUTF(cleanText(tabName != null ? tabName : TAB_SYSTEM));
            m.writer().writeUTF(cleanText(text));
            sendMessage(recipient, m, false);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChatTab(int playerId, String tabName, String text) {
        Player p = getPlayer(playerId);
        if (p != null) sendChatTab(p, tabName, text);
    }

    public void sendChatTab(String playerName, String tabName, String text) {
        Player p = getPlayer(playerName);
        if (p != null) sendChatTab(p, tabName, text);
    }

    /**
     * Gửi tin nhắn Chat Tab tới tất cả người chơi online (loại trừ hoặc bao gồm bot).
     */
    public void sendChatTabToAll(String tabName, String text, boolean excludeBots) {
        if (text == null) return;
        List<Player> list = getAllOnlinePlayers(excludeBots);
        for (Player p : list) {
            sendChatTab(p, tabName, text);
        }
    }

    public void sendChatTabToAll(String tabName, String text) {
        sendChatTabToAll(tabName, text, true);
    }

    /**
     * Gửi tin nhắn Chat Tab tới tất cả người chơi trong một Zone / Map.
     */
    public void sendChatTabToZone(Zone zone, String tabName, String text, boolean excludeBots) {
        if (zone == null || text == null) return;
        for (Player p : zone.players) {
            if (p != null && (!excludeBots || isRealPlayer(p))) {
                sendChatTab(p, tabName, text);
            }
        }
    }

    public void sendChatTabToMap(int mapId, String tabName, String text, boolean excludeBots) {
        for (Map mapItem : MapManager.getInstance().getMaps()) {
            if (mapItem.template.id == mapId) {
                for (Zone zone : mapItem.zones) {
                    sendChatTabToZone(zone, tabName, text, excludeBots);
                }
            }
        }
    }

    // =========================================================================
    // 4. KÊNH THẾ GIỚI (WORLD CHAT / LOA THẾ GIỚI - CAT_WORLD)
    // =========================================================================

    /**
     * Gửi chat vào Kênh Thế Giới cho toàn bộ server (tab "Thế Giới").
     */
    public void chatWorld(Player sender, String text) {
        if (text == null || text.trim().isEmpty()) return;
        String senderName = getSenderName(sender);
        String formatted = senderName + ": " + text.trim();
        sendChatTabToAll(TAB_WORLD, formatted, true);
    }

    public void chatWorld(int senderPlayerId, String text) {
        Player sender = getPlayer(senderPlayerId);
        if (sender != null) {
            chatWorld(sender, text);
        } else {
            chatWorld("Hải Tặc", text);
        }
    }

    public void chatWorld(String senderName, String text) {
        if (text == null || text.trim().isEmpty()) return;
        String formatted = (senderName != null && !senderName.isEmpty()) ? senderName + ": " + text.trim() : text.trim();
        sendChatTabToAll(TAB_WORLD, formatted, true);
    }

    public void chatWorld(String text) {
        chatWorld("Hệ Thống", text);
    }

    /**
     * Gửi Loa Thông Báo Thế Giới chạy chữ trên đỉnh màn hình (Opcode -31, type 1).
     * Gói tin này đồng thời tự động thêm vào tab "Thế Giới" trên Client.
     * 
     * @param text       Nội dung thông báo
     * @param color      Mã màu hiển thị (0: Trắng, 1: Xanh, 2: Đỏ, 3: Cam, 4: Vàng, 5: Xanh lá)
     * @param iconClan   Icon bang hội hoặc -1 nếu không có
     */
    public void sendWorldBanner(String text, int color, int iconClan) {
        if (text == null || text.trim().isEmpty()) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(1); // 1 = World broadcast banner
            m.writer().writeUTF(cleanText(text));
            m.writer().writeByte(color >= 0 ? color : 0);
            m.writer().writeShort(iconClan);

            List<Player> players = getRealOnlinePlayers();
            for (Player p : players) {
                sendMessage(p, m, false);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendWorldBanner(String senderName, String text, int color) {
        String msg = (senderName != null && !senderName.isEmpty()) ? senderName + ": " + text : text;
        sendWorldBanner(msg, color, -1);
    }

    public void sendWorldBanner(String text) {
        sendWorldBanner(text, 0, -1);
    }

    // =========================================================================
    // 5. KÊNH HỆ THỐNG (SYSTEM CHAT / THÔNG BÁO - CAT_SYSTEM)
    // =========================================================================

    /**
     * Gửi tin nhắn vào tab "Hệ Thống" tới toàn bộ người chơi online (loại trừ bot ảo).
     */
    public void chatSystem(String text) {
        if (text == null || text.trim().isEmpty()) return;
        sendChatTabToAll(TAB_SYSTEM, text.trim(), true);
    }

    /**
     * Gửi thông báo hệ thống kèm tiền tố phân loại (ví dụ: [Sự Kiện], [Boss], [Phó Bản], [Admin]).
     */
    public void chatSystem(String prefix, String text) {
        if (text == null || text.trim().isEmpty()) return;
        String formatted = (prefix != null && !prefix.isEmpty()) ? prefix + " " + text.trim() : text.trim();
        sendChatTabToAll(TAB_SYSTEM, formatted, true);
    }

    /**
     * Gửi thông báo hệ thống riêng cho 1 người chơi.
     */
    public void chatSystemToPlayer(Player p, String text) {
        if (p == null || text == null) return;
        sendChatTab(p, TAB_SYSTEM, text.trim());
    }

    public void chatSystemToPlayer(int playerId, String text) {
        Player p = getPlayer(playerId);
        if (p != null) chatSystemToPlayer(p, text);
    }

    public void chatSystemToPlayer(String playerName, String text) {
        Player p = getPlayer(playerName);
        if (p != null) chatSystemToPlayer(p, text);
    }

    public void chatSystemToZone(Zone zone, String text) {
        sendChatTabToZone(zone, TAB_SYSTEM, text, true);
    }

    public void chatSystemToMap(int mapId, String text) {
        sendChatTabToMap(mapId, TAB_SYSTEM, text, true);
    }

    /**
     * Gửi Banner Hệ Thống chạy chữ trên màn hình (Opcode -31, type 0).
     */
    public void sendSystemBanner(String text, int color) {
        if (text == null || text.trim().isEmpty()) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0); // 0 = System info banner
            m.writer().writeUTF(cleanText(text));
            m.writer().writeByte(color >= 0 ? color : 0);
            m.writer().writeShort(-1);

            List<Player> players = getRealOnlinePlayers();
            for (Player p : players) {
                sendMessage(p, m, false);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSystemBanner(String text) {
        sendSystemBanner(text, 0);
    }

    /**
     * Gửi Banner Hệ Thống / Thông báo chạy trên màn hình (Opcode -31, type 0) riêng cho 1 người chơi (không lưu vào tab chat nào).
     */
    public void sendSystemBanner(Player p, String text, int color) {
        if (p == null || text == null || text.trim().isEmpty()) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0); // 0 = System info banner / fly text
            m.writer().writeUTF(cleanText(text));
            m.writer().writeByte(color >= 0 ? color : 0);
            m.writer().writeShort(-1);
            sendMessage(p, m, false);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSystemBanner(Player p, String text) {
        sendSystemBanner(p, text, 0);
    }

    /**
     * Gửi Banner Boss / Chiến Trường trên màn hình (Opcode -31, type 5).
     */
    public void sendBossBanner(String text) {
        if (text == null || text.trim().isEmpty()) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(5); // 5 = Boss/Fight announcement banner
            m.writer().writeUTF(cleanText(text));
            m.writer().writeByte(2); // Red highlight
            m.writer().writeShort(-1);

            List<Player> players = getRealOnlinePlayers();
            for (Player p : players) {
                sendMessage(p, m, false);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi Hộp thoại Modal Popup Thông Báo (Opcode -11) tới người chơi.
     */
    public void sendSystemPopup(Player p, String title, String content) {
        if (p == null || content == null) return;
        try {
            Message m = new Message(-11);
            m.writer().writeShort(0);
            m.writer().writeByte(0);
            m.writer().writeUTF(cleanText(title != null ? title : TAB_NOTIFY));
            m.writer().writeUTF(cleanText(content));
            m.writer().writeByte(0);
            sendMessage(p, m, false);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSystemPopup(int playerId, String title, String content) {
        Player p = getPlayer(playerId);
        if (p != null) sendSystemPopup(p, title, content);
    }

    public void sendSystemPopup(String playerName, String title, String content) {
        Player p = getPlayer(playerName);
        if (p != null) sendSystemPopup(p, title, content);
    }

    public void sendSystemPopupToAll(String title, String content, boolean excludeBots) {
        List<Player> list = getAllOnlinePlayers(excludeBots);
        for (Player p : list) {
            sendSystemPopup(p, title, content);
        }
    }

    public void sendSystemPopupToAll(String title, String content) {
        sendSystemPopupToAll(title, content, true);
    }

    // =========================================================================
    // 6. CẬP NHẬT HỆ THỐNG THƯ & GỬI THƯ (CAT_MAIL - 'Cập Nhật Hệ Thống Thư')
    // =========================================================================

    /**
     * Gửi thông báo 'Cập Nhật Hệ Thống Thư' chuẩn tới người chơi kèm làm mới Hộp Thư.
     */
    public void notifyMailUpdate(Player p, String message) {
        if (p == null) return;
        String notice = (message != null && !message.isEmpty()) ? message : "Hệ thống thư vừa có cập nhật mới!";
        sendSystemPopup(p, "Cập Nhật Hệ Thống Thư", notice);
        chatSystemToPlayer(p, "[Cập Nhật Hệ Thống Thư] " + notice);
    }

    public void notifyMailUpdate(int playerId, String message) {
        Player p = getPlayer(playerId);
        if (p != null) notifyMailUpdate(p, message);
    }

    public void notifyMailUpdate(String playerName, String message) {
        Player p = getPlayer(playerName);
        if (p != null) notifyMailUpdate(p, message);
    }

    /**
     * Broadcast 'Cập Nhật Hệ Thống Thư' cho toàn bộ người chơi online.
     */
    public void notifyMailUpdateToAll(String message) {
        String notice = (message != null && !message.isEmpty()) ? message : "Hệ thống thư vừa có cập nhật mới!";
        sendSystemPopupToAll("Cập Nhật Hệ Thống Thư", notice, true);
        chatSystem("[Cập Nhật Hệ Thống Thư] " + notice);
    }

    /**
     * Gửi Thư Hệ Thống (Thư Thông Báo / Quà tặng / Top / Sự Kiện) qua MailService.
     */
    public MailService.MailEntry sendMail(Player recipient, String sender, String title, String content,
                                          byte typeMail, boolean canReply, List<GiftBox> gifts, long durationMs) {
        if (recipient == null) return null;
        return MailService.sendMail(recipient, sender, title, content, typeMail, canReply, gifts, durationMs);
    }

    public MailService.MailEntry sendMail(int playerId, String sender, String title, String content, List<GiftBox> gifts) {
        Player p = getPlayer(playerId);
        int accountId = (p != null && p.conn != null) ? p.conn.idUser : 0;
        String pName = p != null ? p.name : "";
        return MailService.sendMailOffline(playerId, accountId, pName, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
    }

    public MailService.MailEntry sendMail(String playerName, String sender, String title, String content, List<GiftBox> gifts) {
        Player p = getPlayer(playerName);
        int playerId = p != null ? p.IDPlayer : 0;
        int accountId = (p != null && p.conn != null) ? p.conn.idUser : 0;
        return MailService.sendMailOffline(playerId, accountId, playerName, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
    }

    public MailService.MailEntry sendSystemMail(Player p, String title, String content, List<GiftBox> gifts) {
        return sendMail(p, "Hệ Thống", title, content, gifts != null && !gifts.isEmpty() ? MailService.MAIL_TYPE_GIFT : MailService.MAIL_TYPE_SYSTEM, false, gifts, 0L);
    }

    public MailService.MailEntry sendSystemMail(int playerId, String title, String content, List<GiftBox> gifts) {
        return sendMail(playerId, "Hệ Thống", title, content, gifts);
    }

    public MailService.MailEntry sendSystemMail(String playerName, String title, String content, List<GiftBox> gifts) {
        return sendMail(playerName, "Hệ Thống", title, content, gifts);
    }

    /**
     * Gửi Thư Hệ Thống kèm quà tới tất cả người chơi thật đang online.
     */
    public void sendSystemMailToAllOnline(String title, String content, List<GiftBox> gifts) {
        List<Player> list = getRealOnlinePlayers();
        for (Player p : list) {
            sendSystemMail(p, title, content, gifts);
        }
        notifyMailUpdateToAll("Bạn vừa nhận được thư mới: " + title);
    }

    // =========================================================================
    // 7. KÊNH CÔNG CỘNG / MAP CHAT (PUBLIC CHAT - CAT_PUBLIC & POPUP BUBBLE OPCODE 17)
    // =========================================================================

    /**
     * Gửi chat trong map (vừa hiện bong bóng chat trên đầu Opcode 17, vừa thêm vào tab "Công Cộng" trên Client).
     */
    public void chatPublic(Player sender, String text) {
        if (sender == null || sender.map == null || text == null || text.trim().isEmpty()) return;
        try {
            // Bong bóng chat trên đầu nhân vật & thêm vào Tab Công Cộng (Opcode 17)
            Message mPopup = new Message(17);
            mPopup.writer().writeShort(sender.index_map);
            mPopup.writer().writeByte(0); // 0 = Player
            mPopup.writer().writeUTF(cleanText(text.trim()));
            sender.map.send_msg_all_p(mPopup, null, false);
            mPopup.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatPublic(int senderPlayerId, String text) {
        Player sender = getPlayer(senderPlayerId);
        if (sender != null) chatPublic(sender, text);
    }

    /**
     * Gửi bong bóng chat trên đầu NPC trong map (Opcode 17).
     */
    public void sendNpcChatPopup(Zone zone, int npcId, String text) {
        if (zone == null || text == null || text.trim().isEmpty()) return;
        try {
            Message m = new Message(17);
            m.writer().writeShort(npcId);
            m.writer().writeByte(2); // 2 = NPC
            m.writer().writeUTF(cleanText(text.trim()));
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // 8. KÊNH BANG HỘI & NHÓM (CLAN & PARTY CHAT - CAT_CLAN)
    // =========================================================================

    /**
     * Gửi chat vào tab "Bang Hội" cho toàn bộ thành viên bang.
     */
    public void chatClan(Clan clan, String senderName, String text) {
        if (clan == null || clan.members == null || text == null || text.trim().isEmpty()) return;
        String formatted = "@" + (senderName != null ? senderName : "Bang Hội") + " : " + text.trim();
        for (ClanMember mem : clan.members) {
            if (mem == null || mem.name == null) continue;
            Player p0 = Zone.get_player_by_name_allmap(mem.name);
            if (p0 != null && isRealPlayer(p0)) {
                sendChatTab(p0, TAB_CLAN, formatted);
            }
        }
    }

    public void chatClan(Player sender, String text) {
        if (sender == null || sender.clan == null || text == null) return;
        chatClan(sender.clan, sender.name, text);
    }

    /**
     * Gửi chat vào tab "Nhóm" cho toàn bộ thành viên nhóm.
     */
    public void chatParty(Party party, String senderName, String text) {
        if (party == null || party.list == null || text == null || text.trim().isEmpty()) return;
        String formatted = "@" + (senderName != null ? senderName : "Nhóm") + " : " + text.trim();
        for (Player p0 : party.list) {
            if (p0 != null && isRealPlayer(p0)) {
                sendChatTab(p0, TAB_PARTY, formatted);
            }
        }
    }

    public void chatParty(Player sender, String text) {
        if (sender == null || sender.party == null || text == null) return;
        chatParty(sender.party, sender.name, text);
    }

    // =========================================================================
    // 9. KÊNH TRÒ CHUYỆN RIÊNG 1-1 (PRIVATE CHAT - CAT_PRIVATE)
    // =========================================================================

    /**
     * Gửi tin nhắn trò chuyện riêng 1-1 giữa 2 người chơi (Opcode 18 tab theo tên đối phương).
     */
    public void chatPrivate(Player sender, Player receiver, String text) {
        if (sender == null || receiver == null || text == null || text.trim().isEmpty()) return;
        if (receiver.isBot || receiver.isDe || receiver instanceof DeTu || receiver instanceof bot.mercenary.MercenaryBot) {
            sendSystemPopup(sender, "Thông Báo", "Không thể trò chuyện riêng với lính đánh thuê hoặc đệ tử!");
            return;
        }

        String senderName = sender.name;
        String receiverName = receiver.name;
        String content = text.trim();

        // Gửi tới người nhận: Tab = Tên người gửi, Nội dung = content
        sendChatTab(receiver, senderName, content);

        // Echo về người gửi nếu không cùng 1 nhân vật: Tab = Tên người nhận
        if (sender.IDPlayer != receiver.IDPlayer) {
            sendChatTab(sender, receiverName, content);
        }
    }

    public void chatPrivate(int senderId, int receiverId, String text) {
        Player sender = getPlayer(senderId);
        Player receiver = getPlayer(receiverId);
        if (sender != null && receiver != null) {
            chatPrivate(sender, receiver, text);
        }
    }

    public void chatPrivate(String senderName, String receiverName, String text) {
        Player sender = getPlayer(senderName);
        Player receiver = getPlayer(receiverName);
        if (sender != null && receiver != null) {
            chatPrivate(sender, receiver, text);
        }
    }

    // =========================================================================
    // 10. PHƯƠNG THỨC GỌI TỔNG QUÁT (GENERIC POLYMORPHIC CHAT METHODS)
    // =========================================================================

    /**
     * Phương thức chat tổng quát theo yêu cầu:
     * - Nếu playerID > 0: Gửi tin nhắn hệ thống/chat tới Player có ID tương ứng.
     * - Nếu playerID <= 0: Gửi tin nhắn hệ thống/thế giới tới toàn bộ người chơi online (trừ bot ảo).
     */
    public void chat(int playerID, String text) {
        if (text == null || text.trim().isEmpty()) return;
        if (playerID > 0) {
            Player p = getPlayer(playerID);
            if (p != null) {
                chatSystemToPlayer(p, text);
            }
        } else {
            chatSystem(text);
        }
    }

    /**
     * Gửi tin nhắn trực tiếp tới Player object.
     */
    public void chat(Player p, String text) {
        if (p != null) {
            chatSystemToPlayer(p, text);
        } else {
            chatSystem(text);
        }
    }

    /**
     * Gửi tin nhắn trực tiếp theo tên người chơi.
     */
    public void chat(String playerName, String text) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            Player p = getPlayer(playerName);
            if (p != null) {
                chatSystemToPlayer(p, text);
                return;
            }
        }
        chatSystem(text);
    }

    /**
     * Phương thức điều hướng chat linh hoạt:
     * 
     * @param target  Có thể là Player, Integer ID, String Name, Zone, hoặc null (All Server)
     * @param tabName Tên Tab ("Thế Giới", "Hệ Thống", "Công Cộng", "Bang Hội", "Nhóm", hoặc Tên người nhận)
     * @param text    Nội dung tin nhắn
     */
    public void chat(Object target, String tabName, String text) {
        if (text == null || text.trim().isEmpty()) return;

        if (target == null) {
            sendChatTabToAll(tabName != null ? tabName : TAB_SYSTEM, text, true);
            return;
        }

        if (target instanceof Zone) {
            sendChatTabToZone((Zone) target, tabName != null ? tabName : TAB_PUBLIC, text, true);
            return;
        }

        Player p = resolvePlayer(target);
        if (p != null) {
            sendChatTab(p, tabName != null ? tabName : TAB_SYSTEM, text);
        }
    }
}

