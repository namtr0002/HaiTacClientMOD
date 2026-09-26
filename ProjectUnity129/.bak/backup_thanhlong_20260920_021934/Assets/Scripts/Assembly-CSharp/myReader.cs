using System;
using System.Text;
using UnityEngine;

public class myReader
{
	public sbyte[] buffer;

	private int posRead;

	private int posMark;

	private static string fileName;

	private static int status;

	public myReader()
	{
	}

	public myReader(sbyte[] data)
	{
		buffer = data;
	}

	public myReader(string filename)
	{
		TextAsset textAsset = (TextAsset)Resources.Load(filename, typeof(TextAsset));
		buffer = mSystem.convertToSbyte(textAsset.bytes);
	}

	public sbyte readSByte()
	{
		if (posRead < buffer.Length)
		{
			return buffer[posRead++];
		}
		posRead = buffer.Length;
		throw new Exception(" loi doc sbyte eof ");
	}

	public sbyte readsbyte()
	{
		return readSByte();
	}

	public sbyte readByte()
	{
		return readSByte();
	}

	public void mark(int readlimit)
	{
		posMark = posRead;
	}

	public void reset()
	{
		posRead = posMark;
	}

	public byte readUnsignedByte()
	{
		return convertSbyteToByte(readSByte());
	}

	public short readShort()
	{
		short num = 0;
		for (int i = 0; i < 2; i++)
		{
			num <<= 8;
			num |= (short)(0xFF & buffer[posRead++]);
		}
		return num;
	}

	public ushort readUnsignedShort()
	{
		ushort num = 0;
		for (int i = 0; i < 2; i++)
		{
			num <<= 8;
			num |= (ushort)(0xFF & buffer[posRead++]);
		}
		return num;
	}

	public int readInt()
	{
		int num = 0;
		for (int i = 0; i < 4; i++)
		{
			num <<= 8;
			num |= 0xFF & buffer[posRead++];
		}
		return num;
	}

	public long readLong()
	{
		long num = 0L;
		for (int i = 0; i < 8; i++)
		{
			num <<= 8;
			num |= (long)(0xFF & buffer[posRead++]);
		}
		return num;
	}

	public bool readBool()
	{
		if (readSByte() <= 0)
		{
			return false;
		}
		return true;
	}

	public bool readBoolean()
	{
		if (readSByte() <= 0)
		{
			return false;
		}
		return true;
	}

	public string readString()
	{
		short num = readShort();
		if (num <= 0)
		{
			return string.Empty;
		}
		byte[] array = new byte[num];
		Buffer.BlockCopy(buffer, posRead, array, 0, num);
		posRead += num;
		return Encoding.UTF8.GetString(array);
	}

	public string readStringUTF()
	{
		return readString();
	}

	public string readUTF()
	{
		return readString();
	}

	public int read()
	{
		if (posRead < buffer.Length)
		{
			return readSByte();
		}
		return -1;
	}

	public int read(ref sbyte[] data)
	{
		if (data == null)
		{
			return 0;
		}
		int num = 0;
		for (int i = 0; i < data.Length; i++)
		{
			data[i] = readSByte();
			if (posRead > buffer.Length)
			{
				return -1;
			}
			num++;
		}
		return num;
	}

	public void readFully(ref sbyte[] data)
	{
		if (data != null && data.Length + posRead <= buffer.Length)
		{
			for (int i = 0; i < data.Length; i++)
			{
				data[i] = readSByte();
			}
		}
	}

	public int available()
	{
		return buffer.Length - posRead;
	}

	public static byte convertSbyteToByte(sbyte var)
	{
		if (var > 0)
		{
			return (byte)var;
		}
		return (byte)(var + 256);
	}

	public static byte[] convertSbyteToByte(sbyte[] var)
	{
		if (var == null) return null;
		byte[] array = new byte[var.Length];
		Buffer.BlockCopy(var, 0, array, 0, var.Length);
		return array;
	}

	public void Close()
	{
		buffer = null;
	}

	public void close()
	{
		buffer = null;
	}

	public void read(ref sbyte[] data, int arg1, int arg2)
	{
		if (data == null)
		{
			return;
		}
		for (int i = 0; i < arg2; i++)
		{
			data[i + arg1] = readSByte();
			if (posRead > buffer.Length)
			{
				break;
			}
		}
	}
}
