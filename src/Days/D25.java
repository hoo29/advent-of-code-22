package Days;

import Util.Util;

import java.io.IOException;
import java.util.List;

public class D25 {

    public D25() throws IOException {

        List<String> file = Util.readInput(25, false);
        long sum = 0;
        for (String line : file) {
            long lineSum = 0;
            for (int i = 0; i < line.length(); ++i) {
                double base = Math.pow(5, (line.length() - 1) - i);
                char c = line.charAt(i);
                switch (c) {
                    case '=' -> lineSum += -2 * base;
                    case '-' -> lineSum += -1 * base;
                    case '0' -> lineSum += 0 * base;
                    case '1' -> lineSum += 1 * base;
                    case '2' -> lineSum += 2 * base;
                }
            }
            sum += lineSum;
        }

        System.out.println(sum);
        StringBuilder sb = new StringBuilder();
        do {
            long base = (sum + 2) / 5;
            int digit = (int) (sum - (5 * base));
            char snafuChar = ' ';
            switch (digit) {
                case -2 -> snafuChar = '=';
                case -1 -> snafuChar = '-';
                case 0 -> snafuChar = '0';
                case 1 -> snafuChar = '1';
                case 2 -> snafuChar = '2';
            }
            sb.append(snafuChar);
            sum = base;
        } while (sum != 0);

        System.out.println(sb.reverse());
    }
}
