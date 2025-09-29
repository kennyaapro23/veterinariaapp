package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.Servicio
import kotlinx.coroutines.flow.Flow

// ServicioDao.kt: DAO para operaciones CRUD sobre la entidad Servicio.
@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicios")
    fun getAllServicios(): Flow<List<Servicio>>

    @Query("SELECT * FROM servicios WHERE servicioId = :id")
    suspend fun getServicioById(id: Long): Servicio?

    @Query("SELECT * FROM servicios WHERE nombre LIKE '%' || :nombre || '%'")
    fun buscarServiciosPorNombre(nombre: String): Flow<List<Servicio>>

    @Insert
    suspend fun insertServicio(servicio: Servicio): Long

    @Update
    suspend fun updateServicio(servicio: Servicio)

    @Delete
    suspend fun deleteServicio(servicio: Servicio)

    @Query("DELETE FROM servicios")
    suspend fun deleteAllServicios()
}
