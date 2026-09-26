public final class MsgAutoSell extends MsgDialog {
    private int hItem = 24, wItem = 120, wDia, hDia, AX, AY;
    private int scroll, filter, maxScroll;
    private iCommand cmdOK, cmdClear, cmdViewList, cmdCleanCache, cmdOnOffAuto;

    // Switch (toggle) for auto drop
    private final int switchW = 64;
    private final int switchH = 18;
    private int switchX;
    private int switchY;
    private int switchPadding = 8;

    // Biến cho touch scrolling
    private int lastTouchY = -1;
    private int touchStartY = -1;
    private boolean isDragging = false;

    public void init() {
        super.fontDia = mFont.tahoma_7b_black;
        this.beginDia();
        super.cmdList = new mVector();

        cmdOK = new iCommand("Đóng", 1, this);
        cmdClear = new iCommand("Xóa toàn bộ", 2, this);
        cmdViewList = new iCommand("Danh sách đã chọn", 3, this);
        cmdCleanCache = new iCommand("Làm sạch cache", 4, this);
        cmdOnOffAuto = new iCommand(!Player.isAutoFilterItems ? "Bật tự động lọc đồ" : " Tắt tự động lọc đồ", 5, this);
        super.cmdList.addElement(cmdOnOffAuto);
        super.cmdList.addElement(cmdOK);
        super.cmdList.addElement(cmdViewList);
        super.cmdList.addElement(cmdCleanCache);
        super.cmdList.addElement(cmdClear);
        AThMadaraMOD.cleanAutoSellCache();
        super.wDia = MotherCanvas.w - 20;
        if (super.wDia > 320) super.wDia = 320; // cho chút rộng hơn nếu cần

        // Tính toán chiều cao dựa trên số lượng item (giữ gọn, không lòe)
        int itemCount = Player.vecInventory != null ? Player.vecInventory.size() : 0;
        int maxVisibleItems = 6; // số item hiển thị
        int actualItems = Math.min(itemCount, maxVisibleItems);

        super.hDia = 84 + (actualItems * (hItem + 8)) + 64; // layout cân đối
        super.AX = MotherCanvas.hw - super.wDia / 2;
        super.AY = MotherCanvas.hh - super.hDia / 2;

        super.wItem = wItem;
        this.setPosCmdNew(-2, false);

        // Khởi tạo scroll
        scroll = 0;
        filter = 0;
        maxScroll = 0;

        // Khởi tạo vị trí switch (sắp xếp chuẩn)
        switchX = super.AX + super.wDia - switchPadding - switchW;
        switchY = super.AY + 28; // đặt dưới title một chút
    }

    public void commandPointer(int index, int subIndex) {
        switch (index) {
            case 1:
                super.isClose = true;
                GameCanvas.subDialog = null;
                break;
            case 2:
                AThMadaraMOD.clearAutoSellList();
                break;
            case 3:
                showList();
                break;
            case 4:
                AThMadaraMOD.cleanAutoSellCache();
                break;
            case 5:
                toggleAutoDrop();
                break;
        }
    }

    private void showList() {
        if (AThMadaraMOD.autoSellList.isEmpty()) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Danh sách trống");
            return;
        }
        // Tạo custom dialog để hiển thị icon
        MsgAutoSellList dlg = new MsgAutoSellList();
        dlg.init();
        GameCanvas.subDialog = dlg;
    }

    public void commandPointerMenu(int index, int subIndex) {
        if (index >= 200) {
            AThMadaraMOD.removeAutoSellAt(index - 200);
            GameCanvas.end_Dialog();
        } else if (index == 1) {
            GameCanvas.end_Dialog();
        }
    }

    public void paint(mGraphics g) {
        GameCanvas.resetTrans(g);

        // Vẽ background dialog (đơn giản, không lòe)
        this.AD(g, MotherCanvas.hw - super.AS / 2, super.AY, super.AS, super.hDia, 0);

        // Overlay mờ nhẹ
        g.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
        mGraphics.AC();
        mGraphics.AD();

        int x = super.AX + 10, y = super.AY + 10;

        // Tiêu đề (đơn giản)
        g.setColor(0xFFDCDCDC); // very light gray background for title
        g.fillRoundRectNew(super.AX + 10, y, super.wDia - 20, 18, 4, 4);
        AvMain.FontBorderColor(g, "Auto Sell - Chọn Item", super.AX + super.wDia / 2, y + 9, 2, 0x606060, 0);
        y += 26;

        // Vẽ nút toggle nhỏ ở góc phải (gọn, rõ)
        drawAutoDropSwitch(g);

        // Filter display (gọn)
        String ftext = filter == 0 ? "Tất cả" : filter == 1 ? "Trắng" : "Trắng+Xanh";
        mFont.tahoma_7_white.drawString(g, "Lọc: " + ftext, x, y, 0);
        y += 18;

        // Vẽ danh sách item
        mVector inv = Player.vecInventory != null ? Player.vecInventory : new mVector();
        int visibleCount = 0;
        int maxVisible = 6; // số item hiển thị tối đa
        int itemSpacing = hItem + 8; // khoảng cách

        for (int i = 0; i < inv.size(); ++i) {
            MainItem mi = (MainItem) inv.elementAt(i);
            if (mi == null || !passesFilter(mi)) continue;

            if (visibleCount++ < scroll) continue;
            if (visibleCount - scroll > maxVisible) break;

            int ly = y + (visibleCount - scroll - 1) * itemSpacing;
            int itemX = x;
            int itemW = super.wDia - 24;
            int itemH = itemSpacing - 4;

            // Background nhẹ cho dòng (alternate subtle)
            if ((visibleCount - scroll) % 2 == 0) {
                g.setColor(0xFFF8F8F8);
            } else {
                g.setColor(0xFFFFFFFF);
            }
            g.fillRoundRectNew(itemX + 2, ly + 2, itemW, itemH, 2, 2);

            // Checkbox nhỏ
            boolean isInAutoSell = AThMadaraMOD.isInAutoSell(mi);
            AvMain.fraCheck.drawFrame(isInAutoSell ? 2 : 0, itemX + 6, ly + 6, 0, 3, g);

            // Icon item
            mi.paint(g, itemX + 28, ly + 6, 3);

            // Tên item (cắt gọn)
            String itemName = mi.name;
            if (itemName.length() > 18) itemName = itemName.substring(0, 15) + "...";

            // Tên đen cho readability
            mFont.tahoma_7b_black.drawString(g, itemName, itemX + 54, ly + 8, 0);

            // Số lượng ở hàng dưới nếu có
            if (mi.numPotion > 0) {
                mFont.tahoma_7b_brown.drawString(g, "x" + mi.numPotion, itemX + 54, ly + 20, 0);
            }
        }

        // Hint gọn ở đáy dialog
        mFont.tahoma_7b_brown.drawString(g, "[Chạm] chọn/bỏ chọn  [↑↓] cuộn  [A] lọc", super.AX + 10, super.AY + super.hDia - 30, 0);

        // Vẽ các nút command (theo layout mặc định)
        for (int i = 0; i < super.cmdList.size(); ++i) {
            iCommand cmd = (iCommand) super.cmdList.elementAt(i);
            cmd.paint(g, cmd.xCmd, cmd.yCmd);
        }

        mGraphics.restoreCanvas();
        GameCanvas.resetTrans(g);
    }

    private void drawAutoDropSwitch(mGraphics g) {
        // Vị trí chuẩn (cập nhật mỗi lần paint để tránh lệch)
        switchX = super.AX + super.wDia - switchPadding - switchW;
        switchY = super.AY + 28;

        // Khung switch nhẹ
        g.setColor(0xFFBBBBBB);
        g.fillRoundRectNew(switchX, switchY, switchW, switchH, 6, 6);

        // Nội dung: "Auto" label + trạng thái ON/OFF
        int labelX = switchX - 56;
        int labelY = switchY + (switchH / 2) - 6;
        mFont.tahoma_7_black.drawString(g, "AutoDrop", labelX, labelY + 6, 0);

        // Nút tròn (knob)
        boolean isOn = Player.isAutoFilterItems;
        int knobSize = switchH - 4;
        int knobY = switchY + 2;
        int knobX = isOn ? (switchX + switchW - 2 - knobSize) : (switchX + 2);

        // Fill background for on/off subtle
        if (isOn) {
            g.setColor(0xFFDBEFD9); // nhẹ xanh nhạt
            g.fillRoundRectNew(switchX + 2, switchY + 2, switchW - 4, switchH - 4, 6, 6);
        } else {
            g.setColor(0xFFF2F2F2); // nhẹ xám
            g.fillRoundRectNew(switchX + 2, switchY + 2, switchW - 4, switchH - 4, 6, 6);
        }

        // knob border + fill (trắng)
        g.setColor(0xFFFFFFFF);
        g.fillRoundRectNew(knobX, knobY, knobSize, knobSize, knobSize / 2, knobSize / 2);
        g.setColor(0xFFAAAAAA);
        g.drawRoundRect(knobX, knobY, knobSize, knobSize, knobSize / 2, knobSize / 2);

        // Trạng thái text (ON/OFF) nhỏ nằm bên trong khung (giữa)
        String st = isOn ? "ON" : "OFF";
        mFont.tahoma_7_black.drawString(g, st, switchX + switchW / 2, switchY + switchH / 2 + 6, 2);
    }

    private boolean passesFilter(MainItem mi) {
        return filter == 0 || (filter == 1 && mi.colorName == 0) || (filter == 2 && (mi.colorName == 0 || mi.colorName == 1));
    }

    public void update() {
        this.updateDialog();
        if (super.isClose) this.closeDialog();
        else {
            this.updateAnimation();
            if (GameCanvas.isKeyPressed()) this.handleKeyPress();
            this.updatePointer();
        }
    }

    public void handleKeyPress() {
        if (!this.isClose) {
            // Tính toán maxScroll dựa trên số item thực tế
            mVector inv = Player.vecInventory != null ? Player.vecInventory : new mVector();
            int totalItems = 0;
            for (int i = 0; i < inv.size(); ++i) {
                MainItem mi = (MainItem) inv.elementAt(i);
                if (mi != null && passesFilter(mi)) {
                    totalItems++;
                }
            }
            maxScroll = Math.max(0, totalItems - 6); // 6 là số item hiển thị tối đa

            // Handle scroll with arrow keys
            if (GameCanvas.isKeyPressed(1)) { // UP key
                if (scroll > 0) scroll--;
                GameCanvas.clearKeyPressed(1);
            }
            if (GameCanvas.isKeyPressed(3)) { // DOWN key
                if (scroll < maxScroll) scroll++;
                GameCanvas.clearKeyPressed(3);
            }

            // Handle filter toggle with FIRE key
            if (GameCanvas.isKeyPressed(5)) { // FIRE key
                filter = (filter + 1) % 3;
                scroll = 0; // Reset scroll when filter changes
                GameCanvas.clearKeyPressed(5);
            }

            // Bind a keyboard toggle (optional): key 8 example
            if (GameCanvas.isKeyPressed(8)) {
                toggleAutoDrop();
                GameCanvas.clearKeyPressed(8);
            }

            // Call parent method
            super.handleKeyPress();
        }
    }

    public void updatePointer() {
        if (!this.isClose) {
            // Gọi parent method để xử lý các nút command
            super.updatePointer();

            // Touch area: switch toggle
            if (GameCanvas.isTouch) {
                // cập nhật lại vị trí switch (phòng trường hợp dialog di chuyển)
                switchX = super.AX + super.wDia - switchPadding - switchW;
                switchY = super.AY + 28;

                if (GameCanvas.AB(switchX, switchY, switchW, switchH)) {
                    // Toggle trạng thái
                    toggleAutoDrop();
                    GameCanvas.isPointerSelect = false;
                    return;
                }

                // Xử lý click vào danh sách item với hỗ trợ scroll
                mVector inv = Player.vecInventory != null ? Player.vecInventory : new mVector();
                int visibleCount = 0;
                int maxVisible = 6;
                int startY = super.AY + 54; // vị trí bắt đầu của danh sách item (căn chuẩn)
                int itemSpacing = hItem + 8;

                for (int i = 0; i < inv.size(); ++i) {
                    MainItem mi = (MainItem) inv.elementAt(i);
                    if (mi == null || !passesFilter(mi)) continue;

                    if (visibleCount++ < scroll) continue;
                    if (visibleCount - scroll > maxVisible) break;

                    int rowIndex = visibleCount - scroll - 1;
                    int itemY = startY + rowIndex * itemSpacing;
                    int itemX = super.AX + 10;
                    int itemW = super.wDia - 20;
                    int itemH = itemSpacing;

                    // Kiểm tra click vào item này
                    if (GameCanvas.AB(itemX, itemY, itemW, itemH)) {
                        // Toggle auto sell cho item này
                        AThMadaraMOD.toggleAutoSell(mi);
                        GameCanvas.isPointerSelect = false;
                        break;
                    }
                }
            }
        }
    }

    private void toggleAutoDrop() {
        Player.isAutoFilterItems = !Player.isAutoFilterItems;
        String txt = Player.isAutoFilterItems ? "Bật AutoDrop" : "Tắt AutoDrop";
        Interface_Game.addInfoPlayerNormal(txt, mFont.tahoma_7_yellow);
        AThMadaraMOD.cleanAutoSellCache();
    }
    
    
}
