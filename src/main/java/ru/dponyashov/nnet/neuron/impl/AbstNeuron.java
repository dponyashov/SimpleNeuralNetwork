package ru.dponyashov.nnet.neuron.impl;

import ru.dponyashov.nnet.neuron.Neuron;

public abstract class AbstNeuron extends Neuron {
    private double[] weights;
    private double learnSpeed;
    private double shift;

    protected AbstNeuron(int inputs, double mB, double lSpeed) {
        learnSpeed = lSpeed;
        weights = new double[inputs];
        shift = Math.random() * mB * 2 - mB;
        double w = 0.0;
        for (int i = 0; i < inputs; i++) {
            weights[i] = Math.random() - 0.5;
            w += weights[i] * weights[i];
        }
        for (int i = 0; i < inputs; i++) {
            weights[i] = weights[i] * mB / Math.sqrt(w);
        }
    }

    protected AbstNeuron(double[] weights, double shift, double learnSpeed) {
        this.weights = weights;
        this.learnSpeed = shift;
        this.shift =learnSpeed;
    }

    @Override
    public double doAction(double[] input) {
        return lineFunction(shift + getWeightInputSum(input));
    }

    @Override
    public double[] doTraining(double[] input, double correctValue) {
        double[] deltas = new double[input.length];
        double sumInput = shift + getWeightInputSum(input);
        double delta = getDelta(correctValue, sumInput);
        for (int i = 0; i < deltas.length; i++) {
            deltas[i] = weights[i] * delta;
        }
        reWeight(delta, input);
        return deltas;
    }

    protected abstract double getDelta(double correctValue, double sumInput);

    protected double lineFunction(double input) {
        return 1 / (1 + Math.exp(-1 * input));
    }

    protected double backFunction(double input) {
        return lineFunction(input) * Math.abs(1 - lineFunction(input));
    }

    private void reWeight(double delta, double[] input) {
        shift += learnSpeed * delta;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += input[i] * delta * learnSpeed;
        }
    }

    private double getWeightInputSum(double[] input) {
        double weightInputSum = 0;
        for (int i = 0; i < input.length; i++) {
            weightInputSum += input[i] * weights[i];
        }
        return weightInputSum;
    }

//>>>>>>>>>>

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getLearnSpeed() {
        return learnSpeed;
    }

    public void setLearnSpeed(double learnSpeed) {
        this.learnSpeed = learnSpeed;
    }

    public double getShift() {
        return shift;
    }

    public void setShift(double shift) {
        this.shift = shift;
    }
}