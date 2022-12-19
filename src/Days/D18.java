package Days;

import Util.Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class D18 {


    public D18() throws IOException {

        List<String> file = Util.readInput(18, true);
        Map<Cord, Integer> notConnectedCount = new HashMap<>();

        final Set<Cord> cords = file.stream().reduce(
                new HashSet<>(),
                (acc, a) -> {
                    int[] cord = Arrays.stream(a.split(","))
                            .mapMultiToInt((str, c) -> c.accept(Integer.parseInt(str)))
                            .toArray();

                    acc.add(new Cord(cord[0], cord[1], cord[2]));
                    return acc;
                },
                (a, b) -> {
                    a.addAll(b);
                    return a;
                }
        );

        int sum = 0;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        int maxz = Integer.MIN_VALUE;

        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int minz = Integer.MAX_VALUE;

        for (final Cord c : cords) {
            if (c.x > maxx) {
                maxx = c.x;
            }

            if (c.x < minx) {
                minx = c.x;
            }

            if (c.y > maxy) {
                maxy = c.y;
            }

            if (c.y < miny) {
                miny = c.y;
            }

            if (c.z > maxz) {
                maxz = c.z;
            }

            if (c.z < minz) {
                minz = c.z;
            }

            int notConnected = 0;
            Cord[] toCheck = new Cord[]{
                    new Cord(c.x - 1, c.y, c.z),
                    new Cord(c.x + 1, c.y, c.z),

                    new Cord(c.x, c.y - 1, c.z),
                    new Cord(c.x, c.y + 1, c.z),

                    new Cord(c.x, c.y, c.z - 1),
                    new Cord(c.x, c.y, c.z + 1),
            };
            int cordNotConnectedCount = 0;
            for (final Cord check : toCheck) {
                if (!cords.contains(check)) {
                    ++cordNotConnectedCount;
                }
            }
            sum += cordNotConnectedCount;
            notConnectedCount.put(c, cordNotConnectedCount);
        }

        System.out.println(sum);

        int outsideSum = 0;
        for (final Cord c : cords) {
            // ----- x+
            int wallCount = 0;
            int point = c.x + 1;

            while (point <= maxx) {
                Cord move = new Cord(point, c.y, c.z);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                ++point;
            }

            // ----- x-
            point = c.x - 1;
            while (point >= minx) {
                Cord move = new Cord(point, c.y, c.z);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                --point;
            }

            // ----- y+
            point = c.y + 1;

            while (point <= maxy) {
                Cord move = new Cord(c.x, point, c.z);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                ++point;
            }

            // ----- y-
            point = c.y - 1;

            while (point >= miny) {
                Cord move = new Cord(c.x, point, c.z);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                --point;
            }

            // ----- z+
            point = c.z + 1;

            while (point <= maxz) {
                Cord move = new Cord(c.x, c.y, point);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                ++point;
            }

            // ----- z-
            point = c.z - 1;
            while (point >= minz) {
                Cord move = new Cord(c.x, c.y, point);
                if (cords.contains(move)) {
                    ++wallCount;
                    break;
                }
                --point;
            }
            if (wallCount != 6) {
                outsideSum += notConnectedCount.get(c);
            }
        }


        // 1698 too low
        System.out.println(outsideSum);
    }

    public void test2d() throws IOException {

        Path path = Path.of("src/Data/d18test2d.txt");
        List<String> file = Files.readAllLines(path, StandardCharsets.UTF_8);

        Set<Cord2> cords = file.stream().reduce(
                new HashSet<>(),
                (acc, a) -> {
                    int[] cord = Arrays.stream(a.split(","))
                            .mapMultiToInt((str, c) -> c.accept(Integer.parseInt(str)))
                            .toArray();

                    acc.add(new Cord2(cord[0], cord[1]));
                    return acc;
                },
                (a, b) -> {
                    a.addAll(b);
                    return a;
                }
        );

        int sum = 0;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;


        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;

        for (final Cord2 c : cords) {

            if (c.x > maxx) {
                maxx = c.x;
            }

            if (c.x < minx) {
                minx = c.x;
            }

            if (c.y > maxy) {
                maxy = c.y;
            }

            if (c.y < miny) {
                miny = c.y;
            }

            int notConnected = 0;
            Cord2[] toCheck = new Cord2[]{
                    new Cord2(c.x - 1, c.y),
                    new Cord2(c.x + 1, c.y),

                    new Cord2(c.x, c.y - 1),
                    new Cord2(c.x, c.y + 1),
            };

            for (final Cord2 check : toCheck) {
                if (!cords.contains(check)) {
                    ++notConnected;
                }
            }
            sum += notConnected;
        }

        System.out.println(sum);

        List<Cord2> outside = new ArrayList<>();
        int outsideSum = 0;
        for (final Cord2 c : cords) {
            // ----- x+
            int point = c.x + 1;
            boolean isOut = true;
            while (point <= maxx) {
                Cord2 move = new Cord2(point, c.y);
                if (cords.contains(move)) {
                    isOut = false;
                    break;
                }
                ++point;
            }
            if (isOut) {
                outside.add(c);
                ++outsideSum;
            }
            // ----- x-
            point = c.x - 1;
            isOut = true;
            while (point >= minx) {
                Cord2 move = new Cord2(point, c.y);
                if (cords.contains(move)) {
                    isOut = false;
                    break;
                }
                --point;
            }
            if (isOut) {
                outside.add(c);
                ++outsideSum;
            }

            // ----- y+
            point = c.y + 1;
            isOut = true;
            while (point <= maxy) {
                Cord2 move = new Cord2(c.x, point);
                if (cords.contains(move)) {
                    isOut = false;
                    break;
                }
                ++point;
            }
            if (isOut) {
                outside.add(c);
                ++outsideSum;
            }

            // ----- y-
            point = c.y - 1;
            isOut = true;
            while (point >= miny) {
                Cord2 move = new Cord2(c.x, point);
                if (cords.contains(move)) {
                    isOut = false;
                    break;
                }
                --point;
            }
            if (isOut) {
                outside.add(c);
                ++outsideSum;
            }
        }
        System.out.println(outsideSum);
    }

    private static class Cord {

        int x;
        int y;
        int z;

        public Cord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (!(obj instanceof Cord)) {
                return false;
            } else {
                return this.x == ((Cord) obj).x && this.y == ((Cord) obj).y && this.z == ((Cord) obj).z;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y, this.z);
        }

        @Override
        public String toString() {
            return this.x + "," + this.y + "," + this.z;
        }

    }

    private static class Cord2 {

        int x;
        int y;

        public Cord2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (!(obj instanceof Cord2)) {
                return false;
            } else {
                return this.x == ((Cord2) obj).x && this.y == ((Cord2) obj).y;
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

