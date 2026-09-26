public class DataOutputStream
{
	private myWriter w = new myWriter();

	private ByteArrayOutputStream bos;

	public DataOutputStream(ByteArrayOutputStream bos)
	{
		this.bos = bos;
	}

	public DataOutputStream()
	{
	}

	public void writeShort(short i)
	{
		w.writeShort(i);
		bos.arr = w.getData();
	}

	public void writeInt(int i)
	{
		w.writeInt(i);
		bos.arr = w.getData();
	}

	public void write(sbyte[] data)
	{
		w.writeSByte(data);
		bos.arr = w.getData();
	}

	public sbyte[] toByteArray()
	{
		return w.getData();
	}

	public void close()
	{
		w.Close();
	}

	public void writeByte(sbyte b)
	{
		w.writeByte(b);
		bos.arr = w.getData();
	}

	public void writeUTF(string name)
	{
		w.writeUTF(name);
		bos.arr = w.getData();
	}

	public void writeBoolean(bool b)
	{
		w.writeBoolean(b);
		bos.arr = w.getData();
	}

	public void writeLong(long l)
	{
		for (int i = 56; i >= 0; i -= 8)
		{
			writeByte((sbyte)(l >> i));
		}
	}
}
