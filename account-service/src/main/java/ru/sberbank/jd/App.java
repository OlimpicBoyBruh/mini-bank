package ru.sberbank.jd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Старт приложения.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ru.sberbank.jd"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
