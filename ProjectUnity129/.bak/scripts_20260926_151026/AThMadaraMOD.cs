using System;
using System.IO;

public class AThMadaraMOD
{
	public Player myChar;
	public const int MENU_AUTO_W = 11;
	public const int MENU_AUTO_H = 14;
	public const int MENU_AUTO_OFFSET_X = 3;
	public const int MENU_AUTO_OFFSET_Y = -20;
	public const int MENU_TOUCH_PAD = 4;
	private static AThMadaraMOD instance;
	public static mVector pendingDrops = new mVector();

	public static bool shouldReturnToLastMap = false;
	public static int lastMapId = -1;
	public static int lastX = -1;
	public static int lastY = -1;
	public static long nextReturnTryAt = 0;
	public static bool triedChangeMapOkOnce = false;

	public static int gameSpeed = 1;
	public static bool isAvoidPK = false;
	public static bool isAvoidBoss = false;
	public static bool isAutoReMap = false;
	public static bool isSlaughterActive = false;
	public static string slaughterTargetName = null;
	public static int slaughterTargetLevel = -1;
	public static mVector tempUniqueMonsters = null;

	public static int autoCombatMoveMode = 0; // 0: Dich chuyen, 1: Di chuyen (Di bo / Pathfind)
	public static bool isTuDanhActive = false; // Ke thua de tuong thich

	public static string getCombatMoveModeName()
	{
		return autoCombatMoveMode == 0 ? "Dịch chuyển" : "Đi bộ";
	}

	public static string getCombatStatusText()
	{
		if (GameScreen.player == null || !GameScreen.player.isAutoFireNew108)
		{
			return "[Tắt]";
		}
		string mode = (autoCombatMoveMode == 0 ? "Dịch chuyển" : "Đi bộ");
		if (slaughterTargetName != null)
		{
			return "[" + mode + " - " + slaughterTargetName + "]";
		}
		return "[" + mode + " - Tất cả]";
	}

	public static void saveCombatSettings()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(autoCombatMoveMode);
			CRes.saveRMS("MOD_COMBAT_OPT", baos.toByteArray());
			dos.close();
		}
		catch (Exception) {}
	}

	public static void loadCombatSettings()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MOD_COMBAT_OPT");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				autoCombatMoveMode = dis.readInt();
				if (autoCombatMoveMode < 0 || autoCombatMoveMode > 1) autoCombatMoveMode = 0;
				dis.close();
			}
		}
		catch (Exception) {}
	}

	public static bool isShowMobID = false;
	public static bool isShowCharID = false;
	public static bool isShowNames = true;
	public static bool isShowShadows = true;
	public static bool isOptimizeGame = false;
	public static bool isSlaughterPausedByMove = false;
	public static bool isSlaughterPausedByQuest = false;
	public static bool wasSlaughterActive = false;
	public static bool wasTuDanhActive = false;
	public static int lastPlayerHp = -1;

	public static int slaughterSelectType = 0;
	public static int slaughterPriority = 1;
	public static int slaughterTargetTypeObject = -1;

	// Đồ họa & Tối ưu mở rộng (Tách riêng từng tùy chọn)
	public static bool isShowOtherPlayers = true;
	public static bool isShowWeather = true;
	public static bool isShowPet = true;
	public static bool isShowMasteryEffect = true;
	public static bool isShowTopTitle = true;
	public static bool isShowPlayerNames = true;
	public static bool isShowMonsterNames = true;
	public static bool isHutItem = false;
	public static int pcScreenScale = 100;
	public static InputDialog inputDialogScale;

	public static bool isShowSkillPlayer
	{
		get { return GameScreen.isShowSkillPlayer; }
		set { GameScreen.isShowSkillPlayer = value; }
	}
	public static bool isAutoReconnect = false;
	public static string pendingDisconnectMsg = "";
	public static bool pendingDisconnect = false;
	public static void triggerAutoReconnect()
	{
	}
	public static void cancelAutoReconnect()
	{
		isAutoReconnect = false;
	}

	public static int autoReviveMode = 0;
	public static long reviveFallbackTime = 0;
	public static short reviveFallbackDialogId = -1;
	public static int reviveFallbackTownIndex = -1;

	public static mVector recordedMapIds = new mVector();
	public static mVector recordedPortalNames = new mVector();
	public static int lastRecordedMapId = -1;
	public static string lastSteppedPortalName = null;

	private static long lastAvoidChangeZoneTime = 0;
	private static long lastAvoidBossChangeZoneTime = 0;
	private static long nextWalkToPortalTime = 0;

	public static string getAutoReviveModeName()
	{
		switch (autoReviveMode)
		{
			case 0: return "Tat";
			case 1: return "Beri";
			case 2: return "Ruby";
			case 3: return "Tat ca";
			case 4: return "Ve lang";
			default: return "Tat";
		}
	}

	public static mVector autoSellList = new mVector("AutoSellList");

	public static mVector autoSellItemsCache = new mVector();

	public static MainItem lastProcessedItem = null;

	public static long lastProcessedTime = 0;

	public static AThMadaraMOD getInstance()
	{
		if (instance == null)
		{
			instance = new AThMadaraMOD();
			loadCombatSettings();
			loadCustomSkillEffSettings();
			loadGraphicsSettings();
			loadSpeedSetting();
			loadAutoSellList();
		}
		return instance;
	}

	public static void saveGraphicsSettings()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeBoolean(GameScreen.isShowSkillPlayer);
			dos.writeBoolean(isShowMasteryEffect);
			dos.writeBoolean(isShowTopTitle);
			dos.writeBoolean(isShowPlayerNames);
			dos.writeBoolean(isShowMonsterNames);
			dos.writeBoolean(isShowShadows);
			dos.writeBoolean(isShowMobID);
			dos.writeBoolean(isShowCharID);
			dos.writeBoolean(isHutItem);
			dos.writeBoolean(isShowOtherPlayers);
			dos.writeBoolean(isShowWeather);
			dos.writeBoolean(isShowPet);
			dos.writeBoolean(isOptimizeGame);
			CRes.saveRMS("MOD_GRAPHICS_OPT", baos.toByteArray());
			dos.close();
		}
		catch (Exception) {}
	}

	public static void loadGraphicsSettings()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MOD_GRAPHICS_OPT");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				bool skillShow = dis.readBoolean();
				GameScreen.isShowSkillPlayer = skillShow;
				isShowMasteryEffect = dis.readBoolean();
				isShowTopTitle = dis.readBoolean();
				isShowPlayerNames = dis.readBoolean();
				isShowMonsterNames = dis.readBoolean();
				isShowShadows = dis.readBoolean();
				isShowMobID = dis.readBoolean();
				isShowCharID = dis.readBoolean();
				isHutItem = dis.readBoolean();
				isShowOtherPlayers = dis.readBoolean();
				isShowWeather = dis.readBoolean();
				isShowPet = dis.readBoolean();
				isOptimizeGame = dis.readBoolean();
				dis.close();
			}
		}
		catch (Exception) {}
	}

	public static void setPresetMaxOptimization()
	{
		isShowOtherPlayers = false;
		isShowWeather = false;
		isShowPet = false;
		GameScreen.isShowSkillPlayer = false;
		isShowShadows = false;
		isShowTopTitle = false;
		isShowMasteryEffect = false;
		isOptimizeGame = true;
		saveGraphicsSettings();
		Interface_Game.addInfoPlayerNormal("Đã bật Tối Ưu Max (Tắt hết đồ họa nặng)!", mFont.tahoma_7_yellow);
	}

	public static void setPresetResetGraphics()
	{
		isShowOtherPlayers = true;
		isShowWeather = true;
		isShowPet = true;
		GameScreen.isShowSkillPlayer = true;
		isShowShadows = true;
		isShowPlayerNames = true;
		isShowMonsterNames = true;
		isShowTopTitle = true;
		isShowMasteryEffect = true;
		isShowMobID = false;
		isShowCharID = false;
		isOptimizeGame = false;
		saveGraphicsSettings();
		Interface_Game.addInfoPlayerNormal("Đã khôi phục đồ họa mặc định!", mFont.tahoma_7_yellow);
	}

	public static void openGraphicsMenu()
	{
		mVector menu = new mVector();
		menu.addElement(new iCommand("Nguoi choi khac [" + (isShowOtherPlayers ? "Bat" : "Tat") + "]", 357, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Thoi tiet Map [" + (isShowWeather ? "Bat" : "Tat") + "]", 358, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Thu cung / Pet [" + (isShowPet ? "Bat" : "Tat") + "]", 359, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hien thi Skill & Chieu [" + (GameScreen.isShowSkillPlayer ? "Bat" : "Tat") + "]", 352, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Bong nhan vat [" + (isShowShadows ? "Bat" : "Tat") + "]", 354, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Ten Nguoi choi [" + (isShowPlayerNames ? "Bat" : "Tat") + "]", 369, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Ten Quai / Boss [" + (isShowMonsterNames ? "Bat" : "Tat") + "]", 370, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hieu ung Top / Kich an [" + (isShowTopTitle ? "Bat" : "Tat") + "]", 368, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hieu ung Thong thao [" + (isShowMasteryEffect ? "Bat" : "Tat") + "]", 367, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hien ID Quai / Boss [" + (isShowMobID ? "Bat" : "Tat") + "]", 355, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hien ID Nguoi choi [" + (isShowCharID ? "Bat" : "Tat") + "]", 356, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Hut vat pham [" + (isHutItem ? "Bat" : "Tat") + "]", 371, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("* Toi Uu Max (Tat Het Do Hoa)", 360, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("~ Khoi Phuc Mac Dinh Do Hoa", 361, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
		GameCanvas.menu.startAt(menu, 2, "Do hoa & Toi uu");
	}

	public static bool isModSkill = false;

	public static void openModSkillMenu()
	{
		try
		{
			mVector menu = new mVector();
			menu.addElement(new iCommand("Mod Skill [" + (isModSkill ? "Bat" : "Tat") + "]", 310, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Cai dat Slot & Hieu ung", 318, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Khoi phuc mac dinh tat ca", 317, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Mod Skill (Doi Hieu Ung)");
		}
		catch (Exception) {}
	}

	public static void openCombatUtilitiesMenu()
	{
		try
		{
			mVector menu = new mVector();
			menu.addElement(new iCommand("Gom quai [" + ((GameScreen.player != null && GameScreen.player.isGomQuai112) ? "Bat" : "Tat") + "]", 112, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Dong bang quai [" + ((GameScreen.player != null && GameScreen.player.isDongBangQuai111) ? "Bat" : "Tat") + "]", 111, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Ne PK [" + (isAvoidPK ? "Bat" : "Tat") + "]", 129, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Ne Boss [" + (isAvoidBoss ? "Bat" : "Tat") + "]", 130, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu ve map cu [" + (isAutoReMap ? "Bat" : "Tat") + "]", 128, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Tien ich chien dau");
		}
		catch (Exception) {}
	}

	public static void openActivitiesMenu()
	{
		try
		{
			mVector menu = new mVector();
			menu.addElement(new iCommand("Auto NV Lap [" + (GameScreen.isOnRepeatQuest ? "Bat" : "Tat") + "]", 99, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto San Boss [" + (GameScreen.isOnSuperBoss ? "Bat" : "Tat") + "]", 101, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Chien Truong [" + (GameScreen.isOnAutoPB ? "Bat" : "Tat") + "]", 107, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Hoat dong & Nhiem vu");
		}
		catch (Exception) {}
	}

	public static void openSystemMenu()
	{
		try
		{
			mVector menu = new mVector();
			menu.addElement(new iCommand("Toc do game [" + gameSpeed + "/10]", 114, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("FPS [" + MotherCanvas.targetFPS + " fps]", 120, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Che do phim [" + (Interface_Game.typeTouch == 0 ? "Keypad/D-Pad" : "Touch/Cam ung") + "]", 136, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "He thong & Toc do");
		}
		catch (Exception) {}
	}

	public void openMenuAuto()
	{
		try
		{
			mVector menu = new mVector();
			menu.addElement(new iCommand("Tu Danh / Tan Sat " + getCombatStatusText(), 108, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Skill", 113, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Mod Skill [" + (isModSkill ? "Bat" : "Tat") + "]", 134, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tien ich chien dau", 341, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu hoi sinh [" + getAutoReviveModeName() + "]", 118, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu dong ban do [" + (Player.isAutoFilterItems ? "Bat" : "Tat") + "]", 119, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Hoat dong & NV", 342, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("He thong & Toc do", 343, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Do hoa & Toi uu", 351, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Menu Auto");
		}
		catch (Exception)
		{
		}
	}

	public static void openAutoCombatMenu()
	{
		try
		{
			mVector menu = new mVector();
			bool isCombatActive = GameScreen.player != null && GameScreen.player.isAutoFireNew108;
			string allPrefix = (isCombatActive && slaughterTargetName == null) ? "* " : "  ";
			menu.addElement(new iCommand(allPrefix + "Tan sat: Tat ca", 300, 0, GameCanvas.gameScr));
			if (GameScreen.vecPlayers != null)
			{
				mVector uniqueMonsters = new mVector();
				for (int i = 0; i < GameScreen.vecPlayers.size(); i++)
				{
					MainObject obj = (MainObject)GameScreen.vecPlayers.elementAt(i);
					if (obj == null || obj.typeObject != 1 || obj.isRemove || obj.isDie)
					{
						continue;
					}
					bool exists = false;
					for (int j = 0; j < uniqueMonsters.size(); j++)
					{
						MainObject m = (MainObject)uniqueMonsters.elementAt(j);
						if (m.name == obj.name && m.Lv == obj.Lv)
						{
							exists = true;
							break;
						}
					}
					if (!exists)
					{
						uniqueMonsters.addElement(obj);
					}
				}
				tempUniqueMonsters = uniqueMonsters;
				for (int i = 0; i < uniqueMonsters.size(); i++)
				{
					MainObject m = (MainObject)uniqueMonsters.elementAt(i);
					bool isCur = isCombatActive && slaughterTargetName != null && slaughterTargetName == m.name && slaughterTargetLevel == m.Lv;
					string prefix = isCur ? "* " : "  ";
					menu.addElement(new iCommand(prefix + m.name + " (Lv." + m.Lv + ")", 301, i, GameCanvas.gameScr));
				}
			}
			menu.addElement(new iCommand("Kieu di chuyen: [" + getCombatMoveModeName() + "]", 303, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tat Tu Danh / Tan Sat", 302, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Tu Danh / Tan Sat");
		}
		catch (Exception)
		{
		}
	}

	public static void openSlaughterMenu()
	{
		openAutoCombatMenu();
	}

	public static short[] customSlotEffects = new short[5] { -1, -1, -1, -1, -1 };
	public static System.Collections.Generic.List<short>[] customMultiSlotEffects = new System.Collections.Generic.List<short>[5]
	{
		new System.Collections.Generic.List<short>(),
		new System.Collections.Generic.List<short>(),
		new System.Collections.Generic.List<short>(),
		new System.Collections.Generic.List<short>(),
		new System.Collections.Generic.List<short>()
	};
	public static int selectedSlotToMod = 0;
	public static InputDialog inputDialogCustomEff;

	public static int currentSelectingSubMenuType = 1; // 1: Devil Fruit, 2: 5 Classes

	public static int getSkillSlot(Skill_Info sk)
	{
		if (sk == null) return -1;
		if (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4)) return 4;
		if (sk.typeSkill == 1 && Player.vecListSkill != null)
		{
			mVector uniqueActive = new mVector();
			for (int i = 0; i < Player.vecListSkill.size(); i++)
			{
				Skill_Info s = (Skill_Info)Player.vecListSkill.elementAt(i);
				if (s != null && s.typeSkill == 1)
				{
					bool exists = false;
					for (int j = 0; j < uniqueActive.size(); j++)
					{
						Skill_Info ex = (Skill_Info)uniqueActive.elementAt(j);
						if (ex != null && ex.ID == s.ID) { exists = true; break; }
					}
					if (!exists) uniqueActive.addElement(s);
				}
			}
			for (int i = 0; i < uniqueActive.size(); i++)
			{
				Skill_Info s = (Skill_Info)uniqueActive.elementAt(i);
				if (s != null && s.ID == sk.ID) return i;
			}
		}
		return -1;
	}

	public static bool hasCustomEffect(int slot, short effId)
	{
		if (slot >= 0 && slot < 5 && customMultiSlotEffects[slot] != null)
		{
			return customMultiSlotEffects[slot].Contains(effId);
		}
		return false;
	}

	public static void toggleCustomSkillEff(int slot, short effId)
	{
		if (slot >= 0 && slot < 5 && effId > 0)
		{
			if (customMultiSlotEffects[slot] == null)
			{
				customMultiSlotEffects[slot] = new System.Collections.Generic.List<short>();
			}
			if (customMultiSlotEffects[slot].Contains(effId))
			{
				customMultiSlotEffects[slot].Remove(effId);
				Interface_Game.addInfoPlayerNormal("Đã BỎ Eff " + effId + " khỏi Slot " + slot, mFont.tahoma_7_yellow);
			}
			else
			{
				customMultiSlotEffects[slot].Add(effId);
				Interface_Game.addInfoPlayerNormal("Đã THÊM [*] Eff " + effId + " vào Slot " + slot, mFont.tahoma_7_yellow);
			}
			short orig = getDefaultEffForSlot(slot);
			customSlotEffects[slot] = (customMultiSlotEffects[slot].Count > 0) ? customMultiSlotEffects[slot][0] : (orig > 0 ? orig : (short)0);
			saveCustomSkillEffSettings();

			// Reload menu seamlessly without closing UI
			if (currentSelectingSubMenuType == 1)
			{
				openDevilFruitSkillsMenu(slot);
			}
			else if (currentSelectingSubMenuType == 2)
			{
				openStandardSkillTemplatesMenu(slot);
			}
		}
	}

	public static short getDefaultEffForSlot(int slot)
	{
		if (Player.vecListSkill != null && slot >= 0 && slot < 5)
		{
			if (slot == 4)
			{
				for (int i = 0; i < Player.vecListSkill.size(); i++)
				{
					Skill_Info sk = (Skill_Info)Player.vecListSkill.elementAt(i);
					if (sk != null && (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4)))
					{
						return sk.typeEffSkill;
					}
				}
			}
			else
			{
				mVector uniqueActive = new mVector();
				for (int i = 0; i < Player.vecListSkill.size(); i++)
				{
					Skill_Info s = (Skill_Info)Player.vecListSkill.elementAt(i);
					if (s != null && s.typeSkill == 1)
					{
						bool exists = false;
						for (int j = 0; j < uniqueActive.size(); j++)
						{
							Skill_Info ex = (Skill_Info)uniqueActive.elementAt(j);
							if (ex != null && ex.ID == s.ID) { exists = true; break; }
						}
						if (!exists) uniqueActive.addElement(s);
					}
				}
				if (slot < uniqueActive.size())
				{
					Skill_Info sk = (Skill_Info)uniqueActive.elementAt(slot);
					if (sk != null) return sk.typeEffSkill;
				}
			}
		}
		return -1;
	}

	public static System.Collections.Generic.List<short> getCustomSkillEffList(Skill_Info sk, short defaultEff)
	{
		System.Collections.Generic.List<short> list = new System.Collections.Generic.List<short>();
		short validDefault = (defaultEff > 0) ? defaultEff : ((sk != null && sk.typeEffSkill > 0) ? sk.typeEffSkill : (short)0);
		if (!isModSkill || sk == null)
		{
			if (validDefault > 0) list.Add(validDefault);
			return list;
		}
		int slot = getSkillSlot(sk);
		if (slot >= 0 && slot < 5)
		{
			if (customMultiSlotEffects[slot] == null)
			{
				customMultiSlotEffects[slot] = new System.Collections.Generic.List<short>();
			}
			System.Collections.Generic.List<short> validList = new System.Collections.Generic.List<short>();
			for (int i = 0; i < customMultiSlotEffects[slot].Count; i++)
			{
				short val = customMultiSlotEffects[slot][i];
				if (val > 0)
				{
					validList.Add(val);
				}
			}
			if (validList.Count > 0)
			{
				return validList;
			}
		}
		if (validDefault > 0) list.Add(validDefault);
		return list;
	}

	public static short getCustomSkillEff(Skill_Info sk, short defaultEff)
	{
		if (!isModSkill)
		{
			return (defaultEff > 0) ? defaultEff : ((sk != null && sk.typeEffSkill > 0) ? sk.typeEffSkill : defaultEff);
		}
		System.Collections.Generic.List<short> list = getCustomSkillEffList(sk, defaultEff);
		return (list.Count > 0) ? list[0] : defaultEff;
	}

	public static void saveCustomSkillEffSettings()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeBoolean(isModSkill);
			for (int i = 0; i < 5; i++)
			{
				int count = (customMultiSlotEffects[i] != null) ? customMultiSlotEffects[i].Count : 0;
				dos.writeInt(count);
				for (int j = 0; j < count; j++)
				{
					dos.writeShort(customMultiSlotEffects[i][j]);
				}
			}
			CRes.saveRMS("MOD_SKILL_SLOT_EFF_MULTI", baos.toByteArray());
			dos.close();
		}
		catch (Exception) {}
	}

	public static void loadCustomSkillEffSettings()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MOD_SKILL_SLOT_EFF_MULTI");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				try
				{
					isModSkill = dis.readBoolean();
				}
				catch (Exception)
				{
					isModSkill = false;
				}
				for (int i = 0; i < 5; i++)
				{
					int count = dis.readInt();
					if (customMultiSlotEffects[i] == null)
					{
						customMultiSlotEffects[i] = new System.Collections.Generic.List<short>();
					}
					customMultiSlotEffects[i].Clear();
					for (int j = 0; j < count; j++)
					{
						short val = dis.readShort();
						if (val > 0)
						{
							customMultiSlotEffects[i].Add(val);
						}
					}
					short orig = getDefaultEffForSlot(i);
					if (customMultiSlotEffects[i].Count == 0 && orig > 0)
					{
						customMultiSlotEffects[i].Add(orig);
					}
					customSlotEffects[i] = (customMultiSlotEffects[i].Count > 0) ? customMultiSlotEffects[i][0] : (orig > 0 ? orig : (short)0);
				}
				dis.close();
			}
		}
		catch (Exception) {}
	}

	public static string getEffectName(short effId)
	{
		if (effId <= 0) return "Mặc định (Gốc)";
		return effId switch
		{
			// Trái Cao Su
			164 => "Trái Cao Su - Cú Đấm Cao Su",
			227 => "Trái Cao Su - Súng Máy Cao Su",

			// Trái Lửa (Ace)
			2 => "Trái Lửa (Ace) - Hỏa Súng",
			228 => "Trái Lửa (Ace) - Cột Hỏa Thiên (L1)",
			3 => "Trái Lửa (Ace) - Hỏa Cầu Nổ",
			229 => "Trái Lửa (Ace) - Đại Hỏa Cầu (L1)",
			259 => "Trái Lửa (Ace) - Hỏa Súng Super L1",
			260 => "Trái Lửa (Ace) - Hỏa Súng Super L2",
			261 => "Trái Lửa (Ace) - Hỏa Súng Super L3",
			262 => "Trái Lửa (Ace) - Hỏa Cầu Super L1",
			263 => "Trái Lửa (Ace) - Hỏa Cầu Super L2",
			264 => "Trái Lửa (Ace) - Hỏa Cầu Super L3",

			// Trái Băng (Aokiji)
			4 => "Trái Băng (Aokiji) - Băng Đao",
			230 => "Trái Băng (Aokiji) - Chim Băng (L1)",
			5 => "Trái Băng (Aokiji) - Băng Trụ",
			231 => "Trái Băng (Aokiji) - Đại Băng Trụ (L1)",

			// Trái Khói (Smoker)
			6 => "Trái Khói (Smoker) - Quyền Khói L1",
			232 => "Trái Khói (Smoker) - Quyền Khói L2",
			10 => "Trái Khói (Smoker) - Long Phong Khói L1",
			234 => "Trái Khói (Smoker) - Long Phong Khói L2",

			// Trái Cát (Crocodile)
			25 => "Trái Cát (Crocodile) - Lưỡi Cát L1",
			235 => "Trái Cát (Crocodile) - Lưỡi Cát L2",
			26 => "Trái Cát (Crocodile) - Bão Cát Sa Mạc L1",
			236 => "Trái Cát (Crocodile) - Bão Cát Sa Mạc L2",

			// Trái Sấm (Enel)
			169 => "Trái Sấm (Enel) - Sét Đánh L1",
			237 => "Trái Sấm (Enel) - Sét Đánh L2",
			170 => "Trái Sấm (Enel) - Cột Sấm L1",
			238 => "Trái Sấm (Enel) - Cột Sấm L2",
			1998 => "Trái Sấm (Enel) - Bão Sét Giội Xuống 1",
			1999 => "Trái Sấm (Enel) - Bão Sét Giội Xuống 2",

			// Trái Nham Thạch (Akainu)
			171 => "Trái Nham Thạch (Akainu) - Nham Quyền L1",
			239 => "Trái Nham Thạch (Akainu) - Nham Quyền L2",
			172 => "Trái Nham Thạch (Akainu) - Cột Nham Thạch L1",
			240 => "Trái Nham Thạch (Akainu) - Cột Nham Thạch L2",

			// Trái Chấn Động (Whitebeard)
			210 => "Trái Chấn Động (Whitebeard) - Chấn Quyền L1",
			243 => "Trái Chấn Động (Whitebeard) - Chấn Quyền L2",
			211 => "Trái Chấn Động (Whitebeard) - Sóng Âm L1",
			244 => "Trái Chấn Động (Whitebeard) - Sóng Âm L2",

			// Trái Chim Ứng (Pell)
			179 => "Trái Chim Ứng (Pell) - Vũ Điệu Chim Ứng L1",
			241 => "Trái Chim Ứng (Pell) - Vũ Điệu Chim Ứng L2",

			// Trái Lucci (Zoan Báo)
			209 => "Trái Lucci - Lục Thức Báo L1",
			242 => "Trái Lucci - Lục Thức Báo L2",

			// Trái Nổ (Mr.5 & Đại Nổ)
			233 => "Trái Nổ (Mr.5) - Vụ Nổ Thường",
			2000 => "Trái Nổ - Hỏa Nổ Trái Ác Quỷ",

			// Trái Dao, Sáp, Kilo
			245 => "Trái Dao - Trảm Dao L1",
			251 => "Trái Dao - Trảm Dao L2",
			249 => "Trái Dao 2 - Xoay Phao L1",
			252 => "Trái Dao 2 - Xoay Phao L2",
			246 => "Trái Sáp (Nến) - Khiên Nến L1",
			253 => "Trái Sáp (Nến) - Khiên Nến L2",
			247 => "Trái Sáp 2 - Trụ Nến L1",
			254 => "Trái Sáp 2 - Trụ Nến L2",
			248 => "Trái Kilo - Quả Cân 10000kg L1",
			255 => "Trái Kilo - Quả Cân 10000kg L2",

			// Lục Thức CP9 & Zoan
			266 => "Lục Thức - Rankyaku (Trảm Phong)",
			267 => "Lục Thức - Shigan (Chỉ Súng)",
			268 => "Lục Thức - Door (Trái Cửa L1)",
			269 => "Lục Thức - Door (Trái Cửa L2)",
			270 => "Lục Thức - Kumadori Tóc Rắn",
			274 => "Trái Xà Phòng - Bong Bóng L1",
			275 => "Trái Xà Phòng - Bong Bóng L2",
			276 => "Trái Sói Zoan - Cắn Xé L1",
			277 => "Trái Sói Zoan - Cắn Xé L2",
			278 => "Trái Hươu Zoan - Húc Hươu L1",
			279 => "Trái Hươu Zoan - Húc Hươu L2",
			280 => "Goal - Sút Bóng Thần Thoại",

			// Thần Thoại & Ác Quỷ Đặc Biệt (Ace, Kizaru, Hancock)
			400 => "Trái Ace - Skill 1 (Cấp <5)",
			401 => "Trái Ace - Skill 2 (Cấp <5)",
			402 => "Trái Ace - Skill 1 (Cấp =5 Super)",
			403 => "Trái Ace - Skill 2 (Cấp =5 Super)",
			404 => "Trái Ánh Sáng Kizaru - Skill 1 (Cấp <5)",
			405 => "Trái Ánh Sáng Kizaru - Skill 2 (Cấp <5)",
			406 => "Trái Ánh Sáng Kizaru - Skill 1 (Cấp =5 Super)",
			407 => "Trái Ánh Sáng Kizaru - Skill 2 (Cấp =5 Super)",
			408 => "Trái Tình Yêu Hancock - Skill 1 (Cấp <5)",
			409 => "Trái Tình Yêu Hancock - Skill 2 (Cấp <5)",
			410 => "Trái Tình Yêu Hancock - Skill 1 (Cấp =5 Super)",
			411 => "Trái Tình Yêu Hancock - Skill 2 (Cấp =5 Super)",
			3100 => "Trái Nika - Nắm đấm của Thần (Cấp <5)",
			3101 => "Trái Nika - Nắm đấm của Thần (Cấp =5 Super)",
			3102 => "Trái Nika - Số 4: Snakeman",
			3103 => "Trái Nika - Hơi thở của Thần (Buff)",

			// Kiếm sĩ (Zoro)
			38 => "Kiếm sĩ - Nhất kiếm (Cấp 1)",
			15 => "Kiếm sĩ - Nhất kiếm (Cấp 10)",
			86 => "Kiếm sĩ - Nhất kiếm (Cấp 15)",
			183 => "Kiếm sĩ - Nhất kiếm (Cấp 20)",
			215 => "Kiếm sĩ - Nhất kiếm (Cấp 25)",
			281 => "Kiếm sĩ - Nhất kiếm (Cấp 29)",
			481 => "Kiếm sĩ - Nhất kiếm (Cấp 30 Max)",
			41 => "Kiếm sĩ - Skill 2 (Cấp 1)",
			216 => "Kiếm sĩ - Skill 2 (Cấp 20)",
			482 => "Kiếm sĩ - Skill 2 (Cấp 30 Max)",
			121 => "Kiếm sĩ - Skill 3 (Cấp 1)",
			217 => "Kiếm sĩ - Skill 3 (Cấp 20)",
			483 => "Kiếm sĩ - Skill 3 (Cấp 30 Max)",
			42 => "Kiếm sĩ - Skill Trên Biển",

			// Võ sĩ (Luffy)
			21 => "Võ sĩ - Quả đấm tốc độ (Cấp 1)",
			33 => "Võ sĩ - Quả đấm tốc độ (Cấp 5)",
			83 => "Võ sĩ - Quả đấm tốc độ (Cấp 10)",
			180 => "Võ sĩ - Quả đấm tốc độ (Cấp 15)",
			212 => "Võ sĩ - Quả đấm tốc độ (Cấp 20)",
			271 => "Võ sĩ - Quả đấm tốc độ (Cấp 25)",
			471 => "Võ sĩ - Quả đấm tốc độ (Cấp 30 Max)",
			34 => "Võ sĩ - Skill 2 (Cấp 1)",
			472 => "Võ sĩ - Skill 2 (Cấp 30 Max)",
			1 => "Võ sĩ - Skill 3 (Cấp 1)",
			473 => "Võ sĩ - Skill 3 (Cấp 30 Max)",
			133 => "Võ sĩ - Skill Trên Biển",

			// Đầu bếp (Sanji)
			14 => "Đầu bếp - Hắc cước (Cấp 1)",
			44 => "Đầu bếp - Hắc cước (Cấp 10)",
			124 => "Đầu bếp - Hắc cước (Cấp 15)",
			186 => "Đầu bếp - Hắc cước (Cấp 20)",
			218 => "Đầu bếp - Hắc cước (Cấp 25)",
			491 => "Đầu bếp - Hắc cước (Cấp 30 Max)",
			47 => "Đầu bếp - Skill 2 (Cấp 1)",
			492 => "Đầu bếp - Skill 2 (Cấp 30 Max)",
			49 => "Đầu bếp - Skill 3 (Cấp 1)",
			493 => "Đầu bếp - Skill 3 (Cấp 30 Max)",
			136 => "Đầu bếp - Skill Trên Biển",

			// Hoa tiêu (Nami)
			16 => "Hoa tiêu - Double Shot (Cấp 1)",
			511 => "Hoa tiêu - Double Shot (Cấp 30 Max)",
			9 => "Hoa tiêu - Skill 2 (Cấp 1)",
			512 => "Hoa tiêu - Skill 2 (Cấp 30 Max)",
			31 => "Hoa tiêu - Skill 3 (Cấp 1)",
			513 => "Hoa tiêu - Skill 3 (Cấp 30 Max)",
			11 => "Hoa tiêu - Skill Trên Biển",

			// Xạ thủ (Usopp)
			57 => "Xạ thủ - Gậy chong chóng (Cấp 1)",
			501 => "Xạ thủ - Gậy chong chóng (Cấp 30 Max)",
			64 => "Xạ thủ - Skill 2 (Cấp 1)",
			502 => "Xạ thủ - Skill 2 (Cấp 30 Max)",
			67 => "Xạ thủ - Skill 3 (Cấp 1)",
			503 => "Xạ thủ - Skill 3 (Cấp 30 Max)",
			7 => "Xạ thủ - Skill Trên Biển",

			_ => "Eff ID " + effId
		};
	}

	public static void openModSkillSelectSlotMenu()
	{
		try
		{
			mVector menu = new mVector();
			int activeIdx = 0;

			if (Player.vecListSkill != null && Player.vecListSkill.size() > 0)
			{
				mVector uniqueActiveSkills = new mVector();
				for (int i = 0; i < Player.vecListSkill.size(); i++)
				{
					Skill_Info sk = (Skill_Info)Player.vecListSkill.elementAt(i);
					if (sk == null) continue;
					if (sk.typeSkill != 1 && sk.typeSkill != 4 && !(sk.ID == 3 && sk.typeSkill == 4)) continue;

					bool exists = false;
					for (int j = 0; j < uniqueActiveSkills.size(); j++)
					{
						Skill_Info existSk = (Skill_Info)uniqueActiveSkills.elementAt(j);
						if (existSk != null && existSk.ID == sk.ID)
						{
							exists = true;
							break;
						}
					}
					if (!exists)
					{
						uniqueActiveSkills.addElement(sk);
					}
				}

				for (int i = 0; i < uniqueActiveSkills.size(); i++)
				{
					Skill_Info sk = (Skill_Info)uniqueActiveSkills.elementAt(i);
					if (sk == null) continue;

					int slot = -1;
					string slotPrefix = "";
					if (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4))
					{
						slot = 4;
						slotPrefix = "Skill Biển";
					}
					else if (sk.typeSkill == 1)
					{
						slot = activeIdx;
						slotPrefix = "Slot " + (activeIdx + 1);
						activeIdx++;
					}

					if (slot >= 0 && slot < 5)
					{
						int effCount = (customMultiSlotEffects[slot] != null) ? customMultiSlotEffects[slot].Count : 0;
						string effStatus = (effCount > 1) ? (" [" + effCount + " Eff]") : (effCount == 1 ? (" [Eff: " + customMultiSlotEffects[slot][0] + "]") : (" [Gốc: " + sk.typeEffSkill + "]"));
						string title = slotPrefix + ": " + sk.name + effStatus;
						menu.addElement(new iCommand(title, 311, slot, GameCanvas.gameScr));
					}
				}
			}

			if (menu.size() == 0)
			{
				for (int i = 0; i < 5; i++)
				{
					string slotName = (i == 4) ? "Skill Biển" : ("Slot " + (i + 1));
					int effCount = (customMultiSlotEffects[i] != null) ? customMultiSlotEffects[i].Count : 0;
					string effStatus = (effCount > 0) ? (" [" + effCount + " Eff]") : " [Mặc định]";
					menu.addElement(new iCommand(slotName + effStatus, 311, i, GameCanvas.gameScr));
				}
			}

			menu.addElement(new iCommand("Khôi Phục TẤT CẢ Về Mặc Định", 317, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lại", 134, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "MOD HIỆU ỨNG SKILL");
		}
		catch (Exception) {}
	}

	public static void openModSkillChooseEffMenu(int slot)
	{
		try
		{
			selectedSlotToMod = slot;
			mVector menu = new mVector();
			string slotName = (slot == 4) ? "SKILL BIỂN" : ("SLOT " + (slot + 1));
			int totalEffs = (customMultiSlotEffects[slot] != null) ? customMultiSlotEffects[slot].Count : 0;
			
			menu.addElement(new iCommand("1. Danh Sách Trái Ác Quỷ (" + totalEffs + " đã chọn)", 313, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("2. Danh Sách Skill 5 Phái (" + totalEffs + " đã chọn)", 314, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("3. Nhập Mã Eff ID Tùy Chỉnh", 315, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("4. Khôi Phục Slot Này Về Mặc Định", 316, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lại", 134, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "CÀI ĐẶT " + slotName);
		}
		catch (Exception) {}
	}

	public static void openDevilFruitSkillsMenu(int slot)
	{
		try
		{
			selectedSlotToMod = slot;
			currentSelectingSubMenuType = 1;
			mVector menu = new mVector();

			short defaultEff = getDefaultEffForSlot(slot);
			if (defaultEff > 0)
			{
				bool isAdded = hasCustomEffect(slot, defaultEff);
				string prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(defaultEff) + " [GỐC CỦA SKILL]", 312, defaultEff, GameCanvas.gameScr));
			}

			short[] devilEffs = new short[]
			{
				164, 227, 2, 228, 3, 229, 259, 260, 261, 262, 263, 264,
				4, 230, 5, 231, 6, 232, 10, 234, 25, 235, 26, 236,
				169, 237, 170, 238, 1998, 1999, 171, 239, 172, 240,
				210, 243, 211, 244, 179, 241, 209, 242, 233, 2000,
				245, 251, 249, 252, 246, 253, 247, 254, 248, 255,
				266, 267, 268, 269, 270, 274, 275, 276, 277, 278, 279, 280,
				400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
				3100, 3101, 3102, 3103
			};

			for (int i = 0; i < devilEffs.Length; i++)
			{
				short effId = devilEffs[i];
				if (effId == defaultEff) continue;
				bool isAdded = hasCustomEffect(slot, effId);
				string prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(effId), 312, effId, GameCanvas.gameScr));
			}

			menu.addElement(new iCommand("< Quay lại", 311, slot, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Trái Ác Quỷ (Ấn chọn để THÊM/XÓA)");
		}
		catch (Exception) {}
	}

	public static void openStandardSkillTemplatesMenu(int slot)
	{
		try
		{
			selectedSlotToMod = slot;
			currentSelectingSubMenuType = 2;
			mVector menu = new mVector();

			short defaultEff = getDefaultEffForSlot(slot);
			if (defaultEff > 0)
			{
				bool isAdded = hasCustomEffect(slot, defaultEff);
				String prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(defaultEff) + " [GỐC CỦA SKILL]", 312, defaultEff, GameCanvas.gameScr));
			}

			short[] stdEffs = new short[]
			{
				// Kiếm sĩ (Zoro)
				38, 15, 86, 183, 215, 481, 41, 482, 121, 483, 42,
				// Võ sĩ (Luffy)
				21, 83, 212, 471, 34, 472, 1, 473, 133,
				// Đầu bếp (Sanji)
				14, 44, 218, 491, 492, 493, 136,
				// Hoa tiêu (Nami)
				16, 511, 512, 513, 11,
				// Xạ thủ (Usopp)
				57, 501, 502, 503, 7,
				// Kỹ năng đặc biệt / Lục Thức
				266, 267, 268, 274, 276, 278, 246
			};

			for (int i = 0; i < stdEffs.Length; i++)
			{
				short effId = stdEffs[i];
				if (effId == defaultEff) continue;
				bool isAdded = hasCustomEffect(slot, effId);
				string prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(effId), 312, effId, GameCanvas.gameScr));
			}

			menu.addElement(new iCommand("< Quay lại", 311, slot, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Skill 5 Phái (Ấn chọn để THÊM/XÓA)");
		}
		catch (Exception) {}
	}

	public static void setCustomSkillEff(int slot, short effId)
	{
		toggleCustomSkillEff(slot, effId);
	}

	public static void resetSlotCustomSkillEff(int slot)
	{
		if (slot >= 0 && slot < 5)
		{
			if (customMultiSlotEffects[slot] != null)
			{
				customMultiSlotEffects[slot].Clear();
			}
			else
			{
				customMultiSlotEffects[slot] = new System.Collections.Generic.List<short>();
			}
			short orig = getDefaultEffForSlot(slot);
			if (orig > 0)
			{
				customMultiSlotEffects[slot].Add(orig);
				customSlotEffects[slot] = orig;
			}
			else
			{
				customSlotEffects[slot] = 0;
			}
			saveCustomSkillEffSettings();
			Interface_Game.addInfoPlayerNormal("Đã khôi phục Slot " + slot + " về Mặc Định", mFont.tahoma_7_yellow);
			openModSkillChooseEffMenu(slot);
		}
	}

	public static void resetAllCustomSkillEff()
	{
		for (int i = 0; i < 5; i++)
		{
			if (customMultiSlotEffects[i] != null)
			{
				customMultiSlotEffects[i].Clear();
			}
			else
			{
				customMultiSlotEffects[i] = new System.Collections.Generic.List<short>();
			}
			short orig = getDefaultEffForSlot(i);
			if (orig > 0)
			{
				customMultiSlotEffects[i].Add(orig);
				customSlotEffects[i] = orig;
			}
			else
			{
				customSlotEffects[i] = 0;
			}
		}
		saveCustomSkillEffSettings();
		Interface_Game.addInfoPlayerNormal("Đã khôi phục tất cả Skill về Mặc Định", mFont.tahoma_7_yellow);
		openModSkillSelectSlotMenu();
	}



	public static void saveSpeedSetting(int speed)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(speed);
			CRes.saveRMS("MOD_GAME_SPEED", baos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static void loadSpeedSetting()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MOD_GAME_SPEED");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				gameSpeed = dis.readInt();
				if (gameSpeed < 1)
				{
					gameSpeed = 1;
				}
				if (gameSpeed > 10)
				{
					gameSpeed = 10;
				}
				dis.close();
			}
		}
		catch (Exception)
		{
		}
	}

	public void openMenuSellItem()
	{
		try
		{
			TabScreen tabAutoSellScr = new TabScreen(MainTab.xTab, 0);
			mVector tabs = new mVector();
			TabAutoSellBag tabBag = new TabAutoSellBag("Hành Trang", Player.vecInventory, MainTab.xTab);
			tabBag.initCmd();
			tabs.addElement(tabBag);
			TabAutoSellList tabList = new TabAutoSellList("DS Tự Bán", autoSellItemsCache, MainTab.xTab);
			tabList.initCmd();
			tabs.addElement(tabList);
			tabAutoSellScr.addVecTab(tabs);
			tabAutoSellScr.Show(GameCanvas.gameScr);
		}
		catch (Exception)
		{
		}
	}

	public static bool isInAutoSell(MainItem mi)
	{
		if (mi == null)
		{
			return false;
		}
		for (int i = 0; i < autoSellList.size(); ++i)
		{
			short[] s = (short[])autoSellList.elementAt(i);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID)
			{
				return true;
			}
		}
		return false;
	}

	public static void removeFromAutoSell(MainItem mi)
	{
		if (mi == null)
		{
			return;
		}
		bool removed = false;
		for (int i = 0; i < autoSellList.size(); ++i)
		{
			short[] s = (short[])autoSellList.elementAt(i);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID)
			{
				autoSellList.removeElementAt(i);
				removed = true;
				break;
			}
		}
		for (int j = 0; j < autoSellItemsCache.size(); j++)
		{
			MainItem cached = (MainItem)autoSellItemsCache.elementAt(j);
			if (cached != null && cached.typeObject == mi.typeObject && cached.ID == mi.ID)
			{
				autoSellItemsCache.removeElementAt(j);
				removed = true;
				break;
			}
		}
		if (removed)
		{
			saveAutoSellList();
		}
	}

	public static void checkBlockMessage(string msg)
	{
		if (msg == null || lastProcessedItem == null)
		{
			return;
		}
		if (GameCanvas.timeNow - lastProcessedTime > 4000)
		{
			return;
		}
		string lower = msg.ToLower();
		bool isBlock = false;
		string[] keywords = new string[]
		{
			"không thể vứt", "khong the vut", "không thể bán", "khong the ban", "không thể bỏ", "khong the bo", "không vứt được", "khong vut duoc", "không bán được", "khong ban duoc",
			"vật phẩm khóa", "vat pham khoa", "trang bị khóa", "trang bi khoa", "đồ khóa", "cannot throw", "cannot sell", "cannot discard", "không thể thực hiện", "khong the thuc hien"
		};
		for (int i = 0; i < keywords.Length; i++)
		{
			if (lower.IndexOf(keywords[i]) != -1)
			{
				isBlock = true;
				break;
			}
		}
		if (isBlock)
		{
			Interface_Game.addInfoPlayerNormal("Chặn! Xóa DS: " + lastProcessedItem.name, mFont.tahoma_7_yellow);
			removeFromAutoSell(lastProcessedItem);
			pendingDrops.removeAllElements();
			lastProcessedItem = null;
		}
	}

	public static void toggleAutoSell(MainItem mi)
	{
		if (mi == null)
		{
			return;
		}
		if (isInAutoSell(mi))
		{
			removeFromAutoSell(mi);
			Interface_Game.addInfoPlayerNormal("Đã bỏ khỏi danh sách tự bán: " + mi.name, mFont.tahoma_7_yellow);
			return;
		}
		if (autoSellItemsCache.size() >= 126)
		{
			Interface_Game.addInfoPlayerNormal("Danh sách tự bán tối đa 126 ô!", mFont.tahoma_7_yellow);
			return;
		}
		short[] entry = new short[2] { (short)mi.typeObject, (short)mi.ID };
		autoSellList.addElement(entry);
		MainItem copy = new MainItem(mi.typeObject, mi.ID, mi.idIcon, mi.name, 0);
		copy.colorName = mi.colorName;
		copy.numPotion = (short)((mi.numPotion > 0) ? mi.numPotion : 1);
		autoSellItemsCache.addElement(copy);
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Đã thêm vào danh sách tự bán: " + mi.name, mFont.tahoma_7_yellow);
	}

	public static void clearAutoSellList()
	{
		autoSellList.removeAllElements();
		autoSellItemsCache.removeAllElements();
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Xóa toàn bộ danh sách tự bán", mFont.tahoma_7_yellow);
	}

	public static void removeAutoSellAt(int idx)
	{
		if (idx < 0 || idx >= autoSellList.size())
		{
			return;
		}
		short[] s = (short[])autoSellList.elementAt(idx);
		autoSellList.removeElementAt(idx);
		for (int j = 0; j < autoSellItemsCache.size(); j++)
		{
			MainItem cached = (MainItem)autoSellItemsCache.elementAt(j);
			if (cached != null && cached.typeObject == s[0] && cached.ID == s[1])
			{
				autoSellItemsCache.removeElementAt(j);
				break;
			}
		}
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Đã xóa mục tự bán", mFont.tahoma_7_yellow);
	}

	public static void cleanAutoSellCache()
	{
		mVector toRemove = new mVector();
		for (int i = 0; i < autoSellList.size(); ++i)
		{
			short[] s = (short[])autoSellList.elementAt(i);
			if (s == null)
			{
				toRemove.addElement(i);
			}
		}
		for (int i = toRemove.size() - 1; i >= 0; --i)
		{
			int idx = (int)toRemove.elementAt(i);
			autoSellList.removeElementAt(idx);
		}
	}

	private static bool isPending(MainItem mi)
	{
		if (mi == null)
		{
			return false;
		}
		for (int p = 0; p < pendingDrops.size(); ++p)
		{
			short[] s = (short[])pendingDrops.elementAt(p);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID)
			{
				return true;
			}
		}
		return false;
	}

	private static void addPending(MainItem mi)
	{
		if (mi != null && !isPending(mi))
		{
			short[] entry = new short[2] { (short)mi.typeObject, (short)mi.ID };
			pendingDrops.addElement(entry);
		}
	}

	private static void removePendingIfNotExistsInInventory()
	{
		if (Player.vecInventory == null)
		{
			return;
		}
		for (int i = pendingDrops.size() - 1; i >= 0; i--)
		{
			short[] s = (short[])pendingDrops.elementAt(i);
			bool found = false;
			for (int j = 0; j < Player.vecInventory.size(); j++)
			{
				MainItem mi = (MainItem)Player.vecInventory.elementAt(j);
				if (mi != null && mi.typeObject == s[0] && mi.ID == s[1])
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				pendingDrops.removeElementAt(i);
			}
		}
	}

	public static void autoProcessItems()
	{
		if (Player.vecInventory == null || GameCanvas.gameTick % 20 != 0)
		{
			return;
		}
		if (!Player.isAutoFilterItems)
		{
			return;
		}
		removePendingIfNotExistsInInventory();
		if (pendingDrops.size() > 0)
		{
			if (GameCanvas.timeNow - lastProcessedTime > 4000)
			{
				pendingDrops.removeAllElements();
				lastProcessedItem = null;
			}
			else
			{
				return;
			}
		}
		for (int i = Player.vecInventory.size() - 1; i >= 0; --i)
		{
			MainItem mi = (MainItem)Player.vecInventory.elementAt(i);
			if (mi != null && isInAutoSell(mi))
			{
				if (mi.numPotion <= 0)
				{
					continue;
				}
				lastProcessedItem = mi;
				lastProcessedTime = GameCanvas.timeNow;
				GlobalService.gI().Sell_Item(0, mi.ID, mi.typeObject, (short)mi.numPotion);
				addPending(mi);
				Interface_Game.addInfoPlayerNormal("Tự bán: " + mi.name, mFont.tahoma_7_yellow);
				break;
			}
		}
	}

	public static void saveAutoSellList()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(autoSellItemsCache.size());
			for (int i = 0; i < autoSellItemsCache.size(); i++)
			{
				MainItem mi = (MainItem)autoSellItemsCache.elementAt(i);
				dos.writeByte(mi.typeObject);
				dos.writeShort(mi.ID);
				dos.writeShort(mi.idIcon);
				dos.writeUTF((mi.name != null) ? mi.name : "");
				dos.writeByte(mi.colorName);
			}
			CRes.saveRMS("MOD_AUTO_SELL_LIST", baos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static void loadAutoSellList()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MOD_AUTO_SELL_LIST");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				int size = dis.readInt();
				autoSellList.removeAllElements();
				autoSellItemsCache.removeAllElements();
				for (int i = 0; i < size; i++)
				{
					sbyte typeObject = dis.readByte();
					short ID = dis.readShort();
					short idIcon = dis.readShort();
					string name = dis.readUTF();
					sbyte colorName = dis.readByte();
					MainItem mi = new MainItem(typeObject, ID, idIcon, name, 0);
					mi.colorName = colorName;
					mi.numPotion = 1;
					autoSellItemsCache.addElement(mi);
					short[] entry = new short[2] { (short)typeObject, (short)ID };
					autoSellList.addElement(entry);
				}
				dis.close();
			}
		}
		catch (Exception)
		{
		}
	}

	public static void checkPortalStepped(Player p)
	{
		if (GameCanvas.loadmap == null || LoadMap.vecPointChange == null)
		{
			return;
		}
		for (int i = 0; i < LoadMap.vecPointChange.size(); i++)
		{
			Point pt = (Point)LoadMap.vecPointChange.elementAt(i);
			if (pt != null)
			{
				int dist = MainObject.getDistance(p.x, p.y, pt.x, pt.y);
				if (dist < 50)
				{
					lastSteppedPortalName = pt.name;
					break;
				}
			}
		}
	}

	public static void recordTransition(int fromMapId, int toMapId, string portalName)
	{
		if (shouldReturnToLastMap)
		{
			return;
		}
		int existingIdx = -1;
		for (int i = 0; i < recordedMapIds.size(); i++)
		{
			if ((int)recordedMapIds.elementAt(i) == toMapId)
			{
				existingIdx = i;
				break;
			}
		}
		if (existingIdx != -1)
		{
			while (recordedMapIds.size() > existingIdx + 1)
			{
				recordedMapIds.removeElementAt(recordedMapIds.size() - 1);
				recordedPortalNames.removeElementAt(recordedPortalNames.size() - 1);
			}
		}
		else
		{
			if (recordedMapIds.size() == 0)
			{
				recordedMapIds.addElement(fromMapId);
			}
			recordedPortalNames.addElement((portalName != null) ? portalName : "");
			recordedMapIds.addElement(toMapId);
		}
	}

	public static void updateAvoidPK(Player p)
	{
		if (!isAvoidPK || p == null || p.isDie || GameCanvas.loadMapScr == null)
		{
			return;
		}
		if (GameCanvas.timeNow - lastAvoidChangeZoneTime < 5000)
		{
			return;
		}
		mVector vec = GameScreen.vecPlayers;
		if (vec == null)
		{
			return;
		}
		bool hasPKer = false;
		for (int i = 0; i < vec.size(); i++)
		{
			MainObject o = (MainObject)vec.elementAt(i);
			if (o != null && o.typeObject == 0 && o != p && !o.isRemove && !o.isDie)
			{
				if (o.typePK > 0 || o.colorName == 2 || o.colorName == 4 || o.colorName == 5 || p.setFightPk(o))
				{
					hasPKer = true;
					break;
				}
			}
		}
		if (hasPKer)
		{
			sbyte currentArea = GameCanvas.loadMapScr.area;
			sbyte nextArea = (sbyte)((currentArea + 1) % 10);
			try
			{
				GlobalService.gI().Select_Area((sbyte)0, nextArea);
				Interface_Game.addInfoPlayerNormal("Phát hiện PK! Đang né sang khu " + nextArea, mFont.tahoma_7_yellow);
				lastAvoidChangeZoneTime = GameCanvas.timeNow;
			}
			catch (Exception)
			{
			}
		}
	}

	public static void updateAvoidBoss(Player p)
	{
		if (!isAvoidBoss || p == null || p.isDie || GameCanvas.loadMapScr == null)
		{
			return;
		}
		if (GameCanvas.timeNow - lastAvoidBossChangeZoneTime < 5000)
		{
			return;
		}
		mVector vec = GameScreen.vecPlayers;
		if (vec == null)
		{
			return;
		}
		bool hasBoss = false;
		for (int i = 0; i < vec.size(); i++)
		{
			MainObject o = (MainObject)vec.elementAt(i);
			if (o != null && o.typeObject == 1 && !o.isRemove && !o.isDie)
			{
				if (o.typeBossMonster != 0)
				{
					hasBoss = true;
					break;
				}
			}
		}
		if (hasBoss)
		{
			sbyte currentArea = GameCanvas.loadMapScr.area;
			sbyte nextArea = (sbyte)((currentArea + 1) % 10);
			try
			{
				GlobalService.gI().Select_Area((sbyte)0, nextArea);
				Interface_Game.addInfoPlayerNormal("Phát hiện Boss! Đang né sang khu " + nextArea, mFont.tahoma_7_yellow);
				lastAvoidBossChangeZoneTime = GameCanvas.timeNow;
			}
			catch (Exception)
			{
			}
		}
	}

	public static void returnToLastMap()
	{
		if (lastMapId <= 0)
		{
			Interface_Game.addInfoPlayerNormal("Không có map cũ để quay lại.", mFont.tahoma_7_yellow);
			shouldReturnToLastMap = false;
			return;
		}
		try
		{
			int currentMap = GameCanvas.loadmap.idMap;
			if (currentMap == lastMapId)
			{
				try
				{
					AThMadaraFunc.restoreSavedZoneIfAny();
				}
				catch (Exception)
				{
				}
				if (lastX > 0 && lastY > 0)
				{
					try
					{
						GlobalService.gI().Obj_Move((short)lastX, (short)lastY);
						if (GameScreen.player != null)
						{
							GameScreen.player.x = lastX;
							GameScreen.player.y = lastY;
							GameScreen.player.xLast = lastX;
							GameScreen.player.yLast = lastY;
							Player.isSendMove = true;
						}
					}
					catch (Exception)
					{
					}
				}
				Interface_Game.addInfoPlayerNormal("Đã quay lại vị trí cũ.", mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
				lastMapId = -1;
				lastX = -1;
				lastY = -1;
				triedChangeMapOkOnce = false;
				return;
			}
			if (GameCanvas.timeNow < nextWalkToPortalTime)
			{
				return;
			}
			int curIdx = -1;
			for (int i = 0; i < recordedMapIds.size(); i++)
			{
				if ((int)recordedMapIds.elementAt(i) == currentMap)
				{
					curIdx = i;
					break;
				}
			}
			if (curIdx == -1 || curIdx >= recordedPortalNames.size())
			{
				Interface_Game.addInfoPlayerNormal("Mất dấu đường về map cũ. Vui lòng tự đi lại!", mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
				return;
			}
			string nextPortalName = (string)recordedPortalNames.elementAt(curIdx);
			Point targetPortal = null;
			if (LoadMap.vecPointChange != null)
			{
				for (int i = 0; i < LoadMap.vecPointChange.size(); i++)
				{
					Point pt = (Point)LoadMap.vecPointChange.elementAt(i);
					if (pt != null && pt.name != null && pt.name.Equals(nextPortalName))
					{
						targetPortal = pt;
						break;
					}
				}
				if (targetPortal == null && LoadMap.vecPointChange.size() > 0)
				{
					targetPortal = (Point)LoadMap.vecPointChange.elementAt(0);
				}
			}
			if (targetPortal != null)
			{
				Player p = GameScreen.player;
				if (p != null)
				{
					int tx = targetPortal.x;
					int ty = targetPortal.y;
					int dist = MainObject.getDistance(p.x, p.y, tx, ty);
					if (dist > 28)
					{
						AThMadaraFunc.sendTeleport(p, tx, ty);
						Interface_Game.addInfoPlayerNormal("Đang di chuyển tới cổng: " + nextPortalName, mFont.tahoma_7_yellow);
					}
					else
					{
						GlobalService.gI().Obj_Move((short)tx, (short)ty);
						p.posTransRoad = null;
					}
				}
				nextWalkToPortalTime = GameCanvas.timeNow + 1500;
			}
			else
			{
				Interface_Game.addInfoPlayerNormal("Không tìm thấy cổng: " + nextPortalName, mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
			}
		}
		catch (Exception)
		{
			shouldReturnToLastMap = false;
		}
	}
}
