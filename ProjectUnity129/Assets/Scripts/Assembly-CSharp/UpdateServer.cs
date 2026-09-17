using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;

public class UpdateServer
{
	public static string DEFAULT_SERVER_LIST_URL => ClientConfig.SERVER_LIST_URL;
	public static string SERVER_LIST_URL => ClientConfig.SERVER_LIST_URL;

	public static List<string> serverHosts = new List<string>();
	public static List<string> serverNames = new List<string>();
	public static List<int> serverLang = new List<int>();
	public static List<int> serverFrameId = new List<int>();

	private static long lastUpdateTime = 0;
	private const long UPDATE_INTERVAL = 300000;
	private static bool isUpdating = false;

	static UpdateServer()
	{
		// Mặc định list server ban đầu là EMPTY
		clearServers();
	}

	public static void clearServers()
	{
		lock (serverHosts)
		{
			serverHosts.Clear();
			serverNames.Clear();
			serverLang.Clear();
			serverFrameId.Clear();
			GameCanvas.strListServer = new string[0][];
			GameCanvas.hostServer = "";
			GameCanvas.portServer = 0;
			GameCanvas.IndexServer = 0;

			if (GameCanvas.loginScr != null)
			{
				GameCanvas.loginScr.onServersUpdated();
			}
			if (GameCanvas.fristLoginScr != null)
			{
				GameCanvas.fristLoginScr.onServersUpdated();
			}
		}
	}

	public static void autoUpdateIfNeeded()
	{
		loadServers();
	}

	public static void loadServers()
	{
		loadServers(false);
	}

	public static void loadServers(bool forceUpdate)
	{
		if (!KeyAuthManager.isAuthorized)
		{
			clearServers();
			return;
		}

		long currentTime = mSystem.currentTimeMillis();
		if (!forceUpdate && (currentTime - lastUpdateTime) < UPDATE_INTERVAL && serverHosts.Count > 0)
		{
			return;
		}
		if (isUpdating)
		{
			return;
		}
		isUpdating = true;

		new System.Threading.Thread(() =>
		{
			try
			{
				loadServersFromRemote();
			}
			finally
			{
				isUpdating = false;
				lastUpdateTime = mSystem.currentTimeMillis();
			}
		}).Start();
	}

	public static void applyCustomServers(List<string> entries)
	{
		if (!KeyAuthManager.isAuthorized)
		{
			clearServers();
			return;
		}

		if (entries == null || entries.Count == 0)
		{
			loadServersFromRemote();
			return;
		}

		lock (serverHosts)
		{
			serverHosts.Clear();
			serverNames.Clear();
			serverLang.Clear();
			serverFrameId.Clear();

			for (int i = 0; i < entries.Count; i++)
			{
				if (parseServerEntry(entries[i], out string hp, out string nm, out int lg, out int fid))
				{
					serverHosts.Add(hp);
					serverNames.Add(nm);
					serverLang.Add(lg);
					serverFrameId.Add(fid);
				}
			}

			if (serverHosts.Count > 0)
			{
				buildServerListForLanguage();
				restoreIndexServer();
			}
			else
			{
				loadServersFromRemote();
			}
		}
	}

	public static void loadServersFromRemote()
	{
		if (!KeyAuthManager.isAuthorized)
		{
			clearServers();
			return;
		}

		List<string> tempHosts = new List<string>();
		List<string> tempNames = new List<string>();
		List<int> tempLang = new List<int>();
		List<int> tempFrameId = new List<int>();

		bool loaded = false;

		string query = "?client_id=" + ClientConfig.CLIENT_ID + "&v=" + ClientConfig.CLIENT_VERSION + "&t=" + mSystem.currentTimeMillis();
		// 1. Thử tải từ URL chính của repo
		try
		{
			string content = fetchUrl(DEFAULT_SERVER_LIST_URL + query);
			if (!string.IsNullOrEmpty(content))
			{
				parseRawServerContent(content, tempHosts, tempNames, tempLang, tempFrameId);
				if (tempHosts.Count > 0) loaded = true;
			}
		}
		catch (Exception) {}

		// 2. Nếu không được thì tải từ SERVER_LIST_URL fallback
		if (!loaded)
		{
			try
			{
				string content = fetchUrl(SERVER_LIST_URL + query);
				if (!string.IsNullOrEmpty(content))
				{
					parseRawServerContent(content, tempHosts, tempNames, tempLang, tempFrameId);
					if (tempHosts.Count > 0) loaded = true;
				}
			}
			catch (Exception) {}
		}

		if (loaded)
		{
			lock (serverHosts)
			{
				serverHosts = tempHosts;
				serverNames = tempNames;
				serverLang = tempLang;
				serverFrameId = tempFrameId;
				loadCustomServersFromRms();
				buildServerListForLanguage();
				restoreIndexServer();
			}
		}
	}

	private static void parseRawServerContent(string content, List<string> outHosts, List<string> outNames, List<int> outLang, List<int> outFid)
	{
		content = content.Replace("\r\n", "\n");
		string[] lines = content.Split(new char[] { '\n' }, StringSplitOptions.RemoveEmptyEntries);

		for (int i = 0; i < lines.Length; i++)
		{
			string line = lines[i].Trim();
			if (line.Length == 0 || line.StartsWith("#")) continue;

			string[] entries = line.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
			for (int j = 0; j < entries.Length; j++)
			{
				if (parseServerEntry(entries[j], out string hp, out string nm, out int lg, out int fid))
				{
					outHosts.Add(hp);
					outNames.Add(nm);
					outLang.Add(lg);
					outFid.Add(fid);
				}
			}
		}
	}

	public static bool parseServerEntry(string entry, out string hostPort, out string name, out int lang, out int frameId)
	{
		hostPort = "";
		name = "";
		lang = 0;
		frameId = 0;
		if (string.IsNullOrEmpty(entry)) return false;

		string[] parts = entry.Trim().Split(':');
		int partsSize = parts.Length;
		int port = 2229;
		string host = "";

		if (partsSize >= 3)
		{
			name = parts[0].Trim();
			host = parts[1].Trim();
			try { port = int.Parse(parts[2].Trim()); } catch (Exception) { port = 2229; }
			if (partsSize >= 4)
			{
				try { lang = int.Parse(parts[3].Trim()); } catch (Exception) { lang = 0; }
			}
			if (partsSize >= 5)
			{
				try { frameId = int.Parse(parts[4].Trim()); } catch (Exception) { frameId = 0; }
			}
		}
		else if (partsSize == 2)
		{
			string p1 = parts[1].Trim();
			if (isNumeric(p1))
			{
				host = parts[0].Trim();
				try { port = int.Parse(p1); } catch (Exception) { port = 2229; }
				name = host;
			}
			else
			{
				name = parts[0].Trim();
				host = parts[1].Trim();
			}
		}
		else
		{
			host = parts[0].Trim();
			name = host;
		}

		if (host.Length == 0) return false;
		if (host.Equals("localhost", StringComparison.OrdinalIgnoreCase)) host = "127.0.0.1";
		if (port <= 0 || port > 65535) port = 2229;

		frameId = Math.Max(0, Math.Min(3, frameId));
		hostPort = host + ":" + port;
		if (string.IsNullOrEmpty(name)) name = hostPort;
		return true;
	}

	public static string fetchUrl(string url)
	{
		try
		{
			ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
			HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
			request.Timeout = 6000;
			request.Method = "GET";
			using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
			using (StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8))
			{
				return reader.ReadToEnd();
			}
		}
		catch (Exception)
		{
			try
			{
				return GameMidlet.connectHTTP(url);
			}
			catch (Exception)
			{
				return null;
			}
		}
	}

	public static string getCurrentServerName()
	{
		int relIdx = globalToRelative(GameCanvas.IndexServer);
		if (GameCanvas.strListServer != null
			&& GameCanvas.language >= 0
			&& GameCanvas.language < GameCanvas.strListServer.Length
			&& GameCanvas.strListServer[GameCanvas.language] != null
			&& relIdx >= 0
			&& relIdx < GameCanvas.strListServer[GameCanvas.language].Length)
		{
			return GameCanvas.strListServer[GameCanvas.language][relIdx];
		}
		return getServer(GameCanvas.IndexServer);
	}

	public static void setServerIndex(int index)
	{
		if (index >= 0 && index < serverHosts.Count)
		{
			GameCanvas.IndexServer = index;
			GameCanvas.hostServer = getHost(index);
			GameCanvas.portServer = getPort(index);
			if (GameCanvas.loginScr != null)
			{
				GameCanvas.loginScr.onServersUpdated();
			}
			if (GameCanvas.fristLoginScr != null)
			{
				GameCanvas.fristLoginScr.onServersUpdated();
			}
		}
	}

	public static void buildServerListForLanguage()
	{
		int maxLang = GameCanvas.language;
		for (int i = 0; i < serverLang.Count; i++)
		{
			maxLang = Math.Max(maxLang, serverLang[i]);
		}

		string[][] arr = new string[maxLang + 1][];
		for (int j = 0; j <= maxLang; j++)
		{
			arr[j] = new string[0];
		}

		for (int k = 0; k <= maxLang; k++)
		{
			List<string> list = new List<string>();
			for (int l = 0; l < serverNames.Count; l++)
			{
				int lang = (l < serverLang.Count) ? serverLang[l] : 0;
				if (lang == k)
				{
					list.Add(serverNames[l]);
				}
			}
			arr[k] = list.ToArray();
		}

		GameCanvas.strListServer = arr;
	}

	public static void restoreIndexServer()
	{
		if (serverHosts.Count == 0) return;

		string prevHostPort = null;
		if (!string.IsNullOrEmpty(GameCanvas.hostServer))
		{
			prevHostPort = GameCanvas.hostServer;
			if (GameCanvas.portServer > 0)
			{
				prevHostPort += ":" + GameCanvas.portServer;
			}
		}

		int newIndex = -1;
		if (prevHostPort != null)
		{
			string normalizedPrev = normalizeHostPort(prevHostPort);
			for (int i = 0; i < serverHosts.Count; i++)
			{
				string normalizedServer = normalizeHostPort(serverHosts[i]);
				if (normalizedServer.Equals(normalizedPrev, StringComparison.OrdinalIgnoreCase))
				{
					newIndex = i;
					break;
				}
			}
		}

		if (newIndex != -1)
		{
			GameCanvas.IndexServer = newIndex;
		}
		else
		{
			int lang = GameCanvas.language;
			newIndex = -1;
			for (int j = 0; j < serverLang.Count; j++)
			{
				if (serverLang[j] == lang)
				{
					newIndex = j;
					break;
				}
			}
			if (newIndex == -1) newIndex = 0;
			GameCanvas.IndexServer = newIndex;
		}

		if (GameCanvas.IndexServer >= 0 && GameCanvas.IndexServer < serverHosts.Count)
		{
			GameCanvas.hostServer = getHost(GameCanvas.IndexServer);
			GameCanvas.portServer = getPort(GameCanvas.IndexServer);
		}

		if (GameCanvas.loginScr != null)
		{
			GameCanvas.loginScr.onServersUpdated();
		}
		if (GameCanvas.fristLoginScr != null)
			GameCanvas.fristLoginScr.onServersUpdated();
	}

	public static string getServer(int index)
	{
		if (serverHosts.Count == 0) return "";
		if (index < 0 || index >= serverHosts.Count) return serverHosts[0];
		return serverHosts[index];
	}

	public static string getHost(int index)
	{
		if (index >= 0 && index < serverHosts.Count)
		{
			string hostPort = serverHosts[index];
			string[] parts = hostPort.Split(':');
			if (parts.Length > 0)
			{
				string host = parts[0].Trim();
				if (host.Equals("localhost", StringComparison.OrdinalIgnoreCase)) return "127.0.0.1";
				return host;
			}
		}
		return "";
	}

	public static int getPort(int index)
	{
		if (index >= 0 && index < serverHosts.Count)
		{
			string hostPort = serverHosts[index];
			string[] parts = hostPort.Split(':');
			if (parts.Length > 1)
			{
				if (int.TryParse(parts[1].Trim(), out int p)) return p;
			}
		}
		return 2229;
	}

	private static string normalizeHostPort(string hostPort)
	{
		if (string.IsNullOrEmpty(hostPort)) return hostPort;
		string[] parts = hostPort.Split(':');
		if (parts.Length < 2) return hostPort.ToLower();
		string host = parts[0].Trim().ToLower();
		string port = parts[1].Trim();
		if (host.Equals("localhost")) host = "127.0.0.1";
		return host + ":" + port;
	}

	public static int relativeToGlobal(int relativeIndex, int lang)
	{
		int count = 0;
		for (int i = 0; i < serverHosts.Count; i++)
		{
			int lg = (i < serverLang.Count) ? serverLang[i] : 0;
			if (lg == lang)
			{
				if (count == relativeIndex) return i;
				count++;
			}
		}
		for (int j = 0; j < serverHosts.Count; j++) return j;
		return 0;
	}

	public static int globalToRelative(int globalIndex)
	{
		if (globalIndex < 0 || globalIndex >= serverHosts.Count) return 0;
		int lang = (globalIndex < serverLang.Count) ? serverLang[globalIndex] : 0;
		int relativeIndex = 0;
		for (int i = 0; i < globalIndex; i++)
		{
			int lg = (i < serverLang.Count) ? serverLang[i] : 0;
			if (lg == lang) relativeIndex++;
		}
		return relativeIndex;
	}

	public static bool isServerNew(int globalIndex)
	{
		if (globalIndex >= 0 && globalIndex < serverFrameId.Count)
		{
			if (serverFrameId[globalIndex] == 1) return true;
		}
		if (globalIndex >= 0 && globalIndex < serverNames.Count)
		{
			string name = serverNames[globalIndex];
			if (!string.IsNullOrEmpty(name))
			{
				string upper = name.ToUpper();
				if (upper.Contains("[NEW]") || upper.Contains("(NEW)") || upper.Contains(" NEW") || upper.Contains("MỚI"))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static string getServerName(int index)
	{
		if (index >= 0 && index < serverNames.Count)
		{
			return serverNames[index];
		}
		if (index >= 0 && index < serverHosts.Count)
		{
			return serverHosts[index];
		}
		return "";
	}

	public static void loadCustomServersFromRms()
	{
		if (!KeyAuthManager.isAuthorized) return;
		try
		{
			sbyte[] savedData = CRes.loadRMS("RMS_CUSTOM_SERVERS");
			if (savedData != null && savedData.Length > 0)
			{
				string saved = Encoding.UTF8.GetString(ArrayCast.cast(savedData));
				string[] entries = saved.Split(';');
				for (int i = 0; i < entries.Length; i++)
				{
					if (parseServerEntry(entries[i], out string hp, out string nm, out int lg, out int fid))
					{
						int existing = serverHosts.IndexOf(hp);
						if (existing >= 0)
						{
							serverNames[existing] = nm;
						}
						else
						{
							serverHosts.Add(hp);
							serverNames.Add(nm);
							serverLang.Add(lg);
							serverFrameId.Add(fid);
						}
					}
				}
			}
		}
		catch (Exception) {}
	}

	public static bool parseAndAddCustomServer(string raw)
	{
		if (!KeyAuthManager.checkAuthorizedOrNotice()) return false;
		if (string.IsNullOrEmpty(raw) || string.IsNullOrEmpty(raw.Trim())) return false;
		if (parseServerEntry(raw.Trim(), out string hp, out string nm, out int lg, out int fid))
		{
			int existing = serverHosts.IndexOf(hp);
			if (existing >= 0)
			{
				serverNames[existing] = nm;
				setServerIndex(existing);
			}
			else
			{
				serverHosts.Add(hp);
				serverNames.Add(nm);
				serverLang.Add(lg);
				serverFrameId.Add(fid);
				setServerIndex(serverHosts.Count - 1);
			}

			try
			{
				sbyte[] savedData = CRes.loadRMS("RMS_CUSTOM_SERVERS");
				string saved = (savedData != null && savedData.Length > 0) ? Encoding.UTF8.GetString(ArrayCast.cast(savedData)) : "";
				string newEntry = nm + ":" + hp + ":" + lg + ":" + fid;
				if (!saved.Contains(hp))
				{
					saved = (saved.Length > 0 ? saved + ";" : "") + newEntry;
					byte[] bytes = Encoding.UTF8.GetBytes(saved);
					CRes.saveRMS("RMS_CUSTOM_SERVERS", ArrayCast.cast(bytes));
				}
			}
			catch (Exception) {}

			buildServerListForLanguage();
			return true;
		}
		return false;
	}

	public static bool isAllowedHost(string host)
	{
		return KeyAuthManager.isAuthorized;
	}

	public static void showBlockedDialog()
	{
		GameCanvas.Start_Normal_Only_CmdClose_DiaLog("liên hệ t.me/@ThanhNamYe để thuê mod nhé");
	}

	private static bool isNumeric(string str)
	{
		if (string.IsNullOrEmpty(str)) return false;
		for (int i = 0; i < str.Length; i++)
		{
			if (!char.IsDigit(str[i])) return false;
		}
		return true;
	}
}
