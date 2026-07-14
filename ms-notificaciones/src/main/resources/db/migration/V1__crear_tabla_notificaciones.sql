CREATE TABLE notificaciones (
    id_notificacion INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER,
    tipo VARCHAR(50),
    mensaje VARCHAR(500)
);
