package ru.dponyashov.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataFromSourceFile {
    private static final String FILE_NAME = "/home/dponyashov/projects/NewNet/src/main/resources/source.txt";
    private static final String SOURCE_PATH = "/home/dponyashov/projects/NewNet/src/main/resources/source/";

    private FileDataFromSourceFile() {
    }

    public static List<String> getNameFiles() {
        List<String> lines = new ArrayList<>();
        try (
                FileReader fr = new FileReader(FILE_NAME);
                BufferedReader reader = new BufferedReader(fr)
        ) {
            String fileLine = reader.readLine();
            while (fileLine != null) {
                lines.add(SOURCE_PATH + fileLine);
                fileLine = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return lines;
    }
}