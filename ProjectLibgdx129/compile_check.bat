@echo off
cd /d "%~dp0"
if not exist "build\classes" mkdir "build\classes"
dir /s /b core\src\main\java\*.java > sources.txt
javac -encoding UTF-8 -cp "lib\asm-9.5.jar;lib\libgdx\*" -d build\classes @sources.txt
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] LIBGDX COMPILED CLEANLY WITH ZERO ERRORS!
) else (
    echo [ERROR] FAILED WITH CODE %ERRORLEVEL%
)
del sources.txt 2>nul
