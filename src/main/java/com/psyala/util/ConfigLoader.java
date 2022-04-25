package com.psyala.util;

import com.psyala.pojo.Configuration;
import com.psyala.pojo.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String file = "/config.properties";
    private static final String cfg_botToken = "cfg.botToken";
    private static final String cfg_resetDay = "cfg.resetDay";
    private static final String cfg_resetHourUtc = "cfg.resetHourUtc";
    private static final String cfg_channelOverview = "cfg.channelOverview";
    private static final String data_serversState = "date.serversState";

    public Optional<Configuration> loadConfiguration() {
        try (FileInputStream stream = new FileInputStream(getClass().getResource(file).getPath())) {
            Properties properties = new Properties();
            properties.load(stream);

            ServerList serversList;
            try {
                String json = properties.getProperty(data_serversState, "{\"serverList\"\\:[]}");
                serversList = JsonParser.fromJson(json, ServerList.class);
            } catch (Exception ex) {
                LOGGER.error("Error parsing JSON", ex);
                serversList = new ServerList();
                serversList.serverList = new ArrayList<>();
            }

            return Optional.of(new Configuration(
                    properties.getProperty(cfg_botToken, ""),
                    properties.getProperty(cfg_resetDay, "Wednesday"),
                    Integer.parseInt(properties.getProperty(cfg_resetHourUtc, "7")),
                    properties.getProperty(cfg_channelOverview, "current-keys"),
                    serversList));
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
            properties.setProperty(cfg_channelOverview, configuration.channelOverviewName);
            properties.setProperty(data_serversState, configuration.saveStateJson);

            properties.store(stream, "Comments");
        } catch (Exception ex) {
            LOGGER.error("Error saving configuration file", ex);
        }
    }
}
