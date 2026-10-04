using System;
using System.Collections;
using System.Collections.Generic;
using System.Net.NetworkInformation;
using System.Threading;
using UnityEngine;

[System.Reflection.Obfuscation(Exclude = true, ApplyToMembers = false)]
public class Main : MonoBehaviour
{
	public static Main main;

	public static mGraphics g;

	public static GameMidlet midlet;

	public static string res = "res";

	public static string mainThreadName;

	public static int mainThreadId;

	public static Thread mainThread;

	private static readonly Queue<Action> mainThreadActions = new Queue<Action>();

	public static bool isMainThread
	{
		get
		{
			if (mainThreadId != 0)
			{
				return Thread.CurrentThread.ManagedThreadId == mainThreadId;
			}
			if (mainThread != null)
			{
				return Thread.CurrentThread == mainThread;
			}
			return false;
		}
	}

	public static void runOnMainThread(Action action)
	{
		if (action == null) return;
		if (isMainThread)
		{
			try
			{
				action();
			}
			catch (Exception ex)
			{
				Cout.LogError("MainThreadAction: " + ex);
			}
			return;
		}
		lock (mainThreadActions)
		{
			mainThreadActions.Enqueue(action);
		}
	}

	public static bool started = false;

	public static bool isIpod;

	public static bool isIphone4;

	public static bool isWindowsPhone;

	public static bool isIPhone;

	public static bool IphoneVersionApp;

	public static string IMEI;

	public static int versionIp = 0;

	public static int numberQuit = 1;

	public static int typeClient = 4;

	public const sbyte PC_VERSION = 4;

	public const sbyte IP_APPSTORE = 5;

	public const sbyte WINDOWSPHONE = 6;

	public const sbyte IP_JB = 3;

	private Queue<IEnumerator> jobs = new Queue<IEnumerator>();

	private int updateCount;

	private int paintCount;

	private int count;

	private bool isRun;

	public static int waitTick;

	public static int f;

	public static bool isResume;

	public static bool isMiniApp = true;

	public static bool isQuitApp;

	private Vector2 lastMousePos;

	public static int a = 1;

	public static bool isCompactDevice = true;

	private void Start()
	{
		ScaleGUI.initScaleGUI();
		lastScreenWidth = Screen.width;
		lastScreenHeight = Screen.height;
		if (!started)
		{
			try
			{
				if (string.IsNullOrEmpty(Thread.CurrentThread.Name))
				{
					Thread.CurrentThread.Name = "Main";
				}
			}
			catch { }
			mainThreadName = Thread.CurrentThread.Name ?? "Main";
			GameMidlet.isPC = (Application.platform == RuntimePlatform.WindowsPlayer || Application.platform == RuntimePlatform.WindowsEditor || Application.platform == RuntimePlatform.OSXPlayer || Application.platform == RuntimePlatform.OSXEditor || Application.platform == RuntimePlatform.LinuxPlayer);
			if (Application.platform == RuntimePlatform.Android) {
				GameMidlet.DEVICE = GameMidlet.ANDROID;
				typeClient = 1;
			} else if (Application.platform == RuntimePlatform.IPhonePlayer) {
				GameMidlet.DEVICE = GameMidlet.IOS;
				typeClient = 5;
			} else if (GameMidlet.isPC) {
				GameMidlet.DEVICE = GameMidlet.PC;
				typeClient = 4;
			}
			started = true;
			GameCanvas.readGraphicsPC();
			if (GameMidlet.isPC)
			{
				if (GameCanvas.lv == 0)
				{
					Screen.SetResolution(600, 355, fullscreen: false);
				}
				else
				{
					Screen.SetResolution(1024, 550, fullscreen: false);
				}
			}
		}
	}

	private void SetInit()
	{
		base.enabled = true;
	}

	private void OnHideUnity(bool isGameShown)
	{
		if (!isGameShown)
		{
			Time.timeScale = 0f;
		}
		else
		{
			Time.timeScale = 1f;
		}
	}

	private void OnGUI()
	{
		if (count >= 10)
		{
			if (GameMidlet.gameCanvas != null && g != null)
			{
				checkInput();
				if (Event.current != null && Event.current.type.Equals(EventType.Repaint))
				{
					try
					{
						GameMidlet.gameCanvas.paint(g);
					}
					catch (Exception ex)
					{
						Debug.LogError("[GameCanvas.paint Error] " + ex);
					}
					paintCount++;
					MotherCanvas.updateFPSCounter();
					g.reset();
				}
			}
		}
	}

	public void setsizeChange()
	{
		if (!isRun)
		{
			Screen.orientation = ScreenOrientation.LandscapeLeft;
			Application.runInBackground = true;
			MotherCanvas.loadFPSSetting();
			MotherCanvas.loadZoomSetting();
			MotherCanvas.loadOrientationSetting();
			base.useGUILayout = false;
			ScaleGUI.initScaleGUI();
			isCompactDevice = detectCompactDevice();
			if (main == null)
			{
				main = this;
			}
			try
			{
				IMEI = SystemInfo.deviceUniqueIdentifier;
				if (string.IsNullOrEmpty(IMEI))
				{
					IMEI = GetMacAddress();
				}
			}
			catch
			{
				IMEI = "";
			}
			if (string.IsNullOrEmpty(IMEI))
			{
				IMEI = System.Guid.NewGuid().ToString("N");
			}
			GameMidlet.isPC = (Application.platform == RuntimePlatform.WindowsPlayer || Application.platform == RuntimePlatform.WindowsEditor || Application.platform == RuntimePlatform.OSXPlayer || Application.platform == RuntimePlatform.OSXEditor || Application.platform == RuntimePlatform.LinuxPlayer);
			if (GameMidlet.isPC)
			{
				Screen.fullScreen = false;
			}
			else
			{
				Screen.fullScreen = true;
			}
			if (isWindowsPhone)
			{
				typeClient = 6;
			}
			if (GameMidlet.isPC)
			{
				typeClient = 4;
			}
			if (IphoneVersionApp)
			{
				typeClient = 5;
			}
			if (iPhoneSettings.generation == iPhoneGeneration.iPodTouch4Gen)
			{
				isIpod = true;
			}
			if (iPhoneSettings.generation == iPhoneGeneration.iPhone4)
			{
				isIphone4 = true;
			}
			g = new mGraphics();
			midlet = new GameMidlet();
			Key.mapKeyPC();
			isRun = true;
		}
	}

	public static void setBackupIcloud(string path)
	{
	}

	public string GetMacAddress()
	{
		try
		{
			NetworkInterface[] allNetworkInterfaces = NetworkInterface.GetAllNetworkInterfaces();
			for (int i = 0; i < allNetworkInterfaces.Length; i++)
			{
				PhysicalAddress physicalAddress = allNetworkInterfaces[i].GetPhysicalAddress();
				if (physicalAddress != null && physicalAddress.ToString() != "")
				{
					return physicalAddress.ToString();
				}
			}
		}
		catch { }
		return "";
	}

	public void doClearRMS()
	{
	}

	public static void closeKeyBoard()
	{
		if (TField.kb != null)
		{
			TField.kb.active = false;
			TField.kb = null;
		}
		if (TField.currentTField != null)
		{
			TField.currentTField.isFocus = false;
			TField.currentTField = null;
		}
	}

	private void FixedUpdate()
	{
		MobileInputManager.Update();
		Rms.update();
		count++;
		if (count < 10)
		{
			return;
		}
		Image.update();
		setsizeChange();
		updateCount++;
		ipKeyboard.update();
		Session_ME.update();
		int speed = AThMadaraMOD.gameSpeed;
		if (speed < 1) speed = 1;
		if (speed > 10) speed = 10;
		if (GameMidlet.gameCanvas != null)
		{
			for (int i = 0; i < speed; i++)
			{
				try
				{
					GameMidlet.gameCanvas.update();
				}
				catch (Exception)
				{
				}
			}
		}
		DataInputStream.update();
		SMS.update();
		Net.update();
		f++;
		if (f > 8)
		{
			f = 0;
		}
		if (GameCanvas.isDisConnect || AThMadaraMOD.pendingDisconnect)
		{
			GameCanvas.isDisConnect = false;
			AThMadaraMOD.pendingDisconnect = false;
			string info = T.disconnect;
			if (AThMadaraMOD.pendingDisconnectMsg != null && AThMadaraMOD.pendingDisconnectMsg.Length > 0)
			{
				info = AThMadaraMOD.pendingDisconnectMsg;
				AThMadaraMOD.pendingDisconnectMsg = "";
			}
			else if (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.Length > 0)
			{
				info = GameCanvas.infoDisConnect;
				GameCanvas.infoDisConnect = "";
			}

			// Clean session and player
			Session_ME.gI().close();
			GameScreen.player = null;

			// If not already on login screen, return to login screen
			if (GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.fristLoginScr)
			{
				GameCanvas.loginScr.Show();
			}

			// Show disconnect dialog on top of login screen
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog(info);
		}
	}

	private void Awake()
	{
		main = this;
		mainThread = Thread.CurrentThread;
		mainThreadId = Thread.CurrentThread.ManagedThreadId;
		base.useGUILayout = false;
		GameMidlet.isPC = (Application.platform == RuntimePlatform.WindowsPlayer || Application.platform == RuntimePlatform.WindowsEditor || Application.platform == RuntimePlatform.OSXPlayer || Application.platform == RuntimePlatform.OSXEditor || Application.platform == RuntimePlatform.LinuxPlayer);
		if (Application.platform == RuntimePlatform.Android)
		{
			GameMidlet.DEVICE = GameMidlet.ANDROID;
			typeClient = 1;
		}
		else if (Application.platform == RuntimePlatform.IPhonePlayer)
		{
			GameMidlet.DEVICE = GameMidlet.IOS;
			typeClient = 5;
		}
		else if (GameMidlet.isPC)
		{
			GameMidlet.DEVICE = GameMidlet.PC;
			typeClient = 4;
		}
	}

	private int lastScreenWidth;

	private int lastScreenHeight;

	private void Update()
	{
		if (lastScreenWidth != Screen.width || lastScreenHeight != Screen.height)
		{
			lastScreenWidth = Screen.width;
			lastScreenHeight = Screen.height;
			if (isRun)
			{
				MotherCanvas.refreshDisplay();
			}
		}

		while (jobs.Count > 0)
		{
			StartCoroutine(jobs.Dequeue());
		}

		lock (mainThreadActions)
		{
			while (mainThreadActions.Count > 0)
			{
				try
				{
					mainThreadActions.Dequeue()?.Invoke();
				}
				catch (Exception ex)
				{
					Cout.LogError("Error in main thread action: " + ex);
				}
			}
		}
	}

	internal void AddJob(IEnumerator newJob)
	{
		jobs.Enqueue(newJob);
	}

	private void checkInput()
	{
		int zoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
		if (Input.touchCount > 0)
		{
			// Find primary touch for GameCanvas (prioritize touch not on left D-pad if multi-touch)
			Touch chosenTouch = Input.GetTouch(0);
			if (Input.touchCount > 1)
			{
				int dpadRadius = 70;
				int dpadX = GameCanvas.isTaiTho ? 75 : 62;
				int dpadY = MotherCanvas.h - (GameCanvas.isTaiTho ? 65 : 60);
				for (int i = 0; i < Input.touchCount; i++)
				{
					Touch t = Input.GetTouch(i);
					int tx = (int)(t.position.x / (float)zoom);
					int ty = (int)(((float)Screen.height - t.position.y) / (float)zoom);
					if (CRes.abs(tx - dpadX) > dpadRadius || CRes.abs(ty - dpadY) > dpadRadius)
					{
						chosenTouch = t;
						break;
					}
				}
			}

			int px = (int)(chosenTouch.position.x / (float)zoom);
			int py = (int)(((float)Screen.height - chosenTouch.position.y) / (float)zoom);
			lastMousePos.x = px;
			lastMousePos.y = py;

			if (chosenTouch.phase == TouchPhase.Began)
			{
				GameMidlet.gameCanvas.onPointerPressed(px, py);
			}
			else if (chosenTouch.phase == TouchPhase.Moved || chosenTouch.phase == TouchPhase.Stationary)
			{
				GameMidlet.gameCanvas.onPointerDragged(px, py);
			}
			else if (chosenTouch.phase == TouchPhase.Ended || chosenTouch.phase == TouchPhase.Canceled)
			{
				GameMidlet.gameCanvas.onPointerReleased(px, py);
			}
		}
		else
		{
			if (Input.GetMouseButtonDown(0))
			{
				Vector3 mousePosition = Input.mousePosition;
				int px = (int)(mousePosition.x / (float)zoom);
				int py = (int)(((float)Screen.height - mousePosition.y) / (float)zoom);
				GameMidlet.gameCanvas.onPointerPressed(px, py);
				lastMousePos.x = px;
				lastMousePos.y = py;
			}
			else if (Input.GetMouseButton(0))
			{
				Vector3 mousePosition2 = Input.mousePosition;
				int px = (int)(mousePosition2.x / (float)zoom);
				int py = (int)(((float)Screen.height - mousePosition2.y) / (float)zoom);
				GameMidlet.gameCanvas.onPointerDragged(px, py);
				lastMousePos.x = px;
				lastMousePos.y = py;
			}
			if (Input.GetMouseButtonUp(0))
			{
				Vector3 mousePosition3 = Input.mousePosition;
				int px = (int)(mousePosition3.x / (float)zoom);
				int py = (int)(((float)Screen.height - mousePosition3.y) / (float)zoom);
				lastMousePos.x = px;
				lastMousePos.y = py;
				GameMidlet.gameCanvas.onPointerReleased(px, py);
			}
		}

		if (Event.current != null)
		{
			if (Event.current.type == EventType.KeyDown)
			{
				int num = MyKeyMap.map(Event.current.keyCode);
				if (Input.GetKey(KeyCode.LeftShift) || Input.GetKey(KeyCode.RightShift))
				{
					switch (Event.current.keyCode)
					{
					case KeyCode.Alpha2:
						num = 64;
						break;
					case KeyCode.Minus:
						num = 95;
						break;
					}
				}
				if (num != 0)
				{
					GameMidlet.gameCanvas.keyPressed(num);
					Event.current.Use();
				}
			}
			else if (Event.current.type == EventType.KeyUp)
			{
				int num2 = MyKeyMap.map(Event.current.keyCode);
				if (num2 != 0)
				{
					GameMidlet.gameCanvas.keyReleased(num2);
					Event.current.Use();
				}
			}
		}
	}


	private void OnApplicationQuit()
	{
		Debug.LogWarning("APP QUIT");
		Session_ME.gI().close();
		if (GameMidlet.isPC)
		{
			Application.Quit();
		}
	}

	private void OnApplicationPause(bool paused)
	{
		isResume = !paused;
		if (isQuitApp)
		{
			Application.Quit();
		}
	}

	public static void exit()
	{
		isQuitApp = true;
		if (main != null)
		{
			main.OnApplicationQuit();
		}
		Application.Quit();
	}

	public static bool detectCompactDevice()
	{
		if (iPhoneSettings.generation == iPhoneGeneration.iPhone || iPhoneSettings.generation == iPhoneGeneration.iPhone3G || iPhoneSettings.generation == iPhoneGeneration.iPodTouch1Gen || iPhoneSettings.generation == iPhoneGeneration.iPodTouch2Gen)
		{
			return false;
		}
		return true;
	}

	public static bool checkCanSendSMS()
	{
		if (iPhoneSettings.generation == iPhoneGeneration.iPhone3GS || iPhoneSettings.generation == iPhoneGeneration.iPhone4 || iPhoneSettings.generation > iPhoneGeneration.iPodTouch4Gen)
		{
			return true;
		}
		return false;
	}
}
