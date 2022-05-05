package com.psyala.util;

import com.psyala.pojo.PlayerCharacterPair;

import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {
    public static List<List<PlayerCharacterPair>> generate(int n, int r, List<PlayerCharacterPair> playerCharacterPairList) {
        List<int[]> indexCombinations = new ArrayList<>();
        helper(indexCombinations, new int[r], 0, n - 1, 0);

        List<List<PlayerCharacterPair>> returnList = new ArrayList<>();
        indexCombinations.forEach(ints -> {
            List<PlayerCharacterPair> combination = new ArrayList<>();
            for (int i : ints) {
                combination.add(playerCharacterPairList.get(i));
            }
            returnList.add(combination);
        });

        return returnList;
    }

    private static void helper(List<int[]> combinations, int[] data, int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else {
            int max = Math.min(end, end + 1 - data.length + index);
            for (int i = start; i <= max; i++) {
                data[index] = i;
                helper(combinations, data, i + 1, end, index + 1);
            }
        }
    }
}
