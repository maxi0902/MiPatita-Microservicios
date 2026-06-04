package com.exampleMiPatita.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) de Mascota.
 *
 * IMPORTANTE: en MiPatita esto NO es una @Entity de JPA. La mascota se persiste
 * exclusivamente en el microservicio "Mascotas" (su propia base de datos
 * mascotas.db). Aquí solo usamos este objeto para transportar los datos que
 * viajan por HTTP a través del Feign Client (MascotaClient).
 *
 * Esto respeta el principio de "una base de datos por microservicio": MiPatita
 * nunca toca la tabla mascotas, solo consume su API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mascota {

    private Integer idMascota;
    private String nombre;
    private String raza;
    private Double peso;
    private Integer edad;
    private String tipoPelaje;
    private String nivelEnergia;
}
