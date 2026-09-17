package activities;

import java.io.IOException;
import model.Player;
import historys.zLog;
import network.Service;
import network.Message;
import skill.Skill_Template;
import skill.Skill_info;

public class Learn_Skill {

    public static void process(Player p, Message m2) throws IOException {
        byte act = m2.reader().readByte();
        short id = m2.reader().readShort();
        // System.out.println("act " + act);
        // System.out.println("id " + id);
        switch (act) {
            case 0: {
                Skill_info sk_temp = null;
                for (int i = 0; i < p.skill_point.size(); i++) {
                    if (p.skill_point.get(i).temp.Lv_RQ == -1
                            && p.skill_point.get(i).temp.indexSkillInServer == id) {
                        sk_temp = p.skill_point.get(i);
                        break;
                    }
                }
                if (sk_temp == null) {
                    for (int i = 0; i < p.skill_point.size(); i++) {
                        if (p.skill_point.get(i).temp.Lv_RQ != -1
                                && p.skill_point.get(i).temp.indexSkillInServer == (id - 1)) {
                            sk_temp = p.skill_point.get(i);
                            break;
                        }
                    }
                }
                if (sk_temp != null) {
                    if (p.get_vang() < 10_000) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 10k Beri. Phí học kỹ năng này là 10k Beri!");
                        return;
                    }
                    if (sk_temp.temp.ID == 1015) {
                        if (sk_temp.temp.Lv_RQ == -1) {
                            if (p.item.total_item_bag_by_id(4, 643) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 sách Haki Quan Sát");
                                return;
                            } else {
                                p.item.remove_item47(4, 643, 1);
                                p.item.updateInventory(false);
                            }
                        } else {
                            int extolReq = 100_000;
                            int beriReq = 50_000_000;
                            if (sk_temp.temp.Lv_RQ == 2) {
                                extolReq = 150_000;
                                beriReq = 100_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 3) {
                                extolReq = 200_000;
                                beriReq = 150_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 4) {
                                extolReq = 300_000;
                                beriReq = 200_000_000;
                            }
                            if (p.get_vnd() < extolReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + extolReq + " extol. Hiện tại chỉ có " + p.get_vnd());
                                return;
                            }
                            if (p.get_vang() < beriReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + beriReq + " beri. Hiện tại chỉ có " + p.get_vang());
                                return;
                            }
                            if(extolReq < 0) {
                                zLog.gI().add_log("BUG", "Bug ne 0: " + extolReq);
                                // System.out.println("Bug ne 0: " + extolReq);
                                return;
                            }
                            p.updateVnd(-extolReq);
                            p.update_vang(-beriReq);
                            p.updateMoney();
                        }
                        p.update_vang(10_000);
                    } else if (sk_temp.temp.ID == 1016) {
                        if (sk_temp.temp.Lv_RQ == -1) {
                            if (p.item.total_item_bag_by_id(4, 753) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 sách Haki Vũ Trang");
                                return;
                            } else {
                                p.item.remove_item47(4, 753, 1);
                                p.item.updateInventory(false);
                            }
                        } else {
                            int rubyReq = 5_000;
                            int beriReq = 100_000_000;
                            if (sk_temp.temp.Lv_RQ == 2) {
                                rubyReq = 10_000;
                                beriReq = 200_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 3) {
                                rubyReq = 20_000;
                                beriReq = 400_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 4) {
                                rubyReq = 40_000;
                                beriReq = 800_000_000;
                            }
                            if (p.get_ngoc() < rubyReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + rubyReq + " ruby. Hiện tại chỉ có " + p.get_ngoc());
                                return;
                            }
                            if (p.get_vang() < beriReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + beriReq + " beri. Hiện tại chỉ có " + p.get_vang());
                                return;
                            }
                            p.update_ngoc(-rubyReq);
                            p.update_vang(-beriReq);
                            p.updateMoney();
                        }
                        p.update_vang(10_000);
                    } else if (sk_temp.temp.ID == 1017) {
                        if (sk_temp.temp.Lv_RQ == -1) {
                            if (p.item.total_item_bag_by_id(4, 754) < 1) {
                                p.getService().send_box_ThongBao_OK("Không đủ 1 sách Haki Bá Vương");
                                return;
                            } else {
                                p.item.remove_item47(4, 754, 1);
                                p.item.updateInventory(false);
                            }
                        } else {
                            int rubyReq = 5_000;
                            int beriReq = 100_000_000;
                            if (sk_temp.temp.Lv_RQ == 2) {
                                rubyReq = 10_000;
                                beriReq = 200_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 3) {
                                rubyReq = 20_000;
                                beriReq = 400_000_000;
                            } else if (sk_temp.temp.Lv_RQ == 4) {
                                rubyReq = 40_000;
                                beriReq = 800_000_000;
                            }
                            if (p.get_ngoc() < rubyReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + rubyReq + " ruby. Hiện tại chỉ có " + p.get_ngoc());
                                return;
                            }
                            if (p.get_vang() < beriReq) {
                                p.getService().send_box_ThongBao_OK("Không đủ " + beriReq + " beri. Hiện tại chỉ có " + p.get_vang());
                                return;
                            }
                            p.update_ngoc(-rubyReq);
                            p.update_vang(-beriReq);
                            p.updateMoney();
                        }
                        p.update_vang(10_000);
                    }
                    if (Skill_Template.learn_skill(sk_temp)) {
                        p.update_vang(-10_000);
                        p.updateMoney();
                        p.send_skill();
                        p.update_info_to_all();
                        p.getService().send_box_ThongBao_OK("Học Thành công " + sk_temp.temp.name);
                    } else {
                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra khi học skill, hãy báo cho admin fix ngay");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại");
                }
                break;
            }
        }
    }

    public static void request_learn_new_skill(Player p, Skill_info temp) throws IOException {
        Message m = new Message(-28);
        m.writer().writeByte(0);
        p.write_data_skill(m.writer(), temp);
        p.addmsg(m);
//        System.out.println("4234");
        m.cleanup();
    }

    public static void send_skill_percent(Player p, Skill_info sk_info) throws IOException {
        Message m = new Message(-28);
        m.writer().writeByte(2);
        m.writer().writeShort(sk_info.temp.ID);
        m.writer().writeShort(sk_info.get_percent());
        p.addmsg(m);
        m.cleanup();
    }

    public static void remove_skill(Player p, Skill_info sk_info) throws IOException {
        Message m = new Message(-28);
        m.writer().writeByte(3);
        m.writer().writeShort(sk_info.temp.ID);
        p.addmsg(m);
        m.cleanup();
    }
}
