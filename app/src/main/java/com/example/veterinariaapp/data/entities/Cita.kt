package com.example.veterinariaapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Cita.kt: Entidad Room que representa una cita médica para una mascota.
// Contiene información sobre la mascota, el servicio requerido, la fecha y hora de la cita,
// el motivo de la consulta y el estado de la cita (pendiente, completada, cancelada).

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
