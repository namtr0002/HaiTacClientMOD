        }

        private static void ExtractAndDecryptZipIfExists(string suffix, string outputDir)
        {
            var asm = Assembly.GetExecutingAssembly();
            var resourceName = asm.GetManifestResourceNames()
                .FirstOrDefault(n => n.EndsWith(suffix, StringComparison.OrdinalIgnoreCase));

            if (resourceName == null)
                return;

            long resourceLength = 0;
            using (var stream = asm.GetManifestResourceStream(resourceName))
            {
                if (stream != null)
                    resourceLength = stream.Length;
            }

            string javaExe = Path.Combine(outputDir, "bin", "javaw.exe");
            if (!File.Exists(javaExe))
                javaExe = Path.Combine(outputDir, "bin", "java.exe");

            string verFile = Path.Combine(outputDir, "runtime.ver");
            bool needExtract = true;

            if (Directory.Exists(outputDir) && File.Exists(javaExe) && File.Exists(verFile))
            {
                try
                {
                    string cachedVer = File.ReadAllText(verFile).Trim();
                    if (cachedVer == resourceLength.ToString())
                    {
                        needExtract = false; // Cache trùng khớp kích thước, bỏ qua giải nén!
                    }
                }
                catch
                {
                    needExtract = true;
                }
            }

            if (needExtract)
            {
                try { if (Directory.Exists(outputDir)) Directory.Delete(outputDir, true); } catch { }
                Directory.CreateDirectory(outputDir);

                var data = TryReadEmbeddedResource(suffix);
                if (data == null)
                    return;

                string zipPath = Path.Combine(Path.GetTempPath(), Guid.NewGuid().ToString("N") + ".zip");
                try
                {
                    File.WriteAllBytes(zipPath, Decrypt(data));
                    ZipFile.ExtractToDirectory(zipPath, outputDir, true);
                    File.WriteAllText(verFile, resourceLength.ToString());
                }
                finally
                {
                    TryDeleteFile(zipPath);
                }
            }
        }

        private static void WriteDecryptedResourceIfExists(string suffix, string outputFile)
        {
            var data = TryReadEmbeddedResource(suffix);
            if (data == null)
                throw new FileNotFoundException($"Embedded resource not found: {suffix}");

            File.WriteAllBytes(outputFile, Decrypt(data));
        }

        private static byte[]? TryReadEmbeddedResource(string suffix)
        {
            var asm = Assembly.GetExecutingAssembly();
            var resourceName = asm.GetManifestResourceNames()
                .FirstOrDefault(n => n.EndsWith(suffix, StringComparison.OrdinalIgnoreCase));

            if (resourceName == null)
                return null;

            using var stream = asm.GetManifestResourceStream(resourceName);
            if (stream == null)
                return null;

            using var ms = new MemoryStream();
            stream.CopyTo(ms);
            return ms.ToArray();
        }

        private static byte[] Decrypt(byte[] packed)
        {
            if (packed.Length < 17)
                throw new InvalidDataException("Invalid encrypted payload.");

            byte[] iv = new byte[16];
            Buffer.BlockCopy(packed, 0, iv, 0, 16);

            byte[] cipherText = new byte[packed.Length - 16];
            Buffer.BlockCopy(packed, 16, cipherText, 0, cipherText.Length);

            using var aes = Aes.Create();
            aes.Key = Key;
            aes.IV = iv;
            aes.Mode = CipherMode.CBC;
            aes.Padding = PaddingMode.PKCS7;

            using var decryptor = aes.CreateDecryptor();
            return decryptor.TransformFinalBlock(cipherText, 0, cipherText.Length);
        }

        private static void CleanupOrphanedTempFolders(string currentTempRoot)
        {
            try
            {
                string tempPath = Path.GetTempPath();
                foreach (var dir in Directory.GetDirectories(tempPath, "HTTH_*"))
                {
                    if (dir.Equals(currentTempRoot, StringComparison.OrdinalIgnoreCase))
                        continue;
                    try
                    {
                        Directory.Delete(dir, true);
                    }
                    catch
                    {
                        // File locked (tiến trình khác đang chạy), bỏ qua an toàn
                    }
                }
            }
            catch { }
        }

        private static void TryDeleteDirectory(string path)
        {
            for (int i = 0; i < 5; i++)
            {
                try
                {
                    if (Directory.Exists(path))
                        Directory.Delete(path, true);
                    return;
                }
                catch
                {
                    System.Threading.Thread.Sleep(150);
                }
            }
        }

        private static void TryDeleteFile(string path)
        {
            for (int i = 0; i < 5; i++)
            {
                try
                {
                    if (File.Exists(path))
                        File.Delete(path);
                    return;
                }
                catch
                {
                    System.Threading.Thread.Sleep(100);
                }
            }
        }
    }
}