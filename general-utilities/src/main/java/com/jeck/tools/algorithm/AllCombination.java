package com.jeck.tools.algorithm;

import java.util.ArrayList;
import java.util.List;

public class AllCombination {

    public static void main(String[] args) {

        String[] words = {"happy", "new", "year", "2013"};


        List<String> rs = new ArrayList<>();
        combination(words, 0, "", rs);
        int cnt = 0;
        for (String r : rs) {
            cnt++;
            System.out.println(cnt + ":" + r);
        }

    }

    public static void combination(String[] words, int level, String com, List<String> result) {
        char[] chars = words[level].toCharArray();
        if (level == words.length - 1) {
            for (char aChar : chars) {
                String tmp = com + aChar;
                result.add(tmp);
            }
        } else {
            for (char aChar : chars) {
                String tmp = com + aChar;
                combination(words, level + 1, tmp, result);
            }
        }
    }


}
