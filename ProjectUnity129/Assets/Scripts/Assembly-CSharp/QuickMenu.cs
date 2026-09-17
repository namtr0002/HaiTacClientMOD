using System;

public class QuickMenu : Menu
{
	public static QuickMenu instance;

	public static FrameImage[] fraQuickMenu;
	public static mImage imgNenMenu;
	public static mImage imgTamGiac;

	public ListNew list;
	private bool isClose;
	public int xEff;

	// ==========================================
	// 2-TIER STAGGERED RADIAL ARC PARAMETERS
	// ==========================================
	public float animProgress = 0f; // 0.0f -> 1.0f
	public int originX = 18;
	public int originY = 120;
	public int hoveredItem = -1;
	public int openDelay = 0;
	public bool waitForNewTouch = true;

	public struct ItemPos
	{
		public int targetX;
		public int targetY;
	}

	public ItemPos[] itemPositions;

	public static int[][] mValueCmd = new int[14][]
	{
		new int[2] { 0, 0 },
		new int[2] { 12, 12 },
		new int[2] { 7, 7 },
		new int[2] { 2, 2 },
		new int[2] { 10, 10 },
		new int[2] { 6, 6 },
		new int[2] { 14, 14 },
		new int[2] { 15, 15 },
		new int[2] { 13, 13 },
		new int[2] { 3, 3 },
		new int[2] { 5, 5 },
		new int[2] { 9, 8 },
		new int[2] { 11, 11 },
		new int[2] { 8, 9 }
	};

	public static QuickMenu gI()
	{
		if (instance != null)
		{
			return instance;
		}
		return instance = new QuickMenu();
	}

	public void startAt()
	{
		beginMenu();
		isClose = false;
		openDelay = 2;
		waitForNewTouch = false;
		cmdClose = new iCommand("", -1, this);
		menuSelectedItem = -1;
		hoveredItem = -1;
		animProgress = 0f;

		// Consume existing touches from the button tap that triggered this menu
		GameCanvas.isPointerSelect = false;
		GameCanvas.isPointerClick = false;
		GameCanvas.isPointerRelease = false;

		menuItems = new mVector();
		for (int i = 0; i < mValueCmd.Length; i++)
		{
			int cmdId = mValueCmd[i][0];
			int iconId = mValueCmd[i][1];

			if (cmdId == 6 && (Player.vecParty == null || Player.vecParty.size() <= 0))
			{
				continue;
			}
			if (cmdId == 10 && (GameScreen.player == null || GameScreen.player.clan == null))
			{
				continue;
			}

			iCommand itemCmd = new iCommand(getTextCmd(cmdId), cmdId, this);
			if (fraQuickMenu != null && iconId >= 0 && iconId < fraQuickMenu.Length && fraQuickMenu[iconId] != null)
			{
				itemCmd.setFraCaption(fraQuickMenu[iconId]);
			}
			menuItems.addElement(itemCmd);
		}

		wUni = 60;
		menuW = 50;
		originX = GameCanvas.isTaiTho ? 38 : 18;
		originY = MotherCanvas.h / 2;

		int total = menuItems.size();
		itemPositions = new ItemPos[total];

		// Adaptive Radii ensuring comfortable spacing without overlap
		int rInner = (MotherCanvas.h < 240) ? 46 : ((MotherCanvas.h < 320) ? 54 : 62);
		int rOuter = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);

		if (total <= 6)
		{
			float maxSpan = (MotherCanvas.h < 260) ? 58f : 66f;
			float stepDeg = (total > 1) ? (2 * maxSpan) / (total - 1) : 0f;
			float startDeg = -maxSpan;
			for (int i = 0; i < total; i++)
			{
				float deg = startDeg + i * stepDeg;
				double rad = deg * 3.1415926535897931 / 180.0;
				itemPositions[i].targetX = originX + (int)(rOuter * (float)System.Math.Cos(rad));
				itemPositions[i].targetY = originY + (int)(rOuter * (float)System.Math.Sin(rad));
			}
		}
		else
		{
			int innerCount = total / 2;
			int outerCount = total - innerCount;

			float maxSpanOut = (MotherCanvas.h < 260) ? 68f : 74f;
			float stepDegOut = (outerCount > 1) ? (2 * maxSpanOut) / (outerCount - 1) : 0f;
			float startDegOut = -maxSpanOut;

			// Staggered half-step offset for inner ring so inner items sit right between outer items
			float startDegIn = startDegOut + stepDegOut / 2.0f;
			float stepDegIn = stepDegOut;

			// Vòng trong so le
			for (int i = 0; i < innerCount; i++)
			{
				float deg = startDegIn + i * stepDegIn;
				double rad = deg * 3.1415926535897931 / 180.0;
				itemPositions[i].targetX = originX + (int)(rInner * (float)System.Math.Cos(rad));
				itemPositions[i].targetY = originY + (int)(rInner * (float)System.Math.Sin(rad));
			}

			// Vòng ngoài so le
			for (int i = 0; i < outerCount; i++)
			{
				float deg = startDegOut + i * stepDegOut;
				double rad = deg * 3.1415926535897931 / 180.0;
				int idx = innerCount + i;
				itemPositions[idx].targetX = originX + (int)(rOuter * (float)System.Math.Cos(rad));
				itemPositions[idx].targetY = originY + (int)(rOuter * (float)System.Math.Sin(rad));
			}
		}

		list = new ListNew(0, 0, wUni, MotherCanvas.h, 0, 0, 0, isLim0: true);
		resetBegin();
		isShowMenu = true;
		GameCanvas.ShowMenu(gI());
		backCMD = cmdClose;
	}

	public string getTextCmd(int index)
	{
		if (index >= 0 && index < T.mQuickMenu.Length)
		{
			return T.mQuickMenu[index];
		}
		return T.select;
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
			case -1:
				isClose = true;
				break;
			case 0:
			{
				mVector mVector3 = new mVector();
				mVector3.addElement(GameCanvas.gameScr.cmdFriendList);
				mVector3.addElement(GameCanvas.gameScr.cmdBlackList);
				GameCanvas.menu.startAt(mVector3, 2, T.danhsach);
				break;
			}
			case 1:
				GameCanvas.gameScr.cmdBlackList.perform();
				break;
			case 2:
				GameCanvas.gameScr.cmdAuto.perform();
				break;
			case 3:
			{
				mVector mVector2 = new mVector();
				mVector2.addElement(GameCanvas.gameScr.cmdMenuPk);
				mVector2.addElement(GameCanvas.gameScr.cmdSetDosat);
				GameCanvas.menu.startAt(mVector2, 2, T.chonco);
				break;
			}
			case 4:
				GameCanvas.gameScr.cmdSetDosat.perform();
				break;
			case 5:
				GameCanvas.gameScr.cmdChangeTouch.perform();
				break;
			case 6:
				GameCanvas.gameScr.cmdParty.perform();
				break;
			case 7:
				GameCanvas.gameScr.cmdQuickChat.perform();
				break;
			case 8:
				GameCanvas.gameScr.cmdLogOut.perform();
				break;
			case 9:
				GameCanvas.gameScr.cmdShowWC.perform();
				break;
			case 10:
				GameCanvas.gameScr.cmdClan.perform();
				break;
			case 11:
				if (!GameCanvas.isIos())
				{
					GameCanvas.gameScr.cmdBuyGem.perform();
				}
				break;
			case 12:
				GameCanvas.gameScr.cmdUniform.perform();
				break;
			case 13:
				GameCanvas.gameScr.cmdDauGia.perform();
				break;
			case 14:
				GameCanvas.gameScr.cmdSudo.perform();
				break;
			case 15:
				GameCanvas.gameScr.cmdPet.perform();
				break;
		}
	}

	public override void paintMenu(mGraphics g)
	{
		GameCanvas.resetTrans(g);

		if (menuItems == null || menuItems.size() == 0 || itemPositions == null) return;

		int total = menuItems.size();

		// 1. Vẽ tâm bánh lái
		if (AvMain.fraBtBanhlai != null)
		{
			AvMain.fraBtBanhlai.drawFrame(0, originX, originY, 0, 3, g);
		}

		// 2. Vẽ các tia laser năng lượng nối từ tâm bánh lái ra các icon
		for (int i = 0; i < total && i < itemPositions.Length; i++)
		{
			int itemX = originX + (int)((itemPositions[i].targetX - originX) * animProgress);
			int itemY = originY + (int)((itemPositions[i].targetY - originY) * animProgress);
			bool isSel = (i == menuSelectedItem || i == hoveredItem);

			if (isSel)
			{
				g.setColor(16768815); // Vàng kim sáng rực
				g.drawLine(originX, originY, itemX, itemY);
				g.drawLine(originX, originY - 1, itemX, itemY - 1);
			}
			else
			{
				g.setColor(10515250); // Nâu gỗ trầm thanh lịch
				g.drawLine(originX, originY, itemX, itemY);
			}
		}

		// 3. Vẽ từng icon box với hiệu ứng viền kim loại & highlight
		for (int i = 0; i < total && i < itemPositions.Length; i++)
		{
			int itemX = originX + (int)((itemPositions[i].targetX - originX) * animProgress);
			int itemY = originY + (int)((itemPositions[i].targetY - originY) * animProgress);
			bool isSel = (i == menuSelectedItem || i == hoveredItem);

			int size = isSel ? 28 : 26;

			// Khung nền bo góc
			AvMain.paintRect(g, itemX - size / 2, itemY - size / 2, size, size, (sbyte)(isSel ? 1 : 0), (isSel ? 0 : 1));

			if (isSel && AvMain.imgNenfocus != null)
			{
				g.drawRegion(AvMain.imgNenfocus, 2, 2, size, size, 0, itemX - size / 2, itemY - size / 2, 0);
			}

			// Vẽ icon
			iCommand cmd = (iCommand)menuItems.elementAt(i);
			if (cmd != null)
			{
				cmd.paintOnlyImage(g, itemX, itemY, (sbyte)(isSel ? 1 : 0));
			}
		}

		// 4. Hiển thị Banner HUD Tên Chức Năng ở vị trí đỉnh thông thoáng (không bị ngón tay che)
		int selIdx = (hoveredItem >= 0) ? hoveredItem : menuSelectedItem;
		if (selIdx >= 0 && selIdx < total && animProgress >= 0.7f)
		{
			iCommand selCmd = (iCommand)menuItems.elementAt(selIdx);
			if (selCmd != null && selCmd.caption != null && selCmd.caption.Length > 0)
			{
				int tw = mFont.tahoma_7b_white.getWidth(selCmd.caption);
				int hudW = tw + 20;
				int hudH = 18;
				int hudX = originX + 28;
				int rOutMax = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);
				int hudY = originY - rOutMax - 14;
				if (hudY < 4) hudY = 4;
				if (hudX + hudW > MotherCanvas.w - 4) hudX = MotherCanvas.w - hudW - 4;

				AvMain.paintRect(g, hudX, hudY, hudW, hudH, 1, 0);
				mFont.tahoma_7b_white.drawString(g, selCmd.caption, hudX + hudW / 2, hudY + 3, mFont.CENTER);
			}
		}
	}

	public override void updateMenu()
	{
		if (!isClose)
		{
			if (animProgress < 1.0f)
			{
				animProgress += (1.0f - animProgress) * 0.40f + 0.06f;
				if (animProgress >= 1.0f) animProgress = 1.0f;
			}
			updatePointer();
		}
		else
		{
			animProgress -= 0.22f;
			if (animProgress <= 0.05f)
			{
				isShowMenu = false;
				animProgress = 0f;
			}
		}
	}

	public override void updatePointer()
	{
		if (isClose || menuItems == null || itemPositions == null) return;

		// Bỏ qua các sự kiện chạm/nhả trong những frame đầu vừa mở menu
		if (openDelay > 0)
		{
			openDelay--;
			GameCanvas.isPointerSelect = false;
			GameCanvas.isPointerRelease = false;
			GameCanvas.isPointerClick = false;
			return;
		}

		// Đợi nhả ngón tay từ nút đã bấm mở menu trước đó
		if (waitForNewTouch)
		{
			if (GameCanvas.isPointerRelease || (!GameCanvas.isPointerDown && !GameCanvas.isPointerSelect))
			{
				waitForNewTouch = false;
			}
			GameCanvas.isPointerSelect = false;
			GameCanvas.isPointerClick = false;
			GameCanvas.isPointerRelease = false;
			return;
		}

		int total = menuItems.size();
		bool isSelect = GameCanvas.isPointerSelect || GameCanvas.isPointerClick;
		bool isRelease = GameCanvas.isPointerRelease;

		int px = GameCanvas.px;
		int py = GameCanvas.py;

		// 1. Tìm nút gần nhất và cập nhật hover/focus
		int dxCenter = px - originX;
		int dyCenter = py - originY;
		int distCenterSq = dxCenter * dxCenter + dyCenter * dyCenter;

		int bestIdx = -1;
		int bestDistSq = 999999;
		int grabRadius = (MotherCanvas.h < 260) ? 22 : 28;
		int grabRadiusSq = grabRadius * grabRadius;

		for (int i = 0; i < total && i < itemPositions.Length; i++)
		{
			int itemX = originX + (int)((itemPositions[i].targetX - originX) * animProgress);
			int itemY = originY + (int)((itemPositions[i].targetY - originY) * animProgress);
			int dx = px - itemX;
			int dy = py - itemY;
			int dSq = dx * dx + dy * dy;

			if (dSq < bestDistSq)
			{
				bestDistSq = dSq;
				bestIdx = i;
			}
		}

		if (bestDistSq <= grabRadiusSq)
		{
			hoveredItem = bestIdx;
			menuSelectedItem = bestIdx;
		}
		else if (bestDistSq > 40 * 40)
		{
			hoveredItem = -1;
		}

		// 2. Xử lý CLICK / TAP (Chọn trực tiếp bằng click chuột hoặc chạm màn hình)
		if (isSelect)
		{
			GameCanvas.isPointerSelect = false;
			GameCanvas.isPointerClick = false;
			GameCanvas.isPointerRelease = false;

			if (bestDistSq <= grabRadiusSq && bestIdx >= 0 && bestIdx < menuItems.size())
			{
				// Nhấp trực tiếp vào 1 icon chức năng
				((iCommand)menuItems.elementAt(bestIdx)).perform();
				isClose = true;
				hoveredItem = -1;
				return;
			}
			else if (distCenterSq < 25 * 25)
			{
				// Nhấp vào tâm bánh lái để đóng menu
				isClose = true;
				hoveredItem = -1;
				return;
			}
			else if (animProgress >= 0.85f)
			{
				// Chỉ đóng khi nhấp ra ngoài nếu menu đã bung nở xong hoàn toàn
				int rOutMax = (MotherCanvas.h < 240) ? 82 : ((MotherCanvas.h < 320) ? 98 : 114);
				if (distCenterSq > (rOutMax + 45) * (rOutMax + 45))
				{
					isClose = true;
					hoveredItem = -1;
					return;
				}
			}
		}

		// 3. Xử lý RELEASE (Thả tay sau khi giữ kéo ra một nút)
		if (isRelease)
		{
			GameCanvas.isPointerRelease = false;
			GameCanvas.isPointerSelect = false;
			GameCanvas.isPointerClick = false;

			if (hoveredItem >= 0 && hoveredItem < menuItems.size())
			{
				((iCommand)menuItems.elementAt(hoveredItem)).perform();
				isClose = true;
				hoveredItem = -1;
				return;
			}
			// Nếu thả tay khi không trúng nút nào: giữ nguyên menu hiển thị trên màn hình
		}
	}

	public override void updateMenuKey()
	{
		if (menuItems == null || menuItems.size() == 0) return;

		if (GameCanvas.keyMyHold[41] || GameCanvas.keyActionUni(4))
		{
			GameCanvas.clearKeyHold(41);
			GameCanvas.ClearActionUni(4);
			isClose = true;
			hoveredItem = -1;
			return;
		}

		bool flag = false;
		if (GameCanvas.keyMove(0) || GameCanvas.keyMove(1))
		{
			if (menuSelectedItem <= 0)
			{
				menuSelectedItem = menuItems.size() - 1;
			}
			else
			{
				menuSelectedItem--;
			}
			GameCanvas.ClearkeyMove(0);
			GameCanvas.ClearkeyMove(1);
			flag = true;
		}
		else if (GameCanvas.keyMove(2) || GameCanvas.keyMove(3))
		{
			if (menuSelectedItem < 0 || menuSelectedItem >= menuItems.size() - 1)
			{
				menuSelectedItem = 0;
			}
			else
			{
				menuSelectedItem++;
			}
			GameCanvas.ClearkeyMove(2);
			GameCanvas.ClearkeyMove(3);
			flag = true;
		}

		if (flag)
		{
			menuSelectedItem = AvMain.resetSelect(menuSelectedItem, menuItems.size() - 1, false);
		}

		if (GameCanvas.keyMyHold[5])
		{
			GameCanvas.clearKeyHold(5);
			if (menuSelectedItem < menuItems.size() && menuSelectedItem >= 0)
			{
				((iCommand)menuItems.elementAt(menuSelectedItem)).perform();
				isClose = true;
			}
		}
		updatekeyPC();
	}
}
