package ru.dponyashov.nnet.layer.impl;


import ru.dponyashov.nnet.neuron.Neuron;
import ru.dponyashov.nnet.neuron.impl.InnerNeuron;

public class InnerLayer extends AbstLayer {
    public InnerLayer(int inpCount, int outCount, double lSpeed) {
        super();
        double b = getB(inpCount, outCount);
        Neuron[] neuronsTemp = new Neuron[outCount];
        for (int i = 0; i < outCount; i++) {
            neuronsTemp[i] = new InnerNeuron(inpCount, b, lSpeed);
        }
        setNeurons(neuronsTemp);
    }

    public InnerLayer(Neuron[] neurons){
        super();
        setNeurons(neurons);
    }
}
