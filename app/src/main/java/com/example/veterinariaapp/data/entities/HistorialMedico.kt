package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// HistorialMedico.kt: Entidad Room que representa el historial médico de una mascota.

@Entity(
    tableName = "historial_medico",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["mascotaId"],
            childColumns = ["mascotaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Servicio::class,
            parentColumns = ["servicioId"],
            childColumns = ["servicioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistorialMedico(
    @PrimaryKey(autoGenerate = true)
    val historialId: Long = 0,
    val mascotaId: Long,
    val servicioId: Long,
    val fecha: String,
    val observaciones: String
)
