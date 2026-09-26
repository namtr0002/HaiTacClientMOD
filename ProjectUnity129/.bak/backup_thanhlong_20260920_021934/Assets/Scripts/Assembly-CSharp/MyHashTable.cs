using System.Collections;
using System.Collections.Generic;

public class MyHashTable : IEnumerable
{
	// Chuyển từ Hashtable (non-generic, boxing) sang Dictionary<object,object>
	// Giữ nguyên API để không break bất kỳ caller nào
	public Dictionary<object, object> h = new Dictionary<object, object>();

	public string linkImage = "";
	public sbyte typeKey = -1;

	public MyHashTable()
	{
		h = new Dictionary<object, object>();
	}

	public MyHashTable(string strlink)
	{
		h = new Dictionary<object, object>();
		linkImage = strlink;
	}

	public MyHashTable(sbyte typeKey, string strlink)
	{
		h = new Dictionary<object, object>();
		this.typeKey = typeKey;
		linkImage = strlink;
	}

	public object get(object k)
	{
		h.TryGetValue(k, out object val);
		return val;
	}

	public void clear()
	{
		h.Clear();
	}

	public IDictionaryEnumerator GetEnumerator()
	{
		// Cast qua IDictionary để lấy IDictionaryEnumerator — Dictionary<T,T> implement IDictionary
		return ((System.Collections.IDictionary)h).GetEnumerator();
	}

	IEnumerator IEnumerable.GetEnumerator()
	{
		return ((IDictionary)h).GetEnumerator();
	}

	public int size()
	{
		return h.Count;
	}

	public void put(object k, object v)
	{
		h[k] = v;
	}

	public void remove(object k)
	{
		h.Remove(k);
	}

	public void Remove(string key)
	{
		h.Remove(key);
	}

	public bool containsKey(object key)
	{
		return h.ContainsKey(key);
	}

	public class MyIterator
	{
		private IEnumerator<object> iter;
		private bool hasMore = false;
		public MyIterator(IEnumerable<object> keys)
		{
			iter = keys.GetEnumerator();
			hasMore = iter.MoveNext();
		}
		public bool hasNext() => hasMore;
		public object next()
		{
			object current = iter.Current;
			hasMore = iter.MoveNext();
			return current;
		}
	}

	public MyIterator keys()
	{
		return new MyIterator(h.Keys);
	}
}
