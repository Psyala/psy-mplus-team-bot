package com.psyala.commands;

import com.psyala.Beltip;
import com.psyala.commands.base.ParameterCommand;
import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Character;
import com.psyala.pojo.*;
import com.psyala.pojo.characterists.ArmourClass;
import com.psyala.pojo.characterists.Role;
import com.psyala.pojo.characterists.TrinketClass;
import com.psyala.util.CombinationGenerator;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KeyQuery extends ParameterCommand {
    public KeyQuery() {
        super("query", "Query to see if a group for a specific keystone is possible\r\n\r\n\tUsage: query <DungeonAcronym> <Level> [ArmourClass or TrinketClass]");
    }

    @Override
    public void handle(Guild guild, User author, MessageChannel channel, List<String> parameters) {
        String initialRequest = author.getAsMention() + " - " + MessageListener.COMMAND_CHAR + getCommand() + " " + String.join(" ", parameters);
        String responseMessage = "";
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (parameters.isEmpty()) {
            responseMessage = "No parameters provided";
        } else {
            try {
                String dungeonAcronym = parameters.get(0);
                Dungeon dungeon = Dungeon.fromAcronym(dungeonAcronym);
                int dungeonLevel = Integer.parseInt(parameters.get(1));
                ArmourClass armourClass = null;
                TrinketClass trinketClass = null;

                if (parameters.size() > 2) {
                    String stackParam = parameters.get(2);
                    if (ArmourClass.get(stackParam).isPresent()) {
                        armourClass = ArmourClass.get(stackParam).get();
                    } else if (TrinketClass.get(stackParam).isPresent()) {
                        trinketClass = TrinketClass.get(stackParam).get();
                    }
                }

                Server server = Beltip.guildController.getGuildStorageObject(guild);

                Keystone requestedKeystone = new Keystone();
                requestedKeystone.dungeon = dungeon;
                requestedKeystone.level = dungeonLevel;
                boolean stackRequired = armourClass != null || trinketClass != null;

                List<KeystoneGroup> keystoneGroups = getKeystoneGroups(server, requestedKeystone, armourClass, trinketClass);
                if (keystoneGroups.isEmpty()) {
                    responseMessage = "No suitable group configuration found";
                } else {
                    responseMessage = ":bangbang: **Suitable group has been found** :bangbang:\r\n\r\n**Possible Groups:**";
                    AtomicInteger i = new AtomicInteger();
                    keystoneGroups.stream()
                            .sorted((o1, o2) -> Integer.compare(o2.getNumberOfStackRequirementMet(), o1.getNumberOfStackRequirementMet()))
                            .forEach(keystoneGroup -> {
                                StringBuilder groupComp = new StringBuilder();
                                groupComp.append(outputCharacter(keystoneGroup.getTank(), Role.TANK, keystoneGroup.getTank().name.equalsIgnoreCase(keystoneGroup.getKeystoneHolder().name))).append("\r\n");
                                groupComp.append(outputCharacter(keystoneGroup.getHealer(), Role.HEALER, keystoneGroup.getHealer().name.equalsIgnoreCase(keystoneGroup.getKeystoneHolder().name))).append("\r\n");
                                keystoneGroup.getDps().forEach(dps -> {
                                    groupComp.append(outputCharacter(dps, Role.DPS, dps.name.equalsIgnoreCase(keystoneGroup.getKeystoneHolder().name))).append("\r\n");
                                });

                                embedBuilder.addField(
                                        "Group " + i.incrementAndGet() + (stackRequired ? " | Stack Requirements Met: " + keystoneGroup.getNumberOfStackRequirementMet() : ""),
                                        groupComp.toString(),
                                        false
                                );
                            });
                }
            } catch (IndexOutOfBoundsException ex) {
                responseMessage = "Not all parameters are present";
            } catch (NumberFormatException ex) {
                responseMessage = "Failed to parse provided dungeon level";
            } catch (IllegalArgumentException ex) {
                responseMessage = "Could not parse provided dungeon value";
            }
        }

        MessageEmbed messageEmbed = embedBuilder
                .setTitle("Keystone Query Response")
                .setDescription(initialRequest + "\r\n\r\n" + responseMessage)
                .setColor(MessageFormatting.EMBED_COLOUR)
                .build();
        channel.sendMessageEmbeds(messageEmbed)
                .queue();
    }

    private String outputCharacter(Character character, Role role, boolean keystoneHolder) {
        return MessageFormatting.formatCharacter(character) + " " + role.name() + " " + (keystoneHolder ? Beltip.configuration.iconKeystone : "");
    }

    private List<KeystoneGroup> getKeystoneGroups(Server server, Keystone requestedKeystone, ArmourClass armourClass, TrinketClass trinketClass) {
        List<PlayerCharacterPair> possibleKeyHolders = findPossibleKeyHolders(server, requestedKeystone);

        List<KeystoneGroup> foundKeystoneGroups = new ArrayList<>();
        possibleKeyHolders.forEach(keyholder -> {
            List<Player> otherPlayers = server.playerList.stream()
                    .filter(player -> !player.name.equalsIgnoreCase(keyholder.getPlayer().name))
                    .collect(Collectors.toList());

            List<KeystoneGroup> possibleComps = getPossibleGroupComps(requestedKeystone, keyholder, otherPlayers, armourClass, trinketClass);
            if (armourClass != null || trinketClass != null) {
                possibleComps.removeIf(keystoneGroup -> keystoneGroup.getNumberOfStackRequirementMet() < 3);
            }
            foundKeystoneGroups.addAll(possibleComps);
        });

        return foundKeystoneGroups;
    }

    private List<KeystoneGroup> getPossibleGroupComps(Keystone keystone, PlayerCharacterPair keystoneHolder, List<Player> otherPlayers, ArmourClass armourClass, TrinketClass trinketClass) {
        List<PlayerCharacterPair> possibleTanks = getListOfPossibleRole(Role.TANK, keystoneHolder, otherPlayers);
        List<PlayerCharacterPair> possibleHealers = getListOfPossibleRole(Role.HEALER, keystoneHolder, otherPlayers);
        List<PlayerCharacterPair> possibleDps = getListOfPossibleRole(Role.DPS, keystoneHolder, otherPlayers);

        List<KeystoneGroup> keystoneGroups = new ArrayList<>();
        possibleTanks.forEach(tank -> {
            List<PlayerCharacterPair> filteredHealers = possibleHealers.stream()
                    .filter(healer -> !healer.getPlayer().name.equals(tank.getPlayer().name))
                    .collect(Collectors.toList());

            filteredHealers.forEach(healer -> {
                List<PlayerCharacterPair> filteredDps = possibleDps.stream()
                        .filter(dps -> !dps.getPlayer().name.equals(tank.getPlayer().name) && !dps.getPlayer().name.equals(healer.getPlayer().name))
                        .collect(Collectors.toList());

                if (filteredDps.size() >= 3) {
                    List<List<PlayerCharacterPair>> dpsCombinations = CombinationGenerator.generate(filteredDps.size(), 3, filteredDps);
                    dpsCombinations.forEach(dpsCombination -> {
                        int stackRequirementsMet = 0;
                        if (doesCharacterFitStackRequirement(tank.getCharacter(), armourClass, trinketClass))
                            stackRequirementsMet++;
                        if (doesCharacterFitStackRequirement(healer.getCharacter(), armourClass, trinketClass))
                            stackRequirementsMet++;
                        for (PlayerCharacterPair dps : dpsCombination) {
                            if (doesCharacterFitStackRequirement(dps.getCharacter(), armourClass, trinketClass))
                                stackRequirementsMet++;
                        }

                        keystoneGroups.add(
                                new KeystoneGroup(
                                        keystone,
                                        keystoneHolder.getCharacter(),
                                        tank.getCharacter(),
                                        healer.getCharacter(),
                                        dpsCombination.stream().map(PlayerCharacterPair::getCharacter).collect(Collectors.toList()),
                                        stackRequirementsMet
                                )
                        );
                    });
                }
            });
        });

        return keystoneGroups;
    }

    private boolean doesCharacterFitStackRequirement(Character character, ArmourClass armourClass, TrinketClass trinketClass) {
        if (armourClass == null && trinketClass == null) return true;
        if (armourClass != null && armourClass.equals(character.characterClass.getArmourClass())) return true;
        if (trinketClass != null && character.characterClass.getTrinketClassList().contains(trinketClass)) return true;
        return false;
    }

    private List<PlayerCharacterPair> getListOfPossibleRole(Role desiredRole, PlayerCharacterPair keystoneHolder, List<Player> otherPlayers) {
        List<PlayerCharacterPair> possiblePlayersOfRole = new ArrayList<>();

        if (keystoneHolder.getCharacter().getPlayableRoles().contains(desiredRole))
            possiblePlayersOfRole.add(keystoneHolder);

        otherPlayers.forEach(player -> {
            player.characterList.stream()
                    .filter(character -> character.getPlayableRoles().contains(desiredRole))
                    .forEach(character -> {
                        possiblePlayersOfRole.add(new PlayerCharacterPair(player, character));
                    });
        });
        return possiblePlayersOfRole;
    }

    private List<PlayerCharacterPair> findPossibleKeyHolders(Server server, Keystone requestedKeystone) {
        List<PlayerCharacterPair> possibleKeyHolders = new ArrayList<>();

        server.playerList.forEach(player -> {
            player.characterList.stream()
                    .filter(character -> character.currentKeystone != null)
                    .filter(character -> character.currentKeystone.dungeon.equals(requestedKeystone.dungeon))
                    .filter(character -> character.currentKeystone.level >= requestedKeystone.level)
                    .forEach(character -> {
                        possibleKeyHolders.add(new PlayerCharacterPair(player, character));
                    });
        });

        return possibleKeyHolders;
    }

    public int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return (n * factorial(n - 1));
    }
}
