using System;

public class MainItem
{
	public int numInt = 1;

	public int x;

	public int y;

	public int timeUse;

	public int price;

	public int priceVND;

	public short ID;

	public short idIcon = -1;

	public short priceRuby;

	public short Lv_RQ;

	public short numPotion = 1;

	public short indexHotKey;

	public short idPart;

	public short maxTimeUse;

	public short indexInfoPotion;

	public short valueCheTac;

	public short IDMarket;

	public short numPotionNeed;

	public sbyte indexSort;

	public sbyte typeMaterial;

	public sbyte LvUpgrade;

	public sbyte typeBoat;

	public sbyte numLoKham;

	public sbyte typelock;

	public sbyte numHoleDaDuc;

	public sbyte LvDevilSkill;

	public sbyte phanTramDevilSkill;

	public sbyte indexUniform = -1;

	public sbyte isHoanMy;

	public sbyte valueKickAn;

	public sbyte typeSpec;

	public sbyte perSuc;

	public short[] mDaKham;

	public short[] mWearing;

	public string name;

	public string namepaint = "";

	public string info;

	public string nameUse;

	public mVector vecInfo = new mVector("MainItem.vecInfo");

	public sbyte isTrade;

	public sbyte charClass;

	public sbyte Hp_Mp_Other;

	public sbyte typeEquip;

	public int wInfo = 140;

	public int hInfo = 40;

	public short timeDelayPotion;

	public short value;

	public sbyte colorName;

	public sbyte typeObject;

	public sbyte typeMarket;

	public static MainInfoItem[] mNameAttributes;

	public static string[] DEFAULT_ATTR_NAMES = new string[]
	{
		"Tấn công", "Phòng thủ", "Chí mạng", "Xuyên giáp", "Né tránh",
		"Chính xác", "Máu", "Năng lượng", "Tốc độ", "Kháng tất cả"
	};

	public static sbyte[] DEFAULT_ATTR_PERCENT = new sbyte[10];

	public static void checkLoadDataAttri()
	{
		if (mNameAttributes == null)
		{
			GlobalService.gI().get_DATA(2);
		}
	}

	public static MyHashTable hashPotionTem = new MyHashTable();

	public static MyHashTable hashMaterialTem = new MyHashTable();

	public static MyHashTable hashPotionClan = new MyHashTable();

	public static MyHashTable hashAttriKichAn = new MyHashTable();

	public bool isRemove;

	public bool isIconClan;

	public bool isShop;

	public bool isPaint = true;

	public bool isRemoveVecEff;

	public static mImage imgColorItem;

	public FrameImage fraImgVip;

	public bool isloadfra;

	public static Effect_UpLv_Item eff_UpLv = new Effect_UpLv_Item();

	public static Effect_UpLv_Item eff_UpLv_Clan = new Effect_UpLv_Item();

	public static Effect_UpLv_Item eff_UpLv_Sub = new Effect_UpLv_Item();

	public CountDownTicket marketTime = new CountDownTicket();

	public const sbyte MARKET_NORMAL = 0;

	public const sbyte MARKET_SELLING = 1;

	public const sbyte MARKET_SELLED = 2;

	public const sbyte MARKET_SELL_END = 3;

	public const sbyte MARKET_BUY_COMPLETE = 4;

	public static short[] ID_POTION_CAN_SELL;

	public static short[] ID_MATERIAL_CAN_SELL;

	public const sbyte SPEC_HEART = 1;

	public bool isQuickOpen;

	public bool isKham;

	public int hRunInfo;

	private int lastTick;

	private int framepaint;

	public static int[] mValueUpgrade = new int[17]
	{
		0, 10, 20, 30, 45, 60, 75, 90, 110, 130,
		150, 150, 150, 150, 150, 150, 150
	};

	public MainItem()
	{
	}

	public MainItem(sbyte type, short idIcon, short id, int num)
	{
		typeObject = type;
		this.idIcon = idIcon;
		ID = id;
		numInt = num;
		numPotion = 0;
	}

	public MainItem(sbyte type, short IdIcon, short ID)
	{
		typeObject = type;
		idIcon = IdIcon;
		this.ID = ID;
		numPotion = 0;
	}

	public MainItem(sbyte typeItem, short ID, short idIcon, string name, sbyte isTrade)
	{
		typeObject = typeItem;
		this.ID = ID;
		this.idIcon = idIcon;
		this.name = name;
		this.isTrade = isTrade;
		namepaint = name;
	}

	public MainItem(sbyte typeItem, short ID, short idIcon, short num, sbyte color, sbyte lvUp)
	{
		typeObject = typeItem;
		this.ID = ID;
		this.idIcon = idIcon;
		numPotion = num;
		colorName = color;
		LvUpgrade = lvUp;
		if (LvUpgrade > 0)
		{
			namepaint = name + " +" + LvUpgrade;
		}
		else
		{
			namepaint = name;
		}
	}

	public void setInfoPotion(string info)
	{
		if (info.Length != 0)
		{
			string[] array = null;
			array = mFont.tahoma_7_white.splitFontArray(info, wInfo);
			for (int i = 0; i < array.Length; i++)
			{
				addInfo(array[i], 0);
			}
			if (!isShop || numPotion > 1)
			{
				addInfo(T.soluong + ": " + numPotion, 0);
			}
		}
	}

	public virtual void setInfoItem(MainInfoItem[] mInfoItem)
	{
	}

	public static MainInfoItem getNameAttribute(int id)
	{
		if (mNameAttributes != null && id >= 0 && id < mNameAttributes.Length && mNameAttributes[id] != null)
		{
			return mNameAttributes[id];
		}
		return null;
	}

	public static string getAttributeNameSafe(int id)
	{
		MainInfoItem it = getNameAttribute(id);
		if (it != null && !string.IsNullOrEmpty(it.name))
		{
			return it.name;
		}
		return getFallbackAttributeName(id);
	}

	public static sbyte getAttributePercentSafe(int id)
	{
		MainInfoItem it = getNameAttribute(id);
		if (it != null)
		{
			return it.ispercent;
		}
		return getFallbackAttributePercent(id);
	}

	public static sbyte getAttributeColorSafe(int id)
	{
		MainInfoItem it = getNameAttribute(id);
		if (it != null && it.color != 0)
		{
			return it.color;
		}
		return 4;
	}

	public void addInfoSell(short id, int value)
	{
		string nameAttr = getAttributeNameSafe(id);
		sbyte isPercent = getAttributePercentSafe(id);
		sbyte color = getAttributeColorSafe(id);
		string text = nameAttr + " ";
		text = text + strGetPercent(value, isPercent) + " + ?";
		int num = mFont.tahoma_7_black.getWidth(text) + 10;
		if (num > wInfo)
		{
			wInfo = num;
		}
		vecInfo.addElement(new infoShow(id, value, color, -1));
		updateHInfo();
	}

	public static string getInfoAttriSS(short id, int value, int valueLvUp, int valueLvCur)
	{
		string nameAttr = getAttributeNameSafe(id);
		sbyte isPercent = getAttributePercentSafe(id);
		string text = nameAttr + " ";
		int num = valueSameUpgrade(value, valueLvCur, valueLvUp);
		return text + strGetPercent(num, isPercent);
	}

	public void addInfo(short id, int value, sbyte colorMain)
	{
		string nameAttr = getAttributeNameSafe(id);
		sbyte isPercent = getAttributePercentSafe(id);
		sbyte color = getAttributeColorSafe(id);
		string text = nameAttr + " ";
		text += strGetPercent(value, isPercent);
		int num = mFont.tahoma_7b_black.getWidth(text) + 4;
		if (typelock == 1)
		{
			num += 12;
		}
		if (charClass >= 1)
		{
			num += 12;
		}
		if (LvUpgrade > 0)
		{
			num += 16;
		}
		if (num > wInfo)
		{
			wInfo = num;
		}
		if (colorMain >= 0 && colorMain <= 8)
		{
			color = colorMain;
		}
		vecInfo.addElement(new infoShow(id, value, color, colorMain));
		updateHInfo();
	}

	public void addInfo(short id, int value, sbyte colorMain, sbyte percent)
	{
		string nameAttr = getAttributeNameSafe(id);
		sbyte isPercent = (percent == -1) ? getAttributePercentSafe(id) : percent;
		sbyte color = getAttributeColorSafe(id);
		string text = nameAttr + " ";
		text += strGetPercent(value, isPercent);
		int num = mFont.tahoma_7b_black.getWidth(text) + 4;
		if (typelock == 1)
		{
			num += 12;
		}
		if (charClass >= 1)
		{
			num += 12;
		}
		if (LvUpgrade > 0)
		{
			num += 16;
		}
		if (num > wInfo)
		{
			wInfo = num;
		}
		if (colorMain >= 0 && colorMain <= 8)
		{
			color = colorMain;
		}
		vecInfo.addElement(new infoShow(id, value, color, colorMain));
		updateHInfo();
	}

	public virtual void addInfo(string str, sbyte color)
	{
		vecInfo.addElement(new infoShow(-1, 0, str, color, -1));
		updateHInfo();
	}

	public virtual void addInfo(string str, sbyte color, sbyte colorMain)
	{
		vecInfo.addElement(new infoShow(-1, 0, str, color, colorMain));
		updateHInfo();
	}

	public void addInfoFrist(string str, sbyte color)
	{
		vecInfo.insertElementAt(new infoShow(-1, 0, str, color, -1), 0);
		updateHInfo();
	}

	public void updateHInfo()
	{
		hInfo = (vecInfo.size() + 1) * GameCanvas.hText;
		if (numLoKham > 0)
		{
			hInfo += 22;
		}
		if (isHoanMy == 1)
		{
			hInfo += 14;
		}
		if (hInfo > MainTab.hTab - GameCanvas.hCommand * 3 / 2)
		{
			hRunInfo = hInfo - (MainTab.hTab - GameCanvas.hCommand * 3 / 2);
		}
		else
		{
			hRunInfo = 0;
		}
	}

	public static string strGetPercent(int value, sbyte isPer)
	{
		string text = "";
		switch (isPer)
		{
		case 0:
			text += value;
			break;
		case 1:
			text = text + value / 10 + "," + CRes.abs(value) % 10 + "%";
			break;
		case 2:
			text = text + value / 100 + "," + CRes.abs(value) % 100 + "%";
			break;
		case 10:
			text = text + value / 10 + "," + CRes.abs(value) % 10 + "s";
			break;
		}
		return text;
	}

	public static int getColorName(sbyte color)
	{
		switch (color)
		{
			case 1: return 0x00FF00;
			case 2: return 0x0088FF;
			case 3: return 0xAA00FF;
			case 4: return 0xFF8800;
			case 5: return 0xFF0000;
			default: return 0xFFFFFF;
		}
	}

	public void paintColor(mGraphics g, int x, int y, int w)
	{
		if (colorName * 32 + w <= mImage.getImageHeight(imgColorItem.image))
		{
			g.drawRegion(imgColorItem, 0, colorName * 32, w, w, 0, x, y, 3);
		}
	}

	public virtual void paint(mGraphics g, int x, int y, int w)
	{
		MainImage mainImage = null;
		mainImage = getImage();
		if (mainImage != null && mainImage.img != null)
		{
			paintImgItem(g, mainImage, x, y);
		}
		else
		{
			AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
		}
		paintEff_LvUp(g, x, y, w, 0);
	}

	public virtual void paintNumPotion(mGraphics g, int x, int y, int w, short num)
	{
		int half = (w > 0) ? (w / 2) : (MainTab.wItem / 2);
		int bgX = x + half - 11;
		int bgY = y + half - 6;
		int textY = bgY - 5;
		if (numPotionNeed > 0)
		{
			if (AvMain.imgBgnum != null) g.drawImage(AvMain.imgBgnum, bgX, bgY, 3);
			mFont mFont2 = (numPotionNeed > numPotion) ? mFont.tahoma_7_red : mFont.tahoma_7_yellow;
			string str = numPotion + "/" + numPotionNeed;
			if (mFont.tahoma_7_black != null) mFont.tahoma_7_black.drawString(g, str, bgX + 1, textY + 1, 2);
			mFont2.drawString(g, str, bgX, textY, 2);
		}
		else if (num > 1)
		{
			if (AvMain.imgBgnum != null) g.drawImage(AvMain.imgBgnum, bgX, bgY, 3);
			string str = num.ToString() ?? "";
			if (mFont.tahoma_7_black != null) mFont.tahoma_7_black.drawString(g, str, bgX + 1, textY + 1, 2);
			mFont.tahoma_7_yellow.drawString(g, str, bgX, textY, 2);
		}
	}

	public virtual void paintQuay(mGraphics g, int x, int y, int w)
	{
	}

	public virtual void paintNumPotionQuay(mGraphics g, int x, int y, int w, short num)
	{
		int half = (w > 0) ? (w / 2) : (MainTab.wItem / 2);
		int bgX = x + half - 11;
		int bgY = y + half - 6;
		int textY = bgY - 5;
		if (numPotionNeed > 0)
		{
			if (AvMain.imgBgnum != null) g.drawImage(AvMain.imgBgnum, bgX, bgY, 3);
			mFont mFont2 = (numPotionNeed > numPotion) ? mFont.tahoma_7_red : mFont.tahoma_7_yellow;
			string str = numPotion + "/" + numPotionNeed;
			if (mFont.tahoma_7_black != null) mFont.tahoma_7_black.drawString(g, str, bgX + 1, textY + 1, 2);
			mFont2.drawString(g, str, bgX, textY, 2);
		}
		else
		{
			if (AvMain.imgBgnum != null) g.drawImage(AvMain.imgBgnum, bgX, bgY, 3);
			string str = num.ToString() ?? "";
			if (mFont.tahoma_7_black != null) mFont.tahoma_7_black.drawString(g, str, bgX + 1, textY + 1, 2);
			mFont.tahoma_7_yellow.drawString(g, str, bgX, textY, 2);
		}
	}

	public virtual void paintPotion(mGraphics g, int x, int y, int w)
	{
		paintNumPotion(g, x, y, w, numPotion);
	}

	public void paint(mGraphics g, int x, int y, int w, int lech)
	{
		MainImage mainImage = null;
		mainImage = getImage();
		if (mainImage != null && mainImage.img != null)
		{
			paintImgItem(g, mainImage, x, y);
		}
		else
		{
			AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
		}
		paintEff_LvUp(g, x, y, w, lech);
	}

	public void paintEffSub(mGraphics g, int x, int y, int w, int lech)
	{
		MainImage mainImage = null;
		mainImage = getImage();
		if (mainImage != null && mainImage.img != null)
		{
			paintImgItem(g, mainImage, x, y);
		}
		else
		{
			AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
		}
		if (LvUpgrade > 0)
		{
			eff_UpLv_Sub.paintUpgradeEffect(x, y, LvUpgrade, w - 4, g, lech, isBorder: true);
		}
	}

	public void paintEff_LvUp(mGraphics g, int x, int y, int w, int lech)
	{
		if (indexUniform > -1 && indexUniform <= 2)
		{
			AvMain.fraUniform.drawFrame(indexUniform, x - w / 2 + 2, y + w / 2 - 9, 0, 0, g);
		}
		if (typelock == 1)
		{
			g.drawImage(AvMain.imgLock, x + w / 2 - 1 - 8, y - w / 2 + 2, 0);
		}
		if (LvUpgrade > 0)
		{
			int upgrade = LvUpgrade;
			if (typeSpec == 1)
			{
				upgrade = ((LvUpgrade <= 100) ? (LvUpgrade % 10) : 10);
			}
			eff_UpLv.paintUpgradeEffect(x, y, upgrade, w - 4, g, lech, isBorder: true);
		}
		if (mDaKham == null)
		{
			return;
		}
		for (int i = 0; i < mDaKham.Length; i++)
		{
			int num = (mDaKham[i] - 44) / 6;
			if (mDaKham[i] >= 324 && mDaKham[i] <= 326)
			{
				num = GameCanvas.gameTick / 5 % 6;
			}
			else if (mDaKham[i] >= 241 && mDaKham[i] <= 270)
			{
				num = (mDaKham[i] - 241) / 5;
			}
			else if ((mDaKham[i] >= 368 && mDaKham[i] <= 373) || (mDaKham[i] >= 362 && mDaKham[i] <= 367) || (mDaKham[i] >= 647 && mDaKham[i] <= 682))
			{
				num = 6;
			}
			int pad = 4;
			if (w <= 12)
			{
				pad = 1;
			}
			else if (w <= 20)
			{
				pad = 2;
			}
			int x1 = x - w / 2 + pad;
			int x2 = x + w / 2 - pad;
			int y1 = y - w / 2 + pad;
			int y2 = y + w / 2 - pad;
			int innerW = x2 - x1;
			int innerH = y2 - y1;
			if (innerW <= 0) innerW = 1;
			if (innerH <= 0) innerH = 1;
			int perimeter = 2 * (innerW + innerH);
			int speedTick = GameCanvas.gameTick;
			int pos = (speedTick + (i * perimeter) / mDaKham.Length) % perimeter;
			if (pos < 0) pos += perimeter;

			int num2;
			int num3;
			if (pos < innerW)
			{
				num2 = x1 + pos;
				num3 = y1;
			}
			else if (pos < innerW + innerH)
			{
				num2 = x2;
				num3 = y1 + (pos - innerW);
			}
			else if (pos < innerW * 2 + innerH)
			{
				num2 = x2 - (pos - (innerW + innerH));
				num3 = y2;
			}
			else
			{
				num2 = x1;
				num3 = y2 - (pos - (innerW * 2 + innerH));
			}
			if (num >= 0 && num < AvMain.fraEffItem.nFrame)
			{
				if ((mDaKham[i] >= 241 && mDaKham[i] <= 270) || (mDaKham[i] >= 368 && mDaKham[i] <= 373) || (mDaKham[i] >= 647 && mDaKham[i] <= 682))
				{
					AvMain.fraEffItem2.drawFrame(num * 2 + GameCanvas.gameTick / 5 % 2, num2, num3, 0, 3, g);
				}
				else
				{
					AvMain.fraEffItem.drawFrame(num * 2 + GameCanvas.gameTick / 5 % 2, num2, num3, 0, 3, g);
				}
			}
		}
	}

	public MainImage getImageAll()
	{
		MainImage result = null;
		if (typeObject == 3)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.hashImageItem, 3000);
		}
		if (typeObject == 7)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.hashImageMaterialPotion, 6500);
		}
		if (typeObject == 4)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.hashImagePotion, 2000);
		}
		if (typeObject == 100)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.hashImageItemOther, 9000);
		}
		if (typeObject == 104)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.hashImageSkill, 4000);
		}
		if (typeObject == 105)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.HashImageFashion, 20000);
		}
		if (typeObject == 110)
		{
			result = ObjectData.getImageAll(idIcon, ObjectData.HashImageOtherNew, 23000);
		}
		return result;
	}

	public void paintAllItem_Num1(mGraphics g, int x, int y, int w, int lech, sbyte color, short numPaint)
	{
		paintAllItem(g, x, y, w, lech, color, numPaint, typePaintNum: true);
	}

	public void paintAllItem(mGraphics g, int x, int y, int w, int lech, sbyte color)
	{
		paintAllItem(g, x, y, w, lech, color, numPotion, typePaintNum: true);
	}

	public void paintAllItem(mGraphics g, int x, int y, int w, int lech, sbyte color, short numpaint, bool typePaintNum)
	{
		MainImage imageAll = getImageAll();
		if (imageAll != null && imageAll.img != null)
		{
			paintImgItem(g, imageAll, x, y);
		}
		else
		{
			AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
		}
		if (typeObject == 3)
		{
			AvMain.setTextColor(color).drawString(g, "Lv." + LvUpgrade, x + MainTab.wItem / 2 - 2, y + MainTab.wItem / 2 - 9 - 2, 1);
		}
		else if (typePaintNum)
		{
			paintNumPotion(g, x, y, w, numpaint);
		}
		paintEff_LvUp(g, x, y, w, lech);
	}

	public void paintImgItem(mGraphics g, MainImage img, int x, int y)
	{
		if (!isloadfra)
		{
			setFraImageVip(img);
		}
		if (fraImgVip != null)
		{
			int num = ((framepaint < fraImgVip.nFrame - 1) ? 3 : 15);
			if (CRes.abs(GameCanvas.gameTick - lastTick) > num)
			{
				framepaint++;
				if (framepaint >= fraImgVip.nFrame)
				{
					framepaint = 0;
				}
				lastTick = GameCanvas.gameTick;
			}
			fraImgVip.drawFrame((framepaint <= fraImgVip.nFrame - 1) ? framepaint : 0, x, y, 0, 3, g);
		}
		else
		{
			g.drawImage(img.img, x, y, 3);
		}
	}

	private void setFraImageVip(MainImage img)
	{
		if (img != null && img.img != null)
		{
			int imageWidth = mImage.getImageWidth(img.img.image);
			if (mImage.getImageHeight(img.img.image) / 2 >= imageWidth)
			{
				fraImgVip = new FrameImage(img.img, imageWidth, imageWidth);
			}
			isloadfra = true;
		}
	}

	public virtual void paintHotkey(mGraphics g, int x, int y, int w, int yLech)
	{
		paintAllItem(g, x, y, w, 0, 5);
	}

	public virtual MainImage getImage()
	{
		return getImageAll();
	}

	public static void removeUpdateItemVec(sbyte type, mVector vec)
	{
		if (vec == null) return;
		for (int i = 0; i < vec.size(); i++)
		{
			MainItem mainItem = (MainItem)vec.elementAt(i);
			if (mainItem != null && mainItem.typeObject == type)
			{
				vec.removeElement(mainItem);
				i--;
			}
		}
	}

	public static MainItem getItemVec(sbyte type, short id, mVector vec)
	{
		if (vec == null) return null;
		for (int i = 0; i < vec.size(); i++)
		{
			MainItem mainItem = (MainItem)vec.elementAt(i);
			if (mainItem != null && mainItem.typeObject == type && mainItem.ID == id)
			{
				return mainItem;
			}
		}
		return null;
	}

	public virtual void Use_Item()
	{
	}

	public virtual mVector getActionInven(sbyte type)
	{
		if (typeObject == 110)
		{
			mVector mVector2 = new mVector();
			if (colorName == 1)
			{
				mVector2.addElement(GameCanvas.tabInvenClan.cmdDonotUse);
			}
			else
			{
				mVector2.addElement(GameCanvas.tabInvenClan.cmdUsePotion);
			}
			return mVector2;
		}
		return null;
	}

	public virtual mVector getActionShop(sbyte typeShop)
	{
		mVector obj = new mVector();
		obj.addElement(TabShop.cmdBuyPotion);
		return obj;
	}

	public virtual mVector getActionChest()
	{
		return null;
	}

	public virtual mVector getActionUpgrade()
	{
		return null;
	}

	public virtual mVector getActionSplit()
	{
		return null;
	}

	public mVector getActionTrade()
	{
		mVector mVector2 = new mVector();
		if (TradeScreen.instance != null)
		{
			mVector2.addElement(TradeScreen.instance.cmdBovao);
		}
		return mVector2;
	}

	public static string getFallbackAttributeName(int id)
	{
		switch (id)
		{
		case 0: return "Tấn công";
		case 1: return "Tăng tấn công";
		case 2: return "Phép thuật";
		case 3: return "Phòng thủ";
		case 4: return "Tăng P.Thủ";
		case 5: return "T/n sức mạnh";
		case 6: return "T/n phòng thủ";
		case 7: return "T/n thể lực";
		case 8: return "T/n tinh thần";
		case 9: return "T/n nhanh nhẹn";
		case 10: return "Chí mạng";
		case 11: return "ST chí mạng";
		case 12: return "Né tránh";
		case 13: return "Xuyên giáp";
		case 14: return "Phản đòn";
		case 15: return "HP +";
		case 16: return "MP +";
		case 17: return "Tăng HP";
		case 18: return "Tăng MP";
		case 19: return "Tự hồi HP";
		case 20: return "Tự hồi MP";
		case 21: return "Hút HP";
		case 22: return "Hút MP";
		case 23: return "+ HP/Thức ăn";
		case 24: return "+ MP/Nước uống";
		case 25: return "Tốc độ hồi chiêu";
		case 26: return "Kháng vật lý";
		case 27: return "Kháng phép";
		case 28: return "Hiệu ứng";
		case 29: return "Tỷ lệ hiệu ứng";
		case 30: return "Thời gian hiệu ứng";
		case 31: return "Loại buff";
		case 32: return "Thời gian tác dụng";
		case 33: return "Tăng xp đánh quái";
		case 34: return "Tăng bery nhặt được";
		case 35: return "Tự hồi HP đánh quái";
		case 36: return "Sát thương lên Boss";
		case 37: return "Giảm ST nhận từ Boss";
		case 38: return "Kháng phản đòn";
		case 39: return "Exp Ác Quỷ";
		case 40: return "Hút năng lượng";
		case 41: return "Hút 1 phần máu";
		case 42: return "Hút máu theo TGian";
		case 43: return "X2 khả năng hồi phục";
		case 44: return "Giảm % Max MP";
		case 45: return "Giảm % Max Hp";
		case 46: return "Sát thương cuối";
		case 47: return "Giảm hiệu ứng đ/t";
		case 48: return "Sát thương % máu";
		case 49: return "Giảm chí mạng đ/t";
		case 50: return "Giảm xuyên giáp đ/t";
		case 51: return "Giảm né đòn đ/t";
		case 52: return "Giảm phản đòn đ/t";
		case 53: return "Miễn thương";
		case 54: return "Giảm 90% Damage";
		case 55: return "Từ chối tử thần";
		case 56: return "Máu cuối";
		case 57: return "Sát thương chuẩn";
		case 58: return "Hấp thụ";
		case 59: return "Hút % máu";
		case 60: return "Giảm sức mạnh";
		case 61: return "Hút sức mạnh";
		case 62: return "May Mắn";
		case 63: return "Giảm miễn thương";
		case 64: return "Bộc phá Ác Quỷ";
		case 65: return "Xuyên kháng phép";
		case 66: return "Mp Cuối";
		case 67: return "Tăng Exp đánh quái";
		case 68: return "Tăng Exp Skill đánh quái";
		case 69: return "Giảm ST chí mạng đ/t";
		case 70: return "Giảm P.Thủ cuối";
		case 71: return "Kháng hiệu ứng";
		case 72: return "Tăng beri train quái";
		case 73: return "C.Hóa ST thành Hp";
		case 74: return "C.Hóa ST thành Mp";
		case 75: return "Tăng % choáng";
		case 76: return "Tăng % chính xác";
		case 77: return "Giảm né cuối";
		case 78: return "Choáng xung quanh 2s";
		case 79: return "Hồi máu cuối mỗi 10s";
		case 80: return "+% gây Điện giật";
		case 81: return "Sát thương PvP";
		case 82: return "Giảm ST nhận từ người";
		case 83: return "Kháng chí mạng";
		case 84: return "Kháng choáng";
		case 85: return "Kháng tê liệt";
		case 86: return "Tốc độ di chuyển";
		case 87: return "Kháng sát thương chuẩn";
		case 88: return "Sát thương lên quái";
		case 89: return "Giảm ST nhận từ quái";
		case 90: return "Bỏ qua né tránh";
		case 91: return "Bỏ qua phản đòn";
		case 92: return "Kháng hút máu";
		case 93: return "Tăng hiệu lực bình máu";
		case 94: return "MP thức ăn tăng";
		case 95: return "Tăng tỷ lệ rớt đồ hiếm";
		case 96: return "Tăng Beri Boss";
		case 97: return "Tăng Tấn công theo Cấp";
		case 98: return "Tăng Phòng thủ theo Cấp";
		case 99: return "Tăng HP theo Cấp";
		case 100: return "Tăng MP theo Cấp";
		default: return "Chỉ số " + id;
		}
	}

	public static sbyte getFallbackAttributePercent(int id)
	{
		switch (id)
		{
		case 1:
		case 2:
		case 4:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 17:
		case 18:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 29:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 46:
		case 47:
		case 48:
		case 49:
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
		case 55:
		case 56:
		case 57:
		case 58:
		case 59:
		case 62:
		case 63:
		case 64:
		case 65:
		case 66:
		case 67:
		case 68:
		case 69:
		case 70:
		case 71:
		case 72:
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 78:
		case 79:
		case 80:
		case 81:
		case 82:
		case 83:
		case 84:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89:
		case 90:
		case 91:
		case 92:
		case 93:
		case 94:
		case 95:
		case 96:
			return 1;
		default:
			return 0;
		}
	}

	public static string getInfoEveryWhere(MainInfoItem info)
	{
		if (info == null)
		{
			return "";
		}
		string text = null;
		sbyte ispercent = 0;
		if (mNameAttributes != null && info.id >= 0 && info.id < mNameAttributes.Length && mNameAttributes[info.id] != null)
		{
			text = mNameAttributes[info.id].name;
			ispercent = mNameAttributes[info.id].ispercent;
		}
		if (string.IsNullOrEmpty(text) || text == "null")
		{
			text = getFallbackAttributeName(info.id);
			ispercent = getFallbackAttributePercent(info.id);
		}
		return text + " " + strGetPercent(info.value, ispercent);
	}

	public static string getTimeDelay(int value)
	{
		return value / 1000 + "," + value % 1000 / 100 + "s";
	}

	public static void LoadNameAttribute(DataInputStream iss, bool isSave)
	{
		if (iss == null)
		{
			GlobalService.gI().get_DATA(2);
			return;
		}
		try
		{
			short num = iss.readShort();
			mNameAttributes = new MainInfoItem[num];
			for (int i = 0; i < num; i++)
			{
				string text = iss.readUTF();
				mNameAttributes[i] = new MainInfoItem(text, iss.readByte(), iss.readByte());
			}
			if (isSave)
			{
				GlobalService.VerNameAtribute = iss.readShort();
				SaveRms.saveVer(GlobalService.VerNameAtribute, "VerdataAttri");
			}
			iss.close();
		}
		catch (Exception)
		{
		}
	}

	public static mVector SortVecItem(mVector vec)
	{
		if (vec == null) return null;
		int num = vec.size();
		int num2;
		for (int i = 0; i < num - 1; i++)
		{
			num2 = i;
			for (int j = i + 1; j < num; j++)
			{
				MainItem itJ = (MainItem)vec.elementAt(j);
				MainItem itMin = (MainItem)vec.elementAt(num2);
				if (itJ != null && itMin != null && itJ.indexSort < itMin.indexSort)
				{
					num2 = j;
				}
			}
			if (num2 != i)
			{
				swapItem(vec, i, num2);
			}
		}
		num2 = 0;
		for (int i = 0; i < num - 1; i++)
		{
			num2 = i;
			for (int j = i + 1; j < num; j++)
			{
				MainItem itJ = (MainItem)vec.elementAt(j);
				MainItem itMin = (MainItem)vec.elementAt(num2);
				if (itJ != null && itMin != null && itJ.typeObject == 4 && itJ.ID < itMin.ID)
				{
					num2 = j;
				}
			}
			if (num2 != i)
			{
				swapItem(vec, i, num2);
			}
		}
		return vec;
	}

	private static void swapItem(mVector actors, int dex1, int dex2)
	{
		object obj = actors.elementAt(dex2);
		actors.setElementAt(actors.elementAt(dex1), dex2);
		actors.setElementAt(obj, dex1);
	}

	public static mVector getInfoSS(MainItem itemSet)
	{
		mVector mVector2 = new mVector();
		if (itemSet == null || (itemSet.charClass != GameScreen.player.clazz && itemSet.charClass > 0) || itemSet.typeObject != 3)
		{
			return null;
		}
		MainItem mainItem = (MainItem)GameScreen.player.hashEquip.get(itemSet.typeEquip.ToString() ?? "");
		if (mainItem != null)
		{
			for (int i = 0; i < itemSet.vecInfo.size(); i++)
			{
				infoShow infoShow2 = (infoShow)itemSet.vecInfo.elementAt(i);
				bool flag = false;
				if (infoShow2.id >= 0 && infoShow2.colorMain == infoShow.HARDCODE_INFO_CO_BAN)
				{
					for (int j = 0; j < mainItem.vecInfo.size(); j++)
					{
						infoShow infoShow3 = (infoShow)mainItem.vecInfo.elementAt(j);
						if (infoShow2.id == infoShow3.id)
						{
							int num = infoShow2.value - infoShow3.value;
							sbyte color = 6;
							sbyte isPercent = getAttributePercentSafe(infoShow2.id);
							string str = strGetPercent(num, isPercent);
							if (num >= 0)
							{
								str = "+" + strGetPercent(num, isPercent);
								color = 1;
							}
							mVector2.addElement(new infoShow(-1, num, str, color, -1));
							flag = true;
							break;
						}
					}
				}
				if (!flag)
				{
					mVector2.addElement(new infoShow(-1, 0, "", 0, -1));
				}
			}
		}
		if (mVector2.size() == 0)
		{
			mVector2 = null;
		}
		return mVector2;
	}

	public static mVector getInfoSS(MainItem itemSet, int Plus)
	{
		mVector mVector2 = new mVector();
		if (itemSet == null || (itemSet.charClass != GameScreen.player.clazz && itemSet.charClass > 0))
		{
			return null;
		}
		MainItem mainItem = (MainItem)GameScreen.player.hashEquip.get(itemSet.typeEquip.ToString() ?? "");
		if (mainItem != null)
		{
			for (int i = 0; i < itemSet.vecInfo.size(); i++)
			{
				infoShow infoShow2 = (infoShow)itemSet.vecInfo.elementAt(i);
				bool flag = false;
				if (infoShow2.id >= 0 && infoShow2.colorMain == infoShow.HARDCODE_INFO_CO_BAN)
				{
					for (int j = 0; j < mainItem.vecInfo.size(); j++)
					{
						infoShow infoShow3 = (infoShow)mainItem.vecInfo.elementAt(j);
						if (infoShow2.id == infoShow3.id)
						{
							int num = valueSameUpgrade(infoShow2.value, itemSet.LvUpgrade, Plus) - infoShow3.value;
							sbyte color = 6;
							sbyte isPercent = getAttributePercentSafe(infoShow2.id);
							string str = strGetPercent(num, isPercent);
							if (num >= 0)
							{
								str = "+" + strGetPercent(num, isPercent);
								color = 1;
							}
							mVector2.addElement(new infoShow(-1, num, str, color, -1));
							flag = true;
							break;
						}
					}
				}
				if (!flag)
				{
					mVector2.addElement(new infoShow(-1, 0, "", 0, -1));
				}
			}
		}
		if (mVector2.size() == 0)
		{
			mVector2 = null;
		}
		return mVector2;
	}

	public static int valueSameUpgrade(int value, int cur, int up)
	{
		if (up == cur)
		{
			return value;
		}
		int num = value * 100 / (mValueUpgrade[cur] + 100);
		return num + num * mValueUpgrade[up] / 100;
	}

	public virtual bool getStar()
	{
		return false;
	}

	public virtual void setTimeMarket(int time)
	{
		timeUse = time;
		marketTime.setCountDown(time);
	}

	public void CheckTimeSell()
	{
		if (typeMarket == 1 && marketTime.timeCountDown <= 0)
		{
			typeMarket = 3;
		}
	}

	public static string getDataKichAn(Item item)
	{
		string text = "";
		text = (string)hashAttriKichAn.get(item.valueKickAn.ToString() ?? "");
		if (text == null)
		{
			hashAttriKichAn.put(item.valueKickAn.ToString() ?? "", "");
			GlobalService.gI().GetTemplate(96, item.valueKickAn);
			text = "";
		}
		if (text.Length == 0)
		{
			Item.vecItemKichAnCheckInfo.addElement(item);
		}
		return text;
	}

	public short getIdMarket()
	{
		return IDMarket;
	}

	public bool getInfoPotion(short index)
	{
		if (info == null || info.Length == 0)
		{
			indexInfoPotion = index;
			info = GetInfoPotion(indexInfoPotion);
			if (info.Length == 0)
			{
				return false;
			}
		}
		setInfoPotion(info);
		return true;
	}

	public static string GetInfoPotion(short index)
	{
		string text = (string)Potion.hashInfoPotion.get(index.ToString() ?? "");
		if (text == null)
		{
			Potion.hashInfoPotion.put(index.ToString() ?? "", "");
			GlobalService.gI().getDataInfoPotion(index);
			return "";
		}
		return text;
	}
}
