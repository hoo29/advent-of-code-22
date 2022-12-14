package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class D13 {

    public D13() throws IOException {
        List<String> file = Util.readInput(13, false);
        List<Packet> all = new ArrayList<>();
        int sum = 0;
        int pair = 1;
        for (int i = 0; i < file.size(); i += 3) {
            Packet p1 = new Packet(file.get(i));
            Packet p2 = new Packet(file.get(i + 1));
            all.add(p1);
            all.add(p2);

            if (compare(p1.data, p2.data) == 1) {
                sum += pair;
            }
            ++pair;
        }
        System.out.println(sum);

        Packet div1 = new Packet("[[2]]");
        Packet div2 = new Packet("[[6]]");
        all.add(div1);
        all.add(div2);

        all.sort((a, b) -> compare(b.data, a.data));

        int div1Ind = all.indexOf(div1) + 1;
        int div2Ind = all.indexOf(div2) + 1;
        System.out.println(div1Ind * div2Ind);
    }

    public int compare(List<Data> left, List<Data> right) {

        if (left.size() == 0 && right.size() != 0) {
            return 1;
        }

        if (right.size() == 0 && left.size() != 0) {
            return -1;
        }

        if (left.size() == 0) {
            return 0;
        }

        Data le = left.get(0);
        Data re = right.get(0);

        if (le.val != null && re.val != null) {
            if (le.val < re.val) {
                return 1;
            } else if (le.val > re.val) {
                return -1;
            }
        } else if (le.list != null && re.list != null) {
            int res = compare(le.list, re.list);
            if (res != 0) {
                return res;
            }
        } else if (le.val != null || re.val != null) {
            List<Data> singleList = new ArrayList<>();
            if (le.val != null) {
                singleList.add(le);
                Data d = new Data();
                d.list = singleList;
                List<Data> newLeft = new ArrayList<>(left.subList(1, left.size()));
                newLeft.add(0, d);
                return compare(newLeft, right);
            } else {
                singleList.add(re);
                Data d = new Data();
                d.list = singleList;
                List<Data> newRight = new ArrayList<>(right.subList(1, right.size()));
                newRight.add(0, d);
                return compare(left, newRight);
            }
        } else {
            throw new RuntimeException("idk");
        }


        return compare(left.subList(1, left.size()), right.subList(1, right.size()));
    }

    private static class Packet {
        List<Data> data;
        String line;

        public Packet(String line) {
            this.line = line;
            data = new ArrayList<>();
            Stack<List<Data>> stack = new Stack<>();
            for (int i = 0; i < line.length(); ) {
                char c = line.charAt(i);
                if (c == '[') {
                    stack.push(new ArrayList<>());
                    ++i;
                } else if (c == ']') {
                    List<Data> child = stack.pop();
                    if (stack.empty()) {
                        data = child;
                    } else {
                        Data d = new Data();
                        d.list = child;
                        stack.peek().add(d);
                    }
                    ++i;
                } else if (c == ',') {
                    ++i;
                } else {
                    Data d = new Data();
                    int num = 0;
                    do {
                        num *= 10;
                        num += Character.getNumericValue(c);
                        ++i;
                        c = line.charAt(i);
                    } while (Character.isDigit(c));
                    d.val = num;
                    stack.peek().add(d);
                }
            }
        }

        @Override
        public String toString() {
            return line;
        }
    }

    private static class Data {

        Integer val = null;
        List<Data> list = null;

        public Data() {
        }
    }
}
