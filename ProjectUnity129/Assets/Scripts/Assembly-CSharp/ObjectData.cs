using System;
using System.Collections;

public class ObjectData
{
	public const sbyte T_CHAR_PART = 7;

	public const short ITEMMAP = 0;

	public const short MONSTER = 1000;

	public const short POTION = 2000;

	public const short ITEM = 3000;

	public const short SKILL = 4000;

	public const short SKILL_SMALL = 4500;

	public const short NPC = 5000;

	public const short QUEST_POTION = 6000;

	public const short MATERIAL_POTION = 6500;

	public const short ICON_CLAN = 7000;

	public const short BOAT = 8000;

	public const short ITEM_OTHER = 9000;

	public const short CHAR_PART = 10000;

	public const short FASHION = 20000;

	public const short SKILL_COMBO = 21000;

	public const short ICON_CLAN_BIG = 22000;

	public const short IMAGE_OTHER_NEW = 23000;

	public const short IMAGE_EFF_CLIENT = 24000;

	public const short IMAGE_EFF_CLIENT_LOW = 25000;

	public const short CHAR_PART_VER2 = 26000;

	public const short GHOST = 999;

	public const short OTHER_BIG_BOSS = 0;

	public const short OTHER_TILE = 20;

	public const short OTHER_TILE_WATER = 70;

	public static mVector vecSaveImage = new mVector("ObjectData.vecSaveImage");

	public static MyHashTable HashImageItemMap = new MyHashTable(IconType.ITEM_MAP, "/server/item_map/");

	public static MyHashTable HashImageMonster = new MyHashTable(IconType.MONSTER, "/server/monster/");

	public static MyHashTable hashImageItem = new MyHashTable(IconType.ITEM, "/server/items/");

	public static MyHashTable hashImagePotion = new MyHashTable(IconType.POTION, "/server/potion/");

	public static MyHashTable hashImageSkill = new MyHashTable(IconType.SKILL, "/server/skill/");

	public static MyHashTable hashImageSkillSmall = new MyHashTable(IconType.SKILL_SMALL, "/server/skill_small/");

	public static MyHashTable hashImageNPC = new MyHashTable(IconType.NPC, "/server/npc/");

	public static MyHashTable hashImageQuestPotion = new MyHashTable(IconType.QUEST_POTION, "/server/questitem/");

	public static MyHashTable hashImageMaterialPotion = new MyHashTable(IconType.MATERIAL_POTION, "/server/material/");

	public static MyHashTable hashImageIconClan = new MyHashTable(IconType.CLAN, "/server/Clan/");

	public static MyHashTable hashImageIconClanBig = new MyHashTable(IconType.CLAN_BIG, "/server/ClanBig/");

	public static MyHashTable hashImageBoat = new MyHashTable(IconType.BOAT, "");

	public static MyHashTable hashImageItemOther = new MyHashTable(IconType.ITEM_OTHER, "/server/dialog/");

	public static MyHashTable HashImageCharPart = new MyHashTable(IconType.CHAR_PART, "/server/char_part/Small");

	public static MyHashTable HashImageFashion = new MyHashTable(IconType.FASHION, "/server/itemFashion/");

	public static MyHashTable HashImageOtherNew = new MyHashTable(IconType.OTHER_NEW, "/server/hinhtonghop/");

	public static MyHashTable HashImageEffClient = new MyHashTable(IconType.EFF_CLIENT, "/eff/");

	public static MyHashTable HashImageEffClientLow = new MyHashTable(IconType.EFF_CLIENT_LOW, "/efflow/");

	public static MyHashTable getTable(sbyte type)
	{
		return type switch
		{
			IconType.ITEM_MAP => HashImageItemMap,
			IconType.MONSTER => HashImageMonster,
			IconType.POTION => hashImagePotion,
			IconType.ITEM => hashImageItem,
			IconType.SKILL => hashImageSkill,
			IconType.SKILL_SMALL => hashImageSkillSmall,
			IconType.NPC => hashImageNPC,
			IconType.QUEST_POTION => hashImageQuestPotion,
			IconType.MATERIAL_POTION => hashImageMaterialPotion,
			IconType.CLAN => hashImageIconClan,
			IconType.BOAT => hashImageBoat,
			IconType.ITEM_OTHER => hashImageItemOther,
			IconType.CHAR_PART => HashImageCharPart,
			IconType.FASHION => HashImageFashion,
			IconType.SKILL_COMBO => hashImageSkill,
			IconType.CLAN_BIG => hashImageIconClanBig,
			IconType.OTHER_NEW => HashImageOtherNew,
			IconType.EFF_CLIENT => HashImageEffClient,
			IconType.EFF_CLIENT_LOW => HashImageEffClientLow,
			_ => null,
		};
	}

	private static readonly string[] KEY_CACHE = new string[8192];

	public static string getKey(short id)
	{
		if (id >= 0 && id < 8192)
		{
			string s = KEY_CACHE[id];
			if (s == null)
			{
				s = id.ToString();
				KEY_CACHE[id] = s;
			}
			return s;
		}
		return id.ToString();
	}

	public static MainImage getImage(sbyte type, int id)
	{
		return getImageByType(type, id);
	}

	public static MainImage getImageByType(sbyte type, int id)
	{
		MyHashTable table = getTable(type);
		if (table == null) return null;
		return getImage(table, id);
	}

	public static MainImage getImage(MyHashTable hash, int id)
	{
		if (id < 0 || hash == null) return null;
		string strId = (id >= 0 && id < 8192) ? getKey((short)id) : id.ToString();
		MainImage mainImage = (MainImage)hash.get(strId);
		if (mainImage == null)
		{
			mainImage = new MainImage();
			mainImage.timeImageNull = 1;
			hash.put(strId, mainImage);
			mainImage.img = getFromRms(id, hash.typeKey, hash);
			mainImage.set_W_H();
		}
		mainImage.count = GameCanvas.timeNow / 1000;
		if (mainImage.img == null)
		{
			if (mainImage.timeImageNull == 0 || mainImage.timeImageNull == 1 || mainImage.timeImageNull >= 100)
			{
				if (hash.typeKey >= 0)
				{
					GlobalService.gI().load_image(hash.typeKey, id);
				}
				mainImage.timeImageNull = 2;
			}
			else
			{
				mainImage.timeImageNull++;
				if (mainImage.timeImageNull >= 100)
				{
					mainImage.timeImageNull = 0;
				}
			}
		}
		return mainImage;
	}

	public static MainImage getImage(int packedTypeId)
	{
		if (IconType.isPacked(packedTypeId))
		{
			sbyte type = IconType.getPackedType(packedTypeId);
			int id = IconType.getPackedId(packedTypeId);
			return getImageByType(type, id);
		}
		return getImageByType(IconType.ITEM, packedTypeId);
	}

	public static MainImage getImage(string strTypeId)
	{
		if (string.IsNullOrEmpty(strTypeId)) return null;
		sbyte type = IconType.parseType(strTypeId, IconType.ITEM);
		int id = IconType.parseId(strTypeId, -1);
		if (id < 0) return null;
		return getImageByType(type, id);
	}

	public static MainImage getImageOther(short id, short type)
	{
		return getImageByType(IconType.OTHER_NEW, (int)(id + type));
	}

	public static MainImage getImageAll(short id, MyHashTable hash, short typeImage)
	{
		return getImageAll((int)id, hash, typeImage);
	}

	public static MainImage getImageAll(int id, MyHashTable hash, short typeImage)
	{
		if (id < 0) return null;
		if (hash != null)
		{
			return getImage(hash, id);
		}
		return null;
	}

	public static MainImage getImageAll(short id, string strId, MyHashTable hash, short typeImage)
	{
		return getImageAll((int)id, hash, typeImage);
	}

	public static MainImage getImageAll(int id, string strId, MyHashTable hash, short typeImage)
	{
		return getImageAll(id, hash, typeImage);
	}

	public static mImage getFromRms(short id, int typeImage, MyHashTable myhash)
	{
		return getFromRms((int)id, typeImage, myhash);
	}

	public static mImage getFromRms(int id, int typeImage, MyHashTable myhash)
	{
		if (id < 0) return null;
		sbyte type = (myhash != null && myhash.typeKey >= 0) ? myhash.typeKey : (sbyte)typeImage;
		sbyte[] array = null;
		if (setIdOK((short)id))
		{
			array = CRes.loadRMS("SUB_img_" + type + "_" + id);
		}
		if (array == null)
		{
			GlobalService.gI().load_image(type, id);
			return null;
		}
		try
		{
			return mImage.createImage(array, 0, array.Length);
		}
		catch (Exception)
		{
			GlobalService.gI().load_image(type, id);
			return null;
		}
	}

	public static mImage getImageServerIOS(mImage img, short id, int typeImage, MyHashTable myhash)
	{
		string text = "";
		text = typeImage switch
		{
			24000 => "/eff/g" + id + ".png", 
			4000 => (id >= 500) ? ("/server/skill_small/" + (id - 500) + ".png") : ("/server/skill/" + id + ".png"), 
			8000 => (id >= 500) ? ("/server/boat/" + (id - 500) + ".png") : ("/server/boat_part/" + id + ".png"), 
			_ => myhash.linkImage + id + ".png", 
		};
		try
		{
			img = mImage.createImage(text);
			return img;
		}
		catch (Exception)
		{
			return null;
		}
	}

	public static bool setIdOK(short id)
	{
		if (GameMidlet.DEVICE != 0 && GameMidlet.DEVICE != 4)
		{
			return true;
		}
		return false;
	}

	public static void setToRms(sbyte[] mimg, sbyte type, int id)
	{
		try
		{
			CRes.saveRMS("SUB_img_" + type + "_" + id, mimg);
		}
		catch (Exception)
		{
		}
	}

	public static void setToRms(sbyte[] mimg, short id)
	{
		try
		{
			CRes.saveRMS("SUB_image" + id, mimg);
		}
		catch (Exception)
		{
		}
	}

	public static void saveImageToRmsAndroid(sbyte[] mimg, string name)
	{
		try
		{
			CRes.saveRMS("Main_Image_" + name, mimg);
		}
		catch (Exception)
		{
		}
	}

	private static System.Collections.Generic.List<string> s_keysToRemove = new System.Collections.Generic.List<string>(64);

	public static void checkDelHash(MyHashTable hash, int time, bool isTrue)
	{
		if (hash == null) return;
		if (isTrue)
		{
			hash.clear();
			return;
		}
		if (hash == HashImageEffClient || hash == HashImageEffClientLow)
		{
			return;
		}
		if (GameMidlet.DEVICE == 4 && !isTrue)
		{
			return;
		}
		s_keysToRemove.Clear();
		IDictionaryEnumerator enumerator = hash.GetEnumerator();
		long nowSec = GameCanvas.timeNow / 1000;
		while (enumerator.MoveNext())
		{
			MainImage mainImage = (MainImage)enumerator.Value;
			if (mainImage != null && mainImage.count != -1 && nowSec - mainImage.count > time)
			{
				s_keysToRemove.Add((string)enumerator.Key);
			}
		}
		for (int i = 0; i < s_keysToRemove.Count; i++)
		{
			hash.remove(s_keysToRemove[i]);
		}
		s_keysToRemove.Clear();
	}

	public static void checkDelHash_Data(MyHashTable hash, int time, bool isTrue)
	{
		if (hash == null) return;
		if (isTrue)
		{
			hash.clear();
			return;
		}
		if (GameMidlet.DEVICE == 4 && !isTrue)
		{
			return;
		}
		s_keysToRemove.Clear();
		IDictionaryEnumerator enumerator = hash.GetEnumerator();
		long nowSec = GameCanvas.timeNow / 1000;
		while (enumerator.MoveNext())
		{
			EffectData effectData = (EffectData)enumerator.Value;
			if (effectData != null && effectData.count != -1 && nowSec - effectData.count > time)
			{
				s_keysToRemove.Add((string)enumerator.Key);
			}
		}
		for (int i = 0; i < s_keysToRemove.Count; i++)
		{
			hash.remove(s_keysToRemove[i]);
		}
		s_keysToRemove.Clear();
	}
}
