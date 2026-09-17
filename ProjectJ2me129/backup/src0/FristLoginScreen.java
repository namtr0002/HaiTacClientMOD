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

   // mới: quản lý pagination cho server grid
   private int serverPage = 0;
   private int pages = 1;
   private int serversPerPage = 0;

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
      // safe caption guard
      String serverCaption = T.server;
      if (GameCanvas.strListServer != null
          && GameCanvas.language >= 0
          && GameCanvas.language < GameCanvas.strListServer.length
          && GameCanvas.strListServer[GameCanvas.language] != null
          && GameCanvas.strListServer[GameCanvas.language].length > GameCanvas.IndexServer
      ) {
          serverCaption = T.server + "\n" + GameCanvas.strListServer[GameCanvas.language][GameCanvas.IndexServer];
      }
      this.cmdServer = new iCommand(serverCaption, 4, this);

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

   // ---------- new: build grid helper (pagination + dynamic layout) ----------
   private void buildServerGrid() {
       // reset vecCmd and build only grid + nav; don't include bottom menu buttons here
       this.vecCmd.removeAllElements();

       // safe servers array
       if (GameCanvas.strListServer == null
           || GameCanvas.language < 0
           || GameCanvas.language >= GameCanvas.strListServer.length
           || GameCanvas.strListServer[GameCanvas.language] == null) {
           return;
       }

       String[] servers = GameCanvas.strListServer[GameCanvas.language];
       int n = servers.length;
       if (n <= 0) return;

       // layout params (tweak these if you want denser or looser)
       int padX = 10;
       int topMargin = 18;
       int bottomMargin = 110; // chừa chỗ cho các nút dưới
       int minCellWidth = 72;
       int preferredBtnWidth = 100;
       int cellHeight = 36;
       int minCellHeight = 28;

       // available area for grid
       int availHeight = MotherCanvas.h - topMargin - bottomMargin;
       if (availHeight < 40) availHeight = Math.max(40, MotherCanvas.h - 20);

       // max columns by width
       int maxColsByWidth = Math.max(1, (MotherCanvas.w - 2 * padX) / minCellWidth);
       int cols = Math.min(n, maxColsByWidth);

       // compute rows needed for that cols
       int rows = (n + cols - 1) / cols;

       // max rows that can fit with default cellHeight
       int maxRowsFit = Math.max(1, availHeight / cellHeight);

       // if rows > maxRowsFit, we need pagination (multiple pages)
       if (rows > maxRowsFit) {
           // attempt to increase cols to reduce rows
           boolean found = false;
           for (int tryCols = cols; tryCols <= Math.min(n, maxColsByWidth); tryCols++) {
               int tryRows = (n + tryCols - 1) / tryCols;
               if (tryRows <= maxRowsFit) {
                   cols = tryCols;
                   rows = tryRows;
                   found = true;
                   break;
               }
           }
           if (!found) {
               // cannot fit all rows in one page -> we'll use pagination
               // determine rowsPerPage = maxRowsFit, serversPerPage = cols * rowsPerPage
               int rowsPerPage = maxRowsFit;
               this.serversPerPage = cols * rowsPerPage;
               this.pages = (n + this.serversPerPage - 1) / this.serversPerPage;
               // ensure serverPage in range
               if (this.serverPage < 0) this.serverPage = 0;
               if (this.serverPage >= this.pages) this.serverPage = this.pages - 1;
               // recalc rows for page (may be less for last page)
               rows = rowsPerPage;
               // totalHeight according to rowsPerPage
               int totalHeight = rowsPerPage * cellHeight;
               // compute horizontal spacing
               int usableWidth = MotherCanvas.w - 2 * padX;
               int spacingX = usableWidth / Math.max(1, cols);
               if (spacingX < minCellWidth) spacingX = minCellWidth;
               int btnWidth = Math.min(preferredBtnWidth, spacingX - 8);
               int gridWidth = spacingX * cols;
               int startX = MotherCanvas.hw - gridWidth / 2 + (spacingX - btnWidth) / 2;
               if (startX < 6) startX = 6;
               int startY = topMargin + Math.max(0, (availHeight - totalHeight) / 2);

               // draw only servers for current page
               int startIndex = this.serverPage * this.serversPerPage;
               int endIndex = Math.min(n, startIndex + this.serversPerPage);
               for (int idx = startIndex; idx < endIndex; idx++) {
                   int local = idx - startIndex;
                   int col = local % cols;
                   int row = local / cols;
                   int x = startX + col * spacingX;
                   int y = startY + row * cellHeight;
                   iCommand cmd = new iCommand(T.server + "\n" + servers[idx], 5, idx, this);
                   cmd.setPos(x, y, (FrameImage) null, cmd.caption);
                   cmd.setTypeSpec();
                   this.vecCmd.addElement(cmd);
               }

               // add Prev / Next navigation if pages > 1
               if (this.pages > 1) {
                   int navY = startY + totalHeight + 8;
                   int navOffset = 36;
                   if (this.serverPage > 0) {
                       iCommand prev = new iCommand("<", 30, 0, this);
                       prev.setPos(MotherCanvas.hw - navOffset, navY, (FrameImage)null, prev.caption);
                       prev.setTypeSpec();
                       this.vecCmd.addElement(prev);
                   }
                   if (this.serverPage < this.pages - 1) {
                       iCommand next = new iCommand(">", 31, 0, this);
                       next.setPos(MotherCanvas.hw + navOffset, navY, (FrameImage)null, next.caption);
                       next.setTypeSpec();
                       this.vecCmd.addElement(next);
                   }
               }

               return;
           }
       }

       // If we reach here, either everything fits single page OR we adjusted cols to fit
       // compute spacing and place all servers (single page)
       int usableWidth = MotherCanvas.w - 2 * padX;
       int spacingX = usableWidth / Math.max(1, cols);
       if (spacingX < minCellWidth) spacingX = minCellWidth;
       int btnWidth = Math.min(preferredBtnWidth, spacingX - 8);
       int gridWidth = spacingX * cols;
       int startX = MotherCanvas.hw - gridWidth / 2 + (spacingX - btnWidth) / 2;
       if (startX < 6) startX = 6;
       int totalHeight = rows * cellHeight;
       int startY = topMargin + Math.max(0, (availHeight - totalHeight) / 2);

       // single page: place all servers
       for (int i = 0; i < n; i++) {
           int col = i % cols;
           int row = i / cols;
           int x = startX + col * spacingX;
           int y = startY + row * cellHeight;
           iCommand cmd = new iCommand(T.server + "\n" + servers[i], 5, i, this);
           cmd.setPos(x, y, (FrameImage) null, cmd.caption);
           cmd.setTypeSpec();
           this.vecCmd.addElement(cmd);
       }

       // single page => reset pagination vars
       this.pages = 1;
       this.serverPage = 0;
       this.serversPerPage = n;
   }
   // ---------- end buildServerGrid ----------

   public final void commandPointer(int var1, int var2) {
      int var4;
      label49:
      switch(var1) {
      case 0:
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
         GameCanvas.loginScr.Show();
         break;
      case 2:
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

      // ================ CASE 4: build server grid (open selector) ================
      case 4:
        // khi mở selector, reset page về 0 để bắt đầu từ đầu (bạn muốn giữ page có thể bỏ dòng này)
        this.serverPage = 0;
        buildServerGrid();
        break;

      // chọn server (server iCommand được tạo với id = 5)
      case 5:
        GameCanvas.IndexServer = var2;
        GameCanvas.hostServer = UpdateServer.getHost(var2);   // chỉ lấy host
        GameCanvas.portServer = UpdateServer.getPort(var2);   // lấy port riêng
        this.getVecBegin();
        break;

      // prev page
      case 30:
        if (this.serverPage > 0) {
            this.serverPage--;
            buildServerGrid();
        }
        break;
      // next page
      case 31:
        if (this.serverPage < this.pages - 1) {
            this.serverPage++;
            buildServerGrid();
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
         this.cmdServer.caption = T.server + "\n" + GameCanvas.strListServer[GameCanvas.language][GameCanvas.IndexServer];
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
         this.cmdServer.caption = T.server + "\n" + GameCanvas.strListServer[GameCanvas.language][GameCanvas.IndexServer];
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
}
