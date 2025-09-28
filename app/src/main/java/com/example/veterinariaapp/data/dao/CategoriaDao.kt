package com.example.veterinariaapp.data.dao

import androidx.room.*
import com.example.veterinariaapp.data.entities.Categoria
import kotlinx.coroutines.flow.Flow

// CategoriaDao.kt: DAO para operaciones CRUD sobre la entidad Categoria.

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllCategorias(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE categoriaId = :id")
    suspend fun getCategoriaById(id: Long): Categoria?

    @Query("SELECT * FROM categorias WHERE nombre LIKE '%' || :nombre || '%'")
    fun buscarCategoriasPorNombre(nombre: String): Flow<List<Categoria>>

    @Insert
    suspend fun insertCategoria(categoria: Categoria): Long

    @Update
    suspend fun updateCategoria(categoria: Categoria)

    @Delete
    suspend fun deleteCategoria(categoria: Categoria)
}
