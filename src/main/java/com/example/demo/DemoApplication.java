package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = "com.example.demo")
@EnableJpaRepositories
@EntityScan(basePackages = "com.example.demo.models")
public class DemoApplication {


    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "password123";
        String hashed = "$2a$10$Dow1j0JGjY7r9IPoEfU0xu3cgph4Z1RMejLRVdlD0Zprp/B5OaIgy";

        System.out.println(encoder.matches(raw, hashed)); // should print: true

        String hashedf = new BCryptPasswordEncoder().encode("password123");
        System.out.println(hashedf);
        SpringApplication.run(DemoApplication.class, args);
    }

}
