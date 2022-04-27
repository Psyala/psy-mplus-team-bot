package com.psyala;

import com.psyala.controller.GuildController;
import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Configuration;
import com.psyala.util.ConfigLoader;
import com.psyala.util.JsonParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PsyBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(PsyBot.class);
    private static JDA discordBot;

    public static int MESSAGE_DELETE_TIME = 10;
    public static Configuration configuration;
    public static GuildController guildController;

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
                try {
                    configuration.saveStateJson = JsonParser.toJson(guildController.getServerList());
                } catch (Exception ex) {
                    configuration.saveStateJson = "{\"serverList\":[]}";
                }
                configLoader.saveConfiguration(configuration);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Shutdown");
            }));

            guildController = new GuildController(discordBot.getGuilds());

            LOGGER.info("Initialised");
        } catch (Exception ex) {
            LOGGER.error("An error occurred during initialisation", ex);
            System.exit(-1);
        }
    }


}
