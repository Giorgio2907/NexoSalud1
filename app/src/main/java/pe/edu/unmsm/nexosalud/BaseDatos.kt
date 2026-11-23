package pe.edu.unmsm.nexosalud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Locale

class BaseDatos(context: Context, nombre: String = "nexo_avance1.db") :
    SQLiteOpenHelper(context, nombre, null, 1) {

    override fun onCreate(db: SQLiteDatabase) { db.execSQL(SQL_TAREAS) }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Versión inicial. No se borran registros al actualizar.
        error("Falta definir la migración de $oldVersion a $newVersion.")
    }

    fun crear(codigo: String, accion: String, responsable: String,
              deteccion: Long, plazo: Long): Long {
        val limpio = codigo.trim().uppercase(Locale.ROOT)
        Reglas.validarFormulario(limpio, accion, responsable, deteccion,
            plazo, System.currentTimeMillis())
        val datos = ContentValues().apply {
            put("codigo", limpio)
            put("accion", accion.trim())
            put("responsable", responsable)
            put("deteccion", deteccion)
            put("plazo", plazo)
            put("estado", "Pendiente")
        }
        return try {
            writableDatabase.insertOrThrow("tareas", null, datos)
        } catch (e: SQLiteConstraintException) {
            throw IllegalArgumentException("Ya existe esa acción para la referencia o sus datos no son válidos.", e)
        }
    }

    fun listar(): List<Tarea> {
        val resultado = mutableListOf<Tarea>()
        readableDatabase.rawQuery("SELECT * FROM tareas ORDER BY id DESC", null).use { cursor ->
            while (cursor.moveToNext()) resultado.add(leerTarea(cursor))
        }
        return resultado
    }

    fun obtener(id: Long): Tarea {
        readableDatabase.rawQuery("SELECT * FROM tareas WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            require(cursor.moveToFirst()) { "No se encontró la tarea." }
            return leerTarea(cursor)
        }
    }

    private fun leerTarea(c: Cursor): Tarea {
        fun texto(columna: String) = c.getString(c.getColumnIndexOrThrow(columna))
        fun numero(columna: String) = c.getLong(c.getColumnIndexOrThrow(columna))
        return Tarea(numero("id"), texto("codigo"), texto("accion"), texto("responsable"),
            numero("deteccion"), numero("plazo"), texto("estado"))
    }

    companion object {
        const val SQL_TAREAS = """
            CREATE TABLE tareas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo TEXT NOT NULL COLLATE NOCASE,
                accion TEXT NOT NULL COLLATE NOCASE,
                responsable TEXT NOT NULL,
                deteccion INTEGER NOT NULL,
                plazo INTEGER NOT NULL CHECK(plazo >= deteccion),
                estado TEXT NOT NULL DEFAULT 'Pendiente',
                UNIQUE(codigo, accion)
            )
        """
    }
}
