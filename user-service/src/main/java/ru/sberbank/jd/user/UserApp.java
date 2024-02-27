package ru.sberbank.jd.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ru.sberbank.jd"})
@OpenAPIDefinition(info = @Info(
        title = "User service",
        description = "Service for user CRUD operations and acquiring JWT for operations on other services.",
        version = "1.0",
        contact = @Contact(name = "Danila Tatara")))
public class UserApp {

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}
