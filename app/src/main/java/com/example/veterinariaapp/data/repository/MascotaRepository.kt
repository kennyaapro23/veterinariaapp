package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.MascotaDao
import com.example.veterinariaapp.data.entities.Mascota
import kotlinx.coroutines.flow.Flow

// MascotaRepository.kt: Lógica de acceso a datos para la entidad Mascota.

class MascotaRepository(private val mascotaDao: MascotaDao) {

    fun getAllMascotas(): Flow<List<Mascota>> = mascotaDao.getAllMascotas()

    suspend fun getMascotaById(id: Long): Mascota? = mascotaDao.getMascotaById(id)

    fun getMascotasByPropietario(propietarioId: Long): Flow<List<Mascota>> =
        mascotaDao.getMascotasByPropietario(propietarioId)

    fun getMascotasByCategoria(categoriaId: Long): Flow<List<Mascota>> =
        mascotaDao.getMascotasByCategoria(categoriaId)

    fun buscarMascotas(query: String): Flow<List<Mascota>> =
        mascotaDao.buscarMascotas(query)

    suspend fun insertMascota(mascota: Mascota): Long = mascotaDao.insertMascota(mascota)

    suspend fun updateMascota(mascota: Mascota) = mascotaDao.updateMascota(mascota)

    suspend fun deleteMascota(mascota: Mascota) = mascotaDao.deleteMascota(mascota)
}
