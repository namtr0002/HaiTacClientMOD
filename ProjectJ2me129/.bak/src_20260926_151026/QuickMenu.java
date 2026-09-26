public final class QuickMenu extends Menu {
    private static QuickMenu AT;
    public static FrameImage[] fraQuickMenu;
    public static mImage imgNenMenu;
    public static mImage imgTamGiac;

    // ==========================================
    // 2-TIER STAGGERED RADIAL ARC PARAMETERS
    // ==========================================
    public float animProgress = 0f;
    public int originX = 18;
    public int originY = 120;
    public int hoveredItem = -1;
    public int openDelay = 0;
    public boolean waitForNewTouch = true;
    private boolean wasPointerDown = false;

    public int[] itemPosX;
    public int[] itemPosY;

    private static int[][] AY = new int[][]{
        new int[]{0, 0},
        new int[]{12, 12},
        new int[]{7, 7},
        new int[]{2, 2},
        new int[]{10, 10},
        new int[]{6, 6},
        new int[]{14, 14},
        new int[]{15, 15},
        new int[]{13, 13},
        new int[]{3, 3},
        new int[]{5, 5},
        new int[]{9, 8},
        new int[]{11, 11},
        new int[]{8, 9}
    };

    public static QuickMenu AI() {
        return AT == null ? (AT = new QuickMenu()) : AT;
    }

    public static QuickMenu gI() {
        return AI();
    }

    public static FrameImage getFraQuickMenu(int index) {
        if (fraQuickMenu != null && index >= 0 && index < fraQuickMenu.length && fraQuickMenu[index] != null) {
            return fraQuickMenu[index];
        }
        if (index == 16) {
            try {
                mImage img = mImage.createImage("/point/quick_16.png");
                if (img != null && img.image != null) {
                    FrameImage fra16 = new FrameImage(img, 30, 30);
                    if (fraQuickMenu != null && index < fraQuickMenu.length) {
                        fraQuickMenu[index] = fra16;
                    }
                    return fra16;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public final void AJ() {
        this.beginMenu();
        super.cmdClose = new iCommand("", -1, this);
        this.openDelay = 2;
        this.waitForNewTouch = false;
        this.wasPointerDown = GameCanvas.isPointerDown;
        this.hoveredItem = -1;
        super.AC = -1;
        this.animProgress = 0f;

        // Consume existing touches from the button tap that triggered this menu
        GameCanvas.isPointerSelect = false;

        super.menuItems = new mVector();

        for (int i = 0; i < AY.length; ++i) {
            int cmdId = AY[i][0];
            int iconId = AY[i][1];

            if (cmdId == 6 && (Player.vecParty == null || Player.vecParty.size() <= 0)) {
                continue;
            }
            if (cmdId == 10 && (GameScreen.player == null || GameScreen.player.clan == null)) {
                continue;
            }

            String cap = (cmdId >= 0 && cmdId < T.WJ.length) ? T.WJ[cmdId] : T.AS;
            iCommand cmd = new iCommand(cap, cmdId, this);
            if (fraQuickMenu != null && iconId >= 0 && iconId < fraQuickMenu.length && fraQuickMenu[iconId] != null) {
                cmd.AA(fraQuickMenu[iconId]);
            }
            super.menuItems.addElement(cmd);
        }

        super.wUni = 60;
        super.menuW = 50;
        this.originX = GameCanvas.isTaiTho ? 38 : 18;
        this.originY = MotherCanvas.h / 2;

        int total = super.menuItems.size();
        this.itemPosX = new int[total];
        this.itemPosY = new int[total];

        int rInner = (MotherCanvas.h < 240) ? 46 : ((MotherCanvas.h < 320) ? 54 : 62);
        int rOuter = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);

        if (total <= 6) {
            float maxSpan = (MotherCanvas.h < 260) ? 58f : 66f;
            float stepDeg = (total > 1) ? (2 * maxSpan) / (total - 1) : 0f;
            float startDeg = -maxSpan;
            for (int i = 0; i < total; i++) {
                float deg = startDeg + i * stepDeg;
                double rad = deg * 3.1415926535897931 / 180.0;
                itemPosX[i] = originX + (int)(rOuter * (float)Math.cos(rad));
                itemPosY[i] = originY + (int)(rOuter * (float)Math.sin(rad));
            }
        } else {
            int innerCount = total / 2;
            int outerCount = total - innerCount;

            float maxSpanOut = (MotherCanvas.h < 260) ? 68f : 74f;
            float stepDegOut = (outerCount > 1) ? (2 * maxSpanOut) / (outerCount - 1) : 0f;
            float startDegOut = -maxSpanOut;

            float startDegIn = startDegOut + stepDegOut / 2.0f;
            float stepDegIn = stepDegOut;

            for (int i = 0; i < innerCount; i++) {
                float deg = startDegIn + i * stepDegIn;
                double rad = deg * 3.1415926535897931 / 180.0;
                itemPosX[i] = originX + (int)(rInner * (float)Math.cos(rad));
                itemPosY[i] = originY + (int)(rInner * (float)Math.sin(rad));
            }

            for (int i = 0; i < outerCount; i++) {
                float deg = startDegOut + i * stepDegOut;
                double rad = deg * 3.1415926535897931 / 180.0;
                int idx = innerCount + i;
                itemPosX[idx] = originX + (int)(rOuter * (float)Math.cos(rad));
                itemPosY[idx] = originY + (int)(rOuter * (float)Math.sin(rad));
            }
        }

        super.isShowMenu = true;
        GameCanvas.AA((Menu)AI());
        super.backCMD = super.cmdClose;
    }

    public final void startAt() {
        this.AJ();
    }

    public final void commandPointer(int var1, int var2) {
        mVector var3;
        switch(var1) {
            case -1:
                this.doCloseMenu();
                return;
            case 0:
                (var3 = new mVector()).addElement(GameCanvas.gameScr.AZ);
                var3.addElement(GameCanvas.gameScr.BA);
                GameCanvas.menu.startAt(var3, 2, T.MF);
                return;
            case 1:
                GameCanvas.gameScr.BA.AD();
                return;
            case 2:
                GameCanvas.gameScr.AW.AD();
                return;
            case 3:
                (var3 = new mVector()).addElement(GameCanvas.gameScr.AQ);
                var3.addElement(GameCanvas.gameScr.AR);
                GameCanvas.menu.startAt(var3, 2, T.IQ);
                return;
            case 4:
                GameCanvas.gameScr.AR.AD();
                return;
            case 5:
                GameCanvas.gameScr.BC.AD();
                return;
            case 6:
                GameCanvas.gameScr.AS.AD();
                return;
            case 7:
                GameCanvas.gameScr.BQ.AD();
                return;
            case 8:
                GameCanvas.gameScr.AU.AD();
                return;
            case 9:
                GameCanvas.gameScr.BJ.AD();
                return;
            case 10:
                GameCanvas.gameScr.BM.AD();
                return;
            case 11:
                if (!GameCanvas.isIos()) {
                    GameCanvas.gameScr.BL.AD();
                    return;
                }
                break;
            case 12:
                GameCanvas.gameScr.BP.AD();
                return;
            case 13:
                GameCanvas.gameScr.BU.AD();
                return;
            case 14:
                GameCanvas.gameScr.BV.AD();
                return;
            case 15:
                DualTabScreen.gI().curMainTab = 4;
                DualTabScreen.gI().openPetSubView();
                DualTabScreen.gI().Show((MainScreen)GameCanvas.gameScr);
                return;
        }
    }

    public final void AB(mGraphics var1) {
        GameCanvas.resetTrans(var1);

        if (super.menuItems == null || super.menuItems.size() == 0 || this.itemPosX == null) return;

        int total = super.menuItems.size();

        // 1. Ve tam banh lai
        if (AvMain.fraBtBanhlai != null) {
            AvMain.fraBtBanhlai.drawFrame(0, this.originX, this.originY, 0, 3, var1);
        }

        // 2. Ve cac tia laser nang luong
        for (int i = 0; i < total && i < this.itemPosX.length; i++) {
            int itemX = this.originX + (int)((this.itemPosX[i] - this.originX) * this.animProgress);
            int itemY = this.originY + (int)((this.itemPosY[i] - this.originY) * this.animProgress);
            boolean isSel = (i == super.AC || i == this.hoveredItem);

            if (isSel) {
                var1.setColor(16768815);
                var1.drawLine(this.originX, this.originY, itemX, itemY);
                var1.drawLine(this.originX, this.originY - 1, itemX, itemY - 1);
            } else {
                var1.setColor(10515250);
                var1.drawLine(this.originX, this.originY, itemX, itemY);
            }
        }

        // 3. Ve tung icon box
        for (int i = 0; i < total && i < this.itemPosX.length; i++) {
            int itemX = this.originX + (int)((this.itemPosX[i] - this.originX) * this.animProgress);
            int itemY = this.originY + (int)((this.itemPosY[i] - this.originY) * this.animProgress);
            boolean isSel = (i == super.AC || i == this.hoveredItem);

            int size = isSel ? 28 : 26;

            AvMain.paintRect(var1, itemX - size / 2, itemY - size / 2, size, size, (byte)(isSel ? 1 : 0), (isSel ? 0 : 1));

            if (isSel && AvMain.imgNenfocus != null) {
                var1.drawRegion(AvMain.imgNenfocus, 2, 2, size, size, 0, itemX - size / 2, itemY - size / 2, 0);
            }

            iCommand cmd = (iCommand)super.menuItems.elementAt(i);
            if (cmd != null) {
                cmd.AA(var1, itemX, itemY, (byte)(isSel ? 1 : 0));
            }
        }

        // 4. Banner HUD ten chuc nang
        int selIdx = (this.hoveredItem >= 0) ? this.hoveredItem : super.AC;
        if (selIdx >= 0 && selIdx < total && this.animProgress >= 0.7f) {
            iCommand selCmd = (iCommand)super.menuItems.elementAt(selIdx);
            if (selCmd != null && selCmd.caption != null && selCmd.caption.length() > 0) {
                int tw = mFont.tahoma_7b_white.getWidth(selCmd.caption);
                int hudW = tw + 20;
                int hudH = 18;
                int hudX = this.originX + 28;
                int rOutMax = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);
                int hudY = this.originY - rOutMax - 14;
                if (hudY < 4) hudY = 4;
                if (hudX + hudW > MotherCanvas.w - 4) hudX = MotherCanvas.w - hudW - 4;

                AvMain.paintRect(var1, hudX, hudY, hudW, hudH, (byte)1, 0);
                mFont.tahoma_7b_white.drawString(var1, selCmd.caption, hudX + hudW / 2, hudY + 3, 2);
            }
        }
    }

    public final void paintMenu(mGraphics var1) {
        this.AB(var1);
    }

    public final void AH() {
        if (super.isShowMenu) {
            if (this.animProgress < 1.0f) {
                this.animProgress += (1.0f - this.animProgress) * 0.40f + 0.06f;
                if (this.animProgress >= 1.0f) this.animProgress = 1.0f;
            }
            this.updatePointer();
        }
    }

    public final void updateMenu() {
        this.AH();
    }

    public final void updatePointer() {
        if (!super.isShowMenu || super.menuItems == null || this.itemPosX == null) return;

        boolean pointerDownNow = GameCanvas.isPointerDown;
        boolean isRelease = this.wasPointerDown && !pointerDownNow && !GameCanvas.isPointerSelect;
        this.wasPointerDown = pointerDownNow;

        if (this.openDelay > 0) {
            this.openDelay--;
            GameCanvas.isPointerSelect = false;
            return;
        }

        if (this.waitForNewTouch) {
            if (!pointerDownNow && !GameCanvas.isPointerSelect) {
                this.waitForNewTouch = false;
            }
            GameCanvas.isPointerSelect = false;
            return;
        }

        int total = super.menuItems.size();
        boolean isSelect = GameCanvas.isPointerSelect;

        int px = GameCanvas.AY;
        int py = GameCanvas.AZ;

        int dxCenter = px - this.originX;
        int dyCenter = py - this.originY;
        int distCenterSq = dxCenter * dxCenter + dyCenter * dyCenter;

        int bestIdx = -1;
        int bestDistSq = 999999;
        int grabRadius = (MotherCanvas.h < 260) ? 22 : 28;
        int grabRadiusSq = grabRadius * grabRadius;

        for (int i = 0; i < total && i < this.itemPosX.length; i++) {
            int itemX = this.originX + (int)((this.itemPosX[i] - this.originX) * this.animProgress);
            int itemY = this.originY + (int)((this.itemPosY[i] - this.originY) * this.animProgress);
            int dx = px - itemX;
            int dy = py - itemY;
            int dSq = dx * dx + dy * dy;

            if (dSq < bestDistSq) {
                bestDistSq = dSq;
                bestIdx = i;
            }
        }

        if (bestDistSq <= grabRadiusSq) {
            this.hoveredItem = bestIdx;
            super.AC = bestIdx;
        } else if (bestDistSq > 40 * 40) {
            this.hoveredItem = -1;
        }

        if (isSelect) {
            GameCanvas.isPointerSelect = false;

            if (bestDistSq <= grabRadiusSq && bestIdx >= 0 && bestIdx < super.menuItems.size()) {
                ((iCommand)super.menuItems.elementAt(bestIdx)).AD();
                super.isShowMenu = false;
                this.hoveredItem = -1;
                return;
            } else if (distCenterSq < 25 * 25) {
                super.isShowMenu = false;
                this.hoveredItem = -1;
                return;
            } else if (this.animProgress >= 0.85f) {
                int rOutMax = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);
                if (distCenterSq > (rOutMax + 45) * (rOutMax + 45)) {
                    super.isShowMenu = false;
                    this.hoveredItem = -1;
                    return;
                }
            }
        }

        if (isRelease) {
            if (this.hoveredItem >= 0 && this.hoveredItem < super.menuItems.size()) {
                ((iCommand)super.menuItems.elementAt(this.hoveredItem)).AD();
                super.isShowMenu = false;
                this.hoveredItem = -1;
                return;
            }
        }
    }

    public final void AF() {
        if (super.menuItems == null || super.menuItems.size() == 0) return;

        if (GameCanvas.AL[41] || GameCanvas.isKeyPressed(41) || GameCanvas.AE(4)) {
            GameCanvas.AB(41);
            GameCanvas.clearKeyPressed(41);
            GameCanvas.AF(4);
            super.isShowMenu = false;
            this.hoveredItem = -1;
            return;
        }

        boolean flag = false;
        if (!GameCanvas.isKeyPressed(0) && !GameCanvas.isKeyPressed(1)) {
            if (GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3)) {
                if (super.AC < 0 || super.AC >= super.menuItems.size() - 1) {
                    super.AC = 0;
                } else {
                    ++super.AC;
                }
                GameCanvas.clearKeyPressed(2);
                GameCanvas.clearKeyPressed(3);
                flag = true;
            }
        } else {
            if (super.AC <= 0) {
                super.AC = super.menuItems.size() - 1;
            } else {
                --super.AC;
            }
            GameCanvas.clearKeyPressed(0);
            GameCanvas.clearKeyPressed(1);
            flag = true;
        }

        if (flag) {
            super.AC = AvMain.AA(super.AC, super.menuItems.size() - 1, false);
        }

        if (GameCanvas.AL[5] || GameCanvas.isKeyPressed(5)) {
            GameCanvas.AB(5);
            GameCanvas.clearKeyPressed(5);
            if (super.AC < super.menuItems.size() && super.AC >= 0) {
                ((iCommand)super.menuItems.elementAt(super.AC)).AD();
                super.isShowMenu = false;
            }
        }

        this.AS();
    }

    public final void updateMenuKey() {
        this.AF();
    }
}
