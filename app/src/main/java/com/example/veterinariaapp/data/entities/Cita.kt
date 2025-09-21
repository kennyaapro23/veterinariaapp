package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas",
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
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val citaId: Long = 0,
    val mascotaId: Long,
    val servicioId: Long,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String = "pendiente" // pendiente, completada, cancelada
)
