using System;
using UnityEngine;

public class LoginScreen : MainScreen
{
	public sbyte type;

	public static bool isCheckData = false;

	public const sbyte LOGIN = 0;

	public const sbyte REGISTER = 1;

	public TField tfUser;

	public TField tfPass;

	public TField tfIp;

	private iCommand cmdLogin;

	private iCommand cmdMenu;

	private iCommand cmdRegister;

	private iCommand cmdExitGame;

	private iCommand cmdLowGraphic;

	private iCommand cmdFristLogin;

	private iCommand cmdHelp;

	private iCommand cmdLastLogin;

	private iCommand cmdDelRMS;

	private iCommand cmdOffBg;

	private iCommand cmdTaitho;

	private iCommand cmdOnLowGraphicPc;

	public static iCommand cmdSound;

	private mVector vecCmd = new mVector();

	public static int yBeginPaint = 0;

	private int hItem = 32;

	private int idSelect;

	private int sizeListServer;

	private const int MAX_VISIBLE_ROWS = 4;

	private int serverScrollIndex = 0;

	private int yLechNameServer;

	private InputDialog input;

	public static mVector vecEff = new mVector("LoginScreen.vecEff");

	public static mVector vecOBJ = new mVector("LoginScreen.vecOBJ");

	private static bool isNewShow = false;

	private static bool isLoadDataCharShowOk = false;

	private string nameDevice = Environment.MachineName;

	public static int yPaintLogo;

	public static int dy = 0;

	private int wShowPaper;

	private int maxWShow = 180;

	private int vShow = 6;

	private int hShowPaper = 85;

	public static int hLogo = 50;

	public static int frameBanhlai = 0;

	public static int tickBanhLai;

	public static short idRan = 0;

	public void clampServerScroll()
	{
		int total = Math.Max(0, sizeListServer);
		int maxIndex = Math.Max(0, total - Math.Min(total, MAX_VISIBLE_ROWS));
		if (serverScrollIndex < 0) serverScrollIndex = 0;
		if (serverScrollIndex > maxIndex) serverScrollIndex = maxIndex;
	}

	public LoginScreen()
	{
		if (GameCanvas.isTouch)
		{
			hShowPaper = 120 + hItem + 2;
			hItem = 40;
			yLechNameServer = 4;
		}
		else
		{
			hShowPaper = 85 + hItem + 2;
		}
		UpdateServer.loadServers();
		if (GameCanvas.strListServer != null
			&& GameCanvas.language >= 0
			&& GameCanvas.language < GameCanvas.strListServer.Length
			&& GameCanvas.strListServer[GameCanvas.language] != null)
		{
			sizeListServer = GameCanvas.strListServer[GameCanvas.language].Length;
		}
		else
		{
			sizeListServer = 0;
		}
		clampServerScroll();
		yBeginPaint = MotherCanvas.hh - hShowPaper / 2 + 23;
		tfUser = new TField(MotherCanvas.hw - 70, yBeginPaint + 17, 140);
		tfUser.setStringNull(T.tfUserNull);
		tfIp = new TField(MotherCanvas.hw - 70, yBeginPaint + 17 - hItem, 140);
		tfIp.setText("");
		tfPass = new TField(MotherCanvas.hw - 70, yBeginPaint + 17 + hItem, 140);
		tfPass.setIputType(TField.INPUT_TYPE_PASSWORD);
		tfPass.setStringNull(T.tfPassNull);
		cmdFristLogin = new iCommand(T.newGame, 7, this);
		cmdLogin = new iCommand(T.login, 0, this);
		tfUser.cmdDoneAction = cmdLogin;
		tfPass.cmdDoneAction = cmdLogin;
		cmdMenu = new iCommand(T.menu, 1, this);
		cmdRegister = new iCommand(T.register, 3, this);
		cmdExitGame = new iCommand(T.exit, 4, this);
		cmdLowGraphic = new iCommand(T.on + T.lowGraphic, 6, this);
		cmdLastLogin = new iCommand(T.loadGame, 12, this);
		cmdSound = new iCommand(T.amthanh, 13, this);
		cmdHelp = new iCommand(T.hotro, 10, this);
		cmdDelRMS = new iCommand(T.delRMS, 15, this);
		cmdOffBg = new iCommand(T.offBg, 18, this);
		if (GameMidlet.isPC)
		{
			cmdOnLowGraphicPc = new iCommand(T.lowGraphic, 20, this);
		}
		if (GameCanvas.lowGraphic)
		{
			cmdLowGraphic.caption = T.off + T.lowGraphic;
		}
		cmdTaitho = new iCommand(T.on + T.taitho, 19, this);
		if (GameCanvas.isTaiTho)
		{
			cmdLowGraphic.caption = T.off + T.taitho;
		}
		cmdLogin = AvMain.setPosCMD(cmdLogin, 0);
		cmdRegister = AvMain.setPosCMD(cmdRegister, 0);
		cmdMenu = AvMain.setPosCMD(cmdMenu, 1);
		if (GameCanvas.isTouchNoOrPC())
		{
			tfUser.setFocus(isFocus: true);
		}
		updatePosCmd();
		setCmdClear();
		sbyte[] array = GameMidlet.loadRMS("Main_IPNEW");
		if (array != null)
		{
			try
			{
				DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
				GameCanvas.strIP = dataInputStream.readUTF();
				dataInputStream.close();
			}
			catch (Exception)
			{
			}
		}
		if (yPaintLogo == 0)
		{
			yPaintLogo = hLogo;
		}
	}

	public void updatePosCmd()
	{
		yBeginPaint = MotherCanvas.hh - hShowPaper / 2 + 23;
		if (tfUser != null)
		{
			tfUser.x = MotherCanvas.hw - 70;
			tfUser.y = yBeginPaint + 17;
		}
		if (tfIp != null)
		{
			tfIp.x = MotherCanvas.hw - 70;
			tfIp.y = yBeginPaint + 17 - hItem;
		}
		if (tfPass != null)
		{
			tfPass.x = MotherCanvas.hw - 70;
			tfPass.y = yBeginPaint + 17 + hItem;
		}
		if (AvMain.fraIconMenu == null || AvMain.fraIconMenu.imgFrame == null || AvMain.fraIconMenu.imgFrame.image == null)
		{
			AvMain.fraIconMenu = new FrameImage(mImage.createImage("/point/iconmenu.png"), 30, 30);
		}
		if (AvMain.fraIconHome == null || AvMain.fraIconHome.imgFrame == null || AvMain.fraIconHome.imgFrame.image == null)
		{
			AvMain.fraIconHome = new FrameImage(mImage.createImage("/point/iconhome.png"), 30, 30);
		}
		if (GameCanvas.isTouch)
		{
			if (GameCanvas.isTaiTho)
			{
				cmdMenu.setPos(30, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
				cmdHelp.setPos(MotherCanvas.w - 30, MotherCanvas.h - 15, AvMain.fraIconHome, "");
			}
			else
			{
				cmdMenu.setPos(15, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
				cmdHelp.setPos(MotherCanvas.w - 15, MotherCanvas.h - 15, AvMain.fraIconHome, "");
			}
			right = cmdHelp;
			cmdLogin.setPos(MotherCanvas.hw, yBeginPaint + hShowPaper, AvMain.fraBtLogin, "");
			cmdLogin.isPlayframe = true;
			cmdRegister.setPosXY(MotherCanvas.hw, yBeginPaint + hShowPaper - iCommand.hButtonCmdNor + 8);
		}
		else
		{
			cmdLogin = AvMain.setPosCMD(cmdLogin, 0);
			cmdRegister = AvMain.setPosCMD(cmdRegister, 0);
			cmdMenu = AvMain.setPosCMD(cmdMenu, 1);
		}
		left = cmdMenu;
		menuCMD = cmdMenu;
	}

	public override void Show(MainScreen last)
	{
		Show();
	}

	public override void setxyPlus12()
	{
		GameCanvas.xPlus12 = 2;
		GameCanvas.yPlus12 = 2;
		updatePosCmd();
	}

	public override void Show()
	{
		if (GameScreen.vecHelp != null)
		{
			GameScreen.vecHelp.removeAllElements();
		}
		if (GameCanvas.mapBack == null)
		{
			GameCanvas.mapBack = new MapBackGround();
		}
		GameCanvas.mapBack.setBGLogin();
		GameScreen.player = null;
		Session_ME.gI().close();
		type = 0;
		setCmd();
		wShowPaper = 5;
		if (GameCanvas.currentScreen != null && GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.fristLoginScr)
		{
			beginShowChar();
		}
		loadCharPart();
		updatePosCmd();
		base.Show();
		mSound.playMus(3, mSound.volumeMusic, loop: true);
		isNewShow = true;
	}

	public static void beginShowChar()
	{
		vecOBJ.removeAllElements();
		vecEff.removeAllElements();
	}

	public static void loadCharPart()
	{
	}

	public void setCmd()
	{
		if (type == 0)
		{
			center = cmdLogin;
		}
		else
		{
			center = cmdRegister;
		}
	}

	public override void commandPointer(int index, int subIndex)
	{
		if (index >= 100 && index < 100 + UIThemeManager.TOTAL_THEMES)
		{
			int selTheme = index - 100;
			UIThemeManager.setTheme(selTheme);
			GameCanvas.menu.doCloseMenu();
			return;
		}
		if (index == 99)
		{
			openSelectThemeMenu();
			return;
		}
		if (index == 98)
		{
			UIShowcaseScreen.getInstance().Show(this);
			return;
		}
		if (index == 22)
		{
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			setIndexServer(subIndex);
			GameCanvas.menu.doCloseMenu();
			return;
		}
		switch (index)
		{
		case 0:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			if (tfUser.getText().Trim().Length > 0 && tfPass.getText().Trim().Length > 0)
			{
				ListChar_Screen.IndexCharSelected = -1;
				doLogin(isGetData: true, 0, tfUser.getText(), tfPass.getText());
			}
			else
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.checkRegister1);
			}
			break;
		case 1:
		{
			mVector mVector2 = new mVector();
			SaveRms.userLast = "";
			GameCanvas.saveRms.loadUserLast();
			if (SaveRms.userLast.Length > 0 && CRes.loadRMS("MAIN_user_pass") == null)
			{
				cmdLastLogin.caption = T.loadGame + " " + SaveRms.userLast;
				mVector2.addElement(cmdLastLogin);
			}
			mVector2.addElement(cmdFristLogin);
			mVector2.addElement(new iCommand(T.server ?? "Chọn máy chủ", 27, this));
			if (!GameCanvas.isSuperLowGraphic)
			{
				mVector2.addElement(cmdLowGraphic);
			}
			mVector2.addElement(cmdHelp);
			mVector2.addElement(cmdSound);
			cmdOffBg.caption = T.on + T.offBg;
			if (!GameCanvas.isOffBg)
			{
				cmdOffBg.caption = T.off + T.offBg;
			}
			mVector2.addElement(cmdOffBg);
			if (GameCanvas.isTouch)
			{
				cmdTaitho.caption = T.on + T.taitho;
				if (GameCanvas.isTaiTho)
				{
					cmdTaitho.caption = T.off + T.taitho;
				}
				mVector2.addElement(cmdTaitho);
			}
			mVector2.addElement(cmdDelRMS);
			mVector2.addElement(new iCommand("🔑 Nhập Key Bản Quyền", 23, this));
			if (KeyAuthManager.isAuthorized)
			{
				mVector2.addElement(new iCommand("+ Thêm IP Server", 25, this));
			}
			mVector2.addElement(new iCommand("\uD83C\uDFA8 " + (UIThemeManager.getCurrentTheme() != null ? UIThemeManager.getCurrentTheme().displayName : "Đổi Giao Diện"), 99, this));
			mVector2.addElement(new iCommand("\uD83D\uDDA5 UI Showcase", 98, this));
			mVector2.addElement(cmdExitGame);
			GameCanvas.menu.startAt(mVector2, 0, T.menu);
			break;
		}
		case 2:
			if (type == 0)
			{
				type = 1;
			}
			else
			{
				type = 0;
			}
			setCmd();
			break;
		case 3:
			if (tfUser.getText().Trim().Length > 0 && tfPass.getText().Trim().Length > 0)
			{
				GameCanvas.connect();
				GlobalService.gI().Register(tfUser.getText(), tfPass.getText());
				GameCanvas.clearAll();
			}
			else
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.checkRegister1);
			}
			break;
		case 4:
			GameCanvas.Start_Normal_DiaLog(T.hoiThoat, new iCommand(T.exit, 5, this), isCmdClose: true);
			break;
		case 5:
			GameMidlet.instance.destroy();
			break;
		case 6:
			GameCanvas.lowGraphic = !GameCanvas.lowGraphic;
			cmdLowGraphic.caption = T.on + T.lowGraphic;
			if (GameCanvas.lowGraphic)
			{
				cmdLowGraphic.caption = T.off + T.lowGraphic;
			}
			try
			{
				CRes.saveRMS("SUB_LOWGRAPHIC", new sbyte[1] { (sbyte)(GameCanvas.lowGraphic ? 1 : 0) });
			}
			catch (Exception)
			{
			}
			LoadImageStatic.LoadLowGraphic();
			break;
		case 7:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			ListChar_Screen.IndexCharSelected = -1;
			GameCanvas.loginScr.doLogin(isGetData: true, 1, "", "");
			break;
		case 8:
		case 9:
			KeyAuthManager.checkAuthorizedOrNotice();
			break;
		case 10:
		{
			mVector mVector3 = new mVector();
			mVector3.addElement(new iCommand(T.home, 11, 0, this));
			mVector3.addElement(new iCommand(T.forum, 11, 1, this));
			mVector3.addElement(new iCommand(T.fanpage, 11, 4, this));
			mVector3.addElement(new iCommand(T.changePassword, 11, 2, this));
			mVector3.addElement(new iCommand(T.getPassword, 11, 3, this));
			if (GameMidlet.DEVICE == 6)
			{
				mVector3.addElement(new iCommand(T.rate, 11, 5, this));
			}
			GameCanvas.menu.startAt(mVector3, 2, T.hotro);
			break;
		}
		case 11:
			switch (subIndex)
			{
			case 0:
				GameMidlet.openUrl("http://teamobi.com");
				break;
			case 1:
				GameMidlet.openUrl("http://forum.teamobi.com");
				break;
			case 2:
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Chức năng đổi mật khẩu thực hiện tại trang chủ: http://teamobi.com");
				break;
			case 3:
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Chức năng lấy lại mật khẩu thực hiện tại trang chủ: http://teamobi.com");
				break;
			case 4:
				GameMidlet.openUrl("https://www.facebook.com/haitactihon");
				break;
			case 5:
				GameMidlet.openUrl("http://teamobi.com");
				break;
			}
			break;
		case 12:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			GameCanvas.fristLoginScr.cmdBegin.perform();
			break;
		case 13:
		{
			MsgSound msgSound = new MsgSound();
			msgSound.setinfoSound();
			GameCanvas.Start_Current_Dialog(msgSound);
			break;
		}
		case 14:
		case 17:
			KeyAuthManager.checkAuthorizedOrNotice();
			break;
		case 15:
			GameCanvas.Start_Normal_DiaLog(T.closeDelRMS, new iCommand(T.del, 16, this), isCmdClose: true);
			break;
		case 16:
			isCheckData = false;
			GameMidlet.delAllRms();
			GameCanvas.end_Dialog();
			if (GameMidlet.DEVICE == 2)
			{
				GameCanvas.updateImageAndroidScr = new UpdateImageScreen();
				GameCanvas.updateImageAndroidScr.Show();
			}
			else
			{
				setData();
			}
			break;
		case 18:
			GameCanvas.isOffBg = !GameCanvas.isOffBg;
			try
			{
				CRes.saveRMS("SUB_OFFBG", new sbyte[1] { (sbyte)(GameCanvas.isOffBg ? 1 : 0) });
			}
			catch (Exception)
			{
			}
			break;
		case 19:
			GameCanvas.isTaiTho = !GameCanvas.isTaiTho;
			try
			{
				CRes.saveRMS("SUB_TAITHO", new sbyte[1] { (sbyte)(GameCanvas.isTaiTho ? 1 : 0) });
			}
			catch (Exception)
			{
			}
			GameScreen.interfaceGame.setPosMenu_TaiTho();
			break;
		case 20:
			if (GameMidlet.isPC)
			{
				GameCanvas.Start_Normal_DiaLog(T.confirmChangeGraphicsPC, new iCommand(T.confirmYes, 21, this), isCmdClose: true);
			}
			break;
		case 21:
			if (GameMidlet.isPC)
			{
				GameCanvas.lv = ((GameCanvas.lv == 0) ? 1 : 0);
				saveLvGraphicPC();
				GameCanvas.end_Cur_Dialog();
				Application.Quit();
			}
			break;
		case 23:
			input = new InputDialog();
			input.setinfo("Nhập Key Bản Quyền:", new iCommand("Check Key", 24, this), isNum: false, "MÃ KEY");
			if (input.tfInput != null)
			{
				input.tfInput.setText(KeyAuthManager.getActiveKey());
			}
			GameCanvas.currentDialog = input;
			break;
		case 24:
			if (input != null && input.tfInput != null)
			{
				string textKey = input.tfInput.getText().Trim();
				GameCanvas.end_Dialog();
				KeyAuthManager.checkAndActivateKey(textKey);
			}
			break;
		case 25:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			input = new InputDialog();
			input.setinfo("Nhập Tên:IP:Port hoặc IP:Port\nVD: SV1:127.0.0.1:2239", new iCommand("OK", 26, this), isNum: false, "IP-PORT");
			GameCanvas.currentDialog = input;
			break;
		case 26:
			if (input != null && input.tfInput != null)
			{
				string str = input.tfInput.getText().Trim();
				GameCanvas.end_Dialog();
				if (!string.IsNullOrEmpty(str))
				{
					bool ok = UpdateServer.parseAndAddCustomServer(str);
					if (ok)
					{
						setIndexServer(UpdateServer.serverHosts.Count - 1);
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Đã thêm máy chủ thành công!\n" + UpdateServer.getCurrentServerName());
					}
					else
					{
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Định dạng không hợp lệ!\nVí dụ: SV1:127.0.0.1:2239");
					}
				}
			}
			break;
		case 27:
			openSelectServerMenu();
			break;
		}
		base.commandPointer(index, subIndex);
	}

	private void saveLvGraphicPC()
	{
		Rms.saveRMSInt2("RMS_Graphics_PC", GameCanvas.lv);
	}

	private void saveIP_New()
	{
		if (GameCanvas.strIP.Length == 0)
		{
			GameMidlet.delRMS("Main_IPNEW");
			return;
		}
		string[] array = mFont.split(GameCanvas.strIP, "-");
		GameCanvas.Start_Normal_Only_CmdClose_DiaLog("IP:" + array[0] + "\nPORT:" + array[1]);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try
		{
			dataOutputStream.writeUTF(GameCanvas.strIP);
			GameMidlet.saveRMS("Main_IPNEW", byteArrayOutputStream.toByteArray());
			dataOutputStream.close();
		}
		catch (Exception)
		{
		}
	}

	public override void paint(mGraphics g)
	{
		try
		{
			if (GameCanvas.mapBack != null)
			{
				GameCanvas.mapBack.paintBgLogin(g);
				GameCanvas.mapBack.paintObjFristLogin(g);
				GameCanvas.mapBack.paintObjLastLogin(g);
			}
		}
		catch (Exception)
		{
		}
		paintShowchar(g);
		paintPaper(g, MotherCanvas.hw - wShowPaper / 2, yBeginPaint, wShowPaper, hShowPaper, maxWShow, AvMain.PAPER_NORMAL);
		GameCanvas.resetTrans(g);
		paintLogo(g, MotherCanvas.hw);
		mFont.tahoma_7_black.drawString(g, "Ver: " + ClientConfig.CLIENT_VERSION, MotherCanvas.w - 2, 2 + GameScreen.h12plus, 1);
		mFont.tahoma_7_black.drawString(g, "ID: " + ClientConfig.CLIENT_ID, MotherCanvas.w - 2, 4 + GameScreen.h12plus + GameCanvas.hText / 2, 1);
		GameCanvas.resetTrans(g);
		g.setClip(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		g.saveCanvas();
		g.ClipRec(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		tfUser.paint(g, MotherCanvas.hw - wShowPaper / 2 < tfUser.x);
		g.restoreCanvas();
		g.setClip(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		g.saveCanvas();
		g.ClipRec(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		tfPass.paint(g, MotherCanvas.hw - wShowPaper / 2 < tfPass.x);
		g.restoreCanvas();
		GameCanvas.resetTrans(g);
		paintListServer(g);
		if (idSelect != 2 && idSelect != -2)
		{
			if (GameCanvas.isTouch && !GameCanvas.lowGraphic)
			{
				AvMain.fraBtBanhlai.drawFrame(frameBanhlai, cmdLogin.xCmd, cmdLogin.yCmd, 0, 3, g);
			}
			base.paint(g);
		}
	}

	public static void paintLogo(mGraphics g, int x)
	{
		if (AvMain.imgLg == null)
		{
			LoadImageStatic.loadImageLanguage();
		}
		g.drawImage(AvMain.imgLg, x, yPaintLogo, 3);
		if (GameCanvas.language == 0 && !GameCanvas.lowGraphic && !GameCanvas.isDeviceStore() && GameCanvas.IndexServer < AvMain.fraIconServer.nFrame)
		{
			if (MotherCanvas.h >= 240)
			{
				AvMain.fraIconServer.drawFrame(GameCanvas.IndexServer, x + 35, yPaintLogo + 20, 0, 3, g);
			}
			else
			{
				Interface_Game.fraBorderNoti.drawFrame(GameCanvas.IndexServer + 1, x + 34, yPaintLogo + 17, 0, 3, g);
			}
		}
	}

	public static void updateYPaintLogo(int y)
	{
		if (yPaintLogo > y)
		{
			yPaintLogo -= 2;
			if (yPaintLogo < y)
			{
				yPaintLogo = y;
			}
		}
		else if (yPaintLogo < y)
		{
			yPaintLogo += 2;
			if (yPaintLogo > y)
			{
				yPaintLogo = y;
			}
		}
	}

	public void paintListServer(mGraphics g)
	{
		UpdateServer.loadServers();
		AvMain.paintRect(g, tfPass.x, tfPass.y + hItem, tfPass.width, tfPass.height, 0, (idSelect != -2) ? 1 : 0);
		string curr = T.server + " " + UpdateServer.getCurrentServerName();
		mFont.tahoma_7b_black.drawString(g, curr, tfPass.x + 4, tfPass.y + tfPass.height - 16 + hItem - yLechNameServer, 0);
		g.drawRegion(AvMain.imgArrowListServer, 0, ((idSelect != -2) ? 1 : 0) * 10, 15, 10, 0, tfPass.x + tfPass.width - 12, tfPass.y + hItem + tfPass.height / 2, 3);
		if (UpdateServer.isServerNew(GameCanvas.IndexServer) && AvMain.fraNew != null)
		{
			int tw = mFont.tahoma_7b_black.getWidth(curr);
			AvMain.fraNew.drawFrame(GameCanvas.gameTick / 5 % AvMain.fraNew.nFrame,
					tfPass.x + 4 + tw + 16,
					tfPass.y + hItem + tfPass.height / 2,
					0, 3, g);
		}
	}

	public static void paintShowchar(mGraphics g)
	{
		CRes.quickSort(vecOBJ);
		for (int i = 0; i < vecOBJ.size(); i++)
		{
			MainObject obj = (MainObject)vecOBJ.elementAt(i);
			obj.paint(g);
			obj.ySort = obj.y;
		}
		for (int j = 0; j < vecEff.size(); j++)
		{
			((MainEffect)vecEff.elementAt(j)).paint(g);
		}
	}

	public static void updateCharShow()
	{
		for (int i = 0; i < vecOBJ.size(); i++)
		{
			MainObject mainObject = (MainObject)vecOBJ.elementAt(i);
			mainObject.updateLoginShow();
			if (mainObject.isRemove)
			{
				vecOBJ.removeElement(mainObject);
				i--;
			}
		}
		for (int j = 0; j < vecEff.size(); j++)
		{
			MainEffect mainEffect = (MainEffect)vecEff.elementAt(j);
			mainEffect.update();
			if (mainEffect.isStop)
			{
				vecEff.removeElement(mainEffect);
				j--;
			}
		}
		if (CRes.random(20) == 0 && vecOBJ.size() < MotherCanvas.w / 80)
		{
			addObjShow(isNew: false);
		}
		if (isNewShow)
		{
			isNewShow = false;
			for (int k = 0; k < 3; k++)
			{
				addObjShow(isNew: true);
			}
		}
	}

	public override void update()
	{
		updateYPaintLogo(yBeginPaint / 2);
		updateBanhLai();
		if (wShowPaper < maxWShow)
		{
			wShowPaper += vShow;
			if (wShowPaper > maxWShow)
			{
				wShowPaper = maxWShow;
				vShow = 15;
			}
			if (vShow < 100)
			{
				vShow += 15;
				if (vShow > 100)
				{
					vShow = 100;
				}
			}
		}
		if (GameCanvas.mapBack != null)
		{
			GameCanvas.mapBack.updateCloudLogin();
		}
		tfIp.update();
		tfUser.update();
		tfPass.update();
		updateCharShow();
		if (MsgDialog.isAuroReconect && GameCanvas.gameTick % 100 == 0 && (GameCanvas.currentDialog == null || GameCanvas.currentDialog.type != 9))
		{
			string info = T.disconnect;
			if (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.Length > 10)
			{
				info = GameCanvas.infoDisConnect;
				GameCanvas.infoDisConnect = "";
			}
			mVector mVector2 = new mVector();
			mVector2.addElement(GameScreen.cmdReConnect);
			mVector2.addElement(GameCanvas.gameScr.cmdExit);
			GameCanvas.Start_ReConect_DiaLog(info, mVector2, isCmdClose: false);
		}
	}

	public static void updateBanhLai()
	{
		int num = CRes.random(20);
		if (tickBanhLai < 40)
		{
			tickBanhLai++;
			if (tickBanhLai == 40)
			{
				if (frameBanhlai != 1)
				{
					frameBanhlai = 1;
				}
				tickBanhLai = 0;
			}
		}
		if (num == 0)
		{
			if (frameBanhlai == 0 || frameBanhlai == 2)
			{
				frameBanhlai = 1;
			}
			else if (CRes.random(2) == 0)
			{
				frameBanhlai = 0;
			}
			else
			{
				frameBanhlai = 2;
			}
			tickBanhLai = 0;
		}
	}

	public override void updatekey()
	{
		if (idSelect == -2)
		{
			if (GameCanvas.keyMyHold[5])
			{
				openSelectServerMenu();
				GameCanvas.clearKeyPressed(5);
				GameCanvas.clearKeyHold(5);
			}
		}
		if (GameCanvas.keyMyHold[8])
		{
			if (idSelect == -2)
			{
				if (GameCanvas.isTouchNoOrPC())
				{
					tfUser.setFocus(isFocus: true);
				}
				tfPass.setFocus(isFocus: false);
				idSelect = 0;
			}
			else if (tfUser.isFocused())
			{
				tfUser.setFocus(isFocus: false);
				if (GameCanvas.isTouchNoOrPC())
				{
					tfPass.setFocus(isFocus: true);
				}
			}
			else if (tfPass.isFocused())
			{
				tfUser.setFocus(isFocus: false);
				tfPass.setFocus(isFocus: false);
				idSelect = -2;
			}
			GameCanvas.clearKeyHold(8);
			setCmdClear();
		}
		else if (GameCanvas.keyMyHold[2])
		{
			if (idSelect == -2)
			{
				tfUser.setFocus(isFocus: false);
				if (GameCanvas.isTouchNoOrPC())
				{
					tfPass.setFocus(isFocus: true);
				}
				idSelect = 0;
			}
			else if (tfUser.isFocused())
			{
				tfUser.setFocus(isFocus: false);
				tfPass.setFocus(isFocus: false);
				idSelect = -2;
			}
			else if (tfPass.isFocused())
			{
				if (GameCanvas.isTouchNoOrPC())
				{
					tfUser.setFocus(isFocus: true);
				}
				tfPass.setFocus(isFocus: false);
			}
			GameCanvas.clearKeyHold(2);
			setCmdClear();
		}
		base.updatekey();
		updatekeyPC();
	}

	public void openSelectServerMenu()
	{
		mVector vec = new mVector();
		int lang = GameCanvas.language;
		int currentRelIdx = -1;
		int relCount = 0;
		for (int i = 0; i < UpdateServer.serverHosts.Count; i++)
		{
			if (UpdateServer.serverLang.Count > i && UpdateServer.serverLang[i] == lang)
			{
				string sName = UpdateServer.getServerName(i);
				iCommand cmd = new iCommand(sName, 22, i, this);
				cmd.isNew = UpdateServer.isServerNew(i);
				vec.addElement(cmd);
				if (i == GameCanvas.IndexServer)
				{
					currentRelIdx = relCount;
				}
				relCount++;
			}
		}
		if (KeyAuthManager.isAuthorized)
		{
			iCommand cmdAdd = new iCommand("+ Thêm IP Server", 25, this);
			vec.addElement(cmdAdd);
		}
		iCommand cmdKey = new iCommand("🔑 Nhập Key Bản Quyền", 23, this);
		vec.addElement(cmdKey);

		GameCanvas.menu.startAt(vec, 2, T.server);
		if (currentRelIdx >= 0)
		{
			GameCanvas.menu.menuSelectedItem = currentRelIdx;
		}
	}

	public void onServersUpdated()
	{
		if (GameCanvas.strListServer != null
			&& GameCanvas.language >= 0
			&& GameCanvas.language < GameCanvas.strListServer.Length
			&& GameCanvas.strListServer[GameCanvas.language] != null)
		{
			sizeListServer = GameCanvas.strListServer[GameCanvas.language].Length;
		}
		else
		{
			sizeListServer = 0;
		}

		if (GameCanvas.IndexServer < 0 || GameCanvas.IndexServer >= UpdateServer.serverHosts.Count)
		{
			int lang = GameCanvas.language;
			for (int i = 0; i < UpdateServer.serverLang.Count; i++)
			{
				if (UpdateServer.serverLang[i] == lang)
				{
					GameCanvas.IndexServer = i;
					break;
				}
			}
		}

		if (GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < UpdateServer.serverHosts.Count)
		{
			GameCanvas.hostServer = UpdateServer.getHost(GameCanvas.IndexServer);
			GameCanvas.portServer = UpdateServer.getPort(GameCanvas.IndexServer);
		}

		clampServerScroll();
	}

	public override void updatePointer()
	{
		if (GameCanvas.isPointSelect(tfPass.x, tfPass.y + hItem, tfPass.width, tfPass.height))
		{
			openSelectServerMenu();
			GameCanvas.isPointerSelect = false;
		}
		base.updatePointer();
		tfUser.updatePointer();
		tfPass.updatePointer();
	}

	public void openSelectThemeMenu()
	{
		mVector vec = new mVector();
		string[] names = UIThemeManager.getThemeNames();
		int curTheme = UIThemeManager.getCurrentThemeId();
		for (int i = 0; i < names.Length; i++)
		{
			iCommand cmd = new iCommand(names[i], 100 + i, this);
			vec.addElement(cmd);
		}
		GameCanvas.menu.startAt(vec, 2, "Ch\u1ecdn Giao Di\u1ec7n");
		if (curTheme >= 0 && curTheme < names.Length)
		{
			GameCanvas.menu.menuSelectedItem = curTheme;
		}
	}

	public void setIndexServer(int index)
	{
		int globalIndex = UpdateServer.relativeToGlobal(index, GameCanvas.language);
		if (globalIndex != -1)
		{
			if (Session_ME.gI().isConnected())
			{
				Session_ME.gI().close();
			}
			UpdateServer.setServerIndex(globalIndex);
		}
	}

	public static sbyte pendingTypeLogin = -1;
	public static string pendingUserLogin = "";
	public static string pendingPassLogin = "";

	public void doLogin(bool isGetData, sbyte typeLogin, string user, string pass)
	{
		pendingTypeLogin = typeLogin;
		pendingUserLogin = user != null ? user : "";
		pendingPassLogin = pass != null ? pass : "";

		if (typeLogin == 0)
		{
			if (tfUser != null) tfUser.setText(pendingUserLogin);
			if (tfPass != null) tfPass.setText(pendingPassLogin);
		}

		GameCanvas.connect();
		if (isGetData)
		{
			setData();
		}
		if (!isCheckData)
		{
			GlobalService.gI().CheckVersion();
			GameCanvas.Start_Waiting_Connect_DiaLog(T.checkVersion, isCmdClose: true);
		}
		else
		{
			GlobalService.gI().Login(user, pass, typeLogin);
			GameCanvas.Start_Waiting_Connect_DiaLog(T.doingLogin, isCmdClose: true);
			if (typeLogin >= 0)
			{
				Player.StepAutoRe = 3;
			}
		}
		if (GameMidlet.DEVICE == 4)
		{
			ObjectData.checkDelHash(ObjectData.HashImageCharPart, 120, isTrue: true);
		}
	}

	public override void keyPress(int keyCode)
	{
		if (tfUser.isFocused())
		{
			tfUser.keyPressed(keyCode);
		}
		else if (tfPass.isFocused())
		{
			tfPass.keyPressed(keyCode);
		}
	}

	public void readSpeedUpRMS()
	{
		sbyte[] array = GameMidlet.loadRMS(Environment.MachineName + T.speedUp);
		if (array == null)
		{
			return;
		}
		try
		{
			DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
			GameCanvas.percent = dataInputStream.readInt();
			if (GameCanvas.percent == 0)
			{
				GameCanvas.hardcodeSpeedUp = 0;
			}
			else
			{
				GameCanvas.hardcodeSpeedUp = 300;
			}
			dataInputStream.close();
		}
		catch (Exception)
		{
		}
	}

	public void setData()
	{
		GlobalService.gI().get_DATA(4);
		GlobalService.gI().get_DATA(10);
		GlobalService.gI().get_DATA(8);
		GlobalService.gI().get_DATA(21);
		GlobalService.gI().get_DATA(30);
		GlobalService.gI().Send_Data_List_Server(1);
		GlobalService.gI().Send_Data_List_Server(2);
		GlobalService.gI().get_DATA(31);
		readSpeedUpRMS();
		if (!GlobalService.isGetKichAn)
		{
			GlobalService.gI().get_DATA(26);
		}
		if (!GlobalService.isGetMaterial)
		{
			GlobalService.gI().get_DATA(11);
		}
		if (LoadMap.mSea == null)
		{
			GlobalService.gI().get_DATA(13);
		}
		if (ScreenUpgrade.mItemUpgrade == null)
		{
			GlobalService.gI().get_DATA(19);
		}
		if (GameCanvas.clockServer == 0L)
		{
			GlobalService.gI().get_DATA(17);
		}
	}

	public void setCmdClear()
	{
		if (!GameCanvas.isTouch)
		{
			if (tfUser.isFocused())
			{
				right = tfUser.cmdClear;
			}
			else if (tfPass.isFocused())
			{
				right = tfPass.cmdClear;
			}
		}
	}

	public static void addObjShow(bool isNew)
	{
		if (isLoadDataCharShowOk && !GameCanvas.lowGraphic)
		{
			idRan++;
			Other_Player other_Player = new Other_Player(idRan, 0, "", 0, 0);
			if (CRes.random(2) == 0)
			{
				other_Player.x = -CRes.random(10, 40);
				other_Player.Dir = 2;
			}
			else
			{
				other_Player.x = MotherCanvas.w + CRes.random(10, 40);
				other_Player.Dir = 0;
			}
			if (isNew)
			{
				other_Player.x = CRes.random(10, MotherCanvas.w - 10);
			}
			other_Player.setSpeed(7, 7);
			other_Player.y = MotherCanvas.h - CRes.random(10, 50);
			other_Player.xAnchor = other_Player.x;
			other_Player.toX = other_Player.x;
			other_Player.toY = other_Player.y;
			other_Player.clazz = (sbyte)(1 + CRes.random(5));
			other_Player = CreateChar_Screen.setCharClass(other_Player, isRan: true);
			other_Player.isInfo = true;
			vecOBJ.addElement(other_Player);
		}
	}

	public static void addEffectEnd(short type, int subtype, int x, int y, sbyte dir, MainObject objEff)
	{
		Effect_End o = new Effect_End(type, (sbyte)subtype, x, y, dir, objEff);
		vecEff.addElement(o);
	}

	public override bool keyBack()
	{
		if (GameCanvas.menu != null && GameCanvas.menu.isShowMenu)
		{
			GameCanvas.menu.doCloseMenu();
			return true;
		}
		if (!base.keyBack() && cmdExitGame != null && GameMidlet.DEVICE != 4)
		{
			cmdExitGame.perform();
		}
		return false;
	}
}
