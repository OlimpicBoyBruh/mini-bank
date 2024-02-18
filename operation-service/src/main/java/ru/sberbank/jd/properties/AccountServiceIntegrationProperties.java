package ru.sberbank.jd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

//@ConstructorBinding
@ConfigurationProperties(prefix = "integration.account-service")
@ConfigurationPropertiesScan
@Data
public class AccountServiceIntegrationProperties {
    private String url;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer writeTimeout;
}
