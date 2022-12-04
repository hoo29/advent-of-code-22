package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class D1 {

    public D1() throws IOException {

        List<String> file = Util.readInput(1, false);
        List<List<Integer>> calories = file
                .stream()
                .reduce(
                        new ArrayList<>(),
                        (acc, a) -> {
                            if (acc.size() == 0 || "".equals(a)) {
                                acc.add(new ArrayList<>());
                            } else {
                                acc.get(acc.size() - 1).add(Integer.parseInt(a));
                            }
                            return acc;
                        }, (acc1, acc2) -> {
                            acc1.addAll(acc2);
                            return acc1;
                        });

        List<Elf> elfs = IntStream.range(0, calories.size())
                .mapToObj(ind -> new Elf(calories.get(ind), ind))
                .sorted(Comparator.comparingInt(arg0 -> arg0.total))
                .toList();

        for (Elf elf : elfs) {
            System.out.println(elf);
        }
        int size = elfs.size();
        System.out.println(elfs.get(size - 1).total + elfs.get(size - 2).total + elfs.get(size - 3).total);

    }

    private static class Elf {
        public final Integer total;
        public final Integer index;

        public Elf(List<Integer> calories, int index) {
            this.index = index;
            this.total = calories
                    .stream()
                    .reduce(0, Integer::sum);
        }

        @Override
        public String toString() {
            return "Elf " + this.index + " calories " + this.total;
        }
    }
}
