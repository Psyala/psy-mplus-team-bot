package com.psyala.commands.base;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class SimpleCommand extends Command {
    public SimpleCommand(String command, String description) {
        super(command, description);
    }

    public abstract void handle(Guild guild, MessageChannel channel);
}
