package com.psyala.pojo;

import java.util.ArrayList;
import java.util.List;

public class Server {
    public long guildId;
    public List<Player> playerList = new ArrayList<>();

    public long getGuildId() {
        return guildId;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
