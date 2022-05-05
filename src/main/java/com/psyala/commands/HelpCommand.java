package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.Command;
import com.psyala.commands.base.SimpleCommand;
import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Dungeon;
import com.psyala.pojo.characterists.ArmourClass;
import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.pojo.characterists.TrinketClass;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HelpCommand extends SimpleCommand {
    private final List<Command> commandList;

    public HelpCommand(List<Command> commandList) {
        this("help", "Get a list of commands", commandList);
    }

    public HelpCommand(String command, String description, List<Command> commandList) {
        super(command, description);
        this.commandList = commandList;
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel) {
        String information = "Commands are prefixed with the character: `" + MessageListener.COMMAND_CHAR + "`";

        String commands = "**Commands**" +
                "\r\n```" +
                commandList.stream().map(command ->
                                "• " + command.getCommand() + ": " + command.getDescription())
                        .collect(Collectors.joining("\r\n\r\n")) +
                "```";

        String lookups = "**Lookups**" +
                "\r\n```" +
                "**Dungeons**\r\n• " +
                Arrays.stream(Dungeon.values()).map(Dungeon::getAcronym).collect(Collectors.joining("\r\n• ")) +
                "\r\n```\r\n```" +
                "**Classes**\r\n• " +
                Arrays.stream(CharacterClass.values()).map(Enum::name).collect(Collectors.joining("\r\n• ")) +
                "\r\n```\r\n```" +
                "**Armour Class**\r\n• " +
                Arrays.stream(ArmourClass.values()).map(Enum::name).collect(Collectors.joining("\r\n• ")) +
                "\r\n```\r\n```" +
                "**Trinket Class**\r\n• " +
                Arrays.stream(TrinketClass.values()).map(Enum::name).collect(Collectors.joining("\r\n• ")) +
                "\r\n```";

        String helpMessage = information.concat("\r\n\r\n")
                .concat(commands).concat("\r\n\r\n")
                .concat(lookups);
        author.openPrivateChannel()
                .flatMap(privateChannel -> privateChannel.sendMessageEmbeds(MessageFormatting.createTextualEmbedMessage(":robot: Bot Help :robot:", helpMessage)))
                .queue(success -> {
                }, failure -> {
                    channel.sendMessageEmbeds(
                                    MessageFormatting.createTextualEmbedMessage(
                                            ":robot: Bot Help :robot:",
                                            "Could not whisper you ".concat(author.getAsMention()) + " check your privacy settings!"
                                    )
                            )
                            .delay(Beltip.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue();
                });
    }
}
