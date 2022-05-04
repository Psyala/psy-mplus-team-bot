package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.pojo.Character;
import com.psyala.pojo.*;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class KeystoneRegister extends ParameterCommand {
    public KeystoneRegister() {
        super("keystone", "Register a keystone to the character\r\n\r\n\tUsage: keystone <CharacterName> <DungeonAcronym> <Level>");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String responseMessage = "";

        if (parameters.isEmpty()) {
            responseMessage = "No parameters provided";
        } else {
            try {
                String characterName = parameters.get(0);
                String dungeonAcronym = parameters.get(1);
                int dungeonLevel = Integer.parseInt(parameters.get(2));

                Server server = Beltip.guildController.getGuildStorageObject(guild, new Server());
                server.guildId = guild.getIdLong();

                Optional<Player> playerO = server.playerList.stream()
                        .filter(player -> player.containsCharacter(characterName))
                        .findFirst();
                if (playerO.isPresent()) {
                    Character character = playerO.get().getCharacter(characterName);
                    Dungeon dungeon = Dungeon.fromAcronym(dungeonAcronym);

                    Keystone keystone = new Keystone();
                    keystone.dungeon = dungeon;
                    keystone.level = dungeonLevel;
                    character.currentKeystone = keystone;

                    Beltip.guildController.updateGuildStorageObject(guild, server);
                } else {
                    responseMessage = "Could not find player for character: " + characterName;
                }
            } catch (IndexOutOfBoundsException ex) {
                responseMessage = "Not all parameters are present";
            } catch (NumberFormatException ex) {
                responseMessage = "Failed to parse provided dungeon level";
            } catch (IllegalArgumentException ex) {
                responseMessage = "Could not parse provided dungeon value";
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
