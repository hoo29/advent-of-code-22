package Days;

import Util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class D21 {

    public D21() throws IOException, InterruptedException, ExecutionException {

        List<String> file = Util.readInput(21, false);
        Map<String, Stack<Monkey>> deps = new HashMap<>();
        Map<String, Monkey> solved = new HashMap<>();
        Map<String, Monkey> all = new HashMap<>();

        boolean p2 = true;

        for (String line : file) {
            Monkey m = new Monkey(line, solved, p2);
            all.put(m.name, m);
            if (m.res == null) {
                for (String dName : m.deps) {
                    deps.compute(dName, (k, v) -> {
                        if (v == null) {
                            Stack<Monkey> mDeps = new Stack<>();
                            mDeps.add(m);
                            return mDeps;
                        } else {
                            v.add(m);
                            return v;
                        }
                    });
                }
            } else {
                solved.put(m.name, m);
                Stack<Monkey> settled = new Stack<>();
                settled.push(m);
                while (!settled.isEmpty()) {
                    Monkey curSettled = settled.pop();
                    Stack<Monkey> proc = deps.getOrDefault(curSettled.name, new Stack<>());
                    Stack<Monkey> stillToBeSolved = new Stack<>();
                    while (!proc.isEmpty()) {
                        Monkey curDep = proc.pop();
                        boolean canBeSolved = curDep.setDep(curSettled);
                        if (canBeSolved) {
                            curDep.doOp();
                            settled.push(curDep);
                            solved.put(curDep.name, curDep);
                        } else {
                            stillToBeSolved.add(curDep);
                        }
                    }
                    deps.put(curSettled.name, stillToBeSolved);
                }
            }
        }

        if (!p2) {
            System.out.println(solved.get("root").res);
        } else {
            String eq = makeEquation("root", all);
            System.out.println(eq);
            eq = eq.substring(2, eq.length() - 1);
            String[] parts = eq.split("=");
            BigInteger ans;
            if (all.get("root").op1 == null) {
                ans = solveEquation(parts[0].strip(), new BigInteger(parts[1].strip()));
            } else {
                ans = solveEquation(parts[1].strip(), new BigInteger(parts[0].strip()));
            }

            System.out.println(ans);
        }
    }

    public BigInteger solveEquation(String str, BigInteger val) {
        BigInteger ans = val;

        String eq = str.substring(str.indexOf("(") + 2, str.lastIndexOf(")") - 1);
        if (eq.strip().equals("SOLVE_ME")) {
            return val;
        }
        System.out.print(eq);
        System.out.print(" = ");
        System.out.println(val);
        String op;
        BigInteger operand;
        boolean left = true;
        if (Character.isDigit(eq.charAt(0))) {
            int firstSpace = eq.indexOf(" ");
            op = eq.substring(firstSpace + 1, firstSpace + 2);
            operand = new BigInteger(eq.substring(0, firstSpace));
        } else {
            left = false;
            int lastSpace = eq.lastIndexOf(" ");
            op = eq.substring(lastSpace - 1, lastSpace);
            operand = new BigInteger(eq.substring(lastSpace + 1));
        }

        switch (op) {
            case "/" -> ans = left ? operand.divide(ans) : ans.multiply(operand);
            case "-" -> ans = left ? operand.subtract(ans) : ans.add(operand);
            case "*" -> ans = ans.divide(operand);
            case "+" -> ans = ans.subtract(operand);

            default -> throw new RuntimeException("Unknown op " + op);
        }
        return solveEquation(eq, ans);
    }

    public String makeEquation(String monkeyName, Map<String, Monkey> all) {

        StringBuilder sb = new StringBuilder();
        Monkey m = all.get(monkeyName);
        sb.append("( ");

        if (monkeyName.equals("SOLVE_ME")) {
            sb.append(monkeyName);
        } else if (m.res != null) {
            sb.append(m.res);
        } else {
            if (m.op1 != null) {
                sb.append(m.op1);
            } else {
                sb.append(makeEquation(m.op1Monkey, all));
            }

            sb.append(" ");
            sb.append(m.op);
            sb.append(" ");

            if (m.op2 != null) {
                sb.append(m.op2);
            } else {
                sb.append(makeEquation(m.op2Monkey, all));
            }
        }

        sb.append(" )");

        return sb.toString();
    }

    private static class Monkey {

        final String name;
        final String op;

        Set<String> deps = new HashSet<>();
        String op1Monkey = "";
        String op2Monkey = "";

        BigInteger op1 = null;
        BigInteger op2 = null;

        BigInteger res = null;

        public Monkey(String line, Map<String, Monkey> solved, boolean p2) {
            String[] parts = line.split(":");

            name = parts[0].strip();
            String[] job = parts[1].strip().split(" ");

            if (job.length > 1) {
                if (p2 && "root".equals(name)) {
                    op = "=";
                } else {
                    op = job[1];
                }
                op1Monkey = job[0];
                op2Monkey = job[2];
                if (solved.get(op1Monkey) != null) {
                    op1 = solved.get(op1Monkey).res;
                } else {
                    deps.add(op1Monkey);
                }

                if (solved.get(op2Monkey) != null) {
                    op2 = solved.get(op2Monkey).res;
                } else {
                    deps.add(op2Monkey);
                }

                if (op1 != null && op2 != null) {
                    doOp();
                }
            } else if (p2 && name.equals("humn")) {
                op = "+";
                op1Monkey = "SOLVE_ME";
                deps.add(op1Monkey);
                op2 = BigInteger.ZERO;
            } else {
                op = "noop";
                res = new BigInteger(job[0].strip());
            }
        }

        public boolean setDep(Monkey m) {
            if (m.res == null) {
                throw new RuntimeException("dep didn't have result");
            }

            boolean removed = deps.remove(m.name);
            if (!removed) {
                throw new RuntimeException("dep not a dep");
            }

            if (op1Monkey.equals(m.name)) {
                op1 = m.res;
            } else {
                op2 = m.res;
            }

            return deps.isEmpty();
        }

        public void doOp() {
            if (!deps.isEmpty() || op1 == null || op2 == null) {
                throw new RuntimeException("deps not empty or ops not set");
            }

            switch (op) {
                case "/" -> this.res = op1.divide(op2);
                case "*" -> this.res = op1.multiply(op2);
                case "+" -> this.res = op1.add(op2);
                case "-" -> this.res = op1.subtract(op2);
                default -> throw new RuntimeException("Unknown op " + op);
            }
            if (this.res.equals(BigInteger.ZERO)) {
                System.out.println("oh no");
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Monkey other = (Monkey) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }
}
