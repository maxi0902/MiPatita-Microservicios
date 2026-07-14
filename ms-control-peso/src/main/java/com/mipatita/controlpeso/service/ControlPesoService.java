package com.mipatita.controlpeso.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.controlpeso.client.MascotaClient;
import com.mipatita.controlpeso.model.ControlPeso;
import com.mipatita.controlpeso.repository.ControlPesoRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ControlPesoService {

    private static final Logger log = LoggerFactory.getLogger(ControlPesoService.class);

    @Autowired
    private ControlPesoRepository controlPesoRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<ControlPeso> getControles() {
        log.info("Listando todos los controles de peso");
        return controlPesoRepository.findAll();
    }

    public ControlPeso getControl(Integer idControl) {
        log.info("Buscando control de peso con id {}", idControl);
        return controlPesoRepository.findById(idControl).orElse(null);
    }

    public List<ControlPeso> getControlesPorMascota(Integer idMascota) {
        log.info("Listando controles de peso de la mascota {}", idMascota);
        return controlPesoRepository.findByIdMascota(idMascota);
    }

    public ControlPeso saveControl(ControlPeso control) {
        validarMascotaExiste(control.getIdMascota());
        log.info("Registrando control de peso ({} kg) para la mascota {}",
                control.getPesoKg(), control.getIdMascota());
        return controlPesoRepository.save(control);
    }

    public ControlPeso updateControl(Integer idControl, ControlPeso control) {
        if (!controlPesoRepository.existsById(idControl)) {
            log.warn("No se pudo actualizar: el control {} no existe", idControl);
            return null;
        }
        validarMascotaExiste(control.getIdMascota());
        control.setIdControl(idControl);
        log.info("Actualizando control de peso con id {}", idControl);
        return controlPesoRepository.save(control);
    }

    public boolean deleteControl(Integer idControl) {
        if (controlPesoRepository.existsById(idControl)) {
            controlPesoRepository.deleteById(idControl);
            log.info("Control de peso con id {} eliminado", idControl);
            return true;
        }
        log.warn("No se pudo eliminar: el control {} no existe", idControl);
        return false;
    }

    private void validarMascotaExiste(Integer idMascota) {
        try {
            Boolean existe = mascotaClient.verificarMascota(idMascota);
            if (existe == null || !existe) {
                throw new IllegalArgumentException("La mascota con id " + idMascota + " no existe");
            }
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("La mascota con id " + idMascota + " no existe");
        }
    }
}
