using System;

public class ipKeyboard
{
	public static TouchScreenKeyboard tk;

	public static int TEXT = 0;

	public static int NUMBERIC = 1;

	public static int PASS = 2;

	private static IKAction act;

	public static void openKeyBoard(string caption, int type, string text, IKAction action)
	{
		act = action;
		if (!GameMidlet.isPC)
		{
			TouchScreenKeyboardType t = ((type == 0 || type == 2) ? TouchScreenKeyboardType.ASCIICapable : TouchScreenKeyboardType.NumberPad);
			TouchScreenKeyboard.hideInput = false;
			tk = TouchScreenKeyboard.Open(text, t, false, false, type == 2, false, caption);
		}
	}

	public static void update()
	{
		try
		{
			if (tk != null && tk.done)
			{
				if (act != null && tk.text != null)
				{
					act.perform(tk.text);
				}
				tk = null;
			}
		}
		catch (Exception)
		{
		}
	}
}
