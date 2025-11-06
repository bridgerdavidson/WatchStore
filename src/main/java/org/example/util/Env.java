package org.example.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Env {
    private static final Map<String, String> envMap = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) { // repeat for each line in the file.
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;  // ignore empty lines and comments
                String[] parts = line.split("=", 2); // divide the line into key and value.
                if (parts.length == 2) { // only process lines with exactly 2 parts
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: .env file not found or unreadable.");
        }
    }

    // two version of get method. this is an example of overloading
    // when there is more than one method with the same name but different parameters

    public static String get(String key) {
        return envMap.get(key);
    }


    public static String get(String key, String defaultValue) {
        return envMap.getOrDefault(key, defaultValue);
    }
}
