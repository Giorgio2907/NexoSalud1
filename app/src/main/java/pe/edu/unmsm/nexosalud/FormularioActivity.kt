package pe.edu.unmsm.nexosalud

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormularioActivity : BaseActivity() {
    private var deteccion = System.currentTimeMillis()
    private var plazo = deteccion + 24 * Reglas.HORA
    private lateinit var codigo: TextInputEditText
    private lateinit var accion: TextInputEditText
    private lateinit var responsable: Spinner
    private lateinit var guardar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)
        preparar("Nueva tarea")
        codigo = findViewById(R.id.codigo)
        accion = findViewById(R.id.accion)
        responsable = findViewById(R.id.responsable)
        guardar = findViewById(R.id.guardar)
        responsable.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Reglas.responsables)
        deteccion = savedInstanceState?.getLong("deteccion") ?: deteccion
        plazo = savedInstanceState?.getLong("plazo") ?: plazo
        actualizarFechas()
        findViewById<MaterialButton>(R.id.deteccion).setOnClickListener {
            elegirFecha(deteccion) { deteccion = it; actualizarFechas() }
        }
        findViewById<MaterialButton>(R.id.plazo).setOnClickListener {
            elegirFecha(plazo) { plazo = it; actualizarFechas() }
        }
        guardar.setOnClickListener { guardarTarea() }
    }

    private fun actualizarFechas() {
        findViewById<MaterialButton>(R.id.deteccion).text = "Detección · ${Fechas.texto(deteccion)}"
        findViewById<MaterialButton>(R.id.plazo).text = "Plazo · ${Fechas.texto(plazo)}"
    }

    private fun guardarTarea() {
        val cod = codigo.text.toString()
        val act = accion.text.toString()
        val nombre = responsable.selectedItem.toString()
        try {
            Reglas.validarFormulario(cod, act, nombre, deteccion, plazo, System.currentTimeMillis())
        } catch (e: IllegalArgumentException) {
            mensaje(e.message ?: "Revisa los datos."); return
        }
        // Se toman valores fijos antes de enviar la escritura al hilo de trabajo.
        val fechaDeteccion = deteccion
        val fechaPlazo = plazo
        guardar.isEnabled = false
        consultar({ db.crear(cod, act, nombre, fechaDeteccion, fechaPlazo) },
            error = { guardar.isEnabled = true }) { id ->
            mensaje("Tarea registrada")
            startActivity(Intent(this, DetalleActivity::class.java).putExtra("tarea_id", id))
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("deteccion", deteccion)
        outState.putLong("plazo", plazo)
        super.onSaveInstanceState(outState)
    }
}
