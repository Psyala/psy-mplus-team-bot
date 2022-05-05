package com.psyala.util;

import com.psyala.Beltip;
import com.psyala.pojo.Character;
import com.psyala.pojo.Keystone;
import com.psyala.pojo.characterists.Role;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.codehaus.plexus.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFormatting {
    public static int EMBED_COLOUR = 123456;

    public static MessageEmbed createTextualEmbedMessage(String title, String messageContents) {
        return new MessageEmbed(
                null, title, messageContents, EmbedType.RICH,
                null, EMBED_COLOUR, null, null, null,
                null, null, null, null
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

    public static String formatCharacter(Character character) {
        return character.characterClass.getClassIcon() + " `" + StringUtils.rightPad(character.name, 25, " ") + "`";
    }

    public static String formatRoles(List<Role> roleList) {
        return roleList.stream()
                .sorted(Comparator.comparingInt(Role::getI))
                .map(Role::getRoleIcon)
                .collect(Collectors.joining(" "));
    }

    public static String formatKeystone(Keystone keystone) {
        String keystoneText = keystone == null ?
                "`" + StringUtils.rightPad("None", 10, " ") + "`"
                : "`" + StringUtils.rightPad(keystone.dungeon.getAcronym(), 8, " ") + StringUtils.leftPad(String.valueOf(keystone.level), 2, " ") + "`";

        return Beltip.configuration.iconKeystone + keystoneText;
    }
}
