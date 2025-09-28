# 🐾 VeterinariaApp

Aplicación móvil moderna para la gestión integral de clínicas veterinarias. Permite registrar y administrar mascotas, propietarios, servicios, historial médico y categorías, facilitando la gestión clínica y de clientes con una interfaz intuitiva y profesional.

---

## 🚀 Vista rápida

- **Plataforma:** Android (Kotlin, Jetpack Compose)
- **Arquitectura:** MVVM + Room + Flow
- **UI:** Material 3, diseño responsivo y moderno
- **Propósito:** Digitalizar y optimizar la gestión veterinaria

---

## ✨ Características principales

| Funcionalidad                | Descripción                                                                 |
|------------------------------|-----------------------------------------------------------------------------|
| 🐶 Gestión de Mascotas       | Registro, edición, búsqueda y eliminación de pacientes                       |
| 👤 Gestión de Propietarios   | Administración de clientes y sus datos de contacto                          |
| 🏥 Servicios                 | Catálogo editable de servicios veterinarios                                 |
| 📋 Historial Médico          | Registro de consultas, tratamientos y observaciones por mascota              |
| 🗂️ Categorías                | Organización de mascotas y servicios por categorías personalizadas           |
| 🔍 Búsqueda y Filtros        | Filtros avanzados y búsqueda en todos los módulos                            |
| 🌙 Tema adaptable            | Interfaz clara y oscura, Material 3                                         |
| 🏗️ Datos de ejemplo          | Inicialización automática para pruebas y demostraciones                      |

---

## 🖼️ Capturas de pantalla

> _Agrega aquí tus capturas de pantalla para mostrar la app en acción._
>
> ![Pantalla de inicio](docs/screenshot_home.png)
> ![Gestión de mascotas](docs/screenshot_mascotas.png)

---

## 🛠️ Tecnologías utilizadas

- **Kotlin**: Lenguaje principal de desarrollo
- **Android Jetpack Compose**: UI declarativa y reactiva
- **Room**: Persistencia local con SQLite
- **Coroutines & Flow**: Programación asíncrona y reactiva
- **MVVM**: Separación de lógica, presentación y datos
- **Material 3**: Diseño visual moderno y adaptable

---

## 🏛️ ¿Por qué esta arquitectura?

- **MVVM**: Facilita el mantenimiento, escalabilidad y testeo.
- **Room + Flow**: Permite una base de datos reactiva y eficiente.
- **Jetpack Compose**: UI moderna, menos código y mayor flexibilidad.

---

## 📁 Estructura del proyecto

```
app/
 └─ src/main/java/com/example/veterinariaapp/
     ├─ data/         # Entidades, DAOs, base de datos y repositorios
     ├─ viewmodel/    # ViewModels para cada entidad principal
     ├─ ui/
     │   ├─ screens/  # Pantallas principales de la app
     │   └─ theme/    # Temas, colores y tipografías
     └─ ...
```

---

## ⚡ Instalación y ejecución

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/tu_usuario/veterinariaapp.git
   ```
2. **Abre el proyecto en Android Studio.**
3. **Sincroniza las dependencias Gradle.**
4. **Conecta un dispositivo o usa un emulador.**
5. **Ejecuta la aplicación:**
---

## 🧩 Personalización

- Modifica los colores y el tema en `ui/theme/`.
- Agrega o edita entidades y pantallas según tus necesidades.
- Los datos de ejemplo se inicializan automáticamente al instalar la app (ver `DataInitializer.kt`).

---

## 💡 Ejemplo de flujo de uso

1. **Inicio:** Acceso rápido a módulos de mascotas, historial, servicios y propietarios.
2. **Mascotas:** Añade, edita o elimina mascotas, asignando propietario y categoría.
3. **Historial:** Visualiza y gestiona consultas y tratamientos por mascota.
4. **Servicios:** Administra los servicios veterinarios ofrecidos y sus costos.
5. **Propietarios:** Registra y edita información de los dueños de las mascotas.
6. **Categorías:** Organiza mascotas y servicios por categorías personalizadas.

---

## 📝 Comentarios en el código

Cada archivo fuente contiene un comentario inicial explicando su propósito o lógica principal, para facilitar la comprensión y el mantenimiento del proyecto.


## 🤝 Contacto y agradecimientos

- Desarrollado por Kenny y colaboradores

¿Te gustó el proyecto? ¡No dudes en contactarme para colaboraciones o sugerencias!


## 📄 Licencia

Este proyecto es de código abierto
