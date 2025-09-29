package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.Cita
import kotlinx.coroutines.flow.Flow

// CitaDao.kt: DAO para operaciones CRUD sobre la entidad Cita.
@Dao
interface CitaDao {
    @Query("SELECT * FROM citas")
    fun getAllCitas(): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE citaId = :id")
    suspend fun getCitaById(id: Long): Cita?

    // Consulta personalizada: Listar las próximas citas de una mascota
    @Query("SELECT * FROM citas WHERE mascotaId = :mascotaId AND estado = 'pendiente' ORDER BY fecha ASC, hora ASC")
    fun getProximasCitasByMascota(mascotaId: Long): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE fecha = :fecha ORDER BY hora ASC")
    fun getCitasByFecha(fecha: String): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE estado = :estado ORDER BY fecha ASC, hora ASC")
    fun getCitasByEstado(estado: String): Flow<List<Cita>>

    @Insert
    suspend fun insertCita(cita: Cita): Long

    @Update
    suspend fun updateCita(cita: Cita)

    @Delete
    suspend fun deleteCita(cita: Cita)

    @Query("DELETE FROM citas")
    suspend fun deleteAllCitas()
}
