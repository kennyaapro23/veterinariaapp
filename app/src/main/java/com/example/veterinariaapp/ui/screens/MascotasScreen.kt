package com.example.veterinariaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.data.entities.Mascota
import com.example.veterinariaapp.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    application: VeterinariaApplication,
    onNavigateToHistorial: (Long) -> Unit
) {
    val mascotaViewModel: MascotaViewModel = viewModel(
        factory = MascotaViewModelFactory(application.mascotaRepository)
    )
    val propietarioViewModel: PropietarioViewModel = viewModel(
        factory = PropietarioViewModelFactory(application.propietarioRepository)
    )
    val categoriaViewModel: CategoriaViewModel = viewModel(
        factory = CategoriaViewModelFactory(application.categoriaRepository)
    )

    val mascotas by mascotaViewModel.mascotas.collectAsState()
    val propietarios by propietarioViewModel.propietarios.collectAsState()
    val categorias by categoriaViewModel.categorias.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingMascota by remember { mutableStateOf<Mascota?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var filterByPropietario by remember { mutableStateOf<Long?>(null) }
    var filterByCategoria by remember { mutableStateOf<Long?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var activeFilter by remember { mutableStateOf("none") } // "none", "propietario", "categoria", "search"

    // Aplicar filtros según el tipo activo
    LaunchedEffect(activeFilter, filterByPropietario, filterByCategoria, searchQuery) {
        when (activeFilter) {
            "propietario" -> filterByPropietario?.let { mascotaViewModel.getMascotasByPropietario(it) }
            "categoria" -> filterByCategoria?.let { mascotaViewModel.getMascotasByCategoria(it) }
            "search" -> if (searchQuery.isNotBlank()) mascotaViewModel.buscarMascotas(searchQuery)
            else mascotaViewModel.resetMascotas()
        }
    }

    val mascotasFiltradas = mascotas.filter { mascota ->
        val propietario = propietarios.find { it.propietarioId == mascota.propietarioId }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            mascota.nombre.contains(searchQuery, ignoreCase = true) ||
                    mascota.especie.contains(searchQuery, ignoreCase = true) ||
                    mascota.raza.contains(searchQuery, ignoreCase = true) ||
                    propietario?.nombre?.contains(searchQuery, ignoreCase = true) == true
        }
        val matchesPropietario = filterByPropietario?.let { mascota.propietarioId == it } ?: true
        val matchesCategoria = filterByCategoria?.let { mascota.categoriaId == it } ?: true
        matchesSearch && matchesPropietario && matchesCategoria
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con título y acciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🐾 Registro de Mascotas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Toca ➕ para agregar una nueva mascota",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón de búsqueda
                IconButton(onClick = { showSearchBar = !showSearchBar }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }

                // Botón de filtro
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon(Icons.Default.Settings, contentDescription = "Filtrar")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón agregar más prominente
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    editingMascota = null
                    showDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Mascota") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }

        // Barra de búsqueda expandible
        if (showSearchBar) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por nombre de mascota o propietario...") },
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

        // Filtro actual
        if (filterByPropietario != null || filterByCategoria != null) {
            val propietarioFiltro = propietarios.find { it.propietarioId == filterByPropietario }
            val categoriaFiltro = categorias.find { it.categoriaId == filterByCategoria }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildString {
                            if (propietarioFiltro != null) {
                                append("👤 Filtrando por: ${propietarioFiltro.nombre}")
                            }
                            if (categoriaFiltro != null) {
                                if (propietarioFiltro != null) append(" | ")
                                append("📂 Filtrando por categoría: ${categoriaFiltro.nombre}")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = {
                            filterByPropietario = null
                            filterByCategoria = null
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Quitar filtro")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resumen con resultados de búsqueda
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📊 Resumen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Total de mascotas registradas: ${mascotas.size}")
                Text("Propietarios activos: ${propietarios.size}")
                if (searchQuery.isNotBlank() || filterByPropietario != null || filterByCategoria != null) {
                    Text(
                        text = "Mostrando: ${mascotasFiltradas.size} mascotas",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de mascotas filtradas
        LazyColumn {
            items(mascotasFiltradas) { mascota ->
                MascotaCard(
                    mascota = mascota,
                    propietarios = propietarios,
                    onEdit = {
                        editingMascota = it
                        showDialog = true
                    },
                    onDelete = { mascotaViewModel.deleteMascota(it) },
                    onViewHistorial = { onNavigateToHistorial(it.mascotaId) }
                )
            }

            // Mensaje si no hay resultados
            if (mascotasFiltradas.isEmpty() && mascotas.isNotEmpty()) {
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
                                text = "No se encontraron mascotas",
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

    // Menú de filtros avanzado
    if (showFilterMenu) {
        AlertDialog(
            onDismissRequest = { showFilterMenu = false },
            title = { Text("🔍 Filtros Avanzados") },
            text = {
                LazyColumn {
                    // Sección de filtros generales
                    item {
                        Text(
                            text = "Filtros Generales",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Card(
                            onClick = {
                                filterByPropietario = null
                                filterByCategoria = null
                                activeFilter = "none"
                                showFilterMenu = false
                            },
                            colors = if (filterByPropietario == null && filterByCategoria == null)
                                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            else CardDefaults.cardColors()
                        ) {
                            Text(
                                text = "👥 Mostrar todas las mascotas",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Sección filtros por categoría
                    item {
                        Text(
                            text = "Filtrar por Categoría",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(categorias) { categoria ->
                        val mascotasDeCategoria = mascotas.count { it.categoriaId == categoria.categoriaId }
                        val categoriaColor = try {
                            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(categoria.color))
                        } catch (e: Exception) {
                            MaterialTheme.colorScheme.primary
                        }

                        Card(
                            onClick = {
                                filterByCategoria = categoria.categoriaId
                                filterByPropietario = null
                                activeFilter = "categoria"
                                showFilterMenu = false
                            },
                            colors = if (filterByCategoria == categoria.categoriaId)
                                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            else CardDefaults.cardColors(),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(categoriaColor)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = categoria.nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "$mascotasDeCategoria mascota(s)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }


                    // Spacer entre secciones
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Filtrar por Propietario",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(propietarios) { propietario ->
                        val mascotasDelPropietario = mascotas.count { it.propietarioId == propietario.propietarioId }
                        Card(
                            onClick = {
                                filterByPropietario = propietario.propietarioId
                                filterByCategoria = null
                                activeFilter = "propietario"
                                showFilterMenu = false
                            },
                            colors = if (filterByPropietario == propietario.propietarioId)
                                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            else CardDefaults.cardColors(),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = propietario.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "$mascotasDelPropietario mascota(s)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterMenu = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (showDialog) {
        MascotaDialog(
            mascota = editingMascota,
            propietarios = propietarios,
            onDismiss = { showDialog = false },
            onSave = { mascota ->
                if (editingMascota != null) {
                    mascotaViewModel.updateMascota(mascota)
                } else {
                    mascotaViewModel.insertMascota(mascota)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun MascotaCard(
    mascota: Mascota,
    propietarios: List<com.example.veterinariaapp.data.entities.Propietario>,
    onEdit: (Mascota) -> Unit,
    onDelete: (Mascota) -> Unit,
    onViewHistorial: (Mascota) -> Unit
) {
    val propietario = propietarios.find { it.propietarioId == mascota.propietarioId }

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
                        text = mascota.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("🐕 ${mascota.especie} - ${mascota.raza}")
                    Text("📅 ${mascota.edad} años - ${mascota.sexo}")
                    Text("👤 Propietario: ${propietario?.nombre ?: "No encontrado"}")
                }

                Column {
                    IconButton(onClick = { onViewHistorial(mascota) }) {
                        Icon(Icons.Default.Info, contentDescription = "Ver Historial")
                    }
                    IconButton(onClick = { onEdit(mascota) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(mascota) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaDialog(
    mascota: Mascota?,
    propietarios: List<com.example.veterinariaapp.data.entities.Propietario>,
    onDismiss: () -> Unit,
    onSave: (Mascota) -> Unit
) {
    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var edad by remember { mutableStateOf(mascota?.edad?.toString() ?: "") }
    var sexo by remember { mutableStateOf(mascota?.sexo ?: "Macho") }
    var propietarioSeleccionado by remember { mutableStateOf(mascota?.propietarioId ?: (propietarios.firstOrNull()?.propietarioId ?: 0L)) }

    // Variables para los menús desplegables de Especie y Raza
    var expandedEspecie by remember { mutableStateOf(false) }
    var expandedRaza by remember { mutableStateOf(false) }
    var expandedSexo by remember { mutableStateOf(false) }
    var expandedPropietario by remember { mutableStateOf(false) }

    var especieSeleccionada by remember { mutableStateOf(mascota?.especie ?: "") }
    var razaSeleccionada by remember { mutableStateOf(mascota?.raza ?: "") }

    // Listas de opciones
    val especies = listOf("Perro", "Gato", "Ave", "Roedor", "Reptil", "Conejo")
    val razasPerro = listOf("Labrador Retriever", "Pastor Alemán", "Bulldog", "Poodle", "Chihuahua", "Mestizo")
    val razasGato = listOf("Siamés", "Persa", "Maine Coon", "Bengalí", "Mestizo")
    val razasAve = listOf("Canario", "Periquito", "Cacatúa", "Guacamayo", "Ninfa")
    val razasRoedor = listOf("Hámster", "Cuy", "Jerbo", "Chinchilla")
    val razasReptil = listOf("Iguana", "Gecko", "Serpiente", "Tortuga")
    val razasConejo = listOf("Enano", "Belier", "Angora", "Mini Lop")
    val razasNoEspecificadas = listOf("Sin especificar")

    val razasDisponibles = when (especieSeleccionada) {
        "Perro" -> razasPerro
        "Gato" -> razasGato
        "Ave" -> razasAve
        "Roedor" -> razasRoedor
        "Reptil" -> razasReptil
        "Conejo" -> razasConejo
        else -> razasNoEspecificadas
    }

    // Estados para los errores
    var nombreError by remember { mutableStateOf(false) }
    var especieError by remember { mutableStateOf(false) }
    var razaError by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf(false) }
    var propietarioError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (mascota == null) "➕ Nueva Mascota" else "✏️ Editar Mascota")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        if (nombreError) nombreError = false
                    },
                    label = { Text("Nombre de la mascota") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreError,
                    supportingText = {
                        if (nombreError) {
                            Text("El nombre no puede estar vacío.")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Menú desplegable para Especie
                ExposedDropdownMenuBox(
                    expanded = expandedEspecie,
                    onExpandedChange = { expandedEspecie = !expandedEspecie }
                ) {
                    OutlinedTextField(
                        value = especieSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Especie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecie) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        isError = especieError,
                        supportingText = {
                            if (especieError) {
                                Text("Debe seleccionar una especie.")
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEspecie,
                        onDismissRequest = { expandedEspecie = false }
                    ) {
                        especies.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    especieSeleccionada = opcion
                                    expandedEspecie = false
                                    // Resetear la raza al cambiar la especie
                                    razaSeleccionada = ""
                                    if (especieError) especieError = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Menú desplegable para Raza (dependiente de la especie)
                ExposedDropdownMenuBox(
                    expanded = expandedRaza,
                    onExpandedChange = { expandedRaza = !expandedRaza }
                ) {
                    OutlinedTextField(
                        value = razaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Raza") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRaza) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        isError = razaError,
                        supportingText = {
                            if (razaError) {
                                Text("Debe seleccionar una raza.")
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRaza,
                        onDismissRequest = { expandedRaza = false }
                    ) {
                        razasDisponibles.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    razaSeleccionada = opcion
                                    expandedRaza = false
                                    if (razaError) razaError = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = edad,
                    onValueChange = {
                        edad = it
                        if (edadError) edadError = false
                    },
                    label = { Text("Edad (años)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = edadError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = {
                        if (edadError) {
                            Text("La edad no puede estar vacía y debe ser un número.")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedSexo,
                    onExpandedChange = { expandedSexo = !expandedSexo }
                ) {
                    OutlinedTextField(
                        value = sexo,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Sexo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSexo) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedSexo,
                        onDismissRequest = { expandedSexo = false }
                    ) {
                        listOf("Macho", "Hembra").forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    sexo = opcion
                                    expandedSexo = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedPropietario,
                    onExpandedChange = { expandedPropietario = !expandedPropietario }
                ) {
                    OutlinedTextField(
                        value = propietarios.find { it.propietarioId == propietarioSeleccionado }?.nombre ?: "Seleccionar propietario",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Propietario") },
                        isError = propietarioError,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPropietario) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        supportingText = {
                            if (propietarioError) {
                                Text("Debe seleccionar un propietario.")
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedPropietario,
                        onDismissRequest = { expandedPropietario = false }
                    ) {
                        propietarios.forEach { propietario ->
                            DropdownMenuItem(
                                text = { Text(propietario.nombre) },
                                onClick = {
                                    propietarioSeleccionado = propietario.propietarioId
                                    expandedPropietario = false
                                    if (propietarioError) propietarioError = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Resetear los estados de error
                    nombreError = false
                    especieError = false
                    razaError = false
                    edadError = false
                    propietarioError = false

                    // Validar cada campo
                    if (nombre.trim().isEmpty()) {
                        nombreError = true
                    }
                    if (especieSeleccionada.isEmpty()) {
                        especieError = true
                    }
                    if (razaSeleccionada.isEmpty()) {
                        razaError = true
                    }
                    if (edad.trim().isEmpty() || edad.toIntOrNull() == null) {
                        edadError = true
                    }
                    if (propietarioSeleccionado == 0L) {
                        propietarioError = true
                    }

                    // Si no hay errores, guardar
                    if (!nombreError && !especieError && !razaError && !edadError && !propietarioError) {
                        val edadInt = edad.toIntOrNull() ?: 0
                        val nuevaMascota = Mascota(
                            mascotaId = mascota?.mascotaId ?: 0,
                            nombre = nombre,
                            especie = especieSeleccionada,
                            raza = razaSeleccionada,
                            edad = edadInt,
                            sexo = sexo,
                            propietarioId = propietarioSeleccionado
                        )
                        onSave(nuevaMascota)
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