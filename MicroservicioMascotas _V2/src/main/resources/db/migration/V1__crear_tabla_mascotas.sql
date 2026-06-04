-- V1__crear_tabla_mascotas.sql
CREATE TABLE mascotas (
    id_mascota INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    raza VARCHAR(255),
    peso REAL,
    edad INTEGER,
    tipo_pelaje VARCHAR(255),
    nivel_energia VARCHAR(255)
);