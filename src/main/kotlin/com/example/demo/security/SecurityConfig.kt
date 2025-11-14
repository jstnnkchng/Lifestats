package com.example.demo.security

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableAutoConfiguration
@EnableMethodSecurity
@EnableWebSecurity
open class SecurityConfig {
    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/v1/health",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                    ).permitAll()
                    .anyRequest().authenticated()
            }

        return http.build()
    }
}
