using System;

public class UIShowcaseScreen : MainScreen
{
	private static UIShowcaseScreen instance;
	private int curTab = 0;
	private int curThemeIdx = 0;
	private iCommand cmdBack;
	private iCommand cmdPrev;
	private iCommand cmdNext;
	private iCommand cmdTab;

	public static UIShowcaseScreen getInstance()
	{
		if (instance == null)
		{
			instance = new UIShowcaseScreen();
		}
		return instance;
	}

	public UIShowcaseScreen()
	{
		this.cmdBack = new iCommand("Quay L\u1ea1i", 0, this);
		this.cmdPrev = new iCommand("\u25c4", 1, this);
		this.cmdNext = new iCommand("\u25ba", 2, this);
		this.cmdTab = new iCommand("\u0110\u1ed5i Tab", 3, this);

		base.left = this.cmdBack;
		base.center = this.cmdTab;
		base.right = this.cmdNext;
	}

	public override void Show()
	{
		curThemeIdx = UIThemeManager.getCurrentThemeId();
		base.Show();
	}

	public new void Show(MainScreen prev)
	{
		curThemeIdx = UIThemeManager.getCurrentThemeId();
		base.Show(prev);
	}

	public override void paint(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		UITheme theme = UIThemeManager.getCurrentTheme();
		if (theme == null) return;

		// Background
		g.setColor(theme.colorBgDark);
		g.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);

		// Main Window Frame
		int winW = Math.Min(MotherCanvas.w - 16, 420);
		int winH = Math.Min(MotherCanvas.h - 40, 280);
		int winX = (MotherCanvas.w - winW) / 2;
		int winY = 10;
		theme.paintWindow(g, winX, winY, winW, winH, "UI SHOWCASE: " + theme.displayName);

		// Theme Navigation Header
		int navY = winY + 28;
		string themeLabel = "< " + (curThemeIdx + 1) + "/10: " + theme.displayName + " >";
		mFont.tahoma_7b_yellow.drawString(g, themeLabel, MotherCanvas.hw, navY, 2);

		// Tab Navigation
		int tabY = navY + 16;
		string[] tabs = new string[] { "Buttons", "Slots", "Gauges", "Dialog" };
		int tabW = (winW - 20) / 4;
		for (int i = 0; i < 4; i++)
		{
			theme.paintTabHeader(g, winX + 10 + i * tabW, tabY, tabW, 18, tabs[i], curTab == i, false);
		}

		// Content Area based on curTab
		int contentY = tabY + 24;
		int contentW = winW - 20;
		int contentH = winH - (contentY - winY) - 10;
		int contentX = winX + 10;

		if (curTab == 0) // Buttons
		{
			int btnW = Math.Min(contentW / 2 - 8, 90);
			int btnH = 22;
			theme.paintButton(g, contentX + 4, contentY + 4, btnW, btnH, "Default", ModernUI.BTN_GOLD, false, false);
			theme.paintButton(g, contentX + 12 + btnW, contentY + 4, btnW, btnH, "Focus", ModernUI.BTN_GOLD, false, true);
			theme.paintButton(g, contentX + 4, contentY + 32, btnW, btnH, "Blue Cmd", ModernUI.BTN_BLUE, false, false);
			theme.paintButton(g, contentX + 12 + btnW, contentY + 32, btnW, btnH, "Green Cmd", ModernUI.BTN_GREEN, false, false);
			theme.paintButton(g, contentX + 4, contentY + 60, btnW, btnH, "Red Danger", ModernUI.BTN_RED, false, false);
			theme.paintButton(g, contentX + 12 + btnW, contentY + 60, btnW, btnH, "Gray Mute", ModernUI.BTN_GRAY, false, false);
		}
		else if (curTab == 1) // Slots & Rarities
		{
			int slotSize = 28;
			int startSlotX = contentX + (contentW - (slotSize * 6 + 20)) / 2;
			for (int r = 0; r < 6; r++)
			{
				int sx = startSlotX + r * (slotSize + 4);
				bool isSel = (r == 3);
				theme.paintSlot(g, sx, contentY + 8, slotSize, r, isSel);
				mFont.tahoma_7_white.drawString(g, "T" + r, sx + slotSize / 2, contentY + 14, 2);
			}
			mFont.tahoma_7_white.drawString(g, "6 Tiers: Common, Uncommon, Rare, Epic, Legendary, Mythic", MotherCanvas.hw, contentY + 46, 2);
		}
		else if (curTab == 2) // Combat Gauges & Tooltip
		{
			int barW = Math.Min(contentW - 30, 180);
			int barX = contentX + (contentW - barW) / 2;
			ModernUI.drawProgressBar(g, barX, contentY + 4, barW, 10, 75, 100, theme.colorHpTop, theme.colorHpBot, "HP: 75/100", theme.colorHpTrail);
			ModernUI.drawProgressBar(g, barX, contentY + 20, barW, 10, 50, 100, theme.colorMpTop, theme.colorMpBot, "MP: 50/100", 0);
			ModernUI.drawProgressBar(g, barX, contentY + 36, barW, 10, 90, 100, theme.colorExpTop, theme.colorExpBot, "EXP: 90%", 0);
			// Mini tooltip preview
			int ttW = Math.Min(contentW - 20, 180);
			int ttH = 40;
			theme.paintTooltip(g, contentX + (contentW - ttW) / 2, contentY + 54, ttW, ttH, "Haki V\u0169 Trang (C\u1ea5p 5)", 4);
		}
		else if (curTab == 3) // Dialog Preview
		{
			int diaW = Math.Min(contentW - 20, 200);
			int diaH = 75;
			int diaX = contentX + (contentW - diaW) / 2;
			int diaY = contentY + 4;
			theme.paintDialog(g, diaX, diaY, diaW, diaH, "Th\u00f4ng B\u00e1o X\u00e1c Nh\u1eadn");
			mFont.tahoma_7_white.drawString(g, "B\u1ea1n c\u00f3 ch\u1eafc ch\u1eafn mu\u1ed1n n\u00e2ng c\u1ea5p?", diaX + diaW / 2, diaY + 28, 2);
			theme.paintButton(g, diaX + 10, diaY + diaH - 24, (diaW - 30) / 2, 18, "\u0110\u1ed3ng \u00dd", ModernUI.BTN_GREEN, false, false);
			theme.paintButton(g, diaX + 20 + (diaW - 30) / 2, diaY + diaH - 24, (diaW - 30) / 2, 18, "H\u1ee7y", ModernUI.BTN_GRAY, false, false);
		}

		// Bottom Screen Buttons
		base.paint(g);
	}

	public override void updatePointer()
	{
		int winW = Math.Min(MotherCanvas.w - 16, 420);
		int winX = (MotherCanvas.w - winW) / 2;
		int winY = 10;
		int navY = winY + 28;

		// Click on Theme Navigation < >
		if (GameCanvas.isPointSelect(winX, navY - 4, 30, 24))
		{
			GameCanvas.isPointerSelect = false;
			prevTheme();
			return;
		}
		if (GameCanvas.isPointSelect(winX + winW - 30, navY - 4, 30, 24))
		{
			GameCanvas.isPointerSelect = false;
			nextTheme();
			return;
		}

		// Click on Tabs
		int tabY = navY + 16;
		int tabW = (winW - 20) / 4;
		for (int i = 0; i < 4; i++)
		{
			if (GameCanvas.isPointSelect(winX + 10 + i * tabW, tabY, tabW, 18))
			{
				GameCanvas.isPointerSelect = false;
				curTab = i;
				return;
			}
		}

		base.updatePointer();
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
			case 0:
				if (base.lastScreen != null)
				{
					base.lastScreen.Show();
				}
				else if (GameCanvas.loginScr != null)
				{
					GameCanvas.loginScr.Show();
				}
				break;
			case 1:
				prevTheme();
				break;
			case 2:
				nextTheme();
				break;
			case 3:
				curTab = (curTab + 1) % 4;
				break;
		}
	}

	private void prevTheme()
	{
		curThemeIdx--;
		if (curThemeIdx < 0) curThemeIdx = UIThemeManager.TOTAL_THEMES - 1;
		UIThemeManager.setTheme(curThemeIdx);
	}

	private void nextTheme()
	{
		curThemeIdx = (curThemeIdx + 1) % UIThemeManager.TOTAL_THEMES;
		UIThemeManager.setTheme(curThemeIdx);
	}
}
