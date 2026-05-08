@echo off
chcp 65001 >nul 2>&1
title Joyarta - Iniciando proyecto...

:: ============================================================
::  CONFIGURACION - Modifica estas rutas si cambian
:: ============================================================
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot"
set "MARIADB_BIN=C:\Program Files\MariaDB 12.2\bin"
set "NODE_DIR=C:\Program Files\nodejs"
set "PROJECT_DIR=%~dp0"

set "PATH=%JAVA_HOME%\bin;%NODE_DIR%;%MARIADB_BIN%;%PATH%"

:: ============================================================
::  1. BASE DE DATOS (MariaDB)
:: ============================================================
echo.
echo ===========================================================
echo   [1/3]  Iniciando MariaDB...
echo ===========================================================

REM Comprobar si MariaDB ya esta corriendo
"%MARIADB_BIN%\mysqladmin.exe" -u luis -pluis ping >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] MariaDB ya esta corriendo.
    goto db_ready
)

echo   --^> Arrancando MariaDB...
start "" /B "%MARIADB_BIN%\mysqld.exe" --console

REM Esperar a que MariaDB este lista (maximo 30 segundos)
set /a attempts=0

:wait_db
timeout /t 2 /nobreak >nul
"%MARIADB_BIN%\mysqladmin.exe" -u luis -pluis ping >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] MariaDB iniciada correctamente.
    goto db_ready
)
set /a attempts+=1
if %attempts% lss 15 (
    echo   . Esperando a MariaDB... ^(intento %attempts%^)
    goto wait_db
)
echo   [ERROR] MariaDB no pudo iniciar en 30 segundos.
echo     Revisa la configuracion o inicia MariaDB manualmente.
pause
exit /b 1

:db_ready

:: ============================================================
::  2. BACKEND (Spring Boot)
:: ============================================================
echo.
echo ===========================================================
echo   [2/3]  Iniciando Backend (Spring Boot)...
echo ===========================================================

REM Comprobar si el puerto 8080 ya esta en uso
netstat -aon | findstr ":8080 .*LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] El backend ya esta corriendo en el puerto 8080.
    goto backend_ready
)

echo   --^> Arrancando servidor Spring Boot...
start "Joyarta - Backend" /D "%PROJECT_DIR%Servidor\Joyarta" cmd /k mvnw.cmd spring-boot:run

REM Esperar a que el backend este listo
set /a attempts=0

:wait_backend
timeout /t 3 /nobreak >nul
netstat -aon | findstr ":8080 .*LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] Backend iniciado en http://localhost:8080
    goto backend_ready
)
set /a attempts+=1
if %attempts% lss 20 (
    echo   . Esperando al backend... ^(intento %attempts%^)
    goto wait_backend
)
echo   [ERROR] El backend no se inicio en 60 segundos.
pause
exit /b 1

:backend_ready

:: ============================================================
::  3. FRONTEND (Angular)
:: ============================================================
echo.
echo ===========================================================
echo   [3/3]  Iniciando Frontend (Angular)...
echo ===========================================================

REM Comprobar si el puerto 4200 ya esta en uso
netstat -aon | findstr ":4200 .*LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] El frontend ya esta corriendo en el puerto 4200.
    goto frontend_ready
)

REM Instalar dependencias si no existen
if not exist "%PROJECT_DIR%Cliente\node_modules" (
    echo   --^> Instalando dependencias npm...
    cd /d "%PROJECT_DIR%Cliente"
    call npm install
)

echo   --^> Arrancando servidor Angular...
start "Joyarta - Frontend" /D "%PROJECT_DIR%Cliente" cmd /k npx.cmd ng serve --open

REM Esperar a que el frontend este listo
set /a attempts=0

:wait_frontend
timeout /t 3 /nobreak >nul
netstat -aon | findstr ":4200 .*LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo   [OK] Frontend iniciado en http://localhost:4200
    goto frontend_ready
)
set /a attempts+=1
if %attempts% lss 20 (
    echo   . Esperando al frontend... ^(intento %attempts%^)
    goto wait_frontend
)
echo   [ERROR] El frontend no se inicio en 60 segundos.
pause
exit /b 1

:frontend_ready

:: ============================================================
::  RESUMEN
:: ============================================================
echo.
echo ===========================================================
echo   PROYECTO JOYARTA INICIADO CORRECTAMENTE
echo ===========================================================
echo.
echo   Base de datos:  MariaDB en localhost:3306
echo   Backend:        http://localhost:8080
echo   Frontend:       http://localhost:4200
echo.
echo   Pulsa cualquier tecla para cerrar esta ventana.
echo   (Los servidores seguiran corriendo en segundo plano)
echo ===========================================================
pause >nul
