using System;

public class FristLoginScreen : MainScreen
{
	public iCommand cmdBegin;

	public iCommand cmdChangeAcc;

	public iCommand cmdServer;

	public iCommand cmdNewGame;

	public iCommand cmdMenu;

	private mVector vecCmd = new mVector();

	public static string userNew = "";

	public static InputDialog input;

	public static iCommand cmdRegister;

	private int valueBegin;

	private int idCommand;



	public FristLoginScreen()
	{
		UpdateServer.loadServers();
		if (UpdateServer.serverHosts.Count > 0)
		{
			if (GameCanvas.IndexServer < 0 || GameCanvas.IndexServer >= UpdateServer.serverHosts.Count)
			{
				GameCanvas.IndexServer = 0;
			}
		}
		cmdBegin = new iCommand(T.loadGame, 0, 0, this);
		SaveRms.userLast = "";
		if (CRes.loadRMS("MAIN_user_pass") != null)
		{
			GameCanvas.saveRms.loadUserPass();
			if (GameCanvas.loginScr.tfUser != null)
			{
				string u = GameCanvas.loginScr.tfUser.getText().Trim();
				if (!string.IsNullOrEmpty(u))
				{
					SaveRms.userLast = u;
				}
			}
		}
		else if (CRes.loadRMS("MAIN_user_last") != null)
		{
			GameCanvas.saveRms.loadUserLast();
		}
		else if (CRes.loadRMS("MAIN_frist_login") != null)
		{
			try
			{
				sbyte[] data = CRes.loadRMS("MAIN_frist_login");
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				string guestName = dis.readUTF();
				if (!string.IsNullOrEmpty(guestName))
				{
					SaveRms.userLast = guestName.Trim();
					userNew = guestName.Trim();
				}
				if (dis.available() > 0)
				{
					GameCanvas.IndexServer = dis.readByte();
				}
				dis.close();
			}
			catch (Exception) {}
		}
		else
		{
			if (GameCanvas.strListServer != null
				&& GameCanvas.language >= 0
				&& GameCanvas.language < GameCanvas.strListServer.Length
				&& GameCanvas.strListServer[GameCanvas.language] != null
				&& GameCanvas.strListServer[GameCanvas.language].Length > 0)
			{
				GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].Length - 1;
			}
			else
			{
				GameCanvas.IndexServer = 0;
			}
		}
		if (GameCanvas.strListServer != null
			&& GameCanvas.language >= 0
			&& GameCanvas.language < GameCanvas.strListServer.Length
			&& GameCanvas.strListServer[GameCanvas.language] != null
			&& GameCanvas.IndexServer >= GameCanvas.strListServer[GameCanvas.language].Length)
		{
			GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].Length - 1;
		}
		cmdRegister = new iCommand(T.register, 3, this);
		cmdServer = new iCommand(T.server + "\n" + UpdateServer.getCurrentServerName(), 4, this);
		valueBegin = (GameCanvas.strListServer != null && GameCanvas.language < GameCanvas.strListServer.Length && GameCanvas.strListServer[GameCanvas.language] != null) ? (GameCanvas.strListServer[GameCanvas.language].Length - 1) * 38 : 0;
		getVecBegin();
		if (LoginScreen.yPaintLogo == 0)
		{
			LoginScreen.yPaintLogo = LoginScreen.hLogo;
		}
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
		idCommand = 0;
		if (!GameCanvas.isTouch || GameCanvas.isTouchAndKey())
		{
			for (int i = 0; i < vecCmd.size(); i++)
			{
				iCommand iCommand2 = (iCommand)vecCmd.elementAt(i);
				if (i == idCommand)
				{
					iCommand2.isSelect = true;
				}
				else
				{
					iCommand2.isSelect = false;
				}
			}
		}
		if (GameCanvas.currentScreen != null && GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.fristLoginScr)
		{
			LoginScreen.beginShowChar();
		}
		LoginScreen.loadCharPart();
		base.Show();
		mSound.playMus(3, mSound.volumeMusic, loop: true);
	}

	public override void setxyPlus12()
	{
		GameCanvas.xPlus12 = 2;
		GameCanvas.yPlus12 = 2;
	}

	public void setBeginGame()
	{
		if (CRes.loadRMS("MAIN_user_pass") != null)
		{
			GameCanvas.saveRms.loadUserPass();
			if (GameCanvas.loginScr.tfUser != null)
			{
				string u = GameCanvas.loginScr.tfUser.getText().Trim();
				if (!string.IsNullOrEmpty(u))
				{
					SaveRms.userLast = u;
				}
			}
		}
		else if (CRes.loadRMS("MAIN_user_last") != null)
		{
			GameCanvas.saveRms.loadUserLast();
		}
		else if (CRes.loadRMS("MAIN_frist_login") != null)
		{
			try
			{
				sbyte[] data = CRes.loadRMS("MAIN_frist_login");
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				string guestName = dis.readUTF();
				if (!string.IsNullOrEmpty(guestName))
				{
					SaveRms.userLast = guestName.Trim();
					userNew = guestName.Trim();
				}
				dis.close();
			}
			catch (Exception) {}
		}
		getVecBegin();
	}

	public void setNewAcc(bool isCheckDataOK)
	{
		sbyte[] array = CRes.loadRMS("MAIN_frist_login");
		if (!(array != null || isCheckDataOK))
		{
			return;
		}
		ListChar_Screen.IndexCharSelected = -1;
		userNew = "";
		try
		{
			DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
			userNew = dataInputStream.readUTF();
			if (dataInputStream.available() > 0)
			{
				GameCanvas.IndexServer = dataInputStream.readByte();
			}
		}
		catch (Exception)
		{
			userNew = "";
		}
		mSystem.outz("4");
		GameCanvas.loginScr.doLogin(isGetData: true, 1, userNew, "");
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
			openMenuLeft();
			return;
		}
		if (index == 97)
		{
			openSelectThemeMenu();
			return;
		}
		if (index == 8)
		{
			GameMidlet.instance.exit();
			return;
		}
		switch (index)
		{
		case 0:
		{
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			ListChar_Screen.IndexCharSelected = -1;

			// 1. Kiểm tra tài khoản thật đã lưu (MAIN_user_pass)
			if (CRes.loadRMS("MAIN_user_pass") != null)
			{
				GameCanvas.saveRms.loadUserPass();
				string savedUser = GameCanvas.loginScr.tfUser != null ? GameCanvas.loginScr.tfUser.getText().Trim() : "";
				string savedPass = GameCanvas.loginScr.tfPass != null ? GameCanvas.loginScr.tfPass.getText().Trim() : "";
				if (!string.IsNullOrEmpty(savedUser) && !string.IsNullOrEmpty(savedPass))
				{
					GameCanvas.loginScr.doLogin(isGetData: true, 0, savedUser, savedPass);
					break;
				}
			}

			// 2. Kiểm tra tài khoản khách / chơi nhanh đã lưu (MAIN_frist_login)
			userNew = "";
			sbyte[] array = CRes.loadRMS("MAIN_frist_login");
			if (array != null)
			{
				try
				{
					DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
					userNew = dataInputStream.readUTF();
					if (dataInputStream.available() > 0)
					{
						GameCanvas.IndexServer = dataInputStream.readByte();
					}
				}
				catch (Exception)
				{
					userNew = "";
				}
			}
			if (!string.IsNullOrEmpty(userNew))
			{
				GameCanvas.loginScr.doLogin(isGetData: true, 1, userNew, "");
				break;
			}

			// 3. Nếu chỉ có userLast mà không có pass, mở form đăng nhập để nhập pass
			if (CRes.loadRMS("MAIN_user_last") != null)
			{
				GameCanvas.saveRms.loadUserLast();
				GameCanvas.loginScr.Show();
				break;
			}

			// 4. Nếu chưa từng lưu tài khoản nào, mở form đăng nhập
			GameCanvas.loginScr.Show();
			break;
		}
		case 1:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			GameCanvas.loginScr.Show();
			break;
		case 2:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			if (CRes.loadRMS("MAIN_frist_login") != null)
			{
				GameCanvas.Start_Normal_DiaLog("Bạn đang có tài khoản chơi mới trên máy.\nNếu tạo mới sẽ thay thế tài khoản cũ. Bạn có muốn tiếp tục?", new iCommand("Đồng ý", 21, this), isCmdClose: true);
			}
			else
			{
				GameCanvas.loginScr.doLogin(isGetData: true, 1, "", "");
			}
			break;
		case 21:
			GameCanvas.end_Dialog();
			GameCanvas.loginScr.doLogin(isGetData: true, 1, "", "");
			break;
		case 3:
		{
			string[] array2 = new string[input.mtfInput.Length];
			for (int j = 0; j < array2.Length; j++)
			{
				if (input.mtfInput[j].getText().Length > 0)
				{
					array2[j] = input.mtfInput[j].getText();
				}
				else
				{
					array2[j] = "";
				}
			}
			GlobalService.gI().RegisterNew(array2);
			break;
		}
		case 4:
			openSelectServerMenu();
			break;
		case 5:
		{
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			UpdateServer.setServerIndex(subIndex);
			cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
			getVecBegin();
			GameCanvas.menu.doCloseMenu();
			break;
		}
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
						UpdateServer.setServerIndex(UpdateServer.serverHosts.Count - 1);
						cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
						getVecBegin();
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Đã thêm máy chủ thành công!\n" + UpdateServer.getCurrentServerName());
					}
					else
					{
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Định dạng không hợp lệ!\nVí dụ: SV1:127.0.0.1:2239");
					}
				}
			}
			break;
		case 6:
		case 7:
		{
			KeyAuthManager.checkAuthorizedOrNotice();
			break;
		}
		}
		base.commandPointer(index, subIndex);
	}

	public void onServersUpdated()
	{
		cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
		getVecBegin();
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
				iCommand cmd = new iCommand(sName, 5, i, this);
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

	private void getVecBegin()
	{
		vecCmd.removeAllElements();
		if (GameCanvas.language > GameCanvas.strListServer.Length - 1)
		{
			GameCanvas.language = 0;
		}
		string sName = UpdateServer.getCurrentServerName();

		if (SaveRms.userLast != null && SaveRms.userLast.Length > 0)
		{
			cmdBegin.caption = T.loadGame + "\n " + SaveRms.userLast;
		}
		else
		{
			cmdBegin.caption = T.loadGame;
		}
		cmdBegin.setTypeSpec();

		cmdNewGame = new iCommand(T.newGame, 2, 0, this);
		cmdNewGame.setTypeSpec();

		cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
		cmdChangeAcc.setTypeSpec();

		cmdServer.caption = T.server + "\n" + sName;
		cmdServer.setTypeSpec();

		// Bố cục Dynamic Ngang responsive:
		int btnW = iCommand.wButtonCmd;
		if (MotherCanvas.w >= 320)
		{
			int gap = 8;
			int totalW = 4 * btnW + 3 * gap;
			if (totalW > MotherCanvas.w - 16)
			{
				gap = System.Math.Max(2, (MotherCanvas.w - 16 - 4 * btnW) / 3);
				totalW = 4 * btnW + 3 * gap;
			}
			int startX = MotherCanvas.hw - totalW / 2 + btnW / 2;
			int posY = MotherCanvas.h - 52;

			cmdBegin.setPos(startX + 0 * (btnW + gap), posY, null, cmdBegin.caption);
			cmdNewGame.setPos(startX + 1 * (btnW + gap), posY, null, cmdNewGame.caption);
			cmdChangeAcc.setPos(startX + 2 * (btnW + gap), posY, null, cmdChangeAcc.caption);
			cmdServer.setPos(startX + 3 * (btnW + gap), posY, null, cmdServer.caption);
		}
		else
		{
			int gap = 6;
			int totalW = 2 * btnW + gap;
			int startX1 = MotherCanvas.hw - totalW / 2 + btnW / 2;
			int startX2 = startX1 + btnW + gap;
			int posY1 = MotherCanvas.h - 82;
			int posY2 = MotherCanvas.h - 44;

			cmdBegin.setPos(startX1, posY1, null, cmdBegin.caption);
			cmdNewGame.setPos(startX2, posY1, null, cmdNewGame.caption);
			cmdChangeAcc.setPos(startX1, posY2, null, cmdChangeAcc.caption);
			cmdServer.setPos(startX2, posY2, null, cmdServer.caption);
		}

		vecCmd.addElement(cmdBegin);
		vecCmd.addElement(cmdNewGame);
		vecCmd.addElement(cmdChangeAcc);
		vecCmd.addElement(cmdServer);

		if (AvMain.fraIconMenu == null || AvMain.fraIconMenu.imgFrame == null)
		{
			AvMain.fraIconMenu = new FrameImage(mImage.createImage("/point/iconmenu.png"), 30, 30);
		}
		cmdMenu = new iCommand(T.menu ?? "Menu", 99, this);
		if (GameCanvas.isTaiTho)
		{
			cmdMenu.setPos(30, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
		}
		else
		{
			cmdMenu.setPos(15, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
		}
		if (GameCanvas.isTouch)
		{
			vecCmd.addElement(cmdMenu);
		}
		left = cmdMenu;

		idCommand = 0;
		if (GameCanvas.isTouch && !GameCanvas.isTouchAndKey())
		{
			return;
		}
		for (int i = 0; i < vecCmd.size(); i++)
		{
			iCommand iCommand2 = (iCommand)vecCmd.elementAt(i);
			if (i == idCommand)
			{
				iCommand2.isSelect = true;
			}
			else
			{
				iCommand2.isSelect = false;
			}
		}
	}

	public override void paint(mGraphics g)
	{
		if (GameCanvas.mapBack != null)
		{
			GameCanvas.mapBack.paintBgLogin(g);
			GameCanvas.mapBack.paintObjFristLogin(g);
			GameCanvas.mapBack.paintObjLastLogin(g);
		}
		LoginScreen.paintShowchar(g);
		LoginScreen.paintLogo(g, MotherCanvas.hw);
		mFont.tahoma_7_black.drawString(g, "Ver: " + ClientConfig.CLIENT_VERSION, MotherCanvas.w - 2, 2 + GameScreen.h12plus, 1);
		mFont.tahoma_7_black.drawString(g, "ID: " + ClientConfig.CLIENT_ID, MotherCanvas.w - 2, 4 + GameScreen.h12plus + GameCanvas.hText / 2, 1);
		GameCanvas.resetTrans(g);
		for (int i = 0; i < vecCmd.size(); i++)
		{
			iCommand iCommand2 = (iCommand)vecCmd.elementAt(i);
			iCommand2.paint(g, iCommand2.xCmd, iCommand2.yCmd);
		}
		base.paint(g);
	}

	public override void update()
	{
		LoginScreen.updateYPaintLogo(LoginScreen.hLogo);
		LoginScreen.updateCharShow();
	}

	public override void updatekey()
	{
		int num = vecCmd.size();
		if ((!GameCanvas.isTouch || GameCanvas.isTouchAndKey()) && num > 0)
		{
			int num2 = idCommand;
			if (GameCanvas.keyMove(0))
			{
				idCommand--;
				GameCanvas.ClearkeyMove(0);
			}
			else if (GameCanvas.keyMove(2))
			{
				idCommand++;
				GameCanvas.clearKeyHold(6);
				GameCanvas.ClearkeyMove(2);
			}
			else if (MotherCanvas.w < 320 && GameCanvas.keyMove(1))
			{
				idCommand = System.Math.Max(0, idCommand - 2);
				GameCanvas.ClearkeyMove(1);
			}
			else if (MotherCanvas.w < 320 && GameCanvas.keyMove(3))
			{
				idCommand = System.Math.Min(num - 1, idCommand + 2);
				GameCanvas.ClearkeyMove(3);
			}
			idCommand = AvMain.resetSelect(idCommand, num - 1, isreset: false);
			if (num2 != idCommand && (!GameCanvas.isTouch || GameCanvas.isTouchAndKey()))
			{
				for (int i = 0; i < num; i++)
				{
					iCommand iCommand2 = (iCommand)vecCmd.elementAt(i);
					if (i == idCommand)
					{
						iCommand2.isSelect = true;
					}
					else
					{
						iCommand2.isSelect = false;
					}
				}
			}
		}
		if (GameCanvas.keyMyHold[5])
		{
			GameCanvas.clearKeyHold(5);
			if (vecCmd != null && idCommand < vecCmd.size())
			{
				((iCommand)vecCmd.elementAt(idCommand)).perform();
			}
		}
		base.updatekey();
		updatekeyPC();
	}

	public override void updatePointer()
	{
		for (int i = 0; i < vecCmd.size(); i++)
		{
			((iCommand)vecCmd.elementAt(i)).updatePointer();
		}
		base.updatePointer();
	}

	public void openMenuLeft()
	{
		mVector vec = new mVector();
		UITheme curTheme = UIThemeManager.getCurrentTheme();
		string themeName = (curTheme != null) ? curTheme.displayName : "Giao Di\u1ec7n";
		vec.addElement(new iCommand("\uD83C\uDFA8 Giao di\u1ec7n: " + themeName, 97, this));
		vec.addElement(new iCommand("\uD83C\uDF10 " + (T.server ?? "Ch\u1ecdn M\u00e1y Ch\u1ee7"), 4, this));
		vec.addElement(new iCommand("\uD83D\uDD11 Nh\u1eadp Key B\u1ea3n Quy\u1ec1n", 23, this));
		if (KeyAuthManager.isAuthorized)
		{
			vec.addElement(new iCommand("+ Th\u00eam IP Server", 25, this));
		}
		vec.addElement(new iCommand("\uD83D\uDD04 " + (T.changeAcc ?? "\u0110\u1ed5i T\u00e0i Kho\u1ea3n"), 1, 0, this));
		vec.addElement(new iCommand("\u274C " + (T.exit ?? "Tho\u00e1t"), 8, this));
		GameCanvas.menu.startAt(vec, 0, T.menu ?? "Menu");
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

	public override bool keyBack()
	{
		if (GameCanvas.menu != null && GameCanvas.menu.isShowMenu)
		{
			GameCanvas.menu.doCloseMenu();
			return true;
		}
		return base.keyBack();
	}
}
