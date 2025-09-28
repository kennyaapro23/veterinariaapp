// MainActivity.kt: Actividad principal de la aplicación, gestiona la navegación y la interfaz principal.

package com.example.veterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.veterinariaapp.ui.theme.VeterinariaappTheme
import com.example.veterinariaapp.ui.screens.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama a installSplashScreen() antes de super.onCreate()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeterinariaappTheme {
                VeterinariaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaApp() {
    val application = androidx.compose.ui.platform.LocalContext.current.applicationContext as VeterinariaApplication
    var currentScreen by remember { mutableStateOf("dashboard") }
    var selectedMascotaId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            VeterinariaTopBar(
                currentScreen = currentScreen,
                onNavigateBack = {
                    currentScreen = "dashboard"
                    selectedMascotaId = null
                }
            )
        },
        bottomBar = {
            VeterinariaBottomBar(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    currentScreen = screen
                    if (screen != "historial") {
                        selectedMascotaId = null
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "dashboard" -> DashboardScreen(
                    application = application,
                    onNavigateToMascotas = { currentScreen = "mascotas" },
                    onNavigateToHistorial = { currentScreen = "historial" },
                    onNavigateToServicios = { currentScreen = "servicios" },
                    onNavigateToPropietarios = { currentScreen = "propietarios" }
                )
                "mascotas" -> MascotasScreen(
                    application = application,
                    onNavigateToHistorial = { mascotaId ->
                        selectedMascotaId = mascotaId
                        currentScreen = "historial"
                    }
                )
                "historial" -> HistorialMedicoScreen(
                    application = application,
                    mascotaId = selectedMascotaId
                )
                "servicios" -> ServiciosScreen(application = application)
                "propietarios" -> PropietariosScreen(application = application)
                "categorias" -> CategoriasScreen(application = application)
            }
        }
    }
}

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
                        else -> "🏥 Veterinaria App"
                    }
                )
            }
        },
        navigationIcon = {
            if (currentScreen != "dashboard") {
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
            if (currentScreen == "dashboard") {
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
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        // Dashboard
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = if (currentScreen == "dashboard")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    "Inicio",
                    color = if (currentScreen == "dashboard")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            selected = currentScreen == "dashboard",
            onClick = { onNavigate("dashboard") }
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
            onClick = { onNavigate("mascotas") }
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
            onClick = { onNavigate("historial") }
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
            onClick = { onNavigate("servicios") }
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
            onClick = { onNavigate("propietarios") }
        )
    }
}

@Composable
fun DashboardScreen(
    application: VeterinariaApplication,
    onNavigateToMascotas: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onNavigateToPropietarios: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "🏥 Panel Veterinario",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Tarjetas de acceso rápido
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DashboardCard(
                title = "Mascotas",
                icon = Icons.Default.Face,
                description = "Registrar y gestionar pacientes",
                onClick = onNavigateToMascotas,
                modifier = Modifier.weight(1f)
            )

            DashboardCard(
                title = "Historial",
                icon = Icons.AutoMirrored.Filled.List,
                description = "Consultas y tratamientos",
                onClick = onNavigateToHistorial,
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
                onClick = onNavigateToServicios,
                modifier = Modifier.weight(1f)
            )

            DashboardCard(
                title = "Propietarios",
                icon = Icons.Default.Person,
                description = "Información de clientes",
                onClick = onNavigateToPropietarios,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resumen rápido
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📊 Sistema Veterinario Configurado",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("✅ Base de datos Room activa")
                Text("✅ CRUD completo para mascotas")
                Text("✅ Historial médico digital")
                Text("✅ Gestión de servicios")
                Text("✅ Arquitectura MVVM")
            }
        }
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