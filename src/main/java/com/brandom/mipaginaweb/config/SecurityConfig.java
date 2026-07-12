package com.brandom.mipaginaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/tienda/**", "/producto/**", "/imagenes/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/login", "/oauth2/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/carrito/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/carrito", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}