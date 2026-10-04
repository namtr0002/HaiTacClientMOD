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

	// Trạng thái chờ user bấm nút Tải (chưa bắt đầu tải)
	public const sbyte WAITING_USER = -1;

	public const sbyte CONNECT = 0;

	public const sbyte FAIL = 1;

	public const sbyte LOADING = 2;

	public const sbyte LOADING_OK = 3;

	// Timeout giảm xuống 8 giây
	private const int TIMEOUT_SECONDS = 8;

	public static string strPaint = "";

	public iCommand cmdServer;

	// Nút "Tải dữ liệu"
	private iCommand cmdLoadData;

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

		// Nút chọn server ở trên
		cmdServer = new iCommand(T.server + ": " + UpdateServer.getCurrentServerName(), 1, this);
		cmdServer.setTypeSpec();
		left = cmdServer;

		// Nút Tải dữ liệu ở dưới
		cmdLoadData = new iCommand("Tải dữ liệu", 0, this);

		// KHÔNG tự động tải - chờ user bấm nút
		statusUpdate = WAITING_USER;
		strPaint = "Chọn máy chủ và nhấn \"Tải dữ liệu\"";

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
		// Vẽ nền
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

		// Logo ở trên cùng
		if (imglogo != null)
		{
			g.drawImage(imglogo, MotherCanvas.hw, MotherCanvas.h / 7, 3);
		}

		// ===== NÚT SERVER Ở TRÊN =====
		if (cmdServer != null)
		{
			cmdServer.caption = T.server + ": " + UpdateServer.getCurrentServerName();
			int serverY = MotherCanvas.h / 5;
			cmdServer.setPos(MotherCanvas.hw, serverY, null, cmdServer.caption);
			cmdServer.paint(g, cmdServer.xCmd, cmdServer.yCmd);
		}

		// ===== VÙNG GIỮA - progress bar hoặc thông báo =====
		x = MotherCanvas.hw;
		y = MotherCanvas.hh;

		if (statusUpdate == WAITING_USER)
		{
			// Hiển thị thông báo hướng dẫn ở giữa
			g.setColor(0xFFFFFF);
			g.drawString(strPaint, MotherCanvas.hw, MotherCanvas.hh - 10, 2);
		}
		else if (statusUpdate == CONNECT)
		{
			// Đang kết nối
			g.setColor(0xFFFFFF);
			g.drawString(strPaint, MotherCanvas.hw, MotherCanvas.hh - 10, 2);
			// Hiển thị đếm ngược timeout
			long elapsed = (GameCanvas.timeNow - timeBegin) / 1000;
			long remaining = TIMEOUT_SECONDS - elapsed;
			if (remaining > 0)
			{
				g.setColor(0xCCCCCC);
				g.drawString("(" + remaining + "s)", MotherCanvas.hw, MotherCanvas.hh + 8, 2);
			}
		}
		else if (statusUpdate == LOADING || statusUpdate == LOADING_OK)
		{
			// Progress bar ở giữa màn hình
			g.setColor(0xFFFFFF);
			g.drawString(strPaint, MotherCanvas.hw, y - 20, 2);
			if (imgloading1 != null)
			{
				g.drawImage(imgloading1, x - 61, y - 8, 0);
			}
			if (imgloading2 != null && wpaint >= 0)
			{
				g.drawRegion(imgloading2, 0, 0, wpaint, 16, 0, x - 61, y - 8, 0);
			}
			int num = wpaint;
			if (num < 10) num = 10;
			if (num > maxwPaint - 12) num = maxwPaint - 12;
			g.setColor(0xFFFFFF);
			g.drawString(curNum + " / " + maxNum, MotherCanvas.hw, y + 4, 2);
			if (imgloading3 != null)
			{
				g.drawImage(imgloading3, x - 60 + num, y, 3);
			}
		}
		else if (statusUpdate == FAIL)
		{
			// Lỗi/timeout
			g.setColor(0xFF4444);
			g.drawString(strPaint, MotherCanvas.hw, MotherCanvas.hh - 15, 2);
			g.setColor(0xFFFFFF);
			g.drawString("Nhấn \"Thử lại\" để tải lại", MotherCanvas.hw, MotherCanvas.hh + 5, 2);
		}

		// ===== NÚT TẢI DỮ LIỆU / THỬ LẠI Ở DƯỚI =====
		if (cmdLoadData != null)
		{
			string btnLabel;
			if (statusUpdate == FAIL)
			{
				btnLabel = "Thử lại";
			}
			else if (statusUpdate == CONNECT || statusUpdate == LOADING || statusUpdate == LOADING_OK)
			{
				btnLabel = "Đang tải...";
			}
			else
			{
				btnLabel = "Tải dữ liệu";
			}
			cmdLoadData.caption = btnLabel;
			int loadBtnY = MotherCanvas.h * 4 / 5;
			cmdLoadData.setPos(MotherCanvas.hw, loadBtnY, null, cmdLoadData.caption);
			cmdLoadData.paint(g, cmdLoadData.xCmd, cmdLoadData.yCmd);
		}

		base.paint(g);
	}

	public override void update()
	{
		x = MotherCanvas.hw;
		y = MotherCanvas.hh;
		if (maxNum > 0)
		{
			wpaint = maxwPaint * curNum / maxNum;
			if (wpaint > maxwPaint)
			{
				wpaint = maxwPaint;
			}
		}

		// Kiểm tra timeout chỉ khi đang kết nối (statusUpdate == CONNECT)
		if (statusUpdate == CONNECT && (GameCanvas.timeNow - timeBegin) / 1000 > TIMEOUT_SECONDS)
		{
			if (GameCanvas.indexdownload == 0)
			{
				Session_ME.gI().close();
				GameCanvas.indexdownload++;
				GameCanvas.connectDownload();
				GlobalService.gI().Request_Image_Android(mGraphics.zoomLevel);
				timeBegin = GameCanvas.timeNow;
				setmNamePaint("Đang thử lại...");
			}
			else
			{
				setmNamePaint(T.disconnectUpdateImage);
				statusUpdate = FAIL;
			}
		}

		if (statusUpdate == LOADING_OK && SaveImageRMS.vecSaveImageAndroid.size() == 0)
		{
			GameCanvas.instance.beginGame();
			saveVer();
		}
	}

	public override void updatePointer()
	{
		// Cập nhật nút chọn server
		if (cmdServer != null)
		{
			cmdServer.updatePointer();
		}

		// Cập nhật nút Tải / Thử lại
		if (cmdLoadData != null)
		{
			cmdLoadData.updatePointer();
		}

		// Khi bấm vào vùng màn hình lúc FAIL (ngoài các nút) → thử lại
		if (statusUpdate == FAIL && GameCanvas.isPointerDown
			&& (cmdServer == null || !cmdServer.isSelect)
			&& (cmdLoadData == null || !cmdLoadData.isSelect))
		{
			startDownload();
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
		case 0:
			// Nút "Tải dữ liệu" hoặc "Thử lại"
			startDownload();
			break;
		case 1:
			// Nút chọn server
			openSelectServerMenu();
			break;
		case 2:
		{
			// Chọn server từ menu
			int globalIndex = UpdateServer.relativeToGlobal(subIndex, GameCanvas.language);
			UpdateServer.setServerIndex(globalIndex);
			if (cmdServer != null)
			{
				cmdServer.caption = T.server + ": " + UpdateServer.getCurrentServerName();
			}
			GameCanvas.menu.doCloseMenu();
			// Nếu đang FAIL → tự động retry sau khi đổi server
			if (statusUpdate == FAIL || statusUpdate == WAITING_USER)
			{
				strPaint = "Đã chọn: " + UpdateServer.getCurrentServerName();
			}
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
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Đã thêm máy chủ thành công!\n" + UpdateServer.getCurrentServerName());
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

	/// <summary>
	/// Bắt đầu tải dữ liệu (được gọi khi user bấm nút Tải hoặc Thử lại).
	/// </summary>
	public void startDownload()
	{
		// Không tải nếu đang trong quá trình tải
		if (statusUpdate == CONNECT || statusUpdate == LOADING || statusUpdate == LOADING_OK)
		{
			return;
		}
		statusUpdate = CONNECT;
		curNum = 0;
		maxNum = 0;
		wpaint = -1;
		GameCanvas.indexdownload = 0;
		setmNamePaint(T.pleaseWaiting);
		timeBegin = GameCanvas.timeNow;
		beginLoadImage();
	}

	public void retryDownload()
	{
		Session_ME.gI().close();
		GameCanvas.indexdownload = 0;
		statusUpdate = CONNECT;
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
		statusUpdate = LOADING;
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
		GlobalService.gI().Request_Image_Android(mGraphics.zoomLevel);
	}
}
