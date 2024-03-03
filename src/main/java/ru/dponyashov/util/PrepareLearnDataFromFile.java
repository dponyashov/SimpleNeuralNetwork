package ru.dponyashov.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PrepareLearnDataFromFile {

    private PrepareLearnDataFromFile() {
    }

    public static List<Double> getInputData(String fileName) {
        try {
            File sourceFile = new File(fileName);
            BufferedImage sourceImage = ImageIO.read(sourceFile);
            return ConvertImageToDoubleList.getGrayDigitImage(sourceImage);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Double> getCorrectValue(String fileName, int outSignalCount) {
        List<Double> data = new ArrayList<>();
        int indexCorrectSignal = Integer.parseInt(Paths.get(fileName).getFileName().toString().substring(0, 1));
        for (int i = 0; i < outSignalCount; i++) {
            if (i == indexCorrectSignal) {
                data.add(1.0);
            } else {
                data.add(0.0);
            }
        }
        return data;
    }
}