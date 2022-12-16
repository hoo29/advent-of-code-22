package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class D16 {

    public D16() throws IOException {

        List<String> file = Util.readInput(16, true);
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
        int maxPressure = traverse(start, 1, 0, valves, canBeOpened, "", new HashMap<>());
        System.out.println(maxPressure);
    }

    public int traverse(
            Valve current,
            int minute,
            int pressureRelief,
            Map<String, Valve> valves,
            Set<Valve> canBeOpened,
            String path,
            Map<String, Map<Integer, Integer>> cache) {

        if (pressureRelief > 1651) {
            System.out.println("gone wrong");
        }

        if (minute >= 30 || current == null || canBeOpened.size() == 0) {
            return pressureRelief;
        }

        String newPath;
        if ("".equals(path)) {
            newPath = current.name;
        } else {
            newPath = path + "," + current.name;
        }

        int maxPressure = Integer.MIN_VALUE;

        if (cache.getOrDefault(current.name, new HashMap<>()).getOrDefault(minute,
                Integer.MIN_VALUE) > pressureRelief) {
            return pressureRelief;
        }

        if (current.flowRate > 0 && canBeOpened.contains(current)) {
            int newMinute = minute + 1;
            Set<Valve> op = new HashSet<>(canBeOpened);
            op.remove(current);
            int minutesLeft = 30 - newMinute;
            int newPressure = pressureRelief + (minutesLeft * current.flowRate);
            for (String name : current.neighbours) {
                Valve neighbour = valves.get(name);
                int pathMax = traverse(neighbour, newMinute + 1, newPressure, valves, op, newPath, cache);

                if (pathMax > maxPressure) {
                    maxPressure = pathMax;
                }
            }

            var vCache = cache.getOrDefault(current.name, new HashMap<>());
            var minuteCache = vCache.getOrDefault(newMinute, Integer.MIN_VALUE);

            if (maxPressure > minuteCache) {
                vCache.put(newMinute, maxPressure);
                cache.put(current.name, vCache);
            }

        }

        for (String name : current.neighbours) {
            Valve neighbour = valves.get(name);
            int pathMax = traverse(neighbour, minute + 1, pressureRelief, valves, canBeOpened, newPath, cache);
            if (pathMax > maxPressure) {
                maxPressure = pathMax;
            }
            var vCache = cache.getOrDefault(current.name, new HashMap<>());
            var minuteCache = vCache.getOrDefault(minute, Integer.MIN_VALUE);

            if (maxPressure > minuteCache) {
                vCache.put(minute, maxPressure);
                cache.put(current.name, vCache);
            }
        }

        return maxPressure;
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
            second = second.substring(second.indexOf("valves") + 6);
            second = second.replaceAll(" ", "");
            neighbours = Arrays.asList(second.split(","));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
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

    }

}
