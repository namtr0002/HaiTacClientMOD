using System;

public class UpdateImageScreen : MainScreen
{
	public static int maxNum;

	public static int curNum;

	private int wpaint = -1;

	private int maxwPaint;

	private int x;

	private int y;

	private long timeBegin;

	public static sbyte statusUpdate = 0;

	public const sbyte CONNECT = 0;

	public const sbyte FAIL = 1;

	public const sbyte LOADING = 2;

	public const sbyte LOADING_OK = 3;

	public static string strPaint = "";

	public iCommand cmdServer;

	private mImage imglogo;

	private mImage imgsea;

	private mImage imgsky;

	private mImage imgloading1;

	private mImage imgloading2;

	private mImage imgloading3;

	private int wSky;

	private int wSea;

	private int hSky;

	private int hSea;

	public UpdateImageScreen()
	{
		UpdateServer.loadServers();
		maxwPaint = 122;
		x = MotherCanvas.hw;
		y = MotherCanvas.h / 5 * 4 - 7;
		cmdServer = new iCommand(T.server + ": " + UpdateServer.getCurrentServerName(), 1, this);
		cmdServer.setTypeSpec();
		left = cmdServer;
		beginLoadImage();
		timeBegin = mSystem.currentTimeMillis();
		statusUpdate = 0;
		setmNamePaint(T.pleaseWaiting);
		loadImage();
	}

	public void loadImage()
	{
		if (GameCanvas.language == 1)
		{
			imglogo = mImage.createImage("/new/lgv_e.png");
		}
		else
		{
			imglogo = mImage.createImage("/new/lgv.png");
		}
		imgloading1 = mImage.createImage("/new/koload.png");
		imgloading2 = mImage.createImage("/new/load.png");
		imgloading3 = mImage.createImage("/new/thuyen.png");
		imgsea = mImage.createImageAll("/up0.png");
		imgsky = mImage.createImageAll("/up1.png");
		wSky = mImage.getImageWidth(imgsky.image);
		hSky = mImage.getImageHeight(imgsky.image);
		wSea = mImage.getImageWidth(imgsea.image);
		hSea = mImage.getImageHeight(imgsea.image);
	}

	public override void paint(mGraphics g)
	{
		g.setColor(6014975);
		g.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h / 2);
		g.setColor(16765819);
		g.fillRect(0, MotherCanvas.h / 2, MotherCanvas.w, MotherCanvas.h / 2);
		if (wSky > 0 && hSky > 0)
		{
			for (int i = 0; i <= MotherCanvas.w / wSky; i++)
			{
				g.drawImage(imgsky, i * wSky, MotherCanvas.hh - hSky / 2, 0);
			}
		}
		if (wSea > 0 && hSea > 0)
		{
			for (int j = 0; j <= MotherCanvas.w / wSea; j++)
			{
				g.drawImage(imgsea, j * wSea, MotherCanvas.hh + hSky / 2, 0);
			}
		}
		if (imglogo != null)
		{
			g.drawImage(imglogo, MotherCanvas.hw, MotherCanvas.h / 5, 3);
		}
		g.setColor(0);
		g.drawString(strPaint, MotherCanvas.hw, y - 20 + 7, 2);
		if (statusUpdate == 2 || statusUpdate == 3)
		{
			g.drawImage(imgloading1, x - 61, y - 8, 0);
			if (wpaint >= 0)
			{
				g.drawRegion(imgloading2, 0, 0, wpaint, 16, 0, x - 61, y - 8, 0);
			}
			int num = wpaint;
			if (num < 10)
			{
				num = 10;
			}
			if (num > maxwPaint - 12)
			{
				num = maxwPaint - 12;
			}
			g.drawString(curNum + " / " + maxNum, MotherCanvas.hw, y + 4, 2);
			g.drawImage(imgloading3, x - 60 + num, y, 3);
		}
		if (cmdServer != null)
		{
			cmdServer.caption = T.server + ": " + UpdateServer.getCurrentServerName();
			cmdServer.setPos(MotherCanvas.hw, MotherCanvas.h - 26, null, cmdServer.caption);
			cmdServer.paint(g, cmdServer.xCmd, cmdServer.yCmd);
		}
		base.paint(g);
	}

	public override void update()
	{
		x = MotherCanvas.hw;
		y = MotherCanvas.h / 5 * 4 - 7;
		if (maxNum > 0)
		{
			wpaint = maxwPaint * curNum / maxNum;
			if (wpaint > maxwPaint)
			{
				wpaint = maxwPaint;
			}
		}
		if (statusUpdate == 0 && (GameCanvas.timeNow - timeBegin) / 1000 > 15)
		{
			if (GameCanvas.indexdownload == 0)
			{
				Session_ME.gI().close();
				GameCanvas.indexdownload++;
				GameCanvas.connectDownload();
				GlobalService.gI().Request_Image_Android();
				timeBegin = GameCanvas.timeNow;
			}
			else
			{
				setmNamePaint(T.disconnectUpdateImage);
				statusUpdate = 1;
			}
		}
		if (statusUpdate == 3 && SaveImageRMS.vecSaveImageAndroid.size() == 0)
		{
			GameCanvas.instance.beginGame();
			saveVer();
		}
	}

	public override void updatePointer()
	{
		if (cmdServer != null)
		{
			cmdServer.updatePointer();
		}
		if (statusUpdate == 1 && GameCanvas.isPointerDown && (cmdServer == null || !cmdServer.isSelect))
		{
			retryDownload();
		}
		base.updatePointer();
	}

	public override void updatekey()
	{
		if (GameCanvas.keyMyHold[5])
		{
			GameCanvas.clearKeyHold(5);
			if (cmdServer != null)
			{
				cmdServer.perform();
			}
		}
		base.updatekey();
		updatekeyPC();
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

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
		case 1:
			openSelectServerMenu();
			break;
		case 2:
		{
			int globalIndex = UpdateServer.relativeToGlobal(subIndex, GameCanvas.language);
			UpdateServer.setServerIndex(globalIndex);
			if (cmdServer != null)
			{
				cmdServer.caption = T.server + ": " + UpdateServer.getCurrentServerName();
			}
			GameCanvas.menu.doCloseMenu();
			retryDownload();
			break;
		}
		case 3:
		{
			InputDialog input = new InputDialog();
			input.setinfo("Nhập Tên:IP:Port hoặc IP:Port\nVD: SV1:127.0.0.1:2239", new iCommand("OK", 4, this), isNum: false, "Thêm Server");
			GameCanvas.currentDialog = input;
			break;
		}
		case 4:
		{
			InputDialog input = GameCanvas.currentDialog as InputDialog;
			if (input != null && input.tfInput != null)
			{
				string text = input.tfInput.getText().Trim();
				if (text.Length > 0)
				{
					bool ok = UpdateServer.parseAndAddCustomServer(text);
					if (ok)
					{
						if (cmdServer != null)
						{
							cmdServer.caption = T.server + ": " + UpdateServer.getCurrentServerName();
						}
						retryDownload();
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Đã thêm máy chủ và kết nối tải dữ liệu!");
					}
					else
					{
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Định dạng không hợp lệ!\nVí dụ: SV1:127.0.0.1:2239");
					}
				}
			}
			break;
		}
		}
		base.commandPointer(index, subIndex);
	}

	public void retryDownload()
	{
		Session_ME.gI().close();
		GameCanvas.indexdownload = 0;
		statusUpdate = 0;
		curNum = 0;
		maxNum = 0;
		wpaint = -1;
		setmNamePaint(T.pleaseWaiting);
		timeBegin = GameCanvas.timeNow;
		beginLoadImage();
	}

	public void openSelectServerMenu()
	{
		UpdateServer.loadServers();
		mVector vec = new mVector();
		string[] servers = null;
		if (GameCanvas.strListServer != null
			&& GameCanvas.language >= 0
			&& GameCanvas.language < GameCanvas.strListServer.Length
			&& GameCanvas.strListServer[GameCanvas.language] != null
			&& GameCanvas.strListServer[GameCanvas.language].Length > 0)
		{
			servers = GameCanvas.strListServer[GameCanvas.language];
		}
		else if (UpdateServer.serverNames.Count > 0)
		{
			servers = UpdateServer.serverNames.ToArray();
		}

		if (servers != null)
		{
			for (int i = 0; i < servers.Length; i++)
			{
				iCommand cmd = new iCommand(servers[i], 2, i, this);
				vec.addElement(cmd);
			}
		}

		iCommand cmdAdd = new iCommand("+ Thêm Server", 3, this);
		vec.addElement(cmdAdd);
		GameCanvas.menu.startAt(vec, 2, T.server);
	}

	public static void setValueUpdate(int cur, int max)
	{
		curNum = cur;
		if (max >= 0)
		{
			maxNum = max;
		}
		statusUpdate = 2;
	}

	public static void setmNamePaint(string str)
	{
		strPaint = str;
	}

	public void saveVer()
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try
		{
			dataOutputStream.writeUTF("1.2.9");
			CRes.saveRMS("Main_Load_Image_Android_OK", byteArrayOutputStream.toByteArray());
			dataOutputStream.close();
		}
		catch (Exception)
		{
		}
	}

	public void beginLoadImage()
	{
		Session_ME.gI().close();
		GameCanvas.connectDownload();
		GlobalService.gI().Request_Image_Android();
	}
}
