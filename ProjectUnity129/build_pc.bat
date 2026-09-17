@echo off
SET "UNITY_EXE=C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Unity.exe"
"%UNITY_EXE%" -projectPath "%~dp0." -buildTarget StandaloneWindows64 -executeMethod BuildHelper.BuildWindows -batchmode -quit -logFile "%~dp0build_pc.log"
exit /b %ERRORLEVEL%
