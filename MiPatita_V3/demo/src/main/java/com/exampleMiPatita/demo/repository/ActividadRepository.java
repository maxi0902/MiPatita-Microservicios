package com.exampleMiPatita.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleMiPatita.demo.model.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
}