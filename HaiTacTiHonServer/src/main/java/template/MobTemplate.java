package template;

import java.util.ArrayList;
import java.util.List;

public class MobTemplate {
	public static List<MobTemplate> ENTRYS = new ArrayList<>();
	public int mob_id;
	public String name = "";
	public short level;
	public short hOne;
	public int hp_max;
	public byte typemove;
	public byte ishuman;
	public byte typemonster;
	public short[] wearing;
	public short icon;
	public short head;
	public short hair;
	public short[] skill;

	public static MobTemplate get_mob_template(int id) {
		if (ENTRYS != null) {
			for (MobTemplate mt : ENTRYS) {
				if (mt != null && mt.mob_id == id) {
					return mt;
				}
			}
			if (id >= 0 && id < ENTRYS.size()) {
				return ENTRYS.get(id);
			}
		}
		MobTemplate dummy = new MobTemplate();
		dummy.mob_id = id;
		dummy.name = "Mob " + id;
		return dummy;
	}
}
