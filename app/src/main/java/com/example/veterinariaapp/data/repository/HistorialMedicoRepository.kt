package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.HistorialMedicoDao
import com.example.veterinariaapp.data.entities.HistorialMedico
import kotlinx.coroutines.flow.Flow

// HistorialMedicoRepository.kt: Lógica de acceso a datos para la entidad HistorialMedico.
class HistorialMedicoRepository(private val historialMedicoDao: HistorialMedicoDao) {

    // Este es el método corregido. Ahora se llama 'getAllHistorial'
    // para que coincida con el ViewModel y el DAO.
    fun getAllHistorial(): Flow<List<HistorialMedico>> = historialMedicoDao.getAllHistorial()

    suspend fun getHistorialById(id: Long): HistorialMedico? = historialMedicoDao.getHistorialById(id)

    fun getHistorialByMascota(mascotaId: Long): Flow<List<HistorialMedico>> =
        historialMedicoDao.getHistorialByMascota(mascotaId)

    fun getHistorialByRangoFechas(fechaInicio: String, fechaFin: String): Flow<List<HistorialMedico>> =
        historialMedicoDao.getHistorialByRangoFechas(fechaInicio, fechaFin)

    suspend fun insertHistorial(historial: HistorialMedico): Long = historialMedicoDao.insertHistorial(historial)

    suspend fun updateHistorial(historial: HistorialMedico) = historialMedicoDao.updateHistorial(historial)

    suspend fun deleteHistorial(historial: HistorialMedico) = historialMedicoDao.deleteHistorial(historial)
}