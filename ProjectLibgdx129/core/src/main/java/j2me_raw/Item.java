public final class Item extends MainItem {
   public static mVector vecItemKichAnCheckInfo = new mVector("Item.vecItemKichAnCheckInfo");
   private MainInfoItem[] CP = null;

   public Item(byte var1, short var2, short var3, String var4, byte var5) {
      super((byte)3, var2, var3, var4, var5);
      super.indexSort = 3;
   }

   public Item(byte var1, short var2, short var3, String var4, short var5, short var6, short var7) {
      super(var1, var2, var3, var4, (byte)0);
      super.timeUse = var5;
      if (var5 > 0) {
         this.setTimeMarket(var5 * 60);
      }

      super.maxTimeUse = var6;
      super.AJ = var7;
   }

   public final void AA(short var1, byte var2, byte var3, int var4, byte var5, byte var6, byte var7, short[] var8, short var9, byte var10, byte var11) {
      super.Lv_RQ = var1;
      super.BP = var2;
      super.colorName = var3;
      super.timeUse = var4;
      super.BR = var5;
      super.LvUpgrade = var6;
      if (super.LvUpgrade > 0) {
         super.namepaint = super.name + " +" + super.LvUpgrade;
      } else {
         super.namepaint = super.name;
      }

      super.AX = var7;
      super.BH = var8;
      super.AQ = var9;
      super.BD = var10;
      super.BE = var11;
      if (super.idIcon == 242 && var3 == 0) {
         super.colorName = (byte)(super.LvUpgrade / 10);
         if (super.colorName > 9) {
            super.colorName = 9;
         }

         super.BF = 1;
      }

   }

   public final void AA(MainInfoItem[] var1, int var2) {
      super.vecInfo.removeAllElements();
      if (super.BE >= 0) {
         String var3 = MainItem.getDataKichAn(this);
         String[] var5 = mFont.tahoma_7_black.splitFontArray(var3, super.BS);

         for(int var4 = 0; var4 < var5.length; ++var4) {
            this.addInfo((String)var5[var4], (byte)2);
         }
      }

      this.AA((String)T.SZ, (byte)5, (byte)Class_FB.AG);
      if (super.BR != 10 && super.BF != 1) {
         this.AA((String)(T.gohome + ": " + super.Lv_RQ), (byte)(super.Lv_RQ <= GameScreen.player.Lv ? 4 : 3), (byte)Class_FB.AF);
      }

      this.addInfo((String)(T.RP + super.AQ), (byte)0);
      if (super.BO == 2) {
         this.addInfo((String)T.LY, (byte)3);
      }

      int var6;
      if (super.LvUpgrade >= 11 && super.LvUpgrade <= 15 && super.BF != 1) {
         this.AA((String)T.TA, (byte)5, (byte)Class_FB.AG);
         this.AA((String)(T.ST + super.LvUpgrade), (byte)7, (byte)((byte)(90 + super.LvUpgrade)));

         for(var6 = 0; var6 < super.LvUpgrade - 10; ++var6) {
            byte var7;
            if ((var7 = (byte)(var6 + 101)) > 105) {
               var7 = 105;
            }

            this.AA((String)T.WX[var6], (byte)7, (byte)var7);
            if (var6 == 4) {
               this.AA((String)T.WX[var6 + 1], (byte)7, (byte)var7);
            }
         }
      }

      this.AA((String)T.TB, (byte)5, (byte)Class_FB.AH);

      for(var6 = 0; var6 < var1.length; ++var6) {
         this.AA((short)var1[var6].AA, var1[var6].AE, (byte)var1[var6].AD);
         if (var6 == var2 - 1 && var2 < var1.length) {
            this.AA((String)T.TC, (byte)5, (byte)Class_FB.AG);
         }
      }

      this.CP = null;
   }

   public final mVector AA(byte var1) {
      mVector var2 = new mVector();
      if (var1 == 0) {
         var2.addElement(GameCanvas.tabInven.CF);
         var2.addElement(GameCanvas.tabInven.CG);
      } else if (var1 == 1) {
         var2.addElement(GameCanvas.tabInven.CH);
         var2.addElement(GameCanvas.tabInven.CK);
      } else if (var1 == 2) {
         var2.addElement(GameCanvas.tabInven.CI);
      } else if (var1 == 5) {
         var2.addElement(GameCanvas.CP.CO);
      }

      return var2;
   }

   public final mVector AB(byte var1) {
      mVector var2;
      (var2 = new mVector()).addElement(Class_BH.CD);
      return var2;
   }

   public final mVector getActionChest() {
      mVector var1;
      (var1 = new mVector()).addElement(TabChest.cmdRemove);
      var1.addElement(TabChest.cmdUpgrade);
      return var1;
   }

   public final mVector AB() {
      mVector var1 = new mVector();
      if (ScreenUpgrade.instance != null) {
         var1.addElement(ScreenUpgrade.instance.AS);
      }

      return var1;
   }

   public final mVector AC() {
      mVector var1 = new mVector();
      if (SplitScreen.instance != null) {
         var1.addElement(SplitScreen.instance.AS);
      }

      return var1;
   }

   public final MainImage AD() {
      return ObjectData.getImageAll(super.idIcon, ObjectData.hashImageItem, (short)3000);
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      super.AA(var1, var2, var3, var4);
   }

   public final void AB(mGraphics g, int x, int y, int w) {
      MainImage var5;
      if ((var5 = ObjectData.getImageAll(super.idIcon, ObjectData.HashImageOtherNew, (short)23000)) != null && var5.img != null) {
         g.drawRegion((mImage)var5.img, x, y, 3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
      }

      if (!GameCanvas.lowGraphic) {
         g.drawRegion((mImage)AvMain.mImgRoomW[3], x, y - w / 2, 3);
      }

      if (super.timeUse > 0) {
         if (!GameCanvas.lowGraphic) {
            g.drawRegion(AvMain.mImgRoomW[2], 0, GameCanvas.gameTick / 12 % 4 * 11, 11, 11, 0, x - 22, y - w / 2, 3);
         }

         super.marketTime.paintCountDownTicketHour(g, mFont.tahoma_7_white, x - 6, y - w / 2 - 5, 0);
      } else if (super.timeUse == 0) {
         mFont.tahoma_7b_yellow.drawString(g, T.open, x, y - w / 2 - 5, 2);
      } else {
         String text = super.maxTimeUse / 60 + "h";
         if (super.maxTimeUse % 60 != 0) {
            text = text + super.maxTimeUse % 60 + "'";
         }

         mFont.tahoma_7b_white.drawString(g, text, x, y - w / 2 - 5, 2);
      }
   }

   public final void setTimeMarket(int time) {
      super.timeUse = time;
      super.marketTime.setCountDown(time);
   }

   public static void CheckAddDataKichAn() {
      for(int i = 0; i < vecItemKichAnCheckInfo.size(); ++i) {
         Item item;
         if (MainItem.getDataKichAn(item = (Item)vecItemKichAnCheckInfo.elementAt(i)).length() > 0) {
            vecItemKichAnCheckInfo.removeElement(item);
            --i;
         }
      }
   }
}
