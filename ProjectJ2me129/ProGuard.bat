@echo off
setlocal EnableDelayedExpansion

cd /d "%~dp0"

rem ========================================================
rem Automatic Multi-Engine JDK Scanner (Prioritizes Java 8 JDK)
rem ========================================================
set "FOUND_JDK="

for /d %%D in (
    "C:\Program Files\Java\jdk1.8*"
    "C:\Program Files\Java\jdk-8*"
    "C:\Program Files (x86)\Java\jdk1.8*"
    "C:\Program Files (x86)\Java\jdk-8*"
    "D:\Program Files\Java\jdk1.8*"
    "D:\Program Files\Java\jdk-8*"
    "C:\Java\jdk1.8*"
    "D:\Java\jdk1.8*"
    "C:\Program Files\Eclipse Adoptium\jdk-8*"
    "C:\Program Files\Zulu\zulu-8*"
    "C:\Program Files\Amazon Corretto\jdk1.8*"
    "C:\Program Files\BellSoft\LibericaJDK-8*"
) do (
    if not defined FOUND_JDK if exist "%%D\bin\javac.exe" if exist "%%D\bin\jar.exe" set "FOUND_JDK=%%D"
)

if not defined FOUND_JDK (
    for /d %%D in (
        "C:\Program Files\Java\jdk-17*"
        "C:\Program Files\Java\jdk-21*"
        "C:\Program Files\Java\jdk-22*"
        "C:\Program Files\Java\jdk-11*"
        "C:\Program Files\Java\jdk*"
        "D:\Program Files\Java\jdk*"
        "C:\Program Files\Eclipse Adoptium\jdk*"
        "C:\Program Files\Eclipse Foundation\jdk*"
        "C:\Program Files\Zulu\zulu*"
        "C:\Program Files\Amazon Corretto\jdk*"
        "C:\Program Files\BellSoft\LibericaJDK*"
        "C:\Program Files\Microsoft\jdk*"
        "C:\Java\jdk*"
        "D:\Java\jdk*"
    ) do (
        if not defined FOUND_JDK if exist "%%D\bin\javac.exe" if exist "%%D\bin\jar.exe" set "FOUND_JDK=%%D"
    )
)

if not defined FOUND_JDK if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javac.exe" if exist "%JAVA_HOME%\bin\jar.exe" set "FOUND_JDK=%JAVA_HOME%"
)

set "JAVA_CMD=java"
if defined FOUND_JDK (
    set "JAVA_HOME=%FOUND_JDK%"
    set "PATH=%FOUND_JDK%\bin;%PATH%"
    set "JAVA_CMD=%FOUND_JDK%\bin\java.exe"
    echo [*] Using Java: %JAVA_HOME%
)

"%JAVA_CMD%" -jar "proguard-7.6.0\lib\proguard.jar" @"ProGuardVIP.pro"
