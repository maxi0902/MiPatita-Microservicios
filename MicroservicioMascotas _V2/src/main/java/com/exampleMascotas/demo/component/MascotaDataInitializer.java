package com.exampleMascotas.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.repository.MascotaRepository;

@Component
public class MascotaDataInitializer implements CommandLineRunner {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (mascotaRepository.count() == 0) {
            
            mascotaRepository.save(new Mascota(null, "Oreo", "Pomerania", 4.5, 3, "Alto", "Alto"));
            mascotaRepository.save(new Mascota(null, "Luna", "Sin raza", 6.2, 5, "Rizado", "Medio"));
            mascotaRepository.save(new Mascota(null, "Coco", "Siamés", 4.5, 3, "Corto", "Bajo"));
            mascotaRepository.save(new Mascota(null, "Mia", "Persa", 5.0, 1, "Largo", "Medio"));
            mascotaRepository.save(new Mascota(null, "Spike", "Golden Retriever", 30.0, 1, "Alto", "Alto"));

            System.out.println("Migracion del Microservicio: 5 mascotas creadas con exito");
        }
    }
}