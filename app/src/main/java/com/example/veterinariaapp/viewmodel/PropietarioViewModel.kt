package com.example.veterinariaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.entities.Propietario
import com.example.veterinariaapp.data.repository.PropietarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PropietarioViewModel(private val repository: PropietarioRepository) : ViewModel() {

    private val _propietarios = MutableStateFlow<List<Propietario>>(emptyList())
    val propietarios: StateFlow<List<Propietario>> = _propietarios.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPropietarios().collect {
                _propietarios.value = it
            }
        }
    }

    fun insertPropietario(propietario: Propietario) {
        viewModelScope.launch {
            repository.insertPropietario(propietario)
        }
    }

    fun updatePropietario(propietario: Propietario) {
        viewModelScope.launch {
            repository.updatePropietario(propietario)
        }
    }

    fun deletePropietario(propietario: Propietario) {
        viewModelScope.launch {
            repository.deletePropietario(propietario)
        }
    }
}

class PropietarioViewModelFactory(private val repository: PropietarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropietarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PropietarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
