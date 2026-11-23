CREATE TABLE tareas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo TEXT NOT NULL COLLATE NOCASE,
                accion TEXT NOT NULL COLLATE NOCASE,
                responsable TEXT NOT NULL,
                deteccion INTEGER NOT NULL,
                plazo INTEGER NOT NULL CHECK(plazo >= deteccion),
                estado TEXT NOT NULL DEFAULT 'Pendiente',
                UNIQUE(codigo, accion)
            );
