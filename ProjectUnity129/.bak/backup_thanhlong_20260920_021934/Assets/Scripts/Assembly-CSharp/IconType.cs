public static class IconType
{
	public const sbyte ITEM_MAP = 0;         // Thư mục: item_map
	public const sbyte MONSTER = 1;          // Thư mục: monster
	public const sbyte POTION = 2;           // Thư mục: potion
	public const sbyte ITEM = 3;             // Thư mục: items
	public const sbyte SKILL = 4;            // Thư mục: skill
	public const sbyte SKILL_SMALL = 5;      // Thư mục: skill_small
	public const sbyte NPC = 6;              // Thư mục: npc
	public const sbyte QUEST_POTION = 7;     // Thư mục: questitem
	public const sbyte MATERIAL_POTION = 8;  // Thư mục: material
	public const sbyte CLAN = 9;             // Thư mục: clan
	public const sbyte BOAT = 10;            // Thư mục: boat
	public const sbyte ITEM_OTHER = 11;      // Thư mục: dialog
	public const sbyte CHAR_PART = 12;       // Thư mục: char_part
	public const sbyte FASHION = 13;         // Thư mục: fashion
	public const sbyte SKILL_COMBO = 14;     // Thư mục: skill_combo
	public const sbyte CLAN_BIG = 15;        // Thư mục: clan_big
	public const sbyte OTHER_NEW = 16;       // Thư mục: other_new
	public const sbyte EFF_CLIENT = 17;      // Thư mục: eff
	public const sbyte EFF_CLIENT_LOW = 18;  // Thư mục: efflow

	public const int COUNT = 19;

	public static readonly string[] FOLDERS = new string[19]
	{
		"item_map", "monster", "potion", "items", "skill", "skill_small",
		"npc", "questitem", "material", "clan", "boat", "dialog",
		"char_part", "fashion", "skill_combo", "clan_big", "other_new", "eff", "efflow"
	};

	public static string getFolder(sbyte type)
	{
		if (type >= 0 && type < FOLDERS.Length)
		{
			return FOLDERS[type];
		}
		return "other";
	}

	public static sbyte getFolderType(string folder)
	{
		if (folder != null)
		{
			for (sbyte i = 0; i < FOLDERS.Length; i++)
			{
				if (FOLDERS[i].Equals(folder, System.StringComparison.OrdinalIgnoreCase))
				{
					return i;
				}
			}
		}
		return -1;
	}

	public static int pack(sbyte type, int id)
	{
		return ((type & 0xFF) << 24) | (id & 0x00FFFFFF);
	}

	public static sbyte getPackedType(int packed)
	{
		return (sbyte)((packed >> 24) & 0xFF);
	}

	public static int getPackedId(int packed)
	{
		return packed & 0x00FFFFFF;
	}

	public static bool isPacked(int packed)
	{
		return (packed >> 24) != 0;
	}

	public static string format(sbyte type, int id)
	{
		return getFolder(type) + "_" + id;
	}

	public static sbyte parseType(string strTypeId, sbyte defaultType)
	{
		if (string.IsNullOrEmpty(strTypeId)) return defaultType;
		int sep = strTypeId.IndexOf('_');
		if (sep == -1) sep = strTypeId.IndexOf('/');
		if (sep != -1)
		{
			string prefix = strTypeId.Substring(0, sep);
			sbyte t = getFolderType(prefix);
			if (t != -1) return t;
			sbyte parsedByte;
			if (sbyte.TryParse(prefix, out parsedByte)) return parsedByte;
		}
		return defaultType;
	}

	public static int parseId(string strTypeId, int defaultId)
	{
		if (string.IsNullOrEmpty(strTypeId)) return defaultId;
		int sep = strTypeId.IndexOf('_');
		if (sep == -1) sep = strTypeId.IndexOf('/');
		string idPart = (sep != -1) ? strTypeId.Substring(sep + 1) : strTypeId;
		int dot = idPart.IndexOf('.');
		if (dot != -1) idPart = idPart.Substring(0, dot);
		int parsedInt;
		if (int.TryParse(idPart, out parsedInt)) return parsedInt;
		return defaultId;
	}
}
