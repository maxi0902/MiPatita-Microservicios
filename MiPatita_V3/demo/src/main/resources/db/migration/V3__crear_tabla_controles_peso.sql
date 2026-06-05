CREATE TABLE controles_peso (
    id_control INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    peso_kg REAL NOT NULL,
    fecha VARCHAR(255) NOT NULL,
    observacion VARCHAR(255)
);
