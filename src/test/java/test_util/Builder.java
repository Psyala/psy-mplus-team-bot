package test_util;

import com.psyala.pojo.Character;
import com.psyala.pojo.*;
import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.pojo.characterists.Role;

import java.util.List;

public class Builder {

    public static Player player(String name, List<Character> characterList) {
        Player p = new Player();
        p.name = name;
        p.characterList = characterList;
        return p;
    }

    public static Character character(String name, CharacterClass characterClass, Keystone keystone, List<Role> playableRoles) {
        Character c = new Character();
        c.name = name;
        c.characterClass = characterClass;
        c.currentKeystone = keystone;
        c.playableRoles = playableRoles;
        return c;
    }

    public static Keystone keystone(Dungeon dungeon, int level) {
        Keystone k = new Keystone();
        k.dungeon = dungeon;
        k.level = level;
        return k;
    }

    public static Server server(int guildId, List<Player> playerList) {
        Server s = new Server();
        s.guildId = guildId;
        s.playerList = playerList;
        return s;
    }
}
