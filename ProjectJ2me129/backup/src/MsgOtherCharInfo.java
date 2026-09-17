public final class MsgOtherCharInfo extends MsgDialog {
   private MainObject AB;
   private int AC = 120;
   private int BB;
   private int[][] BC;
   private MainItem BD;
   private int BE;
   private int BF;
   private int BG;
   private int BH = 0;
   private boolean BI = false;
   private boolean BJ = false;
   public static Class_DS AA = null;
   private iCommand BK;
   private int BL = 0;
   private int BM = 0;

   public MsgOtherCharInfo(MainObject var1) {
      this.AB = var1;
      this.BK = null;
      if (var1 != null) {
         super.cmdList.removeAllElements();
         super.AG = new iCommand(T.close, 1, this);
         super.AG = AvMain.AA(super.AG, 2);
         if (AA != null && AA.AC.compareTo(this.AB.name) == 0) {
            this.BK = new iCommand(T.CJ, 2, this);
            this.BK = AvMain.AA(this.BK, 0);
            this.BK = AvMain.AA(this.BK, 1);
            super.DA = this.BK;
         }

         super.DB = super.AG;
         super.wDia = 160;
         this.BH = 0;
         if (var1.hOne > 52) {
            this.BH += var1.hOne - 52;
         }

         if (var1.MY > 0) {
            this.BH += 15;
         }

         super.hDia = 140;
         if (this.BH > 15) {
            super.hDia += this.BH - 15;
         }

         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.hh - super.hDia / 2;
         super.wItem = 27;
         this.BC = new int[TabEquip.BN][];
         int var2 = super.AY + super.wItem - 7 + 15;
         int var3 = super.AX + super.wDia / 6 - super.wItem / 2;

         for(int var4 = 0; var4 < this.BC.length; ++var4) {
            this.BC[var4] = new int[2];
            if (var4 == 8) {
               this.BC[var4][0] = var3 + var4 % 2 * ((super.wDia / 3 << 1) + 2) + super.wItem * 5 / 4;
               this.BC[var4][1] = var2 - super.wItem / 2 + var4 / 2 * super.wItem - super.wItem + super.wItem / 4;
            } else if (var4 == 9) {
               this.BC[var4][0] = var3 + var4 % 2 * ((super.wDia / 3 << 1) + 2) - super.wItem * 5 / 4;
               this.BC[var4][1] = var2 - super.wItem / 2 + var4 / 2 * super.wItem - super.wItem + super.wItem / 4;
            } else {
               this.BC[var4][0] = var3 + var4 % 2 * ((super.wDia / 3 << 1) + 2);
               this.BC[var4][1] = var2 - super.wItem / 2 + var4 / 2 * super.wItem;
            }
         }

         if (GameCanvas.isTouch) {
            super.idSelect = -1;
            super.AG.setPos(super.AX + super.AT / 2 + this.AC / 2, super.AY - 20 + 8, MainTab.fraCloseTab, "");
            super.cmdList.addElement(super.AG);
            if (this.BK != null) {
               this.BK = AvMain.AA(this.BK, 0);
               super.cmdList.addElement(this.BK);
            }
         } else {
            super.hDia += 10;
            this.BI = false;
            this.BD = (MainItem)var1.LR.get("" + super.idSelect);
         }

         super.backCMD = super.AG;
         this.BJ = false;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         AA = null;
         break;
      case 1:
         GameCanvas.end_Dialog();
         AA = null;
         break;
      case 2:
         if (AA != null) {
            GlobalService.getInstance().AA((byte)1, (short)((short)AA.AG), (byte)0);
            if (GameCanvas.eventScr.AY != null) {
               for(var1 = 0; var1 < GameCanvas.eventScr.AY.size(); ++var1) {
                  Class_DS var3;
                  if ((var3 = (Class_DS)GameCanvas.eventScr.AY.elementAt(var1)) == AA) {
                     GameCanvas.eventScr.AY.removeElement(var3);
                     return;
                  }
               }
            }
         }

         return;
      }

      super.commandPointer(var1, var2);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + 4;
      int var3 = super.AX + super.wDia / 2;
      this.AE(var1, super.AX - 5, super.AY - 32, super.AT + 10, super.hDia + 44, super.AT + 10);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + super.wDia / 2 - this.AC / 2, super.AY - 20, this.AC, 16, 4, 4);
      AvMain.FontBorderColor(var1, this.AB.name, super.AX + super.AT / 2, super.AY - 18, 2, (int)6, (int)5);
      var1.AD(MotherCanvas.hw - super.AT / 2, super.AY, super.AT, super.hDia);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AT;
      var10000 = super.AY;
      var10000 = super.AT;
      var10000 = super.hDia;
      mGraphics.AD();
      var2 += 4;
      MainImage var4;
      if (this.AB.clan != null && (var4 = Potion.getIconClan(this.AB.clan.idIcon)) != null && var4.img != null) {
         int var5 = -mFont.tahoma_7b_black.getWidth(this.AB.clan.name) / 2;
         if (var4.frame == -1) {
            var4.set_Frame();
         }

         if (var4.frame <= 1) {
            var1.drawRegion((mImage)var4.img, super.AX + super.wDia / 2 + var5, var2, 3);
         } else {
            byte var6;
            if (this.BM >= var4.frame - 1) {
               var6 = 15;
            } else {
               var6 = 3;
            }

            if (CRes.abs(GameCanvas.gameTick - this.BL) > var6) {
               ++this.BM;
               if (this.BM >= var4.frame) {
                  this.BM = 0;
               }

               this.BL = GameCanvas.gameTick;
            }

            var1.drawRegion(var4.img, 0, this.BM * var4.AB, var4.AB, var4.AB, 0, super.AX + super.wDia / 2 + var5, var2, 3);
         }

         mFont.tahoma_7b_black.drawString(var1, this.AB.clan.name, super.AX + super.wDia / 2 + 9, var2 - 6, 2);
      }

      var2 += 15;
      mImage var10;
      if (this.AB.Lv == 100) {
         var10 = Interface_Game.imgIconMPHP2;
      } else {
         var10 = Interface_Game.imgIconMPHP;
      }

      var1.drawRegion((mImage)var10, var3 - 47 + 7, var2, 0);
      Interface_Game.AA(var1, (byte)1, this.AB.Hp, this.AB.maxHp, var3 - 47 + 18, var2, 0, 9, 66, 0, false, 0, false, this.AB.MA);
      var2 += 11;
      Interface_Game.AA(var1, (byte)2, this.AB.Mp, this.AB.maxMp, var3 - 47 + 18, var2, 0, 9, 66, 0, false, 0, false, 0);
      var2 += 9;
      int var11;
      if (this.AB.Lv == 100) {
         mFont.tahoma_7_black.drawString(var1, this.AB.LvThongThao + " + " + this.AB.KS / 10 + "," + this.AB.KS % 10 + "%", var3 - 47 + 20, var2, 0);
         var2 += 10;
         var11 = this.AB.KS / 10 * 70 / 100;
      } else {
         mFont.tahoma_7_black.drawString(var1, this.AB.Lv + " + " + this.AB.percentLv / 10 + "," + this.AB.percentLv % 10 + "%", var3 - 47 + 20, var2, 0);
         var2 += 10;
         var11 = this.AB.percentLv / 10 * 70 / 100;
      }

      var1.setColor(-15519213);
      var1.fillRect(var3 - 47 + 18, var2, 65, 2);
      if (var11 > 0) {
         var1.setColor(-13263058);
         var1.fillRect(var3 - 47 + 18, var2, var11, 2);
      }

      int var8;
      for(var8 = 1; var8 < 5; ++var8) {
         var1.setColor(-1);
         var1.fillRect(var3 - 47 + 18 + var8 * 13, var2, 1, 2);
      }

      for(var8 = 0; var8 < TabEquip.BN; ++var8) {
         if (var8 < 8) {
            AvMain.paintRect(var1, this.BC[var8][0] - 1, this.BC[var8][1] - 1, super.wItem - 2, super.wItem - 2, (byte)0, 3);
         }

         MainItem var7;
         if ((var7 = (MainItem)this.AB.LR.get("" + var8)) != null) {
            var7.AC(var1, this.BC[var8][0] + super.wItem / 2 - 1, this.BC[var8][1] + super.wItem / 2 - 1, super.wItem - 3);
            var7.AB(var1, this.BC[var8][0] + super.wItem / 2 - 1, this.BC[var8][1] + super.wItem / 2 - 1, super.wItem, 1);
         }

         if (super.idSelect == var8) {
            var1.setColor(-1);
            var1.drawRect(this.BC[var8][0], this.BC[var8][1], super.wItem - 4, super.wItem - 4);
            var1.drawRect(this.BC[var8][0] + 1, this.BC[var8][1] + 1, super.wItem - 6, super.wItem - 6);
         }
      }

      this.AB.paintThanhTich(var1, var2 + 26 + this.BH, super.AX + super.wDia / 2);
      var2 += 50 + this.BH;
      this.AB.AA(var1, super.AX + super.wDia / 2, var2, true);
      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(var8 = 0; var8 < super.cmdList.size(); ++var8) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var8)).paint(var1, var9.xCmd, var9.yCmd);
         }
      }

      super.AD(var1);
      if (this.BI && this.BD != null) {
         MainTab.AA(var1, this.BD, (mVector)null, (byte)0, this.BE, this.BF, this.BD.BS, this.BD.BT, false, this.AB, 0);
      }

   }

   public final void update() {
      if (this.BD != null && !this.BI) {
         ++this.BB;
         if (this.BB >= 10) {
            this.BI = true;
            int var10002 = this.BC[super.idSelect][0] + super.wItem / 2;
            int var4 = this.BC[super.idSelect][1] + super.wItem;
            int var3 = var10002;
            MainItem var2 = this.BD;
            int var5 = 100;
            int var6 = 40;
            if (var2 != null) {
               var5 = var2.BS;
               var6 = var2.BT;
            }

            this.BE = var3 - var5 / 2;
            if (this.BE + var5 > MotherCanvas.w - 8) {
               this.BE = MotherCanvas.w - var5 - 8;
            }

            if (this.BE < 8) {
               this.BE = 8;
            }

            this.BF = var4;
            if (this.BF + var6 > MotherCanvas.h - GameCanvas.hCommand - 8) {
               this.BF = MotherCanvas.h - GameCanvas.hCommand - var6 - 8;
            }

            if (this.BF < 8) {
               this.BF = 8;
            }
         }
      }

      if (!this.BJ) {
         this.BJ = true;

         for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var7;
            if ((var7 = (MainObject)GameScreen.vecPlayers.elementAt(var1)) != null && !var7.isRemove && var7.typeObject == 0 && var7.name.compareTo(this.AB.name) == 0) {
               this.AB.thanhtichLv = var7.thanhtichLv;
               this.AB.thanhtichPvP = var7.thanhtichPvP;
               if (this.AB.thanhtichLv >= 0) {
                  this.BG += 15;
               }

               if (this.AB.thanhtichPvP >= 0) {
                  this.BG += 15;
               }

               if (this.BG > this.BH) {
                  this.BH = this.BG;
               }

               return;
            }
         }
      }

      super.update();
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      int var2 = super.idSelect;
      if (super.idSelect == -1 && (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(1) || GameCanvas.isKeyPressed(3))) {
         super.idSelect = 0;
         GameCanvas.AH();
         var1 = true;
      }

      if (GameCanvas.isKeyPressed(0)) {
         --super.idSelect;
         GameCanvas.clearKeyPressed(0);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(2)) {
         ++super.idSelect;
         GameCanvas.clearKeyPressed(2);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(1)) {
         if (super.idSelect >= 2) {
            super.idSelect -= 2;
         }

         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.idSelect < TabEquip.BN - 2) {
            super.idSelect += 2;
         }

         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (var1) {
         if (super.idSelect == 8) {
            if ((MainItem)this.AB.LR.get("" + super.idSelect) == null) {
               if ((MainItem)this.AB.LR.get("9") == null) {
                  super.idSelect = var2;
                  return;
               }

               super.idSelect = 9;
            }
         } else if (super.idSelect == 9 && (MainItem)this.AB.LR.get("" + super.idSelect) == null) {
            if ((MainItem)this.AB.LR.get("8") == null) {
               super.idSelect = var2;
               return;
            }

            super.idSelect = 8;
         }

         if (super.idSelect >= 0 && super.idSelect < TabEquip.BN) {
            if (var2 != super.idSelect) {
               this.BI = false;
               this.BD = (MainItem)this.AB.LR.get("" + super.idSelect);
            }
         } else {
            super.idSelect = -1;
            this.BI = false;
            this.BD = null;
         }
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      if (super.cmdList != null) {
         for(int var1 = 0; var1 < super.cmdList.size(); ++var1) {
            ((iCommand)super.cmdList.elementAt(var1)).AE();
         }
      }

      if (GameCanvas.isPointerSelect) {
         boolean var4 = true;

         for(int var2 = 0; var2 < TabEquip.BN; ++var2) {
            if ((var2 == 8 || var2 == 9) && (MainItem)GameScreen.player.LR.get("" + var2) == null) {
               GameCanvas.isPointerSelect = false;
               break;
            }

            if (GameCanvas.AB(this.BC[var2][0] - 2, this.BC[var2][1] - 2, super.wItem, super.wItem)) {
               var4 = false;
               if (var2 != super.idSelect) {
                  super.idSelect = var2;
                  this.BD = (MainItem)this.AB.LR.get("" + super.idSelect);
                  this.BI = false;
               }

               GameCanvas.isPointerSelect = false;
               break;
            }
         }

         if (var4) {
            this.BD = null;
            this.BI = false;
            super.idSelect = -1;
         }
      }

      super.updatePointer();
   }
}
