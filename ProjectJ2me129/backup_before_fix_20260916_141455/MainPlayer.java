public class MainPlayer extends MainObject {
   public static short[][][] AA = new short[][][]{{{4, 11}, {16, 10}, {24, 10}}, {{9, 9}, {16, 7}, {23, 8}}, {{11, 9}, {20, 8}, {16, 7}}, {{7, 9}, {15, 13}, {24, 10}}, {{11, 11}, {16, 9}, {21, 12}}, {{10, 11}, {17, 10}, {23, 12}}};
   public int AB = 0;
   private int AH;
   private int AI;
   public int AC = 0;
   public int AD;
   public int AE;
   public mVector AF = new mVector("MainPlayer.vecAllInfo");
   public mVector AG = new mVector("MainPlayer.vecAllInfoParty");
   private int AJ;

   public final void AB(boolean var1) {
      if (super.typeActionBoat == 0) {
         int var2;
         int var3;
         if (GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy) == 1) {
            var2 = GameCanvas.loadmap.AB(super.x + super.vx, super.y + super.vy);
            if (super.vy != 0) {
               if (var2 % GameCanvas.loadmap.mapW > 0 && (GameCanvas.loadmap.AA(super.x + super.vx - LoadMap.wTile, super.y + super.vy) == 0 || GameCanvas.loadmap.AA(super.x + super.vx - LoadMap.wTile, super.y + super.vy) == 2) && (GameCanvas.loadmap.AA(super.x + super.vx - LoadMap.wTile, super.y) == 0 || GameCanvas.loadmap.AA(super.x + super.vx - LoadMap.wTile, super.y) == 2)) {
                  super.vx = -super.CN;
                  super.Dir = 0;
                  super.vy = 0;
               } else if (var2 % GameCanvas.loadmap.mapW >= GameCanvas.loadmap.mapW - 1 || GameCanvas.loadmap.AA(super.x + super.vx + LoadMap.wTile, super.y + super.vy) != 0 && GameCanvas.loadmap.AA(super.x + super.vx + LoadMap.wTile, super.y + super.vy) != 2 || GameCanvas.loadmap.AA(super.x + super.vx + LoadMap.wTile, super.y) != 0 && GameCanvas.loadmap.AA(super.x + super.vx + LoadMap.wTile, super.y) != 2) {
                  super.vy = 0;
               } else {
                  super.vx = super.CN;
                  super.Dir = 2;
                  super.vy = 0;
               }
            } else if (super.vx != 0) {
               if ((GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy - LoadMap.wTile) == 0 || GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy - LoadMap.wTile) == 2) && (GameCanvas.loadmap.AA(super.x, super.y + super.vy - LoadMap.wTile) == 0 || GameCanvas.loadmap.AA(super.x, super.y + super.vy - LoadMap.wTile) == 2)) {
                  super.vy = -super.CN;
                  super.Dir = 1;
               } else if ((GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy + LoadMap.wTile) == 0 || GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy + LoadMap.wTile) == 2) && (GameCanvas.loadmap.AA(super.x, super.y + super.vy + LoadMap.wTile) == 0 || GameCanvas.loadmap.AA(super.x, super.y + super.vy + LoadMap.wTile) == 2)) {
                  super.vy = super.CN;
                  super.Dir = 3;
               }

               super.vx = 0;
            }

            if (super.vx == 0 && super.vy == 0 && super.Action != 4) {
               super.Action = 0;
            }
         } else if (LoadMap.specMap == 4) {
            MainPlayer var10 = this;
            if (super.boatSea != null && super.ID == super.boatSea.ID) {
               super.boatSea.AB(super.vx, super.vy);

               for(var3 = 0; var3 < GameScreen.vecPlayers.size(); ++var3) {
                  MainObject var4;
                  if ((var4 = (MainObject)GameScreen.vecPlayers.elementAt(var3)) != var10 && var4.boatSea != null && var4.ID == var4.boatSea.ID && MainObject.AA((int)var4.x, var4.y, (int)50, (int)50)) {
                     var4.boatSea.AB(0, 0);
                     if (CRes.AA(var10.boatSea, var4.boatSea) && (var10.vx > 0 && var10.x < var4.x || var10.vx < 0 && var10.x > var4.x || var10.vy > 0 && var10.y <= var4.y || var10.vy < 0 && var10.y > var4.y)) {
                        if (super.Action != 4) {
                           super.Action = 0;
                        }

                        super.vx = 0;
                        super.vy = 0;
                        break;
                     }
                  }
               }
            }
         }

         if (super.vx == 0 && super.vy == 0 && LoadMap.AA(GameCanvas.loadmap.AA(super.x, super.y))) {
            int var9 = 24;
            var2 = super.x * 1000;
            var3 = super.y * 1000;
            int var6 = 0;
            boolean var7 = false;

            do {
               var7 = false;
               int var11 = var2 + CRes.getcos(var6) * var9;
               int var5 = var3 + CRes.getsin(var6) * var9;
               int var8;
               if (var11 >= 0 && var5 >= 0 && ((var8 = GameCanvas.loadmap.AA(var11 / 1000, var5 / 1000)) == 0 || var8 == 2)) {
                  super.x = var11 / 1000;
                  super.y = var5 / 1000;
                  this.resetAction();
                  var7 = true;
               }

               var6 += 44;
               if (var6 >= 360) {
                  var6 = 0;
                  var9 += 24;
               }
            } while(!var7);
         }

      }
   }

   public void resetAction() {
      super.vx = super.vy = 0;
      super.toX = super.x;
      super.toY = super.y;
      if (super.Action != 2 && super.Action != 4) {
         super.Action = 0;
      }

   }

   public final void setDataBeginSkill(MainSkill var1, mVector var2) {
      super.plashNow = new Plash(var1, this, var2);
      this.resetBeginFire();
      super.Action = 2;
   }

   public void AB(MainSkill var1, mVector var2) {
      if (super.skillCurrent != null) {
         super.skillCurrent.beginSkill();
      }

      super.vecSkillFires.addElement(new Class_HO(this, var2, var1));
   }

   public void AB() {
      if ((GameCanvas.gameTick + super.LA) % 75 == 0 && CRes.random(3) == 0 && super.Action != 2 && super.skillCurrent != null) {
         int var4 = CRes.random(MainObject.mPosMapTrain.length);
         super.toX = MainObject.mPosMapTrain[var4][0];
         super.toY = MainObject.mPosMapTrain[var4][1];
      } else {
         ++this.AH;
         if (this.AH > 80 && super.NT != null) {
            this.AH = 0;
            super.toX = super.x;
            super.toY = super.y;
            mVector var1 = new mVector();

            for(int var2 = 0; var2 < GameScreen.vecPlayers.size(); ++var2) {
               MainObject var3 = (MainObject)GameScreen.vecPlayers.elementAt(var2);
               if (MainObject.getDistance(super.x, super.y, var3.x, var3.y) < Player.wFocus && var3.typeObject == 1) {
                  Object_Effect_Skill var5;
                  (var5 = new Object_Effect_Skill(var3.ID, var3.typeObject)).AA(var3.maxHp / 10, var3.Hp - var3.maxHp / 10, 0);
                  var1.addElement(var5);
                  MainSkill var6 = new MainSkill((short)-1, super.NT[this.AI]);
                  this.AB(var6, var1);
                  ++this.AI;
                  if (this.AI >= super.NT.length) {
                     this.AI = 0;
                  }

                  return;
               }
            }
         }

      }
   }

   public final void BS() {
      MainPlayer var1;
      int var2;
      Boat var3;
      if (super.typeActionBoat == 1) {
         var1 = this;
         if (super.PY == 0) {
            if (super.Action == 5) {
               if (super.f < 2) {
                  super.dy = (super.f + 1) * 5;
               } else if (super.f != 2 && super.f != 3) {
                  if (super.f > 3) {
                     super.dy = 12 - (super.f - 3) * 5;
                  }
               } else {
                  super.dy = 12;
               }

               if (super.f == 6) {
                  super.x = super.IR;
                  super.y = super.IS;
                  super.toX = super.x;
                  super.toY = super.y;
                  super.dy = 0;
                  super.Action = 0;
                  super.vx = 0;
                  super.vy = 0;
                  super.PY = 1;
                  this.setSpeed(3, 3);

                  for(var2 = 0; var2 < GameScreen.vecBoat.size(); ++var2) {
                     if ((var3 = (Boat)GameScreen.vecBoat.elementAt(var2)).ID == var1.ID) {
                        var3.AH = false;
                        var1.boatSea = var3;
                        break;
                     }
                  }

                  if (var1.boatSea == null) {
                     var1.BX();
                  }
               }

               var1.type_left_right = 2;
               var1.Dir = 2;
            } else {
               ++super.PZ;
               if (super.posTransRoad == null && (MainObject.getDistance(super.x, super.y, super.IR, super.IS) <= 48 || super.PZ > 40)) {
                  super.vx = (super.IR - super.x) / 6;
                  super.vy = (super.IS - super.y) / 6;
                  super.f = 0;
                  super.Action = 5;
                  super.type_left_right = 2;
                  super.Dir = 2;
                  super.PZ = 0;
               }
            }
         } else if (super.PY == 1) {
            ++super.PZ;
            if (super.PZ == 20) {
               this.AB(super.x + 120, super.y);
            }

            if (super.PZ == 50) {
               this.BX();
            }
         }
      } else if (super.typeActionBoat == 2) {
         if (super.PY == 0) {
            if (super.Action == 5) {
               if (super.f == 5) {
                  super.dy = 0;
                  super.Action = 0;
                  super.vx = 0;
                  super.vy = 0;
                  super.PY = 1;
                  this.setSpeed(7, 7);
                  this.AB(super.xAnchor, super.yAnchor);
               } else if (super.f < 2) {
                  super.dy = (super.f + 1) * 5;
               } else if (super.f != 2 && super.f != 3) {
                  if (super.f > 3) {
                     super.dy = 12 - (super.f - 3) * 5;
                  }
               } else {
                  super.dy = 12;
               }
            } else if (super.posTransRoad == null) {
               ++super.PZ;
               if (MainObject.getDistance(super.x, super.y, super.IR, super.IS) <= 72 || super.PZ >= 10) {
                  super.vx = 0;
                  super.vy = 6;
                  super.f = 0;
                  super.Action = 5;
                  super.type_left_right = 2;
                  super.Dir = 2;
                  if (super.boatSea != null) {
                     GameScreen.AA(super.boatSea, true);
                     super.boatSea = null;
                  }
               }
            }
         } else if (super.PY == 1 && super.posTransRoad == null && MainObject.getDistance(super.x, super.y, super.xAnchor, super.yAnchor) <= 48) {
            super.typeActionBoat = 0;
         }
      } else if (super.typeActionBoat == 3) {
         var1 = this;
         if (super.PY == 0) {
            if (super.Action == 5) {
               if (super.f < 2) {
                  super.dy = (super.f + 1) * 5;
               } else if (super.f != 2 && super.f != 3) {
                  if (super.dy > 3) {
                     super.dy = 12 - (super.f - 3) * 5;
                  }
               } else {
                  super.dy = 12;
               }

               if (super.f == 6) {
                  super.x = super.IR;
                  super.y = super.IS;
                  super.dy = 0;
                  super.Action = 0;
                  super.vx = 0;
                  super.vy = 0;
                  super.PY = 1;
                  this.setSpeed(3, 3);

                  for(var2 = 0; var2 < GameScreen.vecBoat.size(); ++var2) {
                     if ((var3 = (Boat)GameScreen.vecBoat.elementAt(var2)).ID == var1.ID) {
                        var3.AH = false;
                        var1.boatSea = var3;
                        break;
                     }
                  }

                  if (var1.boatSea == null) {
                     var1.BX();
                  }
               }

               var1.type_left_right = 2;
               var1.Dir = 2;
            } else {
               ++super.PZ;
               if (super.posTransRoad == null && (MainObject.getDistance(super.x, super.y, super.IR, super.IS) <= 72 || super.PZ > 40)) {
                  super.vx = (super.IR - super.x) / 6;
                  super.vy = (super.IS - super.y) / 6;
                  super.f = 0;
                  super.Action = 5;
                  super.type_left_right = 2;
                  super.Dir = 2;
                  super.PZ = 0;
               }
            }
         } else if (super.PY == 1) {
            ++super.PZ;
            if (super.PZ == 20) {
               this.AB(super.x + 120, super.y);
            }

            if (super.PZ == 50) {
               this.BX();
            }
         }
      } else if (super.typeActionBoat == 4) {
         if (super.PY == 0) {
            if (super.Action == 5) {
               if (super.f < 2) {
                  super.dy = (super.f + 1) * 5;
               } else if (super.f != 2 && super.f != 3) {
                  if (super.dy > 3) {
                     super.dy = 12 - (super.f - 3) * 5;
                  }
               } else {
                  super.dy = 12;
               }

               if (super.f == 5) {
                  super.dy = 0;
                  super.Action = 0;
                  super.vx = 0;
                  super.vy = 0;
                  super.PY = 1;
                  this.setSpeed(7, 7);
                  this.AB(super.xAnchor, super.yAnchor);
               }
            } else if (super.posTransRoad == null && MainObject.getDistance(super.x, super.y, super.IR, super.IS) <= 48) {
               super.vx = 0;
               super.vy = -12;
               super.f = 0;
               super.Action = 5;
               super.type_left_right = 2;
               super.Dir = 2;
               if (super.boatSea != null) {
                  GameScreen.AA(super.boatSea, true);
                  super.boatSea = null;
               }
            }
         } else if (super.PY == 1 && super.posTransRoad == null && MainObject.getDistance(super.x, super.y, super.xAnchor, super.yAnchor) <= 48) {
            super.typeActionBoat = 0;
         }
      }

      ++this.AJ;
   }

   public final void AA(byte var1, int var2, int var3) {
      super.typeActionBoat = var1;
      super.PZ = 0;
      super.posTransRoad = null;
      super.PY = 0;
      super.IR = 0;
      super.IS = 0;
      this.AJ = 0;
      int var4;
      Boat var5;
      if (super.typeActionBoat == 1) {
         for(var4 = 0; var4 < GameScreen.vecBoat.size(); ++var4) {
            if ((var5 = (Boat)GameScreen.vecBoat.elementAt(var4)).ID == super.ID) {
               super.IR = var5.x - (var5.Dir == 2 ? var5.AJ : -var5.AJ);
               super.IS = var5.y;
               break;
            }
         }

         if (super.IR != 0 && super.IS != 0) {
            this.AK();
         } else {
            this.BX();
            super.typeActionBoat = 0;
         }
      } else if (super.typeActionBoat == 2) {
         super.IR = var2;
         super.IS = var3;
         super.xAnchor = super.x;
         super.yAnchor = super.y;
         super.y = super.IS;
         super.x = super.IR - 80;
         super.boatSea = new Boat(super.ID, super.x, super.y, 0, (byte)super.type_left_right);
         super.boatSea.AA(super.BO, super.typePirate);
         this.AJ();

         for(var4 = 0; var4 < GameScreen.vecBoat.size(); ++var4) {
            if ((var5 = (Boat)GameScreen.vecBoat.elementAt(var4)).ID == super.ID) {
               GameScreen.vecBoat.removeElement(var5);
               break;
            }
         }

         this.setSpeed(3, 3);
         super.vySea = 4;
      } else if (super.typeActionBoat == 3) {
         for(var4 = 0; var4 < GameScreen.vecBoat.size(); ++var4) {
            if ((var5 = (Boat)GameScreen.vecBoat.elementAt(var4)).ID == super.ID) {
               super.IR = var5.x - (var5.Dir == 2 ? var5.AJ : -var5.AJ);
               super.IS = var5.y;
               break;
            }
         }

         if (super.IR != 0 && super.IS != 0) {
            this.AK();
         } else {
            this.BX();
            super.typeActionBoat = 0;
         }
      } else {
         if (super.typeActionBoat == 4) {
            super.IR = var2;
            super.IS = var3;
            super.xAnchor = super.x;
            super.yAnchor = super.y;
            super.y = super.IS;
            super.x = super.IR - 80;
            this.AJ();
            super.boatSea = new Boat(super.ID, super.x, super.y, 0, (byte)super.type_left_right);
            super.boatSea.AA(super.BO, super.typePirate);

            for(var4 = 0; var4 < GameScreen.vecBoat.size(); ++var4) {
               if ((var5 = (Boat)GameScreen.vecBoat.elementAt(var4)).ID == super.ID) {
                  GameScreen.vecBoat.removeElement(var5);
                  break;
               }
            }

            this.setSpeed(3, 3);
            super.vySea = 4;
         }

      }
   }

   public final void AF() {
      if (super.boatSea != null && LoadMap.specMap != 4 && this.AJ > 120) {
         if (super.typeActionBoat != 1 && super.typeActionBoat != 3) {
            if (super.typeActionBoat == 2 || super.typeActionBoat == 4) {
               this.setSpeed(7, 7);
               super.boatSea = null;
               this.AJ = 0;
            }
         } else {
            this.BX();
         }

         super.typeActionBoat = 0;
      }

   }

   public void AJ() {
      super.toX = super.IR;
      super.toY = super.IS;
   }

   public void BX() {
      super.isRemove = true;
   }

   public void AK() {
      if (super.typeActionBoat == 1) {
         super.toX = super.IR - 24;
         super.toY = super.IS - 12;
      } else {
         if (super.typeActionBoat == 3) {
            super.toX = super.IR - 24;
            super.toY = super.IS + 12;
         }

      }
   }

   public void AB(int var1, int var2) {
      super.toX = var1;
      super.toY = var2;
   }

   public final iCommand getCenterCmd() {
      return GameCanvas.loadmap.mapLang() ? GameScreen.BY : null;
   }
}
