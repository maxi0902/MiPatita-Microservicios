package com.exampleMiPatita.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.Mascota;
import com.exampleMiPatita.demo.model.Recordatorio;
import com.exampleMiPatita.demo.repository.RecordatorioRepository;

@Service
public class RecordatorioService {

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Autowired
    private MascotaService mascotaService;

    public List<Recordatorio> getRecordatorios() {
        return recordatorioRepository.findAll();
    }

    public Recordatorio getRecordatorio(Integer idRecordatorio) {
        return recordatorioRepository.findById(idRecordatorio).orElse(null);
    }

    public Recordatorio saveRecordatorio(Recordatorio recordatorio) {
        Mascota mascota = mascotaService.getMascota(recordatorio.getIdMascota());
        if (mascota == null) {
            return null;
        }
        if (recordatorio.getFrecuenciaDias() != null && recordatorio.getFrecuenciaDias() < 0) {
            recordatorio.setFrecuenciaDias(0);
        }
        if (recordatorio.getCompletado() == null) {
            recordatorio.setCompletado(false);
        }
        return recordatorioRepository.save(recordatorio);
    }

    public Recordatorio updateRecordatorio(Integer idRecordatorio, Recordatorio recordatorio) {
        if (!recordatorioRepository.existsById(idRecordatorio)) {
            return null;
        }
        Mascota mascota = mascotaService.getMascota(recordatorio.getIdMascota());
        if (mascota == null) {
            return null;
        }
        recordatorio.setIdRecordatorio(idRecordatorio);
        if (recordatorio.getFrecuenciaDias() != null && recordatorio.getFrecuenciaDias() < 0) {
            recordatorio.setFrecuenciaDias(0);
        }
        if (recordatorio.getCompletado() == null) {
            recordatorio.setCompletado(false);
        }
        return recordatorioRepository.save(recordatorio);
    }

    public boolean deleteRecordatorio(Integer idRecordatorio) {
        if (recordatorioRepository.existsById(idRecordatorio)) {
            recordatorioRepository.deleteById(idRecordatorio);
            return true;
        }
        return false;
    }
}