package com.psyala.commands;

import com.psyala.commands.base.Command;
import com.psyala.commands.base.SimpleCommand;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HelpCommand extends SimpleCommand {
    private final List<Command> commandList;

    public HelpCommand(List<Command> commandList) {
        super("help", "Get a list of commands");
        this.commandList = commandList;
    }

    @Override
    public void handle(Guild guild, MessageChannel channel) {
        String helpMessage = commandList.stream().map(command ->
                        "• " + command.getCommand() + ": " + command.getDescription())
                .collect(Collectors.joining("\r\n"));

        channel.sendMessageEmbeds(MessageFormatting.createTextualEmbedMessage("List of Commands", helpMessage))
                .delay(20, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }
}
