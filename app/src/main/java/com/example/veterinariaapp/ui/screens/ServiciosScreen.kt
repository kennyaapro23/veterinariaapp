package com.example.veterinariaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.data.entities.Servicio
import com.example.veterinariaapp.ui.components.VeterinariaBottomBar
import com.example.veterinariaapp.viewmodel.ServicioViewModel
import com.example.veterinariaapp.viewmodel.ServicioViewModelFactory
import androidx.navigation.NavController

// ServiciosScreen.kt: Pantalla para mostrar y gestionar los servicios veterinarios ofrecidos.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen(
    navController: NavController,
    application: VeterinariaApplication
) {
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(application.servicioRepository)
    )

    val servicios by servicioViewModel.servicios.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingServicio by remember { mutableStateOf<Servicio?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Filtrar servicios por búsqueda
    val serviciosFiltrados = if (searchQuery.isBlank()) {
        servicios
    } else {
        servicios.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
                    it.descripcion.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        bottomBar = {
            VeterinariaBottomBar(
                currentScreen = "servicios",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏥 Servicios Veterinarios",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                FloatingActionButton(
                    onClick = {
                        editingServicio = null
                        showDialog = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Servicio")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar servicios...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📊 Catálogo de Servicios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Total de servicios: ${servicios.size}")
                    Text("Mostrando: ${serviciosFiltrados.size} servicios")

                    val costoPromedio = servicios.map { it.costo }.average()
                    if (!costoPromedio.isNaN()) {
                        Text("Costo promedio: $${String.format("%.2f", costoPromedio)}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(serviciosFiltrados) { servicio ->
                    ServicioCard(
                        servicio = servicio,
                        onEdit = {
                            editingServicio = it
                            showDialog = true
                        },
                        onDelete = { servicioViewModel.deleteServicio(it) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ServicioDialog(
            servicio = editingServicio,
            onDismiss = { showDialog = false },
            onSave = { servicio ->
                if (editingServicio != null) {
                    servicioViewModel.updateServicio(servicio)
                } else {
                    servicioViewModel.insertServicio(servicio)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ServicioCard(
    servicio: Servicio,
    onEdit: (Servicio) -> Unit,
    onDelete: (Servicio) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = servicio.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "💰 $${servicio.costo}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (servicio.descripcion.isNotBlank()) {
                        Text(
                            text = servicio.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Column {
                    IconButton(onClick = { onEdit(servicio) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(servicio) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioDialog(
    servicio: Servicio?,
    onDismiss: () -> Unit,
    onSave: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf(servicio?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(servicio?.descripcion ?: "") }
    var costo by remember { mutableStateOf(servicio?.costo?.toString() ?: "") }

    // Estados de error
    var nombreError by remember { mutableStateOf(false) }
    var costoError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (servicio == null) "🏥 Nuevo Servicio" else "✏️ Editar Servicio")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        if (nombreError) nombreError = false
                    },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreError,
                    supportingText = {
                        if (nombreError) {
                            Text("El nombre no puede estar vacío.")
                        }
                    },
                    placeholder = { Text("Ej: Consulta general, Vacunación, Cirugía") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = costo,
                    onValueChange = {
                        costo = it
                        if (costoError) costoError = false
                    },
                    label = { Text("Costo ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = costoError,
                    supportingText = {
                        if (costoError) {
                            Text("El costo debe ser un número válido y no puede estar vacío.")
                        }
                    },
                    placeholder = { Text("Ej: 50.00") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    placeholder = { Text("Describe el servicio veterinario...") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Reiniciar estados de error
                    nombreError = false
                    costoError = false

                    // Validar campos
                    if (nombre.trim().isEmpty()) {
                        nombreError = true
                    }
                    val costoDouble = costo.toDoubleOrNull()
                    if (costo.trim().isEmpty() || costoDouble == null) {
                        costoError = true
                    }

                    // Si no hay errores, guardar
                    if (!nombreError && !costoError) {
                        val nuevoServicio = Servicio(
                            servicioId = servicio?.servicioId ?: 0,
                            nombre = nombre,
                            descripcion = descripcion,
                            costo = costoDouble ?: 0.0
                        )
                        onSave(nuevoServicio)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}