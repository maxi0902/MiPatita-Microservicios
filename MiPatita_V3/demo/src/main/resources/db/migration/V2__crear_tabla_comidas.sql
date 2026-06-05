CREATE TABLE comidas (
    id_comida INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    tipo_comida VARCHAR(255) NOT NULL,
    cantidad_gramos INTEGER NOT NULL,
    fecha VARCHAR(255) NOT NULL,
    observacion VARCHAR(255)
);
