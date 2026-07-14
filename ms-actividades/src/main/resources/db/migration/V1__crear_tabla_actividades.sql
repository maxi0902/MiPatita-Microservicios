CREATE TABLE actividades (
    id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    tipo_actividad VARCHAR(255) NOT NULL,
    duracion_minutos INTEGER NOT NULL,
    pasos INTEGER,
    fecha VARCHAR(255) NOT NULL,
    observacion VARCHAR(255)
);
