package ru.selm.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.selm.manager.client.RestClentProductRestClient;
import ru.selm.manager.security.OAuthClientHttpRequestInterceptor;

@Configuration
public class ClientBeans {
    @Bean
    public RestClentProductRestClient productRestClient(
            @Value("${selmag.services.catalog.uri:${selmag.services.catalog.uri:http://localhost:8081}}") String catalogBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${selmag.services.catalog.registration-id:keycloak}") String registrationId) {

        return new RestClentProductRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri)
                .requestInterceptor(
                        new OAuthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(
                                        clientRegistrationRepository, authorizedClientRepository),
                                registrationId
                        )
                )
                .build()
        );
    }
}
