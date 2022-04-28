package com.psyala.commands;

import com.psyala.PsyBot;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.pojo.Player;
import com.psyala.pojo.Server;
import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CharacterRegister extends ParameterCommand {
    public CharacterRegister() {
        super("char_add", "Register character to the player\r\n\r\n\tUsage: char_add <PlayerName> <CharacterName> <CharacterClass>");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String responseMessage = "";

        if (parameters.isEmpty()) {
            responseMessage = "No parameters provided";
        } else {
            try {
                String playerName = parameters.get(0);
                String characterName = parameters.get(1);
                String characterClass = parameters.get(2);

                Server server = PsyBot.guildController.getGuildStorageObject(guild, new Server());
                server.guildId = guild.getIdLong();

                Optional<Player> playerO = server.playerList.stream().filter(player -> player.name.equalsIgnoreCase(playerName)).findFirst();
                if (playerO.isPresent()) {

                    if (server.playerList
                            .stream()
                            .anyMatch(
                                    player -> player.characterList.stream().anyMatch(character -> character.name.equalsIgnoreCase(characterName))
                            )) {
                        responseMessage = "Character already exists: " + characterName;
                    } else {
                        com.psyala.pojo.Character character = new com.psyala.pojo.Character();
                        character.characterClass = CharacterClass.valueOf(characterClass.toUpperCase());
                        character.name = characterName;
                        character.currentKeystone = null;
                        playerO.get().characterList.add(character);

                        PsyBot.guildController.updateGuildStorageObject(guild, server);
                    }
                } else {
                    responseMessage = "Could not find player: " + playerName;
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
                    .delay(PsyBot.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
    }
}
