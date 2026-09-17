using UnityEngine;

public enum MobileKeyboardState
{
	Closed,
	Opening,
	Focused,
	KeyboardVisible,
	Editing,
	Confirming,
	Committed,
	Closing
}

public class MobileInputManager
{
	public static bool enableInputDebug = false;

	public static MobileKeyboardState KeyboardState = MobileKeyboardState.Closed;

	public static int safeLeft = 0;
	public static int safeRight = 0;
	public static int safeTop = 0;
	public static int safeBottom = 0;

	private static Rect lastSafeArea = new Rect(0f, 0f, 0f, 0f);
	private static int lastScreenWidth = 0;
	private static int lastScreenHeight = 0;
	private static int stabilizedKbHeight = 0;

	public static void Log(string tag, string message)
	{
		if (enableInputDebug)
		{
			Debug.Log("[" + tag + "] " + message);
		}
	}

	public static bool IsKeyboardOpeningOrActive()
	{
		return TouchScreenKeyboard.visible;
	}

	public static void Update()
	{
		UpdateSafeArea();
		CheckBackKey();
	}

	public static void UpdateSafeArea()
	{
		if (lastScreenWidth != Screen.width || lastScreenHeight != Screen.height || lastSafeArea != Screen.safeArea)
		{
			lastScreenWidth = Screen.width;
			lastScreenHeight = Screen.height;
			lastSafeArea = Screen.safeArea;

			int zoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
			safeLeft = (int)(lastSafeArea.xMin / (float)zoom);
			safeBottom = (int)(lastSafeArea.yMin / (float)zoom);
			safeRight = (int)(((float)Screen.width - lastSafeArea.xMax) / (float)zoom);
			safeTop = (int)(((float)Screen.height - lastSafeArea.yMax) / (float)zoom);

			if (safeLeft < 0) safeLeft = 0;
			if (safeRight < 0) safeRight = 0;
			if (safeTop < 0) safeTop = 0;
			if (safeBottom < 0) safeBottom = 0;
		}
	}

	private static void CheckBackKey()
	{
		if (Application.platform == RuntimePlatform.Android && Input.GetKeyDown(KeyCode.Escape))
		{
			if (HandleBackKey())
			{
				GameCanvas.clearKeyPressed(13);
				GameCanvas.clearKeyHold(13);
			}
		}
	}

	public static bool HandleBackKey()
	{
		if (TouchScreenKeyboard.visible)
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
			stabilizedKbHeight = 0;
			GameCanvas.clearKeyPressed(13);
			GameCanvas.clearKeyHold(13);
			return true;
		}
		return false;
	}

	public static int GetKeyboardHeightVirtual()
	{
		if (TouchScreenKeyboard.visible && mGraphics.zoomLevel > 0)
		{
			float rawHeight = TouchScreenKeyboard.area.height;
			if (rawHeight > 0)
			{
				int newH = (int)(rawHeight / (float)mGraphics.zoomLevel);
				if (newH > stabilizedKbHeight)
				{
					stabilizedKbHeight = newH;
				}
				return stabilizedKbHeight;
			}
		}
		else
		{
			stabilizedKbHeight = 0;
		}
		return stabilizedKbHeight;
	}
}
