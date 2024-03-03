package ru.dponyashov.nnet.layer.impl;

import ru.dponyashov.nnet.neuron.Neuron;
import ru.dponyashov.nnet.neuron.impl.OutNeuron;

public class OutLayer extends AbstLayer {
    public OutLayer(int inpCount, int outCount, double lSpeed) {
        super();
        double b = getB(inpCount, outCount);
        Neuron[] neuronsTemp = new Neuron[outCount];
        for (int i = 0; i < outCount; i++) {
            neuronsTemp[i] = new OutNeuron(inpCount, b, lSpeed);
        }
        setNeurons(neuronsTemp);
    }

    public OutLayer(Neuron[] neurons){
        super();
        setNeurons(neurons);
    }
}