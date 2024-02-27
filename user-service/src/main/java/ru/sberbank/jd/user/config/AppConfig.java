package ru.sberbank.jd.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.sberbank.jd.user.service.rest.client.AccountClient;

/**
 * Configuration.
 */
@RequiredArgsConstructor
@Configuration
@PropertySource("classpath:application.yml")
public class AppConfig {

    @Value("${app.account.url}")
    private String accountUrl;

    @Bean
    public RestClient.Builder configClient() {
        return RestClient.builder()
                .baseUrl(accountUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("Error during account processing: "
                            + request.getURI() + ", ends with: "
                            + response.getStatusCode() + " - "
                            + response.getStatusText());
                });
    }

    @Bean
    public AccountClient accountClient(RestClient.Builder client) {
        RestClient restClient = client.build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AccountClient.class);
    }
}
