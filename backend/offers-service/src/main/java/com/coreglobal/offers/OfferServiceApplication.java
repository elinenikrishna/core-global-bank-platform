package com.coreglobal.offers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.coreglobal")
public class OfferServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfferServiceApplication.class, args);
    }
}
