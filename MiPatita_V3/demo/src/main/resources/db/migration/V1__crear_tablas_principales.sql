CREATE TABLE actividades (
    id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    tipo_actividad VARCHAR(255) NOT NULL,
    duracion_minutos INTEGER NOT NULL,
    pasos INTEGER,
    fecha VARCHAR(255) NOT NULL,
    observacion VARCHAR(255)
);

CREATE TABLE recordatorios (
    id_recordatorio INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    tipo_recordatorio VARCHAR(255),
    frecuencia_dias INTEGER,
    fecha_proxima VARCHAR(255),
    completado BOOLEAN
);

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT
);