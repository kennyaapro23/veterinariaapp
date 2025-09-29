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
import com.example.veterinariaapp.data.entities.HistorialMedico
import com.example.veterinariaapp.data.entities.Mascota
import com.example.veterinariaapp.data.entities.Servicio
import com.example.veterinariaapp.viewmodel.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController
import com.example.veterinariaapp.ui.components.VeterinariaBottomBar

/*
HistorialMedicoScreen.kt: Pantalla para mostrar y gestionar el historial médico de una mascota.
Permite ver, buscar y filtrar registros de historial médico, así como agregar, editar o eliminar registros.
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialMedicoScreen(
    navController: NavController,
    application: VeterinariaApplication,
    mascotaId: Long? = null
) {
    val historialViewModel: HistorialMedicoViewModel = viewModel(
        factory = HistorialMedicoViewModelFactory(application.historialMedicoRepository)
    )
    val mascotaViewModel: MascotaViewModel = viewModel(
        factory = MascotaViewModelFactory(application.mascotaRepository)
    )
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(application.servicioRepository)
    )

    val historial by historialViewModel.historialMedico.collectAsState()
    val mascotas by mascotaViewModel.mascotas.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingHistorial by remember { mutableStateOf<HistorialMedico?>(null) }
    var selectedMascotaId by remember { mutableStateOf(mascotaId ?: 0L) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    // Filtrar historial por búsqueda
    val historialFiltrado = historial.filter { registro ->
        val mascota = mascotas.find { it.mascotaId == registro.mascotaId }
        val servicio = servicios.find { it.servicioId == registro.servicioId }
        if (searchQuery.isBlank()) true else {
            registro.observaciones.contains(searchQuery, ignoreCase = true) ||
                    registro.fecha.contains(searchQuery, ignoreCase = true) ||
                    mascota?.nombre?.contains(searchQuery, ignoreCase = true) == true ||
                    servicio?.nombre?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    // Cargar historial de la mascota específica o todo el historial
    LaunchedEffect(selectedMascotaId) {
        if (selectedMascotaId == 0L) {
            historialViewModel.getAllHistorial()
        } else {
            historialViewModel.getHistorialByMascota(selectedMascotaId)
        }
    }

    Scaffold(
        bottomBar = {
            VeterinariaBottomBar(
                currentScreen = "historial",
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
                Column {
                    Text(
                        text = "📋 Historial Médico",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (mascotaId != null) {
                        val mascota = mascotas.find { it.mascotaId == mascotaId }
                        Text(
                            text = "Paciente: ${mascota?.nombre ?: "Cargando..."}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row {
                    // Botón de búsqueda
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }

                    // Botón de filtro (solo si no hay mascota específica)
                    if (mascotaId == null) {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Filtrar")
                        }
                    }

                    // Botón agregar
                    FloatingActionButton(
                        onClick = {
                            editingHistorial = null
                            showDialog = true
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Registro")
                    }
                }
            }

            // Barra de búsqueda expandible
            if (showSearchBar) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar en observaciones, fecha, mascota o servicio...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros y resumen
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📊 Resumen Clínico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Total de consultas: ${historial.size}")
                    Text("Última consulta: ${historial.maxByOrNull { it.fecha }?.fecha ?: "Sin consultas"}")

                    if (searchQuery.isNotBlank()) {
                        Text(
                            text = "Mostrando: ${historialFiltrado.size} registros",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (mascotaId == null) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Selector de mascota para ver historial completo
                        var expandedMascota by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expandedMascota,
                            onExpandedChange = { expandedMascota = !expandedMascota }
                        ) {
                            OutlinedTextField(
                                value = if (selectedMascotaId == 0L) "Todas las mascotas" else mascotas.find { it.mascotaId == selectedMascotaId }?.nombre ?: "Todas las mascotas",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Filtrar por mascota") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMascota) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedMascota,
                                onDismissRequest = { expandedMascota = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Todas las mascotas") },
                                    onClick = {
                                        selectedMascotaId = 0L
                                        expandedMascota = false
                                    }
                                )
                                mascotas.forEach { mascota ->
                                    DropdownMenuItem(
                                        text = { Text("${mascota.nombre} (${mascota.especie})") },
                                        onClick = {
                                            selectedMascotaId = mascota.mascotaId
                                            expandedMascota = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(historialFiltrado.sortedByDescending { it.fecha }) { registro ->
                    HistorialCard(
                        historial = registro,
                        mascotas = mascotas,
                        servicios = servicios,
                        onEdit = {
                            editingHistorial = it
                            showDialog = true
                        },
                        onDelete = { historialViewModel.deleteHistorial(it) }
                    )
                }

                // Mensaje si no hay resultados
                if (historialFiltrado.isEmpty() && historial.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No se encontraron registros",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Intenta con otros términos de búsqueda",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            HistorialDialog(
                historial = editingHistorial,
                mascotas = mascotas,
                servicios = servicios,
                mascotaIdPredeterminada = mascotaId,
                onDismiss = { showDialog = false },
                onSave = { historial ->
                    if (editingHistorial != null) {
                        historialViewModel.updateHistorial(historial)
                    } else {
                        historialViewModel.insertHistorial(historial)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun HistorialCard(
    historial: HistorialMedico,
    mascotas: List<Mascota>,
    servicios: List<Servicio>,
    onEdit: (HistorialMedico) -> Unit,
    onDelete: (HistorialMedico) -> Unit
) {
    val mascota = mascotas.find { it.mascotaId == historial.mascotaId }
    val servicio = servicios.find { it.servicioId == historial.servicioId }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
                        text = "📅 ${historial.fecha}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "🐾 Paciente: ${mascota?.nombre ?: "No encontrado"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "🏥 Servicio: ${servicio?.nombre ?: "No encontrado"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (servicio != null) {
                        Text(
                            text = "💰 Costo: $${servicio.costo}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Column {
                    IconButton(onClick = { onEdit(historial) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(historial) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }

            if (historial.observaciones.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "📝 Observaciones Clínicas:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = historial.observaciones,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialDialog(
    historial: HistorialMedico?,
    mascotas: List<Mascota>,
    servicios: List<Servicio>,
    mascotaIdPredeterminada: Long?,
    onDismiss: () -> Unit,
    onSave: (HistorialMedico) -> Unit
) {
    var mascotaSeleccionada by remember {
        mutableStateOf(historial?.mascotaId ?: mascotaIdPredeterminada ?: (mascotas.firstOrNull()?.mascotaId ?: 0L))
    }
    var servicioSeleccionado by remember {
        mutableStateOf(historial?.servicioId ?: (servicios.firstOrNull()?.servicioId ?: 0L))
    }
    var observaciones by remember { mutableStateOf(historial?.observaciones ?: "") }

    // Estados para el selector de fecha
    var showDatePicker by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember {
        mutableStateOf(historial?.fecha?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)?.time
        } ?: System.currentTimeMillis())
    }

    // Estados para los menús desplegables
    var expandedMascota by remember { mutableStateOf(false) }
    var expandedServicio by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (historial == null) "📋 Nuevo Registro Médico" else "✏️ Editar Registro")
        },
        text = {
            Column {
                // Selector de mascota (solo si no está predeterminada)
                if (mascotaIdPredeterminada == null) {
                    ExposedDropdownMenuBox(
                        expanded = expandedMascota,
                        onExpandedChange = { expandedMascota = !expandedMascota }
                    ) {
                        OutlinedTextField(
                            value = mascotas.find { it.mascotaId == mascotaSeleccionada }?.nombre ?: "Seleccionar mascota",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Paciente") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMascota) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedMascota,
                            onDismissRequest = { expandedMascota = false }
                        ) {
                            mascotas.forEach { mascota ->
                                DropdownMenuItem(
                                    text = { Text("${mascota.nombre} (${mascota.especie})") },
                                    onClick = {
                                        mascotaSeleccionada = mascota.mascotaId
                                        expandedMascota = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Selector de servicio
                ExposedDropdownMenuBox(
                    expanded = expandedServicio,
                    onExpandedChange = { expandedServicio = !expandedServicio }
                ) {
                    OutlinedTextField(
                        value = servicios.find { it.servicioId == servicioSeleccionado }?.nombre ?: "Seleccionar servicio",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Servicio/Tratamiento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServicio) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedServicio,
                        onDismissRequest = { expandedServicio = false }
                    ) {
                        servicios.forEach { servicio ->
                            DropdownMenuItem(
                                text = { Text("${servicio.nombre} - $${servicio.costo}") },
                                onClick = {
                                    servicioSeleccionado = servicio.servicioId
                                    expandedServicio = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Selector de fecha
                OutlinedTextField(
                    value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(fechaSeleccionada)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de consulta") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones clínicas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(fechaSeleccionada))
                    val nuevoHistorial = HistorialMedico(
                        historialId = historial?.historialId ?: 0,
                        mascotaId = mascotaSeleccionada,
                        servicioId = servicioSeleccionado,
                        fecha = fechaFormateada,
                        observaciones = observaciones
                    )
                    onSave(nuevoHistorial)
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

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaSeleccionada
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = datePickerState.selectedDateMillis
                        if (selectedDate != null) {
                            // Corrección para la zona horaria
                            val offset = TimeZone.getDefault().getOffset(selectedDate)
                            fechaSeleccionada = selectedDate + offset
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}