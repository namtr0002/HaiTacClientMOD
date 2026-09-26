using System;

public class GameScreen : MainScreen
{
	public static iCommand[] listCmdTest;

	public static Player player = new Player();

	public static MainObject objFocus = null;

	public static MainObject objGiaotiep;

	public static MainObject objPvPNew = null;

	public static mVector vecPlayers = new mVector("GameScreen.vecPlayers");

	public static bool isVecPlayersDirty = false;
	private static string s_cachedDbgStr = string.Empty;
	private static int s_lastDbgX = -99999;
	private static int s_lastDbgY = -99999;
	private static int s_lastDbgMap = -99999;

	public static void updateCameraBounds()
	{
		if (GameCanvas.loadmap != null && cameraMain != null)
		{
			cameraMain.setAll(GameCanvas.loadmap.limitW, GameCanvas.loadmap.limitH, cameraMain.xCam, cameraMain.yCam);
		}
	}

	public static mVector vecEffTam = new mVector("GameScreen.vecEffTam");

	public static mVector VecEffect = new mVector("GameScreen.vecEffect");

	public static mVector VecNum = new mVector("GameScreen.vecNum");

	public static mVector vecBoat = new mVector("GameScreen.vecBoat");

	public static mVector vecObjMove = new mVector("GameScreen.vecObjMove");

	public static mVector vecObjFire = new mVector("GameScreen.vecObjFire");

	public static mVector vecHighDataEff = new mVector();

	private int nump;

	private int numo;

	private MainObject ob;

	private MainObject maxob = new MainObject();

	public static MainItemMap tr;

	public static MainItemMap maxtr = new MainItemMap();

	public static Interface_Game interfaceGame = new Interface_Game();

	public iCommand cmdNextFocus;

	public iCommand cmdHoiSinh;

	public iCommand cmdGetXpMapTrain;

	public iCommand cmdThoatFormMapTrain;

	public iCommand cmdInfoMe;

	public iCommand cmdSetPk;

	public iCommand cmdSetDosat;

	public iCommand cmdParty;

	public iCommand cmdInviteParty;

	public iCommand cmdLogOut;

	public iCommand cmdTrochuyen;

	public iCommand cmdChatPlayer;

	public iCommand cmdAuto;

	public iCommand cmdChatPopup;

	public iCommand cmdAddFriend;

	public iCommand cmdFriendList;

	public iCommand cmdBlackList;

	public iCommand cmdEvent;

	public iCommand cmdChangeTouch;

	public iCommand cmdFight;

	public iCommand cmdVeLang;

	public iCommand cmdInfoPlayer;

	public iCommand cmdSettingView;

	public iCommand cmdExitView;

	public iCommand cmdExit;

	public iCommand cmdWC;

	public iCommand cmdShowWC;

	public iCommand cmdInviteTrade;

	public iCommand cmdBuyGem;

	public iCommand cmdSendSerial_Mathe;

	public iCommand cmdHoiExit;

	public iCommand cmdMenuPk;

	public iCommand cmdClan;

	public iCommand cmdXinVaoClan;

	public iCommand cmdMoiVaoClan;

	public iCommand cmdShowName;

	public iCommand cmdMPHP;

	public iCommand cmdGetItem;

	public iCommand cmdDonotShowHat;

	public iCommand cmdDonotShowWeaponF;

	public iCommand cmdInfoDoithu;

	public iCommand cmdUniform;

	public iCommand cmdQuickChat;

	public iCommand cmdGiaotiepServer;

	public iCommand cmdLockChat;

	public iCommand cmdShowSkillBuff;

	public iCommand cmdSpam;

	public iCommand cmdTangRuby;

	public iCommand cmdDauGia;

	public iCommand cmdShowSkillPlayer;

	public iCommand cmdShowNhanVat;

	public iCommand cmdSudo;

	public iCommand cmdBaisu;

	public iCommand cmdPet;

	public static iCommand cmdGiaoTiep;

	public static iCommand cmdVotBanh;

	public static iCommand cmdCheckServer;

	public static iCommand cmdReConnect;

	public static int numMess = 0;

	public static bool isMoveCamera = false;

	public static bool isMoveCamEff = false;

	public static bool isPvPNew = false;

	public static int xMoveCam;

	public static int yMoveCam;

	public static int xCur;

	public static int yCur;

	public static int timeResetCam;

	public static int typeMod = 0;

	public static Effect_Map effMap;

	public static Effect_Map effSea;

	public static sbyte typeViewPlayer = 0;

	public const sbyte VIEW_NORMAL = 0;

	public const sbyte VIEW_KHANGIA = 1;

	public static MainObject objView = null;

	public static bool isSetObjdDefault = false;

	public static bool isShowFocusTK = false;

	public static bool isShowSkillBuff = true;

	public static bool isShowSkillPlayer = true;

	public static bool isShowNhanVat = true;

	private InputDialog input;

	public static mVector vecHelp;

	public static int indexHelp = -1;

	public static int indexHDVaoLang = 0;

	public static int tickPvP;

	public static int h12plus = 0;

	public static mVector vecBigBossLittleGraden = new mVector("GameScreen.vecBigBossLittleGraden");

	public static mVector vecGetItemLittle = new mVector("GameScreen.getItemLittle");

	public static bool isShowNameSUPER_BOSS = true;

	public static bool isShowNameXpArena = false;

	public static bool isShowNameSetting = true;

	public static bool isShowNameWW = false;

	public static bool isPaintInterface = true;

	public static bool isShowChat = true;

	public static bool isOffAll = false;

	public static bool isShowName = true;

	public static bool isOnChestAdmin = false;

	public static bool isShowIndex = false;

	public static bool isIP_Local = false;

	public static bool isShowText = false;

	public static bool isShowTextTab = false;

	public static MainClan ClanDao;

	public static bool isOpenDao;

	public int xRec;

	public int yRec;

	public int wRec;

	public int hRec;

	public int colorRec;

	public bool isFullScreen;

	public float fullScreenAlpha;

	public static bool isOnRepeatQuest = false;

	public static short numDot = 0;

	public static bool isOnSuperBoss = false;

	public static mVector vecQuickOpenPotion = new mVector("GameScreen.vecQuickOpenPotion");

	public static short[] listMonsterPokemon = new short[15]
	{
		115, 116, 117, 118, 119, 121, 128, 129, 130, 131,
		168, 169, 170, 171, 172
	};

	public const int option1 = 100;

	public const int option2 = 125;

	public const int option3 = 150;

	public static bool isOnAutoPB = false;

	private AutoRepeatQuest autoRepeatQuest = new AutoRepeatQuest();

	private AutoSuperBoss autoSuperBoss = new AutoSuperBoss();

	public static sbyte typePaintGameScreen = 0;

	public static int dx = 0;

	public static int dy = 0;

	private int xWT2 = MotherCanvas.hw - AvMain.wWanted / 2;

	private int yWT2 = MotherCanvas.hh - AvMain.hWanted / 2;

	public static mVector vecEffSkillSpec = new mVector();

	private static int size = MotherCanvas.h / 6;

	private long timeBeginAfk;

	public static int numEff;

	public static int tickSpam = 0;

	public const sbyte FOOT = 0;

	public static mVector vecEffOnMap = new mVector();

	public static sbyte typeSuperEffect = -1;

	public static int tickSuperEffect = 0;

	public const sbyte LIGHT = 0;

	public const sbyte ADMIN_OFF_NAME = 0;

	public const sbyte ADMIN_OFF_INTERFACE = 1;

	public const sbyte ADMIN_OFF_CHAT = 2;

	public override void Show()
	{
		GameCanvas.gameScr.left = null;
		GameCanvas.gameScr.right = null;
		GameCanvas.gameScr.center = null;
		Interface_Game.timeShowNear = 0;
		base.Show();
		GameCanvas.mapLogin = null;
		setTypeViewPlayer(typeViewPlayer);
		vecEffOnMap.removeAllElements();
		if (GameCanvas.loadmap.mapLang())
		{
			if (Player.isAutoMaterial == 1 || Player.isAutoMaterial == 3)
			{
				Player.SetMaterialToChest(7);
			}
			if (Player.isAutoMaterial == 2 || Player.isAutoMaterial == 3)
			{
				Player.SetDiamondToChest();
			}
		}
		cmdInfoDoithu = null;
		setNumMess();
		Clan_Screen.isNew = false;
		setNewEventCLan();
	}

	public void setNauBanh(bool isNauBanh)
	{
		player.isNauBanh = isNauBanh;
		player.tickNauBanh = 0;
		if (isNauBanh)
		{
			center = cmdVotBanh;
		}
		else
		{
			center = null;
		}
	}

	public override void setxyPlus12()
	{
		GameCanvas.xPlus12 = 2;
		GameCanvas.yPlus12 = 2;
	}

	private void setNewEventCLan()
	{
		if (player.clan == null || GameCanvas.ClanScr == null)
		{
			return;
		}
		for (int i = 0; i < GameCanvas.ClanScr.vecTabChat.size(); i++)
		{
			if (((ChatDetail)GameCanvas.ClanScr.vecTabChat.elementAt(i)).isNew)
			{
				Clan_Screen.isNew = true;
				break;
			}
		}
	}

	public static void setNumMess()
	{
		numMess = 0;
		if (GameCanvas.eventScr != null && GameCanvas.eventScr.vecPlayer != null)
		{
			for (int i = 0; i < GameCanvas.eventScr.vecPlayer.size(); i++)
			{
				if (((InfoMemList)GameCanvas.eventScr.vecPlayer.elementAt(i)).isNew)
				{
					numMess++;
				}
			}
		}
		if (GameCanvas.chatTabScr != null)
		{
			numMess += GameCanvas.chatTabScr.getUnreadCountInCat(ChatDetail.CAT_MAIL);
			numMess += GameCanvas.chatTabScr.getUnreadCountInCat(ChatDetail.CAT_PRIVATE);
		}
	}

	public GameScreen()
	{
		cmdNextFocus = new iCommand(T.next, 0, this);
		cmdHoiSinh = new iCommand(T.revice, 1, this);
		cmdVeLang = new iCommand(T.gohome, 2, 0, this);
		cmdGetXpMapTrain = new iCommand(T.getXp, 3, 1, this);
		cmdThoatFormMapTrain = new iCommand(T.exit, 27, 2, this);
		cmdInfoPlayer = new iCommand(T.info, 28, this);
		cmdInfoMe = new iCommand(T.menu, 6, 0, this);
		cmdSetPk = new iCommand(T.Pk, 7, 0, this);
		cmdSetDosat = new iCommand(T.Dosat, 8, 0, this);
		cmdGiaoTiep = new iCommand(T.Giaotiep, 9, 0, this);
		cmdParty = new iCommand(T.party, 10, 0, this);
		cmdInviteParty = new iCommand(T.cmdloimoiParty, 11, 0, this);
		cmdReConnect = new iCommand(T.again, 12, 0, this);
		cmdLogOut = new iCommand(T.logout, 13, 0, this);
		cmdExit = new iCommand(T.exit, 13, 0, this);
		cmdTrochuyen = new iCommand(T.TroChuyen, 14, 0, this);
		cmdChatPlayer = new iCommand(T.TroChuyen, 15, 0, this);
		cmdAuto = new iCommand(T.setting, 16, 0, this);
		cmdChatPopup = new iCommand(T.chat, 17, 0, this);
		cmdAddFriend = new iCommand(T.addFriend, 20, this);
		cmdFriendList = new iCommand(T.friendList, 21, this);
		cmdBlackList = new iCommand(T.blackList, 22, this);
		cmdEvent = new iCommand(T.cmdEvent, 23, this);
		cmdEvent.hardcodeEvent = true;
		cmdChangeTouch = new iCommand(T.keypad, 24, this);
		cmdFight = new iCommand(T.fight, 26, this);
		cmdSettingView = new iCommand(T.setting, 29, this);
		if (GameCanvas.isTouch)
		{
			cmdSettingView = AvMain.setPosCMD(cmdSettingView, 1);
		}
		cmdExitView = new iCommand(T.exit, 30, this);
		cmdShowWC = new iCommand(T.WorldChanel, 33, this);
		cmdInviteTrade = new iCommand(T.trade, 35, this);
		cmdBuyGem = new iCommand(T.cmdBuy + " " + T.ruby, 36, this);
		cmdSendSerial_Mathe = new iCommand(T.napthe, 37, this);
		cmdHoiExit = new iCommand(T.exit, 38, this);
		cmdMenuPk = new iCommand(T.chonco, 39, this);
		cmdClan = new iCommand(T.Clan, 40, this);
		cmdMoiVaoClan = new iCommand(T.moivaoclan, 41, this);
		cmdXinVaoClan = new iCommand(T.xinvaoclan, 42, this);
		cmdShowName = new iCommand(T.showname, 43, this);
		cmdInfoDoithu = new iCommand(T.info, 47, this);
		cmdUniform = new iCommand(T.changeEquip, 48, this);
		cmdQuickChat = new iCommand(T.quickChat, 49, this);
		cmdGiaotiepServer = new iCommand(T.Giaotiep, 52, this);
		cmdLockChat = new iCommand("LockChat", 53, this);
		cmdShowSkillBuff = new iCommand(T.showSkillBuff, 55, this);
		cmdSpam = new iCommand(T.spamSetup, 57, this);
		cmdTangRuby = new iCommand(T.tangRuby, 58, this);
		cmdDauGia = new iCommand(T.dauGia, 59, this);
		cmdShowSkillPlayer = new iCommand(T.showSkillPlayer, 60, this);
		cmdShowNhanVat = new iCommand(T.showNhanVat, 61, this);
		cmdVotBanh = new iCommand(T.votBanh, 62, this);
		cmdSudo = new iCommand(T.suPhu, 63, this);
		cmdBaisu = new iCommand(T.baisu, 64, this);
		cmdPet = new iCommand(T.pet, 65, this);
		right = cmdNextFocus;
		left = cmdInfoMe;
		if (GameMidlet.DEVICE == 0)
		{
			new Effect_Skill();
		}
	}

	public override void commandPointer(int index, int subIndex)
	{
		if (index >= 1251 && index < 1251 + UIThemeManager.TOTAL_THEMES)
		{
			int selTheme = index - 1251;
			UIThemeManager.setTheme(selTheme);
			GameCanvas.menu.doCloseMenu();
			return;
		}
		if (index == 1250)
		{
			openSelectThemeMenu();
			return;
		}
		switch (index)
		{
		case -52:
			if (!GameMidlet.isTest)
			{
				break;
			}
			GameMidlet.numTest = 2;
			switch (GameMidlet.numTest)
			{
			case 0:
			{
				MainSkill skill = new MainSkill(1999, 1999);
				MainObject mainObject = objFocus;
				if (mainObject != null)
				{
					addEffectSkill(skill, mainObject);
				}
				break;
			}
			case 1:
			{
				player.addEffBuff(3, 2000, 0);
				MainSkill skill = new MainSkill(2000, 2000);
				MainObject mainObject = objFocus;
				if (mainObject != null)
				{
					addEffectSkill(skill, mainObject);
				}
				break;
			}
			case 2:
				addEffectBuffNew(player, 2060, 30000);
				break;
			}
			GameMidlet.numTest++;
			if (GameMidlet.numTest > 2)
			{
				GameMidlet.numTest = 0;
			}
			break;
		case 0:
			if (objFocus != null && !objFocus.returnAction())
			{
				player.nextFocus();
			}
			break;
		case 1:
			setRevice();
			break;
		case 2:
			switch (subIndex)
			{
			case 0:
				GameCanvas.Start_Normal_DiaLog(T.hoiGoHome, new iCommand(T.gohome, 2, 2, this), isCmdClose: true);
				break;
			case 1:
				GlobalService.gI().Player_Revice(1);
				GameCanvas.end_Dialog();
				break;
			case 2:
				GlobalService.gI().Player_Revice(0);
				GameCanvas.end_Dialog();
				break;
			}
			break;
		case 3:
			GlobalService.gI().Get_Xp_Map_Train((sbyte)subIndex);
			break;
		case 4:
			GameCanvas.menu.startAt(getMenu(), 2, T.menu);
			break;
		case 5:
			GlobalService.gI().Set_PK((sbyte)subIndex, 0);
			GameCanvas.end_Dialog();
			break;
		case 6:
			GameCanvas.clearKeyHold();
			GameCanvas.clearKeyPressed();
			if (GameCanvas.isCompactMode())
			{
				GameCanvas.tabInven.setTypeInven(0);
				GameCanvas.tabAllScr.Show(this);
			}
			else
			{
				DualTabScreen.gI().curMainTab = 0;
				DualTabScreen.gI().Show(this);
			}
			player.resetAction();
			GameCanvas.clearAll();
			if (indexHelp == 10 || indexHelp == 14 || indexHelp == 15 || indexHelp == 16)
			{
				MainHelp.setNextHelp(isHanhTrang: true);
			}
			break;
		case 7:
		{
			if (player.Hp <= 0)
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.trongthuong);
				break;
			}
			mVector mVector3 = new mVector();
			for (int i = -1; i < 6; i++)
			{
				iCommand iCommand5 = null;
				if (i == -1)
				{
					iCommand5 = new iCommand(T.thaoco, 5, -1, this);
				}
				else
				{
					iCommand5 = new iCommand(T.mTypePk[3 + i], 5, 3 + i, this);
					iCommand5.setFraCaption(AvMain.fraPk, 3, (3 + i) * 3, 0);
				}
				mVector3.addElement(iCommand5);
			}
			GameCanvas.menu.startAt(mVector3, 2, T.Pk);
			break;
		}
		case 8:
			if (player.Hp <= 0)
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.trongthuong);
			}
			else if (player.typePK == 0)
			{
				GlobalService.gI().Set_PK(-1, 0);
				Interface_Game.addInfoPlayerNormal(T.tatdosat, mFont.tahoma_7_yellow);
			}
			else
			{
				GlobalService.gI().Set_PK(0, 0);
				Interface_Game.addInfoPlayerNormal(T.batdosat, mFont.tahoma_7_yellow);
			}
			break;
		case 9:
			if (objFocus != null)
			{
				objFocus.Giaotiep();
			}
			break;
		case 10:
			if (Player.vecParty.size() > 0)
			{
				PartyScreen.gI().setCamera();
				PartyScreen.gI().Show(GameCanvas.gameScr);
			}
			break;
		case 11:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Party(0, objGiaotiep.ID);
			}
			break;
		case 12:
			GameCanvas.loginScr.Show();
			if (GameCanvas.loginScr.tfUser.getText().Length > 0)
			{
				GameCanvas.loginScr.doLogin(isGetData: false, 0, GameCanvas.loginScr.tfUser.getText(), GameCanvas.loginScr.tfPass.getText());
			}
			else
			{
				GameCanvas.fristLoginScr.cmdBegin.perform();
			}
			break;
		case 13:
			ListChar_Screen.IndexCharSelected = -1;
			GameCanvas.loginScr.Show();
			break;
		case 14:
			GameCanvas.chatTabScr.Show(GameCanvas.gameScr);
			break;
		case 15:
			if (objGiaotiep != null)
			{
				ShowChatTab(objGiaotiep.name);
			}
			break;
		case 16:
		{
			mVector menuGameNew = getMenuGameNew();
			GameCanvas.menu.startAt(menuGameNew, 2, T.setting);
			break;
		}
		case 17:
			if (GameCanvas.currentScreen == this && !GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && !Player.isGhost)
			{
				ChatTextField.gI().setChat();
			}
			break;
		case 18:
			if (Player.isMPHP)
			{
				Player.isMPHP = false;
				GameCanvas.saveRms.SaveAutoMp_Hp();
				setCaptionCmdAutoGetItem();
			}
			else
			{
				MsgAutoMPHP msgAutoMPHP = new MsgAutoMPHP();
				msgAutoMPHP.setinfoAuto_MP_HP();
				GameCanvas.Start_Current_Dialog(msgAutoMPHP);
			}
			break;
		case 19:
		{
			MsgAutoFire msgAutoFire = new MsgAutoFire();
			msgAutoFire.setinfoAuto_Fire();
			GameCanvas.Start_Current_Dialog(msgAutoFire);
			break;
		}
		case 20:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Friend(0, objGiaotiep.ID);
			}
			break;
		case 21:
			if (FriendList.isGetListFriend)
			{
				FriendList.gI().Show(this);
			}
			else
			{
				GlobalService.gI().Friend(2, 0);
			}
			break;
		case 22:
			GlobalService.gI().ListFromServer(2, 2, 0);
			break;
		case 23:
			GameCanvas.eventScr.Show(this);
			break;
		case 24:
			if (Interface_Game.typeTouch == 0)
			{
				Interface_Game.typeTouch = 1;
			}
			else
			{
				Interface_Game.typeTouch = 0;
			}
			cmdChangeTouch.caption = T.keypad;
			if (Interface_Game.typeTouch == 0)
			{
				cmdChangeTouch.caption = T.touch;
				Interface_Game.addInfoPlayerNormal(T.chuyenkeypad, mFont.tahoma_7_yellow);
			}
			else
			{
				Interface_Game.addInfoPlayerNormal(T.chuyentouch, mFont.tahoma_7_yellow);
			}
			try
			{
				CRes.saveRMS("SUB_TYPETOUCH", new sbyte[1] { Interface_Game.typeTouch });
			}
			catch (Exception)
			{
			}
			interfaceGame.setPosSkill();
			Interface_Game.setPosTouch();
			break;
		case 25:
			if (Player.isGetItem)
			{
				Player.isGetItem = false;
				GameCanvas.saveRms.SaveAutoGetItem();
				setCaptionCmdAutoGetItem();
			}
			else
			{
				MsgAutoGetItem msgAutoGetItem = new MsgAutoGetItem();
				msgAutoGetItem.setinfoAuto_Get_Item();
				GameCanvas.Start_Current_Dialog(msgAutoGetItem);
			}
			break;
		case 27:
			GameCanvas.Start_Normal_DiaLog(T.hoiThoatMapTrain, cmdLogOut, isCmdClose: true);
			break;
		case 28:
			if (objGiaotiep != null)
			{
				ShowInfoOtherPlayer(objGiaotiep.name);
			}
			break;
		case 29:
			if (typeViewPlayer != 0)
			{
				doMenuSettingView();
			}
			break;
		case 30:
			if (typeViewPlayer != 0)
			{
				GlobalService.gI().Exit_View();
			}
			break;
		case 31:
			objView = MainObject.get_Object(subIndex, 0);
			break;
		case 32:
			objView = null;
			break;
		case 33:
			if (input == null)
			{
				input = new InputDialog();
				iCommand cmd3 = new iCommand(T.chat, 34, this);
				input.setinfo(T.nhapNoiDung, cmd3, isNum: false, T.WorldChanel);
			}
			else
			{
				input.tfInput.setText("");
			}
			GameCanvas.Start_Current_Dialog(input);
			break;
		case 34:
			GlobalService.gI().World_Chanel(0, input.tfInput.getText());
			break;
		case 35:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Trade(6, objGiaotiep.ID, objGiaotiep.typeObject, 0, "");
			}
			break;
		case 36:
			if (GameCanvas.isDeviceStore() && GameMidlet.DEVICE != 4)
			{
				Open_Nap_Store();
			}
			else
			{
				Open_Dialog_Nap();
			}
			break;
		case 37:
		{
			string[] array = null;
			if (input.mtfInput != null)
			{
				array = new string[input.mtfInput.Length];
				for (int j = 0; j < input.mtfInput.Length; j++)
				{
					array[j] = input.mtfInput[j].getText();
				}
				if (array.Length >= 2)
				{
					GlobalService.gI().Buy_Gem(array[0], array[1]);
				}
			}
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.dangnapthe);
			break;
		}
		case 38:
			GameCanvas.Start_Normal_DiaLog(T.doitaikhoan, cmdExit, isCmdClose: true);
			break;
		case 39:
		{
			mVector mVector4 = new mVector();
			mVector4.addElement(GameCanvas.gameScr.cmdSetPk);
			mVector4.addElement(GameCanvas.gameScr.cmdSetDosat);
			GameCanvas.menu.startAt(mVector4, 2, T.chonco);
			break;
		}
		case 40:
			if (Player.isGetDataClan != 1)
			{
				GlobalService.gI().Clan_CMD(9, "", 0, 0);
				GameCanvas.Start_Waiting_DiaLog(T.pleaseWaiting, isCmdClose: true);
				Player.isGetDataClan = 0;
			}
			else if (player.clan != null)
			{
				if (GameCanvas.ClanScr == null)
				{
					GameCanvas.ClanScr = new Clan_Screen(player.clan);
				}
				GameCanvas.ClanScr.Show(this);
			}
			break;
		case 41:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Clan_CMD(10, "", objGiaotiep.ID, 0);
			}
			break;
		case 42:
			if (objGiaotiep != null && objGiaotiep.clan != null)
			{
				GlobalService.gI().Clan_CMD(11, "", objFocus.clan.ID, 0);
			}
			break;
		case 43:
			isShowNameSetting = !isShowNameSetting;
			break;
		case 44:
			GlobalService.gI().Paint_Client(0, (sbyte)subIndex);
			break;
		case 45:
			GlobalService.gI().Paint_Client(1, (sbyte)subIndex);
			break;
		case 46:
		{
			string text = "";
			try
			{
				text = GameMidlet.connectHTTP(GameMidlet.getThongBao());
			}
			catch (Exception)
			{
			}
			if (text != null && text.Length > 10)
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(text);
			}
			else
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.connectfail);
			}
			break;
		}
		case 47:
		{
			for (int m = 0; m < vecPlayers.size(); m++)
			{
				MainObject mainObject2 = (MainObject)vecPlayers.elementAt(m);
				if (mainObject2 != player)
				{
					ShowInfoOtherPlayer(mainObject2.name);
					break;
				}
			}
			break;
		}
		case 48:
		{
			if (Player.vecUniform.size() == 0)
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.noneUniform);
				break;
			}
			if (Player.vecUniform.size() == 1)
			{
				for (int k = 0; k < Player.vecUniform.size(); k++)
				{
					((Uniform)Player.vecUniform.elementAt(k)).getUniform();
				}
				break;
			}
			for (int l = 0; l < Player.vecUniform.size(); l++)
			{
				Uniform uniform = (Uniform)Player.vecUniform.elementAt(l);
				if (uniform.isSet == 0)
				{
					uniform.getUniform();
				}
				else
				{
					uniform.isSet = 0;
				}
			}
			break;
		}
		case 49:
		{
			mVector menuItems = ((LoadMap.specMap != 10) ? getVecQuickChatGame() : getVecQuickChatLOL());
			GameCanvas.menu.startAt(menuItems, 2, T.quickChat);
			break;
		}
		case 50:
		{
			sbyte b = 3;
			if (Player.ChucInCLan == 0)
			{
				b = 4;
			}
			GlobalService.gI().Quick_Chat_LoL(2, (sbyte)subIndex, b, player.name);
			InfoShowNotify o = new InfoShowNotify(player.name + ": " + T.mChatInLoL[subIndex], b);
			Interface_Game.vecQuickChatLoL.insertElementAt(o, 0);
			break;
		}
		case 51:
			if (subIndex >= 0 && subIndex < T.mChatInGame.Length)
			{
				player.strChatPopup = T.mChatInGame[subIndex];
				GlobalService.gI().chatPopup(T.mChatInGame[subIndex]);
			}
			break;
		case 52:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Giaotiep_FormServer(ReadMessenge.ID_GiaoTiep_Server, objGiaotiep.ID, objGiaotiep.typeObject);
			}
			break;
		case 53:
			if (objGiaotiep != null)
			{
				GlobalService.gI().chatPopup("ops lockchat " + objGiaotiep.name);
			}
			break;
		case 26:
		{
			mVector mVector5 = new mVector();
			mVector5.addElement(new iCommand(T.giaohuu, 54, 0, this));
			mVector5.addElement(new iCommand(T.thachdau, 54, 1, this));
			GameCanvas.menu.startAt(mVector5, 2, T.fight);
			break;
		}
		case 54:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Fight(0, objGiaotiep.ID, (sbyte)subIndex);
			}
			break;
		case 55:
			isShowSkillBuff = !isShowSkillBuff;
			GameCanvas.saveRms.SaveShowSkillBuff();
			break;
		case 56:
			if (subIndex >= 0 && subIndex < GameMidlet.google_productIds.Length)
			{
				GameMidlet.makePurchase(GameMidlet.google_productIds[subIndex]);
			}
			break;
		case 57:
		{
			MsgSpamSetup msgSpamSetup = new MsgSpamSetup();
			msgSpamSetup.setinfo();
			GameCanvas.Start_Sub_Dialog(msgSpamSetup);
			break;
		}
		case 58:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Giaotiep_FormServer(2, objGiaotiep.ID, objGiaotiep.typeObject);
			}
			break;
		case 59:
			GlobalService.gI().Send_Type(-91, 0);
			break;
		case 60:
			isShowSkillPlayer = !isShowSkillPlayer;
			break;
		case 61:
			isShowNhanVat = !isShowNhanVat;
			break;
		case 62:
			mSystem.outz("vot banh");
			if (player.tickNauBanh >= 63 && player.tickNauBanh <= 65)
			{
				GlobalService.gI().VotBanhChung(1, 1, 1);
			}
			else if (player.tickNauBanh >= 66 && player.tickNauBanh <= 87)
			{
				GlobalService.gI().VotBanhChung(1, 1, 2);
			}
			else
			{
				GlobalService.gI().VotBanhChung(1, 1, 0);
			}
			GameCanvas.gameScr.setNauBanh(isNauBanh: false);
			break;
		case 63:
			GameCanvas.SudoScr = new Sudo_Screen(player.sudo);
			break;
		case 64:
			if (objGiaotiep != null)
			{
				GlobalService.gI().Sudo_CMD(19, "", objGiaotiep.ID, 0);
			}
			break;
		case 65:
			DualTabScreen.gI().curMainTab = 4;
			DualTabScreen.gI().openPetSubView();
			DualTabScreen.gI().Show(this);
			break;
		case 99:
			if (isOnRepeatQuest)
			{
				AutoRepeatQuest.stopAuto();
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notifyOffAutoRepeatQuest);
			}
			else
			{
				iCommand cmd2 = new iCommand(T.confirmYes, 100, this);
				GameCanvas.Start_Normal_DiaLog(T.confirmRepeatQuest, cmd2, isCmdClose: true);
			}
			break;
		case 100:
			GameCanvas.end_Dialog();
			isOnRepeatQuest = true;
			if (AutoRepeatQuest.fBeginAuto())
			{
				AutoRepeatQuest.isStart = true;
			}
			break;
		case 101:
			if (isOnSuperBoss)
			{
				AutoSuperBoss.stopAuto();
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notifyOffAutoSuperBoss);
			}
			else
			{
				iCommand cmd = new iCommand(T.confirmYes, 102, this);
				GameCanvas.Start_Normal_DiaLog(T.confirmAutoSuperBoss, cmd, isCmdClose: true);
			}
			break;
		case 102:
			GameCanvas.end_Dialog();
			isOnSuperBoss = true;
			AutoSuperBoss.isStart = true;
			break;
		case 103:
		{
			iCommand iCommand2 = new iCommand(100 + T.percent, 104, this);
			iCommand iCommand3 = new iCommand(125 + T.percent, 105, this);
			iCommand iCommand4 = new iCommand(150 + T.percent, 106, this);
			if (GameCanvas.percent == 0)
			{
				iCommand4.isDisplay = false;
				iCommand4.isSaved = false;
				iCommand3.isDisplay = false;
				iCommand3.isSaved = false;
				iCommand2.isDisplay = true;
				iCommand2.isSaved = true;
			}
			else if (GameCanvas.percent == 25)
			{
				iCommand3.isDisplay = true;
				iCommand3.isSaved = true;
				iCommand4.isDisplay = false;
				iCommand4.isSaved = false;
				iCommand2.isDisplay = false;
				iCommand2.isSaved = false;
			}
			else if (GameCanvas.percent == 50)
			{
				iCommand4.isDisplay = true;
				iCommand4.isSaved = true;
				iCommand2.isDisplay = false;
				iCommand2.isSaved = false;
				iCommand3.isDisplay = false;
				iCommand3.isSaved = false;
			}
			mVector mVector2 = new mVector();
			mVector2.addElement(iCommand2);
			mVector2.addElement(iCommand3);
			mVector2.addElement(iCommand4);
			GameCanvas.Start_Normal_DiaLog(T.chooseSpeedupTitle, mVector2, isCmdClose: true);
			break;
		}
		case 104:
			GameCanvas.percent = 0;
			saveSpeedUpRMS(0);
			GameCanvas.end_Dialog();
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "100" + T.percent);
			break;
		case 105:
			GameCanvas.percent = 25;
			saveSpeedUpRMS(25);
			GameCanvas.end_Dialog();
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "125" + T.percent);
			break;
		case 106:
			GameCanvas.percent = 50;
			saveSpeedUpRMS(50);
			GameCanvas.end_Dialog();
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "150" + T.percent);
			break;
		case 107:
			if (isOnAutoPB)
			{
				AutoBattlefield.StopAutoBattlefield();
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notifyOffAutoPB);
			}
			else
			{
				AutoBattlefield.CheckMapAutoBattlefield();
			}
			break;
		// ======== MOD MENU CASES (port từ J2ME) ========
		case 108: // Mở menu tàn sát
			AThMadaraMOD.openSlaughterMenu();
			break;
		case 109: // Thông báo tự động tàn sát
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tự động tàn sát 109");
			break;
		case 110: // Giữ level
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Giữ level 110");
			break;
		case 111: // Bật/tắt đóng băng quái
			player.isDongBangQuai111 = !player.isDongBangQuai111;
			Interface_Game.addInfoPlayerNormal(
				player.isDongBangQuai111 ? "Bật đóng băng quái" : "Tắt đóng băng quái",
				mFont.tahoma_7_yellow);
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 112: // Bật/tắt gom quái
			player.isGomQuai112 = !player.isGomQuai112;
			Interface_Game.addInfoPlayerNormal(
				player.isGomQuai112 ? "Bật gom quái" : "Tắt gom quái",
				mFont.tahoma_7_yellow);
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 113: // Cài đặt auto skill
		{
			MsgAutoFire autoFireDlg = new MsgAutoFire();
			autoFireDlg.setinfoAuto_Fire();
			GameCanvas.Start_Current_Dialog(autoFireDlg);
			break;
		}
		case 114: // Mở menu chọn tốc độ game (1-10)
		{
			int curSpd = AThMadaraMOD.gameSpeed;
			mVector speedMenu = new mVector();
			for (int i = 1; i <= 10; i++)
			{
				speedMenu.addElement(new iCommand((curSpd == i ? "* " : "  ") + "x" + i, 131, i, this));
			}
			speedMenu.addElement(new iCommand("< Quay lại", 343, 0, this));
			GameCanvas.menu.startAt(speedMenu, 2, "Tốc độ game");
			break;
		}
		case 118: // Bật/tắt tự hồi sinh (5 chế độ: Tắt, Beri, Ruby, Tất cả, Về làng)
			AThMadaraMOD.autoReviveMode = (AThMadaraMOD.autoReviveMode + 1) % 5;
			Player.AutoRevice = (sbyte)(AThMadaraMOD.autoReviveMode != 0 ? 1 : 0);
			Interface_Game.addInfoPlayerNormal(
				"Tự hồi sinh: " + AThMadaraMOD.getAutoReviveModeName(),
				mFont.tahoma_7_yellow);
			AThMadaraMOD.getInstance().openMenuAuto();
			break;
		case 119: // Mở menu lọc đồ tự bán
			AThMadaraMOD.getInstance().openMenuSellItem();
			break;
		case 120: // Mở menu chọn FPS
		{
			int cur = MotherCanvas.targetFPS;
			mVector fpsMenu = new mVector();
			fpsMenu.addElement(new iCommand((cur == 30 ? "* " : "  ") + "30 fps", 123, 0, this));
			fpsMenu.addElement(new iCommand((cur == 45 ? "* " : "  ") + "45 fps", 124, 0, this));
			fpsMenu.addElement(new iCommand((cur == 60 ? "* " : "  ") + "60 fps", 121, 0, this));
			fpsMenu.addElement(new iCommand((cur == 90 ? "* " : "  ") + "90 fps", 132, 0, this));
			fpsMenu.addElement(new iCommand((cur == 120 ? "* " : "  ") + "120 fps", 133, 0, this));
			fpsMenu.addElement(new iCommand("< Quay lại", 343, 0, this));
			GameCanvas.menu.startAt(fpsMenu, 2, "Chọn FPS");
			break;
		}
		case 121: // Set 60 FPS
			MotherCanvas.setFPS(60);
			Interface_Game.addInfoPlayerNormal("FPS: 60", mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		case 123: // Set 30 FPS
			MotherCanvas.setFPS(30);
			Interface_Game.addInfoPlayerNormal("FPS: 30", mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		case 124: // Set 45 FPS
			MotherCanvas.setFPS(45);
			Interface_Game.addInfoPlayerNormal("FPS: 45", mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		case 125: // Reset movement
			player.forceStopMovement();
			Interface_Game.addInfoPlayerNormal("Reset movement OK", mFont.tahoma_7_yellow);
			break;
		case 128: // Tự về map cũ
			AThMadaraMOD.isAutoReMap = !AThMadaraMOD.isAutoReMap;
			Interface_Game.addInfoPlayerNormal(
				AThMadaraMOD.isAutoReMap ? "Bật tự về map cũ" : "Tắt tự về map cũ",
				mFont.tahoma_7_yellow);
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 129: // Né PK
			AThMadaraMOD.isAvoidPK = !AThMadaraMOD.isAvoidPK;
			Interface_Game.addInfoPlayerNormal(
				AThMadaraMOD.isAvoidPK ? "Bật né PK" : "Tắt né PK",
				mFont.tahoma_7_yellow);
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 130: // Né Boss
			AThMadaraMOD.isAvoidBoss = !AThMadaraMOD.isAvoidBoss;
			Interface_Game.addInfoPlayerNormal(
				AThMadaraMOD.isAvoidBoss ? "Bật né Boss" : "Tắt né Boss",
				mFont.tahoma_7_yellow);
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 131: // Thiết lập game speed
		{
			int speed = subIndex;
			AThMadaraMOD.gameSpeed = speed;
			AThMadaraMOD.saveSpeedSetting(speed);
			Interface_Game.addInfoPlayerNormal("Tốc độ: x" + speed, mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		}
		case 132: // Set 90 FPS
			MotherCanvas.setFPS(90);
			Interface_Game.addInfoPlayerNormal("FPS: 90", mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		case 133: // Set 120 FPS
			MotherCanvas.setFPS(120);
			Interface_Game.addInfoPlayerNormal("FPS: 120", mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		case 134: // Mở Mod Skill
			AThMadaraMOD.openModSkillMenu();
			break;
		case 135: // Mở Mod Skill
			AThMadaraMOD.openModSkillMenu();
			break;
		case 136: // Toggle Keypad / Touch mode
			cmdChangeTouch.perform();
			AThMadaraMOD.openSystemMenu();
			break;
		case 310: // Toggle Bật/Tắt Mod Skill
			AThMadaraMOD.isModSkill = !AThMadaraMOD.isModSkill;
			AThMadaraMOD.saveCustomSkillEffSettings();
			Interface_Game.addInfoPlayerNormal("Mod Skill: " + (AThMadaraMOD.isModSkill ? "Bật" : "Tắt"), mFont.tahoma_7_yellow);
			AThMadaraMOD.openModSkillMenu();
			break;
		case 311: // Chọn slot để Mod
			AThMadaraMOD.openModSkillChooseEffMenu(subIndex);
			break;
		case 312: // Đã chọn Eff ID preset
			AThMadaraMOD.setCustomSkillEff(AThMadaraMOD.selectedSlotToMod, (short)subIndex);
			break;
		case 313: // Mở menu Trái Ác Quỷ & Haki
			AThMadaraMOD.openDevilFruitSkillsMenu(subIndex);
			break;
		case 314: // Mở menu Skill Chuẩn Server
			AThMadaraMOD.openStandardSkillTemplatesMenu(subIndex);
			break;
		case 315: // Mở InputDialog nhập Eff ID tùy chỉnh
			AThMadaraMOD.selectedSlotToMod = subIndex;
			AThMadaraMOD.inputDialogCustomEff = new InputDialog();
			iCommand cmdOkCustomEff = new iCommand("OK", 319, this);
			AThMadaraMOD.inputDialogCustomEff.setinfo("Nhập Mã Eff ID (1-9999):", cmdOkCustomEff, true, "1-9999");
			GameCanvas.currentDialog = AThMadaraMOD.inputDialogCustomEff;
			break;
		case 316: // Khôi phục slot hiện tại về mặc định
			AThMadaraMOD.resetSlotCustomSkillEff(subIndex);
			break;
		case 317: // Khôi phục tất cả slot về mặc định
			AThMadaraMOD.resetAllCustomSkillEff();
			AThMadaraMOD.openModSkillMenu();
			break;
		case 318: // Mở chọn slot Mod Skill
			AThMadaraMOD.openModSkillSelectSlotMenu();
			break;
		case 319: // Xác nhận nhập Eff ID tùy chỉnh
			if (AThMadaraMOD.inputDialogCustomEff != null && AThMadaraMOD.inputDialogCustomEff.tfInput != null)
			{
				try
				{
					string strVal = AThMadaraMOD.inputDialogCustomEff.tfInput.getText().Trim();
					if (strVal.Length > 0)
					{
						short effVal = short.Parse(strVal);
						AThMadaraMOD.setCustomSkillEff(AThMadaraMOD.selectedSlotToMod, effVal);
					}
				}
				catch (Exception)
				{
					Interface_Game.addInfoPlayerNormal("Eff ID không hợp lệ!", mFont.tahoma_7_yellow);
				}
			}
			GameCanvas.end_Dialog();
			break;
		case 341: // Mở menu Tiện ích chiến đấu
			AThMadaraMOD.openCombatUtilitiesMenu();
			break;
		case 342: // Mở menu Hoạt động & Nhiệm vụ
			AThMadaraMOD.openActivitiesMenu();
			break;
		case 343: // Mở menu Hệ thống & Tốc độ
			AThMadaraMOD.openSystemMenu();
			break;
		case 300: // Tàn sát / Tự đánh tất cả
			AThMadaraMOD.isSlaughterActive = true;
			AThMadaraMOD.slaughterTargetName = null;
			AThMadaraMOD.slaughterTargetLevel = -1;
			Interface_Game.isAutoFireInterface = true;
			Player.AutoFireCur = 1;
			player.isAutoFireNew108 = true;
			Interface_Game.addInfoPlayerNormal("Tự Đánh/Tàn Sát: Tất cả (" + AThMadaraMOD.getCombatMoveModeName() + ")", mFont.tahoma_7_yellow);
			AThMadaraMOD.openAutoCombatMenu();
			break;
		case 301: // Tàn sát một loại quái
			if (AThMadaraMOD.tempUniqueMonsters != null && subIndex >= 0 && subIndex < AThMadaraMOD.tempUniqueMonsters.size())
			{
				MainObject m = (MainObject)AThMadaraMOD.tempUniqueMonsters.elementAt(subIndex);
				if (m != null)
				{
					AThMadaraMOD.isSlaughterActive = true;
					AThMadaraMOD.slaughterTargetName = m.name;
					AThMadaraMOD.slaughterTargetLevel = m.Lv;
					Interface_Game.isAutoFireInterface = true;
					Player.AutoFireCur = 1;
					player.isAutoFireNew108 = true;
					Interface_Game.addInfoPlayerNormal("Tự Đánh/Tàn Sát: " + m.name + " (" + AThMadaraMOD.getCombatMoveModeName() + ")", mFont.tahoma_7_yellow);
					AThMadaraMOD.openAutoCombatMenu();
				}
			}
			break;
		case 302: // Tắt tàn sát / tự đánh
			AThMadaraMOD.isSlaughterActive = false;
			AThMadaraMOD.slaughterTargetName = null;
			AThMadaraMOD.slaughterTargetLevel = -1;
			Interface_Game.isAutoFireInterface = false;
			Player.AutoFireCur = 0;
			if (player != null)
			{
				player.isAutoFireNew108 = false;
				player.posTransRoad = null;
			}
			Interface_Game.addInfoPlayerNormal("Tắt Tự Đánh / Tàn Sát", mFont.tahoma_7_yellow);
			AThMadaraMOD.getInstance().openMenuAuto();
			break;
		case 303: // Chuyển đổi kiểu di chuyển (Dịch chuyển / Đi bộ)
			AThMadaraMOD.autoCombatMoveMode = (AThMadaraMOD.autoCombatMoveMode == 0 ? 1 : 0);
			AThMadaraMOD.saveCombatSettings();
			Interface_Game.addInfoPlayerNormal("Kiểu tiếp cận: " + AThMadaraMOD.getCombatMoveModeName(), mFont.tahoma_7_yellow);
			AThMadaraMOD.openAutoCombatMenu();
			break;
		case 340: // Quay lại menu MOD
			AThMadaraMOD.getInstance().openMenuAuto();
			break;
		case 350: // Mở menu Tự Đánh / Tàn Sát
			AThMadaraMOD.openAutoCombatMenu();
			break;
		case 351: // Mở menu Đồ họa & Tối ưu
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 352: // Hiển thị Skill
			isShowSkillPlayer = !isShowSkillPlayer;
			AThMadaraMOD.isShowSkillPlayer = isShowSkillPlayer;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 354: // Bóng nhân vật
			AThMadaraMOD.isShowShadows = !AThMadaraMOD.isShowShadows;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 355: // Hiện ID Quái/Boss
			AThMadaraMOD.isShowMobID = !AThMadaraMOD.isShowMobID;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 356: // Hiện ID Người chơi
			AThMadaraMOD.isShowCharID = !AThMadaraMOD.isShowCharID;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 357: // Người chơi khác
			AThMadaraMOD.isShowOtherPlayers = !AThMadaraMOD.isShowOtherPlayers;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 358: // Thời tiết Map
			AThMadaraMOD.isShowWeather = !AThMadaraMOD.isShowWeather;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 359: // Pet / Thú cưng
			AThMadaraMOD.isShowPet = !AThMadaraMOD.isShowPet;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 360: // Preset Tối Ưu Max
			AThMadaraMOD.setPresetMaxOptimization();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 361: // Preset Khôi Phục Mặc Định
			AThMadaraMOD.setPresetResetGraphics();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 367: // Hiển thị Thông thạo
			AThMadaraMOD.isShowMasteryEffect = !AThMadaraMOD.isShowMasteryEffect;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 368: // Hiệu ứng Top / Kích ẩn
			AThMadaraMOD.isShowTopTitle = !AThMadaraMOD.isShowTopTitle;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 369: // Tên Người chơi
			AThMadaraMOD.isShowPlayerNames = !AThMadaraMOD.isShowPlayerNames;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 370: // Tên Quái / Boss
			AThMadaraMOD.isShowMonsterNames = !AThMadaraMOD.isShowMonsterNames;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 371: // Hút vật phẩm
			AThMadaraMOD.isHutItem = !AThMadaraMOD.isHutItem;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 372: // Auto Thi Đấu Biển (RedLine)
			AThMadaraMOD.isAutoRedLine = !AThMadaraMOD.isAutoRedLine;
			AThMadaraMOD.saveAutoRedLineSettings();
			Interface_Game.addInfoPlayerNormal("Auto Thi Dau Bien: " + (AThMadaraMOD.isAutoRedLine ? "Bat" : "Tat"), mFont.tahoma_7_yellow);
			AThMadaraMOD.getInstance().openMenuAuto();
			break;
		case 373: // Hiệu ứng Map nền
			AThMadaraMOD.isShowMapEffect = !AThMadaraMOD.isShowMapEffect;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 374: // Giảm Particle / Hiệu ứng
			AThMadaraMOD.isReduceParticle = !AThMadaraMOD.isReduceParticle;
			AThMadaraMOD.saveGraphicsSettings();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 375: // Preset Cân Bằng
			AThMadaraMOD.setPresetBalanced();
			AThMadaraMOD.openGraphicsMenu();
			break;
		case 376: // Auto Reconnect
			AThMadaraMOD.isAutoReconnect2 = !AThMadaraMOD.isAutoReconnect2;
			AThMadaraMOD.saveAutoRedLineSettings();
			Interface_Game.addInfoPlayerNormal("Auto Reconnect: " + (AThMadaraMOD.isAutoReconnect2 ? "Bat" : "Tat"), mFont.tahoma_7_yellow);
			AThMadaraMOD.openSystemMenu();
			break;
		}
	}

	public void saveSpeedUpRMS(int percent)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try
		{
			dataOutputStream.writeInt(percent);
			GameMidlet.saveRMS(Environment.MachineName + T.speedUp, byteArrayOutputStream.toByteArray());
			dataOutputStream.close();
		}
		catch (Exception)
		{
		}
	}

	public void Open_Nap_Store()
	{
		mVector mVector2 = new mVector();
		for (int i = 0; i < GameMidlet.google_listGems.Length; i++)
		{
			iCommand iCommand2 = new iCommand(GameMidlet.google_listGems[i], 56, i, this);
			iCommand2.setFraCaption(AvMain.fraMoney, 1, 7, 2);
			mVector2.addElement(iCommand2);
		}
		GameCanvas.menu.startAt(mVector2, 2, T.napthe);
	}

	public void Open_Dialog_Nap()
	{
		if (GameCanvas.language == 1)
		{
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.danglam);
			return;
		}
		input = GameCanvas.Start_Input_Dialog_Server(T.mbuygem, T.napthe, cmdSendSerial_Mathe);
		GameCanvas.Start_Current_Dialog(input);
	}

	private mVector getVecQuickChatLOL()
	{
		mVector mVector2 = new mVector();
		for (int i = 0; i < T.mChatInLoL.Length; i++)
		{
			iCommand o = new iCommand(T.mChatInLoL[i], 50, i, this);
			mVector2.addElement(o);
		}
		return mVector2;
	}

	private mVector getVecQuickChatGame()
	{
		mVector mVector2 = new mVector();
		for (int i = 0; i < T.mChatInGame.Length; i++)
		{
			iCommand o = new iCommand(T.mChatInGame[i], 51, i, this);
			mVector2.addElement(o);
		}
		return mVector2;
	}

	private mVector getMenuGameNew()
	{
		mVector mVector2 = new mVector();
		cmdGetItem = new iCommand(T.on + T.autoGetItem, 25, this);
		if (Player.isGetItem)
		{
			cmdGetItem.caption = T.off + T.autoGetItem;
			cmdGetItem.isDonotCloseMenu = true;
		}
		cmdMPHP = new iCommand(T.on + T.autoMPHP, 18, this);
		if (Player.isMPHP)
		{
			cmdMPHP.caption = T.off + T.autoMPHP;
			cmdMPHP.isDonotCloseMenu = true;
		}
		if (isShowNameSetting)
		{
			cmdShowName.caption = T.off + T.showname;
		}
		else
		{
			cmdShowName.caption = T.on + T.showname;
		}
		iCommand o = new iCommand(T.autoFire, 19, this);
		iCommand o2 = new iCommand(isOnRepeatQuest ? (T.off + T.autoRepeatQuest) : (T.on + T.autoRepeatQuest), 99, this);
		mVector2.addElement(o2);
		iCommand o3 = new iCommand(isOnSuperBoss ? (T.off + T.autoSuperBoss) : (T.on + T.autoSuperBoss), 101, this);
		mVector2.addElement(o3);
		iCommand o4 = new iCommand(isOnAutoPB ? (T.off + T.titleAutoPB) : (T.on + T.titleAutoPB), 107, this);
		mVector2.addElement(o4);
		iCommand o5 = new iCommand(T.chooseSpeedUpMenu, 103, this);
		mVector2.addElement(o5);
		UITheme curTheme = UIThemeManager.getCurrentTheme();
		string curThemeName = (curTheme != null) ? curTheme.displayName : "Gốc";
		mVector2.addElement(new iCommand("🎨 Giao diện: " + curThemeName, 1250, this));
		mVector2.addElement(o);
		mVector2.addElement(cmdMPHP);
		mVector2.addElement(cmdGetItem);
		mVector2.addElement(cmdSpam);
		mVector2.addElement(cmdShowName);
		cmdDonotShowHat = new iCommand(T.off + T.showHat, 44, 1, this);
		if (player.isDonotShowHat != 0)
		{
			cmdDonotShowHat = new iCommand(T.on + T.showHat, 44, 0, this);
		}
		if (Player.idFashion >= 0)
		{
			cmdDonotShowWeaponF = new iCommand(T.off + T.showWeaponF, 45, 1, this);
			if (player.isDonotShowWeaponF != 0)
			{
				cmdDonotShowWeaponF = new iCommand(T.on + T.showWeaponF, 45, 0, this);
			}
			mVector2.addElement(cmdDonotShowWeaponF);
		}
		mVector2.addElement(cmdDonotShowHat);
		if (isShowSkillBuff)
		{
			cmdShowSkillBuff.caption = T.off + T.showSkillBuff;
		}
		else
		{
			cmdShowSkillBuff.caption = T.on + T.showSkillBuff;
		}
		mVector2.addElement(cmdShowSkillBuff);
		if (isShowSkillPlayer)
		{
			cmdShowSkillPlayer.caption = T.off + T.showSkillPlayer;
		}
		else
		{
			cmdShowSkillPlayer.caption = T.on + T.showSkillPlayer;
		}
		mVector2.addElement(cmdShowSkillPlayer);
		if (isShowNhanVat)
		{
			cmdShowNhanVat.caption = T.off + T.showNhanVat;
		}
		else
		{
			cmdShowNhanVat.caption = T.on + T.showNhanVat;
		}
		mVector2.addElement(cmdShowNhanVat);
		if (GameMidlet.DEVICE != 0)
		{
			mVector2.addElement(LoginScreen.cmdSound);
		}
		return mVector2;
	}

	private void setCaptionCmdAutoGetItem()
	{
		GameCanvas.menuCur.updateMenuGame(getMenuGameNew());
	}

	public void openSelectThemeMenu()
	{
		mVector vec = new mVector();
		string[] names = UIThemeManager.getThemeNames();
		int curTheme = UIThemeManager.getCurrentThemeId();
		for (int i = 0; i < names.Length; i++)
		{
			iCommand cmd = new iCommand(names[i], 1251 + i, this);
			vec.addElement(cmd);
		}
		GameCanvas.menu.startAt(vec, 2, "Chọn Giao Diện");
		if (curTheme >= 0 && curTheme < names.Length)
		{
			GameCanvas.menu.menuSelectedItem = curTheme;
		}
	}

	private void doMenuSettingView()
	{
		mVector mVector2 = new mVector();
		iCommand o = new iCommand(T.free, 32, this);
		mVector2.addElement(o);
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (mainObject.indexTeam != 0)
			{
				iCommand o2 = new iCommand(T.theo + mainObject.name, 31, mainObject.ID, this);
				mVector2.addElement(o2);
			}
		}
		GameCanvas.menu.startAt(mVector2, 2, T.camera);
	}

	public override void commandMenu(int index, int subIndex)
	{
	}

	public mVector getMenu()
	{
		player.resetAction();
		mVector mVector2 = new mVector();
		mVector2.addElement(cmdFriendList);
		mVector2.addElement(cmdBlackList);
		mVector2.addElement(cmdUniform);
		mVector2.addElement(cmdQuickChat);
		mVector2.addElement(cmdSudo);
		mVector2.addElement(cmdPet);
		mVector2.addElement(cmdDauGia);
		mVector2.addElement(cmdEvent);
		mVector2.addElement(cmdMenuPk);
		if (Player.vecParty.size() > 0)
		{
			mVector2.addElement(cmdParty);
		}
		if (player.clan != null)
		{
			mVector2.addElement(cmdClan);
		}
		mVector2.addElement(cmdTrochuyen);
		mVector2.addElement(cmdAuto);
		mVector2.addElement(cmdShowWC);
		mVector2.addElement(cmdLogOut);
		return mVector2;
	}

	public void setRevice()
	{
		mVector mVector2 = new mVector();
		mVector2.addElement(new iCommand(T.revice, 2, 1, this));
		mVector2.addElement(cmdVeLang);
		GameCanvas.Start_Normal_DiaLog(T.hoiRevice, mVector2, isCmdClose: true);
	}

	public override void paint(mGraphics g)
	{
		dx = 0;
		dy = 0;
		if (LoadMap.timeVibrateScreen > 0)
		{
			if (LoadMap.timeVibrateScreen > 100)
			{
				dy = CRes.random_Am_0(3);
				if (LoadMap.timeVibrateScreen == 101)
				{
					LoadMap.timeVibrateScreen = 0;
				}
			}
			else
			{
				dy = CRes.random_Am_0(3);
				dx = CRes.random_Am(0, 2);
			}
			LoadMap.timeVibrateScreen--;
		}
		g.translate(dx, dy);
		g.translate(-MainScreen.cameraMain.xCam, -MainScreen.cameraMain.yCam);
		if (typePaintGameScreen == 0)
		{
			if (LoadMap.isOnlineMap)
			{
				paintNormal(g);
			}
			else
			{
				paintMapOffLine(g);
			}
		}
		else
		{
			paintSpec(g);
		}
		interfaceGame.paintShowtime(g);
	}

	public static void paintMapOffLine(mGraphics g)
	{
		if (LoadMap.specMap == 5)
		{
			MapOff_RedLine.paintRedLine(g);
		}
		if (LoadMap.specMap == 8)
		{
			MapGotoSky.paint(g);
		}
		if (LoadMap.specMap == 12)
		{
			MapGotoGod.paint(g);
		}
	}

	private void paintSpec(mGraphics g)
	{
		paintRectSpec(g);
		for (int i = 0; i < VecEffect.size(); i++)
		{
			MainEffect mainEffect = (MainEffect)VecEffect.elementAt(i);
			if (mainEffect != null && mainEffect.objMainEff != null && mainEffect.objMainEff == player && mainEffect.levelPaint == -1 && !mainEffect.isRemove && !mainEffect.isStop)
			{
				mainEffect.paint(g);
			}
		}
		CRes.quickSort(vecPlayers);
		for (int j = 0; j < vecPlayers.size(); j++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(j);
			if (!mainObject.isRemove && !mainObject.isStop && (mainObject.isPaintSpec || mainObject == player))
			{
				mainObject.paint(g);
			}
		}
		for (int k = 0; k < VecEffect.size(); k++)
		{
			MainEffect mainEffect2 = (MainEffect)VecEffect.elementAt(k);
			if (mainEffect2 != null && mainEffect2.objMainEff != null && mainEffect2.objMainEff == player && mainEffect2.levelPaint >= 0 && !mainEffect2.isRemove && !mainEffect2.isStop)
			{
				mainEffect2.paint(g);
			}
		}
	}

	public void paintRectSpec(mGraphics g)
	{
		g.setColor(3278096);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
		g.setColor(16079230);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - size, MotherCanvas.w, size * 2);
		g.setColor(16086933);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - size / 2, MotherCanvas.w, size);
		g.setColor(13242941);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - size - 8, MotherCanvas.w, 8);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 + size, MotherCanvas.w, 8);
		g.setColor(11538486);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - size - 12, MotherCanvas.w, 4);
		g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 + size + 8, MotherCanvas.w, 4);
		g.setColor(15963050);
		for (int i = MotherCanvas.h / 4 * 3 - size - 12; i < MotherCanvas.h / 4 * 3 + size + 12; i += 12)
		{
			g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + i, MotherCanvas.w, 1);
		}
		for (int j = 0; j < vecEffSkillSpec.size(); j++)
		{
			Point point = (Point)vecEffSkillSpec.elementAt(j);
			if (point.dis <= 1)
			{
				g.setColor(point.color);
				g.fillRect(MainScreen.cameraMain.xCam + point.x - point.w / 2, MainScreen.cameraMain.yCam + point.y - point.h / 2, point.w, point.h);
			}
		}
	}

	public void paintNormal(mGraphics g)
	{
		if (GameCanvas.mapBack != null)
		{
			GameCanvas.mapBack.paint(g);
		}
		for (int i = 0; i < vecBigBossLittleGraden.size(); i++)
		{
			((BigBossLittleGraden)vecBigBossLittleGraden.elementAt(i)).paint(g, MainScreen.cameraMain.xCam);
		}
		paintTree(g, 0);
		paintTree(g, 1);
		GameCanvas.loadmap.paint(g);
		if (isFullScreen)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int sW = (GameCanvas.w > 0) ? GameCanvas.w : MotherCanvas.w;
			int sH = (GameCanvas.h > 0) ? GameCanvas.h : MotherCanvas.h;
			float baseAlpha = (fullScreenAlpha > 0f) ? (fullScreenAlpha * 0.45f) : 0.35f;
			g.setColor(colorRec, baseAlpha);
			g.fillRect(camX - 60, camY - 60, sW + 120, sH + 120, isA: false);
		}
		else if (wRec > 0)
		{
			g.fillRecAlpla(xRec, yRec, wRec, hRec, colorRec);
		}
		if (!isFullScreen && GameCanvas.mapBack != null)
		{
			GameCanvas.mapBack.paintLast(g);
		}
		if (!isFullScreen && effSea != null)
		{
			effSea.paintSea(g);
		}
		for (int j = 0; j < vecEffOnMap.size(); j++)
		{
			Point point = (Point)vecEffOnMap.elementAt(j);
			if (point.fSmall == 0)
			{
				AvMain.fraImgEffOnMap0.drawFrame(point.subType * 3 + point.frame, point.x, point.y, point.dis, 3, g);
			}
		}
		Interface_Game.paintMoveTo(g);
		if (LoadMap.specMap == 10 && !GameCanvas.lowGraphic)
		{
			for (int k = 0; k < vecPlayers.size(); k++)
			{
				MainObject mainObject = (MainObject)vecPlayers.elementAt(k);
				if (mainObject.isTru() && !mainObject.isDie)
				{
					if (Interface_Game.imgRankSkill == null)
					{
						Interface_Game.imgRankSkill = mImage.createImage("/interface/lol11.png");
						Interface_Game.imgRankSkill.setWH();
					}
					g.drawRegion(Interface_Game.imgRankSkill, 0, 0, Interface_Game.imgRankSkill.w, Interface_Game.imgRankSkill.h, 0, mainObject.xAnchor - Interface_Game.imgRankSkill.w / 2, mainObject.yAnchor, 3);
					g.drawRegion(Interface_Game.imgRankSkill, 0, 0, Interface_Game.imgRankSkill.w, Interface_Game.imgRankSkill.h, 2, mainObject.xAnchor + Interface_Game.imgRankSkill.w / 2, mainObject.yAnchor, 3);
				}
			}
		}
		paintTree(g, 2);
		if (isShowSkillPlayer)
		{
			for (int l = 0; l < VecEffect.size(); l++)
			{
				MainEffect mainEffect = (MainEffect)VecEffect.elementAt(l);
				if (mainEffect != null && (isShowSkillPlayer || (mainEffect.objFireMain != null && mainEffect.objFireMain.ID == player.ID)) && mainEffect.levelPaint == -1 && !mainEffect.isRemove && !mainEffect.isStop)
				{
					mainEffect.paint(g);
				}
			}
			for (int m = 0; m < vecHighDataEff.size(); m++)
			{
				((DataSkillEff)vecHighDataEff.elementAt(m))?.paintBottomEff(g);
			}
		}
		CRes.quickSort(vecPlayers);
		nump = 0;
		numo = 0;
		maxob.y = 10000;
		maxtr.y = 10000;
		while (nump < vecPlayers.size() || numo < LoadMap.mItemMap[3].size())
		{
			try
			{
				ob = maxob;
				tr = maxtr;
				if (nump < vecPlayers.size())
				{
					ob = (MainObject)vecPlayers.elementAt(nump);
				}
				if (numo < LoadMap.mItemMap[3].size())
				{
					tr = (MainItemMap)LoadMap.mItemMap[3].elementAt(numo);
				}
				if (tr == null || (tr.TypeItem != 1 && ob.y + ob.ysai < tr.y + LoadMap.wTile) || (tr.TypeItem == 1 && ob.y + ob.ysai < tr.y))
				{
					nump++;
					if (tr == null)
					{
						numo++;
					}
					if (ob == null || ob.isRemove || ob.isStop)
					{
						// Bỏ qua object bị đánh dấu xóa hoặc dừng
					}
					else if (checkPaintLow(ob))
					{
						ob.paintOnlyShadown(g);
					}
					else if (checkPaintOB(ob))
					{
						if (!AThMadaraMOD.isShowOtherPlayers && ob != null && ob.typeObject == 0 && ob.ID != player.ID)
						{
							// Bỏ qua người chơi khác khi tắt
						}
						else if (!AThMadaraMOD.isShowPet && ob != null && (ob.typeObject == 10 || ob is Pet))
						{
							// Bỏ qua pet / thú cưng khi tắt
						}
						else if (disLowPaintOnlyShadow(ob))
						{
							ob.paintOnlyShadown(g);
						}
						else if (ob.ID != player.ID && ob.typeObject == 0 && !isShowNhanVat)
						{
							ob.paintHideAvatar(g);
						}
						else
						{
							ob.paint(g);
						}
					}
				}
				else
				{
					numo++;
					if (tr.isInScreen())
					{
						tr.paint(g);
					}
				}
			}
			catch (Exception)
			{
			}
		}
		if (effMap != null && AThMadaraMOD.isShowWeather)
		{
			effMap.paintLast(g);
		}
		for (int n = 0; n < LoadMap.vecPointChange.size(); n++)
		{
			Point point2 = (Point)LoadMap.vecPointChange.elementAt(n);
			g.drawRegion(AvMain.imgSelect, 0, 0, 12, 16, LoadMap.mTranPointChangeMap[point2.dis], point2.x + GameCanvas.gameTick % 6 * point2.vx, point2.y + GameCanvas.gameTick % 6 * point2.vy, 3);
			AvMain.Font3dWhite(g, point2.name, point2.x2 + GameCanvas.gameTick % 6 * point2.vx, point2.y2 + GameCanvas.gameTick % 6 * point2.vy, point2.f);
		}
		if (isFullScreen)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int sW = (GameCanvas.w > 0) ? GameCanvas.w : MotherCanvas.w;
			int sH = (GameCanvas.h > 0) ? GameCanvas.h : MotherCanvas.h;
			float alpha = (fullScreenAlpha > 0f) ? fullScreenAlpha : 0.80f;
			g.setColor(colorRec, alpha);
			g.fillRect(camX - 60, camY - 60, sW + 120, sH + 120, isA: false);
		}
		if (isShowSkillPlayer)
		{
			for (int num = 0; num < VecEffect.size(); num++)
			{
				MainEffect mainEffect2 = (MainEffect)VecEffect.elementAt(num);
				if (mainEffect2 != null && (isShowSkillPlayer || (mainEffect2.objFireMain != null && mainEffect2.objFireMain.ID == player.ID)) && mainEffect2.levelPaint == 0 && !mainEffect2.isRemove && !mainEffect2.isStop)
				{
					mainEffect2.paint(g);
				}
			}
			for (int num2 = 0; num2 < vecHighDataEff.size(); num2++)
			{
				((DataSkillEff)vecHighDataEff.elementAt(num2))?.paintTopEff(g);
			}
			for (int num3 = 0; num3 < VecNum.size(); num3++)
			{
				MainEffect mainEffect3 = (MainEffect)VecNum.elementAt(num3);
				if (mainEffect3 != null && mainEffect3.levelPaint == 0 && !mainEffect3.isRemove && !mainEffect3.isStop)
				{
					mainEffect3.paint(g);
				}
			}
			for (int num4 = 0; num4 < VecNum.size(); num4++)
			{
				MainEffect mainEffect4 = (MainEffect)VecNum.elementAt(num4);
				if (mainEffect4 != null && mainEffect4.levelPaint == 1 && !mainEffect4.isRemove && !mainEffect4.isStop)
				{
					mainEffect4.paint(g);
				}
			}
		}
		if (GameCanvas.showDialog != null)
		{
			GameCanvas.showDialog.paint(g);
		}
		paintTree(g, 4);
		paintTree(g, 5);
		if (isShowSkillPlayer)
		{
			for (int num5 = 0; num5 < VecEffect.size(); num5++)
			{
				MainEffect mainEffect5 = (MainEffect)VecEffect.elementAt(num5);
				if (mainEffect5 != null && (isShowSkillPlayer || (mainEffect5.objFireMain != null && mainEffect5.objFireMain.ID == player.ID)) && mainEffect5.levelPaint == 1 && !mainEffect5.isRemove && !mainEffect5.isStop)
				{
					mainEffect5.paint(g);
				}
			}
		}
		if (!getIsOffAdmin(1) && objFocus != null && typeViewPlayer == 0)
		{
			if (objFocus.typeObject == 0 || objFocus.typeObject == 2)
			{
				objFocus.paintName(g, objFocus.colorName, 1);
			}
			if (objFocus.Action != 4 && (objFocus.typeObject == 0 || objFocus.typeObject == 1))
			{
				objFocus.paintHPFocus(g);
			}
		}
		if (!getIsOffAdmin(2))
		{
			for (int num6 = 0; num6 < vecPlayers.size(); num6++)
			{
				MainObject mainObject2 = (MainObject)vecPlayers.elementAt(num6);
				if (mainObject2 == player && Player.isGhost)
				{
					Player.paintGhost(g, player.x, player.y - player.hOne);
				}
				else if (mainObject2.chat != null)
				{
					mainObject2.chat.paint(g);
				}
			}
		}
		Interface_Game.paintShowHelp(g, isInmap: true);
		if (GameCanvas.currentScreen != this)
		{
			return;
		}
		if (typeViewPlayer == 0)
		{
			if (LoadMap.specMap != 3 && !getIsOffAdmin(1))
			{
				Interface_Game.paintIconFocus(g);
			}
			GameCanvas.resetTrans(g);
			if (GameCanvas.currentScreen == GameCanvas.gameScr && LoadMap.specMap != 3 && !getIsOffAdmin(1))
			{
				if (!GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && GameCanvas.subDialog == null)
				{
					Interface_Game.paintNumMess(g, 0, 0);
					interfaceGame.paintInGame(g);
					interfaceGame.paintVecEffKickAn(g);
				}
				Interface_Game.paintAutoRepeatQuest(g);
				Interface_Game.paintWaitingForMonster(g);
				Interface_Game.paintAutoSuperBoss(g);
			}
			if (LoadMap.specMap == 3)
			{
				Interface_Game.PaintLoadData(g, LoadMap.currentXp, LoadMap.maxXp, MotherCanvas.hw - 47, MotherCanvas.h / 8, 100, 12, 15);
				mFont.tahoma_7b_black.drawString(g, Player.strTimeChange, MotherCanvas.hw, MotherCanvas.h / 8 - 10, 2);
			}
			else if (!getIsOffAdmin(1))
			{
				if (!isPvPNew)
				{
					interfaceGame.paintInfoFocus(g);
					bool isborder = true;
					if (GameCanvas.isSmallScreen)
					{
						isborder = false;
					}
					Interface_Game.paintInfoPlayer(g, 3, 3 + h12plus, isborder, mFont.tahoma_7_black);
				}
				else
				{
					Interface_Game.paintPvPNew(g, player, objPvPNew);
				}
			}
			if (GameCanvas.loadmap != null && player != null)
			{
				if (player.x != s_lastDbgX || player.y != s_lastDbgY || GameCanvas.loadmap.idMap != s_lastDbgMap)
				{
					s_lastDbgX = player.x;
					s_lastDbgY = player.y;
					s_lastDbgMap = GameCanvas.loadmap.idMap;
					s_cachedDbgStr = "Map: " + GameCanvas.loadmap.idMap + " | X: " + player.x + " (" + (player.x / 24) + ") | Y: " + player.y + " (" + (player.y / 24) + ") | W: " + GameCanvas.loadmap.maxWMap + " H: " + GameCanvas.loadmap.maxHMap;
				}
				int strH = mFont.tahoma_7_yellow.getHeight();
				int debugX = 4;
				int debugY = MotherCanvas.h - strH - 4 - (GameCanvas.isTouch ? 0 : 15);
				mFont.tahoma_7_yellow.drawString(g, s_cachedDbgStr, debugX + 4, debugY + 2, 0);
			}
		}
		else if (GameCanvas.currentScreen == GameCanvas.gameScr && !GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null)
		{
			GameCanvas.resetTrans(g);
			interfaceGame.paintViewGame(g);
			if (GameCanvas.isTouch)
			{
				paintCmd(g);
			}
		}
		GameCanvas.resetTrans(g);
		if (!getIsOffAdmin(1))
		{
			interfaceGame.paintShowNameMap(g);
		}
		if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !GameCanvas.menuCur.isShowMenu)
		{
			if (GameCanvas.isTouch)
			{
				paintCmd(g);
			}
			else
			{
				paintOnlyCaption(g);
			}
		}
		Interface_Game.paintShowNear(g);
		if (Interface_Game.eventcur == null)
		{
			Interface_Game.paintEffShowMoney(g, MotherCanvas.hw - Interface_Game.wMoneyEff / 2, h12plus, isShow: false, Interface_Game.typeShowMoney);
		}
		if (!getIsOffAdmin(1))
		{
			for (int num7 = 0; num7 < vecBigBossLittleGraden.size(); num7++)
			{
				((BigBossLittleGraden)vecBigBossLittleGraden.elementAt(num7)).paintINFO(g);
			}
		}
		AThMadaraFunc.paintShowDateTime(g);
	}

	public bool disLowPaintOnlyShadow(MainObject obj)
	{
		if (!GameCanvas.isSuperLowGraphic)
		{
			return false;
		}
		if (obj == null)
		{
			return true;
		}
		if (MainObject.getDistance(player.x, player.y, obj.x, obj.y) >= 120)
		{
			if (obj.demDonotPaint < 20)
			{
				obj.demDonotPaint++;
				return false;
			}
			return true;
		}
		obj.demDonotPaint = 0;
		return false;
	}

	public bool checkPaintLow(MainObject obj)
	{
		if (!GameCanvas.lowGraphic)
		{
			return false;
		}
		if (obj == null)
		{
			return false;
		}
		if (objFocus != null && obj == objFocus)
		{
			return false;
		}
		if (MainObject.getDistance(obj.x, obj.y, player.x, player.y) < 120)
		{
			return false;
		}
		return true;
	}

	public bool checkPaintOB(MainObject obj)
	{
		if (obj == null || obj.isRemove || obj.isStop)
		{
			return false;
		}
		if (MainObject.isInScreen(obj))
		{
			return true;
		}
		if (obj.isTru())
		{
			return true;
		}
		return false;
	}

	public void paintTree(mGraphics g, int index)
	{
		for (int i = 0; i < LoadMap.mItemMap[index].size(); i++)
		{
			tr = (MainItemMap)LoadMap.mItemMap[index].elementAt(i);
			if (tr.isInScreen())
			{
				tr.paint(g);
			}
		}
	}

	public void updateOfflineMap()
	{
		if (LoadMap.specMap == 5)
		{
			MapOff_RedLine.updateRedLine();
		}
		if (LoadMap.specMap == 8)
		{
			MapGotoSky.update();
			int num;
			for (num = 0; num < vecObjMove.size(); num++)
			{
				ObjMove objMove = (ObjMove)vecObjMove.elementAt(num);
				if (!objMove.isRemove)
				{
					updateMoveObj(objMove);
				}
				vecObjMove.removeElementAt(num);
				num--;
			}
		}
		if (LoadMap.specMap != 12)
		{
			return;
		}
		MapGotoGod.update();
		int num2;
		for (num2 = 0; num2 < vecObjMove.size(); num2++)
		{
			ObjMove objMove2 = (ObjMove)vecObjMove.elementAt(num2);
			if (!objMove2.isRemove)
			{
				updateMoveObj(objMove2);
			}
			vecObjMove.removeElementAt(num2);
			num2--;
		}
	}

	public override void update()
	{
		if (!LoadMap.isOnlineMap)
		{
			updateOfflineMap();
			return;
		}
		if (GameCanvas.showDialog != null)
		{
			GameCanvas.showDialog.update();
		}
		for (int i = 0; i < vecBigBossLittleGraden.size(); i++)
		{
			((BigBossLittleGraden)vecBigBossLittleGraden.elementAt(i)).update();
		}
		if (Interface_Game.timeShowNear > -5)
		{
			Interface_Game.updateShowNear();
		}
		if (GameCanvas.mapBack != null)
		{
			GameCanvas.mapBack.updateCloud();
			GameCanvas.mapBack.update();
		}
		for (int j = 0; j < LoadMap.mItemMap[3].size(); j++)
		{
			MainItemMap mainItemMap = (MainItemMap)LoadMap.mItemMap[3].elementAt(j);
			if (mainItemMap.TypeItem == 1 || mainItemMap.TypeItem == 1)
			{
				mainItemMap.update();
			}
		}
		int num;
		for (num = 0; num < vecObjMove.size(); num++)
		{
			ObjMove objMove = (ObjMove)vecObjMove.elementAt(num);
			if (!objMove.isRemove)
			{
				updateMoveObj(objMove);
			}
			vecObjMove.removeElementAt(num);
			num--;
		}
		readVecFire();
		for (int k = 0; k < vecPlayers.size(); k++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(k);
			if (mainObject == null || mainObject.isRemove)
			{
				if (GameCanvas.lowGraphic)
				{
					vecPlayers.removeElementAt(k);
					k--;
				}
				else if (mainObject == null || mainObject.timeEffRemoveChar <= 0)
				{
					vecPlayers.removeElementAt(k);
					k--;
				}
				else
				{
					mainObject.timeEffRemoveChar--;
					mainObject.update();
					mainObject.ySort = mainObject.y + mainObject.ysai;
				}
				continue;
			}
			mainObject.update();
			mainObject.ySort = mainObject.y + mainObject.ysai;
			if (mainObject.typeObject != 0)
			{
				continue;
			}
			if (checkPaintOB(mainObject))
			{
				mainObject.timeOutScreen = 0;
			}
			else
			{
				mainObject.timeOutScreen++;
				if (mainObject.timeOutScreen >= 1000)
				{
					mainObject.isRemove = true;
				}
			}
			if (isPvPNew && mainObject != player && mainObject.typeObject == 0 && (objPvPNew == null || objPvPNew.isRemove || objPvPNew.typeObject != 0))
			{
				objPvPNew = mainObject;
			}
			if (objPvPNew != null && objPvPNew.isRemove)
			{
				objPvPNew = null;
			}
		}
		for (int l = 0; l < vecHighDataEff.size(); l++)
		{
			DataSkillEff dataSkillEff = (DataSkillEff)vecHighDataEff.elementAt(l);
			if (dataSkillEff != null)
			{
				dataSkillEff.update();
				if (dataSkillEff.wantDestroy)
				{
					vecHighDataEff.removeElementAt(l);
					l--;
				}
			}
		}
		numEff = 0;
		for (int m = 0; m < VecEffect.size(); m++)
		{
			MainEffect mainEffect = (MainEffect)VecEffect.elementAt(m);
			if (mainEffect == null || mainEffect.isRemove)
			{
				VecEffect.removeElementAt(m);
				m--;
			}
			else if (!mainEffect.isStop)
			{
				numEff++;
				mainEffect.update();
			}
		}
		for (int n = 0; n < VecNum.size(); n++)
		{
			MainEffect mainEffect2 = (MainEffect)VecNum.elementAt(n);
			if (mainEffect2 != null && !mainEffect2.isRemove && !mainEffect2.isStop)
			{
				mainEffect2.update();
			}
		}
		if (tickPvP > 0)
		{
			tickPvP--;
			if (tickPvP == 0 && center == cmdInfoDoithu)
			{
				center = null;
			}
		}
		if (isMoveCamera)
		{
			MainScreen.cameraMain.setCameraWithLim(xCur - xMoveCam, yCur - yMoveCam, isRoom());
		}
		else if (typeViewPlayer == 0)
		{
			if (!isMoveCamEff)
			{
				bool flag = false;
				if (tickPvP > 0 && vecPlayers.size() > 1)
				{
					for (int num2 = 0; num2 < vecPlayers.size(); num2++)
					{
						MainObject mainObject2 = (MainObject)vecPlayers.elementAt(num2);
						if (GameCanvas.loadmap.idMap == 59)
						{
							if (mainObject2.typeObject == 1)
							{
								MainScreen.cameraMain.moveCamera(mainObject2.x - MotherCanvas.w / 2, mainObject2.y - MotherCanvas.h / 3 * 2);
								flag = true;
								break;
							}
						}
						else if (mainObject2 != player)
						{
							MainScreen.cameraMain.moveCamera(mainObject2.x - MotherCanvas.w / 2, mainObject2.y - MotherCanvas.h / 3 * 2);
							flag = true;
							break;
						}
					}
					if (cmdInfoDoithu == null && GameCanvas.loadmap.idMap != 59)
					{
						cmdInfoDoithu = new iCommand(T.info, 47, this);
						GameCanvas.gameScr.center = GameCanvas.gameScr.cmdInfoDoithu;
						GameCanvas.gameScr.cmdInfoDoithu = AvMain.setPosCMD(GameCanvas.gameScr.cmdInfoDoithu, 0);
					}
				}
				if (!flag)
				{
					if (LoadMap.specMap == 7)
					{
						MainScreen.cameraMain.moveCamera(player.x - MotherCanvas.w / 2, player.y - MotherCanvas.h / 5 * 4);
					}
					else
					{
						MainScreen.cameraMain.moveCamera(player.x - MotherCanvas.w / 2, player.y - MotherCanvas.h / 3 * 2);
					}
				}
			}
		}
		else if (objView == null)
		{
			MainScreen.cameraMain.setCameraWithLim(xCur - xMoveCam, yCur - yMoveCam, isRoom());
		}
		else
		{
			MainScreen.cameraMain.moveCamera(objView.x - MotherCanvas.w / 2, objView.y - MotherCanvas.h / 3 * 2);
		}
		MainScreen.cameraMain.UpdateCameraGameScreen();
		if (objFocus != null && objFocus.returnAction())
		{
			objFocus = null;
			center = null;
		}
		int num3 = vecEffTam.size();
		for (int num4 = 0; num4 < num3; num4++)
		{
			MainEffect mainEffect3 = (MainEffect)vecEffTam.elementAt(num4);
			try
			{
				if (mainEffect3 != null && mainEffect3.CreateEffectSkill())
				{
					int num5 = find_Index_Stop(VecEffect);
					if (num5 == VecEffect.size())
					{
						VecEffect.addElement(mainEffect3);
					}
					else
					{
						VecEffect.setElementAt(mainEffect3, num5);
					}
				}
			}
			catch (Exception)
			{
			}
		}
		vecEffTam.removeAllElements();
		Interface_Game.updateNumMess();
		Interface_Game.updateMoveTo();
		interfaceGame.updateShowNameMap();
		if (effMap != null)
		{
			effMap.update();
		}
		if (effSea != null)
		{
			effSea.updateSea();
		}
		for (int num6 = 0; num6 < vecEffOnMap.size(); num6++)
		{
			Point point = (Point)vecEffOnMap.elementAt(num6);
			point.f++;
			if (point.fSmall == 0)
			{
				if (point.f == point.fRe / 6 * 4)
				{
					point.frame = 1;
				}
				if (point.f == point.fRe / 6 * 5)
				{
					point.frame = 2;
				}
				if ((point.f >= point.fRe && vecEffOnMap.size() > 20) || point.f > point.fRe + point.fRe / 2 || point.f >= 100)
				{
					vecEffOnMap.removeElement(point);
					num6--;
				}
			}
		}
		if (typePaintGameScreen != 0)
		{
			for (int num7 = 0; num7 < vecEffSkillSpec.size(); num7++)
			{
				Point point2 = (Point)vecEffSkillSpec.elementAt(num7);
				point2.update();
				if (point2.x < -10)
				{
					point2.x = MotherCanvas.w + CRes.random(MotherCanvas.w / 4);
					point2.y = 5 + num7 * 10 + CRes.random_Am_0(4);
					point2 = createPoint(point2);
				}
			}
		}
		if (GameCanvas.isTypeSpam == 1)
		{
			if (GameCanvas.currentDialog == null && GameCanvas.currentScreen == this)
			{
				tickSpam++;
			}
			if (tickSpam >= 50)
			{
				GameCanvas.isTypeSpam = 0;
				GameCanvas.gameScr.cmdSpam.perform();
			}
		}
		switch (GameCanvas.gameTick % 4000)
		{
		case 3999:
			checkRemoveImage(1, isTrue: false);
			break;
		case 1000:
			checkRemoveImage(2, isTrue: false);
			break;
		case 2000:
			checkRemoveImage(3, isTrue: false);
			break;
		case 3000:
			checkRemoveImage(4, isTrue: false);
			break;
		case 2100:
			GlobalService.gI().checkPlayInMap(vecPlayers);
			break;
		}
		if (ReadMessenge.timeLoadItemMap > 0)
		{
			if (LoadMap.demSendTem == 1000)
			{
				LoadMap.demSendTem = 0;
				GameCanvas.loadmap.checkSetItemMap();
			}
			ReadMessenge.timeLoadItemMap--;
			if (ReadMessenge.timeLoadItemMap == 10)
			{
				GameCanvas.loadmap.checkSetItemMap();
				for (int num8 = 0; num8 < LoadMap.mItemMap.Length; num8++)
				{
					CRes.quickSort(LoadMap.mItemMap[num8]);
				}
			}
		}
		if (isOnRepeatQuest)
		{
			autoRepeatQuest.StartAutoRepeatQuest();
		}
		if (isOnSuperBoss)
		{
			autoSuperBoss.StartAutoSuperBoss();
		}
		if (isOnAutoPB)
		{
			AutoBattlefield.StartAutoBattlefield();
		}
	}

	public static bool checkAddEff()
	{
		if ((GameCanvas.lowGraphic && numEff > 20) || numEff > 100)
		{
			return false;
		}
		return true;
	}

	public static void checkObjRemoveInVecMove(short id, sbyte cat)
	{
		for (int i = 0; i < vecObjMove.size(); i++)
		{
			ObjMove objMove = (ObjMove)vecObjMove.elementAt(i);
			if (id == objMove.id && objMove.typeObj == cat)
			{
				objMove.isRemove = true;
			}
		}
	}

	public static void checkRemoveImage(int type, bool isTrue)
	{
		bool isTrue2 = false;
		if (type == 0 || type == 1)
		{
			ObjectData.checkDelHash_Data(DataSkillEff.ALL_EFF_DATA, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.HashImageItemMap, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.hashImageIconClan, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.hashImageIconClanBig, 120, isTrue2);
			for (int i = 0; i < VecEffect.size(); i++)
			{
				MainEffect mainEffect = (MainEffect)VecEffect.elementAt(i);
				if (mainEffect.isStop)
				{
					mainEffect.isRemove = true;
				}
			}
		}
		else if (type == 0 || type == 2)
		{
			ObjectData.checkDelHash(ObjectData.hashImagePotion, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.HashImageMonster, 120, isTrue2);
			CharPartInfo.checkDelHashCharPart(CharPartInfo.hashMyPart, 240);
			ObjectData.checkDelHash(ObjectData.hashImageItemOther, 120, isTrue2);
		}
		else if (type == 0 || type == 3)
		{
			ObjectData.checkDelHash(ObjectData.hashImageNPC, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.HashImageCharPart, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.hashImageItem, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.hashImageMaterialPotion, 120, isTrue2);
			for (int j = 0; j < VecNum.size(); j++)
			{
				MainEffect mainEffect2 = (MainEffect)VecNum.elementAt(j);
				if (mainEffect2.isStop)
				{
					mainEffect2.isRemove = true;
				}
			}
		}
		else if (type == 0 || type == 4)
		{
			ObjectData.checkDelHash(ObjectData.HashImageEffClient, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.HashImageOtherNew, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.hashImageBoat, 120, isTrue2);
			ObjectData.checkDelHash(ObjectData.HashImageFashion, 120, isTrue2);
		}
		mSystem.gc();
	}

	private void updateMoveObj(ObjMove obj)
	{
		MainObject mainObject = MainObject.get_Object(obj.id, obj.typeObj);
		if (mainObject == null)
		{
			if (obj.typeObj == 1)
			{
				MainMonster mainMonster = new MainMonster(obj.id, obj.x, obj.y);
				addPlayer(mainMonster);
				GlobalService.gI().monster_info(obj.id);
				mainMonster.timeLoadInfo = GameCanvas.timeNow;
			}
			else if (obj.typeObj == 0 || obj.typeObj == 2)
			{
				Other_Player obj2 = new Other_Player(obj.id, obj.typeObj, "", obj.x, obj.y)
				{
					Dir = ((CRes.random(2) != 0) ? 2 : 0)
				};
				addPlayer(obj2);
				GlobalService.gI().char_info(obj.id);
				obj2.timeLoadInfo = GameCanvas.timeNow;
			}
		}
		else
		{
			if (mainObject == player)
			{
				if (mainObject.posTransRoad != null)
				{
					return;
				}
				mainObject.x = obj.x;
				mainObject.y = obj.y;
				mainObject.toX = obj.x;
				mainObject.toY = obj.y;
				mainObject.xStand = obj.x;
				mainObject.yStand = obj.y;
				mainObject.isMoveNor = false;
				mainObject.resetAction();
			}
			else
			{
				if (MainObject.getDistance(mainObject.toX, mainObject.toY, obj.x, obj.y) < mainObject.vMax)
				{
					return;
				}
				if (mainObject.Action == 4 || MainObject.getDistance(mainObject.x, mainObject.y, obj.x, obj.y) > 150)
				{
					mainObject.x = obj.x;
					mainObject.y = obj.y;
					mainObject.toX = obj.x;
					mainObject.toY = obj.y;
					mainObject.toXNew = obj.x;
					mainObject.toYNew = obj.y;
					mainObject.xStand = obj.x;
					mainObject.yStand = obj.y;
					mainObject.vx = 0;
					mainObject.vy = 0;
					mainObject.isMoveNor = false;
					if (mainObject.Action == 4 && mainObject.Hp > 0)
					{
						mainObject.Reveive();
					}
				}
				else if (mainObject.Action != 2 && mainObject.skillCurrent == null)
				{
					if (mainObject.timeBeginUpdateMove < 0)
					{
						mainObject.timeBeginUpdateMove = GameCanvas.gameTick % 10 + 1;
					}
					mainObject.toXNew = obj.x;
					mainObject.toYNew = obj.y;
				}
			}
		}
	}

	public override void updatekey()
	{
		if (player.isNauBanh && GameCanvas.keyMyHold[5] && center != null)
		{
			GameCanvas.clearKeyPressed(5);
			GameCanvas.clearKeyHold(5);
			center.perform();
		}

		// Hotkey phim tat nhanh Mod khi khong co dialog va khong nhap text
		if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !ChatTextField.isShow && TField.currentTField == null)
		{
			if (GameCanvas.keyMyHold[42] || GameCanvas.keyMyPressed[42]) // Phim M -> Menu MOD
			{
				GameCanvas.clearKeyHold(42);
				GameCanvas.clearKeyPressed(42);
				AThMadaraMOD.getInstance().openMenuAuto();
			}
			else if (GameCanvas.keyMyHold[45] || GameCanvas.keyMyPressed[45]) // Phim Q -> QuickMenu
			{
				GameCanvas.clearKeyHold(45);
				GameCanvas.clearKeyPressed(45);
				QuickMenu.gI().startAt();
			}
			else if (GameCanvas.keyMyHold[48] || GameCanvas.keyMyPressed[48]) // Phim T -> Tan Sat
			{
				GameCanvas.clearKeyHold(48);
				GameCanvas.clearKeyPressed(48);
				commandPointer(108, 0);
			}
			else if (GameCanvas.keyMyHold[51] || GameCanvas.keyMyPressed[51]) // Phim I -> Dong Bang Quai
			{
				GameCanvas.clearKeyHold(51);
				GameCanvas.clearKeyPressed(51);
				commandPointer(111, 0);
			}
		}

		if (typeViewPlayer == 0)
		{
			if (player != null)
			{
				player.updateKey();
			}
		}
		else
		{
			updateKeyView();
		}
		base.updatekey();
	}

	private void updateKeyView()
	{
		bool flag = false;
		if (GameCanvas.keyMove(0))
		{
			xMoveCam += 10;
			flag = true;
		}
		else if (GameCanvas.keyMove(2))
		{
			xMoveCam -= 10;
			flag = true;
		}
		else if (GameCanvas.keyMove(1))
		{
			yMoveCam += 10;
			flag = true;
		}
		else if (GameCanvas.keyMove(3))
		{
			yMoveCam -= 10;
			flag = true;
		}
		if (!flag)
		{
			return;
		}
		timeResetCam = 40;
		if (!isMoveCamera)
		{
			xCur = MainScreen.cameraMain.xCam;
			yCur = MainScreen.cameraMain.yCam;
			xMoveCam = 0;
			yMoveCam = 0;
			isMoveCamera = true;
			return;
		}
		if (xCur - xMoveCam <= 0)
		{
			xMoveCam = xCur;
		}
		if (xCur - xMoveCam >= MainScreen.cameraMain.xLimit)
		{
			xMoveCam = xCur - MainScreen.cameraMain.xLimit;
		}
		if (yCur - yMoveCam <= 0)
		{
			yMoveCam = yCur;
		}
		if (yCur - yMoveCam >= MainScreen.cameraMain.yLimit)
		{
			yMoveCam = yCur - MainScreen.cameraMain.yLimit;
		}
	}

	public override void updatePointer()
	{
		base.updatePointer();
		if (!LoadMap.isOnlineMap)
		{
			updatePointerMapOffLine();
		}
		else if (GameCanvas.currentScreen == GameCanvas.gameScr)
		{
			if (typeViewPlayer == 0)
			{
				interfaceGame.updateInGame();
			}
			else
			{
				interfaceGame.updateViewKhanGia();
			}
		}
	}

	private void updatePointerMapOffLine()
	{
		if (LoadMap.specMap == 5 || LoadMap.specMap == 8 || LoadMap.specMap == 12)
		{
			MapOff_RedLine.moveTypeKeypad();
		}
	}

	public static void addBoatVec_And_mItem(Boat boat, bool isSort)
	{
		vecBoat.addElement(boat);
		LoadMap.mItemMap[3].addElement(boat);
		if (isSort)
		{
			CRes.quickSort(LoadMap.mItemMap[3]);
		}
	}

	public static void removeBoatVec_And_mItem(Boat boat)
	{
		vecBoat.removeElement(boat);
		LoadMap.mItemMap[3].removeElement(boat);
	}

	public static void addPlayer(MainObject obj)
	{
		if (obj == null) return;
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject existing = (MainObject)vecPlayers.elementAt(i);
			if (existing != null && existing.typeObject == obj.typeObject)
			{
				short existingId = (existing.typeObject == 10) ? existing.IDMainShiper : existing.ID;
				short newId = (obj.typeObject == 10) ? obj.IDMainShiper : obj.ID;
				if (existingId == newId)
				{
					// Đã tồn tại object cùng ID và type -> thay thế instance cũ, tránh duplicate ghost bot
					vecPlayers.setElementAt(obj, i);
					isVecPlayersDirty = true;
					return;
				}
			}
		}
		vecPlayers.addElement(obj);
		isVecPlayersDirty = true;
	}

	public static MainItemMap addEffectAuto(string key, string value)
	{
		return new EffectAuto(key, value);
	}

	public static void RemoveLoadMap()
	{
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (mainObject != player && (mainObject.typeObject != 10 || mainObject.IDMainShiper != player.ID))
			{
				vecPlayers.removeElementAt(i);
				i--;
			}
		}
		isVecPlayersDirty = true;
		vecBoat.removeAllElements();
		try
		{
			AThMadaraFunc.resetGomData();
			if (player != null)
			{
				player.oldMonsterPos = null;
			}
		}
		catch (Exception)
		{
		}
		GameCanvas.gameScr.resetRecAlpha();
	}

	public static void RemoveAllNPC()
	{
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (mainObject.typeObject == 2)
			{
				vecPlayers.removeElement(mainObject);
				i--;
			}
		}
	}

	public static int find_Index_Stop(mVector vec)
	{
		if (vec == null) return 0;
		int sz = vec.size();
		for (int i = 0; i < sz; i++)
		{
			MainEffect mainEffect = (MainEffect)vec.elementAt(i);
			if (mainEffect != null && (mainEffect.isStop || mainEffect.isRemove))
			{
				return i;
			}
		}
		return sz;
	}

	public static void addLazer(sbyte type, int x, int y, int xto, int yto)
	{
		Lazer o = new Lazer(0, x, y, xto, yto);
		vecEffTam.addElement(o);
	}

	public static void addEffectEnd(short type, int subtype, int x, int y, int time, sbyte dir, MainObject objEff)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subtype, x, y, time, dir, objEff);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectEnd(short type, int subtype, int x, int y, sbyte dir, MainObject objEff)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subtype, x, y, dir, objEff);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectEnd_ToX_ToY(short type, int subtype, int x, int y, int xTo, int yTo, sbyte dir, MainObject objEff)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subtype, x, y, xTo, yTo, dir, objEff);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectBuffNew(MainObject obj, short typeBuff, short time)
	{
		MainBuff mainBuff = new MainBuff(typeBuff);
		mainBuff.obj = obj;
		mainBuff.setTimeBuff(time);
		obj.AddBuffNew2(mainBuff);
	}

	public static void addEffectEnd_ObjTo(short type, int subType, int x, int y, short Id, sbyte cat, sbyte dir, MainObject objEff)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subType, x, y, Id, cat, dir, objEff, 0);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectEnd_ObjTo(short type, int subType, int x, int y, short Id, sbyte cat, short idImage, sbyte dir, MainObject objEff)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subType, x, y, Id, cat, idImage, dir, objEff, 0);
			vecEffTam.addElement(o);
		}
	}

	public static MainEffect createEffectEnd_ObjTo(short type, int subType, int x, int y, short Id, sbyte cat, sbyte dir, MainObject objEff)
	{
		return new Effect_End(type, (sbyte)subType, x, y, Id, cat, dir, objEff, 0);
	}

	public static void addEffectEnd_Time(short type, int subType, int x, int y, short Id, sbyte cat, sbyte dir, MainObject objEff, int time)
	{
		if (objEff == player || checkAddEff())
		{
			Effect_End o = new Effect_End(type, (sbyte)subType, x, y, Id, cat, dir, objEff, time);
			vecEffTam.addElement(o);
		}
	}

	public static mVector cloneTargetVector(mVector vec)
	{
		if (vec == null) return null;
		mVector res = new mVector();
		for (int i = 0; i < vec.size(); i++)
		{
			Object_Effect_Skill o = (Object_Effect_Skill)vec.elementAt(i);
			if (o != null)
			{
				Object_Effect_Skill copy = new Object_Effect_Skill(o.ID, o.tem);
				copy.hpShow = o.hpShow;
				copy.hpLast = o.hpLast;
				copy.hpMagic = o.hpMagic;
				res.addElement(copy);
			}
		}
		return res;
	}

	public static void addEffectSkill(MainSkill skill, MainObject objkill, mVector vecObjsBeFire)
	{
		Skill_Info sk = (skill != null) ? Skill_Info.getSkillFromID(skill.ID) : null;
		short defaultEff = (skill != null) ? skill.typeEffSkill : (short)0;
		System.Collections.Generic.List<short> effList = AThMadaraMOD.getCustomSkillEffList(sk, defaultEff);

		for (int i = 0; i < effList.Count; i++)
		{
			short effId = effList[i];
			if (skill != null)
			{
				effId = Effect_Skill.getUpgradedEffSkill(effId, skill.lvDevil);
			}
			mVector clonedTargets = cloneTargetVector(vecObjsBeFire);
			Effect_Skill o = new Effect_Skill(effId, (skill != null) ? skill.typeSub : 0, objkill, clonedTargets);
			if (skill != null)
			{
				o.skill = skill;
				o.timeEnd = skill.timebuff;
			}
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectSkill2(short typeEffSkill, MainObject objkill, Object_Effect_Skill objbekill, int x, int y)
	{
		mVector mVector2 = new mVector();
		mVector2.addElement(objbekill);
		Effect_Skill o = new Effect_Skill(typeEffSkill, 0, objkill, mVector2, x, y);
		vecEffTam.addElement(o);
	}

	public static void addEffectSkill(MainSkill skill, MainObject objkill)
	{
		Skill_Info sk = (skill != null) ? Skill_Info.getSkillFromID(skill.ID) : null;
		short defaultEff = (skill != null) ? skill.typeEffSkill : (short)0;
		System.Collections.Generic.List<short> effList = AThMadaraMOD.getCustomSkillEffList(sk, defaultEff);

		for (int i = 0; i < effList.Count; i++)
		{
			short effId = effList[i];
			MainSkill customSk = new MainSkill(skill.ID, effId)
			{
				typeSub = skill.typeSub,
				x = skill.x,
				y = skill.y,
				vecPos = skill.vecPos,
				lvDevil = (skill != null) ? skill.lvDevil : (sbyte)0,
				typeEffBuff = (skill != null) ? skill.typeEffBuff : (short)0,
				timebuff = (skill != null) ? skill.timebuff : (short)0,
				timeBegin = (skill != null) ? skill.timeBegin : GameCanvas.timeNow,
				typeBuff = (skill != null) ? skill.typeBuff : (sbyte)0,
				isBuff = (skill != null) && skill.isBuff
			};
			Effect_Skill o = new Effect_Skill(customSk, objkill);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectSkillSpec(MainSkill skill, MainObject objkill)
	{
		Skill_Info sk = (skill != null) ? Skill_Info.getSkillFromID(skill.ID) : null;
		short defaultEff = (skill != null) ? skill.typeEffSkill : (short)0;
		System.Collections.Generic.List<short> effList = AThMadaraMOD.getCustomSkillEffList(sk, defaultEff);

		for (int i = 0; i < effList.Count; i++)
		{
			short effId = effList[i];
			MainSkill customSk = new MainSkill(skill.ID, effId)
			{
				typeSub = skill.typeSub,
				x = skill.x,
				y = skill.y,
				vecPos = skill.vecPos,
				lvDevil = (skill != null) ? skill.lvDevil : (sbyte)0,
				typeEffBuff = (skill != null) ? skill.typeEffBuff : (short)0,
				timebuff = (skill != null) ? skill.timebuff : (short)0,
				timeBegin = (skill != null) ? skill.timeBegin : GameCanvas.timeNow,
				typeBuff = (skill != null) ? skill.typeBuff : (sbyte)0,
				isBuff = (skill != null) && skill.isBuff
			};
			Effect_Skill o = new Effect_Skill(customSk, objkill, customSk.x, customSk.y, customSk.vecPos);
			vecEffTam.addElement(o);
		}
	}

	public static void addEffectNum(string content, int x, int y, sbyte typeColor)
	{
		EffectNum effectNum = new EffectNum(content, x, y, typeColor);
		int num = find_Index_Stop(VecNum);
		if (num == VecNum.size())
		{
			VecNum.addElement(effectNum);
		}
		else
		{
			VecNum.setElementAt(effectNum, num);
		}
	}

	public static void addEffectNumImage(string content, int x, int y, sbyte typeColor, FrameImage fra, int frame)
	{
		EffectNum effectNum = new EffectNum(content, x, y, typeColor, fra, frame);
		int num = find_Index_Stop(VecNum);
		if (num == VecNum.size())
		{
			VecNum.addElement(effectNum);
		}
		else
		{
			VecNum.setElementAt(effectNum, num);
		}
	}

	public static void addEffectNumBig_NEW_AP(int value, int valueAP, int x, int y, sbyte typeColor)
	{
		EffectNum effectNum = new EffectNum(value, valueAP, x, y, typeColor);
		int num = find_Index_Stop(VecNum);
		if (num == VecNum.size())
		{
			VecNum.addElement(effectNum);
		}
		else
		{
			VecNum.setElementAt(effectNum, num);
		}
	}

	public static MainEffect CreateEffectEnd(short type, int subtype, int x, int y, sbyte dir, MainObject objEff)
	{
		return new Effect_End(type, (sbyte)subtype, x, y, dir, objEff);
	}

	public static MainEffect createEffEnd(short type, int subtype, int x, int y, int xto, int yto)
	{
		return new Effect_End(type, (sbyte)subtype, x, y, xto, yto, 0, null);
	}

	public static MainEffect createEffectEndTime(short type, int subtype, int x, int y, int time, sbyte dir, MainObject objEff)
	{
		return new Effect_End(type, (sbyte)subtype, x, y, time, dir, objEff);
	}

	public static void isPaintNormal()
	{
		typePaintGameScreen = 0;
		if (GameCanvas.gameScr != null)
		{
			GameCanvas.gameScr.isFullScreen = false;
			GameCanvas.gameScr.fullScreenAlpha = 0f;
		}
		vecEffSkillSpec.removeAllElements();
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (mainObject.isPaintSpec)
			{
				mainObject.isPaintSpec = false;
			}
		}
	}

	public static void beginPaintSpec()
	{
		int num = MotherCanvas.h / 10;
		typePaintGameScreen = 1;
		vecEffSkillSpec.removeAllElements();
		for (int i = 0; i < num; i++)
		{
			Point point = new Point();
			createPoint(point);
			vecEffSkillSpec.addElement(point);
		}
	}

	public static Point createPoint(Point p)
	{
		p.vx = -CRes.random(25, 35);
		p.w = CRes.random(16, 30);
		p.h = CRes.random(2, 7) / 2;
		p.color = 15970753;
		if (CRes.random(4) == 3)
		{
			p.color = 15791467;
		}
		if (p.y >= MotherCanvas.h / 4 * 3 - size - 12 && p.y <= MotherCanvas.h / 4 * 3 + size + 12)
		{
			p.dis = CRes.random(4);
		}
		else
		{
			p.dis = CRes.random(10);
		}
		return p;
	}

	public void setTypeViewPlayer(sbyte type)
	{
		typeViewPlayer = type;
		isMoveCamera = false;
		xMoveCam = 0;
		yMoveCam = 0;
		if (typeViewPlayer != 0)
		{
			isSetObjdDefault = true;
			objView = null;
			center = null;
			left = cmdSettingView;
			right = cmdExitView;
			MainScreen.cameraMain.moveCamera(player.x - MotherCanvas.w / 2, player.y - MotherCanvas.h / 3 * 2);
			xCur = MainScreen.cameraMain.xTo;
			yCur = MainScreen.cameraMain.yTo;
		}
	}

	public static void setIsMoveEff(bool ismove)
	{
		isMoveCamEff = ismove;
	}

	public static void CreateEffOnMap(sbyte type, int x, int y, int dir, sbyte subtype)
	{
		if (GameCanvas.lowGraphic || vecEffOnMap.size() > 100)
		{
			return;
		}
		Point point = new Point(x + CRes.random_Am_0(2), y + CRes.random_Am_0(2));
		point.fSmall = type;
		point.subType = subtype % 2;
		if (GameMidlet.DEVICE != 0)
		{
			if (vecEffOnMap.size() < 50)
			{
				point.fRe = CRes.random(50, 70);
			}
			else
			{
				point.fRe = CRes.random(20, 30);
			}
		}
		else if (vecEffOnMap.size() < 50)
		{
			point.fRe = CRes.random(15, 25);
		}
		else
		{
			point.fRe = CRes.random(8, 15);
		}
		point.dis = dir;
		vecEffOnMap.addElement(point);
	}

	public static void CreateEffOnMapFullset(short type, int x, int y, int dir, sbyte subtype)
	{
		addEffectEnd(type, 0, x, y, (sbyte)dir, null);
	}

	public static void addHelp(int type, int typeSub)
	{
		MainHelp o = new MainHelp(type, typeSub);
		vecHelp.addElement(o);
	}

	public override bool keyBack()
	{
		if (!base.keyBack() && cmdHoiExit != null)
		{
			cmdHoiExit.perform();
		}
		return false;
	}

	public void ShowInfoOtherPlayer(string name)
	{
		GlobalService.gI().Show_Player_Info(name);
	}

	public void ShowChatTab(string name)
	{
		GameCanvas.chatTabScr.addNewChat(name, "", "", 0, isFocus: true);
		if (MsgSpamSetup.isCheckSpam(1, name))
		{
			GameCanvas.chatTabScr.addNewChat(name, "", T.dangbatspamchat, 0, isFocus: true);
		}
		InfoMemList.setTypeEvent(-1, 2, name, "", 0, 0);
		GameCanvas.chatTabScr.Show(GameCanvas.gameScr);
	}

	public static void setSuperEffect(sbyte type, sbyte tick)
	{
		typeSuperEffect = type;
		tickSuperEffect = tick;
	}

	public static bool getIsOffAdmin(sbyte type)
	{
		if (isOffAll)
		{
			return true;
		}
		if (type == 0 && !isShowName)
		{
			return true;
		}
		if (type == 1 && !isPaintInterface)
		{
			return true;
		}
		if (type == 2 && !isShowChat)
		{
			return true;
		}
		return false;
	}

	public static bool isRoom()
	{
		if (GameCanvas.loadmap.idMap == 100 || GameCanvas.loadmap.idMap == 101)
		{
			return true;
		}
		return false;
	}

	public static short checkPokemon()
	{
		if (objFocus != null && objFocus.isPokemon > 0)
		{
			return objFocus.ID;
		}
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (!mainObject.isRemove && mainObject.isPokemon > 0 && MainObject.getDistance(player.x, player.y, mainObject.x, mainObject.y) <= 160)
			{
				return mainObject.ID;
			}
		}
		return -1;
	}

	public void readVecFire()
	{
		try
		{
			for (int i = 0; i < vecObjFire.size(); i++)
			{
				Message message = (Message)vecObjFire.elementAt(i);
				try
				{
					if (!LoadMapScreen.isNextMap)
					{
						break;
					}
					short iD = message.reader().readShort();
					sbyte tem = message.reader().readByte();
					MainObject mainObject = MainObject.get_Object(iD, tem);
					if (mainObject == null || (GameCanvas.lowGraphic && player != mainObject && MainObject.getDistance(player.x, player.y, mainObject.x, mainObject.y) >= 240))
					{
						vecObjFire.removeElementAt(i);
						i--;
						continue;
					}
					if (mainObject.timeDragon <= 0)
					{
						mainObject.frameOneStep = 20;
					}
					mainObject.timeDragon = 150;
					mainObject.Hp = message.reader().readInt();
					mainObject.Mp = message.reader().readInt();
					short typeEff = message.reader().readShort();
					sbyte b = message.reader().readByte();
					if (b <= 0)
					{
						vecObjFire.removeElementAt(i);
						i--;
						continue;
					}
					mVector mVector2 = new mVector();
					for (int j = 0; j < b; j++)
					{
						short id = message.reader().readShort();
						sbyte tem2 = message.reader().readByte();
						Object_Effect_Skill object_Effect_Skill = new Object_Effect_Skill(id, tem2);
						int hpShow = message.reader().readInt();
						int hpMagic = 0;
						if (message.isOld == 0)
						{
							hpMagic = message.reader().readInt();
						}
						int hpLast = message.reader().readInt();
						sbyte b2 = message.reader().readByte();
						object_Effect_Skill.mEffTypePlus = new int[b2];
						object_Effect_Skill.mEff_HP_Plus = new int[b2];
						object_Effect_Skill.mEff_Time_Plus = new int[b2];
						for (int k = 0; k < b2; k++)
						{
							object_Effect_Skill.mEffTypePlus[k] = message.reader().readShort();
							object_Effect_Skill.mEff_HP_Plus[k] = message.reader().readShort();
							object_Effect_Skill.mEff_Time_Plus[k] = message.reader().readShort();
						}
						object_Effect_Skill.setHP(hpShow, hpLast, hpMagic);
						mVector2.addElement(object_Effect_Skill);
					}
					MainSkill skill = new MainSkill(-1, typeEff);
					mainObject.addSkillFire(skill, mVector2);
				}
				catch (Exception)
				{
				}
				vecObjFire.removeElementAt(i);
				i--;
			}
		}
		catch (Exception)
		{
		}
	}

	public static void addHightDataeff(short type, int x, int y)
	{
		DataSkillEff o = new DataSkillEff(type, x, y);
		vecHighDataEff.addElement(o);
	}

	public static void addHightDataeff(short type, int x, int y, bool changeFlip)
	{
		DataSkillEff o = new DataSkillEff(type, x, y, changeFlip);
		vecHighDataEff.addElement(o);
	}

	public static void addHightDataeff(short type, int x, int y, bool changeFlip, int rotate)
	{
		DataSkillEff o = new DataSkillEff(type, x, y, changeFlip, rotate);
		vecHighDataEff.addElement(o);
	}

	public static ObjMove getObjMove(short id, sbyte cat)
	{
		for (int i = 0; i < vecObjMove.size(); i++)
		{
			ObjMove objMove = (ObjMove)vecObjMove.elementAt(i);
			if (!objMove.isRemove && objMove.id == id && objMove.typeObj == cat)
			{
				return objMove;
			}
		}
		return null;
	}

	public static MainObject findObjByID(short id)
	{
		for (int i = 0; i < vecPlayers.size(); i++)
		{
			MainObject mainObject = (MainObject)vecPlayers.elementAt(i);
			if (mainObject != null && mainObject.ID == id)
			{
				return mainObject;
			}
		}
		return null;
	}

	public void resetRecAlpha()
	{
		GameCanvas.gameScr.colorRec = 0;
		GameCanvas.gameScr.xRec = 0;
		GameCanvas.gameScr.yRec = 0;
		GameCanvas.gameScr.wRec = 0;
		GameCanvas.gameScr.hRec = 0;
		GameCanvas.gameScr.isFullScreen = false;
	}
}
