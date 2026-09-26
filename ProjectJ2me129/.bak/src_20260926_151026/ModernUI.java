public class ModernUI {
    public static boolean isModernUI = true;
    public static int themeMode = 0;
    public static long animTick = 0;

    // Color Palette Constants
    public static final int COLOR_BG_DARK = 0x0C131F;
    public static final int COLOR_BG_WINDOW = 0x111B2C;
    public static final int COLOR_BG_CARD = 0x18253B;
    public static final int COLOR_BG_CARD_ALT = 0x1F304B;
    public static final int COLOR_BG_CARD_HOVER = 0x273C5E;

    public static final int COLOR_GOLD_BRIGHT = 0xFFE066;
    public static final int COLOR_GOLD_BASE = 0xD4AF37;
    public static final int COLOR_GOLD_DARK = 0x8C681E;
    public static final int COLOR_BORDER_SLATE = 0x2D415E;

    public static final int COLOR_HP_TOP = 0xFF3B56;
    public static final int COLOR_HP_BOT = 0xB3001E;
    public static final int COLOR_HP_TRAIL = 0xFFB3BA;
    public static final int COLOR_MP_TOP = 0x00D9FF;
    public static final int COLOR_MP_BOT = 0x0066CC;
    public static final int COLOR_EXP_TOP = 0x2ECC71;
    public static final int COLOR_EXP_BOT = 0x1B7A43;

    public static final int BTN_GOLD = 0;
    public static final int BTN_BLUE = 1;
    public static final int BTN_GREEN = 2;
    public static final int BTN_RED = 3;
    public static final int BTN_GRAY = 4;

    public static final int[] COLOR_RARITY = new int[] {
        0x95A5A6, 0x2ECC71, 0x3498DB, 0x9B59B6, 0xF39C12, 0xE74C3C
    };

    public static final int[] COLOR_RARITY_BG = new int[] {
        0x1A222D, 0x142B20, 0x142738, 0x261B33, 0x332612, 0x331414
    };

    private static int lastPlayerHp = -1;
    private static int trailPlayerHp = -1;

    public static void update() {
        animTick++;
        if (GameScreen.player != null) {
            if (lastPlayerHp == -1 || trailPlayerHp == -1) {
                lastPlayerHp = GameScreen.player.Hp;
                trailPlayerHp = GameScreen.player.Hp;
            }
            if (GameScreen.player.Hp < trailPlayerHp) {
                int diff = (trailPlayerHp - GameScreen.player.Hp) / 8;
                if (diff < 1) diff = 1;
                trailPlayerHp -= diff;
            } else {
                trailPlayerHp = GameScreen.player.Hp;
            }
            lastPlayerHp = GameScreen.player.Hp;
        }
    }

    public static void fillGradientRect(mGraphics g, int x, int y, int w, int h, int topColor, int botColor) {
        if (w <= 0 || h <= 0) return;
        int steps = (h < 6) ? h : 6;
        if (steps <= 1) {
            g.setColor(topColor);
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

        for (int i = 0; i < steps; i++) {
            int r = r1 + ((r2 - r1) * i) / (steps - 1);
            int gg = g1 + ((g2 - g1) * i) / (steps - 1);
            int b = b1 + ((b2 - b1) * i) / (steps - 1);
            int c = (r << 16) | (gg << 8) | b;

            int curH = stepH + (i == steps - 1 ? remH : 0);
            g.setColor(c);
            g.fillRect(x, curY, w, curH);
            curY += curH;
        }
    }

    public static void drawCard(mGraphics g, int x, int y, int w, int h, boolean isFocused, int customBorder) {
        if (w <= 0 || h <= 0) return;
        int topCol = isFocused ? COLOR_BG_CARD_HOVER : COLOR_BG_CARD;
        int botCol = isFocused ? COLOR_BG_CARD : COLOR_BG_DARK;
        fillGradientRect(g, x, y, w, h, topCol, botCol);

        int borderCol = customBorder != -1 ? customBorder : (isFocused ? COLOR_GOLD_BRIGHT : COLOR_BORDER_SLATE);
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);
    }

    public static void drawProgressBar(mGraphics g, int x, int y, int w, int h, long cur, long max, int colorTop, int colorBot, String customText, int trailVal) {
        if (w <= 0 || h <= 0) return;
        if (max <= 0) max = 1;
        if (cur < 0) cur = 0;
        if (cur > max) cur = max;

        g.setColor(0x080E17);
        g.fillRect(x, y, w, h);

        g.setColor(0x1B283C);
        g.drawRect(x, y, w - 1, h - 1);

        int maxFillW = w - 2;
        if (trailVal > cur && trailVal > 0) {
            int trailW = (int)(((long)trailVal * maxFillW) / max);
            if (trailW > maxFillW) trailW = maxFillW;
            g.setColor(COLOR_HP_TRAIL);
            g.fillRect(x + 1, y + 1, trailW, h - 2);
        }

        int fillW = (int)((cur * maxFillW) / max);
        if (fillW > 0) {
            fillGradientRect(g, x + 1, y + 1, fillW, h - 2, colorTop, colorBot);
        }

        String displayText = customText;
        if (displayText == null) {
            displayText = cur + " / " + max;
        }

        int textY = y + (h - GameCanvas.hText) / 2;
        mFont.tahoma_7b_black.drawString(g, displayText, x + w / 2 + 1, textY + 1, 2);
        mFont.tahoma_7b_white.drawString(g, displayText, x + w / 2, textY, 2);
    }

    public static void drawSlot(mGraphics g, int x, int y, int size, int rarity, boolean isSelected) {
        UITheme currentTheme = UIThemeManager.getCurrentTheme();
        if (currentTheme == null || currentTheme.id == 0) {
            AvMain.paintRect(g, x, y, size, size, (byte)(isSelected ? 1 : 0), 3);
            return;
        }
        if (rarity < 0) rarity = 0;
        if (rarity >= COLOR_RARITY.length) rarity = COLOR_RARITY.length - 1;

        int bgCol = COLOR_RARITY_BG[rarity];
        fillGradientRect(g, x, y, size, size, bgCol + 0x0A0A0A, bgCol);

        int borderCol = COLOR_RARITY[rarity];
        g.setColor(borderCol);
        g.drawRect(x, y, size - 1, size - 1);

        if (isSelected) {
            int glowCol = (animTick % 20 < 10) ? COLOR_GOLD_BRIGHT : 0xFFFFFF;
            g.setColor(glowCol);
            g.drawRect(x - 1, y - 1, size + 1, size + 1);
            g.drawRect(x, y, size - 1, size - 1);
        }
    }

    public static void paintModernWindow(mGraphics g, int x, int y, int w, int h, String title, byte typePaper) {
        fillGradientRect(g, x, y, w, h, COLOR_BG_WINDOW, COLOR_BG_DARK);

        g.setColor(COLOR_GOLD_DARK);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(COLOR_GOLD_BASE);
        g.drawRect(x + 1, y + 1, w - 3, h - 3);
        g.setColor(COLOR_BORDER_SLATE);
        g.drawRect(x + 3, y + 3, w - 7, h - 7);

        if (title != null && title.length() > 0) {
            int headerW = (w - 60 < 120) ? 120 : (w - 60);
            int headerH = 22;
            int headerX = x + (w - headerW) / 2;
            int headerY = y - 4;

            fillGradientRect(g, headerX, headerY, headerW, headerH, 0x992222, 0x550000);
            g.setColor(COLOR_GOLD_BRIGHT);
            g.drawRect(headerX, headerY, headerW - 1, headerH - 1);

            int titleY = headerY + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, headerX + headerW / 2 + 1, titleY + 1, 2);
            mFont.tahoma_7b_yellow.drawString(g, title, headerX + headerW / 2, titleY, 2);
        }
    }

    public static void paintModernPlayerHUD(mGraphics g, int x, int y, boolean isborder, mFont fontLv) {
        if (GameScreen.player == null) return;
        if (GameCanvas.isCompactMode()) {
            Interface_Game.paintInfoPlayer(g, x, y, isborder, fontLv);
            return;
        }
        if (GameCanvas.isTaiTho) x += 5;

        int avatarSize = 34;
        int barX = x + avatarSize + 6;
        int barW = 120;
        int barH = 11;

        fillGradientRect(g, x, y, avatarSize, avatarSize, COLOR_BG_CARD, COLOR_BG_DARK);
        g.setColor(COLOR_GOLD_BASE);
        g.drawRect(x, y, avatarSize - 1, avatarSize - 1);

        mImage imgHead = (GameScreen.player.Lv < 100) ? Interface_Game.imgIconMPHP : Interface_Game.imgIconMPHP2;
        if (imgHead != null) {
            g.drawImage(imgHead, x + avatarSize / 2, y + avatarSize / 2, 3);
        }

        int lvBoxW = 24;
        int lvBoxH = 10;
        int lvBoxX = x + (avatarSize - lvBoxW) / 2;
        int lvBoxY = y + avatarSize - 4;

        fillGradientRect(g, lvBoxX, lvBoxY, lvBoxW, lvBoxH, 0x8C681E, 0x4D3605);
        g.setColor(COLOR_GOLD_BRIGHT);
        g.drawRect(lvBoxX, lvBoxY, lvBoxW - 1, lvBoxH - 1);

        String lvStr = (GameScreen.player.Lv >= 100) 
            ? ("TT." + GameScreen.player.LvThongThao) 
            : ("Lv." + GameScreen.player.Lv);
        mFont.tahoma_7_white.drawString(g, lvStr, lvBoxX + lvBoxW / 2, lvBoxY, 2);

        int curHp = GameScreen.player.Hp;
        int maxHp = GameScreen.player.maxHp;
        drawProgressBar(g, barX, y, barW, barH, curHp, maxHp, COLOR_HP_TOP, COLOR_HP_BOT, null, trailPlayerHp);

        int curMp = GameScreen.player.Mp;
        int maxMp = GameScreen.player.maxMp;
        drawProgressBar(g, barX, y + barH + 2, barW, barH, curMp, maxMp, COLOR_MP_TOP, COLOR_MP_BOT, null, -1);

        int expPercent = (GameScreen.player.Lv >= 100) ? GameScreen.player.KS : GameScreen.player.percentLv;
        String expStr = ((GameScreen.player.Lv >= 100) ? "TT: " : "EXP: ") + (expPercent / 10) + "." + (expPercent % 10) + "%";
        drawProgressBar(g, barX, y + (barH + 2) * 2, barW, 9, expPercent, 1000, COLOR_EXP_TOP, COLOR_EXP_BOT, expStr, -1);
    }

    public static void drawButton(mGraphics g, int x, int y, int w, int h, String text, int btnType, boolean isPressed, boolean isFocused) {
        if (w <= 0 || h <= 0) return;
        UITheme currentTheme = UIThemeManager.getCurrentTheme();
        if (currentTheme == null || currentTheme.id == 0) {
            AvMain.paintRect(g, x, y, w, h, (byte)(isPressed ? 1 : 0), 1);
            if (text != null && text.length() > 0) {
                mFont f = isPressed ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white;
                f.drawString(g, text, x + w / 2, y + (h - GameCanvas.hText) / 2, 2);
            }
            return;
        }
        int topCol = COLOR_GOLD_BRIGHT;
        int botCol = COLOR_GOLD_BASE;
        int borderCol = COLOR_GOLD_DARK;
        int textCol = 0x000000;

        switch (btnType) {
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

        if (isPressed) {
            int temp = topCol;
            topCol = botCol;
            botCol = temp;
            y += 1;
        }

        fillGradientRect(g, x, y, w, h, topCol, botCol);
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);

        if (text != null && text.length() > 0) {
            int textY = y + (h - GameCanvas.hText) / 2;
            if (textCol == 0xFFFFFF) {
                mFont.tahoma_7b_black.drawString(g, text, x + w / 2 + 1, textY + 1, 2);
                mFont.tahoma_7b_white.drawString(g, text, x + w / 2, textY, 2);
            } else {
                mFont.tahoma_7b_black.drawString(g, text, x + w / 2, textY, 2);
            }
        }
    }

    public static void drawMailCard(mGraphics g, int x, int y, int w, int h, boolean isSelected, int mailType) {
        if (w <= 0 || h <= 0) return;
        int topCol, botCol, borderCol;
        if (isSelected) {
            topCol = COLOR_BG_CARD_HOVER;
            botCol = COLOR_BG_CARD;
            borderCol = COLOR_GOLD_BRIGHT;
        } else {
            switch (mailType) {
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
        fillGradientRect(g, x, y, w, h, topCol, botCol);
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);
    }

    public static void drawBadge(mGraphics g, int x, int y, int count) {
        if (count <= 0) return;
        String text = (count > 9) ? "9+" : ("" + count);
        int bw = (count > 9) ? 14 : 10;
        int bh = 10;
        fillGradientRect(g, x - bw / 2, y - bh / 2, bw, bh, 0xFF3B56, 0xB3001E);
        g.setColor(0xFFE066);
        g.drawRect(x - bw / 2, y - bh / 2, bw - 1, bh - 1);
        mFont.tahoma_7_white.drawString(g, text, x, y - 4, 2);
    }

    public static void paintModernDialogFrame(mGraphics g, int x, int y, int w, int h, String title) {
        if (w <= 0 || h <= 0) return;
        g.setColor(0x000000);
        g.fillRect(x + 2, y + 2, w, h);

        fillGradientRect(g, x, y, w, h, COLOR_BG_WINDOW, COLOR_BG_DARK);

        g.setColor(COLOR_BORDER_SLATE);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(COLOR_GOLD_DARK);
        g.drawRect(x + 1, y + 1, w - 3, h - 3);

        if (title != null && title.length() > 0) {
            int headerH = 22;
            fillGradientRect(g, x + 2, y + 2, w - 4, headerH, 0x1E2B3C, 0x111B2C);
            g.setColor(COLOR_GOLD_BASE);
            g.drawLine(x + 2, y + 2 + headerH, x + w - 3, y + 2 + headerH);

            int titleY = y + 2 + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, titleY + 1, 2);
            mFont.tahoma_7b_yellow.drawString(g, title, x + w / 2, titleY, 2);
        }
    }

    public static void paintModernTooltip(mGraphics g, int x, int y, int w, int h, String title, int rarity) {
        if (w <= 0 || h <= 0) return;
        if (rarity < 0) rarity = 0;
        if (rarity >= COLOR_RARITY.length) rarity = COLOR_RARITY.length - 1;

        int bgCol = COLOR_RARITY_BG[rarity];
        fillGradientRect(g, x, y, w, h, bgCol + 0x080808, COLOR_BG_DARK);

        int borderCol = COLOR_RARITY[rarity];
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);

        if (title != null && title.length() > 0) {
            int headerH = 18;
            fillGradientRect(g, x + 1, y + 1, w - 2, headerH, bgCol + 0x121212, bgCol);
            g.setColor(borderCol);
            g.drawLine(x + 1, y + 1 + headerH, x + w - 2, y + 1 + headerH);

            int textY = y + 1 + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, textY + 1, 2);
            mFont.tahoma_7b_white.drawString(g, title, x + w / 2, textY, 2);
        }
    }

    public static void paintModernTabHeader(mGraphics g, int x, int y, int w, int h, String text, boolean isSelected, boolean isHover) {
        if (w <= 0 || h <= 0) return;
        int topCol, botCol, borderCol;
        if (isSelected) {
            topCol = 0x992222;
            botCol = 0x550000;
            borderCol = COLOR_GOLD_BRIGHT;
        } else if (isHover) {
            topCol = COLOR_BG_CARD_HOVER;
            botCol = COLOR_BG_CARD;
            borderCol = COLOR_GOLD_BASE;
        } else {
            topCol = COLOR_BG_CARD;
            botCol = COLOR_BG_DARK;
            borderCol = COLOR_BORDER_SLATE;
        }

        fillGradientRect(g, x, y, w, h, topCol, botCol);
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);

        if (isSelected) {
            g.setColor(COLOR_GOLD_BRIGHT);
            g.fillRect(x + 2, y + h - 2, w - 4, 2);
        }

        if (text != null && text.length() > 0) {
            int textY = y + (h - GameCanvas.hText) / 2;
            if (isSelected) {
                mFont.tahoma_7b_black.drawString(g, text, x + w / 2 + 1, textY + 1, 2);
                mFont.tahoma_7b_yellow.drawString(g, text, x + w / 2, textY, 2);
            } else {
                mFont.tahoma_7_white.drawString(g, text, x + w / 2, textY, 2);
            }
        }
    }
}
