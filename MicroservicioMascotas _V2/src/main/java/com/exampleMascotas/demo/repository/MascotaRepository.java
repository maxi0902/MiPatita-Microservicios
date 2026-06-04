package com.exampleMascotas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleMascotas.demo.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    
}
