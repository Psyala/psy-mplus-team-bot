package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.pojo.Player;
import com.psyala.pojo.Server;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CharacterDelist extends ParameterCommand {
    public CharacterDelist() {
        super("char_delist", "Delist character from the player\r\n\r\n\tUsage: char_delist <CharacterName>");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String responseMessage = "";

        if (parameters.isEmpty()) {
            responseMessage = "No parameters provided";
        } else {
            try {
                String characterName = parameters.get(0);

                Server server = Beltip.guildController.getGuildStorageObject(guild, new Server());
                server.guildId = guild.getIdLong();

                Optional<Player> playerO = server.playerList.stream()
                        .filter(player -> player.characterList
                                .stream()
                                .anyMatch(character -> character.name.equalsIgnoreCase(characterName)))
                        .findFirst();
                if (playerO.isPresent()) {
                    playerO.get().characterList.removeIf(c -> c.name.equalsIgnoreCase(characterName));
                    Beltip.guildController.updateGuildStorageObject(guild, server);
                } else {
                    responseMessage = "Could not find character: " + characterName;
                }
            } catch (IndexOutOfBoundsException ex) {
                responseMessage = "Not all parameters are present";
            } catch (IllegalArgumentException ex) {
                responseMessage = "Could not parse provided class value";
            }
        }

        if (!responseMessage.isEmpty())
            channel.sendMessageEmbeds(
                    MessageFormatting.createTextualEmbedMessage("Register Player Response", responseMessage)
            )
                    .delay(Beltip.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
    }
}
