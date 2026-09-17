public final class Item extends MainItem {
   public static mVector vecItemKichAnCheckInfo = new mVector("Item.vecItemKichAnCheckInfo");
   private MainInfoItem[] mInfoItemSave = null;

   public Item(byte typeItem, short id, short idIcon, String name, byte isTrade) {
      super((byte)3, id, idIcon, name, isTrade);
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

   public final void setDataItem(short var1, byte var2, byte colorName, int var4, byte var5, byte var6, byte var7, short[] var8, short var9, byte var10, byte var11) {
      super.Lv_RQ = var1;
      super.charClass = var2;
      super.colorName = colorName;
      super.timeUse = var4;
      super.typeEquip = var5;
      super.LvUpgrade = var6;
      if (super.LvUpgrade > 0) {
         super.namepaint = super.name + " +" + super.LvUpgrade;
      } else {
         super.namepaint = super.name;
      }

      super.numLoKham = var7;
      super.mDaKham = var8;
      super.valueCheTac = var9;
      super.isHoanMy = var10;
      super.valueKickAn = var11;
      if (super.idIcon == 242 && colorName == 0) {
         super.colorName = (byte)(super.LvUpgrade / 10);
         if (super.colorName > 9) {
            super.colorName = 9;
         }

         super.typeSpec = 1;
      }

   }

   public final void setInfoItem(MainInfoItem[] var1, int var2) {
      super.vecInfo.removeAllElements();
      if (super.valueKickAn >= 0) {
         String var3 = MainItem.getDataKichAn(this);
         String[] var5 = mFont.tahoma_7_black.splitFontArray(var3, super.BS);

         for(int var4 = 0; var4 < var5.length; ++var4) {
            this.addInfo((String)var5[var4], (byte)2);
         }
      }

      this.addInfo((String)T.SZ, (byte)5, (byte)infoShow.HARDCODE_PAINT_CENTER);
      if (super.typeEquip != 10 && super.typeSpec != 1) {
         this.addInfo((String)(T.gohome + ": " + super.Lv_RQ), (byte)(super.Lv_RQ <= GameScreen.player.Lv ? 4 : 3), (byte)infoShow.AF);
      }

      this.addInfo((String)(T.RP + super.valueCheTac), (byte)0);
      if (super.BO == 2) {
         this.addInfo((String)T.LY, (byte)3);
      }

      int i;
      if (super.LvUpgrade >= 11 && super.LvUpgrade <= 15 && super.typeSpec != 1) {
         this.addInfo((String)T.thongtinfullset, (byte)5, (byte)infoShow.HARDCODE_PAINT_CENTER);
         this.addInfo((String)(T.ST + super.LvUpgrade), (byte)7, (byte)((byte)(90 + super.LvUpgrade)));

         for(i = 0; i < super.LvUpgrade - 10; ++i) {
            byte var7;
            if ((var7 = (byte)(i + 101)) > 105) {
               var7 = 105;
            }
            this.addInfo((String)T.WX[i], (byte)7, (byte)var7);
            if (i == 4) {
               this.addInfo((String)T.WX[i + 1], (byte)7, (byte)var7);
            }
         }
      }
      this.addInfo((String)T.thongtinchiso, (byte)5, (byte)infoShow.AH);
      for(i = 0; i < var1.length; ++i) {
         this.AA((short)var1[i].AA, var1[i].AE, (byte)var1[i].AD);
         if (i == var2 - 1 && var2 < var1.length) {
            this.addInfo((String)T.TC, (byte)5, (byte)infoShow.HARDCODE_PAINT_CENTER);
         }
      }

      this.mInfoItemSave = null;
   }

   public final mVector getActionInven(byte type) {
      mVector mVector = new mVector();
      if (type == 0) {
         mVector.addElement(GameCanvas.tabInven.CF);
         mVector.addElement(GameCanvas.tabInven.CG);
      } else if (type == 1) {
         mVector.addElement(GameCanvas.tabInven.CH);
         mVector.addElement(GameCanvas.tabInven.CK);
      } else if (type == 2) {
         mVector.addElement(GameCanvas.tabInven.cmdSetChestItem);
      } else if (type == 5) {
         mVector.addElement(GameCanvas.tabInvenMarket.cmdSellMarket);
      }

      return mVector;
   }

   public final mVector getActionShop(byte typeShop) {
      mVector var2;
      (var2 = new mVector()).addElement(TabShop.cmdBuyItem);
      return var2;
   }

   public final mVector getActionChest() {
      mVector var1;
      (var1 = new mVector()).addElement(TabChest.cmdRemove);
      var1.addElement(TabChest.cmdUpgrade);
      return var1;
   }

   public final mVector getActionUpgrade() {
      mVector m = new mVector();
      if (ScreenUpgrade.instance != null) {
         m.addElement(ScreenUpgrade.instance.AS);
      }

      return m;
   }

   public final mVector getActionSplit() {
      mVector m = new mVector();
      if (SplitScreen.instance != null) {
         m.addElement(SplitScreen.instance.AS);
      }

      return m;
   }

   public final MainImage getImage() {
      return ObjectData.getImageAll(super.idIcon, ObjectData.hashImageItem, (short)3000);
   }

   public final void paint(mGraphics g, int x, int y, int w) {
      super.paint(g, x, y, w);
   }

   public final void paintRuong(mGraphics g, int x, int y, int w) {
      MainImage imageRuong;
      if ((imageRuong = ObjectData.getImageAll(super.idIcon, ObjectData.HashImageOtherNew, (short)23000)) != null && imageRuong.img != null) {
         g.drawRegion((mImage)imageRuong.img, x, y, 3);
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
