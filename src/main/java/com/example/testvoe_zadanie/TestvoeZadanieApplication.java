package com.example.testvoe_zadanie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.testvoe_zadanie.Contorllers", "com.example.testvoe_zadanie.models", "com.example.testvoe_zadanie.Repositories", "com.example.testvoe_zadanie.Services", "com.example.testvoe_zadanie.Specifications", "com.example.testvoe_zadanie"})

public class TestvoeZadanieApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestvoeZadanieApplication.class, args);
    }

}
