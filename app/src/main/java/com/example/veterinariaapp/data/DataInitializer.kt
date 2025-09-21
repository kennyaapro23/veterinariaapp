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

class DataInitializer(
    private val propietarioRepository: PropietarioRepository,
    private val servicioRepository: ServicioRepository,
    private val mascotaRepository: MascotaRepository,
    private val historialMedicoRepository: HistorialMedicoRepository,
    private val categoriaRepository: CategoriaRepository
) {

    fun initializeData() {
        CoroutineScope(Dispatchers.IO).launch {
            initializeCategorias()
            initializePropietarios()
            initializeServicios()
            initializeMascotas()
            initializeHistorialMedico()
        }
    }

    private suspend fun initializePropietarios() {
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

        propietariosEjemplo.forEach { propietario ->
            propietarioRepository.insertPropietario(propietario)
        }
    }

    private suspend fun initializeServicios() {
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

        serviciosVeterinarios.forEach { servicio ->
            servicioRepository.insertServicio(servicio)
        }
    }

    private suspend fun initializeMascotas() {
        val mascotasEjemplo = listOf(
            Mascota(
                nombre = "Max",
                especie = "Perro",
                raza = "Golden Retriever",
                edad = 3,
                sexo = "Macho",
                propietarioId = 1L,
                categoriaId = 2L // Perros Grandes
            ),
            Mascota(
                nombre = "Luna",
                especie = "Gato",
                raza = "Persa",
                edad = 2,
                sexo = "Hembra",
                propietarioId = 1L,
                categoriaId = 3L // Gatos Domésticos
            ),
            Mascota(
                nombre = "Rocky",
                especie = "Perro",
                raza = "Bulldog",
                edad = 5,
                sexo = "Macho",
                propietarioId = 2L,
                categoriaId = 1L // Perros Pequeños
            ),
            Mascota(
                nombre = "Bella",
                especie = "Gato",
                raza = "Siamés",
                edad = 4,
                sexo = "Hembra",
                propietarioId = 2L,
                categoriaId = 3L // Gatos Domésticos
            ),
            Mascota(
                nombre = "Charlie",
                especie = "Perro",
                raza = "Labrador",
                edad = 1,
                sexo = "Macho",
                propietarioId = 3L,
                categoriaId = 4L // Cachorros
            )
        )

        mascotasEjemplo.forEach { mascota ->
            mascotaRepository.insertMascota(mascota)
        }
    }

    private suspend fun initializeHistorialMedico() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val historialEjemplo = listOf(
            HistorialMedico(
                mascotaId = 1L,
                servicioId = 1L,
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)),
                observaciones = "Consulta de rutina. Mascota en excelente estado. Se recomienda continuar con alimentación balanceada."
            ),
            HistorialMedico(
                mascotaId = 1L,
                servicioId = 2L,
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000)),
                observaciones = "Vacunación anual completada. Próxima dosis en 12 meses."
            ),
            HistorialMedico(
                mascotaId = 2L,
                servicioId = 1L,
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000)),
                observaciones = "Consulta por pérdida de apetito. Se prescribe tratamiento y dieta especial."
            ),
            HistorialMedico(
                mascotaId = 3L,
                servicioId = 3L,
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 21 * 24 * 60 * 60 * 1000)),
                observaciones = "Desparasitación preventiva realizada. Control en 3 meses."
            ),
            HistorialMedico(
                mascotaId = 4L,
                servicioId = 5L,
                fecha = dateFormat.format(Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000)),
                observaciones = "Limpieza dental realizada. Se observa mejoría significativa en salud bucal."
            )
        )

        historialEjemplo.forEach { historial ->
            historialMedicoRepository.insertHistorial(historial)
        }
    }

    private suspend fun initializeCategorias() {
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

        categoriasEjemplo.forEach { categoria ->
            categoriaRepository.insertCategoria(categoria)
        }
    }
}
