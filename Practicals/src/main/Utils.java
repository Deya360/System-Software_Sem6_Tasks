package main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static void writeToFile(String path, StringBuilder sb) throws IOException {
        try (Writer writer =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(sb.toString());
        }
    }

    public static void printToConsole(StringBuilder sb) {
        System.out.println(sb.toString());
    }
}
