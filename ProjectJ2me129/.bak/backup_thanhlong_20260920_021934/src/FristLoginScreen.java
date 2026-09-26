import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class FristLoginScreen extends MainScreen {
   public iCommand cmdBegin;
   public iCommand cmdMenu;
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
       UpdateServer.loadServers();
       // đảm bảo IndexServer hợp lệ sau khi load server mới
        if (UpdateServer.serverHosts.size() > 0) {
            if (GameCanvas.IndexServer < 0 || GameCanvas.IndexServer >= UpdateServer.serverHosts.size()) {
                GameCanvas.IndexServer = 0;
            }
        }
      this.cmdBegin = new iCommand(T.loadGame, 0, 0, this);
      SaveRms.userLast = "";
      if (CRes.loadRMS("MAIN_user_pass") != null) {
         SaveRms.AL();
         if (GameCanvas.loginScr.AC != null) {
            String u = GameCanvas.loginScr.AC.getText().trim();
            if (u.length() > 0) {
               SaveRms.userLast = u;
            }
         }
      } else if (CRes.loadRMS("MAIN_user_last") != null) {
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AA();
      } else if (CRes.loadRMS("MAIN_frist_login") != null) {
         try {
            byte[] data = CRes.loadRMS("MAIN_frist_login");
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            String guestName = dis.readUTF();
            if (guestName != null && guestName.trim().length() > 0) {
               SaveRms.userLast = guestName.trim();
               AH = guestName.trim();
            }
            if (dis.available() > 0) {
               GameCanvas.IndexServer = dis.readByte();
            }
            dis.close();
         } catch (Exception ignored) {}
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
      if (CRes.loadRMS("MAIN_user_pass") != null) {
         SaveRms.AL();
         if (GameCanvas.loginScr.AC != null) {
            String u = GameCanvas.loginScr.AC.getText().trim();
            if (u.length() > 0) {
               SaveRms.userLast = u;
            }
         }
      } else if (CRes.loadRMS("MAIN_user_last") != null) {
         SaveRms.AA();
      } else if (CRes.loadRMS("MAIN_frist_login") != null) {
         try {
            byte[] data = CRes.loadRMS("MAIN_frist_login");
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            String guestName = dis.readUTF();
            if (guestName != null && guestName.trim().length() > 0) {
               SaveRms.userLast = guestName.trim();
               AH = guestName.trim();
            }
            dis.close();
         } catch (Exception ignored) {}
      }
      this.getVecBegin();
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
      if (var1 >= 100 && var1 < 100 + UIThemeManager.TOTAL_THEMES) {
         int selTheme = var1 - 100;
         UIThemeManager.setTheme(selTheme);
         GameCanvas.menu.doCloseMenu();
         return;
      }
      if (var1 == 99) {
         openMenuLeft();
         return;
      }
      if (var1 == 97) {
         openSelectThemeMenu();
         return;
      }
      if (var1 == 8) {
         GameMidlet.AA();
         return;
      }
      int var4;
      label49:
      switch(var1) {
      case 0:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         ListChar_Screen.IndexCharSelected = -1;

         // 1. Kiểm tra tài khoản thật đã lưu (MAIN_user_pass)
         if (CRes.loadRMS("MAIN_user_pass") != null) {
            SaveRms.AL();
            String savedUser = GameCanvas.loginScr.AC != null ? GameCanvas.loginScr.AC.getText().trim() : "";
            String savedPass = GameCanvas.loginScr.AD != null ? GameCanvas.loginScr.AD.getText().trim() : "";
            if (savedUser.length() > 0 && savedPass.length() > 0) {
               GameCanvas.loginScr.AA(true, (byte)0, savedUser, savedPass);
               break;
            }
         }

         // 2. Kiểm tra tài khoản khách / chơi nhanh đã lưu (MAIN_frist_login)
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

         if (AH != null && AH.trim().length() > 0) {
            GameCanvas.loginScr.AA(true, (byte)1, AH, "");
            break;
         }

         // 3. Nếu chỉ có userLast mà không có pass, mở form đăng nhập
         if (CRes.loadRMS("MAIN_user_last") != null) {
            SaveRms.AA();
            GameCanvas.loginScr.Show();
            break;
         }

         // 4. Nếu chưa từng lưu tài khoản nào, mở form đăng nhập
         GameCanvas.loginScr.Show();
         break;
      case 1:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         GameCanvas.loginScr.Show();
         break;
      case 2:
         if (!KeyAuthManager.checkAuthorizedOrNotice()) return;
         if (CRes.loadRMS("MAIN_frist_login") != null) {
            GameCanvas.Start_Normal_DiaLog("Bạn đang có tài khoản chơi mới trên máy.\nNếu tạo mới sẽ thay thế tài khoản cũ. Bạn có muốn tiếp tục?", new iCommand("Đồng ý", 21, this), true);
         } else {
            GameCanvas.loginScr.AA(true, (byte)1, "", "");
         }
         break;
      case 21:
         GameCanvas.end_Dialog();
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

      String sName = UpdateServer.getCurrentServerName();

      // Luôn có đầy đủ 4 nút: Chơi tiếp, Chơi mới, Đổi tài khoản, Chọn server
      if (SaveRms.userLast != null && SaveRms.userLast.trim().length() > 0) {
         this.cmdBegin.caption = T.loadGame + "\n " + SaveRms.userLast;
      } else {
         this.cmdBegin.caption = T.loadGame;
      }
      this.cmdBegin.setTypeSpec();

      this.cmdNewGame = new iCommand(T.newGame, 2, 0, this);
      this.cmdNewGame.setTypeSpec();

      this.cmdChangeAcc = new iCommand(T.changeAcc, 1, 0, this);
      this.cmdChangeAcc.setTypeSpec();

      this.cmdServer.caption = T.server + "\n" + sName;
      this.cmdServer.setTypeSpec();

      // Bố cục Dynamic Ngang responsive:
      int btnW = iCommand.wButtonCmd;
      if (MotherCanvas.w >= 320) {
         int gap = 8;
         int totalW = 4 * btnW + 3 * gap;
         if (totalW > MotherCanvas.w - 16) {
            gap = Math.max(2, (MotherCanvas.w - 16 - 4 * btnW) / 3);
            totalW = 4 * btnW + 3 * gap;
         }
         int startX = MotherCanvas.hw - totalW / 2 + btnW / 2;
         int posY = MotherCanvas.h - 52;

         this.cmdBegin.setPos(startX + 0 * (btnW + gap), posY, (FrameImage)null, this.cmdBegin.caption);
         this.cmdNewGame.setPos(startX + 1 * (btnW + gap), posY, (FrameImage)null, this.cmdNewGame.caption);
         this.cmdChangeAcc.setPos(startX + 2 * (btnW + gap), posY, (FrameImage)null, this.cmdChangeAcc.caption);
         this.cmdServer.setPos(startX + 3 * (btnW + gap), posY, (FrameImage)null, this.cmdServer.caption);
      } else {
         int gap = 6;
         int totalW = 2 * btnW + gap;
         int startX1 = MotherCanvas.hw - totalW / 2 + btnW / 2;
         int startX2 = startX1 + btnW + gap;
         int posY1 = MotherCanvas.h - 82;
         int posY2 = MotherCanvas.h - 44;

         this.cmdBegin.setPos(startX1, posY1, (FrameImage)null, this.cmdBegin.caption);
         this.cmdNewGame.setPos(startX2, posY1, (FrameImage)null, this.cmdNewGame.caption);
         this.cmdChangeAcc.setPos(startX1, posY2, (FrameImage)null, this.cmdChangeAcc.caption);
         this.cmdServer.setPos(startX2, posY2, (FrameImage)null, this.cmdServer.caption);
      }

      this.vecCmd.addElement(this.cmdBegin);
      this.vecCmd.addElement(this.cmdNewGame);
      this.vecCmd.addElement(this.cmdChangeAcc);
      this.vecCmd.addElement(this.cmdServer);

      this.cmdMenu = new iCommand(T.AU, 99, this);
      if (GameCanvas.isTaiTho) {
         this.cmdMenu.setPos(30, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
      } else {
         this.cmdMenu.setPos(15, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
      }
      if (GameCanvas.isTouch) {
         this.vecCmd.addElement(this.cmdMenu);
      }
      super.DA = this.cmdMenu;

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
         } else if (MotherCanvas.w < 320 && GameCanvas.isKeyPressed(1)) {
            this.idCommand = Math.max(0, this.idCommand - 2);
            GameCanvas.clearKeyPressed(1);
         } else if (MotherCanvas.w < 320 && GameCanvas.isKeyPressed(3)) {
            this.idCommand = Math.min(var1 - 1, this.idCommand + 2);
            GameCanvas.clearKeyPressed(3);
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

   public void openMenuLeft() {
      mVector vec = new mVector();
      UITheme curTheme = UIThemeManager.getCurrentTheme();
      String themeName = (curTheme != null) ? curTheme.displayName : "Giao Di\u1ec7n";
      vec.addElement(new iCommand("\uD83C\uDFA8 Giao di\u1ec7n: " + themeName, 97, this));
      vec.addElement(new iCommand("\uD83C\uDF10 " + T.server, 4, this));
      vec.addElement(new iCommand("\uD83D\uDD11 Nh\u1eadp Key B\u1ea3n Quy\u1ec1n", 23, this));
      if (KeyAuthManager.isAuthorized) {
         vec.addElement(new iCommand("+ Th\u00eam IP Server", 25, this));
      }
      vec.addElement(new iCommand("\uD83D\uDD04 " + T.changeAcc, 1, 0, this));
      vec.addElement(new iCommand("\u274C " + T.AI, 8, this));
      GameCanvas.menu.startAt(vec, 0, T.AU);
   }

   public void openSelectThemeMenu() {
      mVector vec = new mVector();
      String[] names = UIThemeManager.getThemeNames();
      int curTheme = UIThemeManager.getCurrentThemeId();
      for (int i = 0; i < names.length; i++) {
         iCommand cmd = new iCommand(names[i], 100 + i, this);
         vec.addElement(cmd);
      }
      GameCanvas.menu.startAt(vec, 2, "Ch\u1ecdn Giao Di\u1ec7n");
      if (curTheme >= 0 && curTheme < names.length) {
         GameCanvas.menu.AC = curTheme;
         GameCanvas.menu.menuSelectedItem = curTheme;
      }
   }

   public void onServersUpdated() {
      this.getVecBegin();
   }
}
