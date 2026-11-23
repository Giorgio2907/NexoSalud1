package pe.edu.unmsm.nexosalud

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseDatosTest {
    private val contexto get() = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var db: BaseDatos
    private val nombre = "nexo_avance1_pruebas.db"
    private fun crear(): Long {
        val ahora = System.currentTimeMillis()
        return db.crear(" ref-test ", "Completar anexo", "Admisión", ahora - 1000, ahora + Reglas.HORA)
    }
    @Before fun preparar() { contexto.deleteDatabase(nombre); db = BaseDatos(contexto, nombre) }
    @After fun terminar() { db.close(); contexto.deleteDatabase(nombre) }

    @Test fun persisteYNormalizaElCodigo() {
        val id = crear(); db.close(); db = BaseDatos(contexto, nombre)
        assertEquals("REF-TEST", db.obtener(id).codigo)
        assertEquals("Pendiente", db.obtener(id).estado)
    }
    @Test fun duplicadoNoGeneraOtroRegistro() {
        crear()
        assertThrows(IllegalArgumentException::class.java) { crear() }
        assertEquals(1, db.listar().size)
    }
    @Test fun idInexistenteMuestraError() {
        assertThrows(IllegalArgumentException::class.java) { db.obtener(999) }
    }
}
