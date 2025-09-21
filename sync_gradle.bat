@echo off
echo Sincronizando proyecto con Gradle...
cd /d "C:\Users\kenny\veterinariaapp"
gradlew.bat clean build
echo Sincronización completada.
pause

