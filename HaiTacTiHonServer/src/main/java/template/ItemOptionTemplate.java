package template;

import java.util.List;

public class ItemOptionTemplate {
	public static List<ItemOptionTemplate> ENTRYS;
	public short id;
	public String name = "";
	public byte color;
	public byte percent;

	public static ItemOptionTemplate get(int id) {
		if (ENTRYS == null || id < 0) return null;
		if (id < ENTRYS.size()) {
			ItemOptionTemplate t = ENTRYS.get(id);
			if (t != null && t.id == id) return t;
		}
		for (int i = 0; i < ENTRYS.size(); i++) {
			ItemOptionTemplate t = ENTRYS.get(i);
			if (t != null && t.id == id) return t;
		}
		return null;
	}
}
