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
import com.example.veterinariaapp.data.entities.Propietario
import com.example.veterinariaapp.viewmodel.PropietarioViewModel
import com.example.veterinariaapp.viewmodel.PropietarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropietariosScreen(application: VeterinariaApplication) {
    val propietarioViewModel: PropietarioViewModel = viewModel(
        factory = PropietarioViewModelFactory(application.propietarioRepository)
    )

    val propietarios by propietarioViewModel.propietarios.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingPropietario by remember { mutableStateOf<Propietario?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "👥 Propietarios",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = {
                    editingPropietario = null
                    showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Propietario")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📊 Clientes Registrados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Total de propietarios: ${propietarios.size}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(propietarios) { propietario ->
                PropietarioCard(
                    propietario = propietario,
                    onEdit = {
                        editingPropietario = it
                        showDialog = true
                    },
                    onDelete = { propietarioViewModel.deletePropietario(it) }
                )
            }
        }
    }

    if (showDialog) {
        PropietarioDialog(
            propietario = editingPropietario,
            onDismiss = { showDialog = false },
            onSave = { propietario ->
                if (editingPropietario != null) {
                    propietarioViewModel.updatePropietario(propietario)
                } else {
                    propietarioViewModel.insertPropietario(propietario)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun PropietarioCard(
    propietario: Propietario,
    onEdit: (Propietario) -> Unit,
    onDelete: (Propietario) -> Unit
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
                        text = propietario.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(propietario.telefono)
                    }
                    Row {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(propietario.email)
                    }
                    Row {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(propietario.direccion)
                    }
                }

                Column {
                    IconButton(onClick = { onEdit(propietario) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(propietario) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropietarioDialog(
    propietario: Propietario?,
    onDismiss: () -> Unit,
    onSave: (Propietario) -> Unit
) {
    var nombre by remember { mutableStateOf(propietario?.nombre ?: "") }
    var telefono by remember { mutableStateOf(propietario?.telefono ?: "") }
    var email by remember { mutableStateOf(propietario?.email ?: "") }
    var direccion by remember { mutableStateOf(propietario?.direccion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (propietario == null) "👤 Nuevo Propietario" else "✏️ Editar Propietario")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val nuevoPropietario = Propietario(
                        propietarioId = propietario?.propietarioId ?: 0,
                        nombre = nombre,
                        telefono = telefono,
                        email = email,
                        direccion = direccion
                    )
                    onSave(nuevoPropietario)
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
