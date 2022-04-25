package com.psyala;

import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Configuration;
import com.psyala.util.ConfigLoader;
import com.psyala.util.DiscordInteractions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PsyMPlusBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(PsyMPlusBot.class);
    private static JDA discordBot;
    public static Configuration configuration;

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

            List<String> channels = Arrays.asList(configuration.channelOverview, configuration.channelQuery);
            boolean channelsExists = channels.stream()
                    .allMatch(channel -> DiscordInteractions.ensureChannelExists(discordBot, channel));
            if (!channelsExists) {
                LOGGER.error("Not all managed channels exist: " + channels.stream().collect(Collectors.joining(",")));
                System.exit(-1);
            }

            DiscordInteractions.cleanupChannel(discordBot, configuration.channelOverview);
            DiscordInteractions.cleanupChannel(discordBot, configuration.channelQuery);

            LOGGER.info("Initialised");
        } catch (Exception ex) {
            LOGGER.error("An error occurred during initialisation", ex);
        }
    }

}
