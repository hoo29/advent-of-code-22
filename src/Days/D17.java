package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D17 {

    private final boolean printing = false;

    public D17() throws IOException {
        List<String> file = Util.readInput(17, false);

        String[] jets = file.get(0).trim().split("");
        String[] shapes = new String[]{"-", "+", "⅃", "|", "#"};

        List<Rock> fallen = new ArrayList<>();
        Set<Coord> blocked = new HashSet<>();
        Map<Set<Coord>, Integer[]> patterns = new HashMap<>();
        int patternSize = 65;

        int patternOffset = 240;

        /*
        // pattern size 30 test data
        cycle every 210 rocks,
        at fallen rocks x
        minus buffer 240
        divide by cycle count 210
        times that by height gain per cycle of 318
        add on initial height before cycle starts of 369

        Need to go to fallen 1,000,000,000,000
        At fallen rocks 999,999,999,840
        minus buffer 240 = 999,999,999,600
        divide by cycle count 210 = 4,761,904,760
        times by height gain per cycle 318 = 1,514,285,713,680
        add on initial height before cycle starts of 369 = 1,514,285,714,049

        Now need an additional 160 rocks to fall

        400 rocks = 608 height
        minus before cycle value = 239

        1,514,285,714,288
        TEST DONE
        1,514,285,714,288
        -----

        // pattern size 45 my data
        cycle every 1755 rocks,
        at fallen rocks x
        minus buffer 240
        divide by cycle count 1755
        times that by height gain per cycle of 2,747
        add on initial height before cycle starts of 359

        Need to go to fallen 1,000,000,000,000
        At fallen rocks 999,999,998,835
        minus buffer 240 = 999,999,998,595
        divide by cycle count 1755 = 569,800,569
        times by height gain per cycle 2,747 = 1,565,242,163,043
        add on initial height before cycle starts of 359 = 1,565,242,163,402

        Now need an additional 1,165 rocks to fall (+ cycle)

        1405 rocks = 2158 height
        minus before cycle value = 1,799

        1,565,242,165,201
        */

        int width = 7;

        for (int i = 0; i < width; i++) {
            blocked.add(new Coord(i, 0));
        }

        int maxHeight = 0;

        int shapeInd = 0;
        int jetInd = 0;


        while (fallen.size() < 1405) {
            String shape = shapes[shapeInd];

            if (fallen.size() == patternOffset) {
                System.out.println("This height before pattern " + maxHeight);
            }
            if (fallen.size() >= (patternOffset + patternSize) && (fallen.size() % patternSize == 0)) {
                List<Rock> fallenBlock = fallen.subList(fallen.size() - patternSize, fallen.size());
                int minYForBlock = fallenBlock.stream().mapToInt(r -> r.miny).min().orElse(-1);
                Set<Coord> normalised = new HashSet<>();
                for (Rock r : fallenBlock) {
                    normalised.addAll(new Rock(
                            r.shape,
                            r.minx,
                            r.miny - Math.abs(1 - minYForBlock)
                    ).cords);
                }
                if (patterns.containsKey(normalised)) {
                    System.out.println("Found pattern at fallen " + fallen.size() + " " + maxHeight);
                    System.out.println("Matched " + patterns.get(normalised)[0] + " " + patterns.get(normalised)[1]);
                    System.out.println();
                } else {
                    patterns.put(normalised, new Integer[]{fallen.size(), maxHeight});
                }
            }

            Rock rock = new Rock(shape, 2, maxHeight + 4);
            ++shapeInd;
            if (shapeInd == shapes.length) {
                shapeInd = 0;
            }

            int origMaxY = rock.maxy;
            print(rock, fallen, origMaxY, 7);

            boolean falling = true;
            while (falling) {
                String jetDir = jets[jetInd];
                ++jetInd;
                if (jetInd == jets.length) {
                    jetInd = 0;
                }

                if (jetDir.equals(">") && rock.maxx < (width - 1)) {
                    Rock newRock = new Rock(shape, rock.minx + 1, rock.miny);
                    if (checkClear(newRock, blocked)) {
                        rock = newRock;
                        print(rock, fallen, origMaxY, width);
                    }
                }

                if (jetDir.equals("<") && rock.minx > 0) {
                    Rock newRock = new Rock(shape, rock.minx - 1, rock.miny);
                    if (checkClear(newRock, blocked)) {
                        rock = newRock;
                        print(rock, fallen, origMaxY, width);
                    }
                }

                Rock newRock = new Rock(shape, rock.minx, rock.miny - 1);

                if (!checkClear(newRock, blocked)) {
                    falling = false;
                    fallen.add(rock);
                    blocked.addAll(rock.cords);
                    print(null, fallen, origMaxY, width);
                    if (rock.maxy > maxHeight) {
                        maxHeight = rock.maxy;
                    }
                } else {
                    rock = newRock;
                    print(rock, fallen, origMaxY, width);
                }
            }
        }

        System.out.println(maxHeight);
    }

    public void p1() throws IOException {
        List<String> file = Util.readInput(17, true);

        String[] jets = file.get(0).trim().split("");
        String[] shapes = new String[]{"-", "+", "⅃", "|", "#"};

        List<Rock> fallen = new ArrayList<>();
        Set<Coord> blocked = new HashSet<>();

        int width = 7;

        for (int i = 0; i < width; i++) {
            blocked.add(new Coord(i, 0));
        }

        int maxHeight = 0;

        int shapeInd = 0;
        int jetInd = 0;


        while (fallen.size() < 2022) {
            String shape = shapes[shapeInd];
            Rock rock = new Rock(shape, 2, maxHeight + 4);
            ++shapeInd;
            if (shapeInd == shapes.length) {
                shapeInd = 0;
            }

            int origMaxY = rock.maxy;
            print(rock, fallen, origMaxY, 7);

            boolean falling = true;
            while (falling) {
                String jetDir = jets[jetInd];
                ++jetInd;
                if (jetInd == jets.length) {
                    jetInd = 0;
                }

                if (jetDir.equals(">") && rock.maxx < (width - 1)) {
                    Rock newRock = new Rock(shape, rock.minx + 1, rock.miny);
                    if (checkClear(newRock, blocked)) {
                        rock = newRock;
                        print(rock, fallen, origMaxY, width);
                    }
                }

                if (jetDir.equals("<") && rock.minx > 0) {
                    Rock newRock = new Rock(shape, rock.minx - 1, rock.miny);
                    if (checkClear(newRock, blocked)) {
                        rock = newRock;
                        print(rock, fallen, origMaxY, width);
                    }
                }

                Rock newRock = new Rock(shape, rock.minx, rock.miny - 1);

                if (!checkClear(newRock, blocked)) {
                    falling = false;
                    fallen.add(rock);
                    blocked.addAll(rock.cords);
                    print(null, fallen, origMaxY, width);
                    if (rock.maxy > maxHeight) {
                        maxHeight = rock.maxy;
                    }
                } else {
                    rock = newRock;
                    print(rock, fallen, origMaxY, width);
                }
            }
        }

        System.out.println(maxHeight);
    }

    public void print(final Rock rock, final List<Rock> fallen, int maxy, int width) {
        if (!printing) {
            return;
        }

        for (int y = maxy; y >= Math.max(maxy - 20, 0); --y) {
            for (int x = 0; x < width; ++x) {
                if (y == 0) {
                    System.out.print("-");
                } else {
                    Coord c = new Coord(x, y);
                    boolean printed = false;
                    if (rock != null && rock.cords.contains(c)) {
                        System.out.print("@");
                        printed = true;
                    }
                    for (Rock f : fallen) {
                        if (f.cords.contains(c)) {
                            System.out.print("#");
                            if (printed) {
                                System.out.println("woah there");
                            }
                            printed = true;
                        }
                    }
                    if (!printed) {
                        System.out.print(".");
                    }
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    public boolean checkClear(final Rock rock, final Set<Coord> blocked) {
        return Collections.disjoint(rock.cords, blocked);
    }

    private static class Rock {
        List<Coord> cords = new ArrayList<>();
        String shape;

        int miny;
        int maxy;

        int minx;
        int maxx;

        public Rock(String shape, int minx, int miny) {
            this.shape = shape;
            this.minx = minx;
            this.miny = miny;

            switch (shape) {
                case "-" -> {
                    this.maxy = miny;
                    cords.add(new Coord(minx, miny));
                    cords.add(new Coord(minx + 1, miny));
                    cords.add(new Coord(minx + 2, miny));
                    cords.add(new Coord(minx + 3, miny));
                    this.maxx = minx + 3;
                }
                case "+" -> {
                    this.maxy = miny + 2;
                    cords.add(new Coord(minx + 1, miny + 2));
                    cords.add(new Coord(minx, miny + 1));
                    cords.add(new Coord(minx + 1, miny + 1));
                    cords.add(new Coord(minx + 2, miny + 1));
                    cords.add(new Coord(minx + 1, miny));
                    this.maxx = minx + 2;
                }
                case "⅃" -> {
                    this.maxy = miny + 2;
                    cords.add(new Coord(minx + 2, miny + 2));
                    cords.add(new Coord(minx + 2, miny + 1));
                    cords.add(new Coord(minx, miny));
                    cords.add(new Coord(minx + 1, miny));
                    cords.add(new Coord(minx + 2, miny));
                    this.maxx = minx + 2;
                }
                case "|" -> {
                    this.maxy = miny + 3;
                    cords.add(new Coord(minx, miny + 3));
                    cords.add(new Coord(minx, miny + 2));
                    cords.add(new Coord(minx, miny + 1));
                    cords.add(new Coord(minx, miny));
                    this.maxx = minx;
                }
                case "#" -> {
                    this.maxy = miny + 1;
                    cords.add(new Coord(minx, miny + 1));
                    cords.add(new Coord(minx + 1, miny + 1));
                    cords.add(new Coord(minx, miny));
                    cords.add(new Coord(minx + 1, miny));
                    this.maxx = minx + 1;
                }
            }
        }

        public Set<Coord> getCordsCopy() {
            return new HashSet<>(cords);
        }
    }

    private static class Coord {

        public int x;
        public int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (!(obj instanceof Coord)) {
                return false;
            } else {
                return this.x == ((Coord) obj).x && this.y == ((Coord) obj).y;
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
