package com.exampleMascotas.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.repository.MascotaRepository;

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

    public List<Mascota> findByRaza(String raza) {
        log.info("Buscando mascotas de la raza {}", raza);
        return repository.findByRaza(raza);
    }

    public List<Mascota> findByEdad(Integer edad) {
        log.info("Buscando mascotas con edad {}", edad);
        return repository.findByEdad(edad);
    }

    public List<Mascota> findByNivelEnergia(String nivelEnergia) {
        log.info("Buscando mascotas con nivel de energía {}", nivelEnergia);
        return repository.findByNivelEnergia(nivelEnergia);
    }

    public List<Mascota> findByEdadBetween(Integer edadMin, Integer edadMax) {
        log.info("Buscando mascotas con edad entre {} y {}", edadMin, edadMax);
        return repository.findByEdadBetween(edadMin, edadMax);
    }

    public long countByRaza(String raza) {
        log.info("Contando mascotas de la raza {}", raza);
        return repository.countByRaza(raza);
    }
}
