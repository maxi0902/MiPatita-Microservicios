CREATE TABLE recordatorios (
    id_recordatorio INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    tipo_recordatorio VARCHAR(255) NOT NULL,
    frecuencia_dias INTEGER,
    fecha_proxima VARCHAR(255) NOT NULL,
    completado BOOLEAN
);
