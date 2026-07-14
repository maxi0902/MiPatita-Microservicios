package com.mipatita.actividades.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.actividades.client.MascotaClient;
import com.mipatita.actividades.model.Actividad;
import com.mipatita.actividades.repository.ActividadRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ActividadService {

    private static final Logger log = LoggerFactory.getLogger(ActividadService.class);

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Actividad> getActividades() {
        log.info("Listando todas las actividades");
        return actividadRepository.findAll();
    }

    public Actividad getActividad(Integer idActividad) {
        log.info("Buscando actividad con id {}", idActividad);
        return actividadRepository.findById(idActividad).orElse(null);
    }

    public List<Actividad> getActividadesPorMascota(Integer idMascota) {
        log.info("Listando actividades de la mascota {}", idMascota);
        return actividadRepository.findByIdMascota(idMascota);
    }

    public Actividad saveActividad(Actividad actividad) {
        // Regla de negocio: la actividad solo se crea si la mascota existe (validado vía Feign)
        validarMascotaExiste(actividad.getIdMascota()); // regla de  negocio
        log.info("Guardando actividad de tipo {} para la mascota {}",
                actividad.getTipoActividad(), actividad.getIdMascota());
        return actividadRepository.save(actividad); //esto hace que se guarde en la base de datos
    }

    public Actividad updateActividad(Integer idActividad, Actividad actividad) {
        if (!actividadRepository.existsById(idActividad)) {
            log.warn("No se pudo actualizar: la actividad {} no existe", idActividad);
            return null;
        }
        validarMascotaExiste(actividad.getIdMascota());
        actividad.setIdActividad(idActividad);
        log.info("Actualizando actividad con id {}", idActividad);
        return actividadRepository.save(actividad);
    }

    public boolean deleteActividad(Integer idActividad) {
        if (actividadRepository.existsById(idActividad)) {
            actividadRepository.deleteById(idActividad);
            log.info("Actividad con id {} eliminada", idActividad);
            return true;
        }
        log.warn("No se pudo eliminar: la actividad {} no existe", idActividad);
        return false;
    }

    private void validarMascotaExiste(Integer idMascota) {
        try {
            Boolean existe = mascotaClient.verificarMascota(idMascota);
            if (existe == null || !existe) {
                throw new IllegalArgumentException("La mascota con id " + idMascota + " no existe");
            }
        } catch (FeignException.NotFound e) {
            // El microservicio de Mascotas responde 404 cuando la mascota no existe
            throw new IllegalArgumentException("La mascota con id " + idMascota + " no existe");
        }
    }
}
