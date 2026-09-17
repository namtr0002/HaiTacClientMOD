public class ChatDetail extends AvMain {
   public int AE = 5;
   public int AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK;
   public int AL;
   public ListNew AM;
   public mVector AN = new mVector("ChatDetail.vecDetail");
   public String AO;
   public String AP;
   public String AQ;
   public byte AR = -1;
   public boolean AS = false;
   public TField AT;
   public byte AU = 0;
   public int AV = 0;
   private int AA = 0;
   private byte AB = 0;
   public Scroll AW = new Scroll();
   public boolean isNotReply = false;
   public boolean isGiftMail = false;
   public boolean isClaimed = false;
   public int mailId = -1;
   public String mailTitle = "";
   public byte typeMail = 0;
   public Item_Drop[] mItemgift;
   public iCommand cmdNhanQua;
   public iCommand cmdDelMail;

   // Fields for Temporary Invitations (Băng, PvP, Giao dịch, Bạn bè, Nhóm, Bái sư,...)
   public boolean isInvite = false;
   public byte typeInvite = -1;
   public int inviteId = -1;
   public String inviteName = "";
   public String inviteInfo = "";
   public int priceFight = 0;
   public int typeFight = 0;
   public iCommand cmdAcceptInvite;
   public iCommand cmdDeclineInvite;
   public iCommand cmdInfoEnemy;

   public static final byte CAT_MAIL = 0;
   public static final byte CAT_WORLD = 1;
   public static final byte CAT_PUBLIC = 2;
   public static final byte CAT_PRIVATE = 3;
   public static final byte CAT_CLAN = 4;
   public static final byte CAT_SYSTEM = 5;

   public byte typeCategory = CAT_PRIVATE;

   public MarqueeText marqueeTitle;

   public void updateMarquee(int maxW, mFont font) {
      String title = (this.mailTitle != null && this.mailTitle.length() > 0) ? this.mailTitle : this.AO;
      if (title == null || title.length() == 0) return;

      if (this.marqueeTitle == null || this.marqueeTitle.maxW != maxW || !this.marqueeTitle.text.equals(title) || this.marqueeTitle.fontPaint != font) {
         this.marqueeTitle = new MarqueeText(maxW);
         this.marqueeTitle.speed = 1;
         this.marqueeTitle.setdata(title, font);
      }
      this.marqueeTitle.update();
   }

   public void setMailGift(int mailId, String title, byte isClaimed, byte typeMail, Item_Drop[] gifts, boolean notReply) {
      this.typeCategory = CAT_MAIL;
      this.isInvite = false;
      this.mailId = mailId;
      this.mailTitle = (title != null && title.length() > 0) ? title : this.AO;
      this.isClaimed = (isClaimed == 1);
      this.typeMail = typeMail;
      this.mItemgift = gifts;
      this.isGiftMail = (gifts != null && gifts.length > 0);
      this.isNotReply = notReply;
      if (notReply) {
         this.AT = null;
      }
      this.cmdNhanQua = new iCommand(T.nhanqua, 10, this);
      this.cmdDelMail = new iCommand(T.del, 11, this);
      this.marqueeTitle = null;
   }

   public void setInvite(int inviteId, byte typeInvite, String inviteName, String inviteInfo, int priceFight, int typeFight) {
      this.typeCategory = CAT_MAIL;
      this.isInvite = true;
      this.isGiftMail = false;
      this.inviteId = inviteId;
      this.typeInvite = typeInvite;
      this.inviteName = (inviteName != null) ? inviteName : "";
      this.inviteInfo = (inviteInfo != null) ? inviteInfo : "";
      this.priceFight = priceFight;
      this.typeFight = typeFight;
      this.isNotReply = true;
      this.AT = null;
      this.marqueeTitle = null;

      String tagPrefix = "[Lời Mời]";
      switch (typeInvite) {
         case 0: // Kết bạn
            tagPrefix = "[Bạn]";
            break;
         case 1: // Nhóm
            tagPrefix = "[Nhóm]";
            break;
         case 3: // Thách đấu
            tagPrefix = "[Đấu]";
            break;
         case 4: // Giao dịch
            tagPrefix = "[G.Dịch]";
            break;
         case 5: // Xin vào nhóm
            tagPrefix = "[Xin Nhóm]";
            break;
         case 6: // Vào Băng
            tagPrefix = "[Băng]";
            break;
         case 7: // Bang chiến
            tagPrefix = "[Bang Chiến]";
            break;
         case 8: // Bái sư
            tagPrefix = "[Bái Sư]";
            break;
      }
      this.mailTitle = tagPrefix + " " + this.inviteName;

      this.cmdAcceptInvite = new iCommand((T.chapnhan != null && T.chapnhan.length() > 0) ? T.chapnhan : "Chấp nhận", 20, this);
      this.cmdDeclineInvite = new iCommand("Từ chối", 21, this);
      if (typeInvite == 3) {
         this.cmdInfoEnemy = new iCommand((T.AZ != null && T.AZ.length() > 0) ? T.AZ : "Xem TT", 22, this);
      }

      this.AN.removeAllElements();
      String detailMsg = "";
      switch (typeInvite) {
         case 0:
            detailMsg = this.inviteName + " " + T.DN;
            break;
         case 1:
            detailMsg = this.inviteName + T.CI;
            break;
         case 3:
            if (typeFight == 1) {
               detailMsg = this.inviteName + " thách đấu bạn 1vs1. Phí " + priceFight + " ruby cho thông báo người thua chịu.";
            } else {
               detailMsg = this.inviteName + " muốn mời bạn vào 1 trận đấu 1vs1. Mức cược " + priceFight + ".";
            }
            break;
         case 4:
            detailMsg = this.inviteName + " " + T.yeucaugiaodich;
            break;
         case 5:
            detailMsg = this.inviteName + " xin gia nhập vào nhóm của bạn.";
            break;
         case 6:
            detailMsg = this.inviteName + T.NP;
            break;
         case 7:
            detailMsg = this.inviteName + T.SK;
            break;
         case 8:
            detailMsg = this.inviteName + T.VR;
            break;
         default:
            detailMsg = this.inviteName + (this.inviteInfo.length() > 0 ? (": " + this.inviteInfo) : " gửi lời mời đến bạn.");
            break;
      }

      if (detailMsg.length() > 0) {
         this.AA(detailMsg, this.inviteName, 0);
      }
   }

   public void commandPointer(int index, int subIndex) {
      if (index == 10) {
         if (this.mailId > 0 && !this.isClaimed) {
            GlobalService.getInstance().claimMailGift(this.mailId);
         }
      } else if (index == 11) {
         if (this.mailId > 0) {
            GlobalService.getInstance().deleteMail(this.mailId);
         }
      } else if (index == 20) {
         this.acceptInvite();
      } else if (index == 21) {
         this.declineInvite();
      } else if (index == 22) {
         if (this.inviteName != null && this.inviteName.length() > 0) {
            GameScreen.AA(this.inviteName);
            InfoMemList mem = InfoMemList.AA(this.inviteName, (byte)3);
            if (mem != null) {
               MsgOtherCharInfo.AA = mem;
            }
         }
      }
   }

   public void acceptInvite() {
      if (this.inviteId != -1) {
         switch (this.typeInvite) {
            case 0: // Kết bạn
               GlobalService.getInstance().AA((byte)3, (int)this.inviteId);
               break;
            case 1: // Vào nhóm
               GlobalService.getInstance().AD((byte)4, (short)this.inviteId);
               break;
            case 3: // Thách đấu PvP
               GlobalService.getInstance().AA((byte)1, (short)this.inviteId, (byte)this.typeFight);
               break;
            case 4: // Giao dịch
               GlobalService.getInstance().AA((byte)6, (short)this.inviteId, (byte)0, 1, "");
               break;
            case 5: // Xin vào nhóm
               GlobalService.getInstance().AD((byte)6, (short)this.inviteId);
               break;
            case 6: // Lời mời vào Băng
               GlobalService.getInstance().Clan_CMD((byte)12, "", (short)this.inviteId, (byte)0);
               break;
            case 7: // Thách đấu Bang chiến
               GlobalService.getInstance().AD((byte)1, (short)this.inviteId, (byte)this.typeFight);
               break;
            case 8: // Bái sư
               GlobalService.getInstance().AB((byte)18, this.inviteName, (short)this.inviteId, (byte)0);
               break;
         }
      }
      if (GameCanvas.chatTabScr != null) {
         GameCanvas.chatTabScr.removeInvite(this.inviteId, this.typeInvite);
      }
      if (Player.vecEvent != null) {
         for (int i = 0; i < Player.vecEvent.size(); ++i) {
            InfoMemList mem = (InfoMemList) Player.vecEvent.elementAt(i);
            if (mem != null && mem.AG == this.inviteId && mem.AQ == this.typeInvite) {
               Player.vecEvent.removeElementAt(i);
               break;
            }
         }
      }
      GameScreen.AF();
   }

   public void declineInvite() {
      if (this.inviteId != -1) {
         if (this.typeInvite == 7) {
            GlobalService.getInstance().AD((byte)2, (short)this.inviteId, (byte)this.typeFight);
         }
      }
      if (GameCanvas.chatTabScr != null) {
         GameCanvas.chatTabScr.removeInvite(this.inviteId, this.typeInvite);
      }
      if (Player.vecEvent != null) {
         for (int i = 0; i < Player.vecEvent.size(); ++i) {
            InfoMemList mem = (InfoMemList) Player.vecEvent.elementAt(i);
            if (mem != null && mem.AG == this.inviteId && mem.AQ == this.typeInvite) {
               Player.vecEvent.removeElementAt(i);
               break;
            }
         }
      }
      GameScreen.AF();
   }

   public ChatDetail(String var1, byte var2, byte typeCategory) {
      this(var1, var2);
      this.typeCategory = typeCategory;
   }

   public ChatDetail(String var1, byte var2) {
      this.AO = var1;
      this.AU = var2;
      if (var2 == 1 || ChatTabScreen.isSystemTab(var1)) {
         this.typeCategory = CAT_SYSTEM;
      } else if (var2 == 3 || ChatTabScreen.isClanTab(var1)) {
         this.typeCategory = CAT_CLAN;
      } else if (ChatTabScreen.isWorldTab(var1)) {
         this.typeCategory = CAT_WORLD;
      } else if (ChatTabScreen.isPublicTab(var1)) {
         this.typeCategory = CAT_PUBLIC;
      } else {
         this.typeCategory = CAT_PRIVATE;
      }
      this.AP = var1;
      if (var1.length() > 8) {
         this.AP = var1.substring(0, 7) + "..";
      }

      this.AQ = var1;
      if (var1.length() > 14) {
         this.AQ = var1.substring(0, 13) + "..";
      }

      int scrXBe = (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AF : 0;
      int scrYBe = (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AG : 0;
      int scrHCon = (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AI : 180;
      int scrWCon = (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AH : 200;
      int scrMini = (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AE : 5;

      if (this.AU == 0) {
         this.AT = new TField(scrXBe, scrYBe + scrHCon - TField.AB() - scrMini / 2, scrWCon);
         this.AT.AI = false;
      } else if (this.AU == 2) {
         this.AO = T.CN;
      }

      if (var1.compareTo(T.CQ) != 0 && var1.compareTo(T.CR) != 0 && var1.compareTo(T.CS) != 0) {
         this.AB = 0;
      } else {
         this.AB = 1;
      }

      this.AW.setInfo(scrXBe + scrWCon + (scrMini << 1), scrYBe, scrHCon, -7967666);
   }

   public final void AA(String var1, String var2, int var3) {
      if (var1.length() > 0) {
         int splitW = (GameCanvas.chatTabScr != null && GameCanvas.chatTabScr.wRight > 50)
            ? ((this.typeCategory == CAT_MAIL || this.typeCategory == CAT_PRIVATE) ? (GameCanvas.chatTabScr.wRight - 20) : (GameCanvas.chatTabScr.AH - 12))
            : 200;
         String[] var4 = mFont.tahoma_7_white.splitFontArray(var1, splitW);
         byte var5;
         if (var3 >= 0) {
            var5 = (byte)var3;
         } else {
            var5 = this.AB(var2);
         }

         mSystem[] var6;
         if ((var6 = AA(var4, var5)) != null) {
            boolean isMe = (var2 != null && GameScreen.player != null && var2.compareTo(GameScreen.player.name) == 0);
            for(var3 = 0; var3 < var6.length; ++var3) {
               var6[var3].AF = isMe ? (byte)1 : (byte)0;
               this.AN.addElement(var6[var3]);
            }
            while (this.AN.size() > 50) {
               this.AN.removeElementAt(0);
            }
         }

         this.AG();
         if (this.AV > 0 && GameCanvas.chatTabScr != null && GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr.AN != null && GameCanvas.chatTabScr.AN == this) {
            GameCanvas.chatTabScr.AA(var4.length, (byte)1);
         }

         if ((GameCanvas.chatTabScr.AN != null && GameCanvas.chatTabScr.AN != this || GameCanvas.currentScreen != GameCanvas.chatTabScr) && this.AO.compareTo(T.CR) != 0) {
            this.AS = true;
            this.AR = (byte)CRes.random(1, 11);
            if (this.typeCategory == CAT_CLAN || ChatTabScreen.isClanTab(this.AO)) {
               Clan_Screen.AT = true;
            }
            GameScreen.AF();
         }
      }

   }

   public void AA(String var1) {
      String var2 = "";
      if (this.AT != null) {
         var2 = this.AT.getText();
      }

      if (var2 != null && var2.trim().length() > 0) {
         var2 = var2.trim();
         if (this.typeCategory == CAT_WORLD || ChatTabScreen.isWorldTab(this.AO)) {
            GlobalService.getInstance().chatKTG(var2);
         } else if (this.typeCategory == CAT_CLAN || ChatTabScreen.isClanTab(this.AO) || this.AO.compareTo(T.CQ) == 0 || this.AO.compareTo("Bang Hội") == 0 || this.AO.compareTo("Bang") == 0) {
            GlobalService.getInstance().Clan_CMD((byte)0, var2, 0, (byte)0);
         } else if (this.typeCategory == CAT_PUBLIC || this.AO.compareTo("Công Cộng") == 0) {
            if (GameScreen.player != null) {
               GameScreen.player.BC = var2;
            }
            if (GameCanvas.chatTabScr != null) {
               GameCanvas.chatTabScr.addNewChat("Công Cộng", "", (GameScreen.player != null ? GameScreen.player.name : "") + ": " + var2, (byte)0, false, -1, ChatDetail.CAT_PUBLIC);
            }
            GlobalService.getInstance().AA(var2);
         } else if (this.typeCategory == CAT_PRIVATE) {
            this.AA(var2, GameScreen.player != null ? GameScreen.player.name : "", 5);
            GlobalService.getInstance().AA(this.AO, var2);
         } else {
            this.AA(var2, GameScreen.player != null ? GameScreen.player.name : "", 5);
            GlobalService.getInstance().AA(this.AO, var2);
         }
      }

      if (this.AT != null) {
         this.AT.AB("");
      }
   }

   public boolean hasChat(short id, String name, String text) {
      if (this.AN == null || this.AN.size() == 0) {
         return false;
      }
      if (id > 0) {
         for (int i = 0; i < this.AN.size(); i++) {
            mSystem item = (mSystem)this.AN.elementAt(i);
            if (item != null && item.AL == id) {
               return true;
            }
         }
      }
      return false;
   }

   public final void AG() {
      int scrHCon = (this.AI > 0) ? this.AI : ((GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.AI : 180);
      int availableH = scrHCon - (this.AT != null ? TField.AB() + 2 : 0);
      int numRows = (this.mItemgift != null && this.mItemgift.length > 0) ? ((this.mItemgift.length + 1) / 2) : 0;
      int extraH = (numRows > 0) ? (numRows * 32 + 62) : 32;
      if (this.isInvite) {
         extraH = (this.typeInvite == 3) ? 75 : 45;
      }
      int totalH = (this.typeCategory == CAT_MAIL)
         ? ((this.AN.size() + 1) * GameCanvas.hText + extraH)
         : (this.AN.size() * GameCanvas.hText + 10);
      this.AV = totalH - availableH;
      if (this.AV < 0) {
         this.AV = 0;
      }
      if (this.AM != null) {
         this.AM.AD = this.AV;
      }
   }

   public static mSystem[] AA(String[] var0, byte var1) {
      if (var0 != null && var0.length != 0) {
         mSystem[] var2 = new mSystem[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2[var3] = new mSystem(var0[var3], var1);
         }

         return var2;
      } else {
         return null;
      }
   }

   private byte AB(String var1) {
      int var2;
      if (this.AB == 1) {
         var2 = this.AA % 2 == 0 ? 0 : (GameCanvas.IndexServer == 1 ? 1 : 5);
         ++this.AA;
      } else if (var1.compareTo(GameScreen.player.name) == 0) {
         var2 = 5;
      } else {
         if (GameCanvas.IndexServer == 1) {
            return 1;
         }

         var2 = 0;
      }

      return (byte)var2;
   }

   public void paint(mGraphics var1) {
      super.paint(var1);
   }

   public void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public void update() {
   }

   public void updatePointer() {
   }

   public void handleKeyPress() {
      super.handleKeyPress();
   }

   public void AA(short var1, String var2, String var3, String var4, byte var5, byte var6, short var7, long var8) {
   }

   public final void AA(int var1, byte var2) {
      int var3 = this.AI;
      if (this.AT != null) {
         var3 -= this.AT.AD;
      }

      if (var2 != 1) {
         if (var2 == 0) {
            this.AM = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.size() * GameCanvas.hText - var3, true);
            this.AM.AA(this.AM.AD);
            this.AM.AC = this.AM.AD;
            this.AL = this.AN.size() - 1;
         }

      } else {
         byte var4;
         int var5;
         if ((var5 = this.AM.AB) != 0 && var5 != this.AM.AD) {
            if (var5 < this.AM.AD - this.AI) {
               var4 = 1;
            } else {
               var4 = 2;
            }
         } else {
            var4 = 0;
         }

         if (this.AM == null) {
            this.AM = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.size() * GameCanvas.hText - var3, true);
         } else {
            this.AM.AD = this.AN.size() * GameCanvas.hText - var3;
            if (this.AM.AD < 0) {
               this.AM.AD = 0;
            }
         }

         if (var4 == 0) {
            this.AM.AA(this.AM.AD);
         } else if (var4 == 1) {
            this.AM.AA(var5);
            this.AM.AC = var5;
         } else {
            this.AM.AA(var5 + var1 * GameCanvas.hText);
         }
      }
   }

   public void f_() {
      if (this.AT != null && this.AT.getText().length() > 0 && this.AU == 3) {
         GlobalService.getInstance().Clan_CMD((byte)0, this.AT.getText(), 0, (byte)0);
         this.AT.AB("");
      }

   }

   public void AA() {
   }

   public final void AA(mGraphics var1, byte var2, byte var3, int var4, int var5, int var6, boolean var7) {
      if (!GameCanvas.isKeyPressed()) {
         var7 = false;
      }

      if (var2 < 0 || var2 > 9) {
         var2 = 4;
      }

      int var10001;
      switch(var2) {
      case 0:
         var10001 = -10250290;
         break;
      case 1:
         var10001 = -8147265;
         break;
      case 2:
         var10001 = -1942704;
         break;
      case 3:
         var10001 = -1930179;
         break;
      case 4:
         var10001 = -4683678;
         break;
      case 5:
         var10001 = -8009115;
         break;
      case 6:
         var10001 = -336031;
         break;
      case 7:
         var10001 = -2764079;
         break;
      case 8:
         var10001 = -3704225;
         break;
      case 9:
         var10001 = -986896;
         break;
      default:
         var10001 = -4683678;
      }

      var1.setColor(var10001);
      int var8 = this.AF - 2 + var4;
      int var9 = var6 - 1;
      var4 = this.AH + 4 - (var4 << 1);
      var1.fillRect(var8, var9, var4, var5);
      if (var7 && GameCanvas.gameTick % 12 < 6) {
         var2 = 10;
      }

      switch(var2) {
      case 0:
         var10001 = -14131303;
         break;
      case 1:
         var10001 = -12750201;
         break;
      case 2:
         var10001 = -6345178;
         break;
      case 3:
         var10001 = -6203113;
         break;
      case 4:
         var10001 = -7708871;
         break;
      case 5:
         var10001 = -10970049;
         break;
      case 6:
         var10001 = -2183124;
         break;
      case 7:
         var10001 = -8027262;
         break;
      case 8:
         var10001 = -6598604;
         break;
      case 9:
         var10001 = -5131855;
         break;
      case 10:
         var10001 = -16777216;
         break;
      default:
         var10001 = -7708871;
      }

      var1.setColor(var10001);
      AvMain.fraBorderClan.drawFrame(var2 << 2, var8, var9, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 1, var8 - this.AE + var4 + 1, var9, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 2, var8, var9 + var5 - 4, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 3, var8 - this.AE + var4 + 1, var9 + var5 - 4, 0, 0, var1);
      var1.fillRect(var8, var6 + 3, 1, var5 - 8);
      var1.fillRect(var8 - 1 + var4, var6 + 3, 1, var5 - 8);
      var1.fillRect(var8 + 4, var9, var4 - 8, 1);
      var1.fillRect(var8 + 4, var6 + var5 - 2, var4 - 8, 1);
      if (var3 == 1) {
         var1.drawRegion(AvMain.imgChatClan, 0, 7, 7, 7, 0, var8 - 6, var9 + 12, 0);
      } else {
         if (var3 == 2) {
            var1.drawRegion(AvMain.imgChatClan, 0, 0, 7, 7, 0, var8 + var4 - 1, var9 + 12, 0);
         }

      }
   }
}
