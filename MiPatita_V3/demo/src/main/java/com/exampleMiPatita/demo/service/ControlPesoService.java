package com.exampleMiPatita.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.ControlPeso;
import com.exampleMiPatita.demo.repository.ControlPesoRepository;
import com.exampleMiPatita.demo.webclient.MascotaClient;

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
        return controlPesoRepository.findById(idControl).orElse(null);
    }

    public ControlPeso saveControl(ControlPeso control) {
        log.info("Registrando peso para la mascota {}", control.getIdMascota());
        Boolean existeMascota = mascotaClient.verificarMascota(control.getIdMascota());
        if (existeMascota == null || !existeMascota) {
            throw new RuntimeException("Mascota no encontrada, no se puede registrar el peso");
        }
        return controlPesoRepository.save(control);
    }

    public ControlPeso updateControl(Integer idControl, ControlPeso control) {
        if (!controlPesoRepository.existsById(idControl)) {
            return null;
        }
        control.setIdControl(idControl);
        return controlPesoRepository.save(control);
    }

    public boolean deleteControl(Integer idControl) {
        if (controlPesoRepository.existsById(idControl)) {
            controlPesoRepository.deleteById(idControl);
            log.info("Control de peso con id {} eliminado", idControl);
            return true;
        }
        return false;
    }
}
