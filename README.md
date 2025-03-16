# Aplicación de Preferencias Encriptadas

Esta aplicación Android demuestra el uso de SharedPreferences encriptadas utilizando las últimas tecnologías de Android:
- Kotlin
- Jetpack Compose
- EncryptedSharedPreferences
- Material 3

## Características

La aplicación permite guardar de forma segura las siguientes configuraciones de usuario:

- 👤 Nombre del usuario
- 🌓 Tema oscuro (activado/desactivado)
- 🌍 Idioma preferido (Español/English)
- 🔊 Volumen de notificaciones
- 📅 Última fecha y hora de acceso
- 📍 Última ubicación
- ⏱️ Tiempo total de uso (horas, minutos y segundos)

## Seguridad

Todos los datos se almacenan de forma encriptada utilizando:
- AES-256-GCM para encriptación de valores
- AES-256-SIV para encriptación de claves
- Almacenamiento seguro de Android para las claves maestras

## Estructura del Código

### PreferencesManager.kt

Esta clase maneja toda la lógica de almacenamiento encriptado:

```kotlin
class PreferencesManager(context: Context) {
    // Inicialización de EncryptedSharedPreferences
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(...)

    // Propiedades encriptadas
    var userName: String
    var isDarkTheme: Boolean
    var preferredLanguage: String
    var notificationVolume: Float
    var lastAccessTime: String
    var lastLocation: String
    var totalUsageTime: Long
}
```

Características principales:
- Encriptación automática de datos
- Gestión de sesiones de usuario
- Registro automático de tiempos de acceso
- Cálculo del tiempo total de uso

### MainActivity.kt

Implementa la interfaz de usuario usando Jetpack Compose:

```kotlin
@Composable
fun PreferencesScreen(preferencesManager: PreferencesManager) {
    // Estados de Compose para cada preferencia
    var userName by remember { ... }
    var isDarkTheme by remember { ... }
    // ...

    Column {
        // Campos de entrada y controles
        OutlinedTextField(...)  // Nombre de usuario
        Switch(...)            // Tema oscuro
        RadioButton(...)      // Selección de idioma
        Slider(...)          // Control de volumen
        
        // Información de estado
        Card {
            // Muestra tiempo de uso, último acceso, etc.
        }
    }
}
```

Características de la UI:
- Diseño Material 3
- Campos de entrada intuitivos
- Actualización en tiempo real
- Persistencia automática de cambios
- Formato detallado del tiempo de uso (horas:minutos:segundos)

## Gestión del Tiempo

El tiempo total de uso se calcula y muestra de la siguiente manera:

```kotlin
buildString {
    val totalSeconds = totalUsageTime / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    if (hours > 0) append("$hours horas ")
    if (minutes > 0 || hours > 0) append("$minutes minutos ")
    append("$seconds segundos")
}
```

## Ciclo de Vida

1. Al iniciar la app:
   - Se inicializa el `PreferencesManager`
   - Se cargan las preferencias encriptadas
   - Se inicia el registro de tiempo de uso

2. Durante el uso:
   - Los cambios se guardan automáticamente
   - Se actualiza la última fecha de acceso
   - Se registra la ubicación (si está disponible)

3. Al cerrar la app:
   - Se calcula y guarda el tiempo total de uso
   - Se encriptan todos los datos
   - Se finaliza la sesión

## Requisitos

- Android SDK 35 o superior
- Kotlin 1.9.0 o superior
- Dispositivo con Android 7.0 (API 24) o superior
