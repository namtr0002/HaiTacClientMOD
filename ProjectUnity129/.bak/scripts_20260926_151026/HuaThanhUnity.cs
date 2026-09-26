using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using UnityEngine;

/// <summary>
/// Hóa Thần Bảo Mật Unity (HuaThanh / HTTH Security Core Engine)
/// Xử lý giải mã chuỗi Byte/Hex xoay vòng, nạp payload logic mã hóa vào RAM.
/// </summary>
public static class HuaThanhUnity
{
    private static readonly byte[] DEFAULT_KEY = new byte[] { 0x48, 0x54, 0x54, 0x48, 0x5F, 0x56, 0x49, 0x50, 0x32, 0x30, 0x32, 0x36, 0x40, 0x21, 0x23, 0x24 };
    private static bool _isInitialized = false;

    [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.BeforeSceneLoad)]
    public static void Initialize()
    {
        if (_isInitialized) return;
        _isInitialized = true;
        
        try
        {
            // Tự động kiểm tra và khởi tạo giải mã logic bảo mật
            LoadProtectedCorePayload();
        }
        catch (Exception ex)
        {
            Debug.LogError("[HTTH Security] Init failed: " + ex.Message);
        }
    }

    /// <summary>
    /// Giải mã chuỗi được mã hóa dưới dạng mảng byte XOR xoay vòng
    /// </summary>
    public static string DecryptByteString(byte[] encrypted, int salt)
    {
        if (encrypted == null || encrypted.Length == 0) return string.Empty;
        byte[] buffer = new byte[encrypted.Length];
        for (int i = 0; i < encrypted.Length; i++)
        {
            int key = (salt ^ (i * 31 + 0x5A)) & 0xFF;
            buffer[i] = (byte)(encrypted[i] ^ key);
        }
        return Encoding.UTF8.GetString(buffer);
    }

    /// <summary>
    /// Giải mã chuỗi được mã hóa dưới dạng chuỗi Hex XOR xoay vòng
    /// </summary>
    public static string DecryptHexString(string hex, int salt)
    {
        if (string.IsNullOrEmpty(hex)) return string.Empty;
        int length = hex.Length / 2;
        byte[] buffer = new byte[length];
        for (int i = 0; i < length; i++)
        {
            byte b = Convert.ToByte(hex.Substring(i * 2, 2), 16);
            int key = (salt ^ (i * 37 + 0xA5)) & 0xFF;
            buffer[i] = (byte)(b ^ key);
        }
        return Encoding.UTF8.GetString(buffer);
    }

    /// <summary>
    /// Nạp payload nhị phân mã hóa từ Resources
    /// </summary>
    public static byte[] LoadAndDecryptResource(string resourcePath, byte[] customKey = null)
    {
        try
        {
            TextAsset asset = Resources.Load<TextAsset>(resourcePath);
            if (asset == null) return null;
            byte[] data = asset.bytes;
            return DecryptAES(data, customKey ?? DEFAULT_KEY);
        }
        catch
        {
            return null;
        }
    }

    /// <summary>
    /// Giải mã AES-CBC với IV 16 bytes đầu
    /// </summary>
    public static byte[] DecryptAES(byte[] cipherData, byte[] key)
    {
        if (cipherData == null || cipherData.Length <= 16) return null;
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[cipherData.Length - 16];
        Buffer.BlockCopy(cipherData, 0, iv, 0, 16);
        Buffer.BlockCopy(cipherData, 16, cipherText, 0, cipherText.Length);

        using (Aes aes = Aes.Create())
        {
            aes.Key = key;
            aes.IV = iv;
            aes.Mode = CipherMode.CBC;
            aes.Padding = PaddingMode.PKCS7;
            using (ICryptoTransform decryptor = aes.CreateDecryptor())
            {
                return decryptor.TransformFinalBlock(cipherText, 0, cipherText.Length);
            }
        }
    }

    private static void LoadProtectedCorePayload()
    {
        // Core hook cho việc nạp dynamic IL / runtime logic payload
    }
}
