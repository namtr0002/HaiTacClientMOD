public final class MsgAutoFire extends MsgDialog {
   private int AB;
   private int valueFire = 0;
   private int valueRevice = 0;
   public static short[][] value;
   private int BC;
   private int BD;
   private int indexBuff = 0;
   private iCommand BF;

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 9:
         super.isClose = true;
         if (this.valueFire == 0) {
            Player.typeAutoFireMain = 1;
            if (Player.AutoFireCur == 2) {
               Player.AutoFireCur = 1;
            }
         } else if (this.valueFire == 1) {
            Player.typeAutoFireMain = 2;
            if (Player.AutoFireCur == 1) {
               Player.AutoFireCur = 2;
            }
         } else {
            Player.typeAutoFireMain = -1;
            Player.AutoFireCur = -1;
         }

         if (value != null) {
            Player.typeAutoBuff = 0;

            for(var1 = 0; var1 < value.length; ++var1) {
               if (value[var1][1] == 1) {
                  Player.typeAutoBuff = 1;
               }
            }
         }

         Player.AutoRevice = (byte)this.valueRevice;
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AK();
      default:
      }
   }

   public final void AA() {
      super.fontDia = mFont.tahoma_7b_black;
      this.beginDia();
      super.cmdList = new mVector();
      this.BF = new iCommand(T.DA, 9, this);
      super.cmdList.addElement(this.BF);
      super.wDia = MotherCanvas.w;
      if (super.wDia > 180) {
         super.wDia = 180;
      }

      super.AT = super.wDia;
      super.AS = 5;
      this.AB = 28;
      super.wItem = 24;
      super.hDia = 160;
      int var1 = 0;
      short[] var2 = new short[10];

      int var3;
      for(var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
         Skill_Info var4;
         if ((var4 = (Skill_Info)Player.vecListSkill.elementAt(var3)).Lv_RQ >= 0 && var4.typeSkill == 2) {
            var2[var1] = var4.ID;
            ++var1;
         }
      }

      if (var1 > 0) {
         value = new short[var1][];

         for(var3 = 0; var3 < value.length; ++var3) {
            value[var3] = new short[2];
            value[var3][0] = var2[var3];
            value[var3][1] = 1;
         }
      }

      if (var1 > 3) {
         super.hDia += this.AB;
      }

      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      this.indexBuff = 0;
      this.valueRevice = Player.AutoRevice;
      this.BC = super.fontDia.getWidth(T.KW);
      this.BD = super.fontDia.getWidth(T.KX);
      this.setPosCmdNew(-2, false);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY;
      int var3 = super.AX + 15;
      int var10002 = MotherCanvas.hw - super.AS / 2;
      int var10006 = super.AT;
      this.AD(var1, var10002, var2, super.AS, super.hDia, 0);
      var1.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AS;
      var10000 = super.AS;
      var10000 = MotherCanvas.h;
      mGraphics.AD();
      var2 += 12;
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + 10, var2, super.wDia - 20, 16, 4, 4);
      var2 += 3;
      AvMain.FontBorderColor(var1, T.setting, super.AX + super.wDia / 2, var2, 2, (int)6, (int)5);
      int var4;
      if (value == null) {
         var4 = 0;
      } else {
         var4 = (value.length - 1) / 3;
      }

      var2 += this.AB;
      int var5;
      if (GameCanvas.isKeyPressed() && super.idSelect < 3) {
         var5 = this.AB;
         int var6 = var2 - this.AB / 4 - 1 + super.idSelect * this.AB;
         if (super.idSelect == 1) {
            var5 = this.AB * (var4 + 1);
         }

         if (super.idSelect == 2) {
            var6 = var2 - this.AB / 4 - 1 + (super.idSelect + var4) * this.AB;
         }

         this.paintSelect(var1, super.AX, var6, super.wDia, var5);
      }

      mFont.tahoma_7b_brown.drawString(var1, T.KW, var3, var2, 0);
      AvMain.AA(var1, " " + T.mAutoFire[this.valueFire], var3 + this.BC, var2, 0, (byte)0, (byte)7);
      var2 += this.AB;
      mFont.tahoma_7b_brown.drawString(var1, T.KX, var3, var2, 0);
      if (value == null) {
         mFont.tahoma_7_black.drawString(var1, " " + T.KY, var3 + this.BD, var2, 0);
      } else {
         for(var5 = 0; var5 < value.length; ++var5) {
            Skill_Info var9 = Skill_Info.getSkillFromID(value[var5][0]);
            int var7 = var3 + this.BD + super.wItem / 2 + (super.wItem + 8) * (var5 % 3);
            int var8 = var2 + super.wItem / 4 + var5 / 3 * this.AB;
            if (super.idSelect == 1 && this.indexBuff == var5) {
               var1.setColor(-1);
               var1.drawRect(var7 - this.AB / 2 - 1, var8 - this.AB / 2 - 1, this.AB + 1, this.AB);
               var1.setColor(0);
               var1.drawRect(var7 - this.AB / 2, var8 - this.AB / 2, this.AB - 2 + 1, this.AB - 2);
            }

            Skill_Info.paintIcon(var1, var7, var8, var9.idIcon, var9.LvDevilSkill);
            if (value[var5][1] == 0) {
               AvMain.fraDelay2.drawFrame(0, var7, var8, 0, 3, var1);
            }
         }
      }

      var2 += this.AB * (var4 + 1);
      var1.drawRegion((mImage)AvMain.imgBorderCombo, var3 + 5, var2 + 5, 3);
      if (this.valueRevice == 1) {
         AvMain.fraCheck.drawFrame(2, var3 + 5, var2 + 5, 0, 3, var1);
      }

      mFont.tahoma_7b_brown.drawString(var1, T.MG, var3 + 15, var2, 0);
      this.paintInfoHelp(var1);
      if (super.cmdList != null) {
         for(var5 = 0; var5 < super.cmdList.size(); ++var5) {
            iCommand var10;
            (var10 = (iCommand)super.cmdList.elementAt(var5)).paint(var1, var10.xCmd, var10.yCmd);
         }
      }

      mGraphics.restoreCanvas();
   }

   public final void paintSelect(mGraphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(-2458);
      var1.fillRect(var2 + super.AU, var3, var4 - (super.AU << 1), var5);
   }

   public final void update() {
      this.updateDialog();
      if (super.isClose) {
         this.closeDialog();
      } else {
         this.updateAnimation();
         if (GameCanvas.isKeyPressed()) {
            this.handleKeyPress();
         }

         this.updatePointer();
         if (GameCanvas.isKeyPressed()) {
            if (super.idSelect == 3) {
               this.BF.AG = true;
               return;
            }

            this.BF.AG = false;
         }

      }
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(1)) {
         if (super.idSelect > 0) {
            --super.idSelect;
         }

         GameCanvas.clearKeyPressed(1);
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.idSelect < 3) {
            ++super.idSelect;
         }

         GameCanvas.clearKeyPressed(3);
      } else if (GameCanvas.isKeyPressed(0)) {
         this.setSelect(-1);
         GameCanvas.clearKeyPressed(0);
      } else if (GameCanvas.isKeyPressed(2)) {
         this.setSelect(1);
         GameCanvas.clearKeyPressed(2);
      } else if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (super.idSelect == 1) {
            if (value != null) {
               if (value[this.indexBuff][1] == 0) {
                  value[this.indexBuff][1] = 1;
               } else {
                  value[this.indexBuff][1] = 0;
               }

               Skill_Info var1;
               if ((var1 = Skill_Info.getSkillFromID(value[this.indexBuff][0])) != null) {
                  this.setInfoHelp(T.mHelpAutoFire[3 + value[this.indexBuff][1]] + var1.name);
               }
            }
         } else if (super.idSelect == 3 && super.cmdList != null && super.AD < super.cmdList.size()) {
            ((iCommand)super.cmdList.elementAt(super.AD)).AD();
         }
      }

      this.AS();
   }

   private void setSelect(int plus) {
      if (super.idSelect == 0) {
         this.valueFire += plus;
         if (this.valueFire < 0) {
            this.valueFire = 0;
         }

         if (this.valueFire >= T.mAutoFire.length) {
            this.valueFire = T.mAutoFire.length - 1;
         }

         String text = T.mHelpAutoFire[this.valueFire];
         Skill_Info skill_Info;
         if (this.valueFire == 1 && (skill_Info = (Skill_Info)Player.vecListSkill.elementAt(0)) != null) {
            text = text + skill_Info.name;
         }

         this.setInfoHelp(text);
      } else {
         if (super.idSelect == 1) {
            if (value != null) {
               this.indexBuff += plus;
               if (this.indexBuff < 0) {
                  this.indexBuff = 0;
               }

               if (this.indexBuff >= value.length) {
                  this.indexBuff = value.length - 1;
                  return;
               }
            }
         } else if (super.idSelect == 2) {
            if (this.valueRevice == 0) {
               this.valueRevice = 1;
            } else {
               this.valueRevice = 0;
            }
            this.setInfoHelp(T.helpAutoRevice);
         }

      }
   }

   public final void updatePointer() {
      int var1 = super.AY;
      int var2 = super.AX + 15;
      var1 += 15 + super.wItem - super.wItem / 4;
      if (GameCanvas.isPointerSelect) {
         if (GameCanvas.isPoint(var2 + 10, var1, super.wDia, this.AB)) {
            ++this.valueFire;
            if (this.valueFire >= T.mAutoFire.length) {
               this.valueFire = 0;
            }

            String var3 = T.mHelpAutoFire[this.valueFire];
            GameCanvas.isPointerSelect = false;
            Skill_Info var4;
            if (this.valueFire == 1 && (var4 = (Skill_Info)Player.vecListSkill.elementAt(0)) != null) {
               var3 = var3 + var4.name;
            }

            this.setInfoHelp(var3);
         }

         int var7;
         if (value == null) {
            var7 = 0;
         } else {
            var7 = (value.length - 1) / 3;
         }

         int var8 = (var7 + 1) * this.AB;
         if (GameCanvas.isPoint(var2 + 10, var1 + this.AB, super.wDia, var8) && value != null) {
            if (value != null) {
               for(var7 = 0; var7 < value.length; ++var7) {
                  int var5 = var2 + this.BD + (super.wItem + 8) * (var7 % 3);
                  int var6 = var1 + this.AB - super.wItem / 4 + var7 / 3 * this.AB;
                  if (GameCanvas.isPoint(var5 - 4, var6, this.AB + 8, this.AB)) {
                     if (value[var7][1] == 0) {
                        value[var7][1] = 1;
                     } else {
                        value[var7][1] = 0;
                     }

                     Skill_Info var9;
                     if ((var9 = Skill_Info.getSkillFromID(value[var7][0])) != null) {
                        this.setInfoHelp(T.mHelpAutoFire[3 + value[var7][1]] + var9.name);
                     }
                  }
               }
            }

            GameCanvas.isPointerSelect = false;
         }

         if (GameCanvas.isPoint(var2, var1 + this.AB + var8, super.wDia, this.AB)) {
            if (this.valueRevice == 0) {
               this.valueRevice = 1;
            } else {
               this.valueRevice = 0;
            }

            this.setInfoHelp(T.helpAutoRevice);
            GameCanvas.isPointerSelect = false;
         }
      }

      super.updatePointer();
   }
}
