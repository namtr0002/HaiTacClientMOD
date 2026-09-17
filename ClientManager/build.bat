@echo off
setlocal enabledelayedexpansion

echo [INFO] Compiling ClientManager...

set "JAVA_BIN=javac"
set "JAR_BIN=jar"
if exist "C:\Program Files\Java\jdk1.8.0_211\bin\javac.exe" (
    set "JAVA_BIN=C:\Program Files\Java\jdk1.8.0_211\bin\javac.exe"
    set "JAR_BIN=C:\Program Files\Java\jdk1.8.0_211\bin\jar.exe"
)

if not exist "bin" mkdir "bin"

echo [1/3] Scanning sources...
dir /s /b src\*.java > sources.txt

echo [2/3] Compiling...
"%JAVA_BIN%" -encoding UTF-8 -d bin @sources.txt
if errorlevel 1 (
    echo [ERROR] Compilation failed!
    if exist sources.txt del sources.txt
    exit /b 1
)
if exist sources.txt del sources.txt

echo [3/3] Packaging ClientManager.jar...
"%JAR_BIN%" cfe ClientManager.jar com.haitac.manager.Main -C bin .

if errorlevel 1 (
    echo [ERROR] JAR packaging failed!
    exit /b 1
)

echo ========================================================
echo [SUCCESS] ClientManager.jar built successfully!
echo ========================================================
exit /b 0
