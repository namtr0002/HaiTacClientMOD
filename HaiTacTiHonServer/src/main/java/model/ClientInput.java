package model;

import clan.ClanMember;
import clan.Clan;
import event.SuKienHalloween;
import event.SuKienNoel;
import model.DauGia;
import historys.zLog;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import core.Manager;
import core.ZUtil;
import database.DbManager;
import network.Message;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Date;
import map.Zone;
import template.*;

public class ClientInput {

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.isClosed) return;
        try {
            short id = m2.reader().readShort();
//        byte size = m2.reader().readByte();
            String[] name = new String[m2.reader().readByte()];
            for (int i = 0; i < name.length; i++) {
                name[i] = m2.reader().readUTF();
            }
            if (p.inputDialog != null) {
                if (p.inputDialog.hasHandler()) {
                    model.InputDialog dialog = p.inputDialog;
                    p.inputDialog = null;
                    dialog.handle(name);
                    return;
                } else {
                    p.inputDialog = null;
                }
            }
            switch (id) {


            case 124: {
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int numReq = 2 * value;
                            if (p.item.total_item_bag_by_id(4, 207) >= numReq) {
                                p.item.remove_item47(4, 207, numReq);
                                p.item.add_item_bag47(4, 208, value);
                                p.item.updateInventory(false);
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(208));
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ " + numReq + " " + ItemTemplate4.get_item_name(207));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 125: {
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int numReq = 2 * value;
                            if (p.item.total_item_bag_by_id(4, 208) >= numReq) {
                                p.item.remove_item47(4, 208, numReq);
                                p.item.add_item_bag47(4, 209, value);
                                p.item.updateInventory(false);
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(209));
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ " + numReq + " " + ItemTemplate4.get_item_name(208));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 126: {
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int numReq = 2 * value;
                            if (p.item.total_item_bag_by_id(4, 209) >= numReq) {
                                p.item.remove_item47(4, 209, numReq);
                                p.item.add_item_bag47(4, 210, value);
                                p.item.updateInventory(false);
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(210));
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ " + numReq + " " + ItemTemplate4.get_item_name(209));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            
          
          
           
            case -103: { // Mâm Bạc
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int cuaga = 5 * value;
                            int hongmao = 5 * value;
                            int ngavoi = 5 * value;
                           
                            if (p.get_vang() < value * 30_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 30_000 + " vàng");
                                return;
                            }
                             if (p.get_ngoc() < value * 10) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 10 + " vàng");
                                return;
                            }
                            boolean checkitem = true;
                            if (p.item.total_item_bag_by_id(4, 834) < cuaga) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + cuaga + " " + ItemTemplate4.get_item_name(834));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 835) < hongmao) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + hongmao + " " + ItemTemplate4.get_item_name(835));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 836) < ngavoi) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ngavoi + " " + ItemTemplate4.get_item_name(836));
                                checkitem = false;
                            }

                            if (checkitem) {
                                p.update_vang(-value * 30_000);
                                p.update_ngoc(-value * 10);
                                p.item.remove_item47(4, 834, cuaga);
                                p.item.remove_item47(4, 835, hongmao);
                                p.item.remove_item47(4, 836, ngavoi);
                                p.item.add_item_bag47(4, 837, value);
                                p.item.updateInventory(false);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(837));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            
            case -104: { // Mâm Vàng
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int cuaga = 5 * value;
                            int hongmao = 5 * value;
                            int ngavoi = 5 * value;
                           
                            if (p.get_ngoc() < value * 25) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 25 + " vàng");
                                return;
                            }
                             if (p.get_vnd() < value * 25_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 25_000 + " extol");
                                return;
                            }
                            boolean checkitem = true;
                            if (p.item.total_item_bag_by_id(4, 834) < cuaga) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + cuaga + " " + ItemTemplate4.get_item_name(834));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 835) < hongmao) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + hongmao + " " + ItemTemplate4.get_item_name(835));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 836) < ngavoi) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ngavoi + " " + ItemTemplate4.get_item_name(836));
                                checkitem = false;
                            }

                            if (checkitem) {
                                p.update_ngoc(-value * 25);
                                p.updateVnd(-value * 25_000);
                                p.item.remove_item47(4, 834, cuaga);
                                p.item.remove_item47(4, 835, hongmao);
                                p.item.remove_item47(4, 836, ngavoi);
                                p.item.add_item_bag47(4, 838, value);
                                p.item.updateInventory(false);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(838));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            
            case 128: {
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int numReq = 2 * value;
                            int rubyReq = value * 10;
                            long vangReq = (long) value * 5_000L;
                            if (p.get_ngoc() < rubyReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + rubyReq + " ruby");
                                return;
                            }
                            if (p.get_vang() < vangReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + vangReq + " vàng");
                                return;
                            }
                            if (p.item.total_item_bag_by_id(4, 207) < numReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + numReq + " " + ItemTemplate4.get_item_name(207));
                                return;
                            }
                            if (!p.item.can_add_item_bag47(4, 408, value)) {
                                p.getService().send_box_ThongBao_OK("Hành trang đã đầy!");
                                return;
                            }
                            p.update_ngoc(-rubyReq);
                            p.update_vang(-vangReq);
                            p.item.remove_item47(4, 207, numReq);
                            p.item.add_item_bag47(4, 408, value);
                            p.item.updateInventory(false);
                            p.updateMoney();
                            p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(408));
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 129: { // Event 8/3 ( bó hoa hồng )
            if (name.length == 1) {
        if (ZUtil.isnumber(name[0])) {
            int value = Integer.parseInt(name[0]);
            if (value > 0 && value <= 10_000) {
                int hoado = 5 * value; 
                int giaymau = 2 * value;
                int ruybang = 1 * value;
                
                // Kiểm tra nếu người chơi không đủ ngọc
                if (p.get_ngoc() < value * 10) {
                    p.getService().send_box_ThongBao_OK("Không đủ " + value * 10 + " ngọc");
                    return;
                }

                // Kiểm tra nếu người chơi không đủ vàng
                if (p.get_vang() < value * 5_000) {
                    p.getService().send_box_ThongBao_OK("Không đủ " + value * 5_000 + " vàng");
                    return;
                }

                // Kiểm tra nếu người chơi không đủ item trong kho
                boolean checkitem = true;
                if (p.item.total_item_bag_by_id(4, 812) < hoado) {
                    p.getService().send_box_ThongBao_OK("Không đủ " + hoado + " " + ItemTemplate4.get_item_name(812));
                    checkitem = false;
                }
                if (p.item.total_item_bag_by_id(4, 819) < giaymau) {
                    p.getService().send_box_ThongBao_OK("Không đủ " + giaymau + " " + ItemTemplate4.get_item_name(819));
                    checkitem = false;
                }
                if (p.item.total_item_bag_by_id(4, 811) < ruybang) {
                    p.getService().send_box_ThongBao_OK("Không đủ " + ruybang + " " + ItemTemplate4.get_item_name(811));
                    checkitem = false;
                }

                if (checkitem) {
                    // Nếu đủ điều kiện, thực hiện giao dịch
                    p.update_ngoc(-value * 10);
                    p.update_vang(-value * 5_000);
                    p.item.remove_item47(4, 812, hoado);
                    p.item.remove_item47(4, 819, giaymau);
                    p.item.remove_item47(4, 811, ruybang);
                    p.item.add_item_bag47(4, 815, value);
                    p.item.updateInventory(false);
                    p.updateMoney();
                    p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(815));
                }
            } else {
                p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
            }
        }
    }
}
break;
            case 130: { // Event 8/3 ( bó hoa vang )
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int hoavang = 5 * value;
                            int giaymau = 2 * value;
                            int ruybang = 1 * value;

                            // Kiểm tra nếu người chơi không đủ ngọc
                            if (p.get_ngoc() < value * 10) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 10 + " ngọc");
                                return;
                            }

                            // Kiểm tra nếu người chơi không đủ vàng
                            if (p.get_vang() < value * 5_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 5_000 + " vàng");
                                return;
                            }

                            // Kiểm tra nếu người chơi không đủ item trong kho
                            boolean checkitem = true;
                            if (p.item.total_item_bag_by_id(4, 813) < hoavang) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + hoavang + " " + ItemTemplate4.get_item_name(813));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 819) < giaymau) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + giaymau + " " + ItemTemplate4.get_item_name(819));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 811) < ruybang) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ruybang + " " + ItemTemplate4.get_item_name(811));
                                checkitem = false;
                            }

                            if (checkitem) {
                                // Nếu đủ điều kiện, thực hiện giao dịch
                                p.update_ngoc(-value * 10);
                                p.update_vang(-value * 5_000);
                                p.item.remove_item47(4, 813, hoavang);
                                p.item.remove_item47(4, 819, giaymau);
                                p.item.remove_item47(4, 811, ruybang);
                                p.item.add_item_bag47(4, 817, value);
                                p.item.updateInventory(false);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(817));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 131: { // Event 8/3 ( bó hoa xanh )
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int hoaxanh = 5 * value;
                            int giaymau = 2 * value;
                            int ruybang = 1 * value;

                            // Kiểm tra nếu người chơi không đủ ngọc
                            if (p.get_ngoc() < value * 10) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 10 + " ngọc");
                                return;
                            }

                            // Kiểm tra nếu người chơi không đủ vàng
                            if (p.get_vang() < value * 5_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 5_000 + " vàng");
                                return;
                            }

                            // Kiểm tra nếu người chơi không đủ item trong kho
                            boolean checkitem = true;
                            if (p.item.total_item_bag_by_id(4, 814) < hoaxanh) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + hoaxanh + " " + ItemTemplate4.get_item_name(814));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 819) < giaymau) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + giaymau + " " + ItemTemplate4.get_item_name(819));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 811) < ruybang) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + ruybang + " " + ItemTemplate4.get_item_name(811));
                                checkitem = false;
                            }

                            if (checkitem) {
                                // Nếu đủ điều kiện, thực hiện giao dịch
                                p.update_ngoc(-value * 10);
                                p.update_vang(-value * 5_000);
                                p.item.remove_item47(4, 814, hoaxanh);
                                p.item.remove_item47(4, 819, giaymau);
                                p.item.remove_item47(4, 811, ruybang);
                                p.item.add_item_bag47(4, 816, value);
                                p.item.updateInventory(false);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(816));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 132: { // Event 8/3 ( Giỏ hoa )
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int hoahong = 10 * value;
                            int giaymau = 1 * value;
                            int khungtre = 1 * value;

                            // Kiểm tra nếu người chơi không đủ vàng
                            if (p.get_vang() < value * 15_000) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value * 15_000 + " beri");
                                return;
                            }

                            // Kiểm tra nếu người chơi không đủ item trong kho
                            boolean checkitem = true;
                            if (p.item.total_item_bag_by_id(4, 812) < hoahong) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + hoahong + " " + ItemTemplate4.get_item_name(812));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 819) < giaymau) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + giaymau + " " + ItemTemplate4.get_item_name(819));
                                checkitem = false;
                            }
                            if (p.item.total_item_bag_by_id(4, 818) < khungtre) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + khungtre + " " + ItemTemplate4.get_item_name(818));
                                checkitem = false;
                            }

                            if (checkitem) {
                                // Nếu đủ điều kiện, thực hiện giao dịch

                                p.update_vang(-value * 15_000);
                                p.item.remove_item47(4, 812, hoahong);
                                p.item.remove_item47(4, 819, giaymau);
                                p.item.remove_item47(4, 818, khungtre);
                                p.item.add_item_bag47(4, 820, value);
                                p.item.updateInventory(false);
                                p.updateMoney();
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(820));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            case 127: {
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value > 0 && value <= 10_000) {
                            int numReq = 2 * value;
                            if (p.item.total_item_bag_by_id(4, 210) >= numReq) {
                                p.item.remove_item47(4, 210, numReq);
                                p.item.add_item_bag47(4, 407, value);
                                p.item.updateInventory(false);
                                p.getService().send_box_ThongBao_OK("Nhận được " + value + " " + ItemTemplate4.get_item_name(407));
                            } else {
                                p.getService().send_box_ThongBao_OK("Không đủ " + numReq + " " + ItemTemplate4.get_item_name(210));
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
                        }
                    }
                }
            }
            break;
            
            case 23: {
                if (name.length == 2 && (p.admin == 1 || p.getUsername().equals("admin"))) {
                    if (ZUtil.isnumber(name[1])) {
                        try {
                            int value = Integer.parseInt(name[1]);
                            try (Connection conn = DbManager.gI().getConnect();
                                 PreparedStatement ps = conn.prepareStatement("select * from `players` where `name` = ? limit 1")) {
                                ps.setString(1, name[0]);
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        int pId = rs.getInt("id");
                                        int accId = rs.getInt("account_id");
                                        int coinOld = rs.getInt("coin");
                                        try (PreparedStatement psUp = conn.prepareStatement("update `players` set `coin` = `coin` + ? where `name` = ?")) {
                                            psUp.setInt(1, value);
                                            psUp.setString(2, name[0]);
                                            if (psUp.executeUpdate() > 0) {
                                                if (event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025)) {
                                                    addEventRecharge(name[0], value);
                                                } else if (SuKienNoel.gI().isNoel()) {
                                                    addEventRecharge(name[0], value);
                                                }
                                                String old = "Trước: coin: " + coinOld;
                                                String md5Token = historys.RechargeHistory.logRecharge(accId, pId, name[0], "COIN", value, coinOld, (long) coinOld + value, "admin nap tay");
                                                try (PreparedStatement psLog = conn.prepareStatement("INSERT INTO `lichsunap` (`name`, `old`, `value`, `time`, `note`) VALUES (?, ?, ?, ?, ?)")) {
                                                    psLog.setString(1, name[0]);
                                                    psLog.setString(2, old);
                                                    psLog.setInt(3, value);
                                                    psLog.setString(4, (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Date.from(Instant.now())));
                                                    psLog.setString(5, "admin nap tay (MD5: " + md5Token + ")");
                                                    psLog.executeUpdate();
                                                } catch (Exception ignored) {}
                                                p.getService().send_box_ThongBao_OK("Nạp thành công " + value + " coin vào tk " + name[0] + ":\n" + old 
                                                 + "\nMã MD5: " + md5Token
                                                 + (SuKienNoel.gI().isNoel() ? "\n<NOEL EVENT NAP>"  : ""));
                                            } else {
                                                p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
                                            }
                                        }
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Không tìm thấy tài khoản: " + name[0]);
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (NumberFormatException nfe) {
                            p.getService().send_box_ThongBao_OK("Giá trị nạp không hợp lệ!");
                        }
                    }
                }
            }
            break;

            case 25: {
                if (name.length == 2 && (p.admin == 1 || p.getUsername().equals("admin"))) {
                    if (ZUtil.isnumber(name[1])) {
                        try {
                            int value = Integer.parseInt(name[1]);
                            try (Connection conn = DbManager.gI().getConnect();
                                 PreparedStatement ps = conn.prepareStatement("select * from `account` where `username` = ? limit 1")) {
                                ps.setString(1, name[0]);
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        int accId = rs.getInt("id");
                                        int tongnapOld = rs.getInt("tongnap");
                                        try (PreparedStatement psUp = conn.prepareStatement("update `account` set `tongnap` = `tongnap` + ? where `username` = ?")) {
                                            psUp.setInt(1, value);
                                            psUp.setString(2, name[0]);
                                            if (psUp.executeUpdate() > 0) {
                                                String old = "Trước: tongnap: " + tongnapOld;
                                                String md5Token = historys.RechargeHistory.logRecharge(accId, 0, name[0], "TONGNAP", value, tongnapOld, (long) tongnapOld + value, "admin nap tay");
                                                try (PreparedStatement psLog = conn.prepareStatement("INSERT INTO `lichsunap` (`name`, `old`, `value`, `time`, `note`) VALUES (?, ?, ?, ?, ?)")) {
                                                    psLog.setString(1, name[0]);
                                                    psLog.setString(2, old);
                                                    psLog.setInt(3, value);
                                                    psLog.setString(4, (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Date.from(Instant.now())));
                                                    psLog.setString(5, "admin nap tay (MD5: " + md5Token + ")");
                                                    psLog.executeUpdate();
                                                } catch (Exception ignored) {}
                                                p.getService().send_box_ThongBao_OK("Nạp thành công " + value + " Tổng Nạp vào tk " + name[0] + ":\n" + old + "\nMã MD5: " + md5Token);
                                            } else {
                                                p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
                                            }
                                        }
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Không tìm thấy tài khoản: " + name[0]);
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (NumberFormatException nfe) {
                            p.getService().send_box_ThongBao_OK("Giá trị nạp không hợp lệ!");
                        }
                    }
                }
            }
            break;
            case 22: {
                if (name.length == 1) {
                    String mbv = name[0];
                    if (mbv.matches("^[0-9a-z]{6}$")) {
                        p.mbv = mbv;
                        p.getService().send_box_ThongBao_OK("Mã bảo vệ đặt thành công");
                    } else {
                        p.getService().send_box_ThongBao_OK("Yêu cầu mã bảo vệ 6 ký tự bao gồm chữ thường và số!");
                    }
                }
            }
            break;
             case -21: {
                if (name.length == 1) {
                    String newClanName = name[0];
                    if (newClanName.length() < 3) {
                        p.getService().send_box_ThongBao_OK("Tên băng nhiều hơn 3 ký tự");
                        return;
                    }
                    if (p.get_gcoin() < 50_000) {
                        p.getService().send_box_ThongBao_OK("Không đủ 50.000 GCoin");
                        return;
                    }
                    if (Clan.get_clan_by_name(newClanName) != null) {
                        p.getService().send_box_ThongBao_OK("Tên băng này đã đươc sử dụng, hãy dùng tên khác");
                        return;
                    }
                    if (newClanName.toLowerCase().contains("admin")) {
                        p.getService().send_box_ThongBao_OK("Bạn không phải là admin");
                        return;
                    }

                    if (Clan.renameClan(p.clan, newClanName)) {
                        // ThÃ´ng bÃ¡o cho ngÆ°á»i chÆ¡i
                        p.getService().send_box_ThongBao_OK("Đổi tên băng thành công");
                        p.update_gcoin(-50_000);
                        p.updateMoney();
                        // ThÃ´ng bÃ¡o cho cÃ¡c thÃ nh viÃªn khÃ¡c trong clan
                        for (ClanMember member : p.clan.members) {
                            Player otherPlayer = Zone.get_player_by_name_allmap(member.name);
                            if (otherPlayer != null) {
                                Clan.update_list_member(otherPlayer, false);
                                Clan.send_info(otherPlayer, false);
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Tên băng đã tồn tại họăc không hợp lệ.");
                    }
                }
                break;
            }
               case -13: {
                if (name.length == 1) {

                    String xacnhan = name[0];
                    if (xacnhan.equals("xoabang")) {
                        p.setyesNoDialog(new model.YesNoDialog(p, -63, "Thông báo",
                                ("Băn có muốn xóa băng "
                                        + p.clan.name + " không ?"),
                                new String[] { "Đồng ý",  "Hủy" }, new byte[] { -1, -1 }));
                        p.getService().startYesNo();
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn đã nhập sai nội dung");
                    }

                }
                break;
            }
            case 13: {
                if (name.length == 1 && !name[0].isBlank()) {
                    String mbv = name[0];
                    if (mbv.matches("^[0-9a-z]{6}$") && p.mbv.equals(mbv)) {
                        p.passBagOK = true;
                        p.getService().send_box_ThongBao_OK("Mở khoá thành công");
                    } else {
                        p.getService().send_box_ThongBao_OK("Yêu cầu mã bảo vệ 6 ký tự bao gồm chữ thường và số!");
                    }
                }

            }
            break;
            case 20: {
                if (p.data_yesno != null && p.data_yesno.length == 1 && p.data_yesno[0] == 20
                        && name.length == 1 && p.nameNew == null) {
                    if (!p.name.equals(name[0])) {
                        if (name[0].matches("^[a-z0-9]{6,15}$")) {
                            if (database.DbManager.getPlayerIdByName(name[0]) > 0) {
                                p.getService().send_box_ThongBao_OK("Tên nhân vật đã tồn tại! Vui lòng chọn tên khác.");
                                p.data_yesno = null;
                                return;
                            }
                            p.data_yesno = new int[]{75};
                            p.setyesNoDialog(new model.YesNoDialog(p, 75, "Thông báo",
                                    "Xác nhận đổi tên nhân vật thành: " + name[0] + "?",
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                            p.nameNew = name[0];
                            return;
                        } else {
                            p.getService().send_box_ThongBao_OK("Tên nhân vật từ 6 ký tự trở lên và không chứa ký tự đặc biệt!");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Tên mới phải khác tên cũ!");
                    }
                }
                p.data_yesno = null;
            }
            break;
            case 54: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 188) < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " huy hiệu Đặc biệt");
                        return;
                    }
                    p.item.remove_item47(4, 188, value);
                    p.item.add_item_bag47(4, 187, value);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " Huy Hiệu DB sang "
                            + ZUtil.number_format(value) + " Huy Hiệu Lửa");
                }
            }
            break;
            case 53: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 188) < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " huy hiệu Đặc biệt");
                        return;
                    }
                    p.item.remove_item47(4, 188, value);
                    p.item.add_item_bag47(4, 185, value);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " Huy Hiệu DB sang "
                            + ZUtil.number_format(value) + " Huy Hiệu Nước");
                }
            }
            break;
            case 52: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 188) < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " huy hiệu Đặc biệt");
                        return;
                    }
                    p.item.remove_item47(4, 188, value);
                    p.item.add_item_bag47(4, 186, value);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " Huy Hiệu DB sang "
                            + ZUtil.number_format(value) + " Huy Hiệu Đá");
                }
            }
            break;
            case 51: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 188) < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " huy hiệu Đặc biệt");
                        return;
                    }
                    p.item.remove_item47(4, 188, value);
                    p.item.add_item_bag47(4, 183, value);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " Huy Hiệu DB sang "
                            + ZUtil.number_format(value) + " Huy Hiệu Cỏ");
                }
            }
            break;
            case 50: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.item.total_item_bag_by_id(4, 188) < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " huy hiệu Đặc biệt");
                        return;
                    }
                    p.item.remove_item47(4, 188, value);
                    p.item.add_item_bag47(4, 184, value);
                    p.item.updateInventory(false);
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " Huy Hiệu DB sang "
                            + ZUtil.number_format(value) + " Huy Hiệu Lửa");
                }
            }
            break;
            case 28: {
                if (name.length == 2) {
                    if (name[0].matches("^[a-z0-9]{3,15}$") && name[1].matches("^[a-z0-9]{3,15}$")) {
                        if (!name[0].equals(name[1])) {
                            try (Connection conn = DbManager.gI().getConnect();
                                 PreparedStatement ps = conn.prepareStatement("update `account` set `password` = ? where `username` = ? and `password` = ? limit 1")) {
                                ps.setString(1, name[1]);
                                ps.setString(2, p.getUsername());
                                ps.setString(3, name[0]);
                                int res = ps.executeUpdate();
                                if (res == 1) {
                                    p.setPass(name[1]);
                                    p.getService().send_box_ThongBao_OK("Đổi mật khẩu thành công");
                                } else {
                                    p.getService().send_box_ThongBao_OK("Mật khẩu cũ không chính xác");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Mật khẩu mới phải khác mật khẩu cũ");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Ký tự nhập không hợp lệ");
                    }
                }
            }
            break;
            case 27: {
                if (p.clan == null || !p.clan.members.get(0).name.equals(p.name)) {
                    return;
                }
                if (name.length == 1) {
                    if (ZUtil.isnumber(name[0])) {
                        int value = Integer.parseInt(name[0]);
                        if (value <= 0) {
                            p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                            return;
                        }
                        int numXu = p.item.total_item_bag_by_id(4, 580);
                        if (numXu < value) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + value + " xu hành trình. Xu hiện tại: " + numXu);
                            return;
                        }
                        p.item.remove_item47(4, 580, value);
                        p.item.updateInventory(false);
                        int clanHt = p.clan.hanhtrinh.get(0);
                        p.clan.hanhtrinh.set(0, (clanHt + value));
                        Clan.flush(p.clan);
                        p.getService().send_box_ThongBao_OK("Cống hiến xu hành trình thành công, xu hành trình băng hiện tại là " + p.clan.hanhtrinh.get(0));
                    } else {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                    }
                }
            }
            break;
            case 15: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value;
                    try {
                        value = Long.parseLong(name[0]);
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (value <= 0 || value > 2_000_000L) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ (tối đa 2.000.000)");
                        return;
                    }
                    long extolCost = value * 1000L;
                    if (extolCost <= 0 || extolCost > 2_000_000_000L) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.get_vnd() < extolCost) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + ZUtil.number_format(extolCost) + " extol");
                        return;
                    }
                    p.updateVnd(-extolCost);
                    p.update_bua(value);
                    p.updateMoney();
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(extolCost) + " extol sang "
                            + ZUtil.number_format(value) + " búa");
                }
            }
            break;
            case 17: {
                if (name.length == 1 && p.data_yesno != null && p.data_yesno.length == 2
                        && p.data_yesno[0] == 17) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    try {
                        ItemDauGia itSelect = DauGia.getItemByClientIndex(p.data_yesno[1]);
                        if (itSelect == null) {
                            p.getService().send_box_ThongBao_OK("Không tìm thấy vật phẩm đấu giá! Hãy thử lại sau");
                            return;
                        }
                        long inputVal;
                        try {
                            inputVal = Long.parseLong(name[0]);
                        } catch (Exception e) {
                            p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ!");
                            return;
                        }
                        long priceReqLong = (long) itSelect.priceNow + (long) itSelect.minPricePlus;
                        if (priceReqLong > 2_000_000_000L) {
                            priceReqLong = 2_000_000_000L;
                        }
                        if (inputVal < priceReqLong || inputVal > 2_000_000_000L) {
                            p.getService().send_box_ThongBao_OK("Đấu giá mới tối thiểu " + priceReqLong + " búa (tối đa 2 tỷ)!");
                            return;
                        }
                        int value = (int) inputVal;
                        int alreadyBid = itSelect.listPrice.getOrDefault(p.IDPlayer, 0);
                        int needPay = value - alreadyBid;
                        if (needPay < 0) needPay = 0;

                        if (p.get_bua() < needPay) {
                            p.getService().send_box_ThongBao_OK("Không đủ " + needPay + " búa trong hành trang");
                            return;
                        }
                        if (itSelect.isClose || itSelect.time <= System.currentTimeMillis()) {
                            p.getService().send_box_ThongBao_OK("Đấu giá vật phẩm này đã kết thúc!");
                            return;
                        }
                        long time = DauGia.get_time_join(p.name);
                        if (p.getUsername().equals("admin") || time < System.currentTimeMillis()) {
                            DauGia.set_new_value(itSelect, p, value, p.data_yesno[1]);
                            DauGia.update_time_join(p.name);
                            p.updateMoney();
                            p.getService().send_box_ThongBao_OK("Đấu giá thành công với mức giá " + value + " búa!");
                        } else {
                            p.getService().send_box_ThongBao_OK("Chỉ có thể đấu giá mới sau "
                                    + ((time - System.currentTimeMillis()) / 1000) + "s nữa");
                        }
                    } catch (Exception ex) {
                        p.getService().send_box_ThongBao_OK("Không tìm thấy vật phẩm đấu giá! Hãy thử lại sau");
                    }
                }
            }
            break;
            case 14: {
                if (name.length == 1) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value;
                    try {
                        value = Long.parseLong(name[0]);
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (value <= 0 || value > 2_000_000L) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ (tối đa 2.000.000)");
                        return;
                    }
                    long extolGain = value * 1000L;
                    if (extolGain <= 0 || extolGain > 2_000_000_000L) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.get_bua() < value) {
                        p.getService().send_box_ThongBao_OK("Không đủ " + value + " búa");
                        return;
                    }
                    p.update_bua(-value);
                    p.updateVnd(extolGain);
                    p.updateMoney();
                    p.getService().send_box_ThongBao_OK("Đổi " + ZUtil.number_format(value) + " búa sang "
                            + ZUtil.number_format(extolGain) + " extol");
                }
            }
            break;
            case 16: {
                if (p.getUsername().equals("admin") && (name.length == 7 || name.length == 6)) {
                    try {
                        ItemDauGia itNew = new ItemDauGia();
                        itNew.itemID          = Short.parseShort(name[0].trim());
                        itNew.priceStart      = Integer.parseInt(name[1].trim());
                        itNew.priceNow        = itNew.priceStart;
                        itNew.minPricePlus    = Integer.parseInt(name[2].trim());
                        if (name.length == 7) {
                            itNew.minPriceDutDiem = Integer.parseInt(name[3].trim());
                            itNew.maxPriceDutDiem = Integer.parseInt(name[4].trim());
                            itNew.priceDutDiem    = itNew.minPriceDutDiem;
                            String myDate = name[5].trim() + " " + name[6].trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = sdf.parse(myDate);
                            itNew.time = date.getTime();
                        } else {
                            itNew.minPriceDutDiem = Integer.parseInt(name[3].trim());
                            itNew.maxPriceDutDiem = itNew.minPriceDutDiem;
                            itNew.priceDutDiem    = itNew.minPriceDutDiem;
                            String myDate = name[4].trim() + " " + name[5].trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = sdf.parse(myDate);
                            itNew.time = date.getTime();
                        }
                        itNew.name_own  = "";
                        itNew.id_own    = -1;
                        itNew.isClose   = itNew.time < System.currentTimeMillis();
                        itNew.isClaimed = false;
                        itNew.session   = (DauGia.SESSION != null && !DauGia.SESSION.isEmpty()) ? DauGia.SESSION : "MUA_1";
                        itNew.listPrice = new HashMap<>();

                        DauGia.ENTRY.add(itNew);
                        DauGia.saveSettings();

                        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(itNew.itemID);
                        String itemName = itemTemplate4 != null ? itemTemplate4.name : ("ID " + itNew.itemID);
                        p.getService().send_box_ThongBao_OK("Thêm item đấu giá thành công: " + itemName
                                + "\nGiá KĐ: " + itNew.priceStart
                                + "\nGiá Min Dứt Điểm: " + itNew.minPriceDutDiem
                                + "\nGiá Max Dứt Điểm: " + itNew.maxPriceDutDiem);
                    } catch (Exception ex) {
                        p.getService().send_box_ThongBao_OK("Dữ liệu nhập vào không đúng! Hãy nhập theo mẫu:\n"
                                + "1. ItemID (ví dụ: 427)\n2. Giá khởi điểm (5000)\n3. Mức tăng tối thiểu (500)\n"
                                + "4. Giá min dứt điểm (50000)\n5. Giá max dứt điểm (200000)\n"
                                + "6. Ngày kết thúc (31/12/2026)\n7. Giờ kết thúc (20:00:00)");
                    }
                }
            }
            break;
            case 11: {
                if (name.length == 1) {
                    if (p.level < 70) {
                        p.getService().send_box_ThongBao_OK("Level chưa đủ, cần cấp 70 trở lên");
                        return;
                    }
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value;
                    try {
                        value = Long.parseLong(name[0]);
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (value <= 0 || value > 2_000_000_000L) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.clan != null) {
                        synchronized (p) {
                            if ((value + (long) p.clan.get_ngoc()) > 2_000_000_000L) {
                                p.getService().send_box_ThongBao_OK("Số dư quỹ bang quá lớn, hãy thử lại sau");
                                return;
                            }
                            if (p.get_ngoc() < value) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + value + " ruby");
                                return;
                            }
                            p.update_ngoc(-value);
                            p.updateMoney();
                            p.clan.update_ruby((int) value);
                        }
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                            if (p0 != null) {
                                Clan.send_info(p0, false);
                            }
                        }
                        p.getService().send_box_ThongBao_OK("Góp " + value + " ruby vào quỹ băng thành công");
                    }
                }
                break;
            }
            case 10: {
                if (name.length == 1) {
                    if (name[0].length() < 3) {
                        p.getService().send_box_ThongBao_OK("Tên băng nên nhiều hơn 3 ký tự");
                        return;
                    }
                    if (p.get_ngoc() < 1000) {
                        p.getService().send_box_ThongBao_OK("Không đủ 2000 ruby");
                        return;
                    }
                    if (Clan.get_clan_by_name(name[0]) != null) {
                        p.getService().send_box_ThongBao_OK("Tên băng này đã được sử dụng, hãy sử dụng tên khác");
                        return;
                    }
                    //
                    Clan clan = new Clan();
                    clan.id = (short) Clan.get_clan_id();
                    clan.name = name[0];
                    clan.opAttri = new short[]{0, 0, 0, 0, 0};
                    clan.pointAttri = 2;
                    clan.maxAttri = 20;
                    clan.icon = 0;
                    clan.level = 1;
                    clan.xp = 0;
                    clan.thongbao = "";
                    clan.trungsinh = 0;
                    clan.countAction = 0;
                    clan.allowRequest = 1;
                    clan.chat = new ArrayList<>();
                    clan.mem_request = new ArrayList<>();
                    clan.list_it = new ArrayList<>();
                    clan.buff = new java.util.concurrent.CopyOnWriteArrayList<>();
                    clan.hanhtrinh = new ArrayList<>();
                    clan.hanhtrinh.add(0);
                    //
                    clan.members = new ArrayList<>();
                    ClanMember mem = new ClanMember();
                    mem.playerId = p.IDPlayer;
                    mem.name = p.name;
                    mem.conghien = 0;
                    mem.donate = 0;
                    mem.gopRuby = 32_000;
                    mem.numquest = 3;
                    mem.id = 0;
                    mem.hair = (short) p.get_hair();
                    mem.head = (short) p.get_head();
                    mem.hat = p.get_hat();
                    mem.level = p.level;
                    mem.levelInclan = 0;
                    mem.clazz = p.clazz;
                    mem.timeJoinClan = System.currentTimeMillis();
                    clan.members.add(mem);
                    Clan.HM_TIME_GIFT.put(mem.name, (System.currentTimeMillis() + (60_000l * 60 * 8)));
                    //
                    if (Clan.create_new_clan(clan)) {
                        if (!core.Manager.gI().isTestMode()) {
                            p.update_ngoc(-1000);
                            p.updateMoney();
                        }
                        //
                        p.clan = clan;
                        Clan.send_info(p, false);
                        p.syncFullPlayerStats();
                        for (int i = 0; i < p.map.players.size(); i++) {
                            if (!p.map.players.get(i).equals(p)) {
                                Clan.send_me_to_other(p, p.map.players.get(i), false);
                            }
                        }
                        Message m = new Message(-19); // show table select icon
                        m.writer().writeByte(98);
                        m.writer().writeUTF("Cửa hàng biểu tượng");
                        m.writer().writeByte(107);
                        m.writer().writeShort(10);
                        for (int i = 0; i < 10; i++) {
                            m.writer().writeShort(i);
                            m.writer().writeShort(i);
                            m.writer().writeUTF("Huy hiệu " + (i + 1));
                            m.writer().writeUTF(
                                    "Được làm từ gì đấy không biết nữa, mua đeo vào rất đẹp");
                            m.writer().writeShort(0);
                        }
                        p.addmsg(m);
                        m.cleanup();
                    } else {
                        p.getService().send_box_ThongBao_OK("Tên băng này đã được sử dụng, hãy thử lại tên khác");
                    }
                }
                break;
            }
            case 7: {
                if (name.length == 1 && p.data_yesno != null && p.data_yesno.length == 3) {
                    if (p.trade_target != null) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                        p.data_yesno = null;
                        return;
                    }
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value < 2000 || value > 1_000_000_000) {
                        p.getService().send_box_ThongBao_OK("Mức giá bán tối thiểu là 2.000 Extol và tối đa là 1.000.000.000 Extol");
                        return;
                    }
                    int type = p.data_yesno[0];
                    int id_ = p.data_yesno[1];
                    int value_ = p.data_yesno[2];
                    p.data_yesno = new int[]{type, id_, value_, value};
                    if (type == 4) {
                        p.setyesNoDialog(new model.YesNoDialog(p, 22, "Thông báo",
                                ("Bạn có muốn bán " + value_ + " "
                                + ItemTemplate4.get_item_name(id_) + " với giá "
                                + ZUtil.number_format(value)
                                + " Extol? Phí để đăng bán là 200 Extol"),
                                new String[]{"200 Extol", "Không"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    } else if (type == 7) {
                        p.setyesNoDialog(new model.YesNoDialog(p, 22, "Thông báo",
                                ("Bạn có muốn bán " + value_ + " "
                                + ItemTemplate7.get_item_name(id_) + " với giá "
                                + ZUtil.number_format(value)
                                + " Extol? Phí để đăng bán là 200 Extol"),
                                new String[]{"200 Extol", "Không"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                }
                break;
            }
            case 6: {
                if (name.length == 1 && p.data_yesno != null && p.data_yesno.length == 1) {
                    if (p.trade_target != null) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                        p.data_yesno = null;
                        return;
                    }
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value < 20_000 || value > 1_000_000_000) {
                        p.getService().send_box_ThongBao_OK("Mức giá bán tối thiểu là 20.000 Extol và tối đa là 1.000.000.000 Extol");
                        return;
                    }
                    int price = p.data_yesno[0];
                    p.data_yesno = new int[]{price, value};
                    p.setyesNoDialog(new model.YesNoDialog(p, 18, "Thông báo",
                            ("Bạn có muốn bán " + price + " triệu beri với giá "
                            + ZUtil.number_format(value)
                            + " Extol? Phí để đăng bán là 2.000 Extol"),
                            new String[]{"2.000 Extol", "Không"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                }
                break;
            }
            case 5: {
                if (name.length == 1 && p.data_yesno != null && p.data_yesno.length == 1) {
                    if (p.trade_target != null) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                        p.data_yesno = null;
                        return;
                    }
                    if (p.item == null || p.item.bag3 == null || p.data_yesno[0] < 0 || p.data_yesno[0] >= p.item.bag3.length) {
                        p.data_yesno = null;
                        return;
                    }
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    int value = Integer.parseInt(name[0]);
                    if (value < 20_000 || value > 1_000_000_000) {
                        p.getService().send_box_ThongBao_OK("Mức giá bán tối thiểu là 20.000 Extol và tối đa là 1.000.000.000 Extol");
                        return;
                    }
                    int bagIndex = p.data_yesno[0];
                    Item_wear it_select = p.item.bag3[bagIndex];
                    if (it_select != null) {
                        p.data_yesno = new int[]{bagIndex, value};
                        p.setyesNoDialog(new model.YesNoDialog(p, 17, "Thông báo",
                                ("Bạn có muốn bán vật phẩm " + it_select.template.name + " với giá "
                                + ZUtil.number_format(value)
                                + " Extol? Phí để đăng bán là 2.000 Extol"),
                                new String[]{"2.000 Extol", "Không"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                }
                break;
            }
            
            
            case 21: {
                if (name.length == 1) {
                    Player p0 = Zone.get_player_by_name_allmap(name[0]);
                    if (p0 != null) {
                        p.data_yesno = new int[]{85, p0.index_map};
                        p.setyesNoDialog(new model.YesNoDialog(p, 85, "Thông báo", "Xác nhận thuê bot để tiêu diệt " + p0.name + "? Lựa chọn phí beri hoặc ruby",
                                new String[]{"500 Beri", "500 Ruby", "Hủy"}, new byte[]{6, 7, -1}));
                        p.getService().startYesNo();
                    } else {
                        p.getService().send_box_ThongBao_OK("Nhân vật offline");
                    }
                }
            }
            break;
            
            case 4: {
                //System.out.println("123123 " + name.length );
                if (name.length == 1 && p.data_yesno == null) {

                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value = Long.parseLong(name[0]) * 1000;
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.get_vnd() < value) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(value) + " extol");
                        return;
                    }
                    int ruby = ((int) value) / 1000;
                    p.data_yesno = new int[]{ruby};
                    p.setyesNoDialog(new model.YesNoDialog(p, 9, "Thông báo",
                            "Bạn có thật sự muốn đổi " + ZUtil.number_format(value) + " Extol để"
                            + " đổi lấy " + ZUtil.number_format(ruby) + " Ruby không?",
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                    p.getService().startYesNo();
                    break;
                }
                break;
            }

          

            case 8: {// vuong code đổi coin sang rubi
                if (name.length == 1 && p.data_yesno == null) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value = Long.parseLong(name[0]) * 5;
                    if (value <= 0) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    if (p.get_coin() < value) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(value) + " coin");
                        return;
                    }
                    int ruby = (int) ((long) value / 5);
                    p.data_yesno = new int[]{ruby};
                    p.setyesNoDialog(new model.YesNoDialog(p, 60, "Thông báo",
                            "Bạn có thật sự muốn đổi " + ZUtil.number_format(value) + " Coin để"
                                    + " đổi lấy " + ZUtil.number_format(ruby) + " Ruby không?",
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                    p.getService().startYesNo();
                    break;
                }
                break;
            }
            case 9: {
                if (name.length == 1 && p.data_yesno == null) {
                    if (!ZUtil.isnumber(name[0])) {
                        p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                        return;
                    }
                    long value = Long.parseLong(name[0]);
                    if (value <= 0 || value > 2_000_000_000) {
                        p.getService().send_box_ThongBao_OK("Số nhập phải > 0 và < 2tỷ");
                        return;
                    }
                    if (p.ruby() < value) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(value) + " ruby nạp");
                        return;
                    }
                    long beri = value * 10_000;
                    p.data_yesno = new int[]{(int) value};
                    p.setyesNoDialog(new model.YesNoDialog(p, 61, "Thông báo",
                            "Bạn có thật sự muốn đổi " + ZUtil.number_format(value) + " ruby nạp để"
                            + " đổi lấy " + ZUtil.number_format(beri) + " Beri không?",
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                    p.getService().startYesNo();
                    break;
                }
                break;
            }
            case 2: {
                if (name.length == 2) {
                    name[0] = name[0].replace(" ", "");
                    name[1] = name[1].replace(" ", "");
                    name[0] = name[0].toLowerCase();
                    name[1] = name[1].toLowerCase();
                    if (name[0].contains("admin") || name[1].contains("admin")) {
                        p.getService().send_box_ThongBao_OK("Tên tài khoản và mật khẩu không được trùng admin!");
                        return;
                    }
                    Pattern pat = Pattern.compile("^[a-zA-Z0-9@.]{1,30}$");
                    if (!pat.matcher(name[0]).matches() || !pat.matcher(name[1]).matches()) {
                        p.getService().send_box_ThongBao_OK("Tên tài khoản và mật khẩu phải dài hơn 6 và không chứa ký tự đặc biệt!");
                        return;
                    }
                    try (Connection conn = DbManager.gI().getConnect();
                         PreparedStatement ps = conn.prepareStatement(
                                 "UPDATE `account` SET `username` = ?, `password` = ? WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;")) {
                        ps.setString(1, name[0]);
                        ps.setString(2, name[1]);
                        ps.setString(3, p.getUsername());
                        ps.setString(4, p.getPass());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        p.getService().send_box_ThongBao_OK("Tên đã được sử dụng, hãy thử lại!");
                        return;
                    }
                    p.setUsername(name[0]);
                    p.setPass(name[1]);
                    Message m = new Message(-59);
                    m.writer().writeUTF(name[0]);
                    m.writer().writeUTF(name[1]);
                    p.addmsg(m);
                    m.cleanup();
                }
                break;
            }
            case 1: {
                GiftTemplate.execute(p, name);
                break;
            }
            case 32002: {
                if (p.getUsername().equals("admin")) {
                    if (name.length == 3) {
                        if (!ZUtil.isnumber(name[0]) || !ZUtil.isnumber(name[1])
                                || !ZUtil.isnumber(name[2])) {
                            p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                            return;
                        }
                        int value1 = Integer.parseInt(name[0]);
                        int value2 = Integer.parseInt(name[1]);
                        int value3 = Integer.parseInt(name[2]);
                        if (value3 <= 0 || value3 > 32000) {
                            value3 = 1;
                        }
                        switch (value1) {
                            case 3: {
                                ItemTemplate3 temp = ItemTemplate3.get_it_by_id(value2);
                                if (temp != null) {
                                    Item_wear it_add = new Item_wear();
                                    it_add.setup_template_by_id(temp);
                                    if (it_add.template != null) {
                                        p.item.add_item_bag3(it_add);
                                    }
                                    p.item.updateInventory(false);
                                    p.getService().send_box_ThongBao_OK("Lấy thành công " + temp.name);
                                }
                                break;
                            }
                            case 4: {
                                ItemTemplate4 temp = ItemTemplate4.get_it_by_id(value2);
                                if (temp != null) {
                                    p.item.add_item_bag47(4, temp.id, value3);
                                    p.item.updateInventory(false);
                                    p.getService().send_box_ThongBao_OK("Lấy thành công " + value3 + " " + temp.name);
                                }
                                break;
                            }
                            case 7: {
                                ItemTemplate7 temp = ItemTemplate7.get_it_by_id(value2);
                                if (temp != null) {
                                    p.item.add_item_bag47(7, temp.id, value3);
                                    p.item.updateInventory(false);
                                    p.getService().send_box_ThongBao_OK("Lấy thành công " + value3 + " " + temp.name);
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 32000: {
                if (p.getUsername().equals("admin")) {
                    if (name.length == 1) {
                        if (!ZUtil.isnumber(name[0])) {
                            p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                            return;
                        }
                        int value = Integer.parseInt(name[0]);
                        if (value > 100) {
                            value = 100;
                        }
                        if (value == 1) {
                            value = 2;
                        }
                        p.level = (short) (value - 1);
                        p.exp = Level.ENTRYS[p.level - 1].exp - 1;
                        p.update_exp(1, false);
                        p.reset_point(0);
                    }
                }
                break;
            }
            case 32001: {
                if (p.getUsername().equals("admin")) {
                    if (name.length == 1) {
                        if (!ZUtil.isnumber(name[0])) {
                            p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");
                            return;
                        }
                        int value = Integer.parseInt(name[0]);
                        if (value < 0 || value > 10_000_000) {
                            value = 1;
                        }
                        Manager.gI().exp = value;
                        p.getService().send_box_ThongBao_OK("Thay đổi xp x" + value);
                    }
                }
                break;
            }
            case 24: {
//                if (size != 1) {
//                    return;
//                }
//                String value = m2.reader().readUTF();
//                if (!(Util.isnumber(value))) {
//                    p.getService().send_box_ThongBao_OK("Dữ liệu nhập không phải số!!");
//                    return;
//                }
//                int value_ = Integer.parseInt(value);
//                if (value_ <= 0 || value_ > 1_000_000_000 || p.pointAttribute < value_) {
//                    p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ, hãy thử lại");
//                    return;
//                }
//                p.pointAttribute -= value_;
//                p.point1 += value_;
//                p.getService().UpdateInfoMaincharInfo();
//                p.update_info_to_all();
                break;
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEventRecharge(String playerName, int value) {
        int activeEventId = event.EventManager.activeId();
        if (activeEventId == -1) return;

        Player target = map.Zone.get_player_by_name_allmap(playerName);
        if (target != null) {
            event.EventData ev = target.getDataEvent(activeEventId);
            if (ev != null) {
                if (ev.data == null || ev.data.length < 30) {
                    int[] newData = new int[30];
                    if (ev.data != null) System.arraycopy(ev.data, 0, newData, 0, ev.data.length);
                    ev.data = newData;
                }
                ev.data[20] -= value;
            }
            return;
        }

        // If offline, load and update from DB via EventHistory
        historys.EventHistory.updateOfflineEventData(playerName, activeEventId, value);
    }
}
