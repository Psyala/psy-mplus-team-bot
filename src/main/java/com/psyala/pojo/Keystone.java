package com.psyala.pojo;

public class Keystone {
    public Dungeon dungeon;
    public int level;

    @Override
    public String toString() {
        return "{" +
                "dungeon=" + dungeon.name() +
                ", level=" + level +
                '}';
    }
}
