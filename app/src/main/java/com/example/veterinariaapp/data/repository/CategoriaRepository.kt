package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.dao.CategoriaDao
import com.example.veterinariaapp.data.entities.Categoria
import kotlinx.coroutines.flow.Flow

// CategoriaRepository.kt: Lógica de acceso a datos para la entidad Categoria.

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    fun getAllCategorias(): Flow<List<Categoria>> = categoriaDao.getAllCategorias()

    suspend fun getCategoriaById(id: Long): Categoria? = categoriaDao.getCategoriaById(id)

    fun buscarCategoriasPorNombre(nombre: String): Flow<List<Categoria>> =
        categoriaDao.buscarCategoriasPorNombre(nombre)

    suspend fun insertCategoria(categoria: Categoria): Long = categoriaDao.insertCategoria(categoria)

    suspend fun updateCategoria(categoria: Categoria) = categoriaDao.updateCategoria(categoria)

    suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.deleteCategoria(categoria)

    suspend fun deleteAllCategorias() = categoriaDao.deleteAllCategorias()
}
