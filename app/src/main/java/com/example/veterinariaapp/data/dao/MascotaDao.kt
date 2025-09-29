package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.Mascota
import kotlinx.coroutines.flow.Flow

// MascotaDao.kt: DAO para operaciones CRUD sobre la entidad Mascota.
@Dao
interface MascotaDao {
    @Query("SELECT * FROM mascotas")
    fun getAllMascotas(): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE mascotaId = :id")
    suspend fun getMascotaById(id: Long): Mascota?

    // Consulta personalizada: Listar todas las mascotas de un propietario
    @Query("SELECT * FROM mascotas WHERE propietarioId = :propietarioId")
    fun getMascotasByPropietario(propietarioId: Long): Flow<List<Mascota>>

    // Nueva consulta: Buscar mascotas por categoría
    @Query("SELECT * FROM mascotas WHERE categoriaId = :categoriaId")
    fun getMascotasByCategoria(categoriaId: Long): Flow<List<Mascota>>

    // Nueva consulta: Búsqueda avanzada de mascotas
    @Query("SELECT * FROM mascotas WHERE nombre LIKE '%' || :query || '%' OR especie LIKE '%' || :query || '%' OR raza LIKE '%' || :query || '%'")
    fun buscarMascotas(query: String): Flow<List<Mascota>>

    @Insert
    suspend fun insertMascota(mascota: Mascota): Long

    @Update
    suspend fun updateMascota(mascota: Mascota)

    @Delete
    suspend fun deleteMascota(mascota: Mascota)

    @Query("DELETE FROM mascotas")
    suspend fun deleteAllMascotas()
}
