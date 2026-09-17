@echo off
echo =====================================================
echo   BUILDING ENCRYPTED ZERO-EXPOSURE EXE PACKAGE (HaiTacZ129)
echo =====================================================

call gradlew.bat desktop:jar
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Desktop build failed!
    pause
    exit /b %ERRORLEVEL%
)

if not exist "dist" mkdir "dist"
copy /Y "desktop\build\libs\HaiTacTiHon-Desktop.jar" "dist\HaiTacTiHon-Desktop.jar" > NUL

echo.
echo [ENCODE - STAGE 1] Running ProGuard Bytecode Obfuscation and Fake Logic Encryption...
if exist "C:\Program Files\Java\jdk-17\bin\java.exe" (
    "C:\Program Files\Java\jdk-17\bin\java.exe" -jar encode\proguard-7.6.0\lib\proguard.jar -injars dist\HaiTacTiHon-Desktop.jar -outjars dist\HaiTacTiHon-Obfuscated.jar -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.base.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.desktop.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.datatransfer.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.logging.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.management.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.naming.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.prefs.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\java.sql.jmod" -libraryjars "C:\Program Files\Java\jdk-17\jmods\jdk.unsupported.jmod" @proguard-rules.pro
) else (
    java -jar encode\proguard-7.6.0\lib\proguard.jar -injars dist\HaiTacTiHon-Desktop.jar -outjars dist\HaiTacTiHon-Obfuscated.jar @proguard-rules.pro
)

echo.
echo [ENCODE - STAGE 2] Packaging Bundled Native Runtime Directory...

set JPACKAGE_CMD=
if exist "C:\Program Files\Java\jdk-22\bin\jpackage.exe" (
    set JPACKAGE_CMD="C:\Program Files\Java\jdk-22\bin\jpackage.exe"
) else if exist "C:\Program Files\Java\jdk-17\bin\jpackage.exe" (
    set JPACKAGE_CMD="C:\Program Files\Java\jdk-17\bin\jpackage.exe"
) else (
    where jpackage >nul 2>null
    if %ERRORLEVEL% EQU 0 set JPACKAGE_CMD=jpackage
)

if defined JPACKAGE_CMD (
    if exist "dist\release" rd /s /q "dist\release"
    if not exist "dist\input" mkdir "dist\input"
    copy /Y "dist\HaiTacTiHon-Obfuscated.jar" "dist\input\HaiTacTiHon-Obfuscated.jar" > NUL

    %JPACKAGE_CMD% --type app-image --name HaiTacZ129 --input dist\input --main-jar HaiTacTiHon-Obfuscated.jar --main-class DesktopLauncher --add-modules java.base,java.desktop,java.logging,java.naming,java.management,java.sql,jdk.unsupported,jdk.zipfs --dest dist\release
    
    if exist "dist\release\HaiTacZ129" (
        if not exist "dist\release\HaiTacZ129\Data" mkdir "dist\release\HaiTacZ129\Data"
        if not exist "dist\release\HaiTacZ129\Data\rms" mkdir "dist\release\HaiTacZ129\Data\rms"
        if not exist "dist\release\HaiTacZ129\Data\downloads" mkdir "dist\release\HaiTacZ129\Data\downloads"
        if not exist "dist\release\HaiTacZ129\Data\assets" mkdir "dist\release\HaiTacZ129\Data\assets"
        
        if exist "C:\Program Files\Java\jdk-22\bin\java.exe" (
            copy /Y "C:\Program Files\Java\jdk-22\bin\java.exe" "dist\release\HaiTacZ129\runtime\bin\java.exe" > NUL
            copy /Y "C:\Program Files\Java\jdk-22\bin\javaw.exe" "dist\release\HaiTacZ129\runtime\bin\javaw.exe" > NUL
        ) else if exist "C:\Program Files\Java\jdk-17\bin\java.exe" (
            copy /Y "C:\Program Files\Java\jdk-17\bin\java.exe" "dist\release\HaiTacZ129\runtime\bin\java.exe" > NUL
            copy /Y "C:\Program Files\Java\jdk-17\bin\javaw.exe" "dist\release\HaiTacZ129\runtime\bin\javaw.exe" > NUL
        )

        if exist "configbuild.txt" copy /Y "configbuild.txt" "dist\release\HaiTacZ129\configbuild.txt" > NUL

        rem Clean up exposed jar & loose DLL files from release directory while keeping app folder config
        if exist "dist\release\HaiTacZ129\app\HaiTacTiHon-Obfuscated.jar" del /f /q "dist\release\HaiTacZ129\app\HaiTacTiHon-Obfuscated.jar"
        if exist "dist\release\HaiTacZ129\native_shield.dll" del /f /q "dist\release\HaiTacZ129\native_shield.dll"
    )
) else (
    echo [ERROR] jpackage tool not found!
)

echo.
echo [ENCODE - STAGE 3] Compiling Clang C++ Native Engine Shield and AES Encryptor...
if exist "C:\Program Files\LLVM\bin\clang.exe" (
    "C:\Program Files\LLVM\bin\clang.exe" -O3 -shared -o "encode\native_shield.dll" encode\native_shield.c
)
if exist "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe" (
    "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe" /target:exe /optimize /out:encode\EncryptPayload.exe encode\EncryptPayload.cs > NUL
)

if exist "encode\EncryptPayload.exe" (
    encode\EncryptPayload.exe encode\native_shield.dll encode\native_shield.enc
    encode\EncryptPayload.exe dist\HaiTacTiHon-Obfuscated.jar encode\engine.enc
)

echo.
echo [ENCODE - STAGE 4] Compiling Encrypted AES-256 Win32 Launcher EXE...
if exist "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe" (
    "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe" /target:winexe /optimize /resource:encode\native_shield.enc,native_shield.enc /resource:encode\engine.enc,engine.enc /out:"dist\release\HaiTacZ129\HaiTacZ129.exe" encode\HaiTacZ129Launcher.cs > NUL
)

rem Final check: Ensure no loose .jar or .dll exists in dist/release/HaiTacZ129
if exist "dist\release\HaiTacZ129\app\HaiTacTiHon-Obfuscated.jar" del /f /q "dist\release\HaiTacZ129\app\HaiTacTiHon-Obfuscated.jar"
if exist "dist\release\HaiTacZ129\native_shield.dll" del /f /q "dist\release\HaiTacZ129\native_shield.dll"

if exist "dist\release\HaiTacZ129\HaiTacZ129.exe" (
    echo.
    echo =====================================================
    echo   [SUCCESS] Zero-Exposure Encrypted EXE Built Successfully!
    echo   Executable: dist/release/HaiTacZ129/HaiTacZ129.exe
    echo   Data Directory: dist/release/HaiTacZ129/Data/
    echo =====================================================
)

echo.
pause
