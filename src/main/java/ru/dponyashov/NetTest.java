package ru.dponyashov;

import ru.dponyashov.netsaver.NetSaver;
import ru.dponyashov.netsaver.impl.NetSaverImpl;
import ru.dponyashov.nnet.NewNet;
import ru.dponyashov.nnet.impl.NewNetImpl;
import ru.dponyashov.nnet.util.AppLogger;
import ru.dponyashov.util.LearningData;

import java.time.LocalTime;
import java.util.List;

public class NetTest {
    private static final int INPUTS = 10000;
    private static final int[] NEURONS_IN_INNER_LAYERS = {400, 200};
    private static final int OUTPUTS = 10;
    private static final int LEARNING_ITERATION_COUNT = 300;
    private static final int TEST_COUNT = 5;

    public static void main(String... args) {
        NewNet net = new NewNetImpl(INPUTS, NEURONS_IN_INNER_LAYERS, OUTPUTS);
        learningNet(net);
        testingNet(net);
        testingSavedNet(net);
    }

    private static void testingSavedNet(NewNet net) {
        NetSaver saver = new NetSaverImpl();
        saver.save(net);
        NewNet loadedNet = saver.load();
        if(loadedNet != null) {
            testingNet(loadedNet);
        }
    }

    private static void learningNet(NewNet net) {
        List<LearningData> lerningDataList = LearningData.factoryList(OUTPUTS);
        AppLogger.setLog(String.format("Начинаем обучение %s", LocalTime.now()));
        for (int i = 0; i < LEARNING_ITERATION_COUNT; i++) {
            lerningDataList
                    .forEach(ld -> net.doTraining(ld.getInput(), ld.getCorrectValue()));
            if ((i % 100) == 0) {
                AppLogger.setLog(String.format("Осталось %s из %s итераций -> %s",
                        LEARNING_ITERATION_COUNT - i, LEARNING_ITERATION_COUNT, LocalTime.now()));
            }
        }
        AppLogger.setLog(String.format("Обучение завершено %s", LocalTime.now()));
    }

    private static void testingNet(NewNet net) {
        AppLogger.setLog("Проверка тестовых данных");
        for (int i = 0; i < TEST_COUNT; i++) {
            LearningData testData = LearningData.factory(OUTPUTS);
            AppLogger.setLog(roundValueFromArrayToString(testData.getCorrectValue()));
            AppLogger.setLog(roundValueFromArrayToString(net.doAction(testData.getInput())));
            AppLogger.setLog("");
        }
    }

    private static String roundValueFromArrayToString(double[] arrayValues) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; ; i++) {
            sb.append(String.format("%.3f", arrayValues[i]));
            if (i == (arrayValues.length - 1)) {
                return sb.toString();
            }
            sb.append(", ");
        }
    }
}