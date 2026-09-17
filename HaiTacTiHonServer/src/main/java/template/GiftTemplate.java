package template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import database.DbManager;
import model.Player;
import network.Service;

public class GiftTemplate {
	public String giftname;
	public int luotnhap;
	public int gioihan;
	public String notice;
	public int beri;
	public int ruby;
	public byte[] type;
	public short[] id;
	public short[] quant;
	public String used;
	public String special;
	public byte mtv;

	public GiftTemplate(String giftname, int luotnhap, int gioihan, String notice, int beri, int ruby, String item,
	      String used, String special, byte mtv) {
		this.giftname = giftname;
		this.luotnhap = luotnhap;
		this.gioihan = gioihan;
		this.notice = (notice != null) ? notice : "";
		this.beri = beri;
		this.ruby = ruby;
		this.mtv = mtv;
		if (item != null && !item.trim().isEmpty() && !item.trim().equals("[]")) {
			try {
				JSONArray js = (JSONArray) JSONValue.parse(item);
				if (js != null && js.size() > 0) {
					this.type = new byte[js.size()];
					this.id = new short[js.size()];
					this.quant = new short[js.size()];
					for (int i = 0; i < id.length; i++) {
						JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
						if (js2 != null && js2.size() >= 3) {
							this.type[i] = Byte.parseByte(js2.get(0).toString());
							this.id[i] = Short.parseShort(js2.get(1).toString());
							this.quant[i] = Short.parseShort(js2.get(2).toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.used = (used != null) ? used : "";
		this.special = (special != null) ? special : "";
		this.used = this.used.replace(" ", "");
		this.special = this.special.replace(" ", "");
	}

	public synchronized static void update_used(GiftTemplate temp, String name) {
		database.GiftCodeDao.updateUsed(temp, name);
	}

	public synchronized static void execute(Player p, String[] name) throws IOException {
		if (p.level < 1) {
			p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể nhận Giftcode");
			return;
		}

		if (name == null || name.length == 0 || name[0] == null || name[0].trim().isEmpty()) {
			p.getService().send_box_ThongBao_OK("Mã quà tặng không được để trống!");
			return;
		}

		String code = name[0].trim();

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_\\-]{1,50}$");
		if (!pattern.matcher(code).matches()) {
			p.getService().send_box_ThongBao_OK("Ký tự không hợp lệ!");
			return;
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GiftTemplate temp = null;
		try {
			conn = DbManager.gI().getConnect();
			ps = conn.prepareStatement("SELECT * FROM `giftcode` WHERE `giftname` = ? LIMIT 1;");
			ps.setString(1, code);
			rs = ps.executeQuery();
			if (!rs.next()) {
				rs.close();
				ps.close();
				ps = conn.prepareStatement("SELECT * FROM `giftcode` WHERE LOWER(`giftname`) = LOWER(?) LIMIT 1;");
				ps.setString(1, code);
				rs = ps.executeQuery();
				if (!rs.next()) {
					p.getService().send_box_ThongBao_OK("Giftcode không tồn tại hoặc đã được nhập");
					return;
				}
			}
			temp = new GiftTemplate(rs.getString("giftname"), rs.getInt("luotnhap"),
					rs.getInt("gioihan"), rs.getString("thongbao"), rs.getInt("beri"),
					rs.getInt("ruby"), rs.getString("item"), rs.getString("used"),
					rs.getString("special"), rs.getByte("mtv"));
		} catch (SQLException e) {
			e.printStackTrace();
			p.getService().send_box_ThongBao_OK("Có lỗi xảy ra hãy thử lại!");
			return;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (temp != null) {
			if (temp.mtv == 1 && p.getConnStatus() != 1) {
				p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt không thể sử dụng giftcode này !");
				return;
			}
			if (temp.luotnhap >= temp.gioihan) {
				p.getService().send_box_ThongBao_OK("Giftcode này đã đạt lượt nhập tối đa!");
				return;
			}
			if (!temp.used.isEmpty()) {
				String[] used_ = temp.used.split(",");
				for (int i = 0; i < used_.length; i++) {
					String u = used_[i].trim();
					if (!u.isEmpty() && (u.equals(String.valueOf(p.IDPlayer)) || u.equalsIgnoreCase(p.name))) {
						p.getService().send_box_ThongBao_OK("Bạn đã nhập giftcode này rồi!");
						return;
					}
				}
			}
			if (!temp.special.isEmpty()) { // quà chỉ dành cho 1 số acc
				boolean can_receiv = false;
				String[] spec_ = temp.special.split(",");
				for (int i = 0; i < spec_.length; i++) {
					String s = spec_[i].trim();
					if (!s.isEmpty() && (s.equalsIgnoreCase(p.name) || (p.conn != null && s.equalsIgnoreCase(p.conn.user)))) {
						can_receiv = true;
						break;
					}
				}
				if (!can_receiv) {
					p.getService().send_box_ThongBao_OK("Bạn không có tên trong danh sách nhận giftcode này!");
					return;
				}
			}

			// Chuẩn bị danh sách quà trước để kiểm tra ô trống chính xác
			List<GiftBox> listGift = new ArrayList<>();
			if (temp.beri > 0) {
				ItemTemplate4 beriTemplate = ItemTemplate4.get_it_by_id(0);
				if (beriTemplate != null) {
					listGift.add(new GiftBox(beriTemplate, temp.beri));
				}
			}
			if (temp.ruby > 0) {
				ItemTemplate4 rubyTemplate = ItemTemplate4.get_it_by_id(1);
				if (rubyTemplate != null) {
					listGift.add(new GiftBox(rubyTemplate, temp.ruby));
				}
			}
			if (temp.type != null) {
				for (int i = 0; i < temp.type.length; i++) {
					switch (temp.type[i]) {
						case 3: {
							ItemTemplate3 it3 = ItemTemplate3.get_it_by_id(temp.id[i]);
							if (it3 != null) {
								listGift.add(new GiftBox(it3, temp.quant[i]));
							}
							break;
						}
						case 4: {
							ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(temp.id[i]);
							if (it4 != null) {
								listGift.add(new GiftBox(it4, temp.quant[i]));
							}
							break;
						}
						case 7: {
							ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(temp.id[i]);
							if (it7 != null) {
								listGift.add(new GiftBox(it7, temp.quant[i]));
							}
							break;
						}
						default: {
							GiftBox.addGift(listGift, temp.type[i], temp.id[i], temp.quant[i]);
							break;
						}
					}
				}
			}

			// Kiểm tra hành trang: nếu không đủ ô trống -> chặn nhận, không trừ giftcode, không gửi vào thư
			if (!core.RewardService.hasEnoughBagSpace(p, listGift)) {
				int needed = core.RewardService.getRequiredBagSlots(p, listGift);
				p.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống để nhận giftcode!\nCần ít nhất " + needed + " ô trống trong hành trang.");
				return;
			}

			GiftTemplate.update_used(temp, String.valueOf(p.IDPlayer));
			if (!listGift.isEmpty()) {
				String notice = temp.notice;
				if (notice == null || notice.trim().isEmpty()) {
					notice = "Bạn nhận được quà từ Giftcode!";
				}
				Service.send_gift(p, 1, "Quà Giftcode", notice, listGift, true);
			} else {
				p.getService().send_box_ThongBao_OK("Bạn đã nhận giftcode thành công!");
			}
		}
	}
}
