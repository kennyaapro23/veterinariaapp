package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.Propietario
import kotlinx.coroutines.flow.Flow

// PropietarioDao.kt: DAO para operaciones CRUD sobre la entidad Propietario.

@Dao
interface PropietarioDao {
    @Query("SELECT * FROM propietarios")
    fun getAllPropietarios(): Flow<List<Propietario>>

    @Query("SELECT * FROM propietarios WHERE propietarioId = :id")
    suspend fun getPropietarioById(id: Long): Propietario?

    @Insert
    suspend fun insertPropietario(propietario: Propietario): Long

    @Update
    suspend fun updatePropietario(propietario: Propietario)

    @Delete
    suspend fun deletePropietario(propietario: Propietario)
}
