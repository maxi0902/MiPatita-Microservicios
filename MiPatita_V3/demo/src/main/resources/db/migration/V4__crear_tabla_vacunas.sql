CREATE TABLE vacunas (
    id_vacuna INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mascota INTEGER NOT NULL,
    nombre_vacuna VARCHAR(255) NOT NULL,
    fecha_aplicacion VARCHAR(255) NOT NULL,
    veterinario VARCHAR(255)
);
