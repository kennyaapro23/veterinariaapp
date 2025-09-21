package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "propietarios")
data class Propietario(
    @PrimaryKey(autoGenerate = true)
    val propietarioId: Long = 0,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val email: String
)
