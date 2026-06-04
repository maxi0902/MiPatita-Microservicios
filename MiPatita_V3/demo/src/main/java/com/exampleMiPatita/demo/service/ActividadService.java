package com.exampleMiPatita.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.Actividad;
import com.exampleMiPatita.demo.repository.ActividadRepository;
import com.exampleMiPatita.demo.webclient.MascotaClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Actividad> getActividades() {
        return actividadRepository.findAll();
    }

    public Actividad getActividad(Integer idActividad) {
        return actividadRepository.findById(idActividad).orElse(null);
    }

    public Actividad saveActividad(Actividad actividad) {
        
        //FEIGN: Usamos nuestro nuevo cliente directo y limpio
        Boolean existeMascota = mascotaClient.verificarMascota(actividad.getIdMascota());
        
        if (existeMascota == null || !existeMascota) {
            throw new RuntimeException("Mascota no encontrada, no se puede agregar actividad");
        }
        return actividadRepository.save(actividad);
    }

    public Actividad updateActividad(Integer idActividad, Actividad actividad) {
        if (!actividadRepository.existsById(idActividad)) {
            return null;
        }

        Boolean existeMascota = mascotaClient.verificarMascota(actividad.getIdMascota());
        
        if (existeMascota == null || !existeMascota) {
            return null;
        }

        actividad.setIdActividad(idActividad);
        return actividadRepository.save(actividad);
    }

    public boolean deleteActividad(Integer idActividad) {
        if (actividadRepository.existsById(idActividad)) {
            actividadRepository.deleteById(idActividad);
            return true;
        }
        return false;
    }
}