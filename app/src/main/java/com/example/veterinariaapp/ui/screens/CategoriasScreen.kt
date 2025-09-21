package com.example.veterinariaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.data.entities.Categoria
import com.example.veterinariaapp.viewmodel.CategoriaViewModel
import com.example.veterinariaapp.viewmodel.CategoriaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(application: VeterinariaApplication) {
    val categoriaViewModel: CategoriaViewModel = viewModel(
        factory = CategoriaViewModelFactory(application.categoriaRepository)
    )

    val categorias by categoriaViewModel.categorias.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingCategoria by remember { mutableStateOf<Categoria?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }

    // Filtrar categorías por búsqueda
    val categoriasFiltradas = if (searchQuery.isBlank()) {
        categorias
    } else {
        categorias.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
            it.descripcion.contains(searchQuery, ignoreCase = true)
        }
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
                    text = "🏷️ Gestión de Categorías",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Organiza las mascotas por categorías",
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
                    editingCategoria = null
                    showDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Categoría") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }

        // Barra de búsqueda expandible
        if (showSearchBar) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar categorías...") },
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

        // Resumen
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📊 Resumen de Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Total de categorías: ${categorias.size}")
                if (searchQuery.isNotBlank()) {
                    Text(
                        text = "Mostrando: ${categoriasFiltradas.size} categorías",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de categorías
        LazyColumn {
            items(categoriasFiltradas) { categoria ->
                CategoriaCard(
                    categoria = categoria,
                    onEdit = {
                        editingCategoria = it
                        showDialog = true
                    },
                    onDelete = { categoriaViewModel.deleteCategoria(it) }
                )
            }
        }
    }

    if (showDialog) {
        CategoriaDialog(
            categoria = editingCategoria,
            onDismiss = { showDialog = false },
            onSave = { categoria ->
                if (editingCategoria != null) {
                    categoriaViewModel.updateCategoria(categoria)
                } else {
                    categoriaViewModel.insertCategoria(categoria)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun CategoriaCard(
    categoria: Categoria,
    onEdit: (Categoria) -> Unit,
    onDelete: (Categoria) -> Unit
) {
    val categoriaColor = try {
        Color(android.graphics.Color.parseColor(categoria.color))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de color
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(categoriaColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = categoria.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = { onEdit(categoria) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { onDelete(categoria) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaDialog(
    categoria: Categoria?,
    onDismiss: () -> Unit,
    onSave: (Categoria) -> Unit
) {
    var nombre by remember { mutableStateOf(categoria?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(categoria?.descripcion ?: "") }
    var color by remember { mutableStateOf(categoria?.color ?: "#FF6B35") }
    var expandedColor by remember { mutableStateOf(false) }

    val coloresDisponibles = listOf(
        "#FF6B35" to "Naranja",
        "#4ECDC4" to "Verde Agua",
        "#45B7D1" to "Azul",
        "#96CEB4" to "Verde Claro",
        "#FFEAA7" to "Amarillo",
        "#DDA0DD" to "Violeta",
        "#F39C12" to "Amarillo Oscuro",
        "#E74C3C" to "Rojo",
        "#9B59B6" to "Púrpura",
        "#2ECC71" to "Verde"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (categoria == null) "🏷️ Nueva Categoría" else "✏️ Editar Categoría")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: Perros Pequeños, Cachorros...") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    placeholder = { Text("Describe qué tipo de mascotas incluye...") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Selector de color
                ExposedDropdownMenuBox(
                    expanded = expandedColor,
                    onExpandedChange = { expandedColor = !expandedColor }
                ) {
                    OutlinedTextField(
                        value = coloresDisponibles.find { it.first == color }?.second ?: "Personalizado",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Color") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try {
                                            Color(android.graphics.Color.parseColor(color))
                                        } catch (e: Exception) {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    )
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedColor) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedColor,
                        onDismissRequest = { expandedColor = false }
                    ) {
                        coloresDisponibles.forEach { (colorHex, colorName) ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color(android.graphics.Color.parseColor(colorHex)))
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(colorName)
                                    }
                                },
                                onClick = {
                                    color = colorHex
                                    expandedColor = false
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
                    val nuevaCategoria = Categoria(
                        categoriaId = categoria?.categoriaId ?: 0,
                        nombre = nombre,
                        descripcion = descripcion,
                        color = color
                    )
                    onSave(nuevaCategoria)
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
