package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.pojo.Character;
import com.psyala.pojo.Player;
import com.psyala.pojo.Server;
import com.psyala.pojo.characterists.Role;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RoleUpdate extends ParameterCommand {
    public RoleUpdate() {
        super("role_update", "Updates a characters preferred role(s)\r\n\r\n\tUsage: role_update <CharacterName> <Role...>");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String responseMessage = "";

        if (parameters.isEmpty()) {
            responseMessage = "No parameters provided";
        } else if (parameters.size() == 1) {
            responseMessage = "Incorrect number of parameters provided";
        } else {
            try {
                Server server = Beltip.guildController.getGuildStorageObject(guild);
                String characterName = parameters.get(0);

                Optional<Player> playerO = server.playerList.stream()
                        .filter(player -> player.containsCharacter(characterName))
                        .findFirst();
                if (playerO.isPresent()) {
                    Character character = playerO.get().getCharacter(characterName);

                    List<String> remainingParams = new ArrayList<>(parameters);
                    remainingParams.remove(0);

                    List<Role> foundRoles = new ArrayList<>();
                    for (String remainingParam : remainingParams) {
                        Optional<Role> role = Role.get(remainingParam.toUpperCase());
                        role.ifPresent(foundRoles::add);
                    }

                    boolean allRolesOk = foundRoles.stream()
                            .allMatch(role -> character.characterClass.getPossibleRoles().contains(role));

                    if (allRolesOk) {
                        character.playableRoles.clear();
                        character.playableRoles.addAll(foundRoles);
                        Beltip.guildController.updateGuildStorageObject(guild, server);
                    } else {
                        responseMessage = "Invalid role provided for character class: " + character.characterClass.name();
                    }
                } else {
                    responseMessage = "Could not find character: " + characterName;
                }

            } catch (IndexOutOfBoundsException ex) {
                responseMessage = "Not all parameters are present";
            } catch (IllegalArgumentException ex) {
                responseMessage = "Could not parse provided role value";
            }
        }

        if (!responseMessage.isEmpty())
            channel.sendMessageEmbeds(
                    MessageFormatting.createTextualEmbedMessage("Role Update Response", responseMessage)
            )
                    .delay(Beltip.MESSAGE_DELETE_TIME, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
    }
}
