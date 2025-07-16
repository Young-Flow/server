package com.pitchain.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitchain.common.apiPayload.CustomResponse;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.collector.RoleRequestCollector;
import com.pitchain.common.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RoleRequestCollector roleRequestCollector;

    @Value("#{'${spring.cors.allowed-origins}'.replaceAll(' ', '').split(',')}")
    private List<String> allowedOrigins;

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
            auth.anyRequest().permitAll();
        });
        http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(customAuthenticationEntryPoint());
            exception.accessDeniedHandler(customAccessDeniedHandler());
        });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:8080", "https://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Authorization-Refresh"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            final String message = "유효한 인증 정보가 없거나, 존재하지 않는 API를 요청하셨습니다.";
            writeErrorResponse(response, message);
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            final String message = "요청하신 API에 대한 접근 권한이 없습니다.";
            writeErrorResponse(response, message);
        };
    }

    private static void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorStatus errorStatus = ErrorStatus._FORBIDDEN;
        CustomResponse customResponse = CustomResponse.onFailure(errorStatus.getCode(), message);

        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(customResponse));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
