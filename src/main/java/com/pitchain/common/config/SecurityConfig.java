package com.pitchain.common.config;

import com.pitchain.common.collector.RoleRequestCollector;
import com.pitchain.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RoleRequestCollector roleRequestCollector;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
            roleRequestCollector.getRoleUriMap().forEach((role, methodUriMap) -> {
                methodUriMap.forEach((httpMethod, uriSet) -> {
                    auth.requestMatchers(httpMethod, uriSet.toArray(new String[0])).hasAnyAuthority(role.getRoles());
                });
            });
            auth.requestMatchers(SWAGGER_PATTERNS).permitAll();
            auth.requestMatchers(STATIC_RESOURCES_PATTERNS).permitAll();
            auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
            auth.anyRequest().authenticated();
        });

        return http.build();
    }

    private static final String[] SWAGGER_PATTERNS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    private static final String[] STATIC_RESOURCES_PATTERNS = {
            "/img/**",
            "/css/**",
            "/js/**",
            "/favicon.ico",
    };

    private static final String[] PUBLIC_ENDPOINTS = {
            "/health-check", // health check
            "/oauth**",
            "/members/tokens", "/members/emails", // 공통 유저
            "/companies", "/companies/login", // 회사
            "/dev/**", // 개발용
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Authorization-Refresh"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
