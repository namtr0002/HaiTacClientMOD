public final class MapBackGround {
   private byte AC;
   private int[] AD;
   private int[] AE;
   private int[] AF;
   private int[] AG;
   private int AH;
   private int AI;
   private int AJ;
   private int AK = 3;
   private int AL = 150;
   private int AM = 0;
   private int AN = 0;
   public int AA;
   private boolean AO = false;
   private boolean AP;
   public boolean AB = false;
   private mVector AQ = new mVector("MapBackGround.mvecKhangia");
   private mVector AR = new mVector();
   private Point[][] AS;
   private Point AT;
   private short[][] AU = new short[][]{{2, 100, 0}, {3, 160, 0}, {3, 280, 0}};
   private short[][] AV = new short[][]{{2, 40, 10}, {3, 80, 3}, {3, 180, 1}, {2, 300, 14}};
   private short[][] AW = new short[][]{{3, 80, 0}, {2, 140, 0}, {2, 260, 0}, {4, 70, 8}, {5, 20, 1}, {5, 230, 4}, {4, 290, 10}};
   private short[][] AX = new short[][]{{1, 0, 47}, {2, 26, 56}, {3, 62, 75}, {2, 110, 56}, {1, 144, 47}};
   private short[][] AY = new short[][]{{0, 10, 47}, {0, 45, 50}, {0, 105, 54}, {0, 120, 57}, {0, 180, 52}, {0, 210, 48}, {0, 230, 47}, {0, 280, 50}, {0, 310, 52}, {0, 30, 32}, {0, 140, 27}, {0, 205, 30}, {0, 270, 33}};
   private short[][] AZ = new short[][]{{2, 0, -50, -180}, {2, 0, 280, -210}, {2, 2, 200, -200}, {2, 2, 105, -175}, {3, 2, 170, -125}, {3, 2, 20, -240}, {3, 2, 35, -75}, {3, 2, 260, -95}, {5, 0, 20, -235}, {3, 0, 20, -200}, {5, 0, -60, -150}, {0, 0, 110, -174}, {1, 2, 16, -51}, {1, 2, -190, -54}, {1, 0, 270, -56}, {3, 0, -339, -170}, {4, 0, -188, -157}, {3, 2, -65, -115}, {4, 2, 105, -130}, {4, 0, 260, -164}, {4, 0, 300, -260}, {4, 0, 200, -255}};
   private short[][] BA = new short[][]{{0, 0, -127, -165}, {2, 0, 20, -115}, {4, 0, 25, -80}, {2, 2, 260, -135}, {3, 2, 262, -105}, {4, 2, 222, -185}, {4, 0, 160, -65}, {4, 0, -110, -45}, {4, 0, -80, -165}, {5, 2, 56, -195}, {0, 2, 192, -129}, {1, 0, -120, 0}, {1, 2, 110, 0}, {3, 2, 192, -95}, {3, 0, 56, -160}, {3, 0, -127, -130}, {4, 2, -212, -75}};
   private short[][] BB = new short[][]{{5, 0, -30, -12}, {0, 0, 30, -10}, {5, 2, 52, -195}, {3, 2, 52, -160}, {0, 2, 72, -120}, {0, 0, -106, -133}, {2, 0, -139, -190}, {5, 0, -180, -193}, {3, 0, 72, -85}, {3, 0, -106, -100}, {3, 0, -180, -160}, {2, 2, 190, -160}, {4, 0, 200, -120}, {3, 0, 180, -205}, {3, 0, -100, -205}};
   private short[][] BC = new short[][]{{20, -42, 1, 0}, {-24, -79, 0, 1}, {-14, -167, 3, 1}, {26, -178, 2, 0}, {16, -115, 1, 1}};
   private short[][] BD = new short[][]{{120, 10, 3}, {250, -5, 4}, {380, 3, 3}, {500, -10, 3}, {630, 5, 4}, {750, 8, 3}, {900, 0, 4}};
   private short[][] BE = new short[][]{{60, 5, 5}, {190, 0, 6}, {320, -5, 5}, {450, 3, 6}};
   private short[][] BF = new short[][]{{30, 0, 8}, {110, -5, 7}, {200, 5, 7}, {300, 3, 8}};
   private short[][] BG = new short[][]{{0, 2, 52, -160}, {0, 0, 72, -85}, {1, 0, -106, -100}, {0, 0, -180, -160}, {1, 0, 200, -120}, {0, 0, 180, -205}, {1, 0, -100, -205}};
   private short[][] BH;
   private short[][] BI;
   private short[][][] BJ = new short[][][]{{{73, 16}, {55, 13}, {28, 9}, {49, 3}, {40, 1}, {64, -2}}, {{53, 17}, {80, 13}, {57, 4}, {64, 3}, {33, 0}}, {{23, 16}, {65, 15}, {48, 7}, {77, -3}}, {{77, 5}, {55, 4}, {28, 1}, {43, 0}, {62, -2}}};
   private short[] BK = new short[]{16, 30, 40, 65, 76, 84, 98, 108};
   private mImage[] BL;
   private mImage[] BM;
   private mImage[] BN;
   private mImage[] BO;
   private mImage[] BP;
   private mImage BQ;
   private mImage BR;
   private mImage BS;
   private FrameImage BT;
   private FrameImage BU;
   private int BV;
   private int BW;
   private byte BX = -1;
   private static FrameImage BY;
   private static FrameImage BZ;
   private static FrameImage CA;
   private Point[] CB;
   private Point[] CC;
   private int CD;
   private int CE;
   private static int CF = 10;
   private byte CG = 0;
   private Point[] CH;

   public final void AA(byte var1, short var2) {
      this.AR.removeAllElements();
      this.AQ.removeAllElements();
      this.AP = false;
      this.AT = null;
      this.AC = var1;
      if (var1 == 35) {
         this.AA = 700 - var2;
      } else {
         this.AA = GameCanvas.loadmap.maxHMap - var2;
      }

      this.CE = 0;
      this.AB = false;
      this.BP = null;
      int var6;
      if ((GameCanvas.isLowGraOrWP_PvP() || GameCanvas.isOffBg) && this.AH()) {
         if (var2 > 280) {
            this.AA = GameCanvas.loadmap.maxHMap - 280;
         }

         this.BV = -7420929;
         this.BW = -12410121;
         if (LoadMap.idTile != 11 && LoadMap.idTile != 14) {
            this.AD = new int[1];
            this.AD[0] = 96;
            this.AE = new int[this.AD.length];
            this.AE[0] = 96;
            this.AF = new int[this.AD.length];
            this.AF[0] = 8;
            this.BL = new mImage[this.AD.length];
            if (this.AC == 18) {
               this.BL[0] = mImage.createImage("/bg/login3.png");
               this.BV = -10585210;
               this.BW = -14323537;
               this.AB = true;
            } else if (this.AC != 20 && this.AC != 21 && this.AC != 41) {
               this.BL[0] = mImage.createImage("/bg/login1.png");
            } else {
               this.BL[0] = mImage.createImage("/bg/b121.png");
               this.BV = -2759178;
               this.BW = -2759178;
               this.AE[0] = 124;
               if (this.AC == 21) {
                  this.AD[0] = 45;
               } else {
                  this.AD[0] = 55;
               }

               this.AF[0] = 1;
            }

            this.AG = new int[this.AD.length];
            var6 = 0 + this.AD[0];
            this.AG[0] = this.AA - var6;
         } else {
            this.BV = -853254;
            this.BW = -853254;
         }
      } else {
         int var3;
         int var7;
         label556: {
            if (this.BX != this.AC) {
               this.AM = 0;
               int var4;
               label552:
               switch(this.AC) {
               case 0:
                  this.AH = 100;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 75;
                  this.BV = -6434049;
                  this.BW = -16733728;
                  this.AD = new int[3];
                  this.AD[0] = 70;
                  this.AD[1] = -14;
                  this.AD[2] = 55;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 264;
                  this.AE[1] = 264;
                  this.AE[2] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 6;
                  this.AF[2] = 20;
                  this.BL = new mImage[this.AD.length];

                  for(var6 = 0; var6 < this.BL.length; ++var6) {
                     if (var6 == 2) {
                        this.BL[var6] = mImage.createImage("/bg/login1.png");
                     } else {
                        this.BL[var6] = mImage.createImage("/bg/b" + this.AC + var6 + ".png");
                     }
                  }

                  if (BY == null || BZ == null) {
                     BY = new FrameImage(mImage.createImage("/bg/chong.png"), 50, 50);
                     BZ = new FrameImage(mImage.createImage("/bg/chongnho.png"), 24, 24);
                  }

                  this.BO = new mImage[1];
                  this.BO[0] = mImage.createImage("/bg/sea0.png");
                  break label556;
               case 1:
               case 16:
               case 35:
                  this.AH = 74;
                  if (var1 == 35) {
                     this.BR = mImage.createImage("/bg/sky4.png");
                     this.BV = -12160904;
                  } else {
                     this.BR = mImage.createImage("/bg/sky0.png");
                     this.BV = -6434049;
                  }

                  this.AN = 105;
                  this.BW = -16733728;
                  this.AD = new int[2];
                  this.AD[0] = 24;
                  this.AD[1] = 72;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AE[1] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.AF[1] = 4;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/b03.png");
                  this.BL[1] = mImage.createImage("/bg/login1.png");
                  this.AM = -15;
                  break label556;
               case 2:
               case 45:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 55;
                  this.BV = -8467225;
                  this.BW = -12410121;
                  this.AD = new int[2];
                  this.AD[0] = 72;
                  this.AD[1] = -5;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 219;
                  this.AE[1] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  this.BL = new mImage[4];
                  var6 = 0;

                  while(true) {
                     if (var6 >= 4) {
                        break label556;
                     }

                     this.BL[var6] = mImage.createImage("/bg/b2" + var6 + ".png");
                     ++var6;
                  }
               case 3:
                  this.AH = 85;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 75;
                  this.BV = -8467225;
                  this.BW = -9520677;
                  this.AD = new int[3];
                  this.AD[0] = 64;
                  this.AD[1] = -23;
                  this.AD[2] = 40;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 234;
                  this.AE[1] = 32;
                  this.AE[2] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 8;
                  this.AF[2] = 20;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/b10.png");
                  this.BL[1] = mImage.createImage("/bg/b11.png");
                  this.BL[2] = mImage.createImage("/bg/b21.png");
                  break label556;
               case 4:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 75;
                  this.BV = -8467225;
                  this.BW = -12410121;
                  this.AD = new int[2];
                  this.AD[0] = 72;
                  this.AD[1] = -5;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 219;
                  this.AE[1] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  this.BL = new mImage[3];
                  var6 = 0;

                  while(true) {
                     if (var6 >= 3) {
                        break label556;
                     }

                     this.BL[var6] = mImage.createImage("/bg/b2" + var6 + ".png");
                     ++var6;
                  }
               case 5:
                  this.AH = 70;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 55;
                  this.BV = -8467225;
                  this.BW = -9520677;
                  this.AD = new int[2];
                  this.AD[0] = 60;
                  this.AD[1] = 10;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 237;
                  this.AE[1] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 6;
                  this.AF[1] = 20;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/b50.png");
                  this.BL[1] = mImage.createImage("/bg/b21.png");
                  break label556;
               case 6:
                  this.AH = 96;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 35;
                  this.BV = -6434049;
                  this.BW = -7420929;
                  this.AD = new int[1];
                  this.AD[0] = 96;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 20;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/login1.png");
                  break label556;
               case 7:
               case 26:
               case 47:
                  this.AH = 35;
                  this.AN = 80;
                  this.BV = -8856339;
                  this.BW = -8792577;
                  if (this.AC == 26) {
                     this.BV = -14137771;
                     this.BW = -14137771;
                  } else {
                     this.BR = mImage.createImage("/bg/sky2.png");
                  }

                  this.AD = new int[1];
                  this.AD[0] = 35;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 16;
                  if (this.AC == 47) {
                     this.BL = new mImage[3];
                     this.BL[0] = mImage.createImage("/bg/b40.png");
                     this.BL[1] = mImage.createImage("/bg/b40_n.png");
                     this.BL[2] = mImage.createImage("/bg/b23.png");
                  } else {
                     this.BL = new mImage[2];
                     this.BL[0] = mImage.createImage("/bg/b40.png");
                     this.BL[1] = mImage.createImage("/bg/b40_n.png");
                  }

                  this.AM = -40;
                  break label556;
               case 8:
                  this.AH = 96;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 35;
                  this.BV = -6434049;
                  this.BW = -12410121;
                  this.AD = new int[1];
                  this.AD[0] = 96;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 20;
                  this.BL = new mImage[4];
                  this.BL[0] = mImage.createImage("/bg/login1.png");
                  this.BL[3] = mImage.createImage("/bg/b23.png");
                  this.AK = CRes.random(5, 10);
                  this.AL = CRes.random(12, 21) * 10;
                  break label556;
               case 9:
                  this.AH = 96;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 105;
                  this.BV = -6434049;
                  this.BW = -16347222;
                  this.AD = new int[3];
                  this.AD[0] = 24;
                  this.AD[1] = 24;
                  this.AD[2] = 48;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AE[1] = 24;
                  this.AE[2] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 6;
                  this.AF[1] = 12;
                  this.AF[2] = 12;
                  this.BL = new mImage[3];
                  this.BL[2] = mImage.createImage("/bg/login1.png");
                  this.BL[1] = mImage.createImage("/bg/sea0.png");
                  this.BL[0] = mImage.createImage("/bg/b03.png");
                  break label556;
               case 10:
                  this.AH = 74;
                  this.BV = -7420929;
                  this.BW = -16733728;
                  this.AD = new int[2];
                  this.AD[0] = 24;
                  this.AD[1] = 72;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AE[1] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/b03.png");
                  this.BL[1] = mImage.createImage("/bg/login1.png");
                  break label556;
               case 11:
               case 27:
                  this.AH = 107;
                  this.AN = 45;
                  this.BV = -7747590;
                  this.BW = -16733728;
                  if (this.AC == 27) {
                     this.BV = -13219234;
                  } else {
                     this.BR = mImage.createImage("/bg/sky3.png");
                  }

                  this.AD = new int[1];
                  this.AD[0] = 107;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 240;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.BL = new mImage[2];
                  this.BL[0] = mImage.createImage("/bg/b60.png");
                  this.BL[1] = mImage.createImage("/bg/b60_n.png");
                  this.AM = -20;
                  break label556;
               case 12:
               case 13:
               case 14:
               case 24:
               case 25:
                  this.AH = 74;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 105;
                  this.BV = -6434049;
                  this.BW = -16733728;
                  this.AD = new int[2];
                  this.AD[0] = 24;
                  this.AD[1] = 72;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AE[1] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  if (this.AC == 12) {
                     this.BL = new mImage[4];
                     this.BL[0] = mImage.createImage("/bg/b03.png");
                     this.BL[1] = mImage.createImage("/bg/login1.png");
                     this.BL[2] = mImage.createImage("/bg/nui1.png");
                     this.BL[3] = mImage.createImage("/bg/nui2.png");
                     this.BH = this.AU;
                  } else if (this.AC == 13) {
                     this.BL = new mImage[4];
                     this.BL[0] = mImage.createImage("/bg/b03.png");
                     this.BL[1] = mImage.createImage("/bg/login1.png");
                     this.BL[2] = mImage.createImage("/bg/boatnear.png");
                     this.BL[3] = mImage.createImage("/bg/boatfar.png");
                     this.BH = this.AV;
                     if (GameCanvas.loadmap.idMapLoadMap == 191 && CA == null) {
                        CA = new FrameImage(mImage.createImage("/bg/water7.png"), 3);
                     }
                  } else if (this.AC == 14) {
                     this.BL = new mImage[6];
                     this.BL[0] = mImage.createImage("/bg/b03.png");
                     this.BL[1] = mImage.createImage("/bg/login1.png");
                     this.BL[2] = mImage.createImage("/bg/nui1.png");
                     this.BL[3] = mImage.createImage("/bg/nui2.png");
                     this.BL[4] = mImage.createImage("/bg/boatnear.png");
                     this.BL[5] = mImage.createImage("/bg/boatfar.png");
                     this.BH = this.AW;
                  } else if (this.AC == 24) {
                     this.BL = new mImage[6];
                     this.BL[0] = mImage.createImage("/bg/b03.png");
                     this.BL[1] = mImage.createImage("/bg/login1.png");
                     this.BL[2] = mImage.createImage("/bg/nui3.png");
                     this.BL[3] = mImage.createImage("/bg/nui4.png");
                     this.BL[4] = mImage.createImage("/bg/boatnear.png");
                     this.BL[5] = mImage.createImage("/bg/boatfar.png");
                     this.BH = this.AW;
                  } else if (this.AC == 25) {
                     this.BL = new mImage[6];
                     this.BL[0] = mImage.createImage("/bg/b03.png");
                     this.BL[1] = mImage.createImage("/bg/login1.png");
                     this.BL[2] = mImage.createImage("/bg/nui5.png");
                     this.BL[3] = mImage.createImage("/bg/nui6.png");
                     this.BL[4] = mImage.createImage("/bg/boatnear.png");
                     this.BL[5] = mImage.createImage("/bg/boatfar.png");
                     this.BH = this.AW;
                  }
                  break label556;
               case 15:
               case 41:
               case 60:
               case 61:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky6.png");
                  this.AN = 80;
                  this.BV = -8856339;
                  this.BW = -16347222;
                  this.AD = new int[2];
                  this.AD[1] = 30;
                  this.AD[0] = 57;
                  this.AE = new int[this.AD.length];
                  this.AE[1] = 209;
                  this.AE[0] = 125;
                  this.AF = new int[this.AD.length];
                  this.AF[1] = 12;
                  this.AF[0] = 1;
                  this.BL = new mImage[3];
                  if (this.AC != 41 && this.AC != 61) {
                     this.BL[2] = mImage.createImage("/bg/b71.png");
                     this.BL[1] = mImage.createImage("/bg/b21.png");
                     this.BL[0] = mImage.createImage("/bg/b70.png");
                  } else {
                     this.BM = null;
                     this.BV = -6108691;
                     this.BL[2] = mImage.createImage("/bg/b74.png");
                     this.BL[0] = mImage.createImage("/bg/b73.png");
                  }

                  if (this.AC != 60 && this.AC != 61) {
                     byte var9 = 0;
                     if (MotherCanvas.w >= 480) {
                        var9 = 34;
                     } else if (MotherCanvas.w >= 320) {
                        var9 = 24;
                     }

                     for(var3 = 0; var3 <= GameCanvas.loadmap.maxWMap / this.AE[0] + 1; ++var3) {
                        var6 = CRes.random(this.BJ.length);

                        for(var4 = 0; var4 < this.BJ[var6].length; ++var4) {
                           Point_Focus var5;
                           (var5 = new Point_Focus(GameCanvas.loadmap.maxWMap / 125 * 125 - var3 * this.AE[0] + this.BJ[var6][var4][0] + GameCanvas.loadmap.limitW % 125 + var9, this.AA - this.BJ[var6][var4][1])).dis = CRes.random(6) << 1;
                           this.AQ.addElement(var5);
                        }
                     }
                  }
                  break label556;
               case 17:
                  this.AH = 110;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 80;
                  this.BV = -6434049;
                  this.BW = -7420929;
                  this.AD = new int[1];
                  this.AD[0] = 62;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 48;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 1;
                  this.BU = new FrameImage(144, 37, 55);
                  this.BL = new mImage[8];

                  for(var7 = 0; var7 < this.BL.length; ++var7) {
                     if (var7 == 6) {
                        this.BL[var7] = mImage.createImage("/bg/boateff.png");
                     } else if (var7 != 7) {
                        this.BL[var7] = mImage.createImage("/bg/b8" + var7 + ".png");
                     }
                  }

                  this.AT = new Point(140, 0);
                  break label556;
               case 18:
                  this.AB = true;
                  this.BM = null;
                  this.AH = 150;
                  this.BR = mImage.createImage("/bg/sky4.png");
                  this.AN = 70;
                  this.BV = -12160904;
                  this.BW = -11833225;
                  this.AD = new int[2];
                  this.AD[0] = 63;
                  this.AD[1] = -12;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 247;
                  this.AE[1] = 91;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 1;
                  this.AF[1] = 2;
                  this.BL = new mImage[6];

                  for(var7 = 0; var7 < this.BL.length; ++var7) {
                     this.BL[var7] = mImage.createImage("/bg/b9" + var7 + ".png");
                  }

                  if ((var7 = (GameCanvas.loadmap.maxWMap + 30) / 190 + 1) < 2) {
                     var7 = 2;
                  }

                  this.CB = new Point[var7];

                  Point var8;
                  for(var3 = 0; var3 < this.CB.length; ++var3) {
                     (var8 = new Point(-30 + var3 * 190, 0)).frame = (var3 + GameCanvas.loadmap.idMapLoadMap) % 2;
                     if ((var3 + GameCanvas.loadmap.idMapLoadMap) % 4 == 1) {
                        var8.AX = true;
                     }

                     this.CB[var3] = var8;
                  }

                  if ((var7 = (GameCanvas.loadmap.maxWMap - 90) / 190 + 1) < 2) {
                     var7 = 2;
                  }

                  this.CC = new Point[var7];
                  var3 = 0;

                  while(true) {
                     if (var3 >= this.CC.length) {
                        break label556;
                     }

                     var8 = new Point(90 + var3 * 190, 0);
                     if ((var3 + GameCanvas.loadmap.idMapLoadMap) % 4 == 3) {
                        var8.AX = true;
                     }

                     this.CC[var3] = var8;
                     ++var3;
                  }
               case 19:
                  this.AH = 95;
                  this.BR = mImage.createImage("/bg/sky5.png");
                  this.AN = 55;
                  this.BV = -8069639;
                  this.BW = -8792577;
                  this.AD = new int[2];
                  this.AD[0] = 52;
                  this.AD[1] = -12;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 168;
                  this.AE[1] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 8;
                  this.AF[1] = 24;
                  this.BL = new mImage[4];
                  var3 = 0;

                  while(true) {
                     if (var3 >= this.BL.length) {
                        break label556;
                     }

                     this.BL[var3] = mImage.createImage("/bg/b10" + var3 + ".png");
                     ++var3;
                  }
               case 20:
                  this.AH = 120;
                  this.BR = mImage.createImage("/bg/sky6.png");
                  this.AN = 50;
                  this.BV = -6108691;
                  this.BW = -2957848;
                  this.AD = new int[1];
                  this.AD[0] = 135;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 143;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 12;
                  this.BL = new mImage[5];

                  for(var3 = 0; var3 < this.BL.length; ++var3) {
                     this.BL[var3] = mImage.createImage("/bg/b11" + var3 + ".png");
                  }

                  this.BH = this.AX;
                  this.BI = this.AY;
                  this.BQ = mImage.createImage("/bg/b121.png");
                  this.AM = -20;
                  break label556;
               case 21:
                  this.AH = 120;
                  this.BR = mImage.createImage("/bg/sky6.png");
                  this.AN = 110;
                  this.BV = -6108691;
                  this.BW = -2759178;
                  this.AD = new int[2];
                  this.AD[0] = 43;
                  this.AD[1] = 40;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 124;
                  this.AE[1] = 124;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 1;
                  this.AF[1] = 8;
                  this.BL = new mImage[2];

                  for(var3 = 0; var3 < this.BL.length; ++var3) {
                     this.BL[var3] = mImage.createImage("/bg/b12" + var3 + ".png");
                  }

                  this.AM = -20;
                  break label556;
               case 22:
                  this.AH = 120;
                  if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
                     this.BR = mImage.createImage("/bg/sky6.png");
                  }

                  this.AN = 65;
                  this.BV = -6108691;
                  if (GameCanvas.isLowGraOrWP_PvP() || GameCanvas.isOffBg) {
                     this.BV = -2957848;
                  }

                  this.BW = -2957848;
                  this.AD = new int[1];
                  this.AD[0] = 135;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 143;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 12;
                  this.BL = new mImage[6];

                  for(var3 = 0; var3 < this.BL.length; ++var3) {
                     this.BL[var3] = mImage.createImage("/bg/b11" + var3 + ".png");
                  }

                  this.BH = this.AX;
                  this.BI = this.AY;
                  this.BQ = mImage.createImage("/bg/b121.png");
                  this.AM = -20;
                  break label556;
               case 23:
                  this.AH = 95;
                  if (!GameCanvas.lowGraphic && !GameCanvas.isOffBg) {
                     this.BR = mImage.createImage("/bg/sky5.png");
                  }

                  this.AN = 55;
                  this.BV = -8069639;
                  this.BW = -8792577;
                  this.AD = new int[2];
                  this.AD[0] = 52;
                  this.AD[1] = -12;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 168;
                  this.AE[1] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 8;
                  this.AF[1] = 24;
                  this.BL = new mImage[5];
                  var3 = 0;

                  while(true) {
                     if (var3 >= this.BL.length) {
                        break label556;
                     }

                     if (var3 == 4) {
                        this.BL[var3] = mImage.createImage("/bg/b23.png");
                     } else {
                        this.BL[var3] = mImage.createImage("/bg/b10" + var3 + ".png");
                     }

                     ++var3;
                  }
               case 28:
                  this.AH = 70;
                  this.BR = mImage.createImage("/bg/sky7.png");
                  this.AN = 40;
                  this.BV = -16739872;
                  this.BW = -16739872;
                  this.AD = new int[1];
                  this.AD[0] = 66;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 126;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.BL = new mImage[2];
                  this.BL[0] = mImage.createImage("/bg/b131.png");
                  this.BL[1] = mImage.createImage("/bg/b23.png");
                  break;
               case 29:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky8.png");
                  this.AN = 70;
                  this.BV = -16730124;
                  this.BW = -595;
                  this.AD = new int[2];
                  this.AD[0] = 0;
                  this.AD[1] = 20;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 240;
                  this.AE[1] = 190;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.AF[1] = 8;
                  this.BL = new mImage[2];
                  this.BL[0] = mImage.createImage("/bg/b132.png");
                  this.BL[1] = mImage.createImage("/bg/b133.png");
                  this.BM = null;
                  this.AM = -20;
                  break label556;
               case 30:
               case 32:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky8.png");
                  this.AN = 70;
                  this.BV = -16730124;
                  this.BW = -595;
                  this.AD = new int[2];
                  this.AD[0] = 0;
                  this.AD[1] = 20;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 240;
                  this.AE[1] = 190;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/b132.png");
                  this.BL[1] = mImage.createImage("/bg/b133.png");
                  this.BL[2] = mImage.createImage("/bg/b135.png");
                  this.BM = null;
                  this.AM = -20;
                  break label556;
               case 31:
                  this.AH = 70;
                  this.BR = mImage.createImage("/bg/sky7.png");
                  this.AN = 75;
                  this.BV = -16739872;
                  this.BW = -848;
                  this.AD = new int[2];
                  this.AD[0] = 20;
                  this.AD[1] = 20;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 126;
                  this.AE[1] = 190;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.AF[1] = 8;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/b131.png");
                  this.BL[1] = mImage.createImage("/bg/b133.png");
                  break;
               case 33:
                  this.AH = 80;
                  this.BV = -4409135;
                  this.BW = -4409135;
                  if (GameCanvas.loadmap.idMapLoadMap >= 167 && GameCanvas.loadmap.idMapLoadMap <= 176) {
                     this.BV = -15985893;
                     this.BW = -15985893;
                  }

                  this.AD = new int[2];
                  this.AD[0] = 20;
                  this.AD[1] = 10;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 82;
                  this.AE[1] = 45;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 1;
                  this.AF[1] = 1;
                  this.BL = new mImage[2];
                  this.BL[0] = mImage.createImage("/bg/b140.png");
                  this.BL[1] = mImage.createImage("/bg/b141.png");
                  this.BM = null;
                  this.AM = -20;
                  break label556;
               case 34:
               case 48:
               case 49:
               case 50:
               case 52:
               case 54:
               case 56:
               case 57:
               case 58:
               case 59:
               default:
                  this.BV = -7420929;
                  this.BW = -12410121;
                  this.AD = new int[1];
                  this.AD[0] = 96;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 8;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/login1.png");
                  this.AG = new int[this.AD.length];
                  var4 = 0 + this.AD[0];
                  this.AG[0] = this.AA - var4;
                  return;
               case 36:
                  this.AH = 80;
                  this.BR = mImage.createImage("/bg/sky9.png");
                  this.BV = -6823940;
                  this.AN = 75;
                  this.BW = -6823940;
                  this.AD = new int[2];
                  this.AD[0] = 70;
                  this.AD[1] = -47;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 168;
                  this.AE[1] = 72;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 3;
                  this.BL = new mImage[this.AD.length];
                  this.BL[0] = mImage.createImage("/bg/b151.png");
                  this.BL[1] = mImage.createImage("/bg/b150.png");
                  this.AM = -5;
                  break label556;
               case 37:
                  this.AH = 80;
                  this.BR = null;
                  this.BV = -853254;
                  this.AN = 75;
                  this.BW = -6823940;
                  this.AD = new int[1];
                  this.AD[0] = 70;
                  this.AE = new int[1];
                  this.AE[0] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.BL = new mImage[7];

                  for(var4 = 0; var4 < this.BL.length; ++var4) {
                     if (var4 == 6) {
                        this.BL[6] = mImage.createImage("/bg/b151.png");
                     } else {
                        this.BL[var4] = mImage.createImage("/bg/b16" + var4 + ".png");
                        this.BL[var4].setDefault();
                     }
                  }

                  this.AM = -5;
                  break label556;
               case 38:
                  this.AH = 80;
                  this.BR = null;
                  this.BV = -853254;
                  this.AN = 75;
                  this.BW = -6823940;
                  this.AD = new int[1];
                  this.AD[0] = 0;
                  this.AE = new int[1];
                  this.AE[0] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.BL = new mImage[6];

                  for(var4 = 0; var4 < this.BL.length; ++var4) {
                     this.BL[var4] = mImage.createImage("/bg/b16" + var4 + ".png");
                     this.BL[var4].setDefault();
                  }

                  this.AM = -5;
                  break label556;
               case 39:
                  this.AH = 80;
                  this.BR = null;
                  this.BV = -853254;
                  this.AN = 75;
                  this.BW = -6823940;
                  this.AD = new int[1];
                  this.AD[0] = 0;
                  this.AE = new int[1];
                  this.AE[0] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.BL = new mImage[10];

                  for(var4 = 0; var4 < this.BL.length; ++var4) {
                     if (var4 == 6) {
                        this.BL[var4] = mImage.createImage("/bg/b170.png");
                        this.BL[var4].setDefault();
                     } else if (var4 == 7) {
                        this.BL[var4] = mImage.createImage("/bg/b171.png");
                        this.BL[var4].setDefault();
                     } else if (var4 == 8) {
                        this.BL[var4] = mImage.createImage("/bg/b151.png");
                        this.BL[var4].setDefault();
                     } else if (var4 == 9) {
                        this.BL[var4] = mImage.createImage("/bg/b172.png");
                        this.BL[var4].setDefault();
                     } else {
                        this.BL[var4] = mImage.createImage("/bg/b16" + var4 + ".png");
                        this.BL[var4].setDefault();
                     }
                  }

                  this.AM = -5;
                  break label556;
               case 40:
                  this.AH = 96;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 35;
                  this.BV = -6434049;
                  this.BW = -12410121;
                  this.AD = new int[1];
                  this.AD[0] = 96;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 20;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/login1.png");
                  this.BL[1] = mImage.createImage("/bg/b23.png");
                  this.BL[2] = mImage.createImage("/bg/b72.png");
                  this.AK = CRes.random(5, 10);
                  this.AL = CRes.random(12, 21) * 10;
                  this.BH = new short[100][];
                  var6 = 0;

                  for(var7 = 0; var7 <= GameCanvas.loadmap.maxWMap / 120; ++var7) {
                     for(var3 = 0; var3 < this.BK.length; ++var3) {
                        if (CRes.random(5) != 0) {
                           this.BH[var6] = new short[6];
                           this.BH[var6][0] = (short)(this.BK[var3] + CRes.random_Am_0(3) + var7 * 120);
                           this.BH[var6][1] = (short)CRes.random(3);
                           this.BH[var6][2] = (short)CRes.random(6);
                           this.BH[var6][3] = 0;
                           this.BH[var6][4] = 0;
                           this.BH[var6][5] = 0;
                           if (this.BH[var6][0] < GameCanvas.loadmap.maxWMap / 2) {
                              this.BH[var6][5] = 2;
                           }

                           ++var6;
                        }
                     }
                  }

                  this.BH[var6] = null;
                  break label556;
               case 42:
                  this.AH = 70;
                  this.BR = mImage.createImage("/bg/sky7.png");
                  this.AN = 75;
                  this.BV = -16739872;
                  this.BW = -848;
                  this.AD = new int[2];
                  this.AD[0] = 20;
                  this.AD[1] = 20;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 126;
                  this.AE[1] = 190;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.AF[1] = 8;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/b131.png");
                  this.BL[1] = mImage.createImage("/bg/b133.png");
                  this.BL[2] = mImage.createImage("/bg/b72.png");
                  this.BM = null;
                  this.BH = new short[100][];
                  var3 = 0;

                  for(var6 = 0; var6 <= GameCanvas.loadmap.maxWMap / 120; ++var6) {
                     for(var4 = 0; var4 < this.BK.length; ++var4) {
                        if (CRes.random(5) != 0) {
                           this.BH[var3] = new short[6];
                           this.BH[var3][0] = (short)(this.BK[var4] + CRes.random_Am_0(3) + var6 * 120);
                           this.BH[var3][1] = (short)CRes.random(3);
                           this.BH[var3][2] = (short)CRes.random(6);
                           this.BH[var3][3] = 0;
                           this.BH[var3][4] = 0;
                           this.BH[var3][5] = 0;
                           if (this.BH[var3][0] < GameCanvas.loadmap.maxWMap / 2) {
                              this.BH[var3][5] = 2;
                           }

                           ++var3;
                        }
                     }
                  }

                  this.BH[var3] = null;
                  break label556;
               case 43:
               case 62:
                  this.AH = 70;
                  this.BR = null;
                  this.AN = 75;
                  this.BV = -11182530;
                  this.BW = -11182530;
                  this.AD = new int[3];
                  this.AD[0] = 15;
                  this.AD[1] = 15;
                  this.AD[2] = 15;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 211;
                  this.AE[1] = 211;
                  this.AE[2] = 211;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 1;
                  this.AF[1] = 3;
                  this.AF[2] = 12;
                  this.BL = new mImage[9];
                  var6 = 0;

                  while(true) {
                     if (var6 >= this.BL.length) {
                        break label552;
                     }

                     this.BL[var6] = mImage.createImage("/bg/b18" + var6 + ".png");
                     ++var6;
                  }
               case 44:
                  this.AH = 100;
                  this.BR = mImage.createImage("/bg/sky9.png");
                  this.AN = 130;
                  this.BV = -6823940;
                  this.BW = -6823940;
                  this.AD = new int[3];
                  this.AD[0] = 24;
                  this.AD[1] = 20;
                  this.AD[2] = 40;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 168;
                  this.AE[1] = 212;
                  this.AE[2] = 250;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 2;
                  this.AF[1] = 5;
                  this.AF[2] = 8;
                  this.BL = new mImage[6];
                  var6 = 0;

                  while(true) {
                     if (var6 >= this.BL.length) {
                        break label552;
                     }

                     if (var6 == 3) {
                        this.BL[var6] = mImage.createImage("/bg/b170.png");
                        this.BL[var6].setDefault();
                     } else if (var6 == 4) {
                        this.BL[var6] = mImage.createImage("/bg/b171.png");
                        this.BL[var6].setDefault();
                     } else if (var6 == 5) {
                        this.BL[var6] = mImage.createImage("/bg/b172.png");
                        this.BL[var6].setDefault();
                     } else {
                        this.BL[var6] = mImage.createImage("/bg/b19" + var6 + ".png");
                     }

                     ++var6;
                  }
               case 46:
                  this.AH = 80;
                  this.BR = null;
                  this.BV = -853254;
                  this.AN = 75;
                  this.BW = -6823940;
                  this.AD = new int[1];
                  this.AD[0] = 0;
                  this.AE = new int[1];
                  this.AE[0] = 168;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.BL = new mImage[2];
                  var4 = 0;

                  while(true) {
                     if (var4 >= this.BL.length) {
                        break label556;
                     }

                     this.BL[var4] = mImage.createImage("/bg/b16" + (var4 + 3) + ".png");
                     this.BL[var4].setDefault();
                     ++var4;
                  }
               case 51:
                  this.AH = 74;
                  this.BR = mImage.createImage("/bg/sky0.png");
                  this.AN = 105;
                  this.BV = -6434049;
                  this.BW = -16733728;
                  this.AD = new int[2];
                  this.AD[0] = 24;
                  this.AD[1] = 72;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 96;
                  this.AE[1] = 96;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 12;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/b03.png");
                  this.BL[1] = mImage.createImage("/bg/login1.png");
                  this.BL[2] = mImage.createImage("/bg/b23.png");
                  break label556;
               case 53:
                  this.AH = 85;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 65;
                  this.BV = -8467225;
                  this.BW = -9520677;
                  this.AD = new int[3];
                  this.AD[0] = 64;
                  this.AD[1] = -30;
                  this.AD[2] = 40;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 234;
                  this.AE[1] = 32;
                  this.AE[2] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 4;
                  this.AF[1] = 8;
                  this.AF[2] = 20;
                  this.BL = new mImage[4];
                  this.BL[0] = mImage.createImage("/bg/b10.png");
                  this.BL[1] = mImage.createImage("/bg/b11.png");
                  this.BL[2] = mImage.createImage("/bg/b21.png");
                  this.BL[3] = mImage.createImage("/bg/b23.png");
                  break label556;
               case 55:
                  this.AH = 70;
                  this.BR = mImage.createImage("/bg/sky1.png");
                  this.AN = 55;
                  this.BV = -8467225;
                  this.BW = -9520677;
                  this.AD = new int[2];
                  this.AD[0] = 60;
                  this.AD[1] = 10;
                  this.AE = new int[this.AD.length];
                  this.AE[0] = 237;
                  this.AE[1] = 209;
                  this.AF = new int[this.AD.length];
                  this.AF[0] = 6;
                  this.AF[1] = 20;
                  this.BL = new mImage[3];
                  this.BL[0] = mImage.createImage("/bg/b50.png");
                  this.BL[1] = mImage.createImage("/bg/b21.png");
                  this.BL[2] = mImage.createImage("/bg/b23.png");
                  break label556;
               case 63:
                  this.AH = 80;
                  var6 = 0;
                  switch(GameCanvas.loadmap.idMapLoadMap) {
                  case 195:
                     var6 = -4556118;
                     break;
                  case 196:
                     var6 = -4084068;
                     break;
                  case 197:
                     var6 = -1577027;
                     break;
                  case 198:
                     var6 = -12500123;
                  }

                  this.BV = var6;
                  this.BW = var6;
                  if (GameCanvas.loadmap.idMapLoadMap == 197) {
                     this.AD = new int[2];
                     this.AD[0] = 95;
                     this.AD[1] = 140;
                     this.AE = new int[this.AD.length];
                     this.AE[0] = 99;
                     this.AE[1] = 99;
                     this.AF = new int[this.AD.length];
                     this.AF[0] = 1;
                     this.AF[1] = 1;
                     this.BL = new mImage[this.AD.length];
                     this.BL[0] = mImage.createImage("/bg/b" + (GameCanvas.loadmap.idMapLoadMap - 2) + ".png");
                     this.BL[1] = mImage.createImage("/bg/b" + (GameCanvas.loadmap.idMapLoadMap - 2) + ".png");
                  } else {
                     this.AD = new int[1];
                     this.AD[0] = 60;
                     this.AE = new int[this.AD.length];
                     this.AE[0] = 25;
                     this.AF = new int[this.AD.length];
                     this.AF[0] = 1;
                     this.BL = new mImage[1];
                     this.BL[0] = mImage.createImage("/bg/b" + (GameCanvas.loadmap.idMapLoadMap - 2) + ".png");
                  }

                  if (GameCanvas.loadmap.idMapLoadMap == 198) {
                     this.AE[0] = 124;
                  }

                  this.BM = null;
                  this.AM = -20;
                  break label556;
               }
            } else if (this.BX != 28 && this.BX != 29 && this.BX != 30) {
               if (this.AC != 28 && this.AC != 29 && this.AC != 30) {
                  break label556;
               }
            } else if (this.AC == 28 || this.AC == 29 || this.AC == 30) {
               break label556;
            }

            this.BM = null;
         }

         this.AI = this.AA - this.AH + this.AM;
         var6 = 0;
         this.AG = new int[this.AD.length];

         for(var7 = 0; var7 < this.AD.length; ++var7) {
            var6 += this.AD[var7];
            this.AG[var7] = this.AA - var6;
         }

         var7 = GameCanvas.loadmap.maxWMap / 250;
         if ((!GameCanvas.lowGraphic || this.AC == 33) && this.AC != 35 && this.AC != 43 && this.AC != 62) {
            this.AS = new Point[3][];

            for(var3 = 0; var3 < this.AS.length; ++var3) {
               this.AS[var3] = new Point[var7 + 1];

               for(var6 = 0; var6 < this.AS[var3].length; ++var6) {
                  this.AS[var3][var6] = new Point();
                  this.AS[var3][var6].x = CRes.random(GameCanvas.loadmap.maxWMap) * 100;
                  this.AS[var3][var6].y = this.AI - 60 + var3 * 30 + (var3 == 1 ? 5 : 0) + CRes.random_Am_0(10);
                  this.AS[var3][var6].vx = -60 / ((var3 << 2) + 1);
                  this.AS[var3][var6].frame = var3 % 3;
               }
            }
         }

         this.BX = this.AC;
      }
   }

   private boolean AH() {
      return this.AC != 8 && this.AC != 51 && this.AC != 17 && this.AC != 23 && this.AC != 22;
   }

   public final void paint(mGraphics var1) {
      if (MainScreen.cameraMain.yCam < 0) {
         var1.setColor(this.BV);
         var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
      }

      int var2;
      if ((GameCanvas.isLowGraOrWP_PvP() || GameCanvas.isOffBg) && this.AH()) {
         if (MainScreen.cameraMain.yCam + this.CE <= this.AA) {
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
         }

         if (LoadMap.idTile != 11 && LoadMap.idTile != 14) {
            for(var2 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var2 < MotherCanvas.w + this.AE[0]; var2 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[0] + this.CE, 0);
            }

            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + this.CE + 96, MotherCanvas.w, 40);
         }
      } else {
         if (this.AC != 20) {
            if (this.AC != 8 && this.AC != 40) {
               if (MainScreen.cameraMain.yCam - this.CE < this.AG[this.AG.length - 1]) {
                  var1.setColor(this.BV);
                  var1.fillRect(MainScreen.cameraMain.xCam - GameScreen.dx, MainScreen.cameraMain.yCam - GameScreen.dy, MotherCanvas.w + GameScreen.dx, this.AG[this.AG.length - 1] - MainScreen.cameraMain.yCam + this.CE + GameScreen.dy + 10);
               }
            } else if (MainScreen.cameraMain.yCam - this.CE < this.AG[this.AG.length - 1]) {
               var1.setColor(this.BV);
               var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam - this.AJ, MotherCanvas.w, this.AG[this.AG.length - 1] - MainScreen.cameraMain.yCam + this.AJ + this.CE);
            }
         }

         if (this.AC != 20 && this.AC != 21 && this.AC != 22) {
            this.AF(var1);
         }

         int var3;
         int var4;
         int var5;
         switch(this.AC) {
         case 0:
            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
                  if (var3 == 0) {
                     BY.drawFrame(GameCanvas.gameTick / 3 % BY.nFrame, MainScreen.cameraMain.xCam + MotherCanvas.w - var2 + 187, this.AG[var3] + 7 + this.CE, 0, 3, var1);
                  }

                  if (var3 == 1) {
                     BZ.drawFrame(GameCanvas.BJ % BY.nFrame, MainScreen.cameraMain.xCam + MotherCanvas.w - var2 + 243, this.AG[var3] + 4 + this.CE, 0, 3, var1);
                  }
               }
            }

            this.AG(var1);
            return;
         case 1:
         case 3:
         case 5:
         case 6:
         case 10:
         case 35:
            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 2:
         case 4:
         case 45:
            this.AG(var1);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
            }

            if (this.AC != 45) {
               var2 = MainScreen.cameraMain.xCam + 120 - MainScreen.cameraMain.xCam / 16;
               var1.drawRegion((mImage)this.BL[2], var2, this.AA - 112 + this.CE, 0);
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            return;
         case 7:
         case 26:
         case 47:
            this.AG(var1);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] - 18 + this.CE, 0);
               if (this.AC == 26) {
                  var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] - 18 + this.CE, 0);
               }
            }

            return;
         case 8:
            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] - this.AJ + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 9:
            this.AG(var1);

            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  if (var3 == 1) {
                     var1.drawRegion(this.BL[var3], 0, 24 * (GameCanvas.gameTick / 8 % 2), 24, 24, 0, MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
                  } else {
                     var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
                  }
               }
            }

            return;
         case 11:
         case 27:
            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
               if (this.AC == 27) {
                  var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 12:
         case 13:
         case 14:
         case 24:
         case 25:
            for(var3 = this.AD.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            if (this.BH != null) {
               var2 = MainScreen.cameraMain.xCam - MainScreen.cameraMain.xCam / this.AF[this.AF.length - 1];

               for(var3 = 0; var3 < this.BH.length; ++var3) {
                  var1.drawRegion((mImage)this.BL[this.BH[var3][0]], var2 + this.BH[var3][1], this.AA - 48 + this.CE + this.BH[var3][2], 33);
               }
            }

            if (this.AC == 13 && GameCanvas.loadmap.idMapLoadMap == 191) {
               CA.drawFrame(GameCanvas.gameTick / 3 % CA.nFrame, MainScreen.cameraMain.xCam + MotherCanvas.w / 2 - MainScreen.cameraMain.xCam / 4, 45 + this.CE, 0, 3, var1);
               return;
            }
            break;
         case 15:
         case 41:
         case 60:
         case 61:
            if (this.AC == 15 || this.AC == 60) {
               for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
                  var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
               }
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0] + GameCanvas.loadmap.maxWMap % 125; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3 + GameCanvas.loadmap.maxWMap % 125, this.AG[0] + this.CE, 0);
            }

            if (this.AC != 60 && this.AC != 61) {
               for(var3 = 0; var3 < this.AQ.size(); ++var3) {
                  Point_Focus var6;
                  if ((var6 = (Point_Focus)this.AQ.elementAt(var3)).x > MainScreen.cameraMain.xCam - 25 && var6.x < MainScreen.cameraMain.xCam + MotherCanvas.w + 25) {
                     var1.drawRegion(this.BL[2], 0, (var6.dis + var6.frame) * 15, 25, 15, 0, var6.x, var6.y, 33);
                  }
               }
            }

            this.AG(var1);
            return;
         case 16:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            this.AG(var1);
            return;
         case 17:
            if (MainScreen.cameraMain.xCam < 220) {
               var1.drawRegion((mImage)this.BL[5], 0, this.AG[0] - 85, 0);
            }

            if (this.AT != null) {
               if (this.AT.f >= 20 && this.AT.f <= 27 && this.BU != null) {
                  this.BU.drawFrame((this.AT.f - 20) / 4 % 2, this.AT.x + 4, this.AG[0] + this.AT.y + 12, 0, 3, var1);
               }

               var1.drawRegion((mImage)this.BL[4], this.AT.x, this.AG[0] + this.AT.y, 0);
               if (this.AT.f < 120) {
                  var1.drawRegion(this.BL[6], 0, this.AT.f / 4 % 2 * 12, 78, 12, 0, this.AT.x, this.AG[0] - 2, 0);
               }
            }

            if (MainScreen.cameraMain.xCam < 96) {
               var1.drawRegion((mImage)this.BL[1], 0, this.AG[0] - 30, 0);
            }

            for(var3 = 0; var3 < GameCanvas.loadmap.maxWMap - 180; var3 += 96) {
               if (var3 + 180 >= MainScreen.cameraMain.xCam - 96 && var3 + 180 <= MainScreen.cameraMain.xCam + MotherCanvas.w) {
                  if (var3 == 0) {
                     var1.drawRegion((mImage)this.BL[3], var3 + 180, this.AG[0] - 30, 0);
                  } else {
                     var1.drawRegion((mImage)this.BL[2], var3 + 180, this.AG[0] - 30, 0);
                  }
               }
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0], 0);
            }

            this.AG(var1);
            return;
         case 18:
            var2 = MainScreen.cameraMain.xCam / 8;

            for(var3 = 0; var3 < this.CC.length; ++var3) {
               if (this.CC[var3].x + var2 >= MainScreen.cameraMain.xCam - 145 && this.CC[var3].x + var2 <= MainScreen.cameraMain.xCam + MotherCanvas.w && !this.CC[var3].AX) {
                  var1.drawRegion((mImage)this.BL[4], this.CC[var3].x + var2, this.AG[0] + this.CC[var3].y - 100 + 45 + this.CE, 0);
               }
            }

            var2 = MainScreen.cameraMain.xCam / 6;

            for(var3 = 0; var3 < this.CB.length; ++var3) {
               if (this.CB[var3].x + var2 >= MainScreen.cameraMain.xCam - 145 && this.CB[var3].x + var2 <= MainScreen.cameraMain.xCam + MotherCanvas.w && !this.CB[var3].AX) {
                  var1.drawRegion((mImage)this.BL[2 + this.CB[var3].frame], this.CB[var3].x + var2, this.AG[0] + this.CB[var3].y - 100 + 35 + this.CE, 0);
               }
            }

            for(var3 = 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            var2 = MainScreen.cameraMain.xCam + 100 - MainScreen.cameraMain.xCam / 16;
            var1.drawRegion((mImage)this.BL[5], var2 + MotherCanvas.w / 3, this.AG[0] - 120, 0);
            this.AH(var1);
            return;
         case 19:
         case 23:
            if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
               this.AG(var1);
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
            }

            if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
               var2 = MainScreen.cameraMain.xCam + 60 - MainScreen.cameraMain.xCam / 12;
               var1.drawRegion((mImage)this.BL[3], var2 + 120, this.AA - 70 + this.CE, 0);
               var1.drawRegion((mImage)this.BL[2], var2, this.AA - 90 + this.CE, 0);
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            return;
         case 20:
         case 22:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            this.AF(var1);
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 30, MotherCanvas.w, 100);
            if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
               var2 = MainScreen.cameraMain.xCam + 80 - MainScreen.cameraMain.xCam / 24;

               for(var3 = 0; var3 < this.BH.length; ++var3) {
                  var1.drawRegion((mImage)this.BL[this.BH[var3][0]], var2 + this.BH[var3][1], this.AA - this.BH[var3][2] - 100 + this.CE, 0);
               }

               for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
                  var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
               }

               var2 = MainScreen.cameraMain.xCam - MainScreen.cameraMain.xCam / 12;

               for(var3 = 0; var3 < this.BI.length; ++var3) {
                  var1.drawRegion((mImage)this.BL[4], var2 + this.BI[var3][1], this.AA - this.BI[var3][2] + this.CE - 45, 0);
               }
            }

            for(var3 = MainScreen.cameraMain.xCam % 124; var3 < MotherCanvas.w + 124; var3 += 124) {
               var1.drawRegion((mImage)this.BQ, MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + 80, 0);
            }

            if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
               this.AG(var1);
               return;
            }
            break;
         case 21:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            this.AF(var1);
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[1] + this.CE + 4, MotherCanvas.w, 100);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
            }

            var2 = MainScreen.cameraMain.xCam + 200 - MainScreen.cameraMain.xCam / 8;
            var1.drawRegion((mImage)this.BL[0], var2, this.AA - 35 + this.CE, 33);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 28:
            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 29:
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 20, MotherCanvas.w, 100);

            for(var3 = 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 30:
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 10, MotherCanvas.w, 100);

            for(var3 = 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            var2 = MainScreen.cameraMain.xCam + 160 - MainScreen.cameraMain.xCam / 4;
            var1.drawRegion((mImage)this.BL[2], var2, this.AA - 75 + this.CE, 0);
            var1.drawRegion(this.BL[2], 0, 0, 92, 125, 2, var2 + 92, this.AA - 75 + this.CE, 0);
            this.AG(var1);
            return;
         case 31:
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 10, MotherCanvas.w, 100);

            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 32:
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 10, MotherCanvas.w, 100);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
            }

            var2 = MainScreen.cameraMain.xCam + 160 - MainScreen.cameraMain.xCam / 8;
            var1.drawRegion((mImage)this.BL[2], var2, this.AA - 85 + this.CE, 0);
            var1.drawRegion(this.BL[2], 0, 0, 92, 125, 2, var2 + 92, this.AA - 85 + this.CE, 0);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 33:
            if (GameCanvas.loadmap.idMapLoadMap < 167 || GameCanvas.loadmap.idMapLoadMap > 176) {
               for(var3 = 1; var3 >= 0; --var3) {
                  for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                     var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
                  }
               }

               return;
            }
         case 34:
         case 48:
         case 49:
         case 50:
         case 52:
         case 54:
         case 56:
         case 57:
         case 58:
         case 59:
         default:
            break;
         case 36:
            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 37:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            var2 = MainScreen.cameraMain.xCam + 120 - MainScreen.cameraMain.xCam / 8;

            for(var3 = 0; var3 < this.AZ.length; ++var3) {
               var1.drawRegion(this.BL[this.AZ[var3][0]], 0, 0, this.BL[this.AZ[var3][0]].width, this.BL[this.AZ[var3][0]].height, this.AZ[var3][1], var2 + this.AZ[var3][2], this.AA + this.CE + this.AZ[var3][3], 3);
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[6], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 38:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            var2 = MainScreen.cameraMain.xCam + 120 - MainScreen.cameraMain.xCam / 8;

            for(var3 = 0; var3 < this.BA.length; ++var3) {
               var1.drawRegion(this.BL[this.BA[var3][0]], 0, 0, this.BL[this.BA[var3][0]].width, this.BL[this.BA[var3][0]].height, this.BA[var3][1], var2 + this.BA[var3][2], this.AA + this.CE + this.BA[var3][3], 3);
            }

            this.AG(var1);
            return;
         case 39:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            var2 = MainScreen.cameraMain.xCam + 180 - MainScreen.cameraMain.xCam / 8;

            for(var3 = 0; var3 < this.BC.length; ++var3) {
               if (this.BC[var3][3] == 0) {
                  var1.drawRegion(this.BL[9], 0, this.BC[var3][2] * 18, 18, 18, 0, var2 + this.BC[var3][0], this.AA + this.CE + this.BC[var3][1], 3);
               }
            }

            for(var3 = 0; var3 <= 5; ++var3) {
               if (var3 == 0) {
                  var1.drawRegion(this.BL[6], 0, 0, this.BL[6].width, this.BL[6].height, 0, var2, this.AA + this.CE - 23 - var3 * 47 + 30, 3);
               } else {
                  var1.drawRegion(this.BL[7], 0, 0, this.BL[7].width, this.BL[7].height, 0, var2, this.AA + this.CE - 23 - var3 * 47 + 30, 3);
               }
            }

            for(var3 = 0; var3 < this.BC.length; ++var3) {
               if (this.BC[var3][3] == 1) {
                  var1.drawRegion(this.BL[9], 0, this.BC[var3][2] * 18, 18, 18, 0, var2 + this.BC[var3][0], this.AA + this.CE + this.BC[var3][1], 3);
               }
            }

            for(var3 = 0; var3 < this.BB.length; ++var3) {
               var1.drawRegion(this.BL[this.BB[var3][0]], 0, 0, this.BL[this.BB[var3][0]].width, this.BL[this.BB[var3][0]].height, this.BB[var3][1], var2 + this.BB[var3][2], this.AA + this.CE + this.BB[var3][3] + 30, 3);
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[8], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            this.AG(var1);
            return;
         case 40:
            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] - this.AJ + this.CE, 0);
            }

            this.AG(var1);

            for(var3 = 0; var3 < this.BH.length; ++var3) {
               if (this.BH[var3] == null) {
                  return;
               }

               if (12 + this.BH[var3][0] > MainScreen.cameraMain.xCam - 17 && 12 + this.BH[var3][0] < MainScreen.cameraMain.xCam + MotherCanvas.w + 17) {
                  var1.drawRegion(this.BL[2], this.BH[var3][2] * 17, this.BH[var3][3] * 30, 17, 30, this.BH[var3][5], this.BH[var3][0], this.AG[0] + 73 + this.BH[var3][1], 33);
               }
            }

            return;
         case 42:
            var1.setColor(this.BW);
            var1.fillRect(MainScreen.cameraMain.xCam, this.AG[0] + 10, MotherCanvas.w, 100);

            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 43:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);

            for(var3 = MainScreen.cameraMain.xCam / this.AF[2] % this.AE[2]; var3 < MotherCanvas.w + this.AE[2]; var3 += this.AE[2]) {
               var1.drawRegion((mImage)this.BL[2], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[2] + this.CE, 0);
            }

            var2 = MainScreen.cameraMain.xCam - MainScreen.cameraMain.xCam / 12;

            for(var3 = 0; var3 < this.BF.length; ++var3) {
               if (var2 + this.BF[var3][0] >= MainScreen.cameraMain.xCam - 120 && var2 + this.BF[var3][0] <= MainScreen.cameraMain.xCam + MotherCanvas.w + 120) {
                  var1.drawRegion((mImage)this.BL[this.BF[var3][2]], var2 + this.BF[var3][0], this.AG[2] + this.CE + 40 + this.BF[var3][1], 33);
               }
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[1] % this.AE[1]; var3 < MotherCanvas.w + this.AE[1]; var3 += this.AE[1]) {
               var1.drawRegion((mImage)this.BL[1], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[1] + this.CE, 0);
            }

            var2 = MainScreen.cameraMain.xCam - MainScreen.cameraMain.xCam / 3;

            for(var3 = 0; var3 < this.BE.length; ++var3) {
               if (var2 + this.BE[var3][0] >= MainScreen.cameraMain.xCam - 120 && var2 + this.BE[var3][0] <= MainScreen.cameraMain.xCam + MotherCanvas.w + 120) {
                  var1.drawRegion((mImage)this.BL[this.BE[var3][2]], var2 + this.BE[var3][0], this.AG[1] + this.CE + 40 + this.BE[var3][1], 33);
               }
            }

            for(var3 = MainScreen.cameraMain.xCam / this.AF[0] % this.AE[0]; var3 < MotherCanvas.w + this.AE[0]; var3 += this.AE[0]) {
               var1.drawRegion((mImage)this.BL[0], MainScreen.cameraMain.xCam + MotherCanvas.w - var3, this.AG[0] + this.CE, 0);
            }

            var2 = MainScreen.cameraMain.xCam - MainScreen.cameraMain.xCam;

            for(var3 = 0; var3 < this.BD.length; ++var3) {
               if (var2 + this.BD[var3][0] >= MainScreen.cameraMain.xCam - 120 && var2 + this.BD[var3][0] <= MainScreen.cameraMain.xCam + MotherCanvas.w + 120) {
                  var1.drawRegion((mImage)this.BL[this.BD[var3][2]], var2 + this.BD[var3][0], this.AG[0] + this.CE + 40 + this.BD[var3][1], 33);
               }
            }

            this.AG(var1);
            return;
         case 44:
            var2 = MainScreen.cameraMain.xCam + 180 - MainScreen.cameraMain.xCam / 12;

            for(var4 = 0; var4 < this.BC.length; ++var4) {
               if (this.BC[var4][3] == 0) {
                  var1.drawRegion(this.BL[5], 0, this.BC[var4][2] * 18, 18, 18, 0, var2 + this.BC[var4][0], this.AA + this.CE + this.BC[var4][1], 3);
               }
            }

            for(var4 = 0; var4 <= 5; ++var4) {
               if (var4 == 0) {
                  var1.drawRegion(this.BL[3], 0, 0, this.BL[3].width, this.BL[3].height, 0, var2, this.AA + this.CE - 23 - var4 * 47 + 30, 3);
               } else {
                  var1.drawRegion(this.BL[4], 0, 0, this.BL[4].width, this.BL[4].height, 0, var2, this.AA + this.CE - 23 - var4 * 47 + 30, 3);
               }
            }

            for(var4 = 0; var4 < this.BC.length; ++var4) {
               if (this.BC[var4][3] == 1) {
                  var1.drawRegion(this.BL[5], 0, this.BC[var4][2] * 18, 18, 18, 0, var2 + this.BC[var4][0], this.AA + this.CE + this.BC[var4][1], 3);
               }
            }

            for(var4 = this.AE.length - 1; var4 >= 0; --var4) {
               for(var5 = MainScreen.cameraMain.xCam / this.AF[var4] % this.AE[var4]; var5 < MotherCanvas.w + this.AE[var4]; var5 += this.AE[var4]) {
                  var1.drawRegion((mImage)this.BL[var4], MainScreen.cameraMain.xCam + MotherCanvas.w - var5, this.AG[var4] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 46:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            var2 = MainScreen.cameraMain.xCam + 180 - MainScreen.cameraMain.xCam / 4;

            for(var4 = 0; var4 < this.BG.length; ++var4) {
               var1.drawRegion(this.BL[this.BG[var4][0]], 0, 0, this.BL[this.BG[var4][0]].width, this.BL[this.BG[var4][0]].height, this.BG[var4][1], var2 + this.BG[var4][2], this.AA + this.CE + this.BG[var4][3] + 30, 3);
            }

            this.AG(var1);
            break;
         case 51:
         case 53:
         case 55:
            for(var3 = this.AE.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            this.AG(var1);
            return;
         case 62:
            var1.setColor(this.BV);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            this.CD -= MapGotoGod.AA;

            for(var4 = (var2 = (var3 = MainScreen.cameraMain.xCam) + this.CD) / this.AF[2] % this.AE[2]; var4 < var3 + MotherCanvas.w + this.AE[2]; var4 += this.AE[2]) {
               if (var4 >= -this.AE[2]) {
                  var1.drawRegion((mImage)this.BL[2], var4, this.AG[2] + this.CE, 0);
               }
            }

            var4 = var2 / 12;

            while(var4 < var3 + MotherCanvas.w) {
               for(var5 = 0; var5 < this.BF.length; ++var5) {
                  if ((var4 += this.BF[var5][0]) >= -30) {
                     var1.drawRegion((mImage)this.BL[this.BF[var5][2]], var4, this.AG[2] + this.CE + 40 + this.BF[var5][1], 33);
                  }
               }
            }

            for(var4 = var2 / this.AF[1] % this.AE[1]; var4 < var3 + MotherCanvas.w + this.AE[1]; var4 += this.AE[1]) {
               if (var4 >= -this.AE[1]) {
                  var1.drawRegion((mImage)this.BL[1], var4, this.AG[1] + this.CE, 0);
               }
            }

            var4 = var2 / 3;

            while(var4 < var3 + MotherCanvas.w) {
               for(var5 = 0; var5 < this.BE.length; ++var5) {
                  if ((var4 += this.BE[var5][0]) >= -30) {
                     var1.drawRegion((mImage)this.BL[this.BE[var5][2]], var4, this.AG[1] + this.CE + 40 + this.BE[var5][1], 33);
                  }
               }
            }

            for(var4 = var2 / this.AF[0] % this.AE[0]; var4 < var3 + MotherCanvas.w + this.AE[0]; var4 += this.AE[0]) {
               if (var4 >= -this.AE[0]) {
                  var1.drawRegion((mImage)this.BL[0], var4, this.AG[0] + this.CE, 0);
               }
            }

            var4 = var2;

            while(var4 < var3 + MotherCanvas.w) {
               for(var5 = 0; var5 < this.BD.length; ++var5) {
                  if ((var4 += this.BD[var5][0]) >= -30) {
                     var1.drawRegion((mImage)this.BL[this.BD[var5][2]], var4, this.AG[0] + this.CE + 40 + this.BD[var5][1], 33);
                  }
               }
            }

            this.AG(var1);
            return;
         case 63:
            for(var3 = this.AD.length - 1; var3 >= 0; --var3) {
               for(var2 = MainScreen.cameraMain.xCam / this.AF[var3] % this.AE[var3]; var2 < MotherCanvas.w + this.AE[var3]; var2 += this.AE[var3]) {
                  var1.drawRegion((mImage)this.BL[var3], MainScreen.cameraMain.xCam + MotherCanvas.w - var2, this.AG[var3] + this.CE, 0);
               }
            }

            return;
         }

      }
   }

   public final void paintLast(mGraphics var1) {
      if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg || !this.AH()) {
         int var2;
         switch(this.AC) {
         case 2:
         case 22:
         case 23:
         case 28:
         case 47:
         case 51:
         case 53:
         case 55:
            if (MainScreen.cameraMain.yCam > GameCanvas.loadmap.limitH - 25) {
               var1.setColor(this.BW);
               var1.fillRect(MainScreen.cameraMain.xCam, GameCanvas.loadmap.maxHMap - (6 + this.AJ), MotherCanvas.w, 6 + this.AJ);
               this.AA(var1, this.BL[this.BL.length - 1], 64, 12, 18);
               return;
            }
            break;
         case 8:
            if (MainScreen.cameraMain.yCam > GameCanvas.loadmap.limitH - 40) {
               var1.setColor(this.BW);
               var1.fillRect(MainScreen.cameraMain.xCam, GameCanvas.loadmap.maxHMap - (20 + this.AJ), MotherCanvas.w, 20 + this.AJ);

               for(var2 = MainScreen.cameraMain.xCam % 64; var2 < MotherCanvas.w + 64; var2 += 64) {
                  var1.drawRegion(this.BL[3], 0, (var2 / 64 + GameCanvas.BJ) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + MotherCanvas.w - var2, GameCanvas.loadmap.maxHMap - 28 - this.AJ, 0);
               }

               for(var2 = MainScreen.cameraMain.xCam % 64; var2 < MotherCanvas.w + 64 + 64; var2 += 64) {
                  var1.drawRegion(this.BL[3], 0, (var2 / 64 + GameCanvas.BJ + 2) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + 12 + MotherCanvas.w - var2, GameCanvas.loadmap.maxHMap - 12 - this.AJ, 0);
               }

               return;
            }
            break;
         case 40:
            if (MainScreen.cameraMain.yCam > GameCanvas.loadmap.limitH - 40) {
               var1.setColor(this.BW);
               var1.fillRect(MainScreen.cameraMain.xCam, GameCanvas.loadmap.maxHMap - (20 + this.AJ), MotherCanvas.w, 20 + this.AJ);

               for(var2 = MainScreen.cameraMain.xCam % 64; var2 < MotherCanvas.w + 64; var2 += 64) {
                  var1.drawRegion(this.BL[1], 0, (var2 / 64 + GameCanvas.gameTick / 4) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + MotherCanvas.w - var2, GameCanvas.loadmap.maxHMap - 28 - this.AJ, 0);
               }

               for(var2 = MainScreen.cameraMain.xCam % 64; var2 < MotherCanvas.w + 64 + 64; var2 += 64) {
                  var1.drawRegion(this.BL[1], 0, (var2 / 64 + GameCanvas.gameTick / 4 + 2) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + 12 + MotherCanvas.w - var2, GameCanvas.loadmap.maxHMap - 12 - this.AJ, 0);
               }

               return;
            }
            break;
         case 42:
            for(var2 = 0; var2 < this.BH.length && this.BH[var2] != null; ++var2) {
               if (12 + this.BH[var2][0] > MainScreen.cameraMain.xCam - 17 && 12 + this.BH[var2][0] < MainScreen.cameraMain.xCam + MotherCanvas.w + 17) {
                  var1.drawRegion(this.BL[2], this.BH[var2][2] * 17, this.BH[var2][3] * 30, 17, 30, this.BH[var2][5], 12 + this.BH[var2][0], this.AG[0] - 30 + 73 + 90 + this.BH[var2][1], 33);
               }
            }

            return;
         case 45:
            if (MainScreen.cameraMain.yCam > GameCanvas.loadmap.limitH - 25) {
               var1.setColor(this.BW);
               var1.fillRect(MainScreen.cameraMain.xCam, GameCanvas.loadmap.maxHMap - (10 + this.AJ), MotherCanvas.w, 10 + this.AJ);
               this.AA(var1, this.BL[this.BL.length - 1], 64, 12, 22);
               return;
            }
         }

      }
   }

   private void AA(mGraphics var1, mImage var2, int var3, int var4, int var5) {
      for(var3 = MainScreen.cameraMain.xCam % 64; var3 < MotherCanvas.w + 64; var3 += 64) {
         var1.drawRegion(var2, 0, (var3 / 64 + GameCanvas.BJ) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + MotherCanvas.w - var3, GameCanvas.loadmap.maxHMap - var5 - this.AJ, 0);
      }

      for(var3 = MainScreen.cameraMain.xCam % 64; var3 < MotherCanvas.w + 64 + 64; var3 += 64) {
         var1.drawRegion(var2, 0, (var3 / 64 + GameCanvas.BJ + 2) % 4 * 12, 64, 12, 0, MainScreen.cameraMain.xCam + 12 + MotherCanvas.w - var3, GameCanvas.loadmap.maxHMap - (var5 - 10) - this.AJ, 0);
      }

   }

   private void AF(mGraphics var1) {
      if (this.BR != null) {
         int var2;
         for(var2 = 0; var2 < MotherCanvas.w; var2 += 20) {
            var1.drawRegion((mImage)this.BR, MainScreen.cameraMain.xCam + var2, this.AG[0] - this.AN, 0);
         }

         if (CF <= 6 && CF != 4) {
            var1.setColor(-525829);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
         }

         for(var2 = 0; var2 < this.AR.size(); ++var2) {
            Point var3;
            if ((var3 = (Point)this.AR.elementAt(var2)).f > var3.fRe) {
               int var4;
               if ((var4 = 20 + (var3.f - var3.fRe) / 2 * 27) > 101) {
                  var4 = 101;
               }

               var1.drawRegion(this.BS, 0, 0, 41, var4, var3.dis, var3.x, this.AG[0] - this.AN - 20 + var3.y, 0);
            }
         }
      }

   }

   public final void AA() {
      int var1;
      for(var1 = 0; var1 < this.AR.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.AR.elementAt(var1)).f;
         if (var2.f == var2.fRe - 1) {
            var2.x += MainScreen.cameraMain.xCam;
         }

         if (var2.f > var2.fRe + 10) {
            var2.f = 0;
            var2.x = CRes.random(0, MotherCanvas.w);
            var2.y = -CRes.random(40);
            var2.dis = CRes.random(2) << 1;
            var2.fRe = CRes.random(50, 150);
            if (CRes.random(4) == 0) {
               float var4 = mSound.volumeSound;
               mSound.playSound();
            }
         }
      }

      if (this.AR.size() > 0 && ++CF > 50 && CRes.random(60) == 0) {
         CF = 0;
      }

      switch(this.AC) {
      case 2:
      case 22:
      case 23:
      case 45:
      case 47:
      case 51:
      case 53:
      case 55:
         if (!this.AO) {
            if (this.AJ < 6 && CRes.random(15) == 0) {
               ++this.AJ;
            }

            if (CRes.random(200) == 0) {
               this.AO = true;
            }
         } else {
            if (this.AJ > 0 && CRes.random(15) == 0) {
               --this.AJ;
            }

            if (CRes.random(200) == 0) {
               this.AO = false;
            }
         }
         break;
      case 8:
      case 40:
         if (this.AC == 40 && (GameCanvas.isLowGraOrWP_PvP() || GameCanvas.isOffBg)) {
            return;
         }

         if (!this.AO) {
            if (this.AJ < 12 && GameCanvas.gameTick % this.AK == 0) {
               ++this.AJ;
            }

            if (GameCanvas.gameTick % this.AL == 0) {
               this.AO = true;
               this.AK = CRes.random(5, 10);
               this.AL = CRes.random(12, 21) * 10;
            }
         } else {
            if (this.AJ > 0 && GameCanvas.gameTick % this.AK == 0) {
               --this.AJ;
            }

            if (GameCanvas.gameTick % this.AL == 0) {
               this.AO = false;
               this.AK = CRes.random(5, 10);
               this.AL = CRes.random(12, 21) * 10;
            }
         }

         if (this.AC == 40) {
            for(var1 = 0; var1 < this.BH.length && this.BH[var1] != null; ++var1) {
               if (this.BH[var1][3] == 0) {
                  if (CRes.random(32) == 0) {
                     this.BH[var1][3] = 1;
                     this.BH[var1][4] = 0;
                  }
               } else {
                  ++this.BH[var1][4];
                  if (this.BH[var1][4] == 4) {
                     this.BH[var1][3] = 2;
                  } else if (this.BH[var1][4] == 8) {
                     this.BH[var1][3] = 0;
                     if (this.BH[var1][0] < GameScreen.player.x) {
                        this.BH[var1][5] = 2;
                     } else {
                        this.BH[var1][5] = 0;
                     }
                  }
               }
            }
         }
         break;
      case 15:
      case 41:
         for(var1 = 0; var1 < this.AQ.size(); ++var1) {
            Point_Focus var3;
            if ((var3 = (Point_Focus)this.AQ.elementAt(var1)).x > MainScreen.cameraMain.xCam - 25 && var3.x < MainScreen.cameraMain.xCam + MotherCanvas.w + 25) {
               ++var3.AG;
               if (var3.frame == 1) {
                  if (var3.AG > 15) {
                     var3.frame = 0;
                     var3.AG = 0;
                  }
               } else if (var3.frame == 0) {
                  if (this.AP) {
                     if (CRes.random(5) != 0) {
                        var3.AG = 0;
                        var3.frame = 1;
                     }
                  } else if (CRes.random(30) == 0) {
                     var3.AG = 0;
                     var3.frame = 1;
                  }
               }
            }
         }

         if (!this.AP) {
            if (CRes.random(100) == 0) {
               this.AP = true;
            }
         } else if (CRes.random(2) == 0) {
            this.AP = false;
         }
         break;
      case 17:
         if (this.AT != null) {
            ++this.AT.f;
            Point var10000 = this.AT;
            var10000.x += this.AT.vx;
            var10000 = this.AT;
            var10000.y += this.AT.vy;
            if (this.AT.f == 10) {
               this.AT.vx = -6;
               this.AT.vy = -4;
            }

            if (this.AT.f == 20) {
               this.AT.vx = 0;
               this.AT.vy = 0;
               LoadMap.timeVibrateScreen = 8;
            }

            if (this.AT.f == 100) {
               this.AT.vx = 5;
               this.AT.vy = 3;
            }

            if (this.AT.f == 116) {
               this.AT.vx = 0;
               this.AT.vy = 0;
            }

            if (this.AT.f > 160 && CRes.random(40) == 0) {
               this.AT.x = 140;
               this.AT.y = 0;
               this.AT.f = 0;
            }
         }
         break;
      case 42:
         if (GameCanvas.isLowGraOrWP_PvP() || GameCanvas.isOffBg) {
            return;
         }

         for(var1 = 0; var1 < this.BH.length && this.BH[var1] != null; ++var1) {
            if (this.BH[var1][3] == 0) {
               if (CRes.random(32) == 0) {
                  this.BH[var1][3] = 1;
                  this.BH[var1][4] = 0;
               }
            } else {
               ++this.BH[var1][4];
               if (this.BH[var1][4] == 4) {
                  this.BH[var1][3] = 2;
               } else if (this.BH[var1][4] == 8) {
                  this.BH[var1][3] = 0;
                  if (this.BH[var1][0] < GameScreen.player.x) {
                     this.BH[var1][5] = 2;
                  } else {
                     this.BH[var1][5] = 0;
                  }
               }
            }
         }
      }

      if (this.AC != 15 && this.AC != 41 && this.AC != 60 && this.AC != 61 && (this.AC != 20 && this.AC != 21 && this.AC != 22 || !GameCanvas.isLowGraOrWP_PvP()) && this.AC != 28 && this.AC != 31 && this.AC != 42 && this.AC != 33 && this.AC != 43 && this.AC != 63) {
         if (this.AC == 6) {
            this.CE = MainScreen.cameraMain.yCam / 12;
         } else {
            this.CE = MainScreen.cameraMain.yCam / 6;
         }
      } else {
         this.CE = 0;
      }

      if (this.CE < 0) {
         this.CE = 0;
      }

   }

   private void AG(mGraphics var1) {
      if (this.BM == null) {
         this.AA(false);
      } else if (this.AS != null) {
         int var2 = 0;
         if (this.AC == 8) {
            var2 = this.AJ;
         }

         int var3 = MainScreen.cameraMain.xCam / this.AF[this.AF.length - 1];

         for(int var4 = 0; var4 < this.AS.length; ++var4) {
            for(int var5 = 0; var5 < this.AS[var4].length; ++var5) {
               var1.drawRegion((mImage)this.BM[this.AS[var4][var5].frame], MainScreen.cameraMain.xCam - var3 + this.AS[var4][var5].x / 100, this.AS[var4][var5].y + this.CE - var2, 6);
            }
         }

      }
   }

   private void AH(mGraphics var1) {
      int var2;
      if (this.BP == null) {
         MapBackGround var6 = this;
         this.BP = new mImage[3];

         for(var2 = 0; var2 < var6.BP.length; ++var2) {
            var6.BP[var2] = mImage.createImage("/bg/cloud1" + var2 + ".png");
         }

      } else if (this.AS != null) {
         var2 = 0;
         if (this.AC == 8 || this.AC == 40) {
            var2 = this.AJ;
         }

         int var3 = MainScreen.cameraMain.xCam / this.AF[this.AF.length - 1];

         for(int var4 = 0; var4 < this.AS.length; ++var4) {
            for(int var5 = 0; var5 < this.AS[var4].length; ++var5) {
               var1.setColor(0);
               var1.drawRegion((mImage)this.BP[this.AS[var4][var5].frame], MainScreen.cameraMain.xCam - var3 + this.AS[var4][var5].x / 100, this.AS[var4][var5].y + this.CE - var2, 6);
            }
         }

      }
   }

   private void AA(boolean var1) {
      if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
         this.BM = new mImage[3];

         for(int var2 = 0; var2 < this.BM.length; ++var2) {
            if (GameScreen.effMap != null && GameScreen.effMap.AA == 8) {
               this.BM[var2] = mImage.createImage("/bg/cloud1" + var2 + ".png");
            } else if (!var1 && (this.AC == 28 || this.AC == 29 || this.AC == 30 || this.BX == 31 || this.BX == 42)) {
               this.BM[var2] = mImage.createImage("/bg/cloud2" + var2 + ".png");
            } else if (var1 || LoadMap.idTile != 11 && LoadMap.idTile != 14) {
               this.BM[var2] = mImage.createImage("/bg/cloud" + var2 + ".png");
            } else {
               this.BM[var2] = mImage.createImage("/bg/cloud3" + var2 + ".png");
            }
         }

      }
   }

   public final void AB() {
      if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
         if (this.AS != null) {
            for(int var1 = 0; var1 < this.AS.length; ++var1) {
               for(int var2 = 0; var2 < this.AS[var1].length; ++var2) {
                  Point var10000 = this.AS[var1][var2];
                  var10000.x += this.AS[var1][var2].vx;
                  if (this.AS[var1][var2].x / 100 < -80) {
                     this.AS[var1][var2].x = (GameCanvas.loadmap.mapW * LoadMap.wTile + CRes.random_Am_0(125)) * 100;
                     this.AS[var1][var2].y = this.AI - 60 + var1 * 30 + CRes.random_Am_0(10) + (var1 == 1 ? 5 : 0);
                     this.AS[var1][var2].vx = -60 / ((var1 << 2) + 1);
                     this.AS[var1][var2].frame = var1 % 3;
                  }
               }
            }

         }
      }
   }

   public final void AC() {
      this.BX = -1;
      this.CH = new Point[6];
      if (MotherCanvas.w > 300) {
         this.CH = new Point[8];
      }

      this.CH[0] = new Point(MotherCanvas.w - 40 + CRes.random_Am_0(20), MotherCanvas.h - 140 + CRes.random_Am_0(10));
      this.CH[0].frame = 12;
      if (GameCanvas.AF == 0) {
         this.CH[0].vx = -CRes.random(10, 30);
      }

      this.CH[0].x2 = this.CH[0].x * 100;
      this.CH[1] = new Point(20 + CRes.random_Am_0(10), MotherCanvas.h - 25 + CRes.random_Am_0(10));
      this.CH[1].frame = 6 + (GameCanvas.AF << 2);
      this.CH[2] = new Point(180 + CRes.random_Am_0(10), MotherCanvas.h - 8 + CRes.random_Am_0(10));
      this.CH[2].frame = 6 + (GameCanvas.AF << 2);
      this.CH[3] = new Point(100 + CRes.random_Am_0(10), MotherCanvas.h - 30 + CRes.random_Am_0(10));
      this.CH[3].frame = 5 + (GameCanvas.AF << 2);
      this.CH[4] = new Point(MotherCanvas.w - 25 + CRes.random_Am_0(10), MotherCanvas.h - 65 + CRes.random_Am_0(10));
      this.CH[4].frame = 4;
      this.CH[5] = new Point(20 + CRes.random_Am_0(10), MotherCanvas.h - 70 + CRes.random_Am_0(5));
      this.CH[5].frame = 7;
      if (MotherCanvas.w > 300) {
         this.CH[6] = new Point(MotherCanvas.w / 2 + MotherCanvas.w / 3 + CRes.random_Am_0(10), MotherCanvas.h - 65 + CRes.random_Am_0(10));
         this.CH[6].frame = 7;
         this.CH[7] = new Point(MotherCanvas.w / 2 - MotherCanvas.w / 3 + CRes.random_Am_0(10), MotherCanvas.h - 70 + CRes.random_Am_0(5));
         this.CH[7].frame = 4;
      }

      this.CG = 0;
      this.BV = -6434049;
      this.AS = null;
      int var1;
      int var2;
      int var3;
      if (GameCanvas.AF == 0) {
         if (MotherCanvas.h > 230) {
            this.AS = new Point[3][];

            for(var1 = 0; var1 < this.AS.length; ++var1) {
               var2 = CRes.random(1, 3);
               this.AS[var1] = new Point[var2];

               for(var3 = 0; var3 < var2; ++var3) {
                  this.AS[var1][var3] = new Point();
                  this.AS[var1][var3].x = CRes.random(MotherCanvas.w) * 100;
                  this.AS[var1][var3].y = MotherCanvas.h - 290 + var1 * 30 + CRes.random_Am_0(10) + (var1 == 1 ? 5 : 0);
                  this.AS[var1][var3].vx = -CRes.random(100, 200) / ((var1 << 2) + 1);
                  this.AS[var1][var3].frame = var1 % 3;
               }
            }
         }
      } else {
         this.CG = 2;
         this.BV = -12160904;
         this.AS = new Point[1][];

         for(var1 = 0; var1 <= 0; ++var1) {
            var2 = MotherCanvas.w / 18;
            this.AS[0] = new Point[var2];

            for(var3 = 0; var3 < var2; ++var3) {
               this.AS[0][var3] = new Point();
               this.AS[0][var3].x = var3 % 5 * MotherCanvas.w / 5 + MotherCanvas.w / 10 + CRes.random_Am_0(MotherCanvas.w / 10);
               int var4;
               if ((var4 = MotherCanvas.h - 230) <= 0) {
                  var4 = 1;
               }

               this.AS[0][var3].y = MotherCanvas.h - (230 + CRes.random(var4));
               this.AS[0][var3].frame = CRes.random(4) * 3;
               this.AS[0][var3].f = 4;
               this.AS[0][var3].AX = CRes.random(100) == 0;
               if (this.AS[0][var3].AX) {
                  this.AS[0][var3].f = CRes.random(4);
               }
            }
         }
      }

      this.AI();
   }

   public final void AD() {
      if (!GameCanvas.isLowGraOrWP_PvP() && !GameCanvas.isOffBg) {
         if (this.AS != null) {
            int var1;
            int var2;
            if (GameCanvas.AF == 0) {
               for(var1 = 0; var1 < this.AS.length; ++var1) {
                  for(var2 = 0; var2 < this.AS[var1].length; ++var2) {
                     Point var10000 = this.AS[var1][var2];
                     var10000.x += this.AS[var1][var2].vx;
                     if (this.AS[var1][var2].x < -9000) {
                        this.AS[var1][var2].x = (MotherCanvas.w + 60 + CRes.random_Am_0(10)) * 100;
                        this.AS[var1][var2].vx = -CRes.random(100, 200) / ((var1 << 2) + 1);
                        this.AS[var1][var2].frame = var1 % 3;
                     }
                  }
               }

            } else {
               for(var1 = 0; var1 < this.AS.length; ++var1) {
                  for(var2 = 0; var2 < this.AS[var1].length; ++var2) {
                     if (this.AS[var1][var2].AX) {
                        ++this.AS[var1][var2].f;
                        if (this.AS[var1][var2].f / 2 > 2) {
                           this.AS[var1][var2].AX = false;
                           this.AS[var1][var2].f = 4;
                        }
                     } else if (CRes.random(100) == 0) {
                        this.AS[var1][var2].frame = CRes.random(4) * 3;
                        this.AS[var1][var2].f = CRes.random(4);
                        this.AS[var1][var2].AX = true;
                     }
                  }
               }

            }
         }
      }
   }

   public final void AC(mGraphics var1) {
      var1.setColor(this.BV);
      var1.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);
      if (this.BN == null) {
         this.AI();
      } else {
         int var2;
         if (this.BR != null) {
            for(var2 = 0; var2 < MotherCanvas.w; var2 += 20) {
               var1.drawRegion((mImage)this.BR, var2, MotherCanvas.h - 160 - 115, 0);
            }
         }

         for(var2 = 0; var2 < MotherCanvas.w; var2 += 96) {
            var1.drawRegion((mImage)this.BN[1 + this.CG], var2, MotherCanvas.h - 191 - 82 + 34, 0);
         }

         for(var2 = 0; var2 < MotherCanvas.w; var2 += 48) {
            var1.drawRegion((mImage)this.BN[0 + this.CG], var2, MotherCanvas.h - 191, 0);
         }

         for(var2 = 0; var2 < MotherCanvas.w; var2 += 24) {
            var1.drawRegion(this.BO[0], 0, GameCanvas.gameTick / 8 % 2 * 24, 24, 24, 0, var2, MotherCanvas.h - 191, 0);
         }

         if (GameCanvas.AF == 0) {
            this.AJ(var1);
         } else {
            this.AI(var1);
         }
      }
   }

   public final void AD(mGraphics var1) {
      if (this.BN == null) {
         this.AI();
      } else {
         for(int var2 = 4; var2 < this.CH.length; ++var2) {
            if (var2 != 0) {
               var1.drawRegion((mImage)this.BN[this.CH[var2].frame], this.CH[var2].x, this.CH[var2].y, 33);
            }
         }

      }
   }

   public final void AE(mGraphics var1) {
      if (this.BN == null) {
         this.AI();
      } else {
         for(int var2 = 0; var2 < 4; ++var2) {
            if (var2 != 0) {
               var1.drawRegion((mImage)this.BN[this.CH[var2].frame], this.CH[var2].x, this.CH[var2].y, 33);
            }
         }

      }
   }

   private void AI(mGraphics var1) {
      if (this.BT == null) {
         this.BT = new FrameImage(mImage.createImage("/bg/star.png"), 5, 5);
      } else if (this.AS != null) {
         for(int var2 = 0; var2 < this.AS.length; ++var2) {
            for(int var3 = 0; var3 < this.AS[var2].length; ++var3) {
               this.BT.drawFrame(this.AS[var2][var3].frame + this.AS[var2][var3].f / 2, this.AS[var2][var3].x, this.AS[var2][var3].y, 0, 3, var1);
            }
         }

      }
   }

   private void AJ(mGraphics var1) {
      try {
         if (this.BM == null) {
            this.AA(true);
         } else if (this.AS != null) {
            for(int var2 = 0; var2 < this.AS.length; ++var2) {
               for(int var3 = 0; var3 < this.AS[var2].length; ++var3) {
                  var1.drawRegion((mImage)this.BM[this.AS[var2][var3].frame], this.AS[var2][var3].x / 100, this.AS[var2][var3].y, 3);
               }
            }

         }
      } catch (Exception var4) {
      }
   }

   private void AI() {
      if (this.BN == null || this.BO == null) {
         this.BN = new mImage[11];

         for(int var1 = 0; var1 < this.BN.length; ++var1) {
            if (var1 != 8 && (GameCanvas.AF != GameCanvas.BO || var1 != 0 && var1 != 1 && var1 != 5 && var1 != 6) && (GameCanvas.AF != 0 || var1 != 2 && var1 != 3 && var1 != 9 && var1 != 10)) {
               this.BN[var1] = mImage.createImage("/bg/login" + var1 + ".png");
            }
         }

         this.BO = new mImage[1];
         if (GameCanvas.AF == 0) {
            this.BO[0] = mImage.createImage("/bg/sea0.png");
            this.BR = mImage.createImage("/bg/sky0.png");
         } else {
            this.BO[0] = mImage.createImage("/bg/sea3.png");
            this.BR = mImage.createImage("/bg/sky4.png");
         }
      }

      if (this.BX == 28 || this.BX == 29 || this.BX == 30 || this.BX == 31 || this.BX == 42) {
         this.BM = null;
      }

   }

   public final void AE() {
      if (GameCanvas.loadmap.idMapLoadMap != 69 && GameCanvas.loadmap.idMapLoadMap != 113) {
         this.BR = mImage.createImage("/bg/sky6.png");
         this.BV = -6108691;
      }

   }

   public final void AF() {
      if (this.AC != 1 && this.AC != 18 && this.AC != 16 && this.AC != 35 && this.AC != 33 && this.AC != 63 && GameCanvas.loadmap.idMapLoadMap != 113) {
         this.BV = -8937278;
         this.BR = mImage.createImage("/bg/sky10.png");
      }

      int var1;
      if ((var1 = MotherCanvas.w / 150 + 1) > 3) {
         var1 = 3;
      }

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point(CRes.random(0, MotherCanvas.w), -CRes.random(40))).dis = CRes.random(2) << 1;
         var3.fRe = CRes.random(100, 200);
         var3.f = CRes.random(100);
         this.AR.addElement(var3);
      }

      if (this.BS == null) {
         this.BS = mImage.createImage("/bg/thunder.png");
      }

      this.AA(false);
      CF = 10;
   }

   public final void AG() {
      this.BV = -14142143;
      this.BR = mImage.createImage("/bg/sky11.png");
      this.BM = new mImage[3];

      for(int var1 = 0; var1 < this.BM.length; ++var1) {
         if (GameScreen.effMap != null) {
            this.BM[var1] = mImage.createImage("/bg/cloud1" + var1 + ".png");
         }
      }

   }
}
