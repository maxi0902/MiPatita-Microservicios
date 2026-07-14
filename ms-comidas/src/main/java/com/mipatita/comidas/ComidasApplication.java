package com.mipatita.comidas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ComidasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComidasApplication.class, args);
    }
}
