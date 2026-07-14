package com.mipatita.controlpeso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mipatita.controlpeso.model.ControlPeso;

@Repository
public interface ControlPesoRepository extends JpaRepository<ControlPeso, Integer> {

    List<ControlPeso> findByIdMascota(Integer idMascota);
}
