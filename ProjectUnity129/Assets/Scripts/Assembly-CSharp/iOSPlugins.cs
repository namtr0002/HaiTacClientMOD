using System.Runtime.InteropServices;
using UnityEngine;

public class iOSPlugins
{
	public static string devide;

	public static string Myname;

#if UNITY_IPHONE || UNITY_IOS
#if UNITY_IPHONE || UNITY_IOS
#if UNITY_IPHONE || UNITY_IOS
	[DllImport("__Internal")]
	private static extern void _SMSsend(string tophone, string withtext, int n);

	[DllImport("__Internal")]
	private static extern int _unpause();

	[DllImport("__Internal")]
	private static extern int _checkRotation();

	[DllImport("__Internal")]
	private static extern int _back();

	[DllImport("__Internal")]
	private static extern int _Send();

	[DllImport("__Internal")]
	private static extern void _purchaseItem(string itemID, string userName, string gameID);
#endif
#endif
#endif

	public static int Check()
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer)
		{
			return checkCanSendSMS();
		}
		try
		{
			devide = iPhoneSettings.generation.ToString();
			if (!string.IsNullOrEmpty(devide) && devide.Length > 6 && devide.Length > 2 && devide[2] == 'h')
			{
				Myname = SystemInfo.operatingSystem.ToString();
				if (!string.IsNullOrEmpty(Myname) && Myname.Length > 10)
				{
					string text = Myname[10].ToString() ?? "";
					if (text != "2" && text != "3")
					{
						return 0;
					}
					return 1;
				}
			}
			Cout.println(devide + "  loai");
			if (devide == "Unknown" && ScaleGUI.WIDTH * ScaleGUI.HEIGHT < 786432f)
			{
				return 0;
			}
		}
		catch { }
		return -1;
	}

	public static int checkCanSendSMS()
	{
		if (iPhoneSettings.generation == iPhoneGeneration.iPhone3GS || iPhoneSettings.generation == iPhoneGeneration.iPhone4 || iPhoneSettings.generation == iPhoneGeneration.iPhone4S || iPhoneSettings.generation == iPhoneGeneration.iPhone5)
		{
			return 0;
		}
		return -1;
	}

	public static void SMSsend(string phonenumber, string bodytext, int n)
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_SMSsend(phonenumber, bodytext, n);
		}
#endif
	}

	public static void back()
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_back();
		}
#endif
	}

	public static void Send()
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_Send();
		}
#endif
	}

	public static int unpause()
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			return _unpause();
		}
#endif
		return 0;
	}

	public static int checkRotation()
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			return _checkRotation();
		}
#endif
		return 0;
	}

	public static void purchaseItem(string itemID, string userName, string gameID)
	{
#if UNITY_IPHONE || UNITY_IOS
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_purchaseItem(itemID, userName, gameID);
		}
#endif
	}
}
