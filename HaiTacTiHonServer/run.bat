@echo off
title HaiTacTiHonServer - Auto Restart
cd /d "%~dp0"

if exist "C:\Program Files\Java\jdk-22" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-22"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
) else if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
)
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
set "HTTH_RUNNER=1"

:Start
cls
echo ========================================================
echo [%time%] Starting HaiTacTiHonServer...
echo ========================================================

if exist "target\HaiTacTiHonServer-1.0-jar-with-dependencies.jar" (
    java -server -Xms1024M -Xmx2304M -Xss256k -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=45 -XX:G1ReservePercent=15 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1 -Dfile.encoding=UTF-8 -jar "target\HaiTacTiHonServer-1.0-jar-with-dependencies.jar"
) else if exist "HaiTacTiHonServer.exe" (
    HaiTacTiHonServer.exe
) else if exist "release\HaiTacTiHonServer.exe" (
    cd release
    HaiTacTiHonServer.exe
    cd ..
) else (
    echo [!] No compiled JAR or EXE found. Running via build.bat first...
    call build.bat
    if exist "target\HaiTacTiHonServer-1.0-jar-with-dependencies.jar" (
        java -server -Xms1024M -Xmx2304M -Xss256k -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=45 -XX:G1ReservePercent=15 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1 -Dfile.encoding=UTF-8 -jar "target\HaiTacTiHonServer-1.0-jar-with-dependencies.jar"
    )
)

echo.
echo ========================================================
echo [%time%] Server stopped or maintenance finished.
echo Restarting server in 3 seconds... (Press Ctrl+C to exit)
echo ========================================================
ping -n 4 127.0.0.1 >nul
goto Start