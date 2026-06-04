package com.exampleMascotas.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.repository.MascotaRepository;

/**
 * Capa de SERVICIO del microservicio Mascotas.
 * Contiene la lógica de negocio del dominio "mascota" y registra trazas
 * estructuradas (SLF4J) de cada operación importante.
 */
@Service
public class MascotaService {

    private static final Logger log = LoggerFactory.getLogger(MascotaService.class);

    @Autowired
    private MascotaRepository repository;

    public List<Mascota> findAll() {
        log.info("Listando todas las mascotas");
        return repository.findAll();
    }

    public boolean existsById(Integer id) {
        log.info("Verificando existencia de la mascota con id {}", id);
        return repository.existsById(id);
    }

    public Mascota findById(Integer id) {
        log.info("Buscando detalle de la mascota con id {}", id);
        return repository.findById(id).orElse(null);
    }

    public Mascota save(Mascota mascota) {
        log.info("Guardando mascota: {}", mascota.getNombre());
        return repository.save(mascota);
    }

    public boolean deleteById(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Mascota con id {} eliminada", id);
            return true;
        }
        log.warn("No se pudo eliminar: la mascota con id {} no existe", id);
        return false;
    }

    public Mascota update(Integer id, Mascota mascota) {
        if (!repository.existsById(id)) {
            log.warn("No se pudo actualizar: la mascota con id {} no existe", id);
            return null;
        }
        mascota.setIdMascota(id);
        log.info("Actualizando mascota con id {}", id);
        return repository.save(mascota);
    }
}
