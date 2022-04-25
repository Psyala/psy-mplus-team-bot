package com.psyala.commands.base;

public abstract class Command {
    private final String command;
    private final String description;
    protected boolean lastMessageCausedOverviewRefresh = false;

    public Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public boolean didLastMessageCausedOverviewRefresh() {
        return lastMessageCausedOverviewRefresh;
    }
}
