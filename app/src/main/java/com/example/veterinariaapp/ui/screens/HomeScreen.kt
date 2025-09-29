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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.viewmodel.MascotaViewModel
import com.example.veterinariaapp.viewmodel.MascotaViewModelFactory
import com.example.veterinariaapp.viewmodel.PropietarioViewModel
import com.example.veterinariaapp.viewmodel.PropietarioViewModelFactory
import com.example.veterinariaapp.ui.components.VeterinariaTopBar
import com.example.veterinariaapp.ui.components.VeterinariaBottomBar
import com.example.veterinariaapp.ui.components.DashboardCard
import com.example.veterinariaapp.ui.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as VeterinariaApplication
    val mascotaViewModel: MascotaViewModel = viewModel(
        factory = MascotaViewModelFactory(application.mascotaRepository)
    )
    val propietarioViewModel: PropietarioViewModel = viewModel(
        factory = PropietarioViewModelFactory(application.propietarioRepository)
    )

    val mascotas by mascotaViewModel.mascotas.collectAsState()
    val propietarios by propietarioViewModel.propietarios.collectAsState()

    // Mostrar todas las mascotas (sin filtrar)
    // val perros = mascotas.filter { it.especie.equals("Perro", ignoreCase = true) }

    Scaffold(
        topBar = {
            VeterinariaTopBar(
                currentScreen = "home",
                onNavigateBack = { }
            )
        },
        bottomBar = {
            VeterinariaBottomBar(
                currentScreen = "home",
                navController = navController
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "🏥 Panel Veterinario",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Tarjetas de acceso rápido
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        title = "Mascotas",
                        icon = Icons.Default.Face,
                        description = "Registrar y gestionar pacientes",
                        onClick = { navController.navigate(NavigationRoutes.MASCOTAS) },
                        modifier = Modifier.weight(1f)
                    )

                    DashboardCard(
                        title = "Historial",
                        icon = Icons.Default.List,
                        description = "Consultas y tratamientos",
                        onClick = { navController.navigate(NavigationRoutes.HISTORIAL) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        title = "Servicios",
                        icon = Icons.Default.Build,
                        description = "Catálogo de tratamientos",
                        onClick = { navController.navigate(NavigationRoutes.SERVICIOS) },
                        modifier = Modifier.weight(1f)
                    )

                    DashboardCard(
                        title = "Propietarios",
                        icon = Icons.Default.Person,
                        description = "Información de clientes",
                        onClick = { navController.navigate(NavigationRoutes.PROPIETARIOS) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Lista de mascotas registradas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🐾 Mascotas Registradas",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (mascotas.isEmpty()) {
                            Text(
                                text = "No hay mascotas registradas aún.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Total: ${mascotas.size} mascotas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista de todas las mascotas
            items(mascotas) { mascota ->
                val propietario = propietarios.find { it.propietarioId == mascota.propietarioId }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono según la especie
                        Icon(
                            if (mascota.especie.equals("Perro", ignoreCase = true))
                                Icons.Default.Face
                            else
                                Icons.Default.Favorite, // Para gatos u otras mascotas
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mascota.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${mascota.especie} - ${mascota.raza} • ${mascota.edad} años",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Propietario: ${propietario?.nombre ?: "No encontrado"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                navController.navigate(NavigationRoutes.HISTORIAL + "/${mascota.mascotaId}")
                            }
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Ver historial",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Resumen del sistema
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📊 Sistema Veterinario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("✅ Total mascotas: ${mascotas.size}")
                        val perros = mascotas.count { it.especie.equals("Perro", ignoreCase = true) }
                        val gatos = mascotas.count { it.especie.equals("Gato", ignoreCase = true) }
                        Text("🐕 Perros registrados: $perros")
                        Text("🐱 Gatos registrados: $gatos")
                        Text("✅ Propietarios activos: ${propietarios.size}")
                        Text("✅ Base de datos Room activa")
                        Text("✅ Navegación centralizada")
                    }
                }
            }
        }
    }
}
