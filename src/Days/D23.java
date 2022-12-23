package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D23 {

    boolean print = false;

    public D23() throws IOException {

        List<String> file = Util.readInput(23, false);
        boolean p2 = true;
        int rounds = 10;
        if (p2) {
            rounds = Integer.MAX_VALUE;
        }
        Set<Point> current = new HashSet<>();

        for (int y = 0; y < file.size(); ++y) {
            for (int x = 0; x < file.get(y).length(); ++x) {
                if (file.get(y).charAt(x) == '#') {
                    current.add(new Point(x, y));
                }
            }
        }
        print(current);

        for (int r = 0; r < rounds; ++r) {
            Map<Point, Point> proposed = new HashMap<>();
            Map<Point, Boolean> notOccupied = new HashMap<>();
            Set<Point> next = new HashSet<>();
            boolean noElfMoved = true;
            for (Point p : current) {
                List<Point[]> toCheck = new ArrayList<>();
                List<Point> toMove = new ArrayList<>();

                toCheck.add(new Point[]{
                        new Point(p.x, p.y - 1),
                        new Point(p.x + 1, p.y - 1),
                        new Point(p.x - 1, p.y - 1),
                });
                toMove.add(new Point(p.x, p.y - 1));

                toCheck.add(new Point[]{
                        new Point(p.x, p.y + 1),
                        new Point(p.x + 1, p.y + 1),
                        new Point(p.x - 1, p.y + 1),
                });
                toMove.add(new Point(p.x, p.y + 1));

                toCheck.add(new Point[]{
                        new Point(p.x - 1, p.y),
                        new Point(p.x - 1, p.y + 1),
                        new Point(p.x - 1, p.y + -1),
                });
                toMove.add(new Point(p.x - 1, p.y));

                toCheck.add(new Point[]{
                        new Point(p.x + 1, p.y),
                        new Point(p.x + 1, p.y + 1),
                        new Point(p.x + 1, p.y + -1),
                });
                toMove.add(new Point(p.x + 1, p.y));

                boolean clear = true;
                proposed.put(p, p);
                for (Point[] points : toCheck) {
                    for (Point other : points) {
                        if (current.contains(other)) {
                            clear = false;
                            break;
                        }
                    }
                    if (!clear) {
                        break;
                    }
                }

                if (clear) {
                    continue;
                }
                noElfMoved = false;

                for (int checkInd = 0; checkInd < toCheck.size(); ++checkInd) {
                    clear = true;
                    int toCheckInd = ((r % 4) + checkInd) % toCheck.size();
                    for (Point other : toCheck.get(toCheckInd)) {
                        if (current.contains(other)) {
                            clear = false;
                            break;
                        }
                    }
                    if (clear) {
                        Point dest = toMove.get(toCheckInd);
                        proposed.put(p, dest);
                        notOccupied.compute(dest, (k, v) -> v == null);
                        break;
                    }
                }
            }

            if (noElfMoved) {
                System.out.println("Elves stopped moving at round " + (r + 1));
                break;
            }

            for (var item : proposed.entrySet()) {
                if (notOccupied.getOrDefault(item.getValue(), true)) {
                    next.add(item.getValue());
                } else {
                    next.add(item.getKey());
                }
            }
            current = next;
            print(current);
        }


        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : current) {
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
            if (p.y < minY) {
                minY = p.y;
            }
        }

        System.out.println(((maxX - minX + 1) * (maxY - minY + 1)) - current.size());
    }

    private void print(Set<Point> points) {
        if (!print) {
            return;
        }
        System.out.println();
        for (int y = -2; y < 10; ++y) {
            for (int x = -3; x < 11; ++x) {
                if (points.contains(new Point(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private record Point(int x, int y) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
