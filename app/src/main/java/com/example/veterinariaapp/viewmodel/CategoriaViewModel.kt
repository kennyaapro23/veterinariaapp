package com.example.veterinariaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.entities.Categoria
import com.example.veterinariaapp.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val categoriaSeleccionada: StateFlow<Categoria?> = _categoriaSeleccionada.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCategorias().collect {
                _categorias.value = it
            }
        }
    }
    
    fun insertCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repository.insertCategoria(categoria)
        }
    }

    fun updateCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repository.updateCategoria(categoria)
        }
    }

    fun deleteCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repository.deleteCategoria(categoria)
        }
    }

    fun buscarCategoriasPorNombre(nombre: String) {
        viewModelScope.launch {
            repository.buscarCategoriasPorNombre(nombre).collect {
                _categorias.value = it
            }
        }
    }

    fun seleccionarCategoria(categoria: Categoria) {
        _categoriaSeleccionada.value = categoria
    }

    fun limpiarSeleccion() {
        _categoriaSeleccionada.value = null
    }
}

class CategoriaViewModelFactory(private val repository: CategoriaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
