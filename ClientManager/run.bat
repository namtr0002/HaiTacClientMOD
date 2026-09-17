@echo off
chcp 65001 >nul
set "JAVA_EXE=java"
if exist "C:\Program Files\Java\jdk1.8.0_211\bin\java.exe" (
    set "JAVA_EXE=C:\Program Files\Java\jdk1.8.0_211\bin\java.exe"
)

if not exist "ClientManager.jar" (
    echo [INFO] ClientManager.jar chưa được tạo, tiến hành build...
    call build.bat
)

echo [INFO] Khởi chạy HaiTac Client Manager...
start "" "%JAVA_EXE%" -jar ClientManager.jar
