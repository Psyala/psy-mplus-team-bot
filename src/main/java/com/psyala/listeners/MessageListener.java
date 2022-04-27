package com.psyala.listeners;

import com.psyala.PsyBot;
import com.psyala.commands.*;
import com.psyala.commands.base.Command;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.commands.base.SimpleCommand;
import com.psyala.util.DiscordInteractions;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final List<Command> commandList = new ArrayList<>();

    public MessageListener() {
        commandList.add(new HelpCommand(commandList));
        commandList.add(new InitialiseGuild());
        commandList.add(new PlayerRegister());
        commandList.add(new PlayerDelist());
        commandList.add(new CharacterRegister());
        commandList.add(new CharacterDelist());
        commandList.add(new KeystoneRegister());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //Event specific information
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContentDisplay();

        if (author.isBot()) return;

        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();
            } else {
                name = member.getEffectiveName();
            }

            //Handle command
            List<String> messages = Arrays.asList(msg.split("\n"));
            boolean batch = messages.size() > 1;
            messages.forEach(splitMessage -> {
                AtomicBoolean messageDeleted = new AtomicBoolean(false);
                commandList.forEach(command -> {
                    if (splitMessage.equals("!" + command.getCommand()) && command instanceof SimpleCommand) {
                        ((SimpleCommand) command).handle(guild, author, channel);
                        messageDeleted.set(true);
                        if (!batch) DiscordInteractions.deleteMessage(message);
                        LOGGER.info(String.format("(%s)[%s]<%s>: %s", guild.getName(), textChannel.getName(), name, msg));
                    }
                    if (splitMessage.startsWith("!" + command.getCommand()) && command instanceof ParameterCommand) {
                        List<String> parameters = List.of(splitMessage.replace("!" + command.getCommand(), "").split(" "));
                        ((ParameterCommand) command).handle(guild, author, channel, parameters.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
                        messageDeleted.set(true);
                        if (!batch) DiscordInteractions.deleteMessage(message);
                        LOGGER.info(String.format("(%s)[%s]<%s>: %s", guild.getName(), textChannel.getName(), name, msg));
                    }
                });
                if (batch) DiscordInteractions.deleteMessage(message);

                //Delete incoming message if in moderated channel
                if (Arrays.asList(PsyBot.configuration.channelOverviewName)
                        .contains(textChannel.getName()) && !messageDeleted.get()) {
                    DiscordInteractions.deleteMessage(message);
                }
            });
        }
    }
}
