package com.example.veterinariaapp.data

import com.example.veterinariaapp.data.entities.Propietario
import com.example.veterinariaapp.data.entities.Servicio
import com.example.veterinariaapp.data.entities.Mascota
import com.example.veterinariaapp.data.entities.HistorialMedico
import com.example.veterinariaapp.data.entities.Categoria
import com.example.veterinariaapp.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/*
DataInitializer.kt: Inicializa datos de ejemplo en la base de datos al iniciar la app.
*/

class DataInitializer(
    private val propietarioRepository: PropietarioRepository,
    private val servicioRepository: ServicioRepository,
    private val mascotaRepository: MascotaRepository,
    private val historialMedicoRepository: HistorialMedicoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val citaRepository: CitaRepository
) {

    fun initializeData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Limpiar toda la base de datos antes de insertar datos nuevos
            clearDatabase()

            // Insertar en orden correcto y capturar IDs reales
            val categoriaIds = initializeCategorias()
            val propietarioIds = initializePropietarios()
            val servicioIds = initializeServicios()
            val mascotaIds = initializeMascotas(propietarioIds, categoriaIds)
            initializeHistorialMedico(mascotaIds, servicioIds)
        }
    }

    /**
     * Limpia toda la base de datos eliminando todos los registros de todas las tablas
     */
    private suspend fun clearDatabase() {
        // Limpiar en orden inverso debido a las relaciones de claves foráneas
        historialMedicoRepository.deleteAllHistorial()
        citaRepository.deleteAllCitas()
        mascotaRepository.deleteAllMascotas()
        propietarioRepository.deleteAllPropietarios()
        servicioRepository.deleteAllServicios()
        categoriaRepository.deleteAllCategorias()
    }

    private suspend fun initializePropietarios(): List<Long> {
        val propietariosEjemplo = listOf(
            Propietario(
                nombre = "Dr. Juan Pérez",
                telefono = "555-0123",
                email = "juan.perez@email.com",
                direccion = "Av. Principal 123, Ciudad"
            ),
            Propietario(
                nombre = "María García",
                telefono = "555-0456",
                email = "maria.garcia@email.com",
                direccion = "Calle Secundaria 456, Ciudad"
            ),
            Propietario(
                nombre = "Carlos Rodríguez",
                telefono = "555-0789",
                email = "carlos.rodriguez@email.com",
                direccion = "Barrio Norte 789, Ciudad"
            )
        )

        val propietarioIds = mutableListOf<Long>()
        propietariosEjemplo.forEach { propietario ->
            val id = propietarioRepository.insertPropietario(propietario)
            propietarioIds.add(id)
        }
        return propietarioIds
    }

    private suspend fun initializeServicios(): List<Long> {
        val serviciosVeterinarios = listOf(
            Servicio(
                nombre = "Consulta General",
                descripcion = "Examen físico completo y diagnóstico general",
                costo = 50.0
            ),
            Servicio(
                nombre = "Vacunación",
                descripcion = "Aplicación de vacunas según calendario",
                costo = 25.0
            ),
            Servicio(
                nombre = "Desparasitación",
                descripcion = "Tratamiento antiparasitario interno y externo",
                costo = 30.0
            ),
            Servicio(
                nombre = "Esterilización",
                descripcion = "Cirugía de esterilización/castración",
                costo = 150.0
            ),
            Servicio(
                nombre = "Limpieza Dental",
                descripcion = "Profilaxis dental y eliminación de sarro",
                costo = 80.0
            ),
            Servicio(
                nombre = "Radiografía",
                descripcion = "Estudio radiológico para diagnóstico",
                costo = 60.0
            ),
            Servicio(
                nombre = "Análisis de Sangre",
                descripcion = "Hemograma completo y química sanguínea",
                costo = 45.0
            ),
            Servicio(
                nombre = "Cirugía Menor",
                descripcion = "Procedimientos quirúrgicos ambulatorios",
                costo = 120.0
            ),
            Servicio(
                nombre = "Hospitalización",
                descripcion = "Internación por día para tratamiento intensivo",
                costo = 100.0
            ),
            Servicio(
                nombre = "Emergencia",
                descripcion = "Atención de urgencias 24/7",
                costo = 200.0
            )
        )

        val servicioIds = mutableListOf<Long>()
        serviciosVeterinarios.forEach { servicio ->
            val id = servicioRepository.insertServicio(servicio)
            servicioIds.add(id)
        }
        return servicioIds
    }

    private suspend fun initializeMascotas(propietarioIds: List<Long>, categoriaIds: List<Long>): List<Long> {
        // Verificar que tenemos suficientes propietarios y categorías
        if (propietarioIds.isEmpty() || categoriaIds.isEmpty()) {
            return emptyList()
        }

        val mascotasEjemplo = listOf(
            Mascota(
                nombre = "Max",
                especie = "Perro",
                raza = "Golden Retriever",
                edad = 3,
                sexo = "Macho",
                propietarioId = propietarioIds[0], // Usar ID real del primer propietario
                categoriaId = categoriaIds.getOrNull(1) ?: categoriaIds[0] // Perros Grandes o primera categoría
            ),
            Mascota(
                nombre = "Luna",
                especie = "Gato",
                raza = "Persa",
                edad = 2,
                sexo = "Hembra",
                propietarioId = propietarioIds[0],
                categoriaId = categoriaIds.getOrNull(2) ?: categoriaIds[0] // Gatos Domésticos o primera categoría
            ),
            Mascota(
                nombre = "Rocky",
                especie = "Perro",
                raza = "Bulldog",
                edad = 5,
                sexo = "Macho",
                propietarioId = propietarioIds.getOrNull(1) ?: propietarioIds[0],
                categoriaId = categoriaIds.getOrNull(0) ?: categoriaIds[0] // Perros Pequeños o primera categoría
            ),
            Mascota(
                nombre = "Bella",
                especie = "Gato",
                raza = "Siamés",
                edad = 4,
                sexo = "Hembra",
                propietarioId = propietarioIds.getOrNull(1) ?: propietarioIds[0],
                categoriaId = categoriaIds.getOrNull(2) ?: categoriaIds[0] // Gatos Domésticos o primera categoría
            ),
            Mascota(
                nombre = "Charlie",
                especie = "Perro",
                raza = "Labrador",
                edad = 1,
                sexo = "Macho",
                propietarioId = propietarioIds.getOrNull(2) ?: propietarioIds[0],
                categoriaId = categoriaIds.getOrNull(3) ?: categoriaIds[0] // Cachorros o primera categoría
            )
        )

        val mascotaIds = mutableListOf<Long>()
        mascotasEjemplo.forEach { mascota ->
            val id = mascotaRepository.insertMascota(mascota)
            mascotaIds.add(id)
        }
        return mascotaIds
    }

    private suspend fun initializeHistorialMedico(mascotaIds: List<Long>, servicioIds: List<Long>) {
        // Solo crear historial si tenemos mascotas y servicios
        if (mascotaIds.isEmpty() || servicioIds.isEmpty()) {
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val historialEjemplo = listOf(
            HistorialMedico(
                mascotaId = mascotaIds[0],
                servicioId = servicioIds[0], // Consulta General
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)),
                observaciones = "Consulta de rutina. Mascota en excelente estado. Se recomienda continuar con alimentación balanceada."
            ),
            HistorialMedico(
                mascotaId = mascotaIds[0],
                servicioId = servicioIds.getOrNull(1) ?: servicioIds[0], // Vacunación
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000)),
                observaciones = "Vacunación anual completada. Próxima dosis en 12 meses."
            ),
            HistorialMedico(
                mascotaId = mascotaIds.getOrNull(1) ?: mascotaIds[0],
                servicioId = servicioIds[0], // Consulta General
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000)),
                observaciones = "Consulta por pérdida de apetito. Se prescribe tratamiento y dieta especial."
            ),
            HistorialMedico(
                mascotaId = mascotaIds.getOrNull(2) ?: mascotaIds[0],
                servicioId = servicioIds.getOrNull(2) ?: servicioIds[0], // Desparasitación
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 21 * 24 * 60 * 60 * 1000)),
                observaciones = "Desparasitación preventiva realizada. Control en 3 meses."
            ),
            HistorialMedico(
                mascotaId = mascotaIds.getOrNull(3) ?: mascotaIds[0],
                servicioId = servicioIds.getOrNull(4) ?: servicioIds[0], // Limpieza Dental
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000)),
                observaciones = "Limpieza dental realizada. Se observa mejoría significativa en salud bucal."
            )
        )

        historialEjemplo.forEach { historial ->
            historialMedicoRepository.insertHistorial(historial)
        }
    }

    private suspend fun initializeCategorias(): List<Long> {
        val categoriasEjemplo = listOf(
            Categoria(
                nombre = "Perros Pequeños",
                descripcion = "Razas de perros de tamaño pequeño (menos de 10kg)",
                color = "#FF6B35"
            ),
            Categoria(
                nombre = "Perros Grandes",
                descripcion = "Razas de perros de tamaño grande (más de 25kg)",
                color = "#4ECDC4"
            ),
            Categoria(
                nombre = "Gatos Domésticos",
                descripcion = "Felinos domésticos de todas las razas",
                color = "#45B7D1"
            ),
            Categoria(
                nombre = "Cachorros",
                descripcion = "Mascotas menores de 1 año",
                color = "#96CEB4"
            ),
            Categoria(
                nombre = "Mascotas Senior",
                descripcion = "Mascotas mayores de 7 años",
                color = "#FFEAA7"
            ),
            Categoria(
                nombre = "Exóticos",
                descripcion = "Mascotas no convencionales (conejos, aves, etc.)",
                color = "#DDA0DD"
            )
        )

        val categoriaIds = mutableListOf<Long>()
        categoriasEjemplo.forEach { categoria ->
            val id = categoriaRepository.insertCategoria(categoria)
            categoriaIds.add(id)
        }
        return categoriaIds
    }
}
