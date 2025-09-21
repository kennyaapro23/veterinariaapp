package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mascotas",
    foreignKeys = [
        ForeignKey(
            entity = Propietario::class,
            parentColumns = ["propietarioId"],
            childColumns = ["propietarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["categoriaId"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val mascotaId: Long = 0,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val sexo: String,
    val propietarioId: Long,
    val categoriaId: Long? = null
)
