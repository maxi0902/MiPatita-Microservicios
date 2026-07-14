package com.mipatita.recordatorios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mipatita.recordatorios.model.Recordatorio;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {

    List<Recordatorio> findByIdMascota(Integer idMascota);

    List<Recordatorio> findByCompletado(Boolean completado);
}
