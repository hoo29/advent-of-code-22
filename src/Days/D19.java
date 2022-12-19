package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class D19 {

    public D19() throws IOException, InterruptedException, ExecutionException {

        List<String> file = Util.readInput(19, true);
        ExecutorService pool = Executors.newFixedThreadPool(16);

        List<Future<Integer[]>> futures = new ArrayList<>();

        // for (String line : file) {
        // Blueprint b = new Blueprint(line);
        // var future = pool.submit(b);
        // futures.add(future);
        // }

        var future = pool.submit(new Blueprint(file.get(0)));
        futures.add(future);

        int sum = 0;
        for (Future<Integer[]> f : futures) {
            Integer[] res = f.get();
            // sum += res[0] * res[1];
            sum += res[0];
        }
        pool.shutdown();

        System.out.println(sum);

    }

    private class Stats {
        int oreCount;
        int clayCount;
        int obsidianCount;
        int geodeCount;
        int oreRobotCount;
        int clayRobotCount;
        int obsidianRobotCount;
        int geodeRobotCount;

        public Stats(
                int oreCount,
                int clayCount,
                int obsidianCount,
                int geodeCount,
                int oreRobotCount,
                int clayRobotCount,
                int obsidianRobotCount,
                int geodeRobotCount) {
            this.oreCount = oreCount;
            this.clayCount = clayCount;
            this.obsidianCount = obsidianCount;
            this.geodeCount = geodeCount;
            this.oreRobotCount = oreRobotCount;
            this.clayRobotCount = clayRobotCount;
            this.obsidianRobotCount = obsidianRobotCount;
            this.geodeRobotCount = geodeRobotCount;
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
        }
    };

    private class Blueprint implements Callable<Integer[]> {

        private final int id;

        private final int oreRobotOreCost;
        private final int clayRobotOreCost;
        private final int obsidianRobotOreCost;
        private final int obsidianRobotClayCost;
        private final int geodeRobotOreCost;
        private final int geodeRobotObsidianCost;

        private final int maxOre;

        public Blueprint(String line) {

            id = Integer.parseInt(line.substring(line.indexOf(" "), line.indexOf(":")).strip());

            String[] parts = line.substring(line.indexOf(":") + 1).strip().split("\\.");

            oreRobotOreCost = Integer.parseInt(parts[0].strip().split(" ")[4]);

            clayRobotOreCost = Integer.parseInt(parts[1].strip().split(" ")[4]);

            obsidianRobotOreCost = Integer.parseInt(parts[2].strip().split(" ")[4]);
            obsidianRobotClayCost = Integer.parseInt(parts[2].strip().split(" ")[7]);

            geodeRobotOreCost = Integer.parseInt(parts[3].strip().split(" ")[4]);
            geodeRobotObsidianCost = Integer.parseInt(parts[3].strip().split(" ")[7]);

            maxOre = Collections.max(Arrays
                    .asList(new Integer[] { oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost,
                            geodeRobotOreCost }));

        }

        @Override
        public Integer[] call() throws Exception {

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
                    obsidianRobotCount, geodeRobotCount);

            Integer maxCount = tick(minute, start);

            System.out.println("Finished blueprint " + id);

            return new Integer[] {
                    maxCount,
                    id };

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
            return newStats;
        }

        private int tick(
                final int minute,
                final Stats stats) {

            if (minute >= 25) {
                return stats.geodeCount;
            }

            int maxGeodeCount = Integer.MIN_VALUE;

            // spend resources but in a sensible way
            List<Stats> options = new ArrayList<>();

            // build ore robot
            if (stats.oreRobotCount < maxOre && stats.oreCount >= oreRobotOreCost) {
                options.add(procMin("BUILD_ORE", stats));
            }

            // build clay robot
            if (stats.clayRobotCount < obsidianRobotClayCost && stats.oreCount >= clayRobotOreCost) {
                options.add(procMin("BUILD_CLAY", stats));
            }

            // build obsidian robot
            if (stats.obsidianRobotCount < geodeRobotObsidianCost && stats.oreCount >= obsidianRobotOreCost
                    && stats.clayCount >= obsidianRobotClayCost) {
                options.add(procMin("BUILD_OBSIDIAN", stats));
            }

            // build geode robot
            if (stats.oreCount >= geodeRobotOreCost && stats.obsidianCount >= geodeRobotObsidianCost) {
                options.add(procMin("BUILD_GEODE", stats));
            }

            // don't build anything
            if (stats.oreRobotCount < maxOre || stats.clayRobotCount < obsidianRobotClayCost
                    || stats.obsidianRobotCount < geodeRobotObsidianCost) {
                options.add(procMin("NOOP", stats));
            }

            for (final Stats option : options) {
                int optionSum = tick(minute + 1, option);
                if (optionSum > maxGeodeCount) {
                    maxGeodeCount = optionSum;
                }
            }

            return maxGeodeCount;
        }
    }
}
