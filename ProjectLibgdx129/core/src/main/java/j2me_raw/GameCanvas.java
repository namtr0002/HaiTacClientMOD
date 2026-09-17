import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Calendar;
import javax.microedition.lcdui.Graphics;

public final class GameCanvas extends MotherCanvas {
   public static int AE = 0;
   public static byte AF = 0;
   public static byte language = 0;
   private static boolean DD = false;
   public static boolean lowGraphic = false;
   public static boolean AI = false;
   public static GameCanvas instance;
   public static boolean[] AK = new boolean[55];
   private static boolean[] DE = new boolean[55];
   public static boolean[] AL = new boolean[55];
   public static boolean AM = false;
   public static boolean isPointerDown = false;
   public static boolean AO = false;
   public static boolean isPointerSelect = false;
   public static boolean AQ = false;
   public static boolean AR = false;
   public static boolean AS = false;
   public static boolean isSmallScreen = false;
   public static boolean isOffBg = false;
   public static boolean AV = false;
   public static boolean isTaiTho = false;
   public static boolean isTouch;
   public static int AY;
   public static int AZ;
   public static int BA;
   public static int BB;
   public static long timeNow = 0L;
   public static long clockServer = 0L;
   public static int hCommand = 25;
   public static int hText = 15;
   public static int BG;
   public static int BH = 0;
   public static int gameTick;
   public static int BJ;
   public static int xPlus12;
   public static int yPlus12;
   public static int tickSelectChar = 0;
   private static int DF;
   private static mVector DG;
   public static byte BN = 0;
   public static byte BO = 1;
   private mGraphics g = new mGraphics();
   public static MainScreen currentScreen;
   public static MainDialog currentDialog;
   public static MainDialog subDialog;
   public static MainDialog BS;
   public static Class_AK BT;
   public static Class_AL BU;
   public static GameScreen gameScr;
   public static LoadMap loadmap;
   public static ReadMessenge readMessage = new ReadMessenge();
   public static LoginScreen loginScr;
   public static FristLoginScreen fristLoginScr;
   public static MapBackGround mapBack;
   public static MapBackGround mapLogin;
   public static SaveImageRMS saveImage = new SaveImageRMS();
   public static LoadMapScreen loadMapScr;
   public static Menu menuCur = new Menu();
   public static Menu menu = new Menu();
   public static ListChar_Screen CG;
   public static TabScreen tabAllScr;
   public static TabScreen CI;
   public static TabScreen CJ;
   public static ChatTabScreen chatTabScr;
   public static MainEvent eventScr;
   public static SaveRms saveRms;
   public static TabInventory tabInven;
   public static TabInventory CO;
   public static TabInventory CP;
   public static UpdateImageScreen CQ;
   public static Clan_Screen ClanScr;
   public static Sudo_Screen SudoScr;
   private static mVector DI = new mVector("GameCanvas.vecTest");
   private boolean DJ = false;
   public static String infoDisConnect = "";
   public static String[][] strListServer = new String[][]{{"Merry", "Sunny", "Red Force", "Oro Jackson"}, {"Global"}};
   public static String hostServer = "";
   public static int portServer;
   public static int IndexServer = 0;
   public static int CW = 0;
   public static int percentUPCV = 0;
   private static int DK = 0;
   public static int CY = 300;
   private static boolean isLoadImage = false;

   public static int DA = 0;
   private static boolean DM = false;
   public static byte DB = 0;
   public static byte DC = 0;

   public GameCanvas() {
      byte[] var2;
      ByteArrayInputStream var6;
      if ((var2 = CRes.loadRMS("Main_IPWORLD")) != null) {
         var6 = new ByteArrayInputStream(var2);
         DataInputStream dis = new DataInputStream(var6);

         try {
            language = dis.readByte();
            dis.close();
         } catch (Exception var5) {
            language = 0;
         }
      }

      if (language == 1) {
         T_E.loadT_E();
      } else {
         T.loadT();
      }

      instance = this;
      TField.setVendorTypeMode(0);
      isTouch = this.hasPointerEvents();
      byte var7 = 0;
      int var8;
      if ((var8 = Calendar.getInstance().get(11)) >= 18 || var8 < 6) {
         var7 = BO;
      }

      AF = var7;
      if (MotherCanvas.w < 200 || MotherCanvas.h < 200) {
         isSmallScreen = true;
         iCommand.wButtonCmd = 60;
         iCommand.hButtonCmdNor = 20;
         iCommand.hButtonCmdSpec = 46;
         Scroll.hRectScroll = 16;
      }

      BG = 36;
      if (isTouch) {
         BG = 40;
         DG = new mVector();
         iCommand.hButtonCmdNor = 30;
         iCommand.hButtonCmdSpec = 46;
         iCommand.wButtonCmd = 70;
      } else if (isSmallScreen) {
         BG = 30;
      }

      var2 = CRes.loadRMS("Main_Load_Image_Android_OK");
      String var9 = "";
      if (var2 != null) {
         var6 = new ByteArrayInputStream(var2);
         DataInputStream var10 = new DataInputStream(var6);

         try {
            var9 = var10.readUTF();
            var10.close();
         } catch (Exception var4) {
            var9 = "";
         }
      }

      var9.compareTo("1.2.9");
      this.AD();
   }

   public final void AD() {
      if (!this.DJ) {
         mFont.loadmFont();
      }

      CRes.loadSinCos();
      mSound.AA();
      Player.hotkeyPlayer = new Hotkey[2][];

      int var1;
      for(var1 = 0; var1 < Player.hotkeyPlayer.length; ++var1) {
         Player.hotkeyPlayer[var1] = new Hotkey[5];

         for(int var2 = 0; var2 < Player.hotkeyPlayer[var1].length; ++var2) {
            Player.hotkeyPlayer[var1][var2] = new Hotkey();
         }
      }

      if (language == 1) {
         for(var1 = 0; var1 < GameMidlet.AJ.length; ++var1) {
            GameMidlet.AJ[var1] = GameMidlet.AK[var1];
         }
      }

      byte[] var10;
      ByteArrayInputStream var11;
      DataInputStream var13;
      if ((var10 = CRes.loadRMS("SUB_TYPETOUCH")) != null) {
         var11 = new ByteArrayInputStream(var10);
         var13 = new DataInputStream(var11);

         try {
            Interface_Game.typeTouch = var13.readByte();
            var13.close();
         } catch (Exception var9) {
         }
      }

      byte[] var15;
      if ((var15 = CRes.loadRMS("SUB_LOWGRAPHIC")) != null) {
         var11 = new ByteArrayInputStream(var15);
         var13 = new DataInputStream(var11);

         try {
            lowGraphic = var13.readByte() != 0;
            var13.close();
         } catch (Exception var8) {
         }
      }

      if ((var15 = CRes.loadRMS("SUB_OFFBG")) != null) {
         var11 = new ByteArrayInputStream(var15);
         var13 = new DataInputStream(var11);

         try {
            isOffBg = var13.readByte() != 0;
            var13.close();
         } catch (Exception var7) {
         }
      }

      if ((var15 = CRes.loadRMS("Main_isQty")) != null) {
         var11 = new ByteArrayInputStream(var15);
         var13 = new DataInputStream(var11);

         try {
            TField.AF = var13.readByte() == 1;
            var13.close();
         } catch (Exception var6) {
         }
      }

      if ((var15 = CRes.loadRMS("MAIN_SOUND")) != null) {
         mSound.AD = var15[0] == 1;
         mSound.AE = var15[1] == 1;
      }

      DataInputStream var12;
      if ((var15 = CRes.loadRMS("SUB_TAITHO")) != null) {
         var11 = new ByteArrayInputStream(var15);
         var12 = new DataInputStream(var11);

         try {
            isTaiTho = var12.readByte() == 1;
            var12.close();
         } catch (Exception var5) {
         }
      }

      ByteArrayInputStream var14;
      if ((var15 = CRes.loadRMS("SUB_SSITEM")) != null) {
         var14 = new ByteArrayInputStream(var15);
         var12 = new DataInputStream(var14);

         try {
            DB = var12.readByte();
            var12.close();
         } catch (Exception var4) {
         }
      }

      if ((var10 = CRes.loadRMS("SUB_SPAM")) != null) {
         var14 = new ByteArrayInputStream(var10);
         var12 = new DataInputStream(var14);

         try {
            DC = var12.readByte();
            var12.close();
         } catch (Exception var3) {
            DC = 0;
         }
      }

      LoadImageStatic.LoadAllImage();
      MsgDialog.hPlus = 15;
      MainScreen.cameraMain = new Camera();
      MainScreen.cameraSub = new Camera();
      chatTabScr = new ChatTabScreen();
      gameScr = new GameScreen();
      loginScr = new LoginScreen();
      loadmap = new LoadMap();
      loadMapScr = new LoadMapScreen();
      if (MotherCanvas.h < 220 && isTouch) {
         AV = true;
      }

      (new MainTab()).createValue();
      tabAllScr = new TabScreen(MainTab.xTab, (byte)0);
      mVector mvecp = new mVector();
      (tabInven = new TabInventory(T.tabInven, Player.vecInventory, (byte)0, MainTab.xTab)).AQ();
      mvecp.addElement(tabInven);
      TabEquip tabEquip = new TabEquip(T.tabEquip);
      mvecp.addElement(tabEquip);
      TabInfo tabInfo = new TabInfo(T.tabInfo);
      mvecp.addElement(tabInfo);
      TabSkill tabSkill = new TabSkill(T.tabSkill);
      mvecp.addElement(tabSkill);
      TabQuest tabQuest = new TabQuest(T.tabQuest);
      mvecp.addElement(tabQuest);
      if (!isTouch) {
         TabMenu tabMenu = new TabMenu(T.setting, gameScr.AG());
         mvecp.addElement(tabMenu);
      }

      tabAllScr.addVecTab(mvecp);
      (saveRms = new SaveRms()).loadBeginGame();
      fristLoginScr = new FristLoginScreen();
      eventScr = new MainEvent((byte)-3, Player.vecEvent);
      fristLoginScr.Show();
      fristLoginScr.setBeginGame();
   }

   public final void paint(Graphics g) {
      try {
         this.g.g = g;
         if (isTaiTho) {
            this.g.setColor(0);
            this.g.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);
         }

         currentScreen.paint(this.g);
         resetTrans(this.g);
         if (GameScreen.h12plus > 0) {
            this.g.setColor(0);
            this.g.fillRect(0, 0, MotherCanvas.w, GameScreen.h12plus);
            if (AvMain.imgPlus12_2 == null) {
               AvMain.imgPlus12_2 = mImage.createImage("/interface/plus12_2.png");
            } else {
               this.g.drawRegion((mImage)AvMain.imgPlus12_2, 0, 0, 0);
            }

            if (mFont.tahoma_7_white != null) {
               mFont.tahoma_7_white.drawString(this.g, T.QL, xPlus12 + 13, 1, 0);
            }
         }

         if (subDialog != null) {
            subDialog.paint(this.g);
         }

         if (currentDialog != null) {
            currentDialog.paint(this.g);
         } else {
            if (menuCur.isShowMenu) {
               menuCur.AB(this.g);
            } else if (Class_IO.AB) {
               Class_IO.AA().paint(this.g);
            }

            Interface_Game.paintShowHelp(this.g, false);
         }

         if (!GameScreen.getIsOffAdmin((byte)2)) {
            resetTrans(this.g);
            Interface_Game.AF(this.g);
            if (GameScreen.player != null) {
               Interface_Game.AG(this.g);
            }
         }

         resetTrans(this.g);
         if (BH > 0) {
            this.g.setColor(0);
            this.g.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h);
         }

         if (GameScreen.IW) {
            for(int var4 = 0; var4 < DI.size(); ++var4) {
               String var2 = (String)DI.elementAt(var4);
               if (var4 < 20) {
                  mFont.tahoma_7b_black.drawString(this.g, var2, 0, MotherCanvas.h - hText - var4 * hText, 0);
                  mFont.tahoma_7b_white.drawString(this.g, var2, -1, MotherCanvas.h - hText - var4 * hText - 1, 0);
               } else {
                  mFont.tahoma_7b_black.drawString(this.g, var2, MotherCanvas.w, MotherCanvas.h - hText - (var4 - 20) * hText, 1);
                  mFont.tahoma_7b_white.drawString(this.g, var2, MotherCanvas.w - 1, MotherCanvas.h - hText - (var4 - 20) * hText - 1, 1);
               }
            }

            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public final void AB() {
      try {
         if (++gameTick > 12000) {
            gameTick = 0;
         }

         BJ = gameTick / 4;
         if (CW > 0) {
            --CW;
         }

         if (gameTick % 5 == 0) {
            timeNow = System.currentTimeMillis();
         }

         if (BH > 0) {
            BH -= MotherCanvas.h / 10;
         }

         Interface_Game.AL();
         Interface_Game.AM();
         if (GameScreen.isPvPNew) {
            Interface_Game.AL();
            Interface_Game.AM();
         }

         if (currentDialog != null) {
            currentDialog.update();
         } else {
            mGraphics var10000 = this.g;
            Interface_Game.AK();
            if (menuCur.isShowMenu) {
               menuCur.AH();
               menuCur.AF();
            } else if (subDialog != null) {
               subDialog.update();
            } else if (Class_IO.AB) {
               Class_IO.AA().handleKeyPress();
               Class_IO.AA().updatePointer();
            } else {
               currentScreen.handleKeyPress();
               currentScreen.updatePointer();
            }
         }

         currentScreen.update();
         AR = false;
         if (GameScreen.CJ > 0 && --GameScreen.CJ == 0) {
            GameScreen.CC = false;
         }

         isPointerSelect = false;
         if (tickSelectChar > 0) {
            --tickSelectChar;
         }

         if (percentUPCV != DK) {
            for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
               MainObject var2;
               int var3 = (var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).CN * 100 / (100 + DK);
               int var4 = var2.CO * 100 / (100 + DK);
               var2.setSpeed(var3, var4);
            }

            if (percentUPCV == 0) {
               CY = 0;
            } else {
               CY = 300;
            }

            DK = percentUPCV;
         }

      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public static void connectServerP() {
    isLoadImage = false;
    Session_ME.getInstance();
    Session_ME.setHandler((IMessageHandler) GlobalMessageHandler.getInstance());

    // đảm bảo đã load danh sách server
    if (UpdateServer.serverHosts.isEmpty()) {
        UpdateServer.loadServers();
    }

    int idx = GameCanvas.IndexServer;
    if (idx < 0) idx = 0;
    if (idx >= UpdateServer.serverHosts.size()) idx = UpdateServer.serverHosts.size() - 1;

    // Lấy host hiện tại
String host = UpdateServer.getHost(idx);
int port = UpdateServer.getPort(idx);
GameCanvas.hostServer = host;
GameCanvas.portServer = port;

System.out.println("[CONNECT] initial IndexServer= " + idx + " -> " + host + ":" + port);

// Chỉ tự động chuyển nếu server hiện tại là localhost VÀ index hiện tại là 0 (mặc định)
// Giả sử: server index 0 là "SunyTeam" (public), còn các server localhost là để dev
String hostLower = host.toLowerCase();
if ((hostLower.equals("127.0.0.1") || hostLower.equals("localhost"))
    && UpdateServer.serverHosts.size() > 1
    && GameCanvas.IndexServer == 0) { // Chỉ đổi nếu đang ở index 0 (vì có thể là default)
    
    System.out.println("[CONNECT] Detected localhost at index 0, trying to switch to public server...");

    for (int i = 0; i < UpdateServer.serverHosts.size(); i++) {
        String candidateHost = UpdateServer.getHost(i).toLowerCase();
        if (!candidateHost.equals("127.0.0.1") && !candidateHost.equals("localhost")) {
            idx = i;
            host = UpdateServer.getHost(i);
            port = UpdateServer.getPort(i);
            GameCanvas.IndexServer = idx;
            GameCanvas.hostServer = host;
            GameCanvas.portServer = port;
            System.out.println("[CONNECT] Auto-switched to non-local host Index= " + idx + " -> " + host + ":" + port);
            break;
        }
    }
}

// Nếu người dùng chủ động chọn localhost → giữ nguyên lựa chọn
// Không can thiệp

    System.out.println("[CONNECT] final connect -> " + host + ":" + port + " (IndexServer=" + GameCanvas.IndexServer + ")");
    Session_ME.getInstance().connectServer(host, port);
    infoDisConnect = "";
}

    public static void connectDownload() {
    isLoadImage = true;
    Session_ME.getInstance();
    Session_ME.setHandler((IMessageHandler) GlobalMessageHandler.getInstance());

    if (UpdateServer.serverHosts.isEmpty()) {
        UpdateServer.loadServers();
    }
    int idx = GameCanvas.IndexServer;
    if (idx < 0) idx = 0;
    if (idx >= UpdateServer.serverHosts.size()) idx = UpdateServer.serverHosts.size() - 1;

    String host = UpdateServer.getHost(idx);
    int port = 2239; // default download port
    try {
        int candidatePort = UpdateServer.getPort(idx);
        if (candidatePort > 0) {
            port = candidatePort;
        }
    } catch (Exception ex) {}

    GameCanvas.hostServer = host;
    GameCanvas.portServer = port;

    System.out.println("[CONNECT-DOWNLOAD] -> " + host + ":" + port);
    Session_ME.getInstance().connectServer(host, port);
}


   public static void resetTrans(mGraphics var0) {
      var0.translate(-var0.getTranslateX(), -var0.getTranslateY());
      var0.AD(0, 0, MotherCanvas.w, MotherCanvas.h);
   }

   public final void keyPressed(int var1) {
      CW = 4500;
      Player.QT = false;
      MsgDialog.isAuroReconect = false;
      if (Player.SO == 5) {
         Player.SO = 4;
      }

      if (TField.AF && (var1 >= 48 && var1 <= 57 || var1 >= 65 && var1 <= 122 || var1 == 10 || var1 == 8 || var1 == 13 || var1 == 32)) {
         DF = var1;
      }

      if (currentDialog != null) {
         currentDialog.AA(var1);
      } else if (subDialog != null) {
         subDialog.AA(var1);
      } else if (Class_IO.AB) {
         Class_IO.AA().AA(var1);
      } else {
         currentScreen.AA(var1);
      }

      if (!GameScreen.CQ && isTouch) {
         GameScreen.CQ = true;
      }

      boolean var10000;
      label116: {
         switch(var1) {
         case -22:
            AL[41] = true;
            AK[41] = true;
            var10000 = true;
            break label116;
         case -21:
            AL[40] = true;
            AK[40] = true;
            break;
         case 97:
            AL[34] = true;
            AK[34] = true;
            var10000 = true;
            break label116;
         case 98:
            AL[51] = true;
            AK[51] = true;
            var10000 = true;
            break label116;
         case 99:
            AL[48] = true;
            AK[48] = true;
            var10000 = true;
            break label116;
         case 100:
            AL[36] = true;
            AK[36] = true;
            var10000 = true;
            break label116;
         case 101:
            AL[43] = true;
            AK[43] = true;
            var10000 = true;
            break label116;
         case 103:
            AL[31] = true;
            AK[31] = true;
            var10000 = true;
            break label116;
         case 104:
            AL[33] = true;
            AK[33] = true;
            var10000 = true;
            break label116;
         case 105:
            AL[46] = true;
            AK[46] = true;
            var10000 = true;
            break label116;
         case 106:
            AL[35] = true;
            AK[35] = true;
            var10000 = true;
            break label116;
         case 107:
            AL[37] = true;
            AK[37] = true;
            var10000 = true;
            break label116;
         case 108:
            AL[39] = true;
            AK[39] = true;
            break;
         case 109:
            AL[42] = true;
            AK[42] = true;
            var10000 = true;
            break label116;
         case 111:
            AL[44] = true;
            AK[44] = true;
            var10000 = true;
            break label116;
         case 112:
            AL[50] = true;
            AK[50] = true;
            var10000 = true;
            break label116;
         case 113:
            AL[47] = true;
            AK[47] = true;
            var10000 = true;
            break label116;
         case 115:
            AL[38] = true;
            AK[38] = true;
            var10000 = true;
            break label116;
         case 119:
            AL[32] = true;
            AK[32] = true;
            var10000 = true;
            break label116;
         case 120:
            AL[49] = true;
            AK[49] = true;
            var10000 = true;
            break label116;
         case 121:
            AL[45] = true;
            AK[45] = true;
            var10000 = true;
            break label116;
         }

         var10000 = false;
      }

      if (!var10000) {
         if (TField.AF && !Class_IO.AB) {
            if (DF != 114 && DF != 82) {
               if (DF != 116 && DF != 84) {
                  if (DF != 121 && DF != 89) {
                     if (DF != 117 && DF != 85) {
                        if (DF == 105 || DF == 73) {
                           AL[29] = true;
                           AK[29] = true;
                        }
                     } else {
                        AL[27] = true;
                        AK[27] = true;
                     }
                  } else {
                     AL[25] = true;
                     AK[25] = true;
                  }
               } else {
                  AL[23] = true;
                  AK[23] = true;
               }
            } else {
               AL[21] = true;
               AK[21] = true;
            }
         }

         switch(var1) {
         case -7:
            AL[13] = true;
            AK[13] = true;
            break;
         case -6:
            AL[12] = true;
            AK[12] = true;
            return;
         case -5:
         case 10:
            AL[5] = true;
            AK[5] = true;
            return;
         case -4:
            AL[6] = true;
            AK[6] = true;
            return;
         case -3:
            AL[4] = true;
            AK[4] = true;
            return;
         case -2:
            AL[8] = true;
            AK[8] = true;
            return;
         case -1:
            AL[2] = true;
            AK[2] = true;
            return;
         case 35:
            AL[11] = true;
            AK[11] = true;
            return;
         case 42:
            AL[10] = true;
            AK[10] = true;
            return;
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
            AL[var1 - 28] = true;
            AK[var1 - 28] = true;
            return;
         }
      }

   }

   public final void keyReleased(int var1) {
      if (TField.AF) {
         DF = 0;
      }

      label47: {
         boolean var10000;
         switch(var1) {
         case -22:
            AL[41] = false;
            AK[41] = false;
            var10000 = true;
            break label47;
         case -21:
            AL[40] = false;
            AK[40] = false;
            break;
         case 97:
            AL[34] = false;
            AK[34] = false;
            var10000 = true;
            break label47;
         case 98:
            AL[51] = false;
            AK[51] = false;
            var10000 = true;
            break label47;
         case 99:
            AL[48] = false;
            AK[48] = false;
            var10000 = true;
            break label47;
         case 100:
            AL[36] = false;
            AK[36] = false;
            var10000 = true;
            break label47;
         case 101:
            AL[43] = false;
            AK[43] = false;
            var10000 = true;
            break label47;
         case 103:
            AL[31] = false;
            AK[31] = false;
            var10000 = true;
            break label47;
         case 104:
            AL[33] = false;
            AK[33] = false;
            var10000 = true;
            break label47;
         case 105:
            AL[46] = false;
            AK[46] = false;
            var10000 = true;
            break label47;
         case 106:
            AL[35] = false;
            AK[35] = false;
            var10000 = true;
            break label47;
         case 107:
            AL[37] = false;
            AK[37] = false;
            var10000 = true;
            break label47;
         case 108:
            AL[39] = false;
            AK[39] = false;
            break;
         case 109:
            AL[42] = false;
            AK[42] = false;
            var10000 = true;
            break label47;
         case 111:
            AL[44] = false;
            AK[44] = false;
            var10000 = true;
            break label47;
         case 112:
            AL[50] = false;
            AK[50] = false;
            var10000 = true;
            break label47;
         case 113:
            AL[47] = false;
            AK[47] = false;
            var10000 = true;
            break label47;
         case 115:
            AL[38] = false;
            AK[38] = false;
            var10000 = true;
            break label47;
         case 119:
            AL[32] = false;
            AK[32] = false;
            var10000 = true;
            break label47;
         case 120:
            AL[49] = false;
            AK[49] = false;
            var10000 = true;
            break label47;
         case 121:
            AL[45] = false;
            AK[45] = false;
            var10000 = true;
            break label47;
         }

         var10000 = false;
      }

      switch(var1) {
      case -7:
         AL[13] = false;
      default:
         return;
      case -6:
         AL[12] = false;
         return;
      case -5:
      case 10:
         AL[5] = false;
         AK[5] = false;
         return;
      case -4:
         AL[6] = false;
         AK[6] = false;
         return;
      case -3:
         AL[4] = false;
         AK[4] = false;
         return;
      case -2:
         AL[8] = false;
         AK[8] = false;
         return;
      case -1:
         AL[2] = false;
         AK[2] = false;
         return;
      case 35:
         AL[11] = false;
         return;
      case 42:
         AL[10] = false;
         return;
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
         AL[var1 - 28] = false;
      }
   }

   public final void AA(int var1, int var2) {
      isPointerSelect = false;
      AY = var1;
      AZ = var2;
      if (AQ) {
         DG.addElement(new Class_DB(var1, var2));
      } else {
         if (CRes.abs(AY - BA) >= 15 || CRes.abs(AZ - BB) >= 15) {
            AQ = true;
         }

      }
   }

   public final void AB(int var1, int var2) {
      Player.QT = false;
      MsgDialog.isAuroReconect = false;
      if (Player.SO == 5) {
         Player.SO = 4;
      }

      CW = 4500;
      isPointerDown = true;
      AQ = false;
      isPointerSelect = false;
      AO = false;
      AS = false;
      BA = var1;
      BB = var2;
      AY = var1;
      AZ = var2;
   }

   public final void AC(int var1, int var2) {
      if (!AQ && !AS) {
         isPointerSelect = true;
      }

      AH();
      AG();
      isPointerDown = false;
      AO = true;
      AQ = false;
      AR = true;
      AS = false;
      AY = var1;
      AZ = var2;
   }

   public static void AG() {
      AO = false;
      isPointerDown = false;

      for(int var0 = 0; var0 < AK.length; ++var0) {
         AK[var0] = false;
      }

   }

   public static void AA(int var0) {
      AO = false;
      isPointerDown = false;
      AK[var0] = false;
   }

   public static void AH() {
      AO = false;
      isPointerDown = false;

      for(int var0 = 0; var0 < AL.length; ++var0) {
         AL[var0] = false;
      }

   }

   public static void AB(int var0) {
      AO = false;
      isPointerDown = false;
      AL[var0] = false;
   }

   public static void clearAll() {
      AH();
      AG();
      isPointerDown = false;
      AO = false;

      for(int var0 = 0; var0 < DE.length; ++var0) {
         DE[var0] = false;
      }

      isPointerSelect = false;
      AQ = false;
      AR = false;
      isPointerDown = false;
   }

   public static boolean isPointer(int var0, int var1, int var2, int var3) {
      return !isPointerDown && !AO ? false : isPoint(var0, var1, var2, var3);
   }

   public static boolean AB(int var0, int var1, int var2, int var3) {
      return !isPointerSelect ? false : isPoint(var0, var1, var2, var3);
   }

   public static boolean isPoint(int var0, int var1, int var2, int var3) {
      return AY >= var0 && AY <= var0 + var2 && AZ >= var1 && AZ <= var1 + var3;
   }

   public static boolean AD(int var0, int var1, int var2, int var3) {
      return BA >= var0 && BA <= var0 + var2 && BB >= var1 && BB <= var1 + var3;
   }

   public static void Start_Normal_DiaLog(String var0, mVector var1, boolean var2) {
      MsgDialog var3;
      (var3 = new MsgDialog()).AA(var0, var1, var2, (byte)0);
      currentDialog = var3;
   }

   public static void AB(String var0, mVector var1, boolean var2) {
      MsgDialog var3;
      (var3 = new MsgDialog()).AA(var0, var1, false, (byte)9);
      currentDialog = var3;
   }

   public static void Start_Normal_DiaLog_New(String var0, mVector var1, boolean var2, String var3) {
      MsgDialog var4;
      (var4 = new MsgDialog()).AA(var0, var1, true, var3);
      currentDialog = var4;
   }

   public static void Start_Normal_DiaLog(String var0, iCommand var1, boolean var2) {
      mVector var3;
      (var3 = new mVector()).addElement(var1);
        GameCanvas.Start_Normal_DiaLog(var0, var3, var2);
   }

   public static void Start_Normal_Only_CmdClose_DiaLog(String var0) {
      mVector var1 = new mVector();
        GameCanvas.Start_Normal_DiaLog(var0, var1, true);
   }

   public static void AA(String var0, boolean var1) {
      MsgDialog var2;
      (var2 = new MsgDialog()).AA(var0, (mVector)null, var1, (byte)1, 0);
      currentDialog = var2;
   }

   public static void AB(String var0, boolean var1) {
      MsgDialog var2;
      (var2 = new MsgDialog()).AA(var0, (mVector)null, var1, (byte)8, 0);
      currentDialog = var2;
   }

   public static void AA(String var0, boolean var1, int var2, mVector var3) {
      MsgDialog var4;
      (var4 = new MsgDialog()).AA(var0, var3, var1, (byte)6, var2);
      currentDialog = var4;
   }

   public static InputDialog Start_Input_Dialog(String var0, iCommand var1, boolean var2, String var3) {
      InputDialog var4;
      (var4 = new InputDialog()).AA(var0, var1, true, var3);
      return var4;
   }

   public static InputDialog AA(String[] var0, String var1, short var2) {
      InputDialog var3;
      (var3 = new InputDialog()).AA(var0, var1, var2, (iCommand)null, "");
      return var3;
   }

   public static InputDialog AA(String[] var0, String var1, iCommand var2) {
      InputDialog var3;
      (var3 = new InputDialog()).AA(var0, var1, (short)0, var2, "");
      return var3;
   }

   public static MsgArchiDaily AA(String var0, mVector var1) {
      MsgArchiDaily var2;
      (var2 = new MsgArchiDaily()).AA(var0, var1);
      return var2;
   }

   public static MsgTableMatch AA(mVector var0) {
      MsgTableMatch var1;
      (var1 = new MsgTableMatch()).AB(var0);
      return var1;
   }

   public static void AA(MainDialog var0) {
      currentDialog = var0;
   }

   public static void AB(MainDialog var0) {
      subDialog = var0;
   }

   public static void AA(Menu var0) {
      menuCur = var0;
      float var10000 = mSound.volumeSound;
      mSound.playSound();
   }

   public static void end_Dialog() {
      currentDialog = null;
      subDialog = null;
      AH();
      AG();
      if (currentScreen != null) {
         currentScreen.AM();
      }

   }

   public static void AK() {
      currentDialog = null;
      AH();
      AG();
      if (currentScreen != null) {
         currentScreen.AM();
      }

   }

   public static boolean AL() {
      return !menuCur.isShowMenu && currentDialog == null && subDialog == null;
   }

   public static boolean isKeyPressed(int var0) {
      switch(var0) {
      case 0:
         if (!AL[4] && !AL[24] && !AL[34]) {
            break;
         }

         return true;
      case 1:
         if (!AL[2] && !AL[22] && !AL[32]) {
            break;
         }

         return true;
      case 2:
         if (AL[6] || AL[26] || AL[36]) {
            return true;
         }
         break;
      case 3:
         if (AL[8] || AL[28] || AL[38]) {
            return true;
         }
      }

      return false;
   }

   public static void clearKeyPressed(int var0) {
      switch(var0) {
      case 0:
         AL[4] = false;
         AL[24] = false;
         AL[34] = false;
         return;
      case 1:
         AL[2] = false;
         AL[22] = false;
         AL[32] = false;
         return;
      case 2:
         AL[6] = false;
         AL[26] = false;
         AL[36] = false;
      default:
         return;
      case 3:
         AL[8] = false;
         AL[28] = false;
         AL[38] = false;
      }
   }

   public static boolean AE(int var0) {
      switch(var0) {
      case 0:
         if (AG(12) || AG(40)) {
            return true;
         }
         break;
      case 1:
         if (AG(21) || AG(31)) {
            return true;
         }
         break;
      case 2:
         if (AG(13) || AG(41)) {
            return true;
         }
         break;
      case 3:
         if (AG(23) || AG(33)) {
            return true;
         }
         break;
      case 4:
         if (AG(42)) {
            return true;
         }
         break;
      case 5:
         if (AG(5) || AG(25) || AG(35)) {
            return true;
         }
         break;
      case 6:
         if (AG(20) || AG(43)) {
            return true;
         }
         break;
      case 7:
         if (AG(27) || AG(37)) {
            return true;
         }
         break;
      case 8:
         if (AG(11) || AG(44)) {
            return true;
         }
         break;
      case 9:
         if (AG(29) || AG(39)) {
            return true;
         }
         break;
      case 10:
         if (AG(10) || AG(45)) {
            return true;
         }
         break;
      case 11:
         if (AG(46)) {
            return true;
         }
         break;
      case 12:
         if (AG(47)) {
            return true;
         }
         break;
      case 13:
         if (AG(48)) {
            return true;
         }
         break;
      case 14:
         if (AG(49)) {
            return true;
         }
         break;
      case 15:
         if (AG(50)) {
            return true;
         }
         break;
      case 16:
         if (AG(51)) {
            return true;
         }
      }

      return false;
   }

   public static void AF(int var0) {
      switch(var0) {
      case 0:
         AB(12);
         AB(40);
         return;
      case 1:
         AB(21);
         AB(31);
         return;
      case 2:
         AB(13);
         AB(41);
         return;
      case 3:
         AB(23);
         AB(33);
         return;
      case 4:
         AB(42);
         return;
      case 5:
         AB(5);
         AB(25);
         AB(35);
         return;
      case 6:
         AB(20);
         AB(43);
         return;
      case 7:
         AB(27);
         AB(37);
         return;
      case 8:
         AB(11);
         AB(44);
         return;
      case 9:
         AB(29);
         AB(39);
         return;
      case 10:
         AB(10);
         AB(45);
         AA(10);
         AA(45);
         return;
      case 11:
         AB(46);
         return;
      case 12:
         AB(47);
         return;
      case 13:
         AB(48);
         return;
      case 14:
         AB(49);
         return;
      case 15:
         AB(50);
         return;
      case 16:
         AB(51);
      default:
      }
   }

   public static boolean AG(int var0) {
      for(int var1 = 0; var1 < AL.length; ++var1) {
         if (var1 == var0 && (AL[var1] || AK[var1])) {
            return true;
         }
      }

      return false;
   }

   public static boolean AM() {
      for(int var0 = 0; var0 < AL.length; ++var0) {
         if (AL[var0] || AK[var0]) {
            return true;
         }
      }

      return false;
   }

   public static boolean isTouchAndKey() {
      return TField.AF || GameScreen.CQ;
   }

   public static boolean isKeyPressed() {
      return !isTouch || isTouchAndKey();
   }

   public static boolean isIos() {
      return false;
   }

   public static boolean AQ() {
      return false;
   }

   public static boolean AR() {
      return currentDialog != null || subDialog != null || menuCur.isShowMenu;
   }

   public static void AS() {
      if (!isLoadImage) {
         String var0 = T.AJ;
         if (infoDisConnect != null && infoDisConnect.length() > 10) {
            var0 = infoDisConnect;
            infoDisConnect = "";
         }

         boolean var1 = false;
         mVector var2 = new mVector();
         if (currentScreen != loginScr && currentScreen != loadMapScr) {
            var2.addElement(GameScreen.CA);
            var1 = true;
         }

         var2.addElement(gameScr.BI);
         if (var1) {
            AB(var0, var2, false);
            return;
         }

            GameCanvas.Start_Normal_DiaLog(var0, var2, false);
      }

   }

   public static String AT() {
      long var0 = System.currentTimeMillis();
      return (var0 / 1000L / 60L / 60L + 7L) % 24L + "h " + var0 / 1000L / 60L % 60L + "'";
   }

   public static boolean isLowGraOrWP_PvP() {
      return lowGraphic;
   }
}
