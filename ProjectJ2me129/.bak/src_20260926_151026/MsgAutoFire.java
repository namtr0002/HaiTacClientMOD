public final class MsgAutoFire extends MsgDialog {
   private int itemStep = 28;
   private int valueRevice = 0;
   public static short[][] value;
   private int indexBuff = 0;
   private iCommand BF;
   private int numCols = 4;

   public final void commandPointer(int var1, int var2) {
      if (var1 == 9) {
         super.isClose = true;
         Player.typeAutoFireMain = 1;
         if (Player.AutoFireCur != 2) {
            Player.AutoFireCur = 1;
         }

         if (value != null) {
            Player.typeAutoBuff = 0;
            for (int i = 0; i < value.length; ++i) {
               if (value[i][1] == 1) {
                  Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
                  if (sk != null && sk.typeSkill == 2) {
                     Player.typeAutoBuff = 1;
                     break;
                  }
               }
            }
         }

         Player.AutoRevice = (byte)this.valueRevice;
         SaveRms.AK();
      }
   }

   public final void AA() {
      super.fontDia = mFont.tahoma_7b_black;
      this.beginDia();
      super.cmdList = new mVector();
      this.BF = new iCommand(T.DA, 9, this);
      super.cmdList.addElement(this.BF);

      super.wDia = 200;
      if (super.wDia > MotherCanvas.w - 10) {
         super.wDia = MotherCanvas.w - 10;
      }

      super.AT = super.wDia;
      super.AS = 5;
      this.itemStep = 28;
      super.wItem = 24;

      int var1 = 0;
      short[] var2 = new short[50];

      if (Player.vecListSkill != null) {
         for (int var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
            Skill_Info var4 = (Skill_Info)Player.vecListSkill.elementAt(var3);
            if (var4 != null && var4.Lv_RQ >= 0 && (var4.typeSkill == 1 || var4.typeSkill == 4 || var4.typeSkill == 0 || var4.typeSkill == 2)) {
               if (var1 < var2.length) {
                  var2[var1] = var4.ID;
                  ++var1;
               }
            }
         }
      }

      short[][] oldValue = value;
      if (var1 > 0) {
         value = new short[var1][];
         for (int var3 = 0; var3 < value.length; ++var3) {
            value[var3] = new short[2];
            value[var3][0] = var2[var3];
            value[var3][1] = 1;
            if (oldValue != null) {
               for (int oldI = 0; oldI < oldValue.length; oldI++) {
                  if (oldValue[oldI][0] == var2[var3]) {
                     value[var3][1] = oldValue[oldI][1];
                     break;
                  }
               }
            }
         }
      }

      this.numCols = (super.wDia - 24) / this.itemStep;
      if (this.numCols < 3) this.numCols = 3;
      if (this.numCols > 5) this.numCols = 5;

      int rows = (var1 > 0) ? ((var1 + this.numCols - 1) / this.numCols) : 1;
      super.hDia = 65 + rows * this.itemStep + 24;
      if (super.hDia > MotherCanvas.h - 10) {
         super.hDia = MotherCanvas.h - 10;
      }

      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 2;
      this.indexBuff = 0;
      this.valueRevice = Player.AutoRevice;
      this.setPosCmdNew(-2, false);
      if (value != null && value.length > 0) {
         Skill_Info firstSk = Skill_Info.getSkillFromID(value[0][0]);
         if (firstSk != null) {
            this.setInfoHelp((value[0][1] == 1 ? "[Dùng] " : "[Tắt] ") + firstSk.name);
         }
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int curY = super.AY;
      int startX = super.AX + 12;

      this.AD(var1, MotherCanvas.hw - super.AS / 2, curY, super.AS, super.hDia, 0);
      var1.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
      mGraphics.AC();
      mGraphics.AD();

      curY += 10;
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + 10, curY, super.wDia - 20, 16, 4, 4);
      curY += 2;
      AvMain.FontBorderColor(var1, "KỸ NĂNG AUTO", super.AX + super.wDia / 2, curY, 2, 6, 5);

      curY += 20;
      mFont.tahoma_7b_brown.drawString(var1, "Chọn chiêu thức tự đánh & buff:", startX, curY, 0);
      curY += 14;

      if (value != null && value.length > 0) {
         int gridStartX = super.AX + (super.wDia - this.numCols * this.itemStep) / 2;
         for (int i = 0; i < value.length; ++i) {
            Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
            if (sk == null) continue;

            int col = i % this.numCols;
            int row = i / this.numCols;
            int iconCenterX = gridStartX + col * this.itemStep + this.itemStep / 2;
            int iconCenterY = curY + row * this.itemStep + this.itemStep / 2;

            if (super.idSelect == 0 && this.indexBuff == i) {
               var1.setColor(0xFFFF00);
               var1.drawRect(iconCenterX - 13, iconCenterY - 13, 25, 25);
               var1.drawRect(iconCenterX - 14, iconCenterY - 14, 27, 27);
            }

            Skill_Info.paintIcon(var1, iconCenterX, iconCenterY, sk.idIcon, sk.LvDevilSkill);

            if (value[i][1] == 0) {
               AvMain.fraDelay2.drawFrame(0, iconCenterX, iconCenterY, 0, 3, var1);
            } else {
               var1.setColor(0x00FF00);
               var1.fillRect(iconCenterX + 4, iconCenterY + 4, 4, 4);
            }
         }
         int rows = (value.length + this.numCols - 1) / this.numCols;
         curY += rows * this.itemStep + 4;
      } else {
         mFont.tahoma_7_black.drawString(var1, "Không có kỹ năng", startX, curY, 0);
         curY += 28;
      }

      // Checkbox Tự hồi sinh
      if (super.idSelect == 1) {
         var1.setColor(-2458);
         var1.fillRect(super.AX + 8, curY - 2, super.wDia - 16, 16);
      }
      var1.drawRegion((mImage)AvMain.imgBorderCombo, startX + 5, curY + 5, 3);
      if (this.valueRevice == 1) {
         AvMain.fraCheck.drawFrame(2, startX + 5, curY + 5, 0, 3, var1);
      }
      mFont.tahoma_7b_brown.drawString(var1, T.MG, startX + 16, curY, 0);

      this.paintInfoHelp(var1);

      if (super.cmdList != null) {
         for (int i = 0; i < super.cmdList.size(); ++i) {
            iCommand cmd = (iCommand)super.cmdList.elementAt(i);
            cmd.paint(var1, cmd.xCmd, cmd.yCmd);
         }
      }

      mGraphics.restoreCanvas();
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
         if (super.idSelect == 2) {
            this.BF.AG = true;
         } else {
            this.BF.AG = false;
         }
      }
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(1)) { // Len
         if (super.idSelect == 2) {
            super.idSelect = 1;
         } else if (super.idSelect == 1) {
            super.idSelect = 0;
            if (value != null && value.length > 0) {
               this.indexBuff = value.length - 1;
            }
         } else if (super.idSelect == 0) {
            if (this.indexBuff >= this.numCols) {
               this.indexBuff -= this.numCols;
            }
         }
         GameCanvas.clearKeyPressed(1);
         updateHelpText();
      } else if (GameCanvas.isKeyPressed(3)) { // Xuong
         if (super.idSelect == 0) {
            if (value != null && this.indexBuff + this.numCols < value.length) {
               this.indexBuff += this.numCols;
            } else {
               super.idSelect = 1;
            }
         } else if (super.idSelect == 1) {
            super.idSelect = 2;
         }
         GameCanvas.clearKeyPressed(3);
         updateHelpText();
      } else if (GameCanvas.isKeyPressed(0)) { // Trai
         if (super.idSelect == 0) {
            if (this.indexBuff > 0) this.indexBuff--;
         } else if (super.idSelect == 1) {
            this.valueRevice = (this.valueRevice == 1) ? 0 : 1;
         }
         GameCanvas.clearKeyPressed(0);
         updateHelpText();
      } else if (GameCanvas.isKeyPressed(2)) { // Phai
         if (super.idSelect == 0) {
            if (value != null && this.indexBuff < value.length - 1) this.indexBuff++;
         } else if (super.idSelect == 1) {
            this.valueRevice = (this.valueRevice == 1) ? 0 : 1;
         }
         GameCanvas.clearKeyPressed(2);
         updateHelpText();
      } else if (GameCanvas.isKeyPressed(5) || GameCanvas.AK[5] || GameCanvas.isKeyPressed(12) || GameCanvas.AK[12]) {
         GameCanvas.clearKeyHold(5);
         GameCanvas.clearKeyHold(12);
         GameCanvas.clearKeyPressed();
         GameCanvas.AH();
         if (super.idSelect == 0) {
            if (value != null && this.indexBuff >= 0 && this.indexBuff < value.length) {
               value[this.indexBuff][1] = (short)(value[this.indexBuff][1] == 1 ? 0 : 1);
               updateHelpText();
            }
         } else if (super.idSelect == 1) {
            this.valueRevice = (this.valueRevice == 1) ? 0 : 1;
            this.setInfoHelp(T.helpAutoRevice);
         } else if (super.idSelect == 2) {
            this.BF.AD();
         }
      }
      this.AS();
   }

   private void updateHelpText() {
      if (super.idSelect == 0 && value != null && this.indexBuff >= 0 && this.indexBuff < value.length) {
         Skill_Info sk = Skill_Info.getSkillFromID(value[this.indexBuff][0]);
         if (sk != null) {
            this.setInfoHelp((value[this.indexBuff][1] == 1 ? "[Dùng] " : "[Tắt] ") + sk.name);
         }
      } else if (super.idSelect == 1) {
         this.setInfoHelp(T.helpAutoRevice);
      }
   }

   public final void updatePointer() {
      if (GameCanvas.isPointerSelect) {
         int curY = super.AY + 10 + 16 + 2 + 20 + 14;
         int startX = super.AX + 12;

         if (value != null && value.length > 0) {
            int gridStartX = super.AX + (super.wDia - this.numCols * this.itemStep) / 2;
            for (int i = 0; i < value.length; ++i) {
               int col = i % this.numCols;
               int row = i / this.numCols;
               int iconBoxX = gridStartX + col * this.itemStep;
               int iconBoxY = curY + row * this.itemStep;

               if (GameCanvas.isPoint(iconBoxX, iconBoxY, this.itemStep, this.itemStep)) {
                  this.indexBuff = i;
                  super.idSelect = 0;
                  value[i][1] = (short)(value[i][1] == 1 ? 0 : 1);
                  updateHelpText();
                  GameCanvas.isPointerSelect = false;
                  return;
               }
            }
            int rows = (value.length + this.numCols - 1) / this.numCols;
            curY += rows * this.itemStep + 4;
         } else {
            curY += 28;
         }

         // Checkbox Tự hồi sinh
         if (GameCanvas.isPoint(startX, curY - 2, super.wDia - 24, 18)) {
            super.idSelect = 1;
            this.valueRevice = (this.valueRevice == 1) ? 0 : 1;
            this.setInfoHelp(T.helpAutoRevice);
            GameCanvas.isPointerSelect = false;
            return;
         }
      }

      super.updatePointer();
   }
}
