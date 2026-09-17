package template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import network.Message;

public class ItemBoat {
	public static List<ItemBoat> ENTRYS = new ArrayList<>();
	static {
		addBoat((byte)9, "Thân thuyền Gổ", (byte)0, (short)8, (short)8);
		addBoat((byte)2, "Thân thuyền rồng", (byte)0, (short)4, (short)4);
		addBoat((byte)1, "Thân thuyền vàng", (byte)0, (short)0, (short)0);
		addBoat((byte)10, "Cột buồm gổ", (byte)1, (short)9, (short)9);
		addBoat((byte)4, "Cột buồm rồng", (byte)1, (short)5, (short)5);
		addBoat((byte)3, "Cột buồm vàng", (byte)1, (short)1, (short)1);
		addBoat((byte)11, "Mái buồm gổ", (byte)2, (short)10, (short)10);
		addBoat((byte)6, "Mái buồm rồng", (byte)2, (short)6, (short)6);
		addBoat((byte)5, "Mái buồm vàng", (byte)2, (short)2, (short)2);
		addBoat((byte)12, "Thân thuyển gổ", (byte)3, (short)11, (short)11);
		addBoat((byte)8, "Cánh thuyền xanh", (byte)3, (short)7, (short)7);
		addBoat((byte)7, "Cánh thuyền vàng", (byte)3, (short)3, (short)3);
	}

	private static void addBoat(byte id, String name, byte type, short idimg, short icon) {
		ItemBoat temp = new ItemBoat();
		temp.id = id;
		temp.name = name;
		temp.type = type;
		temp.idimg = idimg;
		temp.icon = icon;
		ENTRYS.add(temp);
	}
	public byte id;
	public String name;
	public byte type;
	public short idimg;
	public short icon;

	public static void update_part_boat_when_shopping(Player p) throws IOException {
		Message m = new Message(-62);
		m.writer().writeShort(p.index_map);
		m.writer().writeByte(0);
		m.writer().writeByte(4);
		short[] part_boat = p.get_part_boat();
		m.writer().writeShort(part_boat[0]);
		m.writer().writeShort(part_boat[1]);
		m.writer().writeShort(part_boat[2]);
		m.writer().writeShort(part_boat[3]);
		p.addmsg(m);
		m.cleanup();
	}

	public static ItemBoat get_item(int id) {
		for (int i = 0; i < ItemBoat.ENTRYS.size(); i++) {
			if (ItemBoat.ENTRYS.get(i).id == id) {
				return ItemBoat.ENTRYS.get(i);
			}
		}
		return null;
	}
}
