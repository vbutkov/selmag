package ru.selm.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.selm.manager.client.RestClentProductRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClentProductRestClient productRestClient(
            @Value("${selmag.services.catalog.uri:http://localhost:8081}") String catalogBaseUri) {
        return new RestClentProductRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri)
                .build());
    }
}
