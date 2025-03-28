package com.pickmeapp.pickmeappbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.pickme.pickmeappentityservice.models")
public class PickmeAppBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickmeAppBookingServiceApplication.class, args);
    }

}
