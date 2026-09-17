public final class MonsterHuman extends MainMonster {
   public MonsterHuman(short id, int x, int y, CatalogyMonster cata) {
      super(id, x, y);
      super.timeLoadInfo = GameCanvas.timeNow;
      super.typeObject = 1;
      super.clazz = 6;
      super.idCatMonster = cata.idCat;
      super.ID = id;
      super.xAnchor = x;
      super.yAnchor = y;
      super.x = x;
      super.y = y;
      super.toX = x;
      super.toY = y;
      super.toXNew = x;
      super.toYNew = y;
      super.downSpeedWater = 1;
      super.Dir = 0;
      super.hIconFocus = 0;
      if (LoadMap.specMap == 4) {
         this.setSpeed(4, 3);
         super.wOne = 100;
      } else if (LoadMap.specMap == 6) {
         this.setSpeed(3, 3);
         super.wOne = 26;
      } else {
         this.setSpeed(5, 5);
         super.wOne = 26;
      }

      super.AF = 60;
      super.AB = CRes.random(100, 150);
      super.AE = 80;
      super.AO = 3000;
      super.AG = 30;
      super.CQ = 0;
      if (cata.AM) {
         this.AA(cata);
      }

      this.BU();
   }

   public final void paint(mGraphics var1) {
      if (!super.NP) {
         var1.drawRegion((mImage)AvMain.imgHinhnhan, super.x, super.y, 33);
      } else {
         if (super.Action == 4) {
            if (!super.isDie) {
               this.AA(var1, super.KC - 1, super.KD, super.LY);
               this.AG(var1, super.KC, super.KD - super.KE);
               return;
            }
         } else if (!super.isDie) {
            this.AA(var1, 0);
            this.AD(var1, super.LY);
            mGraphics var2 = var1;
            MonsterHuman var5 = this;

            for(int var3 = 0; var3 < var5.vecEffElite.size(); ++var3) {
               ((MainEffect)var5.vecEffElite.elementAt(var3)).paint(var2, var5.x, var5.y);
            }
         }

      }
   }

   public final void update() {
      this.BJ();
      if (super.AN) {
         this.BB();
         super.x += super.vx;
         super.y += super.vy;
         if (super.vx == 0 && super.vy == 0) {
            this.setSpeed(5, 5);
            super.AN = false;
         }
      } else if (super.CN > 5) {
         this.setSpeed(5, 5);
      }

      if (!super.AN) {
         if (super.KZ > 0) {
            --super.KZ;
            super.vx = 0;
            super.vy = 0;
         } else if (super.Action != 4) {
            if (super.skillCurrent != null) {
               if (super.Action != 2) {
                  this.BB();
               }

               if (super.skillCurrent.AD) {
                  super.skillCurrent = null;
               } else if (CRes.abs(super.x - super.toX) < super.CN && CRes.abs(super.y - super.toY) < super.CN) {
                  super.skillCurrent.AC();
               }
            } else if (super.Action != 2 && super.Action != 3 && super.plashNow == null) {
               if (!MainObject.AB((MainObject)this) && !MainObject.AA(super.toX, super.toY, super.wOne, super.hOne) && LoadMap.specMap != 6) {
                  super.x = super.toX;
                  super.y = super.toY;
                  super.toX = super.toXNew;
                  super.toY = super.toYNew;
                  super.vx = 0;
                  super.vy = 0;
                  if (super.Action != 4) {
                     super.Action = 0;
                  }
               } else {
                  this.BR();
               }
            } else if (super.plashNow != null && super.Action != 2) {
               super.Action = 2;
            }

            if (super.Action != 2 && super.Action != 3) {
               int var1 = GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy);
               byte var10001 = super.downSpeedWater;
               this.AG(var1);
            }
         }

         this.AP();
         this.AK();
         super.update();
         this.AU();
         this.AV();
         if (LoadMap.specMap != 6) {
            this.AB();
         }

         MonsterHuman var4 = this;
         if (super.typeSpecMonSter == 1 && GameCanvas.gameTick % 300 == 0) {
            GameScreen.AA((short)111, 0, super.x, super.y, super.ID, super.typeObject, (byte)super.Dir, this, 0);
         }

         for(int var2 = 0; var2 < var4.vecEffElite.size(); ++var2) {
            ((MainEffect)var4.vecEffElite.elementAt(var2)).update();
         }

      }
   }

   public final void setDataBeginSkill(MainSkill var1, mVector var2) {
      super.plashNow = new Plash(var1, this, var2);
      this.resetBeginFire();
      super.Action = 2;
   }

   public final void updateAva() {
      if (super.f > super.PQ.length - 1) {
         super.Action = 0;
         super.f = 0;
         super.frame = super.PP[super.f];
      } else {
         super.frame = super.PQ[super.f];
      }
   }

   public final void AL() {
      if (super.skillCurrent != null) {
         this.setDataBeginSkill(super.skillCurrent.AC, super.skillCurrent.AB);
         super.skillCurrent.AD = true;
      }

   }

   public final void AJ() {
      super.AN = true;
      super.toXNew = super.x;
      super.toYNew = super.y;
      super.vx = 0;
      super.vy = 0;
      this.setSpeed(10, 10);
      int var2 = 0;

      boolean var3;
      do {
         super.toX = super.xAnchor + CRes.random_Am_0(48);
         super.toY = super.yAnchor + CRes.random_Am_0(48);
         int var1;
         var3 = (var1 = GameCanvas.loadmap.AA(super.toX, super.toY)) != 1 && var1 != -1;
         ++var2;
         if (var2 > 15) {
            var3 = true;
            super.toX = super.xAnchor;
            super.toY = super.yAnchor;
         }
      } while(!var3);

      if (CRes.random(2) == 0) {
         super.x = MainScreen.cameraMain.xCam - 30;
      } else {
         super.x = MainScreen.cameraMain.xCam + MotherCanvas.w + 30;
      }

      if (super.x < 0) {
         super.x = 0;
      }

      if (super.x > GameCanvas.loadmap.maxWMap) {
         super.x = GameCanvas.loadmap.maxWMap;
      }

      super.y = super.toY;
      if (!MainObject.AA(super.toX, super.toY, super.wOne, super.hOne)) {
         super.AN = false;
         this.setSpeed(5, 5);
         super.x = super.toX;
         super.y = super.toY;
      }

   }

   public final void AA(CatalogyMonster var1) {
      super.BR = var1.AD;
      super.name = var1.AL;
      if (super.LvThongThao > 0) {
         super.name = var1.AL + T.TP + super.LvThongThao;
      }

      super.maxHp = var1.AC;
      super.MO = var1.AE;
      super.MR = var1.AF;
      super.Hp = var1.AC;
      super.Lv = var1.AB;
      super.hOne = var1.AI;
      this.AA(var1.AG);
      this.AB(var1.AH);
      this.AA(var1.AK);
      if (super.BT != 778 && super.BT != 781 && super.BT != 784 && super.BT != 788 && super.BT != 791 && super.BT != 794) {
         super.LY = 0;
      } else {
         super.LY = 4;
      }

      super.NP = true;
   }
}
