package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.PropietarioDao
import com.example.veterinariaapp.data.entities.Propietario
import kotlinx.coroutines.flow.Flow

class PropietarioRepository(private val propietarioDao: PropietarioDao) {

    fun getAllPropietarios(): Flow<List<Propietario>> = propietarioDao.getAllPropietarios()

    suspend fun getPropietarioById(id: Long): Propietario? = propietarioDao.getPropietarioById(id)

    suspend fun insertPropietario(propietario: Propietario): Long = propietarioDao.insertPropietario(propietario)

    suspend fun updatePropietario(propietario: Propietario) = propietarioDao.updatePropietario(propietario)

    suspend fun deletePropietario(propietario: Propietario) = propietarioDao.deletePropietario(propietario)
}
