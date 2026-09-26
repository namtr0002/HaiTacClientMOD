using System;

public class Math
{
	public const double PI = System.Math.PI;

	public static int abs(int i)
	{
		if (i <= 0)
		{
			return -i;
		}
		return i;
	}

	public static int min(int x, int y)
	{
		if (x >= y)
		{
			return y;
		}
		return x;
	}

	public static int max(int x, int y)
	{
		if (x <= y)
		{
			return y;
		}
		return x;
	}

	public static int pow(int data, int x)
	{
		int num = 1;
		for (int i = 0; i < x; i++)
		{
			num *= data;
		}
		return num;
	}

	public static int Abs(int i) => abs(i);
	public static float Abs(float f) => System.Math.Abs(f);
	public static double Abs(double d) => System.Math.Abs(d);
	public static int Min(int x, int y) => min(x, y);
	public static float Min(float x, float y) => System.Math.Min(x, y);
	public static double Min(double x, double y) => System.Math.Min(x, y);
	public static long Min(long x, long y) => System.Math.Min(x, y);
	public static int Max(int x, int y) => max(x, y);
	public static float Max(float x, float y) => System.Math.Max(x, y);
	public static double Max(double x, double y) => System.Math.Max(x, y);
	public static long Max(long x, long y) => System.Math.Max(x, y);
}
