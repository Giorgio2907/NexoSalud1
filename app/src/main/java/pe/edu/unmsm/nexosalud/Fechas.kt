package pe.edu.unmsm.nexosalud

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Fechas {
    // Se crea un formato por llamada porque SimpleDateFormat no es seguro entre hilos.
    fun texto(valor: Long): String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-PE")).format(Date(valor))
}
