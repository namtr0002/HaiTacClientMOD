@echo off
cd /d "%~dp0.."
call "%~dp0..\push_xcode_ios.bat"
exit /b %ERRORLEVEL%
