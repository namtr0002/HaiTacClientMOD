@echo off
setlocal
cd /d "C:\DepLor\HTTH\Team"

echo ========================================================
echo [1/3] DANG BUILD ANDROID APK (IL2CPP ARM64+ARMv7)...
echo ========================================================
cd /d "C:\DepLor\HTTH\Team\ProjectUnity129"
call build_android.bat
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build Android APK that bai! Error code: %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Build Android APK thanh cong!
echo.

echo ========================================================
echo [2/3] DANG BUILD PC WINDOWS STANDALONE (IL2CPP x64)...
echo ========================================================
cd /d "C:\DepLor\HTTH\Team\ProjectUnity129"
call build_pc.bat
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build PC Windows that bai! Error code: %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Build PC Windows thanh cong!
echo.

echo ========================================================
echo [3/3] DANG BUILD J2ME JAR (OBFUSCATED ULTIMATE)...
echo ========================================================
cd /d "C:\DepLor\HTTH\Team\ProjectJ2me129"
call build_ultimate.bat
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build J2ME JAR that bai! Error code: %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Build J2ME JAR thanh cong!
echo.

echo ========================================================
echo [HOAN TAT] DA BUILD XONG TOAN BO: APK, PC VA JAR!
echo ========================================================
exit /b 0
