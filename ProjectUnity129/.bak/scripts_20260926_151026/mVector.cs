using System.Collections.Generic;

public class mVector
{
	private List<object> a;

	public mVector()
	{
		a = new List<object>();
	}

	public mVector(string s)
	{
		a = new List<object>();
	}

	public mVector(List<object> a)
	{
		this.a = a ?? new List<object>();
	}

	public void addElement(object o)
	{
		if (a == null) a = new List<object>();
		a.Add(o);
	}

	public bool contains(object o)
	{
		if (a == null) return false;
		return a.Contains(o);
	}

	public int size()
	{
		if (a == null) return 0;
		return a.Count;
	}

	public object elementAt(int index)
	{
		if (a == null) return null;
		if (index > -1 && index < a.Count)
		{
			return a[index];
		}
		return null;
	}

	public void set(int index, object obj)
	{
		if (a == null) return;
		if (index > -1 && index < a.Count)
		{
			a[index] = obj;
		}
	}

	public void setElementAt(object obj, int index)
	{
		if (a == null) return;
		if (index > -1 && index < a.Count)
		{
			a[index] = obj;
		}
	}

	public int indexOf(object o)
	{
		if (a == null) return -1;
		return a.IndexOf(o);
	}

	public int IndexOf(object o)
	{
		if (a == null) return -1;
		return a.IndexOf(o);
	}

	public void removeElementAt(int index)
	{
		if (a == null) return;
		if (index > -1 && index < a.Count)
		{
			a.RemoveAt(index);
		}
	}

	public void removeElement(object o)
	{
		if (a == null) return;
		a.Remove(o);
	}

	public void removeAllElements()
	{
		if (a == null) return;
		a.Clear();
	}

	public void insertElementAt(object o, int i)
	{
		if (a == null) a = new List<object>();
		if (i < 0) i = 0;
		if (i > a.Count) i = a.Count;
		a.Insert(i, o);
	}

	public object firstElement()
	{
		if (a == null) return null;
		if (a.Count > 0)
		{
			return a[0];
		}
		return null;
	}

	public object lastElement()
	{
		if (a == null) return null;
		if (a.Count > 0)
		{
			return a[a.Count - 1];
		}
		return null;
	}
}
