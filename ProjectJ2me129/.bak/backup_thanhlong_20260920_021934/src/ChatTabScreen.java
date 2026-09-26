public class ChatTabScreen extends MainScreen {
   public int AA;
   public int AB;
   public int AC = 225;
   public int AD = 194;
   public int AE = 5;
   public int AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK;
   public int AL;
   public mVector AM = new mVector("ChatTabScreen.vecTabChat");
   public ChatDetail AN;
   public int AO = 0;

   public int catSelect = 0; // 0: Hộp Thư, 1: Thế Giới, 2: Công Cộng, 3: Trò Chuyện, 4: Bang Hội, 5: Hệ Thống
   public String[] mCatNames = new String[] { "Hộp Thư", "Thế Giới", "Công Cộng", "Trò Chuyện", "Bang Hội", "Hệ Thống" };
   public int wLeft;
   public int xRight;
   public int wRight;

   // Sub-filter cho Hộp Thư (Cat 0)
   public int subFilterMail = 0; // 0: Tất cả, 1: Lời mời, 2: Quà thư, 3: Hệ thống
   public String[] mSubFilterNames = new String[] { "Tất cả", "Lời mời", "Quà", "Hệ thống" };

   // Tìm kiếm cho Hộp Thư
   public TField tfSearchMail;

   // Phân trang cho Hộp Thư
   public static final int ITEMS_PER_PAGE = 10;
   public int curPageMail = 0;
   public int totalPagesMail = 1;
   public ListNew camPageTabs = new ListNew();

   // Hỗ trợ màn hình bé vs to
   public boolean isSmallScreen = false;
   public byte smallScreenState = 0; // 0: Danh sách (List), 1: Xem chi tiết (Detail)
   public iCommand cmdBackToList;

   // Tracking tải cache RMS
   public boolean hasLoadedCache = false;

   // GC Optimization: Reusable vectors to prevent per-frame heap allocations
   private mVector[] cachedCatLists = new mVector[] {
      new mVector(), new mVector(), new mVector(), new mVector(), new mVector(), new mVector()
   };
   private mVector cachedFilteredMail = new mVector();
   private mVector cachedPageMailItems = new mVector();

   private ListNew AP;
   private ListNew AQ;
   private ListNew CamLeftList;
   public Scroll scrLeftList = new Scroll();
   public iCommand cmdClose;
   private iCommand AS;
   private iCommand AT;
   private int AU;
   private int AV;

   public ChatTabScreen() {
      this.AP = new ListNew();
      this.AQ = new ListNew();
      this.CamLeftList = new ListNew();
      this.camPageTabs = new ListNew();
      this.cmdClose = new iCommand(T.close, 0, this);
      super.backCMD = this.cmdClose;
      super.DB = this.cmdClose;
      this.AS = new iCommand(T.close + " " + T.CP, 1, this);
      new iCommand(T.close + " " + T.CU, 2, this);
      this.AT = new iCommand(T.AD, 3, this);
      this.cmdBackToList = new iCommand("Quay lại", 4, this);
      if (this.getClass() == ChatTabScreen.class) {
         this.setPos();
      }
   }

   public static boolean isWorldTab(String name) {
      if (name == null) return false;
      String lower = name.trim().toLowerCase();
      return lower.equals("thế giới") || lower.equals("the gioi") || lower.equals("thegioi")
              || lower.equals("ktg") || lower.equals("world") || lower.equals("loa")
              || lower.equals("kênh thế giới") || lower.equals("kenh the gioi")
              || lower.equals("global") || name.equals("Thế Giới") || name.equals("KTG") || name.equals(T.GI);
   }

   public static boolean isPublicTab(String name) {
      if (name == null) return false;
      String lower = name.trim().toLowerCase();
      return lower.equals("công cộng") || lower.equals("cong cong") || lower.equals("khu vực") || lower.equals("khu vuc") || lower.equals("map") || lower.equals("public") || name.equals("Công Cộng");
   }

   public static boolean isClanTab(String name) {
      if (name == null) return false;
      String lower = name.trim().toLowerCase();
      if (lower.equals("bang hội") || lower.equals("bang hoi") || lower.equals("bang") || lower.equals("bang chủ") || lower.equals("nhóm")) {
         return true;
      }
      return name.equals(T.CQ) || name.equals("Bang Hội") || name.equals("Bang") || name.equals(T.CH);
   }

   public static boolean isSystemTab(String name) {
      if (name == null) return false;
      return name.equals(T.RH) || name.equals("Hệ Thống") || name.equals("Server") || name.equals(T.DV) || name.equals(T.QC) || name.equals(T.PD) || name.equals(T.CS);
   }

   public static boolean isMailTab(String name) {
      if (name == null) return false;
      return name.equals("Hộp Thư") || name.equals(T.thuhthong);
   }

   public static boolean isPrivateTab(String name) {
      return !isWorldTab(name) && !isPublicTab(name) && !isClanTab(name) && !isSystemTab(name) && !isMailTab(name);
   }

   public void loadCacheIfNeeded() {
      if (!this.hasLoadedCache && GameScreen.player != null && GameScreen.player.name != null && GameScreen.player.name.length() > 0) {
         mVector cached = SaveRms.loadMailCache(GameScreen.player.name);
         if (cached != null && cached.size() > 0) {
            for (int i = 0; i < cached.size(); i++) {
               ChatDetail cItem = (ChatDetail) cached.elementAt(i);
               if (cItem == null) continue;
               boolean exists = false;
               for (int j = 0; j < this.AM.size(); j++) {
                  ChatDetail cur = (ChatDetail) this.AM.elementAt(j);
                  if (cur == null) continue;
                  if (cItem.isInvite && cur.isInvite && cur.inviteId == cItem.inviteId && cur.typeInvite == cItem.typeInvite) {
                     exists = true;
                     break;
                  }
                  if (!cItem.isInvite && !cur.isInvite && cur.mailId > 0 && cur.mailId == cItem.mailId) {
                     exists = true;
                     break;
                  }
               }
               if (!exists) {
                  this.AM.addElement(cItem);
               }
            }
         }
         this.hasLoadedCache = true;
      }
   }

   public void initDefaultTabs() {
      if (this.getClass() != ChatTabScreen.class) {
         return;
      }

      this.loadCacheIfNeeded();

      boolean hasWorld = false;
      boolean hasPublic = false;
      boolean hasClan = false;
      boolean hasSys = false;

      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d == null) continue;
         if (d.typeCategory == ChatDetail.CAT_WORLD) hasWorld = true;
         if (d.typeCategory == ChatDetail.CAT_PUBLIC) hasPublic = true;
         if (d.typeCategory == ChatDetail.CAT_CLAN) hasClan = true;
         if (d.typeCategory == ChatDetail.CAT_SYSTEM) hasSys = true;
      }

      if (!hasWorld) {
         ChatDetail worldTab = new ChatDetail("Thế Giới", (byte)0, ChatDetail.CAT_WORLD);
         this.AM.addElement(worldTab);
      }
      if (!hasPublic) {
         ChatDetail publicTab = new ChatDetail("Công Cộng", (byte)0, ChatDetail.CAT_PUBLIC);
         this.AM.addElement(publicTab);
      }
      if (!hasClan) {
         ChatDetail clanTab = new ChatDetail(T.CQ, (byte)0, ChatDetail.CAT_CLAN);
         this.AM.addElement(clanTab);
      }
      if (!hasSys) {
         ChatDetail sysTab = new ChatDetail(T.RH, (byte)1, ChatDetail.CAT_SYSTEM);
         this.AM.addElement(sysTab);
      }
   }

   public void setPos() {
      if (this.getClass() != ChatTabScreen.class) {
         return;
      }

      int maxW = MotherCanvas.w;
      int maxH = MotherCanvas.h;

      this.isSmallScreen = (maxW < 340 || maxH < 240);

      if (maxW <= 300) {
         this.AC = maxW - 6;
         this.AD = Math.min(225, maxH - GameCanvas.hCommand - 4);
      } else if (maxW <= 440) {
         this.AC = maxW - 12;
         this.AD = Math.min(245, maxH - GameCanvas.hCommand - 6);
      } else if (maxW <= 640) {
         this.AC = Math.min(maxW - 20, 480);
         this.AD = Math.min(265, maxH - GameCanvas.hCommand - 10);
      } else {
         this.AC = Math.min((int)(maxW * 0.72f), 520);
         if (this.AC < 440) this.AC = 440;
         this.AD = Math.min(275, (int)((maxH - GameCanvas.hCommand) * 0.90f));
         if (this.AD < 230) this.AD = 230;
      }

      this.AA = MotherCanvas.hw - this.AC / 2;
      if (GameCanvas.isTouch) {
         this.AB = MotherCanvas.hh - this.AD / 2;
      } else {
         this.AB = MotherCanvas.hh - this.AD / 2 - GameCanvas.hCommand / 2;
      }

      this.AJ = 10;
      this.AE = 3;
      int topTabH = 22;

      this.AF = this.AA + this.AJ;
      this.AG = this.AB + this.AJ + topTabH + 2;
      this.AH = this.AC - (this.AJ << 1);
      this.AI = this.AD - this.AJ * 2 - topTabH - 4;
      this.AK = this.AI / GameCanvas.hText + 2;

      if (this.isSmallScreen) {
         this.wLeft = this.AH;
         this.xRight = this.AF;
         this.wRight = this.AH;
      } else {
         this.wLeft = 185; // Fit 4 sub-filters on 1 row!
         if (this.wLeft > this.AH / 2) {
            this.wLeft = this.AH / 2;
         }
         this.xRight = this.AF + this.wLeft + 6;
         this.wRight = this.AH - this.wLeft - 6;
      }

      this.AL = this.AH / 5;

      if (this.cmdClose != null) {
         if (GameCanvas.isTouch) {
            this.cmdClose.setPos(this.AA + this.AC - 16, this.AB + 12, MainTab.fraCloseTab, "");
         } else {
            this.cmdClose = AvMain.AA(this.cmdClose, 2);
         }
      }

      if (this.tfSearchMail == null) {
         this.tfSearchMail = new TField(this.AF, this.AG + 24, this.wLeft - 22);
         this.tfSearchMail.AI = false;
         this.tfSearchMail.AD = 18;
      }

      this.updateAllTfPositions();
      if (this.AN != null) {
         this.AN.AG();
         this.AA(0, (byte)0);
      }
      this.updateCamLeftList();
   }

   public static void drawModalBackdrop(mGraphics g) {
      if (g == null) return;
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      if (currentTheme == null || currentTheme.id == 0) return;
      g.setColor(0x000000);
      for (int y = 0; y < MotherCanvas.h; y += 2) {
         g.fillRect(0, y, MotherCanvas.w, 1);
      }
   }

   public static void drawPirateWindowFrame(mGraphics g, int x, int y, int w, int h) {
      if (g == null || w <= 0 || h <= 0) return;
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      if (currentTheme != null && currentTheme.id > 0) {
         currentTheme.paintWindow(g, x, y, w, h, null);
         return;
      }
      // 1. Drop shadow
      g.setColor(0x000000);
      g.fillRect(x + 3, y + 3, w, h);

      // 2. Base Royal Mahogany Wood
      ModernUI.fillGradientRect(g, x, y, w, h, 0x2A180D, 0x140B05);

      // 3. Antique Brass / Gold Outer Double Border
      g.setColor(0x8C681E);
      g.drawRect(x, y, w - 1, h - 1);
      g.setColor(0xD4AF37);
      g.drawRect(x + 1, y + 1, w - 3, h - 3);

      // 4. Inner Dark Bronze Inset Bevel
      g.setColor(0x3E2312);
      g.drawRect(x + 3, y + 3, w - 7, h - 7);
      g.setColor(0x100703);
      g.drawRect(x + 4, y + 4, w - 9, h - 9);

      // 5. Corner Brass Rivets
      drawBrassRivet(g, x + 5, y + 5);
      drawBrassRivet(g, x + w - 9, y + 5);
      drawBrassRivet(g, x + 5, y + h - 9);
      drawBrassRivet(g, x + w - 9, y + h - 9);
   }

   public static void drawBrassRivet(mGraphics g, int rx, int ry) {
      if (g == null) return;
      g.setColor(0x563B12);
      g.fillRect(rx, ry, 4, 4);
      g.setColor(0xFFE066);
      g.fillRect(rx + 1, ry + 1, 2, 2);
   }

   public static void drawParchmentPanel(mGraphics g, int x, int y, int w, int h) {
      if (g == null || w <= 0 || h <= 0) return;
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      if (currentTheme == null || currentTheme.id == 0) {
         AvMain.paintRect(g, x, y, w, h, (byte)0, 1);
         return;
      }
      // Aged pirate parchment paper
      ModernUI.fillGradientRect(g, x, y, w, h, 0x271C13, 0x18100A);
      g.setColor(0x664825);
      g.drawRect(x, y, w - 1, h - 1);
      g.setColor(0x3D2814);
      g.drawRect(x + 1, y + 1, w - 3, h - 3);
   }

   public void updateAllTfPositions() {
      int curLeftW = this.isSmallScreen ? this.AH : this.wLeft;
      if (this.tfSearchMail != null) {
         int subH = 18;
         boolean twoRows = (curLeftW < 160);
         int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
         this.tfSearchMail.AA = this.AF;
         this.tfSearchMail.AB = this.AG + totalSubH + 3;
         this.tfSearchMail.AC = curLeftW - 20;
         this.tfSearchMail.AD = 18;
      }

      if (this.AM != null) {
         int btnSendW = 40;
         for (int i = 0; i < this.AM.size(); ++i) {
            ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
            if (cd != null && cd.AT != null) {
               if (this.catSelect == ChatDetail.CAT_MAIL || this.catSelect == ChatDetail.CAT_PRIVATE) {
                  int curDetailX = (this.isSmallScreen && this.smallScreenState == 1) ? this.AF : this.xRight;
                  int curDetailW = (this.isSmallScreen && this.smallScreenState == 1) ? this.AH : this.wRight;
                  cd.AT.AA = curDetailX;
                  cd.AT.AB = this.AG + this.AI - TField.AB() - (this.AE / 2);
                  cd.AT.AC = curDetailW - btnSendW - 4;
               } else {
                  cd.AT.AA = this.AF;
                  cd.AT.AB = this.AG + this.AI - TField.AB() - (this.AE / 2);
                  cd.AT.AC = this.AH - btnSendW - 4;
               }
            }
         }
      }
   }

   public mVector getListByCat(int cat) {
      if (cat < 0 || cat >= this.cachedCatLists.length) {
         return new mVector();
      }
      mVector res = this.cachedCatLists[cat];
      res.removeAllElements();
      if (this.AM == null) return res;

      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
         if (cd == null) continue;

         if (cat == ChatDetail.CAT_MAIL) { // 0: Hộp Thư
            if (cd.typeCategory == ChatDetail.CAT_MAIL || cd.isInvite || cd.mailId > 0 || cd.isGiftMail || cd.isNotReply || isMailTab(cd.AO)) {
               res.addElement(cd);
            }
         } else if (cat == ChatDetail.CAT_WORLD) { // 1: Thế Giới
            if (cd.typeCategory == ChatDetail.CAT_WORLD || (cd.typeCategory == -1 && isWorldTab(cd.AO))) {
               res.addElement(cd);
            }
         } else if (cat == ChatDetail.CAT_PUBLIC) { // 2: Công Cộng
            if (cd.typeCategory == ChatDetail.CAT_PUBLIC || (cd.typeCategory == -1 && isPublicTab(cd.AO))) {
               res.addElement(cd);
            }
         } else if (cat == ChatDetail.CAT_PRIVATE) { // 3: Trò Chuyện riêng
            if (cd.typeCategory == ChatDetail.CAT_PRIVATE || (cd.typeCategory == -1 && isPrivateTab(cd.AO) && !cd.isInvite && cd.mailId <= 0 && !cd.isGiftMail && !cd.isNotReply)) {
               res.addElement(cd);
            }
         } else if (cat == ChatDetail.CAT_CLAN) { // 4: Bang Hội
            if (cd.typeCategory == ChatDetail.CAT_CLAN || (cd.typeCategory == -1 && isClanTab(cd.AO))) {
               res.addElement(cd);
            }
         } else if (cat == ChatDetail.CAT_SYSTEM) { // 5: Hệ Thống
            if (cd.typeCategory == ChatDetail.CAT_SYSTEM || (cd.typeCategory == -1 && isSystemTab(cd.AO))) {
               res.addElement(cd);
            }
         }
      }
      return res;
   }

   public mVector getFilteredMailList() {
      mVector allMail = this.getListByCat(ChatDetail.CAT_MAIL);
      this.cachedFilteredMail.removeAllElements();
      String query = (this.tfSearchMail != null && this.tfSearchMail.getText() != null) ? this.tfSearchMail.getText().trim().toLowerCase() : "";
      for (int i = 0; i < allMail.size(); i++) {
         ChatDetail cd = (ChatDetail) allMail.elementAt(i);
         if (cd == null) continue;
         if (!cd.isInCategoryFilter(this.subFilterMail)) continue;
         if (query.length() > 0 && !cd.isMatchSearch(query)) continue;
         this.cachedFilteredMail.addElement(cd);
      }
      return this.cachedFilteredMail;
   }

   public mVector getPageMailItems() {
      mVector filtered = this.getFilteredMailList();
      int total = filtered.size();
      this.totalPagesMail = (total + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
      if (this.totalPagesMail < 1) this.totalPagesMail = 1;
      if (this.curPageMail >= this.totalPagesMail) this.curPageMail = this.totalPagesMail - 1;
      if (this.curPageMail < 0) this.curPageMail = 0;

      this.cachedPageMailItems.removeAllElements();
      int start = this.curPageMail * ITEMS_PER_PAGE;
      int end = Math.min(start + ITEMS_PER_PAGE, total);
      for (int i = start; i < end; i++) {
         this.cachedPageMailItems.addElement(filtered.elementAt(i));
      }
      return this.cachedPageMailItems;
   }

   public boolean hasUnreadInCat(int cat) {
      mVector list = this.getListByCat(cat);
      for (int i = 0; i < list.size(); ++i) {
         ChatDetail cd = (ChatDetail) list.elementAt(i);
         if (cd != null && cd.AS) return true;
      }
      return false;
   }

   public int getUnreadCountInCat(int cat) {
      mVector list = this.getListByCat(cat);
      int count = 0;
      for (int i = 0; i < list.size(); ++i) {
         ChatDetail cd = (ChatDetail) list.elementAt(i);
         if (cd != null && cd.AS) {
            count++;
         }
      }
      return count;
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public void Show(MainScreen var1, int cat) {
      if (this.getClass() == ChatTabScreen.class) {
         this.initDefaultTabs();
         this.setPos();
         this.smallScreenState = 0;
         this.catSelect = cat;
         this.selectCategory(cat);
      }
      super.Show(var1);
      GameScreen.AF();
   }

   public void Show(MainScreen var1) {
      if (this.getClass() == ChatTabScreen.class) {
         this.initDefaultTabs();
         this.setPos();
         this.smallScreenState = 0;
         if (this.AN != null && this.getListByCat(this.catSelect).contains(this.AN)) {
            this.selectCategory(this.catSelect, this.AN);
         } else {
            if (this.hasUnreadInCat(ChatDetail.CAT_MAIL)) {
               this.catSelect = ChatDetail.CAT_MAIL;
            } else if (this.hasUnreadInCat(ChatDetail.CAT_PRIVATE)) {
               this.catSelect = ChatDetail.CAT_PRIVATE;
            } else if (this.hasUnreadInCat(ChatDetail.CAT_CLAN)) {
               this.catSelect = ChatDetail.CAT_CLAN;
            }
            this.selectCategory(this.catSelect);
         }
      }
      super.Show(var1);
      GameScreen.AF();
   }

   public void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
      case 1:
         if (super.mainScreen != null) {
            super.mainScreen.Show(super.mainScreen.mainScreen);
            return;
         }
         GameCanvas.gameScr.Show();
         return;
      case 2:
         if (this.AO >= 0 && this.AO < this.AM.size()) {
            this.AM.removeElementAt(this.AO);
         }
         this.AO = AvMain.AA(this.AO, this.AM.size() - 1, false);
         this.AD(this.AO);
         break;
      case 3:
         if (this.AN != null && this.AN.AT != null) {
            this.AN.AA(GameScreen.player.name);
         }
         break;
      case 4: // Quay lại màn hình danh sách (cho màn hình nhỏ)
         this.smallScreenState = 0;
         this.getRightCmd();
         GameScreen.AF();
         break;
      }
      super.commandPointer(var1, var2);
   }

   public void paint(mGraphics var1) {
      if (super.mainScreen != null) {
         super.mainScreen.paint(var1);
      }

      GameCanvas.resetTrans(var1);

      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      if (!isDefaultTheme) {
         // 0. Modal Backdrop Dim (Chống chói/đè giao diện phía sau)
         drawModalBackdrop(var1);

         // 1. Royal Pirate Mahogany Wood Frame with Brass Trim & Rivets
         drawPirateWindowFrame(var1, this.AA, this.AB, this.AC, this.AD);
      } else {
         // Classic HTTH Paper Window Frame
         this.AD(var1, this.AA, this.AB, this.AC, this.AD, 0);
         if (Interface_Game.imgHoavan != null) {
            var1.drawImage(Interface_Game.imgHoavan, this.AA + 6, this.AB + 6, 0);
            var1.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 2, this.AA + this.AC - 29, this.AB + 6, 0);
            var1.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 1, this.AA + 6, this.AB + this.AD - 29, 0);
            var1.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 3, this.AA + this.AC - 29, this.AB + this.AD - 29, 0);
         }
      }

      // 2. 6 Category Tabs on Top
      int numCats = 6;
      int tabH = 20;
      int tabY = this.AB + 8;
      int wTabs = this.AH - 22;
      int tabW = wTabs / numCats;

      for (int i = 0; i < numCats; i++) {
         boolean isSelected = (i == this.catSelect);
         int curX = this.AF + i * tabW;
         int thisTabW = (i == numCats - 1) ? (wTabs - (numCats - 1) * tabW) : tabW;
         int unreadCount = this.getUnreadCountInCat(i);
         boolean hasNew = (unreadCount > 0);

         if (isDefaultTheme) {
            AvMain.paintRect(var1, curX, tabY, thisTabW - 1, tabH, (byte)(isSelected ? 1 : 0), (isSelected ? 0 : 1));
            if (isSelected && AvMain.imgNenfocus != null) {
               var1.drawRegion(AvMain.imgNenfocus, 2, 2, thisTabW - 1, tabH, 0, curX, tabY, 0);
            }
            if (isSelected) {
               mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
            } else {
               mFont.tahoma_7_black.drawStringAutoCenter(var1, this.mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
            }
         } else {
            if (isSelected) {
               // Golden Oak Plank
               ModernUI.fillGradientRect(var1, curX, tabY, thisTabW, tabH, 0x8C561E, 0x54320F);
               var1.setColor(0xD4AF37);
               var1.drawRect(curX, tabY, thisTabW - 1, tabH - 1);
               var1.setColor(0xFFE066);
               var1.fillRect(curX + 1, tabY + 1, thisTabW - 2, 1);
               mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
            } else {
               // Weathered Teak Plank
               ModernUI.fillGradientRect(var1, curX, tabY, thisTabW, tabH, 0x281A10, 0x180F09);
               var1.setColor(0x4E341F);
               var1.drawRect(curX, tabY, thisTabW - 1, tabH - 1);
               mFont.tahoma_7_white.drawStringAutoCenter(var1, this.mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
            }
         }

         // Unread Ruby Wax Seal
         if (hasNew) {
            int sealX = curX + thisTabW - 5;
            int sealY = tabY + 3;
            var1.setColor(0xFFE066);
            var1.fillRect(sealX - 1, sealY - 1, 6, 6);
            var1.setColor(0xD92626);
            var1.fillRect(sealX, sealY, 4, 4);
         }
      }

      // NÚT ĐÓNG (X) GÓC TRÊN BÊN PHẢI
      int xClose = this.AA + this.AC - 19;
      int yClose = this.AB + 8;
      if (isDefaultTheme) {
         AvMain.paintRect(var1, xClose, yClose, 16, 16, (byte)1, 1);
         mFont.tahoma_7b_yellow.drawString(var1, "X", xClose + 8, yClose + 2, 2);
      } else {
         ModernUI.fillGradientRect(var1, xClose, yClose, 16, 16, 0x8C2020, 0x4D1010);
         var1.setColor(0xD4AF37);
         var1.drawRect(xClose, yClose, 15, 15);
         mFont.tahoma_7b_white.drawString(var1, "X", xClose + 8, yClose + 2, 2);
      }

      // 3. NỘI DUNG TỪNG CATEGORY
      if (this.catSelect == ChatDetail.CAT_MAIL) {
         // HỘP THƯ
         if (this.isSmallScreen) {
            if (this.smallScreenState == 0) {
               this.paintMailListView(var1, this.AF, this.AG, this.AH, this.AI);
            } else {
               this.paintMailDetailView(var1, this.AF, this.AG, this.AH, this.AI, true);
            }
         } else {
            // Màn hình to -> 2 Cột chuẩn chỉ
            this.paintMailListView(var1, this.AF, this.AG, this.wLeft, this.AI);
            this.paintMailDetailView(var1, this.xRight, this.AG, this.wRight, this.AI, false);
         }
      } else if (this.catSelect == ChatDetail.CAT_PRIVATE) {
         // TRÒ CHUYỆN RIÊNG
         if (this.isSmallScreen) {
            if (this.smallScreenState == 0) {
               this.paintPrivateListView(var1, this.AF, this.AG, this.AH, this.AI);
            } else {
               this.paintChatDetailView(var1, this.AF, this.AG, this.AH, this.AI, true);
            }
         } else {
            this.paintPrivateListView(var1, this.AF, this.AG, this.wLeft, this.AI);
            this.paintChatDetailView(var1, this.xRight, this.AG, this.wRight, this.AI, false);
         }
      } else {
         // CÁC KÊNH CHAT KHÁC (Thế Giới, Công Cộng, Bang Hội, Hệ Thống)
         this.paintNormalChatView(var1);
      }
   }

   public void paintMailListView(mGraphics var1, int xList, int yList, int wArea, int hArea) {
      int searchH = 18;
      int pageBarH = 20;
      int totalSubH = 20;
      int listY = yList + 42;
      int listH = hArea - 64;

      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      try {
         // 1. Thanh phân loại 4 tab con (Tất cả, Lời mời, Quà, Hệ thống)
         int subH = 18;
         boolean twoRows = (wArea < 160);
         totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
         if (twoRows) {
            int bW = (wArea - 4) / 2;
            for (int i = 0; i < 4; i++) {
               int row = i / 2;
               int col = i % 2;
               int bx = xList + col * (bW + 2);
               int by = yList + row * (subH + 2);
               boolean isSel = (i == this.subFilterMail);
               if (isDefaultTheme) {
                  AvMain.paintRect(var1, bx, by, bW, subH, (byte)(isSel ? 1 : 0), (isSel ? 0 : 1));
                  if (isSel) {
                     mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
                  } else {
                     mFont.tahoma_7_black.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
                  }
               } else {
                  int topCol = isSel ? 0x8C561E : 0x281B12;
                  int botCol = isSel ? 0x54320F : 0x180F09;
                  int bdrCol = isSel ? 0xD4AF37 : 0x4A3320;
                  ModernUI.fillGradientRect(var1, bx, by, bW, subH, topCol, botCol);
                  var1.setColor(bdrCol);
                  var1.drawRect(bx, by, bW - 1, subH - 1);
                  if (isSel) {
                     var1.setColor(0xFFE066);
                     var1.fillRect(bx + 1, by + 1, bW - 2, 1);
                     mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
                  } else {
                     mFont.tahoma_7_white.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
                  }
               }
            }
         } else {
            int bW = (wArea - 6) / 4;
            for (int i = 0; i < 4; i++) {
               int bx = xList + i * (bW + 2);
               boolean isSel = (i == this.subFilterMail);
               if (isDefaultTheme) {
                  AvMain.paintRect(var1, bx, yList, bW, subH, (byte)(isSel ? 1 : 0), (isSel ? 0 : 1));
                  if (isSel) {
                     mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
                  } else {
                     mFont.tahoma_7_black.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
                  }
               } else {
                  int topCol = isSel ? 0x8C561E : 0x281B12;
                  int botCol = isSel ? 0x54320F : 0x180F09;
                  int bdrCol = isSel ? 0xD4AF37 : 0x4A3320;
                  ModernUI.fillGradientRect(var1, bx, yList, bW, subH, topCol, botCol);
                  var1.setColor(bdrCol);
                  var1.drawRect(bx, yList, bW - 1, subH - 1);
                  if (isSel) {
                     var1.setColor(0xFFE066);
                     var1.fillRect(bx + 1, yList + 1, bW - 2, 1);
                     mFont.tahoma_7b_yellow.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
                  } else {
                     mFont.tahoma_7_white.drawStringAutoCenter(var1, this.mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
                  }
               }
            }
         }

         // 2. Ô tìm kiếm
         int searchY = yList + totalSubH + 3;
         if (this.tfSearchMail != null) {
            this.tfSearchMail.AA = xList;
            this.tfSearchMail.AB = searchY;
            this.tfSearchMail.AC = wArea - 20;
            this.tfSearchMail.AD = searchH;

            int clrX = xList + wArea - 18;
            if (isDefaultTheme) {
               AvMain.paintRect(var1, xList, searchY, wArea - 20, searchH, (byte)0, 3);
               String q = this.tfSearchMail.getText();
               if (q != null && q.length() > 0) {
                  mFont.tahoma_7_white.drawString(var1, q, xList + 5, searchY + 2, 0);
               } else {
                  mFont.tahoma_7_white.drawString(var1, "Tìm kiếm...", xList + 5, searchY + 2, 0);
               }
               AvMain.paintRect(var1, clrX, searchY, 18, searchH, (byte)1, 1);
               mFont.tahoma_7b_yellow.drawString(var1, "✕", clrX + 9, searchY + 2, 2);
            } else {
               ModernUI.fillGradientRect(var1, xList, searchY, wArea - 20, searchH, 0x140D08, 0x1F140C);
               var1.setColor(0x563B22);
               var1.drawRect(xList, searchY, wArea - 21, searchH - 1);

               String q = this.tfSearchMail.getText();
               if (q != null && q.length() > 0) {
                  mFont.tahoma_7_white.drawString(var1, q, xList + 5, searchY + 2, 0);
               } else {
                  mFont.tahoma_7_white.drawString(var1, "Tìm kiếm...", xList + 5, searchY + 2, 0);
               }

               // Nút Clear search [✕]
               ModernUI.fillGradientRect(var1, clrX, searchY, 18, searchH, 0x3D1A1A, 0x260E0E);
               var1.setColor(0x8C3333);
               var1.drawRect(clrX, searchY, 17, searchH - 1);
               mFont.tahoma_7b_white.drawString(var1, "✕", clrX + 9, searchY + 2, 2);
            }
         }

         // 3. Khung cuộn danh sách 10 mục
         listY = searchY + searchH + 3;
         listH = hArea - (totalSubH + searchH + pageBarH + 9);
         if (listH < 30) listH = 30;

         drawParchmentPanel(var1, xList - 1, listY - 1, wArea + 2, listH + 2);

         var1.setClip_(xList, listY, wArea, listH);
         int camOffset = (this.CamLeftList != null) ? this.CamLeftList.AC : 0;
         var1.translate(0, -camOffset);

         mVector pageItems = this.getPageMailItems();
         int itemH = 24;
         int maxWText = wArea - 28;

         for (int i = 0; i < pageItems.size(); i++) {
            ChatDetail cd = (ChatDetail) pageItems.elementAt(i);
            if (cd == null) continue;

            int rowY = listY + i * (itemH + 2);
            boolean isRowSelected = (this.AN == cd);

            if (isDefaultTheme) {
               AvMain.paintRect(var1, xList + 1, rowY, wArea - 2, itemH, (byte)(isRowSelected ? 1 : 0), isRowSelected ? 0 : 3);
            } else {
               if (isRowSelected) {
                  ModernUI.fillGradientRect(var1, xList + 1, rowY, wArea - 2, itemH, 0x5C3A1A, 0x3A220E);
                  var1.setColor(0xD4AF37);
                  var1.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
                  var1.setColor(0xFFE066);
                  var1.fillRect(xList + 2, rowY + 1, wArea - 5, 1);
               } else {
                  ModernUI.fillGradientRect(var1, xList + 1, rowY, wArea - 2, itemH, 0x24180E, 0x160E08);
                  var1.setColor(0x422D1B);
                  var1.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
               }
            }

            // Biểu tượng thư
            String iconStr = "✉";
            if (cd.isInvite) iconStr = "⚔";
            else if (cd.isGiftMail) iconStr = "🎁";

            if (cd.isInvite) {
               mFont.tahoma_7b_red.drawString(var1, iconStr, xList + 7, rowY + 4, 2);
            } else if (cd.isGiftMail) {
               mFont.tahoma_7b_yellow.drawString(var1, iconStr, xList + 7, rowY + 4, 2);
            } else {
               mFont.tahoma_7_white.drawString(var1, iconStr, xList + 7, rowY + 4, 2);
            }

            String displayName = (cd.mailTitle != null && cd.mailTitle.length() > 0) ? cd.mailTitle : cd.AO;
            mFont itemFont = isRowSelected ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white;
            itemFont.drawStringAuto(var1, displayName, xList + 16, rowY + 4, maxWText, 0);

            // Chấm đỏ thông báo chưa đọc
            if (cd.AS) {
               int sealX = xList + wArea - 8;
               int sealY = rowY + 6;
               var1.setColor(0xFFE066);
               var1.fillRect(sealX - 1, sealY - 1, 6, 6);
               var1.setColor(0xD92626);
               var1.fillRect(sealX, sealY, 4, 4);
            }
         }

         if (pageItems.size() == 0) {
            mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
            emptyF.drawString(var1, "Không có thư nào", xList + wArea / 2, listY + listH / 2 - 5, 2);
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }

      // 4. Thanh phân trang ở đáy
      try {
         int pageBarY = yList + hArea - pageBarH;
         int arrowBtnW = 20;

         if (isDefaultTheme) {
            // Nút Lùi trang [◀]
            AvMain.paintRect(var1, xList, pageBarY, arrowBtnW, 18, (byte)(this.curPageMail > 0 ? 1 : 0), (this.curPageMail > 0 ? 0 : 1));
            mFont.tahoma_7b_yellow.drawString(var1, "◀", xList + arrowBtnW / 2, pageBarY + 3, 2);

            // Nút Tiến trang [▶]
            int nextX = xList + wArea - arrowBtnW;
            boolean canNext = this.curPageMail < this.totalPagesMail - 1;
            AvMain.paintRect(var1, nextX, pageBarY, arrowBtnW, 18, (byte)(canNext ? 1 : 0), (canNext ? 0 : 1));
            mFont.tahoma_7b_yellow.drawString(var1, "▶", nextX + arrowBtnW / 2, pageBarY + 3, 2);

            // Ô Trang giữa: "Trang X / Y"
            int centerW = wArea - arrowBtnW * 2 - 4;
            int centerX = xList + arrowBtnW + 2;
            AvMain.paintRect(var1, centerX, pageBarY, centerW, 18, (byte)0, 1);
            String pageStr = (this.curPageMail + 1) + " / " + this.totalPagesMail;
            mFont.tahoma_7b_black.drawString(var1, pageStr, centerX + centerW / 2, pageBarY + 3, 2);
         } else {
            // Nút Lùi trang [◀]
            ModernUI.fillGradientRect(var1, xList, pageBarY, arrowBtnW, 18, 0x332214, 0x1F140A);
            var1.setColor(this.curPageMail > 0 ? 0xD4AF37 : 0x4D3622);
            var1.drawRect(xList, pageBarY, arrowBtnW - 1, 17);
            mFont.tahoma_7b_white.drawString(var1, "◀", xList + arrowBtnW / 2, pageBarY + 3, 2);

            // Nút Tiến trang [▶]
            int nextX = xList + wArea - arrowBtnW;
            ModernUI.fillGradientRect(var1, nextX, pageBarY, arrowBtnW, 18, 0x332214, 0x1F140A);
            var1.setColor(this.curPageMail < this.totalPagesMail - 1 ? 0xD4AF37 : 0x4D3622);
            var1.drawRect(nextX, pageBarY, arrowBtnW - 1, 17);
            mFont.tahoma_7b_white.drawString(var1, "▶", nextX + arrowBtnW / 2, pageBarY + 3, 2);

            // Ô Trang giữa: "Trang X / Y"
            int centerW = wArea - arrowBtnW * 2 - 4;
            int centerX = xList + arrowBtnW + 2;
            ModernUI.fillGradientRect(var1, centerX, pageBarY, centerW, 18, 0x1A1009, 0x120A05);
            var1.setColor(0x422C19);
            var1.drawRect(centerX, pageBarY, centerW - 1, 17);
            String pageStr = (this.curPageMail + 1) + " / " + this.totalPagesMail;
            mFont.tahoma_7b_yellow.drawString(var1, pageStr, centerX + centerW / 2, pageBarY + 3, 2);
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }
   }

   public void paintMailDetailView(mGraphics var1, int xDetail, int yDetail, int wArea, int hArea, boolean isSmallMode) {
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      if (this.AN == null) {
         drawParchmentPanel(var1, xDetail - 1, yDetail - 1, wArea + 2, hArea + 2);
         mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
         emptyF.drawString(var1, "Chọn một thư để xem", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
         return;
      }

      int topHeaderH = 0;
      if (isSmallMode) {
         topHeaderH = 22;
         ModernUI.drawButton(var1, xDetail, yDetail, 60, topHeaderH, "◀ Trở về", ModernUI.BTN_BLUE, false, false);
         String topTitle = (this.AN.mailTitle != null && this.AN.mailTitle.length() > 0) ? this.AN.mailTitle : this.AN.AO;
         mFont.tahoma_7b_yellow.drawStringAuto(var1, topTitle, xDetail + 66, yDetail + 4, wArea - 72, 0);
      }

      // Khung giấy da cuộn
      drawParchmentPanel(var1, xDetail - 1, yDetail + topHeaderH - 1, wArea + 2, hArea - topHeaderH + 2);

      int btnBarH = 26;
      int contentY = yDetail + topHeaderH + 4;
      int contentH = hArea - topHeaderH - btnBarH - 8;
      if (contentH < 20) contentH = 20;

      // Tiêu đề thư & người gửi
      String senderName = (this.AN.AO != null) ? this.AN.AO : "Hệ thống";
      String subjectName = (this.AN.mailTitle != null && this.AN.mailTitle.length() > 0) ? this.AN.mailTitle : "";

      if (isDefaultTheme) {
         mFont.tahoma_7b_brown.drawString(var1, "Người gửi: " + senderName, xDetail + 4, contentY, 0);
         contentY += 13;
         if (subjectName.length() > 0) {
            mFont.tahoma_7b_black.drawStringAuto(var1, "Tiêu đề: " + subjectName, xDetail + 4, contentY, wArea - 8, 0);
            contentY += 13;
         }
         var1.setColor(0x8C681E);
         var1.fillRect(xDetail + 2, contentY, wArea - 4, 1);
         contentY += 4;
      } else {
         mFont.tahoma_7b_yellow.drawString(var1, "Người gửi: " + senderName, xDetail + 4, contentY, 0);
         contentY += 13;
         if (subjectName.length() > 0) {
            mFont.tahoma_7b_white.drawStringAuto(var1, "Tiêu đề: " + subjectName, xDetail + 4, contentY, wArea - 8, 0);
            contentY += 13;
         }
         var1.setColor(0x8C681E);
         var1.fillRect(xDetail + 2, contentY, wArea - 4, 1);
         var1.setColor(0xD4AF37);
         var1.fillRect(xDetail + 10, contentY, wArea - 20, 1);
         contentY += 4;
      }
      contentH -= (subjectName.length() > 0 ? 30 : 17);

      // --- VÙNG NỘI DUNG CUỘN (TEXT + VẬT PHẨM QUÀ) ---
      try {
         var1.setClip_(xDetail, contentY, wArea, contentH);
         int camOffset = (this.AP != null) ? this.AP.AC : 0;
         var1.translate(0, -camOffset);

         this.AU = (camOffset / GameCanvas.hText) - 2;
         if (this.AU < 0) this.AU = 0;
         this.AV = this.AU + this.AK + 8;

         if (this.AN.AN != null) {
            for (int var7 = this.AU; var7 <= this.AV; ++var7) {
               if (var7 >= 0 && var7 < this.AN.AN.size()) {
                  mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
                  if (var8 != null && var8.AA != null) {
                     mFont fontToDraw = AvMain.AB(var8.AE);
                     if (fontToDraw == null) fontToDraw = isDefaultTheme ? mFont.tahoma_7_black : mFont.tahoma_7_white;
                     fontToDraw.drawString(var1, var8.AA, xDetail + 4, contentY + var7 * GameCanvas.hText, 0);
                  }
               }
            }
         }

         int textLinesCount = (this.AN.AN != null) ? this.AN.AN.size() : 0;
         int giftY = contentY + (textLinesCount + 1) * GameCanvas.hText;

         // Mức cược Thách đấu PvP
         if (this.AN.isInvite && this.AN.typeInvite == 3) {
            int boxH = 22;
            if (isDefaultTheme) {
               AvMain.paintRect(var1, xDetail + 2, giftY, wArea - 4, boxH, (byte)0, 1);
            } else {
               ModernUI.fillGradientRect(var1, xDetail + 2, giftY, wArea - 4, boxH, 0x331C0D, 0x1F0F05);
               var1.setColor(0xD4AF37);
               var1.drawRect(xDetail + 2, giftY, wArea - 5, boxH - 1);
            }
            String betText = "Mức cược: " + this.AN.priceFight + (this.AN.typeFight == 1 ? " Ruby (thua trả phí)" : " Beri");
            mFont betFont = isDefaultTheme ? mFont.tahoma_7b_brown : mFont.tahoma_7b_yellow;
            betFont.drawString(var1, betText, xDetail + 6, giftY + 4, 0);
            giftY += boxH + 6;
         }

         // Rương quà đính kèm
         if (this.AN.mItemgift != null && this.AN.mItemgift.length > 0) {
            int numRows = (this.AN.mItemgift.length + 1) / 2;
            int cardH = 32;
            int giftBoxH = 22 + numRows * cardH + 4;

            if (isDefaultTheme) {
               AvMain.paintRect(var1, xDetail + 2, giftY, wArea - 4, giftBoxH, (byte)0, 1);
               mFont.tahoma_7b_brown.drawString(var1, "🎁 " + T.quatt + ":", xDetail + 6, giftY + 4, 0);
            } else {
               ModernUI.fillGradientRect(var1, xDetail + 2, giftY, wArea - 4, giftBoxH, 0x2A190C, 0x170C05);
               var1.setColor(0x8C681E);
               var1.drawRect(xDetail + 2, giftY, wArea - 5, giftBoxH - 1);
               mFont.tahoma_7b_yellow.drawString(var1, "🎁 " + T.quatt + ":", xDetail + 6, giftY + 4, 0);
               var1.setColor(0x543A1B);
               var1.fillRect(xDetail + 4, giftY + 18, wArea - 8, 1);
            }

            int colW = (wArea - 12) / 2;
            if (colW < 40) colW = wArea - 12;

            for (int gi = 0; gi < this.AN.mItemgift.length; gi++) {
               Item_Drop gItem = this.AN.mItemgift[gi];
               if (gItem == null) continue;

               int col = gi % 2;
               int row = gi / 2;
               int itemX = xDetail + 4 + col * (colW + 4);
               int itemY = giftY + 22 + row * cardH;

               int qColor = gItem.colorName != 0 ? (int)gItem.colorName : 3;
               ModernUI.drawSlot(var1, itemX, itemY, 26, qColor, false);

               try {
                  if (gItem.typeObject == 4 && gItem.IdIcon <= 2 && gItem.name != null && (gItem.name.toLowerCase().indexOf("beri") >= 0 || gItem.name.toLowerCase().indexOf("ruby") >= 0 || gItem.name.toLowerCase().indexOf("extol") >= 0 || gItem.name.toLowerCase().indexOf("vàng") >= 0 || gItem.name.toLowerCase().indexOf("ngọc") >= 0 || gItem.name.toLowerCase().indexOf("coin") >= 0)) {
                     if (AvMain.fraMoney != null) {
                        if (gItem.IdIcon == 0 || gItem.name.toLowerCase().indexOf("beri") >= 0 || gItem.name.toLowerCase().indexOf("vàng") >= 0) {
                           AvMain.fraMoney.drawFrame(0, itemX + 13, itemY + 13, 0, 3, var1);
                        } else if (gItem.IdIcon == 1 || gItem.name.toLowerCase().indexOf("ruby") >= 0 || gItem.name.toLowerCase().indexOf("ngọc") >= 0) {
                           AvMain.fraMoney.drawFrame(1, itemX + 13, itemY + 13, 0, 3, var1);
                        } else if (gItem.IdIcon == 2 || gItem.name.toLowerCase().indexOf("extol") >= 0 || gItem.name.toLowerCase().indexOf("coin") >= 0) {
                           AvMain.fraMoney.drawFrame(7, itemX + 13, itemY + 13, 0, 3, var1);
                        }
                     }
                  } else {
                     gItem.AA(var1, itemX + 13, itemY + 13);
                  }
               } catch (Throwable eIcon) {
                  mFont.tahoma_7_white.drawString(var1, "?", itemX + 13, itemY + 7, 2);
               }

               if (gItem.name != null && gItem.name.length() > 0) {
                  int maxItemNameW = Math.max(10, colW - 32);
                  mFont nameFont = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7b_white;
                  nameFont.drawStringAuto(var1, gItem.name, itemX + 29, itemY + 1, maxItemNameW, 0);
                  String numStr = (gItem.num > 1) ? ("x" + AvMain.AA(gItem.num)) : "x1";
                  mFont.tahoma_7_yellow.drawString(var1, numStr, itemX + 29, itemY + 13, 0);
               }
            }
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }

      // --- VÙNG NÚT BẤM CỐ ĐỊNH Ở ĐÁY ---
      try {
         int btnY = yDetail + hArea - btnBarH - 2;
         int btnH = 22;

         var1.setColor(isDefaultTheme ? 0xD4AF37 : 0x8C681E);
         var1.fillRect(xDetail, btnY - 3, wArea, 1);

         if (this.AN.isInvite) {
            if (this.AN.typeInvite == 3) {
               int btnW = (wArea - 8) / 3;
               int btn1X = xDetail;
               int btn2X = xDetail + btnW + 4;
               int btn3X = xDetail + (btnW + 4) * 2;
               String capAccept = (T.chapnhan != null && T.chapnhan.length() > 0) ? T.chapnhan : "Chấp nhận";
               ModernUI.drawButton(var1, btn1X, btnY, btnW, btnH, capAccept, ModernUI.BTN_GREEN, false, false);
               ModernUI.drawButton(var1, btn2X, btnY, btnW, btnH, "Xem TT", ModernUI.BTN_BLUE, false, false);
               ModernUI.drawButton(var1, btn3X, btnY, btnW, btnH, "Từ chối", ModernUI.BTN_RED, false, false);
            } else {
               int btnW = (wArea - 6) / 2;
               int btn1X = xDetail;
               int btn2X = xDetail + btnW + 4;
               String capAccept = (T.chapnhan != null && T.chapnhan.length() > 0) ? T.chapnhan : "Chấp nhận";
               ModernUI.drawButton(var1, btn1X, btnY, btnW, btnH, capAccept, ModernUI.BTN_GREEN, false, false);
               ModernUI.drawButton(var1, btn2X, btnY, btnW, btnH, "Từ chối", ModernUI.BTN_RED, false, false);
            }
         } else {
            boolean hasGiftToClaim = (!this.AN.isClaimed && this.AN.mItemgift != null && this.AN.mItemgift.length > 0);
            boolean canReply = (!this.AN.isNotReply && this.AN.AO != null && this.AN.AO.length() > 0 && !isSystemTab(this.AN.AO));

            if (hasGiftToClaim) {
               if (canReply) {
                  int btnW = (wArea - 8) / 3;
                  int btn1X = xDetail;
                  int btn2X = xDetail + btnW + 4;
                  int btn3X = xDetail + (btnW + 4) * 2;
                  String capClaim = (T.nhanqua != null && T.nhanqua.length() > 0) ? T.nhanqua : "Nhận quà";
                  ModernUI.drawButton(var1, btn1X, btnY, btnW, btnH, capClaim, ModernUI.BTN_GREEN, false, false);
                  ModernUI.drawButton(var1, btn2X, btnY, btnW, btnH, "Trả lời", ModernUI.BTN_BLUE, false, false);
                  ModernUI.drawButton(var1, btn3X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
               } else {
                  int btnW = (wArea - 6) / 2;
                  int btn1X = xDetail;
                  int btn2X = xDetail + btnW + 4;
                  String capClaim = (T.nhanqua != null && T.nhanqua.length() > 0) ? T.nhanqua : "Nhận quà";
                  ModernUI.drawButton(var1, btn1X, btnY, btnW, btnH, capClaim, ModernUI.BTN_GREEN, false, false);
                  ModernUI.drawButton(var1, btn2X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
               }
            } else {
               if (canReply) {
                  int btnW = (wArea - 6) / 2;
                  int btn1X = xDetail;
                  int btn2X = xDetail + btnW + 4;
                  ModernUI.drawButton(var1, btn1X, btnY, btnW, btnH, "Trả lời", ModernUI.BTN_BLUE, false, false);
                  ModernUI.drawButton(var1, btn2X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
               } else {
                  int btnW = Math.min(120, wArea - 10);
                  int btnX = xDetail + (wArea - btnW) / 2;
                  ModernUI.drawButton(var1, btnX, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
               }
            }
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }
   }

   public void paintPrivateListView(mGraphics var1, int xList, int yList, int wArea, int hArea) {
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      try {
         drawParchmentPanel(var1, xList - 1, yList - 1, wArea + 2, hArea + 2);

         var1.setClip_(xList, yList, wArea, hArea);
         int camOffset = (this.CamLeftList != null) ? this.CamLeftList.AC : 0;
         var1.translate(0, -camOffset);

         mVector listItems = this.getListByCat(ChatDetail.CAT_PRIVATE);
         int itemH = 24;
         int maxWText = wArea - 20;
         for (int i = 0; i < listItems.size(); i++) {
            ChatDetail cd = (ChatDetail) listItems.elementAt(i);
            if (cd == null) continue;

            int rowY = yList + i * (itemH + 2);
            boolean isRowSelected = (this.AN == cd);

            if (isDefaultTheme) {
               AvMain.paintRect(var1, xList + 1, rowY, wArea - 2, itemH, (byte)(isRowSelected ? 1 : 0), isRowSelected ? 0 : 3);
            } else {
               if (isRowSelected) {
                  ModernUI.fillGradientRect(var1, xList + 1, rowY, wArea - 2, itemH, 0x5C3A1A, 0x3A220E);
                  var1.setColor(0xD4AF37);
                  var1.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
                  var1.setColor(0xFFE066);
                  var1.fillRect(xList + 2, rowY + 1, wArea - 5, 1);
               } else {
                  ModernUI.fillGradientRect(var1, xList + 1, rowY, wArea - 2, itemH, 0x24180E, 0x160E08);
                  var1.setColor(0x422D1B);
                  var1.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
               }
            }

            String displayName = cd.AO;
            mFont itemFont = isRowSelected ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white;
            itemFont.drawStringAuto(var1, displayName, xList + 6, rowY + 4, maxWText, 0);

            if (cd.AS) {
               int sealX = xList + wArea - 8;
               int sealY = rowY + 6;
               var1.setColor(0xFFE066);
               var1.fillRect(sealX - 1, sealY - 1, 6, 6);
               var1.setColor(0xD92626);
               var1.fillRect(sealX, sealY, 4, 4);
            }
         }

         if (listItems.size() == 0) {
            mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
            emptyF.drawString(var1, "Trống", xList + wArea / 2, yList + hArea / 2 - 6, 2);
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }
   }

   public void paintChatDetailView(mGraphics var1, int xDetail, int yDetail, int wArea, int hArea, boolean isSmallMode) {
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      if (this.AN == null) {
         drawParchmentPanel(var1, xDetail - 1, yDetail - 1, wArea + 2, hArea + 2);
         mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
         emptyF.drawString(var1, "Chưa có cuộc trò chuyện", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
         return;
      }

      int topHeaderH = 0;
      if (isSmallMode) {
         topHeaderH = 22;
         ModernUI.drawButton(var1, xDetail, yDetail, 60, topHeaderH, "◀ Trở về", ModernUI.BTN_BLUE, false, false);
         mFont.tahoma_7b_yellow.drawString(var1, this.AN.AO, xDetail + 66, yDetail + 4, 0);
      }

      drawParchmentPanel(var1, xDetail - 1, yDetail + topHeaderH - 1, wArea + 2, hArea - topHeaderH + 2);

      if (this.AN.AT != null) {
         this.AN.AT.paint(var1);
         int btnSendW = 40;
         int btnSendH = this.AN.AT.AD > 20 ? this.AN.AT.AD : 22;
         int btnSendX = this.AN.AT.AA + this.AN.AT.AC + 4;
         int btnSendY = this.AN.AT.AB;
         ModernUI.drawButton(var1, btnSendX, btnSendY, btnSendW, btnSendH, "Gửi", ModernUI.BTN_GREEN, false, false);
      }

      int contentY = yDetail + topHeaderH + 2;
      int rightClipH = hArea - topHeaderH - (this.AN.AT != null ? this.AN.AT.AD : 0) - 4;

      try {
         var1.setClip_(xDetail, contentY, wArea, rightClipH);
         int camOffset = (this.AP != null) ? this.AP.AC : 0;
         var1.translate(0, -camOffset);

         this.AU = (camOffset / GameCanvas.hText) - 2;
         if (this.AU < 0) this.AU = 0;
         this.AV = this.AU + this.AK;

         if (this.AN.AN != null) {
            for (int var7 = this.AU; var7 <= this.AV; ++var7) {
               if (var7 >= 0 && var7 < this.AN.AN.size()) {
                  mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
                  if (var8 != null && var8.AA != null) {
                     if (var8.AF == 1) {
                        int textW = mFont.tahoma_7_white.getWidth(var8.AA);
                        int msgX = xDetail + wArea - 10 - textW;
                        if (msgX < xDetail + 6) msgX = xDetail + 6;
                        if (isDefaultTheme) {
                           AvMain.paintRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, (byte)1, 0);
                           mFont.tahoma_7b_yellow.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
                        } else {
                           ModernUI.fillGradientRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 0x1E3A5F, 0x112238);
                           var1.setColor(0x3B6CA8);
                           var1.drawRect(msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 7, GameCanvas.hText - 1);
                           mFont.tahoma_7b_white.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
                        }
                     } else if (var8.AF == 0) {
                        int textW = mFont.tahoma_7_white.getWidth(var8.AA);
                        int msgX = xDetail + 6;
                        if (isDefaultTheme) {
                           AvMain.paintRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, (byte)0, 3);
                           mFont.tahoma_7b_white.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
                        } else {
                           ModernUI.fillGradientRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 0x3D2817, 0x24160C);
                           var1.setColor(0x664426);
                           var1.drawRect(msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 7, GameCanvas.hText - 1);
                           mFont.tahoma_7b_white.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
                        }
                     } else {
                        mFont fontToDraw = AvMain.AB(var8.AE);
                        if (fontToDraw == null) fontToDraw = isDefaultTheme ? mFont.tahoma_7_black : mFont.tahoma_7_white;
                        fontToDraw.drawString(var1, var8.AA, xDetail + 6, contentY + var7 * GameCanvas.hText, 0);
                     }
                  }
               }
            }
         }
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         var1.clearClip();
         GameCanvas.resetTrans(var1);
      }
   }

   public void paintNormalChatView(mGraphics var1) {
      UITheme currentTheme = UIThemeManager.getCurrentTheme();
      boolean isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

      if (this.AN != null) {
         if (this.AN.AT != null) {
            this.AN.AT.paint(var1);
            int btnSendW = 40;
            int btnSendH = this.AN.AT.AD > 20 ? this.AN.AT.AD : 22;
            int btnSendX = this.AN.AT.AA + this.AN.AT.AC + 4;
            int btnSendY = this.AN.AT.AB;
            ModernUI.drawButton(var1, btnSendX, btnSendY, btnSendW, btnSendH, "Gửi", ModernUI.BTN_GREEN, false, false);
         }

         int fullClipH = this.AI - (this.AN.AT != null ? this.AN.AT.AD : -this.AE) + 2;
         drawParchmentPanel(var1, this.AF - 2, this.AG - 2, this.AH + 4, fullClipH + 4);

         try {
            var1.setClip_(this.AF - this.AE, this.AG - this.AE, this.AH + (this.AE << 1), fullClipH);
            int camOffset = (this.AP != null) ? this.AP.AC : 0;
            var1.translate(0, -camOffset);
            this.AU = (camOffset / GameCanvas.hText) - 2;
            if (this.AU < 0) this.AU = 0;
            this.AV = this.AU + this.AK;

            if (this.AN.AN != null) {
               for (int var7 = this.AU; var7 <= this.AV; ++var7) {
                  if (var7 >= 0 && var7 < this.AN.AN.size()) {
                     mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
                     if (var8 != null && var8.AA != null) {
                        mFont fontToDraw = AvMain.AB(var8.AE);
                        if (fontToDraw == null) fontToDraw = isDefaultTheme ? mFont.tahoma_7_black : mFont.tahoma_7_white;
                        fontToDraw.drawString(var1, var8.AA, this.AF + 2, this.AG + var7 * GameCanvas.hText, 0);
                     }
                  }
               }
            }
         } catch (Throwable e) {
            e.printStackTrace();
         } finally {
            var1.clearClip();
            GameCanvas.resetTrans(var1);
         }
      } else {
         String title = this.mCatNames[this.catSelect];
         drawParchmentPanel(var1, this.AF - 2, this.AG - 2, this.AH + 4, this.AI + 4);
         mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
         emptyF.drawString(var1, "Không có dữ liệu " + title, this.AF + this.AH / 2, this.AG + this.AI / 2 - 6, 2);
      }
   }

   public void update() {
      if (this.CamLeftList != null) {
         this.CamLeftList.AC();
      }
      if (this.AP != null) {
         this.AP.AC();
      }
      if (this.camPageTabs != null) {
         this.camPageTabs.AC();
      }

      if (this.AN != null && this.AN.marqueeTitle != null) {
         this.AN.updateMarquee((this.isSmallScreen ? this.AH : this.wLeft) - 10, mFont.tahoma_7b_white);
      }
      super.update();
   }

   public void selectCategory(int cat) {
      this.selectCategory(cat, null);
   }

   public void selectCategory(int cat, ChatDetail targetTab) {
      this.catSelect = cat;
      if (this.catSelect < 0) this.catSelect = 0;
      if (this.catSelect > 5) this.catSelect = 5;
      this.smallScreenState = 0;

      mVector list = this.getListByCat(this.catSelect);
      if (list.size() > 0) {
         ChatDetail target = targetTab;
         if (target == null || !list.contains(target)) {
            if (this.AN != null && list.contains(this.AN)) {
               target = this.AN;
            } else {
               target = (ChatDetail) list.elementAt(0);
               for (int i = 0; i < list.size(); i++) {
                  ChatDetail item = (ChatDetail) list.elementAt(i);
                  if (item != null && item.AS) {
                     target = item;
                     break;
                  }
               }
            }
         }
         this.AN = target;
         this.AO = this.AM.indexOf(this.AN);
         this.AN.AS = false;
         this.AN.AG();
         this.AA(0, (byte)0);
         this.updateCamLeftList();
         this.getRightCmd();
      } else {
         this.AN = null;
         this.AO = -1;
      }

      this.updateAllTfPositions();
      GameScreen.AF();
   }

   public void handleKeyPress() {
      if (GameCanvas.keyMyHold[4]) { // Sang Trái
         GameCanvas.clearKeyHold(4);
         if (this.catSelect > 0) {
            this.selectCategory(this.catSelect - 1);
         }
      } else if (GameCanvas.keyMyHold[6]) { // Sang Phải
         GameCanvas.clearKeyHold(6);
         if (this.catSelect < 5) {
            this.selectCategory(this.catSelect + 1);
         }
      } else if (GameCanvas.keyMyHold[2]) { // Lên
         GameCanvas.clearKeyHold(2);
         if (this.catSelect == ChatDetail.CAT_MAIL && this.smallScreenState == 0) {
            this.CamLeftList.AB -= 26;
            if (this.CamLeftList.AB < 0) this.CamLeftList.AB = 0;
         } else {
            this.AP.AB -= GameCanvas.hText * 2;
            if (this.AP.AB < 0) this.AP.AB = 0;
         }
      } else if (GameCanvas.keyMyHold[8]) { // Xuống
         GameCanvas.clearKeyHold(8);
         if (this.catSelect == ChatDetail.CAT_MAIL && this.smallScreenState == 0) {
            this.CamLeftList.AB += 26;
            if (this.CamLeftList.AB > this.CamLeftList.AD) this.CamLeftList.AB = this.CamLeftList.AD;
         } else {
            this.AP.AB += GameCanvas.hText * 2;
            if (this.AP.AB > this.AP.AD) this.AP.AB = this.AP.AD;
         }
      }

      super.handleKeyPress();
      if (GameCanvas.AG(5)) {
         if (super.DF != null) {
            GameCanvas.clearKeyPressed(5);
            GameCanvas.clearKeyHold(5);
            super.DF.AD();
            return;
         }
      } else if (GameCanvas.AG(12)) {
         if (super.DE != null) {
            GameCanvas.clearKeyPressed(12);
            GameCanvas.clearKeyHold(12);
            super.DE.AD();
            return;
         }
      } else if (GameCanvas.AG(13) && super.backCMD != null) {
         GameCanvas.clearKeyPressed(13);
         GameCanvas.clearKeyHold(13);
         super.backCMD.AD();
      }
   }

   public void updatePointer() {
      if (this.CamLeftList != null) {
         this.CamLeftList.update_Pos_UP_DOWN();
      }
      if (this.AP != null) {
         this.AP.update_Pos_UP_DOWN();
      }
      if (this.camPageTabs != null) {
         this.camPageTabs.update_Pos_LEFT_RIGHT();
      }

      // 1. Touch Nút Đóng (X)
      int xClose = this.AA + this.AC - 19;
      int yClose = this.AB + 8;
      if (GameCanvas.AB(xClose - 4, yClose - 4, 24, 24) || (this.cmdClose != null && GameCanvas.AB(this.cmdClose.xCmd - 12, this.cmdClose.yCmd - 12, 24, 24))) {
         GameCanvas.isPointerSelect = false;
         if (this.cmdClose != null) {
            this.cmdClose.AD();
         } else if (this.AS != null) {
            this.AS.AD();
         }
         return;
      }

      // 2. Touch Top Category Tabs (6 Tabs)
      int numCats = 6;
      int tabH = 20;
      int tabY = this.AB + 8;
      int wTabs = this.AH - 22;
      int tabW = wTabs / numCats;

      if (GameCanvas.AB(this.AF, tabY, wTabs, tabH)) {
         GameCanvas.isPointerSelect = false;
         int clickedCat = (GameCanvas.AY - this.AF) / tabW;
         if (clickedCat >= 0 && clickedCat < numCats) {
            this.selectCategory(clickedCat);
            return;
         }
      }

      // 3. Xử lý Pointer cho HỘP THƯ (Cat 0)
      if (this.catSelect == ChatDetail.CAT_MAIL) {
         int curListW = this.isSmallScreen ? this.AH : this.wLeft;

         if (!this.isSmallScreen || this.smallScreenState == 0) {
            int subH = 18;
            boolean twoRows = (curListW < 160);
            int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
            if (GameCanvas.AB(this.AF, this.AG, curListW, totalSubH)) {
               GameCanvas.isPointerSelect = false;
               int clickedSub = -1;
               if (twoRows) {
                  int bW = (curListW - 4) / 2;
                  int col = (GameCanvas.AY - this.AF) / (bW + 2);
                  int row = (GameCanvas.AZ - this.AG) / (subH + 2);
                  if (col >= 0 && col < 2 && row >= 0 && row < 2) {
                     clickedSub = row * 2 + col;
                  }
               } else {
                  int bW = (curListW - 6) / 4;
                  int idx = (GameCanvas.AY - this.AF) / (bW + 2);
                  if (idx >= 0 && idx < 4) {
                     clickedSub = idx;
                  }
               }
               if (clickedSub >= 0 && clickedSub < 4) {
                  this.subFilterMail = clickedSub;
                  this.curPageMail = 0;
                  this.updateCamLeftList();
                  return;
               }
            }

            // Touch Ô tìm kiếm & Nút Clear
            int searchY = this.AG + totalSubH + 3;
            int searchH = 18;
            if (this.tfSearchMail != null) {
               this.tfSearchMail.updatePointer();
               int clrX = this.AF + curListW - 18;
               if (GameCanvas.isPointerSelect && GameCanvas.AB(clrX, searchY, 18, searchH)) {
                  GameCanvas.isPointerSelect = false;
                  this.tfSearchMail.AB("");
                  this.curPageMail = 0;
                  this.updateCamLeftList();
                  return;
               }
            }

            // Touch List 10 items
            int listY = searchY + searchH + 3;
            int pageBarH = 20;
            int listH = this.AI - (totalSubH + searchH + pageBarH + 9);
            if (listH < 30) listH = 30;

            if (GameCanvas.isPointerSelect && GameCanvas.AB(this.AF, listY, curListW, listH)) {
               mVector pageItems = this.getPageMailItems();
               int itemH = 26;
               int clickedIdx = (GameCanvas.AZ - listY + this.CamLeftList.AC) / itemH;
               if (clickedIdx >= 0 && clickedIdx < pageItems.size()) {
                  GameCanvas.isPointerSelect = false;
                  ChatDetail selectedDetail = (ChatDetail) pageItems.elementAt(clickedIdx);
                  if (this.AN != selectedDetail) {
                     this.AN = selectedDetail;
                     this.AO = this.AM.indexOf(this.AN);
                     this.AN.AS = false;
                     this.AN.AG();
                     this.AA(0, (byte)0);
                  }
                  if (this.isSmallScreen) {
                     this.smallScreenState = 1; // Chuyển sang màn hình chi tiết
                  }
                  this.getRightCmd();
                  GameScreen.AF();
                  return;
               }
            }

            // Touch Thanh phân trang Hải Tặc ở đáy
            int pageBarY = this.AG + this.AI - pageBarH;
            int arrowBtnW = 20;
            if (GameCanvas.isPointerSelect && GameCanvas.AB(this.AF, pageBarY, arrowBtnW, pageBarH)) {
               GameCanvas.isPointerSelect = false;
               if (this.curPageMail > 0) {
                  this.curPageMail--;
                  this.updateCamLeftList();
               }
               return;
            }
            int nextX = this.AF + curListW - arrowBtnW;
            if (GameCanvas.isPointerSelect && GameCanvas.AB(nextX, pageBarY, arrowBtnW, pageBarH)) {
               GameCanvas.isPointerSelect = false;
               if (this.curPageMail < this.totalPagesMail - 1) {
                  this.curPageMail++;
                  this.updateCamLeftList();
               }
               return;
            }
         }

         // Touch Detail Pane (Màn hình to hoặc SmallScreenState == 1)
         if (!this.isSmallScreen || this.smallScreenState == 1) {
            int dX = this.isSmallScreen ? this.AF : this.xRight;
            int dW = this.isSmallScreen ? this.AH : this.wRight;
            int topHeaderH = this.isSmallScreen ? 22 : 0;

            if (this.isSmallScreen && GameCanvas.isPointerSelect && GameCanvas.AB(dX, this.AG, 65, topHeaderH)) {
               GameCanvas.isPointerSelect = false;
               this.smallScreenState = 0; // Quay về danh sách
               this.getRightCmd();
               return;
            }

            if (this.AN != null && GameCanvas.isPointerSelect) {
               int btnBarH = 26;
               int btnY = this.AG + this.AI - btnBarH - 2;
               int btnH = 22;

               if (this.AN.isInvite) {
                  if (this.AN.typeInvite == 3) {
                     int btnW = (dW - 8) / 3;
                     int btn1X = dX;
                     int btn2X = dX + btnW + 4;
                     int btn3X = dX + (btnW + 4) * 2;

                     if (GameCanvas.AB(btn1X, btnY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdAcceptInvite != null) this.AN.cmdAcceptInvite.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn2X, btnY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdInfoEnemy != null) this.AN.cmdInfoEnemy.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn3X, btnY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDeclineInvite != null) this.AN.cmdDeclineInvite.AD();
                        return;
                     }
                  } else {
                     int btnW = (dW - 6) / 2;
                     int btn1X = dX;
                     int btn2X = dX + btnW + 4;

                     if (GameCanvas.AB(btn1X, btnY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdAcceptInvite != null) this.AN.cmdAcceptInvite.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn2X, btnY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDeclineInvite != null) this.AN.cmdDeclineInvite.AD();
                        return;
                     }
                  }
               } else {
                  boolean hasGiftToClaim = (!this.AN.isClaimed && this.AN.mItemgift != null && this.AN.mItemgift.length > 0);
                  boolean canReply = (!this.AN.isNotReply && this.AN.AO != null && this.AN.AO.length() > 0 && !isSystemTab(this.AN.AO));

                  if (hasGiftToClaim) {
                     if (canReply) {
                        int btnW = (dW - 8) / 3;
                        int btn1X = dX;
                        int btn2X = dX + btnW + 4;
                        int btn3X = dX + (btnW + 4) * 2;

                        if (GameCanvas.AB(btn1X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdNhanQua != null) this.AN.cmdNhanQua.AD();
                           return;
                        }
                        if (GameCanvas.AB(btn2X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           String replyTarget = this.AN.AO;
                           this.addNewChat(replyTarget, "", "", (byte)0, true, -1, ChatDetail.CAT_PRIVATE);
                           if (this.AN != null && this.AN.AT != null) {
                              this.AN.AT.AI = true;
                           }
                           return;
                        }
                        if (GameCanvas.AB(btn3X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                           return;
                        }
                     } else {
                        int btnW = (dW - 6) / 2;
                        int btn1X = dX;
                        int btn2X = dX + btnW + 4;

                        if (GameCanvas.AB(btn1X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdNhanQua != null) this.AN.cmdNhanQua.AD();
                           return;
                        }
                        if (GameCanvas.AB(btn2X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                           return;
                        }
                     }
                  } else {
                     if (canReply) {
                        int btnW = (dW - 6) / 2;
                        int btn1X = dX;
                        int btn2X = dX + btnW + 4;

                        if (GameCanvas.AB(btn1X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           String replyTarget = this.AN.AO;
                           this.addNewChat(replyTarget, "", "", (byte)0, true, -1, ChatDetail.CAT_PRIVATE);
                           if (this.AN != null && this.AN.AT != null) {
                              this.AN.AT.AI = true;
                           }
                           return;
                        }
                        if (GameCanvas.AB(btn2X, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                           return;
                        }
                     } else {
                        int btnW = Math.min(120, dW - 10);
                        int btnX = dX + (dW - btnW) / 2;
                        if (GameCanvas.AB(btnX, btnY, btnW, btnH)) {
                           GameCanvas.isPointerSelect = false;
                           if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                           return;
                        }
                     }
                  }
               }
            }
         }
         return;
      }

      // 4. Xử lý Pointer cho TRÒ CHUYỆN RIÊNG (Cat 3) & CÁC KÊNH CHAT KHÁC
      if (this.catSelect == ChatDetail.CAT_PRIVATE) {
         if (!this.isSmallScreen || this.smallScreenState == 0) {
            int curListW = this.isSmallScreen ? this.AH : this.wLeft;
            if (GameCanvas.isPointerSelect && GameCanvas.AB(this.AF, this.AG, curListW, this.AI)) {
               mVector listItems = this.getListByCat(this.catSelect);
               int itemH = 26;
               int clickedIdx = (GameCanvas.AZ - this.AG + this.CamLeftList.AC) / itemH;
               if (clickedIdx >= 0 && clickedIdx < listItems.size()) {
                  GameCanvas.isPointerSelect = false;
                  ChatDetail selectedDetail = (ChatDetail) listItems.elementAt(clickedIdx);
                  if (this.AN != selectedDetail) {
                     this.AN = selectedDetail;
                     this.AO = this.AM.indexOf(this.AN);
                     this.AN.AS = false;
                     this.AN.AG();
                     this.AA(0, (byte)0);
                  }
                  if (this.isSmallScreen) {
                     this.smallScreenState = 1;
                  }
                  this.getRightCmd();
                  this.updateAllTfPositions();
                  GameScreen.AF();
                  return;
               }
            }
         }

         if (this.isSmallScreen && this.smallScreenState == 1) {
            if (GameCanvas.isPointerSelect && GameCanvas.AB(this.AF, this.AG, 65, 22)) {
               GameCanvas.isPointerSelect = false;
               this.smallScreenState = 0;
               this.getRightCmd();
               return;
            }
         }
      }

      if (this.AN != null && this.AN.AT != null) {
         this.AN.AT.updatePointer();
         int btnSendW = 40;
         int btnSendH = this.AN.AT.AD > 20 ? this.AN.AT.AD : 22;
         int btnSendX = this.AN.AT.AA + this.AN.AT.AC + 4;
         int btnSendY = this.AN.AT.AB;
         if (GameCanvas.isPointerSelect && GameCanvas.AB(btnSendX, btnSendY, btnSendW, btnSendH)) {
            GameCanvas.isPointerSelect = false;
            this.AN.AA(GameScreen.player.name);
            return;
         }
      }

      super.updatePointer();
   }

   public void AA(int var1) {
      if (this.tfSearchMail != null && this.catSelect == ChatDetail.CAT_MAIL) {
         this.tfSearchMail.AD(var1);
      }
      if (var1 == 10 || var1 == 13 || var1 == -5) {
         if (this.AN != null && this.AN.AT != null) {
            this.AN.AA(GameScreen.player.name);
            return;
         }
      }
      if (this.AN != null && this.AN.AT != null) {
         this.AN.AT.AD(var1);
      }
      super.AA(var1);
   }

   public void updateCamLeftList() {
      if (this.catSelect == ChatDetail.CAT_MAIL) {
         mVector pageItems = this.getPageMailItems();
         int totalH = pageItems.size() * 26 + 10;
         int curListW = this.isSmallScreen ? this.AH : this.wLeft;
         int subH = 18;
         boolean twoRows = (curListW < 160);
         int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
         int searchH = 18;
         int pageBarH = 20;
         int listH = this.AI - (totalSubH + searchH + pageBarH + 9);
         if (listH < 30) listH = 30;
         int listY = this.AG + totalSubH + 3 + searchH + 3;
         int lim = totalH - listH;
         if (lim < 0) lim = 0;
         if (this.CamLeftList == null) {
            this.CamLeftList = new ListNew(this.AF, listY, curListW, listH, 0, 0, lim, true);
         } else {
            this.CamLeftList.x = this.AF;
            this.CamLeftList.y = listY;
            this.CamLeftList.maxW = curListW;
            this.CamLeftList.maxH = listH;
            this.CamLeftList.AD = lim;
            if (this.CamLeftList.AB > lim) this.CamLeftList.AB = lim;
            if (this.CamLeftList.AC > lim) this.CamLeftList.AC = lim;
         }

         // Update Horizontal Camera for Page Tabs
         int arrowBtnW = 18;
         int tabAreaW = curListW - (arrowBtnW * 2) - 4;
         int tabBtnW = 24;
         int totalTabsW = this.totalPagesMail * (tabBtnW + 2);
         int tabsLim = totalTabsW - tabAreaW;
         if (tabsLim < 0) tabsLim = 0;
         if (this.camPageTabs == null) {
            this.camPageTabs = new ListNew(this.AF + arrowBtnW + 2, this.AG + this.AI - 22, tabAreaW, 22, 0, 0, tabsLim, false);
         } else {
            this.camPageTabs.AD = tabsLim;
            if (this.camPageTabs.AB > tabsLim) this.camPageTabs.AB = tabsLim;
            if (this.camPageTabs.AC > tabsLim) this.camPageTabs.AC = tabsLim;
         }
      } else {
         mVector list = this.getListByCat(this.catSelect);
         int totalH = list.size() * 26 + 10;
         int lim = totalH - this.AI;
         if (lim < 0) lim = 0;
         int curListW = this.isSmallScreen ? this.AH : this.wLeft;
         if (this.CamLeftList == null) {
            this.CamLeftList = new ListNew(this.AF, this.AG, curListW, this.AI, 0, 0, lim, true);
         } else {
            this.CamLeftList.x = this.AF;
            this.CamLeftList.y = this.AG;
            this.CamLeftList.maxW = curListW;
            this.CamLeftList.maxH = this.AI;
            this.CamLeftList.AD = lim;
            if (this.CamLeftList.AB > lim) this.CamLeftList.AB = lim;
            if (this.CamLeftList.AC > lim) this.CamLeftList.AC = lim;
         }
      }
   }

   public final void AA(int var1, byte var2) {
      if (this.AN == null) return;
      int topHeaderH = (this.isSmallScreen && this.smallScreenState == 1) ? 22 : 0;
      int btnBarH = 26;
      int var3 = (this.catSelect == ChatDetail.CAT_MAIL) ? (this.AI - topHeaderH - btnBarH - 4) : this.AI;
      if (this.AN.AT != null) {
         var3 -= this.AN.AT.AD;
      }

      int numRows = (this.AN.mItemgift != null && this.AN.mItemgift.length > 0) ? ((this.AN.mItemgift.length + 1) / 2) : 0;
      int extraGiftH = (numRows > 0) ? (numRows * 32 + 28) : 0;
      if (this.AN.isInvite && this.AN.typeInvite == 3) {
         extraGiftH += 28;
      }
      int totalH = (this.catSelect == ChatDetail.CAT_MAIL)
         ? ((this.AN.AN.size() + 1) * GameCanvas.hText + extraGiftH + 8)
         : (this.AN.AN.size() * GameCanvas.hText + 10);
      int lim = totalH - var3;
      if (lim < 0) lim = 0;

      int curW = (this.catSelect == ChatDetail.CAT_MAIL || this.catSelect == ChatDetail.CAT_PRIVATE)
         ? ((this.isSmallScreen && this.smallScreenState == 1) ? this.AH : this.wRight)
         : this.AH;
      int curX = (this.catSelect == ChatDetail.CAT_MAIL || this.catSelect == ChatDetail.CAT_PRIVATE)
         ? ((this.isSmallScreen && this.smallScreenState == 1) ? this.AF : this.xRight)
         : this.AF;
      int curY = (this.catSelect == ChatDetail.CAT_MAIL) ? (this.AG + topHeaderH + 2) : this.AG;

      if (this.AP == null) {
         this.AP = new ListNew(curX, curY, curW, var3, 0, 0, lim, true);
      } else {
         this.AP.x = curX;
         this.AP.y = curY;
         this.AP.maxW = curW;
         this.AP.maxH = var3;
         this.AP.AD = lim;
         if (this.AP.AB > lim) this.AP.AB = lim;
         if (this.AP.AC > lim) this.AP.AC = lim;
      }

      if (this.AN.AW != null) {
         this.AN.AW.setInfo(curX + curW + 1, curY, var3, -7967666);
      }

      switch(var2) {
      case 0:
         this.AP.AA(0);
         this.AP.AC = 0;
         break;
      case 1:
         int var7 = this.AP.AB;
         byte var6 = (byte)((var7 != 0 && var7 != this.AP.AD) ? ((var7 < this.AP.AD - var3) ? 1 : 2) : 0);
         switch(var6) {
         case 0:
            this.AP.AA(this.AP.AD);
            return;
         case 1:
            this.AP.AA(var7);
            this.AP.AC = var7;
            return;
         default:
            this.AP.AA(var7 + var1 * GameCanvas.hText);
         }
      }
   }

   public final void addNewChat(String var1, String var2, String var3, byte var4, boolean var5, int var6, byte category) {
      if (var3 == null) return;

      byte targetCat = category;
      if (targetCat == -1) {
         if (isWorldTab(var1)) targetCat = ChatDetail.CAT_WORLD;
         else if (isPublicTab(var1)) targetCat = ChatDetail.CAT_PUBLIC;
         else if (isClanTab(var1)) targetCat = ChatDetail.CAT_CLAN;
         else if (isSystemTab(var1)) targetCat = ChatDetail.CAT_SYSTEM;
         else targetCat = ChatDetail.CAT_PRIVATE;
      }

      if (targetCat == ChatDetail.CAT_SYSTEM) {
         ChatDetail sysTab = null;
         for (int i = 0; i < this.AM.size(); ++i) {
            ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
            if (cd != null && cd.typeCategory == ChatDetail.CAT_SYSTEM) {
               sysTab = cd;
               break;
            }
         }
         if (sysTab == null) {
            sysTab = new ChatDetail(T.RH, (byte)1, ChatDetail.CAT_SYSTEM);
            this.AM.addElement(sysTab);
         }
         if (var3.length() > 0) {
            sysTab.AA(var2 + var3, var1, var6);
         }
         if (var5) {
            this.AN = sysTab;
            this.AO = this.AM.indexOf(sysTab);
            this.catSelect = ChatDetail.CAT_SYSTEM;
            this.AD(this.AO);
         }
         GameScreen.AF();
         return;
      }

      if (targetCat == ChatDetail.CAT_WORLD) {
         ChatDetail worldTab = null;
         for (int i = 0; i < this.AM.size(); ++i) {
            ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
            if (cd != null && cd.typeCategory == ChatDetail.CAT_WORLD) {
               worldTab = cd;
               break;
            }
         }
         if (worldTab == null) {
            worldTab = new ChatDetail("Thế Giới", (byte)0, ChatDetail.CAT_WORLD);
            this.AM.addElement(worldTab);
         }
         if (var3.length() > 0) {
            worldTab.AA(var2 + var3, var1, var6);
         }
         if (var5) {
            this.AN = worldTab;
            this.AO = this.AM.indexOf(worldTab);
            this.catSelect = ChatDetail.CAT_WORLD;
            this.AD(this.AO);
         }
         GameScreen.AF();
         return;
      }

      if (targetCat == ChatDetail.CAT_PUBLIC) {
         ChatDetail pubTab = null;
         for (int i = 0; i < this.AM.size(); ++i) {
            ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
            if (cd != null && cd.typeCategory == ChatDetail.CAT_PUBLIC) {
               pubTab = cd;
               break;
            }
         }
         if (pubTab == null) {
            pubTab = new ChatDetail("Công Cộng", (byte)0, ChatDetail.CAT_PUBLIC);
            this.AM.addElement(pubTab);
         }
         if (var3.length() > 0) {
            pubTab.AA(var2 + var3, var1, var6);
         }
         if (var5) {
            this.AN = pubTab;
            this.AO = this.AM.indexOf(pubTab);
            this.catSelect = ChatDetail.CAT_PUBLIC;
            this.AD(this.AO);
         }
         GameScreen.AF();
         return;
      }

      if (targetCat == ChatDetail.CAT_CLAN) {
         ChatDetail clanTab = null;
         for (int i = 0; i < this.AM.size(); ++i) {
            ChatDetail cd = (ChatDetail) this.AM.elementAt(i);
            if (cd != null && cd.typeCategory == ChatDetail.CAT_CLAN) {
               clanTab = cd;
               break;
            }
         }
         if (clanTab == null) {
            clanTab = new ChatDetail(T.CQ, (byte)0, ChatDetail.CAT_CLAN);
            this.AM.addElement(clanTab);
         }
         if (var3.length() > 0) {
            clanTab.AA(var2 + var3, var1, var6);
         }
         if (var5) {
            this.AN = clanTab;
            this.AO = this.AM.indexOf(clanTab);
            this.catSelect = ChatDetail.CAT_CLAN;
            this.AD(this.AO);
         }
         GameScreen.AF();
         return;
      }

      // Tin nhắn riêng (Private)
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.AO.compareTo(var1) == 0 && d.typeCategory == ChatDetail.CAT_PRIVATE) {
            if (var3.length() > 0) {
               d.AA(var2 + var3, var1, var6);
            }
            if (var5) {
               this.AN = d;
               this.AO = i;
               this.catSelect = ChatDetail.CAT_PRIVATE;
               this.AD(this.AO);
            }
            GameScreen.AF();
            return;
         }
      }

      ChatDetail var7 = new ChatDetail(var1, var4, ChatDetail.CAT_PRIVATE);
      if (var3.length() > 0) {
         var7.AA(var2 + var3, var1, var6);
      }
      this.AM.insertElementAt(var7, 0);
      if (var5 || GameCanvas.currentScreen == GameCanvas.chatTabScr) {
         this.catSelect = ChatDetail.CAT_PRIVATE;
         this.AN = var7;
         this.AO = 0;
         this.AD(this.AO);
      }
      GameScreen.AF();
   }

   public final void addWorldChat(String sender, String text) {
      this.addNewChat("Thế Giới", (sender != null && sender.length() > 0) ? (sender + ": ") : "", text, (byte)0, false, -1, ChatDetail.CAT_WORLD);
   }

   public final void addPublicChat(String sender, String text) {
      this.addNewChat("Công Cộng", (sender != null && sender.length() > 0) ? (sender + ": ") : "", text, (byte)0, false, -1, ChatDetail.CAT_PUBLIC);
   }

   public final void addSystemChat(String text) {
      this.addNewChat("Hệ Thống", "", text, (byte)1, false, -1, ChatDetail.CAT_SYSTEM);
   }

   public final void addClanChat(String sender, String text) {
      this.addNewChat(T.CQ, (sender != null && sender.length() > 0) ? (sender + ": ") : "", text, (byte)0, false, -1, ChatDetail.CAT_CLAN);
   }

   public final void addNewChat(String var1, String var2, String var3, byte var4, boolean var5, int var6) {
      this.addNewChat(var1, var2, var3, var4, var5, var6, (byte)-1);
   }

   public void AD(int var1) {
      if (var1 >= 0 && var1 < this.AM.size()) {
         this.AN = (ChatDetail) this.AM.elementAt(var1);
         this.AO = var1;
         if (this.AN != null) {
            this.AN.AS = false;
            this.AN.AG();
            this.AA(0, (byte)0);
            this.getRightCmd();
         }
         this.updateCamLeftList();
      }
   }

   public void getRightCmd() {
      if (this.isSmallScreen && this.smallScreenState == 1) {
         super.DB = this.cmdBackToList;
         super.backCMD = this.cmdBackToList;
      } else {
         super.DB = this.cmdClose;
         super.backCMD = this.cmdClose;
      }

      if (this.AN == null) {
         super.center = null;
         super.DA = null;
         return;
      }

      if (this.AN.isInvite) {
         super.center = this.AN.cmdAcceptInvite;
         super.DA = (this.AN.typeInvite == 3) ? this.AN.cmdInfoEnemy : this.AN.cmdDeclineInvite;
      } else if (this.AN.isGiftMail && !this.AN.isClaimed) {
         super.center = this.AN.cmdNhanQua;
         super.DA = this.AN.cmdDelMail;
      } else if (this.AN.AT != null) {
         this.AN.AT.AA(true);
         super.center = this.AT;
         super.DA = null;
      } else {
         super.center = null;
         super.DA = null;
      }
   }

   public final void addNewInvite(int inviteId, byte typeInvite, String inviteName, String inviteInfo, int priceFight, int typeFight) {
      if (inviteName == null) inviteName = "";
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.isInvite && d.inviteId == inviteId && d.typeInvite == typeInvite) {
            d.setInvite(inviteId, typeInvite, inviteName, inviteInfo, priceFight, typeFight);
            if (GameCanvas.currentScreen != GameCanvas.chatTabScr || this.AN != d) {
               d.AS = true;
            }
            // Ưu tiên đưa lên đầu
            this.AM.removeElementAt(i);
            this.AM.insertElementAt(d, 0);
            if (GameScreen.player != null) {
               SaveRms.saveMailCache(GameScreen.player.name, this.AM);
            }
            GameScreen.AF();
            return;
         }
      }

      ChatDetail cd = new ChatDetail(inviteName, (byte) 1, ChatDetail.CAT_MAIL);
      cd.setInvite(inviteId, typeInvite, inviteName, inviteInfo, priceFight, typeFight);
      if (GameCanvas.currentScreen != GameCanvas.chatTabScr) {
         cd.AS = true;
      }

      // Mới là ưu tiên hiện lên đầu
      this.AM.insertElementAt(cd, 0);
      if (GameCanvas.currentScreen == GameCanvas.chatTabScr && this.catSelect == ChatDetail.CAT_MAIL) {
         this.curPageMail = 0;
         this.AO = 0;
         this.AD(this.AO);
      }
      if (!GameCanvas.isTouch) {
         this.cmdClose.caption = T.close;
      }
      if (GameScreen.player != null) {
         SaveRms.saveMailCache(GameScreen.player.name, this.AM);
      }
      GameScreen.AF();
   }

   public final void removeInvite(int inviteId, byte typeInvite) {
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.isInvite && d.inviteId == inviteId && (typeInvite == -1 || d.typeInvite == typeInvite)) {
            this.AM.removeElementAt(i);
            if (this.catSelect == ChatDetail.CAT_MAIL) {
               mVector list = this.getPageMailItems();
               if (list.size() > 0) {
                  this.AN = (ChatDetail) list.elementAt(0);
                  this.AO = this.AM.indexOf(this.AN);
                  this.AD(this.AO);
               } else {
                  this.AN = null;
                  this.AO = -1;
               }
            }
            if (GameScreen.player != null) {
               SaveRms.saveMailCache(GameScreen.player.name, this.AM);
            }
            GameScreen.AF();
            break;
         }
      }
   }

   public final void removeAllInvites() {
      for (int i = this.AM.size() - 1; i >= 0; --i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d != null && d.isInvite) {
            this.AM.removeElementAt(i);
         }
      }
      if (this.catSelect == ChatDetail.CAT_MAIL) {
         mVector list = this.getPageMailItems();
         if (list.size() > 0) {
            this.AN = (ChatDetail) list.elementAt(0);
            this.AO = this.AM.indexOf(this.AN);
            this.AD(this.AO);
         } else {
            this.AN = null;
            this.AO = -1;
         }
      }
      if (GameScreen.player != null) {
         SaveRms.saveMailCache(GameScreen.player.name, this.AM);
      }
      GameScreen.AF();
   }

   public final void addNewMail(String var1, String var2, String var3, byte maskNotReply, int mailId, String title, byte isClaimed, byte typeMail, Item_Drop[] gifts) {
      if (var3 == null) var3 = "";
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.mailId == mailId && mailId > 0) {
            d.setMailGift(mailId, title, isClaimed, typeMail, gifts, maskNotReply == 1);
            d.contentRaw = var3;
            if (GameCanvas.currentScreen != GameCanvas.chatTabScr || this.AN != d) {
               d.AS = true;
            }
            // Ưu tiên lên đầu
            this.AM.removeElementAt(i);
            this.AM.insertElementAt(d, 0);
            if (GameScreen.player != null) {
               SaveRms.saveMailCache(GameScreen.player.name, this.AM);
            }
            GameScreen.AF();
            return;
         }
      }

      ChatDetail var10 = new ChatDetail(var1, (byte) 1, ChatDetail.CAT_MAIL);
      var10.setMailGift(mailId, title, isClaimed, typeMail, gifts, maskNotReply == 1);
      var10.contentRaw = var3;
      if (var3.length() > 0) {
         var10.AA(var3, var1, -1);
      }

      if (GameCanvas.currentScreen != GameCanvas.chatTabScr) {
         var10.AS = true;
      }

      // Mới là ưu tiên hiện lên đầu
      this.AM.insertElementAt(var10, 0);
      if (GameCanvas.currentScreen == GameCanvas.chatTabScr) {
         this.catSelect = 0;
         this.curPageMail = 0;
         this.AO = 0;
         this.AD(this.AO);
      }
      if (!GameCanvas.isTouch) {
         this.cmdClose.caption = T.close;
      }
      if (GameScreen.player != null) {
         SaveRms.saveMailCache(GameScreen.player.name, this.AM);
      }
      GameScreen.AF();
   }

   public final void updateMailClaimed(int mailId, boolean success) {
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.mailId == mailId) {
            d.isClaimed = true;
            if (this.AN == d) {
               this.AD(this.AO);
            }
            if (GameScreen.player != null) {
               SaveRms.saveMailCache(GameScreen.player.name, this.AM);
            }
            GameScreen.AF();
            break;
         }
      }
   }

   public final void removeMailTab(int mailId) {
      for (int i = 0; i < this.AM.size(); ++i) {
         ChatDetail d = (ChatDetail) this.AM.elementAt(i);
         if (d.mailId == mailId) {
            this.AM.removeElementAt(i);
            mVector list = this.getPageMailItems();
            if (list.size() > 0) {
               this.AN = (ChatDetail) list.elementAt(0);
               this.AO = this.AM.indexOf(this.AN);
               this.AD(this.AO);
            } else {
               this.AN = null;
               this.AO = -1;
            }
            if (GameScreen.player != null) {
               SaveRms.saveMailCache(GameScreen.player.name, this.AM);
            }
            GameScreen.AF();
            break;
         }
      }
   }

   public final void AA(String var1, String var2, String var3, byte var4, boolean var5) {
      if (!MsgSpamSetup.AA(2, var1)) {
         this.AB(var1, var2, var3, (byte)0, false);
      } else {
         for(int var6 = 0; var6 < this.AM.size(); ++var6) {
            if (((ChatDetail)this.AM.elementAt(var6)).AO.compareTo(var1) == 0) {
               this.AB(var1, var2, var3, (byte)0, false);
               return;
            }
         }
      }
   }

   public final void AB(String var1, String var2, String var3, byte var4, boolean var5) {
      this.addNewChat(var1, var2, var3, var4, var5, -1);
   }

   public void AB() {
      super.handleKeyPress();
   }

   public final void AF() {
      this.AQ = new ListNew(this.AA, this.AB, this.AH, this.AJ + (this.AE << 1), 0, 0, (this.AM.size() + 1) * this.AL / 2 - this.AH, true);
   }
}
