using System;
using System.Collections;
using UnityEngine;

public class mFont
{
	public const sbyte LEFT = 0;

	public const sbyte RIGHT = 1;

	public const sbyte CENTER = 2;

	public const sbyte RED = 0;

	public const sbyte YELLOW = 1;

	public const sbyte GREEN = 2;

	public const sbyte FATAL = 3;

	public const sbyte MISS = 4;

	public const sbyte ORANGE = 5;

	public const sbyte ADDMONEY = 6;

	public const sbyte MISS_ME = 7;

	public const sbyte FATAL_ME = 8;

	public const sbyte GREY = 8;

	public const sbyte HP = 9;

	public const sbyte MP = 10;

	private int space;

	private Image imgFont;

	private string strFont;

	private int[][] fImages;

	public static int yAddFont;

	public static int[] colorPhong = new int[9] { 16777215, 6741809, 11830015, 16686378, 7511551, 16580155, 16711680, 1250067, 11957553 };

	public static int[] colorJava = new int[31]
	{
		0, 16711680, 6520319, 16777215, 16755200, 5449989, 21285, 52224, 7386228, 16771788,
		0, 65535, 21285, 16776960, 5592405, 16742263, 33023, 8701737, 15723503, 7999781,
		16768815, 14961237, 4124899, 4671303, 16096312, 16711680, 16755200, 52224, 16777215, 6520319,
		16096312
	};

	public static mFont gI;

	public static mFont tahoma_7b_red;

	public static mFont tahoma_7b_blue;

	public static mFont tahoma_7b_white;

	public static mFont tahoma_7b_yellow;

	public static mFont tahoma_7b_black;

	public static mFont tahoma_7b_violet;

	public static mFont tahoma_7b_orange;

	public static mFont tahoma_7b_green;

	public static mFont tahoma_7b_brown;

	public static mFont tahoma_7_red;

	public static mFont tahoma_7_blue;

	public static mFont tahoma_7_white;

	public static mFont tahoma_7_yellow;

	public static mFont tahoma_7_black;

	public static mFont tahoma_7_violet;

	public static mFont tahoma_7_orange;

	public static mFont tahoma_7_green;

	public static mFont tahoma_7_brown;

	public static mFont number_yellow;

	public static mFont number_red;

	public static mFont number_green;

	public static mFont number_orange;

	public static mFont bigNumber_red;

	public static mFont bigNumber_While;

	public static mFont bigNumber_yellow;

	public static mFont bigNumber_green;

	public static mFont bigNumber_orange;

	public static mFont bigNumber_blue;

	public static mFont fontBigBorder;

	public static mFont font_f12_White;

	public static mFont tahoma_7_white_Border;

	public static mFont tahoma_7b_yellow_Border;

	public static mFont nameFontRed;

	public static mFont nameFontYellow;

	public static mFont nameFontGreen;

	public Font myFont;

	private int height;

	private int wO;

	public Color color1 = Color.white;

	public Color color2 = Color.gray;

	public sbyte id;

	public sbyte idPhong;

	public int fstyle;

	public string st1 = "áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴĐ";

	public string st2 = "\u00b8µ¶·¹\u00a8¾»¼½Æ©ÊÇÈÉËÐÌÎÏÑªÕÒÓÔÖÝ×ØÜÞãßáâä«èåæçé¬íêëìîóïñòô\u00adøõö÷ùýúûüþ®\u00b8µ¶·¹¡¾»¼½Æ¢ÊÇÈÉËÐÌÎÏÑ£ÕÒÓÔÖÝ×ØÜÞãßáâä¤èåæçé¥íêëìîóïñòô¦øõö÷ùýúûüþ§";

	public static char xu = '`';

	public static char luong = '~';

	public static char huyChuong = '¢';

	public static string xuStr = "`";

	public static string luongStr = "~";

	public static string huyChuongStr = "¢";

	public static char[] nguyenLieu = new char[2] { '¤', '¥' };

	public static string[] nguyenLieuStr = new string[2] { "¤", "¥" };

	private int yAdd;

	private int sizef = 10;

	public static void loadmFont()
	{
		gI = new mFont(0, 0);
		tahoma_7b_red = new mFont(1, 6);
		tahoma_7b_blue = new mFont(2, 4);
		tahoma_7b_white = new mFont(3, 0);
		tahoma_7b_yellow = new mFont(4, 5);
		tahoma_7b_black = new mFont(5, 7);
		tahoma_7b_green = new mFont(7, 1);
		tahoma_7b_violet = new mFont(7, 2);
		tahoma_7b_orange = new mFont(7, 3);
		tahoma_7b_brown = new mFont(8, 8);
		tahoma_7_yellow = new mFont(13, 5);
		tahoma_7_red = new mFont(15, 6);
		tahoma_7_blue = new mFont(16, 4);
		tahoma_7_green = new mFont(17, 1);
		tahoma_7_white = new mFont(18, 0);
		tahoma_7_black = new mFont(18, 7);
		tahoma_7_violet = new mFont(18, 2);
		tahoma_7_orange = new mFont(18, 3);
		tahoma_7_brown = new mFont(18, 8);
		number_yellow = new mFont(20, 5);
		number_red = new mFont(21, 6);
		number_green = new mFont(22, 1);
		number_orange = new mFont(24, 3);
		bigNumber_red = new mFont(25, 0);
		bigNumber_yellow = new mFont(26, 0);
		bigNumber_green = new mFont(27, 0);
		bigNumber_While = new mFont(28, 0);
		bigNumber_blue = new mFont(29, 0);
		bigNumber_orange = new mFont(30, 0);
		fontBigBorder = new mFont(31, 0);
		font_f12_White = new mFont(32, 0);
		tahoma_7_white_Border = new mFont(33, 0);
		tahoma_7b_yellow_Border = new mFont(34, 0);
		nameFontRed = tahoma_7b_red;
		nameFontYellow = tahoma_7_yellow;
		nameFontGreen = tahoma_7_green;
		yAddFont = 1;
		if (mGraphics.zoomLevel == 1)
		{
			yAddFont = -3;
		}
	}

	public mFont(sbyte id, sbyte colorP)
	{
		string text = "chelthm";
		if ((id > 0 && id < 10) || id == 19 || id == 31)
		{
			text = "fontbu";
			sizef = 8;
			yAdd = 3;
			if (mGraphics.zoomLevel == 1)
			{
				text = "fontx1";
				sizef = 11;
				yAdd = 2;
			}
		}
		else if (id >= 10 && id <= 18)
		{
			text = "fontnho";
			yAdd = 4;
			sizef = 9;
			if (mGraphics.zoomLevel == 1)
			{
				text = "fontx1";
				sizef = 9;
				yAdd = 2;
			}
		}
		else if (id > 24)
		{
			text = "fontbu";
			sizef = 10;
			yAdd = 5;
			if (mGraphics.zoomLevel == 1)
			{
				text = "fontx1";
				sizef = 11;
				yAdd = 2;
			}
		}
		this.id = id;
		idPhong = colorP;
		text = "FontSys/" + text;
		myFont = (Font)Resources.Load(text);
		if (id < 25)
		{
			color1 = setColorFontPhong(colorP);
			color2 = setColorFontPhong(colorP);
		}
		else
		{
			color1 = bigColor(id);
			color2 = bigColor(id);
		}
		wO = getWidthExactOf("o");
	}

	public Color setColor(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		float b = (float)num / 256f;
		float g = (float)num2 / 256f;
		float r = (float)num3 / 256f;
		return new Color(r, g, b);
	}

	public Color bigColor(int id)
	{
		return (new Color[10]
		{
			Color.red,
			Color.yellow,
			Color.green,
			Color.white,
			setColor(40404),
			Color.red,
			Color.white,
			Color.white,
			Color.red,
			Color.red
		})[id - 25];
	}

	public void setColorByID(int ID)
	{
		color1 = setColor(colorJava[ID]);
		color2 = setColor(colorJava[ID]);
	}

	public void setColorByIDPhong(int ID)
	{
		color1 = setColor(colorPhong[ID]);
		color2 = setColor(colorPhong[ID]);
	}

	public void setTypePaint(mGraphics g, string st, int x, int y, int align, sbyte idFont)
	{
		sbyte colorByIDPhong = idPhong;
		if (idFont > 0)
		{
			colorByIDPhong = idFont;
		}
		x--;
		if (id > 24)
		{
			Color[] array = new Color[10]
			{
				setColor(6029312),
				setColor(7169025),
				setColor(7680),
				setColor(0),
				setColor(9264),
				setColor(6029312),
				setColor(5127485),
				setColor(5127485),
				setColor(6029312),
				setColor(6029312)
			};
			color1 = array[id - 25];
			color2 = array[id - 25];
			_drawString(g, st, x + 1, y, align);
			_drawString(g, st, x - 1, y, align);
			_drawString(g, st, x, y - 1, align);
			_drawString(g, st, x, y + 1, align);
			_drawString(g, st, x + 1, y + 1, align);
			_drawString(g, st, x + 1, y - 1, align);
			_drawString(g, st, x - 1, y - 1, align);
			_drawString(g, st, x - 1, y + 1, align);
			color1 = bigColor(id);
			color2 = bigColor(id);
		}
		else
		{
			setColorByIDPhong(colorByIDPhong);
		}
		_drawString(g, st, x, y - yAdd, align);
	}

	public Color setColorFont(sbyte id)
	{
		return setColor(colorJava[id]);
	}

	public Color setColorFontPhong(sbyte id)
	{
		return setColor(colorPhong[id]);
	}

	public void drawString(mGraphics g, string st, int x, int y, int align)
	{
		setTypePaint(g, st, x, y, align, 0);
	}

	public void drawString(mGraphics g, string st, int x, int y, int align, mFont font)
	{
		setTypePaint(g, st, x, y + 1, align, font.id);
		setTypePaint(g, st, x, y, align, 0);
	}

	public mVector splitFontVector(string src, int lineWidth)
	{
		mVector mVector2 = new mVector();
		string text = "";
		for (int i = 0; i < src.Length; i++)
		{
			if (src[i] == '\n' || src[i] == '\b')
			{
				mVector2.addElement(text);
				text = "";
				continue;
			}
			text += src[i];
			if (getWidth(text) > lineWidth)
			{
				int num = 0;
				num = text.Length - 1;
				while (num >= 0 && text[num] != ' ')
				{
					num--;
				}
				if (num < 0)
				{
					num = text.Length - 1;
				}
				mVector2.addElement(text.Substring(0, num));
				i = i - (text.Length - num) + 1;
				text = "";
			}
			if (i == src.Length - 1 && !text.Trim().Equals(""))
			{
				mVector2.addElement(text);
			}
		}
		return mVector2;
	}

	public static mVector splitFontVector(string src, char c)
	{
		mVector mVector2 = new mVector();
		string text = "";
		char[] array = src.ToCharArray();
		for (int i = 0; i < src.Length; i++)
		{
			if (array[i] == c)
			{
				mVector2.addElement(text);
				text = "";
				continue;
			}
			text += array[i];
			if (i == src.Length - 1 && !text.Trim().Equals(""))
			{
				mVector2.addElement(text);
			}
		}
		return mVector2;
	}

	public string splitFirst(string str)
	{
		string text = "";
		bool flag = false;
		for (int i = 0; i < str.Length; i++)
		{
			if (!flag)
			{
				string text2 = str.Substring(i);
				text = ((!compare(text2, " ")) ? (text + text2) : (text + str[i] + "-"));
				flag = true;
			}
			else if (str[i] == ' ')
			{
				flag = false;
			}
		}
		return text;
	}

	public string[] splitStrInLine(string src, int lineWidth)
	{
		ArrayList arrayList = splitStrInLineA(src, lineWidth);
		string[] array = new string[arrayList.Count];
		for (int i = 0; i < arrayList.Count; i++)
		{
			array[i] = (string)arrayList[i];
		}
		return array;
	}

	public ArrayList splitStrInLineA(string src, int lineWidth)
	{
		ArrayList arrayList = new ArrayList();
		int i = 0;
		int num = 0;
		int length = src.Length;
		if (length < 5)
		{
			arrayList.Add(src);
			return arrayList;
		}
		string text = "";
		try
		{
			while (true)
			{
				if (getWidthNotExactOf(text) < lineWidth)
				{
					text += src[num];
					num++;
					if (src[num] != '\n')
					{
						if (num < length - 1)
						{
							continue;
						}
						num = length - 1;
					}
				}
				if (num != length - 1 && src[num + 1] != ' ')
				{
					int num2 = num;
					while (src[num + 1] != '\n' && (src[num + 1] != ' ' || src[num] == ' ') && num != i)
					{
						num--;
					}
					if (num == i)
					{
						num = num2;
					}
				}
				string text2 = src.Substring(i, num + 1 - i);
				if (text2[0] == '\n')
				{
					text2 = text2.Substring(1, text2.Length - 1);
				}
				if (text2[text2.Length - 1] == '\n')
				{
					text2 = text2.Substring(0, text2.Length - 1);
				}
				arrayList.Add(text2);
				if (num != length - 1)
				{
					for (i = num + 1; i != length - 1 && src[i] == ' '; i++)
					{
					}
					if (i != length - 1)
					{
						num = i;
						text = "";
						continue;
					}
					break;
				}
				break;
			}
		}
		catch (Exception ex)
		{
			Cout.LogWarning("EXCEPTION WHEN REAL SPLIT " + src + "\nend=" + num + "\n" + ex.Message + "\n" + ex.StackTrace);
			arrayList.Add(src);
		}
		return arrayList;
	}

	public string[] splitFontArray(string src, int lineWidth)
	{
		mVector mVector2 = splitFontVector(src, lineWidth);
		string[] array = new string[mVector2.size()];
		for (int i = 0; i < mVector2.size(); i++)
		{
			array[i] = (string)mVector2.elementAt(i);
		}
		return array;
	}

	public bool compare(string strSource, string str)
	{
		for (int i = 0; i < strSource.Length; i++)
		{
			if ((strSource[i].ToString() ?? "").Equals(str))
			{
				return true;
			}
		}
		return false;
	}

	private GUIStyle cachedCalcStyle;

	public int getWidth(string s)
	{
		return getWidthExactOf(s);
	}

	public int getWidthExactOf(string s)
	{
		if (string.IsNullOrEmpty(s)) return 0;
		if (!Main.isMainThread)
		{
			return getWidthNotExactOf(s);
		}
		try
		{
			if (cachedCalcStyle == null)
			{
				cachedCalcStyle = new GUIStyle();
			}
			cachedCalcStyle.font = myFont;
			cachedCalcStyle.fontSize = sizef * mGraphics.zoomLevel;
			return (int)cachedCalcStyle.CalcSize(new GUIContent(s)).x / mGraphics.zoomLevel;
		}
		catch (Exception ex)
		{
			Cout.LogError("GET WIDTH OF " + s + " FAIL.\n" + ex.Message + "\n" + ex.StackTrace);
			return getWidthNotExactOf(s);
		}
	}

	public int getWidthNotExactOf(string s)
	{
		if (string.IsNullOrEmpty(s)) return 0;
		int charW = (sizef > 0) ? (sizef * 5 / 8) : 7;
		if (charW < 4) charW = 4;
		return s.Length * charW;
	}

	public int getHeight()
	{
		if (height > 0)
		{
			return height / mGraphics.zoomLevel;
		}
		if (!Main.isMainThread)
		{
			return (sizef > 0) ? sizef : 20;
		}
		try
		{
			if (cachedCalcStyle == null)
			{
				cachedCalcStyle = new GUIStyle();
			}
			cachedCalcStyle.font = myFont;
			cachedCalcStyle.fontSize = sizef * mGraphics.zoomLevel;
			height = (int)cachedCalcStyle.CalcSize(new GUIContent("Adg")).y + 2;
		}
		catch (Exception ex)
		{
			Cout.LogError("FAIL GET HEIGHT " + ex.StackTrace);
			height = 20;
		}
		return height / mGraphics.zoomLevel;
	}

	private GUIStyle[] cachedStyles;

	public GUIStyle getCachedStyle(int align = 0)
	{
		if (cachedStyles == null)
		{
			cachedStyles = new GUIStyle[3];
		}
		int styleIndex = (align == 1) ? 1 : ((align == 2 || align == 3) ? 2 : 0);
		GUIStyle gUIStyle = cachedStyles[styleIndex];
		if (gUIStyle == null)
		{
			gUIStyle = new GUIStyle(GUI.skin.label);
			gUIStyle.font = myFont;
			gUIStyle.alignment = (styleIndex == 1) ? TextAnchor.UpperRight : ((styleIndex == 2) ? TextAnchor.UpperCenter : TextAnchor.UpperLeft);
			cachedStyles[styleIndex] = gUIStyle;
		}
		gUIStyle.fontSize = sizef * mGraphics.zoomLevel;
		gUIStyle.normal.textColor = color1;
		return gUIStyle;
	}

	public void _drawString(mGraphics g, string st, int x0, int y0, int align)
	{
		y0 += yAddFont;
		GUIStyle gUIStyle = getCachedStyle(align);
		float num = 0f;
		float num2 = y0;
		switch (align)
		{
		case 0:
			num = x0;
			break;
		case 1:
			num = x0 - MotherCanvas.w;
			break;
		case 2:
		case 3:
			num = x0 - MotherCanvas.w / 2;
			break;
		}
		g.drawString(st, (int)num, (int)num2, gUIStyle);
	}

	public static string[] splitStringSv(string _text, string _searchStr)
	{
		int num = 0;
		int startIndex = 0;
		int length = _searchStr.Length;
		int num2 = _text.IndexOf(_searchStr, startIndex);
		while (num2 != -1)
		{
			startIndex = num2 + length;
			num2 = _text.IndexOf(_searchStr, startIndex);
			num++;
		}
		string[] array = new string[num + 1];
		int num3 = _text.IndexOf(_searchStr);
		int num4 = 0;
		int num5 = 0;
		while (num3 != -1)
		{
			array[num5] = _text.Substring(num4, num3 - num4);
			num4 = num3 + length;
			num3 = _text.IndexOf(_searchStr, num4);
			num5++;
		}
		array[num5] = _text.Substring(num4, _text.Length - num4);
		return array;
	}

	public void reloadImage()
	{
	}

	public void freeImage()
	{
	}

	public static string[] split(string original, string separator)
	{
		mVector mVector2 = new mVector();
		for (int num = original.IndexOf(separator); num >= 0; num = original.IndexOf(separator))
		{
			mVector2.addElement(original.Substring(0, num));
			original = original.Substring(num + separator.Length);
		}
		mVector2.addElement(original);
		string[] array = new string[mVector2.size()];
		if (mVector2.size() > 0)
		{
			for (int i = 0; i < mVector2.size(); i++)
			{
				array[i] = (string)mVector2.elementAt(i);
			}
		}
		return array;
	}

	public void drawStringAuto(mGraphics g, string text, int boxX, int y, int maxW, int align)
	{
		if (string.IsNullOrEmpty(text) || maxW <= 0)
		{
			return;
		}
		int textW = getWidth(text);
		if (textW <= maxW)
		{
			if (align == 2)
			{
				drawString(g, text, boxX + maxW / 2, y, 2);
			}
			else if (align == 1)
			{
				drawString(g, text, boxX + maxW, y, 1);
			}
			else
			{
				drawString(g, text, boxX, y, 0);
			}
		}
		else
		{
			int overflow = textW - maxW;
			int dist = overflow + 8;
			int pause = 22;
			int cycle = (pause + dist) * 2;
			if (cycle <= 0) cycle = 1;
			int tick = System.Math.Abs(GameCanvas.gameTick) % cycle;
			int xOffset = 0;
			if (tick < pause)
			{
				xOffset = 0;
			}
			else if (tick < pause + dist)
			{
				xOffset = tick - pause;
			}
			else if (tick < pause + dist + pause)
			{
				xOffset = dist;
			}
			else
			{
				xOffset = dist - (tick - (pause + dist + pause));
			}
			if (xOffset > overflow)
			{
				xOffset = overflow;
			}
			if (xOffset < 0)
			{
				xOffset = 0;
			}

			g.pushClip();
			try
			{
				g.clipRectIntersect(boxX, y - 1, maxW, getHeight() + 2);
				drawString(g, text, boxX - xOffset, y, 0);
			}
			finally
			{
				g.popClip();
			}
		}
	}

	public void drawStringAutoCenter(mGraphics g, string text, int centerX, int y, int maxW)
	{
		drawStringAuto(g, text, centerX - maxW / 2, y, maxW, 2);
	}

	public void drawStringAuto(mGraphics g, string text, int boxX, int y, int maxW, int align, mFont shadowFont)
	{
		if (shadowFont != null)
		{
			shadowFont.drawStringAuto(g, text, boxX + 1, y + 1, maxW, align);
		}
		drawStringAuto(g, text, boxX, y, maxW, align);
	}

	public void drawStringAutoCenter(mGraphics g, string text, int centerX, int y, int maxW, mFont shadowFont)
	{
		drawStringAuto(g, text, centerX - maxW / 2, y, maxW, 2, shadowFont);
	}
}
