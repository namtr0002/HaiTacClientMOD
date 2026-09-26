public final class TabChest extends MainTabShop {
   public static iCommand cmdRemove;
   public static iCommand cmdGetPotion;
   private static iCommand cmdGetAllMaterial;
   public static iCommand cmdUpgrade;
   public static iCommand cmdChucnang;
   private static iCommand cmdGetAllDiamond;

   public TabChest(String name, mVector vec, int xTab) {
      super(name, vec, Player.maxChest, xTab);
      super.indexIconTab = 7;
      cmdUpgrade = new iCommand(T.morong, 8, this);
   }

   public final void AQ() {
      new iCommand(T.del, 2, this);
      cmdRemove = new iCommand(T.cmdget, 4, this);
      cmdGetPotion = new iCommand(T.cmdget, 11, this);
      cmdGetAllMaterial = new iCommand(T.laytatca, 7, this);
      cmdChucnang = new iCommand(T.CO, 9, this);
      super.cmdMenu = new iCommand(T.AS, 10, this);
      cmdGetAllDiamond = new iCommand(T.KC, 13, this);
   }

   public final void commandPointer(int index, int subIndex) {
      mVector vec;
      switch(index) {
      case 2:
         if (super.itemCur == null) {
            return;
         }

         GameCanvas.Start_Normal_DiaLog(GameMidlet.Start_Normal_DiaLog(T.HoiRemoveENG, 
                 new String[]{"" + super.itemCur.numPotion, super.itemCur.name}), 
                 new iCommand(T.remove, 3, 1, this), 
                 true);
         return;
      case 3:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().Chest((byte)4, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
         return;
      case 4:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().Chest((byte)2, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
         return;
      case 5:
         GameCanvas.end_Dialog();
         if (super.itemCur == null) {
            return;
         }

         if (super.itemCur.numPotion == 1) {
            GlobalService.getInstance().Chest((byte)2, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
         } else {
            super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.cmdget, 6, 0, this), true, super.itemCur.namepaint);
         }

         GameCanvas.subDialog = super.input;
         return;
      case 6:
         if (super.itemCur == null) {
            return;
         }

         boolean var5 = false;

         try {
            if ((index = Integer.parseInt(super.input.tfInput.getText())) < 0) {
               index = 1;
            }
         } catch (Exception var3) {
            index = 1;
         }

         MainItem itemVec;
         if ((itemVec = MainItem.getItemVec(super.itemCur.typeObject, super.itemCur.ID, Player.vecChest)) != null) {
            if (index > itemVec.numPotion) {
               index = itemVec.numPotion;
            }

            GameCanvas.end_Dialog();
            GlobalService.getInstance().Chest((byte)2, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)index);
            return;
         }
         break;
      case 7:
         Player.SetMaterialToInven((byte)7);
         return;
      case 8:
         GlobalService.getInstance().Chest((byte)5, (short)0, (byte)0, (int)0);
         return;
      case 9:
         (vec = new mVector()).addElement(cmdUpgrade);
         vec.addElement(cmdGetAllMaterial);
         vec.addElement(cmdGetAllDiamond);
         GameCanvas.menu.startAt(vec, 2, T.AU);
         return;
      case 10:
         if ((vec = this.getMenuActionItem()) != null) {
            GameCanvas.menu.startAt(vec, 2, T.AU);
            return;
         }
         break;
      case 11:
         if (super.itemCur == null) {
            return;
         }

         if (super.itemCur.numPotion == 1) {
            GlobalService.getInstance().Chest((byte)2, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
            return;
         }

         vec = new mVector();
         iCommand o = new iCommand(T.allMaterial, 12, 0, this);
         vec.addElement(o);
         o = new iCommand(T.soluong, 5, 0, this);
         vec.addElement(o);
         GameCanvas.Start_Normal_DiaLog_New(T.muonlayrabaonhieu + super.itemCur.namepaint, vec, true, super.itemCur.name);
         return;
      case 12:
         if (super.itemCur == null) {
            return;
         }

         if (subIndex == 0) {
            GlobalService.getInstance().Chest((byte)2, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)super.itemCur.numPotion);
         }

         GameCanvas.end_Dialog();
         return;
      case 13:
         Player.SetDiamondToInven();
      }

   }

   public final mVector getMenuActionItem() {
      mVector result = null;
      MainItem mainItem;
      if ((mainItem = (MainItem)Player.vecChest.elementAt(super.IdSelect)) != null) {
         super.itemCur = mainItem;
         result = super.itemCur.getActionChest();
      }

      return result;
   }

   public final iCommand setCmdEndInfo() {
      return cmdUpgrade;
   }
}
