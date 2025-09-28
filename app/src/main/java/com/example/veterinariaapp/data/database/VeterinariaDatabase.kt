package com.example.veterinariaapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.veterinariaapp.data.entities.*
import com.example.veterinariaapp.data.dao.*

/**
 * VeterinariaDatabase.kt: Define la base de datos Room y sus entidades.
 */

@Database(
    entities = [
        Propietario::class,
        Mascota::class,
        Servicio::class,
        HistorialMedico::class,
        Cita::class,
        Categoria::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VeterinariaDatabase : RoomDatabase() {

    abstract fun propietarioDao(): PropietarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun servicioDao(): ServicioDao
    abstract fun historialMedicoDao(): HistorialMedicoDao
    abstract fun citaDao(): CitaDao
    abstract fun categoriaDao(): CategoriaDao

    companion object {
        @Volatile
        private var INSTANCE: VeterinariaDatabase? = null

        fun getDatabase(context: Context): VeterinariaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VeterinariaDatabase::class.java,
                    "veterinaria_database"
                )
                .fallbackToDestructiveMigration() // Esto permite recrear la BD cuando cambias la versión
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
