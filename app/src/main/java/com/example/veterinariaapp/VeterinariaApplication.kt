package com.example.veterinariaapp

import android.app.Application
import com.example.veterinariaapp.data.DataInitializer
import com.example.veterinariaapp.data.database.VeterinariaDatabase
import com.example.veterinariaapp.data.repository.*

/**
 * VeterinariaApplication.kt: Clase de aplicación, inicializa configuraciones globales.
 */

class VeterinariaApplication : Application() {

    val database by lazy { VeterinariaDatabase.getDatabase(this) }

    val propietarioRepository by lazy { PropietarioRepository(database.propietarioDao()) }
    val mascotaRepository by lazy { MascotaRepository(database.mascotaDao()) }
    val servicioRepository by lazy { ServicioRepository(database.servicioDao()) }
    val historialMedicoRepository by lazy { HistorialMedicoRepository(database.historialMedicoDao()) }
    val citaRepository by lazy { CitaRepository(database.citaDao()) }
    val categoriaRepository by lazy { CategoriaRepository(database.categoriaDao()) }

    override fun onCreate() {
        super.onCreate()

        // Inicializar datos de ejemplo
        val dataInitializer = DataInitializer(
            propietarioRepository,
            servicioRepository,
            mascotaRepository,
            historialMedicoRepository,
            categoriaRepository,
            citaRepository
        )
        dataInitializer.initializeData()
    }
}
