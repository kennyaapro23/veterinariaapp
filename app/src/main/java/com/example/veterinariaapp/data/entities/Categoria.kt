package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val categoriaId: Long = 0,
    val nombre: String,
    val descripcion: String,
    val color: String = "#FF6B35" // Color por defecto
)
