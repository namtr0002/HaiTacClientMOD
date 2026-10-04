using System;
using UnityEngine;

public class ModernUI
{
	// ==========================================
	// CONFIGURATION & MASTER SWITCH
	// ==========================================
	public static bool isModernUI = true;
	public static int themeMode = 0; // 0: Deep Navy & Gold
	public static long animTick = 0;

	// ==========================================
	// COLOR PALETTE CONSTANTS
	// ==========================================
	public const int COLOR_BG_DARK = 0x0C131F;        // Nền tối xanh than sâu
	public const int COLOR_BG_WINDOW = 0x111B2C;      // Nền cửa sổ chính
	public const int COLOR_BG_CARD = 0x18253B;        // Nền thẻ card / slot
	public const int COLOR_BG_CARD_ALT = 0x1F304B;    // Nền thẻ card sáng
	public const int COLOR_BG_CARD_HOVER = 0x273C5E;  // Nền khi rê chuột / focus
	public const int COLOR_BG_OVERLAY = 0x000000;     // Nền mờ phía sau dialog

	// Borders & Accents (Gold / Nautical)
	public const int COLOR_GOLD_BRIGHT = 0xFFE066;    // Vàng kim sáng
	public const int COLOR_GOLD_BASE = 0xD4AF37;      // Vàng hoàng gia chuẩn
	public const int COLOR_GOLD_DARK = 0x8C681E;      // Vàng bóng tối
	public const int COLOR_GOLD_BORDER = 0xB89035;    // Viền kim loại mạ vàng
	public const int COLOR_BORDER_SLATE = 0x2D415E;   // Viền xanh xám mảnh
	public const int COLOR_BORDER_LIGHT = 0x48648B;   // Viền sáng nổi

	// Progress Bars
	public const int COLOR_HP_TOP = 0xFF3B56;         // Đỏ Ruby sáng
	public const int COLOR_HP_BOT = 0xB3001E;         // Đỏ sẫm
	public const int COLOR_HP_TRAIL = 0xFFB3BA;       // Đỏ nhạt đuổi theo khi mất máu
	public const int COLOR_MP_TOP = 0x00D9FF;         // Xanh Cyan sáng
	public const int COLOR_MP_BOT = 0x0066CC;         // Xanh Lam sâu
	public const int COLOR_EXP_TOP = 0x2ECC71;        // Xanh Ngọc Lục Bảo
	public const int COLOR_EXP_BOT = 0x1B7A43;        // Xanh lá đậm

	// Rarity Colors (Phẩm chất vật phẩm)
	public static readonly int[] COLOR_RARITY = new int[]
	{
		0x95A5A6, // 0: Trắng / Xám (Common)
		0x2ECC71, // 1: Xanh Lá (Uncommon)
		0x3498DB, // 2: Xanh Dương (Rare)
		0x9B59B6, // 3: Tím (Epic)
		0xF39C12, // 4: Vàng Cam (Legendary)
		0xE74C3C  // 5: Đỏ Huyết / Trái Ác Quỷ (Mythic)
	};

	public static readonly int[] COLOR_RARITY_BG = new int[]
	{
		0x1A222D, // Common BG
		0x142B20, // Uncommon BG
		0x142738, // Rare BG
		0x261B33, // Epic BG
		0x332612, // Legendary BG
		0x331414  // Mythic BG
	};

	// Button Types
	public const int BTN_GOLD = 0;
	public const int BTN_BLUE = 1;
	public const int BTN_GREEN = 2;
	public const int BTN_RED = 3;
	public const int BTN_GRAY = 4;

	// Damage Tracking
	private static int lastPlayerHp = -1;
	private static int trailPlayerHp = -1;

	// ==========================================
	// UPDATE LOGIC
	// ==========================================
	public static void update()
	{
		animTick++;
		if (GameScreen.player != null)
		{
			if (lastPlayerHp == -1 || trailPlayerHp == -1)
			{
				lastPlayerHp = GameScreen.player.Hp;
				trailPlayerHp = GameScreen.player.Hp;
			}
			if (GameScreen.player.Hp < trailPlayerHp)
			{
				trailPlayerHp -= System.Math.Max(1, (trailPlayerHp - GameScreen.player.Hp) / 8);
			}
			else
			{
				trailPlayerHp = GameScreen.player.Hp;
			}
			lastPlayerHp = GameScreen.player.Hp;
		}
	}

	// ==========================================
	// CORE RENDERING PRIMITIVES
	// ==========================================

	public static void fillGradientRect(mGraphics g, int x, int y, int w, int h, int topColor, int botColor, float alpha = 1.0f)
	{
		if (w <= 0 || h <= 0) return;
		int steps = System.Math.Min(h, 8);
		if (steps <= 1)
		{
			g.setColor(topColor, alpha);
			g.fillRect(x, y, w, h);
			return;
		}

		int r1 = (topColor >> 16) & 0xFF;
		int g1 = (topColor >> 8) & 0xFF;
		int b1 = topColor & 0xFF;

		int r2 = (botColor >> 16) & 0xFF;
		int g2 = (botColor >> 8) & 0xFF;
		int b2 = botColor & 0xFF;

		int stepH = h / steps;
		int remH = h % steps;
		int curY = y;

		for (int i = 0; i < steps; i++)
		{
			float ratio = (float)i / (float)(steps - 1);
			int r = (int)(r1 + (r2 - r1) * ratio);
			int gg = (int)(g1 + (g2 - g1) * ratio);
			int b = (int)(b1 + (b2 - b1) * ratio);
			int c = (r << 16) | (gg << 8) | b;

			int curH = stepH + (i == steps - 1 ? remH : 0);
			g.setColor(c, alpha);
			g.fillRect(x, curY, w, curH);
			curY += curH;
		}
	}

	public static void fillRoundRect(mGraphics g, int x, int y, int w, int h, int radius, int color, float alpha = 1.0f)
	{
		if (w <= 0 || h <= 0) return;
		radius = System.Math.Max(1, System.Math.Min(radius, System.Math.Min(w / 2, h / 2)));

		g.setColor(color, alpha);
		g.fillRect(x + radius, y, w - radius * 2, h);
		g.fillRect(x, y + radius, radius, h - radius * 2);
		g.fillRect(x + w - radius, y + radius, radius, h - radius * 2);

		if (radius >= 2)
		{
			g.fillRect(x + 1, y + 1, radius - 1, radius - 1);
			g.fillRect(x + w - radius, y + 1, radius - 1, radius - 1);
			g.fillRect(x + 1, y + h - radius, radius - 1, radius - 1);
			g.fillRect(x + w - radius, y + h - radius, radius - 1, radius - 1);
		}
	}

	public static void drawRoundRect(mGraphics g, int x, int y, int w, int h, int radius, int borderColor, float alpha = 1.0f)
	{
		if (w <= 0 || h <= 0) return;
		radius = System.Math.Max(1, System.Math.Min(radius, System.Math.Min(w / 2, h / 2)));

		g.setColor(borderColor, alpha);
		g.fillRect(x + radius, y, w - radius * 2, 1);
		g.fillRect(x + radius, y + h - 1, w - radius * 2, 1);
		g.fillRect(x, y + radius, 1, h - radius * 2);
		g.fillRect(x + w - 1, y + radius, 1, h - radius * 2);

		if (radius >= 2)
		{
			g.fillRect(x + 1, y + 1, 1, 1);
			g.fillRect(x + w - 2, y + 1, 1, 1);
			g.fillRect(x + 1, y + h - 2, 1, 1);
			g.fillRect(x + w - 2, y + h - 2, 1, 1);
		}
	}

	public static void drawCard(mGraphics g, int x, int y, int w, int h, bool isFocused, int customBorder = -1)
	{
		if (w <= 0 || h <= 0) return;
		g.setColor(0x000000, 0.35f);
		g.fillRect(x + 2, y + 2, w, h);

		int topCol = isFocused ? COLOR_BG_CARD_HOVER : COLOR_BG_CARD;
		int botCol = isFocused ? COLOR_BG_CARD : COLOR_BG_DARK;
		fillGradientRect(g, x, y, w, h, topCol, botCol, 0.95f);

		int borderCol = customBorder != -1 ? customBorder : (isFocused ? COLOR_GOLD_BRIGHT : COLOR_BORDER_SLATE);
		drawRoundRect(g, x, y, w, h, 3, borderCol, 1.0f);

		g.setColor(0xFFFFFF, isFocused ? 0.25f : 0.10f);
		g.fillRect(x + 2, y + 1, w - 4, 1);
	}

	public static void drawProgressBar(mGraphics g, int x, int y, int w, int h, long cur, long max, int colorTop, int colorBot, string customText = null, int trailVal = -1)
	{
		if (w <= 0 || h <= 0) return;
		if (max <= 0) max = 1;
		if (cur < 0) cur = 0;
		if (cur > max) cur = max;

		g.setColor(0x080E17, 0.9f);
		g.fillRect(x, y, w, h);

		g.setColor(0x1B283C, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		int maxFillW = w - 2;
		if (trailVal > cur && trailVal > 0)
		{
			int trailW = (int)((long)trailVal * maxFillW / max);
			if (trailW > maxFillW) trailW = maxFillW;
			g.setColor(COLOR_HP_TRAIL, 0.8f);
			g.fillRect(x + 1, y + 1, trailW, h - 2);
		}

		int fillW = (int)(cur * maxFillW / max);
		if (fillW > 0)
		{
			fillGradientRect(g, x + 1, y + 1, fillW, h - 2, colorTop, colorBot, 1.0f);
			g.setColor(0xFFFFFF, 0.35f);
			g.fillRect(x + 1, y + 1, fillW, System.Math.Max(1, (h - 2) / 3));
		}

		string displayText = customText;
		if (string.IsNullOrEmpty(displayText))
		{
			displayText = cur.ToString("N0") + " / " + max.ToString("N0");
		}

		int textY = y + (h - GameCanvas.hText) / 2;
		mFont.tahoma_7b_black.drawString(g, displayText, x + w / 2 + 1, textY + 1, 2);
		mFont.tahoma_7b_white.drawString(g, displayText, x + w / 2, textY, 2);
	}

	public static void drawSlot(mGraphics g, int x, int y, int size, int rarity, bool isSelected)
	{
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		if (currentTheme == null || currentTheme.id == 0)
		{
			AvMain.paintRect(g, x, y, size, size, (sbyte)(isSelected ? 1 : 0), 3);
			return;
		}
		if (rarity < 0) rarity = 0;
		if (rarity >= COLOR_RARITY.Length) rarity = COLOR_RARITY.Length - 1;

		int bgCol = COLOR_RARITY_BG[rarity];
		fillGradientRect(g, x, y, size, size, bgCol + 0x0A0A0A, bgCol);

		int borderCol = COLOR_RARITY[rarity];
		g.setColor(borderCol);
		g.drawRect(x, y, size - 1, size - 1);

		if (isSelected)
		{
			int glowCol = (animTick % 20 < 10) ? COLOR_GOLD_BRIGHT : 0xFFFFFF;
			g.setColor(glowCol);
			g.drawRect(x - 1, y - 1, size + 1, size + 1);
			g.drawRect(x, y, size - 1, size - 1);
		}
	}

	public static void drawButton(mGraphics g, int x, int y, int w, int h, string text, int btnType, bool isPressed, bool isFocused)
	{
		if (w <= 0 || h <= 0) return;
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		if (currentTheme == null || currentTheme.id == 0)
		{
			AvMain.paintRect(g, x, y, w, h, (sbyte)(isPressed ? 1 : 0), 1);
			if (!string.IsNullOrEmpty(text))
			{
				mFont f = isPressed ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white;
				f.drawString(g, text, x + w / 2, y + (h - GameCanvas.hText) / 2, 2);
			}
			return;
		}
		int topCol = COLOR_GOLD_BRIGHT;
		int botCol = COLOR_GOLD_BASE;
		int borderCol = COLOR_GOLD_DARK;
		int textCol = 0x000000;

		switch (btnType)
		{
			case BTN_BLUE:
				topCol = 0x2980B9;
				botCol = 0x1A5276;
				borderCol = 0x5499C7;
				textCol = 0xFFFFFF;
				break;
			case BTN_GREEN:
				topCol = 0x27AE60;
				botCol = 0x196F3D;
				borderCol = 0x52BE80;
				textCol = 0xFFFFFF;
				break;
			case BTN_RED:
				topCol = 0xE74C3C;
				botCol = 0x922B21;
				borderCol = 0xEC7063;
				textCol = 0xFFFFFF;
				break;
			case BTN_GRAY:
				topCol = 0x566573;
				botCol = 0x2C3E50;
				borderCol = 0x7F8C8D;
				textCol = 0xFFFFFF;
				break;
			default:
				topCol = isFocused ? 0xFFF099 : 0xF5C744;
				botCol = isFocused ? 0xD4AF37 : 0xB8860B;
				borderCol = 0x7D5700;
				textCol = 0x331C00;
				break;
		}

		if (isPressed)
		{
			int temp = topCol;
			topCol = botCol;
			botCol = temp;
			y += 1;
		}

		g.setColor(0x000000, 0.4f);
		g.fillRect(x + 1, y + 2, w, h);

		fillGradientRect(g, x, y, w, h, topCol, botCol, 1.0f);

		g.setColor(borderCol, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		g.setColor(0xFFFFFF, 0.4f);
		g.fillRect(x + 1, y + 1, w - 2, 1);

		if (!string.IsNullOrEmpty(text))
		{
			int textY = y + (h - GameCanvas.hText) / 2;
			if (textCol == 0xFFFFFF)
			{
				mFont.tahoma_7b_black.drawString(g, text, x + w / 2 + 1, textY + 1, 2);
				mFont.tahoma_7b_white.drawString(g, text, x + w / 2, textY, 2);
			}
			else
			{
				mFont.tahoma_7b_black.drawString(g, text, x + w / 2, textY, 2);
			}
		}
	}


	// ==========================================
	// WINDOW & CONTAINER COMPONENTS
	// ==========================================

	public static void paintModernWindow(mGraphics g, int x, int y, int w, int h, string title, sbyte typePaper)
	{
		g.setColor(0x000000, 0.55f);
		g.fillRect(x - 3, y - 3, w + 6, h + 6);

		fillGradientRect(g, x, y, w, h, COLOR_BG_WINDOW, COLOR_BG_DARK, 0.98f);

		g.setColor(COLOR_GOLD_DARK, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		g.setColor(COLOR_GOLD_BASE, 1.0f);
		g.drawRect(x + 1, y + 1, w - 3, h - 3);

		g.setColor(COLOR_BORDER_SLATE, 1.0f);
		g.drawRect(x + 3, y + 3, w - 7, h - 7);

		drawCornerAccents(g, x, y, w, h);

		if (!string.IsNullOrEmpty(title))
		{
			int headerW = System.Math.Min(w - 60, System.Math.Max(120, title.Length * 12 + 40));
			int headerH = 22;
			int headerX = x + (w - headerW) / 2;
			int headerY = y - 4;

			fillGradientRect(g, headerX, headerY, headerW, headerH, 0x992222, 0x550000, 1.0f);
			g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
			g.drawRect(headerX, headerY, headerW - 1, headerH - 1);
			g.setColor(COLOR_GOLD_DARK, 1.0f);
			g.drawRect(headerX + 1, headerY + 1, headerW - 3, headerH - 3);

			int titleY = headerY + (headerH - GameCanvas.hText) / 2;
			mFont.tahoma_7b_black.drawString(g, title, headerX + headerW / 2 + 1, titleY + 1, 2);
			mFont.tahoma_7b_yellow.drawString(g, title, headerX + headerW / 2, titleY, 2);
		}
	}

	private static void drawCornerAccents(mGraphics g, int x, int y, int w, int h)
	{
		g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
		g.fillRect(x + 2, y + 2, 5, 2);
		g.fillRect(x + 2, y + 2, 2, 5);
		g.fillRect(x + w - 7, y + 2, 5, 2);
		g.fillRect(x + w - 4, y + 2, 2, 5);
		g.fillRect(x + 2, y + h - 4, 5, 2);
		g.fillRect(x + 2, y + h - 7, 2, 5);
		g.fillRect(x + w - 7, y + h - 4, 5, 2);
		g.fillRect(x + w - 4, y + h - 7, 2, 5);
	}

	public static void paintModernTabList(mGraphics g, int xpaint, mVector vecTabs, int idSelect, int cmxTab, int cmxLimTab)
	{
		if (vecTabs == null || vecTabs.size() == 0) return;

		int startX = xpaint + 6;
		int startY = MainTab.yTab + 30;
		int tabH = MainTab.hItemTab;

		g.setClip(xpaint - 10, MainTab.yTab + 14, 50, MainTab.hTab - 24);
		mGraphics.resetTransAndroid(g);
		g.translate(0, -cmxTab);

		for (int i = 0; i < vecTabs.size(); i++)
		{
			MainTab tab = (MainTab)vecTabs.elementAt(i);
			if (tab == null) continue;

			int curY = startY + i * tabH;
			bool isSelected = (i == idSelect);

			int btnSize = 28;
			int btnX = isSelected ? startX + 4 : startX;

			int bgTop = isSelected ? 0x992222 : 0x1A283C;
			int bgBot = isSelected ? 0x550000 : 0x0E1726;
			fillGradientRect(g, btnX, curY - btnSize / 2, btnSize, btnSize, bgTop, bgBot, 1.0f);

			int border = isSelected ? COLOR_GOLD_BRIGHT : COLOR_BORDER_SLATE;
			g.setColor(border, 1.0f);
			g.drawRect(btnX, curY - btnSize / 2, btnSize - 1, btnSize - 1);

			if (isSelected)
			{
				g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
				g.fillRect(btnX + btnSize, curY - 2, 3, 4);
			}

			if (tab.indexIconTab == 5 || tab is TabPet)
			{
				if (QuickMenu.fraQuickMenu != null && QuickMenu.fraQuickMenu.Length > 15 && QuickMenu.fraQuickMenu[15] != null)
				{
					QuickMenu.fraQuickMenu[15].drawFrame(0, btnX + btnSize / 2, curY, 0, 3, g);
				}
			}
			else
			{
				short id = (short)(200 + tab.indexIconTab);
				if (GameCanvas.isSmallScreen)
				{
					id = (short)(260 + tab.indexIconTab);
				}
				MainImage imageAll = ObjectData.getImageAll(id, ObjectData.hashImageItemOther, 9000);
				if (imageAll != null && imageAll.img != null)
				{
					g.drawImage(imageAll.img, btnX + btnSize / 2, curY, 3);
				}
			}

			if (tab is TabInfo && Player.pointAttribute > 0)
			{
				g.setColor(0xE74C3C, 1.0f);
				g.fillRect(btnX + btnSize - 6, curY - btnSize / 2, 6, 6);
				g.setColor(0xFFFFFF, 1.0f);
				g.fillRect(btnX + btnSize - 5, curY - btnSize / 2 + 1, 4, 4);
			}
		}

		mGraphics.resetTransAndroid(g);
		GameCanvas.resetTrans(g);
	}

	// ==========================================
	// IN-GAME HUD COMPONENTS
	// ==========================================

	public static void paintModernPlayerHUD(mGraphics g, int x, int y, bool isborder, mFont fontLv)
	{
		if (GameScreen.player == null) return;
		if (GameCanvas.isCompactMode())
		{
			Interface_Game.paintInfoPlayer(g, x, y, isborder, fontLv);
			return;
		}
		if (GameCanvas.isTaiTho) x += 5;

		int avatarSize = 34;
		int barX = x + avatarSize + 6;
		int barW = 120;
		int barH = 11;

		g.setColor(0x000000, 0.45f);
		g.fillRect(x - 1, y - 1, avatarSize + 2, avatarSize + 2);

		fillGradientRect(g, x, y, avatarSize, avatarSize, COLOR_BG_CARD, COLOR_BG_DARK, 1.0f);
		g.setColor(COLOR_GOLD_BASE, 1.0f);
		g.drawRect(x, y, avatarSize - 1, avatarSize - 1);
		g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
		g.drawRect(x + 1, y + 1, avatarSize - 3, avatarSize - 3);

		mImage imgHead = (GameScreen.player.Lv < 100) ? Interface_Game.imgIconMPHP : Interface_Game.imgIconMPHP2;
		if (imgHead != null)
		{
			g.drawImage(imgHead, x + avatarSize / 2, y + avatarSize / 2, 3);
		}

		int lvBoxW = 24;
		int lvBoxH = 10;
		int lvBoxX = x + (avatarSize - lvBoxW) / 2;
		int lvBoxY = y + avatarSize - 4;

		fillGradientRect(g, lvBoxX, lvBoxY, lvBoxW, lvBoxH, 0x8C681E, 0x4D3605, 1.0f);
		g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
		g.drawRect(lvBoxX, lvBoxY, lvBoxW - 1, lvBoxH - 1);

		string lvStr = (GameScreen.player.Lv >= 100) 
			? ("TT." + GameScreen.player.LvThongThao) 
			: ("Lv." + GameScreen.player.Lv);
		mFont.tahoma_7_white.drawString(g, lvStr, lvBoxX + lvBoxW / 2, lvBoxY, 2);

		int curHp = GameScreen.player.Hp;
		int maxHp = GameScreen.player.maxHp;
		drawProgressBar(g, barX, y, barW, barH, curHp, maxHp, COLOR_HP_TOP, COLOR_HP_BOT, null, trailPlayerHp);

		int curMp = GameScreen.player.Mp;
		int maxMp = GameScreen.player.maxMp;
		drawProgressBar(g, barX, y + barH + 2, barW, barH, curMp, maxMp, COLOR_MP_TOP, COLOR_MP_BOT, null);

		int expPercent = (GameScreen.player.Lv >= 100) ? GameScreen.player.percentThongThao : GameScreen.player.percentLv;
		string expStr = ((GameScreen.player.Lv >= 100) ? "TT: " : "EXP: ") + (expPercent / 10) + "." + (expPercent % 10) + "%";
		drawProgressBar(g, barX, y + (barH + 2) * 2, barW, 9, expPercent, 1000, COLOR_EXP_TOP, COLOR_EXP_BOT, expStr);
	}

	public static void paintModernCurrencyHUD(mGraphics g, int x, int y, bool isClan)
	{
		int pillW = 95;
		int pillH = 14;
		int curX = x;
		int curY = y;

		drawCurrencyPill(g, curX, curY, pillW, pillH, 0, GameScreen.player.beli);
		curY += pillH + 2;

		drawCurrencyPill(g, curX, curY, pillW, pillH, 1, GameScreen.player.gem);
		curY += pillH + 2;

		drawCurrencyPill(g, curX, curY, pillW, pillH, 7, GameScreen.player.vnd);
	}

	private static void drawCurrencyPill(mGraphics g, int x, int y, int w, int h, int iconFrame, long amount)
	{
		fillGradientRect(g, x, y, w, h, 0x1B2738, 0x0C131F, 0.9f);
		g.setColor(COLOR_BORDER_SLATE, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		if (AvMain.fraMoney != null && iconFrame < AvMain.fraMoney.nFrame)
		{
			AvMain.fraMoney.drawFrame(iconFrame, x + 7, y + h / 2, 0, 3, g);
		}

		string moneyStr = formatMoney(amount);
		mFont.tahoma_7b_yellow.drawString(g, moneyStr, x + w - 4, y + (h - GameCanvas.hText) / 2, 1);
	}

	public static string formatMoney(long amount)
	{
		if (amount >= 1000000000)
		{
			return (amount / 1000000000.0).ToString("0.##") + "B";
		}
		if (amount >= 1000000)
		{
			return (amount / 1000000.0).ToString("0.##") + "M";
		}
		if (amount >= 10000)
		{
			return (amount / 1000.0).ToString("0.#") + "K";
		}
		return amount.ToString("N0");
	}

	public static void drawCombatPowerBadge(mGraphics g, int x, int y, int power)
	{
		int w = 110;
		int h = 18;
		int startX = x - w / 2;

		fillGradientRect(g, startX, y, w, h, 0x4A1010, 0x240505, 1.0f);
		g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
		g.drawRect(startX, y, w - 1, h - 1);
		g.setColor(COLOR_GOLD_DARK, 1.0f);
		g.drawRect(startX + 1, y + 1, w - 3, h - 3);

		string cpStr = "★ LỰC CHIẾN: " + power.ToString("N0");
		mFont.tahoma_7b_black.drawString(g, cpStr, x + 1, y + 3, 2);
		mFont.tahoma_7b_yellow.drawString(g, cpStr, x, y + 2, 2);
	}

	// ==========================================
	// QUICK MENU DOCK (Left Sidebar)
	// ==========================================
	public static void paintModernQuickMenu(mGraphics g, QuickMenu menu)
	{
		GameCanvas.resetTrans(g);
		int num = menu.menuX + menu.xEff;
		int barW = menu.wUni;

		fillGradientRect(g, num, 0, barW, MotherCanvas.h, 0x1B283C, 0x0A111C, 0.92f);
		g.setColor(COLOR_GOLD_BASE, 0.8f);
		g.fillRect(num + barW - 1, 0, 1, MotherCanvas.h);

		int num2 = (menu.xEff != 0) ? 1 : 0;
		if (QuickMenu.imgTamGiac != null)
		{
			g.drawRegion(QuickMenu.imgTamGiac, 0, num2 * 24, 13, 24, 0, num + barW - 6, MotherCanvas.h / 2 - 12, 0);
		}

		g.translate(0, -menu.list.cmx);
		for (int k = 0; k < menu.menuItems.size(); k++)
		{
			iCommand cmd = (iCommand)menu.menuItems.elementAt(k);
			if (cmd == null) continue;

			int itemY = menu.menuY + menu.menuW / 2 + k * menu.menuW;
			bool isSel = (menu.menuSelectedItem == k);

			int btnSize = 28;
			int btnX = num + (barW - btnSize) / 2;
			int btnY = itemY - 14;

			int bgTop = isSel ? 0x992222 : 0x1E2D44;
			int bgBot = isSel ? 0x550000 : 0x121D2C;
			fillGradientRect(g, btnX, btnY, btnSize, btnSize, bgTop, bgBot, 1.0f);

			int border = isSel ? COLOR_GOLD_BRIGHT : COLOR_BORDER_SLATE;
			g.setColor(border, 1.0f);
			g.drawRect(btnX, btnY, btnSize - 1, btnSize - 1);

			cmd.paintOnlyImage(g, num + barW / 2, itemY - 5, (sbyte)(isSel ? 1 : 0));

			if (!string.IsNullOrEmpty(cmd.caption))
			{
				mFont.tahoma_7_white.drawString(g, cmd.caption, num + barW / 2, itemY + 8, 2);
			}
		}
	}

	public static void drawMailCard(mGraphics g, int x, int y, int w, int h, bool isSelected, int mailType)
	{
		if (w <= 0 || h <= 0) return;
		int topCol, botCol, borderCol;
		if (isSelected)
		{
			topCol = COLOR_BG_CARD_HOVER;
			botCol = COLOR_BG_CARD;
			borderCol = COLOR_GOLD_BRIGHT;
		}
		else
		{
			switch (mailType)
			{
				case 1: // Invite
					topCol = 0x1E2B3C;
					botCol = 0x121B27;
					borderCol = 0x3B5B82;
					break;
				case 2: // Gift
					topCol = 0x2A2418;
					botCol = 0x1A160E;
					borderCol = 0x8C733E;
					break;
				default:
					topCol = COLOR_BG_CARD;
					botCol = COLOR_BG_DARK;
					borderCol = COLOR_BORDER_SLATE;
					break;
			}
		}
		fillGradientRect(g, x, y, w, h, topCol, botCol, 1.0f);
		g.setColor(borderCol, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);
	}

	public static void drawBadge(mGraphics g, int x, int y, int count)
	{
		if (count <= 0) return;
		string text = (count > 9) ? "9+" : ("" + count);
		int bw = (count > 9) ? 14 : 10;
		int bh = 10;
		fillGradientRect(g, x - bw / 2, y - bh / 2, bw, bh, 0xFF3B56, 0xB3001E, 1.0f);
		g.setColor(0xFFE066, 1.0f);
		g.drawRect(x - bw / 2, y - bh / 2, bw - 1, bh - 1);
		mFont.tahoma_7_white.drawString(g, text, x, y - 4, 2);
	}

	public static void paintModernDialogFrame(mGraphics g, int x, int y, int w, int h, string title)
	{
		if (w <= 0 || h <= 0) return;
		g.setColor(0x000000, 0.55f);
		g.fillRect(x + 2, y + 2, w, h);

		fillGradientRect(g, x, y, w, h, COLOR_BG_WINDOW, COLOR_BG_DARK, 0.98f);

		g.setColor(COLOR_BORDER_SLATE, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);
		g.setColor(COLOR_GOLD_DARK, 1.0f);
		g.drawRect(x + 1, y + 1, w - 3, h - 3);

		if (!string.IsNullOrEmpty(title))
		{
			int headerH = 22;
			fillGradientRect(g, x + 2, y + 2, w - 4, headerH, 0x1E2B3C, 0x111B2C, 1.0f);
			g.setColor(COLOR_GOLD_BASE, 1.0f);
			g.drawLine(x + 2, y + 2 + headerH, x + w - 3, y + 2 + headerH);

			int titleY = y + 2 + (headerH - GameCanvas.hText) / 2;
			mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, titleY + 1, 2);
			mFont.tahoma_7b_yellow.drawString(g, title, x + w / 2, titleY, 2);
		}
	}

	public static void paintModernTooltip(mGraphics g, int x, int y, int w, int h, string title, int rarity)
	{
		if (w <= 0 || h <= 0) return;
		if (rarity < 0) rarity = 0;
		if (rarity >= COLOR_RARITY.Length) rarity = COLOR_RARITY.Length - 1;

		int bgCol = COLOR_RARITY_BG[rarity];
		fillGradientRect(g, x, y, w, h, bgCol + 0x080808, COLOR_BG_DARK, 0.95f);

		int borderCol = COLOR_RARITY[rarity];
		g.setColor(borderCol, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		if (!string.IsNullOrEmpty(title))
		{
			int headerH = 18;
			fillGradientRect(g, x + 1, y + 1, w - 2, headerH, bgCol + 0x121212, bgCol, 1.0f);
			g.setColor(borderCol, 1.0f);
			g.drawLine(x + 1, y + 1 + headerH, x + w - 2, y + 1 + headerH);

			int textY = y + 1 + (headerH - GameCanvas.hText) / 2;
			mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, textY + 1, 2);
			mFont.tahoma_7b_white.drawString(g, title, x + w / 2, textY, 2);
		}
	}

	public static void paintModernTabHeader(mGraphics g, int x, int y, int w, int h, string text, bool isSelected, bool isHover)
	{
		if (w <= 0 || h <= 0) return;
		int topCol, botCol, borderCol;
		if (isSelected)
		{
			topCol = 0x992222;
			botCol = 0x550000;
			borderCol = COLOR_GOLD_BRIGHT;
		}
		else if (isHover)
		{
			topCol = COLOR_BG_CARD_HOVER;
			botCol = COLOR_BG_CARD;
			borderCol = COLOR_GOLD_BASE;
		}
		else
		{
			topCol = COLOR_BG_CARD;
			botCol = COLOR_BG_DARK;
			borderCol = COLOR_BORDER_SLATE;
		}

		fillGradientRect(g, x, y, w, h, topCol, botCol, 1.0f);
		g.setColor(borderCol, 1.0f);
		g.drawRect(x, y, w - 1, h - 1);

		if (isSelected)
		{
			g.setColor(COLOR_GOLD_BRIGHT, 1.0f);
			g.fillRect(x + 2, y + h - 2, w - 4, 2);
		}

		if (!string.IsNullOrEmpty(text))
		{
			int textY = y + (h - GameCanvas.hText) / 2;
			if (isSelected)
			{
				mFont.tahoma_7b_black.drawString(g, text, x + w / 2 + 1, textY + 1, 2);
				mFont.tahoma_7b_yellow.drawString(g, text, x + w / 2, textY, 2);
			}
			else
			{
				mFont.tahoma_7_white.drawString(g, text, x + w / 2, textY, 2);
			}
		}
	}
}

