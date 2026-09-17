public final class BigBossLittleGraden {
   public int AA;
   private int AJ;
   private int AK;
   private int AL;
   private int AM;
   private int AN = 1;
   private int AO;
   private int AP;
   private int AQ;
   public int AB = 54;
   public int AC;
   private int AR;
   private int AS;
   private int AT = 0;
   public int AD = 10;
   public int AE = 20;
   public int AF = 10;
   public int AG = 10;
   public int AH;
   public String AI = "";
   private mVector AU = new mVector("BigBossLittleGraden.vecEff");
   private int[][][] AV = new int[][][]{{{0, 0, 62}, {1, 130, 58}, {2, 17, 74}, {3, 149, 92}}, {{0, 0, 62}, {3, 140, 32}, {4, 80, 64}, {5, 65, 17}}, {{0, 0, 62}, {2, 17, 74}, {3, 144, 50}, {4, 80, 64}}, {{0, 0, 62}, {1, 130, 58}, {2, 17, 74}, {3, 149, 92}}, {{6, 52, 73}, {7, 82, 70}, {8, 28, 18}, {9, 149, 95}}, {{6, 52, 73}, {9, 149, 95}, {10, -77, 65}, {11, 79, 65}}, {{6, 52, 73}, {9, 149, 95}, {12, 47, 42}, {13, 83, 73}}, {{6, 52, 73}, {9, 149, 95}, {12, 47, 42}, {13, 83, 73}}};

   public BigBossLittleGraden(byte var1) {
      this.AA = var1;
      if (var1 == 1) {
         this.AM = 4;
         this.AJ = MotherCanvas.hw;
         this.AK = -40;
         this.AO = -16;
         this.AP = 2;
         this.AC = 70;
         this.AI = "Brogy";
         this.AR = 10;
         this.AS = 35;
      } else {
         this.AJ = MotherCanvas.hw - 200;
         this.AK = -40;
         this.AO = 16;
         this.AP = -2;
         this.AC = 42;
         this.AI = "Dorry";
         this.AS = 40;
         this.AR = 10;
      }
   }

   public final void AA(mGraphics var1, int var2) {
      int var3;
      for(var3 = 0; var3 < this.AV[this.AM + this.AT].length; ++var3) {
         MainImage var4;
         if ((var4 = ObjectData.getImageOther((short)this.AV[this.AM + this.AT][var3][0], (short)0)) != null && var4.img != null) {
            var1.drawRegion((mImage)var4.img, this.AJ + var2 + this.AV[this.AM + this.AT][var3][1], this.AK + this.AL + this.AV[this.AM + this.AT][var3][2], 0);
         }
      }

      for(var3 = 0; var3 < this.AU.size(); ++var3) {
         ((MainEffect)this.AU.elementAt(var3)).paint(var1, var2, 0);
      }

   }

   public final void AA() {
      int var1;
      for(var1 = 0; var1 < this.AU.size(); ++var1) {
         MainEffect var2;
         (var2 = (MainEffect)this.AU.elementAt(var1)).update();
         if (var2.isStop) {
            this.AU.removeElement(var2);
            --var1;
         }
      }

      if (GameCanvas.gameTick % 5 == 0 && this.AT != 3 || GameCanvas.gameTick % 10 == 0 && this.AT == 3) {
         this.AL += this.AN;
         if (this.AN > 0) {
            if (this.AL >= 4) {
               this.AN = 0;
            }
         } else if (this.AN < 0) {
            if (this.AL <= 0) {
               this.AN = 0;
            }
         } else if (CRes.random(2) == 0) {
            if (this.AL > 0) {
               this.AN = -4;
            } else {
               this.AN = 4;
            }
         }
      }

      if (this.AT == 1) {
         if (this.AQ < 5) {
            this.AJ += this.AO;
            if (this.AQ == 2) {
               for(var1 = 0; var1 < GameScreen.vecBigBossLittleGraden.size(); ++var1) {
                  BigBossLittleGraden var4;
                  if ((var4 = (BigBossLittleGraden)GameScreen.vecBigBossLittleGraden.elementAt(var1)).AA != this.AA) {
                     var4.AA((byte)2);
                     Class_BN var5;
                     if (this.AA == 0) {
                        var4.AU.addElement(GameScreen.AB((short)100, 0, MotherCanvas.hw + 100, 60, (byte)2, (MainObject)null));
                        var4.AU.addElement(GameScreen.AB((short)100, 0, MotherCanvas.hw + 115, 66, (byte)2, (MainObject)null));
                        var5 = new Class_BN("-" + this.AH, MotherCanvas.hw + 100, 60, 2);
                        var4.AU.addElement(var5);
                     } else {
                        var4.AU.addElement(GameScreen.AB((short)100, 0, MotherCanvas.hw - 50, 40, (byte)0, (MainObject)null));
                        var4.AU.addElement(GameScreen.AB((short)100, 0, MotherCanvas.hw - 65, 45, (byte)0, (MainObject)null));
                        var5 = new Class_BN("-" + this.AH, MotherCanvas.hw + 100, 60, 2);
                        var4.AU.addElement(var5);
                     }
                  }
               }
            }
         } else if (this.AQ >= 8 && this.AQ < 12) {
            this.AJ -= this.AO;
         } else if (this.AQ >= 12) {
            this.AT = 0;
            if (this.AA == 1) {
               this.AJ = MotherCanvas.hw;
            } else {
               this.AJ = MotherCanvas.hw - 200;
            }
         }
      } else if (this.AT == 2) {
         if (this.AQ < 3) {
            this.AJ += this.AP;
         } else if (this.AQ < 6) {
            this.AJ -= this.AP;
         } else if (this.AQ >= 6) {
            this.AT = 0;
            if (this.AA == 1) {
               this.AJ = MotherCanvas.hw;
            } else {
               this.AJ = MotherCanvas.hw - 200;
            }
         }
      } else if (this.AT == 0) {
         if (GameCanvas.gameTick % 100 == 0) {
            if (this.AA == 1) {
               this.AJ = MotherCanvas.hw;
            } else {
               this.AJ = MotherCanvas.hw - 200;
            }
         }
      } else if (this.AT == 3 && this.AK < this.AS) {
         this.AR += 2;
         this.AK += this.AR;
         if (this.AK > this.AS) {
            this.AK = this.AS;
         }
      }

      ++this.AQ;
   }

   public final void AA(byte var1) {
      if (this.AT != 3) {
         if (var1 == 3) {
            this.AT = var1;
            this.AQ = 0;
            this.AR = 10;
         } else {
            if (this.AT != 0) {
               if (this.AT != 2) {
                  return;
               }

               if (this.AA == 1) {
                  this.AJ = MotherCanvas.hw;
               } else {
                  this.AJ = MotherCanvas.hw - 200;
               }
            }

            this.AT = var1;
            this.AQ = 0;
         }
      }
   }
}
