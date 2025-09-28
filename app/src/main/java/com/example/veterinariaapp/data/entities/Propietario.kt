package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Propietario.kt: Entidad Room que representa al dueño de una mascota.
@Entity(tableName = "propietarios")
data class Propietario(
    @PrimaryKey(autoGenerate = true)
    val propietarioId: Long = 0,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val email: String
)
