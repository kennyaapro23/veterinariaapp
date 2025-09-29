package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.HistorialMedico
import kotlinx.coroutines.flow.Flow

// HistorialMedicoDao.kt: DAO para operaciones CRUD sobre la entidad HistorialMedico.

@Dao
interface HistorialMedicoDao {
    @Query("SELECT * FROM historial_medico ORDER BY fecha DESC")
    fun getAllHistorial(): Flow<List<HistorialMedico>>

    // Consulta personalizada: Obtener el historial médico completo de una mascota
    @Query("SELECT * FROM historial_medico WHERE mascotaId = :mascotaId ORDER BY fecha DESC")
    fun getHistorialByMascota(mascotaId: Long): Flow<List<HistorialMedico>>

    // Consulta personalizada: Consultar los servicios realizados en un rango de fechas
    @Query("SELECT * FROM historial_medico WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getHistorialByRangoFechas(fechaInicio: String, fechaFin: String): Flow<List<HistorialMedico>>

    @Query("SELECT * FROM historial_medico WHERE historialId = :id")
    suspend fun getHistorialById(id: Long): HistorialMedico?

    @Insert
    suspend fun insertHistorial(historial: HistorialMedico): Long

    @Update
    suspend fun updateHistorial(historial: HistorialMedico)

    @Delete
    suspend fun deleteHistorial(historial: HistorialMedico)

    @Query("DELETE FROM historial_medico")
    suspend fun deleteAllHistorial()
}