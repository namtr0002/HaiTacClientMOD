public final class MsgOtherCharInfo extends MsgDialog {
   private MainObject AB;
   private int AC = 120;
   private int BB;
   private MainItem BD;
   private int BE;
   private int BF;
   private int BG;
   private int BH = 0;
   private boolean BI = false;
   private boolean BJ = false;
   public static Class_DS AA = null;
   private iCommand BK;
   private int BL = 0;
   private int BM = 0;

   // Màn hình to / nhỏ
   public boolean isBigScreen = false;

   // Cuộn trang bị lên xuống mượt mà trên màn hình nhỏ
   public static final int EQUIP_PAGE_H = 104;
   private int equipScrollY = 0;
   private int equipToY = 0;
   private int equipSubTab = 0; // 0: Trang bị, 1: Thần trang
   private boolean isDraggingEquip = false;
   private int equipDragStartY = 0;
   private int equipDragStartScrollY = 0;

   private iCommand cmdSwitchPage;
   private int tabBtnW = 60;
   private int tabBtnH = 14;
   private int tabBtnY = 0;
   private int viewClipY = 0;
   private int viewClipH = 104;
   private int leftSlotX = 0;
   private int rightSlotX = 0;
   private int slotGapY = 25;

   // Tọa độ 2 panel cho màn hình lớn
   private int panel0X = 0;
   private int panel1X = 0;
   private int panelW = 155;
   private int panelH = 114;
   private int panelY = 0;

   public MsgOtherCharInfo(MainObject var1) {
      this.AB = var1;
      this.BK = null;
      if (var1 != null) {
         super.cmdList.removeAllElements();
         super.AG = new iCommand(T.close, 1, this);
         super.AG = AvMain.AA(super.AG, 2);
         if (AA != null && AA.AC.compareTo(this.AB.name) == 0) {
            this.BK = new iCommand(T.chapnhan, 2, this);
            this.BK = AvMain.AA(this.BK, 0);
            this.BK = AvMain.AA(this.BK, 1);
            super.DA = this.BK;
         }

         super.DB = super.AG;
         this.isBigScreen = false;

         if (GameCanvas.isSmallScreen || GameCanvas.isCompactMode()) {
            super.wDia = 158;
            super.wItem = 23;
         } else {
            super.wDia = 186;
            super.wItem = 26;
         }
         if (super.wDia > MotherCanvas.w - 10) {
            super.wDia = MotherCanvas.w - 10;
         }
         super.hDia = (this.AB.clan != null ? 186 : 172);

         this.AT = super.wDia;
         this.AS = this.AT;
         this.BG = 0;
         if (GameScreen.vecPlayers != null) {
            for(int var4 = 0; var4 < GameScreen.vecPlayers.size(); ++var4) {
               MainObject var7;
               if ((var7 = (MainObject)GameScreen.vecPlayers.elementAt(var4)) != null && !var7.isRemove && var7.typeObject == 0 && var7.name.compareTo(this.AB.name) == 0) {
                  this.AB.thanhtichLv = var7.thanhtichLv;
                  this.AB.thanhtichPvP = var7.thanhtichPvP;
                  break;
               }
            }
         }
         if (this.AB.thanhtichLv >= 0) {
            this.BG += 14;
         }
         if (this.AB.thanhtichPvP >= 0) {
            this.BG += 14;
         }
         this.BJ = true;

         this.BH = this.BG;
         if (var1.hOne > 52) {
            this.BH += var1.hOne - 52;
         }

         int maxH = MotherCanvas.h - 28 - GameCanvas.hCommand;
         if (super.hDia > maxH) {
            super.hDia = maxH;
         }
         if (super.hDia < 148) {
            super.hDia = 148;
         }

         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.hh - super.hDia / 2;
         if (super.AY < 22) {
            super.AY = 22;
         }

         this.tabBtnW = (super.wDia - 24) / 2;
         this.tabBtnH = 14;

         this.cmdSwitchPage = new iCommand("Thần trang", 3, this);
         this.cmdSwitchPage = AvMain.AA(this.cmdSwitchPage, 0);

         if (this.BK != null) {
            super.DA = this.BK;
         } else if (!GameCanvas.isTouch && !this.isBigScreen) {
            super.DA = this.cmdSwitchPage;
         }

         if (GameCanvas.isTouch) {
            super.idSelect = -1;
            super.AG.setPos(super.AX + super.AT / 2 + this.AC / 2, super.AY - 20 + 8, MainTab.fraCloseTab, "");
            super.cmdList.addElement(super.AG);
            if (this.BK != null) {
               this.BK = AvMain.AA(this.BK, 0);
               super.cmdList.addElement(this.BK);
            }
         } else {
            super.idSelect = 0;
            this.BI = false;
            this.BD = (MainItem)var1.hashEquip.get("0");
         }

         super.backCMD = super.AG;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         AA = null;
         break;
      case 1:
         GameCanvas.end_Dialog();
         AA = null;
         break;
      case 2:
         if (AA != null) {
            GlobalService.getInstance().AA((byte)1, (short)((short)AA.AG), (byte)0);
            if (GameCanvas.eventScr.vecPlayer != null) {
               for(var1 = 0; var1 < GameCanvas.eventScr.vecPlayer.size(); ++var1) {
                  InfoMemList var3;
                  if ((var3 = (InfoMemList)GameCanvas.eventScr.vecPlayer.elementAt(var1)) == AA) {
                     GameCanvas.eventScr.vecPlayer.removeElement(var3);
                     return;
                  }
               }
            }
         }
         return;
      case 3:
         if (!this.isBigScreen) {
            this.equipToY = (this.equipToY == 0) ? EQUIP_PAGE_H : 0;
            this.cmdSwitchPage.caption = (this.equipToY == 0) ? "Thần trang" : "Trang bị";
            this.BI = false;
            this.BB = 0;
            if (super.idSelect >= 0 && super.idSelect < 16) {
               this.BD = (MainItem)this.AB.hashEquip.get("" + super.idSelect);
            } else {
               this.BD = null;
            }
         }
         return;
      }

      super.commandPointer(var1, var2);
   }

   private void paintEquipSlot(mGraphics g, int sx, int sy, int size, int equipType) {
      boolean isSel = (super.idSelect == equipType);
      AvMain.paintRect(g, sx, sy, size, size, (byte)(isSel ? 1 : 0), 3);

      MainItem item = (MainItem)this.AB.hashEquip.get("" + equipType);
      if (item != null) {
         item.AC(g, sx + size / 2, sy + size / 2, size - 2);
         item.AB(g, sx + size / 2, sy + size / 2, size, 1);
      } else if (equipType < 8) {
         int frameIdx = equipType % 8;
         AvMain.paintEquipSilhouette(g, frameIdx, sx + size / 2, sy + size / 2);
      }

      if (isSel) {
         g.setColor(0xFFFF00);
         g.drawRect(sx - 1, sy - 1, size + 1, size + 1);
         if (AvMain.imgNenfocus != null) {
            g.drawRegion(AvMain.imgNenfocus, 2, 2, size, size, 0, sx, sy, 0);
         }
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var3 = super.AX + super.wDia / 2;
      this.AE(var1, super.AX - 5, super.AY - 32, super.AT + 10, super.hDia + 44, super.AT + 10);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + super.wDia / 2 - this.AC / 2, super.AY - 20, this.AC, 16, 4, 4);
      AvMain.FontBorderColorAuto(var1, this.AB.name, super.AX + super.AT / 2, super.AY - 18, 2, (int)6, (int)5, this.AC - 6);
      var1.setClip_(MotherCanvas.hw - super.AT / 2, super.AY, super.AT, super.hDia);
      mGraphics.AC();
      mGraphics.AD();

      int yCur = super.AY + 4;
      MainImage var4;
      if (this.AB.clan != null && (var4 = Potion.getIconClan(this.AB.clan.idIcon)) != null && var4.img != null) {
         int var5 = -mFont.tahoma_7b_black.getWidth(this.AB.clan.name) / 2;
         if (var4.frame == -1) {
            var4.set_Frame();
         }

         if (var4.frame <= 1) {
            var1.drawRegion((mImage)var4.img, super.AX + super.wDia / 2 + var5, yCur, 3);
         } else {
            byte var6;
            if (this.BM >= var4.frame - 1) {
               var6 = 15;
            } else {
               var6 = 3;
            }

            if (CRes.abs(GameCanvas.gameTick - this.BL) > var6) {
               ++this.BM;
               if (this.BM >= var4.frame) {
                  this.BM = 0;
               }

               this.BL = GameCanvas.gameTick;
            }

            var1.drawRegion(var4.img, 0, this.BM * var4.AB, var4.AB, var4.AB, 0, super.AX + super.wDia / 2 + var5, yCur, 3);
         }

         mFont.tahoma_7b_black.drawString(var1, this.AB.clan.name, super.AX + super.wDia / 2 + 9, yCur - 6, 2);
         yCur += 14;
      } else {
         yCur += 2;
      }

      // Thanh HP & MP
      mImage var10 = (this.AB.Lv >= 100) ? Interface_Game.imgIconMPHP2 : Interface_Game.imgIconMPHP;
      var1.drawRegion((mImage)var10, var3 - 47 + 7, yCur, 0);
      Interface_Game.AA(var1, (byte)1, this.AB.Hp, this.AB.maxHp, var3 - 47 + 18, yCur, 0, 9, 66, 0, false, 0, false, this.AB.MA);
      yCur += 11;
      Interface_Game.AA(var1, (byte)2, this.AB.Mp, this.AB.maxMp, var3 - 47 + 18, yCur, 0, 9, 66, 0, false, 0, false, 0);
      yCur += 10;

      // Cấp độ & % Kinh nghiệm
      int var11;
      if (this.AB.Lv >= 100) {
         mFont.tahoma_7_black.drawString(var1, this.AB.LvThongThao + " + " + this.AB.KS / 10 + "," + this.AB.KS % 10 + "%", var3 - 47 + 20, yCur, 0);
         yCur += 10;
         var11 = this.AB.KS / 10 * 70 / 100;
      } else {
         mFont.tahoma_7_black.drawString(var1, this.AB.Lv + " + " + this.AB.percentLv / 10 + "," + this.AB.percentLv % 10 + "%", var3 - 47 + 20, yCur, 0);
         yCur += 10;
         var11 = this.AB.percentLv / 10 * 70 / 100;
      }

      var1.setColor(-15519213);
      var1.fillRect(var3 - 47 + 18, yCur, 65, 2);
      if (var11 > 0) {
         var1.setColor(-13263058);
         var1.fillRect(var3 - 47 + 18, yCur, var11, 2);
      }
      for(int var8 = 1; var8 < 5; ++var8) {
         var1.setColor(-1);
         var1.fillRect(var3 - 47 + 18 + var8 * 13, yCur, 1, 2);
      }
      yCur += 6;

      // Chuẩn bị đồ thần trang overrides
      short cw = (short)-2, ch = (short)-2, cb = (short)-2, cl = (short)-2;
      MainItem eq8 = (MainItem)this.AB.hashEquip.get("8");
      if (eq8 != null) cw = (eq8.idPart > 0) ? eq8.idPart : ((eq8.ID == 2603) ? (short)184 : (short)-2);
      MainItem eq9 = (MainItem)this.AB.hashEquip.get("9");
      if (eq9 != null) ch = (eq9.idPart > 0) ? eq9.idPart : ((eq9.ID == 2600) ? (short)222 : (short)-2);
      MainItem eq11 = (MainItem)this.AB.hashEquip.get("11");
      if (eq11 != null) cb = (eq11.idPart > 0) ? eq11.idPart : ((eq11.ID == 2602) ? (short)223 : (short)-2);
      MainItem eq13 = (MainItem)this.AB.hashEquip.get("13");
      if (eq13 != null) cl = (eq13.idPart > 0) ? eq13.idPart : ((eq13.ID == 2601) ? (short)224 : (short)-2);

      if (this.isBigScreen) {
         // =================== GIAO DIỆN MÀN HÌNH LỚN 2 CỘT SONG SONG ===================
         this.panelW = (super.wDia - 24) / 2;
         this.panelH = super.hDia - (yCur - super.AY) - 6;
         if (this.panelH < 114) this.panelH = 114;
         this.panel0X = super.AX + 8;
         this.panel1X = this.panel0X + this.panelW + 8;
         this.panelY = yCur;

         int pSlotGap = 24;
         int pSlotSize = super.wItem;

         // --- PANEL 0: TRANG BỊ THƯỜNG ---
         AvMain.paintRect(var1, this.panel0X, this.panelY, this.panelW, this.panelH, (byte)0, 3);
         AvMain.paintRect(var1, this.panel0X + 2, this.panelY + 2, this.panelW - 4, 13, (byte)0, 1);
         mFont.tahoma_7b_yellow.drawString(var1, "Trang Bị", this.panel0X + this.panelW / 2, this.panelY + 3, 2);

         int p0LeftX = this.panel0X + 6;
         int p0RightX = this.panel0X + this.panelW - pSlotSize - 6;
         for (int i = 0; i < 4; i++) {
            paintEquipSlot(var1, p0LeftX, this.panelY + 17 + i * pSlotGap, pSlotSize, i * 2);
            paintEquipSlot(var1, p0RightX, this.panelY + 17 + i * pSlotGap, pSlotSize, i * 2 + 1);
         }

         int char0CenterX = this.panel0X + this.panelW / 2;
         int char0CenterY = this.panelY + 17 + 76; // Offset hạ xuống chuẩn không bị che
         if (MainObject.imgShadow != null) {
            var1.drawImage((mImage)MainObject.imgShadow, char0CenterX, char0CenterY + 4, 3);
         }
         this.AB.paintThanhTich(var1, this.panelY + 25, char0CenterX);
         this.AB.AA(var1, char0CenterX, char0CenterY, true);

         // --- PANEL 1: THẦN TRANG ---
         AvMain.paintRect(var1, this.panel1X, this.panelY, this.panelW, this.panelH, (byte)0, 3);
         AvMain.paintRect(var1, this.panel1X + 2, this.panelY + 2, this.panelW - 4, 13, (byte)1, 0);
         mFont.tahoma_7b_yellow.drawString(var1, "Thần Trang", this.panel1X + this.panelW / 2, this.panelY + 3, 2);

         int p1LeftX = this.panel1X + 6;
         int p1RightX = this.panel1X + this.panelW - pSlotSize - 6;
         for (int i = 0; i < 4; i++) {
            paintEquipSlot(var1, p1LeftX, this.panelY + 17 + i * pSlotGap, pSlotSize, 8 + i * 2);
            paintEquipSlot(var1, p1RightX, this.panelY + 17 + i * pSlotGap, pSlotSize, 8 + i * 2 + 1);
         }

         int char1CenterX = this.panel1X + this.panelW / 2;
         int char1CenterY = this.panelY + 17 + 76;
         if (MainObject.imgShadow != null) {
            var1.drawImage((mImage)MainObject.imgShadow, char1CenterX, char1CenterY + 4, 3);
         }
         this.AB.paintThanhTich(var1, this.panelY + 25, char1CenterX);
         this.AB.paintCharShowWithOverride(var1, char1CenterX, char1CenterY, true, cw, ch, cb, cl);

      } else {
         // =================== GIAO DIỆN MÀN HÌNH NHỎ / CUỘN MƯỢT MÀ ===================
         this.tabBtnY = yCur;
         int btn0_x = super.AX + 8;
         int btn1_x = super.AX + 12 + this.tabBtnW;
         boolean isTab0 = (this.equipSubTab == 0);
         boolean isTab1 = (this.equipSubTab == 1);

         AvMain.paintRect(var1, btn0_x, this.tabBtnY, this.tabBtnW, this.tabBtnH, (byte)(isTab0 ? 1 : 0), (isTab0 ? 0 : 1));
         if (isTab0 && AvMain.imgNenfocus != null) {
            var1.drawRegion(AvMain.imgNenfocus, 2, 2, this.tabBtnW, this.tabBtnH, 0, btn0_x, this.tabBtnY, 0);
         }
         if (isTab0) {
            mFont.tahoma_7b_yellow.drawString(var1, "Trang Bị", btn0_x + this.tabBtnW / 2, this.tabBtnY + 1, 2);
         } else {
            mFont.tahoma_7_black.drawString(var1, "Trang Bị", btn0_x + this.tabBtnW / 2, this.tabBtnY + 1, 2);
         }

         AvMain.paintRect(var1, btn1_x, this.tabBtnY, this.tabBtnW, this.tabBtnH, (byte)(isTab1 ? 1 : 0), (isTab1 ? 0 : 1));
         if (isTab1 && AvMain.imgNenfocus != null) {
            var1.drawRegion(AvMain.imgNenfocus, 2, 2, this.tabBtnW, this.tabBtnH, 0, btn1_x, this.tabBtnY, 0);
         }
         if (isTab1) {
            mFont.tahoma_7b_yellow.drawString(var1, "Thần Trang", btn1_x + this.tabBtnW / 2, this.tabBtnY + 1, 2);
         } else {
            mFont.tahoma_7_black.drawString(var1, "Thần Trang", btn1_x + this.tabBtnW / 2, this.tabBtnY + 1, 2);
         }
         yCur += this.tabBtnH + 4;

         // Viewport Cuộn Trang Bị Lên Xuống
         int equipAreaX = super.AX + 4;
         int equipAreaW = super.wDia - 8;
         this.viewClipY = yCur;
         this.viewClipH = EQUIP_PAGE_H;
         if (this.viewClipY + this.viewClipH > super.AY + super.hDia - 4) {
            this.viewClipH = super.AY + super.hDia - 4 - this.viewClipY;
         }
         if (this.viewClipH < 60) this.viewClipH = 60;

         this.leftSlotX = equipAreaX + 6;
         this.rightSlotX = equipAreaX + equipAreaW - super.wItem - 6;
         int charCenterX = super.AX + super.wDia / 2;

         var1.setClip(equipAreaX, this.viewClipY, equipAreaW, this.viewClipH);
         var1.translate(0, -this.equipScrollY);

         // --- PAGE 0: TRANG BỊ THƯỜNG (0..7) ---
         int page0Y = this.viewClipY;
         for (int i = 0; i < 4; i++) {
            paintEquipSlot(var1, this.leftSlotX, page0Y + 2 + i * this.slotGapY, super.wItem, i * 2);
            paintEquipSlot(var1, this.rightSlotX, page0Y + 2 + i * this.slotGapY, super.wItem, i * 2 + 1);
         }
         int charCenterY0 = page0Y + 86; // Offset hạ xuống chuẩn
         if (MainObject.imgShadow != null) {
            var1.drawImage((mImage)MainObject.imgShadow, charCenterX, charCenterY0 + 4, 3);
         }
         this.AB.paintThanhTich(var1, page0Y + 14, charCenterX);
         this.AB.AA(var1, charCenterX, charCenterY0, true);

         // --- PAGE 1: THẦN TRANG (8..15) ---
         int page1Y = this.viewClipY + EQUIP_PAGE_H;
         for (int i = 0; i < 4; i++) {
            paintEquipSlot(var1, this.leftSlotX, page1Y + 2 + i * this.slotGapY, super.wItem, 8 + i * 2);
            paintEquipSlot(var1, this.rightSlotX, page1Y + 2 + i * this.slotGapY, super.wItem, 8 + i * 2 + 1);
         }
         int charCenterY1 = page1Y + 86;
         if (MainObject.imgShadow != null) {
            var1.drawImage((mImage)MainObject.imgShadow, charCenterX, charCenterY1 + 4, 3);
         }
         this.AB.paintThanhTich(var1, page1Y + 14, charCenterX);
         this.AB.paintCharShowWithOverride(var1, charCenterX, charCenterY1, true, cw, ch, cb, cl);

         var1.translate(0, this.equipScrollY);
         var1.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(int var8 = 0; var8 < super.cmdList.size(); ++var8) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var8)).paint(var1, var9.xCmd, var9.yCmd);
         }
      }

      super.AD(var1);
      if (this.BI && this.BD != null) {
         MainTab.AA(var1, this.BD, (mVector)null, (byte)0, this.BE, this.BF, this.BD.BS, this.BD.BT, false, this.AB, 0);
      }
   }

   public final void update() {
      if (this.isClose) {
         this.closeDialog();
         return;
      }

      // Smooth scroll animation cho chế độ compact
      if (!this.isBigScreen) {
         if (!this.isDraggingEquip && this.equipScrollY != this.equipToY) {
            int d = (this.equipToY - this.equipScrollY) / 3;
            if (d == 0) d = (this.equipToY > this.equipScrollY) ? 1 : -1;
            this.equipScrollY += d;
            if (CRes.abs(this.equipToY - this.equipScrollY) < 2) {
               this.equipScrollY = this.equipToY;
            }
         }
         this.equipSubTab = (this.equipScrollY > EQUIP_PAGE_H / 2) ? 1 : 0;
      }

      if (this.BD != null && !this.BI) {
         ++this.BB;
         if (this.BB >= 10 && super.idSelect >= 0 && super.idSelect < 16) {
            this.BI = true;
            calcAndSetPosInfo();
         }
      }

      this.updateAnimation();
      this.handleKeyPress();
      this.updatePointer();
   }

   private void calcAndSetPosInfo() {
      if (this.BD == null || super.idSelect < 0 || super.idSelect >= 16) return;
      if (this.isBigScreen) {
         int pSlotGap = 24;
         if (super.idSelect < 8) {
            int row = super.idSelect / 2;
            int col = super.idSelect % 2;
            int sx = (col == 0) ? (this.panel0X + 6) : (this.panel0X + this.panelW - super.wItem - 6);
            int sy = this.panelY + 17 + row * pSlotGap;
            setPosInfo(this.BD, sx + super.wItem / 2, sy + super.wItem);
         } else {
            int idx = super.idSelect - 8;
            int row = idx / 2;
            int col = idx % 2;
            int sx = (col == 0) ? (this.panel1X + 6) : (this.panel1X + this.panelW - super.wItem - 6);
            int sy = this.panelY + 17 + row * pSlotGap;
            setPosInfo(this.BD, sx + super.wItem / 2, sy + super.wItem);
         }
      } else {
         int page = super.idSelect / 8;
         int idx = super.idSelect % 8;
         int row = idx / 2;
         int col = idx % 2;
         int sx = (col == 0) ? this.leftSlotX : this.rightSlotX;
         int sy = this.viewClipY + page * EQUIP_PAGE_H + 2 + row * this.slotGapY - this.equipScrollY;
         setPosInfo(this.BD, sx + super.wItem / 2, sy + super.wItem);
      }
   }

   public final void handleKeyPress() {
      boolean moved = false;
      if (super.idSelect == -1 && (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(1) || GameCanvas.isKeyPressed(3) || GameCanvas.isKeyPressed(5))) {
         super.idSelect = 0;
         GameCanvas.AH();
         moved = true;
      }

      if (!this.isBigScreen && (GameCanvas.keyMyHold[10] || GameCanvas.keyMyHold[11])) {
         GameCanvas.clearKeyHold(10);
         GameCanvas.clearKeyHold(11);
         this.equipToY = (this.equipToY == 0) ? EQUIP_PAGE_H : 0;
         this.cmdSwitchPage.caption = (this.equipToY == 0) ? "Thần trang" : "Trang bị";
         moved = true;
      }

      if (this.isBigScreen) {
         // Điều hướng 4 cột trên màn hình to (0..1: Trang bị, 8..9: Thần trang)
         if (GameCanvas.isKeyPressed(0)) { // Left
            if (super.idSelect >= 8) {
               if (super.idSelect % 2 == 1) {
                  super.idSelect--;
               } else {
                  super.idSelect = (super.idSelect - 8) + 1; // Nhảy sang cột phải của Trang Bị
               }
            } else {
               if (super.idSelect % 2 == 1) {
                  super.idSelect--;
               } else {
                  super.idSelect = (super.idSelect + 8) + 1; // Vòng sang cột phải của Thần Trang
               }
            }
            GameCanvas.ClearkeyMove(0);
            moved = true;
         } else if (GameCanvas.isKeyPressed(2)) { // Right
            if (super.idSelect < 8) {
               if (super.idSelect % 2 == 0) {
                  super.idSelect++;
               } else {
                  super.idSelect = (super.idSelect - 1) + 8; // Nhảy sang cột trái của Thần Trang
               }
            } else {
               if (super.idSelect % 2 == 0) {
                  super.idSelect++;
               } else {
                  super.idSelect = (super.idSelect - 8) - 1; // Vòng về cột trái của Trang Bị
               }
            }
            GameCanvas.ClearkeyMove(2);
            moved = true;
         } else if (GameCanvas.isKeyPressed(1)) { // Up
            if (super.idSelect < 8) {
               if (super.idSelect >= 2) super.idSelect -= 2;
               else super.idSelect += 6; // Wrap row
            } else {
               if (super.idSelect >= 10) super.idSelect -= 2;
               else super.idSelect += 6; // Wrap row
            }
            GameCanvas.ClearkeyMove(1);
            moved = true;
         } else if (GameCanvas.isKeyPressed(3)) { // Down
            if (super.idSelect < 8) {
               if (super.idSelect + 2 < 8) super.idSelect += 2;
               else super.idSelect -= 6; // Wrap row
            } else {
               if (super.idSelect + 2 < 16) super.idSelect += 2;
               else super.idSelect -= 6; // Wrap row
            }
            GameCanvas.ClearkeyMove(3);
            moved = true;
         }
      } else {
         // Điều hướng cuộn mượt mà trên màn hình nhỏ kiểu cũ (Xuống xem Thần Trang)
         if (GameCanvas.isKeyPressed(0)) { // Left
            if (super.idSelect % 2 == 1) {
               super.idSelect--;
            }
            GameCanvas.ClearkeyMove(0);
            moved = true;
         } else if (GameCanvas.isKeyPressed(2)) { // Right
            if (super.idSelect % 2 == 0 && super.idSelect + 1 < 16) {
               super.idSelect++;
            }
            GameCanvas.ClearkeyMove(2);
            moved = true;
         } else if (GameCanvas.isKeyPressed(1)) { // Up
            if (super.idSelect >= 2) {
               super.idSelect -= 2;
            } else {
               super.idSelect = 14 + super.idSelect % 2;
            }
            GameCanvas.ClearkeyMove(1);
            moved = true;
         } else if (GameCanvas.isKeyPressed(3)) { // Down (Xem thần trang bên dưới khi keypad xuống)
            if (super.idSelect + 2 < 16) {
               super.idSelect += 2;
            } else {
               super.idSelect = super.idSelect % 2;
            }
            GameCanvas.ClearkeyMove(3);
            moved = true;
         }
      }

      if (GameCanvas.isKeyPressed(5)) { // Select
         if (super.idSelect >= 0 && super.idSelect < 16) {
            this.BD = (MainItem)this.AB.hashEquip.get("" + super.idSelect);
            this.BI = (this.BD != null);
            if (this.BD != null) {
               calcAndSetPosInfo();
            }
         }
         GameCanvas.ClearkeyMove(5);
      }

      if (moved) {
         if (!this.isBigScreen) {
            if (super.idSelect < 8) {
               this.equipToY = 0;
               this.cmdSwitchPage.caption = "Thần trang";
            } else {
               this.equipToY = EQUIP_PAGE_H;
               this.cmdSwitchPage.caption = "Trang bị";
            }
         }

         if (super.idSelect >= 0 && super.idSelect < 16) {
            this.BI = false;
            this.BB = 0;
            this.BD = (MainItem)this.AB.hashEquip.get("" + super.idSelect);
         } else {
            super.idSelect = 0;
            this.BI = false;
            this.BB = 0;
            this.BD = (MainItem)this.AB.hashEquip.get("0");
         }
      }
   }

   public final void updatePointer() {
      if (super.cmdList != null) {
         for(int var1 = 0; var1 < super.cmdList.size(); ++var1) {
            ((iCommand)super.cmdList.elementAt(var1)).AE();
         }
      }

      if (this.isBigScreen) {
         // Touch / Click trên màn hình lớn
         if (GameCanvas.isPointerSelect) {
            int pSlotGap = 24;
            int pSlotSize = super.wItem;
            boolean outside = true;

            // Kiểm tra các ô Trang Bị (0..7)
            int p0LeftX = this.panel0X + 6;
            int p0RightX = this.panel0X + this.panelW - pSlotSize - 6;
            for (int i = 0; i < 4; i++) {
               int sy = this.panelY + 17 + i * pSlotGap;
               int sL = i * 2;
               int sR = i * 2 + 1;
               if (GameCanvas.AB(p0LeftX - 2, sy - 2, pSlotSize + 4, pSlotSize + 4)) {
                  outside = false;
                  selectSlotPointer(sL, p0LeftX + pSlotSize / 2, sy + pSlotSize);
                  break;
               }
               if (GameCanvas.AB(p0RightX - 2, sy - 2, pSlotSize + 4, pSlotSize + 4)) {
                  outside = false;
                  selectSlotPointer(sR, p0RightX + pSlotSize / 2, sy + pSlotSize);
                  break;
               }
            }

            // Kiểm tra các ô Thần Trang (8..15)
            if (outside) {
               int p1LeftX = this.panel1X + 6;
               int p1RightX = this.panel1X + this.panelW - pSlotSize - 6;
               for (int i = 0; i < 4; i++) {
                  int sy = this.panelY + 17 + i * pSlotGap;
                  int sL = 8 + i * 2;
                  int sR = 8 + i * 2 + 1;
                  if (GameCanvas.AB(p1LeftX - 2, sy - 2, pSlotSize + 4, pSlotSize + 4)) {
                     outside = false;
                     selectSlotPointer(sL, p1LeftX + pSlotSize / 2, sy + pSlotSize);
                     break;
                  }
                  if (GameCanvas.AB(p1RightX - 2, sy - 2, pSlotSize + 4, pSlotSize + 4)) {
                     outside = false;
                     selectSlotPointer(sR, p1RightX + pSlotSize / 2, sy + pSlotSize);
                     break;
                  }
               }
            }

            if (outside && !GameCanvas.isPoint(super.AX, super.AY, super.wDia, super.hDia)) {
               this.BD = null;
               this.BI = false;
               super.idSelect = -1;
            }
         }
      } else {
         // Touch / Drag trên màn hình nhỏ
         int equipAreaX = super.AX + 4;
         int equipAreaW = super.wDia - 8;

         // Xử lý vuốt cuộn lên/xuống (Touch Drag Scroll)
         if (GameCanvas.isPointerDown) {
            if (!this.isDraggingEquip && GameCanvas.isPoint(equipAreaX, this.viewClipY, equipAreaW, this.viewClipH)) {
               this.isDraggingEquip = true;
               this.equipDragStartY = GameCanvas.AZ;
               this.equipDragStartScrollY = this.equipScrollY;
            } else if (this.isDraggingEquip) {
               int dy = GameCanvas.AZ - this.equipDragStartY;
               this.equipScrollY = this.equipDragStartScrollY - dy;
               if (this.equipScrollY < -20) this.equipScrollY = -20 + (this.equipScrollY + 20) / 3;
               if (this.equipScrollY > EQUIP_PAGE_H + 20) this.equipScrollY = EQUIP_PAGE_H + 20 + (this.equipScrollY - EQUIP_PAGE_H - 20) / 3;
            }
         } else {
            if (this.isDraggingEquip) {
               this.isDraggingEquip = false;
               int dragDist = GameCanvas.AZ - this.equipDragStartY;
               if (dragDist < -16) {
                  this.equipToY = EQUIP_PAGE_H;
                  this.cmdSwitchPage.caption = "Trang bị";
               } else if (dragDist > 16) {
                  this.equipToY = 0;
                  this.cmdSwitchPage.caption = "Thần trang";
               } else {
                  this.equipToY = (this.equipScrollY > EQUIP_PAGE_H / 2) ? EQUIP_PAGE_H : 0;
                  this.cmdSwitchPage.caption = (this.equipToY == 0) ? "Thần trang" : "Trang bị";
               }
            }
         }

         if (GameCanvas.isPointerSelect) {
            int btn0_x = super.AX + 8;
            int btn1_x = super.AX + 12 + this.tabBtnW;

            // Chạm Tab 0: Trang bị
            if (GameCanvas.AB(btn0_x, this.tabBtnY - 2, this.tabBtnW, this.tabBtnH + 4)) {
               this.equipToY = 0;
               this.cmdSwitchPage.caption = "Thần trang";
               super.idSelect = -1;
               this.BD = null;
               this.BI = false;
               GameCanvas.isPointerSelect = false;
               return;
            }

            // Chạm Tab 1: Thần trang
            if (GameCanvas.AB(btn1_x, this.tabBtnY - 2, this.tabBtnW, this.tabBtnH + 4)) {
               this.equipToY = EQUIP_PAGE_H;
               this.cmdSwitchPage.caption = "Trang bị";
               super.idSelect = -1;
               this.BD = null;
               this.BI = false;
               GameCanvas.isPointerSelect = false;
               return;
            }

            // Chạm vào các ô trang bị (16 ô)
            boolean outside = true;
            for(int s = 0; s < 16; ++s) {
               int page = s / 8;
               int idx = s % 8;
               int row = idx / 2;
               int col = idx % 2;
               int sx = (col == 0) ? this.leftSlotX : this.rightSlotX;
               int sy = this.viewClipY + page * EQUIP_PAGE_H + 2 + row * this.slotGapY - this.equipScrollY;

               if (sy + super.wItem >= this.viewClipY && sy <= this.viewClipY + this.viewClipH && GameCanvas.AB(sx - 2, sy - 2, super.wItem + 4, super.wItem + 4)) {
                  outside = false;
                  selectSlotPointer(s, sx + super.wItem / 2, sy + super.wItem);
                  break;
               }
            }

            if (outside && !GameCanvas.isPoint(equipAreaX, this.viewClipY, equipAreaW, this.viewClipH)) {
               this.BD = null;
               this.BI = false;
               super.idSelect = -1;
            }
         }
      }
   }

   private void selectSlotPointer(int s, int px, int py) {
      if (s != super.idSelect) {
         super.idSelect = s;
         this.BD = (MainItem)this.AB.hashEquip.get("" + s);
         this.BI = (this.BD != null);
         this.BB = 10;
         if (this.BD != null) {
            setPosInfo(this.BD, px, py);
         }
      }
      GameCanvas.isPointerSelect = false;
   }

   public void setPosInfo(MainItem item, int xbe, int ybe) {
      int num = 100;
      int num2 = 40;
      if (item != null) {
         num = item.BS;
         num2 = item.BT;
      }
      this.BE = xbe - num / 2;
      if (this.BE + num > MotherCanvas.w - 4) {
         this.BE = MotherCanvas.w - num - 4;
      }
      if (this.BE < 4) {
         this.BE = 4;
      }
      this.BF = ybe + 2;
      if (this.BF + num2 > MotherCanvas.h - GameCanvas.hCommand - 4) {
         this.BF = ybe - num2 - super.wItem - 4;
      }
      if (this.BF < 4) {
         this.BF = 4;
      }
      if (item != null) {
         int maxH = MotherCanvas.h - GameCanvas.hCommand - 8 - this.BF;
         if (item.BT > maxH && maxH > 0) {
            item.CO = item.BT - maxH;
         }
      }
   }
}
