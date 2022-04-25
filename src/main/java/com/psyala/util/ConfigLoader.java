package com.psyala.util;

import com.psyala.pojo.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String file = "/config.properties";
    private static final String cfg_botToken = "cfg.botToken";
    private static final String cfg_resetDay = "cfg.resetDay";
    private static final String cfg_resetHourUtc = "cfg.resetHourUtc";
    private static final String cfg_channelOverview = "cfg.channelOverview";
    private static final String cfg_channelQuery = "cfg.channelQuery";
    private static final String data_characters = "data_characters";

    public Optional<Configuration> loadConfiguration() {
        try (FileInputStream stream = new FileInputStream(getClass().getResource(file).getPath())) {
            Properties properties = new Properties();
            properties.load(stream);

            return Optional.of(new Configuration(
                    properties.getProperty(cfg_botToken, ""),
                    properties.getProperty(cfg_resetDay, "Wednesday"),
                    Integer.parseInt(properties.getProperty(cfg_resetHourUtc, "7")),
                    properties.getProperty(cfg_channelOverview, "current-keys"),
                    properties.getProperty(cfg_channelQuery, "boost-query")
            ));
        } catch (Exception ex) {
            LOGGER.error("Error loading configuration file", ex);
            return Optional.empty();
        }
    }

    public void saveConfiguration(Configuration configuration) {
        try (OutputStream stream = new FileOutputStream(getClass().getResource(file).getPath())) {
            Properties properties = new Properties();
            properties.setProperty(cfg_botToken, configuration.botToken);
            properties.setProperty(cfg_resetDay, configuration.resetDay);
            properties.setProperty(cfg_resetHourUtc, String.valueOf(configuration.resetHourUtc));
            properties.setProperty(cfg_channelOverview, configuration.channelOverview);
            properties.setProperty(cfg_channelQuery, configuration.channelQuery);

            properties.store(stream, "Comments");
        } catch (Exception ex) {
            LOGGER.error("Error saving configuration file", ex);
        }
    }
}
