public class TabAutoSellBag : MainTabShop
{
	public TabAutoSellBag(string name, mVector items, int xTab)
		: base(name, items, Player.maxInventory, xTab)
	{
		indexIconTab = 0;
	}

	public void initCmd()
	{
		cmdMenu = new iCommand(T.menu, 10, this);
		setPosCmd(getMenuActionItem());
	}

	public override mVector getMenuActionItem()
	{
		mVector menu = new mVector();
		if (IdSelect >= 0 && IdSelect < vecShop.size())
		{
			itemCur = (MainItem)vecShop.elementAt(IdSelect);
		}
		else
		{
			itemCur = null;
		}

		if (itemCur != null)
		{
			vecInfoSS = MainItem.getInfoSS(itemCur);
			isShowInfo = true;
			setPosInfo();
			if (AThMadaraMOD.isInAutoSell(itemCur))
			{
				menu.addElement(new iCommand("Xóa", 1, this));
			}
			else
			{
				menu.addElement(new iCommand("Thêm", 2, this));
			}
		}
		else
		{
			isShowInfo = false;
		}
		menu.addElement(new iCommand(!Player.isAutoFilterItems ? "Bật Tự Động" : "Tắt Tự Động", 3, this));
		return menu;
	}

	public override void commandPointer(int index, int subIndex)
	{
		switch (index)
		{
		case 1:
		case 2:
			if (itemCur != null)
			{
				AThMadaraMOD.toggleAutoSell(itemCur);
				setPosCmd(getMenuActionItem());
			}
			break;
		case 3:
			Player.isAutoFilterItems = !Player.isAutoFilterItems;
			Interface_Game.addInfoPlayerNormal("Đã " + (Player.isAutoFilterItems ? "Bật" : "Tắt") + " tự động dọn rác", mFont.tahoma_7_yellow);
			setPosCmd(getMenuActionItem());
			break;
		case 10:
			mVector menuActionItem = getMenuActionItem();
			if (menuActionItem != null)
			{
				GameCanvas.menu.startAt(menuActionItem, 2, T.menu);
			}
			break;
		}
	}

	public override void paintItemCur(mGraphics g, MainItem Item, int x, int y)
	{
		if (Item != null && AThMadaraMOD.isInAutoSell(Item))
		{
			int px = x - MainTab.wItem + 1;
			int py = y - MainTab.wItem + 1;
			g.setColor(13959168);
			g.drawRect(px + 1, py + 1, MainTab.wItem - 2, MainTab.wItem - 2);
			g.drawRect(px + 2, py + 2, MainTab.wItem - 4, MainTab.wItem - 4);
		}
	}
}
