@echo off
setlocal enabledelayedexpansion
title HTTH Unity Multi-Platform Build Pipeline

SET "UNITY_EXE=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Unity.exe"
SET "UNITY_JDK=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Data\PlaybackEngines\AndroidPlayer\OpenJDK"

SET "JAVA_HOME=%UNITY_JDK%"
SET "PATH=%UNITY_JDK%\bin;%PATH%"
SET "PATH=%PATH:C:\Program Files\Java\jdk-22\bin;=%"

echo ========================================================
echo        HTTH Unity Multi-Platform Build Pipeline
echo ========================================================
echo.
echo [1/3] Building PC (Windows Standalone x64)...
call "%~dp0build_pc.bat"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] PC Build that bai!
) else (
    echo [SUCCESS] PC Build thanh cong!
)

echo.
echo ========================================================
echo [2/3] Building Android APK (IL2CPP ARM64 + ARMv7)...
echo ========================================================
call "%~dp0build_android.bat"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Android Build that bai!
) else (
    echo [SUCCESS] Android Build thanh cong!
)

echo.
echo ========================================================
echo [3/3] Exporting iOS Xcode Project (ARM64)...
echo ========================================================
call "%~dp0build_ios.bat"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] iOS Export that bai!
) else (
    echo [SUCCESS] iOS Export thanh cong!
)

echo.
echo ========================================================
echo   TAT CA 3 NEN TANG (PC, ANDROID, IOS) DA BUILD XONG!
echo   Xem ket qua trong thu muc: Builds\
echo ========================================================
