package pe.edu.unmsm.nexosalud

import android.app.Application
import java.util.concurrent.Executors

class NexoApp : Application() {
    val baseDatos by lazy { BaseDatos(this) }
    // Un solo hilo mantiene las operaciones de SQLite ordenadas y evita bloquear la pantalla.
    val trabajo = Executors.newSingleThreadExecutor()
}
