using System;

public class TabShop : MainTabShop
{
	public static iCommand cmdBuyItem;

	public static iCommand cmdBuyPotion;

	public static iCommand cmdShip;

	public static iCommand cmdChangeShip;

	public static iCommand cmdUse;

	public static iCommand cmdDonotUse;

	public static iCommand cmdBuyHair;

	public static iCommand cmdBuyIconClan;

	public static iCommand cmdOpenRebuildItem;

	public static iCommand cmdOpenMenuDaKham;

	public static MainObject objPaint = new MainObject();

	public bool isSelect = true;

	private int[] mNumBuyRuby = new int[3] { 1, 5, 20 };

	private short hairEff = -1;

	private short demhairEff;

	public TabShop(string name, mVector vec, sbyte typeShop, int xTab)
		: base(name, vec, vec.size(), xTab)
	{
		typeNPCShop = typeShop;
		indexIconTab = 1;
		if (typeShop == 6)
		{
			indexIconTab = 8;
		}
		if (typeNPCShop == 103)
		{
			addWearingHair(-1, 0);
		}
		else if (typeNPCShop == 112)
		{
			addWearingHair(-1, 0);
		}
		else if (typeNPCShop == 105 || typeNPCShop == 113 || typeNPCShop == 114)
		{
			addWearingFashion(-1, null);
		}
		cmdShip = new iCommand(T.ship, 11, this);
		cmdChangeShip = new iCommand(T.changeship, 12, this);
		cmdUse = new iCommand(T.cmdUse, 13, this);
		cmdDonotUse = new iCommand(T.thaora, 13, this);
		cmdMenu = new iCommand(T.select, 10, this);
		cmdBuyHair = new iCommand(T.select, 3, 1, this);
		cmdBuyPotion = new iCommand(T.cmdBuy, 4, this);
		cmdBuyItem = new iCommand(T.cmdBuy, 3, 0, this);
		cmdBuyIconClan = new iCommand(T.select, 3, 2, this);
		cmdOpenRebuildItem = new iCommand(T.Upgrade, 15, this);
		cmdOpenMenuDaKham = new iCommand(T.menu, 16, this);
		if (typeNPCShop == 113 || typeNPCShop == 114)
		{
			cmdBuyHair = new iCommand(T.select, 3, 3, this);
		}
		beginFocus();
	}

	public override void commandPointer(int index, int subIndex)
	{
		mVector mVector2;
		switch (index)
		{
		case 3:
			if (itemCur == null)
			{
				return;
			}
			if (typeNPCShop != 116 && typeNPCShop != 118)
			{
				if (subIndex == 1)
				{
					string text = T.price + " " + itemCur.price + " " + T.bery + ".";
					if (itemCur.price == 0)
					{
						text = T.price + " " + itemCur.priceRuby + " " + T.ruby + ".";
					}
					if (itemCur.price <= 0 && itemCur.priceRuby <= 0)
					{
						text = string.Empty;
					}
					GameCanvas.Start_Normal_DiaLog(T.hoiMuaItem + itemCur.name + "?\n" + text, new iCommand(T.cmdBuy, 5, 1, this), isCmdClose: true);
					return;
				}
				if (subIndex == 2)
				{
					if (itemCur.priceRuby == 0)
					{
						GameCanvas.Start_Normal_DiaLog(T.banmuonchon + itemCur.name + T.lamcohieu, new iCommand(T.select, 5, 1, this), isCmdClose: true);
						return;
					}
					GameCanvas.Start_Normal_DiaLog(T.banmuonchon + itemCur.name + T.lamcohieu + " " + T.price + " " + itemCur.priceRuby + " " + T.ruby + ".", new iCommand(T.select, 5, 1, this), isCmdClose: true);
					return;
				}
				if (subIndex == 3)
				{
					GameCanvas.Start_Normal_DiaLog(T.banmuonchon + itemCur.name + "?", new iCommand(T.select, 5, 1, this), isCmdClose: true);
					return;
				}
				GameCanvas.Start_Normal_DiaLog(T.hoiMuaItem + "1 " + itemCur.name + "?", new iCommand(T.cmdBuy, 5, 1, this), isCmdClose: true);
				return;
			}
			GameCanvas.Start_Normal_DiaLog(T.banmuonchon + itemCur.namepaint + ".", new iCommand(T.doiqua, 5, 1, this), isCmdClose: true);
			return;
		case 4:
			if (itemCur == null)
			{
				return;
			}
			if (typeNPCShop != 116 && typeNPCShop != 118)
			{
				mVector2 = new mVector();
				string text2 = (itemCur.price > 0) ? ("(" + itemCur.price + " " + T.bery + " / 1 " + T.mon + ")") : ("(" + itemCur.priceRuby + " " + T.ruby + " / 1 " + T.mon + ")");
				for (int i = 0; i < mNumBuyRuby.Length; i++)
				{
					iCommand iCommand2 = new iCommand("x" + mNumBuyRuby[i], 14, mNumBuyRuby[i], this);
					mVector2.addElement(iCommand2);
				}
				GameCanvas.Start_Normal_DiaLog_New(T.hoiMua + "\n" + text2, mVector2, isCmdClose: true, itemCur.name);
				return;
			}
			GameCanvas.Start_Normal_DiaLog(T.banmuonchon + itemCur.namepaint + ".", new iCommand(T.doiqua, 5, 1, this), isCmdClose: true);
			return;
		case 5:
			if (itemCur != null)
			{
				GlobalService.gI().Buy_Item_Potion(typeNPCShop, itemCur.ID, 1, itemCur.typeObject);
				GameCanvas.Start_Waiting_DiaLog(T.pleaseWaiting, isCmdClose: true);
			}
			return;
		case 6:
			if (itemCur != null)
			{
				int num = 1;
				try
				{
					num = int.Parse(input.tfInput.getText());
					if (num < 0) num = 1;
				}
				catch (Exception)
				{
					num = 1;
				}
				GameCanvas.end_Dialog();
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.pleaseWaiting);
				GlobalService.gI().Buy_Item_Potion(typeNPCShop, itemCur.ID, (short)num, itemCur.typeObject);
			}
			return;
		case 10:
			mVector2 = getMenuActionItem();
			if (mVector2 != null)
			{
				GameCanvas.menu.startAt(mVector2, 2, T.menu);
			}
			break;
		case 11:
			GlobalService.gI().Ship(1);
			return;
		case 12:
			GlobalService.gI().Ship(0);
			return;
		case 13:
			if (itemCur != null)
			{
				GlobalService.gI().Use_Item(itemCur.ID, itemCur.typeObject);
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.pleaseWaiting);
			}
			return;
		case 14:
			if (tickbuy <= 0 && itemCur != null)
			{
				GlobalService.gI().Buy_Item_Potion(typeNPCShop, itemCur.ID, (short)subIndex, itemCur.typeObject);
				tickbuy = 5;
			}
			return;
		case 15:
			GlobalService.gI().Upgrade_Item(7, 0, 0);
			return;
		case 16:
			mVector mVector3 = new mVector();
			mVector3.addElement(new iCommand(T.cuonghoa, 17, 9, this));
			mVector3.addElement(new iCommand(T.cmdChuyenHoa, 17, 12, this));
			mVector3.addElement(new iCommand(T.kham, 17, 10, this));
			mVector3.addElement(new iCommand(T.duclo, 17, 13, this));
			GameCanvas.menuCur.startAt(mVector3, 2, T.menu);
			return;
		case 17:
			GlobalService.gI().Upgrade_Item((sbyte)subIndex, 0, 0);
			break;
		default:
			base.commandPointer(index, subIndex);
			break;
		}
	}

	public override mVector getMenuActionItem()
	{
		mVector result = null;
		MainItem mainItem = null;
		if (vecShop != null && IdSelect >= 0 && IdSelect < vecShop.size())
		{
			mainItem = (MainItem)vecShop.elementAt(IdSelect);
		}
		if (mainItem != null)
		{
			itemCur = mainItem;
			if (!isSelect)
			{
				return null;
			}
			result = itemCur.getActionShop(typeNPCShop);
			if (typeNPCShop == 103)
			{
				addWearingHair(7, itemCur.idIcon);
			}
			else if (typeNPCShop == 112)
			{
				addWearingHair(6, itemCur.idIcon);
			}
			else if (typeNPCShop == 105 || typeNPCShop == 113 || typeNPCShop == 114)
			{
				addWearingFashion(7, itemCur.mWearing);
			}
		}
		else if (typeNPCShop == 103)
		{
			addWearingHair(7, GameScreen.player.hair);
		}
		else if (typeNPCShop == 112)
		{
			addWearingHair(6, GameScreen.player.head);
		}
		else if (typeNPCShop == 105)
		{
			addWearingFashion(-1, null);
		}
		return result;
	}

	public override void paintItemCur(mGraphics g, MainItem Item, int x, int y)
	{
		if (MainTabShop.itemShipCur != null && Item.typeObject == MainTabShop.itemShipCur.typeObject && Item.ID == MainTabShop.itemShipCur.ID && typeNPCShop == 101)
		{
			g.drawImage(AvMain.imgcheck, x, y, mGraphics.RIGHT | mGraphics.BOTTOM);
		}
	}

	public override void paintShowBoat(mGraphics g, int x, int y)
	{
		if (typeNPCShop == 102)
		{
			if (GameScreen.player.myBoat == null)
			{
				return;
			}
			for (int i = 0; i < GameScreen.player.myBoat.Length; i++)
			{
				if (itemCur != null && i == itemCur.typeBoat)
				{
					ItemBoat.paintPartBoat(g, itemCur.idPart, itemCur.typeBoat, x, y, 0, 0);
				}
				else
				{
					ItemBoat.paintPartBoat(g, GameScreen.player.myBoat[i], i, x, y, 0, 0);
				}
			}
			ItemBoat.paintPartBoat(g, 0, 100, x, y, 0, 0);
		}
		else if (typeNPCShop == 103 || typeNPCShop == 105 || typeNPCShop == 112 || typeNPCShop == 113 || typeNPCShop == 114)
		{
			objPaint.paintShadow(g, x, y + 4);
			objPaint.paintCharShow(g, x, y + 4, 0, isNhip: true);
		}
	}

	public override void updateBuyItem(short id, sbyte type)
	{
		for (int i = 0; i < vecShop.size(); i++)
		{
			MainItem mainItem = (MainItem)vecShop.elementAt(i);
			if (mainItem.typeObject == type && mainItem.ID == id)
			{
				mainItem.price = 0;
				mainItem.priceRuby = 0;
				mainItem.vecInfo.removeAllElements();
				mainItem.addInfoFrist(T.dasuhuu, 1);
				mainItem.isShop = true;
				if (mainItem.info != null && mainItem.info.Length > 0)
				{
					mainItem.setInfoPotion(mainItem.info);
				}
				mainItem.colorName = 1;
				break;
			}
		}
		updateTrangBi();
		setPosCmd(getMenuActionItem());
	}

	public override void updateTrangBi()
	{
		if (typeNPCShop == 103 || typeNPCShop == 112)
		{
			int num = GameScreen.player.hair;
			if (typeNPCShop == 112)
			{
				num = GameScreen.player.head;
			}
			for (int i = 0; i < vecShop.size(); i++)
			{
				MainItem mainItem = (MainItem)vecShop.elementAt(i);
				if (mainItem.price == 0 && mainItem.priceRuby == 0)
				{
					mainItem.vecInfo.removeAllElements();
					if (mainItem.idIcon == num)
					{
						mainItem.addInfoFrist(T.daTrangBi, 4);
						mainItem.colorName = 4;
					}
					else
					{
						mainItem.addInfoFrist(T.dasuhuu, 1);
						mainItem.colorName = 1;
					}
				}
			}
		}
		else if (typeNPCShop == 102)
		{
			for (int j = 0; j < vecShop.size(); j++)
			{
				MainItem mainItem2 = (MainItem)vecShop.elementAt(j);
				if (mainItem2.price != 0 || mainItem2.priceRuby != 0)
				{
					continue;
				}
				for (int k = 0; k < GameScreen.player.myBoat.Length; k++)
				{
					if (mainItem2.typeBoat == k)
					{
						mainItem2.vecInfo.removeAllElements();
						if (mainItem2.idPart == GameScreen.player.myBoat[k])
						{
							mainItem2.colorName = 4;
							mainItem2.addInfoFrist(T.daTrangBi, 4);
						}
						else
						{
							mainItem2.colorName = 1;
							mainItem2.addInfoFrist(T.dasuhuu, 1);
						}
					}
				}
			}
		}
		else if (typeNPCShop == 105 || typeNPCShop == 113)
		{
			for (int l = 0; l < vecShop.size(); l++)
			{
				MainItem mainItem3 = (MainItem)vecShop.elementAt(l);
				if (mainItem3.price != 0 || mainItem3.priceRuby != 0)
				{
					continue;
				}
				mainItem3.vecInfo.removeAllElements();
				if (mainItem3.ID == Player.idFashion)
				{
					mainItem3.addInfoFrist(T.daTrangBi, 4);
					mainItem3.colorName = 4;
					mainItem3.isShop = true;
					if (mainItem3.info.Length > 0)
					{
						mainItem3.setInfoPotion(mainItem3.info);
					}
				}
				else
				{
					mainItem3.addInfoFrist(T.dasuhuu, 1);
					mainItem3.colorName = 1;
					mainItem3.isShop = true;
					if (mainItem3.info.Length > 0)
					{
						mainItem3.setInfoPotion(mainItem3.info);
					}
				}
			}
		}
		setPosCmd(getMenuActionItem());
	}

	public void addWearingHair(int type, int value)
	{
		switch (type)
		{
		case -1:
			if (GameScreen.player != null)
			{
				objPaint.sethead(GameScreen.player.head);
				objPaint.sethair(GameScreen.player.hair);
				objPaint.body = GameScreen.player.body;
				objPaint.leg = GameScreen.player.leg;
				objPaint.weapon = GameScreen.player.weapon;
				objPaint.clazz = GameScreen.player.clazz;
				objPaint.nFrameEffHair = 0;
				objPaint.nFrameEffHead = 0;
				objPaint.setHeadBigBody();
			}
			break;
		case 7:
			objPaint.resetPart();
			objPaint.sethair((short)value);
			hairEff = (short)value;
			objPaint.nFrameEffHair = 0;
			objPaint.nFrameEffHead = 0;
			break;
		case 6:
			objPaint.sethead((short)value);
			break;
		}
	}

	public void addWearingFashion(int type, short[] mwearing)
	{
		if (GameScreen.player != null)
		{
			objPaint.sethead(GameScreen.player.head);
			objPaint.sethair(GameScreen.player.hair);
			objPaint.body = GameScreen.player.body;
			objPaint.leg = GameScreen.player.leg;
			objPaint.weapon = GameScreen.player.weapon;
			objPaint.hat = GameScreen.player.hat;
			objPaint.nFrameEffHair = 0;
			objPaint.nFrameEffHead = 0;
			objPaint.clazz = GameScreen.player.clazz;
			objPaint.setHeadBigBody();
		}
		if (type != -1)
		{
			objPaint.setWearingIsNull(mwearing);
			objPaint.clazz = GameScreen.player.clazz;
			objPaint.setHeadBigBody();
		}
	}

	public override iCommand getCmdLeft()
	{
		if (typeNPCShop == 6)
		{
			return cmdOpenRebuildItem;
		}
		if (typeNPCShop == 111)
		{
			return cmdOpenMenuDaKham;
		}
		return null;
	}

	public override void updateInshop()
	{
		if ((typeNPCShop != 103 && typeNPCShop != 105 && typeNPCShop != 112 && typeNPCShop != 113 && typeNPCShop != 114) || objPaint == null)
		{
			return;
		}
		if (GameCanvas.gameTick % 100 == 0)
		{
			if (objPaint.isDonotShowHat == 1)
			{
				objPaint.isDonotShowHat = 0;
			}
			else
			{
				objPaint.isDonotShowHat = 1;
			}
		}
		if (typeNPCShop == 103 && hairEff == 772 && GameCanvas.gameTick % 60 == 0)
		{
			objPaint.sethair((short)(hairEff + demhairEff % 3));
			objPaint.nFrameEffHair = 0;
			demhairEff++;
		}
	}
}
