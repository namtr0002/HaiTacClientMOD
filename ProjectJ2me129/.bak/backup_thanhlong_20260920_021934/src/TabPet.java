public final class TabPet extends MainTabShop {
   public iCommand cmdUsePotion;
   public iCommand cmdDonotUse;

   public TabPet(String name, mVector vec, int xbegin) {
      super(name, vec, Player.maxInventory, xbegin);
      super.indexIconTab = 5;
   }

   public final void initCmd() {
      this.cmdUsePotion = new iCommand(T.cmdUse, 34, this);
      this.cmdDonotUse = new iCommand(T.thaora, 35, this);
      super.cmdMenu = new iCommand(T.AS, 10, this);
   }

   public final void AB() {
      super.AQ = MainTab.AE * MainTabShop.BW;
      super.AO = MainTab.xTab + MainTab.AG / 2 - super.AQ / 2 + 10;
      super.AP = MainTab.AI + 32;
      super.AR = MainTab.AH - 32;
      super.AD = Player.maxInventory;
      if (super.vecShop != null && super.vecShop.size() > super.AD) {
         super.AD = super.vecShop.size();
      }
      int var1 = ((super.AD - 1) / MainTabShop.BW + 1) * MainTab.AE - super.AR + super.AS;
      super.BY = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      super.BZ.setInfo(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);

      super.AB();
      if (super.vecShop == null || super.vecShop.size() == 0) {
         GlobalService.getInstance().Send_Pet((byte)3);
      }
      this.AB(this.getMenuActionItem());
   }

   public final void setData(mVector vec) {
      super.vecShop = vec;
      Player.vecPet = vec;
      super.AD = Player.maxInventory;
      if (super.vecShop != null && super.vecShop.size() > super.AD) {
         super.AD = super.vecShop.size();
      }
      super.AQ = MainTab.AE * MainTabShop.BW;
      super.AO = MainTab.xTab + MainTab.AG / 2 - super.AQ / 2 + 10;
      super.AP = MainTab.AI + 32;
      super.AR = MainTab.AH - 32;
      int var1 = ((super.AD - 1) / MainTabShop.BW + 1) * MainTab.AE - super.AR + super.AS;
      super.BY = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      super.BZ.setInfo(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);

      if (super.IdSelect >= 0 && super.IdSelect < super.vecShop.size()) {
         super.itemCur = (MainItem)super.vecShop.elementAt(super.IdSelect);
      } else {
         super.itemCur = null;
      }
      this.AB(this.getMenuActionItem());
   }

   public final mVector getMenuActionItem() {
      mVector var1 = null;
      if (super.IdSelect >= 0 && super.IdSelect < super.vecShop.size()) {
         super.itemCur = (MainItem)super.vecShop.elementAt(super.IdSelect);
      } else {
         super.itemCur = null;
      }
      if (super.itemCur != null) {
         var1 = new mVector();
         if (super.itemCur.colorName == 1) {
            var1.addElement(this.cmdDonotUse);
         } else {
            var1.addElement(this.cmdUsePotion);
         }
      }
      return var1;
   }

   public final void commandPointer(int var1, int var2) {
      switch (var1) {
         case 10:
            mVector var3;
            if ((var3 = this.getMenuActionItem()) != null) {
               GameCanvas.menu.startAt(var3, 2, T.AU);
            }
            break;
         case 34:
            if (super.IdSelect >= 0 && super.IdSelect < super.vecShop.size()) {
               MainItem var4 = (MainItem)super.vecShop.elementAt(super.IdSelect);
               GlobalService.getInstance().Send_Pet((byte)4, (byte)1, var4.ID);
            }
            break;
         case 35:
            if (super.IdSelect >= 0 && super.IdSelect < super.vecShop.size()) {
               MainItem var5 = (MainItem)super.vecShop.elementAt(super.IdSelect);
               GlobalService.getInstance().Send_Pet((byte)4, (byte)0, var5.ID);
            }
            break;
         default:
            super.commandPointer(var1, var2);
            break;
      }
   }

   public final void Use(short var1) {
      for (int var2 = 0; var2 < super.vecShop.size(); ++var2) {
         MainItem var3 = (MainItem)super.vecShop.elementAt(var2);
         if (var3.ID == var1) {
            super.IdSelect = var2;
            var3.colorName = 1;
         } else {
            var3.colorName = 0;
         }
      }
      super.AV = false;
      this.AB(this.getMenuActionItem());
   }
}
