package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D24 {

    boolean print = false;

    public D24() throws IOException {

        List<String> file = Util.readInput(24, false);
        Set<BlizzardPoint> blizzard = new HashSet<>();
        for (int y = 1; y < file.size() - 1; ++y) {
            String line = file.get(y);
            for (int x = 1; x < line.length() - 1; ++x) {
                if (line.charAt(x) != '.') {
                    blizzard.add(new BlizzardPoint(new Point(x, y), line.charAt(x)));
                }
            }
        }


        Point start = new Point(1, 0);
        Point finish = new Point(file.get(file.size() - 1).length() - 2, file.size() - 1);
        GridInfo gi = new GridInfo(start, finish, file.size(), file.get(0).length());
        Map<Set<BlizzardPoint>, Set<BlizzardPoint>> blizzardCache = new HashMap<>();

        Path startPath = new Path(start, blizzard, 0);
        Path fin = nav(startPath, gi, blizzardCache);
        System.out.println(fin.minute);

        Path newFin = new Path(fin.elves, fin.blizzard, 0);
        Path fin2 = nav(newFin, new GridInfo(finish, start, gi.height, gi.width), blizzardCache);
        System.out.println(fin2.minute);

        Path newFin2 = new Path(fin2.elves, fin2.blizzard, 0);
        Path fin3 = nav(newFin2, new GridInfo(start, finish, gi.height, gi.width), blizzardCache);
        System.out.println(fin3.minute);

        System.out.println(fin.minute + fin2.minute + fin3.minute);
    }

    private Path nav(
            final Path start,
            final GridInfo gi,
            final Map<Set<BlizzardPoint>, Set<BlizzardPoint>> blizzardCache
    ) {
        Deque<Path> queue = new ArrayDeque<>();
        queue.addFirst(start);

        Set<Path> visited = new HashSet<>();
        visited.add(start);
        Path cur = start;
        while (!queue.isEmpty()) {
            cur = queue.removeLast();
            if (cur.elves.equals(gi.finish)) {
                break;
            }

            Set<BlizzardPoint> newBlizzard = nextBlizzard(cur.blizzard, blizzardCache, gi);
            List<Point> moves = nextMoves(cur.elves, gi, newBlizzard);
            for (Point move : moves) {
                Path newPath = new Path(move, newBlizzard, cur.minute + 1);
                if (!visited.contains(newPath)) {
                    visited.add(newPath);
                    queue.addFirst(newPath);
                }
            }
        }
        return cur;
    }

    private Set<BlizzardPoint> nextBlizzard(Set<BlizzardPoint> blizzard, Map<Set<BlizzardPoint>, Set<BlizzardPoint>> blizzardCache, GridInfo gi) {
        Set<BlizzardPoint> newBlizzard = blizzardCache.getOrDefault(blizzard, new HashSet<>());
        if (newBlizzard.isEmpty()) {
            for (BlizzardPoint b : blizzard) {
                if (b.dir == '<') {
                    if (b.pos.x > 1) {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x - 1, b.pos.y), b.dir));
                    } else {
                        newBlizzard.add(new BlizzardPoint(new Point(gi.width - 2, b.pos.y), b.dir));
                    }
                } else if (b.dir == '>') {
                    if (b.pos.x < gi.width - 2) {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x + 1, b.pos.y), b.dir));
                    } else {
                        newBlizzard.add(new BlizzardPoint(new Point(1, b.pos.y), b.dir));
                    }
                } else if (b.dir == '^') {
                    if (b.pos.y > 1) {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x, b.pos.y - 1), b.dir));
                    } else {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x, gi.height - 2), b.dir));
                    }
                } else if (b.dir == 'v') {
                    if (b.pos.y < gi.height - 2) {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x, b.pos.y + 1), b.dir));
                    } else {
                        newBlizzard.add(new BlizzardPoint(new Point(b.pos.x, 1), b.dir));
                    }
                }
            }
            blizzardCache.put(blizzard, newBlizzard);
        }

        return newBlizzard;
    }

    private List<Point> nextMoves(Point pos, GridInfo gi, Set<BlizzardPoint> newBlizzard) {

        List<Point> toMove = new ArrayList<>(3);

        // check S
        if (pos.y < gi.height - 2 || (pos.y == gi.finish.y - 1 && pos.x == gi.finish.x)) {
            BlizzardPoint[] pointsToCheck = new BlizzardPoint[]{
                    new BlizzardPoint(new Point(pos.x, pos.y + 1), '^'),
                    new BlizzardPoint(new Point(pos.x, pos.y + 1), '>'),
                    new BlizzardPoint(new Point(pos.x, pos.y + 1), 'v'),
                    new BlizzardPoint(new Point(pos.x, pos.y + 1), '<'),
            };
            boolean safe = true;
            for (BlizzardPoint c : pointsToCheck) {
                if (newBlizzard.contains(c)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                toMove.add(new Point(pos.x, pos.y + 1));
            }
        }

        // check E
        if (pos.x < gi.width - 2 && pos.y != 0 && pos.y != gi.height - 1) {
            BlizzardPoint[] pointsToCheck = new BlizzardPoint[]{
                    new BlizzardPoint(new Point(pos.x + 1, pos.y), '^'),
                    new BlizzardPoint(new Point(pos.x + 1, pos.y), '>'),
                    new BlizzardPoint(new Point(pos.x + 1, pos.y), 'v'),
                    new BlizzardPoint(new Point(pos.x + 1, pos.y), '<'),
            };
            boolean safe = true;
            for (BlizzardPoint c : pointsToCheck) {
                if (newBlizzard.contains(c)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                toMove.add(new Point(pos.x + 1, pos.y));
            }
        }

        // check N
        if (pos.y > 1 || (pos.y == (gi.finish.y + 1) && pos.x == gi.finish.x)) {
            BlizzardPoint[] pointsToCheck = new BlizzardPoint[]{
                    new BlizzardPoint(new Point(pos.x, pos.y - 1), '^'),
                    new BlizzardPoint(new Point(pos.x, pos.y - 1), '>'),
                    new BlizzardPoint(new Point(pos.x, pos.y - 1), 'v'),
                    new BlizzardPoint(new Point(pos.x, pos.y - 1), '<'),
            };
            boolean safe = true;
            for (BlizzardPoint c : pointsToCheck) {
                if (newBlizzard.contains(c)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                toMove.add(new Point(pos.x, pos.y - 1));
            }
        }

        // check W
        if (pos.x > 1 && pos.y != 0 && pos.y != gi.height - 1) {
            BlizzardPoint[] pointsToCheck = new BlizzardPoint[]{
                    new BlizzardPoint(new Point(pos.x - 1, pos.y), '^'),
                    new BlizzardPoint(new Point(pos.x - 1, pos.y), '>'),
                    new BlizzardPoint(new Point(pos.x - 1, pos.y), 'v'),
                    new BlizzardPoint(new Point(pos.x - 1, pos.y), '<'),
            };
            boolean safe = true;
            for (BlizzardPoint c : pointsToCheck) {
                if (newBlizzard.contains(c)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                toMove.add(new Point(pos.x - 1, pos.y));
            }
        }

        // check current

        BlizzardPoint[] pointsToCheck = new BlizzardPoint[]{
                new BlizzardPoint(new Point(pos.x, pos.y), '^'),
                new BlizzardPoint(new Point(pos.x, pos.y), '>'),
                new BlizzardPoint(new Point(pos.x, pos.y), 'v'),
                new BlizzardPoint(new Point(pos.x, pos.y), '<'),
        };
        boolean safe = true;
        for (BlizzardPoint c : pointsToCheck) {
            if (newBlizzard.contains(c)) {
                safe = false;
                break;
            }
        }
        if (safe) {
            toMove.add(pos);
        }

        return toMove;
    }

    private record Path(Point elves, Set<BlizzardPoint> blizzard, int minute) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Path path = (Path) o;

            if (minute != path.minute) return false;
            return Objects.equals(elves, path.elves);
        }

        @Override
        public int hashCode() {
            int result = elves != null ? elves.hashCode() : 0;
            result = 31 * result + minute;
            return result;
        }
    }

    private record GridInfo(Point start, Point finish, int height, int width) {
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

    private record BlizzardPoint(Point pos, char dir) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BlizzardPoint that = (BlizzardPoint) o;

            if (dir != that.dir) return false;
            return Objects.equals(pos, that.pos);
        }

        @Override
        public int hashCode() {
            int result = pos != null ? pos.hashCode() : 0;
            result = 31 * result + (int) dir;
            return result;
        }
    }
}
