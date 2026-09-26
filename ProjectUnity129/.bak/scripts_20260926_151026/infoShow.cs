public class infoShow
{
	public string strShow = "";

	public sbyte color;

	public sbyte colorMain = -1;

	public int id;

	public int value;

	public static sbyte HARDCODE_CHECK_LVRQ = -100;

	public static sbyte HARDCODE_PAINT_CENTER = -99;

	public static sbyte HARDCODE_PAINT_CENTER_CHI_SO = -98;

	public static sbyte HARDCODE_KICH_AN = -97;

	public static sbyte HARDCODE_INFO_CO_BAN = 100;

	public static sbyte HARDCODE_CHECK_FULLSET_1 = 101;

	public static sbyte HARDCODE_CHECK_FULLSET_2 = 102;

	public static sbyte HARDCODE_CHECK_FULLSET_3 = 103;

	public static sbyte HARDCODE_CHECK_FULLSET_4 = 104;

	public static sbyte HARDCODE_CHECK_FULLSET_5 = 105;

	public infoShow(int id, int value, string str, sbyte color, sbyte colorMain)
	{
		strShow = str;
		this.id = id;
		this.value = value;
		this.color = color;
		this.colorMain = colorMain;
	}

	public infoShow(int id, int value, sbyte color, sbyte colorMain)
	{
		this.id = id;
		this.value = value;
		this.color = color;
		this.colorMain = colorMain;
	}

	public string getInfoFormID()
	{
		if (id < 0)
		{
			return strShow ?? "";
		}
		MainItem.checkLoadDataAttri();
		string name = "";
		sbyte isPercent = 0;
		if (MainItem.mNameAttributes != null && id >= 0 && id < MainItem.mNameAttributes.Length && MainItem.mNameAttributes[id] != null)
		{
			name = MainItem.mNameAttributes[id].name;
			isPercent = MainItem.mNameAttributes[id].ispercent;
		}
		else if (id >= 0 && id < MainItem.DEFAULT_ATTR_NAMES.Length && MainItem.DEFAULT_ATTR_NAMES[id] != null)
		{
			name = MainItem.DEFAULT_ATTR_NAMES[id];
			isPercent = MainItem.DEFAULT_ATTR_PERCENT[id];
		}
		if (!string.IsNullOrEmpty(name))
		{
			return string.Concat(name + " ", MainItem.strGetPercent(value, isPercent));
		}
		if (!string.IsNullOrEmpty(strShow))
		{
			return strShow;
		}
		return MainItem.strGetPercent(value, isPercent);
	}
}
