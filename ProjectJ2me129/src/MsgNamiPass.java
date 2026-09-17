public final class MsgNamiPass extends MsgDialog {

    public static class PassItem {
        public byte itemType;
        public short itemId;
        public int quantity;
        public short iconId;
        public String name;
        public String desc;
        public byte status; // 0 = Lock, 1 = Claimable, 2 = Claimed
    }

    public static class PassMilestone {
        public int level;
        public PassItem freeItem;
        public PassItem vipItem;
    }

    public static class PreviewItem {
        public byte itemType;
        public short itemId;
        public String qtyDisplay;
        public short iconId;
        public String name;
        public String rateText;
    }

    public static mVector vecMilestones = new mVector();
    public static mVector vecPreview = new mVector();
    public static String seasonTitle = "BOOYAH NAMI PASS";
    public static int currentLevel = 1;
    public static long currentExp = 0;
    public static long maxExp = 1500;
    public static boolean isVip = false;
    public static boolean isElitePlus = false;
    public static int overLevels = 0;
    public static int unclaimedOverBoxes = 0;

    private int selectedIndex = 0;
    private int selectedTrack = 0; // 0 = Free, 1 = VIP
    private ListNew listScroll;
    private int cellWidth = 46;
    private boolean isShowPreviewPopup = false;

    // Responsive layout coords
    private int trackAreaX, trackAreaY, trackAreaW, trackAreaH;
    private int footX, footY, footW, footH;
    private int btnVipX, btnVipY, btnVipW, btnVipH;
    private int btnClaimAllX, btnClaimAllY, btnClaimAllW, btnClaimAllH;
    private int btnBuyLvX, btnBuyLvY, btnBuyLvW, btnBuyLvH;
    private int btnActionX, btnActionY, btnActionW, btnActionH;

    public void initData(String title, int lv, long exp, long maxE, boolean vip, boolean elite, int overLv, int unclaimOver, mVector milestones, mVector previews) {
        seasonTitle = title;
        currentLevel = lv;
        currentExp = exp;
        maxExp = maxE;
        isVip = vip;
        isElitePlus = elite;
        overLevels = overLv;
        unclaimedOverBoxes = unclaimOver;
        vecMilestones = milestones;
        vecPreview = previews;
        isShowPreviewPopup = false;

        // Dynamic responsive sizing
        super.wDia = MotherCanvas.w - 16;
        if (super.wDia > 440) super.wDia = 440;
        if (super.wDia < 270) super.wDia = 270;

        super.hDia = MotherCanvas.h - 16;
        if (super.hDia > 236) super.hDia = 236;
        if (super.hDia < 196) super.hDia = 196;

        super.AX = MotherCanvas.w / 2 - super.wDia / 2;
        super.AY = MotherCanvas.h / 2 - super.hDia / 2;

        // Layout calculation
        int headerY = super.AY + 18;
        int headerH = 22;

        btnClaimAllW = 70;
        btnClaimAllH = headerH;
        btnClaimAllX = super.AX + super.wDia - 12 - btnClaimAllW;
        btnClaimAllY = headerY;

        btnVipW = 66;
        btnVipH = headerH;
        btnVipX = btnClaimAllX - btnVipW - 6;
        btnVipY = headerY;

        trackAreaX = super.AX + 12;
        trackAreaY = headerY + headerH + 4;
        trackAreaW = super.wDia - 24;
        trackAreaH = 96;

        footX = super.AX + 12;
        footY = trackAreaY + trackAreaH + 4;
        footW = super.wDia - 24;
        footH = (super.AY + super.hDia) - footY - 8;
        if (footH < 54) footH = 54;

        btnBuyLvW = 68;
        btnBuyLvH = 22;
        btnActionW = 68;
        btnActionH = 22;

        btnActionX = footX + footW - 8 - btnActionW;
        btnActionY = footY + footH / 2 - btnActionH / 2;

        btnBuyLvX = btnActionX - btnBuyLvW - 6;
        btnBuyLvY = btnActionY;

        int totalColumns = (vecMilestones != null ? vecMilestones.size() : 100) + 1;
        int totalContentW = totalColumns * cellWidth + 16;
        this.listScroll = new ListNew(trackAreaX, trackAreaY, trackAreaW, trackAreaH, 0, 0, Math.max(0, totalContentW - trackAreaW), true);

        // Close command setup
        super.AG = new iCommand(T.close, -1, this);
        if (GameCanvas.isTouch) {
            super.AG.setPos(super.AX + super.wDia - 14, super.AY + 10, MainTab.fraCloseTab, "");
        }
        super.cmdList.removeAllElements();
        super.cmdList.addElement(super.AG);

        // Wire softkeys & back command for full keypad/controller compatibility
        super.DB = super.AG;       // Softkey Right = Close
        super.backCMD = super.AG;  // Back / ESC = Close

        if (vecMilestones != null && vecMilestones.size() > 0) {
            this.selectedIndex = Math.min(vecMilestones.size() - 1, Math.max(0, currentLevel - 1));
            focusCameraOnIndex(this.selectedIndex);
        }
    }

    private void focusCameraOnIndex(int idx) {
        if (this.listScroll != null) {
            int targetX = idx * cellWidth - (trackAreaW / 2 - cellWidth / 2);
            this.listScroll.AA(targetX);
        }
    }

    public static String formatQty(int qty) {
        if (qty >= 1000000) {
            return (qty / 1000000) + "M";
        } else if (qty >= 10000) {
            return (qty / 1000) + "k";
        }
        return "" + qty;
    }

    public static MainImage getItemImage(byte typeObject, short idIcon) {
        if (typeObject == 3) {
            return ObjectData.getImageAll(idIcon, ObjectData.hashImageItem, (short) 3000);
        } else if (typeObject == 7) {
            return ObjectData.getImageAll(idIcon, ObjectData.hashImageMaterialPotion, (short) 6500);
        } else if (typeObject == 4) {
            MainImage img = ObjectData.getImageAll(idIcon, ObjectData.hashImagePotion, (short) 2000);
            if (img == null || img.img == null) {
                img = ObjectData.getImageAll(idIcon, ObjectData.hashImageItemOther, (short) 9000);
            }
            return img;
        } else if (typeObject == 100) {
            return ObjectData.getImageAll(idIcon, ObjectData.hashImageItemOther, (short) 9000);
        } else if (typeObject == 104) {
            return ObjectData.getImageAll(idIcon, ObjectData.hashImageSkill, (short) 4000);
        } else if (typeObject == 105) {
            return ObjectData.getImageAll(idIcon, ObjectData.HashImageFashion, (short) 20000);
        } else if (typeObject == 110) {
            return ObjectData.getImageAll(idIcon, ObjectData.HashImageOtherNew, (short) 23000);
        }
        MainImage img = ObjectData.getImageAll(idIcon, ObjectData.hashImageItemOther, (short) 9000);
        if (img == null || img.img == null) {
            img = ObjectData.getImageAll(idIcon, ObjectData.hashImagePotion, (short) 2000);
        }
        if (img == null || img.img == null) {
            img = ObjectData.getImageAll(idIcon, ObjectData.hashImageItem, (short) 3000);
        }
        return img;
    }

    public void commandPointer(int cmd, int sub) {
        switch (cmd) {
            case -1: // Close Dialog
                isShowPreviewPopup = false;
                GameCanvas.end_Dialog();
                break;
            case 10: // Nhận lẻ 1 mốc
                if (vecMilestones != null && selectedIndex >= 0 && selectedIndex < vecMilestones.size()) {
                    GlobalService.getInstance().sendNamiPassAction((byte) 0, (short) (selectedIndex + 1), (byte) selectedTrack, 0);
                }
                break;
            case 11: // Nhận tất cả
                GlobalService.getInstance().sendNamiPassAction((byte) 1, (short) 0, (byte) 0, 0);
                break;
            case 12: // Nâng cấp VIP / Elite+
                if (!isVip) {
                    GlobalService.getInstance().sendNamiPassAction((byte) 2, (short) 0, (byte) 0, 0);
                } else if (!isElitePlus) {
                    GlobalService.getInstance().sendNamiPassAction((byte) 3, (short) 0, (byte) 0, 0);
                }
                break;
            case 13: // Mua cấp Pass
                GlobalService.getInstance().sendNamiPassAction((byte) 4, (short) 1, (byte) 0, 1);
                break;
            case 14: // Mở Rương Vô Cực
                GlobalService.getInstance().sendNamiPassAction((byte) 5, (short) 0, (byte) 0, unclaimedOverBoxes);
                break;
            case 15: // Bật / Tắt popup soi rương
                isShowPreviewPopup = !isShowPreviewPopup;
                break;
        }
    }

    public void update() {
        if (this.listScroll != null) {
            this.listScroll.AB(); // Vuốt cảm ứng ngang
            this.listScroll.AC(); // Cập nhật camera quán tính
        }
        this.updatePointer();
        this.handleKeyPress();
    }

    public void updatePointer() {
        // 1. Nếu đang mở popup soi rương, chạm bất kỳ đâu để đóng
        if (isShowPreviewPopup) {
            if (GameCanvas.isPointerSelect) {
                isShowPreviewPopup = false;
                GameCanvas.isPointerSelect = false;
            }
            return;
        }

        // 2. Kiểm tra chạm nút [X] đóng ở góc trên bên phải
        if (GameCanvas.isPointerSelect) {
            if (GameCanvas.isPoint(super.AX + super.wDia - 34, super.AY - 6, 40, 34)) {
                commandPointer(-1, 0);
                GameCanvas.isPointerSelect = false;
                return;
            }
        }

        // 3. Chọn slot trực tiếp khi chạm vào vùng cuộn dual-track
        if (GameCanvas.isPointerSelect || (GameCanvas.isPointerDown && !GameCanvas.AQ)) {
            if (GameCanvas.isPoint(trackAreaX, trackAreaY, trackAreaW, trackAreaH)) {
                int cmx = (listScroll != null) ? listScroll.AC : 0;
                int clickRelX = GameCanvas.AY - (trackAreaX + 4 - cmx);
                int clickedIndex = clickRelX / cellWidth;

                if (clickedIndex < 0) clickedIndex = 0;
                if (vecMilestones != null && clickedIndex > vecMilestones.size()) clickedIndex = vecMilestones.size();

                if (vecMilestones != null && clickedIndex >= 0 && clickedIndex < vecMilestones.size()) {
                    int clickedTrack = (GameCanvas.AZ >= trackAreaY + 46) ? 1 : 0;
                    if (GameCanvas.isPointerSelect && selectedIndex == clickedIndex && selectedTrack == clickedTrack) {
                        PassItem curItem = getSelectedItem();
                        if (curItem != null && curItem.status == 1) {
                            commandPointer(10, 0);
                        } else if (selectedTrack == 1 && !isVip) {
                            commandPointer(12, 0);
                        }
                    } else {
                        selectedIndex = clickedIndex;
                        selectedTrack = clickedTrack;
                    }
                } else if (vecMilestones != null && clickedIndex == vecMilestones.size()) {
                    if (GameCanvas.isPointerSelect) {
                        if (GameCanvas.AZ >= trackAreaY + 54) {
                            if (GameCanvas.AY >= trackAreaX + 4 - cmx + vecMilestones.size() * cellWidth + 28) {
                                commandPointer(14, 0); // Nút Mở
                            } else {
                                commandPointer(15, 0); // Nút Soi
                            }
                        } else {
                            commandPointer(15, 0); // Chạm Rương Vô Cực
                        }
                    }
                    selectedIndex = clickedIndex;
                }
                if (GameCanvas.isPointerSelect) {
                    GameCanvas.isPointerSelect = false;
                }
                return;
            }
        }

        if (GameCanvas.isPointerSelect) {
            // 4. Nút [MỞ VIP] / [VIP ON] (Header)
            if (GameCanvas.isPoint(btnVipX, btnVipY, btnVipW, btnVipH)) {
                commandPointer(12, 0);
                GameCanvas.isPointerSelect = false;
                return;
            }

            // 5. Nút [NHẬN HẾT] (Header)
            if (GameCanvas.isPoint(btnClaimAllX, btnClaimAllY, btnClaimAllW, btnClaimAllH)) {
                commandPointer(11, 0);
                GameCanvas.isPointerSelect = false;
                return;
            }

            // 6. Nút Footer: [MUA CẤP]
            if (GameCanvas.isPoint(btnBuyLvX, btnBuyLvY, btnBuyLvW, btnBuyLvH)) {
                if (selectedIndex == vecMilestones.size()) {
                    commandPointer(15, 0); // Soi kho báu
                } else {
                    commandPointer(13, 0); // Mua cấp
                }
                GameCanvas.isPointerSelect = false;
                return;
            }

            // 7. Nút Footer: Thao tác chính [NHẬN QUÀ] / [MỞ VIP] / [MỞ RƯƠNG]
            if (GameCanvas.isPoint(btnActionX, btnActionY, btnActionW, btnActionH)) {
                if (selectedIndex == vecMilestones.size()) {
                    if (unclaimedOverBoxes > 0) {
                        commandPointer(14, 0); // Mở rương
                    } else {
                        commandPointer(15, 0); // Soi rương
                    }
                } else {
                    if (selectedTrack == 1 && !isVip) {
                        commandPointer(12, 0); // Mở VIP
                    } else {
                        PassItem item = getSelectedItem();
                        if (item != null && item.status == 1) {
                            commandPointer(10, 0); // Nhận quà
                        } else if (item != null && item.status == 0) {
                            commandPointer(13, 0); // Mua cấp nếu chưa đạt
                        }
                    }
                }
                GameCanvas.isPointerSelect = false;
                return;
            }

            // 8. Đóng Dialog khi nhấn ra ngoài khu vực khung
            if (!GameCanvas.isPoint(super.AX, super.AY, super.wDia, super.hDia)) {
                commandPointer(-1, 0);
                GameCanvas.isPointerSelect = false;
                return;
            }
        }

        super.updatePointer();
    }

    public void AA(int keyCode) {
        // Raw KeyCode hook (ESC, Backspace, Softkey 2, Clear)
        if (keyCode == -7 || keyCode == -22 || keyCode == 27 || keyCode == -1 || keyCode == 8) {
            if (isShowPreviewPopup) {
                isShowPreviewPopup = false;
            } else {
                commandPointer(-1, 0);
            }
            return;
        }
        if (keyCode == 10 || keyCode == 13 || keyCode == -5) {
            handleOkAction();
            return;
        }
    }

    public void handleKeyPress() {
        // Xử lý phím F2 / Softkey Phải / Back / Esc
        if (GameCanvas.keyMyHold[13] || GameCanvas.keyMyHold[41] || GameCanvas.isKeyPressed(13) || GameCanvas.isKeyPressed(41)) {
            GameCanvas.clearKeyHold(13);
            GameCanvas.clearKeyHold(41);
            GameCanvas.clearKeyPressed(13);
            GameCanvas.clearKeyPressed(41);
            if (isShowPreviewPopup) {
                isShowPreviewPopup = false;
            } else {
                commandPointer(-1, 0);
            }
            return;
        }

        // Xử lý phím F1 / Softkey Trái (Thao tác nhanh)
        if (GameCanvas.keyMyHold[12] || GameCanvas.keyMyHold[40] || GameCanvas.isKeyPressed(12) || GameCanvas.isKeyPressed(40)) {
            GameCanvas.clearKeyHold(12);
            GameCanvas.clearKeyHold(40);
            GameCanvas.clearKeyPressed(12);
            GameCanvas.clearKeyPressed(40);
            handleOkAction();
            return;
        }

        boolean move = false;
        if (GameCanvas.isKeyPressed(1)) { // Trái
            if (selectedIndex > 0) {
                selectedIndex--;
                move = true;
            }
            GameCanvas.ClearkeyMove(1);
        } else if (GameCanvas.isKeyPressed(3)) { // Phải
            if (vecMilestones != null && selectedIndex < vecMilestones.size()) {
                selectedIndex++;
                move = true;
            }
            GameCanvas.ClearkeyMove(3);
        } else if (GameCanvas.isKeyPressed(0)) { // Lên (Free Track)
            selectedTrack = 0;
            GameCanvas.ClearkeyMove(0);
        } else if (GameCanvas.isKeyPressed(2)) { // Xuống (VIP Track)
            selectedTrack = 1;
            GameCanvas.ClearkeyMove(2);
        } else if (GameCanvas.keyMyHold[5] || GameCanvas.isKeyPressed(5)) { // OK / Chọn
            GameCanvas.clearKeyHold(5);
            GameCanvas.clearKeyPressed(5);
            handleOkAction();
        }

        if (move) {
            focusCameraOnIndex(selectedIndex);
        }

        this.AS();
    }

    private void handleOkAction() {
        if (isShowPreviewPopup) {
            isShowPreviewPopup = false;
            return;
        }
        if (selectedIndex == vecMilestones.size()) {
            if (unclaimedOverBoxes > 0) {
                commandPointer(14, 0);
            } else {
                commandPointer(15, 0);
            }
        } else {
            if (selectedTrack == 1 && !isVip) {
                commandPointer(12, 0);
            } else {
                PassItem curItem = getSelectedItem();
                if (curItem != null && curItem.status == 1) {
                    commandPointer(10, 0);
                } else {
                    commandPointer(13, 0);
                }
            }
        }
    }

    public void paint(mGraphics g) {
        // 1. NỀN DIALOG CHUẨN HẢI TẶC TÍ HON
        AvMain.AB(g, super.AX, super.AY, super.wDia, super.hDia, 0);
        AvMain.FontBorderColor(g, seasonTitle, super.AX + super.wDia / 2, super.AY - 14, 2, 0, 8);

        // 2. HEADER BAR
        int expBoxX = super.AX + 12;
        int expBoxW = btnVipX - expBoxX - 6;
        if (expBoxW > 180) expBoxW = 180;
        int expBoxY = super.AY + 18;
        int expBoxH = 22;

        // Khung Level & Thanh Tiến Trình EXP
        AvMain.paintRect(g, expBoxX, expBoxY, expBoxW, expBoxH, (byte) 1, 1);
        mFont.tahoma_7b_yellow.drawString(g, "Lv." + currentLevel, expBoxX + 6, expBoxY + 5, 0);

        int barX = expBoxX + 36;
        int barY = expBoxY + 4;
        int barW = expBoxW - 42;
        int barH = 14;
        if (barW > 30) {
            g.setColor(0x1e1e1e);
            g.fillRect(barX, barY, barW, barH);
            int fillW = (int) ((currentExp * barW) / Math.max(1, maxExp));
            if (fillW > barW) fillW = barW;
            if (fillW > 0) {
                g.setColor(0x059669); // Xanh ngọc nổi bật
                g.fillRect(barX + 1, barY + 1, fillW - 2, barH - 2);
            }
            mFont.tahoma_7_white.drawString(g, currentExp + "/" + maxExp, barX + barW / 2, barY + 2, 2);
        }

        // Nút [MỞ VIP] / [VIP ON] (Header)
        int vipColorIdx = isVip ? 4 : 0;
        AvMain.paintRect(g, btnVipX, btnVipY, btnVipW, btnVipH, (byte) 1, vipColorIdx);
        String vipText = isVip ? (isElitePlus ? "[ELITE+]" : "[VIP ON]") : "[MỞ VIP]";
        (isVip ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white).drawString(g, vipText, btnVipX + btnVipW / 2, btnVipY + 5, 2);

        // Nút [NHẬN HẾT] (Header)
        AvMain.paintRect(g, btnClaimAllX, btnClaimAllY, btnClaimAllW, btnClaimAllH, (byte) 1, 3);
        mFont.tahoma_7b_white.drawString(g, "[NHẬN HẾT]", btnClaimAllX + btnClaimAllW / 2, btnClaimAllY + 5, 2);

        // 3. DUAL TRACK SCROLL VIEWPORT
        AvMain.paintRect(g, trackAreaX, trackAreaY, trackAreaW, trackAreaH, (byte) 1, 6);
        g.setClip_(trackAreaX + 2, trackAreaY + 2, trackAreaW - 4, trackAreaH - 4);
        int cmx = (listScroll != null) ? listScroll.AC : 0;
        int startX = trackAreaX + 4 - cmx;

        if (vecMilestones != null) {
            for (int i = 0; i < vecMilestones.size(); i++) {
                PassMilestone ms = (PassMilestone) vecMilestones.elementAt(i);
                int itemX = startX + i * cellWidth;
                if (itemX + cellWidth < trackAreaX - 20 || itemX > trackAreaX + trackAreaW + 20) continue;

                // Track Trên: FREE
                paintRewardSlot(g, itemX + 2, trackAreaY + 4, ms.freeItem, i == selectedIndex && selectedTrack == 0, false, ms.level);

                // Dây Line Nối & Huy Hiệu Cấp Mốc Giữa
                int badgeY = trackAreaY + 36;
                boolean reached = currentLevel >= ms.level;
                g.setColor(reached ? -479136 : -7967666);
                g.fillRect(itemX, badgeY + 7, cellWidth, 2);

                AvMain.paintRect(g, itemX + 11, badgeY, 22, 16, (byte) 1, reached ? 0 : 4);
                (reached ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white).drawString(g, "" + ms.level, itemX + 22, badgeY + 3, 2);

                // Track Dưới: VIP
                paintRewardSlot(g, itemX + 2, trackAreaY + 56, ms.vipItem, i == selectedIndex && selectedTrack == 1, true, ms.level);
            }

            // Cột Cuối Cùng: RƯƠNG VÔ CỰC (Endless Mystery Box)
            int overBoxX = startX + vecMilestones.size() * cellWidth + 4;
            paintOverlevelBox(g, overBoxX, trackAreaY + 4, selectedIndex == vecMilestones.size());
        }

        mGraphics.restoreCanvas();

        // 4. FOOTER TOOLTIP CHI TIẾT & CÁC NÚT THAO TÁC
        paintFooterInfo(g);

        // 5. POPUP SOI KHO BÁU RƯƠNG VÔ CỰC
        if (isShowPreviewPopup) {
            paintPreviewPopup(g);
        }

        // 6. VẼ NÚT ĐÓNG [X] VÀ CÁC COMMAND
        GameCanvas.resetTrans(g);
        if (super.cmdList != null) {
            for (int c = 0; c < super.cmdList.size(); c++) {
                iCommand cmd = (iCommand) super.cmdList.elementAt(c);
                if (cmd != null) {
                    cmd.paint(g, cmd.xCmd, cmd.yCmd);
                }
            }
        }
    }

    private void paintRewardSlot(mGraphics g, int x, int y, PassItem item, boolean isFocus, boolean isVipTrack, int lv) {
        int w = 42;
        int h = 28;
        int colorIdx = isFocus ? 0 : (isVipTrack ? 1 : 2);
        AvMain.paintRect(g, x, y, w, h, (byte) 1, colorIdx);

        int centerX = x + w / 2;
        int centerY = y + h / 2;

        if (AvMain.imgBorderIcon != null) {
            g.drawRegion((mImage) AvMain.imgBorderIcon, centerX, centerY, 3);
        }

        if (item != null) {
            MainImage img = getItemImage(item.itemType, item.iconId);
            if (img != null && img.img != null) {
                g.drawRegion((mImage) img.img, centerX, centerY, 3);
            }
            mFont.tahoma_7_white.drawString(g, "x" + formatQty(item.quantity), x + w - 2, y + h - 8, 1);

            if (isVipTrack) {
                mFont.tahoma_7_orange.drawString(g, "VIP", x + 3, y + 1, 0);
            }

            if (item.status == 2) { // Đã nhận
                if (AvMain.imgcheck != null) {
                    g.drawRegion((mImage) AvMain.imgcheck, centerX, centerY, 3);
                } else {
                    mFont.tahoma_7b_yellow.drawString(g, "V", centerX, centerY - 4, 2);
                }
            } else if (item.status == 1) { // Sẵn sàng nhận
                if (GameCanvas.gameTick % 8 < 4) {
                    g.setColor(0xFDE047);
                    g.drawRect(x - 1, y - 1, w + 1, h + 1);
                }
            } else { // Khóa
                if ((currentLevel < lv || (isVipTrack && !isVip)) && AvMain.imgLock != null) {
                    g.drawRegion((mImage) AvMain.imgLock, centerX, centerY, 3);
                }
            }
        } else {
            mFont.tahoma_7_white.drawString(g, "---", centerX, centerY - 4, 2);
        }

        if (isFocus) {
            g.setColor(0xFBBF24);
            g.drawRect(x - 1, y - 1, w + 1, h + 1);
        }
    }

    private void paintOverlevelBox(mGraphics g, int x, int y, boolean isFocus) {
        int w = 54;
        int h = 88;
        AvMain.paintRect(g, x, y, w, h, (byte) 0, isFocus ? 0 : 1);

        mFont.tahoma_7b_yellow.drawString(g, "VÔ CỰC", x + w / 2, y + 4, 2);

        MainImage boxImg = getItemImage((byte) 4, (short) 106);
        if (boxImg != null && boxImg.img != null) {
            g.drawRegion((mImage) boxImg.img, x + w / 2, y + 30, 3);
        }

        mFont.tahoma_7b_white.drawString(g, "x" + unclaimedOverBoxes, x + w / 2, y + 48, 2);

        // 2 Nút Soi & Mở
        AvMain.paintRect(g, x + 3, y + 64, 23, 18, (byte) 1, 3);
        mFont.tahoma_7_white.drawString(g, "SOI", x + 14, y + 67, 2);

        AvMain.paintRect(g, x + 28, y + 64, 23, 18, (byte) 1, unclaimedOverBoxes > 0 ? 0 : 4);
        mFont.tahoma_7_white.drawString(g, "MỞ", x + 39, y + 67, 2);
    }

    private void paintFooterInfo(mGraphics g) {
        AvMain.paintRect(g, footX, footY, footW, footH, (byte) 0, 1);

        if (selectedIndex == vecMilestones.size()) {
            // Xem thông tin Rương Vô Cực
            int iconBoxX = footX + 8;
            int iconBoxY = footY + footH / 2 - 16;
            AvMain.paintRect(g, iconBoxX, iconBoxY, 32, 32, (byte) 1, 0);
            MainImage boxImg = getItemImage((byte) 4, (short) 106);
            if (boxImg != null && boxImg.img != null) {
                g.drawRegion((mImage) boxImg.img, iconBoxX + 16, iconBoxY + 16, 3);
            }

            int textX = iconBoxX + 38;
            mFont.tahoma_7b_yellow.drawString(g, "RƯƠNG VÔ CỰC (Lv.101+)", textX, footY + 4, 0);
            mFont.tahoma_7_white.drawString(g, "Mỗi 2.000 EXP vượt cấp nhận 1 rương báu.", textX, footY + 18, 0);
            mFont.tahoma_7_white.drawString(g, "Rương hiện có: " + unclaimedOverBoxes + " | Cấp vượt: " + overLevels, textX, footY + 32, 0);

            // Nút Soi Kho & Nút Mở Rương
            AvMain.paintRect(g, btnBuyLvX, btnBuyLvY, btnBuyLvW, btnBuyLvH, (byte) 1, 3);
            mFont.tahoma_7b_white.drawString(g, "[SOI KHO]", btnBuyLvX + btnBuyLvW / 2, btnBuyLvY + 5, 2);

            AvMain.paintRect(g, btnActionX, btnActionY, btnActionW, btnActionH, (byte) 1, unclaimedOverBoxes > 0 ? 0 : 4);
            mFont.tahoma_7b_white.drawString(g, "[MỞ (" + unclaimedOverBoxes + ")]", btnActionX + btnActionW / 2, btnActionY + 5, 2);
            return;
        }

        if (vecMilestones != null && selectedIndex >= 0 && selectedIndex < vecMilestones.size()) {
            PassMilestone ms = (PassMilestone) vecMilestones.elementAt(selectedIndex);
            PassItem item = (selectedTrack == 0) ? ms.freeItem : ms.vipItem;
            boolean reached = currentLevel >= ms.level;

            // Icon Khung Preview bên trái
            int iconBoxX = footX + 8;
            int iconBoxY = footY + footH / 2 - 16;
            AvMain.paintRect(g, iconBoxX, iconBoxY, 32, 32, (byte) 1, (selectedTrack == 1) ? 1 : 2);

            if (item != null) {
                MainImage img = getItemImage(item.itemType, item.iconId);
                if (img != null && img.img != null) {
                    g.drawRegion((mImage) img.img, iconBoxX + 16, iconBoxY + 16, 3);
                }
            }

            int textX = iconBoxX + 38;
            if (item != null) {
                String trackLabel = (selectedTrack == 0) ? "[FREE Lv." + ms.level + "] " : "[VIP Lv." + ms.level + "] ";
                mFont.tahoma_7b_yellow.drawString(g, trackLabel + item.name + " (x" + formatQty(item.quantity) + ")", textX, footY + 4, 0);
                mFont.tahoma_7_white.drawString(g, item.desc, textX, footY + 18, 0);

                String reqStr = "Yêu cầu: Cấp " + ms.level;
                if (selectedTrack == 1 && !isVip) {
                    reqStr += " (Chưa mở VIP)";
                    mFont.tahoma_7_orange.drawString(g, reqStr, textX, footY + 32, 0);
                } else if (item.status == 2) {
                    reqStr += " (Đã nhận phần thưởng)";
                    mFont.tahoma_7_white.drawString(g, reqStr, textX, footY + 32, 0);
                } else if (reached) {
                    reqStr += " (Sẵn sàng nhận thưởng!)";
                    mFont.tahoma_7b_yellow.drawString(g, reqStr, textX, footY + 32, 0);
                } else {
                    reqStr += " (Chưa đạt cấp yêu cầu)";
                    mFont.tahoma_7_orange.drawString(g, reqStr, textX, footY + 32, 0);
                }
            } else {
                mFont.tahoma_7b_yellow.drawString(g, "[FREE Lv." + ms.level + "] Không có phần thưởng", textX, footY + 4, 0);
                mFont.tahoma_7_white.drawString(g, "Nâng cấp VIP để nhận trọn bộ quà tặng!", textX, footY + 18, 0);
                mFont.tahoma_7_orange.drawString(g, "Yêu cầu: Cấp " + ms.level + (reached ? " (Đã đạt)" : " (Chưa đạt)"), textX, footY + 32, 0);
            }

            // Nút [MUA CẤP]
            AvMain.paintRect(g, btnBuyLvX, btnBuyLvY, btnBuyLvW, btnBuyLvH, (byte) 1, 3);
            mFont.tahoma_7b_white.drawString(g, "[MUA CẤP]", btnBuyLvX + btnBuyLvW / 2, btnBuyLvY + 5, 2);

            // Nút Thao tác theo trạng thái: [NHẬN] / [MỞ VIP] / [ĐÃ NHẬN] / [CHƯA ĐẠT]
            if (selectedTrack == 1 && !isVip) {
                AvMain.paintRect(g, btnActionX, btnActionY, btnActionW, btnActionH, (byte) 1, 0);
                mFont.tahoma_7b_white.drawString(g, "[MỞ VIP]", btnActionX + btnActionW / 2, btnActionY + 5, 2);
            } else if (item != null && item.status == 1) {
                AvMain.paintRect(g, btnActionX, btnActionY, btnActionW, btnActionH, (byte) 1, 0);
                mFont.tahoma_7b_white.drawString(g, "[NHẬN QUÀ]", btnActionX + btnActionW / 2, btnActionY + 5, 2);
            } else if (item != null && item.status == 2) {
                AvMain.paintRect(g, btnActionX, btnActionY, btnActionW, btnActionH, (byte) 1, 4);
                mFont.tahoma_7_white.drawString(g, "[ĐÃ NHẬN]", btnActionX + btnActionW / 2, btnActionY + 5, 2);
            } else {
                AvMain.paintRect(g, btnActionX, btnActionY, btnActionW, btnActionH, (byte) 1, 4);
                mFont.tahoma_7_white.drawString(g, "[CHƯA ĐẠT]", btnActionX + btnActionW / 2, btnActionY + 5, 2);
            }
        }
    }

    private void paintPreviewPopup(mGraphics g) {
        int popW = Math.min(280, MotherCanvas.w - 20);
        int popH = Math.min(180, MotherCanvas.h - 20);
        int popX = MotherCanvas.w / 2 - popW / 2;
        int popY = MotherCanvas.h / 2 - popH / 2;

        // Dim background
        g.setColor(0xDD000000);
        g.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);

        AvMain.AB(g, popX, popY, popW, popH, 1);
        AvMain.FontBorderColor(g, "[KHO BÁU RƯƠNG VÔ CỰC]", popX + popW / 2, popY - 12, 2, 0, 8);

        if (vecPreview != null) {
            for (int i = 0; i < vecPreview.size(); i++) {
                if (i >= 6) break; // Hiển thị tối đa 6 vật phẩm đại diện
                PreviewItem it = (PreviewItem) vecPreview.elementAt(i);
                int itemY = popY + 22 + i * 22;
                AvMain.paintRect(g, popX + 10, itemY, popW - 20, 20, (byte) 1, 4);

                MainImage img = getItemImage(it.itemType, it.iconId);
                if (img != null && img.img != null) {
                    g.drawRegion((mImage) img.img, popX + 22, itemY + 10, 3);
                }
                mFont.tahoma_7_white.drawString(g, it.name + " (" + it.qtyDisplay + ")", popX + 36, itemY + 4, 0);
                mFont.tahoma_7b_yellow.drawString(g, it.rateText, popX + popW - 16, itemY + 4, 1);
            }
        }

        mFont.tahoma_7_white.drawString(g, "-- Chạm hoặc phím Đóng để tắt --", popX + popW / 2, popY + popH - 12, 2);
    }

    private PassItem getSelectedItem() {
        if (vecMilestones != null && selectedIndex >= 0 && selectedIndex < vecMilestones.size()) {
            PassMilestone ms = (PassMilestone) vecMilestones.elementAt(selectedIndex);
            return (selectedTrack == 0) ? ms.freeItem : ms.vipItem;
        }
        return null;
    }
}
