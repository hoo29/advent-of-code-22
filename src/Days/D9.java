package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class D9 {

    public D9() throws IOException {
        boolean disablePrint = true;
        List<String> file = Util.readInput(9, false);

        Point h = new Point(0, 0);
        Point t = new Point(0, 0);
        Set<Point> visited = new HashSet<>();

        visited.add(new Point(t));

        for (String line : file) {
            String dir = line.split(" ")[0];
            int dist = Integer.parseInt(line.split(" ")[1]);
            for (int i = 0; i < dist; ++i) {
                if ("U".equals(dir)) {
                    h.y += 1;
                    if (!h.touching(t)) {
                        t.x = h.x;
                        t.y = h.y - 1;
                        visited.add(new Point(t));
                    }
                } else if ("D".equals(dir)) {
                    h.y -= 1;
                    if (!h.touching(t)) {
                        t.x = h.x;
                        t.y = h.y + 1;
                        visited.add(new Point(t));
                    }
                } else if ("L".equals(dir)) {
                    h.x -= 1;
                    if (!h.touching(t)) {
                        t.x = h.x + 1;
                        t.y = h.y;
                        visited.add(new Point(t));
                    }
                } else if ("R".equals(dir)) {
                    h.x += 1;
                    if (!h.touching(t)) {
                        t.x = h.x - 1;
                        t.y = h.y;
                        visited.add(new Point(t));
                    }
                }
            }

        }

        System.out.println(visited.size());

        h = new Point(0, 0);
        List<Point> tails = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            tails.add(new Point(0, 0));
        }

        visited = new HashSet<>();
        visited.add(new Point(t));

        for (String line : file) {
            String dir = line.split(" ")[0];
            int dist = Integer.parseInt(line.split(" ")[1]);
            for (int i = 0; i < dist; ++i) {
                if ("U".equals(dir)) {
                    h.y += 1;
                } else if ("D".equals(dir)) {
                    h.y -= 1;
                } else if ("L".equals(dir)) {
                    h.x -= 1;
                } else if ("R".equals(dir)) {
                    h.x += 1;
                }

                printGrid(h, tails, 6, disablePrint);
                checkTouching(h, tails.get(0), null);
                printGrid(h, tails, 6, disablePrint);
                for (int j = 1; j < tails.size(); ++j) {
                    checkTouching(tails.get(j - 1), tails.get(j),
                            j == tails.size() - 1 ? visited : null);
                    printGrid(h, tails, 6, disablePrint);
                }

                printGrid(h, tails, 6, disablePrint);
            }
        }

        System.out.println(visited.size());

    }

    private void checkTouching(Point a, Point b, Set<Point> visited) {
        if (a.touching(b)) {
            return;
        }

        if (a.x > b.x) {
            b.x += 1;
        } else {
            b.x -= 1;
        }

        if (a.y > b.y) {
            b.y += 1;
        } else {
            b.y -= 1;
        }

        if (visited != null) {
            visited.add(new Point(b));
        }

    }

    private void printGrid(Point h, List<Point> tails, int size, boolean disable) {

        if (disable) {
            return;
        }

        List<List<String>> grid = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            List<String> line = new ArrayList<>(Collections.nCopies(size, "."));
            grid.add(line);
        }

        grid.get(h.y).set(h.x, "H");

        for (int i = 0; i < tails.size(); ++i) {
            Point cur = tails.get(i);
            String item = grid.get(cur.y).get(cur.x);
            if (".".equals(item)) {
                grid.get(cur.y).set(cur.x, String.valueOf(i + 1));
            }
        }

        for (int i = grid.size() - 1; i >= 0; --i) {
            for (int j = 0; j < grid.get(i).size(); ++j) {
                if (j == grid.get(i).size() - 1) {
                    System.out.println(grid.get(i).get(j));
                } else {
                    System.out.print(grid.get(i).get(j));
                }
            }
        }
        System.out.println("");
    }

    private static class Point {

        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point other) {
            this.x = other.x;
            this.y = other.y;
        }

        public boolean touching(Point other) {
            return (Math.abs(this.x - other.x) <= 1) && (Math.abs(this.y - other.y) <= 1);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (!(obj instanceof Point)) {
                return false;
            } else {
                return this.x == ((Point) obj).x && this.y == ((Point) obj).y;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }

        @Override
        public String toString() {
            return this.x + "," + this.y;
        }

    }

}
