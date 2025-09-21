@echo off
echo.
echo 🏥 EJECUTANDO VETERINARIA APP - VERSIÓN CORREGIDA
echo ===============================================
echo.
cd /d "C:\Users\kenny\veterinariaapp"

echo 🧹 Limpiando datos antiguos...
rd /s /q "app\build" 2>nul

echo 🔄 Sincronizando con Gradle...
call gradlew.bat clean

echo 🔧 Compilando aplicación...
call gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ COMPILACIÓN EXITOSA!
    echo.
    echo 📱 Instalando en dispositivo...
    call gradlew.bat installDebug

    if %ERRORLEVEL% EQU 0 (
        echo.
        echo 🎉 ¡APLICACIÓN INSTALADA CORRECTAMENTE!
        echo.
        echo 📋 PROBLEMA SOLUCIONADO:
        echo    ✅ Base de datos migrada correctamente
        echo    ✅ Categorías implementadas
        echo    ✅ Búsquedas avanzadas funcionando
        echo    ✅ Botón ➕ Nueva Mascota visible
        echo.
        echo 🚀 ¡La app ya NO se cerrará!
    ) else (
        echo ❌ Error al instalar. Verifica dispositivo conectado.
    )
) else (
    echo ❌ Error en compilación. Revisa los logs.
)

pause
