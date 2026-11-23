package pe.edu.unmsm.nexosalud

import org.junit.Assert.*
import org.junit.Test

class ReglasTest {
    private val ahora = 1_800_000_000_000L
    private fun validar(codigo: String = "REF-001", accion: String = "Completar anexo",
                        responsable: String = "Obstetricia de turno",
                        deteccion: Long = ahora - 1000, plazo: Long = ahora + 1000) {
        Reglas.validarFormulario(codigo, accion, responsable, deteccion, plazo, ahora)
    }
    @Test fun aceptaRegistroValido() { validar() }
    @Test fun rechazaCodigoVacio() {
        assertThrows(IllegalArgumentException::class.java) { validar(codigo = " ") }
    }
    @Test fun rechazaCodigoMultilinea() {
        assertThrows(IllegalArgumentException::class.java) { validar(codigo = "REF\n001") }
    }
    @Test fun rechazaAccionMuyCorta() {
        assertThrows(IllegalArgumentException::class.java) { validar(accion = "Ver") }
    }
    @Test fun rechazaAccionExcesiva() {
        assertThrows(IllegalArgumentException::class.java) { validar(accion = "a".repeat(301)) }
    }
    @Test fun rechazaResponsableFueraDeCatalogo() {
        assertThrows(IllegalArgumentException::class.java) { validar(responsable = "Desconocido") }
    }
    @Test fun rechazaDeteccionFutura() {
        assertThrows(IllegalArgumentException::class.java) { validar(deteccion = ahora + 1) }
    }
    @Test fun rechazaPlazoVencidoOIgualAHoraActual() {
        assertThrows(IllegalArgumentException::class.java) { validar(plazo = ahora - 1) }
        assertThrows(IllegalArgumentException::class.java) { validar(plazo = ahora) }
    }
}
