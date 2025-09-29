// MainActivity.kt: Actividad principal de la aplicación, gestiona la navegación y la interfaz principal.

package com.example.veterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.veterinariaapp.ui.theme.VeterinariaappTheme
import com.example.veterinariaapp.ui.navigation.NavGraph
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController

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

@Composable
fun VeterinariaApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}