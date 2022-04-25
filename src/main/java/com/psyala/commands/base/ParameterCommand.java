package com.psyala.commands.base;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public abstract class ParameterCommand extends Command {
    public ParameterCommand(String command, String description) {
        super(command, description);
    }

    public abstract void handle(Guild guild, User author, MessageChannel channel, List<String> parameters);
}
