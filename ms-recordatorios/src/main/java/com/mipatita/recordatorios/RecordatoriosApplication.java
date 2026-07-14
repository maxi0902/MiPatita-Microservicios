package com.mipatita.recordatorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RecordatoriosApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecordatoriosApplication.class, args);
    }
}
