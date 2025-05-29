@echo off
SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

REM Load variables from .env file
for /f "usebackq tokens=1,* delims==" %%A in (".env") do (
    set "line=%%A=%%B"
    for /f "tokens=1,* delims==" %%a in ("!line!") do (
        set "%%a=%%b"
    )
)

REM Show loaded env values for debug
echo DB_USER: %DB_USER%
echo DB_NAME: %DB_NAME%
echo DB_HOST: %DB_HOST%
echo DB_PORT: %DB_PORT%
echo DB_PASS: %DB_PASS%

REM Check if psql exists
where psql >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    echo  psql is not installed or not in PATH
    pause
    exit /b 1
)

REM Check if gradlew exists
IF NOT EXIST gradlew.bat (
    echo gradlew.bat not found in project root
    pause
    exit /b 1
)

REM Check and create database if needed
echo Checking if database %DB_NAME% exists...
psql -U %DB_USER% -h %DB_HOST% -p %DB_PORT% -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = '%DB_NAME%'" | findstr "1" >nul
IF %ERRORLEVEL% NEQ 0 (
    echo Creating database %DB_NAME%...
    psql -U %DB_USER% -h %DB_HOST% -p %DB_PORT% -d postgres -c "CREATE DATABASE %DB_NAME%"
)

REM Run init.sql
echo Running init.sql...
psql -U %DB_USER% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -f init.sql

REM Start application
echo Starting Spring Boot application...
gradlew.bat bootRun