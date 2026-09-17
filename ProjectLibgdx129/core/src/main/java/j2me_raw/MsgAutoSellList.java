public final class MsgAutoSellList extends MsgDialog {
    private int hItem = 24, wItem = 120, wDia, hDia, AX, AY;
    private int scroll, maxScroll;
    private iCommand cmdOK, cmdClear;

    public void init() {
        super.fontDia = mFont.tahoma_7b_black;
        this.beginDia();
        super.cmdList = new mVector();

        cmdOK = new iCommand("Đóng", 1, this);
        cmdClear = new iCommand("Xóa toàn bộ", 2, this);

        super.cmdList.addElement(cmdOK);
        super.cmdList.addElement(cmdClear);

        // Tính toán kích thước dialog
        super.wDia = MotherCanvas.w - 20;
        if (super.wDia > 280) {
            super.wDia = 280;
        }
        
        // Tính toán chiều cao dựa trên số lượng item đã chọn
        int itemCount = AThMadaraMOD.autoSellList.size();
        int maxVisibleItems = 8; // Số item hiển thị tối đa
        int actualItems = Math.min(itemCount, maxVisibleItems);
        
        super.hDia = 80 + (actualItems * (hItem + 8)) + 80; // Header + items + buttons
        super.AX = MotherCanvas.hw - super.wDia / 2;
        super.AY = MotherCanvas.hh - super.hDia / 2;
        
        super.wItem = wItem;
        this.setPosCmdNew(-2, false);
        
        // Khởi tạo scroll
        scroll = 0;
        maxScroll = 0;
    }

    public void commandPointer(int index, int subIndex) {
        switch (index) {
            case 1: super.isClose = true; GameCanvas.subDialog = null; break;
            case 2: AThMadaraMOD.clearAutoSellList(); break;
        }
    }

    public void paint(mGraphics g) {
        GameCanvas.resetTrans(g);
        
        // Vẽ background dialog theo chuẩn MsgDialog
        this.AD(g, MotherCanvas.hw - super.AS / 2, super.AY, super.AS, super.hDia, 0);
        
        // Vẽ overlay để làm mờ background
        g.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
        mGraphics.AC();
        mGraphics.AD();

        int x = super.AX + 10, y = super.AY + 10;
        
        // Tiêu đề với background
        g.setColor(-805042);
        g.fillRoundRectNew(super.AX + 10, y, super.wDia - 20, 16, 4, 4);
        AvMain.FontBorderColor(g, "Danh sách đã chọn", super.AX + super.wDia / 2, y + 8, 2, 6, 5);
        y += 25;

        // Vẽ danh sách item đã chọn
        int visibleCount = 0;
        int maxVisible = 8; // Số item hiển thị tối đa
        int itemSpacing = hItem + 8; // Khoảng cách giữa các item

        for (int i = 0; i < AThMadaraMOD.autoSellList.size(); ++i) {
            short[] s = (short[]) AThMadaraMOD.autoSellList.elementAt(i);
            if (s == null) continue;
            
            if (visibleCount++ < scroll) continue;
            if (visibleCount - scroll > maxVisible) break;

            int ly = y + (visibleCount - scroll - 1) * itemSpacing;
            
            // Vẽ background cho item (màu xanh nhạt)
            g.setColor(0xFFCCFFCC); // Màu xanh nhạt
            g.fillRoundRectNew(x + 2, ly + 2, super.wDia - 24, itemSpacing - 4, 2, 2);
            
            // Tìm item trong inventory
            MainItem mi = MainItem.getItemVec((byte) s[0], s[1], Player.vecInventory);
            if (mi == null) {
                // Item không còn trong inventory, hiển thị với tên "Unknown"
                mFont.tahoma_7_red.drawString(g, "Unknown", x + 30, ly + 8, 0);
                continue;
            }
            
            // Vẽ icon item (vị trí chuẩn)
            mi.AA(g, x + 20, ly + 8, 3);
            
            // Vẽ tên item (cắt ngắn nếu quá dài)
            String itemName = mi.name;
            if (itemName.length() > 15) {
                itemName = itemName.substring(0, 12) + "...";
            }
            
            // Màu tên item: đen cho dễ nhìn
            mFont.tahoma_7b_black.drawString(g, itemName, x + 50, ly + 8, 0);

            // Vẽ số lượng trên dòng riêng nếu có
            if (mi.numPotion > 0) {
                // Màu số lượng: đỏ cho dễ nhìn, xuống dòng
                mFont.tahoma_7b_red.drawString(
                    g, "x" + mi.numPotion,
                    x + 50, ly + 20, 0
                );
            }
        }

        // Hint text
        mFont.tahoma_7b_brown.drawString(
            g, "[Chạm] để xóa item khỏi danh sách",
            super.AX + 10, super.AY + super.hDia - 25, 0
        );

        // Vẽ các nút command
        for (int i = 0; i < super.cmdList.size(); ++i) {
            iCommand cmd = (iCommand) super.cmdList.elementAt(i);
            cmd.paint(g, cmd.xCmd, cmd.yCmd);
        }
        
        mGraphics.restoreCanvas();
        GameCanvas.resetTrans(g);
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
            // Tính toán maxScroll
            int totalItems = AThMadaraMOD.autoSellList.size();
            maxScroll = Math.max(0, totalItems - 8); // 8 là số item hiển thị tối đa
            
            // Handle scroll with arrow keys
            if (GameCanvas.isKeyPressed(1)) { // UP key
                if (scroll > 0) scroll--;
                GameCanvas.clearKeyPressed(1);
            }
            if (GameCanvas.isKeyPressed(3)) { // DOWN key
                if (scroll < maxScroll) scroll++;
                GameCanvas.clearKeyPressed(3);
            }
            
            // Call parent method
            super.handleKeyPress();
        }
    }

    public void updatePointer() {
        if (!this.isClose) {
            // Gọi parent method để xử lý các nút command
            super.updatePointer();
            
            if (GameCanvas.isTouch) {
                // Xử lý click vào danh sách item để xóa
                int visibleCount = 0;
                int maxVisible = 8;
                int startY = super.AY + 35; // Vị trí bắt đầu của danh sách item
                int itemSpacing = hItem + 8;
                
                for (int i = 0; i < AThMadaraMOD.autoSellList.size(); ++i) {
                    if (visibleCount++ < scroll) continue;
                    if (visibleCount - scroll > maxVisible) break;
                    
                    int rowIndex = visibleCount - scroll - 1;
                    int itemY = startY + rowIndex * itemSpacing;
                    int itemX = super.AX + 10;
                    int itemW = super.wDia - 20;
                    int itemH = itemSpacing;
                    
                    // Kiểm tra click vào item này
                    if (GameCanvas.AB(itemX, itemY, itemW, itemH)) {
                        // Xóa item khỏi danh sách auto sell
                        AThMadaraMOD.removeAutoSellAt(i);
                        GameCanvas.isPointerSelect = false;
                        break;
                    }
                }
            }
        }
    }
}
