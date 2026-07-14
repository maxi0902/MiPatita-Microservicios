package com.mipatita.notificaciones.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.notificaciones.client.RecordatorioClient;
import com.mipatita.notificaciones.client.VacunaClient;
import com.mipatita.notificaciones.dto.RecordatorioDTO;
import com.mipatita.notificaciones.dto.VacunaDTO;
import com.mipatita.notificaciones.model.Notificacion;
import com.mipatita.notificaciones.repository.NotificacionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private RecordatorioClient recordatorioClient;

    @Autowired
    private VacunaClient vacunaClient;

    public List<Notificacion> getNotificaciones() {
        log.info("Listando todas las notificaciones generadas");
        return notificacionRepository.findAll();
    }

    public List<Notificacion> getNotificacionesPorMascota(Integer idMascota) {
        log.info("Listando notificaciones almacenadas de la mascota {}", idMascota);
        return notificacionRepository.findByIdMascota(idMascota);
    }

    /**
     * Genera notificaciones para una mascota combinando datos remotos:
     * - recordatorios pendientes (ms-recordatorios via Feign)
     * - vacunas registradas (ms-vacunas via Feign)
     * Cada notificacion generada se persiste y se devuelve.
     */
    public List<Notificacion> generarParaMascota(Integer idMascota) {
        log.info("Generando notificaciones para la mascota {} (consultando servicios remotos)", idMascota);
        List<Notificacion> generadas = new ArrayList<>();

        try {
            List<RecordatorioDTO> recordatorios = recordatorioClient.obtenerPorMascota(idMascota);
            for (RecordatorioDTO r : recordatorios) {
                if (r.getCompletado() == null || !r.getCompletado()) {
                    generadas.add(new Notificacion(null, idMascota, "RECORDATORIO",
                            "Recordatorio pendiente: " + r.getTipoRecordatorio() + " (" + r.getFechaProxima() + ")"));
                }
            }
        } catch (Exception e) {
            log.warn("No se pudieron obtener recordatorios de la mascota {}: {}", idMascota, e.getMessage());
        }

        try {
            List<VacunaDTO> vacunas = vacunaClient.obtenerPorMascota(idMascota);
            for (VacunaDTO v : vacunas) {
                generadas.add(new Notificacion(null, idMascota, "VACUNA",
                        "Vacuna registrada: " + v.getNombreVacuna() + " (" + v.getFechaAplicacion() + ")"));
            }
        } catch (Exception e) {
            log.warn("No se pudieron obtener vacunas de la mascota {}: {}", idMascota, e.getMessage());
        }

        return notificacionRepository.saveAll(generadas);
    }

    public boolean deleteNotificacion(Integer idNotificacion) {
        if (notificacionRepository.existsById(idNotificacion)) {
            notificacionRepository.deleteById(idNotificacion);
            log.info("Notificacion con id {} eliminada", idNotificacion);
            return true;
        }
        return false;
    }
}
