package com.psyala.util;


import com.psyala.pojo.Character;
import com.psyala.pojo.Dungeon;
import com.psyala.pojo.Player;
import com.psyala.pojo.PlayerCharacterPair;
import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.pojo.characterists.Role;
import org.junit.Assert;
import org.junit.Test;
import test_util.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DpsCombinationGeneratorTest {

    @Test
    public void test_single_combination() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Player1Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Player2Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                1,
                DpsCombinationGenerator.generate(data).size()
        );
    }

    @Test
    public void test_two_combinations() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Player1Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                2,
                DpsCombinationGenerator.generate(data).size()
        );
    }

    @Test
    public void test_three_combinations() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Character c3 = Builder.character("Character3", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2, c3));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                3,
                DpsCombinationGenerator.generate(data).size()
        );
    }


    @Test
    public void test_four_combinations() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Character c2 = Builder.character("Character2", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                4,
                DpsCombinationGenerator.generate(data).size()
        );
    }

    @Test
    public void test_large_combinations() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.WARLOCK, Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 15), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player3
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS, Role.TANK));
            Character c2 = Builder.character("Character2", CharacterClass.PALADIN, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player3", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player4
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player4", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                18,
                DpsCombinationGenerator.generate(data).size()
        );
    }

    @Test
    public void test_large_combinations_2() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.WARLOCK, Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 15), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player3
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS, Role.TANK));
            Character c2 = Builder.character("Character2", CharacterClass.PALADIN, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player3", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player4
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player4", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player5
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player5", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                32,
                DpsCombinationGenerator.generate(data).size()
        );
    }

    @Test
    public void test_large_combinations_3() {
        List<PlayerCharacterPair> data = new ArrayList<>();
        //Player1
        {
            Character c1 = Builder.character("Character1", CharacterClass.PRIEST, Builder.keystone(Dungeon.DE_OTHER_SIDE, 15), Arrays.asList(Role.HEALER));
            Player p = Builder.player("Player1", Arrays.asList(c1));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player2
        {
            Character c1 = Builder.character("Character1", CharacterClass.WARLOCK, Builder.keystone(Dungeon.MISTS_OF_TIRNA_SCITHE, 15), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.THEATRE_OF_PAIN, 15), Arrays.asList(Role.TANK, Role.DPS));
            Player p = Builder.player("Player2", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player3
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS, Role.TANK));
            Character c2 = Builder.character("Character2", CharacterClass.PALADIN, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS));
            Character c3 = Builder.character("Character3", CharacterClass.PALADIN, Builder.keystone(Dungeon.STREETS_OF_WONDER, 15), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player3", Arrays.asList(c1, c2, c3));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player4
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player4", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }
        //Player5
        {
            Character c1 = Builder.character("Character1", CharacterClass.DEMON_HUNTER, Builder.keystone(Dungeon.PLAGUEFALL, 17), Arrays.asList(Role.DPS));
            Character c2 = Builder.character("Character2", CharacterClass.HUNTER, Builder.keystone(Dungeon.SANGUINE_DEPTHS, 20), Arrays.asList(Role.DPS));
            Player p = Builder.player("Player5", Arrays.asList(c1, c2));
            p.characterList.forEach(c -> data.add(new PlayerCharacterPair(p, c)));
        }

        Assert.assertEquals(
                "Incorrect number of combinations",
                39,
                DpsCombinationGenerator.generate(data).size()
        );
    }

}