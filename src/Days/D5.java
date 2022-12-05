package Days;

import Util.Util;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class D5 {

    public D5() throws IOException {

        List<String> file = Util.readInput(5, false);

        int stackInitInd = file.indexOf("");

        String[] stackCounts = file.get(stackInitInd - 1).strip().split(" ");
        int stackCount = Integer.parseInt(stackCounts[stackCounts.length - 1]);
        List<Stack<Character>> stacks = Stream.generate(Stack<Character>::new).limit(stackCount).toList();

        System.out.println(stackCount);
        for (int i = stackInitInd - 2; i >= 0; --i) {
            String line = file.get(i);
            for (int j = 0; j < line.length(); ++j) {
                if (Character.isLetter(line.charAt(j))) {
                    stacks.get((j - 1) / 4).push(line.charAt(j));
                }
            }
        }

        for (int i = stackInitInd + 1; i < file.size(); ++i) {
            String line = file.get(i);
            String parts[] = line.trim().split(" ");
            int count = Integer.parseInt(parts[1]);
            int src = Integer.parseInt(parts[3]) - 1;
            int dst = Integer.parseInt(parts[5]) - 1;
            String move = "";
            for (int j = 0; j < count; ++j) {
                move += stacks.get(src).pop();
            }

            for (int j = move.length() - 1; j >= 0; --j) {
                stacks.get(dst).add(move.charAt(j));
            }
        }

        String output = "";
        for (Stack s : stacks) {
            if (s.size() > 0) {
                output += s.pop();
            }
        }
        System.out.println(output);
    }
}
