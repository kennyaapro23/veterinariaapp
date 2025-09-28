package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Servicio.kt: Entidad Room que representa un servicio veterinario ofrecido.
@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true)
    val servicioId: Long = 0,
    val nombre: String,
    val descripcion: String,
    val costo: Double
)
