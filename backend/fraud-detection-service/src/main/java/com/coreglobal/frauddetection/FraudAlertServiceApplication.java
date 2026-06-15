package com.coreglobal.frauddetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.coreglobal")
public class FraudAlertServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FraudAlertServiceApplication.class, args);
    }
}
