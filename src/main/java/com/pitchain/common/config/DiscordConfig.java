package com.pitchain.common.config;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DiscordConfig {

    @Value("${discord.webhook.url}")
    private String url;

    @Bean
    public WebhookClient webhookClient() {
        WebhookClientBuilder builder = new WebhookClientBuilder(url);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Discord Webhook Client Thread");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        WebhookClient webhookClient = builder.build();

        setDefaultErrorHandler();
        return webhookClient;
    }

    private static void setDefaultErrorHandler() {
        WebhookClient.setDefaultErrorHandler((client, message, throwable) -> {
            log.error("Discord Webhook Error");
            log.error("clientId : {}, message : {}", client.getId(), message);
            if (throwable != null)
                throwable.printStackTrace();

            if (throwable instanceof HttpException ex && ex.getCode() == 404) {
                client.close();
            }
        });

    }
}
