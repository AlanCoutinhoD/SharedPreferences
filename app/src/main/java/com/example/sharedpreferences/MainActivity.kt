package com.example.sharedpreferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharedpreferences.data.PreferencesManager
import androidx.lifecycle.lifecycleScope
import com.example.sharedpreferences.ui.theme.SharedPreferencesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        preferencesManager.startSession()

        enableEdgeToEdge()
        setContent {
            val isDarkTheme = remember { mutableStateOf(preferencesManager.isDarkTheme) }
            
            SharedPreferencesTheme(darkTheme = isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreferencesScreen(preferencesManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesManager.endSession()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(preferencesManager: PreferencesManager) {
    var userName by remember { mutableStateOf(preferencesManager.userName) }
    var isDarkTheme by remember { mutableStateOf(preferencesManager.isDarkTheme) }
    var preferredLanguage by remember { mutableStateOf(preferencesManager.preferredLanguage) }
    var notificationVolume by remember { mutableStateOf(preferencesManager.notificationVolume) }
    val lastAccess by remember { mutableStateOf(preferencesManager.lastAccessTime) }
    val lastLocation by remember { mutableStateOf(preferencesManager.lastLocation) }
    val totalUsageTime by remember { mutableStateOf(preferencesManager.totalUsageTime) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Configuración de Usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { 
                userName = it
                preferencesManager.userName = it
            },
            label = { Text("Nombre de Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tema Oscuro", modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { 
                    isDarkTheme = it
                    preferencesManager.isDarkTheme = it
                }
            )
        }

        Column {
            Text("Idioma Preferido")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = preferredLanguage == "es",
                    onClick = { 
                        preferredLanguage = "es"
                        preferencesManager.preferredLanguage = "es"
                    }
                )
                Text("Español")
                RadioButton(
                    selected = preferredLanguage == "en",
                    onClick = { 
                        preferredLanguage = "en"
                        preferencesManager.preferredLanguage = "en"
                    }
                )
                Text("English")
            }
        }

        Column {
            Text("Volumen de Notificaciones: ${(notificationVolume * 100).toInt()}%")
            Slider(
                value = notificationVolume,
                onValueChange = { 
                    notificationVolume = it
                    preferencesManager.notificationVolume = it
                },
                valueRange = 0f..1f
            )
        }

        // Información de estado
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Último acceso: $lastAccess")
                Text("Última ubicación: ${lastLocation.ifEmpty { "No disponible" }}")
                Text(
                    text = buildString {
                        val totalSeconds = totalUsageTime / 1000
                        val hours = totalSeconds / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        val seconds = totalSeconds % 60
                        if (hours > 0) {
                            append("$hours horas ")
                        }
                        if (minutes > 0 || hours > 0) {
                            append("$minutes minutos ")
                        }
                        append("$seconds segundos")
                    }
                )
            }
        }
    }
}