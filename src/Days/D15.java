package Days;

import Util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class D15 {

    public D15() throws IOException {

        boolean test = false;

        int rowToCheck;
        int bound;
        if (test) {
            rowToCheck = 10;
            bound = 20;
        } else {
            rowToCheck = 2000000;
            bound = 4000000;
        }

        List<String> file = Util.readInput(15, test);
        Map<Point, String> tunnels = new HashMap<>();

        Set<Integer> thingsInRow = new HashSet<>();
        Set<Point> beaconsInRow = new HashSet<>();
        for (String line : file) {
            String sensorRaw = line.split(":")[0];
            String beaconRaw = line.split(":")[1];

            Point sensor = new Point(sensorRaw);
            Point beacon = new Point(beaconRaw);

            if (sensor.y == rowToCheck) {
                thingsInRow.add(sensor.x);
            }

            if (beacon.y == rowToCheck) {
                beaconsInRow.add(beacon);
            }

            tunnels.put(sensor, "S");
            tunnels.put(beacon, "B");

            int dist = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
            int width = dist - Math.abs(sensor.y - rowToCheck);
            if (width > 0) {
                for (int i = sensor.x - width; i <= sensor.x + width; ++i) {
                    thingsInRow.add(i);
                }
            }
        }
        System.out.println(thingsInRow.size() - beaconsInRow.size());

        int row = 0;
        List<Point[]> things = new ArrayList<>();

        for (String line : file) {
            String sensorRaw = line.split(":")[0];
            String beaconRaw = line.split(":")[1];

            Point sensor = new Point(sensorRaw);
            Point beacon = new Point(beaconRaw);

            things.add(new Point[] { sensor, beacon });
        }

        for (; row <= bound; ++row) {
            List<int[]> ranges = new ArrayList<>();
            for (Point[] points : things) {
                Point sensor = points[0];
                Point beacon = points[1];

                if (sensor.y == row) {
                    ranges.add(new int[] { sensor.x, sensor.x });
                }

                if (beacon.y == row) {
                    ranges.add(new int[] { beacon.x, beacon.x });
                }

                int dist = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
                int width = dist - Math.abs(sensor.y - row);
                if (width > 0) {
                    ranges.add(new int[] { sensor.x - width, sensor.x + width });
                }
            }

            ranges.sort(Comparator.comparingInt(a -> a[0]));

            int max = ranges.get(0)[1];
            boolean gap = false;
            for (int i = 1; i < ranges.size(); ++i) {
                if (ranges.get(i)[0] > max) {
                    int x = max + 1;
                    BigInteger why = BigInteger.valueOf(x).multiply(BigInteger.valueOf(4000000))
                            .add(BigInteger.valueOf(row));
                    System.out.println("found gap y " + row + " x " + max + 1);
                    System.out.println("frequency " + why);
                    gap = true;
                    break;
                }

                if (ranges.get(i)[1] > max) {
                    max = ranges.get(i)[1];
                }
            }
            if (gap) {
                break;
            }
        }

    }

    private static class Point {

        public int x;
        public int y;

        public Point(String cords) {
            cords = cords.substring(cords.indexOf("=") - 1);
            String[] parts = cords.strip().split(",");
            this.x = Integer.parseInt(parts[0].substring(parts[0].indexOf("=") + 1));
            this.y = Integer.parseInt(parts[1].substring(parts[1].indexOf("=") + 1));
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
