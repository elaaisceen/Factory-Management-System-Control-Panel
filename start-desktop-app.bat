@echo off
cd /d "%~dp0FabrikaSistemi\backend_service"

echo Maven build...
call mvn package -DskipTests -q
if errorlevel 1 (
    echo Build hatası!
    pause
    exit /b 1
)

echo Uygulama başlatılıyor...
echo http://localhost:8080
java -jar target\stitch-0.0.1-SNAPSHOT.jar --desktop

pause
