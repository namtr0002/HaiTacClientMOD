using System;
using System.Timers;

public class AutoQuest
{
	public static bool AC_AUTO_QUEST = false;

	public static sbyte STATUS_AQ = 0;

	public const sbyte STATUS_BEGIN = 0;

	public const sbyte STATUS_CHAT = 1;

	public const sbyte STATUS_CURRENT = 2;

	public const sbyte STATUS_MOVE = 3;

	public const sbyte STATUS_WAIT = 4;

	private static bool atNPC = false;

	private static bool atCenter = false;

	private static bool isMove = true;

	private static MainQuest mAutoQuest;

	public static MainObject objNPC;

	public static mVector vecAutoQuest;

	public static sbyte numFireBoss = 0;

	public static sbyte iPhanTu = 2;

	public static short idMonster = -1;

	public static short idBoss = -1;

	private static Timer timerRun1;

	private static Timer timerRun2;

	private static Timer timerRun3;

	private static Timer timerRun4;

	private static Timer timerRun5;

	private static Timer timerRun6;

	private static Timer timerRunEnd;

	private static int endTimerRun = 3000;

	public static void changeAcAutoQuest()
	{
		if (!AC_AUTO_QUEST)
		{
			if (GameScreen.player.Action == 4 || GameScreen.player.Hp <= 0)
			{
				AC_AUTO_QUEST = false;
				return;
			}
			AC_AUTO_QUEST = true;
			begin();
		}
		else
		{
			AC_AUTO_QUEST = false;
			STATUS_AQ = 0;
		}
	}

	public static void continueAutoQuest()
	{
		if (!AC_AUTO_QUEST)
		{
			if (GameScreen.player.Action == 4 || GameScreen.player.Hp <= 0)
			{
				AC_AUTO_QUEST = false;
				return;
			}
			AC_AUTO_QUEST = true;
			STATUS_AQ = 3;
			GameScreen.player.isMoveNor = true;
			GameScreen.player.Action = 2;
			Player.setStart_EndAutoFire(isAu: false);
			movePlayerToCenter();
		}
		else
		{
			AC_AUTO_QUEST = false;
		}
	}

	private static void begin()
	{
		try
		{
			STATUS_AQ = 0;
			atNPC = false;
			getNPC();
			if (objNPC == null)
			{
				AC_AUTO_QUEST = false;
				return;
			}
			if (!checkQuestLoopNPC())
			{
				AC_AUTO_QUEST = false;
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.autoRepeatQuest);
				return;
			}
			if (!checkBreak())
			{
				AC_AUTO_QUEST = false;
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notEnoughBread);
				return;
			}
			GameScreen.objFocus = objNPC;
			GameScreen.player.isMoveNor = true;
			GameScreen.player.Action = 2;
			Player.setStart_EndAutoFire(isAu: false);
			movePlayerToNPC();
		}
		catch (Exception)
		{
		}
	}

	public static void updateAutoQuest()
	{
		switch (STATUS_AQ)
		{
		case 0:
			moveNPC();
			break;
		case 1:
			chatQuest();
			break;
		case 2:
			checkQuestFinishAQ();
			break;
		case 3:
			moveCenter();
			break;
		case 4:
			break;
		}
	}

	private static void chatQuest()
	{
		try
		{
			resetAutoQuest();
			STATUS_AQ = 4;
			GameCanvas.menu.doCloseMenu();
			GameCanvas.menuCur.doCloseMenu();
			GameCanvas.end_Dialog();
			GameCanvas.end_Cur_Dialog();
			GameCanvas.clearAll();
			GameScreen.objFocus = objNPC;
			GameScreen.objFocus.Giaotiep();
			vecAutoQuest = objNPC.getListQuestNPC();
			mAutoQuest = (MainQuest)vecAutoQuest.elementAt(0);
			timerRun1 = new Timer(800.0);
			timerRun1.Elapsed += onTimerRun1;
			timerRun1.AutoReset = false;
			timerRun1.Start();
			timerRun2 = new Timer(1100.0);
			timerRun2.Elapsed += onTimerRun2;
			timerRun2.AutoReset = false;
			timerRun2.Start();
			if (checkQuestLoopNotInMap())
			{
				timerRun3 = new Timer(1400.0);
				timerRun3.Elapsed += onTimerRun3;
				timerRun3.AutoReset = false;
				timerRun3.Start();
				timerRun4 = new Timer(1800.0);
				timerRun4.Elapsed += onTimerRun4;
				timerRun4.AutoReset = false;
				timerRun4.Start();
				timerRun5 = new Timer(2200.0);
				timerRun5.Elapsed += onTimerRun5;
				timerRun5.AutoReset = false;
				timerRun5.Start();
				timerRun6 = new Timer(2800.0);
				timerRun6.Elapsed += onTimerRun6;
				timerRun6.AutoReset = false;
				timerRun6.Start();
				endTimerRun = 3000;
			}
			else if (mAutoQuest.statusQuest == 0)
			{
				timerRun3 = new Timer(1400.0);
				timerRun3.Elapsed += onTimerRun3;
				timerRun3.AutoReset = false;
				timerRun3.Start();
				timerRun5 = new Timer(1800.0);
				timerRun5.Elapsed += onTimerRun5;
				timerRun5.AutoReset = false;
				timerRun5.Start();
				timerRun6 = new Timer(2800.0);
				timerRun6.Elapsed += onTimerRun6;
				timerRun6.AutoReset = false;
				timerRun6.Start();
				endTimerRun = 3000;
			}
			else if (mAutoQuest.statusQuest == 1)
			{
				timerRun3 = new Timer(1400.0);
				timerRun3.Elapsed += onTimerRun3;
				timerRun3.AutoReset = false;
				timerRun3.Start();
				timerRun6 = new Timer(1800.0);
				timerRun6.Elapsed += onTimerRun6;
				timerRun6.AutoReset = false;
				timerRun6.Start();
				endTimerRun = 2000;
			}
			else if (mAutoQuest.statusQuest == 2)
			{
				timerRun3 = new Timer(1400.0);
				timerRun3.Elapsed += onTimerRun3;
				timerRun3.AutoReset = false;
				timerRun3.Start();
				timerRun4 = new Timer(1800.0);
				timerRun4.Elapsed += onTimerRun4;
				timerRun4.AutoReset = false;
				timerRun4.Start();
				timerRun6 = new Timer(2800.0);
				timerRun6.Elapsed += onTimerRun6;
				timerRun6.AutoReset = false;
				timerRun6.Start();
				endTimerRun = 3000;
				isMove = false;
			}
			timerRunEnd = new Timer(endTimerRun);
			timerRunEnd.Elapsed += onTimerRunEnd;
			timerRunEnd.AutoReset = false;
			timerRunEnd.Start();
		}
		catch (Exception)
		{
		}
	}

	public static void resetAutoQuest()
	{
		iPhanTu = 2;
		idBoss = -1;
		idMonster = -1;
		numFireBoss = 0;
		atCenter = false;
		isMove = true;
	}

	public static void moveNPC()
	{
		try
		{
			if (GameScreen.player.isMoveNor)
			{
				GameScreen.player.move_to_XY_Normal();
				if (GameScreen.player.posTransRoad != null)
				{
					GameScreen.player.posTransRoad = null;
				}
				if (CRes.abs(GameScreen.player.x - GameScreen.player.toX) < GameScreen.player.vMax && CRes.abs(GameScreen.player.y - GameScreen.player.toY) < GameScreen.player.vMax)
				{
					GameScreen.player.isMoveNor = false;
				}
				movePlayerToNPC();
			}
			else if (atNPC)
			{
				STATUS_AQ = 1;
			}
			else
			{
				AC_AUTO_QUEST = false;
				iCommand cmd = new iCommand(T.next, 67, GameCanvas.gameScr);
				GameCanvas.Start_Normal_DiaLog(T.nextNVL, cmd, isCmdClose: true);
			}
		}
		catch (Exception)
		{
		}
	}

	public static void moveCenter()
	{
		try
		{
			if (GameScreen.player.isMoveNor)
			{
				GameScreen.player.move_to_XY_Normal();
				if (GameScreen.player.posTransRoad != null)
				{
					GameScreen.player.posTransRoad = null;
				}
				if (CRes.abs(GameScreen.player.x - GameScreen.player.toX) < GameScreen.player.vMax && CRes.abs(GameScreen.player.y - GameScreen.player.toY) < GameScreen.player.vMax)
				{
					GameScreen.player.isMoveNor = false;
				}
				movePlayerToCenter();
			}
			else if (atCenter)
			{
				STATUS_AQ = 2;
				GameScreen.objFocus = getMonster();
				Player.objAutoFrist = getMonster();
				GameScreen.player.Action = 1;
				Player.setStart_EndAutoFire(isAu: true);
				Interface_Game.isAutoFireInterface = true;
				numFireBoss = 0;
			}
			else
			{
				AC_AUTO_QUEST = false;
				iCommand cmd = new iCommand(T.next, 69, GameCanvas.gameScr);
				GameCanvas.Start_Normal_DiaLog(T.nextNVL, cmd, isCmdClose: true);
			}
		}
		catch (Exception)
		{
		}
	}

	public static void checkFireBoss()
	{
		if (idBoss <= -1)
		{
			return;
		}
		numFireBoss++;
		if (numFireBoss > 2)
		{
			iPhanTu++;
			if (iPhanTu > 4)
			{
				iPhanTu = 1;
			}
			numFireBoss = 0;
			STATUS_AQ = 3;
			atCenter = false;
			GameScreen.player.isMoveNor = true;
			GameScreen.player.Action = 2;
			Player.setStart_EndAutoFire(isAu: false);
			movePlayerToCenter();
		}
	}

	public static void checkQuestFinishAQ()
	{
		if (isQuestFinish())
		{
			GameScreen.objFocus = objNPC;
			atNPC = false;
			GameScreen.player.isMoveNor = true;
			GameScreen.player.Action = 2;
			Player.setStart_EndAutoFire(isAu: false);
			STATUS_AQ = 0;
			movePlayerToNPC();
		}
	}

	public static bool isQuestFinish()
	{
		if (Player.vecQuest == null)
		{
			return false;
		}
		sbyte b = (sbyte)Player.vecQuest.size();
		for (sbyte b2 = 0; b2 < b; b2++)
		{
			MainQuest mainQuest = (MainQuest)Player.vecQuest.elementAt(b2);
			if (mainQuest.statusQuest == 2 && mainQuest.idNPC == objNPC.ID)
			{
				return true;
			}
		}
		return false;
	}

	public static void movePlayerToNPC()
	{
		int distance = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, objNPC.x, objNPC.y);
		GameScreen.player.toX = objNPC.x;
		GameScreen.player.toY = objNPC.y;
		if (distance < GameScreen.player.vMax * 2)
		{
			GameScreen.player.x = objNPC.x;
			GameScreen.player.y = objNPC.y;
		}
		else
		{
			GameScreen.player.isMoveNor = true;
		}
		if (distance < 15)
		{
			atNPC = true;
			GameScreen.player.isMoveNor = false;
		}
		else
		{
			atNPC = false;
			GameScreen.player.isMoveNor = true;
		}
	}

	public static void movePlayerToCenter()
	{
		int distance = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameCanvas.loadmap.limitW * iPhanTu / 4, GameCanvas.loadmap.maxHMap * 3 / 4);
		GameScreen.player.toX = GameCanvas.loadmap.limitW * iPhanTu / 4;
		GameScreen.player.toY = GameCanvas.loadmap.maxHMap * 3 / 4;
		if (distance < GameScreen.player.vMax)
		{
			GameScreen.player.x = GameCanvas.loadmap.limitW * iPhanTu / 4;
			GameScreen.player.y = GameCanvas.loadmap.maxHMap * 3 / 4;
		}
		else
		{
			GameScreen.player.isMoveNor = true;
		}
		if (distance < 15)
		{
			atCenter = true;
			GameScreen.player.isMoveNor = false;
		}
		else
		{
			atCenter = false;
			GameScreen.player.isMoveNor = true;
		}
	}

	private static MainObject getNPC()
	{
		int num = -1;
		short num2 = (short)GameScreen.vecPlayers.size();
		for (short num3 = 0; num3 < num2; num3++)
		{
			MainObject mainObject = (MainObject)GameScreen.vecPlayers.elementAt(num3);
			if (mainObject.typeObject == 2 && mainObject.typeQuest > 0)
			{
				num = num3;
				break;
			}
		}
		if (num > -1)
		{
			objNPC = (MainObject)GameScreen.vecPlayers.elementAt(num);
		}
		return objNPC;
	}

	public static MainObject getMonster()
	{
		int num = Player.wFocus * 3 / 2;
		MainObject result = null;
		short num2 = (short)GameScreen.vecPlayers.size();
		for (short num3 = 0; num3 < num2; num3++)
		{
			MainObject mainObject = (MainObject)GameScreen.vecPlayers.elementAt(num3);
			if (mainObject.typeObject == 1)
			{
				MainMonster mainMonster = (MainMonster)GameScreen.vecPlayers.elementAt(num3);
				if (idMonster > -1 && mainMonster.idCatMonster == idMonster)
				{
					int distance = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, mainMonster.x, mainMonster.y);
					if (distance < num)
					{
						num = distance;
						result = mainObject;
					}
				}
				if (idBoss > -1 && mainMonster.idCatMonster == idBoss)
				{
					result = mainObject;
					break;
				}
			}
		}
		return result;
	}

	public static MainObject getMonsterByIndex(short index)
	{
		MainObject result = null;
		MainMonster mainMonster = (MainMonster)GameScreen.vecPlayers.elementAt(index);
		if (idMonster > -1 && mainMonster.idCatMonster == idMonster)
		{
			if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, mainMonster.x, mainMonster.y) < Player.wFocus * 3 / 2)
			{
				result = (MainObject)GameScreen.vecPlayers.elementAt(index);
			}
		}
		else if (idBoss > -1 && mainMonster.idCatMonster == idBoss)
		{
			result = (MainObject)GameScreen.vecPlayers.elementAt(index);
		}
		return result;
	}

	private static void setIdMonBoss()
	{
		for (int i = 0; i < Player.vecQuest.size(); i++)
		{
			MainQuest mainQuest = (MainQuest)Player.vecQuest.elementAt(i);
			if (mAutoQuest.ID == mainQuest.ID)
			{
				mAutoQuest = mainQuest;
			}
		}
		for (int j = 0; j < mAutoQuest.vecTypeQuest.size(); j++)
		{
			DataQuest dataQuest = (DataQuest)mAutoQuest.vecTypeQuest.elementAt(j);
			if (dataQuest.numCur < dataQuest.numMax)
			{
				if (dataQuest.numMax == 1)
				{
					idBoss = dataQuest.IDItem;
				}
				else
				{
					idMonster = dataQuest.IDItem;
				}
			}
		}
	}

	private static bool checkQuestLoopNPC()
	{
		if (Player.vecQuest == null)
		{
			return false;
		}
		sbyte b = (sbyte)Player.vecQuest.size();
		for (sbyte b2 = 0; b2 < b; b2++)
		{
			MainQuest mainQuest = (MainQuest)Player.vecQuest.elementAt(b2);
			if (mainQuest.typeMainSub == 2 && mainQuest.idNPC == objNPC.ID)
			{
				return true;
			}
		}
		return false;
	}

	private static bool checkQuestLoopNotInMap()
	{
		if (Player.vecQuest == null)
		{
			return false;
		}
		sbyte b = (sbyte)Player.vecQuest.size();
		for (sbyte b2 = 0; b2 < b; b2++)
		{
			MainQuest mainQuest = (MainQuest)Player.vecQuest.elementAt(b2);
			if (mainQuest.typeMainSub == 2 && mainQuest.idNPC != objNPC.ID)
			{
				return true;
			}
		}
		return false;
	}

	private static bool checkQuestLoopCurrentInMap()
	{
		if (Player.vecQuest == null)
		{
			return false;
		}
		sbyte b = (sbyte)Player.vecQuest.size();
		for (sbyte b2 = 0; b2 < b; b2++)
		{
			MainQuest mainQuest = (MainQuest)Player.vecQuest.elementAt(b2);
			if (mainQuest.typeMainSub == 2 && mainQuest.idNPC == objNPC.ID && mainQuest.statusQuest == 1)
			{
				return true;
			}
		}
		return false;
	}

	private static bool checkBreak()
	{
		if (Player.ticket > 2)
		{
			return true;
		}
		if (checkQuestLoopCurrentInMap())
		{
			return true;
		}
		return false;
	}

	private static void onTimerRun1(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.keyMyHold[6] = true;
			GameCanvas.keyMyPressed[6] = true;
			timerRun1.Dispose();
		}
	}

	private static void onTimerRun2(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.keyMyHold[5] = true;
			GameCanvas.keyMyPressed[5] = true;
			timerRun2.Dispose();
		}
	}

	private static void onTimerRun3(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.keyMyHold[5] = true;
			GameCanvas.keyMyPressed[5] = true;
			timerRun3.Dispose();
		}
	}

	private static void onTimerRun4(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.keyMyHold[5] = true;
			GameCanvas.keyMyPressed[5] = true;
			timerRun4.Dispose();
		}
	}

	private static void onTimerRun5(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.keyMyHold[5] = true;
			GameCanvas.keyMyPressed[5] = true;
			timerRun5.Dispose();
		}
	}

	private static void onTimerRun6(object sender, ElapsedEventArgs e)
	{
		if (AC_AUTO_QUEST)
		{
			GameCanvas.clearAll();
			GameCanvas.end_Dialog();
			GameCanvas.end_Cur_Dialog();
			GameCanvas.menuCur.doCloseMenu();
			GameCanvas.menu.doCloseMenu();
			timerRun6.Dispose();
		}
	}

	private static void onTimerRunEnd(object sender, ElapsedEventArgs e)
	{
		if (!AC_AUTO_QUEST)
		{
			return;
		}
		if (isMove)
		{
			setIdMonBoss();
			STATUS_AQ = 3;
			atCenter = false;
			GameScreen.player.isMoveNor = true;
			GameScreen.player.Action = 2;
			Player.setStart_EndAutoFire(isAu: false);
			movePlayerToCenter();
		}
		else
		{
			if (!checkBreak())
			{
				AC_AUTO_QUEST = false;
				GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notEnoughBread);
				return;
			}
			STATUS_AQ = 1;
		}
		timerRunEnd.Dispose();
	}
}
