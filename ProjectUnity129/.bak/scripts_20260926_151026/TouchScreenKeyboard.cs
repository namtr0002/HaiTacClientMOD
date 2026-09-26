using UnityEngine;

public class TouchScreenKeyboard
{
	public static bool hideInput
	{
		get { return UnityEngine.TouchScreenKeyboard.hideInput; }
		set { UnityEngine.TouchScreenKeyboard.hideInput = value; }
	}

	public static bool visible;

	public static Rect area
	{
		get { return UnityEngine.TouchScreenKeyboard.area; }
	}

	public bool done;

	public bool active;

	public string text;

	private UnityEngine.TouchScreenKeyboard unityKeyboard;

	public static TouchScreenKeyboard Open(string text, TouchScreenKeyboardType t, bool b1 = false, bool b2 = false, bool type = false, bool b3 = false, string caption = "")
	{
		if (Application.platform == RuntimePlatform.IPhonePlayer || Application.platform == RuntimePlatform.Android || UnityEngine.TouchScreenKeyboard.isSupported)
		{
			UnityEngine.TouchScreenKeyboardType unityType = (UnityEngine.TouchScreenKeyboardType)(int)t;
			UnityEngine.TouchScreenKeyboard unityKb = UnityEngine.TouchScreenKeyboard.Open(text ?? "", unityType, b1, b2, type, b3, caption ?? "");
			
			if (unityKb != null)
			{
				TouchScreenKeyboard wrapper = new TouchScreenKeyboard();
				wrapper.unityKeyboard = unityKb;
				wrapper.text = text ?? "";
				wrapper.active = true;
				wrapper.done = false;
				visible = true;
				return wrapper;
			}
		}
		visible = false;
		return null;
	}

	public static void Clear()
	{
		visible = false;
	}

	public void Update()
	{
		if (unityKeyboard != null)
		{
			text = unityKeyboard.text;
			active = unityKeyboard.active;
			visible = active;
			done = unityKeyboard.status == UnityEngine.TouchScreenKeyboard.Status.Done || 
			       unityKeyboard.status == UnityEngine.TouchScreenKeyboard.Status.Canceled;
		}
	}
}
