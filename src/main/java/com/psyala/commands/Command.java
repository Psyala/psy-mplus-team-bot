package com.psyala.commands;

import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class Command {
    private final String command;
    private final String description;

    public Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public abstract void handle(MessageChannel channel);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
