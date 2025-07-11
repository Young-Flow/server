package com.pitchain.common.webhook.strategy;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.pitchain.common.payload.discord.LongTimeRequestDiscordPayload;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;

@Component
public class LongTimeRequestDiscordEmbedBuilder implements DiscordEmbedBuilderStrategy<LongTimeRequestDiscordPayload> {
    @Override
    public WebhookEmbed buildEmbed(LongTimeRequestDiscordPayload payload) {
        return new WebhookEmbedBuilder()
                .setColor(Color.YELLOW.getRGB())
                .setTitle(new WebhookEmbed.EmbedTitle("⚠️ Long Time Request 감지", null))
                .setDescription("> 제한 시간이 초과된 요청이 감지되었습니다.")
                .addField(
                        new WebhookEmbed.EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDD56 발생 시간"), QUOTE_FORMAT.formatted(LocalDateTime.now()))
                )
                .addField(
                        new WebhookEmbed.EmbedField(false, BOLD_FORMAT.formatted("🔗 요청 URL"), QUOTE_FORMAT.formatted(payload.getUrl()))
                )
                .addField(
                        new WebhookEmbed.EmbedField(false, BOLD_FORMAT.formatted("⏱️ 처리 시간"), QUOTE_FORMAT.formatted(payload.getTurnaroundTimeSec() + "ms"))
                )
                .addField(
                        new WebhookEmbed.EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDCC3 RequestId"), QUOTE_FORMAT.formatted(payload.getRequestId()))
                )
                .build();
    }

    @Override
    public Class<LongTimeRequestDiscordPayload> getPayloadType() {
        return LongTimeRequestDiscordPayload.class;
    }
}

