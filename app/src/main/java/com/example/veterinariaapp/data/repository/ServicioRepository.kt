package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.ServicioDao
import com.example.veterinariaapp.data.entities.Servicio
import kotlinx.coroutines.flow.Flow

// ServicioRepository.kt: Lógica de acceso a datos para la entidad Servicio.

class ServicioRepository(private val servicioDao: ServicioDao) {

    fun getAllServicios(): Flow<List<Servicio>> = servicioDao.getAllServicios()

    suspend fun getServicioById(id: Long): Servicio? = servicioDao.getServicioById(id)

    fun buscarServiciosPorNombre(nombre: String): Flow<List<Servicio>> =
        servicioDao.buscarServiciosPorNombre(nombre)

    suspend fun insertServicio(servicio: Servicio): Long = servicioDao.insertServicio(servicio)

    suspend fun updateServicio(servicio: Servicio) = servicioDao.updateServicio(servicio)

    suspend fun deleteServicio(servicio: Servicio) = servicioDao.deleteServicio(servicio)

    suspend fun deleteAllServicios() = servicioDao.deleteAllServicios()
}
