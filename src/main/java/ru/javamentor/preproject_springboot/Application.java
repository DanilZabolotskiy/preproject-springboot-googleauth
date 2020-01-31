package ru.javamentor.preproject_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.javamentor.preproject_springboot")
public class Application  {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }


}
