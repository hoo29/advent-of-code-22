package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D22 {

    boolean print = false;

    public D22() throws IOException {

        List<String> file = Util.readInput(22, false);

        List<List<String>> grid = new ArrayList<>();
        int maxWidth = Integer.MIN_VALUE;
        String rawRoute = "";
        for (int i = 0; i < file.size(); ++i) {
            String line = file.get(i);
            if (i < file.size() - 2) {
                List<String> row = new ArrayList<>(Arrays.asList(line.split("")));
                if (row.size() > maxWidth) {
                    maxWidth = row.size();
                }
                grid.add(row);
            }

            if (i == file.size() - 1) {
                rawRoute = line;
            }
        }

        for (List<String> row : grid) {
            while (row.size() != maxWidth) {
                row.add(" ");
            }
        }

        int ind = 0;
        Deque<Instr> route = new ArrayDeque<>();
        while (ind < rawRoute.length()) {
            int start = ind;
            while (ind < rawRoute.length() && Character.isDigit(rawRoute.charAt(ind))) {
                ++ind;
            }
            int count = ind >= rawRoute.length() ? Integer.parseInt(rawRoute.substring(start)) : Integer.parseInt(rawRoute.substring(start, ind));
            route.push(new Instr(count, ind >= rawRoute.length() ? "" : rawRoute.substring(ind, ind + 1)));
            ++ind;
        }

        Pos pos = new Pos(grid.get(0).indexOf("."), 0, "E");
        grid.get(pos.y).set(pos.x, facingString(pos.dir));
        printGrid(grid);
        while (!route.isEmpty()) {
            Instr instr = route.removeLast();
            pos = move(grid, instr, pos);
        }
        this.print = true;
        printGrid(grid);

        System.out.println(pos);
        ++pos.x;
        ++pos.y;

        System.out.println((1000 * pos.y) + (4 * pos.x) + facingValue(pos.dir));
    }

    private String facingString(String dir) {
        if ("N".equals(dir)) {
            return "^";
        } else if ("E".equals(dir)) {
            return ">";
        } else if ("S".equals(dir)) {
            return "v";
        } else {
            return "<";
        }
    }

    private int facingValue(String dir) {
        if ("N".equals(dir)) {
            return 3;
        } else if ("E".equals(dir)) {
            return 0;
        } else if ("S".equals(dir)) {
            return 1;
        } else {
            return 2;
        }
    }

    private Pos move(List<List<String>> grid, Instr instr, Pos pos) {
        int ind = 0;
        Pos finishPos = new Pos(pos);

        while (ind < instr.count) {
            Pos prevPos = new Pos(finishPos);
            finishPos = nextPos(grid, finishPos);
            if (grid.get(finishPos.y).get(finishPos.x).equals("#")) {
                finishPos = prevPos;
                break;
            } else {
                grid.get(finishPos.y).set(finishPos.x, facingString(finishPos.dir));
            }
            ++ind;
            printGrid(grid);
        }

        finishPos.dir = rotate(finishPos.dir, instr.dir.equals("R"), "".equals(instr.dir));
        grid.get(finishPos.y).set(finishPos.x, facingString(finishPos.dir));
        printGrid(grid);

        return finishPos;

    }

    private void printGrid(List<List<String>> grid) {
        if (!print) {
            return;
        }

        System.out.println("GRID PRINT START");
        System.out.println();
        for (List<String> row : grid) {
            for (String c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private Pos nextPos(List<List<String>> grid, Pos curPos) {
        Pos nextPos = new Pos(curPos);
        if ("N".equals(curPos.dir)) {
            if (curPos.y - 1 < 0 || grid.get(curPos.y - 1).get(curPos.x).equals(" ")) {
                int newY = grid.size() - 1;
                while (grid.get(newY).get(curPos.x).equals(" ")) {
                    --newY;
                }
                nextPos.y = newY;
            } else {
                --nextPos.y;
            }
        } else if ("E".equals(curPos.dir)) {
            if (curPos.x + 1 == grid.get(curPos.y).size() || grid.get(curPos.y).get(curPos.x + 1).equals(" ")) {
                int newX = 0;
                while (grid.get(curPos.y).get(newX).equals(" ")) {
                    newX++;
                }
                nextPos.x = newX;
            } else {
                ++nextPos.x;
            }
        } else if ("S".equals(curPos.dir)) {
            if (curPos.y + 1 == grid.size() || grid.get(curPos.y + 1).get(curPos.x).equals(" ")) {
                int newY = 0;
                while (grid.get(newY).get(curPos.x).equals(" ")) {
                    newY++;
                }
                nextPos.y = newY;
            } else {
                ++nextPos.y;
            }
        } else {
            if (curPos.x - 1 < 0 || grid.get(curPos.y).get(curPos.x - 1).equals(" ")) {
                int newX = grid.get(curPos.y).size() - 1;
                while (grid.get(curPos.y).get(newX).equals(" ")) {
                    --newX;
                }
                nextPos.x = newX;
            } else {
                --nextPos.x;
            }
        }

        return nextPos;
    }

    private String rotate(String curDir, boolean clockwise, boolean lastDir) {

        if (lastDir) {
            return curDir;
        }
        if ("N".equals(curDir)) {
            return clockwise ? "E" : "W";
        } else if ("E".equals(curDir)) {
            return clockwise ? "S" : "N";
        } else if ("S".equals(curDir)) {
            return clockwise ? "W" : "E";
        } else {
            return clockwise ? "N" : "S";
        }
    }

    private record Instr(int count, String dir) {
    }

    private static class Pos {

        int x;
        int y;
        String dir;

        public Pos(int x, int y, String dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        public Pos(Pos other) {
            this.x = other.x;
            this.y = other.y;
            this.dir = other.dir;
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    ", dir='" + dir + '\'' +
                    '}';
        }
    }
}
