package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D18 {

    public D18() throws IOException {

        List<String> file = Util.readInput(18, false);

        final Set<Cord> droplet = file.stream().reduce(
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
                });

        int sum = 0;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        int maxz = Integer.MIN_VALUE;

        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int minz = Integer.MAX_VALUE;

        for (final Cord c : droplet) {
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
                if (!droplet.contains(check)) {
                    ++cordNotConnectedCount;
                }
            }
            sum += cordNotConnectedCount;
        }

        System.out.println(sum);

        Stack<Cord> waterToGrow = new Stack<>();
        Set<Cord> allWater = new HashSet<>();

        Cord waterSeed = new Cord(minx - 1, miny - 1, minz - 1);
        waterToGrow.add(waterSeed);
        allWater.add(waterSeed);

        int outsideSum = 0;
        while (waterToGrow.size() > 0) {
            Cord water = waterToGrow.pop();
            Cord[] toCheck = new Cord[]{
                    new Cord(water.x - 1, water.y, water.z),
                    new Cord(water.x + 1, water.y, water.z),

                    new Cord(water.x, water.y - 1, water.z),
                    new Cord(water.x, water.y + 1, water.z),

                    new Cord(water.x, water.y, water.z - 1),
                    new Cord(water.x, water.y, water.z + 1),
            };
            for (final Cord check : toCheck) {

                if (allWater.contains(check)) {
                    continue;
                }

                if (check.x >= (maxx + 2) || check.x <= (minx - 2) || check.y >= (maxy + 2) || check.y <= (miny - 2)
                        || check.z >= (maxz + 2) || check.z <= (minz - 2)) {
                    continue;
                }

                if (droplet.contains(check)) {
                    ++outsideSum;
                } else {
                    allWater.add(check);
                    waterToGrow.add(check);
                }

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
}
