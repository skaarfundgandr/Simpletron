package com.jm.simpletron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Loader {
    // Search for files in current working directory
    public static void read(String filePath) throws Exception {
        File f = new File(filePath);

        try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
            String current;

            while ((current = bf.readLine()) != null) {
                current = current.replaceAll(";", " ").strip();

                if (current.isEmpty()) continue;
                String[] splittedStr = current.split(" ");

                Memory m = Memory.getInstance();

                if (splittedStr.length < 2) {
                    break;
                }

                m.addItem(splittedStr[1].strip(), Integer.parseInt(splittedStr[0].trim()));
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
