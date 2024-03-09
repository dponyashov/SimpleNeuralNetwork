package ru.dponyashov.nnet.layer.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dponyashov.nnet.layer.Layer;
import ru.dponyashov.nnet.neuron.Neuron;

import java.util.ArrayList;
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
//MONO>>>>>>>
//        for (int i = 0; i < neurons.length; i++) {
//            double[] deltas = neurons[i].doTraining(inputData, correctValue[i]);
//            for (int j = 0; j < deltas.length; j++) {
//                errors[j] += deltas[j];
//            }
//        }
//MONO<<<<<<<<<

//MULTI  >>>>>>>>>>>
        int neuronsInTread = 100;
        int treadsCount = neurons.length / neuronsInTread;
        int remainderNeurons = neurons.length % neuronsInTread;
        double[][] deltasForTread = new double[treadsCount][];
        List<Thread> threads = new ArrayList<>();
        for(int threadIndex = 0; threadIndex < treadsCount; threadIndex++){
            int threadNumber = threadIndex;
            Runnable task = () -> {
                deltasForTread[threadNumber] = new double[errors.length];
                for(int n = 0; n < neuronsInTread; n++) {
                    int neuronNumber = n + threadNumber * neuronsInTread;
                    double[] deltas = neurons[neuronNumber].doTraining(inputData, correctValue[neuronNumber]);
                    for(int d = 0; d < deltas.length; d++){
                        deltasForTread[threadNumber][d] += deltas[d];
                    }
                }
                Thread.currentThread().interrupt();
            };
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        threadsAlive(threads);

        for (double[] doubles : deltasForTread) {
            for (int i = 0; i < doubles.length; i++) {
                errors[i] += doubles[i];
            }
        }

        for(int r = 0; r < remainderNeurons; r++){
            int neuronNumber = r + treadsCount * neuronsInTread;
            double[] deltas = neurons[r].doTraining(inputData, correctValue[neuronNumber]);
            for(int i = 0; i < deltas.length; i++) {
                errors[i] += deltas[i];
            }
        }
//MULTI  <<<<<<<<<<<<<<

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

    private void threadsAlive(List<Thread> threads) {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ignore) {}
        });
    }

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
