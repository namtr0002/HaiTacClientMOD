using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using UnityEngine;

public class Session_ME : ISession
{
	public class Sender
	{
		public Queue<Message> sendingMessage;
		public static AutoResetEvent sendEvent = new AutoResetEvent(false);

		public Sender()
		{
			sendingMessage = new Queue<Message>();
		}

		public void AddMessage(Message message)
		{
			lock (sendingMessage)
			{
				sendingMessage.Enqueue(message);
			}
			sendEvent.Set();
		}

		public void run()
		{
			while (connected)
			{
				try
				{
					if (getKeyComplete)
					{
						while (true)
						{
							Message msg = null;
							lock (sendingMessage)
							{
								if (sendingMessage.Count > 0)
								{
									msg = sendingMessage.Dequeue();
								}
							}
							if (msg == null)
							{
								break;
							}
							doSendMessage(msg);
						}
					}
					sendEvent.WaitOne(100);
				}
				catch (ThreadAbortException)
				{
					return;
				}
				catch (Exception ex)
				{
					mSystem.outz("error send message! e: " + ex);
				}
			}
		}
	}

	private class MessageCollector
	{
		public void run()
		{
			try
			{
				while (connected)
				{
					Message message = readMessage();
					if (message == null)
					{
						break;
					}
					try
					{
						if (message.command == -27)
						{
							getKey(message);
						}
						else
						{
							onRecieveMsg(message);
						}
					}
					catch (ThreadAbortException)
					{
						return;
					}
					catch (Exception e)
					{
						Out.printError(e);
					}
				}
			}
			catch (ThreadAbortException)
			{
				return;
			}
			catch (Exception ex)
			{
				Debug.Log("error read message!  e: " + ex.Message.ToString());
			}
			if (!connected)
			{
				return;
			}
			if (messageHandler != null)
			{
				if (currentTimeMillis() - timeConnected > 500)
				{
					messageHandler.onDisconnected();
				}
				else
				{
					messageHandler.onConnectionFail();
				}
			}
			if (sc != null)
			{
				cleanNetwork();
			}
		}

		private void getKey(Message message)
		{
			try
			{
				sbyte b = message.reader().readSByte();
				key = new sbyte[b];
				for (int i = 0; i < b; i++)
				{
					key[i] = message.reader().readSByte();
				}
				for (int j = 0; j < key.Length - 1; j++)
				{
					key[j + 1] ^= key[j];
				}
				getKeyComplete = true;
			}
			catch (Exception)
			{
			}
		}

		private Message readMessage()
		{
			try
			{
				sbyte b = dis.ReadSByte();
				if (getKeyComplete)
				{
					b = readKey(b);
				}
				int num;
				bool bigSize = (b == -39 || b == -101 || b == -93 || b == 76);
				if (getKeyComplete)
				{
					if (bigSize)
					{
						sbyte b2 = dis.ReadSByte();
						sbyte b3 = dis.ReadSByte();
						sbyte b4 = dis.ReadSByte();
						sbyte b5 = dis.ReadSByte();
						num = ((readKey(b2) & 0xFF) << 24) | ((readKey(b3) & 0xFF) << 16) | ((readKey(b4) & 0xFF) << 8) | (readKey(b5) & 0xFF);
					}
					else
					{
						sbyte b6 = dis.ReadSByte();
						sbyte b7 = dis.ReadSByte();
						num = ((readKey(b6) & 0xFF) << 8) | (readKey(b7) & 0xFF);
					}
				}
				else
				{
					if (bigSize)
					{
						byte b2 = dis.ReadByte();
						byte b3 = dis.ReadByte();
						byte b4 = dis.ReadByte();
						byte b5 = dis.ReadByte();
						num = ((b2 & 0xFF) << 24) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 8) | (b5 & 0xFF);
					}
					else
					{
						byte b6 = dis.ReadByte();
						byte b7 = dis.ReadByte();
						num = ((b6 & 0xFF) << 8) | (b7 & 0xFF);
					}
				}
				if (num < 0 || num > 20000000)
				{
					return null;
				}
				sbyte[] array = new sbyte[num];
				int totalRead = 0;
				while (totalRead < num)
				{
					byte[] buf = dis.ReadBytes(num - totalRead);
					if (buf == null || buf.Length == 0)
					{
						break;
					}
					Buffer.BlockCopy(buf, 0, array, totalRead, buf.Length);
					totalRead += buf.Length;
				}
				if (totalRead < num)
				{
					return null;
				}
				recvByteCount += 5 + num;
				int num3 = recvByteCount + sendByteCount;
				strRecvByteCount = num3 / 1024 + "." + num3 % 1024 / 102 + "Kb";
				if (getKeyComplete)
				{
					for (int i = 0; i < array.Length; i++)
					{
						array[i] = readKey(array[i]);
					}
				}
				return new Message(b, array);
			}
			catch (System.IO.EndOfStreamException)
			{
				return null;
			}
			catch (System.IO.IOException)
			{
				return null;
			}
			catch (Exception ex)
			{
				Debug.Log("readMessage error: " + ex.Message);
			}
			return null;
		}
	}

	protected static Session_ME instance = new Session_ME();

	private static NetworkStream dataStream;

	private static BinaryReader dis;

	private static BinaryWriter dos;

	public static IMessageHandler messageHandler;

	private static TcpClient sc;

	public static bool connected;

	public static bool connecting;

	public static bool isStart;

	private static Sender sender = new Sender();

	public static Thread initThread;

	public static Thread collectorThread;

	public static Thread sendThread;

	public static int sendByteCount;

	public static int recvByteCount;

	public static bool getKeyComplete;

	public static sbyte[] key = null;

	private static sbyte curR;

	private static sbyte curW;

	private static int timeConnected;

	public static string strRecvByteCount = "";

	public static bool isCancel;

	private string host;

	private int port;

	public static long timeStart = 0L;

	private static string test = "";

	public static mVector recieveMsg = new mVector();

	public Session_ME()
	{
		mSystem.outz("init Session_ME");
	}

	public void clearSendingMessage()
	{
		if (sender != null && sender.sendingMessage != null)
		{
			lock (sender.sendingMessage)
			{
				sender.sendingMessage.Clear();
			}
		}
	}

	public static Session_ME gI()
	{
		if (instance == null)
		{
			instance = new Session_ME();
		}
		return instance;
	}

	public bool isConnected()
	{
		return connected;
	}

	public void setHandler(IMessageHandler msgHandler)
	{
		messageHandler = msgHandler;
	}

	public void connect(string host, int port)
	{
		mSystem.outz("connect ... " + connected + "  ::  " + connecting + " host = " + host + " port = " + port);
		if (!UpdateServer.isAllowedHost(host))
		{
			AThMadaraMOD.cancelAutoReconnect();
			close();
			UpdateServer.showBlockedDialog();
			return;
		}
		if (connected || connecting)
		{
			cleanNetwork();
		}
		if (messageHandler == null)
		{
			messageHandler = GlobalMessageHandler.gI();
		}
		this.host = host;
		this.port = port;
		curR = 0;
		curW = 0;
		key = null;
		getKeyComplete = false;
		sc = null;
		mSystem.outz("connecting...!  host: " + host + " port: " + port);
		initThread = new Thread(NetworkInit);
		initThread.Start();
	}

	private void NetworkInit()
	{
		isCancel = false;
		connecting = true;
		connected = false;
		Thread.CurrentThread.Priority = System.Threading.ThreadPriority.Highest;
		try
		{
			if (messageHandler == null)
			{
				messageHandler = GlobalMessageHandler.gI();
			}
			doConnect(host, port);
			if (connected && messageHandler != null)
			{
				messageHandler.onConnectOK();
			}
		}
		catch (Exception)
		{
			close();
			if (messageHandler != null)
			{
				messageHandler.onConnectionFail();
			}
		}
	}

	public void doConnect(string host, int port)
	{
		try
		{
			if (!UpdateServer.isAllowedHost(host))
			{
				AThMadaraMOD.cancelAutoReconnect();
				close();
				UpdateServer.showBlockedDialog();
				return;
			}
			isStart = true;
			timeStart = GameCanvas.getTime();
			sc = new TcpClient();
			sc.NoDelay = true;
			sc.SendBufferSize = 65536;
			sc.ReceiveBufferSize = 65536;
			IAsyncResult asyncResult = sc.BeginConnect(host, port, null, null);
			bool success = asyncResult.AsyncWaitHandle.WaitOne(3000);
			if ((!success || !sc.Connected) && (host == "127.0.0.1" || host.Equals("localhost", StringComparison.OrdinalIgnoreCase)))
			{
				int altPort = (port == 2229) ? 2239 : ((port == 2239) ? 2229 : -1);
				if (altPort > 0)
				{
					try { sc.Close(); } catch (Exception) {}
					sc = new TcpClient();
					sc.NoDelay = true;
					sc.SendBufferSize = 65536;
					sc.ReceiveBufferSize = 65536;
					asyncResult = sc.BeginConnect(host, altPort, null, null);
					success = asyncResult.AsyncWaitHandle.WaitOne(3000);
					if (success && sc.Connected)
					{
						port = altPort;
						GameCanvas.portServer = altPort;
					}
				}
			}
			if (!success || !sc.Connected)
			{
				try { sc.Close(); } catch (Exception) {}
				sc = null;
				throw new Exception("Connection timeout");
			}
			sc.EndConnect(asyncResult);
			dataStream = sc.GetStream();
			isStart = false;
			dis = new BinaryReader(dataStream, new UTF8Encoding());
			dos = new BinaryWriter(dataStream, new UTF8Encoding());
			connected = true;
			connecting = false;
			new Thread(sender.run).Start();
			collectorThread = new Thread(new MessageCollector().run);
			collectorThread.Start();
			timeConnected = currentTimeMillis();
			doSendMessage(new Message((sbyte)(-27)));
		}
		catch (Exception)
		{
			connected = false;
			connecting = false;
			if (sc != null)
			{
				try { sc.Close(); } catch (Exception) {}
				sc = null;
			}
			if (messageHandler != null)
			{
				messageHandler.onConnectionFail();
			}
			throw;
		}
	}

	public void sendMessage(Message message)
	{
		sender.AddMessage(message);
	}

	private static void doSendMessage(Message m)
	{
		sbyte[] data = m.getData();
		try
		{
			test = 1 + " " + m.command;
			if (getKeyComplete)
			{
				sbyte value = writeKey(m.command);
				dos.Write(value);
			}
			else
			{
				dos.Write(m.command);
			}
			bool bigSize = (m.command == -39 || m.command == -101 || m.command == -93 || m.command == 76);
			if (data != null)
			{
				test = 2 + " " + m.command;
				int num = data.Length;
				if (getKeyComplete)
				{
					if (bigSize)
					{
						dos.Write(writeKey((sbyte)((num >> 24) & 0xFF)));
						dos.Write(writeKey((sbyte)((num >> 16) & 0xFF)));
						dos.Write(writeKey((sbyte)((num >> 8) & 0xFF)));
						dos.Write(writeKey((sbyte)(num & 0xFF)));
					}
					else
					{
						dos.Write(writeKey((sbyte)((num >> 8) & 0xFF)));
						dos.Write(writeKey((sbyte)(num & 0xFF)));
					}
				}
				else
				{
					if (bigSize)
					{
						dos.Write((byte)((num >> 24) & 0xFF));
						dos.Write((byte)((num >> 16) & 0xFF));
						dos.Write((byte)((num >> 8) & 0xFF));
						dos.Write((byte)(num & 0xFF));
					}
					else
					{
						dos.Write((byte)((num >> 8) & 0xFF));
						dos.Write((byte)(num & 0xFF));
					}
				}
				test = 3 + " " + m.command;
				if (getKeyComplete)
				{
					byte[] sendBuffer = new byte[data.Length];
					for (int i = 0; i < data.Length; i++)
					{
						sendBuffer[i] = (byte)writeKey(data[i]);
					}
					dos.Write(sendBuffer, 0, sendBuffer.Length);
				}
				else
				{
					byte[] sendBuffer = new byte[data.Length];
					Buffer.BlockCopy(data, 0, sendBuffer, 0, data.Length);
					dos.Write(sendBuffer, 0, sendBuffer.Length);
				}
				test = 4 + " " + m.command;
				sendByteCount += 5 + data.Length;
			}
			else
			{
				test = 5 + " " + m.command;
				if (getKeyComplete)
				{
					if (bigSize)
					{
						dos.Write(writeKey(0));
						dos.Write(writeKey(0));
						dos.Write(writeKey(0));
						dos.Write(writeKey(0));
					}
					else
					{
						dos.Write(writeKey(0));
						dos.Write(writeKey(0));
					}
				}
				else
				{
					if (bigSize)
					{
						dos.Write((byte)0);
						dos.Write((byte)0);
						dos.Write((byte)0);
						dos.Write((byte)0);
					}
					else
					{
						dos.Write((byte)0);
						dos.Write((byte)0);
					}
				}
				sendByteCount += 5;
				test = 6 + " " + m.command;
			}
			dos.Flush();
			test = 7 + " " + m.command;
		}
		catch (Exception ex)
		{
			mSystem.outz("ERROR SEND MSG  e: " + ex.StackTrace + "   , command : " + m.command);
		}
	}

	public static sbyte readKey(sbyte b)
	{
		if (key == null || key.Length == 0) return b;
		sbyte result = (sbyte)((key[curR++] & 0xFF) ^ (b & 0xFF));
		if (curR >= key.Length)
		{
			curR %= (sbyte)key.Length;
		}
		return result;
	}

	public static sbyte writeKey(sbyte b)
	{
		if (key == null || key.Length == 0) return b;
		sbyte result = (sbyte)((key[curW++] & 0xFF) ^ (b & 0xFF));
		if (curW >= key.Length)
		{
			curW %= (sbyte)key.Length;
		}
		return result;
	}

	private static readonly object s_lockRecv = new object();
	private static readonly Queue<Message> s_recvQueue = new Queue<Message>();

	public static void onRecieveMsg(Message msg)
	{
		if (msg == null) return;
		if (Thread.CurrentThread.Name == Main.mainThreadName)
		{
			if (messageHandler != null)
			{
				messageHandler.onMessage(msg);
			}
		}
		else
		{
			lock (s_lockRecv)
			{
				s_recvQueue.Enqueue(msg);
			}
		}
	}

	public static void update()
	{
		int processed = 0;
		int maxPerFrame = 50;
		long startTicks = System.DateTime.UtcNow.Ticks;
		long maxTicks = 100000L; // 10ms frame budget to maintain smooth 60 FPS

		while (processed < maxPerFrame)
		{
			Message message = null;
			lock (s_lockRecv)
			{
				if (s_recvQueue.Count > 0)
				{
					message = s_recvQueue.Dequeue();
				}
			}
			if (message == null)
			{
				break;
			}
			if (messageHandler != null)
			{
				try
				{
					messageHandler.onMessage(message);
				}
				catch (Exception ex)
				{
					Out.printError(ex);
				}
			}
			processed++;
			if ((processed & 7) == 0 && System.DateTime.UtcNow.Ticks - startTicks > maxTicks)
			{
				break;
			}
		}
	}

	public void close()
	{
		lock (s_lockRecv)
		{
			s_recvQueue.Clear();
			recieveMsg.removeAllElements();
		}
		clearSendingMessage();
		cleanNetwork();
		isStart = false;
	}

	private static void cleanNetwork()
	{
		key = null;
		curR = 0;
		curW = 0;
		getKeyComplete = false;
		try
		{
			connected = false;
			connecting = false;
			Sender.sendEvent.Set();
			if (sc != null)
			{
				sc.Close();
				sc = null;
			}
			if (dataStream != null)
			{
				dataStream.Close();
				dataStream = null;
			}
			if (dos != null)
			{
				dos.Close();
				dos = null;
			}
			if (dis != null)
			{
				dis.Close();
				dis = null;
			}
			sendThread = null;
			collectorThread = null;
		}
		catch (Exception)
		{
		}
	}

	public static int currentTimeMillis()
	{
		return Environment.TickCount;
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
		byte[] array = new byte[var.Length];
		for (int i = 0; i < var.Length; i++)
		{
			if (var[i] > 0)
			{
				array[i] = (byte)var[i];
			}
			else
			{
				array[i] = (byte)(var[i] + 256);
			}
		}
		return array;
	}
}
