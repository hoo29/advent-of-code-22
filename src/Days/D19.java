package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class D19 {

    public D19() throws IOException, InterruptedException, ExecutionException {

        List<String> file = Util.readInput(19, false);
        file = file.subList(0, 3);
        ExecutorService pool = Executors.newFixedThreadPool(1);

        List<Future<Integer[]>> futures = new ArrayList<>();

        for (String line : file) {
//            Blueprint b = new Blueprint(line, 24);
            Blueprint b = new Blueprint(line, 32);
            var future = pool.submit(b);
            futures.add(future);
        }

//        int sum = 0;
        int sum = 1;
        for (Future<Integer[]> f : futures) {
            Integer[] res = f.get();
//            sum += res[0] * res[1];
            sum *= res[0];
        }
        pool.shutdown();

        System.out.println(sum);

    }

    private static class Stats {
        int oreCount;
        int clayCount;
        int obsidianCount;
        int geodeCount;
        int oreRobotCount;
        int clayRobotCount;
        int obsidianRobotCount;
        int geodeRobotCount;

        int minute;

        public Stats(
                int oreCount,
                int clayCount,
                int obsidianCount,
                int geodeCount,
                int oreRobotCount,
                int clayRobotCount,
                int obsidianRobotCount,
                int geodeRobotCount,
                int minute) {
            this.oreCount = oreCount;
            this.clayCount = clayCount;
            this.obsidianCount = obsidianCount;
            this.geodeCount = geodeCount;
            this.oreRobotCount = oreRobotCount;
            this.clayRobotCount = clayRobotCount;
            this.obsidianRobotCount = obsidianRobotCount;
            this.geodeRobotCount = geodeRobotCount;
            this.minute = minute;
        }

        public Stats(Stats other) {
            this.oreCount = other.oreCount;
            this.clayCount = other.clayCount;
            this.obsidianCount = other.obsidianCount;
            this.geodeCount = other.geodeCount;
            this.oreRobotCount = other.oreRobotCount;
            this.clayRobotCount = other.clayRobotCount;
            this.obsidianRobotCount = other.obsidianRobotCount;
            this.geodeRobotCount = other.geodeRobotCount;
            this.minute = other.minute;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Stats stats = (Stats) o;
            return oreCount == stats.oreCount && clayCount == stats.clayCount && obsidianCount == stats.obsidianCount
                    && geodeCount == stats.geodeCount && oreRobotCount == stats.oreRobotCount
                    && clayRobotCount == stats.clayRobotCount && obsidianRobotCount == stats.obsidianRobotCount
                    && geodeRobotCount == stats.geodeRobotCount && minute == stats.minute;
        }

        @Override
        public int hashCode() {
            return Objects.hash(oreCount, clayCount, obsidianCount, geodeCount, oreRobotCount, clayRobotCount, obsidianRobotCount, geodeRobotCount, minute);
        }
    }

    private static class Blueprint implements Callable<Integer[]> {

        private final int id;

        private final int oreRobotOreCost;
        private final int clayRobotOreCost;
        private final int obsidianRobotOreCost;
        private final int obsidianRobotClayCost;
        private final int geodeRobotOreCost;
        private final int geodeRobotObsidianCost;

        private final int maxOre;
        private final int minOre;

        private final int maxMinutes;

        private final Map<Stats, Integer> cache = new HashMap<>();

        public Blueprint(String line, int maxMinutes) {

            this.maxMinutes = maxMinutes;
            id = Integer.parseInt(line.substring(line.indexOf(" "), line.indexOf(":")).strip());

            String[] parts = line.substring(line.indexOf(":") + 1).strip().split("\\.");

            oreRobotOreCost = Integer.parseInt(parts[0].strip().split(" ")[4]);

            clayRobotOreCost = Integer.parseInt(parts[1].strip().split(" ")[4]);

            obsidianRobotOreCost = Integer.parseInt(parts[2].strip().split(" ")[4]);
            obsidianRobotClayCost = Integer.parseInt(parts[2].strip().split(" ")[7]);

            geodeRobotOreCost = Integer.parseInt(parts[3].strip().split(" ")[4]);
            geodeRobotObsidianCost = Integer.parseInt(parts[3].strip().split(" ")[7]);

            maxOre = Collections.max(Arrays
                    .asList(oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost,
                            geodeRobotOreCost));

        }

        @Override
        public Integer[] call() throws Exception {
            System.out.println("Starting blueprint " + id);
            int minute = 1;

            int oreCount = 0;
            int clayCount = 0;
            int obsidianCount = 0;
            int geodeCount = 0;

            int oreRobotCount = 1;
            int clayRobotCount = 0;
            int obsidianRobotCount = 0;
            int geodeRobotCount = 0;

            Stats start = new Stats(oreCount, clayCount, obsidianCount, geodeCount, oreRobotCount, clayRobotCount,
                    obsidianRobotCount, geodeRobotCount, minute);

            int maxCount = tick(start);

            System.out.println("Finished blueprint " + id);

            return new Integer[]{
                    maxCount,
                    id};
        }

        private Stats procMin(
                final String op,
                final Stats stats) {

            Stats newStats = new Stats(stats);
            if ("BUILD_ORE".equals(op)) {
                newStats.oreCount -= oreRobotOreCost;
            } else if ("BUILD_CLAY".equals(op)) {
                newStats.oreCount -= clayRobotOreCost;
            } else if ("BUILD_OBSIDIAN".equals(op)) {
                newStats.oreCount -= obsidianRobotOreCost;
                newStats.clayCount -= obsidianRobotClayCost;
            } else if ("BUILD_GEODE".equals(op)) {
                newStats.oreCount -= geodeRobotOreCost;
                newStats.obsidianCount -= geodeRobotObsidianCost;
            }

            // collect resources
            newStats.oreCount += newStats.oreRobotCount;
            newStats.clayCount += newStats.clayRobotCount;
            newStats.obsidianCount += newStats.obsidianRobotCount;
            newStats.geodeCount += newStats.geodeRobotCount;

            if ("BUILD_ORE".equals(op)) {
                ++newStats.oreRobotCount;
            } else if ("BUILD_CLAY".equals(op)) {
                ++newStats.clayRobotCount;
            } else if ("BUILD_OBSIDIAN".equals(op)) {
                ++newStats.obsidianRobotCount;
            } else if ("BUILD_GEODE".equals(op)) {
                ++newStats.geodeRobotCount;
            }

            ++newStats.minute;
            return newStats;
        }

        private int tick(
                final Stats stats) {

            if (stats.minute >= (maxMinutes + 1)) {
                return stats.geodeCount;
            }

            int maxGeodeCount = Integer.MIN_VALUE;

            // spend resources but in a sensible way
            List<Stats> options = new ArrayList<>();

            boolean build = false;
            // build ore robot
            if (stats.oreRobotCount < maxOre && stats.oreCount >= oreRobotOreCost) {
                options.add(procMin("BUILD_ORE", stats));
                build = true;
            }

            // build clay robot
            if (stats.clayRobotCount < obsidianRobotClayCost && stats.oreCount >= clayRobotOreCost) {
                options.add(procMin("BUILD_CLAY", stats));
                build = true;
            }

            // build obsidian robot
            if (stats.obsidianRobotCount < geodeRobotObsidianCost && stats.oreCount >= obsidianRobotOreCost
                    && stats.clayCount >= obsidianRobotClayCost) {
                options.add(procMin("BUILD_OBSIDIAN", stats));
                build = true;
            }

            // build geode robot
            if (stats.oreCount >= geodeRobotOreCost && stats.obsidianCount >= geodeRobotObsidianCost) {
                options.add(procMin("BUILD_GEODE", stats));
                build = true;
            }


            // don't build anything
            if (stats.oreRobotCount < maxOre || stats.clayRobotCount < obsidianRobotClayCost
                    || stats.obsidianRobotCount < geodeRobotObsidianCost) {
                options.add(procMin("NOOP", stats));
            }

            for (final Stats option : options) {
                if (cache.containsKey(option)) {
                    int optionSum = cache.get(option);
                    if (optionSum > maxGeodeCount) {
                        maxGeodeCount = optionSum;
                    }
                } else {
                    final int optionSum = tick(option);
                    cache.put(option, optionSum);
                    if (optionSum > maxGeodeCount) {
                        maxGeodeCount = optionSum;
                    }
                }
            }

            final int max = maxGeodeCount;
            cache.compute(stats, (k, v) -> (v == null || max > v) ? max : v);

            return maxGeodeCount;
        }
    }
}
