@echo off
setlocal EnableExtensions EnableDelayedExpansion

title HTTH ULTIMATE NATIVE BUILDER v6.0

set "APP_NAME=HaiTacTiHonTool"
set "VERSION=1.0-SNAPSHOT"

set "BUILD_DIR=target"
set "RELEASE_DIR=release"
set "RUNTIME_DIR=runtime"
set "CSHARP_DIR=src\main\csharp"
set "RESOURCE_DIR=%CSHARP_DIR%\Resources"
set "PUBLISH_DIR=%CSHARP_DIR%\bin\Release\net9.0-windows\win-x64\publish"

set "MAIN_JAR=%~dp0%BUILD_DIR%\%APP_NAME%-%VERSION%.jar"
set "STUB_JAR=%~dp0%BUILD_DIR%\%APP_NAME%-Stub.jar"
set "MODULES="
set "MAIN_JAR_FOUND="

REM Always prefer a modern JDK (22 > 21 > 17) regardless of system JAVA_HOME
if exist "C:\Program Files\Java\jdk-22" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-22"
) else if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
) else if not defined JAVA_HOME (
    for /f "delims=" %%I in ('where java.exe 2^>nul') do (
        set "JAVA_HOME=%%~dpI.."
    )
)

set "MVN_CMD="
if exist "C:\Program Files\NetBeans-20\netbeans\java\maven\bin\mvn.cmd" (
    set "MVN_CMD=C:\Program Files\NetBeans-20\netbeans\java\maven\bin\mvn.cmd"
) else if exist "C:\DepLor\LANGLA\new\server\environment\maven\bin\mvn.cmd" (
    set "MVN_CMD=C:\DepLor\LANGLA\new\server\environment\maven\bin\mvn.cmd"
) else if exist "C:\Users\DELL\AppData\Local\Programs\IntelliJ IDEA 2025.3.1.1\plugins\maven\lib\maven3\bin\mvn.cmd" (
    set "MVN_CMD=C:\Users\DELL\AppData\Local\Programs\IntelliJ IDEA 2025.3.1.1\plugins\maven\lib\maven3\bin\mvn.cmd"
) else (
    set "MVN_CMD=mvn"
)

set "JAVASSIST_JAR="
if exist "lib\javassist.jar" (
    set "JAVASSIST_JAR=lib\javassist.jar"
) else if exist "lib\javassist-3.30.2-GA.jar" (
    set "JAVASSIST_JAR=lib\javassist-3.30.2-GA.jar"
) else if exist "%USERPROFILE%\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar" (
    set "JAVASSIST_JAR=%USERPROFILE%\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar"
) else if exist "C:\Users\DELL\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar" (
    set "JAVASSIST_JAR=C:\Users\DELL\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar"
) else (
    echo [*] Javassist jar not found locally. Auto-downloading to lib\javassist.jar...
    if not exist "lib" mkdir "lib"
    powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/javassist/javassist/3.30.2-GA/javassist-3.30.2-GA.jar', 'lib\javassist.jar')" >nul 2>&1
    if not exist "lib\javassist.jar" (
        curl -s -L -o "lib\javassist.jar" "https://repo1.maven.org/maven2/org/javassist/javassist/3.30.2-GA/javassist-3.30.2-GA.jar" >nul 2>&1
    )
    if exist "lib\javassist.jar" set "JAVASSIST_JAR=lib\javassist.jar"
)

echo.
echo [*] Cleaning old processes and folders...

taskkill /F /IM java.exe /T >nul 2>&1
taskkill /F /IM javaw.exe /T >nul 2>&1
taskkill /F /IM dotnet.exe /T >nul 2>&1
taskkill /F /IM msbuild.exe /T >nul 2>&1
taskkill /F /IM "%APP_NAME%.exe" /T >nul 2>&1

if exist "%BUILD_DIR%" rmdir /S /Q "%BUILD_DIR%"
if exist "%RELEASE_DIR%" rmdir /S /Q "%RELEASE_DIR%"
if exist "%RUNTIME_DIR%" rmdir /S /Q "%RUNTIME_DIR%"
if exist "%RESOURCE_DIR%" rmdir /S /Q "%RESOURCE_DIR%"
if exist "artifacts" rmdir /S /Q "artifacts"
if exist "temp_stub" rmdir /S /Q "temp_stub"

mkdir "%RESOURCE_DIR%" >nul 2>&1
mkdir artifacts >nul 2>&1

echo.
echo [*] Step 0 - Generating dictionary...

"%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 encodetool\GenerateDict.java
if errorlevel 1 goto fail

"%JAVA_HOME%\bin\java.exe" -cp . encodetool.GenerateDict 12000 dictionary.txt
if errorlevel 1 goto fail

echo.
echo [*] Step 1 - Maven package...

call "%MVN_CMD%" clean package -DskipTests
if errorlevel 1 goto fail

if not exist "%MAIN_JAR%" (
    for /f "delims=" %%A in ('dir /b /a-d "%BUILD_DIR%\%APP_NAME%-*SNAPSHOT*.jar" 2^>nul') do (
        set "MAIN_JAR_FOUND=%%A"
    )
    if defined MAIN_JAR_FOUND set "MAIN_JAR=%BUILD_DIR%\%MAIN_JAR_FOUND%"
)

if not exist "%MAIN_JAR%" goto fail

echo.
echo [*] Step 1.5 - Injecting fake bytecode...

"%JAVA_HOME%\bin\javac.exe" -cp "%JAVASSIST_JAR%" "encodetool\FakeCode.java"
if errorlevel 1 goto fail

"%JAVA_HOME%\bin\java.exe" -cp "%JAVASSIST_JAR%;." encodetool.FakeCode "target\classes" 5
if errorlevel 1 goto fail

"%JAVA_HOME%\bin\jar.exe" uf "%MAIN_JAR%" -C "target\classes" .
if errorlevel 1 goto fail

echo.
echo [*] Step 2 - Creating stub...

mkdir temp_stub >nul 2>&1
pushd temp_stub >nul

"%JAVA_HOME%\bin\jar.exe" xf "%MAIN_JAR%"
if errorlevel 1 goto stub_fail

"%JAVA_HOME%\bin\jar.exe" cfe "%STUB_JAR%" com.deplor.haitactihontool.ui.Bootstrap .
if errorlevel 1 goto stub_fail

popd >nul

if not exist "%STUB_JAR%" goto fail

echo.
echo [*] Step 3 - Detecting Java modules...

set "MODULE_FILE=%TEMP%\htth_modules.txt"
if exist "%MODULE_FILE%" del /F /Q "%MODULE_FILE%"

call "%JAVA_HOME%\bin\jdeps.exe" --ignore-missing-deps --multi-release 22 --recursive --print-module-deps "%MAIN_JAR%" > "%MODULE_FILE%"
if errorlevel 1 goto fail

if not exist "%MODULE_FILE%" goto fail

set /p MODULES=<"%MODULE_FILE%"
del /F /Q "%MODULE_FILE%" >nul 2>&1

if not defined MODULES goto fail

echo [*] Modules: !MODULES!

echo.
echo [*] Step 4 - Building custom runtime...

"%JAVA_HOME%\bin\jlink.exe" --add-modules !MODULES!,jdk.crypto.ec,jdk.crypto.mscapi,jdk.crypto.cryptoki --strip-debug --compress zip-9 --no-header-files --no-man-pages --output "%RUNTIME_DIR%"
if errorlevel 1 goto fail

if not exist "%RUNTIME_DIR%\bin\java.exe" goto fail

echo.
echo [*] Step 5 - Compressing runtime...

powershell -NoProfile -Command "[System.Reflection.Assembly]::LoadWithPartialName('System.IO.Compression.FileSystem') >$null; [System.IO.Compression.ZipFile]::CreateFromDirectory('%RUNTIME_DIR%', 'artifacts\runtime.zip', [System.IO.Compression.CompressionLevel]::Optimal, $false)"
if errorlevel 1 goto fail

echo.
echo [*] Step 6 - Encrypting payloads...

"%JAVA_HOME%\bin\javac.exe" encodetool\Main.java
if errorlevel 1 goto fail

"%JAVA_HOME%\bin\java.exe" encodetool.Main "%STUB_JAR%" "%RESOURCE_DIR%\payload.enc"
if errorlevel 1 goto fail

"%JAVA_HOME%\bin\java.exe" encodetool.Main artifacts\runtime.zip "%RESOURCE_DIR%\runtime.enc"
if errorlevel 1 goto fail

echo.
echo [*] Step 7 - Building native wrapper...

pushd "%CSHARP_DIR%" >nul
dotnet publish HaiTacTiHonWrapper.csproj -c Release -r win-x64 -p:PublishAot=true -p:SelfContained=true -p:InvariantGlobalization=true -p:DebugType=None -p:DebugSymbols=false
if errorlevel 1 (
    popd >nul
    goto fail
)
popd >nul

echo.
echo [*] Step 8 - Finalizing release...

mkdir "%RELEASE_DIR%" >nul 2>&1

copy /Y "%PUBLISH_DIR%\HaiTacTiHonWrapper.exe" "%RELEASE_DIR%\%APP_NAME%.exe" >nul
if errorlevel 1 goto fail

REM Copy Data folder if exists
if exist "Data" (
    echo [*] Copying Data folder...

    if exist "%RELEASE_DIR%\Data" rmdir /S /Q "%RELEASE_DIR%\Data"

    xcopy "Data" "%RELEASE_DIR%\Data\" /E /I /Y /Q
    if errorlevel 1 goto fail
)

echo.
echo [*] Cleaning temporary files...

if exist modules.txt del /F /Q modules.txt
if exist dictionary.txt del /F /Q dictionary.txt
if exist artifacts rmdir /S /Q artifacts
if exist temp_stub rmdir /S /Q temp_stub
if exist runtime rmdir /S /Q runtime

echo.
echo [OK] BUILD SUCCESSFUL
echo [OK] OUTPUT: %RELEASE_DIR%\%APP_NAME%.exe
pause
exit /b 0

:stub_fail
popd >nul

:fail
echo.
echo [!] BUILD FAILED
pause
exit /b 1