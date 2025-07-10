package com.pitchain.common.webhook.strategy;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.pitchain.common.payload.discord.ErrorDiscordPayload;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;

import static club.minnced.discord.webhook.send.WebhookEmbed.*;

@Component
public class ErrorDiscordEmbedBuilder implements DiscordEmbedBuilderStrategy<ErrorDiscordPayload> {

    private static final int MAX_LENGTH = 500;

    @Override
    public WebhookEmbed buildEmbed(ErrorDiscordPayload payload) {
        String stackTrace = getTruncatedStackTrace(payload);

        return new WebhookEmbedBuilder()
                .setColor(Color.RED.getRGB())
                .setTitle(new EmbedTitle("\uD83D\uDEA8 에러 발생", null))
                .setDescription("> 예기치 못한 에러가 발생했습니다")
                .addField(
                        new EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDD56 발생 시간"), QUOTE_FORMAT.formatted(LocalDateTime.now()))
                )
                .addField(
                        new EmbedField(false, BOLD_FORMAT.formatted("🔗 요청 URL"), QUOTE_FORMAT.formatted(payload.getUrl()))
                )
                .addField(
                        new EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDCE6 파라미터 메시지"), QUOTE_FORMAT.formatted(payload.getParameterMessage()))
                )
                .addField(
                        new EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDCC4 Stack Trace"), CODE_FORMAT.formatted(stackTrace))
                )
                .addField(
                        new EmbedField(false, BOLD_FORMAT.formatted("\uD83D\uDCC3 RequestId"), QUOTE_FORMAT.formatted(payload.getRequestId()))
                )
                .build();
    }

    private static String getTruncatedStackTrace(ErrorDiscordPayload payload) {
        String stackTrace = payload.getStackTrace();
        if (stackTrace.length() > MAX_LENGTH)
            stackTrace = stackTrace.substring(0, MAX_LENGTH) + "... ";
        return stackTrace;
    }

    @Override
    public Class<ErrorDiscordPayload> getPayloadType() {
        return ErrorDiscordPayload.class;
    }
}

