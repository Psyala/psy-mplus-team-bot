package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.SimpleCommand;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.TimeUnit;

public class InitialiseGuild extends SimpleCommand {
    public InitialiseGuild() {
        super("init", "Initialises the current server if it has not been initialised previously");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel) {
        if (Beltip.guildController.getValidGuilds().contains(guild)) {
            channel.sendMessageEmbeds(
                    MessageFormatting.createTextualEmbedMessage("Initialisation Result", "Server already initialised")
            )
                    .delay(Beltip.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
        } else {
            boolean initialised = Beltip.guildController.initialiseGuild(guild);
            if (initialised) {
                channel.sendMessageEmbeds(
                        MessageFormatting.createTextualEmbedMessage("Initialisation Result", "Server now initialised")
                ).queue();
            }
        }
    }
}
