package com.psyala.pojo;

import java.util.List;

public class KeystoneGroup {
    private final Keystone keystone;
    private final Character keystoneHolder;
    private final Character tank;
    private final Character healer;
    private final List<Character> dps;
    private final int numberOfStackRequirementMet;

    public KeystoneGroup(Keystone keystone, Character keystoneHolder, Character tank, Character healer, List<Character> dps, int numberOfStackRequirementMet) {
        this.keystone = keystone;
        this.keystoneHolder = keystoneHolder;
        this.tank = tank;
        this.healer = healer;
        this.dps = dps;
        this.numberOfStackRequirementMet = numberOfStackRequirementMet;
    }

    public Keystone getKeystone() {
        return keystone;
    }

    public Character getKeystoneHolder() {
        return keystoneHolder;
    }

    public Character getTank() {
        return tank;
    }

    public Character getHealer() {
        return healer;
    }

    public List<Character> getDps() {
        return dps;
    }

    public int getNumberOfStackRequirementMet() {
        return numberOfStackRequirementMet;
    }
}
