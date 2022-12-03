package Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Util {

    private Util() {
    }

    public static List<String> readInput(Integer day, boolean test) throws IOException {
        String fileName = "src/D" + day;
        if (test) {
            fileName += "/test.txt";
        } else {
            fileName += "/input.txt";
        }
        Path path = Path.of(fileName);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
