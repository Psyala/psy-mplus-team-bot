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
    private static final String cfg_resetHourUtc = "cfg.resetHour";
    private static final String cfg_channelOverview = "cfg.channelOverview";
    private static final String cfg_iconKeystone = "cfg.iconKeystone";
    private static final String cfg_iconClassPriest = "cfg.iconClassPriest";
    private static final String cfg_iconClassMage = "cfg.iconClassMage";
    private static final String cfg_iconClassWarlock = "cfg.iconClassWarlock";
    private static final String cfg_iconClassDemonHunter = "cfg.iconClassDemonHunter";
    private static final String cfg_iconClassRogue = "cfg.iconClassRogue";
    private static final String cfg_iconClassMonk = "cfg.iconClassMonk";
    private static final String cfg_iconClassDruid = "cfg.iconClassDruid";
    private static final String cfg_iconClassHunter = "cfg.iconClassHunter";
    private static final String cfg_iconClassShaman = "cfg.iconClassShaman";
    private static final String cfg_iconClassWarrior = "cfg.iconClassWarrior";
    private static final String cfg_iconClassDeathKnight = "cfg.iconClassDeathKnight";
    private static final String cfg_iconClassPaladin = "cfg.iconClassPaladin";
    private static final String cfg_iconRoleTank = "cfg.iconRoleTank";
    private static final String cfg_iconRoleHealer = "cfg.iconRoleHealer";
    private static final String cfg_iconRoleDps = "cfg.iconRoleDps";

    private static final String data_serversState = "data.serversState";

    public Optional<Configuration> loadConfiguration() {
        try (FileInputStream stream = new FileInputStream(getClass().getResource(file).getPath())) {
            Properties properties = new Properties();
            properties.load(stream);

            ServerList serversList;
            try {
                String json = properties.getProperty(data_serversState, "{\"serverList\":[]}");
                serversList = JsonParser.fromJson(json, ServerList.class);
            } catch (Exception ex) {
                LOGGER.error("Error parsing JSON", ex);
                serversList = new ServerList();
                serversList.serverList = new ArrayList<>();
            }

            return Optional.of(new Configuration(
                    properties.getProperty(cfg_botToken, ""),
                    properties.getProperty(cfg_resetDay, "Wednesday"),
                    Integer.parseInt(properties.getProperty(cfg_resetHourUtc, "8")),
                    properties.getProperty(cfg_channelOverview, "current-keys"),
                    serversList,
                    properties.getProperty(cfg_iconKeystone, "<:keystone:971537147284832276> "),
                    properties.getProperty(cfg_iconClassPriest, "<:class_priest:971537147020607528>"),
                    properties.getProperty(cfg_iconClassMage, "<:class_mage:971537147079323689> "),
                    properties.getProperty(cfg_iconClassWarlock, "<:class_warlock:971537146949288006> "),
                    properties.getProperty(cfg_iconClassDemonHunter, "<:class_demon_hunter:971537146806665236> "),
                    properties.getProperty(cfg_iconClassRogue, "<:class_rogue:971537146768949299>"),
                    properties.getProperty(cfg_iconClassMonk, "<:class_monk:971537146928320582>"),
                    properties.getProperty(cfg_iconClassDruid, "<:class_druid:971537147037376512> "),
                    properties.getProperty(cfg_iconClassHunter, "<:class_hunter:971537146978656306> "),
                    properties.getProperty(cfg_iconClassShaman, "<:class_shaman:971537146966073394>"),
                    properties.getProperty(cfg_iconClassWarrior, "<:class_warrior:971537146626338867>"),
                    properties.getProperty(cfg_iconClassDeathKnight, "<:class_death_knight:971537146907336704> "),
                    properties.getProperty(cfg_iconClassPaladin, "<:class_paladin:971537146974457977>"),
                    properties.getProperty(cfg_iconRoleTank, "<:role_tank:971821578230849576>"),
                    properties.getProperty(cfg_iconRoleHealer, "<:role_healer:971821577945624656>"),
                    properties.getProperty(cfg_iconRoleDps, "<:role_dps:971821578021138473>")
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
            properties.setProperty(cfg_channelOverview, configuration.channelOverviewName);
            properties.setProperty(data_serversState, configuration.saveStateJson);
            properties.setProperty(cfg_iconKeystone, configuration.iconKeystone);
            properties.setProperty(cfg_iconClassPriest, configuration.iconClassPriest);
            properties.setProperty(cfg_iconClassMage, configuration.iconClassMage);
            properties.setProperty(cfg_iconClassWarlock, configuration.iconClassWarlock);
            properties.setProperty(cfg_iconClassDemonHunter, configuration.iconClassDemonHunter);
            properties.setProperty(cfg_iconClassRogue, configuration.iconClassRogue);
            properties.setProperty(cfg_iconClassMonk, configuration.iconClassMonk);
            properties.setProperty(cfg_iconClassDruid, configuration.iconClassDruid);
            properties.setProperty(cfg_iconClassHunter, configuration.iconClassHunter);
            properties.setProperty(cfg_iconClassShaman, configuration.iconClassShaman);
            properties.setProperty(cfg_iconClassWarrior, configuration.iconClassWarrior);
            properties.setProperty(cfg_iconClassDeathKnight, configuration.iconClassDeathKnight);
            properties.setProperty(cfg_iconClassPaladin, configuration.iconClassPaladin);
            properties.setProperty(cfg_iconRoleTank, configuration.iconRoleTank);
            properties.setProperty(cfg_iconRoleHealer, configuration.iconRoleHealer);
            properties.setProperty(cfg_iconRoleDps, configuration.iconRoleDps);

            properties.store(stream, "Comments");
            LOGGER.info("Saved configuration to file.");
        } catch (Exception ex) {
            LOGGER.error("Error saving configuration file", ex);
        }
    }
}
