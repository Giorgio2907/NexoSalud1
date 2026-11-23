# Nexo Salud Avance 1

Aplicaciones Móviles. Versión acotada para presentar el registro y la consulta de tareas internas de subsanación.

## Alcance de esta entrega

Esta aplicación permite registrar una tarea, guardarla en SQLite, verla en una bandeja y consultar su detalle. Incluye validación de campos, selección de responsable y selección de fechas. Todas las tareas se registran como Pendiente.

El avance tiene tres pantallas, una tabla y ocho archivos Kotlin de producción. El código de producción suma 332 líneas, contando comentarios y espacios. Las pruebas se encuentran en carpetas separadas.

Se conservaron Kotlin, XML, Material 3 y los colores del proyecto actualizado. Se retiraron los perfiles, la búsqueda, los filtros de plazo, los cambios de estado, las devoluciones, el reenvío, el cierre, el historial y la nueva observación. Esas funciones pertenecen al proyecto completo que ya tiene el grupo.

El objetivo del recorte es que los cuatro integrantes dominen el recorrido de los datos y puedan presentar un avance inicial concreto. Esta organización es una propuesta para su exposición y no representa un sprint aprobado por el profesor.

## Abrir y ejecutar

1. Extrae el ZIP completo.
2. En Android Studio elige **Open** y abre la carpeta `NexoSaludAvance1`.
3. Espera a que termine Gradle Sync. Se necesita Internet la primera vez.
4. Si faltan componentes, instala Android SDK Platform 35 y Build Tools 35.0.0 desde SDK Manager, revisando personalmente los términos que aparezcan.
5. Usa el JDK integrado compatible con Gradle 8.11.1, un emulador Android 8.0 o superior o un teléfono de pruebas. Ejecuta el módulo `app` con **Run**.

Se mantienen AGP 8.9.2, Gradle 8.11.1 y Kotlin 2.1.20. Abre este proyecto por separado para evitar mezclar los archivos con el proyecto completo.

La aplicación se identifica como `pe.edu.unmsm.nexosalud.avance1` y usa `nexo_avance1.db`. Puede coexistir con la versión completa sin sustituir su aplicación ni su base de datos. El nombre visible es Nexo Salud Avance 1.

## Demostración breve

1. Abre la app. La bandeja aparece vacía.
2. Pulsa Nueva tarea. Intenta guardar con los campos vacíos y explica la validación.
3. Escribe `REF-001`, una acción como `Completar el anexo faltante`, selecciona el responsable y conserva las fechas válidas o elige otras.
4. Guarda. Se abre el detalle del registro almacenado.
5. Vuelve a la bandeja y abre la tarjeta.
6. Cierra y vuelve a abrir la app. El registro sigue en SQLite.

Usa datos ficticios. No hay integración con REFCON ni sincronización entre teléfonos. Este avance no tiene autenticación.

## Preparar la exposición

Lee `docs/GUIA_EXPOSICION_Y_CODIGO.md`. Incluye un guion de aproximadamente 11 minutos, reparto entre cuatro personas, explicación de los archivos, seguimiento de un registro y preguntas de práctica.

El tiempo depende del ensayo. Como referencia, reserva de 10 a 12 minutos para exposición y demostración, más el tiempo que el profesor dedique a preguntas. Para una explicación detallada del código con preguntas, calcula de 15 a 20 minutos en total.

## Verificación

Lee `docs/VERIFICACION.md`. Se distinguen las comprobaciones de código realizadas de la ejecución Android que todavía requiere su equipo.
