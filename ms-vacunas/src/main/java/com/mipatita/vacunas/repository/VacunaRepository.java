package com.mipatita.vacunas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mipatita.vacunas.model.Vacuna;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Integer> {

    List<Vacuna> findByIdMascota(Integer idMascota);
}
