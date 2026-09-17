package activities;

import template.Option;
import template.ItemOptionTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import network.Message;
import model.Player;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DanhHieu {
    public static List<DanhHieu> ENY = new ArrayList<>();

    public int id;
    public String Name;
    public String info;
    public int idEff;
    public int expire = -1; // -1 = vĩnh viễn, > 0 = số giây
    public int coint;
    public List<Option> op = new ArrayList<>();

    public DanhHieu() {
    }

    public DanhHieu(int id, String Name, int idEff) {
        this.id = id;
        this.Name = Name;
        this.idEff = idEff;
    }

    public DanhHieu(int id, String Name, int idEff, int coint, List<Option> op) {
        this.id = id;
        this.Name = Name;
        this.idEff = idEff;
        this.coint = coint;
        if (op != null) {
            this.op.addAll(op);
        }
    }

    public static DanhHieu get_Id(int id) {
        for (DanhHieu dh : ENY) {
            if (dh.id == id) {
                return dh;
            }
        }
        return null;
    }

    public static String formatDuration(long seconds) {
        if (seconds <= 0) return "Vĩnh viễn";
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(" ngày ");
        if (hours > 0) sb.append(hours).append(" giờ ");
        if (mins > 0) sb.append(mins).append(" phút ");
        if (days == 0 && hours == 0 && mins == 0 && secs > 0) sb.append(secs).append(" giây");
        return sb.toString().trim();
    }

    public String getOptionString() {
        if (this.op == null || this.op.isEmpty()) {
            return "Không có thuộc tính cộng thêm.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.op.size(); i++) {
            Option o = this.op.get(i);
            if (o != null) {
                ItemOptionTemplate iot = ItemOptionTemplate.ENTRYS.get(o.id);
                String optName = (iot != null && iot.name != null) ? iot.name : ("Thuộc tính " + o.id);
                if (iot != null && iot.percent == 1) {
                    sb.append("• ").append(optName).append(": +").append(o.getParam()).append("%\n");
                } else {
                    sb.append("• ").append(optName).append(": +").append(o.getParam()).append("\n");
                }
            }
        }
        return sb.toString().trim();
    }

    public String getFullInfoString(Player p) {
        StringBuilder sb = new StringBuilder();

        // 1. Thêm mô tả thủ công nếu có trong DB
        if (this.info != null && !this.info.trim().isEmpty()) {
            sb.append(this.info.trim());
        }

        // 2. Thêm thuộc tính từ op
        String optStr = getOptionString();
        if (optStr != null && !optStr.isEmpty() && !optStr.equals("Không có thuộc tính cộng thêm.")) {
            if (sb.length() > 0) sb.append("\n\n");
            sb.append("Thuộc tính:\n").append(optStr);
        } else if (sb.length() == 0) {
            sb.append("Không có thuộc tính cộng thêm.");
        }

        // 3. Thêm hạn sử dụng
        if (sb.length() > 0) sb.append("\n\n");
        int[] owned = (p != null) ? p.getDanhHieuById(this.id) : null;
        if (owned != null && owned.length >= 3 && owned[2] > 0) {
            long remaining = (long)owned[2] - (System.currentTimeMillis() / 1000L);
            if (remaining > 0) {
                sb.append("Hạn sử dụng: Còn ").append(formatDuration(remaining));
            } else {
                sb.append("Hạn sử dụng: Đã hết hạn");
            }
        } else if (this.expire > 0) {
            sb.append("Hạn sử dụng: ").append(formatDuration(this.expire));
        } else {
            sb.append("Hạn sử dụng: Vĩnh viễn");
        }

        return sb.toString().trim();
    }

    public static void load(Connection conn) {
        ENY.clear();
        boolean loadedFromDb = false;
        if (conn != null) {
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM `danhhieu` ORDER BY `id` ASC")) {
                while (rs.next()) {
                    DanhHieu dh = new DanhHieu();
                    dh.id = rs.getInt("id");
                    dh.Name = rs.getString("name");
                    try {
                        dh.info = rs.getString("info");
                    } catch (Exception ignored) {}
                    try {
                        dh.idEff = rs.getInt("idEff");
                    } catch (Exception e) {
                        try {
                            dh.idEff = rs.getInt("idicon");
                        } catch (Exception ignored) {}
                    }
                    try {
                        dh.expire = rs.getInt("expire");
                    } catch (Exception ignored) {
                        dh.expire = -1;
                    }
                    try {
                        String coinStr = rs.getString("coin");
                        if (coinStr != null && !coinStr.trim().isEmpty()) {
                            dh.coint = Integer.parseInt(coinStr.trim());
                        }
                    } catch (Exception e) {
                        try {
                            dh.coint = rs.getInt("coin");
                        } catch (Exception ignored) {}
                    }

                    String opRaw = rs.getString("op");
                    if (opRaw != null && !opRaw.trim().isEmpty()) {
                        try {
                            JSONArray js = (JSONArray) JSONValue.parse(opRaw);
                            if (js != null) {
                                for (int i = 0; i < js.size(); ++i) {
                                    Object itemObj = js.get(i);
                                    JSONArray js1 = (itemObj instanceof JSONArray) ? (JSONArray) itemObj : (JSONArray) JSONValue.parse(itemObj.toString());
                                    if (js1 != null && js1.size() >= 2) {
                                        dh.op.add(new Option(Integer.parseInt(js1.get(0).toString()),
                                                Integer.parseInt(js1.get(1).toString())));
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            core.Log.warning("DanhHieu", "Error parsing options for DanhHieu ID " + dh.id + ": " + ex.getMessage());
                        }
                    }
                    ENY.add(dh);
                }
                if (!ENY.isEmpty()) {
                    loadedFromDb = true;
                    core.Log.info("DanhHieu", "Loaded " + ENY.size() + " titles from `danhhieu` table.");
                }
            } catch (Exception e) {
                core.Log.warning("DanhHieu", "Table `danhhieu` not found or error loading: " + e.getMessage() + ". Initializing standard title templates.");
            }
        }

        if (!loadedFromDb && ENY.isEmpty()) {
            initDefaultTemplates();
        }
    }

    public static void initDefaultTemplates() {
        ENY.clear();
        core.Log.info("DanhHieu", "Initialized default title templates. Titles can be configured via DB `danhhieu` table or code.");
    }

    public static void process(Player p, Message m) throws IOException {
        if (p == null || m == null) return;
        byte act = m.reader().readByte();
        if (act == 0) {
            // Yêu cầu danh sách Danh Hiệu
            send_list(p);
        } else if (act == 1) {
            // Click action button
            int titleId = m.reader().readInt();
            byte buttonActionId = m.reader().readByte();
            DanhHieu dh = get_Id(titleId);
            if (dh == null) {
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Danh hiệu không tồn tại!");
                }
                return;
            }

            if (buttonActionId == 1) {
                // Kích hoạt / Sử dụng danh hiệu
                boolean hasTitle = p.check_id_danhhieu(titleId);
                boolean isTest = core.Manager.gI() != null && core.Manager.gI().isTestMode();
                if (!hasTitle && isTest) {
                    p.addDanhHieu(titleId);
                    hasTitle = true;
                }

                if (hasTitle) {
                    // Gỡ hiệu ứng danh hiệu cũ nếu có
                    if (p.id_danh_hieu_su_dung > 0) {
                        DanhHieu oldDh = DanhHieu.get_Id(p.id_danh_hieu_su_dung);
                        if (oldDh != null && oldDh.idEff > 0) {
                            removeTitleEffect(p, oldDh.idEff);
                        }
                    }

                    p.offAllDh();
                    int[] owned = p.getDanhHieuById(titleId);
                    if (owned != null && owned.length >= 2) {
                        owned[1] = 1;
                    }
                    p.id_danh_hieu_su_dung = titleId;

                    // Kích hoạt hiệu ứng mới
                    if (dh.idEff > 0) {
                        applyTitleEffect(p, dh.idEff);
                    }

                    p.setAbility();
                    p.update_info_to_all();
                    if (p.getService() != null) {
                        p.getService().send_box_ThongBao_OK("Đã kích hoạt danh hiệu: [" + dh.Name + "]!\n\n" + dh.getFullInfoString(p));
                    }
                    send_list(p);
                } else {
                    if (p.getService() != null) {
                        p.getService().send_box_ThongBao_OK("Bạn chưa sở hữu danh hiệu [" + dh.Name + "]!\n\n" + dh.getFullInfoString(p));
                    }
                }
            } else if (buttonActionId == 2) {
                // Tháo ra / Gỡ bỏ danh hiệu
                if (p.id_danh_hieu_su_dung > 0) {
                    DanhHieu oldDh = DanhHieu.get_Id(p.id_danh_hieu_su_dung);
                    if (oldDh != null && oldDh.idEff > 0) {
                        removeTitleEffect(p, oldDh.idEff);
                    }
                }
                p.offAllDh();
                p.id_danh_hieu_su_dung = -1;
                p.setAbility();
                p.update_info_to_all();
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Đã gỡ bỏ danh hiệu: [" + dh.Name + "]!");
                }
                send_list(p);
            } else if (buttonActionId == 3) {
                // Mua danh hiệu (nếu có giá coin > 0)
                if (dh.coint > 0) {
                    if (p.get_vnd() >= dh.coint) {
                        p.updateVnd(-dh.coint);
                        p.updateMoney();
                        p.addDanhHieu(titleId);
                        int[] owned = p.getDanhHieuById(titleId);
                        if (owned != null && owned.length >= 2) {
                            if (p.id_danh_hieu_su_dung > 0) {
                                DanhHieu oldDh = DanhHieu.get_Id(p.id_danh_hieu_su_dung);
                                if (oldDh != null && oldDh.idEff > 0) {
                                    removeTitleEffect(p, oldDh.idEff);
                                }
                            }
                            p.offAllDh();
                            owned[1] = 1;
                            p.id_danh_hieu_su_dung = titleId;
                            if (dh.idEff > 0) {
                                applyTitleEffect(p, dh.idEff);
                            }
                            p.setAbility();
                            p.update_info_to_all();
                        }
                        if (p.getService() != null) {
                            p.getService().send_box_ThongBao_OK("Mua và kích hoạt thành công danh hiệu: [" + dh.Name + "]!\n\n" + dh.getFullInfoString(p));
                        }
                        send_list(p);
                    } else {
                        if (p.getService() != null) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ Extol/Coin! Cần " + core.ZUtil.number_format(dh.coint) + " (đang có " + core.ZUtil.number_format(p.get_vnd()) + ")");
                        }
                    }
                }
            }
        }
    }

    public static void applyTitleEffect(Player p, int idEff) {
        if (p == null || idEff <= 0) return;
        try {
            Message m = new Message(74);
            m.writer().writeByte(1);
            m.writer().writeShort(p.index_map);
            m.writer().writeShort(idEff);
            m.writer().writeInt(-1); // time = -1: Lặp vĩnh viễn (Permanent loop)
            m.writer().writeByte(0); // typemove = 0
            m.writer().writeByte(0); // loop = 0: Lặp liên tục không gián đoạn
            if (p.map != null) {
                p.map.send_msg_all_p(m, null, true);
            } else {
                p.addmsg(m);
            }
        } catch (Exception e) {
            core.Log.error("DanhHieu", "Error sending title effect: " + e.getMessage());
        }
    }

    public static void removeTitleEffect(Player p, int idEff) {
        if (p == null || idEff <= 0) return;
        try {
            Message m = new Message(74);
            m.writer().writeByte(2);
            m.writer().writeShort(p.index_map);
            m.writer().writeShort(idEff);
            if (p.map != null) {
                p.map.send_msg_all_p(m, null, true);
            } else {
                p.addmsg(m);
            }
        } catch (Exception e) {
            core.Log.error("DanhHieu", "Error removing title effect: " + e.getMessage());
        }
    }

    public static void send_list(Player p) throws IOException {
        if (p == null) return;
        boolean isTest = core.Manager.gI() != null && core.Manager.gI().isTestMode();
        List<DanhHieu> list = (ENY != null) ? ENY : new ArrayList<>();

        Message m = new Message(-109);
        m.writer().writeByte(0); // 0 = Full list
        m.writer().writeShort(list.size());

        int currentUsedId = p.getIdEff();

        for (int i = 0; i < list.size(); i++) {
            DanhHieu dh = list.get(i);
            if (dh == null) continue;

            m.writer().writeInt(dh.id);
            m.writer().writeUTF(dh.Name != null ? dh.Name : "");
            m.writer().writeShort(dh.idEff);
            m.writer().writeInt(dh.coint);

            // Xác định trạng thái người chơi: 0 = Chưa có, 1 = Đã sở hữu, 2 = Đang dùng
            byte state = 0;
            int[] owned = p.getDanhHieuById(dh.id);
            if (owned != null) {
                state = (owned.length >= 2 && owned[1] == 1) ? (byte) 2 : (byte) 1;
            }
            if (currentUsedId == dh.id) {
                state = 2;
            }
            if (isTest && state == 0) {
                state = 1; // Cho phép test trực tiếp
            }
            m.writer().writeByte(state);

            // Gửi chuỗi thông tin & thuộc tính đầy đủ (bao gồm info tùy chỉnh, op, và hạn sử dụng)
            m.writer().writeUTF(dh.getFullInfoString(p));

            // Gửi danh sách Action Buttons do Server điều khiển
            if (state == 2) {
                m.writer().writeByte(1); // 1 button
                m.writer().writeByte(2); // Action ID 2: Tháo Ra
                m.writer().writeUTF("Tháo Ra");
                m.writer().writeByte(2); // Style 2: Red / Outline
            } else if (state == 1) {
                m.writer().writeByte(1); // 1 button
                m.writer().writeByte(1); // Action ID 1: Sử Dụng
                m.writer().writeUTF("Sử Dụng");
                m.writer().writeByte(1); // Style 1: Green / Highlight
            } else {
                if (dh.coint > 0) {
                    m.writer().writeByte(1); // 1 button
                    m.writer().writeByte(3); // Action ID 3: Mua
                    m.writer().writeUTF("Mua (" + core.ZUtil.number_format(dh.coint) + ")");
                    m.writer().writeByte(0); // Style 0: Yellow / Gold
                } else {
                    m.writer().writeByte(0); // 0 buttons (hoặc có thể gửi 1 nút Xem)
                }
            }
        }

        p.addmsg(m);
    }
}
