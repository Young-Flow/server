package com.pitchain.common.webhook.strategy;

import club.minnced.discord.webhook.send.WebhookEmbed;
import com.pitchain.common.payload.Payload;

public interface DiscordEmbedBuilderStrategy<T extends Payload> {
    String QUOTE_FORMAT = "> %s";
    String CODE_FORMAT = "```\n%s```\n";
    String BOLD_FORMAT = "** %s **";

    WebhookEmbed buildEmbed(T payload);

    Class<T> getPayloadType();
}

