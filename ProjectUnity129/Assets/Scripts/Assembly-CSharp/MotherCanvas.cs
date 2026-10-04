using System;
using System.IO;
using UnityEngine;

public class MotherCanvas
{
	public static MotherCanvas instance;

	public GameCanvas tCanvas;

	public int zoomLevel = 1;

	public Image imgCache;

	private int[] imgRGBCache;

	private int newWidth;

	private int newHeight;

	private int[] output;

	public static int w;

	public static int h;

	public static int hw;

	public static int hh;

	public static int w4;

	public static int h4;

	// Compatibility constants
	public const int GRAPHIC_AUTO = 0;
	public const int GRAPHIC_JAVA = 1;
	public const int GRAPHIC_ANDROID = 2;
	public const int GRAPHIC_IOS = 3;
	public const int GRAPHIC_PC = 4;
	public const int GRAPHIC_PIXEL = GRAPHIC_JAVA;
	public const int GRAPHIC_FULL_HD = GRAPHIC_PC;
	public const int GRAPHIC_MINI = 10;

	public static int userZoomSetting = 0;
	public static int selectedZoom => mGraphics.zoomLevel;
	public static int displayZoom => mGraphics.zoomLevel;
	public static int resourceZoom => mGraphics.zoomLevel;
	public static float renderScale => 1f;

	public static void saveZoomSetting(int zoom)
	{
		userZoomSetting = zoom;
		try
		{
			PlayerPrefs.SetInt("MAIN_zoom_setting", zoom);
			PlayerPrefs.Save();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(zoom);
			CRes.saveRMS("MAIN_zoom_setting", baos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static void loadZoomSetting()
	{
		try
		{
			if (PlayerPrefs.HasKey("MAIN_zoom_setting"))
			{
				int savedPref = PlayerPrefs.GetInt("MAIN_zoom_setting");
				if (savedPref >= 1 && savedPref <= 4)
				{
					userZoomSetting = savedPref;
					return;
				}
			}
			sbyte[] data = CRes.loadRMS("MAIN_zoom_setting");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				int saved = dis.readInt();
				dis.close();
				if (saved >= 1 && saved <= 4)
				{
					userZoomSetting = saved;
					PlayerPrefs.SetInt("MAIN_zoom_setting", saved);
					PlayerPrefs.Save();
					return;
				}
			}
		}
		catch (Exception)
		{
		}
		userZoomSetting = 0;
	}

	public static void applyZoom(int zoom)
	{
		if (zoom < 1 || zoom > 4) zoom = 2;
		saveZoomSetting(zoom);
		mGraphics.zoomLevel = zoom;
		mGraphics.zoomResource = (zoom >= 2) ? 2 : 1;
		if (instance != null)
		{
			instance.zoomLevel = zoom;
			instance.checkZoomLevel();
		}
		refreshDisplay();
	}

	public static void applyZoomAndReloadData(int zoom)
	{
		applyZoom(zoom);
	}

	public static void requestChangeZoom(int zoom, string modeName)
	{
		applyZoom(zoom);
	}

	public static bool isZoomDataDownloaded(int zoom) => true;

	// Orientation settings
	public static int userOrientation = 1;

	public static void saveOrientationSetting(int orientation)
	{
		userOrientation = orientation;
		try
		{
			PlayerPrefs.SetInt("MAIN_orientation_setting", orientation);
			PlayerPrefs.Save();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(orientation);
			CRes.saveRMS("MAIN_orientation_setting", baos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static void loadOrientationSetting()
	{
		try
		{
			if (PlayerPrefs.HasKey("MAIN_orientation_setting"))
			{
				int savedPref = PlayerPrefs.GetInt("MAIN_orientation_setting");
				if (savedPref >= 0 && savedPref <= 2)
				{
					userOrientation = savedPref;
					return;
				}
			}
			sbyte[] data = CRes.loadRMS("MAIN_orientation_setting");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				int saved = dis.readInt();
				dis.close();
				if (saved >= 0 && saved <= 2)
				{
					userOrientation = saved;
					PlayerPrefs.SetInt("MAIN_orientation_setting", saved);
					PlayerPrefs.Save();
					return;
				}
			}
		}
		catch (Exception)
		{
		}
		userOrientation = 1;
	}

	public class ActionExitGame : IAction
	{
		private int orientation = -1;

		public ActionExitGame(int orientation = -1)
		{
			this.orientation = orientation;
		}

		public void perform()
		{
			if (orientation >= 0)
			{
				MotherCanvas.applyOrientationToUnity(orientation);
			}
#if UNITY_EDITOR
			UnityEditor.EditorApplication.isPlaying = false;
#else
			if (GameMidlet.instance != null)
			{
				GameMidlet.instance.exit();
			}
			else
			{
				Main.exit();
			}
#endif
		}
	}

	public class ActionApplyOrientationAndReload : IAction
	{
		private int orientation;

		public ActionApplyOrientationAndReload(int orientation)
		{
			this.orientation = orientation;
		}

		public void perform()
		{
			MotherCanvas.applyOrientationAndReloadData(orientation);
			GameCanvas.end_Dialog();
		}
	}

	public static void requestChangeOrientation(int orientation, string modeName)
	{
		saveOrientationSetting(orientation);
		applyOrientationToUnity(orientation);
		showOrientationChangeNotice(modeName, orientation);
	}

	public static void showOrientationChangeNotice(string modeName, int newOrientation)
	{
		saveOrientationSetting(newOrientation);
		applyOrientationToUnity(newOrientation);
		string msg = "Đã chọn: " + modeName + "\n\nVui lòng khởi động lại game hoặc áp dụng ngay!";
		mVector vecCmd = new mVector();
		vecCmd.addElement(new iCommand("Áp dụng ngay", new ActionApplyOrientationAndReload(newOrientation)));
		vecCmd.addElement(new iCommand("Thoát game", new ActionExitGame(newOrientation)));
		GameCanvas.Start_Normal_DiaLog(msg, vecCmd, isCmdClose: true);
	}

	public static void applyOrientationToUnity(int orientation)
	{
		userOrientation = orientation;
		saveOrientationSetting(orientation);

		bool isMobile = Application.platform == RuntimePlatform.Android || Application.platform == RuntimePlatform.IPhonePlayer || Application.isMobilePlatform;

		if (isMobile)
		{
			if (orientation == 2)
			{
				Screen.orientation = ScreenOrientation.Portrait;
			}
			else if (orientation == 0)
			{
				Screen.orientation = ScreenOrientation.AutoRotation;
				Screen.autorotateToLandscapeLeft = true;
				Screen.autorotateToLandscapeRight = true;
				Screen.autorotateToPortrait = true;
				Screen.autorotateToPortraitUpsideDown = false;
			}
			else
			{
				Screen.orientation = ScreenOrientation.LandscapeLeft;
			}
		}
		else if (GameMidlet.isPC || Application.platform == RuntimePlatform.WindowsPlayer || Application.platform == RuntimePlatform.OSXPlayer || Application.platform == RuntimePlatform.LinuxPlayer)
		{
			int curW = Screen.width;
			int curH = Screen.height;
			if (curW <= 0) curW = 1280;
			if (curH <= 0) curH = 720;

			bool isCurrentlyPortrait = curH > curW;
			bool wantsPortrait = (orientation == 2);

			if (wantsPortrait != isCurrentlyPortrait)
			{
				int maxW = (Screen.currentResolution.width > 0) ? Screen.currentResolution.width : 1920;
				int maxH = (Screen.currentResolution.height > 0) ? Screen.currentResolution.height : 1080;
				int resW;
				int resH;
				if (wantsPortrait)
				{
					resH = Mathf.Min(960, maxH - 80);
					resW = (int)(resH * 9f / 16f);
				}
				else
				{
					resH = Mathf.Min(720, maxH - 80);
					resW = (int)(resH * 16f / 9f);
				}
				if (resW > 0 && resH > 0)
				{
					Screen.SetResolution(resW, resH, false);
					ScaleGUI.WIDTH = resW;
					ScaleGUI.HEIGHT = resH;
				}
			}
		}
		else if (Application.isEditor)
		{
			if (orientation == 2 && Screen.width > Screen.height)
			{
				int newW = Screen.height;
				int newH = Screen.width;
				ScaleGUI.WIDTH = newW;
				ScaleGUI.HEIGHT = newH;
			}
			else if (orientation == 1 && Screen.height > Screen.width)
			{
				int newW = Screen.height;
				int newH = Screen.width;
				ScaleGUI.WIDTH = newW;
				ScaleGUI.HEIGHT = newH;
			}
		}
	}

	public static void applyOrientation(int mode)
	{
		applyOrientationToUnity(mode);
	}

	public static void applyOrientationAndReloadData(int orientation)
	{
		applyOrientationToUnity(orientation);
		refreshDisplay();
	}

	public static void refreshDisplay()
	{
		try
		{
			ScaleGUI.initScaleGUI();
			if (instance != null)
			{
				instance.checkZoomLevel();
			}
			mFont.loadmFont();
			LoadImageStatic.loadImageLanguage();
			if (GameCanvas.loadmap != null && MainScreen.cameraMain != null)
			{
				GameCanvas.loadmap.limitW = GameCanvas.loadmap.maxWMap - w;
				GameCanvas.loadmap.limitH = GameCanvas.loadmap.maxHMap - h;
				int px = (GameScreen.player != null) ? GameScreen.player.x : 0;
				int py = (GameScreen.player != null) ? GameScreen.player.y : 0;
				MainScreen.cameraMain.setAll(GameCanvas.loadmap.limitW, GameCanvas.loadmap.limitH, px - hw, py - hh);
				GameScreen.updateCameraBounds();
			}
			if (GameCanvas.currentScreen != null)
			{
				GameCanvas.currentScreen.init();
			}
		}
		catch (Exception ex)
		{
			Debug.LogError("[MotherCanvas] refreshDisplay error: " + ex);
		}
	}

	// FPS management
	public static int targetFPS = 60;
	public static int realFPS = 60;
	public static int currentFPS => realFPS;
	private static int fpsCounter = 0;
	private static long lastFpsTime = 0;

	public static void updateFPSCounter()
	{
		fpsCounter++;
		long now = mSystem.currentTimeMillis();
		if (now - lastFpsTime >= 1000L)
		{
			realFPS = fpsCounter;
			fpsCounter = 0;
			lastFpsTime = now;
		}
	}

	public static void setFPS(int fps)
	{
		if (fps < 10) fps = 10;
		if (fps > 240) fps = 240;
		targetFPS = fps;
		QualitySettings.vSyncCount = 0;
		Application.targetFrameRate = fps;
		saveFPSSetting(fps);
	}

	public static void saveFPSSetting(int fps)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(fps);
			CRes.saveRMS("MAIN_fps_setting", baos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static void loadFPSSetting()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("MAIN_fps_setting");
			if (data != null && data.Length > 0)
			{
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
				int saved = dis.readInt();
				dis.close();
				if (saved >= 10 && saved <= 240)
				{
					targetFPS = saved;
					QualitySettings.vSyncCount = 0;
					Application.targetFrameRate = saved;
					return;
				}
			}
		}
		catch (Exception)
		{
		}
		targetFPS = 60;
		QualitySettings.vSyncCount = 0;
		Application.targetFrameRate = 60;
	}

	public void checkZoomLevel()
	{
		if (ScaleGUI.WIDTH <= 0 || ScaleGUI.HEIGHT <= 0)
		{
			ScaleGUI.initScaleGUI();
		}

		int width = getWidth();
		int height = getHeight();
		
		// Ensure valid dimensions (landscape default)
		if (width <= 0) width = 1280;
		if (height <= 0) height = 720;
		
		int longDim = (width > height) ? width : height;
		int shortDim = (width > height) ? height : width;

		bool isPCPlatform = GameMidlet.isPC 
			|| GameMidlet.DEVICE == GameMidlet.PC 
			|| Application.platform == RuntimePlatform.WindowsPlayer 
			|| Application.platform == RuntimePlatform.WindowsEditor 
			|| Application.platform == RuntimePlatform.OSXPlayer 
			|| Application.platform == RuntimePlatform.OSXEditor 
			|| Application.platform == RuntimePlatform.LinuxPlayer;

		if (isPCPlatform)
		{
			GameMidlet.isPC = true;
			if (GameMidlet.DEVICE != GameMidlet.PC)
			{
				GameMidlet.DEVICE = GameMidlet.PC;
			}
		}

		if (userZoomSetting >= 1 && userZoomSetting <= 4)
		{
			mGraphics.zoomLevel = userZoomSetting;
		}
		else if (GameMidlet.DEVICE == 0) // Thiết bị cũ / J2ME
		{
			mGraphics.zoomLevel = 1;
		}
		else if (isPCPlatform)
		{
			// PC / Unity Editor (Hỗ trợ cả Ngang & Dọc chuẩn xác)
			if (longDim >= 3600 && shortDim >= 1800)
			{
				mGraphics.zoomLevel = 4; // Màn hình 4K UHD
			}
			else if (longDim >= 2200 && shortDim >= 1200)
			{
				mGraphics.zoomLevel = 3; // Màn hình 1440p / 2K QHD
			}
			else if (longDim >= 700 && shortDim >= 400)
			{
				mGraphics.zoomLevel = 2; // Cửa sổ PC lớn (1024x550, 1280x720, 1920x1080)
			}
			else
			{
				mGraphics.zoomLevel = 1; // Cửa sổ PC nhỏ (600x355 chuẩn) hoặc nhỏ hơn 700x400
			}
		}
		else
		{
			// Mobile (Android / iOS - Hỗ trợ cả Ngang & Dọc)
			if (longDim >= 2800 && shortDim >= 1400)
			{
				mGraphics.zoomLevel = 4; // Tablet siêu phân giải
			}
			else if (longDim >= 1600 && shortDim >= 850)
			{
				mGraphics.zoomLevel = 3; // Điện thoại 1080p FHD+ (canvas 360dp chuẩn, UI vừa vặn, không to đùng)
			}
			else if (longDim >= 700 && shortDim >= 400)
			{
				mGraphics.zoomLevel = 2; // Điện thoại 720p HD+
			}
			else
			{
				mGraphics.zoomLevel = 1; // Thiết bị độ phân giải thấp
			}
		}
		
		this.zoomLevel = mGraphics.zoomLevel;
		mGraphics.zoomResource = (mGraphics.zoomLevel >= 2) ? 2 : 1;
		w = (width + mGraphics.zoomLevel - 1) / mGraphics.zoomLevel;
		h = (height + mGraphics.zoomLevel - 1) / mGraphics.zoomLevel;
		
		// Ensure valid calculated dimensions
		if (w <= 0) w = width;
		if (h <= 0) h = height;
		
		hw = w / 2;
		hh = h / 2;
		w4 = w / 4;
		h4 = h / 4;
		
		mSystem.outz("Calculated canvas: w=" + w + " h=" + h + " zoomLevel=" + mGraphics.zoomLevel);
	}

	public MotherCanvas()
	{
	}

	public MotherCanvas(Context context)
	{
	}

	public int getWidth()
	{
		return (int)ScaleGUI.WIDTH;
	}

	public int getHeight()
	{
		return (int)ScaleGUI.HEIGHT;
	}

	public void setChildCanvas(GameCanvas tCanvas)
	{
		this.tCanvas = tCanvas;
	}

	public virtual void paint(mGraphics g)
	{
		tCanvas.paint(g);
	}

	public virtual void update()
	{
		tCanvas.update();
	}

	public virtual void keyPressed(int keyCode)
	{
		tCanvas.keyPressed(keyCode);
	}

	public virtual void keyReleased(int keyCode)
	{
		tCanvas.keyReleased(keyCode);
	}

	public void pointerDragged(int x, int y)
	{
		x /= mGraphics.zoomLevel;
		y /= mGraphics.zoomLevel;
	}

	public void pointerPressed(int x, int y)
	{
		x /= mGraphics.zoomLevel;
		y /= mGraphics.zoomLevel;
	}

	public void pointerReleased(int x, int y)
	{
		x /= mGraphics.zoomLevel;
		y /= mGraphics.zoomLevel;
	}

	public int getWidthz()
	{
		int width = getWidth();
		return width / mGraphics.zoomLevel + width % mGraphics.zoomLevel;
	}

	public int getHeightz()
	{
		int height = getHeight();
		return height / mGraphics.zoomLevel + height % mGraphics.zoomLevel;
	}

	public bool hasPointerEvents()
	{
		return true;
	}

	public bool keyPressPc(int keycode)
	{
		switch (keycode)
		{
		case 97:
			GameCanvas.keyMyHold[34] = true;
			GameCanvas.keyMyPressed[34] = true;
			return true;
		case 98:
			GameCanvas.keyMyHold[51] = true;
			GameCanvas.keyMyPressed[51] = true;
			return true;
		case 99:
			GameCanvas.keyMyHold[48] = true;
			GameCanvas.keyMyPressed[48] = true;
			return true;
		case 100:
			GameCanvas.keyMyHold[36] = true;
			GameCanvas.keyMyPressed[36] = true;
			return true;
		case 101:
			GameCanvas.keyMyHold[43] = true;
			GameCanvas.keyMyPressed[43] = true;
			return true;
		case 49:
		case 103:
			GameCanvas.keyMyHold[31] = true;
			GameCanvas.keyMyPressed[31] = true;
			return true;
		case 50:
		case 104:
			GameCanvas.keyMyHold[33] = true;
			GameCanvas.keyMyPressed[33] = true;
			return true;
		case 105:
			GameCanvas.keyMyHold[46] = true;
			GameCanvas.keyMyPressed[46] = true;
			return true;
		case 51:
		case 106:
			GameCanvas.keyMyHold[35] = true;
			GameCanvas.keyMyPressed[35] = true;
			return true;
		case 52:
		case 107:
			GameCanvas.keyMyHold[37] = true;
			GameCanvas.keyMyPressed[37] = true;
			return true;
		case 53:
		case 108:
			GameCanvas.keyMyHold[39] = true;
			GameCanvas.keyMyPressed[39] = true;
			return true;
		case 109:
			GameCanvas.keyMyHold[42] = true;
			GameCanvas.keyMyPressed[42] = true;
			return true;
		case 111:
			GameCanvas.keyMyHold[44] = true;
			GameCanvas.keyMyPressed[44] = true;
			return true;
		case 112:
			GameCanvas.keyMyHold[50] = true;
			GameCanvas.keyMyPressed[50] = true;
			return true;
		case 113:
			GameCanvas.keyMyHold[47] = true;
			GameCanvas.keyMyPressed[47] = true;
			return true;
		case 115:
			GameCanvas.keyMyHold[38] = true;
			GameCanvas.keyMyPressed[38] = true;
			return true;
		case 119:
			GameCanvas.keyMyHold[32] = true;
			GameCanvas.keyMyPressed[32] = true;
			return true;
		case 120:
			GameCanvas.keyMyHold[49] = true;
			GameCanvas.keyMyPressed[49] = true;
			return true;
		case 121:
			GameCanvas.keyMyHold[45] = true;
			GameCanvas.keyMyPressed[45] = true;
			return true;
		case 32:
			GameCanvas.keyMyHold[5] = true;
			GameCanvas.keyMyPressed[5] = true;
			return true;
		case -26:
			GameCanvas.keyMyHold[41] = true;
			GameCanvas.keyMyPressed[41] = true;
			return true;
		case -21:
			GameCanvas.keyMyHold[40] = true;
			GameCanvas.keyMyPressed[40] = true;
			GameCanvas.keyMyHold[12] = true;
			GameCanvas.keyMyPressed[12] = true;
			return true;
		case -27:
		case -22:
			GameCanvas.keyMyHold[41] = true;
			GameCanvas.keyMyPressed[41] = true;
			GameCanvas.keyMyHold[13] = true;
			GameCanvas.keyMyPressed[13] = true;
			return true;
		}
		return false;
	}

	public bool keyReleasedPc(int keycode)
	{
		switch (keycode)
		{
		case 97:
			GameCanvas.keyMyHold[34] = false;
			GameCanvas.keyMyPressed[34] = false;
			return true;
		case 98:
			GameCanvas.keyMyHold[51] = false;
			GameCanvas.keyMyPressed[51] = false;
			return true;
		case 99:
			GameCanvas.keyMyHold[48] = false;
			GameCanvas.keyMyPressed[48] = false;
			return true;
		case 100:
			GameCanvas.keyMyHold[36] = false;
			GameCanvas.keyMyPressed[36] = false;
			return true;
		case 101:
			GameCanvas.keyMyHold[43] = false;
			GameCanvas.keyMyPressed[43] = false;
			return true;
		case 49:
		case 103:
			GameCanvas.keyMyHold[31] = false;
			GameCanvas.keyMyPressed[31] = false;
			return true;
		case 50:
		case 104:
			GameCanvas.keyMyHold[33] = false;
			GameCanvas.keyMyPressed[33] = false;
			return true;
		case 105:
			GameCanvas.keyMyHold[46] = false;
			GameCanvas.keyMyPressed[46] = false;
			return true;
		case 51:
		case 106:
			GameCanvas.keyMyHold[35] = false;
			GameCanvas.keyMyPressed[35] = false;
			return true;
		case 52:
		case 107:
			GameCanvas.keyMyHold[37] = false;
			GameCanvas.keyMyPressed[37] = false;
			return true;
		case 53:
		case 108:
			GameCanvas.keyMyHold[39] = false;
			GameCanvas.keyMyPressed[39] = false;
			return true;
		case 109:
			GameCanvas.keyMyHold[42] = false;
			GameCanvas.keyMyPressed[42] = false;
			return true;
		case 111:
			GameCanvas.keyMyHold[44] = false;
			GameCanvas.keyMyPressed[44] = false;
			return true;
		case 112:
			GameCanvas.keyMyHold[50] = false;
			GameCanvas.keyMyPressed[50] = false;
			return true;
		case 113:
			GameCanvas.keyMyHold[47] = false;
			GameCanvas.keyMyPressed[47] = false;
			return true;
		case 115:
			GameCanvas.keyMyHold[38] = false;
			GameCanvas.keyMyPressed[38] = false;
			return true;
		case 119:
			GameCanvas.keyMyHold[32] = false;
			GameCanvas.keyMyPressed[32] = false;
			return true;
		case 120:
			GameCanvas.keyMyHold[49] = false;
			GameCanvas.keyMyPressed[49] = false;
			return true;
		case 121:
			GameCanvas.keyMyHold[45] = false;
			GameCanvas.keyMyPressed[45] = false;
			return true;
		case 32:
			GameCanvas.keyMyHold[5] = false;
			GameCanvas.keyMyPressed[5] = false;
			return true;
		case -26:
			GameCanvas.keyMyHold[41] = false;
			GameCanvas.keyMyPressed[41] = false;
			return true;
		case -21:
			GameCanvas.keyMyHold[40] = false;
			GameCanvas.keyMyPressed[40] = false;
			GameCanvas.keyMyHold[12] = false;
			GameCanvas.keyMyPressed[12] = false;
			return true;
		case -27:
		case -22:
			GameCanvas.keyMyHold[41] = false;
			GameCanvas.keyMyPressed[41] = false;
			GameCanvas.keyMyHold[13] = false;
			GameCanvas.keyMyPressed[13] = false;
			return true;
		}
		return false;
	}
}
