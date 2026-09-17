public final class MsgShowGift extends MsgDialog {
   private Item_Drop[] mItemgift;
   private String strphanthuong = "";
   private MainObject BB = null;
   private int indexShowItemBox = 0;
   private int timeShowEff;
   private int BE;
   private int speed;
   private int BG;
   private int BH;
   private short idChest;
   private FrameImage fraRuongVip;
   public static MainItem AA;
   private byte typeBanner = -1;
   private byte BL = 0;
   private static int[] colorBorder = new int[]{-9426176, -7714290, -7714290, -10158080, -7602176, -4784128, -15400104, -14283131, -12574276, -9947390, -6662127, -3373778, -15727484, -15597386, -13035009, -16513215, -15657359, -15195190};
   private static int[] colorBorderVongQuay = new int[]{-1392245, -3563155, -863583, -2968700};
   private int xCloseQuickOpen = 243;
   private int yCloseQuickOpen = 76;
   private int[][] mShowRuongVip = new int[][]{{-1, 0, 1}, {-1, 0, 1}, new int[3], new int[3], {1, 0, -1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, 1}, new int[3], new int[3], {1, 0, -1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, 1}, new int[3], new int[3], {1, 0, -1}, {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}, new int[3], {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}, {-1, 0, 1}, new int[3], {1, 0, -1}};

   public final void setinfoShow_Gift(byte type, String name, String info, Item_Drop[] mitem, short IdChest) {
      this.fraRuongVip = null;
      super.type = type;
      this.mItemgift = mitem;
      super.nameDialog = name;
      this.idChest = IdChest;
      super.wItem = 22;
      super.vecEffUni.removeAllElements();
      super.fontDia = mFont.tahoma_7_black;
      this.timeShowEff = 0;
      this.beginDia();
      super.cmdList = new mVector();
      int var6;
      if (type != 0 && type != 3) {
         if (type == 1 || type == 2 || type >= 10) {
            super.wItem = 26;
            this.strphanthuong = T.nhanduoc;
            this.indexShowItemBox = 0;
            if (type >= 10 && type < 20) {
               this.typeBanner = (byte)(type - 10);
            }

            if (type == 1) {
               boolean var10000;
               if (AA == null) {
                  var10000 = false;
               } else {
                  var6 = 0;

                  while(true) {
                     if (var6 >= Player.vecInventory.size()) {
                        var10000 = false;
                        break;
                     }

                     MainItem var9;
                     if ((var9 = (MainItem)Player.vecInventory.elementAt(var6)).ID == AA.ID && var9.typeObject == AA.typeObject) {
                        var10000 = var9.numPotion > 1;
                        break;
                     }

                     ++var6;
                  }
               }

               if (var10000) {
                  iCommand var7 = new iCommand(T.LS, 11, this);
                  super.cmdList.addElement(var7);
               }
            } else if (type == 21) {
               this.fraRuongVip = null;
               this.indexShowItemBox = this.mItemgift.length;
               MainImage var8;
               if ((var8 = ObjectData.getImageAll(this.idChest, ObjectData.HashImageOtherNew, (short)23000)) != null && var8.img != null) {
                  this.fraRuongVip = new FrameImage(var8.img, 2);
               }

               super.vecEffUni.addElement(GameScreen.AB((short)144, 1, MotherCanvas.hw, MotherCanvas.hh, 2300, (byte)0, (MainObject)null));
            }
         }
      } else {
         this.strphanthuong = T.EJ;
         this.indexShowItemBox = this.mItemgift.length;
      }

      super.AG = new iCommand(T.close, 10, this);
      super.cmdList.addElement(super.AG);
      super.strinfo = null;
      super.wDia = 160;
      int var11;
      if (mitem != null) {
         for(var6 = 0; var6 < mitem.length; ++var6) {
            String var10 = mitem[var6].num + " " + mitem[var6].name;
            if (mitem[var6].typeObject == 3) {
               var10 = mitem[var6].name;
            }

            if ((var11 = mFont.tahoma_7b_black.getWidth(var10)) + 60 > super.wDia) {
               super.wDia = var11 + 60;
            }
         }
      }

      if (super.wDia > MotherCanvas.w) {
         super.wDia = MotherCanvas.w;
      }

      if (type == 20) {
         super.wDia = 190;
         super.wItem = 44;
      }

      var6 = 0;
      if (info != null && info.length() > 0) {
         super.strinfo = super.fontDia.splitFontArray(info, super.wDia - 20);
         var6 = super.strinfo.length;
      }

      super.hDia = GameCanvas.hText * var6 + MsgDialog.hPlus + iCommand.hButtonCmdNor + super.wItem;
      super.hDia += GameCanvas.hCommand;
      if (this.mItemgift != null) {
         if (type == 3) {
            super.hDia += this.mItemgift.length * 50;
            if (mitem != null) {
               for(var11 = 0; var11 < mitem.length; ++var11) {
                  this.typeBanner = (byte)(mitem[var11].IdIcon - 500);
               }
            }
         } else if (type == 20) {
            super.hDia = (this.mItemgift.length / 3 + 1) * super.wItem + iCommand.hButtonCmdNor;
         } else {
            super.hDia += this.mItemgift.length * super.wItem;
         }
      }

      if (!GameCanvas.lowGraphic && this.typeBanner >= AvMain.fraBorderWanted.nFrame) {
         this.typeBanner = (byte)(AvMain.fraBorderWanted.nFrame - 1);
      }

      super.AT = super.wDia;
      super.AS = 5;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      this.setPosCmdNew(-2, false);
   }

   public final void setCmdList(mVector vecCmd) {
      super.cmdList = vecCmd;
      this.setPosCmdNew(-2, false);
   }

   public final void setinfoShow_Gift_OnHead(byte var1, String var2, String info, Item_Drop[] var4, short var5, MainObject var6) {
      this.fraRuongVip = null;
      super.type = var1;
      this.mItemgift = var4;
      super.nameDialog = var2;
      this.idChest = var5;
      this.BB = var6;
      this.speed = 3;
      if (this.mItemgift.length >= 5) {
         this.speed = 4;
      }

      GameCanvas.chatTabScr.addNewChat(T.tabSieuBoss, "", "-- " + info + " --", (byte)1, false, 0);

      for(int i = 0; i < this.mItemgift.length; ++i) {
         Item_Drop item_Drop;
         (item_Drop = this.mItemgift[i]).y = 0 - i * 24;
         info = item_Drop.num + " " + item_Drop.name;
         if (item_Drop.typeObject == 3) {
            info = item_Drop.name;
         }

         GameCanvas.chatTabScr.addNewChat(T.tabSieuBoss, "", info, (byte)1, false, 5);
      }

   }

   public final void paint(mGraphics var1) {
      int var2;
      if (super.type == 22) {
         for(var2 = 0; var2 < this.mItemgift.length; ++var2) {
            Item_Drop var8;
            if ((var8 = this.mItemgift[var2]).y >= 0 && var8.y < 120) {
               if (var8.typeObject == 99) {
                  var1.drawRegion((mImage)AvMain.imgXp, this.BB.x, this.BB.y - 52 - var8.y, 3);
                  AvMain.AB(var8.colorName).drawString(var1, " " + var8.num + var8.name, this.BB.x + 14, this.BB.y - 56 - var8.y, 0);
               } else {
                  var8.AA(var1, this.BB.x, this.BB.y - 52 - var8.y);
                  String var7 = var8.num + " " + var8.name;
                  if (var8.typeObject == 3) {
                     var7 = var8.name;
                  }

                  AvMain.AA(var1, var7, this.BB.x + 15, this.BB.y - 56 - var8.y, 0, (byte)var8.colorName);
               }
            }
         }

      } else {
         GameCanvas.resetTrans(var1);
         if (super.type != 21 || this.BL != 0 && this.BL != 1) {
            if (super.type == 21) {
               AvMain.AF(var1, MotherCanvas.hw - super.AT / 2 - 4, super.AY - 4, super.AT + 8, super.hDia + 8);
               GameCanvas.resetTrans(var1);
            } else if (super.type != 2 && super.type != 3 && (super.type < 10 || super.type > 19)) {
               int var10002 = MotherCanvas.hw - super.AS / 2;
               int var10006 = super.AT;
               this.AD(var1, var10002, super.AY, super.AS, super.hDia, 0);
               GameCanvas.resetTrans(var1);
               var1.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
            } else {
               AvMain.AE(var1, MotherCanvas.hw - super.AT / 2, super.AY, super.AT, super.hDia);
               GameCanvas.resetTrans(var1);
            }

            if (super.type == 21) {
               var1.setColor(-1267638);
               var1.fillRect(super.AX + super.wDia / 2 - 45, super.AY + 13, 90, 16);
               var1.setColor(-6221);
               var1.fillRect(super.AX + super.wDia / 2 - 45, super.AY + 14, 90, 14);
               var1.setColor(-1826);
               var1.fillRect(super.AX + super.wDia / 2 - 45, super.AY + 17, 90, 8);
               var1.drawRegion(AvMain.imgBannerRuong, 0, 0, 16, 16, 0, super.AX + super.wDia / 2 - 45 - 15, super.AY + 11 + 2, 0);
               var1.drawRegion(AvMain.imgBannerRuong, 0, 0, 16, 16, 2, super.AX + super.wDia / 2 + 45 - 1, super.AY + 11 + 2, 0);
            } else if (super.type == 20) {
               if (AvMain.FN == null) {
                  AA();
               }

               var1.setColor(-6325937);
               var1.fillRect(super.AX + super.wDia / 2 - 60, super.AY + 13, 120, 16);
               var1.setColor(-4418715);
               var1.fillRect(super.AX + super.wDia / 2 - 60, super.AY + 14, 120, 14);
               var1.drawRegion(AvMain.FN[0], 0, 0, 16, 16, 0, super.AX + super.wDia / 2 - 60 - 15, super.AY + 11 + 2, 0);
               var1.drawRegion(AvMain.FN[0], 0, 0, 16, 16, 2, super.AX + super.wDia / 2 + 60 - 1, super.AY + 11 + 2, 0);
            } else if (this.typeBanner >= 0 && !GameCanvas.lowGraphic) {
               var1.setColor(colorBorder[this.typeBanner * 3]);
               var1.fillRect(super.AX + 16, super.AY + 11, super.wDia - 30, 20);
               var1.setColor(colorBorder[this.typeBanner * 3 + 1]);
               var1.fillRect(super.AX + 17, super.AY + 12, super.wDia - 32, 18);
               var1.setColor(colorBorder[this.typeBanner * 3 + 2]);
               var1.fillRect(super.AX + 18, super.AY + 13, super.wDia - 34, 16);
               AvMain.fraBorderWanted.drawFrame(this.typeBanner, super.AX + 15, super.AY + 11 + 10, 0, 3, var1);
               AvMain.fraBorderWanted.drawFrame(this.typeBanner, super.AX + 15 + (super.wDia - 28), super.AY + 11 + 10, 2, 3, var1);
            } else {
               var1.setColor(-805042);
               var1.fillRoundRectNew(super.AX + 10, super.AY + 12, super.wDia - 20, 16, 4, 4);
            }

            AvMain.FontBorderColor(var1, super.nameDialog, super.AX + super.wDia / 2, super.AY + 15, 2, (int)6, (int)5);
            var2 = super.AY + GameCanvas.hCommand + 10;
            int var5;
            if (super.strinfo != null) {
               for(var5 = 0; var5 < super.strinfo.length; ++var5) {
                  if (var5 == 0 && super.type == 0) {
                     mFont.tahoma_7b_black.drawString(var1, super.strinfo[var5], super.AX + super.wDia / 2, var2, 2);
                  } else {
                     super.fontDia.drawString(var1, super.strinfo[var5], super.AX + 15, var2, 0);
                  }

                  var2 += GameCanvas.hText;
               }
            }

            if (this.mItemgift != null) {
               if (super.type == 20) {
                  this.AB(var1, var2);
               } else {
                  this.AA(var1, var2);
               }
            }

            for(var5 = 0; var5 < super.vecEffUni.size(); ++var5) {
               MainEffect var4;
               if ((var4 = (MainEffect)super.vecEffUni.elementAt(var5)).levelPaint != -1) {
                  var4.paint(var1);
                  var4.paint(var1, 0, 0);
               }
            }

            if (super.cmdList != null) {
               for(var5 = 0; var5 < super.cmdList.size(); ++var5) {
                  iCommand var6;
                  (var6 = (iCommand)super.cmdList.elementAt(var5)).paint(var1, var6.xCmd, var6.yCmd);
               }
            }

            if (TabInventory.numPotionTemp > 0) {
               MainTab.fraCloseTab.drawFrame(0, this.xCloseQuickOpen, this.yCloseQuickOpen, 0, 3, var1);
            }

         } else {
            for(var2 = 0; var2 < super.vecEffUni.size(); ++var2) {
               MainEffect var3;
               if ((var3 = (MainEffect)super.vecEffUni.elementAt(var2)).levelPaint == -1) {
                  var3.paint(var1, 0, 0);
               }
            }

            this.AE(var1);
            if (TabInventory.numPotionTemp > 0) {
               MainTab.fraCloseTab.drawFrame(0, this.xCloseQuickOpen, this.yCloseQuickOpen, 0, 3, var1);
            }

            GameCanvas.resetTrans(var1);
         }
      }
   }

   private void AE(mGraphics var1) {
      if (this.BL == 0) {
         int var2;
         if (this.fraRuongVip != null && (var2 = this.timeShowEff) < this.mShowRuongVip.length) {
            int var3 = 0;
            int var4 = 0;
            if (this.timeShowEff >= 10 && this.timeShowEff < 24) {
               var3 = CRes.random_Am_0(2);
               var4 = CRes.random_Am_0(2);
            } else if (this.timeShowEff >= 24) {
               var3 = CRes.random_Am_0(3);
               var4 = CRes.random_Am_0(2);
            }

            for(int var5 = 0; var5 < 3; ++var5) {
               var1.drawRegion(this.fraRuongVip.imgFrame, var5 % 3 * this.fraRuongVip.frameWidth / 3, 0, this.fraRuongVip.frameWidth / 3, this.fraRuongVip.frameHeight, 0, MotherCanvas.hw - this.fraRuongVip.frameWidth / 2 + var5 % 3 * (this.fraRuongVip.frameWidth / 3) + var3, MotherCanvas.hh - this.fraRuongVip.frameHeight / 2 + this.mShowRuongVip[var2][var5 % 3] + var4, 0);
               if (var5 == 1) {
                  AvMain.fraEffOpen.drawFrame(this.timeShowEff % AvMain.fraEffOpen.nFrame, MotherCanvas.hw + var3, MotherCanvas.hh + var4, 0, 3, var1);
               }
            }

            return;
         }
      } else if (this.BL == 1 && this.fraRuongVip != null) {
         this.fraRuongVip.drawFrame(1, MotherCanvas.hw, MotherCanvas.hh, 0, 3, var1);
      }

   }

   private void AA(mGraphics var1, int var2) {
      super.fontDia.drawString(var1, this.strphanthuong, super.AX + 15, var2, 0);
      if (super.type != 3) {
         AvMain.paintRect(var1, super.AX + 10, var2 + super.wItem / 2 + 5, super.wDia - 20, this.mItemgift.length * super.wItem, (byte)0, 4);
      }

      var2 += super.wItem;
      this.BE = var2;

      for(int var3 = 0; var3 < this.indexShowItemBox; ++var3) {
         if (var3 < this.mItemgift.length) {
            Item_Drop var4 = this.mItemgift[var3];
            if (super.type == 3) {
               var4.AA(var1, super.AX + super.wDia / 2, var2 + 16);
               var2 += 50;
            } else {
               if (var4.typeObject == 99) {
                  var1.drawRegion((mImage)AvMain.imgXp, super.AX + 28, var2 + super.wItem / 4 + 1, 3);
                  AvMain.AB(var4.colorName).drawString(var1, " " + var4.num + var4.name, super.AX + 42, var2, 0);
               } else {
                  var4.AA(var1, super.AX + 28, var2 + super.wItem / 4);
                  String var5 = var4.num + " " + var4.name;
                  if (var4.typeObject == 3) {
                     var5 = var4.name;
                  }

                  AvMain.AA(var1, var5, super.AX + 43, var2, 0, (byte)var4.colorName);
               }

               var2 += super.wItem;
            }
         }
      }

   }

   private void AB(mGraphics var1, int var2) {
      if (AvMain.FN == null) {
         AA();
      }

      int var3;
      for(var3 = 0; var3 < this.mItemgift.length / 3; ++var3) {
         var1.setColor(colorBorderVongQuay[0 + var3 % 2]);
         var1.fillRect(super.AX + super.wDia / 2 - 50, var2 + var3 * super.wItem, 100, 36);
         var1.setColor(colorBorderVongQuay[2 + var3 % 2]);
         var1.fillRect(super.AX + super.wDia / 2 - 50, var2 + var3 * super.wItem, 100, 36);
         var1.drawRegion(AvMain.FN[1 + var3 % 2], 0, 0, 36, 36, 0, super.AX + super.wDia / 2 - 80, var2 + var3 * super.wItem, 0);
         var1.drawRegion(AvMain.FN[1 + var3 % 2], 0, 36, 36, 36, 0, super.AX + super.wDia / 2 + 80 - 36, var2 + var3 * super.wItem, 0);
         var1.drawRegion(AvMain.FN[4], 0, var3 * 17, 13, 17, 0, super.AX + super.wDia / 2 - 70, var2 + var3 * super.wItem + super.wItem / 2 - 5, 3);
      }

      for(var3 = 0; var3 < this.mItemgift.length; ++var3) {
         int var4 = super.AX + super.wDia / 2 - super.wItem + super.wItem * (var3 % 3);
         int var5 = var2 + super.wItem / 2 + super.wItem * (var3 / 3) - 4;
         var1.drawRegion(AvMain.FN[3], 0, var3 / 3 % 2 * 30, 30, 30, 0, var4, var5, 3);
         Item_Drop var6;
         if (var3 < this.indexShowItemBox && (var6 = this.mItemgift[var3]).IdIcon >= 0) {
            if (var6.typeObject == 99) {
               var1.drawRegion((mImage)AvMain.imgXp, var4, var5 - 3, 3);
            } else {
               var6.AA(var1, var4, var5 - 3);
            }

            AvMain.AB(var6.colorName).drawString(var1, String.valueOf(var6.num), var4 - 1, var5 + 3, 0);
         }
      }

   }

   private static void AA() {
      AvMain.FN = new mImage[5];

      for(int var0 = 0; var0 < AvMain.FN.length; ++var0) {
         AvMain.FN[var0] = mImage.createImage("/interface/diavongquay" + var0 + ".png");
      }

   }

   public final void update() {
      int var1;
      for(var1 = 0; var1 < super.vecEffUni.size(); ++var1) {
         MainEffect var2;
         (var2 = (MainEffect)super.vecEffUni.elementAt(var1)).update();
         if (var2.isStop) {
            super.vecEffUni.removeElement(var2);
            --var1;
         }
      }

      int var5;
      if (super.type == 22) {
         label96: {
            ++this.BH;
            if (this.mItemgift != null) {
               boolean var6 = true;

               for(var5 = 0; var5 < this.mItemgift.length; ++var5) {
                  Item_Drop var7;
                  Item_Drop var8 = var7 = this.mItemgift[var5];
                  var8.y += this.speed;
                  if (var7.y < 0) {
                     var6 = false;
                  }
               }

               if (!var6) {
                  break label96;
               }

               ++this.BG;
               if (this.BG < 40) {
                  break label96;
               }
            }

            GameCanvas.BS = null;
         }

         if (this.BH >= 200) {
            GameCanvas.BS = null;
         }

      } else if (super.isClose) {
         this.closeDialog();
      } else {
         this.updateAnimation();
         if (GameCanvas.isKeyPressed()) {
            this.handleKeyPress();
         }

         this.updatePointer();
         float var10000;
         if (super.type == 21) {
            ++this.timeShowEff;
            if (this.BL == 0) {
               if (this.timeShowEff % 10 == 0 && this.timeShowEff <= 30) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               var1 = (int)((double)this.mShowRuongVip.length / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
               if (this.timeShowEff >= var1) {
                  this.BL = 1;
                  this.timeShowEff = 0;
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               MainImage var4;
               if (this.timeShowEff % 5 == 0 && this.fraRuongVip == null && (var4 = ObjectData.getImageAll(this.idChest, ObjectData.HashImageOtherNew, (short)23000)) != null && var4.img != null) {
                  this.fraRuongVip = new FrameImage(var4.img, 2);
                  return;
               }
            } else if (this.BL == 1 && this.timeShowEff >= 3) {
               this.BL = 2;
               return;
            }
         } else if (super.type == 1 || super.type == 2 || super.type >= 10 && super.type <= 19) {
            if (this.indexShowItemBox < this.mItemgift.length) {
               ++this.timeShowEff;
               var5 = (int)(10.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
               if (this.timeShowEff % var5 == 2) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  super.vecEffUni.addElement(GameScreen.AB((short)53, 0, super.AX + 30, this.BE + this.indexShowItemBox * super.wItem, (byte)0, (MainObject)null));
               }

               if (this.timeShowEff % var5 == 0) {
                  ++this.indexShowItemBox;
                  return;
               }
            }
         } else if (super.type == 20 && this.indexShowItemBox < this.mItemgift.length) {
            ++this.timeShowEff;
            var5 = (int)(10.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            if (this.timeShowEff % var5 == 2) {
               int var3 = super.AX + super.wDia / 2 - super.wItem + super.wItem * (this.indexShowItemBox % 3);
               var1 = super.AY + GameCanvas.hCommand + 10 + super.wItem / 2 + super.wItem * (this.indexShowItemBox / 3) - 4;
               var10000 = mSound.volumeSound;
               mSound.playSound();
               super.vecEffUni.addElement(GameScreen.AB((short)53, 0, var3, var1, (byte)0, (MainObject)null));
            }

            if (this.timeShowEff % var5 == 0) {
               ++this.indexShowItemBox;
            }
         }

      }
   }

   public final void handleKeyPress() {
      if (super.type != 22) {
         if (super.type != 21 || this.BL == 2) {
            int var1 = super.AD;
            int var2 = super.cmdList.size();
            if (GameCanvas.isKeyPressed(0)) {
               --super.AD;
               GameCanvas.clearKeyPressed(0);
            } else if (GameCanvas.isKeyPressed(2)) {
               ++super.AD;
               GameCanvas.clearKeyPressed(2);
            }

            super.AD = AvMain.AA(super.AD, var2 - 1, false);
            if (var1 != super.AD && GameCanvas.isKeyPressed()) {
               if (((iCommand)super.cmdList.elementAt(super.AD)).caption.length() <= 0) {
                  super.AD = 0;
               }

               for(var1 = 0; var1 < var2; ++var1) {
                  iCommand var3 = (iCommand)super.cmdList.elementAt(var1);
                  if (var1 == super.AD) {
                     var3.AG = true;
                  } else {
                     var3.AG = false;
                  }
               }
            }

            if (GameCanvas.AL[5]) {
               GameCanvas.AB(5);
               if (super.cmdList != null && super.AD < super.cmdList.size()) {
                  ((iCommand)super.cmdList.elementAt(super.AD)).AD();
               }
            }

         }
      }
   }

   public final void updatePointer() {
      if (GameCanvas.isPointerSelect && TabInventory.numPotionTemp > 0 && GameCanvas.isPoint(this.xCloseQuickOpen - 10, this.yCloseQuickOpen - 10, 25, 25)) {
         if (TabInventory.numPotionTemp > 0) {
            TabInventory.numPotionTemp = 0;
         }

         GameCanvas.isPointerSelect = false;
      }

      if (super.type != 22) {
         if (super.type != 21 || this.BL == 2) {
            super.updatePointer();
         }
      }
   }
}
