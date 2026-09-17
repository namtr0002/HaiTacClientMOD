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
              || lower.equals("kênh the gioi") || lower.equals("kenh thế giới")
              || lower.equals("global") || name.equals("Thế Giới") || name.equals("KTG") || name.equals(T.CR);
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

      this.isSmallScreen = (maxW < 320 || maxH < 240);

      if (maxW <= 300) {
         this.AC = maxW - 8;
         this.AD = Math.min(220, maxH - GameCanvas.hCommand - 6);
      } else if (maxW <= 480) {
         this.AC = maxW - 20;
         this.AD = Math.min(240, maxH - GameCanvas.hCommand - 10);
      } else if (maxW <= 720) {
         this.AC = (int)(maxW * 0.80f);
         if (this.AC < 340) this.AC = 340;
         if (this.AC > 480) this.AC = 480;
         this.AD = Math.min(260, (int)((maxH - GameCanvas.hCommand) * 0.88f));
      } else {
         this.AC = (int)(maxW * 0.65f);
         if (this.AC < 380) this.AC = 380;
         if (this.AC > 560) this.AC = 560;
         this.AD = Math.min(280, (int)((maxH - GameCanvas.hCommand) * 0.88f));
         if (this.AD < 220) this.AD = 220;
      }

      this.AA = MotherCanvas.hw - this.AC / 2;
      if (GameCanvas.isTouch) {
         this.AB = MotherCanvas.hh - this.AD / 2;
      } else {
         this.AB = MotherCanvas.hh - this.AD / 2 - GameCanvas.hCommand / 2;
      }

      this.AJ = 24;
      this.AF = this.AA + this.AJ + this.AE;
      this.AG = this.AB + this.AJ + this.AE;
      this.AI = this.AD - this.AJ - this.AE - (this.AE << 1);
      this.AH = this.AC - (this.AJ << 1) - (this.AE << 1);
      this.AK = this.AI / GameCanvas.hText + 2;

      if (this.isSmallScreen) {
         this.wLeft = this.AH;
         this.xRight = this.AF;
         this.wRight = this.AH;
      } else {
         this.wLeft = (this.AH >= 360) ? 135 : ((this.AH >= 280) ? 110 : 90);
         this.xRight = this.AF + this.wLeft + 4;
         this.wRight = this.AH - this.wLeft - 4;
      }

      this.AL = this.AH / 5;

      if (this.cmdClose != null) {
         if (GameCanvas.isTouch) {
            this.cmdClose.setPos(this.AA + this.AC - 16, this.AB + 14, MainTab.fraCloseTab, "");
         } else {
            this.cmdClose = AvMain.AA(this.cmdClose, 2);
         }
      }

      if (this.tfSearchMail == null) {
         this.tfSearchMail = new TField(this.AF, this.AG + 22, this.wLeft - 22);
         this.tfSearchMail.AI = false;
         this.tfSearchMail.AE = 20;
      }

      this.updateAllTfPositions();
      if (this.AN != null) {
         this.AN.AG();
         this.AA(0, (byte)0);
      }
      this.updateCamLeftList();
   }

   public void updateAllTfPositions() {
      int curLeftW = this.isSmallScreen ? this.AH : this.wLeft;
      if (this.tfSearchMail != null) {
         this.tfSearchMail.AA = this.AF;
         this.tfSearchMail.AB = this.AG + 20;
         this.tfSearchMail.AC = curLeftW - 24;
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
      mVector res = new mVector();
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
      mVector filtered = new mVector();
      String query = (this.tfSearchMail != null && this.tfSearchMail.getText() != null) ? this.tfSearchMail.getText().trim().toLowerCase() : "";
      for (int i = 0; i < allMail.size(); i++) {
         ChatDetail cd = (ChatDetail) allMail.elementAt(i);
         if (cd == null) continue;
         if (!cd.isInCategoryFilter(this.subFilterMail)) continue;
         if (query.length() > 0 && !cd.isMatchSearch(query)) continue;
         filtered.addElement(cd);
      }
      return filtered;
   }

   public mVector getPageMailItems() {
      mVector filtered = this.getFilteredMailList();
      int total = filtered.size();
      this.totalPagesMail = (total + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
      if (this.totalPagesMail < 1) this.totalPagesMail = 1;
      if (this.curPageMail >= this.totalPagesMail) this.curPageMail = this.totalPagesMail - 1;
      if (this.curPageMail < 0) this.curPageMail = 0;

      mVector pageItems = new mVector();
      int start = this.curPageMail * ITEMS_PER_PAGE;
      int end = Math.min(start + ITEMS_PER_PAGE, total);
      for (int i = start; i < end; i++) {
         pageItems.addElement(filtered.elementAt(i));
      }
      return pageItems;
   }

   public boolean hasUnreadInCat(int cat) {
      mVector list = this.getListByCat(cat);
      for (int i = 0; i < list.size(); ++i) {
         ChatDetail cd = (ChatDetail) list.elementAt(i);
         if (cd != null && cd.AS) return true;
      }
      return false;
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
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
      MainTab.AA(var1, this.AA, this.AB, this.AC, this.AD);
      AvMain.paintRect(var1, this.AF - this.AE, this.AG - this.AE, this.AH + (this.AE << 1), this.AI + (this.AE << 1), (byte)0, (byte)4);

      // 1. THANH NGANG 6 DANH MỤC TRÊN ĐỈNH
      int numCats = 6;
      int tabH = this.AJ / 5 * 4;
      int tabY = this.AB + this.AJ / 10 + this.AE / 2;
      int wTabs = this.AH - 24;
      int tabW = wTabs / numCats;

      for (int i = 0; i < numCats; i++) {
         boolean isSelected = (i == this.catSelect);
         int curX = this.AF + i * tabW;
         int thisTabW = (i == numCats - 1) ? (wTabs - (numCats - 1) * tabW) : tabW;

         byte colorIdx = (byte)(isSelected ? 4 : 2);
         boolean hasNew = this.hasUnreadInCat(i);
         if (hasNew && (GameCanvas.gameTick % 8 < 4)) {
            colorIdx = 1;
         }

         AvMain.paintRect(var1, curX, tabY, thisTabW, tabH, (byte)1, colorIdx);

         String title = this.mCatNames[i];
         if (isSelected) {
            mFont.tahoma_7b_white.drawString(var1, title, curX + thisTabW / 2, tabY + 2, 2);
         } else {
            mFont.tahoma_7_white.drawString(var1, title, curX + thisTabW / 2, tabY + 2, 2);
         }

         if (hasNew) {
            AvMain.paintRect(var1, curX + thisTabW - 6, tabY + 2, 4, 4, (byte)1, (byte)1);
         }
      }

      // NÚT ĐÓNG (X) GÓC TRÊN BÊN PHẢI
      int xClose = this.AA + this.AC - 16;
      int yClose = this.AB + 14;
      if (MainTab.fraCloseTab != null) {
         MainTab.fraCloseTab.drawFrame(0, xClose, yClose, 0, 3, var1);
      } else if (this.cmdClose != null) {
         this.cmdClose.paint(var1, this.cmdClose.xCmd, this.cmdClose.yCmd);
      }

      // 2. NỘI DUNG TỪNG CATEGORY
      if (this.catSelect == ChatDetail.CAT_MAIL) {
         // HỘP THƯ
         if (this.isSmallScreen) {
            if (this.smallScreenState == 0) {
               this.paintMailListView(var1, this.AF, this.AG, this.AH, this.AI);
            } else {
               this.paintMailDetailView(var1, this.AF, this.AG, this.AH, this.AI, true);
            }
         } else {
            // Màn hình to -> 2 Cột
            this.paintMailListView(var1, this.AF, this.AG, this.wLeft, this.AI);
            this.paintMailDetailView(var1, this.xRight, this.AG, this.wRight, this.AI, false);
         }
      } else if (this.catSelect == ChatDetail.CAT_PRIVATE) {
         // TRÒ CHUYỆN RIÊNG (2 Cột trên màn to, 1 Cột Master-Detail trên màn bé)
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
      // 1. Thanh phân loại 4 tab con (Tất cả, Lời mời, Quà, Hệ thống)
      int subH = 18;
      int subW = wArea / 4;
      for (int i = 0; i < 4; i++) {
         int chipX = xList + i * subW;
         int thisChipW = (i == 3) ? (wArea - 3 * subW) : subW;
         byte chipColor = (byte)(i == this.subFilterMail ? 4 : 2);
         AvMain.paintRect(var1, chipX, yList, thisChipW, subH, (byte)1, chipColor);
         mFont f = (i == this.subFilterMail) ? mFont.tahoma_7b_white : mFont.tahoma_7_white;
         f.drawString(var1, this.mSubFilterNames[i], chipX + thisChipW / 2, yList + 2, 2);
      }

      // 2. Ô tìm kiếm
      int searchY = yList + subH + 2;
      int searchH = 20;
      if (this.tfSearchMail != null) {
         this.tfSearchMail.AA = xList;
         this.tfSearchMail.AB = searchY;
         this.tfSearchMail.AC = wArea - 22;
         this.tfSearchMail.AD = searchH;
         this.tfSearchMail.paint(var1);
         if (this.tfSearchMail.getText() == null || this.tfSearchMail.getText().length() == 0) {
            mFont.tahoma_7_white.drawString(var1, "Tìm kiếm...", xList + 4, searchY + 3, 0);
         }
         // Nút Clear search [X]
         int clrX = xList + wArea - 20;
         AvMain.paintRect(var1, clrX, searchY, 18, searchH, (byte)1, (byte)1);
         mFont.tahoma_7b_white.drawString(var1, "x", clrX + 9, searchY + 2, 2);
      }

      // 3. Danh sách 10 mục của trang hiện tại
      int listY = searchY + searchH + 2;
      int pageBarH = 22;
      int listH = hArea - (subH + searchH + pageBarH + 6);

      AvMain.paintRect(var1, xList - 2, listY - 2, wArea + 2, listH + 4, (byte)1, (byte)0);

      var1.setClip_(xList - 2, listY, wArea + 2, listH);
      mGraphics.AC();
      mGraphics.AD();
      var1.translate(0, -this.CamLeftList.AC);

      mVector pageItems = this.getPageMailItems();
      int itemH = 24;
      int maxWText = wArea - 12;

      for (int i = 0; i < pageItems.size(); i++) {
         ChatDetail cd = (ChatDetail) pageItems.elementAt(i);
         if (cd == null) continue;

         int rowY = listY + i * (itemH + 2);
         boolean isRowSelected = (this.AN == cd);

         byte rowColor = (byte)(isRowSelected ? 4 : (cd.isInvite ? 3 : (cd.isGiftMail ? 2 : 1)));
         AvMain.paintRect(var1, xList, rowY, wArea - 2, itemH, (byte)1, rowColor);

         String displayName = (cd.mailTitle != null && cd.mailTitle.length() > 0) ? cd.mailTitle : cd.AO;
         mFont itemFont = isRowSelected ? mFont.tahoma_7b_white : mFont.tahoma_7_white;

         if (isRowSelected && cd.marqueeTitle != null && cd.marqueeTitle.isRun) {
            cd.marqueeTitle.paint(var1, xList + 4, rowY + 5, 0);
         } else {
            String shortName = displayName;
            if (itemFont.getWidth(shortName) > maxWText) {
               while (shortName.length() > 2 && itemFont.getWidth(shortName + "..") > maxWText) {
                  shortName = shortName.substring(0, shortName.length() - 1);
               }
               shortName += "..";
            }
            itemFont.drawString(var1, shortName, xList + 4, rowY + 5, 0);
         }

         // Unread Badge
         if (cd.AS) {
            AvMain.paintRect(var1, xList + wArea - 8, rowY + 4, 4, 4, (byte)1, (byte)1);
         }
      }

      if (pageItems.size() == 0) {
         mFont.tahoma_7_white.drawString(var1, "Không có thư", xList + wArea / 2, listY + listH / 2 - 6, 2);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);

      // 4. Thanh cuộn ngang Tab trang thư ở đáy
      int pageBarY = yList + hArea - pageBarH;
      AvMain.paintRect(var1, xList, pageBarY, wArea, pageBarH, (byte)1, (byte)0);

      int arrowBtnW = 18;
      // Nút Lùi trang [<]
      AvMain.paintRect(var1, xList, pageBarY + 1, arrowBtnW, pageBarH - 2, (byte)1, (byte)(this.curPageMail > 0 ? 2 : 0));
      mFont.tahoma_7b_white.drawString(var1, "<", xList + arrowBtnW / 2, pageBarY + 4, 2);

      // Nút Tiến trang [>]
      int nextX = xList + wArea - arrowBtnW;
      AvMain.paintRect(var1, nextX, pageBarY + 1, arrowBtnW, pageBarH - 2, (byte)1, (byte)(this.curPageMail < this.totalPagesMail - 1 ? 2 : 0));
      mFont.tahoma_7b_white.drawString(var1, ">", nextX + arrowBtnW / 2, pageBarY + 4, 2);

      // Vùng cuộn ngang các Tab trang
      int tabAreaX = xList + arrowBtnW + 2;
      int tabAreaW = wArea - (arrowBtnW * 2) - 4;

      var1.setClip_(tabAreaX, pageBarY, tabAreaW, pageBarH);
      mGraphics.AC();
      mGraphics.AD();
      var1.translate(-this.camPageTabs.AC, 0);

      int tabBtnW = 24;
      for (int p = 0; p < this.totalPagesMail; p++) {
         int pX = tabAreaX + p * (tabBtnW + 2);
         boolean isCurrent = (p == this.curPageMail);
         byte pColor = (byte)(isCurrent ? 4 : 2);
         AvMain.paintRect(var1, pX, pageBarY + 2, tabBtnW, pageBarH - 4, (byte)1, pColor);
         mFont pf = isCurrent ? mFont.tahoma_7b_white : mFont.tahoma_7_white;
         pf.drawString(var1, "" + (p + 1), pX + tabBtnW / 2, pageBarY + 4, 2);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
   }

   public void paintPrivateListView(mGraphics var1, int xList, int yList, int wArea, int hArea) {
      mVector listItems = this.getListByCat(ChatDetail.CAT_PRIVATE);

      AvMain.paintRect(var1, xList - 2, yList - 2, wArea + 2, hArea + 4, (byte)1, (byte)0);

      var1.setClip_(xList - 2, yList, wArea + 2, hArea);
      mGraphics.AC();
      mGraphics.AD();
      var1.translate(0, -this.CamLeftList.AC);

      int itemH = 24;
      int maxWText = wArea - 10;
      for (int i = 0; i < listItems.size(); i++) {
         ChatDetail cd = (ChatDetail) listItems.elementAt(i);
         if (cd == null) continue;

         int rowY = yList + i * (itemH + 2);
         boolean isRowSelected = (this.AN == cd);

         byte rowColor = (byte)(isRowSelected ? 4 : 1);
         AvMain.paintRect(var1, xList, rowY, wArea - 2, itemH, (byte)1, rowColor);

         String displayName = cd.AO;
         mFont itemFont = isRowSelected ? mFont.tahoma_7b_white : mFont.tahoma_7_white;

         String shortName = displayName;
         if (itemFont.getWidth(shortName) > maxWText) {
            while (shortName.length() > 2 && itemFont.getWidth(shortName + "..") > maxWText) {
               shortName = shortName.substring(0, shortName.length() - 1);
            }
            shortName += "..";
         }
         itemFont.drawString(var1, shortName, xList + 4, rowY + 5, 0);

         if (cd.AS) {
            AvMain.paintRect(var1, xList + wArea - 8, rowY + 4, 4, 4, (byte)1, (byte)1);
         }
      }

      if (listItems.size() == 0) {
         mFont.tahoma_7_white.drawString(var1, "Trống", xList + wArea / 2, yList + hArea / 2 - 6, 2);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
   }

   public void paintMailDetailView(mGraphics var1, int xDetail, int yDetail, int wArea, int hArea, boolean isSmallMode) {
      if (this.AN == null) {
         mFont.tahoma_7_white.drawString(var1, "Chọn một thư để xem", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
         return;
      }

      int topHeaderH = 0;
      if (isSmallMode) {
         topHeaderH = 22;
         // Nút quay lại
         AvMain.paintRect(var1, xDetail, yDetail, 55, topHeaderH, (byte)1, (byte)2);
         mFont.tahoma_7b_white.drawString(var1, "< Trở về", xDetail + 27, yDetail + 4, 2);

         // Tiêu đề
         String topTitle = (this.AN.mailTitle != null && this.AN.mailTitle.length() > 0) ? this.AN.mailTitle : this.AN.AO;
         if (mFont.tahoma_7b_white.getWidth(topTitle) > wArea - 65) {
            topTitle = topTitle.substring(0, Math.min(topTitle.length(), 14)) + "..";
         }
         mFont.tahoma_7b_yellow.drawString(var1, topTitle, xDetail + 62, yDetail + 4, 0);
      }

      int contentY = yDetail + topHeaderH + 2;
      int contentH = hArea - topHeaderH - 2;

      var1.setClip_(xDetail - 2, contentY, wArea + 4, contentH);
      mGraphics.AC();
      mGraphics.AD();
      var1.translate(0, -this.AP.AC);

      this.AU = this.AP.AC / GameCanvas.hText - 2;
      if (this.AU < 0) this.AU = 0;
      this.AV = this.AU + this.AK + 4;

      for (int var7 = this.AU; var7 <= this.AV; ++var7) {
         if (var7 < this.AN.AN.size() && var7 >= 0) {
            mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
            if (var8 != null) {
               AvMain.AB(var8.AE).drawString(var1, var8.AA, xDetail + 2, contentY + var7 * GameCanvas.hText, 0);
            }
         }
      }

      int giftY = contentY + (this.AN.AN.size() + 1) * GameCanvas.hText;
      int btnH = 22;

      if (this.AN.isInvite) {
         if (this.AN.typeInvite == 3) {
            int boxH = 22;
            AvMain.paintRect(var1, xDetail - 2, giftY, wArea + 2, boxH, (byte)1, (byte)2);
            String betText = "Mức cược: " + this.AN.priceFight + (this.AN.typeFight == 1 ? " Ruby (thua trả phí)" : " Beri");
            mFont.tahoma_7b_yellow.drawString(var1, betText, xDetail + 4, giftY + 4, 0);
            giftY += boxH + 6;

            int btnW = (wArea - 12) / 3;
            int btn1X = xDetail;
            int btn2X = xDetail + btnW + 4;
            int btn3X = xDetail + (btnW + 4) * 2;

            AvMain.paintRect(var1, btn1X, giftY, btnW, btnH, (byte)1, (byte)4);
            mFont.tahoma_7b_white.drawString(var1, (T.chapnhan != null && T.chapnhan.length() > 0) ? T.chapnhan : "Chấp nhận", btn1X + btnW / 2, giftY + 5, 2);

            AvMain.paintRect(var1, btn2X, giftY, btnW, btnH, (byte)1, (byte)2);
            mFont.tahoma_7b_white.drawString(var1, "Xem TT", btn2X + btnW / 2, giftY + 5, 2);

            AvMain.paintRect(var1, btn3X, giftY, btnW, btnH, (byte)1, (byte)3);
            mFont.tahoma_7b_white.drawString(var1, "Từ chối", btn3X + btnW / 2, giftY + 5, 2);
         } else {
            int btnW = (wArea - 8) / 2;
            int btn1X = xDetail;
            int btn2X = xDetail + btnW + 4;

            AvMain.paintRect(var1, btn1X, giftY, btnW, btnH, (byte)1, (byte)4);
            mFont.tahoma_7b_white.drawString(var1, (T.chapnhan != null && T.chapnhan.length() > 0) ? T.chapnhan : "Chấp nhận", btn1X + btnW / 2, giftY + 5, 2);

            AvMain.paintRect(var1, btn2X, giftY, btnW, btnH, (byte)1, (byte)3);
            mFont.tahoma_7b_white.drawString(var1, "Từ chối", btn2X + btnW / 2, giftY + 5, 2);
         }
      } else if (this.AN.mItemgift != null && this.AN.mItemgift.length > 0) {
         int numRows = (this.AN.mItemgift.length + 1) / 2;
         int cardH = 32;
         int giftBoxH = 22 + numRows * cardH + 4;
         AvMain.paintRect(var1, xDetail - 2, giftY, wArea + 2, giftBoxH, (byte)1, (byte)2);

         var1.setColor(16049947);
         var1.fillRect(xDetail + 2, giftY + 16, wArea - 6, 1);
         mFont.tahoma_7b_yellow.drawString(var1, T.quatt + ":", xDetail + 4, giftY + 2, 0);

         int colW = (wArea - 6) / 2;
         for (int gi = 0; gi < this.AN.mItemgift.length; gi++) {
            Item_Drop gItem = this.AN.mItemgift[gi];
            if (gItem == null) continue;

            int col = gi % 2;
            int row = gi / 2;
            int itemX = xDetail + 2 + col * (colW + 2);
            int itemY = giftY + 20 + row * cardH;

            int qColor = gItem.colorName != 0 ? (int)gItem.colorName : 3;
            AvMain.paintRect(var1, itemX, itemY, 26, 26, (byte)1, (byte)qColor);
            if (gItem.typeObject == 4 && gItem.IdIcon <= 2 && gItem.name != null && (gItem.name.toLowerCase().indexOf("beri") >= 0 || gItem.name.toLowerCase().indexOf("ruby") >= 0 || gItem.name.toLowerCase().indexOf("extol") >= 0 || gItem.name.toLowerCase().indexOf("vàng") >= 0 || gItem.name.toLowerCase().indexOf("ngọc") >= 0 || gItem.name.toLowerCase().indexOf("coin") >= 0)) {
               if (gItem.IdIcon == 0 || gItem.name.toLowerCase().indexOf("beri") >= 0 || gItem.name.toLowerCase().indexOf("vàng") >= 0) {
                  AvMain.fraMoney.drawFrame(0, itemX + 13, itemY + 13, 0, 3, var1);
               } else if (gItem.IdIcon == 1 || gItem.name.toLowerCase().indexOf("ruby") >= 0 || gItem.name.toLowerCase().indexOf("ngọc") >= 0) {
                  AvMain.fraMoney.drawFrame(1, itemX + 13, itemY + 13, 0, 3, var1);
               } else if (gItem.IdIcon == 2 || gItem.name.toLowerCase().indexOf("extol") >= 0 || gItem.name.toLowerCase().indexOf("coin") >= 0) {
                  AvMain.fraMoney.drawFrame(7, itemX + 13, itemY + 13, 0, 3, var1);
               }
            } else {
               gItem.AA(var1, itemX + 13, itemY + 13);
            }

            if (gItem.name != null && gItem.name.length() > 0) {
               String dName = gItem.name;
               if (dName.length() > 8 && colW < 90) {
                  dName = dName.substring(0, 7) + "..";
               }
               mFont.tahoma_7b_white.drawString(var1, dName, itemX + 28, itemY + 1, 0);
               String numStr = (gItem.num > 1) ? ("x" + AvMain.AA(gItem.num)) : "x1";
               mFont.tahoma_7_yellow.drawString(var1, numStr, itemX + 28, itemY + 13, 0);
            }
         }

         giftY += giftBoxH + 4;

         if (!this.AN.isClaimed) {
            int btnW = (wArea - 8) / 2;
            int btn1X = xDetail;
            int btn2X = xDetail + btnW + 4;

            AvMain.paintRect(var1, btn1X, giftY, btnW, btnH, (byte)1, (byte)4);
            mFont.tahoma_7b_white.drawString(var1, T.nhanqua, btn1X + btnW / 2, giftY + 5, 2);

            AvMain.paintRect(var1, btn2X, giftY, btnW, btnH, (byte)1, (byte)3);
            mFont.tahoma_7b_white.drawString(var1, "Xóa thư", btn2X + btnW / 2, giftY + 5, 2);
         } else {
            int btnW = Math.min(110, wArea - 10);
            int btnX = xDetail + (wArea - btnW) / 2;

            AvMain.paintRect(var1, btnX, giftY, btnW, btnH, (byte)1, (byte)3);
            mFont.tahoma_7b_white.drawString(var1, "Xóa thư", btnX + btnW / 2, giftY + 5, 2);
         }
      } else {
         int btnW = Math.min(110, wArea - 10);
         int btnX = xDetail + (wArea - btnW) / 2;

         AvMain.paintRect(var1, btnX, giftY, btnW, btnH, (byte)1, (byte)3);
         mFont.tahoma_7b_white.drawString(var1, "Xóa thư", btnX + btnW / 2, giftY + 5, 2);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
   }

   public void paintChatDetailView(mGraphics var1, int xDetail, int yDetail, int wArea, int hArea, boolean isSmallMode) {
      if (this.AN == null) {
         mFont.tahoma_7_white.drawString(var1, "Chưa có cuộc trò chuyện", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
         return;
      }

      int topHeaderH = 0;
      if (isSmallMode) {
         topHeaderH = 22;
         AvMain.paintRect(var1, xDetail, yDetail, 55, topHeaderH, (byte)1, (byte)2);
         mFont.tahoma_7b_white.drawString(var1, "< Trở về", xDetail + 27, yDetail + 4, 2);

         mFont.tahoma_7b_yellow.drawString(var1, this.AN.AO, xDetail + 62, yDetail + 4, 0);
      }

      if (this.AN.AT != null) {
         this.AN.AT.paint(var1);
         int btnSendW = 40;
         int btnSendH = this.AN.AT.AD > 20 ? this.AN.AT.AD : 22;
         int btnSendX = this.AN.AT.AA + this.AN.AT.AC + 4;
         int btnSendY = this.AN.AT.AB;
         AvMain.paintRect(var1, btnSendX, btnSendY, btnSendW, btnSendH, (byte)1, 4);
         mFont.tahoma_7b_white.drawString(var1, "Gửi", btnSendX + btnSendW / 2, btnSendY + btnSendH / 2 - 5, 2);
      }

      int contentY = yDetail + topHeaderH + 2;
      int rightClipH = hArea - topHeaderH - (this.AN.AT != null ? this.AN.AT.AD : 0) - 4;
      var1.setClip_(xDetail - 2, contentY, wArea + 4, rightClipH);
      mGraphics.AC();
      mGraphics.AD();
      var1.translate(0, -this.AP.AC);

      this.AU = this.AP.AC / GameCanvas.hText - 2;
      if (this.AU < 0) this.AU = 0;
      this.AV = this.AU + this.AK;

      for (int var7 = this.AU; var7 <= this.AV; ++var7) {
         if (var7 < this.AN.AN.size() && var7 >= 0) {
            mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
            if (var8.AF == 1) {
               int textW = mFont.tahoma_7_white.getWidth(var8.AA);
               int msgX = xDetail + wArea - 10 - textW;
               if (msgX < xDetail + 6) msgX = xDetail + 6;
               AvMain.paintRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, (byte)1, (byte)4);
               mFont.tahoma_7b_white.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
            } else if (var8.AF == 0) {
               int textW = mFont.tahoma_7_white.getWidth(var8.AA);
               int msgX = xDetail + 6;
               AvMain.paintRect(var1, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, (byte)1, (byte)2);
               mFont.tahoma_7_white.drawString(var1, var8.AA, msgX, contentY + var7 * GameCanvas.hText, 0);
            } else {
               AvMain.AB(var8.AE).drawString(var1, var8.AA, xDetail, contentY + var7 * GameCanvas.hText, 0);
            }
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
   }

   public void paintNormalChatView(mGraphics var1) {
      if (this.AN != null) {
         if (this.AN.AT != null) {
            this.AN.AT.paint(var1);
            int btnSendW = 40;
            int btnSendH = this.AN.AT.AD > 20 ? this.AN.AT.AD : 22;
            int btnSendX = this.AN.AT.AA + this.AN.AT.AC + 4;
            int btnSendY = this.AN.AT.AB;
            AvMain.paintRect(var1, btnSendX, btnSendY, btnSendW, btnSendH, (byte)1, 4);
            mFont.tahoma_7b_white.drawString(var1, "Gửi", btnSendX + btnSendW / 2, btnSendY + btnSendH / 2 - 5, 2);
         }

         int fullClipH = this.AI - (this.AN.AT != null ? this.AN.AT.AD : -this.AE) + 2;
         var1.setClip_(this.AF - this.AE, this.AG - this.AE, this.AH + (this.AE << 1), fullClipH);
         mGraphics.AC();
         mGraphics.AD();
         var1.translate(0, -this.AP.AC);
         this.AU = this.AP.AC / GameCanvas.hText - 2;
         if (this.AU < 0) this.AU = 0;
         this.AV = this.AU + this.AK;

         for (int var7 = this.AU; var7 <= this.AV; ++var7) {
            if (var7 < this.AN.AN.size() && var7 >= 0) {
               mSystem var8 = (mSystem) this.AN.AN.elementAt(var7);
               AvMain.AB(var8.AE).drawString(var1, var8.AA, this.AF, this.AG + var7 * GameCanvas.hText, 0);
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
      } else {
         String title = this.mCatNames[this.catSelect];
         mFont.tahoma_7_white.drawString(var1, "Không có dữ liệu " + title, this.AF + this.AH / 2, this.AG + this.AI / 2 - 6, 2);
      }
   }

   public void update() {
      if (this.AN != null) {
         if (this.AN.marqueeTitle != null) {
            this.AN.updateMarquee((this.isSmallScreen ? this.AH : this.wLeft) - 10, mFont.tahoma_7b_white);
         }
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
      int xClose = this.AA + this.AC - 16;
      int yClose = this.AB + 14;
      if (GameCanvas.AB(xClose - 14, yClose - 14, 28, 28) || (this.cmdClose != null && GameCanvas.AB(this.cmdClose.xCmd - 12, this.cmdClose.yCmd - 12, 24, 24))) {
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
      int tabH = this.AJ / 5 * 4;
      int tabY = this.AB + this.AJ / 10 + this.AE / 2;
      int wTabs = this.AH - 24;
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
            // Touch 4 Sub-filters [Tất cả][Lời mời][Quà][Hệ thống]
            int subH = 18;
            int subW = curListW / 4;
            if (GameCanvas.AB(this.AF, this.AG, curListW, subH)) {
               GameCanvas.isPointerSelect = false;
               int clickedSub = (GameCanvas.AY - this.AF) / subW;
               if (clickedSub >= 0 && clickedSub < 4) {
                  this.subFilterMail = clickedSub;
                  this.curPageMail = 0;
                  this.updateCamLeftList();
                  return;
               }
            }

            // Touch Ô tìm kiếm & Nút Clear
            if (this.tfSearchMail != null) {
               this.tfSearchMail.updatePointer();
               int clrX = this.AF + curListW - 20;
               int searchY = this.AG + subH + 2;
               if (GameCanvas.isPointerSelect && GameCanvas.AB(clrX, searchY, 18, 20)) {
                  GameCanvas.isPointerSelect = false;
                  this.tfSearchMail.AB("");
                  this.curPageMail = 0;
                  this.updateCamLeftList();
                  return;
               }
            }

            // Touch List 10 items
            int searchY = this.AG + subH + 2;
            int listY = searchY + 20 + 2;
            int pageBarH = 22;
            int listH = this.AI - (subH + 20 + pageBarH + 6);

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

            // Touch Thanh cuộn ngang Tab trang thư ở đáy
            int pageBarY = this.AG + this.AI - pageBarH;
            int arrowBtnW = 18;
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

            int tabAreaX = this.AF + arrowBtnW + 2;
            int tabAreaW = curListW - (arrowBtnW * 2) - 4;
            if (GameCanvas.isPointerSelect && GameCanvas.AB(tabAreaX, pageBarY, tabAreaW, pageBarH)) {
               int tabBtnW = 24;
               int clickedP = (GameCanvas.AY - tabAreaX + this.camPageTabs.AC) / (tabBtnW + 2);
               if (clickedP >= 0 && clickedP < this.totalPagesMail) {
                  GameCanvas.isPointerSelect = false;
                  this.curPageMail = clickedP;
                  this.updateCamLeftList();
                  return;
               }
            }
         }

         // Touch Detail Pane (Màn hình to hoặc SmallScreenState == 1)
         if (!this.isSmallScreen || this.smallScreenState == 1) {
            int dX = this.isSmallScreen ? this.AF : this.xRight;
            int dW = this.isSmallScreen ? this.AH : this.wRight;
            int topHeaderH = this.isSmallScreen ? 22 : 0;

            if (this.isSmallScreen && GameCanvas.isPointerSelect && GameCanvas.AB(dX, this.AG, 55, topHeaderH)) {
               GameCanvas.isPointerSelect = false;
               this.smallScreenState = 0; // Quay về danh sách
               this.getRightCmd();
               return;
            }

            if (this.AN != null && GameCanvas.isPointerSelect) {
               int contentY = this.AG + topHeaderH + 2;
               int giftY = contentY + (this.AN.AN.size() + 1) * GameCanvas.hText;

               if (this.AN.isInvite) {
                  int btnH = 22;
                  if (this.AN.typeInvite == 3) {
                     int boxH = 22;
                     giftY += boxH + 6;
                     int btnScreenY = giftY - this.AP.AC;
                     int btnW = (dW - 12) / 3;
                     int btn1X = dX;
                     int btn2X = dX + btnW + 4;
                     int btn3X = dX + (btnW + 4) * 2;

                     if (GameCanvas.AB(btn1X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdAcceptInvite != null) this.AN.cmdAcceptInvite.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn2X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdInfoEnemy != null) this.AN.cmdInfoEnemy.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn3X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDeclineInvite != null) this.AN.cmdDeclineInvite.AD();
                        return;
                     }
                  } else {
                     int btnScreenY = giftY - this.AP.AC;
                     int btnW = (dW - 8) / 2;
                     int btn1X = dX;
                     int btn2X = dX + btnW + 4;

                     if (GameCanvas.AB(btn1X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdAcceptInvite != null) this.AN.cmdAcceptInvite.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn2X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDeclineInvite != null) this.AN.cmdDeclineInvite.AD();
                        return;
                     }
                  }
               } else {
                  if (this.AN.mItemgift != null && this.AN.mItemgift.length > 0) {
                     int numRows = (this.AN.mItemgift.length + 1) / 2;
                     int cardH = 32;
                     int giftBoxH = 22 + numRows * cardH + 4;
                     giftY += giftBoxH + 4;
                  }
                  int btnScreenY = giftY - this.AP.AC;
                  int btnH = 22;

                  if (!this.AN.isClaimed && this.AN.mItemgift != null && this.AN.mItemgift.length > 0) {
                     int btnW = (dW - 8) / 2;
                     int btn1X = dX;
                     int btn2X = dX + btnW + 4;

                     if (GameCanvas.AB(btn1X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdNhanQua != null) this.AN.cmdNhanQua.AD();
                        return;
                     }
                     if (GameCanvas.AB(btn2X, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                        return;
                     }
                  } else {
                     int btnW = Math.min(110, dW - 10);
                     int btnX = dX + (dW - btnW) / 2;
                     if (GameCanvas.AB(btnX, btnScreenY, btnW, btnH)) {
                        GameCanvas.isPointerSelect = false;
                        if (this.AN.cmdDelMail != null) this.AN.cmdDelMail.AD();
                        return;
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
            if (GameCanvas.isPointerSelect && GameCanvas.AB(this.AF, this.AG, 55, 22)) {
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
         int listH = this.AI - (18 + 20 + 22 + 6);
         int lim = totalH - listH;
         if (lim < 0) lim = 0;
         int curListW = this.isSmallScreen ? this.AH : this.wLeft;
         if (this.CamLeftList == null) {
            this.CamLeftList = new ListNew(this.AF, this.AG + 42, curListW, listH, 0, 0, lim, true);
         } else {
            this.CamLeftList.x = this.AF;
            this.CamLeftList.y = this.AG + 42;
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
      int var3 = this.AI;
      if (this.AN.AT != null) {
         var3 -= this.AN.AT.AD;
      }

      int numRows = (this.AN.mItemgift != null && this.AN.mItemgift.length > 0) ? ((this.AN.mItemgift.length + 1) / 2) : 0;
      int extraGiftH = (numRows > 0) ? (numRows * 32 + 62) : 32;
      int totalH = (this.catSelect == ChatDetail.CAT_MAIL)
         ? ((this.AN.AN.size() + 1) * GameCanvas.hText + extraGiftH)
         : (this.AN.AN.size() * GameCanvas.hText + 10);
      int lim = totalH - var3;
      if (lim < 0) lim = 0;

      int curW = (this.catSelect == ChatDetail.CAT_MAIL || this.catSelect == ChatDetail.CAT_PRIVATE)
         ? ((this.isSmallScreen && this.smallScreenState == 1) ? this.AH : this.wRight)
         : this.AH;
      int curX = (this.catSelect == ChatDetail.CAT_MAIL || this.catSelect == ChatDetail.CAT_PRIVATE)
         ? ((this.isSmallScreen && this.smallScreenState == 1) ? this.AF : this.xRight)
         : this.AF;

      if (this.AP == null) {
         this.AP = new ListNew(curX, this.AG, curW, var3, 0, 0, lim, true);
      } else {
         this.AP.x = curX;
         this.AP.y = this.AG;
         this.AP.maxW = curW;
         this.AP.maxH = var3;
         this.AP.AD = lim;
         if (this.AP.AB > lim) this.AP.AB = lim;
         if (this.AP.AC > lim) this.AP.AC = lim;
      }

      if (this.AN.AW != null) {
         this.AN.AW.setInfo(curX + curW + 1, this.AG, var3, -7967666);
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

   public final void addNewChat(String var1, String var2, String var3, byte var4, boolean var5, int var6) {
      this.addNewChat(var1, var2, var3, var4, var5, var6, (byte)-1);
   }

   public final void AD(int var1) {
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
