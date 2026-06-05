package com.exampleMiPatita.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.model.Comida;
import com.exampleMiPatita.demo.repository.ComidaRepository;
import com.exampleMiPatita.demo.webclient.MascotaClient;

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
        return comidaRepository.findById(idComida).orElse(null);
    }

    public Comida saveComida(Comida comida) {
        log.info("Registrando comida para la mascota {}", comida.getIdMascota());
        Boolean existeMascota = mascotaClient.verificarMascota(comida.getIdMascota());
        if (existeMascota == null || !existeMascota) {
            throw new RuntimeException("Mascota no encontrada, no se puede registrar la comida");
        }
        return comidaRepository.save(comida);
    }

    public Comida updateComida(Integer idComida, Comida comida) {
        if (!comidaRepository.existsById(idComida)) {
            return null;
        }
        comida.setIdComida(idComida);
        return comidaRepository.save(comida);
    }

    public boolean deleteComida(Integer idComida) {
        if (comidaRepository.existsById(idComida)) {
            comidaRepository.deleteById(idComida);
            log.info("Comida con id {} eliminada", idComida);
            return true;
        }
        return false;
    }
}
