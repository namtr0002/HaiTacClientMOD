using System;
using UnityEngine;

public class ChatTabScreen : MainScreen
{
	public int x;
	public int y;
	public int w = 225;
	public int h = 194;
	public int wCon = 225;
	public int hCon = 194;
	public int miniItem = 5;
	public int xBe;
	public int yBe;
	public int wItem;
	public int hItem;
	public int wPaintTab;
	public int hChat;
	public mVector vecTabChat = new mVector("ChatTabScreen.vecTabChat");
	public ChatDetail tabCur;
	public int idSelect;

	public int catSelect = 0; // 0: Hộp Thư, 1: Thế Giới, 2: Công Cộng, 3: Trò Chuyện, 4: Bang Hội, 5: Hệ Thống
	public string[] mCatNames = new string[] { "Hộp Thư", "Thế Giới", "Công Cộng", "Trò Chuyện", "Bang Hội", "Hệ Thống" };
	public int wLeft;
	public int xRight;
	public int wRight;

	// Phân loại con cho Hộp Thư (Sub-filters)
	public int subFilterMail = 0; // 0: Tất cả, 1: Lời mời, 2: Quà thư, 3: Hệ thống
	public string[] mSubFilterNames = new string[] { "Tất cả", "Lời mời", "Quà", "Hệ thống" };

	// Tìm kiếm cho Hộp Thư
	public TField tfSearchMail;

	// Phân trang cho Hộp Thư
	public const int ITEMS_PER_PAGE = 10;
	public int curPageMail = 0;
	public int totalPagesMail = 1;
	public ListNew camPageTabs = new ListNew();

	// Hỗ trợ màn hình bé vs to (Responsive Multi-Screen)
	public bool isSmallScreen = false;
	public sbyte smallScreenState = 0; // 0: Danh sách (List), 1: Xem chi tiết (Detail)
	public iCommand cmdBackToList;

	// Tracking tải cache RMS
	public bool hasLoadedCache = false;

	// GC Optimization: Reusable vectors to prevent per-frame heap allocations
	private mVector[] cachedCatLists = new mVector[] {
		new mVector(), new mVector(), new mVector(), new mVector(), new mVector(), new mVector()
	};
	private mVector cachedFilteredMail = new mVector();
	private mVector cachedPageMailItems = new mVector();

	private int minChat;
	private int maxChat;
	private ListNew CamDetailChat;
	private ListNew CamTab;
	private ListNew CamLeftList;
	public Scroll scrLeftList = new Scroll();
	public iCommand cmdClose;
	private iCommand cmdCloseChat;
	private iCommand cmdChat;

	public ChatTabScreen()
	{
		CamDetailChat = new ListNew();
		CamTab = new ListNew();
		CamLeftList = new ListNew();
		camPageTabs = new ListNew();
		cmdClose = new iCommand(T.close, 0, this);
		backCMD = cmdClose;
		right = cmdClose;
		cmdCloseChat = new iCommand(T.close + " " + T.thistab, 1, this);
		new iCommand(T.close, 2, this);
		cmdChat = new iCommand(T.chat, 3, this);
		cmdBackToList = new iCommand("Quay lại", 4, this);
		if (this.GetType() == typeof(ChatTabScreen))
		{
			setPos();
		}
	}

	public static bool isWorldTab(string name)
	{
		if (string.IsNullOrEmpty(name)) return false;
		string lower = name.Trim().ToLower();
		return lower.Equals("thế giới") || lower.Equals("the gioi") || lower.Equals("thegioi")
			|| lower.Equals("ktg") || lower.Equals("world") || lower.Equals("loa")
			|| lower.Equals("kênh thế giới") || lower.Equals("kenh the gioi")
			|| lower.Equals("kênh the gioi") || lower.Equals("kenh thế giới")
			|| lower.Equals("global") || name.Equals("Thế Giới") || name.Equals("KTG");
	}

	public static bool isPublicTab(string name)
	{
		if (string.IsNullOrEmpty(name)) return false;
		string lower = name.Trim().ToLower();
		return lower.Equals("công cộng") || lower.Equals("cong cong") || lower.Equals("khu vực") || lower.Equals("khu vuc") || lower.Equals("map") || lower.Equals("public") || name.Equals("Công Cộng");
	}

	public static bool isClanTab(string name)
	{
		if (string.IsNullOrEmpty(name)) return false;
		string lower = name.Trim().ToLower();
		if (lower.Equals("bang hội") || lower.Equals("bang hoi") || lower.Equals("bang") || lower.Equals("bang chủ") || lower.Equals("nhóm"))
		{
			return true;
		}
		return name.Equals(T.tabBangHoi) || name.Equals(T.tabBangChu) || name.Equals(T.party) || name.Equals("Bang Hội") || name.Equals("Bang");
	}

	public static bool isSystemTab(string name)
	{
		if (string.IsNullOrEmpty(name)) return false;
		string lower = name.Trim().ToLower();
		if (lower.Equals("hệ thống") || lower.Equals("he thong") || lower.Equals("hethong")
			|| lower.Equals("thông báo") || lower.Equals("thong bao") || lower.Equals("thongbao")
			|| lower.Equals("admin") || lower.Equals("server") || lower.Equals("máy chủ")
			|| lower.Equals("boss") || lower.Equals("siêu boss") || lower.Equals("siêu trùm") || lower.Equals("trùm")
			|| lower.Equals("phó bản") || lower.Equals("pho ban") || lower.Equals("phó bảng")
			|| lower.Equals("sự kiện") || lower.Equals("su kien") || lower.Equals("hoạt động"))
		{
			return true;
		}
		return name.Equals(T.hethong) || name.Equals(T.tabThongBao) || name.Equals(T.cmdEvent)
			|| name.Equals(T.tabPhobang) || name.Equals(T.tabSieuBoss) || name.Equals(T.tabTestAdmin);
	}

	public static bool isMailTab(string name)
	{
		if (string.IsNullOrEmpty(name)) return false;
		return name.Equals("Hộp Thư") || name.Equals(T.thuhthong);
	}

	public static bool isPrivateTab(string name)
	{
		return !isWorldTab(name) && !isPublicTab(name) && !isClanTab(name) && !isSystemTab(name) && !isMailTab(name);
	}

	public void loadCacheIfNeeded()
	{
		if (!hasLoadedCache && GameScreen.player != null && !string.IsNullOrEmpty(GameScreen.player.name))
		{
			mVector cached = Rms.loadMailCache(GameScreen.player.name);
			if (cached != null && cached.size() > 0)
			{
				for (int i = 0; i < cached.size(); i++)
				{
					ChatDetail cItem = (ChatDetail)cached.elementAt(i);
					if (cItem == null) continue;
					bool exists = false;
					for (int j = 0; j < vecTabChat.size(); j++)
					{
						ChatDetail cur = (ChatDetail)vecTabChat.elementAt(j);
						if (cur == null) continue;
						if (cItem.isInvite && cur.isInvite && cur.inviteId == cItem.inviteId && cur.typeInvite == cItem.typeInvite)
						{
							exists = true;
							break;
						}
						if (!cItem.isInvite && !cur.isInvite && cur.mailId > 0 && cur.mailId == cItem.mailId)
						{
							exists = true;
							break;
						}
					}
					if (!exists)
					{
						vecTabChat.addElement(cItem);
					}
				}
			}
			hasLoadedCache = true;
		}
	}

	public void saveMailCacheNow()
	{
		if (GameScreen.player != null && !string.IsNullOrEmpty(GameScreen.player.name))
		{
			mVector mailList = getListByCat(ChatDetail.CAT_MAIL);
			Rms.saveMailCache(GameScreen.player.name, mailList);
		}
	}

	public void initDefaultTabs()
	{
		if (this.GetType() != typeof(ChatTabScreen))
		{
			return;
		}

		loadCacheIfNeeded();

		bool hasWorld = false;
		bool hasPublic = false;
		bool hasClan = false;
		bool hasSys = false;

		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d == null) continue;
			if (d.typeCategory == ChatDetail.CAT_WORLD) hasWorld = true;
			if (d.typeCategory == ChatDetail.CAT_PUBLIC) hasPublic = true;
			if (d.typeCategory == ChatDetail.CAT_CLAN) hasClan = true;
			if (d.typeCategory == ChatDetail.CAT_SYSTEM) hasSys = true;
		}

		if (!hasWorld)
		{
			ChatDetail worldTab = new ChatDetail("Thế Giới", 0, ChatDetail.CAT_WORLD);
			vecTabChat.addElement(worldTab);
		}
		if (!hasPublic)
		{
			ChatDetail publicTab = new ChatDetail("Công Cộng", 0, ChatDetail.CAT_PUBLIC);
			vecTabChat.addElement(publicTab);
		}
		if (!hasClan)
		{
			ChatDetail clanTab = new ChatDetail(T.tabBangHoi, 0, ChatDetail.CAT_CLAN);
			vecTabChat.addElement(clanTab);
		}
		if (!hasSys)
		{
			ChatDetail sysTab = new ChatDetail(T.hethong, 1, ChatDetail.CAT_SYSTEM);
			vecTabChat.addElement(sysTab);
		}
	}

	public virtual void setPos()
	{
		if (this.GetType() != typeof(ChatTabScreen))
		{
			return;
		}

		int maxW = MotherCanvas.w;
		int maxH = MotherCanvas.h;

		isSmallScreen = (maxW < 340 || maxH < 240);

		if (maxW <= 300)
		{
			w = maxW - 8;
			h = System.Math.Min(220, maxH - GameCanvas.hCommand - 6);
		}
		else if (maxW <= 480)
		{
			w = maxW - 20;
			h = System.Math.Min(240, maxH - GameCanvas.hCommand - 10);
		}
		else if (maxW <= 720)
		{
			w = (int)(maxW * 0.82f);
			if (w < 350) w = 350;
			if (w > 490) w = 490;
			h = System.Math.Min(260, (int)((maxH - GameCanvas.hCommand) * 0.88f));
		}
		else
		{
			w = (int)(maxW * 0.68f);
			if (w < 400) w = 400;
			if (w > 580) w = 580;
			h = System.Math.Min(280, (int)((maxH - GameCanvas.hCommand) * 0.88f));
			if (h < 220) h = 220;
		}

		wCon = w;
		hCon = h;

		x = MotherCanvas.hw - w / 2;
		if (GameCanvas.isTouch)
		{
			y = MotherCanvas.hh - h / 2;
		}
		else
		{
			y = MotherCanvas.hh - h / 2 - GameCanvas.hCommand / 2;
		}

		wItem = 24;
		xBe = x + wItem + miniItem;
		yBe = y + wItem + miniItem;
		hItem = h - wItem - miniItem - miniItem * 2;
		wPaintTab = w - wItem * 2 - miniItem * 2;
		hChat = hItem / GameCanvas.hText + 2;

		if (isSmallScreen)
		{
			wLeft = wPaintTab;
			xRight = xBe;
			wRight = wPaintTab;
		}
		else
		{
			wLeft = 185;
			if (wLeft > wPaintTab - 140)
			{
				wLeft = (wPaintTab >= 400) ? 200 : ((wPaintTab >= 300) ? 175 : 155);
			}
			xRight = xBe + wLeft + 4;
			wRight = wPaintTab - wLeft - 4;
		}

		if (cmdClose != null)
		{
			if (GameCanvas.isTouch)
			{
				cmdClose.setPos(x + w - 19, y + 8, MainTab.fraCloseTab, string.Empty);
			}
			else
			{
				cmdClose = AvMain.setPosCMD(cmdClose, 2);
			}
		}

		if (tfSearchMail == null)
		{
			tfSearchMail = new TField(xBe, yBe + 24, wLeft - 22);
			tfSearchMail.isCloseKey = false;
			tfSearchMail.height = 18;
		}

		updateAllTfPositions();
		if (tabCur != null)
		{
			tabCur.setLim();
			updateCameraNew(0, 0);
		}
		updateCamLeftList();
	}

	public static void drawModalBackdrop(mGraphics g)
	{
		if (g == null) return;
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		if (currentTheme == null || currentTheme.id == 0) return;
		g.setColor(0x000000);
		for (int y = 0; y < MotherCanvas.h; y += 2)
		{
			g.fillRect(0, y, MotherCanvas.w, 1);
		}
	}

	public static void drawPirateWindowFrame(mGraphics g, int x, int y, int w, int h)
	{
		if (g == null || w <= 0 || h <= 0) return;
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		if (currentTheme != null && currentTheme.id > 0)
		{
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

	public static void drawBrassRivet(mGraphics g, int rx, int ry)
	{
		if (g == null) return;
		g.setColor(0x563B12);
		g.fillRect(rx, ry, 4, 4);
		g.setColor(0xFFE066);
		g.fillRect(rx + 1, ry + 1, 2, 2);
	}

	public static void drawParchmentPanel(mGraphics g, int x, int y, int w, int h)
	{
		if (g == null || w <= 0 || h <= 0) return;
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		if (currentTheme == null || currentTheme.id == 0)
		{
			AvMain.paintRect(g, x, y, w, h, 0, 1);
			return;
		}
		// Aged pirate parchment paper
		ModernUI.fillGradientRect(g, x, y, w, h, 0x271C13, 0x18100A);
		g.setColor(0x664825);
		g.drawRect(x, y, w - 1, h - 1);
		g.setColor(0x3D2814);
		g.drawRect(x + 1, y + 1, w - 3, h - 3);
	}

	public void updateAllTfPositions()
	{
		int curLeftW = isSmallScreen ? wPaintTab : wLeft;
		if (tfSearchMail != null)
		{
			int subH = 18;
			bool twoRows = (curLeftW < 160);
			int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
			tfSearchMail.x = xBe;
			tfSearchMail.y = yBe + totalSubH + 3;
			tfSearchMail.width = curLeftW - 20;
			tfSearchMail.height = 18;
		}

		if (vecTabChat != null)
		{
			int btnSendW = 40;
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.tfchat != null)
				{
					if (catSelect == ChatDetail.CAT_MAIL || catSelect == ChatDetail.CAT_PRIVATE)
					{
						int curDetailX = (isSmallScreen && smallScreenState == 1) ? xBe : xRight;
						int curDetailW = (isSmallScreen && smallScreenState == 1) ? wPaintTab : wRight;
						cd.tfchat.x = curDetailX;
						cd.tfchat.y = yBe + hItem - TField.getHeight() - miniItem / 2;
						cd.tfchat.width = curDetailW - btnSendW - 4;
					}
					else
					{
						cd.tfchat.x = xBe;
						cd.tfchat.y = yBe + hItem - TField.getHeight() - miniItem / 2;
						cd.tfchat.width = wPaintTab - btnSendW - 4;
					}
				}
			}
		}
	}

	public mVector getListByCat(int cat)
	{
		if (cat < 0 || cat >= cachedCatLists.Length)
		{
			return new mVector();
		}
		mVector res = cachedCatLists[cat];
		res.removeAllElements();
		if (vecTabChat == null) return res;

		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
			if (cd == null) continue;

			if (cat == ChatDetail.CAT_MAIL)
			{
				if (cd.typeCategory == ChatDetail.CAT_MAIL || cd.isInvite || cd.mailId > 0 || cd.isGiftMail || cd.isNotReply || isMailTab(cd.name))
				{
					res.addElement(cd);
				}
			}
			else if (cat == ChatDetail.CAT_WORLD)
			{
				if (cd.typeCategory == ChatDetail.CAT_WORLD || (cd.typeCategory == -1 && isWorldTab(cd.name)))
				{
					res.addElement(cd);
				}
			}
			else if (cat == ChatDetail.CAT_PUBLIC)
			{
				if (cd.typeCategory == ChatDetail.CAT_PUBLIC || (cd.typeCategory == -1 && isPublicTab(cd.name)))
				{
					res.addElement(cd);
				}
			}
			else if (cat == ChatDetail.CAT_PRIVATE)
			{
				if (cd.typeCategory == ChatDetail.CAT_PRIVATE || (cd.typeCategory == -1 && isPrivateTab(cd.name) && !cd.isInvite && cd.mailId <= 0 && !cd.isGiftMail && !cd.isNotReply))
				{
					res.addElement(cd);
				}
			}
			else if (cat == ChatDetail.CAT_CLAN)
			{
				if (cd.typeCategory == ChatDetail.CAT_CLAN || (cd.typeCategory == -1 && isClanTab(cd.name)))
				{
					res.addElement(cd);
				}
			}
			else if (cat == ChatDetail.CAT_SYSTEM)
			{
				if (cd.typeCategory == ChatDetail.CAT_SYSTEM || (cd.typeCategory == -1 && isSystemTab(cd.name)))
				{
					res.addElement(cd);
				}
			}
		}
		return res;
	}

	public mVector getFilteredMailList()
	{
		mVector allMail = getListByCat(ChatDetail.CAT_MAIL);
		cachedFilteredMail.removeAllElements();
		string query = (tfSearchMail != null && !string.IsNullOrEmpty(tfSearchMail.getText())) ? tfSearchMail.getText().Trim().ToLower() : "";
		for (int i = 0; i < allMail.size(); i++)
		{
			ChatDetail cd = (ChatDetail)allMail.elementAt(i);
			if (cd == null) continue;
			if (!cd.isInCategoryFilter(subFilterMail)) continue;
			if (query.Length > 0 && !cd.isMatchSearch(query)) continue;
			cachedFilteredMail.addElement(cd);
		}
		return cachedFilteredMail;
	}

	public mVector getPageMailItems()
	{
		mVector filtered = getFilteredMailList();
		int total = filtered.size();
		totalPagesMail = (total + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
		if (totalPagesMail < 1) totalPagesMail = 1;
		if (curPageMail >= totalPagesMail) curPageMail = totalPagesMail - 1;
		if (curPageMail < 0) curPageMail = 0;

		cachedPageMailItems.removeAllElements();
		int start = curPageMail * ITEMS_PER_PAGE;
		int end = System.Math.Min(start + ITEMS_PER_PAGE, total);
		for (int i = start; i < end; i++)
		{
			cachedPageMailItems.addElement(filtered.elementAt(i));
		}
		return cachedPageMailItems;
	}

	public bool hasUnreadInCat(int cat)
	{
		mVector list = getListByCat(cat);
		for (int i = 0; i < list.size(); i++)
		{
			ChatDetail cd = (ChatDetail)list.elementAt(i);
			if (cd != null && cd.isNew) return true;
		}
		return false;
	}

	public int getUnreadCountInCat(int cat)
	{
		mVector list = getListByCat(cat);
		int count = 0;
		for (int i = 0; i < list.size(); i++)
		{
			ChatDetail cd = (ChatDetail)list.elementAt(i);
			if (cd != null && cd.isNew)
			{
				count++;
			}
		}
		return count;
	}

	public override void setxyPlus12()
	{
		GameCanvas.xPlus12 = 2;
		GameCanvas.yPlus12 = 2;
	}

	public void Show(MainScreen screen, int cat)
	{
		if (this.GetType() == typeof(ChatTabScreen))
		{
			initDefaultTabs();
			setPos();
			smallScreenState = 0;
			catSelect = cat;
			selectCategory(cat);
		}
		base.Show(screen);
		GameScreen.setNumMess();
	}

	public override void Show(MainScreen screen)
	{
		if (this.GetType() == typeof(ChatTabScreen))
		{
			initDefaultTabs();
			setPos();
			smallScreenState = 0;
			if (tabCur != null && getListByCat(catSelect).contains(tabCur))
			{
				selectCategory(catSelect, tabCur);
			}
			else
			{
				if (hasUnreadInCat(ChatDetail.CAT_MAIL))
				{
					catSelect = ChatDetail.CAT_MAIL;
				}
				else if (hasUnreadInCat(ChatDetail.CAT_PRIVATE))
				{
					catSelect = ChatDetail.CAT_PRIVATE;
				}
				else if (hasUnreadInCat(ChatDetail.CAT_CLAN))
				{
					catSelect = ChatDetail.CAT_CLAN;
				}
				selectCategory(catSelect);
			}
		}
		base.Show(screen);
		GameScreen.setNumMess();
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
		case 0:
		case 1:
			if (lastScreen != null)
			{
				lastScreen.Show(lastScreen.lastScreen);
				return;
			}
			GameCanvas.gameScr.Show();
			return;
		case 2:
			if (idSelect >= 0 && idSelect < vecTabChat.size())
			{
				vecTabChat.removeElementAt(idSelect);
			}
			idSelect = AvMain.resetSelect(idSelect, vecTabChat.size() - 1, isreset: false);
			getCurTab(idSelect);
			break;
		case 3:
			if (tabCur != null && tabCur.tfchat != null)
			{
				tabCur.addStartChat(GameScreen.player.name);
			}
			break;
		case 4: // Quay lại màn hình danh sách (cho màn hình nhỏ)
			smallScreenState = 0;
			getRightCmd();
			GameScreen.setNumMess();
			break;
		}
		base.commandPointer(index, subIndex);
	}

	public override void paint(mGraphics g)
	{
		if (lastScreen != null)
		{
			lastScreen.paint(g);
		}

		GameCanvas.resetTrans(g);

		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		if (!isDefaultTheme)
		{
			// 0. Modal Backdrop Dim (Chống chói/đè giao diện phía sau)
			drawModalBackdrop(g);

			// 1. Royal Pirate Mahogany Wood Frame with Brass Trim & Rivets
			drawPirateWindowFrame(g, x, y, w, h);
		}
		else
		{
			paintPaper(g, x, y, w, h, 0);
			if (Interface_Game.imgHoavan != null)
			{
				g.drawImage(Interface_Game.imgHoavan, x + 6, y + 6, 0);
				g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 2, x + w - 29, y + 6, 0);
				g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 1, x + 6, y + h - 29, 0);
				g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 3, x + w - 29, y + h - 29, 0);
			}
		}

		// 2. 6 Category Tabs on Top
		int numCats = 6;
		int tabH = 20;
		int tabY = y + 8;
		int wTabs = wPaintTab - 22;
		int tabW = wTabs / numCats;

		for (int i = 0; i < numCats; i++)
		{
			bool isSelected = (i == catSelect);
			int curX = xBe + i * tabW;
			int thisTabW = (i == numCats - 1) ? (wTabs - (numCats - 1) * tabW) : tabW;
			int unreadCount = getUnreadCountInCat(i);
			bool hasNew = (unreadCount > 0);

			if (isDefaultTheme)
			{
				AvMain.paintRect(g, curX, tabY, thisTabW - 1, tabH, (sbyte)(isSelected ? 1 : 0), isSelected ? 0 : 1);
				if (isSelected && AvMain.imgNenfocus != null)
				{
					g.drawRegion(AvMain.imgNenfocus, 2, 2, thisTabW - 1, tabH, 0, curX, tabY, 0);
				}
				if (isSelected)
				{
					mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
				}
				else
				{
					mFont.tahoma_7_black.drawStringAutoCenter(g, mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
				}
			}
			else
			{
				if (isSelected)
				{
					// Golden Oak Plank
					ModernUI.fillGradientRect(g, curX, tabY, thisTabW, tabH, 0x8C561E, 0x54320F);
					g.setColor(0xD4AF37);
					g.drawRect(curX, tabY, thisTabW - 1, tabH - 1);
					g.setColor(0xFFE066);
					g.fillRect(curX + 1, tabY + 1, thisTabW - 2, 1);
					mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
				}
				else
				{
					// Weathered Teak Plank
					ModernUI.fillGradientRect(g, curX, tabY, thisTabW, tabH, 0x281A10, 0x180F09);
					g.setColor(0x4E341F);
					g.drawRect(curX, tabY, thisTabW - 1, tabH - 1);
					mFont.tahoma_7_white.drawStringAutoCenter(g, mCatNames[i], curX + thisTabW / 2, tabY + 3, thisTabW - 4);
				}
			}

			// Unread Ruby Wax Seal
			if (hasNew)
			{
				int sealX = curX + thisTabW - 5;
				int sealY = tabY + 3;
				g.setColor(0xFFE066);
				g.fillRect(sealX - 1, sealY - 1, 6, 6);
				g.setColor(0xD92626);
				g.fillRect(sealX, sealY, 4, 4);
			}
		}

		// NÚT ĐÓNG (X) GÓC TRÊN BÊN PHẢI
		int xClose = x + w - 19;
		int yClose = y + 8;
		if (isDefaultTheme)
		{
			AvMain.paintRect(g, xClose, yClose, 16, 16, 1, 1);
			mFont.tahoma_7b_yellow.drawString(g, "X", xClose + 8, yClose + 2, 2);
		}
		else
		{
			ModernUI.fillGradientRect(g, xClose, yClose, 16, 16, 0x8C2020, 0x4D1010);
			g.setColor(0xD4AF37);
			g.drawRect(xClose, yClose, 15, 15);
			mFont.tahoma_7b_white.drawString(g, "X", xClose + 8, yClose + 2, 2);
		}

		// 3. NỘI DUNG TỪNG CATEGORY
		if (catSelect == ChatDetail.CAT_MAIL)
		{
			// HỘP THƯ
			if (isSmallScreen)
			{
				if (smallScreenState == 0)
				{
					paintMailListView(g, xBe, yBe, wPaintTab, hItem);
				}
				else
				{
					paintMailDetailView(g, xBe, yBe, wPaintTab, hItem, true);
				}
			}
			else
			{
				paintMailListView(g, xBe, yBe, wLeft, hItem);
				paintMailDetailView(g, xRight, yBe, wRight, hItem, false);
			}
		}
		else if (catSelect == ChatDetail.CAT_PRIVATE)
		{
			// TRÒ CHUYỆN RIÊNG
			if (isSmallScreen)
			{
				if (smallScreenState == 0)
				{
					paintPrivateListView(g, xBe, yBe, wPaintTab, hItem);
				}
				else
				{
					paintChatDetailView(g, xBe, yBe, wPaintTab, hItem, true);
				}
			}
			else
			{
				paintPrivateListView(g, xBe, yBe, wLeft, hItem);
				paintChatDetailView(g, xRight, yBe, wRight, hItem, false);
			}
		}
		else
		{
			// CÁC KÊNH CHAT KHÁC (Thế Giới, Công Cộng, Bang Hội, Hệ Thống)
			paintNormalChatView(g);
		}
	}

	public void paintMailListView(mGraphics g, int xList, int yList, int wArea, int hArea)
	{
		int searchH = 18;
		int pageBarH = 20;
		int totalSubH = 20;
		int listY = yList + 42;
		int listH = hArea - 64;

		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		try
		{
			// 1. Thanh phân loại 4 tab con (Tất cả, Lời mời, Quà, Hệ thống)
			int subH = 18;
			bool twoRows = (wArea < 160);
			totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
			if (twoRows)
			{
				int bW = (wArea - 4) / 2;
				for (int i = 0; i < 4; i++)
				{
					int row = i / 2;
					int col = i % 2;
					int bx = xList + col * (bW + 2);
					int by = yList + row * (subH + 2);
					bool isSel = (i == subFilterMail);
					if (isDefaultTheme)
					{
						AvMain.paintRect(g, bx, by, bW, subH, (sbyte)(isSel ? 1 : 0), isSel ? 0 : 1);
						if (isSel)
						{
							mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
						}
						else
						{
							mFont.tahoma_7_black.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
						}
					}
					else
					{
						int topCol = isSel ? 0x8C561E : 0x281B12;
						int botCol = isSel ? 0x54320F : 0x180F09;
						int bdrCol = isSel ? 0xD4AF37 : 0x4A3320;
						ModernUI.fillGradientRect(g, bx, by, bW, subH, topCol, botCol);
						g.setColor(bdrCol);
						g.drawRect(bx, by, bW - 1, subH - 1);
						if (isSel)
						{
							g.setColor(0xFFE066);
							g.fillRect(bx + 1, by + 1, bW - 2, 1);
							mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
						}
						else
						{
							mFont.tahoma_7_white.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, by + 2, bW - 2);
						}
					}
				}
			}
			else
			{
				int bW = (wArea - 6) / 4;
				for (int i = 0; i < 4; i++)
				{
					int bx = xList + i * (bW + 2);
					bool isSel = (i == subFilterMail);
					if (isDefaultTheme)
					{
						AvMain.paintRect(g, bx, yList, bW, subH, (sbyte)(isSel ? 1 : 0), isSel ? 0 : 1);
						if (isSel)
						{
							mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
						}
						else
						{
							mFont.tahoma_7_black.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
						}
					}
					else
					{
						int topCol = isSel ? 0x8C561E : 0x281B12;
						int botCol = isSel ? 0x54320F : 0x180F09;
						int bdrCol = isSel ? 0xD4AF37 : 0x4A3320;
						ModernUI.fillGradientRect(g, bx, yList, bW, subH, topCol, botCol);
						g.setColor(bdrCol);
						g.drawRect(bx, yList, bW - 1, subH - 1);
						if (isSel)
						{
							g.setColor(0xFFE066);
							g.fillRect(bx + 1, yList + 1, bW - 2, 1);
							mFont.tahoma_7b_yellow.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
						}
						else
						{
							mFont.tahoma_7_white.drawStringAutoCenter(g, mSubFilterNames[i], bx + bW / 2, yList + 2, bW - 2);
						}
					}
				}
			}

			// 2. Ô tìm kiếm
			int searchY = yList + totalSubH + 3;
			if (tfSearchMail != null)
			{
				tfSearchMail.x = xList;
				tfSearchMail.y = searchY;
				tfSearchMail.width = wArea - 20;
				tfSearchMail.height = searchH;

				int clrX = xList + wArea - 18;
				if (isDefaultTheme)
				{
					AvMain.paintRect(g, xList, searchY, wArea - 20, searchH, 0, 3);
					string q = tfSearchMail.getText();
					if (!string.IsNullOrEmpty(q))
					{
						mFont.tahoma_7_white.drawString(g, q, xList + 5, searchY + 2, 0);
					}
					else
					{
						mFont.tahoma_7_white.drawString(g, "Tìm kiếm...", xList + 5, searchY + 2, 0);
					}
					AvMain.paintRect(g, clrX, searchY, 18, searchH, 1, 1);
					mFont.tahoma_7b_yellow.drawString(g, "✕", clrX + 9, searchY + 2, 2);
				}
				else
				{
					ModernUI.fillGradientRect(g, xList, searchY, wArea - 20, searchH, 0x140D08, 0x1F140C);
					g.setColor(0x563B22);
					g.drawRect(xList, searchY, wArea - 21, searchH - 1);

					string q = tfSearchMail.getText();
					if (!string.IsNullOrEmpty(q))
					{
						mFont.tahoma_7_white.drawString(g, q, xList + 5, searchY + 2, 0);
					}
					else
					{
						mFont.tahoma_7_white.drawString(g, "Tìm kiếm...", xList + 5, searchY + 2, 0);
					}

					// Nút Clear search [✕]
					ModernUI.fillGradientRect(g, clrX, searchY, 18, searchH, 0x3D1A1A, 0x260E0E);
					g.setColor(0x8C3333);
					g.drawRect(clrX, searchY, 17, searchH - 1);
					mFont.tahoma_7b_white.drawString(g, "✕", clrX + 9, searchY + 2, 2);
				}
			}

			// 3. Khung cuộn danh sách 10 mục
			listY = searchY + searchH + 3;
			listH = hArea - (totalSubH + searchH + pageBarH + 9);
			if (listH < 30) listH = 30;

			drawParchmentPanel(g, xList - 1, listY - 1, wArea + 2, listH + 2);

			g.setClip(xList, listY, wArea, listH);
			int camOffset = (CamLeftList != null) ? CamLeftList.cmx : 0;
			g.translate(0, -camOffset);

			mVector pageItems = getPageMailItems();
			int itemH = 24;
			int maxWText = wArea - 28;

			for (int i = 0; i < pageItems.size(); i++)
			{
				ChatDetail cd = (ChatDetail)pageItems.elementAt(i);
				if (cd == null) continue;

				int rowY = listY + i * (itemH + 2);
				bool isRowSelected = (tabCur == cd);

				if (isDefaultTheme)
				{
					AvMain.paintRect(g, xList + 1, rowY, wArea - 2, itemH, (sbyte)(isRowSelected ? 1 : 0), isRowSelected ? 0 : 3);
				}
				else
				{
					if (isRowSelected)
					{
						ModernUI.fillGradientRect(g, xList + 1, rowY, wArea - 2, itemH, 0x5C3A1A, 0x3A220E);
						g.setColor(0xD4AF37);
						g.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
						g.setColor(0xFFE066);
						g.fillRect(xList + 2, rowY + 1, wArea - 5, 1);
					}
					else
					{
						ModernUI.fillGradientRect(g, xList + 1, rowY, wArea - 2, itemH, 0x24180E, 0x160E08);
						g.setColor(0x422D1B);
						g.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
					}
				}

				// Biểu tượng thư
				string iconStr = "✉";
				if (cd.isInvite) iconStr = "⚔";
				else if (cd.isGiftMail) iconStr = "🎁";

				if (cd.isInvite)
				{
					mFont.tahoma_7b_red.drawString(g, iconStr, xList + 7, rowY + 4, 2);
				}
				else if (cd.isGiftMail)
				{
					mFont.tahoma_7b_yellow.drawString(g, iconStr, xList + 7, rowY + 4, 2);
				}
				else
				{
					mFont.tahoma_7_white.drawString(g, iconStr, xList + 7, rowY + 4, 2);
				}

				string displayName = (!string.IsNullOrEmpty(cd.mailTitle)) ? cd.mailTitle : cd.name;
				mFont itemFont = isRowSelected ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white;
				itemFont.drawStringAuto(g, displayName, xList + 16, rowY + 4, maxWText, 0);

				// Chấm đỏ thông báo chưa đọc
				if (cd.isNew)
				{
					int sealX = xList + wArea - 8;
					int sealY = rowY + 6;
					g.setColor(0xFFE066);
					g.fillRect(sealX - 1, sealY - 1, 6, 6);
					g.setColor(0xD92626);
					g.fillRect(sealX, sealY, 4, 4);
				}
			}

			if (pageItems.size() == 0)
			{
				mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
				emptyF.drawString(g, "Không có thư nào", xList + wArea / 2, listY + listH / 2 - 5, 2);
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}

		// 4. Thanh phân trang ở đáy
		try
		{
			int pageBarY = yList + hArea - pageBarH;
			int arrowBtnW = 20;

			if (isDefaultTheme)
			{
				// Nút Lùi trang [◀]
				AvMain.paintRect(g, xList, pageBarY, arrowBtnW, 18, (sbyte)(curPageMail > 0 ? 1 : 0), curPageMail > 0 ? 0 : 1);
				mFont.tahoma_7b_yellow.drawString(g, "◀", xList + arrowBtnW / 2, pageBarY + 3, 2);

				// Nút Tiến trang [▶]
				int nextX = xList + wArea - arrowBtnW;
				bool canNext = curPageMail < totalPagesMail - 1;
				AvMain.paintRect(g, nextX, pageBarY, arrowBtnW, 18, (sbyte)(canNext ? 1 : 0), canNext ? 0 : 1);
				mFont.tahoma_7b_yellow.drawString(g, "▶", nextX + arrowBtnW / 2, pageBarY + 3, 2);

				// Ô Trang giữa: "Trang X / Y"
				int centerW = wArea - arrowBtnW * 2 - 4;
				int centerX = xList + arrowBtnW + 2;
				AvMain.paintRect(g, centerX, pageBarY, centerW, 18, 0, 1);
				string pageStr = (curPageMail + 1) + "/" + totalPagesMail;
				mFont.tahoma_7b_black.drawString(g, pageStr, centerX + centerW / 2, pageBarY + 3, 2);
			}
			else
			{
				// Nút Lùi trang [◀]
				ModernUI.fillGradientRect(g, xList, pageBarY, arrowBtnW, 18, 0x332214, 0x1F140A);
				g.setColor(curPageMail > 0 ? 0xD4AF37 : 0x4D3622);
				g.drawRect(xList, pageBarY, arrowBtnW - 1, 17);
				mFont.tahoma_7b_white.drawString(g, "◀", xList + arrowBtnW / 2, pageBarY + 3, 2);

				// Nút Tiến trang [▶]
				int nextX = xList + wArea - arrowBtnW;
				ModernUI.fillGradientRect(g, nextX, pageBarY, arrowBtnW, 18, 0x332214, 0x1F140A);
				g.setColor(curPageMail < totalPagesMail - 1 ? 0xD4AF37 : 0x4D3622);
				g.drawRect(nextX, pageBarY, arrowBtnW - 1, 17);
				mFont.tahoma_7b_white.drawString(g, "▶", nextX + arrowBtnW / 2, pageBarY + 3, 2);

				// Ô Trang giữa: "Trang X / Y"
				int centerW = wArea - arrowBtnW * 2 - 4;
				int centerX = xList + arrowBtnW + 2;
				ModernUI.fillGradientRect(g, centerX, pageBarY, centerW, 18, 0x1C130B, 0x140D07);
				g.setColor(0x563B22);
				g.drawRect(centerX, pageBarY, centerW - 1, 17);
				string pageStr = (curPageMail + 1) + "/" + totalPagesMail;
				mFont.tahoma_7b_yellow.drawString(g, pageStr, centerX + centerW / 2, pageBarY + 3, 2);
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}
	}

	public void paintMailDetailView(mGraphics g, int xDetail, int yDetail, int wArea, int hArea, bool isSmallMode)
	{
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		if (tabCur == null)
		{
			drawParchmentPanel(g, xDetail - 1, yDetail - 1, wArea + 2, hArea + 2);
			mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
			emptyF.drawString(g, "Chọn một thư để xem nội dung", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
			return;
		}

		int topHeaderH = 0;
		if (isSmallMode)
		{
			topHeaderH = 22;
			ModernUI.drawButton(g, xDetail, yDetail, 60, topHeaderH, "◀ Trở về", ModernUI.BTN_BLUE, false, false);
			string topTitle = (!string.IsNullOrEmpty(tabCur.mailTitle)) ? tabCur.mailTitle : tabCur.name;
			mFont.tahoma_7b_yellow.drawStringAuto(g, topTitle, xDetail + 66, yDetail + 4, wArea - 70, 0);
		}

		drawParchmentPanel(g, xDetail - 1, yDetail + topHeaderH - 1, wArea + 2, hArea - topHeaderH + 2);

		int btnBarH = 26;
		int contentY = yDetail + topHeaderH + 2;
		int contentH = hArea - topHeaderH - btnBarH - 4;
		if (contentH < 10) contentH = 10;

		try
		{
			g.setClip(xDetail, contentY, wArea, contentH);
			int camOffset = (CamDetailChat != null) ? CamDetailChat.cmx : 0;
			g.translate(0, -camOffset);

			minChat = (camOffset / GameCanvas.hText) - 2;
			if (minChat < 0) minChat = 0;
			maxChat = minChat + hChat + 6;

			if (tabCur.vecDetail != null)
			{
				for (int var7 = minChat; var7 <= maxChat; var7++)
				{
					if (var7 >= 0 && var7 < tabCur.vecDetail.size())
					{
						MainTextChat tc = (MainTextChat)tabCur.vecDetail.elementAt(var7);
						if (tc != null)
						{
							mFont f = AvMain.setTextColor(tc.color);
							if (f == null || (isDefaultTheme && tc.color == 0)) f = isDefaultTheme ? mFont.tahoma_7_black : mFont.tahoma_7_white;
							f.drawString(g, tc.text, xDetail + 6, contentY + var7 * GameCanvas.hText, 0);
						}
					}
				}
			}

			int giftY = contentY + ((tabCur.vecDetail != null ? tabCur.vecDetail.size() : 0) + 1) * GameCanvas.hText;

			// Hộp thách đấu PvP nếu có
			if (tabCur.isInvite && tabCur.typeInvite == 3)
			{
				int boxH = 22;
				if (isDefaultTheme)
				{
					AvMain.paintRect(g, xDetail + 4, giftY, wArea - 8, boxH, 0, 1);
				}
				else
				{
					ModernUI.fillGradientRect(g, xDetail + 4, giftY, wArea - 8, boxH, 0x3D1A1A, 0x260E0E);
					g.setColor(0x8C3333);
					g.drawRect(xDetail + 4, giftY, wArea - 9, boxH - 1);
				}
				string betText = "Mức cược: " + tabCur.priceFight + (tabCur.typeFight == 1 ? " Ruby (thua trả phí)" : " Beri");
				mFont betFont = isDefaultTheme ? mFont.tahoma_7b_brown : mFont.tahoma_7b_yellow;
				betFont.drawString(g, betText, xDetail + 8, giftY + 4, 0);
				giftY += boxH + 6;
			}

			// Hộp Quà Đính Kèm (Treasure Loot Box)
			if (tabCur.mItemgift != null && tabCur.mItemgift.Length > 0)
			{
				int numRows = (tabCur.mItemgift.Length + 1) / 2;
				int cardH = 32;
				int giftBoxH = 22 + numRows * cardH + 4;

				if (isDefaultTheme)
				{
					AvMain.paintRect(g, xDetail + 2, giftY, wArea - 4, giftBoxH, 0, 1);
					mFont.tahoma_7b_brown.drawString(g, "🎁 " + T.quatt + ":", xDetail + 6, giftY + 4, 0);
				}
				else
				{
					ModernUI.fillGradientRect(g, xDetail + 2, giftY, wArea - 4, giftBoxH, 0x332214, 0x1E130A);
					g.setColor(0x8C681E);
					g.drawRect(xDetail + 2, giftY, wArea - 5, giftBoxH - 1);
					mFont.tahoma_7b_yellow.drawString(g, "🎁 " + T.quatt + ":", xDetail + 6, giftY + 4, 0);
					g.setColor(0x543A1B);
					g.fillRect(xDetail + 4, giftY + 18, wArea - 8, 1);
				}

				int colW = (wArea - 12) / 2;
				if (colW < 40) colW = wArea - 12;

				for (int gi = 0; gi < tabCur.mItemgift.Length; gi++)
				{
					Item_Drop gItem = tabCur.mItemgift[gi];
					if (gItem == null) continue;

					int col = gi % 2;
					int row = gi / 2;
					int itemX = xDetail + 4 + col * (colW + 4);
					int itemY = giftY + 22 + row * cardH;

					int qColor = gItem.colorName != 0 ? (int)gItem.colorName : 3;
					ModernUI.drawSlot(g, itemX, itemY, 26, qColor, false);

					try
					{
						if (gItem.typeObject == 4 && gItem.IdIcon <= 2 && gItem.name != null && (gItem.name.ToLower().IndexOf("beri") >= 0 || gItem.name.ToLower().IndexOf("ruby") >= 0 || gItem.name.ToLower().IndexOf("extol") >= 0 || gItem.name.ToLower().IndexOf("vàng") >= 0 || gItem.name.ToLower().IndexOf("ngọc") >= 0 || gItem.name.ToLower().IndexOf("coin") >= 0))
						{
							if (AvMain.fraMoney != null)
							{
								if (gItem.IdIcon == 0 || gItem.name.ToLower().IndexOf("beri") >= 0 || gItem.name.ToLower().IndexOf("vàng") >= 0)
								{
									AvMain.fraMoney.drawFrame(0, itemX + 13, itemY + 13, 0, 3, g);
								}
								else if (gItem.IdIcon == 1 || gItem.name.ToLower().IndexOf("ruby") >= 0 || gItem.name.ToLower().IndexOf("ngọc") >= 0)
								{
									AvMain.fraMoney.drawFrame(1, itemX + 13, itemY + 13, 0, 3, g);
								}
								else if (gItem.IdIcon == 2 || gItem.name.ToLower().IndexOf("extol") >= 0 || gItem.name.ToLower().IndexOf("coin") >= 0)
								{
									AvMain.fraMoney.drawFrame(7, itemX + 13, itemY + 13, 0, 3, g);
								}
							}
						}
						else
						{
							gItem.paintXY(g, itemX + 13, itemY + 13);
						}
					}
					catch (Exception)
					{
						mFont.tahoma_7_white.drawString(g, "?", itemX + 13, itemY + 7, 2);
					}

					if (gItem.name != null && gItem.name.Length > 0)
					{
						int maxItemNameW = System.Math.Max(10, colW - 32);
						mFont nameFont = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7b_white;
						nameFont.drawStringAuto(g, gItem.name, itemX + 29, itemY + 1, maxItemNameW, 0);
						string numStr = (gItem.num > 1) ? ("x" + AvMain.getDotNumber(gItem.num)) : "x1";
						mFont.tahoma_7_yellow.drawString(g, numStr, itemX + 29, itemY + 13, 0);
					}
				}
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}

		// --- VÙNG NÚT BẤM CỐ ĐỊNH Ở ĐÁY ---
		try
		{
			int btnY = yDetail + hArea - btnBarH - 2;
			int btnH = 22;

			g.setColor(isDefaultTheme ? 0xD4AF37 : 0x8C681E);
			g.fillRect(xDetail, btnY - 3, wArea, 1);

			if (tabCur.isInvite)
			{
				if (tabCur.typeInvite == 3)
				{
					int btnW = (wArea - 8) / 3;
					int btn1X = xDetail;
					int btn2X = xDetail + btnW + 4;
					int btn3X = xDetail + (btnW + 4) * 2;
					string capAccept = (!string.IsNullOrEmpty(T.chapnhan)) ? T.chapnhan : "Chấp nhận";
					ModernUI.drawButton(g, btn1X, btnY, btnW, btnH, capAccept, ModernUI.BTN_GREEN, false, false);
					ModernUI.drawButton(g, btn2X, btnY, btnW, btnH, "Xem TT", ModernUI.BTN_BLUE, false, false);
					ModernUI.drawButton(g, btn3X, btnY, btnW, btnH, "Từ chối", ModernUI.BTN_RED, false, false);
				}
				else
				{
					int btnW = (wArea - 6) / 2;
					int btn1X = xDetail;
					int btn2X = xDetail + btnW + 4;
					string capAccept = (!string.IsNullOrEmpty(T.chapnhan)) ? T.chapnhan : "Chấp nhận";
					ModernUI.drawButton(g, btn1X, btnY, btnW, btnH, capAccept, ModernUI.BTN_GREEN, false, false);
					ModernUI.drawButton(g, btn2X, btnY, btnW, btnH, "Từ chối", ModernUI.BTN_RED, false, false);
				}
			}
			else
			{
				bool hasGiftToClaim = (!tabCur.isClaimed && tabCur.mItemgift != null && tabCur.mItemgift.Length > 0);
				bool canReply = (!tabCur.isNotReply && !string.IsNullOrEmpty(tabCur.name) && !isSystemTab(tabCur.name));

				if (hasGiftToClaim)
				{
					if (canReply)
					{
						int btnW = (wArea - 8) / 3;
						int btn1X = xDetail;
						int btn2X = xDetail + btnW + 4;
						int btn3X = xDetail + (btnW + 4) * 2;
						string capClaim = (!string.IsNullOrEmpty(T.nhanqua)) ? T.nhanqua : "Nhận quà";
						ModernUI.drawButton(g, btn1X, btnY, btnW, btnH, capClaim, ModernUI.BTN_GREEN, false, false);
						ModernUI.drawButton(g, btn2X, btnY, btnW, btnH, "Trả lời", ModernUI.BTN_BLUE, false, false);
						ModernUI.drawButton(g, btn3X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
					}
					else
					{
						int btnW = (wArea - 6) / 2;
						int btn1X = xDetail;
						int btn2X = xDetail + btnW + 4;
						string capClaim = (!string.IsNullOrEmpty(T.nhanqua)) ? T.nhanqua : "Nhận quà";
						ModernUI.drawButton(g, btn1X, btnY, btnW, btnH, capClaim, ModernUI.BTN_GREEN, false, false);
						ModernUI.drawButton(g, btn2X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
					}
				}
				else
				{
					if (canReply)
					{
						int btnW = (wArea - 6) / 2;
						int btn1X = xDetail;
						int btn2X = xDetail + btnW + 4;
						ModernUI.drawButton(g, btn1X, btnY, btnW, btnH, "Trả lời", ModernUI.BTN_BLUE, false, false);
						ModernUI.drawButton(g, btn2X, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
					}
					else
					{
						int btnW = System.Math.Min(120, wArea - 10);
						int btnX = xDetail + (wArea - btnW) / 2;
						ModernUI.drawButton(g, btnX, btnY, btnW, btnH, "Xóa thư", ModernUI.BTN_RED, false, false);
					}
				}
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}
	}

	public void paintPrivateListView(mGraphics g, int xList, int yList, int wArea, int hArea)
	{
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		try
		{
			drawParchmentPanel(g, xList - 1, yList - 1, wArea + 2, hArea + 2);

			g.setClip(xList, yList, wArea, hArea);
			int camOffset = (CamLeftList != null) ? CamLeftList.cmx : 0;
			g.translate(0, -camOffset);

			mVector listItems = getListByCat(ChatDetail.CAT_PRIVATE);
			int itemH = 24;
			int maxWText = wArea - 20;
			for (int i = 0; i < listItems.size(); i++)
			{
				ChatDetail cd = (ChatDetail)listItems.elementAt(i);
				if (cd == null) continue;

				int rowY = yList + i * (itemH + 2);
				bool isRowSelected = (tabCur == cd);

				if (isDefaultTheme)
				{
					AvMain.paintRect(g, xList + 1, rowY, wArea - 2, itemH, (sbyte)(isRowSelected ? 1 : 0), isRowSelected ? 0 : 3);
				}
				else
				{
					if (isRowSelected)
					{
						ModernUI.fillGradientRect(g, xList + 1, rowY, wArea - 2, itemH, 0x5C3A1A, 0x3A220E);
						g.setColor(0xD4AF37);
						g.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
						g.setColor(0xFFE066);
						g.fillRect(xList + 2, rowY + 1, wArea - 5, 1);
					}
					else
					{
						ModernUI.fillGradientRect(g, xList + 1, rowY, wArea - 2, itemH, 0x24180E, 0x160E08);
						g.setColor(0x422D1B);
						g.drawRect(xList + 1, rowY, wArea - 3, itemH - 1);
					}
				}

				string displayName = cd.name;
				mFont itemFont = isRowSelected ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white;
				itemFont.drawStringAuto(g, displayName, xList + 6, rowY + 4, maxWText, 0);

				if (cd.isNew)
				{
					int sealX = xList + wArea - 8;
					int sealY = rowY + 6;
					g.setColor(0xFFE066);
					g.fillRect(sealX - 1, sealY - 1, 6, 6);
					g.setColor(0xD92626);
					g.fillRect(sealX, sealY, 4, 4);
				}
			}

			if (listItems.size() == 0)
			{
				mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
				emptyF.drawString(g, "Trống", xList + wArea / 2, yList + hArea / 2 - 6, 2);
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}
	}

	public void paintChatDetailView(mGraphics g, int xDetail, int yDetail, int wArea, int hArea, bool isSmallMode)
	{
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		if (tabCur == null)
		{
			drawParchmentPanel(g, xDetail - 1, yDetail - 1, wArea + 2, hArea + 2);
			mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
			emptyF.drawString(g, "Chưa có cuộc trò chuyện", xDetail + wArea / 2, yDetail + hArea / 2 - 6, 2);
			return;
		}

		int topHeaderH = 0;
		if (isSmallMode)
		{
			topHeaderH = 22;
			ModernUI.drawButton(g, xDetail, yDetail, 60, topHeaderH, "◀ Trở về", ModernUI.BTN_BLUE, false, false);
			mFont.tahoma_7b_yellow.drawString(g, tabCur.name, xDetail + 66, yDetail + 4, 0);
		}

		drawParchmentPanel(g, xDetail - 1, yDetail + topHeaderH - 1, wArea + 2, hArea - topHeaderH + 2);

		if (tabCur.tfchat != null)
		{
			tabCur.tfchat.paint(g);
			int btnSendW = 40;
			int btnSendH = tabCur.tfchat.height > 20 ? tabCur.tfchat.height : 22;
			int btnSendX = tabCur.tfchat.x + tabCur.tfchat.width + 4;
			int btnSendY = tabCur.tfchat.y;
			ModernUI.drawButton(g, btnSendX, btnSendY, btnSendW, btnSendH, "Gửi", ModernUI.BTN_GREEN, false, false);
		}

		int contentY = yDetail + topHeaderH + 2;
		int rightClipH = hArea - topHeaderH - (tabCur.tfchat != null ? tabCur.tfchat.height : 0) - 4;

		try
		{
			g.setClip(xDetail, contentY, wArea, rightClipH);
			int camOffset = (CamDetailChat != null) ? CamDetailChat.cmx : 0;
			g.translate(0, -camOffset);

			minChat = (camOffset / GameCanvas.hText) - 2;
			if (minChat < 0) minChat = 0;
			maxChat = minChat + hChat;

			if (tabCur.vecDetail != null)
			{
				for (int var7 = minChat; var7 <= maxChat; var7++)
				{
					if (var7 >= 0 && var7 < tabCur.vecDetail.size())
					{
						MainTextChat var8 = (MainTextChat)tabCur.vecDetail.elementAt(var7);
						if (var8 != null)
						{
							if (var8.typeLeftRight == 1)
							{
								int textW = mFont.tahoma_7_white.getWidth(var8.text);
								int msgX = xDetail + wArea - 10 - textW;
								if (msgX < xDetail + 6) msgX = xDetail + 6;
								if (isDefaultTheme)
								{
									AvMain.paintRect(g, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 1, 0);
									mFont.tahoma_7b_yellow.drawString(g, var8.text, msgX, contentY + var7 * GameCanvas.hText + 1, 0);
								}
								else
								{
									ModernUI.fillGradientRect(g, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 0x5C3A1A, 0x3A220E);
									g.setColor(0xD4AF37);
									g.drawRect(msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 7, GameCanvas.hText - 1);
									mFont.tahoma_7b_white.drawString(g, var8.text, msgX, contentY + var7 * GameCanvas.hText + 1, 0);
								}
							}
							else
							{
								int textW = mFont.tahoma_7_white.getWidth(var8.text);
								int msgX = xDetail + 6;
								if (isDefaultTheme)
								{
									AvMain.paintRect(g, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 0, 3);
									mFont.tahoma_7b_white.drawString(g, var8.text, msgX, contentY + var7 * GameCanvas.hText + 1, 0);
								}
								else
								{
									ModernUI.fillGradientRect(g, msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 8, GameCanvas.hText, 0x24180E, 0x160E08);
									g.setColor(0x422D1B);
									g.drawRect(msgX - 4, contentY + var7 * GameCanvas.hText - 1, textW + 7, GameCanvas.hText - 1);
									mFont.tahoma_7_white.drawString(g, var8.text, msgX, contentY + var7 * GameCanvas.hText + 1, 0);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			Debug.LogException(e);
		}
		finally
		{
			g.clearClip();
			GameCanvas.resetTrans(g);
		}
	}

	public void paintNormalChatView(mGraphics g)
	{
		UITheme currentTheme = UIThemeManager.getCurrentTheme();
		bool isDefaultTheme = (currentTheme == null || currentTheme.id == 0);

		drawParchmentPanel(g, xBe - 1, yBe - 1, wPaintTab + 2, hItem + 2);

		if (tabCur != null)
		{
			if (tabCur.tfchat != null)
			{
				tabCur.tfchat.paint(g);
				int btnSendW = 40;
				int btnSendH = tabCur.tfchat.height > 20 ? tabCur.tfchat.height : 22;
				int btnSendX = tabCur.tfchat.x + tabCur.tfchat.width + 4;
				int btnSendY = tabCur.tfchat.y;
				ModernUI.drawButton(g, btnSendX, btnSendY, btnSendW, btnSendH, "Gửi", ModernUI.BTN_GREEN, false, false);
			}

			int fullClipH = hItem - ((tabCur.tfchat != null) ? tabCur.tfchat.height : 0) - 4;
			try
			{
				g.setClip(xBe, yBe, wPaintTab, fullClipH);
				int camOffset = (CamDetailChat != null) ? CamDetailChat.cmx : 0;
				g.translate(0, -camOffset);

				minChat = (camOffset / GameCanvas.hText) - 2;
				if (minChat < 0) minChat = 0;
				maxChat = minChat + hChat + 2;

				if (tabCur.vecDetail != null)
				{
					for (int j = minChat; j <= maxChat; j++)
					{
						if (j >= 0 && j < tabCur.vecDetail.size())
						{
							MainTextChat tc = (MainTextChat)tabCur.vecDetail.elementAt(j);
							if (tc != null)
							{
								mFont f = AvMain.setTextColor(tc.color);
								if (f == null || (isDefaultTheme && tc.color == 0)) f = isDefaultTheme ? mFont.tahoma_7_black : mFont.tahoma_7_white;
								f.drawString(g, tc.text, xBe + 4, yBe + j * GameCanvas.hText, 0);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				Debug.LogException(e);
			}
			finally
			{
				g.clearClip();
				GameCanvas.resetTrans(g);
			}
		}
		else
		{
			string title = mCatNames[catSelect];
			mFont emptyF = isDefaultTheme ? mFont.tahoma_7b_black : mFont.tahoma_7_white;
			emptyF.drawString(g, "Không có dữ liệu " + title, xBe + wPaintTab / 2, yBe + hItem / 2 - 6, 2);
		}
	}

	public override void update()
	{
		if (lastScreen != null)
		{
			lastScreen.update();
		}

		if (tabCur != null)
		{
			if (tabCur.marqueeTitle != null)
			{
				int maxWText = (isSmallScreen ? wPaintTab : wLeft) - 10;
				tabCur.updateMarquee(maxWText, mFont.tahoma_7b_white);
			}
		}

		if (camPageTabs != null)
		{
			camPageTabs.moveCamera();
		}

		if (CamLeftList != null)
		{
			CamLeftList.moveCamera();
		}

		if (CamDetailChat != null)
		{
			CamDetailChat.moveCamera();
		}

		if (tfSearchMail != null && catSelect == ChatDetail.CAT_MAIL)
		{
			tfSearchMail.update();
		}

		if (tabCur != null && tabCur.tfchat != null)
		{
			tabCur.tfchat.update();
		}

		base.update();
	}

	public void selectCategory(int cat, ChatDetail targetTab = null)
	{
		catSelect = cat;
		if (catSelect < 0) catSelect = 0;
		if (catSelect > 5) catSelect = 5;
		smallScreenState = 0;

		mVector list = getListByCat(catSelect);
		if (list.size() > 0)
		{
			ChatDetail target = targetTab;
			if (target == null || !list.contains(target))
			{
				if (tabCur != null && list.contains(tabCur))
				{
					target = tabCur;
				}
				else
				{
					target = (ChatDetail)list.elementAt(0);
					for (int i = 0; i < list.size(); i++)
					{
						ChatDetail item = (ChatDetail)list.elementAt(i);
						if (item != null && item.isNew)
						{
							target = item;
							break;
						}
					}
				}
			}
			tabCur = target;
			idSelect = vecTabChat.indexOf(tabCur);
			if (tabCur != null)
			{
				tabCur.isNew = false;
				tabCur.setLim();
				updateCameraNew(0, 0);
			}
			updateCamLeftList();
			getRightCmd();
		}
		else
		{
			tabCur = null;
			idSelect = -1;
			getRightCmd();
		}

		updateAllTfPositions();
		GameScreen.setNumMess();
	}

	public override void updatekey()
	{
		if (GameCanvas.keyMove(0)) // Trái
		{
			GameCanvas.ClearkeyMove(0);
			if (catSelect == ChatDetail.CAT_MAIL && curPageMail > 0)
			{
				curPageMail--;
				mVector pItems = getPageMailItems();
				if (pItems.size() > 0)
				{
					tabCur = (ChatDetail)pItems.elementAt(0);
					idSelect = vecTabChat.indexOf(tabCur);
					getRightCmd();
				}
			}
			else if (catSelect > 0)
			{
				selectCategory(catSelect - 1);
			}
		}
		else if (GameCanvas.keyMove(2)) // Phải
		{
			GameCanvas.ClearkeyMove(2);
			if (catSelect == ChatDetail.CAT_MAIL && curPageMail < totalPagesMail - 1)
			{
				curPageMail++;
				mVector pItems = getPageMailItems();
				if (pItems.size() > 0)
				{
					tabCur = (ChatDetail)pItems.elementAt(0);
					idSelect = vecTabChat.indexOf(tabCur);
					getRightCmd();
				}
			}
			else if (catSelect < 5)
			{
				selectCategory(catSelect + 1);
			}
		}
		else if (GameCanvas.keyMove(1)) // Lên
		{
			GameCanvas.ClearkeyMove(1);
			if (catSelect == ChatDetail.CAT_MAIL && smallScreenState == 0)
			{
				CamLeftList.cmtoX -= 26;
				if (CamLeftList.cmtoX < 0) CamLeftList.cmtoX = 0;
			}
			else
			{
				CamDetailChat.cmtoX -= GameCanvas.hText * 2;
				if (CamDetailChat.cmtoX < 0) CamDetailChat.cmtoX = 0;
			}
		}
		else if (GameCanvas.keyMove(3)) // Xuống
		{
			GameCanvas.ClearkeyMove(3);
			if (catSelect == ChatDetail.CAT_MAIL && smallScreenState == 0)
			{
				CamLeftList.cmtoX += 26;
				if (CamLeftList.cmtoX > CamLeftList.cmxLim) CamLeftList.cmtoX = CamLeftList.cmxLim;
			}
			else
			{
				CamDetailChat.cmtoX += GameCanvas.hText * 2;
				if (CamDetailChat.cmtoX > CamDetailChat.cmxLim) CamDetailChat.cmtoX = CamDetailChat.cmxLim;
			}
		}
		base.updatekey();
	}

	public override void updatePointer()
	{
		if (CamLeftList != null)
		{
			CamLeftList.update_Pos_UP_DOWN();
		}
		if (CamDetailChat != null)
		{
			CamDetailChat.update_Pos_UP_DOWN();
		}
		if (camPageTabs != null)
		{
			camPageTabs.updatePos_LEFT_RIGHT();
		}

		// 1. Touch Nút Đóng (X)
		int xClose = x + w - 19;
		int yClose = y + 8;
		if (GameCanvas.isPointSelect(xClose - 4, yClose - 4, 24, 24) || (cmdClose != null && GameCanvas.isPointSelect(cmdClose.xCmd - 12, cmdClose.yCmd - 12, 24, 24)))
		{
			GameCanvas.isPointerSelect = false;
			if (cmdClose != null)
			{
				cmdClose.perform();
			}
			else if (cmdCloseChat != null)
			{
				cmdCloseChat.perform();
			}
			return;
		}

		// 2. Touch 6 Danh Mục Lớn ở đỉnh
		int numCats = 6;
		int tabH = 20;
		int tabY = y + 8;
		int wTabs = wPaintTab - 22;
		int tabW = wTabs / numCats;

		if (GameCanvas.isPointSelect(xBe, tabY, wTabs, tabH))
		{
			GameCanvas.isPointerSelect = false;
			int clickedCat = (GameCanvas.px - xBe) / tabW;
			if (clickedCat >= 0 && clickedCat < numCats)
			{
				selectCategory(clickedCat);
				return;
			}
		}

		// 3. XỬ LÝ HỘP THƯ (CAT_MAIL)
		if (catSelect == ChatDetail.CAT_MAIL)
		{
			int curLeftX = xBe;
			int curLeftW = isSmallScreen ? wPaintTab : wLeft;

			if (!isSmallScreen || smallScreenState == 0)
			{
				// A. Touch 4 chip sub-filter (Tất cả, Lời mời, Quà, Hệ thống)
				int subH = 18;
				bool twoRows = (curLeftW < 160);
				int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
				if (GameCanvas.isPointSelect(curLeftX, yBe, curLeftW, totalSubH))
				{
					GameCanvas.isPointerSelect = false;
					int clickedSub = -1;
					if (twoRows)
					{
						int bW = (curLeftW - 4) / 2;
						int col = (GameCanvas.px - curLeftX) / (bW + 2);
						int row = (GameCanvas.py - yBe) / (subH + 2);
						if (col >= 0 && col < 2 && row >= 0 && row < 2)
						{
							clickedSub = row * 2 + col;
						}
					}
					else
					{
						int bW = (curLeftW - 6) / 4;
						int idx = (GameCanvas.px - curLeftX) / (bW + 2);
						if (idx >= 0 && idx < 4)
						{
							clickedSub = idx;
						}
					}

					if (clickedSub >= 0 && clickedSub < 4)
					{
						subFilterMail = clickedSub;
						curPageMail = 0;
						mVector pItems = getPageMailItems();
						if (pItems.size() > 0)
						{
							tabCur = (ChatDetail)pItems.elementAt(0);
							idSelect = vecTabChat.indexOf(tabCur);
							getRightCmd();
						}
						else
						{
							tabCur = null;
							idSelect = -1;
							getRightCmd();
						}
						updateCamLeftList();
						return;
					}
				}

				// B. Touch Ô tìm kiếm / Nút xóa search
				int searchY = yBe + totalSubH + 3;
				int searchH = 18;
				if (tfSearchMail != null)
				{
					int clrX = curLeftX + curLeftW - 18;
					if (GameCanvas.isPointSelect(clrX, searchY, 18, searchH))
					{
						GameCanvas.isPointerSelect = false;
						tfSearchMail.setText("");
						curPageMail = 0;
						updateCamLeftList();
						return;
					}
					if (GameCanvas.isPointSelect(curLeftX, searchY, curLeftW - 20, searchH))
					{
						tfSearchMail.updatePointer();
					}
				}

				// C. Touch Danh sách 10 mục của trang hiện tại
				int listY = searchY + searchH + 3;
				int pageBarH = 20;
				int listH = hItem - (totalSubH + searchH + pageBarH + 9);
				if (listH < 30) listH = 30;

				if (GameCanvas.isPointerSelect && GameCanvas.isPointSelect(curLeftX, listY, curLeftW, listH))
				{
					mVector pageItems = getPageMailItems();
					int itemH = 26;
					int clickedIdx = (GameCanvas.py - listY + CamLeftList.cmx) / itemH;
					if (clickedIdx >= 0 && clickedIdx < pageItems.size())
					{
						GameCanvas.isPointerSelect = false;
						ChatDetail selectedDetail = (ChatDetail)pageItems.elementAt(clickedIdx);
						if (tabCur != selectedDetail)
						{
							tabCur = selectedDetail;
							idSelect = vecTabChat.indexOf(tabCur);
							if (tabCur != null)
							{
								tabCur.isNew = false;
								tabCur.setLim();
								updateCameraNew(0, 0);
							}
							getRightCmd();
							updateAllTfPositions();
							GameScreen.setNumMess();
						}
						if (isSmallScreen)
						{
							smallScreenState = 1;
							getRightCmd();
						}
						return;
					}
				}

				// D. Touch Thanh trang (Pagination)
				int pageBarY = yBe + hItem - pageBarH;
				int arrowBtnW = 20;

				// Nút lùi [◀]
				if (GameCanvas.isPointSelect(curLeftX, pageBarY, arrowBtnW, pageBarH))
				{
					GameCanvas.isPointerSelect = false;
					if (curPageMail > 0)
					{
						curPageMail--;
						mVector pItems = getPageMailItems();
						if (pItems.size() > 0)
						{
							tabCur = (ChatDetail)pItems.elementAt(0);
							idSelect = vecTabChat.indexOf(tabCur);
							getRightCmd();
						}
						updateCamLeftList();
					}
					return;
				}

				// Nút tiến [▶]
				int nextX = curLeftX + curLeftW - arrowBtnW;
				if (GameCanvas.isPointSelect(nextX, pageBarY, arrowBtnW, pageBarH))
				{
					GameCanvas.isPointerSelect = false;
					if (curPageMail < totalPagesMail - 1)
					{
						curPageMail++;
						mVector pItems = getPageMailItems();
						if (pItems.size() > 0)
						{
							tabCur = (ChatDetail)pItems.elementAt(0);
							idSelect = vecTabChat.indexOf(tabCur);
							getRightCmd();
						}
						updateCamLeftList();
					}
					return;
				}
			}

			// E. Touch Vùng Chi Tiết Thư
			if (!isSmallScreen || smallScreenState == 1)
			{
				int curDetailX = (isSmallScreen && smallScreenState == 1) ? xBe : xRight;
				int curDetailW = (isSmallScreen && smallScreenState == 1) ? wPaintTab : wRight;

				// Nút "< Trở về" trong màn hình nhỏ
				if (isSmallScreen && smallScreenState == 1)
				{
					if (GameCanvas.isPointSelect(curDetailX, yBe, 65, 22))
					{
						GameCanvas.isPointerSelect = false;
						smallScreenState = 0;
						getRightCmd();
						GameScreen.setNumMess();
						return;
					}
				}

				if (tabCur != null && GameCanvas.isPointerSelect)
				{
					int btnBarH = 26;
					int btnY = yBe + hItem - btnBarH - 2;
					int btnH = 22;

					if (tabCur.isInvite)
					{
						if (tabCur.typeInvite == 3)
						{
							int btnW = (curDetailW - 8) / 3;
							int btn1X = curDetailX;
							int btn2X = curDetailX + btnW + 4;
							int btn3X = curDetailX + (btnW + 4) * 2;

							if (GameCanvas.isPointSelect(btn1X, btnY, btnW, btnH))
							{
								GameCanvas.isPointerSelect = false;
								if (tabCur.cmdAcceptInvite != null) tabCur.cmdAcceptInvite.perform();
								return;
							}
							if (GameCanvas.isPointSelect(btn2X, btnY, btnW, btnH))
							{
								GameCanvas.isPointerSelect = false;
								if (tabCur.cmdInfoEnemy != null) tabCur.cmdInfoEnemy.perform();
								return;
							}
							if (GameCanvas.isPointSelect(btn3X, btnY, btnW, btnH))
							{
								GameCanvas.isPointerSelect = false;
								if (tabCur.cmdDeclineInvite != null) tabCur.cmdDeclineInvite.perform();
								return;
							}
						}
						else
						{
							int btnW = (curDetailW - 6) / 2;
							int btn1X = curDetailX;
							int btn2X = curDetailX + btnW + 4;

							if (GameCanvas.isPointSelect(btn1X, btnY, btnW, btnH))
							{
								GameCanvas.isPointerSelect = false;
								if (tabCur.cmdAcceptInvite != null) tabCur.cmdAcceptInvite.perform();
								return;
							}
							if (GameCanvas.isPointSelect(btn2X, btnY, btnW, btnH))
							{
								GameCanvas.isPointerSelect = false;
								if (tabCur.cmdDeclineInvite != null) tabCur.cmdDeclineInvite.perform();
								return;
							}
						}
					}
					else
					{
						bool hasGiftToClaim = (!tabCur.isClaimed && tabCur.mItemgift != null && tabCur.mItemgift.Length > 0);
						bool canReply = (!tabCur.isNotReply && !string.IsNullOrEmpty(tabCur.name) && !isSystemTab(tabCur.name));

						if (hasGiftToClaim)
						{
							if (canReply)
							{
								int btnW = (curDetailW - 8) / 3;
								int btn1X = curDetailX;
								int btn2X = curDetailX + btnW + 4;
								int btn3X = curDetailX + (btnW + 4) * 2;

								if (GameCanvas.isPointSelect(btn1X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdNhanQua != null) tabCur.cmdNhanQua.perform();
									return;
								}
								if (GameCanvas.isPointSelect(btn2X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									string replyTarget = tabCur.name;
									addNewChat(replyTarget, "", "", 0, true, -1, ChatDetail.CAT_PRIVATE);
									if (tabCur != null && tabCur.tfchat != null)
									{
										tabCur.tfchat.setFocus(true);
									}
									return;
								}
								if (GameCanvas.isPointSelect(btn3X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdDelMail != null) tabCur.cmdDelMail.perform();
									return;
								}
							}
							else
							{
								int btnW = (curDetailW - 6) / 2;
								int btn1X = curDetailX;
								int btn2X = curDetailX + btnW + 4;

								if (GameCanvas.isPointSelect(btn1X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdNhanQua != null) tabCur.cmdNhanQua.perform();
									return;
								}
								if (GameCanvas.isPointSelect(btn2X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdDelMail != null) tabCur.cmdDelMail.perform();
									return;
								}
							}
						}
						else
						{
							if (canReply)
							{
								int btnW = (curDetailW - 6) / 2;
								int btn1X = curDetailX;
								int btn2X = curDetailX + btnW + 4;

								if (GameCanvas.isPointSelect(btn1X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									string replyTarget = tabCur.name;
									addNewChat(replyTarget, "", "", 0, true, -1, ChatDetail.CAT_PRIVATE);
									if (tabCur != null && tabCur.tfchat != null)
									{
										tabCur.tfchat.setFocus(true);
									}
									return;
								}
								if (GameCanvas.isPointSelect(btn2X, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdDelMail != null) tabCur.cmdDelMail.perform();
									return;
								}
							}
							else
							{
								int btnW = System.Math.Min(120, curDetailW - 10);
								int btnX = curDetailX + (curDetailW - btnW) / 2;
								if (GameCanvas.isPointSelect(btnX, btnY, btnW, btnH))
								{
									GameCanvas.isPointerSelect = false;
									if (tabCur.cmdDelMail != null) tabCur.cmdDelMail.perform();
									return;
								}
							}
						}
					}
				}
			}
		}
		// 4. XỬ LÝ TRÒ CHUYỆN RIÊNG (CAT_PRIVATE)
		else if (catSelect == ChatDetail.CAT_PRIVATE)
		{
			int curLeftX = xBe;
			int curLeftW = isSmallScreen ? wPaintTab : wLeft;

			if (!isSmallScreen || smallScreenState == 0)
			{
				if (GameCanvas.isPointerSelect && GameCanvas.isPointSelect(curLeftX, yBe, curLeftW, hItem))
				{
					mVector listItems = getListByCat(catSelect);
					int itemH = 26;
					int clickedIdx = (GameCanvas.py - yBe + CamLeftList.cmx) / itemH;
					if (clickedIdx >= 0 && clickedIdx < listItems.size())
					{
						GameCanvas.isPointerSelect = false;
						ChatDetail selectedDetail = (ChatDetail)listItems.elementAt(clickedIdx);
						if (tabCur != selectedDetail)
						{
							tabCur = selectedDetail;
							idSelect = vecTabChat.indexOf(tabCur);
							if (tabCur != null)
							{
								tabCur.isNew = false;
								tabCur.setLim();
								updateCameraNew(0, 0);
							}
							getRightCmd();
							updateAllTfPositions();
							GameScreen.setNumMess();
						}
						if (isSmallScreen)
						{
							smallScreenState = 1;
							getRightCmd();
						}
						return;
					}
				}
			}

			if (!isSmallScreen || smallScreenState == 1)
			{
				int curDetailX = (isSmallScreen && smallScreenState == 1) ? xBe : xRight;
				if (isSmallScreen && smallScreenState == 1)
				{
					if (GameCanvas.isPointSelect(curDetailX, yBe, 65, 22))
					{
						GameCanvas.isPointerSelect = false;
						smallScreenState = 0;
						getRightCmd();
						GameScreen.setNumMess();
						return;
					}
				}

				if (tabCur != null && tabCur.tfchat != null)
				{
					tabCur.tfchat.updatePointer();
					int btnSendW = 40;
					int btnSendH = tabCur.tfchat.height > 20 ? tabCur.tfchat.height : 22;
					int btnSendX = tabCur.tfchat.x + tabCur.tfchat.width + 4;
					int btnSendY = tabCur.tfchat.y;
					if (GameCanvas.isPointerSelect && GameCanvas.isPointSelect(btnSendX, btnSendY, btnSendW, btnSendH))
					{
						GameCanvas.isPointerSelect = false;
						tabCur.addStartChat(GameScreen.player.name);
						return;
					}
				}
			}
		}
		// 5. CÁC KÊNH CHAT KHÁC (Thế Giới, Công Cộng, Bang Hội, Hệ Thống)
		else
		{
			if (tabCur != null && tabCur.tfchat != null)
			{
				tabCur.tfchat.updatePointer();
				int btnSendW = 40;
				int btnSendH = tabCur.tfchat.height > 20 ? tabCur.tfchat.height : 22;
				int btnSendX = tabCur.tfchat.x + tabCur.tfchat.width + 4;
				int btnSendY = tabCur.tfchat.y;
				if (GameCanvas.isPointerSelect && GameCanvas.isPointSelect(btnSendX, btnSendY, btnSendW, btnSendH))
				{
					GameCanvas.isPointerSelect = false;
					tabCur.addStartChat(GameScreen.player.name);
					return;
				}
			}
		}

		base.updatePointer();
	}

	public override void keyPress(int keyCode)
	{
		if (keyCode == 10 || keyCode == 13 || keyCode == -5)
		{
			if (tabCur != null && tabCur.tfchat != null)
			{
				tabCur.addStartChat(GameScreen.player.name);
				return;
			}
		}
		if (catSelect == ChatDetail.CAT_MAIL && tfSearchMail != null && tfSearchMail.isFocused())
		{
			tfSearchMail.keyPressed(keyCode);
			return;
		}
		if (tabCur != null && tabCur.tfchat != null)
		{
			tabCur.tfchat.keyPressed(keyCode);
		}
		base.keyPress(keyCode);
	}

	public void updateCamLeftList()
	{
		if (catSelect == ChatDetail.CAT_MAIL)
		{
			mVector pageItems = getPageMailItems();
			int totalH = pageItems.size() * 26 + 10;
			int curLeftW = isSmallScreen ? wPaintTab : wLeft;
			int subH = 18;
			bool twoRows = (curLeftW < 160);
			int totalSubH = twoRows ? (subH * 2 + 3) : (subH + 2);
			int searchH = 18;
			int pageBarH = 20;
			int listH = hItem - (totalSubH + searchH + pageBarH + 9);
			if (listH < 30) listH = 30;
			int listY = yBe + totalSubH + 3 + searchH + 3;
			int lim = totalH - listH;
			if (lim < 0) lim = 0;

			if (CamLeftList == null)
			{
				CamLeftList = new ListNew(xBe, listY, curLeftW, listH, 0, 0, lim, isLim0: true);
			}
			else
			{
				CamLeftList.x = xBe;
				CamLeftList.y = listY;
				CamLeftList.maxW = curLeftW;
				CamLeftList.maxH = listH;
				CamLeftList.cmxLim = lim;
				if (CamLeftList.cmtoX > lim) CamLeftList.cmtoX = lim;
				if (CamLeftList.cmx > lim) CamLeftList.cmx = lim;
			}
		}
		else
		{
			mVector list = getListByCat(catSelect);
			int totalH = list.size() * 26 + 10;
			int lim = totalH - hItem;
			if (lim < 0) lim = 0;
			int curLeftW = isSmallScreen ? wPaintTab : wLeft;
			if (CamLeftList == null)
			{
				CamLeftList = new ListNew(xBe, yBe, curLeftW, hItem, 0, 0, lim, isLim0: true);
			}
			else
			{
				CamLeftList.x = xBe;
				CamLeftList.y = yBe;
				CamLeftList.maxW = curLeftW;
				CamLeftList.maxH = hItem;
				CamLeftList.cmxLim = lim;
				if (CamLeftList.cmtoX > lim) CamLeftList.cmtoX = lim;
				if (CamLeftList.cmx > lim) CamLeftList.cmx = lim;
			}
			scrLeftList.setInfo(xBe + curLeftW - 2, yBe, hItem, 8809550);
		}
	}

	public void updateCameraNew(int size, sbyte type)
	{
		if (tabCur == null)
		{
			return;
		}
		int topHeaderH = (isSmallScreen && smallScreenState == 1) ? 22 : 0;
		int btnBarH = 26;
		int num = (catSelect == ChatDetail.CAT_MAIL) ? (hItem - topHeaderH - btnBarH - 4) : hItem;
		if (tabCur.tfchat != null)
		{
			num -= tabCur.tfchat.height;
		}
		int numRows = (tabCur.mItemgift != null && tabCur.mItemgift.Length > 0) ? ((tabCur.mItemgift.Length + 1) / 2) : 0;
		int extraGiftH = (numRows > 0) ? (numRows * 32 + 28) : 0;
		if (tabCur.isInvite && tabCur.typeInvite == 3)
		{
			extraGiftH += 28;
		}
		int totalH = (catSelect == ChatDetail.CAT_MAIL)
			? ((tabCur.vecDetail.size() + 1) * GameCanvas.hText + extraGiftH + 8)
			: (tabCur.vecDetail.size() * GameCanvas.hText + 10);
		int lim = totalH - num;
		if (lim < 0)
		{
			lim = 0;
		}

		int curW = (catSelect == ChatDetail.CAT_MAIL || catSelect == ChatDetail.CAT_PRIVATE) ? ((isSmallScreen && smallScreenState == 1) ? wPaintTab : wRight) : wPaintTab;
		int curX = (catSelect == ChatDetail.CAT_MAIL || catSelect == ChatDetail.CAT_PRIVATE) ? ((isSmallScreen && smallScreenState == 1) ? xBe : xRight) : xBe;
		int curY = (catSelect == ChatDetail.CAT_MAIL) ? (yBe + topHeaderH + 2) : yBe;

		if (CamDetailChat == null)
		{
			CamDetailChat = new ListNew(curX, curY, curW, num, 0, 0, lim, isLim0: true);
		}
		else
		{
			CamDetailChat.x = curX;
			CamDetailChat.y = curY;
			CamDetailChat.maxW = curW;
			CamDetailChat.maxH = num;
			CamDetailChat.cmxLim = lim;
			if (CamDetailChat.cmtoX > lim) CamDetailChat.cmtoX = lim;
			if (CamDetailChat.cmx > lim) CamDetailChat.cmx = lim;
		}

		if (tabCur.scrChat != null)
		{
			tabCur.scrChat.setInfo(curX + curW + 1, curY, num, 8809550);
		}

		switch (type)
		{
		case 1:
		{
			int cmtoX = CamDetailChat.cmtoX;
			int num2 = ((cmtoX != 0 && cmtoX != CamDetailChat.cmxLim) ? ((cmtoX < CamDetailChat.cmxLim - num) ? 1 : 2) : 0);
			switch (num2)
			{
			case 0:
				CamDetailChat.setToX(CamDetailChat.cmxLim);
				break;
			case 1:
				CamDetailChat.setToX(cmtoX);
				break;
			default:
				CamDetailChat.setToX(cmtoX + size * GameCanvas.hText);
				break;
			}
			break;
		}
		case 0:
			CamDetailChat.setToX(0);
			CamDetailChat.cmx = 0;
			break;
		}
	}

	public void addWorldChat(string sender, string text)
	{
		addNewChat("Thế Giới", (!string.IsNullOrEmpty(sender)) ? (sender + ": ") : "", text, 0, false, -1, ChatDetail.CAT_WORLD);
	}

	public void addPublicChat(string sender, string text)
	{
		addNewChat("Công Cộng", (!string.IsNullOrEmpty(sender)) ? (sender + ": ") : "", text, 0, false, -1, ChatDetail.CAT_PUBLIC);
	}

	public void addSystemChat(string text)
	{
		addNewChat("Hệ Thống", "", text, 1, false, -1, ChatDetail.CAT_SYSTEM);
	}

	public void addClanChat(string sender, string text)
	{
		addNewChat(T.tabBangHoi, (!string.IsNullOrEmpty(sender)) ? (sender + ": ") : "", text, 0, false, -1, ChatDetail.CAT_CLAN);
	}

	public void addNewChat(string name, string FristContent, string content, sbyte type, bool isFocus)
	{
		addNewChat(name, FristContent, content, type, isFocus, -1, -1);
	}

	public void addNewChat(string name, string FristContent, string content, sbyte type, bool isFocus, int color)
	{
		addNewChat(name, FristContent, content, type, isFocus, color, -1);
	}

	public void addNewChat(string name, string FristContent, string content, sbyte type, bool isFocus, int color, sbyte cat)
	{
		if (content == null) return;

		sbyte targetCat = cat;
		if (targetCat < 0)
		{
			if (isWorldTab(name))
			{
				targetCat = ChatDetail.CAT_WORLD;
			}
			else if (type == ChatDetail.TYPE_CLAN_CHAT || isClanTab(name))
			{
				targetCat = ChatDetail.CAT_CLAN;
			}
			else if (type == ChatDetail.TYPE_SERVER || isSystemTab(name))
			{
				targetCat = ChatDetail.CAT_SYSTEM;
			}
			else if (isPublicTab(name))
			{
				targetCat = ChatDetail.CAT_PUBLIC;
			}
			else
			{
				targetCat = ChatDetail.CAT_PRIVATE;
			}
		}

		// 1. Tab Hệ Thống
		if (targetCat == ChatDetail.CAT_SYSTEM)
		{
			ChatDetail sysDetail = null;
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.typeCategory == ChatDetail.CAT_SYSTEM)
				{
					sysDetail = cd;
					break;
				}
			}
			if (sysDetail == null)
			{
				sysDetail = new ChatDetail(T.hethong, 1, ChatDetail.CAT_SYSTEM);
				vecTabChat.addElement(sysDetail);
			}

			string prefix = FristContent;
			if (string.IsNullOrEmpty(prefix) && !name.Equals(T.hethong) && !name.Equals("Hệ Thống"))
			{
				prefix = "[" + name + "] ";
			}
			if (content.Length > 0)
			{
				sysDetail.addNewChat(prefix + content, name, color);
			}
			if (isFocus)
			{
				tabCur = sysDetail;
				idSelect = vecTabChat.indexOf(sysDetail);
				catSelect = ChatDetail.CAT_SYSTEM;
				getCurTab(idSelect);
			}
			GameScreen.setNumMess();
			return;
		}

		// 2. Tab Thế Giới
		if (targetCat == ChatDetail.CAT_WORLD)
		{
			ChatDetail worldDetail = null;
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.typeCategory == ChatDetail.CAT_WORLD)
				{
					worldDetail = cd;
					break;
				}
			}
			if (worldDetail == null)
			{
				worldDetail = new ChatDetail("Thế Giới", 0, ChatDetail.CAT_WORLD);
				vecTabChat.addElement(worldDetail);
			}
			if (content.Length > 0)
			{
				worldDetail.addNewChat(FristContent + content, name, color);
			}
			if (isFocus)
			{
				tabCur = worldDetail;
				idSelect = vecTabChat.indexOf(worldDetail);
				catSelect = ChatDetail.CAT_WORLD;
				getCurTab(idSelect);
			}
			GameScreen.setNumMess();
			return;
		}

		// 3. Tab Công Cộng
		if (targetCat == ChatDetail.CAT_PUBLIC)
		{
			ChatDetail pubDetail = null;
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.typeCategory == ChatDetail.CAT_PUBLIC)
				{
					pubDetail = cd;
					break;
				}
			}
			if (pubDetail == null)
			{
				pubDetail = new ChatDetail("Công Cộng", 0, ChatDetail.CAT_PUBLIC);
				vecTabChat.addElement(pubDetail);
			}
			if (content.Length > 0)
			{
				pubDetail.addNewChat(FristContent + content, name, color);
			}
			if (isFocus)
			{
				tabCur = pubDetail;
				idSelect = vecTabChat.indexOf(pubDetail);
				catSelect = ChatDetail.CAT_PUBLIC;
				getCurTab(idSelect);
			}
			GameScreen.setNumMess();
			return;
		}

		// 4. Tab Bang Hội
		if (targetCat == ChatDetail.CAT_CLAN)
		{
			ChatDetail clanDetail = null;
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.typeCategory == ChatDetail.CAT_CLAN)
				{
					clanDetail = cd;
					break;
				}
			}
			if (clanDetail == null)
			{
				clanDetail = new ChatDetail(T.tabBangHoi, 0, ChatDetail.CAT_CLAN);
				vecTabChat.addElement(clanDetail);
			}
			if (content.Length > 0)
			{
				clanDetail.addNewChat(FristContent + content, name, color);
			}
			if (isFocus)
			{
				tabCur = clanDetail;
				idSelect = vecTabChat.indexOf(clanDetail);
				catSelect = ChatDetail.CAT_CLAN;
				getCurTab(idSelect);
			}
			Clan_Screen.isNew = true;
			GameScreen.setNumMess();
			return;
		}

		// 5. Tab Trò Chuyện Riêng
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail chatDetail = (ChatDetail)vecTabChat.elementAt(i);
			if (chatDetail != null && chatDetail.typeCategory == ChatDetail.CAT_PRIVATE && chatDetail.name.CompareTo(name) == 0)
			{
				if (content.Length > 0)
				{
					chatDetail.addNewChat(FristContent + content, name, color);
				}
				if (isFocus)
				{
					tabCur = chatDetail;
					idSelect = i;
					catSelect = ChatDetail.CAT_PRIVATE;
					getCurTab(idSelect);
				}
				GameScreen.setNumMess();
				return;
			}
		}

		ChatDetail chatDetail2 = new ChatDetail(name, type, ChatDetail.CAT_PRIVATE);
		if (content.Length > 0)
		{
			chatDetail2.addNewChat(FristContent + content, name, color);
		}
		vecTabChat.addElement(chatDetail2);
		if (isFocus)
		{
			tabCur = chatDetail2;
			idSelect = vecTabChat.size() - 1;
			catSelect = ChatDetail.CAT_PRIVATE;
			getCurTab(idSelect);
		}
		if (!GameCanvas.isTouch)
		{
			cmdClose.caption = T.close;
		}
		GameScreen.setNumMess();
	}

	public void addNewChatCheckSpam(string name, string FristContent, string content, sbyte type, bool isFocus)
	{
		if (MsgSpamSetup.isCheckSpam(2, name))
		{
			for (int i = 0; i < vecTabChat.size(); i++)
			{
				ChatDetail cd = (ChatDetail)vecTabChat.elementAt(i);
				if (cd != null && cd.typeCategory == ChatDetail.CAT_PRIVATE && cd.name.CompareTo(name) == 0)
				{
					addNewChat(name, FristContent, content, type, isFocus, -1, ChatDetail.CAT_PRIVATE);
					break;
				}
			}
		}
		else
		{
			addNewChat(name, FristContent, content, type, isFocus, -1, ChatDetail.CAT_PRIVATE);
		}
	}

	public void updateCmd()
	{
	}

	public void updateCamTab()
	{
	}

	public void checkRemoveTab(string name)
	{
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			if (((ChatDetail)vecTabChat.elementAt(i)).name.CompareTo(name) == 0)
			{
				vecTabChat.removeElementAt(i);
				break;
			}
		}
		idSelect = AvMain.resetSelect(idSelect, vecTabChat.size() - 1, isreset: false);
	}

	public void set_text_min_max()
	{
	}

	public virtual void getCurTab(int id)
	{
		idSelect = id;
		if (idSelect < 0 || idSelect >= vecTabChat.size())
		{
			return;
		}
		tabCur = (ChatDetail)vecTabChat.elementAt(idSelect);
		if (tabCur != null)
		{
			tabCur.isNew = false;
			tabCur.setLim();
			updateCameraNew(0, 0);
			getRightCmd();
			if (tabCur.typeCategory == ChatDetail.CAT_SYSTEM)
			{
				Player.strHethong = "";
			}
		}
		updateCamLeftList();
		GameScreen.setNumMess();
	}

	public void getRightCmd()
	{
		if (isSmallScreen && smallScreenState == 1)
		{
			right = cmdBackToList;
			backCMD = cmdBackToList;
		}
		else
		{
			right = cmdClose;
			backCMD = cmdClose;
		}

		if (tabCur == null)
		{
			center = null;
			left = null;
			return;
		}

		if (tabCur.isInvite)
		{
			center = tabCur.cmdAcceptInvite;
			left = (tabCur.typeInvite == 3) ? tabCur.cmdInfoEnemy : tabCur.cmdDeclineInvite;
		}
		else if (tabCur.isGiftMail && !tabCur.isClaimed)
		{
			center = tabCur.cmdNhanQua;
			left = tabCur.cmdDelMail;
		}
		else if (tabCur.tfchat != null)
		{
			tabCur.tfchat.setFocus(isFocus: true);
			center = cmdChat;
			left = null;
		}
		else
		{
			center = null;
			left = null;
		}
	}

	public void addNewInvite(int inviteId, sbyte typeInvite, string inviteName, string inviteInfo, int priceFight, int typeFight)
	{
		if (inviteName == null) inviteName = "";
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d != null && d.isInvite && d.inviteId == inviteId && d.typeInvite == typeInvite)
			{
				d.setInvite(inviteId, typeInvite, inviteName, inviteInfo, priceFight, typeFight);
				if (GameCanvas.currentScreen != GameCanvas.chatTabScr || tabCur != d)
				{
					d.isNew = true;
				}
				vecTabChat.removeElementAt(i);
				vecTabChat.insertElementAt(d, 0);
				saveMailCacheNow();
				GameScreen.setNumMess();
				return;
			}
		}

		ChatDetail chatDetail = new ChatDetail(inviteName, 1, ChatDetail.CAT_MAIL);
		chatDetail.setInvite(inviteId, typeInvite, inviteName, inviteInfo, priceFight, typeFight);
		chatDetail.isNew = true;

		vecTabChat.insertElementAt(chatDetail, 0);
		curPageMail = 0;

		mVector mailList = getListByCat(ChatDetail.CAT_MAIL);
		if (mailList.size() > 100)
		{
			ChatDetail oldest = (ChatDetail)mailList.elementAt(mailList.size() - 1);
			vecTabChat.removeElement(oldest);
		}

		saveMailCacheNow();

		if (GameCanvas.currentScreen == GameCanvas.chatTabScr && catSelect == ChatDetail.CAT_MAIL)
		{
			tabCur = chatDetail;
			idSelect = 0;
			getCurTab(idSelect);
		}
		if (!GameCanvas.isTouch)
		{
			cmdClose.caption = T.close;
		}
		GameScreen.setNumMess();
	}

	public void removeInvite(int inviteId, sbyte typeInvite)
	{
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d != null && d.isInvite && d.inviteId == inviteId && (typeInvite == -1 || d.typeInvite == typeInvite))
			{
				vecTabChat.removeElementAt(i);
				saveMailCacheNow();
				if (catSelect == ChatDetail.CAT_MAIL)
				{
					mVector list = getPageMailItems();
					if (list.size() > 0)
					{
						tabCur = (ChatDetail)list.elementAt(0);
						idSelect = vecTabChat.indexOf(tabCur);
						getCurTab(idSelect);
					}
					else
					{
						tabCur = null;
						idSelect = -1;
					}
				}
				GameScreen.setNumMess();
				break;
			}
		}
	}

	public void removeAllInvites()
	{
		for (int i = vecTabChat.size() - 1; i >= 0; i--)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d != null && d.isInvite)
			{
				vecTabChat.removeElementAt(i);
			}
		}
		saveMailCacheNow();
		if (catSelect == ChatDetail.CAT_MAIL)
		{
			mVector list = getPageMailItems();
			if (list.size() > 0)
			{
				tabCur = (ChatDetail)list.elementAt(0);
				idSelect = vecTabChat.indexOf(tabCur);
				getCurTab(idSelect);
			}
			else
			{
				tabCur = null;
				idSelect = -1;
			}
		}
		GameScreen.setNumMess();
	}

	public void addNewMail(string tabName, string sender, string content, sbyte maskNotReply, int mailId, string title, sbyte isClaimed, sbyte typeMail, Item_Drop[] gifts)
	{
		if (content == null) content = "";
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d != null && d.typeCategory == ChatDetail.CAT_MAIL && d.mailId == mailId && mailId > 0)
			{
				d.setMailGift(mailId, title, isClaimed, typeMail, gifts, maskNotReply == 1);
				d.contentRaw = content;
				if (content.Length > 0)
				{
					d.vecDetail.removeAllElements();
					d.addNewChat(content, tabName, -1);
				}
				if (GameCanvas.currentScreen != GameCanvas.chatTabScr || tabCur != d)
				{
					d.isNew = true;
				}
				vecTabChat.removeElementAt(i);
				vecTabChat.insertElementAt(d, 0);
				saveMailCacheNow();
				GameScreen.setNumMess();
				return;
			}
		}

		ChatDetail chatDetail = new ChatDetail(tabName, 1, ChatDetail.CAT_MAIL);
		chatDetail.setMailGift(mailId, title, isClaimed, typeMail, gifts, maskNotReply == 1);
		chatDetail.contentRaw = content;
		if (content.Length > 0)
		{
			chatDetail.addNewChat(content, tabName, -1);
		}
		chatDetail.isNew = true;

		vecTabChat.insertElementAt(chatDetail, 0);
		curPageMail = 0;

		mVector mailList = getListByCat(ChatDetail.CAT_MAIL);
		if (mailList.size() > 100)
		{
			ChatDetail oldest = (ChatDetail)mailList.elementAt(mailList.size() - 1);
			vecTabChat.removeElement(oldest);
		}

		saveMailCacheNow();

		if (GameCanvas.currentScreen == GameCanvas.chatTabScr && catSelect == ChatDetail.CAT_MAIL)
		{
			tabCur = chatDetail;
			idSelect = 0;
			getCurTab(idSelect);
		}
		if (!GameCanvas.isTouch)
		{
			cmdClose.caption = T.close;
		}
		GameScreen.setNumMess();
	}

	public void updateMailClaimed(int mailId, bool success)
	{
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d.mailId == mailId)
			{
				d.isClaimed = true;
				saveMailCacheNow();
				if (tabCur == d)
				{
					getCurTab(idSelect);
				}
				GameScreen.setNumMess();
				break;
			}
		}
	}

	public void removeMailTab(int mailId)
	{
		for (int i = 0; i < vecTabChat.size(); i++)
		{
			ChatDetail d = (ChatDetail)vecTabChat.elementAt(i);
			if (d.mailId == mailId)
			{
				vecTabChat.removeElementAt(i);
				saveMailCacheNow();
				mVector list = getPageMailItems();
				if (list.size() > 0)
				{
					tabCur = (ChatDetail)list.elementAt(0);
					idSelect = vecTabChat.indexOf(tabCur);
					getCurTab(idSelect);
				}
				else
				{
					tabCur = null;
					idSelect = -1;
				}
				GameScreen.setNumMess();
				break;
			}
		}
	}

	public override bool keyBack()
	{
		if (isSmallScreen && smallScreenState == 1)
		{
			smallScreenState = 0;
			getRightCmd();
			GameScreen.setNumMess();
			return true;
		}
		if (!base.keyBack() && cmdClose != null)
		{
			cmdClose.perform();
		}
		return false;
	}
}
