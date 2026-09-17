package clan;

import event.EventManager;

import model.Buff;
import model.Clazz;
import model.Player;
import core.Manager;
import core.ZUtil;
import event.SuKienTrongCay;
import mob.Mob;
import map.Zone;
import map.zones.ChiemDao;
import map.zones.PvpBang;
import network.Message;
import org.joda.time.LocalTime;
import template.EffTemplate;
import template.ItemBag47;
import template.ItemTemplate8;
import template.MobTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClanService {

    // =========================================================================
    // DISPATCHER - mỗi type gọi đúng 1 hàm handler
    // =========================================================================

    public static void process(Player p, Message m2, int type) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng Băng hải tặc!");
            return;
        }
        switch (type) {
            case 0:  cmd_chat(p, m2);           break;
            case 1:  cmd_truc_xuat(p, m2);      break;
            case 2:  cmd_tang_qua(p, m2);       break;
            case 3:  cmd_phong_chuc(p, m2);     break;
            case 4:  cmd_roi_bang(p);            break;
            case 5:  cmd_thong_bao(p, m2);      break;
            case 6:  cmd_nang_tiem_nang(p, m2); break;
            case 7:  cmd_chap_nhan_xin(p, m2);  break;
            case 9:  cmd_refresh(p);             break;
            case 10: cmd_moi_vao_bang(p, m2);   break;
            case 11: cmd_xin_vao_bang(p, m2);   break;
            case 12: cmd_chap_nhan_moi(p, m2);  break;
            case 13: cmd_nang_cap_bang(p);       break;
            case 14: cmd_dung_item(p, m2);       break;
            case 15: cmd_dong_gop_dialog(p);     break;
            case 16: cmd_tu_choi_xin(p, m2);    break;
            case 17: cmd_update_list_mem(p);     break;
        }
    }

    // =========================================================================
    // HANDLERS - 1 cmd = 1 hàm
    // =========================================================================

    /** case 0 - Chat bang */
    private static void cmd_chat(Player p, Message m2) throws IOException {
        String strChat = m2.reader().readUTF();
        m2.reader().readByte();
        ClanMember me = findMember(p.clan, p.name);
        if (me == null) return;
        ClanChat chat = new ClanChat();
        chat.idMem    = me.id;
        chat.name     = p.name;
        chat.str      = strChat;
        chat.time     = System.currentTimeMillis();
        chat.typeChat = (byte) (me.levelInclan == 0 ? -1 : -4);
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
    }

    /** case 1 - Trục xuất thành viên */
    private static void cmd_truc_xuat(Player p, Message m2) throws IOException {
        String targetName = m2.reader().readUTF();
        byte   chucVu     = m2.reader().readByte();
        if (chucVu != 0) return;
        if (findMemberByRole(p.clan, p.name, 0, 1) == null) return;

        ClanMember mem = findMember(p.clan, targetName);
        if (mem == null) return;
        if (mem.levelInclan == 0) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện chức năng này!");
            return;
        }

        p.clan.members.remove(mem);

        Player kicked = Zone.get_player_by_name_allmap(mem.name);
        if (kicked != null) {
            Clan.send_info(kicked, false);
            for (int i = 0; i < p.map.players.size(); i++) {
                if (!p.map.players.get(i).equals(kicked))
                    Clan.send_me_to_other(kicked, p.map.players.get(i), false);
            }
            kicked.getService().send_box_ThongBao_OK("Bạn bị trục xuất khỏi băng " + p.clan.name);
            Message m = new Message(-52);
            m.writer().writeByte(10);
            m.writer().writeShort(kicked.index_map);
            p.map.send_msg_all_p(m, kicked, true);
            m.cleanup();
            kicked.clan = null;
        }

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) {
                Clan.update_list_member(pt, false);
                Clan.send_info(pt, false);
                Message mOut = new Message(-52);
                mOut.writer().writeByte(12);
                mOut.writer().writeByte(1);
                mOut.writer().writeUTF(mem.name);
                pt.addmsg(mOut);
                mOut.cleanup();
            }
        }

        ClanChat chat = new ClanChat();
        chat.idMem    = p.clan.members.get(0).id;
        chat.name     = p.name;
        chat.str      = mem.name + " bị trục xuất khỏi băng";
        chat.time     = System.currentTimeMillis();
        chat.typeChat = -2;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
        p.getService().send_box_ThongBao_OK(mem.name + " bị trục xuất khỏi băng thành công");
    }

    /** case 2 - Tặng quà */
    private static void cmd_tang_qua(Player p, Message m2) throws IOException {
        String nameTarget = m2.reader().readUTF();
        m2.reader().readByte();
        if (nameTarget.equals(p.name)) return;

        if (Clan.HM_TIME_GIFT.containsKey(p.name)) {
            long cdTime = Clan.HM_TIME_GIFT.get(p.name);
            if (cdTime > System.currentTimeMillis()) {
                p.getService().send_box_ThongBao_OK("Chỉ có thể tặng quà sau "
                        + ZUtil.get_time_str_by_sec2(cdTime - System.currentTimeMillis()));
                return;
            }
        }

        ClanMember me = null, memTarget = null;
        for (int i = 0; i < p.clan.members.size(); i++) {
            ClanMember cm = p.clan.members.get(i);
            if (cm.name.equals(nameTarget)) memTarget = cm;
            if (cm.name.equals(p.name))     me        = cm;
        }
        if (memTarget == null) return;

        Player pTarget = Zone.get_player_by_name_allmap(memTarget.name);
        if (pTarget == null) {
            p.getService().send_box_ThongBao_OK("Đối phương offline");
            return;
        }
        if (pTarget.isBot || pTarget.isDe || pTarget instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Không thể tặng ruby cho lính đánh thuê hoặc đệ tử!");
            return;
        }

        Clan.HM_TIME_GIFT.put(p.name, System.currentTimeMillis() + 60_000L * 60 * 8);
        int rubyAdd = ZUtil.random(1, 6);
        pTarget.update_ngoc(rubyAdd);
        pTarget.updateMoney();
        pTarget.getService().send_box_ThongBao_OK(p.name + " trong băng của bạn tặng bạn " + rubyAdd + " ruby");
        p.getService().send_box_ThongBao_OK("Tặng " + rubyAdd + " ruby cho " + pTarget.name + " thành công");

        // Gửi packet cooldown + donate mới
        Message mCd = new Message(-52);
        mCd.writer().writeByte(13);
        mCd.writer().writeInt((int)((Clan.HM_TIME_GIFT.get(p.name) - System.currentTimeMillis()) / 1_000));
        for (int i = 0; i < p.clan.members.size(); i++) {
            if (p.clan.members.get(i).name.equals(p.name)) {
                p.clan.members.get(i).donate++;
                mCd.writer().writeInt(p.clan.members.get(i).donate);
                break;
            }
        }
        p.addmsg(mCd);
        mCd.cleanup();

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) Clan.update_list_member(pt, false);
        }

        ClanChat chat = new ClanChat();
        assert me != null;
        chat.idMem    = me.id;
        chat.name     = me.name;
        chat.str      = "Tặng " + memTarget.name + " " + rubyAdd + " ruby";
        chat.time     = System.currentTimeMillis();
        chat.typeChat = -4;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
    }

    /** case 3 - Phong chức */
    private static void cmd_phong_chuc(Player p, Message m2) throws IOException {
        String targetName = m2.reader().readUTF();
        byte   chucVu     = m2.reader().readByte();

        if (chucVu == 0 || findMemberByRole(p.clan, p.name, 0, 1) == null) {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
            return;
        }

        ClanMember mem = findMember(p.clan, targetName);
        if (mem == null) return;
        if (mem.levelInclan == 0) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện chức năng này!");
            return;
        }
        if (mem.levelInclan == chucVu) {
            p.getService().send_box_ThongBao_OK("Đối phương đã có chức vụ này");
            return;
        }

        int numTP = 0, numHT = 0;
        for (int i = 0; i < p.clan.members.size(); i++) {
            if (p.clan.members.get(i).levelInclan == 1) numTP++;
            if (p.clan.members.get(i).levelInclan == 2) numHT++;
        }
        if (chucVu == 1 && numTP >= 2) {
            p.getService().send_box_ThongBao_OK("Trong băng chỉ có thể tối đa 2 thuyền phó!");
            return;
        }
        if (chucVu == 2 && numHT >= 1) {
            p.getService().send_box_ThongBao_OK("Trong băng chỉ có thể tối đa 1 hoa tiêu!");
            return;
        }

        mem.levelInclan = chucVu;
        String tenCV = mem.levelInclan == 1 ? "Thuyền phó"
                     : mem.levelInclan == 2 ? "Hoa tiêu" : "thuyền viên";

        Player p0 = Zone.get_player_by_name_allmap(mem.name);
        if (p0 != null) {
            Clan.send_info(p0, false);
            for (int i = 0; i < p.map.players.size(); i++) {
                if (!p.map.players.get(i).equals(p0))
                    Clan.send_me_to_other(p0, p.map.players.get(i), false);
            }
            p0.getService().send_box_ThongBao_OK("Bạn được phong thành " + tenCV);
        }

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) {
                Clan.update_list_member(pt, false);
                Clan.send_info(pt, false);
            }
        }

        ClanChat chat = new ClanChat();
        chat.idMem    = p.clan.members.get(0).id;
        chat.name     = p.name;
        chat.str      = "phong chức " + mem.name + " thành " + tenCV;
        chat.time     = System.currentTimeMillis();
        chat.typeChat = -3;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
        p.getService().send_box_ThongBao_OK("Phong chức " + mem.name + " thành " + tenCV + " thành công");
    }

    /** case 4 - Rời băng */
    private static void cmd_roi_bang(Player p) throws IOException {
        int timeH = LocalTime.now().getHourOfDay();
        if ((ZUtil.is_DayofWeek(2) || ZUtil.is_DayofWeek(4) || ZUtil.is_DayofWeek(6)) && timeH == 21) {
            p.getService().send_box_ThongBao_OK("Đang trong giờ phó bản khổng lồ không thể rời băng vào lúc 19-20h");
            return;
        }
        if (p.clan == null) return;

        clan.Clan clanRef = p.clan;
        String clanName = clanRef.name;

        // Xóa mình khỏi danh sách thành viên
        for (int i = 0; i < clanRef.members.size(); i++) {
            if (clanRef.members.get(i).name.equals(p.name)) {
                clanRef.members.remove(i);
                break;
            }
        }

        // Tách p.clan = null TRƯỚC khi send để client hiểu mình không còn trong bang
        p.clan = null;

        // Gửi lại info cho bản thân (clan == null  client xóa UI băng)
        Clan.send_info(p, false);

        // Cập nhật avatar mình trên map (không còn icon băng)
        for (int i = 0; i < p.map.players.size(); i++) {
            if (!p.map.players.get(i).equals(p))
                Clan.send_me_to_other(p, p.map.players.get(i), false);
        }

        p.getService().send_box_ThongBao_OK("Rời băng " + clanName + " thành công");

        if (clanRef.members.isEmpty()) {
            // Băng không còn ai  giải tán hoàn toàn
            int deletedId = clanRef.id;
            Clan.delete_clan(clanRef);
            new Thread(() -> Clan.delete_clan_db(deletedId)).start();
        } else {
            // Vẫn còn thành viên  thông báo cho họ
            for (int i = 0; i < clanRef.members.size(); i++) {
                Player pt = Zone.get_player_by_name_allmap(clanRef.members.get(i).name);
                if (pt != null) {
                    Clan.update_list_member(pt, false);
                    Clan.send_info(pt, false);
                    Message mOut = new Message(-52);
                    mOut.writer().writeByte(12);
                    mOut.writer().writeByte(1);
                    mOut.writer().writeUTF(p.name);
                    pt.addmsg(mOut);
                    mOut.cleanup();
                }
            }

            // Gửi chat hệ thống trong băng
            ClanChat chat = new ClanChat();
            chat.idMem    = clanRef.members.get(0).id;
            chat.name     = clanRef.members.get(0).name;
            chat.str      = p.name + " rời băng";
            chat.time     = System.currentTimeMillis();
            chat.typeChat = -2;
            clanRef.add_chat(chat);
            clanRef.send_chat(chat, null);
        }

        // Cập nhật icon băng trên map cho mọi người (bao gồm bản thân)
        Message mMap = new Message(-52);
        mMap.writer().writeByte(10);
        mMap.writer().writeShort(p.index_map);
        p.map.send_msg_all_p(mMap, p, true);
        mMap.cleanup();
    }

    /** case 5 - Thay đổi thông báo băng */
    private static void cmd_thong_bao(Player p, Message m2) throws IOException {
        String strNotice = m2.reader().readUTF();
        m2.reader().readByte();
        if (!p.clan.members.get(0).name.equals(p.name)) return;

        p.clan.thongbao = strNotice;
        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) Clan.send_notice(pt, false);
        }

        ClanChat chat = new ClanChat();
        chat.idMem    = p.clan.members.get(0).id;
        chat.name     = p.name;
        chat.str      = strNotice;
        chat.time     = System.currentTimeMillis();
        chat.typeChat = -3;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
    }

    /** case 6 - Nâng tiềm năng băng */
    private static void cmd_nang_tiem_nang(Player p, Message m2) throws IOException {
        byte attrIdx = m2.reader().readByte();
        m2.reader().readByte();
        if (!p.clan.members.get(0).name.equals(p.name)) {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
            return;
        }
        if (p.clan.pointAttri <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ 1 điểm tiềm năng");
            return;
        }
        if ((p.clan.opAttri[attrIdx] + Clan.get_point_trungsinh_plus(p.clan)) >= p.clan.maxAttri) {
            p.getService().send_box_ThongBao_OK("Hiện tại tiềm năng tối đa là " + p.clan.maxAttri);
            return;
        }
        p.clan.pointAttri--;
        p.clan.opAttri[attrIdx]++;
        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) {
                Clan.send_Attri(pt, false);
                pt.update_info_to_all();
            }
        }
    }

    /** case 7 - Chấp nhận yêu cầu xin vào băng */
    private static void cmd_chap_nhan_xin(Player p, Message m2) throws IOException {
        if (findMemberByRole(p.clan, p.name, 0, 2) == null) return;

        m2.reader().readUTF();
        int idChat = m2.reader().readInt();

        ClanChat targetChat = findChatById(p.clan, idChat);
        if (targetChat == null) return;

        ClanMember memReq = p.clan.get_mem_request(targetChat.str.replace(" xin vào băng", ""));
        if (memReq == null) return;

        clearRequestChat(p.clan, targetChat.str);

        int numClazz = 0;
        for (int j = 0; j < p.clan.members.size(); j++) {
            if (p.clan.members.get(j).clazz == memReq.clazz) numClazz++;
        }
        if (numClazz >= 4) {
            p.getService().send_box_ThongBao_OK("Băng đã đầy đủ 4/4 " + Clazz.NAME[memReq.clazz - 1]);
            return;
        }
        if (p.clan.members.size() >= Clan.get_mem_max(p.clan.level, p.clan.trungsinh)) {
            p.getService().send_box_ThongBao_OK("Băng đầy đủ người rồi");
            return;
        }
        if (findMember(p.clan, memReq.name) != null) {
            p.getService().send_box_ThongBao_OK("Đã có mặt trong băng");
            return;
        }

        Player pNew = Zone.get_player_by_name_allmap(memReq.name);
        if (pNew != null && pNew.clan != null) {
            pNew.getService().send_box_ThongBao_OK("Đối phuong đã ở trong băng khác");
            return;
        }
        if (pNew != null) {
            pNew.clan = p.clan;
            memReq.id = (short) ClanMember.get_id(p.clan.members);
            p.clan.members.add(memReq);
            Clan.HM_TIME_GIFT.put(memReq.name, System.currentTimeMillis() + 60_000L * 60 * 8);
            Clan.send_info(pNew, false);
            for (int i = 0; i < p.map.players.size(); i++) {
                if (!p.map.players.get(i).equals(pNew))
                    Clan.send_me_to_other(pNew, p.map.players.get(i), false);
            }
            pNew.getService().send_box_ThongBao_OK("Tham gia băng hải tặc " + p.clan.name + " thành công");
        }

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) {
                Clan.update_list_member(pt, false);
                Clan.send_info(pt, false);
            }
        }
        p.clan.mem_request.remove(memReq);
    }

    /** case 9 - Refresh dữ liệu băng */
    private static void cmd_refresh(Player p) throws IOException {
        Clan.set_data(p, false);
        Clan.send_Attri(p, false);
        Clan.update_list_member(p, false);
        for (int i = 0; i < p.clan.chat.size(); i++) {
            p.clan.send_chat(p.clan.chat.get(i), p);
        }
    }

    /** case 10 - Gửi lời mời vào băng (cùng map) */
    private static void cmd_moi_vao_bang(Player p, Message m2) throws IOException {
        int targetId = m2.reader().readInt();
        Player pTarget = p.map.get_player_by_id_inmap(targetId);
        if (pTarget == null) {
            p.getService().send_box_ThongBao_OK("Nhân vật hiện đang offline hoặc không ở trong map");
            return;
        }
        Message m = new Message(-52);
        m.writer().writeByte(7);
        m.writer().writeInt(p.index_map);
        m.writer().writeUTF(p.name);
        pTarget.addmsg(m);
        m.cleanup();
    }

    /** case 11 - Xin vào băng (từ danh sách) */
    private static void cmd_xin_vao_bang(Player p, Message m2) throws IOException {
        int clanId = m2.reader().readInt();
        Clan clan  = Clan.get_clan_by_id(clanId);
        if (clan == null) return;

        if (clan.allowRequest == 0) {
            p.getService().send_box_ThongBao_OK("Băng này đã đầy đủ người");
            return;
        }

        String reqStr  = p.name + " xin vào băng";
        long   cdLeft  = 0;
        boolean canReq = true;
        for (int i = 0; i < clan.chat.size(); i++) {
            ClanChat cc = clan.chat.get(i);
            if (cc.typeChat == 1 && cc.str.equals(reqStr)
                    && (System.currentTimeMillis() - cc.time) < 600_000L) {
                cdLeft = 600_000L - (System.currentTimeMillis() - cc.time);
                canReq = false;
                break;
            }
        }
        if (!canReq) {
            p.getService().send_box_ThongBao_OK("Xin vào sau " + (cdLeft / 1000) + "s nữa");
            return;
        }

        ClanMember memReq = null;
        for (int i = 0; i < clan.mem_request.size(); i++) {
            if (clan.mem_request.get(i).name.equals(p.name)) {
                memReq = clan.mem_request.get(i);
                break;
            }
        }
        if (memReq == null) {
            memReq              = new ClanMember();
            memReq.playerId     = p.IDPlayer;
            memReq.name         = p.name;
            memReq.conghien     = 0;
            memReq.donate       = 0;
            memReq.gopRuby      = 0;
            memReq.numquest     = 3;
            memReq.id           = 0;
            memReq.hair         = (short) p.get_hair();
            memReq.head         = (short) p.get_head();
            memReq.hat          = p.get_hat();
            memReq.level        = p.level;
            memReq.levelInclan  = 10;
            memReq.clazz        = p.clazz;
            memReq.timeJoinClan = System.currentTimeMillis();
            Clan.HM_TIME_GIFT.put(memReq.name, System.currentTimeMillis() + 60_000L * 60 * 8);
            clan.mem_request.add(memReq);
        }

        ClanChat chat = new ClanChat();
        chat.idMem    = clan.members.get(0).id;
        chat.name     = clan.members.get(0).name;
        chat.str      = reqStr;
        chat.time     = System.currentTimeMillis();
        chat.typeChat = 1;
        clan.add_chat(chat);
        clan.send_chat(chat, null);
    }

    /** case 12 - Chấp nhận lời mời vào băng (cùng map) */
    private static void cmd_chap_nhan_moi(Player p, Message m2) throws IOException {
        if (p.clan != null) {
            p.getService().send_box_ThongBao_OK("Bạn đang ở trong 1 băng hải tặc khác");
            return;
        }

        int inviterId = m2.reader().readInt();
        Player pInviter = p.map.get_player_by_id_inmap(inviterId);
        if (pInviter == null || pInviter.clan == null) {
            p.getService().send_box_ThongBao_OK("Nhân vật hiện đang offline hoặc không ở trong map");
            return;
        }
        if (pInviter.clan.members.size() >= Clan.get_mem_max(pInviter.clan.level, pInviter.clan.trungsinh)) {
            p.getService().send_box_ThongBao_OK("Băng hải tặc đã này đủ người");
            return;
        }
        if (findMemberByRole(pInviter.clan, pInviter.name, 0, 2) == null) {
            p.getService().send_box_ThongBao_OK("Đối phương không phải là đội trưởng");
            return;
        }

        int numClazz = 0;
        for (int j = 0; j < pInviter.clan.members.size(); j++) {
            if (pInviter.clan.members.get(j).clazz == p.clazz) numClazz++;
        }
        if (numClazz >= 4) {
            p.getService().send_box_ThongBao_OK("Băng đã đầy đủ 4/4 " + Clazz.NAME[p.clazz - 1]);
            return;
        }
        if (findMember(pInviter.clan, p.name) != null) {
            p.getService().send_box_ThongBao_OK("Đã có mặt trong băng");
            return;
        }

        p.clan = pInviter.clan;
        ClanMember newMem   = new ClanMember();
        newMem.playerId     = p.IDPlayer;
        newMem.name         = p.name;
        newMem.conghien     = 0;
        newMem.donate       = 0;
        newMem.gopRuby      = 32_000;
        newMem.numquest     = 3;
        newMem.id           = (short) ClanMember.get_id(pInviter.clan.members);
        newMem.hair         = (short) p.get_hair();
        newMem.head         = (short) p.get_head();
        newMem.hat          = p.get_hat();
        newMem.level        = p.level;
        newMem.levelInclan  = 10;
        newMem.clazz        = p.clazz;
        newMem.timeJoinClan = System.currentTimeMillis();
        pInviter.clan.members.add(newMem);
        Clan.HM_TIME_GIFT.put(newMem.name, System.currentTimeMillis() + 60_000L * 60 * 8);

        Clan.send_info(p, false);
        for (int i = 0; i < p.map.players.size(); i++) {
            if (!p.map.players.get(i).equals(p))
                Clan.send_me_to_other(p, p.map.players.get(i), false);
        }
        for (int i = 0; i < pInviter.clan.members.size(); i++) {
            if (!pInviter.clan.members.get(i).name.equals(p.name)) {
                Player pt = Zone.get_player_by_name_allmap(pInviter.clan.members.get(i).name);
                if (pt != null) {
                    Clan.update_list_member(pt, false);
                    Clan.send_info(pt, false);
                }
            }
        }
        p.getService().send_box_ThongBao_OK("Tham gia băng hải tặc " + pInviter.clan.name + " thành công");
    }

    /** case 13 - Nâng cấp băng */
    private static void cmd_nang_cap_bang(Player p) throws IOException {
        if (!p.clan.members.get(0).name.equals(p.name)) {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
            return;
        }
        if (p.clan.xp < Clan.get_xp_max(p.clan.level, p.clan.trungsinh)) {
            p.getService().send_box_ThongBao_OK("Chưa đủ điều kiện nâng cấp bang");
            return;
        }
        int ngocCost = Clan.get_ngoc_upgrade(p.clan.level, p.clan.trungsinh);
        int vangCost = Clan.get_vang_upgrade(p.clan.level);
        if (p.clan.get_ngoc() < ngocCost) {
            p.getService().send_box_ThongBao_OK("Cần " + ZUtil.number_format(ngocCost) + " ruby băng để thực hiện nâng cấp");
            return;
        }
        if (p.clan.get_vang() < vangCost) {
            p.getService().send_box_ThongBao_OK("Cần " + ZUtil.number_format(vangCost) + " beri băng để thực hiện nâng cấp");
            return;
        }
        if (p.clan.level >= 14 && p.clan.trungsinh >= 6) {
            p.getService().send_box_ThongBao_OK("Trùng sinh đạt tối đa không thể nâng thêm!");
            return;
        }

        p.clan.update_ruby(-ngocCost);
        p.clan.update_beri(-vangCost);
        p.clan.xp -= Clan.get_xp_max(p.clan.level, p.clan.trungsinh);
        p.clan.level++;
        p.clan.pointAttri += 2;

        if (p.clan.level >= 16) {
            p.clan.level      = 1;
            p.clan.opAttri    = new short[]{0, 0, 0, 0, 0};
            p.clan.pointAttri = 2;
            p.clan.trungsinh++;
            int baseMax = 20 + p.clan.trungsinh * 5;
            if (baseMax > 50) baseMax = 50;
            p.clan.maxAttri = (short) baseMax;
        }

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) Clan.send_info(pt, false);
        }

        if (p.clan.level == 1) {
            p.getService().send_box_ThongBao_OK("Trùng sinh băng thành công lên " + p.clan.trungsinh);
        } else {
            p.getService().send_box_ThongBao_OK("Nâng cấp bang thành công lên cấp " + p.clan.level);
        }
    }

    /** case 14 - Dùng item băng (dispatcher theo itemId) */
    private static void cmd_dung_item(Player p, Message m2) throws IOException {
        short itemId = m2.reader().readShort();
        m2.reader().readByte();
        if (!p.clan.members.get(0).name.equals(p.name)) {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
            return;
        }
        ItemBag47 itSelect = null;
        for (int i = 0; i < p.clan.list_it.size(); i++) {
            if (p.clan.list_it.get(i).id == itemId && p.clan.list_it.get(i).quant > 0) {
                itSelect = p.clan.list_it.get(i);
                break;
            }
        }
        if (itSelect == null) {
            p.getService().send_box_ThongBao_OK("Không đủ 1 " + ItemTemplate8.getItemName(itemId) + " trong hành trang băng");
            return;
        }
        switch (itemId) {
            case 0:  item_bua_exp(p, itSelect, 0, 100, "bùa kinh nghiệm lv1"); break;
            case 1:  item_bua_exp(p, itSelect, 1, 100, "bùa kinh nghiệm lv2"); break;
            case 2:  item_bua_exp(p, itSelect, 2, 25,  "bùa hp");              break;
            case 3:  item_bua_exp(p, itSelect, 3, 25,  "bùa mp");              break;
            case 4:  item_bua_exp(p, itSelect, 4, 25,  "bùa tổng hợp");       break;
            case 6:  item_beri(p, itSelect);         break;
            case 7:  item_tay_tiem_nang(p, itSelect);break;
            case 8:  item_ve_pvp(p, itSelect);       break;
            case 11: item_bingu(p, itSelect);        break;
            case 17: item_chiem_dao(p, itSelect, 17);break;
            case 18: item_chiem_dao(p, itSelect, 18);break;
            case 19: item_chiem_dao(p, itSelect, 19);break;
            case 20: item_chiem_dao(p, itSelect, 20);break;
            case 21: item_hat_giong(p, itSelect);    break;
            default:
                p.getService().send_box_ThongBao_OK("Hiện tại "
                        + ItemTemplate8.getItemName(itemId)
                        + " chưa thể sử dụng, đợi mình 1 thời gian nữa sẽ cập nhật sớm nhất nha");
        }
    }

    /** case 15 - Mở dialog đóng góp ruby */
    private static void cmd_dong_gop_dialog(Player p) throws IOException {
        new model.InputDialog(p, 11, "Đóng góp băng", new String[]{"Nhập số ruby muốn góp"}).startInput();
    }

    /** case 16 - Từ chối yêu cầu xin vào */
    private static void cmd_tu_choi_xin(Player p, Message m2) throws IOException {
        if (findMemberByRole(p.clan, p.name, 0, 2) == null) return;

        m2.reader().readUTF();
        int idChat = m2.reader().readInt();

        ClanChat targetChat = findChatById(p.clan, idChat);
        if (targetChat == null) return;

        ClanMember memReq = p.clan.get_mem_request(targetChat.str.replace(" xin vào băng", ""));
        if (memReq == null) return;

        clearRequestChat(p.clan, targetChat.str);
        p.clan.mem_request.remove(memReq);
    }

    /** case 17 - Cập nhật danh sách thành viên */
    private static void cmd_update_list_mem(Player p) throws IOException {
        Clan.update_list_member(p, false);
    }

    // =========================================================================
    // ITEM HANDLERS - mỗi loại item 1 hàm riêng
    // =========================================================================

    /** Bùa exp/hp/mp/tổng hợp - dùng chung vì logic giống nhau */
    private static void item_bua_exp(Player p, ItemBag47 it, int buffId, int param, String tenBua) throws IOException {
        long now = System.currentTimeMillis();
        EffTemplate eff = findOrCreateBuff(p.clan, buffId, param);
        if (eff.time > now) eff.time += 60_000L * 60;
        else                eff.time  = now + 60_000L * 60;

        ClanChat chat = new ClanChat();
        chat.idMem    = p.clan.members.get(0).id;
        chat.name     = p.clan.members.get(0).name;
        chat.str      = "sử dụng " + tenBua + " tác dụng còn " + ((eff.time - now) / 1000) + "s";
        chat.time     = now;
        chat.typeChat = -3;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);

        consumeItem(p, it);
        broadcastInfo(p.clan);
    }

    /** Item 6 - Túi beri băng */
    private static void item_beri(Player p, ItemBag47 it) throws IOException {
        consumeItem(p, it);
        p.clan.update_beri(10_000);
        broadcastInfo(p.clan);
    }

    /** Item 7 - Tẩy tiềm năng băng */
    private static void item_tay_tiem_nang(Player p, ItemBag47 it) throws IOException {
        consumeItem(p, it);
        p.clan.pointAttri = 0;
        for (int i = 1; i <= p.clan.level; i++) p.clan.pointAttri += 2;
        p.clan.opAttri = new short[]{0, 0, 0, 0, 0};
        broadcastInfo(p.clan);
        p.getService().send_box_ThongBao_OK("Tẩy tiềm năng băng thành công");
    }

    /** Item 8 - Vé PVP băng */
    private static void item_ve_pvp(Player p, ItemBag47 it) throws IOException {
        if (p.clan.numPvp <= 0) {
            p.getService().send_box_ThongBao_OK("Hôm nay đã hết lượt sử dụng vé pvp băng");
            return;
        }
        p.clan.numPvp--;
        consumeItem(p, it);
        broadcastInfo(p.clan);
        PvpBang.buyTicket(p.clan);
        p.getService().send_box_ThongBao_OK("Số lượt pvp băng hiện tại: " + (5 - PvpBang.getTicket(p.clan)));
    }

    /** Item 11 - Triệu hồi bí ngô (event Halloween) */
    private static void item_bingu(Player p, ItemBag47 it) throws IOException {
        if (!event.EventManager.isActive(3)) return;
        if (p.map.template.id > 48 || p.map.zone_id > 4 || p.map.list_mob.length < 5 || p.map.isMapSea()) {
            p.getService().send_box_ThongBao_OK("Vị trí không phù hợp. Bạn chỉ có thể gọi bí ngô (Từ Làng Cối Xoay Gió đến Thị Trấn Khởi Đầu) ở khu từ 1-5");
            return;
        }
        int slotIdx = -1;
        for (int i = 0; i < p.clan.mob1.length; i++) {
            if (p.clan.mob1[i] == null) { slotIdx = i; break; }
        }
        if (slotIdx == -1) {
            p.getService().send_box_ThongBao_OK("Đã triệu hồi tôi đa 10 bí ngô");
            return;
        }
        consumeItem(p, it);
        broadcastInfo(p.clan);
        Mob mob             = new Mob();
        mob.mtemplate    = MobTemplate.ENTRYS.get(171);
        mob.x               = p.x;
        mob.y               = p.y;
        mob.hp_max          = 50;
        mob.hp              = mob.hp_max;
        mob.level           = 55;
        mob.isdie           = false;
        mob.id_target       = -1;
        mob.index           = -2 - slotIdx;
        mob.map             = p.map;
        mob.boss_inf        = null;
        p.clan.mob1[slotIdx] = mob;
        p.map.getService().move(mob.index, mob.x, mob.y);
    }

    /** Item 17/18/19/20 - Chiếm đảo (tấn công/debuff/buff) */
    private static void item_chiem_dao(Player p, ItemBag47 it, int itemId) throws IOException {
        if (p.clan.cdItem >= System.currentTimeMillis()) {
            p.getService().send_box_ThongBao_OK("Sử dụng sau "
                    + ((p.clan.cdItem - System.currentTimeMillis()) / 1_000) + "s nữa");
            return;
        }
        p.clan.cdItem = System.currentTimeMillis() + 60_000;
        if (!p.clan.equals(ChiemDao.getClanTop(p.map.template.id))) {
            p.getService().send_box_ThongBao_OK("Băng chưa chiếm được đảo");
            return;
        }
        consumeItem(p, it);
        broadcastInfo(p.clan);
        if (p.map.template.id >= 261 && p.map.template.id <= 265) {
            for (int i = 0; i < p.map.players.size(); i++) {
                Player pt      = p.map.players.get(i);
                if (pt == null || pt.equals(p) || pt.isdie) continue;
                boolean isEnemy = (pt.clan != null && !p.clan.equals(pt.clan)) || p.canAttackTargetPlayer(pt);
                if (itemId == 17 && isEnemy) { p.map.getService().send_choang(p, pt, 10_000); pt.add_new_eff(201, 1, 10_000); }
                if (itemId == 18 && isEnemy)   pt.add_new_eff(23, 50, 15_000);
                if (itemId == 19 && !isEnemy)  pt.add_new_eff(24, 1,  15_000);
                if (itemId == 20 && isEnemy)   pt.add_new_eff(25, 1,  15_000);
            }
        }
        p.getService().send_box_ThongBao_OK("Sử dụng thành công");
    }

    /** Item 21 - Gieo hạt giống (event Trồng Cây) */
    private static void item_hat_giong(Player p, ItemBag47 it) throws IOException {
        if (!event.EventManager.isActive(9)) return;
        if (p.map.template.id > 48 || p.map.zone_id > 4 || p.map.list_mob.length < 5 || p.map.isMapSea()) {
            p.getService().send_box_ThongBao_OK("Vị trí không phù hợp. Bạn chỉ có thể gieo hạt giống bên ngoài các Làng để trồng Cây thần kỳ (Từ Làng Cối Xoay Gió đến Thị Trấn Khởi Đầu) ở khu từ 1-5");
            return;
        }
        model.Tree existTree = SuKienTrongCay.getByName(p.name);
        if (existTree != null) {
            p.getService().send_box_ThongBao_OK("Hiện tại bạn đang trồng 1 cây rồi. Vị trí\n"
                    + existTree.map.template.name + " khu " + (existTree.map.zone_id + 1));
            return;
        }
        model.Tree myCay = new model.Tree();
        myCay.index = SuKienTrongCay.getIndex(p.IDPlayer);
        if (myCay.index == -1) return;

        consumeItem(p, it);
        broadcastInfo(p.clan);

        myCay.setup(p);
        SuKienTrongCay.setByName(p.name, myCay);

        Message mTree = new Message(1);
        mTree.writer().writeByte(2);
        mTree.writer().writeShort(myCay.index);
        mTree.writer().writeShort(myCay.x);
        mTree.writer().writeShort(myCay.y);
        p.map.send_msg_all_p(mTree, null, true);
        mTree.cleanup();

        p.getService().send_box_ThongBao_OK("Gieo hạt thành công. Sau 10 phút có thể thu hoạch, nhớ chăm sóc đều đặn để tránh chết cây. Cây sẽ nảy mầm sau 5 - 10 giây");
        myCay.type = 2;
        Manager.gI().chatKTG(1, "Băng " + p.clan.name + " đã đặt nhẹ Hạt giống băng xuống đất và hét lớn: Có ngon thì đến cướp cây của bố", 0);

        for (int i = 0; i < p.clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
            if (pt != null) pt.map.change_flag(pt, -1);
        }

        ClanChat chat = new ClanChat();
        chat.idMem    = p.clan.members.get(0).id;
        chat.name     = p.name;
        chat.str      = "Hạt giống bang hiện tại ở " + myCay.map.template.name + " khu " + (myCay.map.zone_id + 1);
        chat.time     = System.currentTimeMillis();
        chat.typeChat = -3;
        p.clan.add_chat(chat);
        p.clan.send_chat(chat, null);
    }

    // =========================================================================
    // PRIVATE UTILS
    // =========================================================================

    private static ClanMember findMember(Clan clan, String name) {
        for (int i = 0; i < clan.members.size(); i++) {
            if (clan.members.get(i).name.equals(name)) return clan.members.get(i);
        }
        return null;
    }

    /** Tìm member có tên name và levelInclan trong khoảng [minRole, maxRole] */
    private static ClanMember findMemberByRole(Clan clan, String name, int minRole, int maxRole) {
        for (int i = 0; i < clan.members.size(); i++) {
            ClanMember cm = clan.members.get(i);
            if (cm.name.equals(name) && cm.levelInclan >= minRole && cm.levelInclan <= maxRole)
                return cm;
        }
        return null;
    }

    private static ClanChat findChatById(Clan clan, int idChat) {
        for (int i = 0; i < clan.chat.size(); i++) {
            if (clan.chat.get(i).idChat == idChat) return clan.chat.get(i);
        }
        return null;
    }

    /** Xóa toàn bộ chat có nội dung str và gửi packet xóa tới member */
    private static void clearRequestChat(Clan clan, String str) throws IOException {
        List<ClanChat> toRemove = new ArrayList<>();
        for (int i = 0; i < clan.chat.size(); i++) {
            if (clan.chat.get(i).str.equals(str)) toRemove.add(clan.chat.get(i));
        }
        for (int i = 0; i < toRemove.size(); i++) {
            for (int j = 0; j < clan.members.size(); j++) {
                Player pt = Zone.get_player_by_name_allmap(clan.members.get(j).name);
                if (pt != null) {
                    Message m = new Message(-52);
                    m.writer().writeByte(11);
                    m.writer().writeShort(toRemove.get(i).idChat);
                    pt.addmsg(m);
                    m.cleanup();
                }
            }
        }
        clan.chat.removeAll(toRemove);
    }

    private static EffTemplate findOrCreateBuff(Clan clan, int buffId, int param) {
        if (clan != null && clan.buff != null) {
            for (EffTemplate b : clan.buff) {
                if (b != null && b.id == buffId) return b;
            }
            EffTemplate eff = new EffTemplate(buffId, param, 0);
            clan.buff.add(eff);
            return eff;
        }
        return null;
    }

    private static void consumeItem(Player p, ItemBag47 it) {
        if (Manager.gI().isTestMode()) {
            it.quant = 9999;
            return;
        }
        it.quant--;
        if (it.quant <= 0) p.clan.list_it.remove(it);
    }

    public static void broadcastInfo(Clan clan) throws IOException {
        for (int i = 0; i < clan.members.size(); i++) {
            Player pt = Zone.get_player_by_name_allmap(clan.members.get(i).name);
            if (pt != null) {
                Clan.send_info(pt, false);
                pt.syncFullPlayerStats();
            }
        }
    }

    public static void processHuyHieuHanhTrinh(Player p, Message m2) throws IOException {
        if (p == null || p.clan == null) {
            return;
        }
        byte act = m2.reader().readByte();
        switch (act) {
            case 1: { // Xem kho huy hiệu (client gửi Send_Type(-95, 1))
                Message m = new Message(-95);
                m.writer().writeByte(1);
                // Fix: đếm số huy hiệu valid trước để tránh count không khớp với dữ liệu thực tế ghi
                java.util.List<ClanHanhTrinhIcon> validIcons = new java.util.ArrayList<>();
                if (p.clan.hanhtrinh != null && p.clan.hanhtrinh.size() > 1) {
                    for (int i = 1; i < p.clan.hanhtrinh.size(); i++) {
                        ClanHanhTrinhIcon temp = ClanHanhTrinhIcon.getById(p.clan.hanhtrinh.get(i));
                        if (temp != null) {
                            validIcons.add(temp);
                        }
                    }
                }
                m.writer().writeShort(validIcons.size());
                for (ClanHanhTrinhIcon temp : validIcons) {
                    m.writer().writeShort(temp.id);
                    m.writer().writeUTF(temp.name != null ? temp.name : "");
                    m.writer().writeUTF(temp.info != null ? temp.info : "");
                    m.writer().writeShort(temp.icon);
                    boolean isEquipped = (p.clan.hanhtrinhIcon != null && p.clan.hanhtrinhIcon.id == temp.id);
                    m.writer().writeByte(isEquipped ? 1 : 0);
                    if (temp.op != null) {
                        m.writer().writeByte(temp.op.size());
                        for (int j = 0; j < temp.op.size(); j++) {
                            template.Option op = temp.op.get(j);
                            if (op != null) {
                                m.writer().writeByte(op.id);
                                int val = op.getParam();
                                if (val < 0) val = 0;
                                if (val > Short.MAX_VALUE) val = Short.MAX_VALUE;
                                m.writer().writeShort(val);
                            } else {
                                m.writer().writeByte(0);
                                m.writer().writeShort(0);
                            }
                        }
                    } else {
                        m.writer().writeByte(0);
                    }
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 2: { // Trang bị / Tháo huy hiệu (client gửi Huy_hieu(2, type, idIcon))
                if (p.clan.members.isEmpty() || !p.clan.members.get(0).name.equals(p.name)) {
                    p.getService().send_box_ThongBao_OK("Chỉ thuyền trưởng mới có quyền thay đổi huy hiệu!");
                    return;
                }
                byte type = m2.reader().readByte();
                short idIcon = m2.reader().readShort();
                if (type == 1) { // Trang bị
                    ClanHanhTrinhIcon iconSelect = null;
                    if (p.clan.hanhtrinh != null) {
                        for (int i = 1; i < p.clan.hanhtrinh.size(); i++) {
                            ClanHanhTrinhIcon temp = ClanHanhTrinhIcon.getById(p.clan.hanhtrinh.get(i));
                            if (temp != null && (temp.icon == idIcon || temp.id == idIcon)) {
                                iconSelect = temp;
                                break;
                            }
                        }
                    }
                    if (iconSelect != null) {
                        p.clan.icon = iconSelect.icon;
                        p.clan.hanhtrinhIcon = iconSelect;
                        broadcastInfo(p.clan);
                        for (int i2 = 0; i2 < p.clan.members.size(); i2++) {
                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i2).name);
                            if (p0 != null && p0.map != null) {
                                for (int i = 0; i < p0.map.players.size(); i++) {
                                    if (!p0.map.players.get(i).equals(p0)) {
                                        Clan.send_me_to_other(p0, p0.map.players.get(i), false);
                                    }
                                }
                            }
                        }
                        Message m = new Message(-95);
                        m.writer().writeByte(2);
                        m.writer().writeShort(iconSelect.icon);
                        p.addmsg(m);
                        m.cleanup();
                        Clan.flush(p.clan);
                    }
                } else if (type == 0) { // Tháo ra
                    p.clan.hanhtrinhIcon = null;
                    broadcastInfo(p.clan);
                    Message m = new Message(-95);
                    m.writer().writeByte(2);
                    m.writer().writeShort(-1);
                    p.addmsg(m);
                    m.cleanup();
                    Clan.flush(p.clan);
                }
                break;
            }
            case 3: { // Quay huy hiệu hành trình (client gửi Huy_hieu(3, type, id))
                if (p.clan.members.isEmpty() || !p.clan.members.get(0).name.equals(p.name)) {
                    p.getService().send_box_ThongBao_OK("Chỉ thuyền trưởng mới có quyền quay huy hiệu!");
                    return;
                }
                byte type = m2.reader().readByte();
                short id = m2.reader().readShort();
                if (type == 1 && id == 580) {
                    if (p.clan.hanhtrinh == null || p.clan.hanhtrinh.isEmpty()) {
                        p.clan.hanhtrinh = new ArrayList<>();
                        p.clan.hanhtrinh.add(0);
                    }
                    int xuHT = p.clan.hanhtrinh.get(0);
                    if (xuHT > 0) {
                        int remainingXu = xuHT - 1;
                        p.clan.hanhtrinh.set(0, remainingXu);

                        boolean suc = core.ZUtil.random(100) < 65;
                        ClanHanhTrinhIcon rd = null;
                        if (suc) {
                            rd = ClanHanhTrinhIcon.getRd();
                        }

                        Message m = new Message(-95);
                        m.writer().writeByte(3);
                        m.writer().writeByte(1);
                        m.writer().writeShort(remainingXu);
                        if (rd != null) {
                            boolean isNew = !p.clan.hanhtrinh.contains(rd.id);
                            if (isNew) {
                                p.clan.hanhtrinh.add(rd.id);
                            }
                            m.writer().writeShort(rd.icon);
                            m.writer().writeByte(4);
                            p.sendAddChatYellow("Băng nhận được huy hiệu: " + rd.name + (isNew ? " (Mới)!" : "!"));
                        } else {
                            m.writer().writeShort(-1);
                        }
                        p.addmsg(m);
                        m.cleanup();

                        Clan.flush(p.clan);
                    } else {
                        p.getService().send_box_ThongBao_OK("Băng không đủ xu hành trình!");
                    }
                }
                break;
            }
        }
    }

    public static void sendClanFightNotice(Player p, String text) {
        if (p == null || p.isBot) return;
        if (p.getService() != null) {
            p.getService().send_clan_fight_notice(text);
        }
    }

    public static void sendClanFightList(Player p) {
        if (p == null || p.isBot) return;
        List<Clan> listClan = new ArrayList<>();
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            Clan c = Clan.ENTRY.get(i);
            if (c != null && (p.clan == null || c.id != p.clan.id)) {
                listClan.add(c);
                if (listClan.size() >= 20) break;
            }
        }
        if (p.getService() != null) {
            p.getService().send_clan_fight_list(listClan);
        }
    }

    public static void processClanFight(Player p, Message m2) throws IOException {
        if (p == null || p.isBot) return;
        byte action = m2.reader().readByte();
        switch (action) {
            case 0: { // Yêu cầu danh sách clan thách đấu
                sendClanFightList(p);
                break;
            }
            case 1: { // Chấp nhận thách đấu
                short idClan = m2.reader().readShort();
                byte typeFight = m2.reader().readByte();
                Clan inviterClan = Clan.get_clan_by_id(idClan);
                if (inviterClan == null || p.clan == null) {
                    sendClanFightNotice(p, "Không tìm thấy thông tin Băng đối phương!");
                    return;
                }
                sendClanFightNotice(p, "Bạn đã chấp nhận thách đấu với Băng " + inviterClan.name + "!");
                for (int i = 0; i < inviterClan.members.size(); i++) {
                    Player pl = Zone.get_player_by_name_allmap(inviterClan.members.get(i).name);
                    if (pl != null) {
                        sendClanFightNotice(pl, "Băng " + p.clan.name + " đã chấp nhận lời thách đấu!");
                    }
                }
                break;
            }
            case 2: { // Từ chối thách đấu
                short idClan = m2.reader().readShort();
                byte typeFight = m2.reader().readByte();
                Clan inviterClan = Clan.get_clan_by_id(idClan);
                if (inviterClan != null && p.clan != null) {
                    for (int i = 0; i < inviterClan.members.size(); i++) {
                        Player pl = Zone.get_player_by_name_allmap(inviterClan.members.get(i).name);
                        if (pl != null) {
                            sendClanFightNotice(pl, "Băng " + p.clan.name + " đã từ chối lời thách đấu!");
                        }
                    }
                }
                break;
            }
            case 3: { // Tự động ghép cặp
                if (p.clan == null) {
                    sendClanFightNotice(p, "Bạn chưa có Băng hải tặc!");
                    return;
                }
                sendClanFightNotice(p, "Đang tìm kiếm đối thủ xứng tầm...");
                break;
            }
            case 5: { // Gửi lời thách đấu tới clan khác
                short targetClanId = m2.reader().readShort();
                byte typeFight = m2.reader().readByte();
                if (p.clan == null) {
                    sendClanFightNotice(p, "Bạn chưa có Băng hải tặc!");
                    return;
                }
                Clan targetClan = Clan.get_clan_by_id(targetClanId);
                if (targetClan == null) {
                    sendClanFightNotice(p, "Băng đối phương không tồn tại!");
                    return;
                }
                Player targetLeader = null;
                for (int i = 0; i < targetClan.members.size(); i++) {
                    ClanMember mem = targetClan.members.get(i);
                    if (mem.levelInclan == 0 || mem.levelInclan == 1) {
                        Player pl = Zone.get_player_by_name_allmap(mem.name);
                        if (pl != null) {
                            targetLeader = pl;
                            break;
                        }
                    }
                }
                if (targetLeader == null) {
                    sendClanFightNotice(p, "Thủ lĩnh/Phó thủ lĩnh Băng đối phương đang offline!");
                    return;
                }
                if (targetLeader.getService() != null) {
                    targetLeader.getService().send_clan_fight_invite(p.clan.id, p.clan.name, p.clan.level, typeFight);
                }

                sendClanFightNotice(p, "Đã gửi lời thách đấu tới Băng " + targetClan.name + "!");
                break;
            }
            case 6: { // Danh sách thành viên tham chiến
                byte count = m2.reader().readByte();
                List<Short> memIds = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    memIds.add(m2.reader().readShort());
                }
                sendClanFightNotice(p, "Đã cập nhật danh sách thành viên tham gia chiến bang!");
                break;
            }
            default:
                break;
        }
    }
}
