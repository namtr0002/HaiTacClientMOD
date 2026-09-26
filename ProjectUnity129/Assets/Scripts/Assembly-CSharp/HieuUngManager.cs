using System;
using System.IO;

public class HieuUngManager
{
	public static mVector vecHieuUng = new mVector();
	public static MyHashTable disabledEffectIds = new MyHashTable();
	private static bool isLoaded = false;
	private const string RMS_NAME = "RMS_EFF_SETTINGS_V1";

	public static void init()
	{
		if (!isLoaded)
		{
			loadFromRMS();
			isLoaded = true;
		}
	}

	public static bool isEffectDisabled(int id)
	{
		init();
		return disabledEffectIds.containsKey(id.ToString());
	}

	public static bool isDataSkillEffDisabled(short idEff)
	{
		if (idEff <= 0) return false;
		init();
		if (isEffectDisabled(6))
		{
			if (Player.vecDanhHieu != null)
			{
				for (int i = 0; i < Player.vecDanhHieu.size(); i++)
				{
					DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
					if (dh != null && dh.state == 2 && dh.idEff == idEff)
					{
						return true;
					}
				}
			}
		}
		return isEffectDisabled((int)idEff);
	}

	public static bool isSetUpgradeDisabled()
	{
		init();
		return isEffectDisabled(1) || isEffectDisabled(2) || isEffectDisabled(3) || isEffectDisabled(4) || isEffectDisabled(5);
	}

	public static bool isFashionEffectDisabled()
	{
		init();
		return isEffectDisabled(7);
	}

	public static bool isMasteryDisabled()
	{
		if (!AThMadaraMOD.isShowMasteryEffect) return true;
		init();
		return isEffectDisabled(8);
	}

	public static void setEffectState(int id, bool isEnabled)
	{
		init();
		if (isEnabled)
		{
			disabledEffectIds.remove(id.ToString());
		}
		else
		{
			disabledEffectIds.put(id.ToString(), 1);
		}
		saveToRMS();

		if (vecHieuUng != null)
		{
			for (int i = 0; i < vecHieuUng.size(); i++)
			{
				HieuUngInfo hu = (HieuUngInfo)vecHieuUng.elementAt(i);
				if (hu != null && hu.id == id)
				{
					hu.state = (byte)(isEnabled ? 1 : 0);
					hu.actionButtons.removeAllElements();
					if (hu.state == 1)
					{
						hu.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
					}
					else
					{
						hu.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
					}
					break;
				}
			}
		}
	}

	public static void toggleHieuUng(int id)
	{
		bool currentlyDisabled = isEffectDisabled(id);
		setEffectState(id, currentlyDisabled);
	}

	public static void scanActiveEffects()
	{
		init();
		mVector list = new mVector();

		int maxUpgrade = 0;
		if (Player.vecInventory != null)
		{
			for (int i = 0; i < Player.vecInventory.size(); i++)
			{
				MainItem item = (MainItem)Player.vecInventory.elementAt(i);
				if (item != null && item.typeObject == 3 && item.LvUpgrade > maxUpgrade)
				{
					maxUpgrade = item.LvUpgrade;
				}
			}
		}
		if (GameScreen.player != null && GameScreen.player.levelPerfect > 0 && maxUpgrade < 11)
		{
			maxUpgrade = 11 + (GameScreen.player.levelPerfect - 1);
		}

		if (maxUpgrade >= 11)
		{
			int setLv = (maxUpgrade > 15) ? 15 : maxUpgrade;
			int effId = setLv - 10;
			byte state = (byte)(isEffectDisabled(effId) ? 0 : 1);
			HieuUngInfo hu = new HieuUngInfo(effId, "Hào Quang Set +" + setLv, "[Trang bị]", (short)(-effId), (byte)effId, state, "Hào quang tỏa sáng quanh nhân vật khi trang bị set đồ cường hóa +" + setLv + ".");
			if (state == 1) hu.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
			else hu.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
			list.addElement(hu);
		}
		else
		{
			byte state = (byte)(isEffectDisabled(1) ? 0 : 1);
			HieuUngInfo hu = new HieuUngInfo(1, "Hào Quang Set +11", "[Trang bị]", -1, 1, state, "Hào quang tỏa sáng quanh nhân vật khi trang bị set đồ cường hóa +11 trở lên.");
			if (state == 1) hu.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
			else hu.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
			list.addElement(hu);
		}

		short activeTitleEff = -1;
		string activeTitleName = "";
		if (Player.vecDanhHieu != null)
		{
			for (int i = 0; i < Player.vecDanhHieu.size(); i++)
			{
				DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
				if (dh != null && dh.state == 2 && dh.idEff > 0)
				{
					activeTitleEff = dh.idEff;
					activeTitleName = dh.name;
					break;
				}
			}
		}
		byte titleState = (byte)(isEffectDisabled(6) ? 0 : 1);
		string titleDesc = (activeTitleEff > 0) ? ("Hiệu ứng ánh sáng từ danh hiệu: [" + activeTitleName + "]") : "Hiệu ứng ánh sáng vinh quang từ Danh Hiệu đang kích hoạt của nhân vật.";
		HieuUngInfo huTitle = new HieuUngInfo(6, "Hào Quang Danh Hiệu", "[Danh hiệu]", activeTitleEff, 6, titleState, titleDesc);
		if (titleState == 1) huTitle.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
		else huTitle.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
		list.addElement(huTitle);

		byte fasState = (byte)(isEffectDisabled(7) ? 0 : 1);
		HieuUngInfo huFas = new HieuUngInfo(7, "Hào Quang Thần Trang", "[Thần trang]", -10, 7, fasState, "Hiệu ứng lộng lẫy độc quyền tỏa ra từ bộ Thần Trang đang mặc trên người.");
		if (fasState == 1) huFas.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
		else huFas.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
		list.addElement(huFas);

		byte masteryState = (byte)(isEffectDisabled(8) ? 0 : 1);
		HieuUngInfo huMastery = new HieuUngInfo(8, "Vòng Sao Tinh Thông", "[Thành tích]", -11, 8, masteryState, "Vòng sao hiệu ứng cấp độ tinh thông bậc thầy hiển thị trên đầu nhân vật.");
		if (masteryState == 1) huMastery.actionButtons.addElement(new TitleActionBtn(0, "Tắt Hiệu Ứng", 2));
		else huMastery.actionButtons.addElement(new TitleActionBtn(1, "Bật Hiệu Ứng", 1));
		list.addElement(huMastery);

		vecHieuUng = list;
	}

	public static void saveToRMS()
	{
		try
		{
			if (disabledEffectIds == null || disabledEffectIds.size() == 0)
			{
				CRes.saveRMS(RMS_NAME, new sbyte[0]);
				return;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeShort((short)disabledEffectIds.size());
			MyHashTable.MyIterator keys = disabledEffectIds.keys();
			while (keys.hasNext())
			{
				string key = (string)keys.next();
				dos.writeInt(int.Parse(key));
			}
			CRes.saveRMS(RMS_NAME, baos.toByteArray());
		}
		catch (Exception) {}
	}

	public static void loadFromRMS()
	{
		disabledEffectIds = new MyHashTable();
		try
		{
			sbyte[] data = CRes.loadRMS(RMS_NAME);
			if (data != null && data.Length >= 2)
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				DataInputStream dis = new DataInputStream(bais);
				short count = dis.readShort();
				for (int i = 0; i < count; i++)
				{
					int id = dis.readInt();
					disabledEffectIds.put(id.ToString(), 1);
				}
			}
		}
		catch (Exception) {}
	}
}
