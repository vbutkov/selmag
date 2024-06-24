package ru.selm.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest.requestMatchers(HttpMethod.POST, "/catalog-api/products/")
                                .hasAnyAuthority("SCOPE_edit_catalog")
                                .requestMatchers(HttpMethod.PATCH, "/catalog-api/products/{productId:\\d}")
                                .hasAuthority("SCOPE_edit_catalog")
                                .requestMatchers(HttpMethod.DELETE, "/catalog-api/products/{productId:\\d}")
                                .hasAuthority("SCOPE_edit_catalog")
                                .requestMatchers(HttpMethod.GET)
                                .hasAuthority("SCOPE_view_catalog")
                                .anyRequest().denyAll())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                        Customizer.withDefaults()
                ))
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
