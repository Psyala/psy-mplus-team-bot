package com.psyala.commands;

import com.psyala.PsyBot;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.pojo.Player;
import com.psyala.pojo.Server;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerRegister extends ParameterCommand {
    public PlayerRegister() {
        super("register", "Register player to the team\r\n\r\n\tUsage: register <PlayerName>");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String responseMessage = "";

        if (parameters.isEmpty()) {
            responseMessage = "No player name specified.";
        } else {
            String playerName = parameters.get(0);
            if (playerName.trim().isEmpty()) {
                responseMessage = "No player name specified";
            } else {
                Server server = PsyBot.guildController.getGuildStorageObject(guild, new Server());
                server.guildId = guild.getIdLong();

                if (server.playerList.stream().anyMatch(player -> player.name.equalsIgnoreCase(playerName))) {
                    responseMessage = "Player already exists: " + playerName;
                } else {
                    Player newPlayer = new Player();
                    newPlayer.name = playerName;
                    server.playerList.add(newPlayer);
                    PsyBot.guildController.updateGuildStorageObject(guild, server);
                }
            }
        }

        if (!responseMessage.isEmpty())
            channel.sendMessageEmbeds(
                            MessageFormatting.createTextualEmbedMessage("Register Player Response", responseMessage)
                    )
                    .delay(PsyBot.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
    }
}
