package com.example.veterinariaapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.veterinariaapp.VeterinariaApplication
import com.example.veterinariaapp.ui.screens.MascotasScreen
import com.example.veterinariaapp.ui.screens.PropietariosScreen
import com.example.veterinariaapp.ui.screens.ServiciosScreen
import com.example.veterinariaapp.ui.screens.HistorialMedicoScreen
import com.example.veterinariaapp.ui.screens.CategoriasScreen
import com.example.veterinariaapp.ui.screens.HomeScreen
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavGraph(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as VeterinariaApplication
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.HOME
    ) {
        composable(NavigationRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(NavigationRoutes.MASCOTAS) {
            MascotasScreen(navController = navController, application = application, onNavigateToHistorial = { mascotaId ->
                navController.navigate(NavigationRoutes.HISTORIAL + "/$mascotaId")
            })
        }
        composable(NavigationRoutes.PROPIETARIOS) {
            PropietariosScreen(navController = navController, application = application)
        }
        composable(NavigationRoutes.SERVICIOS) {
            ServiciosScreen(navController = navController, application = application)
        }
        composable(NavigationRoutes.HISTORIAL + "/{mascotaId}") { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")?.toLongOrNull()
            HistorialMedicoScreen(navController = navController, application = application, mascotaId = mascotaId)
        }
        composable(NavigationRoutes.HISTORIAL) {
            HistorialMedicoScreen(navController = navController, application = application)
        }
        composable(NavigationRoutes.CATEGORIAS) {
            CategoriasScreen(navController = navController, application = application)
        }
    }
}
