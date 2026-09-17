using System;

public class UIThemeManager
{
	public const int THEME_GOC = 0;
	public const int THEME_NPAPER = 1;
	public const int THEME_THONGTHAO = 2;
	public const int THEME_RUONGVIP = 3;
	public const int THEME_MODERN = 4;
	public const int THEME_PIRATE = 5;
	public const int THEME_GRANDLINE = 6;
	public const int THEME_MARINE = 7;
	public const int THEME_ANCIENT = 8;
	public const int THEME_DARKSEA = 9;

	public const int TOTAL_THEMES = 10;
	private static UITheme[] themes;
	private static int currentThemeId = 0;
	private static bool isInitialized = false;

	public static void init()
	{
		if (isInitialized) return;
		themes = new UITheme[TOTAL_THEMES];

		// 0. Gốc (Classic Paper)
		themes[0] = new UITheme(0, "Goc", "Giao diện: Gốc");
		themes[0].colorBgDark = 0x2A1A0A;
		themes[0].colorBgWindow = 0xE5D2B3;
		themes[0].colorBgCard = 0xD4BE99;
		themes[0].colorBorderGold = 0x8C5825;
		themes[0].colorBorderSlate = 0x5A3825;

		// 1. N-Paper
		themes[1] = new UITheme(1, "NPaper", "Giao diện: N-Paper");
		themes[1].colorBgDark = 0x24180E;
		themes[1].colorBgWindow = 0xDECDB1;
		themes[1].colorBgCard = 0xCFBE9F;
		themes[1].colorBorderGold = 0xB8860B;
		themes[1].colorBorderSlate = 0x4A2E1B;

		// 2. Thống Thạo (Style B)
		themes[2] = new UITheme(2, "ThongThao", "Giao diện: Thống Thạo");
		themes[2].colorBgDark = 0x1A0F07;
		themes[2].colorBgWindow = 0x2E1B0E;
		themes[2].colorBgCard = 0x422815;
		themes[2].colorBgCardHover = 0x5C381E;
		themes[2].colorBorderGold = 0xFFD700;
		themes[2].colorBorderSlate = 0x8B6508;
		themes[2].colorAccentBright = 0xFFF066;

		// 3. Rương VIP (Style C)
		themes[3] = new UITheme(3, "RuongVIP", "Giao diện: Rương VIP");
		themes[3].colorBgDark = 0x0E1424;
		themes[3].colorBgWindow = 0x1A2238;
		themes[3].colorBgCard = 0x24304D;
		themes[3].colorBgCardHover = 0x32436B;
		themes[3].colorBorderGold = 0xF1C40F;
		themes[3].colorBorderSlate = 0x3498DB;
		themes[3].colorAccentBright = 0x5DADE2;

		// 4. HTTH Original Modern
		themes[4] = new UITheme(4, "Modern", "HTTH Original Modern");
		themes[4].colorBgDark = 0x0C131F;
		themes[4].colorBgWindow = 0x111B2C;
		themes[4].colorBgCard = 0x18253B;
		themes[4].colorBgCardHover = 0x273C5E;
		themes[4].colorBorderGold = 0xD4AF37;
		themes[4].colorBorderSlate = 0x2D415E;
		themes[4].colorAccentBright = 0xFFE066;

		// 5. Pirate Adventure
		themes[5] = new UITheme(5, "Pirate", "Pirate Adventure");
		themes[5].colorBgDark = 0x170E08;
		themes[5].colorBgWindow = 0x2C1D11;
		themes[5].colorBgCard = 0x3D2918;
		themes[5].colorBgCardHover = 0x543A22;
		themes[5].colorBorderGold = 0xD35400;
		themes[5].colorBorderSlate = 0x8C5825;
		themes[5].colorAccentBright = 0xF4D03F;

		// 6. Grand Line
		themes[6] = new UITheme(6, "GrandLine", "Grand Line");
		themes[6].colorBgDark = 0x030A14;
		themes[6].colorBgWindow = 0x061121;
		themes[6].colorBgCard = 0x0D1F38;
		themes[6].colorBgCardHover = 0x16335C;
		themes[6].colorBorderGold = 0x00E5FF;
		themes[6].colorBorderSlate = 0x1A5276;
		themes[6].colorAccentBright = 0x80D8FF;

		// 7. Marine
		themes[7] = new UITheme(7, "Marine", "Marine (Chính Nghĩa)");
		themes[7].colorBgDark = 0x101824;
		themes[7].colorBgWindow = 0x1A2942;
		themes[7].colorBgCard = 0x273C5E;
		themes[7].colorBgCardHover = 0x36517E;
		themes[7].colorBorderGold = 0xD4AF37;
		themes[7].colorBorderSlate = 0x85929E;
		themes[7].colorAccentBright = 0xFCF3CF;

		// 8. Ancient Poneglyph
		themes[8] = new UITheme(8, "Ancient", "Ancient Poneglyph");
		themes[8].colorBgDark = 0x121518;
		themes[8].colorBgWindow = 0x1E2328;
		themes[8].colorBgCard = 0x2A3138;
		themes[8].colorBgCardHover = 0x3A444E;
		themes[8].colorBorderGold = 0x50FA7B;
		themes[8].colorBorderSlate = 0x44475A;
		themes[8].colorAccentBright = 0x8BE9FD;

		// 9. Dark Sea
		themes[9] = new UITheme(9, "DarkSea", "Dark Sea (Tứ Hoàng)");
		themes[9].colorBgDark = 0x050508;
		themes[9].colorBgWindow = 0x0A0A0E;
		themes[9].colorBgCard = 0x171420;
		themes[9].colorBgCardHover = 0x252033;
		themes[9].colorBorderGold = 0xE74C3C;
		themes[9].colorBorderSlate = 0x8E44AD;
		themes[9].colorAccentBright = 0xFF5252;

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
