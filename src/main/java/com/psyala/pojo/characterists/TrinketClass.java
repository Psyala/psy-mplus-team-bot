package com.psyala.pojo.characterists;

import java.util.Optional;

public enum TrinketClass {
    INTELLECT, AGILITY, STRENGTH, TANK, HEALER;

    public static Optional<TrinketClass> get(String string) {
        switch (string.toUpperCase()) {
            case "INTELLECT":
                return Optional.of(INTELLECT);
            case "AGILITY":
                return Optional.of(AGILITY);
            case "STRENGTH":
                return Optional.of(STRENGTH);
            case "TANK":
                return Optional.of(TANK);
            case "HEALER":
                return Optional.of(HEALER);
            default:
                return Optional.empty();
        }
    }
}
