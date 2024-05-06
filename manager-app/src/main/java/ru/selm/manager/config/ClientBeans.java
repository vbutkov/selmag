package ru.selm.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import ru.selm.manager.client.RestClentProductRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClentProductRestClient productRestClient(
            @Value("${selmag.services.catalog.uri:http://localhost:8081}") String catalogBaseUri,
            @Value("${selmag.services.catalog.username:}") String catalogUsername,
            @Value("${selmag.services.catalog.password:}") String catalogPassword) {
        return new RestClentProductRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(catalogUsername, catalogPassword))
                .build());
    }
}
