package org.openjdk.jmc.flightrecorder.json.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utils {
  public static InputStream readResource(String path) {
    return Utils.class.getClassLoader().getResourceAsStream(path);
  }

  public static String readResourceAsString(String path) throws IOException {
    try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(path)) {
      if (is == null) return null;
      try (InputStreamReader isr = new InputStreamReader(is);
          BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }
}
