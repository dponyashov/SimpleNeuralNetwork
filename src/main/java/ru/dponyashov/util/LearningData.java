package ru.dponyashov.util;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LearningData {
    private final double[] input;
    private final double[] correctValue;

    private LearningData(List<Double> input, List<Double> correctValue) {
        this.input = convertToArray(input);
        this.correctValue = convertToArray(correctValue);
    }

    public static LearningData factory(int outSignalCount) {
        List<LearningData> learningData = factoryList(outSignalCount);
        int index = new Random().nextInt(learningData.size());
        return learningData.get(index);
    }

    public static List<LearningData> factoryList(int outSignalCount) {
        List<String> files = FileDataFromSourceFile.getNameFiles();
        return files.stream()
                .map((fileName) -> new LearningData(PrepareLearnDataFromFile.getInputData(fileName),
                        PrepareLearnDataFromFile.getCorrectValue(fileName, outSignalCount))
                ).collect(Collectors.toList());
    }

    public double[] getInput() {
        return this.input;
    }

    public double[] getCorrectValue() {
        return this.correctValue;
    }

    private double[] convertToArray(List<Double> list) {
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .toArray();
    }
}