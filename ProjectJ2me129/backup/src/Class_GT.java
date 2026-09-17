public class Class_GT extends MainScreen {
   public byte BL;
   public int BM;
   public int BN;
   public int BO;
   public int BP;
   public int BQ;
   private int AA;
   private int AB;

   public Class_GT(byte var1) {
      this.BL = var1;
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public void AA(int var1, mVector var2) {
      if (var2 != null) {
         if (var2.size() > 0) {
            int var3;
            if ((var3 = var2.size()) == 1) {
               this.AA = this.BM + this.BO / 2;
               this.AB = 0;
            } else {
               boolean var10001 = true;
               this.AB = 10;
               this.AA = this.BM + this.BO / 2 - this.AB / 2 - iCommand.wButtonCmd / 2;
            }

            for(int var4 = 0; var4 < var3; ++var4) {
               iCommand var5;
               (var5 = (iCommand)var2.elementAt(var4)).AG = false;
               if (var3 == 3 && var4 == 2) {
                  var5.setPos(this.BM + this.BO / 2, this.BN + this.BP - iCommand.hButtonCmdNor - (var3 - 1) / 2 * (iCommand.hButtonCmdNor + 5) + iCommand.hButtonCmdNor / 2 + 2 + var4 / 2 * (iCommand.hButtonCmdNor + 5) + var1 - 5, (FrameImage)null, var5.caption);
               } else {
                  var5.setPos(this.AA + var4 % 2 * (iCommand.wButtonCmd + this.AB), this.BN + this.BP - iCommand.hButtonCmdNor / 2 - ((var3 - 1) / 2 * iCommand.hButtonCmdNor + 5) + 2 + var4 / 2 * (iCommand.hButtonCmdNor + 5) + var1 - 5, (FrameImage)null, var5.caption);
               }

               if (var4 == 0 && !GameCanvas.isTouch) {
                  var5.AG = true;
               }
            }
         }

      }
   }

   public final void AA(mVector var1) {
      if (var1 != null) {
         if (var1.size() > 0) {
            int var2;
            if ((var2 = var1.size()) == 1) {
               this.AA = this.BM + this.BO / 2;
               this.AB = 0;
            } else {
               boolean var10001 = true;
               this.AB = 10;
               this.AA = this.BM + this.BO / 2 - this.AB / 2 - iCommand.wButtonCmd / 2;
            }

            for(int var3 = 0; var3 < var2; ++var3) {
               iCommand var4;
               (var4 = (iCommand)var1.elementAt(var3)).AG = false;
               if (var2 == 3 && var3 == 2) {
                  var4.setPos(this.BM + this.BO / 2, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var4.caption);
               } else {
                  var4.setPos(this.AA + var3 % 2 * (iCommand.wButtonCmd + this.AB), MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var4.caption);
               }

               if (var3 == 0 && !GameCanvas.isTouch) {
                  var4.AG = true;
               }
            }
         }

      }
   }

   public void commandPointer(int var1, int var2) {
   }

   public void paint(mGraphics var1) {
   }
}
