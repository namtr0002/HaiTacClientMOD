using System;
using System.IO;
using System.IO.Compression;
using UnityEditor;
using UnityEngine;

/// <summary>
/// HaiTac Build Helper - build Windows, Android & iOS Xcode player từ menu
/// Đặt trong thư mục Assets/Editor/ để Unity nhận diện là Editor script
/// </summary>
public static class BuildHelper
{
    [MenuItem("HaiTacTools/Build Windows Player (x64)", false, 10)]
    public static void BuildWindows()
    {
        string buildPath = "Builds/HaiTacZ129/HaiTacZ129.exe";
        string[] scenes = GetBuildScenes();

        Debug.Log("=== Starting HaiTac Windows Build ===");
        Debug.Log($"Output: {buildPath}");
        Debug.Log($"Scenes: {string.Join(", ", scenes)}");

        BuildPlayerOptions opts = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = buildPath,
            target = BuildTarget.StandaloneWindows64,
            options = BuildOptions.None
        };

        var report = BuildPipeline.BuildPlayer(opts);

        if (report.summary.result == UnityEditor.Build.Reporting.BuildResult.Succeeded)
        {
            Debug.Log($"=== Windows Build THÀNH CÔNG! === Output: {buildPath}");
            if (!Application.isBatchMode) EditorUtility.RevealInFinder(buildPath);
        }
        else
        {
            Debug.LogError($"=== Windows Build THẤT BẠI! === Errors: {report.summary.totalErrors}");
            if (Application.isBatchMode) EditorApplication.Exit(1);
        }
    }

    [MenuItem("HaiTacTools/Android/1. Build Android APK (IL2CPP C++ Mã Hóa Tự Động)", false, 20)]
    public static void BuildAndroid()
    {
        // 1. Luôn kích hoạt mã hóa IL2CPP (C++ Native) & ARM64 + ARMv7
        PlayerSettings.SetScriptingBackend(BuildTargetGroup.Android, ScriptingImplementation.IL2CPP);
        PlayerSettings.Android.targetArchitectures = AndroidArchitecture.ARMv7 | AndroidArchitecture.ARM64;
        PlayerSettings.SetManagedStrippingLevel(BuildTargetGroup.Android, ManagedStrippingLevel.Minimal);

        // 2. FIX: Luôn dùng Embedded OpenJDK của Unity (JDK 11).
        HaiTacTools.AndroidJavaSetup.EnableUnityEmbeddedJdk();
        Debug.Log("[BuildAndroid] Đã chuyển sang Embedded OpenJDK 11 của Unity để tránh lỗi sdkmanager với JDK 22.");

        string outputDir = "Builds/HaiTacZ129_Android";
        if (!Directory.Exists(outputDir))
        {
            Directory.CreateDirectory(outputDir);
        }
        string buildPath = Path.Combine(outputDir, "HaiTacTiHon-v129.apk");
        string[] scenes = GetBuildScenes();

        Debug.Log("=== Starting HaiTac Android APK Build (IL2CPP Protected) ===");
        Debug.Log($"Output APK: {buildPath}");

        BuildPlayerOptions opts = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = buildPath,
            target = BuildTarget.Android,
            options = BuildOptions.None
        };

        var report = BuildPipeline.BuildPlayer(opts);

        if (report.summary.result == UnityEditor.Build.Reporting.BuildResult.Succeeded)
        {
            Debug.Log($"=== Android Build THÀNH CÔNG! === Output: {buildPath}");
            if (!Application.isBatchMode)
            {
                EditorUtility.RevealInFinder(buildPath);
                EditorUtility.DisplayDialog("Build APK Thành Công", $"File APK IL2CPP đã được tạo tại:\n{buildPath}\n\n✓ Toàn bộ code C# đã được mã hóa sang C++ Native\n✓ Tương thích cả máy 32-bit và 64-bit", "OK");
            }
        }
        else
        {
            Debug.LogError($"=== Android Build THẤT BẠI! === Errors: {report.summary.totalErrors}");
            if (!Application.isBatchMode)
            {
                EditorUtility.DisplayDialog("Build APK Thất Bại", $"Đã có {report.summary.totalErrors} lỗi xảy ra trong quá trình build. Vui lòng kiểm tra Console.", "OK");
            }
            else
            {
                EditorApplication.Exit(1);
            }
        }
    }

    [MenuItem("HaiTacTools/Android/2. Export Android Studio Project (IL2CPP C++)", false, 21)]
    public static void ExportAndroidProject()
    {
        PlayerSettings.SetScriptingBackend(BuildTargetGroup.Android, ScriptingImplementation.IL2CPP);
        PlayerSettings.Android.targetArchitectures = AndroidArchitecture.ARMv7 | AndroidArchitecture.ARM64;

        string exportDir = "Builds/HaiTacZ129_AndroidProject";
        if (!Directory.Exists(exportDir))
        {
            Directory.CreateDirectory(exportDir);
        }

        string[] scenes = GetBuildScenes();

        Debug.Log("=== Exporting Android Studio Project with IL2CPP ===");
        EditorUserBuildSettings.exportAsGoogleAndroidProject = true;

        BuildPlayerOptions opts = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = exportDir,
            target = BuildTarget.Android,
            options = BuildOptions.AcceptExternalModificationsToPlayer
        };

        var report = BuildPipeline.BuildPlayer(opts);

        if (report.summary.result == UnityEditor.Build.Reporting.BuildResult.Succeeded)
        {
            Debug.Log($"=== Export Project THÀNH CÔNG! === Thư mục: {exportDir}");
            EditorUtility.RevealInFinder(exportDir);
            EditorUtility.DisplayDialog("Export Thành Công", $"Project Android chứa toàn bộ mã C++ IL2CPP đã được xuất ra tại:\n{exportDir}", "OK");
        }
        else
        {
            Debug.LogError($"=== Export Project THẤT BẠI! === Errors: {report.summary.totalErrors}");
        }
    }

    [MenuItem("HaiTacTools/iOS/1. Export Xcode Project (IL2CPP ARM64 Chống Dịch Ngược)", false, 30)]
    public static void ExportIOSProject()
    {
        // 1. Cấu hình Scripting Backend & IL2CPP cho iOS
        PlayerSettings.SetScriptingBackend(BuildTargetGroup.iOS, ScriptingImplementation.IL2CPP);
        PlayerSettings.iOS.sdkVersion = iOSSdkVersion.DeviceSDK;
        PlayerSettings.iOS.targetOSVersionString = "12.0";
        PlayerSettings.SetManagedStrippingLevel(BuildTargetGroup.iOS, ManagedStrippingLevel.Minimal);

        // 2. Cấu hình Màn hình ngang Landscape & Bundle ID
        PlayerSettings.defaultInterfaceOrientation = UIOrientation.LandscapeLeft;
        PlayerSettings.allowedAutorotateToPortrait = false;
        PlayerSettings.allowedAutorotateToPortraitUpsideDown = false;
        PlayerSettings.allowedAutorotateToLandscapeLeft = true;
        PlayerSettings.allowedAutorotateToLandscapeRight = true;
        PlayerSettings.SetApplicationIdentifier(BuildTargetGroup.iOS, "com.HaiTacZ129.HaiTacZ129");

        string exportDir = "Builds/XCodeHaiTacZ129";
        if (Directory.Exists(exportDir))
        {
            try
            {
                Directory.Delete(exportDir, true);
            }
            catch (Exception ex)
            {
                Debug.LogWarning("[ExportIOSProject] Không thể xóa thư mục cũ: " + ex.Message);
            }
        }

        string[] scenes = GetBuildScenes();

        Debug.Log("=== Exporting Xcode Project with IL2CPP (iOS ARM64) ===");
        Debug.Log($"Output Directory: {exportDir}");

        BuildPlayerOptions opts = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = exportDir,
            target = BuildTarget.iOS,
            options = BuildOptions.None
        };

        var report = BuildPipeline.BuildPlayer(opts);

        if (report.summary.result == UnityEditor.Build.Reporting.BuildResult.Succeeded)
        {
            Debug.Log($"=== Export Xcode Project THÀNH CÔNG! === Thư mục: {exportDir}");
            
            // Tự động nén thành file zip phục vụ GitHub Actions IPA build
            string zipPath = "Builds/XCodeHaiTacZ129.zip";
            CompressDirectoryToZip(exportDir, zipPath);

            if (!Application.isBatchMode)
            {
                EditorUtility.RevealInFinder(exportDir);
                EditorUtility.DisplayDialog("Export Xcode Thành Công", 
                    $"Project Xcode iOS đã được xuất ra tại:\n{exportDir}\n\nĐồng thời đã tự động tạo file:\n{zipPath}\n\n✓ Mã nguồn đã được bảo vệ qua OPS Obfuscator + IL2CPP Native ARM64\n✓ Bạn có thể chạy push_xcode_ios.bat để đẩy lên GitHub tự động build ra file IPA!", "OK");
            }
        }
        else
        {
            Debug.LogError($"=== Export Xcode Project THẤT BẠI! === Errors: {report.summary.totalErrors}");
            if (!Application.isBatchMode)
            {
                EditorUtility.DisplayDialog("Export Xcode Thất Bại", $"Đã có {report.summary.totalErrors} lỗi xảy ra trong quá trình export. Vui lòng kiểm tra Console.", "OK");
            }
            else
            {
                EditorApplication.Exit(1);
            }
        }
    }

    [MenuItem("HaiTacTools/iOS/2. Zip Xcode Project Ready For GitHub Actions", false, 31)]
    public static void ZipXcodeProject()
    {
        string exportDir = "Builds/XCodeHaiTacZ129";
        string zipPath = "Builds/XCodeHaiTacZ129.zip";

        if (!Directory.Exists(exportDir))
        {
            EditorUtility.DisplayDialog("Lỗi", $"Không tìm thấy thư mục {exportDir}!\nVui lòng chọn '1. Export Xcode Project' trước.", "OK");
            return;
        }

        CompressDirectoryToZip(exportDir, zipPath);
        EditorUtility.RevealInFinder(zipPath);
        EditorUtility.DisplayDialog("Nén Zip Thành Công", $"File zip đã được tạo tại:\n{zipPath}\n\nSẵn sàng để push lên GitHub Actions build IPA!", "OK");
    }

    private static void CompressDirectoryToZip(string sourceDir, string zipPath)
    {
        try
        {
            if (File.Exists(zipPath))
            {
                File.Delete(zipPath);
            }

            Debug.Log($"[BuildHelper] Đang nén thư mục {sourceDir} thành {zipPath}...");
            ZipFile.CreateFromDirectory(sourceDir, zipPath, System.IO.Compression.CompressionLevel.Optimal, false);
            Debug.Log($"[BuildHelper] Nén Zip thành công: {zipPath} ({new FileInfo(zipPath).Length / (1024 * 1024)} MB)");
        }
        catch (Exception ex)
        {
            Debug.LogError("[BuildHelper] Lỗi khi nén Zip: " + ex.Message);
        }
    }

    [MenuItem("HaiTacTools/Recompile Scripts Only", false, 50)]
    public static void RecompileScripts()
    {
        UnityEditor.Compilation.CompilationPipeline.RequestScriptCompilation();
        Debug.Log("Đã yêu cầu recompile scripts - chờ Unity compile xong...");
    }

    [MenuItem("HaiTacTools/Run Mobile Input & UI Tests", false, 60)]
    public static void RunInputTests()
    {
        Debug.Log("=== CHẠY TEST TỰ ĐỘNG MOBILE INPUT & KEYBOARD & UI ===");
        
        // 1. Test KeyMap
        int delKey = MyKeyMap.map(KeyCode.Delete);
        int escKey = MyKeyMap.map(KeyCode.Escape);
        int retKey = MyKeyMap.map(KeyCode.Return);
        int bsKey = MyKeyMap.map(KeyCode.Backspace);
        Debug.Log($"[TEST 1] KeyMap Delete={delKey} (Expect 127), Escape={escKey} (Expect -27), Return={retKey} (Expect -5), Backspace={bsKey} (Expect -8) -> " + 
            (delKey == 127 && escKey == -27 && retKey == -5 && bsKey == -8 ? "PASS" : "FAIL"));

        // 2. Test TField text sync & operations
        TField tf = new TField(0, 0, 100);
        tf.setText("TestString");
        bool passSet = tf.getText() == "TestString";
        tf.insertText("123");
        bool passIns = tf.getText() == "TestString123";
        tf.clear();
        bool passClr = tf.getText() == "TestString12";
        tf.clearAllText();
        bool passClrAll = tf.getText() == "";
        Debug.Log($"[TEST 2] TField Text Operations: setText={passSet}, insertText={passIns}, clear={passClr}, clearAll={passClrAll} -> " + 
            (passSet && passIns && passClr && passClrAll ? "PASS" : "FAIL"));

        // 3. Test Delete forward
        TField tf2 = new TField(0, 0, 100);
        tf2.setText("12345");
        tf2.keyPressed(14); // Left
        tf2.keyPressed(14);
        tf2.keyPressed(14);
        tf2.keyPressed(14);
        tf2.keyPressed(14); // Caret at 0
        tf2.keyPressed(127); // Delete forward
        bool passDelFwd = tf2.getText() == "2345";
        Debug.Log($"[TEST 3] TField Forward Delete: text={tf2.getText()} (Expect 2345) -> " + (passDelFwd ? "PASS" : "FAIL"));

        // 4. Test MobileInputManager Back key
        bool backResult = MobileInputManager.HandleBackKey();
        Debug.Log($"[TEST 4] MobileInputManager.HandleBackKey (No active keyboard) -> Result={backResult} (Expect False) -> " + (!backResult ? "PASS" : "FAIL"));

        // 5. Test InputDialog lifecycle
        InputDialog dlg = new InputDialog();
        dlg.setinfo("Nhap so", null, true, "TEST_DLG");
        bool dlgTfOpen = dlg.tfInput != null;
        dlg.closeDialog();
        bool dlgClosed = TField.currentTField == null && MobileInputManager.KeyboardState == MobileKeyboardState.Closed;
        Debug.Log($"[TEST 5] InputDialog Open & Close Lifecycle: tfInputCreated={dlgTfOpen}, currentTFieldCleared={dlgClosed} -> " + 
            (dlgTfOpen && dlgClosed ? "PASS" : "FAIL"));

        Debug.Log("=== HOÀN TẤT TEST MOBILE INPUT ===");
    }

    private static string[] GetBuildScenes()
    {
        var scenes = new string[]
        {
            "Assets/hs.unity"
        };

        var editorScenes = EditorBuildSettings.scenes;
        if (editorScenes != null && editorScenes.Length > 0)
        {
            var sceneList = new System.Collections.Generic.List<string>();
            foreach (var s in editorScenes)
            {
                if (s.enabled) sceneList.Add(s.path);
            }
            if (sceneList.Count > 0) scenes = sceneList.ToArray();
        }

        return scenes;
    }
}

public static class BuildScript
{
    public static void BuildWindows() => BuildHelper.BuildWindows();
    public static void BuildAndroid() => BuildHelper.BuildAndroid();
    public static void BuildIOS() => BuildHelper.ExportIOSProject();
    public static void ExportIOSProject() => BuildHelper.ExportIOSProject();
}

namespace HaiTacTools
{
    public static class AndroidJavaSetup
    {
        public static void EnableUnityEmbeddedJdk()
        {
            try
            {
                EditorPrefs.SetBool("JdkUseEmbedded", true);
            }
            catch (Exception)
            {
            }
        }
    }
}
