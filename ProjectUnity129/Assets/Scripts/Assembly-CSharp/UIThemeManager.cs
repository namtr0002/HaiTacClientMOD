using System;

public class UIThemeManager
{
	public const int THEME_GOC = 0;
	public const int THEME_NEO = 1;

	public const int TOTAL_THEMES = 2;
	private static UITheme[] themes;
	private static int currentThemeId = 0;
	private static bool isInitialized = false;

	public static void init()
	{
		if (isInitialized) return;
		themes = new UITheme[TOTAL_THEMES];

		// 0. Gốc (Classic Original HTTH)
		themes[0] = new UITheme(0, "Goc", "Giao diện: Gốc");
		themes[0].colorBgDark = 0x2A1A0A;
		themes[0].colorBgWindow = 0xE5D2B3;
		themes[0].colorBgCard = 0xD4BE99;
		themes[0].colorBorderGold = 0x8C5825;
		themes[0].colorBorderSlate = 0x5A3825;

		// 1. Neo Pirate Fantasy (Mới 01)
		themes[1] = new UITheme(1, "NeoPirate", "Giao diện: Mới 01");
		themes[1].colorBgDark = 0x0C1018;
		themes[1].colorBgWindow = 0x121824;
		themes[1].colorBgCard = 0x1A2434;
		themes[1].colorBgCardHover = 0x26344A;
		themes[1].colorBorderGold = 0xD4AF37;
		themes[1].colorBorderSlate = 0x2D3E58;
		themes[1].colorAccentBright = 0xFFEB82;

		loadSavedTheme();
		isInitialized = true;
	}

	private static void loadSavedTheme()
	{
		try
		{
			sbyte[] data = CRes.loadRMS("RMS_UI_THEME");
			if (data != null && data.Length > 0)
			{
				int savedId = data[0];
				if (savedId >= 0 && savedId < TOTAL_THEMES)
				{
					currentThemeId = savedId;
				}
			}
		}
		catch (Exception) {}
	}

	public static void setTheme(int themeId)
	{
		init();
		if (themeId < 0 || themeId >= TOTAL_THEMES) return;
		currentThemeId = themeId;
		ModernUI.themeMode = themeId;
		if (currentThemeId == 1)
		{
			UITheme.loadTheme01Assets();
		}
		try
		{
			CRes.saveRMS("RMS_UI_THEME", new sbyte[] { (sbyte)currentThemeId });
		}
		catch (Exception) {}
	}

	public static int getCurrentThemeId()
	{
		init();
		return currentThemeId;
	}

	public static UITheme getCurrentTheme()
	{
		init();
		return themes[currentThemeId];
	}

	public static UITheme getTheme(int id)
	{
		init();
		if (id < 0 || id >= TOTAL_THEMES) id = 0;
		return themes[id];
	}

	public static string[] getThemeNames()
	{
		init();
		string[] names = new string[TOTAL_THEMES];
		for (int i = 0; i < TOTAL_THEMES; i++)
		{
			names[i] = themes[i].displayName;
		}
		return names;
	}
}
