package map.zones;

import bot.mercenary.MercenaryBot;
import model.DeTu;
import model.Player;
import network.Message;
import java.io.IOException;

public class Fight {

    public synchronized static void handle(Player p, Message m2) throws IOException {
        if (p == null) return;
        byte type = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte typeFight = 0;
        if (m2.reader().available() > 0) {
            typeFight = m2.reader().readByte(); // 0: Friendly, 1: Bet fight / Siêu hạng
        }
        long betAmount = 0;
        if (typeFight == 1) {
            if (m2.reader().available() > 0) {
                betAmount = m2.reader().readInt();
            } else {
                betAmount = 10000;
            }
            if (betAmount < 0) betAmount = 0;
        }

        // Chặn Đệ tử và Lính đánh thuê tự gửi thách đấu
        if (p.isDe || p instanceof DeTu || p instanceof MercenaryBot) {
            p.getService().send_box_ThongBao_OK("Đệ tử và lính đánh thuê không thể tham gia thách đấu!");
            return;
        }

        if (type == 0) { // Gửi lời mời thách đấu
            Player p0 = (p.map != null) ? p.map.get_player_by_id_inmap(id) : null;
            if (p0 == null && p.map != null) {
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player pCheck = p.map.players.get(i);
                    if (pCheck != null && (pCheck.index_map == id || pCheck.IDPlayer == id)) {
                        p0 = pCheck;
                        break;
                    }
                }
            }
            if (p0 == null || p0.equals(p)) {
                p.getService().send_box_ThongBao_OK("Đối phương offline hoặc không tìm thấy!");
                return;
            }

            // Chặn thách đấu với Đệ tử và Lính đánh thuê
            if (p0.isDe || p0 instanceof DeTu || p0 instanceof MercenaryBot) {
                p.getService().send_box_ThongBao_OK("Không thể thách đấu với đệ tử hoặc lính đánh thuê!");
                return;
            }

            if (p0.targetFight != null && p0.targetFight.conn != null && !p0.targetFight.equals(p)) {
                p.getService().send_box_ThongBao_OK("Đối phương đang nhận lời mời từ người khác");
                return;
            }

            // Kiểm tra beri đặt cược của người mời (p)
            if (typeFight == 1 && p.get_vang() < betAmount) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ beri để đặt cược!");
                return;
            }

            // Xử lý Thách đấu với Bot Player
            if (p0.isBot) {
                // Kiểm tra beri của bot player nếu là trận cược
                if (typeFight == 1 && p0.get_vang() < betAmount) {
                    p.getService().send_box_ThongBao_OK("Đối thủ không đủ beri để đặt cược!");
                    return;
                }

                // Bot Player đủ điều kiện tự động chấp nhận thách đấu
                p.targetFight = p0;
                p0.targetFight = p;
                p.betAmountFight = betAmount;
                p0.betAmountFight = betAmount;
                p.type_pk = -1;
                p0.type_pk = -1;

                p.save_previous_map();
                p0.save_previous_map();

                MapPvp pvp = MapPvp.createDungeon(p, p0, (byte) (typeFight == 1 ? 3 : 1));
                pvp.join();
                return;
            }

            // Gửi lời mời đến Người chơi thật (p0)
            Message m = new Message(-35);
            m.writer().writeByte(0);
            m.writer().writeShort(p.index_map); // Challenger index_map
            m.writer().writeUTF(p.name);
            m.writer().writeShort(99);
            m.writer().writeByte(typeFight);
            p0.addmsg(m);
            m.cleanup();

            p0.targetFight = p;
            p.targetFight = p0;
            p.betAmountFight = betAmount;
            p0.betAmountFight = betAmount;

            p.getService().send_box_ThongBao_OK("Đã gửi lời mời thách đấu tới " + p0.name);

        } else if (type == 1) { // Người chơi (p) chấp nhận lời mời thách đấu (từ bảng Sự kiện hoặc bảng Thông tin MsgOtherCharInfo)
            Player p_challenger = p.targetFight;
            if (p_challenger == null && p.map != null) {
                p_challenger = p.map.get_player_by_id_inmap(id);
                if (p_challenger == null) {
                    for (int i = 0; i < p.map.players.size(); i++) {
                        Player pCheck = p.map.players.get(i);
                        if (pCheck != null && (pCheck.index_map == id || pCheck.IDPlayer == id)) {
                            p_challenger = pCheck;
                            break;
                        }
                    }
                }
            }

            if (p_challenger == null || p_challenger.conn == null || p_challenger.map == null || !p_challenger.map.equals(p.map)) {
                p.targetFight = null;
                p.getService().send_box_ThongBao_OK("Đối phương đã offline hoặc rời khỏi khu vực!");
                return;
            }

            long bet = (p_challenger.betAmountFight > 0) ? p_challenger.betAmountFight : p.betAmountFight;
            if (bet > 0) {
                if (p.get_vang() < bet) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ beri để cược!");
                    if (p_challenger.getService() != null) {
                        p_challenger.getService().send_box_ThongBao_OK("Đối thủ không đủ beri để cược!");
                    }
                    p_challenger.targetFight = null;
                    p.targetFight = null;
                    return;
                }
                if (p_challenger.get_vang() < bet) {
                    p.getService().send_box_ThongBao_OK("Đối thủ không đủ beri để cược!");
                    if (p_challenger.getService() != null) {
                        p_challenger.getService().send_box_ThongBao_OK("Bạn không đủ beri để cược!");
                    }
                    p_challenger.targetFight = null;
                    p.targetFight = null;
                    return;
                }
            }

            p.targetFight = p_challenger;
            p_challenger.targetFight = p;
            p.betAmountFight = bet;
            p_challenger.betAmountFight = bet;
            p.type_pk = -1;
            p_challenger.type_pk = -1;

            p.save_previous_map();
            p_challenger.save_previous_map();

            byte mode = (byte) (bet > 0 ? 3 : 1);
            MapPvp pvp = MapPvp.createDungeon(p_challenger, p, mode);
            pvp.join();
        }
    }
}
