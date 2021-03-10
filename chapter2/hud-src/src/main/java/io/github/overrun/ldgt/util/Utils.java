package io.github.overrun.ldgt.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author squid233
 * @since 2021/03/08
 */
public final class Utils {
    public static String readLines(String name) {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(name);
             Scanner sc = new Scanner(Objects.requireNonNull(is),
                     StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readShaderLines(String name) {
        String s = readLines(name);
        return s != null ? s : "#version 110\nvoid main(){}";
    }
}
