package com.example.veterinariaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.ui.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaTopBar(
    currentScreen: String,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when(currentScreen) {
                        "mascotas" -> "🐾 Registro de Mascotas"
                        "historial" -> "📋 Historial Médico"
                        "servicios" -> "🏥 Servicios"
                        "propietarios" -> "👥 Propietarios"
                        "categorias" -> "🏷️ Categorías"
                        else -> "🏥 Veterinaria App"
                    }
                )
            }
        },
        navigationIcon = {
            if (currentScreen != "home") {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al inicio")
                }
            } else {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Veterinaria",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        },
        actions = {
            if (currentScreen == "home") {
                IconButton(onClick = { /* Aquí puedes agregar configuraciones */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Configuración")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun VeterinariaBottomBar(
    currentScreen: String,
    navController: NavController
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        // Inicio
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = if (currentScreen == "home")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Inicio",
                    color = if (currentScreen == "home")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "home",
            onClick = { navController.navigate(NavigationRoutes.HOME) }
        )

        // Mascotas
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Face,
                    contentDescription = null,
                    tint = if (currentScreen == "mascotas")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Mascotas",
                    color = if (currentScreen == "mascotas")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "mascotas",
            onClick = { navController.navigate(NavigationRoutes.MASCOTAS) }
        )

        // Historial
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = if (currentScreen == "historial")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Historial",
                    color = if (currentScreen == "historial")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "historial",
            onClick = { navController.navigate(NavigationRoutes.HISTORIAL) }
        )

        // Servicios
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = if (currentScreen == "servicios")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Servicios",
                    color = if (currentScreen == "servicios")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "servicios",
            onClick = { navController.navigate(NavigationRoutes.SERVICIOS) }
        )

        // Propietarios
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = if (currentScreen == "propietarios")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Clientes",
                    color = if (currentScreen == "propietarios")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "propietarios",
            onClick = { navController.navigate(NavigationRoutes.PROPIETARIOS) }
        )
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
