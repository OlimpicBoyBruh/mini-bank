package ru.sberbank.jd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.sberbank.jd"})
public class ModuleApp {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApp.class, args);
    }

}
