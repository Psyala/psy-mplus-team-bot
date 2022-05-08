package com.psyala.controller;

import com.psyala.Beltip;
import com.psyala.pojo.Server;
import com.psyala.pojo.ServerList;
import com.psyala.util.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GuildController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildController.class);

    private final Map<TextChannelImpl, MessageAction> overviewMessageEditBuffer = new HashMap<>();
    private final Timer overviewMessageEditTimer = new Timer("Overview Message Timer");

    private final Timer saveDataTimer = new Timer("Save Data Timer");
    private final List<Guild> validGuilds = new ArrayList<>();
    private final Map<Guild, TextChannelImpl> guildOverviewChannelMap = new HashMap<>();
    private final Map<Guild, Message> guildOverviewMessageMap = new HashMap<>();
    private final Map<Guild, Server> guildStorageMap = new HashMap<>();
    private AtomicBoolean dataDirty = new AtomicBoolean();

    public GuildController(List<Guild> guildList, ConfigLoader configLoader) {
        guildList.forEach(guild -> {
            initialiseGuild(guild, true);

            Server defaultServer = new Server();
            defaultServer.guildId = guild.getIdLong();

            Server serverConfig = Beltip.configuration.initialLoadServerState.serverList.stream()
                    .filter(server -> server.guildId == guild.getIdLong())
                    .findFirst()
                    .orElse(defaultServer);

            guildStorageMap.put(guild, serverConfig);
            guildOverviewUpdated(guild);
        });

        saveDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (dataDirty.get()) {
                        Beltip.configuration.saveStateJson = getServerListJson();
                        configLoader.saveConfiguration(Beltip.configuration);
                        dataDirty.set(false);
                    }
                } catch (Exception ex) {
                    LOGGER.error("Error", ex);
                }
            }
        }, 5000, 60000);

        overviewMessageEditTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    overviewMessageEditBuffer.forEach((overviewChannel, messageAction) -> {
                        messageAction.queue();
                    });
                } catch (Exception ex) {
                    LOGGER.error("Error", ex);
                }
            }
        }, 0, 15000);
    }

    public void purge() {
        overviewMessageEditBuffer.clear();
        overviewMessageEditTimer.purge();
        saveDataTimer.purge();
    }

    public boolean initialiseGuild(Guild guild) {
        return initialiseGuild(guild, false);
    }

    public boolean initialiseGuild(Guild guild, boolean suppressUpdate) {
        if (validGuilds.contains(guild)) return true;

        Optional<TextChannelImpl> overviewChannel = DiscordInteractions.getGuildTextChannel(guild, Beltip.configuration.channelOverviewName);
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
                                        + String.join(", ", Beltip.configuration.channelOverviewName)
                                        + "\r\n\r\nOnce this has been done, run the initialise command."
                                )
                        )
                        .queue();
            LOGGER.info("Failed to initialise guild: " + guild.getName());
            return false;
        }
    }

    private void guildOverviewUpdated(Guild guild) {
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
                                .append(MessageFormatting.formatCharacter(character))
                                .append(" ")
                                .append(MessageFormatting.formatRoles(character.getPlayableRoles(), true))
                                .append(" ")
                                .append(MessageFormatting.formatKeystone(character.currentKeystone));

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
            overviewMessageEditBuffer.put(overviewChannel,
                    guildOverviewMessageMap.get(guild)
                            .editMessageEmbeds(messageEmb)
            );

        } else {
            if (overviewChannel != null)
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

    public Server getGuildStorageObject(Guild guild) {
        Server defaultValue = new Server();
        defaultValue.guildId = guild.getIdLong();
        return guildStorageMap.getOrDefault(guild, defaultValue);
    }

    public void updateGuildStorageObject(Guild guild, Server server) {
        guildStorageMap.put(guild, server);
        guildOverviewUpdated(guild);
        dataDirty.set(true);
    }

    public List<Guild> getValidGuilds() {
        return validGuilds;
    }

    public String getServerListJson() {
        try {
            ServerList serverList = new ServerList();
            serverList.serverList = new ArrayList<>(guildStorageMap.values());
            return JsonParser.toJson(serverList);
        } catch (Exception ex) {
            return "{\"serverList\":[]}";
        }
    }
}


