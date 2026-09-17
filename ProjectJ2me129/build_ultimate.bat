@echo off
setlocal EnableDelayedExpansion

rem Luon dam bao thu muc lam viec la thu muc chua file bat nay
cd /d "%~dp0"

title HTTH J2ME ULTIMATE NATIVE PACKER AND OBFUSCATOR v6.0

echo ========================================================
echo        HaiTacTiHon J2ME Ultimate Build Pipeline
echo ========================================================

rem ========================================================
rem Automatic Multi-Engine JDK Scanner (Prioritizes Java 8 JDK)
rem ========================================================
set "FOUND_JDK="

rem 1. Quet tim JDK 8 tren tat ca cac o dia va thu muc pho bien
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

rem 2. Quet tim cac JDK khac (17, 21, 22, 11, v.v.)
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

rem 3. Kiem tra bien moi truong JAVA_HOME / JDK_HOME hien tai
if not defined FOUND_JDK if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javac.exe" if exist "%JAVA_HOME%\bin\jar.exe" set "FOUND_JDK=%JAVA_HOME%"
)
if not defined FOUND_JDK if defined JDK_HOME (
    if exist "%JDK_HOME%\bin\javac.exe" if exist "%JDK_HOME%\bin\jar.exe" set "FOUND_JDK=%JDK_HOME%"
)

rem 4. Fallback: Tim javac.exe tu PATH
if not defined FOUND_JDK (
    for %%X in (javac.exe) do (
        set "FOUND_JAVAC=%%~$PATH:X"
        if defined FOUND_JAVAC (
            for %%P in ("!FOUND_JAVAC!\..\..") do (
                if exist "%%~fP\bin\jar.exe" set "FOUND_JDK=%%~fP"
            )
        )
    )
)

set "JAVAC_CMD=javac"
set "JAR_CMD=jar"
set "JAVA_CMD=java"

if defined FOUND_JDK (
    set "JAVA_HOME=%FOUND_JDK%"
    set "PATH=%FOUND_JDK%\bin;%PATH%"
    set "JAVAC_CMD=%FOUND_JDK%\bin\javac.exe"
    set "JAR_CMD=%FOUND_JDK%\bin\jar.exe"
    set "JAVA_CMD=%FOUND_JDK%\bin\java.exe"
    echo [+] Auto-detected Valid JDK: %JAVA_HOME%
) else (
    echo [!] WARNING: No JDK with javac/jar detected. Relying on default system PATH...
)

rem Kiem tra thu vien Javassist
set "JAVASSIST_JAR="
if exist "lib\javassist.jar" (
    set "JAVASSIST_JAR=lib\javassist.jar"
) else if exist "lib\javassist-3.30.2-GA.jar" (
    set "JAVASSIST_JAR=lib\javassist-3.30.2-GA.jar"
) else if exist "%USERPROFILE%\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar" (
    set "JAVASSIST_JAR=%USERPROFILE%\.m2\repository\org\javassist\javassist\3.30.2-GA\javassist-3.30.2-GA.jar"
)

echo [*] Step 1: Cleaning previous build artifacts and lingering Java processes...
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1
timeout /t 1 /nobreak >nul 2>&1
if exist "build" rmdir /S /Q "build" 2>nul
if exist "dist" rmdir /S /Q "dist" 2>nul
mkdir "build\classes" 2>nul
mkdir "build\encodetool" 2>nul
mkdir "dist" 2>nul

echo [*] Step 2: Compiling J2ME source code...
dir /s /b "src\*.java" > "build\sources.txt"
"%JAVAC_CMD%" -encoding UTF-8 -source 8 -target 8 -cp "lib\cldc_1.1.jar;lib\midp_2.1.jar;lib\jsr120_1.1.jar;lib\jsr135_1.2.jar;lib\asm-9.5.jar" -d "build\classes" @"build\sources.txt"
if errorlevel 1 (
    echo [!] J2ME Compilation failed with Java 8 target! Fallback to standard javac...
    "%JAVAC_CMD%" -encoding UTF-8 -cp "lib\cldc_1.1.jar;lib\midp_2.1.jar;lib\jsr120_1.1.jar;lib\jsr135_1.2.jar;lib\asm-9.5.jar" -d "build\classes" @"build\sources.txt"
    if errorlevel 1 (
        echo [!] J2ME Compilation failed completely!
        exit /b 1
    )
)

echo [*] Step 3: Bundling visual assets and resources directly from src...
if exist "src\x1" xcopy /e /y /i /q "src\x1" "build\classes\x1" >nul 2>&1
if exist "src\mfont" xcopy /e /y /i /q "src\mfont" "build\classes\mfont" >nul 2>&1
if exist "src\icon.png" copy /y "src\icon.png" "build\classes\icon.png" >nul 2>&1
if exist "src\iconeng.png" copy /y "src\iconeng.png" "build\classes\iconeng.png" >nul 2>&1
if exist "src\icontet.png" copy /y "src\icontet.png" "build\classes\icontet.png" >nul 2>&1
if exist "src\1" copy /y "src\1" "build\classes\1" >nul 2>&1
if exist "src\2" copy /y "src\2" "build\classes\2" >nul 2>&1
if exist "src\3" copy /y "src\3" "build\classes\3" >nul 2>&1
if exist "src\4" copy /y "src\4" "build\classes\4" >nul 2>&1
if exist "src\config.txt" copy /y "src\config.txt" "build\classes\config.txt" >nul 2>&1

echo [*] Step 4: Compiling Security Encodetool suite...
set "ENCODE_CP=lib\asm-9.5.jar;build\classes"
if defined JAVASSIST_JAR set "ENCODE_CP=%JAVASSIST_JAR%;lib\asm-9.5.jar;build\classes"
"%JAVAC_CMD%" -encoding UTF-8 -cp "%ENCODE_CP%" -d "build\encodetool" encodetool\*.java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [!] Encodetool compilation warning, falling back to simple compile...
    "%JAVAC_CMD%" -encoding UTF-8 -cp "lib\asm-9.5.jar;build\classes" -d "build\encodetool" encodetool\GenerateDict.java encodetool\StringHexXorEncryptor.java >nul 2>&1
)

echo [*] Step 5: Generating Lightweight Obfuscation Dictionaries (a b c d ..)...
if exist "build\encodetool\encodetool\GenerateDict.class" (
    "%JAVA_CMD%" -cp "build\encodetool" encodetool.GenerateDict 10000 OBF_ULTIMATE.txt
    "%JAVA_CMD%" -cp "build\encodetool" encodetool.GenerateDict 10000 OBF.txt
)

echo [*] Step 6: Injecting Opaque Guards and Decoy Branches into Real Code...
if exist "build\obf_classes" rmdir /s /q "build\obf_classes" 2>nul
mkdir "build\obf_classes" 2>nul
xcopy /e /y /i /q "build\classes" "build\obf_classes" >nul 2>&1
if exist "build\encodetool\encodetool\FakeLogicInjector.class" (
    "%JAVA_CMD%" -cp "lib\asm-9.5.jar;build\encodetool" encodetool.FakeLogicInjector "build\obf_classes"
)

echo [*] Step 7: Applying Multi-Cipher String Encryption (Preserving '' and "")...
if exist "build\encodetool\encodetool\StringHexXorEncryptor.class" (
    "%JAVA_CMD%" -cp "lib\javassist.jar;lib\asm-9.5.jar;build\encodetool" encodetool.StringHexXorEncryptor "build\obf_classes"
)

echo [*] Step 8: Packaging raw JAR with J2ME MANIFEST.MF...
"%JAR_CMD%" cvfm "build\raw.jar" "manifest.mf" -C "build\obf_classes" . >nul

echo [*] Step 9: Obfuscating Bytecode with ProGuard 7.6.0 (Lightweight 'a b c d')...
"%JAVA_CMD%" -jar "proguard-7.6.0\lib\proguard.jar" @ProGuard_Ultimate.pro
if errorlevel 1 (
    echo [!] ProGuard Obfuscation failed!
    exit /b 1
)

echo [*] Step 10: Re-injecting Standard J2ME Manifest...
"%JAR_CMD%" ufm "dist\HaiTacTiHon129.jar" "manifest.mf" >nul 2>&1

echo [*] Step 11: Generating JAD Descriptor...
for %%F in ("dist\HaiTacTiHon129.jar") do set "JAR_SIZE=%%~zF"
echo MIDlet-1: Hai Tac Ti Hon 129, /icon.png, GameMidlet> "dist\HaiTacTiHon129.jad"
echo MIDlet-Jar-Size: %JAR_SIZE%>> "dist\HaiTacTiHon129.jad"
echo MIDlet-Jar-URL: HaiTacTiHon129.jar>> "dist\HaiTacTiHon129.jad"
echo MIDlet-Name: Hai Tac Ti Hon 129>> "dist\HaiTacTiHon129.jad"
echo MIDlet-Vendor: Teamobi>> "dist\HaiTacTiHon129.jad"
echo MIDlet-Version: 1.2.9>> "dist\HaiTacTiHon129.jad"
echo MicroEdition-Configuration: CLDC-1.1>> "dist\HaiTacTiHon129.jad"
echo MicroEdition-Profile: MIDP-2.0>> "dist\HaiTacTiHon129.jad"
copy /y "dist\HaiTacTiHon129.jad" "HaiTacTiHon129.jad" >nul

echo [*] Step 12: Copying final deliverable JAR...
copy /y "dist\HaiTacTiHon129.jar" "HaiTacTiHon129.jar" >nul

echo ========================================================
echo [+] J2ME BUILD ULTIMATE COMPLETED SUCCESSFULLY!
echo [+] Output Deliverables:
echo     - dist\HaiTacTiHon129.jar (JAR Game)
echo     - dist\HaiTacTiHon129.jad (JAD Descriptor)
echo     - HaiTacTiHon129.jar
echo     - HaiTacTiHon129.jad
echo ========================================================
