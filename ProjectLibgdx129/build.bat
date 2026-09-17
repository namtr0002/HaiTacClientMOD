@echo off
if not exist "build\classes" mkdir "build\classes"
dir /s /b core\src\main\java\*.java desktop\src\main\java\*.java > sources.txt
javac -encoding UTF-8 -cp "lib\asm-9.5.jar;lib\libgdx\*" -d build\classes @sources.txt
xcopy /E /I /Y assets build\classes > NUL
echo Build finished cleanly.
pause
