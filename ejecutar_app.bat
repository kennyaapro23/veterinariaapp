@echo off
echo.
echo ========================================
echo    🏥 EJECUTANDO VETERINARIA APP 🏥
echo ========================================
echo.
echo 📱 Sincronizando proyecto con Gradle...
cd /d "C:\Users\kenny\veterinariaapp"

echo.
echo 🔄 Limpiando proyecto...
call gradlew.bat clean

echo.
echo 🔧 Compilando aplicación...
call gradlew.bat build

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ COMPILACIÓN EXITOSA!
    echo.
    echo 📱 Instalando en dispositivo/emulador...
    call gradlew.bat installDebug

    if %ERRORLEVEL% EQU 0 (
        echo.
        echo 🎉 ¡APLICACIÓN INSTALADA Y LISTA!
        echo.
        echo 📋 TU APP VETERINARIA INCLUYE:
        echo    • 🐾 Registro de mascotas con búsqueda
        echo    • 📋 Historial médico digital
        echo    • 🏥 Catálogo de servicios
        echo    • 👥 Gestión de propietarios
        echo    • 🔍 Búsquedas avanzadas
        echo    • 🎯 Navegación profesional
        echo.
        echo ✨ ¡La app ya está en tu dispositivo!
    ) else (
        echo.
        echo ❌ Error al instalar. Verifica que tengas un dispositivo conectado.
    )
) else (
    echo.
    echo ❌ Error en la compilación.
)

echo.
pause
