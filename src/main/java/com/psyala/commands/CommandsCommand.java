package com.psyala.commands;

import com.psyala.commands.base.Command;

import java.util.List;

public class CommandsCommand extends HelpCommand {

    public CommandsCommand(List<Command> commandList) {
        super("commands", "Get a list of commands", commandList);
    }

}
