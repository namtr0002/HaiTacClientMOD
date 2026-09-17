public class UITheme {
    public int id;
    public String name;
    public String displayName;

    // Background Tokens
    public int colorBgDark = 0x0C1018;
    public int colorBgWindow = 0x121824;
    public int colorBgCard = 0x1A2434;
    public int colorBgCardHover = 0x26344A;

    // Border & Accent Tokens
    public int colorBorderGold = 0xD4AF37;
    public int colorBorderSlate = 0x2D3E58;
    public int colorAccentBright = 0xFFEB82;

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
        0x141822, 0x10241A, 0x102030, 0x20162A, 0x2A1F0E, 0x2A1010
    };

    // -------------------------------------------------------------
    // Theme 01 Real Artwork Assets (Loaded from PNG)
    // -------------------------------------------------------------
    public static boolean isTheme01Loaded = false;
    public static mImage imgWinCornerTL, imgWinCornerTR, imgWinCornerBL, imgWinCornerBR;
    public static mImage imgWinEdgeT, imgWinEdgeB, imgWinEdgeL, imgWinEdgeR;
    public static mImage imgWinHeaderCrest, imgWinBgPattern;

    public static mImage imgBtnFrameGold, imgBtnFrameBlue, imgBtnFrameGreen, imgBtnFrameRed, imgBtnFrameGray;
    public static mImage imgBtnGold, imgBtnBlue, imgBtnGreen, imgBtnRed, imgBtnGray;
    public static FrameImage fraBtnClose;
    public static FrameImage fraBtnPlusMinus;

    public static FrameImage fraSlotItem;
    public static FrameImage fraSlotItem28;
    public static FrameImage fraSlotRarity;
    public static FrameImage fraSlotEquipBg;

    public static FrameImage fraTabHeader;

    public static FrameImage fraAttack;
    public static FrameImage fraSkillSlot;
    public static mImage imgDpadRing, imgDpadKnob;
    public static mImage imgGaugeHpBg, imgGaugeHpFill, imgGaugeMpFill;
    public static mImage imgIconHp, imgIconMp;

    public static FrameImage fraCheckbox;
    public static mImage imgNotiBanner;
    public static FrameImage fraTfBorder;

    public UITheme(int id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    public static void loadTheme01Assets() {
        if (isTheme01Loaded) return;
        try {
            imgWinCornerTL = mImage.createImage("/theme01/window/win_corner_tl.png");
            imgWinCornerTR = mImage.createImage("/theme01/window/win_corner_tr.png");
            imgWinCornerBL = mImage.createImage("/theme01/window/win_corner_bl.png");
            imgWinCornerBR = mImage.createImage("/theme01/window/win_corner_br.png");
            imgWinEdgeT = mImage.createImage("/theme01/window/win_edge_t.png");
            imgWinEdgeB = mImage.createImage("/theme01/window/win_edge_b.png");
            imgWinEdgeL = mImage.createImage("/theme01/window/win_edge_l.png");
            imgWinEdgeR = mImage.createImage("/theme01/window/win_edge_r.png");
            imgWinHeaderCrest = mImage.createImage("/theme01/window/win_header_crest.png");
            imgWinBgPattern = mImage.createImage("/theme01/window/win_bg_pattern.png");

            imgBtnFrameGold = mImage.createImage("/theme01/button/btn_frame_gold.png");
            imgBtnFrameBlue = mImage.createImage("/theme01/button/btn_frame_blue.png");
            imgBtnFrameGreen = mImage.createImage("/theme01/button/btn_frame_green.png");
            imgBtnFrameRed = mImage.createImage("/theme01/button/btn_frame_red.png");
            imgBtnFrameGray = mImage.createImage("/theme01/button/btn_frame_gray.png");

            imgBtnGold = mImage.createImage("/theme01/button/btn_gold.png");
            imgBtnBlue = mImage.createImage("/theme01/button/btn_blue.png");
            imgBtnGreen = mImage.createImage("/theme01/button/btn_green.png");
            imgBtnRed = mImage.createImage("/theme01/button/btn_red.png");
            imgBtnGray = mImage.createImage("/theme01/button/btn_gray.png");

            mImage mClose = mImage.createImage("/theme01/button/btn_close.png");
            if (mClose != null) fraBtnClose = new FrameImage(mClose, 18, 18);

            mImage mPM = mImage.createImage("/theme01/button/btn_plus_minus.png");
            if (mPM != null) fraBtnPlusMinus = new FrameImage(mPM, 30, 28);

            mImage mSlot = mImage.createImage("/theme01/slot/slot_item.png");
            if (mSlot != null) fraSlotItem = new FrameImage(mSlot, 20, 20);

            mImage mSlot28 = mImage.createImage("/theme01/slot/slot_item_28.png");
            if (mSlot28 != null) fraSlotItem28 = new FrameImage(mSlot28, 28, 28);

            mImage mRarity = mImage.createImage("/theme01/slot/slot_rarity.png");
            if (mRarity != null) fraSlotRarity = new FrameImage(mRarity, 20, 20);

            mImage mEquip = mImage.createImage("/theme01/slot/slot_equip_bg.png");
            if (mEquip != null) fraSlotEquipBg = new FrameImage(mEquip, 18, 19);

            mImage mTab = mImage.createImage("/theme01/tab/tab_header.png");
            if (mTab != null) fraTabHeader = new FrameImage(mTab, 20, 20);

            mImage mAtk = mImage.createImage("/theme01/hud/btn_attack.png");
            if (mAtk != null) fraAttack = new FrameImage(mAtk, 50, 50);

            mImage mSkill = mImage.createImage("/theme01/hud/btn_skill_slot.png");
            if (mSkill != null) fraSkillSlot = new FrameImage(mSkill, 30, 30);

            imgDpadRing = mImage.createImage("/theme01/hud/dpad_ring.png");
            imgDpadKnob = mImage.createImage("/theme01/hud/dpad_knob.png");

            imgGaugeHpBg = mImage.createImage("/theme01/hud/gauge_hp_bg.png");
            imgGaugeHpFill = mImage.createImage("/theme01/hud/gauge_hp_fill.png");
            imgGaugeMpFill = mImage.createImage("/theme01/hud/gauge_mp_fill.png");
            imgIconHp = mImage.createImage("/theme01/hud/icon_hp.png");
            imgIconMp = mImage.createImage("/theme01/hud/icon_mp.png");

            mImage mChk = mImage.createImage("/theme01/control/checkbox.png");
            if (mChk != null) fraCheckbox = new FrameImage(mChk, 14, 14);

            imgNotiBanner = mImage.createImage("/theme01/control/noti_banner.png");

            mImage mTf = mImage.createImage("/theme01/control/tf_border.png");
            if (mTf != null) fraTfBorder = new FrameImage(mTf, 6, 6);

            isTheme01Loaded = true;
        } catch (Exception ignored) {}
    }

    public void paintWindow(mGraphics g, int x, int y, int w, int h, String title) {
        if (id == 0) { // Goc (Classic Original Paper)
            if (AvMain.imgPaper != null && AvMain.imgPaper.length >= 8) {
                AvMain.paintRect(g, x, y, w, h, (byte)0, 0);
            } else {
                g.setColor(colorBgWindow);
                g.fillRect(x, y, w, h);
                g.setColor(colorBorderGold);
                g.drawRect(x, y, w - 1, h - 1);
            }
            return;
        }

        // Theme 1: Neo Pirate Fantasy (Real 9-slice Artwork)
        loadTheme01Assets();

        // 1. Central Background
        g.setColor(colorBgDark);
        g.fillRect(x + 4, y + 4, w - 8, h - 8);

        if (imgWinBgPattern != null) {
            int inX = x + 16;
            int inY = y + 16;
            int inW = w - 32;
            int inH = h - 32;
            if (inW > 0 && inH > 0) {
                for (int py = 0; py < inH; py += 32) {
                    int drawH = (inH - py < 32) ? (inH - py) : 32;
                    for (int px = 0; px < inW; px += 32) {
                        int drawW = (inW - px < 32) ? (inW - px) : 32;
                        g.drawRegion(imgWinBgPattern, 0, 0, drawW, drawH, 0, inX + px, inY + py, 0);
                    }
                }
            }
        }

        // 2. Edges
        int edgeX = w - 40;
        int edgeY = h - 40;

        if (edgeX > 0 && imgWinEdgeT != null) {
            for (int ex = 0; ex < edgeX; ex += 20) {
                int sw = (edgeX - ex < 20) ? (edgeX - ex) : 20;
                g.drawRegion(imgWinEdgeT, 0, 0, sw, 20, 0, x + 20 + ex, y, 0);
                if (imgWinEdgeB != null) {
                    g.drawRegion(imgWinEdgeB, 0, 0, sw, 20, 0, x + 20 + ex, y + h - 20, 0);
                }
            }
        }

        if (edgeY > 0 && imgWinEdgeL != null) {
            for (int ey = 0; ey < edgeY; ey += 20) {
                int sh = (edgeY - ey < 20) ? (edgeY - ey) : 20;
                g.drawRegion(imgWinEdgeL, 0, 0, 20, sh, 0, x, y + 20 + ey, 0);
                if (imgWinEdgeR != null) {
                    g.drawRegion(imgWinEdgeR, 0, 0, 20, sh, 0, x + w - 20, y + 20 + ey, 0);
                }
            }
        }

        // 3. 4 Corners
        if (imgWinCornerTL != null) g.drawImage(imgWinCornerTL, x, y, 0);
        if (imgWinCornerTR != null) g.drawImage(imgWinCornerTR, x + w - 20, y, 0);
        if (imgWinCornerBL != null) g.drawImage(imgWinCornerBL, x, y + h - 20, 0);
        if (imgWinCornerBR != null) g.drawImage(imgWinCornerBR, x + w - 20, y + h - 20, 0);

        // 4. Header Title & Crest
        if (title != null && title.length() > 0) {
            int crestW = 120;
            int crestH = 24;
            int crestX = x + (w - crestW) / 2;
            int crestY = y - 4;
            if (imgWinHeaderCrest != null) {
                g.drawImage(imgWinHeaderCrest, crestX, crestY, 0);
            }
            int titleY = crestY + (crestH - GameCanvas.hText) / 2;
            mFont.tahoma_7b_black.drawString(g, title, x + w / 2 + 1, titleY + 1, 2);
            mFont.tahoma_7b_yellow.drawString(g, title, x + w / 2, titleY, 2);
        }
    }

    public void paintButton(mGraphics g, int x, int y, int w, int h, String text, int btnType, boolean isPressed, boolean isFocus) {
        if (w <= 0 || h <= 0) return;
        if (id == 0) {
            int idx = isPressed ? 2 : (isFocus ? 1 : 0);
            if (AvMain.imgButton != null && AvMain.imgButton.length > 0 && AvMain.imgButton[0] != null) {
                AvMain.AA(g, x, y, w, h, idx);
            } else {
                g.setColor(isPressed ? colorBtnBot : colorBtnTop);
                g.fillRect(x, y, w, h);
                g.setColor(colorBtnBorder);
                g.drawRect(x, y, w - 1, h - 1);
            }
            return;
        }

        loadTheme01Assets();

        mImage frameImg = imgBtnFrameGold;
        if (btnType == ModernUI.BTN_BLUE) frameImg = imgBtnFrameBlue;
        else if (btnType == ModernUI.BTN_GREEN) frameImg = imgBtnFrameGreen;
        else if (btnType == ModernUI.BTN_RED) frameImg = imgBtnFrameRed;
        else if (btnType == ModernUI.BTN_GRAY) frameImg = imgBtnFrameGray;

        int frameIdx = isPressed ? 2 : (isFocus ? 1 : 0);
        int srcY = frameIdx * 20;

        if (frameImg != null) {
            int capW = 8;
            int midSrcW = 14;
            int midDestW = w - capW * 2;
            if (midDestW < 0) {
                capW = w / 2;
                midDestW = 0;
            }

            // Left cap
            g.drawRegion(frameImg, 0, srcY, capW, 20, 0, x, y, 0);

            // Middle body
            if (midDestW > 0) {
                for (int mx = 0; mx < midDestW; mx += midSrcW) {
                    int sw = (midDestW - mx < midSrcW) ? (midDestW - mx) : midSrcW;
                    g.drawRegion(frameImg, capW, srcY, sw, 20, 0, x + capW + mx, y, 0);
                }
            }

            // Right cap
            g.drawRegion(frameImg, 30 - capW, srcY, capW, 20, 0, x + w - capW, y, 0);
        } else {
            g.setColor(isPressed ? colorBtnBot : colorBtnTop);
            g.fillRect(x, y, w, h);
            g.setColor(colorBtnBorder);
            g.drawRect(x, y, w - 1, h - 1);
        }

        if (text != null && text.length() > 0) {
            int textY = y + (h - GameCanvas.hText) / 2 + (isPressed ? 1 : 0);
            mFont.tahoma_7b_black.drawString(g, text, x + w / 2 + 1, textY + 1, 2);
            if (btnType == ModernUI.BTN_GOLD) {
                mFont.tahoma_7b_yellow.drawString(g, text, x + w / 2, textY, 2);
            } else {
                mFont.tahoma_7b_white.drawString(g, text, x + w / 2, textY, 2);
            }
        }
    }

    public void paintSlot(mGraphics g, int x, int y, int size, int rarity, boolean isSelected) {
        if (id == 0) {
            if (AvMain.imgDaKham != null) {
                int frame = isSelected ? 1 : 0;
                g.drawRegion(AvMain.imgDaKham, 0, frame * 20, 20, 20, 0, x, y, 0);
            } else {
                g.setColor(colorBgCard);
                g.fillRect(x, y, size, size);
                g.setColor(colorBorderSlate);
                g.drawRect(x, y, size - 1, size - 1);
            }
            return;
        }

        loadTheme01Assets();

        FrameImage slotFra = (size >= 26 && fraSlotItem28 != null) ? fraSlotItem28 : fraSlotItem;
        if (slotFra != null) {
            slotFra.drawFrame(isSelected ? 1 : 0, x, y, 0, 0, g);
        } else {
            g.setColor(colorBgCard);
            g.fillRect(x, y, size, size);
            g.setColor(colorBorderSlate);
            g.drawRect(x, y, size - 1, size - 1);
        }

        if (rarity >= 0 && fraSlotRarity != null) {
            int r = (rarity < 6) ? rarity : 5;
            fraSlotRarity.drawFrame(r, x, y, 0, 0, g);
        }
    }

    public void paintTabHeader(mGraphics g, int x, int y, int w, int h, String text, boolean isSelected, boolean isHover) {
        if (w <= 0 || h <= 0) return;
        if (id == 0) {
            if (MainTab.mImgTab != null && MainTab.mImgTab.length > 0 && MainTab.mImgTab[0] != null) {
                g.drawRegion(MainTab.mImgTab[0], 0, isSelected ? 20 : 0, 20, 20, 0, x, y, 0);
            } else {
                g.setColor(isSelected ? colorBgCardHover : colorBgCard);
                g.fillRect(x, y, w, h);
                g.setColor(colorBorderGold);
                g.drawRect(x, y, w - 1, h - 1);
            }
            return;
        }

        loadTheme01Assets();

        if (fraTabHeader != null && fraTabHeader.imgFrame != null) {
            int srcY = isSelected ? 20 : 0;
            int capW = 6;
            int midW = w - capW * 2;
            if (midW < 0) {
                capW = w / 2;
                midW = 0;
            }
            g.drawRegion(fraTabHeader.imgFrame, 0, srcY, capW, 20, 0, x, y, 0);
            if (midW > 0) {
                for (int mx = 0; mx < midW; mx += 8) {
                    int sw = (midW - mx < 8) ? (midW - mx) : 8;
                    g.drawRegion(fraTabHeader.imgFrame, capW, srcY, sw, 20, 0, x + capW + mx, y, 0);
                }
            }
            g.drawRegion(fraTabHeader.imgFrame, 20 - capW, srcY, capW, 20, 0, x + w - capW, y, 0);
        } else {
            g.setColor(isSelected ? colorBgCardHover : colorBgCard);
            g.fillRect(x, y, w, h);
            g.setColor(colorBorderGold);
            g.drawRect(x, y, w - 1, h - 1);
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

    public void paintGauge(mGraphics g, int x, int y, int w, int h, int cur, int max, int type) {
        if (w <= 0 || h <= 0) return;
        loadTheme01Assets();

        if (imgGaugeHpBg != null) {
            g.drawImage(imgGaugeHpBg, x, y, 0);
        } else {
            g.setColor(colorBorderSlate);
            g.drawRect(x, y, w - 1, h - 1);
        }

        if (max <= 0) max = 1;
        if (cur > max) cur = max;
        if (cur < 0) cur = 0;
        int fillW = (cur * (w - 4)) / max;

        mImage fillImg = (type == 1) ? imgGaugeMpFill : imgGaugeHpFill;
        if (fillW > 0 && fillImg != null) {
            g.drawRegion(fillImg, 0, 0, fillW, 14, 0, x + 2, y, 0);
        }
    }

    public void paintCloseButton(mGraphics g, int x, int y, boolean isPressed) {
        loadTheme01Assets();
        if (fraBtnClose != null) {
            fraBtnClose.drawFrame(isPressed ? 1 : 0, x, y, 0, 0, g);
        }
    }

    public void paintDialog(mGraphics g, int x, int y, int w, int h, String title) {
        paintWindow(g, x, y, w, h, title);
    }

    public void paintTooltip(mGraphics g, int x, int y, int w, int h, String title, int rarity) {
        if (w <= 0 || h <= 0) return;
        paintWindow(g, x, y, w, h, title);
    }
}
