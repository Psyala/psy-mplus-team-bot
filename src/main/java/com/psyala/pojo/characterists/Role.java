package com.psyala.pojo.characterists;

import com.psyala.Beltip;

import java.util.Optional;

public enum Role {
    TANK(0), HEALER(1), DPS(2);

    private final int i;

    Role(int i) {
        this.i = i;
    }

    public String getRoleIcon() {
        switch (this) {
            case TANK:
                return Beltip.configuration.iconRoleTank;
            case HEALER:
                return Beltip.configuration.iconRoleHealer;
            case DPS:
                return Beltip.configuration.iconRoleDps;
            default:
                return ":question:";
        }
    }

    public int getI() {
        return i;
    }

    public static Optional<Role> get(String string) {
        switch (string.toUpperCase()) {
            case "TANK":
                return Optional.of(TANK);
            case "HEAL":
            case "HEALER":
                return Optional.of(HEALER);
            case "DAMAGE":
            case "DD":
            case "DPS":
                return Optional.of(DPS);
            default:
                return Optional.empty();
        }
    }

}
