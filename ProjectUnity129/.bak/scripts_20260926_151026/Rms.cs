using System;
using System.IO;
using System.Threading;
using UnityEngine;

public class Rms
{
	public static int status;

	public static sbyte[] data;

	public static string filename;

	private const int INTERVAL = 5;

	private const int MAXTIME = 500;

	public static void saveRMS(string filename, sbyte[] data)
	{
		__saveRMS("x" + mGraphics.zoomLevel + filename, data);
	}

	public static int lastLoadedZoom = 0;

	public static sbyte[] loadRMS(string filename)
	{
		lastLoadedZoom = mGraphics.zoomLevel;
		sbyte[] array = __loadRMS("x" + mGraphics.zoomLevel + filename);
		if (array != null && array.Length > 0)
		{
			return array;
		}
		int[] fallbackZooms = new int[] { 3, 4, 2, 1 };
		for (int i = 0; i < fallbackZooms.Length; i++)
		{
			if (fallbackZooms[i] != mGraphics.zoomLevel)
			{
				array = __loadRMS("x" + fallbackZooms[i] + filename);
				if (array != null && array.Length > 0)
				{
					lastLoadedZoom = fallbackZooms[i];
					return array;
				}
			}
		}
		lastLoadedZoom = 0;
		return null;
	}

	public static string loadRMSString(string fileName)
	{
		sbyte[] array = loadRMS(fileName);
		if (array == null)
		{
			return null;
		}
		DataInputStream dataInputStream = new DataInputStream(array);
		try
		{
			string result = dataInputStream.readUTF();
			dataInputStream.close();
			return result;
		}
		catch (Exception ex)
		{
			Cout.println(ex.StackTrace);
		}
		return null;
	}

	public static byte[] convertSbyteToByte(sbyte[] var)
	{
		if (var == null) return null;
		byte[] array = new byte[var.Length];
		Buffer.BlockCopy(var, 0, array, 0, var.Length);
		return array;
	}

	public static void saveRMSString(string filename, string data)
	{
		DataOutputStream dataOutputStream = new DataOutputStream();
		try
		{
			dataOutputStream.writeUTF(data);
			saveRMS(filename, dataOutputStream.toByteArray());
			dataOutputStream.close();
		}
		catch (Exception ex)
		{
			Cout.println(ex.StackTrace);
		}
	}

	private static void _saveRMS(string filename, sbyte[] data)
	{
		if (status != 0)
		{
			Debug.LogError("Cannot save RMS " + filename + " because current is saving " + Rms.filename);
			return;
		}
		Rms.filename = filename;
		Rms.data = data;
		status = 2;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Debug.LogError("TOO LONG TO SAVE RMS " + filename);
		}
	}

	private static sbyte[] _loadRMS(string filename)
	{
		if (status != 0)
		{
			Debug.LogError("Cannot load RMS " + filename + " because current is loading " + Rms.filename);
			return null;
		}
		Rms.filename = filename;
		data = null;
		status = 3;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Debug.LogError("TOO LONG TO LOAD RMS " + filename);
		}
		return data;
	}

	public static void update()
	{
		if (status == 2)
		{
			status = 1;
			__saveRMS(filename, data);
			status = 0;
		}
		else if (status == 3)
		{
			status = 1;
			data = __loadRMS(filename);
			status = 0;
		}
	}

	public static int loadRMSInt(string file)
	{
		sbyte[] array = loadRMS(file);
		if (array != null)
		{
			return array[0];
		}
		return -1;
	}

	public static void saveRMSInt(string file, int x)
	{
		try
		{
			saveRMS(file, new sbyte[1] { (sbyte)x });
		}
		catch (Exception)
		{
		}
	}

	public static string path = "";

	public static string GetiPhoneDocumentsPath()
	{
		if (string.IsNullOrEmpty(path))
		{
			path = Application.persistentDataPath;
		}
		return path;
	}

	private static void __saveRMS(string filename, sbyte[] data)
	{
		string text = GetiPhoneDocumentsPath() + "/" + filename;
		FileStream fileStream = new FileStream(text, FileMode.Create);
		fileStream.Write(ArrayCast.cast(data), 0, data.Length);
		fileStream.Flush();
		fileStream.Close();
		Main.setBackupIcloud(text);
	}

	private static sbyte[] __loadRMS(string filename)
	{
		try
		{
			FileStream fileStream = new FileStream(GetiPhoneDocumentsPath() + "/" + filename, FileMode.Open);
			byte[] array = new byte[fileStream.Length];
			fileStream.Read(array, 0, array.Length);
			fileStream.Close();
			ArrayCast.cast(array);
			return ArrayCast.cast(array);
		}
		catch (Exception)
		{
			return null;
		}
	}

	public static void clearAll()
	{
		Debug.LogWarning("ALL RMS CLEAR");
		PlayerPrefs.DeleteAll();
		FileInfo[] files = new DirectoryInfo(GetiPhoneDocumentsPath() + "/").GetFiles();
		for (int i = 0; i < files.Length; i++)
		{
			files[i].Delete();
		}
	}

	public static void DeleteStorage(string path)
	{
		try
		{
			File.Delete(GetiPhoneDocumentsPath() + "/" + path);
		}
		catch (Exception)
		{
		}
	}

	public static string ByteArrayToString(byte[] ba)
	{
		return BitConverter.ToString(ba).Replace("-", "");
	}

	public static byte[] StringToByteArray(string hex)
	{
		int length = hex.Length;
		byte[] array = new byte[length / 2];
		for (int i = 0; i < length; i += 2)
		{
			array[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
		}
		return array;
	}

	public static void deleteRecord(string name)
	{
		try
		{
			PlayerPrefs.DeleteKey(name);
		}
		catch (Exception ex)
		{
			Cout.println("loi xoa RMS --------------------------" + ex.ToString());
		}
	}

	public static void clearRMS()
	{
		deleteRecord("data");
		deleteRecord("dataVersion");
		deleteRecord("map");
		deleteRecord("mapVersion");
		deleteRecord("skill");
		deleteRecord("killVersion");
		deleteRecord("item");
		deleteRecord("itemVersion");
	}

	public static void saveIP(string strID)
	{
		saveRMSString("NRIPlink", strID);
	}

	public static string loadIP()
	{
		string text = loadRMSString("NRIPlink");
		if (text == null)
		{
			return null;
		}
		return text;
	}

	public static int loadRMSInt2(string file)
	{
		sbyte[] array = loadRMS2(file);
		if (array != null)
		{
			return array[0];
		}
		return -1;
	}

	public static void saveRMSInt2(string file, int x)
	{
		try
		{
			saveRMS2(file, new sbyte[1] { (sbyte)x });
		}
		catch (Exception)
		{
		}
	}

	public static void saveRMS2(string filename, sbyte[] data)
	{
		__saveRMS(filename, data);
	}

	public static sbyte[] loadRMS2(string filename)
	{
		return __loadRMS(filename);
	}

	public static void saveMailCache(string playerName, mVector list)
	{
		if (string.IsNullOrEmpty(playerName) || list == null) return;
		try
		{
			DataOutputStream dos = new DataOutputStream();
			
			int count = list.size();
			if (count > 100) count = 100;
			dos.writeShort((short)count);
			
			for (int i = 0; i < count; i++)
			{
				ChatDetail cd = (ChatDetail)list.elementAt(i);
				if (cd == null) continue;
				
				dos.writeBoolean(cd.isInvite);
				if (cd.isInvite)
				{
					dos.writeInt(cd.inviteId);
					dos.writeByte(cd.typeInvite);
					dos.writeUTF(cd.inviteName != null ? cd.inviteName : "");
					dos.writeUTF(cd.inviteInfo != null ? cd.inviteInfo : "");
					dos.writeInt(cd.priceFight);
					dos.writeInt(cd.typeFight);
					dos.writeUTF(cd.contentRaw != null ? cd.contentRaw : "");
					dos.writeLong(cd.receivedTime);
				}
				else
				{
					dos.writeInt(cd.mailId);
					dos.writeUTF(cd.name != null ? cd.name : "");
					dos.writeUTF(cd.mailTitle != null ? cd.mailTitle : "");
					dos.writeUTF(cd.contentRaw != null ? cd.contentRaw : "");
					dos.writeBoolean(cd.isClaimed);
					dos.writeBoolean(cd.isNotReply);
					dos.writeByte(cd.typeMail);
					dos.writeLong(cd.receivedTime);
					
					int gCount = (cd.mItemgift != null) ? cd.mItemgift.Length : 0;
					dos.writeByte((sbyte)gCount);
					if (gCount > 0)
					{
						for (int j = 0; j < gCount; j++)
						{
							Item_Drop it = cd.mItemgift[j];
							if (it != null)
							{
								dos.writeByte(it.typeObject);
								dos.writeShort(it.ID);
								dos.writeUTF(it.name != null ? it.name : "");
								dos.writeShort(it.IdIcon);
								dos.writeInt(it.num);
								dos.writeByte(it.colorName);
							}
							else
							{
								dos.writeByte((sbyte)0);
								dos.writeShort((short)-1);
								dos.writeUTF("");
								dos.writeShort((short)0);
								dos.writeInt(0);
								dos.writeByte((sbyte)0);
							}
						}
					}
				}
			}
			
			saveRMS("HTTH_MAIL_" + playerName.ToLower(), dos.toByteArray());
			dos.close();
		}
		catch (Exception)
		{
		}
	}

	public static mVector loadMailCache(string playerName)
	{
		mVector result = new mVector();
		if (string.IsNullOrEmpty(playerName)) return result;
		try
		{
			sbyte[] data = loadRMS("HTTH_MAIL_" + playerName.ToLower());
			if (data == null || data.Length == 0) return result;
			
			DataInputStream dis = new DataInputStream(data);
			
			int count = dis.readShort();
			for (int i = 0; i < count; i++)
			{
				bool isInvite = dis.readBoolean();
				if (isInvite)
				{
					int inviteId = dis.readInt();
					sbyte typeInvite = dis.readByte();
					string inviteName = dis.readUTF();
					string inviteInfo = dis.readUTF();
					int priceFight = dis.readInt();
					int typeFight = dis.readInt();
					string contentRaw = dis.readUTF();
					long receivedTime = dis.readLong();
					
					ChatDetail cd = new ChatDetail(inviteName, (sbyte)1, ChatDetail.CAT_MAIL);
					cd.setInvite(inviteId, typeInvite, inviteName, inviteInfo, priceFight, typeFight);
					cd.contentRaw = contentRaw;
					cd.receivedTime = receivedTime;
					cd.isNew = false;
					result.addElement(cd);
				}
				else
				{
					int mailId = dis.readInt();
					string sender = dis.readUTF();
					string title = dis.readUTF();
					string contentRaw = dis.readUTF();
					bool isClaimed = dis.readBoolean();
					bool isNotReply = dis.readBoolean();
					sbyte typeMail = dis.readByte();
					long receivedTime = dis.readLong();
					
					int gCount = (int)(dis.readByte() & 0xFF);
					Item_Drop[] gifts = null;
					if (gCount > 0)
					{
						gifts = new Item_Drop[gCount];
						for (int j = 0; j < gCount; j++)
						{
							sbyte gType = dis.readByte();
							short gId = dis.readShort();
							string gName = dis.readUTF();
							short gIcon = dis.readShort();
							int gNum = dis.readInt();
							sbyte gColor = dis.readByte();
							gifts[j] = new Item_Drop(gId, gType, gName, 0, 0, gIcon, gColor);
							gifts[j].num = gNum;
						}
					}
					
					ChatDetail cd = new ChatDetail(sender, (sbyte)1, ChatDetail.CAT_MAIL);
					cd.setMailGift(mailId, title, (sbyte)(isClaimed ? 1 : 0), typeMail, gifts, isNotReply);
					cd.contentRaw = contentRaw;
					cd.receivedTime = receivedTime;
					cd.isNew = false;
					if (!string.IsNullOrEmpty(contentRaw))
					{
						cd.addString(contentRaw, sender, -1);
					}
					result.addElement(cd);
				}
			}
			dis.close();
		}
		catch (Exception)
		{
		}
		return result;
	}
}
