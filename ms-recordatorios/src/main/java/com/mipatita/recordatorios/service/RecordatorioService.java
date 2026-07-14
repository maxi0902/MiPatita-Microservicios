package com.mipatita.recordatorios.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.recordatorios.client.MascotaClient;
import com.mipatita.recordatorios.model.Recordatorio;
import com.mipatita.recordatorios.repository.RecordatorioRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecordatorioService {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioService.class);

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Recordatorio> getRecordatorios() {
        log.info("Listando todos los recordatorios");
        return recordatorioRepository.findAll();
    }

    public Recordatorio getRecordatorio(Integer idRecordatorio) {
        log.info("Buscando recordatorio con id {}", idRecordatorio);
        return recordatorioRepository.findById(idRecordatorio).orElse(null);
    }

    public List<Recordatorio> getRecordatoriosPorMascota(Integer idMascota) {
        log.info("Listando recordatorios de la mascota {}", idMascota);
        return recordatorioRepository.findByIdMascota(idMascota);
    }

    public List<Recordatorio> getPendientes() {
        log.info("Listando recordatorios pendientes");
        return recordatorioRepository.findByCompletado(false);
    }

    public Recordatorio saveRecordatorio(Recordatorio recordatorio) {
        validarMascotaExiste(recordatorio.getIdMascota());
        if (recordatorio.getCompletado() == null) {
            recordatorio.setCompletado(false);
        }
        log.info("Creando recordatorio '{}' para la mascota {}",
                recordatorio.getTipoRecordatorio(), recordatorio.getIdMascota());
        return recordatorioRepository.save(recordatorio);
    }

    public Recordatorio updateRecordatorio(Integer idRecordatorio, Recordatorio recordatorio) {
        if (!recordatorioRepository.existsById(idRecordatorio)) {
            log.warn("No se pudo actualizar: el recordatorio {} no existe", idRecordatorio);
            return null;
        }
        validarMascotaExiste(recordatorio.getIdMascota());
        recordatorio.setIdRecordatorio(idRecordatorio);
        log.info("Actualizando recordatorio con id {}", idRecordatorio);
        return recordatorioRepository.save(recordatorio);
    }

    public boolean deleteRecordatorio(Integer idRecordatorio) {
        if (recordatorioRepository.existsById(idRecordatorio)) {
            recordatorioRepository.deleteById(idRecordatorio);
            log.info("Recordatorio con id {} eliminado", idRecordatorio);
            return true;
        }
        log.warn("No se pudo eliminar: el recordatorio {} no existe", idRecordatorio);
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
