package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D22pt2 {

    public D22pt2() throws IOException {

        List<String> file = Util.readInput(22, false);

        List<List<List<String>>> grids = new ArrayList<>();

        String rawRoute = "";
        int gridRow = 0;
        for (int i = 0; i < file.size(); ++i) {
            String line = file.get(i);
            if (i < file.size() - 2) {
                if (i % 50 == 0) {
                    gridRow = grids.size();
                }


                line = line.strip();
                for (int j = 0; j < line.length(); j += 50) {
                    String row = line.substring(j, j + 50);
                    int gridNumber = gridRow + (j / 50);
                    while (grids.size() < gridNumber + 1) {
                        grids.add(new ArrayList<>());
                    }
                    grids.get(gridNumber).add(new ArrayList<>(Arrays.asList(row.split(""))));
                }
            }

            if (i == file.size() - 1) {
                rawRoute = line;
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

        Pos pos = new Pos(0, 0, "E", 0);
        grids.get(pos.gridNumber).get(pos.y).set(pos.x, facingString(pos.dir));

        while (!route.isEmpty()) {
            Instr instr = route.removeLast();
            pos = move(grids, instr, pos);
        }

        System.out.println(pos);
        ++pos.x;
        ++pos.y;

        if (pos.gridNumber == 2) {
            pos.y += 50;
        } else if (pos.gridNumber == 3 || pos.gridNumber == 4) {
            pos.y += 100;
        } else if (pos.gridNumber == 5) {
            pos.y += 150;
        }

        if (pos.gridNumber == 0 || pos.gridNumber == 2 || pos.gridNumber == 4) {
            pos.x += 50;
        } else if (pos.gridNumber == 1) {
            pos.x += 100;
        }

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

    private Pos move(List<List<List<String>>> grids, Instr instr, Pos pos) {
        int ind = 0;
        Pos finishPos = new Pos(pos);

        while (ind < instr.count) {
            Pos prevPos = new Pos(finishPos);
            finishPos = nextPos(finishPos);
            if (grids.get(finishPos.gridNumber).get(finishPos.y).get(finishPos.x).equals("#")) {
                finishPos = prevPos;
                break;
            }
            ++ind;
        }

        finishPos.dir = rotate(finishPos.dir, instr.dir.equals("R"), "".equals(instr.dir));

        return finishPos;
    }


    private Pos nextPos(Pos curPos) {
        Pos nextPos = new Pos(curPos);
        if ("N".equals(curPos.dir)) {
            if (curPos.y - 1 < 0) {
                if (curPos.gridNumber == 0) {
                    nextPos.gridNumber = 5;
                    nextPos.x = 0;
                    nextPos.y = curPos.x;
                    nextPos.dir = "E";
                } else if (curPos.gridNumber == 1) {
                    nextPos.gridNumber = 5;
                    nextPos.y = 49;
                } else if (curPos.gridNumber == 2) {
                    nextPos.gridNumber = 0;
                    nextPos.y = 49;
                } else if (curPos.gridNumber == 3) {
                    nextPos.gridNumber = 2;
                    nextPos.x = 0;
                    nextPos.y = curPos.x;
                    nextPos.dir = "E";
                } else if (curPos.gridNumber == 4) {
                    nextPos.gridNumber = 2;
                    nextPos.y = 49;
                } else if (curPos.gridNumber == 5) {
                    nextPos.gridNumber = 3;
                    nextPos.y = 49;
                }
            } else {
                --nextPos.y;
            }
        } else if ("E".equals(curPos.dir)) {
            if (curPos.x + 1 == 50) {
                if (curPos.gridNumber == 0) {
                    nextPos.gridNumber = 1;
                    nextPos.x = 0;
                } else if (curPos.gridNumber == 1) {
                    nextPos.gridNumber = 4;
                    nextPos.x = 49;
                    nextPos.y = Math.abs(curPos.y - 49);
                    nextPos.dir = "W";
                } else if (curPos.gridNumber == 2) {
                    nextPos.gridNumber = 1;
                    nextPos.y = 49;
                    nextPos.x = curPos.y;
                    nextPos.dir = "N";
                } else if (curPos.gridNumber == 3) {
                    nextPos.gridNumber = 4;
                    nextPos.x = 0;
                } else if (curPos.gridNumber == 4) {
                    nextPos.gridNumber = 1;
                    nextPos.x = 49;
                    nextPos.y = Math.abs(curPos.y - 49);
                    nextPos.dir = "W";
                } else if (curPos.gridNumber == 5) {
                    nextPos.gridNumber = 4;
                    nextPos.x = curPos.y;
                    nextPos.y = 49;
                    nextPos.dir = "N";
                }
            } else {
                ++nextPos.x;
            }
        } else if ("S".equals(curPos.dir)) {
            if (curPos.y + 1 == 50) {
                if (curPos.gridNumber == 0) {
                    nextPos.gridNumber = 2;
                    nextPos.y = 0;
                } else if (curPos.gridNumber == 1) {
                    nextPos.gridNumber = 2;
                    nextPos.x = 49;
                    nextPos.y = curPos.x;
                    nextPos.dir = "W";
                } else if (curPos.gridNumber == 2) {
                    nextPos.gridNumber = 4;
                    nextPos.y = 0;
                } else if (curPos.gridNumber == 3) {
                    nextPos.gridNumber = 5;
                    nextPos.y = 0;
                } else if (curPos.gridNumber == 4) {
                    nextPos.gridNumber = 5;
                    nextPos.x = 49;
                    nextPos.y = curPos.x;
                    nextPos.dir = "W";
                } else if (curPos.gridNumber == 5) {
                    nextPos.gridNumber = 1;
                    nextPos.y = 0;
                }
            } else {
                ++nextPos.y;
            }
        } else if ("W".equals(curPos.dir)) {
            if (curPos.x - 1 < 0) {
                if (curPos.gridNumber == 0) {
                    nextPos.gridNumber = 3;
                    nextPos.x = 0;
                    nextPos.y = Math.abs(curPos.y - 49);
                    nextPos.dir = "E";
                } else if (curPos.gridNumber == 1) {
                    nextPos.gridNumber = 0;
                    nextPos.x = 49;
                } else if (curPos.gridNumber == 2) {
                    nextPos.gridNumber = 3;
                    nextPos.x = curPos.y;
                    nextPos.y = 0;
                    nextPos.dir = "S";
                } else if (curPos.gridNumber == 3) {
                    nextPos.gridNumber = 0;
                    nextPos.x = 0;
                    nextPos.y = Math.abs(curPos.y - 49);
                    nextPos.dir = "E";
                } else if (curPos.gridNumber == 4) {
                    nextPos.gridNumber = 3;
                    nextPos.x = 49;
                } else if (curPos.gridNumber == 5) {
                    nextPos.gridNumber = 0;
                    nextPos.x = curPos.y;
                    nextPos.y = 0;
                    nextPos.dir = "S";
                }
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
        int gridNumber;

        public Pos(int x, int y, String dir, int gridNumber) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.gridNumber = gridNumber;
        }

        public Pos(Pos other) {
            this.x = other.x;
            this.y = other.y;
            this.dir = other.dir;
            this.gridNumber = other.gridNumber;
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
