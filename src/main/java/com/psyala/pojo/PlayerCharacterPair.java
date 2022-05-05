package com.psyala.pojo;

public class PlayerCharacterPair {
    private final Player player;
    private final Character character;

    public PlayerCharacterPair(Player player, Character character) {
        this.player = player;
        this.character = character;
    }

    public Player getPlayer() {
        return player;
    }

    public Character getCharacter() {
        return character;
    }
}
