using System;

public class FristLoginScreen : MainScreen
{
	public iCommand cmdBegin;

	public iCommand cmdChangeAcc;

	public iCommand cmdServer;

	public iCommand cmdNewGame;

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
		if (CRes.loadRMS("MAIN_user_last") != null)
		{
			GameCanvas.saveRms.loadUserLast();
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
		if (KeyAuthManager.isAuthorized && CRes.loadRMS("MAIN_user_pass") != null)
		{
			GameCanvas.loginScr.Show();
			GameCanvas.loginScr.doLogin(isGetData: true, 0, GameCanvas.loginScr.tfUser.getText(), GameCanvas.loginScr.tfPass.getText());
		}
		else
		{
			setNewAcc(isCheckDataOK: false);
			getVecBegin();
		}
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
		switch (index)
		{
		case 0:
		{
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			ListChar_Screen.IndexCharSelected = -1;
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
			GameCanvas.loginScr.doLogin(isGetData: true, 1, userNew, "");
			break;
		}
		case 1:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
			GameCanvas.loginScr.Show();
			break;
		case 2:
			if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
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
		UpdateServer.loadServers();
		vecCmd.removeAllElements();
		if (GameCanvas.language > GameCanvas.strListServer.Length - 1)
		{
			GameCanvas.language = 0;
		}
		string sName = UpdateServer.getCurrentServerName();

		if (SaveRms.userLast.Length > 0)
		{
			cmdBegin.caption = T.loadGame + "\n " + SaveRms.userLast;
			cmdBegin.setPos(MotherCanvas.hw - 38, MotherCanvas.h - 98, null, cmdBegin.caption);
			cmdBegin.setTypeSpec();
			vecCmd.addElement(cmdBegin);
			cmdNewGame = new iCommand(T.newGame, 2, 0, this);
			cmdNewGame.setPos(MotherCanvas.hw + 38, MotherCanvas.h - 98, null, cmdNewGame.caption);
			cmdNewGame.setTypeSpec();
			vecCmd.addElement(cmdNewGame);
			cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
			cmdChangeAcc.setPos(MotherCanvas.hw - 38, MotherCanvas.h - 46, null, cmdChangeAcc.caption);
			cmdChangeAcc.setTypeSpec();
			vecCmd.addElement(cmdChangeAcc);
			cmdServer.caption = T.server + "\n" + sName;
			cmdServer.setPos(MotherCanvas.hw + 38, MotherCanvas.h - 46, null, cmdServer.caption);
			cmdServer.setTypeSpec();
			vecCmd.addElement(cmdServer);
		}
		else
		{
			cmdNewGame = new iCommand(T.newGame, 2, 0, this);
			cmdNewGame.setPos(MotherCanvas.hw - 76, MotherCanvas.h - 60, null, cmdNewGame.caption);
			cmdNewGame.setTypeSpec();
			vecCmd.addElement(cmdNewGame);
			cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
			cmdChangeAcc.setPos(MotherCanvas.hw, MotherCanvas.h - 60, null, cmdChangeAcc.caption);
			cmdChangeAcc.setTypeSpec();
			vecCmd.addElement(cmdChangeAcc);
			cmdServer.caption = T.server + "\n" + sName;
			cmdServer.setPos(MotherCanvas.hw + 76, MotherCanvas.h - 60, null, cmdServer.caption);
			cmdServer.setTypeSpec();
			vecCmd.addElement(cmdServer);
		}
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
