package com.mipatita.vacunas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.vacunas.client.MascotaClient;
import com.mipatita.vacunas.model.Vacuna;
import com.mipatita.vacunas.repository.VacunaRepository;

import feign.FeignException;
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
        log.info("Listando todas las vacunas");
        return vacunaRepository.findAll();
    }

    public Vacuna getVacuna(Integer idVacuna) {
        log.info("Buscando vacuna con id {}", idVacuna);
        return vacunaRepository.findById(idVacuna).orElse(null);
    }

    public List<Vacuna> getVacunasPorMascota(Integer idMascota) {
        log.info("Listando vacunas de la mascota {}", idMascota);
        return vacunaRepository.findByIdMascota(idMascota);
    }

    public Vacuna saveVacuna(Vacuna vacuna) {
        validarMascotaExiste(vacuna.getIdMascota());
        log.info("Registrando vacuna '{}' para la mascota {}", vacuna.getNombreVacuna(), vacuna.getIdMascota());
        return vacunaRepository.save(vacuna);
    }

    public Vacuna updateVacuna(Integer idVacuna, Vacuna vacuna) {
        if (!vacunaRepository.existsById(idVacuna)) {
            log.warn("No se pudo actualizar: la vacuna {} no existe", idVacuna);
            return null;
        }
        validarMascotaExiste(vacuna.getIdMascota());
        vacuna.setIdVacuna(idVacuna);
        log.info("Actualizando vacuna con id {}", idVacuna);
        return vacunaRepository.save(vacuna);
    }

    public boolean deleteVacuna(Integer idVacuna) {
        if (vacunaRepository.existsById(idVacuna)) {
            vacunaRepository.deleteById(idVacuna);
            log.info("Vacuna con id {} eliminada", idVacuna);
            return true;
        }
        log.warn("No se pudo eliminar: la vacuna {} no existe", idVacuna);
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
