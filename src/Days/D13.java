package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

import javax.management.RuntimeErrorException;

public class D13 {

    public D13() throws IOException {
        List<String> file = Util.readInput(13, false);

        int sum = 0;
        int pair = 1;
        for (int i = 0; i < file.size(); i += 3) {
            Packet p1 = new Packet(file.get(i));
            Packet p2 = new Packet(file.get(i + 1));

            if (compare(p1.data, p2.data) == 1) {
                sum += pair;
            }
            ++pair;
        }

        // 5836 too low
        System.out.println(sum);
    }

    public int compare(Deque<Object> left, Deque<Object> right) {

        if (left.size() == 0 && right.size() == 0) {
            return 0;
        }

        if (left.size() == 0) {
            return 1;
        }

        if (right.size() == 0 && left.size() != 0) {
            return -1;
        }

        Object le = left.removeFirst();
        Object re = right.removeFirst();

        if (le instanceof Integer && re instanceof Integer) {
            if ((Integer) le < (Integer) re) {
                return 1;
            } else if ((Integer) le > (Integer) re) {
                return -1;
            }
        } else if (le instanceof Deque && re instanceof Deque) {
            int res = compare((Deque) le, (Deque) re);
            if (res != 0) {
                return res;
            }
        } else {
            Deque<Integer> singleList = new ArrayDeque<>();
            if (le instanceof Integer) {
                singleList.add((Integer) le);
                left.addFirst(singleList);
                right.addFirst(re);
            } else if (re instanceof Integer) {
                singleList.add((Integer) re);
                left.addFirst(le);
                right.addFirst(singleList);
            } else {
                throw new RuntimeException("idk");
            }
        }

        return compare(left, right);
    }

    private static class Packet {
        Deque<Object> data;

        public Packet(String line) {
            data = new ArrayDeque<>();
            Stack<Deque<Object>> stack = new Stack<>();
            for (char c : line.toCharArray()) {
                if (c == '[') {
                    stack.push(new ArrayDeque<>());
                } else if (c == ']') {
                    Deque<Object> child = stack.pop();
                    if (stack.empty()) {
                        data = child;
                    } else {
                        stack.peek().add(child);
                    }
                } else if (Character.isDigit(c)) {
                    // Deque<Integer> singleList = new ArrayDeque<>();
                    // singleList.add(Character.getNumericValue(c));
                    // stack.peek().add(singleList);
                    stack.peek().add(Character.getNumericValue(c));
                }
            }
        }
    }
}
