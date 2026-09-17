import re
import os

base_dir = r'C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Scripts\Assembly-CSharp'

def repl(path, old, new):
    p = os.path.join(base_dir, path)
    with open(p, 'r', encoding='utf-8') as f:
        content = f.read()
    if old in content:
        content = content.replace(old, new)
        with open(p, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f'Replaced in {path}')
    else:
        print(f'Not found in {path}')

# 1. AvMain.cs
repl('AvMain.cs', 'string text = "";\n\t\tif (m >= 1000000000)', 'if (m >= 1000000000)')

# 2. LoadMap.cs
repl('LoadMap.cs', 'for (int num = 0; num <= w; num = num)', 'for (int num = 0; num <= w;)')
repl('LoadMap.cs', 'for (int num2 = 0; num2 <= h; num2 = num2)', 'for (int num2 = 0; num2 <= h;)')

# 3. LoginScreen.cs
login_old = '''		case 11:
			if (GameCanvas.language == 1)
			{
				switch (subIndex)
				{
				}
			}
			else
			{
				switch (subIndex)
				{
				}
			}
			break;'''
login_new = '''		case 11:
			break;'''
repl('LoginScreen.cs', login_old, login_new)

# 4. Main.cs
repl('Main.cs', 'g.CreateLineMaterial();\n', '')

# 5. Interface_Game.cs
repl('Interface_Game.cs', 'int num = 5;\n\t\t\t\tg.drawRegion', 'g.drawRegion')

# 6. MainQuest.cs
repl('MainQuest.cs', 'iCommand iCommand2 = null;\n\t\tif (step < mstrTalk.Length - 1)', 'if (step < mstrTalk.Length - 1)')

# 7. myReader.cs
myr_old = '''		for (int i = 0; i < 8; i++)
		{
			num <<= 8;
			num |= 0xFF & buffer[posRead++];
		}'''
myr_new = '''		for (int i = 0; i < 8; i++)
		{
			num <<= 8;
			num |= (long)(0xFF & buffer[posRead++]);
		}'''
repl('myReader.cs', myr_old, myr_new)

# 8. WantedScreen.cs
w_old = '''	public override void updatekey()
	{
		bool flag = false;
		if (GameCanvas.keyMove(0))
		{
			if (idSelect > 0)
			{
				idSelect--;
			}
			GameCanvas.ClearkeyMove(0);
			flag = true;
		}
		else if (GameCanvas.keyMove(2))
		{
			if (idSelect < vecList.size() - 1)
			{
				idSelect++;
			}
			GameCanvas.ClearkeyMove(2);
			flag = true;
		}'''
w_new = '''	public override void updatekey()
	{
		if (GameCanvas.keyMove(0))
		{
			if (idSelect > 0)
			{
				idSelect--;
			}
			GameCanvas.ClearkeyMove(0);
		}
		else if (GameCanvas.keyMove(2))
		{
			if (idSelect < vecList.size() - 1)
			{
				idSelect++;
			}
			GameCanvas.ClearkeyMove(2);
		}'''
repl('WantedScreen.cs', w_old, w_new)

# 9. ReadMessenge.cs
repl('ReadMessenge.cs', 'string text = "";\n\t\t\t\t\tmVector2.addElement(new MainInfoItem(b3 switch', 'mVector2.addElement(new MainInfoItem(b3 switch')

# 10. iOSPlugins.cs
ios_old = '''	[DllImport("__Internal")]
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
	private static extern void _purchaseItem(string itemID, string userName, string gameID);'''

ios_new = '''#if UNITY_IPHONE || UNITY_IOS
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
#endif'''

ios_old_2 = '''	public static void SMSsend(string phonenumber, string bodytext, int n)
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_SMSsend(phonenumber, bodytext, n);
		}
	}

	public static void back()
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_back();
		}
	}

	public static void Send()
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_Send();
		}
	}

	public static int unpause()
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			return _unpause();
		}
		return 0;
	}

	public static int checkRotation()
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			return _checkRotation();
		}
		return 0;
	}

	public static void purchaseItem(string itemID, string userName, string gameID)
	{
		if (Application.platform != RuntimePlatform.OSXEditor)
		{
			_purchaseItem(itemID, userName, gameID);
		}
	}'''

ios_new_2 = '''	public static void SMSsend(string phonenumber, string bodytext, int n)
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
	}'''

repl('iOSPlugins.cs', ios_old, ios_new)
repl('iOSPlugins.cs', ios_old_2, ios_new_2)

# Unused fields
def repl_regex(path, pattern):
    p = os.path.join(base_dir, path)
    with open(p, 'r', encoding='utf-8') as f:
        content = f.read()
    new_content = re.sub(pattern, '', content, count=1)
    if new_content != content:
        with open(p, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f'Removed unused field from {path}')
    else:
        print(f'Failed to remove unused field from {path}')

repl_regex('TField.cs', r'\s*public static int CARET_WIDTH = 2;')
repl_regex('TField.cs', r'\s*public static int CARET_SHOWING_TIME = 5;')
repl_regex('Sudo_Mem.cs', r'\s*private int wPaintQua = 93;')
repl_regex('Sudo_Mem.cs', r'\s*private int hItem = 48;')
repl_regex('Sudo_Mem.cs', r'\s*private int minChat;')
repl_regex('MotherCanvas.cs', r'\s*public static int OUTPUTSIZE = 20;')
repl_regex('GameScreen.cs', r'\s*private bool getPointer;')
repl_regex('ItemQuaNT.cs', r'\s*private int sizeItem = 24;')
repl_regex('UpdateImageScreen.cs', r'\s*private int hpaint;')
repl_regex('MainBuff.cs', r'\s*private int fRemove;')
repl_regex('TabSuDo.cs', r'\s*private int wPaintQua = 93;')
repl_regex('GamePad.cs', r'\s*private int R = 30;')
repl_regex('MonsterWalk.cs', r'\s*private int dirLastCur;')

