package com.mipatita.vacunas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VacunasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacunasApplication.class, args);
    }
}
