public final class ScreenUpgradeSkillDevil extends ScreenUpgrade {
   private mVector CQ = new mVector("ScreenUpgradeSkillDevil.vecSkill");
   private int CR = 34;
   private int CS = 5;
   private LoadImageStatic CT = new LoadImageStatic(0);
   public static ScreenUpgradeSkillDevil AA;

   public ScreenUpgradeSkillDevil() {
      super.BG = 9;
      super.BA = T.PZ;
      super.Step = 0;
      super.AG = 10;
      super.AB = 290;
      super.AC = 180;
      super.AK = super.AB / 2;
      super.AL = super.AC - super.AG * 3;
      super.AD = 24;
      if (GameCanvas.isTouch) {
         super.AD = 28;
      }

      if (super.AB > MotherCanvas.w) {
         super.AB = MotherCanvas.w;
      }

      if (super.AC > MotherCanvas.h - (GameCanvas.hCommand << 1)) {
         super.AC = MotherCanvas.h - (GameCanvas.hCommand << 1);
      }

      super.AE = MotherCanvas.hw - super.AB / 2;
      super.AF = MotherCanvas.hh - super.AC / 2 + 5;
      super.AI = super.AE + 5;
      super.AJ = super.AF + 20;
      super.BF = super.AI + super.AK + 4;
      this.CQ.removeAllElements();

      int var1;
      for(var1 = 0; var1 < Player.vecListSkill.size(); ++var1) {
         Skill_Info var2;
         if ((var2 = (Skill_Info)Player.vecListSkill.elementAt(var1)).Lv_RQ >= 0 && var2.typeSkill == 1) {
            this.CQ.addElement(var2);
         }
      }

      var1 = this.CQ.size() * this.CR - super.AL + 10;
      super.AP = new ListNew(super.AE, super.AF + 30, super.AB, super.AL, 0, 0, var1, true);
      super.AT = new iCommand(T.close, -1, this);
      super.AS = new iCommand(T.GN, 0, this);
      super.AU = new iCommand(T.GK, 1, this);
      if (GameCanvas.isTouch) {
         super.AU.setPos(super.AE + super.AK + (super.AB - super.AG * 3 - super.AK) / 2 + super.AD / 2, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, super.AU.caption);
         super.AT.setPos(super.AE + super.AB - 15, super.AF - 15 + 10 + 8, MainTab.fraCloseTab, "");
         super.AS = AvMain.AA(super.AS, 1);
         super.AQ.addElement(super.AT);
         super.AQ.addElement(super.AU);
         super.AQ.addElement(super.AS);
         if (GameCanvas.isKeyPressed()) {
            super.backCMD = super.AT;
            super.DE = super.AS;
            super.DF = super.AU;
         }
      } else {
         super.DB = super.AT;
         super.DA = super.AS;
         super.center = super.AU;
      }

      int var3 = super.AE + super.AK + (super.AB - super.AG * 3 - super.AK) / 6;
      var1 = super.AF + super.AC / 2 - super.AD / 2;
      super.AN = mSystem.new_M_Int(2, 2);
      super.AN[0][0] = var3;
      super.AN[0][1] = var1;
      super.AN[1][0] = var3 + ((super.AB - super.AG * 3 - super.AK) / 6 << 2);
      super.AN[1][1] = var1;
      this.CT = new LoadImageStatic(super.AK - this.CR - 10);
      this.AV();
   }

   public final void AV() {
      ScreenUpgrade.mItemUpgrade = new MainItem[super.AN.length];
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 0:
         if (super.AH >= 0 && super.AH < this.CQ.size()) {
            Skill_Info var3 = (Skill_Info)this.CQ.elementAt(super.AH);
            GlobalService.getInstance().AB((byte)9, var3.ID, (byte)104, (short)0);
            return;
         }
         break;
      case 1:
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            GlobalService.getInstance().AB((byte)12, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)0);
         }
      }

   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      this.AG(var1);
      this.AL(var1);
      mGraphics var3 = var1;
      ScreenUpgradeSkillDevil var2 = this;
      int var4 = this.CS;
      int var5 = 0;
      int var6 = super.AP.AC / this.CR - 1;
      int var7 = super.AL / this.CR + 2 + var6;
      if (super.AH >= 0 && super.AH < this.CQ.size()) {
         var1.setColor(-2047864);
         var1.fillRect(this.CS + 2, super.AH * this.CR + 2, super.AK - 3, this.CR - 4);
         var1.fillRect(this.CS + super.AK - 1, super.AH * this.CR + 3, 1, this.CR - 5);
      }

      for(int var8 = 0; var8 < var2.CQ.size(); ++var8) {
         if (var8 >= var6 && var8 <= var7) {
            Skill_Info var9;
            String var10 = (var9 = (Skill_Info)var2.CQ.elementAt(var8)).name;
            var9.paint(var3, var4 + var2.CR / 2, var5 + var2.CR / 2);
            if (var8 == 0) {
               var3.setColor(-2243683);
               var3.fillRect(var4 + 1, var5 - 1, var2.AK - 1, 2);
               var3.fillRect(var4 + 1 + var2.AK - 1, var5, 1, 2);
            }

            var3.setColor(-2243683);
            var3.fillRect(var4 + 1, var5 + var2.CR - 1, var2.AK - 1, 2);
            var3.fillRect(var4 + 1 + var2.AK - 1, var5 + var2.CR, 1, 2);
            if (var2.AH == var8 && var2.CT.AF) {
               var3.AD(var4 + var2.CR + var2.CS, var5, var2.CT.AB, var2.CR);
               mFont.tahoma_7b_white.drawString(var3, var10, var4 + var2.CR + var2.CS - var2.CT.AD, var5 + var2.CS / 2 + 1, 0);
               var2.AL(var3);
            } else {
               mFont.tahoma_7b_white.drawString(var3, var10, var4 + var2.CR + var2.CS, var5 + var2.CS / 2 + 1, 0);
            }

            mFont.tahoma_7_black.drawString(var3, T.lvDevil + var9.LvDevilSkill + " + " + var9.phanTramDevilSkill + "%", var4 + var2.CR + var2.CS, var5 + var2.CS / 2 + GameCanvas.hText + 1, 0);
         }

         var5 += var2.CR;
      }

      GameCanvas.resetTrans(var1);
      Interface_Game.paintNumMess(var1, -Interface_Game.xNumMess + 8, -Interface_Game.yNumMess + 3);
      this.AA(var1, -1);
      this.AH(var1);
      this.AB(var1);
      if (super.AQ != null && GameCanvas.AL()) {
         for(int var11 = 0; var11 < super.AQ.size(); ++var11) {
            iCommand var12;
            (var12 = (iCommand)super.AQ.elementAt(var11)).paint(var1, var12.xCmd, var12.yCmd);
         }
      }

      this.AK(var1);
      Interface_Game.AA(var1, MotherCanvas.hw - Interface_Game.BW / 2, 0 + GameScreen.h12plus, true, 0);
      this.AA(var1, 0);
   }

   public final void AB(mGraphics var1) {
      mFont.tahoma_7b_black.drawString(var1, T.NS + "50%", super.BF, super.AJ, 0);
   }

   private void AL(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.AE + 2, super.AF + 20 + 2, super.AK + 4, super.AL - 1 - this.CS - 1 + 5);
      var1.translate(super.AE, super.AF + 25);
      var1.translate(0, -super.AP.AC);
   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      super.AP.AC();

      for(int var1 = 0; var1 < super.vecEff.size(); ++var1) {
         MainEffect var2;
         (var2 = (MainEffect)super.vecEff.elementAt(var1)).update();
         if (var2.isStop) {
            super.vecEff.removeElement(var2);
            --var1;
         }
      }

      this.AP();
      this.CT.AA();
   }

   public final void setStep() {
      this.AP();
   }

   public final void handleKeyPress() {
      if (super.Step == 0) {
         boolean var1 = false;
         if (GameCanvas.isKeyPressed(1)) {
            --super.AH;
            GameCanvas.clearKeyPressed(1);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            ++super.AH;
            GameCanvas.clearKeyPressed(3);
            var1 = true;
         }

         if (var1) {
            int var2 = super.AH * this.CR - super.AL / 2;
            if (super.AH > 0) {
               var2 += this.CR / 2;
            }

            super.AP.AA(var2);
            this.AH();
         }

         this.AG();
         this.AS();
      }
   }

   public final void AH() {
      super.AH = AvMain.AA(super.AH, this.CQ.size() - 1, false);
      if (super.AH >= 0 && super.AH < this.CQ.size()) {
         Skill_Info var1 = (Skill_Info)this.CQ.elementAt(super.AH);
         this.CT.AA(var1.name, mFont.tahoma_7b_black);
      }

   }

   public final void updatePointer() {
      if (super.Step == 0) {
         super.AP.update_Pos_UP_DOWN();
         int var1;
         if (GameCanvas.AB(super.AE, super.AF, super.AK, super.AL)) {
            var1 = (GameCanvas.AZ - super.AJ + super.AP.AC) / this.CR;
            int var2 = this.CQ.size();
            if (var1 >= 0 && var1 < var2) {
               GameCanvas.isPointerSelect = false;
               if (var1 == super.AH) {
                  super.AS.AD();
               } else {
                  super.AO = false;
                  super.AH = var1;
                  this.AH();
               }
            } else {
               super.AO = false;
               super.AH = -1;
            }
         }

         if (super.AQ != null) {
            for(var1 = 0; var1 < super.AQ.size(); ++var1) {
               ((iCommand)super.AQ.elementAt(var1)).AE();
            }
         }

         this.AQ();
      }
   }

   public final void AK() {
      if (ScreenUpgrade.mItemUpgrade[0] != null) {
         Skill_Info var1 = null;

         int var2;
         Skill_Info var3;
         for(var2 = 0; var2 < Player.vecListSkill.size(); ++var2) {
            if ((var3 = (Skill_Info)Player.vecListSkill.elementAt(var2)).ID == ScreenUpgrade.mItemUpgrade[0].ID) {
               var1 = var3;
               break;
            }
         }

         if (var1 != null) {
            for(var2 = 0; var2 < this.CQ.size(); ++var2) {
               if ((var3 = (Skill_Info)this.CQ.elementAt(var2)).ID == ScreenUpgrade.mItemUpgrade[0].ID) {
                  var3.LvDevilSkill = var1.LvDevilSkill;
                  var3.phanTramDevilSkill = var1.phanTramDevilSkill;
                  return;
               }
            }

         }
      }
   }
}
