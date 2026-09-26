using System;

public class MenuMeScr
{
	public static void performAction(int actionType, MainScreen backScr)
	{
		if (GameCanvas.isCompactMode())
		{
			GameCanvas.tabInven.setTypeInven(0);
			GameCanvas.tabAllScr.Show(backScr);
			if (GameCanvas.currentScreen == GameCanvas.tabAllScr)
			{
				switch (actionType)
				{
					case 0: // Hanh Trang
						GameCanvas.tabAllScr.idSelect = 1;
						break;
					case 1: // Trang Bi
						GameCanvas.tabAllScr.idSelect = 0;
						break;
					case 2: // Thong Tin & Tiem Nang
						GameCanvas.tabAllScr.idSelect = 2;
						break;
					case 3: // Ky Nang
						GameCanvas.tabAllScr.idSelect = 4;
						break;
					case 4: // Nhiem Vu
						GameCanvas.tabAllScr.idSelect = 3;
						break;
					default:
						if (GameCanvas.tabAllScr.vecTabs != null && GameCanvas.tabAllScr.vecTabs.size() == 6)
						{
							GameCanvas.tabAllScr.idSelect = 5;
						}
						else
						{
							GameCanvas.tabAllScr.idSelect = 0;
						}
						break;
				}
				GameCanvas.tabAllScr.setTabSelect();
				if (GameCanvas.tabAllScr.tabCurrent != null)
				{
					GameCanvas.tabAllScr.tabCurrent.beginFocus();
				}
			}
			return;
		}

		switch (actionType)
		{
			case 0: // Hanh Trang
				DualTabScreen.gI().curMainTab = 0;
				DualTabScreen.gI().focusPane = 1;
				DualTabScreen.gI().Show(backScr);
				break;
			case 1: // Trang Bi
				DualTabScreen.gI().curMainTab = 0;
				DualTabScreen.gI().focusPane = 0;
				DualTabScreen.gI().Show(backScr);
				break;
			case 2: // Thong Tin & Tiem Nang
				DualTabScreen.gI().curMainTab = 1;
				DualTabScreen.gI().Show(backScr);
				break;
			case 3: // Ky Nang
				DualTabScreen.gI().curMainTab = 2;
				DualTabScreen.gI().Show(backScr);
				break;
			case 4: // Nhiem Vu
				DualTabScreen.gI().curMainTab = 3;
				DualTabScreen.gI().Show(backScr);
				break;
			case 5: // Thu Cung
				DualTabScreen.gI().curMainTab = 4;
				DualTabScreen.gI().openPetSubView();
				DualTabScreen.gI().Show(backScr);
				break;
			case 6: // Danh Hieu
				DualTabScreen.gI().curMainTab = 4;
				DualTabScreen.gI().openDanhHieuSubView();
				DualTabScreen.gI().Show(backScr);
				break;
			case 7: // Chuc Nang
			default:
				DualTabScreen.gI().curMainTab = 4;
				DualTabScreen.gI().chucNangSubView = 0;
				DualTabScreen.gI().Show(backScr);
				break;
		}
	}

	public static void performAction(int actionType)
	{
		performAction(actionType, GameCanvas.gameScr);
	}
}
