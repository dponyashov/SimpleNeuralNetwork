package ru.dponyashov.nnet.neuron.impl;

public class OutNeuron extends AbstNeuron {
    public OutNeuron(int inputs, double mB, double lSpeed) {
        super(inputs, mB, lSpeed);
    }
    public OutNeuron(double[] weights, double shift, double learnSpeed) {
        super(weights, shift, learnSpeed);
    }

    @Override
    protected double getDelta(double correctValue, double sumInput) {
        return (correctValue - lineFunction(sumInput)) * backFunction(sumInput);
    }
}