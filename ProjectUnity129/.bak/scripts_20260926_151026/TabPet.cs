using System;

public class TabPet : TabInventory
{
	public TabPet(string name, mVector vec, int xbegin)
		: base(name, vec, 7, xbegin)
	{
		indexIconTab = 5;
	}

	public override void beginFocus()
	{
		wCur = MainTab.wItem * MainTabShop.maxNumItemW;
		xCurBegin = MainTab.xTab + MainTab.wTab / 2 - wCur / 2 + 10;
		yCurBegin = MainTab.yTab + 32;
		hCur = MainTab.hTab - 32;
		maxSize = Player.maxInventory;
		if (vecShop != null && vecShop.size() > maxSize)
		{
			maxSize = vecShop.size();
		}
		int limX = ((maxSize - 1) / MainTabShop.maxNumItemW + 1) * MainTab.wItem - hCur + miniItem;
		list = new ListNew(xCurBegin, yCurBegin, wCur, hCur, 0, 0, limX, isLim0: true);
		scrShop.setInfo(xCurBegin + wCur + miniItem, yCurBegin + miniItem / 2, hCur - miniItem * 2, 8809550);

		base.beginFocus();
		if (vecShop == null || vecShop.size() == 0)
		{
			GlobalService.gI().Send_Pet(3);
		}
		setPosCmd(getMenuActionItem());
	}

	public override void setData(mVector vec)
	{
		vecShop = vec;
		Player.vecPet = vec;
		maxSize = Player.maxInventory;
		if (vecShop != null && vecShop.size() > maxSize)
		{
			maxSize = vecShop.size();
		}
		wCur = MainTab.wItem * MainTabShop.maxNumItemW;
		xCurBegin = MainTab.xTab + MainTab.wTab / 2 - wCur / 2 + 10;
		yCurBegin = MainTab.yTab + 32;
		hCur = MainTab.hTab - 32;
		int limX = ((maxSize - 1) / MainTabShop.maxNumItemW + 1) * MainTab.wItem - hCur + miniItem;
		list = new ListNew(xCurBegin, yCurBegin, wCur, hCur, 0, 0, limX, isLim0: true);
		scrShop.setInfo(xCurBegin + wCur + miniItem, yCurBegin + miniItem / 2, hCur - miniItem * 2, 8809550);

		if (IdSelect >= 0 && IdSelect < vecShop.size())
		{
			itemCur = (MainItem)vecShop.elementAt(IdSelect);
		}
		else
		{
			itemCur = null;
		}
		setPosCmd(getMenuActionItem());
	}

	public override mVector getMenuActionItem()
	{
		mVector mVector2 = null;
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
			mVector2 = new mVector();
			if (itemCur.colorName == 1)
			{
				mVector2.addElement(cmdDonotUse);
			}
			else
			{
				mVector2.addElement(cmdUsePotion);
			}
		}
		return mVector2;
	}
}
