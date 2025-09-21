package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.HistorialMedicoDao
import com.example.veterinariaapp.data.entities.HistorialMedico
import kotlinx.coroutines.flow.Flow

class HistorialMedicoRepository(private val historialMedicoDao: HistorialMedicoDao) {

    fun getAllHistorialMedico(): Flow<List<HistorialMedico>> = historialMedicoDao.getAllHistorialMedico()

    suspend fun getHistorialById(id: Long): HistorialMedico? = historialMedicoDao.getHistorialById(id)

    fun getHistorialByMascota(mascotaId: Long): Flow<List<HistorialMedico>> =
        historialMedicoDao.getHistorialByMascota(mascotaId)

    fun getHistorialByRangoFechas(fechaInicio: String, fechaFin: String): Flow<List<HistorialMedico>> =
        historialMedicoDao.getHistorialByRangoFechas(fechaInicio, fechaFin)

    suspend fun insertHistorial(historial: HistorialMedico): Long = historialMedicoDao.insertHistorial(historial)

    suspend fun updateHistorial(historial: HistorialMedico) = historialMedicoDao.updateHistorial(historial)

    suspend fun deleteHistorial(historial: HistorialMedico) = historialMedicoDao.deleteHistorial(historial)
}
