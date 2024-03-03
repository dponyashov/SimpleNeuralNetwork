package ru.dponyashov.nnet.neuron;

public abstract class Neuron {
    public abstract double doAction(double[] input);

    public abstract double[] doTraining(double[] input, double target);
}