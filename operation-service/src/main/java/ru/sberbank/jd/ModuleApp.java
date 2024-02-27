package ru.sberbank.jd;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.sberbank.jd"})
@OpenAPIDefinition(info = @Info(
        title = "operation-service - Сервис операций по счетам",
        version = "1"))
public class ModuleApp {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApp.class, args);
    }

}
