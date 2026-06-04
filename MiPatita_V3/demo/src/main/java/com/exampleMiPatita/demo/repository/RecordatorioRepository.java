package com.exampleMiPatita.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleMiPatita.demo.model.Recordatorio;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {
}