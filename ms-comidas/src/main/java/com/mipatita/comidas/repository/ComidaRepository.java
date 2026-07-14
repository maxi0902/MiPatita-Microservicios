package com.mipatita.comidas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mipatita.comidas.model.Comida;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Integer> {

    List<Comida> findByIdMascota(Integer idMascota);
}
