using System;
using UnityEngine;

public class GameMidlet
{
	public class connectOk : IKAction
	{
		public void perform(string text)
		{
			GameCanvas.infoDisConnect = text;
		}
	}

	public static string IP = "112.213.94.23";

	public static int PORT = 14445;

	public static sbyte PROVIDER = 0;

	public static byte DEVICE = 2;

	public const byte JAVA = 0;

	public const byte ANDROID = 1;

	public const byte PC = 2;

	public const byte IOS = 3;

	public const byte WP = 4;

	public const byte ANDROID_STORE = 5;

	public const byte IOS_STORE = 6;

	public static byte ZOOM_IOS = 4;

	public static GameCanvas gameCanvas;

	public static GameMidlet instance;

	public static bool isPC;

	public static string strLinkServerCheckENG = "http://54.255.184.239/service/thongbao.txt";

	public static string strLinkServerCheck = "http://teamobi.com/services/ht/t.txt";

	public static string strLinkServerCheck2 = "http://teamobi.com/services/ht/t2.txt";

	public static string LONG = "";

	public static string LAT = "";

	public static string[] google_productIds = null;

	public static string[] google_listGems = null;

	public static string[] google_productIds_Eng = null;

	public static bool isTest = false;

	public static int numTest = 0;

	public static bool isBackWindowsPhone;

	private static connectOk achttp;

	public GameMidlet()
	{
		initGame();
	}

	public void initGame()
	{
		instance = this;
		MotherCanvas.instance = new MotherCanvas();
		instance = this;
		MotherCanvas.instance.checkZoomLevel();
		google_productIds = new string[5] { "htthgem25", "htthgem150", "htthgem350", "htthgem800", "htthgem2500" };
		google_listGems = new string[7] { "0,99$ - 20k Extol + 4 Ruby", "2,99$ - 60k Extol + 20 Ruby", "100$ - 1M Extol + 5k Ruby", "650 Gems ($9.99)", "1400 Gems ($24.99)", "3750 Gems ($49.99)", "8000 Gems ($49.99)" };
		google_productIds_Eng = new string[5] { "pwgem25", "pwgem150", "pwgem350", "pwgem800", "pwgem2500" };
		gameCanvas = new GameCanvas();
		AThMadaraMOD.loadSpeedSetting();
		AThMadaraMOD.loadAutoSellList();
		KeyAuthManager.init();
	}

	public void exit()
	{
		mSystem.gcc();
		notifyDestroyed();
	}

	public static void sendSMS(string data, string to, iCommand successAction, iCommand failAction)
	{
		mSystem.outz("SEND SMS");
	}

	public static void flatForm(string url)
	{
		mSystem.outz("PLATFORM: " + url);
		Application.OpenURL(url);
	}

	public void notifyDestroyed()
	{
		Main.exit();
	}

	public void destroy()
	{
		Main.exit();
	}

	public void platformRequest(string url)
	{
		mSystem.outz("PLATFORM REQUEST: " + url);
		Application.OpenURL(url);
	}

	public static void openUrl(string url)
	{
		mSystem.outz("openUrl  url = " + url);
		Application.OpenURL(url);
	}

	public static void saveRMS(string filename, sbyte[] data)
	{
		Rms.saveRMS(filename, data);
	}

	public static sbyte[] loadRMS(string filename)
	{
		return Rms.loadRMS(filename);
	}

	public static string loginPlus()
	{
		return "";
	}

	public static void delRMS(string name)
	{
		Rms.deleteRecord(name);
	}

	public static void delRMS()
	{
		Rms.clearRMS();
	}

	public static DataInputStream openFile(string path)
	{
		try
		{
			return DataInputStream.getResourceAsStream(Main.res + path);
		}
		catch (Exception e)
		{
			Out.printError(e);
		}
		return null;
	}

	public static bool isIosNetwork()
	{
		return false;
	}

	public static string SubStr(string str, int begin, int end)
	{
		return str.Substring(begin, end - 1);
	}

	public static void delAllRms()
	{
		Rms.clearAll();
		Rms.clearRMS();
	}

	public static void closeApp()
	{
	}

	public static string format(string strformat, string str)
	{
		object[] args = new string[1] { str };
		return string.Format(strformat, args);
	}

	public static string format(string strformat, string[] args)
	{
		try
		{
			return string.Format(strformat, args);
		}
		catch (Exception)
		{
			mSystem.outloi("loi ghep chuoi");
		}
		return "loi ghep chuoi";
	}

	public static string fixString(string chat)
	{
		if (string.IsNullOrEmpty(chat))
		{
			return "";
		}
		chat = chat.Replace("\\n", "\n");
		return chat;
	}


	public static string connectHTTP(string Url)
	{
		mSystem.outz("connect HTTP URL = " + Url);
		achttp = new connectOk();
		Net.connectHTTP(Url, achttp);
		return "";
	}

	public void removeEditText()
	{
	}

	public static string getThongBao()
	{
		if (GameCanvas.language != 0)
		{
			return strLinkServerCheckENG;
		}
		if (GameCanvas.IndexServer == 0)
		{
			return strLinkServerCheck;
		}
		return strLinkServerCheck2;
	}

	public void CheckPerGPS()
	{
		GlobalService.gI().send_GPS();
	}

	public static void makePurchase(string str)
	{
	}

	public static void loginOk()
	{
	}

	public static void CheckThanhToan(string str)
	{
	}
}
