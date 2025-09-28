package com.example.veterinariaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.entities.Mascota
import com.example.veterinariaapp.data.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// MascotaViewModel.kt: ViewModel para la gestión de mascotas en la app.

class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas.asStateFlow()

    private val _mascotaSeleccionada = MutableStateFlow<Mascota?>(null)
    val mascotaSeleccionada: StateFlow<Mascota?> = _mascotaSeleccionada.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMascotas().collect {
                _mascotas.value = it
            }
        }
    }

    fun insertMascota(mascota: Mascota) {
        viewModelScope.launch {
            repository.insertMascota(mascota)
        }
    }

    fun updateMascota(mascota: Mascota) {
        viewModelScope.launch {
            repository.updateMascota(mascota)
        }
    }

    fun deleteMascota(mascota: Mascota) {
        viewModelScope.launch {
            repository.deleteMascota(mascota)
        }
    }

    fun getMascotasByPropietario(propietarioId: Long) {
        viewModelScope.launch {
            repository.getMascotasByPropietario(propietarioId).collect {
                _mascotas.value = it
            }
        }
    }

    fun seleccionarMascota(mascota: Mascota) {
        _mascotaSeleccionada.value = mascota
    }

    fun limpiarSeleccion() {
        _mascotaSeleccionada.value = null
    }

    fun getMascotasByCategoria(categoriaId: Long) {
        viewModelScope.launch {
            repository.getMascotasByCategoria(categoriaId).collect {
                _mascotas.value = it
            }
        }
    }

    fun buscarMascotas(query: String) {
        viewModelScope.launch {
            repository.buscarMascotas(query).collect {
                _mascotas.value = it
            }
        }
    }

    fun resetMascotas() {
        viewModelScope.launch {
            repository.getAllMascotas().collect {
                _mascotas.value = it
            }
        }
    }
}

class MascotaViewModelFactory(private val repository: MascotaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MascotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MascotaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
