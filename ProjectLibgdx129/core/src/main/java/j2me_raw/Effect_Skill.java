public final class Effect_Skill extends MainEffect {
   private int[][] CG;
   private int subType;
   private int CI;
   private int CJ;
   private int CK;
   private int[][] CL;
   private int[][] CM;
   private int fPlayFrameSuper;
   private FrameImage[] CO;
   private boolean isAddSound;
   private MainObject objBeFireMain;
   private mVector VecEff;
   private mVector VecSubEff;
   private mVector CT;
   private int[] CU;
   private int[] CV;
   private short[] CW;
   private int x1000;
   private int y1000;
   private int CZ;
   private int DA;
   private int DB;
   private int gocT_Arc;
   private int[] DD;
   private int DE;
   private int DF;
   private int DG;
   private int DH;
   private int DI;
   private Point_Focus DJ;
   private Point_Focus DK;
   private int DL;
   private int DM;
   private int DN;

   public Effect_Skill(MainSkill var1, MainObject var2, int var3, int var4, mVector var5) {
      int[][][] var10000 = new int[][][]{{{4, -26, -23}, {1, -10, 20}}, {{3, -15, -43}, {1, 5, -26}, {3, -28, -4}, {4, -28, 10}}, {{0, -27, -90}, {3, -27, -76}, {4, -22, -58}, {2, -10, -30}, {1, 0, -14}}, {{3, -44, -70}, {0, -44, -45}, {4, -36, -21}, {3, -24, 0}}};
      this.CG = new int[][]{{3, -15, -35}, {4, -25, -32}, {5, -25, -27}, {0, -30, -20}, {1, -30, -20}, {2, -30, -20}};
      this.CI = 0;
      this.fPlayFrameSuper = 0;
      this.isAddSound = false;
      this.VecEff = new mVector();
      this.VecSubEff = new mVector();
      this.CT = new mVector();
      new mVector();
      this.CW = new short[]{0, 50, 75, 100, 20, 110, 30};
      this.DD = new int[]{0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330};
      this.DE = 15;
      this.DH = 0;
      this.DN = 0;
      this.CI = 0;
      super.AB = 0;
      if (LoadMapScreen.isNextMap) {
         super.x = var3;
         super.y = var4;
         this.CT = var5;
         super.Dir = var1.AN;
         if (this.CT != null && this.CT.size() > 0) {
            Point_Focus var6 = (Point_Focus)this.CT.elementAt(0);
            super.toX = var6.x;
            super.toY = var6.y;
         } else {
            super.toX = var3;
            super.toY = var4;
         }

         super.AM = -1;
         this.objBeFireMain = var2;
         super.isStop = false;
         super.BI = false;
         super.f = -1;
         super.typeEffect = var1.AA;
         this.subType = var1.AE;
         super.timeBegin = var1.AK;
         super.timeEnd = var1.AF;
         super.objFireMain = var2;
         super.isEff = true;
         super.numNextFrame = 1;
      }
   }

   public Effect_Skill(MainSkill var1, MainObject var2) {
      int[][][] var10000 = new int[][][]{{{4, -26, -23}, {1, -10, 20}}, {{3, -15, -43}, {1, 5, -26}, {3, -28, -4}, {4, -28, 10}}, {{0, -27, -90}, {3, -27, -76}, {4, -22, -58}, {2, -10, -30}, {1, 0, -14}}, {{3, -44, -70}, {0, -44, -45}, {4, -36, -21}, {3, -24, 0}}};
      this.CG = new int[][]{{3, -15, -35}, {4, -25, -32}, {5, -25, -27}, {0, -30, -20}, {1, -30, -20}, {2, -30, -20}};
      this.CI = 0;
      this.fPlayFrameSuper = 0;
      this.isAddSound = false;
      this.VecEff = new mVector();
      this.VecSubEff = new mVector();
      this.CT = new mVector();
      new mVector();
      this.CW = new short[]{0, 50, 75, 100, 20, 110, 30};
      this.DD = new int[]{0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330};
      this.DE = 15;
      this.DH = 0;
      this.DN = 0;
      this.CI = 0;
      super.AB = 0;
      if (LoadMapScreen.isNextMap) {
         super.AM = -1;
         this.objBeFireMain = var2;
         super.isStop = false;
         super.BI = false;
         super.f = -1;
         super.typeEffect = var1.AA;
         this.subType = var1.AE;
         super.timeBegin = var1.AK;
         super.objFireMain = var2;
         super.isEff = true;
         super.numNextFrame = 1;
      }
   }

   public Effect_Skill() {
      int[][][] var10000 = new int[][][]{{{4, -26, -23}, {1, -10, 20}}, {{3, -15, -43}, {1, 5, -26}, {3, -28, -4}, {4, -28, 10}}, {{0, -27, -90}, {3, -27, -76}, {4, -22, -58}, {2, -10, -30}, {1, 0, -14}}, {{3, -44, -70}, {0, -44, -45}, {4, -36, -21}, {3, -24, 0}}};
      this.CG = new int[][]{{3, -15, -35}, {4, -25, -32}, {5, -25, -27}, {0, -30, -20}, {1, -30, -20}, {2, -30, -20}};
      this.CI = 0;
      this.fPlayFrameSuper = 0;
      this.isAddSound = false;
      this.VecEff = new mVector();
      this.VecSubEff = new mVector();
      this.CT = new mVector();
      new mVector();
      this.CW = new short[]{0, 50, 75, 100, 20, 110, 30};
      this.DD = new int[]{0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330};
      this.DE = 15;
      this.DH = 0;
      this.DN = 0;
   }

   public Effect_Skill(int var1, int var2, MainObject var3, mVector var4) {
      int[][][] var10000 = new int[][][]{{{4, -26, -23}, {1, -10, 20}}, {{3, -15, -43}, {1, 5, -26}, {3, -28, -4}, {4, -28, 10}}, {{0, -27, -90}, {3, -27, -76}, {4, -22, -58}, {2, -10, -30}, {1, 0, -14}}, {{3, -44, -70}, {0, -44, -45}, {4, -36, -21}, {3, -24, 0}}};
      this.CG = new int[][]{{3, -15, -35}, {4, -25, -32}, {5, -25, -27}, {0, -30, -20}, {1, -30, -20}, {2, -30, -20}};
      this.CI = 0;
      this.fPlayFrameSuper = 0;
      this.isAddSound = false;
      this.VecEff = new mVector();
      this.VecSubEff = new mVector();
      this.CT = new mVector();
      new mVector();
      this.CW = new short[]{0, 50, 75, 100, 20, 110, 30};
      this.DD = new int[]{0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330};
      this.DE = 15;
      this.DH = 0;
      this.DN = 0;
      this.CI = 0;
      super.AB = 0;
      if (LoadMapScreen.isNextMap) {
         super.AM = -1;
         this.objBeFireMain = null;
         this.subType = var2;
         super.isStop = false;
         super.BI = false;
         if (var4 != null && var4.size() != 0) {
            super.vecObjsBeFire = var4;
            super.f = -1;
            super.typeEffect = var1;
            super.timeBegin = GameCanvas.timeNow;
            super.objFireMain = var3;
            Object_Effect_Skill var5;
            if ((var5 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)) != null) {
               this.objBeFireMain = MainObject.get_Object((int)var5.ID, (byte)var5.tem);
            }

            if (this.objBeFireMain != null && super.objFireMain != null) {
               super.isEff = false;
               if (super.objFireMain == GameScreen.player && LoadMap.specMap != 3) {
                  super.isEff = true;
               }

               super.numNextFrame = 1;
               super.x = super.objFireMain.x;
               super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
               super.toX = this.objBeFireMain.x;
               super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
               if (super.objFireMain != this.objBeFireMain) {
                  this.setAngle();
                  super.objFireMain.type_left_right = super.Dir;
                  super.objFireMain.Dir = super.Dir;
               }

            }
         }
      }
   }

   public Effect_Skill(int var1, int var2, MainObject var3, mVector var4, int var5, int var6) {
      int[][][] var10000 = new int[][][]{{{4, -26, -23}, {1, -10, 20}}, {{3, -15, -43}, {1, 5, -26}, {3, -28, -4}, {4, -28, 10}}, {{0, -27, -90}, {3, -27, -76}, {4, -22, -58}, {2, -10, -30}, {1, 0, -14}}, {{3, -44, -70}, {0, -44, -45}, {4, -36, -21}, {3, -24, 0}}};
      this.CG = new int[][]{{3, -15, -35}, {4, -25, -32}, {5, -25, -27}, {0, -30, -20}, {1, -30, -20}, {2, -30, -20}};
      this.CI = 0;
      this.fPlayFrameSuper = 0;
      this.isAddSound = false;
      this.VecEff = new mVector();
      this.VecSubEff = new mVector();
      this.CT = new mVector();
      new mVector();
      this.CW = new short[]{0, 50, 75, 100, 20, 110, 30};
      this.DD = new int[]{0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330};
      this.DE = 15;
      this.DH = 0;
      this.DN = 0;
      this.CI = 0;
      super.AB = 0;
      if (LoadMapScreen.isNextMap) {
         super.AM = -1;
         this.objBeFireMain = null;
         this.subType = 0;
         super.isStop = false;
         super.BI = false;
         if (var4 != null && var4.size() != 0) {
            super.vecObjsBeFire = var4;
            super.f = -1;
            super.typeEffect = var1;
            super.timeBegin = GameCanvas.timeNow;
            super.objFireMain = var3;
            Object_Effect_Skill var7;
            if ((var7 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)) != null) {
               this.objBeFireMain = MainObject.get_Object((int)var7.ID, (byte)var7.tem);
            }

            if (this.objBeFireMain != null && super.objFireMain != null) {
               super.isEff = false;
               if (super.objFireMain == GameScreen.player && LoadMap.specMap != 3) {
                  super.isEff = true;
               }

               super.numNextFrame = 1;
               super.x = var5;
               super.y = var6;
               super.toX = this.objBeFireMain.x;
               super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
               if (super.objFireMain != this.objBeFireMain) {
                  this.setAngle();
                  super.objFireMain.type_left_right = super.Dir;
                  super.objFireMain.Dir = super.Dir;
               }

            }
         }
      }
   }

   private void createEffFireExplore() {
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      super.indexImg = 0;
      super.fraImgEff = new FrameImage(mImage.createImage("/test_eff/skill2/eff.png"), 18);
      super.x = GameScreen.player.x;
      super.y = GameScreen.player.y;
      Player.isBlock = true;
   }

   private void createEffThunderFalls() {
      super.indexImg = 0;
      super.AS = -1;
      super.AT = super.AU = -1;
      super.fRemove = 24;
      super.fraImgEff = new FrameImage(mImage.createImage("/test_eff/skill1/eff1.png"), 4);
      super.fraImgSubEff = new FrameImage(mImage.createImage("/test_eff/skill1/eff2.png"), 4);
      super.fraImgSub2Eff = new FrameImage(mImage.createImage("/test_eff/skill1/eff3.png"), 4);
      super.x = this.objBeFireMain.x;
      super.y = this.objBeFireMain.y;
      this.CU = new int[]{-50, 70, 55, -30};
      this.CV = new int[]{-30, -20, 30, 20};
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void updateEffFireExplore() {
      if (super.f % 3 == 0 && !super.BQ) {
         ++super.AS;
      }

      if (super.AS > 18) {
         super.AS = 0;
         ++super.indexImg;
      }

      if (super.indexImg > 0) {
         Player.isBlock = false;
         this.removeEff();
      }

   }

   private void updateEffThunderFall() {
      super.x = this.objBeFireMain.x;
      super.y = this.objBeFireMain.y;

      for(int var1 = 0; var1 < this.CU.length; ++var1) {
         if (GameCanvas.loadmap.AA(super.x + this.CU[var1], super.y + this.CV[var1]) == 1) {
            this.CV[var1] = -this.CV[var1] + CRes.random(10, 20);
         }
      }

      if (super.indexImg < 5) {
         if (super.f % 3 == 0 && !super.BQ) {
            ++super.AS;
         }

         if (super.AS > 3) {
            super.BT = System.currentTimeMillis();
            super.BQ = true;
            super.AS = 0;
            ++super.indexImg;
         }

         if (super.BQ) {
            GameScreen.addEffectEnd((short)63, 0, super.x, super.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)59, 0, super.x, super.y, super.Dir, super.objMainEff);
            if (System.currentTimeMillis() - super.BT >= 300L) {
               super.BQ = false;
            }
         }
      }

      if (super.AW < 4) {
         if (super.f % 3 == 0 && !super.BR && super.indexImg > 0) {
            ++super.AT;
         }

         if (super.AT > 3) {
            super.BU = System.currentTimeMillis();
            super.BR = true;
            super.AT = 0;
            ++super.AW;
         }

         if (super.BR && System.currentTimeMillis() - super.BU >= 300L) {
            super.BR = false;
         }
      }

      if (super.AX < 4) {
         if (super.f % 3 == 0 && !super.BS && super.indexImg > 0) {
            ++super.AU;
         }

         if (super.AU > 3) {
            super.BV = System.currentTimeMillis();
            super.BS = true;
            super.AU = 0;
            ++super.AX;
         }

         if (super.BS && System.currentTimeMillis() - super.BV >= 300L) {
            super.BS = false;
         }
      }

      if (super.indexImg == 5 && super.AW == 4 && super.AX == 4) {
         this.removeEff();
      }

   }

   public final boolean CreateEffectSkill() {
      if (super.objFireMain != null && this.objBeFireMain != null && !super.objFireMain.returnAction() && !this.objBeFireMain.returnAction()) {
         if (super.objFireMain == GameScreen.player || CRes.random(3) == 0) {
            this.isAddSound = true;
         }

         super.objMainEff = super.objFireMain;
         super.am_duong = -1;
         if (GameCanvas.lowGraphic && super.objFireMain != GameScreen.player) {
            if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, super.objFireMain.x, super.objFireMain.y) > 120) {
               this.removeEff();
               return true;
            }

            if (GameScreen.vecObjFire.size() > 30) {
               this.removeEff();
               return true;
            }
         }

         if (super.Dir == 2) {
            super.am_duong = 1;
         }

         int num17;
         Object_Effect_Skill var2;
         int ydich;
         MainObject var5;
         float var10000;
         int xdich;
         label442:
         switch(super.typeEffect) {
         case -1:
            super.fRemove = 60;
            super.fraImgEff = new FrameImage(mImage.createImage("/eff/n1.png"), 14, 15);
            super.fraImgSubEff = new FrameImage(mImage.createImage("/eff/n1.png"), 14, 15);
            super.vMax = 16000;
            this.createDanFocus();
            super.frame = this.setFrameAngle(this.gocT_Arc);
            break;
         case 0:
            this.createNormal();
            break;
         case 1:
         case 37:
            this.createLuffy1();
            break;
         case 2:
         case 228:
         case 259:
         case 260:
         case 261:
            this.create_Devil_FIRE1();
            break;
         case 3:
         case 229:
         case 262:
         case 263:
         case 264:
            this.create_Devil_FIRE2();
            break;
         case 4:
         case 230:
            this.create_Devil_ICE1();
            break;
         case 5:
         case 231:
            this.create_Devil_ICE2();
            break;
         case 6:
         case 232:
            this.create_Devil_Smoker1();
            break;
         case 7:
            this.createUssopSea1();
            break;
         case 9:
         case 53:
         case 163:
            this.createNami1();
            break;
         case 10:
         case 234:
            this.create_Devil_Smoker2();
            break;
         case 11:
            super.fRemove = 15;
            this.createNamiSea1_2();
            break;
         case 12:
         case 49:
         case 50:
         case 188:
         case 220:
         case 293:
            this.createSanji2();
            break;
         case 13:
         case 258:
            this.createSmoker1();
            break;
         case 14:
         case 44:
            super.fRemove = 9;
            break;
         case 15:
         case 38:
            this.createZoro3();
            break;
         case 16:
         case 51:
            this.createNamiSkill1();
            break;
         case 17:
            super.isEff = true;
            super.Dir = (byte)super.objFireMain.type_left_right;
            this.addSoundBuff();
            super.y = super.objFireMain.y;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.fraImgEff = new FrameImage(101, 40, 47);
            super.fRemove = 20;
            return true;
         case 18:
            this.createSmoker2();
            break;
         case 19:
            this.createZoro4();
            break;
         case 20:
            super.fRemove = 24;
            super.levelPaint = -1;
            super.fraImgEff = new FrameImage(171, 153, 84, 100, 54);
            GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 600, super.Dir, super.objMainEff);
            super.y = super.objFireMain.y + 20;
            break;
         case 21:
         case 33:
         case 176:
            super.fRemove = 8;
            break;
         case 22:
         case 98:
            this.createCabaji_2();
            break;
         case 23:
            super.fRemove = 3;
            super.fraImgEff = new FrameImage(20, 10, 10);
            super.vMax = 18;
            super.y -= 5;
            if (super.Dir == 0) {
               super.x -= 10;
            } else {
               super.x += 10;
            }

            xdich = super.toX - super.x;
            ydich = super.toY - super.y;
            this.create_Speed(xdich, ydich, (Point_Focus)null);
            GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
            this.fPlayFrameSuper = super.fRemove;
            if (super.fRemove < 5) {
               super.fRemove = 5;
            }
            break;
         case 24:
         case 80:
            super.fRemove = 14;
            break;
         case 25:
         case 235:
            this.create_Crocodile_1();
            break;
         case 26:
         case 236:
            this.addVir(10, 5, 10, true);
            super.fRemove = 20;
            super.fraImgEff = new FrameImage(99, 32, 32);
            super.y = super.objFireMain.y;
            if (this.isAddSound) {
               this.addSoundBuff();
            }
            break;
         case 27:
            this.createChess();
            break;
         case 28:
            this.createKuromarimo();
            break;
         case 29:
            this.createZoro8();
            break;
         case 30:
            this.createMon_1();
            break;
         case 31:
         case 55:
         case 56:
         case 191:
         case 223:
         case 313:
            this.createNamiSkill3();
            break;
         case 32:
            this.createWapol();
            break;
         case 34:
         case 35:
            this.createLuffy6();
            break;
         case 36:
            this.createWapol2();
            break;
         case 39:
            this.createWapol3();
            break;
         case 40:
            this.create_Wapol4();
            break;
         case 41:
            this.createZoro_S2_L1_New();
            break;
         case 42:
            super.fRemove = 15;
            this.createZoroSkill3_Lv1();
            break;
         case 43:
            super.fRemove = 20;
            this.createZoroSkill3_Lv1();
            break;
         case 45:
            this.createMr3_1();
            break;
         case 46:
            this.addSoundBuff();
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            if (super.objFireMain.posTransRoad != null) {
               super.objFireMain.posTransRoad = null;
            }

            GameScreen.addEffectEnd((short)85, 0, super.x, super.y, 500, super.Dir, super.objMainEff);
            super.isEff = true;
            super.fRemove = 1;
            return true;
         case 47:
         case 48:
            this.createSanji1();
            break;
         case 52:
         case 189:
         case 221:
         case 311:
            this.createNamiSkill1_L3();
            break;
         case 54:
            this.createMr3_2();
            break;
         case 57:
         case 64:
         case 66:
         case 206:
         case 207:
            this.createUssop2();
            break;
         case 58:
            this.createUssopSkill1_Lv3();
            break;
         case 59:
         case 60:
            this.createMissGold_1();
            break;
         case 61:
            this.createLapin();
            break;
         case 62:
            this.createMon29();
            break;
         case 63:
         case 190:
         case 222:
         case 312:
            this.createNami1_SHORT();
            break;
         case 65:
         case 70:
         case 107:
            this.createGhin_1();
            break;
         case 67:
         case 68:
         case 69:
         case 194:
         case 226:
            this.create_Ussop_S3_L1();
            break;
         case 71:
         case 145:
         case 146:
         case 147:
         case 148:
            this.createMon2();
            break;
         case 72:
         case 92:
            this.createMon3();
            break;
         case 73:
         case 74:
            this.createMon_4_5();
            break;
         case 75:
            this.createMon6();
            break;
         case 76:
            this.createAlvida1();
            break;
         case 77:
            this.createAlvida2();
            break;
         case 78:
            super.fRemove = 6;
            break;
         case 79:
            super.fRemove = 8;
            break;
         case 81:
         case 143:
         case 149:
            this.createMon_10();
            break;
         case 82:
         case 144:
            this.createMon_11();
            break;
         case 83:
            super.fRemove = 16;
            if (super.objFireMain.type_left_right == 0) {
               super.Dir = 0;
            } else {
               super.Dir = 2;
            }
            break;
         case 84:
         case 181:
         case 213:
         case 272:
            this.createLuffy_New2_SHORT();
            break;
         case 85:
         case 182:
         case 214:
         case 273:
            this.createLuffy_New3();
            break;
         case 86:
         case 183:
         case 215:
            this.createZoro_S1_L3_SHORT();
            break;
         case 87:
         case 184:
         case 216:
            this.createZoro_New2();
            break;
         case 88:
            GameScreen.addEffectEnd((short)30, 0, super.x, super.y - 30, 300, super.Dir, super.objMainEff);
            super.fRemove = 8;
            this.addSound((byte)3);
            break;
         case 89:
            this.createMorgan_2();
            break;
         case 90:
         case 91:
            super.fRemove = 1;
            break;
         case 93:
            super.toY = this.objBeFireMain.y;
            super.fRemove = 32;
            super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
            break;
         case 94:
            this.createMohji_2();
            break;
         case 95:
            this.createBuggy_1();
            break;
         case 96:
            this.createBuggy_2();
            break;
         case 97:
            this.createCabaji_1();
            break;
         case 99:
            this.createNyaban_1();
            break;
         case 100:
            this.createNyaban_2();
            break;
         case 101:
            this.createNyaban_3();
            break;
         case 102:
            this.createJango_1();
            break;
         case 103:
            this.createKuro_1();
            break;
         case 104:
            this.createKuro_2();
            break;
         case 105:
            this.createPearl_1();
            break;
         case 106:
            this.createPearl_2();
            break;
         case 108:
            this.createGhin_2();
            break;
         case 109:
            this.createDonKrieg_1();
            break;
         case 110:
            this.createDonKrieg_2();
            break;
         case 111:
            this.createDonKrieg_3();
            break;
         case 112:
            super.numNextFrame = 2;
            super.fraImgEff = new FrameImage(140, 70, 70);
            if (super.Dir == 0) {
               super.x -= 20;
            } else {
               super.x += 20;
            }

            super.fRemove = 15;
            break;
         case 113:
         case 150:
         case 151:
         case 152:
         case 153:
            this.createHachi_2();
            break;
         case 114:
            this.createChu_1();
            break;
         case 115:
            this.createChu_2();
            break;
         case 116:
            this.createKurobi_1();
            break;
         case 117:
            this.createKurobi_2();
            break;
         case 118:
            this.createArlong_1();
            break;
         case 119:
            this.createArlong_2();
            break;
         case 120:
            this.createArlong_3();
            break;
         case 121:
            this.create_Zoro_S3_L1();
            break;
         case 122:
            this.create_Zoro_S3_L2();
            break;
         case 123:
         case 185:
         case 217:
         case 283:
            this.create_Zoro_S3_L3();
            break;
         case 124:
         case 186:
         case 218:
            this.createSanji_s1_l3_SHORT();
            break;
         case 125:
         case 187:
            this.createSanji_s2_l3_New_SHORT();
            break;
         case 126:
         case 192:
            this.createUssopSkill1_Lv3_SHORT();
            break;
         case 127:
         case 193:
         case 225:
         case 302:
            this.create_Ussop_S2_L3();
            break;
         case 128:
            super.fRemove = 10;
            break;
         case 129:
         case 130:
            super.fRemove = 16;
            break;
         case 131:
            super.fRemove = 6;
            break;
         case 132:
            super.fRemove = 10;
            break;
         case 133:
            super.fraImgEff = new FrameImage(193, 25, 15);
            super.fraImgSubEff = new FrameImage(68, 28, 44);
            super.fRemove = 15;
            super.vMax = 18;
            break;
         case 134:
         case 135:
            super.fraImgEff = new FrameImage(193, 25, 15);
            super.fraImgSubEff = new FrameImage(68, 28, 44);
            super.fraImgSub2Eff = new FrameImage(194, 48, 34, 1);
            if (super.typeEffect == 135) {
               super.fraImgSub3Eff = new FrameImage(30, 38, 38);
            }

            super.fRemove = 20;
            super.vMax = 18;
            break;
         case 136:
            super.fraImgEff = new FrameImage(183, 20, 54);
            super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
            super.fRemove = 15;
            super.y = super.objFireMain.y;
            break;
         case 137:
         case 138:
            super.fraImgEff = new FrameImage(183, 20, 54);
            super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
            super.fRemove = 20;
            super.y = super.objFireMain.y;
            break;
         case 139:
            super.fRemove = 20;
            this.createNamiSea1_2();
            break;
         case 140:
            super.fRemove = 40;
            this.createNamiSea3();
            break;
         case 141:
            this.createUssopSea2();
            break;
         case 142:
            this.createUssopSea3();
            break;
         case 154:
            this.createZoro1();
            break;
         case 155:
            this.createZoro2();
            break;
         case 156:
            super.fRemove = 33;
            break;
         case 157:
            this.createZoro_New1();
            break;
         case 158:
         case 177:
            this.createSanji_s1_l3_New();
            break;
         case 159:
            this.createUssopSkill1_Lv3_New();
            break;
         case 160:
            this.createLuffy_New2();
            break;
         case 161:
            this.createZoro_New2_SHORT();
            break;
         case 162:
            this.createSanji_s2_l3_New();
            break;
         case 164:
         case 227:
            this.createCausu_1();
            break;
         case 165:
            this.addSoundBuff();
            super.isEff = true;
            super.Dir = (byte)super.objFireMain.type_left_right;
            this.addSoundBuff();
            GameScreen.addEffectEnd((short)85, 0, super.x, super.y, 900, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)85, 0, super.x, super.y, 900, super.Dir, super.objMainEff);
            super.y = super.objFireMain.y;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.fraImgEff = new FrameImage(101, 40, 47);
            super.fRemove = 40;
            return true;
         case 166:
            super.isEff = true;
            super.Dir = (byte)super.objFireMain.type_left_right;
            this.addSoundBuff();
            super.y = super.objFireMain.y;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.fraImgEff = new FrameImage(101, 40, 47);
            super.fRemove = 20;
            return true;
         case 167:
            super.fraImgEff = new FrameImage(152, 25, 21);
            super.fraImgSubEff = new FrameImage(201, 64, 50, 45, 35);
            super.fraImgSub2Eff = new FrameImage(217, 39, 18);
            super.fraImgSub3Eff = new FrameImage(92, 64, 126, 45, 89, 1);
            super.BP = new FrameImage(218, 64, 64);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y + 10;
            super.fRemove = 30;
            this.x1000 = super.x;
            this.y1000 = super.y;
            super.levelPaint = -1;
            break;
         case 168:
            super.fRemove = 10;
            super.fraImgEff = new FrameImage(255, 42, 50, 3);
            super.y = super.objFireMain.y;
            break;
         case 169:
         case 237:
            super.fraImgEff = new FrameImage(240, 30, 73, 1);
            super.fraImgSubEff = new FrameImage(241, 40, 27, 2);
            super.fraImgSub2Eff = new FrameImage(104, 30, 30);
            super.fraImgSub3Eff = new FrameImage(242, 49, 28, 2);
            super.BP = new FrameImage(243, 36, 39);
            super.fRemove = 33;
            super.x = this.objBeFireMain.x;
            super.y = this.objBeFireMain.y;
            this.y1000 = 240;
            break;
         case 170:
         case 238:
            super.fraImgEff = new FrameImage(244, 20, 37, 3);
            super.fraImgSubEff = new FrameImage(152, 25, 21);
            super.BP = new FrameImage(243, 36, 39);
            super.fraImgSub2Eff = new FrameImage(240, 30, 73, 1);
            super.fraImgSub3Eff = new FrameImage(241, 40, 27, 2);
            super.fRemove = 43;
            super.vMax = 30;
            if (this.isAddSound) {
               this.addSoundBuffShort();
            }
            break;
         case 171:
         case 239:
            super.y = this.objBeFireMain.y;
            super.x = this.objBeFireMain.x;
            super.fraImgEff = new FrameImage(118, 62, 64, 47, 48);
            super.fraImgSubEff = new FrameImage(174, 40, 40);
            super.fraImgSub2Eff = new FrameImage(247, 49, 28);
            super.fraImgSub3Eff = new FrameImage(254, 30, 40);
            super.vMax = 16;
            super.fRemove = 24;
            break;
         case 172:
         case 240:
            super.fRemove = 24;
            super.fraImgEff = new FrameImage(254, 30, 40);
            if (this.isAddSound) {
               this.addSoundBuffShort();
            }
            break;
         case 173:
            super.fRemove = 8;
            super.fraImgEff = new FrameImage(257, 15, 51);
            super.fraImgSubEff = new FrameImage(3, 30, 50);
            super.vMax = 12;
            super.x += super.am_duong * 30;
            break;
         case 174:
            super.fRemove = 12;
            super.fraImgEff = new FrameImage(219, 47, 7);
            super.vMax = 7;
            break;
         case 175:
            super.levelPaint = -1;
            super.fRemove = 8;
            super.fraImgEff = new FrameImage(258, 35, 28);
            break;
         case 178:
            super.fraImgEff = new FrameImage(266, 80, 100, 64, 80, 2);
            super.fraImgSubEff = new FrameImage(201, 64, 50, 45, 35);
            super.fRemove = 35;
            GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne, 200, super.Dir, super.objMainEff);
            super.x -= super.am_duong * 15;
            super.y = super.objFireMain.y;
            super.vMax = 10;
            super.frame = -1;
            break;
         case 179:
         case 241:
            super.fRemove = 26;
            if (super.objFireMain.vecEffBuff != null) {
               for(num17 = 0; num17 < super.objFireMain.vecEffBuff.size(); ++num17) {
                  if (((MainBuff)super.objFireMain.vecEffBuff.elementAt(num17)).IdBuff == 2037) {
                     super.fraImgEff = new FrameImage(267, 46, 53);
                     super.fraImgSubEff = new FrameImage(270, 80, 47);
                     super.fraImgSub2Eff = new FrameImage(271, 130, 80, 3);
                     super.fraImgSub3Eff = new FrameImage(272, 50, 24);
                     if (super.typeEffect == 241) {
                        super.BP = new FrameImage(224, 22, 28);
                     }

                     super.frame = 1;
                     break;
                  }
               }
            }

            if (super.fraImgEff == null) {
               super.fraImgEff = new FrameImage(10, 40, 47);
               super.fraImgSubEff = new FrameImage(260, 54, 54, 1);
               super.frame = 0;
            }

            if (super.typeEffect == 241) {
               super.step = 1;
            }
            break;
         case 180:
         case 212:
            super.fRemove = 20;
            if (super.objFireMain.type_left_right == 0) {
               super.Dir = 0;
            } else {
               super.Dir = 2;
            }

            if (super.typeEffect == 212) {
               super.fraImgEff = new FrameImage(61, 24, 30);
            }
            break;
         case 195:
            super.fraImgEff = new FrameImage(238, 30, 73);
            super.fraImgSubEff = new FrameImage(195, 40, 27);
            super.fRemove = 20;
            GameScreen.addEffectEnd((short)30, 0, super.x - super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2 - 5, 300, super.Dir, super.objMainEff);
            break;
         case 196:
            super.fraImgEff = new FrameImage(225, 24, 32);
            super.fraImgSubEff = new FrameImage(286, 50, 100);
            super.fraImgSub2Eff = new FrameImage(98, 78, 70);
            GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 800, super.Dir, super.objMainEff);
            super.fRemove = 25;
            break;
         case 197:
            super.fraImgEff = new FrameImage(287, 76, 27);
            super.fRemove = 8;
            super.x += super.am_duong * 20;
            super.y -= 10;
            break;
         case 198:
            super.fraImgEff = new FrameImage(288, 30, 30);
            super.fRemove = 20;
            super.vMax = 12;
            super.x += super.am_duong * 25;
            break;
         case 199:
            super.fraImgEff = new FrameImage(291, 47, 48);
            super.fRemove = 16;
            super.y = super.objMainEff.y;
            break;
         case 200:
            super.Dir = (byte)super.objFireMain.type_left_right;
            super.am_duong = -1;
            if (super.Dir == 2) {
               super.am_duong = 1;
            }

            super.fraImgEff = new FrameImage(292, 78, 24);
            super.fraImgSubEff = new FrameImage(293, 50, 14);
            super.fRemove = 16;
            super.vMax = 10;
            super.objFireMain.NF = false;
            super.x += super.am_duong * 30;
            super.y -= 5;
            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 800, super.Dir, super.objMainEff);
            break;
         case 201:
            super.fRemove = 16;
            super.vMax = 10;
            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 400, super.Dir, super.objMainEff);
            super.y = super.objFireMain.y;
            break;
         case 202:
            super.fraImgEff = new FrameImage(295, 34, 24);
            super.vMax = 12;
            GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne - 10, 400, super.Dir, super.objMainEff);
            super.fRemove = 16;
            break;
         case 203:
            super.fraImgEff = new FrameImage(296, 36, 63);
            super.fRemove = 16;
            super.y = super.objMainEff.y;
            break;
         case 204:
            super.fRemove = 26;
            super.fraImgSubEff = new FrameImage(297, 83, 47);
            super.fraImgSub2Eff = new FrameImage(272, 50, 24);
            break;
         case 205:
            super.fraImgEff = new FrameImage(224, 22, 28);
            super.fraImgSubEff = new FrameImage(32, 45, 45);
            super.fRemove = 60;
            break;
         case 208:
            this.create_Eff_Tru();
            break;
         case 209:
         case 242:
            this.create_Eff_Lucci_1();
            break;
         case 210:
         case 243:
            this.create_Eff_Dong_Dat_1();
            break;
         case 211:
         case 244:
            this.create_Eff_Dong_Dat_2();
            break;
         case 219:
            super.fraImgEff = new FrameImage(323, 92, 64);
            super.fraImgSubEff = new FrameImage(183, 20, 54);
            super.fRemove = 26;
            GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 15, super.y, 200, super.Dir, super.objMainEff);
            super.mframe = new int[]{-1, -1, -1, -1, -1, 0, 0, 1, 1, -1, 2, 2, 2, 4, 4, 5, 5, -1, 6, 6, 7, -1};
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
            break;
         case 224:
         case 301:
            this.create_Ussop_S1_L5();
            break;
         case 233:
            super.fraImgEff = new FrameImage(107, 50, 54);
            super.fRemove = 20;
            break;
         case 245:
         case 251:
            super.fraImgEff = new FrameImage(357, 100, 100, 2);
            super.fraImgSubEff = new FrameImage(358, 51, 22);
            super.fRemove = 22;
            super.x += super.am_duong * 30;
            this.CM = new int[][]{new int[3], {0, 10, 0}, {0, 25, 0}, {1, 0, -15}, {1, 10, -5}, {1, 20, 5}, {2, 10, 0}, {2, 15, 5}, {2, 30, 15}, {3, 0, 0}, {3, 10, 0}, {3, 30, 0}};
            break;
         case 246:
         case 253:
            super.fraImgEff = new FrameImage(351, 35, 62);
            super.fraImgSubEff = new FrameImage(354, 40, 47);
            super.fRemove = 26;
            if (this.isAddSound) {
               this.addSoundBuffShort();
            }

            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 400, super.Dir, super.objMainEff);
            break;
         case 247:
         case 254:
            super.y = super.objFireMain.y - super.objFireMain.hOne + 8;
            super.fraImgEff = new FrameImage(352, 52, 15);
            super.fraImgSubEff = new FrameImage(353, 9, 7);
            super.fraImgSub2Eff = new FrameImage(355, 9, 10);
            super.fraImgSub3Eff = new FrameImage(224, 22, 28);
            super.fRemove = 24;
            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 400, super.Dir, super.objMainEff);
            super.vMax = 140;
            if (CRes.abs(super.objFireMain.x - this.objBeFireMain.x) < 32) {
               MainObject var7 = super.objFireMain;
               var7.x -= super.am_duong << 5;
            }
            break;
         case 248:
         case 255:
            this.createKilo_1();
            break;
         case 249:
         case 252:
            super.fraImgEff = new FrameImage(357, 100, 100, 2);
            super.fraImgSubEff = new FrameImage(359, 64, 64);
            super.fraImgSub2Eff = new FrameImage(183, 20, 54);
            super.fRemove = 22;
            super.x += super.am_duong * 30;
            this.addSoundBuffShort();
            this.CM = new int[][]{new int[3], {0, 10, 0}, {0, 35, 0}, {1, 0, -15}, {1, 10, -5}, {1, 30, 5}, {2, 10, 0}, {2, 15, 5}, {2, 40, 15}, {3, 0, 0}, {3, 10, 0}, {3, 40, 0}};
            break;
         case 250:
            this.create_Eff_Tru_2();
            break;
         case 266:
            this.createRankyaku();
            break;
         case 267:
            this.createShigan();
            break;
         case 268:
         case 269:
            this.createDoor();
            break;
         case 270:
            super.numNextFrame = 2;
            super.fraImgEff = new FrameImage(427, 4);
            if (super.Dir == 0) {
               super.x -= 20;
            } else {
               super.x += 20;
            }

            super.fRemove = 15;
            break;
         case 271:
            super.fRemove = 15;
            if (super.objFireMain.type_left_right == 0) {
               super.Dir = 0;
            } else {
               super.Dir = 2;
            }

            super.fraImgEff = new FrameImage(61, 24, 30);
            break;
         case 274:
         case 275:
            super.fRemove = 15;
            super.fraImgEff = new FrameImage(431, 2);
            break;
         case 276:
         case 277:
            this.createSoi();
            break;
         case 278:
         case 279:
            this.createHuou();
            break;
         case 280:
            super.fraImgEff = new FrameImage(mImage.createImage("/eff/khungthanh.png"), 1);
            super.fraImgSubEff = new FrameImage(mImage.createImage("/eff/ball.png"), 4);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
            super.toX = super.x + 200;
            super.toY = super.y - 30;
            super.vx = 5;
            super.vy = -1;
            super.vMax = 10;
            xdich = super.toX - super.x;
            ydich = super.toY - super.y - CRes.random(20);
            this.create_Speed(xdich, ydich, (Point_Focus)null);
            super.fRemove = 40;
            super.objFireMain.Dir = 2;
            break;
         case 281:
            this.createZoro_S1_L6();
            break;
         case 282:
            super.fRemove = 34;
            super.vMax = 12;
            super.fraImgEff = new FrameImage(413, 91, 73);
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
            super.fraImgSubEff = new FrameImage(415, 3);
            break;
         case 291:
            super.fraImgEff = new FrameImage(323, 92, 64);
            super.fraImgSubEff = new FrameImage(183, 20, 54);
            super.fRemove = 18;
            GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 15, super.y, 200, super.Dir, super.objMainEff);
            super.mframe = new int[]{4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 6, 6, 6, -1};
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
            break;
         case 292:
            super.fraImgEff = new FrameImage(323, 92, 64);
            super.fraImgSubEff = new FrameImage(183, 20, 54);
            super.fRemove = 26;
            GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 15, super.y, 200, super.Dir, super.objMainEff);
            super.mframe = new int[]{4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 6, 6, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
            break;
         case 303:
            this.create_Ussop_S3_L6();
            break;
         case 400:
            num17 = 0;
            while(true) {
               if (num17 >= super.vecObjsBeFire.size()) {
                  break label442;
               }
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(num17)) != null && (var5 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                  GameScreen.addHightDataeff((short)2, var5.x, var5.y);
                  LoadMap.timeVibrateScreen = CRes.random(6, 20);
                  GameScreen.addEffectEnd((short)112, 0, var5.x, var5.y, super.Dir, super.objMainEff);
                  this.VecSubEff.addElement(new Point(var5.x, var5.y));
               }
               ++num17;
            }
         case 401://Xoáy đen
            super.levelPaint = 1;
            super.x = this.objBeFireMain.x;
            super.y = this.objBeFireMain.y;
            GameScreen.addHightDataeff((short)4, super.x, super.y);
            GameScreen.addEffectEnd((short)63, 0, super.x, super.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)110, 0, super.x, super.y, super.Dir, super.objMainEff);
            LoadMap.timeVibrateScreen = CRes.random(1, 5);
            this.removeEff();
            break;
         case 402:
            this.VecSubEff = new mVector();

            for(num17 = 0; num17 < super.vecObjsBeFire.size(); ++num17) {
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(num17)) != null && (var5 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                  GameScreen.addHightDataeff((short)2, var5.x, var5.y);
                  LoadMap.timeVibrateScreen = CRes.random(6, 20);
                  GameScreen.addEffectEnd((short)112, 0, var5.x, var5.y, super.Dir, super.objMainEff);
                  this.VecSubEff.addElement(new Point(var5.x, var5.y));
               }
            }

            super.frameSuper = 4;
            super.fraImgEff = new FrameImage(32, 45, 45, (byte)5, super.frameSuper);
            super.fRemove = 30;
            super.vMax = 12;
            super.y = super.objFireMain.y;
            break;
         case 403:
            super.levelPaint = 1;
            super.x = this.objBeFireMain.x;
            super.y = this.objBeFireMain.y;
            GameScreen.addHightDataeff((short)8, super.x, super.y);
            GameScreen.addEffectEnd((short)63, 0, super.x, super.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)110, 0, super.x, super.y, super.Dir, super.objMainEff);
            LoadMap.timeVibrateScreen = CRes.random(1, 5);
            this.removeEff();
            break;
         case 471:
            super.fRemove = 21;
            if (super.objFireMain.type_left_right == 0) {
               super.Dir = 0;
            } else {
               super.Dir = 2;
            }

            super.fraImgEff = new FrameImage(61, 24, 30);
            break;
         case 472:
            this.create_Luffy_S2_L7();
            break;
         case 473:
            this.create_Luffy_S3_L7();
            break;
         case 481:
            this.create_Zoro_S1_L7();
            break;
         case 482:
            super.fRemove = 42;
            super.vMax = 12;
            super.fraImgEff = new FrameImage(413, 91, 73);
            super.mframe = new int[]{-2, -2, -2, -2, -2, -2, 0, 1, 2, -1, -1, -2, -2, -2, -2, 0, 1, 2, -1, -1, -2, -2, -2, -2, 0, 1, 2, -1, -1, -2, -2, -2, -2, 0, 1, 2, -1, -1, -2, -2, -2, -2};
            super.fraImgSubEff = new FrameImage(440, 12);
            super.mframeSub = new int[]{0, 0, 1, 1, 2, 2, -1, -1, -1, 3, 3, 4, 4, 5, 5, -1, -1, -1, 6, -1, 7, -1, 8, 8, -1, -1, -1, 9, -1, 10, -1, 11, 11, -1, -1, -1, 9, -1, 10, -1, 11, 11};
            this.x1000 = super.x + 30 * super.am_duong;
            num17 = this.x1000 - super.x;
            this.VecSubEff.addElement(this.create_Speed(num17, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 3, super.toX, super.toY));
            this.VecSubEff.addElement(this.create_Speed(-num17, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 3, super.toX, super.toY));
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
            break;
         case 483:
            this.create_Zoro_S3_L7();
            break;
         case 491:
            super.fraImgEff = new FrameImage(323, 92, 64);
            super.fraImgSub2Eff = new FrameImage(460, 13);
            super.fraImgSubEff = new FrameImage(183, 20, 54);
            super.fRemove = 29;
            GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 15, super.y, 200, super.Dir, super.objMainEff);
            super.mframe = new int[]{4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 6, 6, 6, -1};
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
            break;
         case 492:
            super.fraImgEff = new FrameImage(323, 92, 64);
            super.fraImgSubEff = new FrameImage(183, 20, 54);
            super.fraImgSub2Eff = new FrameImage(468, 6);
            super.fRemove = 26;
            GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 15, super.y, 200, super.Dir, super.objMainEff);
            super.mframe = new int[]{4, 4, 5, 5, 4, 4, 5, 5, 4, 4, 5, 5, 6, 6, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
            break;
         case 493:
            this.create_Sanji_S3_L7();
            break;
         case 501:
            this.create_Ussop_S1_L7();
            break;
         case 502:
            this.create_Ussop_S2_L7();
            break;
         case 503:
            this.create_Ussop_S3_L7();
            break;
         case 511:
            this.create_Nami_S1_L7();
            break;
         case 512:
            this.create_Nami_S2_L7();
            break;
         case 513:
            this.create_Nami_S3_L7();
            break;
         case 1998:
         case 1999:
            this.createEffThunderFalls();
            break;
         case 2000:
            this.createEffFireExplore();
            break;
         case 10001:
            super.fraImgEff = new FrameImage(173, 70, 42, 50, 30);
            super.fraImgSubEff = new FrameImage(172, 60, 43);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.levelPaint = -1;
            break;
         case 10002:
            super.fraImgEff = new FrameImage(76, 32, 70);
            super.fraImgSubEff = new FrameImage(129, 40, 80);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
            super.fRemove = 22;
            break;
         case 10003:
            super.fraImgEff = new FrameImage(77, 64, 75, 43, 50);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 4;
            break;
         case 10004:
            super.fraImgEff = new FrameImage(174, 40, 40);
            super.fraImgSubEff = new FrameImage(26, 40, 40);
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
            super.fRemove = 30;
            break;
         case 10005:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            break;
         case 10006:
         case 10011:
            super.fraImgSubEff = new FrameImage(172, 60, 43);
            super.levelPaint = -1;
            break;
         case 10007:
            super.fraImgEff = new FrameImage(118, 62, 64, 47, 48);
            super.fraImgSubEff = new FrameImage(173, 70, 42, 50, 30);
            break;
         case 10008:
            super.levelPaint = -1;
            super.fraImgEff = new FrameImage(175, 13, 11);
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            break;
         case 10009:
            super.objFireMain.Dir = super.Dir;
            super.fRemove = 30;
            break;
         case 10010:
         case 10013:
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.fraImgEff = new FrameImage(178, 70, 65);
            super.numNextFrame = 2;
            break;
         case 10012:
            this.createXerath3();
            break;
         case 10015:
            this.createUrgot3();
            break;
         case 10017:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 4;
            super.fraImgEff = new FrameImage(180, 32, 63);
            super.numNextFrame = 3;
            break;
         case 10018:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
            super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
            break;
         case 10019:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
            super.fRemove = 8;
            break;
         case 10020:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.fraImgEff = new FrameImage(189, 37, 62);
            super.numNextFrame = 3;
            super.levelPaint = -1;
            break;
         case 10021:
         case 10022:
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.fraImgEff = new FrameImage(181, 47, 63, 38, 51);
            super.numNextFrame = 3;
            super.levelPaint = -1;
            break;
         case 10023:
            super.fRemove = 4;
            break;
         case 10024:
            this.setAngle();
            super.objFireMain.Dir = super.Dir;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.fraImgEff = new FrameImage(181, 47, 63, 38, 51);
            super.fraImgSubEff = new FrameImage(172, 60, 43);
            super.numNextFrame = 3;
            super.levelPaint = -1;
            break;
         case 10025:
            this.setAngle();
            super.objFireMain.Dir = super.Dir;
            this.createMonster_NEM_BOOM_2();
            break;
         case 10026:
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            super.fraImgEff = new FrameImage(182, 56, 80, 40, 57);
            super.numNextFrame = 2;
            super.levelPaint = -1;
            break;
         case 10027:
            for(num17 = 0; num17 < this.CT.size(); ++num17) {
               Point_Focus var4 = (Point_Focus)this.CT.elementAt(num17);
               GameScreen.addEffectEnd_ObjTo((short)22, 0, var4.x, var4.y - 30, (short)super.objFireMain.ID, (byte)super.objFireMain.typeObject, (byte)super.objFireMain.Dir, super.objMainEff);
            }

            super.fRemove = 10;
            break;
         case 10028:
            super.fraImgSub3Eff = new FrameImage(242, 49, 28, 2);
            super.fRemove = 33;
            super.x = this.objBeFireMain.x;
            super.y = this.objBeFireMain.y;
            this.y1000 = 240;

            for(num17 = 0; num17 < super.vecObjsBeFire.size(); ++num17) {
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(num17)) != null) {
                  GameScreen.AA((short)-1, super.objFireMain, var2, super.x + this.CW[CRes.random(this.CW.length - 1)], super.y - 200 + CRes.random_Am(-10, 10));
                  GameScreen.AA((short)-1, super.objFireMain, var2, super.x + this.CW[CRes.random(this.CW.length - 1)], super.y - 200 + CRes.random_Am(-10, 10));
                  GameScreen.AA((short)-1, super.objFireMain, var2, super.x + this.CW[CRes.random(this.CW.length - 1)], super.y - 200 + CRes.random_Am(-10, 10));
               }
            }

            GameScreen.addEffectEnd((short)112, 0, super.x, super.y + 10, super.Dir, super.objMainEff);
            break;
         case 10030:
            this.create_ho_den_vu_tru();
         }

         if (super.objFireMain == GameScreen.player) {
            for(num17 = 0; num17 < super.vecObjsBeFire.size(); ++num17) {
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(num17)) != null) {
                  if (GameScreen.typePaintGameScreen == 1) {
                     if ((var5 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                        var5.NG = true;
                     }
                  } else if ((var5 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null && var5.typeObject == 1 && (ydich = CRes.abs(super.objFireMain.x - var5.x)) < 32) {
                     var5.x += super.am_duong * (ydich - 32 + 10);
                     var5.IQ = (54 - ydich) / 2 * super.am_duong;
                     if (var5.Action != 4 && var5.Action != 2 && var5.Hp > 0) {
                        var5.Action = 3;
                        var5.f = 0;
                        var5.resetAction();
                     } else {
                        var5.IQ = 0;
                        var5.dy = 0;
                     }
                  }
               }
            }
         }

         if (!super.isEff) {
            setHP_New(super.vecObjsBeFire, super.objFireMain, false);
            if (super.vecObjsBeFire.size() == 0) {
               super.isStop = true;
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void createDanFocus() {
      switch(CRes.random(4)) {
      case 0:
         this.gocT_Arc = 90;
         break;
      case 1:
         this.gocT_Arc = 270;
         break;
      case 2:
         this.gocT_Arc = 180;
         break;
      case 3:
         this.gocT_Arc = 0;
      }

      super.va = 4096;
      super.vx = 0;
      super.vy = 0;
      super.life = 0;
      this.CZ = super.va * CRes.getcos(this.gocT_Arc) >> 10;
      this.DA = super.va * CRes.getsin(this.gocT_Arc) >> 10;
   }

   private void create_Eff_Tru_2() {
      super.fraImgEff = new FrameImage(100, 15, 20);
      super.y = super.objFireMain.y - 55;
      if (super.objFireMain.IdIcon == 58) {
         super.fraImgEff = new FrameImage(366, 15, 20);
         super.y = super.objFireMain.y - 80;
      }

      super.vMax = 20;

      for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
         Object_Effect_Skill var2;
         MainObject var6;
         if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var6 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
            Point_Focus var3 = new Point_Focus(super.x, super.y);
            int var4 = var6.x - super.x;
            int var5 = var6.y - var6.hOne / 2 - super.y;
            this.create_Speed(var4, var5, var3, super.x, super.y, var6.x, var6.y - var6.hOne / 2);
            var3.dis = 0;
            if (var6.x > super.x) {
               var3.dis = 2;
            }

            this.VecEff.addElement(var3);
         }
      }

   }

   private void createKilo_1() {
      super.fraImgEff = new FrameImage(356, 40, 80);
      super.fraImgSubEff = new FrameImage(183, 20, 54);
      super.toY = this.objBeFireMain.y;
      super.fRemove = 22;
   }

   private void create_Crocodile_1() {
      super.fRemove = 20;
      super.y = super.objFireMain.y;
      super.fraImgEff = new FrameImage(200, 54, 70, 40, 52);
      super.objFireMain.isTanHinh = true;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.typeEffect == 235) {
         super.vMax = 120;
         super.fraImgSubEff = new FrameImage(118, 62, 64);
      }

   }

   private void create_Ussop_S1_L5() {
      super.fraImgEff = new FrameImage(183, 20, 54);
      super.fraImgSubEff = new FrameImage(330, 46, 49);
      this.CM = new int[][]{{-5, -15}, {5, 15}, {15, -5}, {-15, 5}, {-10, -10}, {10, 10}};
      super.y = super.objFireMain.y;
      super.fRemove = 18;
      super.vMax = 24;
      if (super.typeEffect == 301) {
         this.x1000 = super.x + 30 * super.am_duong;
         int var10000 = this.x1000;
         var10000 = this.x1000;
         super.fraImgSub2Eff = new FrameImage(416, 4);
         int var1 = this.x1000 - super.x;
         this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 4 * 3, super.toX, super.toY));
         this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 2, super.toX, super.toY));
      }

   }

   private void create_Ussop_S1_L7() {
      super.fraImgEff = new FrameImage(183, 20, 54);
      super.fraImgSubEff = new FrameImage(330, 46, 49);
      this.CM = new int[][]{{-5, -15}, {5, 15}, {15, -5}, {-15, 5}, {-10, -10}, {10, 10}};
      super.y = super.objFireMain.y;
      super.fRemove = 18;
      super.vMax = 24;
      this.x1000 = super.x + 30 * super.am_duong;
      int var10000 = this.x1000;
      var10000 = this.x1000;
      super.fraImgSub2Eff = new FrameImage(453, 4);
      int var1 = this.x1000 - super.x;
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 4 * 3, super.toX, super.toY));
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne / 2, super.toX, super.toY));
   }

   private void create_Eff_Dong_Dat_2() {
      super.fraImgEff = new FrameImage(118, 62, 64);
      if (super.typeEffect == 244) {
         super.fraImgSubEff = new FrameImage(138, 62, 64);
      }

      super.fRemove = 30;
      GameScreen.addEffectEnd((short)30, 0, super.x + 10, super.objFireMain.y - super.objFireMain.hOne / 2, 600, super.Dir, super.objMainEff);
      GameScreen.addEffectEnd((short)30, 0, super.x - 10, super.objFireMain.y - super.objFireMain.hOne / 2, 600, super.Dir, super.objMainEff);
      super.y = super.objFireMain.y;
      if (this.isAddSound) {
         this.addSoundBuffShort();
      }

   }

   private void create_Eff_Dong_Dat_1() {
      super.fraImgEff = new FrameImage(310, 73, 59);
      super.fraImgSubEff = new FrameImage(311, 149, 179);
      GameScreen.addEffectEnd((short)30, 0, super.x + 10, super.objFireMain.y - super.objFireMain.hOne / 2, 400, super.Dir, super.objMainEff);
      GameScreen.addEffectEnd((short)30, 0, super.x - 10, super.objFireMain.y - super.objFireMain.hOne / 2, 400, super.Dir, super.objMainEff);
      super.fRemove = 50;
      if (super.objFireMain == GameScreen.player) {
         super.fRemove = 70;
      }

      if (this.isAddSound) {
         this.addSoundBuffShort();
      }

   }

   private void create_Eff_Lucci_1() {
      super.fraImgEff = new FrameImage(274, 23, 74, 3);
      super.frame = 0;
      super.vx = super.am_duong * 12;
      this.x1000 = super.x;
      GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2, 400, super.Dir, super.objMainEff);
      super.x = this.x1000 - super.am_duong * 24;
      super.fRemove = 20;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
         this.addSoundBuffShort();
      }

      if (super.objFireMain.vecEffBuff != null) {
         for(int var1 = 0; var1 < super.objFireMain.vecEffBuff.size(); ++var1) {
            MainBuff var2;
            if ((var2 = (MainBuff)super.objFireMain.vecEffBuff.elementAt(var1)).IdBuff == 2040 || var2.IdBuff == 2064) {
               super.fraImgSubEff = new FrameImage(273, 24, 24, 4);
               super.frame = 1;
               break;
            }

            if (var2.IdBuff == 2061) {
               super.fraImgSubEff = new FrameImage(273, 24, 24, 4);
               super.frame = 3;
               break;
            }
         }
      }

      if (super.frame == 0) {
         super.mframe = new int[]{1, 0, 1, 0, 1, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
      } else {
         super.mframe = new int[]{1, 0, 1, 0, 1, 0, 0, 1, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};
      }

      if (super.typeEffect == 242) {
         GameScreen.addEffectEnd((short)147, (byte)super.frame, super.x + super.am_duong * 120, super.objFireMain.y - super.objFireMain.hOne / 2, 400, super.Dir, super.objMainEff);
      }

   }

   private void create_Eff_Tru() {
      super.fraImgEff = new FrameImage(100, 15, 20);
      super.vMax = 20;
      super.y = super.objFireMain.y - 55;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void createMon29() {
      super.fraImgEff = new FrameImage(118, 62, 64, 47, 48);
      super.toY = this.objBeFireMain.y + 5;
      super.numNextFrame = 2;
      super.fRemove = 8;
   }

   private void createLapin() {
      super.vMax = 16;
      super.fraImgEff = new FrameImage(213, 15, 15);
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void AB(mGraphics var1) {
      if (!super.BQ) {
         if (super.typeEffect == 1999) {
            super.fraImgEff.drawFrame(super.AS, super.x + this.CU[0], super.y + this.CV[0], 0, 33, var1);
         }

         super.fraImgSub2Eff.drawFrame(super.AS, super.x, super.y, 0, 33, var1);
         super.fraImgSubEff.drawFrame(super.AS, super.x + this.CU[1], super.y + this.CV[1], 0, 33, var1);
      }

      if (!super.BR && super.typeEffect == 1999) {
         super.fraImgEff.drawFrame(super.AT, super.x + this.CU[2], super.y + this.CV[2], 0, 33, var1);
      }

      if (!super.BS) {
         super.fraImgSubEff.drawFrame(super.AU, super.x + this.CU[3], super.y + this.CV[3], 0, 33, var1);
      }

   }

   public final void paint(mGraphics var1) {
      try {
         int var2;
         int var3;
         int var4;
         boolean var5;
         Point_Focus var7;
         Point_Focus var8;
         Point var9;
         byte var10;
         Point var12;
         Point_Focus var13;
         int var14;
         Point var15;
         byte var16;
         Point var17;
         Point_Focus var19;
         byte var21;
         switch(super.typeEffect) {
         case -1:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);
               super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x, var17.y, 0, 3, var1);
            }

            return;
         case 0:
         case 36:
         case 61:
         case 71:
         case 76:
         case 81:
         case 143:
         case 145:
         case 146:
         case 148:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 1:
         case 37:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f % super.fraImgSubEff.nFrame, var15.x, var15.y, super.Dir, 3, var1);
            }

            return;
         case 2:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrameNew(var15.frame, var15.x, var15.y, 0, 33, var1);
            }

            return;
         case 3:
         case 229:
         case 262:
         case 263:
         case 264:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).frame == 0) {
                  var5 = false;
                  if (var15.f < var15.fRe - 3) {
                     var14 = var15.f % 2;
                  } else {
                     var14 = super.fraImgSubEff.maxNumFrame - (var15.fRe - var15.f);
                  }

                  super.fraImgSubEff.drawFrameNew_BeginSuper(var14, var15.x / 1000, var15.y / 1000, 0, 3, var1);
               } else {
                  var5 = false;
                  if (var15.f < var15.fRe - 3) {
                     var14 = (var15.f + var15.fSmall) % 3;
                  } else {
                     var14 = super.fraImgEff.maxNumFrame - (var15.fRe - var15.f);
                  }

                  super.fraImgEff.drawFrameNew_BeginSuper(var14, var15.x / 1000, var15.y / 1000, 0, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var15 = (Point)this.VecSubEff.elementAt(var2)).frame == 0) {
                  super.fraImgSubEff.drawFrameNew_BeginSuper(var15.f % super.fraImgSubEff.maxNumFrame, var15.x, var15.y, 0, 3, var1);
               } else if (var15.frame == 1) {
                  super.fraImgSub2Eff.drawFrameNew_BeginSuper(var15.f / 2 % 3, var15.x, var15.y, 0, 33, var1);
               }
            }

            if (super.f >= 13 && super.f <= 23) {
               super.fraImgSub3Eff.drawFrameNew_BeginSuper(super.f / 2 % 3, super.x, super.y + 8, 0, 33, var1);
               return;
            }

            if (super.f >= 8 && super.f <= 28) {
               super.fraImgEff.drawFrameNew_BeginSuper(super.f % 5, super.x, super.y + 3, 0, 33, var1);
               return;
            }
            break;
         case 4:
         case 230:
            if (super.f >= 0 && super.f < super.mframe.length) {
               super.fraImgSub2Eff.drawFrame(super.mframe[super.f], super.x, super.y + 4, 0, 33, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgSub3Eff.drawFrame(var15.f / 2 % super.fraImgSub3Eff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).f >= var15.fSmall) {
                  if (var15.frame == 0) {
                     super.fraImgEff.drawFrame(0, var15.x, var15.y, 0, 3, var1);
                  } else if (var15.frame == 1 && super.fraImgEff.getImageFrame() != null) {
                     var1.drawRegion(super.fraImgEff.getImageFrame(), 0, 0, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight - var15.dis, 0, var15.x, var15.y, 33);
                  }
               }
            }

            return;
         case 5:
         case 231:
            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgSub3Eff.drawFrame(var15.f / 2 % super.fraImgSub3Eff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            if (super.f >= 10 && super.f <= 15) {
               super.fraImgEff.drawFrame(0, super.x + this.CL[0][0] * super.am_duong, super.y + this.CL[0][1], super.Dir, 3, var1);
            }

            if (super.f > 15 && super.f <= 17) {
               super.fraImgEff.drawFrame(1, super.x + this.CL[1][0] * super.am_duong, super.y + this.CL[1][1], super.Dir, 3, var1);
            }

            if (super.f > 17 && super.f <= 26) {
               super.fraImgSubEff.drawFrame((super.f - 18) / 3, super.x + this.CL[2][0] * super.am_duong, super.y + this.CL[2][1], super.Dir, 3, var1);
               return;
            }
            break;
         case 6:
         case 232:
            if (super.f >= 20 && super.f <= 24) {
               super.fraImgEff.drawFrame((super.f - 30) / 2, super.x, super.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var15 = (Point)this.VecSubEff.elementAt(var2)).frame == 1) {
                  super.fraImgSub3Eff.drawFrame(3 + var15.f % 3, var15.x, var15.y, 0, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrame(var15.f % super.fraImgSub2Eff.nFrame, var15.x, var15.y, 0, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var13.frame / 2, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 7:
         case 141:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               this.AA(var1, super.fraImgEff, var13.frame, var13.x, var13.y);
            }

            return;
         case 9:
         case 53:
         case 163:
            if (super.f < 3) {
               super.fraImgEff.drawFrame(super.f, super.x, super.y, super.Dir, 3, var1);
               return;
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + var13.AG % super.fraImgSubEff.maxNumFrame, var13.x, var13.y, 0, 3, var1);
               if (super.typeEffect != 9) {
                  super.fraImgSub2Eff.drawFrame(CRes.random(super.fraImgSub2Eff.nFrame), var13.x, var13.y, 0, 3, var1);
               }
            }

            return;
         case 10:
         case 234:
            if (!this.checkNullObject((int)1)) {
               if (super.f >= 7) {
                  super.fraImgEff.drawFrame((super.f - 7) / 2, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
               }

               if (super.f >= 7 && super.f <= 16) {
                  super.fraImgSubEff.drawFrame((super.f - 11) / 2 % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy + 5, super.Dir, 33, var1);
               }

               if (super.f >= 24 && super.f <= 29) {
                  super.fraImgSubEff.drawFrame((2 - (super.f - 34)) / 2 % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy + 5, super.Dir, 33, var1);
               }
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var15 = (Point)this.VecSubEff.elementAt(var2)).frame == 1) {
                  super.BP.drawFrame(3 + var15.f % 3, var15.x, var15.y, 0, 3, var1);
               } else {
                  super.fraImgSub3Eff.drawFrame(var15.f % super.fraImgSub3Eff.nFrame, var15.x, var15.y, 0, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrame(var13.AG / 2 % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 11:
            if (super.f > 3 && super.f < 12) {
               super.fraImgSub2Eff.drawFrameNew(super.BE * super.fraImgSub2Eff.maxNumFrame + super.f % super.fraImgSub2Eff.maxNumFrame, super.AZ, super.BA, super.Dir, 3, var1);
               super.fraImgSub3Eff.drawFrame(CRes.random(super.fraImgSub3Eff.nFrame), super.AZ, super.BA, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, 0, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG % super.fraImgEff.nFrame, var13.x, var13.y, 0, 33, var1);
            }

            return;
         case 12:
         case 49:
         case 50:
         case 188:
         case 220:
         case 293:
            this.AW(var1);
            return;
         case 13:
         case 258:
            if (!this.checkNullObject((int)1)) {
               if (super.f >= 7 && super.f <= 12) {
                  super.fraImgEff.drawFrame((super.f - 7) / 2, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
               }

               if (super.f >= 9 && super.f <= 11) {
                  super.fraImgSubEff.drawFrame((super.f - 9) % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy + 5, super.Dir, 33, var1);
               }

               if (super.f >= 18 && super.f <= 20) {
                  super.fraImgSubEff.drawFrame((2 - (super.f - 18)) % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy + 5, super.Dir, 33, var1);
               }
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var7 = (Point_Focus)this.VecEff.elementAt(var3);
               if (super.typeEffect == 13) {
                  super.fraImgSub2Eff.drawFrame(0, var7.x, var7.y, super.Dir, 3, var1);
               } else if (super.fraImgSub2Eff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgSub2Eff.getImageFrame(), 0, 0, super.fraImgSub2Eff.frameWidth, 62, 0, var7.x, var7.y, 33);
               }
            }

            for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
               var9 = (Point)this.VecSubEff.elementAt(var3);
               super.fraImgSub3Eff.drawFrame(var9.f % super.fraImgSub3Eff.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 16:
         case 51:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + super.f % super.fraImgSubEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
               if (super.fraImgEff != null) {
                  super.fraImgEff.drawFrameNew(super.BE * super.fraImgEff.maxNumFrame + CRes.random(super.fraImgEff.maxNumFrame), super.x, super.y, super.Dir, 3, var1);
                  return;
               }
            }
            break;
         case 18:
            for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
               var9 = (Point)this.VecSubEff.elementAt(var3);
               super.fraImgSubEff.drawFrame(var9.f / 2 % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var7 = (Point_Focus)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrame(0, var7.x, var7.y, super.frame, 3, var1);
            }

            return;
         case 19:
            if (!this.checkNullObject((int)1) && super.f > 0 && super.f <= 12) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y, 0, 33, var1);
               return;
            }
            break;
         case 20:
            if (super.f >= 17 && super.f <= 24) {
               super.fraImgEff.drawFrame((super.f - 17) / 2, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 22:
         case 98:
            if (!this.checkNullObject((int)1)) {
               if (super.f < 5) {
                  super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, var1);
               }

               if (super.f >= 10 && super.f <= 14) {
                  super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, var1);
                  return;
               }
            }
            break;
         case 23:
            if (super.f < this.fPlayFrameSuper) {
               super.fraImgEff.drawFrame(3, super.x, super.y, 0, 3, var1);
               return;
            }
            break;
         case 25:
         case 235:
            this.AK(var1);
            return;
         case 26:
         case 236:
            this.AJ(var1);
            return;
         case 27:
            if (super.f % 4 < 2) {
               this.AA(var1, super.fraImgEff, super.frame, super.x, super.y);
               return;
            }

            this.AA(var1, super.fraImgSubEff, super.frame, super.x, super.y);
            return;
         case 28:
            super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, 0, 3, var1);
            return;
         case 29:
            super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + 5, super.Dir, 33, var1);
            return;
         case 30:
            if (super.f < 3) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }

            super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 3, var1);
            return;
         case 31:
         case 55:
         case 56:
         case 191:
         case 223:
         case 313:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + super.f % super.fraImgSubEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
               if (super.fraImgSub2Eff != null) {
                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG % super.fraImgEff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
               if (super.fraImgSub2Eff != null) {
                  super.fraImgSub2Eff.drawFrame(var13.AG % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
               }
            }

            return;
         case 32:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               if ((var17 = (Point)this.VecEff.elementAt(var14)).frame == 0) {
                  super.fraImgEff.drawFrame(0, var17.x, var17.y, super.Dir, 33, var1);
               } else {
                  super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x, var17.y, super.Dir, 33, var1);
               }
            }

            return;
         case 34:
            if (super.f <= 1 && super.objFireMain != null) {
               if (super.f == 0) {
                  super.fraImgSubEff.drawFrame(super.f, super.x, super.y + super.objFireMain.hOne / 2, super.Dir, 33, var1);
               } else {
                  super.fraImgSubEff.drawFrame(super.f, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
               }
            }

            if (super.f >= 7 && super.objFireMain != null) {
               var10 = 16;
               if (super.Dir == 0) {
                  var10 = -16;
               }

               super.fraImgEff.drawFrame(2, super.objFireMain.x + var10, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, 3, var1);
               return;
            }
            break;
         case 35:
            if (super.f == 0 && super.objFireMain != null) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y + super.objFireMain.hOne / 2, super.Dir, 33, var1);
            }

            if (super.f >= 5 && super.objFireMain != null) {
               var10 = 16;
               if (super.Dir == 0) {
                  var10 = -16;
               }

               super.fraImgEff.drawFrame(2, super.objFireMain.x + var10, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f / 2, var15.x, var15.y, super.Dir, 33, var1);
            }

            return;
         case 39:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var19 = (Point_Focus)this.VecEff.elementAt(var14);
               super.fraImgEff.drawFrame(2, var19.x, var19.y, super.Dir, 33, var1);
            }

            return;
         case 40:
            if (super.Dir == 0) {
               var1.setColor(-820712);
               var1.fillRect(super.x, super.y - 3, this.x1000 - super.x, 6);
               var1.setColor(-791797);
               var1.fillRect(super.x, super.y - 2, this.x1000 - super.x, 4);
               var1.setColor(-1);
               var1.fillRect(super.x, super.y - 1, this.x1000 - super.x, 2);
               return;
            }

            var1.setColor(-820712);
            var1.fillRect(this.x1000, super.y - 3, super.x, 6);
            var1.setColor(-791797);
            var1.fillRect(this.x1000, super.y - 2, super.x, 4);
            var1.setColor(-1);
            var1.fillRect(this.x1000, super.y - 1, super.x, 2);
            return;
         case 45:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var19 = (Point_Focus)this.VecEff.elementAt(var14);
               super.fraImgEff.drawFrame(1, var19.x, var19.y, var19.dis, 3, var1);
            }

            return;
         case 52:
         case 189:
         case 221:
         case 311:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + super.f % super.fraImgSubEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
               var2 = 12 + CRes.random(super.fraImgEff.maxNumFrame);
               if ((super.typeEffect == 221 || super.typeEffect == 311) && CRes.random(2) == 0) {
                  var2 -= 4;
               }

               super.fraImgEff.drawFrameNew(var2, super.x, super.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrameNew(12 - (var15.frame << 2) + var15.f, var15.x, var15.y, super.Dir, 3, var1);
            }

            return;
         case 54:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               if ((var19 = (Point_Focus)this.VecEff.elementAt(var14)).frame == 0) {
                  super.fraImgEff.drawFrame(0, var19.x, var19.y, var19.dis, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrame(var19.AG / 2 % 3, var19.x, var19.y, var19.dis, 3, var1);
               }
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if ((var17 = (Point)this.VecSubEff.elementAt(var14)).AZ != null && !var17.AZ.returnAction()) {
                  if (var17.frame == 0) {
                     super.fraImgEff.drawFrame(2, var17.AZ.x, var17.AZ.y - var17.AZ.hOne / 2, var17.dis, 3, var1);
                  } else if (var17.frame == 1) {
                     super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.AZ.x, var17.AZ.y - var17.AZ.hOne / 2 + 5, var17.dis, 33, var1);
                  }
               }
            }

            return;
         case 57:
         case 64:
         case 66:
            if (super.f < this.fPlayFrameSuper) {
               super.fraImgEff.drawFrame(0, super.x, super.y, 0, 3, var1);
               return;
            }
            break;
         case 58:
            super.fraImgEff.drawFrame(2, super.x, super.y, 0, 3, var1);
            return;
         case 59:
            if (!this.checkNullObject((int)2)) {
               if (super.f < 6) {
                  super.fraImgEff.drawFrame(super.f / 2, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, 0, 3, var1);
                  return;
               }

               if (super.f % 4 < 2) {
                  super.fraImgEff.drawFrame(3, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, 0, 3, var1);
                  return;
               }
            }
            break;
         case 60:
            if (!this.checkNullObject((int)2)) {
               if (super.f < 9) {
                  super.fraImgEff.drawFrame(4 + super.f / 3, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, 0, 3, var1);
                  return;
               }

               if (super.f % 4 < 3) {
                  super.fraImgEff.drawFrame(7, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, 0, 3, var1);
                  return;
               }
            }
            break;
         case 62:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.toX, super.toY, 0, 33, var1);
            return;
         case 63:
         case 190:
         case 222:
         case 312:
            if (super.f >= 20 && super.f < 23 || super.f >= 10 && super.f < 13) {
               super.fraImgEff.drawFrame(super.f % 10, super.x, super.y, super.Dir, 3, var1);
            }

            if (super.typeEffect == 222 || super.typeEffect == 312) {
               for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
                  var15 = (Point)this.VecSubEff.elementAt(var2);
                  super.fraImgSub3Eff.drawFrame(var15.f / 2, var15.x, var15.y, 0, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + var13.AG % super.fraImgSubEff.maxNumFrame, var13.x, var13.y, 0, 3, var1);
               super.fraImgSub2Eff.drawFrame(CRes.random(super.fraImgSub2Eff.nFrame), var13.x, var13.y, 0, 3, var1);
            }

            return;
         case 65:
         case 107:
            if (super.f < 4) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 67:
         case 68:
         case 69:
         case 194:
         case 226:
            if (super.f >= 10 && super.f <= super.fRemove) {
               int var10006 = super.f;
               this.AA(var1, super.fraImgEff, super.frame, super.x, super.y);
               return;
            }
            break;
         case 70:
            if (super.f < 4) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 33, var1);
               return;
            }

            super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 33, var1);
            return;
         case 72:
         case 92:
            super.fraImgEff.drawFrame(3, super.x, super.y, super.Dir, 3, var1);
            return;
         case 73:
         case 74:
            if (super.f < 2) {
               super.fraImgEff.drawFrame(0, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 75:
            if (super.f < 2) {
               super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 3, var1);
            }

            super.fraImgEff.drawFrame(super.frame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 77:
            var2 = super.x;
            var3 = super.y;
            if (super.f > 7) {
               super.fraImgEff.drawFrame(0, var2, var3, super.Dir, 3, var1);
               var3 += 15;
               super.fraImgSubEff.drawFrame(0, var2, var3, super.Dir, 3, var1);
               return;
            }
            break;
         case 82:
         case 144:
            if (super.f <= super.fRemove) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 84:
         case 181:
         case 213:
         case 272:
            this.AU(var1);
            return;
         case 85:
         case 182:
         case 214:
         case 273:
            this.AR(var1);
            return;
         case 86:
         case 157:
         case 183:
         case 215:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var3 = (var8 = (Point_Focus)this.VecEff.elementAt(var4)).AG * super.fraImgEff.frameHeight / 3 + super.fraImgEff.frameHeight / 3) > super.fraImgEff.frameHeight) {
                  var3 = super.fraImgEff.frameHeight;
               }

               if (super.fraImgEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), 0, super.fraImgEff.frameHeight - var3 + var8.AG % super.fraImgEff.nFrame * super.fraImgEff.frameHeight, super.fraImgEff.frameWidth, var3, 0, var8.x, var8.y, 33);
               }
            }

            return;
         case 87:
         case 184:
         case 216:
            if (super.f > 12 && super.f < 15) {
               super.fraImgEff.drawFrame(super.f - 13, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
               return;
            }

            if (super.f > 22 && super.f < 25) {
               super.fraImgEff.drawFrame(super.f - 23, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
               return;
            }

            if (super.f > 28 && super.f < 31) {
               super.fraImgEff.drawFrame(super.f - 29, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
               return;
            }

            if (super.f > 34 && super.f < 37) {
               super.fraImgEff.drawFrame(super.f - 35, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
               return;
            }
            break;
         case 93:
            if (super.f > 2 && super.f < 6) {
               super.fraImgEff.drawFrame(super.f - 3, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            }

            if (super.f > 8 && super.f < 12) {
               super.fraImgEff.drawFrame(11 - super.f, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            }

            if (super.f > 26 && super.f < 29) {
               super.fraImgEff.drawFrame(super.f - 27, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
               return;
            }
            break;
         case 94:
            if (super.f <= 3) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 33, var1);
            }

            if (super.f > 3 && super.f <= 7) {
               super.fraImgEff.drawFrame((super.f - 4) / 2, super.x, super.y, super.Dir == 0 ? 2 : 0, 33, var1);
               return;
            }
            break;
         case 95:
            if (super.f < 2) {
               super.fraImgEff.drawFrame(super.f, super.x, super.y + 3, super.Dir, 3, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var8 = (Point_Focus)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var8.frame, var8.x, var8.y, super.Dir, 3, var1);
            }

            return;
         case 96:
            this.AQ(var1);
            return;
         case 97:
            if (super.f < 4) {
               super.fraImgSub2Eff.drawFrame(super.f, super.x, super.y, super.Dir, 3, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var8 = (Point_Focus)this.VecEff.elementAt(var4)).frame == 0) {
                  super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, var8.x, var8.y, super.Dir, 3, var1);
               } else {
                  super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, var8.x, var8.y, super.Dir, 3, var1);
               }
            }

            return;
         case 99:
            if ((var4 = super.f % 4) < 4 && super.f < 8) {
               super.fraImgEff.drawFrame(var4, super.x, super.y - (var4 << 1) + 5, super.Dir, 3, var1);
               var21 = 1;
               if (super.Dir == 2) {
                  var21 = 3;
               }

               super.fraImgEff.drawFrame(var4, super.x, super.y - (var4 << 1) - 15, var21, 3, var1);
               return;
            }
            break;
         case 100:
            if (super.f >= 5 && super.f <= 11) {
               super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 101:
            if (super.f >= 6 && super.f <= 15) {
               var2 = (super.f - 2) % 4;
               super.fraImgEff.drawFrame(var2, super.objFireMain.x + this.x1000, super.objFireMain.y - super.objFireMain.hOne / 2 - (var2 << 1) + 5, super.Dir, 3, var1);
               var16 = 1;
               if (super.Dir == 2) {
                  var16 = 3;
               }

               super.fraImgEff.drawFrame(var2, super.objFireMain.x + this.x1000, super.objFireMain.y - super.objFireMain.hOne / 2 - (var2 << 1) - 15, var16, 3, var1);
               return;
            }
            break;
         case 102:
            if (super.f < 4) {
               super.fraImgSub2Eff.drawFrame(super.f, super.x, super.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG % super.fraImgEff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgSubEff.drawFrame((var15.f + var15.frame) % super.fraImgSubEff.nFrame, var15.x, var15.y, super.Dir, 3, var1);
            }

            return;
         case 103:
            if (super.f < 4) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               byte var20 = super.Dir;
               if (var15.frame == 2) {
                  var20 = 5;
               }

               super.fraImgEff.drawFrame(var15.frame, var15.x, var15.y, var20, 3, var1);
            }

            return;
         case 104:
            if (super.f < 8 && super.f % 2 == 1) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).frame == 4) {
                  super.fraImgSubEff.drawFrame(0, var15.x, var15.y, super.Dir, 33, var1);
               } else {
                  var14 = var15.dis;
                  var4 = var15.frame;
                  if (var15.frame == 2) {
                     var14 = 5;
                  } else if (var15.frame == 3) {
                     var4 = 2;
                  }

                  super.fraImgEff.drawFrame(var4, var15.x, var15.y, var14, 3, var1);
               }
            }

            return;
         case 106:
            if (super.f < 10 || super.f % 4 > 1) {
               for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
                  var15 = (Point)this.VecEff.elementAt(var2);
                  super.fraImgEff.drawFrame((super.f / 2 + var15.frame) % 3, var15.x, var15.y, super.Dir, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecSubEff.elementAt(var2);
               super.fraImgEff.drawFrame((super.f + var13.frame) % 3, var13.x, var13.y - 4, super.Dir, 3, var1);
               if (var13.AG % 2 == 0) {
                  super.fraImgSubEff.drawFrame(0, var13.x, var13.y + 4, super.Dir, 3, var1);
               }
            }

            return;
         case 108:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame((var15.frame + super.f / var15.dis) % super.fraImgEff.nFrame, var15.x, var15.y, super.Dir, 3, var1);
            }

            return;
         case 109:
            this.AP(var1);
            return;
         case 110:
            this.AO(var1);
            return;
         case 111:
            this.AN(var1);
            return;
         case 112:
         case 270:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 113:
         case 150:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 114:
         case 115:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(4, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 116:
            if (super.f >= 11 && super.f <= 16) {
               super.fraImgEff.drawFrame((super.f - 11) / 3, super.x + this.x1000, super.y, super.Dir, 3, var1);
               return;
            }

            if (super.f >= 26 && super.f <= 31) {
               super.fraImgEff.drawFrame((super.f - 26) / 3, super.x + this.x1000, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 117:
            this.AM(var1);
            return;
         case 118:
            if (super.vecObjsBeFire.size() <= 1) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 3, var1);
               return;
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var15.f / 2, var15.x, var15.y, var15.dis, 3, var1);
            }

            return;
         case 119:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(0, var13.x, var13.y, var13.dis, 3, var1);
               super.fraImgSubEff.drawFrame(0, var13.x, var13.y + 30, var13.dis, 3, var1);
               if (super.f % 2 == 0) {
                  if (var13.dis == 0) {
                     super.fraImgSub2Eff.drawFrame(CRes.random(2), var13.x - 25, var13.y, 0, 3, var1);
                  } else if (var13.dis == 2) {
                     super.fraImgSub2Eff.drawFrame(CRes.random(2), var13.x + 25, var13.y, 2, 3, var1);
                  }
               }
            }

            return;
         case 120:
            if (super.f <= 9) {
               super.fraImgSubEff.drawFrame(0, super.x + this.CL[2][0], super.y + this.CL[2][1], super.Dir, 3, var1);
            } else if (super.f >= 10 && super.f <= 11) {
               super.fraImgEff.drawFrame(0, super.x + this.CL[0][0], super.y + this.CL[0][1], super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(1, super.x + this.CL[3][0], super.y + this.CL[3][1], super.Dir, 3, var1);
            } else if (super.f >= 12 && super.f <= 13) {
               super.fraImgEff.drawFrame(1, super.x + this.CL[1][0], super.y + this.CL[1][1], super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(2, super.x + this.CL[4][0], super.y + this.CL[4][1], super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrame(var15.frame, var15.x, var15.y, super.Dir, 33, var1);
            }

            return;
         case 121:
            if (super.f >= 13) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 122:
            if (super.f >= 16) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.f / 2 % 2, this.x1000, this.y1000, super.Dir, 0, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrame(var13.AG % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 33, var1);
            }

            return;
         case 123:
         case 185:
         case 217:
         case 283:
            if (super.f >= 9 && super.f <= 11 || super.f >= 24 && super.f <= 26) {
               super.BP.drawFrame(0, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
            }

            if (super.f <= 11 || super.f >= 26) {
               if (super.typeEffect != 185 && super.typeEffect != 217 && super.typeEffect != 283) {
                  super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               } else {
                  var2 = super.f / 2 % super.fraImgEff.nFrame;
                  if (super.typeEffect == 217 || super.typeEffect == 283) {
                     var2 += 2;
                  }

                  super.fraImgEff.drawFrameNew(var2, super.x + super.am_duong * 5, super.y, super.Dir, 3, var1);
               }

               super.fraImgSubEff.drawFrame(super.f / 2 % 2, this.x1000, this.y1000, super.Dir, 0, var1);
            }

            if (super.typeEffect != 283) {
               for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
                  if ((var15 = (Point)this.VecEff.elementAt(var2)).f >= 3 && (var15.f - 3) / 2 < 3) {
                     super.fraImgSub2Eff.drawFrame((var15.f - 3) / 2, var15.x, var15.y, super.Dir, 3, var1);
                  }

                  if (var15.f / 2 < 3) {
                     super.fraImgSub3Eff.drawFrame(var15.f / 2, var15.x, var15.y, super.Dir, 3, var1);
                  }
               }

               return;
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrame(var13.AG % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 33, var1);
            }

            return;
         case 124:
         case 186:
         case 218:
            if (super.f >= 0 && super.f <= 5) {
               super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, 33, var1);
               return;
            }
            break;
         case 125:
         case 162:
         case 187:
            if (super.objFireMain.isTanHinh) {
               super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, 33, var1);
               return;
            }
            break;
         case 126:
         case 159:
         case 192:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               super.fraImgEff.drawFrame(2, super.x, super.y, 0, 3, var1);
            }

            if (super.objFireMain.isTanHinh) {
               super.fraImgSubEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, 33, var1);
               return;
            }
            break;
         case 127:
         case 193:
         case 225:
         case 302:
            if (super.typeEffect == 302 && super.f > 2 && super.f < 15) {
               var8 = (Point_Focus)this.VecEff.elementAt(0);
               super.fraImgSub3Eff.drawFrame(super.f / 3 < super.fraImgSub3Eff.nFrame ? super.f / 3 : super.fraImgSub3Eff.nFrame - 1, var8.x, var8.y, super.Dir ^ 2, 3, var1);
            }

            if (super.f >= 7 && super.f <= 15) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x + super.am_duong * 40, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, 3, var1);
            }

            if (super.f >= 15) {
               super.fraImgSub2Eff.drawFrame(0, super.x, super.y + 50, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.mframe[super.f / 2 % super.mframe.length], super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 133:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame((var13.frame + var13.AG / 2) % super.fraImgEff.nFrame, var13.x + CRes.random_Am_0(3), var13.y + CRes.random_Am_0(3), var13.dis, 3, var1);
            }

            if (super.f >= 2 && super.f <= 4) {
               super.fraImgSubEff.drawFrame(super.f - 7, super.x + super.am_duong * 17, super.y, super.Dir == 2 ? 0 : 2, 3, var1);
            }

            if (super.f >= 10 && super.f <= 12) {
               super.fraImgSubEff.drawFrame(super.f - 15, super.x + super.am_duong * 17, super.y, super.Dir == 2 ? 0 : 2, 3, var1);
               return;
            }
            break;
         case 134:
         case 135:
            if (super.fraImgSub3Eff != null) {
               for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
                  var15 = (Point)this.VecSubEff.elementAt(var2);
                  super.fraImgSub3Eff.drawFrame(1 + var15.f / 2, var15.x, var15.y, 0, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var13 = (Point_Focus)this.VecEff.elementAt(var2)).AM == 1) {
                  super.fraImgSub2Eff.drawFrameNew(var13.frame % super.fraImgSub2Eff.nFrame, var13.x + CRes.random_Am_0(5), var13.y + CRes.random_Am_0(5), var13.dis, 3, var1);
               } else {
                  super.fraImgEff.drawFrame((var13.frame + var13.AG / 2) % super.fraImgEff.nFrame, var13.x + CRes.random_Am_0(5), var13.y + CRes.random_Am_0(5), var13.dis, 3, var1);
               }
            }

            if (super.f >= 2 && super.f <= 4) {
               super.fraImgSubEff.drawFrame(super.f - 7, super.x + super.am_duong * 17, super.y, super.Dir == 2 ? 0 : 2, 3, var1);
            }

            if (super.f >= 5 && super.f <= 7) {
               super.fraImgSubEff.drawFrame(super.f - 15, super.x + super.am_duong * 17, super.y, super.Dir == 2 ? 0 : 2, 3, var1);
            }

            if (!this.checkNullObject((int)1) && super.f >= 10 && super.f <= 13) {
               super.fraImgSubEff.drawFrame(super.f - 15, super.x + super.am_duong * 17, super.y - super.objFireMain.dy, super.Dir == 2 ? 0 : 2, 3, var1);
               return;
            }
            break;
         case 136:
            if (super.f == 4 || super.f == 10 || super.f == 14) {
               super.fraImgSubEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
            }

            if (super.f != 1 && super.f != 3 && super.f != 11 && super.f != 13) {
               break;
            }

            super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, 0, 33, var1);
            return;
         case 137:
         case 138:
            if (super.f == 2 || super.f == 13 || super.f == 18) {
               super.fraImgSubEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
            }

            if (super.f != 3 && super.f != 12 && super.f != 19) {
               break;
            }

            super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, 0, 33, var1);
            return;
         case 139:
            if (super.f > 2 && super.f < 16) {
               super.fraImgSub2Eff.drawFrameNew(super.BE * super.fraImgSub2Eff.maxNumFrame + super.f % super.fraImgSub2Eff.maxNumFrame, super.AZ, super.BA, super.Dir, 3, var1);
               super.fraImgSub3Eff.drawFrame(CRes.random(super.fraImgSub3Eff.nFrame), super.AZ, super.BA, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, 0, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG % super.fraImgEff.nFrame, var13.x, var13.y, 0, 33, var1);
               super.fraImgSub3Eff.drawFrame(CRes.random(super.fraImgSub3Eff.nFrame), var13.x + CRes.random_Am_0(10), var13.y - CRes.random(10), 0, 33, var1);
            }

            return;
         case 140:
            if (super.f > 2 && super.f < 18) {
               super.fraImgSub2Eff.drawFrameNew(super.BE * super.fraImgSub2Eff.maxNumFrame + super.f % super.fraImgSub2Eff.maxNumFrame, super.AZ, super.BA, super.Dir, 3, var1);
               super.fraImgSub3Eff.drawFrame(CRes.random(super.fraImgSub3Eff.nFrame), super.AZ, super.BA, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               this.CO[2].drawFrame(var13.AG % this.CO[2].nFrame, var13.x, var13.y, 0, 3, var1);
            }

            if (super.f >= 32 && super.f <= 36 && !this.checkNullObject((int)2) && CRes.random(4) != 0) {
               var2 = CRes.random(1, 5);

               for(var3 = 0; var3 < var2; ++var3) {
                  var4 = CRes.random_Am(0, 25) + this.objBeFireMain.x;
                  this.CO[1].drawFrame(CRes.random(this.CO[1].nFrame), var4, this.objBeFireMain.y - 70, 0, 0, var1);
               }
            }

            if (super.f < 20 || super.f > 38) {
               break;
            }

            if (super.f >= 24 && super.f < 36) {
               if (super.f < 28) {
                  this.CO[0].drawFrame(1, this.objBeFireMain.x, this.objBeFireMain.y - 60, 0, 33, var1);
                  return;
               }

               if (super.f < 36) {
                  this.CO[0].drawFrame(2, this.objBeFireMain.x, this.objBeFireMain.y - 60, 0, 33, var1);
                  return;
               }
               break;
            }

            this.CO[0].drawFrame(0, this.objBeFireMain.x, this.objBeFireMain.y - 60, 0, 33, var1);
            return;
         case 142:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame((var13.frame + super.f) % super.fraImgEff.nFrame, var13.x / 10, var13.y / 10, var13.dis, 3, var1);
            }

            return;
         case 147:
            super.fraImgEff.drawFrame(5, super.x, super.y, super.Dir, 3, var1);
            return;
         case 149:
            if ((var2 = super.f) > 2) {
               var2 = 2;
            }

            super.fraImgEff.drawFrame(var2, super.x, super.y, super.Dir, 3, var1);
            return;
         case 151:
         case 152:
         case 153:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(super.frame * 3 + super.f / 2 % 2, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 154:
            super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 155:
            if (super.f < 2 || super.f > 5) {
               super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.x + super.AZ, super.y, super.Dir, 3, var1);
            }

            if (super.f < 6 && super.f > 1) {
               super.fraImgSubEff.drawFrame((super.f + (super.f > 3 ? 1 : 0)) % 2, super.x + super.AZ, super.y, 0, 3, var1);
               return;
            }
            break;
         case 158:
         case 177:
            if (super.f >= 20 && super.f <= 25) {
               super.fraImgEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, 33, var1);
               return;
            }
            break;
         case 160:
            this.AT(var1);
            return;
         case 161:
            this.AL(var1);
            return;
         case 164:
         case 227:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).dis == 0) {
                  super.fraImgEff.drawFrame(var15.frame, var15.x, var15.y, super.Dir, 3, var1);
               } else {
                  super.fraImgSubEff.drawFrame(var15.frame, var15.x, var15.y, super.Dir, 3, var1);
               }
            }

            return;
         case 165:
         case 166:
            if ((var3 = super.f / 2 % 6) < 2) {
               super.fraImgEff.drawFrame(var3, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 167:
            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               var17 = (Point)this.VecSubEff.elementAt(var14);
               super.fraImgSub2Eff.drawFrame(var17.frame, var17.x, var17.y, 0, 33, var1);
            }

            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);
               super.fraImgSub3Eff.drawFrameNew(var17.f / 3, var17.x, var17.y, 0, 33, var1);
            }

            if (super.f >= 4 && super.f <= 5) {
               var1.drawRegion(super.BP.getImageFrame(), 0, 0, super.BP.frameWidth, super.BP.frameHeight / (super.f - 3), 0, this.x1000, this.y1000, 33);
            }

            if (super.f > 4) {
               if (super.f > super.fRemove - 4) {
                  super.fraImgSub2Eff.drawFrame(super.fRemove - super.f, this.x1000, this.y1000, 0, 33, var1);
               } else {
                  super.fraImgSub2Eff.drawFrame(3, this.x1000, this.y1000, 0, 33, var1);
               }
            }

            if (super.f < 8) {
               super.fraImgSubEff.drawFrame(super.f / 2, this.x1000, this.y1000, 0, 33, var1);
               return;
            }
            break;
         case 168:
            if (super.f < 12) {
               var14 = this.CG[super.f / 2][1];
               var4 = this.CG[super.f / 2][2];
               var21 = 0;
               if (!this.checkNullObject((int)1) && super.objFireMain.Dir == 2) {
                  var21 = 2;
                  var14 = -this.CG[super.f / 2][1];
               }

               super.fraImgEff.drawFrameNew(super.f / 2 % super.fraImgEff.nFrame, super.x + var14, super.y + var4, var21, 3, var1);
               return;
            }
            break;
         case 169:
         case 237:
            if (super.f <= 20 && !this.checkNullObject((int)1)) {
               super.BP.drawFrame(CRes.random(super.BP.nFrame), super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2 + 3, 0, 3, var1);
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if (var14 % 2 == 1) {
                  var17 = (Point)this.VecSubEff.elementAt(var14);
                  super.fraImgSubEff.drawFrameNew(var17.frame * super.fraImgSubEff.maxNumFrame + GameCanvas.gameTick / 2 % super.fraImgSubEff.maxNumFrame, var17.x / 10, var17.y / 10, 0, 3, var1);

                  for(var2 = 0; var2 < 4; ++var2) {
                     super.fraImgEff.drawFrameNew(var17.frame * super.fraImgEff.maxNumFrame, var17.x / 10, var17.y / 10 - var2 * 73, CRes.random(2) == 0 ? 0 : 2, 33, var1);
                  }
               }
            }

            if (super.f > 16) {
               var4 = GameCanvas.gameTick % 2 << 1;
               if (this.y1000 == 0) {
                  super.fraImgSub3Eff.drawFrameNew(GameCanvas.gameTick / 3 % super.fraImgSub3Eff.nFrame, super.x, super.y + 7, 0, 3, var1);
               }

               var1.setColor(-16369695);
               var1.fillRect(super.x - 20 - -4, super.y - 350 - this.y1000, 32, 360);
               var1.setColor(-16745233);
               var1.fillRect(super.x - 18 - -4, super.y - 350 - this.y1000, 28, 360);
               var1.setColor(-5116164);
               var1.fillRect(super.x - 16 - -4, super.y - 350 - this.y1000, 24, 360);
               var1.setColor(-16745233);
               var1.fillRect(super.x - 14 - -4 + var4, super.y - 350 - this.y1000, 20 - (var4 << 1), 360);
               var1.setColor(-5116164);
               var1.fillRect(super.x - 12 - -4 + var4, super.y - 350 - this.y1000, 16 - (var4 << 1), 360);
               var1.setColor(-262402);
               var1.fillRect(super.x - 10 - -4 + var4, super.y - 350 - this.y1000, 12 - (var4 << 1), 360);
            }

            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);
               super.fraImgSub2Eff.drawFrame(var17.f / 2, var17.x, var17.y, 0, 3, var1);
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if (var14 % 2 == 0) {
                  var17 = (Point)this.VecSubEff.elementAt(var14);
                  super.fraImgSubEff.drawFrameNew(var17.frame * super.fraImgSubEff.maxNumFrame + GameCanvas.gameTick / 2 % super.fraImgSubEff.maxNumFrame, var17.x / 10, var17.y / 10, 0, 3, var1);

                  for(var2 = 0; var2 < 4; ++var2) {
                     super.fraImgEff.drawFrameNew(var17.frame * super.fraImgEff.maxNumFrame, var17.x / 10, var17.y / 10 - var2 * 73, CRes.random(2) == 0 ? 0 : 2, 33, var1);
                  }
               }
            }

            return;
         case 170:
         case 238:
            if (super.f <= 20 && !this.checkNullObject((int)1)) {
               super.BP.drawFrame(CRes.random(super.BP.nFrame), super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2 + 3, 0, 3, var1);
            }

            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var19 = (Point_Focus)this.VecEff.elementAt(var14);
               var21 = 0;
               if (super.Dir == 2) {
                  var21 = 2;
               }

               if (var19.AG >= var19.fRe) {
                  if (super.fraImgEff.getImageFrame() != null && var19.AG % 5 != 2 && var19.AG < var19.fRe + 8) {
                     var1.drawRegion(super.fraImgEff.getImageFrame(), var19.AQ * super.fraImgEff.frameWidth, var19.frame * super.fraImgEff.frameHeight, super.fraImgEff.frameWidth, var19.AM, var21, var19.x, var19.y, 33);
                  }
               } else if (var19.AG % 5 != 2) {
                  super.fraImgEff.drawFrameNew(var19.AQ * super.fraImgEff.maxNumFrame + var19.frame, var19.x, var19.y, var21, 3, var1);
               }
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               var17 = (Point)this.VecSubEff.elementAt(var14);
               super.fraImgSub3Eff.drawFrameNew(var17.frame * super.fraImgSub3Eff.maxNumFrame + GameCanvas.gameTick / 2 % super.fraImgSub3Eff.maxNumFrame, var17.x / 10, var17.y / 10, 0, 3, var1);

               for(var2 = 0; var2 < 4; ++var2) {
                  super.fraImgSub2Eff.drawFrameNew(var17.frame * super.fraImgSub2Eff.maxNumFrame, var17.x / 10, var17.y / 10 - var2 * 73, CRes.random(2) == 0 ? 0 : 2, 33, var1);
               }
            }

            return;
         case 171:
         case 239:
            if (super.f < 20 && !this.checkNullObject((int)1) && (super.f <= 8 || super.f >= 13)) {
               super.fraImgSub3Eff.drawFrame(super.f / 2 % super.fraImgSub3Eff.nFrame, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, super.Dir, 33, var1);
            }

            for(var14 = this.VecEff.size() - 1; var14 >= 0; --var14) {
               if ((var17 = (Point)this.VecEff.elementAt(var14)).frame == 0 && var17.fSmall >= 2) {
                  super.fraImgEff.drawFrame(var17.f / 2 % super.fraImgEff.nFrame, var17.x / 1000, var17.y / 1000, 0, 33, var1);
               } else if (var17.frame == 1 && var17.fSmall == 3) {
                  super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x / 1000, var17.y / 1000, 0, 33, var1);
               }
            }

            if (super.f > 6 && super.f < super.fRemove) {
               var4 = GameCanvas.gameTick % 2 << 1;
               super.fraImgSub2Eff.drawFrame(GameCanvas.gameTick / 3 % super.fraImgSub2Eff.nFrame, super.x, super.y - 3, 0, 3, var1);
               var1.setColor(-54784);
               var1.fillRect(super.x - 20 - -4, super.y - this.y1000, 32, this.y1000);
               var1.setColor(-31744);
               var1.fillRect(super.x - 18 - -4, super.y - this.y1000, 28, this.y1000);
               var1.setColor(-12032);
               var1.fillRect(super.x - 16 - -4, super.y - this.y1000, 24, this.y1000);
               var1.setColor(-31744);
               var1.fillRect(super.x - 14 - -4 + var4, super.y - this.y1000, 20 - (var4 << 1), this.y1000);
               var1.setColor(-12032);
               var1.fillRect(super.x - 12 - -4 + var4, super.y - this.y1000, 16 - (var4 << 1), this.y1000);
               var1.setColor(-131);
               var1.fillRect(super.x - 10 - -4 + var4, super.y - this.y1000, 12 - (var4 << 1), this.y1000);
            }

            for(var14 = this.VecEff.size() - 1; var14 >= 0; --var14) {
               if ((var17 = (Point)this.VecEff.elementAt(var14)).frame == 0 && var17.fSmall < 2) {
                  super.fraImgEff.drawFrame(var17.f / 2 % super.fraImgEff.nFrame, var17.x / 1000, var17.y / 1000, 0, 33, var1);
               } else if (var17.frame == 1 && var17.fSmall != 3) {
                  super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x / 1000, var17.y / 1000, 0, 33, var1);
               }
            }

            return;
         case 172:
         case 240:
            if (super.f >= 20 || this.checkNullObject((int)1) || super.f > 8 && super.f < 13) {
               break;
            }

            super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, super.Dir, 33, var1);
            return;
         case 173:
            this.AI(var1);
            return;
         case 174:
            if (super.f >= 4 && super.f <= super.fRemove && !this.checkNullObject((int)1)) {
               byte var18 = 1;
               if (super.f < 6) {
                  var18 = 0;
               }

               super.fraImgEff.drawFrame(var18, super.objFireMain.x + super.am_duong * 36, super.objFireMain.y - 25, super.objFireMain.type_left_right, 3, var1);
               return;
            }
            break;
         case 175:
            this.AH(var1);
            return;
         case 178:
            this.AG(var1);
            return;
         case 179:
         case 241:
            if (super.f > 0 && super.f <= 2 || super.f >= 24 && super.f <= 25) {
               super.fraImgEff.drawFrame(0, super.x + super.am_duong * 5, super.y, super.Dir, 3, var1);
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if ((var17 = (Point)this.VecSubEff.elementAt(var14)).frame == 0) {
                  if (super.frame == 1) {
                     super.BP.drawFrameNew(var17.f / 2 % super.BP.nFrame, var17.x, var17.y, var17.dis, 3, var1);
                  } else {
                     super.fraImgSubEff.drawFrameNew((var17.f + var17.frame) % super.fraImgSubEff.nFrame, var17.x, var17.y, var17.dis, 3, var1);
                  }
               }
            }

            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);
               if (super.frame == 1) {
                  super.fraImgSubEff.drawFrame(var17.f % super.fraImgSubEff.nFrame, var17.x, var17.y, var17.dis, 3, var1);
                  super.fraImgSub3Eff.drawFrame(0, var17.x, var17.y + 60, var17.dis, 3, var1);
                  if (var17.f % 2 == 0) {
                     super.fraImgSub2Eff.drawFrameNew(super.step * super.fraImgSub2Eff.maxNumFrame + var17.f / 3 % super.fraImgSub2Eff.maxNumFrame, var17.x + super.am_duong * 10, var17.y + 5, var17.dis, 3, var1);
                  }
               } else {
                  super.fraImgSubEff.drawFrameNew(var17.f % super.fraImgSubEff.nFrame, var17.x, var17.y, var17.dis, 3, var1);
               }
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if ((var17 = (Point)this.VecSubEff.elementAt(var14)).frame == 1 && super.frame == 1) {
                  super.BP.drawFrameNew(var17.f / 2 % super.BP.nFrame, var17.x, var17.y, var17.dis, 3, var1);
               }
            }

            return;
         case 195:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);

               for(var2 = 0; var2 < 4; ++var2) {
                  super.fraImgEff.drawFrame(0, var17.x / 10, var17.y / 10 - 73 - var2 * 73, CRes.random(2) << 1, 0, var1);
               }

               super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x / 10 + 15, var17.y / 10 + 4, CRes.random(2) << 1, 33, var1);
            }

            return;
         case 196:
            for(var14 = 0; var14 < this.VecEff.size(); ++var14) {
               var17 = (Point)this.VecEff.elementAt(var14);
               super.fraImgEff.drawFrame(var17.f % super.fraImgEff.nFrame, var17.x, var17.y, super.Dir, 3, var1);
            }

            for(var14 = 0; var14 < this.VecSubEff.size(); ++var14) {
               if ((var17 = (Point)this.VecSubEff.elementAt(var14)).frame == 0) {
                  super.fraImgSub2Eff.drawFrame(var17.f % super.fraImgSub2Eff.nFrame, var17.x, var17.y, super.Dir, 3, var1);
               } else {
                  super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x - 50, var17.y - 50, 0, 0, var1);
                  super.fraImgSubEff.drawFrame(var17.f / 2 % super.fraImgSubEff.nFrame, var17.x, var17.y - 50, 2, 0, var1);
               }
            }

            return;
         case 197:
            var14 = 30 + super.f / 2 * 15;
            var4 = 0;
            if (var14 > 76) {
               var14 = 76;
            }

            if (super.Dir == 0) {
               var4 = var14;
            }

            if (super.fraImgEff.getImageFrame() != null) {
               var1.drawRegion(super.fraImgEff.getImageFrame(), 0, 0, var14, 27, super.Dir, super.x - var4, super.y - 13, 0);
               return;
            }
            break;
         case 198:
            if (super.f < 8) {
               super.fraImgEff.drawFrame(super.f / 4, super.x, super.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.frame, var13.x, var13.y, 0, 3, var1);
            }

            return;
         case 199:
            if (!this.checkNullObject((int)1) && super.objFireMain.isTanHinh) {
               super.fraImgEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 200:
            if (super.f < 20) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x + 4 * super.am_duong, super.y - super.f % 4 / 2 * 3 + 2, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame((var13.frame + var13.AG) % super.fraImgEff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
            }

            return;
         case 202:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG / 2 % 2, var13.x, var13.y, 0, 3, var1);
            }

            if (!this.checkNullObject((int)1)) {
               if (super.f < 10) {
                  super.fraImgEff.drawFrame(super.f / 2, super.x, super.objFireMain.y - super.objFireMain.hOne - 15, 0, 3, var1);
                  return;
               }

               if (super.f < 12) {
                  super.fraImgEff.drawFrame(2, super.x + super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2 - 20, 0, 3, var1);
                  return;
               }
            }
            break;
         case 203:
            if (!this.checkNullObject((int)1) && super.objFireMain.isTanHinh) {
               super.fraImgEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 204:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f % super.fraImgSubEff.nFrame, var15.x, var15.y, var15.dis, 3, var1);
               super.fraImgSub2Eff.drawFrame(0, var15.x, var15.y + 60, var15.dis, 3, var1);
            }

            return;
         case 205:
            if (super.f > 10 && super.f <= super.fRemove) {
               super.fraImgEff.drawFrame(0, super.x + this.x1000 / 1000, super.y + this.y1000 / 1000, 0, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(1 + var15.f / 3, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 206:
            if (super.f < this.fPlayFrameSuper) {
               this.AA(var1, super.fraImgEff, super.frame, super.x, super.y);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f / 2 % super.fraImgSubEff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 207:
            if (super.f < this.fPlayFrameSuper) {
               super.fraImgEff.drawFrame(3, super.x, super.y, 0, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f / 2 % super.fraImgSubEff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 208:
            this.AF(var1);
            return;
         case 209:
         case 242:
            if (super.typeEffect == 242 && super.objFireMain != null && super.f < 11) {
               super.objFireMain.AA(var1, super.objFireMain.x + super.am_duong * 120, super.objFireMain.y, super.objFireMain.frame, super.objFireMain.type_left_right == 0 ? 2 : 0, true);
            }

            if (super.frame == 1) {
               for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
                  var15 = (Point)this.VecEff.elementAt(var2);
                  if (super.fraImgSubEff != null && super.fraImgSubEff.imgFrame != null) {
                     super.fraImgSubEff.drawFrameNew(CRes.random(super.fraImgSubEff.maxNumFrame), var15.x, var15.y, 0, 3, var1);
                  }

                  super.fraImgEff.drawFrameNew(6 + var15.frame, var15.x, var15.y, 0, 3, var1);
               }
            } else {
               for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
                  var15 = (Point)this.VecEff.elementAt(var2);
                  super.fraImgEff.drawFrameNew(6 + var15.frame, var15.x, var15.y, 0, 3, var1);
               }
            }

            if (super.f < super.fRemove) {
               super.fraImgEff.drawFrameNew(6 + super.mframe[super.f], super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 210:
         case 243:
            this.AE(var1);
            return;
         case 211:
         case 244:
            this.AD(var1);
            return;
         case 212:
         case 271:
         case 274:
         case 275:
            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgEff.drawFrame(var15.f % super.fraImgEff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 219:
         case 292:
            if (super.f == 4) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 3, var1);
            }

            if (super.f == 24) {
               super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 33, var1);
            }

            if (super.mframe[super.f] >= 0) {
               super.fraImgEff.drawFrame(super.mframe[super.f], super.x, super.y + 5, super.Dir, 33, var1);
               return;
            }
            break;
         case 224:
            if (super.f == 1 || super.f == 15) {
               super.fraImgEff.drawFrame(0, super.x, super.y, 0, 33, var1);
            }

            if (super.objFireMain.isTanHinh) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x + this.CM[(super.f - 2) / 2][0], super.y + this.CM[(super.f - 2) / 2][1], super.Dir, 33, var1);
               return;
            }
            break;
         case 228:
         case 259:
         case 260:
         case 261:
            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrameNew_BeginSuper(var15.f, var15.x, var15.y, 0, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrameNew_BeginSuper(var15.frame, var15.x, var15.y, 0, 33, var1);
            }

            if (!this.checkNullObject((int)1) && super.f >= 4 && super.f <= 12) {
               super.fraImgSubEff.drawFrameNew_BeginSuper(super.f % super.fraImgSubEff.maxNumFrame, super.objFireMain.x - super.am_duong * 20, super.objFireMain.y - super.objFireMain.dy - 15, super.objFireMain.type_left_right, 3, var1);
               return;
            }
            break;
         case 233:
            return;
         case 245:
         case 251:
            if (!this.checkNullObject((int)1) && super.f >= 8 && super.f <= 19 && super.f - 8 < this.CM.length) {
               super.fraImgEff.drawFrameNew(this.CM[super.f - 8][0], super.objFireMain.x + super.am_duong * (this.CM[super.f - 8][1] + 20), super.objFireMain.y - super.objFireMain.hOne / 2 - this.CM[super.f - 8][2] - super.objFireMain.dy, super.objFireMain.type_left_right, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrame(var15.f / 2 % super.fraImgSubEff.nFrame, var15.x, var15.y, var15.dis, 3, var1);
            }

            return;
         case 246:
         case 253:
            if (super.f >= 10 && super.f <= super.fRemove - 4 && super.f % 3 != 2) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x, super.y + 3, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).frame == 0) {
                  super.fraImgEff.drawFrame(0, var15.x, var15.y, 0, 3, var1);
               } else if (var15.frame == 1) {
                  if (super.fraImgEff.getImageFrame() != null) {
                     var1.drawRegion(super.fraImgEff.getImageFrame(), 0, 0, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight - var15.dis, 0, var15.x, var15.y, 33);
                  }
               } else if ((var15.frame == 2 || var15.frame == 3) && super.fraImgEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), 0, (var15.frame - 1) * super.fraImgEff.frameHeight, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight - var15.dis, 0, var15.x, var15.y, 33);
               }
            }

            return;
         case 247:
         case 254:
            if (super.f >= 5 && super.f <= 7) {
               if (super.fraImgEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), 0, 0, super.fraImgEff.frameWidth / 4 * (super.f - 4), super.fraImgEff.frameHeight, super.Dir, super.x, super.y, 3);
               }

               if (!this.checkNullObject((int)1)) {
                  super.fraImgSub2Eff.drawFrame(0, super.x - super.am_duong * 10, super.objFireMain.y - super.objFireMain.hOne + 10, super.Dir, 3, var1);
               }
            }

            if (super.f == 7 || super.f == 8 || super.f == 14 || super.f == 15) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x - super.am_duong * 14, super.y, super.Dir, 3, var1);
               if (!this.checkNullObject((int)1)) {
                  super.fraImgSub2Eff.drawFrame(0, super.x - super.am_duong * 10, super.objFireMain.y - super.objFireMain.hOne + 10, super.Dir, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               if (super.typeEffect == 254 && CRes.random(2) == 0) {
                  super.fraImgSub3Eff.drawFrame(CRes.random(5), var13.x / 10 + CRes.random_Am_0(5) + super.am_duong * 5, var13.y / 10 - 8, var13.AB, 3, var1);
               }

               super.fraImgEff.drawFrame(var13.AG / 2 % super.fraImgEff.nFrame, var13.x / 10, var13.y / 10, var13.AB, 3, var1);
               if (super.typeEffect == 254 && CRes.random(2) == 0) {
                  super.fraImgSub3Eff.drawFrame(CRes.random(5), var13.x / 10 + CRes.random_Am_0(5) + super.am_duong * 20, var13.y / 10 - 8, var13.AB, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var15 = (Point)this.VecSubEff.elementAt(var2)).fRe == 5) {
                  super.fraImgSub3Eff.drawFrame(var15.f % super.fraImgSub3Eff.nFrame, var15.x, var15.y, 0, 3, var1);
               } else {
                  super.fraImgSubEff.drawFrame(var15.f % super.fraImgSubEff.nFrame, var15.x, var15.y, 0, 3, var1);
               }
            }

            return;
         case 248:
         case 255:
            if (!this.checkNullObject((int)1)) {
               if (super.f == 5 || super.f == 6 || super.f == 10 || super.f == 11 || super.f == 14) {
                  super.fraImgSubEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, var1);
               }

               if (super.f >= 15 && super.f <= 19) {
                  super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, var1);
                  return;
               }
            }
            break;
         case 249:
         case 252:
            if (!this.checkNullObject((int)1) && (super.f == 6 || super.f == 8 || super.f == 19 || super.f == 21)) {
               super.fraImgSub2Eff.drawFrame(0, super.objFireMain.x, super.objFireMain.y, 0, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var4 = ((var15 = (Point)this.VecEff.elementAt(var2)).f + var15.frame) % this.CM.length;
               super.fraImgSubEff.drawFrameNew(var15.f % super.fraImgSubEff.nFrame, var15.x, var15.y - var15.AF, var15.dis, 3, var1);
               super.fraImgEff.drawFrameNew(this.CM[var4][0], var15.x + var15.fSmall * (this.CM[var4][1] + 20), var15.y - this.CM[var4][2] - var15.AF, var15.dis, 3, var1);
            }
         default:
            return;
         case 250:
            this.AC(var1);
            return;
         case 266:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if (super.f > 3 + (var4 << 2)) {
                  var8 = (Point_Focus)this.VecEff.elementAt(var4);
                  var16 = 0;
                  if (super.Dir == 2) {
                     var16 = 2;
                  }

                  super.fraImgEff.drawFrame(0, var8.x, var8.y, var16, 3, var1);
               }
            }

            return;
         case 267:
            if (super.f > 2) {
               for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
                  var8 = (Point_Focus)this.VecEff.elementAt(var4);
                  var16 = 0;
                  if (super.Dir == 2) {
                     var16 = 2;
                  }

                  super.fraImgEff.drawFrame(0, var8.x, var8.y - 5, var16, 3, var1);
               }

               return;
            }
            break;
         case 268:
            if ((super.f < 2 || super.f > 11) && (super.f < 16 || super.f > 25)) {
               break;
            }

            super.fraImgEff.drawFrame(GameCanvas.BJ % 2, this.x1000, super.y + 3, 0, 3, var1);
            return;
         case 269:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var12 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(GameCanvas.BJ % 2, var12.x, var12.y, super.Dir, 3, var1);
            }

            return;
         case 276:
         case 277:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var8 = (Point_Focus)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame((var8.frame + var8.AG) % super.fraImgEff.nFrame, var8.x, var8.y, super.Dir, 3, var1);
            }

            return;
         case 278:
         case 279:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var12 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrameNew(var12.frame + var12.dis * 5, this.objBeFireMain.x + var12.x, super.y + var12.y, 0, 3, var1);
            }

            for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
               var12 = (Point)this.VecSubEff.elementAt(var4);
               super.fraImgSubEff.drawFrameNew(var12.frame + (var12.dis << 2), this.objBeFireMain.x + var12.x, super.y + var12.y, 0, 3, var1);
            }

            return;
         case 280:
            super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, 0, 3, var1);
            super.fraImgEff.drawFrame(0, super.toX, super.toY, 0, 3, var1);
            return;
         case 281:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var3 = (var8 = (Point_Focus)this.VecEff.elementAt(var4)).AG * super.fraImgEff.frameHeight / 3 + super.fraImgEff.frameHeight / 3) > super.fraImgEff.frameHeight) {
                  var3 = super.fraImgEff.frameHeight;
               }

               if (super.fraImgEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), 0, super.fraImgEff.frameHeight - var3 + var8.AG % super.fraImgEff.nFrame * super.fraImgEff.frameHeight, super.fraImgEff.frameWidth, var3, 0, var8.x, var8.y, 33);
               }
            }

            for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
               if (super.f > 8 + (var4 << 2)) {
                  var8 = (Point_Focus)this.VecSubEff.elementAt(var4);
                  var16 = 0;
                  if (super.Dir == 2) {
                     var16 = 2;
                  }

                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSub2Eff.nFrame, var8.x, var8.y, var16, 3, var1);
               }
            }

            return;
         case 282:
            if (super.f > 12 && super.f < 15) {
               super.fraImgEff.drawFrame(super.f - 13, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            } else if (super.f > 22 && super.f < 25) {
               super.fraImgEff.drawFrame(super.f - 23, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            } else if (super.f > 28 && super.f < 31) {
               super.fraImgEff.drawFrame(super.f - 29, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            } else if (super.f > 34 && super.f < 37) {
               super.fraImgEff.drawFrame(super.f - 35, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var3 = (var12 = (Point)this.VecEff.elementAt(var4)).dis;
               var14 = var12.frame;
               if (var12.frame == 2) {
                  var3 = 5;
               } else if (var12.frame == 3) {
                  var14 = 2;
               }

               super.fraImgSubEff.drawFrame(var14, var12.x, var12.y, var3, 3, var1);
            }

            return;
         case 291:
            if (super.f == 4) {
               super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 33, var1);
            }

            if (super.mframe[super.f] >= 0) {
               super.fraImgEff.drawFrame(super.mframe[super.f], this.objBeFireMain.x - super.am_duong * 30, this.objBeFireMain.y + 5, super.Dir, 33, var1);
               return;
            }
            break;
         case 301:
            if (super.f == 1 || super.f == 15) {
               super.fraImgEff.drawFrame(0, super.x, super.y, 0, 33, var1);
            }

            if (super.objFireMain.isTanHinh) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x + this.CM[(super.f - 2) / 2][0], super.y + this.CM[(super.f - 2) / 2][1], super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if (super.f > var2 << 2) {
                  var13 = (Point_Focus)this.VecSubEff.elementAt(var2);
                  byte var11 = 0;
                  if (super.Dir == 0) {
                     var11 = 2;
                  }

                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSub2Eff.nFrame, var13.x, var13.y, var11, 3, var1);
               }
            }

            return;
         case 303:
         case 503:
            if (super.f >= 10 && super.f <= super.fRemove) {
               this.AA(var1, super.fraImgEff, super.frame, super.x, super.y);
               this.AA(var1, super.fraImgEff, this.DL, this.DJ.x, this.DJ.y);
               this.AA(var1, super.fraImgEff, this.DM, this.DK.x, this.DK.y);
               return;
            }
            break;
         case 401://Xoáy đen
            return;
         case 402:
            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               if ((var15 = (Point)this.VecEff.elementAt(var2)).frame != 0) {
                  var5 = false;
                  if (var15.f < var15.fRe - 3) {
                     var14 = (var15.f + var15.fSmall) % 3;
                  } else {
                     var14 = super.fraImgEff.maxNumFrame - (var15.fRe - var15.f);
                  }

                  super.fraImgEff.drawFrameNew_BeginSuper(var14, var15.x / 1000, var15.y / 1000, 0, 3, var1);
               }
            }

            return;
         case 471:
            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgEff.drawFrame(var15.f % super.fraImgEff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 472:
            this.AV(var1);
            return;
         case 473:
            this.AS(var1);
            return;
         case 481:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var3 = (var8 = (Point_Focus)this.VecEff.elementAt(var4)).AG * super.fraImgEff.frameHeight / super.fraImgEff.nFrame + super.fraImgEff.frameHeight / super.fraImgEff.nFrame) > super.fraImgEff.frameHeight) {
                  var3 = super.fraImgEff.frameHeight;
               }

               if (super.fraImgEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), 0, super.fraImgEff.frameHeight - var3 + var8.AG % super.fraImgEff.nFrame * super.fraImgEff.frameHeight, super.fraImgEff.frameWidth, var3, 0, super.x, super.y, 33);
               }
            }

            for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
               var8 = (Point_Focus)this.VecSubEff.elementAt(var4);
               var16 = 2;
               if (super.Dir == 2) {
                  var16 = 0;
               }

               if (super.f > 8) {
                  super.fraImgSub2Eff.drawFrame((super.f - 8) / super.numNextFrame % super.fraImgSub2Eff.nFrame, var8.x, var8.y + 5, var16, 3, var1);
               }
            }

            return;
         case 482:
            if (super.mframe[super.f] >= 0) {
               super.fraImgEff.drawFrame(super.mframe[super.f], super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var12 = (Point)this.VecEff.elementAt(var4);
               if (super.mframeSub[var12.f] >= 0) {
                  super.fraImgSubEff.drawFrame(super.mframeSub[var12.f], var12.x, var12.y, super.Dir == 2 ? 0 : 2, 3, var1);
               }
            }

            if (super.f > 30) {
               for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
                  var8 = (Point_Focus)this.VecSubEff.elementAt(var4);
                  if (super.Dir == 2) {
                     super.fraImgSubEff.drawFrame(7, var8.x, var8.y, var4 == 0 ? 2 : 0, 3, var1);
                  } else {
                     super.fraImgSubEff.drawFrame(7, var8.x, var8.y, var4 == 0 ? 0 : 2, 3, var1);
                  }
               }

               return;
            }
            break;
         case 483:
            if (super.f >= 9 && super.f <= 11 || super.f >= 24 && super.f <= 26) {
               super.BP.drawFrame(0, super.objFireMain.x, super.objFireMain.y, super.Dir, 33, var1);
            }

            if (super.f <= 11 || super.f >= 26) {
               super.fraImgEff.drawFrameNew(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x + super.am_duong * 5, super.y, super.Dir == 0 ? 2 : 0, 3, var1);
            }

            super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y + 20, super.Dir == 0 ? 2 : 0, 33, var1);

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSub2Eff.drawFrame(var13.AG / super.numNextFrame % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 33, var1);
            }

            return;
         case 491:
            if (super.f == 4) {
               super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 33, var1);
            }

            if (super.f > 4 && super.f < super.fRemove) {
               super.fraImgSub2Eff.drawFrame((super.f - 5) / 2 % super.fraImgSub2Eff.nFrame, this.objBeFireMain.x - super.am_duong * 30, this.objBeFireMain.y, super.Dir, 33, var1);
            }

            if (super.mframe[super.f] >= 0) {
               super.fraImgEff.drawFrame(super.mframe[super.f], this.objBeFireMain.x - super.am_duong * 30, this.objBeFireMain.y + 5, super.Dir, 33, var1);
               return;
            }
            break;
         case 492:
            if (super.f == 4) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 3, var1);
            }

            if (super.f == 24) {
               super.fraImgSubEff.drawFrame(0, this.x1000, this.y1000, super.Dir, 33, var1);
            }

            if (super.mframe[super.f] >= 0) {
               super.fraImgEff.drawFrame(super.mframe[super.f], super.x, super.y + 5, super.Dir, 33, var1);
               return;
            }
            break;
         case 493:
            this.AX(var1);
            return;
         case 501:
            if (super.f == 1 || super.f == 15) {
               super.fraImgEff.drawFrame(0, super.x, super.y, 0, 33, var1);
            }

            if (super.objFireMain.isTanHinh) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x + this.CM[(super.f - 2) / 2][0], super.y + this.CM[(super.f - 2) / 2][1], super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if (super.f > var2 << 2) {
                  var13 = (Point_Focus)this.VecSubEff.elementAt(var2);
                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
               }
            }

            return;
         case 502:
            if (super.f > 2 && super.f < 15) {
               var8 = (Point_Focus)this.VecEff.elementAt(0);
               super.fraImgSub3Eff.drawFrame(super.f / 3 < super.fraImgSub3Eff.nFrame ? super.f / 3 : super.fraImgSub3Eff.nFrame - 1, var8.x, var8.y, super.Dir ^ 2, 3, var1);
            }

            if (super.f >= 7 && super.f <= 15) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x + super.am_duong * 40, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, 3, var1);
            }

            if (super.f >= 15) {
               var10 = 40;
               if (super.Dir == 2) {
                  var10 = -40;
               }

               super.fraImgSub2Eff.drawFrame(0, super.x, super.y + 50, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.mframe[super.f / 2 % super.mframe.length], super.x, super.y, super.Dir, 3, var1);
               super.fraImgSub2Eff.drawFrame(0, super.x + var10, super.y - 20 + 50, super.Dir, 3, var1);
               super.fraImgSubEff.drawFrame(super.mframe[super.f / 2 % super.mframe.length], super.x + var10, super.y - 20, super.Dir, 3, var1);
               return;
            }
            break;
         case 511:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + super.f % super.fraImgSubEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
               var2 = 12 + CRes.random(super.fraImgEff.maxNumFrame);
               if (CRes.random(2) == 0) {
                  var2 -= 4;
               }

               super.fraImgEff.drawFrameNew(var2, super.x, super.y, super.Dir, 3, var1);
            }

            if (super.f >= 5 && super.f < super.fRemove) {
               super.fraImgSub2Eff.drawFrame((super.f - 5) / 2 % super.fraImgSub2Eff.nFrame, this.objBeFireMain.x - super.am_duong * 30, this.objBeFireMain.y, super.Dir, 33, var1);
            }

            if (super.f > 5 && super.f % 4 == 0) {
               super.fraImgSub3Eff.drawFrameNew(super.f % super.fraImgSub3Eff.nFrame, this.objBeFireMain.x - super.am_duong * 30, this.objBeFireMain.y, super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var15 = (Point)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrameNew(12 - (var15.frame << 2) + var15.f, var15.x, var15.y, super.Dir, 3, var1);
            }

            return;
         case 512:
            if (super.f >= 20 && super.f < 23 || super.f >= 10 && super.f < 13) {
               super.fraImgEff.drawFrame(super.f % 10, super.x, super.y, super.Dir, 3, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgSub3Eff.drawFrame(var15.f / 2, var15.x, var15.y, 0, 3, var1);
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + var13.AG % super.fraImgSubEff.maxNumFrame, var13.x, var13.y, 0, 3, var1);
               super.fraImgSub2Eff.drawFrame(CRes.random(super.fraImgSub2Eff.nFrame), var13.x, var13.y, 0, 3, var1);
            }

            return;
         case 513:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrameNew(super.BE * super.fraImgSubEff.maxNumFrame + super.f % super.fraImgSubEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
               if (super.fraImgSub2Eff != null) {
                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               }
            }

            for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
               var13 = (Point_Focus)this.VecEff.elementAt(var2);
               super.fraImgEff.drawFrame(var13.AG % super.fraImgEff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
               if (super.fraImgSub2Eff != null) {
                  super.fraImgSub2Eff.drawFrame(var13.AG % super.fraImgSub2Eff.nFrame, var13.x, var13.y, super.Dir, 3, var1);
               }
            }

            return;
         case 1998:
         case 1999:
            this.AB(var1);
            return;
         case 2000:
            super.fraImgEff.drawFrame(super.AS, super.x, super.y - 22, 0, 3, var1);
            return;
         case 10001:
            this.paintPan_1(var1);
            return;
         case 10002:
            if (super.f < 6) {
               super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.x, super.y - super.objFireMain.dy, super.Dir, 33, var1);
            }

            if (super.f >= 13 && super.f <= 18) {
               super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, 33, var1);
               return;
            }
            break;
         case 10003:
            this.AY(var1);
            return;
         case 10004:
            if (super.f < 4) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var9 = (Point)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrame(var9.f / 2 % super.fraImgEff.nFrame, var9.x, var9.y, super.Dir, 33, var1);
            }

            return;
         case 10006:
         case 10011:
            for(var3 = 0; var3 < this.CT.size(); ++var3) {
               var7 = (Point_Focus)this.CT.elementAt(var3);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 0, 40, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 2, 36, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 1, 24, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 3, 0, var1);
               var1.setColor(0);
               var1.fillRect(var7.x - 1, var7.y - 1, 3, 3);
            }

            return;
         case 10007:
            for(var3 = 0; var3 < this.CT.size(); ++var3) {
               var7 = (Point_Focus)this.CT.elementAt(var3);
               super.fraImgEff.drawFrame(var7.AG / 2 % super.fraImgEff.nFrame, var7.x, var7.y, super.Dir, 33, var1);
               super.fraImgSubEff.drawFrame(var7.AG / 2 % super.fraImgSubEff.nFrame, var7.x, var7.y, super.Dir, 3, var1);
            }

            return;
         case 10008:
            for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
               var9 = (Point)this.VecSubEff.elementAt(var3);
               super.fraImgEff.drawFrame(var9.frame, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 10010:
         case 10013:
            super.fraImgEff.drawFrame(GameCanvas.gameTick / super.numNextFrame % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, 3, var1);
            return;
         case 10012:
            if (super.f <= super.fRemove) {
               super.fraImgEff.drawFrame(1, this.x1000 / 1000, this.y1000, super.Dir, 3, var1);
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               (var9 = (Point)this.VecEff.elementAt(var3)).AY.drawFrame(var9.f / 2, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 10015:
            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var9 = (Point)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrame(var9.frame, super.objFireMain.x, super.objFireMain.y + var9.y, super.Dir, 33, var1);
            }

            return;
         case 10017:
            super.fraImgEff.drawFrame(GameCanvas.gameTick / super.numNextFrame % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 4, super.Dir, 3, var1);
            return;
         case 10018:
            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var9 = (Point)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrame(var9.frame, var9.x, var9.y, super.Dir, 33, var1);
            }

            return;
         case 10020:
         case 10021:
         case 10022:
         case 10026:
            super.fraImgEff.drawFrame(GameCanvas.gameTick / super.numNextFrame % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, 3, var1);
            return;
         case 10024:
            super.fraImgEff.drawFrame(GameCanvas.gameTick / super.numNextFrame % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, 3, var1);

            for(var3 = 0; var3 < this.CT.size(); ++var3) {
               var7 = (Point_Focus)this.CT.elementAt(var3);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 0, 40, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 2, 36, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 1, 24, var1);
               super.fraImgSubEff.drawFrame(var7.frame + var7.AG / 3 % 2, var7.x, var7.y, 3, 0, var1);
            }

            return;
         case 10025:
            super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 10028:
            if (super.f > 16) {
               var5 = false;
               var4 = GameCanvas.gameTick % 2 << 1;
               if (this.y1000 == 0) {
                  super.fraImgSub3Eff.drawFrameNew(GameCanvas.gameTick / 3 % super.fraImgSub3Eff.nFrame, super.x, super.y + 7, 0, 3, var1);
               }

               var1.setColor(-15263716);
               var1.fillRect(super.x - 20 - -4, super.y - 350 - this.y1000, 32, 360);
               var1.setColor(-14868960);
               var1.fillRect(super.x - 18 - -4, super.y - 350 - this.y1000, 28, 360);
               var1.setColor(-9736336);
               var1.fillRect(super.x - 16 - -4, super.y - 350 - this.y1000, 24, 360);
               var1.setColor(-14276565);
               var1.fillRect(super.x - 14 - -4 + var4, super.y - 350 - this.y1000, 20 - (var4 << 1), 360);
               var1.setColor(-7236459);
               var1.fillRect(super.x - 12 - -4 + var4, super.y - 350 - this.y1000, 16 - (var4 << 1), 360);
               var1.setColor(-262402);
               var1.fillRect(super.x - 10 - -4 + var4, super.y - 350 - this.y1000, 12 - (var4 << 1), 360);
               return;
            }
            break;
         case 10030:
            return;
         }
      } catch (Exception var6) {
      }

   }

   private void AC(mGraphics var1) {
      int var2;
      for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point_Focus var3 = (Point_Focus)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(0, var3.x, var3.y, 0, 3, var1);
      }

      for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var4 = (Point)this.VecSubEff.elementAt(var2);
         super.fraImgEff.drawFrame(var4.f, var4.x, var4.y, 0, 3, var1);
      }

   }

   private void AD(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.VecSubEff.elementAt(var2)).frame == 0 && super.typeEffect == 244) {
            super.fraImgSubEff.drawFrameNew(var3.f / 3 % super.fraImgSubEff.nFrame, var3.x, var3.y, super.Dir, 33, var1);
         } else {
            super.fraImgEff.drawFrameNew(var3.f / 3 % super.fraImgEff.nFrame, var3.x, var3.y, super.Dir, 33, var1);
         }
      }

   }

   private void AE(mGraphics var1) {
      if (super.f > 20 && super.fraImgEff.getImageFrame() != null) {
         byte var2 = 1;
         int var3;
         int var4;
         if (super.f < 24) {
            var2 = 3;
            var4 = super.fraImgEff.frameWidth / 3;
            var3 = super.fraImgEff.frameWidth / 2 - var4 / 2;
         } else if (super.f < 27) {
            var2 = 2;
            var4 = super.fraImgEff.frameWidth / 2;
            var3 = super.fraImgEff.frameWidth / 2 - var4 / 2;
         } else {
            var4 = super.fraImgEff.frameWidth;
            var3 = 0;
         }

         var1.drawRegion(super.fraImgEff.getImageFrame(), var3, 0, var4, super.fraImgEff.frameHeight, 0, MainScreen.cameraMain.xCam + super.x, MainScreen.cameraMain.yCam + super.y, 3);
         if (super.f < 24) {
            var4 = super.fraImgSubEff.frameWidth / var2;
            var3 = super.fraImgSubEff.frameWidth / 2 - var4 / 2;
         } else if (super.f < 27) {
            var4 = super.fraImgSubEff.frameWidth / var2;
            var3 = super.fraImgSubEff.frameWidth / 2 - var4 / 2;
         } else {
            var4 = super.fraImgSubEff.frameWidth;
            var3 = 0;
         }

         var1.drawRegion(super.fraImgSubEff.getImageFrame(), var3, 0, var4, super.fraImgSubEff.frameHeight, 0, MainScreen.cameraMain.xCam + this.x1000, MainScreen.cameraMain.yCam + this.y1000, 3);
      }

   }

   private void AF(mGraphics var1) {
      if (super.f <= super.fRemove) {
         super.fraImgEff.drawFrame(0, super.x, super.y, 0, 3, var1);
      }

      for(int var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(var3.f, var3.x, var3.y, 0, 3, var1);
      }

   }

   private void AG(mGraphics var1) {
      if (super.frame == 0) {
         super.fraImgEff.drawFrameNew(4, super.x - (super.am_duong << 2), super.y + 14 - 4 - 8, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(4, super.x + (super.am_duong << 2), super.y + 14 - 2 - 8, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(5, super.x + (super.am_duong << 3), super.y + 14 - 8, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(5, super.x + super.am_duong * 12, super.y + 14 + 2 - 8, super.Dir, 33, var1);
      } else if (super.frame == 1) {
         super.fraImgEff.drawFrameNew(2, super.x - super.am_duong * 5, super.y - 6 - 24, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(2, super.x + super.am_duong * 5, super.y - 3 - 16, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(3, super.x + super.am_duong * 15, super.y + 2 - 10, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(3, super.x + super.am_duong * 25, super.y + 13 - 10, super.Dir, 33, var1);
      } else if (super.frame == 2) {
         super.fraImgEff.drawFrameNew(0, super.x - super.am_duong * 5, super.y - 6 - 24, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(0, super.x + super.am_duong * 5, super.y - 3 - 6 - 10, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(0, super.x + super.am_duong * 15, super.y + 2 - 10, super.Dir, 33, var1);
         super.fraImgEff.drawFrameNew(0, super.x + super.am_duong * 25, super.y + 13 - 10, super.Dir, 33, var1);
      }

      if (super.f >= 10 && super.f < 14 && !this.checkNullObject((int)1)) {
         super.fraImgSubEff.drawFrameNew(2 + (super.f - 10) / 2, super.objFireMain.x + super.am_duong * 20, super.objFireMain.y - 50, super.Dir, 3, var1);
      }

   }

   private void AH(mGraphics var1) {
      for(int var2 = this.VecEff.size() - 1; var2 >= 0; --var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(var3.frame, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void AI(mGraphics var1) {
      byte var2 = 0;
      if (!this.checkNullObject((int)1) && super.objFireMain.Dir == 2) {
         var2 = 2;
      }

      if (super.f < 6) {
         super.fraImgSubEff.drawFrameNew(super.f / 2 % super.fraImgSubEff.nFrame, super.x - 10 * super.am_duong, super.y, var2, 3, var1);
      }

      for(int var4 = this.VecEff.size() - 1; var4 >= 0; --var4) {
         Point_Focus var3 = (Point_Focus)this.VecEff.elementAt(var4);
         super.fraImgEff.drawFrame(var3.AG / 2 % super.fraImgEff.nFrame, var3.x, var3.y, super.Dir, 3, var1);
      }

   }

   private void AJ(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(2 + var3.f % 3, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void AK(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3 = (Point)this.VecSubEff.elementAt(var2);
         super.fraImgSubEff.drawFrame(var3.f % super.fraImgSubEff.nFrame, var3.x / 10, var3.y / 10, 0, 33, var1);
      }

      if (super.f <= 2) {
         super.fraImgEff.drawFrame(super.f, super.x, super.y, 0, 33, var1);
      }

      if (super.f >= 10 && super.f <= 12) {
         super.fraImgEff.drawFrame(12 - super.f, super.x, super.y, 0, 33, var1);
      }

   }

   private void AL(mGraphics var1) {
      if (super.f > 2 && super.f < 5) {
         super.fraImgEff.drawFrame(super.f - 13, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
      } else if (super.f > 12 && super.f < 15) {
         super.fraImgEff.drawFrame(super.f - 23, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
      } else {
         if (super.f > 18 && super.f < 21) {
            super.fraImgEff.drawFrame(super.f - 29, super.objFireMain.x, super.objFireMain.y - 10, super.Dir, 33, var1);
         }

      }
   }

   public final void update() {
      if (super.objFireMain == null || !super.objFireMain.returnAction() && super.objFireMain.Action != 4) {
         super.update();
         byte var1;
         Point point2;
         MainObject var3;
         Point var4;
         int var5;
         Object_Effect_Skill var6;
         int var9;
         int var12;
         Point var15;
         float var10000;
         switch(super.typeEffect) {
         case -1:
            if (super.f < super.fRemove) {
               this.updateAngleXP();
               super.frame = this.setFrameAngle(this.gocT_Arc);
            }

            if (this.VecEff.size() == 0 && super.f > super.fRemove) {
               this.removeEff();
            }

            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               ++(point2 = (Point)this.VecEff.elementAt(var12)).f;
               if (point2.f / 2 > 3) {
                  this.VecEff.removeElement(point2);
                  --var12;
               }
            }

            if (super.f == super.fRemove) {
               GameScreen.addEffectEnd((short)108, 0, super.x, super.y + 10, super.Dir, super.objMainEff);
               return;
            }
            break;
         case 0:
            this.updateAngleNormal(this.objBeFireMain, 0);
            return;
         case 1:
         case 37:
            this.updateLuffy1();
            return;
         case 2:
            this.update_Ace_1();
            return;
         case 3:
         case 229:
         case 262:
         case 263:
         case 264:
            this.update_Ace_2();
            return;
         case 4:
         case 230:
            this.update_Aokiji_1();
            return;
         case 5:
         case 231:
            this.update_Aokiji_2();
            return;
         case 6:
         case 232:
            this.update_Smoker_1();
            return;
         case 7:
            this.updateUssopSea1();
            return;
         case 9:
         case 53:
         case 163:
            this.updateNami1();
            return;
         case 10:
         case 234:
            this.update_Smoker_2();
            return;
         case 11:
            this.updateNamiSea1();
            return;
         case 12:
         case 188:
         case 220:
         case 293:
            this.updateSanji2();
            return;
         case 13:
         case 258:
            this.update_Mon_Smoker_1();
            return;
         case 14:
         case 44:
            this.updateSanji4();
            return;
         case 15:
         case 38:
            this.updateZoro3();
            return;
         case 16:
         case 51:
            this.updateNami4();
            return;
         case 17:
         case 165:
         case 166:
            if (super.f >= super.fRemove) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.isTanHinh = false;
               }

               this.removeEff();
               return;
            }
            break;
         case 18:
            this.update_Mon_Smoker_2();
            return;
         case 19:
            this.updateZoroSea3();
            return;
         case 20:
            this.update_Mon_Valentine();
            return;
         case 21:
         case 33:
         case 176:
            this.updateLuffyS1();
            return;
         case 22:
         case 98:
            this.updateCabaji_2();
            return;
         case 23:
            this.update_Mon_Mr5();
            return;
         case 24:
         case 80:
            if (super.f == 7 || super.f == 2 || super.f == 12) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.NF = true;
               }

               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
               if (super.typeEffect == 24 && !this.checkNullObject((int)2)) {
                  GameScreen.addEffectEnd((short)4, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3) - 10, super.Dir, super.objMainEff);
               }
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 25:
         case 235:
            this.update_Crocodile_1();
            return;
         case 26:
         case 236:
            this.update_Crocodile_2();
            return;
         case 27:
            if (super.f >= super.fRemove) {
               if (!this.checkNullObject((int)2)) {
                  this.setAva(0, this.objBeFireMain);
                  GameScreen.addEffectEnd((short)36, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
               }

               this.removeEff();
               return;
            }
            break;
         case 28:
            if (super.f >= super.fRemove) {
               if (!this.checkNullObject((int)2)) {
                  this.setAva(0, this.objBeFireMain);
                  GameScreen.addEffectEnd_ObjTo((short)102, this.subType, super.toX, super.toY, (short)this.objBeFireMain.ID, (byte)this.objBeFireMain.typeObject, super.Dir, (MainObject)null);
               }

               this.removeEff();
               return;
            }
            break;
         case 29:
            this.updateZoro8();
            return;
         case 30:
            if (super.f >= super.fRemove) {
               GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 31:
         case 55:
         case 56:
         case 191:
         case 223:
            this.updateNami5();
            return;
         case 32:
            this.updateWapol_1();
            return;
         case 34:
            this.updateLuffy6();
            return;
         case 35:
            this.updateLuffy_S2_L2();
            return;
         case 36:
            if (super.f >= super.fRemove) {
               this.setAva(1, this.objBeFireMain);
               this.removeEff();
               return;
            }
            break;
         case 39:
            this.updateWapol_3();
            return;
         case 40:
            this.update_Wapol_4();
            return;
         case 41:
            this.updateZoroS2_L1_NEW();
            return;
         case 42:
            this.updateZoroSea1();
            return;
         case 43:
            this.updateZoroSea2();
            return;
         case 45:
            this.updateMr3_1();
            return;
         case 46:
            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 47:
         case 48:
            this.updateSanji1();
            return;
         case 49:
         case 50:
            this.updateSanjiSkill3_Lv1();
            return;
         case 52:
         case 189:
         case 221:
         case 311:
            this.update_Nami_S1_L3();
            return;
         case 54:
            this.updateMr3_2();
            return;
         case 57:
            this.updateUssop2();
            return;
         case 58:
            this.updateUssopSkill1_Lv3();
            return;
         case 59:
         case 60:
         case 62:
            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 61:
            if (super.f >= super.fRemove) {
               GameScreen.addEffectEnd((short)60, 2, super.toX, super.toY, super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 63:
         case 190:
         case 222:
         case 312:
            this.updateNami1_SHORT();
            return;
         case 64:
         case 66:
            this.updateUssop_Skill2();
            return;
         case 65:
            if (super.f >= super.fRemove) {
               this.addSound((byte)5);
               this.addVir(3, 5, 10, false);
               GameScreen.addEffectEnd((short)35, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)21, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)107, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.setAva(2, this.objBeFireMain);
               this.removeEff();
               return;
            }
            break;
         case 67:
         case 68:
         case 69:
         case 194:
            this.update_Ussop_S3_L1();
            return;
         case 70:
            if (super.f == 4) {
               super.x += super.am_duong * 20;
               super.y -= 10;
               var12 = super.toX - super.x;
               var9 = super.toY - super.y;
               this.create_Speed(var12, var9, (Point_Focus)null);
               super.fRemove += 4;
            }

            if (super.f >= super.fRemove) {
               GameScreen.addEffectEnd((short)4, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 71:
         case 72:
         case 75:
         case 92:
         case 145:
         case 146:
         case 147:
         case 148:
            if (super.f >= super.fRemove) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.NF = true;
               }

               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 73:
         case 78:
            if (super.f == 5 || super.f == 0) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.NF = true;
               }

               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 74:
            this.update_Mon_5();
            return;
         case 76:
            if (super.f >= super.fRemove) {
               GameScreen.addEffectEnd((short)8, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 77:
            this.updateAlvida2();
            return;
         case 79:
            if (super.f == 6 || super.f == 0) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.NF = true;
               }

               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 81:
         case 143:
         case 149:
            this.updateMon10();
            return;
         case 82:
         case 144:
            this.updateMon11();
            return;
         case 83:
         case 180:
         case 212:
            this.updateLuffyS1_L3_SHORT();
            return;
         case 84:
         case 181:
         case 213:
         case 272:
            this.updateLuffyS2_NEW_SHORT();
            return;
         case 85:
         case 182:
            this.updateLuffyS3_New();
            return;
         case 86:
         case 183:
         case 215:
            this.updateZoro_S1_L3_SHORT();
            return;
         case 87:
         case 184:
         case 216:
            this.updateZoroS2_New();
            return;
         case 88:
            this.updateMorgan_1();
            return;
         case 89:
            this.updateMorgan_2();
            return;
         case 90:
         case 91:
            if (super.f > super.fRemove) {
               this.addSound((byte)2);
               GameScreen.addEffectEnd((short)3, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 93:
            this.updateMohji_1();
            return;
         case 94:
            this.updateMohji_2();
            return;
         case 95:
            this.updateBuggy_1();
            return;
         case 96:
            this.updateBuggy_2();
            return;
         case 97:
            this.updateCabaji_1();
            return;
         case 99:
            if (super.f == 2) {
               this.addSound((byte)10);
            }

            if (super.f == 2 || super.f == 8) {
               GameScreen.addEffectEnd((short)3, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            }

            if (super.f > super.fRemove) {
               this.setAva(0, this.objBeFireMain);
               this.removeEff();
               return;
            }
            break;
         case 100:
            this.updateNyaban_2();
            return;
         case 101:
            this.updateNyaban_3();
            return;
         case 102:
            this.updateJango_1();
            return;
         case 103:
            this.updateKuro_1();
            return;
         case 104:
            this.updateKuro_2();
            return;
         case 105:
         case 107:
            if (super.f >= super.fRemove) {
               this.addSound((byte)14);
               this.addVir(3, 5, 10, false);
               GameScreen.addEffectEnd((short)35, 0, super.x, super.y, super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
               this.removeEff();
               return;
            }
            break;
         case 106:
            this.updatePearl_2();
            return;
         case 108:
            this.updateGhin_2();
            return;
         case 109:
            this.updateDonKrieg_1();
            return;
         case 110:
            this.updateDonKrieg_2();
            return;
         case 111:
            this.updateDonKrieg_3();
            return;
         case 112:
         case 270:
            this.updateHachi_1();
            return;
         case 113:
         case 150:
         case 151:
         case 152:
         case 153:
            this.updateHachi_2();
            return;
         case 114:
            this.updateChu_1();
            return;
         case 115:
            this.updateChu_2();
            return;
         case 116:
            this.updateKurobi_1();
            return;
         case 117:
            this.updateKurobi_2();
            return;
         case 118:
            this.updateArlong_1();
            return;
         case 119:
            this.updateArlong_2();
            return;
         case 120:
            this.updateArlong_3();
            return;
         case 121:
            this.update_Zoro_S3_L1();
            return;
         case 122:
            this.update_Zoro_S3_L2();
            return;
         case 123:
         case 185:
         case 217:
            this.update_Zoro_S3_L3();
            return;
         case 124:
         case 186:
         case 218:
            this.updateSanji_S1_L3_SHORT();
            return;
         case 125:
         case 187:
            this.updateSanji_S2_L3_New_SHORT();
            return;
         case 126:
         case 192:
            this.updateUssopSkill1_Lv3_SHORT();
            return;
         case 127:
         case 193:
         case 225:
            this.updateUssop_S2_L3_New();
            return;
         case 128:
            if (super.f == 0 || super.f == 8) {
               GameScreen.addEffectEnd((short)8, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 129:
         case 130:
            if (super.f == 0 || super.f == 8 || super.f == 14) {
               GameScreen.addEffectEnd((short)8, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 131:
         case 132:
            this.updateLuffyMon16_17();
            return;
         case 133:
            this.updateLuffySea1();
            return;
         case 134:
         case 135:
            this.updateLuffySea2();
            return;
         case 136:
            this.updateSanjiSea1();
            return;
         case 137:
         case 138:
            this.updateSanjiSea2();
            return;
         case 139:
            this.updateNamiSea2();
            return;
         case 140:
            this.updateNamiSea3();
            return;
         case 141:
            this.updateUssopSea2();
            return;
         case 142:
            this.updateUssopSea3();
            return;
         case 154:
            this.updateZoro1();
            return;
         case 155:
            this.updateZoro2();
            return;
         case 156:
            this.updateLuffyS1_NEW();
            return;
         case 157:
            this.updateZoroS1_New();
            return;
         case 158:
         case 177:
            this.updateSanji_S1_L3_New();
            return;
         case 159:
            this.updateUssopSkill1_Lv3_New();
            return;
         case 160:
            this.updateLuffyS2_NEW();
            return;
         case 161:
            this.updateZoroS2_New_SHORT();
            return;
         case 162:
            this.updateSanji_S2_L3_New();
            return;
         case 164:
         case 227:
            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               (point2 = (Point)this.VecEff.elementAt(var12)).update();
               ++point2.frame;
               if (point2.dis == 0) {
                  if (point2.frame >= super.fraImgEff.nFrame) {
                     point2.frame = 0;
                  }
               } else if (point2.frame >= super.fraImgSubEff.nFrame) {
                  point2.frame = 0;
               }

               if (point2.f >= point2.fRe) {
                  if (CRes.random(2) == 1) {
                     this.setAva(0, this.objBeFireMain);
                  }

                  this.VecEff.removeElement(point2);
                  --var12;
               }
            }

            if (super.f <= super.fRemove - 5 && super.f % 3 == 0) {
               (var15 = new Point()).x = super.x + super.am_duong * 15;
               var15.y = super.y;
               var15.vx = super.am_duong * (5 + CRes.random(2));
               if (super.typeEffect == 227) {
                  var15.vx = super.am_duong * (5 + CRes.random(2));
               }

               var15.vy = CRes.random_Am_0(2);
               var15.fRe = 6 + CRes.random(3);
               var15.dis = CRes.random(3) == 0 ? 0 : 1;
               this.VecEff.addElement(var15);
               if (CRes.random(2) == 0) {
                  this.addSound((byte)4);
                  if (!this.checkNullObject((int)2)) {
                     GameScreen.addEffectEnd((short)108, 1, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
                  }
               }
            }

            if (super.f >= super.fRemove && this.VecEff.size() == 0) {
               this.removeEff();
               return;
            }
            break;
         case 167:
            this.updateMissMS_1();
            return;
         case 168:
            this.update_Mr1_1();
            return;
         case 169:
         case 237:
            this.updateSet_1();
            return;
         case 170:
         case 238:
            this.updateSet_2();
            return;
         case 171:
         case 239:
            this.updateNamThach_1();
            return;
         case 172:
         case 240:
            this.update_Nham_thach_2();
            return;
         case 173:
            this.update_Mr1_2();
            return;
         case 174:
            this.update_DF_1();
            return;
         case 175:
            this.update_DF_2();
            return;
         case 178:
            this.update_Mr0_1();
            return;
         case 179:
         case 241:
            this.update_Pell_1();
            return;
         case 195:
            this.update_Enel_1();
            return;
         case 196:
            this.update_Enel_2();
            return;
         case 197:
            this.update_Enel_3();
            return;
         case 198:
            this.update_Satori_1();
            return;
         case 199:
            this.update_Satori_2();
            return;
         case 200:
            this.update_Ohm_1();
            return;
         case 201:
            this.update_Ohm_2();
            return;
         case 202:
            this.update_Gedatsu_1();
            return;
         case 203:
            this.update_Gedatsu_2();
            return;
         case 204:
            this.update_Shura_1();
            return;
         case 205:
            this.update_Shura_2();
            return;
         case 206:
         case 207:
            this.update_Linh_Troi();
            return;
         case 208:
            this.update_Tru_1();
            return;
         case 209:
         case 242:
            this.update_Lucci_1();
            return;
         case 210:
         case 243:
            this.update_Dong_Dat_1();
            return;
         case 211:
         case 244:
            this.update_Dong_Dat_2();
            return;
         case 214:
         case 273:
            this.updateLuffyS3_L5();
            return;
         case 219:
            if (super.f >= super.fRemove || this.checkNullObject((int)3)) {
               super.objFireMain.isTanHinh = false;
               this.removeEff();
            }

            if (super.f == 5 || super.f == 14) {
               if (MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 160) {
                  super.objFireMain.x = this.objBeFireMain.x;
                  super.objFireMain.y = this.objBeFireMain.y;
               }

               super.objFireMain.isTanHinh = true;
               this.changeDir();
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
            }

            if (super.f == 9 || super.f == 19) {
               super.objFireMain.isTanHinh = true;
               this.changeDir();
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
            }

            if (super.f == 6 || super.f == 11 || super.f == 15 || super.f == 20) {
               var1 = 0;
               if (super.f == 6 || super.f == 15) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  var1 = 1;
               }

               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.addVir(5, 5, 10, true);
               GameScreen.addEffectEnd((short)36, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)25, 4, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
            }

            if (super.f == 24) {
               super.objFireMain.isTanHinh = false;
               super.objFireMain.x = this.x1000;
               super.objFireMain.y = this.y1000;
               return;
            }
            break;
         case 224:
         case 301:
            this.update_Ussop_S1_L5();
            return;
         case 226:
            this.update_Ussop_S3_L5();
            return;
         case 228:
         case 259:
         case 260:
         case 261:
            this.update_Ace_1_L2();
            return;
         case 233:
            return;
         case 245:
         case 251:
            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               ++(point2 = (Point)this.VecEff.elementAt(var12)).f;
               if (point2.f >= point2.fRe) {
                  this.VecEff.removeElement(var12);
                  --var12;
               }
            }

            if (super.f == 6 && !this.checkNullObject((int)1)) {
               super.objFireMain.isTanHinh = true;
               (var15 = new Point(super.objFireMain.x + super.am_duong * 30, super.objFireMain.y - super.objFireMain.hOne / 2)).fRe = 6;
               var15.dis = super.objFireMain.type_left_right;
               this.VecEff.addElement(var15);
            }

            if (super.f == 7 && MainObject.getDistance(super.objMainEff.x, super.objMainEff.y, this.objBeFireMain.x, this.objBeFireMain.y) < 260 && !this.checkNullObject((int)3)) {
               super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
               super.objFireMain.y = this.objBeFireMain.y;
               (var15 = new Point(super.objFireMain.x - super.am_duong * 30, super.objFireMain.y - super.objFireMain.hOne / 2)).fRe = 6;
               var15.dis = super.objFireMain.type_left_right;
               this.VecEff.addElement(var15);
            }

            if (super.f == 8 && !this.checkNullObject((int)1)) {
               super.objFireMain.isTanHinh = false;
            }

            if (super.typeEffect == 251 && super.f >= 8 && super.f <= 20 && !this.checkNullObject((int)3)) {
               super.objFireMain.dy = (super.f - 8) / 3 * 12;
               this.objBeFireMain.dy = super.objFireMain.dy;
            }

            if (!this.checkNullObject((int)2) && (super.f == 9 || super.f == 12 || super.f == 15 || super.f == 18)) {
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
               if (super.f == 9 || super.f == 15) {
                  this.setAva(1, this.objBeFireMain);
               }
            }

            if (super.f >= super.fRemove && this.VecEff.size() == 0) {
               this.removeEff();
               return;
            }
            break;
         case 246:
         case 253:
            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               (point2 = (Point)this.VecEff.elementAt(var12)).update();
               if (point2.frame == 0 && point2.f == point2.fRe) {
                  point2.frame = 1;
                  point2.vx = 0;
                  point2.vy = 0;
                  point2.fSmall = CRes.random(16, 24);
                  GameScreen.addEffectEnd((short)133, 1, point2.x, point2.y, super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd((short)59, 0, point2.x, point2.y + 5, super.Dir, super.objMainEff);
                  if (point2.AZ != null) {
                     this.setAva(1, point2.AZ);
                  }
               }

               if (point2.frame == 1 && point2.f == point2.fRe + point2.fSmall) {
                  point2.frame = 2;
                  point2.f = 0;
               }

               if (point2.frame == 2 && point2.f == 4) {
                  point2.frame = 3;
                  point2.f = 0;
               }

               if (point2.frame == 3 && point2.f == 2) {
                  this.VecEff.removeElement(point2);
                  --var12;
               }
            }

            if (super.f == 12) {
               if (this.isAddSound) {
                  this.addSound((byte)14);
               }

               var12 = 0;

               label960:
               while(true) {
                  if (var12 >= super.vecObjsBeFire.size()) {
                     if (this.checkNullObject((int)1)) {
                        break;
                     }

                     var12 = CRes.random(1, 3);
                     var9 = 0;

                     while(true) {
                        if (var9 >= var12) {
                           break label960;
                        }

                        Point var11;
                        (var11 = new Point()).vy = CRes.random(30, 40);
                        var11.dis = CRes.random(14, 26);
                        var11.fRe = 4 + super.vecObjsBeFire.size() + var9;
                        var11.x = this.objBeFireMain.x + CRes.random_Am_0(100);
                        var11.y = this.objBeFireMain.y + CRes.random_Am_0(80);
                        var11.frame = 0;
                        if (GameCanvas.loadmap.AA(var11.x, var11.y) == 0) {
                           var11.y += -(var11.vy * var11.fRe) + CRes.random(5);
                           this.VecEff.addElement(var11);
                        }

                        ++var9;
                     }
                  }

                  if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var12)) != null && (var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
                     (var4 = new Point()).vy = CRes.random(30, 40);
                     var4.dis = CRes.random(14, 26);
                     var4.fRe = var12 + 4;
                     var4.x = var3.x;
                     var4.y = var3.y + CRes.random(5);
                     if (super.typeEffect == 253) {
                        for(var5 = 0; var5 < 2; ++var5) {
                           (point2 = new Point()).vy = CRes.random(30, 40);
                           point2.dis = var4.dis + 10;
                           point2.fRe = var12 + 5 + var5;
                           point2.x = var3.x - 25 + var5 * 50;
                           point2.y = var4.y - point2.vy * point2.fRe;
                           point2.frame = 0;
                           this.VecEff.addElement(point2);
                        }
                     }

                     var4.y += -var4.vy * var4.fRe;
                     var4.frame = 0;
                     var4.AZ = var3;
                     this.VecEff.addElement(var4);
                  }

                  ++var12;
               }
            }

            if (super.f >= super.fRemove && this.VecEff.size() == 0) {
               this.removeEff();
               return;
            }
            break;
         case 247:
         case 254:
            for(var12 = 0; var12 < this.VecSubEff.size(); ++var12) {
               ++(point2 = (Point)this.VecSubEff.elementAt(var12)).f;
               if (point2.f >= point2.fRe) {
                  this.VecSubEff.removeElement(point2);
               }
            }

            int var8;
            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               Point_Focus var7;
               (var7 = (Point_Focus)this.VecEff.elementAt(var12)).update_Vx_Vy();
               if (var7.AG >= var7.fRe + 10) {
                  this.VecEff.removeElement(var7);
               } else {
                  var8 = CRes.random(1, 4);

                  for(int var10 = 0; var10 < var8; ++var10) {
                     Point var13;
                     (var13 = new Point(var7.x / 10 + CRes.random_Am_0(4) - var7.AI / 10, var7.y / 10 + CRes.random_Am_0(4) - var7.vy / 10)).fRe = 4;
                     if (super.typeEffect == 254 && var10 == 0 && CRes.random(3) == 0) {
                        var13.fRe = 5;
                     }

                     this.VecSubEff.addElement(var13);
                  }
               }

               if (var7.AG == var7.fRe && !this.checkNullObject((int)2)) {
                  this.setAva(1, this.objBeFireMain);
                  GameScreen.addEffectEnd((short)8, 0, var7.x / 10, var7.y / 10, super.Dir, super.objMainEff);
                  if (super.typeEffect == 254) {
                     GameScreen.addEffectEnd((short)108, 1, var7.x / 10, var7.y / 10 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
                  }
               }
            }

            if (super.f == 10 || super.f == 15) {
               if (this.isAddSound) {
                  this.addSound((byte)18);
               }

               Point_Focus var14 = new Point_Focus(super.x * 10, super.y * 10);
               if (!this.checkNullObject((int)2)) {
                  var9 = this.objBeFireMain.x - super.x;
                  var8 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
                  (var14 = this.create_Speed(var9 * 10, var8 * 10, var14, super.x * 10, super.y * 10, this.objBeFireMain.x * 10, (this.objBeFireMain.y - this.objBeFireMain.hOne / 2) * 10)).AB = 0;
                  if (var9 > 0) {
                     var14.AB = 2;
                  }

                  this.VecEff.addElement(var14);
               }
            }

            if (super.f >= super.fRemove && this.VecEff.size() == 0) {
               this.removeEff();
               return;
            }
            break;
         case 248:
         case 255:
            if (!this.checkNullObject((int)1)) {
               if (super.f == 6 || super.f == 11) {
                  super.objFireMain.isTanHinh = true;
               }

               if (super.f == 7) {
                  super.objFireMain.isTanHinh = false;
               }

               if (super.f == 12) {
                  if (this.isAddSound) {
                     this.addSound((byte)51);
                  }

                  if (MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 260) {
                     super.objFireMain.x = this.objBeFireMain.x;
                     super.objFireMain.y = this.objBeFireMain.y + 5;
                     super.objFireMain.dy = 400;
                  }
               }

               if (super.f == 14) {
                  super.objFireMain.isTanHinh = false;
                  super.objFireMain.dy = 400;
               }

               if (super.f >= 15 && super.f <= 19) {
                  if (super.typeEffect == 255) {
                     GameScreen.addEffectEnd((short)108, 5, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, super.Dir, super.objMainEff);
                     GameScreen.addEffectEnd((short)108, 5, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy + 40, super.Dir, super.objMainEff);
                  }

                  if (super.objFireMain.dy >= 0) {
                     MainObject var17 = super.objFireMain;
                     var17.dy -= 80;
                  }
               }

               if (super.f == 19) {
                  if (this.isAddSound) {
                     this.addSound((byte)5);
                  }

                  super.objFireMain.dy = 0;
                  this.setAva(1, this.objBeFireMain);
                  GameScreen.addEffectEnd((short)148, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objMainEff);
                  if (super.typeEffect == 255) {
                     GameScreen.addEffectEnd((short)54, 12, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objMainEff);
                  }

                  GameScreen.addEffectEnd((short)45, 0, super.objFireMain.x, super.objFireMain.y + 25, super.Dir, super.objMainEff);
               }
            }

            if (super.f > super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 249:
         case 252:
            for(var12 = 0; var12 < this.VecEff.size(); ++var12) {
               ++(point2 = (Point)this.VecEff.elementAt(var12)).f;
               if (super.typeEffect == 252) {
                  point2.AF = (point2.f - 1) / 3 * 12;
                  point2.AZ.dy = point2.AF;
               }

               if (point2.f == 1 || point2.f == 4 || point2.f == 7 || point2.f == 10) {
                  GameScreen.addEffectEnd((short)10, 0, point2.AZ.x, point2.AZ.y - point2.AZ.dy - point2.AZ.hOne / 2, super.Dir, super.objMainEff);
                  if (point2.f == 1 || point2.f == 7) {
                     this.setAva(1, point2.AZ);
                  }
               }

               if (point2.f >= point2.fRe) {
                  this.VecEff.removeElement(var12);
                  --var12;
               }
            }

            if (super.f == 7 && !this.checkNullObject((int)1)) {
               super.objFireMain.isTanHinh = true;
            }

            if (super.f == 8) {
               for(var12 = 0; var12 < super.vecObjsBeFire.size(); ++var12) {
                  if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var12)) != null && (var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
                     (var4 = new Point()).dis = CRes.random(2) << 1;
                     var4.fSmall = -1;
                     if (var4.dis == 2) {
                        var4.fSmall = 1;
                     }

                     var5 = 30 * var4.fSmall;
                     var4.x = var3.x - var5;
                     var4.fRe = this.CM.length;
                     var4.y = var3.y - var3.hOne / 2;
                     var4.frame = CRes.random(4) * 3;
                     var4.AZ = var3;
                     this.VecEff.addElement(var4);
                  }
               }
            }

            if (!this.checkNullObject((int)2) && (super.f == 9 || super.f == 12 || super.f == 15 || super.f == 18)) {
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               if (CRes.random(2) == 0) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }
            }

            if (super.f == 20 && !this.checkNullObject((int)1)) {
               super.objFireMain.isTanHinh = false;
            }

            if (super.f >= super.fRemove && this.VecEff.size() == 0) {
               this.removeEff();
               return;
            }
            break;
         case 250:
            this.update_Tru_2();
            return;
         case 266:
            this.updateRankyaku();
            return;
         case 267:
            this.updateShigan();
            return;
         case 268:
            this.updateDoor();
            return;
         case 269:
            this.updateDoor2();
            return;
         case 271:
            this.update_Luffy_S1_L6();
            return;
         case 274:
         case 275:
            this.updateXaPhong();
            return;
         case 276:
            this.updateSoi();
            return;
         case 277:
            this.updateSoi2();
            return;
         case 278:
         case 279:
            this.updateHuou();
            return;
         case 280:
            if (super.x > super.toX) {
               super.x = super.toX;
               if (super.y < super.toY + 20) {
                  super.y += 5;
               }
            }

            if (super.f == 15) {
               GameScreen.addEffectEnd((short)178, 0, super.toX, super.toY - 55, super.Dir, super.objMainEff);
            }

            if (super.f == 10) {
               GameScreen.addEffectEnd((short)119, 4, super.objFireMain.x + 20, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            }

            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 281:
            this.update_Zoro_S1_L6();
            return;
         case 282:
            this.update_Zoro_S2_L6();
            return;
         case 283:
            this.update_Zoro_S3_L6();
            return;
         case 291:
            if (super.f >= super.fRemove || this.checkNullObject((int)3)) {
               super.objFireMain.isTanHinh = false;
               this.removeEff();
            }

            if (super.f == 0 || super.f == 8 || super.f == 15 || super.f == 25) {
               super.objFireMain.isTanHinh = true;
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
               if (super.f == 8 || super.f == 15) {
                  GameScreen.addEffectEnd((short)119, 4, this.objBeFireMain.x + super.am_duong * 10, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - 5, super.Dir, super.objMainEff);
               }
            }

            if (super.f == 6 || super.f == 11 || super.f == 15 || super.f == 20) {
               var1 = 0;
               if (super.f == 6 || super.f == 15) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  var1 = 1;
               }

               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.addVir(5, 5, 10, true);
               GameScreen.addEffectEnd((short)36, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)25, 4, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
               return;
            }
            break;
         case 292:
            if (super.f >= super.fRemove || this.checkNullObject((int)3)) {
               super.objFireMain.isTanHinh = false;
               this.removeEff();
            }

            if (super.f == 0 || super.f == 8 || super.f == 15 || super.f == 25) {
               if (MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 160) {
                  super.objFireMain.x = this.objBeFireMain.x;
                  super.objFireMain.y = this.objBeFireMain.y;
               }

               super.objFireMain.isTanHinh = true;
               this.changeDir();
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
            }

            if (super.f >= 15 && super.f <= 19) {
               super.y -= 12 * (super.f - 14);
               super.objFireMain.isTanHinh = true;
            }

            if (super.f >= 20 && super.f <= 24) {
               super.y += 12 * (super.f - 19);
               super.objFireMain.isTanHinh = true;
            }

            if (super.f == 25) {
               GameScreen.addEffectEnd((short)172, 0, this.objBeFireMain.x, super.y + 5, super.Dir, super.objMainEff);
            }

            if (super.f == 6 || super.f == 11 || super.f == 15 || super.f == 20) {
               var1 = 0;
               if (super.f == 6 || super.f == 15) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  var1 = 1;
               }

               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.addVir(5, 5, 10, true);
               GameScreen.addEffectEnd((short)36, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)25, 4, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
               return;
            }
            break;
         case 302:
            this.updateUssop_S2_L6();
            return;
         case 303:
            this.update_Ussop_S3_L6();
            return;
         case 313:
            this.updateNami6();
            return;
         case 401://Xoáy đen
            return;
         case 402:
            this.update_Blackhole();
            return;
         case 471:
            this.update_Luffy_S1_L7();
            return;
         case 472:
            this.update_Luffy_S2_L7();
            return;
         case 473:
            this.update_Luffy_S3_L7();
            return;
         case 481:
            this.update_Zoro_S1_L7();
            return;
         case 482:
            this.update_Zoro_S2_L7();
            return;
         case 483:
            this.update_Zoro_S3_L7();
            return;
         case 491:
            if (super.f >= super.fRemove || this.checkNullObject((int)3)) {
               super.objFireMain.isTanHinh = false;
               this.removeEff();
            }

            if (super.f == 0 || super.f == 8 || super.f == 15 || super.f == 25) {
               super.objFireMain.isTanHinh = true;
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
               this.setAva(2, this.objBeFireMain);
               if (super.f == 8 || super.f == 15) {
                  GameScreen.addEffectEnd((short)119, 4, this.objBeFireMain.x + super.am_duong * 15, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - 5, super.Dir, super.objMainEff);
               }
            }

            if (super.f == 6 || super.f == 11 || super.f == 15 || super.f == 20) {
               var1 = 0;
               if (super.f == 6 || super.f == 15) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  var1 = 1;
               }

               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.addVir(3, 5, 10, true);
               GameScreen.addEffectEnd((short)36, var1, this.objBeFireMain.x - super.am_duong * 35, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)25, 4, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.objBeFireMain.x += super.am_duong * 18;
               this.setAva(2, this.objBeFireMain);
               return;
            }
            break;
         case 492:
            if (super.f >= super.fRemove || this.checkNullObject((int)3)) {
               super.objFireMain.isTanHinh = false;
               this.removeEff();
            }

            if (super.f == 0 || super.f == 8 || super.f == 15 || super.f == 25) {
               if (MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 160) {
                  super.objFireMain.x = this.objBeFireMain.x;
                  super.objFireMain.y = this.objBeFireMain.y;
               }

               super.objFireMain.isTanHinh = true;
               this.changeDir();
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
               super.x = this.objBeFireMain.x + super.am_duong * 30;
               super.y = this.objBeFireMain.y;
            }

            if (super.f >= 15 && super.f <= 19) {
               super.y -= 12 * (super.f - 14);
               super.objFireMain.isTanHinh = true;
            }

            if (super.f >= 20 && super.f <= 24) {
               super.y += 12 * (super.f - 19);
               super.objFireMain.isTanHinh = true;
            }

            if (super.f == 25) {
               GameScreen.addEffectEnd((short)172, 1, this.objBeFireMain.x, super.y + 5, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)112, 2, super.objFireMain.x - super.objFireMain.wOne, super.objFireMain.y + 10, super.Dir, super.objFireMain);
            }

            if (super.f == 6 || super.f == 11 || super.f == 15 || super.f == 20) {
               var1 = 0;
               if (super.f == 6 || super.f == 15) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  var1 = 1;
               }

               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.addVir(5, 5, 10, true);
               GameScreen.addEffectEnd((short)36, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)25, 4, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
               return;
            }
            break;
         case 493:
            this.update_Sanji_S3_L7();
            return;
         case 501:
            this.update_Ussop_S1_L7();
            return;
         case 502:
            this.update_Ussop_S2_L7();
            return;
         case 503:
            this.update_Ussop_S3_L7();
            return;
         case 511:
            this.update_Nami_S1_L7();
            return;
         case 512:
            this.update_Nami_S2_L7();
            return;
         case 513:
            this.update_Nami_S3_L7();
            return;
         case 1998:
         case 1999:
            this.updateEffThunderFall();
            return;
         case 2000:
            this.updateEffFireExplore();
            return;
         case 10001:
            this.update_Pan1();
            return;
         case 10002:
            this.updatePan2();
            return;
         case 10003:
         case 10017:
         case 10020:
         case 10021:
         case 10022:
         case 10026:
            if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
               this.removeEff();
               return;
            }
            break;
         case 10004:
            this.updateGalio2();
            return;
         case 10005:
            this.updateNoNangLuong1();
            return;
         case 10006:
            this.updateNoNangLuong2();
            return;
         case 10007:
            this.updateNoNangLuong3();
            return;
         case 10008:
            this.updateNoTheoHuong_1();
            return;
         case 10009:
            this.updateNoTheoHuong_2();
            return;
         case 10010:
         case 10013:
            this.updateXerath1();
            return;
         case 10011:
         case 10024:
            this.updateXerath2();
            return;
         case 10012:
            this.updatexerath3();
            return;
         case 10015:
            this.updateUrgot3();
            return;
         case 10018:
            this.updateMonster_Chay_Thang();
            return;
         case 10019:
            if (!this.checkNullObject((int)1)) {
               super.objFireMain.vx = super.am_duong * 15;
            }

            if (super.f >= super.fRemove) {
               if (!this.checkNullObject((int)1)) {
                  super.objFireMain.vx = 0;
               }

               this.removeEff();
               return;
            }
            break;
         case 10023:
            this.updateMonster_DanhTron();
            return;
         case 10025:
            if (super.f >= super.fRemove) {
               GameScreen.addEffectEnd((short)57, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.removeEff();
               return;
            }
            break;
         case 10027:
            if (super.f >= super.fRemove) {
               this.removeEff();
               return;
            }
            break;
         case 10028:
            this.updateHoDen();
            break;
         case 10030:
            this.update_ho_den_vu_tru();
            return;
         }

      } else {
         this.removeEff();
      }
   }

   private void updateAngleXP() {
      Point var1;
      if (super.typeEffect == -1) {
         (var1 = new Point()).x = super.x;
         var1.y = super.y;
         this.VecEff.addElement(var1);
      }

      if (this.objBeFireMain != null && !this.objBeFireMain.isRemove && super.f < super.fRemove) {
         MainObject var10000 = this.objBeFireMain;
         int var4 = this.objBeFireMain.x - super.x;
         int var2 = this.objBeFireMain.y - (this.objBeFireMain.hOne >> 1) - super.y;
         ++super.life;
         if ((CRes.abs(var4) >= 16 || CRes.abs(var2) >= 16) && super.life <= super.fRemove) {
            int var3;
            if (CRes.abs((var3 = CRes.AA(var4, var2)) - this.gocT_Arc) < 90 || var4 * var4 + var2 * var2 > 4096) {
               if (CRes.abs(var3 - this.gocT_Arc) < 15) {
                  this.gocT_Arc = var3;
               } else if ((var3 - this.gocT_Arc < 0 || var3 - this.gocT_Arc >= 180) && var3 - this.gocT_Arc >= -180) {
                  this.gocT_Arc = CRes.fixangle(this.gocT_Arc - 15);
               } else {
                  this.gocT_Arc = CRes.fixangle(this.gocT_Arc + 15);
               }
            }

            if (super.va < 8192) {
               super.va += 3096;
            }

            this.CZ = super.va * CRes.getcos(this.gocT_Arc) >> 10;
            this.DA = super.va * CRes.getsin(this.gocT_Arc) >> 10;
            var4 = var4 + this.CZ >> 10;
            super.x += var4;
            var4 = var2 + this.DA >> 10;
            super.y += var4;
            if (super.typeEffect != -1) {
               (var1 = new Point()).x = super.x;
               var1.y = super.y;
               this.VecEff.addElement(var1);
            }

         } else {
            super.f = super.fRemove;
         }
      } else {
         super.f = super.fRemove;
      }
   }

   private void update_Tru_2() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var1);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var3;
         (var3 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var3.AG >= var3.fRe) {
            GameScreen.addEffectEnd((short)25, 4, var3.x, var3.y, (byte)var3.dis, super.objMainEff);
            this.VecEff.removeElement(var1);
            --var1;
         } else {
            (var2 = new Point(var3.x, var3.y)).fRe = 5;
            this.VecSubEff.addElement(var2);
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Dong_Dat_2() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var2.frame == 1 && var2.f >= 6) {
            var2.frame = 0;
            var2.f = 0;
         }

         if (var2.f / 3 >= 4) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= 12 && super.f % 2 == 0 && super.f <= 30) {
         var1 = (super.f - 12) / 4;
         int var8 = var1 + 4;
         var1 = 30 + var1 * 35;
         int var3 = 100 / var8;
         int var4;
         int var5;
         Point var6;
         int var7;
         if (super.f % 4 == 0) {
            var4 = 180 + var8 * var3 / 2;

            for(var5 = 0; var5 < var8; ++var5) {
               (var6 = new Point()).x = super.x + CRes.getcos(CRes.fixangle(var4)) * var1 / 1000;
               var6.y = super.y + CRes.getsin(CRes.fixangle(var4)) * var1 / 1000;
               if (super.typeEffect == 244) {
                  var6.frame = 1;
               }

               if ((var7 = GameCanvas.loadmap.AA(var6.x, var6.y)) == 0 || var7 == 2) {
                  this.VecSubEff.addElement(var6);
                  if (var5 % 2 == 0) {
                     GameScreen.addEffectEnd((short)110, 0, var6.x, var6.y, super.Dir, super.objMainEff);
                  }

                  GameScreen.addEffectEnd((short)63, 0, var6.x, var6.y + 5, super.Dir, super.objMainEff);
               }

               var4 -= var3;
            }
         }

         if (super.f % 4 == 2) {
            var1 += 15;
            var4 = 360 - var8 * var3 / 2;

            for(var5 = 0; var5 < var8; ++var5) {
               (var6 = new Point()).x = super.x + CRes.getcos(CRes.fixangle(var4)) * var1 / 1000;
               var6.y = super.y + CRes.getsin(CRes.fixangle(var4)) * var1 / 1000;
               if (super.typeEffect == 244) {
                  var6.frame = 1;
               }

               if ((var7 = GameCanvas.loadmap.AA(var6.x, var6.y)) == 0 || var7 == 2) {
                  this.VecSubEff.addElement(var6);
                  if (var5 % 2 == 0) {
                     GameScreen.addEffectEnd((short)110, 0, var6.x, var6.y, super.Dir, super.objMainEff);
                  }

                  GameScreen.addEffectEnd((short)63, 0, var6.x, var6.y + 5, super.Dir, super.objMainEff);
               }

               var4 += var3;
            }
         }

         if (this.isAddSound && super.f % 4 == 0) {
            float var9 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Ace_1() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            if (CRes.random(2) == 0) {
               this.addSound((byte)5);
            }

            this.VecEff.removeElement(var2);
            GameScreen.addEffectEnd((short)2, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            if (CRes.random(4) == 0) {
               GameScreen.addEffectEnd((short)110, 1, var2.x, var2.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)63, 0, var2.x, var2.y + 5, super.Dir, super.objMainEff);
            }

            --var1;
         }
      }

      if (super.f >= 7 && super.f <= 12 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = (super.f - 6) * 30;
      }

      if (super.f >= 14 && super.f <= 26) {
         if (super.f == 14 && !this.checkNullObject((int)3)) {
            super.toY = this.objBeFireMain.y;
            super.toX = this.objBeFireMain.x;
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
         }

         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f > 15 && super.f < 26 && super.f % 5 == 0) {
            this.addSound((byte)15);
         }

         if (super.f > 15 && super.f % 2 == 1 && super.f <= 25) {
            var1 = CRes.random(2, 4);

            for(int var4 = 0; var4 < var1; ++var4) {
               Point var3;
               (var3 = new Point()).vy = CRes.random(25, 35);
               var3.x = super.toX + CRes.random_Am(0, 50);
               var3.y = super.toY - var3.vy * 3 + CRes.random_Am(0, 20) + 5;
               var3.frame = CRes.random(super.fraImgEff.maxNumFrame);
               var3.fRe = 3;
               this.VecEff.addElement(var3);
            }
         }

         if (super.f % 6 == 1) {
            this.setAva(0, this.objBeFireMain);
            this.addVir(1, 6, 12, false);
         }
      }

      if (super.f == 27 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 80;
         super.objFireMain.isTanHinh = false;
      }

      if (super.f > 27 && super.f <= 30 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 80 - (super.f - 27) * 20;
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void update_Ace_1_L2() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         var2.y += var2.vy;
         if (var2.f >= super.fraImgSub2Eff.maxNumFrame) {
            this.VecSubEff.removeElement(var1);
            --var1;
         }
      }

      Point var3;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         var2 = (Point)this.VecEff.elementAt(var1);
         (var3 = new Point(var2.x, var2.y - 30)).vy = -4;
         this.VecSubEff.addElement(var3);
         var2.update();
         if (var2.f >= var2.fRe) {
            if (CRes.random(2) == 0) {
               this.addSound((byte)5);
            }

            this.VecEff.removeElement(var2);
            if (CRes.random(2) == 0) {
               byte var5 = (byte)(super.frameSuper + 1);
               if (super.typeEffect == 228) {
                  var5 = 0;
               }

               GameScreen.addEffectEnd((short)141, var5, var2.x, var2.y, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)2, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            }

            if (CRes.random(2) == 0) {
               GameScreen.addEffectEnd((short)110, 1, var2.x, var2.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)63, 0, var2.x, var2.y + 5, super.Dir, super.objMainEff);
            }

            --var1;
         }
      }

      if (super.f >= 4 && super.f <= 26) {
         super.objFireMain.NH = false;
      }

      if (super.f >= 7 && super.f <= 12 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = (super.f - 6) * 40;
      }

      if (super.f >= 14 && super.f <= 26) {
         if (super.f == 14 && !this.checkNullObject((int)3)) {
            super.toY = this.objBeFireMain.y;
            super.toX = this.objBeFireMain.x;
            this.x1000 = super.objFireMain.x;
            this.y1000 = super.objFireMain.y;
         }

         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f > 15 && super.f < 26 && super.f % 5 == 0) {
            this.addSound((byte)15);
         }

         if (super.f > 15 && super.f % 2 == 1 && super.f <= 25) {
            var1 = CRes.random(2, 4);

            for(int var4 = 0; var4 < var1; ++var4) {
               (var3 = new Point()).vy = CRes.random(25, 35);
               var3.x = super.toX + CRes.random_Am(0, 50);
               var3.y = super.toY - var3.vy * 3 + CRes.random_Am(0, 20) + 5;
               var3.frame = CRes.random(super.fraImgEff.maxNumFrame);
               var3.fRe = 3;
               this.VecEff.addElement(var3);
            }
         }

         if (super.f % 6 == 1) {
            this.setAva(0, this.objBeFireMain);
            this.addVir(1, 6, 12, false);
         }
      }

      if (super.f == 27 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 80;
         super.objFireMain.isTanHinh = false;
         super.objFireMain.NH = true;
      }

      if (super.f > 27 && super.f <= 30 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 80 - (super.f - 27) * 20;
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void update_Ace_2() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.frame == 0) {
            if (var2.f / 2 >= 5) {
               this.VecSubEff.removeElement(var2);
               --var1;
            }
         } else if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.frame == 1 && super.f % 4 == 0) {
            Point var3;
            (var3 = new Point()).x = var2.x / 1000;
            var3.y = var2.y / 1000;
            var3.frame = 1;
            var3.fRe = CRes.random(8, 10);
            var3.f = CRes.random(3);
            var3.fRe += var3.f;
            this.VecSubEff.addElement(var3);
         }

         if (var2.f >= var2.fRe) {
            this.addSound((byte)15);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f <= 10 && CRes.random(2) == 0) {
         Point var8;
         (var8 = new Point()).x = super.x + CRes.random_Am_0(10);
         var8.y = super.y - 10 - CRes.random(20);
         var8.vx = CRes.random_Am_0(3);
         var8.vy = -CRes.random(3, 7);
         this.VecSubEff.addElement(var8);
      }

      Point var4;
      int var6;
      if (super.f == 15) {
         this.addSound((byte)15);
         byte var9 = 12;
         if (super.typeEffect != 3) {
            var9 = 16;
         }

         int var5 = 0;

         for(var6 = 0; var6 < var9; ++var6) {
            var5 %= 360;
            (var4 = new Point(super.x * 1000, super.y * 1000)).vx = CRes.getcos(var5) * super.vMax;
            var4.vy = CRes.getsin(var5) * (super.vMax / 2);
            var4.fRe = 7;
            var4.frame = 0;
            this.VecEff.addElement(var4);
            var5 += 360 / var9;
         }
      }

      if (super.f == 20) {
         this.addSound((byte)15);
         this.addVir(1, 6, 12, true);
         var1 = 15;
         byte var7 = 16;
         if (super.typeEffect != 3) {
            var7 = 20;
         }

         for(var6 = 0; var6 < var7; ++var6) {
            var1 %= 360;
            (var4 = new Point(super.x * 1000, super.y * 1000)).vx = CRes.getcos(var1) * super.vMax;
            var4.vy = CRes.getsin(var1) * (super.vMax / 2);
            var4.fRe = 12;
            var4.fSmall = CRes.random(super.fraImgEff.maxNumFrame);
            var4.frame = 1;
            this.VecEff.addElement(var4);
            var1 += 360 / var7;
         }
      }

      if (super.typeEffect != 3 && super.f == 23) {
         this.addSound((byte)15);
         this.addVir(1, 6, 12, true);
         var1 = 30;

         for(var6 = 0; var6 < 24; ++var6) {
            var1 %= 360;
            (var4 = new Point(super.x * 1000, super.y * 1000)).vx = CRes.getcos(var1) * super.vMax;
            var4.vy = CRes.getsin(var1) * (super.vMax / 2);
            var4.fRe = 16;
            var4.fSmall = CRes.random(super.fraImgEff.maxNumFrame);
            var4.frame = 1;
            this.VecEff.addElement(var4);
            var1 += 15;
         }
      }

      if (super.f == 26) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var10;
            if ((var10 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null) {
               MainObject var11 = MainObject.get_Object((int)var10.ID, (byte)var10.tem);
               this.setAva(1, var11);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void update_Blackhole() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.addSound((byte)15);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      int var3;
      Point var4;
      int var5;
      Point var6;
      if (super.f == 7) {
         this.addSound((byte)15);
         byte var7 = 12;
         if (super.typeEffect != 3) {
            var7 = 16;
         }

         int var8 = 0;

         for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
            var4 = (Point)this.VecSubEff.elementAt(var3);

            for(var5 = 0; var5 < var7; ++var5) {
               var8 %= 360;
               (var6 = new Point(var4.x * 1000, var4.y * 1000)).vx = CRes.getcos(var8) * super.vMax;
               var6.vy = CRes.getsin(var8) * (super.vMax / 2);
               var6.fRe = 7;
               var6.frame = 0;
               this.VecEff.addElement(var6);
               var8 += 360 / var7;
            }
         }
      }

      if (super.f == 12) {
         this.addSound((byte)15);
         this.addVir(1, 6, 12, true);
         var1 = 15;
         byte var9 = 16;
         if (super.typeEffect != 3) {
            var9 = 20;
         }

         for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
            var4 = (Point)this.VecSubEff.elementAt(var3);

            for(var5 = 0; var5 < var9; ++var5) {
               var1 %= 360;
               (var6 = new Point(var4.x * 1000, var4.y * 1000)).vx = CRes.getcos(var1) * super.vMax;
               var6.vy = CRes.getsin(var1) * (super.vMax / 2);
               var6.fRe = 12;
               var6.fSmall = CRes.random(super.fraImgEff.maxNumFrame);
               var6.frame = 1;
               this.VecEff.addElement(var6);
               var1 += 360 / var9;
            }
         }
      }

      if (super.f == 26) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var10;
            if ((var10 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null) {
               MainObject var11 = MainObject.get_Object((int)var10.ID, (byte)var10.tem);
               this.setAva(1, var11);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void update_ho_den_vu_tru() {
      this.DE += 5;
      ++this.DG;
      if (this.DG % 2 == 0) {
         for(int var1 = 0; var1 < this.DD.length; ++var1) {
            GameScreen.addEffectEnd((short)166, 0, 2 * CRes.getcos(this.DD[var1]) * this.DE / 1024 + super.x, CRes.getsin(this.DD[var1]) * this.DE / 1024 + super.y, super.Dir, super.objMainEff);
            int[] var10000 = this.DD;
            var10000[var1] += 15;
            if (this.DD[var1] > 360) {
               this.DD[var1] = this.DD[var1] = 360;
            }
         }
      }

      ++this.DF;
      if (this.DF % 5 == 0) {
         this.removeEff();
      }

   }

   private void update_Aokiji_1() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         if ((var2 = (Point)this.VecEff.elementAt(var1)).f >= var2.fSmall) {
            if (super.typeEffect == 230) {
               Point var3;
               (var3 = new Point(var2.x, var2.y - 25)).vy = -4;
               var3.f = CRes.random(2);
               var3.fRe = 6;
               this.VecSubEff.addElement(var3);
            }

            var2.update();
         } else {
            ++var2.f;
         }

         if (var2.frame == 0) {
            if (var2.f >= var2.fRe) {
               var2.vy = 0;
               var2.frame = 1;
               var2.fRe = CRes.random(10, 12);
               var2.f = 0;
               GameScreen.addEffectEnd((short)17, CRes.random(20, 30), var2.x, var2.y, super.Dir, super.objMainEff);
               if (CRes.random(2) == 0) {
                  GameScreen.addEffectEnd((short)110, 2, var2.x, var2.y, super.Dir, super.objMainEff);
               }
            }
         } else if (var2.frame == 1 && var2.f == var2.fRe) {
            GameScreen.addEffectEnd((short)14, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      Point var6;
      if (super.f <= 25 && CRes.random(4) == 0) {
         (var6 = new Point()).x = super.x + CRes.random_Am_0(15);
         var6.y = super.y - CRes.random(20);
         var6.vx = CRes.random_Am_0(3);
         var6.vy = -CRes.random(3, 7);
         var6.fRe = 10;
         this.VecSubEff.addElement(var6);
      }

      if (super.typeEffect == 230 && super.f >= 16 && super.f < 20) {
         (var6 = new Point()).vy = CRes.random(30, 40);
         var6.dis = CRes.random(25, 35);
         var6.fSmall = 10;
         var6.x = super.objFireMain.x + CRes.random_Am_0(MotherCanvas.w / 2);
         var6.y = super.objFireMain.y - (var6.vy << 2) - 60;
         var6.frame = 0;
         var6.fRe = 5 + var6.fSmall + CRes.random(3);
         this.VecEff.addElement(var6);
      }

      Object_Effect_Skill var4;
      MainObject var5;
      if (super.f == 20) {
         this.addSound((byte)15);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               (var2 = new Point()).vy = CRes.random(30, 40);
               var2.dis = CRes.random(25, 35);
               var2.fRe = 4;
               var2.x = var5.x;
               var2.y = var5.y - var2.vy * var2.fRe + CRes.random(5);
               var2.frame = 0;
               this.VecEff.addElement(var2);
            }
         }

         if (!this.checkNullObject((int)1)) {
            for(var1 = 0; var1 < 5; ++var1) {
               (var2 = new Point()).vy = CRes.random(30, 40);
               var2.dis = CRes.random(25, 35);
               var2.fSmall = var1 * 3;
               var2.x = super.objFireMain.x + CRes.random_Am_0(MotherCanvas.w / 2);
               var2.y = super.objFireMain.y - (var2.vy << 2) - 60;
               var2.frame = 0;
               var2.fRe = 5 + var2.fSmall + CRes.random(3);
               this.VecEff.addElement(var2);
            }
         }
      }

      if (super.f == 24) {
         this.addVir(1, 6, 12, true);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null) {
               var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem);
               this.setAva(1, var5);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Aokiji_2() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f / 2 >= 5) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         Point var3;
         (var3 = new Point(var4.x, var4.y)).vx = CRes.random_Am_0(3);
         var3.vy = -CRes.random(3, 7);
         this.VecSubEff.addElement(var3);
         if (var4.AG == var4.fRe) {
            if (!this.checkNullObject((int)2)) {
               this.setAva(1, this.objBeFireMain);
            }

            if (super.typeEffect == 231) {
               if (!this.checkNullObject((int)3) && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 60) {
                  GameScreen.addEffectEnd((short)142, 1, var4.x - 30, var4.y + 8, super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd((short)142, 1, var4.x + 30, var4.y + 8, super.Dir, super.objMainEff);
               } else {
                  byte var6 = 1;
                  if (var4.vy < 0) {
                     var6 = -1;
                  }

                  if (var4.vy == 0) {
                     var6 = 0;
                  }

                  GameScreen.addEffectEnd((short)142, 0, var4.x - 40 * super.am_duong, var4.y + 8 - var6 * 15, super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd((short)142, 1, var4.x - 20 * super.am_duong, var4.y + 8 - var6 * 7, super.Dir, super.objMainEff);
               }
            }

            GameScreen.addEffectEnd((short)88, 0, var4.x, var4.y + 8, super.Dir, super.objMainEff);
            this.addVir(2, 6, 12, true);
            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      if (super.f <= 15 && CRes.random(4) == 0) {
         Point var7;
         (var7 = new Point()).x = super.x + CRes.random_Am_0(15);
         var7.y = super.y - CRes.random(20);
         var7.vx = CRes.random_Am_0(3);
         var7.vy = -CRes.random(3, 7);
         this.VecSubEff.addElement(var7);
      }

      if (super.f == 18) {
         if (!this.checkNullObject((int)2)) {
            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y;
         }

         Point_Focus var8 = new Point_Focus();
         int var5 = super.toX - (super.x + super.am_duong * 75);
         int var9 = super.toY - super.y;
         var8 = this.create_Speed(var5, var9, var8, super.x + super.am_duong * 75, super.y, super.toX, super.toY);
         this.VecEff.addElement(var8);
         this.addVir(2, 6, 12, true);
         GameScreen.addEffectEnd((short)110, 2, var8.x, var8.y, super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)110, 2, var8.x, var8.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Smoker_1() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         Point var3;
         if (super.typeEffect == 232 && super.f % 2 == 0 && !GameCanvas.lowGraphic) {
            (var3 = new Point()).x = var4.x;
            var3.y = var4.y;
            var3.fRe = 7;
            var3.frame = 1;
            this.VecSubEff.addElement(var3);
         }

         (var3 = new Point()).x = var4.x + CRes.random_Am_0(5);
         var3.y = var4.y;
         var3.vx = 0;
         var3.vy = -CRes.random(1, 4);
         var3.fRe = CRes.random(4, 7);
         this.VecSubEff.addElement(var3);
         ++var4.frame;
         if (var4.AG >= var4.fRe) {
            var4.AI = 0;
            var4.vy = 0;
            var4.x = var4.AK;
            var4.y = var4.AL;
         } else if (var4.frame / 2 > super.fraImgSubEff.nFrame - 1) {
            var4.frame = super.fraImgSubEff.nFrame - 1 << 1;
         }

         if (var4.AG >= var4.fRe && var4.frame >= super.fraImgSubEff.nFrame) {
            if (!this.checkNullObject((int)2)) {
               this.setAva(2, this.objBeFireMain);
            }

            this.addSound((byte)15);
            GameScreen.addEffectEnd((short)18, 0, var4.x, var4.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 4, var4.x, var4.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)54, 3, super.toX, super.toY, super.Dir, super.objMainEff);
            if (super.typeEffect == 232) {
               GameScreen.addEffectEnd((short)146, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            }

            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      Point var6;
      if (super.f <= 10 || super.f > 20 && super.f < 26) {
         if (CRes.random(4) == 0) {
            (var6 = new Point()).x = super.x + CRes.random_Am_0(15);
            var6.y = super.y + CRes.random(20);
            var6.vx = CRes.random_Am_0(3);
            var6.vy = -CRes.random(3, 7);
            var6.fRe = CRes.random(6, 10);
            this.VecSubEff.addElement(var6);
         }
      } else if (super.f <= 20 && CRes.random(2) == 0) {
         (var6 = new Point()).x = super.x + CRes.random_Am_0(15) - super.am_duong * 10;
         var6.y = super.y + CRes.random(20);
         var6.vx = CRes.random_Am_0(4);
         var6.vy = -CRes.random(4, 8);
         var6.fRe = CRes.random(8, 14);
         this.VecSubEff.addElement(var6);
      }

      if (super.f == 24) {
         this.addSound((byte)32);
         if (!this.checkNullObject((int)2)) {
            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
         }

         Point_Focus var8 = new Point_Focus();
         int var5 = super.toX - super.x;
         int var7 = super.toY - super.y;
         var8 = this.create_Speed(var5, var7, var8, super.x, super.y, super.toX, super.toY);
         this.VecEff.addElement(var8);
         this.addVir(5, 6, 12, true);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Smoker_2() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var10;
         (var10 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         Point var3;
         if (super.typeEffect == 234 && !GameCanvas.lowGraphic) {
            (var3 = new Point()).x = var10.x;
            var3.y = var10.y;
            var3.fRe = 3;
            var3.frame = 1;
            this.VecSubEff.addElement(var3);
         }

         (var3 = new Point()).x = var10.x;
         var3.y = var10.y;
         var3.vx = 0;
         var3.vy = -CRes.random(1, 3);
         var3.fRe = CRes.random(3, 6);
         this.VecSubEff.addElement(var3);
         if (var10.AG >= var10.fRe) {
            this.addVir(5, 6, 12, true);
            LoadMap.timeVibrateScreen = CRes.random(6, 12);
            GameScreen.addEffectEnd((short)18, 0, var10.x, var10.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)63, 0, var10.x, var10.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)110, 0, var10.x, var10.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var10);
            --var1;
         }
      }

      if (super.f <= 5 && CRes.random(3) == 0) {
         Point var12;
         (var12 = new Point()).x = super.x + CRes.random_Am_0(15);
         var12.y = super.y + CRes.random(20);
         var12.vx = CRes.random_Am_0(3);
         var12.vy = -CRes.random(3, 7);
         var12.fRe = CRes.random(6, 10);
         this.VecSubEff.addElement(var12);
      }

      if (super.f == 8 && !this.checkNullObject((int)1)) {
         super.objFireMain.NH = false;
      }

      if (super.f == 10 && !this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = true;
      }

      if (super.f == 15 || super.f == 20 || (super.f == 12 || super.f == 17) && super.typeEffect == 234 && !GameCanvas.lowGraphic) {
         this.addSound((byte)15);
         var1 = super.x - super.am_duong * 40 + CRes.random_Am_0(30);
         int var11 = super.y - 160 + CRes.random_Am_0(20);

         for(int var13 = 0; var13 < super.vecObjsBeFire.size(); ++var13) {
            Object_Effect_Skill var4;
            MainObject var14;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var13)) != null && (var14 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               Point_Focus var5 = new Point_Focus();
               int var6 = CRes.random_Am_0(15);
               int var7 = CRes.random_Am_0(10);
               int var8 = var14.x + var6 - var1;
               int var9 = var14.y + var7 - var11;
               var5 = this.create_Speed(var8, var9, var5, var1, var11, var14.x + var6, var14.y + var7);
               this.VecEff.addElement(var5);
            }
         }
      }

      if (super.f == 26 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 0;
         super.objFireMain.isTanHinh = false;
         super.objFireMain.NH = true;
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Mon_Smoker_1() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var9;
         (var9 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         Point var3;
         (var3 = new Point()).x = var9.x;
         var3.y = var9.y;
         var3.vx = 0;
         var3.vy = -CRes.random(1, 3);
         var3.fRe = CRes.random(3, 6);
         this.VecSubEff.addElement(var3);
         if (var9.AG >= var9.fRe) {
            this.addSound((byte)5);
            this.addVir(5, 5, 10, false);
            GameScreen.addEffectEnd((short)18, 0, var9.x, var9.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var9);
            --var1;
         }
      }

      if (super.f <= 7 && CRes.random(4) == 0) {
         Point var12;
         (var12 = new Point()).x = super.x + CRes.random_Am_0(15);
         var12.y = super.y + CRes.random(20);
         var12.vx = CRes.random_Am_0(3);
         var12.vy = -CRes.random(3, 7);
         var12.fRe = CRes.random(6, 10);
         this.VecSubEff.addElement(var12);
      }

      if (super.f == 9 && !this.checkNullObject((int)1)) {
         super.objFireMain.NH = false;
      }

      if (super.f == 11) {
         this.addSound((byte)3);
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = true;
         }
      }

      if (super.f == 15) {
         var1 = super.y - 160 + CRes.random_Am_0(20);

         for(int var10 = 0; var10 < super.vecObjsBeFire.size(); ++var10) {
            Object_Effect_Skill var11;
            MainObject var13;
            if ((var11 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var10)) != null && (var13 = MainObject.get_Object((int)var11.ID, (byte)var11.tem)) != null) {
               Point_Focus var4 = new Point_Focus();
               int var5 = CRes.random_Am_0(15);
               int var6 = CRes.random_Am_0(10);
               int var7 = var13.x + var5 - var13.x;
               int var8 = var13.y + var6 - var1;
               var4 = this.create_Speed(var7, var8, var4, var13.x, var1, var13.x + var5, var13.y + var6);
               this.VecEff.addElement(var4);
            }
         }
      }

      if (super.f == 18 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 0;
         super.objFireMain.isTanHinh = false;
         super.objFireMain.NH = true;
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Mon_Smoker_2() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var2.f / 2 >= super.fraImgSubEff.nFrame) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      Point_Focus var6;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var6 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (super.f % 2 == 0) {
            Point var3;
            (var3 = new Point()).x = var6.x;
            var3.y = var6.y;
            this.VecSubEff.addElement(var3);
         }

         if (var6.AG == var6.fRe) {
            this.addSound((byte)14);
            GameScreen.addEffectEnd((short)18, 0, var6.x, var6.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var6);
            --var1;
         }
      }

      if (super.f == 8) {
         this.addSound((byte)19);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var7;
            MainObject var8;
            if ((var7 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var7.ID, (byte)var7.tem)) != null) {
               var6 = new Point_Focus();
               int var4 = var8.x - super.x;
               int var5 = var8.y - var8.hOne / 2 - super.y;
               var6 = this.create_Speed(var4, var5, var6, super.x, super.y, var8.x, var8.y - var8.hOne / 2);
               this.VecEff.addElement(var6);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Mon_5() {
      if (super.f == 3 && !this.checkNullObject((int)1)) {
         byte var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         GameScreen.addEffectEnd((short)72, 2, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.NF = true;
         }

         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void update_Mon_Valentine() {
      if (super.f == 16) {
         for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd((short)63, 0, var3.x, var3.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)59, 0, var3.x, var3.y, super.Dir, super.objMainEff);
               this.setAva(1, var3);
            }
         }

         LoadMap.timeVibrateScreen = 10;
         if (!this.checkNullObject((int)1)) {
            MainObject var10000 = super.objFireMain;
            var10000.y += 4;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Mon_Mr5() {
      if (super.f >= super.fRemove) {
         this.removeEff();

         for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd((short)4, 0, var3.x + CRes.random_Am_0(15), var3.y - CRes.random(0, var3.hOne / 4 * 3) - 10, super.Dir, super.objMainEff);
               this.setAva(1, var3);
            }
         }
      }

   }

   private void update_Crocodile_1() {
      int var1;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var1);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var4.AG % 2 == 0) {
            Point var3;
            (var3 = new Point(var4.x, var4.y)).fRe = 4;
            this.VecSubEff.addElement(var3);
         }

         if (var4.AG >= var4.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == 9 && !this.checkNullObject((int)2)) {
         if (super.typeEffect == 235 && !GameCanvas.lowGraphic) {
            var1 = this.objBeFireMain.x - super.am_duong * 48 - super.objFireMain.x;
            int var5 = this.objBeFireMain.y - super.objFireMain.y;
            Point_Focus var7 = new Point_Focus(super.objFireMain.x * 10, super.objFireMain.y * 10);
            this.create_Speed(var1 * 10, var5 * 10, var7, super.objFireMain.x * 10, super.objFireMain.y * 10, (this.objBeFireMain.x - super.am_duong * 48) * 10, this.objBeFireMain.y);
            this.VecEff.addElement(var7);
         }

         super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 48;
         super.objFireMain.y = this.objBeFireMain.y;
      }

      if (super.f == 12 && !this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 15) {
         if (!this.checkNullObject((int)2)) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(10, 5, 10, true);
            if (super.vecObjsBeFire.size() > 1) {
               for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
                  Object_Effect_Skill var6;
                  MainObject var8;
                  if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
                     GameScreen.addEffectEnd((short)63, 0, var8.x + 10, var8.y, super.Dir, var8);
                     GameScreen.addEffectEnd((short)98, 0, var8.x, var8.y + 5, super.Dir, var8);
                     GameScreen.addEffectEnd((short)110, 0, var8.x, var8.y + 5, super.Dir, var8);
                     GameScreen.addEffectEnd((short)108, 5, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
                     this.setAva(1, var8);
                  }
               }
            } else {
               GameScreen.addEffectEnd((short)63, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)63, 0, this.objBeFireMain.x - 10, this.objBeFireMain.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)63, 0, this.objBeFireMain.x + 10, this.objBeFireMain.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)98, 0, this.objBeFireMain.x, this.objBeFireMain.y + 5, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)59, 0, this.objBeFireMain.x, this.objBeFireMain.y + 5, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)110, 0, this.objBeFireMain.x, this.objBeFireMain.y + 5, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 5, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            if (super.typeEffect == 235 && !GameCanvas.lowGraphic) {
               GameScreen.addEffectEnd((short)54, 10, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            }
         }

         this.setAva(2, this.objBeFireMain);
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void update_Crocodile_2() {
      if (super.f % 2 == 0 && super.f <= super.fRemove - 3) {
         Point var1;
         (var1 = new Point(super.x + CRes.random_Am_0(10), super.y + 10 + CRes.random_Am_0(10))).vx = CRes.random_Am_0(3);
         var1.vy = -CRes.random(3, 5);
         var1.fRe = CRes.random(10, 14);
         this.VecEff.addElement(var1);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      int var3;
      for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var3)).update();
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

      if (super.f == 13 && !this.checkNullObject((int)1)) {
         byte var5 = 1;
         if (super.typeEffect == 236 && !GameCanvas.lowGraphic) {
            var5 = 11;
         }

         GameScreen.addEffectEnd((short)54, var5, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      if (super.f == 15) {
         for(var3 = 0; var3 < super.vecObjsBeFire.size(); ++var3) {
            Object_Effect_Skill var4;
            MainObject var6;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var3)) != null && (var6 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               GameScreen.addEffectEnd((short)63, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)99, 0, var6.x, var6.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)59, 0, var6.x, var6.y, super.Dir, super.objMainEff);
               this.setAva(1, var6);
            }
         }
      }

      if (this.isAddSound && (super.f == 14 || super.f == 17 || super.f == 20)) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void update_Wapol_4() {
      if (super.f == 5 && !this.checkNullObject((int)2)) {
         GameScreen.addEffectEnd((short)57, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         this.setAva(1, this.objBeFireMain);
      }

      if (super.f == 8) {
         super.vx = 0;
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Nham_thach_2() {
      if (super.f > 10 && super.f % 3 == 0) {
         byte var1 = 0;
         if (super.typeEffect == 240) {
            var1 = 1;
         }

         if (this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var3;
            if (var2 != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd((short)113, var1, var3.x, var3.y, super.Dir, super.objMainEff);
               this.setAva(2, var3);
            }
         } else if (CRes.random(2) == 0) {
            GameScreen.addEffectEnd((short)113, var1, this.objBeFireMain.x + CRes.random_Am_0(160), this.objBeFireMain.y + CRes.random_Am_0(80), super.Dir, super.objMainEff);
         }

         if (super.f % 6 == 0) {
            this.addSound((byte)15);
         }

         this.addVir(3, 5, 10, false);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Mr1_1() {
      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         if (super.f % 4 == 0) {
            for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var2;
               MainObject var3;
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                  this.setAva(1, var3);
                  if (!this.checkNullObject((int)2)) {
                     GameScreen.addEffectEnd((short)1, 0, var3.x + CRes.random_Am_0(10), var3.y - var3.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
                  }
               }
            }
         }

      }
   }

   private void update_Mr1_2() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG == var2.fRe) {
            GameScreen.addEffectEnd((short)1, 0, var2.x + CRes.random_Am_0(10), var2.y + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(1, var2.AR);
         }

         if (var2.AG >= var2.fRe + 6) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 2) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var6;
            MainObject var7;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var7 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var3;
               (var3 = new Point_Focus()).x = super.x;
               var3.y = super.y;
               int var4 = var7.x - var3.x;
               int var5 = var7.y - var7.hOne / 2 - var3.y;
               (var3 = this.create_Speed(var4, var5, var3)).AR = var7;
               var3.dis = 0;
               if (super.x < var7.x) {
                  var3.dis = 2;
               }

               this.VecEff.addElement(var3);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_DF_1() {
      if (!this.checkNullObject((int)1)) {
         if (super.f >= 4 && super.f <= 10) {
            super.objFireMain.vx = super.vMax * super.am_duong;
         } else {
            super.objFireMain.vx = 0;
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void update_DF_2() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f < 4) {
            var2.frame = var2.f / 2;
         } else if (var2.f >= 4 && var2.f <= var2.fRe - 2) {
            var2.frame = 2;
         } else {
            var2.frame = var2.fRe - var2.f;
         }

         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == 2) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var4;
            MainObject var5;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               Point var3;
               (var3 = new Point()).x = var5.x;
               var3.y = var5.y + 4;
               var3.fRe = 30 + CRes.random(12);
               GameScreen.addEffectEnd((short)10, 0, var5.x, var5.y - var5.dy - var5.hOne / 2, super.Dir, super.objMainEff);
               this.VecEff.addElement(var3);
            }
         }

         if (this.objBeFireMain != null) {
            for(var1 = 0; var1 < 4; ++var1) {
               (var2 = new Point()).x = this.objBeFireMain.x + CRes.random_Am_0(160);
               var2.y = this.objBeFireMain.y + 4 + CRes.random_Am_0(80);
               var2.fRe = 30 + CRes.random(12);
               this.VecEff.addElement(var2);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Mr0_1() {
      if (super.f == 10 && !this.checkNullObject((int)2)) {
         super.vx = super.am_duong * super.vMax;
      }

      if (super.f == 15) {
         GameScreen.addEffectEnd_ToX_ToY((short)62, 0, super.x, super.y - 30, (int)(super.x + super.vx * 20), (int)(super.y - 30), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd_ToX_ToY((short)62, 0, super.x + 10 * super.am_duong, super.y - 20, (int)(super.x + super.vx * 20), (int)(super.y - 20), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd_ToX_ToY((short)62, 0, super.x + 20 * super.am_duong, super.y - 10, (int)(super.x + super.vx * 20), (int)(super.y - 10), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd_ToX_ToY((short)62, 0, super.x + 30 * super.am_duong, super.y, (int)(super.x + super.vx * 20), (int)super.y, super.Dir, super.objMainEff);
      }

      if (super.f < 10) {
         super.frame = -1;
      } else if (super.f < 14) {
         super.frame = 0;
      } else if (super.f < 30) {
         super.frame = 1;
      } else if (super.f < 35) {
         super.frame = 2;
      }

      if (super.f >= super.fRemove) {
         this.removeEff();

         for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               this.setAva(2, var3);
            }
         }
      }

   }

   private void update_Pell_1() {
      if (super.f > 1 && super.f < 26) {
         super.objFireMain.isTanHinh = true;
      } else {
         super.objFireMain.isTanHinh = false;
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (super.frame == 1) {
            var2.frame = CRes.random(2);
         }

         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (super.typeEffect == 241) {
            Point var3 = new Point(var2.x, var2.y);
            if (super.frame == 1) {
               var3.x = var2.x + var2.vx + CRes.random_Am_0(5);
               var3.y = var2.y + var2.vy + 5 + CRes.random_Am_0(15);
               var3.fRe = 10;
               var3.vx = CRes.random_Am_0(2);
               var3.vy = -CRes.random_Am(2, 5);
               Point var4;
               (var4 = new Point(var2.x + var2.vx + CRes.random_Am_0(5), var2.y + var2.vy + 5 + CRes.random_Am_0(15))).fRe = 10;
               var4.vx = CRes.random_Am_0(2);
               var4.vy = -CRes.random_Am(2, 5);
               this.VecSubEff.addElement(var4);
            } else {
               var3.fRe = 3;
               var3.frame = CRes.random(3);
            }

            this.VecSubEff.addElement(var3);
         }

         if (var2.f > 10) {
            var2.vy -= 2;
         } else {
            --var2.vy;
         }

         if (var2.f == 10 && !this.checkNullObject((int)2)) {
            if (super.frame == 1) {
               GameScreen.addEffectEnd((short)118, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)54, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            this.setAva(2, this.objBeFireMain);
         }

         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 4) {
         if (this.isAddSound) {
            float var10000;
            if (super.frame == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            } else {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         Point var7 = new Point();
         int var5 = super.toX;
         int var6 = super.toY;
         if (!this.checkNullObject((int)2)) {
            var5 = this.objBeFireMain.x;
            var6 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
         }

         var7.x = var5 - super.am_duong * 240;
         var7.vx = 24 * super.am_duong;
         var7.y = var6 - 55;
         var7.vy = 9;
         var7.dis = super.Dir;
         var7.fRe = 20;
         this.VecEff.addElement(var7);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Enel_1() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f > 10 && super.f < super.fRemove && super.f % 2 == 0) {
         if (this.CI < super.vecObjsBeFire.size()) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var4;
               MainObject var5;
               if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
                  Point var3;
                  (var3 = new Point()).x = var5.x * 10;
                  var3.y = (var5.y + 4) * 10;
                  var3.vx = CRes.random_Am_0(30);
                  var3.vy = CRes.random_Am_0(30);
                  var3.fRe = 15 + CRes.random(6);
                  GameScreen.addEffectEnd((short)10, 0, var5.x, var5.y - var5.dy - var5.hOne / 2, super.Dir, super.objMainEff);
                  this.setAva(2, var5);
                  this.VecEff.addElement(var3);
               }

               ++this.CI;
            }
         } else {
            Point var6;
            (var6 = new Point()).x = (this.objBeFireMain.x + CRes.random_Am_0(100)) * 10;
            var6.y = (this.objBeFireMain.y + CRes.random_Am_0(80)) * 10;
            var6.vx = CRes.random_Am_0(30);
            var6.vy = CRes.random_Am_0(30);
            var6.fRe = 10 + CRes.random(6);
            this.VecEff.addElement(var6);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Enel_2() {
      if (super.f >= super.fRemove) {
         this.removeEff();
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= 3) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      Point var3;
      int var4;
      if (super.f == 0 || super.f == 9) {
         var1 = 0;

         for(var4 = 0; var4 < 8; ++var4) {
            var3 = new Point(super.x + CRes.getcos(var1) * 30 / 1000, super.y + CRes.getsin(var1) * 25 / 1000);
            this.VecEff.addElement(var3);
            var1 += 45;
         }
      }

      if (super.f == 3 || super.f == 12) {
         var1 = 0;

         for(var4 = 0; var4 < 12; ++var4) {
            var3 = new Point(super.x + CRes.getcos(var1) * 40 / 1000, super.y + CRes.getsin(var1) * 30 / 1000);
            this.VecEff.addElement(var3);
            var1 += 30;
         }
      }

      if (super.f == 15) {
         var4 = 0;

         for(int var5 = 0; var5 < 16; ++var5) {
            Point var7 = new Point(super.x + CRes.getcos(var4) * 55 / 1000, super.y + CRes.getsin(var4) * 35 / 1000);
            this.VecEff.addElement(var7);
            var4 += 22;
         }

         (var3 = new Point(super.x, super.y)).frame = 0;
         var3.fRe = 4;
         this.VecSubEff.addElement(var3);
      }

      if (super.f == 18) {
         (var2 = new Point(super.x, super.y)).frame = 1;
         var2.fRe = 4;
         this.VecSubEff.addElement(var2);
      }

      if (super.f == 22) {
         for(var4 = 0; var4 < super.vecObjsBeFire.size(); ++var4) {
            Object_Effect_Skill var6;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var4)) != null) {
               MainObject var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem);
               this.setAva(2, var8);
            }
         }

         GameScreen.addEffectEnd((short)121, 0, super.x, super.y, super.Dir, super.objMainEff);
      }

   }

   private void update_Enel_3() {
      if (super.f == 4) {
         super.vx = super.am_duong << 3;

         for(int var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               this.setAva(2, var3);
               GameScreen.addEffectEnd((short)42, 0, var3.x, var3.y, super.Dir, var3);
            }
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Satori_1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         var2.frame = 2 + var2.AG % 2;
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)122, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == 8) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            MainObject var3;
            Object_Effect_Skill var6;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var4;
               (var4 = new Point_Focus()).x = super.x;
               var4.y = super.y;
               int var7 = var3.x - var4.x;
               int var5 = var3.y - var3.hOne / 2 - var4.y;
               (var4 = this.create_Speed(var7, var5, var4)).AR = var3;
               var4.frame = 1;
               if (var4.fRe < 3) {
                  var4.fRe = 3;
               }

               this.VecEff.addElement(var4);
            }

            ++this.CI;
         }

         for(var1 = this.CI; var1 < 5; ++var1) {
            (var2 = new Point_Focus()).x = super.x;
            var2.y = super.y;
            int var8 = 110 * super.am_duong + CRes.random_Am_0(50);
            int var9 = CRes.random_Am_0(40);
            (var2 = this.create_Speed(var8, var9, var2)).AR = null;
            var2.frame = 1;
            if (var2.fRe < 3) {
               var2.fRe = 3;
            }

            this.VecEff.addElement(var2);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Satori_2() {
      if (super.f >= 4 && (super.f < 9 || super.f >= 13)) {
         super.objFireMain.isTanHinh = false;
      } else {
         super.objFireMain.isTanHinh = true;
      }

      if (super.f == 2 && !this.checkNullObject((int)3)) {
         this.x1000 = super.objFireMain.x;
         this.y1000 = super.objFireMain.y;
         super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
         super.objFireMain.y = this.objBeFireMain.y;
         super.x = super.objFireMain.x;
         super.y = super.objFireMain.y;
      }

      if (super.f == 11 && !this.checkNullObject((int)3)) {
         super.objFireMain.x = this.x1000;
         super.objFireMain.y = this.y1000;
         super.x = super.objFireMain.x;
         super.y = super.objFireMain.y;
      }

      if (super.f == 7) {
         GameScreen.addEffectEnd((short)123, 1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - 10 + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         this.setAva(2, this.objBeFireMain);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Ohm_1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG == var2.fRe && var2.AR != null) {
            GameScreen.addEffectEnd((short)123, 3, var2.x, var2.y, super.Dir, super.objMainEff);
            this.setAva(2, var2.AR);
         }

         if (var2.AG >= var2.fRe + 3) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f >= 10 && super.f % 2 == 0 && super.f < 20) {
         if (this.CI < super.vecObjsBeFire.size()) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var6;
               if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null) {
                  MainObject var3;
                  if ((var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
                     (var2 = new Point_Focus()).x = super.x;
                     var2.y = super.y;
                     int var4 = var3.x - var2.x;
                     int var5 = var3.y - var3.hOne / 2 - var2.y;
                     (var2 = this.create_Speed(var4, var5, var2)).AR = var3;
                     var2.frame = CRes.random(4);
                     this.VecEff.addElement(var2);
                  }

                  ++this.CI;
               }
            }
         } else {
            Point_Focus var7;
            (var7 = new Point_Focus()).x = super.x;
            var7.y = super.y;
            int var8 = 150 * super.am_duong + CRes.random_Am_0(50);
            int var9 = CRes.random_Am_0(40);
            (var7 = this.create_Speed(var8, var9, var7)).AR = null;
            var7.frame = CRes.random(4);
            var7.fRe += 5;
            this.VecEff.addElement(var7);
         }
      }

      if (super.f == 20) {
         super.objFireMain.NF = true;
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Ohm_2() {
      int var1;
      Point_Focus var2;
      int var3;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG % 2 == 1) {
            GameScreen.addEffectEnd((short)66, 0, var2.x, var2.y, super.Dir, super.objMainEff);
         }

         if (var2.AG == var2.fRe) {
            if ((var3 = GameCanvas.loadmap.AA(var2.x, var2.y)) == 0 || var3 == 2) {
               GameScreen.addEffectEnd((short)124, 0, var2.x, var2.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)125, 0, var2.x, var2.y + 8, super.Dir, super.objMainEff);
            }

            if (var2.AR != null) {
               this.setAva(2, var2.AR);
            }

            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == 10) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var6;
            MainObject var8;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var4;
               (var4 = new Point_Focus()).x = super.x;
               var4.y = super.y;
               int var7 = var8.x - var4.x;
               int var5 = var8.y - var8.hOne / 2 - var4.y;
               (var4 = this.create_Speed(var7, var5, var4)).AR = var8;
               var4.frame = 1;
               if (var4.fRe < 3) {
                  var4.fRe = 3;
               }

               this.VecEff.addElement(var4);
            }

            ++this.CI;
         }

         for(var1 = this.CI; var1 < 5; ++var1) {
            (var2 = new Point_Focus()).x = super.x;
            var2.y = super.y;
            var3 = CRes.random_Am(60, 140);
            int var9 = CRes.random_Am_0(60);
            (var2 = this.create_Speed(var3, var9, var2)).AR = null;
            var2.frame = 1;
            if (var2.fRe < 3) {
               var2.fRe = 3;
            }

            this.VecEff.addElement(var2);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Gedatsu_1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG > var2.fRe && var2.AR != null) {
            var2.x = var2.AR.x;
            var2.y = var2.AR.y - var2.AR.hOne / 2;
         }

         if (var2.AG > var2.fRe + 5) {
            GameScreen.addEffectEnd((short)123, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            this.setAva(2, var2.AR);
            this.VecEff.removeElement(var1);
            --var1;
         }

         if (var2.AG == var2.fRe) {
            if (var2.AR == null) {
               GameScreen.addEffectEnd((short)123, 0, var2.x, var2.y, super.Dir, super.objMainEff);
               this.VecEff.removeElement(var1);
               --var1;
            } else {
               var2.AI = 0;
               var2.vy = 0;
               var2.x = var2.AR.x;
               var2.y = var2.AR.y - var2.AR.hOne / 2;
            }
         }
      }

      if (super.f == 11 && !this.checkNullObject((int)1)) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            MainObject var3;
            Object_Effect_Skill var6;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var4;
               (var4 = new Point_Focus()).x = super.objFireMain.x + super.am_duong * 30;
               var4.y = super.objFireMain.y - super.objFireMain.hOne / 2 - 10;
               int var7 = var3.x - var4.x;
               int var5 = var3.y - var3.hOne / 2 - var4.y;
               (var4 = this.create_Speed(var7, var5, var4)).AR = var3;
               var4.frame = 1;
               this.VecEff.addElement(var4);
            }

            ++this.CI;
         }

         for(var1 = this.CI; var1 < 5; ++var1) {
            (var2 = new Point_Focus()).x = super.objFireMain.x + super.am_duong * 30;
            var2.y = super.objFireMain.y - super.objFireMain.hOne / 2 - 10;
            int var8 = CRes.random_Am(60, 140);
            int var9 = CRes.random_Am_0(60);
            (var2 = this.create_Speed(var8, var9, var2)).AR = null;
            var2.frame = 0;
            if (var2.fRe < 3) {
               var2.fRe = 3;
            }

            this.VecEff.addElement(var2);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Gedatsu_2() {
      if (super.f < 4) {
         super.objFireMain.isTanHinh = true;
      } else {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 2 && !this.checkNullObject((int)3)) {
         this.x1000 = super.objFireMain.x;
         this.y1000 = super.objFireMain.y;
         if (!this.checkNullObject((int)2)) {
            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            super.objFireMain.y = this.objBeFireMain.y;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y;
         }

         GameScreen.addEffectEnd((short)30, 0, super.x, super.y - super.objFireMain.hOne / 2 - 10, 140, super.Dir, super.objMainEff);
      }

      if (super.f == 14) {
         GameScreen.addEffectEnd((short)123, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - 15 + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         this.setAva(2, this.objBeFireMain);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Shura_1() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f > 10) {
            var2.vy -= 2;
         } else {
            --var2.vy;
         }

         if (var2.f == 10 && !this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)123, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.setAva(2, this.objBeFireMain);
         }

         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 10) {
         Point var4 = new Point();
         int var5 = super.toX;
         int var3 = super.toY;
         if (!this.checkNullObject((int)2)) {
            var5 = this.objBeFireMain.x;
            var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
         }

         var4.x = var5 - super.am_duong * 240;
         var4.vx = 24 * super.am_duong;
         var4.y = var3 - 55;
         var4.vy = 9;
         var4.dis = super.Dir;
         var4.fRe = 20;
         this.VecEff.addElement(var4);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Shura_2() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == 10) {
         super.x += super.am_duong * 20;
         if (super.Dir == 0) {
            super.x -= 30;
         }

         super.vMax = 5;
         super.vx = super.am_duong * super.vMax;
      }

      if (super.f > 10) {
         var1 = 360 - super.f % 12 * 30;
         int var3 = 26 + super.f / 4 * 3;
         this.x1000 = CRes.getcos(CRes.fixangle(var1)) * ((var3 << 1) / 3);
         this.y1000 = CRes.getsin(CRes.fixangle(var1)) * var3;
         if (super.f < super.fRemove) {
            (var2 = new Point(super.x + this.x1000 / 1000, super.y + this.y1000 / 1000)).fRe = 12;
            this.VecEff.addElement(var2);
         }
      }

      if (super.f == 20) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var4;
            MainObject var5;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               this.setAva(1, var5);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Linh_Troi() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f == this.fPlayFrameSuper) {
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
      } else if (super.f < this.fPlayFrameSuper) {
         Point var3;
         (var3 = new Point()).x = super.x;
         var3.y = super.y;
         var3.fRe = 6;
         this.VecEff.addElement(var3);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Tru_1() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f < super.fRemove) {
         Point var3;
         (var3 = new Point(super.x, super.y)).fRe = 5;
         this.VecEff.addElement(var3);
      }

      if (super.f == super.fRemove) {
         GameScreen.addEffectEnd((short)25, 4, super.toX, super.toY, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Lucci_1() {
      if (super.f == 2 || super.f == 4 || super.f == 6) {
         super.x = this.x1000 - super.am_duong * 24;
      }

      if (super.f >= 7 && super.vx <= 20) {
         super.vx += super.am_duong << 1;
      }

      if (super.f == 6 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
         if (super.frame == 1) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      if (super.f == 8) {
         this.setAva(2, this.objBeFireMain);
         GameScreen.addEffectEnd((short)132, (byte)super.frame, super.x + super.am_duong * 10, super.objFireMain.y - super.objFireMain.hOne / 2 - 5, 0, super.Dir, super.objMainEff);
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (super.frame == 0 && var2.f == 2) {
            var2.frame = 0;
         }

         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f < super.fRemove) {
         if (super.frame == 1) {
            for(var1 = 0; var1 <= super.mframe[super.f]; ++var1) {
               label90: {
                  (var2 = new Point()).x = super.x;
                  var2.y = super.y - super.mframe[super.f] * 10 + var1 * 20;
                  if (super.mframe[super.f] >= 2) {
                     if (super.mframe[super.f] == 2) {
                        if (var1 == 1) {
                           var2.fRe = 4;
                        } else {
                           var2.fRe = 2;
                        }
                        break label90;
                     }

                     if (super.mframe[super.f] == 3) {
                        if (var1 != 1 && var1 != 2) {
                           var2.fRe = 2;
                           break label90;
                        }

                        var2.fRe = 4;
                        break label90;
                     }

                     if (var1 == 2) {
                        var2.fRe = 6;
                        break label90;
                     }

                     if (var1 == 1 || var1 == 3) {
                        var2.fRe = 4;
                        break label90;
                     }
                  }

                  var2.fRe = 2;
               }

               this.VecEff.addElement(var2);
            }
         } else if (super.mframe[super.f] == 2) {
            Point var3;
            (var3 = new Point()).x = super.x;
            var3.y = super.y;
            var3.frame = 1;
            var3.fRe = 4;
            this.VecEff.addElement(var3);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Dong_Dat_1() {
      if (super.f >= 2 && super.f <= 22) {
         this.addVir(2, 5, 12, true);
      }

      int var2;
      if (super.f == 15) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         int var1 = super.objFireMain.x - GameScreen.player.x;
         var2 = (super.objFireMain.y - GameScreen.player.y) / 2;
         super.x = MotherCanvas.hw + 30 + CRes.random_Am_0(10) + var1;
         super.y = MotherCanvas.hh + CRes.random_Am_0(10) + var2;
         this.x1000 = super.x - 90 + CRes.random_Am_0(10);
         this.y1000 = super.y + CRes.random_Am_0(10);
      }

      if (super.f == 22) {
         GameScreen.addEffectEnd((short)133, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objMainEff);
         if (super.typeEffect == 243) {
            GameScreen.addEffectEnd((short)113, 2, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objMainEff);
         }

         MainObject var7 = super.objFireMain;
         var7.y += 3;
      }

      byte var4 = 0;
      if (super.f >= 22 && super.f % 2 == 0 && super.f <= 32) {
         if (this.CI < super.vecObjsBeFire.size()) {
            for(var2 = 0; var2 < super.vecObjsBeFire.size(); ++var2) {
               Object_Effect_Skill var3 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
               if (super.typeEffect == 243 && CRes.random(2) == 0) {
                  var4 = 1;
               }

               MainObject var5;
               if (var3 != null && (var5 = MainObject.get_Object((int)var3.ID, (byte)var3.tem)) != null) {
                  this.setAva(2, var5);
                  GameScreen.addEffectEnd((short)134, var4, var5.x, var5.y, super.Dir, super.objMainEff);
               }

               ++this.CI;
            }
         } else {
            if (super.typeEffect == 243 && CRes.random(2) == 0) {
               var4 = 1;
            }

            var2 = super.objFireMain.x + CRes.random_Am(110, 140);
            int var6 = super.objFireMain.y + CRes.random_Am(10, 40);
            GameScreen.addEffectEnd((short)134, var4, var2, var6, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateNamThach_1() {
      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f == 1) {
            if (var2.dis == 0) {
               int var3;
               if ((var3 = GameCanvas.loadmap.AA(var2.x / 1000, var2.y / 1000)) != 0 && var3 != 2) {
                  var2.AW = true;
               } else {
                  if (var2.frame == 0) {
                     GameScreen.addEffectEnd((short)63, 0, var2.x / 1000, var2.y / 1000, super.Dir, super.objMainEff);
                  }

                  if (var2.frame == 1) {
                     GameScreen.addEffectEnd((short)63, 3, var2.x / 1000, var2.y / 1000, super.Dir, super.objMainEff);
                  }
               }
            }

            if (CRes.random(6) == 0) {
               GameScreen.addEffectEnd((short)110, var2.frame, var2.x / 1000, var2.y / 1000, super.Dir, super.objMainEff);
            }
         }

         if (var2.f / 2 >= super.fraImgEff.nFrame || var2.AW) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      Point var4;
      float var5;
      if (super.f < 7) {
         var1 = 0;
         if (super.typeEffect == 239) {
            var1 = super.f * 10;
         }

         (var2 = new Point()).x = super.x * 1000 + CRes.getcos(var1 + 35) * (6 - super.f) * super.vMax;
         var2.y = super.y * 1000 + CRes.getsin(var1 + 35) * (6 - super.f) * (super.vMax - 4);
         var2.dis = super.f % 2;
         var2.fSmall = 0;
         this.VecEff.addElement(var2);
         (var4 = new Point()).x = super.x * 1000 + CRes.getcos(var1 + 145) * (6 - super.f) * super.vMax;
         var4.y = super.y * 1000 + CRes.getsin(var1 + 145) * (6 - super.f) * (super.vMax - 4);
         var4.dis = super.f % 2;
         var4.fSmall = 1;
         this.VecEff.addElement(var4);
         (var4 = new Point()).x = super.x * 1000 + CRes.getcos(var1 + 215) * (6 - super.f) * super.vMax;
         var4.y = super.y * 1000 + CRes.getsin(var1 + 215) * (6 - super.f) * (super.vMax - 4);
         var4.dis = super.f % 2;
         var4.fSmall = 2;
         this.VecEff.addElement(var4);
         (var4 = new Point()).x = super.x * 1000 + CRes.getcos(CRes.fixangle(var1 + 325)) * (6 - super.f) * super.vMax;
         var4.y = super.y * 1000 + CRes.getsin(CRes.fixangle(var1 + 325)) * (6 - super.f) * (super.vMax - 4);
         var4.dis = super.f % 2;
         var4.fSmall = 3;
         this.VecEff.addElement(var4);
         if (var2.f % 2 == 1 && ((var1 = GameCanvas.loadmap.AA(var2.x / 10, var2.y / 10)) == 0 || var1 == 2)) {
            GameScreen.addEffectEnd((short)63, 0, var2.x / 10, var2.y / 10, super.Dir, super.objMainEff);
         }

         if (super.f % 4 == 2 && this.isAddSound) {
            var5 = mSound.volumeSound;
            mSound.playSound();
         }
      } else if (super.f < 20) {
         if (super.f == 7 && !this.checkNullObject((int)2)) {
            this.setAva(2, this.objBeFireMain);
            if (this.isAddSound) {
               var5 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         GameScreen.addEffectEnd((short)108, 7, super.x, super.y - CRes.random(240), super.Dir, super.objMainEff);
         if (CRes.random(3) == 0) {
            GameScreen.addEffectEnd((short)110, 1, super.x, super.y, super.Dir, super.objMainEff);
         }

         this.y1000 += 60;
         if (this.y1000 > 480) {
            this.y1000 = 480;
         }

         if (super.f % 2 == 1) {
            var1 = 0;
            if (super.typeEffect == 239) {
               var1 = (super.f - 7) / 2 * 5;
            }

            ++this.DH;
            (var2 = new Point()).x = super.x * 1000 + CRes.getcos(CRes.fixangle(var1 + 0)) * ((super.f - 5) / 2) * super.vMax;
            var2.y = super.y * 1000 + CRes.getsin(CRes.fixangle(var1 + 0)) * ((super.f - 5) / 2) * (super.vMax - 4);
            var2.frame = 1;
            var2.dis = this.DH % 2;
            var2.fSmall = 0;
            this.VecEff.addElement(var2);
            (var4 = new Point()).x = super.x * 1000 + CRes.getcos(var1 + 90) * ((super.f - 5) / 2) * super.vMax;
            var4.y = super.y * 1000 + CRes.getsin(var1 + 90) * ((super.f - 5) / 2) * (super.vMax - 4);
            var4.frame = 1;
            var4.fSmall = 1;
            var4.dis = this.DH % 2;
            this.VecEff.addElement(var4);
            (var4 = new Point()).x = super.x * 1000 + CRes.getcos(var1 + 180) * ((super.f - 5) / 2) * super.vMax;
            var4.y = super.y * 1000 + CRes.getsin(var1 + 180) * ((super.f - 5) / 2) * (super.vMax - 4);
            var4.frame = 1;
            var4.dis = this.DH % 2;
            var4.fSmall = 2;
            this.VecEff.addElement(var4);
            (var4 = new Point()).x = super.x * 1000 + CRes.getcos(CRes.fixangle(var1 + 270)) * ((super.f - 5) / 2) * super.vMax;
            var4.y = super.y * 1000 + CRes.getsin(CRes.fixangle(var1 + 270)) * ((super.f - 5) / 2) * (super.vMax - 4);
            var4.frame = 1;
            var4.fSmall = 3;
            var4.dis = this.DH % 2;
            this.VecEff.addElement(var4);
         }
      }

      if (super.f == super.fRemove - 5) {
         this.setAva(2, this.objBeFireMain);
         GameScreen.addEffectEnd((short)112, 1, super.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         if (super.f % 4 == 0) {
            LoadMap.timeVibrateScreen = 105;
            GameScreen.addEffectEnd((short)59, 0, super.x + CRes.random_Am_0(15), super.y + 5 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
         }

      }
   }

   private void updateWapol_1() {
      Point var1;
      if (super.f < super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
            super.objFireMain.dy = 4;
         }

         if (super.f % 2 == 1) {
            (var1 = new Point(super.x, super.y)).frame = 0;
            this.VecEff.addElement(var1);
         }
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var3)).f;
         if (var2.f >= 4) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

      if (super.f == super.fRemove) {
         super.objFireMain.plashNow.AA((byte)0);
         super.vx = 0;
         super.vy = 0;
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.dy = 0;
         }

         (var1 = new Point(super.toX, super.toY - 24)).frame = 1;
         this.VecEff.addElement(var1);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateWapol_3() {
      if (super.f == 4) {
         GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 200, super.Dir, super.objMainEff);
      }

      int var4;
      if (super.f >= 9 && super.f <= super.fRemove && super.f % 3 == 0) {
         int var6;
         if (this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var2;
            if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
               Point_Focus var3 = new Point_Focus();
               var4 = var2.x - super.x;
               var6 = var2.y - var2.hOne / 2 - super.y;
               var3 = this.create_Speed(var4, var6, var3);
               this.VecEff.addElement(var3);
            }
         } else {
            Point_Focus var5 = new Point_Focus();
            var6 = 120 + CRes.random_Am_0(30);
            int var7 = CRes.random_Am_0(50);
            if (super.Dir == 0) {
               var6 = -var6;
            }

            var5 = this.create_Speed(var6, var7, var5);
            this.VecEff.addElement(var5);
         }
      }

      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var8;
         (var8 = (Point_Focus)this.VecEff.elementAt(var4)).update_Vx_Vy();
         if (var8.AG >= var8.fRe) {
            GameScreen.addEffectEnd((short)57, 0, var8.x, var8.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var8);
            --var4;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateMr3_1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)103, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            if (var2.AR != null) {
               this.setAva(2, var2.AR);
               GameScreen.addEffectEnd((short)8, 0, var2.AR.x, var2.AR.y - var2.AR.hOne / 2, super.Dir, var2.AR);
            }

            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 9 && !this.checkNullObject((int)2)) {
         if (super.vecObjsBeFire.size() > 1) {
            super.fRemove = 25;

            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               MainObject var3;
               Object_Effect_Skill var6;
               if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
                  var2 = new Point_Focus();
                  int var4 = var3.x - super.x;
                  int var5 = var3.y - super.y;
                  (var2 = this.create_Speed(var4, var5, var2, super.x, super.y, var3.x, var3.y)).dis = super.Dir;
                  var2.AR = var3;
                  this.VecEff.addElement(var2);
               }
            }
         } else {
            if (super.Dir == 0) {
               super.toX = this.objBeFireMain.x + 10;
            } else {
               super.toX = this.objBeFireMain.x - 10;
            }

            super.toY = this.objBeFireMain.y + 5;
            var1 = super.toX - super.x;
            int var7 = super.toY - super.y;
            Point_Focus var8 = new Point_Focus();
            (var8 = this.create_Speed(var1, var7, var8)).dis = super.Dir;
            var8.AR = this.objBeFireMain;
            this.VecEff.addElement(var8);
            super.fRemove = 15 + var8.fRe;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateMr3_2() {
      int var1;
      Point_Focus var5;
      if (super.f == 1 || super.f == 10) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               (var5 = new Point_Focus()).AR = var3;
               int var4 = var3.x - super.x;
               int var6 = var3.y - var3.hOne / 2 - super.y;
               var5 = this.create_Speed(var4, var6, var5);
               if (super.f == 1) {
                  var5.frame = 0;
               }

               if (super.f == 10) {
                  var5.frame = 1;
               }

               this.VecEff.addElement(var5);
            }
         }
      }

      if (super.f == 6 && !this.checkNullObject((int)1)) {
         super.objFireMain.AA(T.PE, true);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var5 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var5.AG >= var5.fRe) {
            Point var8;
            if (var5.frame == 0) {
               (var8 = new Point()).frame = var5.frame;
               var8.fRe = (var5.fRe << 1) + 10;
               var8.AZ = var5.AR;
               this.VecSubEff.addElement(var8);
            } else {
               (var8 = new Point()).frame = var5.frame;
               var8.fRe = CRes.random(12, 20);
               var8.AZ = var5.AR;
               this.VecSubEff.addElement(var8);
            }

            this.VecEff.removeElement(var5);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var7;
         (var7 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var7.f >= var7.fRe) {
            this.VecSubEff.removeElement(var7);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateMissMS_1() {
      if (super.f >= 30) {
         if (!this.checkNullObject((int)1) && super.f == 30) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.NH = false;
            super.objFireMain.dy = -20;
         }

         if (!this.checkNullObject((int)1) && super.f == 31) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.NH = false;
            super.objFireMain.dy = -10;
         }

         if (!this.checkNullObject((int)1) && super.f == 32) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.NH = true;
            super.objFireMain.dy = 10;
         }

         if (!this.checkNullObject((int)1) && super.f == 33) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.NH = true;
            super.objFireMain.dy = 20;
         }
      } else if (super.f >= 2 && super.f < 30 && !this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = true;
      }

      if (super.f >= 8 && super.f % 5 == 0 && this.CI < super.vecObjsBeFire.size()) {
         this.addSound((byte)15);
         Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
         ++this.CI;
         MainObject var2;
         if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
            Point var3;
            (var3 = new Point()).x = var2.x;
            var3.y = var2.y;
            var3.fRe = 12;
            this.VecEff.addElement(var3);
            (var3 = new Point()).x = var2.x;
            var3.y = var2.y + 10;
            var3.fRe = 20;
            this.VecSubEff.addElement(var3);
            this.setAva(2, var2);
         }

         this.addVir(3, 5, 10, false);
      }

      int var4;
      Point var5;
      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         (var5 = (Point)this.VecEff.elementAt(var4)).update();
         if (var5.f >= var5.fRe) {
            this.VecEff.removeElement(var5);
            --var4;
         }
      }

      for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
         (var5 = (Point)this.VecSubEff.elementAt(var4)).update();
         if (var5.f < 3) {
            var5.frame = var5.f;
         }

         if (var5.f > var5.fRe - 3) {
            var5.frame = var5.fRe - var5.f;
         }

         if (var5.f >= var5.fRe) {
            this.VecSubEff.removeElement(var5);
            --var4;
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void updateHoDen() {
      int var1;
      if (GameCanvas.gameTick % 20 == 0) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null) {
               GameScreen.AA((short)-1, super.objFireMain, var2, super.x + this.CW[CRes.random(this.CW.length - 1)], super.y - 200 + CRes.random_Am(-10, 10));
               GameScreen.AA((short)-1, super.objFireMain, var2, super.x + this.CW[CRes.random(this.CW.length - 1)], super.y - 200 + CRes.random_Am(-10, 10));
            }
         }
      }

      if (super.f == 16 && this.DI <= 1) {
         ++this.DI;
         GameScreen.addEffectEnd((short)164, 0, super.x, super.y, super.Dir, super.objMainEff);
      }

      Point var3;
      if (super.f == 10 || super.f == 16) {
         for(var1 = 0; var1 < 4; ++var1) {
            var3 = new Point();
            if (var1 == 0) {
               var3.x = (super.x - 80) * 10;
               var3.y = super.y * 10;
               var3.vx = CRes.random(30, 50);
               var3.vy = CRes.random(30, 50);
            } else if (var1 == 1) {
               var3.x = super.x * 10;
               var3.y = (super.y - 40) * 10;
               var3.vx = -CRes.random(40, 60);
               var3.vy = CRes.random(20, 40);
            } else if (var1 == 2) {
               var3.x = super.x * 10;
               var3.y = (super.y + 40) * 10;
               var3.vx = CRes.random(40, 60);
               var3.vy = -CRes.random(25, 45);
            } else if (var1 == 3) {
               var3.x = (super.x + 80) * 10;
               var3.y = super.y * 10;
               var3.vx = -CRes.random(30, 50);
               var3.vy = -CRes.random(30, 50);
            }

            if (var1 % 2 == 1 && super.f == 10 || var1 % 2 == 0 && super.f == 16) {
               var3.frame = 1;
            }

            var3.fRe = 22;
            this.VecSubEff.addElement(var3);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var3 = (Point)this.VecEff.elementAt(var1)).f;
         if (var3.f >= 6) {
            this.VecEff.removeElement(var3);
            --var1;
         }
      }

      if (super.f > 16) {
         if (this.y1000 > 0) {
            this.y1000 -= 120;
            if (this.y1000 < 0) {
               this.y1000 = 0;
            }
         }

         if (this.y1000 == 0 && super.f < super.fRemove) {
            if (CRes.random(2) == 0) {
               Point var4 = new Point(super.x + CRes.random_Am_0(20), super.y + 5 + CRes.random_Am_0(10));
               this.VecEff.addElement(var4);
            }

            if (super.f % 4 == 0) {
               LoadMap.timeVibrateScreen = 105;
            }
         }
      }

   }

   private void updateSet_1() {
      float var10000;
      int var1;
      Point var2;
      if (super.f == 10 || super.f == 16 && super.typeEffect == 237) {
         for(var1 = 0; var1 < 4; ++var1) {
            var2 = new Point();
            if (var1 == 0) {
               var2.x = (super.x - 80) * 10;
               var2.y = super.y * 10;
               var2.vx = CRes.random(30, 50);
               var2.vy = CRes.random(30, 50);
            } else if (var1 == 1) {
               var2.x = super.x * 10;
               var2.y = (super.y - 40) * 10;
               var2.vx = -CRes.random(40, 60);
               var2.vy = CRes.random(20, 40);
            } else if (var1 == 2) {
               var2.x = super.x * 10;
               var2.y = (super.y + 40) * 10;
               var2.vx = CRes.random(40, 60);
               var2.vy = -CRes.random(25, 45);
            } else if (var1 == 3) {
               var2.x = (super.x + 80) * 10;
               var2.y = super.y * 10;
               var2.vx = -CRes.random(30, 50);
               var2.vy = -CRes.random(30, 50);
            }

            if (super.typeEffect == 237 && (var1 % 2 == 1 && super.f == 10 || var1 % 2 == 0 && super.f == 16)) {
               var2.frame = 1;
            }

            var2.fRe = 22;
            this.VecSubEff.addElement(var2);
         }

         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      if (super.f == super.fRemove - 5) {
         this.setAva(2, this.objBeFireMain);
         GameScreen.addEffectEnd((short)112, 0, super.x, super.y + 10, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= super.fraImgSub2Eff.nFrame << 1) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         int var3;
         if (var2.f % 5 == 0 && ((var3 = GameCanvas.loadmap.AA(var2.x / 10, var2.y / 10)) == 0 || var3 == 2)) {
            GameScreen.addEffectEnd((short)63, 0, var2.x / 10, var2.y / 10, super.Dir, super.objMainEff);
         }

         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 16 && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f > 16) {
         if (this.y1000 > 0) {
            this.y1000 -= 120;
            if (this.y1000 < 0) {
               this.y1000 = 0;
            }
         }

         if (this.y1000 == 0 && super.f < super.fRemove) {
            GameScreen.addEffectEnd((short)108, 6, super.x, super.y - CRes.random(240), super.Dir, super.objMainEff);
            if (CRes.random(2) == 0) {
               Point var4 = new Point(super.x + CRes.random_Am_0(20), super.y + 5 + CRes.random_Am_0(10));
               this.VecEff.addElement(var4);
            }

            if (super.f % 4 == 0) {
               LoadMap.timeVibrateScreen = 105;
               GameScreen.addEffectEnd((short)110, 0, super.x + CRes.random_Am_0(15), super.y + 5 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            }
         }
      }

   }

   private void updateSet_2() {
      int var3;
      int var5;
      if (super.f >= 10 && super.f <= 20) {
         if (this.isAddSound && super.f % 3 == 0) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var2;
            if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
               var3 = 1 + CRes.random(2);

               for(var5 = 0; var5 < var3; ++var5) {
                  Point_Focus var4;
                  (var4 = new Point_Focus()).x = var2.x + 300;
                  if (super.Dir == 2) {
                     var4.x = var2.x - 300;
                  }

                  var4.y = var2.y - 400;
                  var4.AK = var2.x + CRes.random_Am_0(20);
                  var4.AL = var2.y + CRes.random_Am_0(10);
                  (var4 = this.create_Speed(var4.AK - var4.x, var4.AL - var4.y, var4, var4.x, var4.y, var4.AK, var4.AL)).dis = CRes.random(16, 30);
                  var4.AM = CRes.random(10, 25);
                  var4.frame = CRes.random(super.fraImgEff.nFrame);
                  if (var5 == 0) {
                     var4.AP = 1;
                  }

                  if (super.typeEffect == 238 && CRes.random(2) == 0) {
                     var4.AQ = 1;
                  }

                  this.VecEff.addElement(var4);
               }
            }
         } else if (!this.checkNullObject((int)2)) {
            var5 = 1 + CRes.random(4) / 3;

            for(int var6 = 0; var6 < var5; ++var6) {
               Point_Focus var9;
               (var9 = new Point_Focus()).x = this.objBeFireMain.x + 300;
               if (super.Dir == 2) {
                  var9.x = this.objBeFireMain.x - 300;
               }

               var9.y = this.objBeFireMain.y - 400;
               var9.AK = this.objBeFireMain.x + CRes.random_Am_0(160);
               var9.AL = this.objBeFireMain.y + CRes.random_Am_0(80);
               (var9 = this.create_Speed(var9.AK - var9.x, var9.AL - var9.y, var9, var9.x, var9.y, var9.AK, var9.AL)).dis = CRes.random(16, 30);
               var9.AM = CRes.random(10, 25);
               var9.frame = CRes.random(super.fraImgEff.nFrame);
               if (super.typeEffect == 238 && CRes.random(2) == 0) {
                  var9.AQ = 1;
               }

               this.VecEff.addElement(var9);
            }
         }
      }

      for(var5 = 0; var5 < this.VecEff.size(); ++var5) {
         Point_Focus var7;
         (var7 = (Point_Focus)this.VecEff.elementAt(var5)).update_Vx_Vy();
         if (var7.AG == var7.fRe) {
            var7.AI = 0;
            var7.vy = 0;
            var7.x = var7.AK;
            var7.y = var7.AL;
            if (CRes.random(3) == 0 || var7.AP == 1) {
               Point var10;
               (var10 = new Point()).x = var7.x * 10;
               var10.y = var7.y * 10;
               var10.vx = CRes.random_Am_0(30);
               var10.vy = CRes.random_Am_0(30);
               var10.fRe = 14 + CRes.random(6);
               var10.frame = var7.AQ;
               this.VecSubEff.addElement(var10);
               GameScreen.addEffectEnd((short)59, 0, var7.x, var7.y, super.Dir, super.objMainEff);
            }

            if (GameCanvas.loadmap.AA(var7.x, var7.y) == -1) {
               var7.AA = true;
            } else {
               GameScreen.addEffectEnd((short)63, 0, var7.x, var7.y, super.Dir, super.objMainEff);
            }
         }

         if (var7.AG % 2 == 0) {
            ++var7.frame;
            if (var7.frame >= super.fraImgEff.maxNumFrame) {
               var7.frame = 0;
            }
         }

         if (var7.AG >= var7.fRe + var7.dis || var7.AA) {
            this.VecEff.removeElement(var7);
            --var5;
         }
      }

      for(var5 = 0; var5 < this.VecSubEff.size(); ++var5) {
         Point var8;
         (var8 = (Point)this.VecSubEff.elementAt(var5)).update();
         if (var8.f % 8 == 0 && ((var3 = GameCanvas.loadmap.AA(var8.x / 10, var8.y / 10)) == 0 || var3 == 2)) {
            GameScreen.addEffectEnd((short)63, 0, var8.x / 10, var8.y / 10, super.Dir, super.objMainEff);
         }

         if (var8.f >= var8.fRe) {
            this.VecSubEff.removeElement(var8);
            --var5;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateZoroS2_L1_NEW() {
      float var10000;
      if (super.f == 1) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         GameScreen.addEffectEnd((short)16, 0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      if (super.f == 2) {
         GameScreen.addEffectEnd((short)26, 1, this.objBeFireMain.x, this.objBeFireMain.y, (byte)0, super.objMainEff);
      }

      if (super.f == 4) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         byte var1 = 10;
         if (super.Dir == 0) {
            var1 = -10;
         }

         GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      if (super.f == 5) {
         GameScreen.addEffectEnd((short)26, 1, this.objBeFireMain.x, this.objBeFireMain.y, (byte)2, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   public final void stopUpdateNormal() {
      this.removeEff();
   }

   public final void removeEff() {
      if (super.objFireMain == GameScreen.player && GameScreen.typePaintGameScreen == 1) {
         GameScreen.isPaintNormal();
      }

      if (!super.isEff) {
         mVector var2 = super.vecObjsBeFire;
         Effect_Skill effSkill = this;
         if (var2 != null && var2.size() != 0) {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               Object_Effect_Skill object_Effect_Skill;
               MainObject mainObject;
               if ((mainObject = MainObject.get_Object((int)(object_Effect_Skill = (Object_Effect_Skill)var2.elementAt(var3)).ID, (byte)object_Effect_Skill.tem)) != null && !mainObject.returnAction()) {
                  boolean flag = setAddEffPlus(object_Effect_Skill, mainObject, effSkill.objFireMain, effSkill.objMainEff);
                  if (mainObject.Hp <= 0 && mainObject.Action != 4) {
                     mainObject.beginDie(effSkill.objFireMain);
                  }

                  byte typeColor = 15;
                  if (!effSkill.checkNullObject((int)1) && effSkill.objFireMain == GameScreen.player) {
                     typeColor = 13;
                  }

                  int num = object_Effect_Skill.hpShow;
                  if (effSkill.objFireMain.typeObject == 1) {
                     typeColor = 14;
                     num = -num;
                  }

                  if (effSkill.objFireMain == GameScreen.player || mainObject == GameScreen.player || !GameCanvas.lowGraphic) {
                     if (object_Effect_Skill.hpShow == 0) {
                        GameScreen.addEffectNumBig_NEW_AP((int)num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, (byte)17);
                     } else {
                        if (flag) {
                           typeColor = 16;
                        }

                        GameScreen.addEffectNumBig_NEW_AP(num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, typeColor);
                     }
                     MainObject var9 = effSkill.objMainEff;
                     MainObject var12 = effSkill.objFireMain;
                     Object_Effect_Skill var10 = object_Effect_Skill;
                     int var10000;
                     if (object_Effect_Skill != null && mainObject != null && var12 != null) {
                        label107: {
                           num = 0;

                           while(num < var10.mEffTypePlus.length) {
                              switch(var10.mEffTypePlus[num]) {
                              case 1058:
                                 GameScreen.addEffectEnd((short)20, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, (byte)mainObject.Dir, var9);
                                 var10000 = num;
                                 break label107;
                              default:
                                 ++num;
                              }
                           }

                           var10000 = -1;
                        }
                     } else {
                        var10000 = -1;
                     }

                     int var11 = var10000;
                     if (var10000 >= 0 && object_Effect_Skill.AG[var11] > 0) {
                        GameScreen.addEffectNumBig_NEW_AP((int)object_Effect_Skill.AG[var11], object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, (byte)25);
                     }
                  }
               }
            }

            if (effSkill.objFireMain != GameScreen.player && effSkill.objFireMain.Hp <= 0 && effSkill.objFireMain.Action != 4) {
               effSkill.objFireMain.beginDie(effSkill.objFireMain);
            }
         }
      }

      this.VecEff.removeAllElements();
      this.VecSubEff.removeAllElements();
      super.isStop = true;
      super.f = -1;
   }

   private void createNormal() {
      super.fRemove = 60;
      switch(this.subType) {
      case 0:
         super.fraImgEff = new FrameImage(0, 14, 14);
      }

      label23: {
         super.vMax = 8000;
         super.numNextFrame = 2;
         MainObject var2 = super.objFireMain;
         if (var2 != null) {
            switch(var2.Dir) {
            case 0:
               super.gocT_Arc = 180;
               break label23;
            case 1:
               super.gocT_Arc = 270;
               break label23;
            case 2:
               break;
            case 3:
               super.gocT_Arc = 90;
            default:
               break label23;
            }
         }

         super.gocT_Arc = 0;
      }

      super.va = 4096;
      super.vx = 0;
      super.vy = 0;
      super.life = 0;
      super.vX1000 = super.va * CRes.getcos(super.gocT_Arc) >> 10;
      super.vY1000 = super.va * CRes.getsin(super.gocT_Arc) >> 10;
   }

   private void create_Ussop_S3_L1() {
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      super.fRemove = 20;
      super.vMax = 10;
      super.numNextFrame = 2;
      super.fraImgEff = new FrameImage(111, 40, 30, 40, 30);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 3, 300, super.Dir, super.objMainEff);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void create_Ussop_S3_L6() {
      super.y -= 6;
      super.x += 30 * super.am_duong;
      super.fRemove = 20;
      super.vMax = 10;
      super.numNextFrame = 2;
      super.fraImgEff = new FrameImage(418, 6);
      GameScreen.addEffectEnd((short)53, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 3, 300, super.Dir, super.objMainEff);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 3, 300, super.Dir, super.objMainEff);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void create_Ussop_S3_L7() {
      super.y -= 6;
      super.x += 30 * super.am_duong;
      super.fRemove = 20;
      super.vMax = 10;
      super.numNextFrame = 2;
      super.fraImgEff = new FrameImage(418, 6);
      super.fraImgSubEff = new FrameImage(456, 10);
      GameScreen.addEffectEnd((short)53, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 3, 300, super.Dir, super.objMainEff);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 3, 300, super.Dir, super.objMainEff);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createLuffy1() {
      super.fRemove = super.vecObjsBeFire.size() * 3 + 6;
      if (super.fRemove < 12) {
         super.fRemove = 12;
      }

      super.fraImgEff = new FrameImage(1, 80, 40);
      if (super.typeEffect == 37) {
         super.fraImgSubEff = new FrameImage(27, 24, 32);
      }

      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createSanji1() {
      super.y = super.objFireMain.y;
      super.fRemove = 12;
   }

   private void createZoro1() {
      super.fraImgEff = new FrameImage(10, 40, 47);
      int var1;
      if (CRes.abs(var1 = super.objFireMain.x - this.objBeFireMain.x) > 50) {
         super.fRemove = 5;
         super.vx = (CRes.abs(var1) - 24) / 5;
      } else if (CRes.abs(var1) > 24) {
         super.vx = 5;
         super.fRemove = (CRes.abs(var1) - 24) / 5;
      } else {
         super.fRemove = 1;
         super.vx = 0;
      }

      if (super.Dir == 0) {
         super.AZ = 20;
         super.vx = -super.vx;
      } else {
         super.AZ = -20;
      }
   }

   private void createZoro2() {
      super.fraImgEff = new FrameImage(10, 40, 47);
      super.fraImgSubEff = new FrameImage(11, 40, 50);
      super.fRemove = 7;
      super.BA = this.objBeFireMain.hOne / 2;
      if (super.objFireMain != null) {
         super.objFireMain.isTanHinh = true;
         if (super.objFireMain.plashNow != null) {
            super.objFireMain.plashNow.AA((byte)1);
         }
      }

      if (super.Dir == 0) {
         super.toX += 30;
      } else {
         super.toX -= 30;
      }
   }

   private void createUssopSea1() {
      super.fraImgEff = new FrameImage(12, 15, 15);
      super.vMax = 24;
      super.fRemove = 15;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
   }

   private void createUssopSea2() {
      super.fraImgEff = new FrameImage(196, 15, 15);
      super.vMax = 24;
      super.fRemove = 20;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }
   }

   private void createUssopSea3() {
      super.fraImgEff = new FrameImage(197, 15, 10);
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.vMax = 12;
      super.fRemove = 20;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 600, super.Dir, super.objMainEff);
   }

   private void createUssop2() {
      super.fraImgEff = new FrameImage(20, 10, 10);
      super.vMax = 24;
      if (super.typeEffect == 206) {
         this.setAngle();
         super.fraImgEff = new FrameImage(305, 16, 12);
         super.fraImgSubEff = new FrameImage(304, 10, 7);
         super.vMax = 16;
      } else if (super.typeEffect == 207) {
         this.setAngle();
         super.fraImgEff = new FrameImage(20, 10, 10);
         super.fraImgSubEff = new FrameImage(304, 10, 7);
         super.vMax = 16;
      }

      super.fRemove = 5;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      if (super.typeEffect == 206) {
         var1 = CRes.AA(var1, var2);
         super.frame = this.setFrameAngle(var1);
      }

      GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
      this.fPlayFrameSuper = super.fRemove;
      if (super.fRemove < 5) {
         super.fRemove = 5;
      }

   }

   private void createUssopSkill1_Lv3() {
      super.fraImgEff = new FrameImage(53, 9, 9);
      super.fraImgSubEff = new FrameImage(20, 10, 10);
      super.vMax = 24;
      super.fRemove = 5;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      Point_Focus var3 = new Point_Focus();
      (var3 = this.create_Speed(var1, var2, var3)).frame = 1;
      GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
      this.VecEff.addElement(var3);
   }

   private void createNami1() {
      super.fraImgEff = new FrameImage(22, 70, 50);
      super.fraImgSubEff = new FrameImage(298, 24, 24, 6);
      super.fRemove = 10;
      if (super.typeEffect == 53 || super.typeEffect == 163) {
         super.fraImgSub2Eff = new FrameImage(27, 24, 24);
      }

      super.BE = super.objFireMain.MW;
      super.vMax = 12;
      super.y += 5;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      Point_Focus var3 = new Point_Focus();
      (var3 = this.create_Speed(var1, var2, var3)).frame = 0;
      this.VecEff.addElement(var3);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createNami1_SHORT() {
      super.fraImgEff = new FrameImage(22, 70, 50);
      super.fraImgSubEff = new FrameImage(298, 24, 24, 6);
      if (super.typeEffect == 190 || super.typeEffect == 222 || super.typeEffect == 312) {
         super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
         if (super.typeEffect == 222 || super.typeEffect == 312) {
            super.fraImgEff = new FrameImage(324, 70, 50);
            super.fraImgSub3Eff = new FrameImage(326, 26, 26, 3);
         }
      }

      super.BE = super.objFireMain.MW;
      super.fRemove = 24;
      super.fraImgSub2Eff = new FrameImage(27, 24, 24);
      super.vMax = 12;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      byte var1;
      if (super.Dir == 0) {
         var1 = 15;
      } else {
         var1 = -15;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, 500, super.Dir, super.objMainEff);
   }

   private void create_Nami_S2_L7() {
      super.fraImgEff = new FrameImage(324, 70, 50);
      super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
      super.fraImgSub3Eff = new FrameImage(326, 26, 26, 3);
      super.BE = super.objFireMain.MW;
      super.fRemove = 24;
      super.fraImgSub2Eff = new FrameImage(27, 24, 24);
      super.vMax = 12;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      byte var1;
      if (super.Dir == 0) {
         var1 = 15;
      } else {
         var1 = -15;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, 500, super.Dir, super.objMainEff);
   }

   private void createNamiSea1_2() {
      super.BA = super.y;
      super.y += super.objFireMain.hOne / 2;
      super.vMax = 12;
      super.fraImgEff = new FrameImage(28, 46, 50, 46, 50);
      super.fraImgSubEff = new FrameImage(29, 28, 30, 28, 30);
      super.fraImgSub2Eff = new FrameImage(298, 24, 24, 6);
      super.BE = super.objFireMain.MW;
      if (super.Dir == 0) {
         super.AZ = super.x - 20;
      } else {
         super.AZ = super.x + 20;
      }

      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      if (super.typeEffect == 139) {
         super.fraImgSub3Eff = new FrameImage(27, 24, 24);
         GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 500, super.Dir, super.objMainEff);
      } else {
         super.fraImgSub3Eff = new FrameImage(13, 24, 24);
         GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createNamiSea3() {
      super.BA = super.y;
      super.y += super.objFireMain.hOne / 2;
      super.vMax = 12;
      super.fraImgSub2Eff = new FrameImage(298, 24, 24, 6);
      super.BE = super.objFireMain.MW;
      if (super.Dir == 0) {
         super.AZ = super.x - 20;
      } else {
         super.AZ = super.x + 20;
      }

      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      super.fraImgSub3Eff = new FrameImage(27, 24, 24);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, 500, super.Dir, super.objMainEff);
      this.CO = new FrameImage[3];
      this.CO[0] = new FrameImage(25, 80, 40, 60, 30);
      this.CO[1] = new FrameImage(24, 15, 60);
      this.CO[2] = new FrameImage(81, 24, 24);
   }

   private void createSanji2() {
      super.numNextFrame = 2;
      super.vMax = 16;
      super.fRemove = 16;
      super.fraImgEff = new FrameImage(31, 70, 70);
      super.Dir = (byte)super.objFireMain.type_left_right;
      short var1 = 300;
      if (super.typeEffect == 12) {
         super.fraImgSubEff = new FrameImage(77, 64, 75, 43, 50);
         super.fraImgSub2Eff = new FrameImage(224, 22, 28);
         super.fraImgSub3Eff = new FrameImage(78, 22, 28);
         super.fRemove = 24;
         var1 = 600;
      } else if (super.typeEffect != 188 && super.typeEffect != 220 && super.typeEffect != 293) {
         if (super.typeEffect == 49) {
            super.fraImgSubEff = new FrameImage(78, 22, 28);
            super.fraImgSub2Eff = new FrameImage(102, 35, 19);
         } else if (super.typeEffect == 50) {
            super.fraImgSub2Eff = new FrameImage(103, 35, 19, 35, 19);
            super.fraImgSubEff = new FrameImage(78, 22, 28);
         }
      } else {
         super.fraImgSubEff = new FrameImage(282, 64, 75);
         if (super.typeEffect == 293) {
            super.fraImgSub2Eff = new FrameImage(406, 42, 34);
            super.BP = new FrameImage(283, 22, 28);
            super.fraImgSubEff = new FrameImage(412, 64, 75);
         } else if (super.typeEffect == 220) {
            super.fraImgSub2Eff = new FrameImage(325, 32, 31);
            super.BP = new FrameImage(224, 22, 28);
         } else {
            super.fraImgSub2Eff = new FrameImage(224, 22, 28);
         }

         super.fraImgSub3Eff = new FrameImage(283, 22, 28);
         super.fraImgEff = new FrameImage(284, 70, 70);
         super.fRemove = 24;
         var1 = 600;
      }

      this.x1000 = super.x;
      this.y1000 = super.objFireMain.y;
      if (super.Dir == 0) {
         super.x -= 16;
      } else {
         super.x += 16;
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - (super.objFireMain.hOne / 3 << 1), var1, super.Dir, super.objMainEff);
   }

   private void create_Sanji_S3_L7() {
      super.numNextFrame = 2;
      super.vMax = 16;
      super.fraImgEff = new FrameImage(31, 70, 70);
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.fraImgEff = new FrameImage(284, 70, 70);
      super.fraImgSubEff = new FrameImage(470, 12);
      super.fraImgSub2Eff = new FrameImage(406, 42, 34);
      super.fraImgSub3Eff = new FrameImage(283, 22, 28);
      super.BP = new FrameImage(283, 22, 28);
      super.fRemove = 24;
      this.x1000 = super.x;
      this.y1000 = super.objFireMain.y;
      if (super.Dir == 0) {
         super.x -= 16;
      } else {
         super.x += 16;
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - (super.objFireMain.hOne / 3 << 1), 600, super.Dir, super.objMainEff);
   }

   private void createRankyaku() {
      super.vMax = 16;
      super.fRemove = 22;
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.fraImgEff = new FrameImage(428, 1);
      this.x1000 = super.x + 30 * super.am_duong;
      int var1 = this.x1000 - super.x;
      this.VecEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y, super.toX, super.toY));
      this.VecEff.addElement(this.create_Speed(var1, -7, new Point_Focus(), super.x, super.y, super.toX, super.toY));
      this.VecEff.addElement(this.create_Speed(var1, 7, new Point_Focus(), super.x, super.y, super.toX, super.toY));
   }

   private void createSoi() {
      super.vMax = 12;
      super.fRemove = 20;
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.fraImgEff = new FrameImage(429, 4);
      if (super.typeEffect == 277) {
         super.fraImgEff = new FrameImage(430, 4);
      }

   }

   private void createShigan() {
      super.vMax = 17;
      super.fRemove = 14;
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.fraImgEff = new FrameImage(75, 1);
      this.x1000 = super.x + 30 * super.am_duong;
      int var1 = this.x1000 - super.x;
      this.VecEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y, super.toX, super.toY));
      GameScreen.addEffectEnd((short)30, 0, super.x + 15 * super.am_duong, super.objFireMain.y - (super.objFireMain.hOne / 3 << 1), 200, super.Dir, super.objMainEff);
   }

   private void createDoor() {
      super.fRemove = 26;
      super.fraImgEff = new FrameImage(426, 2);
      super.levelPaint = -1;
   }

   private void createHuou() {
      super.fRemove = 20;
      super.fraImgEff = new FrameImage(176, 3, 25, 1);
      super.fraImgSubEff = new FrameImage(220, 9, 9, 4);
      this.DB = 30;
      if (super.typeEffect == 279) {
         this.DB = 60;
      }

      if (GameCanvas.isLowGraOrWP_PvP()) {
         this.DB = 10;
      }

      for(int var1 = 0; var1 < this.DB; ++var1) {
         Point var2 = new Point();
         this.createPointHuou(var2);
         var2.vy = 20;
         this.VecEff.addElement(var2);
      }
   }

   private Point createPointHuou(Point p) {
      p.frame = CRes.random(5);
      if (super.typeEffect == 279) {
         p.x = CRes.random_Am_0(60);
         p.y = -10 - CRes.random(60);
         p.dis = CRes.random(6);
      } else {
         p.x = CRes.random_Am_0(40);
         p.y = -10 - CRes.random(60);
         p.dis = 2;
      }

      return p;
   }

   private void createZoro3() {
      super.fRemove = 12;
      if (super.typeEffect == 15) {
         super.fRemove = 15;
      }

   }

   private void createZoro4() {
      super.fraImgSub2Eff = new FrameImage(71, 64, 25);
      super.fraImgEff = new FrameImage(88, 32, 70);
      super.fRemove = 20;
      super.vMax = 12;
   }

   private void createZoroSkill3_Lv1() {
      super.vMax = 12;
      super.y = super.objFireMain.y + 5;
   }

   private void createZoro8() {
      super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
      super.objFireMain.isTanHinh = true;
      if (super.objFireMain.plashNow != null) {
         super.objFireMain.plashNow.AA((byte)1);
      }

      super.x = super.objFireMain.x;
      super.y = super.objFireMain.y;
      super.toX = this.objBeFireMain.x;
      super.toY = this.objBeFireMain.y;
      super.vMax = 20;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      var1 = CRes.AA(var1, var2);
      super.toX = super.x + 90 * CRes.getcos(var1) / 1000;
      super.toY = super.y + 90 * CRes.getsin(var1) / 1000;
      var1 = super.toX - super.x;
      if ((var2 = super.toY - super.y) == 0) {
         var2 = 1;
      }

      if (var1 == 0) {
         var1 = 1;
      }

      int var5;
      if ((var5 = MainObject.AD(var1, var2) / super.vMax) == 0) {
         var5 = 1;
      }

      int var3 = var1 / var5;
      int var4 = var2 / var5;
      if (CRes.abs(var3) > CRes.abs(var1)) {
         var3 = var1;
      }

      if (CRes.abs(var4) > CRes.abs(var2)) {
         var4 = var2;
      }

      super.vx = var3;
      super.vy = var4;
      super.fRemove = var5;
      if (super.fRemove > 0) {
         super.AM = (byte)(super.fRemove / 2);
      }

   }

   private void createLuffy6() {
      if (super.objFireMain == GameScreen.player) {
         GameScreen.AB(true);
      }

      super.fRemove = 11;
      if (!this.checkNullObject((int)3)) {
         super.objFireMain.x = this.objBeFireMain.x + super.objFireMain.CN * 3 * 7;
         if (super.Dir == 2) {
            super.objFireMain.x = this.objBeFireMain.x - super.objFireMain.CN * 3 * 7;
         }

         super.objFireMain.y = this.objBeFireMain.y;
      }

      super.fraImgEff = new FrameImage(4, 20, 20);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
   }

   private void createNamiSkill1_L3() {
      super.Dir = (byte)super.objFireMain.type_left_right;

      for(int var1 = 0; var1 < 2; ++var1) {
         int var2 = 25;
         if (super.objFireMain.hOne > 1) {
            var2 = super.objFireMain.hOne / 2;
         }

         Point var3 = new Point(super.x + CRes.random_Am_0(20), super.y + CRes.random_Am_0(var2));
         this.VecEff.addElement(var3);
      }

      super.fRemove = 16;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
      super.fraImgEff = new FrameImage(273, 24, 24, 4);
      super.BE = super.objFireMain.MW;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void create_Nami_S1_L7() {
      super.Dir = (byte)super.objFireMain.type_left_right;

      for(int var1 = 0; var1 < 2; ++var1) {
         int var2 = 25;
         if (super.objFireMain.hOne > 1) {
            var2 = super.objFireMain.hOne / 2;
         }

         Point var3 = new Point(super.x + CRes.random_Am_0(20), super.y + CRes.random_Am_0(var2));
         this.VecEff.addElement(var3);
      }

      super.fRemove = 25;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      super.fraImgEff = new FrameImage(273, 24, 24, 4);
      super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
      super.fraImgSub2Eff = new FrameImage(446, 10);
      super.fraImgSub3Eff = new FrameImage(411, 3);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createNamiSkill3() {
      super.fRemove = 20;
      super.vMax = 10;
      this.x1000 = super.x;
      if (super.objFireMain.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      super.BE = super.objFireMain.MW;
      if (super.typeEffect == 31) {
         super.fraImgEff = new FrameImage(83, 14, 14);
         super.fraImgSubEff = new FrameImage(298, 24, 24, 6);
      } else if (super.typeEffect == 55 || super.typeEffect == 56 || super.typeEffect == 191 || super.typeEffect == 223 || super.typeEffect == 313) {
         super.fraImgEff = new FrameImage(81, 24, 24);
         super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
         super.fraImgSub2Eff = new FrameImage(27, 24, 24);
         if (super.typeEffect == 56 || super.typeEffect == 191 || super.typeEffect == 223 || super.typeEffect == 313) {
            GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - (super.objFireMain.hOne / 3 << 1), 1000, super.Dir, super.objMainEff);
            super.fRemove = 26;
         }
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void create_Nami_S3_L7() {
      super.vMax = 10;
      this.x1000 = super.x;
      if (super.objFireMain.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      super.BE = super.objFireMain.MW;
      super.fraImgEff = new FrameImage(81, 24, 24);
      super.fraImgSubEff = new FrameImage(299, 26, 26, 2);
      super.fraImgSub2Eff = new FrameImage(27, 24, 24);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.objFireMain.y - (super.objFireMain.hOne / 3 << 1), 1000, super.Dir, super.objMainEff);
      super.fRemove = 26;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createNamiSkill1() {
      super.fRemove = 16;
      if (super.Dir == 0) {
         super.x -= 20;
      } else {
         super.x += 20;
      }

      super.BE = super.objFireMain.MW;
      super.fraImgSubEff = new FrameImage(298, 24, 24, 6);
      if (super.typeEffect == 51) {
         super.fraImgEff = new FrameImage(299, 26, 26, 2);
      }

      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void createAlvida2() {
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      super.y -= 15;
      super.fraImgEff = new FrameImage(116, 38, 53);
      super.fraImgSubEff = new FrameImage(117, 38, 22);
      super.fRemove = 10;
      int var1 = super.x;
      if (super.Dir == 0) {
         var1 += 45;
      } else {
         var1 -= 45;
      }

      GameScreen.addEffectEnd((short)30, 0, var1, super.y - 30, 300, super.Dir, super.objMainEff);
   }

   private void createAlvida1() {
      if (super.Dir == 0) {
         super.x -= 26;
      } else {
         super.x += 26;
      }

      super.y -= 15;
      super.fraImgEff = new FrameImage(116, 38, 53);
      super.fRemove = 2;
      this.addSound((byte)2);
   }

   private void createMon_4_5() {
      if (super.Dir == 0) {
         super.x -= 14;
      } else {
         super.x += 14;
      }

      super.y -= 10;
      super.fRemove = 6;
      if (super.typeEffect == 73) {
         super.fraImgEff = new FrameImage(115, 34, 27);
      } else {
         super.fraImgEff = new FrameImage(35, 34, 27);
      }
   }

   private void createMon6() {
      if (super.Dir == 0) {
         super.x -= 14;
      } else {
         super.x += 14;
      }

      this.x1000 = super.x;
      this.y1000 = super.y - 10;
      super.vMax = 14;
      super.fraImgEff = new FrameImage(47, 41, 14);
      super.fraImgSubEff = new FrameImage(35, 34, 27);
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      super.frame = CRes.random(super.fraImgSubEff.nFrame);
   }

   private void createMon3() {
      if (super.Dir == 0) {
         super.x -= 25;
      } else {
         super.x += 25;
      }

      super.vMax = 14;
      super.fraImgEff = new FrameImage(20, 10, 10);
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
   }

   private void createMon2() {
      super.vMax = 12;
      if (super.typeEffect == 145) {
         super.fraImgEff = new FrameImage(60, 15, 15);
      } else if (super.typeEffect == 146) {
         super.fraImgEff = new FrameImage(59, 23, 23);
      } else if (super.typeEffect == 147) {
         super.fraImgEff = new FrameImage(20, 10, 10);
      } else if (super.typeEffect == 148) {
         super.fraImgEff = new FrameImage(73, 20, 20);
         super.numNextFrame = 2;
         super.vMax = 14;
      } else {
         super.fraImgEff = new FrameImage(114, 21, 14);
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      super.objFireMain.NF = false;
   }

   private void createZoro_New2() {
      super.fRemove = 44;
      super.vMax = 12;
      super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createZoro_New1() {
      super.fRemove = 50;
      super.vMax = 12;
      super.fraImgEff = new FrameImage(88, 32, 70);
      super.y = super.objFireMain.y;
   }

   private void createZoro_S1_L3_SHORT() {
      super.fRemove = 16;
      if (super.typeEffect == 183 || super.typeEffect == 215) {
         super.fRemove = 18;
      }

      super.vMax = 12;
      super.fraImgEff = new FrameImage(88, 32, 70);
      if (super.typeEffect == 215) {
         super.fraImgEff = new FrameImage(319, 32, 70);
      }

      super.y = super.objFireMain.y;
   }

   private void createZoro_S1_L6() {
      super.fRemove = 20;
      super.vMax = 18;
      super.fraImgEff = new FrameImage(422, 32, 70);
      super.y = super.objFireMain.y;
      this.x1000 = super.x + 30 * super.am_duong;
      int var10000 = this.x1000;
      var10000 = this.x1000;
      super.fraImgSub2Eff = new FrameImage(417, 3);
      int var1 = this.x1000 - super.x;
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne - 20, super.toX, super.toY));
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne - 15, super.toX, super.toY));
   }

   private void create_Zoro_S1_L7() {
      super.fRemove = 32;
      super.vMax = 18;
      super.fraImgEff = new FrameImage(467, 3);
      super.y = super.objFireMain.y;
      this.x1000 = super.x + 30 * super.am_duong;
      int var10000 = this.x1000;
      var10000 = this.x1000;
      super.fraImgSub2Eff = new FrameImage(441, 7);
      super.numNextFrame = 3;
      int var1 = this.x1000 - super.x;
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne - 20, super.toX, super.toY));
      this.VecSubEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), super.x, super.y - super.objFireMain.hOne - 15, super.toX, super.toY));
   }

   private void createLuffy_New3() {
      super.levelPaint = -1;
      super.fRemove = 30;
      if (super.typeEffect == 182) {
         super.fraImgEff = new FrameImage(276, 90, 50);
      } else if (super.typeEffect != 214 && super.typeEffect != 273) {
         super.fraImgEff = new FrameImage(1, 80, 40);
      } else {
         super.fraImgEff = new FrameImage(317, 90, 50);
         super.levelPaint = 0;
      }

      super.fraImgSubEff = new FrameImage(27, 24, 32);
      super.fraImgSub2Eff = new FrameImage(8, 40, 47, 40, 47);
      super.Dir = (byte)super.objFireMain.type_left_right;
   }

   private void create_Luffy_S3_L7() {
      super.fRemove = 30;
      super.levelPaint = 0;
      super.fraImgEff = new FrameImage(317, 90, 50);
      super.fraImgSubEff = new FrameImage(27, 24, 32);
      super.fraImgSub2Eff = new FrameImage(8, 40, 47, 40, 47);
      super.Dir = (byte)super.objFireMain.type_left_right;
   }

   private void createLuffy_New2() {
      if (super.objFireMain == GameScreen.player) {
         GameScreen.AB(true);
      }

      if (!this.checkNullObject((int)3)) {
         super.objFireMain.x = this.objBeFireMain.x + 30;
         if (super.Dir == 2) {
            super.objFireMain.x = this.objBeFireMain.x - 30;
         }

         super.objFireMain.y = this.objBeFireMain.y;
      }

      byte var1 = -15;
      if (super.Dir == 0) {
         var1 = 15;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.y, 300, super.Dir, super.objMainEff);
      super.fraImgEff = new FrameImage(4, 20, 20);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
      super.fraImgSub2Eff = new FrameImage(11, 40, 50);
      super.fRemove = 34;
   }

   private void createLuffy_New2_SHORT() {
      if (super.objFireMain == GameScreen.player) {
         GameScreen.AB(true);
      }

      if (!this.checkNullObject((int)3)) {
         super.objFireMain.x = this.objBeFireMain.x + 30;
         if (super.Dir == 2) {
            super.objFireMain.x = this.objBeFireMain.x - 30;
         }

         super.objFireMain.y = this.objBeFireMain.y;
      }

      super.fraImgEff = new FrameImage(4, 20, 20);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
      super.fraImgSub2Eff = new FrameImage(11, 40, 50);
      if (super.typeEffect == 213 || super.typeEffect == 272) {
         super.fraImgSub3Eff = new FrameImage(316, 44, 47);
      }

      super.fRemove = 24;
   }

   private void create_Luffy_S2_L7() {
      if (super.objFireMain == GameScreen.player) {
         GameScreen.AB(true);
      }

      if (!this.checkNullObject((int)3)) {
         super.objFireMain.x = this.objBeFireMain.x + 30;
         if (super.Dir == 2) {
            super.objFireMain.x = this.objBeFireMain.x - 30;
         }

         super.objFireMain.y = this.objBeFireMain.y;
      }

      super.fraImgEff = new FrameImage(4, 20, 20);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
      super.fraImgSub2Eff = new FrameImage(11, 40, 50);
      super.fraImgSub3Eff = new FrameImage(316, 44, 47);
      super.fRemove = 24;
   }

   private void createMon_1() {
      if (super.Dir == 0) {
         this.x1000 = super.x - 10;
         super.x -= 20;
      } else {
         this.x1000 = super.x + 10;
         super.x += 20;
      }

      this.y1000 = super.y - 12;
      super.fraImgEff = new FrameImage(114, 16, 13);
      super.fraImgSubEff = new FrameImage(35, 34, 27);
      super.fRemove = 6;
      super.vx = 3 * super.am_duong;
   }

   private void createMon_10() {
      super.fRemove = 5;
      super.fraImgEff = new FrameImage(120, 50, 25);
      if (super.typeEffect == 143) {
         super.fraImgEff = new FrameImage(2, 53, 29);
      }

      if (super.typeEffect == 149) {
         super.fraImgEff = new FrameImage(68, 28, 44);
      }

      super.numNextFrame = 1;
      if (super.Dir == 0) {
         super.x -= 10;
      } else {
         super.x += 10;
      }

      if (super.Dir == 0) {
         super.vx = -8;
      } else {
         super.vx = 8;
      }
   }

   private void createMon_11() {
      super.numNextFrame = 1;
      if (super.Dir == 0) {
         super.x -= 10;
      } else {
         super.x += 10;
      }

      if (super.Dir == 0) {
         super.vX1000 = -12;
      } else {
         super.vX1000 = 12;
      }

      super.vMax = 12;
      super.fraImgEff = new FrameImage(120, 50, 25);
      if (super.typeEffect == 144) {
         super.fraImgEff = new FrameImage(2, 53, 29);
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void createCausu_1() {
      super.fRemove = 26;
      if (super.typeEffect == 227) {
         super.fraImgEff = new FrameImage(317, 90, 50);
         super.fraImgSubEff = new FrameImage(334, 75, 42);
      } else {
         super.fraImgEff = new FrameImage(1, 80, 40);
         super.fraImgSubEff = new FrameImage(62, 48, 34);
      }

      if (!this.checkNullObject((int)3)) {
         super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 48;
         super.objFireMain.y = this.objBeFireMain.y;
         super.x = super.objFireMain.x;
         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
      }

      for(int var1 = 0; var1 < 2; ++var1) {
         Point var2;
         (var2 = new Point()).x = super.x + super.am_duong * 15;
         var2.y = super.y;
         var2.vx = super.am_duong * (5 + CRes.random(2));
         var2.vy = CRes.random_Am_0(2);
         var2.fRe = 6 + CRes.random(3);
         var2.dis = CRes.random(3) == 0 ? 0 : 1;
         this.VecEff.addElement(var2);
      }

   }

   private void createMorgan_2() {
      byte var1 = 20;
      if (super.Dir == 2) {
         var1 = -20;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.y, 300, super.Dir, super.objMainEff);
      super.fRemove = 8;
      this.addSound((byte)7);
   }

   private void createCabaji_1() {
      super.fRemove = 5;
      super.fraImgEff = new FrameImage(186, 19, 22);
      super.fraImgSubEff = new FrameImage(187, 20, 20);
      super.fraImgSub2Eff = new FrameImage(120, 50, 25);
      super.vMax = 14;
      byte var1 = -14;
      if (super.Dir == 2) {
         var1 = 14;
      }

      super.x += var1;
   }

   private void createBuggy_2() {
      super.fraImgEff = new FrameImage(125, 60, 44, 60, 44);
      super.fraImgSubEff = new FrameImage(126, 45, 45);
      super.fraImgSub2Eff = new FrameImage(3, 30, 50);
      super.fraImgSub3Eff = new FrameImage(128, 16, 16);
      super.vMax = 24;
      byte var1 = -14;
      if (super.Dir == 2) {
         var1 = 14;
      }

      this.x1000 = super.x + var1;
      this.y1000 = super.y + 14;
      super.fRemove = 49;
   }

   private void createBuggy_1() {
      super.fRemove = 5;
      byte var1 = -25;
      if (super.Dir == 2) {
         var1 = 25;
      }

      super.x += var1;
      super.fraImgEff = new FrameImage(124, 27, 22);
      super.vMax = 10;
   }

   private void createMohji_2() {
      super.fRemove = 8;
      super.fraImgEff = new FrameImage(120, 50, 25);
      byte var1 = -25;
      if (super.Dir == 2) {
         var1 = 25;
      }

      super.x += var1;
      super.y += 10;
   }

   private void createKuro_1() {
      super.fraImgEff = new FrameImage(45, 80, 25);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
      super.fRemove = 18;
      super.objFireMain.isTanHinh = true;
      super.toY = this.objBeFireMain.y;
      super.y = super.objFireMain.y;
   }

   private void createJango_1() {
      super.fRemove = 5;
      super.fraImgEff = new FrameImage(131, 20, 10);
      super.fraImgSubEff = new FrameImage(27, 12, 12);
      super.fraImgSub2Eff = new FrameImage(120, 50, 25);
      super.vMax = 14;
      byte var1 = -14;
      if (super.Dir == 2) {
         var1 = 14;
      }

      super.x += var1;
   }

   private void createNyaban_3() {
      super.fraImgEff = new FrameImage(120, 50, 25);
      super.fRemove = 27;
      this.x1000 = -15;
      if (super.Dir == 2) {
         this.x1000 = 15;
      }

      super.vx = (super.toX - (super.x + this.x1000)) / 5;
   }

   private void createNyaban_2() {
      super.fraImgEff = new FrameImage(130, 48, 39);
      super.fRemove = 12;
      super.vx = (super.toX - super.x) / 5;
   }

   private void createNyaban_1() {
      super.fRemove = 10;
      super.fraImgEff = new FrameImage(120, 50, 25);
      byte var1 = -14;
      if (super.Dir == 2) {
         var1 = 14;
      }

      super.x += var1;
   }

   private void createCabaji_2() {
      super.fraImgEff = new FrameImage(129, 40, 80);
      super.fraImgSubEff = new FrameImage(76, 32, 70);
      super.toY = this.objBeFireMain.y;
      super.fRemove = 15;
   }

   private void createKurobi_1() {
      super.fRemove = 32;
      super.fraImgEff = new FrameImage(144, 37, 55);
      this.x1000 = -30;
      if (super.Dir == 2) {
         this.x1000 = 30;
      }

      super.y -= 5;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createChu_2() {
      super.vMax = 14;
      super.fraImgEff = new FrameImage(20, 10, 10);
      super.fRemove = 40;
      super.y -= 5;
      byte var1 = 10;
      if (super.Dir == 2) {
         var1 = -10;
      }

      super.x += var1;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createChu_1() {
      super.vMax = 14;
      super.fraImgEff = new FrameImage(20, 10, 10);
      super.fRemove = 20;
      super.y -= 5;
      byte var1 = 10;
      if (super.Dir == 2) {
         var1 = -10;
      }

      super.x += var1;
   }

   private void createHachi_2() {
      super.vMax = 14;
      super.fraImgEff = new FrameImage(81, 24, 24);
      if (super.typeEffect == 150) {
         super.fraImgEff = new FrameImage(83, 14, 14);
      } else if (super.typeEffect == 151) {
         super.fraImgEff = new FrameImage(80, 30, 15);
         super.frame = 0;
      } else if (super.typeEffect == 152) {
         super.fraImgEff = new FrameImage(80, 30, 15);
         super.frame = 1;
      } else if (super.typeEffect == 153) {
         super.fraImgEff = new FrameImage(80, 30, 15);
         super.frame = 2;
      }

      super.fRemove = 24;
      super.y -= 10;
      byte var1 = 10;
      if (super.Dir == 2) {
         var1 = -10;
      }

      if (super.typeEffect == 113) {
         GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 600, super.Dir, super.objMainEff);
      } else {
         super.fRemove = 8;
         super.vMax = 16;
      }

      super.x += var1;
      this.addSound((byte)32);
   }

   private void createDonKrieg_3() {
      super.fRemove = 30;
      super.fraImgEff = new FrameImage(137, 75, 65);
      this.CL = new int[2][];
      this.CL[0] = new int[2];
      this.CL[1] = new int[2];
      this.CL[0][0] = 0;
      this.CL[0][1] = -37;
      this.CL[1][0] = -28;
      this.CL[1][1] = -28;
      byte var1 = 25;
      if (super.Dir == 2) {
         this.CL[1][0] = 28;
         var1 = -25;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createDonKrieg_1() {
      super.fraImgEff = new FrameImage(134, 30, 42);
      super.fraImgSubEff = new FrameImage(135, 20, 20);
      super.vMax = 12;
      byte var1 = 10;
      this.x1000 = 15;
      super.AZ = -10;
      if (super.Dir == 0) {
         var1 = -10;
         this.x1000 = -15;
         super.AZ = 10;
      }

      super.x += var1;
      super.y -= 5;
      super.fRemove = 22;
   }

   private void createDonKrieg_2() {
      super.fraImgEff = new FrameImage(134, 30, 42);
      super.fraImgSubEff = new FrameImage(136, 16, 12);
      super.fraImgSub2Eff = new FrameImage(131, 20, 10);
      super.vMax = 8;
      byte var1 = 10;
      this.x1000 = 15;
      super.AZ = -10;
      if (super.Dir == 0) {
         var1 = -10;
         this.x1000 = -15;
         super.AZ = 10;
      }

      super.x += var1;
      super.y -= 5;
      super.fRemove = 22;
      this.CJ = super.x;
      this.CK = super.y;
   }

   private void createGhin_2() {
      super.objFireMain.NF = false;
      super.fRemove = 30;
      super.fraImgEff = new FrameImage(133, 36, 44);
      byte var1 = 3;
      super.vx = -8;
      if (super.Dir == 2) {
         var1 = -3;
         super.vx = 8;
      }

      Point var2;
      (var2 = new Point(super.x - 15, super.y + var1)).frame = 0;
      var2.dis = 4;
      this.VecEff.addElement(var2);
      Point var3;
      (var3 = new Point(super.x + 15, super.y - var1)).frame = 1;
      var3.dis = 4;
      this.VecEff.addElement(var3);
   }

   private void createGhin_1() {
      super.fraImgEff = new FrameImage(132, 60, 35);
      byte var1 = 25;
      byte var2 = 10;
      if (super.Dir == 0) {
         var1 = -25;
      }

      if (super.typeEffect == 65 || super.typeEffect == 70) {
         super.fraImgEff = new FrameImage(215, 60, 35);
         if (super.typeEffect == 70) {
            super.vMax = 16;
            super.fraImgSubEff = new FrameImage(216, 18, 18);
         }

         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         var2 = 13;
         super.levelPaint = -1;
      }

      super.fRemove = 6;
      super.x += var1;
      super.y += var2;
   }

   private void createPearl_2() {
      super.fRemove = 34;
      super.vMax = 12;
      super.fraImgEff = new FrameImage(78, 22, 28);
      super.fraImgSubEff = new FrameImage(20, 10, 10);
      Point var1;
      (var1 = new Point(super.x - 18, super.y - 10)).frame = CRes.random(3);
      this.VecEff.addElement(var1);
      (var1 = new Point(super.x + 18, super.y - 10)).frame = CRes.random(3);
      this.VecEff.addElement(var1);
   }

   private void createPearl_1() {
      super.fRemove = 10;
      byte var1 = 15;
      if (super.Dir == 0) {
         var1 = -15;
      }

      GameScreen.addEffectEnd((short)30, 0, super.x - var1, super.y, 300, super.Dir, super.objMainEff);
      super.x += var1;
   }

   private void createKuro_2() {
      super.fraImgEff = new FrameImage(45, 80, 25);
      super.fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
      super.fRemove = 38;
      super.y = super.objFireMain.y;
      this.x1000 = super.x;
      this.y1000 = super.y;
   }

   private void createArlong_3() {
      super.fraImgEff = new FrameImage(148, 104, 85);
      super.fraImgSubEff = new FrameImage(149, 73, 73);
      super.fraImgSub2Eff = new FrameImage(150, 66, 70, 42, 45);
      super.objFireMain.isTanHinh = false;
      this.CL = new int[5][];
      this.CL[0] = new int[2];
      this.CL[1] = new int[2];
      this.CL[2] = new int[2];
      this.CL[3] = new int[2];
      this.CL[4] = new int[2];
      this.CL[0][0] = -15;
      this.CL[0][1] = -30;
      this.CL[1][0] = -30;
      this.CL[1][1] = 10;
      this.CL[2][0] = 38;
      this.CL[2][1] = -30;
      this.CL[3][0] = -30;
      this.CL[3][1] = -20;
      this.CL[4][0] = -20;
      this.CL[4][1] = 20;
      if (super.Dir == 2) {
         for(int var1 = 0; var1 < this.CL.length; ++var1) {
            this.CL[var1][0] = -this.CL[var1][0];
         }
      }

      GameScreen.addEffectEnd((short)30, 0, super.x + this.CL[2][0], super.y + this.CL[2][1], 350, super.Dir, super.objMainEff);
      super.fRemove = 20;
   }

   private void createArlong_2() {
      super.fraImgEff = new FrameImage(146, 96, 24);
      super.fraImgSubEff = new FrameImage(147, 48, 12);
      super.fraImgSub2Eff = new FrameImage(256, 80, 40);
      super.objFireMain.isTanHinh = false;
      super.fRemove = 40;
      super.vMax = 30;
   }

   private void createArlong_1() {
      super.fraImgEff = new FrameImage(145, 80, 80, 60, 60);
      super.fRemove = 12;
      super.objFireMain.isTanHinh = false;
      if (super.vecObjsBeFire.size() > 1) {
         for(int var3 = 0; var3 < super.vecObjsBeFire.size(); ++var3) {
            Object_Effect_Skill var2;
            MainObject var4;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var3)) != null && (var4 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               Point var5 = new Point(var4.x, var4.y - var4.hOne / 2);
               if (super.x < var5.x) {
                  var5.dis = 2;
               } else {
                  var5.dis = 0;
               }

               this.VecEff.addElement(var5);
            }
         }

      } else {
         byte var1 = -15;
         if (super.Dir == 2) {
            var1 = 15;
         }

         super.x += var1;
         super.y -= 10;
      }
   }

   private void createKurobi_2() {
      super.fraImgEff = new FrameImage(144, 37, 55);
      super.fRemove = 30;
      this.x1000 = -25;
      this.y1000 = -25;
      if (super.Dir == 2) {
         this.x1000 = 25;
      }

      super.vx = (super.toX - (super.x + this.x1000)) / 5;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createUrgot3() {
      super.fRemove = 40;
      super.fraImgEff = new FrameImage(179, 54, 25);

      for(int var1 = 0; var1 < 5; ++var1) {
         Point var2;
         (var2 = new Point()).y = -CRes.random(30);
         var2.vy = CRes.random_Am(3, 8);
         var2.frame = CRes.random(3);
         this.VecEff.addElement(var2);
      }

   }

   private void createXerath3() {
      super.AZ = 4;
      super.BA = 6;
      int var1 = 0;

      int var2;
      for(var2 = 1; var2 <= super.BA; ++var2) {
         var1 -= var2 * super.AZ;
      }

      super.fraImgEff = new FrameImage(83, 14, 14);
      super.fraImgSubEff = new FrameImage(51, 9, 9);
      super.fraImgSub2Eff = new FrameImage(52, 5, 5);
      super.x = super.objFireMain.x;
      super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
      this.x1000 = super.x * 1000;
      this.y1000 = super.y;
      var2 = var1 - (super.toY - super.y);
      var1 = super.BA - 1;
      if (var2 < 0) {
         for(int var3 = 1; var3 < 60; ++var3) {
            if ((var2 += var3 * super.AZ) >= 0) {
               var1 += var3;
               break;
            }
         }
      }

      super.vY1000 = -(super.AZ * super.BA);
      super.vX1000 = (super.toX - super.x) * 1000 / var1;
      super.fRemove = var1;
   }

   private void create_Zoro_S3_L2() {
      super.fraImgEff = new FrameImage(165, 27, 50);
      super.fraImgSubEff = new FrameImage(167, 78, 22);
      super.fraImgSub2Eff = new FrameImage(166, 50, 60);
      super.fRemove = 36;
      super.vMax = 12;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 500, super.Dir, super.objMainEff);
      byte var1 = -15;
      this.x1000 = super.x + 15;
      this.y1000 = super.objFireMain.y - 22;
      if (super.Dir == 2) {
         var1 = 15;
         this.x1000 = super.x - 63;
      }

      super.x += var1;
      super.y -= 5;
   }

   private void create_Zoro_S3_L1() {
      super.fraImgEff = new FrameImage(165, 27, 50);
      super.fRemove = 30;
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 400, super.Dir, super.objMainEff);
      byte var1 = -15;
      if (super.Dir == 2) {
         var1 = 15;
      }

      super.x += var1;
      super.y -= 5;
   }

   private void createMonster_NEM_BOOM_2() {
      super.fraImgEff = new FrameImage(188, 9, 16);
      super.vMax = 12;
      super.y = super.objFireMain.y - this.objBeFireMain.hOne / 2;
      if (super.Dir == 0) {
         super.x -= 15;
      } else {
         super.x += 15;
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void create_Ussop_S2_L3() {
      super.vMax = 12;
      super.fRemove = 34;
      GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 25, super.y - 5, 400, super.Dir, super.objMainEff);
      super.fraImgEff = new FrameImage(185, 55, 25);
      if (super.typeEffect == 193) {
         super.fraImgSubEff = new FrameImage(285, 111, 90);
         super.mframe = new int[]{0, 1, 2, 1};
      } else if (super.typeEffect == 225) {
         super.fRemove = 40;
         super.fraImgEff = new FrameImage(333, 55, 25);
         super.fraImgSubEff = new FrameImage(332, 111, 90);
         super.mframe = new int[]{0, 1, 2, 1};
      } else if (super.typeEffect == 302) {
         super.fraImgEff = new FrameImage(419, 2);
         super.fraImgSubEff = new FrameImage(404, 3);
         super.mframe = new int[]{0, 1, 2, 1};
         super.fraImgSub3Eff = new FrameImage(405, 3);
         int var1;
         int var2 = (var1 = super.x - 50 * super.am_duong) - super.x;
         this.VecEff.addElement(this.create_Speed(var2, 0, new Point_Focus(), super.x, super.y, var1, super.y));
      } else {
         super.fraImgSubEff = new FrameImage(184, 111, 70, 79, 50);
         super.mframe = new int[]{0, 1};
      }

      super.fraImgSub2Eff = new FrameImage(251, 52, 21);
   }

   private void create_Ussop_S2_L7() {
      super.vMax = 12;
      super.fRemove = 34;
      GameScreen.addEffectEnd((short)30, 0, super.x + super.am_duong * 25, super.y - 5, 400, super.Dir, super.objMainEff);
      super.fraImgEff = new FrameImage(419, 2);
      super.fraImgSubEff = new FrameImage(404, 3);
      super.fraImgSub2Eff = new FrameImage(251, 52, 21);
      super.fraImgSub3Eff = new FrameImage(405, 3);
      super.mframe = new int[]{0, 1, 2, 1};
      int var1;
      int var2 = (var1 = super.x - 50 * super.am_duong) - super.x;
      this.VecEff.addElement(this.create_Speed(var2, 0, new Point_Focus(), super.x, super.y, var1, super.y));
   }

   private void createUssopSkill1_Lv3_New() {
      super.fraImgEff = new FrameImage(53, 9, 9);
      super.fraImgSubEff = new FrameImage(183, 20, 54);
      super.vMax = 24;
      super.fRemove = 25;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      Point_Focus var3 = new Point_Focus();
      (var3 = this.create_Speed(var1, var2, var3)).frame = 1;
      GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
      this.VecEff.addElement(var3);
      this.CJ = super.objFireMain.x;
      this.CK = super.objFireMain.y;
   }

   private void createUssopSkill1_Lv3_SHORT() {
      super.fraImgEff = new FrameImage(53, 9, 9);
      super.fraImgSubEff = new FrameImage(183, 20, 54);
      super.vMax = 24;
      super.fRemove = 16;
      super.y -= 6;
      if (super.Dir == 0) {
         super.x -= 30;
      } else {
         super.x += 30;
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      Point_Focus var3 = new Point_Focus();
      (var3 = this.create_Speed(var1, var2, var3)).frame = 1;
      GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
      this.VecEff.addElement(var3);
      this.CJ = super.objFireMain.x;
      this.CK = super.objFireMain.y;
   }

   private void createSanji_s2_l3_New() {
      super.y = super.objFireMain.y;
      super.fraImgEff = new FrameImage(183, 20, 54);
      super.fRemove = 44;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
   }

   private void createSanji_s2_l3_New_SHORT() {
      super.y = super.objFireMain.y;
      super.fraImgEff = new FrameImage(183, 20, 54);
      super.fRemove = 24;
   }

   private void createSanji_s1_l3_New() {
      super.fraImgEff = new FrameImage(183, 20, 54);
      if (super.typeEffect == 177) {
         super.fraImgEff = new FrameImage(265, 20, 54);
      }

      super.fRemove = 50;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
   }

   private void createSanji_s1_l3_SHORT() {
      super.fraImgEff = new FrameImage(183, 20, 54);
      super.fRemove = 16;
   }

   private boolean checkNullObject(int var1) {
      if (var1 != 1 || super.objFireMain != null && !super.objFireMain.returnAction()) {
         if (var1 != 2 || this.objBeFireMain != null && !this.objBeFireMain.returnAction()) {
            return var1 == 3 && (super.objFireMain == null || super.objFireMain.returnAction() || this.objBeFireMain == null || this.objBeFireMain.returnAction());
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private void create_Devil_FIRE1() {
      this.addSoundBuff();
      if (super.typeEffect != 259 && super.typeEffect != 260 && super.typeEffect != 261) {
         super.fraImgEff = new FrameImage(7, 34, 64, 2);
         if (super.typeEffect == 228) {
            super.fraImgSubEff = new FrameImage(336, 74, 30, 3);
            super.fraImgSub2Eff = new FrameImage(78, 22, 28, 5);
         }
      } else {
         if (super.typeEffect == 259) {
            super.frameSuper = 1;
         } else if (super.typeEffect == 260) {
            super.frameSuper = 2;
         } else if (super.typeEffect == 261) {
            super.frameSuper = 3;
         }

         super.fraImgSubEff = new FrameImage(336, 74, 30, (byte)3, super.frameSuper);
         super.fraImgSub2Eff = new FrameImage(78, 22, 28, (byte)5, super.frameSuper);
         super.fraImgEff = new FrameImage(7, 34, 64, (byte)2, super.frameSuper);
      }

      super.fRemove = 30;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 250, super.Dir, super.objMainEff);
      super.toY = this.objBeFireMain.y;
   }

   private void create_Devil_FIRE2() {
      this.addSoundBuff();
      super.frameSuper = 0;
      if (super.typeEffect == 262) {
         super.frameSuper = 1;
      } else if (super.typeEffect == 263) {
         super.frameSuper = 2;
      } else if (super.typeEffect == 264) {
         super.frameSuper = 3;
      }

      super.fraImgEff = new FrameImage(32, 45, 45, (byte)5, super.frameSuper);
      super.fraImgSubEff = new FrameImage(78, 22, 28, (byte)5, super.frameSuper);
      super.fraImgSub2Eff = new FrameImage(224, 22, 28, (byte)5, super.frameSuper);
      super.fraImgSub3Eff = new FrameImage(38, 50, 80, (byte)3, super.frameSuper);
      super.fRemove = 30;
      super.vMax = 12;
      super.y = super.objFireMain.y;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y - super.objFireMain.hOne / 2, 1200, super.Dir, super.objMainEff);
   }

   private void create_ho_den_vu_tru() {
      Object_Effect_Skill var1;
      MainObject var2;
      if ((var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)) != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
         super.x = var2.x;
         super.y = var2.y;
      }

      for(int var3 = 0; var3 < this.DD.length; ++var3) {
         GameScreen.addEffectEnd((short)166, 0, 2 * CRes.getcos(this.DD[var3]) * this.DE / 1024 + super.x, CRes.getsin(this.DD[var3]) * this.DE / 1024 + super.y, super.Dir, super.objMainEff);
      }

   }

   private void create_Devil_ICE1() {
      this.addSoundBuff();
      super.fraImgEff = new FrameImage(37, 31, 74);
      super.fraImgSub2Eff = new FrameImage(40, 63, 20);
      super.fraImgSub3Eff = new FrameImage(41, 40, 40);
      super.y = super.objFireMain.y;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y - super.objFireMain.hOne / 2, 1200, super.Dir, super.objMainEff);
      super.mframe = new int[]{-1, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 0, 0, 0};
      super.fRemove = 30;
   }

   private void create_Devil_ICE2() {
      this.addSoundBuff();
      super.fraImgEff = new FrameImage(5, 80, 50);
      super.fraImgSub3Eff = new FrameImage(41, 40, 40);
      super.fraImgSubEff = new FrameImage(43, 84, 110);
      this.CL = new int[3][];
      this.CL[0] = new int[2];
      this.CL[1] = new int[2];
      this.CL[2] = new int[2];
      this.CL[0][0] = -40;
      this.CL[0][1] = -35 + super.objFireMain.LZ;
      this.CL[1][0] = 20;
      this.CL[1][1] = -67;
      this.CL[2][0] = 47;
      this.CL[2][1] = -50 + super.objFireMain.LZ;
      super.fRemove = 30;
      super.vMax = 10;
      super.y = super.objFireMain.y;
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y - super.objFireMain.hOne / 2, 1200, super.Dir, super.objMainEff);
   }

   private void create_Devil_Smoker1() {
      this.addSoundBuff();
      super.fraImgEff = new FrameImage(58, 40, 27);
      super.fraImgSubEff = new FrameImage(57, 42, 50, 32, 38);
      super.fraImgSub2Eff = new FrameImage(61, 24, 30);
      if (super.typeEffect == 232) {
         super.fraImgSub3Eff = new FrameImage(85, 34, 34, 28, 28);
      }

      super.fRemove = 30;
      super.vMax = 12;
   }

   private void create_Devil_Smoker2() {
      this.addSoundBuff();
      super.fraImgEff = new FrameImage(64, 50, 45);
      super.fraImgSubEff = new FrameImage(63, 71, 60, 50, 40);
      super.fraImgSub2Eff = new FrameImage(65, 59, 65);
      super.fraImgSub3Eff = new FrameImage(61, 24, 30);
      if (super.typeEffect == 234) {
         super.BP = new FrameImage(85, 34, 34, 28, 28);
      }

      super.fRemove = 30;
      super.vMax = 26;
   }

   private void createSmoker1() {
      super.fraImgEff = new FrameImage(64, 50, 45);
      super.fraImgSubEff = new FrameImage(63, 71, 60, 51, 43);
      super.fraImgSub2Eff = new FrameImage(86, 32, 79);
      super.fraImgSub3Eff = new FrameImage(61, 24, 30);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 100, super.Dir, super.objMainEff);
      super.fRemove = 20;
      super.vMax = 26;
   }

   private void createSmoker2() {
      super.fraImgEff = new FrameImage(86, 32, 79);
      super.fraImgSubEff = new FrameImage(87, 35, 35, 28, 28);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 100, super.Dir, super.objMainEff);
      super.frame = 5;
      if (super.Dir == 2) {
         super.frame = 6;
      }

      super.fRemove = 20;
      super.vMax = 14;
   }

   private void createZoro_S2_L1_New() {
      super.fRemove = 6;
   }

   private void createMissGold_1() {
      super.fraImgEff = new FrameImage(212, 33, 24);
      super.fRemove = 24;
   }

   private void createMr3_2() {
      super.fraImgEff = new FrameImage(211, 35, 22);
      super.fraImgSubEff = new FrameImage(32, 45, 45, 34, 34);
      super.fraImgSub2Eff = new FrameImage(160, 9, 14);
      super.fRemove = 20;
      super.vMax = 16;
   }

   private void createMr3_1() {
      super.fraImgEff = new FrameImage(211, 35, 22);
      GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
      super.fRemove = 20;
      super.vMax = 16;
   }

   private void create_Wapol4() {
      super.fraImgEff = new FrameImage(20, 10, 10);
      if (super.Dir == 0) {
         super.x -= 25;
         super.vx = -48;
      } else {
         super.x += 25;
         super.vx = 48;
      }

      super.y += 7;
      this.x1000 = super.x;
      super.fRemove = 20;
   }

   private void createWapol3() {
      super.fraImgEff = new FrameImage(20, 10, 10);
      if (super.Dir == 0) {
         super.x -= 5;
      } else {
         super.x += 5;
      }

      super.y += 7;
      super.fRemove = 25;
      super.vMax = 14;
   }

   private void createWapol2() {
      super.fraImgEff = new FrameImage(209, 32, 46);
      if (super.Dir == 0) {
         super.x -= 10;
      } else {
         super.x += 10;
      }

      super.y -= 5;
      super.numNextFrame = 2;
      super.vy = -3;
      super.fRemove = 4;
   }

   private void createWapol() {
      super.levelPaint = -1;
      super.fraImgEff = new FrameImage(208, 50, 57);
      super.fraImgSubEff = new FrameImage(144, 37, 55);
      super.vMax = 14;
      super.y = super.objFireMain.y;
      super.toY = this.objBeFireMain.y;
      if (super.objFireMain.plashNow != null) {
         super.objFireMain.plashNow.AA((byte)1);
      }

      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void createKuromarimo() {
      super.fraImgEff = new FrameImage(207, 14, 14);
      super.vMax = 14;
      if (super.Dir == 0) {
         super.x -= 5;
      } else {
         super.x += 5;
      }

      super.y -= 20;
      if (CRes.random(2) == 0) {
         this.subType = 0;
         super.toX += 6;
      } else {
         this.subType = 1;
         super.toX -= 6;
      }

      if (!this.checkNullObject((int)2)) {
         super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
      }

      super.toY += 14;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
   }

   private void createChess() {
      super.fraImgEff = new FrameImage(205, 20, 20);
      super.fraImgSubEff = new FrameImage(206, 20, 20);
      super.vMax = 18;
      if (super.Dir == 0) {
         super.x -= 10;
      } else {
         super.x += 10;
      }

      super.y -= 10;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      int var3 = CRes.AA(var1, var2);
      this.create_Speed(var1, var2, (Point_Focus)null);
      super.frame = this.setFrameAngle(var3);
   }

   private void create_Zoro_S3_L3() {
      super.Dir = (byte)super.objFireMain.type_left_right;
      if (super.typeEffect != 185 && super.typeEffect != 217) {
         super.fraImgEff = new FrameImage(165, 27, 50);
      } else {
         super.fraImgEff = new FrameImage(280, 50, 74, 2);
      }

      super.fraImgSubEff = new FrameImage(167, 78, 22);
      super.fraImgSub2Eff = new FrameImage(16, 55, 55);
      super.fraImgSub3Eff = new FrameImage(17, 55, 55);
      super.BP = new FrameImage(8, 40, 47, 40, 47);
      super.fRemove = 30;
      super.vMax = 12;
      if (super.typeEffect == 283) {
         super.fraImgEff = new FrameImage(421, 50, 74, 2);
         super.fraImgSub2Eff = new FrameImage(409, 4);
      }

      byte var1 = -15;
      this.CJ = super.objFireMain.x;
      this.CK = super.objFireMain.y;
      this.x1000 = super.x - 5;
      this.y1000 = super.objFireMain.y - 22;
      super.objFireMain.dy = 0;
      if (super.Dir == 2) {
         var1 = 15;
         this.x1000 = super.x - 73;
      }

      super.x += var1;
      super.y -= 5;
   }

   private void create_Zoro_S3_L7() {
      super.Dir = (byte)super.objFireMain.type_left_right;
      super.fraImgEff = new FrameImage(440, 12);
      super.fraImgSubEff = new FrameImage(442, 14);
      super.fraImgSub2Eff = new FrameImage(445, 9);
      super.fraImgSub3Eff = new FrameImage(17, 55, 55);
      super.BP = new FrameImage(8, 40, 47, 40, 47);
      super.fRemove = 30;
      super.vMax = 12;
      super.numNextFrame = 2;
      byte var1 = -15;
      this.CJ = super.objFireMain.x;
      this.CK = super.objFireMain.y;
      this.x1000 = super.x - 5;
      this.y1000 = super.objFireMain.y - 22;
      super.objFireMain.dy = 0;
      if (super.Dir == 2) {
         var1 = 15;
         this.x1000 = super.x - 73;
      }

      super.x += var1;
      super.y -= 5;
   }

   private void createZoro_New2_SHORT() {
      super.fRemove = 24;
      super.vMax = 12;
      super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
   }

   private void updateLuffy1() {
      if (this.objBeFireMain != null && super.f % 3 == 0 && this.CI < super.vecObjsBeFire.size()) {
         Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
         ++this.CI;
         MainObject var2;
         if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
            byte var5 = 0;
            if (super.objFireMain.x < var2.x) {
               var5 = 2;
            }

            byte var3 = 12;
            if (super.Dir == 0) {
               var3 = -12;
            }

            byte var4 = 0;
            if (super.typeEffect == 37) {
               var4 = 2;
            }

            GameScreen.addEffectEnd_ObjTo((short)13, var4, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (short)var2.ID, (byte)var2.typeObject, var5, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else if (super.typeEffect == 37 && super.f % 2 == 0) {
         Point var6 = new Point(super.x + CRes.random_Am_0(15), super.y + CRes.random_Am_0(20));
         this.VecEff.addElement(var6);
      }

      for(int var8 = 0; var8 < this.VecEff.size(); ++var8) {
         Point var7;
         ++(var7 = (Point)this.VecEff.elementAt(var8)).f;
         if (var7.f >= 3) {
            this.VecEff.removeElement(var7);
            --var8;
         }
      }

   }

   private void updateSanji1() {
      byte var1;
      if (super.f == 1) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 15;
         if (super.Dir == 0) {
            var1 = -15;
         }

         GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
      }

      if (super.f == 8 && super.objFireMain != null) {
         var1 = 27;
         if (super.Dir == 0) {
            var1 = -27;
         }

         if (super.typeEffect == 47 || super.typeEffect == 48) {
            byte var2 = 0;
            if (super.typeEffect == 48) {
               var2 = 1;
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd_ObjTo((short)37, var2, super.x + var1, super.y - super.objFireMain.hOne / 2, (short)this.objBeFireMain.ID, (byte)this.objBeFireMain.typeObject, super.Dir, super.objMainEff);
            }
         }
      }

      if (super.f >= super.fRemove) {
         super.objFireMain.dx = 0;
         this.removeEff();
      }

   }

   private void updateZoro1() {
      if (!this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = true;
         super.objFireMain.Action = 2;
         super.objFireMain.vx = super.vx;
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.Action = 0;
         }

         GameScreen.addEffectEnd((short)86, 0, super.x + (super.Dir == 0 ? 20 : -20), super.y, super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)9, 0, super.toX, super.toY + 25, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateZoro2() {
      if (super.f < 2) {
         super.AZ = -3 + super.f * 3;
      }

      if (super.f == 4) {
         if (!this.checkNullObject((int)1) && super.objFireMain == GameScreen.player) {
            int var5 = super.toY + super.fraImgEff.frameHeight / 2;
            int var4 = super.y;
            int var3 = super.toX;
            int var2 = super.x;
            Effect_Skill var1 = this;
            if (super.objFireMain != GameScreen.player) {
               super.objFireMain.x = var3;
               super.objFireMain.y = var5;
            } else {
               if (MainObject.getDistance(var2, var4, var2, var4) <= 30) {
                  super.objFireMain.x = var3;
                  super.objFireMain.y = var5;
               }

               int var6;
               if ((var6 = CRes.abs(var2 - var3)) < CRes.abs(var4 - var5)) {
                  var6 = CRes.abs(var4 - var5);
               }

               if ((var4 = var6 / 20) == 0) {
                  var4 = 1;
               }

               var6 = (var3 - var2) / var4;
               var2 = (var3 - var2) / var4;

               for(int var7 = 0; var7 < var4; ++var7) {
                  MainObject var10000 = var1.objFireMain;
                  var10000.x += var6;
                  var10000 = var1.objFireMain;
                  var10000.y += var2;
                  GlobalService.getInstance().Obj_Move((short)var1.objFireMain.x, (short)var1.objFireMain.y);
               }

               var1.objFireMain.x = var3;
               var1.objFireMain.y = var5;
               GlobalService.getInstance().Obj_Move((short)var1.objFireMain.x, (short)var1.objFireMain.y);
            }
         }

         super.AZ = 0;
         super.x = super.toX;
         super.y = super.toY;
      }

      if (super.f > 5) {
         super.AZ = 3 - (super.f - 5) * 3;
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
            if (super.objFireMain.plashNow != null) {
               super.objFireMain.plashNow.AA((byte)0);
            }
         }

         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)9, 0, this.objBeFireMain.x, this.objBeFireMain.y + 25, super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)86, 0, super.x + (super.Dir == 0 ? -10 : 10), super.y - 25, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateUssopSea1() {
      if ((super.f == 8 || super.f == 12) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.VecEff.removeElement(var2);
            GameScreen.addEffectEnd((short)1, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            --var1;
         }
      }

      if (super.f == 10 || super.f == 13 || super.f == 15) {
         if (!this.checkNullObject((int)2)) {
            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(8);
         }

         this.setAngle();
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.Dir = super.Dir;
         }

         var1 = super.toX - super.x;
         int var5 = super.toY - super.y;
         int var3 = CRes.AA(var1, var5);
         Point_Focus var4 = new Point_Focus();
         (var4 = this.create_Speed(var1, var5, var4)).frame = this.setFrameAngle(var3);
         this.VecEff.addElement(var4);
         GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)93, 2, super.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateUssopSea2() {
      if ((super.f == 8 || super.f == 12) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)81, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)1, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 4 || super.f == 9 || super.f == 14 || super.f == 19) {
         if (!this.checkNullObject((int)3)) {
            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(8);
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2 - 6;
            if (super.Dir == 0) {
               super.x = super.objFireMain.x - 22;
            } else {
               super.x = super.objFireMain.x + 22;
            }
         }

         GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
         this.setAngle();
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.Dir = super.Dir;
         }

         byte var7 = 1;
         if (super.f == 9 || super.f == 19) {
            var7 = 2;
         }

         for(int var8 = 0; var8 < var7; ++var8) {
            if (var8 == 1) {
               super.y -= 10;
            }

            int var3 = super.toX - super.x;
            int var4 = super.toY - super.y;
            int var5 = CRes.AA(var3, var4);
            Point_Focus var6 = new Point_Focus();
            (var6 = this.create_Speed(var3, var4, var6)).frame = this.setFrameAngle(var5);
            this.VecEff.addElement(var6);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateUssopSea3() {
      if ((super.f == 4 || super.f == 8 || super.f == 12 || super.f == 16) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
         if (super.f == 8 || super.f == 16) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      if (super.f == 10 && !this.checkNullObject((int)2)) {
         GameScreen.addEffectEnd((short)108, 1, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f > 4 && super.f % 3 == 0 && super.f <= 19) {
         GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
         var1 = CRes.random(6, 9);

         for(int var4 = 0; var4 < var1; ++var4) {
            Point_Focus var3;
            (var3 = new Point_Focus()).x = super.x * 10;
            var3.y = super.y * 10;
            var3.AI = super.vMax * 10 * super.am_duong + CRes.random_Am_0(7);
            var3.vy = -(var1 * 13) / 2 + var4 * 13;
            var3.frame = 0;
            var3.fRe = 16;
            var3.dis = super.Dir;
            this.VecEff.addElement(var3);
         }

         this.addVir(5, 5, 10, true);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateUssop2() {
      if (super.f == 3) {
         GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
      }

      if ((super.f == 0 || super.f == 3) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f == super.fRemove - 2) {
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)93, 1, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)93, 1, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateUssop_Skill2() {
      float var10000;
      if (super.f == 3 && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f == 1) {
         GameScreen.addEffectEnd((short)5, 0, super.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         if (super.typeEffect == 64) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            GameScreen.addEffectEnd((short)12, 1, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         } else if (super.typeEffect == 66) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.setAva(1, this.objBeFireMain);
            GameScreen.addEffectEnd((short)4, 2, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)93, 2, super.toX + CRes.random_Am_0(12), super.toY + CRes.random_Am_0(12), super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateNami1() {
      if (super.f > 1) {
         for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
            Point_Focus var2;
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG >= var2.fRe) {
               float var10000;
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.setAva(1, this.objBeFireMain);
               byte var3 = 0;
               if (super.typeEffect == 9) {
                  GameScreen.addEffectEnd((short)3, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               } else if (super.typeEffect == 53) {
                  GameScreen.addEffectEnd((short)38, 1, super.toX, super.toY, super.Dir, super.objMainEff);
                  var3 = 1;
               } else if (super.typeEffect == 163) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  this.addVir(5, 5, 10, true);
                  GameScreen.addEffectEnd((short)42, 0, super.toX, super.toY, super.Dir, super.objMainEff);
                  var3 = 1;
               }

               GameScreen.addEffectEnd((short)6, var3, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)93, 1, super.toX, super.toY, super.Dir, super.objMainEff);
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      }

   }

   private void updateNami1_SHORT() {
      float var10000;
      if (super.f == 12 || super.f == 22) {
         super.y += 5;
         int var2 = super.toX - super.x;
         int var3 = super.toY - super.y;
         Point_Focus var4 = new Point_Focus();
         var4 = this.create_Speed(var2, var3, var4);
         if (super.f == 22) {
            var4.frame = 1;
         }

         this.VecEff.addElement(var4);
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      int var1;
      if (super.typeEffect == 222 || super.typeEffect == 312) {
         for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
            Point var5;
            ++(var5 = (Point)this.VecSubEff.elementAt(var1)).f;
            if (var5.f / 2 >= super.fraImgSub3Eff.nFrame) {
               this.VecSubEff.removeElement(var1);
               --var1;
            }
         }
      }

      if (super.f > 1) {
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            Point_Focus var6 = (Point_Focus)this.VecEff.elementAt(var1);
            if ((super.typeEffect == 222 || super.typeEffect == 312) && !GameCanvas.lowGraphic) {
               Point var7 = new Point(var6.x, var6.y);
               this.VecSubEff.addElement(var7);
            }

            var6.update_Vx_Vy();
            if (var6.AG > var6.fRe) {
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.setAva(1, this.objBeFireMain);
               byte var8 = 1;
               if (super.typeEffect == 190) {
                  var8 = 2;
               } else if (super.typeEffect == 222 || super.typeEffect == 312) {
                  var8 = 3;
               }

               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.addVir(5, 5, 10, true);
               if (var6.frame == 1) {
                  byte var9 = 2;
                  if (super.typeEffect == 190 || super.typeEffect == 222 || super.typeEffect == 312) {
                     var9 = 8;
                     GameScreen.addEffectEnd((short)108, 3, super.toX, super.toY, super.Dir, super.objMainEff);
                  }

                  GameScreen.addEffectEnd((short)54, var9, super.toX, super.toY, super.Dir, super.objMainEff);
               } else if (!GameCanvas.lowGraphic) {
                  if (super.typeEffect == 222) {
                     GameScreen.addEffectEnd((short)139, 0, super.toX, super.toY, super.Dir, super.objMainEff);
                  }

                  if (super.typeEffect == 312) {
                     GameScreen.addEffectEnd((short)139, 1, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
                  }
               }

               GameScreen.addEffectEnd((short)42, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)6, var8, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 8, super.toX, super.toY, super.Dir, super.objMainEff);
               this.VecEff.removeElement(var6);
               --var1;
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void update_Nami_S2_L7() {
      float var10000;
      if (super.f == 12 || super.f == 22) {
         super.y += 5;
         int var2 = super.toX - super.x;
         int var3 = super.toY - super.y;
         Point_Focus var1 = new Point_Focus();
         var1 = this.create_Speed(var2, var3, var1);
         if (super.f == 22) {
            var1.frame = 1;
         }

         this.VecEff.addElement(var1);
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }
      }

      int var4;
      for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
         Point var5;
         ++(var5 = (Point)this.VecSubEff.elementAt(var4)).f;
         if (var5.f / 2 >= super.fraImgSub3Eff.nFrame) {
            this.VecSubEff.removeElement(var4);
            --var4;
         }
      }

      if (super.f > 1) {
         for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
            Point_Focus var6 = (Point_Focus)this.VecEff.elementAt(var4);
            if (!GameCanvas.lowGraphic) {
               Point var7 = new Point(var6.x, var6.y);
               this.VecSubEff.addElement(var7);
            }

            var6.update_Vx_Vy();
            if (var6.AG > var6.fRe) {
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.setAva(1, this.objBeFireMain);
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.addVir(5, 5, 10, true);
               if (var6.frame == 1) {
                  GameScreen.addEffectEnd((short)108, 3, super.toX, super.toY, super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd((short)54, 8, super.toX, super.toY, super.Dir, super.objMainEff);
               } else if (!GameCanvas.lowGraphic) {
                  GameScreen.addEffectEnd((short)184, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
               }

               GameScreen.addEffectEnd((short)42, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)6, 3, super.toX, super.toY, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 8, super.toX, super.toY, super.Dir, super.objMainEff);
               this.VecEff.removeElement(var6);
               --var4;
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateNamiSea1() {
      float var10000;
      if (super.f == 4 && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      if (super.f == 10 && !this.checkNullObject((int)2)) {
         var1 = this.objBeFireMain.x - super.x;
         int var2 = this.objBeFireMain.y - super.y;
         Point_Focus var3 = new Point_Focus();
         var3 = this.create_Speed(var1, var2, var3);
         this.VecEff.addElement(var3);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var4.AG >= var4.fRe) {
            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)2)) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 1, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateNamiSea2() {
      float var10000;
      if (super.f == 8 && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f >= 2 && super.f <= 16) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.NF = false;
         }
      } else {
         super.objFireMain.NF = true;
      }

      int var1;
      if (super.f == 14 && !this.checkNullObject((int)2)) {
         var1 = this.objBeFireMain.x - super.x;
         int var2 = this.objBeFireMain.y - super.y;
         Point_Focus var3 = new Point_Focus();
         var3 = this.create_Speed(var1, var2, var3);
         this.VecEff.addElement(var3);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var4.AG >= var4.fRe) {
            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)2)) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            GameScreen.addEffectEnd((short)41, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateNamiSea3() {
      float var10000;
      if (super.f == 2 && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f >= 2 && super.f <= 16) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.NF = false;
         }
      } else {
         super.objFireMain.NF = true;
      }

      int var1;
      int var2;
      if (super.f >= 24 && super.f <= 34 && !this.checkNullObject((int)2) && CRes.random(4) != 0) {
         var1 = CRes.random(1, 3);

         for(var2 = 0; var2 < var1; ++var2) {
            int var3 = CRes.random_Am(0, 25) + this.objBeFireMain.x;
            GameScreen.addEffectEnd((short)90, 1, var3, this.objBeFireMain.y - 10, super.Dir, super.objMainEff);
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var4.AG >= var4.fRe) {
            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      if ((super.f == 10 || super.f == 16) && !this.checkNullObject((int)3)) {
         var1 = this.objBeFireMain.x - super.x;
         var2 = this.objBeFireMain.y - 60 - super.y;
         Point_Focus var5 = new Point_Focus();
         (var5 = this.create_Speed(var1, var2, var5, super.x, super.objFireMain.y - super.objFireMain.hOne / 2, this.objBeFireMain.x, this.objBeFireMain.y - 70)).frame = 1;
         this.VecEff.addElement(var5);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)2)) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            GameScreen.addEffectEnd((short)41, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 8, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateSanji2() {
      if (super.f == 4) {
         this.addVir(5, 5, 10, true);
      }

      int var6;
      if (super.f >= 6 && super.f <= super.fRemove) {
         if (!this.checkNullObject((int)1) && CRes.random(2) == 0) {
            super.objFireMain.dx = CRes.random_Am_0(2);
            super.AZ = super.objFireMain.dx;
         }

         if (super.f % 3 == 0) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            int var4;
            int var7;
            if (this.CI < super.vecObjsBeFire.size()) {
               Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
               ++this.CI;
               MainObject var2;
               if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
                  int var3 = var2.x - super.x;
                  var4 = var2.y - super.objFireMain.hOne / 2 - super.y;
                  Point_Focus var5 = new Point_Focus();
                  var7 = super.y;
                  super.y += CRes.random_Am_0(15);
                  var5 = this.create_Speed(var3, var4, var5);
                  super.y = var7;
                  var5.dis = 1;
                  var5.AM = 0;
                  if (super.typeEffect == 220 || super.typeEffect == 293) {
                     var5.AM = 5;
                  }

                  var5.frame = this.CI % 2;
                  this.VecEff.addElement(var5);
               }
            } else if (!GameCanvas.lowGraphic) {
               ++this.CI;
               var6 = super.am_duong * 140 + CRes.random_Am_0(20);
               var7 = CRes.random_Am_0(80);
               Point_Focus var10 = new Point_Focus();
               var4 = super.y;
               super.y += CRes.random_Am_0(15);
               var10 = this.create_Speed(var6, var7, var10);
               super.y = var4;
               var10.dis = 0;
               if (super.typeEffect == 220 || super.typeEffect == 293) {
                  var10.AM = 5;
               }

               var10.frame = this.CI % 2;
               this.VecEff.addElement(var10);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.dx = 0;
         }

         this.removeEff();
      }

      for(var6 = 0; var6 < this.VecSubEff.size(); ++var6) {
         Point var8;
         ++(var8 = (Point)this.VecSubEff.elementAt(var6)).f;
         if (var8.f >= super.fraImgSub3Eff.nFrame) {
            this.VecSubEff.removeElement(var8);
            --var6;
         }
      }

      for(var6 = 0; var6 < this.VecEff.size(); ++var6) {
         Point_Focus var9;
         (var9 = (Point_Focus)this.VecEff.elementAt(var6)).update_Vx_Vy();
         Point var11;
         (var11 = new Point(var9.x, var9.y)).frame = var9.frame;
         this.VecSubEff.addElement(var11);
         if (var9.AG == var9.fRe && var9.dis == 1) {
            GameScreen.addEffectEnd((short)35, 0, var9.x, var9.y, super.Dir, super.objMainEff);
            byte var12 = 7;
            if (super.typeEffect == 293) {
               var12 = 0;
            }

            GameScreen.addEffectEnd((short)108, var12, var9.x, var9.y, super.Dir, super.objMainEff);
         }

         if (var9.AG >= var9.fRe + var9.AM) {
            this.VecEff.removeElement(var9);
            --var6;
         }
      }

   }

   private void update_Sanji_S3_L7() {
      if (super.f == 4) {
         this.addVir(5, 5, 10, true);
      }

      int var6;
      if (super.f >= 6 && super.f <= super.fRemove) {
         if (!this.checkNullObject((int)1) && CRes.random(2) == 0) {
            super.objFireMain.dx = CRes.random_Am_0(2);
            super.AZ = super.objFireMain.dx;
         }

         if (super.f % 3 == 0) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            int var4;
            Point_Focus var5;
            if (this.CI < super.vecObjsBeFire.size()) {
               Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
               ++this.CI;
               MainObject var2;
               if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
                  int var3 = var2.x - super.x;
                  var4 = var2.y - super.objFireMain.hOne / 2 - super.y;
                  var5 = new Point_Focus();
                  var6 = super.y;
                  super.y += CRes.random_Am_0(15);
                  var5 = this.create_Speed(var3, var4, var5);
                  super.y = var6;
                  var5.dis = 1;
                  var5.AM = 0;
                  var5.AM = 5;
                  var5.frame = this.CI % 4;
                  if (super.Dir == 2) {
                     var5.AB = 2;
                  } else {
                     var5.AB = 0;
                  }

                  this.VecEff.addElement(var5);
                  Point_Focus var7 = new Point_Focus();
                  (var7 = this.create_Speed(-var3, var4, var7)).dis = 1;
                  var7.AM = 5;
                  var7.frame = this.CI % 4;
                  if (super.Dir == 2) {
                     var7.AB = 0;
                  } else {
                     var7.AB = 2;
                  }

                  this.VecEff.addElement(var7);
               }
            } else if (!GameCanvas.lowGraphic) {
               ++this.CI;
               var6 = super.am_duong * 140 + CRes.random_Am_0(20);
               int var8 = CRes.random_Am_0(80);
               Point_Focus var11 = new Point_Focus();
               var4 = super.y;
               super.y += CRes.random_Am_0(15);
               var11 = this.create_Speed(var6, var8, var11);
               super.y = var4;
               var11.dis = 0;
               var11.AM = 5;
               var11.frame = this.CI % 4;
               if (super.Dir == 2) {
                  var11.AB = 2;
               } else {
                  var11.AB = 0;
               }

               this.VecEff.addElement(var11);
               (var5 = new Point_Focus()).dis = 0;
               var5.AM = 5;
               var5.frame = this.CI % 4;
               var5 = this.create_Speed(-var6, var8, var5);
               if (super.Dir == 2) {
                  var5.AB = 0;
               } else {
                  var5.AB = 2;
               }

               this.VecEff.addElement(var5);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.dx = 0;
         }

         this.removeEff();
      }

      for(var6 = 0; var6 < this.VecSubEff.size(); ++var6) {
         Point var9;
         ++(var9 = (Point)this.VecSubEff.elementAt(var6)).f;
         if (var9.f >= super.fraImgSub3Eff.nFrame) {
            this.VecSubEff.removeElement(var9);
            --var6;
         }
      }

      for(var6 = 0; var6 < this.VecEff.size(); ++var6) {
         Point_Focus var10;
         (var10 = (Point_Focus)this.VecEff.elementAt(var6)).update_Vx_Vy();
         Point var12;
         (var12 = new Point(var10.x, var10.y)).frame = var10.frame;
         var12.dis = var10.AB;
         this.VecSubEff.addElement(var12);
         if (var10.AG == var10.fRe && var10.dis == 1) {
            GameScreen.addEffectEnd((short)35, 0, var10.x, var10.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 0, var10.x, var10.y, super.Dir, super.objMainEff);
         }

         if (var10.AG >= var10.fRe + var10.AM) {
            this.VecEff.removeElement(var10);
            --var6;
         }
      }

   }

   private void updateRankyaku() {
      if (super.f >= 3 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         if (super.f > 3 + (var1 << 2)) {
            Point_Focus var2;
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var1 == 0 & var2.AG == var2.fRe) {
               GameScreen.addEffectEnd((short)19, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 8, var2.AK, var2.AL, super.Dir, super.objMainEff);
            }

            if (var2.AG >= var2.fRe + 15) {
               this.VecEff.removeElement(var1);
               --var1;
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateSoi() {
      if (super.f >= 2 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      this.x1000 = super.x + 30 * super.am_duong;
      int var1 = this.x1000 - super.x;
      if (super.f == 2) {
         this.VecEff.addElement(this.create_Speed(var1, -8, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
         this.VecEff.addElement(this.create_Speed(var1, 8, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
      }

      if (super.f == 4) {
         this.VecEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var1 == 0 & var2.AG == var2.fRe) {
            GameScreen.addEffectEnd((short)123, 3, var2.AK, var2.AL, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 3, var2.AK, var2.AL, super.Dir, super.objMainEff);
         }

         if (var2.AG >= var2.fRe + 25) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateSoi2() {
      if (super.f >= 2 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      this.x1000 = super.x + 30 * super.am_duong;
      int var1 = this.x1000 - super.x;
      if (super.f == 2) {
         this.VecEff.addElement(this.create_Speed(var1, -14, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
         this.VecEff.addElement(this.create_Speed(var1, 14, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
      }

      if (super.f == 4) {
         this.VecEff.addElement(this.create_Speed(var1, -8, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
         this.VecEff.addElement(this.create_Speed(var1, 8, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
      }

      if (super.f == 6) {
         this.VecEff.addElement(this.create_Speed(var1, 0, new Point_Focus(), this.x1000, super.y, super.toX, super.toY));
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var1 == 0 & var2.AG == var2.fRe) {
            GameScreen.addEffectEnd((short)19, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 7, var2.AK, var2.AL, super.Dir, super.objMainEff);
         }

         if (var2.AG >= var2.fRe + 25) {
            this.VecEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateHuou() {
      if (super.f >= 4 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f == 5) {
         GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var3 = var2 = (Point)this.VecEff.elementAt(var1);
         var3.x += var2.vx;
         var2.y += var2.vy;
         if (var2.y >= 40) {
            if (var1 >= this.DB) {
               this.VecEff.removeElement(var1);
               --var1;
            } else {
               this.createPointHuou(var2);
               (var2 = new Point()).x = CRes.random_Am_0(40);
               var2.y = CRes.random_Am_0(30);
               var2.dis = 5;
               if (super.typeEffect == 279) {
                  var2.dis = CRes.random(10);
               }

               var2.frame = 0;
               var2.AR = 3;
               this.VecSubEff.addElement(var2);
            }
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).frame;
         if (var2.frame >= var2.AR) {
            this.VecSubEff.removeElement(var1);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateShigan() {
      if (super.f > 2) {
         for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
            Point_Focus var2;
            (var2 = (Point_Focus)this.VecEff.elementAt(0)).update_Vx_Vy();
            if (super.f == 4) {
               GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 5, var2.AK, var2.AL, super.Dir, super.objMainEff);
            }
         }
      }

      if (super.f >= super.fRemove) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateDoor() {
      if (super.f == 1) {
         this.x1000 = super.x;
      }

      if (super.f == 15) {
         this.x1000 = this.objBeFireMain.x + 40 * super.am_duong;
      }

      if (super.f == 6) {
         super.objFireMain.isTanHinh = true;
         GameScreen.addEffectEnd((short)80, 0, super.objFireMain.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f == 20) {
         super.objFireMain.x = this.x1000;
         this.changeDir();
         super.objFireMain.Dir = super.Dir;
         GameScreen.addEffectEnd((short)80, 0, super.objFireMain.x, super.y, super.Dir, super.objMainEff);
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 23) {
         GameScreen.addEffectEnd((short)123, 2, this.objBeFireMain.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void AA(int var1, int var2) {
      Point var3;
      (var3 = new Point()).x = var1;
      var3.y = var2;
      this.VecEff.addElement(var3);
   }

   private void updateDoor2() {
      if (super.f >= 2 && super.f <= 20) {
         if (super.f == 2) {
            this.AA(super.x, super.y);
         }

         if (super.f == 6) {
            this.AA(this.objBeFireMain.x + 90 * super.am_duong, super.y - 60);
         }

         if (super.f == 8) {
            this.AA(this.objBeFireMain.x - 90 * super.am_duong, super.y - 60);
         }

         if (super.f == 12) {
            this.AA(this.objBeFireMain.x + 90 * super.am_duong, super.y + 60);
         }

         if (super.f == 16) {
            this.AA(this.objBeFireMain.x - 90 * super.am_duong, super.y + 60);
         }

         if (super.f == 20) {
            this.AA(this.objBeFireMain.x + 40 * super.am_duong, super.y);
         }
      }

      if (super.f >= 4 && super.f <= 25 && (super.f - 4) % 4 == 0) {
         Point var1 = (Point)this.VecEff.elementAt((super.f - 4) / 4);
         super.objFireMain.isTanHinh = true;
         GameScreen.addEffectEnd((short)80, 0, var1.x, var1.y, super.Dir, super.objMainEff);
      }

      if (super.f == 25) {
         super.objFireMain.x = this.objBeFireMain.x + 40 * super.am_duong;
         this.changeDir();
         super.objFireMain.Dir = super.Dir;
         super.objFireMain.isTanHinh = false;
         GameScreen.addEffectEnd((short)80, 0, super.objFireMain.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f == 23) {
         GameScreen.addEffectEnd((short)123, 2, this.objBeFireMain.x, super.y, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateSanji4() {
      if (this.objBeFireMain != null && this.objBeFireMain.hOne > 0) {
         if (super.f == 1 && this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f % 4 == 0) {
            if (super.typeEffect == 14) {
               this.setAva(0, this.objBeFireMain);
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)93, 2, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            }

            if (super.typeEffect == 44) {
               this.setAva(1, this.objBeFireMain);
               if (!this.checkNullObject((int)2)) {
                  GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
               }

               byte var1 = 25;
               if (super.Dir == 0) {
                  var1 = -25;
               }

               if (!this.checkNullObject((int)1)) {
                  GameScreen.addEffectEnd((short)35, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               }
            }
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateZoroSea3() {
      if ((super.f == 4 || super.f == 10) && !this.checkNullObject((int)1)) {
         GameScreen.addEffectEnd((short)30, 0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, 200, super.Dir, super.objMainEff);
      }

      if (super.f > 0 && super.f <= 4 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = super.f * 14;
      }

      if (super.f >= 5 && super.f <= 13 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 56;
      }

      if (super.f >= 14 && super.f <= 17 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = (17 - super.f) * 14;
      }

      if (super.f == 5 || super.f == 11) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         byte var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)16, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 10 - super.objFireMain.dy, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 - 10 - super.objFireMain.dy, super.Dir, super.objMainEff);
         }
      }

      if (!this.checkNullObject((int)3) && (super.f == 6 || super.f == 12)) {
         this.addVir(5, 5, 10, true);
         byte var3 = 0;
         if (super.objFireMain.x < this.objBeFireMain.x) {
            var3 = 2;
         }

         byte var2 = 18;
         if (super.Dir == 0) {
            var2 = -18;
         }

         GameScreen.addEffectEnd_ObjTo((short)27, 2, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2, (short)this.objBeFireMain.ID, (byte)this.objBeFireMain.typeObject, var3, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.dy = 0;
         }

         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateZoroSea1() {
      if (super.f == 1 && !this.checkNullObject((int)1)) {
         GameScreen.addEffectEnd((short)30, 0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
      }

      if (super.f == 11) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         byte var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)16, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, super.objMainEff);
         }
      }

      if (!this.checkNullObject((int)3) && super.f == 12) {
         byte var3 = 0;
         if (super.objFireMain.x < this.objBeFireMain.x) {
            var3 = 2;
         }

         byte var2 = 18;
         if (super.Dir == 0) {
            var2 = -18;
         }

         GameScreen.addEffectEnd_ObjTo((short)27, 0, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2, (short)this.objBeFireMain.ID, (byte)this.objBeFireMain.typeObject, var3, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateZoroSea2() {
      if (super.f == 7 && !this.checkNullObject((int)1)) {
         GameScreen.addEffectEnd((short)30, 0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne, 250, super.Dir, super.objMainEff);
      }

      if (super.f == 4 || super.f == 16) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)1)) {
            byte var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            if (!this.checkNullObject((int)1)) {
               GameScreen.addEffectEnd((short)16, 0, super.x, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)16, 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 - 10, super.Dir, super.objMainEff);
            }
         }
      }

      if (!this.checkNullObject((int)3) && (super.f == 5 || super.f == 17)) {
         byte var3 = 0;
         if (super.objFireMain.x < this.objBeFireMain.x) {
            var3 = 2;
         }

         byte var2 = 18;
         if (super.Dir == 0) {
            var2 = -18;
         }

         GameScreen.addEffectEnd_ObjTo((short)27, 1, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2, (short)this.objBeFireMain.ID, (byte)this.objBeFireMain.typeObject, var3, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         this.removeEff();
      }

   }

   private void updateZoro3() {
      float var10000;
      byte var1;
      if (super.f == 5) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         this.setAva(0, this.objBeFireMain);
         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)16, 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }
      }

      if (super.f == 10) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 30;
         if (super.Dir == 0) {
            var1 = -30;
         }

         this.setAva(0, this.objBeFireMain);
         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)16, 2, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 5, super.Dir, super.objMainEff);
         }
      }

      if (super.f == 15) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         this.setAva(1, this.objBeFireMain);
         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)16, 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateLuffy6() {
      if (super.f >= super.fRemove || this.checkNullObject((int)1)) {
         this.removeEff();
         if (super.objFireMain == GameScreen.player) {
            GameScreen.AB(false);
         }
      }

      if (super.f < 7) {
         if (super.Dir == 0) {
            super.objFireMain.vx = -super.objFireMain.CN * 3;
         } else {
            super.objFireMain.vx = super.objFireMain.CN * 3;
         }
      } else {
         super.objFireMain.vx = 0;
      }

      if (super.f == 7) {
         this.setAva(1, this.objBeFireMain);
         byte var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         GameScreen.addEffectEnd((short)93, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
      }

   }

   private void updateLuffy_S2_L2() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f < 6) {
            if (super.Dir == 0) {
               super.objFireMain.vx = -super.objFireMain.CN * 3;
            } else {
               super.objFireMain.vx = super.objFireMain.CN * 3;
            }

            if (super.f % 2 == 1) {
               Point var1 = new Point(super.objFireMain.x - super.objFireMain.vx / 2, super.objFireMain.y);
               this.VecEff.addElement(var1);
            }
         } else {
            super.objFireMain.vx = 0;
         }

         for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
            Point var2;
            ++(var2 = (Point)this.VecEff.elementAt(var3)).f;
            if (var2.f / 2 >= 3) {
               this.VecEff.removeElement(var2);
               --var3;
            }
         }

         if (super.f == 6) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.setAva(2, this.objBeFireMain);
            byte var4 = 20;
            if (super.Dir == 0) {
               var4 = -20;
            }

            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var4, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
         }

      } else {
         this.removeEff();
         if (super.objFireMain == GameScreen.player) {
            GameScreen.AB(false);
         }

      }
   }

   private void updateNami5() {
      if (!this.checkNullObject((int)1)) {
         if (super.objFireMain.Dir == 0) {
            super.x = this.x1000 - 20;
         } else {
            super.x = this.x1000 + 20;
         }
      }

      int var5;
      if (super.f > 5 && (super.typeEffect == 55 || super.typeEffect == 31 || super.f >= 10) && super.f % 3 == 0 && super.f <= super.fRemove) {
         int var3;
         if (this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var2;
            if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
               var3 = var2.hOne / 2;
               if (super.typeEffect == 56 || super.typeEffect == 191 || super.typeEffect == 223) {
                  var3 = var2.hOne + 20;
               }

               var5 = var2.x - super.x;
               var3 = var2.y - var3 - super.y;
               Point_Focus var4 = new Point_Focus();
               (var4 = this.create_Speed(var5, var3, var4)).AR = var2;
               this.VecEff.addElement(var4);
            }
         } else if (super.typeEffect == 223 && !GameCanvas.lowGraphic) {
            int var7 = CRes.random_Am_0(100);
            var3 = -50 + CRes.random_Am_0(60);
            Point_Focus var6 = new Point_Focus();
            var6 = this.create_Speed(var7, var3, var6);
            this.VecEff.addElement(var6);
         }
      }

      for(var5 = 0; var5 < this.VecEff.size(); ++var5) {
         Point_Focus var8;
         (var8 = (Point_Focus)this.VecEff.elementAt(var5)).update_Vx_Vy();
         if (var8.AG >= var8.fRe) {
            float var10000;
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.typeEffect == 31) {
               GameScreen.addEffectEnd((short)38, 0, var8.x, var8.y, super.Dir, super.objMainEff);
            } else if (super.typeEffect == 55) {
               GameScreen.addEffectEnd((short)41, 0, var8.x, var8.y, super.Dir, super.objMainEff);
            } else if (super.typeEffect == 56 || super.typeEffect == 191 || super.typeEffect == 191 || super.typeEffect == 223) {
               if (this.isAddSound) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               this.addVir(5, 5, 10, true);
               byte var9 = 0;
               if (super.typeEffect == 191) {
                  var9 = 1;
               } else if (super.typeEffect == 223) {
                  var9 = 2;
               }

               if (var8.AR == null) {
                  GameScreen.addEffectEnd((short)39, var9, var8.x, var8.y, super.Dir, super.objMainEff);
               } else {
                  GameScreen.addEffectEnd_ObjTo((short)39, var9, var8.AR.x, var8.AR.y - var8.AR.hOne - 20, (short)var8.AR.ID, (byte)var8.AR.typeObject, (byte)0, super.objMainEff);
               }
            }

            GameScreen.addEffectEnd((short)93, 1, var8.x, var8.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var8);
            --var5;
         }
      }

      if (super.f > super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateNami6() {
      if (!this.checkNullObject((int)1)) {
         if (super.objFireMain.Dir == 0) {
            super.x = this.x1000 - 20;
         } else {
            super.x = this.x1000 + 20;
         }
      }

      Point_Focus var1;
      int var3;
      if (super.f == 10) {
         var3 = -(super.objFireMain.hOne + 50);
         var1 = new Point_Focus();
         var1 = this.create_Speed(0, var3, var1);
         this.VecEff.addElement(var1);
      }

      int var4;
      if (super.f >= 10 && super.f <= 19) {
         var4 = super.objFireMain.hOne + 50;
         int var2 = 100 * CRes.getcos((super.f - 10) * 360 / 10) / 1000;
         var3 = -var4 + 30 * CRes.getsin((super.f - 10) * 360 / 10) / 1000;
         var1 = new Point_Focus();
         (var1 = this.create_Speed(var2, var3, var1, super.x, super.y, var2 - super.x, var3 - super.y)).AR = super.objFireMain;
         this.VecEff.addElement(var1);
      }

      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var5;
         (var5 = (Point_Focus)this.VecEff.elementAt(var4)).update_Vx_Vy();
         if (var5.AG >= var5.fRe) {
            float var10000;
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            byte var7 = 2;
            if (var5.AR == null) {
               var7 = 3;
            }

            GameScreen.addEffectEnd((short)39, var7, var5.x, var5.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 1, var5.x, var5.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var5);
            --var4;
         }
      }

      if (super.f > super.fRemove && this.VecEff.size() == 0) {
         for(var4 = 0; var4 < super.vecObjsBeFire.size(); ++var4) {
            Object_Effect_Skill var6;
            MainObject var8;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var4)) != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               GameScreen.addEffectEnd((short)42, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)41, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)8, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)108, 8, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
            }
         }

         this.removeEff();
      }

   }

   private void update_Nami_S3_L7() {
      if (!this.checkNullObject((int)1)) {
         if (super.objFireMain.Dir == 0) {
            super.x = this.x1000 - 20;
         } else {
            super.x = this.x1000 + 20;
         }
      }

      int var3;
      Point_Focus var4;
      if (super.f == 10) {
         var3 = -(super.objFireMain.hOne + 50);
         var4 = new Point_Focus();
         var4 = this.create_Speed(0, var3, var4);
         this.VecEff.addElement(var4);
      }

      int var1;
      if (super.f >= 10 && super.f <= 19) {
         var1 = super.objFireMain.hOne + 25;
         int var2 = 100 * CRes.getcos((super.f - 10) * 360 / 10) / 1000;
         var3 = -var1 + 30 * CRes.getsin((super.f - 10) * 360 / 10) / 1000;
         var4 = new Point_Focus();
         (var4 = this.create_Speed(var2, var3, var4, super.x, super.y, var2 - super.x, var3 - super.y)).AR = super.objFireMain;
         var4.frame = 0;
         this.VecEff.addElement(var4);
         var1 += 25;
         var2 = 150 * CRes.getcos((super.f - 10) * 360 / 10) / 1000;
         var3 = -var1 + 30 * CRes.getsin((super.f - 10) * 360 / 10) / 1000;
         var4 = new Point_Focus();
         (var4 = this.create_Speed(var2, var3, var4, super.x, super.y, var2 - super.x, var3 - super.y)).frame = 1;
         var4.AR = super.objFireMain;
         this.VecEff.addElement(var4);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var5;
         (var5 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var5.AG >= var5.fRe) {
            float var10000;
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            byte var7 = 2;
            if (var5.AR == null) {
               var7 = 3;
            }

            if (var5.frame == 0) {
               GameScreen.addEffectEnd((short)39, var7, var5.x, var5.y, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)185, var7, var5.x, var5.y, super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)93, 1, var5.x, var5.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var5);
            --var1;
         }
      }

      if (super.f > super.fRemove && this.VecEff.size() == 0) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var6;
            MainObject var8;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               GameScreen.addEffectEnd((short)42, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)41, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)8, 0, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
               GameScreen.addEffectEnd((short)108, 8, var8.x, var8.y - var8.hOne / 2, super.Dir, var8);
            }
         }

         this.removeEff();
      }

   }

   private void updateNami4() {
      if (super.f == 8 && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         if (super.f > 4 && super.f % 5 == 0 && !this.checkNullObject((int)2)) {
            if (super.typeEffect == 16) {
               this.setAva(0, this.objBeFireMain);
               GameScreen.addEffectEnd((short)3, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            } else if (super.typeEffect == 51) {
               this.setAva(1, this.objBeFireMain);
               GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)93, 1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         }

      }
   }

   private void updateZoro8() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         super.objFireMain.vx = super.vx;
         super.objFireMain.vy = super.vy;
         if (LoadMap.AA(GameCanvas.loadmap.AA(super.objFireMain.x + super.objFireMain.vx, super.objFireMain.y + super.objFireMain.vy))) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.isTanHinh = false;
            if (super.objFireMain.plashNow != null) {
               super.objFireMain.plashNow.AA((byte)0);
            }
         }

         byte var1 = 30;
         if (super.Dir == 0) {
            var1 = -30;
         }

         if (!this.checkNullObject((int)1)) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.typeEffect == 29) {
               this.setAva(2, this.objBeFireMain);
               GameScreen.addEffectEnd((short)26, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)19, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
            }

            GameScreen.addEffectEnd((short)93, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }
   }

   private void updateUssopSkill1_Lv3() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      float var10000;
      if ((super.f == 0 || super.f == 3) && this.isAddSound) {
         var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (var2.frame == 0) {
               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(20), super.toY + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)35, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            } else if (var2.frame == 1) {
               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(20), super.toY + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)35, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            }

            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 3) {
         var1 = super.toX - super.x;
         int var4 = super.toY - super.y;
         Point_Focus var3 = new Point_Focus();
         (var3 = this.create_Speed(var1, var4, var3)).frame = 1;
         GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
         this.VecEff.addElement(var3);
      }

   }

   private void updateUssopSkill1_Lv3_New() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)1)) {
         this.removeEff();
      }

      if ((super.f == 0 || super.f == 3 || super.f == 10 || super.f == 13 || super.f == 20 || super.f == 23) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.addVir(5, 5, 10, true);
            if (var2.frame == 0) {
               GameScreen.addEffectEnd((short)1, 0, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            } else if (var2.frame == 1) {
               GameScreen.addEffectEnd((short)1, 0, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            }

            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 5 || super.f == 15) {
         super.objFireMain.isTanHinh = true;
      }

      if (super.f == 7) {
         MainObject var7 = super.objFireMain;
         var7.x -= super.am_duong * 10;
         var7 = super.objFireMain;
         var7.y += CRes.random_Am(1, 2) * 20;
         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         super.x = super.objFireMain.x;
         super.y -= 6;
         if (super.Dir == 0) {
            super.x -= 30;
         } else {
            super.x += 30;
         }
      }

      if (super.f == 9 || super.f == 19) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 17) {
         super.objFireMain.x = this.CJ;
         super.objFireMain.y = this.CK;
         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         super.x = super.objFireMain.x;
         super.y -= 6;
         if (super.Dir == 0) {
            super.x -= 30;
         } else {
            super.x += 30;
         }
      }

      if ((super.f == 3 || super.f == 10 || super.f == 13 || super.f == 20 || super.f == 13) && !this.checkNullObject((int)3)) {
         byte var5 = 30;
         if (super.Dir == 0) {
            var5 = -30;
         }

         int var6 = this.objBeFireMain.x - (super.objFireMain.x + var5);
         int var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - (super.objFireMain.y - super.objFireMain.hOne / 2);
         Point_Focus var4 = new Point_Focus();
         (var4 = this.create_Speed(var6, var3, var4, super.objFireMain.x + var5, super.objFireMain.y - super.objFireMain.hOne / 2, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).frame = 1;
         GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
         this.VecEff.addElement(var4);
      }

   }

   private void updateUssopSkill1_Lv3_SHORT() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)1)) {
         this.removeEff();
      }

      if ((super.f == 0 || super.f == 3 || super.f == 10 || super.f == 13) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.addVir(5, 5, 10, true);
            if (super.typeEffect == 192) {
               GameScreen.addEffectEnd((short)25, 4, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 7, var2.AK, var2.AL, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)1, 0, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
               if (var2.frame == 2) {
                  GameScreen.addEffectEnd((short)108, 5, var2.AK, var2.AL, super.Dir, super.objMainEff);
               }
            }

            GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 5 || super.f == 14) {
         super.objFireMain.isTanHinh = true;
      }

      if (super.f == 7) {
         MainObject var7 = super.objFireMain;
         var7.x -= super.am_duong * 10;
         var7 = super.objFireMain;
         var7.y += CRes.random_Am(1, 2) * 20;
         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         super.x = super.objFireMain.x;
         super.y -= 6;
         if (super.Dir == 0) {
            super.x -= 30;
         } else {
            super.x += 30;
         }
      }

      if (super.f == 15) {
         super.objFireMain.x = this.CJ;
         super.objFireMain.y = this.CK;
         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         super.x = super.objFireMain.x;
         super.y -= 6;
         if (super.Dir == 0) {
            super.x -= 30;
         } else {
            super.x += 30;
         }
      }

      if (super.f == 9 || super.f == 15) {
         super.objFireMain.isTanHinh = false;
      }

      if ((super.f == 3 || super.f == 10 || super.f == 13) && !this.checkNullObject((int)3)) {
         byte var5 = 30;
         if (super.Dir == 0) {
            var5 = -30;
         }

         int var6 = this.objBeFireMain.x - (super.objFireMain.x + var5);
         int var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - (super.objFireMain.y - super.objFireMain.hOne / 2);
         Point_Focus var4 = new Point_Focus();
         (var4 = this.create_Speed(var6, var3, var4, super.objFireMain.x + var5, super.objFireMain.y - super.objFireMain.hOne / 2, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).frame = 1;
         if (super.f == 10) {
            var4.frame = 2;
         }

         GameScreen.addEffectEnd((short)1, 0, super.x, super.y, super.Dir, super.objMainEff);
         this.VecEff.addElement(var4);
      }

   }

   private void update_Ussop_S1_L5() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)1)) {
         if (super.objFireMain != null) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

      if ((super.f == 2 || super.f == 6 || super.f == 10 || super.f == 14) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.addVir(5, 5, 10, true);
            byte var3 = 4;
            if (var2.frame == 2) {
               var3 = 3;
            }

            GameScreen.addEffectEnd((short)25, var3, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 7, var2.AK, var2.AL, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 2) {
         super.objFireMain.isTanHinh = true;
      } else if (super.f == 15) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f > 3 && super.f % 2 == 0 && !this.checkNullObject((int)3)) {
         byte var6 = 25;
         int var7;
         if ((var7 = (super.f - 2) / 2) >= this.CM.length) {
            return;
         }

         if (super.Dir == 0) {
            var6 = -25;
         }

         int var8 = super.objFireMain.x + this.CM[var7][0] - this.objBeFireMain.x + var6;
         int var4 = super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0] - (this.objBeFireMain.y - this.objBeFireMain.hOne / 2);
         Point_Focus var5 = new Point_Focus();
         (var5 = this.create_Speed(var8, var4, var5, super.objFireMain.x + this.CM[var7][0] + var6, super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0], this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).frame = 1;
         if (super.f == 8 || super.f == 14) {
            var5.frame = 2;
         }

         GameScreen.addEffectEnd((short)1, 0, super.objFireMain.x + this.CM[var7][0] + var6, super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0] - 10, super.Dir, super.objMainEff);
         this.VecEff.addElement(var5);
      }

      if (super.typeEffect == 301) {
         for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
            if (super.f > var1 << 2) {
               ((Point_Focus)this.VecSubEff.elementAt(var1)).update_Vx_Vy();
            }
         }
      }

   }

   private void update_Ussop_S1_L7() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)1)) {
         if (super.objFireMain != null) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

      if ((super.f == 2 || super.f == 6 || super.f == 10 || super.f == 14) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            this.addVir(5, 5, 10, true);
            byte var3 = 4;
            if (var2.frame == 2) {
               var3 = 3;
            }

            GameScreen.addEffectEnd((short)25, var3, var2.AK + CRes.random_Am_0(20), var2.AL + CRes.random_Am_0(20), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 7, var2.AK, var2.AL, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)35, 0, var2.AK, var2.AL, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 2) {
         super.objFireMain.isTanHinh = true;
      } else if (super.f == 15) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f > 3 && super.f % 2 == 0 && !this.checkNullObject((int)3)) {
         byte var6 = 25;
         int var7;
         if ((var7 = (super.f - 2) / 2) >= this.CM.length) {
            return;
         }

         if (super.Dir == 0) {
            var6 = -25;
         }

         int var8 = super.objFireMain.x + this.CM[var7][0] - this.objBeFireMain.x + var6;
         int var4 = super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0] - (this.objBeFireMain.y - this.objBeFireMain.hOne / 2);
         Point_Focus var5 = new Point_Focus();
         (var5 = this.create_Speed(var8, var4, var5, super.objFireMain.x + this.CM[var7][0] + var6, super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0], this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).frame = 1;
         if (super.f == 8 || super.f == 14) {
            var5.frame = 2;
         }

         GameScreen.addEffectEnd((short)1, 0, super.objFireMain.x + this.CM[var7][0] + var6, super.objFireMain.y - super.objFireMain.hOne / 2 + this.CM[var7][0] - 10, super.Dir, super.objMainEff);
         this.VecEff.addElement(var5);
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         if (super.f > var1 << 2) {
            ((Point_Focus)this.VecSubEff.elementAt(var1)).update_Vx_Vy();
         }
      }

   }

   private void update_Nami_S1_L3() {
      int var5;
      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         float var10000;
         if (this.isAddSound && super.f == 8) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if ((super.f == 5 || super.f == 15) && this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f == 5 && super.typeEffect == 311) {
            GameScreen.addEffectEnd((short)174, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, this.objBeFireMain);
         }

         if (super.f > 4 && super.f % 5 == 0 && this.objBeFireMain != null) {
            this.addVir(5, 5, 10, true);
            this.setAva(1, this.objBeFireMain);
            byte var1 = 1;
            if ((super.typeEffect == 221 || super.typeEffect == 311) && CRes.random(2) == 0) {
               var1 = 3;
            }

            GameScreen.addEffectEnd((short)38, var1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            if (super.f == 10 || super.typeEffect == 221 || super.typeEffect == 311) {
               var1 = 3;
               if ((super.typeEffect == 221 || super.typeEffect == 311) && CRes.random(2) == 0) {
                  var1 = 8;
               }

               GameScreen.addEffectEnd((short)108, var1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            }
         }

         if ((super.typeEffect == 189 || super.typeEffect == 221 || super.typeEffect == 311) && super.f > 4 && super.f % 3 == 0 && this.objBeFireMain != null) {
            short var4 = 38;
            if ((super.typeEffect == 221 || super.typeEffect == 311) && CRes.random(2) == 0) {
               var4 = 138;
            }

            GameScreen.addEffectEnd(var4, 2, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         }

         if (super.objFireMain != null && !GameCanvas.lowGraphic) {
            var5 = super.x - 20;
            if (super.Dir == 0) {
               var5 = super.x + 20;
            }

            int var2 = 25;
            if (super.objFireMain.hOne > 1) {
               var2 = super.objFireMain.hOne / 2;
            }

            Point var6 = new Point(var5 + CRes.random_Am_0(20), super.y + CRes.random_Am_0(var2));
            if ((super.typeEffect == 221 || super.typeEffect == 311) && CRes.random(2) == 0) {
               var6.frame = 1;
            }

            this.VecEff.addElement(var6);
         }
      }

      for(var5 = 0; var5 < this.VecEff.size(); ++var5) {
         Point var3;
         ++(var3 = (Point)this.VecEff.elementAt(var5)).f;
         if (var3.f >= 4) {
            this.VecEff.removeElement(var3);
            --var5;
         }
      }

   }

   private void update_Nami_S1_L7() {
      int var5;
      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         float var10000;
         if (this.isAddSound && super.f == 8) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if ((super.f == 5 || super.f == 15) && this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f == 4) {
            GameScreen.addEffectEnd((short)138, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, this.objBeFireMain);
         } else if (super.f == 3) {
            GameScreen.addEffectEnd((short)38, 2, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, this.objBeFireMain);
         } else if (super.f == 2) {
            GameScreen.addEffectEnd((short)38, 1, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, this.objBeFireMain);
         } else if (super.f == 1) {
            GameScreen.addEffectEnd((short)38, 3, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, this.objBeFireMain);
         } else if (super.f == 6 || super.f == 10 || super.f == 14 || super.f == 18) {
            this.objBeFireMain.x += super.am_duong * 18;
            this.setAva(2, this.objBeFireMain);
         }

         if (super.f > 4 && super.f % 5 == 0 && this.objBeFireMain != null) {
            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            byte var1 = 1;
            if (CRes.random(2) == 0) {
               var1 = 3;
            }

            GameScreen.addEffectEnd((short)38, var1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            var1 = 3;
            if (CRes.random(2) == 0) {
               var1 = 8;
            }

            GameScreen.addEffectEnd((short)108, var1, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         }

         if (super.f > 4 && super.f % 3 == 0 && this.objBeFireMain != null) {
            short var4 = 38;
            if (CRes.random(2) == 0) {
               var4 = 138;
            }

            GameScreen.addEffectEnd(var4, 2, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         }

         if (super.objFireMain != null && !GameCanvas.lowGraphic) {
            var5 = super.x - 20;
            if (super.Dir == 0) {
               var5 = super.x + 20;
            }

            int var2 = 25;
            if (super.objFireMain.hOne > 1) {
               var2 = super.objFireMain.hOne / 2;
            }

            Point var6 = new Point(var5 + CRes.random_Am_0(20), super.y + CRes.random_Am_0(var2));
            if (CRes.random(2) == 0) {
               var6.frame = 1;
            }

            this.VecEff.addElement(var6);
         }
      }

      for(var5 = 0; var5 < this.VecEff.size(); ++var5) {
         Point var3;
         ++(var3 = (Point)this.VecEff.elementAt(var5)).f;
         if (var3.f >= 4) {
            this.VecEff.removeElement(var3);
            --var5;
         }
      }

   }

   private void updateSanjiSkill3_Lv1() {
      int var4;
      if (super.f >= 4) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.objFireMain != null && CRes.random(2) == 0) {
            super.objFireMain.dx = CRes.random_Am_0(2);
            super.AZ = super.objFireMain.dx;
         }

         if (super.f % 2 == 0 && this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var2;
            if (var1 != null && (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)) != null) {
               var4 = var2.x - super.x;
               int var5 = var2.y - super.objFireMain.hOne / 2 - super.y;
               Point_Focus var3 = new Point_Focus();
               (var3 = this.create_Speed(var4, var5, var3)).frame = CRes.random(6);
               this.VecEff.addElement(var3);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (super.objFireMain != null) {
            super.objFireMain.dx = 0;
         }

         this.removeEff();
      }

      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var6;
         (var6 = (Point_Focus)this.VecEff.elementAt(var4)).update_Vx_Vy();
         if (var6.AG >= var6.fRe) {
            if (super.typeEffect == 49) {
               GameScreen.addEffectEnd((short)1, 0, var6.x, var6.y, super.Dir, super.objMainEff);
            } else if (super.typeEffect == 50) {
               GameScreen.addEffectEnd((short)35, 0, var6.x, var6.y, super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)93, 2, var6.x, var6.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var6);
            --var4;
         }
      }

   }

   private void updateLuffyS1() {
      if (this.objBeFireMain != null && this.objBeFireMain.hOne > 0 && super.f % 5 == 0) {
         byte var1 = 0;
         if (super.typeEffect == 33) {
            var1 = 2;
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         this.setAva(var1, this.objBeFireMain);
         if (!this.checkNullObject((int)1)) {
            byte var2 = 28;
            if (super.objFireMain.Dir == 0) {
               var2 = -28;
            }

            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.typeEffect == 176) {
               GameScreen.addEffectEnd((short)114, 0, super.objFireMain.x + var2 - (super.am_duong << 3), super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 5, (byte)super.objFireMain.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)25, var1, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)93, 0, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            }
         }
      }

      if (super.f >= super.fRemove) {
         if (super.typeEffect == 176 && !this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateLuffyS1_NEW() {
      float var10000;
      byte var1;
      if (super.f < 20 && super.f % 5 == 0) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)2)) {
            MainEffect.AB(-6, this.objBeFireMain);
            if (this.objBeFireMain.typeObject == 1 && this.objBeFireMain.Action != 4) {
               this.objBeFireMain.Action = 3;
            }

            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)25, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }
      }

      if (super.f == 20) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = -15;
         if (super.Dir == 0) {
            var1 = 15;
         }

         GameScreen.addEffectEnd((short)171, 0, super.x + var1, super.y, 450, super.Dir, super.objMainEff);
      }

      if (super.f == 32) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(5, 5, 10, true);
         this.setAva(2, this.objBeFireMain);
         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)54, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateLuffyS1_L3_SHORT() {
      float var10000;
      byte var1;
      if (super.f == 4) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)2)) {
            MainEffect.AB(-6, this.objBeFireMain);
            if (this.objBeFireMain.typeObject == 1 && this.objBeFireMain.Action != 4) {
               this.objBeFireMain.Action = 3;
            }

            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)25, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }
      }

      if (super.f == 5) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = -15;
         if (super.Dir == 0) {
            var1 = 15;
         }

         GameScreen.addEffectEnd((short)30, 0, super.x + var1, super.y, 150, super.Dir, super.objMainEff);
      }

      label148: {
         int var10001;
         byte var5;
         if (super.typeEffect == 83) {
            if (super.f != 15) {
               break label148;
            }

            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            var1 = 28;
            if (super.Dir == 0) {
               var1 = -28;
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            if (this.checkNullObject((int)1)) {
               break label148;
            }

            GameScreen.addEffectEnd((short)25, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            var5 = 54;
            var10001 = 0;
         } else {
            if (super.typeEffect == 180) {
               if (super.f == 13 || super.f == 17) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  this.addVir(5, 5, 10, true);
                  this.setAva(2, this.objBeFireMain);
                  var1 = 28;
                  if (super.Dir == 0) {
                     var1 = -28;
                  }

                  if (!this.checkNullObject((int)2)) {
                     GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
                     GameScreen.addEffectEnd((short)108, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
                  }

                  if (!this.checkNullObject((int)1)) {
                     GameScreen.addEffectEnd((short)25, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
                     GameScreen.addEffectEnd((short)54, super.f == 13 ? 7 : 6, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
                  }
               }
               break label148;
            }

            if (super.typeEffect != 212) {
               break label148;
            }

            for(int var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
               Point var2;
               (var2 = (Point)this.VecSubEff.elementAt(var3)).update();
               if (var2.f >= var2.fRe) {
                  this.VecSubEff.removeElement(var2);
                  --var3;
               }
            }

            if (super.f < super.fRemove && super.f % 3 == 0 && !GameCanvas.lowGraphic) {
               Point var4;
               (var4 = new Point()).x = super.x + CRes.random_Am_0(15);
               var4.y = super.y + 15 + CRes.random_Am_0(5);
               var4.vx = CRes.random_Am_0(2);
               var4.vy = -CRes.random(1, 4);
               var4.fRe = CRes.random(10, 14);
               this.VecSubEff.addElement(var4);
            }

            if (super.f != 13 && super.f != 17) {
               break label148;
            }

            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            var1 = 28;
            if (super.Dir == 0) {
               var1 = -28;
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            if (this.checkNullObject((int)1)) {
               break label148;
            }

            GameScreen.addEffectEnd((short)25, 2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            var5 = 54;
            var10001 = super.f == 13 ? 7 : 9;
         }

         GameScreen.addEffectEnd(var5, var10001, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && (super.typeEffect != 212 || this.VecSubEff.size() == 0)) {
         this.removeEff();
      }

   }

   private void update_Luffy_S1_L6() {
      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

      float var10000;
      byte var1;
      if (super.f == 1) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = -15;
         if (super.Dir == 0) {
            var1 = 15;
         }

         GameScreen.addEffectEnd((short)171, 0, super.x + var1, super.y, 450, super.Dir, super.objMainEff);
      }

      if (super.f == 10) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(5, 5, 10, true);
         this.setAva(2, this.objBeFireMain);
         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 5, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)54, 5, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)119, 3, super.objFireMain.x + super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
      }

      if (super.f == 14) {
         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)54, 6, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }
      }

      for(int var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var3)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var3;
         }
      }

      if (super.f < super.fRemove && super.f % 3 == 0 && !GameCanvas.lowGraphic) {
         Point var4;
         (var4 = new Point()).x = super.x + CRes.random_Am_0(15);
         var4.y = super.y + 15 + CRes.random_Am_0(5);
         var4.vx = CRes.random_Am_0(2);
         var4.vy = -CRes.random(1, 4);
         var4.fRe = CRes.random(10, 14);
         this.VecSubEff.addElement(var4);
      }

   }

   private void update_Luffy_S1_L7() {
      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

      float var10000;
      byte var1;
      if (super.f == 0) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         var1 = -15;
         if (super.Dir == 0) {
            var1 = 15;
         }

         GameScreen.addEffectEnd((short)171, 1, super.x + var1, super.y, 450, super.Dir, super.objMainEff);
      }

      if (super.f == 10) {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(5, 5, 10, true);
         this.setAva(2, this.objBeFireMain);
         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 5, this.objBeFireMain.x, this.objBeFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)54, 5, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)182, 3, super.objFireMain.x + super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
      }

      if (super.f == 14) {
         var1 = 28;
         if (super.Dir == 0) {
            var1 = -28;
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)54, 6, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }
      }

      for(int var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
         Point var2;
         (var2 = (Point)this.VecSubEff.elementAt(var3)).update();
         if (var2.f >= var2.fRe) {
            this.VecSubEff.removeElement(var2);
            --var3;
         }
      }

      if (super.f < super.fRemove && super.f % 3 == 0 && !GameCanvas.lowGraphic) {
         Point var4;
         (var4 = new Point()).x = super.x + CRes.random_Am_0(15);
         var4.y = super.y + 15 + CRes.random_Am_0(5);
         var4.vx = CRes.random_Am_0(2);
         var4.vy = -CRes.random(1, 4);
         var4.fRe = CRes.random(10, 14);
         this.VecSubEff.addElement(var4);
      }

   }

   private void updateXaPhong() {
      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         (var2 = (Point)this.VecSubEff.elementAt(var1)).update();
         if (var2.f >= var2.fRe) {
            int var3 = CRes.random(3) + 1;
            if (super.typeEffect == 274) {
               var3 = 2;
            }

            if (var3 == 2) {
               GameScreen.addEffectEnd((short)71, 0, var2.x, var2.y, super.Dir, super.objMainEff);
               if (CRes.random(4) == 0) {
                  GameScreen.addEffectEnd((short)108, 4, var2.x, var2.y, super.Dir, super.objMainEff);
               }
            } else {
               GameScreen.addEffectEnd((short)38, var3, var2.x, var2.y, super.Dir, super.objMainEff);
               if (CRes.random(4) == 0) {
                  if (var3 == 1) {
                     GameScreen.addEffectEnd((short)108, 3, var2.x, var2.y, super.Dir, super.objMainEff);
                  } else {
                     GameScreen.addEffectEnd((short)108, 8, var2.x, var2.y, super.Dir, super.objMainEff);
                  }
               }
            }

            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f < super.fRemove && !GameCanvas.lowGraphic) {
         for(var1 = 0; var1 < 2; ++var1) {
            (var2 = new Point()).x = this.objBeFireMain.x + CRes.random_Am_0(15);
            var2.y = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10);
            var2.vx = CRes.random_Am_0(4);
            var2.vy = CRes.random_Am_0(5);
            var2.fRe = CRes.random(10, 14);
            this.VecSubEff.addElement(var2);
         }
      }

   }

   private void updateMorgan_1() {
      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            byte var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -10, super.Dir, super.objMainEff);
            var1 = 13;
            if (super.Dir == 0) {
               var1 = -13;
            }

            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -10, super.Dir, super.objMainEff);
            var1 = 5;
            if (super.Dir == 0) {
               var1 = -5;
            }

            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -10, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)2)) {
            this.addVir(3, 5, 10, false);
            this.setAva(1, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateMorgan_2() {
      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            byte var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            GameScreen.addEffectEnd((short)16, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -10, super.Dir, super.objMainEff);
            var1 = 15;
            if (super.Dir == 0) {
               var1 = -13;
            }

            GameScreen.addEffectEnd((short)16, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -7, super.Dir, super.objMainEff);
            var1 = 10;
            if (super.Dir == 0) {
               var1 = -5;
            }

            GameScreen.addEffectEnd((short)16, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + -4, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)2)) {
            this.setAva(1, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateZoroS2_New() {
      float var10000;
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if ((super.f <= 12 || super.f >= 20) && (super.f <= 22 || super.f >= 26) && (super.f <= 28 || super.f >= 32) && (super.f <= 34 || super.f >= 38)) {
            super.objFireMain.isTanHinh = false;
         } else {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 17) {
            super.objFireMain.y = this.objBeFireMain.y;
            this.DN = 8;
         }

         if (super.f < 20 && super.f >= 17) {
            MainObject var8 = this.objBeFireMain;
            var8.dy += this.DN;
            this.DN /= 2;
         }

         if (super.f >= 20 && super.f < 26) {
            this.DN = 0;
            this.objBeFireMain.dy = 20;
            super.objFireMain.dy = 15;
         }

         byte var1;
         byte var2;
         if (super.f == 20) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            var1 = 2;
            if (super.Dir == 0) {
               var1 = -2;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            var2 = 1;
            if (super.typeEffect == 184 || super.typeEffect == 216) {
               var2 = -1;
               var1 = 10;
               if (super.Dir == 0) {
                  var1 = -10;
               }
            }

            GameScreen.addEffectEnd((short)16, var2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            if (super.typeEffect == 216) {
               GameScreen.addEffectEnd((short)136, 0, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            }
         }

         if (super.f >= 26 && super.f < 32) {
            this.objBeFireMain.dy = 30;
            super.objFireMain.dy = 25;
         }

         if (super.f == 26) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 30;
            if (super.Dir == 0) {
               var1 = -30;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            var2 = 2;
            if (super.typeEffect == 184 || super.typeEffect == 216) {
               var2 = -2;
               var1 = 15;
               if (super.Dir == 0) {
                  var1 = -15;
               }
            }

            GameScreen.addEffectEnd((short)16, var2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 32) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            this.objBeFireMain.dy = 40;
            super.objFireMain.dy = 35;
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            var2 = 1;
            if (super.typeEffect == 184 || super.typeEffect == 216) {
               var2 = -1;
               var1 = 10;
               if (super.Dir == 0) {
                  var1 = -10;
               }
            }

            GameScreen.addEffectEnd((short)16, var2, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 38) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            Point_Focus var6 = new Point_Focus();
            var2 = 20;
            if (super.Dir == 0) {
               var2 = -20;
            }

            int var5 = super.toX;
            int var4 = super.toY;
            super.toX = super.x;
            super.toY = super.y;
            super.x = var5;
            super.y = var4;
            var5 = super.toX - (super.x - var2);
            var4 = super.toY - super.y;
            super.objFireMain.x = super.x - var2;
            super.objFireMain.y = super.y;
            super.objFireMain.dy = 0;
            this.objBeFireMain.dy = 0;
            this.create_Speed(var5, var4, var6);
            super.objFireMain.vx = var6.AI;
            super.objFireMain.vy = -var6.vy;
            super.objFireMain.toX = var6.AK;
            super.objFireMain.toY = var6.AL;
         }

         if (super.f > 38 && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, super.objFireMain.toX, super.objFireMain.toY) < super.vMax) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
         }

      } else {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            this.setAva(2, this.objBeFireMain);
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            byte var7 = super.Dir;
            byte var3 = 0;
            if (super.typeEffect == 184) {
               var3 = 2;
            } else if (super.typeEffect == 216) {
               var3 = 3;
            }

            GameScreen.addEffectEnd((short)26, var3, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S2_L6() {
      float var10000;
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if ((super.f <= 12 || super.f >= 20) && (super.f <= 22 || super.f >= 26) && (super.f <= 28 || super.f >= 32) && (super.f <= 34 || super.f >= 38)) {
            super.objFireMain.isTanHinh = false;
         } else {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 17) {
            super.objFireMain.y = this.objBeFireMain.y;
            this.DN = 8;
         }

         if (super.f < 20 && super.f >= 17) {
            MainObject var9 = this.objBeFireMain;
            var9.dy += this.DN;
            this.DN /= 2;
         }

         if (super.f >= 20 && super.f < 26) {
            this.DN = 0;
            this.objBeFireMain.dy = 20;
            super.objFireMain.dy = 15;
         }

         byte var1;
         if (super.f == 10) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f >= 16 && super.f < 22) {
            this.objBeFireMain.dy = 30;
            super.objFireMain.dy = 25;
         }

         if (super.f == 16) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 30;
            if (super.Dir == 0) {
               var1 = -30;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(0, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f == 22) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            this.objBeFireMain.dy = 40;
            super.objFireMain.dy = 35;
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f == 28) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            Point_Focus var6 = new Point_Focus();
            byte var2 = 20;
            if (super.Dir == 0) {
               var2 = -20;
            }

            int var3 = super.toX;
            int var4 = super.toY;
            super.toX = super.x;
            super.toY = super.y;
            super.x = var3;
            super.y = var4;
            var3 = super.toX - (super.x - var2);
            var4 = super.toY - super.y;
            super.objFireMain.x = super.x - var2;
            super.objFireMain.y = super.y;
            super.objFireMain.dy = 0;
            this.objBeFireMain.dy = 0;
            this.create_Speed(var3, var4, var6);
            super.objFireMain.vx = var6.AI;
            super.objFireMain.vy = -var6.vy;
            super.objFireMain.toX = var6.AK;
            super.objFireMain.toY = var6.AL;
         }

         if (super.f > 28 && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, super.objFireMain.toX, super.objFireMain.toY) < super.vMax) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
         }

         for(int var7 = 0; var7 < this.VecEff.size(); ++var7) {
            Point var5;
            (var5 = (Point)this.VecEff.elementAt(var7)).update();
            if (var5.f >= var5.fRe) {
               this.VecEff.removeElement(var5);
               --var7;
            }
         }

      } else {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            this.setAva(2, this.objBeFireMain);
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            byte var8 = super.Dir;
            GameScreen.addEffectEnd((short)26, 4, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S2_L7() {
      float var10000;
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if (super.mframe[super.f] > -2) {
            super.objFireMain.isTanHinh = true;
         } else {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 14) {
            super.objFireMain.y = this.objBeFireMain.y;
            this.DN = 8;
         }

         if (super.f < 18 && super.f >= 14) {
            MainObject var9 = this.objBeFireMain;
            var9.dy += this.DN;
            this.DN /= 2;
         }

         if (super.f >= 18 && super.f < 26) {
            this.DN = 0;
            this.objBeFireMain.dy = 20;
            super.objFireMain.dy = 15;
         }

         byte var1;
         if (super.f == 10) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f >= 14 && super.f < 22) {
            this.objBeFireMain.dy = 30;
            super.objFireMain.dy = 25;
         }

         if (super.f == 16) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 30;
            if (super.Dir == 0) {
               var1 = -30;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(0, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f == 22) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX - var1;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            this.objBeFireMain.dy = 40;
            super.objFireMain.dy = 35;
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            this.AB(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
         }

         if (super.f == 28) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            Point_Focus var6 = new Point_Focus();
            byte var2 = 20;
            if (super.Dir == 0) {
               var2 = -20;
            }

            int var3 = super.toX;
            int var4 = super.toY;
            super.toX = super.x;
            super.toY = super.y;
            super.x = var3;
            super.y = var4;
            var3 = super.toX - (super.x - var2);
            var4 = super.toY - super.y;
            super.objFireMain.x = super.x - var2;
            super.objFireMain.y = super.y;
            super.objFireMain.dy = 0;
            this.objBeFireMain.dy = 0;
            this.create_Speed(var3, var4, var6);
            super.objFireMain.vx = var6.AI;
            super.objFireMain.vy = -var6.vy;
            super.objFireMain.toX = var6.AK;
            super.objFireMain.toY = var6.AL;
         }

         if (super.f > 28 && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, super.objFireMain.toX, super.objFireMain.toY) < super.vMax) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
         }

         int var7;
         for(var7 = 0; var7 < this.VecEff.size(); ++var7) {
            Point var5;
            (var5 = (Point)this.VecEff.elementAt(var7)).update();
            if (var5.f >= var5.fRe) {
               this.VecEff.removeElement(var5);
               --var7;
            }
         }

         if (super.f > 30) {
            for(var7 = 0; var7 < this.VecSubEff.size(); ++var7) {
               ((Point_Focus)this.VecSubEff.elementAt(var7)).update_Vx_Vy();
            }
         }

      } else {
         if (this.isAddSound) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            this.setAva(2, this.objBeFireMain);
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            byte var8 = super.Dir;
            GameScreen.addEffectEnd((short)26, 5, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateZoroS2_New_SHORT() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if ((super.f <= 2 || super.f >= 10) && (super.f <= 12 || super.f >= 16) && (super.f <= 18 || super.f >= 22)) {
            super.objFireMain.isTanHinh = false;
         } else {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 7) {
            super.objFireMain.y = this.objBeFireMain.y;
            this.DN = 8;
         }

         if (super.f < 10 && super.f >= 7) {
            MainObject var10000 = this.objBeFireMain;
            var10000.dy += this.DN;
            this.DN /= 2;
         }

         if (super.f >= 10 && super.f < 16) {
            this.DN = 0;
            this.objBeFireMain.dy = 20;
            super.objFireMain.dy = 15;
         }

         byte var3;
         float var4;
         if (super.f == 10) {
            if (this.isAddSound) {
               var4 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            super.objFireMain.x = super.toX - var3;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f >= 16 && super.f < 22) {
            this.objBeFireMain.dy = 30;
            super.objFireMain.dy = 25;
         }

         if (super.f == 16) {
            if (this.isAddSound) {
               var4 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var3 = 30;
            if (super.Dir == 0) {
               var3 = -30;
            }

            super.objFireMain.x = super.toX - var3;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 2, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 22) {
            if (this.isAddSound) {
               var4 = mSound.volumeSound;
               mSound.playSound();
            }

            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            super.objFireMain.x = super.toX - var3;
            super.objFireMain.y = super.toY + this.objBeFireMain.hOne / 2;
            this.setAva(0, this.objBeFireMain);
            this.objBeFireMain.dy = 40;
            super.objFireMain.dy = 35;
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            if (this.isAddSound) {
               var4 = mSound.volumeSound;
               mSound.playSound();
            }

            if (!this.checkNullObject((int)2)) {
               this.setAva(2, this.objBeFireMain);
               GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         if (super.f > 23 && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, super.objFireMain.toX, super.objFireMain.toY) < super.vMax) {
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            byte var1 = 30;
            if (super.Dir == 0) {
               var1 = -30;
            }

            byte var2 = 0;
            if (super.typeEffect == 184) {
               var2 = 2;
            }

            if (super.typeEffect == 482) {
               var2 = 5;
            }

            GameScreen.addEffectEnd((short)26, var2, super.objFireMain.x + var1, super.objFireMain.y - 5, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)26, var2, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 3, super.objFireMain.x + var1, super.objFireMain.y - 35, super.Dir, super.objMainEff);
            super.objFireMain.vx = 0;
            super.objFireMain.vy = 0;
            super.objFireMain.toX = super.objFireMain.x;
            super.objFireMain.toY = super.objFireMain.y;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateZoroS1_New() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         int var1;
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            Point_Focus var2;
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe + 1) {
               var2.AI = 0;
               var2.vy = 0;
               var2.x = this.objBeFireMain.x;
               var2.y = this.objBeFireMain.y;
            }

            if (var2.AG > var2.fRe + 10) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         int var4;
         if (super.f <= super.fRemove) {
            if (super.f == 21) {
               this.DN = 12;
            }

            if (super.f >= 21 && super.f <= 26) {
               MainObject var10000 = this.objBeFireMain;
               var10000.dy += this.DN;
               this.DN -= 2;
            }

            if (super.f > 26) {
               this.DN = 0;
               this.setAva(-1, this.objBeFireMain);
               super.objFireMain.y = this.objBeFireMain.y;
               super.objFireMain.vx = 0;
               super.objFireMain.dy = 40;
               this.objBeFireMain.dy = 45;
            } else if (super.f == 24) {
               this.setAva(-1, this.objBeFireMain);
               var4 = this.objBeFireMain.x - 10;
               if (super.Dir == 0) {
                  var4 = this.objBeFireMain.x + 10;
               }

               var1 = var4 - super.objFireMain.x;
               super.objFireMain.vx = var1 / 4;
            } else if (super.f >= 22) {
               this.setAva(-1, this.objBeFireMain);
            }
         }

         byte var5;
         float var6;
         if (super.f == 5 || super.f == 37) {
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            if (super.f == 5) {
               this.setAva(0, this.objBeFireMain);
            }

            var5 = 20;
            if (super.Dir == 0) {
               var5 = -20;
            }

            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var5, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 10 || super.f == 42) {
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.f == 10) {
               this.setAva(0, this.objBeFireMain);
            }

            var5 = 30;
            if (super.Dir == 0) {
               var5 = -30;
            }

            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 2, super.objFireMain.x + var5, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 47) {
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
            }

            var5 = 20;
            if (super.Dir == 0) {
               var5 = -20;
            }

            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.objFireMain.x + var5, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 12) {
            GameScreen.addEffectEnd((short)30, 0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, 300, super.Dir, super.objMainEff);
         }

         if (super.f == 22) {
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
               var6 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            var5 = 20;
            if (super.Dir == 0) {
               var5 = -20;
            }

            this.setAva(0, this.objBeFireMain);
            GameScreen.addEffectEnd((short)19, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)16, 1, super.x + var5, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            var1 = this.objBeFireMain.x - super.x;
            var4 = this.objBeFireMain.y - super.y;
            Point_Focus var3 = new Point_Focus();
            var3 = this.create_Speed(var1, var4, var3);
            this.VecEff.addElement(var3);
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
            super.objFireMain.dy = 0;
         }

         this.removeEff();
      }
   }

   private void updateZoro_S1_L3_SHORT() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         int var1;
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            Point_Focus var2;
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe + 1) {
               var2.AI = 0;
               var2.vy = 0;
               if (super.typeEffect == 183 || super.typeEffect == 215) {
                  var2.vy = -4;
               }

               var2.x = this.objBeFireMain.x;
               var2.y = this.objBeFireMain.y;
            }

            if (super.typeEffect != 183 && super.typeEffect != 215) {
               if (var2.AG > var2.fRe + 5) {
                  this.VecEff.removeElement(var2);
                  --var1;
               }
            } else if (var2.AG > var2.fRe + 7) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         int var4;
         if (super.f <= 14) {
            if (super.f == 1) {
               this.DN = 12;
            }

            if (super.f > 0 && super.f <= 6) {
               MainObject var10000 = this.objBeFireMain;
               var10000.dy += this.DN;
               this.DN -= 2;
            }

            if (super.f > 6) {
               this.DN = 0;
               this.setAva(-1, this.objBeFireMain);
               super.objFireMain.y = this.objBeFireMain.y;
               super.objFireMain.vx = 0;
               super.objFireMain.dy = 40;
               this.objBeFireMain.dy = 45;
            } else if (super.f == 4) {
               this.setAva(-1, this.objBeFireMain);
               var4 = this.objBeFireMain.x - 10;
               if (super.Dir == 0) {
                  var4 = this.objBeFireMain.x + 10;
               }

               var1 = var4 - super.objFireMain.x;
               super.objFireMain.vx = var1 / 4;
            } else if (super.f >= 2) {
               this.setAva(-1, this.objBeFireMain);
            }
         }

         if (super.f == 8 || super.f == 12) {
            if (this.isAddSound) {
               float var7 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.setAva(1, this.objBeFireMain);
            byte var6 = 20;
            if (super.Dir == 0) {
               var6 = -20;
            }

            byte var5 = 1;
            if (super.typeEffect == 183) {
               var5 = -1;
            }

            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 2, super.objFireMain.x + var6, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            if (super.typeEffect == 215) {
               GameScreen.addEffectEnd((short)135, 0, super.objFireMain.x + var6, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 5 + 5, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)16, var5, super.objFireMain.x + var6, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
            }
         }

         if (super.f == 1) {
            var4 = this.objBeFireMain.x - super.x;
            var1 = this.objBeFireMain.y - super.y;
            Point_Focus var3 = new Point_Focus();
            var3 = this.create_Speed(var4, var1, var3);
            this.VecEff.addElement(var3);
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
            super.objFireMain.dy = 0;
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S1_L6() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         int var1;
         Point_Focus var2;
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe + 1) {
               var2.AI = 0;
               var2.vy = -4;
            }

            if (var2.AG > var2.fRe + 7) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (super.f <= 14) {
            if (super.f == 1) {
               this.DN = 12;
            }

            if (super.f > 0 && super.f <= 6) {
               MainObject var10000 = this.objBeFireMain;
               var10000.dy += this.DN;
               this.DN -= 2;
            }

            if (super.f > 6) {
               this.DN = 0;
               this.setAva(-1, this.objBeFireMain);
               super.objFireMain.y = this.objBeFireMain.y;
               super.objFireMain.vx = 0;
               super.objFireMain.dy = 40;
               this.objBeFireMain.dy = 45;
            } else if (super.f == 4) {
               this.setAva(-1, this.objBeFireMain);
               int var5 = this.objBeFireMain.x;
               if (super.Dir == 0) {
                  var5 = this.objBeFireMain.x;
               }

               var5 = super.objFireMain.x;
            } else if (super.f >= 2) {
               this.setAva(-1, this.objBeFireMain);
            }
         }

         if (super.f == 8 || super.f == 12) {
            if (this.isAddSound) {
               float var6 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.setAva(1, this.objBeFireMain);
            byte var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 2, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 1) {
            var1 = this.objBeFireMain.y - super.y;
            var2 = new Point_Focus();
            (var2 = this.create_Speed(0, var1, var2)).x = this.objBeFireMain.x;
            var2.y = this.objBeFireMain.y;
            this.VecEff.addElement(var2);
            var1 = super.Dir == 0 ? 5 : -5;
            GameScreen.addEffectEnd((short)170, 0, super.objFireMain.x + var1, super.objFireMain.y + 22, super.Dir, super.objMainEff);
         }

         for(int var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
            if (super.f > 8 + (var4 << 2)) {
               ((Point_Focus)this.VecSubEff.elementAt(var4)).update_Vx_Vy();
            }
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
            super.objFireMain.dy = 0;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S1_L7() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         int var1;
         Point_Focus var2;
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe + 1) {
               var2.AI = 0;
               var2.vy = -4;
            }

            if (var2.AG > var2.fRe + 7) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (super.f <= 14) {
            if (super.f == 1) {
               this.DN = 12;
            }

            if (super.f > 0 && super.f <= 6) {
               MainObject var10000 = this.objBeFireMain;
               var10000.dy += this.DN;
               this.DN -= 2;
            }

            if (super.f > 6) {
               this.DN = 0;
               this.setAva(-1, this.objBeFireMain);
               super.objFireMain.y = this.objBeFireMain.y;
               super.objFireMain.vx = 0;
               super.objFireMain.dy = 40;
               this.objBeFireMain.dy = 45;
            } else if (super.f == 4) {
               this.setAva(-1, this.objBeFireMain);
               int var5 = this.objBeFireMain.x;
               if (super.Dir == 0) {
                  var5 = this.objBeFireMain.x;
               }

               var5 = super.objFireMain.x;
            } else if (super.f >= 2) {
               this.setAva(-1, this.objBeFireMain);
            }
         }

         if (super.f == 8 || super.f == 12) {
            if (this.isAddSound) {
               float var6 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            this.setAva(1, this.objBeFireMain);
            byte var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            GameScreen.addEffectEnd((short)10, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 2, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.dy - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 1) {
            var1 = this.objBeFireMain.y - super.y;
            var2 = new Point_Focus();
            (var2 = this.create_Speed(0, var1, var2)).x = this.objBeFireMain.x;
            var2.y = this.objBeFireMain.y;
            this.VecEff.addElement(var2);
            var1 = super.Dir == 0 ? 5 : -5;
            GameScreen.addEffectEnd((short)181, 0, super.objFireMain.x + var1, super.objFireMain.y + 22, super.Dir, super.objMainEff);
         }

         for(int var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
            if (super.f > 8 + (var4 << 2)) {
               ((Point_Focus)this.VecSubEff.elementAt(var4)).update_Vx_Vy();
            }
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
            super.objFireMain.dy = 0;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateLuffyS3_New() {
      if ((super.f < super.fRemove || this.VecEff.size() != 0) && !this.checkNullObject((int)1)) {
         byte var1;
         Point var2;
         if (super.f == 5) {
            var1 = 30;
            if (super.Dir == 2) {
               var1 = -30;
            }

            var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y);
            this.VecSubEff.addElement(var2);
         }

         if (super.f == 10) {
            var1 = -10;
            if (super.Dir == 2) {
               var1 = 10;
            }

            var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - 35);
            this.VecSubEff.addElement(var2);
         }

         if (super.f == 15) {
            var1 = -10;
            if (super.Dir == 2) {
               var1 = 10;
            }

            var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y + 35);
            this.VecSubEff.addElement(var2);
         }

         if ((super.f == 22 || super.f == 25) && this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f >= 18) {
            if (super.f % 3 == 0) {
               int var6;
               if (this.CI < super.vecObjsBeFire.size()) {
                  Object_Effect_Skill var13 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
                  ++this.CI;
                  MainObject var10;
                  if (var13 != null && (var10 = MainObject.get_Object((int)var13.ID, (byte)var13.tem)) != null) {
                     byte var9 = 0;
                     if (super.objFireMain.x < var10.x) {
                        var9 = 2;
                     }

                     byte var12 = 12;
                     if (super.Dir == 0) {
                        var12 = -12;
                     }

                     byte var14 = 2;
                     if (super.typeEffect == 182) {
                        var14 = 3;
                     }

                     GameScreen.addEffectEnd_ObjTo((short)13, var14, super.objFireMain.x + var12, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (short)var10.ID, (byte)var10.typeObject, var9, super.objMainEff);

                     for(var6 = 0; var6 < this.VecSubEff.size(); ++var6) {
                        Point var16 = (Point)this.VecSubEff.elementAt(var6);
                        var1 = -20;
                        if (super.Dir == 2) {
                           var1 = 20;
                        }

                        GameScreen.addEffectEnd_ObjTo((short)13, var14, var16.x + var12 + var1, var16.y - super.objFireMain.hOne / 2, (short)var10.ID, (byte)var10.typeObject, var9, super.objMainEff);
                     }
                  }
               } else {
                  var1 = 12;
                  if (super.Dir == 0) {
                     var1 = -12;
                  }

                  byte var8 = 0;
                  if (super.typeEffect == 182) {
                     var8 = 3;
                  }

                  int var3;
                  if (CRes.random(3) == 0) {
                     var3 = super.objFireMain.x + var1 + super.am_duong * 120 + CRes.random_Am_0(20);
                     int var4 = super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10 - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                     GameScreen.addEffectEnd_ToX_ToY((short)13, (byte)var8, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (int)var3, (int)var4, super.Dir, super.objMainEff);
                  }

                  for(var3 = 0; var3 < this.VecSubEff.size(); ++var3) {
                     Point var11 = (Point)this.VecSubEff.elementAt(var3);
                     if (CRes.random(3) == 0) {
                        byte var5 = -20;
                        if (super.Dir == 2) {
                           var5 = 20;
                        }

                        var6 = var11.x + var1 + var5 + super.am_duong * 120 + CRes.random_Am_0(20);
                        int var7 = var11.y - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                        GameScreen.addEffectEnd_ToX_ToY((short)13, (byte)var8, var11.x + var1 + var5, var11.y - super.objFireMain.hOne / 2, (int)var6, (int)var7, super.Dir, super.objMainEff);
                     }
                  }
               }
            }

            this.addVir(15, 5, 10, true);
         }

         int var15;
         for(var15 = 0; var15 < this.VecSubEff.size(); ++var15) {
            ++((Point)this.VecSubEff.elementAt(var15)).f;
         }

         for(var15 = 0; var15 < this.VecEff.size(); ++var15) {
            ++(var2 = (Point)this.VecEff.elementAt(var15)).f;
            if (var2.f >= 3) {
               this.VecEff.removeElement(var2);
               --var15;
            }
         }

      } else {
         this.removeEff();
      }
   }

   private void updateLuffyS3_L5() {
      if ((super.f < super.fRemove || this.VecEff.size() != 0) && !this.checkNullObject((int)1)) {
         MainObject var10000;
         if (super.f < 4) {
            super.objFireMain.vx = -(super.am_duong * 7);
            var10000 = super.objFireMain;
            var10000.dy += 20 - super.f * 3;
         } else if (super.f < super.fRemove - 3) {
            super.objFireMain.dy = 60;
            super.objFireMain.vx = 0;
         } else {
            if (super.objFireMain.dy <= 10) {
               super.objFireMain.dy = 0;
            }

            if (super.objFireMain.dy != 0) {
               var10000 = super.objFireMain;
               var10000.dy /= 3;
            }
         }

         byte var1;
         Point var2;
         if (super.f == 5) {
            var1 = 40;
            if (super.Dir == 2) {
               var1 = -40;
            }

            var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy);
            this.VecSubEff.addElement(var2);
         }

         if (super.typeEffect == 273) {
            Point var3;
            if (super.f == 10) {
               var1 = 15;
               if (super.Dir == 2) {
                  var1 = -15;
               }

               var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - 45);
               this.VecSubEff.addElement(var2);
               var3 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy + 45);
               this.VecSubEff.addElement(var3);
            }

            if (super.f == 15) {
               var1 = 40;
               if (super.Dir == 2) {
                  var1 = -40;
               }

               var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - 90);
               this.VecSubEff.addElement(var2);
               var3 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy + 90);
               this.VecSubEff.addElement(var3);
            }
         } else {
            if (super.f == 10) {
               var1 = 15;
               if (super.Dir == 2) {
                  var1 = -15;
               }

               var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - 45);
               this.VecSubEff.addElement(var2);
            }

            if (super.f == 15) {
               var1 = 15;
               if (super.Dir == 2) {
                  var1 = -15;
               }

               var2 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy + 45);
               this.VecSubEff.addElement(var2);
            }
         }

         if ((super.f == 22 || super.f == 25) && this.isAddSound) {
            float var18 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f >= 18) {
            if (super.f % 3 == 0) {
               int var6;
               if (this.CI < super.vecObjsBeFire.size()) {
                  Object_Effect_Skill var15 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
                  ++this.CI;
                  MainObject var13;
                  if (var15 != null && (var13 = MainObject.get_Object((int)var15.ID, (byte)var15.tem)) != null) {
                     byte var11 = 0;
                     if (super.objFireMain.x < var13.x) {
                        var11 = 2;
                     }

                     byte var12 = 12;
                     if (super.Dir == 0) {
                        var12 = -12;
                     }

                     byte var14 = 4;
                     if (super.typeEffect == 273) {
                        var14 = 5;
                     }

                     GameScreen.addEffectEnd_ObjTo((short)13, var14, super.objFireMain.x + var12, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (short)var13.ID, (byte)var13.typeObject, var11, super.objMainEff);

                     for(var6 = 0; var6 < this.VecSubEff.size(); ++var6) {
                        Point var16 = (Point)this.VecSubEff.elementAt(var6);
                        var1 = -20;
                        if (super.Dir == 2) {
                           var1 = 20;
                        }

                        GameScreen.addEffectEnd_ObjTo((short)13, var14, var16.x + var12 + var1, var16.y - super.objFireMain.hOne / 2, (short)var13.ID, (byte)var13.typeObject, var11, super.objMainEff);
                     }
                  }
               } else if (!GameCanvas.lowGraphic) {
                  var1 = 12;
                  if (super.Dir == 0) {
                     var1 = -12;
                  }

                  byte var8 = 4;
                  if (super.typeEffect == 273) {
                     var8 = 5;
                  }

                  int var9;
                  if (CRes.random(3) == 0) {
                     var9 = super.objFireMain.x + var1 + super.am_duong * 120 + CRes.random_Am_0(20);
                     int var4 = super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10 - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                     GameScreen.addEffectEnd_ToX_ToY((short)13, (byte)var8, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (int)var9, (int)var4, super.Dir, super.objMainEff);
                  }

                  for(var9 = 0; var9 < this.VecSubEff.size(); ++var9) {
                     Point var10 = (Point)this.VecSubEff.elementAt(var9);
                     if (CRes.random(3) == 0) {
                        byte var5 = -20;
                        if (super.Dir == 2) {
                           var5 = 20;
                        }

                        var6 = var10.x + var1 + var5 + super.am_duong * 120 + CRes.random_Am_0(20);
                        int var7 = var10.y - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                        GameScreen.addEffectEnd_ToX_ToY((short)13, (byte)var8, var10.x + var1 + var5, var10.y - super.objFireMain.hOne / 2, (int)var6, (int)var7, super.Dir, super.objMainEff);
                     }
                  }
               }
            }

            this.addVir(15, 5, 10, true);
         }

         int var17;
         for(var17 = 0; var17 < this.VecSubEff.size(); ++var17) {
            ++((Point)this.VecSubEff.elementAt(var17)).f;
         }

         for(var17 = 0; var17 < this.VecEff.size(); ++var17) {
            ++(var2 = (Point)this.VecEff.elementAt(var17)).f;
            if (var2.f >= 3) {
               this.VecEff.removeElement(var2);
               --var17;
            }
         }

      } else {
         this.removeEff();
      }
   }

   private void update_Luffy_S3_L7() {
      if ((super.f < super.fRemove || this.VecEff.size() != 0) && !this.checkNullObject((int)1)) {
         MainObject var10000;
         if (super.f < 4) {
            super.objFireMain.vx = -(super.am_duong * 7);
            var10000 = super.objFireMain;
            var10000.dy += 20 - super.f * 3;
         } else if (super.f < super.fRemove - 3) {
            super.objFireMain.dy = 60;
            super.objFireMain.vx = 0;
         } else {
            if (super.objFireMain.dy <= 10) {
               super.objFireMain.dy = 0;
            }

            if (super.objFireMain.dy != 0) {
               var10000 = super.objFireMain;
               var10000.dy /= 3;
            }
         }

         short var1;
         byte var2;
         if (super.f == 3) {
            var1 = -150;
            var2 = 2;
            if (super.Dir == 2) {
               var1 = 150;
               var2 = 0;
            }

            Point var3;
            (var3 = new Point(super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.dy)).AM = var2;
            this.VecSubEff.addElement(var3);
         }

         byte var9;
         byte var11;
         if (super.f == 5) {
            var9 = 40;
            if (super.Dir == 2) {
               var9 = -40;
            }

            short var10 = -150;
            var11 = 2;
            if (super.Dir == 2) {
               var10 = 150;
               var11 = 0;
            }

            Point var4;
            (var4 = new Point(super.objFireMain.x + var9, super.objFireMain.y - super.objFireMain.dy)).AM = super.Dir;
            this.VecSubEff.addElement(var4);
            (var4 = new Point(super.objFireMain.x - var9 + var10, super.objFireMain.y - super.objFireMain.dy)).AM = var11;
            this.VecSubEff.addElement(var4);
         }

         Point var12;
         if (super.f == 10) {
            var9 = 15;
            if (super.Dir == 2) {
               var9 = -15;
            }

            (var12 = new Point(super.objFireMain.x + var9, super.objFireMain.y - super.objFireMain.dy - 45)).AM = super.Dir;
            this.VecSubEff.addElement(var12);
            (var12 = new Point(super.objFireMain.x + var9, super.objFireMain.y - super.objFireMain.dy + 45)).AM = super.Dir;
            this.VecSubEff.addElement(var12);
            short var13 = -150;
            byte var14 = 2;
            if (super.Dir == 2) {
               var13 = 150;
               var14 = 0;
            }

            (var12 = new Point(super.objFireMain.x - var9 + var13, super.objFireMain.y - super.objFireMain.dy - 45)).AM = var14;
            this.VecSubEff.addElement(var12);
            (var12 = new Point(super.objFireMain.x - var9 + var13, super.objFireMain.y - super.objFireMain.dy + 45)).AM = var14;
            this.VecSubEff.addElement(var12);
         }

         if ((super.f == 22 || super.f == 25) && this.isAddSound) {
            float var25 = mSound.volumeSound;
            mSound.playSound();
         }

         if (super.f >= 18) {
            if (super.f % 3 == 0) {
               int var7;
               if (this.CI < super.vecObjsBeFire.size()) {
                  Object_Effect_Skill var15 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
                  ++this.CI;
                  MainObject var20;
                  if (var15 != null && (var20 = MainObject.get_Object((int)var15.ID, (byte)var15.tem)) != null) {
                     var11 = 0;
                     if (super.objFireMain.x < var20.x) {
                        var11 = 2;
                     }

                     byte var22 = 12;
                     if (super.Dir == 0) {
                        var22 = -12;
                     }

                     short var18 = -150;
                     byte var23 = 2;
                     if (var11 == 2) {
                        var18 = 150;
                        var23 = 0;
                     }

                     GameScreen.addEffectEnd_ObjTo((short)13, 5, super.objFireMain.x + var22, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (short)var20.ID, (byte)var20.typeObject, var11, super.objMainEff);
                     GameScreen.addEffectEnd_ObjTo((short)13, 5, super.objFireMain.x + var22 + var18, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (short)var20.ID, (byte)var20.typeObject, (byte)var23, super.objMainEff);

                     for(var7 = 0; var7 < this.VecSubEff.size(); ++var7) {
                        Point var24 = (Point)this.VecSubEff.elementAt(var7);
                        var9 = -20;
                        if (super.Dir == 2) {
                           var9 = 20;
                        }

                        GameScreen.addEffectEnd_ObjTo((short)13, 5, var24.x + var22 + var9, var24.y - super.objFireMain.hOne / 2, (short)var20.ID, (byte)var20.typeObject, (byte)var24.AM, super.objMainEff);
                     }
                  }
               } else if (!GameCanvas.lowGraphic) {
                  var1 = -150;
                  var2 = 2;
                  if (super.Dir == 2) {
                     var1 = 150;
                     var2 = 0;
                  }

                  byte var16 = 12;
                  if (super.Dir == 0) {
                     var16 = -12;
                  }

                  int var19;
                  if (CRes.random(3) == 0) {
                     var19 = super.objFireMain.x + var16 + super.am_duong * 120 + CRes.random_Am_0(20);
                     int var5 = super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10 - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                     GameScreen.addEffectEnd_ToX_ToY((short)13, 5, super.objFireMain.x + var16, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (int)var19, (int)var5, super.Dir, super.objMainEff);
                     GameScreen.addEffectEnd_ToX_ToY((short)13, 5, super.objFireMain.x - var16 + var1, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, (int)(var19 + var1), (int)var5, (byte)var2, super.objMainEff);
                  }

                  for(var19 = 0; var19 < this.VecSubEff.size(); ++var19) {
                     Point var17 = (Point)this.VecSubEff.elementAt(var19);
                     if (CRes.random(3) == 0) {
                        byte var6 = -20;
                        if (super.Dir == 2) {
                           var6 = 20;
                        }

                        var7 = var17.x + var16 + var6 + super.am_duong * 120 + CRes.random_Am_0(20);
                        int var8 = var17.y - super.objFireMain.hOne / 2 + CRes.random_Am_0(80);
                        GameScreen.addEffectEnd_ToX_ToY((short)13, 5, var17.x + var16 + var6 + var1, var17.y - super.objFireMain.hOne / 2, (int)(var7 + var1), (int)var8, (byte)var17.AM, super.objMainEff);
                     }
                  }
               }
            }

            this.addVir(15, 5, 10, true);
         }

         int var21;
         for(var21 = 0; var21 < this.VecSubEff.size(); ++var21) {
            ++((Point)this.VecSubEff.elementAt(var21)).f;
         }

         for(var21 = 0; var21 < this.VecEff.size(); ++var21) {
            ++(var12 = (Point)this.VecEff.elementAt(var21)).f;
            if (var12.f >= 3) {
               this.VecEff.removeElement(var12);
               --var21;
            }
         }

      } else {
         this.removeEff();
      }
   }

   private void updateLuffyS2_NEW() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f >= 12 && super.f <= 20) {
            super.objFireMain.isTanHinh = true;
            if (super.objFireMain == GameScreen.player) {
               Player.isSendMove = false;
            }
         } else {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 16) {
            short var1 = 220;
            if (super.Dir == 0) {
               var1 = -220;
            }

            MainObject var10000 = super.objFireMain;
            var10000.x += var1;
            super.x = super.objFireMain.x;
            super.Dir = (byte)(super.Dir == 0 ? 2 : 0);
            super.objFireMain.Dir = super.Dir;
         }

         byte var3;
         float var6;
         if (super.f == 12) {
            MainEffect.AB(-10, this.objBeFireMain);
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f == 29) {
            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            if (this.isAddSound) {
               var6 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f > 20) {
            if (super.f < 27) {
               if (super.Dir == 0) {
                  super.objFireMain.vx = -super.objFireMain.CN << 2;
               } else {
                  super.objFireMain.vx = super.objFireMain.CN << 2;
               }

               if (super.f % 2 == 0 || super.typeEffect == 35) {
                  Point var4 = new Point(super.objFireMain.x - super.objFireMain.vx / 2, super.objFireMain.y);
                  this.VecEff.addElement(var4);
               }
            } else {
               if (super.objFireMain == GameScreen.player) {
                  Player.isSendMove = true;
               }

               super.objFireMain.vx = 0;
            }

            for(int var5 = 0; var5 < this.VecEff.size(); ++var5) {
               Point var2;
               ++(var2 = (Point)this.VecEff.elementAt(var5)).f;
               if (var2.f / 2 >= 3) {
                  this.VecEff.removeElement(var2);
                  --var5;
               }
            }
         }

      } else {
         this.removeEff();
         if (super.objFireMain == GameScreen.player) {
            GameScreen.AB(true);
         }

      }
   }

   private void updateLuffyS2_NEW_SHORT() {
      if ((super.f < super.fRemove || super.typeEffect == 213 && super.typeEffect != 272 && this.VecSubEff.size() != 0) && !this.checkNullObject((int)1)) {
         if (super.f >= 4 && super.f <= 11) {
            super.objFireMain.isTanHinh = true;
            if (super.objFireMain == GameScreen.player) {
               Player.isSendMove = false;
            }
         } else {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 7) {
            short var1 = 320;
            if (super.Dir == 0) {
               var1 = -320;
            }

            MainObject var10000 = super.objFireMain;
            var10000.x += var1;
            super.x = super.objFireMain.x;
            super.Dir = (byte)(super.Dir == 0 ? 2 : 0);
            super.objFireMain.Dir = super.Dir;
         }

         byte var3;
         float var7;
         if (super.f == 3) {
            MainEffect.AB(-10, this.objBeFireMain);
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            if (this.isAddSound) {
               var7 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f == 20) {
            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            var3 = 20;
            if (super.Dir == 0) {
               var3 = -20;
            }

            byte var2;
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
               var2 = 5;
               if (super.typeEffect == 272) {
                  var2 = 3;
               }

               GameScreen.addEffectEnd((short)108, var2, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            if (!this.checkNullObject((int)1)) {
               GameScreen.addEffectEnd((short)0, 0, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
               if (super.typeEffect == 181 || super.typeEffect == 213 || super.typeEffect == 272) {
                  var3 = 10;
                  if (super.Dir == 0) {
                     var3 = -10;
                  }

                  var2 = 0;
                  if (super.typeEffect == 213) {
                     var2 = 3;
                  } else if (super.typeEffect == 272) {
                     var2 = 4;
                  }

                  GameScreen.addEffectEnd((short)119, var2, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
               }
            }

            if (this.isAddSound) {
               var7 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f == 22 && super.typeEffect == 272) {
            var3 = 10;
            if (super.Dir == 0) {
               var3 = -10;
            }

            GameScreen.addEffectEnd((short)173, 0, super.objFireMain.x + var3, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
         }

         if (super.f > 11) {
            if (super.f < 18) {
               if (super.Dir == 0) {
                  super.objFireMain.vx = -super.objFireMain.CN << 2;
               } else {
                  super.objFireMain.vx = super.objFireMain.CN << 2;
               }

               Point var5;
               if (super.f % 2 == 0 || super.typeEffect == 35) {
                  var5 = new Point(super.objFireMain.x - super.objFireMain.vx / 2, super.objFireMain.y);
                  this.VecEff.addElement(var5);
               }

               if (!this.checkNullObject((int)1)) {
                  GameScreen.addEffectEnd((short)109, 0, super.objFireMain.x, super.objFireMain.y + 5, super.Dir, super.objMainEff);
               }

               if (super.typeEffect == 213 || super.typeEffect == 272) {
                  (var5 = new Point(super.objFireMain.x, super.objFireMain.y + 2)).frame = (super.f - 12) / 2;
                  if (var5.frame >= super.fraImgSub3Eff.nFrame) {
                     var5.frame = super.fraImgSub3Eff.nFrame - 1;
                  }

                  this.VecSubEff.addElement(var5);
               }
            } else {
               if (super.objFireMain == GameScreen.player) {
                  Player.isSendMove = true;
               }

               super.objFireMain.vx = 0;
            }

            Point var4;
            int var6;
            for(var6 = 0; var6 < this.VecEff.size(); ++var6) {
               ++(var4 = (Point)this.VecEff.elementAt(var6)).f;
               if (var4.f / 2 >= 3) {
                  this.VecEff.removeElement(var4);
                  --var6;
               }
            }

            if (super.typeEffect == 213 || super.typeEffect == 272) {
               for(var6 = 0; var6 < this.VecSubEff.size(); ++var6) {
                  ++(var4 = (Point)this.VecSubEff.elementAt(var6)).f;
                  if (var4.f >= 5) {
                     this.VecSubEff.removeElement(var6);
                     --var6;
                  }
               }
            }
         }

      } else {
         this.removeEff();
         if (super.objFireMain == GameScreen.player) {
            GameScreen.AB(true);
         }

      }
   }

   private void update_Luffy_S2_L7() {
      if ((super.f < super.fRemove || this.VecSubEff.size() != 0) && !this.checkNullObject((int)1)) {
         if (super.f >= 4 && super.f <= 11) {
            super.objFireMain.isTanHinh = true;
            if (super.objFireMain == GameScreen.player) {
               Player.isSendMove = false;
            }
         } else {
            super.objFireMain.isTanHinh = false;
         }

         byte var1;
         if (super.f == 7) {
            var1 = 120;
            if (super.Dir == 0) {
               var1 = -120;
            }

            MainObject var10000 = super.objFireMain;
            var10000.x += var1;
            super.x = super.objFireMain.x;
            super.Dir = (byte)(super.Dir == 0 ? 2 : 0);
            super.objFireMain.Dir = super.Dir;
         }

         float var5;
         if (super.f == 3) {
            MainEffect.AB(-10, this.objBeFireMain);
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            GameScreen.addEffectEnd((short)0, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            if (this.isAddSound) {
               var5 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f == 20) {
            this.addVir(5, 5, 10, true);
            this.setAva(2, this.objBeFireMain);
            var1 = 20;
            if (super.Dir == 0) {
               var1 = -20;
            }

            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)8, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 3, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            if (!this.checkNullObject((int)1)) {
               GameScreen.addEffectEnd((short)0, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
               var1 = 10;
               if (super.Dir == 0) {
                  var1 = -10;
               }

               GameScreen.addEffectEnd((short)119, 4, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
            }

            if (this.isAddSound) {
               var5 = mSound.volumeSound;
               mSound.playSound();
            }
         }

         if (super.f == 22) {
            var1 = 10;
            if (super.Dir == 0) {
               var1 = -10;
            }

            GameScreen.addEffectEnd((short)173, 1, super.objFireMain.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
         }

         if (super.f > 11) {
            if (super.f < 18) {
               if (super.Dir == 0) {
                  super.objFireMain.vx = -super.objFireMain.CN << 2;
               } else {
                  super.objFireMain.vx = super.objFireMain.CN << 2;
               }

               Point var3;
               if (super.f % 2 == 0 || super.typeEffect == 35) {
                  var3 = new Point(super.objFireMain.x - super.objFireMain.vx / 2, super.objFireMain.y);
                  this.VecEff.addElement(var3);
               }

               if (!this.checkNullObject((int)1)) {
                  GameScreen.addEffectEnd((short)109, 0, super.objFireMain.x, super.objFireMain.y + 5, super.Dir, super.objMainEff);
               }

               (var3 = new Point(super.objFireMain.x, super.objFireMain.y + 2)).frame = (super.f - 12) / 2;
               if (var3.frame >= super.fraImgSub3Eff.nFrame) {
                  var3.frame = super.fraImgSub3Eff.nFrame - 1;
               }

               this.VecSubEff.addElement(var3);
            } else {
               if (super.objFireMain == GameScreen.player) {
                  Player.isSendMove = true;
               }

               super.objFireMain.vx = 0;
            }

            Point var2;
            int var4;
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               ++(var2 = (Point)this.VecEff.elementAt(var4)).f;
               if (var2.f / 2 >= 3) {
                  this.VecEff.removeElement(var2);
                  --var4;
               }
            }

            for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
               ++(var2 = (Point)this.VecSubEff.elementAt(var4)).f;
               if (var2.f >= 5) {
                  this.VecSubEff.removeElement(var4);
                  --var4;
               }
            }
         }

      } else {
         this.removeEff();
         if (super.objFireMain == GameScreen.player) {
            GameScreen.AB(true);
         }

      }
   }

   private void updateMon11() {
      if (!this.checkNullObject((int)1)) {
         if (super.f < 2) {
            super.objFireMain.vx = super.vX1000;
         } else if (super.f < 5) {
            super.objFireMain.vx = -super.vX1000;
         } else {
            super.objFireMain.vx = 0;
         }
      }

      if (super.f == super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
         }

         if (super.typeEffect == 144) {
            GameScreen.addEffectEnd((short)11, 0, super.toX + CRes.random_Am_0(5), super.toY + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         } else {
            GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         }

         if (super.fRemove >= 4) {
            if (!this.checkNullObject((int)1)) {
               super.objFireMain.vx = 0;
            }

            this.removeEff();
         }
      }

      if (super.fRemove < 4 && super.f == 4) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
         }

         this.removeEff();
      }

   }

   private void updateMon10() {
      if (!this.checkNullObject((int)1)) {
         if (super.f < 2) {
            super.objFireMain.vx = super.vx;
         } else {
            super.objFireMain.vx = -super.vx;
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.vx = 0;
         }

         if (super.typeEffect == 149) {
            GameScreen.addEffectEnd((short)8, 0, super.toX + CRes.random_Am_0(5), super.toY + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         } else if (super.typeEffect == 143) {
            GameScreen.addEffectEnd((short)11, 0, super.toX + CRes.random_Am_0(5), super.toY + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         } else {
            GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         }

         this.removeEff();
      }

   }

   private void updateAlvida2() {
      int var1;
      if (super.f == 7) {
         this.addSound((byte)14);
         var1 = super.x;
         if (super.Dir == 0) {
            var1 -= 15;
         } else {
            var1 += 15;
         }

         GameScreen.addEffectEnd((short)89, 0, var1, super.y + 20, super.Dir, super.objMainEff);
      }

      if (super.f >= 7) {
         super.vy = 6;
      }

      if (super.f >= super.fRemove) {
         this.addVir(3, 5, 10, false);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd((short)52, 0, var3.x, var3.y + 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)8, 0, var3.x, var3.y - var3.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         this.removeEff();
      }

   }

   private void update_Ussop_S3_L5() {
      if (super.f == 1) {
         this.CM = new int[][]{{34, -30, 1}, {67, -44, 1}, {100, -42, 2}, {126, -17, 1}};
      }

      int var1;
      if (super.f == 10 && !this.checkNullObject((int)3)) {
         var1 = super.toX - super.x;
         int var2 = super.toY - this.objBeFireMain.hOne - super.y - 50;
         this.create_Speed(var1, var2, (Point_Focus)null);
         var2 = CRes.AA(var1, var2);
         super.frame = this.setFrameAngle(var2);
         super.fRemove += 10;
      }

      if (super.f == super.fRemove) {
         this.addVir(5, 5, 10, true);
         GameScreen.addEffectEnd((short)120, 0, super.x, super.y, super.Dir, super.objMainEff);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var3;
            MainObject var4;
            if ((var3 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var4 = MainObject.get_Object((int)var3.ID, (byte)var3.tem)) != null) {
               GameScreen.addEffectEnd((short)93, 2, var4.x + CRes.random_Am_0(10), var4.y - var4.hOne + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            }
         }

         this.CI = 0;
         super.vx = 0;
         super.vy = 0;
      }

      if (super.f > super.fRemove && super.f % 2 == 1) {
         if (!GameCanvas.lowGraphic) {
            for(var1 = 0; var1 < 2; ++var1) {
               GameScreen.addEffectEnd((short)120, this.CM[this.CI][2], super.x + this.CM[this.CI][0] * (var1 == 0 ? 1 : -1), super.y + this.CM[this.CI][1], super.Dir, super.objMainEff);
            }
         }

         ++this.CI;
         if (this.CI >= this.CM.length) {
            this.removeEff();
         }
      }

   }

   private void update_Ussop_S3_L6() {
      if (super.f == 1) {
         this.CM = new int[][]{{40, -60, CRes.random(2, 4)}, {80, -25, CRes.random(1, 3)}, {120, -60, CRes.random(2, 4)}, {160, -25, CRes.random(2, 4)}};
      }

      int var1;
      if (super.f == 10 && !this.checkNullObject((int)3)) {
         var1 = super.toX - super.x;
         int var2 = super.toY - this.objBeFireMain.hOne - super.y - 50;
         this.create_Speed(var1, var2, (Point_Focus)null);
         int var3 = CRes.AA(var1, var2);
         super.frame = this.setFrameAngle(var3);
         super.fRemove += 10;
         super.vMax = 14;
         this.DJ = this.create_Speed(super.toX + 80 - super.x, var2, new Point_Focus());
         this.DK = this.create_Speed(super.toX - 80 - super.x, var2, new Point_Focus());
         var3 = CRes.AA(super.toX + 80 - super.x, var2);
         this.DL = this.setFrameAngle(var3);
         var3 = CRes.AA(super.toX - 80 - super.x, var2);
         this.DM = this.setFrameAngle(var3);
      }

      if (super.f > 10 && super.f < super.fRemove) {
         this.DJ.update_Vx_Vy();
         this.DK.update_Vx_Vy();
      }

      if (super.f == super.fRemove) {
         this.addVir(5, 5, 10, true);
         GameScreen.addEffectEnd((short)168, 1, super.x, super.y, super.Dir, super.objMainEff);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var4;
            MainObject var5;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               GameScreen.addEffectEnd((short)93, 2, var5.x + CRes.random_Am_0(10), var5.y - var5.hOne + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            }
         }

         this.CI = 0;
         super.vx = 0;
         super.vy = 0;
      }

      if (super.f > super.fRemove && super.f % 2 == 1) {
         if (!GameCanvas.lowGraphic) {
            for(var1 = 0; var1 < 2; ++var1) {
               GameScreen.addEffectEnd((short)168, this.CM[this.CI][2], super.x + this.CM[this.CI][0] * (var1 == 0 ? 1 : -1), super.y + this.CM[this.CI][1], super.Dir, super.objMainEff);
            }
         }

         ++this.CI;
         if (this.CI >= this.CM.length) {
            this.removeEff();
         }
      }

   }

   private void update_Ussop_S3_L7() {
      if (super.f == 1) {
         this.CM = new int[][]{{40, -60, CRes.random(2, 4)}, {80, -25, CRes.random(1, 3)}, {120, -60, CRes.random(2, 4)}, {160, -25, CRes.random(2, 4)}};
      }

      int var1;
      if (super.f == 10 && !this.checkNullObject((int)3)) {
         var1 = super.toX - super.x;
         int var2 = super.toY - this.objBeFireMain.hOne - super.y - 50;
         this.create_Speed(var1, var2, (Point_Focus)null);
         int var3 = CRes.AA(var1, var2);
         super.frame = this.setFrameAngle(var3);
         super.fRemove += 10;
         super.vMax = 14;
         this.DJ = this.create_Speed(super.toX + 80 - super.x, var2, new Point_Focus());
         this.DK = this.create_Speed(super.toX - 80 - super.x, var2, new Point_Focus());
         var3 = CRes.AA(super.toX + 80 - super.x, var2);
         this.DL = this.setFrameAngle(var3);
         var3 = CRes.AA(super.toX - 80 - super.x, var2);
         this.DM = this.setFrameAngle(var3);
      }

      if (super.f > 10 && super.f < super.fRemove) {
         this.DJ.update_Vx_Vy();
         this.DK.update_Vx_Vy();
      }

      if (super.f == 15) {
         this.addVir(5, 5, 10, true);
         GameScreen.addEffectEnd((short)168, 1, super.x, super.y, super.Dir, super.objMainEff);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var4;
            MainObject var5;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               GameScreen.addEffectEnd((short)183, 0, var5.x, var5.y - var5.hOne / 3, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)93, 2, var5.x + CRes.random_Am_0(10), var5.y - var5.hOne + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            }
         }

         this.CI = 0;
         super.vx = 0;
         super.vy = 0;
      }

      if (super.f == super.fRemove) {
         GameScreen.addEffectEnd((short)183, 1, super.objMainEff.x, super.objMainEff.y - super.objMainEff.hOne / 3, super.Dir, super.objMainEff);
      }

      if (super.f > super.fRemove && super.f % 2 == 1) {
         if (!GameCanvas.lowGraphic) {
            for(var1 = 0; var1 < 2; ++var1) {
               GameScreen.addEffectEnd((short)168, this.CM[this.CI][2], super.x + this.CM[this.CI][0] * (var1 == 0 ? 1 : -1), super.y + this.CM[this.CI][1], super.Dir, super.objMainEff);
            }
         }

         ++this.CI;
         if (this.CI >= this.CM.length) {
            this.removeEff();
         }
      }

   }

   private void update_Ussop_S3_L1() {
      int var2;
      if (super.f == 10 && !this.checkNullObject((int)3)) {
         int var1 = super.toX - super.x;
         var2 = super.toY - this.objBeFireMain.hOne - super.y - 30;
         this.create_Speed(var1, var2, (Point_Focus)null);
         var1 = CRes.AA(var1, var2);
         super.frame = this.setFrameAngle(var1);
         if (super.typeEffect != 69 && super.typeEffect != 194) {
            GameScreen.addEffectEnd((short)5, 0, super.x, super.y, super.Dir, super.objMainEff);
         }

         super.fRemove += 10;
      }

      if (super.f >= super.fRemove) {
         byte var3 = 0;
         if (super.typeEffect == 68) {
            var3 = 1;
         } else if (super.typeEffect == 69) {
            this.addVir(5, 5, 10, true);
            var3 = 2;
            GameScreen.addEffectEnd((short)48, 0, super.x - 30 + CRes.random_Am_0(10), super.y - 30 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)48, 0, super.x + 30 + CRes.random_Am_0(10), super.y - 30 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         } else if (super.typeEffect == 194) {
            this.addVir(5, 5, 10, true);
            var3 = 2;
            GameScreen.addEffectEnd((short)120, 0, super.x - 30 + CRes.random_Am_0(10), super.y - 30 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)120, 0, super.x + 30 + CRes.random_Am_0(10), super.y - 30 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)120, 0, super.x - 60 + CRes.random_Am_0(10), super.y + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)120, 0, super.x + 60 + CRes.random_Am_0(10), super.y + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)48, var3, super.x, super.y, super.Dir, super.objMainEff);

         for(var2 = 0; var2 < super.vecObjsBeFire.size(); ++var2) {
            Object_Effect_Skill var4;
            MainObject var5;
            if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var2)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               float var10000;
               if (super.typeEffect == 67) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  this.setAva(0, var5);
                  GameScreen.addEffectEnd((short)1, 0, var5.x, var5.y - var5.hOne / 2, super.Dir, super.objMainEff);
               } else if (super.typeEffect == 68) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  this.setAva(1, var5);
                  GameScreen.addEffectEnd((short)1, 0, var5.x + CRes.random_Am_0(10), var5.y - var5.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd((short)1, 0, var5.x + CRes.random_Am_0(10), var5.y - var5.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
               } else if (super.typeEffect == 69) {
                  if (this.isAddSound) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }

                  if (var2 == 0) {
                     this.setAva(2, var5);
                  } else {
                     GameScreen.addEffectEnd_ObjTo((short)49, 0, super.x, super.y, (short)var5.ID, (byte)var5.typeObject, super.Dir, super.objMainEff);
                  }
               }

               GameScreen.addEffectEnd((short)93, 2, var5.x + CRes.random_Am_0(10), var5.y - var5.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            }
         }

         this.removeEff();
      }

   }

   private void updateMohji_1() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if ((super.f < 3 || super.f > 11) && (super.f < 26 || super.f > 30)) {
            super.objFireMain.isTanHinh = false;
         } else {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 8) {
            byte var1 = 20;
            if (super.Dir == 2) {
               var1 = -20;
            }

            super.objFireMain.x = super.toX + var1;
            super.objFireMain.y = super.toY;
         }

         if (super.f == 12 || super.f == 16) {
            this.addSound((byte)7);
         }

         if (super.f == 12) {
            GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(10), super.toY - 5 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         }

         if (super.f == 20) {
            GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(10), super.toY - 5 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         }

         if (super.f == 30) {
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateMohji_2() {
      if (super.f == 2) {
         this.addSound((byte)7);
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(10), super.toY + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         this.setAva(0, this.objBeFireMain);
      }

      if (super.f == 6) {
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(10), super.toY + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         this.setAva(0, this.objBeFireMain);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateBuggy_1() {
      if (super.f == 2) {
         this.addSound((byte)19);
         if (!this.checkNullObject((int)1) && super.objFireMain.plashNow != null) {
            super.objFireMain.plashNow.AA((byte)1);
         }

         Point_Focus var1 = new Point_Focus();
         int var2 = super.toX - super.x;
         int var3 = super.toY - super.y;
         var1.AB = super.Dir;
         var1.frame = 1;
         this.create_Speed(var2, var3, var1);
         this.VecEff.addElement(var1);
      }

      for(int var6 = 0; var6 < this.VecEff.size(); ++var6) {
         Point_Focus var7;
         (var7 = (Point_Focus)this.VecEff.elementAt(var6)).update_Vx_Vy();
         if (var7.AG >= var7.fRe) {
            if (var7.frame == 1) {
               this.addSound((byte)7);
               Point_Focus var8 = new Point_Focus();
               GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
               int var4 = super.x;
               int var5 = super.y;
               super.x = super.toX;
               super.y = super.toY;
               super.toX = var4;
               super.toY = var5;
               var4 = super.toX - super.x;
               var5 = super.toY - super.y;
               var8.AB = super.Dir;
               var8.frame = 2;
               this.create_Speed(var4, var5, var8);
               this.VecEff.addElement(var8);
            }

            this.VecEff.removeElement(var7);
            --var6;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)1) && super.objFireMain.plashNow != null) {
            super.objFireMain.plashNow.AA((byte)0);
         }

         this.removeEff();
      }

   }

   private void updateBuggy_2() {
      if (super.f == 18) {
         GameScreen.addEffectEnd((short)30, 0, this.x1000, this.y1000, 300, super.Dir, super.objMainEff);
      }

      if (super.f == 28) {
         this.addSound((byte)15);
         this.addVir(2, 6, 10, false);
         Point_Focus var1 = new Point_Focus();
         short var2 = -260;
         if (super.Dir == 2) {
            var2 = 260;
         }

         (var1 = this.create_Speed(var2, 0, var1)).y = this.y1000;
         this.VecEff.addElement(var1);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point_Focus var4;
         (var4 = (Point_Focus)this.VecEff.elementAt(var3)).update_Vx_Vy();
         if (var4.AG >= var4.fRe) {
            this.VecEff.removeElement(var4);
            --var3;
         }
      }

      if (super.f > 28) {
         this.addSound((byte)19);
         if (super.f % 2 == 0 && this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var5 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var6;
            if (var5 != null && (var6 = MainObject.get_Object((int)var5.ID, (byte)var5.tem)) != null) {
               this.setAva(1, var6);
               GameScreen.addEffectEnd((short)48, 1, var6.x, var6.y - var6.hOne / 2, super.Dir, super.objMainEff);
            }
         }
      }

   }

   private void updateCabaji_1() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var2;
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)1, 0, var2.AR.x + CRes.random_Am_0(5), var2.AR.y - var2.AR.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 2) {
         this.addSound((byte)18);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var6;
            MainObject var7;
            if ((var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var7 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var3 = new Point_Focus();
               int var4 = var7.x - super.x;
               int var5 = var7.y - var7.hOne / 2 - super.y;
               (var3 = this.create_Speed(var4, var5, var3)).AR = var7;
               var3.frame = CRes.random(2);
               this.VecEff.addElement(var3);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateDonKrieg_3() {
      if (super.f == 18) {
         this.addSound((byte)15);
         byte var1 = -45;
         if (super.Dir == 2) {
            var1 = 45;
         }

         GameScreen.addEffectEnd((short)57, 0, super.x + var1, super.y + 12, super.Dir, super.objMainEff);
      }

      if (super.f > 18 && super.f < 28) {
         if (super.f == 20 || super.f == 26) {
            this.addSound((byte)14);
         }

         if (super.f % 2 == 1) {
            int var2 = -40 - ((super.f - 18) / 2 + 1) * 30;
            if (super.Dir == 2) {
               var2 = 40 + ((super.f - 18) / 2 + 1) * 30;
            }

            GameScreen.addEffectEnd((short)58, 0, super.x + var2, super.y + 30, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)59, 0, super.x + var2, super.y + 30, super.Dir, super.objMainEff);
            this.addVir(2, 5, 10, false);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateDonKrieg_2() {
      if (super.f == 2) {
         this.CJ += this.x1000;
      }

      int var3;
      if (super.f == 10) {
         this.addSound((byte)32);
         if (!this.checkNullObject((int)2)) {
            this.addVir(3, 5, 10, false);
            Point_Focus var1 = new Point_Focus();
            int var2 = this.objBeFireMain.x - super.x;
            var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
            var1 = this.create_Speed(var2, var3, var1);
            GameScreen.addEffectEnd((short)12, 1, super.x, super.y, super.Dir, super.objMainEff);
            this.VecEff.addElement(var1);
         }
      }

      int var8;
      Point_Focus var9;
      for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
         (var9 = (Point_Focus)this.VecEff.elementAt(var8)).update_Vx_Vy();
         if (var9.AG >= var9.fRe) {
            super.x = var9.x;
            super.y = var9.y;

            for(var3 = 0; var3 < super.vecObjsBeFire.size(); ++var3) {
               Object_Effect_Skill var4;
               MainObject var5;
               if ((var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var3)) != null && (var5 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
                  super.vMax = 8 + CRes.random(5);
                  Point_Focus var6 = new Point_Focus();
                  int var10 = var5.x - super.x;
                  int var7 = var5.y - var5.hOne / 2 - super.y;
                  (var6 = this.create_Speed(var10, var7, var6)).AR = var5;
                  this.VecSubEff.addElement(var6);
               }
            }

            GameScreen.addEffectEnd((short)57, 0, super.x, super.y, super.Dir, super.objMainEff);
            if (this.VecSubEff.size() < 8) {
               for(var3 = 0; var3 < 8 - this.VecEff.size(); ++var3) {
                  super.vMax = 8 + CRes.random(5);
                  Point_Focus var11 = new Point_Focus();
                  int var12 = CRes.random_Am_0(120);
                  int var13 = CRes.random_Am_0(50);
                  var11 = this.create_Speed(var12, var13, var11);
                  this.VecSubEff.addElement(var11);
               }
            }

            this.VecEff.removeElement(var9);
            --var8;
         }
      }

      for(var8 = 0; var8 < this.VecSubEff.size(); ++var8) {
         (var9 = (Point_Focus)this.VecSubEff.elementAt(var8)).update_Vx_Vy();
         if (var9.AG == var9.fRe && var9.AR != null) {
            GameScreen.addEffectEnd((short)1, 0, var9.x + CRes.random_Am_0(5), var9.y + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            this.setAva(0, var9.AR);
         }

         if (var9.AG > var9.fRe + 8) {
            this.VecSubEff.removeElement(var9);
            --var8;
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0 && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateDonKrieg_1() {
      if (super.f == 2) {
         super.x += this.x1000;
         this.x1000 = super.x;
         this.y1000 = super.y;
      }

      int var1;
      Point_Focus var8;
      if (super.f == 10) {
         this.addSound((byte)32);

         int var4;
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               for(var4 = 0; var4 < 2; ++var4) {
                  Point_Focus var5 = new Point_Focus();
                  if (super.Dir == 0) {
                     super.x += CRes.random(10);
                  } else {
                     super.x -= CRes.random(10);
                  }

                  super.y += CRes.random_Am_0(25);
                  int var7 = var3.x - super.x;
                  int var6 = var3.y - var3.hOne / 2 - super.y;
                  var5 = this.create_Speed(var7, var6, var5);
                  var7 = CRes.AA(var7, var6);
                  var5.frame = this.setFrameAngle(var7);
                  GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
                  if (var4 == 0) {
                     var5.AR = var3;
                  }

                  this.VecEff.addElement(var5);
                  super.x = this.x1000;
                  super.y = this.y1000;
               }
            }
         }

         if (this.VecEff.size() < 8) {
            for(var1 = 0; var1 < 8 - this.VecEff.size(); ++var1) {
               var8 = new Point_Focus();
               if (super.Dir == 0) {
                  super.x += CRes.random(10);
               } else {
                  super.x -= CRes.random(10);
               }

               super.y += CRes.random_Am_0(25);
               int var9 = 120 + CRes.random_Am_0(30);
               var4 = CRes.random_Am_0(50);
               if (super.Dir == 0) {
                  var9 = -var9;
               }

               var8 = this.create_Speed(var9, var4, var8);
               int var10 = CRes.AA(var9, var4);
               var8.frame = this.setFrameAngle(var10);
               GameScreen.addEffectEnd((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
               this.VecEff.addElement(var8);
               super.x = this.x1000;
               super.y = this.y1000;
            }
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var8 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var8.AG == var8.fRe && var8.AR != null) {
            GameScreen.addEffectEnd((short)1, 0, var8.x + CRes.random_Am_0(5), var8.y + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            this.setAva(0, var8.AR);
         }

         if (var8.AG > var8.fRe + 10) {
            this.VecEff.removeElement(var8);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateKuro_2() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)1)) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

      if (super.f == 10 || super.f == 24) {
         this.addSound((byte)10);
      }

      if (super.f == 32 || super.f == 18) {
         this.addSound((byte)7);
      }

      Point var1;
      if (super.f <= 8) {
         if (super.f == 0 || super.f == 4) {
            super.objFireMain.x = super.x + 3;
         }

         if (super.f == 2 || super.f == 8) {
            super.objFireMain.x = super.x - 3;
         }
      } else if (super.f < super.fRemove) {
         if (super.f == 10) {
            this.AA(0, super.toX + CRes.random_Am_0(30), super.toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
            this.setAva(0, this.objBeFireMain);
         } else if (super.f % 4 == 0) {
            this.AA(CRes.random(4), super.toX + CRes.random_Am_0(30), super.toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
            this.setAva(0, this.objBeFireMain);
         }

         if (super.objFireMain.isTanHinh) {
            if (CRes.random(5) == 0) {
               super.objFireMain.isTanHinh = false;
            }
         } else if (CRes.random(3) == 0) {
            super.objFireMain.isTanHinh = true;
            super.objFireMain.x = super.toX + CRes.random_Am_0(30);
            super.objFireMain.y = super.toY + CRes.random_Am_0(30);
         }

         if (CRes.random(5) == 0) {
            (var1 = new Point()).x = super.toX + CRes.random_Am_0(30);
            var1.y = super.toY + CRes.random_Am_0(30);
            var1.frame = 4;
            var1.fRe = 3;
            var1.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var1);
            this.addVir(3, 5, 10, false);
         }
      }

      if (super.f == super.fRemove - 2) {
         (var1 = new Point()).x = this.x1000;
         var1.y = this.y1000;
         var1.frame = 4;
         var1.fRe = 2;
         var1.dis = super.Dir;
         this.VecEff.addElement(var1);
      }

      if (super.f == super.fRemove) {
         super.objFireMain.isTanHinh = false;
         super.objFireMain.x = this.x1000;
         super.objFireMain.y = this.y1000;
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var3)).update();
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

   }

   private void AA(int var1, int var2, int var3, int var4) {
      Point var5;
      if (var1 == 0) {
         for(var1 = 0; var1 < var4; ++var1) {
            (var5 = new Point()).y = var3;
            if (super.Dir == 2) {
               var5.x = var2 + var1 * 7;
            } else {
               var5.x = var2 - var1 * 7;
            }

            var5.vy = -7;
            var5.frame = 2;
            var5.fRe = 5;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else if (var1 == 1) {
         for(var1 = 0; var1 < var4; ++var1) {
            (var5 = new Point()).y = var3 + var1 * 7;
            var5.x = var2;
            var5.vx = -5;
            var5.frame = 3;
            var5.fRe = 5;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else if (var1 == 2) {
         for(var1 = 0; var1 < var4; ++var1) {
            (var5 = new Point()).y = var3 + var1 * 7;
            var5.x = var2;
            var5.vx = -3;
            if (super.Dir == 0) {
               var5.vx = 3;
            }

            var5.frame = 0;
            var5.fRe = 4;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else {
         if (var1 == 3) {
            for(var1 = 0; var1 < var4; ++var1) {
               (var5 = new Point()).y = var3 + var1 * 7;
               var5.x = var2;
               var5.vx = -3;
               if (super.Dir == 0) {
                  var5.vx = 3;
               }

               var5.frame = 1;
               var5.fRe = 4;
               var5.dis = CRes.random(2) == 0 ? 0 : 2;
               this.VecEff.addElement(var5);
            }
         }

      }
   }

   private void AB(int var1, int var2, int var3, int var4) {
      Point var5;
      if (var1 == 0) {
         for(var1 = 0; var1 < 2; ++var1) {
            (var5 = new Point()).y = var3;
            if (super.Dir == 2) {
               var5.x = var2 + var1 * 15;
            } else {
               var5.x = var2 - var1 * 15;
            }

            var5.vy = -7;
            var5.frame = 2;
            var5.fRe = 4;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else {
         if (var1 == 1) {
            for(var1 = 0; var1 < 2; ++var1) {
               (var5 = new Point()).y = var3 + var1 * 15;
               var5.x = var2;
               var5.vx = -5;
               var5.frame = 3;
               var5.fRe = 4;
               var5.dis = CRes.random(2) == 0 ? 0 : 2;
               this.VecEff.addElement(var5);
            }
         }

      }
   }

   private void updateKuro_1() {
      byte var1;
      if (super.f == 2) {
         var1 = 14;
         if (super.Dir == 2) {
            var1 = -14;
         }

         super.x = super.toX + var1;
         super.y = super.toY - super.objFireMain.hOne / 2;
         super.objFireMain.x = super.x;
         super.objFireMain.y = super.toY;
      }

      if (super.f == 4) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 5) {
         var1 = -14;
         if (super.Dir == 2) {
            var1 = 14;
         }

         super.x += var1;

         for(int var2 = 0; var2 < 3; ++var2) {
            Point var4;
            (var4 = new Point()).y = super.y;
            if (super.Dir == 2) {
               var4.x = super.x + var2 * 7;
            } else {
               var4.x = super.x - var2 * 7;
            }

            var4.vy = -10;
            var4.frame = 2;
            var4.fRe = 5;
            this.VecEff.addElement(var4);
         }
      }

      if (super.f > 5) {
         if (super.f < 11) {
            super.objFireMain.dy = 10 * (super.f - 6);
            this.objBeFireMain.dy = 12 * (super.f - 6);
         } else if (super.f < 15) {
            super.objFireMain.dy = 50;
            this.objBeFireMain.dy = 60;
            super.objFireMain.vx = -5;
            if (super.Dir == 0) {
               super.objFireMain.vx = 5;
            }
         }
      }

      if (super.f == 8) {
         this.addSound((byte)7);
         this.setAva(0, this.objBeFireMain);
      }

      Point var3;
      int var5;
      for(var5 = 0; var5 < this.VecEff.size(); ++var5) {
         (var3 = (Point)this.VecEff.elementAt(var5)).update();
         if (var3.f >= var3.fRe) {
            this.VecEff.removeElement(var3);
            --var5;
         }
      }

      if (super.f == 13) {
         this.addSound((byte)7);
         this.setAva(1, this.objBeFireMain);

         for(var5 = 0; var5 < 3; ++var5) {
            (var3 = new Point()).y = super.y - super.objFireMain.dy + var5 * 7;
            var3.x = super.x;
            var3.vx = -3;
            if (super.Dir == 0) {
               var3.vx = 3;
            }

            var3.frame = 0;
            var3.fRe = 4;
            this.VecEff.addElement(var3);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.addVir(3, 5, 10, false);
         super.objFireMain.dy = 0;
         this.removeEff();
      }

   }

   private void updateNyaban_2() {
      if (super.f <= super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f == 1) {
            this.addSound((byte)3);
         }

         if (super.f < 5) {
            super.objFireMain.dy = 40 * super.f;
            super.objFireMain.vx = super.vx;
         } else if (super.f < 8) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 5) {
            super.objFireMain.x = super.toX;
            super.objFireMain.vx = 0;
            super.objFireMain.dy = 100;
         }

         if (super.f == 6) {
            this.addSound((byte)14);
            this.addVir(2, 5, 10, false);
            super.objFireMain.dy = 0;
            this.setAva(1, this.objBeFireMain);
            GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(5), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)90, 0, super.toX, this.objBeFireMain.y + 10, super.Dir, super.objMainEff);
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.toX = super.x;
            super.objFireMain.isTanHinh = false;
            super.objFireMain.dy = 0;
         }

         this.setAva(0, this.objBeFireMain);
         this.removeEff();
      }
   }

   private void updateNyaban_3() {
      if (super.f > super.fRemove || this.checkNullObject((int)1)) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.toX = super.x;
            super.objFireMain.dy = 0;
            super.objFireMain.vx = 0;
         }

         this.removeEff();
      }

      if (super.f < 3) {
         super.objFireMain.dy = 10 * super.f;
         super.objFireMain.vx = super.vx;
      } else if (super.f < 6) {
         super.objFireMain.dy = 10 * (6 - super.f);
         super.objFireMain.vx = super.vx;
      }

      if (super.f == 6) {
         super.objFireMain.dy = 0;
         super.objFireMain.vx = 0;
      }

      if (super.f == 17) {
         super.objFireMain.Dir = super.objFireMain.Dir == 0 ? 2 : 0;
         super.vx = 20;
         if (super.objFireMain.Dir == 0) {
            super.vx = -20;
         }

         this.setAva(0, this.objBeFireMain);
      }

      if (super.f == 8 || super.f == 13) {
         this.addSound((byte)7);
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(5), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
      }

      if (super.f > 17) {
         if (super.f < 22) {
            super.objFireMain.dy = 5 * (super.f - 17);
            super.objFireMain.vx = super.vx;
            return;
         }

         if (super.f < 26) {
            super.objFireMain.dy = 5 * (25 - super.f);
            super.objFireMain.vx = super.vx;
         }
      }

   }

   private void updateJango_1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         var2 = (Point_Focus)this.VecEff.elementAt(var1);
         Point var3;
         (var3 = new Point(var2.x, var2.y)).frame = CRes.random(super.fraImgSubEff.nFrame);
         this.VecSubEff.addElement(var3);
         var2.update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)1, 0, var2.AR.x + CRes.random_Am_0(5), var2.AR.y - var2.AR.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var6;
         ++(var6 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var6.f >= 2) {
            this.VecSubEff.removeElement(var6);
            --var1;
         }
      }

      if (super.f == 2) {
         this.addSound((byte)18);

         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var7;
            MainObject var8;
            if ((var7 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var7.ID, (byte)var7.tem)) != null) {
               var2 = new Point_Focus();
               int var4 = var8.x - super.x;
               int var5 = var8.y - var8.hOne / 2 - super.y;
               (var2 = this.create_Speed(var4, var5, var2)).AR = var8;
               this.VecEff.addElement(var2);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateCabaji_2() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f == 1) {
            this.addSound((byte)3);
         }

         if (super.f < 5) {
            super.objFireMain.dy = 70 * super.f;
         } else if (super.f >= 5 && super.f <= 10) {
            super.objFireMain.dy = 330;
         } else if (super.f <= 13) {
            super.objFireMain.dy = (13 - super.f) * 110;
         }

         if (super.f == 10) {
            super.objFireMain.x = super.toX;
            super.objFireMain.y = super.toY;
         }

         if (super.f == 13) {
            this.addSound((byte)15);
            this.addVir(3, 5, 10, false);
            super.objFireMain.dy = 0;
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(5), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)9, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            this.setAva(2, this.objBeFireMain);
            if (super.typeEffect == 22) {
               GameScreen.addEffectEnd((short)45, 0, super.toX, super.toY + 20, super.Dir, super.objMainEff);
            }
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.toX = super.x;
         }

         this.removeEff();
      }
   }

   private void updateArlong_3() {
      int var1;
      if (super.f == 12) {
         this.addSound((byte)15);
         if (super.vecObjsBeFire.size() > 1) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var2;
               MainObject var5;
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var5 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                  this.setAva(1, var5);
               }
            }
         } else {
            this.setAva(1, this.objBeFireMain);
         }

         GameScreen.addEffectEnd((short)8, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         Point var3;
         (var3 = new Point(super.x + this.CL[4][0], super.y + 30)).vx = -10;
         if (super.Dir == 2) {
            var3.vx = 10;
         }

         var3.fRe = 12;
         this.VecEff.addElement(var3);
      }

      if (super.f == 18 || super.f == 22) {
         this.addSound((byte)14);
      }

      if (super.f == 13) {
         this.addVir(1, 6, 12, false);
         byte var4 = -10;
         if (super.Dir == 2) {
            var4 = 10;
         }

         GameScreen.addEffectEnd_ToX_ToY((short)62, 0, super.x + this.CL[4][0], super.y + 30, (int)(super.x + this.CL[4][0] + var4 * 12), (int)(super.y + 30), super.Dir, super.objMainEff);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var6;
         (var6 = (Point)this.VecEff.elementAt(var1)).update();
         if (var6.f < var6.fRe - 2) {
            var6.frame = CRes.random(2);
         } else {
            var6.frame = 2;
         }

         if (super.f % 3 == 0) {
            GameScreen.addEffectEnd((short)59, 0, var6.x, var6.y, super.Dir, super.objMainEff);
         }

         if (var6.f >= var6.fRe) {
            this.VecEff.removeElement(var6);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateArlong_2() {
      if (super.f >= super.fRemove && this.VecEff.size() == 0 || this.checkNullObject((int)3)) {
         if (!this.checkNullObject((int)1) && super.objFireMain.plashNow != null) {
            super.objFireMain.plashNow.AA((byte)0);
         }

         this.removeEff();
      }

      if (super.f == 2 || super.f == 12 || super.f == 22) {
         this.addSound((byte)19);
      }

      if (super.f == 2) {
         this.addVir(3, 5, 10, false);
         super.objFireMain.isTanHinh = true;
         if (super.objFireMain.plashNow != null) {
            super.objFireMain.plashNow.AA((byte)1);
         }

         Point_Focus var1 = new Point_Focus();
         int var2 = this.objBeFireMain.x - super.x;
         int var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
         var1.AR = this.objBeFireMain;
         (var1 = this.create_Speed(var2, var3, var1)).frame = 0;
         var1.dis = super.Dir;
         this.VecEff.addElement(var1);
      }

      for(int var6 = 0; var6 < this.VecEff.size(); ++var6) {
         Point_Focus var7;
         (var7 = (Point_Focus)this.VecEff.elementAt(var6)).update_Vx_Vy();
         if (var7.AG >= var7.fRe) {
            if (var7.frame == 2) {
               super.objFireMain.isTanHinh = false;
               if (super.objFireMain.plashNow != null) {
                  super.objFireMain.plashNow.AA((byte)0);
               }

               this.VecEff.removeElement(var7);
               --var6;
            } else if (var7.AG == var7.fRe) {
               GameScreen.addEffectEnd((short)8, 0, super.toX, super.toY, super.Dir, super.objMainEff);
               this.setAva(1, this.objBeFireMain);
            }
         }

         int var4;
         int var5;
         Point_Focus var8;
         if (var7.frame == 0 && var7.AG >= 8) {
            var8 = new Point_Focus();
            var4 = this.objBeFireMain.x - var7.x;
            var5 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - var7.y;
            var8.AR = this.objBeFireMain;
            (var8 = this.create_Speed(var4, var5, var8, var7.x, var7.y, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).frame = 1;
            var8.dis = super.Dir == 0 ? 2 : 0;
            this.VecEff.addElement(var8);
            this.VecEff.removeElement(var7);
            --var6;
         } else if (var7.AG >= 22 && var7.frame == 1) {
            super.vMax = 20;
            var8 = new Point_Focus();
            var4 = super.x - var7.x;
            var5 = super.y - var7.y;
            (var8 = this.create_Speed(var4, var5, var8, var7.x, var7.y, super.x, super.y)).frame = 2;
            var8.dis = super.Dir;
            this.VecEff.addElement(var8);
            this.VecEff.removeElement(var7);
            --var6;
         }
      }

   }

   private void updateArlong_1() {
      int var1;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++((Point)this.VecEff.elementAt(var1)).f;
      }

      if (super.f == 6) {
         this.addSound((byte)33);
         this.addVir(3, 5, 10, false);
         if (super.vecObjsBeFire.size() > 1) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var2;
               MainObject var3;
               if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
                  GameScreen.addEffectEnd((short)8, 0, var3.x, var3.y - var3.hOne / 2, super.Dir, super.objMainEff);
                  this.setAva(1, var3);
               }
            }
         } else {
            GameScreen.addEffectEnd((short)8, 0, super.toX, super.toY, super.Dir, super.objMainEff);
            this.setAva(1, this.objBeFireMain);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateKurobi_2() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f >= 10 && super.f <= 16) {
            if (super.f < 13) {
               super.objFireMain.dy = 10 * (super.f - 10);
               super.objFireMain.vx = super.vx;
            } else if (super.f < 16) {
               super.objFireMain.dy = 10 * (16 - super.f);
               super.objFireMain.vx = super.vx;
            }

            if (super.f == 16) {
               if (!this.checkNullObject((int)2)) {
                  GameScreen.addEffectEnd((short)25, 0, this.objBeFireMain.x, this.objBeFireMain.y - (this.objBeFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               }

               super.objFireMain.dy = 0;
               super.objFireMain.vx = 0;
               this.setAva(1, this.objBeFireMain);
            }
         }

         if (super.f == 18) {
            GameScreen.addEffectEnd((short)30, 0, super.x, super.y + 10, 200, super.Dir, super.objMainEff);
         }

         if (super.f == 26) {
            this.addSound((byte)5);
            if (!this.checkNullObject((int)2)) {
               this.addVir(2, 5, 10, false);
               GameScreen.addEffectEnd((short)25, 0, this.objBeFireMain.x, this.objBeFireMain.y - (this.objBeFireMain.hOne / 3 << 1) + 5, super.Dir, super.objMainEff);
            }

            super.objFireMain.dy = 0;
            super.objFireMain.vx = 0;
            this.setAva(1, this.objBeFireMain);
         }

      } else {
         this.removeEff();
      }
   }

   private void updateKurobi_1() {
      if (super.f == 12 || super.f == 27) {
         this.addSound((byte)13);
         if (!this.checkNullObject((int)2)) {
            this.addVir(2, 5, 10, false);
            GameScreen.addEffectEnd((short)25, 0, this.objBeFireMain.x, this.objBeFireMain.y - (this.objBeFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         }
      }

      if (super.f == 15) {
         GameScreen.addEffectEnd((short)30, 0, super.x, super.y, 300, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateChu_2() {
      int var1;
      Point_Focus var2;
      if (super.f >= 10 && super.f < super.fRemove && super.f % 4 == 0) {
         int var4;
         if (super.f % 8 == 0 && this.CI < super.vecObjsBeFire.size()) {
            this.addSound((byte)21);
            Object_Effect_Skill var5 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var6;
            if (var5 != null && (var6 = MainObject.get_Object((int)var5.ID, (byte)var5.tem)) != null) {
               Point_Focus var7 = new Point_Focus();
               var4 = var6.x - super.x;
               var1 = var6.y - var6.hOne / 2 - super.y;
               var7.AR = var6;
               var7 = this.create_Speed(var4, var1, var7);
               this.VecEff.addElement(var7);
            }

            this.addVir(3, 5, 10, false);
         } else {
            for(var1 = 0; var1 < 2; ++var1) {
               var2 = new Point_Focus();
               int var3 = 120 + CRes.random_Am_0(30);
               var4 = CRes.random_Am_0(50);
               if (super.Dir == 0) {
                  var3 = -var3;
               }

               var2 = this.create_Speed(var3, var4, var2);
               this.VecEff.addElement(var2);
            }
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)61, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            if (var2.AR != null) {
               this.setAva(0, var2.AR);
            }

            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateChu_1() {
      if (super.f == 10 || super.f == 14 || super.f == 18) {
         this.addSound((byte)21);
         if (!this.checkNullObject((int)2)) {
            Point_Focus var1 = new Point_Focus();
            int var2 = this.objBeFireMain.x - super.x;
            int var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
            var1 = this.create_Speed(var2, var3, var1);
            this.VecEff.addElement(var1);
         }
      }

      for(int var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var5;
         (var5 = (Point_Focus)this.VecEff.elementAt(var4)).update_Vx_Vy();
         if (var5.AG >= var5.fRe) {
            GameScreen.addEffectEnd((short)61, 0, var5.x, var5.y, super.Dir, super.objMainEff);
            if (CRes.random(3) == 0) {
               this.setAva(0, this.objBeFireMain);
            }

            this.VecEff.removeElement(var5);
            --var4;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateHachi_2() {
      if (super.f == super.fRemove - 4 && !this.checkNullObject((int)2)) {
         Point_Focus var1 = new Point_Focus();
         int var2 = this.objBeFireMain.x - super.x;
         int var3 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
         var1 = this.create_Speed(var2, var3, var1);
         this.VecEff.addElement(var1);
      }

      for(int var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var5;
         (var5 = (Point_Focus)this.VecEff.elementAt(var4)).update_Vx_Vy();
         byte var6 = 0;
         if (super.typeEffect == 150) {
            var6 = 1;
         } else if (super.typeEffect != 113) {
            var6 = 2;
         }

         if (var5.AG >= var5.fRe) {
            if (var6 < 2) {
               GameScreen.addEffectEnd((short)60, var6, var5.x, var5.y, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)34, 0, var5.x, var5.y, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)87, super.frame, var5.x, var5.y, super.Dir, super.objMainEff);
            }

            this.VecEff.removeElement(var5);
            --var4;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateHachi_1() {
      if (super.f == 1) {
         this.addSound((byte)4);
      }

      if (super.f < 10 && super.f % 3 == 0) {
         this.setAva(0, this.objBeFireMain);
         GameScreen.addEffectEnd((short)1, 0, super.toX + CRes.random_Am_0(15), super.toY + CRes.random_Am_0(15), super.Dir, super.objMainEff);
         this.addVir(3, 5, 10, false);
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateGhin_2() {
      if (super.f == 1 || super.f == 8) {
         this.addSound((byte)10);
      }

      int var1;
      if (super.f == 4) {
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            ((Point)this.VecEff.elementAt(var1)).dis = 2;
         }
      }

      Point var2;
      if (super.f == 12) {
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            var2 = (Point)this.VecEff.elementAt(var1);
            if (!this.checkNullObject((int)1)) {
               if (super.Dir == 0) {
                  var2.x = super.objFireMain.x + 20;
               } else {
                  var2.x = super.objFireMain.x - 20;
               }
            } else {
               var2.x = super.x;
            }

            var2.y = super.objFireMain.y - 28 + var1 * 4;
            var2.vx = super.vx;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (!this.checkNullObject((int)2) && var1 == 0 && super.f % 4 == 0 && CRes.abs(var2.x - this.objBeFireMain.x) < 30) {
            GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(5), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(5), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.NF = true;
            super.objFireMain.vx = 0;
         }

         this.removeEff();
      } else {
         if (super.f >= 12 && !this.checkNullObject((int)1)) {
            super.objFireMain.vx = super.vx;
         }

      }
   }

   private void updatePearl_2() {
      int var5;
      if (super.f > 10 && super.f < super.fRemove && super.f % 4 == 0) {
         this.addSound((byte)19);
         if (this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var1 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            if (var1 != null) {
               MainObject var2;
               int var3 = (var2 = MainObject.get_Object((int)var1.ID, (byte)var1.tem)).x - super.x;
               var5 = var2.y - var2.hOne / 2 - super.y;
               Point_Focus var4 = new Point_Focus();
               (var4 = this.create_Speed(var3, var5, var4)).frame = CRes.random(3);
               var4.AR = var2;
               this.VecSubEff.addElement(var4);
            }
         } else if (!this.checkNullObject((int)2)) {
            var5 = this.objBeFireMain.x + CRes.random_Am_0(30) - super.x;
            int var6 = this.objBeFireMain.y + CRes.random_Am_0(30) - this.objBeFireMain.hOne / 2 - super.y;
            Point_Focus var8 = new Point_Focus();
            (var8 = this.create_Speed(var5, var6, var8)).frame = CRes.random(3);
            this.VecSubEff.addElement(var8);
         }
      }

      for(var5 = 0; var5 < this.VecSubEff.size(); ++var5) {
         Point_Focus var7;
         (var7 = (Point_Focus)this.VecSubEff.elementAt(var5)).update_Vx_Vy();
         if (var7.AG >= var7.fRe) {
            if (var7.AR != null) {
               GameScreen.addEffectEnd_ObjTo((short)55, 0, var7.AR.x, var7.AR.y - var7.AR.hOne / 2, (short)var7.AR.ID, (byte)var7.AR.typeObject, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)55, 0, var7.x, var7.y, super.Dir, super.objMainEff);
            }

            this.VecSubEff.removeElement(var7);
            --var5;
         }
      }

      if (super.f >= super.fRemove && this.VecSubEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateUrgot3() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         if ((var2 = (Point)this.VecEff.elementAt(var1)).vy > 0 && var2.y >= 0 || var2.vy < 0 && var2.y <= -30) {
            var2.vy = -var2.vy;
         }

         var2.y += var2.vy;
      }

      if (super.f == 30 && !this.checkNullObject((int)1)) {
         super.objFireMain.x = super.toX;
         super.objFireMain.y = super.toY;
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updatexerath3() {
      super.vY1000 += super.AZ;
      this.x1000 += super.vX1000;
      this.y1000 += super.vY1000;
      if (super.f == super.fRemove) {
         this.x1000 = super.toX * 1000;
         this.y1000 = super.toY;
         GameScreen.addEffectEnd((short)68, 0, super.toX, super.toY + 10, super.Dir, super.objMainEff);
      }

      int var1;
      Point var2;
      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else {
         for(var1 = 0; var1 <= 0; ++var1) {
            (var2 = new Point()).x = this.x1000 / 1000;
            var2.y = this.y1000;
            if (CRes.random(3) == 0) {
               var2.AY = super.fraImgSubEff;
            } else {
               var2.AY = super.fraImgSub2Eff;
            }

            this.VecEff.addElement(var2);
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= 8) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

   }

   private void updateXerath2() {
      if (super.f > 10) {
         for(int var1 = 0; var1 < this.CT.size(); ++var1) {
            ++((Point_Focus)this.CT.elementAt(var1)).AG;
         }
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

   }

   private void updateXerath1() {
      if (super.f == 5 && !this.checkNullObject((int)1)) {
         GameScreen.addEffectEnd((short)30, 2, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, (short)((int)((long)super.timeEnd - (GameCanvas.timeNow - super.timeBegin) - 200L)), super.Dir, super.objMainEff);
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

   }

   private void updateNoTheoHuong_1() {
      if (super.f == 5 && !this.checkNullObject((int)1)) {
         GameScreen.addEffectEnd((short)30, 2, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, (short)((int)((long)super.timeEnd - (GameCanvas.timeNow - super.timeBegin) - 200L)), super.Dir, super.objMainEff);
         super.x = super.objFireMain.x;
         super.y = super.objFireMain.y;
      }

      int var1;
      Point var2;
      if (super.f == 20 || super.f == 40) {
         for(var1 = 0; var1 < 4; ++var1) {
            (var2 = new Point()).x = super.x + super.am_duong * 20;
            var2.y = super.y - 30 + var1 * 20;
            var2.vx = super.am_duong * 40;
            this.VecEff.addElement(var2);
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         ++(var2 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var2.f > 6 && var2.f % 2 == 0) {
            ++var2.frame;
         }

         if (var2.frame > 2) {
            this.VecSubEff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f % 3 == 1) {
            Point var3;
            (var3 = new Point()).x = var2.x;
            var3.y = var2.y;
            this.VecSubEff.addElement(var3);
            var2.x += var2.vx;
         }

         if (var2.f > 13) {
            this.VecEff.removeElement(var2);
         }
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

   }

   private void updateNoTheoHuong_2() {
      if (super.f < 30 && super.f % 6 == 3) {
         this.addVir(3, 5, 10, false);

         for(int var1 = 0; var1 < 4; ++var1) {
            GameScreen.addEffectEnd((short)52, 0, super.x + super.am_duong * 20 + super.am_duong * (super.f / 6) * 40, super.y - 30 + var1 * 20, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateNoNangLuong3() {
      for(int var1 = 0; var1 < this.CT.size(); ++var1) {
         Point_Focus var2;
         if ((var2 = (Point_Focus)this.CT.elementAt(var1)).AG == 0) {
            this.addVir(2, 6, 10, false);
            GameScreen.addEffectEnd((short)63, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)59, 0, var2.x, var2.y, super.Dir, super.objMainEff);
         }

         ++var2.AG;
         if (var2.AG >= 8) {
            this.CT.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.CT.size() == 0) {
         this.removeEff();
      }

   }

   private void updateNoNangLuong2() {
      if (super.f > 25) {
         for(int var1 = 0; var1 < this.CT.size(); ++var1) {
            ++((Point_Focus)this.CT.elementAt(var1)).AG;
         }
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

   }

   private void updateNoNangLuong1() {
      if (super.f == 5) {
         GameScreen.addEffectEnd((short)30, 2, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, (short)((int)((long)super.timeEnd - (GameCanvas.timeNow - super.timeBegin) - 200L)), super.Dir, super.objMainEff);
      }

      if (CRes.random(6) == 0) {
         for(int var1 = this.CI; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1);
            if (this.CI == GameScreen.vecPlayers.size() - 1) {
               this.CI = 0;
            }

            if (var2 != super.objFireMain && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, var2.x, var2.y) <= 220) {
               this.CI = var1 + 1;
               if (this.CI >= GameScreen.vecPlayers.size()) {
                  this.CI = 0;
               }

               GameScreen.addEffectEnd_ObjTo((short)22, 0, var2.x, var2.y - var2.hOne / 2, (short)super.objFireMain.ID, (byte)super.objFireMain.typeObject, (byte)super.objFireMain.Dir, super.objMainEff);
               break;
            }
         }
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd || this.checkNullObject((int)1)) {
         this.removeEff();
      }

   }

   private void updateGalio2() {
      int var1;
      int var2;
      Point var3;
      if (super.f == 2) {
         var1 = 0;

         for(var2 = 0; var2 < 8; ++var2) {
            var1 %= 360;
            var3 = new Point(super.x + CRes.getcos(var1) * 43 / 1000, super.y + CRes.getsin(var1) * 23 / 1000);
            this.VecEff.addElement(var3);
            GameScreen.addEffectEnd((short)66, 0, var3.x, var3.y, super.Dir, super.objMainEff);
            var1 += 45;
         }
      }

      if (super.f == 8) {
         var1 = 22;

         for(var2 = 0; var2 < 12; ++var2) {
            var1 %= 360;
            var3 = new Point(super.x + CRes.getcos(var1) * 65 / 1000, super.y + CRes.getsin(var1) * 40 / 1000);
            this.VecEff.addElement(var3);
            GameScreen.addEffectEnd((short)66, 0, var3.x, var3.y, super.Dir, super.objMainEff);
            var1 += 30;
         }
      }

      if (super.f == 14) {
         var1 = 45;
         this.addVir(2, 6, 12, false);

         for(var2 = 0; var2 < 16; ++var2) {
            var1 %= 360;
            var3 = new Point(super.x + CRes.getcos(var1) * 100 / 1000, super.y + CRes.getsin(var1) * 65 / 1000);
            this.VecEff.addElement(var3);
            GameScreen.addEffectEnd((short)66, 0, var3.x, var3.y, super.Dir, super.objMainEff);
            var1 += 22;
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var4;
         ++(var4 = (Point)this.VecEff.elementAt(var1)).f;
         if (var4.f >= 8) {
            this.VecEff.removeElement(var4);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updatePan2() {
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         if (super.f < 6) {
            super.objFireMain.dy = super.f * 40;
         }

         if (super.f >= 6 && super.f <= 12) {
            super.objFireMain.dy = 480;
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 13) {
            super.objFireMain.isTanHinh = false;
            super.objFireMain.x = super.toX;
            super.objFireMain.y = super.toY;
         }

         if (super.f > 13 && super.f < 18) {
            super.objFireMain.dy = (17 - super.f) * 120;
         }

         if (super.f >= 18) {
            super.objFireMain.dy = 0;
         }

         if (super.f == 18) {
            this.addVir(2, 6, 10, false);
            GameScreen.addEffectEnd((short)65, 0, super.objFireMain.x, super.objFireMain.y + 22, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)59, 0, super.objFireMain.x + CRes.random_Am_0(10), super.objFireMain.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)59, 0, super.objFireMain.x + CRes.random_Am_0(10), super.objFireMain.y, super.Dir, super.objMainEff);
         }

      } else {
         this.removeEff();
      }
   }

   private void update_Pan1() {
      Point var1;
      if (super.f == 15) {
         var1 = new Point(super.toX, super.toY);
         this.VecEff.addElement(var1);
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

      if (super.f == 17) {
         (var1 = new Point(super.toX, super.toY)).frame = 2;
         this.VecEff.addElement(var1);
      }

      if (super.f == 19 || super.f == 21 || super.f > 25) {
         for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
            Point var2 = (Point)this.VecEff.elementAt(var3);
            if (super.f >= 25) {
               ++var2.f;
            } else if (var2.frame == 2 || var2.frame == 4) {
               var2.frame += 2;
            }
         }
      }

   }

   private void update_Zoro_S3_L3() {
      int var1;
      Point var2;
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         this.x1000 += super.vx;
         if (super.f == 6) {
            super.vx = 8;
            if (super.Dir == 0) {
               super.vx = -8;
            }

            super.objFireMain.vx = super.vx;
         }

         if (super.f == 10 && super.typeEffect == 217 && !GameCanvas.lowGraphic) {
            GameScreen.addEffectEnd((short)137, 0, super.objFireMain.x, super.objFireMain.y + 10, super.Dir, super.objMainEff);
         }

         if (super.f == 12) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 14) {
            super.vx = 0;
            super.objFireMain.vx = super.vx;
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 20) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               Object_Effect_Skill var5;
               MainObject var8;
               if ((var5 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var8 = MainObject.get_Object((int)var5.ID, (byte)var5.tem)) != null) {
                  GameScreen.addEffectEnd((short)108, 1, var8.x, var8.y - var8.hOne / 2, super.Dir, super.objMainEff);
                  GameScreen.addEffectEnd_ObjTo((short)24, 0, var8.x, var8.y, (short)var8.ID, (byte)var8.typeObject, (byte)0, (MainObject)null);
               }
            }
         }

         if (super.f >= 16 && super.f % 3 == 0 && this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var4 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var6;
            if (var4 != null && (var6 = MainObject.get_Object((int)var4.ID, (byte)var4.tem)) != null) {
               Point var9;
               (var9 = new Point(var6.x, var6.y - var6.hOne / 2)).AZ = var6;
               this.VecEff.addElement(var9);
            }
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
            var2.x = var2.AZ.x;
            var2.y = var2.AZ.y - var2.AZ.hOne / 2;
         }

         if (super.f == 24) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = this.CJ;
            super.objFireMain.y = this.CK;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            this.x1000 = super.x - 15;
            this.y1000 = super.objFireMain.y - 22;
            byte var7 = -15;
            if (super.Dir == 2) {
               var7 = 15;
               this.x1000 = super.x - 63;
            }

            super.x += var7;
            super.y -= 5;
         }

         if (super.f == 26) {
            super.objFireMain.isTanHinh = false;
         }

      } else {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(10, 5, 10, true);

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            var2 = (Point)this.VecEff.elementAt(var1);
            byte var3 = 0;
            if (super.typeEffect == 185) {
               var3 = 1;
            }

            GameScreen.addEffectEnd((short)64, var3, var2.x, var2.y, super.Dir, super.objMainEff);
            this.setAva(1, var2.AZ);
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S3_L6() {
      int var1;
      Point_Focus var2;
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         this.x1000 += super.vx;
         if (super.f == 6) {
            super.vx = 8;
            if (super.Dir == 0) {
               super.vx = -8;
            }

            super.objFireMain.vx = super.vx;
         }

         if (super.f == 10 && !GameCanvas.lowGraphic) {
            GameScreen.addEffectEnd((short)137, 1, super.objFireMain.x, super.objFireMain.y + 10, super.Dir, super.objMainEff);
         }

         if (super.f == 12) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 14) {
            super.vx = 0;
            super.objFireMain.vx = super.vx;
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 20) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               MainObject var3;
               Object_Effect_Skill var7;
               if ((var7 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var7.ID, (byte)var7.tem)) != null) {
                  GameScreen.addEffectEnd((short)108, 1, var3.x, var3.y - var3.hOne / 2, super.Dir, super.objMainEff);
               }
            }
         }

         if (super.f >= 10 && super.f % 3 == 0 && this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var8;
            if (var6 != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var9 = new Point_Focus();
               var1 = super.y;
               if (!this.checkNullObject((int)1)) {
                  var1 = super.objFireMain.y;
               }

               int var4 = var8.x - super.x;
               int var5 = var8.y - var1;
               (var9 = this.create_Speed(var4, var5, var9, super.x, var1, var8.x, var8.y)).AR = var8;
               this.VecEff.addElement(var9);
            }
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe) {
               this.setAva(1, var2.AR);
               var2.AI = 0;
               var2.vy = 0;
               var2.x = var2.AR.x;
               var2.y = var2.AR.y;
            }

            if (var2.AG > var2.fRe) {
               var2.AR.dy = CRes.random(20, 30);
               if (var2.AG < var2.fRe + 4) {
                  this.setAva(-1, var2.AR);
               }
            }
         }

         if (super.f == 24) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = this.CJ;
            super.objFireMain.y = this.CK;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            this.x1000 = super.x - 15;
            this.y1000 = super.objFireMain.y - 22;
            byte var10 = -15;
            if (super.Dir == 2) {
               var10 = 15;
               this.x1000 = super.x - 63;
            }

            super.x += var10;
            super.y -= 5;
         }

         if (super.f == 26) {
            super.objFireMain.isTanHinh = false;
         }

      } else {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(10, 5, 10, true);

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            var2 = (Point_Focus)this.VecEff.elementAt(var1);
            GameScreen.addEffectEnd((short)64, 2, var2.x, var2.y - var2.AR.hOne / 2, super.Dir, super.objMainEff);
            this.setAva(1, var2.AR);
         }

         this.removeEff();
      }
   }

   private void update_Zoro_S3_L7() {
      int var1;
      Point_Focus var2;
      if (super.f < super.fRemove && !this.checkNullObject((int)1)) {
         this.x1000 += super.vx;
         if (super.f == 6) {
            super.vx = 8;
            if (super.Dir == 0) {
               super.vx = -8;
            }

            super.objFireMain.vx = super.vx;
         }

         if (super.f == 10 && !GameCanvas.lowGraphic) {
            GameScreen.addEffectEnd((short)137, 2, super.objFireMain.x, super.objFireMain.y + 10, super.Dir, super.objMainEff);
         }

         if (super.f == 12) {
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 14) {
            super.vx = 0;
            super.objFireMain.vx = super.vx;
            super.objFireMain.isTanHinh = true;
         }

         if (super.f == 20) {
            for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
               MainObject var3;
               Object_Effect_Skill var7;
               if ((var7 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var7.ID, (byte)var7.tem)) != null) {
                  GameScreen.addEffectEnd((short)108, 1, var3.x, var3.y - var3.hOne / 2, super.Dir, super.objMainEff);
               }
            }
         }

         if (super.f >= 10 && super.f % 3 == 0 && this.CI < super.vecObjsBeFire.size()) {
            Object_Effect_Skill var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
            ++this.CI;
            MainObject var8;
            if (var6 != null && (var8 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
               Point_Focus var9 = new Point_Focus();
               var1 = super.y;
               if (!this.checkNullObject((int)1)) {
                  var1 = super.objFireMain.y;
               }

               int var4 = var8.x - super.x;
               int var5 = var8.y - var1;
               (var9 = this.create_Speed(var4, var5, var9, super.x, var1, var8.x, var8.y)).AR = var8;
               this.VecEff.addElement(var9);
            }
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
            if (var2.AG == var2.fRe) {
               this.setAva(1, var2.AR);
               var2.AI = 0;
               var2.vy = 0;
               var2.x = var2.AR.x;
               var2.y = var2.AR.y;
            }

            if (var2.AG > var2.fRe) {
               var2.AR.dy = CRes.random(20, 30);
               if (var2.AG < var2.fRe + 4) {
                  this.setAva(-1, var2.AR);
               }
            }
         }

         if (super.f == 24) {
            this.changeDir();
            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = this.CJ;
            super.objFireMain.y = this.CK;
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
            this.x1000 = super.x - 15;
            this.y1000 = super.objFireMain.y - 22;
            byte var10 = -15;
            if (super.Dir == 2) {
               var10 = 15;
               this.x1000 = super.x - 63;
            }

            super.x += var10;
            super.y -= 5;
         }

         if (super.f == 26) {
            super.objFireMain.isTanHinh = false;
         }

      } else {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         this.addVir(10, 5, 10, true);

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            var2 = (Point_Focus)this.VecEff.elementAt(var1);
            GameScreen.addEffectEnd((short)64, 3, var2.x, var2.y - var2.AR.hOne / 2, super.Dir, super.objMainEff);
            this.setAva(1, var2.AR);
         }

         this.removeEff();
      }
   }

   private void changeDir() {
      super.Dir = (byte)(super.Dir == 2 ? 0 : 2);
   }

   private void addVir(int var1, int var2, int var3, boolean var4) {
      if ((!var4 || !this.checkNullObject((int)1) && super.objFireMain == GameScreen.player) && CRes.random(var1) == 0) {
         LoadMap.timeVibrateScreen = CRes.random(var2, var3);
      }

   }

   private void update_Zoro_S3_L2() {
      if ((super.f == 13 || super.f == 20) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      if (super.f == 15) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            Object_Effect_Skill var2;
            MainObject var3;
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd_ObjTo((short)24, 0, var3.x, var3.y, (short)var3.ID, (byte)var3.typeObject, (byte)0, (MainObject)null);
            }
         }
      }

      if (super.f > 20 && super.f % 3 == 0 && this.CI < super.vecObjsBeFire.size()) {
         Object_Effect_Skill var6 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(this.CI);
         ++this.CI;
         MainObject var7;
         if (var6 != null && (var7 = MainObject.get_Object((int)var6.ID, (byte)var6.tem)) != null) {
            Point_Focus var9 = new Point_Focus();
            var1 = super.y;
            if (!this.checkNullObject((int)1)) {
               var1 = super.objFireMain.y;
            }

            int var4 = var7.x - super.x;
            int var5 = var7.y - var1;
            (var9 = this.create_Speed(var4, var5, var9, super.x, var1, var7.x, var7.y)).AR = var7;
            this.VecEff.addElement(var9);
         }
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point_Focus var8;
         (var8 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var8.AG == var8.fRe) {
            this.setAva(1, var8.AR);
            var8.AI = 0;
            var8.vy = 0;
            var8.x = var8.AR.x;
            var8.y = var8.AR.y;
         }

         if (var8.AG > var8.fRe) {
            var8.AR.dy = CRes.random(20, 30);
            if (var8.AG < var8.fRe + 4) {
               this.setAva(-1, var8.AR);
            }

            if (var8.AG >= var8.fRe + 8) {
               this.VecEff.removeElement(var8);
               --var1;
            }
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void update_Zoro_S3_L1() {
      if ((super.f == 13 || super.f == 20) && this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

      int var1;
      Object_Effect_Skill var2;
      MainObject var3;
      if (super.f == 15) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd_ObjTo((short)24, 0, var3.x, var3.y, (short)var3.ID, (byte)var3.typeObject, (byte)0, (MainObject)null);
            }
         }
      }

      if (super.f == 23) {
         for(var1 = 0; var1 < super.vecObjsBeFire.size(); ++var1) {
            if ((var2 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var1)) != null && (var3 = MainObject.get_Object((int)var2.ID, (byte)var2.tem)) != null) {
               GameScreen.addEffectEnd((short)11, 0, var3.x, var3.y - var3.hOne / 2, super.Dir, super.objMainEff);
               this.setAva(0, var3);
            }
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateLuffyMon16_17() {
      if (super.f == 1 || super.f == 5 || super.f == 10) {
         byte var1 = 20;
         if (super.Dir == 0) {
            var1 = -20;
         }

         this.setAva(0, this.objBeFireMain);
         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)35, 0, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
         }

         if (!this.checkNullObject((int)1)) {
            GameScreen.addEffectEnd((short)72, super.f == 5 ? 2 : 1, super.x + var1, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateLuffySea1() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)93, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)8, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 3 || super.f == 11) {
         if (this.isAddSound) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            for(var1 = 0; var1 < 2; ++var1) {
               var2 = new Point_Focus(super.x, super.y);
               int var3 = this.objBeFireMain.x - super.x;
               int var4 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
               (var2 = this.create_Speed(var3, var4, var2)).frame = CRes.random(3);
               var2.dis = super.Dir;
               this.VecEff.addElement(var2);
            }
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateLuffySea2() {
      int var1;
      Point_Focus var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point_Focus)this.VecEff.elementAt(var1)).update_Vx_Vy();
         if (var2.AG >= var2.fRe) {
            GameScreen.addEffectEnd((short)8, 0, var2.x, var2.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 4, var2.x, var2.y, super.Dir, super.objMainEff);
            this.VecEff.removeElement(var2);
            --var1;
         } else if (super.typeEffect == 135) {
            Point var3 = new Point(var2.x, var2.y);
            this.VecSubEff.addElement(var3);
         }
      }

      for(var1 = 0; var1 < this.VecSubEff.size(); ++var1) {
         Point var5;
         ++(var5 = (Point)this.VecSubEff.elementAt(var1)).f;
         if (var5.f >= 4) {
            this.VecSubEff.removeElement(var5);
            --var1;
         }
      }

      if (super.f >= 10 && super.f <= 13 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = super.f - 9 << 3;
      }

      if (super.f >= 14 && super.f <= 16 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 32;
      }

      if (super.f >= 17 && super.f <= 20 && !this.checkNullObject((int)1)) {
         super.objFireMain.dy = 20 - super.f << 3;
      }

      int var7;
      float var9;
      if (super.f == 3 || super.f == 6) {
         if (this.isAddSound) {
            var9 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            for(var1 = 0; var1 < 2; ++var1) {
               var2 = new Point_Focus(super.x, super.y);
               var7 = this.objBeFireMain.x - super.x;
               int var4 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - super.y;
               (var2 = this.create_Speed(var7, var4, var2)).frame = CRes.random(3);
               var2.dis = super.Dir;
               if (super.typeEffect == 135) {
                  var2.AM = 1;
               }

               this.VecEff.addElement(var2);
            }
         }
      }

      if (super.f == 12 && this.isAddSound) {
         var9 = mSound.volumeSound;
         mSound.playSound();
      }

      if (super.f == 15 && !this.checkNullObject((int)3)) {
         Point_Focus var8 = new Point_Focus(super.x, super.y);
         int var6 = this.objBeFireMain.x - super.x;
         var7 = this.objBeFireMain.y - this.objBeFireMain.hOne / 2 - (super.y - super.objFireMain.dy);
         (var8 = this.create_Speed(var6, var7, var8, super.x, super.y - super.objFireMain.dy, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2)).AM = 1;
         var8.frame = CRes.random(4);
         var8.dis = super.Dir;
         this.VecEff.addElement(var8);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.dy = 0;
         }

         this.removeEff();
      }

   }

   private void updateSanjiSea1() {
      if (super.f <= 4 || super.f >= 11 && super.f <= 15) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = true;
         }
      } else if (!this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = false;
      }

      if (super.f == 2 && !this.checkNullObject((int)3)) {
         int var1 = this.objBeFireMain.x - 20;
         if (super.Dir == 0) {
            var1 = this.objBeFireMain.x + 20;
         }

         super.objFireMain.x = var1;
         super.objFireMain.y = this.objBeFireMain.y;
      }

      if (super.f == 12 && !this.checkNullObject((int)1)) {
         super.objFireMain.x = super.x;
         super.objFireMain.y = super.y;
      }

      if (this.objBeFireMain != null && this.objBeFireMain.hOne > 0 && (super.f == 6 || super.f == 9)) {
         if (this.isAddSound && super.f == 6) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)2)) {
            GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 2, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
         }

         this.removeEff();
      }

   }

   private void updateSanjiSea2() {
      if (super.f > 4 && (super.f < 8 || super.f > 13) && super.f != 19) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }
      } else if (!this.checkNullObject((int)1)) {
         super.objFireMain.isTanHinh = true;
      }

      int var1;
      if (super.f == 2 && !this.checkNullObject((int)3)) {
         var1 = this.objBeFireMain.x - 20;
         if (super.Dir == 0) {
            var1 = this.objBeFireMain.x + 20;
         }

         super.objFireMain.x = var1;
         super.objFireMain.y = this.objBeFireMain.y;
         super.objFireMain.Dir = super.Dir;
      }

      if (super.f == 12 && !this.checkNullObject((int)3)) {
         var1 = this.objBeFireMain.x + 20;
         if (super.Dir == 0) {
            var1 = this.objBeFireMain.x - 20;
         }

         super.objFireMain.x = var1;
         super.objFireMain.y = this.objBeFireMain.y;
         super.objFireMain.Dir = super.Dir == 0 ? 2 : 0;
      }

      if (super.f == 19 && !this.checkNullObject((int)1)) {
         super.objFireMain.x = super.x;
         super.objFireMain.y = super.y;
         super.objFireMain.Dir = super.Dir;
      }

      if (this.objBeFireMain != null && this.objBeFireMain.hOne > 0 && (super.f == 4 || super.f == 6 || super.f == 14 || super.f == 16)) {
         if (this.isAddSound && (super.f == 4 || super.f == 14)) {
            float var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         if (!this.checkNullObject((int)3)) {
            if (super.objFireMain.hOne > 0) {
               byte var2 = 25;
               if (super.objFireMain.Dir == 0) {
                  var2 = -25;
               }

               GameScreen.addEffectEnd((short)36, 0, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            }

            if (super.typeEffect == 137) {
               GameScreen.addEffectEnd((short)1, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3), super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)4, 0, this.objBeFireMain.x + CRes.random_Am_0(15), this.objBeFireMain.y - CRes.random(0, this.objBeFireMain.hOne / 4 * 3) - 10, super.Dir, super.objMainEff);
               this.addVir(5, 5, 10, true);
            }
         }
      }

      if (super.f >= super.fRemove) {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
            super.objFireMain.Dir = super.Dir;
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }

   }

   private void updateMonster_DanhTron() {
      if (super.f == 1 && !this.checkNullObject((int)1)) {
         for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var2;
            if ((var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).typeObject == 0 && MainObject.getDistance(var2.x, var2.y, super.objFireMain.x, super.objFireMain.y) <= 60) {
               this.setAva(-1, var2);
               GameScreen.addEffectEnd((short)3, 0, var2.x, var2.y - var2.hOne / 2, super.Dir, super.objMainEff);
            }
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateUssop_S2_L3_New() {
      if (super.f >= super.fRemove) {
         this.removeEff();
      } else {
         if (super.f == 15) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
            super.y -= 6;
            if (super.Dir == 0) {
               super.x -= 30;
            } else {
               super.x += 30;
            }

            if (super.toX > super.x) {
               super.vx = 12;
            } else {
               super.vx = -12;
            }

            if (super.toY > super.y) {
               super.vy = 2;
            } else {
               super.vy = -2;
            }

            this.setAngle();
            GameScreen.addEffectEnd((short)57, 0, super.x, super.y, super.Dir, super.objMainEff);
            this.addVir(5, 5, 10, true);
         }

         if (super.f > 15 && super.f < super.fRemove && super.typeEffect == 225 && !GameCanvas.lowGraphic) {
            GameScreen.addEffectEnd((short)140, 0, super.x, super.y + 40, super.Dir, super.objMainEff);
         }

         if (super.typeEffect != 225 && super.f == super.fRemove - 10 || super.typeEffect == 225 && super.f == super.fRemove - 16) {
            this.setAva(2, this.objBeFireMain);
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)4, 2, this.objBeFireMain.x + CRes.random_Am_0(12), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(12), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }
         }

      }
   }

   private void updateUssop_S2_L6() {
      if (super.f >= super.fRemove) {
         super.objFireMain.isTanHinh = false;
         this.removeEff();
      } else {
         if (super.f == 15) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
            super.y -= 6;
            if (super.Dir == 0) {
               super.x -= 30;
            } else {
               super.x += 30;
            }

            if (super.toX > super.x) {
               super.vx = 12;
            } else {
               super.vx = -12;
            }

            if (super.toY > super.y) {
               super.vy = 2;
            } else {
               super.vy = -2;
            }

            this.setAngle();
            GameScreen.addEffectEnd((short)168, 2, super.x, super.y, super.Dir, super.objMainEff);
            this.addVir(5, 5, 10, true);
         }

         if (super.f > 15 && super.f < super.fRemove && !GameCanvas.lowGraphic) {
            GameScreen.addEffectEnd((short)167, 0, super.x, super.y + 40, super.Dir, super.objMainEff);
         }

         if (super.f == super.fRemove - 10 || super.f == super.fRemove - 16) {
            this.setAva(2, this.objBeFireMain);
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)4, 2, this.objBeFireMain.x + CRes.random_Am_0(12), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(12), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         if (super.f > 2 && super.f < 6) {
            ((Point_Focus)this.VecEff.elementAt(0)).AA();
         }

         if (super.f > 2 && super.f < 15) {
            super.objFireMain.isTanHinh = true;
         } else {
            super.objFireMain.isTanHinh = false;
         }
      }
   }

   private void update_Ussop_S2_L7() {
      if (super.f >= super.fRemove) {
         super.objFireMain.isTanHinh = false;
         this.removeEff();
      } else {
         byte var1;
         if (super.f == 15) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            super.toX = this.objBeFireMain.x;
            super.toY = this.objBeFireMain.y - this.objBeFireMain.hOne / 2;
            super.y -= 6;
            if (super.Dir == 0) {
               super.x -= 30;
            } else {
               super.x += 30;
            }

            if (super.toX > super.x) {
               super.vx = 12;
            } else {
               super.vx = -12;
            }

            if (super.toY > super.y) {
               super.vy = 2;
            } else {
               super.vy = -2;
            }

            this.setAngle();
            var1 = 40;
            if (super.Dir == 2) {
               var1 = -40;
            }

            GameScreen.addEffectEnd((short)168, 2, super.x, super.y, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)168, 2, super.x + var1, super.y - 20, super.Dir, super.objMainEff);
            this.addVir(5, 5, 10, true);
         }

         if (super.f > 15 && super.f < super.fRemove && !GameCanvas.lowGraphic) {
            var1 = 40;
            if (super.Dir == 2) {
               var1 = -40;
            }

            GameScreen.addEffectEnd((short)167, 0, super.x, super.y + 40, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)167, 0, super.x + var1, super.y - 20 + 40, super.Dir, super.objMainEff);
         }

         if (super.f == super.fRemove - 10 || super.f == super.fRemove - 16) {
            this.setAva(2, this.objBeFireMain);
            if (!this.checkNullObject((int)2)) {
               GameScreen.addEffectEnd((short)4, 2, this.objBeFireMain.x + CRes.random_Am_0(12), this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(12), super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x, this.objBeFireMain.y - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         if (super.f > 2 && super.f < 6) {
            ((Point_Focus)this.VecEff.elementAt(0)).AA();
         }

         if (super.f > 2 && super.f < 15) {
            super.objFireMain.isTanHinh = true;
         } else {
            super.objFireMain.isTanHinh = false;
         }
      }
   }

   private void updateMonster_Chay_Thang() {
      if (super.f > 12 && super.f % 12 == 0) {
         Point var1;
         (var1 = new Point()).vx = super.am_duong * 15;
         if (!this.checkNullObject((int)1)) {
            var1.y = super.objFireMain.y;
            var1.x = super.objFireMain.x + var1.vx;
         } else {
            var1.y = super.y;
            var1.x = super.x + var1.vx;
         }

         this.VecEff.addElement(var1);
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var3)).update();
         if (var2.f > 6) {
            ++var2.frame;
         }

         if (var2.frame >= 3) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

      if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
         this.removeEff();
      }

   }

   private void updateSanji_S2_L3_New() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if ((super.f % 10 > 9 || super.f % 10 <= 1) && super.f > 5 && super.f < 35) {
            super.objFireMain.isTanHinh = true;
         } else {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 10 || super.f == 20 || super.f == 30) {
            if (super.f > 10) {
               this.changeDir();
               super.am_duong = -1;
               if (super.Dir == 2) {
                  super.am_duong = 1;
               }

               super.objFireMain.Dir = super.Dir;
            }

            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            super.objFireMain.y = this.objBeFireMain.y;
         }

         if (super.f < 40 && super.f >= 10 && (super.f % 10 == 2 || super.f % 10 == 7)) {
            if (this.isAddSound && super.f % 10 == 2) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            GameScreen.addEffectEnd((short)36, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)25, 0, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            this.setAva(0, this.objBeFireMain);
         }

         if (super.f == 42) {
            super.objFireMain.isTanHinh = true;
            this.changeDir();
            super.am_duong = -1;
            if (super.Dir == 2) {
               super.am_duong = 1;
            }

            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateSanji_S2_L3_New_SHORT() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if ((super.f % 10 > 9 || super.f % 10 <= 1) && super.f > 5 && super.f < 25) {
            super.objFireMain.isTanHinh = true;
         } else {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 1 || super.f == 10 || super.f == 20) {
            this.changeDir();
            super.am_duong = -1;
            if (super.Dir == 2) {
               super.am_duong = 1;
            }

            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            super.objFireMain.y = this.objBeFireMain.y;
         }

         if (super.f < 24 && (super.f % 10 == 2 || super.f % 10 == 7)) {
            if (this.isAddSound && super.f % 10 == 2) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.f % 10 == 2 || super.typeEffect == 187) {
               GameScreen.addEffectEnd((short)108, 7, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            }

            this.addVir(5, 5, 10, true);
            GameScreen.addEffectEnd((short)36, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            byte var1 = 0;
            if (super.typeEffect == 187) {
               var1 = 4;
            }

            GameScreen.addEffectEnd((short)25, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            if (super.typeEffect == 187) {
               GameScreen.addEffectEnd((short)119, 2, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, (byte)super.objFireMain.Dir, super.objMainEff);
            }

            this.setAva(0, this.objBeFireMain);
         }

         if (super.f == 22) {
            super.objFireMain.isTanHinh = true;
            this.changeDir();
            super.am_duong = -1;
            if (super.Dir == 2) {
               super.am_duong = 1;
            }

            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = super.x;
            super.objFireMain.y = super.y;
         }

      } else {
         if (!this.checkNullObject((int)1)) {
            super.objFireMain.isTanHinh = false;
         }

         this.removeEff();
      }
   }

   private void updateSanji_S1_L3_New() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if (super.f == 10) {
            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            super.objFireMain.y = this.objBeFireMain.y;
         }

         float var10000;
         if (super.f == 12 || super.f == 17) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.typeEffect == 177) {
               GameScreen.addEffectEnd((short)19, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 1, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)36, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            }

            GameScreen.addEffectEnd((short)25, 0, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
         }

         if (super.f == 20) {
            super.vY1000 = 35;
            super.objFireMain.isTanHinh = true;
         }

         if (super.f >= 20 && super.f <= 27) {
            this.objBeFireMain.dy = super.BA;
            super.BA += super.vY1000;
            if (super.vY1000 > 0) {
               super.vY1000 -= 5;
            }

            this.setAva(-1, this.objBeFireMain);
         }

         if (super.f == 25) {
            super.objFireMain.isTanHinh = false;
         }

         if (super.f == 23) {
            super.objFireMain.dy = 105;
            this.changeDir();
            super.am_duong = -1;
            if (super.Dir == 2) {
               super.am_duong = 1;
            }

            super.objFireMain.Dir = super.Dir;
            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            GameScreen.addEffectEnd((short)30, 0, super.objFireMain.x - super.am_duong * 5, super.objFireMain.y - super.objFireMain.hOne / 2 - super.objFireMain.dy, 400, super.Dir, super.objMainEff);
         }

         if (super.f >= 23 && super.f <= 40) {
            super.objFireMain.dy = 105;
            if (super.f >= 27) {
               this.objBeFireMain.dy = 105;
            }

            this.setAva(-1, this.objBeFireMain);
         }

         if (super.f == 40) {
            if (this.isAddSound) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.addVir(5, 5, 10, true);
            if (super.typeEffect == 177) {
               GameScreen.addEffectEnd((short)19, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)108, 1, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            } else {
               GameScreen.addEffectEnd((short)36, 0, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               GameScreen.addEffectEnd((short)35, 0, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.dy - this.objBeFireMain.hOne / 2, super.Dir, super.objMainEff);
            }

            super.vY1000 = 10;
            super.BA = 120;
         }

         if (super.f >= 41 && super.f <= 46) {
            super.BA -= super.vY1000;
            this.objBeFireMain.dy = super.BA;
            super.vY1000 += 5;
            if (this.objBeFireMain.dy < 0) {
               this.objBeFireMain.dy = 0;
            }

            this.objBeFireMain.vx = super.am_duong * 15;
            super.objFireMain.AU();
         }

         if (super.f > 47) {
            this.objBeFireMain.vx = 0;
         }

      } else {
         if (!this.checkNullObject((int)3)) {
            super.objFireMain.isTanHinh = false;
            this.objBeFireMain.vx = 0;
         }

         this.removeEff();
      }
   }

   private void updateSanji_S1_L3_SHORT() {
      if (super.f < super.fRemove && !this.checkNullObject((int)3)) {
         if (super.f == 1) {
            super.objFireMain.x = this.objBeFireMain.x - super.am_duong * 30;
            super.objFireMain.y = this.objBeFireMain.y;
         }

         if (super.f == 7 || super.f == 10 || super.f == 13) {
            if (this.isAddSound) {
               float var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            this.setAva(0, this.objBeFireMain);
            byte var1 = 1;
            if (super.typeEffect != 218 || super.f == 10) {
               var1 = 0;
            }

            GameScreen.addEffectEnd((short)36, var1, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
            var1 = 0;
            if (super.typeEffect == 186) {
               var1 = 4;
            }

            GameScreen.addEffectEnd((short)25, var1, this.objBeFireMain.x - super.am_duong * 5, this.objBeFireMain.y - this.objBeFireMain.hOne / 2 + CRes.random_Am_0(10), super.Dir, super.objMainEff);
            if (super.f == 10) {
               GameScreen.addEffectEnd((short)108, 7, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
               if (super.typeEffect == 186) {
                  GameScreen.addEffectEnd((short)119, 1, super.objFireMain.x + super.am_duong * 25, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
               }

               if (super.typeEffect == 218) {
                  GameScreen.addEffectEnd((short)119, 4, super.objFireMain.x + super.am_duong * 20, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, super.objMainEff);
               }
            }
         }

      } else {
         if (!this.checkNullObject((int)3)) {
            super.objFireMain.isTanHinh = false;
            this.objBeFireMain.vx = 0;
         }

         this.removeEff();
      }
   }

   private void addSound(byte var1) {
      if (this.isAddSound) {
         float var10000 = mSound.volumeSound;
         mSound.playSound();
      }

   }

   private void addSoundBuff() {
      if (super.objFireMain.clazz == 1) {
         this.addSound((byte)6);
      } else if (super.objFireMain.clazz == 2) {
         this.addSound((byte)8);
      } else if (super.objFireMain.clazz == 3) {
         this.addSound((byte)16);
      } else if (super.objFireMain.clazz == 4) {
         this.addSound((byte)22);
      } else {
         if (super.objFireMain.clazz == 5) {
            this.addSound((byte)34);
         }

      }
   }

   private void addSoundBuffShort() {
      if (super.objFireMain.clazz == 1) {
         this.addSound((byte)44);
      } else if (super.objFireMain.clazz == 2) {
         this.addSound((byte)45);
      } else if (super.objFireMain.clazz == 3) {
         this.addSound((byte)46);
      } else if (super.objFireMain.clazz == 4) {
         this.addSound((byte)22);
      } else {
         if (super.objFireMain.clazz == 5) {
            this.addSound((byte)34);
         }

      }
   }

   private void AM(mGraphics var1) {
      if (super.f >= 15 && super.f <= 20) {
         super.fraImgEff.drawFrame((super.f - 11) / 3, super.objFireMain.x + this.x1000, super.objFireMain.y + this.y1000, super.Dir, 3, var1);
      } else {
         if (super.f >= 25 && super.f <= 30) {
            super.fraImgEff.drawFrame((super.f - 25) / 3, super.objFireMain.x + this.x1000, super.objFireMain.y + this.y1000 + 10, super.Dir, 3, var1);
         }

      }
   }

   private void AN(mGraphics var1) {
      if (super.f > 10 && super.f < 18) {
         int var2 = super.x + this.CL[1][0];
         int var3 = super.y + this.CL[1][1];
         byte var4 = 1;
         if (super.f < 13) {
            var2 = super.x + this.CL[0][0];
            var3 = super.y + this.CL[0][1];
            var4 = 0;
         }

         super.fraImgEff.drawFrame(var4, var2, var3, super.Dir, 3, var1);
      }

   }

   private void AO(mGraphics var1) {
      int var2;
      if (super.f < super.fRemove) {
         var2 = this.CJ;
         int var3 = super.f / 2;
         if (super.f > 16) {
            var3 = 22 - super.f;
            if (super.f > 20) {
               var2 = this.CJ + (super.AZ << 1);
            } else if (super.f > 18) {
               var2 = this.CJ + super.AZ;
            }
         } else if (super.f >= 4) {
            var3 = 2;
         }

         if (var3 == 2) {
            var3 = 3;
         }

         if (super.f >= 10 && super.f <= 12) {
            var2 = this.CJ + super.AZ;
         }

         super.fraImgEff.drawFrame(var3, var2, super.y, super.Dir, 3, var1);
      }

      Point_Focus var4;
      for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
         var4 = (Point_Focus)this.VecEff.elementAt(var2);
         super.fraImgSubEff.drawFrame(0, var4.x, var4.y, super.Dir, 3, var1);
      }

      for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         var4 = (Point_Focus)this.VecSubEff.elementAt(var2);
         super.fraImgSub2Eff.drawFrame(var4.AG % super.fraImgSub2Eff.nFrame, var4.x, var4.y, super.Dir, 3, var1);
      }

   }

   private void AP(mGraphics var1) {
      int var2;
      if (super.f < super.fRemove) {
         var2 = super.x;
         int var3 = super.f / 2;
         if (super.f > 16) {
            var3 = 22 - super.f;
            if (super.f > 20) {
               var2 = super.x + (super.AZ << 1);
            } else if (super.f > 18) {
               var2 = super.x + super.AZ;
            }
         } else if (super.f >= 4) {
            var3 = 2;
         }

         if (super.f >= 10 && super.f <= 12) {
            var2 = super.x + super.AZ;
         }

         super.fraImgEff.drawFrame(var3, var2, super.y, super.Dir, 3, var1);
      }

      for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point_Focus var4 = (Point_Focus)this.VecEff.elementAt(var2);
         this.AA(var1, super.fraImgSubEff, var4.frame, var4.x, var4.y);
      }

   }

   private void AQ(mGraphics var1) {
      byte var2 = 0;
      int var3 = 5;
      if (super.f == 28) {
         var2 = -6;
      } else if (super.f > 28) {
         var2 = 0;
         var3 = 10;
      } else {
         var3 = 0;
      }

      int var4;
      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point_Focus var5 = (Point_Focus)this.VecEff.elementAt(var4);
         if (super.Dir == 2) {
            var1.setColor(-820712);
            var1.fillRect(this.x1000 - var3, this.y1000 - 7, CRes.abs(var5.x - this.x1000) + var3, 14);
            var1.setColor(-791797);
            var1.fillRect(this.x1000 - var3, this.y1000 - 6, CRes.abs(var5.x - this.x1000) + var3, 12);
            var1.setColor(-131587);
            var1.fillRect(this.x1000 - var3, this.y1000 - 4, CRes.abs(var5.x - this.x1000) + var3, 8);
         } else {
            var1.setColor(-820712);
            var1.fillRect(this.x1000 - CRes.abs(var5.x - this.x1000), this.y1000 - 7, CRes.abs(var5.x - this.x1000) + var3, 14);
            var1.setColor(-791797);
            var1.fillRect(this.x1000 - CRes.abs(var5.x - this.x1000), this.y1000 - 6, CRes.abs(var5.x - this.x1000) + var3, 12);
            var1.setColor(-131587);
            var1.fillRect(this.x1000 - CRes.abs(var5.x - this.x1000), this.y1000 - 4, CRes.abs(var5.x - this.x1000) + var3, 8);
         }

         super.fraImgSub3Eff.drawFrame(0, var5.x, var5.y, super.Dir, 3, var1);
      }

      if (super.f > 8 && super.f < 42) {
         if (super.Dir == 2) {
            var3 = -var3;
         }

         byte var6 = 0;
         if (super.f < 16) {
            var6 = 2;
         } else if (super.f == 16 || super.f == 17) {
            var6 = 1;
         }

         super.fraImgSubEff.drawFrame(var6, super.x + var3, super.y + 38 + var2, super.Dir, 33, var1);
      }

      if (super.f >= 18 && super.f <= 20) {
         super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSub2Eff.nFrame, this.x1000, this.y1000, super.Dir, 3, var1);
      }

      if (super.f < 12) {
         var4 = 1 + super.f / 2 % 2;
         if (super.f < 2 || super.f > 9) {
            var4 = 0;
         }

         super.fraImgEff.drawFrame(var4, super.x, super.y, super.Dir, 17, var1);
      }

      if (super.f > 40) {
         var4 = 1 + super.f / 2 % 2;
         if (super.f > 46) {
            var4 = 0;
         }

         super.fraImgEff.drawFrame(var4, super.x, super.y, super.Dir, 17, var1);
      }

   }

   private void AR(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.VecSubEff.elementAt(var2)).f < 3) {
            super.fraImgSub2Eff.drawFrame(2 - var3.f, var3.x, var3.y, super.Dir, 33, var1);
         } else {
            super.objFireMain.AA(var1, var3.x, var3.y, super.objFireMain.frame, super.objFireMain.Dir, true);
         }

         byte var4 = -20;
         if (super.Dir == 2) {
            var4 = 20;
         }

         if (super.f > 20) {
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, var3.x + var4, var3.y - super.objFireMain.hOne / 2, super.Dir, 3, var1);
         }
      }

      if (super.f > 20) {
         super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
      }

   }

   private void AS(mGraphics var1) {
      (new StringBuffer("vestsub size   = ")).append(this.VecSubEff.size()).toString();

      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.VecSubEff.elementAt(var2)).f < 3) {
            super.fraImgSub2Eff.drawFrame(2 - var3.f, var3.x, var3.y, var3.AM, 33, var1);
         } else {
            super.objFireMain.AA(var1, var3.x, var3.y, super.objFireMain.frame, var3.AM, true);
         }

         byte var4 = -20;
         if (super.Dir == 2) {
            var4 = 20;
         }

         if (super.f > 20) {
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, var3.x + var4, var3.y - super.objFireMain.hOne / 2, var3.AM, 3, var1);
         }
      }

      if (super.f > 20) {
         super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
      }

   }

   private void AT(mGraphics var1) {
      if (super.objFireMain != null) {
         if (super.f == 1) {
            super.fraImgSubEff.drawFrame(0, super.x, super.y + super.objFireMain.hOne / 2, super.Dir, 33, var1);
         }

         if (super.f >= 9 && super.f <= 11 || super.f > 25 && super.f < 33) {
            byte var2 = 16;
            if (super.Dir == 0) {
               var2 = -16;
            }

            super.fraImgEff.drawFrame(2, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.hOne / 2 + 2, super.Dir, 3, var1);
         }
      }

      if (super.f >= 12 && super.f <= 15) {
         super.fraImgSubEff.drawFrame((super.f - 12) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      if (super.f >= 17 && super.f <= 20) {
         super.fraImgSub2Eff.drawFrame((super.f - 17) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      for(int var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point var3 = (Point)this.VecEff.elementAt(var4);
         super.fraImgSubEff.drawFrame(var3.f / 2, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void AU(mGraphics var1) {
      if (super.objFireMain != null && (super.f >= 2 && super.f <= 3 || super.f > 16 && super.f < 22)) {
         byte var2 = 16;
         if (super.Dir == 0) {
            var2 = -16;
         }

         super.fraImgEff.drawFrame(2, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.hOne / 2 + 2 - super.objFireMain.dy, super.Dir, 3, var1);
      }

      if (super.f >= 3 && super.f <= 6) {
         super.fraImgSubEff.drawFrame((super.f - 12) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      if (super.f >= 8 && super.f <= 11) {
         super.fraImgSub2Eff.drawFrame((super.f - 17) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      Point var3;
      int var4;
      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         var3 = (Point)this.VecEff.elementAt(var4);
         super.fraImgSubEff.drawFrame(var3.f / 2, var3.x, var3.y, super.Dir, 33, var1);
      }

      if (super.typeEffect == 213 || super.typeEffect == 272) {
         for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
            var3 = (Point)this.VecSubEff.elementAt(var4);
            super.fraImgSub3Eff.drawFrame(var3.frame, var3.x, var3.y, super.Dir, 33, var1);
         }
      }

   }

   private void AV(mGraphics var1) {
      if (super.objFireMain != null && (super.f >= 2 && super.f <= 3 || super.f > 16 && super.f < 22)) {
         byte var2 = 16;
         if (super.Dir == 0) {
            var2 = -16;
         }

         super.fraImgEff.drawFrame(2, super.objFireMain.x + var2, super.objFireMain.y - super.objFireMain.hOne / 2 + 2 - super.objFireMain.dy, super.Dir, 3, var1);
      }

      if (super.f >= 3 && super.f <= 6) {
         super.fraImgSubEff.drawFrame((super.f - 12) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      if (super.f >= 8 && super.f <= 11) {
         super.fraImgSub2Eff.drawFrame((super.f - 17) / 2, super.x, super.y, super.Dir, 3, var1);
      }

      Point var3;
      int var4;
      for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
         var3 = (Point)this.VecEff.elementAt(var4);
         super.fraImgSubEff.drawFrame(var3.f / 2, var3.x, var3.y, super.Dir, 33, var1);
      }

      for(var4 = 0; var4 < this.VecSubEff.size(); ++var4) {
         var3 = (Point)this.VecSubEff.elementAt(var4);
         super.fraImgSub3Eff.drawFrame(var3.frame, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void AW(mGraphics var1) {
      if (super.f >= 4 && super.f < super.fRemove) {
         super.fraImgEff.drawFrame((super.f - 4) / super.numNextFrame % super.fraImgEff.nFrame, super.x - super.AZ, super.y, super.Dir, 3, var1);
      }

      int var2;
      if (super.typeEffect != 49 && super.typeEffect != 50) {
         Point var4;
         if (super.typeEffect != 220 && super.typeEffect != 293) {
            if (super.f > 1 && super.f < super.fRemove - 1 && super.fraImgSubEff != null) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, this.x1000, this.y1000 + 5, super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var4 = (Point)this.VecSubEff.elementAt(var2)).f == 0) {
                  super.fraImgSub2Eff.drawFrame(var4.f, var4.x, var4.y, super.Dir, 3, var1);
               } else {
                  super.fraImgSub3Eff.drawFrame(var4.f, var4.x, var4.y, super.Dir, 3, var1);
               }
            }

         } else {
            if (super.f > 1 && super.f < super.fRemove - 1 && super.fraImgSubEff != null) {
               super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, this.x1000, this.y1000 + 5, super.Dir, 33, var1);
            }

            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               if ((var4 = (Point)this.VecSubEff.elementAt(var2)).f == 0) {
                  super.fraImgSub2Eff.drawFrame(var4.frame, var4.x + super.am_duong * 5, var4.y + 4, super.Dir, 3, var1);
               } else if (var4.frame == 0) {
                  super.fraImgSub3Eff.drawFrame(var4.f, var4.x, var4.y, super.Dir, 3, var1);
               } else {
                  super.BP.drawFrame(var4.f, var4.x, var4.y, super.Dir, 3, var1);
               }
            }

         }
      } else {
         for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
            Point_Focus var3 = (Point_Focus)this.VecEff.elementAt(var2);
            if (super.fraImgSubEff != null) {
               super.fraImgSubEff.drawFrame(var3.AG % super.fraImgSubEff.nFrame, var3.x, var3.y, super.Dir, 3, var1);
            }

            super.fraImgSub2Eff.drawFrame((var3.AG + var3.frame) % super.fraImgSub2Eff.nFrame, var3.x, var3.y, super.Dir, 3, var1);
         }

      }
   }

   private void AX(mGraphics var1) {
      if (super.f >= 4 && super.f < super.fRemove) {
         super.fraImgEff.drawFrame((super.f - 4) / super.numNextFrame % super.fraImgEff.nFrame, super.x - super.AZ, super.y, super.Dir, 3, var1);
      }

      if (super.f > 1 && super.f < super.fRemove - 1 && super.fraImgSubEff != null) {
         super.fraImgSubEff.drawFrame(super.f / 2 % super.fraImgSubEff.nFrame, this.x1000, this.y1000 + 5, super.Dir, 33, var1);
      }

      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.VecSubEff.elementAt(var2)).f == 0) {
            super.fraImgSub2Eff.drawFrame(var3.frame, var3.x + super.am_duong * 5, var3.y + 4, var3.dis, 3, var1);
         } else if (var3.frame == 0) {
            super.fraImgSub3Eff.drawFrame(var3.f, var3.x, var3.y, super.Dir, 3, var1);
         } else {
            super.BP.drawFrame(var3.f, var3.x, var3.y, super.Dir, 3, var1);
         }
      }

   }

   private void AY(mGraphics var1) {
      int var2 = 2;
      super.numNextFrame = 4;
      if (super.f > 40) {
         var2 = super.fraImgEff.nFrame;
         super.numNextFrame = 2;
      }

      super.fraImgEff.drawFrame(super.f / super.numNextFrame % var2, super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne / 2, super.Dir, 3, var1);
   }

   private void paintPan_1(mGraphics var1) {
      int num = 3;
      if (super.f > 20) {
         num = super.fraImgEff.nFrame;
      }

      super.fraImgEff.drawFrame(super.f / 2 % num, super.objFireMain.x, super.objFireMain.y, super.Dir, 3, var1);

      for(num = 0; num < this.VecEff.size(); ++num) {
         Point var3 = (Point)this.VecEff.elementAt(num);
         super.fraImgSubEff.drawFrame(var3.frame + var3.f / 3 % 2, var3.x, var3.y, 0, 40, var1);
         super.fraImgSubEff.drawFrame(var3.frame + var3.f / 3 % 2, var3.x, var3.y, 2, 36, var1);
         super.fraImgSubEff.drawFrame(var3.frame + var3.f / 3 % 2, var3.x, var3.y, 1, 24, var1);
         super.fraImgSubEff.drawFrame(var3.frame + var3.f / 3 % 2, var3.x, var3.y, 3, 0, var1);
      }

   }

   public final void replaceHP(mVector var1) {
      label39:
      for(int var2 = 0; var2 < super.vecObjsBeFire.size(); ++var2) {
         Object_Effect_Skill var3;
         if ((var3 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var2)) != null) {
            for(int var4 = 0; var4 < var1.size(); ++var4) {
               Object_Effect_Skill var5;
               if ((var5 = (Object_Effect_Skill)var1.elementAt(var2)) != null && var3.ID == var5.ID) {
                  if (GameScreen.IX) {
                     GameCanvas.chatTabScr.AB(T.tabTestAdmin, "+DAM: ", "" + var3.hpShow, (byte)1, false);
                  }

                  var3.hpShow = var5.hpShow;
                  var3.hpMagic = var5.hpMagic;
                  var3.mEffTypePlus = new int[var5.mEffTypePlus.length];
                  var3.AG = new int[var5.mEffTypePlus.length];
                  var3.mEff_Time_Plus = new int[var5.mEffTypePlus.length];
                  var4 = 0;

                  while(true) {
                     if (var4 >= var3.mEffTypePlus.length) {
                        continue label39;
                     }

                     var3.mEffTypePlus[var4] = var5.mEffTypePlus[var4];
                     var3.AG[var4] = var5.AG[var4];
                     var3.mEff_Time_Plus[var4] = var5.mEff_Time_Plus[var4];
                     ++var4;
                  }
               }
            }
         }
      }

   }

   public static void setHP_New(mVector vec, MainObject objFire, boolean isAdd) {
      for(int i = 0; i < vec.size(); ++i) {
         Object_Effect_Skill object_Effect_Skill;
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)(object_Effect_Skill = (Object_Effect_Skill)vec.elementAt(i)).ID, (byte)object_Effect_Skill.tem)) == null) {
            vec.removeElement(object_Effect_Skill);
            --i;
         } else if (mainObject.Action != 4) {
            if (object_Effect_Skill.hpLast < mainObject.Hp) {
               mainObject.Hp = object_Effect_Skill.hpLast;
            }

            if (isAdd) {
               boolean flag = setAddEffPlus(object_Effect_Skill, mainObject, objFire, objFire);
               byte var7 = 15;
               int var8 = object_Effect_Skill.hpShow;
               if (objFire == GameScreen.player) {
                  var7 = 13;
               }

               if (objFire.typeObject == 1) {
                  var7 = 14;
                  var8 = -var8;
               }

               if (objFire == GameScreen.player && GameScreen.IX) {
                  GameCanvas.chatTabScr.AB(T.tabTestAdmin, "+DAM: ", "" + object_Effect_Skill.hpShow, (byte)1, false);
               }

               if (objFire == GameScreen.player || mainObject == GameScreen.player || !GameCanvas.lowGraphic) {
                  if (object_Effect_Skill.hpShow == 0) {
                     GameScreen.addEffectNumBig_NEW_AP((int)var8, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, (byte)17);
                  } else {
                     if (flag) {
                        var7 = 16;
                     }

                     GameScreen.addEffectNumBig_NEW_AP(var8, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, var7);
                  }
               }

               if (mainObject.Hp <= 0) {
                  mainObject.beginDie(objFire);
               }
            }
         }
      }

   }

   private static boolean setAddEffPlus(Object_Effect_Skill objEff, MainObject obj, MainObject objFire, MainObject OBJMainEff) {
      if (objEff != null && obj != null && objFire != null) {
         boolean result = false;

         for(int i = 0; i < objEff.mEffTypePlus.length; ++i) {
            switch(objEff.mEffTypePlus[i]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 15:
            case 16:
            case 17:
               obj.addEffSpec((short)objEff.mEffTypePlus[i], (short)objEff.mEff_Time_Plus[i]);
               break;
            case 12:
               GameScreen.addEffectNum(objEff.hpShow + T.chuan, obj.x, obj.y - obj.hOne, (byte)11);
               break;
            case 1010:
               if (objEff.hpShow <= 1) {
                  return false;
               }

               GameScreen.addEffectEnd((short)20, 0, obj.x, obj.y - obj.hOne / 2, (byte)obj.Dir, OBJMainEff);
               result = true;
               break;
            case 1013:
               GameScreen.addEffectEnd((short)21, 0, obj.x, obj.y - obj.hOne / 2, (byte)obj.Dir, OBJMainEff);
               break;
            case 1014:
               GameScreen.addEffectEnd_ToX_ToY((short)23, 0, obj.x, obj.y - obj.hOne / 2, (int)objFire.x, (int)(objFire.y - objFire.hOne / 2), (byte)obj.Dir, OBJMainEff);
               break;
            case 1021:
            case 1022:
               GameScreen.addEffectEnd_ObjTo((short)22, (byte)(objEff.mEffTypePlus[i] == 1021 ? 1 : 0), obj.x, obj.y - obj.hOne / 2, (short)objFire.ID, (byte)objFire.typeObject, (byte)objFire.Dir, OBJMainEff);
            }
         }

         return result;
      } else {
         return false;
      }
   }
}
