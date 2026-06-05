package com.exampleMiPatita.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.Vacuna;
import com.exampleMiPatita.demo.repository.VacunaRepository;
import com.exampleMiPatita.demo.webclient.MascotaClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VacunaService {

    private static final Logger log = LoggerFactory.getLogger(VacunaService.class);

    @Autowired
    private VacunaRepository vacunaRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Vacuna> getVacunas() {
        log.info("Listando todas las vacunas registradas");
        return vacunaRepository.findAll();
    }

    public Vacuna getVacuna(Integer idVacuna) {
        return vacunaRepository.findById(idVacuna).orElse(null);
    }

    public Vacuna saveVacuna(Vacuna vacuna) {
        log.info("Registrando vacuna para la mascota {}", vacuna.getIdMascota());
        Boolean existeMascota = mascotaClient.verificarMascota(vacuna.getIdMascota());
        if (existeMascota == null || !existeMascota) {
            throw new RuntimeException("Mascota no encontrada, no se puede registrar la vacuna");
        }
        return vacunaRepository.save(vacuna);
    }

    public Vacuna updateVacuna(Integer idVacuna, Vacuna vacuna) {
        if (!vacunaRepository.existsById(idVacuna)) {
            return null;
        }
        vacuna.setIdVacuna(idVacuna);
        return vacunaRepository.save(vacuna);
    }

    public boolean deleteVacuna(Integer idVacuna) {
        if (vacunaRepository.existsById(idVacuna)) {
            vacunaRepository.deleteById(idVacuna);
            log.info("Vacuna con id {} eliminada", idVacuna);
            return true;
        }
        return false;
    }
}
