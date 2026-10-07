@echo off
SET "UNITY_EXE=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Unity.exe"
echo [INFO] Starting Unity iOS Xcode Export (IL2CPP ARM64)...
"%UNITY_EXE%" -projectPath "%~dp0." -buildTarget iOS -executeMethod BuildHelper.ExportIOSProject -batchmode -quit -logFile "%~dp0build_ios.log"
IF %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Export Xcode project completed! Check Builds\HaiTacZ\ folder.
) ELSE (
    echo [ERROR] Export Xcode failed! Check build_ios.log for details.
)
exit /b %ERRORLEVEL%
