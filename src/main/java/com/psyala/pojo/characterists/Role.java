package com.psyala.pojo.characterists;

import com.psyala.Beltip;

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
}
