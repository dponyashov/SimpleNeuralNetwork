package ru.dponyashov.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ConvertImageToDoubleList {
    private static final int MAX_COLLOR_VALUE = 255;

    private ConvertImageToDoubleList() {
    }

    public static List<Double> getGrayDigitImage(BufferedImage sourceImage) {
        List<Double> grayImage = new ArrayList<>();
        for (int i = 0; i < sourceImage.getHeight(); i++) {
            for (int j = 0; j < sourceImage.getWidth(); j++) {
                Color point = new Color(sourceImage.getRGB(j, i));
                grayImage.add(Math.abs(MAX_COLLOR_VALUE - getGrayForPoint(point)) / MAX_COLLOR_VALUE);
            }
        }
        return grayImage;
    }

    private static double getGrayForPoint(Color point) {
        return point.getRed() * 0.3 + point.getGreen() * 0.59 + point.getBlue() * 0.11;
    }


}
