package com.exampleMiPatita.demo.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.Mascota;
import com.exampleMiPatita.demo.webclient.MascotaClient;

@Service
public class MascotaService {
    
    private static final Logger log = LoggerFactory.getLogger(MascotaService.class);

    @Autowired
    private MascotaClient mascotaClient;

    public List<Mascota> getMascotas() {
        log.info("Iniciando consulta de listado de mascotas al microservicio externo");
        try {
            List<Mascota> lista = mascotaClient.listarMascotas();
            log.info("Consulta exitosa. Se recuperaron {} mascotas", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error crítico al conectar con microservicio de mascotas: {}", e.getMessage());
            return Arrays.asList();
        }
    }

    public Mascota getMascota(Integer idMascota) {
        log.info("Solicitando detalle de mascota con ID: {}", idMascota);
        try {
            Boolean existe = mascotaClient.verificarMascota(idMascota);
            if (existe != null && existe) {
                return mascotaClient.obtenerMascota(idMascota); 
            }
            log.warn("Intento de acceso a mascota inexistente ID: {}", idMascota);
            return null; 
        } catch (Exception e) {
            log.error("Error al obtener detalle de mascota ID {}: {}", idMascota, e.getMessage());
            return null;
        }
    }

    public Mascota saveMascota(Mascota mascota) {
        log.info("Guardando nueva mascota: {}", mascota.getNombre());
        try {
            Mascota guardada = mascotaClient.guardarMascota(mascota);
            log.info("Mascota guardada exitosamente con ID: {}", guardada.getIdMascota());
            return guardada;
        } catch (Exception e) {
            log.error("Error al guardar mascota {}: {}", mascota.getNombre(), e.getMessage());
            return null;
        }
    }

    public Mascota updateMascota(Integer idMascota, Mascota mascota) {
        log.info("Actualizando datos de mascota ID: {}", idMascota);
        try {
            return mascotaClient.actualizarMascota(idMascota, mascota);
        } catch (Exception e) {
            log.error("Error al actualizar mascota ID {}: {}", idMascota, e.getMessage());
            return null;
        }
    }
    
    public boolean deleteMascota(Integer idMascota) {
        log.info("Ejecutando eliminación de mascota ID: {}", idMascota);
        try {
            mascotaClient.eliminarMascota(idMascota);
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar mascota ID {}: {}", idMascota, e.getMessage());
            return false;
        }
    }
}