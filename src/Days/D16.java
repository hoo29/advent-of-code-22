package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class D16 {

    public D16() throws IOException, ExecutionException, InterruptedException {

        List<String> file = Util.readInput(16, false);
        Map<String, Valve> valves = new HashMap<>();
        Set<Valve> canBeOpened = new HashSet<>();

        for (String line : file) {
            Valve v = new Valve(line);

            valves.put(v.name, v);
            if (v.flowRate > 0) {
                canBeOpened.add(v);
            }
        }

        Valve start = valves.get("AA");


        Map<List<String>, Integer> allPaths = new HashMap<>();
        int maxPressure = traverse(start, 26, 0, 0, valves, new HashMap<>(), canBeOpened, "", allPaths);
        System.out.println(maxPressure);
        var allThings = allPaths.entrySet().stream().toList();
        int threads = 16;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        int range = allThings.size() / threads;

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < threads; ++i) {
            int end = i == (threads - 1) ? allPaths.size() : (i + 1) * range;
            var future = pool.submit(new P2BruteForce(i, i * range, end, allThings));
            futures.add(future);
        }
        int p2Max = Integer.MIN_VALUE;
        for (Future<Integer> future : futures) {
            int val = future.get();
            if (val > p2Max) {
                p2Max = val;
            }
        }
        pool.shutdown();

        System.out.println(p2Max);
    }

    public int distance(Valve cur, Valve end, int dist, Map<String, Valve> valves, Set<Valve> visited) {

        if (cur.equals(end)) {
            return dist;
        }

        Set<Valve> newVisited = new HashSet<>(visited);
        newVisited.add(cur);

        int bestDst = Integer.MAX_VALUE;
        for (String name : cur.neighbours) {
            Valve next = valves.get(name);
            if (visited.contains(next)) {
                continue;
            }
            int neighbourDist = distance(next, end, dist + 1, valves, newVisited);
            if (neighbourDist < bestDst) {
                bestDst = neighbourDist;
            }
        }

        return bestDst;
    }

    public int traverse(
            Valve current,
            int timeLeft,
            int pressureRelief,
            int pressureSum,
            Map<String, Valve> valves,
            Map<String, Integer> distanceCache,
            Set<Valve> canBeOpened,
            String opened,
            Map<List<String>, Integer> allPaths
    ) {
        if (timeLeft <= 0) {
            List<String> path = Arrays.asList(opened.substring(0, opened.length() - 1).split(","));
            allPaths.put(path, pressureRelief);
            return pressureRelief;
        }

        int newPressure = pressureRelief + (timeLeft * current.flowRate);
        int newPressureSum = pressureSum + current.flowRate;
        String newOpened = opened;
        if (!"AA".equals(current.name)) {
            newOpened += current.name + ",";
        }

        int maxPressure = newPressure;

        for (Valve v : canBeOpened) {
            int dist;
            if (v.equals(current)) {
                continue;
            }

            String distCacheKey = current.name + v.name;
            if (distanceCache.containsKey(distCacheKey)) {
                dist = distanceCache.get(distCacheKey);
            } else {
                dist = distance(current, v, 0, valves, new HashSet<>());
                distanceCache.put(distCacheKey, dist);
            }

            Set<Valve> newCanBeOpened = new HashSet<>(canBeOpened);
            newCanBeOpened.remove(v);

            int pathPressure = traverse(v, timeLeft - dist - 1, newPressure, newPressureSum, valves, distanceCache, newCanBeOpened, newOpened, allPaths);

            if (pathPressure > maxPressure) {
                maxPressure = pathPressure;
            }
        }
        return maxPressure;
    }

    private record P2BruteForce(int range, int start, int stop,
                                List<Map.Entry<List<String>, Integer>> allThings) implements Callable<Integer> {
        @Override
        public Integer call() {
            int p2Max = Integer.MIN_VALUE;

            for (int i = start; i < Math.min(allThings.size() - 1, stop); ++i) {
                for (int j = i + 1; j < allThings.size(); ++j) {
                    Set<String> why = new HashSet<>(allThings.get(i).getKey());
                    if (!why.removeAll(allThings.get(j).getKey()) && (allThings.get(i).getValue() + allThings.get(j).getValue() > p2Max)) {
                        p2Max = allThings.get(i).getValue() + allThings.get(j).getValue();
                    }
                }
            }
            System.out.println("finished " + range);
            return p2Max;
        }
    }

    private static class Valve {
        int flowRate;
        String name;
        List<String> neighbours;

        public Valve(String line) {
            String[] first = line.split(";")[0].split(" ");

            name = first[1];
            flowRate = Integer.parseInt(first[4].split("=")[1].strip());

            String second = line.split(";")[1];
            second = second.substring(second.indexOf("valve") + 6);
            second = second.replaceAll(" ", "");
            neighbours = Arrays.asList(second.split(","));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (obj instanceof String) {
                return this.name.equals(obj);
            } else if (!(obj instanceof Valve)) {
                return false;
            } else {
                return this.name.equals(((Valve) obj).name);
            }
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
