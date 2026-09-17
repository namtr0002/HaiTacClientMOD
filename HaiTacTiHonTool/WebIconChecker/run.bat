@echo off
chcp 65001 >nul
title Hải Tặc Tí Hôn - Web Icon Checker (CSDL: haitacz)
echo ================================================================
echo   HẢI TẶC TÍ HÔN - WEB ICON CHECKER ^& OFFSET INSPECTOR
echo   CSDL MySQL: haitacz (localhost:3306)
echo   Icon Path: HaiTacTiHonServer/data/icon
echo ================================================================
echo.
cd /d "%~dp0"
start http://localhost:8088
python server.py
pause
