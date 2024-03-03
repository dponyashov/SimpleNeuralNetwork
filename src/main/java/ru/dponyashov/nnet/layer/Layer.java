package ru.dponyashov.nnet.layer;

public interface Layer {
    double[] doAction(double[] input);

    double[] doTraining(double[] correctValue);

    void setNextLayer(Layer nextLayer);

    void setPreviousLayer(Layer previousLayer);
}
