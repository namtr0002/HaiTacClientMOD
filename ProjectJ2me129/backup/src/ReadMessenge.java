import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;

public final class ReadMessenge extends AvMain {
   private short[][] AM = new short[][]{{33, 35, 37}, {15, 29, 122}, {44, 48, 50}, {51, 53, 55}, {58, 66, 68}};
   private short[][] AN = new short[][]{{83, 84, 85}, {86, 87, 123}, {124, 125, 12}, {52, 63, 56}, {126, 127, 69}};
   private short[][] AO = new short[][]{{180, 181, 182}, {183, 184, 185}, {186, 187, 188}, {189, 190, 191}, {192, 193, 194}};
   private short[][] AP = new short[][]{{212, 213, 214}, {215, 216, 217}, {218, 219, 220}, {221, 222, 223}, {224, 225, 226}};
   public Message msgLuu = null;
   public short idMapLuu = 0;
   public static byte actionChangeMap = 0;
   public static boolean isNondata = false;
   private static short indexHotKeySkill = 0;
   private static short AR = -1;
   private int AS = 0;
   public static short idItemUpgrade = 0;
   public static int AF = 0;
   private static byte[] AT;
   public static short AG = -1;
   public static mImage[] AH;
   private static short AU = -1;
   private static short AV = -1;
   public static byte AI = 0;
   public static int AJ = -1;
   public static byte AK = -1;
   public static String AL = "";

   public final void commandPointer(int var1, int var2) {
      int var3;
      ReadMessenge var5;
      iCommand var7;
      mVector var8;
      mVector var10;
      iCommand var12;
      switch(var1) {
      case 0:
         GlobalService.getInstance().Choice_Dialog_server(AR, (byte)var2);
         GameCanvas.end_Dialog();
         return;
      case 1:
         if (var2 != 0) {
            if (ScreenUpgrade.isClickUpgradeItemToSucess) {
               ScreenUpgrade.beriOrRuby = (byte)var2;
            }

            GlobalService.getInstance().Upgrade_Item((byte)2, (short)idItemUpgrade, (byte)((byte)var2));
         }

         GameCanvas.end_Dialog();
         return;
      case 3:
         if (++MainObject.BH >= MainObject.BG.length - 1) {
            GameCanvas.menu.AA((mVector)null, MainObject.BG[MainObject.BG.length - 1], var2, (byte)2, true, 2, false);
            return;
         }

         var8 = new mVector();
         var12 = new iCommand(T.AK, 3, var2, this);
         var8.addElement(var12);
         GameCanvas.menu.AA(var8, MainObject.BG[MainObject.BH], var2, (byte)2, true, 2, false);
         return;
      case 4:
         GlobalService.getInstance().AA((byte)3, (short)AU, (short)AV);
         GameCanvas.end_Dialog();
         return;
      case 5:
         switch(var2) {
         case 0:
            GameScreen.IP = !GameScreen.IP;
            return;
         case 1:
            GameScreen.IQ = !GameScreen.IQ;
            return;
         case 2:
            GameScreen.IS = !GameScreen.IS;
            return;
         case 3:
            GameScreen.IR = !GameScreen.IR;
            return;
         case 4:
            GameScreen.IU = !GameScreen.IU;
            return;
         case 5:
            GameScreen.IW = !GameScreen.IW;
            return;
         case 6:
            GameScreen.IV = !GameScreen.IV;
            return;
         case 7:
            GameScreen.IX = !GameScreen.IX;
         default:
            return;
         }
      case 6:
         byte var11 = -1;
         if (GameCanvas.currentScreen == GameCanvas.CJ) {
            var11 = (byte)GameCanvas.CJ.idSelect;
         }

         GlobalService.getInstance().AC((byte)1, var11, (short)var2, (byte)0, (short)1);
         return;
      case 7:
         var10 = new mVector();

         for(var1 = 0; var1 < 5; ++var1) {
            var7 = new iCommand(T.WA[var1 + 1], 8, var1, this);
            var10.addElement(var7);
         }

         GameCanvas.menu.startAt(var10, 2, "Select Class");
         return;
      case 8:
         var2 = var2;
         var5 = this;

         for(var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
            ((Skill_Info)Player.vecListSkill.elementAt(var3)).typeEffSkill = var5.AO[var2][var3];
            if (var3 == 2) {
               break;
            }
         }

         return;
      case 9:
         var8 = new mVector();

         for(var2 = 0; var2 < 5; ++var2) {
            var12 = new iCommand(T.WA[var2 + 1], 10, var2, this);
            var8.addElement(var12);
         }

         GameCanvas.menu.startAt(var8, 2, "Select Class");
         return;
      case 10:
         var2 = var2;
         var5 = this;

         for(var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
            ((Skill_Info)Player.vecListSkill.elementAt(var3)).typeEffSkill = var5.AP[var2][var3];
            if (var3 == 2) {
               break;
            }
         }

         return;
      case 11:
         GlobalService.getInstance().AD((byte)5, (short)var2, (byte)0);
         return;
      case 12:
         if (var2 != 0) {
            if (ScreenUpgrade.isClickUpgradeSuperItemToSucess) {
               ScreenUpgrade.beriOrRuby = (byte)var2;
            }

            GlobalService.getInstance().AA((byte)2, idItemUpgrade, (byte)((byte)var2), (byte)0);
         }

         GameCanvas.end_Dialog();
         return;
      case 13:
         GlobalService.getInstance().AA((byte)2, idItemUpgrade, (byte)0, (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 14:
         mVector var9 = new mVector();

         for(var3 = 0; var3 < 5; ++var3) {
            iCommand var6 = new iCommand(T.WA[var3 + 1], 16, var3, this);
            var9.addElement(var6);
         }

         GameCanvas.menu.startAt(var9, 2, "Select Class");
         return;
      case 15:
         var10 = new mVector();

         for(var1 = 0; var1 < 5; ++var1) {
            var7 = new iCommand(T.WA[var1 + 1], 17, var1, this);
            var10.addElement(var7);
         }

         GameCanvas.menu.startAt(var10, 2, "Select Class");
         return;
      case 16:
         var2 = var2;
         var5 = this;

         for(var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
            ((Skill_Info)Player.vecListSkill.elementAt(var3)).typeEffSkill = var5.AM[var2][var3];
            if (var3 == 2) {
               break;
            }
         }

         return;
      case 17:
         var2 = var2;
         var5 = this;

         for(var3 = 0; var3 < Player.vecListSkill.size(); ++var3) {
            ((Skill_Info)Player.vecListSkill.elementAt(var3)).typeEffSkill = var5.AN[var2][var3];
            if (var3 == 2) {
               break;
            }
         }

         return;
      case 18:
         GlobalService.getInstance().AB((byte)2, idItemUpgrade, (byte)0, (byte)0);
         GameCanvas.end_Dialog();
      case 2:
      default:
      }
   }

   public static void ListChar(Message var0) {
      try {
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AA(GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
         ListChar_Screen.AA.removeAllElements();
         byte var1 = var0.reader().readByte();

         for(int var2 = 0; var2 < var1; ++var2) {
            short var3 = var0.reader().readShort();
            String var4 = var0.reader().readUTF();
            Class_ET var9;
            (var9 = new Class_ET(var3, (byte)0, var4, 0, 0)).clazz = var0.reader().readByte();
            var9.Lv = var0.reader().readShort();
            var9.sethead(var0.reader().readShort());
            var9.sethair(var0.reader().readShort());
            short var10;
            if ((var10 = var0.reader().readShort()) >= 0) {
               var9.clan = new MainClan();
               var9.clan.idIcon = var10;
               var9.clan.AJ = 10;
            }

            byte var11;
            short[] var5 = new short[var11 = var0.reader().readByte()];

            int var6;
            for(var6 = 0; var6 < var5.length; ++var6) {
               var5[var6] = -1;
            }

            for(var6 = 0; var6 < var11; ++var6) {
               if (var0.reader().readByte() == 1) {
                  var5[var6] = var0.reader().readShort();
               }
            }

            var9.AA(var5);
            var9.MJ = var0.reader().readByte();
            if (var9.MJ != 0) {
               var9.timeDie = (long)var0.reader().readInt();
            }

            ListChar_Screen.AA.addElement(var9);
         }

         if (var1 == 0) {
            CreateChar_Screen.AB().Show((MainScreen)GameCanvas.loginScr);
         } else {
            ListChar_Screen.AF().Show();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   public final void ChangeMap(Message m) {
      try {
         actionChangeMap = 0;
         GameCanvas.loadmap.idLastMap = GameCanvas.loadmap.idMapLoadMap;
         short num;
         if (setMapSea(num = m.reader().readShort())) {
            this.idMapLuu = num;
            this.msgLuu = m;
            isNondata = false;
         } else {
            this.readChangeMapNew(m, num);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private static boolean setMapSea(int id) {
      if (LoadMap.mSea != null) {
         for(int i = 0; i < LoadMap.mSea.length; ++i) {
            if (LoadMap.mSea[i][1] == id && LoadMap.mSea[i][0] == GameCanvas.loadmap.idLastMap) {
               GameScreen.player.addEffBuff((byte)(LoadMap.mSea[i][2] - 1), LoadMap.mSea[i][3], LoadMap.mSea[i][4]);
               return true;
            }
         }
      }

      return false;
   }

   public final void readChangeMapNew(Message m, short idmap) {
      try {
         GameCanvas.loadMapScr.Show();
         GameScreen.RemoveLoadMap();
         DataMap dataMap = new DataMap();
         GameCanvas.loadmap.idMapLoadMap = idmap;
         Interface_Game.nameMap = "";
         GameCanvas.loadMapScr.area = m.reader().readByte();
         byte typeViewPlayer = m.reader().readByte();
         GameScreen.player.posTransRoad = null;
         GameScreen.player.x = m.reader().readShort();
         GameScreen.player.y = m.reader().readShort();
         GameScreen.player.toX = GameScreen.player.x;
         GameScreen.player.toY = GameScreen.player.y;
         GameScreen.player.maxHp = m.reader().readInt();
         GameScreen.player.Hp = m.reader().readInt();
         GameScreen.player.maxMp = m.reader().readInt();
         GameScreen.player.Mp = m.reader().readInt();
         byte b = m.reader().readByte();
         if (LoadMap.specMap == 3) {
            GameScreen.player.isBeginTrain = false;
         } else if (LoadMap.specMap == 4) {
            SaveRms var10000 = GameCanvas.saveRms;
            SaveRms.loadHotKey(SaveRms.datahotKeySkill);
         }

         if ((LoadMap.specMap = m.reader().readByte()) == 1 && check_IDMap_PVP()) {
            GameScreen.tickPvP = 60;
         }

         if (b == 1) {
            LoadMap.isOnlineMap = true;
            int num = m.reader().readInt();
            byte[] array = null;
            if (num > 0) {
               array = new byte[num];
               m.reader().read(array);
            }

            dataMap.setDataMap(array);
            GameCanvas.loadmap.loadmap(array);
            num = m.reader().readInt();
            array = null;
            if (num > 0) {
               array = new byte[num];
               m.reader().read(array);
            }
            dataMap.setDataItemMap(array);
            GameCanvas.loadMapScr.mItemMap = array;
            LoadMap.vecPointChange.removeAllElements();
            byte var8 = m.reader().readByte();
            dataMap.vecPointMap.removeAllElements();
            for(num = 0; num < var8; ++num) {
               Point point;
               label183: {
                  (point = new Point()).name = m.reader().readUTF();
                  point.x = m.reader().readShort();
                  point.y = m.reader().readShort();
                  dataMap.vecPointMap.addElement(point);
                  if (GameCanvas.loadmap.idMapLoadMap != 158 && GameCanvas.loadmap.idMapLoadMap != 160 && GameCanvas.loadmap.idMapLoadMap != 162 && GameCanvas.loadmap.idMapLoadMap != 164 && GameCanvas.loadmap.idMapLoadMap != 166 || point.y >= GameCanvas.loadmap.mapH * LoadMap.wTile - 115) {
                     if (GameCanvas.loadmap.idMapLoadMap == 157 || GameCanvas.loadmap.idMapLoadMap == 159 || GameCanvas.loadmap.idMapLoadMap == 161 || GameCanvas.loadmap.idMapLoadMap == 163 || GameCanvas.loadmap.idMapLoadMap == 165 || (GameCanvas.loadmap.idMapLoadMap >= 167 && GameCanvas.loadmap.idMapLoadMap <= 176 || GameCanvas.loadmap.idMapLoadMap == 190) && point.y <= 72) {
                        continue;
                     }

                     if (point.x < 115) {
                        point.dis = 0;
                        point.x2 = point.x - 8;
                        point.y2 = point.y - 18;
                        point.f = 0;
                        point.vx = -1;
                        break label183;
                     }

                     if (point.x > GameCanvas.loadmap.mapW * LoadMap.wTile - 115) {
                        point.dis = 1;
                        point.f = 1;
                        point.x2 = point.x + 8;
                        point.y2 = point.y - 18;
                        point.vx = 1;
                        break label183;
                     }

                     if (point.y > GameCanvas.loadmap.mapH * LoadMap.wTile - 115 || GameCanvas.loadmap.idMapLoadMap == 191) {
                        point.dis = 3;
                        point.f = 2;
                        point.x2 = point.x;
                        point.y2 = point.y - 20;
                        point.vy = 1;
                        break label183;
                     }
                  }

                  point.y -= 10;
                  point.dis = 2;
                  point.f = 2;
                  point.x2 = point.x;
                  point.y2 = point.y + 10;
                  point.vy = -1;
               }

               LoadMap.vecPointChange.addElement(point);
            }
         }

         if (b == 0) {
            LoadMap.isOnlineMap = false;
            if (LoadMap.specMap == 5) {
               LoadRedLine(false);
            } else if (LoadMap.specMap == 8) {
               MapGotoSky.setPos();
            } else if (LoadMap.specMap == 12) {
               MapGotoGod.setPos();
            }
         }

         LoadMapScreen.IDBack = m.reader().readByte();
         LoadMapScreen.HBack = m.reader().readShort();
         dataMap.IDBack = LoadMapScreen.IDBack;
         dataMap.HBack = LoadMapScreen.HBack;
         LoadMapScreen.isNextMap = true;
         if (b == 1) {
            GameCanvas.gameScr.setTypeViewPlayer(typeViewPlayer);
         }

         byte b3 = m.reader().readByte();
         byte level = m.reader().readByte();
         LoadMapScreen.typeChangeMap = m.reader().readByte();
         if (b3 >= 0 && !GameCanvas.lowGraphic) {
            GameScreen.effMap = new Effect_Map(b3, level);
         } else {
            GameScreen.effMap = null;
         }

         if (LoadMap.specMap == 3) {
            GlobalService.getInstance().Get_Xp_Map_Train((byte)0);
            Player.AutoFireCur = Player.typeAutoFireMain;
            MainObject.mPosMapTrain = mSystem.new_M_Int(b3 = m.reader().readByte(), 2);
            int var14 = 0;

            while(true) {
               if (var14 >= b3) {
                  Player.strTimeChange = m.reader().readUTF();
                  break;
               }

               for(int i1 = 0; i1 < 2; ++i1) {
                  MainObject.mPosMapTrain[var14][i1] = m.reader().readByte();
               }
               ++var14;
            }
         }

         Interface_Game.nameMap = m.reader().readUTF();
         dataMap.nameMap = Interface_Game.nameMap;
         DataMap.hashDataMap.put("" + idmap, dataMap);
         if (LoadMap.specMap == 4) {
            GameScreen.player.boatSea = new Boat(GameScreen.player.ID, GameScreen.player.x, GameScreen.player.y, 0, (byte)GameScreen.player.type_left_right);
            GameScreen.player.setSpeed(4, 3);
            GameScreen.player.vySea = 4;
            if (!GameCanvas.lowGraphic) {
               GameScreen.effSea = new Effect_Map((byte)4, (byte)0);
               return;
            }
         } else if (LoadMap.isOnlineMap && LoadMap.specMap != 5 && LoadMap.specMap != 8) {
            GameScreen.player.boatSea = null;
            GameScreen.player.setSpeed(7, 7);
            GameScreen.effSea = null;
            return;
         }
      } catch (Exception var9) {
         GlobalService.getInstance().changeMapOk();
         var9.printStackTrace();
      }

   }

   private static boolean check_IDMap_PVP() {
      return GameCanvas.loadmap.idMapLoadMap == 58 || GameCanvas.loadmap.idMapLoadMap == 59 || GameCanvas.loadmap.idMapLoadMap == 109 || GameCanvas.loadmap.idMapLoadMap == 119 || GameCanvas.loadmap.idMapLoadMap == 120 || GameCanvas.loadmap.idMapLoadMap == 121 || GameCanvas.loadmap.idMapLoadMap == 123;
   }

   public static void LoadRedLine(boolean isFinish) {
      MapOff_RedLine.setTypeMoveredLine((byte)0);
      GameCanvas.loadMapScr.mItemMap = null;
      MainScreen.cameraMain.xCam = 0;
      MainScreen.cameraMain.yCam = 0;
      MapOff_RedLine.xHardCodeMapRedLine = MotherCanvas.w / 3;
      if (isFinish) {
         MapOff_RedLine.isFinish = true;
         MapOff_RedLine.AL = MotherCanvas.h / 5 * 3;
         MapOff_RedLine.AN = 8;
         GameScreen.player.x = 0;
         GameScreen.player.y = MapOff_RedLine.AL + 20;
      } else {
         MapOff_RedLine.isFinish = false;
         MapOff_RedLine.AL = MotherCanvas.h / 5 << 2;
         MapOff_RedLine.AN = 6;
         GameScreen.player.x = MapOff_RedLine.xHardCodeMapRedLine;
         GameScreen.player.y = MapOff_RedLine.AL;
      }

      GameScreen.player.Dir = 2;
      GameScreen.player.type_left_right = 2;
      GameScreen.player.boatSea = new Boat(GameScreen.player.ID, GameScreen.player.x, GameScreen.player.y, 0, (byte)GameScreen.player.type_left_right);
      GameScreen.player.boatSea.AA(GameScreen.player.BO, GameScreen.player.typePirate);
      Player.SF = false;
      Player.SG = 0;
      GameScreen.player.SC = 2;
      GameScreen.player.SB = 2;
      MapOff_RedLine.AG = 0;
      MapOff_RedLine.AH = MapOff_RedLine.AL - 40;
      MapOff_RedLine.AB.removeAllElements();
      MapOff_RedLine.AC.removeAllElements();
      MapOff_RedLine.AD.removeAllElements();
      MapOff_RedLine.AE.removeAllElements();
      MapOff_RedLine.AF.removeAllElements();
      int var1 = MotherCanvas.w / 24 + 4;

      int var2;
      for(var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = var2 * 24;
         if (isFinish) {
            var3.y = var2 * 5;
         } else {
            var3.y = -(var2 * 5);
         }

         var3.frame = CRes.random(7);
         MapOff_RedLine.AB.addElement(var3);
      }

      for(var2 = 0; var2 < MotherCanvas.w / 24 + 2; ++var2) {
         for(int var5 = 0; var5 <= (MotherCanvas.h - MapOff_RedLine.AH + var2 * 10 + 6) / 24; ++var5) {
            Point var4;
            (var4 = new Point()).x = var2 * 24;
            if (isFinish) {
               var4.y = var2 * 5 - 6 + var5 * 24;
            } else {
               var4.y = -var2 * 5 - 6 + var5 * 24;
            }

            var4.frame = CRes.random(7);
            if (CRes.random(10) == 0) {
               var4.AX = true;
            }

            MapOff_RedLine.AC.addElement(var4);
         }
      }

   }

   public static void ObjectMove(Message m) {
      try {
         if (LoadMapScreen.isNextMap) {
            while(m.reader().available() > 0) {
               byte cat = m.reader().readByte();
               short id = m.reader().readShort();
               short x = m.reader().readShort();
               short y = m.reader().readShort();
               if (GameScreen.vecObjMove.size() <= 50) {
                  ObjMove o = new ObjMove(cat, id, x, y);
                  GameScreen.vecObjMove.addElement(o);
               }
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public final void SetImage(Message m) {
      try {
         short id = m.reader().readShort();
         byte[] raws = new byte[m.reader().available()];
         m.reader().read(raws);
         Ageticon.onReceive(id, raws);
         if (ObjectData.setIdOK()) {
            SaveImageRMS.vecSaveImage.addElement(new idSaveImage(id, raws));
         }

         mImage image = mImage.AA(raws);
         if (id >= 26000) {
            ObjectData.HashImageCharPart.put("" + (id - 26000 + 10000), new MainImage(image));
         } else if (id >= 25000) {
            ObjectData.HashImageEffClientLow.put("" + (id - 25000), new MainImage(image));
         } else if (id >= 24000) {
            ObjectData.HashImageEffClient.put("" + (id - 24000), new MainImage(image));
         } else if (id >= 23000) {
            ObjectData.HashImageOtherNew.put("" + (id - 23000), new MainImage(image));
         } else if (id >= 22000) {
            ObjectData.hashImageIconClanBig.put("" + (id - 22000), new MainImage(image));
         } else if (id >= 21000) {
            ObjectData.hashImageSkill.put("" + id, new MainImage(image));
         } else if (id >= 20000) {
            ObjectData.HashImageFashion.put("" + (id - 20000), new MainImage(image));
         } else if (id >= 10000) {
            ObjectData.HashImageCharPart.put("" + (id - 10000), new MainImage(image));
         } else if (id >= 9000) {
            ObjectData.hashImageItemOther.put("" + (id - 9000), new MainImage(image));
         } else if (id >= 8000) {
            ObjectData.hashImageBoat.put("" + (id - 8000), new MainImage(image));
         } else if (id >= 7000) {
            ObjectData.hashImageIconClan.put("" + (id - 7000), new MainImage(image));
         } else if (id >= 6500) {
            ObjectData.hashImageMaterialPotion.put("" + (id - 6500), new MainImage(image));
         } else if (id >= 6000) {
            ObjectData.hashImageQuestPotion.put("" + (id - 6000), new MainImage(image));
         } else if (id >= 5000) {
            ObjectData.hashImageNPC.put("" + (id - 5000), new MainImage(image));
         } else if (id >= 4500) {
            ObjectData.hashImageSkillSmall.put("" + (id - 4500), new MainImage(image));
         } else if (id >= 4000) {
            ObjectData.hashImageSkill.put("" + (id - 4000), new MainImage(image));
         } else if (id >= 3000) {
            ObjectData.hashImageItem.put("" + (id - 3000), new MainImage(image));
         } else if (id >= 2000) {
            ObjectData.hashImagePotion.put("" + (id - 2000), new MainImage(image));
         } else {
            if (id < 1000) {
               ObjectData.HashImageItemMap.put("" + id, new MainImage(image));
               return;
            }

            ObjectData.HashImageMonster.put("" + (id - 1000), new MainImage(image));
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public final void loadImage(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         short var3 = var1.reader().readShort();
         byte[] var4 = new byte[var1.reader().available()];
         var1.reader().read(var4);
         if (ObjectData.setIdOK()) {
            SaveImageRMS.vecSaveImage.addElement(new idSaveImage(var3, var4));
         }

         mImage var6 = mImage.AA(var4);
         if (var2 == 7) {
            ObjectData.HashImageCharPart.put("" + var3, new MainImage(var6));
         }

      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public final void char_info(Message m) {
      try {
         short id = m.reader().readShort();
         byte b = m.reader().readByte();
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)id, (byte)b)) != null) {
            byte typePlayer = m.reader().readByte();
            mainObject.typePirate = m.reader().readByte();
            mainObject.typePK = m.reader().readByte();
            byte b2 = m.reader().readByte();
            mainObject.indexTeam = m.reader().readByte();
            mainObject.name = m.reader().readUTF();
            mainObject.typePlayer = typePlayer;
            mainObject.Lv = m.reader().readShort();
            mainObject.maxHp = m.reader().readInt();
            mainObject.Hp = m.reader().readInt();
            mainObject.LvThongThao = m.reader().readShort();
            mainObject.rankWanted = m.reader().readInt();
            mainObject.levelPerfect = m.reader().readByte();
            if (mainObject.typePlayer == 1) {
               mainObject.colorName = 4;
            }
            if (mainObject.Hp <= 0) {
               mainObject.beginDie((MainObject)null);
               mainObject.isDie = true;
            } else {
               mainObject.Action = 0;
            }

            byte imgMonSterforOtherPlayer;
            short idmainShiper;
            if (mainObject.typePlayer != 2 && mainObject.typePlayer != 3) {
               if (LoadMap.specMap == 4) {
                  mainObject.boatSea = new Boat(mainObject.ID, mainObject.x, mainObject.y, 0, (byte)mainObject.type_left_right);
                  mainObject.setSpeed(4, 3);
                  mainObject.wOne = 100;
                  mainObject.vySea = 4;
               } else {
                  mainObject.boatSea = null;
                  mainObject.setSpeed(7, 7);
                  mainObject.wOne = 26;
               }

               mainObject.clazz = m.reader().readByte();
               mainObject.MH = m.reader().readByte();
               if (b == 2) {
                  mainObject.BF = m.reader().readUTF();
                  mainObject.BA = m.reader().readUTF();
               }

               if (m.reader().available() > 0) {
                  mainObject.MA = m.reader().readByte();
               }

               if (LoadMap.specMap == 3) {
                  GlobalService.getInstance().AE(id);
                  mainObject.BP();
               }

               if (GameScreen.CN == 1) {
                  imgMonSterforOtherPlayer = 0;
                  b = 0;

                  for(int var16 = 0; var16 < GameScreen.vecPlayers.size(); ++var16) {
                     MainObject var7;
                     if ((var7 = (MainObject)GameScreen.vecPlayers.elementAt(var16)).indexTeam == 1) {
                        var7.MM = imgMonSterforOtherPlayer++;
                     } else if (var7.indexTeam == 2) {
                        var7.MM = b++;
                     }
                  }

                  if (GameScreen.CP && GameScreen.CO == null) {
                     GameScreen.CO = mainObject;
                  }
               }
            } else {
               if (b2 == 3) {
                  b2 = -1;
               }

               mainObject.colorName = 4;
               if (mainObject.typePlayer == 3) {
                  mainObject.colorName = 2;
               }

               mainObject.IdIcon = m.reader().readShort();
               imgMonSterforOtherPlayer = m.reader().readByte();
               idmainShiper = m.reader().readShort();
               typePlayer = m.reader().readByte();
               mainObject.setSpeed(2, 2);
               mainObject.typePirate = typePlayer;
               mainObject.IDMainShiper = idmainShiper;
               mainObject.setImgMonSterforOtherPlayer(imgMonSterforOtherPlayer);
               if (LoadMap.specMap == 4) {
                  mainObject.boatSea = new Boat(mainObject.ID, mainObject.x, mainObject.y, 0, (byte)mainObject.type_left_right);
                  mainObject.vySea = 4;
               }
            }

            mainObject.isInfo = true;
            mainObject.AB(b2);
            mainObject.BU();

            try {
               id = m.reader().readShort();
            } catch (Exception var11) {
               id = -1;
            }

            mainObject.AE(id);

            try {
               idmainShiper = m.reader().readShort();
            } catch (Exception var10) {
               idmainShiper = -1;
            }

            mainObject.AF(idmainShiper);

            short var17;
            try {
               var17 = m.reader().readShort();
            } catch (Exception var9) {
               var17 = -1;
            }

            mainObject.AG(var17);

            short var13;
            try {
               var13 = m.reader().readShort();
            } catch (Exception var8) {
               var13 = -1;
            }

            mainObject.AH(var13);
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   public static void AG(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         if (var0.reader().available() == 0) {
            return;
         }

         byte var4 = var0.reader().readByte();
         if (var1 == GameScreen.player.ID) {
            return;
         }

         MainObject var2;
         if ((var2 = MainObject.get_Object((int)var1, (byte)0)) != null && !var2.isRemove) {
            var2.isRemove = true;
            if (GameCanvas.lowGraphic) {
               GameScreen.AA((short)var1, (byte)0);
               return;
            }

            switch(var4) {
            case 1:
               var2.LC = 8;
               if (GameScreen.isShowNameSUPER_BOSS) {
                  GameScreen.addEffectEnd((short)31, 0, var2.x, var2.y, (byte)var2.type_left_right, (MainObject)null);
                  return;
               }
               break;
            case 2:
               if (var2.x < 100) {
                  var2.LC = 10;
                  var2.toX = 0;
                  return;
               }

               if (var2.x > GameCanvas.loadmap.maxWMap - 100) {
                  var2.LC = 10;
                  var2.toX = GameCanvas.loadmap.maxWMap;
                  return;
               }

               GameScreen.addEffectEnd((short)80, 0, var2.x, var2.y - var2.hOne / 2, (byte)var2.type_left_right, (MainObject)null);
               return;
            case 3:
               if (var2.typePlayer != 2 && var2.typePlayer != 3) {
                  label64:
                  for(int var5 = 0; var5 < GameScreen.vecBoat.size(); ++var5) {
                     Boat var6;
                     if ((var6 = (Boat)GameScreen.vecBoat.elementAt(var5)).ID == var2.ID) {
                        var5 = 0;

                        while(true) {
                           if (var5 >= LoadMap.mSea.length) {
                              break label64;
                           }

                           if (LoadMap.mSea[var5][0] == GameCanvas.loadmap.idMapLoadMap) {
                              var2.LC = 80;
                              var2.AA((byte)((byte)(LoadMap.mSea[var5][2] - 1)), (int)var6.x, (int)LoadMap.mSea[var5][4]);
                              return;
                           }

                           ++var5;
                        }
                     }
                  }

                  GameScreen.addEffectEnd((short)80, 0, var2.x, var2.y - var2.hOne / 2, (byte)var2.type_left_right, (MainObject)null);
                  return;
               }

               var2.LC = 80;
               return;
            default:
               GameScreen.addEffectEnd((short)80, 0, var2.x, var2.y - var2.hOne / 2, (byte)var2.type_left_right, (MainObject)null);
               return;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   

   private static void FZ(Message m) {
      try {
         byte[] array = new byte[m.reader().available()];
         m.reader().read(array);
         ByteArrayInputStream bais = new ByteArrayInputStream(array);
         CharPartInfo.LoadDataCharPart((DataInputStream)(new DataInputStream(bais)), 1);
         SaveRms.saveData(array, "dataCharPart");
      } catch (Exception ex) {
      }
   }

   private static void Get_Potion_Tem(Message m, byte typePotion) {
      try {
         byte[] array = new byte[m.reader().available()];
         m.reader().read(array);
         ByteArrayInputStream bais = new ByteArrayInputStream(array);
         Potion.UpdateDataPotion(new DataInputStream(bais), true, typePotion);
         if (typePotion == 4) {
            SaveRms.saveData(array, "dataPotion");
         }

         if (typePotion == 8) {
            SaveRms.saveData(array, "dataPotionClan");
            return;
         }
      } catch (Exception ex) {
      }

   }

   private static void Update_Potion_Tem(Message m, byte typePotion) {
      try {
         byte[] array = new byte[m.reader().available()];
         m.reader().read(array);
         ByteArrayInputStream bais = new ByteArrayInputStream(array);
         Potion.UpdateDataPotion(new DataInputStream(bais), true, (byte)4);
         SaveRms.saveData(array, "dataPotion");
      } catch (Exception var2) {
      }
   }
   public final void getData(Message m) {
      try {
         int var3;
         byte var4;
         ByteArrayInputStream bais;
         int j;
         byte b;
         byte b4;
         byte[] var26;
         switch(m.reader().readByte()) {
         case 2:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               MainItem.AA(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataAttri");
               break;
            } catch (Exception var15) {
               var15.printStackTrace();
               return;
            }
         case 3://
            this.Get_Skill_Player(m);
            //Agetskill.onServerMessage(m);
            return;
         case 4:
            LoadMap.mLockMap = new byte[b = m.reader().readByte()];
            for(var3 = 0; var3 < b; ++var3) {
               LoadMap.mLockMap[var3] = m.reader().readByte();
            }
            return;
         case 5:
            FZ(m);
            return;
         case 6:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               LoadMap.AA(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataNameMap");
               break;
            } catch (Exception var14) {
               return;
            }
         case 7:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               MainQuest.AA(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataNamePotionquest");
               break;
            } catch (Exception var13) {
               return;
            }
         case 8:
            TabInventory.priceItemSell = m.reader().readShort();
            TabInventory.maxPriceItemSell = m.reader().readShort();
            TabInventory.CU = m.reader().readShort();
            return;
         case 9:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               LoadMap.AB(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataItemMap");
               break;
            } catch (Exception var12) {
               return;
            }
         case 10:
            LoadMap.mMapLang = new short[b4 = m.reader().readByte()];

            for(j = 0; j < b4; ++j) {
               LoadMap.mMapLang[j] = m.reader().readShort();
            }

            return;
         case 11:
            b = m.reader().readByte();

            for(var3 = 0; var3 < b; ++var3) {
               MainMaterial var27 = new MainMaterial(m.reader().readByte(), m.reader().readUTF(), m.reader().readByte(), m.reader().readByte(), m.reader().readInt(), m.reader().readShort(), m.reader().readByte());
               MainItem.hashMaterialTem.put("" + var27.ID, var27);
            }
            GlobalService.AB = true;
            return;
         case 12:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               MainDataUpgrade.LoadDataUpgrade(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataUpgradeSave");
               break;
            } catch (Exception var11) {
               return;
            }
         case 13:
            LoadMap.mSea = new short[b4 = m.reader().readByte()][];
            for(int i = 0; i < b4; ++i) {
               LoadMap.mSea[i] = new short[6];

               for(j = 0; j < LoadMap.mSea[i].length; ++j) {
                  LoadMap.mSea[i][j] = m.reader().readShort();
               }
            }

            return;
         case 15:
            m = m;

            try {
               var26 = new byte[m.reader().available()];
               m.reader().read(var26);
               bais = new ByteArrayInputStream(var26);
               CatalogyMonster.AA(new DataInputStream(bais), true);
               SaveRms.saveData(var26, "dataMon");
               break;
            } catch (Exception var16) {
               var16.printStackTrace();
               return;
            }
         case 17:
            GameCanvas.clockServer = m.reader().readLong() - GameCanvas.timeNow;
            return;
         case 18:
            Get_Potion_Tem(m, (byte)8);
            return;
         case 19:
            ScreenUpgrade.BH = new short[var4 = m.reader().readByte()];

            for(j = 0; j < var4; ++j) {
               ScreenUpgrade.BH[j] = m.reader().readShort();
            }

            ScreenUpgrade.BI = new short[b = m.reader().readByte()];

            for(int var9 = 0; var9 < b; ++var9) {
               ScreenUpgrade.BI[var9] = m.reader().readShort();
            }

            return;
         case 21:
            if (m.reader().readByte() == 0) {
               GameScreen.h12plus = 0;
               Interface_Game.AD();
            } else {
               GameScreen.h12plus = 12;
               Interface_Game.AD();
            }

            Interface_Game var10000 = GameScreen.interfaceGame;
            Interface_Game.AC();
            return;
         case 22:
            byte var18;
            if ((var18 = m.reader().readByte()) == 1) {
               GameScreen.isShowNameXpArena = true;
               return;
            }

            if (var18 == 0) {
               GameScreen.isShowNameSUPER_BOSS = false;
               return;
            }

            if (var18 == 2) {
               GameScreen.isShowNameWW = true;
               return;
            }
            break;
         case 23:
            FZ(m);
            return;
         case 25:
            Get_Potion_Tem(m, (byte)4);
            return;
         case 26:
            GlobalService.AC = true;
            b = m.reader().readByte();

            for(var3 = 0; var3 < b; ++var3) {
               String var23 = m.reader().readUTF();
               MainItem.CD.put("" + var3, var23);
            }

            return;
         case 27:
            if (m.reader().readByte() == 1) {
               GameScreen.IZ = true;
               return;
            }

            GameScreen.IZ = false;
            return;
         case 28:
            Update_Potion_Tem(m, (byte)4);
            return;
         case 29:
            Update_Potion_Tem(m, (byte)4);
            return;
         case 30:
            T.mEffSpec = new String[var4 = m.reader().readByte()];

            for(j = 0; j < var4; ++j) {
               T.mEffSpec[j] = m.reader().readUTF();
            }

            return;
         case 31:
            short var2;
            MainObject.dataEffHair = new DataHairServer[var2 = m.reader().readShort()];

            for(var3 = 0; var3 < var2; ++var3) {
               MainObject.dataEffHair[var3] = new DataHairServer();
               MainObject.dataEffHair[var3].id = m.reader().readInt();
               MainObject.dataEffHair[var3].idIcon = m.reader().readInt();
            }

            short var22;
            if ((var22 = m.reader().readShort()) <= 0) {
               break;
            }

            MainObject.PA = new DataHairServer[var22];

            for(j = 0; j < var22; ++j) {
               MainObject.PA[j] = new DataHairServer();
               MainObject.PA[j].id = m.reader().readInt();
               MainObject.PA[j].AC = m.reader().readUTF();
            }
         case 14:
         case 16:
         case 20:
         case 24:
         default:
            return;
         }
      } catch (Exception var17) {
         (new StringBuffer("info hair e::")).append(var17.getMessage()).toString();
      }

   }
   private void Get_Skill_Player(Message m) {
      try {
         indexHotKeySkill = 0;
         Player.vecListSkill.removeAllElements();
         byte var2 = m.reader().readByte();

         int num;
         Skill_Info skillIn4;
         for(num = 0; num < var2; ++num) {
            if ((skillIn4 = readSkillInfo(m)) != null) {
               skillIn4.indexHotKey = indexHotKeySkill;
               Player.vecListSkill.addElement(skillIn4);
               ++indexHotKeySkill;
            }
         }
         Player.vecListSkill = MainItem.SortVecItem(Player.vecListSkill);
         GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)0, (byte[])null);
         Player.isSkillready = false;
         for(num = 0; num < Player.vecListSkill.size(); ++num) {
            if ((skillIn4 = (Skill_Info)Player.vecListSkill.elementAt(num)).Lv_RQ == -1 && (skillIn4.typeSkill != 3 && skillIn4.typeSkill != 6 || TabSkill.BO < Player.QM)) {
               Player.isSkillready = true;
               break;
            }
         }
         Player.setHotKeyBuff();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   private static Skill_Info readSkillInfo(Message m) {
      Skill_Info skIn4 = null;
      try {
         (skIn4 = new Skill_Info(m.reader().readShort(), m.reader().readShort(), m.reader().readShort(), m.reader().readByte(), m.reader().readByte(), m.reader().readUTF(), m.reader().readShort(), m.reader().readShort(), (short)0)).getData(m.reader().readByte(), m.reader().readShort(), m.reader().readInt(), m.reader().readShort(), m.reader().readInt(), m.reader().readByte(), m.reader().readUTF(), m.reader().readByte(), m.reader().readShort(), m.reader().readByte());
         skIn4.vecAtt.removeAllElements();
         byte var2 = m.reader().readByte();
         for(int i = 0; i < var2; ++i) {
            MainInfoItem mInfoItem = new MainInfoItem(m.reader().readByte(), m.reader().readShort());
            skIn4.vecAtt.addElement(mInfoItem);
         }

         skIn4.idEffSpec = m.reader().readByte();
         if (skIn4.idEffSpec > 0) {
            skIn4.perEffSpec = m.reader().readShort();
            skIn4.timeEffSpec = m.reader().readShort();
         }

         skIn4.LvDevilSkill = m.reader().readByte();
         skIn4.phanTramDevilSkill = m.reader().readByte();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
      return skIn4;
   }

   public static void monsterInfo(Message m) {
      try {
         if (!LoadMapScreen.isNextMap) {
            return;
         }

         short id = m.reader().readShort();
         short idCatMonster = m.reader().readShort();
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)id, (byte)1)) != null) {
            mainObject.isRemove = true;
         }

         short x = m.reader().readShort();
         short y = m.reader().readShort();
         MainMonster mainMonster;
         if ((mainMonster = MainMonster.createMonster(id, x, y, idCatMonster)) == null) {
            return;
         }

         mainMonster.Lv = m.reader().readShort();
         mainMonster.Hp = m.reader().readInt();
         mainMonster.maxHp = m.reader().readInt();
         x = m.reader().readShort();
         MainSkill skilldefault = new MainSkill((short)-1, x);
         mainMonster.Skilldefault = skilldefault;
         mainMonster.timeRevice = m.reader().readShort();
         mainMonster.typeSpecMonSter = m.reader().readByte();
         mainMonster.isInfo = true;
         if (m.reader().available() > 0) {
            mainMonster.LvThongThao = m.reader().readByte();
            if (mainMonster.LvThongThao > 0) {
               mainMonster.name = mainMonster.name + T.TP + mainMonster.LvThongThao;
               mainMonster.BU();
            }
         }

         mainMonster.idCatMonster = idCatMonster;
         GameScreen.addPlayer((MainObject)mainMonster);
         if (mainMonster.MX > 0) {
            GameScreen.addEffectEnd_ObjTo((short)32, 0, mainMonster.x, mainMonster.y, (short)mainMonster.ID, (byte)mainMonster.typeObject, (byte)mainMonster.Dir, (MainObject)null);
         }

         if (mainMonster.BZ() && mainMonster.Hp <= 0) {
            mainMonster.isDie = true;
            mainMonster.timeDie = 0L;
            return;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void remove_Char(Message var0) {
      try {
         if (LoadMapScreen.isNextMap) {
            if (GameCanvas.lowGraphic && GameScreen.vecObjFire.size() > 20) {
               GameScreen.vecObjFire.removeElement(1);
            }

            var0.isOld = 1;
            GameScreen.vecObjFire.addElement(var0);
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   public static void AK(Message var0) {
      try {
         MainMonster var2;
         if ((var2 = (MainMonster)MainObject.get_Object((int)var0.reader().readShort(), (byte)1)) != null && !var2.returnAction()) {
            if (var2.Action == 4) {
               if (var2.timeRevice < 0) {
                  return;
               }

               var2.Reveive();
            }

            if (var2.skillCurrent != null) {
               var2.AL();
            }

            var2.AM = false;
            var2.LO = null;
            var2.posTransRoad = null;
         }
      } catch (Exception var1) {
      }
   }

   public static void AL(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         short var3 = var0.reader().readShort();
         byte var4 = var0.reader().readByte();
         short var6 = var0.reader().readShort();
         MainObject var8 = MainObject.get_Object((int)var3, (byte)var4);
         MainObject var7 = MainObject.get_Object((int)var1, (byte)var2);
         if (var2 == 0 && var7 != null && !var7.returnAction()) {
            var7.KN = var6;
         }

         if (var8 != null && !var8.returnAction()) {
            if (var8.Hp > 0) {
               var8.Hp = 0;
               var8.beginDie(var7);
               if (var8 == GameScreen.player || var8.typeObject == 0 && CRes.random(3) == 0) {
                  float var10000;
                  if (var8.clazz == 4) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  } else {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }
               }
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public static void Revice_Player(Message m) {
      try {
         short ID = m.reader().readShort();
         byte type = m.reader().readByte();
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)ID, (byte)type)) != null && !mainObject.returnAction()) {
            mainObject.maxHp = m.reader().readInt();
            mainObject.maxMp = m.reader().readInt();
            mainObject.Reveive();
         }
      } catch (Exception var3) {
      }
   }

   public static void AN(Message var0) {
      try {
         MainObject var1;
         if ((var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)0)) != null && !var1.returnAction()) {
            if (var1.Lv == 100) {
               var1.KS = var0.reader().readShort();
            } else {
               var1.percentLv = var0.reader().readShort();
            }

            int var3 = var0.reader().readInt();
            if (!GameCanvas.lowGraphic || var1 == GameScreen.player) {
               GameScreen.addEffectNum(var3 + "xp", var1.x, var1.y - var1.hOne, (byte)1);
            }

            if (var1 == GameScreen.player && GameScreen.IX) {
               GameCanvas.chatTabScr.AB(T.tabTestAdmin, "+Xp: ", "" + var3, (byte)1, false);
               return;
            }
         }
      } catch (Exception var2) {
      }

   }

   public static void AO(Message var0) {
      try {
         MainObject var1;
         if ((var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)0)) != null && !var1.returnAction()) {
            int var3 = var0.reader().readInt();
            if (!GameCanvas.lowGraphic || var1 == GameScreen.player) {
               GameScreen.addEffectNum(var3 + "xp", var1.x, var1.y - var1.hOne, (byte)24);
            }
            if (var1 == GameScreen.player && GameScreen.IX) {
               GameCanvas.chatTabScr.AB(T.tabTestAdmin, "+XpSkill: ", "" + var3, (byte)1, false);
               return;
            }
         }
      } catch (Exception var2) {
      }

   }

   public static void AP(Message var0) {
      try {
         LoadMap.AV = var0.reader().readInt();
         LoadMap.AW = var0.reader().readInt();
      } catch (Exception var1) {
      }
   }

   public final void Main_char_Info(Message var1) {
      try {
         SaveRms var10000;
         int var2;
         if (GameScreen.player == null) {
            GameScreen.player = new Player();
            var10000 = GameCanvas.saveRms;
            SaveRms.AA(GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
            GameScreen.vecPlayers.addElement(GameScreen.player);
            GameCanvas.chatTabScr.AM.removeAllElements();
            Player.vecParty.removeAllElements();
            Player.mSatnhan = new short[0];
            GameCanvas.tabAllScr.idSelect = 0;
            GameCanvas.tabAllScr.setTabSelect();
            GameCanvas.tabAllScr.AC.AB();
            Interface_Game.vecEventShow.removeAllElements();
            Interface_Game.vecInfoServer.removeAllElements();
            Interface_Game.CC = null;
            Interface_Game.CD = null;
            Interface_Game.CE = null;
            Interface_Game.BO = null;
            Class_GL.AA = null;
            LoadMap.hashMapItem.AA();
            LoadMap.BD = 0;
            Player.vecEvent.removeAllElements();
            Player.vecChest.removeAllElements();
            Player.vecInventory.removeAllElements();
            Player.AW.removeAllElements();
            Player.QG = null;
            Player.QP = 0;
            Player.isGhost = false;
            Player.SN = 0L;
            MainTab.BI.setCountDown(0);
            MainTab.BH.setCountDown(0);
            MainTab.BG.setCountDown(0);
            MainTab.BJ.setCountDown(0);
            LoadMap.specMap = 0;
            GameScreen.CV = -1;
            GameScreen.CU = null;
            GameCanvas.chatTabScr.AB(T.CR, "", T.CT, (byte)1, false);
            GameCanvas.chatTabScr.AD(0);
            GameCanvas.chatTabScr.AF();
            GameScreen.numMess = 0;
            Class_FN.AA = false;
            Player.isGetItem = true;
            Player.isMPHP = true;
            Player.typeAutoFireMain = 1;
            Player.AutoFireCur = 0;
            Player.isGetDataClan = -1;

            for(var2 = 0; var2 < Player.QX.length; ++var2) {
               Player.QX[var2] = null;
            }

            GameScreen.player.clan = null;
            GameCanvas.ClanScr = null;
            MsgAutoFire.value = null;
            ScreenUpgradeSkillDevil.AA = null;
            Player.setStart_EndAutoFire(false);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)1, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)8, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)2, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)3, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)4, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)5, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)6, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)7, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)9, (byte[])null);
            GlobalService.getInstance().Save_RMS_Server((byte)0, (byte)10, (byte[])null);
            if (Player.AutoRevice == 1) {
               Interface_Game.addInfoPlayerNormal(T.MH, mFont.tahoma_7_white);
            }
         }

         GameScreen.player.ID = var1.reader().readShort();
         GameScreen.player.name = var1.reader().readUTF();
         var10000 = GameCanvas.saveRms;
         SaveRms.AA(GameScreen.player.name);
         GameScreen.player.maxHp = var1.reader().readInt();
         GameScreen.player.maxMp = var1.reader().readInt();
         GameScreen.player.Hp = var1.reader().readInt();
         GameScreen.player.Mp = var1.reader().readInt();
         GameScreen.player.Lv = var1.reader().readShort();
         GameScreen.player.percentLv = var1.reader().readShort();
         GameScreen.player.LvThongThao = var1.reader().readShort();
         GameScreen.player.KS = var1.reader().readShort();
         GameScreen.player.rankWanted = var1.reader().readInt();
         GameScreen.player.clazz = var1.reader().readByte();
         GameScreen.player.isInfo = true;
         GameScreen.player.KN = var1.reader().readInt();
         Player.AS = var1.reader().readShort();
         GameScreen.player.typePirate = var1.reader().readByte();
         Player.QL = var1.reader().readByte();
         Player.QM = var1.reader().readByte();
         GameScreen.player.levelPerfect = var1.reader().readByte();
         Player.giamCountDownAtt = 0;
         AR(var1);
         Player.AR = var1.reader().readShort();
         byte var11;
         Player.QA = new byte[var11 = var1.reader().readByte()];
         Player.QB = new byte[var11];

         for(var2 = 0; var2 < Player.QA.length; ++var2) {
            Player.QA[var2] = var1.reader().readByte();
         }

         for(var2 = 0; var2 < Player.QB.length; ++var2) {
            Player.QB[var2] = var1.reader().readByte();
         }

         GameScreen.player.AF.removeAllElements();
         var11 = var1.reader().readByte();

         for(int var3 = 0; var3 < var11; ++var3) {
            byte var4 = var1.reader().readByte();
            MainInfoItem var13;
            if ((var13 = new MainInfoItem(var4, var1.reader().readInt())).AA <= 4 || var13.AA >= 10) {
               GameScreen.player.AF.addElement(var13);
            }

            for(int var5 = 0; var5 < Player.RP[0].length; ++var5) {
               if (var13.AA == Player.RP[0][var5]) {
                  Player.RP[1][var5] = var13.AE;
               }
            }

            if (var13.AA == 25) {
               Player.giamCountDownAtt = (short)var13.AE;
            }
         }

         Player.SetGiamCountDown();
         Player.RQ[0] = MainItem.AA(Player.RP[1][0], (byte)0);
         Player.RQ[1] = MainItem.AA(Player.RP[1][1], (byte)1);
         Player.RQ[2] = "" + (Player.RP[1][2] + Player.RP[1][2] * Player.RP[1][3] / 1000);
         Player.RQ[3] = MainItem.AA(Player.RP[1][4], (byte)1);
         Player.RQ[4] = MainItem.AA(Player.RP[1][5], (byte)1);
         Player.RQ[5] = MainItem.AA(Player.RP[1][6], (byte)1);

         short var12;
         try {
            var12 = var1.reader().readShort();
         } catch (Exception var9) {
            var12 = -1;
         }

         GameScreen.player.AE(var12);

         short var14;
         try {
            var14 = var1.reader().readShort();
         } catch (Exception var8) {
            var14 = -1;
         }

         GameScreen.player.AF(var14);

         try {
            var14 = var1.reader().readShort();
         } catch (Exception var7) {
            var14 = -1;
         }

         GameScreen.player.AG(var14);

         short var15;
         try {
            var15 = var1.reader().readShort();
         } catch (Exception var6) {
            var15 = -1;
         }

         GameScreen.player.AH(var15);
      } catch (Exception var10) {
         var10.printStackTrace();
      }
   }

   public static void AR(Message var0) {
      try {
         byte var1;
         Class_CV[] var2 = new Class_CV[var1 = var0.reader().readByte()];

         for(int var3 = 0; var3 < var1; ++var3) {
            String var4 = var0.reader().readUTF();
            short var5 = var0.reader().readShort();
            short var6 = var0.reader().readShort();
            MainInfoItem[] var7 = new MainInfoItem[var0.reader().readByte()];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = new MainInfoItem(var0.reader().readByte(), var0.reader().readInt());
            }

            var2[var3] = new Class_CV((byte)var3, var5, var6, var4, var7);
         }

         TabInfo.AA(var2);
      } catch (Exception var9) {
      }
   }

   public final void Dialog_More_server(Message var1) {
      System.out.println("Dialog server tra kq");
      if (GameCanvas.currentScreen == GameCanvas.CQ) {
         GameCanvas.currentDialog = null;
         GameCanvas.subDialog = null;
      } else {
         try {
            AR = var1.reader().readShort();
            byte var2 = var1.reader().readByte();
            String var3 = var1.reader().readUTF();
            String var4 = var1.reader().readUTF();
            mVector var5 = new mVector();
            byte var6 = var1.reader().readByte();

            int var7;
            for(var7 = 0; var7 < var6; ++var7) {
               String var8 = var1.reader().readUTF();
               byte var9 = var1.reader().readByte();
               iCommand var10 = new iCommand(var8, 0, var9, this);
               byte var11;
               if ((var11 = var1.reader().readByte()) >= 0) {
                  MainItem var19 = new MainItem((byte)100, var11, (short)0);
                  var10.AA(var19);
                  var10 = var10;
               }

               var5.addElement(var10);
            }

            if (var2 == 0) {
               GameCanvas.Start_Normal_DiaLog(var4, var5, true);
            } else if (var2 == 1) {
               MsgDialog var16;
               (var16 = new MsgDialog()).AA(var4, var3);
               GameCanvas.Start_Current_Dialog((MainDialog)var16);
            } else if (var2 == 2) {
               GameCanvas.Start_Normal_DiaLog(var4, var5, false);
            } else if (var2 == 3) {
               var7 = var1.reader().readInt();
               GameCanvas.AA(var4, false, var7, var5);
            } else if (var2 == 4) {
               byte var17;
               Item_Drop[] var18 = new Item_Drop[var17 = var1.reader().readByte()];

               for(int var20 = 0; var20 < var17; ++var20) {
                  byte var22 = var1.reader().readByte();
                  String var23 = var1.reader().readUTF();
                  short var14 = var1.reader().readShort();
                  int var15 = var1.reader().readInt();
                  byte var12 = var1.reader().readByte();
                  var18[var20] = new Item_Drop((short)var20, var22, var23, 0, 0, var14, var12);
                  var18[var20].num = var15;
               }

               MsgShowGift var21;
               (var21 = new MsgShowGift()).setinfoShow_Gift((byte)1, var3, var4, var18, (short)-1);
               var21.setCmdList(var5);
               GameCanvas.Start_Current_Dialog((MainDialog)var21);
            }

            GameMidlet var10000 = GameMidlet.AD;
            GameMidlet.AF();
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }
   }

   public static void Item_Drop(Message m) {
      try {
         mVector mVector = new mVector();
         byte b = m.reader().readByte();

         int i;
         for(i = 0; i < b; ++i) {
            short id = m.reader().readShort();
            byte typeItem = m.reader().readByte();
            short Idicon = m.reader().readShort();
            byte colorName = m.reader().readByte();
            String name = m.reader().readUTF();
            MainItem var15;
            (var15 = new MainItem(typeItem, id, Idicon, name, (byte)0)).colorName = colorName;
            mVector.addElement(var15);
         }

         short id2 = m.reader().readShort();
         byte tem = m.reader().readByte();
         short id3 = m.reader().readShort();
         int num = 456;
         int num2 = 240;
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)id2, (byte)tem)) != null) {
            num = mainObject.x;
            num2 = mainObject.y;
         } else {
            MainObject mainObject2;
            if ((mainObject2 = MainObject.get_Object((int)id3, (byte)0)) != null) {
               num = mainObject2.x;
               num2 = mainObject2.y;
            }
         }

         if (mVector.size() > 3) {
            byte var21 = 40;
            byte var12 = 45;
            int var13 = CRes.random(45);

            for(i = 0; i < mVector.size(); ++i) {
               if (i == 8) {
                  var21 = 60;
                  var12 = 30;
                  var13 = CRes.random(30);
               } else if (i == 20) {
                  var21 = 80;
                  var12 = 22;
                  var13 = CRes.random(22);
               } else if (i == 36) {
                  var21 = 100;
                  var12 = 15;
                  var13 = CRes.random(15);
               }

               int var20 = num + CRes.getcos(CRes.fixangle(var13)) * var21 / 1000;
               int var25 = num2 + CRes.getsin(CRes.fixangle(var13)) * var21 / 1000;
               MainItem var9 = (MainItem)mVector.elementAt(i);
               GameScreen.addPlayer((MainObject)(new Item_Drop(var9.ID, var9.typeObject, var9.name, num, num2, var9.idIcon, var9.colorName, var20, var25)));
               var13 += var12;
            }

         } else {
            for(int var19 = 0; var19 < mVector.size(); ++var19) {
               MainItem var11 = (MainItem)mVector.elementAt(var19);
               GameScreen.addPlayer((MainObject)(new Item_Drop(var11.ID, var11.typeObject, var11.name, num, num2, var11.idIcon, var11.colorName)));
            }

         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }
   }

   public static void GetItemMap(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         short var8 = var0.reader().readShort();
         Item_Drop var10;
         if ((var10 = (Item_Drop)MainObject.get_Object((int)var1, (byte)var2)) != null && !var10.returnAction()) {
            MainObject var9;
            if ((var9 = MainObject.get_Object((int)var8, (byte)0)) != null && !var9.returnAction()) {
               if (var9 == GameScreen.player) {
                  if (GameScreen.CV == 9) {
                     MainHelp.AA(false);
                  }

                  MainHelp.AA(8);
               }

               if (!GameCanvas.lowGraphic || var9 == GameScreen.player) {
                  if (var10.typeObject == 4 && var10.colorName == 5) {
                     GameScreen.AA(var10.name, var9.x, var9.y - var9.hOne / 2, (byte)10, AvMain.fraMoney, 0);
                  } else if ((var10.typeObject == 3 || var10.typeObject == 4) && var10.colorName > 0) {
                     GameScreen.addEffectNum(var10.name, var9.x, var9.y - var9.hOne / 2, (byte)(-var10.colorName));
                  } else {
                     GameScreen.addEffectNum(var10.name, var9.x, var9.y - var9.hOne / 2, (byte)5);
                  }
               }

               var10.LO = var9;
               if (var9 == GameScreen.player && var2 == 5) {
                  var8 = var10.IdIcon;
                  MainQuest var11 = null;
                  boolean var12 = false;

                  int var3;
                  for(var3 = 0; var3 < Player.QI.size() && !var12; ++var3) {
                     MainQuest var4;
                     if ((var4 = (MainQuest)Player.QI.elementAt(var3)).AB == 1 && (var11 == null || var11.AA >= var4.AA)) {
                        for(int var5 = 0; var5 < var4.AM.size(); ++var5) {
                           Class_CP var6;
                           if ((var6 = (Class_CP)var4.AM.elementAt(var5)).AA == 2 && var6.AB == var8 && var6.AD < var6.AC) {
                              var11 = var4;
                              if (var4.AA == 1) {
                                 var12 = true;
                              }
                              break;
                           }
                        }
                     }
                  }

                  if (var11 != null) {
                     for(var3 = 0; var3 < var11.AM.size(); ++var3) {
                        Class_CP var13;
                        if ((var13 = (Class_CP)var11.AM.elementAt(var3)).AA == 2 && var13.AB == var8 && var13.AD < var13.AC) {
                           ++var13.AD;
                           if (GameScreen.isOnRepeatQuest) {
                              AutoRepeatQuest.AD = true;
                              AutoRepeatQuest.AE = null;
                              AutoRepeatQuest.AC = false;
                              return;
                           }
                           break;
                        }
                     }
                  }

               }
            } else {
               var10.isRemove = true;
            }
         }
      } catch (Exception var7) {
      }
   }

   public static void AV(Message var0) {
      try {
         if (LoadMap.specMap == 7) {
            short var1 = var0.reader().readShort();
            byte var2 = var0.reader().readByte();
            byte var6 = var0.reader().readByte();
            Item_Drop var7;
            if ((var7 = (Item_Drop)MainObject.get_Object((int)var1, (byte)var2)) != null && !var7.returnAction()) {
               BigBossLittleGraden var8 = null;

               for(int var3 = 0; var3 < GameScreen.vecBigBossLittleGraden.size(); ++var3) {
                  BigBossLittleGraden var4;
                  if ((var4 = (BigBossLittleGraden)GameScreen.vecBigBossLittleGraden.elementAt(var3)).AA == var6) {
                     var8 = var4;
                     break;
                  }
               }

               if (var8 != null) {
                  var7.LP = var8;
               } else {
                  var7.isRemove = true;
               }
            }
         }
      } catch (Exception var5) {
      }
   }

   public static void AW(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var3 = var0.reader().readByte();
         MainObject var4;
         if ((var4 = MainObject.get_Object((int)var1, (byte)var3)) == GameScreen.player) {
            return;
         }

         if (var4 != null && !var4.isRemove) {
            var4.isRemove = true;
            var4.LC = 2;
            return;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void update_InVen_Or_Chest(Message var1, mVector var2, byte typeInvenOrChest) {
      try {
         byte b = var1.reader().readByte();
         byte b2 = var1.reader().readByte();
         byte b3;
         if (b == 0) {
            MainItem.removeUpdateItemVec(b2, var2);
            b3 = var1.reader().readByte();
            int i;
            if (b2 != 4 && b2 != 8) {
               if (b2 == 3) {
                  for(i = 0; i < b3; ++i) {
                     Item var27;
                     if ((var27 = GE(var1)) != null) {
                        var2.addElement(var27);
                     }
                  }
               } else if (b2 == 5) {
                  for(i = 0; i < b3; ++i) {
                     Quest_Potion var25;
                     if ((var25 = GF(var1)) != null) {
                        var2.addElement(var25);
                     }
                  }
               } else if (b2 == 7) {
                  for(i = 0; i < b3; ++i) {
                     MainMaterial mainMaterial;
                     if ((mainMaterial = readUpdateMaterial(var1, false)) != null) {
                        var2.addElement(mainMaterial);
                     }
                  }
               }
            } else {
               for(i = 0; i < b3; ++i) {
                  Potion potion2;
                  if ((potion2 = readUpdatePotion(var1, false, b2)) != null) {
                     var2.addElement(potion2);
                  }
               }
            }
         } else {
            MainItem itemVec;
            if (b == 1) {
               if (b2 != 4 && b2 != 8) {
                  if (b2 == 3) {
                     Item item2;
                     if ((item2 = GE(var1)) == null) {
                        return;
                     }

                     if ((itemVec = MainItem.getItemVec(b2, item2.ID, var2)) == null) {
                        var2.addElement(item2);
                     } else {
                        var2.setElementAt(item2, var2.indexOf(itemVec));
                     }
                  } else if (b2 == 5) {
                     Quest_Potion var33;
                     if ((var33 = GF(var1)) == null) {
                        return;
                     }

                     if ((itemVec = MainItem.getItemVec(b2, var33.ID, var2)) == null) {
                        var2.addElement(var33);
                     } else {
                        var2.setElementAt(var33, var2.indexOf(itemVec));
                     }
                  } else if (b2 == 7) {
                     MainMaterial mainMaterial2;
                     if ((mainMaterial2 = readUpdateMaterial(var1, false)) == null) {
                        return;
                     }

                     if ((itemVec = MainItem.getItemVec(b2, mainMaterial2.ID, var2)) == null) {
                        var2.addElement(mainMaterial2);
                     } else {
                        var2.setElementAt(mainMaterial2, var2.indexOf(itemVec));
                     }
                  }
               } else {
                  Potion var30;
                  if ((var30 = readUpdatePotion(var1, false, b2)) == null) {
                     return;
                  }

                  if ((itemVec = MainItem.getItemVec(b2, var30.ID, var2)) == null) {
                     var2.addElement(var30);
                  } else {
                     var2.setElementAt(var30, var2.indexOf(itemVec));
                  }
               }

               MainTabShop.CA = true;
            } else {
               int ruby;
               int var20;
               if (b == 2) {
                  short var28 = var1.reader().readShort();
                  if ((itemVec = MainItem.getItemVec(b2, var28, var2)) != null) {
                     itemVec.numPotion = 0;
                     itemVec.CE = true;
                     var2.removeElement(itemVec);
                  }

                  if (typeInvenOrChest == 100 && b2 == 4) {
                     for(ruby = 0; ruby < Player.hotkeyPlayer.length; ++ruby) {
                        for(var20 = 0; var20 < Player.hotkeyPlayer[ruby].length; ++var20) {
                           Hotkey var36;
                           if ((var36 = Player.hotkeyPlayer[ruby][var20]).itemcur != null && var36.itemcur.ID == var28) {
                              var36.itemcur = null;
                           }
                        }
                     }
                  }

                  if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
                     GameCanvas.tabInven.AP();
                  }
               } else if (b != 3) {
                  if (b == 6) {
                     if (typeInvenOrChest == 100) {
                        Player.maxInventory = var1.reader().readShort();
                        GameCanvas.tabAllScr.AC.AA(Player.maxInventory);
                     } else if (typeInvenOrChest == 99) {
                        Player.maxChest = var1.reader().readShort();
                        if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
                           for(int var26 = 0; var26 < GameCanvas.tabShopScr.AB.size(); ++var26) {
                              MainTab var7;
                              if ((var7 = (MainTab)GameCanvas.tabShopScr.AB.elementAt(var26)).indexIconTab == 7) {
                                 var7.AD = Player.maxChest;
                              }
                           }
                        }
                     }
                  }
               } else {
                  long beri = var1.reader().readLong();
                  ruby = var1.reader().readInt();
                  if (typeInvenOrChest == 100) {
                     boolean flag = false;
                     short ticket = var1.reader().readShort();
                     String var11 = null;
                     if (GameScreen.player.Beri != beri) {
                        if (GameScreen.player.Beri > beri) {
                           var11 = "-" + (GameScreen.player.Beri - beri);
                        } else {
                           var11 = "+" + (beri - GameScreen.player.Beri);
                        }

                        if (GameCanvas.currentScreen != GameCanvas.tabAllScr && GameCanvas.currentScreen != GameCanvas.tabShopScr) {
                           if (GameCanvas.currentScreen == GameCanvas.gameScr) {
                              GameScreen.AA(var11, GameScreen.player.x, GameScreen.player.y - GameScreen.player.hOne, (byte)10, AvMain.fraMoney, 0);
                           }
                        } else {
                           TabScreen.AA(var11, MainTab.AZ - 20, MainTab.BA + 10, (byte)10, AvMain.fraMoney, 0);
                        }
                     }

                     if (GameScreen.player.Ruby != ruby) {
                        if (GameScreen.player.Ruby > ruby) {
                           var11 = "-" + (GameScreen.player.Ruby - ruby);
                        } else {
                           var11 = "+" + (ruby - GameScreen.player.Ruby);
                        }

                        if (GameCanvas.currentScreen != GameCanvas.tabAllScr && GameCanvas.currentScreen != GameCanvas.tabShopScr) {
                           if (GameCanvas.currentScreen == GameCanvas.gameScr) {
                              GameScreen.AA(var11, GameScreen.player.x, GameScreen.player.y - GameScreen.player.hOne, (byte)10, AvMain.fraMoney, 1);
                              Interface_Game.BY = 40;
                              Interface_Game.BZ = 0;
                           } else if (GameCanvas.currentScreen == Class_GJ.AA) {
                              Class_GJ.AA.AA(var11, MainTab.AZ - 10, MainTab.BA + 36, (byte)10, AvMain.fraMoney, 1);
                           }
                        } else {
                           TabScreen.AA(var11, MainTab.AZ - 20, MainTab.BA + 23, (byte)10, AvMain.fraMoney, 1);
                        }
                     }

                     if (Player.Ticket > ticket && GameCanvas.currentScreen == GameCanvas.gameScr) {
                        GameScreen.AA("-" + (Player.Ticket - ticket), GameScreen.player.x, GameScreen.player.y - GameScreen.player.hOne, (byte)10, AvMain.fraMoney, 2);
                        Interface_Game.BY = 40;
                        Interface_Game.BZ = 1;
                     }

                     GameScreen.player.Beri = beri;
                     GameScreen.player.Ruby = ruby;
                     Player.Ticket = ticket;
                     Player.MaxTicket = var1.reader().readShort();
                     if ((b = var1.reader().readByte()) != Player.RH) {
                        flag = true;
                     }

                     Player.RK = var1.reader().readByte();
                     Player.RH = b;
                     if ((b3 = var1.reader().readByte()) != Player.RG) {
                        flag = true;
                     }

                     Player.RG = b3;
                     Player.RJ = var1.reader().readByte();
                     if (flag) {
                        Interface_Game.BY = 40;
                        Interface_Game.BZ = 1;
                     }
                  } else if (typeInvenOrChest == 99) {
                     Player.RE = beri;
                     Player.RD = ruby;
                  }

                  if ((var20 = var1.reader().readInt()) != GameScreen.player.PD && GameCanvas.CJ != null && GameCanvas.currentScreen == GameCanvas.CJ) {
                     String var29;
                     if (GameScreen.player.PD > var20) {
                        var29 = "-" + (GameScreen.player.PD - var20);
                     } else {
                        var29 = "+" + (var20 - GameScreen.player.PD);
                     }

                     TabScreen.AA(var29, MainTab.AZ - 20, MainTab.BA + 36, (byte)10, AvMain.fraMoney, 7);
                  }

                  GameScreen.player.PD = var20;
                  int var34;
                  if ((var34 = var1.reader().readInt()) != GameScreen.player.PF && GameCanvas.currentScreen == Class_FD.AA) {
                     String var17;
                     if (GameScreen.player.PF > var34) {
                        var17 = "-" + (GameScreen.player.PF - var34);
                     } else {
                        var17 = "+" + (var34 - GameScreen.player.PF);
                     }

                     Class_FD.AA(var17, MainTab.AZ - 10, MainTab.BA + 36, (byte)10, AvMain.fraMoney, 8);
                  }

                  GameScreen.player.PF = var34;
                  if ((ruby = var1.reader().readInt()) != GameScreen.player.PG && GameCanvas.currentScreen == Class_FD.AA) {
                     String var21;
                     if (GameScreen.player.PG > ruby) {
                        var21 = "-" + (GameScreen.player.PG - ruby);
                     } else {
                        var21 = "+" + (ruby - GameScreen.player.PG);
                     }

                     (new StringBuffer("Add effect num diemNap ")).append(var21).toString();
                     TabScreen.AA(var21, MainTab.AZ - 20, MainTab.BA + 36, (byte)10, AvMain.fraMoney, 9);
                  }

                  GameScreen.player.PG = ruby;
               }
            }
         }

         TabScreen.AA = true;
         if (typeInvenOrChest == 100 && Player.isFullInven && Player.vecInventory.size() < Player.maxInventory) {
            Player.isFullInven = false;
         }

         if (MainTabShop.CA && (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr)) {
            MainItem.SortVecItem(var2);
            MainTabShop.CA = false;
            return;
         }
      } catch (Exception var15) {
         var15.printStackTrace();
      }

   }

   private static Potion readUpdatePotion(Message var0, boolean var1, byte var2) {
      Potion var3 = null;

      try {
         short var4 = var0.reader().readShort();
         short var7 = var0.reader().readShort();
         Potion var5 = null;
         if (var2 == 4) {
            var5 = Potion.AD(var4);
         }

         if (var2 == 8) {
            var5 = (Potion)MainItem.CC.get("" + var4);
         }

         var3 = null;
         if (var5.CR) {
            (var3 = new Potion(var2, var4, var5.idIcon, var5.name, var5.BO)).AA(var5.AF, var5.AJ, var5.BV, var5.CQ, var5.BU, var5.BQ, var5.BM);
            var3.numPotion = var7;
            var3.CG = var1;
            if (var2 == 8) {
               var3.info = var5.info;
            }

            if (!var3.AA(var5.AP)) {
               Potion.CP.addElement(var3);
            }
         } else {
            var3 = new Potion(var2, var4, var7, var1);
            Potion.AA.addElement(var3);
         }

         if (var4 == 232) {
            LuckyScreen.BL = true;
         }

         if (var4 == 441) {
            LuckyScreen.BL = true;
         }

         Hotkey.checkUpdatePotion(var3);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var3;
   }

   private static Class_DV GC(Message var0) {
      Class_DV var1 = null;

      try {
         byte var2 = var0.reader().readByte();
         String var3 = var0.reader().readUTF();
         byte var4 = var0.reader().readByte();
         short var5 = var0.reader().readShort();
         short var7 = var0.reader().readShort();
         var1 = new Class_DV(var2, var7, var5, var3, var4);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var1;
   }

   private static Class_DX AC(Message var0, byte var1) {
      Class_DX var2 = null;

      try {
         short var3 = var0.reader().readShort();
         String var4 = var0.reader().readUTF();
         var0.reader().readByte();
         short var5 = var0.reader().readShort();
         var0.reader().readShort();
         var2 = new Class_DX(var3, var5, var4, var1);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var2;
   }

   private static Class_DW GD(Message var0) {
      Class_DW var1 = null;

      try {
         short var2 = var0.reader().readShort();
         String var3 = var0.reader().readUTF();
         String var4 = var0.reader().readUTF();
         short var5 = var0.reader().readShort();
         short[] var6 = new short[var0.reader().readByte()];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = var0.reader().readShort();
         }

         (var1 = new Class_DW(var2, var5, var3, var4, var6)).CG = true;
         var1.AA(var1.info);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return var1;
   }

   private static MainMaterial readUpdateMaterial(Message var0, boolean var1) {
      MainMaterial var2 = null;

      try {
         byte var3 = var0.reader().readByte();
         short var6 = var0.reader().readShort();
         MainMaterial var4 = (MainMaterial)MainItem.hashMaterialTem.get("" + var3);
         (var2 = new MainMaterial((byte)7, var3, var4.name, var4.typeMaterial, (byte)var4.idIcon, var6, var4.AF, var4.AJ, var4.BO)).CG = var1;
         var2.AA(var4.info);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   private static Item GE(Message var0) {
      Item var1 = null;

      try {
         short var2 = var0.reader().readShort();
         String var3 = var0.reader().readUTF();
         byte var4 = var0.reader().readByte();
         byte var5 = var0.reader().readByte();
         short var6 = var0.reader().readShort();
         short var7 = var0.reader().readShort();
         byte var8 = var0.reader().readByte();
         byte var9 = var0.reader().readByte();
         byte var10 = var0.reader().readByte();
         byte var11 = var0.reader().readByte();
         byte var12 = var0.reader().readByte();
         int var13 = var0.reader().readInt();
         short var14 = var0.reader().readShort();
         byte var15 = var0.reader().readByte();
         byte var16 = var0.reader().readByte();
         byte var17;
         MainInfoItem[] var19 = new MainInfoItem[var17 = var0.reader().readByte()];

         int var20;
         byte var21;
         for(var20 = 0; var20 < var17; ++var20) {
            var21 = var0.reader().readByte();
            short var22 = var0.reader().readShort();
            var19[var20] = new MainInfoItem(var21, var22);
            var19[var20].AD = infoShow.HARDCODE_INFO_CO_BAN;
         }

         byte var28 = var0.reader().readByte();
         MainInfoItem[] var18 = new MainInfoItem[var17 + var28];

         int var24;
         for(var24 = 0; var24 < var17; ++var24) {
            var18[var24] = new MainInfoItem(var19[var24].AA, var19[var24].AE, var19[var24].AD);
         }

         byte var25;
         for(var24 = 0; var24 < var28; ++var24) {
            var25 = var0.reader().readByte();
            short var26 = var0.reader().readShort();
            var18[var24 + var17] = new MainInfoItem(var25, var26);
            if (MainItem.BZ[var25].AB != 0) {
               var18[var24 + var17].AD = MainItem.BZ[var25].AB;
            } else {
               var18[var24 + var17].AD = 4;
            }
         }

         var21 = var0.reader().readByte();
         short[] var27 = new short[var25 = var0.reader().readByte()];

         for(var20 = 0; var20 < var25; ++var20) {
            var27[var20] = var0.reader().readShort();
         }

         (var1 = new Item((byte)3, var2, var6, var3, var10)).setDataItem(var7, var4, var9, var13, var5, var8, var21, var27, var14, var15, var16);
         var1.AR = var2;
         var1.AY = var11;
         if (var1.AY == 1) {
            var1.BO = 1;
         }

         var1.AZ = var12;
         var1.setInfoItem(var18, var17);
         return var1;
      } catch (Exception var23) {
         var23.printStackTrace();
         return var1;
      }
   }

   private static Quest_Potion GF(Message var0) {
      Quest_Potion var1 = null;

      try {
         short var2 = var0.reader().readShort();
         String var3 = var0.reader().readUTF();
         short var5 = var0.reader().readShort();
         (var1 = new Quest_Potion((byte)5, var2, var3)).numPotion = var5;
         var1.AA(T.CG);
      } catch (Exception var4) {
      }

      return var1;
   }

   public final void update_Inventory(Message var1) {
      this.update_InVen_Or_Chest(var1, Player.vecInventory, (byte)100);
   }

   public final void update_Chest(Message var1) {
      this.update_InVen_Or_Chest(var1, Player.vecChest, (byte)99);
   }

   private void update_Inven_Clan(Message var1) {
      this.update_InVen_Or_Chest(var1, Player.AW, (byte)110);
   }

   public static void use_Potion(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         MainItem var2;
         if ((var2 = MainItem.getItemVec((byte)4, var1, Player.vecInventory)) != null) {
            short var5 = var0.reader().readShort();
            var2.numPotion = var5;
            if (var2.BQ == 1 || var2.BQ == 2) {
               Interface_Game var10000 = GameScreen.interfaceGame;
               Interface_Game.AA(var2);
            }

            if (var5 <= 0) {
               GameScreen.player.SI = null;
               GameScreen.player.SJ = null;
               Player.vecInventory.removeElement(var2);
               if (Player.isFullInven && Player.vecInventory.size() < Player.maxInventory) {
                  Player.isFullInven = false;
               }

               for(int var6 = 0; var6 < Player.hotkeyPlayer.length; ++var6) {
                  for(int var7 = 0; var7 < Player.hotkeyPlayer[var6].length; ++var7) {
                     Hotkey var3;
                     if ((var3 = Player.hotkeyPlayer[var6][var7]).itemcur != null && var3.itemcur.ID == var1) {
                        var3.itemcur = null;
                     }
                  }
               }

               if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
                  GameCanvas.tabInven.AP();
                  return;
               }
            }
         }
      } catch (Exception var4) {
      }

   }

   public final void BA(Message var1) {
      try {
         short var2 = var1.reader().readShort();
         byte var3 = var1.reader().readByte();
         MainObject var4;
         if ((var4 = MainObject.get_Object((int)var2, (byte)var3)) == null || var4.returnAction()) {
            return;
         }

         var4.maxHp = var1.reader().readInt();
         int var5 = var1.reader().readInt();
         int var6;
         if ((var6 = var1.reader().readInt()) != 0) {
            String var7 = "" + var6;
            if (var6 > 0) {
               var7 = "+" + var6;
            }

            if (var4 == GameScreen.player) {
               GameScreen.addEffectNum(var7, var4.x - 12, var4.y - var4.hOne / 4 * 3 - this.AS, (byte)3);
            }
         }

         var4.Hp = var5;
         if (var4.Action == 4 && var4.Hp > 0) {
            var4.Reveive();
         }

         int var11;
         if (var3 == 0) {
            for(var11 = 0; var11 < Player.vecParty.size(); ++var11) {
               Class_DS var10;
               if ((var10 = (Class_DS)Player.vecParty.elementAt(var11)).AG == var2 && var3 == 0) {
                  var10.AA(var5, var4.maxHp, var4.Lv);
               }
            }
         }

         if (var3 != 1) {
            var4.maxMp = var1.reader().readInt();
            var11 = var1.reader().readInt();
            if ((var6 = var1.reader().readInt()) != 0) {
               String var9 = "" + var6;
               if (var6 > 0) {
                  var9 = "+" + var6;
               }

               if (var4 == GameScreen.player) {
                  GameScreen.addEffectNum(var9, var4.x + 12, var4.y - var4.hOne / 4 * 3 - this.AS, (byte)4);
               }
            }

            var4.Mp = var11;
         }

         this.AS += 10;
         if (this.AS > 20) {
            this.AS = 0;
            return;
         }
      } catch (Exception var8) {
      }

   }

   public static void update_PK(Message var0) {
      try {
         short id = var0.reader().readShort();
         byte b = var0.reader().readByte();
         byte typePirate = var0.reader().readByte();
         byte isDonotShowHat = var0.reader().readByte();
         MainObject mainObject;
         if ((mainObject = MainObject.get_Object((int)id, (byte)0)) != null && !mainObject.returnAction()) {
            byte var5 = mainObject.typePK;
            mainObject.typePK = b;
            mainObject.typePirate = typePirate;
            mainObject.isDonotShowHat = isDonotShowHat;
            if (mainObject == GameScreen.player) {
               GameCanvas.gameScr.AR.caption = T.AY;
               if (b == 0) {
                  GameCanvas.gameScr.AR.caption = T.BB;
               }

               if (LoadMap.specMap == 1) {
                  GlobalService.getInstance().Set_PK((byte)b, (byte)1);
               }

               if (var5 == 0) {
                  Interface_Game.addInfoPlayerNormal(T.tatdosat, mFont.tahoma_7_yellow);
               } else if (b == 0) {
                  Interface_Game.addInfoPlayerNormal(T.batdosat, mFont.tahoma_7_yellow);
               }
            }

            var0.reader().readShort();
            b = var0.reader().readByte();
            mainObject.isDonotShowWeaponF = b;
            byte var7 = var0.reader().readByte();
            mainObject.MT = var7;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public static void BC(Message var0) {
      try {
         byte var1;
         Player.AZ = new short[var1 = var0.reader().readByte()];

         for(int var2 = 0; var2 < var1; ++var2) {
            Player.AZ[var2] = var0.reader().readShort();
         }

      } catch (Exception var3) {
      }
   }

   public final void Effect_Obj(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         short var3 = var1.reader().readShort();
         byte var4 = var1.reader().readByte();
         short var5 = var1.reader().readShort();
         MainObject var6;
         if (((var6 = MainObject.get_Object((int)var3, (byte)var4)) == null || var6.returnAction()) && var2 < 16 && var2 > 18) {
            return;
         }

         short var8;
         short var9;
         byte var10;
         int var11;
         byte var12;
         MainObject var17;
         byte var19;
         switch(var2) {
         case 0:
            if (var6 == GameScreen.player) {
               if (GameScreen.CV == 13) {
                  GameScreen.AB((int)13, (int)0);
               } else if (GameScreen.player.Lv == 20) {
                  GameScreen.AB((int)19, (int)0);
               }

               for(int var14 = 0; var14 < Player.vecInventory.size(); ++var14) {
                  MainItem var20;
                  if ((var20 = (MainItem)Player.vecInventory.elementAt(var14)).typeObject == 3 && var20.Lv_RQ == GameScreen.player.Lv) {
                     for(int var18 = 0; var18 < var20.vecInfo.size(); ++var18) {
                        infoShow var21;
                        if ((var21 = (infoShow)var20.vecInfo.elementAt(var18)).AC == infoShow.AF) {
                           var21.AB = 4;
                        }
                     }
                  }
               }
            }

            GameScreen.addEffectEnd_ObjTo((short)28, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)var6.Dir, var6);
            return;
         case 1:
            if (var6 == GameScreen.player && (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr)) {
               GameCanvas.gameScr.Show();
            }

            var6.addEffBuff((byte)3, (short)165, (short)0);
            return;
         case 2:
            var9 = (short)(var1.reader().readShort() * 24);
            var8 = (short)(var1.reader().readShort() * 24);
            var6.x = var9;
            var6.y = var8;
            var6.addEffBuff((byte)3, (short)17, (short)var5);
            return;
         case 3:
            if (var6 != null) {
               GameScreen.addEffectEnd_ObjTo((short)46, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)var6.Dir, var6);
               return;
            }
            break;
         case 4:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            GameScreen.AA((short)96, 0, var6.x, var6.y, var9, var12, (byte)var6.Dir, var6, var5);
            return;
         case 5:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            GameScreen.AA((short)97, 0, var6.x, var6.y, var9, var12, (byte)var6.Dir, var6, var5);
            return;
         case 6:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            MainObject var13;
            if ((var13 = MainObject.get_Object((int)var9, (byte)var12)) != null) {
               GameScreen.addEffectEnd_ObjTo((short)101, 0, var13.x, var13.y, (short)var13.ID, (byte)var13.typeObject, (byte)0, var6);
            }

            var6.BW();
            return;
         case 7:
            GameScreen.addEffectEnd_ObjTo((short)101, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 8:
            var10 = var1.reader().readByte();
            GameScreen.addEffectEnd_ObjTo((short)104, var10, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 9:
            var10 = var1.reader().readByte();
            GameScreen.addEffectEnd_ObjTo((short)105, var10, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 10:
            GameScreen.addEffectEnd_ObjTo((short)106, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 11:
            GameScreen.addEffectEnd((short)115, 0, var6.x, var6.y, 0, (byte)0, var6);
            return;
         case 12:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            var8 = var1.reader().readShort();
            var19 = 0;
            if (var8 == 180) {
               var19 = 0;
            } else if (var8 == 181) {
               var19 = 1;
            } else if (var8 == 182) {
               var19 = 2;
            } else if (var8 == 193) {
               var19 = 3;
            } else if (var8 == 233) {
               var19 = 4;
            } else if (var8 == 234) {
               var19 = 5;
            } else if (var8 == 235) {
               var19 = 6;
            } else if (var8 == 577) {
               var19 = 7;
            } else if (var8 == 611) {
               var19 = 24;
            }

            GameScreen.addEffectEnd_ObjTo((short)127, var19, var6.x, var6.y, (short)var9, (byte)var12, (byte)0, var6);
            return;
         case 13:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            var8 = var1.reader().readShort();
            var19 = 10;
            if (var8 == 180) {
               var19 = 10;
            } else if (var8 == 181) {
               var19 = 11;
            } else if (var8 == 182) {
               var19 = 12;
            } else if (var8 == 193) {
               var19 = 13;
            } else if (var8 == 233) {
               var19 = 14;
            } else if (var8 == 234) {
               var19 = 15;
            } else if (var8 == 235) {
               var19 = 16;
            } else if (var8 == 577) {
               var19 = 17;
            } else if (var8 == 611) {
               var19 = 24;
            }

            GameScreen.addEffectEnd_ObjTo((short)127, var19, var6.x, var6.y, (short)var9, (byte)var12, (byte)0, var6);
            return;
         case 14:
            GameScreen.AA((short)130, 0, var6.x, var6.y, var3, var4, (byte)0, var6, var5);
            return;
         case 15:
            GameScreen.AA((short)131, 0, var6.x, var6.y, var3, var4, (byte)0, var6, var5);
            return;
         case 16:
            var8 = var1.reader().readShort();

            for(var11 = 0; var11 < GameScreen.vecPlayers.size(); ++var11) {
               if ((var17 = (MainObject)GameScreen.vecPlayers.elementAt(var11)).typeObject == 0 && (var17.clan == null || var8 != var17.clan.ID)) {
                  var17.addEffSpec((short)1, (short)var5);
                  GameScreen.AA((short)149, 0, var17.x, var17.y, var17.ID, var17.typeObject, (byte)0, var17, var5);
               }
            }

            return;
         case 17:
            var8 = var1.reader().readShort();

            for(var11 = 0; var11 < GameScreen.vecPlayers.size(); ++var11) {
               if ((var17 = (MainObject)GameScreen.vecPlayers.elementAt(var11)).typeObject == 0 && (var17.clan == null || var17.clan.ID != var8)) {
                  GameScreen.AA((short)151, 0, var17.x, var17.y, var17.ID, var17.typeObject, (byte)0, var17, var5);
                  GameScreen.AA((short)150, 0, var17.x, var17.y, var17.ID, var17.typeObject, (byte)0, var17, var5 / 10);
               }
            }

            return;
         case 18:
            var8 = var1.reader().readShort();

            for(var11 = 0; var11 < GameScreen.vecPlayers.size(); ++var11) {
               if ((var17 = (MainObject)GameScreen.vecPlayers.elementAt(var11)).typeObject == 0 && var17.clan != null && var17.clan.ID == var8) {
                  GameScreen.AA((short)151, 1, var17.x, var17.y, var17.ID, var17.typeObject, (byte)0, var17, var5);
                  var17.addEffSpec((short)11, (short)var5);
               }
            }

            return;
         case 19:
            GameScreen.addEffectEnd((short)80, 0, var6.x, var6.y - var6.hOne / 2, (byte)0, (MainObject)null);
            GameScreen.AA((short)152, 0, var6.x, var6.y, var3, var4, (byte)0, var6, var5 / 10);
            return;
         case 20:
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr) {
               GameCanvas.gameScr.Show();
            }

            GameScreen.addEffectEnd_ObjTo((short)153, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 21:
            GameScreen.AA((short)156, 0, var6.x, var6.y, var6.ID, var6.typeObject, (byte)0, var6, var5);
            return;
         case 22:
            GameScreen.addEffectEnd_ObjTo((short)159, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)var6.Dir, var6);
            return;
         case 23:
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr) {
               GameCanvas.gameScr.Show();
            }

            GameScreen.addEffectEnd_ObjTo((short)158, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 24:
            GameScreen.addEffectEnd_ObjTo((short)162, 0, var6.x, var6.y - 2, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 25:
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr) {
               GameCanvas.gameScr.Show();
            }

            GameScreen.addEffectEnd_ObjTo((short)178, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)0, var6);
            return;
         case 26:
            if (var6 != null) {
               var9 = (short)(var5 * 100);
               GameScreen.AA(var6, (short)2060, var9);
               return;
            }
            break;
         case 27:
            var9 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            short var16 = var1.reader().readShort();
            GameScreen.AA((short)179, 10, var6.x, var6.y, var9, var12, var16, (byte)0, var6);
            GameScreen.addEffectEnd_ObjTo((short)159, 0, var6.x, var6.y, (short)var6.ID, (byte)var6.typeObject, (byte)var6.Dir, var6);
            return;
         case 28:
            if (var6 != null) {
               var9 = var1.reader().readShort();

               for(var5 = 0; var5 < var9; ++var5) {
                  var3 = var1.reader().readShort();
                  int var15 = var1.reader().readInt();
                  var6.AA(var3, var15);
               }

               return;
            }
            break;
         case 29:
            if (var6 == GameScreen.player && (GameCanvas.currentScreen == GameCanvas.tabAllScr || GameCanvas.currentScreen == GameCanvas.tabShopScr)) {
               GameCanvas.gameScr.Show();
            }

            GameScreen.AA(new MainSkill((short)402, (short)0), var6);
         default:
            return;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void BE(Message var0) {
      GameScreen.AI();

      try {
         byte var1 = var0.reader().readByte();
         boolean var2 = false;

         for(int var3 = 0; var3 < var1; ++var3) {
            short var4 = var0.reader().readShort();
            String var5 = var0.reader().readUTF();
            String var6 = var0.reader().readUTF();
            String var7 = var0.reader().readUTF();
            short var8 = var0.reader().readShort();
            short var9 = var0.reader().readShort();
            byte var10 = var0.reader().readByte();
            byte var11 = var0.reader().readByte();
            byte var12 = var0.reader().readByte();
            byte var13 = var0.reader().readByte();
            byte var14 = var0.reader().readByte();
            Class_EP var16;
            (var16 = new Class_EP(var5, var6, var4, var8, var9, var12, var13, var14)).AA = var10;
            var16.BA = var7;
            var16.MP = var11;
            byte var17;
            if (var14 == 0) {
               var17 = var0.reader().readByte();
               byte var19 = var0.reader().readByte();
               var16.AA(var17, var19);
            } else {
               var16.sethead(var0.reader().readShort());
               var16.sethair(var0.reader().readShort());
               var16.hOne = 52;
               var16.wOne = 26;
               if (LoadMap.specMap == 4) {
                  var16.wOne = 100;
               }

               short[] var18 = new short[var17 = var0.reader().readByte()];

               int var20;
               for(var20 = 0; var20 < var18.length; ++var20) {
                  var18[var20] = -1;
               }

               for(var20 = 0; var20 < var17; ++var20) {
                  if (var0.reader().readByte() == 1) {
                     var18[var20] = var0.reader().readShort();
                  }
               }

               var16.AA(var18);
            }

            var16.isRemove = false;
            var16.isInfo = true;
            GameScreen.addPlayer((MainObject)var16);
            var16.BN();
            if (var10 == 98 && !var2) {
               GlobalService.getInstance().AN();
               var2 = true;
            }
         }

      } catch (Exception var15) {
         var15.printStackTrace();
      }
   }

   public static void BF(Message var0) {
      try {
         byte var1;
         short var2;
         short var5;
         mVector var17;
         String var25;
         if ((var1 = var0.reader().readByte()) == 0) {
            String var13 = var0.reader().readUTF();
            var2 = var0.reader().readShort();
            var17 = new mVector();

            for(int var20 = 0; var20 < var2; ++var20) {
               var5 = var0.reader().readShort();
               var25 = var0.reader().readUTF();
               String var29 = var0.reader().readUTF();
               short var32 = var0.reader().readShort();
               String var9 = var0.reader().readUTF();
               Potion var24 = new Potion(var5, var32, var25, var29, (short)0);
               byte var30 = var0.reader().readByte();

               for(int var31 = 0; var31 < var30; ++var31) {
                  byte var33 = var0.reader().readByte();
                  var32 = var0.reader().readShort();
                  var24.addInfo(var33, var32, (byte)infoShow.HARDCODE_INFO_CO_BAN, (byte)1);
               }

               if (var9 != "") {
                  var24.addInfo((String)var9, (byte)5);
               }

               if (var24.AF > 0) {
                  var24.addInfoFrist(T.BG + " " + var24.AF + " " + T.bery, (byte)5);
               } else if (var24.AJ > 0) {
                  var24.addInfoFrist(T.BG + " " + var24.AJ + " " + T.EW, (byte)5);
               }

               var17.addElement(var24);
            }

            GameCanvas.tabShopScr = new TabScreen(MainTab.xTab, (byte)0);
            mVector var26 = new mVector();
            GameCanvas.tabShopScr.isShopClan = true;
            GameCanvas.tabInven.AA((byte)1);
            TabShop var22;
            (var22 = new TabShop(var13, var17, (byte)107, MainTab.xTab)).CL = false;
            var26.addElement(var22);
            GameCanvas.tabShopScr.addVecTab(var26);
            GameCanvas.tabShopScr.idSelect = 0;
            GameCanvas.tabShopScr.Show((MainScreen)GameCanvas.gameScr);
            GameCanvas.tabShopScr.typeCurrent = 1;
            GameCanvas.tabShopScr.setTabSelect();
            return;
         }

         short var12;
         if (var1 == 1) {
            var12 = var0.reader().readShort();
            mVector var14 = new mVector();

            for(int var16 = 0; var16 < var12; ++var16) {
               short var19 = var0.reader().readShort();
               String var21 = var0.reader().readUTF();
               var25 = var0.reader().readUTF();
               short var7 = var0.reader().readShort();
               Class_DY var8 = new Class_DY(var19, var7, var21, var25);
               if (var0.reader().readByte() == 1) {
                  var8.addInfoFrist(T.daTrangBi, (byte)1);
                  var8.colorName = 1;
               }

               byte var23 = var0.reader().readByte();

               for(int var27 = 0; var27 < var23; ++var27) {
                  byte var28 = var0.reader().readByte();
                  short var10 = var0.reader().readShort();
                  var8.addInfo(var28, var10, (byte)infoShow.HARDCODE_INFO_CO_BAN, (byte)1);
               }

               var14.addElement(var8);
            }

            GameCanvas.tabShopScr = new TabScreen(MainTab.xTab, (byte)0);
            var17 = new mVector();
            GameCanvas.tabShopScr.isShopClan = true;
            (GameCanvas.tabInvenClan = new TabInventory(T.UT, var14, (byte)6, MainTab.xTab)).initCmd();
            var17.addElement(GameCanvas.tabInvenClan);
            GameCanvas.tabShopScr.addVecTab(var17);
            GameCanvas.tabShopScr.idSelect = 0;
            GameCanvas.tabShopScr.Show((MainScreen)GameCanvas.ClanScr);
            GameCanvas.tabShopScr.typeCurrent = 1;
            GameCanvas.tabShopScr.setTabSelect();
            return;
         }

         if (var1 == 2) {
            var12 = var0.reader().readShort();
            GameCanvas.tabInvenClan.AA(var12);
            return;
         }

         if (var1 == 3) {
            if ((var1 = var0.reader().readByte()) == 0) {
               var2 = var0.reader().readShort();
               short var15 = var0.reader().readShort();
               byte var18 = var0.reader().readByte();
               var5 = var0.reader().readShort();
               Potion var6;
               (var6 = new Potion(var18, var2, var15, false)).idIcon = var5;
               (HuyHieuClanScreen.instance = new HuyHieuClanScreen()).potionQuay = var6;
               HuyHieuClanScreen.instance.Show((MainScreen)GameCanvas.gameScr);
               return;
            }

            if (var1 == 1) {
               Interface_Game.BN = false;
               HuyHieuClanScreen.instance.potionQuay.numPotion = var0.reader().readShort();
               float var10000;
               if ((var2 = var0.reader().readShort()) == -1) {
                  HuyHieuClanScreen.instance.isThanhCong = false;
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  HuyHieuClanScreen.instance.AY = 1;
                  HuyHieuClanScreen.instance.AW = 0;
                  return;
               }

               byte var3 = var0.reader().readByte();
               Potion var4 = new Potion(var3, (short)-1, var2, "", (byte)0);
               HuyHieuClanScreen.instance.potionNhan = var4;
               HuyHieuClanScreen.instance.isThanhCong = true;
               var10000 = mSound.volumeSound;
               mSound.playSound();
               HuyHieuClanScreen.instance.AY = 1;
               HuyHieuClanScreen.instance.AW = 0;
               return;
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public final void Shop_NPC(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         String var3 = var1.reader().readUTF();
         byte var4 = var1.reader().readByte();
         short var5 = var1.reader().readShort();
         mVector var6 = new mVector();
         int var7;
         Potion var8;
         if (var4 != 4 && var4 != 8) {
            int var10;
            if (var4 == 3) {
               for(var7 = 0; var7 < var5; ++var7) {
                  Item var25 = GE(var1);
                  byte var20 = var1.reader().readByte();
                  var10 = var1.reader().readInt();
                  if (var25 != null) {
                     var25.AF = var10;
                     var25.addInfoFrist(T.BG + " " + var25.AF + " " + (var20 == 0 ? T.bery : T.EW), (byte)5);
                     var6.addElement(var25);
                  }
               }
            } else if (var4 == 7) {
               for(var7 = 0; var7 < var5; ++var7) {
                  MainMaterial var23;
                  if ((var23 = readUpdateMaterial(var1, true)) != null) {
                     if (var23.AF > 0) {
                        var23.addInfoFrist(T.BG + " " + var23.AF + " " + T.bery, (byte)5);
                     } else {
                        var23.addInfoFrist(T.BG + " " + var23.AJ + " " + T.EW, (byte)5);
                     }

                     var6.addElement(var23);
                  }
               }
            } else if (var4 == 102) {
               var7 = 0;

               while(true) {
                  if (var7 >= var5) {
                     GlobalService.getInstance().AI();
                     break;
                  }

                  Class_DV var21;
                  (var21 = GC(var1)).AF = var1.reader().readInt();
                  var21.AJ = var1.reader().readShort();
                  if (var21 != null) {
                     if (var21.AF > 0) {
                        var21.addInfoFrist(T.BG + " " + var21.AF + " " + T.bery, (byte)5);
                     } else if (var21.AJ > 0) {
                        var21.addInfoFrist(T.BG + " " + var21.AJ + " " + T.EW, (byte)5);
                     } else {
                        boolean var19 = false;
                        if (GameScreen.player.BO != null) {
                           for(var10 = 0; var10 < GameScreen.player.BO.length; ++var10) {
                              if (var10 == var21.AW && var21.AN == GameScreen.player.BO[var10]) {
                                 var21.addInfoFrist(T.daTrangBi, (byte)4);
                                 var21.colorName = 1;
                                 var19 = true;
                              }
                           }
                        }

                        if (!var19) {
                           var21.addInfoFrist(T.IV, (byte)1);
                           var21.colorName = 1;
                        }
                     }

                     var6.addElement(var21);
                  }

                  ++var7;
               }
            } else {
               short var14;
               if (var4 != 103 && var4 != 108) {
                  if (var4 != 105) {
                     if (var4 == 107) {
                        for(var7 = 0; var7 < var5; ++var7) {
                           var14 = var1.reader().readShort();
                           short var17 = var1.reader().readShort();
                           String var22 = var1.reader().readUTF();
                           String var11 = var1.reader().readUTF();
                           short var12 = var1.reader().readShort();
                           if ((var8 = new Potion(var14, var17, var22, var11, var12)).AF > 0) {
                              var8.addInfoFrist(T.BG + " " + var8.AF + " " + T.bery, (byte)5);
                           } else if (var8.AJ > 0) {
                              var8.addInfoFrist(T.BG + " " + var8.AJ + " " + T.EW, (byte)5);
                           }

                           var6.addElement(var8);
                        }
                     } else if (var4 == 11) {
                        for(var7 = 0; var7 < var5; ++var7) {
                           MainItem var18;
                           (var18 = new MainItem()).ID = var1.reader().readShort();
                           var18.typeObject = var1.reader().readByte();
                           var18.namepaint = var1.reader().readUTF();
                           var18.idIcon = var1.reader().readShort();
                           String var16 = var1.reader().readUTF();
                           var18.CG = true;
                           var18.AA(var16);
                           var6.addElement(var18);
                        }
                     }
                  } else {
                     for(var7 = 0; var7 < var5; ++var7) {
                        Class_DW var15;
                        (var15 = GD(var1)).AF = var1.reader().readInt();
                        var15.AJ = var1.reader().readShort();
                        var15.LvUpgrade = var1.reader().readByte();
                        if (var15 != null) {
                           if (var15.AF > 0) {
                              var15.addInfoFrist(T.BG + " " + var15.AF + " " + T.bery, (byte)5);
                           } else if (var15.AJ > 0) {
                              var15.addInfoFrist(T.BG + " " + var15.AJ + " " + T.EW, (byte)5);
                           } else if (var15.AF == -2) {
                              var15.addInfoFrist(T.QK, (byte)5);
                           } else if (var15.AF == -1) {
                              var15.addInfoFrist(T.PJ, (byte)6);
                              var15.colorName = 6;
                           } else if (var15.ID == Player.idFashion && var2 != 114) {
                              var15.addInfoFrist(T.daTrangBi, (byte)4);
                              var15.colorName = 4;
                           } else {
                              var15.addInfoFrist(T.IV, (byte)1);
                              var15.colorName = 1;
                           }

                           var6.addElement(var15);
                        }
                     }
                  }
               } else {
                  for(var7 = 0; var7 < var5; ++var7) {
                     var14 = GameScreen.player.BW;
                     if (var4 == 108) {
                        var14 = GameScreen.player.BS;
                     }

                     Class_DX var9 = AC(var1, var4);
                     if (MainObject.PA != null) {
                        for(var10 = 0; var10 < MainObject.PA.length; ++var10) {
                           if (var9.ID == MainObject.PA[var10].id) {
                              var9.AA(MainObject.PA[var10].AC);
                              break;
                           }
                        }
                     }

                     var9.AF = var1.reader().readInt();
                     var9.AJ = var1.reader().readShort();
                     if (var9 != null) {
                        if (var9.AF > 0) {
                           var9.addInfoFrist(T.BG + " " + var9.AF + " " + T.bery, (byte)5);
                        } else if (var9.AJ > 0) {
                           var9.addInfoFrist(T.BG + " " + var9.AJ + " " + T.EW, (byte)5);
                        } else if (var9.idIcon == var14) {
                           var9.addInfoFrist(T.daTrangBi, (byte)4);
                           var9.colorName = 4;
                        } else {
                           var9.addInfoFrist(T.IV, (byte)1);
                           var9.colorName = 1;
                        }

                        var6.addElement(var9);
                     }
                  }
               }
            }
         } else {
            for(var7 = 0; var7 < var5; ++var7) {
               if ((var8 = readUpdatePotion(var1, true, var4)) != null) {
                  if (var8.AF > 0) {
                     var8.addInfoFrist(T.BG + " " + var8.AF + " " + T.bery, (byte)5);
                  } else if (var8.AJ > 0) {
                     var8.addInfoFrist(T.BG + " " + var8.AJ + " " + T.EW, (byte)5);
                  }

                  var6.addElement(var8);
               }
            }
         }

         TabShop var27 = null;
         GameCanvas.tabShopScr = new TabScreen(MainTab.xTab, (byte)0);
         mVector var26 = new mVector();
         if (var2 == 110) {
            GameCanvas.tabShopScr.isShopClan = true;
            (GameCanvas.tabInvenClan = new TabInventory(T.tabInven, Player.AW, (byte)4, MainTab.xTab)).initCmd();
            var26.addElement(GameCanvas.tabInvenClan);
         } else if (var4 != 107) {
            var26.addElement(GameCanvas.tabInven);
         } else {
            GameCanvas.tabShopScr.isShopClan = true;
         }

         if (var2 == 99) {
            GameCanvas.tabInven.AA((byte)2);
            TabChest var24;
            (var24 = new TabChest(var3, Player.vecChest, MainTab.xTab)).AQ();
            var26.addElement(var24);
         } else {
            GameCanvas.tabInven.AA((byte)1);
            var27 = new TabShop(var3, var6, var2, MainTab.xTab);
            var26.addElement(var27);
         }

         GameCanvas.tabShopScr.addVecTab(var26);
         if (var4 != 107) {
            GameCanvas.tabShopScr.idSelect = 1;
         } else {
            GameCanvas.tabShopScr.idSelect = 0;
         }

         GameCanvas.tabShopScr.Show((MainScreen)GameCanvas.gameScr);
         if (var2 == 99) {
            GameCanvas.tabShopScr.typeCurrent = 0;
         } else {
            GameCanvas.tabShopScr.typeCurrent = 1;
         }

         GameCanvas.tabShopScr.setTabSelect();
         if (var2 == 101 && var27 != null) {
            var27.AB();
            return;
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public final void Dynamic_Menu(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         short var3 = var1.reader().readShort();
         byte var4 = var1.reader().readByte();
         String var5 = var1.reader().readUTF();
         byte var6 = var1.reader().readByte();
         mVector var7 = new mVector();

         for(int var8 = 0; var8 < var6; ++var8) {
            iCommand var9 = new iCommand(var1.reader().readUTF(), (IAction)null);
            byte var11;
            if (var2 == 1) {
               byte var10 = var1.reader().readByte();
               var11 = var1.reader().readByte();
               iCommand var12 = var9;
               if (var2 == 1 && var10 >= 0) {
                  var9.setFraCaption((FrameImage)AvMain.fraCheck, 1, var10, (int)0);
               }

               var9 = var9;
               var12.AA(AvMain.AC(var11));
            } else {
               short var15;
               MainItem var17;
               if (var2 == 3) {
                  var15 = var1.reader().readShort();
                  if ((var11 = var1.reader().readByte()) == 7) {
                     MainMaterial var16;
                     if ((var16 = (MainMaterial)MainItem.hashMaterialTem.get("" + var15)) != null) {
                        var17 = new MainItem(var11, var16.idIcon, (short)0);
                        var9.AA(var17);
                     }
                  } else {
                     Potion var18;
                     if (var11 == 4 && (var18 = Potion.AD(var15)) != null && var18.CR) {
                        var17 = new MainItem(var11, var18.idIcon, (short)0);
                        var9.AA(var17);
                     }
                  }
               } else {
                  MainItem var19;
                  if (var2 == 4) {
                     var15 = var1.reader().readShort();
                     var19 = new MainItem((byte)104, var15, (short)0);
                     var9.AA(var19);
                  } else if (var2 == 5) {
                     var15 = var1.reader().readShort();
                     var19 = new MainItem((byte)100, (short)var8, var15, (short)0, (byte)0, (byte)0);
                     var9.AA(var19);
                  } else if (var2 == 6) {
                     var15 = var1.reader().readShort();
                     var11 = var1.reader().readByte();
                     var17 = new MainItem((byte)100, (short)var8, var15, (short)0, (byte)0, (byte)0);
                     var9.AA(var17);
                     var9.AA(AvMain.AC(var11));
                  }
               }
            }

            var7.addElement(var9);
         }

         if (var2 == 2) {
            GameCanvas.menu.AA(var7, "", var3, (byte)2, false, 0, true);
            GameCanvas.menu.AO = var4;
         } else {
            byte var14 = 2;
            if (var2 == 1) {
               var14 = 4;
            }

            GameCanvas.menu.AA(var7, var14, var4, var3, var5);
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }
   }

   public final void List_Quest(Message var1) {
      try {
         mVector var2 = new mVector();
         byte var3;
         MainQuest var5;
         int var8;
         MainQuest var10;
         if ((var3 = var1.reader().readByte()) == 0) {
            var3 = var1.reader().readByte();

            for(int var4 = 0; var4 < var3; ++var4) {
               var5 = GJ(var1);
               var2.addElement(var5);
            }

            byte var9 = var1.reader().readByte();

            for(int var11 = 0; var11 < var9; ++var11) {
               MainQuest var7 = GI(var1);
               var2.addElement(var7);
            }

            byte var12 = var1.reader().readByte();

            for(var8 = 0; var8 < var12; ++var8) {
               var10 = GH(var1);
               var2.addElement(var10);
            }

            Player.QI = var2;
         } else if (var3 == 1) {
            var3 = var1.reader().readByte();
            var10 = null;
            if (var3 == 0) {
               if ((var10 = GJ(var1)).AA != 2) {
                  Player.QW = var10;
               }
            } else if (var3 == 1) {
               var10 = GI(var1);
               if (GameScreen.CV == 14) {
                  GameScreen.AB((int)14, (int)1);
               }
            } else if (var3 == 2) {
               var10 = GH(var1);
               if (GameScreen.CV == 14) {
                  GameScreen.AB((int)14, (int)1);
               }
            }

            if ((var5 = MainQuest.AA(var10.AE)) != null) {
               Player.QI.removeElement(var5);
            }

            if (var10 != null) {
               Player.QI.addElement(var10);
            }
         } else if (var3 == 2) {
            if ((var10 = MainQuest.AA(var1.reader().readShort())) != null) {
               Player.QI.removeElement(var10);
            }
         } else if (var3 == 5 && (var10 = MainQuest.AA(var1.reader().readShort())) != null) {
            GameCanvas.end_Dialog();
            MsgDialog var13;
            (var13 = new MsgDialog()).AA(var10, true);
            GameCanvas.Start_Current_Dialog((MainDialog)var13);
         }

         for(var8 = 0; var8 < GameScreen.vecPlayers.size(); ++var8) {
            MainObject var14;
            if ((var14 = (MainObject)GameScreen.vecPlayers.elementAt(var8)).typeObject == 2) {
               var14.BN();
            }
         }

         TabQuest.BO = false;

         for(var8 = 0; var8 < Player.QI.size(); ++var8) {
            if ((var10 = (MainQuest)Player.QI.elementAt(var8)).AB == 0 || var10.AB == 2) {
               TabQuest.BO = true;
               return;
            }
         }

      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   private static MainQuest GH(Message var0) {
      MainQuest var1 = null;

      try {
         short var2 = var0.reader().readShort();
         (var1 = new MainQuest(var2)).AB = 2;
         var1.AA = var0.reader().readByte();
         var0.reader().readByte();
         var1.AH = var0.reader().readUTF();
         var1.AC = var0.reader().readShort();
         var1.AD = var0.reader().readShort();
         var1.AI = var0.reader().readUTF();
         byte var4 = var0.reader().readByte();
         Class_CP var5 = new Class_CP(var4);
         var1.AM.addElement(var5);
         var1.AK = var0.reader().readUTF();
         var1.AL = var0.reader().readUTF();
         var1.AJ = var0.reader().readUTF();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   private static MainQuest GI(Message var0) {
      MainQuest var1 = null;

      try {
         short var2 = var0.reader().readShort();
         (var1 = new MainQuest(var2)).AB = 1;
         var1.AA = var0.reader().readByte();
         var0.reader().readByte();
         var1.AH = var0.reader().readUTF();
         var1.AC = var0.reader().readShort();
         var1.AJ = var0.reader().readUTF();
         var1.AK = var0.reader().readUTF();
         byte var7 = var0.reader().readByte();

         for(int var3 = 0; var3 < var7; ++var3) {
            byte var4 = var0.reader().readByte();
            Class_CP var5 = new Class_CP(var4);
            if (var4 == 2 || var4 == 1) {
               var5.AA(var0.reader().readShort(), var0.reader().readShort(), var0.reader().readShort());
            }

            var1.AM.addElement(var5);
         }

         var1.AL = var0.reader().readUTF();
         var1.AF = var0.reader().readShort();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var1;
   }

   private static MainQuest GJ(Message var0) {
      MainQuest var1 = null;

      try {
         short var2 = var0.reader().readShort();
         (var1 = new MainQuest(var2)).AB = 0;
         var1.AA = var0.reader().readByte();
         var0.reader().readByte();
         var1.AH = var0.reader().readUTF();
         var1.AC = var0.reader().readShort();
         var1.AI = var0.reader().readUTF();
         byte var4 = var0.reader().readByte();
         var1.AK = var0.reader().readUTF();
         Class_CP var5 = new Class_CP(var4);
         var1.AM.addElement(var5);
         var1.AL = var0.reader().readUTF();
         var1.AG = var0.reader().readShort();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   public static void get_Info_NPC(Message var0) {
      try {
         MainObject var1;
         (var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)2)).IdIcon = var0.reader().readShort();
         var1.BF = var0.reader().readUTF();
         byte var2 = var0.reader().readByte();
         byte var4 = var0.reader().readByte();
         GameCanvas.loadmap.AA(var1.x, var1.y, var2, var4);
      } catch (Exception var3) {
      }
   }

   public static void Party(Message var0) {
      try {
         int var1 = Player.vecParty.size();
         byte var2;
         if ((var2 = var0.reader().readByte()) != 5) {
            short var8;
            Class_DS var9;
            if (var2 == 1) {
               if ((var9 = Class_DS.AA(var8 = var0.reader().readShort(), Player.vecParty)) == null) {
                  var9 = new Class_DS(var8);
                  Player.vecParty.addElement(var9);
               }

               var9.AA(var0.reader().readUTF(), var0.reader().readShort(), var0.reader().readByte(), var0.reader().readByte());

               for(int var11 = 0; var11 < GameScreen.vecPlayers.size(); ++var11) {
                  MainObject var14 = (MainObject)GameScreen.vecPlayers.elementAt(var11);
                  if (var8 == var14.ID && var14.typeObject == 0) {
                     var9.AA(var14.Hp, var14.maxHp, var14.Lv);
                  }
               }
            } else if (var2 == 2) {
               if ((var9 = Class_DS.AA(var0.reader().readShort(), Player.vecParty)) != null) {
                  Player.vecParty.removeElement(var9);
               }
            } else if (var2 == 3) {
               Player.vecParty.removeAllElements();
            } else {
               String var12;
               if (var2 == 0) {
                  var8 = var0.reader().readShort();
                  var12 = var0.reader().readUTF();
                  Class_DS.AA(var8, (byte)1, var12, T.DX, 0, 0);
               } else if (var2 == 7) {
                  var8 = var0.reader().readShort();
                  var12 = var0.reader().readUTF();
                  Class_DS.AA(var8, (byte)5, var12, T.LG, 0, 0);
               }
            }
         } else {
            Player.vecParty.removeAllElements();
            var2 = var0.reader().readByte();

            int var3;
            for(var3 = 0; var3 < var2; ++var3) {
               short var4 = var0.reader().readShort();
               Class_DS var5;
               (var5 = new Class_DS(var4)).AA(var0.reader().readUTF(), var0.reader().readShort(), var0.reader().readByte(), var0.reader().readByte());
               Player.vecParty.addElement(var5);
            }

            for(var3 = 0; var3 < GameScreen.vecPlayers.size(); ++var3) {
               MainObject var10 = (MainObject)GameScreen.vecPlayers.elementAt(var3);

               for(int var13 = 0; var13 < Player.vecParty.size(); ++var13) {
                  Class_DS var7;
                  if ((var7 = (Class_DS)Player.vecParty.elementAt(var13)).AG == var10.ID && var10.typeObject == 0) {
                     var7.AA(var10.Hp, var10.maxHp, var10.Lv);
                  }
               }
            }
         }

         if (Player.vecParty.size() != var1 && GameCanvas.currentScreen == PartyScreen.AI()) {
            PartyScreen.AI().AH();
            return;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void BL(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var5;
         if ((var5 = MainObject.get_Object((int)var1, (byte)var2)) != null) {
            String var4 = GameMidlet.AE(var0.reader().readUTF());
            var5.BC = var4;
            if (var5.typeObject == 0) {
               GameCanvas.chatTabScr.AB(T.CR, "", var5.name + ": " + var4, (byte)1, false);
               return;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void BM(Message var0) {
      try {
         String var1 = var0.reader().readUTF();
         String var3;
         if ((var3 = GameMidlet.AE(var0.reader().readUTF())) != null && var3.trim().length() != 0) {
            if (var1.compareTo(T.CR) == 0) {
               GameCanvas.chatTabScr.AB(var1, T.CT + ": ", var3, (byte)1, false);
            } else if (var1.compareTo(T.DV) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)1, false);
            } else if (var1.compareTo(T.QC) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)1, false);
            } else if (var1.compareTo(T.PD) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)1, false);
            } else if (var1.compareTo(T.CS) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)1, false);
            } else if (var1.compareTo(T.CQ) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)0, false);
            } else if (var1.compareTo(T.CH) == 0) {
               GameCanvas.chatTabScr.AB(var1, "", var3, (byte)0, false);
            } else {
               GameCanvas.chatTabScr.AA(var1, var1 + ": ", var3, (byte)0, false);
            }
         }
      } catch (Exception var2) {
      }
   }

   public final void BN(Message var1) {
      try {
         short var2 = var1.reader().readShort();
         byte var3 = var1.reader().readByte();
         MainObject var14;
         if ((var14 = MainObject.get_Object((int)var2, (byte)var3)) != null) {
            String var10000 = var14.name;
            short var4 = var1.reader().readShort();
            var14.sethead(var4);
            var14.sethair(var1.reader().readShort());
            byte var15;
            short[] var5 = new short[var15 = var1.reader().readByte()];

            int var6;
            for(var6 = 0; var6 < var5.length; ++var6) {
               var5[var6] = -1;
            }

            for(var6 = 0; var6 < var15; ++var6) {
               if (var1.reader().readByte() == 1 && var14 == GameScreen.player) {
                  Item var7 = GE(var1);
                  GameScreen.player.LR.put("" + var6, var7);
                  if (var7.typeSpec == 1) {
                     GameScreen.player.MA = var7.LvUpgrade;
                  }
               }

               var5[var6] = var1.reader().readShort();
            }

            if (var3 == 1) {
               var14.AB(var5);
            } else {
               var14.AA(var5);
            }

            if (var14 == GameScreen.player) {
               if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
                  for(var6 = 0; var6 < GameCanvas.tabShopScr.AB.size(); ++var6) {
                     ((MainTab)GameCanvas.tabShopScr.AB.elementAt(var6)).AI();
                  }
               }

               TabScreen.AA = true;
               Class_FA.AA(false);
            }

            short var17;
            try {
               var17 = var1.reader().readShort();
            } catch (Exception var11) {
               var17 = -1;
            }

            var14.AE(var17);

            short var16;
            try {
               var16 = var1.reader().readShort();
            } catch (Exception var10) {
               var16 = -1;
            }

            var14.AF(var16);

            try {
               var16 = var1.reader().readShort();
            } catch (Exception var9) {
               var16 = -1;
            }

            var14.AG(var16);

            short var13;
            try {
               var13 = var1.reader().readShort();
            } catch (Exception var8) {
               var13 = -1;
            }

            var14.AH(var13);
            return;
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   public static void BO(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         short var2 = var0.reader().readShort();
         byte[] var3 = null;
         if (var2 > 0) {
            var3 = new byte[var2];

            for(int var5 = 0; var5 < var3.length; ++var5) {
               var3[var5] = var0.reader().readByte();
            }
         }

         GameCanvas.saveRms.AA(var1, var3);
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public final void BP(Message var1) {
      try {
         byte var2;
         Skill_Info var10;
         int var14;
         if ((var2 = var1.reader().readByte()) == 0 || var2 == 1) {
            Skill_Info var3;
            if ((var3 = readSkillInfo(var1)) == null) {
               return;
            }

            if (var2 == 0) {
               MsgDialog var4;
               (var4 = new MsgDialog()).AA(var3);
               GameCanvas.Start_Current_Dialog((MainDialog)var4);
            } else if (var2 == 1) {
               if ((var10 = Skill_Info.getSkillFromID(var3.ID)) != null) {
                  var3.indexHotKey = var10.indexHotKey;
                  Player.vecListSkill.setElementAt(var3, Player.vecListSkill.indexOf(var10));
               } else {
                  var3.indexHotKey = indexHotKeySkill++;
                  Player.vecListSkill.addElement(var3);
               }

               Player.vecListSkill = MainItem.SortVecItem(Player.vecListSkill);
            }

            if (var2 == 1) {
               if (var3.typeSkill == 1) {
                  if (var3.Lv_RQ == 1) {
                     boolean var12 = false;

                     for(var14 = 0; var14 < Player.hotkeyPlayer[0].length && !var12; ++var14) {
                        if (Player.hotkeyPlayer[Player.currentTab][var14].skill == null) {
                           MainSkill var7;
                           (var7 = new MainSkill(var3.ID, (short)-1)).AB = var3.indexHotKey;
                           var7.idIcon = var3.idIcon;
                           var7.isBuff = var3.typeSkill == 2;
                           Player.AA(var14, var7, (MainItem)null);
                           var12 = true;
                        }
                     }
                  } else if (var3.Lv_RQ > 1) {
                     MainImage var11 = Skill_Info.getImage(var3.idIcon);
                     FrameImage var5 = null;
                     if (var11 != null && var11.img != null) {
                        var5 = new FrameImage(var11.img, mImage.getImageWidth(var11.img.image), mImage.getImageHeight(var11.img.image));
                     }

                     GameScreen.AA(T.LA, GameScreen.player.x, GameScreen.player.y - GameScreen.player.hOne, (byte)2, var5, 0);
                  }
               }

               Player.setHotKeyBuff();
            }

            if (!Player.isSkillready) {
               if (var3.Lv_RQ == -1) {
                  Player.isSkillready = true;
               }
            } else {
               Player.isSkillready = false;

               for(int var13 = 0; var13 < Player.vecListSkill.size(); ++var13) {
                  Skill_Info var15;
                  if ((var15 = (Skill_Info)Player.vecListSkill.elementAt(var13)).Lv_RQ == -1 && (var15.typeSkill != 3 || TabSkill.BO < Player.QM)) {
                     Player.isSkillready = true;
                     break;
                  }
               }
            }
         }

         if (var2 == 2 && (var10 = Skill_Info.getSkillFromID(var1.reader().readShort())) != null) {
            var10.percentLv = var1.reader().readShort();
         }

         if (var2 == 3) {
            short var9;
            var10 = Skill_Info.getSkillFromID(var9 = var1.reader().readShort());

            for(var14 = 0; var14 < Player.hotkeyPlayer.length; ++var14) {
               for(int var6 = 0; var6 < Player.hotkeyPlayer[var14].length; ++var6) {
                  Hotkey var16;
                  if ((var16 = Player.hotkeyPlayer[var14][var6]).skill != null && var16.skill.ID == var9) {
                     var16.skill = null;
                  }
               }
            }

            Player.vecListSkill.removeElement(var10);
            Player.setHotKeyBuff();
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public final void BQ(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) == 0) {
            short var10 = var1.reader().readShort();
            String var12 = var1.reader().readUTF();
            Class_DS.AA(var10, (byte)0, var12, T.DW, 0, 0);
            return;
         }

         if (var2 == 1) {
            int var9 = var1.reader().readInt();

            for(int var11 = 0; var11 < Player.RN.size(); ++var11) {
               Class_DS var13;
               if ((var13 = (Class_DS)Player.RN.elementAt(var11)).AG == var9) {
                  Player.RN.removeElement(var13);
                  return;
               }
            }

            return;
         }

         if (var2 == 2) {
            mVector var8 = new mVector();
            byte var3 = var1.reader().readByte();

            for(int var4 = 0; var4 < var3; ++var4) {
               Class_DS var5;
               if ((var5 = GK(var1)) != null) {
                  var8.addElement(var5);
               }
            }

            Player.RN = var8;
            Class_FN.AA = true;
            Class_FN.AG().AY = Player.RN;
            Class_FN.AG().AH();
            if (!Class_AT.AA) {
               Class_FN.AG().Show((MainScreen)GameCanvas.gameScr);
            }

            Class_AT.AA = false;
            return;
         }

         Class_DS var7;
         if (var2 == 3 && (var7 = GK(var1)) != null) {
            Player.RN.addElement(var7);
            return;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public final void BR(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         String var3 = var1.reader().readUTF();
         byte var4 = var1.reader().readByte();
         mVector var5 = new mVector();
         byte var6 = var1.reader().readByte();

         for(int var7 = 0; var7 < var6; ++var7) {
            Class_DS var8;
            if (PlayerListServer.AA(var2)) {
               var8 = GM(var1);
            } else if (var2 == 9) {
               var8 = GL(var1);
            } else {
               var8 = GK(var1);
            }

            if (var8 != null) {
               var5.addElement(var8);
            }
         }

         if (var2 == 2) {
            (Class_FE.AA = new Class_FE(var2, var5, var3, var4)).Show((MainScreen)GameCanvas.gameScr);
            Class_FE.AA.BI = false;
         } else if (var2 == 9) {
            (Class_HI.AA = new Class_HI(var5, (byte)0, (byte)0)).Show((MainScreen)GameCanvas.gameScr);
         } else {
            (PlayerListServer.instance = new PlayerListServer(var2, var5, var3, var4)).Show((MainScreen)GameCanvas.gameScr);
            PlayerListServer.instance.BI = false;
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }

   private static Class_DS GK(Message var0) {
      Class_DS var1 = null;

      try {
         int var2 = var0.reader().readInt();
         (var1 = new Class_DS(var2)).AC = var0.reader().readUTF();
         if (var1.AC.compareTo(GameScreen.player.name) == 0) {
            var1.AZ = true;
         }

         var1.AK = var0.reader().readShort();
         var1.AV = var0.reader().readShort();
         var1.AW = var0.reader().readShort();
         var1.AX = var0.reader().readShort();
         var1.AP = var0.reader().readByte();
         var1.AD = var0.reader().readUTF();
         var1.AN = var0.reader().readShort();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   private static Class_DS GL(Message var0) {
      Class_DS var1 = null;

      try {
         int var2 = var0.reader().readInt();
         (var1 = new Class_DS(var2)).AC = var0.reader().readUTF();
         if (var1.AC.compareTo(GameScreen.player.name) == 0) {
            var1.AZ = true;
         }

         short var10001 = var0.reader().readShort();
         short var10002 = var0.reader().readShort();
         short var10003 = var0.reader().readShort();
         short var10004 = var0.reader().readShort();
         short var8;
         short var7 = var8 = var0.reader().readShort();
         short var6 = var10004;
         short var5 = var10003;
         short var4 = var10002;
         short var3 = var10001;
         var1.BC = new MainObject();
         var1.BC.sethead(var3);
         var1.BC.sethair(var4);
         var1.BC.BX = var5;
         var1.BC.BT = var6;
         var1.BC.BU = var7;
         var1.BC.BV = var8;
         var1.BC.rankWanted = var0.reader().readInt();
         var1.BC.KP = var0.reader().readInt();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return var1;
   }

   private static Class_DS GM(Message var0) {
      Class_DS var1 = null;

      try {
         short var2 = var0.reader().readShort();
         var1 = new Class_DS(var2);
         String var4 = GameMidlet.AE(var0.reader().readUTF());
         var1.AC = var4;
         var1.AD = var0.reader().readUTF();
         var1.AA = var0.reader().readShort();
         var1.AN = var0.reader().readShort();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   public static void BS(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         String var2 = GameMidlet.AE(var0.reader().readUTF());
         byte var3 = var0.reader().readByte();
         InfoShowNotify var8 = new InfoShowNotify(var2, var1);
         mFont var4 = null;
         if (var1 == 0) {
            var4 = AvMain.AB(var3);
         }

         if (var1 == 1) {
            var4 = AvMain.AC(var3);
         }

         var8.AB(var4);

         try {
            short var7 = var0.reader().readShort();
            var8.AD = var7;
         } catch (Exception var5) {
         }

         Interface_Game.AA(var8);
      } catch (Exception var6) {
      }
   }

   public static void Check_Data_Ver(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         short var2 = var0.reader().readShort();
         if (!GameCanvas.lowGraphic) {
            if (var1 != GlobalService.AD) {
               GlobalService.getInstance().get_DATA((byte)15);
            } else {
               CatalogyMonster.AA(SaveRms.AB("dataMon"), false);
            }

            if (var2 != GlobalService.AE) {
               GlobalService.getInstance().get_DATA((byte)28);
            } else {
               Potion.UpdateDataPotion(SaveRms.AB("dataPotion"), false, (byte)4);
            }
         } else {
            LoadMapScreen.isLoadDataMon = true;
         }

         if (var0.reader().readShort() != GlobalService.AF) {
            GlobalService.getInstance().get_DATA((byte)2);
         } else {
            MainItem.AA(SaveRms.AB("dataAttri"), false);
         }

         var0.reader().readShort();
         if (var0.reader().readShort() != GlobalService.AG) {
            GlobalService.getInstance().get_DATA((byte)6);
         } else {
            LoadMap.AA(SaveRms.AB("dataNameMap"), false);
         }

         if (var0.reader().readShort() != GlobalService.AI) {
            GlobalService.getInstance().get_DATA((byte)7);
         } else {
            MainQuest.AA(SaveRms.AB("dataNamePotionquest"), false);
         }

         var0.reader().readShort();
         if ((var1 = var0.reader().readShort()) != GlobalService.AK) {
            if (GlobalService.AK != -1) {
               GameMidlet.AC();
            }

            GlobalService.AK = var1;
            SaveRms.AA(var1, "VerdataImageSave");
         }

         if (var0.reader().readShort() != GlobalService.AL) {
            GlobalService.getInstance().get_DATA((byte)12);
         } else {
            MainDataUpgrade.LoadDataUpgrade(SaveRms.AB("dataUpgradeSave"), false);
         }

         if (var0.reader().readShort() != GlobalService.AM) {
            GlobalService.getInstance().get_DATA((byte)29);
         } else {
            Potion.UpdateDataPotion(SaveRms.AB("dataPotionClan"), false, (byte)8);
         }

         LoginScreen.AB = true;
         GameCanvas.end_Dialog();
         if (GameCanvas.currentScreen == GameCanvas.loginScr) {
            GameCanvas.loginScr.AA(false, (byte)0, GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
            return;
         }

         if (GameCanvas.currentScreen == GameCanvas.fristLoginScr) {
            FristLoginScreen var10000 = GameCanvas.fristLoginScr;
            FristLoginScreen.AA(true);
            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void BU(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         short var2 = -1;
         if (var1 == 21) {
            var2 = var0.reader().readShort();
         }

         String var3 = var0.reader().readUTF();
         String var4 = var0.reader().readUTF();
         byte var5;
         Item_Drop[] var6 = new Item_Drop[var5 = var0.reader().readByte()];

         for(int var7 = 0; var7 < var5; ++var7) {
            byte var8 = var0.reader().readByte();
            String var9 = var0.reader().readUTF();
            short var10 = var0.reader().readShort();
            int var11 = var0.reader().readInt();
            byte var12 = var0.reader().readByte();
            var6[var7] = new Item_Drop((short)var7, var8, var9, 0, 0, var10, var12);
            var6[var7].num = var11;
         }

         MsgShowGift var14 = new MsgShowGift();
         Item_Drop var15;
         if (var1 == 23) {
            (var15 = new Item_Drop((short)-1, (byte)4, T.TS, GameScreen.player.x, GameScreen.player.y, (short)325, (byte)4)).BQ = var6;
            GameScreen.addPlayer((MainObject)var15);
         } else if (var1 == 24) {
            (var15 = new Item_Drop((short)-1, (byte)4, T.TS, GameScreen.player.x, GameScreen.player.y, (short)326, (byte)4)).BQ = var6;
            GameScreen.addPlayer((MainObject)var15);
         } else if (var1 == 22) {
            var14.setinfoShow_Gift_OnHead(var1, var3, var4, var6, var2, GameScreen.player);
            GameCanvas.BS = var14;
         } else {
            var14.setinfoShow_Gift(var1, var3, var4, var6, var2);
            GameCanvas.Start_Current_Dialog((MainDialog)var14);
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }
   }

   public static void BV(Message var0) {
      try {
         if (var0.reader().readByte() == 0) {
            short var1 = var0.reader().readShort();
            String var2 = var0.reader().readUTF();
            short var3 = var0.reader().readShort();
            byte var4 = 0;
            if (var0.reader().available() > 0) {
               var4 = var0.reader().readByte();
            }

            Class_DS.AA(var1, (byte)3, var2, T.EL, var3, var4);
            return;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void BW(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         short var2 = var0.reader().readShort();
         short var3 = var0.reader().readShort();
         byte var4 = var0.reader().readByte();
         short var5 = var0.reader().readShort();
         short var6 = var0.reader().readShort();
         int var7 = var0.reader().readInt();
         byte var8 = var0.reader().readByte();
         byte var9 = var0.reader().readByte();
         MainObject var11;
         if (var1 == 1 && (var11 = MainObject.get_Object((int)var3, (byte)var4)) != null && var11 != GameScreen.player) {
            var11.addEffBuff((byte)1, (short)var6, (short)0);
         }

         MainBuff var12;
         (var12 = new MainBuff(var2)).AA(var7);
         var12.AC = var5;
         mVector var14 = new mVector();

         int var16;
         for(int var15 = 0; var15 < var9; ++var15) {
            MainObject var18;
            if ((var18 = MainObject.get_Object((int)var0.reader().readShort(), (byte)var8)) != null) {
               var14.addElement(var18);
               if (var18 == GameScreen.player) {
                  byte var20 = var0.reader().readByte();

                  for(var16 = 0; var16 < var20; ++var16) {
                     MainInfoItem var21 = new MainInfoItem(var0.reader().readByte(), var0.reader().readShort());
                     var12.AH.addElement(var21);
                  }
               }

               if (var12.IdBuff == 2057) {
                  var18.AA((short)3, (int)var12.timeBuff);
               }

               var18.AA(var12);
            }
         }

         if (var0.reader().readByte() == 3) {
            short var17 = var0.reader().readShort();
            var5 = var0.reader().readShort();
            var6 = var0.reader().readShort();
            var12.AG = true;
            var12.AI = var17;
            var12.AJ = var5;
            var12.AK = var6;

            for(var16 = 0; var16 < var14.size(); ++var16) {
               ((MainObject)var14.elementAt(var16)).BI();
            }

            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void BX(Message var0) {
      try {
         LoadMapScreen.typeChangeMap = 0;
         GameCanvas.loadMapScr.Show();
         GameCanvas.loadmap.idLastMap = GameCanvas.loadmap.idMapLoadMap;
         GameScreen.RemoveLoadMap();
         GameCanvas.loadMapScr.area = var0.reader().readByte();
         byte var1 = var0.reader().readByte();
         GameScreen.player.posTransRoad = null;
         GameScreen.player.x = var0.reader().readShort();
         GameScreen.player.y = var0.reader().readShort();
         GameScreen.player.maxHp = var0.reader().readInt();
         GameScreen.player.Hp = var0.reader().readInt();
         GameScreen.player.maxMp = var0.reader().readInt();
         GameScreen.player.Mp = var0.reader().readInt();
         LoadMapScreen.IDBack = var0.reader().readByte();
         LoadMapScreen.HBack = var0.reader().readShort();
         LoadMapScreen.isNextMap = true;
         GameCanvas.gameScr.setTypeViewPlayer(var1);
      } catch (Exception var2) {
      }
   }

   public static void BY(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var5;
         (var5 = MainObject.get_Object((int)var1, (byte)0)).NT = new short[var2];

         for(int var3 = 0; var3 < var2; ++var3) {
            var5.NT[var3] = var0.reader().readShort();
         }

      } catch (Exception var4) {
      }
   }

   public static void AreaStatus(Message var0) {
      try {
         byte var1;
         byte[] var2 = new byte[var1 = var0.reader().readByte()];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = var0.reader().readByte();
         }

         MsgArea msgArea;
         (msgArea = new MsgArea()).setinfoChangeArea(var2, (byte)0);
         GameCanvas.Start_Current_Dialog((MainDialog)msgArea);
      } catch (Exception var4) {
      }
   }

   public static void Register(Message var0) {
      try {
         if (var0.reader().readByte() == 0) {
            String var3 = var0.reader().readUTF();
            GameCanvas.loginScr.AA = 0;
            GameCanvas.loginScr.AH();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var3);
            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void CB(Message var0) {
      try {
         short var1 = var0.reader().readShort();

         for(int var2 = 0; var2 < ListChar_Screen.AA.size(); ++var2) {
            MainObject var3;
            if ((var3 = (MainObject)ListChar_Screen.AA.elementAt(var2)).ID == var1) {
               var3.MJ = var0.reader().readByte();
               if (var3.MJ != 0) {
                  var3.timeDie = (long)var0.reader().readInt();
               } else {
                  var3.timeDie = 0L;
               }
            }
         }

      } catch (Exception var4) {
      }
   }

   public static void CC(Message var0) {
      try {
         MainObject var2;
         if ((var2 = MainObject.get_Object((int)var0.reader().readShort(), (byte)1)) != null && (var2.x < MainScreen.cameraMain.xCam || var2.x > MainScreen.cameraMain.xCam + MotherCanvas.w)) {
            var2.Hp = 0;
            var2.beginDie((MainObject)null);
            return;
         }
      } catch (Exception var1) {
      }

   }

   public static void CD(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         short var3 = var0.reader().readShort();
         short var6 = var0.reader().readShort();
         mFont var4 = mFont.tahoma_7_white;
         if (var3 >= var6) {
            var4 = mFont.tahoma_7_yellow;
         }

         if (var2 == 1) {
            CatalogyMonster var7 = MainMonster.AD(var1);
            Interface_Game.addInfoPlayerNormal(T.CC + " " + var7.AL + ": " + var3 + "/" + var6, var4);
            return;
         }

         if (var2 == 5 && var1 < Class_CP.AF.length) {
            Interface_Game.addInfoPlayerNormal(T.CB + " " + Class_CP.AF[var1] + ": " + var3 + "/" + var6, var4);
            return;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void CE(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         short var3 = var0.reader().readShort();
         short var6 = var0.reader().readShort();
         MainObject var4;
         if ((var4 = MainObject.get_Object((int)var1, (byte)var2)) != null && !var4.returnAction()) {
            GameScreen.addEffectEnd_ObjTo((short)56, 0, var3, var6, (short)var1, (byte)var2, (byte)var4.Dir, var4);
            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void CF(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         mVector var4 = new mVector();
         mVector var5 = new mVector();
         MainObject var7;
         if ((var7 = MainObject.get_Object((int)var1, (byte)var2)) != null && !var7.returnAction()) {
            short var3 = var0.reader().readShort();
            Object_Effect_Skill var6 = new Object_Effect_Skill(var7.ID, var7.typeObject);
            short var8 = var0.reader().readShort();
            byte var9 = var0.reader().readByte();
            int var10;
            Point_Focus var11;
            byte var15;
            switch(var3) {
            case 10014:
               GameScreen.addEffectEnd_ObjTo((short)69, 0, 0, 0, (short)var1, (byte)var2, (byte)var7.Dir, var7);
               return;
            case 10015:
            default:
               if (var9 != 0 && var9 != 2) {
                  var9 = 0;
               }

               MainSkill var13;
               (var13 = new MainSkill((short)-1, var3)).setTypeBuff((byte)2, (short)0, var8);
               short var14 = var0.reader().readShort();
               var3 = var0.reader().readShort();
               var15 = var0.reader().readByte();

               for(var10 = 0; var10 < var15; ++var10) {
                  var11 = new Point_Focus(var0.reader().readShort(), var0.reader().readShort());
                  var5.addElement(var11);
               }

               var13.AA(var14, var3, var5, var9);
               var4.addElement(var6);
               var7.AB(var13, var4);
               return;
            case 10016:
               var0.reader().readShort();
               var0.reader().readShort();
               var15 = var0.reader().readByte();

               for(var10 = 0; var10 < var15; ++var10) {
                  var11 = new Point_Focus(var0.reader().readShort(), var0.reader().readShort());
                  GameScreen.addEffectEnd_ObjTo((short)70, 0, var11.x, var11.y, (short)var1, (byte)var2, (byte)var7.Dir, var7);
               }

               return;
            }
         }
      } catch (Exception var12) {
      }

   }

   public static void CG(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         int var3 = var0.reader().readInt();
         int var4 = var0.reader().readInt();
         short var5 = var0.reader().readShort();
         short var7 = var0.reader().readShort();
         MainObject var8;
         if ((var8 = MainObject.get_Object((int)var1, (byte)var2)) != null && !var8.returnAction()) {
            var8.maxHp = var4;
            String var9 = "" + (var3 - var8.Hp);
            if (var3 > var8.Hp) {
               var9 = "+" + (var3 - var8.Hp);
            }

            if (var3 - var8.Hp != 0) {
               GameScreen.addEffectNum(var9, var8.x, var8.y - var8.hOne / 4 * 3, (byte)3);
            }

            var8.Hp = var3;
            if (var5 != -1) {
               var8.addEffSpec(var5, var7);
            }

            if (var8 == GameScreen.player) {
               GlobalService.getInstance().Obj_Move((short)var8.x, (short)var8.y);
               return;
            }
         }
      } catch (Exception var6) {
      }

   }

   public static void CH(Message var0) {
      try {
         ++UpdateImageScreen.AB;
         String var1 = var0.reader().readUTF();
         byte[] var2 = new byte[var0.reader().available()];
         var0.reader().read(var2);
         SaveImageRMS.AB.addElement(new idSaveImage(var1, var2));
      } catch (Exception var3) {
      }
   }

   public static void resetValueUpdateImage(Message var0) {
      try {
         String var1 = var0.reader().readUTF();

         for(int var2 = 0; var2 < T.mStringDownload.length; ++var2) {
            if (var1.compareTo(T.mStringDownload[var2]) == 0) {
               var1 = T.mStringDownload[var2 + 1];
               break;
            }
         }

         UpdateImageScreen.AA(T.FW + var1);
         UpdateImageScreen.AB(0, var0.reader().readShort());
      } catch (Exception var3) {
      }
   }

   public static void LoadImageAndroidOk() {
      try {
         UpdateImageScreen.AA(T.FX);
         UpdateImageScreen.AC = 3;
         UpdateImageScreen.AA = SaveImageRMS.AB.size();
         GameCanvas.saveImage.start();
      } catch (Exception var0) {
      }
   }

   public static void CJ(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) <= 0) {
            Interface_Game.BU = false;
            Player.RB = -1;
            Interface_Game.BS = -1;
         } else {
            Interface_Game.BU = true;
            Player.RB = -1;
            Player.QG = new short[var1];

            for(int var2 = 0; var2 < var1; ++var2) {
               Player.QG[var2] = var0.reader().readShort();
            }

            Interface_Game.BR = 30;
            Interface_Game.BS = -1;
         }
      } catch (Exception var3) {
      }
   }

   public final void ShowInfoPlayer(Message m) {
      try {
         MainObject mainObject;
         (mainObject = new MainObject()).name = m.reader().readUTF();
         mainObject.maxHp = m.reader().readInt();
         mainObject.maxMp = m.reader().readInt();
         mainObject.Hp = m.reader().readInt();
         mainObject.Mp = m.reader().readInt();
         mainObject.Lv = m.reader().readShort();
         mainObject.percentLv = m.reader().readShort();
         short headset = m.reader().readShort();
         mainObject.sethead(headset);
         mainObject.sethair(m.reader().readShort());
         if ((headset = m.reader().readShort()) >= 0) {
            mainObject.clan = new MainClan();
            mainObject.clan.ID = headset;
            mainObject.clan.idIcon = m.reader().readShort();
            String text = GameMidlet.AE(m.reader().readUTF());
            mainObject.clan.name = text;
         }

         byte var9;
         short[] var4 = new short[var9 = m.reader().readByte()];

         int var5;
         for(var5 = 0; var5 < var4.length; ++var5) {
            var4[var5] = -1;
         }

         for(var5 = 0; var5 < var9; ++var5) {
            if (m.reader().readByte() == 1) {
               Item var6 = GE(m);
               mainObject.LR.put("" + var5, var6);
               var4[var5] = m.reader().readShort();
            }
         }

         mainObject.AA(var4);
         byte var10 = m.reader().readByte();
         short var11 = m.reader().readShort();
         if (var10 == 0) {
            mainObject.BY = var11;
         }

         mainObject.MY = m.reader().readByte();
         GameCanvas.Start_Current_Dialog((MainDialog)(new MsgOtherCharInfo(mainObject)));
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public static void loadDataEff(Message m) {
      try {
         byte[] dataeff = new byte[m.reader().available()];
         m.reader().read(dataeff);
         EffectAuto.readData(dataeff, true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void update_Pk_Point(Message var0) {
      try {
         GameScreen.player.KN = var0.reader().readInt();
      } catch (Exception var1) {
      }
   }

   public static void setWeather(Message var0) {
      if (!GameCanvas.lowGraphic && !LoadMapScreen.isSuperBoss) {
         try {
            byte var1 = var0.reader().readByte();
            byte var3 = var0.reader().readByte();
            if (var1 < 0) {
               GameScreen.effMap = null;
               return;
            }

            GameScreen.effMap = new Effect_Map(var1, var3);
            if (var1 == 9) {
               GameCanvas.mapBack.AG();
               LoadMapScreen.isSuperBoss = true;
               return;
            }

            if (var1 == 8) {
               if (GameCanvas.mapBack != null) {
                  GameCanvas.mapBack.AF();
                  return;
               }
            } else if (var1 == 1 && GameCanvas.mapBack != null) {
               GameCanvas.mapBack.AE();
               return;
            }
         } catch (Exception var2) {
         }

      }
   }

   public final void Upgrade_Item(Message m) {
      try {
         byte b;
         if ((b = m.reader().readByte()) != 7 && ScreenUpgrade.instance == null) {
            ScreenUpgrade.instance = new ScreenUpgrade((byte)5, -1);
         }

         String info;
         if (b == 0) {
            info = m.reader().readUTF();
            if (ScreenUpgrade.isClickUpgradeItemToSucess) {
               ScreenUpgrade.stopUpgradeItemAuto();
            }
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(info);
            return;
         }

         if (b == 1) {
            info = m.reader().readUTF();
            int num = m.reader().readInt();
            short num2 = m.reader().readShort();
            idItemUpgrade = m.reader().readShort();
            mVector mVector = new mVector();
            iCommand iCommand;
            if (num > 0) {
               (iCommand = new iCommand(String.valueOf(num), 1, 1, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 0, (int)0);
               mVector.addElement(iCommand);
            }

            if (num2 > 0) {
               (iCommand = new iCommand(String.valueOf(num2), 1, 2, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 1, (int)0);
               mVector.addElement(iCommand);
            }

            iCommand = new iCommand(T.close, 1, 0, this);
            mVector.addElement(iCommand);
            GameCanvas.Start_Normal_DiaLog(info, mVector, false);
            return;
         }
         if (b == 2) {
            info = m.reader().readUTF();
            ScreenUpgrade.instance.showServer = info;
            ScreenUpgrade.instance.Step = 1;
            if (ScreenUpgrade.isClickUpgradeItemToSucess) {
               ScreenUpgrade.isUpgradeItemSucess = true;
               return;
            }
         } else if (b == 3) {
            info = m.reader().readUTF();
            ScreenUpgrade.instance.showServer = info;
            ScreenUpgrade.instance.Step = 2;
            if (ScreenUpgrade.isClickUpgradeItemToSucess) {
               ScreenUpgrade.isUpgradeItemSucess = false;
               return;
            }
         } else {
            MainItem mainItem;
            if (b == 4) {
               short id = m.reader().readShort();
               MainItem itemVec;
               if ((itemVec = MainItem.getItemVec((byte)3, id, Player.vecInventory)) != null) {
                  mainItem = new MainItem((byte)3, itemVec.ID, itemVec.idIcon, (short)1, itemVec.colorName, itemVec.LvUpgrade);
                  ScreenUpgrade.mItemUpgrade[0] = mainItem;
                  ScreenUpgrade.instance.setDataUpgrade();
               }
               ScreenUpgrade.instance.getMenuActionItem();
               return;
            }
            MainItem itemVec2;
            short id2;
            byte flag;
            if (b == 5) {
               flag = m.reader().readByte();
               id2 = m.reader().readShort();
               if (flag == 0) {
                  ScreenUpgrade.mItemUpgrade[1] = null;
                  ScreenUpgrade.valueLucky = 1;
               } else if ((mainItem = MainItem.getItemVec((byte)7, id2, Player.vecInventory)) != null) {
                  if (mainItem.typeMaterial == 4) {
                     ScreenUpgrade.valueLucky = 10;
                  } else if (mainItem.typeMaterial == 2) {
                     ScreenUpgrade.valueLucky = 20;
                  }
                  itemVec2 = new MainItem((byte)7, mainItem.ID, mainItem.idIcon, (short)1, mainItem.colorName, mainItem.LvUpgrade);
                  ScreenUpgrade.mItemUpgrade[1] = itemVec2;
               }
               ScreenUpgrade.instance.getMenuActionItem();
               return;
            }
            if (b == 6) {
               flag = m.reader().readByte();
               id2 = m.reader().readShort();
               if (flag == 0) {
                  ScreenUpgrade.mItemUpgrade[2] = null;
               } else if ((mainItem = MainItem.getItemVec((byte)7, id2, Player.vecInventory)) != null) {
                  itemVec2 = new MainItem((byte)7, mainItem.ID, mainItem.idIcon, (short)1, mainItem.colorName, mainItem.LvUpgrade);
                  ScreenUpgrade.mItemUpgrade[2] = itemVec2;
               }

               ScreenUpgrade.instance.getMenuActionItem();
               return;
            }
            if (b == 7) {
               (ScreenUpgrade.instance = new ScreenUpgrade((byte)5, -1)).Show((MainScreen)GameCanvas.gameScr);
               return;
            }

            if (b == 15) {
               if (MsgUpdateHeart.instance == null) {
                  MsgUpdateHeart.instance = new MsgUpdateHeart();
               }

               MsgUpdateHeart.instance.Show((MainScreen)GameCanvas.gameScr);
               return;
            }

            if (b == 16 || b == 17) {
               info = m.reader().readUTF();
               MsgUpdateHeart.instance.updateStepUpgrade(b, info);
               return;
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public final void CP(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) != 7 && ScreenUpgrade.instance == null) {
            ScreenUpgrade.instance = new ScreenUpgrade((byte)15, -1);
         }

         if (var2 == 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
            if (ScreenUpgrade.isClickUpgradeSuperItemToSucess) {
               ScreenUpgrade.stopUpgradeSuperItemAuto();
               return;
            }
         } else {
            String var8;
            short var15;
            if (var2 == 1) {
               var8 = var1.reader().readUTF();
               int var14 = var1.reader().readInt();
               var15 = var1.reader().readShort();
               idItemUpgrade = var1.reader().readShort();
               mVector var18 = new mVector();
               iCommand var16;
               if (var14 > 0) {
                  (var16 = new iCommand(String.valueOf(var14), 12, 1, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 0, (int)0);
                  var18.addElement(var16);
               }

               if (var15 > 0) {
                  (var16 = new iCommand(String.valueOf(var15), 12, 2, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 1, (int)0);
                  var18.addElement(var16);
               }

               var16 = new iCommand(T.close, 1, 0, this);
               var18.addElement(var16);
               GameCanvas.Start_Normal_DiaLog(var8, var18, false);
               return;
            }

            if (var2 == 2) {
               var8 = var1.reader().readUTF();
               ScreenUpgrade.instance.showServer = var8;
               ScreenUpgrade.instance.Step = 1;
               if (ScreenUpgrade.isClickUpgradeSuperItemToSucess) {
                  ScreenUpgrade.CC = true;
                  return;
               }
            } else if (var2 == 3) {
               var8 = var1.reader().readUTF();
               ScreenUpgrade.instance.showServer = var8;
               ScreenUpgrade.instance.Step = 2;
               if (ScreenUpgrade.isClickUpgradeSuperItemToSucess) {
                  ScreenUpgrade.CC = false;
                  return;
               }
            } else {
               MainItem var4;
               MainItem var9;
               if (var2 == 4) {
                  short var10 = var1.reader().readShort();
                  if ((var9 = MainItem.getItemVec((byte)3, var10, Player.vecInventory)) != null) {
                     var4 = new MainItem((byte)3, var9.ID, var9.idIcon, (short)1, var9.colorName, var9.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[0] = var4;
                     ScreenUpgrade.mItemUpgradeSaveTemp[0] = var4;
                  }

                  var15 = var1.reader().readShort();
                  short var17 = var1.reader().readShort();
                  MainMaterial var13 = (MainMaterial)MainItem.hashMaterialTem.get("" + var15);
                  MainItem var11;
                  (var11 = new MainItem((byte)7, var13.idIcon, var15)).numPotion = var17;
                  ScreenUpgrade.mItemUpgrade[4] = var11;
                  var10 = var1.reader().readShort();
                  short var7 = var1.reader().readShort();
                  var13 = (MainMaterial)MainItem.hashMaterialTem.get("" + var10);
                  (var11 = new MainItem((byte)7, var13.idIcon, var10)).numPotion = var7;
                  ScreenUpgrade.mItemUpgrade[5] = var11;
                  return;
               }

               short var3;
               MainItem var5;
               if (var2 == 5) {
                  var2 = var1.reader().readByte();
                  var3 = var1.reader().readShort();
                  if (var2 == 0) {
                     ScreenUpgrade.mItemUpgradeSaveTemp[1] = null;
                     ScreenUpgrade.mItemUpgrade[1] = null;
                     ScreenUpgrade.valueLucky = 1;
                  } else if ((var4 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
                     if (var4.typeMaterial == 4) {
                        ScreenUpgrade.valueLucky = 10;
                     } else if (var4.typeMaterial == 2) {
                        ScreenUpgrade.valueLucky = 20;
                     }

                     var5 = new MainItem((byte)7, var4.ID, var4.idIcon, (short)1, var4.colorName, var4.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[1] = var5;
                     ScreenUpgrade.mItemUpgradeSaveTemp[1] = var5;
                  }

                  ScreenUpgrade.instance.getMenuActionItem();
                  return;
               }

               if (var2 == 6) {
                  var2 = var1.reader().readByte();
                  var3 = var1.reader().readShort();
                  byte var12 = var1.reader().readByte();
                  if (var2 == 0) {
                     ScreenUpgrade.mItemUpgradeSaveTemp[2] = null;
                     ScreenUpgrade.mItemUpgrade[2] = null;
                  } else if ((var5 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
                     (var9 = new MainItem((byte)7, var5.ID, var5.idIcon, (short)1, var5.colorName, var5.LvUpgrade)).numPotion = var12;
                     ScreenUpgrade.mItemUpgrade[2] = var9;
                     ScreenUpgrade.mItemUpgradeSaveTemp[2] = var9;
                  }

                  ScreenUpgrade.instance.getMenuActionItem();
                  return;
               }

               if (var2 == 14) {
                  var2 = var1.reader().readByte();
                  var3 = var1.reader().readShort();
                  if (var2 == 0) {
                     ScreenUpgrade.mItemUpgrade[3] = null;
                     ScreenUpgrade.mItemUpgradeSaveTemp[3] = null;
                  } else if ((var4 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
                     var5 = new MainItem((byte)7, var4.ID, var4.idIcon, (short)1, var4.colorName, var4.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[3] = var5;
                     ScreenUpgrade.mItemUpgradeSaveTemp[3] = var5;
                  }

                  ScreenUpgrade.instance.getMenuActionItem();
                  return;
               }

               if (var2 == 7) {
                  (ScreenUpgrade.instance = new ScreenUpgrade((byte)15, -1)).Show((MainScreen)GameCanvas.gameScr);
                  return;
               }
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public final void CQ(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         (new StringBuffer("nhan type upgrade dial =")).append(var2).toString();
         if (var2 != 7 && ScreenUpgrade.instance == null) {
            ScreenUpgrade.instance = new ScreenUpgrade((byte)18, -1);
         }

         if (var2 == 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
            return;
         }

         if (var2 == 7) {
            (ScreenUpgrade.instance = new ScreenUpgrade((byte)18, -1)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         short var3;
         MainItem var4;
         MainItem var8;
         byte var12;
         if (var2 == 4) {
            short var13 = var1.reader().readShort();
            MainItem var11;
            if ((var11 = MainItem.getItemVec((byte)3, var13, Player.vecInventory)) != null) {
               var4 = new MainItem((byte)3, var11.ID, var11.idIcon, (short)1, var11.colorName, var11.LvUpgrade);
               ScreenUpgrade.mItemUpgrade[0] = var4;
            }

            var12 = var1.reader().readByte();

            int var18;
            for(var18 = 1; var18 <= var12; ++var18) {
               var13 = var1.reader().readShort();
               var3 = var1.reader().readShort();
               MainMaterial var6 = (MainMaterial)MainItem.hashMaterialTem.get("" + var13);
               (var8 = new MainItem((byte)7, var6.idIcon, var13)).numPotion = var3;
               ScreenUpgrade.mItemUpgrade[ScreenUpgrade.mItemUpgrade.length - var18] = var8;
            }

            var18 = var1.reader().readInt();
            int var17 = var1.reader().readInt();
            int var15 = var1.reader().readInt();
            byte var19 = var1.reader().readByte();
            ScreenUpgrade.instance.CM = var19;
            ScreenUpgrade.instance.CE = 0;
            ScreenUpgrade.instance.CF = 0;
            ScreenUpgrade var10000 = ScreenUpgrade.instance;
            ScreenUpgrade.setInfo_money(var19, var18, var17, var15);
            ScreenUpgrade.instance.getMenuActionItem();
            return;
         }

         String var10;
         if (var2 == 1) {
            var10 = var1.reader().readUTF();
            idItemUpgrade = var1.reader().readShort();
            mVector var9 = new mVector();
            iCommand var14 = new iCommand(T.CJ, 18, 0, this);
            var9.addElement(var14);
            iCommand var16 = new iCommand(T.close, 1, 0, this);
            var9.addElement(var16);
            GameCanvas.Start_Normal_DiaLog(var10, var9, false);
            return;
         }

         if (var2 == 2) {
            var10 = var1.reader().readUTF();
            ScreenUpgrade.instance.showServer = var10;
            ScreenUpgrade.instance.Step = 1;
            return;
         }

         if (var2 == 3) {
            var10 = var1.reader().readUTF();
            ScreenUpgrade.instance.showServer = var10;
            ScreenUpgrade.instance.Step = 2;
            return;
         }

         MainItem var5;
         if (var2 == 5) {
            var2 = var1.reader().readByte();
            var3 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            if (var2 == 0) {
               ScreenUpgrade.mItemUpgrade[1] = null;
               ScreenUpgrade.valueLucky = 1;
            } else if ((var5 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
               (var8 = new MainItem((byte)7, var5.ID, var5.idIcon, (short)1, var5.colorName, var5.LvUpgrade)).numPotion = var12;
               ScreenUpgrade.mItemUpgrade[1] = var8;
            }

            ScreenUpgrade.instance.CF = var1.reader().readByte();
            ScreenUpgrade.instance.getMenuActionItem();
            return;
         }

         if (var2 == 6) {
            var2 = var1.reader().readByte();
            var3 = var1.reader().readShort();
            var12 = var1.reader().readByte();
            if (var2 == 0) {
               ScreenUpgrade.mItemUpgrade[2] = null;
            } else if ((var5 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
               (var8 = new MainItem((byte)7, var5.ID, var5.idIcon, (short)1, var5.colorName, var5.LvUpgrade)).numPotion = var12;
               ScreenUpgrade.mItemUpgrade[2] = var8;
            }

            ScreenUpgrade.instance.CE = var1.reader().readByte();
            ScreenUpgrade.instance.getMenuActionItem();
            (new StringBuffer(" valueBaoHiem = ")).append(ScreenUpgrade.instance.CE).toString();
            return;
         }

         if (var2 == 14) {
            var2 = var1.reader().readByte();
            var3 = var1.reader().readShort();
            if (var2 == 0) {
               ScreenUpgrade.mItemUpgrade[3] = null;
            } else if ((var4 = MainItem.getItemVec((byte)7, var3, Player.vecInventory)) != null) {
               var5 = new MainItem((byte)7, var4.ID, var4.idIcon, (short)1, var4.colorName, var4.LvUpgrade);
               ScreenUpgrade.mItemUpgrade[3] = var5;
            }

            ScreenUpgrade.instance.getMenuActionItem();
            return;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public final void CR(Message var1) {
      try {
         byte var2;
         byte var3;
         mVector var4;
         short var6;
         String var7;
         String var8;
         short var9;
         byte var10;
         boolean var12;
         Class_DW var13;
         Potion var15;
         MainMaterial var17;
         int var21;
         mVector var22;
         int var24;
         short var26;
         short var28;
         String var31;
         short var33;
         if ((var2 = var1.reader().readByte()) == 0) {
            var1.reader().readByte();
            var12 = false;
            var3 = var1.reader().readByte();
            var4 = new mVector();

            for(var21 = 0; var21 < var3; ++var21) {
               var6 = var1.reader().readShort();
               var7 = var1.reader().readUTF();
               var8 = var1.reader().readUTF();
               var9 = var1.reader().readShort();
               var10 = var1.reader().readByte();
               (var13 = new Class_DW(var6, var9, var7, var8, (short[])null)).CG = false;
               var13.LvUpgrade = var10;
               var13.AA(var13.info);
               if (var13.ID == Player.idFashion) {
                  var13.addInfoFrist(T.daTrangBi, (byte)4);
                  var13.colorName = 4;
               } else {
                  var13.addInfoFrist(T.IV, (byte)1);
                  var13.colorName = 1;
               }

               var4.addElement(var13);
            }

            var3 = var1.reader().readByte();
            (new StringBuffer("size da kham ")).append(var3).toString();
            var22 = new mVector();

            for(var24 = 0; var24 < var3; ++var24) {
               var2 = var1.reader().readByte();
               var26 = var1.reader().readShort();
               var28 = var1.reader().readShort();
               var31 = var1.reader().readUTF();
               var33 = var1.reader().readShort();
               (new StringBuffer(String.valueOf(var2))).append(" ").append(var26).append(" ").append(var28).append(" ").append(var31).append(" ").append(var33).toString();
               if (var2 == 4) {
                  (var15 = new Potion(var2, var26, var33, var31, (byte)0)).numPotion = var28;
                  var22.addElement(var15);
               } else if (var2 == 7) {
                  var17 = (MainMaterial)MainItem.hashMaterialTem.get("" + var26);
                  var17 = new MainMaterial((byte)7, (byte)var26, var17.name, var17.typeMaterial, (byte)var17.idIcon, var28, var17.AF, var17.AJ, var17.BO);
                  var22.addElement(var17);
               }
            }

            (SkinUpgradeScreen.instance = new SkinUpgradeScreen((byte)22, -1, var4, var22)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var2 == 1) {
            var2 = var1.reader().readByte();
            short var16 = var1.reader().readShort();
            byte var19 = var1.reader().readByte();
            byte var25 = var1.reader().readByte();
            if (var1.reader().readByte() == 0) {
               ScreenUpgrade.mItemUpgrade[var25] = null;
            } else {
               MainItem var27;
               if (var2 == 105) {
                  var27 = MainItem.getItemVec(var2, var16, SkinUpgradeScreen.vecSkin);
               } else {
                  var27 = MainItem.getItemVec(var2, var16, SkinUpgradeScreen.vecDa);
               }

               if (var27 != null) {
                  MainItem var30;
                  (var30 = new MainItem(var2, var27.ID, var27.idIcon, (short)1, var27.colorName, var27.LvUpgrade)).BG = var19;
                  ScreenUpgrade.mItemUpgrade[var25] = var30;
               }
            }

            int var29 = var1.reader().readInt();
            var28 = var1.reader().readShort();
            int var32 = var1.reader().readInt();
            SkinUpgradeScreen var10000 = SkinUpgradeScreen.instance;
            SkinUpgradeScreen.AA(var29, var28, var32);
            SkinUpgradeScreen.instance.getMenuActionItem();
            SkinUpgradeScreen.instance.AV();
            return;
         }

         if (var2 == 4) {
            String var20 = var1.reader().readUTF();
            idItemUpgrade = var1.reader().readShort();
            mVector var14 = new mVector();
            iCommand var18 = new iCommand(T.CJ, 15, 0, SkinUpgradeScreen.instance);
            var14.addElement(var18);
            iCommand var23 = new iCommand(T.close, 1, 0, this);
            var14.addElement(var23);
            GameCanvas.Start_Normal_DiaLog(var20, var14, false);
            return;
         }

         if (var2 == 3) {
            var2 = var1.reader().readByte();
            var1.reader().readShort();
            var1.reader().readByte();
            String var5 = var1.reader().readUTF();
            SkinUpgradeScreen.instance.showServer = var5;
            if (var2 == 0) {
               SkinUpgradeScreen.instance.Step = 1;
               if (ScreenUpgrade.BT) {
                  SkinUpgradeScreen.AW();
                  return;
               }
            } else {
               SkinUpgradeScreen.instance.Step = 2;
               if (var5.indexOf(T.YJ) >= 0 && ScreenUpgrade.BT) {
                  ScreenUpgrade.BU = true;
                  return;
               }
            }
         } else {
            if (var2 == 5) {
               var1.reader().readByte();
               var12 = false;
               var3 = var1.reader().readByte();
               var4 = new mVector();

               for(var21 = 0; var21 < var3; ++var21) {
                  var6 = var1.reader().readShort();
                  var7 = var1.reader().readUTF();
                  var8 = var1.reader().readUTF();
                  var9 = var1.reader().readShort();
                  var10 = var1.reader().readByte();
                  (var13 = new Class_DW(var6, var9, var7, var8, (short[])null)).CG = false;
                  var13.LvUpgrade = var10;
                  var13.AA(var13.info);
                  if (var13.ID == Player.idFashion) {
                     var13.addInfoFrist(T.daTrangBi, (byte)4);
                     var13.colorName = 4;
                  } else {
                     var13.addInfoFrist(T.IV, (byte)1);
                     var13.colorName = 1;
                  }

                  var4.addElement(var13);
               }

               var3 = var1.reader().readByte();
               var22 = new mVector();

               for(var24 = 0; var24 < var3; ++var24) {
                  var2 = var1.reader().readByte();
                  var26 = var1.reader().readShort();
                  var28 = var1.reader().readShort();
                  var31 = var1.reader().readUTF();
                  var33 = var1.reader().readShort();
                  if (var2 == 4) {
                     (var15 = new Potion(var2, var26, var33, var31, (byte)0)).numPotion = var28;
                     var22.addElement(var15);
                  } else if (var2 == 7) {
                     var17 = (MainMaterial)MainItem.hashMaterialTem.get("" + var26);
                     var17 = new MainMaterial((byte)7, (byte)var26, var17.name, var17.typeMaterial, (byte)var17.idIcon, var28, var17.AF, var17.AJ, var17.BO);
                     var22.addElement(var17);
                  }
               }

               SkinUpgradeScreen.vecSkin = var4;
               SkinUpgradeScreen.vecDa = var22;
               return;
            }

            if (var2 == 6) {
               SkinUpgradeScreen.instance.CF = var1.reader().readByte();
               return;
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public final void CS(Message var1) {
      try {
         byte var2;
         short var4;
         MainItem var8;
         String var15;
         byte var18;
         byte var22;
         MainItem var23;
         byte var24;
         if ((var2 = var1.reader().readByte()) == 0) {
            var1 = var1;

            try {
               if ((var2 = var1.reader().readByte()) != 3 && SplitScreen.instance == null) {
                  SplitScreen.instance = new SplitScreen((byte)0, -1);
               }

               if (var2 == 0) {
                  short var17 = var1.reader().readShort();
                  byte var14 = var1.reader().readByte();
                  var4 = var1.reader().readShort();
                  MainItem var20;
                  if ((var20 = MainItem.getItemVec(var14, var17, Player.vecInventory)) != null) {
                     var22 = var20.colorName;
                     if (var14 != 3) {
                        var22 = 5;
                     }

                     MainItem var25 = new MainItem(var14, var20.ID, var20.idIcon, var4, var22, var20.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[0] = var25;
                     ScreenUpgrade.mItemUpgrade[1] = null;
                     return;
                  }
               } else if (var2 == 1) {
                  var15 = var1.reader().readUTF();
                  SplitScreen.instance.showServer = var15;
                  short var16 = var1.reader().readShort();
                  var18 = var1.reader().readByte();
                  short var21 = var1.reader().readShort();
                  if ((var23 = MainItem.getItemVec(var18, var16, Player.vecInventory)) != null) {
                     var24 = var23.colorName;
                     if (var18 != 3) {
                        var24 = 5;
                     }

                     var8 = new MainItem(var23.typeObject, var23.ID, var23.idIcon, var21, var24, var23.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[1] = var8;
                     ScreenUpgrade.mItemUpgrade[1].CE = true;
                     SplitScreen.instance.Step = 1;
                     return;
                  }
               } else {
                  if (var2 == 3) {
                     (SplitScreen.instance = new SplitScreen((byte)0, -1)).Show((MainScreen)GameCanvas.gameScr);
                     return;
                  }

                  if (var2 == 2) {
                     GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
                     return;
                  }
               }
            } catch (Exception var10) {
            }

            return;
         } else if (var2 == 1) {
            var1 = var1;

            try {
               short var6;
               if ((var2 = var1.reader().readByte()) == 0) {
                  var15 = var1.reader().readUTF();
                  MainItem[] var3 = new MainItem[var18 = var1.reader().readByte()];

                  for(int var19 = 0; var19 < var18; ++var19) {
                     var3[var19] = new MainItem();
                     var6 = var1.reader().readShort();
                     var24 = var1.reader().readByte();
                     short var26 = var1.reader().readShort();
                     MainMaterial var27 = null;
                     if (var24 == 7) {
                        var27 = (MainMaterial)MainItem.hashMaterialTem.get("" + var6);
                     }

                     if (var27 != null) {
                        var22 = var27.colorName;
                        if (var24 != 3) {
                           var22 = 5;
                        }

                        var23 = new MainItem(var24, var27.ID, var27.idIcon, var26, var22, var27.LvUpgrade);
                        var3[var19] = var23;
                     }
                  }

                  if (ScreenJoinItem.instance == null) {
                     ScreenJoinItem.instance = new ScreenJoinItem();
                  }

                  ScreenJoinItem.instance.AA(var15, var3);
                  ScreenJoinItem.instance.Show((MainScreen)GameCanvas.gameScr);
                  return;
               }

               if (var2 == 1) {
                  GameCanvas.end_Dialog();
                  byte var13 = 0;
                  var1.reader().readUTF();
                  var4 = var1.reader().readShort();
                  byte var5;
                  if ((var5 = var1.reader().readByte()) == 7 && var4 == 8) {
                     var13 = 1;
                  }

                  var6 = var1.reader().readShort();
                  MainMaterial var7 = null;
                  var8 = null;
                  if (var5 == 7) {
                     var7 = (MainMaterial)MainItem.hashMaterialTem.get("" + var4);
                  }

                  if (var7 != null) {
                     byte var9 = var7.colorName;
                     if (var5 != 3) {
                        var9 = 5;
                     }

                     var8 = new MainItem(var5, var7.ID, var7.idIcon, var6, var9, var7.LvUpgrade);
                  }

                  if (ScreenJoinItem.instance == null) {
                     ScreenJoinItem.instance = new ScreenJoinItem();
                  }

                  if (var8 != null) {
                     ScreenJoinItem.instance.AA(var8, var13);
                     return;
                  }
               } else if (var2 == 2) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
                  return;
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }

            return;
         }
      } catch (Exception var12) {
      }

   }

   public final void CT(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         byte var3 = var1.reader().readByte();
         if (var2 != 0 && TradeScreen.instance == null) {
            TradeScreen.instance = new TradeScreen((byte)6, 1);
         }

         String var8;
         if (var2 == 0) {
            var8 = var1.reader().readUTF();
            (TradeScreen.instance = new TradeScreen((byte)6, 1)).AA(var8);
            TradeScreen.instance.Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var2 == 1) {
            if ((var2 = var1.reader().readByte()) == 6) {
               int var4 = var1.reader().readInt();
               if (var3 == 0) {
                  TradeScreen.instance.AA.Beri = (long)var4;
                  return;
               }

               if (var3 == 1) {
                  TradeScreen.instance.CQ.Beri = (long)var4;
                  return;
               }
            } else {
               byte var10 = var1.reader().readByte();
               Object var5 = null;
               if (var10 == 1) {
                  if (var2 == 3) {
                     var5 = GE(var1);
                  }

                  if (var2 == 7) {
                     var5 = readUpdateMaterial(var1, false);
                  }

                  if (var2 == 4) {
                     var5 = readUpdatePotion(var1, false, var2);
                  }
               } else if (var10 == 0) {
                  short var7 = var1.reader().readShort();
                  var5 = new MainItem(var2, var7, (short)0, (short)1, (byte)0, (byte)0);
               }

               if (var5 != null) {
                  TradeScreen.instance.AA(var3, var10, (MainItem)var5);
                  return;
               }
            }
         } else if (var2 == 3) {
            if (var3 == 0) {
               TradeScreen.instance.AA.PB = 1;
               TradeScreen.instance.AV();
               return;
            }

            if (var3 == 1) {
               TradeScreen.instance.CQ.PB = 1;
               return;
            }
         } else if (var2 == 4) {
            if (var3 == 0) {
               TradeScreen.instance.AA.PB = 2;
               TradeScreen.instance.AW();
               return;
            }

            if (var3 == 1) {
               TradeScreen.instance.CQ.PB = 2;
               return;
            }
         } else if (var2 == 2) {
            var8 = var1.reader().readUTF();
            if (var3 == 0) {
               TradeScreen.instance.AA.BC = var8;
               return;
            }

            if (var3 == 1) {
               TradeScreen.instance.CQ.BC = var8;
               return;
            }
         } else {
            if (var2 == 5) {
               var8 = var1.reader().readUTF();
               GameCanvas.gameScr.Show();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var8);
               return;
            }

            if (var2 == 6) {
               short var9 = var1.reader().readShort();
               String var11 = var1.reader().readUTF();
               Class_DS.AA(var9, (byte)4, var11, T.eventTrade, 0, 0);
               return;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void CU(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         String var2 = var0.reader().readUTF();
         if (var1 == 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var2);
            short var4 = var0.reader().readShort();
            MainTabShop.BQ = new MainItem((byte)4, (short)-1, var4);
            if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
               MainTabShop.BR = true;
               return;
            }
         } else {
            if (var1 == 1) {
               if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
                  GameCanvas.gameScr.Show();
               }

               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var2);
               return;
            }

            if (var1 == 2) {
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var2);
               return;
            }
         }
      } catch (Exception var3) {
      }

   }

   public final void CV(Message var1) {
      try {
         short var2;
         MainObject var3;
         if ((var3 = MainObject.get_Object((int)(var2 = var1.reader().readShort()), (byte)2)) != null && !var3.isRemove) {
            MainObject.BG = mFont.split(var1.reader().readUTF(), "\b");
            MainObject.BH = 0;
            mVector var5 = new mVector();
            iCommand var6 = new iCommand(T.AK, 3, var2, this);
            var5.addElement(var6);
            GameCanvas.menu.AA(var5, MainObject.BG[0], var2, (byte)2, true, 2, false);
         }
      } catch (Exception var4) {
      }
   }

   public static void ghost(Message var0) {
      try {
         if (var0.reader().readByte() <= 0) {
            Player.isGhost = false;
            GameScreen.addEffectEnd((short)84, 0, GameScreen.player.x, GameScreen.player.y - 70, (byte)GameScreen.player.Dir, GameScreen.player);
            ObjectData.hashImageItemOther.remove("999");
         } else {
            ObjectData.hashImageItemOther.remove("999");
            GameScreen.player.resetAction();
            Player.isGhost = true;
            Player.vecGhostInput.removeAllElements();
            InfoShowNotify var3 = new InfoShowNotify(T.HX, (byte)0);
            mFont var1 = AvMain.AB(0);
            var3.AB(var1);
            Interface_Game.AA(var3);
            ObjectData.getImageAll((short)999, ObjectData.hashImageItemOther, (short)9000);
         }
      } catch (Exception var2) {
      }
   }

   public static void CX(Message var0) {
      try {
         byte var1 = var0.reader().readByte();

         int var2;
         Boat var3;
         for(var2 = 0; var2 < var1; ++var2) {
            AF = (var3 = new Boat(var0.reader().readShort(), var0.reader().readShort(), var0.reader().readShort(), 0, (byte)2)).y;
            byte var4;
            if ((var4 = var0.reader().readByte()) > 0) {
               short[] var7 = new short[var4];

               for(int var5 = 0; var5 < var7.length; ++var5) {
                  var7[var5] = var0.reader().readShort();
               }

               var3.AZ = var7;
            }

            GameScreen.AA(var3, false);
         }

         if (GameScreen.player.boatSea != null) {
            for(var2 = 0; var2 < GameScreen.vecBoat.size(); ++var2) {
               if ((var3 = (Boat)GameScreen.vecBoat.elementAt(var2)).ID == GameScreen.player.boatSea.ID) {
                  GameScreen.AA(var3);
                  break;
               }
            }
         }

         CRes.quickSort(LoadMap.mItemMap[3]);
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public static void Ok_Change_Map_Link() {
      try {
         if (Player.AutoFireCur <= 0 && GameScreen.player.Action == 0) {
            GlobalService.getInstance().OkChangeMapLink();
            return;
         }
      } catch (Exception var0) {
      }

   }

   public static void CY(Message var0) {
      try {
         AT = new byte[var0.reader().available()];
         var0.reader().read(AT);
      } catch (Exception var1) {
      }
   }

   public static void Frist_Login(Message var0) {
      try {
         String text = var0.reader().readUTF();
         (new StringBuffer("CMd -57 team str=")).append(text).toString();
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2;
         (var2 = new DataOutputStream(var1)).writeUTF(text);
         var2.writeByte((byte)GameCanvas.IndexServer);
         CRes.saveRMS("MAIN_frist_login", var1.toByteArray());
         var2.close();
         GameMidlet.AC("MAIN_user_pass");
         GameCanvas.loginScr.AC.AB("");
         GameCanvas.loginScr.AD.AB("");
         GameMidlet.AC("MAIN_user_last");
         SaveRms.userLast = "";
      } catch (Exception var3) {
      }
   }

   public static void DA(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         String var2 = var0.reader().readUTF();
         String[] var3 = new String[var0.reader().readByte()];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = var0.reader().readUTF();
         }

         GameCanvas.subDialog = GameCanvas.AA(var3, var2, var1);
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public static void DB(Message var0) {
      try {
         String var1 = var0.reader().readUTF();
         String var3 = var0.reader().readUTF();
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AA(var1, var3);
         GameMidlet.AC("MAIN_frist_login");
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.IN + "\n" + T.AA + ": " + var1 + "\n" + T.AB + ": " + var3);
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public static void DC(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var5 = var0.reader().readByte();
         if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
            for(int var2 = 0; var2 < GameCanvas.tabShopScr.AB.size(); ++var2) {
               ((MainTab)GameCanvas.tabShopScr.AB.elementAt(var2)).AA(var1, var5);
            }

            return;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void DD(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         int var3 = var0.reader().readInt();
         if (var1 == 0) {
            MainTab.BG.setCountDown(var3);
         }

         if (var1 == 1) {
            MainTab.BI.setCountDown(var3);
         }

         if (var1 == 2) {
            MainTab.BH.setCountDown(var3);
         }

         if (var1 == 3) {
            MainTab.BJ.setCountDown(var3);
            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void DE(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var5;
         if ((var5 = MainObject.get_Object((int)var1, (byte)var2)) != null) {
            short[] var6 = new short[var0.reader().readByte()];

            int var3;
            for(var3 = 0; var3 < var6.length; ++var3) {
               var6[var3] = var0.reader().readShort();
            }

            var5.BO = var6;
            if (var5.boatSea != null && var5.boatSea.ID == var5.ID) {
               var5.boatSea.AA(var6, var5.typePirate);
            }

            if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
               for(var3 = 0; var3 < GameCanvas.tabShopScr.AB.size(); ++var3) {
                  ((MainTab)GameCanvas.tabShopScr.AB.elementAt(var3)).AI();
               }

               return;
            }
         }
      } catch (Exception var4) {
      }

   }

   public static void login_Ok() {
      if (ListChar_Screen.IndexCharSelected >= 0) {
         GlobalService.getInstance().get_DATA((byte)3);
      }

      GameMidlet.loginPlus();
      GameMidlet.loginOk();
      MsgDialog.isAuroReconect = false;
   }

   public static void PvP(Message m) {
      try {
         byte b = m.reader().readByte();
         if (PvPScreen.instance == null) {
            PvPScreen.instance = new PvPScreen();
         }

         String var2;
         if (b == 3) {
            var2 = m.reader().readUTF();
            byte var4 = m.reader().readByte();
            Class_ET var6;
            (var6 = new Class_ET((short)0, (byte)0, var2, 0, 0)).clazz = var4;
            PvPScreen var5;
            (var5 = PvPScreen.instance).AA = var6;
            byte var7 = var5.AA.clazz;
            switch(var7) {
            case 1:
               var5.AA.BS = 0;
               var5.AA.BW = 1;
               var5.AA.BX = -1;
               var5.AA.BT = 3;
               var5.AA.BU = 4;
               var5.AA.BV = -1;
               break;
            case 2:
               var5.AA.BS = 0;
               var5.AA.BW = 24;
               var5.AA.BX = -1;
               var5.AA.BT = 26;
               var5.AA.BU = 27;
               var5.AA.BV = 5;
               break;
            case 3:
               var5.AA.BS = 0;
               var5.AA.BW = 28;
               var5.AA.BX = -1;
               var5.AA.BT = 30;
               var5.AA.BU = 31;
               var5.AA.BV = 180;
               break;
            case 4:
               var5.AA.BS = 0;
               var5.AA.BW = 32;
               var5.AA.BX = -1;
               var5.AA.BT = 34;
               var5.AA.BU = 35;
               var5.AA.BV = 6;
               break;
            case 5:
               var5.AA.BS = 0;
               var5.AA.BW = 36;
               var5.AA.BX = -1;
               var5.AA.BT = 38;
               var5.AA.BU = 39;
               var5.AA.BV = 7;
            }
         } else if (b == 4) {
            if (m.reader().readByte() == 0) {
               PvPScreen.instance.AB = true;
            } else {
               PvPScreen.instance.AC = true;
            }
         } else if (b == 6) {
            var2 = m.reader().readUTF();
            PvPScreen.instance.AA(var2);
         } else if (b == 0) {
            PvPScreen.instance.AD = m.reader().readShort();
         }

         PvPScreen.instance.AA(b);
      } catch (Exception var3) {
      }
   }

   public static void DG(Message var0) {
      try {
         MainObject var1;
         if ((var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)2)) != null) {
            var1.BF = var0.reader().readUTF();
            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void DH(Message var0) {
      try {
         String var2 = var0.reader().readUTF();
         MsgDialog.AA((MainEffect)(new Class_BN(var2, 0, 0, 7)));
      } catch (Exception var1) {
      }
   }

   public static void DI(Message var0) {
      try {
         Player.giamCountDownParty = 0;
         GameScreen.player.AG.removeAllElements();
         byte var1 = var0.reader().readByte();

         for(int var2 = 0; var2 < var1; ++var2) {
            MainInfoItem var3 = new MainInfoItem(var0.reader().readByte(), var0.reader().readShort());
            GameScreen.player.AG.addElement(var3);
            if (var3.AA == 25) {
               Player.giamCountDownParty = 0;
            }
         }

         Player.SetGiamCountDown();
      } catch (Exception var4) {
      }
   }

   public static void DJ(Message var0) {
      try {
         GameScreen.player.KQ = var0.reader().readInt();
         GameScreen.player.BP[0] = var0.reader().readInt();
         GameScreen.player.BP[1] = var0.reader().readInt();
      } catch (Exception var1) {
      }
   }

   public static void DK(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0 || SplitScreen.instance != null) {
            if (var1 == 0) {
               byte var16 = var0.reader().readByte();
               (SplitScreen.instance = new SplitScreen(var16, -1)).Show((MainScreen)GameCanvas.gameScr);
            } else {
               byte var3;
               short var4;
               short var9;
               byte var11;
               MainItem var14;
               if (var1 == 1) {
                  var11 = 0;
                  var9 = var0.reader().readShort();
                  var3 = var0.reader().readByte();
                  if ((SplitScreen.instance.BG == 1 || SplitScreen.instance.BG == 10 || SplitScreen.instance.BG == 11) && var3 != 3) {
                     var11 = 1;
                  }

                  var4 = var0.reader().readShort();
                  MainItem var8;
                  if ((var8 = MainItem.getItemVec(var3, var9, Player.vecInventory)) != null) {
                     var1 = var8.colorName;
                     if (var3 != 3) {
                        var1 = 5;
                     }

                     (var14 = new MainItem(var3, var8.ID, var8.idIcon, var4, var1, var8.LvUpgrade)).numLoKham = var8.numLoKham;
                     ScreenUpgrade.mItemUpgrade[var11] = var14;
                  }

                  SplitScreen.instance.AV();
               } else if (var1 == 2) {
                  short var15 = var0.reader().readShort();
                  var1 = var0.reader().readByte();

                  for(int var13 = 0; var13 < ScreenUpgrade.mItemUpgrade.length; ++var13) {
                     if (ScreenUpgrade.mItemUpgrade[var13].ID == var15 && ScreenUpgrade.mItemUpgrade[var13].typeObject == var1) {
                        ScreenUpgrade.mItemUpgrade[var13] = null;
                     }
                  }

               } else if (var1 == 3) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var0.reader().readUTF());
               } else {
                  String var2;
                  if (var1 == 4) {
                     var2 = var0.reader().readUTF();
                     SplitScreen.instance.AK();
                     SplitScreen.instance.showServer = var2;
                     SplitScreen.instance.Step = 1;
                  } else {
                     byte var7;
                     MainItem var10;
                     if (var1 == 5) {
                        var2 = var0.reader().readUTF();
                        SplitScreen.instance.AK();
                        var0.reader().readShort();
                        short var12 = var0.reader().readShort();
                        var4 = var0.reader().readShort();
                        if ((var10 = MainItem.getItemVec(var7 = var0.reader().readByte(), var12, Player.vecInventory)) != null && var4 > 0) {
                           (var14 = new MainItem(var7, var10.ID, var10.idIcon, var4, var10.colorName, var10.LvUpgrade)).CE = true;
                           ScreenUpgrade.mItemUpgrade[1] = var14;
                        }

                        SplitScreen.instance.showServer = var2;
                        SplitScreen.instance.Step = 1;
                     } else if (var1 == 6) {
                        var2 = var0.reader().readUTF();
                        SplitScreen.instance.AK();
                        SplitScreen.instance.showServer = var2;
                        SplitScreen.instance.Step = 1;
                     } else if (var1 == 7) {
                        var2 = var0.reader().readUTF();
                        SplitScreen.instance.AK();
                        SplitScreen.instance.showServer = var2;
                        SplitScreen.instance.Step = 1;
                     } else if (var1 != 20 && var1 != 21 && var1 != 22 && var1 != 23) {
                        if (var1 == 25) {
                           var2 = var0.reader().readUTF();
                           SplitScreen.instance.AK();
                           SplitScreen.instance.showServer = var2;
                           SplitScreen.instance.Step = 1;
                        } else if (var1 != 28 && var1 != 29) {
                           if (var1 == 25) {
                              var2 = var0.reader().readUTF();
                              SplitScreen.instance.AK();
                              SplitScreen.instance.showServer = var2;
                              SplitScreen.instance.Step = 1;
                           } else if (var1 == 27 || var1 == 30) {
                              var2 = var0.reader().readUTF();
                              SplitScreen.instance.AK();
                              SplitScreen.instance.showServer = var2;
                              if (var1 == 30) {
                                 SplitScreen.instance.Step = 3;
                              } else {
                                 SplitScreen.instance.Step = 1;
                              }
                           } else if (var1 == 31) {
                              var2 = var0.reader().readUTF();
                              SplitScreen.instance.CQ = var0.reader().readShort();
                              SplitScreen.instance.AK();
                              SplitScreen.instance.showServer = var2;
                              SplitScreen.instance.Step = 1;
                           } else if (var1 == 32) {
                              var2 = var0.reader().readUTF();
                              SplitScreen.instance.AK();
                              SplitScreen.instance.showServer = var2;
                              SplitScreen.instance.Step = 3;
                           }
                        } else {
                           var11 = 0;
                           if (var1 == 29) {
                              var11 = 1;
                           }

                           var9 = var0.reader().readShort();
                           var3 = var0.reader().readByte();
                           var4 = var0.reader().readShort();
                           var7 = var0.reader().readByte();
                           if ((var10 = MainItem.getItemVec(var3, var9, Player.vecInventory)) != null) {
                              byte var5 = var10.colorName;
                              if (var3 != 3) {
                                 var5 = 5;
                              }

                              var10 = new MainItem(var3, var10.ID, var10.idIcon, var4, var5, var10.LvUpgrade);
                              ScreenUpgrade.mItemUpgrade[var11] = var10;
                           }

                           SplitScreen.instance.CS = var7;
                        }
                     } else {
                        var2 = var0.reader().readUTF();
                        SplitScreen.instance.AK();
                        SplitScreen.instance.showServer = var2;
                        if (var1 != 20 && var1 != 22) {
                           SplitScreen.instance.Step = 3;
                        } else {
                           SplitScreen.instance.Step = 1;
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public static void DL(Message var0) {
      try {
         String var1 = var0.reader().readUTF();
         short var3 = var0.reader().readShort();
         GameCanvas.AA(var1, true, var3, (mVector)null);
      } catch (Exception var2) {
      }
   }

   public static void DM(Message var0) {
      try {
         LoadMap.AZ = var0.reader().readByte();
         LoadMap.BA = var0.reader().readByte();
      } catch (Exception var1) {
      }
   }

   public static void Auto_Revice(Message m) {
      try {
         if (m.reader().readByte() == 1) {
            short id = m.reader().readShort();
            byte tem = m.reader().readByte();
            int timeSafe = m.reader().readInt();
            MainObject mainObject;
            if ((mainObject = MainObject.get_Object((int)id, (byte)tem)) != null) {
               if (mainObject == GameScreen.player && Player.AutoFireCur == 0) {
                  Player.AutoFireCur = 1;
               }

               mainObject.timeSafe = timeSafe;
               mainObject.timeBeginSafe = GameCanvas.timeNow;
               return;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void DO(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         AG = var0.reader().readShort();
         String var7;
         if (var1 == 0) {
            ListDungeon.AB = false;
            ListDungeon.AA.removeAllElements();
            var7 = var0.reader().readUTF();
            byte var8 = var0.reader().readByte();

            for(int var9 = 0; var9 < var8; ++var9) {
               short var4 = var0.reader().readShort();
               Class_DS var10;
               (var10 = new Class_DS(var4)).AA(var0.reader().readUTF(), var0.reader().readShort(), (byte)0, (byte)0);
               if (var9 == 0) {
                  var10.AP = 1;
                  if (var10.AG == GameScreen.player.ID) {
                     ListDungeon.AB = true;
                  }
               }

               ListDungeon.AA.addElement(var10);
            }

            (ListDungeon.instance = new ListDungeon((byte)-4, ListDungeon.AA, var7)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         int var2;
         Class_DS var3;
         short var6;
         if (var1 == 1) {
            var6 = var0.reader().readShort();
            if (ListDungeon.AA != null) {
               for(var2 = 0; var2 < ListDungeon.AA.size(); ++var2) {
                  if ((var3 = (Class_DS)ListDungeon.AA.elementAt(var2)).AG == var6) {
                     var3.AP = 1;
                     return;
                  }
               }

               return;
            }
         } else {
            if (var1 == 2) {
               var7 = var0.reader().readUTF();
               ListDungeon.AA.removeAllElements();
               if (GameCanvas.currentScreen != GameCanvas.gameScr) {
                  GameCanvas.gameScr.Show();
               }

               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var7);
               return;
            }

            if (var1 == 3) {
               var6 = var0.reader().readShort();
               if (ListDungeon.AA != null) {
                  for(var2 = 0; var2 < ListDungeon.AA.size(); ++var2) {
                     if ((var3 = (Class_DS)ListDungeon.AA.elementAt(var2)).AG == var6) {
                        var3.AP = 2;
                        return;
                     }
                  }

                  return;
               }
            }
         }
      } catch (Exception var5) {
      }

   }

   public static void DP(Message var0) {
      try {
         byte var1;
         int var2;
         byte[] var3;
         if ((var1 = var0.reader().readByte()) == 0) {
            var1 = var0.reader().readByte();
            MapOff_RedLine.setTypeMoveredLine((byte)0);
            AH = new mImage[var1];

            for(var2 = 0; var2 < var1; ++var2) {
               var3 = new byte[var0.reader().readInt()];
               var0.reader().read(var3);
               AH[var2] = mImage.AA(var3);
            }

            MapOff_RedLine.AI = var0.reader().readInt();
            MapOff_RedLine.AK = GameCanvas.timeNow;
            Player.RC = -1;
            MapOff_RedLine.AJ = 0;
            return;
         }

         if (var1 == 1) {
            AH = null;
            Interface_Game.addInfoPlayerNormal(T.MJ, mFont.tahoma_7_white);
            Player.RC = -1;
            MapOff_RedLine.setTypeMoveredLine((byte)1);
            return;
         }

         if (var1 == 2) {
            AH = null;
            Interface_Game.addInfoPlayerNormal(T.MK, mFont.tahoma_7_white);
            Player.RC = -1;
            MapOff_RedLine.setTypeMoveredLine((byte)2);
            return;
         }

         if (var1 == 3) {
            var1 = var0.reader().readByte();
            GameScreen.player.SH = 1;
            AH = new mImage[var1];

            for(var2 = 0; var2 < var1; ++var2) {
               var3 = new byte[var0.reader().readInt()];
               var0.reader().read(var3);
               AH[var2] = mImage.AA(var3);
            }

            MapOff_RedLine.AI = var0.reader().readInt();
            MapOff_RedLine.AK = GameCanvas.timeNow;
            Player.RC = -1;
            MapOff_RedLine.AJ = 0;
            return;
         }

         if (var1 == 5) {
            AH = null;
            Interface_Game.addInfoPlayerNormal(T.QM, mFont.tahoma_7_white);
            Player.RC = -1;
            if (LoadMap.specMap == 8) {
               MapGotoSky.AA((byte)3);
               return;
            }

            if (LoadMap.specMap == 12) {
               MapGotoGod.AA((byte)3);
               return;
            }
         } else if (var1 == 4) {
            AH = null;
            Interface_Game.addInfoPlayerNormal(T.QN, mFont.tahoma_7_white);
            Player.RC = -1;
            MapGotoSky.AA((byte)2);
            return;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void DQ(Message var0) {
      try {
         byte var1;
         short var2;
         String var5;
         if ((var1 = var0.reader().readByte()) == 0 || var1 == 2) {
            var2 = var0.reader().readShort();
            var5 = var0.reader().readUTF();
            Interface_Game.BL.setCountDown(var2);
            Interface_Game.BL.strInfo = var5;
            Interface_Game.BL.typeTime = var1;
            return;
         }

         if (var1 == 1) {
            Interface_Game.CL = var0.reader().readInt();
            Interface_Game.CK = var0.reader().readInt();
            return;
         }

         if (var1 == 3) {
            var2 = var0.reader().readShort();
            var5 = var0.reader().readUTF();
            Interface_Game.BM.setCountDown(var2);
            Interface_Game.BM.strInfo = var5;
            Interface_Game.BM.typeTime = var1;
            return;
         }

         byte var3;
         if (var1 == 4) {
            Interface_Game.BL.typeTime = var1;
            var2 = var0.reader().readShort();
            var3 = var0.reader().readByte();
            var1 = var0.reader().readByte();
            Interface_Game.BL.setCountDown(var2);
            Interface_Game.BL.valueLeft = var3;
            Interface_Game.BL.valueright = var1;
            return;
         }

         if (var1 == 5) {
            if (MotherCanvas.w >= 300) {
               GameScreen.isPvPNew = true;
               Interface_Game.BL.typeTime = var1;
            } else {
               Interface_Game.BL.typeTime = 4;
            }

            var2 = var0.reader().readShort();
            var3 = var0.reader().readByte();
            var1 = var0.reader().readByte();
            Interface_Game.BL.setCountDown(var2);
            Interface_Game.BL.valueLeft = var3;
            Interface_Game.BL.valueright = var1;
            Interface_Game.BV = var0.reader().readByte();
            return;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void DR(Message var0) {
      try {
         Player.idFashion = var0.reader().readShort();
         Player.giamCountDownAtt = var0.reader().readShort();
         Player.SetGiamCountDown();
         Player.RX = var0.reader().readShort();
      } catch (Exception var1) {
      }
   }

   public final void DS(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) == 0) {
            if (GameScreen.player.clan == null) {
               GameScreen.player.clan = new MainClan(var1.reader().readShort(), var1.reader().readUTF());
               return;
            }

            GameScreen.player.clan.ID = var1.reader().readShort();
            GameScreen.player.clan.name = var1.reader().readUTF();
            return;
         }

         Class_DS var4;
         String var14;
         if (var2 == 1) {
            if (GameScreen.player.clan == null) {
               GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
               return;
            }

            var14 = var1.reader().readUTF();
            byte var3 = var1.reader().readByte();
            if ((var4 = Clan_Screen.AA(var14)) != null) {
               var4.AS = var3;
               if (var4.AC.compareTo(GameScreen.player.name) == 0) {
                  Player.QN = var3;
                  return;
               }
            }
         } else {
            short var5;
            short var27;
            if (var2 == 2) {
               if (GameScreen.player.clan == null) {
                  GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
                  return;
               }

               MainClan var10000 = GameScreen.player.clan;
               short var10001 = var1.reader().readShort();
               String var10002 = var1.reader().readUTF();
               short var10003 = var1.reader().readShort();
               int var10004 = var1.reader().readInt();
               int var10005 = var1.reader().readInt();
               byte var10006 = var1.reader().readByte();
               byte var10007 = var1.reader().readByte();
               int var10008 = var1.reader().readInt();
               String var11 = var1.reader().readUTF();
               int var10 = var10008;
               byte var9 = var10007;
               byte var8 = var10006;
               int var7 = var10005;
               int var6 = var10004;
               var5 = var10003;
               String var24 = var10002;
               var27 = var10001;
               MainClan var25 = var10000;
               var10000.idIcon = var27;
               var25.AB = var24;
               var25.AD = var5;
               var25.AG = var6;
               var25.AH = var7;
               var25.AE = var8;
               var25.AF = var9;
               var25.AI = var10;
               var11 = GameMidlet.AE(var11);
               var25.AC = var11;
               if (Player.isGetDataClan == 0) {
                  if (GameCanvas.ClanScr == null) {
                     GameCanvas.ClanScr = new Clan_Screen(GameScreen.player.clan);
                  }

                  GameCanvas.ClanScr.Show((MainScreen)GameCanvas.gameScr);
                  Player.isGetDataClan = 1;
               }

               GameScreen.player.clan.AS = var1.reader().readByte();
               GameScreen.player.clan.AO = var1.reader().readInt();
               return;
            }

            int var18;
            if (var2 == 3) {
               if (GameScreen.player.clan == null) {
                  GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
                  return;
               }

               var2 = var1.reader().readByte();

               for(var18 = 0; var18 < var2; ++var18) {
                  GN(var1);
               }

               Class_FK.AA = 10;
               return;
            }

            short var20;
            if (var2 == 4) {
               if (GameScreen.player.clan == null) {
                  GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
                  return;
               }

               var20 = var1.reader().readShort();
               var27 = var1.reader().readShort();
               short[] var22 = new short[5];

               for(int var23 = 0; var23 < var22.length; ++var23) {
                  var22[var23] = var1.reader().readShort();
               }

               GameScreen.player.clan.AK = var27;
               GameScreen.player.clan.AL = var20;
               GameScreen.player.clan.AX = var22;
               return;
            }

            MainObject var16;
            if (var2 == 5) {
               if ((var16 = MainObject.get_Object((int)var1.reader().readShort(), (byte)0)) != null) {
                  short var19 = var1.reader().readShort();
                  var5 = var1.reader().readShort();
                  var2 = var1.reader().readByte();
                  if (var16.clan == null) {
                     var16.clan = new MainClan(var19, var5, var2);
                  } else {
                     var16.clan.ID = var19;
                     var16.clan.idIcon = var5;
                     var16.clan.AJ = var2;
                  }

                  if (var16 == GameScreen.player) {
                     Player.QN = var2;
                  }

                  byte var13 = var1.reader().readByte();
                  var16.clan.AT = var13;
               }

               if (var16.boatSea != null) {
                  var16.boatSea.AA();
                  return;
               }
            } else {
               if (var2 == 8) {
                  if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                     AA(var1, (int)0);
                     GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AP);
                     return;
                  }

                  return;
               }

               int var15;
               if (var2 == 7) {
                  var15 = var1.reader().readInt();
                  String var26 = var1.reader().readUTF();
                  Class_DS.AA(var15, (byte)6, var26, T.NO, 0, 0);
                  return;
               }

               if (var2 == 9) {
                  if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                     GameScreen.player.clan.vecChatClan.removeAllElements();
                     var20 = var1.reader().readShort();

                     for(var18 = 0; var18 < var20; ++var18) {
                        AA(var1, (int)0);
                     }

                     return;
                  }

                  return;
               }

               if (var2 == 11) {
                  if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                     var20 = var1.reader().readShort();
                     GameCanvas.ClanScr.AP.AA(var20);
                     return;
                  }

                  return;
               }

               if (var2 == 10) {
                  if ((var16 = MainObject.get_Object((int)var1.reader().readShort(), (byte)0)) != null && var16.clan != null) {
                     var16.clan = null;
                  }

                  if (var16 == GameScreen.player && GameCanvas.currentScreen == GameCanvas.ClanScr) {
                     GameCanvas.gameScr.Show();
                     GameCanvas.ClanScr = null;
                     Player.isGetDataClan = -1;
                     return;
                  }
               } else {
                  if (var2 == 12) {
                     if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                        if ((var2 = var1.reader().readByte()) == 0) {
                           GN(var1);
                        } else if (var2 == 1) {
                           Clan_Screen.AB(var1.reader().readUTF());
                        }

                        GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AQ);
                        return;
                     }

                     return;
                  }

                  if (var2 == 13) {
                     var15 = var1.reader().readInt();
                     MainTab.BK.setCountDown(var15);
                     var18 = var1.reader().readInt();
                     if ((var4 = Clan_Screen.AA(GameScreen.player.ID)) != null) {
                        var4.AL = var18;
                        return;
                     }
                  } else {
                     if (var2 == 14) {
                        if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                           var14 = var1.reader().readUTF();
                           GameScreen.player.clan.AC = var14;
                           GameCanvas.ClanScr.AR.AA(var14, GameCanvas.ClanScr.AR.AH - 6);
                           GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AR);
                           return;
                        }

                        return;
                     }

                     if (var2 == 15) {
                        short[] var17 = new short[5];

                        for(var18 = 0; var18 < 5; ++var18) {
                           var17[var18] = var1.reader().readShort();
                        }

                        for(var18 = 0; var18 < Player.QF.length; ++var18) {
                           Class_CV var21 = Player.QF[var18];
                           if (var18 < var17.length) {
                              var21.AB = var17[var18];
                           }
                        }

                        return;
                     }

                     if (var2 == 16) {
                        if (GameScreen.player.clan == null) {
                           return;
                        }

                        GameScreen.player.clan.AR = var1.reader().readByte();
                        GameScreen.player.clan.AG = var1.reader().readInt();
                        return;
                     }

                     if (var2 == 17) {
                        if (GameScreen.player.clan == null) {
                           return;
                        }

                        GameScreen.player.clan.AM = var1.reader().readInt();
                        GameScreen.player.clan.AN = var1.reader().readInt();
                        return;
                     }

                     if (var2 == 19) {
                        this.update_Inven_Clan(var1);
                        return;
                     }

                     if (var2 == 20) {
                        if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                           GameCanvas.ClanScr.AS.AN.removeAllElements();

                           while(var1.reader().available() > 0) {
                              AA(var1, (int)1);
                           }

                           GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AS);
                           return;
                        }

                        return;
                     }

                     if (var2 == 21) {
                        var14 = var1.reader().readUTF();
                        if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
                           GameCanvas.gameScr.Show();
                        }

                        GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var14);
                        return;
                     }
                  }
               }
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   private static void GN(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         String var2;
         Class_DS var3 = Clan_Screen.AA(var2 = var0.reader().readUTF());
         boolean var4 = false;
         if (var3 == null) {
            var3 = new Class_DS(var1);
            var4 = true;
         }

         short var10002 = var0.reader().readShort();
         byte var10003 = var0.reader().readByte();
         short var10004 = var0.reader().readShort();
         short var10005 = var0.reader().readShort();
         short var10006 = var0.reader().readShort();
         int var11 = var0.reader().readInt();
         short var10 = var10006;
         short var9 = var10005;
         short var8 = var10004;
         byte var7 = var10003;
         short var6 = var10002;
         var3.AC = var2;
         var3.AK = var6;
         var3.AS = var7;
         var3.AL = var8;
         var3.BA = var9;
         var3.AM = var10;
         var3.BB = var11;
         var3.AA(var0.reader().readShort(), var0.reader().readShort(), var0.reader().readShort());
         var3.AP = var0.reader().readByte();
         if (var4) {
            GameScreen.player.clan.vecMem.addElement(var3);
         }

         if (var2.compareTo(GameScreen.player.name) == 0) {
            Player.QN = var3.AS;
         }

         if (GameCanvas.ClanScr != null) {
            GameCanvas.ClanScr.AQ.AB();
            return;
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   private static void AA(Message var0, int var1) {
      try {
         byte var2 = var0.reader().readByte();
         short var3 = var0.reader().readShort();
         short var4 = var0.reader().readShort();
         String var5 = var0.reader().readUTF();
         String var6 = var0.reader().readUTF();
         long var7 = var0.reader().readLong() - GameCanvas.clockServer;
         short var10001;
         short var10002;
         short var20;
         short var21;
         String var22;
         String var23;
         Class_DS var24;
         if (var1 == 0) {
            var10001 = var3;
            var10002 = var4;
            byte var14 = var2;
            var23 = var6;
            var22 = var5;
            var21 = var10002;
            var20 = var10001;
            Class_FH var18 = GameCanvas.ClanScr.AP;
            var24 = Clan_Screen.AA(var5);
            var6 = "";
            if (var24 != null) {
               var6 = Class_DS.AA(var24.AS);
            }

            if (var14 == -1) {
               if (var21 == GameScreen.player.ID) {
                  var18.AA(var20, var23, var22, var6, (byte)1, (byte)-1, var21, var7);
               } else {
                  var18.AA(var20, var23, var22, var6, (byte)0, (byte)-1, var21, var7);
               }
            } else if (var14 == 1) {
               var18.AA(var20, var23, var22, "", (byte)3, (byte)1, var21, var7);
            } else if (var14 == -2) {
               var18.AA(var20, var23, var22, "", (byte)2, (byte)-1, var21, var7);
            } else {
               if (var14 != -3) {
                  if (var14 == -4) {
                     var18.AA(var20, var23, var22, var6, (byte)5, (byte)-1, var21, var7);
                  }

                  return;
               }

               var18.AA(var20, var23, T.NG, var22, (byte)3, (byte)-1, var21, var7);
            }
         } else if (var1 == 1) {
            var10001 = var3;
            var10002 = var4;
            var23 = var6;
            var22 = var5;
            var21 = var10002;
            var20 = var10001;
            Class_FI var19 = GameCanvas.ClanScr.AS;
            var24 = Clan_Screen.AA(var5);
            var6 = "";
            if (var24 != null) {
               var6 = Class_DS.AA(var24.AS);
            }

            var19.AA(var20, var23, var22, var6, (byte)9, (byte)-1, var21, var7);
            return;
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      }

   }

   public final void DT(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) != 0 && SplitScreen.instance == null) {
            SplitScreen.instance = new SplitScreen((byte)7, -1);
         }

         if (var2 == 0) {
            (SplitScreen.instance = new SplitScreen((byte)7, -1)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var2 == 1) {
            var2 = var1.reader().readByte();
            short var5 = var1.reader().readShort();
            MainItem var3;
            if ((var3 = MainItem.getItemVec((byte)3, var5, Player.vecInventory)) != null) {
               if (var2 == 0) {
                  AU = var5;
               } else if (var2 == 1) {
                  AV = var5;
               }

               MainItem var6 = new MainItem((byte)3, var3.ID, var3.idIcon, (short)1, var3.colorName, var3.LvUpgrade);
               ScreenUpgrade.mItemUpgrade[var2] = var6;
               return;
            }
         } else {
            if (var2 == 3) {
               String var9 = var1.reader().readUTF();
               SplitScreen.instance.showServer = var9;
               AI = var1.reader().readByte();
               SplitScreen.instance.Step = 1;
               return;
            }

            if (var2 == 2) {
               SplitScreen.instance.CR = var1.reader().readShort();
               mVector var8 = new mVector();
               iCommand var7;
               (var7 = new iCommand(String.valueOf(SplitScreen.instance.CR), 4, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 1, (int)0);
               var8.addElement(var7);
               iCommand var10 = new iCommand(T.close, 1, 0, this);
               var8.addElement(var10);
               GameCanvas.Start_Normal_DiaLog(T.NT + SplitScreen.instance.CR + " " + T.EW + "?", var8, false);
               return;
            }
         }
      } catch (Exception var4) {
      }

   }

   public static void DU(Message var0) {
      try {
         byte var1;
         int var2;
         BigBossLittleGraden var3;
         int var7;
         if ((var1 = var0.reader().readByte()) == 0) {
            var1 = var0.reader().readByte();
            var2 = var0.reader().readInt();
            var7 = var0.reader().readInt();
            (var3 = new BigBossLittleGraden(var1)).AE = var2;
            var3.AG = var7;
            GameScreen.vecBigBossLittleGraden.addElement(var3);
            return;
         }

         if (var1 == 1) {
            var1 = var0.reader().readByte();
            var2 = var0.reader().readInt();
            var7 = var0.reader().readInt();

            for(int var8 = 0; var8 < GameScreen.vecBigBossLittleGraden.size(); ++var8) {
               BigBossLittleGraden var4;
               if ((var4 = (BigBossLittleGraden)GameScreen.vecBigBossLittleGraden.elementAt(var8)).AA == var1) {
                  var4.AD = var2;
                  var4.AF = var7;
                  return;
               }
            }

            return;
         }

         if (var1 == 2) {
            var1 = var0.reader().readByte();
            var2 = var0.reader().readInt();

            for(var7 = 0; var7 < GameScreen.vecBigBossLittleGraden.size(); ++var7) {
               if ((var3 = (BigBossLittleGraden)GameScreen.vecBigBossLittleGraden.elementAt(var7)).AA == var1) {
                  var3.AA((byte)1);
                  var3.AH = var2;
                  return;
               }
            }

            return;
         }

         if (var1 == 3) {
            var1 = var0.reader().readByte();

            for(var2 = 0; var2 < GameScreen.vecBigBossLittleGraden.size(); ++var2) {
               BigBossLittleGraden var6;
               if ((var6 = (BigBossLittleGraden)GameScreen.vecBigBossLittleGraden.elementAt(var2)).AA == var1) {
                  var6.AA((byte)3);
                  return;
               }
            }

            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void Pet(Message m) {
      try {
         byte b;
         short id;
         if ((b = m.reader().readByte()) == 0) {
            id = m.reader().readShort();
            short num = m.reader().readShort();
            short idImage = m.reader().readShort();
            byte type = m.reader().readByte();
            MainObject mainObject;
            if ((mainObject = MainObject.getPet(num)) == null) {
               GameScreen.addPlayer((MainObject)(new Pet(id, num, idImage, type)));
            } else {
               mainObject.setDataPet(id, idImage, type);
            }
         } else if (b == 1) {
            m.reader().readShort();
            MainObject mainObject2;
            if ((mainObject2 = MainObject.getPet(m.reader().readShort())) != null) {
               mainObject2.isRemove = true;
            }
         }
         if (b == 3) {
            id = m.reader().readShort();
            mVector mVector = new mVector();

            for(int i = 0; i < id; ++i) {
               short id2 = m.reader().readShort();
               String name = m.reader().readUTF();
               String info = m.reader().readUTF();
               short idIcon = m.reader().readShort();
               byte type    = m.reader().readByte();
               byte isUse = m.reader().readByte();
               byte upgrade = m.reader().readByte();
               MainItem mainItem;
               (mainItem = new MainItem(type, id2, idIcon, name, (byte)0, upgrade)).name = name;
               mainItem.info = info;
               mainItem.LvUpgrade = (byte)upgrade;
               if (mainItem.LvUpgrade > 0) {
                   mainItem.colorName = 5;
                   mainItem.namepaint = mainItem.name + " +" + mainItem.LvUpgrade;
               } else {
                   mainItem.namepaint = mainItem.name;
               }
               if (isUse == 1) {
                  mainItem.addInfoFrist(T.daTrangBi, (byte)1);
                  mainItem.colorName = 1;
               }
               b = m.reader().readByte();

               for(int k = 0; k < b; ++k) {
                  byte var19 = m.reader().readByte();
                  short var20 = m.reader().readShort();
                  mainItem.addInfo(var19, var20, (byte)infoShow.HARDCODE_INFO_CO_BAN, (byte)1);
               }

               mVector.addElement(mainItem);
            }
            GameCanvas.tabShopScr = new TabScreen(MainTab.xTab, (byte)0);
            mVector mVector2 = new mVector();
            GameCanvas.tabShopScr.isShopClan = false;
            (GameCanvas.tabInvenClan = new TabInventory(T.khoPet, mVector, (byte)7, MainTab.xTab)).initCmd();
            mVector2.addElement(GameCanvas.tabInvenClan);
            GameCanvas.tabShopScr.addVecTab(mVector2);
            GameCanvas.tabShopScr.idSelect = 0;
            GameCanvas.tabShopScr.Show((MainScreen)GameCanvas.gameScr);
            GameCanvas.tabShopScr.typeCurrent = 1;
            GameCanvas.tabShopScr.setTabSelect();
            return;
         }

         if (b == 4) {
            id = m.reader().readShort();
            (new StringBuffer("action use idIcon ")).append(id).toString();
            GameCanvas.tabInvenClan.AA(id);
            return;
         }
      } catch (Exception var9) {
      }

   }

   public static void DW(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         String var2 = var0.reader().readUTF();
         String[] var3 = new String[var0.reader().readByte()];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = var0.reader().readUTF();
         }

         GameCanvas.currentDialog = GameCanvas.AA(var3, var2, var1);
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public static void DX(Message var0) {
      try {
         byte var2 = var0.reader().readByte();
         FristLoginScreen.AC = new iCommand(T.CX, 3, GameCanvas.fristLoginScr);
         (FristLoginScreen.AB = new InputDialog()).AA(T.WQ, T.AZ, (short)0, FristLoginScreen.AC, T.PF);
         if (var2 == 1) {
            FristLoginScreen.AB.AA(T.WR);
         }

         GameCanvas.AB(FristLoginScreen.AB);
      } catch (Exception var1) {
      }
   }

   public final void ChangeMapNonData(Message var1) {
      try {
         actionChangeMap = 0;
         GameCanvas.loadmap.idLastMap = GameCanvas.loadmap.idMapLoadMap;
         short var2;
         if (setMapSea(var2 = var1.reader().readShort())) {
            this.idMapLuu = var2;
            this.msgLuu = var1;
            isNondata = true;
         } else {
            this.readChangeMapNonData(var1, var2);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public final void readChangeMapNonData(Message m, short idMap) {
      try {
         GameCanvas.loadMapScr.Show();
         GameScreen.RemoveLoadMap();
         DataMap dataMap;
         if ((dataMap = (DataMap)DataMap.hashDataMap.get("" + idMap)) == null) {
            GlobalService.getInstance().NextMap(idMap);
            return;
         }

         GameCanvas.loadmap.idMapLoadMap = idMap;
         Interface_Game.nameMap = "";
         GameCanvas.loadMapScr.area = m.reader().readByte();
         byte typeViewPlayer = m.reader().readByte();
         GameScreen.player.posTransRoad = null;
         GameScreen.player.x = m.reader().readShort();
         GameScreen.player.y = m.reader().readShort();
         GameScreen.player.toX = GameScreen.player.x;
         GameScreen.player.toY = GameScreen.player.y;
         GameScreen.player.maxHp = m.reader().readInt();
         GameScreen.player.Hp = m.reader().readInt();
         GameScreen.player.maxMp = m.reader().readInt();
         GameScreen.player.Mp = m.reader().readInt();
         byte var4 = m.reader().readByte();
         if (LoadMap.specMap == 3) {
            GameScreen.player.isBeginTrain = false;
         } else if (LoadMap.specMap == 4) {
            SaveRms var10000 = GameCanvas.saveRms;
            SaveRms.loadHotKey(SaveRms.datahotKeySkill);
         }

         if ((LoadMap.specMap = m.reader().readByte()) == 1 && check_IDMap_PVP()) {
            GameScreen.tickPvP = 60;
         }

         int var5;
         if (var4 == 1) {
            LoadMap.isOnlineMap = true;
            GameCanvas.loadmap.loadmap(dataMap.data);
            GameCanvas.loadMapScr.mItemMap = dataMap.dataItem;
            LoadMap.vecPointChange.removeAllElements();
            if (GameCanvas.loadmap.idMapLoadMap != 157 && GameCanvas.loadmap.idMapLoadMap != 159 && GameCanvas.loadmap.idMapLoadMap != 161) {
               for(var5 = 0; var5 < dataMap.vecPointMap.size(); ++var5) {
                  Point var6 = (Point)dataMap.vecPointMap.elementAt(var5);
                  LoadMap.vecPointChange.addElement(var6);
               }
            }
         }

         if (var4 == 0) {
            LoadMap.isOnlineMap = false;
            if (LoadMap.specMap == 5) {
               LoadRedLine(false);
            } else if (LoadMap.specMap == 8) {
               MapGotoSky.setPos();
            } else if (LoadMap.specMap == 12) {
               MapGotoGod.setPos();
            }
         }

         LoadMapScreen.IDBack = dataMap.IDBack;
         LoadMapScreen.HBack = dataMap.HBack;
         LoadMapScreen.isNextMap = true;
         if (var4 == 1) {
            GameCanvas.gameScr.setTypeViewPlayer(typeViewPlayer);
         }

         byte var10 = m.reader().readByte();
         byte var11 = m.reader().readByte();
         LoadMapScreen.typeChangeMap = m.reader().readByte();
         if (var10 >= 0 && !GameCanvas.lowGraphic) {
            GameScreen.effMap = new Effect_Map(var10, var11);
         } else {
            GameScreen.effMap = null;
         }

         if (LoadMap.specMap == 3) {
            GlobalService.getInstance().Get_Xp_Map_Train((byte)0);
            Player.AutoFireCur = Player.typeAutoFireMain;
            MainObject.mPosMapTrain = mSystem.new_M_Int(typeViewPlayer = m.reader().readByte(), 2);
            int var9 = 0;

            while(true) {
               if (var9 >= typeViewPlayer) {
                  Player.strTimeChange = m.reader().readUTF();
                  break;
               }

               for(var5 = 0; var5 < 2; ++var5) {
                  MainObject.mPosMapTrain[var9][var5] = m.reader().readByte();
               }

               ++var9;
            }
         }

         Interface_Game.nameMap = dataMap.nameMap;
         if (LoadMap.specMap == 4) {
            GameScreen.player.boatSea = new Boat(GameScreen.player.ID, GameScreen.player.x, GameScreen.player.y, 0, (byte)GameScreen.player.type_left_right);
            GameScreen.player.setSpeed(4, 3);
            GameScreen.player.vySea = 4;
            if (!GameCanvas.lowGraphic) {
               GameScreen.effSea = new Effect_Map((byte)4, (byte)0);
               return;
            }
         } else if (LoadMap.isOnlineMap && LoadMap.specMap != 5) {
            GameScreen.player.boatSea = null;
            GameScreen.player.setSpeed(7, 7);
            GameScreen.effSea = null;
            return;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void PvP_Thong_Bao(Message var0) {
      try {
         Interface_Game.setTypePvP(var0.reader().readByte());
      } catch (Exception var1) {
      }
   }

   public static void EA(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            String var7 = var0.reader().readUTF();
            byte var2 = var0.reader().readByte();
            mVector var3 = new mVector();

            for(int var4 = 0; var4 < var2; ++var4) {
               Class_DQ var5 = new Class_DQ(var0.reader().readUTF(), var0.reader().readUTF(), var0.reader().readInt(), var0.reader().readInt(), var0.reader().readShort(), var0.reader().readByte());
               var3.addElement(var5);
            }

            GameCanvas.AB(GameCanvas.AA(var7, var3));
            return;
         }

         if (var1 == 1) {
            var1 = var0.reader().readByte();
            if (MsgArchiDaily.vecArchi != null && var1 >= 0 && var1 < MsgArchiDaily.vecArchi.size()) {
               ((Class_DQ)MsgArchiDaily.vecArchi.elementAt(var1)).AG = var0.reader().readByte();
               return;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void EB(Message var0) {
      try {
         mVector var1 = new mVector();
         byte var2 = var0.reader().readByte();

         for(int var3 = 0; var3 < var2; ++var3) {
            Class_DR var4;
            (var4 = new Class_DR()).AB = var0.reader().readByte();
            if (var4.AB != 0 && var4.AB != 2) {
               if (var4.AB == 1) {
                  var4.AA = new String[4];
                  var4.AA[0] = var0.reader().readUTF();
                  var4.AA[1] = var0.reader().readUTF();
                  var4.AA[2] = var0.reader().readUTF();
                  var4.AA[3] = var0.reader().readUTF();
               }
            } else {
               var4.AA = new String[2];
               var4.AA[0] = var0.reader().readUTF();
               var4.AA[1] = var0.reader().readUTF();
            }

            var4.AC = var0.reader().readByte();
            var1.addElement(var4);
            GameCanvas.AB(GameCanvas.AA(var1));
         }

      } catch (Exception var5) {
      }
   }

   public static void ReadPartNew(Message m) {
      try {
         short num = m.reader().readShort();
         byte type = m.reader().readByte();
         mPart mPartp = new mPart(type);
         for(int i = 0; i < mPartp.pi.length; ++i) {
            mPartp.pi[i] = new PartImage();
            mPartp.pi[i].id = m.reader().readShort();
            mPartp.pi[i].dx = m.reader().readByte();
            mPartp.pi[i].dy = m.reader().readByte();
         }
         CharPartInfo.hashMyPart.put("" + num, mPartp);
         Agetpart.onPartReceived(num, mPartp);
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public static void ED(Message var0) {
      try {
         MainObject var1;
         if ((var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)0)) != null) {
            var1.LW = 0;
            var1.IV = var0.reader().readInt();
            byte var3;
            if ((var3 = var0.reader().readByte()) == 1) {
               var1.LW = 6;
               return;
            }

            if (var3 == 2 || var3 == 3) {
               var1.LW = 1;
               return;
            }

            if (var3 <= 10) {
               var1.LW = 4;
               return;
            }
         }
      } catch (Exception var2) {
      }

   }

   public static void EE(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         String var2 = var0.reader().readUTF();
         mVector var9;
         if (var1 == 0) {
            (var9 = new mVector()).addElement(TabInventory.CQ);
            var9.addElement(TabInventory.CR);
            GameCanvas.Start_Normal_DiaLog(var2, var9, true);
            return;
         }

         if (var1 == 1) {
            var9 = new mVector();
            byte var3 = var0.reader().readByte();

            for(int var4 = 0; var4 < var3; ++var4) {
               byte var5 = var0.reader().readByte();
               String var6 = var0.reader().readUTF();
               short var7 = var0.reader().readShort();
               MainItem var11;
               (var11 = new MainItem(var5, (short)var4, var7, (short)0, (byte)0, (byte)0)).name = var6;
               var9.addElement(var11);
            }

            MsgDialog var10;
            (var10 = new MsgDialog()).AA(var2, var9, (mVector)null, true);
            GameCanvas.Start_Current_Dialog((MainDialog)var10);
            return;
         }

         if (var1 == 2) {
            if (GameCanvas.currentScreen != GameCanvas.gameScr) {
               GameCanvas.gameScr.Show();
            }

            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var2);
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void EF(Message var0) {
      try {
         MainObject var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)2);
         byte var3 = var0.reader().readByte();
         if (var1 != null) {
            var1.MU = var3;
            var1.BY();
            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void EG(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var3 = MainObject.get_Object((int)var1, (byte)var2);
         short var4 = var0.reader().readShort();
         String var6 = var0.reader().readUTF();
         if (var3 != null) {
            var3.IW = var4;
            if (var4 != -1) {
               var3.LK = var4;
            }

            var3.LJ = GameCanvas.timeNow;
            var3.BD = var6;
            (new StringBuffer("item drop id cat ")).append(var1).append(" - ").append(var2).append(" namePlayer =").append(var6).append("!").toString();
            var3.BE = true;
            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void EH(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var5;
         if ((var5 = MainObject.get_Object((int)var1, (byte)var2)) != null) {
            var2 = var0.reader().readByte();
            short var4 = var0.reader().readShort();
            if (var5.name == GameScreen.player.name && var2 == 0) {
               var5.BY = var4;
            }

            if (var2 == 6) {
               var5.sethead(var4);
               return;
            }
         }
      } catch (Exception var3) {
      }

   }

   public final void EI(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) == 2) {
            int var10;
            if ((var10 = MotherCanvas.hw - (MainTab.AG + 130) / 2) < 0) {
               var10 = 0;
            }

            GameCanvas.CJ = new TabScreen(var10, (byte)0);
            mVector var12 = new mVector();
            Class_GN var15 = new Class_GN(T.PT, (mVector)null, GameCanvas.CJ, (byte)0);
            var12.addElement(var15);
            GameCanvas.CJ.addVecTab(var12);
            Class_GN var16 = new Class_GN(T.PU, (mVector)null, GameCanvas.CJ, (byte)1);
            var12.addElement(var16);
            Class_GN var20 = new Class_GN(T.PV, (mVector)null, GameCanvas.CJ, (byte)2);
            var12.addElement(var20);
            var20 = new Class_GN(T.PW, (mVector)null, GameCanvas.CJ, (byte)5);
            var12.addElement(var20);
            Class_GN var18 = new Class_GN(T.PX, (mVector)null, GameCanvas.CJ, (byte)6);
            var12.addElement(var18);
            Class_GN var9 = new Class_GN(T.HO, (mVector)null, GameCanvas.CJ, (byte)3);
            var12.addElement(var9);
            (GameCanvas.tabInvenMarket = new TabInventory(T.tabInven, Player.vecInventory, (byte)5, var10)).initCmd();
            var12.addElement(GameCanvas.tabInvenMarket);
            GameCanvas.CJ.idSelect = 0;
            GameCanvas.CJ.Show((MainScreen)GameCanvas.gameScr);
            GameCanvas.CJ.setTabSelect();
            TabScreen var10000 = GameCanvas.CJ;
            MainScreen.AD((byte)1);
            return;
         }

         short var3;
         mVector var4;
         int var5;
         if (var2 == 3) {
            if (GameCanvas.CJ == null) {
               return;
            }

            var2 = var1.reader().readByte();
            var3 = var1.reader().readShort();
            var4 = new mVector();

            for(var5 = 0; var5 < var3; ++var5) {
               Item var19;
               (var19 = GE(var1)).AG = var1.reader().readInt();
               var19.setTimeMarket(var1.reader().readInt());
               var19.BY = var1.reader().readByte();
               var4.addElement(var19);
            }

            GameCanvas.CJ.AA(var2, var4);
            return;
         }

         if (var2 == 8) {
            byte[] var11 = new byte[var2 = var1.reader().readByte()];

            for(int var13 = 0; var13 < var2; ++var13) {
               var11[var13] = 0;
            }

            MsgArea var14;
            (var14 = new MsgArea()).setinfoChangeArea(var11, (byte)1);
            GameCanvas.Start_Current_Dialog((MainDialog)var14);
            return;
         }

         if (var2 == 9) {
            if (GameCanvas.CJ == null) {
               return;
            }

            var2 = var1.reader().readByte();
            var3 = var1.reader().readShort();
            var4 = new mVector();

            for(var5 = 0; var5 < var3; ++var5) {
               byte var6;
               Object var17;
               if ((var6 = var1.reader().readByte()) == 3) {
                  var17 = GE(var1);
               } else {
                  var17 = AD(var1, var6);
               }

               ((MainItem)var17).AG = var1.reader().readInt();
               int var7 = var1.reader().readInt();
               ((MainItem)var17).setTimeMarket(var7);
               ((MainItem)var17).BY = var1.reader().readByte();
               var4.addElement(var17);
            }

            GameCanvas.CJ.AA(var2, var4);
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   private static MainItem AD(Message var0, byte var1) {
      try {
         short var2 = var0.reader().readShort();
         short var3 = var0.reader().readShort();
         short var7 = var0.reader().readShort();
         MainItem var4 = null;
         if (var1 == 4) {
            Potion var5 = Potion.AD(var3);
            (var4 = new MainItem(var1, var5.idIcon, var3)).AR = var2;
            var4.numPotion = var7;
            var4.name = var5.name;
            var4.namepaint = var5.name;
            if (var5.CR) {
               if (!var4.AA(var5.AP)) {
                  Potion.CP.addElement(var4);
               }
            } else {
               var4.AA(var5.info);
            }
         } else if (var1 == 7) {
            MainMaterial var8 = (MainMaterial)MainItem.hashMaterialTem.get("" + var3);
            (var4 = new MainItem(var1, var8.idIcon, var3)).AR = var2;
            var4.numPotion = var7;
            var4.name = var8.name;
            var4.namepaint = var8.name;
            var4.AA(var8.info);
         }

         return var4;
      } catch (Exception var6) {
         return null;
      }
   }

   public final void EJ(Message var1) {
      try {
         byte var2;
         if ((var2 = var1.reader().readByte()) == 20) {
            var1 = var1;

            try {
               String var16 = var1.reader().readUTF();
               byte var3;
               MainItem[] var4 = new MainItem[(var3 = var1.reader().readByte()) + 1];

               int var5;
               short var6;
               short var9;
               for(var5 = 0; var5 < var3; ++var5) {
                  var6 = var1.reader().readShort();
                  short var7 = var1.reader().readShort();
                  byte var8 = var1.reader().readByte();
                  var9 = var1.reader().readShort();
                  MainItem var10;
                  (var10 = new MainItem(var8, var9, var6)).AS = var7;
                  var4[var5] = var10;
                  MainItem var11;
                  if ((var11 = MainItem.getItemVec(var8, var6, Player.vecInventory)) != null) {
                     var10.numPotion = var11.numPotion;
                  }
               }

               var5 = var1.reader().readInt();
               var6 = var1.reader().readShort();
               int var25 = var1.reader().readInt();
               short var27 = var1.reader().readShort();
               var9 = var1.reader().readShort();
               byte var29 = var1.reader().readByte();
               short var30 = var1.reader().readShort();
               byte var14 = var1.reader().readByte();
               MainItem var28;
               (var28 = new MainItem(var29, var30, var27)).numPotion = var9;
               var4[var4.length - 1] = var28;
               ScreenUpgrade.instance = new ScreenUpgrade((byte)20, var3);
               ScreenUpgrade.setInfo_money(var14, var5, var6, var25);
               ScreenUpgrade.instance.setInfo(var4);
               ScreenUpgrade.instance.BA = var16;
               ScreenUpgrade.instance.Show((MainScreen)GameCanvas.gameScr);
            } catch (Exception var12) {
               var12.printStackTrace();
               return;
            }
         } else {
            String var23;
            if (var2 == 21) {
               var2 = var1.reader().readByte();
               var23 = var1.reader().readUTF();
               ScreenUpgrade.instance.showServer = var23;
               if (var2 == 1) {
                  ScreenUpgrade.instance.Step = 1;
                  return;
               }

               ScreenUpgrade.instance.Step = 2;
               return;
            }

            if (var2 == 13) {
               (SplitScreen.instance = new SplitScreen((byte)8, -1)).Show((MainScreen)GameCanvas.gameScr);
               return;
            }

            short var15;
            MainItem var17;
            short var18;
            MainItem var19;
            byte var21;
            byte var24;
            if (var2 == 14) {
               var2 = var1.reader().readByte();
               var18 = var1.reader().readShort();
               var21 = var1.reader().readByte();
               var15 = var1.reader().readShort();
               if ((var19 = MainItem.getItemVec(var21, var18, Player.vecInventory)) != null) {
                  var24 = var19.colorName;
                  if (var21 != 3) {
                     var24 = 5;
                  }

                  var17 = new MainItem(var21, var19.ID, var19.idIcon, var15, var24, var19.LvUpgrade);
                  ScreenUpgrade.mItemUpgrade[var2] = var17;
                  return;
               }
            } else {
               int var20;
               if (var2 == 15) {
                  for(var20 = 0; var20 < ScreenUpgrade.mItemUpgrade.length; ++var20) {
                     ScreenUpgrade.mItemUpgrade[var20] = null;
                  }

                  return;
               }

               if (var2 == 16) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
                  return;
               }

               if (var2 == 17) {
                  var2 = var1.reader().readByte();
                  var23 = var1.reader().readUTF();
                  SplitScreen.instance.showServer = var23;
                  if (var2 == 1) {
                     SplitScreen.instance.Step = 1;
                     return;
                  }

                  SplitScreen.instance.Step = 3;
                  return;
               }

               if (var2 == 19) {
                  var2 = var1.reader().readByte();
                  SplitScreen.instance.CS = var2;
                  return;
               }

               if (var2 == 8) {
                  (ScreenUpgradeSkillDevil.AA = new ScreenUpgradeSkillDevil()).AV();
                  ScreenUpgradeSkillDevil.AA.Show((MainScreen)GameCanvas.gameScr);
                  return;
               }

               if (var2 == 9) {
                  var2 = var1.reader().readByte();
                  var18 = var1.reader().readShort();
                  var21 = var1.reader().readByte();
                  var15 = var1.reader().readShort();
                  if (var21 == 104) {
                     Skill_Info var22 = Skill_Info.getSkillFromID(var18);
                     MainItem var26 = new MainItem(var21, var22.ID, var22.idIcon, var15, (byte)0, var22.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[var2] = var26;
                     return;
                  }

                  if ((var19 = MainItem.getItemVec(var21, var18, Player.vecInventory)) != null) {
                     var24 = var19.colorName;
                     if (var21 != 3) {
                        var24 = 5;
                     }

                     var17 = new MainItem(var21, var19.ID, var19.idIcon, var15, var24, var19.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[var2] = var17;
                     return;
                  }
               } else {
                  if (var2 == 10) {
                     for(var20 = 0; var20 < ScreenUpgrade.mItemUpgrade.length; ++var20) {
                        ScreenUpgrade.mItemUpgrade[var20] = null;
                     }

                     return;
                  }

                  if (var2 == 11) {
                     GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
                     return;
                  }

                  if (var2 == 12) {
                     var2 = var1.reader().readByte();
                     var23 = var1.reader().readUTF();
                     ScreenUpgradeSkillDevil.AA.showServer = var23;
                     if (var2 == 1) {
                        ScreenUpgradeSkillDevil.AA.Step = 1;
                        return;
                     }

                     ScreenUpgradeSkillDevil.AA.Step = 3;
                     return;
                  }
               }
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public static void EK(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         (new StringBuffer(" cmdEvent action = ")).append(var1).toString();
         if (var1 == 2) {
            if (GameCanvas.BT == null) {
               (GameCanvas.BT = new Class_AK()).AA();
            }

            byte[] var2 = new byte[var1 = var0.reader().readByte()];

            for(int var3 = 0; var3 < var1; ++var3) {
               var2[var3] = var0.reader().readByte();
            }

            GameCanvas.BT.AA(var2);
            return;
         }

         if (var1 == 3) {
            if (GameCanvas.BT == null) {
               GameCanvas.BT = new Class_AK();
            }

            GameCanvas.BT.AA();
            GameCanvas.AB(GameCanvas.BT);
            return;
         }

         if (var1 == 1) {
            GameCanvas.end_Dialog();
            return;
         }
      } catch (Exception var4) {
      }

   }

   public static void EL(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         byte var2;
         if ((var2 = var0.reader().readByte()) == 2) {
            if (GameCanvas.BU == null) {
               (GameCanvas.BU = new Class_AL(var1)).AA((short[][])null);
            }

            short[] var9 = new short[var1 = var0.reader().readByte()];
            byte[] var10 = new byte[var1];

            for(int var11 = 0; var11 < var1; ++var11) {
               var9[var11] = var0.reader().readShort();
               var10[var11] = var0.reader().readByte();
            }

            GameCanvas.BU.AA(var9, var10);
            return;
         }

         if (var2 == 3) {
            if (GameCanvas.BU == null) {
               GameCanvas.BU = new Class_AL(var1);
            }

            short var7;
            short[][] var8 = new short[var7 = var0.reader().readShort()][];

            for(int var3 = 0; var3 < var7; ++var3) {
               short var4 = var0.reader().readShort();
               var8[var3] = new short[var4];

               for(int var5 = 0; var5 < var4; ++var5) {
                  var8[var3][var5] = var0.reader().readShort();
               }
            }

            GameCanvas.BU.AA(var8);
            GameCanvas.AB(GameCanvas.BU);
            return;
         }

         if (var2 == 1) {
            GameCanvas.end_Dialog();
            return;
         }
      } catch (Exception var6) {
      }

   }

   public static void EM(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var3;
         (var3 = MainObject.get_Object((int)var1, (byte)var2)).LL = var0.reader().readShort();
         var3.LM = var0.reader().readByte();
         var3.PN = System.currentTimeMillis();
         (new StringBuffer("id ")).append(var1).append(" cat ").append(var2).append(" time ").append(var3.LL).append(" state").append(var3.LM).toString();
      } catch (Exception var4) {
      }
   }

   public static void EN(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         byte[] var2 = new byte[var0.reader().available()];
         var0.reader().read(var2);
         ByteArrayInputStream var4 = new ByteArrayInputStream(var2);
         DataInputStream var5 = new DataInputStream(var4);
         if (var1 == 4) {
            Potion.AA(var5, (byte)4);
            Potion.AE();
            return;
         }

         if (var1 == 1) {
            CatalogyMonster.AA(var5);
            return;
         }

         if (var1 == 98) {
            LoadMap var10000 = GameCanvas.loadmap;
            LoadMap.AA(var5);
            AJ = 30;
            --LoadMap.BD;
            return;
         }

         if (var1 == 97) {
            Plash.readDataPlash(var5);
            return;
         }

         if (var1 == 96) {
            var1 = var5.readByte();
            String var6 = var5.readUTF();
            MainItem.CD.put("" + var1, var6);
            Item.CheckAddDataKichAn();
            return;
         }
      } catch (Exception var3) {
      }

   }

   public static void EO(Message var0) {
      try {
         GameCanvas.infoDisConnect = var0.reader().readUTF();
         GlobalService.getInstance().AL();
      } catch (Exception var1) {
      }
   }

   public static void Max_Level(Message msh) {
      try {
         byte b = msh.reader().readByte();
         Player.pointMaxLevelAttri = msh.reader().readShort();
         if (b == 0) {
            Player.AX.removeAllElements();
            mVector mvec = new mVector();
            byte b2 = msh.reader().readByte();

            for(int j = 0; j < b2; ++j) {
               MaxLevelAttribute var4 = new MaxLevelAttribute(msh.reader().readShort(), msh.reader().readUTF(), msh.reader().readShort(), msh.reader().readShort());
               mvec.addElement(var4);
            }

            Player.AX = mvec;
            return;
         }

         if (b == 1) {
            short var6 = msh.reader().readShort();
            if (Player.AX != null) {
               for(int var2 = 0; var2 < Player.AX.size(); ++var2) {
                  MaxLevelAttribute var3;
                  if ((var3 = (MaxLevelAttribute)Player.AX.elementAt(var2)).AC == var6) {
                     var3.AA = msh.reader().readShort();
                  }
               }

               return;
            }
         } else if (b == 2) {
            Class_GO.AF().AG();
            Class_GO.AF().Show((MainScreen)GameCanvas.gameScr);
            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void EQ(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 4) {
            GameScreen.player.KP = var0.reader().readInt();
         } else {
            if (Class_GL.AA == null) {
               Class_GL.AA = new Class_GL();
            }

            if (var1 == 5) {
               int var3 = var0.reader().readInt();
               Class_GL.AA.AD(var3);
            } else {
               Class_GL.AA.AA(var1);
            }
         }
      } catch (Exception var2) {
      }
   }

   public static void ER(Message var0) {
      try {
         byte var1;
         byte var2;
         if ((var1 = var0.reader().readByte()) == 0) {
            var2 = var0.reader().readByte();
            short var3 = var0.reader().readShort();
            short var4 = var0.reader().readShort();
            byte var5 = var0.reader().readByte();
            String var6 = var0.reader().readUTF();
            short var7 = var0.reader().readShort();
            short var8 = var0.reader().readShort();
            short var9 = var0.reader().readShort();
            Item var11 = new Item(var5, var3, var4, var6, var8, var7, var9);
            Player.QX[var2] = var11;
         }

         if (var1 == 1) {
            var2 = var0.reader().readByte();
            Player.QX[var2] = null;
            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void AG() {
      GameCanvas.chatTabScr.AD(1);
      GameCanvas.chatTabScr.Show(GameCanvas.currentScreen);
   }

   public static void ES(Message var0) {
      try {
         byte var1;
         int var6;
         if ((var1 = var0.reader().readByte()) == 0) {
            var1 = var0.reader().readByte();

            for(var6 = 0; var6 < var1; ++var6) {
               if (var6 < Interface_Game.AS.length) {
                  Interface_Game.AS[var6][1] = (short)var0.reader().readByte();
               }
            }

            return;
         }

         if (var1 == 1) {
            var1 = var0.reader().readByte();

            for(var6 = 0; var6 < var1; ++var6) {
               short var7 = var0.reader().readShort();
               byte var9 = var0.reader().readByte();
               MainObject var8;
               if ((var8 = MainObject.get_Object((int)var7, (byte)1)) != null) {
                  var8.typePK = var9;
               }
            }

            return;
         }

         if (var1 == 2) {
            var1 = var0.reader().readByte();
            byte var2 = var0.reader().readByte();
            String var3 = var0.reader().readUTF();
            InfoShowNotify var4 = new InfoShowNotify(var3 + ": " + T.WU[var1], var2);
            Interface_Game.vecQuickChatLoL.insertElementAt(var4, 0);
            return;
         }

         if (var1 == 3) {
            Interface_Game.AT = var0.reader().readShort();
            Interface_Game.AU = var0.reader().readShort();
            Interface_Game.AV = var0.reader().readShort();
            Interface_Game.AW = var0.reader().readShort();
            return;
         }

         if (var1 == 4) {
            Interface_Game.BF = var0.reader().readByte();
            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void ET(Message var0) {
      try {
         if (LoadMapScreen.isNextMap) {
            if (GameCanvas.lowGraphic && GameScreen.vecObjFire.size() > 20) {
               GameScreen.vecObjFire.removeElement(1);
            }

            GameScreen.vecObjFire.addElement(var0);
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   public static void AH() {
      Player.SO = 6;
   }

   public static void EU(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         byte var4 = var0.reader().readByte();
         MainObject var6;
         if ((var6 = MainObject.get_Object((int)var1, (byte)var2)) != null && var4 >= 0 && var4 < T.WV.length) {
            String var5 = GameMidlet.AE(T.WV[var4]);
            var6.BC = var5;
            if (var6.typeObject == 0) {
               GameCanvas.chatTabScr.AB(T.CR, "", var6.name + ": " + var5, (byte)1, false);
               return;
            }
         }
      } catch (Exception var3) {
      }

   }

   public static void EV(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         String var3 = var0.reader().readUTF();
         Potion.CS.put("" + var1, var3);
         Potion.AA(var1, var3);
      } catch (Exception var2) {
      }
   }

   public static void EW(Message var0) {
      try {
         MainObject var1;
         if ((var1 = MainObject.get_Object((int)var0.reader().readShort(), (byte)0)) != null) {
            var1.IX = var0.reader().readByte();
            var1.IY = var0.reader().readShort();
            var1.IZ = var0.reader().readShort();
            return;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public final void EX(Message var1) {
      try {
         short var2 = var1.reader().readShort();
         byte var3 = var1.reader().readByte();
         MainObject var4;
         if ((var4 = MainObject.get_Object((int)var2, (byte)var3)) == null || var4.returnAction()) {
            return;
         }

         byte var5 = var1.reader().readByte();
         var4.maxHp = var1.reader().readInt();
         int var6 = var1.reader().readInt();
         int var7;
         if ((var7 = var1.reader().readInt()) != 0) {
            String var8 = "" + var7;
            if (var7 > 0) {
               var8 = "+" + var7;
            }

            var4.Hp = var6;
            if (var5 == 1) {
               GameScreen.addEffectNumBig_NEW_AP((int)var7, 0, var4.x, var4.y - var4.hOne, (byte)20);
            } else {
               GameScreen.addEffectNum(var8, var4.x - 12, var4.y - var4.hOne / 4 * 3 - this.AS, (byte)3);
            }
         }

         if (var4.Action == 4 && var4.Hp > 0) {
            var4.Reveive();
         }

         int var13;
         for(var13 = 0; var13 < Player.vecParty.size(); ++var13) {
            Class_DS var11;
            if ((var11 = (Class_DS)Player.vecParty.elementAt(var13)).AG == var2 && var3 == 0) {
               var11.AA(var6, var4.maxHp, var4.Lv);
               break;
            }
         }

         if (var3 != 1) {
            var4.maxMp = var1.reader().readInt();
            var13 = var1.reader().readInt();
            int var12;
            if ((var12 = var1.reader().readInt()) != 0) {
               String var10 = "" + var12;
               if (var12 > 0) {
                  var10 = "+" + var12;
               }

               var4.Mp = var13;
               GameScreen.addEffectNum(var10, var4.x + 12, var4.y - var4.hOne / 4 * 3 - this.AS, (byte)4);
            }
         }

         this.AS += 10;
         if (this.AS > 20) {
            this.AS = 0;
            return;
         }
      } catch (Exception var9) {
      }

   }

   public static void EY(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         short var2 = var0.reader().readShort();
         short var3 = var0.reader().readShort();
         byte var4 = var0.reader().readByte();
         var0.reader().readByte();
         int var5 = var0.reader().readInt();
         MainObject var15;
         if ((var15 = MainObject.get_Object((int)var3, (byte)var4)) != null && var1 != 11) {
            GameScreen.addEffectEnd((short)143, var1, var15.x, var15.y - 50, (byte)0, var15);
            if (var15 == GameScreen.player) {
               Class_EB var16 = new Class_EB((short)(var1 + 400), var1, 1500);
               Interface_Game var10000 = GameScreen.interfaceGame;
               Interface_Game.AA((MainItem)var16);
               Player.AA(var16.indexHotKey, var2 * 1000, true, (byte)0);
            }
         }

         short var17 = var0.reader().readShort();
         byte var13 = var0.reader().readByte();
         byte var6 = var0.reader().readByte();
         int var8 = var0.reader().readInt();
         MainObject var14 = MainObject.get_Object((int)var17, (byte)var13);
         Class_BR var10;
         switch(var1) {
         case 0:
            if (var15 != null) {
               var10 = new Class_BR(var15, (short)11, (short)var5);
               var15.vecEffspec.addElement(var10);
               return;
            }
            break;
         case 1:
            if (var15 != null) {
               GameScreen.addEffectNumBig_NEW_AP((int)var5, 0, var15.x, var15.y - var15.hOne, (byte)21);
               return;
            }
            break;
         case 2:
         case 10:
            if (var14 != null) {
               var14.addEffSpec((short)var6, (short)((short)var8));
               return;
            }
            break;
         case 3:
            if (var14 != null) {
               GameScreen.addEffectNumBig_NEW_AP((int)(-var8), 0, var14.x, var14.y - var14.hOne, (byte)22);
               return;
            }
            break;
         case 4:
            if (var14 != null) {
               GameScreen.addEffectNumBig_NEW_AP((int)(-var8), 0, var14.x, var14.y - var14.hOne, (byte)21);
               return;
            }
            break;
         case 5:
            if (var15 != null) {
               var10 = new Class_BR(var15, (short)12, (short)var5);
               var15.vecEffspec.addElement(var10);
               return;
            }
            break;
         case 6:
            if (var15 != null) {
               GameScreen.addEffectNumBig_NEW_AP((int)var5, 0, var15.x, var15.y - var15.hOne, (byte)21);
               return;
            }
            break;
         case 8:
         case 9:
            if (var15 != null && var15 == GameScreen.player) {
               Enumeration var9 = Player.QE.GetEnumerator();

               while(var9.hasMoreElements()) {
                  String var11 = (String)var9.nextElement();
                  DelaySkill var12;
                  if ((var12 = (DelaySkill)Player.QE.get(var11)).AA == 1) {
                     var12.AC = 0;
                  }
               }

               return;
            }
            break;
         case 11:
            if (var15 == null) {
               break;
            }

            GameScreen.addEffectNumBig_NEW_AP((int)var5, 0, var15.x, var15.y - var15.hOne, (byte)21);
         case 7:
         default:
            return;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void EZ(Message var0) {
      try {
         if (Interface_Game.BO == null) {
            Interface_Game.BO = new Class_EB[20];
         }

         byte var1 = var0.reader().readByte();
         byte var4 = var0.reader().readByte();

         for(int var2 = 0; var2 < Interface_Game.BO.length; ++var2) {
            if (Interface_Game.BO[var2] == null) {
               Interface_Game.BO[var2] = new Class_EB((short)(var1 + 400), var1, 1500);
               Interface_Game.BO[var2].numPotion = var4;
               Interface_Game.BO[var2].AE();
               return;
            }

            if (Interface_Game.BO[var2].ID == var1) {
               Interface_Game.BO[var2].numPotion = var4;
               return;
            }
         }

      } catch (Exception var3) {
      }
   }

   public static void FA(Message var0) {
      try {
         AK = var0.reader().readByte();
         AL = var0.reader().readUTF();
      } catch (Exception var1) {
      }
   }

   public final void FB(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         byte var3 = var1.reader().readByte();
         if (var2 == 0) {
            int var4;
            if (var3 == 0) {
               String var15 = var1.reader().readUTF();
               short var17 = var1.reader().readShort();
               var4 = var1.reader().readInt();
               int var20 = var1.reader().readInt();
               int var6 = var1.reader().readInt();
               byte var7 = var1.reader().readByte();
               byte var8 = var1.reader().readByte();
               byte var9 = var1.reader().readByte();
               byte var10 = var1.reader().readByte();
               byte var12 = var1.reader().readByte();
               (Class_GZ.AA = new Class_GZ(var15, var20, var4, var6, var7, var17)).AA(var8, var9, var10, var12, false);
               Class_GZ.AA.Show((MainScreen)GameCanvas.gameScr);
               return;
            }

            int var13;
            int var16;
            byte var19;
            if (var3 == 1) {
               var13 = var1.reader().readInt();
               var16 = var1.reader().readInt();
               var4 = var1.reader().readInt();
               var19 = var1.reader().readByte();
               Class_GZ.AA.AA(var16, var13, var4, var19);
               return;
            }

            if (var3 == 2) {
               var2 = var1.reader().readByte();
               var3 = var1.reader().readByte();
               byte var18 = var1.reader().readByte();
               var19 = var1.reader().readByte();
               Class_GZ.AA.AA(var2, var3, var18, var19, true);
               return;
            }

            if (var3 == 3) {
               var13 = var1.reader().readInt();
               var16 = var1.reader().readInt();
               Class_GZ.AA.AB(var16, var13);
               return;
            }

            if (var3 == 4) {
               var2 = var1.reader().readByte();
               mVector var14 = new mVector();

               for(var4 = 0; var4 < var2; ++var4) {
                  String var5 = var1.reader().readUTF();
                  var14.addElement(new iCommand(var5, 100, this));
               }

               GameCanvas.menu.startAt(var14, 2, T.VC);
               return;
            }
         } else if (var2 == 1 && var3 == 0) {
            GameCanvas.gameScr.AA(true);
            return;
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static void FC(Message var0) {
      try {
         byte var1;
         short var12;
         byte var15;
         byte var17;
         if ((var1 = var0.reader().readByte()) == 0) {
            var12 = var0.reader().readShort();
            String var14 = var0.reader().readUTF();
            var15 = var0.reader().readByte();
            mVector var16 = new mVector();

            for(var17 = 0; var17 < var15; ++var17) {
               String var19 = var0.reader().readUTF();
               byte var20 = var0.reader().readByte();
               short var22 = var0.reader().readShort();
               var0.reader().readByte();
               short var9 = var0.reader().readShort();
               Potion var21;
               (var21 = new Potion(var20, var22, var9, "", (byte)0)).namepaint = var19;
               var21.info = var0.reader().readUTF();
               var21.AA(var21.info);
               var16.addElement(var21);
            }

            (SplitScreen.instance = new SplitScreen((byte)21, -1)).BN = var16;
            SplitScreen.instance.AH();
            SplitScreen.instance.CU = var12;
            SplitScreen.instance.CV = var14;
            SplitScreen.instance.Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var1 == 1) {
            var1 = var0.reader().readByte();
            mVector var13 = new mVector();

            for(var15 = 0; var15 < var1; ++var15) {
               String var4 = var0.reader().readUTF();
               var17 = var0.reader().readByte();
               short var18 = var0.reader().readShort();
               short var7 = var0.reader().readShort();
               Potion var8;
               (var8 = new Potion(var17, var18, var7, "", (byte)0)).namepaint = var4;
               var8.info = var0.reader().readUTF();
               var8.AA(var8.info);
               var13.addElement(var8);
            }

            SplitScreen.instance.CT = var13;
            if (var1 == 0) {
               ScreenUpgrade.mItemUpgrade[0] = null;
            } else {
               ScreenUpgrade.mItemUpgrade[0] = (MainItem)var13.elementAt(0);
            }

            SplitScreen.instance.AW();
            return;
         }

         if (var1 == 2) {
            var12 = var0.reader().readShort();
            SplitScreen.instance.AA(var12);
            return;
         }

         byte var2;
         if (var1 == 3) {
            String var11 = var0.reader().readUTF();
            var2 = var0.reader().readByte();
            short var3 = var0.reader().readShort();
            var0.reader().readByte();
            short var5 = var0.reader().readShort();
            Potion var6;
            (var6 = new Potion(var2, var3, var5, "", (byte)0)).namepaint = var11;
            var6.info = var0.reader().readUTF();
            var6.AA(var6.info);
            SplitScreen.instance.BN.addElement(var6);
            ScreenUpgrade.mItemUpgrade[0] = null;
            SplitScreen.instance.AW();
            return;
         }

         if (var1 == 4) {
            (Class_FQ.AB = new Class_FQ()).AA = var0.reader().readUTF();
            var1 = var0.reader().readByte();
            Class_FQ.AB.AC = new String[var1];
            Class_FQ.AB.AD = new short[var1];

            for(var2 = 0; var2 < var1; ++var2) {
               Class_FQ.AB.AC[var2] = var0.reader().readUTF();
               Class_FQ.AB.AD[var2] = var0.reader().readShort();
            }

            Class_FQ.AB.Show((MainScreen)GameCanvas.gameScr);
            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void FD(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            (LuckyScreen.AH = new LuckyScreen()).Show((MainScreen)GameCanvas.gameScr);
            LuckyScreen.AH.BM = null;
            GlobalService.getInstance().AL((byte)3);
            LuckyScreen.BJ = new mVector();
            return;
         }

         if (var1 == 1 || var1 == 2) {
            var1 = var0.reader().readByte();
            LuckyScreen.AH.AX = 0;
            float var10000;
            if (var1 == 9) {
               LuckyScreen.AH.AX = 1;
               var10000 = mSound.volumeSound;
               mSound.playSound();
            } else {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            LuckyScreen.AH.AY = 1;
            LuckyScreen.AH.AW = 0;
            Item_Drop[] var10 = new Item_Drop[var1];

            for(int var11 = var1 - 1; var11 >= 0; --var11) {
               byte var12 = var0.reader().readByte();
               String var13 = var0.reader().readUTF();
               short var9 = var0.reader().readShort();
               int var6 = var0.reader().readInt();
               byte var7 = var0.reader().readByte();
               var10[var11] = new Item_Drop((short)var11, var12, var13, 0, 0, var9, var7);
               var10[var11].num = var6;
            }

            LuckyScreen.AH.AA(var10);
            return;
         }

         if (var1 == 3) {
            LuckyScreen.BJ.removeAllElements();
            var1 = var0.reader().readByte();

            for(short var2 = 0; var2 < var1; ++var2) {
               byte var3 = var0.reader().readByte();
               short var4 = var0.reader().readShort();
               MainItem var5 = new MainItem(var3, var4, var2);
               LuckyScreen.BJ.addElement(var5);
            }

            LuckyScreen.AH.AK();
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void FE(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            (Class_GK.AA = new Class_GK()).Show((MainScreen)GameCanvas.gameScr);
            Class_GK.AA.BM = null;
            GlobalService.getInstance().AM((byte)3);
            LuckyScreen.BJ = new mVector();
            return;
         }

         if (var1 == 1 || var1 == 2) {
            var1 = var0.reader().readByte();
            Class_GK.AA.AX = 0;
            float var10000;
            if (var1 == 9) {
               Class_GK.AA.AX = 1;
               var10000 = mSound.volumeSound;
               mSound.playSound();
            } else {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            Class_GK.AA.AY = 1;
            Class_GK.AA.AW = 0;
            Item_Drop[] var10 = new Item_Drop[var1];

            for(int var11 = var1 - 1; var11 >= 0; --var11) {
               byte var12 = var0.reader().readByte();
               String var13 = var0.reader().readUTF();
               short var9 = var0.reader().readShort();
               int var6 = var0.reader().readInt();
               byte var7 = var0.reader().readByte();
               var10[var11] = new Item_Drop((short)var11, var12, var13, 0, 0, var9, var7);
               var10[var11].num = var6;
            }

            Class_GK.AA.AA(var10);
            return;
         }

         if (var1 == 3) {
            LuckyScreen.BJ.removeAllElements();
            var1 = var0.reader().readByte();

            for(short var2 = 0; var2 < var1; ++var2) {
               byte var3 = var0.reader().readByte();
               short var4 = var0.reader().readShort();
               MainItem var5 = new MainItem(var3, var4, var2);
               LuckyScreen.BJ.addElement(var5);
            }

            Class_GK.AA.AK();
            return;
         }

         if (var1 == 4) {
            Class_GK.AA.AB = var0.reader().readShort();
            Class_GK.AA.AD = var0.reader().readShort();
            Class_GK.AA.AC = var0.reader().readShort();
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void FF(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            (Class_GJ.AA = new Class_GJ()).AB = var0.reader().readUTF();
            Class_GJ.AA.Show((MainScreen)GameCanvas.gameScr);
            Class_GJ.AA.BM = null;
            GlobalService.getInstance().AN((byte)1);
            LuckyScreen.BJ = new mVector();
            return;
         }

         if (var1 == 1) {
            LuckyScreen.BJ.removeAllElements();
            byte[] var2 = new byte[var1 = var0.reader().readByte()];

            for(short var3 = 0; var3 < var1; ++var3) {
               byte var4 = var0.reader().readByte();
               byte var5 = var0.reader().readByte();
               short var6 = var0.reader().readShort();
               int var7 = var0.reader().readInt();
               MainItem var9 = new MainItem(var5, var6, var4, var7);
               LuckyScreen.BJ.addElement(var9);
               var4 = var0.reader().readByte();
               var2[var3] = var4;
            }

            Class_GJ.AA.AA(var2);
            Class_GJ.AA.AK();
            return;
         }

         if (var1 == 2) {
            var1 = var0.reader().readByte();
            Class_GJ.AA.AA(var1);
            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public final void FG(Message var1) {
      try {
         mVector var2 = new mVector();
         byte var3;
         int var4;
         short var5;
         String var6;
         short var7;
         if ((var3 = var1.reader().readByte()) == 0) {
            var3 = var1.reader().readByte();

            for(var4 = 0; var4 < var3; ++var4) {
               var5 = var1.reader().readShort();
               if ((var6 = var1.reader().readUTF()).length() >= 15) {
                  var6 = var6.substring(0, 12) + "...";
               }

               var7 = var1.reader().readShort();
               short var14 = var1.reader().readShort();
               iCommand var12;
               (var12 = new iCommand(var6 + " " + T.gohome + " " + var14, 11, var5, this)).AA(var7);
               var12.AX = var14;
               var2.addElement(var12);
            }

            GameCanvas.menu.startAt(var2, 2, T.SE);
            return;
         }

         if (var3 == 4) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var1.reader().readUTF());
            return;
         }

         if (var3 == 5) {
            short var10 = var1.reader().readShort();
            String var11;
            if ((var11 = var1.reader().readUTF()).length() >= 15) {
               var11 = var11.substring(0, 12) + "...";
            }

            var5 = var1.reader().readShort();
            byte var13 = var1.reader().readByte();
            Class_DS.AA(var10, (byte)7, var11 + " " + T.gohome + " " + var5, T.SF, 5, var13);
            return;
         }

         if (var3 == 6) {
            var3 = var1.reader().readByte();

            for(var4 = 0; var4 < var3; ++var4) {
               var5 = var1.reader().readShort();
               var6 = var1.reader().readUTF();
               var7 = var1.reader().readShort();
               Class_DS var8;
               (var8 = new Class_DS(var5)).AC = var6;
               var8.AD = T.gohome + var7;
               var8.AN = var4;
               var2.addElement(var8);
            }

            (PlayerListServer.instance = new PlayerListServer((byte)15, var2, T.SG, (byte)0)).Show((MainScreen)GameCanvas.gameScr);
            PlayerListServer.instance.BI = false;
            return;
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void FH(Message var0) {
      try {
         Class_AM var1 = new Class_AM();
         String var2 = var0.reader().readUTF();
         String var3 = var0.reader().readUTF();
         byte var4;
         Item_Drop[] var5 = new Item_Drop[var4 = var0.reader().readByte()];

         for(int var6 = 0; var6 < var4; ++var6) {
            byte var7 = var0.reader().readByte();
            String var8 = var0.reader().readUTF();
            short var9 = var0.reader().readShort();
            byte var10 = var0.reader().readByte();
            var5[var6] = new Item_Drop((short)var6, var7, var8, 0, 0, var9, var10);
            var5[var6].num = var0.reader().readShort();
            var5[var6].AA = var0.reader().readByte();
         }

         var1.AA((byte)0, var2, var5, (byte)0, var3, (MainItem)null);
         GameCanvas.Start_Current_Dialog((MainDialog)var1);
      } catch (Exception var11) {
         var11.printStackTrace();
      }
   }

   public static void FI(Message var0) {
      try {
         Class_AM var1 = new Class_AM();
         String var2 = var0.reader().readUTF();
         String var3 = var0.reader().readUTF();
         byte var4;
         Item_Drop[] var5 = new Item_Drop[var4 = var0.reader().readByte()];

         byte var7;
         for(int var6 = 0; var6 < var4; ++var6) {
            var7 = var0.reader().readByte();
            String var8 = var0.reader().readUTF();
            short var9 = var0.reader().readShort();
            byte var10 = var0.reader().readByte();
            var5[var6] = new Item_Drop((short)var6, var7, var8, 0, 0, var9, var10);
            var5[var6].num = var0.reader().readShort();
            var5[var6].AA = var0.reader().readByte();
         }

         short var12 = var0.reader().readShort();
         var7 = var0.reader().readByte();
         MainItem var13;
         (var13 = new MainItem(var7, (short)-1, var12)).name = var2;
         var1.AA((byte)1, var2, var5, (byte)0, var3, var13);
         GameCanvas.AB(var1);
      } catch (Exception var11) {
         var11.printStackTrace();
      }
   }

   public static void AI() {
      GameMidlet var10000 = GameMidlet.AD;
      GameMidlet.AH();
   }

   public static void FJ(Message var0) {
      try {
         mVector var1 = new mVector();
         byte var2 = var0.reader().readByte();

         for(int var3 = 0; var3 < var2; ++var3) {
            MainClan var4;
            (var4 = new MainClan()).name = var0.reader().readUTF();
            var4.idIcon = var0.reader().readShort();
            var4.AG = var0.reader().readInt();
            var1.addElement(var4);
         }

         Interface_Game.vecClanDam.removeAllElements();
         Interface_Game.vecClanDam = var1;
      } catch (Exception var5) {
      }
   }

   public static void FK(Message var0) {
      try {
         (GameScreen.ClanDao = new MainClan()).ID = var0.reader().readShort();
         GameScreen.ClanDao.idIcon = var0.reader().readShort();
         GameScreen.ClanDao.name = var0.reader().readUTF();
         GameScreen.ClanDao.AB = var0.reader().readUTF();
         GameScreen.ClanDao.AD = var0.reader().readShort();
         GameScreen.ClanDao.AE = var0.reader().readByte();
         GameScreen.ClanDao.AF = var0.reader().readByte();
         GameScreen.ClanDao.AI = var0.reader().readInt();
      } catch (Exception var1) {
      }
   }

   public static void FL(Message var0) {
      try {
         short var1 = var0.reader().readShort();
         byte var2 = var0.reader().readByte();
         MainObject var4;
         if ((var4 = MainObject.get_Object((int)var1, (byte)var2)) != null && !var4.returnAction()) {
            var4.thanhtichPvP = var0.reader().readByte();
            var4.thanhtichLv = var0.reader().readByte();
            var4.MY = var0.reader().readByte();
            return;
         }
      } catch (Exception var3) {
      }

   }

   public static void FM(Message var0) {
      try {
         byte var1 = var0.reader().readByte();

         for(int var2 = 0; var2 < var1; ++var2) {
            MainObject var3 = MainObject.get_Object((int)var0.reader().readShort(), (byte)0);
            short var4 = var0.reader().readShort();
            byte var5 = var0.reader().readByte();
            short var6 = var0.reader().readShort();
            if (var3 != null) {
               var3.AA(var4, var5, var6);
            }
         }

      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public static void FN(Message var0) {
      try {
         var0.reader().readUTF();
         GameMidlet.AK();
      } catch (Exception var1) {
      }
   }

   public static void FO(Message var0) {
      try {
         Interface_Game.typeTitleRoomFight = var0.reader().readByte();
      } catch (Exception var1) {
      }
   }

   public static void FP(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            var1 = var0.reader().readByte();
            mVector var2 = new mVector();

            for(int var3 = 0; var3 < var1; ++var3) {
               byte var4 = var0.reader().readByte();
               Class_DS var15 = new Class_DS(var4);
               int var5 = var0.reader().readInt();
               int var6 = var0.reader().readInt();
               int var7 = var0.reader().readInt();
               byte var8 = var0.reader().readByte();
               String var9 = var0.reader().readUTF();
               short var10 = var0.reader().readShort();
               short var11 = var0.reader().readShort();
               byte var12 = var0.reader().readByte();
               byte var13 = var0.reader().readByte();
               var15.BI = new Class_IR(var9, var8, var10, var11, var12);
               var15.BI.AA(var5, var6, var7);
               var15.BG = var13;
               var2.addElement(var15);
            }

            (Class_FD.AA = new Class_FD(var2)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var1 == 1) {
            Class_FD.AA.AA(var0.reader().readByte(), var0.reader().readShort(), var0.reader().readInt(), var0.reader().readInt());
            return;
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      }

   }

   public static void FQ(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            int var11 = var0.reader().readInt();
            byte var2 = var0.reader().readByte();
            mVector var3 = new mVector();

            for(int var4 = 0; var4 < var2; ++var4) {
               byte var5 = var0.reader().readByte();
               Class_DS var12 = new Class_DS(var5);
               int var6 = var0.reader().readInt();
               byte var7 = var0.reader().readByte();
               short var8 = var0.reader().readShort();
               var12.BH = new Class_AX(var6, var7, var8);
               mVector var13 = new mVector();

               for(int var14 = 0; var14 < var8; ++var14) {
                  Class_IR var9 = new Class_IR(var0.reader().readUTF(), var0.reader().readByte(), var0.reader().readShort(), var0.reader().readShort(), var0.reader().readByte());
                  var13.addElement(var9);
               }

               var12.BH.AE = var13;
               var3.addElement(var12);
            }

            (Class_GD.AA = new Class_GD(var3, var11)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var1 == 2) {
            var1 = var0.reader().readByte();
            Class_GD.AA.AA(var1);
            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public final void Read_Sudo(Message var1) {
      try {
         byte var2;
         byte var3;
         byte var5;
         mVector var12;
         if ((var2 = var1.reader().readByte()) == 3) {
            var12 = new mVector();
            if ((Player.QO = var3 = var1.reader().readByte()) != 0) {
               String var4;
               if (var3 == 2) {
                  var4 = T.VK + T.VM;
               } else if (var3 == 1) {
                  var4 = T.VK + T.VL;
               } else {
                  var4 = "";
               }

               var12.addElement(new MainInfoItem(var4, 0));
               var5 = var1.reader().readByte();
               var12.addElement(new MainInfoItem(T.VN, var5));
               short var6 = var1.reader().readShort();
               short var7 = var1.reader().readShort();
               var12.addElement(new MainInfoItem(T.VO, 0));
               var12.addElement(new MainInfoItem(T.gohome + ": " + var6, 0));
               Sudo_Info.AA.AC = var7;
               byte var8 = var1.reader().readByte();

               for(int var9 = 0; var9 < var8; ++var9) {
                  String var10 = var1.reader().readUTF();
                  var12.addElement(new MainInfoItem(var10, 0));
               }

               Sudo_Info.AD = var12;
               Sudo_Info.AA.AF();
               GameCanvas.SudoScr.Show((MainScreen)GameCanvas.gameScr);
               return;
            }
         } else {
            if (var2 == 2) {
               var12 = new mVector();
               var3 = var1.reader().readByte();

               for(int var27 = 0; var27 < var3; ++var27) {
                  Class_DS var22 = new Class_DS(-1);
                  String var23 = var1.reader().readUTF();
                  String var29 = var1.reader().readUTF();
                  short var30 = var1.reader().readShort();
                  short var31 = var1.reader().readShort();
                  short var32 = var1.reader().readShort();
                  var22.AE = var23;
                  var22.AC = var29;
                  var22.AA(var30, var31, var32);
                  var22.AU = var1.reader().readByte();
                  var22.AK = var1.reader().readShort();
                  var22.AF = var1.reader().readUTF();
                  var22.AP = var1.reader().readByte();
                  var12.addElement(var22);
                  if (var29.compareTo(GameScreen.player.name) == 0) {
                     Player.QO = var22.AU;
                  }
               }

               Class_GW.AA = var12;
               return;
            }

            if (var2 == 0) {
               if (GameScreen.player.clan == null) {
                  GameScreen.player.clan = new MainClan(var1.reader().readShort(), var1.reader().readUTF());
                  return;
               }

               GameScreen.player.clan.ID = var1.reader().readShort();
               GameScreen.player.clan.name = var1.reader().readUTF();
               return;
            }

            String var13;
            Class_DS var19;
            if (var2 == 1) {
               if (GameScreen.player.clan == null) {
                  GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
                  return;
               }

               var13 = var1.reader().readUTF();
               var3 = var1.reader().readByte();
               if ((var19 = Clan_Screen.AA(var13)) != null) {
                  var19.AS = var3;
                  if (var19.AC.compareTo(GameScreen.player.name) == 0) {
                     Player.QN = var3;
                     return;
                  }
               }
            } else {
               short var18;
               if (var2 == 4) {
                  if (GameScreen.player.clan == null) {
                     GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
                     return;
                  }

                  var18 = var1.reader().readShort();
                  short var28 = var1.reader().readShort();
                  short[] var25 = new short[5];

                  for(int var20 = 0; var20 < var25.length; ++var20) {
                     var25[var20] = var1.reader().readShort();
                  }

                  GameScreen.player.clan.AK = var28;
                  GameScreen.player.clan.AL = var18;
                  GameScreen.player.clan.AX = var25;
                  return;
               }

               MainObject var15;
               if (var2 == 13) {
                  if ((var15 = MainObject.get_Object((int)var1.reader().readShort(), (byte)0)) != null) {
                     short var21 = var1.reader().readShort();
                     var5 = var1.reader().readByte();
                     MainSudo var10000;
                     if (var15.PJ == null) {
                        var15.PJ = new MainSudo(var21, var5);
                     } else {
                        var10000 = var15.PJ;
                        var10000 = var15.PJ;
                     }

                     if (var15 == GameScreen.player) {
                        Player.QN = var5;
                     }

                     var1.reader().readShort();
                     var1.reader().readUTF();
                     var10000 = var15.PJ;
                     var10000 = var15.PJ;
                     return;
                  }
               } else {
                  if (var2 == 8) {
                     if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                        AA(var1, (int)0);
                        GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AP);
                        return;
                     }

                     return;
                  }

                  int var14;
                  if (var2 == 7) {
                     var14 = var1.reader().readInt();
                     String var26 = var1.reader().readUTF();
                     Class_DS.AA(var14, (byte)8, var26, T.VQ, 0, 0);
                     return;
                  }

                  int var17;
                  if (var2 == 9) {
                     if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                        GameScreen.player.clan.vecChatClan.removeAllElements();
                        var18 = var1.reader().readShort();

                        for(var17 = 0; var17 < var18; ++var17) {
                           AA(var1, (int)0);
                        }

                        return;
                     }

                     return;
                  }

                  if (var2 == 11) {
                     if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                        var18 = var1.reader().readShort();
                        GameCanvas.ClanScr.AP.AA(var18);
                        return;
                     }

                     return;
                  }

                  if (var2 == 10) {
                     if ((var15 = MainObject.get_Object((int)var1.reader().readShort(), (byte)0)) != null && var15.PJ != null) {
                        var15.PJ = null;
                     }

                     if (var15 == GameScreen.player && GameCanvas.currentScreen == GameCanvas.SudoScr) {
                        GameCanvas.gameScr.Show();
                        GameCanvas.SudoScr = null;
                        Player.isGetDataClan = -1;
                        return;
                     }
                  } else {
                     if (var2 == 12) {
                        if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                           if ((var2 = var1.reader().readByte()) == 0) {
                              GN(var1);
                           } else if (var2 == 1) {
                              Clan_Screen.AB(var1.reader().readUTF());
                           }

                           GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AQ);
                           return;
                        }

                        return;
                     }

                     if (var2 == 13) {
                        var14 = var1.reader().readInt();
                        MainTab.BK.setCountDown(var14);
                        var17 = var1.reader().readInt();
                        if ((var19 = Clan_Screen.AA(GameScreen.player.ID)) != null) {
                           var19.AL = var17;
                           return;
                        }
                     } else {
                        if (var2 == 14) {
                           if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                              var13 = var1.reader().readUTF();
                              GameScreen.player.clan.AC = var13;
                              GameCanvas.ClanScr.AR.AA(var13, GameCanvas.ClanScr.AR.AH - 6);
                              GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AR);
                              return;
                           }

                           return;
                        }

                        if (var2 == 15) {
                           short[] var16 = new short[5];

                           for(var17 = 0; var17 < 5; ++var17) {
                              var16[var17] = var1.reader().readShort();
                           }

                           for(var17 = 0; var17 < Player.QF.length; ++var17) {
                              Class_CV var24 = Player.QF[var17];
                              if (var17 < var16.length) {
                                 var24.AB = var16[var17];
                              }
                           }

                           return;
                        }

                        if (var2 == 16) {
                           if (GameScreen.player.clan == null) {
                              return;
                           }

                           GameScreen.player.clan.AR = var1.reader().readByte();
                           GameScreen.player.clan.AG = var1.reader().readInt();
                           return;
                        }

                        if (var2 == 17) {
                           if (GameScreen.player.clan == null) {
                              return;
                           }

                           GameScreen.player.clan.AM = var1.reader().readInt();
                           GameScreen.player.clan.AN = var1.reader().readInt();
                           return;
                        }

                        if (var2 == 19) {
                           this.update_Inven_Clan(var1);
                           return;
                        }

                        if (var2 == 20) {
                           if (GameScreen.player.clan != null && GameCanvas.ClanScr != null) {
                              GameCanvas.ClanScr.AS.AN.removeAllElements();

                              while(var1.reader().available() > 0) {
                                 AA(var1, (int)1);
                              }

                              GameCanvas.ClanScr.AA((ChatDetail)GameCanvas.ClanScr.AS);
                              return;
                           }

                           return;
                        }

                        if (var2 == 21) {
                           var13 = var1.reader().readUTF();
                           if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
                              GameCanvas.gameScr.Show();
                           }

                           GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var13);
                           return;
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static void FS(Message var0) {
      try {
         byte var1;
         int var3;
         Class_DS var4;
         if ((var1 = var0.reader().readByte()) == 0) {
            var1 = var0.reader().readByte();
            mVector var12 = new mVector();

            for(var3 = 0; var3 < var1; ++var3) {
               short var5 = var0.reader().readShort();
               int var6 = var0.reader().readInt();
               var4 = new Class_DS(var3);
               byte var7 = var0.reader().readByte();
               var4.BH = new Class_AX(var5, (byte)1, var7);
               var4.BH.AA = var6;
               mVector var13 = new mVector();

               for(var6 = 0; var6 < var7; ++var6) {
                  Class_IR var8 = new Class_IR(var0.reader().readUTF(), var0.reader().readByte(), var0.reader().readShort(), var0.reader().readShort(), var0.reader().readByte());
                  var13.addElement(var8);
               }

               var4.BH.AE = var13;
               var12.addElement(var4);
            }

            (Class_HA.AA = new Class_HA(var12)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var1 == 2) {
            short var11 = var0.reader().readShort();
            int var2 = var0.reader().readInt();
            var11 = var11;
            Class_HA var10 = Class_HA.AA;
            var3 = 0;

            while(true) {
               if (var3 >= var10.AY.size()) {
                  return;
               }

               if ((var4 = (Class_DS)var10.AY.elementAt(var3)).BH.AB == var11) {
                  var4.BH.AA = var2;
                  break;
               }

               ++var3;
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void FT(Message var0) {
      try {
         byte var1;
         if ((var1 = var0.reader().readByte()) == 0) {
            int var11 = var0.reader().readInt();
            byte var2 = var0.reader().readByte();
            mVector var3 = new mVector();

            for(int var4 = 0; var4 < var2; ++var4) {
               byte var5 = var0.reader().readByte();
               Class_DS var12 = new Class_DS(var5);
               int var6 = var0.reader().readInt();
               byte var7 = var0.reader().readByte();
               short var8 = var0.reader().readShort();
               var12.BH = new Class_AX(var6, var7, var8);
               mVector var13 = new mVector();

               for(int var14 = 0; var14 < var8; ++var14) {
                  Class_IR var9 = new Class_IR(var0.reader().readUTF(), var0.reader().readByte(), var0.reader().readShort(), var0.reader().readShort(), var0.reader().readByte());
                  var13.addElement(var9);
               }

               var12.BH.AE = var13;
               var3.addElement(var12);
            }

            (Class_HC.AA = new Class_HC(var3, var11)).Show((MainScreen)GameCanvas.gameScr);
            return;
         }

         if (var1 == 2) {
            var1 = var0.reader().readByte();
            Class_HC.AA.AA(var1);
            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public final void FU(Message var1) {
      try {
         byte var2 = var1.reader().readByte();
         mVector var3 = new mVector();
         short var4;
         int var5;
         Class_DS var6;
         switch(var2) {
         case 0:
            var4 = var1.reader().readShort();

            for(var5 = 0; var5 < var4; ++var5) {
               (var6 = GO(var1)).AP = var1.reader().readByte();
               if (var6 != null) {
                  var3.addElement(var6);
               }
            }

            (Class_HF.AA = new Class_HF(var2, var3)).Show((MainScreen)GameCanvas.gameScr);
            Class_HF.AA.BI = false;
            return;
         case 1:
         case 4:
         case 5:
            var2 = var2;
            var1 = var1;
            var3 = null;

            try {
               short var13 = var1.reader().readShort();
               Class_DS var14;
               (var14 = new Class_DS(var13)).BC = new MainObject();
               var14.BC.name = var1.reader().readUTF();
               var14.BC.KP = var1.reader().readInt();
               var14.BC.Lv = var1.reader().readShort();
               var4 = var1.reader().readShort();
               short var15 = var1.reader().readShort();
               byte var16;
               short[] var7 = new short[var16 = var1.reader().readByte()];

               for(byte var8 = 0; var8 < var16; ++var8) {
                  var7[var8] = var1.reader().readShort();
               }

               short var10003 = var7[1];
               short var10004 = var7[3];
               short var10005 = var7[5];
               short var10 = var7[0];
               short var9 = var10005;
               short var18 = var10004;
               short var17 = var10003;
               if (var14.BC == null) {
                  var14.BC = new MainObject();
               }

               var14.BC.sethead(var4);
               var14.BC.sethair(var15);
               var14.BC.BX = var17;
               var14.BC.BT = var18;
               var14.BC.BU = var9;
               var14.BC.BV = var10;
               if (var2 == 4 || var2 == 5) {
                  var14.BE = var1.reader().readByte();
               }

               if (var2 == 5) {
                  var14.BF = var1.reader().readByte();
               }

               (Class_HG.AA = new Class_HG(var2, var14)).Show((MainScreen)GameCanvas.gameScr);
               break;
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         case 2:
         default:
            return;
         case 3:
            var4 = var1.reader().readShort();

            for(var5 = 0; var5 < var4; ++var5) {
               (var6 = GO(var1)).BD = var1.reader().readUTF();
               var6.BE = var1.reader().readByte();
               var6.BF = var1.reader().readByte();
               if (var6 != null) {
                  var3.addElement(var6);
               }
            }

            (Class_HF.AA = new Class_HF(var2, var3)).Show((MainScreen)GameCanvas.gameScr);
            Class_HF.AA.BI = false;
            return;
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   private static Class_DS GO(Message var0) {
      Class_DS var1 = null;

      try {
         short var2 = var0.reader().readShort();
         (var1 = new Class_DS(var2)).AC = var0.reader().readUTF();
         if (var1.AC.compareTo(GameScreen.player.name) == 0) {
            var1.AZ = true;
         }

         if (var1.BC == null) {
            var1.BC = new MainObject();
         }

         var1.BC.KP = var0.reader().readInt();
         var1.BC.name = var0.reader().readUTF();
         var1.BC.Lv = var0.reader().readShort();
         short var10001 = var0.reader().readShort();
         short var4;
         short var3 = var4 = var0.reader().readShort();
         var2 = var10001;
         if (var1.BC == null) {
            var1.BC = new MainObject();
         }

         var1.BC.BS = var2;
         var1.BC.BW = var3;
         var1.BC.BX = var4;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var1;
   }

   public static void FV(Message var0) {
      try {
         short var1;
         int var2;
         int var4;
         short var5;
         switch(var0.reader().readByte()) {
         case 0:
            if ((var1 = var0.reader().readShort()) > 0) {
               MainObject.PW = new short[var1];

               for(var2 = 0; var2 < var1; ++var2) {
                  MainObject.PW[var2] = var0.reader().readShort();
               }

               return;
            }
            break;
         case 1:
            MainItem.CL = new short[var5 = var0.reader().readShort()];

            for(var4 = 0; var4 < var5; ++var4) {
               MainItem.CL[var4] = var0.reader().readShort();
            }

            MainItem.CM = new short[var1 = var0.reader().readShort()];

            for(var2 = 0; var2 < var1; ++var2) {
               MainItem.CM[var2] = var0.reader().readShort();
            }

            return;
         case 2:
            MainObject.PT = new short[var5 = var0.reader().readShort()];

            for(var4 = 0; var4 < var5; ++var4) {
               MainObject.PT[var4] = var0.reader().readShort();
            }

            MainObject.PU = new short[var1 = var0.reader().readShort()];

            for(var2 = 0; var2 < var1; ++var2) {
               MainObject.PU[var2] = var0.reader().readShort();
            }

            MainObject.PV = new short[var5 = var0.reader().readShort()];

            for(var4 = 0; var4 < var5; ++var4) {
               MainObject.PV[var4] = var0.reader().readShort();
            }

            return;
         case 3:
            if ((var1 = var0.reader().readShort()) > 0) {
               MainObject.PX = new short[var1];

               for(var2 = 0; var2 < var1; ++var2) {
                  MainObject.PX[var2] = var0.reader().readShort();
               }

               return;
            }
            break;
         default:
            return;
         }
      } catch (Exception var3) {
      }

   }

   public static void FW(Message var0) {
      try {
         switch(var0.reader().readByte()) {
         case 0:
            GameCanvas.gameScr.isFullScreen = false;
            GameCanvas.gameScr.KA = var0.reader().readShort();
            GameCanvas.gameScr.KB = var0.reader().readShort();
            GameCanvas.gameScr.wRec = var0.reader().readShort();
            GameCanvas.gameScr.KD = var0.reader().readShort();
            GameCanvas.gameScr.KE = var0.reader().readInt();
            if (GameCanvas.gameScr.wRec == 0 && GameCanvas.gameScr.KD == 0) {
               GameCanvas.gameScr.isFullScreen = true;
               return;
            }
            break;
         case 1:
            return;
         default:
            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void getDataEff(Message m) {
      try {
         short id;
         MainObject mainObject;
         switch(m.reader().readByte()) {
         case 0:
            byte[] data = new byte[m.reader().available()];
            m.reader().read(data);
            Ageteff.processEffectData(data);
            DataSkillEff.readData(data);
            return;
         case 1:
            id = m.reader().readShort();
            short id2 = m.reader().readShort();
            int time = m.reader().readInt();
            byte typemove = m.reader().readByte();
            byte loop = m.reader().readByte();
            if ((mainObject = GameScreen.AA(id)) != null) {
               mainObject.addDataEff(id2, time, typemove, loop);
               return;
            }
            break;
         case 2:
            id = m.reader().readShort();
            short id3 = m.reader().readShort();
            if ((mainObject = GameScreen.AA(id)) == null) {
               break;
            }
            mainObject.removeDataEff(id3);
         default:
            return;
         }
      } catch (Exception var5) {
      }

   }

   public static void dataListServer(Message m) {
      try {
         byte type;
         short num;
         if ((type = m.reader().readByte()) == 1) {
            num = m.reader().readShort();

            for(int i = 0; i < num; ++i) {
               short num2 = m.reader().readShort();
               ItemQuickOpen o = new ItemQuickOpen(num2);
               GameScreen.vecQuickOpenPotion.addElement(o);
            }
         }

         if (type == 2 && (num = m.reader().readShort()) > 0) {
            short[] listMonsterPokemon;
            int num3;
            GameScreen.listMonsterPokemon = new short[num3 = (listMonsterPokemon = GameScreen.listMonsterPokemon).length + num];
            int j;
            for(j = 0; j < listMonsterPokemon.length; ++j) {
               GameScreen.listMonsterPokemon[j] = listMonsterPokemon[j];
            }

            for(j = listMonsterPokemon.length; j < num3; ++j) {
               short num4 = m.reader().readShort();
               GameScreen.listMonsterPokemon[j] = num4;
            }

            return;
         }
      } catch (Exception var6) {
      }

   }
}
