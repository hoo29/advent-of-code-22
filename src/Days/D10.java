package Days;

import Util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D10 {

    public D10() throws IOException {
        List<String> file = Util.readInput(10, false);

        Map<String, Integer> cycles = new HashMap<>();

        cycles.put("noop", 1);
        cycles.put("addx", 2);

        int cycle = 1;
        int signal = 0;
        int reg = 1;
        int cx = 0;
        int cy = 0;
        int width = 40;
        int height = 6;

        String[][] crt = new String[height][width];

        for (String line : file) {
            String instr = line.split(" ")[0];
            int cycleInstr = cycles.get(instr);
            for (int i = 0; i < cycleInstr; ++i) {

                if ((cycle + 20) % 40 == 0) {
                    signal += reg * cycle;
                }

                if (Math.abs(cx - reg) <= 1) {
                    crt[cy][cx] = "#";
                } else {
                    crt[cy][cx] = ".";
                }

                if ("addx".equals(instr) && i == cycleInstr - 1) {
                    reg += Integer.parseInt(line.split(" ")[1]);
                }

                ++cycle;

                if (cx == width - 1) {
                    cx = 0;
                    if (cy == height - 1) {
                        cy = 0;
                    } else {
                        ++cy;
                    }
                } else {
                    ++cx;
                }
                printCrt(crt);
            }
        }

        System.out.println(signal);
    }

    private void printCrt(String[][] crt) {
        for (String[] strings : crt) {
            for (int i = 0; i < strings.length; ++i) {
                System.out.print(" ");
                if (i == strings.length - 1) {
                    System.out.println(strings[i]);
                } else {
                    System.out.print(strings[i]);
                }
            }
        }
        System.out.println();
    }
}

