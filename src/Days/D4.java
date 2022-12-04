package Days;

import Util.Util;

import java.io.IOException;
import java.util.List;

public class D4 {

    public D4() throws IOException {

        List<String> file = Util.readInput(4, false);

        int sum = 0;
        for (String pair : file) {
            String[] parts = pair.split(",");
            int p1L = Integer.parseInt(parts[0].split("-")[0]);
            int p1U = Integer.parseInt(parts[0].split("-")[1]);

            int p2L = Integer.parseInt(parts[1].split("-")[0]);
            int p2U = Integer.parseInt(parts[1].split("-")[1]);

            if (p1L >= p2L && p1U <= p2U) {
                sum += 1;
            } else if (p2L >= p1L && p2U <= p1U) {
                sum += 1;
            }
        }
        System.out.println(sum);

        sum = 0;
        for (String pair : file) {
            String[] parts = pair.split(",");
            int p1L = Integer.parseInt(parts[0].split("-")[0]);
            int p1U = Integer.parseInt(parts[0].split("-")[1]);

            int p2L = Integer.parseInt(parts[1].split("-")[0]);
            int p2U = Integer.parseInt(parts[1].split("-")[1]);

            if (p2L >= p1L && p1U >= p2L) {
                sum += 1;
            } else if (p1L >= p2L && p2U >= p1L) {
                sum += 1;
            }
        }
        System.out.println(sum);
    }
}
