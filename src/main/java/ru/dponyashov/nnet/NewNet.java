package ru.dponyashov.nnet;

public interface NewNet {
    double[] doAction(double[] input);

    double[] doTraining(double[] input, double[] correctValue);
}