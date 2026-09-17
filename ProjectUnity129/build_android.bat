@echo off
REM =========================================================
REM  build_android.bat  -  ProjectUnity129 Android Build
REM  Fix: Buoc Java 22 khoi PATH, dung OpenJDK 11 cua Unity
REM =========================================================

SET UNITY_EXE=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Unity.exe
SET UNITY_JDK=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Data\PlaybackEngines\AndroidPlayer\OpenJDK

REM -- Override JAVA_HOME sang JDK 11 cua Unity
SET JAVA_HOME=%UNITY_JDK%

REM -- Dua JDK 11 len dau PATH, loai bo JDK 22 neu co
SET PATH=%UNITY_JDK%\bin;%PATH%
SET PATH=%PATH:C:\Program Files\Java\jdk-22\bin;=%

REM -- Tao repositories.cfg de sdkmanager khong fetch remote (optional)
IF NOT EXIST "%USERPROFILE%\.android" MKDIR "%USERPROFILE%\.android"
IF NOT EXIST "%USERPROFILE%\.android\repositories.cfg" ECHO.>"%USERPROFILE%\.android\repositories.cfg"

REM -- Xac nhan Java version dang dung
echo [INFO] JAVA_HOME = %JAVA_HOME%
"%UNITY_JDK%\bin\java.exe" -version

REM -- Build Android APK
echo.
echo [INFO] Starting Unity Android build...
"%UNITY_EXE%" ^
  -projectPath "%~dp0." ^
  -buildTarget Android ^
  -executeMethod BuildHelper.BuildAndroid ^
  -batchmode ^
  -quit ^
  -logFile "%~dp0build_android.log"

IF %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Build completed! Check Builds\ folder.
) ELSE (
    echo [ERROR] Build failed! See build_android.log for details.
)
exit /b %ERRORLEVEL%
