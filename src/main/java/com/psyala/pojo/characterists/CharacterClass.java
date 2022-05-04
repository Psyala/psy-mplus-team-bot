package com.psyala.pojo.characterists;

import com.psyala.Beltip;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CharacterClass {
    PRIEST(ArmourClass.CLOTH, Arrays.asList(TrinketClass.INTELLECT, TrinketClass.HEALER)),
    MAGE(ArmourClass.CLOTH, Collections.singletonList(TrinketClass.INTELLECT)),
    WARLOCK(ArmourClass.CLOTH, Collections.singletonList(TrinketClass.INTELLECT)),
    DEMON_HUNTER(ArmourClass.LEATHER, Arrays.asList(TrinketClass.AGILITY, TrinketClass.TANK)),
    ROGUE(ArmourClass.LEATHER, Collections.singletonList(TrinketClass.AGILITY)),
    MONK(ArmourClass.LEATHER, Arrays.asList(TrinketClass.INTELLECT, TrinketClass.AGILITY, TrinketClass.HEALER, TrinketClass.TANK)),
    DRUID(ArmourClass.LEATHER, Arrays.asList(TrinketClass.INTELLECT, TrinketClass.AGILITY, TrinketClass.HEALER, TrinketClass.TANK)),
    HUNTER(ArmourClass.MAIL, Collections.singletonList(TrinketClass.AGILITY)),
    SHAMAN(ArmourClass.MAIL, Arrays.asList(TrinketClass.INTELLECT, TrinketClass.AGILITY, TrinketClass.HEALER)),
    //    EVOKER(ArmourClass.MAIL, Arrays.asList(TrinketClass.INTELLECT, TrinketClass.HEALER)),
    WARRIOR(ArmourClass.PLATE, Arrays.asList(TrinketClass.STRENGTH, TrinketClass.TANK)),
    DEATH_KNIGHT(ArmourClass.PLATE, Arrays.asList(TrinketClass.STRENGTH, TrinketClass.TANK)),
    PALADIN(ArmourClass.PLATE, Arrays.asList(TrinketClass.STRENGTH, TrinketClass.INTELLECT, TrinketClass.HEALER, TrinketClass.TANK));

    private final ArmourClass armourClass;
    private final List<TrinketClass> trinketClassList;

    CharacterClass(ArmourClass armourClass, List<TrinketClass> trinketClassList) {
        this.armourClass = armourClass;
        this.trinketClassList = trinketClassList;
    }

    public ArmourClass getArmourClass() {
        return armourClass;
    }

    public List<TrinketClass> getTrinketClassList() {
        return trinketClassList;
    }

    public String getClassIcon() {
        switch (this) {
            case PRIEST:
                return Beltip.configuration.iconClassPriest;
            case MAGE:
                return Beltip.configuration.iconClassMage;
            case WARLOCK:
                return Beltip.configuration.iconClassWarlock;
            case DEMON_HUNTER:
                return Beltip.configuration.iconClassDemonHunter;
            case ROGUE:
                return Beltip.configuration.iconClassRogue;
            case MONK:
                return Beltip.configuration.iconClassMonk;
            case DRUID:
                return Beltip.configuration.iconClassDruid;
            case HUNTER:
                return Beltip.configuration.iconClassHunter;
            case SHAMAN:
                return Beltip.configuration.iconClassShaman;
            case WARRIOR:
                return Beltip.configuration.iconClassWarrior;
            case DEATH_KNIGHT:
                return Beltip.configuration.iconClassDeathKnight;
            case PALADIN:
                return Beltip.configuration.iconClassPaladin;
            default:
                return ":question:";
        }
    }
}
