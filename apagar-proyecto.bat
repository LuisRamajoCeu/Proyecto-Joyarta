@echo off
chcp 65001 >nul 2>&1
title Joyarta - Apagando proyecto...

set "MARIADB_BIN=C:\Program Files\MariaDB 12.2\bin"

echo.
echo ===========================================================
echo   APAGANDO PROYECTO JOYARTA
echo ===========================================================

:: ============================================================
::  1. FRONTEND (Angular - puerto 4200)
:: ============================================================
echo.
echo   [1/3]  Deteniendo Frontend (Angular)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":4200 .*LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
echo   [OK] Frontend detenido.

:: ============================================================
::  2. BACKEND (Spring Boot - puerto 8080)
:: ============================================================
echo.
echo   [2/3]  Deteniendo Backend (Spring Boot)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 .*LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
:: Cerrar ventanas de cmd del backend
taskkill /FI "WINDOWTITLE eq Joyarta - Backend*" /F >nul 2>&1
echo   [OK] Backend detenido.

:: ============================================================
::  3. BASE DE DATOS (MariaDB)
:: ============================================================
echo.
echo   [3/3]  Deteniendo MariaDB...
"%MARIADB_BIN%\mysqladmin.exe" -u luis -pluis shutdown >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] MariaDB detenida correctamente.
) else (
    REM Intentar matar el proceso directamente
    taskkill /IM mysqld.exe /F >nul 2>&1
    echo   [OK] MariaDB detenida.
)

:: ============================================================
::  RESUMEN
:: ============================================================
echo.
echo ===========================================================
echo   PROYECTO JOYARTA APAGADO CORRECTAMENTE
echo ===========================================================
echo.
echo   Todos los servicios han sido detenidos.
echo.
echo   Pulsa cualquier tecla para cerrar esta ventana.
echo ===========================================================
pause >nul
