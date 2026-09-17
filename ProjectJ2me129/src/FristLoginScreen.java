import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class FristLoginScreen extends MainScreen {
   public iCommand cmdBegin;
   private iCommand cmdChangeAcc;
   private iCommand cmdServer;
   private iCommand cmdNewGame;
   private mVector vecCmd = new mVector();
   private static String AH = "";
   public static InputDialog AB;
   public static iCommand AC;
   private int AI = 0;
   private int idCommand = 0;

   private InputDialog input;

   public FristLoginScreen() {
        CRes.saveRMS("MAIN_frist_login", null);
        CRes.saveRMS("MAIN_user_last", null);
        CRes.saveRMS("MAIN_user_pass", null);

       UpdateServer.loadServers();
       // đảm bảo IndexServer hợp lệ sau khi load server mới
        if (UpdateServer.serverHosts.size() > 0) {
            if (GameCanvas.IndexServer < 0 || GameCanvas.IndexServer >= UpdateServer.serverHosts.size()) {
                GameCanvas.IndexServer = 0;
            }
        }
      this.cmdBegin = new iCommand(T.loadGame, 0, 0, this);
      SaveRms.userLast = "";
      if (CRes.loadRMS("MAIN_user_last") != null) {
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AA();
      } else {
         // safe guard: dự phòng nếu strListServer chưa có ngôn ngữ
         if (GameCanvas.strListServer != null
             && GameCanvas.language >= 0
             && GameCanvas.language < GameCanvas.strListServer.length
             && GameCanvas.strListServer[GameCanvas.language] != null
             && GameCanvas.strListServer[GameCanvas.language].length > 0) {
             GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].length - 1;
         } else {
             GameCanvas.IndexServer = 0;
         }
      }

      if (GameCanvas.strListServer != null
          && GameCanvas.language >= 0
          && GameCanvas.language < GameCanvas.strListServer.length
          && GameCanvas.strListServer[GameCanvas.language] != null
          && GameCanvas.IndexServer >= GameCanvas.strListServer[GameCanvas.language].length) {
         GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].length - 1;
      }

      AC = new iCommand(T.CX, 3, this);
      this.cmdServer = new iCommand(T.server + "\n" + UpdateServer.getCurrentServerName(), 4, this);

      // ==== tính AI an toàn, dynamic theo số server và spacingYDefault ====
      int nServers = 0;
      if (GameCanvas.strListServer != null
          && GameCanvas.language >= 0
          && GameCanvas.language < GameCanvas.strListServer.length
          && GameCanvas.strListServer[GameCanvas.language] != null) {
          nServers = GameCanvas.strListServer[GameCanvas.language].length;
      }
      int colsDefault = 2; // mặc định ước lượng cột
      int rowsNeeded = (nServers + colsDefault - 1) / colsDefault;
      int spacingYDefault = 36; // chiều cao mỗi ô (gần nhau)
      this.AI = Math.max(0, (rowsNeeded - 1) * spacingYDefault);
      // ==== kết thúc tính AI ====

      this.getVecBegin();
      if (LoginScreen.yPaintLogo == 0) {
         LoginScreen.yPaintLogo = LoginScreen.hLogo;
      }

   }

   public final void Show() {
      if (GameScreen.CU != null) {
         GameScreen.CU.removeAllElements();
      }

      if (GameCanvas.mapBack == null) {
         GameCanvas.mapBack = new MapBackGround();
      }

      GameCanvas.mapBack.AC();
      GameScreen.player = null;
      Session_ME.getInstance().close();
      this.idCommand = 0;
      if (!GameCanvas.isTouch || GameCanvas.isTouchAndKey()) {
         for(int var1 = 0; var1 < this.vecCmd.size(); ++var1) {
            iCommand var2 = (iCommand)this.vecCmd.elementAt(var1);
            if (var1 == this.idCommand) {
               var2.AG = true;
            } else {
               var2.AG = false;
            }
         }
      }

      if (GameCanvas.currentScreen != null && GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.fristLoginScr) {
         LoginScreen.AF();
      }

      LoginScreen.AG();
      super.Show();
      float var10000 = mSound.AB;
      mSound.AC();
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void setBeginGame() {
      if (KeyAuthManager.isAuthorized && CRes.loadRMS("MAIN_user_pass") != null) {
         GameCanvas.loginScr.Show();
         GameCanvas.loginScr.AA(true, (byte)0, GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
      } else {
         AA(false);
         this.getVecBegin();
      }
   }

   public static void AA(boolean var0) {
      byte[] data;
      if ((data = CRes.loadRMS("MAIN_frist_login")) != null || var0) {
         ListChar_Screen.IndexCharSelected = -1;
         AH = "";
         try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream dis;
            //AH = (var4 = new DataInputStream(var3)).readUTF();
            dis = new DataInputStream(bais);
            AH = "";
            if (dis.available() > 0) {
               GameCanvas.IndexServer = dis.readByte();
            }
         } catch (Exception var2) {
            AH = "";
         }

         GameCanvas.loginScr.AA(true, (byte)1, AH, "");
      }

   }

   public void openSelectServerMenu() {
      mVector vec = new mVector();
      int lang = GameCanvas.language;
      int currentRelIdx = -1;
      int relCount = 0;
      for (int i = 0; i < UpdateServer.serverHosts.size(); i++) {
         if (UpdateServer.serverLang.size() > i && UpdateServer.serverLang.get(i) == lang) {
            String sName = UpdateServer.getServerName(i);
            iCommand cmd = new iCommand(sName, 5, i, this);
            cmd.isNew = UpdateServer.isServerNew(i);
            vec.addElement(cmd);
            if (i == GameCanvas.IndexServer) {
               currentRelIdx = relCount;
            }
            relCount++;
         }
      }
      if (KeyAuthManager.isAuthorized) {
         iCommand cmdAdd = new iCommand("+ Thêm IP Server", 25, this);
         vec.addElement(cmdAdd);
      }
      iCommand cmdKey = new iCommand("🔑 Nhập Key Bản Quyền", 23, this);
      vec.addElement(cmdKey);

      GameCanvas.menu.startAt(vec, 2, T.server);
      if (currentRelIdx >= 0) {
         GameCanvas.menu.AC = currentRelIdx;
         GameCanvas.menu.menuSelectedItem = currentRelIdx;
      }
   }

   public final void commandPointer(int var1, int var2) {
      int var4;
      label49:
      switch(var1) {
      case 0:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         ListChar_Screen.IndexCharSelected = -1;
         AH = "";
         byte[] var7;
         if ((var7 = CRes.loadRMS("MAIN_frist_login")) != null) {
            try {
               ByteArrayInputStream var8 = new ByteArrayInputStream(var7);
               DataInputStream var9;
               AH = (var9 = new DataInputStream(var8)).readUTF();
               if (var9.available() > 0) {
                  GameCanvas.IndexServer = var9.readByte();
               }
            } catch (Exception var5) {
               AH = "";
            }
         }

         GameCanvas.loginScr.AA(true, (byte)1, AH, "");
         break;
      case 1:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         GameCanvas.loginScr.Show();
         break;
      case 2:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         GameCanvas.loginScr.AA(true, (byte)1, "", "");
         break;
      case 3:
         String[] var6 = new String[AB.AB.length];

         for(var4 = 0; var4 < var6.length; ++var4) {
            if (AB.AB[var4].getText().length() > 0) {
               var6[var4] = AB.AB[var4].getText();
            } else {
               var6[var4] = "";
            }
         }

         GlobalService.getInstance().AA(var6);
         break;

      case 4:
         openSelectServerMenu();
         break;

      case 5:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         GameCanvas.IndexServer = var2;
         GameCanvas.hostServer = UpdateServer.getHost(var2);
         GameCanvas.portServer = UpdateServer.getPort(var2);
         if (Session_ME.getInstance().AB()) {
            Session_ME.getInstance().close();
         }
         GameCanvas.menu.doCloseMenu();
         this.cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
         this.getVecBegin();
         break;

      case 23:
         this.input = new InputDialog();
         this.input.setinfo("Nhập Key Bản Quyền:", new iCommand("Check Key", 24, this), false, "MÃ KEY");
         this.input.tfInput.AB(KeyAuthManager.getActiveKey());
         GameCanvas.Start_Current_Dialog(this.input);
         break;

      case 24:
         if (this.input != null) {
            String textKey = this.input.getText().trim();
            GameCanvas.end_Dialog();
            KeyAuthManager.checkAndActivateKey(textKey);
         }
         break;

      case 25:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         this.input = new InputDialog();
         this.input.setinfo("Nhập Tên:IP:Port hoặc IP:Port\nVD: SV1:127.0.0.1:2239", new iCommand(T.AI, 26, this), false, "IP-PORT");
         GameCanvas.Start_Current_Dialog(this.input);
         break;

      case 26:
         if (this.input != null) {
            String str = this.input.getText().trim();
            GameCanvas.end_Dialog();
            if (str.length() > 0) {
               boolean ok = UpdateServer.parseAndAddCustomServer(str);
               if (ok) {
                  this.cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
                  this.getVecBegin();
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Đã thêm máy chủ thành công!\n" + UpdateServer.getCurrentServerName());
               } else {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Định dạng không hợp lệ!\nVí dụ: SV1:127.0.0.1:2239");
               }
            }
         }
         break;
      }

      super.commandPointer(var1, var2);
   }

   private void getVecBegin() {
      this.vecCmd.removeAllElements();
      if (GameCanvas.language > GameCanvas.strListServer.length - 1) {
         GameCanvas.language = 0;
      }

      if (GameCanvas.IndexServer > GameCanvas.strListServer[GameCanvas.language].length - 1) {
         GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].length - 1;
      }

      if (SaveRms.userLast.length() > 0) {
         this.cmdBegin.caption = T.loadGame + "\n " + SaveRms.userLast;
         this.cmdBegin.setPos(MotherCanvas.hw - 38, MotherCanvas.h - 98, (FrameImage)null, this.cmdBegin.caption);
         this.cmdBegin.setTypeSpec();
         this.vecCmd.addElement(this.cmdBegin);
         this.cmdNewGame = new iCommand(T.newGame, 2, 0, this);
         this.cmdNewGame.setPos(MotherCanvas.hw + 38, MotherCanvas.h - 98, (FrameImage)null, this.cmdNewGame.caption);
         this.cmdNewGame.setTypeSpec();
         this.vecCmd.addElement(this.cmdNewGame);
         this.cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
         this.cmdChangeAcc.setPos(MotherCanvas.hw - 38, MotherCanvas.h - 46, (FrameImage)null, this.cmdChangeAcc.caption);
         this.cmdChangeAcc.setTypeSpec();
         this.vecCmd.addElement(this.cmdChangeAcc);
         this.cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
         this.cmdServer.setPos(MotherCanvas.hw + 38, MotherCanvas.h - 46, (FrameImage)null, this.cmdServer.caption);
         this.cmdServer.setTypeSpec();
         this.vecCmd.addElement(this.cmdServer);
      } else {
         this.cmdNewGame = new iCommand(T.newGame, 2, 0, this);
         this.cmdNewGame.setPos(MotherCanvas.hw - 76, MotherCanvas.h - 60, (FrameImage)null, this.cmdNewGame.caption);
         this.cmdNewGame.setTypeSpec();
         this.vecCmd.addElement(this.cmdNewGame);
         this.cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
         this.cmdChangeAcc.setPos(MotherCanvas.hw, MotherCanvas.h - 60, (FrameImage)null, this.cmdChangeAcc.caption);
         this.cmdChangeAcc.setTypeSpec();
         this.vecCmd.addElement(this.cmdChangeAcc);
         this.cmdServer.caption = T.server + "\n" + UpdateServer.getCurrentServerName();
         this.cmdServer.setPos(MotherCanvas.hw + 76, MotherCanvas.h - 60, (FrameImage)null, this.cmdServer.caption);
         this.cmdServer.setTypeSpec();
         this.vecCmd.addElement(this.cmdServer);
      }

      this.idCommand = 0;
      if (!GameCanvas.isTouch || GameCanvas.isTouchAndKey()) {
         for(int var1 = 0; var1 < this.vecCmd.size(); ++var1) {
            iCommand var2 = (iCommand)this.vecCmd.elementAt(var1);
            if (var1 == this.idCommand) {
               var2.AG = true;
            } else {
               var2.AG = false;
            }
         }
      }

   }

   public final void paint(mGraphics var1) {
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AC(var1);
         GameCanvas.mapBack.AE(var1);
         GameCanvas.mapBack.AD(var1);
      }

      LoginScreen.AB(var1);
      LoginScreen.paintLogo(var1, MotherCanvas.hw);
      mFont.tahoma_7_black.drawString(var1, "Ver: " + ClientConfig.CLIENT_VERSION, MotherCanvas.w - 2, 2 + GameScreen.h12plus, 1);
      mFont.tahoma_7_black.drawString(var1, "ID: " + ClientConfig.CLIENT_ID, MotherCanvas.w - 2, 4 + GameScreen.h12plus + GameCanvas.hText / 2, 1);
      GameCanvas.resetTrans(var1);

      for(int var2 = 0; var2 < this.vecCmd.size(); ++var2) {
         iCommand var3;
         (var3 = (iCommand)this.vecCmd.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
      }

      super.paint(var1);
   }

   public final void update() {
      LoginScreen.updateYPaintLogo(LoginScreen.hLogo);
      LoginScreen.updateCharShow();
   }

   public final void handleKeyPress() {
      int var1 = this.vecCmd.size();
      if ((!GameCanvas.isTouch || GameCanvas.isTouchAndKey()) && var1 > 0) {
         int var2 = this.idCommand;
         if (GameCanvas.isKeyPressed(0)) {
            --this.idCommand;
            GameCanvas.clearKeyPressed(0);
         } else if (GameCanvas.isKeyPressed(2)) {
            ++this.idCommand;
            GameCanvas.AB(6);
            GameCanvas.clearKeyPressed(2);
         }

         this.idCommand = AvMain.AA(this.idCommand, var1 - 1, false);
         if (var2 != this.idCommand && (!GameCanvas.isTouch || GameCanvas.isTouchAndKey())) {
            for(var2 = 0; var2 < var1; ++var2) {
               iCommand var3 = (iCommand)this.vecCmd.elementAt(var2);
               if (var2 == this.idCommand) {
                  var3.AG = true;
               } else {
                  var3.AG = false;
               }
            }
         }
      }

      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (this.vecCmd != null && this.idCommand < this.vecCmd.size()) {
            ((iCommand)this.vecCmd.elementAt(this.idCommand)).AD();
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      for(int var1 = 0; var1 < this.vecCmd.size(); ++var1) {
         ((iCommand)this.vecCmd.elementAt(var1)).AE();
      }

      super.updatePointer();
   }

   public void onServersUpdated() {
      this.getVecBegin();
   }
}
