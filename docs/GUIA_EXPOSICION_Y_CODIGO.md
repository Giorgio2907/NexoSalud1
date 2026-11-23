# Guía de exposición y estudio del código

## Tiempo y dificultad

Para este alcance, una exposición de 10 a 12 minutos es razonable para cuatro personas. La siguiente distribución suma 11 minutos de intervención. Ensayen también los cambios de expositor y la demostración. Las preguntas del profesor pueden ampliar el tiempo total a 15 o 20 minutos.

La dificultad es baja a intermedia para quienes ya conocen variables, funciones, clases y SQL básico. Los puntos que requieren más estudio son el ciclo de vida de una Activity, la lectura de un Cursor y el trabajo en segundo plano. Si Kotlin y SQL son nuevos para el grupo, harán falta sesiones de práctica antes de poder defender el código.

## Reparto propuesto entre cuatro integrantes

| Integrante | Tiempo orientativo | Qué explica | Archivos principales |
|---|---|---|---|
| 1 | 2 min 30 s | Problema, alcance, modelo de datos en Kotlin y bandeja | `Tarea.kt`, `MainActivity.kt`, `activity_main.xml` |
| 2 | 2 min 30 s | Formulario, controles, validaciones y fechas | `FormularioActivity.kt`, `activity_formulario.xml`, `Reglas` |
| 3 | 3 min | Tabla SQLite, inserción, consultas, duplicados y persistencia | `BaseDatos.kt`, `modelo_datos.sql` |
| 4 | 3 min | Paso del identificador, detalle, hilo de trabajo y demostración de persistencia | `DetalleActivity.kt`, `BaseActivity.kt`, `NexoApp.kt`, `Fechas.kt` |

La asignación es por contenido. Todos deben poder seguir el recorrido completo del dato y contestar las preguntas básicas de las otras partes.

Si les conceden solo ocho minutos, reduzcan cada intervención a aproximadamente dos minutos y muestren un registro completo. Si tienen quince, agreguen la prueba de campos inválidos y una consulta del código SQL. Con cinco minutos, prioricen el objetivo, una demostración y las tres operaciones de base de datos, con cerca de un minuto por persona y otro para transiciones.

## Guion breve

### Integrante 1

“Nexo Salud propone registrar tareas internas relacionadas con observaciones de referencias. En este avance implementamos tres acciones concretas, registrar una tarea, listar las registradas y consultar su detalle. La información se conserva en SQLite. Las tareas empiezan en Pendiente. La atención, el cierre y el control de plazos se desarrollarán en siguientes incrementos.”

Muestra `Tarea`, explica que contiene datos y abre la bandeja. Señala el botón Nueva tarea y la lista. Explica que `onResume` consulta la base cada vez que se vuelve a esa pantalla.

### Integrante 2

“El formulario obtiene un código, la acción pendiente, un responsable y dos fechas. Al pulsar Guardar, leemos los controles y validamos. Por ejemplo, el código no puede estar vacío y el plazo debe ser futuro. Si hay un error, se informa y no se guarda el registro.”

Muestra una validación y completa un caso válido. Señala el listener de Guardar, los valores leídos y la llamada a `db.crear`.

### Integrante 3

“SQLite guarda una fila por tarea. La clave primaria es un número generado por la base. El código de referencia se guarda como un campo aparte. Usamos ContentValues para insertar y una consulta con un parámetro para obtener una tarea específica. El Cursor recorre las filas y las convierte a objetos Tarea.”

Muestra la tabla y las funciones `crear`, `listar` y `obtener`. Explica la restricción de duplicados y que cerrar la app no borra el archivo de la base.

### Integrante 4

“Después de guardar, abrimos el detalle enviando el identificador de la tarea mediante un Intent. El detalle vuelve a consultar SQLite con ese identificador. Las consultas se ejecutan fuera del hilo que dibuja la interfaz y su resultado se muestra después en pantalla.”

Vuelve a la bandeja, abre el detalle y demuestra la persistencia al cerrar y reabrir. Explica que todo funciona localmente y termina con el alcance del siguiente incremento.

## Recorrido completo de un registro

1. `MainActivity.onCreate` prepara el botón Nueva tarea.
2. El botón abre `FormularioActivity` mediante un Intent.
3. `FormularioActivity.onCreate` enlaza los controles XML con variables Kotlin.
4. El usuario llena los campos y pulsa Guardar.
5. `guardarTarea` lee el texto y la opción elegida del Spinner.
6. `Reglas.validarFormulario` revisa la información usando `require`.
7. Se desactiva el botón para evitar dos guardados simultáneos.
8. `BaseActivity.consultar` manda la operación al hilo de trabajo de `NexoApp`.
9. `BaseDatos.crear` normaliza el código, valida nuevamente e inserta una fila mediante ContentValues.
10. SQLite devuelve el nuevo identificador numérico.
11. El resultado regresa al hilo de la interfaz.
12. Se abre el detalle con el identificador y se cierra el formulario.
13. `DetalleActivity` consulta la fila con `db.obtener`.
14. Al volver a la bandeja, `onResume` ejecuta `db.listar` y dibuja las tarjetas.

La validación se realiza tanto en la pantalla como en la base. En la pantalla da una respuesta inmediata. En la clase de datos impide registrar entradas inválidas si en el futuro se la invoca desde otra pantalla.

## Explicación de cada archivo

### Tarea y Reglas

`data class Tarea` agrupa los campos de un registro. `Long` representa el identificador y las fechas; `String` representa los textos. `val` indica que cada propiedad de ese objeto se asigna al crearlo y luego no se reasigna.

`object Reglas` es un objeto compartido que contiene el catálogo de responsables y las validaciones. `HORA` equivale a 3 600 000 milisegundos. El valor predeterminado del plazo se calcula sumando 24 horas a la hora de apertura del formulario.

`require` comprueba una condición. Cuando la condición es falsa, lanza una excepción con el mensaje especificado. `guardarTarea` captura esa excepción y muestra el aviso. Esa es la razón de que una entrada inválida no llegue al INSERT.

### MainActivity

Una Activity representa una pantalla con su comportamiento. `setContentView` elige el XML y `findViewById` busca un control por su identificador.

`onCreate` configura la pantalla y sus listeners. `onResume` se ejecuta cuando vuelve al primer plano. Se usa para refrescar la bandeja después de registrar una tarea.

`mostrar` recibe una lista de tareas. Primero actualiza el total y el mensaje de lista vacía. Luego limpia las tarjetas anteriores, recorre las tareas y crea una tarjeta por cada una. Esto evita duplicar visualmente los registros cada vez que se regresa a la bandeja.

`layoutInflater.inflate` convierte `item_tarea.xml` en una vista utilizable. El listener de esa tarjeta abre el detalle enviando el ID de su tarea.

### FormularioActivity

Los `lateinit var` se inicializan después de `setContentView`, cuando ya existen los controles. Antes de ese momento, todavía no pueden usarse.

El Spinner muestra una lista mediante `ArrayAdapter`. La opción seleccionada se convierte en el nombre del responsable. Los botones de fecha abren un selector de día y luego uno de hora.

`guardarTarea` toma una copia de los valores antes de iniciar el trabajo de base de datos. Así la escritura usa los datos que el usuario confirmó, aunque después se interactúe con la pantalla.

`guardar.isEnabled = false` bloquea otro envío mientras se guarda. Si falla la operación, vuelve a habilitarse. Si se completa, se muestra el detalle del registro nuevo.

`onSaveInstanceState` conserva las fechas elegidas durante una recreación de la pantalla, como una rotación. Los campos de texto y el Spinner tienen IDs para que Android pueda restaurar su estado de vista. Su comportamiento visual debe comprobarse en el dispositivo de presentación.

### BaseDatos

`SQLiteOpenHelper` gestiona la apertura del archivo y la creación o actualización de su estructura. `onCreate` se ejecuta cuando la base se crea por primera vez; no se ejecuta cada vez que se abre la app.

La tabla tiene siete columnas en total. `id` es una clave primaria autogenerada, `codigo` identifica la referencia, `accion` describe qué hacer, `responsable` indica quién lo atendería, `deteccion` y `plazo` guardan tiempos numéricos, y `estado` guarda Pendiente. La restricción UNIQUE sobre código y acción no es una columna adicional.

`ContentValues` contiene pares de nombre de columna y valor. `insertOrThrow` inserta la fila y devuelve el ID. Si se infringe una restricción, lanza un error. La aplicación lo convierte en un aviso comprensible.

La restricción `UNIQUE(codigo, accion)` evita registrar dos veces la misma combinación. Se permite tener distintas acciones para una referencia. `COLLATE NOCASE` compara sin distinguir mayúsculas y minúsculas según las reglas incorporadas de SQLite. No debe presentarse como una búsqueda lingüística completa para todos los acentos.

`listar` usa `ORDER BY id DESC` para colocar primero lo último registrado. `obtener` filtra por `id = ?`. El signo `?` se reemplaza por el valor pasado en el arreglo de argumentos; los datos no se concatenan dentro del SQL.

El Cursor es el resultado de una consulta. `moveToNext` recorre sus filas; `moveToFirst` se coloca en la primera. `leerTarea` convierte los valores de una fila a un objeto Kotlin. El bloque `use` cierra el Cursor al terminar.

La versión actual de la base es 1. `onUpgrade` está preparado como punto de extensión, pero todavía no contiene una migración. Una próxima versión que cambie la tabla deberá definir cómo conservar los datos existentes.

### DetalleActivity

`intent.getLongExtra` obtiene el identificador enviado desde la tarjeta o desde el formulario. Ese ID es diferente del código de referencia. La pantalla usa el ID para leer exactamente una tarea.

El detalle muestra los datos almacenados y permite volver a la bandeja. Este incremento no edita ni elimina tareas, ni modifica su estado.

### NexoApp y BaseActivity

`NexoApp` es la clase de aplicación declarada en el manifiesto. Tiene una instancia compartida de BaseDatos y un ejecutor de un solo hilo. `by lazy` crea la instancia cuando se necesita por primera vez.

`BaseActivity` reúne utilidades repetidas por las tres pantallas. `preparar` configura la barra superior y el espacio para las barras del sistema y el teclado. `mensaje` muestra un Toast. `elegirFecha` presenta los selectores de fecha y hora.

`consultar` recibe una operación y una función para su resultado. `app.trabajo.execute` realiza la operación fuera del hilo de la interfaz. `runOnUiThread` vuelve al hilo visual para actualizar los controles. La condición `isFinishing || isDestroyed` evita actualizar una Activity que ya terminó.

`runCatching` representa éxito o error. Si la operación falla, se muestra un diálogo y se ejecuta el callback de error. El formulario lo usa para habilitar nuevamente Guardar.

### Fechas

Las fechas se guardan como milisegundos y se muestran como `dd/MM/yyyy HH:mm`. Separar almacenamiento y presentación permite consultar y comparar valores numéricos sin depender del formato escrito.

## Preguntas que los cuatro deben poder responder

| Pregunta | Idea central de la respuesta |
|---|---|
| ¿Dónde se guardan los datos? | En el archivo SQLite de esta instalación, dentro del almacenamiento privado de la app. |
| ¿Qué demuestra este incremento? | Captura, validación, almacenamiento persistente y consulta de tareas. |
| ¿Por qué se usa una data class? | Para representar de manera clara una fila como un objeto Kotlin. |
| ¿Qué diferencia hay entre ID y código? | El ID es la clave de una tarea; el código identifica la referencia y puede aparecer en distintas tareas. |
| ¿Por qué usar onResume en la bandeja? | Para consultar nuevamente al regresar desde otra pantalla. |
| ¿Qué recibe el detalle? | Un ID numérico enviado mediante un Intent. |
| ¿Qué pasa si se guarda dos veces lo mismo? | UNIQUE impide duplicar código y acción, y se muestra un aviso. |
| ¿Qué significa el signo de interrogación en el SELECT? | Es un parámetro cuyo valor se pasa aparte del texto SQL. |
| ¿Qué hace el Cursor? | Permite leer las filas devueltas por una consulta. |
| ¿Por qué hay un hilo de trabajo? | Para que las operaciones de base no bloqueen la interfaz. |
| ¿Se cambia el estado de la tarea? | En este incremento todas se registran como Pendiente. Los cambios vendrán después. |
| ¿Se comunican varios teléfonos? | Cada instalación es local y mantiene su propia base. |
| ¿La app reenvía en REFCON? | Este incremento registra y consulta tareas internas; no realiza esa operación oficial. |
| ¿Por qué se eligió SQLite directo? | Hace visible el SQL que el grupo debe comprender en este avance. Room puede evaluarse en una ampliación. |
| ¿Ya está probado en Android? | Hay comprobaciones de código y esquema; la ejecución visual y las pruebas instrumentadas deben completarse en su equipo. |

## Práctica para dominarlo

En la primera sesión, abran los archivos en el orden de esta guía y sigan un registro desde Guardar hasta la consulta del detalle. En la segunda, cada integrante explica su parte sin leer el guion. En la tercera, intercambien preguntas entre módulos y realicen un ensayo cronometrado.

Una señal útil de dominio es poder señalar dónde se origina cada valor, qué función lo valida, qué columna lo almacena y qué consulta lo recupera. Además, cada integrante debe poder anticipar el resultado de un formulario vacío, un duplicado y una consulta por ID inexistente.
