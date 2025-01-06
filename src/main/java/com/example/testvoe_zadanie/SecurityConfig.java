package com.example.testvoe_zadanie;

import com.example.testvoe_zadanie.Services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Кодировщик паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Конфигурация безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyAuthority("SIMPLE_USER", "ADMINISTRATOR") // Разрешение на GET для обычных пользователей и администраторов
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasAuthority("ADMINISTRATOR") // Только администраторы могут выполнять POST
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAuthority("ADMINISTRATOR") // Только администраторы могут выполнять PUT
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasAuthority("ADMINISTRATOR") // Только администраторы могут выполнять DELETE
                         .anyRequest().authenticated()
                      //  .anyRequest().denyAll() // Запретить все остальные запросы
                )
                .formLogin(form -> form
                        .permitAll() // Доступ к странице входа для всех
                        .defaultSuccessUrl("/api/employees", true) // Перенаправление после успешного входа
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода из аккаунта
                        .logoutSuccessUrl("/login?logout") // Перенаправление после выхода
                )
                .httpBasic(Customizer.withDefaults()) // Включение базовой аутентификации
                .userDetailsService(userDetailsService); // Используем свой UserDetailsService

        return http.build();
    }
}