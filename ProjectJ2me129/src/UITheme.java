public class UITheme {
    public int id;
    public String name;
    public String displayName;

    // Background Tokens
    public int colorBgDark = 0x0C131F;
    public int colorBgWindow = 0x111B2C;
    public int colorBgCard = 0x18253B;
    public int colorBgCardHover = 0x273C5E;

    // Border & Accent Tokens
    public int colorBorderGold = 0xD4AF37;
    public int colorBorderSlate = 0x2D415E;
    public int colorAccentBright = 0xFFE066;

    // Combat Gauge Tokens
    public int colorHpTop = 0xFF3B56;
    public int colorHpBot = 0xB3001E;
    public int colorHpTrail = 0xFFB3BA;
    public int colorMpTop = 0x00D9FF;
    public int colorMpBot = 0x0066CC;
    public int colorExpTop = 0x2ECC71;
    public int colorExpBot = 0x1B7A43;

    // Button Tokens
    public int colorBtnTop = 0xF5C744;
    public int colorBtnBot = 0xB8860B;
    public int colorBtnBorder = 0x7D5700;
    public int colorBtnText = 0x331C00;

    // 6 Tiers Rarity Colors
    public int[] colorRarity = new int[] {
        0x95A5A6, 0x2ECC71, 0x3498DB, 0x9B59B6, 0xF39C12, 0xE74C3C
    };
    public int[] colorRarityBg = new int[] {
        0x1A222D, 0x142B20, 0x142738, 0x261B33, 0x332612, 0x331414
    };

    public UITheme(int id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    public void paintWindow(mGraphics g, int x, int y, int w, int h, String title) {
        if (id == 0) { // Goc (Classic Paper)
            if (AvMain.imgPaper != null && AvMain.imgPaper.length >= 8) {
                AvMain.paintRect(g, x, y, w, h, (byte)0, 0);
            } else {
                paintProceduralWindow(g, x, y, w, h, title);
            }
            return;
        } else if (id == 1) { // N-Paper
            if (AvMain.AA != null && AvMain.AA.length >= 8) {
                paintPaperN(g, x, y, w, h);
            } else {
                paintProceduralWindow(g, x, y, w, h, title);
            }
            return;
        } else if (id == 2) { // Thong Thao (Style B)
            if (AvMain.mimgBgB == null) {
                LoadImageStatic.loadImageBgB();
            }
            if (AvMain.mimgBgB != null && AvMain.mimgBgB.length >= 8) {
                AvMain.paintThongThao(g, x, y, w, h);
                paintTitleBar(g, x, y, w, title);
                return;
            }
        } else if (id == 3) { // Ruong VIP (Style C)
            if (AvMain.mimgBgC == null) {
                LoadImageStatic.loadImageBgC();
            }
            if (AvMain.mimgBgC != null && AvMain.mimgBgC.length >= 8) {
                AvMain.paintRuongVip(g, x, y, w, h);
                paintTitleBar(g, x, y, w, title);
                return;
            }
        }

        // Themes 4 to 9: Modern procedural vector rendering
        paintProceduralWindow(g, x, y, w, h, title);
    }

    private void paintPaperN(mGraphics g, int x, int y, int w, int h) {
        if (AvMain.AA == null) return;
        g.setColor(0xDECDB1);
        g.fillRect(x + 8, y + 8, w - 16, h - 16);
        g.setColor(colorBorderSlate);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(colorBorderGold);
        g.drawRect(x + 2, y + 2, w - 5, h - 5);
    }

    private void paintProceduralWindow(mGraphics g, int x, int y, int w, int h, String title) {
        ModernUI.fillGradientRect(g, x, y, w, h, colorBgWindow, colorBgDark);
        g.setColor(colorBorderSlate);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(colorBorderGold);
        g.drawRect(x + 1, y + 1, w - 3, h - 3);
        g.setColor(colorBgCard);
        g.drawRect(x + 3, y + 3, w - 7, h - 7);

        paintTitleBar(g, x, y, w, title);
    }

    private void paintTitleBar(mGraphics g, int x, int y, int w, String title) {
        if (title != null && title.length() > 0) {
            int headerW = (w - 60 < 130) ? 130 : (w - 60);
            int headerH = 22;
            int headerX = x + (w - headerW) / 2;
            int headerY = y - 4;

            ModernUI.fillGradientRect(g, headerX, headerY, headerW, headerH, colorBgCardHover, colorBgCard);
            g.setColor(colorAccentBright);
            g.drawRect(headerX, headerY, headerW - 1, headerH - 1);

            int titleY = headerY + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, headerX + headerW / 2 + 1, titleY + 1, 2);
            mFont.tahoma_7b_yellow.drawString(g, title, headerX + headerW / 2, titleY, 2);
        }
    }

    public void paintButton(mGraphics g, int x, int y, int w, int h, String text, int btnType, boolean isPressed, boolean isFocus) {
        if (w <= 0 || h <= 0) return;
        int topCol = colorBtnTop;
        int botCol = colorBtnBot;
        int borderCol = colorBtnBorder;
        int textCol = colorBtnText;

        if (btnType == ModernUI.BTN_BLUE) {
            topCol = 0x2980B9; botCol = 0x1A5276; borderCol = 0x5499C7; textCol = 0xFFFFFF;
        } else if (btnType == ModernUI.BTN_GREEN) {
            topCol = 0x27AE60; botCol = 0x196F3D; borderCol = 0x52BE80; textCol = 0xFFFFFF;
        } else if (btnType == ModernUI.BTN_RED) {
            topCol = 0xE74C3C; botCol = 0x922B21; borderCol = 0xEC7063; textCol = 0xFFFFFF;
        } else if (btnType == ModernUI.BTN_GRAY) {
            topCol = 0x566573; botCol = 0x2C3E50; borderCol = 0x7F8C8D; textCol = 0xFFFFFF;
        } else {
            if (isFocus) {
                topCol = colorAccentBright;
                botCol = colorBorderGold;
                borderCol = 0xFFFFFF;
            }
        }

        if (isPressed) {
            int tmp = topCol; topCol = botCol; botCol = tmp;
            y += 1;
        }

        ModernUI.fillGradientRect(g, x, y, w, h, topCol, botCol);
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

    public void paintSlot(mGraphics g, int x, int y, int size, int rarity, boolean isSelected) {
        if (rarity < 0) rarity = 0;
        if (rarity >= colorRarity.length) rarity = colorRarity.length - 1;

        int bgCol = colorRarityBg[rarity];
        ModernUI.fillGradientRect(g, x, y, size, size, bgCol + 0x0A0A0A, bgCol);

        int borderCol = colorRarity[rarity];
        g.setColor(borderCol);
        g.drawRect(x, y, size - 1, size - 1);

        if (isSelected) {
            int glowCol = (GameCanvas.gameTick % 20 < 10) ? colorAccentBright : 0xFFFFFF;
            g.setColor(glowCol);
            g.drawRect(x - 1, y - 1, size + 1, size + 1);
            g.drawRect(x, y, size - 1, size - 1);
        }
    }

    public void paintDialog(mGraphics g, int x, int y, int w, int h, String title) {
        if (w <= 0 || h <= 0) return;
        g.setColor(0x000000);
        g.fillRect(x + 2, y + 2, w, h);

        ModernUI.fillGradientRect(g, x, y, w, h, colorBgWindow, colorBgDark);
        g.setColor(colorBorderSlate);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(colorBorderGold);
        g.drawRect(x + 1, y + 1, w - 3, h - 3);

        if (title != null && title.length() > 0) {
            int headerH = 22;
            ModernUI.fillGradientRect(g, x + 2, y + 2, w - 4, headerH, colorBgCardHover, colorBgCard);
            g.setColor(colorBorderGold);
            g.drawLine(x + 2, y + 2 + headerH, x + w - 3, y + 2 + headerH);

            int titleY = y + 2 + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, titleY + 1, 2);
            mFont.tahoma_7b_yellow.drawString(g, title, x + w / 2, titleY, 2);
        }
    }

    public void paintTooltip(mGraphics g, int x, int y, int w, int h, String title, int rarity) {
        if (w <= 0 || h <= 0) return;
        if (rarity < 0) rarity = 0;
        if (rarity >= colorRarity.length) rarity = colorRarity.length - 1;

        int bgCol = colorRarityBg[rarity];
        ModernUI.fillGradientRect(g, x, y, w, h, bgCol + 0x080808, colorBgDark);

        int borderCol = colorRarity[rarity];
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);

        if (title != null && title.length() > 0) {
            int headerH = 18;
            ModernUI.fillGradientRect(g, x + 1, y + 1, w - 2, headerH, bgCol + 0x141414, bgCol);
            g.setColor(borderCol);
            g.drawLine(x + 1, y + 1 + headerH, x + w - 2, y + 1 + headerH);

            int textY = y + 1 + (headerH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, textY + 1, 2);
            mFont.tahoma_7b_white.drawString(g, title, x + w / 2, textY, 2);
        }
    }

    public void paintTabHeader(mGraphics g, int x, int y, int w, int h, String text, boolean isSelected, boolean isHover) {
        if (w <= 0 || h <= 0) return;
        int topCol, botCol, borderCol;
        if (isSelected) {
            topCol = colorBgCardHover;
            botCol = colorBgCard;
            borderCol = colorAccentBright;
        } else if (isHover) {
            topCol = colorBgCard;
            botCol = colorBgDark;
            borderCol = colorBorderGold;
        } else {
            topCol = colorBgCard;
            botCol = colorBgDark;
            borderCol = colorBorderSlate;
        }

        ModernUI.fillGradientRect(g, x, y, w, h, topCol, botCol);
        g.setColor(borderCol);
        g.drawRect(x, y, w - 1, h - 1);

        if (isSelected) {
            g.setColor(colorAccentBright);
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
