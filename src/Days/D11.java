package Days;

import Util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class D11 {

    public D11() throws IOException {
        List<String> file = Util.readInput(11, false);

        List<Monkey> monkeys = new ArrayList<>();
        int allDivisors = 1;
        int i = 0;
        while (i < file.size()) {
            String line = file.get(i);
            if (!line.startsWith("Monkey")) {
                throw new RuntimeException("Parse has gone wrong");
            }
            List<BigInteger> startingItems = Arrays
                    .stream(
                            file.get(i + 1)
                                    .split(":")[1]
                                    .split(","))
                    .reduce(new ArrayList<>(), (acc, s) -> {
                                acc.add(BigInteger.valueOf(Integer.parseInt(s.strip())));
                                return acc;
                            }, (acc1, acc2) -> {
                                acc1.addAll(acc2);
                                return acc1;
                            }
                    );

            String[] operation = file.get(i + 2).split("= ")[1].split(" ");

            int divisor = Integer.parseInt(file.get(i + 3).split("by")[1].strip());
            allDivisors *= divisor;
            int trueDest = Integer.parseInt(file.get(i + 4).substring(file.get(i + 4).lastIndexOf(" ")).strip());
            int falseDest = Integer.parseInt(file.get(i + 5).substring(file.get(i + 5).lastIndexOf(" ")).strip());

            monkeys.add(new Monkey(
                    startingItems, operation, divisor, trueDest, falseDest
            ));
            i += 7;
        }

        for (Monkey m : monkeys) {
            m.allDivisors = allDivisors;
        }

        int rounds = 10000;

        for (i = 0; i < rounds; ++i) {
            for (Monkey m : monkeys) {
                m.processItems(monkeys);
            }
        }

        List<BigInteger> count = monkeys.stream().reduce(new ArrayList<>(), (acc, m) -> {
            acc.add(m.inspectCount);
            return acc;
        }, (acc1, acc2) -> {
            acc1.addAll(acc2);
            return acc1;
        });
        Collections.sort(count);
        Collections.reverse(count);

        // 552662866 too low
        System.out.println(count.get(0).multiply(count.get(1)));
    }

    private static class Monkey {
        Deque<BigInteger> items;
        String[] operation;
        BigInteger divisor;
        int trueDest;
        int falseDest;
        int allDivisors;

        BigInteger inspectCount = BigInteger.ZERO;

        public Monkey(List<BigInteger> startingItems, String[] operation, int divisor, int trueDest, int falseDest) {
            this.items = new ArrayDeque<>(startingItems);
            this.operation = operation;
            this.divisor = BigInteger.valueOf(divisor);
            this.trueDest = trueDest;
            this.falseDest = falseDest;
        }

        public void processItems(List<Monkey> monkeys) {

            while (items.size() > 0) {
                inspectCount = inspectCount.add(BigInteger.ONE);
                BigInteger item = items.poll();
                item = item.mod(BigInteger.valueOf(allDivisors));
                BigInteger op1 = parseOp(operation[0], item);
                BigInteger op2 = parseOp(operation[2], item);
                BigInteger newValue = doOperation(op1, op2, operation[1]);
                if (newValue.mod(divisor).equals(BigInteger.ZERO)) {
                    monkeys.get(trueDest).items.push(newValue);
                } else {
                    monkeys.get(falseDest).items.push(newValue);
                }
            }
        }

        private BigInteger doOperation(BigInteger op1, BigInteger op2, String op) {
            return switch (op) {
                case "/" -> op1.divide(op2);
                case "*" -> op1.multiply(op2);
                case "+" -> op1.add(op2);
                case "-" -> op1.subtract(op2);
                default -> throw new RuntimeException("Unknown op " + op);
            };
        }

        private BigInteger parseOp(String str, BigInteger item) {
            if ("old".equals(str)) {
                return item;
            } else {
                return BigInteger.valueOf(Integer.parseInt(str.strip()));
            }
        }
    }
}

