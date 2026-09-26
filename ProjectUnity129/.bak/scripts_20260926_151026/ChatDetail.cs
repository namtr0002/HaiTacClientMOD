public class ChatDetail : AvMain
{
	public int x;

	public int y;

	public int miniItem = 5;

	public int xBe;

	public int yBe;

	public int wCon;

	public int hCon;

	public int wItem;

	public int hChat;

	public int idSelect;

	public ListNew CamDetailChat;

	public mVector vecDetail = new mVector("ChatDetail.vecDetail");

	public string name;

	public string friend;

	public string shortName;

	public string shortNameFocus;

	public sbyte timeNew = -1;

	public bool isNew;

	public TField tfchat;

	public sbyte typeChat;

	public const sbyte TYPE_CHAT = 0;

	public const sbyte TYPE_SERVER = 1;

	public const sbyte TYPE_ADDFRIEND = 2;

	public const sbyte TYPE_CLAN_CHAT = 3;

	public const sbyte TYPE_CLAN_MEM = 4;

	public const sbyte TYPE_CLAN_INFO = 5;

	public int limY;

	private int indexColor;

	private sbyte typeColorChat;

	public const sbyte TYPE_TROCHUYEN = 0;

	public const sbyte TYPE_BANGHOI_NHOM = 1;

	public Scroll scrChat = new Scroll();

	public const sbyte BG_BLUE_DARK = 0;

	public const sbyte BG_BLUE = 1;

	public const sbyte BG_RED = 2;

	public const sbyte BG_ORANGE = 3;

	public const sbyte BG_BROWN = 4;

	public const sbyte BG_GREEN = 5;

	public const sbyte BG_GOLD = 6;

	public const sbyte BG_SILVER = 7;

	public const sbyte BG_COPPER = 8;

	public const sbyte BG_WHITE = 9;

	public const sbyte BG_FOCUS = 10;

	public const sbyte CAT_MAIL = 0;
	public const sbyte CAT_WORLD = 1;
	public const sbyte CAT_PUBLIC = 2;
	public const sbyte CAT_PRIVATE = 3;
	public const sbyte CAT_CLAN = 4;
	public const sbyte CAT_SYSTEM = 5;

	public sbyte typeCategory = CAT_PRIVATE;

	public bool isNotReply = false;
	public bool isGiftMail = false;
	public bool isClaimed = false;
	public int mailId = -1;
	public string mailTitle = "";
	public sbyte typeMail = 0;
	public Item_Drop[] mItemgift;
	public iCommand cmdNhanQua;
	public iCommand cmdDelMail;
	public MarqueeText marqueeTitle;

	// Fields for Temporary Invitations (Băng, PvP, Giao dịch, Bạn bè, Nhóm, Bái sư,...)
	public bool isInvite = false;
	public sbyte typeInvite = -1;
	public int inviteId = -1;
	public string inviteName = "";
	public string inviteInfo = "";
	public int priceFight = 0;
	public sbyte typeFight = 0;
	public iCommand cmdAcceptInvite;
	public iCommand cmdDeclineInvite;
	public iCommand cmdInfoEnemy;

	// Cache & Search fields
	public string contentRaw = "";
	public long receivedTime = 0L;

	public ChatDetail(string name, sbyte type, sbyte typeCategory) : this(name, type)
	{
		this.typeCategory = typeCategory;
	}

	public ChatDetail(string name, sbyte type)
	{
		this.name = name;
		typeChat = type;
		shortName = name;
		if (name.Length >= 5)
		{
			shortName = name.Substring(0, 5);
		}
		shortNameFocus = name;
		if (name.Length >= 10)
		{
			shortNameFocus = name.Substring(0, 9) + "...";
		}
		if (typeChat == 0)
		{
			tfchat = new TField(GameCanvas.chatTabScr.xBe, GameCanvas.chatTabScr.yBe + GameCanvas.chatTabScr.hCon - TField.getHeight() - GameCanvas.chatTabScr.miniItem / 2, GameCanvas.chatTabScr.wCon);
			tfchat.isCloseKey = false;
		}
		else if (typeChat == 2)
		{
			friend = name;
			this.name = T.addFriend;
		}
		if (name.CompareTo(T.tabBangHoi) == 0 || name.CompareTo(T.tabServer) == 0 || name.CompareTo(T.tabBangChu) == 0)
		{
			typeColorChat = 1;
		}
		else
		{
			typeColorChat = 0;
		}
		scrChat.setInfo(GameCanvas.chatTabScr.xBe + GameCanvas.chatTabScr.wCon + GameCanvas.chatTabScr.miniItem * 2, GameCanvas.chatTabScr.yBe, GameCanvas.chatTabScr.hCon, 8809550);
	}

	public void addString(string str, string nametext, int color)
	{
		if (str.Length <= 0)
		{
			return;
		}
		string[] array = mFont.tahoma_7_white.splitFontArray(str, GameCanvas.chatTabScr.wCon);
		sbyte b = 0;
		b = ((color < 0) ? setColorText(nametext) : ((sbyte)color));
		MainTextChat[] array2 = addChatNew(array, b);
		if (array2 != null)
		{
			for (int i = 0; i < array2.Length; i++)
			{
				vecDetail.addElement(array2[i]);
			}
		}
		setLim();
		if (limY > 0 && GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr.tabCur != null && GameCanvas.chatTabScr.tabCur == this)
		{
			GameCanvas.chatTabScr.updateCameraNew(array.Length, 1);
		}
		if (((GameCanvas.chatTabScr.tabCur != null && GameCanvas.chatTabScr.tabCur != this) || GameCanvas.currentScreen != GameCanvas.chatTabScr) && name.CompareTo(T.tabServer) != 0)
		{
			isNew = true;
			timeNew = (sbyte)CRes.random(1, 11);
		}
	}

	public void addStartChat(string nametext)
	{
		string text = "";
		if (tfchat != null)
		{
			text = tfchat.getText();
		}
		if (text.Length > 0)
		{
			text = text.Trim();
			if (text.Equals("/lv5", System.StringComparison.OrdinalIgnoreCase) || text.Equals("/cap5", System.StringComparison.OrdinalIgnoreCase) || text.Equals("lv5", System.StringComparison.OrdinalIgnoreCase) || text.Equals("cap 5", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 5;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: CẤP = 5 (Hoàng Kim Thần Thoại)", mFont.tahoma_7_yellow);
				if (tfchat != null) tfchat.setText("");
				return;
			}
			if (text.Equals("/lv1", System.StringComparison.OrdinalIgnoreCase) || text.Equals("/cap1", System.StringComparison.OrdinalIgnoreCase) || text.Equals("lv1", System.StringComparison.OrdinalIgnoreCase) || text.Equals("cap 1", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 1;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: CẤP < 5 (Lam Hỏa Tiêu Chuẩn)", mFont.tahoma_7_yellow);
				if (tfchat != null) tfchat.setText("");
				return;
			}
			if (text.Equals("/auto", System.StringComparison.OrdinalIgnoreCase) || text.Equals("/lv0", System.StringComparison.OrdinalIgnoreCase) || text.Equals("auto", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 0;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: TỰ ĐỘNG THEO SKILL", mFont.tahoma_7_yellow);
				if (tfchat != null) tfchat.setText("");
				return;
			}
			if (typeCategory == CAT_WORLD || ChatTabScreen.isWorldTab(name))
			{
				GlobalService.gI().World_Chanel(0, text);
			}
			else if (typeCategory == CAT_CLAN || ChatTabScreen.isClanTab(name) || name.Equals(T.tabBangHoi) || name.Equals("Bang Hội") || name.Equals("Bang"))
			{
				GlobalService.gI().Clan_CMD(0, text, 0, 0);
			}
			else if (typeCategory == CAT_PUBLIC || ChatTabScreen.isPublicTab(name) || name.Equals("Công Cộng"))
			{
				GlobalService.gI().chatPopup(text);
			}
			else
			{
				string[] array = mFont.tahoma_7_white.splitFontArray(GameScreen.player.name + ": " + text, GameCanvas.chatTabScr.wCon);
				MainTextChat[] array2 = addChatNew(array, setColorText(nametext));
				if (array2 != null)
				{
					for (int i = 0; i < array2.Length; i++)
					{
						vecDetail.addElement(array2[i]);
					}
				}
				setLim();
				if (GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr.tabCur != null && GameCanvas.chatTabScr.tabCur == this)
				{
					GameCanvas.chatTabScr.updateCameraNew(array.Length, 1);
				}
				GlobalService.gI().chatTab(name, text);
			}
		}
		if (tfchat != null)
		{
			tfchat.setText("");
		}
	}

	public void addNewChat(string text, string nametext, int color)
	{
		if (string.IsNullOrEmpty(text)) return;
		sbyte c = (color != -1) ? (sbyte)color : setColorText(nametext);
		string[] array = mFont.tahoma_7_white.splitFontArray(text, (GameCanvas.chatTabScr != null) ? GameCanvas.chatTabScr.wCon : 200);
		MainTextChat[] array2 = addChatNew(array, c);
		if (array2 != null)
		{
			for (int i = 0; i < array2.Length; i++)
			{
				vecDetail.addElement(array2[i]);
			}
		}
		setLim();
		if (GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr != null && GameCanvas.chatTabScr.tabCur == this)
		{
			GameCanvas.chatTabScr.updateCameraNew(array.Length, 1);
		}
	}

	public bool isMatchSearch(string query)
	{
		if (string.IsNullOrEmpty(query)) return true;
		string q = query.Trim().ToLower();
		if (!string.IsNullOrEmpty(name) && name.ToLower().Contains(q)) return true;
		if (!string.IsNullOrEmpty(mailTitle) && mailTitle.ToLower().Contains(q)) return true;
		if (!string.IsNullOrEmpty(inviteName) && inviteName.ToLower().Contains(q)) return true;
		if (!string.IsNullOrEmpty(inviteInfo) && inviteInfo.ToLower().Contains(q)) return true;
		if (!string.IsNullOrEmpty(contentRaw) && contentRaw.ToLower().Contains(q)) return true;
		return false;
	}

	public bool isInCategoryFilter(int subFilter)
	{
		if (subFilter == 0) return true; // Tất cả
		if (subFilter == 1) return isInvite; // Lời mời
		if (subFilter == 2) return isGiftMail; // Quà
		if (subFilter == 3) return !isInvite && !isGiftMail; // Hệ thống
		return true;
	}

	public void updateMarquee(int maxW, mFont font)
	{
		string title = (!string.IsNullOrEmpty(mailTitle)) ? mailTitle : name;
		if (string.IsNullOrEmpty(title)) return;

		if (marqueeTitle == null || marqueeTitle.maxW != maxW || marqueeTitle.text != title || marqueeTitle.fontPaint != font)
		{
			marqueeTitle = new MarqueeText(maxW);
			marqueeTitle.speed = 1;
			marqueeTitle.setdata(title, font);
		}
		marqueeTitle.update();
	}

	public void setMailGift(int mailId, string title, sbyte isClaimed, sbyte typeMail, Item_Drop[] gifts, bool notReply)
	{
		this.typeCategory = CAT_MAIL;
		this.isInvite = false;
		this.mailId = mailId;
		this.mailTitle = (!string.IsNullOrEmpty(title)) ? title : this.name;
		this.isClaimed = (isClaimed == 1);
		this.typeMail = typeMail;
		this.mItemgift = gifts;
		this.isGiftMail = (gifts != null && gifts.Length > 0);
		this.isNotReply = notReply;
		this.receivedTime = mSystem.currentTimeMillis();
		if (notReply)
		{
			this.tfchat = null;
		}
		this.cmdNhanQua = new iCommand((!string.IsNullOrEmpty(T.nhanqua)) ? T.nhanqua : "Nhận quà", 10, this);
		this.cmdDelMail = new iCommand((!string.IsNullOrEmpty(T.del)) ? T.del : "Xóa thư", 11, this);
		this.marqueeTitle = null;
	}

	public void setInvite(int inviteId, sbyte typeInvite, string inviteName, string inviteInfo, int priceFight, int typeFight)
	{
		this.typeCategory = CAT_MAIL;
		this.isInvite = true;
		this.isGiftMail = false;
		this.inviteId = inviteId;
		this.typeInvite = typeInvite;
		this.inviteName = (!string.IsNullOrEmpty(inviteName)) ? inviteName : "";
		this.inviteInfo = (!string.IsNullOrEmpty(inviteInfo)) ? inviteInfo : "";
		this.priceFight = priceFight;
		this.typeFight = (sbyte)typeFight;
		this.isNotReply = true;
		this.receivedTime = mSystem.currentTimeMillis();
		this.tfchat = null;
		this.marqueeTitle = null;

		string tagPrefix = "[Lời Mời]";
		switch (typeInvite)
		{
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

		this.cmdAcceptInvite = new iCommand((!string.IsNullOrEmpty(T.chapnhan)) ? T.chapnhan : "Chấp nhận", 20, this);
		this.cmdDeclineInvite = new iCommand("Từ chối", 21, this);
		if (typeInvite == 3)
		{
			this.cmdInfoEnemy = new iCommand((!string.IsNullOrEmpty(T.info)) ? T.info : "Xem TT", 22, this);
		}

		this.vecDetail.removeAllElements();
		string detailMsg = "";
		switch (typeInvite)
		{
		case 0:
			detailMsg = this.inviteName + " " + T.eventAddFriend;
			break;
		case 1:
			detailMsg = this.inviteName + T.eventParty;
			break;
		case 3:
			if (typeFight == 1)
			{
				detailMsg = this.inviteName + " thách đấu bạn 1vs1. Phí " + priceFight + " ruby cho thông báo người thua chịu.";
			}
			else
			{
				detailMsg = this.inviteName + " muốn mời bạn vào 1 trận đấu 1vs1. Mức cược " + priceFight + ".";
			}
			break;
		case 4:
			detailMsg = this.inviteName + " " + T.eventTrade;
			break;
		case 5:
			detailMsg = this.inviteName + " xin gia nhập vào nhóm của bạn.";
			break;
		case 6:
			detailMsg = this.inviteName + T.eventMoiClan;
			break;
		case 7:
			detailMsg = this.inviteName + T.eventClanFight;
			break;
		case 8:
			detailMsg = this.inviteName + T.baisu;
			break;
		default:
			detailMsg = this.inviteName + ((this.inviteInfo.Length > 0) ? (": " + this.inviteInfo) : " gửi lời mời đến bạn.");
			break;
		}

		this.contentRaw = detailMsg;
		if (detailMsg.Length > 0)
		{
			this.addNewChat(detailMsg, this.inviteName, -1);
		}
	}

	public void setLim()
	{
		limY = vecDetail.size() * GameCanvas.hText - (GameCanvas.chatTabScr.hCon - ((typeChat == 0) ? (TField.getHeight() + 2) : 0));
		if (limY < 0)
		{
			limY = 0;
		}
	}

	public MainTextChat[] addChatNew(string[] mstr, sbyte color)
	{
		if (mstr == null || mstr.Length == 0)
		{
			return null;
		}
		MainTextChat[] array = new MainTextChat[mstr.Length];
		for (int i = 0; i < mstr.Length; i++)
		{
			array[i] = new MainTextChat(mstr[i], color);
		}
		return array;
	}

	private sbyte setColorText(string name)
	{
		sbyte b = 0;
		if (typeColorChat == 1)
		{
			b = (sbyte)((indexColor % 2 != 0) ? colorServer() : 0);
			indexColor++;
		}
		else if (name != null && GameScreen.player != null && GameScreen.player.name != null && name.CompareTo(GameScreen.player.name) == 0)
		{
			b = 5;
		}
		else
		{
			if (GameCanvas.IndexServer == 1)
			{
				return 1;
			}
			b = 0;
		}
		return b;
	}

	private sbyte colorServer()
	{
		if (GameCanvas.IndexServer == 1)
		{
			return 1;
		}
		return 5;
	}

	public override void paint(mGraphics g)
	{
		base.paint(g);
	}

	public virtual void setPos(int xBe, int yBe, int wCon, int hCon, int miniItem, int hchat)
	{
	}

	public override void update()
	{
	}

	public override void commandPointer(int index, int subIndex)
	{
		if (index == 10)
		{
			if (mailId > 0 && !isClaimed)
			{
				GlobalService.gI().claimMailGift(mailId);
			}
		}
		else if (index == 11)
		{
			if (mailId > 0)
			{
				GlobalService.gI().deleteMail(mailId);
				if (GameCanvas.chatTabScr != null)
				{
					GameCanvas.chatTabScr.removeMailTab(mailId);
				}
			}
		}
		else if (index == 20)
		{
			acceptInvite();
		}
		else if (index == 21)
		{
			declineInvite();
		}
		else if (index == 22)
		{
			if (!string.IsNullOrEmpty(inviteName))
			{
				GameCanvas.gameScr.ShowInfoOtherPlayer(inviteName);
			}
		}
		base.commandPointer(index, subIndex);
	}

	public void acceptInvite()
	{
		if (inviteId != -1)
		{
			switch (typeInvite)
			{
			case 0: // Kết bạn
				GlobalService.gI().Friend(3, inviteId);
				break;
			case 1: // Vào nhóm
				GlobalService.gI().Party(4, (short)inviteId);
				break;
			case 3: // Thách đấu PvP
				GlobalService.gI().Fight(1, (short)inviteId, typeFight);
				break;
			case 4: // Giao dịch
				GlobalService.gI().Trade(6, (short)inviteId, 0, 1, "");
				break;
			case 5: // Xin vào nhóm
				GlobalService.gI().Party(6, (short)inviteId);
				break;
			case 6: // Lời mời vào Băng
				GlobalService.gI().Clan_CMD(12, "", (short)inviteId, 0);
				break;
			case 7: // Thách đấu Bang chiến
				GlobalService.gI().Clan_Fight(1, (short)inviteId, typeFight);
				break;
			case 8: // Bái sư
				GlobalService.gI().Sudo_CMD(18, inviteName, (short)inviteId, 0);
				break;
			}
		}
		if (GameCanvas.chatTabScr != null)
		{
			GameCanvas.chatTabScr.removeInvite(inviteId, typeInvite);
		}
		if (Player.vecEvent != null)
		{
			for (int i = 0; i < Player.vecEvent.size(); i++)
			{
				InfoMemList mem = (InfoMemList)Player.vecEvent.elementAt(i);
				if (mem != null && mem.id == inviteId && mem.typeEvent == typeInvite)
				{
					Player.vecEvent.removeElementAt(i);
					break;
				}
			}
		}
		GameScreen.setNumMess();
	}

	public void declineInvite()
	{
		if (inviteId != -1)
		{
			if (typeInvite == 7)
			{
				GlobalService.gI().Clan_Fight(2, (short)inviteId, typeFight);
			}
		}
		if (GameCanvas.chatTabScr != null)
		{
			GameCanvas.chatTabScr.removeInvite(inviteId, typeInvite);
		}
		if (Player.vecEvent != null)
		{
			for (int i = 0; i < Player.vecEvent.size(); i++)
			{
				InfoMemList mem = (InfoMemList)Player.vecEvent.elementAt(i);
				if (mem != null && mem.id == inviteId && mem.typeEvent == typeInvite)
				{
					Player.vecEvent.removeElementAt(i);
					break;
				}
			}
		}
		GameScreen.setNumMess();
	}

	public override void updatekey()
	{
		base.updatekey();
	}

	public virtual void addStringClan(short ID, string str, string nametext, string textRight, sbyte typeBg, sbyte IDEvent, short IdMem, long time)
	{
	}

	public virtual void updateCameraNew(int size, sbyte type)
	{
		int num = hCon;
		if (tfchat != null)
		{
			num -= tfchat.height;
		}
		switch (type)
		{
		case 1:
		{
			int cmtoX = CamDetailChat.cmtoX;
			int num2 = 0;
			num2 = ((cmtoX != 0 && cmtoX != CamDetailChat.cmxLim) ? ((cmtoX < CamDetailChat.cmxLim - hCon) ? 1 : 2) : 0);
			if (CamDetailChat == null)
			{
				CamDetailChat = new ListNew(xBe, yBe, wCon, num, 0, 0, vecDetail.size() * GameCanvas.hText - num, isLim0: true);
			}
			else
			{
				CamDetailChat.cmxLim = vecDetail.size() * GameCanvas.hText - num;
				if (CamDetailChat.cmxLim < 0)
				{
					CamDetailChat.cmxLim = 0;
				}
			}
			switch (num2)
			{
			case 0:
				CamDetailChat.setToX(CamDetailChat.cmxLim);
				break;
			case 1:
				CamDetailChat.setToX(cmtoX);
				CamDetailChat.cmx = cmtoX;
				break;
			default:
				CamDetailChat.setToX(cmtoX + size * GameCanvas.hText);
				break;
			}
			break;
		}
		case 0:
			CamDetailChat = new ListNew(xBe, yBe, wCon, num, 0, 0, vecDetail.size() * GameCanvas.hText - num, isLim0: true);
			CamDetailChat.setToX(CamDetailChat.cmxLim);
			CamDetailChat.cmx = CamDetailChat.cmxLim;
			idSelect = vecDetail.size() - 1;
			break;
		}
	}

	public void sendChat()
	{
		if (tfchat != null && tfchat.getText().Length > 0 && typeChat == 3)
		{
			GlobalService.gI().Clan_CMD(0, tfchat.getText(), 0, 0);
			tfchat.setText("");
		}
	}

	public virtual void beginFocus()
	{
	}

	public void paintBorder(mGraphics g, sbyte typeColorBg, sbyte typeChatLeftRight, int wLech, int hpaint, int ypaint, bool isfocus)
	{
		if (!GameCanvas.isTouchNoOrPC())
		{
			isfocus = false;
		}
		if (typeColorBg < 0 || typeColorBg > 9)
		{
			typeColorBg = 4;
		}
		g.setColor(getColorBg(typeColorBg));
		int num = xBe - 2 + wLech;
		int num2 = ypaint - 1;
		int num3 = wCon + 4 - wLech * 2;
		g.fillRect(num, num2, num3, hpaint);
		if (isfocus && GameCanvas.gameTick % 12 < 6)
		{
			typeColorBg = 10;
		}
		g.setColor(getColorBorderBg(typeColorBg));
		AvMain.fraBorderClan.drawFrame(typeColorBg * 4, num, num2, 0, 0, g);
		AvMain.fraBorderClan.drawFrame(typeColorBg * 4 + 1, num - miniItem + num3 + 1, num2, 0, 0, g);
		AvMain.fraBorderClan.drawFrame(typeColorBg * 4 + 2, num, num2 + hpaint - 4, 0, 0, g);
		AvMain.fraBorderClan.drawFrame(typeColorBg * 4 + 3, num - miniItem + num3 + 1, num2 + hpaint - 4, 0, 0, g);
		g.fillRect(num, ypaint + 3, 1, hpaint - 8);
		g.fillRect(num - 1 + num3, ypaint + 3, 1, hpaint - 8);
		g.fillRect(num + 4, ypaint - 1, num3 - 8, 1);
		g.fillRect(num + 4, ypaint + hpaint - 2, num3 - 8, 1);
		switch (typeChatLeftRight)
		{
		case 1:
			g.drawRegion(AvMain.imgChatClan, 0, 7, 7, 7, 0, num - 6, num2 + 12, 0);
			break;
		case 2:
			g.drawRegion(AvMain.imgChatClan, 0, 0, 7, 7, 0, num + num3 - 1, num2 + 12, 0);
			break;
		}
	}

	public int getColorBg(sbyte typecolor)
	{
		return typecolor switch
		{
			0 => 6526926, 
			1 => 8629951, 
			2 => 14834512, 
			3 => 14847037, 
			4 => 12093538, 
			5 => 8768101, 
			6 => 16441185, 
			7 => 14013137, 
			8 => 13072991, 
			9 => 15790320, 
			_ => 12093538, 
		};
	}

	public int getColorBorderBg(sbyte typecolor)
	{
		return typecolor switch
		{
			0 => 2645913, 
			1 => 4027015, 
			2 => 10432038, 
			3 => 10574103, 
			4 => 9068345, 
			5 => 5807167, 
			6 => 14594092, 
			7 => 8749954, 
			8 => 10178612, 
			9 => 11645361, 
			10 => 0, 
			_ => 9068345, 
		};
	}

	public int getColorBorderNumber(sbyte typecolor)
	{
		return typecolor switch
		{
			5 => 4239412, 
			4 => 9986635, 
			_ => 9986635, 
		};
	}

	public virtual void setIsNew(bool isnew)
	{
		isNew = isnew;
	}
}
