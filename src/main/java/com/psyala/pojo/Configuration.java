package com.psyala.pojo;

public class Configuration {
    public final String botToken;
    public final String resetDay;
    public final Integer resetHourUtc;
    public final String channelOverview;
    public final String channelQuery;

    public Configuration(String botToken, String resetDay, Integer resetHourUtc, String channelOverview, String channelQuery) {
        this.botToken = botToken;
        this.resetDay = resetDay;
        this.resetHourUtc = resetHourUtc;
        this.channelOverview = channelOverview;
        this.channelQuery = channelQuery;
    }

}
