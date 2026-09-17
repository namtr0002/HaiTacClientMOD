package activities;

import java.io.IOException;
import model.Player;
import model.DeTu;
import model.YesNoDialog;
import network.Service;
import network.Message;
import map.Zone;
import core.Manager;
import core.GlobalChatService;

public class Chat {

    public static boolean isWorldTabName(String name) {
        if (name == null) return false;
        String lower = name.trim().toLowerCase();
        return lower.equals("thế giới") || lower.equals("the gioi") || lower.equals("thegioi")
                || lower.equals("ktg") || lower.equals("world") || lower.equals("loa")
                || lower.equals("kênh thế giới") || lower.equals("kenh the gioi")
                || lower.equals("kênh the gioi") || lower.equals("kenh thế giới")
                || lower.equals("global");
    }

    public static String extractWorldChatPrefix(String text) {
        if (text == null) return null;
        String trimmed = text.trim();
        String lower = trimmed.toLowerCase();
        String[] prefixes = new String[]{
            "!tg ", "/tg ", "@tg ", "#tg ",
            "!ktg ", "/ktg ", "@ktg ", "#ktg ",
            "!thegioi ", "/thegioi ", "@thegioi ",
            "!world ", "/world ", "@world ",
            "!loa ", "/loa ", "@loa "
        };
        for (String prefix : prefixes) {
            if (lower.startsWith(prefix)) {
                return trimmed.substring(prefix.length()).trim();
            }
        }
        return null;
    }

    public static void process(Player p, Message m2, int type) throws IOException {
        if (p != null && p.isMute) {
            p.getService().send_box_ThongBao_OK("Bạn đang bị Admin cấm chat (Khóa mõm)!");
            return;
        }
        if (type == 0) {
            String tab_name = m2.reader().readUTF();
            String text = m2.reader().readUTF();
            String senderName = (p instanceof DeTu && ((DeTu) p).master != null) ? ((DeTu) p).master.name : p.name;

            // === LỆNH ĐỔI THÂN: !doithan / !suphu / !sp ===
            if (text != null) {
                String lower = text.trim().toLowerCase();
                if (lower.equals("!doithan") || lower.equals("!doit") || lower.equals("!switch")
                        || lower.equals("!suphu") || lower.equals("!sp") || lower.equals("!su phu")
                        || lower.equals("!dt doit") || lower.equals("!dt switch") || lower.equals("!dt doithan")) {
                    if (p instanceof DeTu) {
                        DeTu dt = (DeTu) p;
                        if (dt.master != null) {
                            dt.switchCharacter(dt.master);
                            return;
                        }
                    } else if (p.detu != null) {
                        p.switchCharacter(p.detu);
                        return;
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                        return;
                    }
                }
                if (lower.equals("!cuonghoa") || lower.equals("!cuonghoaskill") || lower.equals("!nangcap")
                        || lower.equals("!skill") || lower.equals("!acquy") || lower.equals("!nangcapacquy")
                        || lower.equals("!dt cuonghoa") || lower.equals("!dt skill") || lower.equals("!dt acquy")) {
                    if (p instanceof DeTu) {
                        activities.UpgradeDevil.show_table(p, 1);
                    } else if (p.detu != null) {
                        p.switchCharacter(p.detu);
                        activities.UpgradeDevil.show_table(p.detu, 1);
                    } else {
                        activities.UpgradeDevil.show_table(p, 1);
                    }
                    return;
                }
                if (lower.equals("!checknap") || lower.equals("!theodoinap") || lower.equals("!lichsunap")
                        || lower.equals("/checknap") || lower.equals("/theodoinap") || lower.equals("/lichsunap")
                        || lower.equals("!lsnap") || lower.equals("/lsnap")) {
                    historys.RechargeHistory.showRechargeHistoryDialog(p);
                    return;
                }
            }

            // === LỆNH CHAT ĐỆ TỬ: !dt <trạng thái> / !detu <trạng thái> ===
            if (text != null && (text.toLowerCase().startsWith("!detu ") || text.toLowerCase().startsWith("!dt "))) {
                String cmd = text.toLowerCase().startsWith("!detu ") ? text.substring(6).trim().toLowerCase() : text.substring(4).trim().toLowerCase();
                Player master = (p instanceof DeTu) ? ((DeTu) p).master : p;
                DeTu dt = (master != null) ? master.detu : null;
                if (dt == null && master != null && master.isFusion) {
                    // Có thể đệ tử đang hợp thể (dtu != null nhưng detuStatus == FUSION)
                    dt = master.detu;
                }
                if (dt == null && !(p instanceof DeTu)) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
                    return;
                }
                if (cmd.equalsIgnoreCase("cuonghoa")
                        || cmd.equalsIgnoreCase("cường hóa")
                        || cmd.equalsIgnoreCase("nangcap")
                        || cmd.equalsIgnoreCase("nâng cấp")
                        || cmd.equalsIgnoreCase("skill")
                        || cmd.equalsIgnoreCase("acquy")
                        || cmd.equalsIgnoreCase("ác quỷ")) {
                    if (p instanceof DeTu) {
                        activities.UpgradeDevil.show_table(p, 1);
                    } else if (dt != null) {
                        p.switchCharacter(dt);
                        activities.UpgradeDevil.show_table(dt, 1);
                    }
                    return;
                } else if (cmd.equalsIgnoreCase("theo")
                        || cmd.equalsIgnoreCase("follow")) {
                    if (dt != null) dt.setStatus(DeTu.STATUS_FOLLOW);
                    return;
                } else if (cmd.equalsIgnoreCase("bv")
                        || cmd.equalsIgnoreCase("bao ve")
                        || cmd.equalsIgnoreCase("bảo vệ")
                        || cmd.equalsIgnoreCase("protect")) {
                    if (dt != null) dt.setStatus(DeTu.STATUS_PROTECT);
                    return;
                } else if (cmd.equalsIgnoreCase("tk")
                        || cmd.equalsIgnoreCase("tc")
                        || cmd.equalsIgnoreCase("tan cong")
                        || cmd.equalsIgnoreCase("tấn công")
                        || cmd.equalsIgnoreCase("attack")
                        || cmd.equalsIgnoreCase("atk")) {
                    if (dt != null) dt.setStatus(DeTu.STATUS_ATTACK);
                    return;
                } else if (cmd.equalsIgnoreCase("ht")
                        || cmd.equalsIgnoreCase("hopthe")
                        || cmd.equalsIgnoreCase("hợp thể")
                        || cmd.equalsIgnoreCase("fusion")) {
                    if (dt != null) dt.setStatus(DeTu.STATUS_FUSION);
                    return;
                } else if (cmd.equalsIgnoreCase("ve")
                        || cmd.equalsIgnoreCase("venha")
                        || cmd.equalsIgnoreCase("về nhà")
                        || cmd.equalsIgnoreCase("home")) {
                    if (dt != null) dt.setStatus(DeTu.STATUS_HOME);
                    return;
                } else {
                    p.getService().send_box_ThongBao_OK(
                        "Lệnh đệ tử: !dt theo | !dt bv | !dt tk | !dt ht | !dt ve | !dt cuonghoa | !doithan"
                    );
                    return;
                }
            }
            // === LỆNH CHAT LÍNH ĐÁNH THUÊ: !linh <trạng thái> ===
            if (text != null && (text.toLowerCase().startsWith("!linh") || text.toLowerCase().startsWith("!lính"))) {
                String cmd = text.length() > 5 ? text.substring(5).trim().toLowerCase() : "th";
                Player master = (p instanceof DeTu && ((DeTu) p).master != null) ? ((DeTu) p).master : p;
                switch (cmd) {
                    case "th":
                    case "trieuhoi":
                    case "triệu hồi":
                    case "theo":
                    case "gọi":
                    case "goi":
                        bot.mercenary.MercenaryManager.gI().summonAllActiveBots(master);
                        return;
                    case "ve":
                    case "về":
                    case "rut":
                    case "rút":
                    case "an":
                    case "ẩn":
                        bot.mercenary.MercenaryManager.gI().recallAllBots(master);
                        return;
                    default:
                        p.getService().send_box_ThongBao_OK("Lệnh lính đánh thuê: !linh goi (triệu hồi) hoặc !linh ve (rút về)");
                        return;
                }
            }

            if (isWorldTabName(tab_name)) {
                requestWorldChat(p, text);
                return;
            }

            switch (tab_name) {
                case "Nhóm": {
                    if (p.party != null) {
                        for (int i = 0; i < p.party.list.size(); i++) {
                            Player p0 = p.party.list.get(i);
                            if (p0.index_map != p.index_map) {
                                send_chat(p0, tab_name, "@" + senderName + " : " + text, false);
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Nhóm không tồn tại");
                    }
                    break;
                }
                case "Bang Hội":
                case "Bang hội":
                case "Bang": {
                    if (p.clan != null) {
                        GlobalChatService.getInstance().chatClan(p.clan, senderName, text);
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn chưa tham gia bang hội!");
                    }
                    break;
                }
                case "Hệ Thống":
                case "Hệ thống": {
                    p.getService().send_box_ThongBao_OK("Kênh Hệ Thống chỉ nhận thông báo từ máy chủ!");
                    break;
                }
                case "Công Cộng":
                case "Công cộng":
                case "Tin đến":
                case "tin đến":
                case "Tin Den":
                case "tin den": {
                    GlobalChatService.getInstance().chatPublic(p, text);
                    break;
                }
                default: {
                    Player p0 = Zone.get_player_by_name_allmap(tab_name);
                    if (p0 != null) {
                        if (p0.isBot || p0.isDe || p0 instanceof DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                            p.getService().send_box_ThongBao_OK("Không thể trò chuyện riêng với lính đánh thuê hoặc đệ tử!");
                            return;
                        }
                        send_chat(p0, senderName, text, false);
                    } else {
                        p.getService().send_box_ThongBao_OK("Đối phương offline");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Yêu cầu gửi chat Kênh Thế Giới (5 Ruby/lượt) với hộp thoại Yes/No xác nhận từ Server.
     */
    public static void requestWorldChat(Player p, String text) {
        if (p == null || p.isClosed) return;
        if (text == null || text.trim().isEmpty()) return;

        final String chatContent = text.trim();

        // 1. Kiểm tra cấp độ (tối thiểu level 20)
        if (p.level < 20) {
            p.getService().send_box_ThongBao_OK("Chưa đủ cấp độ 20 không thể chat Kênh Thế Giới!");
            return;
        }

        // 2. Kiểm tra giãn cách chat (Cooldown 30s)
        if (p.time_chat_ktg > System.currentTimeMillis()) {
            long remainingSeconds = (p.time_chat_ktg - System.currentTimeMillis()) / 1000L;
            if (remainingSeconds < 1) remainingSeconds = 1;
            p.getService().send_box_ThongBao_OK("Vui lòng chờ " + remainingSeconds + "s nữa để chat tiếp Kênh Thế Giới!");
            return;
        }

        // 3. Kiểm tra số dư Ruby (Ngọc)
        if (p.get_ngoc() < 5) {
            p.getService().send_box_ThongBao_OK("Bạn không đủ 5 Ruby để chat Kênh Thế Giới!");
            return;
        }

        // 4. Mở hộp thoại Yes/No xác nhận từ phía Server
        String confirmMsg = "Bạn có đồng ý dùng 5 Ruby để gửi tin nhắn lên Kênh Thế Giới không?\nNội dung: " + chatContent;
        YesNoDialog dlg = new YesNoDialog(p, 9988, "Kênh Thế Giới", confirmMsg, new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1});
        dlg.setHandler((byte val) -> {
            if (val == 0) { // Người chơi bấm "Đồng ý"
                if (p.isClosed || p.conn == null || !p.conn.connected) return;

                // Kiểm tra lại điều kiện an toàn khi người chơi bấm Đồng ý
                if (p.level < 20) {
                    p.getService().send_box_ThongBao_OK("Chưa đủ cấp độ 20 không thể chat Kênh Thế Giới!");
                    return;
                }
                if (p.time_chat_ktg > System.currentTimeMillis()) {
                    long remainingSeconds = (p.time_chat_ktg - System.currentTimeMillis()) / 1000L;
                    if (remainingSeconds < 1) remainingSeconds = 1;
                    p.getService().send_box_ThongBao_OK("Vui lòng chờ " + remainingSeconds + "s nữa để chat tiếp Kênh Thế Giới!");
                    return;
                }
                if (p.get_ngoc() < 5) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ 5 Ruby để chat Kênh Thế Giới!");
                    return;
                }

                // Trừ 5 Ruby và cập nhật tiền
                p.update_ngoc(-5);
                p.updateMoney();

                // Thiết lập cooldown 30 giây
                p.time_chat_ktg = System.currentTimeMillis() + 30_000L;

                // Lấy tên người gửi (hỗ trợ cả đệ tử !doithan)
                String senderName = (p instanceof DeTu && ((DeTu) p).master != null) 
                    ? ((DeTu) p).master.name 
                    : p.name;

                // Gửi Loa Thế Giới (Opcode -31 type 1)
                try {
                    Manager.gI().chatKTG(1, senderName + ": " + chatContent, 0);
                    p.getService().send_box_ThongBao_OK("Đã gửi tin nhắn Kênh Thế Giới thành công (-5 Ruby)!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dlg.startYesNo();
    }

    public static void send_chat(Player p, String tab_name, String text, boolean cache)
            throws IOException {
        Message m = new Message(18);
        m.writer().writeUTF(tab_name);
        m.writer().writeUTF(text);
        if (p.conn != null) {
            if (cache) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
        } else if (p.detu != null && p.detu.conn != null) {
            if (cache) {
                p.detu.msgs.add(m);
            } else {
                p.detu.addmsg(m);
            }
        }
        m.cleanup();
    }
}
