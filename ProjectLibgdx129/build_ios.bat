@echo off
echo =======================================================
echo   BUILDING OBFUSCATED IOS RELEASE IPA (ROBOVM & NATIVE)
echo =======================================================
call gradlew.bat ios:createIPA
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] iOS IPA build requires macOS with Xcode environment.
    pause
    exit /b %ERRORLEVEL%
)

if not exist "dist" mkdir "dist"
if exist "ios\build\robovm\IOSLauncher.ipa" (
    copy /Y "ios\build\robovm\IOSLauncher.ipa" "dist\HaiTacTiHon-Release.ipa" > NUL
    echo.
    echo [SUCCESS] iOS Release IPA built and copied to 'dist/HaiTacTiHon-Release.ipa'
)

echo.
pause
