public final class Class_GK extends LuckyScreen {
   public static Class_GK AA;
   public short AB = -1;
   public short AC = 0;
   public short AD = 0;

   public Class_GK() {
      super.AY = 0;
      super.AM = 260;
      super.AN = 215;
      super.AK = MotherCanvas.hw - super.AM / 2;
      super.AL = MotherCanvas.hh - super.AN / 2;
      super.AQ = super.AK + 100;
      super.AR = super.AL + super.AN / 2 + 12;
      super.AU = (super.AN - 25) / 5 + 5;
      super.AS = super.AK + super.AM - 40;
      super.AT = super.AL + 15 + super.AU;
      LuckyScreen.BF = null;
      this.AF();
      this.AH();
      super.AZ = new iCommand(T.close, -1, this);
      super.cmdQuay = new iCommand("", 2, 1, this);
      super.BB = new iCommand("", 2, 2, this);
      super.cmdQuay.setPos(super.AS, super.AT + (super.AU << 1), super.BD, "");
      super.BB.setPos(super.AS, super.AT + super.AU * 3, super.BE, "");
      super.AI = new mVector();
      super.AI.addElement(super.cmdQuay);
      super.AI.addElement(super.BB);
      super.AI.addElement(super.AZ);
      if (GameCanvas.isTouch) {
         int var1;
         if ((var1 = super.AK + super.AM - 13) > MotherCanvas.w - 9) {
            var1 = MotherCanvas.w - 9;
         }

         super.AZ.setPos(var1, super.AL + 13, MainTab.fraCloseTab, "");
      } else {
         AvMain.AA(super.AZ, 2);
         super.DB = super.AZ;
         super.AO = 1;
         super.cmdQuay.isPlayframe = true;
      }

      this.AJ();
      super.BM = null;
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
      case 1:
         GlobalService.getInstance().AM((byte)4);
         return;
      case 2:
         GlobalService.getInstance().AM((byte)var2);
      case 0:
      default:
      }
   }

   public final void AK() {
      for(int var1 = 0; var1 < LuckyScreen.BJ.size(); ++var1) {
         MainItem var2 = (MainItem)LuckyScreen.BJ.elementAt(var1);
         if (var1 < 8) {
            var2.AC = super.AQ + CRes.getcos(CRes.fixangle(var1 * 360 / 8)) * 66 / 1000;
            var2.AD = super.AR + CRes.getsin(CRes.fixangle(var1 * 360 / 8)) * 66 / 1000;
         } else if (var1 % 2 == 0) {
            var2.AC = super.AQ + 1 + (CRes.getcos(CRes.fixangle(30 + var1 / 2 * 360 / 6)) << 5) / 1000;
            var2.AD = super.AR + (CRes.getsin(CRes.fixangle(30 + var1 / 2 * 360 / 6)) << 5) / 1000;
         } else {
            var2.AC = super.AQ + 1 + (CRes.getcos(CRes.fixangle(210 + var1 / 2 * 360 / 6)) << 5) / 1000;
            var2.AD = super.AR + (CRes.getsin(CRes.fixangle(210 + var1 / 2 * 360 / 6)) << 5) / 1000;
         }
      }

   }

   public final void AF() {
      if (LuckyScreen.BF == null) {
         LuckyScreen.BF = new mImage[6];

         for(int var1 = 0; var1 < LuckyScreen.BF.length; ++var1) {
            if (var1 != 5) {
               LuckyScreen.BF[var1] = mImage.createImage("/interface/lucky" + var1 + ".png");
            }
         }
      }

      if (super.BC == null) {
         super.BC = new FrameImage(mImage.createImage("/interface/lucky8.png"), 37, 27);
         super.BD = new FrameImage(mImage.createImage("/interface/lucky6.png"), 40, 42);
         super.BE = new FrameImage(mImage.createImage("/interface/lucky7.png"), 40, 42);
      }

   }

   private void AH() {
      MainImage var1;
      if (LuckyScreen.BF[5] == null && this.AB != -1 && (var1 = ObjectData.getImageAll(this.AB, ObjectData.HashImageOtherNew, (short)23000)) != null) {
         LuckyScreen.BF[5] = var1.img;
      }

   }

   public final void paint(mGraphics var1) {
      this.AH();
      super.paint(var1);
      mFont.tahoma_7b_black.drawString(var1, "QUAY", super.AS, super.AT + super.AU + 5 - 12, 2);
      AvMain.AB(var1, "" + this.AC, super.AS, super.AT + super.AU + 5, 2, (int)4);
   }

   public final int AL() {
      return this.AD;
   }
}
