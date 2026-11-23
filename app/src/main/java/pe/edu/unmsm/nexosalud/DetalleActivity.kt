package pe.edu.unmsm.nexosalud

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class DetalleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        preparar("Detalle de tarea")
        findViewById<MaterialButton>(R.id.volver).setOnClickListener { finish() }
        val id = intent.getLongExtra("tarea_id", -1)
        consultar({ db.obtener(id) }) { tarea ->
            findViewById<TextView>(R.id.codigo).text = "${tarea.codigo} · Tarea #${tarea.id}"
            findViewById<TextView>(R.id.estado).text = tarea.estado
            findViewById<TextView>(R.id.accion).text = tarea.accion
            findViewById<TextView>(R.id.datos).text =
                "Responsable\n${tarea.responsable}\n\nDetección\n${Fechas.texto(tarea.deteccion)}" +
                "\n\nPlazo interno\n${Fechas.texto(tarea.plazo)}"
        }
    }
}
