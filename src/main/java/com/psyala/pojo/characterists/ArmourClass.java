package com.psyala.pojo.characterists;

import java.util.Optional;

public enum ArmourClass {
    CLOTH, LEATHER, MAIL, PLATE;

    public static Optional<ArmourClass> get(String string) {
        switch (string.toUpperCase()) {
            case "CLOTH":
                return Optional.of(CLOTH);
            case "LEATHER":
                return Optional.of(LEATHER);
            case "MAIL":
                return Optional.of(MAIL);
            case "PLATE":
                return Optional.of(PLATE);
            default:
                return Optional.empty();
        }
    }
}
