public final class Class_ET extends MainPlayer {
   public Class_ET(short var1, byte var2, String var3, int var4, int var5) {
      new mVector("Other_Player.vecEffShip");
      super.hOne = 52;
      if (LoadMap.specMap == 4) {
         this.setSpeed(4, 3);
         super.wOne = 100;
      } else {
         this.setSpeed(7, 7);
         super.wOne = 26;
      }

      super.typeObject = var2;
      super.name = var3;
      super.ID = var1;
      super.x = var4;
      super.y = var5;
      super.toX = var4;
      super.toY = var5;
      super.toXNew = var4;
      super.toYNew = var5;
      super.hIconFocus = 0;
      super.f = CRes.random(super.feStand.length);
      super.Action = 0;
      super.colorName = 5;
      this.BU();
   }

   public final void setImgMonSterforOtherPlayer(byte var1) {
      super.wOne = 0;
      super.BR = var1;
      switch(var1) {
      case 0:
         super.IO = 5;
         super.BM = MonsterWalk.AU;
         return;
      case 1:
         super.IO = 7;
         super.BM = MonsterWalk.AT;
         return;
      case 2:
         super.IO = 8;
         super.BM = MonsterWalk.AV;
         return;
      case 4:
         super.IO = 7;
         super.BM = MonsterWalk.AW;
         return;
      case 13:
         super.IO = 9;
         super.BM = Class_EV.AS;
         return;
      case 17:
         super.IO = 4;
         super.BM = MonsterWalk.AZ;
         super.LY = 0;
         return;
      case 19:
         super.IO = 2;
         super.BM = MonsterWalk.AY;
         super.LY = 0;
         return;
      default:
         super.IO = 5;
         super.BM = MonsterWalk.AS;
      }
   }

   public final void paint(mGraphics var1) {
      if (super.isInfo) {
         byte var2 = super.colorName;
         int var3;
         if (Player.vecParty.size() > 0) {
            for(var3 = 0; var3 < Player.vecParty.size(); ++var3) {
               if (((Class_DS)Player.vecParty.elementAt(var3)).AC.compareTo(super.name) == 0) {
                  var2 = 4;
                  break;
               }
            }
         }

         if (Player.AY.length > 0) {
            for(var3 = 0; var3 < Player.AY.length; ++var3) {
               if (super.ID == Player.AY[var3]) {
                  var2 = 6;
               }
            }
         }

         if (this.BT()) {
            if (super.Action == 4) {
               if (!super.isDie) {
                  var1.drawRegion((mImage)MainObject.imgShadow, super.KC + 1, super.KD, 3);
                  this.AG(var1, super.KC, super.KD - super.KE);
               } else {
                  if (LoadMap.specMap != 4) {
                     this.AC(var1, super.x);
                  }

                  var1.drawRegion(AvMain.fraDiePlayer.imgFrame, 0, super.f / 5 % AvMain.fraDiePlayer.nFrame * AvMain.fraDiePlayer.frameHeight, AvMain.fraDiePlayer.frameWidth, AvMain.fraDiePlayer.frameHeight - 2 + super.CH / 10, 0, super.x - 4, super.y - super.dy, 36);
               }
            } else {
               this.AD(var1, 0);
            }
         } else {
            if (super.BR == 19) {
               if (super.Action == 4) {
                  if (super.LY >= 0) {
                     this.AA(var1, super.x, 0, super.LY);
                  }

                  MainImage var5;
                  if ((var5 = ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageMonster, (short)1000)) != null && var5.img != null) {
                     var1.drawRegion(var5.img, 0, super.BM[super.Action][super.f] * super.hOne, super.wOne, super.hOne, super.Dir == 2 ? 2 : 0, super.x, super.y - super.dy, 33);
                  }
               }

               super.dy = 0;
            }

            if (super.Action != 4) {
               this.AE(var1);
            }
         }

         this.AA(var1, (byte)var2, 0);
      }
   }

   public final void AC(mGraphics var1) {
      if (super.isInfo) {
         byte var2 = super.colorName;
         int var3;
         if (Player.vecParty.size() > 0) {
            for(var3 = 0; var3 < Player.vecParty.size(); ++var3) {
               if (((Class_DS)Player.vecParty.elementAt(var3)).AC.compareTo(super.name) == 0) {
                  var2 = 4;
                  break;
               }
            }
         }

         if (Player.AY.length > 0) {
            for(var3 = 0; var3 < Player.AY.length; ++var3) {
               if (super.ID == Player.AY[var3]) {
                  var2 = 6;
               }
            }
         }

         if (this.BT()) {
            if (super.Action == 4) {
               if (!super.isDie) {
                  var1.drawRegion((mImage)MainObject.imgShadow, super.KC + 1, super.KD, 3);
               } else {
                  if (LoadMap.specMap != 4) {
                     this.AC(var1, super.x);
                  }

                  var1.drawRegion(AvMain.fraDiePlayer.imgFrame, 0, super.f / 5 % AvMain.fraDiePlayer.nFrame * AvMain.fraDiePlayer.frameHeight, AvMain.fraDiePlayer.frameWidth, AvMain.fraDiePlayer.frameHeight - 2 + super.CH / 10, 0, super.x - 4, super.y - super.dy, 36);
               }
            } else {
               boolean var5 = false;
               this.AB(var1, (int)0);
               int var10000 = super.y;
               var10000 = super.dy;
               if (this.BV() && super.boatSea != null && super.boatSea.ID == super.ID) {
                  super.boatSea.AB(var1);
                  if (super.dy == 0) {
                     var10000 = super.y;
                     var10000 = super.CH;
                  }

                  super.boatSea.AE(var1);
               } else if (!super.isTanHinh) {
                  if (super.typeObject == 1) {
                     this.AA(var1, super.x - super.dx, super.KA, 0);
                  } else {
                     this.AC(var1, super.x - super.dx);
                  }
               }

               if (this.BV() && super.boatSea != null && super.boatSea.ID == super.ID) {
                  super.boatSea.AG(var1);
               }
            }
         } else {
            if (super.BR == 19) {
               if (super.Action == 4) {
                  if (super.LY >= 0) {
                     this.AA(var1, super.x, 0, super.LY);
                  }

                  MainImage var6;
                  if ((var6 = ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageMonster, (short)1000)) != null && var6.img != null) {
                     var1.drawRegion(var6.img, 0, super.BM[super.Action][super.f] * super.hOne, super.wOne, super.hOne, super.Dir == 2 ? 2 : 0, super.x, super.y - super.dy, 33);
                  }
               }

               super.dy = 0;
            }

            if (super.Action != 4) {
               this.AE(var1);
            }
         }

         this.AA(var1, (byte)var2, 0);
      }
   }

   public final void a_(mGraphics var1) {
      var1.drawRegion((mImage)MainObject.imgShadow, super.x, super.y, 3);
      var1.drawRegion((mImage)AvMain.imgHinhnhan, super.x, super.y, 33);
   }

   private void AE(mGraphics var1) {
      if (super.BM != null) {
         MainImage var2 = ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageMonster, (short)1000);
         int var3 = super.Action;
         if ((LoadMap.specMap == 4 || super.typeActionBoat != 0) && super.boatSea != null && super.Action == 1) {
            var3 = 0;
         }

         if (var3 > super.BM.length - 1) {
            var3 = 0;
         }

         if (super.f > super.BM[var3].length - 1) {
            super.f = 0;
         }

         if (super.IdIcon != 999) {
            var1.drawRegion((mImage)AvMain.imgShadowSmall, super.x, super.y - 3, 3);
         }

         if (var2.img != null) {
            if (super.wOne <= 0 || super.hOne <= 0) {
               super.hOne = mImage.getImageHeight(var2.img.image) / super.IO;
               super.wOne = mImage.getImageWidth(var2.img.image);
            }

            int var4 = super.y - super.dy;
            if ((LoadMap.specMap == 4 || super.typeActionBoat != 0) && super.boatSea != null && super.boatSea.ID == super.ID) {
               super.boatSea.AB(var1);
               if (super.dy == 0) {
                  int var10000 = super.y;
                  var10000 = super.CH;
               }

               super.boatSea.AF(var1);
               super.boatSea.AE(var1);
               super.boatSea.AG(var1);
               return;
            }

            var1.drawRegion(var2.img, 0, super.BM[var3][super.f] * super.hOne, super.wOne, super.hOne, super.type_left_right, super.x, var4, 33);
            if (super.LM > 0) {
               if (super.LM == 1 || super.LM == 3) {
                  AvMain.fraTrongCay.drawFrame(0, super.x - 20, var4 - super.hOne / 2 - 5, 0, 3, var1);
               }

               if (super.LM == 2 || super.LM == 3) {
                  AvMain.fraTrongCay.drawFrame(1, super.x + 20, var4 - super.hOne / 2 - 5, 0, 3, var1);
               }

               if (super.LM == 4) {
                  AvMain.fraTrongCay.drawFrame(2, super.x - 20, var4 - super.hOne / 2 - 5, 0, 3, var1);
                  AvMain.fraTrongCay.drawFrame(2, super.x + 20, var4 - super.hOne / 2 - 5, 0, 3, var1);
                  AvMain.fraTrongCay.drawFrame(2, super.x, var4 - super.hOne / 2 - 20, 0, 3, var1);
               }
            }
         }

      }
   }

   public final void update() {
      if (super.Action == 4) {
         if (this.BT()) {
            this.BJ();
         }
      } else {
         if (super.typeActionBoat != 0) {
            this.BS();
            if (super.Action != 5) {
               this.BB();
            }
         }

         this.AF();
         if (LoadMap.specMap == 3) {
            this.AB();
         }

         if (!super.isInfo && GameCanvas.gameTick % 20 == 0 && (GameCanvas.timeNow - super.timeLoadInfo) / 1000L > 10L) {
            super.timeLoadInfo = GameCanvas.timeNow;
            GlobalService.getInstance().char_info(super.ID);
         }

         if (this.BT()) {
            this.BJ();
         } else {
            this.AA(false);
            this.BL();
            if (super.Action != 2) {
               this.AU();
            }
         }

         if (super.skillCurrent == null && super.vecSkillFires.size() > 0) {
            super.skillCurrent = (Class_HO)super.vecSkillFires.elementAt(0);
            this.resetBeginFire();
            ObjMove var2;
            if ((var2 = GameScreen.AB(super.ID, super.typeObject)) != null) {
               super.CB = var2.AC;
               super.CC = var2.AD;
            }

            super.vecSkillFires.removeElement(0);
         }

         if (super.skillCurrent != null) {
            if (super.skillCurrent.AD) {
               super.skillCurrent = null;
            } else if (CRes.abs(super.x - super.toX) < super.CN && CRes.abs(super.y - super.toY) < super.CN) {
               if (super.CB >= 0 && super.CC >= 0) {
                  if (MainObject.getDistance(super.x, super.y, super.CB, super.CC) > 48) {
                     GameScreen.addEffectEnd_ObjTo((short)56, 0, super.x, super.y, (short)super.ID, (byte)super.typeObject, (byte)super.Dir, this);
                     super.toX = super.CB;
                     super.toY = super.CC;
                     super.x = super.CB;
                     super.y = super.CC;
                  }

                  super.CB = -1;
                  super.CC = -1;
               }

               super.skillCurrent.AC();
            }
         }

         if (!MainObject.AB((MainObject)this) && !MainObject.AA(super.toX, super.toY, super.wOne, super.hOne) && !MainObject.AA(super.toXNew, super.toYNew, super.wOne, super.hOne)) {
            super.toX = super.toXNew;
            super.toY = super.toYNew;
            super.x = super.toX;
            super.y = super.toY;
            super.vx = 0;
            super.vy = 0;
            if (super.Action != 4) {
               super.Action = 0;
            }

            return;
         }

         if (super.typeActionBoat == 0 && super.Action != 2) {
            this.BR();
            int var1 = GameCanvas.loadmap.AA(super.x + super.vx, super.y + super.vy);
            this.AG(var1);
         }
      }

      super.update();
   }

   public final void BD() {
      if (super.typeObject == 2) {
         this.BE();
      } else if (super.typePlayer == 0) {
         GameScreen.AC = GameScreen.objFocus;
         mVector var2 = new mVector();
         if (ReadMessenge.AK >= 0) {
            GameCanvas.gameScr.BR.caption = ReadMessenge.AL;
            var2.addElement(GameCanvas.gameScr.BR);
         }

         var2.addElement(GameCanvas.gameScr.BF);
         if (GameCanvas.language == 0) {
            var2.addElement(GameCanvas.gameScr.BT);
         }

         var2.addElement(GameCanvas.gameScr.AT);
         var2.addElement(GameCanvas.gameScr.AV);
         var2.addElement(GameCanvas.gameScr.AY);
         var2.addElement(GameCanvas.gameScr.BD);
         var2.addElement(GameCanvas.gameScr.BK);
         if (super.clan == null && GameScreen.player.clan != null) {
            var2.addElement(GameCanvas.gameScr.BO);
         }

         if (super.clan != null && GameScreen.player.clan == null) {
            var2.addElement(GameCanvas.gameScr.BN);
         }

         var2.addElement(GameCanvas.gameScr.BW);
         GameCanvas.menu.startAt(var2, 2, T.CA);
      } else if (super.typePlayer == 1) {
         this.AA(T.FZ, true);
      } else {
         if (super.typePlayer == 2) {
            this.AA(T.playerShiper, true);
         }

      }
   }

   public final void a_() {
      if (GameScreen.player.AC(this)) {
         GameCanvas.isPointerSelect = false;
         GameScreen.player.beginPlayerFirePoint();
      } else {
         GameCanvas.isPointerSelect = false;
      }
   }

   public final void setFireObject(int var1) {
      if (GameScreen.player.AC(this) && !GameCanvas.loadmap.mapLang()) {
         GameScreen.player.beginPlayerFire(var1);
      } else {
         if (var1 == 2) {
            this.BD();
         }

      }
   }
}
