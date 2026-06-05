package com.exampleMiPatita.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleMiPatita.demo.model.Comida;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Integer> {
}
