package com.example.veterinariaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.entities.HistorialMedico
import com.example.veterinariaapp.data.repository.HistorialMedicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistorialMedicoViewModel(private val repository: HistorialMedicoRepository) : ViewModel() {

    private val _historialMedico = MutableStateFlow<List<HistorialMedico>>(emptyList())
    val historialMedico: StateFlow<List<HistorialMedico>> = _historialMedico.asStateFlow()

    private val _registroSeleccionado = MutableStateFlow<HistorialMedico?>(null)
    val registroSeleccionado: StateFlow<HistorialMedico?> = _registroSeleccionado.asStateFlow()

    fun insertHistorial(historial: HistorialMedico) {
        viewModelScope.launch {
            repository.insertHistorial(historial)
        }
    }

    fun updateHistorial(historial: HistorialMedico) {
        viewModelScope.launch {
            repository.updateHistorial(historial)
        }
    }

    fun deleteHistorial(historial: HistorialMedico) {
        viewModelScope.launch {
            repository.deleteHistorial(historial)
        }
    }

    fun getHistorialByMascota(mascotaId: Long) {
        viewModelScope.launch {
            repository.getHistorialByMascota(mascotaId).collect {
                _historialMedico.value = it
            }
        }
    }

    fun getHistorialByRangoFechas(fechaInicio: String, fechaFin: String) {
        viewModelScope.launch {
            repository.getHistorialByRangoFechas(fechaInicio, fechaFin).collect {
                _historialMedico.value = it
            }
        }
    }

    fun seleccionarRegistro(historial: HistorialMedico) {
        _registroSeleccionado.value = historial
    }

    fun limpiarSeleccion() {
        _registroSeleccionado.value = null
    }
}

class HistorialMedicoViewModelFactory(private val repository: HistorialMedicoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialMedicoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistorialMedicoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
