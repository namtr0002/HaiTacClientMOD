public class Point
{
	public int x;

	public int y;

	public int g;

	public int v;

	public int w;

	public int h;

	public int color;

	public int limitY;

	public int vx;

	public int vy;

	public int x2;

	public int y2;

	public int x0;

	public int y0;

	public int dir;

	public int dis;

	public int f;

	public int fRe;

	public int frame;

	public int maxframe;

	public int fSmall;

	public int levelPaint;

	public int subType;

	public mVector vecEffPoint;

	public string name;

	public bool isRemove;

	public bool isSmall;

	public static FrameImage[] FraEffInMap;

	public FrameImage fraImgEff;

	public MainObject obj;

	public Point()
	{
	}

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public static Point obtain(int x = 0, int y = 0)
	{
		return PointPool.obtain(x, y);
	}

	public static void recycle(Point p)
	{
		PointPool.recycle(p);
	}

	public static void recycleAll(mVector vec)
	{
		PointPool.recycleAll(vec);
	}

	public void reset()
	{
		x = y = g = v = w = h = color = limitY = vx = vy = x2 = y2 = x0 = y0 = dir = dis = f = fRe = frame = maxframe = fSmall = levelPaint = subType = 0;
		name = null;
		isRemove = false;
		isSmall = false;
		fraImgEff = null;
		obj = null;
		if (vecEffPoint != null) vecEffPoint.removeAllElements();
	}

	public void update()
	{
		f++;
		x += vx;
		y += vy;
	}

	public void paint(mGraphics g)
	{
		if (!isRemove)
		{
			int num = 0;
			if (isSmall && f >= fSmall)
			{
				num = 1;
			}
			FraEffInMap[color].drawFrame(frame / 2 + num, x, y, dis, 3, g);
		}
	}

	public void updateInMap()
	{
		f++;
		if (maxframe > 1)
		{
			frame++;
			if (frame / 2 >= maxframe)
			{
				frame = 0;
			}
		}
		if (f >= fRe)
		{
			isRemove = true;
		}
	}

	public void updateObj()
	{
		if (obj != null && obj.Action != 4 && !obj.returnAction())
		{
			x = obj.x;
			y = obj.y;
		}
	}
}

public class PointPool
{
	private static System.Collections.Generic.Stack<Point> pool = new System.Collections.Generic.Stack<Point>(128);

	public static Point obtain(int x = 0, int y = 0)
	{
		lock (pool)
		{
			if (pool.Count > 0)
			{
				Point p = pool.Pop();
				p.reset();
				p.x = x;
				p.y = y;
				return p;
			}
		}
		return new Point(x, y);
	}

	public static void recycle(Point p)
	{
		if (p == null) return;
		lock (pool)
		{
			if (pool.Count < 256)
			{
				p.reset();
				pool.Push(p);
			}
		}
	}

	public static void recycleAll(mVector vec)
	{
		if (vec == null) return;
		for (int i = 0; i < vec.size(); i++)
		{
			Point p = vec.elementAt(i) as Point;
			if (p != null) recycle(p);
		}
		vec.removeAllElements();
	}
}
