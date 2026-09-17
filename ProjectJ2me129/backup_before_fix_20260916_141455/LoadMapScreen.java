public final class LoadMapScreen extends MainScreen {
   public static boolean isNextMap = false;
   public static boolean isLoadDataMon = false;
   private static boolean isPaintBack = true;
   public static boolean isSuperBoss = false;
   private long time;
   public byte[] mItemMap;
   public static byte IDBack;
   public static byte typeChangeMap = 0;
   public static short HBack;
   private int tick = 0;
   private int tickPaintMap = 0;
   public static byte isMapSky = -1;
   private static byte countLoadMap = 0;
   public byte area = -1;

   public final void Show() {
      ++countLoadMap;
      isNextMap = false;
      GameCanvas.menuCur.isShowMenu = false;
      isPaintBack = true;
      GameScreen.isShowNameSUPER_BOSS = true;
      GameScreen.isShowNameXpArena = false;
      GameScreen.isShowNameWW = false;
      byte var10001 = countLoadMap;
      GameScreen.checkRemoveImage(1);
      super.Show();
      if (GameCanvas.lowGraphic) {
         AvMain.imgLg = null;
      }

      this.time = GameCanvas.timeNow;
      if (Interface_Game.isPaintInfoFocus) {
         Interface_Game.isPaintInfoFocus = false;
      }

      GameCanvas.saveImage.start();
      GameScreen.VecEffect.removeAllElements();
      GameScreen.VecNum.removeAllElements();
      GameScreen.vecObjMove.removeAllElements();
      GameScreen.isPvPNew = false;
      GameScreen.objPvPNew = null;
      Interface_Game.typeTitleRoomFight = 0;
      Interface_Game.vecClanDam.removeAllElements();
      this.tick = 0;
      this.tickPaintMap = 0;
      GameCanvas.mapBack = null;
      GameScreen.ClanDao = null;
      GameCanvas.isLowGraOrWP_PvP();
      GameScreen.vecBigBossLittleGraden.removeAllElements();
      if (GameCanvas.loadmap.mapLang()) {
         Interface_Game.mImgPvPType = null;
         MapOff_RedLine.mImgMapOffline = null;
         AvMain.imgTimePvpSmall = null;
         AvMain.imgTimePvp = null;
      }

      if (GameCanvas.loadmap.idMapLoadMap >= 988 && GameCanvas.loadmap.idMapLoadMap <= 995) {
         Interface_Game.indexPaintTable = 1;
      } else if (GameCanvas.loadmap.idMapLoadMap == 999 || GameCanvas.loadmap.idMapLoadMap == 997 || GameCanvas.loadmap.idMapLoadMap == 1000 || GameCanvas.loadmap.idMapLoadMap == 1001) {
         Interface_Game.indexPaintTable = 0;
      }

      var10001 = countLoadMap;
      GameScreen.checkRemoveImage(2);
      ListDungeon.instance = null;
      CreateChar_Screen.instance = null;
      PartyScreen.instance = null;
      PlayerListServer.instance = null;
      PvPScreen.instance = null;
      ScreenUpgrade.instance = null;
      SplitScreen.instance = null;
      TradeScreen.instance = null;
      ScreenJoinItem.instance = null;
      MapGotoSky.isBeginEffBoat = false;
      isSuperBoss = false;
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void paint(mGraphics var1) {
      try {
         GameCanvas.resetTrans(var1);
         var1.setColor(0);
         var1.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);
         if (GameCanvas.mapLogin != null && isPaintBack) {
            GameCanvas.isLowGraOrWP_PvP();
         }

         if (!GameCanvas.lowGraphic) {
            LoginScreen.paintLogo(var1, MotherCanvas.hw);
         }

         if (!GameCanvas.lowGraphic) {
            AvMain.fraBtBanhlai.drawFrame(LoginScreen.AH, MotherCanvas.w - 30, MotherCanvas.h - 30, 0, 3, var1);
         } else {
            MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick % MsgDialog.fraImgWaiting.nFrame, MotherCanvas.w - MsgDialog.fraImgWaiting.frameWidth, MotherCanvas.h - MsgDialog.fraImgWaiting.frameHeight, 0, 3, var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public final void update() {
      try {
         LoginScreen.updateYPaintLogo(LoginScreen.hLogo);
         LoginScreen.updateBanhLai();
         ++this.tick;
         if (GameCanvas.mapLogin != null && isPaintBack) {
            GameCanvas.isLowGraOrWP_PvP();
         }
         if (isNextMap && isLoadDataMon && (SaveImageRMS.vecSaveImage.size() <= 20 || (GameCanvas.timeNow - this.time) / 1000L > 15L)) {
            byte var10001 = countLoadMap;
            GameScreen.checkRemoveImage(3);
            if (isPaintBack) {
               ++this.tickPaintMap;
               if (this.tickPaintMap > 10) {
                  isPaintBack = false;
                  this.tickPaintMap = 0;
               }

               return;
            }

            int var1;
            for(var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
               MainObject var2;
               if ((var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)) == null || var2.isRemove) {
                  GameScreen.vecPlayers.removeElement(var2);
                  --var1;
               }
            }

            if (this.mItemMap != null) {
               GameCanvas.loadmap.load_ItemMap(this.mItemMap);
            }

            GameScreen.player.posTransRoad = null;
            GameScreen.player.skillCurrent = null;
            GameScreen.player.plashNow = null;
            GameScreen.player.Action = 0;
            GameScreen.player.resetAction();
            GameScreen.player.typeActionBoat = 0;
            GameScreen.player.toX = GameScreen.player.x;
            GameScreen.player.toY = GameScreen.player.y;
            var10001 = countLoadMap;
            GameScreen.checkRemoveImage(4);
            MainObject var8;
            if (GameScreen.player.NJ && (var8 = MainObject.getPet(GameScreen.player.ID)) != null) {
               var8.AF(GameScreen.player.x, GameScreen.player.y);
            }

            Interface_Game.CK = -1;
            Interface_Game.CL = -1;
            Interface_Game.BL.timeCountDown = 0;
            Interface_Game.BL.valueLeft = 0;
            Interface_Game.BL.valueright = 0;
            Interface_Game.BM.timeCountDown = 0;
            Player.setStart_EndAutoFire(false);
            if (LoadMap.specMap == 3) {
               Player.QY = CRes.random(MainObject.mPosMapTrain.length);
               GameScreen.player.posTransRoad = GameCanvas.loadmap.AA(MainObject.mPosMapTrain[Player.QY][0], MainObject.mPosMapTrain[Player.QY][1], GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 200, GameScreen.player);
            }

            if (LoadMap.specMap == 4) {
               Skill_Info var10 = null;

               int var9;
               for(var9 = 0; var9 < Player.vecListSkill.size(); ++var9) {
                  Skill_Info var3;
                  if ((var3 = (Skill_Info)Player.vecListSkill.elementAt(var9)).typeSkill == 4) {
                     var10 = var3;
                     break;
                  }
               }

               if (var10 != null) {
                  MainSkill var12;
                  (var12 = new MainSkill(var10.ID, var10.typeEffSkill)).AB = var10.indexHotKey;
                  var12.idIcon = var10.idIcon;
                  var12.isBuff = false;
                  boolean var13 = false;

                  for(var1 = 0; var1 < Player.hotkeyPlayer.length; ++var1) {
                     var13 = true;

                     for(int var4 = 0; var4 < Player.hotkeyPlayer[var1].length; ++var4) {
                        int var5 = var4;
                        if (var4 == 0) {
                           var5 = 2;
                        } else if (var4 == 2) {
                           var5 = 0;
                        }

                        Hotkey var6;
                        if ((var6 = Player.hotkeyPlayer[var1][var5]).skill != null && var6.skill.AM == 0) {
                           if (var13) {
                              Player.hotkeyPlayer[var1][var5].setSkill(var12, var12.idIcon);
                              var13 = false;
                           } else {
                              Player.hotkeyPlayer[var1][var5].skill = null;
                           }
                        }
                     }
                  }
               } else {
                  for(var9 = 0; var9 < Player.hotkeyPlayer.length; ++var9) {
                     for(int var11 = 0; var11 < Player.hotkeyPlayer[var9].length; ++var11) {
                        if (Player.hotkeyPlayer[var9][var11].skill != null) {
                           Player.hotkeyPlayer[var9][var11].skill = null;
                        }
                     }
                  }
               }
            } else if (GameScreen.CN == 0 && LoadMap.isOnlineMap && GameCanvas.loadmap.idMapLoadMap != 157 && GameCanvas.loadmap.idMapLoadMap != 159 && GameCanvas.loadmap.idMapLoadMap != 161) {
               if (typeChangeMap == 2) {
                  if (GameScreen.player.x < 100 || GameScreen.player.x > GameCanvas.loadmap.maxWMap - 100) {
                     var1 = GameScreen.player.x / 24 * 24;
                     if (GameScreen.player.x < 100) {
                        GameScreen.player.x = 0;
                     } else {
                        GameScreen.player.x = GameCanvas.loadmap.maxWMap - LoadMap.wTile;
                     }

                     GameScreen.player.toX = GameScreen.player.x;
                     GameScreen.player.toY = GameScreen.player.y;
                     GameScreen.player.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 20, GameScreen.player);
                     GameScreen.player.AC = 1;
                     Player.isSendMove = false;
                     if (GameScreen.player.posTransRoad == null || GameScreen.player.posTransRoad.length > 20) {
                        GameScreen.player.x = var1;
                        Player.isSendMove = true;
                     }
                  }
               } else if (typeChangeMap == 1) {
                  GameScreen.addEffectEnd_ObjTo((short)32, 0, GameScreen.player.x, GameScreen.player.y, (short)GameScreen.player.ID, (byte)GameScreen.player.typeObject, (byte)GameScreen.player.type_left_right, GameScreen.player);
               }
            }

            Player.isSendMove = false;
            if (GameCanvas.mapBack == null) {
               GameCanvas.mapBack = new MapBackGround();
            }

            GameCanvas.mapBack.AA(IDBack, HBack);
            if (GameScreen.effMap != null && GameScreen.effMap.AA == 1 && GameCanvas.mapBack != null) {
               GameCanvas.mapBack.AE();
            }

            if (LoadMap.specMap == 8) {
               MapGotoSky.AA((byte)0);
               MapGotoSky.AC();
               Player.SA = null;
            }

            if (LoadMap.specMap == 12) {
               MapGotoGod.AA((byte)0);
               MapGotoGod.AB();
               Player.SA = null;
            }

            LoadMap.AX = this.area;
            GameCanvas.gameScr.Show();
            Interface_Game.CI = 0;
            GameCanvas.BH = MotherCanvas.h / 4 * 3;
            if ((Interface_Game.CJ = mFont.tahoma_7b_white.getWidth(LoadMap.AB(GameCanvas.loadmap.idMapLoadMap)) + 20) < 100) {
               Interface_Game.CJ = 100;
            }

            if (LoadMap.mSea != null && GameScreen.CN == 0) {
               for(var1 = 0; var1 < LoadMap.mSea.length; ++var1) {
                  if (LoadMap.mSea[var1][0] == GameCanvas.loadmap.idMapLoadMap && LoadMap.mSea[var1][1] == GameCanvas.loadmap.idLastMap) {
                     GameScreen.player.addEffBuff((byte)LoadMap.mSea[var1][2], LoadMap.mSea[var1][3], LoadMap.mSea[var1][4]);
                     break;
                  }
               }
            }

            GlobalService.getInstance().changeMapOk();
            if (GameScreen.CU == null) {
               GameScreen.CU = new mVector();
               if (GameScreen.CV >= 0) {
                  MainHelp.AA(false);
               }
            }

            if (GameCanvas.loadmap.idMapLoadMap == 1 && GameScreen.CV == 14) {
               GameScreen.AB((int)14, (int)0);
            }

            AF();
            LoadMap.BB = mFont.tahoma_7_white.splitFontArray(LoadMap.AB(GameCanvas.loadmap.idMapLoadMap), 70);
            GlobalService.getInstance().AK();
            if (GameCanvas.loadmap.idMapLoadMap == 130) {
               GameScreen.player.Dir = 0;
               GameScreen.player.type_left_right = 0;
            } else if (GameCanvas.loadmap.idMapLoadMap == 129) {
               GameScreen.player.Dir = 2;
               GameScreen.player.type_left_right = 2;
            }

            if (Player.SO == 3) {
               Player.SO = 0;
            }

            if (GameCanvas.loadmap.mapLang()) {
               Player.xBeginAuto = 0;
               Player.yBeginAuto = 0;
            }

            if (GameScreen.player.Lv < 50 && GameScreen.CW < T.WY.length) {
               boolean var14 = false;
               if (GameCanvas.loadmap.idMapLoadMap == 9 && GameScreen.player.Lv < 20 && GameScreen.CW <= 0) {
                  var14 = true;
                  GameScreen.CW = 1;
               } else if (GameCanvas.loadmap.idMapLoadMap == 17 && GameScreen.CW <= 1 && GameScreen.player.Lv < 30) {
                  var14 = true;
                  GameScreen.CW = 2;
               } else if (GameCanvas.loadmap.idMapLoadMap == 25 && GameScreen.CW <= 2 && GameScreen.player.Lv < 40) {
                  var14 = true;
                  GameScreen.CW = 3;
               } else if (GameCanvas.loadmap.idMapLoadMap == 33 && GameScreen.CW <= 3) {
                  var14 = true;
                  GameScreen.CW = 4;
               } else if (GameCanvas.loadmap.idMapLoadMap == 41 && GameScreen.CW <= 4) {
                  var14 = true;
                  GameScreen.CW = 5;
               } else if (GameCanvas.loadmap.idMapLoadMap == 49 && GameScreen.CW <= 5) {
                  var14 = true;
                  GameScreen.CW = 6;
               }

               if (var14 && GameScreen.CW < T.WY.length) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.WY[GameScreen.CW - 1]);
                  GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)8, (byte[])(new byte[]{(byte)GameScreen.CW}));
               }
            }

            if (GameScreen.isOnRepeatQuest) {
               AutoRepeatQuest.AB();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XL);
            }

            if (GameScreen.isOnSuperBoss) {
               AutoSuperBoss.AB();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XY);
            }

            if (GameScreen.isOnAutoPB && !AutoBattlefield.checkMapPb()) {
               AutoBattlefield.StopAutoBattlefield();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.noAutoPB);
               return;
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void AF() {
      try {
         int var10000 = GameCanvas.loadmap.idMapLoadMap;
         mSound.AE();
         float var1 = mSound.AB;
         mSound.AC();
      } catch (Exception var0) {
      }
   }
}
