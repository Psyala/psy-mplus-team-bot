package com.psyala.pojo;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    public List<Character> characterList = new ArrayList<>();

    public boolean containsCharacter(String characterName) {
        return characterList.stream()
                .anyMatch(character -> character.name.equalsIgnoreCase(characterName));
    }

    public Character getCharacter(String characterName) {
        return characterList.stream()
                .filter(character -> character.name.equalsIgnoreCase(characterName))
                .findFirst()
                .orElse(null);
    }
}
