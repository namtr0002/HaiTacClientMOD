public class TabAutoSellBag extends MainTabShop {
    public TabAutoSellBag(String name, mVector items, int xTab) {
        super(name, items, Player.maxInventory, xTab);
        super.indexIconTab = 0; // Icon Túi Đồ
    }

    public void initCmd() {
        super.cmdMenu = new iCommand(T.AS, 10, this);
        this.AB(this.getMenuActionItem());
    }

    public mVector getMenuActionItem() {
        mVector menu = new mVector();
        if (super.IdSelect >= 0 && super.IdSelect < this.vecShop.size()) {
            this.itemCur = (MainItem) this.vecShop.elementAt(super.IdSelect);
        } else {
            this.itemCur = null;
        }

        if (this.itemCur != null) {
            super.vecInfoSS = MainItem.AA(this.itemCur);
            super.AV = true;
            this.AG();
            if (AThMadaraMOD.isInAutoSell(this.itemCur)) {
                menu.addElement(new iCommand("Xóa", 1, this));
            } else {
                menu.addElement(new iCommand("Thêm", 2, this));
            }
        } else {
            super.AV = false;
        }
        menu.addElement(new iCommand(!Player.isAutoFilterItems ? "Bật Tự Động" : "Tắt Tự Động", 3, this));
        return menu;
    }

    public void commandPointer(int index, int subIndex) {
        switch (index) {
            case 1:
            case 2:
                if (this.itemCur != null) {
                    AThMadaraMOD.toggleAutoSell(this.itemCur);
                    this.AB(this.getMenuActionItem());
                }
                break;
            case 3:
                Player.isAutoFilterItems = !Player.isAutoFilterItems;
                Interface_Game.addInfoPlayerNormal("Đã " + (Player.isAutoFilterItems ? "Bật" : "Tắt") + " tự động dọn rác", mFont.tahoma_7_yellow);
                this.AB(this.getMenuActionItem());
                break;
            case 10:
                mVector menuActionItem;
                if ((menuActionItem = this.getMenuActionItem()) != null) {
                    GameCanvas.menu.startAt(menuActionItem, 2, T.AU);
                }
                break;
        }
    }

    // Ghi đè phương thức AA của MainTabShop để vẽ viền đỏ cho vật phẩm được chọn tự động bán
    public void AA(mGraphics var1, MainItem var2, int var3, int var4) {
        if (var2 != null && AThMadaraMOD.isInAutoSell(var2)) {
            int px = var3 - MainTab.AE + 1;
            int py = var4 - MainTab.AE + 1;
            
            // Vẽ viền đỏ
            var1.setColor(0xFFD50000); // Red
            var1.drawRect(px + 1, py + 1, MainTab.AE - 2, MainTab.AE - 2);
            var1.drawRect(px + 2, py + 2, MainTab.AE - 4, MainTab.AE - 4);
        }
    }
}
