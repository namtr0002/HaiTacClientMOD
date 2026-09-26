public class TabAutoSellList extends MainTabShop {
    public TabAutoSellList(String name, mVector items, int xTab) {
        super(name, items, 126, xTab); // Max 126 slots!
        super.indexIconTab = 2; // Renders with settings/skill icon
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
            menu.addElement(new iCommand("Xóa", 1, this));
        } else {
            super.AV = false;
        }
        menu.addElement(new iCommand(!Player.isAutoFilterItems ? "Bật Tự Động" : "Tắt Tự Động", 3, this));
        return menu;
    }

    public void commandPointer(int index, int subIndex) {
        switch (index) {
            case 1: // Xóa Bán
                if (this.itemCur != null) {
                    AThMadaraMOD.toggleAutoSell(this.itemCur);
                    this.AB(this.getMenuActionItem());
                }
                break;
            case 3: // Bật/Tắt Tự Động
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
}
