using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Threading;

public class KeyAuthManager
{
	public const string GITHUB_RAW_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/license.enc";
	public const string AES_SECRET = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
	public const string DEFAULT_CLIENT_KEY = "HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026";
	public const string CLIENT_LINE = "MOD_UNITY";
	public const string NOTICE_MESSAGE = "liên hệ t.me/@ThanhNamYe để thuê mod nhé";

	public static bool isAuthorized = true;
	private static string activeKey = DEFAULT_CLIENT_KEY;

	public static void init()
	{
		try
		{
			loadSavedKey();
			isAuthorized = true;
			UpdateServer.loadServers();
			new Thread(syncAndValidateLicense).Start();
		}
		catch (Exception)
		{
			isAuthorized = true;
			UpdateServer.loadServers();
		}
	}

	public static void loadSavedKey()
	{
		try
		{
			sbyte[] array = CRes.loadRMS("mod_key_unity");
			if (array != null && array.Length > 0)
			{
				byte[] bytes = ArrayCast.cast(array);
				string text = Encoding.UTF8.GetString(bytes).Trim();
				if (!string.IsNullOrEmpty(text))
				{
					activeKey = text;
				}
			}
		}
		catch (Exception)
		{
		}
	}

	public static void saveKey(string key)
	{
		try
		{
			if (!string.IsNullOrEmpty(key) && key.Trim().Length > 0)
			{
				activeKey = key.Trim();
				byte[] bytes = Encoding.UTF8.GetBytes(activeKey);
				CRes.saveRMS("mod_key_unity", ArrayCast.cast(bytes));
			}
		}
		catch (Exception)
		{
		}
	}

	public static string getActiveKey()
	{
		if (!string.IsNullOrEmpty(activeKey) && activeKey.Trim().Length > 0)
		{
			return activeKey.Trim();
		}
		return DEFAULT_CLIENT_KEY;
	}

	public static bool checkAuthorizedOrNotice()
	{
		if (!isAuthorized)
		{
			showBlockedDialog();
			return false;
		}
		return true;
	}

	public static void showBlockedDialog()
	{
		GameCanvas.Start_Normal_Only_CmdClose_DiaLog(NOTICE_MESSAGE);
	}

	public static void checkAndActivateKey(string inputKey)
	{
		if (string.IsNullOrEmpty(inputKey) || inputKey.Trim().Length == 0)
		{
			GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Mã Key không được để trống!");
			return;
		}

		GameCanvas.Start_Waiting_Connect_DiaLog("Đang kiểm tra Key bản quyền...", isCmdClose: true);

		new Thread(() =>
		{
			try
			{
				string target = inputKey.Trim();
				bool ok = syncAndValidateSpecificKey(target);
				Main.runOnMainThread(() =>
				{
					GameCanvas.end_Dialog();

					if (ok)
					{
						saveKey(target);
						isAuthorized = true;
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog(
							"Kích hoạt Key thành công!\nKey: " + target + "\nĐã mở khóa danh sách máy chủ và cho phép thêm IP Server!");
						if (GameCanvas.loginScr != null) GameCanvas.loginScr.onServersUpdated();
						if (GameCanvas.fristLoginScr != null) GameCanvas.fristLoginScr.onServersUpdated();
					}
					else
					{
						isAuthorized = false;
						UpdateServer.clearServers();
						GameCanvas.Start_Normal_Only_CmdClose_DiaLog(NOTICE_MESSAGE);
					}
				});
			}
			catch (Exception)
			{
				Main.runOnMainThread(() =>
				{
					isAuthorized = false;
					UpdateServer.clearServers();
					GameCanvas.end_Dialog();
					GameCanvas.Start_Normal_Only_CmdClose_DiaLog(NOTICE_MESSAGE);
				});
			}
		}).Start();
	}

	public static bool isLocalValidKey(string targetKey)
	{
		if (string.IsNullOrEmpty(targetKey)) return false;
		string k = targetKey.Trim();
		return k.Equals(DEFAULT_CLIENT_KEY, StringComparison.OrdinalIgnoreCase)
			|| k.Equals("HTTH_CLIENT_NAMTR0002_SECURE_KEY_DEFAULT_2026", StringComparison.OrdinalIgnoreCase)
			|| k.Equals("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026", StringComparison.OrdinalIgnoreCase)
			|| k.Equals("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V3_2026", StringComparison.OrdinalIgnoreCase)
			|| k.Equals("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026", StringComparison.OrdinalIgnoreCase)
			|| k.StartsWith("HTTH_CLIENT_NAMTR0002_SECURE_KEY_", StringComparison.OrdinalIgnoreCase);
	}

	public static bool syncAndValidateSpecificKey(string targetKey)
	{
		if (string.IsNullOrEmpty(targetKey) || targetKey.Trim().Length == 0) return false;
		targetKey = targetKey.Trim();

		if (isLocalValidKey(targetKey))
		{
			return true;
		}

		try
		{
			ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
			HttpWebRequest httpWebRequest = (HttpWebRequest)WebRequest.Create(GITHUB_RAW_URL + "?t=" + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds());
			httpWebRequest.Method = "GET";
			httpWebRequest.Timeout = 3000;
			httpWebRequest.ReadWriteTimeout = 5000;

			using (HttpWebResponse httpWebResponse = (HttpWebResponse)httpWebRequest.GetResponse())
			{
				if (httpWebResponse.StatusCode != HttpStatusCode.OK)
				{
					return false;
				}

				using (Stream stream = httpWebResponse.GetResponseStream())
				using (StreamReader streamReader = new StreamReader(stream, Encoding.UTF8))
				{
					string text = streamReader.ReadToEnd().Trim();
					if (string.IsNullOrEmpty(text))
					{
						return false;
					}

					byte[] encryptedData = Convert.FromBase64String(text.Replace(" ", "").Replace("\r", "").Replace("\n", ""));
					byte[] keyBytes;
					using (SHA256 sHA = SHA256.Create())
					{
						byte[] hash = sHA.ComputeHash(Encoding.UTF8.GetBytes(AES_SECRET));
						keyBytes = new byte[16];
						Array.Copy(hash, keyBytes, 16);
					}

					byte[] iv = new byte[16];
					Array.Copy(encryptedData, 0, iv, 0, 16);
					byte[] cipherBytes = new byte[encryptedData.Length - 16];
					Array.Copy(encryptedData, 16, cipherBytes, 0, cipherBytes.Length);

					string json;
					using (Aes aes = Aes.Create())
					{
						aes.Key = keyBytes;
						aes.IV = iv;
						aes.Mode = CipherMode.CBC;
						aes.Padding = PaddingMode.PKCS7;
						using (ICryptoTransform cryptoTransform = aes.CreateDecryptor())
						{
							byte[] plainBytes = cryptoTransform.TransformFinalBlock(cipherBytes, 0, cipherBytes.Length);
							json = Encoding.UTF8.GetString(plainBytes);
						}
					}

					// 1. Kiểm tra master_enabled
					if (json.Contains("\"master_enabled\": false") || json.Contains("\"master_enabled\":false"))
					{
						return false;
					}

					// 2. Kiểm tra dòng MOD_UNITY
					int lineIdx = json.IndexOf("\"line_id\": \"" + CLIENT_LINE + "\"");
					if (lineIdx == -1) lineIdx = json.IndexOf("\"line_id\":\"" + CLIENT_LINE + "\"");
					if (lineIdx != -1)
					{
						string lineSnippet = json.Substring(lineIdx, Math.Min(json.Length - lineIdx, 300));
						if (lineSnippet.Contains("\"enabled\": false") || lineSnippet.Contains("\"enabled\":false"))
						{
							return false;
						}
					}

					// 3. Tìm mã targetKey trong danh sách keys
					int keyPos = json.IndexOf("\"key\": \"" + targetKey + "\"");
					if (keyPos == -1) keyPos = json.IndexOf("\"key\":\"" + targetKey + "\"");

					if (keyPos == -1)
					{
						return false;
					}

					int blockStart = json.LastIndexOf('{', keyPos);
					int blockEnd = json.IndexOf('}', keyPos);
					if (blockStart == -1 || blockEnd == -1)
					{
						return false;
					}

					string keyBlock = json.Substring(blockStart, blockEnd - blockStart + 1);

					// Kiểm tra status LOCKED
					if (keyBlock.Contains("\"status\": \"LOCKED\"") || keyBlock.Contains("\"status\":\"LOCKED\""))
					{
						return false;
					}

					// Kiểm tra hạn sử dụng (expire_at)
					long expireAt = -1;
					int expIdx = keyBlock.IndexOf("\"expire_at\":");
					if (expIdx == -1) expIdx = keyBlock.IndexOf("\"expire_at\" :");
					if (expIdx != -1)
					{
						int numStart = expIdx + 12;
						while (numStart < keyBlock.Length && (keyBlock[numStart] == ' ' || keyBlock[numStart] == ':')) numStart++;
						int numEnd = numStart;
						while (numEnd < keyBlock.Length && (char.IsDigit(keyBlock[numEnd]) || keyBlock[numEnd] == '-')) numEnd++;
						long.TryParse(keyBlock.Substring(numStart, numEnd - numStart), out expireAt);
					}

					if (expireAt > 0 && expireAt < DateTimeOffset.UtcNow.ToUnixTimeMilliseconds())
					{
						return false;
					}

					// Xác thực thành công!
					// Trích xuất server riêng nếu có
					List<string> customServers = new List<string>();
					int srvIdx = keyBlock.IndexOf("\"servers\":");
					if (srvIdx == -1) srvIdx = keyBlock.IndexOf("\"servers\" :");
					if (srvIdx != -1)
					{
						int arrStart = keyBlock.IndexOf('[', srvIdx);
						int arrEnd = keyBlock.IndexOf(']', srvIdx);
						if (arrStart != -1 && arrEnd != -1 && arrEnd > arrStart)
						{
							string srvContent = keyBlock.Substring(arrStart + 1, arrEnd - arrStart - 1);
							string[] items = srvContent.Split(',');
							for (int k = 0; k < items.Length; k++)
							{
								string clean = items[k].Trim().Replace("\"", "");
								if (!string.IsNullOrEmpty(clean))
								{
									customServers.Add(clean);
								}
							}
						}
					}

					if (customServers.Count > 0)
					{
						UpdateServer.applyCustomServers(customServers);
					}
					else
					{
						UpdateServer.loadServersFromRemote();
					}

					return true;
				}
			}
		}
		catch (Exception)
		{
			return false;
		}
	}

	public static void syncAndValidateLicense()
	{
		try
		{
			string currentKey = getActiveKey();
			if (isLocalValidKey(currentKey))
			{
				isAuthorized = true;
				return;
			}
			bool ok = syncAndValidateSpecificKey(currentKey);
			if (ok)
			{
				isAuthorized = true;
			}
		}
		catch (Exception)
		{
		}
	}

	public static string getClientToken()
	{
		try
		{
			return "SIG_" + CLIENT_LINE + "_" + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
		}
		catch (Exception)
		{
			return "SIG_FAILSAFE";
		}
	}
}
