package ru.dponyashov.nnet.layer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dponyashov.nnet.layer.Layer;
import ru.dponyashov.nnet.neuron.Neuron;

import java.util.List;

public abstract class AbstLayer implements Layer {
    @JsonIgnore
    private Layer nextLayer;
    @JsonIgnore
    private Layer previousLayer;
    private double[] inputData;
    private Neuron[] neurons;

    public AbstLayer() {
    }

    @Override
    public double[] doAction(double[] input) {
        this.inputData = input;
        double[] output = new double[neurons.length];

        for (int i = 0; i < neurons.length; i++) {
            output[i] = neurons[i].doAction(inputData);
        }


        if (nextLayer != null) {
            return nextLayer.doAction(output);
        }
        return output;
    }

    @Override
    public double[] doTraining(double[] correctValue) {
        if (inputData == null) {
            throw new RuntimeException("Входные данные еще не определены, сделайте прямой расчет");
        }
        double[] errors = new double[inputData.length];

        for (int i = 0; i < neurons.length; i++) {
            double[] deltas = neurons[i].doTraining(inputData, correctValue[i]);
            for (int j = 0; j < deltas.length; j++) {
                errors[j] += deltas[j];
            }
        }

        if (previousLayer != null) {
            return previousLayer.doTraining(errors);
        }
        return errors;
    }


    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    public void setPreviousLayer(Layer previousLayer) {
        this.previousLayer = previousLayer;
    }

    protected void setNeurons(Neuron[] neurons) {
        this.neurons = neurons;
    }

    protected double getB(int inpCount, int outCount) {
        return 0.7 * outCount / inpCount;
    }

    private boolean ThreadsAlive(List<Thread> threads) {
        boolean isAlive = false;
        for (Thread thread : threads) {
            isAlive = isAlive || thread.isAlive();
        }
        return isAlive;
    }

//>>>>>>>>>>>>


    public Layer getPreviousLayer() {
        return previousLayer;
    }

    public Layer getNextLayer() {
        return nextLayer;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }
}
