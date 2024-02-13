package ru.sberbank.jd.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AccountServiceIntegration {
    private final RestTemplate restTemplate;

}
