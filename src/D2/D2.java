package D2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import Util.Util;

public class D2 {

    private final Integer[][] scores = {
            { 3, 0, 6 },
            { 6, 3, 0 },
            { 0, 6, 3 }
    };

    public D2() throws IOException {

        List<String> file = Util.readInput(2, false);

        int score = 0;
        for (String line : file) {
            String[] vals = line.split(" ");
            score += myShapeScore(vals[1]);
            score += roundScore(vals[1], vals[0]);
        }

        System.out.println(score);

        score = 0;
        for (String line : file) {
            // X means you need to lose, Y means you need to end the round in a draw, and Z
            // means you need to win
            String[] vals = line.split(" ");
            String myPlay = whatShouldIPlay(vals[1], vals[0]);
            score += myShapeScore(myPlay);
            score += roundScore(myPlay, vals[0]);
        }
        System.out.println(score);

    }

    private String whatShouldIPlay(String outcome, String theirChoice) {
        List<Integer> row = Arrays.asList(scores[choiceIndex(theirChoice)]);
        int desired = "X".equals(outcome)
                ? 6
                : "Y".equals(outcome) ? 3 : 0;
        String[] choices = { "X", "Y", "Z" };
        return choices[row.indexOf(desired)];
    }

    private int choiceIndex(String choice) {
        switch (choice) {
            case "X":
            case "A":
                return 0;
            case "B":
            case "Y":
                return 1;
            default:
                return 2;
        }
    }

    private int myShapeScore(String choice) {

        if (choice.equals("X")) {
            return 1;
        }

        if (choice.equals("Y")) {
            return 2;
        }

        return 3;
    }

    private int roundScore(String myChoice, String theirChoice) {
        return this.scores[choiceIndex(myChoice)][choiceIndex(theirChoice)];
    }
}
