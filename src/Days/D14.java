package Days;

import Util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D14 {

    public D14() throws IOException {
        List<String> file = Util.readInput(14, false);
        Map<Point, String> cave = new HashMap<>();
        int maxDepth = Integer.MIN_VALUE;
        for (String line : file) {
            Point last = null;
            for (String part : line.split("->")) {
                Point p = new Point(part);
                if (p.y > maxDepth) {
                    maxDepth = p.y;
                }
                cave.put(p, "#");
                if (last != null) {
                    Point join = new Point(p);
                    while (join.x > last.x) {
                        join = new Point(join.x - 1, join.y);
                        cave.put(join, "#");
                    }
                    while (join.x < last.x) {
                        join = new Point(join.x + 1, join.y);
                        cave.put(join, "#");
                    }
                    while (join.y > last.y) {
                        join = new Point(join.x, join.y - 1);
                        cave.put(join, "#");
                    }
                    while (join.y < last.y) {
                        join = new Point(join.x, join.y + 1);
                        cave.put(join, "#");
                    }
                }
                last = p;
            }
        }

        int floor = maxDepth + 2;
        int units = 0;
        Point sand = new Point(500, 0);
        while (true) {
            while (!cave.containsKey(sand) && sand.y < floor) {
                sand.y += 1;
            }

            if (sand.y == 0) {
                break;
            }
            sand.y -= 1;

            Point bl = new Point(sand.x - 1, sand.y + 1);
            if (bl.y < floor && !cave.containsKey(bl)) {
                sand = bl;
                continue;
            }

            Point br = new Point(sand.x + 1, sand.y + 1);
            if (br.y < floor && !cave.containsKey(br)) {
                sand = br;
                continue;
            }

            cave.put(sand, "o");
            sand = new Point(500, 0);
            ++units;
        }

        System.out.println(units);
    }

    private static class Point {

        public int x;
        public int y;

        public Point(String cords) {
            String[] parts = cords.strip().split(",");
            this.x = Integer.parseInt(parts[0]);
            this.y = Integer.parseInt(parts[1]);
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point other) {
            this.x = other.x;
            this.y = other.y;
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
