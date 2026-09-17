import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public final class GameScreen extends MainScreen {
   public static Player player = new Player();
   public static MainObject objFocus = null;
   public static MainObject AC;
   public static MainObject objPvPNew = null;
   public static mVector vecPlayers = new mVector("GameScreen.vecPlayers");
   private static mVector vecEffTam = new mVector("GameScreen.vecEffTam");
   public static mVector VecEffect = new mVector("GameScreen.vecEffect");
   public static mVector VecNum = new mVector("GameScreen.vecNum");
   public static mVector vecBoat = new mVector("GameScreen.vecBoat");
   public static mVector vecObjMove = new mVector("GameScreen.vecObjMove");
   public static mVector vecObjFire = new mVector("GameScreen.vecObjFire");
   private static mVector vecHighDataEff = new mVector();
   private int nump;
   private int numo;
   private MainObject KW;
   private MainObject maxob = new MainObject();
   private static MainItemMap KY;
   private static MainItemMap maxtr = new MainItemMap();
   public static Interface_Game interfaceGame = new Interface_Game();
   public iCommand AL;
   public iCommand AM;
   public iCommand AN;
   public iCommand AO;
   public iCommand AP;
   public iCommand AQ;
   public iCommand AR;
   public iCommand AS;
   public iCommand AT;
   public iCommand AU;
   private iCommand LA;
   public iCommand AV;
   public iCommand AW;
   public iCommand AX;
   public iCommand AY;
   public iCommand AZ;
   public iCommand BA;
   public iCommand BB;
   public iCommand BC;
   public iCommand BD;
   public iCommand BE;
   public iCommand BF;
   public iCommand BG;
   public iCommand BH;
   public iCommand BI;
   public iCommand BJ;
   public iCommand BK;
   public iCommand BL;
   private iCommand LB;
   private iCommand LC;
   public iCommand BM;
   public iCommand BN;
   public iCommand BO;
   private iCommand cmdShowName;
   private iCommand cmdMPHP;
   private iCommand cmdGetItem;
   public iCommand cmdDonotShowHat;
   private iCommand cmdDonotShowWeaponF;
   private iCommand LI;
   public iCommand BP;
   public iCommand BQ;
   public iCommand BR;
   public iCommand BS;
   private iCommand cmdShowSkillBuff;
   private iCommand cmdSpam;
   public iCommand BT;
   public iCommand BU;
   private iCommand cmdShowSkillPlayer;
   private iCommand cmdShowNhanVat;
   public iCommand BV;
   public iCommand BW;
   public iCommand BX;
   public static iCommand BY;
   private static iCommand LN;
   public static iCommand BZ;
   public static iCommand CA;
   public static int numMess = 0;
   public static boolean CC = false;
   public static boolean CD = false;
   public static boolean isPvPNew = false;
   public static int CF;
   public static int CG;
   public static int CH;
   public static int CI;
   public static int CJ;
   public static int CK = 0;
   public static Effect_Map effMap;
   public static Effect_Map effSea;
   public static byte CN = 0;
   public static MainObject CO = null;
   public static boolean CP = false;
   public static boolean CQ = false;
   public static boolean isShowSkillBuff = true;
   public static boolean isShowSkillPlayer = true;
   public static boolean isShowNhanVat = true;
   private InputDialog LO;
   public static mVector CU;
   public static int CV = -1;
   public static int CW = 0;
   public static int tickPvP;
   public static int h12plus = 0;
   public static mVector vecBigBossLittleGraden = new mVector("GameScreen.vecBigBossLittleGraden");
   public static boolean isShowNameSUPER_BOSS;
   public static boolean isShowNameXpArena;
   public static boolean isShowNameSetting;
   public static boolean isShowNameWW;
   public static boolean IP;
   public static boolean IQ;
   public static boolean IR;
   public static boolean IS;
   public static boolean IT;
   public static boolean IU;
   public static boolean IV;
   public static boolean IW;
   public static boolean IX;
   public static MainClan ClanDao;
   public static boolean IZ;
   public int KA;
   public int KB;
   public int wRec;
   public int KD;
   public int KE;
   public boolean isFullScreen = false;
   public static boolean isOnRepeatQuest;
   public static short KH;
   public static boolean isOnSuperBoss;
   public static mVector vecQuickOpenPotion;
   public static short[] listMonsterPokemon;
   public static boolean isOnAutoPB;
   private AutoRepeatQuest LP = new AutoRepeatQuest();
   private AutoSuperBoss LQ = new AutoSuperBoss();
   public static byte typePaintGameScreen;
   public static int dx;
   public static int dy;
   private static mVector LR;
   private static int LS;
   private static int LT;
   private static int LU;
   private static mVector vecEffOnMap;

   static {
      new mVector("GameScreen.getItemLittle");
      isShowNameSUPER_BOSS = true;
      isShowNameXpArena = false;
      isShowNameSetting = true;
      isShowNameWW = false;
      IP = true;
      IQ = true;
      IR = false;
      IS = true;
      IT = false;
      IU = false;
      IV = false;
      IW = false;
      IX = false;
      isOnRepeatQuest = false;
      KH = 0;
      isOnSuperBoss = false;
      vecQuickOpenPotion = new mVector("GameScreen.vecQuickOpenPotion");
      listMonsterPokemon = new short[]{115, 116, 117, 118, 119, 121, 128, 129, 130, 131, 168, 169, 170, 171, 172};
      isOnAutoPB = false;
      typePaintGameScreen = 0;
      dx = 0;
      dy = 0;
      LR = new mVector();
      LS = MotherCanvas.h / 6;
      LU = 0;
      vecEffOnMap = new mVector();
   }

   public final void Show() {
      GameCanvas.gameScr.DA = null;
      GameCanvas.gameScr.DB = null;
      GameCanvas.gameScr.center = null;
      Interface_Game.CO = 0;
      super.Show();
      GameCanvas.mapLogin = null;
      this.setTypeViewPlayer(CN);
      vecEffOnMap.removeAllElements();
      if (GameCanvas.loadmap.mapLang()) {
         if (Player.QP == 1 || Player.QP == 3) {
            Player.AD((byte)7);
         }

         if (Player.QP == 2 || Player.QP == 3) {
            Player.CH();
         }
      }

      this.LI = null;
      AF();
      Clan_Screen.AT = false;
      if (player.clan != null && GameCanvas.ClanScr != null) {
         for(int var1 = 0; var1 < GameCanvas.ClanScr.AM.size(); ++var1) {
            if (((ChatDetail)GameCanvas.ClanScr.AM.elementAt(var1)).AS) {
               Clan_Screen.AT = true;
               return;
            }
         }
      }

   }

   public final void AA(boolean var1) {
      player.RZ = var1;
      player.RY = 0;
      if (var1) {
         super.center = LN;
      } else {
         super.center = null;
      }
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public static void AF() {
      numMess = 0;
      if (GameCanvas.eventScr != null && GameCanvas.eventScr.AY != null) {
         for(int var0 = 0; var0 < GameCanvas.eventScr.AY.size(); ++var0) {
            if (((Class_DS)GameCanvas.eventScr.AY.elementAt(var0)).AY) {
               ++numMess;
            }
         }
      }

   }

   public GameScreen() {
      int var10000 = MotherCanvas.hw;
      var10000 = AvMain.DG;
      var10000 = MotherCanvas.hh;
      var10000 = AvMain.DH;
      this.AL = new iCommand(T.AK, 0, this);
      this.AM = new iCommand(T.revice, 1, this);
      this.BE = new iCommand(T.AN, 2, 0, this);
      this.AN = new iCommand(T.AQ, 3, 1, this);
      this.AO = new iCommand(T.AI, 27, 2, this);
      this.BF = new iCommand(T.AZ, 28, this);
      this.AP = new iCommand(T.AU, 6, 0, this);
      this.AQ = new iCommand(T.AX, 7, 0, this);
      this.AR = new iCommand(T.AY, 8, 0, this);
      BY = new iCommand(T.CA, 9, 0, this);
      this.AS = new iCommand(T.CH, 10, 0, this);
      this.AT = new iCommand(T.CK, 11, 0, this);
      CA = new iCommand(T.AH, 12, 0, this);
      this.AU = new iCommand(T.FY, 13, 0, this);
      this.BI = new iCommand(T.AI, 13, 0, this);
      this.LA = new iCommand(T.CP, 14, 0, this);
      this.AV = new iCommand(T.CP, 15, 0, this);
      this.AW = new iCommand(T.setting, 16, 0, this);
      this.AX = new iCommand(T.AD, 17, 0, this);
      this.AY = new iCommand(T.CN, 20, this);
      this.AZ = new iCommand(T.DP, 21, this);
      this.BA = new iCommand(T.DQ, 22, this);
      this.BB = new iCommand(T.DV, 23, this);
      this.BB.AT = true;
      this.BC = new iCommand(T.DZ, 24, this);
      this.BD = new iCommand(T.EM, 26, this);
      this.BG = new iCommand(T.setting, 29, this);
      if (GameCanvas.isTouch) {
         this.BG = AvMain.AA(this.BG, 1);
      }

      this.BH = new iCommand(T.AI, 30, this);
      this.BJ = new iCommand(T.GI, 33, this);
      this.BK = new iCommand(T.trade, 35, this);
      this.BL = new iCommand(T.BN + " " + T.EW, 36, this);
      this.LB = new iCommand(T.LV, 37, this);
      new iCommand(T.AI, 38, this);
      this.LC = new iCommand(T.IQ, 39, this);
      this.BM = new iCommand(T.Clan, 40, this);
      this.BO = new iCommand(T.NM, 41, this);
      this.BN = new iCommand(T.NN, 42, this);
      this.cmdShowName = new iCommand(T.PK, 43, this);
      this.LI = new iCommand(T.AZ, 47, this);
      this.BP = new iCommand(T.QY, 48, this);
      this.BQ = new iCommand(T.RG, 49, this);
      this.BR = new iCommand(T.CA, 52, this);
      this.BS = new iCommand("LockChat", 53, this);
      this.cmdShowSkillBuff = new iCommand(T.showSkillBuff, 55, this);
      this.cmdSpam = new iCommand(T.TK, 57, this);
      this.BT = new iCommand(T.TR, 58, this);
      this.BU = new iCommand(T.UI, 59, this);
      this.cmdShowSkillPlayer = new iCommand(T.showSkillPlayer, 60, this);
      this.cmdShowNhanVat = new iCommand(T.showNhanVat, 61, this);
      LN = new iCommand(T.VD, 62, this);
      this.BV = new iCommand(T.VL, 63, this);
      this.BW = new iCommand(T.VQ, 64, this);
      this.BX = new iCommand(T.VW, 65, this);
      if (!GameCanvas.isTouch) {
         super.DB = this.AL;
         super.DA = this.AP;
      }

      new Effect_Skill();
   }

   public final void commandPointer(int index, int subIndex) {
      iCommand iCommand;
      iCommand iCommand2;
      mVector mVec;
      mVector mVec2;
      String str;
      SaveRms var25;
      switch(index) {
      case -52:
         return;
      case -51:
      case -50:
      case -49:
      case -48:
      case -47:
      case -46:
      case -45:
      case -44:
      case -43:
      case -42:
      case -41:
      case -40:
      case -39:
      case -38:
      case -37:
      case -36:
      case -35:
      case -34:
      case -33:
      case -32:
      case -31:
      case -30:
      case -29:
      case -28:
      case -27:
      case -26:
      case -25:
      case -24:
      case -23:
      case -22:
      case -21:
      case -20:
      case -19:
      case -18:
      case -17:
      case -16:
      case -15:
      case -14:
      case -13:
      case -12:
      case -11:
      case -10:
      case -9:
      case -8:
      case -7:
      case -6:
      case -5:
      case -4:
      case -3:
      case -2:
      case -1:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      default:
         break;
      case 0:
         if (objFocus != null && !objFocus.returnAction()) {
            player.nextFocus();
            return;
         }
         break;
      case 1:
         (mVec = new mVector()).addElement(new iCommand(T.revice, 2, 1, this));
         mVec.addElement(this.BE);
         GameCanvas.Start_Normal_DiaLog(T.hoiRevice, mVec, true);
         return;
      case 2:
         if (subIndex == 0) {
            GameCanvas.Start_Normal_DiaLog(T.hoiGoHome, new iCommand(T.AN, 2, 2, this), true);
            return;
         }

         if (subIndex == 1) {
            GlobalService.getInstance().Player_Revice((byte)1);
            GameCanvas.end_Dialog();
            return;
         }

         if (subIndex == 2) {
            GlobalService.getInstance().Player_Revice((byte)0);
            GameCanvas.end_Dialog();
            return;
         }
         break;
      case 3:
         GlobalService.getInstance().Get_Xp_Map_Train((byte)subIndex);
         return;
      case 4:
         GameCanvas.menu.startAt(this.AG(), 2, T.AU);
         return;
      case 5:
         GlobalService.getInstance().Set_PK((byte)((byte)subIndex), (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 6:
         GlobalService.getInstance().Update_Pk_Point();
         GameCanvas.tabInven.AA((byte)0);
         GameCanvas.tabAllScr.Show((MainScreen)this);
         player.resetAction();
         GameCanvas.clearAll();
         if (CV == 10 || CV == 14 || CV == 15 || CV == 16) {
            MainHelp.AA(true);
            return;
         }
         break;
      case 7:
         if (player.Hp <= 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.HR);
            return;
         }

         mVec2 = new mVector();

         for(subIndex = -1; subIndex < 6; ++subIndex) {
            if (subIndex == -1) {
               iCommand = new iCommand(T.BA, 5, -1, this);
            } else {
               (iCommand = new iCommand(T.VX[subIndex + 3], 5, subIndex + 3, this)).setFraCaption((FrameImage)AvMain.fraPk, 3, (subIndex + 3) * 3, (int)0);
            }

            mVec2.addElement(iCommand);
         }

         GameCanvas.menu.startAt(mVec2, 2, T.AX);
         return;
      case 8:
         if (player.Hp <= 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.HR);
            return;
         }

         if (player.typePK == 0) {
            GlobalService.getInstance().Set_PK((byte)-1, (byte)0);
            Interface_Game.addInfoPlayerNormal(T.tatdosat, mFont.tahoma_7_yellow);
            return;
         }

         GlobalService.getInstance().Set_PK((byte)0, (byte)0);
         Interface_Game.addInfoPlayerNormal(T.batdosat, mFont.tahoma_7_yellow);
         return;
      case 9:
         if (objFocus != null) {
            objFocus.BD();
            return;
         }
         break;
      case 10:
         if (Player.vecParty.size() > 0) {
            PartyScreen.AI().AF();
            PartyScreen.AI().Show((MainScreen)GameCanvas.gameScr);
            return;
         }
         break;
      case 11:
         if (AC != null) {
            GlobalService.getInstance().AD((byte)0, (short)AC.ID);
            return;
         }
         break;
      case 12:
         GameCanvas.loginScr.Show();
         if (GameCanvas.loginScr.AC.getText().length() > 0) {
            GameCanvas.loginScr.AA(false, (byte)0, GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
            return;
         }

         GameCanvas.fristLoginScr.cmdBegin.AD();
         return;
      case 13:
         ListChar_Screen.IndexCharSelected = -1;
         GameCanvas.loginScr.Show();
         return;
      case 14:
         GameCanvas.chatTabScr.Show((MainScreen)GameCanvas.gameScr);
         return;
      case 15:
         if (AC != null) {
            AB(AC.name);
            return;
         }
         break;
      case 16:
         mVec = this.getMenuGameNew();
         GameCanvas.menu.startAt(mVec, 2, T.setting);
         return;
      case 17:
         if (GameCanvas.currentScreen == this && !GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && !Player.isGhost) {
            Class_IO.AA().AB();
            return;
         }
         break;
      case 18:
         if (Player.isMPHP) {
            Player.isMPHP = false;
            var25 = GameCanvas.saveRms;
            SaveRms.AF();
            this.setCaptionCmdAutoGetItem();
            return;
         }

         Class_AI var21;
         (var21 = new Class_AI()).AA();
         GameCanvas.AA((MainDialog)var21);
         return;
      case 19:
         MsgAutoFire var19;
         (var19 = new MsgAutoFire()).AA();
         GameCanvas.AA((MainDialog)var19);
         return;
      case 20:
         if (AC != null) {
            GlobalService.getInstance().AA((byte)0, (int)AC.ID);
            return;
         }
         break;
      case 21:
         if (Class_FN.AA) {
            Class_FN.AG().Show((MainScreen)this);
            return;
         }

         GlobalService.getInstance().AA((byte)2, (int)0);
         return;
      case 22:
         GlobalService.getInstance().AA((byte)2, (byte)2, (byte)0);
         return;
      case 23:
         GameCanvas.eventScr.Show((MainScreen)this);
         return;
      case 24:
         if (Interface_Game.typeTouch == 0) {
            Interface_Game.typeTouch = 1;
         } else {
            Interface_Game.typeTouch = 0;
         }

         this.BC.caption = T.DZ;
         if (Interface_Game.typeTouch == 0) {
            this.BC.caption = T.EA;
            Interface_Game.addInfoPlayerNormal(T.chuyenkeypad, mFont.tahoma_7_yellow);
         } else {
            Interface_Game.addInfoPlayerNormal(T.chuyentouch, mFont.tahoma_7_yellow);
         }

         try {
            CRes.saveRMS("SUB_TYPETOUCH", new byte[]{Interface_Game.typeTouch});
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         Interface_Game var26 = interfaceGame;
         Interface_Game.AI();
         Interface_Game.setPosTouch();
         return;
      case 25:
         if (Player.isGetItem) {
            Player.isGetItem = false;
            var25 = GameCanvas.saveRms;
            SaveRms.AH();
            this.setCaptionCmdAutoGetItem();
            return;
         }

         Class_AH var24;
         (var24 = new Class_AH()).AA();
         GameCanvas.AA((MainDialog)var24);
         return;
      case 26:
         (mVec2 = new mVector()).addElement(new iCommand(T.RY, 54, 0, this));
         mVec2.addElement(new iCommand(T.RZ, 54, 1, this));
         GameCanvas.menu.startAt(mVec2, 2, T.EM);
         return;
      case 27:
         GameCanvas.Start_Normal_DiaLog(T.FN, this.AU, true);
         return;
      case 28:
         if (AC != null) {
            str = AC.name;
            GlobalService.getInstance().Show_Player_Info(str);
            return;
         }
         break;
      case 29:
         if (CN != 0) {
            GameScreen var23 = this;
            mVec = new mVector();
            iCommand = new iCommand(T.GB, 32, this);
            mVec.addElement(iCommand);

            for(int var18 = 0; var18 < vecPlayers.size(); ++var18) {
               MainObject var14;
               if ((var14 = (MainObject)vecPlayers.elementAt(var18)).indexTeam != 0) {
                  iCommand var17 = new iCommand(T.GA + var14.name, 31, var14.ID, var23);
                  mVec.addElement(var17);
               }
            }

            GameCanvas.menu.startAt(mVec, 2, T.GC);
            return;
         }
         break;
      case 30:
         if (CN != 0) {
            GlobalService.getInstance().AE();
            return;
         }
         break;
      case 31:
         CO = MainObject.get_Object((int)subIndex, (byte)0);
         return;
      case 32:
         CO = null;
         return;
      case 33:
         if (this.LO == null) {
            this.LO = new InputDialog();
            iCommand2 = new iCommand(T.AD, 34, this);
            this.LO.AA(T.GJ, iCommand2, false, T.GI);
         } else {
            this.LO.tfInput.AB("");
         }

         GameCanvas.AA((MainDialog)this.LO);
         return;
      case 34:
         GlobalService.getInstance().World_Chanel((byte)0, (String)this.LO.tfInput.getText());
         return;
      case 35:
         if (AC != null) {
            GlobalService.getInstance().AA((byte)6, AC.ID, AC.typeObject, 0, "");
            return;
         }
         break;
      case 36:
         if (GameCanvas.AQ()) {
            this.AN();
            return;
         }

         if (GameCanvas.language != 1) {
            this.LO = GameCanvas.AA(T.WP, T.LV, this.LB);
            GameCanvas.AA((MainDialog)this.LO);
            return;
         }

         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.SP);
         break;
      case 37:
         iCommand2 = null;
         if (this.LO.AB != null) {
            String[] var22 = new String[this.LO.AB.length];

            for(subIndex = 0; subIndex < this.LO.AB.length; ++subIndex) {
               var22[subIndex] = this.LO.AB[subIndex].getText();
            }

            if (var22.length >= 2) {
               GlobalService.getInstance().AC(var22[0], var22[1]);
            }
         }

         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LW);
         return;
      case 38:
         GameCanvas.Start_Normal_DiaLog(T.LX, this.BI, true);
         return;
      case 39:
         (mVec = new mVector()).addElement(GameCanvas.gameScr.AQ);
         mVec.addElement(GameCanvas.gameScr.AR);
         GameCanvas.menu.startAt(mVec, 2, T.IQ);
         return;
      case 40:
         if (Player.isGetDataClan != 1) {
            GlobalService.getInstance().Clan_CMD((byte)9, "", 0, (byte)0);
            GameCanvas.AA(T.BP, true);
            Player.isGetDataClan = 0;
            return;
         }

         if (player.clan != null) {
            if (GameCanvas.ClanScr == null) {
               GameCanvas.ClanScr = new Clan_Screen(player.clan);
            }

            GameCanvas.ClanScr.Show((MainScreen)this);
            return;
         }
         break;
      case 41:
         if (AC != null) {
            GlobalService.getInstance().Clan_CMD((byte)10, "", AC.ID, (byte)0);
            return;
         }
         break;
      case 42:
         if (AC != null && AC.clan != null) {
            GlobalService.getInstance().Clan_CMD((byte)11, "", objFocus.clan.ID, (byte)0);
            return;
         }
         break;
      case 43:
         isShowNameSetting = !isShowNameSetting;
         return;
      case 44:
         GlobalService.getInstance().AE((byte)0, (byte)((byte)subIndex));
         return;
      case 45:
         GlobalService.getInstance().AE((byte)1, (byte)((byte)subIndex));
         return;
      case 46:
         str = "";

         try {
            str = GameMidlet.AF(GameMidlet.AG());
         } catch (Exception var5) {
         }

         if (str != null && str.length() > 10) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(str);
            return;
         }

         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.ES);
         return;
      case 47:
         for(index = 0; index < vecPlayers.size(); ++index) {
            MainObject var16;
            if ((var16 = (MainObject)vecPlayers.elementAt(index)) != player) {
               str = var16.name;
               GlobalService.getInstance().Show_Player_Info(str);
               return;
            }
         }

         return;
      case 48:
         if (Player.QK.size() == 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.QZ);
            return;
         }

         if (Player.QK.size() != 1) {
            for(index = 0; index < Player.QK.size(); ++index) {
               Class_FA var15;
               if ((var15 = (Class_FA)Player.QK.elementAt(index)).AB == 0) {
                  var15.AA();
               } else {
                  var15.AB = 0;
               }
            }

            return;
         }

         for(index = 0; index < Player.QK.size(); ++index) {
            ((Class_FA)Player.QK.elementAt(index)).AA();
         }

         return;
      case 49:
         if (LoadMap.specMap == 10) {
            mVec2 = this.AO();
         } else {
            mVec2 = this.AP();
         }

         GameCanvas.menu.startAt(mVec2, 2, T.RG);
         return;
      case 50:
         byte var13 = 3;
         if (Player.QN == 0) {
            var13 = 4;
         }

         GlobalService.getInstance().AA((byte)2, (byte)subIndex, var13, player.name);
         InfoShowNotify var11 = new InfoShowNotify(player.name + ": " + T.WU[subIndex], var13);
         Interface_Game.vecQuickChatLoL.insertElementAt(var11, 0);
         return;
      case 51:
         if (subIndex >= 0 && subIndex < T.WV.length) {
            player.BC = T.WV[subIndex];
            GlobalService.getInstance().AA(T.WV[subIndex]);
            return;
         }
         break;
      case 52:
         if (AC != null) {
            GlobalService.getInstance().AC(ReadMessenge.AK, AC.ID, AC.typeObject);
            return;
         }
         break;
      case 53:
         if (AC != null) {
            GlobalService.getInstance().AA("ops lockchat " + AC.name);
            return;
         }
         break;
      case 54:
         if (AC != null) {
            GlobalService.getInstance().AA((byte)0, (short)AC.ID, (byte)((byte)subIndex));
            return;
         }
         break;
      case 55:
         isShowSkillBuff = !isShowSkillBuff;
         var25 = GameCanvas.saveRms;
         SaveRms.AI();
         return;
      case 56:
         if (subIndex >= 0 && subIndex < GameMidlet.AJ.length) {
            String[] var10000 = GameMidlet.AJ;
            GameMidlet.AI();
            return;
         }
         break;
      case 57:
         Class_AT var8;
         (var8 = new Class_AT()).AA();
         GameCanvas.AB(var8);
         return;
      case 58:
         if (AC != null) {
            GlobalService.getInstance().AC((byte)2, (short)AC.ID, AC.typeObject);
            return;
         }
         break;
      case 59:
         GlobalService.getInstance().AJ((byte)-91, (byte)0);
         return;
      case 60:
         isShowSkillPlayer = !isShowSkillPlayer;
         return;
      case 61:
         isShowNhanVat = !isShowNhanVat;
         return;
      case 62:
         if (player.RY >= 63 && player.RY <= 65) {
            GlobalService.getInstance().VotBanhChung((byte)1, (byte)1, (byte)1);
         } else if (player.RY >= 66 && player.RY <= 87) {
            GlobalService.getInstance().VotBanhChung((byte)1, (byte)1, (byte)2);
         } else {
            GlobalService.getInstance().VotBanhChung((byte)1, (byte)1, (byte)0);
         }

         GameCanvas.gameScr.AA(false);
         return;
      case 63:
         GameCanvas.SudoScr = new Sudo_Screen(player.PJ);
         return;
      case 64:
         if (AC != null) {
            GlobalService.getInstance().AB((byte)19, "", AC.ID, (byte)0);
            return;
         }
         break;
      case 65:
         GlobalService.getInstance().Send_Pet((byte)3);
         return;
      case 99:
         if (isOnRepeatQuest) {
            AutoRepeatQuest.AB();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XJ);
            return;
         }

         iCommand2 = new iCommand(T.XF, 100, this);
         GameCanvas.Start_Normal_DiaLog(T.XE, iCommand2, true);
         return;
      case 100:
         GameCanvas.end_Dialog();
         isOnRepeatQuest = true;
         if (AutoRepeatQuest.CC()) {
            AutoRepeatQuest.isStart = true;
            return;
         }
         break;
      case 101:
         if (isOnSuperBoss) {
            AutoSuperBoss.AB();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XU);
            return;
         }

         iCommand2 = new iCommand(T.XF, 102, this);
         GameCanvas.Start_Normal_DiaLog(T.XT, iCommand2, true);
         return;
      case 102:
         GameCanvas.end_Dialog();
         isOnSuperBoss = true;
         AutoSuperBoss.AA = true;
         return;
      case 103:
         iCommand2 = new iCommand(100 + T.percent, 104, this);
         iCommand iCommand3 = new iCommand(125 + T.percent, 105, this);
         iCommand = new iCommand(150 + T.percent, 106, this);
         if (GameCanvas.percentUPCV == 0) {
            iCommand.isDisplay = false;
            iCommand.isSaved = false;
            iCommand3.isDisplay = false;
            iCommand3.isSaved = false;
            iCommand2.isDisplay = true;
            iCommand2.isSaved = true;
         } else if (GameCanvas.percentUPCV == 25) {
            iCommand3.isDisplay = true;
            iCommand3.isSaved = true;
            iCommand.isDisplay = false;
            iCommand.isSaved = false;
            iCommand2.isDisplay = false;
            iCommand2.isSaved = false;
         } else if (GameCanvas.percentUPCV == 50) {
            iCommand.isDisplay = true;
            iCommand.isSaved = true;
            iCommand2.isDisplay = false;
            iCommand2.isSaved = false;
            iCommand3.isDisplay = false;
            iCommand3.isSaved = false;
         }

         mVector mVec3;
         (mVec3 = new mVector()).addElement(iCommand2);
         mVec3.addElement(iCommand3);
         mVec3.addElement(iCommand);
         GameCanvas.Start_Normal_DiaLog(T.chooseSpeedupTitle, mVec3, true);
         return;
      case 104:
         GameCanvas.percentUPCV = 0;
         saveSpeedUpRMS(0);
         GameCanvas.end_Dialog();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "100" + T.percent);
         return;
      case 105:
         GameCanvas.percentUPCV = 25;
         saveSpeedUpRMS(25);
         GameCanvas.end_Dialog();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "125" + T.percent);
         return;
      case 106:
         GameCanvas.percentUPCV = 50;
         saveSpeedUpRMS(50);
         GameCanvas.end_Dialog();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.successSpeedUp + "150" + T.percent);
         return;
      case 107:
         if (isOnAutoPB) {
            AutoBattlefield.StopAutoBattlefield();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notifyOffAutoPB);
            return;
         }
         AutoBattlefield.CheckMapAutoBattlefield();
         return;
      case 108: {//Bật/Tắt tàn sát
          if (player.isAutoFireNew108) {
            player.isAutoFireNew108 = false;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tự động đánh quái tắt 108");
            return;
          }
          player.isAutoFireNew108 = true;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tự động đánh quái bật 108");
          return;
      }
      case 109: {//Bật/Tắt tự động pk
          GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tự động tàn sát 109");
          return;
      }
      case 110: {//Bật/Tắt giữ level
          GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Giữ level 110");
          return;
      }
      case 111: {//Bật/Tắt đóng băng quái
          if (player.isDongBangQuai111) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tắt đóng băng quái");
            player.isDongBangQuai111 = false;
            return;
          }
          player.isDongBangQuai111 = true;
          GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bật đóng băng quái");
          return;
      }
      case 112: {//Bật/Tắt chua co gi
          if (!player.isGomQuai112) {
            player.isGomQuai112 = true;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bật gom quái");
            return;
          }
          player.isGomQuai112 = false;
          GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tắt gom quái");
          return;
      }
      case 114: {//tăng 5 tốc độ game
          
          return;
      }
      case 115: {//giảm 5 tốc độ game
          
          return;
      }
      case 118: {
          if (!player.isAutoRevice) {
            player.isAutoRevice = true;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bật tự động hồi sinh");
            return;
          }
          player.isAutoRevice = false;
          GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Tắt tự động hồi sinh");
          return;
      }
      case 119: {//Mở menu lọc đồ
          AThMadaraMOD.getInstance().openMenuSellItem();
          return;
      }
      }
   }

   private static void saveSpeedUpRMS(int var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2 = new DataOutputStream(var1);

      try {
         var2.writeInt(var0);
         String var4 = System.getProperty("microedition.platform");
         GameMidlet.AA(var4 + T.YB, var1.toByteArray());
         var2.close();
      } catch (Exception var3) {
      }
   }

   private void AN() {
      mVector var1 = new mVector();

      for(int var2 = 0; var2 < GameMidlet.AL.length; ++var2) {
         iCommand var3;
         (var3 = new iCommand(GameMidlet.AL[var2], 56, var2, this)).setFraCaption((FrameImage)AvMain.fraMoney, 1, 7, (int)2);
         var1.addElement(var3);
      }

      GameCanvas.menu.startAt(var1, 2, T.LV);
   }

   private mVector AO() {
      mVector var1 = new mVector();

      for(int var2 = 0; var2 < T.WU.length; ++var2) {
         iCommand var3 = new iCommand(T.WU[var2], 50, var2, this);
         var1.addElement(var3);
      }

      return var1;
   }

   private mVector AP() {
      mVector var1 = new mVector();

      for(int var2 = 0; var2 < T.WV.length; ++var2) {
         iCommand var3 = new iCommand(T.WV[var2], 51, var2, this);
         var1.addElement(var3);
      }

      return var1;
   }

   private mVector getMenuGameNew() {
      mVector var1 = new mVector();
      this.cmdGetItem = new iCommand(T.on + T.autoGetItem, 25, this);
      if (Player.isGetItem) {
         this.cmdGetItem.caption = T.off + T.autoGetItem;
         this.cmdGetItem.isDonotCloseMenu = true;
      }

      this.cmdMPHP = new iCommand(T.on + T.autoMPHP, 18, this);
      if (Player.isMPHP) {
         this.cmdMPHP.caption = T.off + T.autoMPHP;
         this.cmdMPHP.isDonotCloseMenu = true;
      }

      if (isShowNameSetting) {
         this.cmdShowName.caption = T.off + T.PK;
      } else {
         this.cmdShowName.caption = T.on + T.PK;
      }

      iCommand var2 = new iCommand(T.autoFire, 19, this);
      String var3 = isOnRepeatQuest ? T.off + T.autoRepeatQuest : T.on + T.autoRepeatQuest;
      iCommand var4 = new iCommand(var3, 99, this);
      var1.addElement(var4);
      var3 = isOnSuperBoss ? T.off + T.autoSuperBoss : T.on + T.autoSuperBoss;
      var4 = new iCommand(var3, 101, this);
      var1.addElement(var4);
      var3 = isOnAutoPB ? T.off + T.titleAutoPB : T.on + T.titleAutoPB;
      var4 = new iCommand(var3, 107, this);
      var1.addElement(var4);
      var3 = T.chooseSpeedUpMenu;
      var4 = new iCommand(var3, 103, this);
      var1.addElement(var4);
      var1.addElement(var2);
      var1.addElement(this.cmdMPHP);
      var1.addElement(this.cmdGetItem);
      var1.addElement(this.cmdSpam);
      var1.addElement(this.cmdShowName);
      this.cmdDonotShowHat = new iCommand(T.off + T.showHat, 44, 1, this);
      if (player.isDonotShowHat != 0) {
         this.cmdDonotShowHat = new iCommand(T.on + T.showHat, 44, 0, this);
      }

      if (Player.idFashion >= 0) {
         this.cmdDonotShowWeaponF = new iCommand(T.off + T.PO, 45, 1, this);
         if (player.isDonotShowWeaponF != 0) {
            this.cmdDonotShowWeaponF = new iCommand(T.on + T.PO, 45, 0, this);
         }

         var1.addElement(this.cmdDonotShowWeaponF);
      }

      var1.addElement(this.cmdDonotShowHat);
      if (isShowSkillBuff) {
         this.cmdShowSkillBuff.caption = T.off + T.showSkillBuff;
      } else {
         this.cmdShowSkillBuff.caption = T.on + T.showSkillBuff;
      }

      var1.addElement(this.cmdShowSkillBuff);
      if (isShowSkillPlayer) {
         this.cmdShowSkillPlayer.caption = T.off + T.showSkillPlayer;
      } else {
         this.cmdShowSkillPlayer.caption = T.on + T.showSkillPlayer;
      }

      var1.addElement(this.cmdShowSkillPlayer);
      if (isShowNhanVat) {
         this.cmdShowNhanVat.caption = T.off + T.showNhanVat;
      } else {
         this.cmdShowNhanVat.caption = T.on + T.showNhanVat;
      }

      var1.addElement(this.cmdShowNhanVat);
      return var1;
   }
   
   private mVector getMenuMOD() {
      mVector var1 = new mVector();
      this.cmdGetItem = new iCommand(T.on + T.autoGetItem, 25, this);
      if (Player.isGetItem) {
         this.cmdGetItem.caption = T.off + T.autoGetItem;
         this.cmdGetItem.isDonotCloseMenu = true;
      }

      this.cmdMPHP = new iCommand(T.on + T.autoMPHP, 18, this);
      if (Player.isMPHP) {
         this.cmdMPHP.caption = T.off + T.autoMPHP;
         this.cmdMPHP.isDonotCloseMenu = true;
      }

      if (isShowNameSetting) {
         this.cmdShowName.caption = T.off + T.PK;
      } else {
         this.cmdShowName.caption = T.on + T.PK;
      }

      iCommand var2 = new iCommand(T.autoFire, 19, this);
      String var3 = isOnRepeatQuest ? T.off + T.autoRepeatQuest : T.on + T.autoRepeatQuest;
      iCommand var4 = new iCommand(var3, 99, this);
      var1.addElement(var4);
      var3 = isOnSuperBoss ? T.off + T.autoSuperBoss : T.on + T.autoSuperBoss;
      var4 = new iCommand(var3, 101, this);
      var1.addElement(var4);
      var3 = isOnAutoPB ? T.off + T.titleAutoPB : T.on + T.titleAutoPB;
      var4 = new iCommand(var3, 107, this);
      var1.addElement(var4);
      var3 = T.chooseSpeedUpMenu;
      var4 = new iCommand(var3, 103, this);
      var1.addElement(var4);
      var1.addElement(var2);
      var1.addElement(this.cmdMPHP);
      var1.addElement(this.cmdGetItem);
      var1.addElement(this.cmdSpam);
      var1.addElement(this.cmdShowName);
      this.cmdDonotShowHat = new iCommand(T.off + T.showHat, 44, 1, this);
      if (player.isDonotShowHat != 0) {
         this.cmdDonotShowHat = new iCommand(T.on + T.showHat, 44, 0, this);
      }

      if (Player.idFashion >= 0) {
         this.cmdDonotShowWeaponF = new iCommand(T.off + T.PO, 45, 1, this);
         if (player.isDonotShowWeaponF != 0) {
            this.cmdDonotShowWeaponF = new iCommand(T.on + T.PO, 45, 0, this);
         }

         var1.addElement(this.cmdDonotShowWeaponF);
      }

      var1.addElement(this.cmdDonotShowHat);
      if (isShowSkillBuff) {
         this.cmdShowSkillBuff.caption = T.off + T.showSkillBuff;
      } else {
         this.cmdShowSkillBuff.caption = T.on + T.showSkillBuff;
      }

      var1.addElement(this.cmdShowSkillBuff);
      if (isShowSkillPlayer) {
         this.cmdShowSkillPlayer.caption = T.off + T.showSkillPlayer;
      } else {
         this.cmdShowSkillPlayer.caption = T.on + T.showSkillPlayer;
      }

      var1.addElement(this.cmdShowSkillPlayer);
      if (isShowNhanVat) {
         this.cmdShowNhanVat.caption = T.off + T.showNhanVat;
      } else {
         this.cmdShowNhanVat.caption = T.on + T.showNhanVat;
      }

      var1.addElement(this.cmdShowNhanVat);
      return var1;
   }

   private void setCaptionCmdAutoGetItem() {
      GameCanvas.menuCur.updateMenuGame(this.getMenuGameNew());
   }

   public final mVector AG() {
      player.resetAction();
      mVector var1;
      (var1 = new mVector()).addElement(this.AZ);
      var1.addElement(this.BA);
      var1.addElement(this.BP);
      var1.addElement(this.BQ);
      var1.addElement(this.BV);
      var1.addElement(this.BX);
      var1.addElement(this.BU);
      var1.addElement(this.BB);
      var1.addElement(this.LC);
      if (Player.vecParty.size() > 0) {
         var1.addElement(this.AS);
      }

      if (player.clan != null) {
         var1.addElement(this.BM);
      }

      var1.addElement(this.LA);
      var1.addElement(this.AW);
      var1.addElement(this.BJ);
      var1.addElement(this.AU);
      return var1;
   }

   public final void paint(mGraphics g) {
      dx = 0;
      dy = 0;
      if (LoadMap.timeVibrateScreen > 0) {
         if (LoadMap.timeVibrateScreen > 100) {
            dy = CRes.random_Am_0(3);
            if (LoadMap.timeVibrateScreen == 101) {
               LoadMap.timeVibrateScreen = 0;
            }
         } else {
            dy = CRes.random_Am_0(3);
            dx = CRes.random_Am(0, 2);
         }

         --LoadMap.timeVibrateScreen;
      }
      g.translate(dx, dy);
      g.translate(-MainScreen.cameraMain.xCam, -MainScreen.cameraMain.yCam);
      mGraphics mGraphics;
      int i   ;
      int var6;
      MainObject var10;
      MainEffect var11;
      if (typePaintGameScreen == 0) {
         if (LoadMap.isOnlineMap) {
            mGraphics = g;
            GameScreen gamescr = this;
            if (GameCanvas.mapBack != null) {
               GameCanvas.mapBack.paint(g);
            }

            for(i    = 0; i    < vecBigBossLittleGraden.size(); ++i   ) {
               ((BigBossLittleGraden)vecBigBossLittleGraden.elementAt(i   )).AA(mGraphics, MainScreen.cameraMain.xCam);
            }

            paintTree(mGraphics, 0);
            paintTree(mGraphics, 1);
            GameCanvas.loadmap.paint(mGraphics);
            if (this.isFullScreen) {
               mGraphics.drawRecAlpa(0, 0, GameCanvas.loadmap.mapW * 24, GameCanvas.loadmap.mapH * 24, this.KE);
            } else if (this.wRec > 0) {
               mGraphics.fillRecAlpla(this.KA, this.KB, this.wRec, this.KD, this.KE);
            }

            if (GameCanvas.mapBack != null) {
               GameCanvas.mapBack.paintLast(mGraphics);
            }

            if (effSea != null) {
               effSea.paintSea(mGraphics);
            }

            Point point;
            for(i    = 0; i    < vecEffOnMap.size(); ++i   ) {
               if ((point = (Point)vecEffOnMap.elementAt(i   )).fSmall == 0) {
                  AvMain.fraImgEffOnMap0.drawFrame(point.subType * 3 + point.frame, point.x, point.y, point.dis, 3, mGraphics);
               }
            }

            Interface_Game.paintMoveTo(mGraphics);
            if (LoadMap.specMap == 10 && !GameCanvas.lowGraphic) {
               for(i    = 0; i    < vecPlayers.size(); ++i   ) {
                  if ((var10 = (MainObject)vecPlayers.elementAt(i   )).BZ() && !var10.isDie) {
                     if (Interface_Game.imgRankSkill == null) {
                        (Interface_Game.imgRankSkill = mImage.createImage("/interface/lol11.png")).setDefault();
                     }

                     mGraphics.drawRegion(Interface_Game.imgRankSkill, 0, 0, Interface_Game.imgRankSkill.width, Interface_Game.imgRankSkill.height, 0, var10.xAnchor - Interface_Game.imgRankSkill.width / 2, var10.yAnchor, 3);
                     mGraphics.drawRegion(Interface_Game.imgRankSkill, 0, 0, Interface_Game.imgRankSkill.width, Interface_Game.imgRankSkill.height, 2, var10.xAnchor + Interface_Game.imgRankSkill.width / 2, var10.yAnchor, 3);
                  }
               }
            }

            paintTree(mGraphics, 2);
            DataSkillEff dataSkillEff;
            if (isShowSkillPlayer) {
               i    = 0;

               label559:
               while(true) {
                  if (i    >= VecEffect.size()) {
                     i    = 0;

                     while(true) {
                        if (i    >= vecHighDataEff.size()) {
                           break label559;
                        }

                        if ((dataSkillEff = (DataSkillEff)vecHighDataEff.elementAt(i   )) != null) {
                           dataSkillEff.paintBottomEff(mGraphics);
                        }

                        ++i   ;
                     }
                  }

                  if ((var11 = (MainEffect)VecEffect.elementAt(i   )) != null && (isShowSkillPlayer || var11.objFireMain != null && var11.objFireMain.ID == player.ID) && var11.levelPaint == -1 && !var11.BI && !var11.isStop) {
                     var11.paint(mGraphics);
                  }

                  ++i   ;
               }
            }

            CRes.quickSort(vecPlayers);
            this.nump = 0;
            this.numo = 0;
            this.maxob.y = 10000;
            maxtr.y = 10000;

            while(gamescr.nump < vecPlayers.size() || gamescr.numo < LoadMap.mItemMap[3].size()) {
               try {
                  gamescr.KW = gamescr.maxob;
                  KY = maxtr;
                  if (gamescr.nump < vecPlayers.size()) {
                     gamescr.KW = (MainObject)vecPlayers.elementAt(gamescr.nump);
                  }

                  if (gamescr.numo < LoadMap.mItemMap[3].size()) {
                     KY = (MainItemMap)LoadMap.mItemMap[3].elementAt(gamescr.numo);
                  }

                  if (KY == null || KY.TypeItem != 1 && gamescr.KW.y + gamescr.KW.CQ < KY.y + LoadMap.wTile || KY.TypeItem == 1 && gamescr.KW.y + gamescr.KW.CQ < KY.y) {
                     ++gamescr.nump;
                     if (KY == null) {
                        ++gamescr.numo;
                     }

                     var10 = gamescr.KW;
                     if (!GameCanvas.lowGraphic ? false : (var10 == null ? false : (objFocus != null && var10 == objFocus ? false : MainObject.getDistance(var10.x, var10.y, player.x, player.y) >= 120))) {
                        gamescr.KW.a_(mGraphics);
                     } else if (AB(gamescr.KW)) {
                        var10 = gamescr.KW;
                        if (gamescr.KW.ID != player.ID && gamescr.KW.typeObject == 0 && !isShowNhanVat) {
                           gamescr.KW.AC(mGraphics);
                        } else {
                           gamescr.KW.paint(mGraphics);
                        }
                     }
                  } else {
                     ++gamescr.numo;
                     if (KY.AB()) {
                        KY.paint(mGraphics);
                     }
                  }
               } catch (Exception var9) {
               }
            }

            if (effMap != null) {
               effMap.AA(mGraphics);
            }

            for(i    = 0; i    < LoadMap.vecPointChange.size(); ++i   ) {
               point = (Point)LoadMap.vecPointChange.elementAt(i   );
               mGraphics.drawRegion(AvMain.imgSelect, 0, 0, 12, 16, LoadMap.mTranPointChangeMap[point.dis], point.x + GameCanvas.gameTick % 6 * point.vx, point.y + GameCanvas.gameTick % 6 * point.vy, 3);
               AvMain.AA(mGraphics, point.name, point.x2 + GameCanvas.gameTick % 6 * point.vx, point.y2 + GameCanvas.gameTick % 6 * point.vy, point.f);
            }

            if (isShowSkillPlayer) {
               i    = 0;

               label491:
               while(true) {
                  if (i    >= VecEffect.size()) {
                     for(i    = 0; i    < vecHighDataEff.size(); ++i   ) {
                        if ((dataSkillEff = (DataSkillEff)vecHighDataEff.elementAt(i   )) != null) {
                           dataSkillEff.AA(mGraphics);
                        }
                     }

                     for(i    = 0; i    < VecNum.size(); ++i   ) {
                        if ((var11 = (MainEffect)VecNum.elementAt(i   )) != null && var11.levelPaint == 0 && !var11.BI && !var11.isStop) {
                           var11.paint(mGraphics);
                        }
                     }

                     i    = 0;

                     while(true) {
                        if (i    >= VecNum.size()) {
                           break label491;
                        }

                        if ((var11 = (MainEffect)VecNum.elementAt(i   )) != null && var11.levelPaint == 1 && !var11.BI && !var11.isStop) {
                           var11.paint(mGraphics);
                        }

                        ++i   ;
                     }
                  }

                  if ((var11 = (MainEffect)VecEffect.elementAt(i   )) != null && (isShowSkillPlayer || var11.objFireMain != null && var11.objFireMain.ID == player.ID) && var11.levelPaint == 0 && !var11.BI && !var11.isStop) {
                     var11.paint(mGraphics);
                  }

                  ++i   ;
               }
            }

            if (GameCanvas.BS != null) {
               GameCanvas.BS.paint(mGraphics);
            }

            paintTree(mGraphics, 4);
            paintTree(mGraphics, 5);
            if (isShowSkillPlayer) {
               for(i    = 0; i    < VecEffect.size(); ++i   ) {
                  if ((var11 = (MainEffect)VecEffect.elementAt(i   )) != null && (isShowSkillPlayer || var11.objFireMain != null && var11.objFireMain.ID == player.ID) && var11.levelPaint == 1 && !var11.BI && !var11.isStop) {
                     var11.paint(mGraphics);
                  }
               }
            }

            if (!getIsOffAdmin((byte)1) && objFocus != null && CN == 0) {
               if (objFocus.typeObject == 0 || objFocus.typeObject == 2) {
                  objFocus.AA(mGraphics, (byte)objFocus.colorName, 1);
               }

               if (objFocus.Action != 4 && (objFocus.typeObject == 0 || objFocus.typeObject == 1)) {
                  objFocus.AK(mGraphics);
               }
            }

            if (!getIsOffAdmin((byte)2)) {
               for(i    = 0; i    < vecPlayers.size(); ++i   ) {
                  if ((var10 = (MainObject)vecPlayers.elementAt(i   )) == player && Player.isGhost) {
                     int var10000 = player.x;
                     var10000 = player.y;
                     var10000 = player.hOne;
                     Player.AL();
                  } else if (var10.LS != null) {
                     var10.LS.paint(mGraphics);
                  }
               }
            }

            Interface_Game.paintShowHelp(mGraphics, true);
            if (GameCanvas.currentScreen == gamescr) {
               Interface_Game var18;
               if (CN == 0) {
                  if (LoadMap.specMap != 3 && !getIsOffAdmin((byte)1)) {
                     Interface_Game.paintIconFocus(mGraphics);
                  }

                  GameCanvas.resetTrans(mGraphics);
                  if (GameCanvas.currentScreen == GameCanvas.gameScr && LoadMap.specMap != 3 && !getIsOffAdmin((byte)1)) {
                     if (!GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && GameCanvas.subDialog == null) {
                        Interface_Game.paintNumMess(mGraphics, 0, 0);
                        interfaceGame.paintInGame(mGraphics);
                        var18 = interfaceGame;
                        Interface_Game.paintVecEffKickAn(mGraphics);
                     }

                     Interface_Game.paintAutoRepeatQuest(mGraphics);
                     Interface_Game.paintWaitingForMonster(mGraphics);
                     Interface_Game.paintAutoSuperBoss(mGraphics);
                  }

                  if (LoadMap.specMap == 3) {
                     Interface_Game.PaintLoadData(mGraphics, LoadMap.AV, LoadMap.AW, MotherCanvas.hw - 47, MotherCanvas.h / 8, 100, 12, 15);
                     mFont.tahoma_7b_black.drawString(mGraphics, Player.strTimeChange, MotherCanvas.hw, MotherCanvas.h / 8 - 10, 2);
                  } else if (!getIsOffAdmin((byte)1)) {
                     if (!isPvPNew) {
                        var18 = interfaceGame;
                        Interface_Game.paintInfoFocus(mGraphics);
                        boolean var17 = true;
                        if (GameCanvas.isSmallScreen) {
                           var17 = false;
                        }

                        Interface_Game.paintInfoPlayer(mGraphics, 3, 3 + h12plus, var17, mFont.tahoma_7_black);
                     } else {
                        Interface_Game.paintPvPNew(mGraphics, player, objPvPNew);
                     }
                  }
               } else if (GameCanvas.currentScreen == GameCanvas.gameScr && !GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null) {
                  GameCanvas.resetTrans(mGraphics);
                  var18 = interfaceGame;
                  Interface_Game.AC(mGraphics);
                  if (GameCanvas.isTouch) {
                     gamescr.AD(mGraphics);
                  }
               }

               GameCanvas.resetTrans(mGraphics);
               if (!getIsOffAdmin((byte)1)) {
                  interfaceGame.AI(mGraphics);
               }

               if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !GameCanvas.menuCur.isShowMenu) {
                  if (GameCanvas.isTouch) {
                     gamescr.AD(mGraphics);
                  } else {
                     GameScreen var14;
                     if ((var14 = gamescr).DA != null) {
                        var14.DA.AC(mGraphics, GameCanvas.BG / 3 << 1, MotherCanvas.h - iCommand.hButtonCmdNor / 2 - 1);
                     }

                     if (var14.DB != null) {
                        var14.DB.AC(mGraphics, MotherCanvas.w - (GameCanvas.BG / 3 << 1), MotherCanvas.h - iCommand.hButtonCmdNor / 2 - 1);
                     }

                     if (var14.center != null) {
                        var14.center.AC(mGraphics, MotherCanvas.hw, MotherCanvas.h - iCommand.hButtonCmdNor / 2 - 1);
                     }
                  }
               }

               Interface_Game.paintShowNear(mGraphics);
               if (Interface_Game.CB == null) {
                  Interface_Game.AA(mGraphics, MotherCanvas.hw - Interface_Game.BW / 2, 0 + h12plus, false, Interface_Game.BZ);
               }

               if (!getIsOffAdmin((byte)1)) {
                  for(i    = 0; i    < vecBigBossLittleGraden.size(); ++i   ) {
                     BigBossLittleGraden var15;
                     var6 = (var15 = (BigBossLittleGraden)vecBigBossLittleGraden.elementAt(i   )).AC + h12plus;
                     int var7 = MotherCanvas.w - var15.AB - 2;
                     int var8 = mFont.tahoma_7b_black.getWidth(var15.AI);
                     AvMain.AC(mGraphics, var7, var6 - 1, var15.AB, 26);
                     if (var15.AA == 0) {
                        AvMain.fraPk.drawFrame(12 + GameCanvas.gameTick / 3 % 3, var7 + var15.AB / 2 - var8 / 2 - 4, var6 + 6, 0, 3, mGraphics);
                     } else {
                        AvMain.fraPk.drawFrame(15 + GameCanvas.gameTick / 3 % 3, var7 + var15.AB / 2 - var8 / 2 - 4, var6 + 6, 0, 3, mGraphics);
                     }

                     AvMain.AA(mGraphics, var15.AI, var7 + var15.AB / 2 + 6, var6, 2);
                     var6 += 13;
                     Interface_Game.AA(mGraphics, (byte)1, var15.AD, var15.AE, var7 + (var15.AB - 44) / 2, var6, 10, 4, 44, -1, false, 0, false, 0);
                     var6 += 5;
                     Interface_Game.AA(mGraphics, (byte)2, var15.AF, var15.AG, var7 + (var15.AB - 44) / 2, var6, 10, 4, 44, -1, false, 0, false, 0);
                  }
               }
            }
         } else {
            if (LoadMap.specMap == 5) {
               MapOff_RedLine.AA(g);
            }

            if (LoadMap.specMap == 8) {
               MapGotoSky.AA(g);
            }

            if (LoadMap.specMap == 12) {
               MapGotoGod.AA(g);
            }
         }
      } else {
         mGraphics = g;
         mGraphics var16 = g;
         g.setColor(-13499120);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
         g.setColor(-697986);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - LS, MotherCanvas.w, LS << 1);
         g.setColor(-690283);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - LS / 2, MotherCanvas.w, LS);
         g.setColor(-3534275);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - LS - 8, MotherCanvas.w, 8);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 + LS, MotherCanvas.w, 8);
         g.setColor(-5238730);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 - LS - 12, MotherCanvas.w, 4);
         g.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + MotherCanvas.h / 4 * 3 + LS + 8, MotherCanvas.w, 4);
         g.setColor(-814166);

         for(var6 = MotherCanvas.h / 4 * 3 - LS - 12; var6 < MotherCanvas.h / 4 * 3 + LS + 12; var6 += 12) {
            var16.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam + var6, MotherCanvas.w, 1);
         }

         for(var6 = 0; var6 < LR.size(); ++var6) {
            Point var13;
            if ((var13 = (Point)LR.elementAt(var6)).dis <= 1) {
               var16.setColor(var13.AE);
               var16.fillRect(MainScreen.cameraMain.xCam + var13.x - var13.AC / 2, MainScreen.cameraMain.yCam + var13.y - var13.AD / 2, var13.AC, var13.AD);
            }
         }

         for(i    = 0; i    < VecEffect.size(); ++i   ) {
            if ((var11 = (MainEffect)VecEffect.elementAt(i   )) != null && var11.objMainEff != null && var11.objMainEff == player && var11.levelPaint == -1 && !var11.BI && !var11.isStop) {
               var11.paint(mGraphics);
            }
         }

         CRes.quickSort(vecPlayers);

         for(i    = 0; i    < vecPlayers.size(); ++i   ) {
            if (!(var10 = (MainObject)vecPlayers.elementAt(i   )).isRemove && (var10.NG || var10 == player)) {
               var10.paint(mGraphics);
            }
         }

         for(i    = 0; i    < VecEffect.size(); ++i   ) {
            if ((var11 = (MainEffect)VecEffect.elementAt(i   )) != null && var11.objMainEff != null && var11.objMainEff == player && var11.levelPaint >= 0 && !var11.BI && !var11.isStop) {
               var11.paint(mGraphics);
            }
         }
      }

      interfaceGame.paintShowtime(g);
       AThMadaraFunc.paintShowDateTime(g);
   }

   private static boolean AB(MainObject var0) {
      if (var0 == null) {
         return false;
      } else if (MainObject.AB(var0)) {
         return true;
      } else {
         return var0.BZ();
      }
   }

   private static void paintTree(mGraphics var0, int var1) {
      for(int var2 = 0; var2 < LoadMap.mItemMap[var1].size(); ++var2) {
         if ((KY = (MainItemMap)LoadMap.mItemMap[var1].elementAt(var2)).AB()) {
            KY.paint(var0);
         }
      }

   }

   public final void update() {
      if (!LoadMap.isOnlineMap) {
         if (LoadMap.specMap == 5) {
            MapOff_RedLine.AA();
         }

         ObjMove var32;
         if (LoadMap.specMap == 8) {
            MapGotoSky.AB();

            for(; 0 < vecObjMove.size(); vecObjMove.removeElement(0)) {
               if (!(var32 = (ObjMove)vecObjMove.elementAt(0)).AE) {
                  AA(var32);
               }
            }
         }

         if (LoadMap.specMap == 12) {
            MapGotoGod.AC();

            for(; 0 < vecObjMove.size(); vecObjMove.removeElement(0)) {
               if (!(var32 = (ObjMove)vecObjMove.elementAt(0)).AE) {
                  AA(var32);
               }
            }
         }

      } else {
         if (GameCanvas.BS != null) {
            GameCanvas.BS.update();
         }

         int var1;
         for(var1 = 0; var1 < vecBigBossLittleGraden.size(); ++var1) {
            ((BigBossLittleGraden)vecBigBossLittleGraden.elementAt(var1)).AA();
         }

         if (Interface_Game.CO > -5) {
            Interface_Game.AQ();
         }

         if (GameCanvas.mapBack != null) {
            GameCanvas.mapBack.AB();
            GameCanvas.mapBack.AA();
         }

         for(var1 = 0; var1 < LoadMap.mItemMap[3].size(); ++var1) {
            MainItemMap var2;
            if ((var2 = (MainItemMap)LoadMap.mItemMap[3].elementAt(var1)).TypeItem == 1 || var2.TypeItem == 1) {
               var2.update();
            }
         }

         for(; 0 < vecObjMove.size(); vecObjMove.removeElement(0)) {
            ObjMove var15;
            if (!(var15 = (ObjMove)vecObjMove.elementAt(0)).AE) {
               AA(var15);
            }
         }

         MainObject var18;
         try {
            while(0 < vecObjFire.size()) {
               Message var16 = (Message)vecObjFire.elementAt(0);

               try {
                  if (!LoadMapScreen.isNextMap) {
                     break;
                  }

                  short var17 = var16.reader().readShort();
                  byte var3 = var16.reader().readByte();
                  if ((var18 = MainObject.get_Object((int)var17, (byte)var3)) == null || GameCanvas.lowGraphic && player != var18 && MainObject.getDistance(player.x, player.y, var18.x, var18.y) >= 240) {
                     break;
                  }

                  if (var18.LD <= 0) {
                     var18.CS = 20;
                  }

                  var18.LD = 150;
                  var18.Hp = var16.reader().readInt();
                  var18.Mp = var16.reader().readInt();
                  short var19 = var16.reader().readShort();
                  byte var4;
                  if ((var4 = var16.reader().readByte()) <= 0) {
                     vecObjFire.removeElement(0);
                     continue;
                  }

                  mVector var5 = new mVector();

                  for(int var6 = 0; var6 < var4; ++var6) {
                     short var7 = var16.reader().readShort();
                     byte var8 = var16.reader().readByte();
                     Object_Effect_Skill var30 = new Object_Effect_Skill(var7, var8);
                     int var31 = var16.reader().readInt();
                     int var9 = 0;
                     if (var16.isOld == 0) {
                        var9 = var16.reader().readInt();
                     }

                     int var10 = var16.reader().readInt();
                     byte var11 = var16.reader().readByte();
                     var30.mEffTypePlus = new int[var11];
                     var30.AG = new int[var11];
                     var30.mEff_Time_Plus = new int[var11];

                     for(int var12 = 0; var12 < var11; ++var12) {
                        var30.mEffTypePlus[var12] = var16.reader().readShort();
                        var30.AG[var12] = var16.reader().readShort();
                        var30.mEff_Time_Plus[var12] = var16.reader().readShort();
                     }

                     var30.AA(var31, var10, var9);
                     var5.addElement(var30);
                  }

                  MainSkill var28 = new MainSkill((short)-1, var19);
                  var18.AB(var28, var5);
               } catch (Exception var13) {
               }

               vecObjFire.removeElement(0);
            }
         } catch (Exception var14) {
         }

         for(var1 = 0; var1 < vecPlayers.size(); ++var1) {
            if ((var18 = (MainObject)vecPlayers.elementAt(var1)) != null && !var18.isRemove) {
               var18.update();
               var18.ySort = var18.y + var18.CQ;
               if (var18.typeObject == 0) {
                  if (AB(var18)) {
                     var18.LE = 0;
                  } else {
                     ++var18.LE;
                     if (var18.LE >= 1000) {
                        var18.isRemove = true;
                     }
                  }

                  if (isPvPNew && var18 != player && objPvPNew == null) {
                     objPvPNew = var18;
                  }
               }
            } else if (GameCanvas.lowGraphic) {
               vecPlayers.removeElement(var1);
               --var1;
            } else if (var18 != null && var18.LC > 0) {
               --var18.LC;
               var18.update();
               var18.ySort = var18.y + var18.CQ;
            } else {
               vecPlayers.removeElement(var1);
               --var1;
            }
         }

         for(var1 = 0; var1 < vecHighDataEff.size(); ++var1) {
            DataSkillEff var20;
            if ((var20 = (DataSkillEff)vecHighDataEff.elementAt(var1)) != null) {
               var20.AA();
               if (var20.wantDestroy) {
                  vecHighDataEff.removeElement(var1);
               }
            }
         }

         LT = 0;

         MainEffect var22;
         for(var1 = 0; var1 < VecEffect.size(); ++var1) {
            if ((var22 = (MainEffect)VecEffect.elementAt(var1)) != null && !var22.BI) {
               if (!var22.isStop) {
                  ++LT;
                  var22.update();
               }
            } else {
               VecEffect.removeElement(var1);
               --var1;
            }
         }

         for(var1 = 0; var1 < VecNum.size(); ++var1) {
            if ((var22 = (MainEffect)VecNum.elementAt(var1)) != null && !var22.BI && !var22.isStop) {
               var22.update();
            }
         }

         if (tickPvP > 0 && --tickPvP == 0 && super.center == this.LI) {
            super.center = null;
         }

         int var26;
         if (CC) {
            MainScreen.cameraMain.AA(CH - CF, CI - CG, AV());
         } else if (CN == 0) {
            if (!CD) {
               boolean var29 = false;
               if (tickPvP > 0 && vecPlayers.size() > 1) {
                  for(var26 = 0; var26 < vecPlayers.size(); ++var26) {
                     MainObject var21 = (MainObject)vecPlayers.elementAt(var26);
                     if (GameCanvas.loadmap.idMapLoadMap == 59) {
                        if (var21.typeObject == 1) {
                           MainScreen.cameraMain.AA(var21.x - MotherCanvas.w / 2, var21.y - (MotherCanvas.h / 3 << 1));
                           var29 = true;
                           break;
                        }
                     } else if (var21 != player) {
                        MainScreen.cameraMain.AA(var21.x - MotherCanvas.w / 2, var21.y - (MotherCanvas.h / 3 << 1));
                        var29 = true;
                        break;
                     }
                  }

                  if (this.LI == null && GameCanvas.loadmap.idMapLoadMap != 59) {
                     this.LI = new iCommand(T.AZ, 47, this);
                     GameCanvas.gameScr.center = GameCanvas.gameScr.LI;
                     GameCanvas.gameScr.LI = AvMain.AA(GameCanvas.gameScr.LI, 0);
                  }
               }

               if (!var29) {
                  if (LoadMap.specMap == 7) {
                     MainScreen.cameraMain.AA(player.x - MotherCanvas.w / 2, player.y - (MotherCanvas.h / 5 << 2));
                  } else {
                     MainScreen.cameraMain.AA(player.x - MotherCanvas.w / 2, player.y - (MotherCanvas.h / 3 << 1));
                  }
               }
            }
         } else if (CO == null) {
            MainScreen.cameraMain.AA(CH - CF, CI - CG, AV());
         } else {
            MainScreen.cameraMain.AA(CO.x - MotherCanvas.w / 2, CO.y - (MotherCanvas.h / 3 << 1));
         }

         MainScreen.cameraMain.AA();
         if (objFocus != null && objFocus.returnAction()) {
            objFocus = null;
            super.center = null;
         }

         var1 = vecEffTam.size();

         for(var26 = 0; var26 < var1; ++var26) {
            MainEffect var23;
            if ((var23 = (MainEffect)vecEffTam.elementAt(0)).CreateEffectSkill()) {
               int var24;
               if ((var24 = AA(VecEffect)) == VecEffect.size()) {
                  VecEffect.addElement(var23);
               } else {
                  VecEffect.setElementAt(var23, var24);
               }
            }

            vecEffTam.removeElement(0);
         }

         Interface_Game.AN();
         Interface_Game.AO();
         Interface_Game var10000 = interfaceGame;
         Interface_Game.AP();
         if (effMap != null) {
            effMap.AB();
         }

         if (effSea != null) {
            effSea.AA();
         }

         Point var25;
         for(var26 = 0; var26 < vecEffOnMap.size(); ++var26) {
            ++(var25 = (Point)vecEffOnMap.elementAt(var26)).f;
            if (var25.fSmall == 0) {
               if (var25.f == var25.fRe / 6 << 2) {
                  var25.frame = 1;
               }

               if (var25.f == var25.fRe / 6 * 5) {
                  var25.frame = 2;
               }

               if (var25.f >= var25.fRe && vecEffOnMap.size() > 20 || var25.f > var25.fRe + var25.fRe / 2 || var25.f >= 100) {
                  vecEffOnMap.removeElement(var25);
                  --var26;
               }
            }
         }

         if (typePaintGameScreen != 0) {
            for(var26 = 0; var26 < LR.size(); ++var26) {
               (var25 = (Point)LR.elementAt(var26)).update();
               if (var25.x < -10) {
                  var25.x = MotherCanvas.w + CRes.random(MotherCanvas.w / 4);
                  var25.y = 5 + var26 * 10 + CRes.random_Am_0(4);
                  AA(var25);
               }
            }
         }

         if (GameCanvas.DC == 1) {
            if (GameCanvas.currentDialog == null && GameCanvas.currentScreen == this) {
               ++LU;
            }

            if (LU >= 50) {
               GameCanvas.DC = 0;
               GameCanvas.gameScr.cmdSpam.AD();
            }
         }

         if ((var26 = GameCanvas.gameTick % 4000) == 3999) {
            checkRemoveImage(1);
         } else if (var26 == 1000) {
            checkRemoveImage(2);
         } else if (var26 == 2000) {
            checkRemoveImage(3);
         } else if (var26 == 3000) {
            checkRemoveImage(4);
         } else if (var26 == 2100) {
            GlobalService.getInstance().AA(vecPlayers);
         }

         if (ReadMessenge.AJ > 0) {
            if (LoadMap.BD == 1000) {
               LoadMap.BD = 0;
               GameCanvas.loadmap.AC();
            }

            if (--ReadMessenge.AJ == 10) {
               GameCanvas.loadmap.AC();

               for(int var27 = 0; var27 < LoadMap.mItemMap.length; ++var27) {
                  CRes.quickSort(LoadMap.mItemMap[var27]);
               }
            }
         }

         if (isOnRepeatQuest) {
            this.LP.StartAutoRepeatQuest();
         }

         if (isOnSuperBoss) {
            this.LQ.StartAutoSuperBoss();
         }

         if (isOnAutoPB) {
            AutoBattlefield.StartAutoBattlefield();
         }

      }
   }

   private static boolean AU() {
      return (!GameCanvas.lowGraphic || LT <= 20) && LT <= 100;
   }

   public static void AA(short var0, byte var1) {
      for(int var3 = 0; var3 < vecObjMove.size(); ++var3) {
         ObjMove var2 = (ObjMove)vecObjMove.elementAt(var3);
         if (var0 == var2.AB && var2.AA == 0) {
            var2.AE = true;
         }
      }

   }

   public static void checkRemoveImage(int var0) {
      MainEffect var1;
      if (var0 != 0 && var0 != 1) {
         if (var0 != 0 && var0 != 2) {
            if (var0 == 0 || var0 == 3) {
               ObjectData.checkDelHash(ObjectData.hashImageNPC, 120, false);
               ObjectData.checkDelHash(ObjectData.HashImageCharPart, 120, false);
               ObjectData.checkDelHash(ObjectData.hashImageItem, 120, false);
               ObjectData.checkDelHash(ObjectData.hashImageMaterialPotion, 120, false);

               for(var0 = 0; var0 < VecNum.size(); ++var0) {
                  if ((var1 = (MainEffect)VecNum.elementAt(var0)).isStop) {
                     var1.BI = true;
                  }
               }
            } else if (var0 == 0 || var0 == 4) {
               ObjectData.checkDelHash(ObjectData.HashImageEffClient, 120, false);
               ObjectData.checkDelHash(ObjectData.HashImageOtherNew, 120, false);
               ObjectData.checkDelHash(ObjectData.hashImageBoat, 120, false);
               ObjectData.checkDelHash(ObjectData.HashImageFashion, 120, false);
            }
         } else {
            ObjectData.checkDelHash(ObjectData.hashImagePotion, 120, false);
            ObjectData.checkDelHash(ObjectData.HashImageMonster, 120, false);
            CharPartInfo.checkDelHashCharPart((MyHashTable)CharPartInfo.hashMyPart, 240);
            ObjectData.checkDelHash(ObjectData.hashImageItemOther, 120, false);
         }
      } else {
         ObjectData.checkDelHash_Data(DataSkillEff.ALL_EFF_DATA, 120, false);
         ObjectData.checkDelHash(ObjectData.HashImageItemMap, 120, false);
         ObjectData.checkDelHash(ObjectData.hashImageIconClan, 120, false);
         ObjectData.checkDelHash(ObjectData.hashImageIconClanBig, 120, false);

         for(var0 = 0; var0 < VecEffect.size(); ++var0) {
            if ((var1 = (MainEffect)VecEffect.elementAt(var0)).isStop) {
               var1.BI = true;
            }
         }
      }

      System.gc();
   }

   private static void AA(ObjMove var0) {
      MainObject var1;
      if ((var1 = MainObject.get_Object((int)var0.AB, (byte)var0.AA)) == null) {
         if (var0.AA == 1) {
            MainMonster var4;
            MainMonster var2 = var4 = new MainMonster(var0.AB, var0.AC, var0.AD);
            vecPlayers.addElement(var2);
            GlobalService.getInstance().monster_info(var0.AB);
            var4.timeLoadInfo = GameCanvas.timeNow;
            return;
         }

         if (var0.AA == 0 || var0.AA == 2) {
            Class_ET var3;
            (var3 = new Class_ET(var0.AB, var0.AA, "", var0.AC, var0.AD)).Dir = CRes.random(2) == 0 ? 0 : 2;
            vecPlayers.addElement(var3);
            GlobalService.getInstance().char_info(var0.AB);
            var3.timeLoadInfo = GameCanvas.timeNow;
            return;
         }
      } else if (MainObject.getDistance(var1.toX, var1.toY, var0.AC, var0.AD) >= var1.CN) {
         if (var1 == player) {
            var1.toX = var0.AC;
            var1.toY = var0.AD;
            var1.NR = true;
            return;
         }

         if (var1.Action != 2 && var1.Action != 4 && var1.skillCurrent == null) {
            if (var1.timeBeginUpdateMove < 0) {
               var1.timeBeginUpdateMove = GameCanvas.gameTick % 10 + 1;
            }

            var1.toXNew = var0.AC;
            var1.toYNew = var0.AD;
         }
      }

   }

   public final void handleKeyPress() {
      if (player.RZ && GameCanvas.AL[5] && super.center != null) {
         GameCanvas.AA(5);
         GameCanvas.AB(5);
         super.center.AD();
      }

      if (CN == 0) {
         if (player != null) {
            player.updateKey();
         }
      } else {
         boolean var1 = false;
         if (GameCanvas.isKeyPressed(0)) {
            CF += 10;
            var1 = true;
         } else if (GameCanvas.isKeyPressed(2)) {
            CF -= 10;
            var1 = true;
         } else if (GameCanvas.isKeyPressed(1)) {
            CG += 10;
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            CG -= 10;
            var1 = true;
         }

         if (var1) {
            CJ = 40;
            if (!CC) {
               CH = MainScreen.cameraMain.xCam;
               CI = MainScreen.cameraMain.yCam;
               CF = 0;
               CG = 0;
               CC = true;
            } else {
               if (CH - CF <= 0) {
                  CF = CH;
               }

               if (CH - CF >= MainScreen.cameraMain.AE) {
                  CF = CH - MainScreen.cameraMain.AE;
               }

               if (CI - CG <= 0) {
                  CG = CI;
               }

               if (CI - CG >= MainScreen.cameraMain.AF) {
                  CG = CI - MainScreen.cameraMain.AF;
               }
            }
         }
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      super.updatePointer();
      if (LoadMap.isOnlineMap) {
         if (GameCanvas.currentScreen == GameCanvas.gameScr) {
            if (CN == 0) {
               interfaceGame.AF();
               return;
            }

            interfaceGame.AG();
         }

      } else {
         if (LoadMap.specMap == 5 || LoadMap.specMap == 8 || LoadMap.specMap == 12) {
            MapOff_RedLine.AB();
         }

      }
   }

   public static void AA(Boat var0, boolean var1) {
      vecBoat.addElement(var0);
      LoadMap.mItemMap[3].addElement(var0);
      if (var1) {
         CRes.quickSort(LoadMap.mItemMap[3]);
      }

   }

   public static void AA(Boat var0) {
      vecBoat.removeElement(var0);
      LoadMap.mItemMap[3].removeElement(var0);
   }

   public static void addPlayer(MainObject var0) {
      vecPlayers.addElement(var0);
   }

   public static MainItemMap addEffectAuto(String key, String value) {
      return new EffectAuto(value);
   }

   public static void RemoveLoadMap() {
      for(int var0 = 0; var0 < vecPlayers.size(); ++var0) {
         MainObject var1;
         if ((var1 = (MainObject)vecPlayers.elementAt(var0)) != player && (var1.typeObject != 10 || var1.IDMainShiper != player.ID)) {
            var1.isRemove = true;
         }
      }

      vecBoat.removeAllElements();
      GameScreen var10000 = GameCanvas.gameScr;
      GameCanvas.gameScr.KE = 0;
      GameCanvas.gameScr.KA = 0;
      GameCanvas.gameScr.KB = 0;
      GameCanvas.gameScr.wRec = 0;
      GameCanvas.gameScr.KD = 0;
      GameCanvas.gameScr.isFullScreen = false;
   }

   public static void AI() {
      for(int var0 = 0; var0 < vecPlayers.size(); ++var0) {
         MainObject var1;
         if ((var1 = (MainObject)vecPlayers.elementAt(var0)).typeObject == 2) {
            vecPlayers.removeElement(var1);
            --var0;
         }
      }

   }

   public static int AA(mVector var0) {
      int var1 = var0.size();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         MainEffect var3;
         if ((var3 = (MainEffect)var0.elementAt(var2)).isStop && !var3.BI) {
            return var2;
         }
      }

      return var1;
   }

   public static void addEffectEnd(short var0, int var1, int var2, int var3, int var4, byte var5, MainObject var6) {
      if (var6 == player || AU()) {
         Effect_End var7 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5, var6);
         vecEffTam.addElement(var7);
      }
   }

   public static void addEffectEnd(short var0, int var1, int var2, int var3, byte var4, MainObject var5) {
      if (var5 == player || AU()) {
         Effect_End var6 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5);
         vecEffTam.addElement(var6);
      }
   }

   public static void addEffectEnd_ToX_ToY(short var0, int var1, int var2, int var3, int var4, int var5, byte var6, MainObject var7) {
      if (var7 == player || AU()) {
         Effect_End var8 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5, var6, var7);
         vecEffTam.addElement(var8);
      }
   }

   public static void AA(MainObject var0, short var1, short var2) {
      MainBuff var3;
      (var3 = new MainBuff((short)2060)).AA = var0;
      var3.AA(var2);
      var0.AB(var3);
   }

   public static void addEffectEnd_ObjTo(short var0, int var1, int var2, int var3, short var4, byte var5, byte var6, MainObject var7) {
      if (var7 == player || AU()) {
         Effect_End var8 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5, var6, var7, 0);
         vecEffTam.addElement(var8);
      }
   }

   public static void AA(short var0, int var1, int var2, int var3, short var4, byte var5, short var6, byte var7, MainObject var8) {
      if (var8 == player || AU()) {
         Effect_End var9 = new Effect_End((short)179, (byte)10, var2, var3, var4, var5, var6, (byte)0, var8, 0);
         vecEffTam.addElement(var9);
      }
   }

   public static MainEffect AB(short var0, int var1, int var2, int var3, short var4, byte var5, byte var6, MainObject var7) {
      return new Effect_End(var0, (byte)var1, var2, var3, var4, var5, var6, var7, 0);
   }

   public static void AA(short var0, int var1, int var2, int var3, short var4, byte var5, byte var6, MainObject var7, int var8) {
      if (var7 == player || AU()) {
         Effect_End var9 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5, var6, var7, var8);
         vecEffTam.addElement(var9);
      }
   }

   public static void AA(MainSkill var0, MainObject var1, mVector var2) {
      Effect_Skill var3 = new Effect_Skill(var0.AA, 0, var1, var2);
      vecEffTam.addElement(var3);
   }

   public static void AA(short var0, MainObject var1, Object_Effect_Skill var2, int var3, int var4) {
      mVector var5;
      (var5 = new mVector()).addElement(var2);
      Effect_Skill var6 = new Effect_Skill(-1, 0, var1, var5, var3, var4);
      vecEffTam.addElement(var6);
   }

   public static void AA(MainSkill var0, MainObject var1) {
      Effect_Skill var2 = new Effect_Skill(var0, var1);
      vecEffTam.addElement(var2);
   }

   public static void AB(MainSkill var0, MainObject var1) {
      Effect_Skill var2 = new Effect_Skill(var0, var1, var0.AI, var0.AJ, var0.AL);
      vecEffTam.addElement(var2);
   }

   public static void addEffectNum(String var0, int var1, int var2, byte var3) {
      Class_BN var4 = new Class_BN(var0, var1, var2, var3);
      if ((var1 = AA(VecNum)) == VecNum.size()) {
         VecNum.addElement(var4);
      } else {
         VecNum.setElementAt(var4, var1);
      }
   }

   public static void AA(String var0, int var1, int var2, byte var3, FrameImage var4, int var5) {
      Class_BN var6 = new Class_BN(var0, var1, var2, var3, var4, var5);
      if ((var1 = AA(VecNum)) == VecNum.size()) {
         VecNum.addElement(var6);
      } else {
         VecNum.setElementAt(var6, var1);
      }
   }

   public static void addEffectNumBig_NEW_AP(int var0, int var1, int var2, int var3, byte var4) {
      Class_BN var5 = new Class_BN(var0, var1, var2, var3, var4);
      if ((var1 = AA(VecNum)) == VecNum.size()) {
         VecNum.addElement(var5);
      } else {
         VecNum.setElementAt(var5, var1);
      }
   }

   public static MainEffect AB(short var0, int var1, int var2, int var3, byte var4, MainObject var5) {
      return new Effect_End(var0, (byte)0, var2, var3, var4, (MainObject)null);
   }

   public static MainEffect AA(short var0, int var1, int var2, int var3, int var4, int var5) {
      return new Effect_End(var0, (byte)var1, var2, var3, var4, var5, (byte)0, (MainObject)null);
   }

   public static MainEffect AB(short var0, int var1, int var2, int var3, int var4, byte var5, MainObject var6) {
      return new Effect_End((short)144, (byte)1, var2, var3, 2300, (byte)0, (MainObject)null);
   }

   public static void isPaintNormal() {
      typePaintGameScreen = 0;
      LR.removeAllElements();

      for(int var0 = 0; var0 < vecPlayers.size(); ++var0) {
         MainObject var1;
         if ((var1 = (MainObject)vecPlayers.elementAt(var0)).NG) {
            var1.NG = false;
         }
      }

   }

   public static void beginPaintSpec() {
      int var0 = MotherCanvas.h / 10;
      typePaintGameScreen = 1;
      LR.removeAllElements();

      for(int var1 = 0; var1 < var0; ++var1) {
         Point var2;
         AA(var2 = new Point());
         LR.addElement(var2);
      }

   }

   private static Point AA(Point var0) {
      var0.vx = -CRes.random(25, 35);
      var0.AC = CRes.random(16, 30);
      var0.AD = CRes.random(2, 7) / 2;
      var0.AE = -806463;
      if (CRes.random(4) == 3) {
         var0.AE = -985749;
      }

      if (var0.y >= MotherCanvas.h / 4 * 3 - LS - 12 && var0.y <= MotherCanvas.h / 4 * 3 + LS + 12) {
         var0.dis = CRes.random(4);
      } else {
         var0.dis = CRes.random(10);
      }

      return var0;
   }

   public final void setTypeViewPlayer(byte var1) {
      CN = var1;
      CC = false;
      CF = 0;
      CG = 0;
      if (CN != 0) {
         CP = true;
         CO = null;
         super.center = null;
         super.DA = this.BG;
         super.DB = this.BH;
         MainScreen.cameraMain.AA(player.x - MotherCanvas.w / 2, player.y - (MotherCanvas.h / 3 << 1));
         CH = MainScreen.cameraMain.AC;
         CI = MainScreen.cameraMain.AD;
      }

   }

   public static void AB(boolean var0) {
      CD = var0;
   }

   public static void AA(byte var0, int var1, int var2, int var3, byte var4) {
      if (!GameCanvas.lowGraphic && vecEffOnMap.size() <= 100) {
         Point var5;
         (var5 = new Point(var1 + CRes.random_Am_0(2), var2 + CRes.random_Am_0(2))).fSmall = 0;
         var5.subType = var4 % 2;
         if (vecEffOnMap.size() < 50) {
            var5.fRe = CRes.random(15, 25);
         } else {
            var5.fRe = CRes.random(8, 15);
         }

         var5.dis = var3;
         vecEffOnMap.addElement(var5);
      }
   }

   public static void AA(short var0, int var1, int var2, int var3) {
        GameScreen.addEffectEnd((short)157, 0, var1, var2, (byte)var3, (MainObject)null);
   }

   public static void AB(int var0, int var1) {
      MainHelp var2 = new MainHelp(var0, var1);
      CU.addElement(var2);
   }

   public static void AA(String var0) {
      GlobalService.getInstance().Show_Player_Info(var0);
   }

   public static void AB(String var0) {
      GameCanvas.chatTabScr.AB(var0, "", "", (byte)0, true);
      if (Class_AT.AA(1, var0)) {
         GameCanvas.chatTabScr.AB(var0, "", T.TL, (byte)0, true);
      }

      Class_DS.AA(-1, (byte)2, var0, "", 0, 0);
      GameCanvas.chatTabScr.Show((MainScreen)GameCanvas.gameScr);
   }

   public static boolean getIsOffAdmin(byte var0) {
      if (IR) {
         return true;
      } else if (var0 == 0 && !IS) {
         return true;
      } else if (var0 == 1 && !IP) {
         return true;
      } else {
         return var0 == 2 && !IQ;
      }
   }

   private static boolean AV() {
      return GameCanvas.loadmap.idMapLoadMap == 100 || GameCanvas.loadmap.idMapLoadMap == 101;
   }

   public static short AL() {
      if (objFocus != null && objFocus.MX > 0) {
         return objFocus.ID;
      } else {
         for(int var0 = 0; var0 < vecPlayers.size(); ++var0) {
            MainObject var1;
            if (!(var1 = (MainObject)vecPlayers.elementAt(var0)).isRemove && var1.MX > 0 && MainObject.getDistance(player.x, player.y, var1.x, var1.y) <= 160) {
               return var1.ID;
            }
         }

         return -1;
      }
   }

   public static void addHightDataeff(short id, int x, int y) {
      DataSkillEff dse = new DataSkillEff(id, x, y);
      vecHighDataEff.addElement(dse);
   }

   public static ObjMove AB(short var0, byte var1) {
      for(int var2 = 0; var2 < vecObjMove.size(); ++var2) {
         ObjMove var3;
         if (!(var3 = (ObjMove)vecObjMove.elementAt(var2)).AE && var3.AB == var0 && var3.AA == var1) {
            return var3;
         }
      }

      return null;
   }

   public static MainObject AA(short var0) {
      for(int var1 = 0; var1 < vecPlayers.size(); ++var1) {
         MainObject var2;
         if ((var2 = (MainObject)vecPlayers.elementAt(var1)) != null && var2.ID == var0) {
            return var2;
         }
      }

      return null;
   }
}
