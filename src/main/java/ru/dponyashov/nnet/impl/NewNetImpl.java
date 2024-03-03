package ru.dponyashov.nnet.impl;

import ru.dponyashov.nnet.NewNet;
import ru.dponyashov.nnet.layer.Layer;
import ru.dponyashov.nnet.layer.impl.InnerLayer;
import ru.dponyashov.nnet.layer.impl.OutLayer;

public class NewNetImpl implements NewNet {
    private static final double SPEED_TRAINING = 0.1;
    private Layer inputLayer;
    private Layer outputLayer;
    public NewNetImpl(int inputs, int[] neuronsInInnerLayers, int outputs) {
        if (neuronsInInnerLayers.length < 1) {
            generateNetWithOutputLayerOnly(inputs, outputs);
        } else {
            generateNetWithManyInnerLayers(inputs, neuronsInInnerLayers, outputs);
        }
    }

    public NewNetImpl(Layer inputLayer, Layer outputLayer) {
        if(inputLayer == null) {
            this.inputLayer = outputLayer;
        } else {
            this.inputLayer = inputLayer;
        }
        this.outputLayer = outputLayer;
    }

    @Override
    public double[] doAction(double[] input) {
        return inputLayer.doAction(input);
    }

    @Override
    public double[] doTraining(double[] input, double[] correctValue) {
        doAction(input);
        return outputLayer.doTraining(correctValue);
    }

    private void generateNetWithOutputLayerOnly(int inputs, int outputs) {
        inputLayer = new OutLayer(inputs, outputs, SPEED_TRAINING);
        outputLayer = inputLayer;
    }

    private void generateNetWithManyInnerLayers(int inputs, int[] neuronsInInnerLayers, int outputs) {
        inputLayer = new InnerLayer(inputs, neuronsInInnerLayers[0], SPEED_TRAINING);
        Layer prevLayer = inputLayer;
        for (int i = 1; i < neuronsInInnerLayers.length; i++) {
            Layer nextLayer = new InnerLayer(neuronsInInnerLayers[i - 1], neuronsInInnerLayers[i], SPEED_TRAINING);
            nextLayer.setPreviousLayer(prevLayer);
            prevLayer.setNextLayer(nextLayer);
            prevLayer = nextLayer;
        }
        outputLayer = new OutLayer(neuronsInInnerLayers[neuronsInInnerLayers.length - 1], outputs, SPEED_TRAINING);
        outputLayer.setPreviousLayer(prevLayer);
        prevLayer.setNextLayer(outputLayer);
    }

    public Layer getInputLayer() {
        return inputLayer;
    }
}