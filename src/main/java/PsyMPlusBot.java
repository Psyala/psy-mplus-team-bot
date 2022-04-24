import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConfigLoader;
import util.Configuration;

import java.util.Optional;

public class PsyMPlusBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(PsyMPlusBot.class);
    private static JDA discordBot;

    public static void main(String[] args) {
        try {
            Optional<Configuration> configuration = ConfigLoader.loadConfiguration();
            if (configuration.isEmpty()) throw new Exception("Configuration File is Empty");

            discordBot = JDABuilder.createDefault(configuration.get().botToken)
                    .setActivity(Activity.listening("to your commands"))
                    .build();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                discordBot.shutdown();
                ConfigLoader.saveConfiguration(configuration.get());
            }));
        } catch (Exception ex) {
            LOGGER.error("An error occurred during initialisation", ex);
        }
    }
}
