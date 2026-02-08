package com.hscp.user.config;

import com.hscp.user.filter.GatewayAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/users/hello").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new GatewayAuthFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
