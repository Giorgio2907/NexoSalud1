# Verificación del avance reducido

## Comprobaciones ejecutadas en el entorno de creación

- Compilación de los ocho archivos Kotlin de producción contra clases Android y AndroidX, usando Kotlin 2.1.20.
- Ocho pruebas JUnit sobre las validaciones del formulario.
- Compilación de los recursos XML con AAPT2 oficial 8.9.2-12782657.
- Comprobación del esquema en SQLite del entorno de desarrollo, incluyendo persistencia al reabrir, rechazo de duplicados, restricción de fechas y orden de consulta.
- Contraste entre los IDs que usa Kotlin y los que declaran los XML.

La comprobación Kotlin emplea un R temporal generado desde los recursos y clases Android disponibles en Maven para validar tipos y sintaxis. No sustituye la compilación completa con el SDK y el plugin Android. Los archivos temporales no están incluidos en la entrega.

## Pruebas preparadas para Android

En `app/src/androidTest` hay tres pruebas preparadas para ejecutar en un dispositivo. Comprueban persistencia y normalización del código, rechazo de duplicados y consulta de un ID inexistente. Usan una base de pruebas aislada.

Estas pruebas instrumentadas no se ejecutaron aquí. Tampoco se generó un APK ni se verificó visualmente esta versión en un emulador. Para exponerla, el grupo debe completar esas comprobaciones en Android Studio.

Desde la terminal de Android Studio en Windows pueden usar

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

Con el emulador o teléfono de pruebas conectado

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

## Ensayo en el dispositivo

1. Intentar guardar con código y acción vacíos.
2. Registrar una tarea válida y revisar su detalle.
3. Volver a Bandeja y comprobar que aparece una sola tarjeta.
4. Intentar repetir código y acción y comprobar el aviso.
5. Cerrar y abrir la aplicación y consultar el registro almacenado.
6. Rotar el dispositivo con un formulario sin guardar y revisar los datos y las fechas.
7. Abrir el teclado y comprobar que el formulario se puede desplazar hasta Guardar.

El proyecto completo del compañero se conserva separado. Esta entrega se instala como otra aplicación y usa su propia base.
