package com.pitchain.common.webhook;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.payload.discord.DiscordPayload;
import com.pitchain.common.webhook.strategy.DiscordEmbedBuilderStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DiscordWebhookSender {
    private final WebhookClient webhookClient;
    private final Map<Class<? extends DiscordPayload>, DiscordEmbedBuilderStrategy> strategyMap;

    public DiscordWebhookSender(WebhookClient webhookClient, List<DiscordEmbedBuilderStrategy> strategies) {
        this.webhookClient = webhookClient;
        this.strategyMap = strategies.stream().collect(Collectors.toMap(DiscordEmbedBuilderStrategy::getPayloadType, Function.identity()));
    }

    public void send(DiscordPayload payload) {
        DiscordEmbedBuilderStrategy strategy = strategyMap.get(payload.getClass());
        if (strategy == null) {
            throw new GeneralException(ErrorStatus.DISCORD_NO_WEBHOOK_STRATEGY);
        }

        WebhookEmbed embed = strategy.buildEmbed(payload);

        WebhookMessage webhookMessage = new WebhookMessageBuilder()
                .setUsername("Pitchain Webhook")
                .addEmbeds(embed)
                .build();

        webhookClient.send(webhookMessage);
    }
}
