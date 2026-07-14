package com.mipatita.comidas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mipatita.comidas.client.MascotaClient;
import com.mipatita.comidas.model.Comida;
import com.mipatita.comidas.repository.ComidaRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComidaService {

    private static final Logger log = LoggerFactory.getLogger(ComidaService.class);

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Comida> getComidas() {
        log.info("Listando todas las comidas registradas");
        return comidaRepository.findAll();
    }

    public Comida getComida(Integer idComida) {
        log.info("Buscando comida con id {}", idComida);
        return comidaRepository.findById(idComida).orElse(null);
    }

    public List<Comida> getComidasPorMascota(Integer idMascota) {
        log.info("Listando comidas de la mascota {}", idMascota);
        return comidaRepository.findByIdMascota(idMascota);
    }

    public Comida saveComida(Comida comida) {
        validarMascotaExiste(comida.getIdMascota());
        log.info("Registrando comida {} para la mascota {}", comida.getTipoComida(), comida.getIdMascota());
        return comidaRepository.save(comida);
    }

    public Comida updateComida(Integer idComida, Comida comida) {
        if (!comidaRepository.existsById(idComida)) {
            log.warn("No se pudo actualizar: la comida {} no existe", idComida);
            return null;
        }
        validarMascotaExiste(comida.getIdMascota());
        comida.setIdComida(idComida);
        log.info("Actualizando comida con id {}", idComida);
        return comidaRepository.save(comida);
    }

    public boolean deleteComida(Integer idComida) {
        if (comidaRepository.existsById(idComida)) {
            comidaRepository.deleteById(idComida);
            log.info("Comida con id {} eliminada", idComida);
            return true;
        }
        log.warn("No se pudo eliminar: la comida {} no existe", idComida);
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
