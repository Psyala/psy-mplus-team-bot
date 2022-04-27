package com.psyala.controller;

import com.psyala.PsyBot;
import com.psyala.pojo.Server;
import com.psyala.pojo.ServerList;
import com.psyala.util.DiscordInteractions;
import com.psyala.util.MessageFormatting;
import com.psyala.util.ResetHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GuildController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildController.class);

    private final List<Guild> validGuilds = new ArrayList<>();
    private final Map<Guild, TextChannelImpl> guildOverviewChannelMap = new HashMap<>();
    private final Map<Guild, Message> guildOverviewMessageMap = new HashMap<>();
    private final Map<Guild, Server> guildStorageMap = new HashMap<>();

    public GuildController(List<Guild> guildList) {
        guildList.forEach(guild -> {
            initialiseGuild(guild, true);

            Optional<Server> initialConfig = PsyBot.configuration.initialLoadServerState.serverList.stream()
                    .filter(server -> server.guildId == guild.getIdLong())
                    .findFirst();
            if (initialConfig.isPresent()) {
                guildStorageMap.put(guild, initialConfig.get());
                guildOverviewUpdated(guild);
            }
        });
    }

    public boolean initialiseGuild(Guild guild) {
        return initialiseGuild(guild, false);
    }

    public boolean initialiseGuild(Guild guild, boolean suppressUpdate) {
        if (validGuilds.contains(guild)) return true;

        Optional<TextChannelImpl> overviewChannel = DiscordInteractions.getGuildTextChannel(guild, PsyBot.configuration.channelOverviewName);
        boolean allChannelsFound = overviewChannel.isPresent();

        if (allChannelsFound) {
            DiscordInteractions.cleanupTextChannel(overviewChannel.get());
            guildOverviewChannelMap.put(guild, overviewChannel.get());
            validGuilds.add(guild);
            if (!suppressUpdate) guildOverviewUpdated(guild);
            LOGGER.info("Initialised guild: " + guild.getName());
            return true;
        } else {
            if (guild.getSystemChannel() != null)
                guild.getSystemChannel()
                        .sendMessageEmbeds(MessageFormatting.createTextualEmbedMessage(
                                "Server not configured correctly",
                                "This bot requires the following text channels to be present:\r\n"
                                        + String.join(", ", PsyBot.configuration.channelOverviewName)
                                        + "\r\nOnce this has been done, run the initialise command."
                                )
                        )
                        .queue();
            LOGGER.info("Failed to initialise guild: " + guild.getName());
            return false;
        }
    }

    public void guildOverviewUpdated(Guild guild) {
        TextChannelImpl overviewChannel = guildOverviewChannelMap.get(guild);

        StringBuilder output = new StringBuilder();
        output.append("**Reset Day:** :calendar: ")
                .append(ResetHandler.getInstance().getNextResetDay().format(ResetHandler.DATE_FORMAT))
                .append("\r\n\r\n");

        if (!guildStorageMap.containsKey(guild) || guildStorageMap.get(guild).playerList.isEmpty()) {
            output.append(":bangbang: **No players configured** :bangbang:");
        } else {
            List<String> playerOutput = new ArrayList<>();
            guildStorageMap.get(guild).playerList.stream().sorted(Comparator.comparing(o -> o.name)).forEach(player -> {
                StringBuilder playerBuilder = new StringBuilder();
                playerBuilder.append("**").append(player.name).append("**\r\n");

                if (player.characterList.isEmpty()) {
                    playerBuilder.append(":bangbang: **No characters configured** :bangbang:");
                } else {
                    List<String> characterOutput = new ArrayList<>();
                    player.characterList.stream().sorted(Comparator.comparing(o -> o.name)).forEach(character -> {
                        StringBuilder charBuilder = new StringBuilder()
                                .append(character.characterClass.getClassIcon())
                                .append("` ")
                                .append(StringUtils.rightPad(character.name, 25, " "))
                                .append("`")
                                .append(PsyBot.configuration.iconKeystone);

                        if (character.currentKeystone == null) {
                            charBuilder.append("` ")
                                    .append(StringUtils.rightPad("None", 10, " "))
                                    .append("`");
                        } else {
                            charBuilder.append("` ")
                                    .append(StringUtils.rightPad(character.currentKeystone.dungeon.getAcronym(), 8, " "))
                                    .append(StringUtils.leftPad(String.valueOf(character.currentKeystone.level), 2, " "))
                                    .append("`");
                        }

                        characterOutput.add(charBuilder.toString());
                    });
                    playerBuilder.append(String.join("\r\n", characterOutput));
                }
                playerOutput.add(playerBuilder.toString());
            });
            output.append(String.join("\r\n\r\n", playerOutput));
        }

        MessageEmbed messageEmb = MessageFormatting.createTextualEmbedMessage(
                MessageFormatting.biggify("Team Keys"),
                output.toString()
        );

        if (guildOverviewMessageMap.containsKey(guild)) {
            guildOverviewMessageMap.get(guild)
                    .editMessageEmbeds(messageEmb)
                    .queue(message -> {
                        LOGGER.info("Edited Overview Message for: ".concat(guild.getName()));
                    });
        } else {
            overviewChannel.sendMessageEmbeds(messageEmb)
                    .queue(message -> {
                        LOGGER.info("Sent Overview Message to: ".concat(guild.getName()));
                        guildOverviewMessageMap.put(guild, message);
                    });
        }
    }

    public TextChannelImpl getGuildOverviewChannel(Guild guild) {
        return guildOverviewChannelMap.get(guild);
    }

    public Server getGuildStorageObject(Guild guild, Server defaultValue) {
        return guildStorageMap.getOrDefault(guild, defaultValue);
    }

    public void saveGuildStorageObject(Guild guild, Server server) {
        guildStorageMap.put(guild, server);
    }

    public List<Guild> getValidGuilds() {
        return validGuilds;
    }

    public ServerList getServerList() {
        ServerList serverList = new ServerList();
        serverList.serverList = new ArrayList<>(guildStorageMap.values());
        return serverList;
    }


}

