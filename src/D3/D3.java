package D3;

import Util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D3 {

    public D3() throws IOException {

        List<String> file = Util.readInput(3, false);

        List<Rucksack> rucksacks = file.stream().map(a -> new Rucksack(a)).toList();

        int sum = 0;
        int a = 'a';
        int A = 'A';
        for (Rucksack r : rucksacks) {
            if (r.intersection.equals(r.intersection.toUpperCase())) {
                sum += (int) r.intersection.charAt(0) - A + 27;
            } else {
                sum += (int) r.intersection.charAt(0) - a + 1;
            }
        }

        System.out.println(sum);

        sum = 0;
        for (int i = 0; i < file.size(); i += 3) {
            Set<String> bag1 = new HashSet<>(Arrays.asList(file.get(i).split("")));
            Set<String> bag2 = new HashSet<>(Arrays.asList(file.get(i + 1).split("")));
            Set<String> bag3 = new HashSet<>(Arrays.asList(file.get(i + 2).split("")));

            bag1.retainAll(bag2);
            bag1.retainAll(bag3);
            if (bag1.size() != 1) {
                throw new RuntimeException("there should be 1");
            }
            String intersection = bag1.toArray(new String[]{})[0];
            if (intersection.equals(intersection.toUpperCase())) {
                sum += (int) intersection.charAt(0) - A + 27;
            } else {
                sum += (int) intersection.charAt(0) - a + 1;
            }
        }
        System.out.println(sum);
    }

    private static class Rucksack {

        public final String intersection;

        public Rucksack(String input) {
            int mid = (input.length() / 2);
            String[] parts = {input.substring(0, mid), input.substring(mid)};
            Set<String> bag1 = new HashSet<>(Arrays.asList(parts[0].split("")));
            Set<String> bag2 = new HashSet<>(Arrays.asList(parts[1].split("")));
            bag1.retainAll(bag2);
            if (bag1.size() != 1) {
                throw new RuntimeException("there should be 1");
            }
            this.intersection = bag1.toArray(new String[]{})[0];
        }

    }
}
