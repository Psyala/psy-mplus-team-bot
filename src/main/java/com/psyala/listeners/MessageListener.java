package com.psyala.listeners;

import com.psyala.Beltip;
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
    public static final String COMMAND_CHAR = "!";
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final List<Command> commandList = new ArrayList<>();

    public MessageListener() {
        commandList.add(new HelpCommand(commandList));
        commandList.add(new CommandsCommand(commandList));
        commandList.add(new InitialiseGuild());
        commandList.add(new PlayerRegister());
        commandList.add(new PlayerDelist());
        commandList.add(new CharacterRegister());
        commandList.add(new CharacterDelist());
        commandList.add(new KeystoneRegister());
        commandList.add(new KeyQuery());
        commandList.add(new RoleUpdate());
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
            AtomicBoolean commandActioned = new AtomicBoolean(false);
            messages.forEach(splitMessage -> {
                commandList.forEach(command -> {
                    if (splitMessage.equals(COMMAND_CHAR + command.getCommand()) && command instanceof SimpleCommand) {
                        ((SimpleCommand) command).handle(guild, author, channel);
                        commandActioned.set(true);
                        LOGGER.info(String.format("(%s)[%s]<%s>: %s", guild.getName(), textChannel.getName(), name, splitMessage));
                    }
                    if (splitMessage.startsWith(COMMAND_CHAR + command.getCommand()) && command instanceof ParameterCommand) {
                        List<String> parameters = Arrays.asList(splitMessage.replace("!" + command.getCommand(), "").split(" "));
                        ((ParameterCommand) command).handle(guild, author, channel, parameters.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
                        commandActioned.set(true);
                        LOGGER.info(String.format("(%s)[%s]<%s>: %s", guild.getName(), textChannel.getName(), name, splitMessage));
                    }
                });

                //Delete incoming message if in moderated channel
                if (Arrays.asList(Beltip.configuration.channelOverviewName)
                        .contains(textChannel.getName())) {
                    DiscordInteractions.deleteMessage(message);
                }
            });
        }
    }
}
