package pe.edu.unmsm.nexosalud

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar

open class BaseActivity : AppCompatActivity() {
    protected val app get() = application as NexoApp
    protected val db get() = app.baseDatos
    protected fun preparar(titulo: String, volver: Boolean = true) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val barra = findViewById<MaterialToolbar>(R.id.barra)
        barra.title = titulo
        if (volver) {
            barra.setNavigationIcon(R.drawable.ic_volver)
            barra.setNavigationOnClickListener { finish() }
        }
        val raiz = findViewById<View>(R.id.raiz)
        val izquierda = raiz.paddingLeft; val derecha = raiz.paddingRight
        val arriba = raiz.paddingTop; val abajo = raiz.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(izquierda + barras.left, arriba + barras.top,
                derecha + barras.right, abajo + barras.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(raiz)
    }

    protected fun mensaje(texto: String) = Toast.makeText(this, texto, Toast.LENGTH_LONG).show()

    // El bloque SQL corre fuera del hilo visual. El resultado vuelve al hilo de la pantalla.
    protected fun <T> consultar(trabajo: () -> T, error: () -> Unit = {}, listo: (T) -> Unit) {
        app.trabajo.execute {
            val resultado = runCatching(trabajo)
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                resultado.fold(listo) {
                    error()
                    MaterialAlertDialogBuilder(this).setTitle("No se pudo completar")
                        .setMessage(it.message ?: "Inténtalo nuevamente.")
                        .setPositiveButton("Aceptar", null).show()
                }
            }
        }
    }

    protected fun elegirFecha(actual: Long, alElegir: (Long) -> Unit) {
        val c = Calendar.getInstance().apply { timeInMillis = actual }
        DatePickerDialog(this, { _, ano, mes, dia ->
            TimePickerDialog(this, { _, hora, minuto ->
                val nueva = Calendar.getInstance().apply {
                    set(ano, mes, dia, hora, minuto, 0); set(Calendar.MILLISECOND, 0)
                }
                alElegir(nueva.timeInMillis)
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }
}
