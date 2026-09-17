public class CountDownTicket
{
	public int timeCountDown;

	public long tickBeginCount;

	public string strInfo = "";

	public sbyte typeTime;

	public int valueLeft;

	public int valueright;

	public void setCountDown(int time)
	{
		timeCountDown = time;
		tickBeginCount = GameCanvas.timeNow;
	}

	public int CheckUpdate()
	{
		if (timeCountDown > 0)
		{
			updateTimeCountDownTicket();
			if (timeCountDown > 0)
			{
				return 1;
			}
			return 0;
		}
		return -1;
	}

	public void updateTimeCountDownTicket()
	{
		if (timeCountDown > 0)
		{
			if (GameCanvas.timeNow - tickBeginCount > 2000)
			{
				short num = (short)((GameCanvas.timeNow - tickBeginCount) / 1000);
				timeCountDown -= num;
				tickBeginCount += num * 1000;
			}
			if (GameCanvas.timeNow - tickBeginCount > 1000)
			{
				timeCountDown--;
				tickBeginCount += 1000L;
			}
		}
	}

	public void paintCountDownTicket(mGraphics g, mFont f, int x, int y, int anchor)
	{
		int time = 0;
		if (timeCountDown > 0)
		{
			time = timeCountDown;
		}
		f.drawString(g, timeShow(time), x, y, anchor);
	}

	public void paintCountDownTicketHour(mGraphics g, mFont f, int x, int y, int anchor)
	{
		if (timeCountDown >= 3600)
		{
			string text = "";
			string text2 = "";
			int num = timeCountDown / 60;
			int num2 = num / 60;
			int num3 = num % 60;
			text = ((num2 >= 10) ? (num2.ToString() ?? "") : ("0" + num2));
			f.drawString(g, text, x, y, anchor);
			int width = f.getWidth(text);
			if (GameCanvas.gameTick % 25 < 20)
			{
				f.drawString(g, ":", x + width + 1, y, anchor);
			}
			text2 = ((num3 >= 10) ? (text2 + num3) : (text2 + "0" + num3));
			f.drawString(g, text2, x + width + 4, y, anchor);
		}
		else
		{
			f.drawString(g, timeShow(timeCountDown), x, y, anchor);
		}
	}

	public static string timeShow(int time)
	{
		string text = "";
		if (time >= 3600)
		{
			int num = time / 60;
			text += num / 60;
			text = ((GameCanvas.gameTick % 25 >= 20) ? (text + " ") : (text + ":"));
			if (num % 60 < 10)
			{
				return text + "0" + num % 60;
			}
			return text + num % 60;
		}
		text = ((time < 60) ? (text + "00") : ((time >= 600) ? (text + time / 60) : (text + "0" + time / 60)));
		if (time % 60 < 10)
		{
			return text + ":0" + time % 60;
		}
		return text + ":" + time % 60;
	}
}
