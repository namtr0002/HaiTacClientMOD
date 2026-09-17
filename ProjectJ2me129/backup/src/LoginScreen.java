import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class LoginScreen extends MainScreen {
   public byte AA = 0;
   public static boolean AB = false;
   public TField AC;
   public TField AD;
   private TField AI;
   private iCommand AJ;
   private iCommand AK;
   private iCommand AL;
   private iCommand AM;
   private iCommand AN;
   private iCommand AO;
   private iCommand AP;
   private iCommand AQ;
   private iCommand AR;
   private iCommand AS;
   private iCommand AT;
   public static iCommand AE;
   private static int AU = 0;
   private int AV;
   private int AW;
   private int AX;
   private int AY;
   private InputDialog AZ;
   private static mVector vecEff = new mVector("LoginScreen.vecEff");
   private static mVector vecOBJ = new mVector("LoginScreen.vecOBJ");
   private static boolean isNewShow = false;
   private static boolean BD = false;
   public static int yPaintLogo;
   private static int BE = 0;
   private int BF;
   private int BG;
   private int BH;
   private int BI;
   public static int hLogo = 50;
   public static int AH = 0;
   private static int BJ;
   private static short BK = 0;
   // thay đổi: hiển thị tối đa 4 hàng trong dropdown
   private static final int MAX_VISIBLE_ROWS = 4;

   // index scroll (hàng đầu tiên đang hiển thị trong dropdown)
   private int serverScrollIndex = 0;

   // ---------------------- Constructor (thay thế hoàn chỉnh) ----------------------
   public LoginScreen() {
    new mVector();
    this.AV = 32;
    this.AW = 0;
    this.AX = 0;
    this.AY = 0;
    this.BG = 180;
    this.BH = 6;
    this.BI = 85;
    if (GameCanvas.isTouch) {
        this.BI = 120 + this.AV + 2;
        this.AV = 40;
        this.AY = 4;
    } else {
        this.BI = 85 + this.AV + 2;
    }

    // lấy số phần tử server an toàn
    if (GameCanvas.strListServer != null
            && GameCanvas.language >= 0
            && GameCanvas.language < GameCanvas.strListServer.length
            && GameCanvas.strListServer[GameCanvas.language] != null) {
        this.AX = GameCanvas.strListServer[GameCanvas.language].length;
    } else {
        this.AX = 0;
    }

    // nâng/giảm vị trí khung để dropdown không quá thấp
    AU = MotherCanvas.hh - this.BI / 2 + 10;

    // tạo các ô nhập
    this.AC = new TField(MotherCanvas.hw - 70, AU + 17, 140);
    this.AC.AA(T.EX);
    this.AI = new TField(MotherCanvas.hw - 70, AU + 17 - this.AV, 140);

    if (CRes.loadRMS("MAIN_ip_last") != null) {
        GameCanvas.hostServer = SaveRms.AB().trim();
        this.AI.AB(GameCanvas.hostServer);
    } else {
        if (!UpdateServer.serverHosts.isEmpty()) {
            this.AI.AB(UpdateServer.getHost(0) + ":" + UpdateServer.getPort(0));
        } else {
            this.AI.AB("");
        }
    }

    // TẠO AD: width + x căn giữa so với BF vùng login
    int listWidth = 140; // bạn có thể chỉnh về 120 nếu muốn nhỏ hơn
    int listX = MotherCanvas.hw - listWidth / 2;
    int listY = AU + 17 + this.AV;
    this.AD = new TField(listX, listY, listWidth);
    this.AD.AG(2);
    this.AD.AA(T.EY);

    // Đặt chiều cao item dựa trên font + padding (đảm bảo không quá to)
    try {
        int fh = mFont.tahoma_7b_black.getHeight();
        this.AD.AD = Math.max(22, fh + 8);
    } catch (Exception ex) {
        this.AD.AD = 24;
    }

    // cập nhật lại số phần tử server sau khi AD đã khởi tạo
    if (GameCanvas.strListServer != null
            && GameCanvas.language >= 0
            && GameCanvas.language < GameCanvas.strListServer.length
            && GameCanvas.strListServer[GameCanvas.language] != null) {
        this.AX = GameCanvas.strListServer[GameCanvas.language].length;
    } else {
        this.AX = 0;
    }

    // clamp BE (scroll offset) trong khoảng hợp lệ (vẫn giữ BE vì các phần khác có thể dùng)
    if (this.AD.AD > 0) {
        int maxScroll = Math.max(0, this.AD.AD * (this.AX - Math.min(this.AX, MAX_VISIBLE_ROWS)));
        if (BE < -maxScroll) BE = -maxScroll;
        if (BE > 0) BE = 0;
    }

    // clamp serverScrollIndex ban đầu
    clampServerScroll();

    // Các phần còn lại khởi tạo command giống cũ (không thay đổi logic)
    this.AO = new iCommand(T.newGame, 7, this);
    this.AJ = new iCommand(T.AF, 0, this);
    this.AK = new iCommand(T.AU, 1, this);
    this.AL = new iCommand(T.CX, 3, this);
    this.AM = new iCommand(T.AI, 4, this);
    this.AN = new iCommand(T.on + T.HW, 6, this);
    this.AQ = new iCommand(T.loadGame, 12, this);
    AE = new iCommand(T.MB, 13, this);
    this.AP = new iCommand(T.LB, 10, this);
    this.AR = new iCommand(T.IR, 15, this);
    this.AS = new iCommand(T.QW, 18, this);
    if (GameCanvas.lowGraphic) {
        this.AN.caption = T.off + T.HW;
    }
    this.AT = new iCommand(T.on + T.SO, 19, this);
    if (GameCanvas.isTaiTho) {
        this.AN.caption = T.off + T.SO;
    }
    this.AJ = AvMain.AA(this.AJ, 0);
    this.AL = AvMain.AA(this.AL, 0);
    this.AK = AvMain.AA(this.AK, 1);

    if (GameCanvas.isTouch) {
        if (GameCanvas.isTaiTho) {
            this.AK.setPos(30, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
            this.AP.setPos(MotherCanvas.w - 30, MotherCanvas.h - 15, AvMain.fraIconHome, "");
        } else {
            this.AK.setPos(15, MotherCanvas.h - 15, AvMain.fraIconMenu, "");
            this.AP.setPos(MotherCanvas.w - 15, MotherCanvas.h - 15, AvMain.fraIconHome, "");
        }
        super.DB = this.AP;
    }

    if (GameCanvas.isKeyPressed()) {
        this.AC.AA(true);
    }
    if (GameCanvas.isTouch) {
        this.AJ.setPos(MotherCanvas.hw, AU + this.BI, AvMain.fraBtLogin, "");
        this.AJ.isPlayframe = true;
        this.AL.AA(MotherCanvas.hw, AU + this.BI - iCommand.hButtonCmdNor + 8);
    }

    super.DA = this.AK;
    super.DE = this.AK;
    this.AN();

    // load MAIN_IPNEW nếu có
    byte[] var1;
    if ((var1 = GameMidlet.AA("Main_IPNEW")) != null) {
        try {
            ByteArrayInputStream var3 = new ByteArrayInputStream(var1);
            DataInputStream var4;
            GameCanvas.hostServer = (var4 = new DataInputStream(var3)).readUTF();
            var4.close();
        } catch (Exception var2) {
        }
    }

    if (yPaintLogo == 0) {
        yPaintLogo = hLogo;
    }
   }

   // helper clamp scroll index
   private void clampServerScroll() {
       int total = Math.max(0, this.AX);
       int maxIndex = Math.max(0, total - Math.min(total, MAX_VISIBLE_ROWS));
       if (this.serverScrollIndex < 0) this.serverScrollIndex = 0;
       if (this.serverScrollIndex > maxIndex) this.serverScrollIndex = maxIndex;
   }

   // ---------------------- paint (thay thế hoàn chỉnh) ----------------------
   public final void paint(mGraphics var1) {
    try {
        if (GameCanvas.mapBack != null) {
            GameCanvas.mapBack.AC(var1);
            GameCanvas.mapBack.AE(var1);
            GameCanvas.mapBack.AD(var1);
        }
    } catch (Exception var7) {
    }

    AB(var1);
    int var10002 = MotherCanvas.hw - this.BF / 2;
    int var10006 = this.BG;
    this.AD(var1, var10002, AU, this.BF, this.BI, 0);
    GameCanvas.resetTrans(var1);
    paintLogo(var1, MotherCanvas.hw);
    mFont.tahoma_7_black.drawString(var1, "Ver: 1.2.9", MotherCanvas.w - 2, 2 + GameScreen.h12plus, 1);
    mFont.tahoma_7_black.drawString(var1, "No: 1", MotherCanvas.w - 2, 4 + GameScreen.h12plus + GameCanvas.hText / 2, 1);
    GameCanvas.resetTrans(var1);

    // vẽ phần input (ghi nhớ vùng clip để tránh vẽ vượt)
    var1.AD(MotherCanvas.hw - this.BF / 2, 0, this.BF, MotherCanvas.h);
    mGraphics.AC();
    this.AC.AA(var1, MotherCanvas.hw - this.BF / 2 < this.AC.AA);
    mGraphics.restoreCanvas();

    var1.AD(MotherCanvas.hw - this.BF / 2, 0, this.BF, MotherCanvas.h);
    mGraphics.AC();
    this.AD.AA(var1, MotherCanvas.hw - this.BF / 2 < this.AD.AA);
    mGraphics.restoreCanvas();
    GameCanvas.resetTrans(var1);
    mGraphics g = var1;
    LoginScreen self = this;

    // --- vẽ server box hoặc dropdown ---
    // lấy servers an toàn
    String[] servers = null;
    if (GameCanvas.strListServer != null
            && GameCanvas.language >= 0
            && GameCanvas.language < GameCanvas.strListServer.length) {
        servers = GameCanvas.strListServer[GameCanvas.language];
    }

    if (this.AW != 2) {
        // collapsed
        AvMain.paintRect(g, this.AD.AA, this.AD.AB + this.AV, this.AD.AC, this.AD.AD, (byte)0, this.AW == -2 ? 0 : 1);
        String curr = "";
        if (servers != null && GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < servers.length) {
            curr = T.server + " " + servers[GameCanvas.IndexServer];
        }
        mFont.tahoma_7b_black.drawString(g, curr, this.AD.AA + 4, this.AD.AB + this.AD.AD - 16 + this.AV - this.AY, 0);
        g.drawRegion(AvMain.imgArrowListServer, 0, (this.AW == -2 ? 0 : 1) * 10, 15, 10, 0, this.AD.AA + this.AD.AC - 12, this.AD.AB + this.AV + this.AD.AD / 2, 3);
    } else {
        // expanded dropdown: compute visible rows, draw up if not enough space below
        int totalRows = Math.max(0, this.AX);
        int visibleRows = Math.min(totalRows, MAX_VISIBLE_ROWS);
        int itemH = Math.max(1, this.AD.AD);
        int listHeight = itemH * visibleRows;

        int listX = this.AD.AA;
        int belowY = this.AD.AB + this.AV;
        int spaceBelow = MotherCanvas.h - belowY;
        int listY;
        boolean drawUp = false;
        if (spaceBelow < listHeight + 10) {
            drawUp = true;
            listY = this.AD.AB + this.AV - listHeight; // vẽ lên trên
        } else {
            listY = belowY; // vẽ xuống dưới
        }

        // clamp BE (giữ để tương thích)
        int maxScroll = Math.max(0, itemH * (totalRows - visibleRows));
        if (BE < -maxScroll) BE = -maxScroll;
        if (BE > 0) BE = 0;

        // background
        AvMain.paintRect(g, listX, listY, this.AD.AC, listHeight, (byte)0, 0);

        // clamp serverScrollIndex
        clampServerScroll();
        int firstIndex = this.serverScrollIndex;
        int lastIndex = Math.min(totalRows, firstIndex + visibleRows);

        for (int idx = firstIndex; idx < lastIndex; idx++) {
            int drawRow = idx - firstIndex;
            int yItem = listY + drawRow * itemH; // dùng serverScrollIndex (hàng) chứ không dùng BE pixel offset

            if (drawRow > 0) {
                g.setColor(-4167672);
                g.drawRect(listX + 6, yItem - 1, this.AD.AC - 12, 1);
            }

            if (idx == GameCanvas.IndexServer) {
                g.setColor(-2582464);
                g.fillRect(listX + 3, yItem + 3, this.AD.AC - 4, itemH - 6);
            }

            String text = "";
            if (servers != null && idx < servers.length) {
                text = T.server + " " + servers[idx];
            }

            int textY = yItem + (itemH - mFont.tahoma_7b_black.getHeight()) / 2 + 1;
            mFont.tahoma_7b_black.drawString(g, text, listX + 4, textY, 0);

            if (idx > 0 && servers != null && idx == servers.length - 1) {
                int tw = mFont.tahoma_7b_black.getWidth(text);
                AvMain.fraNew.drawFrame(GameCanvas.gameTick / 5 % AvMain.fraNew.nFrame,
                        listX + 4 + tw + 16,
                        yItem + itemH - 16 - this.AY + 4,
                        0, 3, g);
            }
        }

        // scrollbar (dựa trên serverScrollIndex)
        if (totalRows > visibleRows && itemH > 0) {
            int scrollbarH = listHeight;
            int thumbH = Math.max(8, scrollbarH * visibleRows / totalRows);
            int maxIndex = Math.max(0, totalRows - visibleRows);
            int thumbMax = scrollbarH - thumbH;
            int thumbY;
            if (maxIndex > 0) {
                thumbY = listY + (this.serverScrollIndex * thumbMax) / maxIndex;
            } else {
                thumbY = listY;
            }
            int thumbX = listX + this.AD.AC - 6;
            g.setColor(0x444444);
            g.fillRect(thumbX, listY + 2, 4, scrollbarH - 4);
            g.setColor(0xAAAAAA);
            g.fillRect(thumbX, thumbY, 4, thumbH);
        }
    }

    // vẽ các button/controls khác khi dropdown đóng
    if (this.AW != 2 && this.AW != -2) {
        if (GameCanvas.isTouch && !GameCanvas.lowGraphic) {
            AvMain.fraBtBanhlai.drawFrame(AH, this.AJ.xCmd, this.AJ.yCmd, 0, 3, var1);
        }
        super.paint(var1);
    }
   }

   // ---------------------- updatePointer (thay thế hoàn chỉnh) ----------------------
   public final void updatePointer() {
    if (this.AW == 2) {
        int totalRows = this.AX;
        int visibleRows = Math.min(totalRows, MAX_VISIBLE_ROWS);
        int itemH = Math.max(1, this.AD.AD);
        int listHeight = itemH * visibleRows;
        int listX = this.AD.AA;
        int belowY = this.AD.AB + this.AV;
        int spaceBelow = MotherCanvas.h - belowY;
        int listY;
        if (spaceBelow < listHeight + 10) {
            listY = this.AD.AB + this.AV - listHeight; // hiển thị lên trên
        } else {
            listY = belowY; // hiển thị xuống dưới
        }

        // clamp scroll index
        clampServerScroll();
        int maxIndex = Math.max(0, totalRows - visibleRows);
        if (this.serverScrollIndex > maxIndex) this.serverScrollIndex = maxIndex;
        if (this.serverScrollIndex < 0) this.serverScrollIndex = 0;

        int firstIndex = this.serverScrollIndex;
        int lastIndex = Math.min(totalRows, firstIndex + visibleRows);

        // Detect taps on top/bottom small zones to scroll
        int topZoneY = listY;
        int topZoneH = Math.max(6, itemH / 4); // nhỏ vùng chạm để scroll lên
        if (GameCanvas.AB(listX, topZoneY, this.AD.AC, topZoneH)) {
            if (this.serverScrollIndex > 0) {
                this.serverScrollIndex--;
            }
            GameCanvas.isPointerSelect = false;
            return;
        }

        int bottomZoneY = listY + listHeight - Math.max(6, itemH / 4);
        int bottomZoneH = Math.max(6, itemH / 4);
        if (GameCanvas.AB(listX, bottomZoneY, this.AD.AC, bottomZoneH)) {
            if (this.serverScrollIndex < maxIndex) {
                this.serverScrollIndex++;
            }
            GameCanvas.isPointerSelect = false;
            return;
        }

        // detect tap on items
        for (int idx = firstIndex; idx < lastIndex; idx++) {
            int visRow = idx - firstIndex;
            int yRect = listY + visRow * itemH;
            if (GameCanvas.AB(listX, yRect, this.AD.AC, itemH)) {
                this.AE(idx);
                this.AW = -1;
                GameCanvas.isPointerSelect = false;
                break;
            }
        }

        if (GameCanvas.AB(0, 0, MotherCanvas.w, MotherCanvas.h)) {
            this.AW = -1;
            GameCanvas.isPointerSelect = false;
        }
    } else if (GameCanvas.AB(this.AD.AA, this.AD.AB + this.AV, this.AD.AC, this.AD.AD)) {
        this.AW = 2;
        GameCanvas.isPointerSelect = false;

        // khi mở dropdown, đảm bảo server hiện tại nằm trong vùng visible
        int totalRows = this.AX;
        int visibleRows = Math.min(totalRows, MAX_VISIBLE_ROWS);
        int maxIndex = Math.max(0, totalRows - visibleRows);
        if (GameCanvas.IndexServer < 0) GameCanvas.IndexServer = 0;
        if (GameCanvas.IndexServer > totalRows - 1) GameCanvas.IndexServer = totalRows - 1;
        if (GameCanvas.IndexServer < this.serverScrollIndex) {
            this.serverScrollIndex = GameCanvas.IndexServer;
        } else if (GameCanvas.IndexServer >= this.serverScrollIndex + visibleRows) {
            this.serverScrollIndex = GameCanvas.IndexServer - visibleRows + 1;
        }
        if (this.serverScrollIndex < 0) this.serverScrollIndex = 0;
        if (this.serverScrollIndex > maxIndex) this.serverScrollIndex = maxIndex;
    }

    super.updatePointer();
    this.AC.updatePointer();
    this.AI.updatePointer();
    this.AD.updatePointer();
   }

    public void onServersUpdated() {
        // Cập nhật lại số lượng server theo ngôn ngữ hiện tại
        if (GameCanvas.strListServer != null 
            && GameCanvas.language >= 0 
            && GameCanvas.language < GameCanvas.strListServer.length 
            && GameCanvas.strListServer[GameCanvas.language] != null) {
            this.AX = GameCanvas.strListServer[GameCanvas.language].length;
        } else {
            this.AX = 0;
        }

        // Kiểm tra lại IndexServer hợp lệ
        if (GameCanvas.IndexServer < 0 || GameCanvas.IndexServer >= UpdateServer.serverHosts.size()) {
            // Tìm server đầu tiên phù hợp ngôn ngữ
            int lang = GameCanvas.language;
            for (int i = 0; i < UpdateServer.serverLang.size(); i++) {
                if (UpdateServer.serverLang.get(i) == lang) {
                    GameCanvas.IndexServer = i;
                    break;
                }
            }
        }

        // Cập nhật host/port
        if (GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < UpdateServer.serverHosts.size()) {
            GameCanvas.hostServer = UpdateServer.getHost(GameCanvas.IndexServer);
            GameCanvas.portServer = UpdateServer.getPort(GameCanvas.IndexServer);
        }

        // Clamp BE (scroll)
        if (this.AD != null && this.AD.AD > 0) {
            int maxScroll = Math.max(0, this.AD.AD * (this.AX - Math.min(this.AX, MAX_VISIBLE_ROWS)));
            if (BE < -maxScroll) BE = -maxScroll;
            if (BE > 0) BE = 0;
        }

        // clamp scroll index theo AX
        clampServerScroll();
    }

   public final void Show(MainScreen var1) {
      this.Show();
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void Show() {
       UpdateServer.loadServers();
      if (GameScreen.CU != null) {
         GameScreen.CU.removeAllElements();
      }

      if (GameCanvas.mapBack == null) {
         GameCanvas.mapBack = new MapBackGround();
      }

      GameCanvas.mapBack.AC();
      GameScreen.player = null;
      Session_ME.getInstance().close();
      this.AA = 0;
      this.AH();
      this.BF = 5;
      if (GameCanvas.currentScreen != null && GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.fristLoginScr) {
         AF();
      }

      super.Show();
      float var10000 = mSound.AB;
      mSound.AC();
      isNewShow = true;
   }

   public static void AF() {
      vecOBJ.removeAllElements();
      vecEff.removeAllElements();
   }

   public static void AG() {
   }

   public final void AH() {
      if (this.AA == 0) {
         super.center = this.AJ;
      } else {
         super.center = this.AL;
      }
   }

   public final void commandPointer(int var1, int var2) {
      mVector var7;
      switch(var1) {
      case 0:
         if (this.AC.getText().trim().length() > 0 && this.AD.getText().trim().length() > 0) {
            ListChar_Screen.IndexCharSelected = -1;
            this.AA(true, (byte)0, this.AC.getText(), this.AD.getText());
         } else {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.checkRegister1);
         }
         break;
      case 1:
         var7 = new mVector();
         SaveRms.userLast = "";
         SaveRms var9 = GameCanvas.saveRms;
         SaveRms.AA();
         if (SaveRms.userLast.length() > 0 && CRes.loadRMS("MAIN_user_pass") == null) {
            this.AQ.caption = T.loadGame + " " + SaveRms.userLast;
            var7.addElement(this.AQ);
         }

         var7.addElement(this.AO);
         var7.addElement(this.AN);
         if (!GameCanvas.isTouch) {
            var7.addElement(this.AP);
         }

         var7.addElement(AE);
         this.AS.caption = T.on + T.QW;
         if (!GameCanvas.isOffBg) {
            this.AS.caption = T.off + T.QW;
         }

         var7.addElement(this.AS);
         if (GameCanvas.isTouch) {
            this.AT.caption = T.on + T.SO;
            if (GameCanvas.isTaiTho) {
               this.AT.caption = T.off + T.SO;
            }

            var7.addElement(this.AT);
         }

         var7.addElement(this.AM);
         if (GameScreen.IV) {
            var7.addElement(new iCommand("NHAP IP", 8, this));
            var7.addElement(new iCommand("VŨ ĐỆ 146 port 23", 17, this));
         }

         if (GameCanvas.hostServer.length() > 0) {
            var7.addElement(new iCommand("XOA IP", 14, this));
         }

         GameCanvas.menu.startAt(var7, 0, T.AU);
         break;
      case 2:
         if (this.AA == 0) {
            this.AA = 1;
         } else {
            this.AA = 0;
         }

         this.AH();
         break;
      case 3:
         if (this.AC.getText().trim().length() > 0 && this.AD.getText().trim().length() > 0) {
            GameCanvas.connectServerP();
            GlobalService.getInstance().AB(this.AC.getText(), this.AD.getText());
            GameCanvas.clearAll();
         } else {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.checkRegister1);
         }
         break;
      case 4:
         GameCanvas.Start_Normal_DiaLog(T.FO, new iCommand(T.AI, 5, this), true);
         break;
      case 5:
         GameMidlet var8 = GameMidlet.AD;
         GameMidlet.AA();
         break;
      case 6:
         GameCanvas.lowGraphic = !GameCanvas.lowGraphic;
         this.AN.caption = T.on + T.HW;
         if (GameCanvas.lowGraphic) {
            this.AN.caption = T.off + T.HW;
         }

         try {
            CRes.saveRMS("SUB_LOWGRAPHIC", new byte[]{(byte)(GameCanvas.lowGraphic ? 1 : 0)});
         } catch (Exception var6) {
         }

         LoadImageStatic.LoadLowGraphic();
         break;
      case 7:
         ListChar_Screen.IndexCharSelected = -1;
         GameCanvas.loginScr.AA(true, (byte)1, "", "");
         break;
      case 8:
         this.AZ = new InputDialog();
         this.AZ.AA("NHAP IP", new iCommand("OK", 9, this), false, "IP-PORT");
         GameCanvas.currentDialog = this.AZ;
         break;
      case 9:
         GameCanvas.hostServer = this.AZ.tfInput.getText().trim();
         saveIP_New();
         break;
      case 10:
         (var7 = new mVector()).addElement(new iCommand(T.LC, 11, 0, this));
         var7.addElement(new iCommand(T.LD, 11, 1, this));
         var7.addElement(new iCommand(T.RL, 11, 4, this));
         var7.addElement(new iCommand(T.LE, 11, 2, this));
         var7.addElement(new iCommand(T.LF, 11, 3, this));
         GameCanvas.menu.startAt(var7, 2, T.LB);
         break;
      case 11:
         if (GameCanvas.language == 1) {
            if (var2 != 0 && var2 != 1 && var2 != 2 && var2 != 3 && var2 != 4 && var2 == 5) {
            }
         } else if (var2 != 0 && var2 != 1 && var2 != 2 && var2 != 3 && var2 != 4 && var2 == 5) {
         }
         break;
      case 12:
         GameCanvas.fristLoginScr.cmdBegin.AD();
         break;
      case 13:
         Class_AS var3;
         (var3 = new Class_AS()).AA();
         GameCanvas.Start_Current_Dialog((MainDialog)var3);
         break;
      case 14:
         GameCanvas.hostServer = "";
         GameMidlet.AC("Main_IPNEW");
         break;
      case 15:
         GameCanvas.Start_Normal_DiaLog(T.ML, new iCommand(T.del, 16, this), true);
         break;
      case 16:
         AB = false;
         GameMidlet.AB();
         GameCanvas.end_Dialog();
         this.AL();
         break;
      case 17:
        if (!UpdateServer.serverHosts.isEmpty()) {
            GameCanvas.IndexServer = 0; // chọn server đầu trong list
            GameCanvas.hostServer = UpdateServer.getHost(0) + "-" + UpdateServer.getPort(0);
            saveIP_New();
        }
        break;

      case 18:
         GameCanvas.isOffBg = !GameCanvas.isOffBg;

         try {
            CRes.saveRMS("SUB_OFFBG", new byte[]{(byte)(GameCanvas.isOffBg ? 1 : 0)});
         } catch (Exception var5) {
         }
         break;
      case 19:
         GameCanvas.isTaiTho = !GameCanvas.isTaiTho;

         try {
            CRes.saveRMS("SUB_TAITHO", new byte[]{(byte)(GameCanvas.isTaiTho ? 1 : 0)});
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         Interface_Game var10000 = GameScreen.interfaceGame;
         Interface_Game.setPosMenu_TaiTho();
      case 20:
      case 21:
      }

      super.commandPointer(var1, var2);
   }

   private static void saveIP_New() {
      if (GameCanvas.hostServer.length() == 0) {
         GameMidlet.AC("Main_IPNEW");
      } else {
         String[] var0 = mFont.split(GameCanvas.hostServer, "-");
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog("IP:" + var0[0] + "\nPORT:" + var0[1]);
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var1 = new DataOutputStream(var3);

         try {
            var1.writeUTF(GameCanvas.hostServer);
            GameMidlet.AA("Main_IPNEW", var3.toByteArray());
            var1.close();
         } catch (Exception var2) {
         }
      }
   }

   public static void paintLogo(mGraphics g, int x) {
    if (AvMain.imgLg == null) {
        LoadImageStatic.loadImageLanguage();
    }

    g.drawRegion((mImage) AvMain.imgLg, x, yPaintLogo, 3);

    // Chỉ hiển thị icon nếu ngôn ngữ là 0, không lowGraphic, và có fraIconServer
    if (GameCanvas.language == 0 
        && !GameCanvas.lowGraphic 
        && !GameCanvas.AQ() 
        && UpdateServer.serverFrameId.size() > GameCanvas.IndexServer) {

        int frameId = UpdateServer.serverFrameId.get(GameCanvas.IndexServer);
        if (frameId >= 0 && frameId < AvMain.fraIconServer.nFrame) {
            if (MotherCanvas.h >= 240) {
                AvMain.fraIconServer.drawFrame(frameId, x + 35, yPaintLogo + 20, 0, 3, g);
            } else {
                Interface_Game.fraBorderNoti.drawFrame(frameId + 1, x + 34, yPaintLogo + 17, 0, 3, g);
            }
        }
    }
   }

   public static void updateYPaintLogo(int y) {
      if (yPaintLogo > y) {
         if ((yPaintLogo -= 2) < y) {
            yPaintLogo = y;
            return;
         }
      } else if (yPaintLogo < y && (yPaintLogo += 2) > y) {
         yPaintLogo = y;
      }

   }

   public static void AB(mGraphics var0) {
      CRes.quickSort(vecOBJ);

      int var1;
      for(var1 = 0; var1 < vecOBJ.size(); ++var1) {
         MainObject var2;
         (var2 = (MainObject)vecOBJ.elementAt(var1)).paint(var0);
         var2.ySort = var2.y;
      }

      for(var1 = 0; var1 < vecEff.size(); ++var1) {
         ((MainEffect)vecEff.elementAt(var1)).paint(var0);
      }

   }

   public static void updateCharShow() {
      int k;
      for(k = 0; k < vecOBJ.size(); ++k) {
         MainObject var1;
         (var1 = (MainObject)vecOBJ.elementAt(k)).AO();
         if (var1.isRemove) {
            vecOBJ.removeElement(var1);
            --k;
         }
      }

      for(k = 0; k < vecEff.size(); ++k) {
         MainEffect mainEffect;
         (mainEffect = (MainEffect)vecEff.elementAt(k)).update();
         if (mainEffect.isStop) {
            vecEff.removeElement(mainEffect);
            --k;
         }
      }

      boolean addObjShow;
      if (CRes.random(20) == 0 && vecOBJ.size() < MotherCanvas.w / 80) {
         addObjShow = false;
      }

      if (isNewShow) {
         isNewShow = false;

         for(k = 0; k < 3; ++k) {
            addObjShow = true;
         }
      }

   }

   public final void update() {
      updateYPaintLogo(AU / 2);
      updateBanhLai();
      if (this.BF < this.BG) {
         this.BF += this.BH;
         if (this.BF > this.BG) {
            this.BF = this.BG;
            this.BH = 15;
         }

         if (this.BH < 100) {
            this.BH += 15;
            if (this.BH > 100) {
               this.BH = 100;
            }
         }
      }

      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AD();
      }

      this.AI.update();
      this.AC.update();
      this.AD.update();
      updateCharShow();
      if (MsgDialog.isAuroReconect && GameCanvas.gameTick % 100 == 0 && (GameCanvas.currentDialog == null || GameCanvas.currentDialog.type != 9)) {
         String var1 = T.AJ;
         if (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.length() > 10) {
            var1 = GameCanvas.infoDisConnect;
            GameCanvas.infoDisConnect = "";
         }

         mVector var2;
         (var2 = new mVector()).addElement(GameScreen.CA);
         var2.addElement(GameCanvas.gameScr.BI);
         GameCanvas.AB(var1, var2, false);
      }

   }

   public static void updateBanhLai() {
      int var0 = CRes.random(20);
      if (BJ < 40 && ++BJ == 40) {
         if (AH != 1) {
            AH = 1;
         }

         BJ = 0;
      }

      if (var0 == 0) {
         if (AH != 0 && AH != 2) {
            if (CRes.random(2) == 0) {
               AH = 0;
            } else {
               AH = 2;
            }
         } else {
            AH = 1;
         }

         BJ = 0;
      }

   }

   public final void handleKeyPress() {
      if (this.AW == -2) {
         if (GameCanvas.AL[5]) {
            this.AW = 2;
            GameCanvas.AA(5);
            GameCanvas.AB(5);
         }
      } else if (this.AW == 2 && GameCanvas.AL[5]) {
         this.AW = -2;
         GameCanvas.AA(5);
         GameCanvas.AB(5);
      }

      if (GameCanvas.AL[8]) {
         if (this.AW == 2) {
            if (GameCanvas.IndexServer < this.AX - 1) {
               this.AE(GameCanvas.IndexServer + 1);
               // ensure selected visible
               clampServerScroll();
               // if selected beyond visible window, adjust
               int visibleRows = Math.min(this.AX, MAX_VISIBLE_ROWS);
               if (GameCanvas.IndexServer >= this.serverScrollIndex + visibleRows) {
                   this.serverScrollIndex = GameCanvas.IndexServer - visibleRows + 1;
               }
            }
         } else if (this.AW == -2) {
            if (GameCanvas.isKeyPressed()) {
               this.AC.AA(true);
            }

            this.AD.AA(false);
            this.AW = 0;
         } else if (this.AC.AF()) {
            this.AC.AA(false);
            if (GameCanvas.isKeyPressed()) {
               this.AD.AA(true);
            }
         } else if (this.AD.AF()) {
            this.AC.AA(false);
            this.AD.AA(false);
            this.AW = -2;
         }

         GameCanvas.AB(8);
         this.AN();
      } else if (GameCanvas.AL[2]) {
         if (this.AW == 2) {
            if (GameCanvas.IndexServer > 0) {
               this.AE(GameCanvas.IndexServer - 1);
               clampServerScroll();
               int visibleRows = Math.min(this.AX, MAX_VISIBLE_ROWS);
               if (GameCanvas.IndexServer < this.serverScrollIndex) {
                   this.serverScrollIndex = GameCanvas.IndexServer;
               }
            }
         } else if (this.AW == -2) {
            this.AC.AA(false);
            if (GameCanvas.isKeyPressed()) {
               this.AD.AA(true);
            }

            this.AW = 0;
         } else if (this.AC.AF()) {
            this.AC.AA(false);
            this.AD.AA(false);
            this.AW = -2;
         } else if (this.AD.AF()) {
            if (GameCanvas.isKeyPressed()) {
               this.AC.AA(true);
            }

            this.AD.AA(false);
         }

         GameCanvas.AB(2);
         this.AN();
      }

      super.handleKeyPress();
      this.AS();
   }

   private void AE(int var1) {
    if (var1 >= 0 && var1 < UpdateServer.serverHosts.size()) {
        GameCanvas.IndexServer = var1;
        GameCanvas.hostServer = UpdateServer.getHost(var1);
        GameCanvas.portServer = UpdateServer.getPort(var1);
        if (Session_ME.getInstance().AB()) {
            Session_ME.getInstance().close();
        }
        // khi chọn server bằng AE, đảm bảo scroll index cập nhật để hiện server được chọn
        clampServerScroll();
        int visibleRows = Math.min(this.AX, MAX_VISIBLE_ROWS);
        if (GameCanvas.IndexServer < this.serverScrollIndex) {
            this.serverScrollIndex = GameCanvas.IndexServer;
        } else if (GameCanvas.IndexServer >= this.serverScrollIndex + visibleRows) {
            this.serverScrollIndex = GameCanvas.IndexServer - visibleRows + 1;
        }
    }
   }


   public final void AA(boolean var1, byte var2, String var3, String var4) {
      GameCanvas.connectServerP();
      if (var1) {
         this.AL();
      }

      if (!AB) {
         GlobalService.getInstance().AB();
         GameCanvas.AB(T.ED, false);
      } else {
         GlobalService.getInstance().Login(var3, var4, var2);
         GameCanvas.AB(T.GH, true);
         if (var2 >= 0) {
            Player.SO = 3;
         }

      }
   }

   public final void AA(int var1) {
      if (this.AC.AF()) {
         this.AC.AD(var1);
      } else {
         if (this.AD.AF()) {
            this.AD.AD(var1);
         }

      }
   }

   private void AL() {
      GlobalService.getInstance().get_DATA((byte)4);
      GlobalService.getInstance().get_DATA((byte)10);
      GlobalService.getInstance().get_DATA((byte)8);
      GlobalService.getInstance().get_DATA((byte)21);
      GlobalService.getInstance().get_DATA((byte)30);
      GlobalService.getInstance().AR((byte)1);
      GlobalService.getInstance().AR((byte)2);
      GlobalService.getInstance().get_DATA((byte)31);
      String var1 = System.getProperty("microedition.platform");
      byte[] var3;
      if ((var3 = GameMidlet.AA(var1 + T.YB)) != null) {
         try {
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            DataInputStream var5;
            if ((GameCanvas.percentUPCV = (var5 = new DataInputStream(var4)).readInt()) == 0) {
               GameCanvas.CY = 0;
            } else {
               GameCanvas.CY = 300;
            }

            var5.close();
         } catch (Exception var2) {
         }
      }

      if (!GlobalService.AC) {
         GlobalService.getInstance().get_DATA((byte)26);
      }

      if (!GlobalService.AB) {
         GlobalService.getInstance().get_DATA((byte)11);
      }

      if (LoadMap.mSea == null) {
         GlobalService.getInstance().get_DATA((byte)13);
      }

      if (ScreenUpgrade.mItemUpgrade == null) {
         GlobalService.getInstance().get_DATA((byte)19);
      }

      if (GameCanvas.clockServer == 0L) {
         GlobalService.getInstance().get_DATA((byte)17);
      }

   }

   private void AN() {
      if (!GameCanvas.isTouch) {
         if (this.AC.AF()) {
            super.DB = this.AC.AG;
            return;
         }

         if (this.AD.AF()) {
            super.DB = this.AD.AG;
         }
      }

   }

   public static void AA(short var0, int var1, int var2, int var3, byte var4, MainObject var5) {
      Effect_End var6 = new Effect_End(var0, (byte)var1, var2, var3, var4, var5);
      vecEff.addElement(var6);
   }
}
