package ru.dponyashov.nnet.neuron.impl;

public class InnerNeuron extends AbstNeuron {
    public InnerNeuron(int inputs, double mB, double lSpeed) {
        super(inputs, mB, lSpeed);
    }

    public InnerNeuron(double[] weights, double shift, double learnSpeed) {
        super(weights, shift, learnSpeed);
    }

    @Override
    protected double getDelta(double correctValue, double sumInput) {
        return correctValue * backFunction(sumInput);
    }
}