import java.util.Enumeration;
import java.util.concurrent.TimeoutException;

public final class Player extends MainPlayer {
   public static boolean isAutoFilterItems = false;
   public boolean isAutoFireNew108 = false;
   public boolean isDongBangQuai111 = false;
   private java.util.Map<MainMonster,int[]> oldMonsterPos = null;
   public boolean isGomQuai112 = false;
   public static boolean isAutoRevice = false;
   public boolean isAutoReMap = false;
   public short lastMapId = -1;   // map trước khi chết
   public short lastX, lastY;     // toạ độ trước khi chết
   
   public static boolean isSendMove = true;
   public static boolean isGhost = false;// Chết | Sống
   public static boolean isBlock = false;
   public static byte AutoFireCur = 0;
   public static byte currentTab = 0;
   public static byte typeAutoBuff = 0;
   public static byte typeAutoFireMain = 1;
   public static byte AutoRevice = 0;
   public static byte isGetDataClan = -1;
   private int demUnFire = 0;
   public static int wFocus = 140;
   public static int AR;
   public static int AS = 10;
   public static int AT = 0;
   public static mVector vecInventory = new mVector("Player.vecInventory");
   public static mVector vecChest = new mVector("Player.vecChest");
   public static mVector AW = new mVector("Player.vecInvenClan");
   public static mVector AX = new mVector("Player.vecMaxLevelAttri");
   public static short[] AY = new short[5];
   public static short[] AZ;
   public static byte[] QA = new byte[4];
   public static byte[] QB = new byte[4];
   public static Hotkey[][] hotkeyPlayer;
   public static Hotkey[] hotkeyBuffPlayer;
   public static MyHashTable QE = new MyHashTable();
   public static Class_CV[] QF = new Class_CV[5];
   public static short[] QG;
   public static mVector vecListSkill = new mVector("Player.vecListSkill");
   public static mVector QI = new mVector("Player.vecQuest");
   public static mVector vecGhostInput = new mVector("Player.vecGhostInput");
   public static mVector QK = new mVector("Player.vecUniform");
   public static byte QL;
   public static byte QM = 0;
   public static byte QN = 10;
   public static byte QO = 0;
   public static byte QP;
   public static String strTimeChange = "";
   public static String QR = "";
   public static boolean isSkillready = false;
   public static boolean QT;
   public static short QU = 0;
   public static short idFashion = -1;
   public static MainQuest QW = null;
   public static Item[] QX = new Item[2];
   public static int QY = 0;
   public static int maxInventory = 40;
   public static int maxChest = 10;
   public static int RB = -1;
   public static int RC = -1;
   public static int RD;
   public static long RE;
   public static short Ticket = 0;
   public static short RG = 0;
   public static short RH = 0;
   public static short MaxTicket = 0;
   public static short RJ = 0;
   public static short RK = 0;
   public static MainQuest RL;
   public static mVector vecParty = new mVector("Player.vecParty");
   public static mVector RN = new mVector("Player.vecFriendList");
   public static mVector vecEvent = new mVector("Player.vecEvent");
   private static boolean SQ;
   private static int SR;
   public static int[][] RP = new int[][]{{0, 2, 3, 4, 10, 11, 14}, new int[7]};
   public static String[] RQ = new String[]{"", "", "", "", "", ""};
   public static boolean isMPHP = true;
   public static boolean isGetItem = true;
   public static int xBeginAuto;
   public static int yBeginAuto;
   private static boolean isBack = false;
   private static int ST = 200;
   public static short giamCountDownAtt = 0;
   public static short giamCountDownParty = 0;
   private static short GiamCountDownCur = 0;
   private static short tickAutoRevice = 100;
   public static short RX = 0;
   public int RY = 0;
   public boolean RZ = false;
   public static Boat SA = null;
   public int SB = 2;
   public int SC = 2;
   private int SW = 0;
   private int SX = 0;
   public int SD = 0;
   public int SE = 0;
   public static boolean SF;
   public static int SG;
   public int SH = 0;
   public MainItem SI;
   public MainItem SJ;
   private int SY = 0;
   private int SZ = 0;
   public static boolean isFullInven;
   public boolean isBeginTrain = false;
   private int TA = 0;
   private int[] mindexkey = new int[]{-1, -1, -1, -1};
   private int TC = 0;
   public long timeFristSkill = 0L;
   public static int IndexFire;
   private MainObject TE = null;
   private int TF = 0;
   private static int TG;
   public static long SN;
   private static long TH;
   public static byte SO;
   private static int TI;

   static {
      int[] var10000 = new int[]{0, 0, 1, 1, 2, 2, 2, 2, 1, 1};
      SF = false;
      SG = 0;
      isFullInven = false;
      IndexFire = 0;
      TG = 0;
      SO = 0;
      TI = 0;
   }

   public Player() {
      super.hOne = 52;
      super.wOne = 26;
      this.setSpeed(7, 7);
      super.x = MotherCanvas.w / 2;
      super.y = MotherCanvas.h / 2;
      super.Hp = 100;
      super.Mp = 100;
      super.maxHp = 100;
      super.maxMp = 100;
      super.KO = 0;
      super.hIconFocus = 0;
      super.Action = 0;
      hotkeyPlayer = new Hotkey[2][];
      hotkeyBuffPlayer = new Hotkey[6];

      for(int var1 = 0; var1 < hotkeyPlayer.length; ++var1) {
         hotkeyPlayer[var1] = new Hotkey[6];

         for(int var2 = 0; var2 < hotkeyPlayer[var1].length; ++var2) {
            hotkeyPlayer[var1][var2] = new Hotkey();
         }
      }

      super.BO = new short[]{4, 5, 6, 7};
      super.BP = new int[2];
      super.KQ = 0;
   }

   public final void paint(mGraphics var1) {
      if (GameScreen.CN != 1) {
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

         if (!isGhost && GameScreen.typePaintGameScreen != 1) {
            this.AA(var1, (byte)0, 0);
         }

         if (GameScreen.IU) {
            mFont.tahoma_7b_black.drawString(var1, super.x + " - " + super.y, super.x, super.y + 5, 2);
         }

      }
   }

   public final void AE(mGraphics var1) {
      this.AB(var1, (int)0);
      int var2 = super.y - super.dy;
      if (SA != null) {
         SA.AB(var1);
         if (super.dy == 0) {
            var2 = super.y - super.CH / 10;
         }

         SA.AE(var1);
      } else if (!super.isTanHinh) {
         this.AC(var1, super.x - super.dx);
      }

      if (!super.isTanHinh) {
         this.AD(var1, super.x - super.dx, var2);
         this.AI(var1);
         int var3 = GameScreen.vecPlayers.size();

         for(int var4 = GameScreen.vecPlayers.size() - 1; var4 >= 0; --var4) {
            MainObject var5;
            if ((var5 = (MainObject)GameScreen.vecPlayers.elementAt(var4)) != this) {
               var5.type_left_right = super.type_left_right;
               int var7 = super.x - super.dx + var3 * 7;
               var5.AG(var1, var7, var2);
               --var3;
            }
         }

         this.AG(var1, super.x - super.dx, var2);
      }

      if (SA != null) {
         SA.AG(var1);
      }

      if (!super.isTanHinh) {
         this.AE(var1, super.x - super.dx, var2);
         this.AH(var1);
      }

   }

   public static void AL() {
   }
   
   public void updateGomQuai() {
        if (isGomQuai112) {
            if (oldMonsterPos == null) {
                // Bật gom: gom về giữa map, follow = false (vì bạn yêu cầu giữa map cố định)
                // Nếu muốn quái vẫn follow player, đổi follow=true
                oldMonsterPos = AThMadaraFunc.startGomAllFollow(this /*player*/, 0 /*dx*/, 0 /*dy*/, false /*follow*/, true /*useMapCenter*/);
            } else {
                // Gom đã bật: cập nhật (nếu follow=true hoặc để lock giữa map chúng ta vẫn gọi update để enforce)
                AThMadaraFunc.updateGomPositions(this);
                // Khóa player ở vị trí gom center nếu đang tàn sát:
                if (this.isAutoFireNew108) {
                    // đảm bảo player cố định ở center
                    try {
                        if (AThMadaraFunc.gomUseMapCenter) {
                            this.x = AThMadaraFunc.gomFixedX;
                            this.y = AThMadaraFunc.gomFixedY;
                            this.toX = this.x; this.toY = this.y;
                            this.posTransRoad = null;
                            this.vx = 0; this.vy = 0;
                        }
                    } catch (Throwable ignored) {}
                }
            }
        } else {
            if (oldMonsterPos != null) {
                AThMadaraFunc.stopGomAll(oldMonsterPos);
                oldMonsterPos = null;
            }
        }
    }

   public final void update() {
      if (super.typeActionBoat != 0) {
         this.BS();
      }

      this.AF();
      if (this.CM()) {
         super.vx = 0;
         super.vy = 0;
         if (super.Action == 1) {
            super.Action = 0;
         }

         if (super.skillCurrent != null) {
            if (super.skillCurrent.AD) {
               super.skillCurrent = null;
            } else {
               super.skillCurrent.beginSkill();
               if (super.LD <= 0) {
                  super.CS = 20;
               }

               super.LD = 150;
            }
         }

         if (super.posTransRoad != null) {
            super.posTransRoad = null;
         }

         if (this.AI((short)1)) {
            super.Dir = super.KX;
         }
      }

      this.BJ();
      if (super.Action != 2 && !super.NR) {
         this.AB(true);
      }

      if (LoadMap.specMap == 3) {
         this.AB();
      }

      if (super.Hp <= 0) {
         super.Hp = 0;
         super.Mp = 0;
         if (super.Action != 4) {
            if (super.plashNow != null) {
               super.plashNow = null;
            }

            if (super.skillCurrent != null) {
               super.skillCurrent.AD = true;
            }

            if (super.posTransRoad != null) {
               super.posTransRoad = null;
            }

            setStart_EndAutoFire(false);
            Interface_Game.vecEffCurrent.removeAllElements();
            this.setCmdGame();
            ++super.timeDie;
            if (super.timeDie >= 40L) {
               this.beginDie((MainObject)null);
            }
         }

         // Hồi sinh tự động (cơ chế cũ)
         if (AutoRevice != 0) {
            if (tickAutoRevice <= 0) {
               GlobalService.getInstance().Auto_revice((byte)1);
               tickAutoRevice = 100;
            } else {
               --tickAutoRevice;
            }
         }
         // Hồi sinh tự động (cơ chế mới + remap về map cũ)
         if (Player.isAutoRevice && GameCanvas.gameTick % 30 == 0) {
            try { AThMadaraFunc.autoReviceV1(this); } catch (Throwable ignored) {}
         }
      }

      int var10000;
      if (super.posTransRoad != null) {
         if (CRes.abs(super.x - super.toX) <= super.CN && CRes.abs(super.y - super.toY) <= super.CN) {
            if (super.AC > super.posTransRoad.length - 1) {
               super.AC = 0;
               super.posTransRoad = null;
               super.AD = 0;
               super.AE = 0;
            } else {
               var10000 = super.AC;
               short[] var18 = super.posTransRoad;
               byte var5 = (byte)(super.posTransRoad[super.AC] >> 8);
               byte var6 = (byte)super.posTransRoad[super.AC];
               super.toX = var5 * LoadMap.wTile + LoadMap.wTile / 2;
               super.toY = var6 * LoadMap.wTile + LoadMap.wTile / 2;
               ++super.AC;
            }
         }

         this.AW();
      } else if (!isSendMove) {
         isSendMove = true;
         super.CX = super.x;
         super.CY = super.y;
      }

      if (MainObject.getDistance(super.CX, super.CY, super.x, super.y) >= 50 || super.AB > 20 || super.tickAfterSkill == 0) {
         super.CX = super.x;
         super.CY = super.y;
         if (super.AB > 20) {
            super.AB = -1;
         }

         if (super.tickAfterSkill == 0) {
            super.tickAfterSkill = -1;
         }

         if (isSendMove && !this.CM() && LoadMap.specMap != 3 && super.typeActionBoat == 0) {
            GlobalService.getInstance().Obj_Move((short)super.x, (short)super.y);
         }
      }

      int i;
      if (AZ != null) {
         AY = new short[AZ.length];

         for(i = 0; i < AZ.length; ++i) {
            AY[i] = AZ[i];
         }

         AZ = null;
      }

      int var2;
      if (LoadMap.specMap == 4 && super.boatSea != null && super.boatSea.ID == super.ID && GameCanvas.gameTick % 20 == 0) {
         Player var4 = this;
         super.boatSea.AB(0, 0);

         for(int var14 = 0; var14 < GameScreen.vecPlayers.size(); ++var14) {
            MainObject var17;
            if ((var17 = (MainObject)GameScreen.vecPlayers.elementAt(var14)) != var4 && var17.boatSea != null && var17.boatSea.ID == var17.ID && var17.vx == 0 && var17.vy == 0) {
               var17.boatSea.AB(0, 0);
               if (CRes.AA(var4.boatSea, var17.boatSea)) {
                  if (CRes.abs(var17.IM - var17.x) < 48 && CRes.abs(var17.IN - var17.y) < 48) {
                     var17.toX = var17.IM;
                     var17.toY = var17.IN;
                     var17.IM = var17.x;
                     var17.IN = var17.y;
                  } else {
                     var10000 = var17.x;
                     int var1 = 0;
                     var10000 = var17.y;
                     var2 = 0;

                     for(i = 0; i < 4; ++i) {
                        var1 = var17.x + CRes.random_Am(24, 48);
                        var2 = var17.y + CRes.random_Am(24, 48);
                        if (!LoadMap.AA(GameCanvas.loadmap.AA(var1, var2))) {
                           break;
                        }
                     }

                     var17.toX = var1;
                     var17.toY = var2;
                     var17.IM = var17.x;
                     var17.IN = var17.y;
                  }
               }
            }
         }
      }
      this.AC(false);
      this.setCmdGame();
      ++this.SY;
      ++this.SZ;
      if (super.Action != 4 && super.Hp > 0 && !isGhost) {
         if (isMPHP) {
            if (this.AF((byte)1)) {
               if (this.SI == null || this.SI.numPotion <= 0 || this.SY >= 500) {
                  this.SI = AG((byte)1);
                  this.SY = 0;
               }

               if (this.SI != null && this.SI.numPotion > 0) {
                  this.SI.Use_Item();
               }
            }

            if (this.AF((byte)2)) {
               if (this.SJ == null || this.SJ.numPotion <= 0 || this.SZ >= 500) {
                  this.SJ = AG((byte)2);
                  this.SZ = 0;
               }

               if (this.SJ != null && this.SJ.numPotion > 0) {
                  this.SJ.Use_Item();
               }
            }
         }

         Player pp2;//autofire old
         if (AutoFireCur > 0 && super.Action != 2 && Interface_Game.isAutoFireInterface) {
            this.setAutoFire(AutoFireCur == 2);
            if (typeAutoBuff == 1 && MsgAutoFire.value != null && GameCanvas.gameTick % 5 == 1) {
               pp2 = this;
               Skill_Info skillFromID;
               for (i = 0; 
                    i < MsgAutoFire.value.length
                    && (MsgAutoFire.value[i][1] != 1
                        || (skillFromID = Skill_Info.getSkillFromID(MsgAutoFire.value[i][0])) == null
                        || getManaNeedUse((int) skillFromID.manaLost) > pp2.Mp
                        || !DelaySkill.getDelay(skillFromID.indexHotKey).isCoolDown()
                        || !pp2.beginPlayerFire(skillFromID));
                    ++i) {
               }
            }
         }
         
         if (isAutoFireNew108) {//autofirenew
             pp2 = this;
             AThMadaraFunc.autoFireMoveToTarget(pp2);//tự động đánh dịch chuyển đến mục tiêu
         }
         AThMadaraMOD.cleanAutoSellCache();
         AThMadaraMOD.autoProcessItems();
         if (AThMadaraMOD.shouldReturnToLastMap) {
            Player p2p = this;
            if (p2p != null && p2p.Hp > 0) {
                // Đã hồi sinh xong → quay lại map cũ
                AThMadaraMOD.returnToLastMap();
            }
        }
//         Agetskill.run();
//         Ageticon.run();//kéo icon
//         Ageteff.run();//kéo effect
//         Agetpart.run();//kéo part
//         Agetmap.run(0, 32767, 1);//kéo map
//        Ageteffauto.run();
         updateGomQuai();//mod gom quái
         if (isGetItem) {
            label501: {
               pp2 = this;
               if (GameCanvas.gameTick % 200 == 0) {
                  if (isFullInven) {
                     Interface_Game.addInfoPlayerNormal(T.fullInven, mFont.tahoma_7_white);
                     break label501;
                  }

                  if (vecInventory.size() >= maxInventory) {
                     isFullInven = true;
                  }
               }

               for(i = 0; i < GameScreen.vecPlayers.size(); ++i) {
                  MainObject var12;
                  if ((var12 = (MainObject)GameScreen.vecPlayers.elementAt(i)) != null && !var12.NQ && var12.BQ == null) {
                     boolean var16 = false;
                     if (var12.BD != "") {
                        var16 = false;
                     } else if (var12.typeObject != 5 && var12.typeObject != 7) {
                        if (var12.typeObject == 4 || var12.typeObject == 3) {
                           boolean var19;
                           label359: {
                              if (var12.typeObject == 4) {
                                 if (var12.colorName == 5) {
                                    if (Class_AH.AA[2] == 0) {
                                       var19 = true;
                                       break label359;
                                    }
                                 } else if (Class_AH.AA[1] != 3) {
                                    if (Class_AH.AA[1] == 0) {
                                       var19 = true;
                                       break label359;
                                    }

                                    if (var12.colorName == 4 && Class_AH.AA[1] == 2) {
                                       var19 = true;
                                       break label359;
                                    }

                                    if (var12.colorName == 6 && Class_AH.AA[1] == 1) {
                                       var19 = true;
                                       break label359;
                                    }
                                 }
                              } else if (var12.typeObject == 3 && !isFullInven && Class_AH.AA[0] != 3) {
                                 if (Class_AH.AA[0] == 0) {
                                    var19 = true;
                                    break label359;
                                 }

                                 if (var12.colorName > Class_AH.AA[0]) {
                                    var19 = true;
                                    break label359;
                                 }
                              }

                              var19 = false;
                           }

                           if (var19) {
                              var16 = true;
                           }
                        }
                     } else {
                        var16 = true;
                     }

                     if (var16 && MainObject.getDistance(pp2.x, pp2.y, var12.x, var12.y) < wFocus) {
                        GlobalService.getInstance().Get_Item_Map(var12.ID, var12.typeObject);
                        var12.NQ = true;
                     }
                  }
               }
            }
         }
      } else {
         setStart_EndAutoFire(false);
      }

      if (super.NR) {
         this.BB();
         if (super.posTransRoad != null) {
            super.posTransRoad = null;
         }

         if (CRes.abs(super.x - super.toX) < super.CN && CRes.abs(super.y - super.toY) < super.CN) {
            super.NR = false;
         }
      } else if (super.posTransRoad == null && super.skillCurrent != null) {
         if (super.skillCurrent.AD) {
            super.skillCurrent = null;
         } else {
            super.skillCurrent.AA();
         }
      }

      Enumeration var10 = QE.GetEnumerator();

      while(var10.hasMoreElements()) {
         String var15 = (String)var10.nextElement();
         DelaySkill var13;
         if ((var13 = (DelaySkill)QE.get(var15)).AC > -150) {
            var13.AC = (int)((long)var13.AC - (GameCanvas.timeNow - var13.AB));
            var13.AB = GameCanvas.timeNow;
         }
      }

      if (!SQ && ++SR > 60) {
         SQ = true;
         SR = 0;
      }

      if (GameScreen.CD && super.Action != 2) {
         GameScreen.CD = false;
      }

      label321: {
         if (SN > super.Beri) {
            Interface_Game.BY = 60;
            Interface_Game.BZ = 0;
            if (TH == 0L) {
               if ((TH = (SN - super.Beri) / 10L) < 100L) {
                  TH = 100L;
               }
               break label321;
            }

            if ((SN -= TH) > super.Beri) {
               break label321;
            }
         }

         SN = super.Beri;
         TH = 0L;
      }

      if (SO != 4 && SO != 3 && SO != 6) {
         label311: {
            Player var8 = this;
            if (SO == 0) {
               TI = 0;
               if (!GameCanvas.loadmap.mapLang()) {
                  if (xBeginAuto != 0 && yBeginAuto != 0) {
                     SO = 1;
                  } else {
                     SO = 4;
                  }
                  break label311;
               }
            } else {
               if (SO == 1) {
                  GameScreen.player.toX = GameScreen.player.x;
                  GameScreen.player.toY = GameScreen.player.y;
                  super.posTransRoad = GameCanvas.loadmap.AA(xBeginAuto / LoadMap.wTile, yBeginAuto / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 200, this);
                  SO = 2;
                  break label311;
               }

               if (SO == 2) {
                  if (super.posTransRoad == null) {
                     this.nextMonster();
                     setStart_EndAutoFire(true);
                     SO = 5;
                     QR = QR + T.RI + GameCanvas.AT() + "\n";
                     GameCanvas.chatTabScr.AB(T.RH, "", QR, (byte)1, false);
                  }
                  break label311;
               }

               if (SO != 5 || ++TI % 200 != 0) {
                  break label311;
               }

               if (AutoFireCur != typeAutoFireMain) {
                  var2 = 0;

                  do {
                     ++var2;
                     var8.nextMonster();
                  } while(var2 != 10 && (GameScreen.objFocus == null || GameScreen.objFocus.typeObject != 1));

                  setStart_EndAutoFire(true);
                  TI = 0;
                  break label311;
               }
            }

            SO = 4;
         }
      }

      if (this.RZ) {
         ++this.RY;
         if (this.RY > 87) {
            GameCanvas.gameScr.AA(false);
            GlobalService.getInstance().VotBanhChung((byte)1, (byte)1, (byte)3);
         }
      }

      super.update();
   }

   public final void CA() {
      if (super.boatSea != null) {
         super.y += super.vy;
         super.x += super.vx;
         if (this.SX > 0) {
            --this.SX;
         }

         if (this.SB != this.SC) {
            this.SW = 6;
            if (this.SB > this.SC) {
               super.vy = 4;
            } else {
               super.vy = -4;
            }

            this.SC = this.SB;
            this.SX = 50;
         }

         if (SF) {
            if (this.SW == 0) {
               super.y = MapOff_RedLine.AL - 48 + this.SC * 24;
            }

            if (this.SB == 0) {
               this.SB = 1;
            } else if (this.SB == 3) {
               this.SB = 2;
            } else if (CRes.random(2) == 0) {
               ++this.SB;
            } else {
               --this.SB;
            }

            SF = false;
         }

         if (super.boatSea != null) {
            super.boatSea.AA(super.x, super.y, 0, (byte)super.type_left_right);
         }

         if (super.vy != 0) {
            super.boatSea.AH();
         }

         if (this.SW > 0) {
            --this.SW;
            if (this.SW == 0) {
               super.vy = 0;
               super.y = MapOff_RedLine.AL - 48 + this.SC * 24;
            }
         }

         if (GameCanvas.gameTick % 8 == 0) {
            super.boatSea.AA(super.x, super.y, -1, (byte)super.type_left_right, -3);
         }

         if (CRes.random(4) == 0) {
            super.boatSea.AA(super.x, super.y, 0, (byte)(super.type_left_right == 0 ? 2 : 0), -3);
         }

         if (GameCanvas.gameTick % 6 == 0) {
            super.boatSea.AA(super.x, super.y, 2, (byte)super.type_left_right, -3);
         }

         if (super.boatSea != null) {
            super.boatSea.AF();
         }

         if (this.SX <= 0 && CRes.random(120) == 0 && this.SC == this.SB && this.SE == 0) {
            if (this.SB == 0) {
               this.SB = 1;
            } else if (this.SB == 3) {
               this.SB = 2;
            } else if (CRes.random(2) == 0) {
               ++this.SB;
            } else {
               --this.SB;
            }
         }

         int var1;
         Point var2;
         if (this.SE != 0 && this.SE != 2) {
            if (this.SE == 1) {
               if (SG == 0) {
                  for(var1 = 0; var1 < MapOff_RedLine.AD.size(); ++var1) {
                     if ((var2 = (Point)MapOff_RedLine.AD.elementAt(var1)).dis == this.SB && var2.x + var2.x2 > MapOff_RedLine.xHardCodeMapRedLine + 50 && var2.x + var2.x2 < MapOff_RedLine.xHardCodeMapRedLine + 65) {
                        SG = 1;
                        MapOff_RedLine.AN = 0;
                        GameScreen.player.vx = -3;
                        if (this.SC < 2) {
                           GameScreen.player.vy = 1;
                           return;
                        }

                        GameScreen.player.vy = -1;
                        return;
                     }
                  }
               } else if (SG == 1 && super.x < 0) {
                  GlobalService.getInstance().AC((byte)1, (byte)0);
                  SG = 11;
               }
            }
         } else {
            for(var1 = 0; var1 < MapOff_RedLine.AD.size(); ++var1) {
               if ((var2 = (Point)MapOff_RedLine.AD.elementAt(var1)).dis == this.SB && var2.x + var2.x2 > MapOff_RedLine.xHardCodeMapRedLine + 80 && var2.x + var2.x2 < MapOff_RedLine.xHardCodeMapRedLine + 120) {
                  SF = true;
                  return;
               }
            }

            if (SG == 2) {
               ++this.SD;
               if (MapOff_RedLine.AD.size() == 0 || this.SD > 100) {
                  SG = 12;
                  MapOff_RedLine.AN = 0;
                  GameScreen.player.vx = 5;
               }
            }

            if (SG == 12 && super.x > MotherCanvas.w - 50) {
               MapOff_RedLine.AC();
            }
         }
      }

      this.BO();
      this.AF(super.LG);
   }

   public final void CB() {
      super.vx = 3;
      if (GameCanvas.gameTick % 10 == 0 && CRes.random(4) == 0) {
         if (super.vy == 0) {
            super.vy = 1;
         } else {
            super.vy = 0;
         }
      }

      super.y += super.vy;
      super.x += super.vx;
      if (super.boatSea != null) {
         super.boatSea.AA(super.x, super.y, 0, (byte)super.type_left_right);
      }

      if (super.vy != 0) {
         super.boatSea.AH();
      }

      if (GameCanvas.gameTick % 8 == 0) {
         super.boatSea.AA(super.x, super.y, -1, (byte)super.type_left_right, -3);
      }

      if (CRes.random(4) == 0) {
         super.boatSea.AA(super.x, super.y, 0, (byte)(super.type_left_right == 0 ? 2 : 0), -3);
      }

      if (GameCanvas.gameTick % 6 == 0) {
         super.boatSea.AA(super.x, super.y, 2, (byte)super.type_left_right, -3);
      }

      if (super.boatSea != null) {
         super.boatSea.AF();
      }

      this.BO();
      this.AF(super.LG);
      if (SG == 12 && super.x > MotherCanvas.w - 50) {
         GlobalService.getInstance().AC((byte)2, (byte)0);
         SG = 22;
      }

   }

   public final void CC() {
      if (SA == null || !SA.AI) {
         for(int var1 = GameScreen.vecPlayers.size() - 1; var1 >= 0; --var1) {
            MainObject var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1);
            if (vecParty.size() > 0 && ((Class_DS)vecParty.elementAt(0)).AG == var2.ID) {
               boolean var3 = true;
               if (SA == null) {
                  SA = new Boat(var2.ID, var2.x, var2.y, 0, (byte)var2.type_left_right);
               } else {
                  var3 = false;
               }

               if (var2.BO != null) {
                  SA.AA(var2.BO, var2.typePirate);
               } else {
                  var3 = false;
               }

               if (var2.clan != null) {
                  SA.AA = var2.clan.idIcon;
               } else {
                  var3 = false;
               }

               SA.AI = var3;
            }
         }
      }

      if (SA != null) {
         if (SA != null) {
            SA.AA(super.x, super.y, 0, (byte)super.type_left_right);
         }

         if (super.vy != 0) {
            SA.AH();
         }

         if (this.SH != 2) {
            if (GameCanvas.gameTick % 8 == 0) {
               SA.AA(super.x, super.y, -1, (byte)super.type_left_right, -3);
            }

            if (CRes.random(4) == 0) {
               SA.AA(super.x, super.y, 0, (byte)(super.type_left_right == 0 ? 2 : 0), -3);
            }

            if (GameCanvas.gameTick % 6 == 0) {
               SA.AA(super.x, super.y, 2, (byte)super.type_left_right, -3);
            }
         }

         SA.AF();
      }

      if (this.SH == 0) {
         this.BB();
         super.y += super.vy;
         super.x += super.vx;
      } else if (this.SH == 1) {
         this.BB();
         super.y += super.vy;
         super.x += super.vx;
      } else if (this.SH == 2) {
         super.y += super.vy;
         super.x += super.vx;
         if (super.vy < 10) {
            ++super.vy;
         }
      } else if (this.SH == 3) {
         this.BB();
         super.y += super.vy;
         super.x += super.vx;
      }

      if (!MapGotoSky.AB) {
         MainScreen.cameraMain.AA(GameScreen.player.x - MotherCanvas.w / 2, GameScreen.player.y - (MotherCanvas.h / 3 << 1));
      }

      this.BO();
      this.AQ();
      this.AR();
   }

   public final void AC(byte var1) {
      if (SA == null || !SA.AI) {
         for(int var2 = GameScreen.vecPlayers.size() - 1; var2 >= 0; --var2) {
            MainObject var3 = (MainObject)GameScreen.vecPlayers.elementAt(var2);
            if (vecParty.size() > 0 && ((Class_DS)vecParty.elementAt(0)).AG == var3.ID) {
               boolean var4 = true;
               if (SA == null) {
                  SA = new Boat(var3.ID, var3.x, var3.y, 0, (byte)var3.type_left_right);
               } else {
                  var4 = false;
               }

               if (var3.BO != null) {
                  SA.AA(var3.BO, var3.typePirate);
               } else {
                  var4 = false;
               }

               if (var3.clan != null) {
                  SA.AA = var3.clan.idIcon;
               } else {
                  var4 = false;
               }

               SA.AI = var4;
            }
         }
      }

      if (SA != null) {
         if (SA != null) {
            SA.AA(super.x, super.y, 0, (byte)super.type_left_right);
         }

         if (super.vy != 0) {
            SA.AH();
         }

         if (this.SH != 2) {
            if (GameCanvas.gameTick % 8 == 0) {
               SA.AA(super.x, super.y, -1, (byte)super.type_left_right, -3);
            }

            if (CRes.random(4) == 0) {
               SA.AA(super.x, super.y, 0, (byte)(super.type_left_right == 0 ? 2 : 0), -3);
            }

            if (GameCanvas.gameTick % 6 == 0) {
               SA.AA(super.x, super.y, 2, (byte)super.type_left_right, -3);
            }
         }

         SA.AF();
      }

      if (this.SH == 0) {
         this.BB();
         super.y += super.vy;
         super.x += super.vx;
      } else if (this.SH == 1) {
         this.BB();
         super.y += super.vy;
         super.x += super.vx;
      } else if (this.SH == 2) {
         if (var1 == 0) {
            this.BB();
            super.y += super.vy;
            super.x += super.vx;
         } else {
            super.y += super.vy;
            super.x += super.vx;
            if (super.vy < 10) {
               ++super.vy;
            }
         }
      } else if (this.SH == 3) {
         super.y += super.vy;
         super.x += super.vx;
         if (super.vx < 4) {
            ++super.vx;
         }
      }

      this.BO();
      this.AQ();
      this.AR();
   }

   private boolean AF(byte var1) {
      if (var1 == 1) {
         if (GameCanvas.loadmap.idMapLoadMap == 986 || GameCanvas.loadmap.idMapLoadMap == 985) {
            return false;
         }

         if (super.Hp * 100 / super.maxHp <= Class_AI.AB) {
            return true;
         }
      } else if (var1 == 2 && super.Mp * 100 / super.maxMp <= Class_AI.AA) {
         return true;
      }

      return false;
   }

   private static MainItem AG(byte var0) {
      MainItem var1 = null;

      for(int var2 = 0; var2 < vecInventory.size(); ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)vecInventory.elementAt(var2)).typeObject == 4 && var0 == var3.BQ && var3.numPotion > 0 && (var1 == null || Class_AI.AC == 0 && var3.BV < var1.BV || Class_AI.AC == 1 && var3.BV > var1.BV)) {
            var1 = var3;
         }
      }

      return var1;
   }

   public final void AB() {
      if (GameCanvas.gameScr.DA != GameCanvas.gameScr.AN || GameCanvas.gameScr.DB != GameCanvas.gameScr.AO) {
         GameCanvas.gameScr.DA = GameCanvas.gameScr.AN;
         GameCanvas.gameScr.DB = GameCanvas.gameScr.AO;
         super.center = null;
      }

      if (GameCanvas.gameTick % 125 == 0) {
         GlobalService.getInstance().Get_Xp_Map_Train((byte)0);
      }

      if ((GameCanvas.gameTick % 75 == 0 || !this.isBeginTrain) && CRes.random(3) == 0 && super.Action != 2) {
         GameScreen.player.toX = GameScreen.player.x;
         GameScreen.player.toY = GameScreen.player.y;
         if (GameScreen.player.posTransRoad != null) {
            GameScreen.player.AC = 1;
         }

         for(int var1 = 0; var1 < 10; ++var1) {
            int var2;
            if ((var2 = CRes.random(MainObject.mPosMapTrain.length)) != QY) {
               QY = var2;
               break;
            }
         }

         super.posTransRoad = GameCanvas.loadmap.AA(MainObject.mPosMapTrain[QY][0], MainObject.mPosMapTrain[QY][1], GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 200, this);
         AutoFireCur = 0;
         this.isBeginTrain = true;
      }

      if (super.posTransRoad == null) {
         setStart_EndAutoFire(true);
      }

   }

   public final void updateKey() {
      if (LoadMap.specMap != 3 && super.typeActionBoat == 0) {
         if (!isBlock) {
            if (!isGhost) {
               boolean var1 = false;
               if (super.Action != 4 && super.Action != 2 && super.Action != 3 && isSendMove && super.posTransRoad == null && super.Hp > 0) {
                  super.vx = 0;
                  super.vy = 0;
                  Player var2 = this;

                  int var3;
                  for(var3 = 0; var3 < 4; ++var3) {
                     if (GameCanvas.isKeyPressed(var3)) {
                        if (var2.mindexkey[var3] == -1) {
                           var2.mindexkey[var3] = var2.TA++;
                        }
                     } else {
                        var2.mindexkey[var3] = -1;
                     }
                  }

                  var3 = -1;
                  int var4 = -1;

                  for(int var5 = 0; var5 < var2.mindexkey.length; ++var5) {
                     if (var2.mindexkey[var5] > var3) {
                        var3 = var2.mindexkey[var5];
                        var4 = var5;
                     }
                  }

                  if (var4 >= 0) {
                     switch(var4) {
                     case 0:
                        var2.Action = 1;
                        var2.Dir = 0;
                        var2.vx = -var2.CN;
                        var2.vy = 0;
                        break;
                     case 1:
                        var2.Action = 1;
                        if (LoadMap.specMap == 4) {
                           var2.vy = -var2.CO;
                        } else {
                           var2.vy = -var2.CN;
                        }

                        var2.vx = 0;
                        break;
                     case 2:
                        var2.Action = 1;
                        var2.Dir = 2;
                        var2.vx = var2.CN;
                        var2.vy = 0;
                        break;
                     case 3:
                        var2.Action = 1;
                        if (LoadMap.specMap == 4) {
                           var2.vy = var2.CO;
                        } else {
                           var2.vy = var2.CN;
                        }

                        var2.vx = 0;
                     }
                  } else {
                     var2.TA = 0;
                  }

                  if (super.vx == 0 && super.vy == 0) {
                     if (super.AB != -1) {
                        ++super.AB;
                     }
                  } else {
                     var1 = true;
                     super.AB = 0;
                     setStart_EndAutoFire(false);
                  }
               }

               if (var1) {
                  super.skillCurrent = null;
               }

               if (GameCanvas.AE(6) && GameScreen.objFocus != null) {
                  GameCanvas.AF(6);
                  if (!GameCanvas.loadmap.mapLang()) {
                     GameScreen.interfaceGame.selectPointer(2);
                  }
               }

               if (GameCanvas.AE(8)) {
                  GameCanvas.AF(8);
                  GameCanvas.gameScr.BB.AD();
               }
            }

            if (GameCanvas.AE(1)) {
               this.setActionHotKey((int)0);
            } else if (GameCanvas.AE(3)) {
               this.setActionHotKey((int)1);
            } else if (GameCanvas.AE(5)) {
               this.setActionHotKey((int)2);
            } else if (GameCanvas.AE(7)) {
               this.setActionHotKey((int)3);
            } else if (GameCanvas.AE(9)) {
               this.setActionHotKey((int)999);
            }

            if (GameCanvas.AE(10)) {
               GameCanvas.AF(10);
               GameCanvas.gameScr.AX.AD();
            }

            if (GameCanvas.AL[40] || GameCanvas.isTouch && GameCanvas.AL[12]) {
               GameCanvas.AB(40);
               GameCanvas.AB(12);
               GameCanvas.gameScr.AP.AD();
            }

            if (GameCanvas.AL[41] || GameCanvas.isTouch && GameCanvas.AL[13]) {
               GameCanvas.AB(41);
               GameCanvas.AB(13);
               GameCanvas.gameScr.AL.AD();
            }

            if (GameCanvas.AE(4)) {
               GameCanvas.AF(4);
               if (GameCanvas.isTouch) {
                  Interface_Game.AH();
               } else {
                  this.AH((byte)4);
               }
            }

            if (GameCanvas.isKeyPressed()) {
               if (GameCanvas.AE(11)) {
                  GameCanvas.AF(11);
                  this.AH((byte)11);
                  return;
               }

               if (GameCanvas.AE(12)) {
                  GameCanvas.AF(12);
                  this.AH((byte)12);
                  return;
               }

               if (GameCanvas.AE(13)) {
                  GameCanvas.AF(13);
                  this.AH((byte)13);
                  return;
               }

               if (GameCanvas.AE(14)) {
                  GameCanvas.AF(14);
                  this.AH((byte)14);
                  return;
               }

               if (GameCanvas.AE(16)) {
                  GameCanvas.AF(16);
                  this.AH((byte)16);
                  return;
               }

               if (GameCanvas.AE(15)) {
                  GameCanvas.AF(15);
                  GameCanvas.gameScr.AZ.AD();
               }
            }

         }
      }
   }

   private void AH(byte var1) {
      GlobalService.getInstance().Update_Pk_Point();
      GameCanvas.tabInven.AA((byte)0);
      GameCanvas.tabAllScr.Show((MainScreen)GameCanvas.gameScr);
      this.resetAction();
      GameCanvas.clearAll();
      if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
         if (var1 == 11) {
            GameCanvas.tabAllScr.AE = 0;
         } else if (var1 == 12) {
            GameCanvas.tabAllScr.AE = 4;
         } else if (var1 == 13) {
            GameCanvas.tabAllScr.AE = 2;
         } else if (var1 == 14) {
            GameCanvas.tabAllScr.AE = 3;
         } else if (var1 == 16) {
            GameCanvas.tabAllScr.AE = 1;
         } else if (var1 == 4 && GameCanvas.tabAllScr.AB.size() == 6) {
            GameCanvas.tabAllScr.AE = 5;
         }

         GameCanvas.tabAllScr.AF();
         GameCanvas.tabAllScr.AC.AB();
      }

   }

   public final void AC(boolean var1) {
      if (!GameScreen.isOnAutoPB && !GameScreen.isOnRepeatQuest && !GameScreen.isOnSuperBoss) {
         if (!GameScreen.isOnRepeatQuest && !GameScreen.isOnSuperBoss) {
            if (GameScreen.objFocus != null && (GameScreen.objFocus.typeObject == 1 && (GameScreen.objFocus.Action == 4 || GameScreen.objFocus.isDie) || MainObject.getDistance(GameScreen.objFocus.x, GameScreen.objFocus.y, super.x, super.y) > wFocus + 60)) {
               GameScreen.objFocus = null;
               GameCanvas.gameScr.center = null;
               if (Interface_Game.isPaintInfoFocus) {
                  Interface_Game.isPaintInfoFocus = false;
               }
            }

            if (super.Action != 2 && super.Action != 4) {
               if (GameScreen.objFocus == null) {
                  int var2 = GameScreen.vecPlayers.size();
                  if (this.TC > var2 - 1) {
                     this.TC = var2 - 1;
                  }

                  int var3 = 1000;
                  byte var4 = -1;
                  MainObject var5 = null;

                  for(int var6 = 0; var6 < var2; ++var6) {
                     MainObject var7;
                     int var8;
                     if ((var8 = MainObject.getDistance((var7 = (MainObject)GameScreen.vecPlayers.elementAt((var6 + this.TC) % var2)).x, var7.y, super.x, super.y)) <= wFocus) {
                        byte var9 = var7.typeObject;
                        byte var10 = -1;
                        switch(var9) {
                        case 0:
                           var10 = 0;
                           break;
                        case 1:
                           var10 = 1;
                           break;
                        case 2:
                           var10 = 3;
                           break;
                        case 3:
                        case 4:
                           var10 = 2;
                        }

                        if (var10 >= var4 && !CheckSkipFocus(var7) && (var7.typeObject != 2 || !var1) && (var8 < var3 || var10 > var4)) {
                           var5 = var7;
                           var3 = var8;
                           this.TC = var6;
                           var4 = var10;
                        }
                     }
                  }

                  if (var5 != null) {
                     GameScreen.objFocus = var5;
                     if (!GameCanvas.isTouch) {
                        GameCanvas.gameScr.center = GameScreen.objFocus.getCenterCmd();
                     }
                  }
               }

            }
         }
      } else {
         if (GameScreen.objFocus != null && GameScreen.objFocus.typeObject == 1 && (GameScreen.objFocus.Action == 4 || GameScreen.objFocus.isDie)) {
            GameScreen.objFocus = null;
            GameCanvas.gameScr.center = null;
            if (Interface_Game.isPaintInfoFocus) {
               Interface_Game.isPaintInfoFocus = false;
            }
         }
      }
   }

   private static boolean CheckSkipFocus(MainObject var0) {
      return var0 == GameScreen.player || (var0.Action == 4 || var0.isDie) && var0.typeObject != 0 || var0.isRemove || GameScreen.objFocus != null && var0 == GameScreen.objFocus || var0.typeObject == 10;
   }

   public final void nextFocus() {
      setStart_EndAutoFire(false);
      if (super.Action != 2) {
         int j = -1;
         int i;
         if (GameScreen.objFocus != null) {
            for(i = 0; i < GameScreen.vecPlayers.size(); ++i) {
               if ((MainObject)GameScreen.vecPlayers.elementAt(i) == GameScreen.objFocus) {
                  j = i;
                  break;
               }
            }
         }

         MainObject mainObject3;
         if (super.typePK == 0) {
            if (j >= 0) {
               for(i = j; i < GameScreen.vecPlayers.size(); ++i) {
                  if ((mainObject3 = (MainObject)GameScreen.vecPlayers.elementAt(i)).typeObject == 0 && !CheckSkipFocus(mainObject3) && MainObject.getDistance(mainObject3.x, mainObject3.y, super.x, super.y) < wFocus) {
                     GameScreen.objFocus = mainObject3;
                     if (!GameCanvas.isTouch) {
                        GameCanvas.gameScr.center = GameScreen.objFocus.getCenterCmd();
                     }

                     return;
                  }
               }
            } else {
               j = GameScreen.vecPlayers.size();
            }
         }
         if (j >= 0) {
            for(i = j; i < GameScreen.vecPlayers.size(); ++i) {
               if (!CheckSkipFocus(mainObject3 = (MainObject)GameScreen.vecPlayers.elementAt(i)) && (super.typePK != 0 || mainObject3.typeObject != 0) && MainObject.getDistance(mainObject3.x, mainObject3.y, super.x, super.y) < wFocus) {
                  GameScreen.objFocus = mainObject3;
                  if (!GameCanvas.isTouch) {
                     GameCanvas.gameScr.center = GameScreen.objFocus.getCenterCmd();
                  }

                  return;
               }
            }
         } else {
            j = GameScreen.vecPlayers.size();
         }
         for(i = 0; i < j; ++i) {
            if (!CheckSkipFocus(mainObject3 = (MainObject)GameScreen.vecPlayers.elementAt(i)) && (super.typePK != 0 || mainObject3.typeObject != 0) && MainObject.getDistance(mainObject3.x, mainObject3.y, super.x, super.y) < wFocus) {
               GameScreen.objFocus = mainObject3;
               if (!GameCanvas.isTouch) {
                  GameCanvas.gameScr.center = GameScreen.objFocus.getCenterCmd();
               }

               return;
            }
         }

      }
   }

   void nextMonster() {
      if (!GameScreen.isOnRepeatQuest && !GameScreen.isOnSuperBoss) {
         int num = wFocus * 3 / 2;
         MainObject mainObject = null;

         for(int i = 0; i < GameScreen.vecPlayers.size(); ++i) {
            MainObject mainObject2;
            int distance;
            if ((mainObject2 = (MainObject)GameScreen.vecPlayers.elementAt(i)) != null && mainObject2.Action != 4 && !mainObject2.NQ && mainObject2.typeObject != 10 && mainObject2.typeObject == 1 && (distance = MainObject.getDistance(super.x, super.y, mainObject2.x, mainObject2.y)) < num) {
               num = distance;
               mainObject = mainObject2;
            }
         }
         if (mainObject != null) {
            GameScreen.objFocus = mainObject;
            this.demUnFire = 0;
         } else {
            ++this.demUnFire;
         }
      }
   }

   public final void setActionHotKey(int index) {
      if (index != 999) {
         GameCanvas.clearAll();
         if (isGhost) {
            Point_Focus point_Focus;
            (point_Focus = new Point_Focus()).dis = index;
            vecGhostInput.addElement(point_Focus);
            if (vecGhostInput.size() > 4) {
               vecGhostInput.removeElement(0);
            }
            GlobalService.getInstance().ghost((byte)index);
         } else if (super.Action != 2 && super.Action != 4 && super.Hp > 0) {
            Hotkey hotkey;
            if ((hotkey = hotkeyPlayer[currentTab][index]).itemcur != null) {
               hotkey.itemcur.Use_Item();
            } else if (hotkey.skill != null && hotkey.skill.isBuff && !GameCanvas.loadmap.mapLang() && this.setSkillBuff(hotkey)) {
               this.beginPlayerFire(hotkey);
            } else {
               if (GameScreen.objFocus != null) {
                  GameScreen.objFocus.setFireObject(index);
               }

            }
         }
      } else {
         GameCanvas.clearAll();
         for(int i = 0; i < GameScreen.vecPlayers.size(); ++i) {
            GameScreen.vecPlayers.elementAt(i);
         }
      }
   }

   public final void setActionHotKeyBuff(int index) {
      GameCanvas.clearAll();
      if (super.Action != 2 && super.Action != 4 && super.Hp > 0) {
         Hotkey hotkey;
         if ((hotkey = hotkeyBuffPlayer[index]) != null) {
            if (hotkey.skill != null && !GameCanvas.loadmap.mapLang() && this.setSkillBuff(hotkey)) {
               this.beginPlayerFire(hotkey);
            }

         }
      }
   }

   private boolean setSkillBuff(Hotkey var1) {
      Skill_Info skillFromID;
      if ((skillFromID = Skill_Info.getSkillFromID(var1.skill.ID)).typeSkill == 2) {
         if (skillFromID.typeBuff == 1 || skillFromID.typeBuff == 2) {
            return true;
         }

         if (skillFromID.typeBuff == 3) {
            return this.AC(GameScreen.objFocus);
         }
      }

      return false;
   }

   public final void beginPlayerFirePoint() {
      if (super.Action != 4 && super.Hp > 0) {
         for(int i = 0; i < hotkeyPlayer[0].length; ++i) {
            int num = i;
            if (i == 0) {
               num = 2;
            } else if (i <= 2) {
               num = i - 1;
            }

            Skill_Info skillFromID;
            Hotkey hotkey;
            if ((hotkey = hotkeyPlayer[currentTab][num]).skill != null && DelaySkill.getDelay((skillFromID = Skill_Info.getSkillFromID(hotkey.skill.ID)).indexHotKey).isCoolDown() && super.Mp >= getManaNeedUse((int)skillFromID.manaLost)) {
               this.beginPlayerFire(hotkey);
               return;
            }
         }

      }
   }

   public boolean beginPlayerFire(Skill_Info skill_info) {
      if (skill_info == null) {
         return false;
      } else {
         setStart_EndAutoFire(true);
         if (!DelaySkill.getDelay(skill_info.indexHotKey).isCoolDown()) {
            return false;
         } else {
            if (LoadMap.specMap == 4 && skill_info.typeSkill == 1 || LoadMap.specMap != 4 && skill_info.typeSkill == 4) {
               Interface_Game.addInfoPlayerNormal(T.HT, mFont.tahoma_7_white);
            }
            if (super.Mp < getManaNeedUse((int)skill_info.manaLost)) {
               Interface_Game.addInfoPlayerNormal(T.manaLost, mFont.tahoma_7_white);
               return false;
            } else {
               Player var3 = this;
               int var2 = 0;

               boolean var10000;
               label139:
               while(true) {
                  if (var2 >= var3.vecEffspec.size()) {
                     for(var2 = 0; var2 < var3.PL.size(); ++var2) {
                        DataSkillEff var11;
                        if ((var11 = (DataSkillEff)var3.PL.elementAt(var2)) != null && var11.typeMove == 1) {
                           var10000 = true;
                           break label139;
                        }
                     }

                     var10000 = false;
                     break;
                  }

                  Class_BR var4;
                  if ((var4 = (Class_BR)var3.vecEffspec.elementAt(var2)).typeEffect == 1 || var4.typeEffect == 5) {
                     var10000 = true;
                     break;
                  }

                  ++var2;
               }

               if (var10000) {
                  return false;
               } else {
                  new mVector();
                  Skill_Info var8 = skill_info;
                  var3 = this;
                  mVector var13 = new mVector();
                  Object_Effect_Skill var5;
                  if (skill_info.typeSkill == 2) {
                     if (skill_info.typeBuff == 1 || skill_info.typeBuff == 2 || skill_info.typeBuff == 2) {
                        var5 = new Object_Effect_Skill(super.ID, super.typeObject);
                        var13.addElement(var5);
                     }

                     if (GameScreen.objFocus != null && (skill_info.typeBuff == 2 && GameScreen.objFocus.typeObject == 0 || skill_info.typeBuff == 3)) {
                        var5 = new Object_Effect_Skill(GameScreen.objFocus.ID, GameScreen.objFocus.typeObject);
                        var13.addElement(var5);
                     }
                  } else {
                     var5 = new Object_Effect_Skill(GameScreen.objFocus.ID, GameScreen.objFocus.typeObject);
                     var13.addElement(var5);
                     byte var16 = GameScreen.objFocus.typeObject;

                     for(int var6 = 0; var6 < GameScreen.vecPlayers.size() && var13.size() < var8.nTarget; ++var6) {
                        MainObject var7;
                        if ((var7 = (MainObject)GameScreen.vecPlayers.elementAt(var6)).typeObject == var16 && var7 != GameScreen.objFocus && var3.AC(var7) && MainObject.getDistance(GameScreen.objFocus.x, GameScreen.objFocus.y, var7.x, var7.y) <= 120) {
                           Object_Effect_Skill var18 = new Object_Effect_Skill(var7.ID, var16);
                           var13.addElement(var18);
                        }
                     }
                  }

                  mVector var9 = var13;
                  if (LoadMap.specMap == 3) {
                     for(int var10 = 0; var10 < var9.size(); ++var10) {
                        Object_Effect_Skill var14;
                        MainObject var17;
                        if ((var17 = MainObject.get_Object((int)(var14 = (Object_Effect_Skill)var9.elementAt(var10)).ID, (byte)var14.tem)) != null) {
                           var14.AA(var17.maxHp / 10, var17.Hp - var17.maxHp / 10, 0);
                        }
                     }
                  }

                  MainSkill var19;
                  if (skill_info == null) {
                     var19 = null;
                  } else {
                     MainSkill var15 = new MainSkill(skill_info.ID, skill_info.typeEffSkill);
                     if (skill_info.typeSkill == 2) {
                        var15.setTypeBuff((byte)1, (short)46, (short)0);
                     }

                     var15.AR = skill_info.range;
                     var15.AH = skill_info.timeDelay;
                     var15.AB = skill_info.indexHotKey;
                     var15.AS = skill_info.manaLost;
                     var15.AG = skill_info.typeDevil;
                     var15.lvDevil = skill_info.LvDevilSkill;
                     var19 = var15;
                  }

                  MainSkill var12 = var19;
                  if (var19 != null && var9.size() != 0) {
                     if (skill_info.typeSkill == 1 || skill_info.typeSkill == 4) {
                        this.TE = GameScreen.objFocus;
                     }

                     super.skillCurrent = new Class_HO(this, var9, var12);
                     super.skillCurrent.AA();
                     if (this.AI((short)8)) {
                        super.skillCurrent.AD = true;
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }
      }
   }

   public static int getManaNeedUse(int var0) {
      return var0 + var0 * RX / 1000;
   }

   public final void beginPlayerFire(int var1) {
      Hotkey key = hotkeyPlayer[currentTab][var1];
      this.beginPlayerFire(key);
   }

   private void beginPlayerFire(Hotkey var1) {
      if (var1.skill != null) {
         Skill_Info var2 = Skill_Info.getSkillFromID(var1.skill.ID);
         this.beginPlayerFire(var2);
      }
   }

   private void setCmdGame() {
      if (GameScreen.CN == 0) {
         if (GameCanvas.currentScreen == GameCanvas.gameScr) {
            if (LoadMap.specMap == 3) {
               if (GameCanvas.gameScr.DA != GameCanvas.gameScr.AN || GameCanvas.gameScr.DB != GameCanvas.gameScr.AO) {
                  GameCanvas.gameScr.DA = GameCanvas.gameScr.AN;
                  GameCanvas.gameScr.DB = GameCanvas.gameScr.AO;
                  super.center = null;
                  return;
               }
            } else if (super.Action == 4) {
               if (LoadMap.specMap != 1 && LoadMap.specMap != 2 && LoadMap.specMap != 10 && LoadMap.specMap != 11) {
                  if (!GameCanvas.isTouch) {
                     if (GameCanvas.gameScr.DA != GameCanvas.gameScr.AM) {
                        GameCanvas.gameScr.DA = GameCanvas.gameScr.AM;
                        return;
                     }
                  } else if (GameCanvas.gameScr.center != GameCanvas.gameScr.AM) {
                     GameCanvas.gameScr.center = GameCanvas.gameScr.AM;
                     GameCanvas.gameScr.AM = AvMain.AA(GameCanvas.gameScr.AM, 0);
                     return;
                  }
               }
            } else if (!GameCanvas.isTouch) {
               if (GameCanvas.gameScr.DA != GameCanvas.gameScr.AP) {
                  GameCanvas.gameScr.DA = GameCanvas.gameScr.AP;
               }

               if (GameCanvas.gameScr.DB != GameCanvas.gameScr.AL) {
                  GameCanvas.gameScr.DB = GameCanvas.gameScr.AL;
                  return;
               }
            } else if (GameCanvas.gameScr.center == GameCanvas.gameScr.AM || GameCanvas.gameScr.center == GameCanvas.gameScr.BE) {
               GameCanvas.gameScr.center = null;
               return;
            }
         }
      } else if (GameScreen.CN == 1) {
         if (GameCanvas.gameScr.DA != GameCanvas.gameScr.BG) {
            GameCanvas.gameScr.DA = GameCanvas.gameScr.BG;
         }

         if (GameCanvas.gameScr.DB != GameCanvas.gameScr.BH) {
            GameCanvas.gameScr.DB = GameCanvas.gameScr.BH;
         }

         if (GameCanvas.gameScr.center != null) {
            GameCanvas.gameScr.center = null;
         }
      }

   }

   public final boolean AC(MainObject var1) {
      if (var1 != null && !var1.returnAction() && var1.Action != 4) {
         if (GameCanvas.loadmap.mapLang()) {
            return false;
         } else if (var1.typeObject == 1) {
            return !var1.BZ() || super.typePK != var1.typePK;
         } else if (var1.typeObject == 2) {
            return false;
         } else {
            if (super.typePirate != -1) {
               if (var1.typePirate == super.typePirate && var1.IDMainShiper == super.ID) {
                  return false;
               }

               if ((super.typePirate == 0 || super.typePirate == 1) && var1.typePirate == 2) {
                  return true;
               }

               if (super.typePirate == 2 && var1.typePirate != -1) {
                  return true;
               }
            }

            if (var1.Lv < 0) {
               return false;
            } else if (super.typePK == 0) {
               return true;
            } else if (var1.typePK == 0) {
               return true;
            } else if (var1.typePK == 1) {
               return true;
            } else if (super.typePK == 2 && var1.typePK >= 0) {
               return true;
            } else if (super.typePK == 3 && !(super.clan == null ? false : (var1.clan == null ? false : super.clan.ID == var1.clan.ID)) && var1.typePK >= 0) {
               return true;
            } else if (super.typePK >= 4 && var1.typePK >= 0 && var1.typePK != super.typePK && var1.typePlayer != 2 && var1.typePlayer != 3) {
               return true;
            } else if (super.typePK != 1) {
               return false;
            } else {
               for(int var2 = 0; var2 < AY.length; ++var2) {
                  if (var1.ID == AY[var2]) {
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   public final void AB(MainSkill var1, mVector var2) {
      super.tickAfterSkill = 70;
      if (GameScreen.CV == 2) {
         MainHelp.AA(false);
      }

      Object_Effect_Skill var4;
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.size() && (var4 = (Object_Effect_Skill)var2.elementAt(var3)).tem == 1; ++var3) {
            if (var4.hpLast <= 0) {
               try {
                  MainMonster var13;
                  if ((var13 = (MainMonster)MainObject.get_Object((int)var4.ID, (byte)1)) != null) {
                     MainQuest var5 = null;
                     boolean var6 = false;

                     int var7;
                     for(var7 = 0; var7 < QI.size() && !var6; ++var7) {
                        MainQuest var8;
                        if ((var8 = (MainQuest)QI.elementAt(var7)).AB == 1) {
                           for(int var9 = 0; var9 < var8.AM.size(); ++var9) {
                              Class_CP var10;
                              if ((var10 = (Class_CP)var8.AM.elementAt(var9)).AA == 1 && var10.AB == var13.idCatMonster && var10.AD < var10.AC) {
                                 var5 = var8;
                                 if (var8.AA == 1) {
                                    var6 = true;
                                 }
                              }
                           }
                        }
                     }

                     if (var5 != null) {
                        for(var7 = 0; var7 < var5.AM.size(); ++var7) {
                           Class_CP var16;
                           if ((var16 = (Class_CP)var5.AM.elementAt(var7)).AA == 1 && var16.AB == var13.idCatMonster && var16.AD < var16.AC) {
                              ++var16.AD;
                              if (GameScreen.isOnRepeatQuest) {
                                 AutoRepeatQuest.AD = true;
                                 AutoRepeatQuest.AE = null;
                                 AutoRepeatQuest.AC = false;
                              }
                           }
                        }
                     }
                  }
               } catch (Exception var11) {
               }
            }
         }
      }

      boolean var12 = true;

      for(int var14 = 0; var14 < GameScreen.VecEffect.size(); ++var14) {
         MainEffect var15;
         if ((var15 = (MainEffect)GameScreen.VecEffect.elementAt(var14)).AB == 0 && !var15.BK && !var15.isStop && var15.typeEffect == var1.AA && var15.AM == -1 && var15.objFireMain != null && var15.objFireMain == GameScreen.player) {
            var15.replaceHP(var2);
            var15.isEff = false;
            var15.BK = true;
            var12 = false;
            break;
         }
      }

      Effect_Skill.setHP_New(var2, this, var12);
   }

   public static void AA(int var0, MainSkill var1, MainItem var2) {
      if (var1 != null) {
         hotkeyPlayer[currentTab][var0].setSkill(var1, var1.idIcon);
      } else if (var2 != null) {
         hotkeyPlayer[currentTab][var0].setPotion(var2);
      }

      if (LoadMap.specMap != 4) {
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AD();
      }

   }

   public static void setHotKeyBuff() {
      if (GameCanvas.isTouch) {
         if (vecListSkill != null) {
            if (hotkeyBuffPlayer == null) {
               hotkeyBuffPlayer = new Hotkey[6];
            }

            int var0;
            for(var0 = 0; var0 < hotkeyBuffPlayer.length; ++var0) {
               hotkeyBuffPlayer[var0] = null;
            }

            var0 = 0;

            for(int var1 = 0; var1 < vecListSkill.size(); ++var1) {
               Skill_Info var2;
               if ((var2 = (Skill_Info)vecListSkill.elementAt(var1)).Lv_RQ >= 0 && var2.typeSkill == 2) {
                  MainSkill var3 = new MainSkill(var2.ID, var2.typeEffSkill);
                  hotkeyBuffPlayer[var0] = new Hotkey();
                  var3.AB = var2.indexHotKey;
                  hotkeyBuffPlayer[var0].setSkill(var3, var2.idIcon);
                  ++var0;
               }

               if (var0 == 6) {
                  return;
               }
            }

         }
      }
   }

   public static void AA(int var0, int var1, boolean var2, byte var3) {
      DelaySkill var4 = DelaySkill.getDelay(var0);
      if (!var2) {
         if (GameScreen.player.AI((short)8)) {
            var1 += var1 / 2;
         }

         short var10000 = GiamCountDownCur;
         var1 -= var1 * GiamCountDownCur / 1000;
      }

      if (var1 < 1000) {
         var1 = 1000;
      }

      var4.AB = GameCanvas.timeNow;
      var4.AC = var1 + 200;
      var4.AD = var1 + 200;
      var4.AA = var3;
   }

   public final void setUseMana(int var1) {
      super.Mp -= getManaNeedUse(var1);
   }

   public final void setAutoFire(boolean isAutoNew) {
      if (GameCanvas.gameTick % 5 == 0 && GameCanvas.timeNow - this.timeFristSkill > 1000L && super.skillCurrent == null) {
         if (isBack) {
            if (MainObject.getDistance(super.x, super.y, xBeginAuto, yBeginAuto) <= LoadMap.wTile << 1) {
               isBack = false;
            }
            if (super.posTransRoad == null && super.Action == 0) {
               GameScreen.player.toX = GameScreen.player.x;
               GameScreen.player.toY = GameScreen.player.y;
               super.posTransRoad = GameCanvas.loadmap.AA(xBeginAuto / LoadMap.wTile, yBeginAuto / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 80, this);
               if (super.posTransRoad != null && super.posTransRoad.length > 80) {
                  super.posTransRoad = null;
               }
            }
         } else if (this.TE == null || GameScreen.objFocus == null || this.TE != GameScreen.objFocus) {
            label206: {
               if ((TG = MainObject.getDistance(super.x, super.y, xBeginAuto, yBeginAuto)) <= ST + 200) {
                  if (TG > ST + 100 && (GameScreen.objFocus == null || GameScreen.objFocus.Hp > GameScreen.objFocus.maxHp / 10 || GameScreen.objFocus.MO == 0)) {
                     isBack = true;
                     break label206;
                  }

                  if (TG <= ST || GameScreen.objFocus != null && GameScreen.objFocus.Hp <= GameScreen.objFocus.maxHp - GameScreen.objFocus.maxHp / 20 && GameScreen.objFocus.MO != 0) {
                     break label206;
                  }
               }

               isBack = true;
            }
         }

         if (isBack) {
            ++this.TF;
            if (this.TF > 200) {
               isBack = false;
            }

            return;
         }

         this.TF = 0;
         if (GameScreen.isOnRepeatQuest && GameScreen.objFocus == null || GameScreen.isOnSuperBoss && GameScreen.objFocus == null) {
            AutoFireCur = 0;
            return;
         }

         MainObject var2 = GameScreen.objFocus;
         if (GameScreen.isOnAutoPB) {
            if (var2 == null || var2.isRemove || var2.isDie || var2.Hp <= 0) {
               AutoBattlefield.isChangeFocus = true;
               if (GameScreen.objFocus == null) {
                  return;
               }
            }
         } else if (var2 != null && var2.typeObject == 0 && (this.TE == null || this.TE.typeObject == 0)) {
            if (!this.AC(var2)) {
               AutoFireCur = 0;
               return;
            }
         } else {
            if (var2 == null || var2.returnAction() || var2.typeObject != 1 || var2.isDie || var2.Hp <= 0) {
               this.nextMonster();
               if (GameScreen.objFocus != null) {
                  if (GameScreen.objFocus.typeObject != 1) {
                     return;
                  }

                  GameScreen.addEffectEnd_ObjTo((short)24, 0, GameScreen.objFocus.x, GameScreen.objFocus.y, (short)GameScreen.objFocus.ID, (byte)GameScreen.objFocus.typeObject, (byte)0, (MainObject)null);
                  Interface_Game.isPaintInfoFocus = true;
               }
            }

            if ((var2 = GameScreen.objFocus) == null || var2.typeObject != 1) {
               return;
            }
         }

         int var6 = hotkeyPlayer[0].length;

         for(int var3 = 0; var3 < var6; ++var3) {
            int var4 = (var3 + IndexFire) % var6;
            if (isAutoNew) {
               var4 = IndexFire % var6;
               IndexFire = 2;
            }

            Hotkey var5;
            Skill_Info var7;
            if ((var5 = hotkeyPlayer[currentTab][var4]).skill != null && DelaySkill.getDelay(var5.skill.AB).isCoolDown() && (var7 = Skill_Info.getSkillFromID(var5.skill.ID)).typeSkill != 2 && var7 != null && getManaNeedUse((int)var7.manaLost) <= super.Mp) {
               GameScreen.objFocus.setFireObject(var4);
               this.timeFristSkill = GameCanvas.timeNow;
               if (!isAutoNew) {
                  IndexFire = var4 + 1;
               }

               return;
            }

            if (isAutoNew) {
               break;
            }
         }
      }

   }

   public static void setStart_EndAutoFire(boolean isAu) {
      if (isAu) {
         if (AutoFireCur != typeAutoFireMain) {
            AutoFireCur = typeAutoFireMain;
            xBeginAuto = GameScreen.player.x;
            yBeginAuto = GameScreen.player.y;
            return;
         }
      } else if (AutoFireCur > 0) {
         AutoFireCur = 0;
      }

   }

   private boolean AI(short var1) {
      for(int var2 = 0; var2 < super.vecEffspec.size(); ++var2) {
         if (((Class_BR)super.vecEffspec.elementAt(var2)).typeEffect == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean CM() {
      int var1;
      for(var1 = 0; var1 < super.vecEffspec.size(); ++var1) {
         Class_BR var2;
         if ((var2 = (Class_BR)super.vecEffspec.elementAt(var1)).typeEffect == 1 || var2.typeEffect == 8) {
            return true;
         }
      }

      for(var1 = 0; var1 < super.PL.size(); ++var1) {
         DataSkillEff var3;
         if ((var3 = (DataSkillEff)super.PL.elementAt(var1)) != null && var3.typeMove == 1) {
            return true;
         }
      }

      return false;
   }

   public final void AK() {
      super.toX = super.x;
      super.toY = super.y;
      int var1;
      int var2;
      if (super.typeActionBoat != 1) {
         if (super.typeActionBoat == 3) {
            var1 = super.IR - 24;
            var2 = super.IS - 48;
            super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 20, this);
            if (super.posTransRoad == null || super.posTransRoad.length > 20) {
               var1 = super.IR + 24;
               var2 = super.IS - 48;
               super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 20, this);
            }

            if (super.posTransRoad == null || super.posTransRoad.length > 20) {
               super.posTransRoad = null;
               super.x = super.IR - 24;
               super.y = super.IS - 48;
            }
         }
      } else {
         var1 = super.IR - 24;
         var2 = super.IS + 24;
         super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 20, this);
         if (super.posTransRoad == null || super.posTransRoad.length > 20) {
            var1 = super.IR + 24;
            var2 = super.IS + 24;
            super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 20, this);
         }

         if (super.posTransRoad == null || super.posTransRoad.length > 20) {
            super.posTransRoad = null;
            super.x = super.IR - 24;
            super.y = super.IS + 24;
            return;
         }
      }

   }

   public final void AJ() {
      super.toX = super.x;
      super.toY = super.y;
      int var1 = super.IR;
      int var2 = super.IS;
      super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 20, this);
      GameScreen.player.AC = 1;
      if (super.posTransRoad == null || super.posTransRoad.length > 20) {
         super.posTransRoad = null;
         super.x = super.IR;
         super.y = super.IS;
      }

   }

   public final void BX() {
      if (ReadMessenge.isNondata) {
         GameCanvas.readMessage.readChangeMapNonData(GameCanvas.readMessage.msgLuu, GameCanvas.readMessage.idMapLuu);
      } else {
         GameCanvas.readMessage.readChangeMapNew(GameCanvas.readMessage.msgLuu, GameCanvas.readMessage.idMapLuu);
      }

      super.PM = 0;
   }

   public final void AB(int var1, int var2) {
      super.toX = super.x;
      super.toY = super.y;
      super.posTransRoad = GameCanvas.loadmap.AA(var1 / LoadMap.wTile, var2 / LoadMap.wTile, super.x / LoadMap.wTile, super.y / LoadMap.wTile, 30, this);
      GameScreen.player.AC = 1;
   }

   public static void AD(byte var0) {
      for(int var2 = 0; var2 < vecInventory.size(); ++var2) {
         MainItem var1;
         if ((var1 = (MainItem)vecInventory.elementAt(var2)).typeObject == 7) {
            GlobalService.getInstance().Chest((byte)1, var1.ID, (byte)var1.typeObject, (int)var1.numPotion);
         }
      }

   }

   public static void SetMaterialToInven(byte var0) {
      for(int var2 = 0; var2 < vecChest.size(); ++var2) {
         MainItem var1;
         if ((var1 = (MainItem)vecChest.elementAt(var2)).typeObject == 7) {
            GlobalService.getInstance().Chest((byte)2, var1.ID, (byte)var1.typeObject, (int)var1.numPotion);
         }
      }

   }

   public static void CH() {
      for(int var0 = 0; var0 < vecInventory.size(); ++var0) {
         MainItem var1;
         if ((var1 = (MainItem)vecInventory.elementAt(var0)).typeObject == 4 && (var1.ID >= 44 && var1.ID <= 79 || var1.ID >= 362 && var1.ID <= 367)) {
            GlobalService.getInstance().Chest((byte)1, var1.ID, (byte)var1.typeObject, (int)var1.numPotion);
         }
      }

   }

   public static void SetDiamondToInven() {
      for(int var0 = 0; var0 < vecChest.size(); ++var0) {
         MainItem var1;
         if ((var1 = (MainItem)vecChest.elementAt(var0)).typeObject == 4 && (var1.ID >= 44 && var1.ID <= 79 || var1.ID >= 362 && var1.ID <= 367)) {
            GlobalService.getInstance().Chest((byte)2, var1.ID, (byte)var1.typeObject, (int)var1.numPotion);
         }
      }

   }

   public final void resetAction() {
      this.mindexkey = new int[]{-1, -1, -1, -1};
      super.resetAction();
   }

   public final void actionStand() {
      if (super.tickAfterSkill > 0) {
         --super.tickAfterSkill;
      }

      if (super.f > super.feStand.length - 1) {
         super.f = 0;
      }

      if (isGhost) {
         super.f = 0;
      }

      super.frame = super.feStand[super.f];
   }

   public static void SetGiamCountDown() {
      if (vecParty.size() == 0) {
         giamCountDownParty = 0;
      }

      GiamCountDownCur = (short)(0 + giamCountDownAtt);
   }

   public final void updateTimeSafe() {
      if (super.timeSafe > 0 && (int)(GameCanvas.timeNow - super.timeBeginSafe) > 1000) {
         --super.timeSafe;
         super.timeBeginSafe += 1000L;
      }

   }

   public final void addEffBuff(byte var1, short var2, short var3) {
      if (super.skillCurrent != null) {
         super.skillCurrent.beginSkill();
      }

      mVector var4 = new mVector();
      Object_Effect_Skill var5;
      (var5 = new Object_Effect_Skill(super.ID, super.typeObject)).AA(0, super.Hp, 0);
      var4.addElement(var5);
      MainSkill var6;
      (var6 = new MainSkill((short)-1, var2)).setTypeBuff(var1, var2, var3);
      this.setDataBeginSkill(var6, var4);
   }

   public final void AA(MainBuff var1) {
      for(int var2 = 0; var2 < super.vecEffBuff.size(); ++var2) {
         if (((MainBuff)super.vecEffBuff.elementAt(var2)).IdBuff == var1.IdBuff) {
            super.vecEffBuff.removeElement(var2);
            AJ(var1.IdBuff);
            --var2;
         }
      }

      var1.setYlech((MainObject)this);
      super.vecEffBuff.addElement(var1);
      Class_EB var4 = new Class_EB(var1.AC, var1.IdBuff, 1000);
      Interface_Game var10000 = GameScreen.interfaceGame;
      Interface_Game.AA((MainItem)var4);
      AA(var4.indexHotKey, var1.timeBuff, true, (byte)0);
   }

   public final void AB(MainBuff var1) {
      for(int var2 = 0; var2 < super.vecEffBuffNew.size(); ++var2) {
         if (((MainBuff)super.vecEffBuffNew.elementAt(var2)).IdBuff == var1.IdBuff) {
            super.vecEffBuffNew.removeElement(var2);
            AJ(var1.IdBuff);
            --var2;
         }
      }

      var1.setYlech((MainObject)this);
      super.vecEffBuffNew.addElement(var1);
   }

   private static void AJ(short var0) {
      for(int var1 = 0; var1 < Interface_Game.vecEffCurrent.size(); ++var1) {
         MainItem var2;
         if ((var2 = (MainItem)Interface_Game.vecEffCurrent.elementAt(var1)).typeObject == 9 && var2.ID == var0) {
            var2.CE = true;
         }
      }

   }
}
