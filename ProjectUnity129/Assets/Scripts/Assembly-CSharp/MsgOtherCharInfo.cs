public class MsgOtherCharInfo : MsgDialog
{
	private MainObject obj;

	private int sizeBanner = 120;

	private int timeShowInfo;

	private MainItem itemCur;

	private int xInfo;

	private int yInfo;

	private int ylechthanhtich;

	private int yLechChar;

	private bool isShowInfo;

	private bool isCheckTop;

	public static InfoMemList infoFight;

	private iCommand cmdfight;

	private int lastTick;

	private int framepaint;

	// Cuộn trang bị lên xuống mượt mà như menu bản thân
	public const int EQUIP_PAGE_H = 104;

	private int equipScrollY = 0;

	private int equipToY = 0;

	private int equipSubTab = 0; // 0: Trang bị, 1: Thần trang

	private bool isDraggingEquip = false;

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

	public MsgOtherCharInfo(MainObject obj)
	{
		this.obj = obj;
		cmdfight = null;
		if (obj == null)
		{
			return;
		}
		cmdList.removeAllElements();
		cmdClose = new iCommand(T.close, 1, this);
		cmdClose = AvMain.setPosCMD(cmdClose, 2);
		if (infoFight != null && infoFight.name.CompareTo(this.obj.name) == 0)
		{
			cmdfight = new iCommand(T.chapnhan, 2, this);
			cmdfight = AvMain.setPosCMD(cmdfight, 0);
			cmdfight = AvMain.setPosCMD(cmdfight, 1);
			left = cmdfight;
		}
		right = cmdClose;
		if (GameCanvas.isSmallScreen || GameCanvas.isCompactMode())
		{
			wDia = 158;
			wItem = 23;
		}
		else
		{
			wDia = 176;
			wItem = 26;
		}
		if (wDia > MotherCanvas.w - 10)
		{
			wDia = MotherCanvas.w - 10;
		}
		maxWShow = wDia;
		wShowPaper = maxWShow;

		ylechthanhtich = 0;
		if (GameScreen.vecPlayers != null)
		{
			for (int i = 0; i < GameScreen.vecPlayers.size(); i++)
			{
				MainObject mainObject = (MainObject)GameScreen.vecPlayers.elementAt(i);
				if (mainObject != null && !mainObject.isRemove && mainObject.typeObject == 0 && mainObject.name.CompareTo(obj.name) == 0)
				{
					obj.thanhtichLv = mainObject.thanhtichLv;
					obj.thanhtichPvP = mainObject.thanhtichPvP;
					break;
				}
			}
		}
		if (obj.thanhtichLv >= 0)
		{
			ylechthanhtich += 14;
		}
		if (obj.thanhtichPvP >= 0)
		{
			ylechthanhtich += 14;
		}
		isCheckTop = true;

		yLechChar = ylechthanhtich;
		if (obj.hOne > 52)
		{
			yLechChar += obj.hOne - 52;
		}

		int maxH = MotherCanvas.h - 28 - GameCanvas.hCommand;
		hDia = (obj.clan != null ? 180 : 168);
		if (hDia > maxH)
		{
			hDia = maxH;
		}
		if (hDia < 148)
		{
			hDia = 148;
		}

		xDia = MotherCanvas.hw - wDia / 2;
		yDia = MotherCanvas.hh - hDia / 2;
		if (yDia < 22)
		{
			yDia = 22;
		}

		tabBtnW = (wDia - 24) / 2;
		tabBtnH = 14;

		cmdSwitchPage = new iCommand("Thần trang", 3, this);
		cmdSwitchPage = AvMain.setPosCMD(cmdSwitchPage, 0);

		if (cmdfight != null)
		{
			left = cmdfight;
		}
		else if (!GameCanvas.isTouch)
		{
			left = cmdSwitchPage;
		}

		if (GameCanvas.isTouch)
		{
			idSelect = -1;
			cmdClose.setPos(xDia + maxWShow / 2 + sizeBanner / 2, yDia - 20 + 8, MainTab.fraCloseTab, "");
			cmdList.addElement(cmdClose);
			if (cmdfight != null)
			{
				cmdfight = AvMain.setPosCMD(cmdfight, 0);
				cmdList.addElement(cmdfight);
			}
		}
		else
		{
			idSelect = 0;
			isShowInfo = false;
			itemCur = (MainItem)obj.hashEquip.get("0");
		}
		backCMD = cmdClose;
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
		case 0:
			doMenu();
			infoFight = null;
			break;
		case 1:
			GameCanvas.end_Dialog();
			infoFight = null;
			break;
		case 2:
		{
			if (infoFight == null)
			{
				return;
			}
			GlobalService.gI().Fight(1, (short)infoFight.id, 0);
			if (GameCanvas.eventScr.vecPlayer == null)
			{
				return;
			}
			for (int i = 0; i < GameCanvas.eventScr.vecPlayer.size(); i++)
			{
				InfoMemList infoMemList = (InfoMemList)GameCanvas.eventScr.vecPlayer.elementAt(i);
				if (infoMemList == infoFight)
				{
					GameCanvas.eventScr.vecPlayer.removeElement(infoMemList);
					break;
				}
			}
			return;
		}
		case 3:
			equipToY = (equipToY == 0) ? EQUIP_PAGE_H : 0;
			cmdSwitchPage.caption = (equipToY == 0) ? "Thần trang" : "Trang bị";
			isShowInfo = false;
			timeShowInfo = 0;
			if (idSelect >= 0 && idSelect < 16)
			{
				itemCur = (MainItem)obj.hashEquip.get(idSelect.ToString() ?? "");
			}
			else
			{
				itemCur = null;
			}
			return;
		}
		base.commandPointer(index, subIndex);
	}

	private void doMenu()
	{
	}

	private void paintEquipSlot(mGraphics g, int sx, int sy, int size, int equipType)
	{
		bool isSel = (idSelect == equipType);
		AvMain.paintRect(g, sx, sy, size, size, (sbyte)(isSel ? 1 : 0), 3);

		MainItem item = (MainItem)obj.hashEquip.get(equipType.ToString() ?? "");
		if (item != null)
		{
			item.paintColor(g, sx + size / 2, sy + size / 2, size - 2);
			item.paint(g, sx + size / 2, sy + size / 2, size, 1);
		}
		else if (equipType < 8 && AvMain.fraEquip != null)
		{
			int frameIdx = equipType % 8;
			if (frameIdx < AvMain.fraEquip.nFrame)
			{
				AvMain.fraEquip.drawFrame(frameIdx, sx + size / 2, sy + size / 2, 0, 3, g);
			}
		}

		if (isSel)
		{
			g.setColor(16776960);
			g.drawRect(sx - 1, sy - 1, size + 1, size + 1);
			if (AvMain.imgNenfocus != null)
			{
				g.drawRegion(AvMain.imgNenfocus, 2, 2, size, size, 0, sx, sy, 0);
			}
		}
	}

	public override void paint(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		int num2 = xDia + wDia / 2;
		paintPaper_UpDown(g, xDia - 5, yDia - 32, maxWShow + 10, hDia + 44, maxWShow + 10);
		g.setColor(15972174);
		g.fillRoundRect(xDia + wDia / 2 - sizeBanner / 2, yDia - 20, sizeBanner, 16, 4, 4);
		AvMain.FontBorderColorAuto(g, obj.name, xDia + maxWShow / 2, yDia - 18, 2, 6, 5, sizeBanner - 6);
		g.setClip(MotherCanvas.hw - maxWShow / 2, yDia, maxWShow, hDia);
		g.saveCanvas();
		g.ClipRec(MotherCanvas.hw - maxWShow / 2, yDia, maxWShow, hDia);

		int yCur = yDia + 4;
		if (obj.clan != null)
		{
			MainImage iconClan = Potion.getIconClan(obj.clan.idIcon);
			if (iconClan != null && iconClan.img != null)
			{
				int num3 = -mFont.tahoma_7b_black.getWidth(obj.clan.name) / 2;
				if (iconClan.frame == -1)
				{
					iconClan.set_Frame();
				}
				if (iconClan.frame <= 1)
				{
					g.drawImage(iconClan.img, xDia + wDia / 2 + num3, yCur, 3);
				}
				else
				{
					int num4 = ((framepaint < iconClan.frame - 1) ? 3 : 15);
					if (CRes.abs(GameCanvas.gameTick - lastTick) > num4)
					{
						framepaint++;
						if (framepaint >= iconClan.frame)
						{
							framepaint = 0;
						}
						lastTick = GameCanvas.gameTick;
					}
					g.drawRegion(iconClan.img, 0, framepaint * iconClan.w, iconClan.w, iconClan.w, 0, xDia + wDia / 2 + num3, yCur, 3);
				}
				mFont.tahoma_7b_black.drawString(g, obj.clan.name, xDia + wDia / 2 + 9, yCur - 6, 2);
				yCur += 14;
			}
			else
			{
				yCur += 2;
			}
		}
		else
		{
			yCur += 2;
		}

		// Thanh HP & MP
		int num5 = 47;
		mImage image = ((obj.Lv < 100) ? Interface_Game.imgIconMPHP : Interface_Game.imgIconMPHP2);
		g.drawImage(image, num2 - num5 + 7, yCur, 0);
		Interface_Game.PaintHPMP(g, 1, obj.Hp, obj.maxHp, num2 - num5 + 18, yCur, 0, 9, 66, 0, isflip: false, 0, isUpdateEff: false, obj.lvHeart);
		yCur += 11;
		Interface_Game.PaintHPMP(g, 2, obj.Mp, obj.maxMp, num2 - num5 + 18, yCur, 0, 9, 66, 0, isflip: false, 0, isUpdateEff: false, 0);
		yCur += 10;

		// Cấp độ & % Kinh nghiệm
		int num6 = 0;
		if (obj.Lv >= 100)
		{
			mFont.tahoma_7_black.drawString(g, obj.LvThongThao + " + " + obj.percentThongThao / 10 + "," + obj.percentThongThao % 10 + "%", num2 - num5 + 20, yCur, 0);
			yCur += 10;
			num6 = obj.percentThongThao / 10 * 70 / 100;
		}
		else
		{
			mFont.tahoma_7_black.drawString(g, obj.Lv + " + " + obj.percentLv / 10 + "," + obj.percentLv % 10 + "%", num2 - num5 + 20, yCur, 0);
			yCur += 10;
			num6 = obj.percentLv / 10 * 70 / 100;
		}
		g.setColor(1258003);
		g.fillRect(num2 - num5 + 18, yCur, 65, 2);
		if (num6 > 0)
		{
			g.setColor(3514158);
			g.fillRect(num2 - num5 + 18, yCur, num6, 2);
		}
		for (int i = 1; i < 5; i++)
		{
			g.setColor(16777215);
			g.fillRect(num2 - num5 + 18 + i * 13, yCur, 1, 2);
		}
		yCur += 6;

		// --- 2 Nút Tab: [Trang Bị] và [Thần Trang] kiểu DualTabScreen ---
		tabBtnY = yCur;
		int btn0_x = xDia + 8;
		int btn1_x = xDia + 12 + tabBtnW;
		bool isTab0 = (equipSubTab == 0);
		bool isTab1 = (equipSubTab == 1);

		AvMain.paintRect(g, btn0_x, tabBtnY, tabBtnW, tabBtnH, (sbyte)(isTab0 ? 1 : 0), (isTab0 ? 0 : 1));
		if (isTab0 && AvMain.imgNenfocus != null)
		{
			g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBtnH, 0, btn0_x, tabBtnY, 0);
		}
		if (isTab0)
		{
			mFont.tahoma_7b_yellow.drawString(g, "Trang Bị", btn0_x + tabBtnW / 2, tabBtnY + 1, 2);
		}
		else
		{
			mFont.tahoma_7_black.drawString(g, "Trang Bị", btn0_x + tabBtnW / 2, tabBtnY + 1, 2);
		}

		AvMain.paintRect(g, btn1_x, tabBtnY, tabBtnW, tabBtnH, (sbyte)(isTab1 ? 1 : 0), (isTab1 ? 0 : 1));
		if (isTab1 && AvMain.imgNenfocus != null)
		{
			g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBtnH, 0, btn1_x, tabBtnY, 0);
		}
		if (isTab1)
		{
			mFont.tahoma_7b_yellow.drawString(g, "Thần Trang", btn1_x + tabBtnW / 2, tabBtnY + 1, 2);
		}
		else
		{
			mFont.tahoma_7_black.drawString(g, "Thần Trang", btn1_x + tabBtnW / 2, tabBtnY + 1, 2);
		}
		yCur += tabBtnH + 4;

		// --- Viewport Cuộn Trang Bị Lên Xuống (104px) ---
		int equipAreaX = xDia + 4;
		int equipAreaW = wDia - 8;
		viewClipY = yCur;
		viewClipH = EQUIP_PAGE_H;
		if (viewClipY + viewClipH > yDia + hDia - 4)
		{
			viewClipH = yDia + hDia - 4 - viewClipY;
		}
		if (viewClipH < 60) viewClipH = 60;

		leftSlotX = equipAreaX + 6;
		rightSlotX = equipAreaX + equipAreaW - wItem - 6;
		int charCenterX = xDia + wDia / 2;

		g.setClip(equipAreaX, viewClipY, equipAreaW, viewClipH);
		g.translate(0, -equipScrollY);

		// --- PAGE 0: TRANG BỊ THƯỜNG (0..7) ---
		int page0Y = viewClipY;
		for (int i = 0; i < 4; i++)
		{
			paintEquipSlot(g, leftSlotX, page0Y + 2 + i * slotGapY, wItem, i * 2);
			paintEquipSlot(g, rightSlotX, page0Y + 2 + i * slotGapY, wItem, i * 2 + 1);
		}
		int charCenterY0 = page0Y + 80;
		if (MainObject.imgShadow != null)
		{
			g.drawImage(MainObject.imgShadow, charCenterX, charCenterY0 + 4, 3);
		}
		obj.paintThanhTich(g, page0Y + 14, charCenterX);
		obj.paintCharShow(g, charCenterX, charCenterY0, 0, isNhip: true);

		// --- PAGE 1: THẦN TRANG (8..15) ---
		int page1Y = viewClipY + EQUIP_PAGE_H;
		for (int i = 0; i < 4; i++)
		{
			paintEquipSlot(g, leftSlotX, page1Y + 2 + i * slotGapY, wItem, 8 + i * 2);
			paintEquipSlot(g, rightSlotX, page1Y + 2 + i * slotGapY, wItem, 8 + i * 2 + 1);
		}
		int charCenterY1 = page1Y + 80;
		if (MainObject.imgShadow != null)
		{
			g.drawImage(MainObject.imgShadow, charCenterX, charCenterY1 + 4, 3);
		}
		obj.paintThanhTich(g, page1Y + 14, charCenterX);
		short cw = -2, ch = -2, cb = -2, cl = -2;
		MainItem eq8 = (MainItem)obj.hashEquip.get("8");
		if (eq8 != null) cw = (eq8.idPart > 0) ? eq8.idPart : ((eq8.ID == 2603) ? ((short)184) : ((short)-2));
		MainItem eq9 = (MainItem)obj.hashEquip.get("9");
		if (eq9 != null) ch = (eq9.idPart > 0) ? eq9.idPart : ((eq9.ID == 2600) ? ((short)222) : ((short)-2));
		MainItem eq11 = (MainItem)obj.hashEquip.get("11");
		if (eq11 != null) cb = (eq11.idPart > 0) ? eq11.idPart : ((eq11.ID == 2602) ? ((short)223) : ((short)-2));
		MainItem eq13 = (MainItem)obj.hashEquip.get("13");
		if (eq13 != null) cl = (eq13.idPart > 0) ? eq13.idPart : ((eq13.ID == 2601) ? ((short)224) : ((short)-2));
		obj.paintCharShowWithOverride(g, charCenterX, charCenterY1, 0, isNhip: true, cw, ch, cb, cl);

		g.translate(0, equipScrollY);
		g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

		mGraphics.resetTransAndroid(g);
		g.restoreCanvas();
		GameCanvas.resetTrans(g);
		if (cmdList != null)
		{
			for (int k = 0; k < cmdList.size(); k++)
			{
				iCommand iCommand2 = (iCommand)cmdList.elementAt(k);
				iCommand2.paint(g, iCommand2.xCmd, iCommand2.yCmd);
			}
		}
		paintCmd(g);
		if (isShowInfo && itemCur != null)
		{
			MainTab.paintInfoEveryWhere(g, itemCur, null, 0, xInfo, yInfo, itemCur.wInfo, itemCur.hInfo, isLv: false, obj, 0);
		}
	}

	public override void update()
	{
		if (isClose)
		{
			updateClose();
			return;
		}

		// Smooth scroll animation
		if (!isDraggingEquip && equipScrollY != equipToY)
		{
			int d = (equipToY - equipScrollY) / 3;
			if (d == 0) d = (equipToY > equipScrollY) ? 1 : -1;
			equipScrollY += d;
			if (CRes.abs(equipToY - equipScrollY) < 2)
			{
				equipScrollY = equipToY;
			}
		}
		equipSubTab = (equipScrollY > EQUIP_PAGE_H / 2) ? 1 : 0;

		if (itemCur != null && !isShowInfo)
		{
			timeShowInfo++;
			if (timeShowInfo >= 10 && idSelect >= 0 && idSelect < 16)
			{
				isShowInfo = true;
				int page = idSelect / 8;
				int idx = idSelect % 8;
				int row = idx / 2;
				int col = idx % 2;
				int sx = (col == 0) ? leftSlotX : rightSlotX;
				int sy = viewClipY + page * EQUIP_PAGE_H + 2 + row * slotGapY - equipScrollY;
				setPosInfo(itemCur, sx + wItem / 2, sy + wItem);
			}
		}
		updateOpen();
		if (GameCanvas.isTouchNoOrPC())
		{
			updatekey();
		}
		updatePointer();
	}

	public override void updatekey()
	{
		bool flag = false;
		int num = idSelect;
		if (idSelect == -1 && (GameCanvas.keyMove(0) || GameCanvas.keyMove(2) || GameCanvas.keyMove(1) || GameCanvas.keyMove(3) || GameCanvas.keyMove(5)))
		{
			idSelect = 0;
			GameCanvas.clearKeyHold();
			flag = true;
		}

		if (GameCanvas.keyMyHold[10] || GameCanvas.keyMyHold[11])
		{
			GameCanvas.clearKeyHold(10);
			GameCanvas.clearKeyHold(11);
			equipToY = (equipToY == 0) ? EQUIP_PAGE_H : 0;
			cmdSwitchPage.caption = (equipToY == 0) ? "Thần trang" : "Trang bị";
			flag = true;
		}

		if (GameCanvas.keyMove(0))
		{
			if (idSelect % 2 == 1)
			{
				idSelect--;
			}
			GameCanvas.ClearkeyMove(0);
			flag = true;
		}
		else if (GameCanvas.keyMove(2))
		{
			if (idSelect % 2 == 0 && idSelect + 1 < 16)
			{
				idSelect++;
			}
			GameCanvas.ClearkeyMove(2);
			flag = true;
		}
		else if (GameCanvas.keyMove(1))
		{
			if (idSelect >= 2)
			{
				idSelect -= 2;
			}
			else
			{
				idSelect = 14 + idSelect % 2;
			}
			GameCanvas.ClearkeyMove(1);
			flag = true;
		}
		else if (GameCanvas.keyMove(3))
		{
			if (idSelect + 2 < 16)
			{
				idSelect += 2;
			}
			else
			{
				idSelect = idSelect % 2;
			}
			GameCanvas.ClearkeyMove(3);
			flag = true;
		}
		else if (GameCanvas.keyMove(5))
		{
			if (idSelect >= 0 && idSelect < 16)
			{
				itemCur = (MainItem)obj.hashEquip.get(idSelect.ToString() ?? "");
				isShowInfo = (itemCur != null);
				if (itemCur != null)
				{
					int page = idSelect / 8;
					int idx = idSelect % 8;
					int row = idx / 2;
					int col = idx % 2;
					int sx = (col == 0) ? leftSlotX : rightSlotX;
					int sy = viewClipY + page * EQUIP_PAGE_H + 2 + row * slotGapY - equipScrollY;
					setPosInfo(itemCur, sx + wItem / 2, sy + wItem);
				}
			}
			GameCanvas.ClearkeyMove(5);
		}

		if (flag)
		{
			if (idSelect < 8)
			{
				equipToY = 0;
				cmdSwitchPage.caption = "Thần trang";
			}
			else
			{
				equipToY = EQUIP_PAGE_H;
				cmdSwitchPage.caption = "Trang bị";
			}

			if (idSelect >= 0 && idSelect < 16)
			{
				isShowInfo = false;
				timeShowInfo = 0;
				itemCur = (MainItem)obj.hashEquip.get(idSelect.ToString() ?? "");
			}
			else
			{
				idSelect = 0;
				isShowInfo = false;
				timeShowInfo = 0;
				itemCur = (MainItem)obj.hashEquip.get("0");
			}
		}
	}

	public override void updatePointer()
	{
		if (cmdList != null)
		{
			for (int i = 0; i < cmdList.size(); i++)
			{
				((iCommand)cmdList.elementAt(i)).updatePointer();
			}
		}

		int equipAreaX = xDia + 4;
		int equipAreaW = wDia - 8;

		// Xử lý vuốt cuộn lên/xuống (Touch Drag Scroll)
		if (GameCanvas.isPointerDown)
		{
			if (!isDraggingEquip && GameCanvas.isPoint(equipAreaX, viewClipY, equipAreaW, viewClipH))
			{
				isDraggingEquip = true;
				equipDragStartY = GameCanvas.py;
				equipDragStartScrollY = equipScrollY;
			}
			else if (isDraggingEquip)
			{
				int dy = GameCanvas.py - equipDragStartY;
				equipScrollY = equipDragStartScrollY - dy;
				if (equipScrollY < -20) equipScrollY = -20 + (equipScrollY + 20) / 3;
				if (equipScrollY > EQUIP_PAGE_H + 20) equipScrollY = EQUIP_PAGE_H + 20 + (equipScrollY - EQUIP_PAGE_H - 20) / 3;
			}
		}
		else
		{
			if (isDraggingEquip)
			{
				isDraggingEquip = false;
				int dragDist = GameCanvas.py - equipDragStartY;
				if (dragDist < -16)
				{
					equipToY = EQUIP_PAGE_H;
					cmdSwitchPage.caption = "Trang bị";
				}
				else if (dragDist > 16)
				{
					equipToY = 0;
					cmdSwitchPage.caption = "Thần trang";
				}
				else
				{
					equipToY = (equipScrollY > EQUIP_PAGE_H / 2) ? EQUIP_PAGE_H : 0;
					cmdSwitchPage.caption = (equipToY == 0) ? "Thần trang" : "Trang bị";
				}
			}
		}

		if (GameCanvas.isPointerSelect)
		{
			int btn0_x = xDia + 8;
			int btn1_x = xDia + 12 + tabBtnW;

			// Chạm Tab 0: Trang bị
			if (GameCanvas.isPointSelect(btn0_x, tabBtnY - 2, tabBtnW, tabBtnH + 4))
			{
				equipToY = 0;
				cmdSwitchPage.caption = "Thần trang";
				idSelect = -1;
				itemCur = null;
				isShowInfo = false;
				GameCanvas.isPointerSelect = false;
				return;
			}

			// Chạm Tab 1: Thần trang
			if (GameCanvas.isPointSelect(btn1_x, tabBtnY - 2, tabBtnW, tabBtnH + 4))
			{
				equipToY = EQUIP_PAGE_H;
				cmdSwitchPage.caption = "Trang bị";
				idSelect = -1;
				itemCur = null;
				isShowInfo = false;
				GameCanvas.isPointerSelect = false;
				return;
			}

			// Chạm vào các ô trang bị (16 ô)
			bool flag = true;
			for (int s = 0; s < 16; s++)
			{
				int page = s / 8;
				int idx = s % 8;
				int row = idx / 2;
				int col = idx % 2;
				int sx = (col == 0) ? leftSlotX : rightSlotX;
				int sy = viewClipY + page * EQUIP_PAGE_H + 2 + row * slotGapY - equipScrollY;

				if (sy + wItem >= viewClipY && sy <= viewClipY + viewClipH && GameCanvas.isPointSelect(sx - 2, sy - 2, wItem + 4, wItem + 4))
				{
					flag = false;
					if (s != idSelect)
					{
						idSelect = s;
						itemCur = (MainItem)obj.hashEquip.get(s.ToString() ?? "");
						isShowInfo = (itemCur != null);
						timeShowInfo = 10;
						if (itemCur != null)
						{
							setPosInfo(itemCur, sx + wItem / 2, sy + wItem);
						}
					}
					GameCanvas.isPointerSelect = false;
					break;
				}
			}

			if (flag && !GameCanvas.isPoint(equipAreaX, viewClipY, equipAreaW, viewClipH))
			{
				itemCur = null;
				isShowInfo = false;
				timeShowInfo = 0;
				idSelect = -1;
			}
		}
	}

	public void setPosInfo(MainItem item, int xbe, int ybe)
	{
		int num = 100;
		int num2 = 40;
		if (item != null)
		{
			num = item.wInfo;
			num2 = item.hInfo;
		}
		xInfo = xbe - num / 2;
		if (xInfo + num > MotherCanvas.w - 4)
		{
			xInfo = MotherCanvas.w - num - 4;
		}
		if (xInfo < 4)
		{
			xInfo = 4;
		}
		yInfo = ybe + 2;
		if (yInfo + num2 > MotherCanvas.h - GameCanvas.hCommand - 4)
		{
			yInfo = ybe - num2 - wItem - 4;
		}
		if (yInfo < 4)
		{
			yInfo = 4;
		}
		if (item != null)
		{
			int maxH = MotherCanvas.h - GameCanvas.hCommand - 8 - yInfo;
			if (item.hInfo > maxH && maxH > 0)
			{
				item.hRunInfo = item.hInfo - maxH;
			}
		}
	}
}
