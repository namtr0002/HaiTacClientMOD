using System;

public class UILayoutEngine
{
	public const int ANCHOR_TOP_LEFT = 0;
	public const int ANCHOR_TOP_CENTER = 1;
	public const int ANCHOR_TOP_RIGHT = 2;
	public const int ANCHOR_CENTER = 3;
	public const int ANCHOR_BOTTOM_LEFT = 4;
	public const int ANCHOR_BOTTOM_CENTER = 5;
	public const int ANCHOR_BOTTOM_RIGHT = 6;

	public static bool isCompactMode()
	{
		return MotherCanvas.w < 360 || MotherCanvas.h < 280;
	}

	public static bool isWidescreen()
	{
		return MotherCanvas.w >= 480;
	}

	public static int getSafeLeft()
	{
		if (GameCanvas.isTaiTho)
		{
			return 24;
		}
		return 6;
	}

	public static int getSafeRight()
	{
		if (GameCanvas.isTaiTho)
		{
			return MotherCanvas.w - 24;
		}
		return MotherCanvas.w - 6;
	}

	public static int getAnchorX(int anchor, int elementWidth, int marginX)
	{
		int safeL = getSafeLeft();
		int safeR = getSafeRight();
		switch (anchor)
		{
			case ANCHOR_TOP_LEFT:
			case ANCHOR_BOTTOM_LEFT:
				return safeL + marginX;
			case ANCHOR_TOP_CENTER:
			case ANCHOR_CENTER:
			case ANCHOR_BOTTOM_CENTER:
				return (MotherCanvas.w - elementWidth) / 2 + marginX;
			case ANCHOR_TOP_RIGHT:
			case ANCHOR_BOTTOM_RIGHT:
				return safeR - elementWidth - marginX;
			default:
				return marginX;
		}
	}

	public static int getAnchorY(int anchor, int elementHeight, int marginY)
	{
		switch (anchor)
		{
			case ANCHOR_TOP_LEFT:
			case ANCHOR_TOP_CENTER:
			case ANCHOR_TOP_RIGHT:
				return marginY;
			case ANCHOR_CENTER:
				return (MotherCanvas.h - elementHeight) / 2 + marginY;
			case ANCHOR_BOTTOM_LEFT:
			case ANCHOR_BOTTOM_CENTER:
			case ANCHOR_BOTTOM_RIGHT:
				return MotherCanvas.h - elementHeight - marginY;
			default:
				return marginY;
		}
	}

	public static int clamp(int val, int min, int max)
	{
		if (val < min) return min;
		if (val > max) return max;
		return val;
	}

	public static int getMasterWidth(int totalW)
	{
		return (totalW * 38) / 100;
	}

	public static int getDetailWidth(int totalW)
	{
		return totalW - getMasterWidth(totalW) - 8;
	}
}
