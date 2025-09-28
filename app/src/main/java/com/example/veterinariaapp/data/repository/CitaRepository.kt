package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.CitaDao
import com.example.veterinariaapp.data.entities.Cita
import kotlinx.coroutines.flow.Flow

// CitaRepository.kt: Lógica de acceso a datos para la entidad Cita.

class CitaRepository(private val citaDao: CitaDao) {

    fun getAllCitas(): Flow<List<Cita>> = citaDao.getAllCitas()

    suspend fun getCitaById(id: Long): Cita? = citaDao.getCitaById(id)

    fun getProximasCitasByMascota(mascotaId: Long): Flow<List<Cita>> =
        citaDao.getProximasCitasByMascota(mascotaId)

    fun getCitasByFecha(fecha: String): Flow<List<Cita>> =
        citaDao.getCitasByFecha(fecha)

    fun getCitasByEstado(estado: String): Flow<List<Cita>> =
        citaDao.getCitasByEstado(estado)

    suspend fun insertCita(cita: Cita): Long = citaDao.insertCita(cita)

    suspend fun updateCita(cita: Cita) = citaDao.updateCita(cita)

    suspend fun deleteCita(cita: Cita) = citaDao.deleteCita(cita)
}
