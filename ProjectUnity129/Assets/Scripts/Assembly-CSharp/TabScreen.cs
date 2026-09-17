public class TabScreen : MainScreen
{
	public static bool isRefresh = false;

	public const sbyte TAB = 0;

	public const sbyte INFO = 1;

	public mVector vecTabs = new mVector("TabScreen.vecTabs");

	public static mVector vecEff = new mVector("TabScreen.vecEff");

	public ListNew listTab;

	public MainTab tabCurrent;

	public iCommand cmdClose;

	public sbyte typeCurrent;

	public sbyte typeMarket;

	public int idSelect;

	public bool isShopClan;

	public bool isFixscreen;

	public int xbeginPaintTab;

	private int[] mIndexMarket = new int[6] { 0, 1, 2, 5, 3, 4 };

	public TabScreen(int xbegin, sbyte type)
	{
		xbeginPaintTab = xbegin;
		typeMarket = type;
		cmdClose = new iCommand(T.close, 0, this);
		int x = xbeginPaintTab + 22 + (MainTab.wTab - 22) / 2 - MainTab.wTab / 4 * 3 / 2 + MainTab.wTab / 4 * 3;
		if (GameCanvas.isTouch)
		{
			cmdClose.setPos(x, MainTab.yTab + 7 + 8, MainTab.fraCloseTab, "");
		}
		right = cmdClose;
		backCMD = cmdClose;
	}

	public void initListTab()
	{
		xbeginPaintTab = MainTab.xTab;
		int x = xbeginPaintTab + 22 + (MainTab.wTab - 22) / 2 - MainTab.wTab / 4 * 3 / 2 + MainTab.wTab / 4 * 3;
		if (GameCanvas.isTouch && cmdClose != null)
		{
			cmdClose.setPos(x, MainTab.yTab + 7 + 8, MainTab.fraCloseTab, "");
		}
		if (vecTabs != null && vecTabs.size() > 0)
		{
			int hListTab = MainTab.hTab - 36;
			int limY = vecTabs.size() * MainTab.hItemTab - hListTab;
			int xList = xbeginPaintTab + 22 - MainTab.hItemTab / 2;
			int yList = MainTab.yTab + 26;
			listTab = new ListNew(xList, yList, MainTab.hItemTab, hListTab, 0, 0, limY, isLim0: true);
		}
	}

	public override void Show(MainScreen screen)
	{
		initListTab();
		if (!GameCanvas.isTouch)
		{
			typeCurrent = 0;
			setTabSelect();
			endInfoTab();
		}
		else
		{
			typeCurrent = 1;
			setTabSelect();
			tabCurrent.beginFocus();
		}
		base.Show(screen);
	}

	public override void setxyPlus12()
	{
		GameCanvas.xPlus12 = 2;
		GameCanvas.yPlus12 = 2;
	}

	public override void commandPointer(int index, int subIndex)
	{
		if (index != 0)
		{
			return;
		}
		if (GameCanvas.isTouch)
		{
			lastScreen.Show(lastScreen.lastScreen);
			if (GameScreen.indexHelp == 16)
			{
				GameScreen.addHelp(16, 1);
			}
		}
		else if (typeCurrent == 0)
		{
			lastScreen.Show(lastScreen.lastScreen);
		}
		else if (typeCurrent == 1)
		{
			typeCurrent = 0;
		}
	}

	public void addVecTab(mVector vec)
	{
		vecTabs = vec;
		if (tabCurrent == null && vecTabs.size() > 0)
		{
			tabCurrent = (MainTab)vecTabs.elementAt(0);
			idSelect = 0;
		}
		initListTab();
	}

	public override void paint(mGraphics g)
	{
		if (lastScreen != null)
		{
			lastScreen.paint(g);
		}
		GameCanvas.resetTrans(g);
		MainTab.paintBgTab(g, xbeginPaintTab, isShopClan, typeMarket);
		int cmxTab = (listTab != null && listTab.cmxLim > 0) ? listTab.cmx : 0;
		int cmxLimTab = (listTab != null) ? listTab.cmxLim : 0;
		MainTab.paintListTab(g, xbeginPaintTab, vecTabs, idSelect, cmxTab, cmxLimTab);
		Interface_Game.paintNumMess(g, -Interface_Game.xNumMess + 8, 0);
		base.paint(g);
		if (tabCurrent != null)
		{
			tabCurrent.paint(g);
			if (tabCurrent.indexIconTab == 2 && GameCanvas.isTouch)
			{
				cmdClose.paint(g, cmdClose.xCmd, cmdClose.yCmd);
			}
		}
		for (int i = 0; i < vecEff.size(); i++)
		{
			MainEffect mainEffect = (MainEffect)vecEff.elementAt(i);
			if (mainEffect != null && !mainEffect.isRemove && !mainEffect.isStop)
			{
				mainEffect.paint(g);
			}
		}
	}

	public override void update()
	{
		MainTab.timenhapnhay++;
		if (lastScreen != null)
		{
			lastScreen.update();
		}
		if (listTab != null && listTab.cmxLim > 0)
		{
			listTab.moveCamera();
		}
		if (!GameCanvas.isTouch)
		{
			if (typeCurrent == 0)
			{
				if (cmdClose.caption != T.close)
				{
					cmdClose.caption = T.close;
				}
			}
			else if (typeCurrent == 1 && cmdClose.caption != T.back)
			{
				cmdClose.caption = T.back;
			}
		}
		if (tabCurrent != null)
		{
			tabCurrent.update();
			if (isRefresh)
			{
				isRefresh = false;
				tabCurrent.updateInfo();
			}
		}
		for (int i = 0; i < vecEff.size(); i++)
		{
			MainEffect mainEffect = (MainEffect)vecEff.elementAt(i);
			if (mainEffect != null && !mainEffect.isRemove && !mainEffect.isStop)
			{
				mainEffect.update();
			}
		}
	}

	public override void updatekey()
	{
		if (typeCurrent == 0)
		{
			int num = idSelect;
			if (GameCanvas.keyMove(1))
			{
				idSelect--;
				GameCanvas.ClearkeyMove(1);
			}
			else if (GameCanvas.keyMove(3))
			{
				idSelect++;
				GameCanvas.ClearkeyMove(3);
			}
			if (GameCanvas.keyMove(0) || GameCanvas.keyMove(2))
			{
				typeCurrent = 1;
				tabCurrent.beginFocus();
				GameCanvas.ClearkeyMove(0);
				GameCanvas.ClearkeyMove(2);
			}
			if (num != idSelect)
			{
				MainTab.timenhapnhay = 0;
				idSelect = AvMain.resetSelect(idSelect, vecTabs.size() - 1, isreset: true);
				if (listTab != null && listTab.cmxLim > 0)
				{
					listTab.cmtoX = idSelect * MainTab.hItemTab - (MainTab.hTab - 36) / 2 + MainTab.hItemTab / 2;
					if (listTab.cmtoX > listTab.cmxLim)
					{
						listTab.cmtoX = listTab.cmxLim;
					}
					if (listTab.cmtoX < 0)
					{
						listTab.cmtoX = 0;
					}
				}
				setTabSelect();
				endInfoTab();
				tabCurrent.beginFocus();
			}
		}
		else
		{
			if (tabCurrent != null)
			{
				tabCurrent.updatekey();
			}
			if (typeCurrent == 0)
			{
				endInfoTab();
			}
		}
		base.updatekey();
		updatekeyPC();
	}

	public override void updatePointer()
	{
		if (listTab != null && listTab.cmxLim > 0)
		{
			listTab.update_Pos_UP_DOWN();
		}
		int x = xbeginPaintTab + 22 - MainTab.hItemTab / 2;
		int num = MainTab.yTab + 36 - MainTab.hItemTab / 2;
		if (listTab != null && listTab.cmxLim > 0)
		{
			num = MainTab.yTab + 26;
		}
		else if (num + vecTabs.size() * MainTab.hItemTab > MainTab.yTab + MainTab.hTab)
		{
			num = MainTab.yTab + MainTab.hTab / 2 - vecTabs.size() * MainTab.hItemTab / 2;
		}
		int cmx = (listTab != null && listTab.cmxLim > 0) ? listTab.cmx : 0;
		if (GameCanvas.isPointSelect(x, num - cmx, MainTab.hItemTab, MainTab.hItemTab * vecTabs.size()))
		{
			int num2 = (GameCanvas.py - num + cmx) / MainTab.hItemTab;
			int num3 = vecTabs.size();
			if (num2 >= 0 && num2 < num3)
			{
				GameCanvas.isPointerSelect = false;
				if (num2 != idSelect)
				{
					idSelect = num2;
					setTabSelect();
					tabCurrent.beginFocus();
				}
				if (!GameCanvas.currentScreen.setCurTypetab(1))
				{
					GameCanvas.currentScreen.setTypeTab(1);
				}
			}
		}
		if (tabCurrent != null)
		{
			tabCurrent.updatePointer();
		}
		base.updatePointer();
	}

	public void setTabSelect()
	{
		tabCurrent = (MainTab)vecTabs.elementAt(idSelect);
		MainTab.xRun = -1;
		MainTab.timeRun = 0;
	}

	public void endInfoTab()
	{
		left = null;
		center = null;
		right = cmdClose;
		MainTab.xRun = -1;
		MainTab.timeRun = 0;
		if (tabCurrent != null)
		{
			left = tabCurrent.setCmdEndInfo();
		}
	}

	public void updateInfo()
	{
		if (tabCurrent != null)
		{
			tabCurrent.updateInfo();
		}
	}

	public static void addEffectNumImage(string content, int x, int y, sbyte typeColor, FrameImage fra, int frame)
	{
		EffectNum effectNum = new EffectNum(content, x, y, typeColor, fra, frame);
		int num = GameScreen.find_Index_Stop(vecEff);
		if (num == vecEff.size())
		{
			vecEff.addElement(effectNum);
		}
		else
		{
			vecEff.setElementAt(effectNum, num);
		}
	}

	public override bool keyBack()
	{
		if (!base.keyBack() && cmdClose != null)
		{
			cmdClose.perform();
		}
		return false;
	}

	public void updateVecDataMarket(sbyte index, mVector vec)
	{
		if (index < 0 || index >= vecTabs.size())
		{
			return;
		}
		for (int i = 0; i < vecTabs.size(); i++)
		{
			MainTab mainTab = (MainTab)vecTabs.elementAt(i);
			if (mainTab.indexMarket == index)
			{
				mainTab.setData(vec);
				break;
			}
		}
	}
}
