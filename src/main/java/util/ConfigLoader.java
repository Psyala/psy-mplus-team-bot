package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String cfg_botToken = "cfg.botToken";
    private static final String data_characters = "data_characters";

    public Optional<Configuration> loadConfiguration() {
        try (InputStream stream = new FileInputStream(getClass().getResource("config.properties").getPath())) {
            Properties properties = new Properties();
            properties.load(stream);

            return Optional.of(new Configuration(
                    properties.getProperty(cfg_botToken, "")
            ));
        } catch (Exception ex) {
            LOGGER.error("Error loading configuration file", ex);
            return Optional.empty();
        }
    }

    public void saveConfiguration(Configuration configuration) {
        try (OutputStream stream = new FileOutputStream(getClass().getResource("config.properties").getPath())) {
            Properties properties = new Properties();
            properties.setProperty(cfg_botToken, configuration.botToken);

            properties.store(stream, null);
        } catch (Exception ex) {
            LOGGER.error("Error saving configuration file", ex);
        }
    }
}
