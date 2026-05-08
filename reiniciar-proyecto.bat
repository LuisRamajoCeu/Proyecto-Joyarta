@echo off
chcp 65001 >nul 2>&1
title Joyarta - Reiniciando proyecto...

set "MARIADB_BIN=C:\Program Files\MariaDB 12.2\bin"

echo.
echo ===========================================================
echo   REINICIANDO PROYECTO JOYARTA
echo ===========================================================

:: ============================================================
::  PASO 1: APAGAR TODO
:: ============================================================
echo.
echo -----------------------------------------------------------
echo   Deteniendo servicios actuales...
echo -----------------------------------------------------------

echo   --^> Deteniendo Frontend...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":4200 .*LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
echo   [OK] Frontend detenido.

echo   --^> Deteniendo Backend...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 .*LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
taskkill /FI "WINDOWTITLE eq Joyarta - Backend*" /F >nul 2>&1
echo   [OK] Backend detenido.

echo   --^> Deteniendo MariaDB...
"%MARIADB_BIN%\mysqladmin.exe" -u luis -pluis shutdown >nul 2>&1
if not %errorlevel%==0 (
    taskkill /IM mysqld.exe /F >nul 2>&1
)
echo   [OK] MariaDB detenida.

REM Esperar a que los puertos se liberen
echo.
echo   Esperando a que los puertos se liberen...
timeout /t 3 /nobreak >nul

:: ============================================================
::  PASO 2: INICIAR TODO DE NUEVO
:: ============================================================
echo.
echo -----------------------------------------------------------
echo   Iniciando servicios de nuevo...
echo -----------------------------------------------------------

REM Llamar al script de inicio
call "%~dp0iniciar-proyecto.bat"
