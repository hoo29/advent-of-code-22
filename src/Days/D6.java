package Days;

import Util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D6 {

    public D6() throws IOException {

        List<String> file = Util.readInput(6, false);

        String line = file.get(0);

        int i = 0;
        for (; i < line.length() - 4; ++i) {
            String seg = line.substring(i, i + 4);
            Set<String> buff = new HashSet<>();
            buff.addAll(Arrays.asList(seg.split("")));
            if (buff.size() == 4) {
                break;
            }
        }

        System.out.println(i + 4);

        i = 0;
        for (; i < line.length() - 14; ++i) {
            String seg = line.substring(i, i + 14);
            Set<String> buff = new HashSet<>();
            buff.addAll(Arrays.asList(seg.split("")));
            if (buff.size() == 14) {
                break;
            }
        }

        System.out.println(i + 14);
    }
}
