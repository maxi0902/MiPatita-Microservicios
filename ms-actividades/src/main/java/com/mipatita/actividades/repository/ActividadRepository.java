package com.mipatita.actividades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mipatita.actividades.model.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {

    List<Actividad> findByIdMascota(Integer idMascota);
}
