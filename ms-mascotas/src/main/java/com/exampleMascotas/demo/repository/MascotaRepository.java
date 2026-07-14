package com.exampleMascotas.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleMascotas.demo.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    List<Mascota> findByRaza(String raza);

    List<Mascota> findByEdad(Integer edad);

    List<Mascota> findByNivelEnergia(String nivelEnergia);

    List<Mascota> findByEdadBetween(Integer edadMin, Integer edadMax);

    long countByRaza(String raza);
}
