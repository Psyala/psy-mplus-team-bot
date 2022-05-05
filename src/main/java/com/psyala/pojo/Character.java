package com.psyala.pojo;

import com.psyala.pojo.characterists.CharacterClass;
import com.psyala.pojo.characterists.Role;

import java.util.List;

public class Character {
    public String name;
    public CharacterClass characterClass;
    public Keystone currentKeystone;
    public List<Role> playableRoles;

    public List<Role> getPlayableRoles() {
        if (playableRoles == null || playableRoles.isEmpty()) return characterClass.getPossibleRoles();
        return playableRoles;
    }
}
