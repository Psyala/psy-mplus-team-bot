package com.psyala.commands;

import com.psyala.pojo.Character;
import com.psyala.pojo.Dungeon;
import com.psyala.pojo.Player;
import com.psyala.pojo.Server;
import com.psyala.pojo.characterists.ArmourClass;
import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.pojo.characterists.Role;
import org.junit.Assert;
import org.junit.Test;
import test_util.Builder;

import java.util.ArrayList;
import java.util.Arrays;

public class KeyQueryTest {
    KeyQuery classUnderTest = new KeyQuery();

    private Server getTestData() {
        Server s = new Server();
        s.guildId = 1;
        s.playerList = new ArrayList<>();

        //Player1
        {
            Character c1 = Builder.character("Player1Character1", CharacterClass.SHAMAN, Builder.keystone(Dungeon.HALLS_OF_ATONEMENT, 15), Arrays.asList(Role.HEALER, Role.DPS));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            s.playerList.add(p);
        }
        //Player2
        {
            Character c1 = Builder.character("Player2Character1", CharacterClass.WARLOCK, Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 15), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Player2Character3", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.HALLS_OF_ATONEMENT, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            s.playerList.add(p);
        }
        //Player3
        {
            Character c1 = Builder.character("Player3Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS, Role.TANK));
            Character c2 = Builder.character("Player3Character2", CharacterClass.PALADIN, Builder.keystone(Dungeon.PLAGUEFALL, 15), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player3", Arrays.asList(c1, c2));
            s.playerList.add(p);
        }
        //Player4
        {
            Character c1 = Builder.character("Player4Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Player4Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player4", Arrays.asList(c1, c2));
            s.playerList.add(p);
        }
        return s;
    }

    @Test
    public void dungeon_not_available() {
        Assert.assertEquals(
                "Expected no possible combinations",
                0,
                classUnderTest.getKeystoneGroups(getTestData(), Builder.keystone(Dungeon.SOLEAHS_GAMBIT, 15), null, null).size()
        );
    }

    @Test
    public void dungeon_too_low_level() {
        Assert.assertEquals(
                "Expected no possible combinations",
                0,
                classUnderTest.getKeystoneGroups(getTestData(), Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 30), null, null).size()
        );
    }

    @Test
    public void dungeon_available_no_armor_stack() {
        Assert.assertEquals(
                "Expected no possible combinations",
                0,
                classUnderTest.getKeystoneGroups(getTestData(), Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 15), ArmourClass.PLATE, null).size()
        );
    }

    @Test
    public void dungeon_available() {
        Assert.assertEquals(
                "Incorrect number of possible combinations",
                8,
                classUnderTest.getKeystoneGroups(getTestData(), Builder.keystone(Dungeon.HALLS_OF_ATONEMENT, 15), null, null).size()
        );
    }
}