@echo off
if not exist build\classes mkdir build\classes
dir /s /b src\*.java > build\sources.txt
"C:\Program Files\Java\jdk1.8.0_211\bin\javac.exe" -encoding UTF-8 -source 8 -target 8 -cp "lib\cldc_1.1.jar;lib\midp_2.1.jar;lib\jsr120_1.1.jar;lib\jsr135_1.2.jar;lib\asm-9.5.jar" -d build\classes @build\sources.txt > compile_errors.log 2>&1
echo JAVAC EXIT CODE: %ERRORLEVEL%
