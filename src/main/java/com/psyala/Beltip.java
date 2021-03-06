package com.psyala;

import com.psyala.controller.GuildController;
import com.psyala.controller.MessageController;
import com.psyala.listeners.MessageListener;
import com.psyala.pojo.Configuration;
import com.psyala.pojo.Server;
import com.psyala.util.ConfigLoader;
import com.psyala.util.ResetHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Beltip {
    private static final Logger LOGGER = LoggerFactory.getLogger(Beltip.class);
    private static final Timer resetTimer = new Timer("Reset Timer");
    private static JDA discordBot;

    public static int MESSAGE_DELETE_TIME = 10;
    public static int MESSAGE_IMPORTANT_DELETE_TIME = 30;
    public static Configuration configuration;
    public static GuildController guildController;
    public static MessageController messageController;
    public static String version;

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting up...");
            version = Beltip.class.getPackage().getImplementationVersion() == null ? "DEV" : Beltip.class.getPackage().getImplementationVersion();
            LOGGER.info("Version - " + version);

            ConfigLoader configLoader = new ConfigLoader();
            Optional<Configuration> config = configLoader.loadConfiguration();
            if (!config.isPresent()) return;
            configuration = config.get();
            LOGGER.info("Loaded configuration");

            if (configuration.botToken.isEmpty()) {
                LOGGER.error("No bot token configured!");
                System.exit(-1);
            }

            discordBot = JDABuilder.createLight(config.get().botToken)
                    .setActivity(Activity.listening("your commands"))
                    .disableCache(CacheFlag.ACTIVITY)
                    .disableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new MessageListener())
                    .build();
            discordBot.awaitReady();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Starting Shutdown");
                guildController.purge();
                discordBot.shutdownNow();
                configuration.saveStateJson = guildController.getServerListJson();
                configLoader.saveConfiguration(configuration);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Shutdown");
            }));

            guildController = new GuildController(discordBot.getGuilds(), configLoader);

            messageController = new MessageController();

            resetTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    boolean hasResetHappened = ResetHandler.getInstance().hasResetHappened();
                    if (hasResetHappened) {
                        guildController.getValidGuilds().forEach(guild -> {
                            Server dataObject = guildController.getGuildStorageObject(guild);

                            dataObject.playerList.forEach(player -> {
                                player.characterList.forEach(character -> character.currentKeystone = null);
                            });

                            guildController.updateGuildStorageObject(guild, dataObject);
                        });
                    }
                }
            }, 60000, 60000);

            LOGGER.info("Initialised");
        } catch (Exception ex) {
            LOGGER.error("An error occurred during initialisation", ex);
            System.exit(-1);
        }
    }
}
