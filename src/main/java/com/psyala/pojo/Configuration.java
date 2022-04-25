package com.psyala.pojo;

public class Configuration {
    public final String botToken;
    public final String resetDay;
    public final Integer resetHourUtc;
    public final String channelOverviewName;

    public Configuration(String botToken, String resetDay, Integer resetHourUtc, String channelOverview) {
        this.botToken = botToken;
        this.resetDay = resetDay;
        this.resetHourUtc = resetHourUtc;
        this.channelOverviewName = channelOverview;
    }
}
