package com.psyala.listeners;

import com.psyala.PsyMPlusBot;
import com.psyala.commands.Command;
import com.psyala.commands.HelpCommand;
import net.dv8tion.jda.api.JDA;
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

public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final List<Command> commandList = new ArrayList<>();

    public MessageListener() {
        commandList.add(new HelpCommand(commandList));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        JDA discordBot = event.getJDA();

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
            LOGGER.info(String.format("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg));

            //Handle command
            AtomicBoolean messageDeleted = new AtomicBoolean(false);
            commandList.forEach(command -> {
                if (msg.equals("!" + command.getCommand())) {
                    command.handle(channel);
                    message.delete().queue();
                    messageDeleted.set(true);
                }
            });

            //Delete incoming message if in moderated channel
            if (Arrays.asList(PsyMPlusBot.configuration.channelOverview, PsyMPlusBot.configuration.channelQuery)
                    .contains(textChannel.getName()) && !messageDeleted.get()) {
                message.delete().queue();
            }
        }

    }
}
