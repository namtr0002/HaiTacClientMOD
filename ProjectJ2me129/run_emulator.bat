@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"
title AngelChip Emulator - HaiTacTiHon129

rem ========================================================
rem Find Java 8 Runtime (Prefer javaw for clean GUI window)
rem ========================================================
set "JAVA_EXE="
set "JAVAW_EXE="

if exist "C:\Program Files\Java\jdk1.8.0_211\bin\javaw.exe" (
    set "JAVAW_EXE=C:\Program Files\Java\jdk1.8.0_211\bin\javaw.exe"
    set "JAVA_EXE=C:\Program Files\Java\jdk1.8.0_211\bin\java.exe"
) else if exist "C:\Program Files\Java\jre1.8.0_211\bin\javaw.exe" (
    set "JAVAW_EXE=C:\Program Files\Java\jre1.8.0_211\bin\javaw.exe"
    set "JAVA_EXE=C:\Program Files\Java\jre1.8.0_211\bin\java.exe"
) else (
    for /d %%D in ("C:\Program Files\Java\jdk1.8*" "C:\Program Files\Java\jre1.8*" "D:\Program Files\Java\jdk1.8*") do (
        if not defined JAVAW_EXE if exist "%%D\bin\javaw.exe" (
            set "JAVAW_EXE=%%D\bin\javaw.exe"
            set "JAVA_EXE=%%D\bin\java.exe"
        )
    )
)

if not defined JAVA_EXE if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
    if exist "%JAVA_HOME%\bin\javaw.exe" set "JAVAW_EXE=%JAVA_HOME%\bin\javaw.exe"
)

if not defined JAVA_EXE set "JAVA_EXE=java"
if not defined JAVAW_EXE set "JAVAW_EXE=javaw"

rem ========================================================
rem Target Determination
rem ========================================================
set "RUN_TARGET=%1"

if "%RUN_TARGET%"=="" (
    if exist "HaiTacTiHon129.jad" (
        set "RUN_TARGET=jad"
    ) else if exist "HaiTacTiHon129.jar" (
        set "RUN_TARGET=jar"
    ) else (
        set "RUN_TARGET=src"
    )
)

echo ========================================================
echo     AngelChip Emulator Launcher - Hai Tac Ti Hon 129
echo ========================================================

if /i "%RUN_TARGET%"=="src" (
    echo [+] Launching AngelChip Emulator from source: build\classes [GameMidlet]...
    start "" "%JAVAW_EXE%" -Dfile.encoding=UTF-8 -cp "AngelChipEmulator_AutoSleep.jar;build\classes" org.microemu.app.Main --propertiesjad HaiTacTiHon129.jad GameMidlet
) else if /i "%RUN_TARGET%"=="jar" (
    echo [+] Launching AngelChip Emulator with HaiTacTiHon129.jar...
    start "" "%JAVAW_EXE%" -Dfile.encoding=UTF-8 -jar AngelChipEmulator_AutoSleep.jar HaiTacTiHon129.jar
) else (
    echo [+] Launching AngelChip Emulator with HaiTacTiHon129.jad...
    start "" "%JAVAW_EXE%" -Dfile.encoding=UTF-8 -jar AngelChipEmulator_AutoSleep.jar HaiTacTiHon129.jad
)

echo [+] Emulator window opened successfully!
ping 127.0.0.1 -n 2 >nul 2>&1
exit /b 0
