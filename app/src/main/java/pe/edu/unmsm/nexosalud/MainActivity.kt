package pe.edu.unmsm.nexosalud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preparar("Nexo Salud", false)
        findViewById<MaterialButton>(R.id.nueva).setOnClickListener {
            startActivity(Intent(this, FormularioActivity::class.java))
        }
    }

    // Se ejecuta también al volver del formulario o del detalle.
    override fun onResume() {
        super.onResume()
        consultar({ db.listar() }) { tareas -> mostrar(tareas) }
    }

    private fun mostrar(tareas: List<Tarea>) {
        findViewById<TextView>(R.id.resumen).text = "${tareas.size} tareas registradas"
        findViewById<View>(R.id.vacio).visibility = if (tareas.isEmpty()) View.VISIBLE else View.GONE
        val lista = findViewById<LinearLayout>(R.id.lista)
        lista.removeAllViews()
        for (tarea in tareas) {
            val tarjeta = layoutInflater.inflate(R.layout.item_tarea, lista, false)
            tarjeta.findViewById<TextView>(R.id.codigo).text = "${tarea.codigo} · Tarea #${tarea.id}"
            tarjeta.findViewById<TextView>(R.id.accion).text = tarea.accion
            tarjeta.findViewById<TextView>(R.id.responsable).text = tarea.responsable
            tarjeta.findViewById<TextView>(R.id.estado).text = tarea.estado
            tarjeta.findViewById<TextView>(R.id.plazo).text = "Plazo · ${Fechas.texto(tarea.plazo)}"
            tarjeta.setOnClickListener {
                val destino = Intent(this, DetalleActivity::class.java)
                destino.putExtra("tarea_id", tarea.id)
                startActivity(destino)
            }
            lista.addView(tarjeta)
        }
    }
}
