package com.pitchain.common.config;

import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(closeableHttpClient());
    }

    /**
     * Apache CloseableHttpClient
     * HTTP Connection 비동기 요청
     */
    @Bean
    public CloseableHttpClient closeableHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager())
                .build();

        return httpClient;
    }

    /**
     * Apache PoolingHttpClientConnectionManager
     * HTTP Connection Pooling 관리
     *
     * 필요 시 커넥션 설정 수정 가능
     * DEFAULT_MAX_TOTAL_CONNECTIONS = 25
     * DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5
     */
    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultTlsConfig(TlsConfig.custom()
                        .setHandshakeTimeout(Timeout.ofSeconds(30))
                        .build())
                .build();

    }

}
