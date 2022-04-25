package com.psyala;

import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Configuration;
import com.psyala.pojo.Server;
import com.psyala.util.ConfigLoader;
import com.psyala.util.DiscordInteractions;
import com.psyala.util.MessageFormatting;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PsyBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(PsyBot.class);
    private static JDA discordBot;

    public static Configuration configuration;
    public static List<Guild> validGuilds = new ArrayList<>();
    public static Map<Guild, TextChannelImpl> guildOverviewChannelMap = new HashMap<>();
    public static Map<Guild, Server> guildServerMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            ConfigLoader configLoader = new ConfigLoader();
            Optional<Configuration> config = configLoader.loadConfiguration();
            if (config.isEmpty()) return;
            configuration = config.get();

            discordBot = JDABuilder.createLight(config.get().botToken)
                    .setActivity(Activity.listening("your commands"))
                    .disableCache(CacheFlag.ACTIVITY)
                    .disableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new MessageListener())
                    .build();
            discordBot.awaitReady();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Starting Shutdown");
                discordBot.shutdownNow();
                configLoader.saveConfiguration(configuration);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Shutdown");
            }));

            discordBot.getGuilds().forEach(PsyBot::initialiseGuild);

            LOGGER.info("Initialised");
        } catch (Exception ex) {
            LOGGER.error("An error occurred during initialisation", ex);
            System.exit(-1);
        }
    }

    public static boolean initialiseGuild(Guild guild) {
        if (validGuilds.contains(guild)) return true;

        Optional<TextChannelImpl> overviewChannel = DiscordInteractions.getGuildTextChannel(guild, PsyBot.configuration.channelOverviewName);
        boolean allChannelsFound = overviewChannel.isPresent();

        if (allChannelsFound) {
            guildOverviewChannelMap.put(guild, overviewChannel.get());
            validGuilds.add(guild);
            guildOverviewUpdated(guild);
            LOGGER.info("Initialised guild: " + guild.getName());
            return true;
        } else {
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

    public static void guildOverviewUpdated(Guild guild) {
        TextChannelImpl overviewChannel = guildOverviewChannelMap.get(guild);
        DiscordInteractions.cleanupTextChannel(overviewChannel);

        StringBuilder output = new StringBuilder();
        if (!guildServerMap.containsKey(guild) || guildServerMap.get(guild).playerList.isEmpty()) {
            output.append(":bangbang: **No players configured** :bangbang:");
        } else {
            List<String> playerOutput = new ArrayList<>();
            guildServerMap.get(guild).playerList.stream().sorted(Comparator.comparing(o -> o.name)).forEach(player -> {
                StringBuilder playerBuilder = new StringBuilder();
                playerBuilder.append("**").append(player.name).append("**\r\n");

                if (player.characterList.isEmpty()) {
                    playerBuilder.append("    :bangbang: **No characters configured** :bangbang:");
                } else {
                    //TODO: Output char details
                }
                playerOutput.add(playerBuilder.toString());
            });
            output.append(String.join("\r\n\r\n", playerOutput));
        }

        overviewChannel.sendMessageEmbeds(MessageFormatting.createTextualEmbedMessage(
                        "Team Keystone Overview",
                        output.toString()
                ))
                .queue();
    }
}
