public final class Effect_Skill extends MainEffect {
   // ─── Constants đồng bộ với Unity (typeEffect IDs) ───
   public static final int TYPE_NIKA_ACTIVE_1_LEVEL1  = 3100;
   public static final int TYPE_NIKA_ACTIVE_1_LEVEL5  = 3101;
   public static final int TYPE_NIKA_ACTIVE_2         = 3102;
   public static final int TYPE_NIKA_BUFF             = 3103;
   public static final int TYPE_LIGHT_ACTIVE_1_LEVEL5 = 3104;
   public static final int TYPE_LIGHT_ACTIVE_2_LEVEL5 = 3105;
   public static final int TYPE_LOVE_ACTIVE_1_LEVEL5  = 3106;
   public static final int TYPE_LOVE_ACTIVE_2_LEVEL5  = 3107;
   public static final int TYPE_NIKYU_ACTIVE_1        = 3120;
   public static final int TYPE_NIKYU_ACTIVE_2        = 3121;
   public static final int TYPE_NIKYU_BUFF            = 3122;

   private static final short NIKYU_PROJECTILE = 3120;
   private static final short NIKYU_IMPACT = 3121;
   private static final short NIKYU_DASH = 3122;
   private static final short NIKYU_REPULSION = 3123;
   private static final short NIKYU_BUFF_EFF = 3124;
   private static final short NIKYU_PASSIVE_EFF = 3125;

   private static final short LIGHT_LEVEL5_CAST = 37;
   private static final short LIGHT_LEVEL5_PROJECTILE = 38;
   private static final short LIGHT_LEVEL5_IMPACT = 39;
   private static final short LIGHT_LEVEL5_ACTIVE_2_START = 40;
   private static final short LOVE_LEVEL5_FINISH_ATTACHED = 20;
   private static final short LOVE_LEVEL5_FINISH_IMPACT = 36;

   private static final int NIKA_VARIANT_ACTIVE_1_LEVEL1 = 0;
   private static final int NIKA_VARIANT_ACTIVE_1_LEVEL5 = 1;
   private static final int NIKA_VARIANT_ACTIVE_2 = 2;

   private static final short NIKA_DATA_LEVEL1_START = 61;
   private static final short NIKA_DATA_LEVEL1_JUMP = 62;
   private static final short NIKA_DATA_LEVEL1_LANDING_LEFT = 63;
   private static final short NIKA_DATA_LEVEL1_LANDING_RIGHT = 64;
   private static final short NIKA_DATA_LEVEL5_START = 65;
   private static final short NIKA_DATA_LEVEL5_JUMP = 66;
   private static final short NIKA_DATA_LEVEL5_LANDING_LEFT = 67;
   private static final short NIKA_DATA_LEVEL5_LANDING_RIGHT = 68;
   private static final short NIKA_DATA_BUFF = 69;
   private static final short NIKA_DATA_ACTIVE_2 = 70;

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
   private boolean isSpawnedPool;
   private MainObject objBeFireMain;
   public MainSkill skill;
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

   public int enelOrigX;
   public int enelOrigY;
   public int phoenixOrigX;
   public int phoenixOrigY;
   public int phoenixTargetX;
   public int phoenixTargetY;
   public int marcoWaveHitMask;
   public int daibutsuCasterX;
   public int daibutsuCasterY;
   public int daibutsuImpactX;
   public int daibutsuImpactY;
   public int kidCannonImpactX;
   public int kidCannonImpactY;



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
         Skill_Info _sk1 = (var1 != null) ? Skill_Info.getSkillFromID(var1.ID) : null;
         int _skillIdx1 = (_sk1 != null) ? _sk1.indexSkillInServer : -1;
         super.typeEffect = (var1 != null) ? getUpgradedEffSkill(var1.AA, var1.lvDevil, _skillIdx1) : 0;
         this.skill = var1;
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
         Skill_Info _sk2 = (var1 != null) ? Skill_Info.getSkillFromID(var1.ID) : null;
         int _skillIdx2 = (_sk2 != null) ? _sk2.indexSkillInServer : -1;
         super.typeEffect = (var1 != null) ? getUpgradedEffSkill(var1.AA, var1.lvDevil, _skillIdx2) : 0;
         this.skill = var1;
         this.subType = var1.AE;
         super.timeBegin = var1.AK;
         super.timeEnd = (var1 != null) ? var1.AF : 0;
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

   public static short getUpgradedEffSkill(short baseEff, byte lvDevil) {
      return getUpgradedEffSkill(baseEff, lvDevil, -1);
   }

   public static short getUpgradedEffSkill(short baseEff, byte lvDevil, int skillIndex) {
      if (lvDevil < 5) return baseEff;
      // Trái Bóng Tối (Teach - Dark): Skill1 400->402, Skill2 401->403
      if (baseEff == 400) return 402;
      if (baseEff == 401) return 403;
      // Trái Ánh Sáng (Kizaru - Light): Skill1 404->406, Skill2 405->407
      if (baseEff == 404) return 406;
      if (baseEff == 405) return 407;
      // Trái Tình Yêu (Hancock - Love): Skill1 408->410, Skill2 409->411
      if (baseEff == 408) return 410;
      if (baseEff == 409) return 411;
      // Trái Nika: Skill1 3100->3101, Skill2 3102->3102, Buff 3103->3103
      if (baseEff == 3100) return 3101;
      if (baseEff == 3102) return 3102;
      if (baseEff == 3103) return 3103;
      switch (baseEff) {
         case 2: return 228;
         case 3: return 229;
         case 4: return 230;
         case 5: return 231;
         case 6: return 232;
         case 10: return 234;
         case 25: return 235;
         case 164: return 227;
         case 245: return 251;
         case 246: return 253;
         case 247: return 254;
         case 248: return 255;
         case 249: return 252;
         default: return baseEff;
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
         if (super.typeEffect == 4017 || super.typeEffect == 4010) {
            this.createThanTrangSkill(4010);
            return true;
         }
         if (super.typeEffect == 4018) {
            this.createVenomRain4018();
            return true;
         }
         if (super.typeEffect == 4019) {
            this.createVenomBuff4019();
            return true;
         }
         if ((super.typeEffect >= 4001 && super.typeEffect <= 4080) || (super.typeEffect >= 4201 && super.typeEffect <= 4216) || (super.typeEffect >= 4501 && super.typeEffect <= 4516)) {
            this.createThanTrangSkill(super.typeEffect);
            return true;
         }

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
         // ─── Boa Hancock Skill 1 Level 1..4 (Mero Mero Mellow) ───
         case 404:
         {
            DataSkillEff eff16 = new DataSkillEff((short)16, 0);
            super.fRemove = (eff16.sequence != null && eff16.sequence.length > 0) ? eff16.sequence.length : 44;
            DataSkillEff eff17 = new DataSkillEff((short)17, 0);
            int seq17Len = (eff17.sequence != null && eff17.sequence.length > 0) ? eff17.sequence.length : 26;
            super.mframe = new int[1];
            super.mframe[0] = super.fRemove - seq17Len;
            if (super.mframe[0] < 0) super.mframe[0] = 18;
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)14, 2000);
               super.objFireMain.addDataEff((short)15, 2000);
               super.objFireMain.addDataEff((short)16, 0);
               LoadMap.timeVibrateScreen = CRes.random(6, 20);
               GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
            }
            break;
         }
         // ─── Boa Hancock Skill 2 Level 1..4 (Slave Arrow) ───
         case 405:
         {
            super.frame = 5;
            super.mframe = new int[super.frame];
            super.mframe[0] = 0;
            int[] defaultSeqLens405 = new int[] { 8, 4, 6, 10 };
            for (short n = 1; n < 5; n++)
            {
               DataSkillEff eff = new DataSkillEff((short)(n + 18), 0);
               int seqLen = (eff.sequence != null && eff.sequence.length > 0) ? eff.sequence.length : defaultSeqLens405[n - 1];
               super.mframe[n] = seqLen + super.mframe[n - 1] + 1;
            }
            if (this.VecEff == null) this.VecEff = new mVector();
            this.VecEff.removeAllElements();
            if (this.VecSubEff == null) this.VecSubEff = new mVector();
            this.VecSubEff.removeAllElements();
            if (super.vecObjsBeFire != null)
            {
               for (int j = 0; j < super.vecObjsBeFire.size(); j++)
               {
                  Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(j);
                  if (objEff != null)
                  {
                     MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                     if (target != null)
                     {
                        Point pt = new Point();
                        pt.obj = target;
                        pt.AZ = target;
                        pt.frame = j;
                        pt.f = -(j * 5);
                        pt.fRe = super.mframe[super.frame - 1];
                        this.VecEff.addElement(pt);
                     }
                  }
               }
            }
            break;
         }
         // ─── Boa Hancock Skill 1 Level 5 (Upgraded Mero Mero Mellow) ───
         case 406:
         {
            DataSkillEff eff37 = new DataSkillEff((short)37, 0);
            super.fRemove = (eff37.sequence != null && eff37.sequence.length > 0) ? eff37.sequence.length : 56;
            DataSkillEff eff17 = new DataSkillEff((short)17, 0);
            int seq17Len = (eff17.sequence != null && eff17.sequence.length > 0) ? eff17.sequence.length : 26;
            DataSkillEff eff38 = new DataSkillEff((short)38, 0);
            int seq38Len = (eff38.sequence != null && eff38.sequence.length > 0) ? eff38.sequence.length : 6;
            DataSkillEff eff40 = new DataSkillEff((short)40, 0);
            int seq40Len = (eff40.sequence != null && eff40.sequence.length > 0) ? eff40.sequence.length : 6;
            super.mframe = new int[3];
            super.mframe[0] = super.fRemove - seq17Len - seq38Len - seq40Len;
            super.mframe[1] = super.fRemove - seq38Len - seq40Len;
            super.mframe[2] = super.fRemove - seq40Len;
            if (super.mframe[0] < 0) super.mframe[0] = 18;
            if (super.mframe[1] <= super.mframe[0]) super.mframe[1] = 44;
            if (super.mframe[2] <= super.mframe[1]) super.mframe[2] = 50;
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)14, 2000);
               super.objFireMain.addDataEff((short)15, 2000);
               super.objFireMain.addDataEff((short)37, 0);
               LoadMap.timeVibrateScreen = CRes.random(6, 20);
               GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
            }
            break;
         }
         // ─── Boa Hancock Skill 2 Level 5 (Upgraded Slave Arrow) ───
         case 407:
         {
            super.frame = 5;
            super.mframe = new int[super.frame];
            super.mframe[0] = 0;
            for (short n = 1; n < 5; n++)
            {
               DataSkillEff eff = new DataSkillEff((short)(n + 45), 0);
               int seqLen = (eff.sequence != null) ? eff.sequence.length : 8;
               super.mframe[n] = seqLen + super.mframe[n - 1] + 1;
            }
            if (this.VecEff == null) this.VecEff = new mVector();
            this.VecEff.removeAllElements();
            if (this.VecSubEff == null) this.VecSubEff = new mVector();
            this.VecSubEff.removeAllElements();
            if (super.vecObjsBeFire != null)
            {
               for (int j = 0; j < super.vecObjsBeFire.size(); j++)
               {
                  Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(j);
                  if (objEff != null)
                  {
                     MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                     if (target != null)
                     {
                        Point pt = new Point();
                        pt.obj = target;
                        pt.AZ = target;
                        pt.frame = j;
                        pt.f = -(j * 5);
                        pt.fRe = super.mframe[super.frame - 1];
                        this.VecEff.addElement(pt);
                     }
                  }
               }
            }
            break;
         }
         // ─── Kizaru Skill 1 Level 1..4 (Yasakani no Magatama) ───
         case 408:
         {
            DataSkillEff eff26 = new DataSkillEff((short)26, 0);
            super.frame = ((eff26.sequence != null) ? eff26.sequence.length : 20) / 2;
            if (this.VecEff == null) this.VecEff = new mVector();
            this.VecEff.removeAllElements();
            int dist = 0;
            if (super.vecObjsBeFire != null)
            {
               for (int idx = 0; idx < super.vecObjsBeFire.size(); idx++)
               {
                  Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(idx);
                  if (objEff != null)
                  {
                     MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                     if (target != null)
                     {
                        dist = (super.objFireMain.x < target.x)
                           ? (target.x - super.objFireMain.x - super.objFireMain.wOne) / 18 + 3
                           : (super.objFireMain.x - target.x) / 18 + 3;
                        Point pt = new Point(super.objFireMain.x, super.objFireMain.y);
                        pt.fRe = dist;
                        pt.dir = (super.objFireMain.x < target.x) ? 2 : 0;
                        pt.color = pt.dir;
                        pt.frame = idx;
                        pt.obj = target;
                        pt.AZ = target;
                        this.VecEff.addElement(pt);
                     }
                  }
               }
            }
            DataSkillEff eff28 = new DataSkillEff((short)28, 0);
            super.fRemove = super.frame + dist + 3 + ((eff28.sequence != null) ? eff28.sequence.length : 10);
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)25, 0);
               super.objFireMain.addDataEff((short)26, 0);
               super.objFireMain.addDataEff((short)27, 0);
               LoadMap.timeVibrateScreen = CRes.random(6, 20);
               GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
            }
            break;
         }
         // ─── Kizaru Skill 2 Level 1..4 (Yata no Kagami) ───
         case 409:
         {
            super.frame = 2;
            super.fRemove = 1;
            super.mframe = new int[super.frame];
            for (short i = 0; i < super.frame; i++)
            {
               DataSkillEff eff = new DataSkillEff((short)(i + 29), 0);
               int seqLen = (eff.sequence != null) ? eff.sequence.length : 4;
               super.mframe[i] = seqLen + super.fRemove + 1;
               super.fRemove = super.mframe[i];
            }
            DataSkillEff eff33 = new DataSkillEff((short)33, 0);
            super.fRemove += (eff33.sequence != null) ? eff33.sequence.length : 10;
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)29, 0);
            }
            break;
         }
         // ─── Kizaru Skill 1 Level 5 (Upgraded Yasakani no Magatama) ───
         case 410:
         {
            DataSkillEff eff26 = new DataSkillEff((short)26, 0);
            super.frame = ((eff26.sequence != null) ? eff26.sequence.length : 20) / 2;
            if (this.VecEff == null) this.VecEff = new mVector();
            this.VecEff.removeAllElements();
            int dist = 0;
            if (super.vecObjsBeFire != null)
            {
               for (int idx = 0; idx < super.vecObjsBeFire.size(); idx++)
               {
                  Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(idx);
                  if (objEff != null)
                  {
                     MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                     if (target != null)
                     {
                        dist = (super.objFireMain.x < target.x)
                           ? (target.x - super.objFireMain.x - super.objFireMain.wOne) / 18 + 3
                           : (super.objFireMain.x - target.x) / 18 + 3;
                        Point pt = new Point(super.objFireMain.x, super.objFireMain.y);
                        pt.fRe = dist;
                        pt.dir = (super.objFireMain.x < target.x) ? 2 : 0;
                        pt.color = pt.dir;
                        pt.frame = idx;
                        pt.obj = target;
                        pt.AZ = target;
                        this.VecEff.addElement(pt);
                     }
                  }
               }
            }
            DataSkillEff eff28 = new DataSkillEff((short)28, 0);
            super.fRemove = super.frame + dist + 3 + ((eff28.sequence != null) ? eff28.sequence.length : 10);
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)25, 0);
               super.objFireMain.addDataEff((short)26, 0);
               super.objFireMain.addDataEff((short)27, 0);
               LoadMap.timeVibrateScreen = CRes.random(6, 20);
               GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
            }
            break;
         }
         // ─── Kizaru Skill 2 Level 5 (Upgraded Yata no Kagami) ───
         case 411:
         {
            super.frame = 2;
            super.fRemove = 1;
            super.mframe = new int[super.frame];
            for (short i = 0; i < super.frame; i++)
            {
               DataSkillEff eff = new DataSkillEff((short)(i + 41), 0);
               int seqLen = (eff.sequence != null) ? eff.sequence.length : 4;
               super.mframe[i] = seqLen + super.fRemove + 1;
               super.fRemove = super.mframe[i];
            }
            DataSkillEff eff45 = new DataSkillEff((short)45, 0);
            super.fRemove += (eff45.sequence != null) ? eff45.sequence.length : 10;
            if (super.objFireMain != null)
            {
               super.objFireMain.addDataEff((short)41, 0);
            }
            break;
         }
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
            super.fraImgSub2Eff = null; // Old skill effs only 0-466. 468 is Thần Trang Venom
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
         // ─── Trái Nika ───
         case 3100:
            this.createNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL1);
            break;
         case 3101:
            this.createNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL5);
            break;
         case 3102:
            this.createNikaJump(NIKA_VARIANT_ACTIVE_2);
            break;
         case 3103:
            this.createNikaBuff();
            break;
         // ─── Trái Ánh Sáng ───
         case 3104:
            this.createLightActive1Level5();
            break;
         case 3105:
            this.createLightActive2Level5();
            break;
         // ─── Trái Tình Yêu ───
         case 3106:
            this.createSkillBuff((short)super.timeBegin);
            break;
         case 3107:
            this.createLoveActive2Level5();
            break;
         // ─── Trái Chân Gấu (Nikyu) ───
         case 3120:
            this.createNikyuActive1();
            break;
         case 3121:
            this.createNikyuActive2();
            break;
         case 3122:
            this.createNikyuBuff();
            break;
         case 4017:
            this.createThanTrangSkill(4010);
            break;
         case 4018:
            this.createVenomRain4018();
            break;
         case 4019:
            this.createVenomBuff4019();
            break;
         // ─── 16 Thần Trang (4001..4080, 4201..4216, 4501..4516) ───
         case 4001: case 4002: case 4003: case 4004: case 4005: case 4006:
         case 4007: case 4008: case 4009: case 4010: case 4011: case 4012:
         case 4013: case 4014: case 4015: case 4016:
         case 4201: case 4202: case 4203: case 4204: case 4205: case 4206:
         case 4207: case 4208: case 4209: case 4210: case 4211: case 4212:
         case 4213: case 4214: case 4215: case 4216:
         case 4501: case 4502: case 4503: case 4504: case 4505: case 4506:
         case 4507: case 4508: case 4509: case 4510: case 4511: case 4512:
         case 4513: case 4514: case 4515: case 4516:
            this.createThanTrangSkill(super.typeEffect);
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

   private void paintEffThunderFall(mGraphics var1) {
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
      if (super.typeEffect == 4017 || super.typeEffect == 4010) {
         this.paintThanTrangSkill(var1);
         return;
      }
      if (super.typeEffect == 4018) {
         this.paintVenomRain4018(var1);
         return;
      }
      if (super.typeEffect == 4019) {
         this.paintVenomBuff4019(var1);
         return;
      }
      if ((super.typeEffect >= 4001 && super.typeEffect <= 4080) || (super.typeEffect >= 4201 && super.typeEffect <= 4216) || (super.typeEffect >= 4501 && super.typeEffect <= 4516)) {
         this.paintThanTrangSkill(var1);
         return;
      }
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
               this.paint_Bullet(var1, super.fraImgEff, var13.frame, var13.x, var13.y);
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
            this.paintSanji_3(var1);
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
            this.paintCrocodile1(var1);
            return;
         case 26:
         case 236:
            this.paintCrocodile2(var1);
            return;
         case 27:
            if (super.f % 4 < 2) {
               this.paint_Bullet(var1, super.fraImgEff, super.frame, super.x, super.y);
               return;
            }

            this.paint_Bullet(var1, super.fraImgSubEff, super.frame, super.x, super.y);
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
               this.paint_Bullet(var1, super.fraImgEff, super.frame, super.x, super.y);
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
            this.paintLuffy_New2_SHORT(var1);
            return;
         case 85:
         case 182:
         case 214:
         case 273:
            this.paintLuffy_New3(var1);
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
            this.paintBuggy_2(var1);
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
            this.paintDonKrieg_1(var1);
            return;
         case 110:
            this.paintDonKrieg_2(var1);
            return;
         case 111:
            this.paintDonKrieg_3(var1);
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
            this.paintKurobi_2(var1);
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
            this.paintLuffy_New2(var1);
            return;
         case 161:
            this.paintZoroS2_L3_SHORT(var1);
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
            this.paintEff_Mr1_2(var1);
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
            this.paintEff_Df_2(var1);
            return;
         case 178:
            this.paintEff_Mr0_1(var1);
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
               this.paint_Bullet(var1, super.fraImgEff, super.frame, super.x, super.y);
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
            this.paintEffTru(var1);
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
            this.paint_Dong_Dat_1(var1);
            return;
         case 211:
         case 244:
            this.paint_Dong_Dat_2(var1);
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
            this.paintEffTru2(var1);
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
               this.paint_Bullet(var1, super.fraImgEff, super.frame, super.x, super.y);
               this.paint_Bullet(var1, super.fraImgEff, this.DL, this.DJ.x, this.DJ.y);
               this.paint_Bullet(var1, super.fraImgEff, this.DM, this.DK.x, this.DK.y);
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
         // ─── Paint Boa Hancock & Kizaru (không vẽ fraImgEff) ───
         case 404:
         case 405:
         case 406:
         case 407:
         case 408:
         case 409:
         case 410:
         case 411:
            return;
         case 471:
            for(var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
               var15 = (Point)this.VecSubEff.elementAt(var2);
               super.fraImgEff.drawFrame(var15.f % super.fraImgEff.nFrame, var15.x, var15.y, 0, 3, var1);
            }

            return;
         case 472:
            this.paint_Luffy_S2_L7(var1);
            return;
         case 473:
            this.paint_Luffy_S3_L7(var1);
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
            this.paint_Sanji_S3_L7(var1);
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
            this.paintEffThunderFall(var1);
            return;
         case 2000:
            super.fraImgEff.drawFrame(super.AS, super.x, super.y - 22, 0, 3, var1);
            return;
         // ─── Paint Trái Nika ───
         case 3100: case 3101: case 3102:
            this.paintNikaJump(var1);
            return;
         case 3103:
            return;
         // ─── Paint Trái Ánh Sáng ───
         case 3104: case 3105:
            return;
         // ─── Paint Trái Tình Yêu ───
         case 3106: case 3107:
            return;
         // ─── Paint Trái Chân Gấu (Nikyu) ───
         case 3120: case 3121: case 3122:
            return;
         // ─── Paint 16 Thần Trang (4001..4080, 4201..4216, 4501..4516) ───
         case 4001: case 4002: case 4003: case 4004: case 4005: case 4006:
         case 4007: case 4008: case 4009: case 4010: case 4011: case 4012:
         case 4013: case 4014: case 4015: case 4016:
         case 4201: case 4202: case 4203: case 4204: case 4205: case 4206:
         case 4207: case 4208: case 4209: case 4210: case 4211: case 4212:
         case 4213: case 4214: case 4215: case 4216:
         case 4501: case 4502: case 4503: case 4504: case 4505: case 4506:
         case 4507: case 4508: case 4509: case 4510: case 4511: case 4512:
         case 4513: case 4514: case 4515: case 4516:
            this.paintThanTrangSkill(var1);
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
            this.paintGalio_1(var1);
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

   private void paintEffTru2(mGraphics var1) {
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

   private void paint_Dong_Dat_2(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecSubEff.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.VecSubEff.elementAt(var2)).frame == 0 && super.typeEffect == 244) {
            super.fraImgSubEff.drawFrameNew(var3.f / 3 % super.fraImgSubEff.nFrame, var3.x, var3.y, super.Dir, 33, var1);
         } else {
            super.fraImgEff.drawFrameNew(var3.f / 3 % super.fraImgEff.nFrame, var3.x, var3.y, super.Dir, 33, var1);
         }
      }

   }

   private void paint_Dong_Dat_1(mGraphics var1) {
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

   private void paintEffTru(mGraphics var1) {
      if (super.f <= super.fRemove) {
         super.fraImgEff.drawFrame(0, super.x, super.y, 0, 3, var1);
      }

      for(int var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(var3.f, var3.x, var3.y, 0, 3, var1);
      }

   }

   private void paintEff_Mr0_1(mGraphics var1) {
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

   private void paintEff_Df_2(mGraphics var1) {
      for(int var2 = this.VecEff.size() - 1; var2 >= 0; --var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(var3.frame, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void paintEff_Mr1_2(mGraphics var1) {
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

   private void paintCrocodile2(mGraphics var1) {
      for(int var2 = 0; var2 < this.VecEff.size(); ++var2) {
         Point var3 = (Point)this.VecEff.elementAt(var2);
         super.fraImgEff.drawFrame(2 + var3.f % 3, var3.x, var3.y, super.Dir, 33, var1);
      }

   }

   private void paintCrocodile1(mGraphics var1) {
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

   private void paintZoroS2_L3_SHORT(mGraphics var1) {
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
      super.update();
      if (super.typeEffect == 4017 || super.typeEffect == 4010) {
         this.updateThanTrangSkill(4010);
         return;
      }
      if (super.typeEffect == 4018) {
         this.updateVenomRain4018();
         return;
      }
      if (super.typeEffect == 4019) {
         this.updateVenomBuff4019();
         return;
      }
      if ((super.typeEffect >= 4001 && super.typeEffect <= 4080) || (super.typeEffect >= 4201 && super.typeEffect <= 4216) || (super.typeEffect >= 4501 && super.typeEffect <= 4516)) {
         this.updateThanTrangSkill(super.typeEffect);
         return;
      }
      if (super.objFireMain == null || !super.objFireMain.returnAction() && super.objFireMain.Action != 4) {
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
         case 404:
         {
            // Boa Hancock Skill 1 Level 1..4 (Mero Mero Mellow)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            MainObject target404 = (this.objBeFireMain != null) ? this.objBeFireMain :
               (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0 ?
                  MainObject.get_Object((int)((Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)).ID, (byte)((Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)).tem) : null);
            if (target404 != null)
            {
               if (super.mframe != null && super.mframe.length > 0 && super.f == super.mframe[0])
               {
                  for (short n = 17; n < 19; n++)
                  {
                     GameScreen.addHightDataeff(n, target404.x, target404.y);
                  }
               }
               if (super.mframe != null && super.mframe.length > 0 && super.f == super.mframe[0] + 7)
               {
                  GameScreen.addEffectEnd((short)108, 5, target404.x + CRes.random_Am_0(60), target404.y - CRes.random(30), super.Dir, target404);
                  LoadMap.timeVibrateScreen = CRes.random(6, 20);
                  GameScreen.addEffectEnd((short)112, 0, target404.x, target404.y, super.Dir, target404);
               }
            }
            return;
         }
         case 405:
         {
            // Boa Hancock Skill 2 Level 1..4 (Slave Arrow)
            if (this.VecEff == null) { this.removeEff(); return; }
            for (int j = 0; j < this.VecEff.size(); j++)
            {
               Point pt = (Point)this.VecEff.elementAt(j);
               MainObject ptObj = (pt.obj != null) ? pt.obj : pt.AZ;
               if (ptObj != null)
               {
                  for (short n = 0; n < super.frame - 1; n++)
                  {
                     if (pt.f == super.mframe[n])
                     {
                        GameScreen.addHightDataeff((short)(n + 19), ptObj.x, ptObj.y);
                        if (n == 1)
                        {
                           GameScreen.addEffectEnd((short)110, 0, ptObj.x + CRes.random_Am_0(15), ptObj.y + CRes.random_Am_0(5), super.Dir, ptObj);
                           GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                           LoadMap.timeVibrateScreen = CRes.random(1, 5);
                        }
                        if (n == 3)
                        {
                           LoadMap.timeVibrateScreen = CRes.random(6, 20);
                           GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                        }
                     }
                  }
                  if (super.mframe != null && super.mframe.length > 1 && pt.f > super.mframe[1])
                  {
                     if (pt.f % 3 == 0)
                     {
                        GameScreen.addEffectEnd((short)108, 5, ptObj.x + CRes.random_Am_0(10), ptObj.y - CRes.random(240), super.Dir, ptObj);
                     }
                     if (super.frame >= 2 && pt.f == super.mframe[super.frame - 2] + 5)
                     {
                        GameScreen.addEffectEnd((short)108, 5, ptObj.x + CRes.random_Am_0(60), ptObj.y - CRes.random(30), super.Dir, ptObj);
                     }
                  }
               }
               pt.f++;
               if (pt.f >= pt.fRe)
               {
                  this.VecEff.removeElementAt(j);
                  j--;
               }
            }
            if (this.VecEff.size() == 0)
            {
               this.removeEff();
            }
            return;
         }
         case 406:
         {
            // Boa Hancock Skill 1 Level 5 (Upgraded Mero Mero Mellow)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            MainObject target406 = (this.objBeFireMain != null) ? this.objBeFireMain :
               (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0 ?
                  MainObject.get_Object((int)((Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)).ID, (byte)((Object_Effect_Skill)super.vecObjsBeFire.elementAt(0)).tem) : null);
            if (target406 != null)
            {
               if (super.mframe != null && super.mframe.length > 0 && super.f == super.mframe[0])
               {
                  for (short n = 17; n < 19; n++)
                  {
                     GameScreen.addHightDataeff(n, target406.x, target406.y);
                  }
               }
               if (super.mframe != null && super.mframe.length > 1 && super.f == super.mframe[1])
               {
                  GameScreen.addHightDataeff((short)38, target406.x, target406.y);
               }
               if (super.mframe != null && super.mframe.length > 2 && super.f == super.mframe[2])
               {
                  GameScreen.addHightDataeff((short)39, target406.x, target406.y);
                  GameScreen.addHightDataeff((short)40, target406.x, target406.y);
               }
               if (super.mframe != null && super.mframe.length > 0 && super.f == super.mframe[0] + 7)
               {
                  GameScreen.addEffectEnd((short)108, 5, target406.x + CRes.random_Am_0(60), target406.y - CRes.random(30), super.Dir, target406);
                  LoadMap.timeVibrateScreen = CRes.random(6, 20);
                  GameScreen.addEffectEnd((short)112, 0, target406.x, target406.y, super.Dir, target406);
               }
            }
            return;
         }
         case 407:
         {
            // Boa Hancock Skill 2 Level 5 (Upgraded Slave Arrow)
            if (this.VecEff == null) { this.removeEff(); return; }
            for (int j = 0; j < this.VecEff.size(); j++)
            {
               Point pt = (Point)this.VecEff.elementAt(j);
               MainObject ptObj = (pt.obj != null) ? pt.obj : pt.AZ;
               if (ptObj != null)
               {
                  for (short n = 0; n < super.frame - 1; n++)
                  {
                     if (pt.f == super.mframe[n])
                     {
                        GameScreen.addHightDataeff((short)(n + 46), ptObj.x, ptObj.y);
                        if (n == 1)
                        {
                           GameScreen.addEffectEnd((short)110, 0, ptObj.x + CRes.random_Am_0(15), ptObj.y + CRes.random_Am_0(5), super.Dir, ptObj);
                           GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                           LoadMap.timeVibrateScreen = CRes.random(1, 5);
                        }
                        if (n == 3)
                        {
                           LoadMap.timeVibrateScreen = CRes.random(6, 20);
                           GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                        }
                     }
                  }
                  if (super.mframe != null && super.mframe.length > 1 && pt.f > super.mframe[1])
                  {
                     if (pt.f % 3 == 0)
                     {
                        GameScreen.addEffectEnd((short)108, 5, ptObj.x + CRes.random_Am_0(10), ptObj.y - CRes.random(240), super.Dir, ptObj);
                     }
                     if (super.frame >= 2 && pt.f == super.mframe[super.frame - 2] + 5)
                     {
                        GameScreen.addEffectEnd((short)108, 5, ptObj.x + CRes.random_Am_0(60), ptObj.y - CRes.random(30), super.Dir, ptObj);
                     }
                  }
               }
               pt.f++;
               if (pt.f >= pt.fRe)
               {
                  this.VecEff.removeElementAt(j);
                  j--;
               }
            }
            if (this.VecEff.size() == 0)
            {
               this.removeEff();
            }
            return;
         }
         case 408:
         {
            // Kizaru Skill 1 Level 1..4 (Yasakani no Magatama)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            if (super.f > super.frame && this.VecEff != null)
            {
               for (int idx = 0; idx < this.VecEff.size(); idx++)
               {
                  Point pt = (Point)this.VecEff.elementAt(idx);
                  int ptDir = (pt.dir != 0) ? pt.dir : pt.color;
                  pt.x += (ptDir - 1) * 20;
                  short num3 = (short)((pt.frame == 0) ? 36 : 34);
                  if (ptDir > 0)
                  {
                     GameScreen.addHightDataeff(num3, pt.x, pt.y);
                  }
                  else
                  {
                     GameScreen.addHightDataeff(num3, pt.x, pt.y, true);
                  }
                  MainObject ptObj = (pt.obj != null) ? pt.obj : pt.AZ;
                  if (pt.f == pt.fRe - 2)
                  {
                     if (ptObj != null)
                     {
                        ptObj.x = pt.x + (ptDir - 1) * 10;
                        GameScreen.addHightDataeff((short)33, ptObj.x, ptObj.y);
                        GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                        this.setAva(2, ptObj);
                     }
                  }
                  if (pt.f == pt.fRe)
                  {
                     GameScreen.addHightDataeff((short)28, pt.x, pt.y - 40);
                     GameScreen.addHightDataeff((short)28, pt.x, pt.y);
                     this.VecEff.removeElementAt(idx);
                     idx--;
                  }
                  else
                  {
                     pt.f++;
                  }
               }
            }
            return;
         }
         case 409:
         {
            // Kizaru Skill 2 Level 1..4 (Yata no Kagami)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            if (super.objFireMain == null) { this.removeEff(); return; }
            for (int i = 0; i < super.frame; i++)
            {
               if (super.mframe != null && super.f == super.mframe[i])
               {
                  super.objFireMain.addDataEff((short)(i + 30), 0);
                  if (i == 0)
                  {
                     LoadMap.timeVibrateScreen = CRes.random(1, 5);
                     GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
                     int dirFactor = ((super.objFireMain.type_left_right == 2) ? 1 : (-1));
                     if (super.vecObjsBeFire != null)
                     {
                        for (int targetIdx = 0; targetIdx < super.vecObjsBeFire.size(); targetIdx++)
                        {
                           Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(targetIdx);
                           if (objEff != null)
                           {
                              MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                              if (target != null)
                              {
                                 GameScreen.addHightDataeff((short)33, target.x, target.y);
                                 GameScreen.addEffectEnd((short)112, 0, target.x, target.y, super.Dir, target);
                                 target.x += dirFactor * 20;
                                 this.setAva(2, target);
                              }
                           }
                        }
                     }
                  }
                  if (i == super.frame - 1)
                  {
                     super.objFireMain.addDataEff((short)32, 0);
                     LoadMap.timeVibrateScreen = CRes.random(1, 5);
                  }
               }
            }
            return;
         }
         case 410:
         {
            // Kizaru Skill 1 Level 5 (Upgraded Yasakani no Magatama)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            if (super.f > super.frame && this.VecEff != null)
            {
               for (int idx = 0; idx < this.VecEff.size(); idx++)
               {
                  Point pt = (Point)this.VecEff.elementAt(idx);
                  int ptDir = (pt.dir != 0) ? pt.dir : pt.color;
                  pt.x += (ptDir - 1) * 20;
                  short num54 = (short)((pt.frame == 0) ? 35 : 36);
                  if (ptDir > 0)
                  {
                     GameScreen.addHightDataeff(num54, pt.x, pt.y);
                  }
                  else
                  {
                     GameScreen.addHightDataeff(num54, pt.x, pt.y, true);
                  }
                  MainObject ptObj = (pt.obj != null) ? pt.obj : pt.AZ;
                  if (pt.f == pt.fRe - 2)
                  {
                     if (ptObj != null)
                     {
                        ptObj.x = pt.x + (ptDir - 1) * 10;
                        GameScreen.addHightDataeff((short)33, ptObj.x, ptObj.y);
                        GameScreen.addEffectEnd((short)112, 0, ptObj.x, ptObj.y, super.Dir, ptObj);
                        this.setAva(2, ptObj);
                     }
                  }
                  if (pt.f == pt.fRe)
                  {
                     int offset = (ptDir - 1) * 15;
                     GameScreen.addHightDataeff((short)28, pt.x, pt.y - 40);
                     GameScreen.addHightDataeff((short)28, pt.x, pt.y);
                     GameScreen.addHightDataeff((short)28, pt.x + offset, pt.y - 60);
                     GameScreen.addHightDataeff((short)28, pt.x + offset, pt.y + 20);
                     this.VecEff.removeElementAt(idx);
                     idx--;
                  }
                  else
                  {
                     pt.f++;
                  }
               }
            }
            return;
         }
         case 411:
         {
            // Kizaru Skill 2 Level 5 (Upgraded Yata no Kagami)
            if (super.f > super.fRemove)
            {
               this.removeEff();
            }
            if (super.objFireMain == null) { this.removeEff(); return; }
            for (int i = 0; i < super.frame; i++)
            {
               if (super.mframe != null && super.f == super.mframe[i])
               {
                  super.objFireMain.addDataEff((short)(i + 42), 0);
                  if (i == 0)
                  {
                     LoadMap.timeVibrateScreen = CRes.random(1, 5);
                     GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
                     int dirFactor = ((super.objFireMain.type_left_right == 2) ? 1 : (-1));
                     if (super.vecObjsBeFire != null)
                     {
                        for (int targetIdx = 0; targetIdx < super.vecObjsBeFire.size(); targetIdx++)
                        {
                           Object_Effect_Skill objEff = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(targetIdx);
                           if (objEff != null)
                           {
                              MainObject target = MainObject.get_Object((int)objEff.ID, (byte)objEff.tem);
                              if (target != null)
                              {
                                 GameScreen.addHightDataeff((short)45, target.x, target.y);
                                 GameScreen.addEffectEnd((short)112, 0, target.x, target.y, super.Dir, target);
                                 target.x += dirFactor * 20;
                                 this.setAva(2, target);
                              }
                           }
                        }
                     }
                  }
                  if (i == super.frame - 1)
                  {
                     super.objFireMain.addDataEff((short)44, 0);
                     LoadMap.timeVibrateScreen = CRes.random(1, 5);
                  }
               }
            }
            return;
         }
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
         // ─── Update Trái Nika ───
         case 3100:
            this.updateNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL1);
            return;
         case 3101:
            this.updateNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL5);
            return;
         case 3102:
            this.updateNikaJump(NIKA_VARIANT_ACTIVE_2);
            return;
         case 3103:
            this.updateNikaBuff();
            return;
         // ─── Update Trái Ánh Sáng ───
         case 3104:
            this.updateLightActive1Level5();
            return;
         case 3105:
            this.updateLightActive2Level5();
            return;
         // ─── Update Trái Tình Yêu ───
         case 3106:
            this.updateSkillBuff();
            return;
         case 3107:
            this.updateLoveActive2Level5();
            return;
         // ─── Update Trái Chân Gấu (Nikyu) ───
         case 3120:
            this.updateNikyuActive1();
            return;
         case 3121:
            this.updateNikyuActive2();
            return;
         case 3122:
            this.updateNikyuBuff();
            return;
         case 4017:
         case 4010:
            if (this.subType == 1004)
            {
               if (super.f >= super.fRemove)
               {
                  this.removeEff();
               }
               return;
            }
            this.updateThanTrangSkill(4010);
            return;
         case 4018:
            this.updateVenomRain4018();
            return;
         case 4019:
            this.updateVenomBuff4019();
            return;
         // ─── Update 16 Thần Trang (4001..4080, 4201..4216, 4501..4516) ───
         case 4001: case 4002: case 4003: case 4004: case 4005: case 4006:
         case 4007: case 4008: case 4009: case 4011: case 4012:
         case 4013: case 4014: case 4015: case 4016:
         case 4201: case 4202: case 4203: case 4204: case 4205: case 4206:
         case 4207: case 4208: case 4209: case 4210: case 4211: case 4212:
         case 4213: case 4214: case 4215: case 4216:
         case 4501: case 4502: case 4503: case 4504: case 4505: case 4506:
         case 4507: case 4508: case 4509: case 4510: case 4511: case 4512:
         case 4513: case 4514: case 4515: case 4516:
            this.updateThanTrangSkill(super.typeEffect);
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
      int effSetId = (super.typeEffect >= 4001 && super.typeEffect <= 4016) ? (super.typeEffect - 4000) : ((super.typeEffect >= 4201 && super.typeEffect <= 4216) ? (super.typeEffect - 4200) : ((super.typeEffect >= 4501 && super.typeEffect <= 4516) ? (super.typeEffect - 4500) : (((super.typeEffect - 4001) / 5) + 1)));
      if (effSetId == 5 || effSetId == 9) {
         Player.isBlock = false;
         GameCanvas.gameScr.isFullScreen = false;
      }
      if (GameScreen.typePaintGameScreen == 1) {
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
      super.fraImgSubEff = null; // Old skill effs only 0-466. 470 is Thần Trang Venom
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
      super.fraImgEff = new FrameImage(443, 5);
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

   private void addPoint(int var1, int var2) {
      Point var3;
      (var3 = new Point()).x = var1;
      var3.y = var2;
      this.VecEff.addElement(var3);
   }

   private void updateDoor2() {
      if (super.f >= 2 && super.f <= 20) {
         if (super.f == 2) {
            this.addPoint(super.x, super.y);
         }

         if (super.f == 6) {
            this.addPoint(this.objBeFireMain.x + 90 * super.am_duong, super.y - 60);
         }

         if (super.f == 8) {
            this.addPoint(this.objBeFireMain.x - 90 * super.am_duong, super.y - 60);
         }

         if (super.f == 12) {
            this.addPoint(this.objBeFireMain.x + 90 * super.am_duong, super.y + 60);
         }

         if (super.f == 16) {
            this.addPoint(this.objBeFireMain.x - 90 * super.am_duong, super.y + 60);
         }

         if (super.f == 20) {
            this.addPoint(this.objBeFireMain.x + 40 * super.am_duong, super.y);
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
            this.createSkillZoro2(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillZoro2(0, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillZoro2(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillZoro2(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillZoro2(0, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillZoro2(1, super.toX + CRes.random_Am_0(5), super.toY - 20, 2);
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
            this.createSkillKuro(0, super.toX + CRes.random_Am_0(30), super.toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
            this.setAva(0, this.objBeFireMain);
         } else if (super.f % 4 == 0) {
            this.createSkillKuro(CRes.random(4), super.toX + CRes.random_Am_0(30), super.toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
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

   private void createSkillKuro(int type, int x, int y, int size) {
      Point var5;
      if (type == 0) {
         for(type = 0; type < size; ++type) {
            (var5 = new Point()).y = y;
            if (super.Dir == 2) {
               var5.x = x + type * 7;
            } else {
               var5.x = x - type * 7;
            }

            var5.vy = -7;
            var5.frame = 2;
            var5.fRe = 5;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else if (type == 1) {
         for(type = 0; type < size; ++type) {
            (var5 = new Point()).y = y + type * 7;
            var5.x = x;
            var5.vx = -5;
            var5.frame = 3;
            var5.fRe = 5;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else if (type == 2) {
         for(type = 0; type < size; ++type) {
            (var5 = new Point()).y = y + type * 7;
            var5.x = x;
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
         if (type == 3) {
            for(type = 0; type < size; ++type) {
               (var5 = new Point()).y = y + type * 7;
               var5.x = x;
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

   private void createSkillZoro2(int type, int x, int y, int size) {
      Point var5;
      if (type == 0) {
         for(type = 0; type < 2; ++type) {
            (var5 = new Point()).y = y;
            if (super.Dir == 2) {
               var5.x = x + type * 15;
            } else {
               var5.x = x - type * 15;
            }

            var5.vy = -7;
            var5.frame = 2;
            var5.fRe = 4;
            var5.dis = CRes.random(2) == 0 ? 0 : 2;
            this.VecEff.addElement(var5);
         }

      } else {
         if (type == 1) {
            for(type = 0; type < 2; ++type) {
               (var5 = new Point()).y = y + type * 15;
               var5.x = x;
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

   private void paintKurobi_2(mGraphics var1) {
      if (super.f >= 15 && super.f <= 20) {
         super.fraImgEff.drawFrame((super.f - 11) / 3, super.objFireMain.x + this.x1000, super.objFireMain.y + this.y1000, super.Dir, 3, var1);
      } else {
         if (super.f >= 25 && super.f <= 30) {
            super.fraImgEff.drawFrame((super.f - 25) / 3, super.objFireMain.x + this.x1000, super.objFireMain.y + this.y1000 + 10, super.Dir, 3, var1);
         }

      }
   }

   private void paintDonKrieg_3(mGraphics var1) {
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

   private void paintDonKrieg_2(mGraphics var1) {
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

   private void paintDonKrieg_1(mGraphics var1) {
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
         this.paint_Bullet(var1, super.fraImgSubEff, var4.frame, var4.x, var4.y);
      }

   }

   private void paintBuggy_2(mGraphics var1) {
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

   private void paintLuffy_New3(mGraphics var1) {
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

   private void paint_Luffy_S3_L7(mGraphics var1) {
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

   private void paintLuffy_New2(mGraphics var1) {
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

   private void paintLuffy_New2_SHORT(mGraphics var1) {
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

   private void paint_Luffy_S2_L7(mGraphics var1) {
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

   private void paintSanji_3(mGraphics var1) {
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

   private void paint_Sanji_S3_L7(mGraphics var1) {
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

   private void paintGalio_1(mGraphics var1) {
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

   
   public void addSound(int soundId) {
      this.addSound((byte)soundId);
   }



	private static FrameImage s_nikaEff1;
	private static FrameImage s_nikaEff2;

	private void createNikaJump(int variant)
	{
		super.fraImgEff = (s_nikaEff1 != null ? s_nikaEff1 : (s_nikaEff1 = new FrameImage(356, 40, 80)));
		super.fraImgSubEff = (s_nikaEff2 != null ? s_nikaEff2 : (s_nikaEff2 = new FrameImage(183, 20, 54)));
		if (this.objBeFireMain != null)
		{
			super.toY = this.objBeFireMain.y;
			super.toX = this.objBeFireMain.x;
		}
		super.fRemove = (variant == NIKA_VARIANT_ACTIVE_2) ? 52 : 30;
		super.step = 0;

		if (variant == NIKA_VARIANT_ACTIVE_2)
		{
			GameScreen.addEffectEnd(30, 0, super.x, super.y, 250, super.Dir, super.objMainEff);
		}
	}

	private void paintNikaJump(mGraphics g)
	{
		if (this.checkNullObject(1))
		{
			return;
		}

		if (super.f >= 3 && super.f <= 6 && super.fraImgSubEff != null && super.fraImgSubEff.nFrame > 0 && super.objFireMain != null)
		{
			super.fraImgSubEff.drawFrame(0, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, g);
		}

		if (super.f >= 10 && super.f <= 20 && super.fraImgEff != null && super.fraImgEff.nFrame > 0 && super.objFireMain != null)
		{
			super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y - super.objFireMain.dy, 0, 33, g);
		}
	}

	private void updateNikaJump(int variant)
	{
		if (this.checkNullObject(3))
		{
			if (super.objFireMain != null)
			{
				super.objFireMain.dy = 0;
				super.objFireMain.isTanHinh = false;
			}
			this.finishNikaEffect();
			return;
		}

		if (super.f >= 0 && super.f <= 8)
		{
			super.objFireMain.dy += 60;
			if (super.f == 4)
			{
				this.addSound(51);
			}
		}

		boolean activeSkill2 = variant == NIKA_VARIANT_ACTIVE_2;
		boolean level5 = variant == NIKA_VARIANT_ACTIVE_1_LEVEL5;

		if (activeSkill2 && super.f == 0 && super.step == 0)
		{
			GameScreen.addHightDataeff(NIKA_DATA_ACTIVE_2, super.objFireMain.x, super.objFireMain.y);
			super.step = 1;
		}
		else if (!activeSkill2 && super.f == 0 && super.step == 0)
		{
			if (this.objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_START : NIKA_DATA_LEVEL1_START, this.objBeFireMain.x, this.objBeFireMain.y);
			}
			super.step = 1;
		}
		else if (!activeSkill2 && super.f == 5 && super.step == 1)
		{
			if (this.objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_JUMP : NIKA_DATA_LEVEL1_JUMP, this.objBeFireMain.x, this.objBeFireMain.y);
			}
			super.step = 2;
		}

		if (super.f == 9)
		{
			super.objFireMain.dy = 480;
		}

		if (super.f >= 10 && super.f <= 20 && super.objFireMain.dy >= 0)
		{
			super.objFireMain.dy -= 60;
		}

		if (super.f == 12 && this.objBeFireMain != null && MainObject.getDistance(super.objFireMain.x, super.objFireMain.y, this.objBeFireMain.x, this.objBeFireMain.y) < 260)
		{
			super.objFireMain.x = this.objBeFireMain.x;
			super.objFireMain.y = this.objBeFireMain.y + 5;
		}

		if (super.f == 21)
		{
			super.objFireMain.dy = 0;
			this.addSound(5);
			if (this.objBeFireMain != null)
			{
				this.setAva(1, this.objBeFireMain);
				GameScreen.addEffectEnd(148, 0, this.objBeFireMain.x, this.objBeFireMain.y, super.Dir, super.objMainEff);
				GameScreen.addEffectEnd(45, 0, this.objBeFireMain.x, this.objBeFireMain.y + 25, super.Dir, super.objMainEff);
			}
			LoadMap.timeVibrateScreen = CRes.random(2, 6);
		}

		if (!activeSkill2 && super.f == 25 && super.step == 2)
		{
			if (this.objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_LANDING_LEFT : NIKA_DATA_LEVEL1_LANDING_LEFT, this.objBeFireMain.x - 30, this.objBeFireMain.y);
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_LANDING_RIGHT : NIKA_DATA_LEVEL1_LANDING_RIGHT, this.objBeFireMain.x + 30, this.objBeFireMain.y);
			}
			super.step = 3;
		}

		if (super.f > super.fRemove)
		{
			if (super.objFireMain != null)
			{
				super.objFireMain.dy = 0;
				super.objFireMain.isTanHinh = false;
			}
			this.finishNikaEffect();
		}
	}

	private void createNikaBuff()
	{
		super.fRemove = 2;
		super.levelPaint = 1;
	}

	private void updateNikaBuff()
	{
		if (super.f == 0 && !this.checkNullObject(1))
		{
			super.objFireMain.addDataEff(NIKA_DATA_BUFF, 25000, 0, 0);
			this.addSound(30);
		}

		if (super.f > super.fRemove)
		{
			this.finishNikaEffect();
		}
	}

	private void createLightActive1Level5()
	{
		if (super.objFireMain != null)
		{
			super.objFireMain.addDataEff(LIGHT_LEVEL5_CAST, 0, (byte)0, (byte)0);
		}

		this.VecSubEff.removeAllElements();
		int maxTravelFrame = 0;

		if (super.vecObjsBeFire != null)
		{
			for (int i = 0; i < super.vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (targetInfo == null)
				{
					continue;
				}

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				Point_Focus projectile = new Point_Focus(0, 0);
				projectile.objMain = target;
				projectile.Dir = super.Dir;
				projectile.x = super.x;
				projectile.y = super.y - 25;
				int targetX = target == null ? super.toX : target.x;
				int targetY = target == null ? super.toY : target.y - (target.hOne / 2);
				int distance = CRes.abs(targetX - projectile.x);
				int travelFrame = distance / 20;
				if (travelFrame < 1)
				{
					travelFrame = 1;
				}

				projectile.fRe = travelFrame;
				projectile.f = 0;
				projectile.vy = (targetY - projectile.y) / travelFrame;
				this.VecSubEff.addElement(projectile);
				if (travelFrame > maxTravelFrame)
				{
					maxTravelFrame = travelFrame;
				}
			}
		}

		super.fRemove = (short)(maxTravelFrame + 20);
	}

	private void updateLightActive1Level5()
	{
		for (int i = 0; i < this.VecSubEff.size(); i++)
		{
			Point_Focus projectile = (Point_Focus)this.VecSubEff.elementAt(i);
			if (projectile == null) continue;
			projectile.x += projectile.Dir == 2 ? 20 : -20;
			projectile.y += projectile.vy;
			GameScreen.addHightDataeff(
				LIGHT_LEVEL5_PROJECTILE,
				projectile.x,
				projectile.y
			);
			projectile.f++;

			if (projectile.f >= projectile.fRe)
			{
				int impactX = projectile.objMain == null ? projectile.x : projectile.objMain.x;
				int impactY = projectile.objMain == null ? projectile.y : projectile.objMain.y - (projectile.objMain.hOne / 2);
				GameScreen.addHightDataeff(LIGHT_LEVEL5_IMPACT, impactX, impactY);
				LoadMap.timeVibrateScreen = CRes.random(6, 15);
				this.VecSubEff.removeElement(projectile);
				i--;
			}
		}

		if (super.f >= super.fRemove || this.VecSubEff.size() == 0)
		{
			this.removeEff();
		}
	}

	private void createLightActive2Level5()
	{
		int nFrame = 5;
		super.mframe = new int[nFrame];
		super.mframe[0] = 0;

		for (short i = 1; i < nFrame; i++)
		{
			DataSkillEff data = new DataSkillEff(
				(short)(LIGHT_LEVEL5_ACTIVE_2_START + i - 1),
				0
			);
			super.mframe[i] = (data.sequence != null ? data.sequence.length : 10) + super.mframe[i - 1] + 1;
		}

		this.VecSubEff.removeAllElements();
		if (super.vecObjsBeFire != null)
		{
			for (int i = 0; i < super.vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (targetInfo == null)
				{
					continue;
				}

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				if (target != null)
				{
					Point_Focus sequence = new Point_Focus(0, 0);
					sequence.objMain = target;
					sequence.dis = (short)i;
					sequence.f = (short)(-i * 5);
					sequence.fRe = (short)super.mframe[nFrame - 1];
					this.VecSubEff.addElement(sequence);
				}
			}
		}
		super.fRemove = (short)(super.mframe[nFrame - 1] + 20);
	}

	private void updateLightActive2Level5()
	{
		if (super.mframe == null)
		{
			this.removeEff();
			return;
		}
		int nFrame = super.mframe.length;
		for (int i = 0; i < this.VecSubEff.size(); i++)
		{
			Point_Focus sequence = (Point_Focus)this.VecSubEff.elementAt(i);
			if (sequence == null || sequence.objMain == null)
			{
				this.VecSubEff.removeElement(sequence);
				i--;
				continue;
			}

			for (short frameIndex = 0; frameIndex < nFrame - 1; frameIndex++)
			{
				if (sequence.f == super.mframe[frameIndex])
				{
					GameScreen.addHightDataeff(
						(short)(LIGHT_LEVEL5_ACTIVE_2_START + frameIndex),
						sequence.objMain.x,
						sequence.objMain.y
					);
					if (frameIndex == 1)
					{
						GameScreen.addEffectEnd(
							110,
							0,
							sequence.objMain.x + CRes.random_Am_0(15),
							sequence.objMain.y + CRes.random_Am_0(5),
							super.Dir,
							sequence.objMain
						);
					}
					if (frameIndex == 1 || frameIndex == 3)
					{
						GameScreen.addEffectEnd(
							112,
							0,
							sequence.objMain.x,
							sequence.objMain.y,
							super.Dir,
							sequence.objMain
						);
						LoadMap.timeVibrateScreen = (frameIndex == 1 ? CRes.random(1, 5) : CRes.random(6, 20));
					}
				}
			}

			if (sequence.f > super.mframe[1])
			{
				if (sequence.f % 3 == 0)
				{
					GameScreen.addEffectEnd(
						108,
						5,
						sequence.objMain.x + CRes.random_Am_0(10),
						sequence.objMain.y - CRes.random(240),
						super.Dir,
						sequence.objMain
					);
					this.addSound(17);
				}

				if (sequence.f > super.mframe[nFrame - 2] + 4)
				{
					GameScreen.addEffectEnd(
						108,
						5,
						sequence.objMain.x + CRes.random_Am_0(60),
						sequence.objMain.y - CRes.random(30),
						super.Dir,
						sequence.objMain
					);
				}
			}

			sequence.f++;
			if (sequence.f >= sequence.fRe)
			{
				this.VecSubEff.removeElement(sequence);
				i--;
			}
		}

		if (this.VecSubEff.size() == 0 || (super.fRemove > 0 && super.f >= super.fRemove))
		{
			this.removeEff();
		}
	}

	private void createLoveActive2Level5()
	{
		int nFrame = 5;
		super.mframe = new int[nFrame];
		super.mframe[0] = 0;
		for (short i = 1; i < nFrame; i++)
		{
			DataSkillEff data = new DataSkillEff((short)(30 + i - 1), 0);
			super.mframe[i] = (data.sequence != null ? data.sequence.length : 10) + super.mframe[i - 1] + 1;
		}
		super.fRemove = (short)(super.mframe[nFrame - 1] + 10);
	}

	private void updateLoveActive2Level5()
	{
		if (super.objFireMain == null || super.f > super.fRemove)
		{
			this.removeEff();
			return;
		}

		if (super.mframe != null)
		{
			for (short i = 0; i < super.mframe.length; i++)
			{
				if (super.f == super.mframe[i])
				{
					super.objFireMain.addDataEff((short)(30 + i), 0, (byte)0, (byte)0);
					if (i == super.mframe.length - 1)
					{
						super.objFireMain.addDataEff(
							LOVE_LEVEL5_FINISH_ATTACHED,
							0,
							(byte)0,
							(byte)0
						);
						if (this.objBeFireMain != null)
						{
							GameScreen.addHightDataeff(
								LOVE_LEVEL5_FINISH_IMPACT,
								this.objBeFireMain.x,
								this.objBeFireMain.y
							);
						}
					}
				}
			}
		}
	}

	private void finishNikaEffect()
	{
		this.removeEff();
	}

	private void createSkillBuff(short timeBuff)
	{
		super.fRemove = 2;
		super.levelPaint = 1;
	}

	private void updateSkillBuff()
	{
		if (super.f == 0 && !this.checkNullObject(1))
		{
			super.objFireMain.addDataEff((short)35, (int)super.timeBegin, (byte)0, (byte)0);
			this.addSoundBuffShort();
		}
		if (super.f > super.fRemove)
		{
			this.finishNikaEffect();
		}
	}

	private void createNikyuActive1()
	{
		if (super.objFireMain != null)
		{
			super.objFireMain.addDataEff(NIKYU_PROJECTILE, 0, (byte)0, (byte)0);
		}

		this.VecSubEff.removeAllElements();
		int maxTravelFrame = 0;

		if (super.vecObjsBeFire != null)
		{
			for (int i = 0; i < super.vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (targetInfo == null) continue;

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				Point_Focus projectile = new Point_Focus(0, 0);
				projectile.objMain = target;
				projectile.Dir = super.Dir;
				projectile.x = super.x;
				projectile.y = super.y - 25;
				int targetX = target == null ? super.toX : target.x;
				int targetY = target == null ? super.toY : target.y - (target.hOne / 2);
				int distance = CRes.abs(targetX - projectile.x);
				int travelSpeed = 24;
				int travelFrame = distance / travelSpeed;
				if (travelFrame < 1) travelFrame = 1;

				projectile.fRe = travelFrame;
				projectile.f = 0;
				projectile.vy = (targetY - projectile.y) / travelFrame;
				this.VecSubEff.addElement(projectile);
				if (travelFrame > maxTravelFrame) maxTravelFrame = travelFrame;
			}
		}
		super.fRemove = (short)(maxTravelFrame + 25);
	}

	private void updateNikyuActive1()
	{
		for (int i = 0; i < this.VecSubEff.size(); i++)
		{
			Point_Focus projectile = (Point_Focus)this.VecSubEff.elementAt(i);
			projectile.x += projectile.Dir == 2 ? 24 : -24;
			projectile.y += projectile.vy;
			GameScreen.addHightDataeff(
				NIKYU_PROJECTILE,
				projectile.x,
				projectile.y,
				projectile.Dir == 2
			);
			projectile.f++;

			if (projectile.f >= projectile.fRe)
			{
				int impactX = projectile.objMain == null ? projectile.x : projectile.objMain.x;
				int impactY = projectile.objMain == null ? projectile.y : projectile.objMain.y - (projectile.objMain.hOne / 2);
				GameScreen.addHightDataeff(NIKYU_IMPACT, impactX, impactY, projectile.Dir == 2);
				LoadMap.timeVibrateScreen = CRes.random(8, 18);
				this.VecSubEff.removeElement(projectile);
				i--;
			}
		}

		if (super.f >= super.fRemove && this.VecSubEff.size() == 0)
		{
			this.removeEff();
		}
	}

	private void createNikyuActive2()
	{
		if (super.objFireMain != null)
		{
			GameScreen.addHightDataeff(NIKYU_DASH, super.objFireMain.x, super.objFireMain.y, super.Dir == 2);
		}

		this.VecSubEff.removeAllElements();
		if (super.vecObjsBeFire != null)
		{
			for (int i = 0; i < super.vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (targetInfo == null) continue;

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				if (target != null)
				{
					Point_Focus repulsion = new Point_Focus(0, 0);
					repulsion.objMain = target;
					repulsion.Dir = super.Dir;
					repulsion.f = 0;
					repulsion.fRe = 24;
					this.VecSubEff.addElement(repulsion);
				}
			}
		}
		super.fRemove = 30;
	}

	private void updateNikyuActive2()
	{
		if (super.f == 2)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point_Focus repulsion = (Point_Focus)this.VecSubEff.elementAt(i);
				if (repulsion.objMain != null)
				{
					GameScreen.addHightDataeff(NIKYU_REPULSION, repulsion.objMain.x, repulsion.objMain.y, repulsion.Dir == 2);
					LoadMap.timeVibrateScreen = CRes.random(10, 20);
					int pushDist = (repulsion.Dir == 2) ? 100 : -100;
					repulsion.objMain.x += pushDist;
				}
			}
		}

		if (super.f >= super.fRemove)
		{
			this.removeEff();
		}
	}

	private void createNikyuBuff()
	{
		super.fRemove = 2;
		super.levelPaint = 1;
	}

	private void updateNikyuBuff()
	{
		if (super.f == 0 && !this.checkNullObject(1))
		{
			super.objFireMain.addDataEff(NIKYU_BUFF_EFF, (int)super.timeBegin, (byte)0, (byte)0);
			this.addSoundBuffShort();
		}
		if (super.f > super.fRemove)
		{
			this.finishNikaEffect();
		}
	}

	private static FrameImage[][] s_ttFrames;

	private static void ensureThanTrangFrames()
	{
		if (s_ttFrames != null) return;
		s_ttFrames = new FrameImage[17][];
		s_ttFrames[1] = new FrameImage[] {
			new FrameImage(101, 40, 47), new FrameImage(240, 30, 73, 1), new FrameImage(183, 3), new FrameImage(239, 40, 40), new FrameImage(406, 30, 30)
		};
		s_ttFrames[2] = new FrameImage[] {
			// 4002 Đại Phún Hỏa Volcano _X (Akainu Magma Eruption) — 10 Authentic Fire & Magma Textures
			new FrameImage(271, 130, 80, 3),          // [0] 271: Đại Phún Hỏa Cự Quyền / Colossal Magma Wave Fist (130x80, 3f)
			new FrameImage(336, 74, 30, 3),           // [1] 336: Dòng Dung Nham Cánh Tay / Magma Arm Stream (74x30, 3f)
			new FrameImage(254, 30, 40),              // [2] 254: Phản Lực Hỏa Diễm / Fiery Jet Trail (30x40, 1f)
			new FrameImage(252, 62, 64, 4),           // [3] 252: Vụ Nổ Dung Nham Bộc Phát / Magma Impact Blast (62x64, 4f)
			new FrameImage(238, 30, 73),              // [4] 238: Cột Nham Thạch Phun Trào / Molten Magma Pillar (30x73, 1f)
			new FrameImage(240, 30, 73, 1),           // [5] 240: Cột Lửa Vút Trời Thẳng Đứng / Rising Flame Column (30x73, 1f)
			new FrameImage(239, 38, 22),              // [6] 239: Hồ Dung Nham Sôi Sùng Sục / Boiling Magma Pool (38x22, 1f)
			new FrameImage(246, 49, 21, 4),           // [7] 246: Vết Rạn Nứt Núi Lửa Phun Trào / Volcanic Ground Fissure (49x21, 4f)
			new FrameImage(78, 22, 28, 5),            // [8] 78: Hạt Tàn Lửa Đỏ Li Ti / Crimson Sparks (22x28, 5f)
			new FrameImage(272, 50, 24)               // [9] 272: Vết Cháy Sém Mặt Đất / Scorched Earth Ground Shadow (50x24, 1f)
		};
		s_ttFrames[3] = new FrameImage[] {
			new FrameImage(37, 31, 74),          // [0] 37: Cột Băng Đao / Colossal Ice Pillar (31x74)
			new FrameImage(40, 63, 20),          // [1] 40: Thềm Băng Mặt Đất / Permafrost Ground Patch (63x20)
			new FrameImage(41, 40, 40),          // [2] 41: Hoa Tuyết & Bụi Băng Tinh Thể / Diamond Snow Crystal (40x40)
			new FrameImage(43, 84, 110),         // [3] 43: Đại Băng Trĩ / Glacial Avalanche Wave (84x110)
			new FrameImage(89, 28, 44),          // [4] 89: Gai Băng Nhọn Tủa / Jagged Ground Icicles (28x44)
			new FrameImage(46, 70, 100, 49, 70), // [5] 46: Hàn Băng Xung Kích / Glacial Shockwave (70x100)
			new FrameImage(39, 53, 28),          // [6] 39: Khối Băng Vĩnh Cửu / Eternal Permafrost Block (53x28)
			new FrameImage(41, 40, 40),          // [7] 41: Hoa Tuyết & Kim Cương Băng Tinh (40x40)
			new FrameImage(152, 25, 21)          // [8] 152: Khói Bụi Sương Lạnh / Sub-Zero Frost Fog (25x21)
		};
		s_ttFrames[4] = new FrameImage[] {
			new FrameImage(255, 42, 50, 3), new FrameImage(254, 30, 40)
		};
		s_ttFrames[5] = new FrameImage[] {
			// 4005 Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity) — 14 Master VFX Assets
			new FrameImage(480, 120, 100, 8),        // [0] 480: Caster Void Aura (120x100, 8f, anchor 33)
			new FrameImage(481, 240, 180, 8),        // [1] 481: Singularity Gate Cast (240x180, 8f, anchor 33)
			new FrameImage(482, 180,  80, 8),        // [2] 482: Void Wave Projectile (180x80, 8f, anchor 3)
			new FrameImage(483, 240, 240, 10),       // [3] 483: Colossal Black Hole Vortex (240x240, 10f, anchor 3)
			new FrameImage(484, 280, 200, 12),       // [4] 484: Cataclysmic Singularity Collapse (280x200, 12f, anchor 3)
			new FrameImage(485,  90, 140, 8),        // [5] 485: Target Void Spire Particles (90x140, 8f, anchor 33)
			new FrameImage(272,  50,  24),           // [6] 272: Vết Cháy Sém / Hồ Hư Vô Mặt Đất (50x24)
			new FrameImage(246,  49,  21, 4),        // [7] 246: Vết Rạn Nứt Hư Vô Địa Chấn (49x21, 4f)
			new FrameImage(285, 111,  90),           // [8] 285: Sóng Không Gian Biến Dạng Hư Vô (111x90, 3f)
			new FrameImage(394, 126,  41),           // [9] 394: Vành Đai Trọng Lực Sóng Xung Kích (126x41, 3f)
			new FrameImage(104,  30,  30),           // [10] 104: Chớp Sao Bụi Hắc Ám / Dark Starburst Sparks (30x30, 3f)
			new FrameImage(152,  25,  21),           // [11] 152: Khói Bụi Va Chạm Mặt Đất (25x21)
			new FrameImage(92,   64, 126, 45, 89, 1),// [12] 92: Sét Hư Vô Tím Đen / Cosmic Void Lightning (64x126)
			new FrameImage(175,  40,  40)            // [13] 175: Vòng Nén Trọng Lực Hư Vô (40x40)
		};
		s_ttFrames[6] = new FrameImage[] {
			// 4006 Enel 200M Volt El Thor — 8 Pure Authentic Lightning Assets
			new FrameImage(243, 36, 39),          // [0] 243: Cầu lôi tụ điện (36x39) — High-voltage plasma sphere
			new FrameImage(244, 20, 37, 3),       // [1] 244: Sét chéo dội trần (20x37, 3f) — Sky diagonal thunderbolt
			new FrameImage(240, 30, 73, 1),       // [2] 240: Cột sét dọc (30x73, 1f) — Vertical lightning bolt column
			new FrameImage(241, 40, 27, 2),       // [3] 241: Điện quang mặt đất (40x27, 2f) — Ground spiderweb crackle
			new FrameImage(242, 49, 28, 2),       // [4] 242: Vành đai điện xả (49x28, 2f) — Ground discharge ring
			new FrameImage(104, 30, 30),          // [5] 104: Chớp sao bùng nổ hồ quang (30x30) — Starburst spark flash
			new FrameImage(152, 25, 21),          // [6] 152: Khói bụi tiếp đất (25x21) — Ground impact dust
			new FrameImage(92, 64, 126, 45, 89, 1)// [7] 92:  Cung sét khổng lồ / Hồ quang cao thế (64x126, 1f)
		};
		s_ttFrames[7] = new FrameImage[] {
			new FrameImage(310, 73, 59), new FrameImage(312, 121, 77)
		};
		s_ttFrames[8] = new FrameImage[] {
			// 4008 ROOM Gamma Knife (Law - Phẫu thuật Ope Ope no Mi)
			new FrameImage(393, 1), // [0] 393: ROOM sphere quanh nhân vật (1 frame 110x110)
			new FrameImage(394, 3), // [1] 394: Vòng sáng chân nhân vật (3 frame dọc chuẩn)
			new FrameImage(391, 1), // [2] 391: Hiệu ứng nửa trên mục tiêu dính (1 frame 28x13)
			new FrameImage(392, 3), // [3] 392: Vòng sáng chân mục tiêu dính (3 frame dọc chuẩn tương tự 394)
			new FrameImage(358, 3)  // [4] 358: Vết chém nhỏ ngẫu nhiên ở mục tiêu (3 frame dọc 51x22)
		};
		s_ttFrames[9] = new FrameImage[] {
			// 4009 Eustass Kid: Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun)
			new FrameImage(243, 36, 39),        // [0] Lõi plasma từ trường quay cuồng (36x39)
			new FrameImage(92, 40, 40),         // [1] Tia sét hồ quang điện từ (40x40)
			new FrameImage(104, 30, 30),        // [2] Tia lửa ma sát kim loại & điểm nổ (30x30)
			new FrameImage(175, 40, 40),        // [3] Vòng sóng nén từ trường (40x40)
			new FrameImage(152, 25, 21),        // [4] Khói bụi va chạm mặt đất (25x21)
			new FrameImage(240, 30, 73, 1),     // [5] Cột năng lượng ánh sáng thẳng đứng (30x73, 1f)
			new FrameImage(238, 110, 50),       // [6] Sóng chấn địa chấn (110x50)
			new FrameImage(402, 120, 60),       // [7] Luồng phản lực Railgun (120x60)
			new FrameImage(358, 51, 22),        // [8] Mảnh kim loại & tia chém (51x22)
			new FrameImage(272, 50, 24)         // [9] Vòng định vị mục tiêu mặt đất (50x24)
		};
		s_ttFrames[10] = new FrameImage[] {
			// 4010 Cổ Độc Phán Quyết Venom _X — texIDs 467-473 (Cinematic VFX)
			new FrameImage(467, 8),
			new FrameImage(468, 11),
			new FrameImage(469, 6),
			new FrameImage(470, 13),
			new FrameImage(471, 8),
			new FrameImage(472, 8),
			new FrameImage(473, 4)
		};
		s_ttFrames[11] = new FrameImage[] {
			new FrameImage(266, 80, 100, 64, 80, 2), new FrameImage(254, 30, 40)
		};
		s_ttFrames[12] = new FrameImage[] {
			// 4012 Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) — 12 Authentic VFX Assets
			new FrameImage(474, 120, 100, 8),  // [0] Lam Hỏa Caster Aura (120x100, 8f, anchor 33)
			new FrameImage(475, 240, 180, 8),  // [1] Phượng Hoàng Thức Tỉnh Cast (240x180, 8f, anchor 33)
			new FrameImage(476, 180,  80, 8),  // [2] Phượng Hoàng Phi Thiên Projectile (180x80, 8f, anchor 3)
			new FrameImage(477, 240, 240, 10), // [3] Lam Hỏa Đại Bộc Phá _X Impact (240x240, 10f, anchor 3)
			new FrameImage(478, 280, 200, 12), // [4] Tung Cánh Phượng Hoàng Finisher AOE (280x200, 12f, anchor 33)
			new FrameImage(479,  90, 140, 8),  // [5] Cột Lam Hỏa Thiêu Đốt Particles (90x140, 8f, anchor 33)
			new FrameImage(243, 36, 39),         // [6] Swirling Solar Blue Flame Core (36x39)
			new FrameImage(242, 49, 28, 2),      // [7] Ground Flame Shockwave Ring (49x28, 2f)
			new FrameImage(241, 40, 27, 2),      // [8] Radiating Blue Ground Fire Sparks (40x27, 2f)
			new FrameImage(224, 22, 28, 5),      // [9] Sacred Rebirth Feathers & Embers (22x28, 5f)
			new FrameImage(272, 50, 24),         // [10] Ground Tracking Shadow (50x24)
			new FrameImage(104, 30, 30)          // [11] Starburst Blue Sparks & Flash (30x30, 3f)
		};
		s_ttFrames[13] = new FrameImage[] {
			// 4013 Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu Golden Shockwave) — 15 Authentic Textures
			new FrameImage(416, 78, 40),              // [0] 416: Kim Cương Phật Chưởng / Palm Thrust Wave (78x40, 4f)
			new FrameImage(171, 153, 84),             // [1] 171: Sóng Xung Kích Hoàng Kim / Traveling Shockwave Ring (153x84, 4f)
			new FrameImage(453, 169, 126),            // [2] 453: Đại Bộc Phá Cực Đại / Colossal Mega Shockwave (169x126, 3f)
			new FrameImage(394, 126, 41),             // [3] 394: Vành Đai Địa Chấn / Ground Shockwave Ring (126x41, 3f)
			new FrameImage(357, 100, 100, 2),         // [4] 357: Pháp Luân Kim Quang / Sacred Dharma Wheel Nimbus (100x100, 4f)
			new FrameImage(315, 77, 54, 3),           // [5] 315: Khai Hoa Kim Liên / Divine Lotus Bloom (77x54, 6f)
			new FrameImage(174, 40, 40, 4),           // [6] 174: Thái Dương Quang Cầu / Condensed Palm Energy Core (40x40, 8f)
			new FrameImage(335, 80, 80, 2),           // [7] 335: Phật Chưởng Bộc Phá / Palm Blast Burst Cone (80x80, 10f)
			new FrameImage(300, 80, 25, 3),           // [8] 300: Địa Chấn Thổ Bụi / Seismic Dust Upheaval (80x25, 9f)
			new FrameImage(246, 49, 21, 4),           // [9] 246: Vết Rạn Nứt Địa Chấn / Ground Seismic Fissure Crevasse (49x21, 8f)
			new FrameImage(285, 111, 90),             // [10] 285: Sóng Không Gian Biến Dạng / Radial Distortion Wave (111x90, 3f)
			new FrameImage(66, 75, 55),               // [11] 66: Lõi Bạch Kim Thiểm Quang / Divine White-Gold Core Flash (75x55, 1f)
			new FrameImage(104, 30, 30),              // [12] 104: Chớp Sao Kim Cương / Diamond Starburst Sparks (30x30, 3f)
			new FrameImage(267, 47, 53),              // [13] 267: Kim Sắc Hộ Thể Linh Khí / Golden Transformation Aura (47x53, 1f)
			new FrameImage(224, 22, 28, 5)            // [14] 224: Hạt Kim Quang Linh Khí / Sacred Floating Nirvana Embers (22x28, 20f)
		};
		s_ttFrames[14] = new FrameImage[] {
			new FrameImage(291, 47, 48), new FrameImage(295, 34, 24)
		};
		s_ttFrames[15] = new FrameImage[] {
			new FrameImage(101, 40, 47), new FrameImage(240, 30, 73, 1)
		};
		s_ttFrames[16] = new FrameImage[] {
			new FrameImage(404, 40, 40), new FrameImage(408, 30, 30), new FrameImage(447, 40, 40), new FrameImage(456, 30, 73, 1),
			new FrameImage(254, 30, 40), new FrameImage(247, 40, 20), new FrameImage(285, 30, 30), new FrameImage(108, 30, 30),
			new FrameImage(100, 20, 20), new FrameImage(224, 22, 28), new FrameImage(152, 25, 21), new FrameImage(272, 30, 30)
		};
	}

	private void createThanTrangSkill(int typeEff)
	{
		this.VecSubEff.removeAllElements();
		int setId = (typeEff == 4017 || typeEff == 4010) ? 10 : ((typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : ((typeEff >= 4201 && typeEff <= 4216) ? (typeEff - 4200) : ((typeEff >= 4501 && typeEff <= 4516) ? (typeEff - 4500) : (((typeEff - 4001) / 5) + 1))));
		if (setId < 1 || setId > 16) setId = 1;
		ensureThanTrangFrames();
		FrameImage[] arr = s_ttFrames[setId];
		if (arr != null)
		{
			if (arr.length > 0) super.fraImgEff = arr[0];
			if (arr.length > 1) super.fraImgSubEff = arr[1];
			if (arr.length > 2) super.fraImgSub2Eff = arr[2];
			if (arr.length > 3) super.fraImgSub3Eff = arr[3];
			if (arr.length > 4) super.fraImgSub4Eff = arr[4];
			if (arr.length > 5) super.fraImgSub5Eff = arr[5];
			if (arr.length > 6) super.fraImgSub6Eff = arr[6];
		}

		// Play Sound Effect on cast
		switch (setId)
		{
			case 1:
				this.addSound(5);
				this.addSound(51);
				break;
			case 7:
				this.addSound(51);
				this.addSound(14);
				break;
			case 2:
				this.addSound(5);
				this.addSound(51);
				break;
			case 8:
				this.addSound(10);
				this.addSound(18);
				break;
			case 12: this.addSound(10); break;
			case 15: this.addSound(5); break;
			case 10: case 14: this.addSound(14); break;
			case 3: this.addSound(2); break;
			case 4: case 11: case 13: this.addSound(10); break;
			case 6: case 9: this.addSound(18); break;
			case 5: this.addSound(4); break;
			default: this.addSound(5); break;
		}

		// Ground casting ripple & celestial array under caster
		if (super.objFireMain != null)
		{
			switch (setId)
			{
				case 1:
					GameScreen.addHightDataeff(33, super.x, super.y);
					GameScreen.addEffectEnd(63, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(175, 0, super.x, super.y - 15, (byte)super.Dir, super.objFireMain);
					break;
				case 2:
					GameScreen.addHightDataeff(33, super.x, super.y);
					GameScreen.addEffectEnd(111, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(63, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(112, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					break;
				case 7:
					GameScreen.addEffectEnd(133, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(110, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(92, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addHightDataeff(33, super.x, super.y);
					break;
				case 8:
					break;
				case 15: GameScreen.addEffectEnd(63, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 3: GameScreen.addEffectEnd(35, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 4: GameScreen.addEffectEnd(175, 0, super.x, super.y - 20, (byte)super.Dir, super.objFireMain); break;
				case 13: GameScreen.addHightDataeff(33, super.x, super.y); break;
				case 5: GameScreen.addEffectEnd(108, 7, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 6: GameScreen.addEffectEnd(40, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 9: GameScreen.addEffectEnd(92, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 10: GameScreen.addEffectEnd(108, 7, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 11: GameScreen.addEffectEnd(175, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
				case 12:
					this.marcoWaveHitMask = 0;
					GameScreen.addEffectEnd(63, 0, super.x, super.y, (byte)super.Dir, super.objFireMain);
					GameScreen.addEffectEnd(175, 0, super.x, super.y - 15, (byte)super.Dir, super.objFireMain);
					break;
				case 14: GameScreen.addEffectEnd(50, 0, super.x, super.y, (byte)super.Dir, super.objFireMain); break;
			}
		}

		int maxTravelFrame = 0;
		if (setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				for (int i = 0; i < super.vecObjsBeFire.size(); i++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
					if (object_Effect_Skill != null)
					{
						MainObject target = MainObject.get_Object((int)object_Effect_Skill.ID, (byte)object_Effect_Skill.tem);
						Point_Focus point_Focus = new Point_Focus(0, 0);
						point_Focus.objMain = target;
						point_Focus.Dir = (byte)super.Dir;
						point_Focus.x = super.x;
						point_Focus.y = super.y - ((super.objFireMain != null) ? (super.objFireMain.hOne / 2) : 20);
						int targetX = (target == null) ? super.toX : target.x;
						int targetY = (target == null) ? super.toY : (target.y - target.hOne / 2);
						int distance = Math.abs(targetX - point_Focus.x);
						int travelSpeed = 22;
						int travelFrame = distance / travelSpeed;
						if (travelFrame < 1)
						{
							travelFrame = 1;
						}
						point_Focus.fRe = travelFrame;
						point_Focus.f = 0;
						point_Focus.vy = (targetY - point_Focus.y) / travelFrame;
						point_Focus.toX = targetX;
						point_Focus.toY = targetY;
						this.VecSubEff.addElement(point_Focus);
						if (travelFrame > maxTravelFrame)
						{
							maxTravelFrame = travelFrame;
						}
					}
				}
			}
			else
			{
				Point_Focus point_Focus = new Point_Focus(0, 0);
				point_Focus.objMain = null;
				point_Focus.Dir = (byte)super.Dir;
				point_Focus.x = super.x;
				point_Focus.y = super.y - ((super.objFireMain != null) ? (super.objFireMain.hOne / 2) : 20);
				int targetX = super.x + ((super.Dir == 2) ? 160 : -160);
				int targetY = super.y - 20;
				int travelFrame = 7;
				point_Focus.fRe = travelFrame;
				point_Focus.f = 0;
				point_Focus.vy = (targetY - point_Focus.y) / travelFrame;
				point_Focus.toX = targetX;
				point_Focus.toY = targetY;
				this.VecSubEff.addElement(point_Focus);
				maxTravelFrame = travelFrame;
			}
		}

		if (setId == 2)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			int facingSign = (super.Dir == 2) ? 1 : -1;

			int centerX = super.toX;
			int centerY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						centerX = t0.x;
						centerY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (centerX == 0 && centerY == 0)
			{
				centerX = casterX + facingSign * 160;
				centerY = casterY - 15;
			}

			// Clamp distance to range = 220
			int dist = Math.abs(centerX - casterX);
			if (dist > 220)
			{
				centerX = casterX + facingSign * 220;
			}
			super.toX = centerX;
			super.toY = centerY;

			// Khởi tạo 12 hạt tàn lửa nham thạch bắn tung tóe quanh tâm chấn
			for (int i = 0; i < 12; i++)
			{
				Point ep = new Point();
				int angle = (i * 30) % 360;
				int speed = 3 + (i % 4);
				ep.x = centerX;
				ep.y = centerY - 10;
				ep.vx = (CRes.getcos(angle) * speed) >> 10;
				ep.vy = -(3 + (i % 4));
				ep.f = 0;
				ep.fRe = 24;
				ep.color = (i % 2 == 0) ? 0 : 2;
				this.VecSubEff.addElement(ep);
			}

			super.fRemove = 70;
		}
		else if (setId == 3)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int centerX = super.toX;
			int centerY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill t0 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (t0 != null)
				{
					MainObject target = MainObject.get_Object((int)t0.ID, (byte)t0.tem);
					if (target != null)
					{
						centerX = target.x;
						centerY = target.y;
					}
				}
			}
			if (centerX == 0 && centerY == 0)
			{
				centerX = super.x + (super.Dir == 2 ? 140 : -140);
				centerY = super.y - 15;
			}
			super.toX = centerX;
			super.toY = centerY;

			// 1. SEVEN COLOSSAL PERMAFROST PILLARS & 6 INTERMEDIATE JAGGED SPIKES
			// Final Unified Structure ("cuối vẫn ra 1 hình"):
			// 7 Colossal Ice Spires (dis = max height) & 6 Ground Icicles forming the majestic Glacial Peak
			int pattern = CRes.random(6);
			int[] spireDelays = new int[7];
			int[] spikeDelays = new int[6];
			int[] jitterX = new int[7];
			int[] jitterY = new int[7];

			switch (pattern)
			{
				case 0: // Pattern 0: Băng Tâm Bộc Phát (Epicenter Bloom / Inside-Out Surge)
					spireDelays[0] = 14; // Center pinnacle erupts first!
					spireDelays[1] = 16; spireDelays[2] = 17; // Inner flank
					spireDelays[3] = 19; spireDelays[4] = 20; // Mid flank
					spireDelays[5] = 22; spireDelays[6] = 23; // Outer perimeter
					spikeDelays[4] = 15; spikeDelays[5] = 15;
					spikeDelays[0] = 18; spikeDelays[1] = 18;
					spikeDelays[2] = 21; spikeDelays[3] = 21;
					break;

				case 1: // Pattern 1: Gọng Kìm Băng Phổ (Glacial Pincer / Outside-In Converge)
					spireDelays[5] = 14; spireDelays[6] = 14; // Outer flanks seal perimeter first!
					spireDelays[3] = 17; spireDelays[4] = 17; // Mid flank
					spireDelays[1] = 20; spireDelays[2] = 20; // Inner flank
					spireDelays[0] = 23; // Grand center pinnacle climax!
					spikeDelays[2] = 15; spikeDelays[3] = 15;
					spikeDelays[0] = 18; spikeDelays[1] = 18;
					spikeDelays[4] = 21; spikeDelays[5] = 21;
					break;

				case 2: // Pattern 2: Hàn Băng Thần Triều Thuận (Tidal Avalanche Wave Left-to-Right Surge)
					spireDelays[5] = 14; // Left Outer
					spireDelays[3] = 16; // Left Mid
					spireDelays[1] = 18; // Left Inner
					spireDelays[0] = 20; // Center
					spireDelays[2] = 22; // Right Inner
					spireDelays[4] = 24; // Right Mid
					spireDelays[6] = 26; // Right Outer
					spikeDelays[2] = 15; spikeDelays[0] = 17; spikeDelays[4] = 19;
					spikeDelays[5] = 21; spikeDelays[1] = 23; spikeDelays[3] = 25;
					break;

				case 3: // Pattern 3: Hàn Băng Thần Triều Nghịch (Tidal Avalanche Wave Right-to-Left Surge)
					spireDelays[6] = 14; // Right Outer
					spireDelays[4] = 16; // Right Mid
					spireDelays[2] = 18; // Right Inner
					spireDelays[0] = 20; // Center
					spireDelays[1] = 22; // Left Inner
					spireDelays[3] = 24; // Left Mid
					spireDelays[5] = 26; // Left Outer
					spikeDelays[3] = 15; spikeDelays[1] = 17; spikeDelays[5] = 19;
					spikeDelays[4] = 21; spikeDelays[0] = 23; spikeDelays[2] = 25;
					break;

				case 4: // Pattern 4: Hàn Băng Tinh Khắc Đan Chéo (Criss-Cross Zigzag Eruption)
					spireDelays[5] = 14; // Left Outer
					spireDelays[6] = 15; // Right Outer
					spireDelays[4] = 17; // Right Mid
					spireDelays[3] = 18; // Left Mid
					spireDelays[1] = 20; // Left Inner
					spireDelays[2] = 21; // Right Inner
					spireDelays[0] = 23; // Center Pinnacle Climax
					spikeDelays[2] = 15; spikeDelays[3] = 16;
					spikeDelays[1] = 18; spikeDelays[0] = 19;
					spikeDelays[4] = 21; spikeDelays[5] = 22;
					break;

				default: // Pattern 5: Băng Tách Hỗn Mang Tự Nhiên (Organic Shuffled Fracture with Natural Micro-Jitter)
					int[] baseDelays = { 14, 15, 17, 18, 20, 22, 23 };
					for (int s = 6; s > 0; s--)
					{
						int r = CRes.random(s + 1);
						int tmp = baseDelays[s];
						baseDelays[s] = baseDelays[r];
						baseDelays[r] = tmp;
					}
					for (int s = 0; s < 7; s++)
					{
						spireDelays[s] = baseDelays[s];
						jitterX[s] = CRes.random_Am_0(5);
						jitterY[s] = CRes.random_Am_0(3);
					}
					for (int k = 0; k < 6; k++)
					{
						spikeDelays[k] = 15 + CRes.random(8);
					}
					break;
			}

			Point p0 = new Point(); p0.x = centerX + jitterX[0]; p0.y = centerY + jitterY[0]; p0.f = 0; p0.fSmall = spireDelays[0]; p0.subType = 0; p0.dis = 135; p0.color = 0; this.VecEff.addElement(p0);
			Point p1 = new Point(); p1.x = centerX - 42 + jitterX[1]; p1.y = centerY - 5 + jitterY[1]; p1.f = 0; p1.fSmall = spireDelays[1]; p1.subType = 0; p1.dis = 110; p1.color = 2; this.VecEff.addElement(p1);
			Point p2 = new Point(); p2.x = centerX + 42 + jitterX[2]; p2.y = centerY - 4 + jitterY[2]; p2.f = 0; p2.fSmall = spireDelays[2]; p2.subType = 0; p2.dis = 110; p2.color = 0; this.VecEff.addElement(p2);
			Point p3 = new Point(); p3.x = centerX - 85 + jitterX[3]; p3.y = centerY + 2 + jitterY[3]; p3.f = 0; p3.fSmall = spireDelays[3]; p3.subType = 0; p3.dis = 88; p3.color = 2; this.VecEff.addElement(p3);
			Point p4 = new Point(); p4.x = centerX + 85 + jitterX[4]; p4.y = centerY + 3 + jitterY[4]; p4.f = 0; p4.fSmall = spireDelays[4]; p4.subType = 0; p4.dis = 88; p4.color = 0; this.VecEff.addElement(p4);
			Point p5 = new Point(); p5.x = centerX - 135 + jitterX[5]; p5.y = centerY + 6 + jitterY[5]; p5.f = 0; p5.fSmall = spireDelays[5]; p5.subType = 0; p5.dis = 68; p5.color = 2; this.VecEff.addElement(p5);
			Point p6 = new Point(); p6.x = centerX + 135 + jitterX[6]; p6.y = centerY + 8 + jitterY[6]; p6.f = 0; p6.fSmall = spireDelays[6]; p6.subType = 0; p6.dis = 68; p6.color = 0; this.VecEff.addElement(p6);

			int[][] subOffsets = {
				{-65, 10},
				{65, 12},
				{-110, -8},
				{110, -6},
				{-20, 14},
				{20, 15}
			};
			for (int i = 0; i < subOffsets.length; i++)
			{
				Point sp = new Point();
				sp.x = centerX + subOffsets[i][0];
				sp.y = centerY + subOffsets[i][1];
				sp.f = 0; sp.fSmall = spikeDelays[i]; sp.subType = 1; sp.dis = 40; sp.color = (i % 2 == 0) ? 0 : 2;
				this.VecEff.addElement(sp);
			}

			// 2. 24 BLIZZARD VORTEX PARTICLES
			for (int b = 0; b < 24; b++)
			{
				Point bp = new Point();
				bp.x = centerX;
				bp.y = centerY;
				bp.frame = b * 15;
				bp.dis = 30 + (b * 5);
				bp.fSmall = b;
				bp.subType = (b % 3 == 0) ? 1 : 0;
				this.VecSubEff.addElement(bp);
			}

			super.fRemove = 65;
		}
		else if (setId == 5)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			int facingSign = (super.Dir == 2) ? 1 : -1;

			int impactX = super.toX;
			int impactY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill t0 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (t0 != null)
				{
					MainObject target = MainObject.get_Object((int)t0.ID, (byte)t0.tem);
					if (target != null)
					{
						impactX = target.x;
						impactY = target.y;
					}
				}
			}
			if (impactX == 0 && impactY == 0)
			{
				impactX = casterX + facingSign * 160;
				impactY = casterY;
			}
			super.toX = impactX;
			super.toY = impactY;

			// Hướng mặt Caster về phía mục tiêu và vào pose tụ lực hắc ám
			if (super.objFireMain != null)
			{
				super.Dir = (byte)((impactX >= casterX) ? 2 : 0);
				super.objFireMain.type_left_right = super.Dir;
				super.objFireMain.Dir = super.Dir;
				super.objFireMain.Action = 2; // Windup tụ năng lượng Hắc Ám
				super.objFireMain.f = 0;
			}

			// Khởi tạo 16 hạt vật chất tối xoay quanh đĩa bồi tụ chân trời sự kiện (this.VecSubEff)
			for (int b = 0; b < 16; b++)
			{
				Point bp = new Point();
				bp.x = impactX;
				bp.y = impactY;
				bp.frame = b * 22;                  // Góc xoay quỹ đạo ban đầu
				bp.dis = 45 + (b * 6);              // Bán kính xoay (45px -> 140px)
				bp.fSmall = b;                      // Seed nhấp nháy
				bp.subType = (b % 3 == 0) ? 1 : 0;  // 0 = điểm sáng tím, 1 = đốm sao hắc ám
				bp.color = (b % 2 == 0) ? 0x9C27B0 : 0x4A148C; // Màu tím hư vô / tím đậm
				this.VecSubEff.addElement(bp);
			}

			// Khởi tạo 6 điểm nứt hư vô mặt đất quanh tâm chấn (this.VecEff)
			int[][] riftOffsets = {
				{ 0, 4 }, { -45, 2 }, { 45, 3 }, { -85, 5 }, { 85, 4 }, { 0, -10 }
			};
			for (int r = 0; r < riftOffsets.length; r++)
			{
				Point rp = new Point();
				rp.x = impactX + riftOffsets[r][0];
				rp.y = impactY + riftOffsets[r][1];
				rp.f = 0;
				rp.fSmall = 16 + r * 3; // Delay xuất hiện rạn nứt
				rp.subType = (r % 2 == 0) ? 0 : 2; // Lật hình ngẫu nhiên
				this.VecEff.addElement(rp);
			}

			this.addSound((byte)10);
			super.fRemove = 76;
		}
		else if (setId == 7)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			int centerX = super.toX;
			int centerY = super.toY;
			if (centerX == 0 && centerY == 0)
			{
				centerX = super.x + (super.Dir == 2 ? 120 : -120);
				centerY = super.y - 20;
			}

			// 1. SCREEN-SPACE SPATIAL SHATTER NODES (subType = 2: Pinned to viewport screen percentage)
			// Center Screen Master Fracture
			Point sc0 = new Point(); sc0.x = 50; sc0.y = 45; sc0.f = 0; sc0.fSmall = 2; sc0.subType = 2; sc0.color = 1; this.VecEff.addElement(sc0);
			// Staggered screen-space fractures across the viewport
			Point sc1 = new Point(); sc1.x = 24; sc1.y = 28; sc1.f = 0; sc1.fSmall = 8; sc1.subType = 2; sc1.color = 0; this.VecEff.addElement(sc1);
			Point sc2 = new Point(); sc2.x = 76; sc2.y = 25; sc2.f = 0; sc2.fSmall = 12; sc2.subType = 2; sc2.color = 1; this.VecEff.addElement(sc2);
			Point sc3 = new Point(); sc3.x = 18; sc3.y = 72; sc3.f = 0; sc3.fSmall = 18; sc3.subType = 2; sc3.color = 1; this.VecEff.addElement(sc3);
			Point sc4 = new Point(); sc4.x = 82; sc4.y = 74; sc4.f = 0; sc4.fSmall = 22; sc4.subType = 2; sc4.color = 0; this.VecEff.addElement(sc4);

			// 2. WIDELY SPACED & ORGANIC RANDOMIZED GROUND SEISMIC RUPTURE NODES (subType = 0 / 1)
			// Guaranteed non-overlapping spread across wide battlefield range (-220px to +220px)
			int[][] seismicOffsets = new int[][] {
				new int[] { 0, 5, 4, 1 },
				new int[] { -55, -8, 8, 0 },
				new int[] { 60, -12, 10, 1 },
				new int[] { -115, 12, 16, 1 },
				new int[] { 125, 8, 20, 0 },
				new int[] { -175, -5, 26, 0 },
				new int[] { 185, -10, 30, 1 },
				new int[] { -225, 10, 36, 1 },
				new int[] { 230, 14, 40, 0 }
			};

			for (int i = 0; i < seismicOffsets.length; i++)
			{
				Point p = new Point();
				p.x = centerX + seismicOffsets[i][0] + CRes.random_Am_0(12);
				p.y = centerY + 15 + seismicOffsets[i][1] + CRes.random_Am_0(6);
				p.f = 0;
				p.fSmall = seismicOffsets[i][2];
				p.subType = seismicOffsets[i][3];
				p.color = (i % 2);
				this.VecEff.addElement(p);
			}

			// 3. TARGET-ANCHORED SHATTER NODES (Only for valid living targets, spaced out)
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
						{
							Point tn = new Point();
							tn.x = tObj.x;
							tn.y = tObj.y - (tObj.hOne / 2);
							tn.f = 0;
							tn.fSmall = 12 + k * 5;
							tn.subType = (k % 2);
							tn.color = 1;
							this.VecEff.addElement(tn);
						}
					}
				}
			}
			super.fRemove = Math.max(maxTravelFrame + 75, 85);
		}
		else if (setId == 6)
		{
			// Enel 200M Volt El Thor — Full Procedural Lightning Matrix & Random Aerial Teleport
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			int facingSign = (super.Dir == 2) ? 1 : -1;

			this.enelOrigX = casterX;
			this.enelOrigY = casterY;
			if (super.objFireMain != null && super.objFireMain == GameScreen.player)
			{
				Player.isBlock = true;
			}

			// Epicenter determination
			int impactX = super.toX;
			int impactY = super.toY;
			int nTargets = (super.vecObjsBeFire != null) ? super.vecObjsBeFire.size() : 0;
			if (nTargets > 0)
			{
				int sumX = 0;
				int sumY = 0;
				int validCount = 0;
				for (int k = 0; k < nTargets; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject t = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (t != null && !t.isDie && t.Hp > 0)
						{
							sumX += t.x;
							sumY += t.y;
							validCount++;
						}
					}
				}
				if (validCount > 0)
				{
					impactX = sumX / validCount;
					impactY = sumY / validCount;
				}
			}
			if (impactX == 0 && impactY == 0)
			{
				impactX = casterX + facingSign * 140;
				impactY = casterY;
			}
			super.toX = impactX;
			super.toY = impactY;

			// ─── 1. MATRIX NODES GENERATION (Stored in this.VecEff) ───
			// Node 0: Epicenter Master Node (subType = 1: Colossal 200M Volt El Thor Beam, strike at f = 36)
			Point nodeCenter = new Point();
			nodeCenter.x = impactX;
			nodeCenter.y = impactY;
			nodeCenter.f = 0;
			nodeCenter.fSmall = 36;
			nodeCenter.subType = 1;
			nodeCenter.color = 0;
			nodeCenter.dis = 120;
			this.VecEff.addElement(nodeCenter);

			// Nodes 1..N: Locked Target Nodes (subType = 0: Cascading Sky Bolts)
			for (int i = 0; i < nTargets; i++)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (tInfo != null)
				{
					MainObject t = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (t != null && !t.isDie)
					{
						Point pTarget = new Point();
						pTarget.x = t.x;
						pTarget.y = t.y;
						pTarget.f = 0;
						pTarget.fSmall = 16 + i * 4; // Staggered: 16, 20, 24, 28, 32
						pTarget.subType = 0;
						pTarget.color = (i % 2 == 0 ? 0 : 2);
						pTarget.dis = 30;
						this.VecEff.addElement(pTarget);
					}
				}
			}

			// Nodes: Inner Geometric Matrix Ring (6 Nodes at r = 65px, staggered strikes)
			int rInner = 65;
			for (int a = 0; a < 6; a++)
			{
				int angle = a * 60 + 15;
				Point pInner = new Point();
				pInner.x = impactX + (rInner * CRes.getcos(angle) >> 10);
				pInner.y = impactY + ((rInner * 3 / 5) * CRes.getsin(angle) >> 10);
				pInner.f = 0;
				pInner.fSmall = 14 + (a * 3); // 14, 17, 20, 23, 26, 29
				pInner.subType = 0;
				pInner.color = (a % 2 == 0 ? 0 : 2);
				pInner.dis = 45;
				this.VecEff.addElement(pInner);
			}

			// Nodes: Outer Perimeter Grid Nodes (6 Nodes at r = 150px)
			int rOuter = 150;
			for (int b = 0; b < 6; b++)
			{
				int angle = b * 60 + 45;
				Point pOuter = new Point();
				int perturbR = rOuter + CRes.random_Am_0(15);
				pOuter.x = impactX + (perturbR * CRes.getcos(angle) >> 10);
				pOuter.y = impactY + ((perturbR * 3 / 5) * CRes.getsin(angle) >> 10);
				pOuter.f = 0;
				pOuter.fSmall = 22 + (b * 4); // 22, 26, 30, 34, 38, 42
				pOuter.subType = (b % 2 == 0 ? 0 : 2);
				pOuter.color = (b % 2 == 0 ? 2 : 0);
				pOuter.dis = 60;
				this.VecEff.addElement(pOuter);
			}

			// Nodes: Residual Overdrive Aftershock Nodes (Landing at f = 46, 50, 54, 58)
			int[][] aftershockOffsets = new int[][] {
				new int[] { -55, -8, 46, 2, 0 },
				new int[] { 60, -10, 50, 2, 2 },
				new int[] { -85, 12, 54, 2, 0 },
				new int[] { 90, 8, 58, 2, 2 }
			};
			for (int s = 0; s < aftershockOffsets.length; s++)
			{
				Point pAfter = new Point();
				pAfter.x = impactX + aftershockOffsets[s][0];
				pAfter.y = impactY + aftershockOffsets[s][1];
				pAfter.f = 0;
				pAfter.fSmall = aftershockOffsets[s][2];
				pAfter.subType = aftershockOffsets[s][3];
				pAfter.color = aftershockOffsets[s][4];
				this.VecEff.addElement(pAfter);
			}

			// ─── 2. MATRIX INTERCONNECTION EDGES / LIGHTNING ARCS (Stored in this.VecSubEff) ───
			// Interconnect nodes to draw the glowing celestial lightning web / matrix lines!
			int totalNodes = this.VecEff.size();
			if (totalNodes >= 7)
			{
				// A. Center-to-Inner Spokes (Connecting Node 0 to Inner Ring Nodes)
				for (int i = 1; i <= 6 && i < totalNodes; i++)
				{
					Point targetNode = (Point)this.VecEff.elementAt(i);
					Point arc = new Point();
					arc.x = nodeCenter.x;
					arc.y = nodeCenter.y - 10;
					arc.x2 = targetNode.x;
					arc.y2 = targetNode.y;
					arc.fSmall = 10 + i * 2; // Active from f=12..38
					arc.fRe = 28; // Duration
					arc.subType = 0; // 0 = standard lightning arc
					arc.frame = i * 7; // jitter seed
					arc.color = (i % 2 == 0 ? 0x00B0FF : 0x33B5E5);
					this.VecSubEff.addElement(arc);
				}

				// B. Inner Ring Perimeter Polygon (Connecting adjacent inner nodes)
				for (int i = 1; i <= 6 && i < totalNodes; i++)
				{
					int nextIdx = (i == 6) ? 1 : (i + 1);
					Point n1 = (Point)this.VecEff.elementAt(i);
					Point n2 = (Point)this.VecEff.elementAt(nextIdx);
					Point arc = new Point();
					arc.x = n1.x;
					arc.y = n1.y;
					arc.x2 = n2.x;
					arc.y2 = n2.y;
					arc.fSmall = 12 + i * 2;
					arc.fRe = 26;
					arc.subType = 0;
					arc.frame = i * 11;
					arc.color = 0x80D8FF;
					this.VecSubEff.addElement(arc);
				}

				// C. Outer Ring Polygon & Spokes to Inner Ring
				for (int i = 7; i <= 12 && i < totalNodes; i++)
				{
					Point nOuter = (Point)this.VecEff.elementAt(i);
					int innerIdx = 1 + (i - 7) % 6;
					Point nInner = (Point)this.VecEff.elementAt(innerIdx);
					// Ray connecting inner to outer
					Point arcRay = new Point();
					arcRay.x = nInner.x;
					arcRay.y = nInner.y;
					arcRay.x2 = nOuter.x;
					arcRay.y2 = nOuter.y;
					arcRay.fSmall = 16 + (i - 7) * 3;
					arcRay.fRe = 24;
					arcRay.subType = 0;
					arcRay.frame = i * 13;
					arcRay.color = 0x00E5FF;
					this.VecSubEff.addElement(arcRay);

					// Outer loop edge
					int nextOuter = (i == 12) ? 7 : (i + 1);
					if (nextOuter < totalNodes)
					{
						Point nOuterNext = (Point)this.VecEff.elementAt(nextOuter);
						Point arcOuter = new Point();
						arcOuter.x = nOuter.x;
						arcOuter.y = nOuter.y;
						arcOuter.x2 = nOuterNext.x;
						arcOuter.y2 = nOuterNext.y;
						arcOuter.fSmall = 18 + (i - 7) * 2;
						arcOuter.fRe = 22;
						arcOuter.subType = 0;
						arcOuter.frame = i * 17;
						arcOuter.color = 0xB1EDFC;
						this.VecSubEff.addElement(arcOuter);
					}
				}

				// D. Sky-to-Ground Feeder Arcs (From high altitude stormcloud down to ground nodes)
				for (int sky = 0; sky < 5; sky++)
				{
					Point arcSky = new Point();
					arcSky.x = impactX - 120 + sky * 60 + CRes.random_Am_0(15);
					arcSky.y = impactY - 260; // High in the heavens
					int groundTargetIdx = (sky * 2) % totalNodes;
					Point groundNode = (Point)this.VecEff.elementAt(groundTargetIdx);
					arcSky.x2 = groundNode.x;
					arcSky.y2 = groundNode.y;
					arcSky.fSmall = 14 + sky * 4;
					arcSky.fRe = 26;
					arcSky.subType = 1; // 1 = Vertical Sky Lightning Feeder
					arcSky.frame = sky * 19;
					arcSky.color = 0xFFFFFF;
					this.VecSubEff.addElement(arcSky);
				}
			}

			super.fRemove = 78;
		}
		else if (setId == 8)
		{
			if (this.VecEff != null) this.VecEff.removeAllElements();
			if (this.VecSubEff != null) this.VecSubEff.removeAllElements();
			super.fRemove = 74;
		}
		else if (setId == 12)
		{
			// 4012 Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax)
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			this.phoenixOrigX = casterX;
			this.phoenixOrigY = casterY;

			int facingSign = (super.Dir == 2) ? 1 : -1;
			int targetX = super.toX;
			int targetY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 160;
				targetY = casterY - 15;
			}

			// Clamp distance to range = 220
			int dist = Math.abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.phoenixTargetX = targetX;
			this.phoenixTargetY = targetY;
			this.marcoWaveHitMask = 0;

			// Movement Lock: Không cho player di chuyển khi đang tung tuyệt chiêu
			if (super.objFireMain != null && super.objFireMain == GameScreen.player)
			{
				Player.isBlock = true;
				GameScreen.player.Action = 0;
				GameScreen.player.toX = GameScreen.player.x;
				GameScreen.player.toY = GameScreen.player.y;
			}

			// Sinh 16 hạt lông vũ linh thiêng & tàn lam hỏa (Sacred Feathers & Blue Embers)
			for (int i = 0; i < 16; i++)
			{
				Point p = new Point();
				p.x = casterX + CRes.random_Am_0(25);
				p.y = casterY - 10 + CRes.random_Am_0(20);
				p.vx = CRes.random_Am_0(3);
				p.vy = -(2 + CRes.random(4));
				p.f = 0;
				p.fRe = 20 + CRes.random(15);
				p.subType = (i % 2); // 0: feather (p12[9]), 1: spark (p12[8])
				p.color = (i % 2 == 0) ? 0 : 2;
				this.VecSubEff.addElement(p);
			}

			this.addSound(10);
			LoadMap.timeVibrateScreen = 6;
			super.fRemove = 68;
		}
		else if (setId == 13)
		{
			// 4013 Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu Golden Shockwave) - 44-tick 5-phase cinematic animation
			if (this.VecEff != null) this.VecEff.removeAllElements();
			if (this.VecSubEff != null) this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			this.daibutsuCasterX = casterX;
			this.daibutsuCasterY = casterY;

			int facingSign = (super.Dir == 2) ? 1 : -1;
			int targetX = super.toX;
			int targetY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 180;
				targetY = casterY - 15;
			}
			// Clamp to max skill range = 220
			int dist = Math.abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.daibutsuImpactX = targetX;
			this.daibutsuImpactY = targetY;

			super.fRemove = 44;
		}
		else if (setId == 9)
		{
			// 4009 Eustass Kid: Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun)
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			int facingSign = (super.Dir == 2) ? 1 : -1;

			// Root fixed casting coordinates
			super.x = casterX;
			super.y = casterY;
			if (super.objFireMain != null)
			{
				super.objFireMain.Action = 2; // Đổi pose gồng nhẹ khi bắt đầu tụ lực
				super.objFireMain.f = 0;
			}
			super.fRemove = 48;

			// Lock target coordinates (tối đa range = 220)
			int targetX = super.toX;
			int targetY = super.toY;
			if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 180;
				targetY = casterY - 15;
			}
			// Clamp range to 220px theo chuẩn skill data
			int dist = Math.abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.kidCannonImpactX = targetX;
			this.kidCannonImpactY = targetY;

			// Master Pool of 48 authentic metallic weapon icons in HTTH
			int[] baseWeaponPool = {
				// Swords & Sabers (11)
				2, 26, 50, 74, 98, 122, 146, 170, 194, 218, 257,
				// Daggers, Cleavers & Great Blades (11)
				3, 27, 51, 75, 99, 123, 147, 171, 195, 219, 252,
				// Staves, Spears, Tridents & Polearms (11)
				4, 28, 52, 76, 100, 124, 148, 172, 196, 220, 256,
				// Pistols, Rifles, Flintlocks & Cannons (11)
				5, 29, 53, 77, 101, 125, 149, 173, 197, 221, 269,
				// Iron Claws & Spiked Gauntlets (4)
				49, 169, 193, 255
			};

			// Shuffle using Fisher-Yates to guarantee 100% unique weapons on every cast (NO DUPLICATES)
			int[] shuffledPool = new int[baseWeaponPool.length];
			System.arraycopy(baseWeaponPool, 0, shuffledPool, 0, baseWeaponPool.length);
			for (int s = shuffledPool.length - 1; s > 0; s--)
			{
				int r = CRes.random(s + 1);
				int tmp = shuffledPool[s];
				shuffledPool[s] = shuffledPool[r];
				shuffledPool[r] = tmp;
			}

			// Scan nearby mobs & players within 220px to pull weapons from
			mVector nearbyEntities = new mVector();
			if (GameScreen.vecPlayers != null)
			{
				for (int k = 0; k < GameScreen.vecPlayers.size(); k++)
				{
					MainObject obj = (MainObject)GameScreen.vecPlayers.elementAt(k);
					if (obj != null && obj != super.objFireMain && !obj.isDie && obj.Hp > 0 && !obj.isRemove)
					{
						int d = MainObject.getDistance(casterX, casterY, obj.x, obj.y);
						if (d <= 220)
						{
							nearbyEntities.addElement(obj);
						}
					}
				}
			}

			int totalWeapons = 28;
			for (int i = 0; i < totalWeapons; i++)
			{
				Point p = new Point();
				p.subType = shuffledPool[i];

				if (i < nearbyEntities.size())
				{
					MainObject ent = (MainObject)nearbyEntities.elementAt(i);
					p.AZ = ent;
					p.x2 = ent.x + CRes.random_Am_0(10);
					p.y2 = ent.y - (ent.hOne / 2) + CRes.random_Am_0(8);
				}
				else
				{
					p.AZ = null;
					int pullAngle = (i * (360 / totalWeapons) + CRes.random_Am_0(16) + 360) % 360;
					int pullDist = 140 + CRes.random(0, 100);
					p.x2 = casterX + (pullDist * CRes.getcos(pullAngle)) / 1000;
					p.y2 = casterY + (pullDist * CRes.getsin(pullAngle)) / 1000;
				}

				p.x = p.x2;
				p.y = p.y2;

				// Relative offsets in cannon assembly formation (Twin-Rail Damned Punk Great Cannon)
				// 0..7: Upper Rail, 8..15: Lower Rail, 16..21: Breech & Coils, 22..27: Muzzle Converters
				int rx = 0;
				int ry = 0;
				int depth = 0;
				if (i < 8)
				{
					// Upper Magnetic Rail
					rx = facingSign * (-10 + i * 7);
					ry = -14 + (i % 2) * 2;
					depth = 1;
				}
				else if (i < 16)
				{
					// Lower Magnetic Rail
					int k = i - 8;
					rx = facingSign * (-10 + k * 7);
					ry = 14 - (k % 2) * 2;
					depth = 0;
				}
				else if (i < 22)
				{
					// Breech & Magnetic Accelerator Coils
					int k = i - 16;
					rx = facingSign * (-22 + (k % 3) * 8);
					ry = -8 + (k / 3) * 16;
					depth = (k % 2 == 0) ? 0 : 1;
				}
				else
				{
					// Muzzle Converters & Rail Stabilizers
					int k = i - 22;
					rx = facingSign * (36 + (k % 2) * 8);
					ry = -10 + k * 4;
					depth = 1;
				}

				p.AK = rx;
				p.AL = ry;
				p.color = depth; // 0 = back layer, 1 = front layer
				p.frame = CRes.random(8);

				p.fSmall = CRes.random(0, 4); // Takeoff delay
				p.fRe = 10 + CRes.random(0, 4); // Flight duration: lands at cannon by frame 10..14

				p.f = (i + CRes.random(4)) % 4; // Flight style
				p.dis = 35 + CRes.random(0, 35); // Curvature amplitude

				// Dispersal explosion velocities (for Phase 5)
				int disperseAngle = (i * (360 / totalWeapons) + CRes.random_Am_0(20) + 360) % 360;
				int disperseSpeed = 16 + CRes.random(0, 16);
				p.vx = (disperseSpeed * CRes.getcos(disperseAngle)) / 1000;
				p.vy = (disperseSpeed * CRes.getsin(disperseAngle)) / 1000 - 4;

				this.VecEff.addElement(p);
			}

			this.addSound((byte)18);
		}
		else if (setId == 10)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();

			// 1. Ưu tiên tuyệt đối tìm mục tiêu chuẩn:
			MainObject target = null;
			if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != super.objFireMain)
			{
				target = GameScreen.objFocus;
			}
			else if (this.objBeFireMain != null && this.objBeFireMain != super.objFireMain && !this.objBeFireMain.isDie && !this.objBeFireMain.isRemove)
			{
				target = this.objBeFireMain;
			}
			else if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill t0 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (t0 != null)
					{
						MainObject mob = MainObject.get_Object((int)t0.ID, (byte)t0.tem);
						if (mob != null && !mob.isDie && !mob.isRemove && mob != super.objFireMain)
						{
							target = mob;
							break;
						}
					}
				}
			}

			int centerX = 0;
			int centerY = 0;
			if (target != null)
			{
				this.objBeFireMain = target;
				centerX = target.x;
				centerY = target.y;
				super.toX = centerX;
				super.toY = centerY;
				if (super.objFireMain != null)
				{
					super.Dir = (byte)((target.x >= super.objFireMain.x) ? 2 : 0);
					super.objFireMain.type_left_right = super.Dir;
					super.objFireMain.Dir = super.Dir;
				}
			}
			else if (super.toX != 0 || super.toY != 0)
			{
				centerX = super.toX;
				centerY = super.toY;
			}
			else
			{
				centerX = super.x + (super.Dir == 2 ? 140 : -140);
				centerY = super.y;
				super.toX = centerX;
				super.toY = centerY;
			}

			Point targetCenter = new Point();
			targetCenter.x = centerX;
			targetCenter.y = centerY;
			targetCenter.obj = target;
			this.VecEff.addElement(targetCenter);
			super.fRemove = 72;

			if (super.levelPaint >= 0)
			{
				Effect_Skill groundPool = new Effect_Skill();
				groundPool.typeEffect = typeEff;
				groundPool.levelPaint = -1; // ONTOP = 0: DƯỚI MỤC TIÊU & NHÂN VẬT
				groundPool.x = centerX;
				groundPool.y = centerY;
				groundPool.toX = centerX;
				groundPool.toY = centerY;
				groundPool.Dir = super.Dir;
				groundPool.objFireMain = super.objFireMain;
				groundPool.objBeFireMain = target;
				groundPool.vecObjsBeFire = super.vecObjsBeFire;
				groundPool.fRemove = 72;
				groundPool.f = 0;
				groundPool.subType = 1004;
				if (groundPool.VecEff == null) groundPool.VecEff = new mVector();
				groundPool.VecEff.addElement(new Point(centerX, centerY));
				GameScreen.VecEffect.addElement(groundPool);
			}
		}
		else
		{
			super.fRemove = maxTravelFrame + 25;
		}
	}

	private void updateThanTrangSkill(int typeEff)
	{
		int setId = (typeEff == 4017 || typeEff == 4010) ? 10 : ((typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : (((typeEff - 4001) / 5) + 1));
		if (setId < 1 || setId > 16) setId = 1;

		// Continuous Volcanic Screen Heat Vibration & Sound for Set 2 (Dung Nham - Volcano)
		if (setId == 2 && super.f <= super.fRemove)
		{
			if (super.f == 2)
			{
				this.addSound(5);
				LoadMap.timeVibrateScreen = 6;
			}
			else if (super.f == 12)
			{
				this.addSound(5);
			}
			else if (super.f == 24)
			{
				// VA CHẠM CỰC ĐẠI - IMPACT & VOLCANIC EXPLOSION!
				this.addSound(51);
				this.addSound(18);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 28;

				// 1 HIT DUY NHẤT (nKick = 1): Kích ứng quái trúng đòn giật lùi & chớp trắng
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo != null)
						{
							MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
							if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
							{
								this.setAva(2, target);
								target.dy = -8;
							}
						}
					}
				}
			}
			else if (super.f == 36)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 10;
			}
		}

		// Cập nhật vị trí hạt tàn lửa nham thạch (VecSubEff) sau va chạm (f >= 24)
		if (setId == 2)
		{
			if (super.f >= 24 && this.VecSubEff != null)
			{
				int impactY = super.toY;
				for (int k = 0; k < this.VecSubEff.size(); k++)
				{
					Point p = (Point)this.VecSubEff.elementAt(k);
					if (p != null)
					{
						p.f++;
						if (p.f < 20)
						{
							p.x += p.vx;
							p.y += p.vy;
							p.vy++;
							if (p.y > impactY + 12)
							{
								p.y = impactY + 12;
								p.vx = 0;
								p.vy = 0;
							}
						}
					}
				}
			}

			// Clean finish
			if (super.f >= super.fRemove)
			{
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo != null)
						{
							MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
							if (target != null) target.dy = 0;
						}
					}
				}
			}
		}

		// ─── setId 3: Kỷ Băng Hà Tuyệt Đối X (Aokiji Ice Age) ───
		if (setId == 3)
		{
			// 1. Screen Freezing Audio & Glacial Creak Milestones
			if (super.f == 4)
			{
				this.addSound(2);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (super.f == 14)
			{
				this.addSound(14);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 28;
			}
			else if (super.f == 28)
			{
				this.addSound(2);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 16;
			}
			else if (super.f == 42)
			{
				this.addSound(10);
				LoadMap.timeVibrateScreen = 10;
			}

			// 2. Target Freezing & Sub-Zero Damage Pulses (Hit at f = 14, 28, 42)
			if (super.f == 14 || super.f == 28 || super.f == 42)
			{
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;

						this.setAva(2, target);
						target.dy = 0;
						// Pure sub-zero ice burst at target feet (Effect 35: Jagged Ice 89)
						GameScreen.addEffectEnd((short)35, 0, target.x, target.y, (byte)0, null);
					}
				}
			}

			// 3. 24 Blizzard Snow Crystal Vortex Orbiting Simulation (this.VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point bp = (Point)this.VecSubEff.elementAt(i);
					if (bp != null)
					{
						bp.frame = (bp.frame + 12) % 360;
						if (super.f < 14)
						{
							bp.dis = Math.min(135, bp.dis + 7);
						}
						else if (super.f > 45)
						{
							bp.dis = Math.max(0, bp.dis - 5);
						}
					}
				}
			}

			// 4. Glacial Pillars & Icicles Eruption Age Advance (this.VecEff)
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point sp = (Point)this.VecEff.elementAt(i);
					if (sp != null)
					{
						sp.f++;
					}
				}
			}

			// 5. Clean up targets on end
			if (super.f >= super.fRemove && super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
					if (target != null) target.dy = 0;
				}
			}
		}

		// ─── setId 5: Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity) ───
		if (setId == 5)
		{
			int impactX = super.toX;
			int impactY = super.toY;

			// TUYỆT ĐỐI KHÔNG LÀM TỐI MAP VÀ TRỜI (Bản đồ và bầu trời giữ nguyên 100% tự nhiên)
			GameCanvas.gameScr.isFullScreen = false;

			// 1. Audio Milestones & Dynamic Screen Shake (chuẩn nhịp chiến đấu hoành tráng)
			if (super.f == 2)
			{
				this.addSound((byte)10); // Tiếng rền trầm hư vô tích tụ
				LoadMap.timeVibrateScreen = 4;
			}
			else if (super.f == 14)
			{
				// Caster xuất chiêu phóng hắc tinh cầu xé toạc không gian
				this.addSound((byte)4);
				this.addSound((byte)51);
				LoadMap.timeVibrateScreen = 8;
				if (super.objFireMain != null)
				{
					super.objFireMain.Action = 2;
					super.objFireMain.f = 0; // Đổi thế xuất chiêu oanh tạc
				}
			}
			else if (super.f == 22)
			{
				// Hố đen khai mở gầm vang
				this.addSound((byte)10);
				this.addSound((byte)51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (super.f == 34 || super.f == 42)
			{
				// Nhịp co thắt trọng lực
				this.addSound((byte)10);
				LoadMap.timeVibrateScreen = 6;
			}
			else if (super.f == 50)
			{
				// Vụ Nổ Đại Hư Vô Sụp Đổ Cực Đại & Giải Phóng
				this.addSound((byte)14);
				this.addSound((byte)51);
				this.addSound((byte)18);
				LoadMap.timeVibrateScreen = 28;
			}

			// 2. Caster Action Transition:
			// Sau khi giải phóng hắc tinh cầu (f >= 28), Caster tự do di chuyển chiến đấu bình thường
			if (super.objFireMain != null)
			{
				if (super.f == 28)
				{
					super.objFireMain.Action = 0;
					if (super.objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}

			// 3. Phản Ứng Chiến Đấu Của Mục Tiêu (Hiện mục tiêu bình thường, dính đòn rung giật tự nhiên)
			if (super.vecObjsBeFire != null)
			{
				// Các nhịp hút chấn thương (f = 22, 30, 38, 46)
				if (super.f == 22 || super.f == 30 || super.f == 38 || super.f == 46)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						this.setAva(2, target);
						target.dy = -4; // Nhịp giật sát thương nhẹ
					}
				}
				// Cú nổ sụp đổ hố đen cực đại tại f = 50: Giật lùi mạnh
				else if (super.f == 50)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						this.setAva(2, target);
						target.dy = -10; // Hất tung nhẹ khỏi mặt đất khi hố đen phát nổ
					}
				}
				// Hồi phục tiếp đất mượt mà sau khi bị hất tung
				else if (super.f > 50 && super.f <= 58)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target != null && target.dy < 0)
						{
							target.dy += 2;
						}
					}
				}
			}

			// 4. Cập nhật 16 Hạt Vật Chất Tối Xoay Xoắn Ốc Quỹ Đạo Hố Đen (this.VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point bp = (Point)this.VecSubEff.elementAt(i);
					if (bp != null)
					{
						int rotSpd = (super.f < 22) ? 8 : ((super.f < 48) ? 16 : 24);
						bp.frame = (bp.frame + rotSpd) % 360;
						if (super.f >= 20 && super.f <= 48)
						{
							// Xoáy logarithmic thu dần bán kính vào tâm hố đen
							if (bp.dis > 15) bp.dis -= 1;
						}
						else if (super.f > 50)
						{
							// Bắn tung tỏa ra ngoài khi hố đen sụp đổ
							bp.dis += 8;
						}
					}
				}
			}

			// 5. Cập nhật tiến trình rạn nứt mặt đất (this.VecEff)
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point rp = (Point)this.VecEff.elementAt(i);
					if (rp != null)
					{
						rp.f++;
					}
				}
			}

			// 6. Hoàn tất chiêu & Giải phóng an toàn
			if (super.f >= super.fRemove)
			{
				GameCanvas.gameScr.isFullScreen = false;
				if (super.objFireMain != null && super.objFireMain == GameScreen.player)
				{
					Player.isBlock = false;
				}
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null)
						{
							target.dy = 0;
							if (target == GameScreen.player)
							{
								Player.isBlock = false;
							}
						}
					}
				}
			}
		}

		// ─── setId 6: 200 Triệu Volt Thần Lôi (Enel El Thor) — Full Random Aerial Teleport, Flight & Matrix Update ───
		if (setId == 6)
		{
			int casterOrigX = this.enelOrigX;
			int casterOrigY = this.enelOrigY;
			int impactX = super.toX;
			int impactY = super.toY;
			int facingSign = (super.Dir == 2) ? 1 : -1;

			// 1. RANDOM AERIAL TELEPORT & LEVITATION FLIGHT CHOREOGRAPHY
			if (super.objFireMain != null)
			{
				if (super.f < 6)
				{
					// Ground charging stage
					super.objFireMain.dy = 0;
					super.objFireMain.isTanHinh = false;
				}
				else if (super.f == 6)
				{
					// ─── TELEPORT 1: Ground to Sky Ascent ───
					GameScreen.addEffectEnd(40, 0, super.objFireMain.x, super.objFireMain.y, (byte)0, null);
					GameScreen.addEffectEnd(42, 0, super.objFireMain.x, super.objFireMain.y, (byte)0, null);
					this.addSound(18);
					super.objFireMain.x = casterOrigX + facingSign * 50;
					super.objFireMain.dy = -50;
				}
				else if (super.f > 6 && super.f < 19)
				{
					// Aerial hovering 1 with electric tremor
					int tremor = (super.f % 2 == 0) ? -1 : 1;
					super.objFireMain.dy = -50 + tremor;
				}
				else if (super.f == 19)
				{
					// ─── TELEPORT 2: Aerial Lightning Blitz Dash ───
					GameScreen.addEffectEnd(40, 0, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, (byte)0, null);
					GameScreen.addEffectEnd(42, 0, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, (byte)0, null);
					this.addSound(18);
					// Teleport to random aerial vantage over targets
					super.objFireMain.x = impactX - facingSign * 55 + CRes.random_Am_0(25);
					super.objFireMain.dy = -60;
				}
				else if (super.f > 19 && super.f < 32)
				{
					// Aerial hovering 2 with matrix invocation
					int tremor = (super.f % 2 == 0) ? -1 : 1;
					super.objFireMain.dy = -60 + tremor;
				}
				else if (super.f == 32)
				{
					// ─── TELEPORT 3: Zenith Climax Apex (Over the epicenter) ───
					GameScreen.addEffectEnd(40, 0, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, (byte)0, null);
					GameScreen.addEffectEnd(42, 0, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, (byte)0, null);
					this.addSound(51);
					super.objFireMain.x = impactX;
					super.objFireMain.dy = -75;
				}
				else if (super.f > 32 && super.f < 48)
				{
					// Zenith levitation unleashing the 200M volt beam
					int tremor = (super.f % 2 == 0) ? -2 : 2;
					super.objFireMain.dy = -75 + tremor;
				}
				else if (super.f == 48)
				{
					// ─── TELEPORT 4: Flank Descending Overdrive ───
					GameScreen.addEffectEnd(40, 0, super.objFireMain.x, super.objFireMain.y + super.objFireMain.dy, (byte)0, null);
					super.objFireMain.x = impactX + facingSign * 70;
					super.objFireMain.dy = -38;
				}
				else if (super.f > 48 && super.f < 68)
				{
					// Gliding down smoothly
					int tDown = super.f - 48; // 0..20
					super.objFireMain.dy = -38 + (38 * tDown / 20);
				}
				else if (super.f >= 68)
				{
					// ─── TELEPORT 5: Return to Origin (Perfect Safety) ───
					super.objFireMain.x = casterOrigX;
					super.objFireMain.y = casterOrigY;
					super.objFireMain.dy = 0;
					super.objFireMain.isTanHinh = false;
					if (super.objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}

			// 2. AUDIO & SCREEN SHAKE MILESTONES
			if (super.f == 2)
			{
				this.addSound(10); // High-voltage charge hum
				LoadMap.timeVibrateScreen = 6;
			}
			else if (super.f == 14)
			{
				this.addSound(18); // Matrix activation
				LoadMap.timeVibrateScreen = 10;
			}
			else if (super.f == 26)
			{
				this.addSound(10);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 16;
			}
			else if (super.f == 36)
			{
				// CLIMAX CATACLYSM: 200M Volt El Thor mega impact!
				this.addSound(51);
				this.addSound(14);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 38; // Violent full-screen quake
			}
			else if (super.f == 46 || super.f == 54)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 12;
			}

			// 3. TARGET PARALYSIS, AIRBORNE POP & DAMAGE SYNCHRONIZATION
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
					if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;

					// Intermediate matrix shocks (f = 20, 28)
					if (super.f == 20 || super.f == 28)
					{
						this.setAva(2, target);
						GameScreen.addEffectEnd(40, 0, target.x, target.y - target.hOne / 2, (byte)0, null);
					}

					// Mega Climax Strike at f = 36: Popped high into air, paralyzed!
					if (super.f == 36)
					{
						this.setAva(2, target);
						target.dy = -24; // High pop
						GameScreen.addEffectEnd(40, 0, target.x, target.y, (byte)0, null);
						GameScreen.addEffectEnd(42, 0, target.x, target.y, (byte)0, null);
					}

					// Lateral paralyzed electric shaking while suspended (f = 37..50)
					if (super.f >= 37 && super.f <= 50)
					{
						int wobble = (super.f % 2 == 0) ? 3 : -3;
						target.x += wobble;
					}

					// Smooth gravity recovery
					if (super.f >= 48 && super.f <= 66)
					{
						if (target.dy < 0)
						{
							target.dy += 2;
						}
					}

					// Final cleanup
					if (super.f >= super.fRemove)
					{
						target.dy = 0;
					}
				}
			}

			// Skill lifecycle safety finish
			if (super.f >= super.fRemove)
			{
				if (super.objFireMain != null)
				{
					super.objFireMain.x = casterOrigX;
					super.objFireMain.y = casterOrigY;
					super.objFireMain.dy = 0;
					super.objFireMain.isTanHinh = false;
					if (super.objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}
		}

		// Smooth Camera Vibration & Audio Beats for Set 7 (Chấn Thiên)
		if (setId == 7 && super.f <= super.fRemove)
		{
			if (super.f == 4)
			{
				this.addSound(51);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 20;
			}
			else if (super.f == 16)
			{
				this.addSound(51);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 24;
			}
			else if (super.f == 28)
			{
				this.addSound(51);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 20;
			}
			else if (super.f == 42)
			{
				this.addSound(5);
				LoadMap.timeVibrateScreen = 14;
			}
		}

		// Multi-Pulse Target Damage & Flinch Reaction at Primary Seismic Waves
		if (setId == 7 && (super.f == 14 || super.f == 26 || super.f == 38 || super.f == 52))
		{
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
							GameScreen.addEffectEnd(134, (byte)(k % 2), target.x, target.y, (byte)super.Dir, super.objMainEff);
							GameScreen.addEffectEnd(110, 0, target.x, target.y, 0, null);
						}
					}
				}
			}
		}

		// Progressive Organic Seismic Ruptures Jumping Across Terrain
		if (setId == 7 && this.VecEff != null)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int screenW = GameCanvas.w > 0 ? GameCanvas.w : MotherCanvas.w;
			int screenH = GameCanvas.h > 0 ? GameCanvas.h : MotherCanvas.h;

			for (int k = 0; k < this.VecEff.size(); k++)
			{
				Point node = (Point)this.VecEff.elementAt(k);
				if (node != null)
				{
					node.f++;
					int age = node.f - node.fSmall;
					int posX = (node.subType == 2) ? (camX + screenW * node.x / 100) : node.x;
					int posY = (node.subType == 2) ? (camY + screenH * node.y / 100) : node.y;

					if (age == 1)
					{
						// Phase 1: Seismic ground cracking / shockwave ring
						if (node.subType == 2)
						{
							GameScreen.addEffectEnd(133, 0, posX, posY, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
						else
						{
							GameScreen.addEffectEnd(110, 0, posX, posY, 0, null);
							GameScreen.addEffectEnd(133, (byte)node.color, posX, posY + 4, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
					}
					else if (age == 10)
					{
						// Phase 2: Atmospheric spatial rupture & dust upheaval
						if (node.subType != 2)
						{
							GameScreen.addEffectEnd(134, 0, posX, posY - 5, 0, null);
							GameScreen.addEffectEnd(92, 0, posX, posY, 0, null);
							GameScreen.addEffectEnd(50, 0, posX, posY - 4, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
					}
				}
			}
		}

		// 1. Smooth Airborne Levitation for Set 8 (Law Takt: ascent 0→72px easing-out, hover, landing easing-in)
		if (setId == 8 && super.f <= 74)
		{
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target == null) continue;
						if (target.isDie || target.Hp <= 0 || target.isRemove)
						{
							target.dy = 0;
							continue;
						}
						int targetDy = 0;
						if (super.f >= 3 && super.f <= 22)
						{
							// Easing-out quadratic ascent: fast at start, slow near peak (0→72px over 20 frames)
							int t = super.f - 3; // 0..19
							int tScaled = (19 - t) * 100 / 19;
							int remaining = 100 - (tScaled * tScaled / 100);
							targetDy = 72 * remaining / 100;
						}
						else if (super.f > 22 && super.f <= 56)
						{
							targetDy = 72; // Suspended in air inside ROOM sphere
						}
						else if (super.f > 56 && super.f <= 70)
						{
							// Easing-in quadratic landing: slow at start, accelerates down (72→0px over 14 frames)
							int t = super.f - 56; // 0..14
							int tScaled = (14 - t) * 100 / 14;
							targetDy = 72 * tScaled * tScaled / 10000;
						}
						else
						{
							targetDy = 0;
						}
						target.dy = targetDy;
					}
				}
			}
		}

		// 2. Sound cues for Set 8
		if (setId == 8)
		{
			if (super.f == 2)
			{
				this.addSound(10);
			}
			else if (super.f == 14)
			{
				this.addSound(51);
			}
			else if (super.f == 30)
			{
				this.addSound(18); // Mid-ROOM ambient pulse
			}
		}

		// 3. Periodic Hit Feedback on Targets while levitating (super.f = 8..56 every 8 frames)
		if (setId == 8 && (super.f % 8 == 0) && super.f >= 8 && super.f <= 56)
		{
			this.addSound(18);
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
							int targetCenterY = target.y - target.dy - (target.hOne / 2);
							GameScreen.addEffectEnd(10, 0, target.x, targetCenterY, (byte)super.Dir, super.objMainEff);
						}
					}
				}
			}
		}

		// 4. Final Clean Reset for Set 8 on completion
		if (setId == 8 && super.f >= super.fRemove)
		{
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
						if (target != null)
						{
							target.dy = 0;
						}
					}
				}
			}
		}

		// ─── setId 12: Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) ───
		if (setId == 12)
		{
			// Caster state management
			if (super.objFireMain != null)
			{
				super.objFireMain.isTanHinh = false;
				super.objFireMain.dy = 0;
			}

			// Sound and screenshake cues
			if (super.f == 4)
			{
				this.addSound(18); // Tiếng ngọn lửa bùng phát
				LoadMap.timeVibrateScreen = 6;
			}
			else if (super.f == 16)
			{
				this.addSound(5);  // Tiếng phượng hoàng cất cánh rít gió
				this.addSound(51);
				LoadMap.timeVibrateScreen = 8;
			}
			else if (super.f == 32)
			{
				// ─── CLIMAX IMPACT X ───
				this.addSound(14); // Tiếng nổ oanh tạc cực lớn
				this.addSound(51);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 32; // Rung màn hình cực đại 32 tick

				// Spawn các vụ nổ và hiệu ứng phụ tại tâm chấn
				GameScreen.addEffectEnd(118, 0, this.phoenixTargetX, this.phoenixTargetY, (byte)0, null);
				GameScreen.addEffectEnd(54, 0, this.phoenixTargetX, this.phoenixTargetY - 10, (byte)0, null);
				GameScreen.addEffectEnd(104, 0, this.phoenixTargetX, this.phoenixTargetY - 25, (byte)0, null);

				// Kích nổ lan ra nTarget = 5 mục tiêu, nảy lên không trung dy = -18
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						if (Math.abs(target.x - this.phoenixTargetX) <= 140)
						{
							this.setAva(2, target);
							target.dy = -18; // Pop airborne
							GameScreen.addEffectEnd(118, 0, target.x, target.y - 20, (byte)0, null);
						}
					}
				}
			}
			else if (super.f == 40)
			{
				// Finisher Wings Sweep Sound
				this.addSound(10);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (super.f == 46)
			{
				this.addSound(18); // Tiếng lam hỏa thiêu đốt bốc lên dưới chân mục tiêu
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						if (Math.abs(target.x - this.phoenixTargetX) <= 140)
						{
							this.setAva(2, target); // Giật chớp trắng lần 2
							GameScreen.addEffectEnd(54, 0, target.x, target.y - 10, (byte)0, null);
						}
					}
				}
			}

			// Target gravity recovery after pop (f = 42..62)
			if (super.f >= 42 && super.f <= 62)
			{
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null && target.dy < 0)
						{
							target.dy += 3;
							if (target.dy > 0) target.dy = 0;
						}
					}
				}
			}

			// Update floating particles (VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point p = (Point)this.VecSubEff.elementAt(i);
					if (p != null)
					{
						p.f++;
						p.x += p.vx;
						p.y += p.vy;
						if (p.f % 4 == 0)
						{
							p.vx = CRes.random_Am_0(2);
						}
						if (p.f >= p.fRe)
						{
							// Tái sinh hạt ở quanh tâm chấn nếu f >= 32
							if (super.f >= 32 && super.f < 60)
							{
								p.x = this.phoenixTargetX + CRes.random_Am_0(70);
								p.y = this.phoenixTargetY - 10 + CRes.random_Am_0(30);
								p.vx = CRes.random_Am_0(3);
								p.vy = -(2 + CRes.random(4));
								p.f = 0;
								p.fRe = 15 + CRes.random(10);
							}
						}
					}
				}
			}

			// End skill cleanup (f >= fRemove)
			if (super.f >= super.fRemove)
			{
				if (super.objFireMain != null)
				{
					super.objFireMain.isTanHinh = false;
					super.objFireMain.dy = 0;
					if (super.objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
				if (super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null) target.dy = 0;
					}
				}
				if (GameScreen.typePaintGameScreen == 1)
				{
					GameScreen.isPaintNormal();
				}
				this.removeEff();
			}
		}

		// ─── setId 9: Eustass Kid — Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun Laser & Colossal Pillar) ───
		if (setId == 9)
		{
			// Toạ độ xuất chiêu cố định - nhân vật giữ nguyên x, y không bị lệch khi di chuyển
			int casterX = super.x;
			int casterY = super.y;
			int facingSign = (super.Dir == 2) ? 1 : -1;
			int impactX = (this.kidCannonImpactX != 0) ? this.kidCannonImpactX : super.toX;
			int impactY = (this.kidCannonImpactY != 0) ? this.kidCannonImpactY : super.toY;

			int cannonBaseX = casterX + facingSign * 35;
			int cannonBaseY = casterY - 26;

			// Phase 1: Hút vũ khí & Pose gồng nhẹ (f: 0..14)
			if (super.f < 14)
			{
				if (super.f == 2 || super.f == 8)
				{
					this.addSound((byte)18); // Tiếng từ trường hút kim loại
				}
			}

			// Phase 2: Ra hiệu ứng bắn laze -> Mở khóa di chuyển ngay lập tức (f == 14)
			if (super.f == 14)
			{
				if (super.objFireMain == GameScreen.player)
				{
					Player.isBlock = false;
				}
				if (super.objFireMain != null && super.objFireMain.Action == 2)
				{
					super.objFireMain.Action = 0; // Trở về pose bình thường, nhân vật tự do di chuyển ngay
				}
				this.addSound((byte)14); // Tiếng nổ khai hỏa đại pháo
				LoadMap.timeVibrateScreen = 8;
			}

			// Phase 3 & 4: Laser bắn trúng & Cột sáng nổ đi (f == 18)
			if (super.f == 18)
			{
				// Vết nứt đất phát sáng ở tầng levelPaint = -1 (dưới chân char/mob)
				GameScreen.addEffectEnd((short)133, 0, impactX, impactY, (byte)0, null);

				this.addSound((byte)14);
				LoadMap.timeVibrateScreen = 20;

				// Tính sát thương & ĐẨY LÙI mục tiêu chính + mục tiêu lân cận (tối đa nTarget = 5, bán kính rangeLan = 140)
				int hitCount = 0;
				// 1. Đẩy lùi mục tiêu chính
				if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							target.x += facingSign * 28; // Đẩy lùi mạnh về sau theo hướng bắn
							this.setAva(2, target);
							hitCount++;
						}
					}
				}

				// 2. Đẩy lùi các mục tiêu lân cận trong vecObjsBeFire
				if (super.vecObjsBeFire != null)
				{
					for (int k = 1; k < super.vecObjsBeFire.size() && hitCount < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo != null)
						{
							MainObject other = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (other != null && !other.isDie && other.Hp > 0 && !other.isRemove)
							{
								int dist = MainObject.getDistance(impactX, impactY, other.x, other.y);
								if (dist <= 140)
								{
									other.x += facingSign * 22; // Đẩy lùi mục tiêu lân cận
									this.setAva(2, other);
									hitCount++;
								}
							}
						}
					}
				}

				// 3. Đẩy lùi các quái/mục tiêu lân cận trên bản đồ (nếu chưa đủ nTarget = 5)
				if (hitCount < 5 && GameScreen.vecPlayers != null)
				{
					for (int k = 0; k < GameScreen.vecPlayers.size() && hitCount < 5; k++)
					{
						MainObject other = (MainObject)GameScreen.vecPlayers.elementAt(k);
						if (other != null && other != super.objFireMain && !other.isDie && other.Hp > 0 && !other.isRemove)
						{
							int dist = MainObject.getDistance(impactX, impactY, other.x, other.y);
							if (dist <= 140)
							{
								boolean alreadyHit = false;
								if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
								{
									Object_Effect_Skill t0 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(0);
									if (t0 != null && t0.ID == other.ID && t0.tem == other.typeObject)
									{
										alreadyHit = true;
									}
								}
								if (!alreadyHit)
								{
									other.x += facingSign * 22;
									this.setAva(2, other);
									hitCount++;
								}
							}
						}
					}
				}
			}

			// Cập nhật toạ độ 28 vũ khí kim loại trong VecEff
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point p = (Point)this.VecEff.elementAt(i);
					if (p == null) continue;

					p.frame = (p.frame + 1) % 8;

					if (super.f < 14)
					{
						// Bay xoáy từ ngoài vào tụ thành nòng pháo
						if (super.f >= p.fSmall)
						{
							int dur = Math.max(1, p.fRe - p.fSmall);
							int elapsed = Math.min(dur, super.f - p.fSmall);
							int targetX = cannonBaseX + p.AK;
							int targetY = cannonBaseY + p.AL;

							int style = p.f % 4;
							if (style == 3)
							{
								int hyperEase = (elapsed * elapsed * elapsed * 100) / (dur * dur * dur);
								p.x = p.x2 + ((targetX - p.x2) * hyperEase) / 100;
								p.y = p.y2 + ((targetY - p.y2) * hyperEase) / 100;
							}
							else
							{
								int easePercent = (elapsed * elapsed * 100) / (dur * dur);
								int baseX = p.x2 + ((targetX - p.x2) * easePercent) / 100;
								int baseY = p.y2 + ((targetY - p.y2) * easePercent) / 100;

								if (style == 0)
								{
									int arcMid = (elapsed * (dur - elapsed) * 300) / (dur * dur);
									p.x = baseX;
									p.y = baseY - arcMid;
								}
								else if (style == 1)
								{
									int waveX = (CRes.getsin((elapsed * 180) / dur) * p.dis) / 1000;
									p.x = baseX + waveX;
									p.y = baseY;
								}
								else
								{
									int zig = ((elapsed % 4) < 2) ? 6 : -6;
									p.x = baseX + zig;
									p.y = baseY - zig;
								}
							}
						}
					}
					else if (super.f < 32)
					{
						// Khóa chặt vị trí nòng pháo phát sáng
						p.x = cannonBaseX + p.AK;
						p.y = cannonBaseY + p.AL;
					}
					else
					{
						// Phase 5: Tán xạ ra xa khi năng lượng giải phóng xong
						p.x += p.vx;
						p.y += p.vy;
						p.vy += 1;
					}
				}
			}
			return;
		}

		// ─── setId 10: Cổ Độc Phán Quyết Venom _X (Magellan Hell's Judgment) — UPDATE TIMELINE ───
		if (setId == 10)
		{
			// Vũng axit độc ngầm cố định 1 vị trí tại tâm mục tiêu đã chọn, không di chuyển theo quái
			if (this.subType == 1004)
			{
				if (super.f >= super.fRemove)
				{
					this.removeEff();
				}
				return;
			}

			int targetX = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).x : super.toX;
			int targetY = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).y : super.toY;

			// Sound & Screen Vibration Timeline
			if (super.f == 5)
			{
				this.addSound((byte)10); // Toxic charge hiss
			}
			else if (super.f == 12)
			{
				this.addSound((byte)14); // Hydra 3-headed roar
				LoadMap.timeVibrateScreen = 8;
			}
			else if (super.f == 24)
			{
				this.addSound((byte)5);  // Venom dragon projectile blast
			}
			else if (super.f == 35)
			{
				// ZERO HOUR: VENOM IMPACT X DETONATION!
				this.addSound((byte)14);
				this.addSound((byte)51);
				this.addSound((byte)18);
				LoadMap.timeVibrateScreen = 28;

				// Flash & Flinch targets
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
						}
					}
				}
			}

			}
			else if (super.f == 46)
			{
				// Acid pool active bubbling sound
				this.addSound((byte)10);
			}
			else if (super.f == 52)
			{
				// Geyser eruption sound & screen tremor
				this.addSound((byte)51);
				this.addSound((byte)14);
				LoadMap.timeVibrateScreen = 16;
			}

			// Target flinch release at f44
			if (super.f == 44 && super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (target != null)
						{
							target.NG = false;
						}
					}
				}
			}

			// Release player block when skill completes
			if (super.f >= super.fRemove)
			{
				if (super.objFireMain != null)
				{
					super.objFireMain.NG = false;
					if (super.objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
						if (super.objFireMain.Action == 2)
						{
							super.objFireMain.Action = 0;
						}
					}
				}
				GameScreen.isPaintNormal();
			}
		}

		// ─── setId 13: Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu) – 5-Phase Timing & Hit Frame Sync ───
		if (setId == 13)
		{
			// Phase 1: Divine Charge Chime & Pre-Tremor
			if (super.f == 2)
			{
				this.addSound(10);
			}
			if (super.f == 6)
			{
				LoadMap.timeVibrateScreen = 6;
			}
			// Phase 2: Palm Thrust Sonic Boom at tick 10
			if (super.f == 10)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 16;
			}
			// Phase 4: OFFICIAL HIT FRAME at tick 21 (Impact detonation on all targets, damage tick, knockback recoil & screen shake)
			if (super.f == 21)
			{
				this.addSound(14);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 38;
				int facingSign = (super.Dir == 2) ? 1 : -1;
				int impX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : (super.x + facingSign * 180);
				int impY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : (super.y - 15);

				// Engine shockwave ring & detonations at epicenter
				GameScreen.addHightDataeff(238, impX, impY, super.Dir == 2);
				GameScreen.addHightDataeff(33, impX, impY + 10);
				GameScreen.addEffectEnd(92, 0, impX - 25, impY - 20, 0, null);
				GameScreen.addEffectEnd(92, 0, impX + 25, impY - 20, 2, null);
				GameScreen.addEffectEnd(110, 0, impX, impY, 0, null);

				if (super.vecObjsBeFire != null)
				{
					for (int j = 0; j < super.vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
							{
								this.setAva(2, tObj);
								tObj.x += facingSign * 22;
								tObj.dy = -14;
								GameScreen.addEffectEnd(63, 0, tObj.x, tObj.y, 0, null);
							}
						}
					}
				}
			}
			// Airborne recoil recovery for targets (ticks 22..29)
			if (super.f >= 22 && super.f <= 29)
			{
				if (super.vecObjsBeFire != null)
				{
					for (int j = 0; j < super.vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj != null && tObj.dy < 0)
							{
								tObj.dy += 2;
							}
						}
					}
				}
			}
			// Secondary Lotus Bloom Resonance at tick 26
			if (super.f == 26)
			{
				int facingSign = (super.Dir == 2) ? 1 : -1;
				int impX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : (super.x + facingSign * 180);
				int impY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : (super.y - 15);
				GameScreen.addHightDataeff(33, impX, impY + 8);
				this.addSound(10);
				LoadMap.timeVibrateScreen = 14;
			}
			// Clean reset at end of skill
			if (super.f >= super.fRemove)
			{
				if (super.vecObjsBeFire != null)
				{
					for (int j = 0; j < super.vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj != null)
							{
								tObj.dy = 0;
							}
						}
					}
				}
			}
		}

		if (setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)this.VecSubEff.elementAt(i);
				if (point_Focus == null) continue;
			point_Focus.x += ((point_Focus.Dir == 2) ? 22 : -22);
			point_Focus.y += point_Focus.vy;
			point_Focus.f++;

			// Dynamic Trailing Particles along trajectory
			if (point_Focus.f % 2 == 0)
			{
				if (setId == 1)
				{
					GameScreen.addEffectEnd(63, 0, point_Focus.x, point_Focus.y, 0, null);
				}
				else if (setId == 7)
				{
					GameScreen.addEffectEnd(133, 1, point_Focus.x, point_Focus.y + 8, (byte)point_Focus.Dir, null);
				}
			}

			// Impact & Explosion Phase
			if (point_Focus.f >= point_Focus.fRe)
			{
				int impactX = (point_Focus.objMain == null) ? point_Focus.toX : point_Focus.objMain.x;
				int impactY = (point_Focus.objMain == null) ? point_Focus.toY : (point_Focus.objMain.y - point_Focus.objMain.hOne / 2);
				if (point_Focus.objMain != null && !point_Focus.objMain.isDie && point_Focus.objMain.Hp > 0 && !point_Focus.objMain.isRemove)
				{
					this.setAva(2, point_Focus.objMain);
				}

				// Supreme Ultimate Impact for 16 Sets
				switch (setId)
				{
					case 1: // Entei Flame Emperor Supreme Cataclysm
						GameScreen.addHightDataeff(70, impactX, impactY - 45, false);
						GameScreen.addHightDataeff(70, impactX, impactY - 15, true);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX - 35, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX + 35, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY - 25, 0, null);
						LoadMap.timeVibrateScreen = 30;
						this.addSound(14);
						this.addSound(51);
						break;
					case 2: // Ryusei Kazan Magma Volcano
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(111, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(112, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(113, 0, impactX, impactY - 20, 0, null);
						GameScreen.addEffectEnd(85, 0, impactX, impactY - 15, 0, null);
						GameScreen.addEffectEnd(60, 2, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						this.addSound(51);
						this.addSound(5);
						break;
					case 3: // Ice Age Glacial Burst
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(35, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(17, 30, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 22;
						break;
					case 4: // Yasakani no Magatama Light Rain
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 5: // Black Hole & Dark Liberation
						GameScreen.addHightDataeff(8, impactX, impactY);
						GameScreen.addEffectEnd(108, 8, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 6: // 200M Volts Enel El Thor — Divine Thunder Impact
						GameScreen.addEffectEnd(40, 0, impactX, impactY, 0, null);          // electric burst
						GameScreen.addEffectEnd(42, 0, impactX, impactY, 0, null);          // electric spark
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);          // electric arc
						LoadMap.timeVibrateScreen = 30;
						this.addSound(18);
						break;
					case 7: // Island Shaker Quake Tsunami
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(133, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(133, 1, impactX, impactY + 6, 0, null);
						GameScreen.addEffectEnd(134, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(134, 1, impactX - 40, impactY + 6, 0, null);
						GameScreen.addEffectEnd(134, 1, impactX + 40, impactY + 6, 0, null);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(50, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX - 40, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX + 40, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY - 20, 0, null);
						LoadMap.timeVibrateScreen = 35;
						this.addSound(51);
						this.addSound(14);
						break;
					case 8: // Set 8: Law Phẫu Thuật
						GameScreen.addEffectEnd(10, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(30, 0, impactX, impactY, 400, 0, null);
						this.addSound(18);
						break;
					case 9: // Damned Punk Railgun
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 10: // Venom Demon Hell's Judgment
						GameScreen.addHightDataeff(8, impactX, impactY);
						GameScreen.addEffectEnd(108, 8, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 26;
						break;
					case 11: // Perfume Femur Petrification
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 24;
						break;
					case 12: // Blue Phoenix Rebirth Blaze
						GameScreen.addHightDataeff(70, impactX, impactY - 30, false);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 13: // Golden Buddha Divine Shockwave
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 14: // Dragon Roar & Thunder Bagua
						GameScreen.addHightDataeff(4, impactX, impactY);
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(50, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 15: // Sabo Dragon Flame Emperor
						GameScreen.addHightDataeff(70, impactX, impactY - 35, false);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(120, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 26;
						break;
				}
				this.VecSubEff.removeElement(point_Focus);
				i--;
			}
		}
		}
		if ((super.f >= super.fRemove && this.VecSubEff.size() == 0) || super.f > super.fRemove + 15)
		{
			if (super.objFireMain != null) super.objFireMain.dy = 0;
			if (GameScreen.typePaintGameScreen == 1)
			{
				GameScreen.isPaintNormal();
			}
			this.removeEff();
		}
		// setId 2/3/5/6/7/8/12/13/16 dùng this.VecEff thay this.VecSubEff — this.removeEff khi hết super.fRemove
		int _setId2 = (typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : (((typeEff - 4001) / 5) + 1);
		if (super.f > super.fRemove + 5 && (_setId2 == 2 || _setId2 == 3 || _setId2 == 5 || _setId2 == 6 || _setId2 == 7 || _setId2 == 8 || _setId2 == 12 || _setId2 == 13 || _setId2 == 16))
		{
			if (super.objFireMain != null) super.objFireMain.dy = 0;
			if (GameScreen.typePaintGameScreen == 1)
			{
				GameScreen.isPaintNormal();
			}
			this.removeEff();
		}
	}

	private void paintThanTrangSkill(mGraphics g)
	{
		if (g == null) return;
		try
		{
			int setId = (typeEffect == 4017 || typeEffect == 4010) ? 10 : ((typeEffect >= 4001 && typeEffect <= 4016) ? (typeEffect - 4000) : (((typeEffect - 4001) / 5) + 1));
			if (setId < 1 || setId > 16) setId = 1;
			ensureThanTrangFrames();
		// ═══════════════════════════════════════════════════════════════════
		// Paint Set 2: Đại Phún Hỏa Volcano _X (Akainu Magma Eruption)
		// Chuẩn hóa theo phong cách Hỏa Diễm & Nham Thạch nguyên bản HTTH
		// ═══════════════════════════════════════════════════════════════════
		if (setId == 2)
		{
			try
			{
				FrameImage[] p2 = (s_ttFrames != null && s_ttFrames.length > 2) ? s_ttFrames[2] : null;
				if (p2 != null && p2.length >= 10)
				{
					int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
					int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
					int impactX = super.toX;
					int impactY = super.toY;
					int facingSign = (super.Dir == 2) ? 1 : -1;
					int dirTrans = (super.Dir == 2) ? 0 : 2;

					// ─── TẦNG 1: TỤ LỰC DUNG NHAM TẠI CASTER (f: 0..12) ───
					if (super.f <= 12)
					{
						// Nền đất nứt lửa dưới chân Akainu (p2[7]: 246)
						if (p2[7] != null && p2[7].nFrame > 0)
						{
							int fissF = (super.f / 3) % p2[7].nFrame;
							p2[7].drawFrameNew(fissF, casterX, casterY + 4, 0, 3, g);
						}

						// Dòng dung nham cánh tay rực đỏ (p2[1]: 336)
						if (p2[1] != null && p2[1].nFrame > 0)
						{
							int armF = (super.f / 2) % p2[1].nFrame;
							p2[1].drawFrame(armF, casterX + facingSign * 12, casterY - 20, dirTrans, 3, g);
						}

						// Nắm đấm cự quyền tích tụ năng lượng (p2[0]: 271)
						if (p2[0] != null && p2[0].nFrame > 0)
						{
							int fistOffset = Math.min(20, 8 + super.f);
							p2[0].drawFrameNew(0, casterX + facingSign * fistOffset, casterY - 20, dirTrans, 3, g);
						}
					}

					// ─── TẦNG 2: ĐẠI PHÚN HỎA CỰ QUYỀN LAO KÍCH (f: 12..24) ───
					if (super.f >= 12 && super.f <= 24)
					{
						int t = super.f - 12; // 0..12
						int fistStartX = casterX + facingSign * 24;
						int fistStartY = casterY - 20;
						int fistX = fistStartX + ((impactX - fistStartX) * t) / 12;
						int fistY = fistStartY + ((impactY - 15 - fistStartY) * t) / 12;

						// Bóng trượt mặt đất sém đen (p2[9]: 272)
						if (p2[9] != null && p2[9].nFrame > 0)
						{
							p2[9].drawFrame(0, fistX, impactY + 4, 0, 3, g);
						}

						// Luồng cánh tay dung nham nối dài kéo sau lưng Cự Quyền (p2[1]: 336)
						if (p2[1] != null && p2[1].nFrame > 0)
						{
							int armF = (super.f / 2) % p2[1].nFrame;
							p2[1].drawFrame(armF, fistX - facingSign * 35, fistY, dirTrans, 3, g);
						}

						// Phản lực hỏa diễm bộc phát (p2[2]: 254)
						if (p2[2] != null && p2[2].nFrame > 0)
						{
							p2[2].drawFrame(0, fistX - facingSign * 55, fistY, dirTrans, 3, g);
						}

						// ĐẠI PHÚN HỎA CỰ QUYỀN KHỔNG LỒ (p2[0]: 271, 130x80)
						if (p2[0] != null && p2[0].nFrame > 0)
						{
							int fistF = (super.f / 2) % p2[0].nFrame;
							p2[0].drawFrameNew(fistF, fistX, fistY, dirTrans, 3, g);
						}
					}

					// ─── TẦNG 3: VỤ NỔ DUNG NHAM ĐẠI BỘC PHÁ TẠI TÂM CHẤN (f: 24..38) ───
					if (super.f >= 24 && super.f <= 38 && p2[3] != null && p2[3].nFrame > 0)
					{
						int blastF = (super.f - 24) / 3;
						if (blastF < p2[3].nFrame)
						{
							p2[3].drawFrameNew(blastF, impactX, impactY - 18, (super.f % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// ─── TẦNG 4: HỒ DUNG NHAM, VẾT RẠN NỨT & CỘT NHAM THẠCH PHUN TRÀO (f: 24..62) ───
					if (super.f >= 24 && super.f <= 62)
					{
						// A. Vết Rạn Nứt Núi Lửa (p2[7]: 246)
						if (p2[7] != null && p2[7].nFrame > 0)
						{
							int fissF = ((super.f - 24) / 3) % p2[7].nFrame;
							p2[7].drawFrameNew(fissF, impactX, impactY + 5, 0, 3, g);
							p2[7].drawFrameNew((fissF + 1) % p2[7].nFrame, impactX - 50, impactY + 3, 2, 3, g);
							p2[7].drawFrameNew((fissF + 2) % p2[7].nFrame, impactX + 50, impactY + 3, 0, 3, g);
						}

						// B. Hồ Dung Nham Sôi Sùng Sục (p2[6]: 239)
						if (p2[6] != null && p2[6].nFrame > 0)
						{
							int poolTrans1 = (super.f % 8 < 4) ? 0 : 2;
							int poolTrans2 = (super.f % 8 < 4) ? 2 : 0;
							p2[6].drawFrame(0, impactX, impactY + 2, poolTrans1, 3, g);
							p2[6].drawFrame(0, impactX - 45, impactY + 2, poolTrans2, 3, g);
							p2[6].drawFrame(0, impactX + 45, impactY + 2, poolTrans1, 3, g);
						}

						// C. Cột Nham Thạch (p2[4]: 238) & Cột Lửa (p2[5]: 240) Phun Trào Dữ Dội
						if (super.f >= 25 && super.f <= 58)
						{
							// 1. Cột trung tâm chọc trời
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX, impactY + 4, (super.f % 4 < 2 ? 0 : 2), 33, g);
							}
							if (p2[5] != null && p2[5].nFrame > 0)
							{
								p2[5].drawFrame(0, impactX, impactY - 60, (super.f % 4 < 2 ? 2 : 0), 33, g);
							}

							// 2. Hai cột phun trào hai bên sườn (bán kính 45px)
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX - 45, impactY - 5, 2, 33, g);
								p2[4].drawFrame(0, impactX + 45, impactY - 5, 0, 33, g);
							}
							if (p2[5] != null && p2[5].nFrame > 0)
							{
								p2[5].drawFrame(0, impactX - 45, impactY - 65, 0, 33, g);
								p2[5].drawFrame(0, impactX + 45, impactY - 65, 2, 33, g);
							}

							// 3. Hai cột rìa ngoài bao quát vùng lan 140px (p2[4]: 238)
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX - 85, impactY + 2, 0, 33, g);
								p2[4].drawFrame(0, impactX + 85, impactY + 2, 2, 33, g);
							}
						}
					}

					// ─── TẦNG 5: HẠT TÀN LỬA ĐỎ BẮN TUNG TÓE (f: 25..60) ───
					if (this.VecSubEff != null && super.f >= 25 && super.f <= 60 && p2[8] != null && p2[8].nFrame > 0)
					{
						for (int i = 0; i < this.VecSubEff.size(); i++)
						{
							Point p = (Point)this.VecSubEff.elementAt(i);
							if (p != null)
							{
								int sparkF = (p.f / 2) % p2[8].nFrame;
								p2[8].drawFrame(sparkF, p.x, p.y, p.color, 3, g);
							}
						}
					}

					// ─── TẦNG 6: HIỆU ỨNG THIÊU ĐỐT DƯỚI CHÂN MỤC TIÊU (f: 25..55) ───
					if (super.f >= 25 && super.f <= 55 && super.vecObjsBeFire != null)
					{
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int targetFlip = (k % 2 == 0) ? 0 : 2;

							// Vũng dung nham thiêu đốt dưới chân mục tiêu (p2[6]: 239)
							if (p2[6] != null && p2[6].nFrame > 0)
							{
								p2[6].drawFrame(0, tObj.x, tObj.y + 2, targetFlip, 3, g);
							}

							// Tàn lửa đỏ bùng cháy trên người mục tiêu (p2[8]: 78)
							if (p2[8] != null && p2[8].nFrame > 0 && (super.f + k) % 2 == 0)
							{
								int spF = (super.f / 2 + k) % p2[8].nFrame;
								p2[8].drawFrame(spF, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
			}
		}

		// Paint Set 3: Kỷ Băng Hà Tuyết Đối X (Absolute Blizzard / Ice Age X)
		if (setId == 3)
		{
			try
			{
				FrameImage[] p3 = s_ttFrames[3];
				if (p3 != null && p3.length >= 9)
				{
					int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
					int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
					int centerX = super.toX;
					int centerY = super.toY;
					int facingSign = (super.Dir == 2) ? 1 : -1;
					int dirTrans = (super.Dir == 2) ? 0 : 2;

					// ─── 1. CASTER SUB-ZERO AURA & 3D ORBITING DIAMOND ORBS (f: 0..22) ───
					if (super.f <= 22)
					{
						// Ground permafrost patch under caster feet (p3[1]: 40, 63x20)
						if (p3[1] != null && p3[1].nFrame > 0)
						{
							p3[1].drawFrame(0, casterX, casterY + 2, 0, 3, g);
						}

						// 3 Orbital Diamond Snow Spheres revolving around caster (p3[2]: 41, 40x40 & p3[7]: 104, 30x30)
						if (p3[2] != null && p3[2].nFrame > 0)
						{
							int baseAngle = (super.f * 24) % 360;
							for (int orb = 0; orb < 3; orb++)
							{
								int angle = (baseAngle + orb * 120) % 360;
								int ox = casterX + (CRes.getcos(angle) * 32 >> 10);
								int oy = casterY - 18 + (CRes.getsin(angle) * 12 >> 10);
								p3[2].drawFrame((super.f / 2 + orb) % p3[2].nFrame, ox, oy, 0, 3, g);
								if (p3[7] != null && p3[7].nFrame > 0 && super.f % 2 == 0)
								{
									p3[7].drawFrame((super.f / 2 + orb) % p3[7].nFrame, ox, oy - 2, 0, 3, g);
								}
							}
						}
					}

					// ─── 2. SURGING GLACIAL AVALANCHE WAVE (f: 5..20) ───
					if (super.f >= 5 && super.f <= 20)
					{
						int tProg = super.f - 5; // 0..15
						int waveX = casterX + ((centerX - casterX) * tProg) / 15;
						int waveY = casterY + ((centerY - casterY) * tProg) / 15;

						// Frozen Permafrost wake trailing behind the wave
						if (p3[1] != null && p3[1].nFrame > 0 && tProg >= 3)
						{
							int trailX = casterX + ((centerX - casterX) * (tProg - 3)) / 15;
							int trailY = casterY + ((centerY - casterY) * (tProg - 3)) / 15;
							p3[1].drawFrame(0, trailX, trailY + 2, dirTrans, 3, g);
						}

						// Colossal Ice Pheasant Crest Surge (p3[3]: 43, 84x110)
						if (p3[3] != null && p3[3].nFrame > 0)
						{
							int wFrame = Math.min(p3[3].nFrame - 1, tProg / 3);
							p3[3].drawFrame(wFrame, waveX, waveY - 25, dirTrans, 3, g);
						}

						// Snow / Frost Mist Sparkles along path (p3[2]: 41)
						if (p3[2] != null && p3[2].nFrame > 0)
						{
							p3[2].drawFrame((super.f / 2) % p3[2].nFrame, waveX - facingSign * 20, waveY - 10, 0, 3, g);
						}
					}

					// ─── 3. PERMAFROST GROUND CARPET AT EPICENTER (f: 14..55) ───
					if (super.f >= 14 && super.f <= 55 && p3[1] != null && p3[1].nFrame > 0)
					{
						p3[1].drawFrame(0, centerX, centerY + 4, 0, 3, g);
						p3[1].drawFrame(0, centerX - 55, centerY + 2, 0, 3, g);
						p3[1].drawFrame(0, centerX + 55, centerY + 2, 2, 3, g);
						p3[1].drawFrame(0, centerX - 110, centerY + 3, 2, 3, g);
						p3[1].drawFrame(0, centerX + 110, centerY + 3, 0, 3, g);
					}

					// ─── 4. FOREST OF 7 COLOSSAL ICE SPIRES, JAGGED ICICLES & BLIZZARD VORTEX (f: 14..55) ───
					if (this.VecEff != null && super.f >= 14)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point spire = (Point)this.VecEff.elementAt(i);
							if (spire == null) continue;

							if (spire.subType == 0)
							{
								int age = super.f - spire.fSmall;
								if (age < 0 || age >= 38) continue;

								// ─── Colossal Ice Spire (p3[0]: 37, 31x74) ───
								int targetH = spire.dis; // 65..140 px
								int curH = (age <= 4) ? (targetH * age / 4) : targetH;
								int shakeX = (age > 28 && (age % 2 == 0)) ? (spire.x + ((age % 4 == 0) ? 1 : -1)) : spire.x;
								int riseOffsetY = (targetH - curH);

								if (p3[0] != null && p3[0].nFrame > 0)
								{
									p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY, spire.color, 33, g);
									if (curH > 74)
									{
										p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY - 55, spire.color == 2 ? 0 : 2, 33, g);
									}
									if (curH > 120)
									{
										p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY - 100, spire.color, 33, g);
									}
								}

								// Base Jagged Ice (p3[4]: 89, 28x44) at ground level
								if (p3[4] != null && p3[4].nFrame > 0 && age >= 2 && age <= 34)
								{
									int jF = Math.min(p3[4].nFrame - 1, age / 4);
									p3[4].drawFrame(jF, shakeX, spire.y + 2, spire.color, 33, g);
								}

								// Spire Frost Sparkle Glint (p3[7]: 104) at tip
								if (p3[7] != null && p3[7].nFrame > 0 && age >= 4 && age <= 28 && ((age + spire.fSmall) % 3 == 0))
								{
									p3[7].drawFrame((age / 3) % p3[7].nFrame, shakeX, spire.y - curH + 4, 0, 3, g);
								}

								// Base sub-zero frost mist (p3[8]: 152)
								if (p3[8] != null && p3[8].nFrame > 0 && age <= 12)
								{
									p3[8].drawFrame(age / 3 % p3[8].nFrame, shakeX + (spire.color == 2 ? 8 : -8), spire.y + 2, spire.color, 33, g);
								}
							}
							else if (spire.subType == 1)
							{
								int age = super.f - spire.fSmall;
								if (age < 0 || age >= 38) continue;

								// ─── Intermediate Ground Icicles (p3[4]: 89, 28x44) ───
								if (p3[4] != null && p3[4].nFrame > 0 && age <= 32)
								{
									int jF = Math.min(p3[4].nFrame - 1, age / 3);
									p3[4].drawFrame(jF, spire.x, spire.y + 2, spire.color, 33, g);
								}
							}
							else if (spire.subType == 2 && super.f >= 16 && super.f <= 54)
							{
								// ─── Blizzard Vortex & Diamond Dust Particles (subType = 2) ───
								int curAngle = (spire.frame + (super.f - 16) * 16) % 360;
								int radVariation = CRes.getsin(((super.f - 16) * 18 + spire.fSmall * 20) % 360) * 12 >> 10;
								int curR = Math.max(12, spire.dis + radVariation);

								int px = centerX + (CRes.getcos(curAngle) * curR >> 10);
								int py = centerY - 28 + (CRes.getsin(curAngle) * (curR * 3 / 5) >> 10);

								if (spire.color == 1 && p3[7] != null && p3[7].nFrame > 0)
								{
									p3[7].drawFrame((super.f / 2 + spire.fSmall) % p3[7].nFrame, px, py, 0, 3, g);
								}
								else if (p3[2] != null && p3[2].nFrame > 0)
								{
									p3[2].drawFrame((super.f / 2 + spire.fSmall) % p3[2].nFrame, px, py, 0, 3, g);
								}
							}
						}
					}

					// ─── 5. FROST ENCASEMENT & DIAMOND GLINT OVER TARGETS (f: 18..50) ───
					if (super.f >= 18 && super.f <= 50 && super.vecObjsBeFire != null)
					{
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							// Eternal permafrost block encasing target (p3[6]: 39, 53x28)
							if (p3[6] != null && p3[6].nFrame > 0)
							{
								p3[6].drawFrame(0, tObj.x, tObj.y - 6, 0, 3, g);
							}
							// Frozen head frost crown / diamond glint (p3[7]: 104)
							if (p3[7] != null && p3[7].nFrame > 0 && (super.f + k) % 3 == 0)
							{
								p3[7].drawFrame((super.f / 2 + k) % p3[7].nFrame, tObj.x, tObj.y - tObj.hOne - 4, 0, 3, g);
							}
							// Ground freeze base under target feet (p3[1]: 40)
							if (p3[1] != null && p3[1].nFrame > 0)
							{
								p3[1].drawFrame(0, tObj.x, tObj.y + 2, (k % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// ─── 6. CATACLYSMIC ICE SHATTER SPARKS (f: 45..56) ───
					if (super.f >= 45 && super.f <= 56 && p3[7] != null && p3[7].nFrame > 0)
					{
						int sProg = super.f - 45;
						for (int sp = 0; sp < 8; sp++)
						{
							int spAng = sp * 45;
							int spDist = sProg * 10;
							int sx = centerX + (CRes.getcos(spAng) * spDist >> 10);
							int sy = centerY - 30 + (CRes.getsin(spAng) * (spDist * 3 / 4) >> 10);
							p3[7].drawFrame((sProg + sp) % p3[7].nFrame, sx, sy, 0, 3, g);
						}
					}
				}
			}
			catch (Exception e)
			{
			}
		}

		// ═══════════════════════════════════════════════════════════════════
		// Paint Set 5: Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity)
		// 6 Master Dark Void Assets (IDs 480..485)
		// ═══════════════════════════════════════════════════════════════════
		// ═══════════════════════════════════════════════════════════════════
		// Paint Set 5: Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity)
		// Master Multi-Layered VFX System (14 Visual Layers + Procedural Gravitational Spirals)
		// ═══════════════════════════════════════════════════════════════════
		if (setId == 5)
		{
			try
			{
				FrameImage[] p5 = (s_ttFrames != null && s_ttFrames.length > 5) ? s_ttFrames[5] : null;
				if (p5 != null && p5.length >= 6)
				{
					int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
					int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
					int impactX = super.toX;
					int impactY = super.toY;
					int facingSign = (super.Dir == 2) ? 1 : -1;
					int dirTrans = (super.Dir == 2) ? 0 : 2;

					// ─── TẦNG 1: VỰC THẲM HẮC ÁM & HỒ BÓNG TỐI DƯỚI ĐẤT (f: 18..72) ───
					// Sử dụng p5[6] (272: Scorched Ground Shadow) xếp tầng tạo thành Hồ Hư Vô Khổng Lồ
					if (p5.length > 6 && p5[6] != null && p5[6].nFrame > 0 && super.f >= 18 && super.f <= 72)
					{
						p5[6].drawFrame(0, impactX, impactY + 6, 0, 3, g);
						p5[6].drawFrame(0, impactX - 45, impactY + 4, 2, 3, g);
						p5[6].drawFrame(0, impactX + 45, impactY + 4, 0, 3, g);
						p5[6].drawFrame(0, impactX - 85, impactY + 3, 0, 3, g);
						p5[6].drawFrame(0, impactX + 85, impactY + 3, 2, 3, g);
					}

					// ─── TẦNG 2: VẾT RẠN NỨT ĐỊA CHẤN HƯ VÔ (f: 20..68) ───
					// Sử dụng p5[7] (246: Ground Fissure) rạn nứt mặt đất dưới sức hút hố đen
					if (p5.length > 7 && p5[7] != null && p5[7].nFrame > 0 && this.VecEff != null && super.f >= 20 && super.f <= 68)
					{
						for (int r = 0; r < this.VecEff.size(); r++)
						{
							Point rp = (Point)this.VecEff.elementAt(r);
							if (rp != null && super.f >= rp.fSmall)
							{
								int fissF = ((super.f - rp.fSmall) / 3) % p5[7].nFrame;
								p5[7].drawFrameNew(fissF, rp.x, rp.y, rp.subType, 3, g);
							}
						}
					}

					// ─── TẦNG 3: VÀNH ĐAI SÓNG TRỌNG LỰC ĐỊA BÀN (f: 20..64) ───
					// Sử dụng p5[9] (394: 126x41 Ground Shockwave Ring) tỏa sóng hấp dẫn liên tục
					if (p5.length > 9 && p5[9] != null && p5[9].nFrame > 0 && super.f >= 20 && super.f <= 64)
					{
						int ringF = ((super.f - 20) / 2) % p5[9].nFrame;
						p5[9].drawFrame(ringF, impactX, impactY + 4, 0, 3, g);
						if (super.f % 4 < 2)
						{
							p5[9].drawFrame((ringF + 1) % p5[9].nFrame, impactX, impactY + 2, 2, 3, g);
						}
					}

					// ─── TẦNG 4: KHÍ HẮC ÁM & CỔNG KHÔNG GIAN TẠI CASTER (f: 0..32) ───
					// Nền đen dưới chân Caster (p5[6]: 272)
					if (p5.length > 6 && p5[6] != null && p5[6].nFrame > 0 && super.f <= 26)
					{
						p5[6].drawFrame(0, casterX, casterY + 4, 0, 3, g);
					}
					// Khói đen hư vô cuộn quanh Caster (p5[0]: 480)
					if (p5[0] != null && super.f <= 28)
					{
						int n = (p5[0].nFrame > 0) ? p5[0].nFrame : 8;
						int auraF = (super.f / 2) % n;
						p5[0].drawFrame(auraF, casterX, casterY + 2, dirTrans, 33, g);
					}
					// Cổng không gian mở trước Caster giải phóng hắc ám (p5[1]: 481)
					if (p5[1] != null && super.f <= 22)
					{
						int n = (p5[1].nFrame > 0) ? p5[1].nFrame : 8;
						int castF = Math.min(n - 1, super.f / 2);
						p5[1].drawFrame(castF, casterX + facingSign * 35, casterY + 4, dirTrans, 33, g);
					}

					// ─── TẦNG 5: HẮC TINH CẦU LAO XÉ KHÔNG GIAN (f: 12..24) ───
					// p5[2]: 482 (Void Wave Projectile) phóng từ Caster tới tâm Hố Đen
					if (p5[2] != null && super.f >= 12 && super.f <= 24)
					{
						int n = (p5[2].nFrame > 0) ? p5[2].nFrame : 8;
						int pF = (super.f - 12) % n;
						int startX = casterX + facingSign * 35;
						int startY = casterY - 15;
						int endX = impactX;
						int endY = impactY - 20;
						int tProg = super.f - 12; // 0..12
						int curX = startX + (endX - startX) * tProg / 12;
						int curY = startY + (endY - startY) * tProg / 12;

						// Vòng nén trọng lực bay kèm (p5[13]: 175)
						if (p5.length > 13 && p5[13] != null && p5[13].nFrame > 0)
						{
							p5[13].drawFrame(0, curX - facingSign * 18, curY, dirTrans, 3, g);
						}
						// Bóng hắc tinh cầu mặt đất (p5[6]: 272)
						if (p5.length > 6 && p5[6] != null && p5[6].nFrame > 0)
						{
							p5[6].drawFrame(0, curX, impactY + 3, 0, 3, g);
						}
						// Hắc tinh cầu chính (p5[2]: 482)
						p5[2].drawFrame(pF, curX, curY, dirTrans, 3, g);
					}

					// ─── TẦNG 6: MA TRẬN XOÁY TRỌNG LỰC HƯ VÔ QUANH HỐ ĐEN (f: 20..54) ───
					// Vẽ các nhánh xoắn ốc trọng lực Logarithmic Spiral hút vào tâm (Procedural Cosmic Darkness)
					if (super.f >= 20 && super.f <= 54)
					{
						int rotBase = (super.f * 9) % 360;
						// 3 Nhánh xoắn ốc hắc ám (Triple Accretion Spiral Arms)
						for (int arm = 0; arm < 3; arm++)
						{
							int armAngle = (rotBase + arm * 120) % 360;
							int prevPx = 0;
							int prevPy = 0;
							// Đi từ rìa ngoài (120px) xoáy vào tâm (15px)
							for (int step = 6; step >= 1; step--)
							{
								int rDist = step * 20; // 120, 100, 80, 60, 40, 20
								int spiralAng = (armAngle + (6 - step) * 28) % 360;
								int px = impactX + (rDist * CRes.getcos(spiralAng) >> 10);
								int py = impactY - 20 + ((rDist * 3 / 5) * CRes.getsin(spiralAng) >> 10);

								if (step < 6)
								{
									// Lớp hào quang tím huyền ảo ngoài
									g.setColor(0x7B1FA2);
									g.drawLine(prevPx, prevPy, px, py);
									// Lớp lõi hắc ám obsidian bên trong
									g.setColor(0x1A0033);
									g.drawLine(prevPx + 1, prevPy, px + 1, py);
									g.setColor(0x0A0014);
									g.drawLine(prevPx, prevPy + 1, px, py + 1);
								}
								prevPx = px;
								prevPy = py;
							}
						}

						// Vành đai chân trời sự kiện (Concentric Event Horizon Rings)
						g.setColor(0x4A148C);
						int r1 = 35 + (super.f % 6) * 2;
						int r2 = 65 + ((super.f + 3) % 6) * 2;
						for (int ang = 0; ang < 12; ang++)
						{
							int a1 = (rotBase + ang * 30) % 360;
							int a2 = (rotBase + (ang + 1) * 30) % 360;
							int px1 = impactX + (r1 * CRes.getcos(a1) >> 10);
							int py1 = impactY - 20 + ((r1 * 3 / 5) * CRes.getsin(a1) >> 10);
							int px2 = impactX + (r1 * CRes.getcos(a2) >> 10);
							int py2 = impactY - 20 + ((r1 * 3 / 5) * CRes.getsin(a2) >> 10);
							g.drawLine(px1, py1, px2, py2);

							int qx1 = impactX + (r2 * CRes.getcos(a1) >> 10);
							int qy1 = impactY - 20 + ((r2 * 3 / 5) * CRes.getsin(a1) >> 10);
							int qx2 = impactX + (r2 * CRes.getcos(a2) >> 10);
							int qy2 = impactY - 20 + ((r2 * 3 / 5) * CRes.getsin(a2) >> 10);
							g.drawLine(qx1, qy1, qx2, qy2);
						}
					}

					// ─── TẦNG 7: 16 HẠT VẬT CHẤT TỐI XOAY HÚT VÀO HỐ ĐEN (this.VecSubEff) (f: 20..54) ───
					if (this.VecSubEff != null && super.f >= 20 && super.f <= 54)
					{
						for (int i = 0; i < this.VecSubEff.size(); i++)
						{
							Point bp = (Point)this.VecSubEff.elementAt(i);
							if (bp != null)
							{
								int px = impactX + (bp.dis * CRes.getcos(bp.frame) >> 10);
								int py = impactY - 20 + ((bp.dis * 3 / 5) * CRes.getsin(bp.frame) >> 10);

								// Đốm sao bụi hắc ám (p5[10]: 104)
								if (bp.subType == 1 && p5.length > 10 && p5[10] != null && p5[10].nFrame > 0)
								{
									int sF = (super.f / 2 + bp.fSmall) % p5[10].nFrame;
									p5[10].drawFrame(sF, px, py, 0, 3, g);
								}
								else
								{
									// Điểm phát sáng vật chất tối tím/đen
									g.setColor(bp.color);
									g.fillRect(px - 1, py - 1, 3, 3);
									g.setColor(0xE1BEE7);
									g.fillRect(px, py, 1, 1);
								}
							}
						}
					}

					// ─── TẦNG 8: LỖ ĐEN THÔN PHỆ KHỔNG LỒ 240x240 (p5[3]: 483) (f: 20..52) ───
					if (p5[3] != null && super.f >= 20 && super.f <= 52)
					{
						int n = (p5[3].nFrame > 0) ? p5[3].nFrame : 10;
						int vF = ((super.f - 20) / 2) % n;
						p5[3].drawFrame(vF, impactX, impactY - 20, 0, 3, g);

						// Lớp lõi hố đen thứ hai xoay nghịch tạo chiều sâu không gian
						if (super.f >= 24 && super.f <= 48 && (super.f % 2 == 0))
						{
							p5[3].drawFrame((vF + 3) % n, impactX, impactY - 20, 2, 3, g);
						}
					}

					// ─── TẦNG 9: TIA SÉT HƯ VÔ KẾT NỐI TÂM HỐ ĐEN VỚI CÁC MỤC TIÊU (f: 24..50) ───
					if (super.f >= 24 && super.f <= 50 && super.vecObjsBeFire != null)
					{
						int tickSeed = GameCanvas.gameTick;
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int tx = tObj.x;
							int ty = tObj.y - tObj.hOne / 2;
							int jitter = ((tickSeed + k * 3) % 3 - 1) * 7;
							int midX1 = impactX + (tx - impactX) / 3 + jitter;
							int midY1 = (impactY - 20) + (ty - (impactY - 20)) / 3 - jitter / 2;
							int midX2 = impactX + (tx - impactX) * 2 / 3 - jitter;
							int midY2 = (impactY - 20) + (ty - (impactY - 20)) * 2 / 3 + jitter / 2;

							// Sét tím hư vô
							g.setColor(0x9C27B0);
							g.drawLine(impactX, impactY - 20, midX1, midY1);
							g.drawLine(midX1, midY1, midX2, midY2);
							g.drawLine(midX2, midY2, tx, ty);
							// Sợi quang học trắng-tím ở lõi
							g.setColor(0xE1BEE7);
							g.drawLine(impactX + 1, impactY - 20, midX1 + 1, midY1);
							g.drawLine(midX1 + 1, midY1, midX2 + 1, midY2);
							g.drawLine(midX2 + 1, midY2, tx + 1, ty);

							// Sét lớn hồ quang hư vô (p5[12]: 92)
							if (p5.length > 12 && p5[12] != null && ((super.f + k) % 4 == 0))
							{
								p5[12].drawFrame(0, tx, ty - 15, (k % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// ─── TẦNG 10: TRỤ HẮC ÁM & BỤI SAO TRÓI CHÂN MỤC TIÊU (f: 22..62) ───
					if (p5[5] != null && super.f >= 22 && super.f <= 62 && super.vecObjsBeFire != null)
					{
						int n = (p5[5].nFrame > 0) ? p5[5].nFrame : 8;
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int sF = ((super.f - 22) / 2 + k * 2) % n;
							// Bóng đen dưới chân mục tiêu (p5[6]: 272)
							if (p5.length > 6 && p5[6] != null && p5[6].nFrame > 0)
							{
								p5[6].drawFrame(0, tObj.x, tObj.y + 3, (k % 2 == 0 ? 0 : 2), 3, g);
							}
							// Cột hắc khí xoắn ốc nuốt trọn mục tiêu (p5[5]: 485, 90x140)
							p5[5].drawFrame(sF, tObj.x, tObj.y + 2, (k % 2 == 0 ? 0 : 2), 33, g);

							// Điểm nổ sao đen trên người mục tiêu (p5[10]: 104)
							if (p5.length > 10 && p5[10] != null && p5[10].nFrame > 0 && (super.f + k) % 3 == 0)
							{
								int spkF = (super.f / 2 + k) % p5[10].nFrame;
								p5[10].drawFrame(spkF, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
							}
						}
					}

					// ─── TẦNG 11: ĐẠI HƯ VÔ SỤP ĐỔ BỘC PHÁ CỰC ĐẠI (f: 48..74) ───
					// Vụ nổ hủy diệt 280x200 (p5[4]: 484)
					if (p5[4] != null && super.f >= 48 && super.f <= 74)
					{
						int n = (p5[4].nFrame > 0) ? p5[4].nFrame : 12;
						int colF = (super.f - 48) / 2;
						if (colF < n)
						{
							p5[4].drawFrame(colF, impactX, impactY - 25, (super.f % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// ─── TẦNG 12: SÓNG BIẾN DẠNG KHÔNG GIAN BÙNG NỔ (f: 48..66) ───
					// Sóng cầu méo mó không gian (p5[8]: 285, 111x90)
					if (p5.length > 8 && p5[8] != null && p5[8].nFrame > 0 && super.f >= 48 && super.f <= 66)
					{
						int distF = Math.min(p5[8].nFrame - 1, (super.f - 48) / 3);
						p5[8].drawFrame(distF, impactX, impactY - 25, 0, 3, g);
						p5[8].drawFrame(distF, impactX, impactY - 25, 2, 3, g);
					}

					// ─── TẦNG 13: VÀNH ĐAI XUNG KÍCH BỘC PHÁ BỐN PHƯƠNG (f: 49..68) ───
					// Vành đai nén trọng lực vỡ tung (p5[13]: 175) & Vành đai mặt đất (p5[9]: 394)
					if (super.f >= 49 && super.f <= 68)
					{
						int ageDet = super.f - 49;
						if (p5.length > 13 && p5[13] != null && p5[13].nFrame > 0)
						{
							p5[13].drawFrame(0, impactX - ageDet * 6, impactY - 20, 0, 3, g);
							p5[13].drawFrame(0, impactX + ageDet * 6, impactY - 20, 2, 3, g);
							p5[13].drawFrame(0, impactX, impactY - 20 - ageDet * 4, 0, 3, g);
							p5[13].drawFrame(0, impactX, impactY - 20 + ageDet * 4, 2, 3, g);
						}
						if (p5.length > 9 && p5[9] != null && p5[9].nFrame > 0 && ageDet <= 14)
						{
							p5[9].drawFrame(ageDet / 2 % p5[9].nFrame, impactX - 60, impactY + 4, 0, 3, g);
							p5[9].drawFrame(ageDet / 2 % p5[9].nFrame, impactX + 60, impactY + 4, 2, 3, g);
						}
					}

					// ─── TẦNG 14: MẢNH VẬT CHẤT TỐI BẮN RA 8 HƯỚNG & KHÓI BỤI SỤP ĐỔ (f: 50..70) ───
					if (super.f >= 50 && super.f <= 70)
					{
						int sProg = super.f - 50;
						// Bụi khói tiếp đất (p5[11]: 152)
						if (p5.length > 11 && p5[11] != null && p5[11].nFrame > 0 && sProg <= 12)
						{
							int dF = Math.min(p5[11].nFrame - 1, sProg / 2);
							p5[11].drawFrame(dF, impactX - 55, impactY + 4, 0, 3, g);
							p5[11].drawFrame(dF, impactX + 55, impactY + 4, 2, 3, g);
						}
						// 8 Tia mảnh vỡ hư vô bắn tỏa ra 8 góc (p5[10]: 104)
						if (p5.length > 10 && p5[10] != null && p5[10].nFrame > 0)
						{
							for (int sp = 0; sp < 8; sp++)
							{
								int spAng = sp * 45;
								int spDist = sProg * 9;
								int sx = impactX + (spDist * CRes.getcos(spAng) >> 10);
								int sy = impactY - 25 + ((spDist * 3 / 4) * CRes.getsin(spAng) >> 10);
								p5[10].drawFrame((sProg + sp) % p5[10].nFrame, sx, sy, 0, 3, g);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
			}
		}

		// Paint Multi-Node Gradual Spatial Shatter & Full Screen Glass Break for Set 7 (Quake / Trấn Thiên)
		if (setId == 7 && this.VecEff != null)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int screenW = GameCanvas.w > 0 ? GameCanvas.w : MotherCanvas.w;
			int screenH = GameCanvas.h > 0 ? GameCanvas.h : MotherCanvas.h;

			for (int i = 0; i < this.VecEff.size(); i++)
			{
				Point node = (Point)this.VecEff.elementAt(i);
				if (node != null)
				{
					int age = node.f - node.fSmall;
					if (age > 0 && age < 55)
					{
						FrameImage img = (node.color == 1 && super.fraImgSubEff != null) ? super.fraImgSubEff : super.fraImgEff;
						if (img != null && img.getImageFrame() != null)
						{
							int w = img.frameWidth;
							int h = img.frameHeight;
							if (w > 0 && h > 0)
							{
								int drawX = (node.subType == 2) ? (camX + screenW * node.x / 100) : node.x;
								int drawY = (node.subType == 2) ? (camY + screenH * node.y / 100) : node.y;

								// Subtle natural vibration on fracture lines
								if (age > 6 && age < 30 && (age % 3 == 0))
								{
									drawX += (age % 2 == 0 ? 1 : -1);
								}

								if (age < 5)
								{
									// Stage 1: Initial hairline fracture from crack center
									int clipW = Math.max(1, w / 3);
									int clipH = Math.max(1, h / 3);
									int srcX = (w - clipW) / 2;
									int srcY = (h - clipH) / 2;
									g.drawRegion(img.getImageFrame(), srcX, srcY, clipW, clipH, 0, drawX, drawY, 3);
								}
								else if (age < 14)
								{
									// Stage 2: Rapidly spiderwebbing cracks expanding outwards
									int clipW = Math.max(1, w * 3 / 4);
									int clipH = Math.max(1, h * 3 / 4);
									int srcX = (w - clipW) / 2;
									int srcY = (h - clipH) / 2;
									g.drawRegion(img.getImageFrame(), srcX, srcY, clipW, clipH, 0, drawX, drawY, 3);
								}
								else
								{
									// Stage 3: Full shattered glass pane & celestial spatial rupture
									g.drawRegion(img.getImageFrame(), 0, 0, w, h, 0, drawX, drawY, 3);
								}
							}
						}
					}
				}
			}
		}

		// Paint Set 6: Enel 200 Triệu Volt Thần Lôi — Pure Authentic Lightning Matrix, Aerial Teleport & 200M Volt El Thor
		if (setId == 6)
		{
			FrameImage[] p6 = s_ttFrames[6];
			if (p6 != null && p6.length >= 8)
			{
				int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
				int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
				int casterDy = (super.objFireMain != null) ? super.objFireMain.dy : 0;
				int facingSign = (super.Dir == 2) ? 1 : -1;
				int cy = casterY + casterDy;

				int impactX = super.toX;
				int impactY = super.toY;
				if (impactX == 0 && impactY == 0)
				{
					impactX = casterX + facingSign * 140;
					impactY = casterY;
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 1: GROUND LIGHTNING MAGIC MATRIX (f: 4..68)
				// ═══════════════════════════════════════════════════════════════════
				if (super.f >= 4 && super.f <= 68)
				{
					// High-Voltage Ground Discharge Rings (p6[4]: 242, 49x28, 2f)
					if (p6[4] != null && p6[4].nFrame > 0)
					{
						int ringF = (super.f / 3) % p6[4].nFrame;
						// Epicenter ring
						p6[4].drawFrameNew(ringF, impactX, impactY + 4, 0, 3, g);
						// Perimeter discharge nodes (left, right, top, bottom, far-left, far-right)
						p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, impactX - 65, impactY + 4, 0, 3, g);
						p6[4].drawFrameNew((ringF + 2) % p6[4].nFrame, impactX + 65, impactY + 4, 2, 3, g);
						p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, impactX, impactY - 22, 0, 3, g);
						p6[4].drawFrameNew((ringF + 2) % p6[4].nFrame, impactX, impactY + 22, 2, 3, g);
						p6[4].drawFrameNew((ringF + 3) % p6[4].nFrame, impactX - 120, impactY + 4, 0, 3, g);
						p6[4].drawFrameNew((ringF + 3) % p6[4].nFrame, impactX + 120, impactY + 4, 2, 3, g);
					}

					// Ground Lightning Spiderweb Crackles (p6[3]: 241, 40x27, 2f)
					if (p6[3] != null && p6[3].maxNumFrame > 0)
					{
						int crackleF = (super.f % 2) * 2 + (GameCanvas.gameTick / 2 % 2);
						p6[3].drawFrameNew(crackleF, impactX, impactY + 8, 0, 3, g);
						p6[3].drawFrameNew((crackleF + 1) % 4, impactX - 70, impactY + 6, 0, 3, g);
						p6[3].drawFrameNew((crackleF + 2) % 4, impactX + 70, impactY + 6, 2, 3, g);
						p6[3].drawFrameNew((crackleF + 3) % 4, impactX - 130, impactY + 4, 0, 3, g);
						p6[3].drawFrameNew((crackleF) % 4, impactX + 130, impactY + 4, 2, 3, g);
					}

					// Locked target grounding rings (p6[4]: 242)
					if (p6[4] != null && p6[4].nFrame > 0 && super.vecObjsBeFire != null)
					{
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo != null)
							{
								MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
								if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
								{
									int ringF = (super.f / 3 + k) % p6[4].nFrame;
									p6[4].drawFrameNew(ringF, tObj.x, tObj.y + 4, (k % 2 == 0 ? 0 : 2), 3, g);
								}
							}
						}
					}

					// Procedural Rotating Geometric Electric Matrix Polygons (Dual-layer Cyan & White Core)
					int rotAngle = (super.f * 5) % 360;
					g.setColor(0x00E5FF);
					for (int ang = 0; ang < 6; ang++)
					{
						int a1 = (rotAngle + ang * 60) % 360;
						int a2 = (rotAngle + (ang + 1) * 60) % 360;
						int px1 = impactX + (65 * CRes.getcos(a1) >> 10);
						int py1 = impactY + (39 * CRes.getsin(a1) >> 10);
						int px2 = impactX + (65 * CRes.getcos(a2) >> 10);
						int py2 = impactY + (39 * CRes.getsin(a2) >> 10);
						g.drawLine(px1, py1, px2, py2);

						int ox1 = impactX + (125 * CRes.getcos(a1) >> 10);
						int oy1 = impactY + (75 * CRes.getsin(a1) >> 10);
						int ox2 = impactX + (125 * CRes.getcos(a2) >> 10);
						int oy2 = impactY + (75 * CRes.getsin(a2) >> 10);
						g.drawLine(ox1, oy1, ox2, oy2);
						g.drawLine(px1, py1, ox1, oy1);
					}
					g.setColor(0xFFFFFF);
					for (int ang = 0; ang < 6; ang++)
					{
						int a1 = (rotAngle + ang * 60) % 360;
						int px1 = impactX + (65 * CRes.getcos(a1) >> 10);
						int py1 = impactY + (39 * CRes.getsin(a1) >> 10);
						g.fillRect(px1 - 1, py1 - 1, 3, 3);
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 2: PROCEDURAL LIGHTNING MATRIX ARCS ("VẼ MA TRẬN SÉT") (f: 10..56)
				// ═══════════════════════════════════════════════════════════════════
				if (this.VecSubEff != null && super.f >= 10 && super.f <= 56)
				{
					int tickSeed = GameCanvas.gameTick;
					for (int e = 0; e < this.VecSubEff.size(); e++)
					{
						Point arc = (Point)this.VecSubEff.elementAt(e);
						if (arc != null && super.f >= arc.fSmall && super.f <= (arc.fSmall + arc.fRe))
						{
							int x1 = arc.x;
							int y1 = arc.y;
							int x2 = arc.x2;
							int y2 = arc.y2;

							// Compute 4-segment procedural electric zigzag displacement
							int jitter = ((tickSeed + arc.frame + e) % 3 - 1) * 6;
							int midX1 = x1 + (x2 - x1) / 4 + jitter;
							int midY1 = y1 + (y2 - y1) / 4 - (jitter / 2);
							int midX2 = x1 + (x2 - x1) / 2 - jitter;
							int midY2 = y1 + (y2 - y1) / 2 + (jitter / 2);
							int midX3 = x1 + (x2 - x1) * 3 / 4 + jitter;
							int midY3 = y1 + (y2 - y1) * 3 / 4 - (jitter / 2);

							// Outer High-Voltage Corona Line (Cyan / Electric Blue)
							g.setColor(arc.color);
							g.drawLine(x1, y1, midX1, midY1);
							g.drawLine(midX1, midY1, midX2, midY2);
							g.drawLine(midX2, midY2, midX3, midY3);
							g.drawLine(midX3, midY3, x2, y2);

							// Inner Searing White-Hot Core Line
							g.setColor(0xFFFFFF);
							g.drawLine(x1 + 1, y1, midX1 + 1, midY1);
							g.drawLine(midX1 + 1, midY1, midX2 + 1, midY2);
							g.drawLine(midX2 + 1, midY2, midX3 + 1, midY3);
							g.drawLine(midX3 + 1, midY3, x2 + 1, y2);

							// Starburst sparks at junction nodes
							if (p6[5] != null && p6[5].nFrame > 0 && ((super.f + e) % 3 == 0))
							{
								int sF = ((super.f / 2) + e) % p6[5].nFrame;
								p6[5].drawFrame(sF, midX2, midY2, 0, 3, g);
							}

							// High-voltage giant electric arc bursts (p6[7]: 92) at active nodes
							if (p6[7] != null && (super.f % 6 == 0) && e % 4 == 0)
							{
								p6[7].drawFrame(0, midX2, midY2 - 30, (e % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// Swirling electric plasma sparks & mini-orbs drifting across matrix
					if (p6[5] != null && p6[5].nFrame > 0)
					{
						for (int em = 0; em < 6; em++)
						{
							int emAngle = (super.f * 14 + em * 60) % 360;
							int emDist = 45 + (em * 16);
							int emX = impactX + (emDist * CRes.getcos(emAngle) >> 10);
							int emY = impactY - 15 + ((emDist * 3 / 5) * CRes.getsin(emAngle) >> 10);
							int emF = (super.f / 2 + em) % p6[5].nFrame;
							p6[5].drawFrame(emF, emX, emY, (em % 2 == 0 ? 0 : 2), 3, g);
						}
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 3: SEQUENTIAL SKY THUNDERBOLTS & CLIMAX 200M VOLT EL THOR
				// ═══════════════════════════════════════════════════════════════════
				if (this.VecEff != null)
				{
					for (int i = 0; i < this.VecEff.size(); i++)
					{
						Point strike = (Point)this.VecEff.elementAt(i);
						if (strike == null) continue;
						int age = super.f - strike.fSmall;

						// ─── TYPE 0: Diagonal Cascading Lightning Bolt ───
						if (strike.subType == 0)
						{
							// In-flight descending lightning fork (age -3..-1)
							if (age >= -3 && age < 0)
							{
								int startX = strike.x + (strike.color == 2 ? -80 : 80);
								int startY = strike.y - 240;
								int midX = (startX + strike.x) / 2 + (strike.color == 2 ? 15 : -15);
								int midY = (startY + strike.y) / 2;
								if (p6[1] != null && p6[1].maxNumFrame > 0)
								{
									int boltF1 = (super.f % 2) * 3 + ((age + 4) % 3);
									int boltF2 = ((super.f + 1) % 2) * 3 + ((age + 5) % 3);
									p6[1].drawFrameNew(boltF1, midX, midY, strike.color, 3, g);
									p6[1].drawFrameNew(boltF2, strike.x, strike.y - 20, strike.color == 2 ? 0 : 2, 3, g);
								}
							}
							// Ground Impact (age 0..8)
							else if (age >= 0 && age <= 8)
							{
								// Dust puff (p6[6]: 152)
								if (p6[6] != null && p6[6].nFrame > 0 && age <= 4)
								{
									int dustF = Math.min(p6[6].nFrame - 1, age / 2);
									p6[6].drawFrame(dustF, strike.x, strike.y, strike.color, 3, g);
								}
								// Ground crackle (p6[3]: 241)
								if (p6[3] != null && p6[3].maxNumFrame > 0)
								{
									int crackleF = (strike.color == 2 ? 2 : 0) + (GameCanvas.gameTick / 2 % 2);
									p6[3].drawFrameNew(crackleF, strike.x, strike.y + 4, 0, 3, g);
								}
								// 3-Segment Vertical Lightning Column (p6[2]: 240)
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 6)
								{
									int colFlip = (age % 2 == 0) ? 0 : 2;
									for (int s = 0; s < 3; s++)
									{
										p6[2].drawFrameNew((super.f + s) % 2, strike.x, strike.y - s * 73, colFlip, 33, g);
									}
								}
								// Starburst Spark (p6[5]: 104)
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 4)
								{
									p6[5].drawFrame(age / 2, strike.x, strike.y - 12, 0, 3, g);
								}
							}
						}

						// ─── TYPE 1: COLOSSAL 200 MILLION VOLT DIVINE BEAM (Climax at f = 36..60) ───
						else if (strike.subType == 1)
						{
							if (age >= 0 && age <= 24)
							{
								// Smooth beam expansion and taper
								int beamW;
								if (age <= 2)
								{
									beamW = 40 + (70 - 40) * age / 2;
								}
								else if (age <= 10)
								{
									beamW = 70 - 10 * (age - 2) / 8;
								}
								else if (age <= 18)
								{
									beamW = 60 - 30 * (age - 10) / 8;
								}
								else
								{
									beamW = Math.max(8, 30 - 22 * (age - 18) / 6);
								}

								int beamJitter = (GameCanvas.gameTick % 2) << 1;
								int topY = strike.y - 420;

								// Layer 1: Deep Royal Plasma Blue Corona (0x0638E1)
								g.setColor(0x0638E1);
								g.fillRect(strike.x - beamW / 2, topY, beamW, 420);

								// Layer 2: Vivid Azure Cyan (0x007CEF)
								if (beamW > 10)
								{
									g.setColor(0x007CEF);
									g.fillRect(strike.x - (beamW - 10) / 2, topY, beamW - 10, 420);
								}

								// Layer 3: Electric Cyan (0x33B5E5)
								if (beamW > 20)
								{
									g.setColor(0x33B5E5);
									g.fillRect(strike.x - (beamW - 20) / 2, topY, beamW - 20, 420);
								}

								// Layer 4: Soft Celestial Glow (0xB1EDFC)
								if (beamW > 30)
								{
									g.setColor(0xB1EDFC);
									g.fillRect(strike.x - (beamW - 30) / 2 + beamJitter, topY, (beamW - 30) - (beamJitter << 1), 420);
								}

								// Layer 5: Ultra White-Cyan Glow (0xE0F7FA)
								if (beamW > 40)
								{
									g.setColor(0xE0F7FA);
									g.fillRect(strike.x - (beamW - 40) / 2 + beamJitter, topY, (beamW - 40) - (beamJitter << 1), 420);
								}

								// Layer 6: Searing White-Hot Core (0xFCFFFE)
								int coreW = Math.max(6, beamW - 48);
								g.setColor(0xFCFFFE);
								g.fillRect(strike.x - coreW / 2 + beamJitter, topY, coreW, 420);

								// Layer 7: Pure White Center (0xFFFFFF)
								g.setColor(0xFFFFFF);
								g.fillRect(strike.x - 2 + (beamJitter / 2), topY, 4, 420);

								// Flanking Giant Lightning Columns (p6[2]: 240) stacked 5 segments high (365px tall)
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 18)
								{
									int flankFlip = (age % 2 == 0) ? 0 : 2;
									for (int s = 0; s < 5; s++)
									{
										p6[2].drawFrameNew((super.f + s) % 2, strike.x - 30, strike.y - s * 73, flankFlip, 33, g);
										p6[2].drawFrameNew((super.f + s + 1) % 2, strike.x + 30, strike.y - s * 73, flankFlip == 0 ? 2 : 0, 33, g);
									}
								}

								// Ground Mega Discharge Rings (p6[4]: 242)
								if (p6[4] != null && p6[4].nFrame > 0 && age <= 22)
								{
									int ringF = (GameCanvas.gameTick / 2) % p6[4].nFrame;
									p6[4].drawFrameNew(ringF, strike.x - 22, strike.y + 6, 0, 3, g);
									p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, strike.x + 22, strike.y + 6, 2, 3, g);
								}

								// Ground Spiderweb Lightning Crawl (p6[3]: 241)
								if (p6[3] != null && p6[3].maxNumFrame > 0 && age <= 22)
								{
									int cF = (GameCanvas.gameTick / 2) % 4;
									p6[3].drawFrameNew(cF, strike.x - 40, strike.y + 6, 0, 3, g);
									p6[3].drawFrameNew((cF + 1) % 4, strike.x + 40, strike.y + 6, 2, 3, g);
								}

								// Giant High-Voltage Electric Arc Spire (p6[7]: 92) surging at epicenter
								if (p6[7] != null && age >= 2 && age <= 18)
								{
									p6[7].drawFrame(0, strike.x, strike.y - 45, 0, 3, g);
									p6[7].drawFrame(0, strike.x - 25, strike.y - 35, 2, 3, g);
									p6[7].drawFrame(0, strike.x + 25, strike.y - 35, 0, 3, g);
								}

								// Exploding Starburst Sparks (p6[5]: 104)
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 18)
								{
									int sparkF = (age / 2) % p6[5].nFrame;
									p6[5].drawFrame(sparkF, strike.x, strike.y - 18, 0, 3, g);
									p6[5].drawFrame((sparkF + 1) % p6[5].nFrame, strike.x - 28, strike.y - 35, 0, 3, g);
									p6[5].drawFrame((sparkF + 2) % p6[5].nFrame, strike.x + 28, strike.y - 35, 0, 3, g);
								}

								// Ground Dust Puffs (p6[6]: 152) surging outward
								if (p6[6] != null && p6[6].nFrame > 0 && age <= 14)
								{
									int dustF = Math.min(p6[6].nFrame - 1, age / 3);
									p6[6].drawFrame(dustF, strike.x - 65, strike.y, 0, 3, g);
									p6[6].drawFrame(dustF, strike.x + 65, strike.y, 2, 3, g);
								}
							}
						}

						// ─── TYPE 2: Residual Aftershock Bolts ───
						else if (strike.subType == 2)
						{
							if (age >= 0 && age <= 8)
							{
								if (p6[3] != null && p6[3].maxNumFrame > 0)
								{
									p6[3].drawFrameNew((strike.color == 2 ? 2 : 0) + (GameCanvas.gameTick / 2 % 2), strike.x, strike.y + 4, strike.color, 3, g);
								}
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 5)
								{
									for (int s = 0; s < 2; s++)
									{
										p6[2].drawFrameNew((super.f + s) % 2, strike.x, strike.y - s * 73, strike.color, 33, g);
									}
								}
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 4)
								{
									p6[5].drawFrame(age / 2, strike.x, strike.y - 10, 0, 3, g);
								}
							}
						}
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 4: CASTER (ENEL) AERIAL RENDERING, CORONA & TELEPORTS (f: 0..68)
				// ═══════════════════════════════════════════════════════════════════
				// Ground discharge ring under caster when charging (f = 2..8)
				if (p6[4] != null && super.f >= 2 && super.f <= 8 && p6[4].nFrame > 0)
				{
					int ringF = (GameCanvas.gameTick / 2) % p6[4].nFrame;
					p6[4].drawFrameNew(ringF, casterX, casterY + 6, 0, 3, g);
				}

				// Ground lightning crackle directly beneath hovering Enel (f = 6..64)
				if (p6[3] != null && p6[3].maxNumFrame > 0 && super.f >= 6 && super.f <= 64)
				{
					int groundF = (super.f % 2) * 2 + (GameCanvas.gameTick / 2 % 2);
					p6[3].drawFrameNew(groundF, casterX, casterY + 6, 0, 3, g);
				}

				// Lightning Dash Flash Streak on Teleport frames (f = 6, 19, 32, 48, 68)
				if (super.f == 6 || super.f == 19 || super.f == 32 || super.f == 48 || super.f == 68)
				{
					g.setColor(0xFFFFFF);
					g.drawLine(casterX, cy, casterX, cy + 50);
					g.drawLine(casterX - 1, cy, casterX - 1, cy + 50);
					g.drawLine(casterX + 1, cy, casterX + 1, cy + 50);
					if (p6[5] != null && p6[5].nFrame > 0)
					{
						p6[5].drawFrame(0, casterX, cy, 0, 3, g);
					}
				}

				// Electric Corona Aura radiating around Enel while levitating
				if (super.f >= 4 && super.f <= 66)
				{
					int tick = GameCanvas.gameTick;
					g.setColor(0x00E5FF);
					for (int r = 0; r < 4; r++)
					{
						int rx = casterX + ((r % 2 == 0 ? -1 : 1) * (14 + (tick * 3 + r * 5) % 12));
						int ry = cy - 26 + ((r < 2 ? -1 : 1) * (8 + (tick * 2 + r * 7) % 14));
						g.drawLine(casterX, cy - 18, rx, ry);
					}
					g.setColor(0xFFFFFF);
					for (int r = 0; r < 4; r++)
					{
						int rx = casterX + ((r % 2 == 0 ? -1 : 1) * (14 + (tick * 3 + r * 5) % 12));
						int ry = cy - 26 + ((r < 2 ? -1 : 1) * (8 + (tick * 2 + r * 7) % 14));
						g.fillRect(rx - 1, ry - 1, 2, 2);
					}
				}

				// Zenith High-Voltage Arc Discharge summoning the El Thor (f = 28..44)
				if (p6[7] != null && super.f >= 28 && super.f <= 44)
				{
					p6[7].drawFrame(0, casterX, cy - 70, (super.f % 2 == 0 ? 0 : 2), 3, g);
				}

				// Triple Orbiting High-Voltage Lightning Spheres (p6[0]: 243)
				if (p6[0] != null && super.f >= 2 && super.f <= 66 && p6[0].nFrame > 0)
				{
					int baseAngle = (super.f * 18) % 360;
					for (int orbIdx = 0; orbIdx < 3; orbIdx++)
					{
						int orbAngle = (baseAngle + orbIdx * 120) % 360;
						int ox = casterX + (CRes.getcos(orbAngle) * 26 >> 10);
						int oy = cy - 16 + (CRes.getsin(orbAngle) * 10 >> 10);
						int orbF = ((super.f / 2) + orbIdx) % p6[0].nFrame;
						p6[0].drawFrame(orbF, ox, oy, 0, 3, g);
						if (p6[5] != null && p6[5].nFrame > 0)
						{
							p6[5].drawFrame(((super.f / 2) + orbIdx) % p6[5].nFrame, ox, oy - 2, 0, 3, g);
						}
					}
				}

				// Target Electrical Shock Sparks while floating / stunned (p6[5]: 104)
				if (p6[5] != null && super.f >= 36 && super.f <= 52 && p6[5].nFrame > 0 && super.vecObjsBeFire != null)
				{
					for (int k = 0; k < super.vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject t = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
						if (t == null || t.isDie || t.Hp <= 0 || t.isRemove) continue;
						int sparkF = (super.f / 2 + k) % p6[5].nFrame;
						p6[5].drawFrame(sparkF, t.x + (k % 2 == 0 ? -6 : 6), t.y + t.dy - 18, 0, 3, g);
					}
				}
			}
		}

		// Paint Set 8: Trafalgar Law - ROOM Gamma Knife (Phẫu thuật Ope Ope no Mi)
		// 393: ROOM sphere quanh nhân vật (1 frame 110x110)
		// 394: 3 frame dọc chia chuẩn xuất hiện ở chân nhân vật
		// 391: Hiệu ứng xuất hiện ở nửa trên mục tiêu dính
		// 392: 3 frame dọc tương tự 394 xuất hiện ở chân mục tiêu dính
		// Không vẽ bóng tròn ở mục tiêu; mục tiêu chỉ bay lên lơ lửng
		if (setId == 8)
		{
			int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
			int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
			int casterCenterY = casterY - (super.objFireMain != null ? (super.objFireMain.hOne / 2) : 22);

			// 1. Vòng sáng ma trận chân nhân vật tung chiêu (394: 3 frame dọc chia chuẩn)
			if (super.fraImgSubEff != null && super.fraImgSubEff.nFrame > 0 && super.f <= 65)
			{
				int f394 = (super.f / 3) % super.fraImgSubEff.nFrame;
				super.fraImgSubEff.drawFrame(f394, casterX, casterY, 0, 33, g);
			}

			// 2. Quả cầu ROOM bao quanh nhân vật tung chiêu (393: 110x110)
			if (super.fraImgEff != null && super.fraImgEff.nFrame > 0 && super.f >= 2 && super.f <= 65)
			{
				super.fraImgEff.drawFrame(0, casterX, casterCenterY, 0, 3, g);
			}

			// 3. Hiệu ứng trên mục tiêu dính đòn (vecObjsBeFire): 392 ở chân, 391 ở nửa trên, vết chém 358
			if (super.vecObjsBeFire != null && super.f >= 3 && super.f <= 68)
			{
				for (int k = 0; k < super.vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (byte)targetInfo.tem);
					if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
					{
						int targetFeetY = target.y - target.dy;
						int targetH = (target.hOne > 0) ? target.hOne : 45;
						int targetUpperY = targetFeetY - (targetH * 3 / 4);

						// 392: 3 frame dọc chia chuẩn xuất hiện ở chân mục tiêu dính
						if (super.fraImgSub3Eff != null && super.fraImgSub3Eff.nFrame > 0)
						{
							int f392 = (super.f / 3) % super.fraImgSub3Eff.nFrame;
							super.fraImgSub3Eff.drawFrame(f392, target.x, targetFeetY, 0, 33, g);
						}

						// 391: Xuất hiện ở nửa trên mục tiêu dính
						if (super.fraImgSub2Eff != null && super.fraImgSub2Eff.nFrame > 0)
						{
							super.fraImgSub2Eff.drawFrame(0, target.x, targetUpperY, 0, 3, g);
						}

						// 358: Vết chém nhỏ ngẫu nhiên trên mục tiêu
						if (super.fraImgSub4Eff != null && super.fraImgSub4Eff.nFrame > 0 && super.f >= 4 && super.f <= 56)
						{
							int slashF = (super.f / 2) % super.fraImgSub4Eff.nFrame;
							int seed = (k * 13 + super.f * 17);
							int offX = (seed % 25) - 12;
							int offY = ((seed / 3) % 21) - 10;
							int trans = (seed % 2 == 0) ? 0 : 2;
							int targetCenterY = targetFeetY - (targetH / 2);
							super.fraImgSub4Eff.drawFrame(slashF, target.x + offX, targetCenterY + offY, trans, 3, g);
						}
					}
				}
			}
		}

		// ─── setId 12: Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) — PAINT ───
		if (setId == 12)
		{
			FrameImage[] p12 = s_ttFrames[12];
			if (p12 != null && p12.length >= 12)
			{
				int facingSign = (this.phoenixTargetX >= this.phoenixOrigX) ? 1 : -1;
				int pDir = (facingSign == 1) ? 2 : 0;

				// ─── LỚP HẠT PARTICLES DƯỚI (VecSubEff: lông vũ & tàn lửa) ───
				if (this.VecSubEff != null)
				{
					for (int i = 0; i < this.VecSubEff.size(); i++)
					{
						Point p = (Point)this.VecSubEff.elementAt(i);
						if (p != null && p.f < p.fRe)
						{
							if (p.subType == 0 && p12[9] != null && p12[9].nFrame > 0)
							{
								// Sacred Rebirth Feathers (224, 22x28, 5f)
								int featherF = (p.f / 3) % p12[9].nFrame;
								p12[9].drawFrame(featherF, p.x, p.y, p.color, 3, g);
							}
							else if (p12[8] != null && p12[8].nFrame > 0)
							{
								// Blue Ground Fire Sparks (241, 40x27, 2f)
								int sparkF = (p.f / 2) % p12[8].nFrame;
								p12[8].drawFrame(sparkF, p.x, p.y, p.color, 3, g);
							}
						}
					}
				}

				// ─── GIAI ĐOẠN 1: TỤ KHÍ & THỨC TỈNH (f = 4..16) ───
				if (super.f >= 4 && super.f < 18)
				{
					int castF = super.f - 4; // 0..13

					// Caster Aura (p12[0]: 474, 120x100, 8f, anchor 33)
					if (p12[0] != null && p12[0].nFrame > 0)
					{
						int auraF = (castF / 2) % p12[0].nFrame;
						p12[0].drawFrame(auraF, this.phoenixOrigX, this.phoenixOrigY, pDir, 33, g);
					}

					// Awakening Phoenix Silhouette behind Caster (p12[1]: 475, 240x180, 8f, anchor 33)
					if (p12[1] != null && p12[1].nFrame > 0 && castF >= 2)
					{
						int awkF = ((castF - 2) / 2) % p12[1].nFrame;
						p12[1].drawFrame(awkF, this.phoenixOrigX + (facingSign * 10), this.phoenixOrigY - 20, pDir, 33, g);
					}

					// Swirling Solar Blue Flame Core (p12[6]: 243, 36x39)
					if (p12[6] != null)
					{
						p12[6].drawFrame(0, this.phoenixOrigX, this.phoenixOrigY - 25, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 2: PHƯỢNG HOÀNG PHI THIÊN PROJECTILE (f = 16..34) ───
				if (super.f >= 16 && super.f <= 34)
				{
					int t = super.f - 16; // 0..18
					int totalT = 18;

					// Interpolation Parabol từ phoenixOrigX/Y tới phoenixTargetX/Y
					int curX = this.phoenixOrigX + (this.phoenixTargetX - this.phoenixOrigX) * t / totalT;
					int linearY = (this.phoenixOrigY - 25) + ((this.phoenixTargetY - 15) - (this.phoenixOrigY - 25)) * t / totalT;
					// Parabola arc: bay vút lên rồi cắm xuống: 4 * h * (t / totalT) * (1 - t / totalT)
					int arcH = 45;
					int curY = linearY - (4 * arcH * t * (totalT - t)) / (totalT * totalT);

					// Ground Shadow (p12[10]: 272, 50x24)
					if (p12[10] != null)
					{
						p12[10].drawFrame(0, curX, this.phoenixOrigY + 2, 0, 3, g);
					}

					// Flame Shockwave trail behind bird (p12[7]: 242, 49x28)
					if (p12[7] != null && p12[7].nFrame > 0 && t % 3 == 0)
					{
						p12[7].drawFrame((t / 3) % p12[7].nFrame, curX - facingSign * 35, curY + 10, pDir, 3, g);
					}

					// Phượng Hoàng Phi Thiên Projectile (p12[2]: 476, 180x80, 8f, anchor 3)
					if (p12[2] != null && p12[2].nFrame > 0)
					{
						int projF = (t / 2) % p12[2].nFrame;
						p12[2].drawFrame(projF, curX, curY, pDir, 3, g);
					}

					// Solar Blue Flame Core in projectile center (p12[6]: 243, 36x39)
					if (p12[6] != null)
					{
						p12[6].drawFrame(0, curX, curY, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 3: CLIMAX IMPACT _X (f = 32..48) ───
				if (super.f >= 32 && super.f <= 48)
				{
					int impAge = super.f - 32; // 0..16

					// Ground Shockwave Ring (p12[7]: 242, 49x28, 2f)
					if (p12[7] != null && p12[7].nFrame > 0 && impAge <= 12)
					{
						int ringF = (impAge / 2) % p12[7].nFrame;
						p12[7].drawFrame(ringF, this.phoenixTargetX - 35, this.phoenixTargetY + 5, 0, 3, g);
						p12[7].drawFrame((ringF + 1) % p12[7].nFrame, this.phoenixTargetX + 35, this.phoenixTargetY + 5, 2, 3, g);
					}

					// Ground Fire Radiating (p12[8]: 241, 40x27, 2f)
					if (p12[8] != null && p12[8].nFrame > 0 && impAge <= 14)
					{
						int fireF = (impAge / 2) % p12[8].nFrame;
						p12[8].drawFrame(fireF, this.phoenixTargetX, this.phoenixTargetY + 4, 0, 3, g);
						p12[8].drawFrame((fireF + 1) % p12[8].nFrame, this.phoenixTargetX - 60, this.phoenixTargetY + 4, 0, 3, g);
						p12[8].drawFrame((fireF + 1) % p12[8].nFrame, this.phoenixTargetX + 60, this.phoenixTargetY + 4, 2, 3, g);
					}

					// Lam Hỏa Đại Bộc Phá _X Impact (p12[3]: 477, 240x240, 10f, anchor 3)
					if (p12[3] != null && p12[3].nFrame > 0)
					{
						int impF = impAge;
						if (impF >= p12[3].nFrame) impF = p12[3].nFrame - 1;
						p12[3].drawFrame(impF, this.phoenixTargetX, this.phoenixTargetY - 40, pDir, 3, g);
					}

					// Starburst Sparks & Flash (p12[11]: 104, 30x30)
					if (p12[11] != null && impAge <= 8)
					{
						int sparkF = (impAge / 2) % 3;
						p12[11].drawFrame(sparkF, this.phoenixTargetX - 35, this.phoenixTargetY - 60, 0, 3, g);
						p12[11].drawFrame((sparkF + 1) % 3, this.phoenixTargetX + 35, this.phoenixTargetY - 60, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 4: TUNG CÁNH PHƯỢNG HOÀNG FINISHER (f = 40..64) ───
				if (super.f >= 40 && super.f <= 64)
				{
					int wingAge = super.f - 40; // 0..24

					// Finisher Wings Sweep (p12[4]: 478, 280x200, 12f, anchor 33)
					if (p12[4] != null && p12[4].nFrame > 0)
					{
						int wingF = wingAge / 2;
						if (wingF >= p12[4].nFrame) wingF = p12[4].nFrame - 1;
						p12[4].drawFrame(wingF, this.phoenixTargetX, this.phoenixTargetY, pDir, 33, g);
					}

					// Swirling Solar Core in chest of Wings (p12[6]: 243, 36x39)
					if (p12[6] != null && wingAge <= 16)
					{
						p12[6].drawFrame(0, this.phoenixTargetX, this.phoenixTargetY - 50, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 5: CỘT LAM HỎA THIÊU ĐỐT TRÊN MỤC TIÊU (f = 46..66) ───
				if (super.f >= 46 && super.f <= 66)
				{
					int geyAge = super.f - 46; // 0..20
					if (p12[5] != null && p12[5].nFrame > 0)
					{
						int geyF = (geyAge / 2);
						if (geyF >= p12[5].nFrame) geyF = p12[5].nFrame - 1;

						if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
						{
							for (int k = 0; k < super.vecObjsBeFire.size() && k < 5; k++)
							{
								Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
								if (tInfo == null) continue;
								MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
								if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
								if (Math.abs(target.x - this.phoenixTargetX) <= 140)
								{
									p12[5].drawFrame(geyF, target.x, target.y, 0, 33, g);
								}
							}
						}
						else
						{
							p12[5].drawFrame(geyF, this.phoenixTargetX, this.phoenixTargetY, 0, 33, g);
						}
					}
				}
			}
		}

		// ─── Paint Set 9: Eustass Kid — Từ Trường Bộc Phá Đại Pháo _X (Straight Laser & Exploding Light Pillar) ───
		if (setId == 9)
		{
			FrameImage[] p9 = (s_ttFrames != null && s_ttFrames.length > 9) ? s_ttFrames[9] : null;
			if (p9 != null && p9.length >= 10)
			{
				int casterX = super.x;
				int casterY = super.y;
				int facingSign = (super.Dir == 2) ? 1 : -1;
				int impactX = (this.kidCannonImpactX != 0) ? this.kidCannonImpactX : super.toX;
				int impactY = (this.kidCannonImpactY != 0) ? this.kidCannonImpactY : super.toY;

				int cannonBaseX = casterX + facingSign * 35;
				int cannonBaseY = casterY - 26;

				// 1. Vòng định vị từ tính dưới chân mục tiêu (f < 18)
				if (super.f < 18 && p9[9] != null && p9[9].nFrame > 0)
				{
					p9[9].drawFrame(0, impactX, impactY + 4, 0, 3, g);
				}

				// 2. Cấu trúc nòng pháo kim loại (f < 32)
				if (super.f < 32)
				{
					// Layer 0: Các vũ khí lớp sau (color == 0)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null && p.color == 0)
							{
								paintKidWeapon(g, p, p9);
							}
						}
					}

					// Lõi plasma từ trường quay cuồng ở khóa nòng (p9[0]: 243)
					if (super.f >= 8 && p9[0] != null && p9[0].nFrame > 0)
					{
						int coreF = (super.f / 2) % p9[0].nFrame;
						p9[0].drawFrame(coreF, cannonBaseX - facingSign * 10, cannonBaseY, 0, 3, g);
					}

					// Tia sét hồ quang điện từ giữa các thanh ray (p9[1]: 92)
					if (super.f >= 8 && p9[1] != null && p9[1].nFrame > 0)
					{
						for (int k = 0; k < 3; k++)
						{
							int arcX = cannonBaseX + facingSign * (k * 14);
							int arcY = cannonBaseY + ((k % 2 == 0) ? -12 : 12);
							int arcF = (super.f / 2 + k) % p9[1].nFrame;
							p9[1].drawFrame(arcF, arcX, arcY, (k % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// Layer 1: Các vũ khí lớp trước (color != 0)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null && p.color != 0)
							{
								paintKidWeapon(g, p, p9);
							}
						}
					}

					// Vòng sóng nén họng pháo khi bắn (f: 14..32)
					int muzzleX = cannonBaseX + facingSign * 45;
					int muzzleY = cannonBaseY;
					if (super.f >= 14 && super.f <= 32)
					{
						if (p9[3] != null && p9[3].nFrame > 0)
						{
							int ringF = ((super.f - 14) / 2) % p9[3].nFrame;
							p9[3].drawFrame(ringF, muzzleX, muzzleY, 0, 3, g);
						}
					}
				}
				else
				{
					// Phase 5: Tán xạ các mảnh kim loại ra xa (f >= 32)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null)
							{
								paintKidWeapon(g, p, p9);
								if (super.f % 2 == 0 && p9[2] != null && p9[2].nFrame > 0)
								{
									p9[2].drawFrame(0, p.x, p.y - 4, 0, 3, g);
								}
							}
						}
					}
				}

				// 3. TIA LAZE BẮN THEO HƯỚNG MỤC TIÊU THẲNG TẮP (f: 14..32)
				if (super.f >= 14 && super.f < 32)
				{
					int muzzleX = cannonBaseX + facingSign * 45;
					int muzzleY = cannonBaseY;
					int endX = impactX + facingSign * 35;
					int endY = impactY;

					int dx = endX - muzzleX;
					int dy = endY - muzzleY;
					int beamDist = (int)Math.sqrt(dx * dx + dy * dy);
					if (beamDist <= 0) beamDist = 1;

					// Vector pháp tuyến đơn vị tỉ lệ 1000
					int perpX = (-dy * 1000) / beamDist;
					int perpY = (dx * 1000) / beamDist;

					// Độ dày chùm tia nở rộng rồi thu gọn
					int beamW;
					if (super.f < 18)
					{
						beamW = 8 + (super.f - 14) * 4;
					}
					else if (super.f < 26)
					{
						beamW = 24 + ((super.f % 2) * 4);
					}
					else
					{
						beamW = Math.max(2, 24 - (super.f - 26) * 4);
					}

					// Tầng 1: Hào quang điện từ xanh ngoại vi (0x007CEF)
					g.setColor(0x007CEF);
					for (int w = -beamW / 2; w <= beamW / 2; w += 2)
					{
						int ox = (w * perpX) / 1000;
						int oy = (w * perpY) / 1000;
						g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
					}

					// Tầng 2: Chùm tia Neon Cyan từ tính (0x00E5FF)
					int innerW = (beamW * 3) / 5;
					if (innerW > 4)
					{
						g.setColor(0x00E5FF);
						for (int w = -innerW / 2; w <= innerW / 2; w += 2)
						{
							int ox = (w * perpX) / 1000;
							int oy = (w * perpY) / 1000;
							g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
						}
					}

					// Tầng 3: Lõi năng lượng trắng sáng chói lòa (0xFFFFFF)
					int coreW = Math.max(2, beamW / 3);
					g.setColor(0xFFFFFF);
					for (int w = -coreW / 2; w <= coreW / 2; w++)
					{
						int ox = (w * perpX) / 1000;
						int oy = (w * perpY) / 1000;
						g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
					}

					// Vòng sóng nén lao dọc theo tia laze
					if (p9[3] != null && p9[3].nFrame > 0)
					{
						for (int s = 0; s < 3; s++)
						{
							int progress = ((super.f * 50 + s * 333) % 1000);
							int sx = muzzleX + (dx * progress) / 1000;
							int sy = muzzleY + (dy * progress) / 1000;
							p9[3].drawFrame((super.f + s) % p9[3].nFrame, sx, sy, 0, 3, g);
						}
					}
				}

				// 4. CỘT SÁNG NỔ ĐI TẠI MỤC TIÊU (f: 18..44)
				if (super.f >= 18 && super.f < 44)
				{
					int pillarAge = super.f - 18;
					int topY = impactY - 400;

					int pillW;
					if (pillarAge <= 3)
					{
						pillW = 28 + pillarAge * 14;
					}
					else if (pillarAge <= 11)
					{
						pillW = 70 - (pillarAge - 4) * 2;
					}
					else
					{
						pillW = Math.max(4, 56 - (pillarAge - 12) * 5);
					}

					int jitter = (GameCanvas.gameTick % 2) << 1;

					// Tầng 1: Hào quang tím xanh đậm (0x0638E1)
					g.setColor(0x0638E1);
					g.fillRect(impactX - pillW / 2, topY, pillW, 400);

					// Tầng 2: Xanh lam từ trường (0x007CEF)
					if (pillW > 10)
					{
						g.setColor(0x007CEF);
						g.fillRect(impactX - (pillW - 10) / 2, topY, pillW - 10, 400);
					}

					// Tầng 3: Laser Neon Cyan rực rỡ (0x00E5FF)
					if (pillW > 20)
					{
						g.setColor(0x00E5FF);
						g.fillRect(impactX - (pillW - 20) / 2, topY, pillW - 20, 400);
					}

					// Tầng 4: Ánh sáng trắng-cyan mềm mại (0xB1EDFC)
					if (pillW > 32)
					{
						g.setColor(0xB1EDFC);
						g.fillRect(impactX - (pillW - 32) / 2 + jitter, topY, (pillW - 32) - (jitter << 1), 400);
					}

					// Tầng 5: Lõi nhiệt hạch trắng tinh khiết (0xFCFFFE)
					int coreW = Math.max(4, pillW - 46);
					g.setColor(0xFCFFFE);
					g.fillRect(impactX - coreW / 2 + jitter, topY, coreW, 400);

					// Trụ cột quang năng bên trong cột sáng (p9[5]: 240, 30x73)
					if (p9[5] != null && p9[5].nFrame > 0)
					{
						int flip = (pillarAge % 2 == 0) ? 0 : 2;
						for (int seg = 0; seg < 5; seg++)
						{
							p9[5].drawFrame(0, impactX, impactY - seg * 73, flip, 33, g);
						}
					}

					// Tia lửa ma sát mặt đất tại tâm nổ (p9[2]: 104)
					if (pillarAge <= 8 && p9[2] != null && p9[2].nFrame > 0)
					{
						p9[2].drawFrame(pillarAge / 2, impactX, impactY - 12, 0, 3, g);
					}
				}
			}
		}

		// ─── setId 10: Cổ Độc Phán Quyết Venom _X (Magellan Hell's Judgment) — CINEMATIC PAINT ───
		if (setId == 10)
		{
			FrameImage[] p10 = (s_ttFrames != null && s_ttFrames.length > 10) ? s_ttFrames[10] : null;
			if (p10 != null && p10.length >= 7)
			{
				int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
				int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;
				int casterDir = super.Dir;

				// Vị trí mục tiêu cố định 1 vị trí đã chốt từ lúc thi triển skill (không trôi theo quái)
				int targetX = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).x : super.toX;
				int targetY = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).y : super.toY;
				if (targetX == 0 && targetY == 0)
				{
					targetX = casterX + (casterDir == 2 ? 140 : -140);
					targetY = casterY;
				}

				// GROUND POOL EFFECT (subType == 1004): Vũng axit độc cố định 1 vị trí tại x, y mặt đất
				if (this.subType == 1004)
				{
					if (super.f >= 35 && super.f <= 70 && p10[4] != null && p10[4].nFrame > 0)
					{
						int poolFrame = Math.min(p10[4].nFrame - 1, (super.f - 35) * p10[4].nFrame / 35);
						p10[4].drawFrame(poolFrame, super.x, super.y, 0, 3, g);
					}
					return;
				}

				// 2. PHASE 1: CASTER AURA (f = 5..25) — Toxic aura around caster feet
				if (super.f >= 5 && super.f <= 25 && p10[0] != null && p10[0].nFrame > 0)
				{
					int auraFrame = (super.f - 5) * p10[0].nFrame / 20;
					if (auraFrame >= p10[0].nFrame) auraFrame = p10[0].nFrame - 1;
					p10[0].drawFrame(auraFrame, casterX, casterY, 0, 33, g);
				}

				// 3. PHASE 2 & 3: HYDRA SUMMON (f = 10..30) — Colossal 3-headed poison dragon
				if (super.f >= 10 && super.f <= 30 && p10[1] != null && p10[1].nFrame > 0)
				{
					int hydraFrame = Math.min(p10[1].nFrame - 1, (super.f - 10) * p10[1].nFrame / 20);
					int hTrans = (casterDir == 2) ? 0 : 2;
					p10[1].drawFrame(hydraFrame, casterX, casterY, hTrans, 33, g);
				}

				// LỚP 4: ẢO ẢNH PHANTOM MAGELLAN (f = 10..26) — Avatar chỉ huy đứng sát lưng Caster
				if (super.f >= 10 && super.f <= 26 && p10[6] != null && p10[6].nFrame > 0)
				{
					int mPose = (super.f <= 17) ? 1 : 2; // Pose 1: Gồng trượng, Pose 2: Đâm trượng chỉ định
					p10[6].drawFrame(mPose, casterX, casterY, (casterDir == 2 ? 0 : 2), 33, g);
				}

				// 5. PHASE 4: VENOM PROJECTILE TRAVEL (f = 24..35) — Flying poison dragon head
				if (super.f >= 24 && super.f <= 35 && p10[2] != null && p10[2].nFrame > 0)
				{
					int pProg = super.f - 24; // 0..11
					int startX = casterX;
					int startY = casterY - 30;
					int endY = targetY - 25;
					int curX = startX + (targetX - startX) * pProg / 11;
					int arc = CRes.getsin(pProg * 180 / 11) * 35 >> 10;
					int curY = startY + (endY - startY) * pProg / 11 - arc;
					int projFrame = (pProg * p10[2].nFrame / 12) % p10[2].nFrame;
					int pTrans = (targetX >= startX) ? 0 : 2;
					p10[2].drawFrame(projFrame, curX, curY, pTrans, 3, g);
				}

				// 6. PHASE 5: VENOM IMPACT X CATACLYSM (f = 35..48) — Giant purple/magenta X explosion
				if (super.f >= 35 && super.f <= 48 && p10[3] != null && p10[3].nFrame > 0)
				{
					int impFrame = Math.min(p10[3].nFrame - 1, (super.f - 35) * p10[3].nFrame / 13);
					p10[3].drawFrame(impFrame, targetX, targetY - 20, 0, 33, g);
				}

				// 7. PHASE 7: POISON BURST GEYSERS (f = 46..68) — Erupting skull geysers at each target
				if (super.f >= 46 && super.f <= 68 && p10[5] != null && p10[5].nFrame > 0)
				{
					int burstFrame = Math.min(p10[5].nFrame - 1, (super.f - 46) * p10[5].nFrame / 22);
					if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
					{
						for (int k = 0; k < super.vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;
							int tBurstF = Math.min(p10[5].nFrame - 1, Math.max(0, burstFrame - (k % 2)));
							p10[5].drawFrame(tBurstF, tObj.x, tObj.y, 0, 3, g);
						}
					}
					else
					{
						p10[5].drawFrame(burstFrame, targetX, targetY, 0, 3, g);
					}
				}
			}
		}

		// Paint Set 13: Đại Phật Sóng Xung Kích _X - Sengoku Daibutsu Authentic Golden Shockwave
		if (setId == 13)
		{
			FrameImage[] parts = (s_ttFrames != null && s_ttFrames.length > 13) ? s_ttFrames[13] : null;
			if (parts != null && parts.length >= 15)
			{
				int casterX = (this.daibutsuCasterX != 0) ? this.daibutsuCasterX : ((super.objFireMain != null) ? super.objFireMain.x : super.x);
				int casterY = (this.daibutsuCasterY != 0) ? this.daibutsuCasterY : ((super.objFireMain != null) ? super.objFireMain.y : super.y);
				int impactX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : super.toX;
				int impactY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : super.toY;
				int dirTrans = (super.Dir == 2) ? 0 : 2;
				int facingSign = (super.Dir == 2) ? 1 : -1;

				if (impactX == 0 && impactY == 0)
				{
					impactX = casterX + facingSign * 180;
					impactY = casterY - 15;
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 1: Ground Seismic Crevasses & Earth Dust Upheaval
				// ─────────────────────────────────────────────────────────────
				// Ground crevasses under caster feet during windup (f <= 22, parts[9]: 246, 49x21)
				if (super.f <= 22 && parts[9] != null)
				{
					parts[9].drawFrameNew((super.f / 2) % 4, casterX, casterY + 6, 0, 3, g);
					parts[9].drawFrameNew(((super.f / 2) + 1) % 4, casterX + facingSign * 35, casterY + 6, dirTrans, 3, g);
				}
				// Ground crevasses under impact epicenter (f 20..44, parts[9]: 246, 49x21)
				if (super.f >= 20 && super.f <= 44 && parts[9] != null)
				{
					int fissF = ((super.f - 20) / 2) % 4;
					parts[9].drawFrameNew(fissF, impactX, impactY + 12, 0, 3, g);
					parts[9].drawFrameNew((fissF + 1) % 4, impactX - 45, impactY + 10, 0, 3, g);
					parts[9].drawFrameNew((fissF + 2) % 4, impactX + 45, impactY + 10, 2, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 2: Caster Daibutsu Manifestation: Shimmering Aura, Dharma Nimbus & Core
				// ─────────────────────────────────────────────────────────────
				// Golden Shimmering Transformation Aura (f <= 25, parts[13]: 267, 47x53)
				if (super.f <= 25 && parts[13] != null)
				{
					int aFlip = (super.f % 2 == 0) ? dirTrans : (dirTrans == 0 ? 2 : 0);
					int aJitterX = (super.f % 2 == 0) ? 1 : -1;
					parts[13].drawFrame(0, casterX + aJitterX, casterY - 20, aFlip, 3, g);
					parts[13].drawFrame(0, casterX, casterY - 20, dirTrans, 3, g);
				}
				// Sacred Dharma Wheel Nimbus (Pháp Luân Kim Quang) spinning smoothly behind caster (f <= 26, parts[4]: 357, 100x100)
				if (super.f <= 26 && parts[4] != null)
				{
					int haloF = (super.f / 2) % 4;
					int haloBobY = (CRes.getsin((super.f * 36) % 360) * 3) >> 10;
					parts[4].drawFrameNew(haloF, casterX - facingSign * 10, casterY - 38 + haloBobY, 0, 3, g);
				}
				// Condensed Golden Solar Core in palm during windup (f <= 11, parts[6]: 174, 40x40)
				if (super.f <= 11 && parts[6] != null)
				{
					int coreF = super.f % 8; // High-speed plasma spin (1 tick per frame)
					int coreX = casterX + facingSign * (18 + (super.f * 6 / 11));
					int coreY = casterY - 20;
					parts[6].drawFrameNew(coreF, coreX, coreY, 0, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 3: Kim Cương Phật Chưởng Thrust & Shockwave Burst Snap (f 10..22)
				// ─────────────────────────────────────────────────────────────
				if (super.f >= 10 && super.f <= 22 && parts[0] != null)
				{
					int palmProgress = super.f - 10;
					int palmOffset;
					if (palmProgress <= 0) palmOffset = 0;
					else if (palmProgress == 1) palmOffset = 24;
					else if (palmProgress == 2) palmOffset = 50;
					else if (palmProgress == 3) palmOffset = 68;
					else palmOffset = Math.min(78, 68 + (palmProgress - 3) * 2);

					int palmX = casterX + facingSign * (26 + palmOffset);
					int palmY = casterY - 20;
					int palmF = Math.min(3, palmProgress / 2);

					// Dynamic Kinetic Ghost Trails (After-Images)
					if (palmProgress >= 1 && palmProgress <= 6)
					{
						parts[0].drawFrame(Math.max(0, palmF - 1), palmX - facingSign * 16, palmY, dirTrans, 3, g);
						parts[0].drawFrame(Math.max(0, palmF - 2), palmX - facingSign * 32, palmY, dirTrans, 3, g);
					}
					// Main Golden Palm Thrust Wave (parts[0]: 416, 78x40)
					parts[0].drawFrame(palmF, palmX, palmY, dirTrans, 3, g);

					// Palm Blast Burst Cone pulsing with rapid alternation (parts[7]: 335, 80x80)
					if (parts[7] != null && super.f >= 11 && super.f <= 20)
					{
						int blastF = (super.f - 11) % 4;
						parts[7].drawFrameNew(blastF, palmX + facingSign * 32, palmY, dirTrans, 3, g);
					}
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 4: Progressive Traveling Kinetic Shockwave with Cubic Easing Out (f 12..21)
				// ─────────────────────────────────────────────────────────────
				if (super.f >= 12 && super.f <= 21 && parts[1] != null)
				{
					int startX = casterX + facingSign * 48;
					int t = super.f - 12; // 0..9
					// Cubic Ease-Out acceleration: 0%, 34%, 58%, 75%, 88%, 95%, 98%, 100%
					int easeRatio;
					if (t <= 0) easeRatio = 0;
					else if (t == 1) easeRatio = 87;
					else if (t == 2) easeRatio = 148;
					else if (t == 3) easeRatio = 193;
					else if (t == 4) easeRatio = 224;
					else if (t == 5) easeRatio = 243;
					else if (t == 6) easeRatio = 252;
					else if (t == 7) easeRatio = 255;
					else easeRatio = 256;

					int distX = impactX - startX;
					int distY = impactY - (casterY - 20);
					int ringX = startX + (distX * easeRatio) / 256;
					int ringY = (casterY - 20) + (distY * easeRatio) / 256;
					int waveJitter = (t % 2 == 0) ? 1 : -1;
					int ringF = Math.min(3, t / 2);

					// 1. Leading Compression Arc Wave (parts[0]: 416)
					if (parts[0] != null)
					{
						parts[0].drawFrame(ringF, ringX + facingSign * 24, ringY, dirTrans, 3, g);
					}

					// 2. Primary Golden Shockwave Ring (parts[1]: 171, 153x84) with vibration
					parts[1].drawFrame(ringF, ringX, ringY + waveJitter, dirTrans, 3, g);

					// 3. Secondary Concentric Echo Ring trailing 24px behind
					if (t >= 1)
					{
						int echoX = ringX - facingSign * 24;
						parts[1].drawFrame(Math.max(0, ringF - 1), echoX, ringY, dirTrans, 3, g);
					}

					// 4. Ground Elliptical Ring sweeping along floor (parts[3]: 394, 126x41)
					if (parts[3] != null)
					{
						parts[3].drawFrame(t % 3, ringX, casterY + 2, 0, 3, g);
					}
					// 5. Space Distortion Wave vibrating atmosphere (parts[10]: 285, 111x90)
					if (parts[10] != null)
					{
						int dFlip = (t % 2 == 0) ? dirTrans : (dirTrans == 0 ? 2 : 0);
						parts[10].drawFrame((t * 2) % 3, ringX - facingSign * 10, ringY, dFlip, 3, g);
					}
					// 6. Ground Seismic Dust Upheaval Cascade sweeping directly under wave (parts[8]: 300, 80x25)
					if (parts[8] != null)
					{
						parts[8].drawFrameNew((t * 2) % 3, ringX, casterY + 4, dirTrans, 33, g);
					}
					// 7. Starburst Sparks on Wave Crest (parts[12]: 104, 30x30)
					if (parts[12] != null)
					{
						parts[12].drawFrame(t % 3, ringX + facingSign * 42, ringY - 18, 0, 3, g);
						parts[12].drawFrame((t + 1) % 3, ringX + facingSign * 42, ringY + 18, 2, 3, g);
					}
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 5: Epicenter Cataclysm & Detonation at (impactX, impactY) (f 21..36)
				// ─────────────────────────────────────────────────────────────
				// Ground Elliptical Shockwave Ring (parts[3]: 394, 126x41)
				if (super.f >= 20 && super.f <= 36 && parts[3] != null)
				{
					int gF = ((super.f - 20) / 2) % 3;
					parts[3].drawFrame(gF, impactX, impactY + 12, 0, 3, g);
				}

				// Colossal Mega Shockwave Explosion (parts[2]: 453, 169x126) (f 21..34)
				if (super.f >= 21 && super.f <= 34 && parts[2] != null)
				{
					int blastAge = super.f - 21;
					int megaF = (blastAge < 3) ? 0 : ((blastAge < 7) ? 1 : 2);
					int megaJitter = (super.f % 2 == 0) ? 1 : -1;
					parts[2].drawFrame(megaF, impactX + megaJitter, impactY - 10, 0, 3, g);
				}

				// Radial Space Distortion Wave with Alternating Flip (parts[10]: 285, 111x90) (f 21..32)
				if (super.f >= 21 && super.f <= 32 && parts[10] != null)
				{
					int distF = ((super.f - 21) / 2) % 3;
					int distFlip = ((super.f - 21) % 2 == 0) ? 0 : 2;
					parts[10].drawFrame(distF, impactX, impactY - 15, distFlip, 3, g);
				}

				// Blinding White-Gold Core Flash (parts[11]: 66, 75x55) (f 21..24)
				if (super.f >= 21 && super.f <= 24 && parts[11] != null)
				{
					parts[11].drawFrame(0, impactX, impactY - 15, 0, 3, g);
					if (super.vecObjsBeFire != null)
					{
						for (int k = 1; k < super.vecObjsBeFire.size(); k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
							if (tInfo != null)
							{
								MainObject tObj = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
								if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
								{
									parts[11].drawFrame(0, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
								}
							}
						}
					}
				}

				// Diamond Starburst Sparks 4-Way Detonation Expanding with Velocity (parts[12]: 104, 30x30) (f 22..35)
				if (super.f >= 22 && super.f <= 35 && parts[12] != null)
				{
					int spAge = super.f - 22;
					int spDist = spAge * 5;
					int spF = (spAge / 2) % 3;
					parts[12].drawFrame(spF, impactX - 30 - spDist, impactY - 20 - spDist, 0, 3, g);
					parts[12].drawFrame((spF + 1) % 3, impactX + 30 + spDist, impactY - 20 - spDist, 2, 3, g);
					parts[12].drawFrame((spF + 2) % 3, impactX - 20 - spDist, impactY + 10 + spDist / 2, 0, 3, g);
					parts[12].drawFrame(spF, impactX + 20 + spDist, impactY + 10 + spDist / 2, 2, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 6: Divine Lotus Bloom & Sacred Dharma Wheel Resonance (f 21..44)
				// ─────────────────────────────────────────────────────────────
				// Sacred Dharma Wheel Mandala Nimbus at Epicenter (parts[4]: 357, 100x100) (f 21..38)
				if (super.f >= 21 && super.f <= 38 && parts[4] != null)
				{
					int wheelF = ((super.f - 21) / 2) % 4;
					parts[4].drawFrameNew(wheelF, impactX, impactY - 25, 0, 3, g);
				}

				// Divine Lotus Bloom Khai Hoa Kim Liên (parts[5]: 315, 77x54) (f 21..44)
				// Full biological blooming stages 0..5 (bud -> opening -> bloom -> full radiance)
				if (super.f >= 21 && super.f <= 44 && parts[5] != null)
				{
					int lotusF = Math.min(5, (super.f - 21) / 4);
					parts[5].drawFrameNew(lotusF, impactX, impactY + 6, 0, 3, g);
				}

				// 6 Sacred Golden Nirvana Embers with Multi-Harmonic Sinusoidal Sway (parts[14]: 224, 22x28) (f 24..45)
				if (super.f >= 24 && super.f <= 45 && parts[14] != null)
				{
					for (int e = 0; e < 6; e++)
					{
						int eAge = super.f - 24 + e * 3;
						int driftUp = eAge * 3;
						int angle = (eAge * 28 + e * 60) % 360;
						int swayX = (CRes.getsin(angle) * (14 + (e % 3) * 4)) >> 10;
						int embF = (eAge / 2) % 5;
						int eBaseX = impactX - 50 + e * 20;
						parts[14].drawFrameNew(embF, eBaseX + swayX, impactY - 15 - driftUp, (e % 2 == 0 ? 0 : 2), 3, g);
					}
				}
			}
		}

		// Paint caster charging aura during initial casting frames
		if (super.objFireMain != null && super.f < 15 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13)
		{
			if (setId == 1 && super.fraImgSub2Eff != null && super.fraImgSub2Eff.nFrame > 0)
			{
				super.fraImgSub2Eff.drawFrame(0, super.objFireMain.x, super.objFireMain.y + 4, 0, 3, g);
			}
			if (super.fraImgEff != null && super.fraImgEff.nFrame > 0)
			{
				int frameIdx = (super.f / 2) % super.fraImgEff.nFrame;
				super.fraImgEff.drawFrame(frameIdx, super.objFireMain.x + (super.Dir == 2 ? 16 : -16), super.objFireMain.y - 15, super.Dir == 2 ? 0 : 2, 3, g);
			}
		}

		// Paint active flying projectiles with directional orientation & glowing dual layer
		if (this.VecSubEff != null && setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point_Focus projectile = (Point_Focus)this.VecSubEff.elementAt(i);
				if (projectile != null)
				{
					if (super.fraImgSubEff != null && super.fraImgSubEff.nFrame > 0)
					{
						int frameIdx = (projectile.f / 2) % super.fraImgSubEff.nFrame;
						super.fraImgSubEff.drawFrame(frameIdx, projectile.x, projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
					if (setId == 1 && super.fraImgSub3Eff != null && super.fraImgSub3Eff.nFrame > 0)
					{
						int frameIdx = (projectile.f / 2) % super.fraImgSub3Eff.nFrame;
						super.fraImgSub3Eff.drawFrame(frameIdx, projectile.x + (projectile.Dir == 2 ? 10 : -10), projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
					if (super.fraImgEff != null && super.fraImgEff.nFrame > 0)
					{
						int frameIdx = ((projectile.f / 2) + 1) % super.fraImgEff.nFrame;
						super.fraImgEff.drawFrame(frameIdx, projectile.x, projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
				}
			}
		}
		}
		catch (Exception e)
		{
		}
	}

	private void paintKidWeapon(mGraphics g, Point p, FrameImage[] p9)
	{
		if (p == null || g == null) return;
		boolean drawn = false;
		try
		{
			MainImage m = ObjectData.getImageAll((short)p.subType, ObjectData.hashImageItem, (short)3000);
			if (m != null && m.img != null && m.img.image != null)
			{
				if (m.AB <= 0 || m.AC <= 0)
				{
					m.AA();
				}
				if (m.AB > 0 && m.AC > 0)
				{
					g.drawRegion(m.img, 0, 0, m.AB, m.AC, p.frame % 8, p.x, p.y, 3);
					drawn = true;
				}
			}
		}
		catch (Exception ignored)
		{
		}

		// Fallback to authentic metallic blade glint (p9[8]: 358, 51x22)
		if (!drawn && p9 != null && p9.length > 8 && p9[8] != null && p9[8].nFrame > 0)
		{
			p9[8].drawFrame(0, p.x, p.y, p.frame % 4, 3, g);
		}
	}

	// =========================================================================
	// DOKU DOKU NO MI DEVIL FRUIT SYSTEM (SKILL 4017, 4018, 4019)
	// ID 4017: Cổ Độc Phán Quyết (Y hệt 4010 Thần Trang Magellan)
	// ID 4018: Bách Độc Vũ Mưa Độc Tách Tầng Chuẩn Xịn (Decoupled Toxic Rain)
	// ID 4019: Độc Long Thức Tỉnh (Thuần Gồng Buff Siêu Tốc 0.2s Frame 6)
	// =========================================================================

	private static FrameImage s_imgRainDrop;      // ID 474 (32x48, 6f) - Mưa Độc Long giáng xuống
	private static FrameImage s_imgRainSplash;    // ID 475 (64x36, 6f) - Bộc phá tiếp đất
	private static FrameImage s_imgPoisonBubbles; // ID 476 (30x30, 8f) - Khí độc dập dềnh
	private static FrameImage s_imgAuraDragon;    // ID 477 (100x120, 8f) - Song Long Hỏa Trụ sau lưng Caster
	private static FrameImage s_imgAcidPool4018;  // ID 478 (110x50, 6f) - Vũng axit mặt đất dưới chân mục tiêu

	private static void ensureVenomSkillFrames()
	{
		ensureVenomSkillFrames(false);
	}

	private static void ensureVenomSkillFrames(boolean forceLoad)
	{
		if (forceLoad || s_imgRainDrop == null || s_imgRainDrop.nFrame != 6)
		{
			s_imgRainDrop = new FrameImage(474, 6);
		}
		if (forceLoad || s_imgRainSplash == null || s_imgRainSplash.nFrame != 6)
		{
			s_imgRainSplash = new FrameImage(475, 6);
		}
		if (forceLoad || s_imgPoisonBubbles == null || s_imgPoisonBubbles.nFrame != 8)
		{
			s_imgPoisonBubbles = new FrameImage(476, 8);
		}
		if (forceLoad || s_imgAuraDragon == null || s_imgAuraDragon.nFrame != 8)
		{
			s_imgAuraDragon = new FrameImage(477, 8);
		}
		if (forceLoad || s_imgAcidPool4018 == null || s_imgAcidPool4018.nFrame != 6)
		{
			s_imgAcidPool4018 = new FrameImage(478, 6);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// SKILL 4018: BÁCH ĐỘC VŨ (VENOM DRAGON RAIN & ACID POOL UNDER TARGETS)
	// ─────────────────────────────────────────────────────────────────────────

	private void createVenomRain4018()
	{
		this.VecEff = new mVector();
		this.VecSubEff = new mVector();
		this.isSpawnedPool = false; // Flag: vũng độc ngầm chưa tạo
		ensureVenomSkillFrames(true);
		super.fRemove = 65;
		super.levelPaint = 0; // TẦNG TRÊN (0): GIỌT MƯA VÀ IMPACT TIẾP ĐẤT RƠI TRÊN MẶT ĐẤT & TRÊN MỤC TIÊU

		// 1. Ưu tiên tuyệt đối ghim chuẩn giữa mục tiêu đang chọn (GameScreen.objFocus)
		MainObject target = null;
		if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != super.objFireMain)
		{
			target = GameScreen.objFocus;
		}
		else if (this.objBeFireMain != null && this.objBeFireMain != super.objFireMain && !this.objBeFireMain.isDie && !this.objBeFireMain.isRemove)
		{
			target = this.objBeFireMain;
		}
		else if (super.vecObjsBeFire != null && super.vecObjsBeFire.size() > 0)
		{
			for (int i = 0; i < super.vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill o = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(i);
				if (o != null)
				{
					MainObject mob = MainObject.get_Object((int)o.ID, (byte)o.tem);
					if (mob != null && !mob.isDie && !mob.isRemove && mob != super.objFireMain)
					{
						target = mob;
						break;
					}
				}
			}
		}

		if (target != null)
		{
			this.objBeFireMain = target;
			super.toX = target.x;
			super.toY = target.y;
			if (super.objFireMain != null)
			{
				super.Dir = (byte)((target.x >= super.objFireMain.x) ? 2 : 0);
				super.objFireMain.type_left_right = super.Dir;
				super.objFireMain.Dir = super.Dir;
			}
		}

		// Xác định vị trí mặt đất chuẩn dưới chân mục tiêu (bỏ dyShadow âm làm trôi cao)
		int groundY = (target != null) ? target.y : ((super.toY != 0) ? super.toY : super.y);
		int mainTargetX = (target != null) ? target.x : ((super.toX != 0) ? super.toX : (super.x + ((super.Dir == 2) ? 140 : -140)));
		super.toX = mainTargetX;
		super.toY = groundY;

		// 1. Tạo bão Mưa Độc Long dày đặc (474: 36x48) rơi liên tục từ f=3 đến f=50 (70 giọt phân bố ngẫu nhiên cách đều, sinh động)
		int totalDrops = 70;
		for (int i = 0; i < totalDrops; i++)
		{
			Point drop = new Point();
			// Khởi hành rải đều liên tục từ f=3 đến f=48 với độ lệch ngẫu nhiên
			int fStart = 3 + (i * 45 / totalDrops) + ((i * 3) % 4 - 1);
			if (fStart < 3) fStart = 3;
			int fallDur = 6 + (i % 6); // 6..11 frames rơi vũ bão, tốc độ đa dạng sinh động
			drop.fSmall = fStart;
			drop.fRe = fStart + fallDur;

			// Tọa độ xuất phát trên cao theo góc chéo tự nhiên ~48..56 độ:
			int spawnDistY = 175 + ((i * 19) % 55); // 175..229 px
			int spawnAngleRatio = 125 + ((i * 7) % 30); // 125..154
			int spawnDistX = spawnDistY * spawnAngleRatio / 180;
			drop.dis = (super.Dir == 2) ? -spawnDistX : spawnDistX;
			drop.vy = spawnDistY;

			// Điểm tiếp đất trải rộng ngẫu nhiên khắp khu vực đầm lầy độc (-115..+115, -16..+18)
			int landOffsetX = -115 + ((i * 53 + 17) % 231) + ((i * 5) % 9 - 4);
			int landOffsetY = -16 + ((i * 23 + 11) % 35) + ((i * 3) % 7 - 3);
			drop.color = landOffsetX;
			drop.frame = landOffsetY;
			drop.x2 = mainTargetX + drop.color;
			drop.y2 = groundY + drop.frame;
			drop.obj = null; // CỐ ĐỊNH 1 VỊ TRÍ TRÊN MẶT ĐẤT, KHÔNG TRÔI THEO QUÁI
			drop.f = 0; // Đếm frame hiệu ứng bắn tóe tiếp đất (rain_splash)
			drop.AW = false; // false: đang rơi, true: tiếp đất bộc phá
			this.VecSubEff.addElement(drop);
		}

		// 2. Bong bóng độc (476: 30x30) dập dềnh trên khắp mặt hồ vũng độc (16 bóng trải rộng)
		int[] bXOffsets = new int[] { -82, -68, -52, -38, -25, -12, 0, 14, 26, 38, 52, 66, 80, -45, 18, 58 };
		int[] bYOffsets = new int[] { 3, 9, -7, 6, -9, 8, 2, -6, 9, 3, -8, 7, 2, 11, -5, 5 };
		for (int b = 0; b < 16; b++)
		{
			Point bubble = new Point();
			int bxOff = bXOffsets[b] + ((b * 5) % 9 - 4);
			int byOff = bYOffsets[b] + ((b * 3) % 7 - 3);
			bubble.x = mainTargetX + bxOff;
			bubble.y = groundY + byOff;
			bubble.x2 = bubble.x;
			bubble.y2 = bubble.y;
			bubble.color = bxOff;
			bubble.subType = byOff;
			bubble.obj = null; // CỐ ĐỊNH 1 VỊ TRÍ TRÊN MẶT ĐẤT, KHÔNG TRÔI THEO QUÁI
			bubble.fSmall = 10 + (b * 2); // Nổi lên dập dềnh so le
			bubble.fRe = bubble.fSmall + 46;
			bubble.f = 0;
			bubble.frame = (b * 3) % 8;
			this.VecEff.addElement(bubble);
		}

		this.addSound(10);
	}

	private void updateVenomRain4018()
	{
		// Xử lý Companion Sub-effect: CỤM NHIỀU VŨNG ĐỘC DƯỚI CHÂN MỤC TIÊU (subType == 1008, levelPaint == -1)
		// CỐ ĐỊNH 100% VỊ TRÍ MẶT ĐẤT ĐÃ TẠO, KHÔNG DI CHUYỂN THEO QUÁI
		if (this.subType == 1008)
		{
			if (super.f >= super.fRemove)
			{
				this.removeEff();
			}
			return;
		}

		// Âm thanh và rung chấn ở từng đợt giáng thế của mưa độc long
		if (super.f == 8)
		{
			this.addSound(10);
		}
		else if (super.f == 12 || super.f == 18 || super.f == 24 || super.f == 30 || super.f == 36 || super.f == 42 || super.f == 48)
		{
			this.addSound(51);
			LoadMap.timeVibrateScreen = 4;
			if (super.vecObjsBeFire != null)
			{
				for (int k = 0; k < super.vecObjsBeFire.size() && k < 4; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
					if (tInfo == null) continue;
					MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
					this.setAva(2, target);
					target.dy = -3;
				}
			}
		}

		// TẠO CỤM NHIỀU VŨNG ĐỘC NGẪU NHIÊN CÁCH NHAU ĐẸP MẮT ĐAN XEN DƯỚI CHÂN MỤC TIÊU KHI GIỌT ĐẦU TIẾP ĐẤT (f >= 10)
		// ĐẶC BIỆT: levelPaint = -1 (LỚP TILE MAP, DƯỚI TẤT CẢ CHAR VÀ QUÁI - CHAR VÀ QUÁI Ở TRÊN)
		if (super.f >= 10 && !this.isSpawnedPool)
		{
			this.isSpawnedPool = true;
			int poolX = super.toX;
			int poolY = super.toY + 4; // Hạ thấp 4px sát chân/bóng đất
			Effect_Skill groundPool = new Effect_Skill();
			groundPool.typeEffect = 4018;
			groundPool.subType = 1008; // 1008 = Ground acid pool cluster companion
			groundPool.levelPaint = -1; // VẼ TRÊN LỚP TILE MAP, DƯỚI TẤT CẢ MỤC TIÊU & NHÂN VẬT!
			groundPool.x = poolX;
			groundPool.y = poolY;
			groundPool.toX = poolX;
			groundPool.toY = poolY;
			groundPool.Dir = super.Dir;
			groundPool.objFireMain = (super.objFireMain != null) ? super.objFireMain : GameScreen.player;
			groundPool.objBeFireMain = this.objBeFireMain;
			groundPool.VecEff = this.VecEff;
			groundPool.VecSubEff = new mVector();

			// Cấu hình cụm 12 vũng độc ngẫu nhiên, cách nhau thoáng, không trùng lặp, mở rộng thành đầm lầy độc:
			// { offsetX, offsetY, trans (0 hoặc 2), phase (0..5), delayFrame (fSmall) }
			int[][] poolConfigs = new int[][]
			{
				new int[] { 0, 0, 0, 0, 0 },     // Vũng 0: Tâm chính
				new int[] { -32, 5, 2, 2, 1 },    // Vũng 1: Bên trái gần
				new int[] { 35, -3, 0, 4, 2 },    // Vũng 2: Bên phải gần
				new int[] { 10, 8, 2, 1, 3 },     // Vũng 3: Phía trước trung tâm
				new int[] { -12, -7, 0, 3, 4 },   // Vũng 4: Phía sau trung tâm
				new int[] { -60, -4, 2, 5, 5 },   // Vũng 5: Cánh trái trung bình
				new int[] { 62, 6, 0, 0, 6 },     // Vũng 6: Cánh phải trung bình
				new int[] { -86, 5, 2, 3, 8 },    // Vũng 7: Rìa trái xa
				new int[] { 88, -4, 0, 1, 9 },    // Vũng 8: Rìa phải xa
				new int[] { -46, 11, 0, 4, 10 },  // Vũng 9: Vạt dưới trái
				new int[] { 44, -10, 2, 2, 11 },  // Vũng 10: Vạt trên phải
				new int[] { -15, 13, 2, 5, 12 }   // Vũng 11: Mép tiền tuyến
			};
			for (int pIdx = 0; pIdx < poolConfigs.length; pIdx++)
			{
				Point p = new Point();
				p.color = poolConfigs[pIdx][0] + CRes.random_Am_0(6);   // offsetX ngẫu nhiên
				p.subType = poolConfigs[pIdx][1] + CRes.random_Am_0(3); // offsetY ngẫu nhiên
				p.dis = poolConfigs[pIdx][2];                           // trans (0=bình thường, 2=lật ngang)
				p.frame = poolConfigs[pIdx][3];                         // phase offset (bọt khí không trùng nhịp)
				p.fSmall = poolConfigs[pIdx][4];                        // delay frame xuất hiện lan tỏa
				groundPool.VecSubEff.addElement(p);
			}

			groundPool.f = 0;
			groundPool.fRemove = 55; // 10 + 55 = 65 (kết thúc cùng lúc với skill)
			GameScreen.VecEffect.addElement(groundPool);
		}

		// Cập nhật giọt mưa độc rơi và bộc phá tiếp đất
		if (this.VecSubEff != null)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point drop = (Point)this.VecSubEff.elementAt(i);
				if (drop == null || super.f < drop.fSmall) continue;

				if (super.f >= drop.fRe)
				{
					drop.AW = true;
					drop.f++; // Đếm frame hoạt ảnh bắn tóe (rain_splash)
				}
			}
		}

		// Cập nhật bong bóng độc dập dềnh trên mặt vũng độc (không bay vọt lên trời)
		if (this.VecEff != null)
		{
			for (int i = 0; i < this.VecEff.size(); i++)
			{
				Point bubble = (Point)this.VecEff.elementAt(i);
				if (bubble == null || super.f < bubble.fSmall || super.f >= bubble.fRe) continue;
				bubble.f++;
				int sine = CRes.getsin(((bubble.f + bubble.frame * 8) * 24) % 360) * 2 >> 10;
				bubble.y = bubble.y2 + sine;
			}
		}

		if (super.f >= 55 && super.vecObjsBeFire != null)
		{
			for (int k = 0; k < super.vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(k);
				if (tInfo != null)
				{
					MainObject target = MainObject.get_Object((int)tInfo.ID, (byte)tInfo.tem);
					if (target != null) target.dy = 0;
				}
			}
		}

		if (super.f >= super.fRemove)
		{
			this.removeEff();
		}
	}

	private void paintVenomRain4018(mGraphics g)
	{
		if (g == null) return;
		ensureVenomSkillFrames();

		// 1. NẾU LÀ COMPANION SUB-EFFECT (subType == 1008, levelPaint == -1):
		// VẼ CỤM NHIỀU VŨNG AXIT NGẪU NHIÊN GẦN NHAU DƯỚI CHÂN MỤC TIÊU CHÍNH (ANCHOR 3, LEVELPAINT = -1, DƯỚI TẤT CẢ CHAR VÀ QUÁI)
		if (this.subType == 1008)
		{
			if (s_imgAcidPool4018 != null && s_imgAcidPool4018.nFrame > 0)
			{
				if (this.VecSubEff != null && this.VecSubEff.size() > 0)
				{
					for (int pIdx = 0; pIdx < this.VecSubEff.size(); pIdx++)
					{
						Point pool = (Point)this.VecSubEff.elementAt(pIdx);
						if (pool == null || super.f < pool.fSmall) continue;
						int poolAge = super.f - pool.fSmall;
						int poolF = (poolAge < 8) ? (poolAge * s_imgAcidPool4018.nFrame / 8) : (((poolAge / 2) + pool.frame) % s_imgAcidPool4018.nFrame);
						int px = super.x + pool.color;
						int py = super.y + pool.subType;
						s_imgAcidPool4018.drawFrame(poolF % s_imgAcidPool4018.nFrame, px, py, pool.dis, 3, g);
					}
				}
				else
				{
					int poolF = (super.f < 8) ? (super.f * s_imgAcidPool4018.nFrame / 8) : ((super.f / 2) % s_imgAcidPool4018.nFrame);
					s_imgAcidPool4018.drawFrame(poolF % s_imgAcidPool4018.nFrame, super.x, super.y, 0, 3, g);
				}
			}
			// Bong bóng độc mặt đất cũng vẽ ở tầng ngầm này để nằm dưới chân nhân vật và quái
			if (s_imgPoisonBubbles != null && s_imgPoisonBubbles.nFrame > 0 && this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point bubble = (Point)this.VecEff.elementAt(i);
					if (bubble == null || super.f < bubble.fSmall || super.f >= bubble.fRe) continue;
					int bFrame = ((bubble.f / 2) + bubble.frame) % s_imgPoisonBubbles.nFrame;
					s_imgPoisonBubbles.drawFrame(bFrame, bubble.x, bubble.y, 0, 3, g);
				}
			}
			return;
		}

		// 2. VƯƠNG MIỆN BỘC PHÁ TIẾP ĐẤT (rain_splash: 475, 64x36, ANCHOR 3 TẠI TIẾP ĐẤT SÁT MẶT ĐẤT)
		if (s_imgRainSplash != null && s_imgRainSplash.nFrame > 0 && this.VecSubEff != null)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point drop = (Point)this.VecSubEff.elementAt(i);
				if (drop == null || !drop.AW) continue;
				if (drop.f >= 0 && drop.f < s_imgRainSplash.nFrame)
				{
					s_imgRainSplash.drawFrame(drop.f, drop.x2, drop.y2, 0, 3, g);
				}
			}
		}

		// 3. GIỌT MƯA ĐỘC LONG BAY CHÉO XUỐNG ĐẤT (rain_drop: 474, 36x48, ANCHOR 3)
		// Quỹ đạo rơi chéo từ (lx + drop.dis, ly - drop.vy) tiếp đất tại (lx, ly)
		if (s_imgRainDrop != null && s_imgRainDrop.nFrame > 0 && this.VecSubEff != null)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point drop = (Point)this.VecSubEff.elementAt(i);
				if (drop == null || drop.AW || super.f < drop.fSmall || super.f >= drop.fRe) continue;

				int lx = drop.x2;
				int ly = drop.y2;

				int p = super.f - drop.fSmall;
				int dur = drop.fRe - drop.fSmall;
				if (dur < 1) dur = 1;
				int sx = lx + drop.dis;
				int sy = ly - drop.vy;

				int curX = sx + (lx - sx) * p / dur;
				int curY = sy + (ly - sy) * p / dur;

				int dropF = (p * s_imgRainDrop.nFrame / dur) % s_imgRainDrop.nFrame;
				int trans = (super.Dir == 2) ? 0 : 2;
				int headOffX = (super.Dir == 2) ? 6 : -6;
				s_imgRainDrop.drawFrame(dropF, curX - headOffX, curY, trans, 3, g);
			}
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// SKILL 4019: ĐỘC LONG THỨC TỈNH (BUFF SAU LƯNG NHÂN VẬT, 0.2s KÍCH HOẠT)
	// ─────────────────────────────────────────────────────────────────────────

	private void createVenomBuff4019()
	{
		ensureVenomSkillFrames(true);
		super.fRemove = 999999;
		super.levelPaint = -1; // VẼ SAU LƯNG NHÂN VẬT THEO YÊU CẦU NGƯỜI DÙNG!
		super.timeBegin = GameCanvas.timeNow;

		// Tính toán thời gian tác dụng buff chuẩn từ tham số kỹ năng
		int buffMs = 0;
		if (super.timeEnd > 0)
		{
			buffMs = (super.timeEnd < 1000) ? (super.timeEnd * 100) : (int)super.timeEnd;
		}
		if (buffMs <= 0 && this.skill != null)
		{
			if (this.skill.AF > 0)
			{
				buffMs = (this.skill.AF < 1000) ? (this.skill.AF * 100) : (int)this.skill.AF;
			}
			Skill_Info sk = Skill_Info.getSkillFromID(this.skill.ID);
			if (sk != null && sk.vecAtt != null)
			{
				for (int i = 0; i < sk.vecAtt.size(); i++)
				{
					MainInfoItem item = (MainInfoItem)sk.vecAtt.elementAt(i);
					if (item != null && item.AA == 32 && item.AE > 0)
					{
						int val = (item.AE < 1000) ? (item.AE * 100) : item.AE;
						if (val > buffMs) buffMs = val;
						break;
					}
				}
				if (buffMs <= 0 && sk.timeEffSpec > 0)
				{
					buffMs = (sk.timeEffSpec < 1000) ? (sk.timeEffSpec * 100) : (int)sk.timeEffSpec;
				}
			}
		}
		if (buffMs <= 0)
		{
			// Mặc định chuẩn cấu hình server: sk4019 Option 32 = 180 (18.0s = 18000ms)
			buffMs = 18000;
		}
		super.timeEnd = (short)Math.min(32000, buffMs);
		this.addSound(10);
	}

	private void updateVenomBuff4019()
	{
		// KÍCH HOẠT THẦN TỐC TẠI FRAME 6 (~0.2s)!
		if (super.f == 6)
		{
			this.addSound(14);
			this.addSound(51);
			LoadMap.timeVibrateScreen = 8;

			if (super.objFireMain != null)
			{
				super.objFireMain.addDataEff((short)108, (int)super.timeEnd, (byte)0, (byte)0);
			}
		}

		// Hết thời gian tác dụng buff theo param của skill -> tự xóa
		if (super.timeEnd > 0 && GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd)
		{
			this.removeEff();
			return;
		}

		// Nếu đối tượng thi triển tử vong hoặc bị xóa -> tự xóa
		if (super.objFireMain != null && (super.objFireMain.isDie || super.objFireMain.Hp <= 0 || super.objFireMain.isRemove))
		{
			this.removeEff();
			return;
		}
	}

	private void paintVenomBuff4019(mGraphics g)
	{
		if (g == null) return;
		ensureVenomSkillFrames();
		super.levelPaint = -1; // Đảm bảo luôn vẽ sau lưng / dưới nhân vật

		int casterX = (super.objFireMain != null) ? super.objFireMain.x : super.x;
		int casterY = (super.objFireMain != null) ? super.objFireMain.y : super.y;

		// Song Long Hỏa Trụ (aura: 477, 100x120) - SAU LƯNG NHÂN VẬT (chỉ vẽ trên nhân vật, đã bỏ vũng độc)
		if (s_imgAuraDragon != null && s_imgAuraDragon.nFrame > 0)
		{
			int dragonF = ((super.f < 4 ? super.f : (super.f - 4)) / 2) % s_imgAuraDragon.nFrame;
			s_imgAuraDragon.drawFrame(dragonF, casterX, casterY, 0, 33, g);
		}
	}
}
