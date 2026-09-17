public final class Class_HO {
   private MainObject AE;
   public MainObject AA;
   public mVector AB;
   public MainSkill AC;
   public boolean AD = false;
   private boolean AF = false;
   private int AG = 0;

   public Class_HO(MainObject var1, mVector var2, MainSkill var3) {
      this.AE = var1;
      this.AB = var2;
      this.AC = var3;
      Object_Effect_Skill var4 = (Object_Effect_Skill)this.AB.elementAt(0);
      this.AA = MainObject.get_Object((int)var4.ID, (byte)var4.tem);
      if (var3.AM == 0) {
         if (this.AE != null && !this.AE.returnAction() && (this.AB != null && this.AB.size() != 0 || var3.AM == 3) && this.AA != null && !this.AA.returnAction()) {
            if (this.AE != GameScreen.player) {
               this.AC.AA();
               var1.timeBeginUpdateMove = -1;
            }

         } else {
            this.AD = true;
         }
      }
   }

   public final void AA() {
      if (this.AC.AM != 0) {
         this.beginSkill();
      } else if (this.AA != null && !this.AF) {
         int var1 = MainObject.getDistance(this.AE.x, this.AE.y, this.AA.x, this.AA.y);
         int var2 = CRes.AA(this.AE.x - this.AA.x, this.AE.y - this.AA.y);
         if ((CRes.abs(var1) >= this.AC.AR || !AA(var2) || this.AE.posTransRoad != null) && this.AG < 2) {
            ++this.AG;
            int var4 = 0;
            int var6 = 0;
            int var7 = 64;
            if (64 > this.AC.AR - 8) {
               var7 = this.AC.AR - 8;
            }

            int var5 = 360;
            if (this.AE.x < this.AA.x) {
               var5 = 180;
            }

            int var3;
            do {
               var2 = this.AA.x + CRes.getcos(CRes.fixangle(var5 + var6)) * var7 / 1000;
               var3 = this.AA.y + CRes.getsin(CRes.fixangle(var5 + var6)) * var7 / 1000;
               var1 = GameCanvas.loadmap.AA(var2, var3);
               if (var4 % 2 == 0) {
                  var6 = (var4 % 6 + 1) * 7;
               } else {
                  var6 = -(var4 % 6 + 1) * 7;
               }

               ++var4;
               if (var4 == 6) {
                  var7 = 40;
                  if (40 > this.AC.AR - 8) {
                     var7 = this.AC.AR - 8;
                  }

                  var5 = var5 == 180 ? 360 : 180;
                  var6 = 0;
               }

               if (var4 > 12) {
                  var2 = this.AA.x + CRes.getcos(CRes.fixangle(0)) * var7 / 1000;
                  var3 = this.AA.y + CRes.getsin(CRes.fixangle(0)) * var7 / 1000;
                  this.AE.posTransRoad = null;
                  break;
               }

               if (var4 % 2 == 0 && var7 < this.AC.AR - 8 - LoadMap.wTile / 2) {
                  var7 += LoadMap.wTile / 2;
               }
            } while(var1 == -1 || var1 == 1);

            if (this.AE == GameScreen.player) {
               this.AA.KZ = 10;
               GameScreen.player.AD = 0;
               GameScreen.player.AE = 0;
               GameScreen.player.toX = GameScreen.player.x;
               GameScreen.player.toY = GameScreen.player.y;
               GameScreen.player.AC = 0;
               if (GameScreen.player.posTransRoad != null) {
                  GameScreen.player.AC = 1;
               }

               GameScreen.player.posTransRoad = GameCanvas.loadmap.AA(var2 / LoadMap.wTile, var3 / LoadMap.wTile, GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 16, GameScreen.player);
               GameScreen.player.posTransRoad = GameCanvas.loadmap.AA(var2 / LoadMap.wTile, var3 / LoadMap.wTile, GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 16, GameScreen.player);
               if (GameScreen.player.posTransRoad != null && GameScreen.player.posTransRoad.length > 16) {
                  this.AF = true;
                  GameScreen.player.posTransRoad = null;
               } else {
                  if (GameScreen.player.posTransRoad == null) {
                     GameScreen.player.toX = var2;
                     GameScreen.player.toY = var3;
                     if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameScreen.player.toX, GameScreen.player.toY) < GameScreen.player.CN << 1) {
                        GameScreen.player.x = var2;
                        GameScreen.player.y = var3;
                        this.beginSkill();
                     } else {
                        GameScreen.player.NR = true;
                     }
                  }

                  if (var2 <= GameScreen.player.x) {
                     GameScreen.player.Dir = 0;
                     return;
                  }

                  GameScreen.player.Dir = 2;
               }
            } else {
               this.AE.toX = var2;
               this.AE.toY = var3;
            }

         } else {
            this.beginSkill();
         }
      } else {
         this.AD = true;
      }
   }

   private static boolean AA(int var0) {
      return CRes.fixangle(var0) <= 225 && CRes.fixangle(var0) >= 135 || CRes.fixangle(var0) >= 315 || CRes.fixangle(var0) <= 45;
   }

   public final void beginSkill() {
      if (this.AE == null) {
         this.AD = true;
      } else if (this.AC.AM == 3) {
         this.AE.setDataBeginSkill(this.AC, this.AB);
         this.AD = true;
      } else {
         int var1;
         if (this.AC.AM == 0 && LoadMap.specMap != 4 && this.AA != null && ((var1 = MainObject.getDistance(this.AE.x, this.AE.y, this.AA.x, this.AA.y)) <= 32 || var1 <= 48 && this.AE.clazz == 5)) {
            if (CRes.random(8) == 0) {
               if (this.AE.clazz == 5) {
                  if (this.AE.x < this.AA.x) {
                     this.AE.x = this.AA.x + 48;
                  } else {
                     this.AE.x = this.AA.x - 48;
                  }
               } else if (this.AE.x < this.AA.x) {
                  this.AE.x = this.AA.x + 32;
               } else {
                  this.AE.x = this.AA.x - 32;
               }

               this.AE.type_left_right = this.AE.type_left_right == 0 ? 2 : 0;
               GameScreen.addEffectEnd_ObjTo((short)56, 0, this.AE.x, this.AE.y, (short)this.AE.ID, (byte)this.AE.typeObject, (byte)this.AE.type_left_right, this.AE);
            } else if (this.AE.clazz == 5) {
               if (this.AE.x < this.AA.x) {
                  this.AE.x = this.AA.x - 48;
               } else {
                  this.AE.x = this.AA.x + 48;
               }
            } else if (this.AE.x < this.AA.x) {
               this.AA.x = this.AE.x + 32;
            } else {
               this.AA.x = this.AE.x - 32;
            }
         }

         if (this.AE == GameScreen.player && LoadMap.specMap != 3) {
            GameScreen.player.setUseMana(this.AC.AS);
            Player.AA(this.AC.AB, this.AC.AH, this.AC.AQ, (byte)1);
            if (this.AC.AM != 0) {
               if (this.AA != null) {
                  GlobalService.getInstance().AB(this.AC.ID, this.AA.typeObject, this.AB);
               }
            } else {
               if (this.AA != null) {
                  GlobalService.getInstance().Obj_Move((short)GameScreen.player.x, (short)GameScreen.player.y);
                  GlobalService.getInstance().Player_Fire(this.AC.ID, this.AA.typeObject, this.AB);
               }

               if (this.AC.AG == 0) {
                  Interface_Game.BS = ++Player.RB;
                  Interface_Game.BT = 0;
               }
            }
         }

         this.AE.setDataBeginSkill(this.AC, this.AB);
         this.AD = true;
      }
   }

   public final void AC() {
      if (this.AC.AM != 0) {
         if (this.AC.AM == 2) {
            if (this.AG == 0) {
               this.AG = 1;
               this.AE.toX = this.AC.AI;
               this.AE.toY = this.AC.AJ;
            } else {
               if (MainObject.getDistance(this.AE.x, this.AE.y, this.AC.AI, this.AC.AJ) >= 24) {
                  GameScreen.addEffectEnd_ObjTo((short)56, 0, this.AC.AI, this.AC.AJ, (short)this.AE.ID, (byte)this.AE.typeObject, (byte)this.AE.Dir, this.AE);
               }

               this.beginSkill();
            }
         } else {
            this.beginSkill();
         }
      } else if (this.AA == null) {
         this.AD = true;
      } else if (this.AG != 1 && this.AE.vecSkillFires.size() <= 0) {
         int var1 = MainObject.getDistance(this.AE.x, this.AE.y, this.AA.x, this.AA.y);
         int var2 = CRes.AA(this.AE.x - this.AA.x, this.AE.y - this.AA.y);
         if (CRes.abs(var1) < this.AC.AR && AA(var2)) {
            this.beginSkill();
         } else {
            this.AG = 1;
            short var4 = 180;
            int var3 = this.AC.AR - 5;
            if (CRes.random(2) == 0) {
               var4 = 0;
            }

            var1 = this.AA.x + CRes.getcos(CRes.fixangle(var4)) * var3 / 1000;
            var2 = this.AA.y + CRes.getsin(CRes.fixangle(var4)) * var3 / 1000;
            this.AE.toX = var1;
            this.AE.toY = var2;
         }
      } else {
         this.beginSkill();
         this.AE.toX = this.AE.x;
         this.AE.toY = this.AE.y;
      }
   }
}
