package com.psyala.commands;

import com.psyala.PsyBot;
import com.psyala.commands.base.SimpleCommand;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.concurrent.TimeUnit;

public class InitialiseGuild extends SimpleCommand {
    public InitialiseGuild() {
        super("init", "Initialises the current server if it has not been initialised previously");
    }

    @Override
    public void handle(Guild guild, MessageChannel channel) {
        if (PsyBot.validGuilds.contains(guild)) {
            channel.sendMessageEmbeds(
                            MessageFormatting.createTextualEmbedMessage("Initialisation Result", "Server already initialised")
                    )
                    .delay(20, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
        } else {
            boolean initialised = PsyBot.initialiseGuild(guild);
            if (initialised) {
                channel.sendMessageEmbeds(
                                MessageFormatting.createTextualEmbedMessage("Initialisation Result", "Server now initialised")
                        )
                        .queue();
            }
        }
    }
}
