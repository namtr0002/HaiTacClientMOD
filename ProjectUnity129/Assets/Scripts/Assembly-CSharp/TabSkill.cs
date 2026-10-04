using System;

public class TabSkill : MainTab
{
	private Skill_Info skillCur;

	private mVector vecCmd = new mVector();

	public static iCommand cmdSetPoint;

	public static iCommand cmdSetHotKey;

	private InputDialog input;

	private ListNew list;

	private MarqueeText marName = new MarqueeText(0);

	private Scroll scrSkill = new Scroll();

	public static int numVecListSkillPaint = 0;

	public static int indexDevil;

	public static int indexBuff = 0;

	public static int indexPassive = 0;

	public static int indexHaki = 0;

	public static int indexJob = 0;

	public static int indexDevilSkill = 0;

	public static int indexBuffSkill = 0;

	public static int indexPassiveSkill = 0;

	public static int indexHakiSkill = 0;

	public static int indexJobSkill = 0;

	public static int numActiveSkill = 0;

	public static int numDevilSkill = 0;

	public static int numBuffSkill = 0;

	public static int numPassiveSkill = 0;

	public static TabSkill me;

	public static int numHakiSkill = 0;

	public static int numJobSkill = 0;

	public static mVector vecListSkillPaint = new mVector();

	public TabSkill(string name)
	{
		me = this;
		nameTab = name;
		initCmd();
		list = new ListNew();
		indexIconTab = 3;
		wItemCur = 32;
		if (isBigScreen)
		{
			wItemCur = 36;
		}
		marName = new MarqueeText(wCur - (wItemCur + miniItem * 2 + 3));
	}

	public static void reload()
	{
		if (me != null)
		{
			me.reloadTabSkill();
		}
	}

	public void reloadTabSkill()
	{
		int prevSelect = IdSelect;
		short prevSkillId = (skillCur != null) ? skillCur.ID : (short)-1;

		indexDevil = 0;
		indexBuff = 0;
		indexPassive = 0;
		indexHaki = 0;
		indexJob = 0;
		indexDevilSkill = 0;
		indexBuffSkill = 0;
		indexPassiveSkill = 0;
		indexHakiSkill = 0;
		indexJobSkill = 0;
		numActiveSkill = 0;
		numDevilSkill = 0;
		numBuffSkill = 0;
		numPassiveSkill = 0;
		numHakiSkill = 0;
		numJobSkill = 0;
		mVector mVector2 = new mVector();
		mVector mVector3 = new mVector();
		mVector mVector4 = new mVector();
		mVector mVector5 = new mVector();
		mVector mVector6 = new mVector();
		mVector mVector7 = new mVector();
		vecListSkillPaint.removeAllElements();
		mVector varActive = new mVector();
		mVector varSea = new mVector();
		for (int i = 0; i < Player.vecListSkill.size(); i++)
		{
			Skill_Info skill_Info = (Skill_Info)Player.vecListSkill.elementAt(i);
			if (skill_Info.Lv_RQ != -1)
			{
				if (skill_Info.typeDevil == 0 && (skill_Info.typeSkill == 1 || skill_Info.typeSkill == 4))
				{
					if (skill_Info.typeSkill == 4)
					{
						varSea.addElement(skill_Info);
					}
					else
					{
						varActive.addElement(skill_Info);
					}
				}
				else if (skill_Info.typeDevil == 1)
				{
					mVector3.addElement(skill_Info);
					numDevilSkill++;
				}
				else if (skill_Info.typeDevil == 2)
				{
					mVector6.addElement(skill_Info);
					numHakiSkill++;
				}
				else if (skill_Info.typeSkill == 6)
				{
					mVector7.addElement(skill_Info);
					numJobSkill++;
				}
				else if (skill_Info.typeSkill == 2)
				{
					mVector4.addElement(skill_Info);
					numBuffSkill++;
				}
				else if (skill_Info.typeSkill == 3)
				{
					mVector5.addElement(skill_Info);
					numPassiveSkill++;
				}
			}
		}
		int nFirstActive = (varActive.size() < 3) ? varActive.size() : 3;
		for (int k = 0; k < nFirstActive; k++)
		{
			mVector2.addElement(varActive.elementAt(k));
		}
		for (int k = 0; k < varSea.size(); k++)
		{
			mVector2.addElement(varSea.elementAt(k));
		}
		for (int k = nFirstActive; k < varActive.size(); k++)
		{
			mVector2.addElement(varActive.elementAt(k));
		}
		numActiveSkill = mVector2.size();
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		if (numActiveSkill > 0)
		{
			for (int j = 0; j < numActiveSkill; j++)
			{
				Skill_Info o = (Skill_Info)mVector2.elementAt(j);
				vecListSkillPaint.addElement(o);
			}
			num2 = numActiveSkill;
			num = numActiveSkill + 1;
			num3++;
		}
		if (numDevilSkill > 0)
		{
			for (int k = 0; k < numDevilSkill; k++)
			{
				Skill_Info o2 = (Skill_Info)mVector3.elementAt(k);
				vecListSkillPaint.addElement(o2);
			}
			indexDevilSkill = num2;
			num2 = indexDevilSkill + numDevilSkill;
			indexDevil = num;
			num = indexDevil + numDevilSkill + 1;
			num3++;
		}
		if (numBuffSkill > 0)
		{
			for (int l = 0; l < numBuffSkill; l++)
			{
				Skill_Info o3 = (Skill_Info)mVector4.elementAt(l);
				vecListSkillPaint.addElement(o3);
			}
			indexBuffSkill = num2;
			num2 = indexBuffSkill + numBuffSkill;
			indexBuff = num;
			num = indexBuff + numBuffSkill + 1;
			num3++;
		}
		if (numPassiveSkill > 0)
		{
			for (int m = 0; m < numPassiveSkill; m++)
			{
				Skill_Info o4 = (Skill_Info)mVector5.elementAt(m);
				vecListSkillPaint.addElement(o4);
			}
			indexPassiveSkill = num2;
			num2 = indexPassiveSkill + numPassiveSkill;
			indexPassive = num;
			num = indexPassive + numPassiveSkill + 1;
			num3++;
		}
		if (numHakiSkill > 0)
		{
			for (int n = 0; n < numHakiSkill; n++)
			{
				Skill_Info o5 = (Skill_Info)mVector6.elementAt(n);
				vecListSkillPaint.addElement(o5);
			}
			indexHakiSkill = num2;
			num2 = indexHakiSkill + numHakiSkill;
			indexHaki = num;
			num = indexHaki + numHakiSkill + 1;
			num3++;
		}
		if (numJobSkill > 0)
		{
			for (int num4 = 0; num4 < numJobSkill; num4++)
			{
				Skill_Info o6 = (Skill_Info)mVector7.elementAt(num4);
				vecListSkillPaint.addElement(o6);
			}
			indexJobSkill = num2;
			indexJob = num;
			num3++;
		}
		numVecListSkillPaint = vecListSkillPaint.size();
		int limX = (numVecListSkillPaint + num3) * wItemCur - hCur + miniItem * 3;
		if (limX < 0) limX = 0;
		int curScroll = (list != null) ? list.cmx : 0;
		if (curScroll < 0) curScroll = 0;
		if (curScroll > limX) curScroll = limX;
		list = new ListNew(xCurBegin, yCurBegin, wCur, hCur, 0, 0, limX, isLim0: true);
		list.cmx = curScroll;
		list.cmtoX = curScroll;
		scrSkill.setInfo(xCurBegin + wCur + miniItem, yCurBegin + miniItem / 2, hCur - miniItem * 2, 8809550);

		int targetSelect = -1;
		if (prevSkillId >= 0)
		{
			for (int i = 0; i < vecListSkillPaint.size(); i++)
			{
				Skill_Info sk = (Skill_Info)vecListSkillPaint.elementAt(i);
				if (sk.ID == prevSkillId)
				{
					targetSelect = i;
					break;
				}
			}
		}
		if (targetSelect == -1 && prevSelect >= 0 && prevSelect < numVecListSkillPaint)
		{
			targetSelect = prevSelect;
		}

		if (targetSelect >= 0 && targetSelect < numVecListSkillPaint)
		{
			IdSelect = targetSelect;
			getSkillCur();
		}
		else
		{
			if (GameCanvas.isTouchNoOrPC())
			{
				IdSelect = (numVecListSkillPaint > 0) ? 0 : -1;
				getSkillCur();
			}
			else
			{
				IdSelect = -1;
				skillCur = null;
				setPosCmd(null);
			}
		}
	}

	public override void beginFocus()
	{
		IdSelect = -1;
		reloadTabSkill();
		MainTab.xRun = -1;
		MainTab.timeRun = 0;
		if (GameCanvas.isTouchNoOrPC())
		{
			IdSelect = (numVecListSkillPaint > 0) ? 0 : -1;
			getSkillCur();
		}
		else
		{
			IdSelect = -1;
			skillCur = null;
			setPosCmd(null);
		}
	}

	private void initCmd()
	{
		cmdSetPoint = new iCommand(T.congDiem, 0, this);
		cmdSetHotKey = new iCommand(T.cmdHotKey, 3, this);
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
		case 0:
			if (skillCur != null)
			{
				if (Player.mLvSkill[skillCur.indexHotKey] >= Skill_Info.maxLv)
				{
					GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.maxLvSkill);
					vecCmd = skillCur.getActionInven();
				}
				else if (Player.pointSkill > 1)
				{
					input = GameCanvas.Start_Input_Dialog(T.nhappoint, new iCommand(T.cmdSetPoint, 1, 0, this), isNum: true, T.congDiem);
					GameCanvas.subDialog = input;
				}
				else if (Player.pointSkill == 1)
				{
					commandPointer(2, 0);
				}
			}
			break;
		case 1:
			if (skillCur == null)
			{
				break;
			}
			if (subIndex == 0)
			{
				int num = 1;
				try
				{
					num = int.Parse(input.tfInput.getText());
					if (num < 0)
					{
						num = 1;
					}
				}
				catch (Exception)
				{
					num = 1;
				}
				GlobalService.gI().Add_Point_Skill(skillCur.indexHotKey, (short)num);
				GameCanvas.end_Dialog();
			}
			else
			{
				GlobalService.gI().Add_Point_Skill(skillCur.indexHotKey, 1);
				GameCanvas.end_Dialog();
			}
			break;
		case 2:
			GameCanvas.Start_Normal_DiaLog(T.add1Point, new iCommand(T.congDiem, 1, 1, this), isCmdClose: true);
			break;
		case 3:
		{
			if (skillCur == null || skillCur.Lv_RQ <= 0)
			{
				break;
			}
			bool isSea = (LoadMap.specMap == 4 || (GameScreen.player != null && GameScreen.player.boatSea != null));
			if (skillCur != null && ((isSea && skillCur.typeSkill != 4 && skillCur.typeSkill != 2) || (!isSea && skillCur.typeSkill == 4)))
			{
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(isSea ? "Kỹ năng này không thể sử dụng trên biển" : T.khongdungduocmapnay);
				break;
			}
			mVector mVector2 = new mVector();
			for (int i = 0; i < 6; i++)
			{
				if (i != 2 && (GameCanvas.isTouch || i != 5))
				{
					iCommand iCommand2 = null;
					iCommand2 = (GameCanvas.isTouch ? new iCommand(T.keys + " " + (i + 1), 4, i, this) : ((!TField.isQwerty) ? new iCommand(T.keys + " " + (i * 2 + 1), 4, i, this) : new iCommand(T.keys + " " + T.mKeyQty[i], 4, i, this)));
					mVector2.addElement(iCommand2);
				}
			}
			GameCanvas.menu.startAt(mVector2, 2, T.cmdHotKey);
			break;
		}
		case 4:
			if (skillCur != null)
			{
				MainSkill mainSkill = new MainSkill(skillCur.ID, skillCur.typeEffSkill);
				mainSkill.indexHotKey = skillCur.indexHotKey;
				mainSkill.idIcon = skillCur.idIcon;
				mainSkill.isBuff = skillCur.typeSkill == 2;
				mainSkill.lvDevil = skillCur.LvDevilSkill;
				mainSkill.typeDevil = skillCur.typeDevil;
				Player.setHotKey(subIndex, mainSkill, null);
				Interface_Game.timePaintIconSkill = 100;
			}
			break;
		}
	}

	public override void paint(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		AvMain.paintRect(g, xCurBegin, yCurBegin, wCur, hCur - miniItem, 0, 4);
		g.saveCanvas();
		g.ClipRec(xCurBegin + 2, yCurBegin + 1, wCur - 4, hCur - 1 - miniItem - 1);
		setClip(g);
		int num = miniItem;
		int num2 = 0;
		AvMain.Font3dWhite(g, T.skillActive, num, num2 + wItemCur / 2 - 2, 0);
		num2 += wItemCur;
		if (IdSelect >= 0 && IdSelect < numVecListSkillPaint && GameCanvas.currentScreen.setCurTypetab(1))
		{
			paintSelect(g);
		}
		int num3 = 0;
		int num4 = 0;
		num3 = list.cmx / wItemCur;
		num4 = hCur / wItemCur + 1 + num3;
		num3 = changeValue(num3);
		num4 = changeValue(num4);
		for (int i = 0; i < numVecListSkillPaint; i++)
		{
			if (numDevilSkill > 0 && i == indexDevilSkill)
			{
				AvMain.Font3dWhite(g, T.skillDevil, num, num2 + wItemCur / 2 - 6, 0);
				num2 += wItemCur;
			}
			if (numBuffSkill > 0 && i == indexBuffSkill)
			{
				AvMain.Font3dWhite(g, T.skillBuff, num, num2 + wItemCur / 2 - 6, 0);
				num2 += wItemCur;
			}
			if (numPassiveSkill > 0 && i == indexPassiveSkill)
			{
				AvMain.Font3dWhite(g, T.skillPassive + ": " + numPassiveSkill + "/" + Player.numPassive, num, num2 + wItemCur / 2 - 6, 0);
				num2 += wItemCur;
			}
			if (numHakiSkill > 0 && i == indexHakiSkill)
			{
				AvMain.Font3dWhite(g, T.skillHaki, num, num2 + wItemCur / 2 - 6, 0);
				num2 += wItemCur;
			}
			if (numJobSkill > 0 && i == indexJobSkill)
			{
				AvMain.Font3dWhite(g, T.skillJob, num, num2 + wItemCur / 2 - 6, 0);
				num2 += wItemCur;
			}
			if (i >= num3 && i <= num4)
			{
				Skill_Info skill_Info = (Skill_Info)vecListSkillPaint.elementAt(i);
				string st = skill_Info.name;
				skill_Info.paint(g, num + wItemCur / 2, num2 + wItemCur / 2);
				if (skill_Info.Lv_RQ == Skill_Info.maxLv)
				{
					st = skill_Info.name + " " + T.max;
				}
				if (skill_Info.Lv_RQ < 0)
				{
					mFont.tahoma_7b_blue.drawString(g, st, num + wItemCur + miniItem, num2 + miniItem / 2 + 1, 0);
				}
				else if (IdSelect == i && GameCanvas.currentScreen.setCurTypetab(1) && marName.isRun)
				{
					g.setClip(num + wItemCur + miniItem, num2, marName.maxW - 1, wItemCur);
					mFont.tahoma_7b_white.drawString(g, st, num + wItemCur + miniItem - marName.xplus, num2 + miniItem / 2 + 1, 0);
					setClip(g);
				}
				else
				{
					mFont.tahoma_7b_white.drawString(g, st, num + wItemCur + miniItem, num2 + miniItem / 2 + 1, 0);
				}
				if (skill_Info.typeDevil == 1)
				{
					string text = "";
					text = ((skill_Info.typeSkill == 1) ? T.devilFruitA : ((skill_Info.typeSkill != 3) ? T.devilFruitB : T.devilFruitP));
					mFont.tahoma_7_green.drawString(g, text, num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
				}
				else if (skill_Info.typeDevil == 2)
				{
					string text2 = "";
					text2 = ((skill_Info.typeSkill == 1) ? T.devilHakiA : ((skill_Info.typeSkill != 3) ? T.devilHakiB : T.devilHakiP));
					mFont.tahoma_7_green.drawString(g, text2, num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
				}
				else if (skill_Info.typeSkill == 3 || skill_Info.typeSkill == 2)
				{
					mFont.tahoma_7_green.drawString(g, T.Lv + ": " + skill_Info.Lv_RQ + " " + skill_Info.getStrType(), num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
				}
				else if (skill_Info.Lv_RQ >= Skill_Info.maxLv)
				{
					mFont.tahoma_7_green.drawString(g, T.maxLv, num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
				}
				else if (GameCanvas.isSmallScreen)
				{
					mFont.tahoma_7_green.drawString(g, T.Lv + ": " + skill_Info.Lv_RQ + "+" + MainItem.strGetPercent(skill_Info.percentLv, 1), num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
				}
				else
				{
					string lvStr = T.Lv + ": " + skill_Info.Lv_RQ;
					mFont.tahoma_7_green.drawString(g, lvStr, num + wItemCur + miniItem, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
					int lvW = mFont.tahoma_7_green.getWidth(lvStr);
					int barX = num + wItemCur + miniItem + lvW + 4;
					int barW = wCur - (wItemCur + miniItem + lvW + 4) - miniItem;
					if (barW >= 36)
					{
						Interface_Game.PaintHPMP(g, 3, skill_Info.percentLv, 100, barX, num2 + miniItem / 2 + GameCanvas.hText + 1, 0, miniItem * 2, barW, 1, isflip: false, 0, isUpdateEff: false, 0);
					}
					else
					{
						mFont.tahoma_7_green.drawString(g, "+" + MainItem.strGetPercent(skill_Info.percentLv, 1), barX, num2 + miniItem / 2 + GameCanvas.hText + 1, 0);
					}
				}
				if (skill_Info.phanTramDevilSkill > 0)
				{
					int num5 = skill_Info.phanTramDevilSkill / 5;
					num5 = ((num5 < 20) ? (num5 + 1) : ((num5 != 20) ? 22 : (num5 + 2)));
					g.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - num5, 22, num5, 0, num + wItemCur / 2, num2 + wItemCur / 2 + 11, 33);
				}
			}
			num2 += wItemCur;
		}
		mGraphics.resetTransAndroid(g);
		g.restoreCanvas();
		GameCanvas.resetTrans(g);
		if (!GameCanvas.currentScreen.setCurTypetab(1))
		{
			return;
		}
		if (list.cmxLim > 0)
		{
			scrSkill.paint(g);
		}
		if (isShowInfo && skillCur != null)
		{
			bool isLv = false;
			if (skillCur.typeSkill == 1 || skillCur.typeSkill == 4)
			{
				isLv = true;
			}
			ShowInfo(g, skillCur, null, 0, xInfo, yInfo, isLv, null, 0);
		}
		if (vecCmd != null && GameCanvas.getShowCmd())
		{
			for (int j = 0; j < vecCmd.size(); j++)
			{
				iCommand iCommand2 = (iCommand)vecCmd.elementAt(j);
				iCommand2.paint(g, iCommand2.xCmd, iCommand2.yCmd);
			}
		}
		base.paint(g);
	}

	public void setClip(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		g.setClip(xCurBegin + 2, yCurBegin + 1, wCur - 4, hCur - 1 - miniItem - 1);
		g.translate(xCurBegin, yCurBegin);
		g.translate(0, -list.cmx);
	}

	public void paintSelect(mGraphics g)
	{
		int num = wItemCur;
		if (numDevilSkill > 0 && IdSelect >= indexDevilSkill)
		{
			num += wItemCur;
		}
		if (numBuffSkill > 0 && IdSelect >= indexBuffSkill)
		{
			num += wItemCur;
		}
		if (numPassiveSkill > 0 && IdSelect >= indexPassiveSkill)
		{
			num += wItemCur;
		}
		if (numHakiSkill > 0 && IdSelect >= indexHakiSkill)
		{
			num += wItemCur;
		}
		if (numJobSkill > 0 && IdSelect >= indexJobSkill)
		{
			num += wItemCur;
		}
		g.setColor(16446420);
		g.fillRect(miniItem / 2, num + IdSelect * wItemCur, 1, wItemCur);
		g.fillRect(wCur - miniItem / 2 - 1, num + IdSelect * wItemCur, 1, wItemCur);
		g.fillRect(miniItem / 2 + 1, num + IdSelect * wItemCur - 1, wCur - miniItem - 1, 1);
		g.fillRect(miniItem / 2 + 1, num + IdSelect * wItemCur + wItemCur, wCur - miniItem - 1, 1);
	}

	public override void update()
	{
		int cmx = list.cmx;
		list.moveCamera();
		scrSkill.setYScrool(list.cmx, list.cmxLim);
		if (list.cmx != cmx || list.pointerIsDowning)
		{
			isShowInfo = false;
		}
		else if (skillCur != null)
		{
			updateShowInfo();
		}
		marName.update2();
	}

	public override void updatekey()
	{
		bool flag = false;
		if (GameCanvas.keyMove(1))
		{
			IdSelect--;
			GameCanvas.ClearkeyMove(1);
			flag = true;
		}
		else if (GameCanvas.keyMove(3))
		{
			IdSelect++;
			GameCanvas.ClearkeyMove(3);
			flag = true;
		}
		if (GameCanvas.keyMove(0) || GameCanvas.keyMove(2))
		{
			GameCanvas.currentScreen.setTypeTab(0);
			GameCanvas.ClearkeyMove(0);
			GameCanvas.ClearkeyMove(2);
		}
		if (flag)
		{
			IdSelect = AvMain.resetSelect(IdSelect, numVecListSkillPaint - 1, isreset: false);
			if (IdSelect >= 0)
			{
				if (GameCanvas.isTouchNoOrPC())
				{
					int num = (IdSelect + 2) * wItemCur - hCur / 2;
					if (numDevilSkill > 0 && IdSelect >= indexDevilSkill)
					{
						num += wItemCur;
					}
					if (numBuffSkill > 0 && IdSelect >= indexBuffSkill)
					{
						num += wItemCur;
					}
					if (numPassiveSkill > 0 && IdSelect >= indexPassiveSkill)
					{
						num += wItemCur;
					}
					if (numHakiSkill > 0 && IdSelect >= indexHakiSkill)
					{
						num += wItemCur;
					}
					if (numJobSkill > 0 && IdSelect >= indexJobSkill)
					{
						num += wItemCur;
					}
					list.setToX(num);
				}
				getSkillCur();
				isShowInfo = false;
			}
			else
			{
				skillCur = null;
			}
		}
		base.updatekey();
		updatekeyPC();
	}

	public override void updatePointer()
	{
		list.update_Pos_UP_DOWN();
		if (GameCanvas.isPointerSelect && numVecListSkillPaint > 0 && GameCanvas.isPoint(xCurBegin, yCurBegin, wCur, hCur))
		{
			int num = (GameCanvas.py - yCurBegin + list.cmx) / wItemCur;
			if (num != 0 && num != indexDevil && num != indexBuff && num != indexPassive && num != indexHaki && num != indexJob)
			{
				num = changeValue(num);
				if (num != IdSelect && num >= 0 && num < numVecListSkillPaint)
				{
					IdSelect = num;
					getSkillCur();
				}
			}
			GameCanvas.isPointerSelect = false;
		}
		if (vecCmd != null)
		{
			for (int i = 0; i < vecCmd.size(); i++)
			{
				((iCommand)vecCmd.elementAt(i)).updatePointer();
			}
		}
		base.updatePointer();
	}

	public int changeValue(int value)
	{
		int num = value;
		num--;
		if (indexDevil > 0 && value > indexDevil)
		{
			num--;
		}
		if (indexBuff > 0 && value > indexBuff)
		{
			num--;
		}
		if (indexPassive > 0 && value > indexPassive)
		{
			num--;
		}
		if (indexHaki > 0 && value > indexHaki)
		{
			num--;
		}
		if (indexJob > 0 && value > indexJob)
		{
			num--;
		}
		return num;
	}

	public void getSkillCur()
	{
		if (IdSelect < 0 || IdSelect >= numVecListSkillPaint)
		{
			return;
		}
		Skill_Info skill_Info = (Skill_Info)vecListSkillPaint.elementAt(IdSelect);
		if (skill_Info.Lv_RQ == -1)
		{
			skillCur = null;
		}
		else if (skillCur == null || skillCur != skill_Info)
		{
			skillCur = skill_Info;
			if (skillCur != null)
			{
				setPosCmd(skillCur.getActionInven());
			}
			marName.setdata(skill_Info.name, mFont.tahoma_7b_black);
			timeShowInfo = 0;
			isShowInfo = false;
			MainTab.xRun = -1;
			MainTab.timeRun = 0;
		}
	}

	public override void setPosInfo()
	{
		if (skillCur.Lv_RQ != -1)
		{
			if (skillCur != null)
			{
				skillCur.setVecInfo(skillCur.wInfo);
			}
			setPosInfo(skillCur, xCurBegin + MainTab.wTab, yCurBegin + (IdSelect + 1) * wItemCur - list.cmx + 1 + miniItem * 2);
		}
	}

	public void setPosCmd(mVector vec)
	{
		left = null;
		center = null;
		vecCmd.removeAllElements();
		if (vec == null || MainTab.bigInfo > 0)
		{
			return;
		}
		if (GameCanvas.isTouch)
		{
			vecCmd = vec;
			for (int i = 0; i < vecCmd.size(); i++)
			{
				iCommand cmd = (iCommand)vecCmd.elementAt(i);
				cmd = AvMain.setPosCMD(cmd, i);
				if (i == 0)
				{
					okCMD = cmd;
				}
				if (i == 1)
				{
					menuCMD = cmd;
				}
			}
			return;
		}
		for (int j = 0; j < vec.size(); j++)
		{
			iCommand iCommand2 = (iCommand)vec.elementAt(j);
			if (j == 0)
			{
				center = iCommand2;
			}
			if (j == 1)
			{
				left = iCommand2;
			}
		}
	}
}
