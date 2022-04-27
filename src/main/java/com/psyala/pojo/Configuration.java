package com.psyala.pojo;

public class Configuration {
    public final String botToken;
    public final String resetDay;
    public final Integer resetHourUtc;
    public final String channelOverviewName;
    public final ServerList initialLoadServerState;
    public String saveStateJson;
    public final String iconKeystone;
    public final String iconClassPriest;
    public final String iconClassMage;
    public final String iconClassWarlock;
    public final String iconClassDemonHunter;
    public final String iconClassRogue;
    public final String iconClassMonk;
    public final String iconClassDruid;
    public final String iconClassHunter;
    public final String iconClassShaman;
    public final String iconClassWarrior;
    public final String iconClassDeathKnight;
    public final String iconClassPaladin;

    public Configuration(String botToken, String resetDay, Integer resetHourUtc, String channelOverview, ServerList initialLoadServerState,
                         String iconKeystone, String iconClassPriest, String iconClassMage, String iconClassWarlock,
                         String iconClassDemonHunter, String iconClassRogue, String iconClassMonk, String iconClassDruid,
                         String iconClassHunter, String iconClassShaman,
                         String iconClassWarrior, String iconClassDeathKnight, String iconClassPaladin) {
        this.botToken = botToken;
        this.resetDay = resetDay;
        this.resetHourUtc = resetHourUtc;
        this.channelOverviewName = channelOverview;
        this.initialLoadServerState = initialLoadServerState;
        this.iconKeystone = iconKeystone;
        this.iconClassPriest = iconClassPriest;
        this.iconClassMage = iconClassMage;
        this.iconClassWarlock = iconClassWarlock;
        this.iconClassDemonHunter = iconClassDemonHunter;
        this.iconClassRogue = iconClassRogue;
        this.iconClassMonk = iconClassMonk;
        this.iconClassDruid = iconClassDruid;
        this.iconClassHunter = iconClassHunter;
        this.iconClassShaman = iconClassShaman;
        this.iconClassWarrior = iconClassWarrior;
        this.iconClassDeathKnight = iconClassDeathKnight;
        this.iconClassPaladin = iconClassPaladin;
    }
}
