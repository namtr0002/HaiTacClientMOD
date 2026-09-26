public class DelaySkill
{
	public sbyte typeSkill;

	public long timebegin;

	public int value;

	public int limit;

	public void paint(mGraphics g, int x, int y, int w)
	{
		FrameImage frameImage = AvMain.fraDelay;
		if (w > 32)
		{
			w = 32;
		}
		if (w <= 24)
		{
			frameImage = AvMain.fraDelay2;
		}
		if (limit <= 0)
		{
			return;
		}
		if (value > 0)
		{
			int num = 4 - value / (limit / 5);
			if (num >= 0 && num <= 5)
			{
				g.drawRegion(frameImage.imgFrame, frameImage.frameWidth / 2 - w / 2, frameImage.frameWidth / 2 - w / 2 + num * frameImage.frameHeight, w, w, 0, x, y, 0);
			}
			int num2 = value / 1000;
			string text = ((num2 != 0) ? (num2.ToString() ?? "") : ("0." + value % 1000 / 100));
			paintCooldownText(g, mFont.tahoma_7_white, text, x, y, w);
		}
		else if (value > -150)
		{
			g.setColor(15658700);
			g.fillRoundRect(x + 1, y + 1, w - 2, w - 2, 4, 4);
		}
	}

	public void paintOnlytime(mGraphics g, int x, int y, int w, sbyte color)
	{
		if (value <= 0)
		{
			return;
		}
		int num = value / 1000;
		string text = ((num != 0) ? (num.ToString() ?? "") : ("0." + value % 1000 / 100));
		paintCooldownText(g, AvMain.setTextColor(color), text, x, y, w);
	}

	public static void paintCooldownText(mGraphics g, mFont font, string text, int x, int y, int w)
	{
		if (g == null || font == null || string.IsNullOrEmpty(text) || w <= 0)
		{
			return;
		}
		int pad = 1;
		int boxX = x + pad;
		int boxY = y + pad;
		int boxW = w - pad * 2;
		int boxH = w - pad * 2;
		if (boxW <= 0 || boxH <= 0)
		{
			boxW = w;
			boxH = w;
			boxX = x;
			boxY = y;
		}

		int textW = font.getWidth(text);
		int textY = y + w / 2 - 5;

		if (textW <= boxW)
		{
			font.drawString(g, text, x + w / 2, textY, 2);
		}
		else
		{
			int maxScroll = textW - boxW;
			int speedTicks = 2;
			int pauseTicks = 16;
			int scrollTicks = maxScroll * speedTicks;
			int totalCycle = pauseTicks * 2 + scrollTicks * 2;

			int tick = (int)(GameCanvas.gameTick % totalCycle);
			if (tick < 0)
			{
				tick += totalCycle;
			}
			int offset;
			if (tick < pauseTicks)
			{
				offset = 0;
			}
			else if (tick < pauseTicks + scrollTicks)
			{
				offset = (tick - pauseTicks) / speedTicks;
			}
			else if (tick < pauseTicks * 2 + scrollTicks)
			{
				offset = maxScroll;
			}
			else
			{
				offset = maxScroll - (tick - (pauseTicks * 2 + scrollTicks)) / speedTicks;
			}

			if (offset < 0) offset = 0;
			if (offset > maxScroll) offset = maxScroll;

			mGraphics.ClipState oldClip = g.getClipState();
			g.setClip(boxX, boxY, boxW, boxH);
			font.drawString(g, text, boxX - offset, textY, 0);
			g.setClipState(oldClip);
		}
	}

	public bool isCoolDown()
	{
		if (value > 0)
		{
			return false;
		}
		return true;
	}

	public static DelaySkill getDelay(int index)
	{
		DelaySkill delaySkill = (DelaySkill)Player.delaySkill.get(index.ToString() ?? "");
		if (delaySkill == null)
		{
			delaySkill = new DelaySkill();
			delaySkill.value = -150;
			delaySkill.limit = 0;
			delaySkill.timebegin = GameCanvas.timeNow;
			Player.delaySkill.put(index.ToString() ?? "", delaySkill);
		}
		return delaySkill;
	}
}
