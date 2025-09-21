package com.example.veterinariaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.entities.Servicio
import com.example.veterinariaapp.data.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServicioViewModel(private val repository: ServicioRepository) : ViewModel() {

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios.asStateFlow()

    private val _servicioSeleccionado = MutableStateFlow<Servicio?>(null)
    val servicioSeleccionado: StateFlow<Servicio?> = _servicioSeleccionado.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllServicios().collect {
                _servicios.value = it
            }
        }
    }

    fun insertServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.insertServicio(servicio)
        }
    }

    fun updateServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.updateServicio(servicio)
        }
    }

    fun deleteServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.deleteServicio(servicio)
        }
    }

    fun buscarServiciosPorNombre(nombre: String) {
        viewModelScope.launch {
            repository.buscarServiciosPorNombre(nombre).collect {
                _servicios.value = it
            }
        }
    }

    fun seleccionarServicio(servicio: Servicio) {
        _servicioSeleccionado.value = servicio
    }

    fun limpiarSeleccion() {
        _servicioSeleccionado.value = null
    }
}

class ServicioViewModelFactory(private val repository: ServicioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServicioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
