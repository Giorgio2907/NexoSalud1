package pe.edu.unmsm.nexosalud

// Una instancia de esta clase representa una fila de la tabla tareas.
data class Tarea(
    val id: Long,
    val codigo: String,
    val accion: String,
    val responsable: String,
    val deteccion: Long,
    val plazo: Long,
    val estado: String
)

object Reglas {
    const val HORA = 60 * 60 * 1000L
    val responsables = listOf("Obstetricia de turno", "Admisión", "Coordinación del CMI")

    // Si un dato es inválido, require detiene el registro con un mensaje.
    fun validarFormulario(codigo: String, accion: String, responsable: String,
                          deteccion: Long, plazo: Long, ahora: Long) {
        require(codigo.trim().length in 3..40 && !codigo.contains('\n')) {
            "El código debe tener de 3 a 40 caracteres en una sola línea."
        }
        require(accion.trim().length in 5..300) { "Describe la acción en 5 a 300 caracteres." }
        require(responsable in responsables) { "Selecciona un responsable." }
        require(deteccion <= ahora) { "La detección no puede estar en el futuro." }
        require(plazo > ahora && plazo >= deteccion) { "El plazo debe ser posterior a la hora actual." }
    }
}
