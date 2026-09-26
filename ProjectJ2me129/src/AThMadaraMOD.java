import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class AThMadaraMOD {
	public Player myChar;
	public static final int MENU_AUTO_W = 11;
	public static final int MENU_AUTO_H = 14;
	public static final int MENU_AUTO_OFFSET_X = 3;
	public static final int MENU_AUTO_OFFSET_Y = -20;
	public static final int MENU_TOUCH_PAD = 4;
	private static AThMadaraMOD instance;
	public static mVector pendingDrops = new mVector();

	public static boolean shouldReturnToLastMap = false;
	public static int lastMapId = -1;
	public static int lastX = -1;
	public static int lastY = -1;
	public static long nextReturnTryAt = 0;
	public static boolean triedChangeMapOkOnce = false;

	public static int gameSpeed = 1;
	public static boolean isAvoidPK = false;
	public static boolean isAvoidBoss = false;
	public static boolean isAutoReMap = false;
	public static boolean isSlaughterActive = false;
	public static String slaughterTargetName = null;
	public static int slaughterTargetLevel = -1;
	public static mVector tempUniqueMonsters = null;

	public static int autoCombatMoveMode = 0; // 0: Dich chuyen, 1: Di chuyen (Di bo / Pathfind)
	public static boolean isTuDanhActive = false; // Ke thua de tuong thich

	public static String getCombatMoveModeName() {
		return autoCombatMoveMode == 0 ? "Dịch chuyển" : "Đi bộ";
	}

	public static String getCombatStatusText() {
		if (GameScreen.player == null || !GameScreen.player.isAutoFireNew108) {
			return "[Tắt]";
		}
		String mode = (autoCombatMoveMode == 0 ? "Dịch chuyển" : "Đi bộ");
		if (slaughterTargetName != null) {
			return "[" + mode + " - " + slaughterTargetName + "]";
		}
		return "[" + mode + " - Tất cả]";
	}

	public static void saveCombatSettings() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(autoCombatMoveMode);
			CRes.saveRMS("MOD_COMBAT_OPT", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadCombatSettings() {
		try {
			byte[] data = CRes.loadRMS("MOD_COMBAT_OPT");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				autoCombatMoveMode = dis.readInt();
				if (autoCombatMoveMode < 0 || autoCombatMoveMode > 1) autoCombatMoveMode = 0;
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public static boolean isShowMobID = false;
	public static boolean isShowCharID = false;
	public static boolean isShowNames = true;
	public static boolean isShowShadows = true;
	public static boolean isOptimizeGame = false;
	public static boolean isSlaughterPausedByMove = false;
	public static boolean isSlaughterPausedByQuest = false;
	public static boolean wasSlaughterActive = false;
	public static boolean wasTuDanhActive = false;
	public static int lastPlayerHp = -1;

	public static int slaughterSelectType = 0;
	public static int slaughterPriority = 1;
	public static int slaughterTargetTypeObject = -1;

	// Do hoa & Toi uu mo rong
	public static boolean isShowOtherPlayers = true;
	public static boolean isShowWeather = true;
	public static boolean isShowPet = true;
	public static boolean isShowMasteryEffect = true;
	public static boolean isShowTopTitle = true;
	public static boolean isShowPlayerNames = true;
	public static boolean isShowMonsterNames = true;
	public static boolean isHutItem = false;
	public static boolean isShowSkillPlayer = true;
	public static boolean isShowMapEffect = true;
	public static boolean isReduceParticle = false;
	public static boolean isAutoRedLine = false;
	public static boolean isAutoReconnect2 = false; // Auto reconnect thực sự (phân biệt với stub cũ)

	public static boolean isAutoReconnect = false;
	public static String pendingDisconnectMsg = "";
	public static boolean pendingDisconnect = false;
	public static void triggerAutoReconnect() {
		if (!isAutoReconnect2) return;
		try {
			if (TcpClient.getInstant() != null) {
				TcpClient.getInstant().reconnect();
				Interface_Game.addInfoPlayerNormal("Dang ket noi lai...", mFont.tahoma_7_yellow);
			} else {
				Interface_Game.addInfoPlayerNormal("Khong tim thay ket noi!", mFont.tahoma_7_yellow);
			}
		} catch (Exception e) {
			Interface_Game.addInfoPlayerNormal("Loi reconnect: " + e.getMessage(), mFont.tahoma_7_yellow);
		}
	}
	public static void cancelAutoReconnect() {
		isAutoReconnect = false;
	}

	public static int autoReviveMode = 0;
	public static long reviveFallbackTime = 0;
	public static short reviveFallbackDialogId = -1;
	public static int reviveFallbackTownIndex = -1;

	public static mVector recordedMapIds = new mVector();
	public static mVector recordedPortalNames = new mVector();
	public static int lastRecordedMapId = -1;
	public static String lastSteppedPortalName = null;

	private static long lastAvoidChangeZoneTime = 0;
	private static long lastAvoidBossChangeZoneTime = 0;
	private static long nextWalkToPortalTime = 0;

	public static String getAutoReviveModeName() {
		switch (autoReviveMode) {
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
	public static int AUTOSELL_FILTER = 0;

	public static AThMadaraMOD getInstance() {
		if (instance == null) {
			instance = new AThMadaraMOD();
			loadCombatSettings();
			loadCustomSkillEffSettings();
			loadGraphicsSettings();
			loadSpeedSetting();
			loadAutoSellList();
			loadAutoRedLineSettings();
		}
		return instance;
	}

	public static void saveGraphicsSettings() {
		try {
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
			dos.writeBoolean(isShowMapEffect);
			dos.writeBoolean(isReduceParticle);
			CRes.saveRMS("MOD_GRAPHICS_OPT", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadGraphicsSettings() {
		try {
			byte[] data = CRes.loadRMS("MOD_GRAPHICS_OPT");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				boolean skillShow = dis.readBoolean();
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
				try { isShowMapEffect = dis.readBoolean(); } catch (Exception ignored) {}
				try { isReduceParticle = dis.readBoolean(); } catch (Exception ignored) {}
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public static void setPresetMaxOptimization() {
		isShowOtherPlayers = false;
		isShowWeather = false;
		isShowPet = false;
		GameScreen.isShowSkillPlayer = false;
		isShowShadows = false;
		isShowTopTitle = false;
		isShowMasteryEffect = false;
		isOptimizeGame = true;
		isShowMapEffect = false;
		isReduceParticle = true;
		saveGraphicsSettings();
		Interface_Game.addInfoPlayerNormal("Da bat Toi Uu Max (Tat het do hoa nang)!", mFont.tahoma_7_yellow);
	}

	public static void setPresetResetGraphics() {
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
		isShowMapEffect = true;
		isReduceParticle = false;
		saveGraphicsSettings();
		Interface_Game.addInfoPlayerNormal("Da khoi phuc do hoa mac dinh!", mFont.tahoma_7_yellow);
	}

	public static void openGraphicsMenu() {
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
		menu.addElement(new iCommand("Hieu ung Map nen [" + (isShowMapEffect ? "Bat" : "Tat") + "]", 373, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("Giam Particle / Hieu ung [" + (isReduceParticle ? "Bat" : "Tat") + "]", 374, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("* Toi Uu Max (Tat Het Do Hoa)", 360, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("# Preset Can Bang (Khuyen dung)", 375, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("~ Khoi Phuc Mac Dinh Do Hoa", 361, 0, GameCanvas.gameScr));
		menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
		GameCanvas.menu.startAt(menu, 2, "Do hoa & Toi uu");
	}

	public static void setPresetBalanced() {
		isShowOtherPlayers = true;
		isShowWeather = false;
		isShowPet = false;
		GameScreen.isShowSkillPlayer = true;
		isShowShadows = false;
		isShowPlayerNames = true;
		isShowMonsterNames = true;
		isShowTopTitle = true;
		isShowMasteryEffect = false;
		isShowMobID = false;
		isShowCharID = false;
		isHutItem = false;
		isShowMapEffect = false;
		isReduceParticle = true;
		isOptimizeGame = false;
		saveGraphicsSettings();
		Interface_Game.addInfoPlayerNormal("Da bat Preset Can Bang (Toi uu + giu can ban)!", mFont.tahoma_7_yellow);
	}

	public static boolean isModSkill = false;

	public static void openModSkillMenu() {
		try {
			mVector menu = new mVector();
			menu.addElement(new iCommand("Mod Skill [" + (isModSkill ? "Bat" : "Tat") + "]", 310, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Cai dat Slot & Hieu ung", 318, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Khoi phuc mac dinh tat ca", 317, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Mod Skill (Doi Hieu Ung)");
		} catch (Exception ignored) {}
	}

	public static void openCombatUtilitiesMenu() {
		try {
			mVector menu = new mVector();
			menu.addElement(new iCommand("Gom quai [" + ((GameScreen.player != null && GameScreen.player.isGomQuai112) ? "Bat" : "Tat") + "]", 112, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Dong bang quai [" + ((GameScreen.player != null && GameScreen.player.isDongBangQuai111) ? "Bat" : "Tat") + "]", 111, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Ne PK [" + (isAvoidPK ? "Bat" : "Tat") + "]", 129, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Ne Boss [" + (isAvoidBoss ? "Bat" : "Tat") + "]", 130, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu ve map cu [" + (isAutoReMap ? "Bat" : "Tat") + "]", 128, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Tien ich chien dau");
		} catch (Exception ignored) {}
	}

	public static void openActivitiesMenu() {
		try {
			mVector menu = new mVector();
			menu.addElement(new iCommand("Auto NV Lap [" + (GameScreen.isOnRepeatQuest ? "Bat" : "Tat") + "]", 99, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto San Boss [" + (GameScreen.isOnSuperBoss ? "Bat" : "Tat") + "]", 101, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Chien Truong [" + (GameScreen.isOnAutoPB ? "Bat" : "Tat") + "]", 107, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Hoat dong & Nhiem vu");
		} catch (Exception ignored) {}
	}

	public static void openSystemMenu() {
		try {
			mVector menu = new mVector();
			menu.addElement(new iCommand("Toc do game [" + gameSpeed + "/10]", 114, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("FPS [" + MotherCanvas.targetFPS + " fps]", 120, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Che do phim [" + (Interface_Game.typeTouch == 0 ? "Keypad/D-Pad" : "Touch/Cam ung") + "]", 136, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Reconnect [" + (isAutoReconnect2 ? "Bat" : "Tat") + "]", 376, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "He thong & Toc do");
		} catch (Exception ignored) {}
	}

	public void openMenuAuto() {
		try {
			mVector menu = new mVector();
			menu.addElement(new iCommand("Tu Danh / Tan Sat " + getCombatStatusText(), 108, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Skill", 113, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Mod Skill [" + (isModSkill ? "Bat" : "Tat") + "]", 134, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tien ich chien dau", 341, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu hoi sinh [" + getAutoReviveModeName() + "]", 118, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tu dong ban do [" + (Player.isAutoFilterItems ? "Bat" : "Tat") + "]", 119, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Auto Thi Dau Bien [" + (isAutoRedLine ? "Bat" : "Tat") + "]", 372, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Hoat dong & NV", 342, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("He thong & Toc do", 343, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Do hoa & Toi uu", 351, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Menu Auto");
		} catch (Exception ignored) {}
	}

	public static void openAutoCombatMenu() {
		try {
			mVector menu = new mVector();
			boolean isCombatActive = GameScreen.player != null && GameScreen.player.isAutoFireNew108;
			String allPrefix = (isCombatActive && slaughterTargetName == null) ? "* " : "  ";
			menu.addElement(new iCommand(allPrefix + "Tan sat: Tat ca", 300, 0, GameCanvas.gameScr));
			if (GameScreen.vecPlayers != null) {
				mVector uniqueMonsters = new mVector();
				for (int i = 0; i < GameScreen.vecPlayers.size(); i++) {
					MainObject obj = (MainObject) GameScreen.vecPlayers.elementAt(i);
					if (obj == null || obj.typeObject != 1 || obj.isRemove || obj.Hp <= 0) {
						continue;
					}
					boolean exists = false;
					for (int j = 0; j < uniqueMonsters.size(); j++) {
						MainObject m = (MainObject) uniqueMonsters.elementAt(j);
						if (m != null && m.name != null && m.name.equals(obj.name) && m.Lv == obj.Lv) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						uniqueMonsters.addElement(obj);
					}
				}
				tempUniqueMonsters = uniqueMonsters;
				for (int i = 0; i < uniqueMonsters.size(); i++) {
					MainObject m = (MainObject) uniqueMonsters.elementAt(i);
					boolean isCur = isCombatActive && slaughterTargetName != null && slaughterTargetName.equals(m.name) && slaughterTargetLevel == m.Lv;
					String prefix = isCur ? "* " : "  ";
					menu.addElement(new iCommand(prefix + m.name + " (Lv." + m.Lv + ")", 301, i, GameCanvas.gameScr));
				}
			}
			menu.addElement(new iCommand("Kieu di chuyen: [" + getCombatMoveModeName() + "]", 303, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("Tat Tu Danh / Tan Sat", 302, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lai", 340, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Tu Danh / Tan Sat");
		} catch (Exception ignored) {}
	}

	public static void openSlaughterMenu() {
		openAutoCombatMenu();
	}

	public static short[] customSlotEffects = new short[]{-1, -1, -1, -1, -1};
	public static mVector[] customMultiSlotEffects = new mVector[]{
		new mVector(), new mVector(), new mVector(), new mVector(), new mVector()
	};
	public static int selectedSlotToMod = 0;
	public static InputDialog inputDialogCustomEff;
	public static int currentSelectingSubMenuType = 1; // 1: Devil Fruit, 2: 5 Classes

	public static int getSkillSlot(Skill_Info sk) {
		if (sk == null) return -1;
		if (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4)) return 4;
		if (sk.typeSkill == 1 && Player.vecListSkill != null) {
			mVector uniqueActive = new mVector();
			for (int i = 0; i < Player.vecListSkill.size(); i++) {
				Skill_Info s = (Skill_Info) Player.vecListSkill.elementAt(i);
				if (s != null && s.typeSkill == 1) {
					boolean exists = false;
					for (int j = 0; j < uniqueActive.size(); j++) {
						Skill_Info ex = (Skill_Info) uniqueActive.elementAt(j);
						if (ex != null && ex.ID == s.ID) { exists = true; break; }
					}
					if (!exists) uniqueActive.addElement(s);
				}
			}
			for (int i = 0; i < uniqueActive.size(); i++) {
				Skill_Info s = (Skill_Info) uniqueActive.elementAt(i);
				if (s != null && s.ID == sk.ID) return i;
			}
		}
		return -1;
	}

	public static boolean hasCustomEffect(int slot, short effId) {
		if (slot >= 0 && slot < 5 && customMultiSlotEffects[slot] != null) {
			for (int i = 0; i < customMultiSlotEffects[slot].size(); i++) {
				Short s = (Short) customMultiSlotEffects[slot].elementAt(i);
				if (s != null && s.shortValue() == effId) return true;
			}
		}
		return false;
	}

	public static void toggleCustomSkillEff(int slot, short effId) {
		if (slot >= 0 && slot < 5 && effId > 0) {
			if (customMultiSlotEffects[slot] == null) {
				customMultiSlotEffects[slot] = new mVector();
			}
			boolean found = false;
			for (int i = 0; i < customMultiSlotEffects[slot].size(); i++) {
				Short s = (Short) customMultiSlotEffects[slot].elementAt(i);
				if (s != null && s.shortValue() == effId) {
					customMultiSlotEffects[slot].removeElementAt(i);
					found = true;
					Interface_Game.addInfoPlayerNormal("Đã BỎ Eff " + effId + " khỏi Slot " + slot, mFont.tahoma_7_yellow);
					break;
				}
			}
			if (!found) {
				customMultiSlotEffects[slot].addElement(new Short(effId));
				Interface_Game.addInfoPlayerNormal("Đã THÊM [*] Eff " + effId + " vào Slot " + slot, mFont.tahoma_7_yellow);
			}
			short orig = getDefaultEffForSlot(slot);
			customSlotEffects[slot] = (customMultiSlotEffects[slot].size() > 0) ? ((Short)customMultiSlotEffects[slot].elementAt(0)).shortValue() : (orig > 0 ? orig : (short)0);
			saveCustomSkillEffSettings();

			if (currentSelectingSubMenuType == 1) {
				openDevilFruitSkillsMenu(slot);
			} else if (currentSelectingSubMenuType == 2) {
				openStandardSkillTemplatesMenu(slot);
			}
		}
	}

	public static short getDefaultEffForSlot(int slot) {
		if (Player.vecListSkill != null && slot >= 0 && slot < 5) {
			if (slot == 4) {
				for (int i = 0; i < Player.vecListSkill.size(); i++) {
					Skill_Info sk = (Skill_Info) Player.vecListSkill.elementAt(i);
					if (sk != null && (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4))) {
						return sk.typeEffSkill;
					}
				}
			} else {
				mVector uniqueActive = new mVector();
				for (int i = 0; i < Player.vecListSkill.size(); i++) {
					Skill_Info s = (Skill_Info) Player.vecListSkill.elementAt(i);
					if (s != null && s.typeSkill == 1) {
						boolean exists = false;
						for (int j = 0; j < uniqueActive.size(); j++) {
							Skill_Info ex = (Skill_Info) uniqueActive.elementAt(j);
							if (ex != null && ex.ID == s.ID) { exists = true; break; }
						}
						if (!exists) uniqueActive.addElement(s);
					}
				}
				if (slot < uniqueActive.size()) {
					Skill_Info sk = (Skill_Info) uniqueActive.elementAt(slot);
					if (sk != null) return sk.typeEffSkill;
				}
			}
		}
		return -1;
	}

	public static mVector getCustomSkillEffList(Skill_Info sk, short defaultEff) {
		mVector list = new mVector();
		short validDefault = (defaultEff > 0) ? defaultEff : ((sk != null && sk.typeEffSkill > 0) ? sk.typeEffSkill : (short)0);
		if (!isModSkill || sk == null) {
			if (validDefault > 0) list.addElement(new Short(validDefault));
			return list;
		}
		int slot = getSkillSlot(sk);
		if (slot >= 0 && slot < 5) {
			if (customMultiSlotEffects[slot] == null) {
				customMultiSlotEffects[slot] = new mVector();
			}
			mVector validList = new mVector();
			for (int i = 0; i < customMultiSlotEffects[slot].size(); i++) {
				Short val = (Short) customMultiSlotEffects[slot].elementAt(i);
				if (val != null && val.shortValue() > 0) {
					validList.addElement(val);
				}
			}
			if (validList.size() > 0) {
				return validList;
			}
		}
		if (validDefault > 0) list.addElement(new Short(validDefault));
		return list;
	}

	public static short getCustomSkillEff(Skill_Info sk, short defaultEff) {
		if (!isModSkill) {
			return (defaultEff > 0) ? defaultEff : ((sk != null && sk.typeEffSkill > 0) ? sk.typeEffSkill : defaultEff);
		}
		mVector list = getCustomSkillEffList(sk, defaultEff);
		return (list.size() > 0) ? ((Short)list.elementAt(0)).shortValue() : defaultEff;
	}

	public static void saveCustomSkillEffSettings() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeBoolean(isModSkill);
			for (int i = 0; i < 5; i++) {
				int count = (customMultiSlotEffects[i] != null) ? customMultiSlotEffects[i].size() : 0;
				dos.writeInt(count);
				for (int j = 0; j < count; j++) {
					Short val = (Short) customMultiSlotEffects[i].elementAt(j);
					dos.writeShort(val != null ? val.shortValue() : (short)0);
				}
			}
			CRes.saveRMS("MOD_SKILL_SLOT_EFF_MULTI", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadCustomSkillEffSettings() {
		try {
			byte[] data = CRes.loadRMS("MOD_SKILL_SLOT_EFF_MULTI");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				try {
					isModSkill = dis.readBoolean();
				} catch (Exception e) {
					isModSkill = false;
				}
				for (int i = 0; i < 5; i++) {
					int count = dis.readInt();
					if (customMultiSlotEffects[i] == null) {
						customMultiSlotEffects[i] = new mVector();
					}
					customMultiSlotEffects[i].removeAllElements();
					for (int j = 0; j < count; j++) {
						short val = dis.readShort();
						if (val > 0) {
							customMultiSlotEffects[i].addElement(new Short(val));
						}
					}
					short orig = getDefaultEffForSlot(i);
					if (customMultiSlotEffects[i].size() == 0 && orig > 0) {
						customMultiSlotEffects[i].addElement(new Short(orig));
					}
					customSlotEffects[i] = (customMultiSlotEffects[i].size() > 0) ? ((Short)customMultiSlotEffects[i].elementAt(0)).shortValue() : (orig > 0 ? orig : (short)0);
				}
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public static String getEffectName(short effId) {
		if (effId <= 0) return "Mặc định (Gốc)";
		switch (effId) {
			case 164: return "Trái Cao Su - Cú Đấm Cao Su";
			case 227: return "Trái Cao Su - Súng Máy Cao Su";
			case 2: return "Trái Lửa (Ace) - Hỏa Súng";
			case 228: return "Trái Lửa (Ace) - Cột Hỏa Thiên (L1)";
			case 3: return "Trái Lửa (Ace) - Hỏa Cầu Nổ";
			case 229: return "Trái Lửa (Ace) - Đại Hỏa Cầu (L1)";
			case 259: return "Trái Lửa (Ace) - Hỏa Súng Super L1";
			case 260: return "Trái Lửa (Ace) - Hỏa Súng Super L2";
			case 261: return "Trái Lửa (Ace) - Hỏa Súng Super L3";
			case 262: return "Trái Lửa (Ace) - Hỏa Cầu Super L1";
			case 263: return "Trái Lửa (Ace) - Hỏa Cầu Super L2";
			case 264: return "Trái Lửa (Ace) - Hỏa Cầu Super L3";
			case 4: return "Trái Băng (Aokiji) - Băng Đao";
			case 230: return "Trái Băng (Aokiji) - Chim Băng (L1)";
			case 5: return "Trái Băng (Aokiji) - Băng Trụ";
			case 231: return "Trái Băng (Aokiji) - Đại Băng Trụ (L1)";
			case 6: return "Trái Khói (Smoker) - Quyền Khói L1";
			case 232: return "Trái Khói (Smoker) - Quyền Khói L2";
			case 10: return "Trái Khói (Smoker) - Long Phong Khói L1";
			case 234: return "Trái Khói (Smoker) - Long Phong Khói L2";
			case 25: return "Trái Cát (Crocodile) - Lưỡi Cát L1";
			case 235: return "Trái Cát (Crocodile) - Lưỡi Cát L2";
			case 26: return "Trái Cát (Crocodile) - Bão Cát Sa Mạc L1";
			case 236: return "Trái Cát (Crocodile) - Bão Cát Sa Mạc L2";
			case 169: return "Trái Sấm (Enel) - Sét Đánh L1";
			case 237: return "Trái Sấm (Enel) - Sét Đánh L2";
			case 170: return "Trái Sấm (Enel) - Cột Sấm L1";
			case 238: return "Trái Sấm (Enel) - Cột Sấm L2";
			case 1998: return "Trái Sấm (Enel) - Bão Sét Giội Xuống 1";
			case 1999: return "Trái Sấm (Enel) - Bão Sét Giội Xuống 2";
			case 171: return "Trái Nham Thạch (Akainu) - Nham Quyền L1";
			case 239: return "Trái Nham Thạch (Akainu) - Nham Quyền L2";
			case 172: return "Trái Nham Thạch (Akainu) - Cột Nham Thạch L1";
			case 240: return "Trái Nham Thạch (Akainu) - Cột Nham Thạch L2";
			case 210: return "Trái Chấn Động (Whitebeard) - Chấn Quyền L1";
			case 243: return "Trái Chấn Động (Whitebeard) - Chấn Quyền L2";
			case 211: return "Trái Chấn Động (Whitebeard) - Sóng Âm L1";
			case 244: return "Trái Chấn Động (Whitebeard) - Sóng Âm L2";
			case 179: return "Trái Chim Ứng (Pell) - Vũ Điệu Chim Ứng L1";
			case 241: return "Trái Chim Ứng (Pell) - Vũ Điệu Chim Ứng L2";
			case 209: return "Trái Lucci - Lục Thức Báo L1";
			case 242: return "Trái Lucci - Lục Thức Báo L2";
			case 233: return "Trái Nổ (Mr.5) - Vụ Nổ Thường";
			case 2000: return "Trái Nổ - Hỏa Nổ Trái Ác Quỷ";
			case 245: return "Trái Dao - Trảm Dao L1";
			case 251: return "Trái Dao - Trảm Dao L2";
			case 249: return "Trái Dao 2 - Xoay Phao L1";
			case 252: return "Trái Dao 2 - Xoay Phao L2";
			case 246: return "Trái Sáp (Nến) - Khiên Nến L1";
			case 253: return "Trái Sáp (Nến) - Khiên Nến L2";
			case 247: return "Trái Sáp 2 - Trụ Nến L1";
			case 254: return "Trái Sáp 2 - Trụ Nến L2";
			case 248: return "Trái Kilo - Quả Cân 10000kg L1";
			case 255: return "Trái Kilo - Quả Cân 10000kg L2";
			case 266: return "Lục Thức - Rankyaku (Trảm Phong)";
			case 267: return "Lục Thức - Shigan (Chỉ Súng)";
			case 268: return "Lục Thức - Door (Trái Cửa L1)";
			case 269: return "Lục Thức - Door (Trái Cửa L2)";
			case 270: return "Lục Thức - Kumadori Tóc Rắn";
			case 274: return "Trái Xà Phòng - Bong Bóng L1";
			case 275: return "Trái Xà Phòng - Bong Bóng L2";
			case 276: return "Trái Sói Zoan - Cắn Xé L1";
			case 277: return "Trái Sói Zoan - Cắn Xé L2";
			case 278: return "Trái Hươu Zoan - Húc Hươu L1";
			case 279: return "Trái Hươu Zoan - Húc Hươu L2";
			case 280: return "Goal - Sút Bóng Thần Thoại";
			case 400: return "Trái Ace - Skill 1 (Cấp <5)";
			case 401: return "Trái Ace - Skill 2 (Cấp <5)";
			case 402: return "Trái Ace - Skill 1 (Cấp =5 Super)";
			case 403: return "Trái Ace - Skill 2 (Cấp =5 Super)";
			case 404: return "Trái Ánh Sáng Kizaru - Skill 1 (Cấp <5)";
			case 405: return "Trái Ánh Sáng Kizaru - Skill 2 (Cấp <5)";
			case 406: return "Trái Ánh Sáng Kizaru - Skill 1 (Cấp =5 Super)";
			case 407: return "Trái Ánh Sáng Kizaru - Skill 2 (Cấp =5 Super)";
			case 408: return "Trái Tình Yêu Hancock - Skill 1 (Cấp <5)";
			case 409: return "Trái Tình Yêu Hancock - Skill 2 (Cấp <5)";
			case 410: return "Trái Tình Yêu Hancock - Skill 1 (Cấp =5 Super)";
			case 411: return "Trái Tình Yêu Hancock - Skill 2 (Cấp =5 Super)";
			case 3100: return "Trái Nika - Nắm đấm của Thần (Cấp <5)";
			case 3101: return "Trái Nika - Nắm đấm của Thần (Cấp =5 Super)";
			case 3102: return "Trái Nika - Số 4: Snakeman";
			case 3103: return "Trái Nika - Hơi thở của Thần (Buff)";
			case 38: return "Kiếm sĩ - Nhất kiếm (Cấp 1)";
			case 15: return "Kiếm sĩ - Nhất kiếm (Cấp 10)";
			case 86: return "Kiếm sĩ - Nhất kiếm (Cấp 15)";
			case 183: return "Kiếm sĩ - Nhất kiếm (Cấp 20)";
			case 215: return "Kiếm sĩ - Nhất kiếm (Cấp 25)";
			case 281: return "Kiếm sĩ - Nhất kiếm (Cấp 29)";
			case 481: return "Kiếm sĩ - Nhất kiếm (Cấp 30 Max)";
			case 41: return "Kiếm sĩ - Skill 2 (Cấp 1)";
			case 216: return "Kiếm sĩ - Skill 2 (Cấp 20)";
			case 482: return "Kiếm sĩ - Skill 2 (Cấp 30 Max)";
			case 121: return "Kiếm sĩ - Skill 3 (Cấp 1)";
			case 217: return "Kiếm sĩ - Skill 3 (Cấp 20)";
			case 483: return "Kiếm sĩ - Skill 3 (Cấp 30 Max)";
			case 42: return "Kiếm sĩ - Skill Trên Biển";
			case 21: return "Võ sĩ - Quả đấm tốc độ (Cấp 1)";
			case 33: return "Võ sĩ - Quả đấm tốc độ (Cấp 5)";
			case 83: return "Võ sĩ - Quả đấm tốc độ (Cấp 10)";
			case 180: return "Võ sĩ - Quả đấm tốc độ (Cấp 15)";
			case 212: return "Võ sĩ - Quả đấm tốc độ (Cấp 20)";
			case 271: return "Võ sĩ - Quả đấm tốc độ (Cấp 25)";
			case 471: return "Võ sĩ - Quả đấm tốc độ (Cấp 30 Max)";
			case 34: return "Võ sĩ - Skill 2 (Cấp 1)";
			case 472: return "Võ sĩ - Skill 2 (Cấp 30 Max)";
			case 1: return "Võ sĩ - Skill 3 (Cấp 1)";
			case 473: return "Võ sĩ - Skill 3 (Cấp 30 Max)";
			case 133: return "Võ sĩ - Skill Trên Biển";
			case 14: return "Đầu bếp - Hắc cước (Cấp 1)";
			case 44: return "Đầu bếp - Hắc cước (Cấp 10)";
			case 124: return "Đầu bếp - Hắc cước (Cấp 15)";
			case 186: return "Đầu bếp - Hắc cước (Cấp 20)";
			case 218: return "Đầu bếp - Hắc cước (Cấp 25)";
			case 491: return "Đầu bếp - Hắc cước (Cấp 30 Max)";
			case 47: return "Đầu bếp - Skill 2 (Cấp 1)";
			case 492: return "Đầu bếp - Skill 2 (Cấp 30 Max)";
			case 49: return "Đầu bếp - Skill 3 (Cấp 1)";
			case 493: return "Đầu bếp - Skill 3 (Cấp 30 Max)";
			case 136: return "Đầu bếp - Skill Trên Biển";
			case 16: return "Hoa tiêu - Double Shot (Cấp 1)";
			case 511: return "Hoa tiêu - Double Shot (Cấp 30 Max)";
			case 9: return "Hoa tiêu - Skill 2 (Cấp 1)";
			case 512: return "Hoa tiêu - Skill 2 (Cấp 30 Max)";
			case 31: return "Hoa tiêu - Skill 3 (Cấp 1)";
			case 513: return "Hoa tiêu - Skill 3 (Cấp 30 Max)";
			case 11: return "Hoa tiêu - Skill Trên Biển";
			case 57: return "Xạ thủ - Gậy chong chóng (Cấp 1)";
			case 501: return "Xạ thủ - Gậy chong chóng (Cấp 30 Max)";
			case 64: return "Xạ thủ - Skill 2 (Cấp 1)";
			case 502: return "Xạ thủ - Skill 2 (Cấp 30 Max)";
			case 67: return "Xạ thủ - Skill 3 (Cấp 1)";
			case 503: return "Xạ thủ - Skill 3 (Cấp 30 Max)";
			case 7: return "Xạ thủ - Skill Trên Biển";
			default: return "Eff ID " + effId;
		}
	}

	public static void openModSkillSelectSlotMenu() {
		try {
			mVector menu = new mVector();
			int activeIdx = 0;

			if (Player.vecListSkill != null && Player.vecListSkill.size() > 0) {
				mVector uniqueActiveSkills = new mVector();
				for (int i = 0; i < Player.vecListSkill.size(); i++) {
					Skill_Info sk = (Skill_Info) Player.vecListSkill.elementAt(i);
					if (sk == null) continue;
					if (sk.typeSkill != 1 && sk.typeSkill != 4 && !(sk.ID == 3 && sk.typeSkill == 4)) continue;

					boolean exists = false;
					for (int j = 0; j < uniqueActiveSkills.size(); j++) {
						Skill_Info existSk = (Skill_Info) uniqueActiveSkills.elementAt(j);
						if (existSk != null && existSk.ID == sk.ID) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						uniqueActiveSkills.addElement(sk);
					}
				}

				for (int i = 0; i < uniqueActiveSkills.size(); i++) {
					Skill_Info sk = (Skill_Info) uniqueActiveSkills.elementAt(i);
					if (sk == null) continue;

					int slot = -1;
					String slotPrefix = "";
					if (sk.typeSkill == 4 || (sk.ID == 3 && sk.typeSkill == 4)) {
						slot = 4;
						slotPrefix = "Skill Biển";
					} else if (sk.typeSkill == 1) {
						slot = activeIdx;
						slotPrefix = "Slot " + (activeIdx + 1);
						activeIdx++;
					}

					if (slot >= 0 && slot < 5) {
						int effCount = (customMultiSlotEffects[slot] != null) ? customMultiSlotEffects[slot].size() : 0;
						String effStatus = (effCount > 1) ? (" [" + effCount + " Eff]") : (effCount == 1 ? (" [Eff: " + ((Short)customMultiSlotEffects[slot].elementAt(0)).shortValue() + "]") : (" [Gốc: " + sk.typeEffSkill + "]"));
						String title = slotPrefix + ": " + sk.name + effStatus;
						menu.addElement(new iCommand(title, 311, slot, GameCanvas.gameScr));
					}
				}
			}

			if (menu.size() == 0) {
				for (int i = 0; i < 5; i++) {
					String slotName = (i == 4) ? "Skill Biển" : ("Slot " + (i + 1));
					int effCount = (customMultiSlotEffects[i] != null) ? customMultiSlotEffects[i].size() : 0;
					String effStatus = (effCount > 0) ? (" [" + effCount + " Eff]") : " [Mặc định]";
					menu.addElement(new iCommand(slotName + effStatus, 311, i, GameCanvas.gameScr));
				}
			}

			menu.addElement(new iCommand("Khôi Phục TẤT CẢ Về Mặc Định", 317, 0, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lại", 134, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "MOD HIỆU ỨNG SKILL");
		} catch (Exception ignored) {}
	}

	public static void openModSkillChooseEffMenu(int slot) {
		try {
			selectedSlotToMod = slot;
			mVector menu = new mVector();
			String slotName = (slot == 4) ? "SKILL BIỂN" : ("SLOT " + (slot + 1));
			int totalEffs = (customMultiSlotEffects[slot] != null) ? customMultiSlotEffects[slot].size() : 0;

			menu.addElement(new iCommand("1. Danh Sách Trái Ác Quỷ (" + totalEffs + " đã chọn)", 313, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("2. Danh Sách Skill 5 Phái (" + totalEffs + " đã chọn)", 314, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("3. Nhập Mã Eff ID Tùy Chỉnh", 315, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("4. Khôi Phục Slot Này Về Mặc Định", 316, slot, GameCanvas.gameScr));
			menu.addElement(new iCommand("< Quay lại", 134, 0, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "CÀI ĐẶT " + slotName);
		} catch (Exception ignored) {}
	}

	public static void openDevilFruitSkillsMenu(int slot) {
		try {
			selectedSlotToMod = slot;
			currentSelectingSubMenuType = 1;
			mVector menu = new mVector();

			short defaultEff = getDefaultEffForSlot(slot);
			if (defaultEff > 0) {
				boolean isAdded = hasCustomEffect(slot, defaultEff);
				String prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(defaultEff) + " [GỐC CỦA SKILL]", 312, defaultEff, GameCanvas.gameScr));
			}

			short[] devilEffs = new short[]{
				164, 227, 2, 228, 3, 229, 259, 260, 261, 262, 263, 264,
				4, 230, 5, 231, 6, 232, 10, 234, 25, 235, 26, 236,
				169, 237, 170, 238, 1998, 1999, 171, 239, 172, 240,
				210, 243, 211, 244, 179, 241, 209, 242, 233, 2000,
				245, 251, 249, 252, 246, 253, 247, 254, 248, 255,
				266, 267, 268, 269, 270, 274, 275, 276, 277, 278, 279, 280,
				400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
				3100, 3101, 3102, 3103
			};

			for (int i = 0; i < devilEffs.length; i++) {
				short effId = devilEffs[i];
				if (effId == defaultEff) continue;
				boolean isAdded = hasCustomEffect(slot, effId);
				String prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(effId), 312, effId, GameCanvas.gameScr));
			}

			menu.addElement(new iCommand("< Quay lại", 311, slot, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Trái Ác Quỷ (Ấn chọn để THÊM/XÓA)");
		} catch (Exception ignored) {}
	}

	public static void openStandardSkillTemplatesMenu(int slot) {
		try {
			selectedSlotToMod = slot;
			currentSelectingSubMenuType = 2;
			mVector menu = new mVector();

			short defaultEff = getDefaultEffForSlot(slot);
			if (defaultEff > 0) {
				boolean isAdded = hasCustomEffect(slot, defaultEff);
				String prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(defaultEff) + " [GỐC CỦA SKILL]", 312, defaultEff, GameCanvas.gameScr));
			}

			short[] stdEffs = new short[]{
				38, 15, 86, 183, 215, 481, 41, 482, 121, 483, 42,
				21, 83, 212, 471, 34, 472, 1, 473, 133,
				14, 44, 218, 491, 492, 493, 136,
				16, 511, 512, 513, 11,
				57, 501, 502, 503, 7,
				266, 267, 268, 274, 276, 278, 246
			};

			for (int i = 0; i < stdEffs.length; i++) {
				short effId = stdEffs[i];
				if (effId == defaultEff) continue;
				boolean isAdded = hasCustomEffect(slot, effId);
				String prefix = isAdded ? "[*] " : "[ ] ";
				menu.addElement(new iCommand(prefix + getEffectName(effId), 312, effId, GameCanvas.gameScr));
			}

			menu.addElement(new iCommand("< Quay lại", 311, slot, GameCanvas.gameScr));
			GameCanvas.menu.startAt(menu, 2, "Skill 5 Phái (Ấn chọn để THÊM/XÓA)");
		} catch (Exception ignored) {}
	}

	public static void setCustomSkillEff(int slot, short effId) {
		toggleCustomSkillEff(slot, effId);
	}

	public static void resetSlotCustomSkillEff(int slot) {
		if (slot >= 0 && slot < 5) {
			if (customMultiSlotEffects[slot] != null) {
				customMultiSlotEffects[slot].removeAllElements();
			} else {
				customMultiSlotEffects[slot] = new mVector();
			}
			short orig = getDefaultEffForSlot(slot);
			if (orig > 0) {
				customMultiSlotEffects[slot].addElement(new Short(orig));
				customSlotEffects[slot] = orig;
			} else {
				customSlotEffects[slot] = 0;
			}
			saveCustomSkillEffSettings();
			Interface_Game.addInfoPlayerNormal("Đã khôi phục Slot " + slot + " về Mặc Định", mFont.tahoma_7_yellow);
			openModSkillChooseEffMenu(slot);
		}
	}

	public static void resetAllCustomSkillEff() {
		for (int i = 0; i < 5; i++) {
			if (customMultiSlotEffects[i] != null) {
				customMultiSlotEffects[i].removeAllElements();
			} else {
				customMultiSlotEffects[i] = new mVector();
			}
			short orig = getDefaultEffForSlot(i);
			if (orig > 0) {
				customMultiSlotEffects[i].addElement(new Short(orig));
				customSlotEffects[i] = orig;
			} else {
				customSlotEffects[i] = 0;
			}
		}
		saveCustomSkillEffSettings();
		Interface_Game.addInfoPlayerNormal("Đã khôi phục tất cả Skill về Mặc Định", mFont.tahoma_7_yellow);
		openModSkillSelectSlotMenu();
	}

	public static void saveSpeedSetting(int speed) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(speed);
			CRes.saveRMS("MOD_GAME_SPEED", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadSpeedSetting() {
		try {
			byte[] data = CRes.loadRMS("MOD_GAME_SPEED");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				gameSpeed = dis.readInt();
				if (gameSpeed < 1) gameSpeed = 1;
				if (gameSpeed > 10) gameSpeed = 10;
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public static void saveAutoRedLineSettings() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeBoolean(isAutoRedLine);
			dos.writeBoolean(isAutoReconnect2);
			CRes.saveRMS("MOD_AUTO_REDLINE", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadAutoRedLineSettings() {
		try {
			byte[] data = CRes.loadRMS("MOD_AUTO_REDLINE");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				try { isAutoRedLine = dis.readBoolean(); } catch (Exception ignored) {}
				try { isAutoReconnect2 = dis.readBoolean(); } catch (Exception ignored) {}
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public void openMenuSellItem() {
		try {
			TabScreen tabAutoSellScr = new TabScreen(MainTab.xTab, (byte)0);
			mVector tabs = new mVector();
			TabAutoSellBag tabBag = new TabAutoSellBag("Hành Trang", Player.vecInventory, MainTab.xTab);
			tabBag.initCmd();
			tabs.addElement(tabBag);
			TabAutoSellList tabList = new TabAutoSellList("DS Tự Bán", autoSellItemsCache, MainTab.xTab);
			tabList.initCmd();
			tabs.addElement(tabList);
			tabAutoSellScr.addVecTab(tabs);
			tabAutoSellScr.Show(GameCanvas.gameScr);
		} catch (Exception ignored) {}
	}

	public static boolean isInAutoSell(MainItem mi) {
		if (mi == null) return false;
		for (int i = 0; i < autoSellList.size(); ++i) {
			short[] s = (short[]) autoSellList.elementAt(i);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) {
				return true;
			}
		}
		return false;
	}

	public static void removeFromAutoSell(MainItem mi) {
		if (mi == null) return;
		boolean removed = false;
		for (int i = 0; i < autoSellList.size(); ++i) {
			short[] s = (short[]) autoSellList.elementAt(i);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) {
				autoSellList.removeElementAt(i);
				removed = true;
				break;
			}
		}
		for (int j = 0; j < autoSellItemsCache.size(); j++) {
			MainItem cached = (MainItem) autoSellItemsCache.elementAt(j);
			if (cached != null && cached.typeObject == mi.typeObject && cached.ID == mi.ID) {
				autoSellItemsCache.removeElementAt(j);
				removed = true;
				break;
			}
		}
		if (removed) {
			saveAutoSellList();
		}
	}

	public static void checkBlockMessage(String msg) {
		if (msg == null || lastProcessedItem == null) return;
		if (GameCanvas.timeNow - lastProcessedTime > 4000) return;
		String lower = msg.toLowerCase();
		boolean isBlock = false;
		String[] keywords = new String[]{
			"không thể vứt", "khong the vut", "không thể bán", "khong the ban", "không thể bỏ", "khong the bo",
			"không vứt được", "khong vut duoc", "không bán được", "khong ban duoc", "vật phẩm khóa", "vat pham khoa",
			"trang bị khóa", "trang bi khoa", "đồ khóa", "cannot throw", "cannot sell", "cannot discard",
			"không thể thực hiện", "khong the thuc hien"
		};
		for (int i = 0; i < keywords.length; i++) {
			if (lower.indexOf(keywords[i]) != -1) {
				isBlock = true;
				break;
			}
		}
		if (isBlock) {
			Interface_Game.addInfoPlayerNormal("Chặn! Xóa DS: " + lastProcessedItem.name, mFont.tahoma_7_yellow);
			removeFromAutoSell(lastProcessedItem);
			pendingDrops.removeAllElements();
			lastProcessedItem = null;
		}
	}

	public static void toggleAutoSell(MainItem mi) {
		if (mi == null) return;
		if (isInAutoSell(mi)) {
			removeFromAutoSell(mi);
			Interface_Game.addInfoPlayerNormal("Đã bỏ khỏi danh sách tự bán: " + mi.name, mFont.tahoma_7_yellow);
			return;
		}
		if (autoSellItemsCache.size() >= 126) {
			Interface_Game.addInfoPlayerNormal("Danh sách tự bán tối đa 126 ô!", mFont.tahoma_7_yellow);
			return;
		}
		short[] entry = new short[]{(short)mi.typeObject, (short)mi.ID};
		autoSellList.addElement(entry);
		MainItem copy = new MainItem(mi.typeObject, mi.ID, mi.idIcon, mi.name, (byte)0);
		copy.colorName = mi.colorName;
		copy.numPotion = (short)((mi.numPotion > 0) ? mi.numPotion : 1);
		autoSellItemsCache.addElement(copy);
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Đã thêm vào danh sách tự bán: " + mi.name, mFont.tahoma_7_yellow);
	}

	public static void clearAutoSellList() {
		autoSellList.removeAllElements();
		autoSellItemsCache.removeAllElements();
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Xóa toàn bộ danh sách tự bán", mFont.tahoma_7_yellow);
	}

	public static void removeAutoSellAt(int idx) {
		if (idx < 0 || idx >= autoSellList.size()) return;
		short[] s = (short[]) autoSellList.elementAt(idx);
		autoSellList.removeElementAt(idx);
		for (int j = 0; j < autoSellItemsCache.size(); j++) {
			MainItem cached = (MainItem) autoSellItemsCache.elementAt(j);
			if (cached != null && cached.typeObject == s[0] && cached.ID == s[1]) {
				autoSellItemsCache.removeElementAt(j);
				break;
			}
		}
		saveAutoSellList();
		Interface_Game.addInfoPlayerNormal("Đã xóa mục tự bán", mFont.tahoma_7_yellow);
	}

	public static void cleanAutoSellCache() {
		// Giữ nguyên danh sách đã cài đặt, chỉ lọc bỏ phần tử null
		for (int i = autoSellList.size() - 1; i >= 0; --i) {
			if (autoSellList.elementAt(i) == null) {
				autoSellList.removeElementAt(i);
			}
		}
	}

	private static boolean isPending(MainItem mi) {
		if (mi == null) return false;
		for (int p = 0; p < pendingDrops.size(); ++p) {
			short[] s = (short[]) pendingDrops.elementAt(p);
			if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) {
				return true;
			}
		}
		return false;
	}

	private static void addPending(MainItem mi) {
		if (mi != null && !isPending(mi)) {
			short[] entry = new short[]{(short)mi.typeObject, (short)mi.ID};
			pendingDrops.addElement(entry);
		}
	}

	private static void removePendingIfNotExistsInInventory() {
		if (Player.vecInventory == null) return;
		for (int i = pendingDrops.size() - 1; i >= 0; i--) {
			short[] s = (short[]) pendingDrops.elementAt(i);
			boolean found = false;
			for (int j = 0; j < Player.vecInventory.size(); j++) {
				MainItem mi = (MainItem) Player.vecInventory.elementAt(j);
				if (mi != null && mi.typeObject == s[0] && mi.ID == s[1]) {
					found = true;
					break;
				}
			}
			if (!found) {
				pendingDrops.removeElementAt(i);
			}
		}
	}

	public static void autoProcessItems() {
		if (Player.vecInventory == null || GameCanvas.gameTick % 20 != 0) return;
		if (!Player.isAutoFilterItems) return;
		removePendingIfNotExistsInInventory();
		if (pendingDrops.size() > 0) {
			if (GameCanvas.timeNow - lastProcessedTime > 4000) {
				pendingDrops.removeAllElements();
				lastProcessedItem = null;
			} else {
				return;
			}
		}
		for (int i = Player.vecInventory.size() - 1; i >= 0; --i) {
			MainItem mi = (MainItem) Player.vecInventory.elementAt(i);
			if (mi != null && isInAutoSell(mi)) {
				if (mi.numPotion <= 0) continue;
				lastProcessedItem = mi;
				lastProcessedTime = GameCanvas.timeNow;
				GlobalService.getInstance().Sell_Item((byte)0, mi.ID, (byte)mi.typeObject, (short)mi.numPotion);
				addPending(mi);
				Interface_Game.addInfoPlayerNormal("Tự động bán: " + mi.name, mFont.tahoma_7_yellow);
				break;
			}
		}
	}

	public static void saveAutoSellList() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(autoSellItemsCache.size());
			for (int i = 0; i < autoSellItemsCache.size(); i++) {
				MainItem mi = (MainItem) autoSellItemsCache.elementAt(i);
				dos.writeByte(mi.typeObject);
				dos.writeShort(mi.ID);
				dos.writeShort(mi.idIcon);
				dos.writeUTF(mi.name != null ? mi.name : "");
				dos.writeByte(mi.colorName);
			}
			CRes.saveRMS("MOD_AUTO_SELL_LIST", baos.toByteArray());
			dos.close();
		} catch (Exception ignored) {}
	}

	public static void loadAutoSellList() {
		try {
			byte[] data = CRes.loadRMS("MOD_AUTO_SELL_LIST");
			if (data != null && data.length > 0) {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				int size = dis.readInt();
				autoSellList.removeAllElements();
				autoSellItemsCache.removeAllElements();
				for (int i = 0; i < size; i++) {
					byte typeObject = dis.readByte();
					short ID = dis.readShort();
					short idIcon = dis.readShort();
					String name = dis.readUTF();
					byte colorName = dis.readByte();
					MainItem mi = new MainItem(typeObject, ID, idIcon, name, (byte)0);
					mi.colorName = colorName;
					mi.numPotion = 1;
					autoSellItemsCache.addElement(mi);
					short[] entry = new short[]{(short)typeObject, (short)ID};
					autoSellList.addElement(entry);
				}
				dis.close();
			}
		} catch (Exception ignored) {}
	}

	public static void checkPortalStepped(Player p) {
		if (GameCanvas.loadmap == null || LoadMap.vecPointChange == null) return;
		for (int i = 0; i < LoadMap.vecPointChange.size(); i++) {
			Point pt = (Point) LoadMap.vecPointChange.elementAt(i);
			if (pt != null) {
				int dist = MainObject.getDistance(p.x, p.y, pt.x, pt.y);
				if (dist < 50) {
					lastSteppedPortalName = pt.name;
					break;
				}
			}
		}
	}

	public static void recordTransition(int fromMapId, int toMapId, String portalName) {
		if (shouldReturnToLastMap) return;
		int existingIdx = -1;
		for (int i = 0; i < recordedMapIds.size(); i++) {
			Integer mid = (Integer) recordedMapIds.elementAt(i);
			if (mid != null && mid.intValue() == toMapId) {
				existingIdx = i;
				break;
			}
		}
		if (existingIdx != -1) {
			while (recordedMapIds.size() > existingIdx + 1) {
				recordedMapIds.removeElementAt(recordedMapIds.size() - 1);
				recordedPortalNames.removeElementAt(recordedPortalNames.size() - 1);
			}
		} else {
			if (recordedMapIds.size() == 0) {
				recordedMapIds.addElement(new Integer(fromMapId));
			}
			recordedPortalNames.addElement(portalName != null ? portalName : "");
			recordedMapIds.addElement(new Integer(toMapId));
		}
	}

	public static void updateAvoidPK(Player p) {
		if (!isAvoidPK || p == null || p.Hp <= 0 || GameCanvas.loadMapScr == null) return;
		if (GameCanvas.timeNow - lastAvoidChangeZoneTime < 5000) return;
		mVector vec = GameScreen.vecPlayers;
		if (vec == null) return;
		boolean hasPKer = false;
		for (int i = 0; i < vec.size(); i++) {
			MainObject o = (MainObject) vec.elementAt(i);
			if (o != null && o.typeObject == 0 && o != p && !o.isRemove && o.Hp > 0) {
				if (o.typePK > 0 || o.colorName == 2 || o.colorName == 4 || o.colorName == 5 || p.AC(o)) {
					hasPKer = true;
					break;
				}
			}
		}
		if (hasPKer) {
			byte currentArea = GameCanvas.loadMapScr.area;
			byte nextArea = (byte) ((currentArea + 1) % 10);
			try {
				GlobalService.getInstance().AB((byte)0, nextArea);
				Interface_Game.addInfoPlayerNormal("Phát hiện PK! Đang né sang khu " + nextArea, mFont.tahoma_7_yellow);
				lastAvoidChangeZoneTime = GameCanvas.timeNow;
			} catch (Exception ignored) {}
		}
	}

	public static void updateAvoidBoss(Player p) {
		if (!isAvoidBoss || p == null || p.Hp <= 0 || GameCanvas.loadMapScr == null) return;
		if (GameCanvas.timeNow - lastAvoidBossChangeZoneTime < 5000) return;
		mVector vec = GameScreen.vecPlayers;
		if (vec == null) return;
		boolean hasBoss = false;
		for (int i = 0; i < vec.size(); i++) {
			MainObject o = (MainObject) vec.elementAt(i);
			if (o != null && o.typeObject == 1 && !o.isRemove && o.Hp > 0) {
				if (o.MR != 0 || o.typeBossMonster != 0 || o.typeSpecMonSter == 1) {
					hasBoss = true;
					break;
				}
			}
		}
		if (hasBoss) {
			byte currentArea = GameCanvas.loadMapScr.area;
			byte nextArea = (byte) ((currentArea + 1) % 10);
			try {
				GlobalService.getInstance().AB((byte)0, nextArea);
				Interface_Game.addInfoPlayerNormal("Phát hiện Boss! Đang né sang khu " + nextArea, mFont.tahoma_7_yellow);
				lastAvoidBossChangeZoneTime = GameCanvas.timeNow;
			} catch (Exception ignored) {}
		}
	}

	public static void returnToLastMap() {
		if (lastMapId <= 0) {
			Interface_Game.addInfoPlayerNormal("Không có map cũ để quay lại.", mFont.tahoma_7_yellow);
			shouldReturnToLastMap = false;
			return;
		}
		try {
			int currentMap = GameCanvas.loadmap != null ? GameCanvas.loadmap.idMapLoadMap : -1;
			if (currentMap == lastMapId) {
				try {
					AThMadaraFunc.restoreSavedZoneIfAny();
				} catch (Exception ignored) {}
				if (lastX > 0 && lastY > 0) {
					try {
						GlobalService.getInstance().Obj_Move((short) lastX, (short) lastY);
						if (GameScreen.player != null) {
							GameScreen.player.x = lastX;
							GameScreen.player.y = lastY;
							GameScreen.player.CX = lastX;
							GameScreen.player.CY = lastY;
							Player.isSendMove = true;
						}
					} catch (Exception ignored) {}
				}
				Interface_Game.addInfoPlayerNormal("Đã quay lại vị trí cũ.", mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
				lastMapId = -1;
				lastX = -1;
				lastY = -1;
				triedChangeMapOkOnce = false;
				return;
			}
			if (GameCanvas.timeNow < nextWalkToPortalTime) {
				return;
			}
			int curIdx = -1;
			for (int i = 0; i < recordedMapIds.size(); i++) {
				Integer mid = (Integer) recordedMapIds.elementAt(i);
				if (mid != null && mid.intValue() == currentMap) {
					curIdx = i;
					break;
				}
			}
			if (curIdx == -1 || curIdx >= recordedPortalNames.size()) {
				Interface_Game.addInfoPlayerNormal("Mất dấu đường về map cũ. Vui lòng tự đi lại!", mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
				return;
			}
			String nextPortalName = (String) recordedPortalNames.elementAt(curIdx);
			Point targetPortal = null;
			if (LoadMap.vecPointChange != null) {
				for (int i = 0; i < LoadMap.vecPointChange.size(); i++) {
					Point pt = (Point) LoadMap.vecPointChange.elementAt(i);
					if (pt != null && pt.name != null && pt.name.equals(nextPortalName)) {
						targetPortal = pt;
						break;
					}
				}
				if (targetPortal == null && LoadMap.vecPointChange.size() > 0) {
					targetPortal = (Point) LoadMap.vecPointChange.elementAt(0);
				}
			}
			if (targetPortal != null) {
				Player p = GameScreen.player;
				if (p != null) {
					int tx = targetPortal.x;
					int ty = targetPortal.y;
					int dist = MainObject.getDistance(p.x, p.y, tx, ty);
					if (dist > 28) {
						AThMadaraFunc.sendTeleport(p, tx, ty);
						Interface_Game.addInfoPlayerNormal("Đang di chuyển tới cổng: " + nextPortalName, mFont.tahoma_7_yellow);
					} else {
						GlobalService.getInstance().Obj_Move((short) tx, (short) ty);
						p.posTransRoad = null;
					}
				}
				nextWalkToPortalTime = GameCanvas.timeNow + 1500;
			} else {
				Interface_Game.addInfoPlayerNormal("Không tìm thấy cổng: " + nextPortalName, mFont.tahoma_7_yellow);
				shouldReturnToLastMap = false;
			}
		} catch (Exception e) {
			shouldReturnToLastMap = false;
		}
	}
}
