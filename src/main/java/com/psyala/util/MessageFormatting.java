package com.psyala.util;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageFormatting {
    public static MessageEmbed createTextualEmbedMessage(String title, String messageContents) {
        return new MessageEmbed(
                null, title, messageContents, EmbedType.RICH,
                null, 123456, null, null, null,
                null, null, null, null
        );
    }
}
