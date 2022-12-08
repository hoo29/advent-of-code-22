package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D8 {

    public D8() throws IOException {

        List<String> file = Util.readInput(8, false);

        List<List<Integer>> forest = new ArrayList<>();
        for (String line : file) {
            List<Integer> row = line.chars().map(Character::getNumericValue).boxed().toList();
            forest.add(row);
        }

        int sum = 0;
        for (int i = 1; i < forest.size() - 1; ++i) {
            for (int j = 1; j < forest.get(i).size() - 1; ++j) {
                if (p1(forest, i, j)) {
                    ++sum;
                }
            }
        }

        sum += (4 * forest.size());
        sum -= 4;
        System.out.println(sum);

        List<Integer> totals = new ArrayList<>();
        for (int i = 1; i < forest.size() - 1; ++i) {
            for (int j = 1; j < forest.get(i).size() - 1; ++j) {
                totals.add(p2(forest, i, j));
            }
        }

        System.out.println(Collections.max(totals));
    }

    private boolean p1(List<List<Integer>> forest, int i, int j) {

        Integer val = forest.get(i).get(j);

        // check top
        List<Integer> cur = new ArrayList<>();
        for (int x = i - 1; x >= 0; --x) {
            cur.add(forest.get(x).get(j));
        }

        if (val > Collections.max(cur)) {
            return true;
        }
        cur.clear();

        // check bottom
        cur = new ArrayList<>();
        for (int x = i + 1; x < forest.size(); ++x) {
            cur.add(forest.get(x).get(j));
        }

        if (val > Collections.max(cur)) {
            return true;
        }
        cur.clear();

        // check right
        cur = new ArrayList<>();
        for (int y = j + 1; y < forest.get(i).size(); ++y) {
            cur.add(forest.get(i).get(y));
        }

        if (val > Collections.max(cur)) {
            return true;
        }
        cur.clear();

        // check left
        cur = new ArrayList<>();
        for (int y = j - 1; y >= 0; --y) {
            cur.add(forest.get(i).get(y));
        }

        if (val > Collections.max(cur)) {
            return true;
        }
        cur.clear();

        return false;
    }

    private int p2(List<List<Integer>> forest, int i, int j) {

        Integer val = forest.get(i).get(j);

        // check top
        int sumT = 0;
        for (int x = i - 1; x >= 0; --x) {
            if (val > forest.get(x).get(j)) {
                ++sumT;
            } else {
                ++sumT;
                break;
            }

        }

        // check bottom
        int sumB = 0;
        for (int x = i + 1; x < forest.size(); ++x) {
            if (val > forest.get(x).get(j)) {
                ++sumB;
            } else {
                ++sumB;
                break;
            }

        }

        // check right
        int sumR = 0;
        for (int y = j + 1; y < forest.get(i).size(); ++y) {
            if (val > forest.get(i).get(y)) {
                ++sumR;
            } else {
                ++sumR;
                break;
            }
        }

        // check left
        int sumL = 0;
        for (int y = j - 1; y >= 0; --y) {
            if (val > forest.get(i).get(y)) {
                ++sumL;
            } else {
                ++sumL;
                break;
            }
        }

        return sumT * sumB * sumL * sumR;
    }

}
