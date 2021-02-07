package me.hoy.demospringdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DemospringdatajpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemospringdatajpaApplication.class, args);
    }

}
