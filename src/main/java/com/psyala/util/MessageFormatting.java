package com.psyala.util;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Arrays;
import java.util.List;

public class MessageFormatting {

    public static MessageEmbed createTextualEmbedMessage(String title, String messageContents) {
        return new MessageEmbed(
                null, title, messageContents, EmbedType.RICH,
                null, 123456, null, null, null,
                null, null, null, null
        );
    }

    public static MessageEmbed createFieldEmbedMessage(String title, List<MessageEmbed.Field> fieldList) {
        return new MessageEmbed(
                null, title, null, EmbedType.RICH,
                null, 123456, null, null, null,
                null, null, null, fieldList
        );
    }

    public static String biggify(String string) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(string.toLowerCase().split("|")).forEach(c -> {
            if (c.equals(" ")) {
                sb.append(" ");
            } else {
                sb.append(":regional_indicator_").append(c).append(": ");
            }
        });
        return sb.toString();
    }
}
