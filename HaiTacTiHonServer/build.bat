@echo off
title HaiTacTiHonServer - Build
cd /d "%~dp0"

if exist "C:\Program Files\Java\jdk-22" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-22"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
) else if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
)

if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ========================================================
echo Building HaiTacTiHonServer with Maven...
echo ========================================================

call mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo [BUILD SUCCESS] HaiTacTiHonServer packaged successfully!
    echo Output: target\HaiTacTiHonServer-1.0-jar-with-dependencies.jar
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo [BUILD ERROR] Maven build failed with error code %ERRORLEVEL%
    echo ========================================================
)
